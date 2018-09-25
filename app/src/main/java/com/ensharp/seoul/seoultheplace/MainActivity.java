package com.ensharp.seoul.seoultheplace;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.ensharp.seoul.seoultheplace.Course.CourseModifyFragment;
import com.ensharp.seoul.seoultheplace.Course.SaveCourseActivity;
import com.ensharp.seoul.seoultheplace.Fragments.*;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageButton[] bottomButtons;
    private Fragment[] fragments;
    private Fragment currentFragment;
    private LinearLayout rootLayout;
    private int currentFragmentNumber = 0;
    private Fragment fragmentToChange;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // course, place 초기화
        setContentView(R.layout.activity_main);

        fragments = new Fragment[]{
                new MainFragment(), new CustomizedFragment(), new LikeFragment(), new SettingFragment()
        };

        ImageButton homeButton = findViewById(R.id.homeButton);
        ImageButton searchButton = findViewById(R.id.searchButton);
        ImageButton bookMarkButton = findViewById(R.id.bookmarkButton);
        ImageButton myPageButton = findViewById(R.id.mypageButton);

        homeButton.setTag("home");
        searchButton.setTag("search");
        bookMarkButton.setTag("bookmark");
        myPageButton.setTag("mypage");

        // 하단 버튼 객체 초기화
        bottomButtons = new ImageButton[] {
                homeButton,
                searchButton,
                bookMarkButton,
                myPageButton
        };

        for(int i=0; i<fragments.length; i++) {
            bottomButtons[i].setOnClickListener(bottomButtonOnClickListener);
        }

        // 메인 fragment
        currentFragment = fragments[0];
        currentFragmentNumber = 0;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragments[0], "MainFragment")
                .commit();

        rootLayout = (LinearLayout) findViewById(R.id.linear_wrapper);
        rootLayout.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        int rootViewHeight = rootLayout.getRootView().getHeight();
                        int linearWrapperHeight = rootLayout.getHeight();
                        int diff = rootViewHeight - linearWrapperHeight;
                        // 키보드가 내려간 상태면
                        if(currentFragment.equals(fragments[0]) && diff < dpToPx(50)) {
                            ((MainFragment)fragments[0]).viewVisible();
                        }
                        else {
                            ((MainFragment)fragments[0]).viewInvisible();
                        }
                    }
                });

        String[] neededPermissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        ActivityCompat.requestPermissions(this, neededPermissions,0);
    }

    //0번째가 홈, 1번째가 내가만든 코스, 2번째가 좋아요 코스, 3번째가 좋아요 코스
    public void setBottomButtons(int currentFragment, int nextFragment) {

        //현재 프레그먼트가 홈
        if(currentFragment == 0) {
            bottomButtons[currentFragment].setImageResource(R.drawable.home);
            //다음 프레그먼트가 홈
           if(nextFragment == 0) {
              bottomButtons[currentFragment].setImageResource(R.drawable.home_colored);
           } else if(nextFragment == 1) { //다음 프레그먼트가 내가 편집한 프레그먼트
               bottomButtons[nextFragment].setImageResource(R.drawable.notebook_colored);
           } else if(nextFragment == 2) { //다음 프레그먼트가 좋아요 프레그먼트
               bottomButtons[nextFragment].setImageResource(R.drawable.heart_colored);
           } else if(nextFragment == 3) { //다음 프레그먼트가 내정보 프레그먼트
               bottomButtons[nextFragment].setImageResource(R.drawable.user_colored);
           }
        }
        //현재 프레그먼트가 내가만든 코스
        else if(currentFragment == 1) {
            bottomButtons[currentFragment].setImageResource(R.drawable.notebook);
            //다음 프레그먼트가 홈
            if(nextFragment == 0) {
                bottomButtons[nextFragment].setImageResource(R.drawable.home_colored);
            } else if(nextFragment == 1) {
                bottomButtons[currentFragment].setImageResource(R.drawable.notebook_colored);
            } else if(nextFragment == 2) {
                bottomButtons[nextFragment].setImageResource(R.drawable.heart_colored);
            } else if(nextFragment == 3) {
                bottomButtons[nextFragment].setImageResource(R.drawable.user_colored);
            }
        }
        //현재 프레그먼트가 좋아요
        else if(currentFragment == 2) {
            bottomButtons[currentFragment].setImageResource(R.drawable.heart);
            //다음 프레그먼트가 홈
            if(nextFragment == 0) {
                bottomButtons[nextFragment].setImageResource(R.drawable.home_colored);
            } else if(nextFragment == 1) {
                bottomButtons[nextFragment].setImageResource(R.drawable.notebook_colored);
            } else if(nextFragment == 2) {
                bottomButtons[currentFragment].setImageResource(R.drawable.heart_colored);
            } else if(nextFragment == 3) {
                bottomButtons[nextFragment].setImageResource(R.drawable.user_colored);
            }
        }
        else {
            bottomButtons[currentFragment].setImageResource(R.drawable.user);
            if(nextFragment == 0) {
                bottomButtons[nextFragment].setImageResource(R.drawable.home_colored);
            } else if(nextFragment == 1) {
                bottomButtons[nextFragment].setImageResource(R.drawable.notebook_colored);
            } else if(nextFragment == 2) {
                bottomButtons[nextFragment].setImageResource(R.drawable.heart_colored);
            } else if(nextFragment == 3) {
                bottomButtons[currentFragment].setImageResource(R.drawable.user_colored);
            }
        }
    }

    private View.OnClickListener bottomButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Fragment fragment = fragments[0];
            int nextFragmentNumber = 0;

            switch ((String) view.getTag()) {
                case "home":
                    fragment = fragments[0];
                    nextFragmentNumber = 0;
                    break;
                case "search":
                    fragment = fragments[1];
                    nextFragmentNumber = 1;
                    break;
                case "bookmark":
                    fragment = fragments[2];
                    nextFragmentNumber = 2;
                    break;
                case "mypage":
                    fragment = fragments[3];
                    nextFragmentNumber = 3;
                    break;
            }

            currentFragment = fragment;
            if(currentFragmentNumber <= nextFragmentNumber) {
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
                        .replace(R.id.fragment, fragment, "")
                        .commit();
            }
            else{
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right)
                        .replace(R.id.fragment, fragment)
                        .commit();
            }
            DeleteBackStack();
            currentFragmentNumber = nextFragmentNumber;
        }
    };

    public float dpToPx(float valueInDp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);

    }

    public void changeToWebFragment(String link) {
        final Fragment fragment = new WebViewFragment(link);

        Log.e("web/MainActivity", "came here");

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment, "WEB_FRAGMENT")
                .addToBackStack(null)
                .commit();
    }

    public void changeToCourseFragment(CourseVO course, int enterRoute) {
        final Fragment fragment = new CourseFragment(course, enterRoute);

        Log.e("editted_course/MainActivity", "came here");

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void changeToPlaceFragment(CourseVO course, int index, int enterRoute) {
        if (enterRoute == PlaceFragment.VIA_COURSE)
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.anim_slide_in_bottom,R.anim.anim_slide_out_top,R.anim.anim_slide_in_top,R.anim.anim_slide_out_bottom)
                    .replace(R.id.fragment, new PlaceFragment(course, index, enterRoute))
                    .addToBackStack("PLACE_FRAGMENT")
                    .commit();
        else
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.anim_slide_in_bottom,R.anim.anim_slide_out_top,R.anim.anim_slide_in_top,R.anim.anim_slide_out_bottom)
                    .replace(R.id.fragment, new PlaceFragment(course, index, enterRoute))
                    .addToBackStack(null)
                    .commit();
    }

    public void changeToPlaceFragment(String placeCode, int enterRoute) {
        if (enterRoute == PlaceFragment.VIA_COURSE)
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.anim_slide_in_bottom,R.anim.anim_slide_out_top,R.anim.anim_slide_in_top,R.anim.anim_slide_out_bottom)
                    .replace(R.id.fragment, new PlaceFragment(placeCode, enterRoute))
                    .addToBackStack("PLACE_FRAGMENT")
                    .commit();
        else
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.anim_slide_in_bottom,R.anim.anim_slide_out_top,R.anim.anim_slide_in_top,R.anim.anim_slide_out_bottom)
                    .replace(R.id.fragment, new PlaceFragment(placeCode, enterRoute))
                    .addToBackStack(null)
                    .commit();
    }

    public void changeModifyFragment(List<PlaceVO> list){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right,R.anim.anim_slide_in_right,R.anim.anim_slide_out_left)
                .replace(R.id.fragment, new CourseModifyFragment(list))
                .addToBackStack(null)
                .commit();
    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    public void DeleteBackStack() { //뒤로가기 눌렀을시 전 프래그먼트로 이동 X
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void SetSaveData(String[] datas){
        for(int i = 0 ; i < datas.length;i++){
            if(datas[i]!=null)
            Log.e("Datas :",datas[i]);
        }
        Intent intent = new Intent(this, SaveCourseActivity.class);
        intent.putExtra("codes",datas);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String[] insertData = new String[]{null, null, null, null, null, null, null, null};
                SharedPreferences sp = getSharedPreferences("data", 0);
                Log.e("SaveTest :", "onActivityResult");
                Log.e("SaveTest : ", "email " + sp.getString("email", ""));
                insertData[0] = sp.getString("email", "");
                insertData[1] = data.getStringExtra("title");
                insertData[2] = data.getStringExtra("description");
                for (int i = 0; i < data.getStringArrayExtra("codes").length; i++) {
                    if (data.getStringArrayExtra("codes")[i] != null) {
                        insertData[3 + i] = data.getStringArrayExtra("codes")[i];
                    } else {
                        insertData[3 + i] = null;
                    }
                }
                DAO dao = new DAO();
                Log.e("SaveTest : ", dao.insertMemberCourseData(insertData));
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right)
                        .replace(R.id.fragment, new CustomizedFragment())
                        .commit();
            }
        }
    }

    public String getUserID() {
        return getSharedPreferences("data", 0).getString("email","");
    }

    public void ChangeMakersFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, new MakersFragment())
                .addToBackStack(null)
                .commit();
    }

    public void ChangeSettingFragment(){
        DeleteBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragments[3])
                .commit();
    }
}
