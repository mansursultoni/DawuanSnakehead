package com.sultoniapk.dawuansnakehead;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

public class DaftarActivity extends AppCompatActivity {

    Button TombolKembali;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.skyblue));

        TombolKembali = findViewById(R.id.btnKembali);
        TombolKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}