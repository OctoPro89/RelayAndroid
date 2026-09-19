package com.octodev.relay;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TorrentItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    public int id;

    @SerializedName("auth_id")
    public String authId;

    @SerializedName("server")
    public int server;

    @SerializedName("hash")
    public String hash;

    @SerializedName("name")
    public String name;

    @SerializedName("magnet")
    public String magnet;

    @SerializedName("size")
    public Long size;

    @SerializedName("active")
    public Boolean active;

    @SerializedName("created_at")
    public Date createdAt;

    @SerializedName("updated_at")
    public Date updatedAt;

    @SerializedName("allow_zipped")
    public Boolean allowZipped;

    @SerializedName("availability")
    public Float availability;

    @SerializedName("cached")
    public Boolean cached;

    @SerializedName("cached_at")
    public Date cachedAt;

    @SerializedName("download_finished")
    public Boolean downloadFinished;

    @SerializedName("download_present")
    public Boolean downloadPresent;

    @SerializedName("download_path")
    public String downloadPath;

    @SerializedName("download_speed")
    public Long downloadSpeed;

    @SerializedName("download_state")
    public String downloadState;

    @SerializedName("eta")
    public Long eta;

    @SerializedName("expires_at")
    public Date expiresAt;

    @SerializedName("files")
    public List<FileItem> files;

    @SerializedName("long_term_seeding")
    public Boolean longTermSeeding;

    @SerializedName("owner")
    public String owner;

    @SerializedName("peers")
    public int peers;

    @SerializedName("progress")
    public Float progress;

    @SerializedName("ratio")
    public Float ratio;

    @SerializedName("seed_torrent")
    public Boolean seedTorrent;

    @SerializedName("seeds")
    public int seeds;

    @SerializedName("torrent_file")
    public Boolean torrentFile;

    @SerializedName("total_downloaded")
    public Long totalDownloaded;

    @SerializedName("total_uploaded")
    public Long totalUploaded;

    @SerializedName("tracker")
    public String tracker;

    @SerializedName("tracker_message")
    public String trackerMessage;

    @SerializedName("upload_speed")
    public Long uploadSpeed;
}
