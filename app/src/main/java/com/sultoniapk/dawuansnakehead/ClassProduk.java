package com.sultoniapk.dawuansnakehead;

public class ClassProduk {
    private String nama, nomor, harga, deskripsi, foto;

    public ClassProduk() {
    }

    public ClassProduk(String nama, String nomor, String harga, String deskripsi, String foto) {
        this.nama = nama;
        this.nomor = nomor;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.foto = foto;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
