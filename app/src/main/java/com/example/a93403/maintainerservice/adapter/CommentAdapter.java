package com.example.a93403.maintainerservice.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a93403.maintainerservice.R;
import com.example.a93403.maintainerservice.base.MyApplication;
import com.example.a93403.maintainerservice.bean.Comment;
import com.example.a93403.maintainerservice.widget.CircleImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Title: CarService
 * Date: Create in 2018/5/2 16:03
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Comment> commentList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView comment_image_view;
        TextView comment_name_tv;
        RatingBar comment_rating_bar;
        TextView comment_content_tv;
        TextView comment_time_tv;

        private ViewHolder(View itemView) {
            super(itemView);
            comment_image_view = itemView.findViewById(R.id.comment_image_view);
            comment_name_tv = itemView.findViewById(R.id.comment_name_tv);
            comment_rating_bar = itemView.findViewById(R.id.comment_rating_bar);
            comment_content_tv = itemView.findViewById(R.id.comment_content_tv);
            comment_time_tv = itemView.findViewById(R.id.comment_time_tv);
        }
    }

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position) {
        Comment comment = commentList.get(position);

        holder.comment_name_tv.setText(comment.getUsername());
        holder.comment_rating_bar.setRating(comment.getScore());
        holder.comment_content_tv.setText(comment.getContent());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String time = sdf.format(new Date(Long.valueOf(comment.getTime())));
        holder.comment_time_tv.setText(comment.getTime().toString());

        Glide.with(MyApplication.getContext())
                .load(comment.getPortrait())
                .error(R.drawable.load_fail)
                .into(holder.comment_image_view);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}
