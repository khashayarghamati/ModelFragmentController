package ghamati.com.mfc;

import android.content.Context;

/**
 * Created by khashayar on 9/12/16.
 */
public class ContextHolder {

    private Context context;

    static class InstanceHolder{
        static ContextHolder Instance=new ContextHolder();
    }

    public static synchronized ContextHolder getInstance(){
        return InstanceHolder.Instance;
    }

    private ContextHolder(){}


    public void init(Context context){
        this.context=context;
    }

    public Context getContext(){
        return context;
    }
}
