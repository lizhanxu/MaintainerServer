package com.example.a93403.maintainerservice.base;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

import cn.jpush.android.api.JPushInterface;

/**
 * Title: CarService
 * Date: Create in 2018/4/21 21:56
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */

public class MyApplication extends Application {

    private static Context context;
    public static int judgement= 0;

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        context = getApplicationContext();

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    public static Context getContext() {
        return context;
    }
}
