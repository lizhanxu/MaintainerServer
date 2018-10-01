package com.example.a93403.maintainerservice.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.a93403.maintainerservice.R;
import com.example.a93403.maintainerservice.adapter.OrderAdapter;
import com.example.a93403.maintainerservice.annotation.InjectView;
import com.example.a93403.maintainerservice.base.BaseActivity;
import com.example.a93403.maintainerservice.bean.Order;
import com.example.a93403.maintainerservice.bean.User;
import com.example.a93403.maintainerservice.bean.json.OrderJson;
import com.example.a93403.maintainerservice.util.InjectUtil;
import com.example.a93403.maintainerservice.util.LocationUtil;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Take_orderActivity extends BaseActivity {

    private static final String TAG = "Take_orderActivity";

    public static final String TRANSMIT_PARAM = "ORDER_LIST";

    private static String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int LOCATION_CODE = 1;

    public static Double longitude = 106.528041;
    public static Double latitude = 29.455653;

    public List<OrderJson> orderList = null;
    private OrderAdapter adapter;
    Location location = null;

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
        init();

        int i = ContextCompat.checkSelfPermission(Take_orderActivity.this, permissions[0]);
        if (i != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(Take_orderActivity.this, permissions, LOCATION_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意
                } else {
                    // 权限被用户拒绝
                    Toast.makeText(Take_orderActivity.this, "请开启定位权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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

        location = LocationUtil.requestLocation(Take_orderActivity.this);
        if (null != location) {
            Log.i(TAG, "不为空: " + longitude + " : " + latitude);
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        } else {
            Log.i(TAG, "为空");
        }

        initRecycleView();
    }

    public void initRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycle_view.setLayoutManager(layoutManager);
        adapter = new OrderAdapter(orderList);
        recycle_view.setAdapter(adapter);
    }

    public static void launchActivity(Context context, List<OrderJson> orderList) {
        Intent intent = new Intent(context, Take_orderActivity.class);
        intent.putExtra(TRANSMIT_PARAM, (Serializable)orderList);
        context.startActivity(intent);
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
