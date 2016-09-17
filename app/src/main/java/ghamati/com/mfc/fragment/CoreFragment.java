package ghamati.com.mfc.fragment;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import ghamati.com.mfc.adapter.CoreAdapter;
import ghamati.com.mfc.adapter.CoreRowData;
import ghamati.com.mfc.fragment.listener.ItemListener;


/**
 * Created by khashayar on 6/8/15.
 */
public abstract class CoreFragment extends Fragment
        implements ItemListener {

    protected ArrayList<CoreRowData> mRowsDataCore = new ArrayList<>();
    protected CoreAdapter _Adapter;
    protected RecyclerView _RecyclerView;

    private boolean isHorizontal =false;
    private boolean isScrollAble =false;
    private boolean isList=true;
    private boolean isGrid=false;
    private int     cols;
    private String identifier = null;
    private DataSetForViews dataSetForViews=null;


    /**
     *
     *Call On onCreate()
     * use for init Your Row , set RecyclerView Scroll Orientation , RecyclerView Scroll State or etc
     *
     */
    public abstract void pickRowData();

    /**
     *
     * Call On onCreateView()
     *if you want get Data From Server so You can use This Method
     *
     */
    public abstract void loadData();

    public abstract void onItemClickListener(View view, int i);


    /**
     *
     * if you want Change your Row Style such as Row Background , Row Text Font or etc you can use this
     * for any Row it call
     *
     */
    public abstract void changeLayoutStyle();

    /**
     * 
     * it call on RecyclerView Scroll Listener 
     * @param isDownScroll
     */
    public abstract void onRefresh(boolean isDownScroll);

    /**
     * 
     * call on onDestroyView()
     * 
     */
    public abstract void DestroyState();

    /**
     * 
     *call on onPause()
     * 
     */
    public abstract void pauseState();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pickRowData();

        _Adapter = new CoreAdapter(mRowsDataCore, this);

    }



    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {


        if(isList) {
            if (_RecyclerView == null) {
                _RecyclerView = new RecyclerView(getActivity());
                _RecyclerView.setHasFixedSize(true);

                if (isHorizontal) {

                    if (isScrollAble) {
                        _RecyclerView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
                        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
                        _RecyclerView.setLayoutManager(layoutManager);
                    } else {
                        _RecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
                        CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(getContext());
                        _RecyclerView.setLayoutManager(layoutManager);
                    }


                    _RecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        /**
                         * Callback method to be invoked when RecyclerView's scroll state changes.
                         *
                         * @param recyclerView The RecyclerView whose scroll state has changed.
                         * @param newState     The updated scroll state. One of ,
                         *                      or .
                         */
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                        }

                        /**
                         * Callback method to be invoked when the RecyclerView has been scrolled. This will be
                         * called after the scroll has completed.
                         * <p/>
                         * This callback will also be called if visible item range changes after a layout
                         * calculation. In that case, dx and dy will be 0.
                         *
                         * @param recyclerView The RecyclerView which scrolled.
                         * @param dx           The amount of horizontal scroll.
                         * @param dy           The amount of vertical scroll.
                         */
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            if (!recyclerView.canScrollHorizontally(-1)) {
                                onRefresh(true);
                            } else if (!recyclerView.canScrollHorizontally(1)) {
                                onRefresh(false);
                            } else if (dy < 0) {
                                onRefresh(false);
                            } else if (dy > 0) {
                                onRefresh(true);
                                
                            }
                        }
                    });


                } else {
                    if(isGrid){
                        RecyclerView.LayoutManager mLayoutManager=new GridLayoutManager(getContext(),cols);
                        _RecyclerView.setLayoutManager(mLayoutManager);
                    }
                }

                if (_Adapter != null) {
                    _RecyclerView.setAdapter(_Adapter);
                }
            }
            loadData();
            return _RecyclerView;
        }else {
                if (identifier != null) {
                    View localView = inflater.inflate(Integer.parseInt(identifier), container, false);
                    WeakReference<View> _View = new WeakReference<>(localView);

                    if (_View.get() != null && dataSetForViews != null) {
                        dataSetForViews.initViewChildren(_View.get());
                        dataSetForViews.prepareViewChildren();
                        dataSetForViews.applyRowStyles();

                        loadData();
                        return _View.get();
                    }
                }

            return null;
        }
    }

    public void set_Adapter(){
        if(mRowsDataCore.size()>0) {

            if(_Adapter!=null){
                _Adapter.setCoreRowData(mRowsDataCore);
            }else {
                _Adapter=new CoreAdapter(mRowsDataCore,this);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _RecyclerView = null;

        DestroyState();
    }

    @Override
    public void onPause(){
        super.onPause();

        pauseState();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        _Adapter = null;

    }

    @Override
    public void onResume(){
        super.onResume();
        this.changeLayoutStyle();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    /**
     *
     *
     * use for set RecyclerView Orientation
     *
     * @param isHorizontal
     */
    public void setRecyclerViewOrientation(boolean isHorizontal){
        this.isHorizontal =isHorizontal;
    }


    /**
     *
     * use for set Disable RecyclerView Scroll
     *
     * @param isScrollAble
     */
    public void setIsScrollAble(boolean isScrollAble){
        this.isScrollAble =isScrollAble;
    }


    public int[] getDisplaySize(){
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int[] sizes = {size.x,size.y};
        return sizes;
    }

    @Override
    public void onItemClick(View view, int i) {
        this.onItemClickListener(view, i);
    }


    /**
     *
     * False == Don't Use RecyclerView (You Should set Layout ID by setIdentifier)
     *
     * @param isList    (Default is True)
     */
    public void setIsList( boolean isList){
        this.isList=isList;
    }

    /**
     *
     *
     * it use when you want use GridLayout
     *
     * @param isGrid
     * @param cols
     */
    public void setIsGrid(boolean isGrid,int cols){
        this.isGrid=isGrid;
        this.cols=cols;
    }


    /**
     * When You Don't Want Use RecyclerView set Your Layout
     *
     * @param identifier
     */
    public void setIdentifier(String identifier){
        this.identifier =identifier;
    }

    /**
     *
     * When You Don't Want Use RecyclerView you should implements DataSetForViews
     * in your sub Fragment and set it Reference by This method
     *
     * @param dataSetForViews
     */
    public void setDataSetForViews(DataSetForViews dataSetForViews){
        this.dataSetForViews=dataSetForViews;
    }
}
