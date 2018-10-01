package com.example.a93403.maintainerservice.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.a93403.maintainerservice.R;
import com.example.a93403.maintainerservice.adapter.OrderAdapter;
import com.example.a93403.maintainerservice.annotation.InjectView;
import com.example.a93403.maintainerservice.base.BaseActivity;
import com.example.a93403.maintainerservice.bean.Order;
import com.example.a93403.maintainerservice.bean.User;
import com.example.a93403.maintainerservice.bean.json.OrderJson;
import com.example.a93403.maintainerservice.util.InjectUtil;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Take_orderActivity extends BaseActivity {

    private static final String TAG = "Take_orderActivity";

    public static final String TRANSMIT_PARAM = "ORDER_LIST";

    public List<OrderJson> orderList = null;
    private OrderAdapter adapter;

    @InjectView(R.id.take_order_tb)
    private Toolbar toolbar;

    @InjectView(R.id.swipe_take_order)
    private SwipeRefreshLayout swipeRefresh;

    @InjectView(R.id.recycle_view)
    private RecyclerView recycle_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_order);
        InjectUtil.InjectView(this); // 自定义控件绑定注解

        // 接收订单数据
        Intent intent = getIntent();
        orderList = (List<OrderJson>) intent.getSerializableExtra(TRANSMIT_PARAM);

        if (!orderList.isEmpty()) {
            Log.i(TAG, "进入接收订单后订单JSON数据的传输情况=======》\n" + new Gson().toJson(orderList));
        } else {
            Log.i(TAG, "onCreate: 订单数据为空");
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycle_view.setLayoutManager(layoutManager);
        initRecycleView();

        init();
    }

    public void initRecycleView() {
        adapter = new OrderAdapter(orderList);
        recycle_view.setAdapter(adapter);
    }

    public static void launchActivity(Context context, List<OrderJson> orderList) {
        Intent intent = new Intent(context, Take_orderActivity.class);
        intent.putExtra(TRANSMIT_PARAM, (Serializable)orderList);
        context.startActivity(intent);
    }

    private void init() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true); // 返回按钮可点击
        }
        toolbar.setSubtitle("我要接单");

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshOrders();
            }
        });
    }

    public void refreshOrders() {
        swipeRefresh.setRefreshing(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Take_orderActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
