package ghamati.com.mfc.controller;

import android.content.ContentValues;
import android.database.Cursor;

import com.android.volley.VolleyError;

import org.chalup.microorm.MicroOrm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ghamati.com.mfc.SharedPreferencesKit;
import ghamati.com.mfc.database.CoreDB;
import ghamati.com.mfc.database.CoreTableInfo;
import ghamati.com.mfc.observer.DataObserver;
import ghamati.com.mfc.service.RestKit;
import ghamati.com.mfc.service.toolbox.GsonRequestListener;

/**
 * Created by khashayar on 9/12/16.
 */
public abstract class CoreController<T> {


    String route;

    List<DataObserver> observers;



    boolean hasDataBase;

    protected CoreDB coreDB;
    private CoreTableInfo coreTableInfo;
    private ContentValues values;
    public CoreController(){


        Class<T> tClass=(Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];


        try {
            coreTableInfo=(CoreTableInfo) tClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }



        this.route= setRoute();
        this.hasDataBase=hasDataBase();

        observers=new ArrayList<>();
    }

    public abstract void response(T data);
    public abstract void notifyObservers(List<DataObserver> observers);
    public abstract String setRoute();
    public abstract boolean hasDataBase();



    protected void setTimeStamp(String timeStamp) {
        SharedPreferencesKit.getInstance().setString(coreTableInfo.getSubClassName(),timeStamp);

    }

    protected String getTimeStamp(){
        return SharedPreferencesKit.getInstance().getString(coreTableInfo.getSubClassName());
    }


    protected void checkDataType(final Class<T> aclass){
        if (getTimeStamp().isEmpty() || !hasDataBase) {
            RestKit
                    .getInstance()
                    .Get(route, aclass, new GsonRequestListener<T>() {
                        @Override
                        public void onResponseSuccess(T response,String json) {

                            if (hasDataBase && coreTableInfo != null) {
                                coreDB=new CoreDB();

                                if (!coreDB.isTableExists(coreTableInfo.getSubClassName())) {
                                    coreDB.setTableInfo(coreTableInfo.generateColumens(), coreTableInfo.getSubClassName());
                                    coreDB.createTable();
                                }

                                setTimeStamp(String.valueOf(System.currentTimeMillis()/1000));
                                updateDB(json);
                            }
                            response(response);
                            notifyObservers(observers);
                        }


                        @Override
                        public void onResponseError(VolleyError error) {
                            // TODO: 9/17/16 Print Error

                        }
                    });

        }else {
            String query="SELECT * FROM "+coreTableInfo.getSubClassName();
            Cursor cursor=coreDB.select(query);
            MicroOrm uOrm = new MicroOrm();
            response(uOrm.fromCursor(cursor,aclass));
        }
    }


    private void parseJsonArray(JSONArray jsonArray,String colName,String anotationName,String fieldType){

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jo = jsonArray.getJSONObject(i);
                parseJsonObject(jo,colName,anotationName,fieldType);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseJsonObject(JSONObject jsonObject,String colName,String annotationName,String fieldType){

        if(values==null){
            values=new ContentValues();
        }

        try {

            if (fieldType.equals("Number")) {
                values.put(colName, (Double) jsonObject.get(annotationName!=null ? annotationName:colName));
            } else {
                values.put(colName, (String) jsonObject.get(annotationName!=null ? annotationName:colName));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void updateDB(final String json){
        new Thread(new Runnable() {
            @Override
            public void run() {


                Field[] fields = coreTableInfo.getReflection().getFields();

                for (Field field : fields) {

                    Annotation[] annotations = field.getAnnotations();

                    if (annotations.length > 0) {
                        for (Annotation annotation : annotations) {
                            Class<? extends Annotation> type = annotation.annotationType();

                            for (Method method : type.getDeclaredMethods()) {
                                Object value = null;
                                try {
                                    value = method.invoke(annotation, (Object[]) null);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                                if (method.getName().equals("value") &&
                                        annotation.annotationType().getName().equals("com.google.gson.annotations.SerializedName")) {

                                    Object jsonObj = null;
                                    try {
                                        jsonObj = new JSONTokener(json).nextValue();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (jsonObj instanceof JSONObject) {
                                        parseJsonObject((JSONObject) jsonObj,field.getName(),String.valueOf(value),
                                                field.getType().getName().equals(String.class.getName())? "String":"Number");

                                    } else {
                                        if (jsonObj instanceof JSONArray) {
                                            JSONArray jsonArray = (JSONArray) jsonObj;
                                            parseJsonArray(jsonArray,field.getName(),String.valueOf(value),
                                                    field.getType().getName().equals(String.class.getName())? "String":"Number");
                                        }
                                    }
                                }

                            }

                        }
                    }else {
                        Object jsonObj = null;
                        try {
                            jsonObj = new JSONTokener(json).nextValue();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (jsonObj instanceof JSONObject) {
                            parseJsonObject((JSONObject) jsonObj,field.getName(),null,
                                    field.getType().getName().equals(String.class.getName())? "String":"Number");

                        } else {
                            if (jsonObj instanceof JSONArray) {
                                JSONArray jsonArray = (JSONArray) jsonObj;
                                parseJsonArray(jsonArray,field.getName(),null,
                                        field.getType().getName().equals(String.class.getName())? "String":"Number");
                            }
                        }
                    }

                }
            }
        }).start();
    }


    protected void attachObserver(DataObserver observer){
        observers.add(observer);
    }

    protected void detachObserver(DataObserver observer){
        observers.remove(observer);
    }

}
