package com.octodev.relay;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private TorrentAdapter torrentAdapter;
    private EditText searchBar;
    private RecyclerView tableView;

    public static APIKeyStorage apiKeyStorage;
    public static TorboxAPIClient apiClient;
    private TorrentItem clickedTorrent;
    private final java.util.List<TorrentItem> allTorrents = new java.util.ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(ApplicationStyle.BACKGROUND);
        getWindow().setNavigationBarColor(ApplicationStyle.BACKGROUND);
        getWindow().getDecorView().setSystemUiVisibility(0);

        apiKeyStorage = new APIKeyStorage(this);
        apiClient = new TorboxAPIClient();

        setupUI();
    }

    private void setupUI() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(ApplicationStyle.BACKGROUND);

        setupToolbar(root);
        setupSearchBar(root);
        setupTableView(root);
//        loadFakeTorrents();
        loadTorrents();

        setContentView(root);

        root.setFocusableInTouchMode(true);
        root.requestFocus();
    }

    private void setupToolbar(LinearLayout root) {
        LinearLayout toolbar = new LinearLayout(this);
        toolbar.setOrientation(LinearLayout.HORIZONTAL);
        toolbar.setGravity(Gravity.CENTER_VERTICAL);
        toolbar.setPadding(dp(4), 0, dp(4), 0);
        toolbar.setBackgroundColor(ApplicationStyle.BACKGROUND);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(dp(48), dp(48));

        // Settings button
        ImageButton settingsButton = new ImageButton(this);
        settingsButton.setImageResource(R.drawable.ic_settings);
        settingsButton.setColorFilter(ApplicationStyle.TEXT);
        settingsButton.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        settingsButton.setContentDescription("Settings");

        toolbar.addView(settingsButton, buttonParams);

        settingsButton.setOnClickListener(v -> promptForApiKey());

        // Title
        TextView title = new TextView(this);
        title.setText("Torrents");
        title.setTextColor(ApplicationStyle.TEXT);
        title.setTextSize(18);
        title.setGravity(Gravity.CENTER);

        toolbar.addView(title, new LinearLayout.LayoutParams(0, dp(48), 1));

        // Add button
        ImageButton addButton = new ImageButton(this);
        addButton.setImageResource(R.drawable.ic_add);
        addButton.setColorFilter(ApplicationStyle.TEXT);
        addButton.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        addButton.setContentDescription("Add torrent");

        toolbar.addView(addButton, buttonParams);
        root.addView(toolbar, new LinearLayout.LayoutParams(-1, dp(48)));
    }


    private void setupSearchBar(LinearLayout root) {
        searchBar = new EditText(this);
        searchBar.setHint("Search torrents");
        searchBar.setHintTextColor(ApplicationStyle.SECONDARY_TEXT);
        searchBar.setTextColor(ApplicationStyle.TEXT);
        searchBar.setTextSize(16);
        searchBar.setSingleLine(true);
        searchBar.setPadding(dp(16), 0, dp(16), 0);

        GradientDrawable background = new GradientDrawable();
        background.setColor(ApplicationStyle.SURFACE);
        background.setCornerRadius(dp(10));
        searchBar.setBackground(background);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, dp(48));
        params.setMargins(dp(8), dp(8), dp(8), dp(8));
        root.addView(searchBar, params);

        searchBar.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTorrents(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void setupTableView(LinearLayout root) {
        tableView = new RecyclerView(this);
        tableView.setLayoutManager(new LinearLayoutManager(this));

        torrentAdapter = new TorrentAdapter();
        torrentAdapter.setOnTorrentClickListener(torrent -> {
            Intent intent = new Intent(this, TorrentDetailViewActivity.class);
            intent.putExtra("torrent", torrent);
            startActivity(intent);
        });

        tableView.setAdapter(torrentAdapter);

        root.addView(tableView, new LinearLayout.LayoutParams(-1, 0, 1));
    }

    private void filterTorrents(String query) {
        String search = query.trim().toLowerCase(java.util.Locale.ROOT);

        if (search.isEmpty()) {
            torrentAdapter.setTorrents(new java.util.ArrayList<>(allTorrents));
            return;
        }

        java.util.List<TorrentItem> filtered = new java.util.ArrayList<>();

        for (TorrentItem torrent : allTorrents) {
            if (contains(torrent.name, search)  || contains(torrent.hash, search)  || contains(torrent.magnet, search) || contains(torrent.downloadState, search)) {
                filtered.add(torrent);
            }
        }

        torrentAdapter.setTorrents(filtered);
    }

    private boolean contains(String value, String search) {
        return value != null && value.toLowerCase(java.util.Locale.ROOT).contains(search);
    }

    private void loadTorrents() {
        String apiKey;

        try {
            apiKey = apiKeyStorage.load();
        } catch (Exception e) {
            e.printStackTrace();
            android.widget.Toast.makeText(this, "Unable to read API key.", android.widget.Toast.LENGTH_LONG).show();
            return;
        }

        if (apiKey == null || apiKey.trim().isEmpty()) {
            promptForApiKey();
            return;
        }

        apiClient.getMyList(
            apiKey,
            new TorboxAPIClient.MyListCallback() {
                @Override
                public void onSuccess(TorboxAPIClient.MyListResponse response) {
                    runOnUiThread(() -> {
                        allTorrents.clear();

                        if (response.data != null) {
                            allTorrents.addAll(response.data);
                        }

                        filterTorrents(searchBar.getText().toString());
                    });
                }

                @Override
                public void onError(Exception error) {
                    runOnUiThread(() -> {
                        android.widget.Toast.makeText(MainActivity.this, "Unable to load torrents: " + error.getMessage(), android.widget.Toast.LENGTH_LONG).show();
                    });
                }
            }
        );
    }

    private void saveApiKey(String apiKey) {
        try {
            apiKeyStorage.save(apiKey);
            loadTorrents();
        } catch (Exception e) {
            e.printStackTrace();
            android.widget.Toast.makeText(this, "Unable to save API key.", android.widget.Toast.LENGTH_LONG).show();
        }
    }

    private void promptForApiKey() {
        EditText input = new EditText(this);

        input.setHint("API key");
        input.setSingleLine(true);
        input.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);

        int padding = dp(24);

        LinearLayout container = new LinearLayout(this);
        container.setPadding(padding, 0, padding, 0);
        container.addView(
            input,
            new LinearLayout.LayoutParams(
                -1,
                dp(50)
            )
        );

        new androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("API Key Required")
            .setMessage("Please enter your TorBox API key.")
            .setView(container)
            .setCancelable(false)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Save", (dialog, which) -> {
                String key = input.getText()
                        .toString()
                        .trim();

                if (!key.isEmpty()) {
                    saveApiKey(input.getText().toString().trim());
                    loadTorrents();
                }
            })
            .show();
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

        allTorrents.clear();
        allTorrents.addAll(
            java.util.Arrays.asList(
                torrent1,
                torrent2,
                torrent3,
                torrent4,
                torrent5,
                torrent6
            )
        );

        torrentAdapter.setTorrents(allTorrents);
    }

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}