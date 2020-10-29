package com.example.introslider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IntroSliderActivity extends AppCompatActivity {

    private int currentItem;//current item of view pager
    private int itemLength;//length item of view pager
    private IntroSliderPrefManager prefManager;//managing preference
    private ViewPager viewPager;
    private LinearLayout layoutDots;
    private Button btnSkip, btnNext;
    private ViewPagerAdapter adapter;//adapter of viewPager
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slider);
        prefManager = new IntroSliderPrefManager(this);
        if (!prefManager.getPref()) {
            goToHome();
            return;
        }
        statusBar();
        viewPager = findViewById(R.id.view_pager);
        layoutDots = findViewById(R.id.layoutDots);
        btnNext = findViewById(R.id.btnNext);
        btnSkip = findViewById(R.id.btnSkip);
        adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        itemLength = viewPager.getAdapter().getCount();
        showDots(viewPager.getCurrentItem());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                showDots(viewPager.getCurrentItem());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (viewPager.getCurrentItem() == itemLength - 1){
                    btnSkip.setVisibility(View.GONE);
                    btnNext.setText(R.string.gotit);
                } else {
                    btnSkip.setVisibility(View.VISIBLE);
                    btnNext.setText(R.string.next);
                }
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.setPref(false);
                goToHome();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == itemLength - 1) {//got it
                    prefManager.setPref(false);
                    goToHome();
                } else {//next
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            }
        });
    }

    private void showDots(int pageNumber) {
        TextView [] dots = new TextView[itemLength];
        layoutDots.removeAllViews();
        for (int i = 0; i< dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
            dots[i].setTextColor(ContextCompat.getColor(this,
                    (i == pageNumber ? R.color.dot_active : R.color.dot_incative)));
            layoutDots.addView(dots[i]);
        }
    }

    private void goToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void statusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public class ViewPagerAdapter extends PagerAdapter {
        String [] titles = getResources().getStringArray(R.array.slide_titles);
        String [] desc = getResources().getStringArray(R.array.slide_descriptions);
        int [] icons = {R.drawable.ic_food, R.drawable.ic_movie, R.drawable.ic_discount, R.drawable.ic_travel};
        int [] bgColor = {R.color.slide_1_bg_color, R.color.slide_2_bg_color, R.color.slide_3_bg_color, R.color.slide_4_bg_color};

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = LayoutInflater.from(IntroSliderActivity.this).inflate(R.layout.slide, container, false);
            //background color
            ConstraintLayout layout = view.findViewById(R.id.bgLayout);
            layout.setBackgroundColor(ContextCompat.getColor(IntroSliderActivity.this, bgColor[position]));
            //background color
            //titles
            TextView lblTitle = view.findViewById(R.id.lblTitle);
            lblTitle.setText(titles[position]);
            //titles
            //decs
            TextView lblDesc = view.findViewById(R.id.lblDesc);
            lblDesc.setText(desc[position]);
            //decs
            //icons
            ImageView imgIconSlider = view.findViewById(R.id.imgIconSlider);
            imgIconSlider.setImageResource(icons[position]);
            //icons
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}