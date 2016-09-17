package ghamati.com.mfc.database;

import android.provider.BaseColumns;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;

import ghamati.com.mfc.database.datatype.IntegerField;

/**
 * Created by khashayar on 9/12/16.
 */
public abstract class CoreTableInfo implements BaseColumns{

    String subClassName;



    Class<?> models;

    String[] columnsName;

    public CoreTableInfo(){

        subClassName=classReflection().getClass().getName();
        models=classReflection();

    }


    protected abstract Class<?> classReflection();

    public HashMap<String,Integer> generateColumens(){

        HashMap<String,Integer>hashMap=new HashMap<>();

        Field[] fields=models.getFields();

        columnsName=new String[fields.length];

        for(int i=0;i<fields.length;i++){

            int type=-1;

            if (fields[i].getType().getName().equals(Number.class.getName())) {
                try {

                    Object object= null;
                    try {
                        object = fields[i].get(models.newInstance());
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }

                    if (object != null) {
                        type=((IntegerField)object).intValue();
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }else {
                if (fields[i].getType().getName().equals(String.class.getName())) {

                    try {

                        Object object= null;
                        try {
                            object = fields[i].get(models.newInstance());
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }

                        if (object != null) {
                            String s=(String) object;
                            String[] strings=s.split("-");
                            if(strings[1].equals("StringField")) {
                                type=Integer.valueOf(strings[0]);
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

            if(type!=-1) {
                columnsName[i]=fields[i].getName();
                hashMap.put(fields[i].getName(), type);
            }
        }

        return hashMap;
    }


    public String[] getColumnsName(){
        return columnsName;
    }


    public String getSubClassName() {
        return subClassName;
    }

    public Annotation[] getAnnotations(){
        return models.getDeclaredAnnotations();
    }

    public Class<?> getReflection() {
        return models;
    }
}
