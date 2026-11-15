package controller;

import model.Putusan;
import struktur.DynamicArray;

public class StatistikController {

    private PutusanController controller;

    public StatistikController(PutusanController controller) {
        this.controller = controller;
    }

    public void tampilkanJumlahPutusan() {
        System.out.println("Jumlah seluruh putusan: "
                + controller.jumlahPutusan());
    }

    public void hitungRataRataVonis() {
        int jumlahData = controller.jumlahPutusan();

        if (jumlahData == 0) {
            System.out.println("Belum ada data putusan.");
            return;
        }

        double totalVonis = 0;

        for (int i = 0; i < jumlahData; i++) {
            Putusan p = controller.ambilPutusan(i);
            totalVonis += p.getVonisBulan();
        }

        double rataRata = totalVonis / jumlahData;

        System.out.println("Rata-rata vonis: " + rataRata + " bulan");
    }

    public void hitungTotalDenda() {
        long totalDenda = 0;

        for (int i = 0; i < controller.jumlahPutusan(); i++) {
            Putusan p = controller.ambilPutusan(i);
            totalDenda += p.getDendaRupiah();
        }

        System.out.println("Total denda: Rp" + totalDenda);
    }

    public void tampilkanDistribusiNarkotika() {
        String[] jenis = new String[controller.jumlahPutusan()];
        int[] jumlah = new int[controller.jumlahPutusan()];
        int banyakJenis = 0;

        for (int i = 0; i < controller.jumlahPutusan(); i++) {
            Putusan p = controller.ambilPutusan(i);
            String namaJenis = p.getJenisNarkotika();

            if (namaJenis == null || namaJenis.trim().isEmpty()) {
                namaJenis = "Tidak diketahui";
            }

            int posisi = -1;

            for (int j = 0; j < banyakJenis; j++) {
                if (jenis[j].equalsIgnoreCase(namaJenis)) {
                    posisi = j;
                    break;
                }
            }

            if (posisi == -1) {
                jenis[banyakJenis] = namaJenis;
                jumlah[banyakJenis] = 1;
                banyakJenis++;
            } else {
                jumlah[posisi]++;
            }
        }

        System.out.println("\nDistribusi Jenis Narkotika:");

        for (int i = 0; i < banyakJenis; i++) {
            System.out.println(jenis[i] + " : " + jumlah[i] + " putusan");
        }
    }

    public HasilAgregasi hitungAgregasi(DynamicArray<Putusan> sumber) {
        int n = sumber == null ? controller.jumlahPutusan() : sumber.size();

        long totalVonis = 0;
        long totalDenda = 0;
        DynamicArray<HasilAgregasi.Entri> distribusi = new DynamicArray<>();

        for (int i = 0; i < n; i++) {
            Putusan p = sumber == null
                    ? controller.ambilPutusan(i)
                    : sumber.get(i);

            totalVonis += p.getVonisBulan();
            totalDenda += p.getDendaRupiah();
            hitungJenis(p.getJenisNarkotika(), distribusi);
        }

        urutkanDistribusi(distribusi);

        double rataRata = n == 0 ? 0 : (double) totalVonis / n;
        return new HasilAgregasi(n, rataRata, totalDenda, distribusi);
    }

    private void hitungJenis(String teks, DynamicArray<HasilAgregasi.Entri> distribusi) {
        if (teks == null || teks.trim().isEmpty()) {
            catatZat("Tidak diketahui", distribusi);
            return;
        }

        String[] bagian = teks.split(",");

        for (int i = 0; i < bagian.length; i++) {
            String nama = normalisasiZat(bagian[i]);

            if (!nama.isEmpty()) {
                catatZat(nama, distribusi);
            }
        }
    }

    private String normalisasiZat(String zat) {
        String nama = zat.trim();

        if (nama.startsWith("(") && nama.endsWith(")") && nama.length() > 2) {
            nama = nama.substring(1, nama.length() - 1).trim();
        }

        return nama.replaceAll("(?i)shabu", "Sabu");
    }

    private void catatZat(String nama, DynamicArray<HasilAgregasi.Entri> distribusi) {
        for (int i = 0; i < distribusi.size(); i++) {
            if (distribusi.get(i).getNama().equalsIgnoreCase(nama)) {
                distribusi.get(i).tambah();
                return;
            }
        }

        distribusi.add(new HasilAgregasi.Entri(nama));
    }

    private void urutkanDistribusi(DynamicArray<HasilAgregasi.Entri> d) {
        for (int i = 1; i < d.size(); i++) {
            HasilAgregasi.Entri kunci = d.get(i);
            int j = i - 1;

            while (j >= 0 && d.get(j).getJumlah() < kunci.getJumlah()) {
                d.set(j + 1, d.get(j));
                j--;
            }

            d.set(j + 1, kunci);
        }
    }

    public void tampilkanSemuaStatistik() {
        System.out.println("\n===== STATISTIK PUTUSAN =====");

        tampilkanJumlahPutusan();
        hitungRataRataVonis();
        hitungTotalDenda();
        tampilkanDistribusiNarkotika();
    }
}