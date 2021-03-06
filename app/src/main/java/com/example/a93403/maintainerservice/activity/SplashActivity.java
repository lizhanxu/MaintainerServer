package com.example.a93403.maintainerservice.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.a93403.maintainerservice.R;
import com.example.a93403.maintainerservice.base.BaseActivity;
import com.example.a93403.maintainerservice.constant.APPConsts;
import com.example.a93403.maintainerservice.bean.User;
import com.example.a93403.maintainerservice.util.SharedPreferencesUtil;

import org.litepal.crud.DataSupport;

import java.util.List;

public class SplashActivity extends BaseActivity {
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Boolean isLogin = (Boolean) SharedPreferencesUtil.query(this, APPConsts.SHARED_KEY_ISLOGIN, "boolean");
        if (isLogin != null && isLogin) {

            final List<User> userList = DataSupport.findAll(User.class);

            if (!userList.isEmpty()) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.launchActivity(SplashActivity.this, userList.get(0));
                        SplashActivity.this.finish();
                        // 淡入淡出动画效果
//                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        // 类似iphone进入和退出效果
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                    }
                }, 1000);
            }
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    LoginActivity.launchActivity(SplashActivity.this);
                    SplashActivity.this.finish();
                    // 淡入淡出动画效果
//                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    // 类似iphone进入和退出效果
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                }
            }, 1200);
        }
    }
}
