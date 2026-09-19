package com.octodev.relay;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetMyListResponse {

    @SerializedName("success")
    public Boolean success;

    @SerializedName("error")
    public String error;

    @SerializedName("detail")
    public String detail;

    @SerializedName("data")
    public List<TorrentItem> data;
}
