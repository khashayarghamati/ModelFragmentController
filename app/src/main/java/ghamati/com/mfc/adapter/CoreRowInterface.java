package ghamati.com.mfc.adapter;

import android.view.View;

/**
 * Created by khashayar on 2014-12-06.
 */
public interface CoreRowInterface {

    void grabChildren();

    void prepareWithParams(CoreRowData params);

    void applyRowStyles();

    void createViewHolder(View view);

    ViewHolderPattern getViewHolderInstance();


}
