package com.sultoniapk.dawuansnakehead;

public class HelperClass {
    public HelperClass() {
    }

    String nama, username, telepon;

    public HelperClass(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public HelperClass(String nama, String username, String telepon) {
        this.nama       = nama;
        this.username   = username;
        this.telepon    = telepon;
    }
}
