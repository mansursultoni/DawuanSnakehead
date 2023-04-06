package com.sultoniapk.dawuansnakehead;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ActivityDetailProduk extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    ImageView FotoProduk;
    EditText TextNama, TextNomor, TextHarga, TextDeskripsi;
    Button TombolEdit, TombolHapus, TombolKembali;
    ProgressBar progressBar;
    private Uri filePath;
    private String fotoUrl, produkId;
    private static final int IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);
        FotoProduk      = findViewById(R.id.imageView);
        TextNama        = findViewById(R.id.editTextNama);
        TextNomor       = findViewById(R.id.editTextNomor);
        TextHarga       = findViewById(R.id.editTextHarga);
        TextDeskripsi   = findViewById(R.id.editTextDeskripsi);
        TombolHapus     = findViewById(R.id.buttonDelete);
        TombolEdit      = findViewById(R.id.buttonUpdate);
        TombolKembali   = findViewById(R.id.buttonBack);
        progressBar     = findViewById(R.id.progressBar);
        progressBar.setVisibility(INVISIBLE);
        firebaseFirestore   = FirebaseFirestore.getInstance();
        storageReference    = FirebaseStorage.getInstance().getReference();
        produkId = getIntent().getExtras().getString("nomor");
        readData();
        FotoProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ambilGambar();
            }
        });
        TombolEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        TombolHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusData();
            }
        });
        TombolKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void readData(){
        firebaseFirestore.collection("Produk").whereEqualTo("nomor", produkId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                TextNama.setText(document.getString("nama"));
                                TextNomor.setText(document.getString("nomor"));
                                TextHarga.setText(document.getString("harga"));
                                TextDeskripsi.setText(document.getString("deskripsi"));
                                fotoUrl = document.getString("foto");
                                if (fotoUrl != ""){
                                    Picasso.get().load(fotoUrl).fit().into(FotoProduk);
                                }else{
                                    Picasso.get().load(R.drawable.ic_user).fit().into(FotoProduk);
                                }
                            }
                        }else{
                            Toast.makeText(ActivityDetailProduk.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void SimpanData(String nama, String nomor, String harga, String deskripsi, String foto){
        Map<String, Object> contactData = new HashMap<>();
        contactData.put("nama", nama);
        contactData.put("nomor", nomor);
        contactData.put("harga", harga);
        contactData.put("deskripsi", deskripsi);
        contactData.put("foto", foto);
        firebaseFirestore.collection("Produk").document(nomor).set(contactData).isSuccessful();
    }
    private void ambilGambar(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"),IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null ){
            filePath = data.getData();
            Picasso.get().load(filePath).fit().into(FotoProduk);
        }else{
            Toast.makeText(this, "Tidak ada gambar dipilih", Toast.LENGTH_SHORT).show();
        }
    }
    private void uploadImage(){
        if(filePath != null){
            final StorageReference ref = storageReference.child(TextNomor.getText().toString());
            UploadTask uploadTask = ref.putFile(filePath);
            Task<Uri> uriTask = uploadTask.continueWithTask(task -> {
                return ref.getDownloadUrl();
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri imagePath = task.getResult();
                    fotoUrl = imagePath.toString();
                    SimpanData(TextNama.getText().toString(),
                            TextNomor.getText().toString(),
                            TextHarga.getText().toString(),
                            TextDeskripsi.getText().toString(),
                            fotoUrl);
                    progressBar.setProgress(0);
                    progressBar.setVisibility(INVISIBLE);
                    Toast.makeText(ActivityDetailProduk.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(VISIBLE);
                    double progres = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int)progres);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(INVISIBLE);
                    Toast.makeText(ActivityDetailProduk.this, "Failed " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }else{
            SimpanData(TextNama.getText().toString(),
                    TextNomor.getText().toString(),
                    TextHarga.getText().toString(),
                    TextDeskripsi.getText().toString(),
                    fotoUrl);
            Toast.makeText(this, "Produk telah diubah", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private void hapusData(){
        firebaseFirestore.collection("Produk").document(produkId).delete();
        storageReference.child(produkId).delete();
        Toast.makeText(this, "Produk telah dihapus", Toast.LENGTH_SHORT).show();
        finish();

    }
}