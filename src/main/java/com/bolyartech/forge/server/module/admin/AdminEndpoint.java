package com.bolyartech.forge.server.module.admin;

import com.bolyartech.forge.server.handler.ForgeSecureEndpoint;
import com.bolyartech.forge.server.module.admin.data.AdminUser;
import com.bolyartech.forge.server.module.user.UserResponseCodes;
import com.bolyartech.forge.server.response.ResponseException;
import com.bolyartech.forge.server.response.forge.ForgeResponse;
import com.bolyartech.forge.server.route.RequestContext;
import com.bolyartech.forge.server.session.Session;


abstract public class AdminEndpoint extends ForgeSecureEndpoint {
    abstract public ForgeResponse handle(RequestContext ctx, AdminUser user) throws ResponseException;


    @Override
    public ForgeResponse handleForgeSecure(RequestContext ctx) throws ResponseException {

        Session session = ctx.getSession();
        AdminUser user = session.getVar(SessionVars.VAR_USER);
        if (user != null) {
            return handle(ctx, user);
        } else {
            return new ForgeResponse(UserResponseCodes.Errors.NOT_LOGGED_IN, "Not logged in");
        }
    }
}
