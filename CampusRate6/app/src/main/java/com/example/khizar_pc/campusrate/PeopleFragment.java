package com.example.khizar_pc.campusrate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PeopleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PeopleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PeopleFragment extends Fragment{

    DatabaseReference myRef;
    FirebaseDatabase database;
    String fbId;
    private SearchView search;
    private OnFragmentInteractionListener mListener;
    private ArrayList<String> mNames;
    private HashMap<String,Bitmap> images;
    private ArrayList<Float> mRatings;
    private ArrayList<String> mIds;
    private SwipeRefreshLayout refresh;
    //private StorageReference mStorageRef;
    PeopleRecyclerAdapter adapter;
    Bitmap bm;
    final long ONE_MEGABYTE = 1024 * 1024;

    public PeopleFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static PeopleFragment newInstance() {
        PeopleFragment fragment = new PeopleFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_people, container, false);
        fbId = getActivity().getIntent().getStringExtra("FB_ID");
        search = rootView.findViewById(R.id.search);
        //search.setMaxWidth(Integer.MAX_VALUE);
        refresh = rootView.findViewById(R.id.swipe);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initArrays();
                refresh.setRefreshing(false);
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });
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
    private void search(String query)
    {
        ArrayList<String> tempNames = new ArrayList<>();
        HashMap<String,Bitmap> tempImages = new HashMap<>();
        ArrayList<Float> tempRatings = new ArrayList<>();
        ArrayList<String> tempIds = new ArrayList<>();
        query = query.toLowerCase();
        //upper case check
        for (int i = 0; i < mIds.size(); i++)
        {
            if (mNames.get(i).toLowerCase().contains(query))
            {
                tempNames.add(mNames.get(i));
                tempImages.put(mIds.get(i), images.get(mIds.get(i)));
                tempRatings.add(mRatings.get(i));
                tempIds.add(mIds.get(i));

            }
        }

        initRecycler(tempIds, tempNames,tempImages, tempRatings);

    }
    public void initArrays()
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
                       // if(childs.child("Name").getValue(String.class).contains(query)) {
                            mIds.add(id);
                            mNames.add(childs.child("Name").getValue(String.class));
                            overall = childs.child("Averages").child("OverallAvg").getValue(Float.class);
                            mRatings.add(overall);
                       // }
                    }
                }
                initImages();
                initRecycler(mIds, mNames,images, mRatings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void initImages()
    {
        //CHECK IF ALREADY IN PHONE...also read temp file slides
        /*images = new HashMap<>();
        for(int i = 0; i < mIds.size(); i++) {
            final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("images/" + mIds.get(i) + ".jpg");
            localFile = null;
            try {
                localFile = File.createTempFile(mIds.get(i), "jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }

            mStorageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getPath());
                    String id = taskSnapshot.getStorage().getName().substring( 0, taskSnapshot.getStorage().getName().indexOf("."));
                    images.put(id,bitmap);
                    Log.d("whatevs", mStorageRef.getName());
                    adapter.notifyDataSetChanged();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors

                }
            });
        }*/
        images = new HashMap<>();
        for(int i = 0; i < mIds.size(); i++) {
            final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("images/" + mIds.get(i) + ".jpg");
            mStorageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    String id = mStorageRef.getName().substring( 0, mStorageRef.getName().indexOf("."));
                    images.put(id, bm);
                    adapter.notifyDataSetChanged();
                    Log.d("whatevs", Integer.toString(bm.getByteCount()));

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }

    }

    private void initRecycler(ArrayList<String> ids, ArrayList<String> names, HashMap<String,Bitmap> imgs, ArrayList<Float> ratings)
    {
        RecyclerView recycler = getView().findViewById(R.id.recycler_people);
        //RelativeLayout rl = findViewById(R.id.list_relative);

        adapter = new PeopleRecyclerAdapter(ids, names,imgs,ratings, getActivity(), fbId);
        recycler.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();

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
