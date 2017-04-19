package com.bbbond.kexuetuokouxiu.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.bean.Programme;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Weya on 2016/11/11.
 */

public class ProgrammeAdapter extends RecyclerView.Adapter<ProgrammeAdapter.ViewHolder> {

    private Context context;
    private List<Programme> programmeList;
    private OnClickListener listener;

    public ProgrammeAdapter(Context context, List<Programme> programmeList) {
        this.context = context;
        this.programmeList = programmeList;
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_programme, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int pos = position;
        Programme programme = programmeList.get(position);
        holder.tvTitle.setText(programme.getTitle());
        holder.tvCreator.setText(programme.getCreator());
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd yyyy", Locale.CHINA);
        holder.tvPubDate.setText(sdf.format(new Date(programme.getPubDate())));
        holder.itemView.setTag(programme);
        if (listener != null)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.click(pos, holder.ivProgrammeImg);
                }
            });
    }

    @Override
    public int getItemCount() {
        return programmeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProgrammeImg;
        TextView tvTitle;
        TextView tvCreator;
        TextView tvPubDate;

        ViewHolder(View itemView) {
            super(itemView);
            ivProgrammeImg = (ImageView) itemView.findViewById(R.id.ivProgrammeImg);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvCreator = (TextView) itemView.findViewById(R.id.tvCreator);
            tvPubDate = (TextView) itemView.findViewById(R.id.tvPubDate);
        }
    }

    public interface OnClickListener {
        void click(int position, ImageView ivProgrammeImg);
    }
}
