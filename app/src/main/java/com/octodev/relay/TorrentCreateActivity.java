package com.octodev.relay;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.AppLaunchChecker;

public class TorrentCreateActivity extends AppCompatActivity {

    private EditText magnetInput;
    private Switch allowZipSwitch;
    private RadioGroup seedGroup;
    private TextView createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(ApplicationStyle.BACKGROUND);
        getWindow().setNavigationBarColor(ApplicationStyle.BACKGROUND);
        getWindow().getDecorView().setSystemUiVisibility(0);

        setupUI();
    }

    private void setupUI() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(ApplicationStyle.BACKGROUND);

        setupToolbar(root);
        setupForm(root);

        setContentView(root);
    }

    private void setupToolbar(LinearLayout root) {
        LinearLayout toolbar = new LinearLayout(this);
        toolbar.setOrientation(LinearLayout.HORIZONTAL);
        toolbar.setGravity(Gravity.CENTER_VERTICAL);
        toolbar.setPadding(dp(4), 0, dp(4), 0);

        TextView cancelButton = new TextView(this);
        cancelButton.setText("Cancel");
        cancelButton.setTextColor(ApplicationStyle.TEXT);
        cancelButton.setTextSize(16);
        cancelButton.setGravity(Gravity.CENTER);
        cancelButton.setOnClickListener(v -> finish());

        toolbar.addView(cancelButton, new LinearLayout.LayoutParams(dp(80), dp(48)));

        TextView title = new TextView(this);
        title.setText("Create Torrent");
        title.setTextColor(ApplicationStyle.TEXT);
        title.setTextSize(18);
        title.setGravity(Gravity.CENTER);

        toolbar.addView(title, new LinearLayout.LayoutParams(0, dp(48), 1));

        createButton = new TextView(this);
        createButton.setText("Create");
        createButton.setTextColor(ApplicationStyle.TEXT);
        createButton.setTextSize(16);
        createButton.setGravity(Gravity.CENTER);
        createButton.setOnClickListener(v -> createTorrent());
        createButton.setTextColor(ApplicationStyle.BLUE);

        toolbar.addView(createButton, new LinearLayout.LayoutParams(dp(80), dp(48)));

        root.addView(toolbar, new LinearLayout.LayoutParams(-1, dp(48)));
    }

    private void setupForm(LinearLayout root) {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(dp(20), dp(20), dp(20), dp(20));

        TextView magnetLabel = new TextView(this);
        magnetLabel.setText("Magnet Link");
        magnetLabel.setTextColor(ApplicationStyle.TEXT);
        magnetLabel.setTextSize(16);

        container.addView(magnetLabel, new LinearLayout.LayoutParams(-1, dp(40)));

        magnetInput = new EditText(this);
        magnetInput.setHint("magnet:?xt=urn:btih:...");
        magnetInput.setHintTextColor(ApplicationStyle.SECONDARY_TEXT);
        magnetInput.setTextColor(ApplicationStyle.TEXT);
        magnetInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        magnetInput.setSingleLine(false);

        container.addView(magnetInput, new LinearLayout.LayoutParams(-1, dp(100)));

        LinearLayout zipRow = new LinearLayout(this);
        zipRow.setOrientation(LinearLayout.HORIZONTAL);
        zipRow.setGravity(Gravity.CENTER_VERTICAL);
        zipRow.setPadding(0, dp(4), 0, dp(4));

        TextView zipLabel = new TextView(this);
        zipLabel.setText("Allow Zip Packaging");
        zipLabel.setTextColor(ApplicationStyle.TEXT);
        zipLabel.setTextSize(16);
        zipLabel.setGravity(Gravity.CENTER_VERTICAL);

        zipRow.addView(zipLabel, new LinearLayout.LayoutParams(0, dp(56), 1));

        allowZipSwitch = new Switch(this);
        allowZipSwitch.setChecked(true);
        allowZipSwitch.setGravity(Gravity.CENTER_VERTICAL);

        allowZipSwitch.setTrackTintList(ColorStateList.valueOf(ApplicationStyle.SECONDARY_TEXT));

        int[][] states = new int[][] {
                new int[] { android.R.attr.state_checked },
                new int[] {}
        };

        int[] colors = new int[] {
                ApplicationStyle.BLUE, // checked
                ApplicationStyle.TEXT  // unchecked
        };

        allowZipSwitch.setThumbTintList(new ColorStateList(states, colors));

        zipRow.addView(allowZipSwitch,  new LinearLayout.LayoutParams(-2, dp(56)));

        container.addView(zipRow);

        TextView seedLabel = new TextView(this);
        seedLabel.setText("Seeding Action");
        seedLabel.setTextColor(ApplicationStyle.TEXT);
        seedLabel.setTextSize(16);

        LinearLayout.LayoutParams seedLabelParams = new LinearLayout.LayoutParams(-1, dp(40));
        seedLabelParams.topMargin = dp(16);
        container.addView(seedLabel, seedLabelParams);

        seedGroup = new RadioGroup(this);
        seedGroup.setOrientation(RadioGroup.VERTICAL);

        RadioButton auto = new RadioButton(this);
        auto.setText("Auto");
        auto.setTextColor(ApplicationStyle.TEXT);
        auto.setId(1001);

        RadioButton seed = new RadioButton(this);
        seed.setText("Seed");
        seed.setTextColor(ApplicationStyle.TEXT);
        seed.setId(1002);

        RadioButton dontSeed = new RadioButton(this);
        dontSeed.setText("Don't Seed");
        dontSeed.setTextColor(ApplicationStyle.TEXT);
        dontSeed.setId(1003);

        seedGroup.addView(auto);
        seedGroup.addView(seed);
        seedGroup.addView(dontSeed);
        auto.setChecked(true);

        container.addView(seedGroup);

        root.addView(container, new LinearLayout.LayoutParams(-1, 0, 1));
    }

    private void createTorrent() {
        String magnet = magnetInput.getText().toString().trim();

        if (magnet.isEmpty()) {
            showError("Please enter a valid magnet link.");
            return;
        }

        if (!magnet.toLowerCase(java.util.Locale.ROOT).startsWith("magnet:?")) {
            showError("Please enter a valid magnet link.");
            return;
        }

        String apiKey;

        try {
            apiKey = MainActivity.apiKeyStorage.load();
        } catch (Exception e) {
            showError("Unable to read API key.");
            return;
        }

        if (apiKey == null || apiKey.trim().isEmpty()) {
            showError("No TorBox API key configured.");
            return;
        }

        boolean allowZip = allowZipSwitch.isChecked();

        int seed;

        switch (seedGroup.getCheckedRadioButtonId()) {
            case 1002:
                seed = 2;
                break;
            case 1003:
                seed = 3;
                break;
            case 1001:
            default:
                seed = 1;
                break;
        }

        setCreating(true);

        MainActivity.apiClient.createTorrent(apiKey, magnet, null, allowZip, false, seed,
                new TorboxAPIClient.CreateTorrentCallback() {
                    @Override
                    public void onSuccess(TorboxAPIClient.CreateTorrentResponse response) {
                        runOnUiThread(() -> {
                            setCreating(false);

                            String detail = response.detail;

                            if (detail == null || detail.isEmpty()) {
                                detail = "Torrent added successfully.";
                            }

                            new AlertDialog.Builder(TorrentCreateActivity.this)
                                .setTitle("Create Torrent Success")
                                .setMessage(detail)
                                .setPositiveButton("OK", (dialog, which) -> {
                                    setResult(RESULT_OK);
                                    finish();
                                })
                                .show();
                        });
                    }

                    @Override
                    public void onError(Exception error) {
                        runOnUiThread(() -> {
                            setCreating(false);
                            showError("Failed to create torrent:\n\n" + error.getMessage());
                        });
                    }
                }
        );
    }

    private void setCreating(boolean creating) {
        magnetInput.setEnabled(!creating);
        allowZipSwitch.setEnabled(!creating);
        seedGroup.setEnabled(!creating);
        createButton.setEnabled(!creating);
        createButton.setAlpha(creating ? 0.5f : 1.0f);

        if (creating) {
            createButton.setText("...");
        } else {
            createButton.setText("Create");
        }
    }

    private void showError(String message) {
        new AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show();
    }

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}