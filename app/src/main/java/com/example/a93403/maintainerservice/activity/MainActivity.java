package com.example.a93403.maintainerservice.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.a93403.maintainerservice.R;
import com.example.a93403.maintainerservice.annotation.InjectView;
import com.example.a93403.maintainerservice.base.ActivityCollector;
import com.example.a93403.maintainerservice.base.BaseActivity;
import com.example.a93403.maintainerservice.bean.CurrentOrder;
import com.example.a93403.maintainerservice.bean.User;
import com.example.a93403.maintainerservice.bean.json.OrderJson;
import com.example.a93403.maintainerservice.util.FormatCheckUtil;
import com.example.a93403.maintainerservice.util.InjectUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shinelw.library.ColorArcProgressBar;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.a93403.maintainerservice.constant.APPConsts.ORDER_FINISH_RESPONSE;
import static com.example.a93403.maintainerservice.constant.APPConsts.ORDER_KEY_MESSAGE;
import static com.example.a93403.maintainerservice.constant.APPConsts.ORDER_NOTIFICATION_ACCEPT_TITLE;
import static com.example.a93403.maintainerservice.constant.APPConsts.PUSH_TYPE_MESSAGE;
import static com.example.a93403.maintainerservice.constant.APPConsts.PUSH_TYPE_NOTIFY;


public class MainActivity extends BaseActivity {

    public static final String TRANSMIT_PARAM = "USER";
    private static final String TAG = "MainActivity";
    @InjectView(R.id.toolbar)
    private Toolbar toolbar;
    @InjectView(R.id.drawer_layout)
    private DrawerLayout mDrawerLayout;
    @InjectView(R.id.nav_view)
    private NavigationView navView;
    @InjectView(R.id.nav_view)
    private NavigationView navigationView;

    private TextView username;
    private CircleImageView head_portrait;
    private User user;

    @InjectView(R.id.bar1)
    private ColorArcProgressBar colorArcProgressBar;

    @InjectView(R.id.test_btn)
    private Button test_btn;

    @InjectView(R.id.accept_order_tv)
    private TextView accept_order_tv;

    private CurrentOrder order;
    public List<OrderJson> jsonList = new ArrayList<>();
    public List<CurrentOrder> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectUtil.InjectView(this); // 自定义控件绑定注解

        registerMessageReceiver();  // 注册接收推送消息的广播

        user = (User) getIntent().getSerializableExtra(TRANSMIT_PARAM);

        @SuppressLint("SimpleDateFormat") String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Log.i(TAG, "onResponse: " + time);

        test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorArcProgressBar.setCurrentValues(0);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        colorArcProgressBar.setCurrentValues(100);
                    }
                }, 1000);
            }
        });

        accept_order_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Take_orderActivity.launchActivity(MainActivity.this, orderList);
            }
        });

        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.navigation);
        }

        View view = navigationView.inflateHeaderView(R.layout.nav_header);
        head_portrait = view.findViewById(R.id.head_portrait);
        username = view.findViewById(R.id.username);
        username.setText(user.getNickname() == null ? "暂无昵称" : user.getNickname());
        Glide.with(MainActivity.this)
                .load(user.getPortrait())
                .error(R.drawable.load_fail)
                .into(head_portrait);
        head_portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user_info", user);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });
        navView.setCheckedItem(R.id.first);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.feedback:
                        FeedbackActivity.launchActivity(MainActivity.this,user.getNickname());
                        break;
                    case R.id.order:
                        Take_orderActivity.launchActivity(MainActivity.this, orderList);
                        break;
                    case R.id.current_order:
                        List<CurrentOrder> currentOrders = DataSupport.findAll(CurrentOrder.class);
                        Log.i(TAG, "init: 输出来数据库数据===>" + new Gson().toJson(currentOrders));
                        if(currentOrders == null || currentOrders.isEmpty())
                        {
                            Intent intent = new Intent(MainActivity.this, EmptyActivity.class);
                            startActivity(intent);
                        }else{
                            order=currentOrders.get(0);

                            Current_orderActivity.launchActivity(MainActivity.this, order);
                        }
                        break;
                    case R.id.order_history:
                        Order_historyActivity.launchActivity(MainActivity.this,user);
                        break;
                    case R.id.comment:
                        CommentActivity.launchActivity(MainActivity.this,user.getId());
                        break;
                    case R.id.settings:
                        SettingsActivity.launchActivity(MainActivity.this,user);
                        break;
                    default:break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }
    public static void launchActivity(Context context, User user) {
        Intent intent = new Intent(context, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TRANSMIT_PARAM, user);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.personal:
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user_info", user);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 1) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                user = (User) bundle.getSerializable("user_info");
                if (user != null) {

                    Glide.with(MainActivity.this)
                            .load(user.getPortrait())
                            .error(R.drawable.load_fail)
                            .into(head_portrait);
                    username.setText(user.getNickname() == null ? "暂无昵称" : user.getNickname());
                }
            }
        }
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.jpush.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static final String KEY_TYPE = "type";
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }
    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String title = intent.getStringExtra(KEY_TITLE);
                    String type = intent.getStringExtra(KEY_TYPE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    Log.i(TAG, "TYPE===>" + type + " | TITLE===>" + title + "  ====>\n" + extras);

                    switch (type) {
                        case PUSH_TYPE_NOTIFY:
                            processNotification(title, extras);
                            break;
                        case PUSH_TYPE_MESSAGE:
                            processMessage(title, extras);
                            break;
                        default:
                            break;
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void processNotification(String title, String extras) {
        if (null == title) {
            Log.i(TAG, "processNotification: 消息title为空");
            return;
        }
        switch (title) {
            // 订单在被用户创建后发送过来的接单请求
            case ORDER_NOTIFICATION_ACCEPT_TITLE:
                if (!FormatCheckUtil.isEmpty(extras)) {
                    // 订单接受请求
                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd HH:mm:ss")
                            .create();

                    OrderJson orderJson = gson.fromJson(extras, new TypeToken<OrderJson>() {
                    }.getType());
                    CurrentOrder currentOrder = new CurrentOrder(orderJson);
                    jsonList.add(orderJson);
                    orderList.add(currentOrder);
                    Log.i(TAG, "onReceive: 订单列表数据===>" + new Gson().toJson(orderList));

                    // 判断Take_orderActivity是否已经存在，如果存在则更新其显示内容
                    Take_orderActivity take_orderActivity = ActivityCollector.getActivity(Take_orderActivity.class);
                    if (null != take_orderActivity) {
                        Log.i(TAG, "onReceive: 接单活动已经存在！！");
                        take_orderActivity.orderList.add(currentOrder);

                        take_orderActivity.refreshOrders();
                    } else {
                        Log.i(TAG, "onReceive: 接单活动不存在！！");
                    }
                }
                break;
        }
    }

    private void processMessage(String title, String extras) {
        if (null == title) {
            Log.i(TAG, "processMessage: 消息title为空");
            return;
        }
        switch (title) {
            // 订单结束请求的响应结果通知
            case ORDER_FINISH_RESPONSE:
                Map<String, Object> response = new Gson().fromJson(extras, new TypeToken<Map<String, Object>>() {}.getType());
                Log.i(TAG, "processCustomMessage: 请求结果-->" + new Gson().toJson(response));
                if (response != null) {
                    String msg = (String) response.get(ORDER_KEY_MESSAGE);
                    Log.i(TAG, "processCustomMessage: " + msg);

                    ActivityCollector.getActivity(Current_orderActivity.class).processOrderFinishResponse(msg);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
