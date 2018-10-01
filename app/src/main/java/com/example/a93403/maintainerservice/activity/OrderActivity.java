package com.example.a93403.maintainerservice.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.a93403.maintainerservice.R;
import com.example.a93403.maintainerservice.annotation.InjectView;
import com.example.a93403.maintainerservice.base.BaseActivity;
import com.example.a93403.maintainerservice.bean.FaultCode;
import com.example.a93403.maintainerservice.bean.Order;
import com.example.a93403.maintainerservice.bean.Repairman;
import com.example.a93403.maintainerservice.bean.User;
import com.example.a93403.maintainerservice.bean.json.OrderJson;
import com.example.a93403.maintainerservice.constant.Actions;
import com.example.a93403.maintainerservice.constant.UrlConsts;
import com.example.a93403.maintainerservice.util.HttpUtil;
import com.example.a93403.maintainerservice.util.InjectUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderActivity extends BaseActivity {
    public static final String TRANSMIT_PARAM = "ORDER";
    private static final String TAG = "OrderActivity";
    private OrderJson order;
    private String json;
    @InjectView(R.id.order_tb)
    private Toolbar toolbar;
    @InjectView(R.id.order_id)
    private TextView order_id;
    @InjectView(R.id.order_date)
    private TextView order_date;
    @InjectView(R.id.user_name)
    private TextView user_name;
    @InjectView(R.id.user_tele)
    private TextView user_tele;
    @InjectView(R.id.car_brand)
    private TextView car_brand;
    @InjectView(R.id.car_type)
    private TextView car_type;
    @InjectView(R.id.fault_code)
    private TextView fault_code;
    @InjectView(R.id.fault_describe)
    private TextView fault_describe;
    @InjectView(R.id.ack_button)
    private TextView ack_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        order = (OrderJson) getIntent().getSerializableExtra(TRANSMIT_PARAM);
        InjectUtil.InjectView(this); // 自定义控件绑定注解
        init();

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        json = gson.toJson(order);
        Log.i(TAG, "onCreate: " + json);
        ack_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
                HttpUtil.okHttpPost(UrlConsts.getRequestURL(Actions.ACTION_ACCEPT_ORDER), requestBody, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.i(TAG, "请求失败");
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.code() == 200) {
                            String res = response.body().toString();

                            Log.i(TAG, "onResponse: 得到服务器返回数据===>" + res);
                        }
                    }
                });
            }
        });

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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        order_id.setText(order.getOrderNo());
        order_date.setText(formatter.format(order.getCreateTime()));
        user_name.setText(order.getCustomer().getCustName());
        user_tele.setText(order.getCustomer().getCustPhone());
        car_brand.setText(order.getCustomer().getCarBrand());
        car_type.setText(order.getCustomer().getCarId());
        StringBuilder stringBuilder_code = new StringBuilder("");
        for (FaultCode faultCode : order.getFaultCodeList()) {
            stringBuilder_code.append(faultCode.getCode()).append(";  ");
        }
        fault_code.setText(stringBuilder_code.toString());
        StringBuilder stringBuilder_describe = new StringBuilder("");
        for (FaultCode faultCode : order.getFaultCodeList()) {
            stringBuilder_describe.append(faultCode.getDescribe()).append(";  ");
        }
        fault_describe.setText(stringBuilder_describe.toString());


        // 从数据库中读取用户信息
        List<User> user = DataSupport.findAll(User.class);
        Repairman repairman = new Repairman(user.get(0));
        order.setRepairman(repairman);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            OrderActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
