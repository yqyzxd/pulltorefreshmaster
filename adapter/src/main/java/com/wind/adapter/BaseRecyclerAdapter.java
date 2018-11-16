package com.wind.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wind on 2018/1/2.
 */

public abstract class BaseRecyclerAdapter<T,H extends  RecyclerView.ViewHolder> extends RecyclerView.Adapter<H> {

    protected List<T> items;
    protected Activity mActivity;
    private int mLayoutRes;
    public BaseRecyclerAdapter(Activity activity, int layoutRes){
        this.mActivity=activity;
        this.items=new ArrayList<>();
        this.mLayoutRes=layoutRes;
    }
    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mActivity.getLayoutInflater().inflate(mLayoutRes,parent,false);
        return  onCreateViewHolder(view);
    }
    public abstract H onCreateViewHolder(View v);
    public T getItem(int position){
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void replace(List<T> items){
        this.items.clear();
        addAll(items);
    }
    public void addAll(List<T> items){
        this.items.addAll(items);
        notifyDataSetChanged();
    }

}
