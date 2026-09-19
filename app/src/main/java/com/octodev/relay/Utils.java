package com.octodev.relay;

public class Utils {
    public static void appendMetadata(StringBuilder builder, String value) {
        if (builder.length() > 0)
            builder.append(" • ");
        builder.append(value);
    }

    public static String formatSpeed(long speed) {
        double value = speed;
        if (value >= 1024.0 * 1024.0 * 1024.0)
            return String.format("%.1f GB/s", value / (1024.0 * 1024.0 * 1024.0));
        if (value >= 1024.0 * 1024.0)
            return String.format("%.1f MB/s", value / (1024.0 * 1024.0));
        if (value >= 1024.0)
            return String.format("%.1f KB/s", value / 1024.0);
        return String.format("%.0f B/s", value);
    }

    public static String formatSize(long size) {
        double value = size;
        if (value >= 1024.0 * 1024.0 * 1024.0)
            return String.format("%.2f GB", value / (1024.0 * 1024.0 * 1024.0));
        if (value >= 1024.0 * 1024.0)
            return String.format("%.1f MB", value / (1024.0 * 1024.0));
        if (value >= 1024.0)
            return String.format("%.1f KB", value / 1024.0);
        return size + " B";
    }
}
