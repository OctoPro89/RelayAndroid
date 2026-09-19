package com.octodev.relay;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FileItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    public int id;

    @SerializedName("hash")
    public String hash;

    @SerializedName("name")
    public String name;

    @SerializedName("absolute_path")
    public String absolutePath;

    @SerializedName("s3_path")
    public String s3Path;

    @SerializedName("short_name")
    public String shortName;

    @SerializedName("size")
    public long size;

    @SerializedName("mimetype")
    public String mimeType;

    @SerializedName("zipped")
    public boolean zipped;

    @SerializedName("infected")
    public boolean infected;

    @SerializedName("md5")
    public String md5;
}
