package com.hank.appbasicframework.db.dao;

import com.hank.appbasicframework.db.data.Person;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

public class PersonDao extends BaseDaoImpl<Person, String> {

    public PersonDao(DatabaseHelper helper) throws SQLException {
        super(helper.getConnectionSource(), Person.class);
    }

    public PersonDao(ConnectionSource connectionSource,
                     Class<Person> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public Person getPersonByNumber(String number) throws SQLException {
        QueryBuilder<Person, String> queryBuilder = queryBuilder();
        Where<Person, String> where = queryBuilder.where();
        where.eq("number", number);
        List<Person> list = query(queryBuilder.prepare());
        if (!list.isEmpty())
            return list.get(0);
        return null;
    }

    public List<Person> getPersonList(int gender) throws SQLException {
        QueryBuilder<Person, String> queryBuilder = queryBuilder();
        Where<Person, String> where = queryBuilder.where();
        where.eq("gender", gender);
        return query(queryBuilder.prepare());
    }

    public void clearPersonTable() throws SQLException {
        TransactionManager.callInTransaction(connectionSource,
                new Callable<Void>() {

                    @Override
                    public Void call() throws Exception {
                        for (Person person : queryForAll()) {
                            delete(person);
                        }
                        return null;
                    }
                });
    }
}
