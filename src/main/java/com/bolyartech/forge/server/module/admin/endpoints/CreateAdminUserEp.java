package com.bolyartech.forge.server.module.admin.endpoints;

import com.bolyartech.forge.server.db.DbPool;
import com.bolyartech.forge.server.misc.Params;
import com.bolyartech.forge.server.module.admin.AdminDbEndpoint;
import com.bolyartech.forge.server.module.admin.data.AdminUser;
import com.bolyartech.forge.server.module.admin.data.AdminUserDbh;
import com.bolyartech.forge.server.module.admin.data.AdminUserScram;
import com.bolyartech.forge.server.module.admin.data.AdminUserScramDbh;
import com.bolyartech.forge.server.module.user.UserResponseCodes;
import com.bolyartech.forge.server.module.user.data.user.User;
import com.bolyartech.forge.server.module.user_scram.data.scram.ScramDbh;
import com.bolyartech.forge.server.module.user_scram.data.scram.UserScramUtils;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.response.forge.MissingParametersResponse;
import com.bolyartech.forge.server.response.forge.OkResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.scram_sasl.common.ScramUtils;

import java.sql.Connection;
import java.sql.SQLException;


public class CreateAdminUserEp extends AdminDbEndpoint {
    static final int INVALID_NAME = -100;

    static final String PARAM_USERNAME = "username";
    static final String PARAM_PASSWORD = "password";
    static final String PARAM_NAME = "name";
    static final String PARAM_SUPER_ADMIN = "super_admin";


    private final AdminUserDbh adminUserDbh;
    private final ScramDbh adminScramDbh;
    private final AdminUserScramDbh adminUserScramDbh;


    public CreateAdminUserEp(DbPool dbPool, AdminUserDbh adminUserDbh, ScramDbh adminScramDbh,
                             AdminUserScramDbh adminUserScramDbh) {
        super(dbPool);
        this.adminUserDbh = adminUserDbh;
        this.adminScramDbh = adminScramDbh;
        this.adminUserScramDbh = adminUserScramDbh;
    }


    @Override
    public ForgeResponse handle(RequestContext ctx, Connection dbc, AdminUser user)
            throws ResponseException, SQLException {


        if (user.isSuperAdmin()) {
            String username = ctx.getFromPost(PARAM_USERNAME);
            String password = ctx.getFromPost(PARAM_PASSWORD);
            String name = ctx.getFromPost(PARAM_NAME);
            String superAdminRaw = ctx.getFromPost(PARAM_SUPER_ADMIN);

            if (Params.areAllPresent(username, password, name)) {
                if (!User.isValidUsername(username)) {
                    return new ForgeResponse(UserResponseCodes.Errors.INVALID_USERNAME.getCode(), "Invalid username");
                }

                if (!AdminUser.isValidName(name)) {
                    return new ForgeResponse(INVALID_NAME, "Invalid name");
                }

                if (!AdminUser.isValidPasswordLength(password)) {
                    return new ForgeResponse(UserResponseCodes.Errors.PASSWORD_TOO_SHORT.getCode(), "Invalid screen name");
                }

                boolean superAdmin;
                superAdmin = superAdminRaw != null && superAdminRaw.equals("1");

                ScramUtils.NewPasswordStringData data = UserScramUtils.createPasswordData(password);

                AdminUserScram scram = adminUserScramDbh.createNew(dbc, adminUserDbh, adminScramDbh,
                        superAdmin, name, username, data);

                if (scram != null) {
                    return new OkResponse();
                } else {
                    return new ForgeResponse(UserResponseCodes.Errors.USERNAME_EXISTS, "Invalid Login");
                }
            } else {
                return MissingParametersResponse.getInstance();
            }
        } else {
            return new ForgeResponse(UserResponseCodes.Errors.NO_ENOUGH_PRIVILEGES, "Missing parameters");
        }
    }
}
