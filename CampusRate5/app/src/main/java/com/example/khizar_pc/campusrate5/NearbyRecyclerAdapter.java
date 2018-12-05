package com.example.khizar_pc.campusrate5;

import android.content.Context;
import android.content.Intent;
import android.media.AudioTrack;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NearbyRecyclerAdapter extends RecyclerView.Adapter<NearbyRecyclerAdapter.ViewHolder> {

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mIds = new ArrayList<>();
    private ArrayList<Float> mRatings = new ArrayList<>();
    private String myFbId;
    private Context mContext;

    public NearbyRecyclerAdapter(ArrayList<String> mIds, ArrayList<String> mNames, ArrayList<Float> mRatings, Context mContext, String fbId) {
        this.mNames = mNames;
        this.mIds = mIds;
        this.mRatings = mRatings;
        this.mContext = mContext;
        this.myFbId = fbId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nearby_recyleritem, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.txt1.setText(mNames.get(i));
        viewHolder.image.setImageResource(R.mipmap.ic_launcher);

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRate(myFbId, mIds.get(i));
            }
        });
    }

    public void openRate(String myId, String hisId)
    {
        Intent intent = new Intent(mContext, RateActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("MY_ID", myId);
        intent.putExtra("HIS_ID", hisId);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView txt1;
        CardView parentLayout;

        public CardView getParentLayout() {
            return parentLayout;
        }

        public ViewHolder(View itemView)
        {
            super(itemView);
            image = itemView.findViewById(R.id.dp);
            txt1 = itemView.findViewById(R.id.name);
            parentLayout = itemView.findViewById(R.id.list_const);

        }
    }


}
