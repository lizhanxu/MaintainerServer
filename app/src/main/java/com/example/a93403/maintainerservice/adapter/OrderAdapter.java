package com.example.a93403.maintainerservice.adapter;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a93403.maintainerservice.R;
import com.example.a93403.maintainerservice.activity.OrderActivity;
import com.example.a93403.maintainerservice.bean.FaultCode;
import com.example.a93403.maintainerservice.bean.json.OrderJson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{
    private List<OrderJson> mOrderList;
    private static final String TAG = "OrderAdapter";
    static class ViewHolder extends  RecyclerView.ViewHolder{
        View orderView;
        TextView order_time;
        TextView order_type;
        TextView order_distance;
        TextView order_describe;

        public ViewHolder(View View) {
            super(View);
            orderView = View;
            order_time=View.findViewById(R.id.order_time);
            order_type=View.findViewById(R.id.order_type);
            order_distance=View.findViewById(R.id.order_distance);
            order_describe=View.findViewById(R.id.order_describe);
        }
    }

    public OrderAdapter(List<OrderJson> mOrderList) {
        this.mOrderList = mOrderList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.orderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                OrderJson order = mOrderList.get(position);
                OrderActivity.launchActivity(view.getContext(),order);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderJson order = mOrderList.get(position);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        holder.order_time.setText(formatter.format(new Date()));
        holder.order_type.setText(order.getCustomer().getCarBrand() + order.getCustomer().getCarId());
        holder.order_distance.setText(String.valueOf(order.getLatitude()));


        StringBuilder stringBuilder = new StringBuilder("");
        for (FaultCode faultCode : order.getFaultCodeList()) {
            stringBuilder.append(faultCode.getDescribe()).append(";  ");
        }
        Log.i(TAG, "onBindViewHolder: " + stringBuilder.toString());

        holder.order_describe.setText(stringBuilder.toString());

    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }
}
