package com.bolyartech.forge.server.module.admin.data;


import com.bolyartech.forge.server.module.user_scram.data.scram.Scram;


public final class AdminUserScram {
    private final AdminUser user;
    private final Scram scram;


    public AdminUserScram(AdminUser user, Scram scram) {
        this.user = user;
        this.scram = scram;
    }


    public AdminUser getUser() {
        return user;
    }


    public Scram getScram() {
        return scram;
    }

}
