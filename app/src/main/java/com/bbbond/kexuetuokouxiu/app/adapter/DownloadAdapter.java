package com.bbbond.kexuetuokouxiu.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.bean.DownloadItem;

import java.util.List;

/**
 * Created by bbbond on 2017/5/26.
 */

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {

    private Context context;
    private List<DownloadItem> downloadItemList;
    private OnClickListener listener;

    public DownloadAdapter(Context context, List<DownloadItem> downloadItemList) {
        this.context = context;
        this.downloadItemList = downloadItemList;
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_programme_cache, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;
        DownloadItem item = downloadItemList.get(pos);
        holder.title.setText(item.getTitle());
        holder.titleNum.setText(context.getResources().getString(R.string.download_num, item.getTitleNum()));
        String subTitle = "";
        if (item.getSize() <= 0) {
            subTitle = context.getResources().getString(R.string.download_no_data);
        } else if (item.getSize() - item.getCachedSize() <= 0) {
            subTitle = context.getResources().getString(R.string.download_finish);
        } else if (item.getSize() - item.getCachedSize() > 0) {
            subTitle = context.getResources().getString(R.string.download_process, String.valueOf(item.getSize() - item.getCachedSize()), String.valueOf(item.getSize()));
        }
        holder.subTitle.setText(subTitle);
        if (listener != null)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.click(pos);
                }
            });
    }

    @Override
    public int getItemCount() {
        return downloadItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView titleNum;
        TextView subTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            titleNum = (TextView) itemView.findViewById(R.id.title_num);
            subTitle = (TextView) itemView.findViewById(R.id.subTitle);
        }
    }

    public interface OnClickListener {
        void click(int position);
    }
}
