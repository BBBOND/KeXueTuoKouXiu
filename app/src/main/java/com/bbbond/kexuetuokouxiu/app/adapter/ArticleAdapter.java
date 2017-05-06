package com.bbbond.kexuetuokouxiu.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.bean.Programme;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by bbbond on 2017/5/2.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private Context context;
    private List<Programme> programmeList;
    private OnClickListener listener;

    public ArticleAdapter(Context context, List<Programme> programmeList) {
        this.context = context;
        this.programmeList = programmeList;
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_article, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;
        Programme programme = programmeList.get(pos);
        holder.tvTitle.setText(programme.getTitle());
        holder.tvCategory.setText(context.getString(R.string.type, programme.getCategory()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        holder.tvPubDate.setText(sdf.format(new Date(programme.getPubDate())));
        holder.tvContent.setText(programme.getSummary());
        holder.itemView.setTag(programme);
        if (listener != null)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.click(pos);
                }
            });
    }

    @Override
    public int getItemCount() {
        return programmeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvCategory;
        TextView tvPubDate;
        TextView tvContent;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
            tvPubDate = (TextView) itemView.findViewById(R.id.tvPubDate);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
        }
    }

    public interface OnClickListener {
        void click(int position);
    }
}
