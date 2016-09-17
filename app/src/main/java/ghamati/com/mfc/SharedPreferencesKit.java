package ghamati.com.mfc;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by khashayar on 12/27/15.
 */
public class SharedPreferencesKit {

    private SharedPreferences mSharedPreferences;

    private SharedPreferencesKit(){}

    static class InstanceHolder{
        static  SharedPreferencesKit INSTANCE=new SharedPreferencesKit();
    }

    public static synchronized SharedPreferencesKit getInstance(){
        return InstanceHolder.INSTANCE;
    }

    public void init(){
        Context context = ContextHolder.getInstance().getContext();
        mSharedPreferences = context.getSharedPreferences(createName(context), Context.MODE_PRIVATE);
    }

    public void setString(String key,String value){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public void setBoolean(String key,Boolean value){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public Boolean getBoolean(String key){
        return mSharedPreferences.getBoolean(key,false);
    }

    public String getString(String key){
        return mSharedPreferences.getString(key,"");

    }

    private static String createName(Context context){
        int stringId = context.getApplicationInfo().labelRes;
        return context.getString(stringId)+".spk";
    }

}
