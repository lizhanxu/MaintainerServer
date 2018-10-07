package com.example.a93403.maintainerservice.adapter;


import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a93403.maintainerservice.R;
import com.example.a93403.maintainerservice.activity.OrderActivity;
import com.example.a93403.maintainerservice.activity.Take_orderActivity;
import com.example.a93403.maintainerservice.bean.CurrentOrder;
import com.example.a93403.maintainerservice.bean.FaultCode;
import com.example.a93403.maintainerservice.bean.json.OrderJson;
import com.example.a93403.maintainerservice.util.LocationUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.a93403.maintainerservice.base.MyApplication.judgement;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<CurrentOrder> mOrderList;
    private static final String TAG = "OrderAdapter";

    static class ViewHolder extends RecyclerView.ViewHolder {
        View orderView;
        TextView order_time;
        TextView order_type;
        TextView order_distance;
        TextView order_describe;
        TextView text_1;

        public ViewHolder(View View) {
            super(View);
            orderView = View;
            order_time = View.findViewById(R.id.order_time);
            order_type = View.findViewById(R.id.order_type);
            order_distance = View.findViewById(R.id.order_distance);
            order_describe = View.findViewById(R.id.order_describe);
            text_1 = View.findViewById(R.id.text_1);
        }
    }

    public OrderAdapter(List<CurrentOrder> mOrderList) {
        this.mOrderList = mOrderList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.orderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                CurrentOrder order = mOrderList.get(position);
                OrderActivity.launchActivity(view.getContext(), order);


            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CurrentOrder order = mOrderList.get(position);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        holder.order_time.setText(formatter.format(order.getPublish_time()));
        holder.order_type.setText(String.format("%s%s", order.getCar_brand(), order.getCar_type()));

        double longitude1 = Take_orderActivity.longitude;
        double latitude1 = Take_orderActivity.latitude;
        double longitude2 = order.getLongitude();
        double latitude2 = order.getLatitude();
        double distance = algorithm(longitude1,latitude1,longitude2,latitude2)/1000;

        DecimalFormat df = new DecimalFormat("0.00");

        if(judgement == 1){
            holder.order_distance.setVisibility(View.INVISIBLE);
            holder.text_1.setVisibility(View.INVISIBLE);
        }
        holder.order_distance.setText(df.format(distance));

        Log.i(TAG, "onBindViewHolder1: " + longitude1 + " : " + latitude1 );
        Log.i(TAG, "onBindViewHolder2: " + longitude2 + " : " + latitude2 );

        holder.order_describe.setText(order.getDescribe());

    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    public static double algorithm(double longitude1, double latitude1, double longitude2, double latitude2) {
        double Lat1 = rad(latitude1); // 纬度
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;//两点纬度之差
        double b = rad(longitude1) - rad(longitude2); //经度之差
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(Lat1) * Math.cos(Lat2) * Math.pow(Math.sin(b / 2), 2)));//计算两点距离的公式
        s = s * 6378137.0;//弧长乘地球半径（半径为米）
        s = Math.round(s * 10000d) / 10000d;//精确距离的数值
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.00; //角度转换成弧度
    }


}
