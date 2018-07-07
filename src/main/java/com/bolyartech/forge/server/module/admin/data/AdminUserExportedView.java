package com.bolyartech.forge.server.module.admin.data;

import com.google.gson.annotations.SerializedName;


public class AdminUserExportedView {
    @SerializedName("id")
    private final long id;
    @SerializedName("username")
    private final String username;
    @SerializedName("is_disabled")
    private final boolean isDisabled;
    @SerializedName("is_super_admin")
    private final boolean isSuperAdmin;
    @SerializedName("name")
    private final String name;


    public AdminUserExportedView(long id, String username, boolean isDisabled, boolean isSuperAdmin, String name) {
        this.id = id;
        this.username = username;
        this.isDisabled = isDisabled;
        this.isSuperAdmin = isSuperAdmin;
        this.name = name;
    }


    public long getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }


    public boolean isDisabled() {
        return isDisabled;
    }


    public boolean isSuperAdmin() {
        return isSuperAdmin;
    }


    public String getName() {
        return name;
    }
}
