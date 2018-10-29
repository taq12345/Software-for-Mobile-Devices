package com.example.khizar_pc.campusrate5;

import android.content.Context;
import android.content.Intent;
import android.media.AudioTrack;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NearbyRecyclerAdapter extends RecyclerView.Adapter<NearbyRecyclerAdapter.ViewHolder> {

    private ArrayList<String> mNames = new ArrayList<>();
    //private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mRatings = new ArrayList<>();
    private Context mContext;

    public NearbyRecyclerAdapter(ArrayList<String> mNames, ArrayList<String> mRatings, Context mContext) {
        this.mNames = mNames;
        //this.mImages = mImages;
        this.mRatings = mRatings;
        this.mContext = mContext;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nearby_recyleritem, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.txt1.setText(mNames.get(i));
        viewHolder.txt2.setText(mRatings.get(i));
        viewHolder.image.setImageResource(R.mipmap.ic_launcher);

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRate();
            }
        });
    }

    public void openRate()
    {
        Intent intent = new Intent(mContext, RateActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView txt1;
        TextView txt2;
        RelativeLayout parentLayout;

        public RelativeLayout getParentLayout() {
            return parentLayout;
        }

        public ViewHolder(View itemView)
        {
            super(itemView);
            image = itemView.findViewById(R.id.dp);
            txt1 = itemView.findViewById(R.id.name);
            txt2 = itemView.findViewById(R.id.rating);
            parentLayout = itemView.findViewById(R.id.list_relative);

        }
    }


}
