package com.example.tastefultable;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashScreenActivity.this.runOnUiThread(() -> { openNextScreen(); });
            }
        },2000);
    }

    private void openNextScreen() {
        startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
        finish();
    }
}
