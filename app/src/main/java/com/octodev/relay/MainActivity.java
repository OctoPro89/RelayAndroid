package com.octodev.relay;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private static final int BACKGROUND = Color.BLACK;
    private static final int SURFACE = Color.rgb(38, 38, 41);
    private static final int TEXT = Color.WHITE;
    private static final int SECONDARY_TEXT = Color.rgb(160, 160, 165);

    private TorrentAdapter torrentAdapter;
    private EditText searchBar;
    private RecyclerView tableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(BACKGROUND);
        getWindow().setNavigationBarColor(BACKGROUND);
        getWindow().getDecorView().setSystemUiVisibility(0);

        setupUI();
    }

    private void setupUI() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BACKGROUND);

        setupToolbar(root);
        setupSearchBar(root);
        setupTableView(root);
        loadFakeTorrents();

        setContentView(root);
    }

    private void setupToolbar(LinearLayout root) {
        LinearLayout toolbar = new LinearLayout(this);
        toolbar.setOrientation(LinearLayout.HORIZONTAL);
        toolbar.setGravity(Gravity.CENTER_VERTICAL);
        toolbar.setPadding(dp(4), 0, dp(4), 0);
        toolbar.setBackgroundColor(BACKGROUND);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(dp(44), dp(44));

        TextView settingsButton = new TextView(this);
        settingsButton.setText("⚙");
        settingsButton.setTextColor(TEXT);
        settingsButton.setTextSize(20);
        settingsButton.setGravity(Gravity.CENTER);
        toolbar.addView(settingsButton, buttonParams);

        TextView title = new TextView(this);
        title.setText("Torrents");
        title.setTextColor(TEXT);
        title.setTextSize(18);
        title.setGravity(Gravity.CENTER);

        toolbar.addView(title, new LinearLayout.LayoutParams(0, dp(44), 1));

        TextView addButton = new TextView(this);
        addButton.setText("+");
        addButton.setTextColor(TEXT);
        addButton.setTextSize(26);
        addButton.setGravity(Gravity.CENTER);
        toolbar.addView(addButton, buttonParams);

        root.addView(toolbar, new LinearLayout.LayoutParams(-1, dp(44)));
    }
    private void setupSearchBar(LinearLayout root) {
        searchBar = new EditText(this);
        searchBar.setHint("Search torrents");
        searchBar.setHintTextColor(SECONDARY_TEXT);
        searchBar.setTextColor(TEXT);
        searchBar.setTextSize(16);
        searchBar.setSingleLine(true);
        searchBar.setPadding(dp(16), 0, dp(16), 0);

        GradientDrawable background = new GradientDrawable();
        background.setColor(SURFACE);
        background.setCornerRadius(dp(10));
        searchBar.setBackground(background);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, dp(48));
        params.setMargins(dp(8), dp(8), dp(8), dp(8));

        root.addView(searchBar, params);
    }

    private void setupTableView(LinearLayout root) {
        tableView = new RecyclerView(this);
        tableView.setLayoutManager(new LinearLayoutManager(this));

        torrentAdapter = new TorrentAdapter();
        tableView.setAdapter(torrentAdapter);

        root.addView(tableView, new LinearLayout.LayoutParams(-1, 0, 1));
    }

    private void loadFakeTorrents() {
        TorrentItem torrent1 = new TorrentItem();
        torrent1.name = "Big Buck Bunny 4K";
        torrent1.size = 4294967296L;
        torrent1.active = true;
        torrent1.downloadFinished = false;
        torrent1.downloadState = "Downloading";
        torrent1.downloadSpeed = 12582912L;
        torrent1.progress = 0.73f;
        torrent1.cached = true;
        torrent1.downloadPresent = false;
        torrent1.allowZipped = true;
        torrent1.files = new java.util.ArrayList<>();

        TorrentItem torrent2 = new TorrentItem();
        torrent2.name = "Ubuntu 24.04 Desktop";
        torrent2.size = 5368709120L;
        torrent2.active = true;
        torrent2.downloadFinished = false;
        torrent2.downloadState = "Downloading";
        torrent2.downloadSpeed = 5242880L;
        torrent2.progress = 0.42f;
        torrent2.cached = false;
        torrent2.downloadPresent = false;
        torrent2.allowZipped = false;
        torrent2.files = new java.util.ArrayList<>();

        TorrentItem torrent3 = new TorrentItem();
        torrent3.name = "My Awesome Movie";
        torrent3.size = 2147483648L;
        torrent3.active = false;
        torrent3.downloadFinished = false;
        torrent3.downloadState = "Paused";
        torrent3.downloadSpeed = 0L;
        torrent3.progress = 0.31f;
        torrent3.cached = false;
        torrent3.downloadPresent = false;
        torrent3.allowZipped = true;
        torrent3.files = new java.util.ArrayList<>();

        TorrentItem torrent4 = new TorrentItem();
        torrent4.name = "Really Long Torrent Name That Should Get Truncated";
        torrent4.size = 10737418240L;
        torrent4.active = false;
        torrent4.downloadFinished = true;
        torrent4.downloadState = "Completed";
        torrent4.downloadSpeed = 0L;
        torrent4.progress = 1.0f;
        torrent4.cached = true;
        torrent4.downloadPresent = true;
        torrent4.allowZipped = false;
        torrent4.files = new java.util.ArrayList<>();

        TorrentItem torrent5 = new TorrentItem();
        torrent5.name = "Really Long Torrent Name That Should Get Truncated";
        torrent5.size = 10737418240L;
        torrent5.active = false;
        torrent5.downloadFinished = true;
        torrent5.downloadState = "Completed";
        torrent5.downloadSpeed = 0L;
        torrent5.progress = 1.0f;
        torrent5.cached = true;
        torrent5.downloadPresent = true;
        torrent5.allowZipped = false;
        torrent5.files = new java.util.ArrayList<>();

        TorrentItem torrent6 = new TorrentItem();
        torrent6.name = "Really Long Torrent Name That Should Get Truncated";
        torrent6.size = 10737418240L;
        torrent6.active = false;
        torrent6.downloadFinished = true;
        torrent6.downloadState = "Completed";
        torrent6.downloadSpeed = 0L;
        torrent6.progress = 1.0f;
        torrent6.cached = true;
        torrent6.downloadPresent = true;
        torrent6.allowZipped = false;
        torrent6.files = new java.util.ArrayList<>();

        torrentAdapter.setTorrents(java.util.Arrays.asList(torrent1, torrent2, torrent3, torrent4, torrent5, torrent6));
    }

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
