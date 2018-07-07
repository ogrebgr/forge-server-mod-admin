package com.bolyartech.forge.server.module.admin;

import com.bolyartech.forge.server.db.DbPool;
import com.bolyartech.forge.server.module.HttpModule;
import com.bolyartech.forge.server.module.admin.data.*;
import com.bolyartech.forge.server.module.admin.endpoints.*;
import com.bolyartech.forge.server.module.user.data.user.UserDbh;
import com.bolyartech.forge.server.module.user.data.user.UserDbhImpl;
import com.bolyartech.forge.server.module.user_scram.data.scram.ScramDbh;
import com.bolyartech.forge.server.route.PostRoute;
import com.bolyartech.forge.server.route.Route;

import java.util.ArrayList;
import java.util.List;


public class AdminModule implements HttpModule {
    private static final String DEFAULT_PATH_PREFIX = "/api/admin/";

    private static final String MODULE_SYSTEM_NAME = "admin";
    private static final int MODULE_VERSION_CODE = 1;
    private static final String MODULE_VERSION_NAME = "1.0.0";

    private final String pathPrefix;
    private final DbPool dbPool;
    private final AdminUserDbh adminUserDbh;
    private final ScramDbh userScramDbh;
    private final ScramDbh adminScramDbh;
    private final UserDbh userDbh;
    private final AdminUserScramDbh adminUserScramDbh;
    private final UserExportedViewDbh userExportedViewDbh;
    private final AdminUserExportedViewDbh adminUserExportedViewDbh;


    public AdminModule(String pathPrefix,
                       DbPool dbPool,
                       AdminUserDbh adminUserDbh,
                       ScramDbh userScramDbh,
                       ScramDbh adminScramDbh,
                       UserDbh userDbh,
                       AdminUserScramDbh adminUserScramDbh,
                       UserExportedViewDbh userExportedViewDbh,
                       AdminUserExportedViewDbh adminUserExportedViewDbh) {

        this.pathPrefix = pathPrefix;
        this.dbPool = dbPool;
        this.adminUserDbh = adminUserDbh;
        this.userScramDbh = userScramDbh;
        this.adminScramDbh = adminScramDbh;
        this.userDbh = userDbh;
        this.adminUserScramDbh = adminUserScramDbh;
        this.userExportedViewDbh = userExportedViewDbh;
        this.adminUserExportedViewDbh = adminUserExportedViewDbh;

    }


    public AdminModule(
            DbPool dbPool,
            AdminUserDbh adminUserDbh,
            ScramDbh userScramDbh,
            ScramDbh adminScramDbh,
            UserDbh userDbh,
            AdminUserScramDbh adminUserScramDbh,
            UserExportedViewDbh userExportedViewDbh,
            AdminUserExportedViewDbh adminUserExportedViewDbh) {

        pathPrefix = DEFAULT_PATH_PREFIX;
        this.dbPool = dbPool;
        this.adminUserDbh = adminUserDbh;
        this.userScramDbh = userScramDbh;
        this.adminScramDbh = adminScramDbh;
        this.userDbh = userDbh;
        this.adminUserScramDbh = adminUserScramDbh;
        this.userExportedViewDbh = userExportedViewDbh;
        this.adminUserExportedViewDbh = adminUserExportedViewDbh;
    }


    public static AdminModule createDefault(DbPool dbPool) {
        return new AdminModule(dbPool,
                new AdminUserDbhImpl(),
                new AdminScramDbhImpl(),
                new AdminScramDbhImpl(),
                new UserDbhImpl(),
                new AdminUserScramDbhImpl(),
                new UserExportedViewDbhImpl(),
                new AdminUserExportedViewDbhImpl());
    }


    @Override
    public List<Route> createRoutes() {
        List<Route> ret = new ArrayList<>();

        ret.add(new PostRoute(pathPrefix + "login",
                new LoginEp(dbPool, adminUserDbh, adminScramDbh)));
        ret.add(new PostRoute(pathPrefix + "logout",
                new LogoutEp()));
        ret.add(new PostRoute(pathPrefix + "users",
                new UserListEp(dbPool, userExportedViewDbh)));
        ret.add(new PostRoute(pathPrefix + "user_find",
                new FindUserEp(dbPool, userExportedViewDbh)));
        ret.add(new PostRoute(pathPrefix + "user_disable",
                new DisableUserEp(dbPool, userDbh)));
        ret.add(new PostRoute(pathPrefix + "user_chpwd",
                new ChangePasswordEp(dbPool, userScramDbh)));
        ret.add(new PostRoute(pathPrefix + "admin_users",
                new AdminUserListEp(dbPool, adminUserExportedViewDbh)));
        ret.add(new PostRoute(pathPrefix + "admin_user_chpwd",
                new ChangeAdminPasswordEp(dbPool, adminScramDbh)));
        ret.add(new PostRoute(pathPrefix + "chpwd_own",
                new ChangeAdminPasswordEp(dbPool, adminScramDbh)));
        ret.add(new PostRoute(pathPrefix + "admin_user_create",
                new CreateAdminUserEp(dbPool, adminUserDbh, adminScramDbh, adminUserScramDbh)));
        ret.add(new PostRoute(pathPrefix + "admin_user_disable",
                new DisableAdminUserEp(dbPool, adminUserDbh)));

        return ret;
    }


    @Override
    public String getSystemName() {
        return MODULE_SYSTEM_NAME;
    }


    @Override
    public String getShortDescription() {
        return "";
    }


    @Override
    public int getVersionCode() {
        return MODULE_VERSION_CODE;
    }


    @Override
    public String getVersionName() {
        return MODULE_VERSION_NAME;
    }
}
