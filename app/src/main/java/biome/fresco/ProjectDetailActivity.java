package biome.fresco;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;

import io.codetail.animation.ViewAnimationUtils;

public class ProjectDetailActivity extends AppCompatActivity {

    float x;
    float y;
    Toolbar toolbar;

    View rootLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        x = intent.getExtras().getFloat("xcoord");
        y = intent.getExtras().getFloat("ycoord");



        setContentView(R.layout.activity_project_detail);
        rootLayout = findViewById(R.id.root_layout);

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
                AlphaAnimation anim = new AlphaAnimation(0.0f,1.0f);
                anim.setDuration(350);
                toolbar.startAnimation(anim);
            }
        }, 500);


    }

}
