package ghamati.com.mfc.service.toolbox;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

/**
 * Created by khashayar on 7/8/15.
 */
public interface GsonRequestListener<T> {
    void onResponseSuccess(T response,String json);

    void onResponseError(VolleyError error);
}
