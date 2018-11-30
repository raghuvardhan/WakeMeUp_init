package com.raghu.android.wakemeup.Database.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Category implements Serializable{

    @PrimaryKey(autoGenerate = true)
    int categoryId;

    String name;

    public Category(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    @Ignore
    public Category(String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
