package com.bolyartech.forge.server.module.admin.data;

import com.bolyartech.forge.server.module.user.data.scram.ScramDbh;
import com.bolyartech.scram_sasl.common.ScramUtils;

import java.sql.Connection;
import java.sql.SQLException;


public interface AdminUserScramDbh {
    public AdminUserScram createNew(Connection dbc,
                                    AdminUserDbh adminUserDbh,
                                    ScramDbh scramDbh,
                                    boolean isSuperAdmin,
                                    String name,
                                    String username,
                                    ScramUtils.NewPasswordStringData data) throws SQLException;
}
