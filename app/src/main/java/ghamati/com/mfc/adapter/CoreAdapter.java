package ghamati.com.mfc.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ghamati.com.mfc.fragment.listener.ItemListener;


/**
 * Created by khashayar on 7/21/15.
 */
public class CoreAdapter extends RecyclerView.Adapter<ViewHolderPattern> {

    private ArrayList<CoreRowData> coreRowData;
    private ItemListener listener=null;

    public CoreAdapter(@NonNull ArrayList<CoreRowData> coreRowDatas, @NonNull ItemListener listener){

        this.coreRowData =coreRowDatas;
        this.listener=listener;
    }


    @Override
    public ViewHolderPattern onCreateViewHolder(ViewGroup viewGroup, int i) {
        CoreRowData row_data = getRowType(i);

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        CoreRowInterface coreRowInterface;
        coreRowInterface = (CoreRowInterface) inflater.inflate(row_data.identifier, viewGroup, false);
        coreRowInterface.createViewHolder((View) coreRowInterface);

        coreRowInterface.getViewHolderInstance().RegisterListener(listener!=null ? listener : null );

        return coreRowInterface.getViewHolderInstance();
    }

    @Override
    public void onBindViewHolder(ViewHolderPattern viewHolderCore, int i) {
        viewHolderCore.grabChildren();
        viewHolderCore.prepareWithParams(getRowType(i));
        viewHolderCore.applyRowStyles();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return coreRowData.size();
    }

    private CoreRowData getRowType(int i){
        return coreRowData.get(i);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public void setCoreRowData(ArrayList<CoreRowData> coreRowData){
        this.coreRowData = coreRowData;
        notifyDataSetChanged();
    }

}
