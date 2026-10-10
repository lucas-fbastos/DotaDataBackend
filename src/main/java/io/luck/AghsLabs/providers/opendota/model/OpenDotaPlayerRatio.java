package io.luck.AghsLabs.providers.opendota.model;

public record OpenDotaPlayerRatio(
        String win,
        String lose
) {

    public static final OpenDotaPlayerRatio DEFAULT = new OpenDotaPlayerRatio("0","0");
}
