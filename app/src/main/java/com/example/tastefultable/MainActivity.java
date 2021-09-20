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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import PreferencesManager.PreferencesManager;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;  // Menu Button
    TextView mTextViewEmailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

        loadFragment(new HomeFragment());

        setNavigationDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

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

        // To access the email Id textview from the headerView.
        View headerView = navigationView.getHeaderView(0);
        mTextViewEmailId = (TextView) headerView.findViewById(R.id.textViewUserEmailID);

        String email = PreferencesManager.getString(getApplicationContext(),"Email","");
        Log.i("EMAIL", "setNavigationDrawerEmail: " + email);

        //Toast.makeText(this, "LOGGED IN USER" + email, Toast.LENGTH_SHORT).show();
        mTextViewEmailId.setText(email);
        //Toast.makeText(this, mTextViewEmailId.getText(), Toast.LENGTH_SHORT).show();

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