package com.example.khizar_pc.campusrate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class PeopleRecyclerAdapter extends RecyclerView.Adapter<PeopleRecyclerAdapter.ViewHolder> {

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mIds = new ArrayList<>();
    private ArrayList<Float> mRatings = new ArrayList<>();
    private HashMap<String,Bitmap> images = new HashMap<>();
    private String myFbId;
    private Context mContext;

    public PeopleRecyclerAdapter(ArrayList<String> mIds, ArrayList<String> mNames, HashMap<String,Bitmap> images, ArrayList<Float> mRatings, Context mContext, String fbId) {
        this.mNames = mNames;
        this.mIds = mIds;
        this.mRatings = mRatings;
        this.mContext = mContext;
        this.images = images;
        this.myFbId = fbId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.people_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.txt1.setText(mNames.get(i) + " ★");
        viewHolder.image.setImageBitmap(images.get(mIds.get(i)));

       /* if(images.size() == mIds.size()) {
            viewHolder.image.setImageBitmap(images.get(i));
        }
        else {
            viewHolder.image.setImageResource(R.mipmap.ic_launcher);
        }*/

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openRate(myFbId, mIds.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        //return mNames.size();
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView txt1;
        FrameLayout parentLayout;

        public FrameLayout getParentLayout() {
            return parentLayout;
        }

        public ViewHolder(View itemView)
        {
            super(itemView);
            image = itemView.findViewById(R.id.peopleImg);
            txt1 = itemView.findViewById(R.id.peopleName);
            parentLayout = itemView.findViewById(R.id.frameLayout);

        }
    }

    public void openRate(String myId, String hisId)
    {
        Intent intent = new Intent(mContext, RateActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("MY_ID", myId);
        intent.putExtra("HIS_ID", hisId);
        mContext.startActivity(intent);
    }
}
