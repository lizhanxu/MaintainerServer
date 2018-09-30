package com.example.a93403.maintainerservice.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.a93403.maintainerservice.R;
import com.example.a93403.maintainerservice.bean.Order;
import com.example.a93403.maintainerservice.bean.User;

public class OrderActivity extends AppCompatActivity {
    public static final String TRANSMIT_PARAM = "ORDER";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
    }
    public static void launchActivity(Context context, Order order) {
        Intent intent = new Intent(context, OrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TRANSMIT_PARAM, order);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
