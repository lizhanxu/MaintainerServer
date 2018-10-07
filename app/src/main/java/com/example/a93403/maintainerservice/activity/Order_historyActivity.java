package com.example.a93403.maintainerservice.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
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
import com.example.a93403.maintainerservice.bean.CurrentOrder;
import com.example.a93403.maintainerservice.bean.ResultArray;
import com.example.a93403.maintainerservice.bean.User;
import com.example.a93403.maintainerservice.constant.Actions;
import com.example.a93403.maintainerservice.constant.UrlConsts;
import com.example.a93403.maintainerservice.util.HttpUtil;
import com.example.a93403.maintainerservice.util.InjectUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.a93403.maintainerservice.base.MyApplication.judgement;

public class Order_historyActivity extends BaseActivity {

    public static final String TRANSMIT_PARAM = "USER";
    public List<CurrentOrder> orderList = new ArrayList<>();
    private User user;
    private static final String TAG = "Order_historyActivity";

    @InjectView(R.id.history_order_tb)
    private Toolbar toolbar;
    @InjectView(R.id.swipe_history_order)
    private SwipeRefreshLayout swipeRefresh;
    @InjectView(R.id.recycle_view)
    private RecyclerView recycle_view;
    private OrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        InjectUtil.InjectView(this); // 自定义控件绑定注解
        user = (User) getIntent().getSerializableExtra(TRANSMIT_PARAM);
        Log.i(TAG, "onCreate: user-->" + user.toString());
        init();
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        OrderAdapter adapter = new OrderAdapter(orderList);
//        recyclerView.setAdapter(adapter);
    }

    public static void launchActivity(Context context, User user) {
        Intent intent = new Intent(context, Order_historyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TRANSMIT_PARAM, user);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void init() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true); // 返回按钮可点击
        }
        toolbar.setSubtitle("历史订单");

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshOrders();
            }
        });

        initRecycleView();

        requestHistoryOrder();
    }

    private void requestHistoryOrder() {

        RequestBody requestBody = new FormBody.Builder()
                .add("repairmanId", String.valueOf(user.getId())) // order.getCustomer().getCustPhone()  //13900000000
                .build();

        HttpUtil.okHttpPost(UrlConsts.getRequestURL(Actions.ACTION_QUERY_ORDER), requestBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();

                ResultArray<CurrentOrder> result = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().fromJson(res, new TypeToken<ResultArray<CurrentOrder>>() {}.getType());
                Log.i(TAG, "onResponse: " + new Gson().toJson(result));

                orderList.clear();
                orderList.addAll(result.getData());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Order_historyActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void initRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycle_view.setLayoutManager(layoutManager);
        adapter = new OrderAdapter(orderList);
        recycle_view.setAdapter(adapter);
        judgement = 1;
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
}
