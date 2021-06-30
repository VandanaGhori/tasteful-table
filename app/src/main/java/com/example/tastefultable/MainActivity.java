package com.example.tastefultable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.admin.FactoryResetProtectionPolicy;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;  // Menu Button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        loadFragment(new HomeFragment());

        setNavigationDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Need to override this method to hide soft keyboard over the change between EditTextView
    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }*/

    private void loadFragment(Fragment frag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout,frag);
        transaction.commit();
    }

    private void initialize() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.myDrawerLayout);
        mToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
    }

    private void setNavigationDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Fragment frag = null;
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    frag = new HomeFragment();
                } else if (itemId == R.id.nav_my_account) {
                    frag = new LoginFragment();
                } else if (itemId == R.id.nav_favourite) {
                    frag = new FavoriteFragment();
                } else if (itemId == R.id.nav_share_app) {
                    frag = new AppShareFragment();
                }
               // Toast.makeText(getApplicationContext(),item.getTitle(),Toast.LENGTH_LONG).show();
                if (frag != null) {
                    loadFragment(frag);
                    mDrawerLayout.closeDrawers();
                    return true;
                }
                return false;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}