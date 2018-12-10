package com.example.khizar_pc.campusrate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    String fbId;
    DatabaseReference myRef;
    FirebaseDatabase database;
    private HashMap<String, String> mNames;
    private ArrayList<String> mIds;
    private HashMap<String,Bitmap> images;
    private ArrayList<HashMap<String, Float>> mRatings;
    private ArrayList<String> comments;
    HistoryRecyclerAdapter adapter;

    public HistoryFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        fbId = getActivity().getIntent().getStringExtra("FB_ID");
        //ADD REFRESH
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initArrays();
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void initArrays()
    {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Users").child(fbId).child("RatingsReceived");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String id = null;
               // mNames = new ArrayList<>();
                mIds = new ArrayList<>();
                mRatings = new ArrayList<>();
                comments = new ArrayList<>();
                images = new HashMap<>();

                for (DataSnapshot childs : dataSnapshot.getChildren()) {

                    HashMap<String, Float> hashMap = new HashMap<>();
                    mIds.add(childs.getKey());
                    hashMap.put("Attractiveness", childs.child("Attractiveness").getValue(Float.class));
                    hashMap.put("Confidence", childs.child("Confidence").getValue(Float.class));
                    hashMap.put("Honesty", childs.child("Honesty").getValue(Float.class));
                    hashMap.put("Humor", childs.child("Humor").getValue(Float.class));
                    hashMap.put("Intelligence", childs.child("Intelligence").getValue(Float.class));
                    comments.add(childs.child("Comment").getValue(String.class));
                    mRatings.add(hashMap);
                }
                database = FirebaseDatabase.getInstance();
                myRef = database.getReference().child("Users");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mNames = new HashMap<>();
                        for (DataSnapshot childs : dataSnapshot.getChildren()) {
                            for(int i = 0; i < mIds.size();i++)
                            {
                                if( childs.getKey().equals(mIds.get(i)))
                                {
                                    mNames.put(mIds.get(i),childs.child("Name").getValue(String.class));
                                }
                            }
                        }
                        for(int i = 0; i < mIds.size(); i++)
                        {
                            if(mIds.get(i).contains("Anonymous"))
                            {
                                mNames.put(mIds.get(i),"Anonymous");
                            }
                        }
                        initImages();
                        initRecycler();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void initImages()
    {
        images = new HashMap<>();
        for(int i = 0; i < mIds.size(); i++) {
            final StorageReference mStorageRef;
            if(mIds.get(i).contains("Anonymous"))
            {
                Log.d("anon", "anon seen");
                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.anon);
                images.put(mIds.get(i), bm);
                //mStorageRef = FirebaseStorage.getInstance().getReference().child("images/" + "Anonymous" + ".jpg");
            }
            else {
                mStorageRef = FirebaseStorage.getInstance().getReference().child("images/" + mIds.get(i) + ".jpg");
                mStorageRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        String id = mStorageRef.getName().substring( 0, mStorageRef.getName().indexOf("."));
                        images.put(id, bm);
                        adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }

        }

    }

    private void initRecycler()
    {
        RecyclerView recycler = getView().findViewById(R.id.recycler_history);
        //RelativeLayout rl = findViewById(R.id.list_relative);
        adapter = new HistoryRecyclerAdapter(mIds, images,mNames,mRatings,comments,getActivity());
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();

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
}
