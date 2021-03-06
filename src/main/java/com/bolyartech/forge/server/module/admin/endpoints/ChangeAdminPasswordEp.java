package com.bolyartech.forge.server.module.admin.endpoints;

import com.bolyartech.forge.server.db.DbPool;
import com.bolyartech.forge.server.misc.Params;
import com.bolyartech.forge.server.module.admin.AdminDbEndpoint;
import com.bolyartech.forge.server.module.admin.data.AdminUser;
import com.bolyartech.forge.server.module.user.UserResponseCodes;
import com.bolyartech.forge.server.module.user_scram.data.scram.ScramDbh;
import com.bolyartech.forge.server.module.user_scram.data.scram.UserScramUtils;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.BasicResponseCodes;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.response.forge.MissingParametersResponse;
import com.bolyartech.forge.server.response.forge.OkResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.scram_sasl.common.ScramUtils;

import java.sql.Connection;
import java.sql.SQLException;


public class ChangeAdminPasswordEp extends AdminDbEndpoint {
    static final int ERROR_USER_NOT_FOUND = -100;

    static final String PARAM_USER = "user";
    static final String PARAM_PASSWORD = "new_password";

    private final ScramDbh adminScramDbh;


    public ChangeAdminPasswordEp(DbPool dbPool, ScramDbh adminScramDbh) {
        super(dbPool);
        this.adminScramDbh = adminScramDbh;
    }


    @Override
    public ForgeResponse handle(RequestContext ctx, Connection dbc, AdminUser user)
            throws ResponseException, SQLException {


        if (user.isSuperAdmin()) {
            String userIdRaw = ctx.getFromPost(PARAM_USER);
            String newPassword = ctx.getFromPost(PARAM_PASSWORD);

            if (Params.areAllPresent(userIdRaw, newPassword)) {
                if (!AdminUser.isValidPasswordLength(newPassword)) {
                    return new ForgeResponse(UserResponseCodes.Errors.PASSWORD_TOO_SHORT.getCode(), "Invalid screen name");
                }

                try {
                    long userId = Long.parseLong(userIdRaw);
                    ScramUtils.NewPasswordStringData data = UserScramUtils.createPasswordData(newPassword);

                    if (adminScramDbh.changePassword(dbc, user.getId(), data)) {
                        return OkResponse.getInstance();
                    } else {
                        return new ForgeResponse(ERROR_USER_NOT_FOUND);
                    }

                } catch (NumberFormatException e) {
                    return new ForgeResponse(BasicResponseCodes.Errors.INVALID_PARAMETER_VALUE.getCode(), "Invalid id");
                }
            } else {
                return MissingParametersResponse.getInstance();
            }
        } else {
            return new ForgeResponse(UserResponseCodes.Errors.NO_ENOUGH_PRIVILEGES.getCode(), "Missing parameters");
        }
    }
}
