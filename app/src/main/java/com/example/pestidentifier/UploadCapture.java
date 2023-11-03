package com.example.pestidentifier;

import static com.example.pestidentifier.R.id.go_to_dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class UploadCapture extends AppCompatActivity {

    Button capture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_upload_capture);

        capture = findViewById(go_to_dashboard);
        //upload = findViewById(go_to_dashboard2);

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadCapture.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}