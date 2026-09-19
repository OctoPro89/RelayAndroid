package com.octodev.relay;

import com.google.gson.annotations.SerializedName;

public class ControlTorrentResponse {

    @SerializedName("success")
    public Boolean success;

    @SerializedName("error")
    public String error;

    @SerializedName("detail")
    public String detail;
}
