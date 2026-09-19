package com.octodev.relay;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TorrentAdapter extends RecyclerView.Adapter<TorrentTableViewCell> {
    private final List<TorrentItem> torrents = new ArrayList<>();
    private OnTorrentClickListener onTorrentClickListener;

    public interface OnTorrentClickListener {
        void onTorrentClick(TorrentItem torrent);
    }

    public void setOnTorrentClickListener(OnTorrentClickListener listener) {
        this.onTorrentClickListener = listener;
    }

    public void setTorrents(List<TorrentItem> torrents) {
        this.torrents.clear();
        this.torrents.addAll(torrents);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TorrentTableViewCell onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TorrentTableViewCell cell = new TorrentTableViewCell(parent.getContext());
        cell.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return cell;
    }

    @Override
    public void onBindViewHolder(@NonNull TorrentTableViewCell holder,  int position) {
        TorrentItem torrent = torrents.get(position);
        holder.configure(torrent);
        holder.itemView.setOnClickListener(v -> {
            if (onTorrentClickListener != null) {
                onTorrentClickListener.onTorrentClick(torrent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return torrents.size();
    }
}
