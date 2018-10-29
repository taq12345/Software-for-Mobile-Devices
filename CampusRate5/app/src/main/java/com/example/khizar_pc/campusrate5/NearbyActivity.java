package com.example.khizar_pc.campusrate5;

import android.content.Intent;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class NearbyActivity extends AppCompatActivity {

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mRatings = new ArrayList<>();
    private TabItem tabItem;
    TabLayout toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        toolbar = findViewById(R.id.tabLayout1);
        toolbar.getTabAt(1).select();
        toolbar.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getPosition() == 0)
                {
                    openSettings();
                }
                if(tab.getPosition() == 1)
                {
                    openNearby();
                }
                if(tab.getPosition() == 2)
                {
                    openMe();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0)
                {
                    openSettings();
                }
                if(tab.getPosition() == 1)
                {
                    openNearby();
                }
                if(tab.getPosition() == 2)
                {
                    openMe();
                }
            }
        });
        initArrays();
        initRecycler();



    }

    private void initArrays()
    {
        for (int i = 0; i < 10; i++)
        {
            mNames.add("User " + Integer.toString(i));
            mRatings.add("Overall Rating: " + Integer.toString(i));
        }
    }

    private void initRecycler()
    {
        RecyclerView recycler = findViewById(R.id.recycler_view);
        //RelativeLayout rl = findViewById(R.id.list_relative);

        NearbyRecyclerAdapter adapter = new NearbyRecyclerAdapter(mNames, mRatings, this);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));
    }

    public void openSettings()
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }

    public void openNearby()
    {
        Intent intent = new Intent(this, NearbyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }

    public void openMe()
    {
        Intent intent = new Intent(this, MeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }


}
