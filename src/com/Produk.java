package com;

public abstract class Produk {
    private String id;
    private String nama;
    private double harga;
    private int stok;

    public Produk(String id, String nama, double harga, int stok) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }

    public String getId() { return id; }
    public String getNama() { return nama; }
    public double getHarga() { return harga; }
    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }

    public abstract String getKategori();
}