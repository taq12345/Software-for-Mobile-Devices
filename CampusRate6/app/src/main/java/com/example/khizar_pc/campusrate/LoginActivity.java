package com.example.khizar_pc.campusrate;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    Button login;

    private String fbId;
    private String name;
    FirebaseDatabase database;
    private CallbackManager cbm;
    DatabaseReference myRef;
    TextView isOn;
    Bitmap dp;
    String profilePicUrl;
    LocationListener locationListener;
    LocationManager locationManager;
    TextView url;
    TextView dist;
    ImageView img;
    private float distance = 1000;
    private StorageReference mStorageRef;
    private Button fbLogin;
    final long ONE_MEGABYTE = 1024 * 1024;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null && !accessToken.isExpired()) {
            fbId = accessToken.getUserId();
            SharedPreferences prefs = getSharedPreferences("Users", MODE_PRIVATE);
            name = prefs.getString(fbId, null);
            //download DP or fetch from shared prefs

            mStorageRef = FirebaseStorage.getInstance().getReference().child("images/"+fbId+".jpg");
            mStorageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    //Toast.makeText(getApplicationContext(), "IN DOWNLOAD", Toast.LENGTH_SHORT).show();
                    dp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    manageAccount();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
        else {
            setContentView(R.layout.activity_login);
            fbId = "1234";
            name = "Bo Bim";
            img = findViewById(R.id.dp);
            fbLogin = findViewById(R.id.fbLogin);
            login = findViewById(R.id.button2);
            dist = findViewById(R.id.distance);
            isOn = findViewById(R.id.isOn);
            isOn.setText("");
            dist.setText("");
            getLocation();

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(distance < 10000) {
                        manageAccount();
                    }
                }
            });

            cbm = CallbackManager.Factory.create();
            checkLogin();
            fbLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
                }
            });
        }
    }

    public void manageAccount()
    {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String check;

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    check = child.getKey();
                    if (check.equals(fbId)) {
                        openMain();
                        return;
                    }
                }
                //Do other stuff
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
                uploadDp();
                openMain();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "UNABLE TO LOGIN", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        cbm.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void uploadDp()
    {
        mStorageRef = FirebaseStorage.getInstance().getReference().child("images/" + fbId + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        dp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mStorageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
    }
    public void getLocation()
    {
        // GET LOCATION
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false)
        {
            isOn.setText("Please turn your GPS on");
        }
        else
        {
            isOn.setText("");
        }
        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                Location myLocation = new Location("whatevs");
                myLocation.setLatitude(31.481139);
                myLocation.setLongitude(74.303428);
                distance = location.distanceTo(myLocation);
                //my park 31.538098, 74.376413
                //my home 31.537164, 74.375087
                //fast 31.481139, 74.303428
                dist.setText("You are " + distance + " METERS away from the center of campus");
                //emailId.setText("cganed");
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    public void checkLogin() {

        LoginManager.getInstance().registerCallback(cbm, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                try {
                                    Toast.makeText(getApplicationContext(), "LOGGED IN",Toast.LENGTH_LONG).show();
                                    fbId = object.getString("id");
                                    name = object.getString("name");
                                    profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                    dp = BitmapFactory.decodeStream(new URL(profilePicUrl).openConnection().getInputStream());

                                    //save dp and name function
                                    SharedPreferences.Editor editor = getSharedPreferences("Users", MODE_PRIVATE).edit();
                                    editor.putString(fbId, name);
                                    editor.apply();
                                    manageAccount();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "CANCELLED",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(), "ERROR",Toast.LENGTH_LONG).show();
                exception.printStackTrace();
            }
        });
    }

    public void openMain()
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("FB_ID", fbId);
        intent.putExtra("NAME", name);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
