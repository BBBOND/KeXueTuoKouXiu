package com.bbbond.kexuetuokouxiu.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.bean.DownloadingItem;

import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadEvent;
import zlc.season.rxdownload2.entity.DownloadFlag;

/**
 * Created by bbbond on 2017/5/30.
 */

public class DownloadTabSecondAdapter extends RecyclerView.Adapter<DownloadTabSecondAdapter.ViewHolder> {

    private Context context;
    private List<DownloadingItem> downloadingItemList;
    private OnClickListener clickListener;
    private OnCloseClickListener closeClickListener;
    private Disposable subscribe;

    public DownloadTabSecondAdapter(Context context, List<DownloadingItem> downloadingItemList) {
        this.context = context;
        this.downloadingItemList = downloadingItemList;
    }

    public void setOnClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setOnCloseClickListener(OnCloseClickListener closeClickListener) {
        this.closeClickListener = closeClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_downloading_detail, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int pos = position;
        final DownloadingItem downloadingItem = downloadingItemList.get(pos);
        holder.tvTitle.setText(downloadingItem.getTitle());
        subscribe = RxDownload
                .getInstance(context)
                .receiveDownloadStatus(downloadingItem.getUrl())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DownloadEvent>() {
                    @Override
                    public void accept(DownloadEvent downloadEvent) throws Exception {
                        switch (downloadEvent.getFlag()) {
                            case DownloadFlag.PAUSED:
                                holder.pbProgress.setVisibility(View.GONE);
                                holder.tvSize.setText(String.format(Locale.CHINA, "已暂停 %1$s/%2$s", downloadEvent.getDownloadStatus().getFormatDownloadSize(), downloadEvent.getDownloadStatus().getFormatTotalSize()));
                                if (clickListener != null)
                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            clickListener.click(pos, downloadingItem, false);
                                        }
                                    });
                                break;
                            case DownloadFlag.STARTED:
                                holder.pbProgress.setVisibility(View.VISIBLE);
                                holder.pbProgress.setProgress((int) (downloadEvent.getDownloadStatus().getDownloadSize() * 100 / downloadEvent.getDownloadStatus().getTotalSize()));
                                holder.tvSize.setText(String.format(Locale.CHINA, "%1$s/%2$s", downloadEvent.getDownloadStatus().getFormatDownloadSize(), downloadEvent.getDownloadStatus().getFormatTotalSize()));
                                if (clickListener != null)
                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            clickListener.click(pos, downloadingItem, true);
                                        }
                                    });
                                break;
                            case DownloadFlag.COMPLETED:
                                holder.pbProgress.setVisibility(View.GONE);
                                holder.tvSize.setText(String.format(Locale.CHINA, "已完成 %1$s/%2$s", downloadEvent.getDownloadStatus().getFormatDownloadSize(), downloadEvent.getDownloadStatus().getFormatTotalSize()));
                                if (clickListener != null)
                                    holder.itemView.setOnClickListener(null);
                                if (subscribe.isDisposed()) {
                                    subscribe.dispose();
                                }
                                break;
                            case DownloadFlag.DELETED:
                            case DownloadFlag.CANCELED:
                                if (subscribe.isDisposed()) {
                                    subscribe.dispose();
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
        if (closeClickListener != null)
            holder.ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeClickListener.click(pos, downloadingItem);
                }
            });
    }

    @Override
    public int getItemCount() {
        return downloadingItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivClose;
        TextView tvTitle;
        ProgressBar pbProgress;
        TextView tvSize;

        public ViewHolder(View itemView) {
            super(itemView);
            ivClose = (ImageView) itemView.findViewById(R.id.iv_close);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            pbProgress = (ProgressBar) itemView.findViewById(R.id.pb_progress);
            tvSize = (TextView) itemView.findViewById(R.id.tv_size);
        }
    }

    public interface OnClickListener {
        void click(int position, DownloadingItem downloadingItem, boolean isDownloading);
    }

    public interface OnCloseClickListener {
        void click(int position, DownloadingItem downloadingItem);
    }
}
