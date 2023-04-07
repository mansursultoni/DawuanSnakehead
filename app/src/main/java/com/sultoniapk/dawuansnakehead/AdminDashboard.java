package com.sultoniapk.dawuansnakehead;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.orange));

        recyclerView        = findViewById(R.id.recyclerView);
        progressBar         = findViewById(R.id.progressBar);
        firebaseFirestore   = FirebaseFirestore.getInstance();

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

//        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(linearLayoutManager);

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
        Query query = firebaseFirestore.collection("Produk");
        FirestoreRecyclerOptions<ClassProduk> response = new FirestoreRecyclerOptions.Builder<ClassProduk>()
                .setQuery(query, ClassProduk.class).build();
        adapter = new FirestoreRecyclerAdapter<ClassProduk, ProdukHolder>(response) {
            @NonNull
            @Override
            public ProdukHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produk, parent, false);
                return new ProdukHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull ProdukHolder holder, int position, @NonNull final ClassProduk model) {
                progressBar.setVisibility(View.GONE);
                if( model.getFoto() != null) {
                    Picasso.get().load(model.getFoto()).fit().into(holder.fotoContact);
                }else{
                    Picasso.get().load(ic_user).fit().into(holder.fotoContact);
                }
                holder.namaProduk.setText(model.getNama());
                holder.hargaProduk.setText(model.getHarga());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AdminDashboard.this, ActivityDetailProduk.class);
                        intent.putExtra("nomor", model.getNomor());
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
    public class ProdukHolder extends RecyclerView.ViewHolder{
        ImageView fotoContact;
        TextView namaProduk;
        TextView hargaProduk;
        ConstraintLayout constraintLayout;
        public ProdukHolder(@NonNull View itemView) {
            super(itemView);
            fotoContact = itemView.findViewById(R.id.imageViewFoto);
            namaProduk = itemView.findViewById(R.id.textViewNama);
            hargaProduk = itemView.findViewById(R.id.textViewHarga);
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