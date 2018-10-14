package com.example.khizar_pc.campusrate;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    private String id;

    private String name;

    User(String id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public String getId()
    {
        return id;
    }
    public String getName()
    {
        return name;
    }
}
