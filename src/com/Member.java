package com;

public class Member extends Person implements Diskon {
    private String idMember;
    private double poin;

    public Member(String nama, String noHP, String idMember) {
        super(nama, noHP);
        this.idMember = idMember;
        this.poin = 0;
    }

    @Override
    public double hitungDiskon(double totalHarga) {
        return totalHarga * 0.10; // Diskon 10%
    }

    public void tambahPoin(double totalBayar) {
        this.poin = this.poin + (totalBayar * 0.01);
    }

    public String getIdMember() { return idMember; }

    public double getPoin() { return poin; }
}