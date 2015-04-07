package com.hank.appbasicframework.db.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hank.appbasicframework.common.Constants;
import com.hank.appbasicframework.db.data.Person;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Class to handle the database
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // The DAO object we use to access the person table
    private Dao<Person, String> mPersonDao = null;

    public DatabaseHelper(Context context) {
        super(context, Constants.DB_NAME, null, 1);
    }

    /**
     * Initial：Create the database table
     *
     * @see com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase,
     * com.j256.ormlite.support.ConnectionSource)
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, PersonDao.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        onCreate(db, connectionSource);
    }

    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It
     * will create it or just give the cached value.
     */
    public Dao<Person, String> getPersonDao() throws SQLException {
        if (mPersonDao == null) {
            mPersonDao = getDao(Person.class);
        }
        return mPersonDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        mPersonDao = null;

    }
}