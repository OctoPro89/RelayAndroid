package com.octodev.relay;

import com.google.gson.annotations.SerializedName;

public class TorrentData {

    @SerializedName("hash")
    public String hash;

    @SerializedName("torrent_id")
    public int torrentId;

    @SerializedName("auth_id")
    public String authId;
}
