package com.bbbond.kexuetuokouxiu.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.bean.Comment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Weya on 2016/12/4.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context context;
    private List<Comment> comments;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_comment, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.tvCreator.setText(comment.getCreator());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-dd-MM hh:mm", Locale.CHINA);
        holder.tvPubDate.setText(format.format(new Date(comment.getPubDate())));
        holder.tvContent.setText(comment.getDescription());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCreator;
        TextView tvPubDate;
        TextView tvContent;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCreator = (TextView) itemView.findViewById(R.id.tvCreator);
            tvPubDate = (TextView) itemView.findViewById(R.id.tvPubDate);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
        }
    }
}
