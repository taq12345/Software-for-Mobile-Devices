package com.example.khizar_pc.campusrate;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder> {

    //private ArrayList<String> mNames = new ArrayList<>();
    //private ArrayList<String> mIds = new ArrayList<>();

    private ArrayList<HashMap<String, Float>> mRatings = new ArrayList<>();
    private HashMap<String, String> mNames = new HashMap<>();
    private ArrayList<String> mIds = new ArrayList<>();
    private HashMap<String,Bitmap> images = new HashMap<>();
    private ArrayList<String> comments = new ArrayList<>();
    private Context mContext;

    HistoryRecyclerAdapter(ArrayList<String> mIds, HashMap<String,Bitmap> images, HashMap<String,String> mNames, ArrayList<HashMap<String, Float>> mRatings, ArrayList<String> comments, Context mContext)
    {
        this.mIds = mIds;
        this.mNames = mNames;
        this.mRatings = mRatings;
        this.mContext = mContext;
        this.images = images;
        this.comments = comments;
    }

    @NonNull
    @Override
    public HistoryRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_item, viewGroup, false);
        HistoryRecyclerAdapter.ViewHolder holder = new HistoryRecyclerAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryRecyclerAdapter.ViewHolder viewHolder, int i) {
        viewHolder.name.setText(mNames.get(mIds.get(i)));
        viewHolder.attrRate.setRating(mRatings.get(i).get("Attractiveness"));
        viewHolder.honRate.setRating(mRatings.get(i).get("Honesty"));
        viewHolder.humRate.setRating(mRatings.get(i).get("Humor"));
        viewHolder.intelRate.setRating(mRatings.get(i).get("Intelligence"));
        viewHolder.confRate.setRating(mRatings.get(i).get("Confidence"));
        viewHolder.image.setImageBitmap(images.get(mIds.get(i)));
        viewHolder.comment.setText(comments.get(i));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView name;
        TextView attr;
        TextView hon;
        TextView hum;
        TextView intel;
        TextView conf;
        TextView comment;
        RatingBar attrRate;
        RatingBar honRate;
        RatingBar humRate;
        RatingBar intelRate;
        RatingBar confRate;

        LinearLayout parentLayout;

        public LinearLayout getParentLayout() {
            return parentLayout;
        }

        public ViewHolder(View itemView)
        {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.userImg);
            attr = itemView.findViewById(R.id.attr);
            hon = itemView.findViewById(R.id.hon);
            hum = itemView.findViewById(R.id.hum);
            intel = itemView.findViewById(R.id.intel);
            conf = itemView.findViewById(R.id.conf);
            comment = itemView.findViewById(R.id.comment);
            attrRate = itemView.findViewById(R.id.attrRate);
            honRate =  itemView.findViewById(R.id.honRate);
            humRate = itemView.findViewById(R.id.humRate);
            intelRate = itemView.findViewById(R.id.intRate);
            confRate = itemView.findViewById(R.id.confRate);
            parentLayout = itemView.findViewById(R.id.frameLayout);

        }
    }
}
