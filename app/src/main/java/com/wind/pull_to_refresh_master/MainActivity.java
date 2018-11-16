package com.wind.pull_to_refresh_master;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wind.pulltorefresh.OnRefreshStateListener;
import com.wind.pulltorefresh.PullToRefershView;
import com.wind.pulltorefresh.RefreshState;
import com.wind.pulltorefresh.Refreshable;
import com.wind.pulltorefresh.RefreshableAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private OnRefreshStateListener mOnRefreshStateListener;
    StringAdapter adapter;
    SwipeRefreshLayout swipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rv = findViewById(R.id.rv);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        adapter = new StringAdapter();
        rv.setAdapter(adapter);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            list.add("" + i);
        }
        adapter.addAll(list);

        mOnRefreshStateListener = Refreshable
                .wrap(rv)
                .threshold(10)
                .onRefreshListener(new PullToRefershView.OnRefreshListener() {
                    @Override
                    public void onPullDownToRefresh(RefreshableAdapter adapter) {
                        refresh(adapter);
                    }

                    @Override
                    public void onPullUpToRefresh(RefreshableAdapter adapter) {
                        loadMore(adapter);
                    }
                })
                .getRefreshStateListener();


    }

    private void refresh(final RefreshableAdapter refreshableAdapter) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    list.add("" + i);
                }
                adapter.replace(list);
                refreshableAdapter.notifyDataSetChanged();
                mOnRefreshStateListener.onRefreshState(RefreshState.STATE_COMPLETE);

            }
        }, 1000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private void loadMore(final RefreshableAdapter refreshableAdapter) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    list.add("" + i);
                }
                adapter.addAll(list);
                refreshableAdapter.notifyDataSetChanged();
                mOnRefreshStateListener.onRefreshState(RefreshState.STATE_COMPLETE);
            }
        }, 1000);
    }


}
