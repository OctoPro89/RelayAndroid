package com.octodev.relay;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TorboxAPIClient {
    private static final String BASE_URL = "https://api.torbox.app";

    private final OkHttpClient client;
    private final Gson gson;

    public TorboxAPIClient() {
        client = new OkHttpClient();
        gson = new Gson();
    }

    public void getMyList(String apiKey, MyListCallback callback) {
        String url = BASE_URL + "/v1/api/torrents/mylist" + "?offset=0" + "&limit=1000" + "&bypass_cache=false";

        Request request = new Request.Builder()
            .url(url)
            .get()
            .header("Authorization", "Bearer " + apiKey)
            .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try (Response res = response) {
                    if (!res.isSuccessful()) {
                        callback.onError(new IOException("HTTP " + res.code() + ": " + res.message()));
                        return;
                    }

                    if (res.body() == null) {
                        callback.onError(new IOException("Empty response"));
                        return;
                    }

                    String json = res.body().string();

                    MyListResponse result = gson.fromJson(json, MyListResponse.class);

                    if (result == null) {
                        callback.onError(new IOException("Invalid response"));
                        return;
                    }

                    if (Boolean.FALSE.equals(result.success)) {
                        String message = result.detail;

                        if (message == null || message.isEmpty()) {
                            message = result.error;
                        }

                        callback.onError(new IOException(
                                message != null ? message : "TorBox request failed"
                        ));
                        return;
                    }

                    callback.onSuccess(result);

                } catch (Exception e) {
                    callback.onError(e);
                }
            }
        });
    }

    public void requestDownloadLink(
            String apiKey, int torrentId, int fileId, DownloadLinkCallback callback) {

        String url = BASE_URL + "/v1/api/torrents/requestdl"
                + "?token=" + apiKey
                + "&torrent_id=" + torrentId
                + "&file_id=" + fileId
                + "&zip_link=false"
                + "&torrent_file=false"
                + "&user_ip="
                + "&redirect=false";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try (Response res = response) {
                    if (!res.isSuccessful()) {
                        callback.onError(new IOException(
                                "HTTP " + res.code() + ": " + res.message()));
                        return;
                    }

                    if (res.body() == null) {
                        callback.onError(new IOException("Empty response"));
                        return;
                    }

                    DownloadLinkResponse result =
                            gson.fromJson(res.body().string(), DownloadLinkResponse.class);

                    if (result == null) {
                        callback.onError(new IOException("Invalid response"));
                        return;
                    }

                    if (Boolean.FALSE.equals(result.success)) {
                        String message = result.detail;

                        if (message == null || message.isEmpty()) {
                            message = result.error;
                        }

                        callback.onError(new IOException(
                                message != null ? message : "TorBox request failed"));
                        return;
                    }

                    callback.onSuccess(result);
                } catch (Exception e) {
                    callback.onError(e);
                }
            }
        });
    }

    public void requestTorrentDownloadLink(
            String apiKey, int torrentId, boolean zippedLink, DownloadLinkCallback callback) {

        String url = BASE_URL + "/v1/api/torrents/requestdl"
                + "?token=" + apiKey
                + "&torrent_id=" + torrentId
                + "&zip_link=" + (zippedLink ? "true" : "false")
                + "&user_ip="
                + "&redirect=false";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try (Response res = response) {
                    if (!res.isSuccessful()) {
                        callback.onError(new IOException("HTTP " + res.code() + ": " + res.message()));
                        return;
                    }

                    if (res.body() == null) {
                        callback.onError(new IOException("Empty response"));
                        return;
                    }

                    DownloadLinkResponse result = gson.fromJson(res.body().string(), DownloadLinkResponse.class);

                    if (result == null) {
                        callback.onError(new IOException("Invalid response"));
                        return;
                    }

                    if (Boolean.FALSE.equals(result.success)) {
                        String message = result.detail;

                        if (message == null || message.isEmpty()) {
                            message = result.error;
                        }

                        callback.onError(new IOException(message != null ? message : "TorBox request failed"));
                        return;
                    }

                    callback.onSuccess(result);
                } catch (Exception e) {
                    callback.onError(e);
                }
            }
        });
    }

    public interface MyListCallback {
        void onSuccess(MyListResponse response);

        void onError(Exception error);
    }

    public static class MyListResponse {
        public Boolean success;
        public String error;
        public String detail;
        public List<TorrentItem> data;
    }

    public interface DownloadLinkCallback {
        void onSuccess(DownloadLinkResponse response);
        void onError(Exception error);
    }

    public static class DownloadLinkResponse {
        public Boolean success;
        public String error;
        public String detail;
        public String data;
    }
}
