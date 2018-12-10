package com.example.khizar_pc.campusrate;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class RateActivity extends AppCompatActivity {

    Button rate;
    String myFbId;
    String hisFbId;
    CircleImageView img;
    Bitmap dp;
    RatingBar humor;
    Switch anon;
    RatingBar attr;
    RatingBar conf;
    RatingBar hon;
    RatingBar intel;
    TextView rateCount;
    TextView overall;
    TextView name;
    EditText comment;
    DatabaseReference myRef;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        rate = findViewById(R.id.rate);
        myFbId = getIntent().getStringExtra("MY_ID");
        hisFbId = getIntent().getStringExtra("HIS_ID");
       // dp = getIntent().getParcelableExtra("DP");

        database = FirebaseDatabase.getInstance();

        img = findViewById(R.id.user_pic);
        anon = findViewById(R.id.anon);
        overall = findViewById(R.id.average);
        rateCount = findViewById(R.id.rateCount);
        humor = findViewById(R.id.humRate);
        attr = findViewById(R.id.attrRate);
        conf = findViewById(R.id.confRate);
        comment = findViewById(R.id.comment);
        hon = findViewById(R.id.honRate);
        intel = findViewById(R.id.intRate);
        name = findViewById(R.id.user_name);
        showRatings();
        setImage();
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Successfully Submitted", Toast.LENGTH_SHORT).show();
                //write ratings. make a received for person rated and given for person rating
                float hum = humor.getRating();
                float att = attr.getRating();
                float con = conf.getRating();
                float honesty = hon.getRating();
                float intelli = intel.getRating();
                myRef = database.getReference().child("Users").child(myFbId).child("RatingsGiven").child(hisFbId);
                myRef.child("Confidence").setValue(con);
                myRef.child("Humor").setValue(hum);
                myRef.child("Honesty").setValue(honesty);
                myRef.child("Intelligence").setValue(intelli);
                myRef.child("Attractiveness").setValue(att);

                myRef = database.getReference().child("Users").child(hisFbId);
                //if anonymous option on then instead of myFbId, send "Anonymous"
                if(anon.isChecked())
                {
                    myRef = myRef.child("RatingsReceived").child("Anonymous" + myFbId );
                }
                else {
                    myRef = myRef.child("RatingsReceived").child(myFbId);
                }
                myRef.child("Confidence").setValue(con);
                myRef.child("Humor").setValue(hum);
                myRef.child("Honesty").setValue(honesty);
                myRef.child("Intelligence").setValue(intelli);
                myRef.child("Attractiveness").setValue(att);
                myRef.child("Comment").setValue(comment.getText().toString());
                calcAverages();
                //openNearby(myFbId);
            }
        });
    }
    public void setImage()
    {
        final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("images/" + hisFbId + ".jpg");
        mStorageRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                dp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                img.setImageBitmap(dp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
    public void showRatings()
    {
        myRef = database.getReference().child("Users").child(hisFbId);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                humor.setRating(dataSnapshot.child("Averages").child("HumAvg").getValue(Float.class));
                attr.setRating(dataSnapshot.child("Averages").child("AttrAvg").getValue(Float.class));
                conf.setRating(dataSnapshot.child("Averages").child("ConfAvg").getValue(Float.class));
                hon.setRating(dataSnapshot.child("Averages").child("HonAvg").getValue(Float.class));
                intel.setRating(dataSnapshot.child("Averages").child("IntAvg").getValue(Float.class));
                float overallRating = dataSnapshot.child("Averages").child("OverallAvg").getValue(Float.class);
                String floatToString = String.format("%.2f",overallRating);
                overall.setText(floatToString);
                myRef = database.getReference().child("Users").child(hisFbId);
                name.setText(dataSnapshot.child("Name").getValue(String.class));
                rateCount.setText(Long.toString(dataSnapshot.child("RatingsReceived").getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void calcAverages()
    {
        myRef = database.getReference().child("Users").child(hisFbId).child("RatingsReceived");
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

                    myRef = database.getReference().child("Users").child(hisFbId).child("Averages");
                    myRef.child("AttrAvg").setValue(attra);
                    myRef.child("ConfAvg").setValue(confi);
                    myRef.child("HumAvg").setValue(hum);
                    myRef.child("IntAvg").setValue(inteli);
                    myRef.child("HonAvg").setValue(hone);
                    float total = (attra + confi + hone + hum + inteli) / 5;
                    myRef.child("OverallAvg").setValue(total);
                }
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
