package com;

import java.util.HashMap;
import java.util.Map;

public class Pakaian extends Produk {
    private String bahan;
    private Map<String, Integer> stokPerUkuran;

    public Pakaian(String id, String nama, double harga, String bahan) {
        super(id, nama, harga, 0); 
        this.bahan = bahan;
        this.stokPerUkuran = new HashMap<>();
    }

    public void tambahStok(String ukuran, int jumlah) {
        stokPerUkuran.put(ukuran, stokPerUkuran.getOrDefault(ukuran, 0) + jumlah);
    }

    public int getStokUkuran(String ukuran) {
        return stokPerUkuran.getOrDefault(ukuran, 0);
    }

    public void kurangiStok(String ukuran, int jumlah) {
        if (stokPerUkuran.containsKey(ukuran)) {
            stokPerUkuran.put(ukuran, stokPerUkuran.get(ukuran) - jumlah);
        }
    }

    public Map<String, Integer> getStokPerUkuran() {
        return stokPerUkuran;
    }

    public String getBahan() { return bahan; }

    @Override
    public String getKategori() {
        return "Pakaian (" + bahan + ")";
    }
    
    @Override
    public int getStok() {
        int total = 0;
        for (int stok : stokPerUkuran.values()) {
            total += stok;
        }
        return total;
    }
}