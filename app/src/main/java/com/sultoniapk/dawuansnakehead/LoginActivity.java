package com.sultoniapk.dawuansnakehead;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;


public class LoginActivity extends AppCompatActivity {
    Button TombolDaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.skyblue));

        TombolDaftar = findViewById(R.id.btnDaftar);
        TombolDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,DaftarActivity.class));
            }
        });
    }
}