package com.octodev.relay;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TorrentFileTableViewAdapter extends RecyclerView.Adapter<TorrentFileTableViewCell> {
    private final List<FileItem> files = new ArrayList<>();
    private int torrentID;
    private OnFileClickListener onFileClickListener;

    public interface OnFileClickListener {
        void onFileClick(FileItem file);
    }

    public void setOnFileClickListener(OnFileClickListener listener) {
        this.onFileClickListener = listener;
    }

    public void setFilesAndTorrentID(List<FileItem> files, int torrentID) {
        this.files.clear();
        this.files.addAll(files);
        this.torrentID = torrentID;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TorrentFileTableViewCell onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TorrentFileTableViewCell cell = new TorrentFileTableViewCell(parent.getContext());
        cell.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return cell;
    }

    @Override
    public void onBindViewHolder(@NonNull TorrentFileTableViewCell holder, int position) {
        FileItem file = files.get(position);
        holder.configure(file, torrentID);
        holder.itemView.setOnClickListener(v -> {
            if (onFileClickListener != null) {
                onFileClickListener.onFileClick(file);
            }
        });
    }

    @Override
    public int getItemCount() {
        return files.size();
    }
}