package ghamati.com.mfc.database;

/**
 * Created by khashayar on 9/16/16.
 */
public class AttrType {

     public static class DbType{

         public static final int INTEGER_NULLABLE=0;
         public static final int INTEGER_NOT_NULL=1;
         public static final int INTEGER_NOT_NULL_PRIMARY =2;
         public static final int INTEGER_NOT_NULL_PRIMARY_AUTOINCREMENT =3;

         public static final int TEXT_NULLABLE=4;
         public static final int TEXT_NOT_NULL=5;
         public static final int TEXT_NOT_NULL_PRIMARY =6;
    }
}
