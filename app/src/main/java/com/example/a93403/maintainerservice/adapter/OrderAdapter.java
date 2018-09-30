package com.example.a93403.maintainerservice.adapter;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a93403.maintainerservice.R;
import com.example.a93403.maintainerservice.activity.OrderActivity;
import com.example.a93403.maintainerservice.activity.Take_orderActivity;
import com.example.a93403.maintainerservice.bean.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{
    private List<Order> mOrderList;
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

    public OrderAdapter(List<Order> mOrderList) {
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
                Order order = mOrderList.get(position);
                OrderActivity.launchActivity(view.getContext(),order);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order order = mOrderList.get(position);
        holder.order_time.setText(order.getTime().toString());
        holder.order_type.setText(order.getCar_type());
        holder.order_distance.setText(String.valueOf(order.getDistance()));
        holder.order_describe.setText(order.getDescribe());

    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }
}
