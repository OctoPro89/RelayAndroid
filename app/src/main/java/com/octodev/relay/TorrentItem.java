package com.octodev.relay;

import java.util.List;

public class TorrentItem {
    public Integer id;
    public String authId;
    public Integer server;
    public String hash;
    public String name;
    public String magnet;
    public Long size;
    public Boolean active;
    public String createdAt;
    public String updatedAt;
    public Boolean allowZipped;
    public Float availability;
    public Boolean cached;
    public String cachedAt;
    public Boolean downloadFinished;
    public Boolean downloadPresent;
    public String downloadPath;
    public Long downloadSpeed;
    public String downloadState;
    public Long eta;
    public String expiresAt;
    public List<FileItem> files;
    public Boolean longTermSeeding;
    public String owner;
    public Integer peers;
    public Float progress;
    public Float ratio;
    public Boolean seedTorrent;
    public Integer seeds;
    public Boolean torrentFile;
    public Long totalDownloaded;
    public Long totalUploaded;
    public String tracker;
    public String trackerMessage;
    public Long uploadSpeed;
}
