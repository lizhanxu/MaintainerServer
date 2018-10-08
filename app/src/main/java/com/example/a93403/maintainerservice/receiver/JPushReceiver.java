package com.example.a93403.maintainerservice.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.a93403.maintainerservice.activity.Current_orderActivity;
import com.example.a93403.maintainerservice.activity.MainActivity;
import com.example.a93403.maintainerservice.base.ActivityCollector;
import com.example.a93403.maintainerservice.bean.CurrentOrder;
import com.example.a93403.maintainerservice.util.FormatCheckUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

import static com.example.a93403.maintainerservice.base.MyApplication.isShowFinishRequestDialog;
import static com.example.a93403.maintainerservice.constant.APPConsts.ORDER_NOTIFICATION_FINISH_TITLE;
import static com.example.a93403.maintainerservice.constant.APPConsts.PUSH_TYPE_MESSAGE;
import static com.example.a93403.maintainerservice.constant.APPConsts.PUSH_TYPE_NOTIFY;

public class JPushReceiver extends BroadcastReceiver {

    private static final String TAG = "JPushReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            String action = intent.getAction();
            assert bundle != null;
            assert action != null;
//            Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            switch (intent.getAction()) {
                case JPushInterface.ACTION_REGISTRATION_ID:
//                    String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//                    Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
//                    SharedPreferencesUtil.save(context, APPConsts.SHARED_KEY_REGISTRATION_ID, regId);
                    break;
                case JPushInterface.ACTION_MESSAGE_RECEIVED:
                    Log.d(TAG, "===============================  接收到推送下来的自定义消息Message  ==================================");
                    sendToMainActivity(context, bundle, PUSH_TYPE_MESSAGE);

                    break;
                case JPushInterface.ACTION_NOTIFICATION_RECEIVED:
                    Log.d(TAG, "===============================  接收到推送下来的通知Notification  ==================================");
                    sendToMainActivity(context, bundle, PUSH_TYPE_NOTIFY);
                    break;
                case JPushInterface.ACTION_NOTIFICATION_OPENED:
                    Log.d(TAG, "======================================  用户点击打开了通知  =========================================");
                    processOpenNotify(context, bundle);
                    break;
                case JPushInterface.ACTION_RICHPUSH_CALLBACK:
                    Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                    //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

                    break;
                case JPushInterface.ACTION_CONNECTION_CHANGE:
                    boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                    Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
                    break;
                default:
                    Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //send msg to NotifyActivity
    private void processOpenNotify(Context context, Bundle bundle) {

        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.i(TAG, "processNotify: title-->" + title);
        Log.i(TAG, "processNotify: extras-->" + extras);
        if (ORDER_NOTIFICATION_FINISH_TITLE.equals(title)) {
            Map<String, Object> request = new Gson().fromJson(extras, new TypeToken<Map<String, Object>>() {}.getType());
            if (request != null) {

                Current_orderActivity current_orderActivity = ActivityCollector.getActivity(Current_orderActivity.class);
                if (null != current_orderActivity) {
                    current_orderActivity.showFinishRequestDialog();
                } else {
                    isShowFinishRequestDialog = true;

                    List<CurrentOrder> currentOrders = DataSupport.findAll(CurrentOrder.class);
                    Log.i(TAG, "processOpenNotify: 输出来数据库数据===>" + new Gson().toJson(currentOrders));

                    // 打开自定义的Activity
                    Intent intent = new Intent(context, Current_orderActivity.class);
                    intent.putExtra("ORDER", currentOrders.get(0));
                    // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            }
        }
    }

    private void sendToMainActivity(Context context, Bundle bundle, String type) {
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String notify_title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.i(TAG, "processNotify: title-->" + title);
        Log.i(TAG, "processNotify: notify_title-->" + notify_title);
        Log.i(TAG, "processNotify: message-->" + message);
        Log.i(TAG, "processNotify: extras-->" + extras);

        Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
        msgIntent.putExtra(MainActivity.KEY_TYPE, type);
        if (PUSH_TYPE_MESSAGE.equals(type)) {
            msgIntent.putExtra(MainActivity.KEY_TITLE, title);
        } else if (PUSH_TYPE_NOTIFY.equals(type)) {
            msgIntent.putExtra(MainActivity.KEY_TITLE, notify_title);
        }
        if (!FormatCheckUtil.isEmpty(extras)) {
            try {
                JSONObject extraJson = new JSONObject(extras);
                if (extraJson.length() > 0) {
                    msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
    }
}
