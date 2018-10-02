package com.example.a93403.maintainerservice.activity;

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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


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

    private List<OrderJson> jsonList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectUtil.InjectView(this); // 自定义控件绑定注解

        registerMessageReceiver();  // 注册接收推送消息的广播

        user = (User) getIntent().getSerializableExtra(TRANSMIT_PARAM);

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
                        Take_orderActivity.launchActivity(MainActivity.this, jsonList);
                        break;
                    case R.id.current_order:
                        List<CurrentOrder> currentOrders = DataSupport.findAll(CurrentOrder.class);
                        if(currentOrders == null)
                        {
                            Intent intent = new Intent(MainActivity.this, EmptyActivity.class);
                            startActivity(intent);
                        }else{
                            Current_orderActivity.launchActivity(MainActivity.this, null);
                        }
                        break;
                    case R.id.order_history:
                        Order_historyActivity.launchActivity(MainActivity.this,user);
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
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    if (!FormatCheckUtil.isEmpty(extras)) {

                        Gson gson = new GsonBuilder()
                                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                .create();

                        OrderJson orderJson = gson.fromJson(extras, new TypeToken<OrderJson>(){}.getType());
                        Log.i(TAG, "onReceive: 测试时间===》" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderJson.getCreateTime()));
                        jsonList.add(orderJson);

                        // 判断Take_orderActivity是否已经存在，如果存在则更新其显示内容
                        Take_orderActivity take_orderActivity = ActivityCollector.getActivity(Take_orderActivity.class);
                        if (null != take_orderActivity) {
                            Log.i(TAG, "onReceive: 接单活动已经存在！！");
                            take_orderActivity.orderList.add(orderJson);

                            take_orderActivity.refreshOrders();
                        } else {
                            Log.i(TAG, "onReceive: 接单活动不存在！！");
                        }
                    }
                }
            } catch (Exception e){
            }
        }
    }
    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
