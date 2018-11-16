package com.wind.pulltorefresh;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.wind.pulltorefresh.RefreshState.STATE_COMPLETE;
import static com.wind.pulltorefresh.RefreshState.STATE_ERROR;
import static com.wind.pulltorefresh.RefreshState.STATE_LOADING;


public class RefreshableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int POSITION_REFRESHABLE = Integer.MAX_VALUE;
    private RecyclerView.Adapter mRealAdapter;
    public RefreshState mState;
    private boolean mShowLoadMoreItem;
    public RefreshableAdapter(RecyclerView.Adapter realAdapter) {
        this.mRealAdapter = realAdapter;
        mState=STATE_COMPLETE;
    }

    public RecyclerView.Adapter getRealAdapter() {
        return mRealAdapter;
    }

    @Override
    public int getItemViewType(int position) {
        int type;

        if (position == getItemCount() - 1&& mShowLoadMoreItem) {
            type= POSITION_REFRESHABLE;
        }else {
            type= mRealAdapter.getItemViewType(position);
        }
        return type;

    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemViewType) {
        RecyclerView.ViewHolder vh;
        switch (itemViewType) {
            case POSITION_REFRESHABLE:
                vh= onCreateViewHolder(viewGroup);
                break;
            default:
                vh= mRealAdapter.onCreateViewHolder(viewGroup, itemViewType);
            break;
        }
        return vh;
    }

    private RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        View itemView=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_refreshable,viewGroup,false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof RefreshableAdapter.ViewHolder){
            onBindViewHolder(viewHolder);
        }else {
            mRealAdapter.onBindViewHolder(viewHolder, position);
        }
        /*int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case POSITION_REFRESHABLE:
                 onBindViewHolder(viewHolder);
                break;
            default:
                mRealAdapter.onBindViewHolder(viewHolder, position);
                break;
        }*/
    }

    private void onBindViewHolder(RecyclerView.ViewHolder viewHolder) {
        ViewHolder vh= (ViewHolder) viewHolder;
        String text="";
        switch (mState){
            case STATE_LOADING:
                text="正在加载...";
                break;
            case STATE_ERROR:
                text="请检查网络";
                break;
            case STATE_COMPLETE:
                text="加载完成";
                break;
        }
        vh.tv_state.setText(text);

    }


    public void showLoading(){
        mState=STATE_LOADING;
        notifyItemChanged(getItemCount()-1);
    }
    public void showError(){
        mState=STATE_ERROR;
        notifyItemChanged(getItemCount()-1);
    }
    public void showComplete(){
        mState=STATE_COMPLETE;
        notifyItemChanged(getItemCount()-1);
    }
    @Override
    public int getItemCount() {

        return mShowLoadMoreItem?mRealAdapter.getItemCount()+1:mRealAdapter.getItemCount();
    }

    public void setRefreshState(RefreshState state) {
        mState=state;
        switch (mState){
            case STATE_LOADING:
                showLoading();
                break;
            case STATE_ERROR:
                showError();
                break;
            case STATE_COMPLETE:
                showComplete();
                break;
        }
    }

    public void toggleLoadMoreItem(boolean show) {
        this.mShowLoadMoreItem=show;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_state;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_state=itemView.findViewById(R.id.tv_state);
        }
    }
}
