package com.example.a93403.maintainerservice.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import com.example.a93403.maintainerservice.bean.User;
import com.example.a93403.maintainerservice.util.InjectUtil;
import com.shinelw.library.ColorArcProgressBar;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectUtil.InjectView(this); // 自定义控件绑定注解
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
                        Take_orderActivity.launchActivity(MainActivity.this,user);
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
}
