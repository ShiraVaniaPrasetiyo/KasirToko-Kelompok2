package com;

import java.io.*;
import java.util.*;

public class FileHandler {

    // ================== BACA PRODUK DARI FILE ==================
    public static ArrayList<Produk> bacaProduk() {
        ArrayList<Produk> daftarProduk = new ArrayList<>();
        try {
            File file = new File("produk.txt");
            if (!file.exists()) {
                System.out.println(">> File produk.txt tidak ditemukan.");
                return daftarProduk;
            }
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if (data[0].equalsIgnoreCase("Pakaian")) {
                    Pakaian p = new Pakaian(data[1], data[2], Double.parseDouble(data[3]), data[4]);
                    daftarProduk.add(p);
                } else if (data[0].equalsIgnoreCase("Aksesoris")) {
                    Aksesoris a = new Aksesoris(data[1], data[2], Double.parseDouble(data[3]), 0, data[4]);
                    daftarProduk.add(a);
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error baca produk: " + e.getMessage());
        }
        return daftarProduk;
    }

    // ================== BACA STOK PAKAIAN ==================
    public static void bacaStokPakaian(ArrayList<Produk> daftarProduk) {
        try {
            File file = new File("stok_pakaian.txt");
            if (!file.exists()) return;
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                String idProduk = data[0];
                String ukuran = data[1];
                int jumlah = Integer.parseInt(data[2]);
                for (Produk p : daftarProduk) {
                    if (p.getId().equals(idProduk) && p instanceof Pakaian) {
                        ((Pakaian) p).tambahStok(ukuran, jumlah);
                    }
                }
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error baca stok: " + e.getMessage());
        }
    }

    // ================== SIMPAN PRODUK KE FILE ==================
    public static void simpanProduk(ArrayList<Produk> daftarProduk) {
        try {
            FileWriter writer = new FileWriter("produk.txt");
            for (Produk p : daftarProduk) {
                if (p instanceof Pakaian) {
                    writer.write("Pakaian," + p.getId() + "," + p.getNama() + "," + p.getHarga() + "," + ((Pakaian) p).getBahan() + "\n");
                } else if (p instanceof Aksesoris) {
                    writer.write("Aksesoris," + p.getId() + "," + p.getNama() + "," + p.getHarga() + "," + ((Aksesoris) p).getJenis() + "\n");
                }
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error simpan produk: " + e.getMessage());
        }
    }

    // ================== SIMPAN STOK PAKAIAN ==================
    public static void simpanStokPakaian(ArrayList<Produk> daftarProduk) {
        try {
            FileWriter writer = new FileWriter("stok_pakaian.txt");
            for (Produk p : daftarProduk) {
                if (p instanceof Pakaian) {
                    Pakaian pk = (Pakaian) p;
                    for (Map.Entry<String, Integer> entry : pk.getStokPerUkuran().entrySet()) {
                        writer.write(pk.getId() + "," + entry.getKey() + "," + entry.getValue() + "\n");
                    }
                }
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error simpan stok: " + e.getMessage());
        }
    }

    // ================== BACA MEMBER DARI FILE ==================
    public static ArrayList<Member> bacaMember() {
        ArrayList<Member> daftarMember = new ArrayList<>();
        try {
            File file = new File("member.txt");
            if (!file.exists()) {
                System.out.println(">> File member.txt tidak ditemukan.");
                return daftarMember;
            }
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                Member m = new Member(data[1], data[2], data[0]);
                m.tambahPoin(Double.parseDouble(data[3]));
                daftarMember.add(m);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error baca member: " + e.getMessage());
        }
        return daftarMember;
    }

    // ================== SIMPAN MEMBER KE FILE ==================
    public static void simpanMember(ArrayList<Member> daftarMember) {
        try {
            FileWriter writer = new FileWriter("member.txt");
            for (Member m : daftarMember) {
                writer.write(m.getIdMember() + "," + m.getNama() + "," + m.getNoHP() + "," + m.getPoin() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error simpan member: " + e.getMessage());
        }
    }
}