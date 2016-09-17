package ghamati.com.mfc.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ghamati.com.mfc.BuildConfig;
import ghamati.com.mfc.ContextHolder;
import ghamati.com.mfc.R;

/**
 * Created by khashayar on 9/12/16.
 */
public class CoreDB extends SQLiteOpenHelper {




    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;

    private HashMap<String,Integer> table;
    private String tableName=null;

    public CoreDB() {
        super(ContextHolder.getInstance().getContext(), createDBName(ContextHolder.getInstance().getContext()), null, DATABASE_VERSION);
    }


    public void setTableInfo(HashMap<String,Integer> tableColumen,String tableName){
        this.table=tableColumen;
        this.tableName=tableName;
    }


    public void onCreate(SQLiteDatabase db) {

        if(tableName!=null) {
            createTable();
        }
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over

        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private static String createDBName(Context context){
        int stringId = context.getApplicationInfo().labelRes;
        return context.getString(stringId)+".db";
    }

  
    public boolean isTableExists(String tableName) {

        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public long getTableRecordCount(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, tableName);
        db.close();
        return cnt;
    }

    public void insert(String tblName,ContentValues values){
        if(isTableExists(tblName)) {
            SQLiteDatabase db = getWritableDatabase();
            db.insert(tblName, null, values);
            db.close();
        }


    }

    public Cursor select(String query){
        SQLiteDatabase db=getReadableDatabase();
        return db.rawQuery(query,null);
    }

    public void createTable(){
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + tableName);

        String query = "CREATE TABLE " + tableName + "(";


        int i = 0;
        for (String statement : table.keySet()) {
            switch (table.get(statement)) {
                case AttrType.DbType.INTEGER_NULLABLE: {
                    query += statement + " INTEGER NOT NULL";
                }
                break;

                case AttrType.DbType.INTEGER_NOT_NULL: {
                    query += statement + " INTEGER";
                }
                break;

                case AttrType.DbType.INTEGER_NOT_NULL_PRIMARY: {
                    query += statement + " INTEGER PRIMARY KEY NOT NULL";
                }
                break;

                case AttrType.DbType.INTEGER_NOT_NULL_PRIMARY_AUTOINCREMENT: {
                    query += statement + " INTEGER AUTOINCREMENT PRIMARY KEY NOT NULL ";
                }
                break;

                case AttrType.DbType.TEXT_NULLABLE: {
                    query += statement + " TEXT NOT NULL";
                }
                break;

                case AttrType.DbType.TEXT_NOT_NULL: {
                    query += statement + " TEXT";
                }
                break;

                case AttrType.DbType.TEXT_NOT_NULL_PRIMARY: {
                    query += statement + " TEXT PRIMARY KEY NOT NULL";
                }
                break;
            }

            if (++i < table.size()) {
                query += " , ";
            } else {
                query += ");";
            }
        }
        db.execSQL(query);
    }
}