package com.bolyartech.forge.server.modules.admin.data;

import com.bolyartech.forge.server.config.ForgeConfigurationException;
import com.bolyartech.forge.server.db.*;
import com.bolyartech.forge.server.module.admin.data.UserExportedView;
import com.bolyartech.forge.server.module.admin.data.UserExportedViewDbhImpl;
import com.bolyartech.forge.server.module.user.data.UserLoginType;
import com.bolyartech.forge.server.module.user.data.screen_name.ScreenName;
import com.bolyartech.forge.server.module.user.data.screen_name.ScreenNameDbh;
import com.bolyartech.forge.server.module.user.data.screen_name.ScreenNameDbhImpl;
import com.bolyartech.forge.server.module.user.data.user.User;
import com.bolyartech.forge.server.module.user.data.user.UserDbh;
import com.bolyartech.forge.server.module.user.data.user.UserDbhImpl;
import com.bolyartech.forge.server.module.user_scram.data.scram.Scram;
import com.bolyartech.forge.server.module.user_scram.data.scram.ScramDbh;
import com.bolyartech.forge.server.module.user_scram.data.scram.ScramDbhImpl;
import com.bolyartech.scram_sasl.common.ScramUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertTrue;


public class UserExportedViewDbhImplTest {
    private DbPool mDbPool;


    @Before
    public void setup() throws ForgeConfigurationException, SQLException {
        if (mDbPool == null) {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("db.conf").getFile());
            DbConfigurationLoader loader = new FileDbConfigurationLoader(file.getParent());

            DbConfiguration dbConf = loader.load();

            mDbPool = DbUtils.createC3P0DbPool(dbConf);
        }

        Connection dbc = mDbPool.getConnection();

        DbTools.deleteAllScrams(dbc);
        DbTools.deleteAllScreenNames(dbc);
        DbTools.deleteAllUsers(dbc);

        dbc.close();
    }


    @Test
    public void testList() throws SQLException {
        ScreenNameDbh dbh = new ScreenNameDbhImpl();
        UserDbh userDbh = new UserDbhImpl();
        ScramDbh scramDbh = new ScramDbhImpl();

        ScramUtils.NewPasswordStringData data = new ScramUtils.NewPasswordStringData("salted", "salt", "clientKey",
                "server_key", "stored_key", 11);

        Connection dbc = mDbPool.getConnection();
        User userNew1 = userDbh.createNew(dbc, true, UserLoginType.GOOGLE);
        ScreenName sn1 = dbh.createNew(dbc, userNew1.getId(), "some Screenname");
        Scram scrNew1 = scramDbh.createNew(dbc, userNew1.getId(), "username", data);

        UserExportedViewDbhImpl impl = new UserExportedViewDbhImpl();
        List<UserExportedView> list = impl.list(dbc, 0, 20);
        assertTrue("Unexpected size", list.size() == 1);


        User userNew2 = userDbh.createNew(dbc, true, UserLoginType.GOOGLE);
        ScreenName sn2 = dbh.createNew(dbc, userNew2.getId(), "some Screenname2");
        Scram scrNew2 = scramDbh.createNew(dbc, userNew2.getId(), "username2", data);
        list = impl.list(dbc, 0, 20);
        assertTrue("Unexpected size", list.size() == 2);

        // test limit working
        list = impl.list(dbc, 0, 1);
        assertTrue("Unexpected size", list.size() == 1);

        // test idGreaterThan working
        list = impl.list(dbc, userNew1.getId(), 10);
        assertTrue("Unexpected size", list.size() == 1);
    }
}