package ghamati.com.mfc.adapter;

/**
 * Created by khashayar on 2014-12-06.
 */
public abstract class CoreRowData {

    public int rowID;
    public int identifier;


    public CoreRowData(int id, int identifier) {
        rowID = id;
        this.identifier = identifier;


    }



}
