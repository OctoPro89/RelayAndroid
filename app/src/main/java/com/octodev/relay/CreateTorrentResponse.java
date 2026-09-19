package com.octodev.relay;

import com.google.gson.annotations.SerializedName;

public class CreateTorrentResponse {

    @SerializedName("success")
    public boolean success;

    @SerializedName("error")
    public String error;

    @SerializedName("detail")
    public String detail;

    @SerializedName("data")
    public TorrentData data;
}
