package com.huaihsuanhuang.chatterbox.Mainpage;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.huaihsuanhuang.chatterbox.Account.SettingActivity;
import com.huaihsuanhuang.chatterbox.Account.StartActivity;
import com.huaihsuanhuang.chatterbox.Account.UsersActivity;
import com.huaihsuanhuang.chatterbox.Adapter.SectionPagerAdapter;
import com.huaihsuanhuang.chatterbox.R;

import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private android.support.v7.widget.Toolbar mToolbar;
    private RelativeLayout mainactivity_layout;
    private ViewPager mviewPager;
    private SectionPagerAdapter msectionPagerAdapter;
    private TabLayout mtabLayout;
    private DatabaseReference userref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            mAuth = FirebaseAuth.getInstance();
            if (mAuth.getCurrentUser() != null) {
                mToolbar = findViewById(R.id.toolbar_mainactivity);
                mainactivity_layout = findViewById(R.id.mainactivity_layout);
                setSupportActionBar(mToolbar);
                getSupportActionBar().setTitle("ChatterBox");
                mviewPager = findViewById(R.id.viewpager_main);
                msectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
                mviewPager.setAdapter(msectionPagerAdapter);
                mtabLayout = findViewById(R.id.tablayout_mainactivity);
                mtabLayout.setupWithViewPager(mviewPager);

                userref = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

            } else {
                sendtostartpage();
            }
        }



    @Override
    public void onStart() {
        super.onStart();

            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {
                sendtostartpage();
            } else {
                userref.child("online").setValue("true");
            }
    }


    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentuser != null) {

            userref.child("online").setValue(String.valueOf(System.currentTimeMillis()));

        }
    }

    private void sendtostartpage() {
        Intent intent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main_side, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.menu_main_side_logout: {
                Snackbar.make(mainactivity_layout, "Logged out", Snackbar.LENGTH_SHORT).show();
                mAuth.signOut();
                sendtostartpage();


                return true;
            }
            case R.id.menu_main_side_allusers: {
                Intent intent = new Intent(MainActivity.this, UsersActivity.class);
                startActivity(intent);

                return true;
            }
            case R.id.menu_main_side_setting: {
                Intent intent_setting = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent_setting);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
