package com.hank.appbasicframework.db.data;

import com.hank.appbasicframework.db.dao.PersonDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "PersonTable", daoClass = PersonDao.class)
public class Person implements Serializable {

    private static final long serialVersionUID = 197726875530544690L;
    @DatabaseField
    private int number;
    @DatabaseField
    private int name;
    /**
     * Gender female:0  male:1
     */
    @DatabaseField
    private int gender;

    public int getNumber() {
        return number;
    }

    public int getName() {
        return name;
    }

    public int getGender() {
        return gender;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setName(int name) {
        this.name = name;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
