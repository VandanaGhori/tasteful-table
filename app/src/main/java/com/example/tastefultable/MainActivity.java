package com.example.tastefultable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
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
        setNavigationDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                    mDrawerLayout.closeDrawers();
                } else if (itemId == R.id.nav_favourite) {
                    frag = new FavoriteFragment();
                } else if (itemId == R.id.nav_share_app) {
                    frag = new AppShareFragment();
                }
                Toast.makeText(getApplicationContext(),item.getTitle(),Toast.LENGTH_LONG).show();
                if (frag != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.frameLayout,frag);
                    transaction.commit();
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