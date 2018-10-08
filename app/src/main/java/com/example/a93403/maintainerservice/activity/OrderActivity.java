package com.example.a93403.maintainerservice.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a93403.maintainerservice.R;
import com.example.a93403.maintainerservice.annotation.InjectView;
import com.example.a93403.maintainerservice.base.ActivityCollector;
import com.example.a93403.maintainerservice.base.BaseActivity;
import com.example.a93403.maintainerservice.bean.CurrentOrder;
import com.example.a93403.maintainerservice.bean.User;
import com.example.a93403.maintainerservice.constant.Actions;
import com.example.a93403.maintainerservice.constant.UrlConsts;
import com.example.a93403.maintainerservice.util.HttpUtil;
import com.example.a93403.maintainerservice.util.InjectUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.a93403.maintainerservice.base.MyApplication.judgement;

public class OrderActivity extends BaseActivity {
    public static final String TRANSMIT_PARAM = "ORDER";
    private static final String TAG = "OrderActivity";
    private CurrentOrder order = null;
    private String json;
    private User user;
    private Dialog dialog = null;
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
    private Button ack_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        order = (CurrentOrder) getIntent().getSerializableExtra(TRANSMIT_PARAM);
        InjectUtil.InjectView(this); // 自定义控件绑定注解
        if (judgement == 1){
            ack_button.setVisibility(View.INVISIBLE);
        }
        init();

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        json = gson.toJson(order);
        Log.i(TAG, "onCreate: " + json);
        ack_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDialog("请稍等...");
                final long startTime = System.currentTimeMillis();

                Log.i(TAG, "onClick: userid-->" + user.getId());
                RequestBody requestBody = new FormBody.Builder()
                        .add("orderNo", order.getOrder_id()) // order.getCustomer().getCustPhone()  //13900000000
                        .add("customerPhone", order.getPhone())
                        .add("repairmanId", String.valueOf(user.getId()))
                        .build();

                HttpUtil.okHttpPost(UrlConsts.getRequestURL(Actions.ACTION_ACCEPT_ORDER), requestBody, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.i(TAG, "请求失败");
                        endDialog();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String prompt;
                        if (response.code() == 200) {
                            String res = response.body().string();
                            Log.i(TAG, "onResponse: 得到服务器返回数据===>" + res);

                            Map<String, String> result = new Gson().fromJson(res, new TypeToken<Map<String, String>>() {}.getType());
                            if (UrlConsts.CODE_SUCCESS.equals(result.get(UrlConsts.KEY_RETURN_CODE))) {
                                prompt = "接单成功";

                                Date take_order_time = new Date();


                                LitePal.getDatabase();
                                CurrentOrder currentOrder = new CurrentOrder();
                                //初始化数据库
                                DataSupport.deleteAll(CurrentOrder.class);

                                // 将订单数据存入数据库
                                currentOrder.setOrder_id(order.getOrder_id());
                                currentOrder.setPublish_time(order.getPublish_time());
                                Log.i(TAG, "onResponse: 接单时间"+take_order_time.toString());
                                currentOrder.setAck_time(take_order_time);
                                currentOrder.setNickname(order.getNickname());
                                currentOrder.setPhone(order.getPhone());
                                currentOrder.setCar_brand(order.getCar_brand());
                                currentOrder.setCar_type(order.getCar_type());
                                currentOrder.setFault_code(order.getFault_code());
                                currentOrder.setDescribe(order.getDescribe());

                                currentOrder.save();

                                MainActivity mainActivity = ActivityCollector.getActivity(MainActivity.class);

                                //删除接单页面中的已接订单
                                Iterator<CurrentOrder> iter = mainActivity.orderList.iterator();
                                while (iter.hasNext()) {
                                    CurrentOrder item = iter.next();
                                    if (item.getOrder_id().equals(order.getOrder_id())) {
                                        iter.remove();
                                    }
                                }

//                                Log.i(TAG, "onResponse: before-->" + new Gson().toJson(mainActivity.orderList));
//                                Log.i(TAG, "onResponse: before-->" + new Gson().toJson(order));
//                                mainActivity.orderList.remove(order);
//                                Log.i(TAG, "onResponse: after-->" + new Gson().toJson(mainActivity.orderList));

                                Current_orderActivity.launchActivity(OrderActivity.this, order);

                                endDialog();

                                // 只有当take_orderActivity存在时才将其finish，不然直接finish会闪退
                                Take_orderActivity take_orderActivity = ActivityCollector.getActivity(Take_orderActivity.class);
                                if (take_orderActivity != null){
                                    ActivityCollector.getActivity(Take_orderActivity.class).finish();
                                }
                                OrderActivity.this.finish();


                            } else {
                                prompt = "接单失败";
                            }
                        } else {
                            prompt = "未知错误";
                        }
                        long endTime = System.currentTimeMillis();
                        if (endTime - startTime < 1000) {
                            try {
                                Thread.sleep(1000 - (endTime - startTime));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            endDialog();
                        }

                        final String finalPrompt = prompt;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(OrderActivity.this, finalPrompt, Toast.LENGTH_SHORT).show();
                            }
                        });
                        endDialog();
                    }
                });
            }
        });

    }
    public static void launchActivity(Context context, CurrentOrder order) {
        Intent intent = new Intent(context, OrderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
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

        order_id.setText(order.getOrder_id());
        order_date.setText(formatter.format(order.getPublish_time()));
        user_name.setText(order.getNickname());
        user_tele.setText(order.getPhone());
        car_brand.setText(order.getCar_brand());
        car_type.setText(order.getCar_type());
        fault_code.setText(order.getFault_code());
        fault_describe.setText(order.getDescribe());

        // 从数据库中读取用户信息
        List<User> userList = DataSupport.findAll(User.class);
        user = userList.get(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            OrderActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void startDialog(String msg) {
        dialog = new Dialog(OrderActivity.this, R.style.MyDialogStyle);
        dialog.setContentView(R.layout.loading);
        dialog.setCanceledOnTouchOutside(false);
        TextView message = (TextView) dialog.getWindow().findViewById(R.id.load_msg);
        if (dialog != null && !dialog.isShowing()) {
            message.setText(msg);
            dialog.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        endDialog();
    }

    private void endDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
