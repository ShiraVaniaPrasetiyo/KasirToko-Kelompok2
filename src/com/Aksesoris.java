package com;

public class Aksesoris extends Produk {
    private String jenis;

    public Aksesoris(String id, String nama, double harga, int stok, String jenis) {
        super(id, nama, harga, stok);
        this.jenis = jenis;
    }

    public String getJenis() { return jenis; }

    @Override
    public String getKategori() {
        return "Aksesoris (" + jenis + ")";
    }
}