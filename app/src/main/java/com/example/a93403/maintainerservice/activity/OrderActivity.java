package com.example.a93403.maintainerservice.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.a93403.maintainerservice.R;
import com.example.a93403.maintainerservice.annotation.InjectView;
import com.example.a93403.maintainerservice.bean.Order;
import com.example.a93403.maintainerservice.bean.User;
import com.example.a93403.maintainerservice.bean.json.OrderJson;
import com.example.a93403.maintainerservice.util.InjectUtil;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    public static final String TRANSMIT_PARAM = "ORDER";
    private OrderJson order;
    @InjectView(R.id.order_tb)
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        order = (OrderJson) getIntent().getSerializableExtra(TRANSMIT_PARAM);
        InjectUtil.InjectView(this); // 自定义控件绑定注解

        init();


    }
    public static void launchActivity(Context context, OrderJson order) {
        Intent intent = new Intent(context, OrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TRANSMIT_PARAM, order);
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
        toolbar.setSubtitle("订单信息");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            OrderActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
