package com.example.ratatouille_android.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ratatouille_android.BuildConfig;
import com.example.ratatouille_android.R;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {
    public static String address = "http://ec2-13-39-47-160.eu-west-3.compute.amazonaws.com:5000";
    private Button btn;
    public static FirebaseAnalytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = FirebaseAnalytics.getInstance(this);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.entra_in_app);
        View.OnClickListener onClickEntraInAppListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeActivity();
            }
        };
        btn.setOnClickListener(onClickEntraInAppListener);

    }

    private void changeActivity(){
        Intent switchActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(switchActivityIntent);
    }
}