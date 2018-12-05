package com.example.khizar_pc.campusrate5;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NearbyActivity extends AppCompatActivity {

    DatabaseReference myRef;
    FirebaseDatabase database;
    private ArrayList<String> mNames;
    private ArrayList<Float> mRatings;
    private ArrayList<String> mIds;
    private TabItem tabItem;
    //private TextView test;
    TabLayout toolbar;
    private SearchView search;
    public String fbId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        search = findViewById(R.id.searchView);
        fbId = getIntent().getStringExtra("EXTRA_SESSION_ID"); //MY FB ID
       // test = findViewById(R.id.testN);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                initArrays(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                initArrays(newText);
                return false;
            }
        });
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
                    openMe("10217120210298546");
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
                    openMe("10217120210298546");
                }
            }
        });
        initArrays("");



    }

    private void initArrays(final String query)
    {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String id = null;
                mNames = new ArrayList<>();
                mRatings = new ArrayList<>();
                mIds = new ArrayList<>();
                float overall;
                for (DataSnapshot childs : dataSnapshot.getChildren()) {
                    id = childs.getKey();

                    if(!id.equals(fbId)) {
                        //if name matches search query
                        if(childs.child("Name").getValue(String.class).contains(query)) {
                            mIds.add(id);
                            mNames.add(childs.child("Name").getValue(String.class));
                            overall = childs.child("Averages").child("OverallAvg").getValue(Float.class);
                            mRatings.add(overall);
                        }
                    }
                }
                initRecycler();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initRecycler()
    {
        RecyclerView recycler = findViewById(R.id.recycler_view);
        //RelativeLayout rl = findViewById(R.id.list_relative);

        NearbyRecyclerAdapter adapter = new NearbyRecyclerAdapter(mIds, mNames, mRatings, this, fbId);
        recycler.setLayoutManager(new GridLayoutManager(this,3));
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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

    public void openMe(String id)
    {
        Intent intent = new Intent(this, MeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXTRA_SESSION_ID", id);
        startActivity(intent);
    }


}
