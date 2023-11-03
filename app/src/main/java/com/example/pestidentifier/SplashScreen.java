package com.example.pestidentifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    //Use the handler function to determine the load duration
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Use the fullScreen and hide the notifications bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        //Call the PostDelayed Method and tell it the duration of the delayed Method
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Use the intent package to load a next activity
                Intent intent = new Intent(SplashScreen.this, UploadCapture.class);
                startActivity(intent);
                finish();
            }
        }, 4000);



    }
}