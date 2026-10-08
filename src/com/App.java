package com;

import java.io.*;
import java.util.*;

public class App {
    static ArrayList<Produk> daftarProduk = new ArrayList<>();
    static ArrayList<Member> daftarMember = new ArrayList<>(); 
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        // ============ BACA DATA DARI FILE (BUKAN HARDCODE!) ============
        System.out.println(">> Memuat data dari database file...");
        daftarProduk = FileHandler.bacaProduk();
        FileHandler.bacaStokPakaian(daftarProduk);
        daftarMember = FileHandler.bacaMember();
        
        if (daftarProduk.isEmpty()) {
            System.out.println(">> File produk kosong. Tambahkan produk via menu 3.");
        } else {
            System.out.println(">> " + daftarProduk.size() + " produk berhasil dimuat.");
        }
        if (daftarMember.isEmpty()) {
            System.out.println(">> File member kosong. Daftarkan member saat transaksi.");
        } else {
            System.out.println(">> " + daftarMember.size() + " member berhasil dimuat.");
        }

        boolean running = true;
        while (running) {
            System.out.println("\n=== MENU UTAMA TOKO FASHION KELOMPOK C ===");
            System.out.println("1. Lihat Produk");
            System.out.println("2. Transaksi Kasir");
            System.out.println("3. Manajemen Produk (Tambah Barang/Stok)");
            System.out.println("4. Keluar & Simpan");
            System.out.print("Pilih menu: ");
            
            try {
                int pilihan = input.nextInt();
                input.nextLine(); 

                switch (pilihan) {
                    case 1: tampilkanProduk(); break;
                    case 2: menuTransaksi(); break;
                    case 3: menuManajemenProduk(); break; 
                    case 4: 
                        // SIMPAN SEMUA DATA KE FILE SEBELUM KELUAR
                        FileHandler.simpanProduk(daftarProduk);
                        FileHandler.simpanStokPakaian(daftarProduk);
                        FileHandler.simpanMember(daftarMember);
                        running = false; 
                        System.out.println(">> Data berhasil disimpan. Terima kasih!"); 
                        break;
                    default: System.out.println("Pilihan tidak valid.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Input harus berupa angka!");
                input.nextLine(); 
            }
        }
    }

    static void tampilkanProduk() {
        System.out.println("\n--- DAFTAR PRODUK ---");
        for (Produk p : daftarProduk) {
            String stokInfo = (p instanceof Pakaian) ? ((Pakaian) p).getStokPerUkuran().toString() : String.valueOf(p.getStok());
            System.out.printf("%s | %s | %s | Rp%.0f | Stok: %s%n", 
                p.getId(), p.getNama(), p.getKategori(), p.getHarga(), stokInfo);
        }
    }

    static Produk cariProduk(String id) {
        for (Produk p : daftarProduk) {
            if (p.getId().equalsIgnoreCase(id)) return p;
        }
        return null;
    }

    static Member cariMember(String id) {
        for (Member m : daftarMember) {
            if (m.getIdMember().equalsIgnoreCase(id)) return m;
        }
        return null;
    }

    // ============ MANAJEMEN PRODUK (TAMBAH BARANG/STOK) ============
    static void menuManajemenProduk() {
        System.out.println("\n--- MANAJEMEN PRODUK ---");
        System.out.println("1. Tambah Produk Baru");
        System.out.println("2. Tambah Stok Produk yang Ada");
        System.out.print("Pilih (1/2): ");
        String pilih = input.nextLine();

        if (pilih.equals("1")) {
            System.out.print("Jenis Produk (1. Pakaian / 2. Aksesoris): ");
            String jenis = input.nextLine();
            System.out.print("ID Produk: "); String id = input.nextLine();
            System.out.print("Nama Produk: "); String nama = input.nextLine();
            System.out.print("Harga: "); double harga = Double.parseDouble(input.nextLine());

            if (jenis.equals("1")) {
                System.out.print("Bahan: "); String bahan = input.nextLine();
                Pakaian p = new Pakaian(id, nama, harga, bahan);
                System.out.print("Stok Ukuran S: "); p.tambahStok("S", Integer.parseInt(input.nextLine()));
                System.out.print("Stok Ukuran M: "); p.tambahStok("M", Integer.parseInt(input.nextLine()));
                System.out.print("Stok Ukuran L: "); p.tambahStok("L", Integer.parseInt(input.nextLine()));
                daftarProduk.add(p);
            } else {
                System.out.print("Jenis Aksesoris: "); String jns = input.nextLine();
                System.out.print("Stok Awal: "); int stok = Integer.parseInt(input.nextLine());
                daftarProduk.add(new Aksesoris(id, nama, harga, stok, jns));
            }
            FileHandler.simpanProduk(daftarProduk);
            FileHandler.simpanStokPakaian(daftarProduk);
            System.out.println(">> Produk berhasil ditambahkan dan disimpan ke file!");

        } else if (pilih.equals("2")) {
            tampilkanProduk();
            System.out.print("Masukkan ID Produk: ");
            String idCari = input.nextLine();
            Produk found = cariProduk(idCari);
            if (found != null) {
                if (found instanceof Pakaian) {
                    Pakaian pk = (Pakaian) found;
                    System.out.print("Tambah stok ukuran (S/M/L/XL): "); String uk = input.nextLine().toUpperCase();
                    System.out.print("Jumlah tambahan: "); int jml = Integer.parseInt(input.nextLine());
                    pk.tambahStok(uk, jml);
                } else {
                    System.out.print("Jumlah tambahan stok: "); int jml = Integer.parseInt(input.nextLine());
                    found.setStok(found.getStok() + jml);
                }
                FileHandler.simpanProduk(daftarProduk);
                FileHandler.simpanStokPakaian(daftarProduk);
                System.out.println(">> Stok berhasil ditambahkan dan disimpan ke file!");
            } else {
                System.out.println("Produk tidak ditemukan.");
            }
        }
    }

    // ============ TRANSAKSI KASIR ============
    static void menuTransaksi() {
        String idTrx = "TRX-" + System.currentTimeMillis();
        String tanggal = new Date().toString();
        Member member = null;

        System.out.print("Apakah Anda Member? (y/n): ");
        String isMember = input.nextLine();

        if (isMember.equalsIgnoreCase("y")) {
            System.out.print("Masukkan ID Member (Contoh: M001): ");
            String idInput = input.nextLine();
            
            member = cariMember(idInput);

            if (member != null) {
                System.out.println(">> Selamat datang kembali, " + member.getNama() + "!");
                System.out.println(">> Poin Anda saat ini: " + member.getPoin());
            } else {
                System.out.println(">> ID Member tidak ditemukan.");
                System.out.print(">> Daftar sebagai member baru? (y/n): ");
                String daftarBaru = input.nextLine();
                
                if (daftarBaru.equalsIgnoreCase("y")) {
                    System.out.print("Masukkan Nama Anda: ");
                    String namaBaru = input.nextLine();
                    System.out.print("Masukkan No HP: ");
                    String hpBaru = input.nextLine();
                    
                    member = new Member(namaBaru, hpBaru, idInput);
                    daftarMember.add(member);
                    FileHandler.simpanMember(daftarMember);
                    System.out.println(">> Member baru berhasil didaftarkan dan disimpan ke file!");
                } else {
                    System.out.println(">> Melanjutkan sebagai Non-Member.");
                }
            }
        }

        Transaksi transaksi = new Transaksi(idTrx, tanggal, member);
        tampilkanProduk();

        boolean belanja = true;
        while (belanja) {
            System.out.println("\n--- INPUT KERANJANG ---");
            System.out.println("Format: [ID_PRODUK] [JUMLAH]  (Contoh: P001 2)");
            System.out.println("Ketik 'selesai' jika sudah selesai belanja.");
            System.out.print("Input: ");
            
            String inputBelanja = input.nextLine();

            if (inputBelanja.equalsIgnoreCase("selesai")) {
                belanja = false;
            } else {
                String[] parts = inputBelanja.split(" ");
                
                if (parts.length < 2) {
                    System.out.println("Format salah! Gunakan: [ID] [Jumlah]. Contoh: P001 2");
                    continue;
                }

                String idBeli = parts[0];
                int jumlahBeli;

                try {
                    jumlahBeli = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Jumlah harus berupa angka!");
                    continue;
                }

                Produk found = cariProduk(idBeli);

                if (found != null) {
                    String ukuranDipilih = "-";

                    if (found instanceof Pakaian) {
                        Pakaian pk = (Pakaian) found;
                        System.out.print("Pilih Ukuran untuk " + found.getNama() + " (S/M/L/XL): ");
                        ukuranDipilih = input.nextLine().toUpperCase();
                        
                        if (pk.getStokUkuran(ukuranDipilih) <= 0) {
                            System.out.println("Maaf, ukuran " + ukuranDipilih + " habis.");
                            continue;
                        }
                    }

                    if (found instanceof Pakaian) {
                        Pakaian pk = (Pakaian) found;
                        if (pk.getStokUkuran(ukuranDipilih) < jumlahBeli) {
                            System.out.println("Stok tidak cukup! Sisa: " + pk.getStokUkuran(ukuranDipilih));
                            continue;
                        }
                        pk.kurangiStok(ukuranDipilih, jumlahBeli); 
                    } else {
                        if (found.getStok() < jumlahBeli) {
                            System.out.println("Stok tidak cukup! Sisa: " + found.getStok());
                            continue;
                        }
                        found.setStok(found.getStok() - jumlahBeli); 
                    }

                    transaksi.addProduk(found, ukuranDipilih, jumlahBeli);
                    System.out.println(">> " + found.getNama() + " (" + ukuranDipilih + ") x" + jumlahBeli + " ditambahkan ke keranjang.");
                } else {
                    System.out.println("Produk dengan ID " + idBeli + " tidak ditemukan.");
                }
            }
        }

        if (!transaksi.getListBelanja().isEmpty()) { 
            double total = transaksi.hitungTotal();
            System.out.printf("\nTOTAL YANG HARUS DIBAYAR: Rp %.0f\n", total);
            
            System.out.println("Pilih Metode Pembayaran:");
            System.out.println("1. Tunai (Cash)");
            System.out.println("2. QRIS");
            System.out.println("3. Kartu Debit/Kredit");
            System.out.print("Pilihan (1/2/3): ");
            String pilihanBayar = input.nextLine();
            
            String metode = "Tunai";
            double uangBayar = total;

            if (pilihanBayar.equals("1")) {
                metode = "Tunai";
                System.out.print("Masukkan jumlah uang tunai: ");
                try {
                    uangBayar = Double.parseDouble(input.nextLine());
                    if (uangBayar < total) {
                        System.out.println("Uang tidak cukup! Transaksi dibatalkan.");
                        return; 
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Input uang tidak valid. Transaksi dibatalkan.");
                    return;
                }
            } else if (pilihanBayar.equals("2")) {
                metode = "QRIS";
                System.out.println("Silakan scan QRIS... (Tekan Enter untuk konfirmasi lunas)");
                input.nextLine();
            } else if (pilihanBayar.equals("3")) {
                metode = "Kartu";
                System.out.println("Silakan gesek/tap kartu... (Tekan Enter untuk konfirmasi lunas)");
                input.nextLine();
            }

            transaksi.prosesPembayaran(metode, uangBayar);
            transaksi.cetakStruk();
            
            if (member != null) {
                member.tambahPoin(total);
                FileHandler.simpanMember(daftarMember);
                System.out.println(">> Poin Anda bertambah! Total Poin sekarang: " + member.getPoin());
            }
            
            // SIMPAN PERUBAHAN STOK KE FILE
            FileHandler.simpanProduk(daftarProduk);
            FileHandler.simpanStokPakaian(daftarProduk);
            
            simpanRiwayatTransaksi(transaksi); 
        } else {
            System.out.println("Tidak ada barang yang dibeli.");
        }
    }

    static void simpanRiwayatTransaksi(Transaksi t) {
        try {
            FileWriter writer = new FileWriter("riwayat_transaksi.txt", true);
            writer.write(t.toFileString() + "\n");
            writer.close();
            System.out.println(">> Riwayat transaksi berhasil disimpan ke database.");
        } catch (IOException e) {
            System.out.println("Gagal menyimpan riwayat: " + e.getMessage());
        }
    }
}