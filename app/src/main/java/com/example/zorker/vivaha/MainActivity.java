package com.example.zorker.vivaha;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.zorker.vivaha.Account.AccountFragment;
import com.example.zorker.vivaha.Account.RegisterUserArea;
import com.example.zorker.vivaha.Account.RegisterUserDetails;
import com.example.zorker.vivaha.Chat.ChatFragment;
import com.example.zorker.vivaha.Inbox.InboxFragment;
import com.example.zorker.vivaha.Matches.MatchesFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private AccountFragment fragment_profile;
    private MatchesFragment fragment_matches;
    private InboxFragment fragment_inbox;
    private ChatFragment fragment_chat;
    private FrameLayout frameLayout;
    //private RegisterUserDetails frame_test;
    private android.support.v7.widget.Toolbar toolbar;
    private FirebaseAuth mauth;
    private long timeinmillis;
    private Toast toast;
    private DatabaseReference mref_users;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu,menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onBackPressed() {


        if (timeinmillis + 2000 > System.currentTimeMillis()) {
            toast.cancel();
            super.onBackPressed();
        }
        else {
            toast = Toast.makeText(getBaseContext(),"press back again to exit",Toast.LENGTH_SHORT);
            toast.show();
        }
        timeinmillis = System.currentTimeMillis();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.toolbar_menu_help:
                Toast.makeText(this, "help", Toast.LENGTH_SHORT).show();
                break;
            case R.id.toolbar_menu_about:
                startActivity(new Intent(getApplicationContext(),About.class));
                break;
            case R.id.toolbar_menu_logout:
                mauth = FirebaseAuth.getInstance();
                mauth.signOut();
                Intent logout = new Intent(MainActivity.this,FrameHolderAccount.class);
                startActivity(logout);
                finish();
                break;
                default:
                    return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mauth= FirebaseAuth.getInstance();
        mref_users = FirebaseDatabase.getInstance().getReference("Users");

        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        toolbar.setTitle("vivaha");
        toolbar.setLogo(R.mipmap.vivaha_toolbar_logo_final);


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_widget);
        frameLayout = (FrameLayout) findViewById(R.id.framelayout_main);
        fragment_profile = new AccountFragment();
        fragment_matches = new MatchesFragment();
        fragment_inbox = new InboxFragment();
        fragment_chat = new ChatFragment();
        //frame_test = new RegisterUserDetails();
        setFragment(fragment_matches);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_matches);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.bottom_nav_profile:
                        setFragment(fragment_profile);
                        break;
                    case R.id.bottom_nav_matches:
                        setFragment(fragment_matches);
                        break;
                    case R.id.bottom_nav_inbox:
                        setFragment(fragment_inbox);
                        break;
                    case R.id.bottom_nav_chat:
                        setFragment(fragment_chat);
                        break;
                        default:
                            return false;
                }
                return true;
            }


        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mauth.getCurrentUser();

        if (user==null)
        {

            startActivity(new Intent(MainActivity.this,FrameHolderAccount.class));
            finish();
        }
        else
        {
            mref_users.child(mauth.getUid()).child("u_online").setValue(1);
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mauth.getCurrentUser()!=null) {
            mref_users.child(mauth.getUid()).child("u_online").setValue(ServerValue.TIMESTAMP);
        }
    }


    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout_main,fragment);
        fragmentTransaction.commit();
    }
}
