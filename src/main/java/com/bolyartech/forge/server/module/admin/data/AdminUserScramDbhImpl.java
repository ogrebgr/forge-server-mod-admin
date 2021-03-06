package com.bolyartech.forge.server.module.admin.data;

import com.bolyartech.forge.server.module.user_scram.data.scram.Scram;
import com.bolyartech.forge.server.module.user_scram.data.scram.ScramDbh;
import com.bolyartech.scram_sasl.common.ScramUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class AdminUserScramDbhImpl implements AdminUserScramDbh {
    @Override
    public AdminUserScram createNew(Connection dbc,
                                    AdminUserDbh adminUserDbh,
                                    ScramDbh scramDbh,
                                    boolean isSuperAdmin,
                                    String name,
                                    String username,
                                    ScramUtils.NewPasswordStringData data) throws SQLException {

        try {
            String sqlLock = "LOCK TABLES admin_users WRITE, admin_user_scram WRITE";
            Statement stLock = dbc.createStatement();
            stLock.execute(sqlLock);

            if (!scramDbh.usernameExists(dbc, username)) {
                try {
                    dbc.setAutoCommit(false);
                    AdminUser user = adminUserDbh.createNew(dbc, isSuperAdmin, name);
                    Scram scram = scramDbh.createNew(dbc, user.getId(), username, data);
                    dbc.commit();
                    return new AdminUserScram(user, scram);
                } catch (SQLException e) {
                    dbc.rollback();
                    throw e;
                } finally {
                    dbc.setAutoCommit(true);
                }
            } else {
                return null;
            }
        } finally {
            String sqlLock = "UNLOCK TABLES";
            Statement stLock = dbc.createStatement();
            stLock.execute(sqlLock);
        }
    }
}
