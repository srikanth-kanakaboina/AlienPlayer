package com.jk.alienplayer.data;

import android.text.TextUtils;

public class ArtistInfo {
    public long id;
    public String name;

    public ArtistInfo(long id, String name) {
        this.id = id;
        if (TextUtils.isEmpty(name)) {
            this.name = "unknown";
        } else {
            this.name = name;
        }
    }

}
