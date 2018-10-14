package com.example.khizar_pc.campusrate;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

//CREATING DATA ACCESS OBJECT
@Dao
public interface UserDAO {

    //WRITE OPERATION
    @Insert
    void insert(User user);

    //UPDATE OPERATION
    @Query("DELETE FROM User")
    void deleteAll();

    //READ OPERATION
    @Query("SELECT * from User ORDER BY name ASC")
    LiveData<List<User>> getAllWords();
}
