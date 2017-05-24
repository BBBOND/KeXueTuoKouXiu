package com.bbbond.kexuetuokouxiu.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.bean.Programme;

import java.util.List;

/**
 * Created by bbbond on 2017/5/24.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private Context context;
    private List<Programme> programmeList;
    private OnClickListener listener;

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public SearchAdapter(Context context, List<Programme> programmeList) {
        this.context = context;
        this.programmeList = programmeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_programme_search, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final int pos = position;
        final Programme programme = programmeList.get(position);
        holder.tvTitle.setText(programme.getTitle());
        holder.tvCreator.setText(programme.getCreator());
        holder.itemView.setTag(programme);
        if (listener != null)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.click(pos, programme.getId());
                }
            });
    }

    @Override
    public int getItemCount() {
        return programmeList == null ? 0 : programmeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvCreator;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvCreator = (TextView) itemView.findViewById(R.id.tv_creator);
        }
    }

    public interface OnClickListener {
        void click(int position, String id);
    }
}