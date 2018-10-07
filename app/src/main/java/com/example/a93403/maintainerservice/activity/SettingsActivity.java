package com.example.a93403.maintainerservice.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.a93403.maintainerservice.R;
import com.example.a93403.maintainerservice.annotation.InjectView;
import com.example.a93403.maintainerservice.base.BaseActivity;
import com.example.a93403.maintainerservice.bean.User;
import com.example.a93403.maintainerservice.constant.APPConsts;
import com.example.a93403.maintainerservice.util.InjectUtil;
import com.example.a93403.maintainerservice.util.SharedPreferencesUtil;

import org.litepal.crud.DataSupport;

public class SettingsActivity extends BaseActivity {

    public static final String TRANSMIT_PARAM = "USER";
    private static final String PARAM_USER = "user_info";
    private User user;

    @InjectView(R.id.settings_tb)
    private Toolbar toolbar;

    @InjectView(R.id.user_info_rl)
    private RelativeLayout user_info_rl;

    @InjectView(R.id.pam_rl)
    private RelativeLayout pam_rl;

    @InjectView(R.id.help_rl)
    private RelativeLayout help_rl;

    @InjectView(R.id.about_ap_rl)
    private RelativeLayout about_ap_rl;

    @InjectView(R.id.exit_login_rl)
    private RelativeLayout exit_login_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        InjectUtil.InjectView(this); // 自定义控件绑定注解
        user = (User) getIntent().getSerializableExtra(TRANSMIT_PARAM);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true); // 返回按钮可点击
        }
        toolbar.setSubtitle("设置");

        user_info_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user_info", user);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });

        pam_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        help_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        about_ap_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        exit_login_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExitDialog();
            }
        });
    }

    public static void launchActivity(Context context, User user) {
        Intent intent = new Intent(context, SettingsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TRANSMIT_PARAM, user);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void showExitDialog(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setIcon(R.mipmap.app_log);
        builder.setTitle("提示");
        builder.setMessage("是否要退出账号?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataSupport.deleteAll(User.class, "phone = ?", user.getPhone());

                SharedPreferencesUtil.save(SettingsActivity.this, APPConsts.SHARED_KEY_ISLOGIN, false);
                LoginActivity.launchActivity(SettingsActivity.this);
                SettingsActivity.this.finish();
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            SettingsActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
