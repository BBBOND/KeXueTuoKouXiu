package com.bbbond.kexuetuokouxiu.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.bean.ProgrammeCache;

import java.util.List;

/**
 * Created by bbbond on 2017/5/15.
 */

public class DownloadTabFirstAdapter extends RecyclerView.Adapter<DownloadTabFirstAdapter.ViewHolder> {

    private Context context;
    private List<ProgrammeCache> programmeCacheList;
    private OnClickListener listener;

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public DownloadTabFirstAdapter(Context context, List<ProgrammeCache> programmeCacheList) {
        this.context = context;
        this.programmeCacheList = programmeCacheList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_download, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final int pos = position;
        final ProgrammeCache programmeCache = programmeCacheList.get(position);
        holder.tvTitle.setText(programmeCache.getTitle());
        holder.tvCreator.setText(programmeCache.getCreator());
        holder.itemView.setTag(programmeCache);
        if (listener != null)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.click(pos, programmeCache.getId());
                }
            });
    }

    @Override
    public int getItemCount() {
        return programmeCacheList == null ? 0 : programmeCacheList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvCreator;
        TextView tvSize;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvCreator = (TextView) itemView.findViewById(R.id.tv_creator);
            tvSize = (TextView) itemView.findViewById(R.id.tv_size);
        }
    }

    public interface OnClickListener {
        void click(int position, String id);
    }
}
