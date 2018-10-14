package com.example.khizar_pc.campusrate;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

//CREATING ENTITY
@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    User(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }
    public String getName()
    {
        return name;
    }
}
