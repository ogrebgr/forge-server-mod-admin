package com.bolyartech.forge.server.module.admin.data;

import com.bolyartech.forge.server.module.user.data.scram.Scram;


public final class AdminUserScram {
    private final AdminUser mUser;
    private final Scram mScram;


    public AdminUserScram(AdminUser user, Scram scram) {
        mUser = user;
        mScram = scram;
    }


    public AdminUser getUser() {
        return mUser;
    }


    public Scram getScram() {
        return mScram;
    }

}
