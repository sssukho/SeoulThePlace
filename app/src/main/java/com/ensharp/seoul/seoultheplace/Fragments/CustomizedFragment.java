package com.ensharp.seoul.seoultheplace.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ensharp.seoul.seoultheplace.FavoriteVO;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.R;
import com.ensharp.seoul.seoultheplace.UIElement.CustomizedCourseAdapter;
import com.ensharp.seoul.seoultheplace.UIElement.FloatingButton.AddFloatingActionButton;
import com.ensharp.seoul.seoultheplace.UIElement.FloatingButton.FloatingActionButton;
import com.ensharp.seoul.seoultheplace.UIElement.FloatingButton.FloatingActionsMenu;
import com.ensharp.seoul.seoultheplace.UIElement.SwipeDismissListViewTouchListener;

import java.util.ArrayList;
import java.util.List;

public class CustomizedFragment extends Fragment {

    private View rootView;
    private ListView listView;
    private List<FavoriteVO> favorites = new ArrayList<FavoriteVO>();
    private CustomizedCourseAdapter adapter;
    private boolean isExpanded = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_customized, container, false);
        listView = (ListView) rootView.findViewById(R.id.customized_list);

        final MainActivity activity = (MainActivity) getActivity();

        initFavorites();
        adapter = new CustomizedCourseAdapter(getContext(), 0, favorites);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onItemClickListener);
        SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(listView, new SwipeDismissListViewTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(int position) {
                if (isExpanded) return false;

                return true;
            }

            @Override
            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                if (isExpanded) return;

                for (int position : reverseSortedPositions) {
                    favorites.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        listView.setOnTouchListener(touchListener);

        return rootView;
    }

    public void initFavorites() {
        favorites.add(new FavoriteVO("http://mblogthumb4.phinf.naver.net/20160517_63/rudekf007_1463467183216LiA4r_JPEG/KakaoTalk_20160517_100111254.jpg?type=w2", "정국", "서울특별시 강남구 도산대로16길 13-20"));
        favorites.add(new FavoriteVO("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3xSg6WMNXG19Fd1p_ivl4AR7dPnbthyzKCmykBEytSECSGMNN", "백현", "서울 강남구 선릉로190길 114"));
        favorites.add(new FavoriteVO("http://news.kbs.co.kr/data/news/2018/03/04/3613494_pc3.jpg", "김태리", "서울특별시 강남구 학동로28길 23"));
    }

    public void setIsExpanded(boolean value) {
        isExpanded = value;

        if (isExpanded) listView.setEnabled(false);
        else listView.setEnabled(true);
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (isExpanded) return;

            Object object = listView.getItemAtPosition(position);
            FavoriteVO favorite = (FavoriteVO) object;
            Toast.makeText(getContext(), ((FavoriteVO) object).getName(), Toast.LENGTH_SHORT).show();
        }
    };

}