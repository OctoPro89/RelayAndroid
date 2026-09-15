package com.octodev.relay;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TorrentAdapter extends RecyclerView.Adapter<TorrentTableViewCell> {
    private final List<TorrentItem> torrents = new ArrayList<>();

    public void setTorrents(List<TorrentItem> torrents) {
        this.torrents.clear();
        this.torrents.addAll(torrents);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TorrentTableViewCell onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TorrentTableViewCell cell = new TorrentTableViewCell(parent.getContext());
        cell.itemView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        return cell;
    }

    @Override
    public void onBindViewHolder(@NonNull TorrentTableViewCell holder, int position) {
        holder.configure(torrents.get(position));
    }

    @Override
    public int getItemCount() {
        return torrents.size();
    }
}