package com.example.a93403.maintainerservice.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.a93403.maintainerservice.R;
import com.example.a93403.maintainerservice.adapter.OrderAdapter;
import com.example.a93403.maintainerservice.annotation.InjectView;
import com.example.a93403.maintainerservice.base.BaseActivity;
import com.example.a93403.maintainerservice.bean.Order;
import com.example.a93403.maintainerservice.bean.User;
import com.example.a93403.maintainerservice.bean.json.OrderJson;
import com.example.a93403.maintainerservice.util.InjectUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Current_orderActivity extends BaseActivity {

    public static final String TRANSMIT_PARAM = "ORDER";
    private List<OrderJson> orderList = new ArrayList<>();

    @InjectView(R.id.current_order_tb)
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);
        InjectUtil.InjectView(this); // 自定义控件绑定注解
        init();
    }

    public static void launchActivity(Context context, OrderJson orderJson) {
        Intent intent = new Intent(context, Current_orderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TRANSMIT_PARAM, orderJson);
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
        toolbar.setSubtitle("当前订单");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Current_orderActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
