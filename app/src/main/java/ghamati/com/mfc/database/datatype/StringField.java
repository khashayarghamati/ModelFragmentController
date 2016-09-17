package ghamati.com.mfc.database.datatype;

import ghamati.com.mfc.database.AttrType;

/**
 * Created by khashayar on 9/16/16.
 */
public class StringField {
    int type;


    public static synchronized String getSpecifyType(boolean isNullAble,boolean isPrimaryKey){
        int type= AttrType.DbType.TEXT_NULLABLE;

        if(isPrimaryKey && !isNullAble){
            type= AttrType.DbType.TEXT_NOT_NULL_PRIMARY;
        }else {
            if(!isNullAble){
                type= AttrType.DbType.TEXT_NOT_NULL;
            }

        }



        StringField instance=getNewInstance();
        instance.setType(type);
        return type+"-StringField";
    }

    public static StringField getNewInstance(){
        return new StringField();
    }

    public void setType(int type) {
        this.type = type;
    }

}
