package com.ensharp.seoul.seoultheplace.Course;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ensharp.seoul.seoultheplace.DownloadImageTask;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    Context context;
    List<PlaceVO> items;
    int item_layout;
    CourseModifyFragment fragment;
    public int choosedMember = 0;


    public RecyclerAdapter(Context context, List<PlaceVO> items, int item_layout,CourseModifyFragment fragment) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
        this.fragment = fragment;
    }

    public int GetChoosedMemeber(){
        return choosedMember;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final PlaceVO item = items.get(position);
        SetImageBox(holder,position);
        Picasso.get().load(item.getImageURL()[0]).into(holder.image);
        holder.title.setText(item.getName());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosedMember = position;
                NotifyDataSetChanged(position);
                if(item.getName().equals("+")) {
                    if(choosedMember>0)
                        fragment.setItemData(items.get(position-1));
                    else
                        fragment.setItemData(null);
                    fragment.ChangeItemData();
                }
                else{
                    fragment.setItemData(item);
                }
            }
        });
    }

    public void NotifyDataSetChanged(int position){
        choosedMember = position;
        notifyDataSetChanged();
    }
    public void SetImageBox(ViewHolder holder,final int position){
        if(choosedMember == position){
            holder.cardview.setLayoutParams(holder.params1);
        }
        else{
            holder.cardview.setLayoutParams(holder.params2);
        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        CardView cardview;
        RelativeLayout.LayoutParams params1;
        RelativeLayout.LayoutParams params2;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.OriginCardImage);
            title = (TextView) itemView.findViewById(R.id.OriginCardName);
            cardview = (CardView) itemView.findViewById(R.id.OriginCardView);
            params1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ChangeDp(200));
            params1.setMargins(ChangeDp(10),ChangeDp(10),ChangeDp(10),ChangeDp(10));
            params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ChangeDp(100));
            params2.setMargins(ChangeDp(10),ChangeDp(10),ChangeDp(10),ChangeDp(10));
        }
    }
    public int ChangeDp(int data){
        int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, data, context.getResources().getDisplayMetrics());
        return dp;
    }
}