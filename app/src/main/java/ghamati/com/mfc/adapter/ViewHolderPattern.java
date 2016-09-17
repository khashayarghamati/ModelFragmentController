package ghamati.com.mfc.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ghamati.com.mfc.fragment.listener.ItemListener;


/**
 * Created by khashayar on 7/22/15.
 */
public class ViewHolderPattern extends RecyclerView.ViewHolder
        implements View.OnClickListener,View.OnLongClickListener {

    private ItemListener listener=null;
    private CoreRowInterface coreRowInterface;

    public void grabChildren(){
        if(coreRowInterface!=null){
            coreRowInterface.grabChildren();
        }
    }
    public void prepareWithParams(CoreRowData params){
        if(coreRowInterface!=null){
            coreRowInterface.prepareWithParams(params);
        }
    }
    public void applyRowStyles(){
        if(coreRowInterface!=null){
            coreRowInterface.applyRowStyles();
        }
    }
    public void createViewHolder(View view){
        if(coreRowInterface!=null){
            coreRowInterface.createViewHolder(view);
        }
    }
    public RecyclerView.ViewHolder getViewHolderInstance(){
        if(coreRowInterface!=null){
            return coreRowInterface.getViewHolderInstance();
        }
        return null;
    }



    public ViewHolderPattern(@NonNull View itemView, @NonNull CoreRowInterface coreRowInterface) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.coreRowInterface=coreRowInterface;
    }

    public void RegisterListener(@NonNull ItemListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onItemClick(view,getPosition());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if(listener!=null){
            listener.onLongItemClickListener(view, getPosition());
            return true;
        }
        return false;
    }
}
