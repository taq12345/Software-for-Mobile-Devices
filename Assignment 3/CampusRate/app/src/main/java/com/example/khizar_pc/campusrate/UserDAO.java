package com.example.khizar_pc.campusrate;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert
    void insert(User user);

    @Query("DELETE FROM User")
    void deleteAll();

    @Query("SELECT * from User ORDER BY name ASC")
    LiveData<List<User>> getAllWords();
}
