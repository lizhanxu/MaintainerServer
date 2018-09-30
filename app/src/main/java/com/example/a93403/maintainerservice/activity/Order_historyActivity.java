package com.example.a93403.maintainerservice.activity;

import android.content.Context;
import android.content.Intent;
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
import com.example.a93403.maintainerservice.bean.Order;
import com.example.a93403.maintainerservice.bean.User;
import com.example.a93403.maintainerservice.bean.json.OrderJson;
import com.example.a93403.maintainerservice.util.InjectUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order_historyActivity extends AppCompatActivity {

    public static final String TRANSMIT_PARAM = "USER";
    private List<OrderJson> orderList = new ArrayList<>();

    @InjectView(R.id.history_order_tb)
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        InjectUtil.InjectView(this); // 自定义控件绑定注解
//        initOrders();
        init();
        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        OrderAdapter adapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(adapter);
    }

//    private void initOrders(){
//
//        for(int i=0;i<2;i++){
//            Order order_1 = new Order(new Date(),"奥迪A8",8,"这辆车有问题！");
//            orderList.add(order_1);
//            Order order_2 = new Order(new Date(),"大众A3",3,"这辆车有大问题！");
//            orderList.add(order_2);
//        }
//    }
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Order_historyActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
