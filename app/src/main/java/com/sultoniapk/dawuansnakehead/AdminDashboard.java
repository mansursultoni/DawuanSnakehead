package com.sultoniapk.dawuansnakehead;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;


import static com.sultoniapk.dawuansnakehead.R.drawable.ic_user;

public class AdminDashboard extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

//        getSupportActionBar().setHomeAsUpIndicator(ic_user);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        firebaseFirestore = FirebaseFirestore.getInstance();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        getData();
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this, ActivityTambahProduk.class));
            }
        });
    }
    private void getData() {
        Query query = firebaseFirestore.collection("Contacts");
        FirestoreRecyclerOptions<ClassProduk> response = new FirestoreRecyclerOptions.Builder<ClassProduk>()
                .setQuery(query, ClassProduk.class).build();
        adapter = new FirestoreRecyclerAdapter<ClassProduk, ContactsHolder>(response) {
            @NonNull
            @Override
            public ContactsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produk, parent, false);
                return new ContactsHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull ContactsHolder holder, int position, @NonNull final ClassProduk model) {
                progressBar.setVisibility(View.GONE);
                if( model.getFoto() != null) {
                    Picasso.get().load(model.getFoto()).fit().into(holder.fotoContact);
                }else{
                    Picasso.get().load(ic_user).fit().into(holder.fotoContact);
                }
                holder.namaContact.setText(model.getNama());
                holder.teleponContact.setText(model.getTelepon());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AdminDashboard.this, ActivityDetailProduk.class);
                        intent.putExtra("telepon", model.getTelepon());
                        startActivity(intent);
//Snackbar.make(recyclerView, model.getNama()+", " +model.getTelepon(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                });
            }
            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                Log.e("Ditemukan Error: ", e.getMessage());
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
    public class ContactsHolder extends RecyclerView.ViewHolder{
        ImageView fotoContact;
        TextView namaContact;
        TextView teleponContact;
        ConstraintLayout constraintLayout;
        public ContactsHolder(@NonNull View itemView) {
            super(itemView);
            fotoContact = itemView.findViewById(R.id.imageViewFoto);
            namaContact = itemView.findViewById(R.id.textViewNama);
            teleponContact = itemView.findViewById(R.id.textViewTelepon);
//            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onResume() {
        super.onResume();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}