package ghamati.com.mfc.service.toolbox;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by khashayar on 7/8/15.
 */
public class GsonRequest<T> extends Request<T> {


    private final Class<T> clazz;
    private Map<String, String> param = null;
    private final GsonRequestListener<T> Listener;
    private Request.Priority Priority;

    private String json;

    public GsonRequest(int method,String url, Class<T> clazz, Map<String, String> param,
                       GsonRequestListener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);

        setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        this.clazz = clazz;
        this.param = param;
        this.Listener = listener;
        this.Priority = Request.Priority.NORMAL;
        this.setShouldCache(true);
    }



    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {

        if (volleyError.networkResponse != null) {
            switch (volleyError.networkResponse.statusCode) {
                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                case HttpURLConnection.HTTP_BAD_GATEWAY:
                case HttpURLConnection.HTTP_UNAVAILABLE:
                case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
                case HttpURLConnection.HTTP_BAD_REQUEST:
                    System.out.println("SERVICE ERROR "+volleyError.networkResponse.headers +" " +volleyError.networkResponse.data +" " + volleyError.networkResponse.networkTimeMs);

                    break;
            }
        } else {
            if (volleyError.getClass().equals(TimeoutError.class)) {
                System.out.println("SERVICE ERROR "+"TIMEOUT ERROR");
            }
        }

        return volleyError;
    }

    @Override
    public Request.Priority getPriority() {
        return this.Priority;
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        return new DefaultRetryPolicy();
    }


    @Override
     public Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = param != null ? param : new Hashtable<String, String>();
        return params;
    }




//    @Override
//    public String getBodyContentType() {
//        return "application/json";
//    }


//    @Override
//    public Map<String,String> getHeaders() {
//        Map<String,String > stringStringMap=new HashMap<>();
//        stringStringMap.put("Content-Type", "application/json");
//        return stringStringMap;
//    }
    @Override
    protected void deliverResponse(T response) {
        Listener.onResponseSuccess(response,json);
    }



    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
             json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            Gson gson = new Gson();

            return Response.success(
                    gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }


}
