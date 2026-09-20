package com.octodev.relay;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.TouchDelegate;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.ClipData;
import android.content.ClipboardManager;
import androidx.appcompat.app.AlertDialog;

import androidx.recyclerview.widget.RecyclerView;

public class TorrentFileTableViewCell extends RecyclerView.ViewHolder {
    private final TextView nameLabel;
    private final TextView sizeLabel;
    private final TextView pathLabel;
    private int torrentID;
    private int fileID;

    public TorrentFileTableViewCell(Context context) {
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

        LinearLayout bottomRow = new LinearLayout(context);
        bottomRow.setOrientation(LinearLayout.HORIZONTAL);
        bottomRow.setGravity(Gravity.CENTER_VERTICAL);
        root.addView(bottomRow, new LinearLayout.LayoutParams(-1, dp(context, 22)));

        TextView requestDl = new TextView(context);
        requestDl.setTextColor(ApplicationStyle.BLUE);
        requestDl.setTextSize(13);
        requestDl.setText("Request DL");
        requestDl.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

        requestDl.setOnClickListener(v -> {
            String apiKey;

            try {
                apiKey = MainActivity.apiKeyStorage.load();
            } catch (Exception e) {
                e.printStackTrace();
                android.widget.Toast.makeText(context, "Unable to read API key.", android.widget.Toast.LENGTH_LONG).show();
                return;
            }

            if (apiKey == null || apiKey.trim().isEmpty()) {
                new androidx.appcompat.app.AlertDialog.Builder(context)
                    .setTitle("API Key not set")
                    .setMessage("Please set your API Key on the main page.")
                    .setPositiveButton("Ok", null)
                    .show();
                return;
            }

            MainActivity.apiClient.requestDownloadLink(apiKey, torrentID, fileID, new TorboxAPIClient.DownloadLinkCallback() {
                @Override
                public void onSuccess(TorboxAPIClient.DownloadLinkResponse response) {
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                        if (response == null || response.data == null || response.data.isEmpty()) {
                            showDownloadError(context);
                            return;
                        }

                        String downloadUrl = response.data;

                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

                        if (clipboard == null) {
                            showDownloadError(context);
                            return;
                        }

                        ClipData clip = ClipData.newPlainText("Direct download link", downloadUrl);
                        clipboard.setPrimaryClip(clip);

                        new AlertDialog.Builder(context)
                                .setTitle("Request DL")
                                .setMessage("Direct download link copied to clipboard.")
                                .setPositiveButton("OK", null)
                                .show();
                    });
                }

                @Override
                public void onError(Exception error) {
                    error.printStackTrace();
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> showDownloadError(context));
                }
            });
        });

        nameRow.addView(requestDl, new FrameLayout.LayoutParams(dp(context, 70), -1, Gravity.RIGHT));
        // make touch area bigger
        requestDl.post(() -> {
            Rect hitRect = new Rect();
            requestDl.getHitRect(hitRect);

            int horizontal = dp(context, 10);
            int vertical = dp(context, 10);

            hitRect.left -= horizontal;
            hitRect.right += horizontal;
            hitRect.top -= vertical;
            hitRect.bottom += vertical;

            nameRow.setTouchDelegate(new TouchDelegate(hitRect, requestDl));
        });

        sizeLabel = new TextView(context);
        sizeLabel.setTextColor(ApplicationStyle.SECONDARY_TEXT);
        sizeLabel.setTextSize(11);
        sizeLabel.setTypeface(Typeface.MONOSPACE);
        sizeLabel.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        sizeLabel.setSingleLine(true);
        bottomRow.addView(sizeLabel, new LinearLayout.LayoutParams(-2, -1));

        pathLabel = new TextView(context);
        pathLabel.setTextColor(ApplicationStyle.SECONDARY_TEXT);
        pathLabel.setTextSize(11);
        pathLabel.setSingleLine(true);
        pathLabel.setEllipsize(TextUtils.TruncateAt.END);
        pathLabel.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams metadataParams = new LinearLayout.LayoutParams(0, -1, 1);
        metadataParams.leftMargin = dp(context, 8);
        bottomRow.addView(pathLabel, metadataParams);
    }

    private static void showDownloadError(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Request DL")
                .setMessage("Failed to get direct download link.")
                .setPositiveButton("OK", null)
                .show();
    }

    public void configure(FileItem fileItem, Integer _torrentID) {
        String name = fileItem.name != null ? fileItem.name : "Unknown torrent";
        torrentID = _torrentID;
        fileID = fileItem.id;
        nameLabel.setText(name);
        sizeLabel.setText(Utils.formatSize(fileItem.size));
        pathLabel.setText(fileItem.absolutePath != null ? fileItem.absolutePath : "");
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
