package com.example.a93403.maintainerservice.activity;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a93403.maintainerservice.R;
import com.example.a93403.maintainerservice.adapter.CommentAdapter;
import com.example.a93403.maintainerservice.annotation.InjectView;
import com.example.a93403.maintainerservice.base.BaseActivity;
import com.example.a93403.maintainerservice.bean.Comment;
import com.example.a93403.maintainerservice.bean.ResultArray;
import com.example.a93403.maintainerservice.constant.Actions;
import com.example.a93403.maintainerservice.constant.UrlConsts;
import com.example.a93403.maintainerservice.util.HttpUtil;
import com.example.a93403.maintainerservice.util.InjectUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommentActivity extends BaseActivity {
    public static final String PARAM_REPAIRMAN_ID = "repairman_id";
    private static final String TAG = "CommentActivity";

    private Integer repairman_id;

    private Dialog dialog;

    @InjectView(R.id.mybar_back_ib)
    private ImageView mybar_back_ib;

    @InjectView(R.id.mybar_title_tv)
    private TextView mybar_title_tv;

    @InjectView(R.id.comment_empty_layout)
    private RelativeLayout comment_empty_layout;

    @InjectView(R.id.comment_show_rv)
    private RecyclerView recyclerView;

    private CommentAdapter commentAdapter;
    private List<Comment> commentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        InjectUtil.InjectView(this);
        repairman_id = getIntent().getIntExtra(PARAM_REPAIRMAN_ID, -1);

        init();
    }

    private void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(commentList);
        recyclerView.setAdapter(commentAdapter);
        sendCommentRequest(repairman_id);

        if (commentList == null || !commentList.isEmpty()) {
            comment_empty_layout.setVisibility(View.VISIBLE);
        } else {
            comment_empty_layout.setVisibility(View.GONE);
        }

        mybar_title_tv.setText("我的评价");

        mybar_back_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentActivity.this.finish();
            }
        });
    }

    private void sendCommentRequest(Integer repairman_id) {
        startDialog("Loading...");
        RequestBody requestBody = new FormBody.Builder()
                .add("repairman_id", String.valueOf(repairman_id))
                .build();

        Log.i(TAG, "sendCommentRequest: "+String.valueOf(repairman_id));
        HttpUtil.okHttpPost(UrlConsts.getRequestURL(Actions.ACTION_GET_COMMENT), requestBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        endDialog();
                        Toast.makeText(CommentActivity.this, "评论获取失败，请检查网络", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();
                final ResultArray<Comment> result = new Gson().fromJson(res, new TypeToken<ResultArray<Comment>>(){}.getType());
                if (UrlConsts.CODE_SUCCESS.equals(result.getCode())) {
                    commentList.clear();
                    commentList.addAll(result.getData());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            endDialog();
                            commentAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            endDialog();
                            Toast.makeText(CommentActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public static void launchActivity(Context context, Integer custId) {
        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra(PARAM_REPAIRMAN_ID, custId);
        context.startActivity(intent);
    }

    private void startDialog(String msg) {
        dialog = new Dialog(CommentActivity.this, R.style.MyDialogStyle);
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
}
