package ghamati.com.mfc.service;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.util.Map;

import ghamati.com.mfc.ContextHolder;
import ghamati.com.mfc.service.toolbox.GsonRequest;
import ghamati.com.mfc.service.toolbox.GsonRequestListener;


/**
 * Created by khashayar on 2014-12-07.
 */
public class RestKit {

    private String baseURL = "";
    private RequestQueue mRequestQueue;


    static class INSTANCEHolder{
        public static RestKit INSTANCE=new RestKit();
    }

    public static synchronized RestKit getInstance(){
        return INSTANCEHolder.INSTANCE;
    }



    public void init(){

        File cacheDIR=new File(ContextHolder.getInstance().getContext().getCacheDir(),"cahe_dir_define");

        Cache cache =new DiskBasedCache(cacheDIR,1024 * 1024); //1 MB

        Network network = new BasicNetwork(new HurlStack());

        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();

    }

    public void setBaseURL(String baseURL){
        this.baseURL=baseURL;

    }

    public String getURLForMethod(String method) {
        return baseURL + Uri.encode(method, "@#&=*+-_.,:!?()/~'%");
    }


    public<T> void Put(String method, final Map<String, String > params, final Class<T> clazz,
                       final GsonRequestListener<T> listener) {

        this.GsonRequest(Request.Method.PUT,getURLForMethod(method),params,clazz,listener);
    }

    public<T> void Post(String method, final Map<String, Object> params, final Class<T> clazz,
                        final GsonRequestListener<T> listener) {


        JSONObject jsonObject=new JSONObject(params);

        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,getURLForMethod(method),jsonObject,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                listener.onResponseSuccess(gson.fromJson(String.valueOf(response),clazz),String.valueOf(response));
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onResponseError(error);
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        mRequestQueue.add(jsonRequest);


    }


    public <T> void Get(String method,final Class<T> clazz,
                        final GsonRequestListener<T> listener){

        this.GsonRequest(Request.Method.GET,getURLForMethod(method),null,clazz,listener);
    }



    public <T> void GsonRequest(int method, String url, Map<String, String> params, Class<T> clazz,
                                final GsonRequestListener<T> listener) {
        GsonRequest<T> request = new GsonRequest<T>
                (method, url, clazz, params, listener, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (listener != null){
                            listener.onResponseError(error);
                        }
                    }
                });
        if(mRequestQueue!=null){
            request.setShouldCache(false);
            mRequestQueue.add(request);
        }
    }


}
