package com.wind.pulltorefresh;

import android.support.v7.widget.RecyclerView;

public class Refreshable {


    public static PullToRefershView wrap(RecyclerView recyclerView){
       // Refreshable refreshable=new Refreshable();
        PullToRefershView pullToRefershView=new PullToRefershView(recyclerView);
        return pullToRefershView;
    }


   /* public Refreshable onRefreshListener(PullToRefershView.OnRefreshListener listener){
        mPullToRefershView.setOnRefreshListener(listener);
        return this;
    }


    public OnRefreshStateListener getStateListener(){
        return mPullToRefershView;
    }*/


}
