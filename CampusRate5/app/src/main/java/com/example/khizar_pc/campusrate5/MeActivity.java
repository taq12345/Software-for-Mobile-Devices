package com.example.khizar_pc.campusrate5;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MeActivity extends AppCompatActivity {

    TabLayout toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        toolbar = findViewById(R.id.tabLayout3);
        toolbar.getTabAt(2).select();
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
