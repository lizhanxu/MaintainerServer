package com.example.a93403.maintainerservice.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import com.example.a93403.maintainerservice.base.BaseActivity;
import com.example.a93403.maintainerservice.bean.CurrentOrder;
import com.example.a93403.maintainerservice.bean.FaultCode;
import com.example.a93403.maintainerservice.bean.json.OrderJson;
import com.example.a93403.maintainerservice.constant.Actions;
import com.example.a93403.maintainerservice.constant.UrlConsts;
import com.example.a93403.maintainerservice.util.HttpUtil;
import com.example.a93403.maintainerservice.util.InjectUtil;
import com.example.a93403.maintainerservice.util.NavigationUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Current_orderActivity extends BaseActivity {

    public static final String TRANSMIT_PARAM = "ORDER";
    private static final String TAG = "Current_orderActivity";
    private CurrentOrder order;
    private Dialog dialog = null;

    @InjectView(R.id.order_id)
    private TextView order_id;
    @InjectView(R.id.order_date)
    private TextView order_date;
    @InjectView(R.id.order_date_take)
    private TextView order_date_take;
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
    @InjectView(R.id.begin_btn)
    private Button begin_btn;
    @InjectView(R.id.end_btn)
    private Button end_btn;


    @InjectView(R.id.current_order_tb)
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);
        InjectUtil.InjectView(this); // 自定义控件绑定注解
        order = (CurrentOrder) getIntent().getSerializableExtra(TRANSMIT_PARAM);
        init();
        begin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Current_orderActivity.this);
                builder.setTitle("提示")
                        .setIcon(R.mipmap.app_log)
                        .setCancelable(false)
                        .setMessage("请在手机设置里打开GPS")
                        .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                                NavigationUtil.toNavigation(Current_orderActivity.this, order.getLongitude(), order.getLatitude());
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(Current_orderActivity.this, "取消导航", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });
        end_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDialog("请稍等...");
                final long startTime = System.currentTimeMillis();
                RequestBody requestBody = new FormBody.Builder()
                        .add("orderNo",order.getOrder_id())
                        .add("customerPhone",order.getPhone()) // order.getPhone() //13900000000
                        .build();
                HttpUtil.okHttpPost(UrlConsts.getRequestURL(Actions.ACTION_FINISH_ORDER), requestBody, new Callback() {
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
                                prompt = "结束订单成功";
                                Log.i(TAG, "结束订单成功");
                                //初始化数据库
                                LitePal.getDatabase();
                                DataSupport.deleteAll(CurrentOrder.class);

                                endDialog();
                                Current_orderActivity.this.finish();
                            } else {
                                prompt = "结束接单失败";
                                Log.i(TAG, "结束订单成功************1");
                            }
                            Log.i(TAG, "结束订单成功*************2");
                        } else {
                            prompt = "未知错误";
                            Log.i(TAG, "结束订单成功**************3");
                        }
                        Log.i(TAG, "结束订单成功******************4");
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
                                Log.i(TAG, "消息提示成功");
                                Toast.makeText(Current_orderActivity.this, finalPrompt, Toast.LENGTH_SHORT).show();
                            }
                        });
                        endDialog();
                    }
                });
            }
        });
    }

    public static void launchActivity(Context context, CurrentOrder order) {
        Intent intent = new Intent(context, Current_orderActivity.class);
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
        toolbar.setSubtitle("当前订单");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        List<CurrentOrder> currentOrders = DataSupport.findAll(CurrentOrder.class);

        Log.i(TAG, "init: 输出来数据库数据===>" + new Gson().toJson(currentOrders));
        if(order != null){
            order_id.setText(order.getOrder_id());
            order_date.setText(formatter.format(order.getPublish_time()));
            user_name.setText(order.getNickname());
            user_tele.setText(order.getPhone());
            car_brand.setText(order.getCar_brand());
            car_type.setText(order.getCar_type());
            fault_code.setText(order.getFault_code());
            fault_describe.setText(order.getDescribe());
            //读取数据库，取出接单时间
            Log.i(TAG, "init: " + new Gson().toJson(currentOrders));
            order_date_take.setText(formatter.format(currentOrders.get(0).getAck_time()));
        } else if (null != currentOrders && !currentOrders.isEmpty()) {
            order_date_take.setText(formatter.format(currentOrders.get(0).getAck_time()));
            order_id.setText(currentOrders.get(0).getOrder_id());
            order_date.setText(formatter.format(currentOrders.get(0).getPublish_time()));
            user_name.setText(currentOrders.get(0).getNickname());
            user_tele.setText(currentOrders.get(0).getPhone());
            car_brand.setText(currentOrders.get(0).getCar_brand());
            car_type.setText(currentOrders.get(0).getCar_type());
            fault_code.setText(currentOrders.get(0).getFault_code());
            fault_describe.setText(currentOrders.get(0).getDescribe());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Current_orderActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void startDialog(String msg) {
        dialog = new Dialog(Current_orderActivity.this, R.style.MyDialogStyle);
        dialog.setContentView(R.layout.loading);
        dialog.setCanceledOnTouchOutside(false);
        TextView message = (TextView) dialog.getWindow().findViewById(R.id.load_msg);
        if (dialog != null && !dialog.isShowing()) {
            message.setText(msg);
            dialog.show();
        }
    }

    private void endDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

//    private void sendResquestWithOkHttp(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    OkHttpClient client = new OkHttpClient();
//                    RequestBody requestBody = new FormBody.Builder()
//                            .add("orderNo",order.getOrderNo())
//                            .add("customerPhone","13900000000") // order.getCustomer().getCustPhone()
//                            .build();
//                    Request request = new Request.Builder()
//                            .url("http://192.168.1.104:8080/order/finish.do")
//                            .post(requestBody)
//                            .build();
//                    Response response = client.newCall(request).execute();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        })
//    }
}
