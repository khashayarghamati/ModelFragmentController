package ghamati.com.mfc.database.datatype;

import ghamati.com.mfc.database.AttrType;

/**
 * Created by khashayar on 9/16/16.
 */
public class IntegerField extends Number {

    int type;

    @Override
    public int intValue() {
        return type;
    }

    @Override
    public long longValue() {
        return type;
    }

    @Override
    public float floatValue() {
        return type;
    }

    @Override
    public double doubleValue() {
        return type;
    }

    public static synchronized IntegerField getSpecifyType(boolean isNullAble,boolean isPrimaryKey,
                                                           boolean isAutoIncrement){
        int type= AttrType.DbType.INTEGER_NULLABLE;

        if(isPrimaryKey && isAutoIncrement && !isNullAble){
            type= AttrType.DbType.INTEGER_NOT_NULL_PRIMARY_AUTOINCREMENT;
        }else {
            if (isPrimaryKey && !isNullAble){
                type= AttrType.DbType.INTEGER_NOT_NULL_PRIMARY;
            }else {
                if(!isNullAble){
                    type= AttrType.DbType.INTEGER_NOT_NULL;
                }
            }
        }

        IntegerField instance=getNewInstance();
        instance.setType(type);
        return instance;
    }

    public static IntegerField getNewInstance(){
        return new IntegerField();
    }

    public void setType(int type) {
        this.type = type;
    }
}
