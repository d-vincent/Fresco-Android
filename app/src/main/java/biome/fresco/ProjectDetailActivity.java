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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import biome.fresco.Fragments.ProjectFilesFragment;
import biome.fresco.Fragments.ProjectNotes;
import biome.fresco.Objects.ProjectObject;
import biome.fresco.Fragments.UserNotes;
import io.codetail.animation.ViewAnimationUtils;

import static biome.fresco.MainActivity.mDatabase;

public class ProjectDetailActivity extends AppCompatActivity {


    public static ProjectObject mProject;

    float x;
    float y;

    String projectId;
    Toolbar toolbar;

    public View topBarView;

    ViewPager mainViewPager;
    TabLayout tabs;
    View rootLayout;


    @Override
    public void onBackPressed() {


        boolean wentBack = false;
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null) {
            //TODO: Perform your logic to pass back press here
            for(Fragment fragment : fragmentList){
                if(fragment instanceof ProjectFilesFragment){
                   if ( ((ProjectFilesFragment)fragment).navigateUp()){
                       wentBack = true;
                   }

                }
            }
        }
        if(!wentBack){
            super.onBackPressed();
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        x = intent.getExtras().getFloat("xcoord");
        y = intent.getExtras().getFloat("ycoord");







        projectId = intent.getExtras().getString("projectId");

        mProject = new ProjectObject();
        mDatabase.child("projects").child(projectId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProject.setAuthorId((String)dataSnapshot.child("author").getValue());
                mProject.setProjectId(projectId);
                mProject.setName((String)dataSnapshot.child("name").getValue());
                mProject.setProjectDescription((String)dataSnapshot.child("desc").getValue());

                //DataSnapshot chatIds = dataSnapshot.child("chats").getChildren();
                ArrayList<String> chatIds = new ArrayList<String>();
                for (DataSnapshot snap: dataSnapshot.child("chats").getChildren()){
                    chatIds.add((String)snap.getKey());
                }
                mProject.setChatIds(chatIds);
                mProject.setSearchName((String)dataSnapshot.child("searchName").getValue());
                mProject.setRootChatId((String)dataSnapshot.child("rootChat").getValue());
                mProject.setRootFolderId((String)dataSnapshot.child("rootFolder").getValue());

                setContentView(R.layout.activity_project_detail);
                rootLayout = findViewById(R.id.root_layout);

                topBarView = findViewById(R.id.top_bar_layout);


                tabs = (TabLayout) findViewById(R.id.sliding_tabs);
                toolbar = (Toolbar)findViewById(R.id.my_toolbar);
                setSupportActionBar(toolbar);

                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_48dp);

                toolbar.setTitle(mProject.getName());

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
                tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                    UserNotes tab3 = UserNotes.newInstance();
                    return tab3;
                case 3:
                    ProjectFilesFragment tab4 = ProjectFilesFragment.newInstance();
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
                    return "Agenda";
                case 1:
                    return "Project Notes";
                case 2:
                    return "Personal Notes";
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
