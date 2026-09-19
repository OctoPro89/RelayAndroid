package com.octodev.relay;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TorrentDetailViewActivity extends AppCompatActivity {
    private TorrentItem torrent;

    private RecyclerView fileView;
    private TorrentFileTableViewAdapter fileAdapter;
    private ProgressBar progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(ApplicationStyle.BACKGROUND);
        getWindow().setNavigationBarColor(ApplicationStyle.BACKGROUND);
        getWindow().getDecorView().setSystemUiVisibility(0);

        setupUI();
    }

    private void setupUI() {
        torrent = (TorrentItem) getIntent().getSerializableExtra("torrent");

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(ApplicationStyle.BACKGROUND);

        setupToolbar(root);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(16), 0, dp(16), 0);

        setupDetails(content);

        TextView filesText = new TextView(this);

        int fileCount = torrent.files != null ? torrent.files.size() : 0;
        filesText.setText(String.format("FILES • %d", fileCount));
        filesText.setTextColor(ApplicationStyle.SECONDARY_TEXT);
        filesText.setTextSize(12);
        filesText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        content.addView(filesText, new LinearLayout.LayoutParams(-1, dp(28)));

        setupFileView(content);

        root.addView(content, new LinearLayout.LayoutParams(-1, 0, 1));
        setContentView(root);
    }

    private void setupToolbar(LinearLayout root) {
        LinearLayout toolbar = new LinearLayout(this);
        toolbar.setOrientation(LinearLayout.HORIZONTAL);
        toolbar.setGravity(Gravity.CENTER_VERTICAL);
        toolbar.setPadding(dp(4), 0, dp(4), 0);
        toolbar.setBackgroundColor(ApplicationStyle.BACKGROUND);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(dp(44), dp(44));

        ImageButton backButton = new ImageButton(this);
        backButton.setImageResource(R.drawable.ic_arrow_back);
        backButton.setColorFilter(ApplicationStyle.TEXT);
        backButton.setBackgroundColor(Color.TRANSPARENT);
        backButton.setContentDescription("Back");

        backButton.setOnClickListener(v -> finish());
        toolbar.addView(backButton, buttonParams);

        TextView title = new TextView(this);
        title.setText("Torrent");
        title.setTextColor(ApplicationStyle.TEXT);
        title.setTextSize(18);
        title.setGravity(Gravity.CENTER);

        toolbar.addView(title, new LinearLayout.LayoutParams(0, dp(44), 1));

        // spacing to even out
        TextView spacer = new TextView(this);

        toolbar.addView(spacer, buttonParams);
        root.addView(toolbar, new LinearLayout.LayoutParams(-1, dp(44)));
    }

    private void setupDetails(LinearLayout root) {
        FrameLayout nameRow = new FrameLayout(this);
        LinearLayout.LayoutParams nameRowParams = new LinearLayout.LayoutParams(-1, dp(28));
        nameRowParams.topMargin = dp(12);
        root.addView(nameRow, nameRowParams);


        TextView nameLabel = new TextView(this);
        nameLabel.setTextColor(ApplicationStyle.TEXT);
        nameLabel.setTextSize(16);
        nameLabel.setMaxLines(1);
        nameLabel.setEllipsize(TextUtils.TruncateAt.END);

        FrameLayout.LayoutParams nameParams = new FrameLayout.LayoutParams(-1, -1);
        nameParams.rightMargin = dp(70);
        nameRow.addView(nameLabel, nameParams);

        TextView progressLabel = new TextView(this);
        progressLabel.setTextColor(ApplicationStyle.TEXT);
        progressLabel.setTextSize(13);
        progressLabel.setTypeface(Typeface.MONOSPACE);
        progressLabel.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        nameRow.addView(progressLabel, new FrameLayout.LayoutParams(dp(70), -1, Gravity.RIGHT));

        TextView statusLabel = new TextView(this);
        statusLabel.setTextSize(12);
        statusLabel.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        root.addView(statusLabel, new LinearLayout.LayoutParams(-1, dp(20)));

        progressView = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressView.setMax(100);
        root.addView(progressView, new LinearLayout.LayoutParams(-1, dp(5)));

        LinearLayout bottomRow = new LinearLayout(this);
        bottomRow.setOrientation(LinearLayout.HORIZONTAL);
        bottomRow.setGravity(Gravity.CENTER_VERTICAL);
        root.addView(bottomRow, new LinearLayout.LayoutParams(-1, dp(22)));

        TextView sizeLabel = new TextView(this);
        sizeLabel.setTextColor(ApplicationStyle.SECONDARY_TEXT);
        sizeLabel.setTextSize(11);
        sizeLabel.setTypeface(Typeface.MONOSPACE);
        sizeLabel.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        sizeLabel.setSingleLine(true);
        bottomRow.addView(sizeLabel, new LinearLayout.LayoutParams(-2, -1));

        TextView metadataLabel = new TextView(this);
        metadataLabel.setTextColor(ApplicationStyle.SECONDARY_TEXT);
        metadataLabel.setTextSize(11);
        metadataLabel.setSingleLine(true);
        metadataLabel.setEllipsize(TextUtils.TruncateAt.END);
        metadataLabel.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams metadataParams = new LinearLayout.LayoutParams(0, -1, 1);
        metadataParams.leftMargin = dp(8);
        bottomRow.addView(metadataLabel, metadataParams);

        TextView speedLabel = new TextView(this);
        speedLabel.setTextColor(ApplicationStyle.SECONDARY_TEXT);
        speedLabel.setTextSize(11);
        speedLabel.setTypeface(Typeface.MONOSPACE);
        speedLabel.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        speedLabel.setSingleLine(true);
        bottomRow.addView(speedLabel, new LinearLayout.LayoutParams(-2, -1));

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

    private void setupFileView(LinearLayout root) {
        fileView = new RecyclerView(this);
        fileView.setLayoutManager(new LinearLayoutManager(this));

        fileAdapter = new TorrentFileTableViewAdapter();
        fileAdapter.setOnFileClickListener(fileItem -> {
            // TODO:
        });

        fileView.setAdapter(fileAdapter);
        fileAdapter.setFilesAndTorrentID(torrent.files, torrent.id);

        root.addView(fileView, new LinearLayout.LayoutParams(-1, 0, 1));
    }

    private void setProgressColor(int color) {
        GradientDrawable track = new GradientDrawable();
        track.setColor(Color.rgb(55, 55, 58));
        track.setCornerRadius(dp(100));

        GradientDrawable progress = new GradientDrawable();
        progress.setColor(color);
        progress.setCornerRadius(dp(100));

        LayerDrawable drawable = new LayerDrawable(new android.graphics.drawable.Drawable[]{track, progress});
        drawable.setId(0, android.R.id.background);
        drawable.setId(1, android.R.id.progress);
        drawable.setLayerWidth(1, -1);
        drawable.setLayerHeight(1, -1);

        progressView.setProgressDrawable(drawable);
    }

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
