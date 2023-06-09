package com.barakadanie.bcd.pentopaperwritersltd.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.barakadanie.bcd.pentopaperwritersltd.Common.LoginActivity;
import com.barakadanie.bcd.pentopaperwritersltd.Common.RegisterActivity;
import com.barakadanie.bcd.pentopaperwritersltd.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
private DrawerLayout drawerLayout;
private Toolbar mToolBar;
BottomNavigationView bottomNavigationView;
private NavigationView navigationView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.menu);
        bottomNavigationView=findViewById(R.id.bottom_nav);
        mToolBar=findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        bottomNavigationView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) this);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,mToolBar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        newRecordFragment myfragment=new newRecordFragment();
    }
    protected  void onDestroy()
    {
        super.onDestroy();
        clearUserSession();
        releaseResources();
    }

    private void releaseResources() {
        FirebaseDatabase.getInstance().goOffline();
    }

    private void clearUserSession() {
        SharedPreferences sharedPreferences=getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId())
        {
            case R.id.addRecord:
                fragment=new newRecordFragment();
                break;
            case R.id.history:
                fragment=new HistoryFragment();
                break;
            case R.id.updateRecord:
                fragment=new UpdateRecordFragment();
                break;
            case R.id.records:
                fragment=new RecordsFragment();
                break;
            default:
                return false;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
        return true;
    }
}