package biome.fresco;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;

import java.util.Locale;

import io.codetail.animation.ViewAnimationUtils;

public class ProjectDetailActivity extends AppCompatActivity {


    public static ProjectObject mProject;

    float x;
    float y;
    Toolbar toolbar;

    ViewPager mainViewPager;
    TabLayout tabs;
    View rootLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        x = intent.getExtras().getFloat("xcoord");
        y = intent.getExtras().getFloat("ycoord");




        setContentView(R.layout.activity_project_detail);
        rootLayout = findViewById(R.id.root_layout);

        tabs = (TabLayout) findViewById(R.id.sliding_tabs);
        toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        toolbar.setBackground(getResources().getDrawable(R.drawable.actionbar_top));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Project Name");

        if (savedInstanceState == null) {
            rootLayout.setVisibility(View.INVISIBLE);

            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        circularRevealActivity();
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });
            }
        }


        mainViewPager = (ViewPager)findViewById(R.id.pager);
        mainViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(),4));
        tabs = (TabLayout)findViewById(R.id.sliding_tabs);
        tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
        tabs.setupWithViewPager(mainViewPager);
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        tabs.setTabMode(TabLayout.MODE_FIXED);
        tabs.setMinimumWidth(400);
        mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabs.getTabAt(position).select();


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void circularRevealActivity() {

        int cx = Math.round(x);
        int cy = Math.round(y);
        float finalRadius = Math.max(rootLayout.getWidth(), rootLayout.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, cx, cy, 0, finalRadius);
        circularReveal.setDuration(500);

        // make the view visible and start the animation
        rootLayout.setVisibility(View.VISIBLE);
        circularReveal.start();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                toolbar.setVisibility(View.VISIBLE);
                tabs.setVisibility(View.VISIBLE);
                mainViewPager.setVisibility(View.VISIBLE);
                AlphaAnimation anim = new AlphaAnimation(0.0f,1.0f);
                anim.setDuration(350);
                toolbar.startAnimation(anim);
                tabs.startAnimation(anim);
                mainViewPager.startAnimation(anim);
            }
        }, 500);
    }


    public class PagerAdapter extends FragmentPagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    ProjectNotes tab1 = ProjectNotes.newInstance();
                    return tab1;
                case 1:
                    ProjectNotes tab2 = ProjectNotes.newInstance();
                    return tab2;
                case 2:
                    ProjectNotes tab3 = ProjectNotes.newInstance();
                    return tab3;
                case 3:
                    ProjectNotes tab4 = ProjectNotes.newInstance();
                    return tab4;

                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Notes";
                case 1:
                    return "Your Notes";
                case 2:
                    return "Chats";
                case 3:
                    return "Files";
            }
            return null;
        }


        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
