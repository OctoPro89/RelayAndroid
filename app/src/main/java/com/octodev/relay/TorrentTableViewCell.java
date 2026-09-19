package com.octodev.relay;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class TorrentTableViewCell extends RecyclerView.ViewHolder {

    private final TextView nameLabel;
    private final TextView statusLabel;
    private final TextView progressLabel;
    private final TextView speedLabel;
    private final TextView sizeLabel;
    private final TextView metadataLabel;
    private final ProgressBar progressView;

    public TorrentTableViewCell(Context context) {
        super(createRoot(context));

        LinearLayout root = (LinearLayout)itemView;

        FrameLayout nameRow = new FrameLayout(context);
        root.addView(nameRow, new LinearLayout.LayoutParams(-1, dp(context, 24)));

        nameLabel = new TextView(context);
        nameLabel.setTextColor(ApplicationStyle.TEXT);
        nameLabel.setTextSize(16);
        nameLabel.setMaxLines(1);
        nameLabel.setEllipsize(TextUtils.TruncateAt.END);

        FrameLayout.LayoutParams nameParams = new FrameLayout.LayoutParams(-1, -1);
        nameParams.rightMargin = dp(context, 70);
        nameRow.addView(nameLabel, nameParams);

        progressLabel = new TextView(context);
        progressLabel.setTextColor(ApplicationStyle.TEXT);
        progressLabel.setTextSize(13);
        progressLabel.setTypeface(Typeface.MONOSPACE);
        progressLabel.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        nameRow.addView(progressLabel, new FrameLayout.LayoutParams(dp(context, 70), -1, Gravity.RIGHT));

        statusLabel = new TextView(context);
        statusLabel.setTextSize(12);
        statusLabel.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        root.addView(statusLabel, new LinearLayout.LayoutParams(-1, dp(context, 20)));

        progressView = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressView.setMax(100);
        root.addView(progressView, new LinearLayout.LayoutParams(-1, dp(context, 5)));

        LinearLayout bottomRow = new LinearLayout(context);
        bottomRow.setOrientation(LinearLayout.HORIZONTAL);
        bottomRow.setGravity(Gravity.CENTER_VERTICAL);
        root.addView(bottomRow, new LinearLayout.LayoutParams(-1, dp(context, 22)));

        sizeLabel = new TextView(context);
        sizeLabel.setTextColor(ApplicationStyle.SECONDARY_TEXT);
        sizeLabel.setTextSize(11);
        sizeLabel.setTypeface(Typeface.MONOSPACE);
        sizeLabel.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        sizeLabel.setSingleLine(true);
        bottomRow.addView(sizeLabel, new LinearLayout.LayoutParams(-2, -1));

        metadataLabel = new TextView(context);
        metadataLabel.setTextColor(ApplicationStyle.SECONDARY_TEXT);
        metadataLabel.setTextSize(11);
        metadataLabel.setSingleLine(true);
        metadataLabel.setEllipsize(TextUtils.TruncateAt.END);
        metadataLabel.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams metadataParams = new LinearLayout.LayoutParams(0, -1, 1);
        metadataParams.leftMargin = dp(context, 8);
        bottomRow.addView(metadataLabel, metadataParams);

        speedLabel = new TextView(context);
        speedLabel.setTextColor(ApplicationStyle.SECONDARY_TEXT);
        speedLabel.setTextSize(11);
        speedLabel.setTypeface(Typeface.MONOSPACE);
        speedLabel.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        speedLabel.setSingleLine(true);
        bottomRow.addView(speedLabel, new LinearLayout.LayoutParams(-2, -1));
    }

    public void configure(TorrentItem torrent) {
        String name = torrent.name != null ? torrent.name : "Unknown torrent";
        String state = torrent.downloadState != null ? torrent.downloadState : "";
        float progress = torrent.progress != null ? torrent.progress : 0.0f;

        progress = Math.max(0.0f, Math.min(1.0f, progress));

        nameLabel.setText(name);
        progressLabel.setText(String.format("%.1f%%", progress * 100.0f));
        progressView.setProgress((int) (progress * 100));

        if (Boolean.TRUE.equals(torrent.downloadFinished)) {
            statusLabel.setText("Completed");
            statusLabel.setTextColor(ApplicationStyle.GREEN);
            setProgressColor(ApplicationStyle.GREEN);
            speedLabel.setText("");
        } else if (!Boolean.TRUE.equals(torrent.active)) {
            statusLabel.setText(!state.isEmpty() ? state : "Paused");
            statusLabel.setTextColor(ApplicationStyle.SECONDARY_TEXT);
            setProgressColor(ApplicationStyle.SECONDARY_TEXT);
            speedLabel.setText("");
        } else {
            statusLabel.setText(!state.isEmpty() ? state : "Downloading");
            statusLabel.setTextColor(ApplicationStyle.BLUE);
            setProgressColor(ApplicationStyle.BLUE);

            long speed = torrent.downloadSpeed != null ? torrent.downloadSpeed : 0;
            speedLabel.setText(speed > 0 ? "↓ " + Utils.formatSpeed(speed) : "");
        }

        long size = torrent.size != null ? torrent.size : 0;
        sizeLabel.setText(Utils.formatSize(size));

        StringBuilder metadata = new StringBuilder();
        if (torrent.files != null && !torrent.files.isEmpty())
            metadata.append(torrent.files.size()).append(" files");
        if (Boolean.TRUE.equals(torrent.cached))
            Utils.appendMetadata(metadata, "Cached");
        if (Boolean.TRUE.equals(torrent.downloadPresent))
            Utils.appendMetadata(metadata, "Downloaded");
        if (Boolean.TRUE.equals(torrent.allowZipped))
            Utils.appendMetadata(metadata, "ZIP ALLOWED");

        metadataLabel.setText(metadata.toString());
    }

    private void setProgressColor(int color) {
        GradientDrawable track = new GradientDrawable();
        track.setColor(Color.rgb(55, 55, 58));
        track.setCornerRadius(dp(itemView.getContext(), 100));

        GradientDrawable progress = new GradientDrawable();
        progress.setColor(color);
        progress.setCornerRadius(dp(itemView.getContext(), 100));

        LayerDrawable drawable = new LayerDrawable(new android.graphics.drawable.Drawable[]{track, progress});
        drawable.setId(0, android.R.id.background);
        drawable.setId(1, android.R.id.progress);
        drawable.setLayerWidth(1, -1);
        drawable.setLayerHeight(1, -1);

        progressView.setProgressDrawable(drawable);
    }

    private static LinearLayout createRoot(Context context) {
        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(context, 16), dp(context, 8), dp(context, 16), dp(context, 8));
        root.setBackgroundColor(ApplicationStyle.BACKGROUND);
        return root;
    }

    private static int dp(Context context, float value) {
        return (int) (value * context.getResources().getDisplayMetrics().density + 0.5f);
    }
}
