package com.bolyartech.forge.server.module.admin.data;

import com.google.gson.annotations.SerializedName;


public final class UserExportedView {
    @SerializedName("id")
    public final long id;
    @SerializedName("username")
    private final String username;
    @SerializedName("screen_name")
    private final String screenName;
    @SerializedName("disabled")
    private final boolean isDisabled;


    public UserExportedView(long id, String username, String screenName, boolean isDisabled) {
        this.id = id;
        this.username = username;
        this.screenName = screenName;
        this.isDisabled = isDisabled;
    }
}
