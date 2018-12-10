package com.example.khizar_pc.campusrate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    String fbId;
    String name;
    Bitmap display;
    TextView avg;
    TextView rateCount;
    RatingBar humor;
    RatingBar attr;
    RatingBar conf;
    RatingBar hon;
    RatingBar intel;
    Button logout;
    CircleImageView dp;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private TextView userName;
    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
       // Bundle args = new Bundle();
       // fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_user, container, false);
        fbId = getActivity().getIntent().getStringExtra("FB_ID");
        name = getActivity().getIntent().getStringExtra("NAME");
        downloadDp();
        humor = rootView.findViewById(R.id.humRate);
        attr = rootView.findViewById(R.id.attrRate);
        conf = rootView.findViewById(R.id.confRate);
        hon = rootView.findViewById(R.id.honRate);
        intel = rootView.findViewById(R.id.intRate);
        userName = rootView.findViewById(R.id.user_name);
        avg = rootView.findViewById(R.id.average);
        rateCount = rootView.findViewById(R.id.rateCount);
        logout = rootView.findViewById(R.id.logout);
        dp = rootView.findViewById(R.id.dp);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                openLogin();
            }
        });
        setViews();
        //userName.setText(name);
        return rootView;
    }

    public void downloadDp()
    {
        final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("images/" + fbId + ".jpg");
        mStorageRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                display = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                dp.setImageBitmap(display);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
    public void setViews()
    {
        database = FirebaseDatabase.getInstance();
        userName.setText(name);
        myRef = database.getReference().child("Users").child(fbId).child("Averages");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                humor.setRating(dataSnapshot.child("HumAvg").getValue(Float.class));
                intel.setRating(dataSnapshot.child("IntAvg").getValue(Float.class));
                conf.setRating(dataSnapshot.child("ConfAvg").getValue(Float.class));
                attr.setRating(dataSnapshot.child("AttrAvg").getValue(Float.class));
                hon.setRating(dataSnapshot.child("HonAvg").getValue(Float.class));
                float overallRating = dataSnapshot.child("OverallAvg").getValue(Float.class);
                String floatToString = String.format("%.2f",overallRating);
                avg.setText(floatToString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRef = database.getReference().child("Users").child(fbId).child("RatingsReceived");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rateCount.setText(Long.toString(dataSnapshot.getChildrenCount()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void openLogin()
    {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
