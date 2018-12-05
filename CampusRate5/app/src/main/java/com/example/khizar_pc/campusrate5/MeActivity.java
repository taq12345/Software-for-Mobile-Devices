package com.example.khizar_pc.campusrate5;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MeActivity extends AppCompatActivity {

    TabLayout toolbar;
    String fbId;
    String name;
    private ProfilePictureView dp;
    DatabaseReference myRef;
    FirebaseDatabase database;
    RatingBar humor;
    RatingBar attr;
    RatingBar conf;
    RatingBar hon;
    RatingBar intel;
    Button logout;
    private TextView test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        humor = findViewById(R.id.humor);
        attr = findViewById(R.id.attractiveness);
        conf = findViewById(R.id.confidence);
        hon = findViewById(R.id.honesty);
        intel = findViewById(R.id.intelligence);
        test = findViewById(R.id.textView10);
        toolbar = findViewById(R.id.tabLayout3);
        dp = findViewById(R.id.imageView2);
        fbId = getIntent().getStringExtra("EXTRA_SESSION_ID");
        name = getIntent().getStringExtra("NAME");
        dp.setProfileId(fbId);

        logout = findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                openLogin();
            }
        });

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
                    openNearby(fbId);
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
                    openNearby(fbId);
                }
                if(tab.getPosition() == 2)
                {
                    openMe();
                }
            }
        });

        fillMe();
    }

    public void fillMe()
    {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Users");

        //make all stars 0 if overall avg -1
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String check;
                boolean exists = false;

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    check = child.getKey();
                    if(check.equals(fbId))
                    {
                        exists = true;
                        break;
                    }
                }

                if (exists == true){
                    setAverages();

                }
                else
                {
                    // maybe create new function for this for neatness
                    myRef.child(fbId).child("Name").setValue(name);
                    myRef = myRef.child(fbId).child("Averages");
                    myRef.child("AttrAvg").setValue(0);
                    myRef.child("ConfAvg").setValue(0);
                    myRef.child("HonAvg").setValue(0);
                    myRef.child("HumAvg").setValue(0);
                    myRef.child("IntAvg").setValue(0);
                    myRef.child("OverallAvg").setValue(0);
                    myRef.child("RatingsReceived");
                    myRef.child("RatingsGiven");
                    humor.setRating(0);
                    intel.setRating(0);
                    conf.setRating(0);
                    attr.setRating(0);
                    hon.setRating(0);
                }
                test.setText(name);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setAverages()
    {
        myRef = database.getReference().child("Users").child(fbId).child("RatingsReceived");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               float attra = 0;
               float confi = 0;
               float hone = 0;
               float hum = 0;
               float inteli = 0;
               int count = (int)dataSnapshot.getChildrenCount();

               if(count >0) {
                   for (DataSnapshot childs : dataSnapshot.getChildren()) {
                       attra = attra + childs.child("Attractiveness").getValue(Float.class);
                       confi = confi + childs.child("Confidence").getValue(Float.class);
                       hone = hone + childs.child("Honesty").getValue(Float.class);
                       hum = hum + childs.child("Humor").getValue(Float.class);
                       inteli = inteli + childs.child("Intelligence").getValue(Float.class);
                   }
                   attra = attra / count;
                   confi = confi / count;
                   hone = hone / count;
                   hum = hum / count;
                   inteli = inteli / count;

                   myRef = database.getReference().child("Users").child(fbId).child("Averages");
                   myRef.child("AttrAvg").setValue(attra);
                   myRef.child("ConfAvg").setValue(confi);
                   myRef.child("HumAvg").setValue(hum);
                   myRef.child("IntAvg").setValue(inteli);
                   myRef.child("HonAvg").setValue(hone);
                   float total = (attra + confi + hone + hum + inteli) / 5;
                   myRef.child("OverallAvg").setValue(total);

                   humor.setRating(hum);
                   intel.setRating(inteli);
                   conf.setRating(confi);
                   attr.setRating(attra);
                   hon.setRating(hone);
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void openSettings()
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }
    public void openLogin()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }

    public void openNearby(String id)
    {
        Intent intent = new Intent(this, NearbyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXTRA_SESSION_ID", id);
        startActivity(intent);
    }

    public void openMe()
    {
        Intent intent = new Intent(this, MeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }
}
