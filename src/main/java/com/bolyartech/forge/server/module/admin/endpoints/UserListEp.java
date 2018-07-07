package com.bolyartech.forge.server.module.admin.endpoints;

import com.bolyartech.forge.server.db.DbPool;
import com.bolyartech.forge.server.module.admin.AdminDbEndpoint;
import com.bolyartech.forge.server.module.admin.data.AdminUser;
import com.bolyartech.forge.server.module.admin.data.UserExportedView;
import com.bolyartech.forge.server.module.admin.data.UserExportedViewDbh;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.BasicResponseCodes;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.response.forge.MissingParametersResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.google.common.base.Strings;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class UserListEp extends AdminDbEndpoint {
    private static final int USERS_PAGE_SIZE = 10;

    private final UserExportedViewDbh userExportedViewDbh;

    private final Gson gson;


    public UserListEp(DbPool dbPool, UserExportedViewDbh userExportedViewDbh) {
        super(dbPool);
        this.userExportedViewDbh = userExportedViewDbh;
        gson = new Gson();
    }


    @Override
    public ForgeResponse handle(RequestContext ctx,
                                Connection dbc,
                                AdminUser user) throws ResponseException, SQLException {

        String idGreaterThanRaw = ctx.getFromPost("id");
        if (Strings.isNullOrEmpty(idGreaterThanRaw)) {
            long id = 0;
            if (!Strings.isNullOrEmpty(idGreaterThanRaw)) {
                try {
                    id = Long.parseLong(idGreaterThanRaw);
                } catch (NumberFormatException e) {
                    return new ForgeResponse(BasicResponseCodes.Errors.INVALID_PARAMETER_VALUE, "Invalid id: " + id);
                }

            }

            List<UserExportedView> users = userExportedViewDbh.list(dbc, id, USERS_PAGE_SIZE);

            return new ForgeResponse(BasicResponseCodes.Oks.OK, gson.toJson(users));
        } else {
            return MissingParametersResponse.getInstance();
        }
    }
}
