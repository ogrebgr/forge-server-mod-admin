package com.bolyartech.forge.server.modules.admin.data;

import com.bolyartech.forge.server.config.ForgeConfigurationException;
import com.bolyartech.forge.server.db.*;
import com.bolyartech.forge.server.module.admin.data.AdminUser;
import com.bolyartech.forge.server.module.admin.data.AdminUserDbhImpl;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;


public class AdminUserDbhImplTest {
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
        DbTools.deleteAllAdminScrams(dbc);
        DbTools.deleteAllAdminUsers(dbc);

        dbc.close();
    }


    @Test
    public void testCreateNew() throws SQLException {
        Connection dbc = mDbPool.getConnection();

        AdminUserDbhImpl impl = new AdminUserDbhImpl();
        AdminUser newUser = impl.createNew(dbc, false, "gele");

        AdminUser loadedUser = impl.loadById(dbc, newUser.getId());

        assertTrue("Not same user", loadedUser.equals(newUser));
    }


    @Test
    public void testChangeNew() throws SQLException {
        Connection dbc = mDbPool.getConnection();

        AdminUserDbhImpl impl = new AdminUserDbhImpl();
        AdminUser newUser = impl.createNew(dbc, false, "gele");

        boolean changed = impl.changeName(dbc, newUser, "trytkata");

        AdminUser loadedUser = impl.loadById(dbc, newUser.getId());

        assertTrue("Not same user", loadedUser.getName().equals("trytkata"));
    }


    @Test
    public void testChangeDisabled() throws SQLException {
        Connection dbc = mDbPool.getConnection();

        AdminUserDbhImpl impl = new AdminUserDbhImpl();
        AdminUser newUser = impl.createNew(dbc, false, "gele");

        boolean changed = impl.changeDisabled(dbc, newUser.getId(), true);

        AdminUser loadedUser = impl.loadById(dbc, newUser.getId());

        assertTrue("Not same user", loadedUser.isDisabled());
    }


    @Test
    public void testChangeSuperAdmin() throws SQLException {
        Connection dbc = mDbPool.getConnection();

        AdminUserDbhImpl impl = new AdminUserDbhImpl();
        AdminUser newUser = impl.createNew(dbc, false, "gele");

        AdminUser changed = impl.changeSuperAdmin(dbc, newUser, true);

        AdminUser loadedUser = impl.loadById(dbc, newUser.getId());

        assertTrue("Not same user", loadedUser.equals(changed));
    }
}
