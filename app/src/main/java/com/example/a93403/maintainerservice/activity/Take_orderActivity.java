package com.example.a93403.maintainerservice.activity;

import android.content.Context;
import android.content.Intent;
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
import com.example.a93403.maintainerservice.bean.Order;
import com.example.a93403.maintainerservice.bean.User;
import com.example.a93403.maintainerservice.bean.json.OrderJson;
import com.example.a93403.maintainerservice.util.InjectUtil;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Take_orderActivity extends AppCompatActivity {
    public static final String TRANSMIT_PARAM = "ORDER_LIST";
    private List<OrderJson> orderList;
    private static final String TAG = "Take_orderActivity";

    @InjectView(R.id.take_order_tb)
    private Toolbar toolbar;

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

            RecyclerView recyclerView = findViewById(R.id.recycle_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            OrderAdapter adapter = new OrderAdapter(orderList);
            recyclerView.setAdapter(adapter);
        } else {
            Log.i(TAG, "onCreate: 订单数据为空");
        }

        init();
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Take_orderActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
