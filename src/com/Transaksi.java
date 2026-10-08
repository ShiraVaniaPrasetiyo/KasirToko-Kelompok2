package com;

import java.util.ArrayList;

public class Transaksi {
    private String idTransaksi;
    private String tanggal;
    private ArrayList<Produk> listBelanja;
    private ArrayList<String> detailUkuran;
    private ArrayList<Integer> jumlahBeli;
    private Member member;
    private double totalBayar;
    
    private String metodePembayaran;
    private double uangDibayar;
    private double kembalian;

    public Transaksi(String idTransaksi, String tanggal, Member member) {
        this.idTransaksi = idTransaksi;
        this.tanggal = tanggal;
        this.member = member;
        this.listBelanja = new ArrayList<>();
        this.detailUkuran = new ArrayList<>();
        this.jumlahBeli = new ArrayList<>();
        this.totalBayar = 0;
    }

    public void addProduk(Produk p, String ukuran, int jumlah) {
        listBelanja.add(p);
        detailUkuran.add(ukuran);
        jumlahBeli.add(jumlah);
    }

    public ArrayList<Produk> getListBelanja() { return listBelanja; }

    public double hitungTotal() {
        double total = 0;
        for (int i = 0; i < listBelanja.size(); i++) {
            total += (listBelanja.get(i).getHarga() * jumlahBeli.get(i));
        }
        if (member != null) {
            double diskon = member.hitungDiskon(total);
            total -= diskon;
        }
        this.totalBayar = total;
        return total;
    }

    public void prosesPembayaran(String metode, double uang) {
        this.metodePembayaran = metode;
        this.uangDibayar = uang;
        this.kembalian = uang - totalBayar;
    }

    public double getTotalBayar() { return totalBayar; }
    public double getKembalian() { return kembalian; }

    private int hitungTotalQty() {
        int total = 0;
        for (int qty : jumlahBeli) {
            total += qty;
        }
        return total;
    }

    public void cetakStruk() {
        System.out.println("========================================");
        System.out.println("       TOKO FASHION KELOMPOK C          ");
        System.out.println("   Jl. Contoh No. 123, Jakarta Barat    ");
        System.out.println("   NPWP: 12.345.678.9-012.000           ");
        System.out.println("========================================");
        System.out.printf("Receipt No : %s%n", idTransaksi);
        System.out.printf("Date       : %s%n", tanggal);
        if(member != null) {
            System.out.printf("Member     : %s (%s)%n", member.getNama(), member.getIdMember());
        } else {
            System.out.println("Member     : Non-Member");
        }
        System.out.println("----------------------------------------");
        
        System.out.println("No  Item                          Amount");
        System.out.println("----------------------------------------");
        
        int no = 1;
        for (int i = 0; i < listBelanja.size(); i++) {
            Produk p = listBelanja.get(i);
            String ukuran = detailUkuran.get(i);
            int qty = jumlahBeli.get(i);
            String namaBarang = p.getNama();
            if (!ukuran.equals("-")) namaBarang += " (" + ukuran + ")";
            
            System.out.printf("%-3d %-30s%n", no, namaBarang);
            System.out.printf("    %-3d x %-15.0f %15.0f%n", qty, p.getHarga(), (qty * p.getHarga()));
            no++;
        }
        System.out.println("----------------------------------------");
        
        double subTotal = 0;
        for (int i = 0; i < listBelanja.size(); i++) {
            subTotal += (listBelanja.get(i).getHarga() * jumlahBeli.get(i));
        }
        double diskon = (member != null) ? member.hitungDiskon(subTotal) : 0;
        
        System.out.printf("Total Qty  : %-5d%n", hitungTotalQty());
        System.out.printf("SubTotal   : %25.0f%n", subTotal);
        System.out.printf("Discount   : %25.0f%n", diskon);
        System.out.printf("Total      : %25.0f%n", totalBayar);
        System.out.println("----------------------------------------");
        System.out.printf("Payment    : %-15s %10.0f%n", metodePembayaran, uangDibayar);
        System.out.printf("Change     : %25.0f%n", kembalian);
        System.out.println("========================================");
        System.out.println("           THANK YOU                    ");
        System.out.println("      KEBIJAKAN PENUKARAN               ");
        System.out.println(" Hanya berlaku untuk barang harga normal");
        System.out.println(" Kondisi barang masih bagus & kemasan asli");
        System.out.println(" Melampirkan tanda terima pembelian     ");
        System.out.println(" Berlaku 7 hari dari tanggal pembelian  ");
        System.out.println("========================================");
    }
    
    public String toFileString() {
        String namaMember = (member != null) ? member.getNama() : "Umum";
        return idTransaksi + "," + tanggal + "," + namaMember + "," + totalBayar + "," + metodePembayaran;
    }
}