package com.sultoniapk.dawuansnakehead;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DaftarActivity extends AppCompatActivity {

    private Button TombolKembali, TombolDaftar;
    private EditText Nama, Username, Telepon;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.skyblue));

        TombolKembali   = findViewById(R.id.btnKembali);
        TombolDaftar    = findViewById(R.id.btnDaftar);

        Nama            = findViewById(R.id.txtNama);
        Username        = findViewById(R.id.txtUsername);
        Telepon         = findViewById(R.id.txtTelepon);

        TombolDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userUsername   = Username.getText().toString();
                final String userTelepon    = Telepon.getText().toString();
                final String userNama       = Nama.getText().toString();

                if (userNama.isEmpty() || userUsername.isEmpty() || userTelepon.isEmpty()){
                    Toast.makeText(DaftarActivity.this, "Mohon isi data dengan lengkap..", Toast.LENGTH_SHORT).show();
                }
                else{
                    database    = FirebaseDatabase.getInstance();
                    reference   = database.getReference("user");

                    String nama         = Nama.getText().toString();
                    String username     = Username.getText().toString();
                    String telepon      = Telepon.getText().toString();

                    HelperClass helperClass = new HelperClass(nama, username, telepon);
                    reference.child(nama).setValue(helperClass);

                    Toast.makeText(DaftarActivity.this, "Registrasi berhasil, silahkan Login.", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

        TombolKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}