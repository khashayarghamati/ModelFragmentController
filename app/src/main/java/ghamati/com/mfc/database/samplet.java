package ghamati.com.mfc.database;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.util.HashMap;

import ghamati.com.mfc.database.datatype.IntegerField;
import ghamati.com.mfc.database.datatype.StringField;

/**
 * Created by khashayar on 9/12/16.
 */
public class samplet extends CoreTableInfo {

    public Number integerDataType=IntegerField.getSpecifyType(true,false,false);
    public String str = StringField.getSpecifyType(false,false);


    @Override
    public Class<?> classReflection() {
        return getClass();
    }
}
