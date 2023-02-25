package com.sultoniapk.dawuansnakehead;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {
    Button TombolDaftar, TombolLogin;
    EditText EditUsername, EditTelepon;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://dawuansnakehead-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.skyblue));

        TombolLogin     = findViewById(R.id.btnLogin);
        TombolDaftar    = findViewById(R.id.btnDaftar);

        EditUsername    = findViewById(R.id.txtUsername);
        EditTelepon     = findViewById(R.id.txtTelepon);

        TombolLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateUsername() | !validateTelepon()){

                }
                else {
                    cekUser();
                }
            }
        });

        TombolDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,DaftarActivity.class));
            }
        });


    }
    public void cekUser(){
        String userUsername = EditUsername.getText().toString();
        String userTelepon  = EditTelepon.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        Query cekUserDatabase       = reference.orderByChild("username").equalTo(userUsername);

        cekUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    EditUsername.setError(null);
                    String tlepDB = snapshot.child(userUsername).child("telepon").getValue(String.class);

                    if (!Objects.equals(tlepDB, userTelepon)){
                        EditUsername.setError(null);
                        startActivity(new Intent(LoginActivity.this,UserDashboard.class));
                        finish();
                    }
                    else {
                        EditTelepon.setError("Telepon tidak ditemukan.");
                        EditTelepon.requestFocus();
                    }
                }
                else {
                    EditUsername.setError("Username tidak ditemukan.");
                    EditUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public Boolean validateUsername(){
        String val = EditUsername.getText().toString();
        if (val.isEmpty()){
            EditUsername.setError("Username tidak boleh kosong.");
            return false;
        }
        else {
            EditUsername.setError(null);
            return true;
        }
    }

    public Boolean validateTelepon(){
        String val = EditTelepon.getText().toString();
        if (val.isEmpty()){
            EditTelepon.setError("Telepon tidak boleh kosong.");
            return false;
        }
        else {
            EditTelepon.setError(null);
            return true;
        }
    }
}