package com.sultoniapk.dawuansnakehead;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ActivityTambahProduk extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    ImageView FotoProduk;
    EditText TextNama, TextNomor, TextHarga, TextDeskripsi;
    Button TombolSimpan, TombolKembali;
    ProgressBar progressBar;
    private Uri filePath;
    private String fotoUrl;
    private static final int IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_produk);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.orange));

        FotoProduk      = findViewById(R.id.imageView);

        TextNama        = findViewById(R.id.editTextNama);
        TextNomor       = findViewById(R.id.editTextNomor);
        TextHarga       = findViewById(R.id.editTextHarga);
        TextDeskripsi   = findViewById(R.id.editTextDeskripsi);
        TombolKembali   = findViewById(R.id.buttonBack);
        TombolSimpan    = findViewById(R.id.buttonUpdate);

        progressBar     = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        firebaseFirestore   = FirebaseFirestore.getInstance();
        storageReference    = FirebaseStorage.getInstance().getReference();
        FotoProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ambilGambar();
            }
        });
        TombolSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        TombolKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
            Picasso.get().load(filePath).fit().centerInside().into(FotoProduk);
        }else{
            Toast.makeText(this, "Tidak ada gambar dipilih", Toast.LENGTH_SHORT).show();
        }
    }
    private void uploadImage(){
        if(filePath != null){
            final StorageReference ref = storageReference.child(TextNomor.getText().toString());
            UploadTask uploadTask = ref.putFile(filePath);
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    return ref.getDownloadUrl();
                }
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
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(ActivityTambahProduk.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.VISIBLE);
                    double progres = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int)progres);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(ActivityTambahProduk.this, "Failed " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}