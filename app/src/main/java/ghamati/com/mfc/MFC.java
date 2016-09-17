package ghamati.com.mfc;

import android.content.Context;

import ghamati.com.mfc.service.RestKit;

/**
 * Created by khashayar on 9/17/16.
 */
public class MFC {

    static class InstanceHolder{
        static MFC mfc=new MFC();
    }

    public static synchronized MFC getInstance(Context context){
        ContextHolder.getInstance().init(context);
        SharedPreferencesKit.getInstance().init();

        return InstanceHolder.mfc;
    }

    public void setServerBaseURL(String baseURL){
        RestKit.getInstance().init();
        RestKit.getInstance().setBaseURL(baseURL);
    }

}
