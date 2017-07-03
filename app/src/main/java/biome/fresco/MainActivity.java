package biome.fresco;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import biome.fresco.Fragments.Feed;
import biome.fresco.Fragments.LoginFrag;
import biome.fresco.Fragments.MainFragment;
import biome.fresco.Fragments.ProjectFragment;
import biome.fresco.Fragments.TeamFragment;
import biome.fresco.Objects.ChatObject;
import biome.fresco.Fragments.DirectMessage;

import static com.google.android.gms.internal.zzs.TAG;


public class MainActivity extends AppCompatActivity {


    public static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static DatabaseReference mDatabase;
    public static String uid;
    public static String token;
    String toUserPhotoUrl;

    public FloatingActionMenu mFab;
    DrawerLayout mDrawerLayout;

    Fragment mFragment;

    public static int RC_GOOGLE_SIGN_IN = 0;

    Toolbar toolbar;
    MaterialSearchBar searchBar;
    public BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FirebaseApp.initializeApp(this);
        //Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        mFab = (FloatingActionMenu) findViewById(R.id.main_fab);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    uid = user.getUid();
                    if (token != null) {
                        mDatabase.child("users").child(uid).child("tokens").child("android").child(token);
                    }
                    mFragment = ProjectFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().add(R.id.container, mFragment).commit();
                    findViewById(R.id.bottomBar).setVisibility(View.VISIBLE);

                    Log.d("Firebase", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    getSupportFragmentManager().beginTransaction().add(R.id.container, LoginFrag.newInstance()).commit();
                    Log.d("Firebase", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        Bundle extras = getIntent().getExtras();
        if (extras != null){

            if (extras.getString("userid") != null) {
                String chatType = extras.getString("type");
                final String chatId = extras.getString("key");
                //boolean isDirectMessage = extras.getBoolean("directMessages");
                final String toUserId = extras.getString("userid");
                final String toUserName = extras.getString("name");


                mDatabase.child("users").child(toUserId).child("photoUrl").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        toUserPhotoUrl = (String) dataSnapshot.getValue();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                mDatabase.child("users").child(chatId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        ChatObject chatObject = new ChatObject(chatId);
                        chatObject.setToUserId(toUserId);
                        chatObject.setToUserImageUrl(toUserPhotoUrl);
                        chatObject.setToUserName(toUserName);

                        FragmentManager fm = getSupportFragmentManager();
                        fm.beginTransaction().replace(R.id.container, DirectMessage.newInstance(chatId, toUserPhotoUrl, toUserId, toUserName)).addToBackStack("").commit();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
//            Toast.makeText(this, chatType.length(), Toast.LENGTH_LONG).show();
        }

        bottomBar = (BottomBar)findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId){
                    case R.id.tab_home:
                        if (!(mFragment instanceof ProjectFragment)){
                            mFragment = ProjectFragment.newInstance();
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, mFragment).commit();
                        }
                        break;
                    case R.id.tab_projects:

                        if (!(mFragment instanceof ProjectFragment)){
                            mFragment = ProjectFragment.newInstance();
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, mFragment).commit();
                        }
                        break;
                    case R.id.tab_teams:

                        if (!(mFragment instanceof TeamFragment)){
                            mFragment = TeamFragment.newInstance();
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, mFragment).commit();
                        }
                        break;
                    case R.id.tab_messages:

                        if (!(mFragment instanceof Feed)){
                            mFragment = Feed.newInstance();
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, mFragment).commit();
                        }
                        break;
                    case R.id.tab_search:
                        if (!(mFragment instanceof SearchFragment)){
                            mFragment = SearchFragment.newInstance();
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, mFragment).commit();
                        }
                        break;
                }
            }
        });

        toolbar = (Toolbar)findViewById(R.id.my_toolbar);
       // toolbar.setBackground(getResources().getDrawable(R.drawable.actionbar_top));
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,  mDrawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle.syncState();

        searchBar = (MaterialSearchBar)findViewById(R.id.search_bar);

    }

    public void openDrawer(){
       mDrawerLayout.openDrawer(Gravity.LEFT);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.toolbar_search:
                Toast.makeText(this, "Some search results", Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }

    private void handleSignInResult(GoogleSignInResult result){

        if(result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            FirebaseUser user = mAuth.getCurrentUser();
            Toast.makeText(this, user.getEmail(), Toast.LENGTH_SHORT).show();
           // getSupportFragmentManager().beginTransaction().replace(R.id.container, MainFragment.newInstance()).commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        switch ( requestCode){
//            case 0:{
//                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//                handleSignInResult(result);
//            }
//            default:
//            {
//                super.onActivityResult(requestCode,resultCode,data);
//            }
//        }
    }

    public void login(String email, String password){


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {

                            getSupportFragmentManager().beginTransaction().replace(R.id.container, MainFragment.newInstance()).commit();

                        }

                        // ...
                    }
                });

    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }



    private void signIn(String email, String password) {




        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Firebase", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Firebase", "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });
        // [END sign_in_with_email]
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
