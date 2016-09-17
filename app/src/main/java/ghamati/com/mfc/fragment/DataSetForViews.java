package ghamati.com.mfc.fragment;

import android.view.View;

/**
 * Created by khashayar on 10/28/15.
 */
public interface DataSetForViews {
    void initViewChildren(View view);
    void prepareViewChildren();
    void applyRowStyles();
}
