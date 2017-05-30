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
    private OnLongClickListener longClickListener;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    public void setOnLongClickListener(OnLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_comment, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Comment comment = comments.get(position);
        holder.tvId.setText(String.format(Locale.CHINA, "#%1$s", position + 1));
        holder.tvCreator.setText(comment.getCreator());
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm", Locale.CHINA);
        holder.tvPubDate.setText(format.format(new Date(comment.getPubDate())));
        holder.tvContent.setText(comment.getDescription());
        if (longClickListener != null)
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return longClickListener.longClick(position, comment);
                }
            });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvId;
        TextView tvCreator;
        TextView tvPubDate;
        TextView tvContent;

        public ViewHolder(View itemView) {
            super(itemView);
            tvId = (TextView) itemView.findViewById(R.id.tv_id);
            tvCreator = (TextView) itemView.findViewById(R.id.tv_creator);
            tvPubDate = (TextView) itemView.findViewById(R.id.tv_pub_date);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    public interface OnLongClickListener {
        boolean longClick(int pos, Comment comment);
    }
}
