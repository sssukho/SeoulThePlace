package com.ensharp.seoul.seoultheplace.UIElement;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.Fragments.PlaceFragment;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.PicassoImage;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;

public class CourseHolder extends RecyclerView.ViewHolder {
    private DAO dao = new DAO();
    private MainActivity activity;
    private Context context;
    private CourseVO course;
    private String userID;

    private CardView container;
    private CardView textBox;
    private ImageView image;
    private TextView name;
    private TextView location;
    private ImageView like;
    private boolean isLiked = false;

    public CourseHolder(View itemView, MainActivity activity) {
        super(itemView);

        this.activity = activity;
        container = (CardView) itemView.findViewById(R.id.courseView);
        container.setElevation(0);
        container.setCardElevation(0);
        container.setOnClickListener(onContainerClickListener);
        textBox = (CardView) itemView.findViewById(R.id.container);
        textBox.setElevation(0);
        textBox.setCardElevation(0);
        image = (ImageView) itemView.findViewById(R.id.course_image);
        name = (TextView)itemView.findViewById(R.id.course_name);
        location = (TextView)itemView.findViewById(R.id.course_location);
        like = (ImageView) itemView.findViewById(R.id.like_button);
        like.setOnClickListener(onLikeClickListener);
    }

    public void setData(CourseVO course, Context context,  String userID) {
        this.course = course;
        this.context = context;
        this.userID = userID;

        name.setText(course.getName());
        PicassoImage.DownLoadImage(course.getImage(), image);
        location.setText(course.getLocation());
        if (dao.checkLikedCourse(course.getCode(), userID).equals("true")) {
            like.setImageDrawable(context.getDrawable(R.drawable.choiced_heart));
            isLiked = true;
        } else {
            like.setImageDrawable(context.getDrawable(R.drawable.unchoiced_heart));
            isLiked = false;
        }
    }

    private View.OnClickListener onContainerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            activity.changeToPlaceFragment(place.getCode(), PlaceFragment.VIA_SEARCH);
        }
    };

    private View.OnClickListener onLikeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isLiked) {
                like.setImageDrawable(context.getDrawable(R.drawable.unchoiced_heart));
                isLiked = false;
            } else {
                like.setImageDrawable(context.getDrawable(R.drawable.choiced_heart));
                isLiked = true;
            }
            dao.likeCourse(course.getCode(), userID);
        }
    };
}
