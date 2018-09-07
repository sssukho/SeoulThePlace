package com.ensharp.seoul.seoultheplace.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ensharp.seoul.seoultheplace.Login.LoginBackgroundActivity;
import com.ensharp.seoul.seoultheplace.R;
import com.ensharp.seoul.seoultheplace.UIElement.HorizontalListView;
import com.ensharp.seoul.seoultheplace.UIElement.TagAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingFragment extends Fragment implements View.OnClickListener {
    public SettingFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_setting, container, false);

        ArrayList<String> tags = new ArrayList<String>(
                Arrays.asList(new String[]{"로그아웃", "튜토리얼", "피드백 보내기"}));

        TagAdapter tagAdapter = new TagAdapter(getActivity(), tags);

        HorizontalListView tagListView = (HorizontalListView) rootView.findViewById(R.id.tagListView);
        tagListView.setAdapter(tagAdapter);

        Button btn = (Button)rootView.findViewById(R.id.logout);
        btn.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout:
                SharedPreferences sf = getActivity().getSharedPreferences("data",0);
                SharedPreferences.Editor editor = sf.edit();
                editor.remove("email");
                editor.remove("password");
                editor.remove("name");
                editor.remove("age");
                editor.remove("type");
                editor.remove("sex");
                editor.commit();
                Intent intent = new Intent(getActivity(),LoginBackgroundActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }
    }
}