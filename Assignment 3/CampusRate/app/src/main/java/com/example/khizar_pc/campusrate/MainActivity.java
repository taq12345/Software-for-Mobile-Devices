package com.example.khizar_pc.campusrate;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    //DECLARING DATABASE
    private static final String DATABASE_NAME = "user_db";
    private UserRoomDatabase userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //INITIALISING DATABASE
        userDatabase = Room.databaseBuilder(getApplicationContext(),
                UserRoomDatabase.class, DATABASE_NAME).build();

        // USING OPERATION IN ACTIVITY TO WRITE TO DATABASE
       new Thread(new Runnable() {
            @Override
            public void run() {
                User user =new User(1,"talha");
                userDatabase.userDao().insert(user);
            }
        }).start();
    }
}
