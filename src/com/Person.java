package com;

public abstract class Person {
    private String nama;
    private String noHP;

    public Person(String nama, String noHP) {
        this.nama = nama;
        this.noHP = noHP;
    }

    public String getNama() { return nama; }
    public String getNoHP() { return noHP; }
}