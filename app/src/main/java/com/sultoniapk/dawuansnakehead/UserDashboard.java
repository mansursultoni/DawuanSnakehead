package com.sultoniapk.dawuansnakehead;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserDashboard extends AppCompatActivity {

    Button TombolLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        TombolLogout     = findViewById(R.id.btnLogout);

        TombolLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDashboard.this,LoginActivity.class));
                finish();
            }
        });
    }
}