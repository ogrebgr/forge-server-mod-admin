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


public class FindUserEp extends AdminDbEndpoint {
    static final String PARAM_PATTERN = "pattern";

    private final Gson mGson;

    private final UserExportedViewDbh mUserExportedViewDbh;


    public FindUserEp(DbPool dbPool, UserExportedViewDbh userExportedViewDbh) {
        super(dbPool);
        mUserExportedViewDbh = userExportedViewDbh;
        mGson = new Gson();
    }


    @Override
    public ForgeResponse handle(RequestContext ctx, Connection dbc, AdminUser user)
            throws ResponseException, SQLException {

        String pattern = ctx.getFromPost(PARAM_PATTERN);

        if (!Strings.isNullOrEmpty(pattern)) {
            List<UserExportedView> rez = mUserExportedViewDbh.findByPattern(dbc, pattern);
            return new ForgeResponse(BasicResponseCodes.Oks.OK, mGson.toJson(rez));
        } else {
            return MissingParametersResponse.getInstance();
        }
    }
}