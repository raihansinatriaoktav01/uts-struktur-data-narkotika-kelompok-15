package controller;

import model.Putusan;
import struktur.DynamicArray;
import struktur.AuditList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PutusanController {

    private static final DateTimeFormatter FORMAT_WAKTU_AUDIT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private DynamicArray<Putusan> daftarPutusan;
    private AuditList<String> riwayat;
    private int langkahUrutTerakhir;

    public PutusanController() {
        daftarPutusan = new DynamicArray<>();
        riwayat = new AuditList<>();
    }

    public void tambahPutusan(Putusan putusan) {
        if (putusan == null) {
            throw new IllegalArgumentException("Data putusan tidak boleh null.");
        }

        daftarPutusan.add(putusan);

        catatAktivitas(
                "Menambahkan putusan: " + putusan.getNomorPerkara()
        );
    }

    public void ubahPutusan(int posisi, Putusan putusanBaru) {
        if (putusanBaru == null) {
            throw new IllegalArgumentException("Data putusan tidak boleh null.");
        }

        Putusan putusanLama = daftarPutusan.get(posisi);
        daftarPutusan.set(posisi, putusanBaru);

        catatAktivitas(
                "Mengubah putusan: "
                        + putusanLama.getNomorPerkara()
                        + " menjadi "
                        + putusanBaru.getNomorPerkara()
        );
    }

    public void hapusPutusan(int posisi) {
        Putusan putusan = daftarPutusan.remove(posisi);

        catatAktivitas(
                "Menghapus putusan: " + putusan.getNomorPerkara()
        );
    }

    public Putusan ambilPutusan(int posisi) {
        return daftarPutusan.get(posisi);
    }

    public void tampilkanSemua() {
        if (daftarPutusan.isEmpty()) {
            System.out.println("Belum ada data putusan.");
            return;
        }

        for (int i = 0; i < daftarPutusan.size(); i++) {
            Putusan p = daftarPutusan.get(i);

            System.out.println("Data ke-" + (i + 1));
            System.out.println("Case ID       : " + p.getCaseId());
            System.out.println("Nomor Perkara : " + p.getNomorPerkara());
            System.out.println("Pengadilan    : " + p.getPengadilan());
            System.out.println("Tanggal       : " + p.getTanggalPutusan());
            System.out.println("Jenis Narkotika: " + p.getJenisNarkotika());
            System.out.println("Vonis         : " + p.getVonisBulan() + " bulan");
            System.out.println("----------------------------------");
        }
    }

    public Putusan cariNomorPerkara(String nomorPerkara) {
        int langkah = 0;

        for (int i = 0; i < daftarPutusan.size(); i++) {
            langkah++;

            Putusan p = daftarPutusan.get(i);

            if (p.getNomorPerkara().equalsIgnoreCase(nomorPerkara)) {
                catatAktivitas("Mencari nomor perkara: " + nomorPerkara);

                System.out.println("Data ditemukan!");
                System.out.println("Jumlah langkah pencarian: " + langkah);

                return p;
            }
        }

        catatAktivitas("Mencari nomor perkara: " + nomorPerkara);

        System.out.println("Data tidak ditemukan.");
        System.out.println("Jumlah langkah pencarian: " + langkah);

        return null;
    }

    public Putusan cariCaseId(String caseId) {
        int langkah = 0;

        for (int i = 0; i < daftarPutusan.size(); i++) {
            langkah++;

            Putusan p = daftarPutusan.get(i);

            if (p.getCaseId().equalsIgnoreCase(caseId)) {
                catatAktivitas("Mencari Case ID: " + caseId);

                System.out.println("Data ditemukan!");
                System.out.println("Jumlah langkah pencarian: " + langkah);

                return p;
            }
        }

        catatAktivitas("Mencari Case ID: " + caseId);

        System.out.println("Data tidak ditemukan.");
        System.out.println("Jumlah langkah pencarian: " + langkah);

        return null;
    }

    public HasilCari cari(String kunci, boolean berdasarkanCaseId) {
        String target = kunci == null ? "" : kunci.trim();
        String jenis = berdasarkanCaseId ? "Case ID" : "Nomor Perkara";
        int langkah = 0;

        for (int i = 0; i < daftarPutusan.size(); i++) {
            langkah++;

            Putusan p = daftarPutusan.get(i);
            String nilai = berdasarkanCaseId
                    ? p.getCaseId()
                    : p.getNomorPerkara();

            if (nilai != null && nilai.equalsIgnoreCase(target)) {
                catatAktivitas("Mencari " + jenis + ": " + target
                        + " (ditemukan, " + langkah + " langkah)");
                return new HasilCari(true, i, langkah, p);
            }
        }

        catatAktivitas("Mencari " + jenis + ": " + target
                + " (tidak ditemukan, " + langkah + " langkah)");
        return new HasilCari(false, -1, langkah, null);
    }

    public int cariPosisiCaseId(String caseId) {
        for (int i = 0; i < daftarPutusan.size(); i++) {
            if (daftarPutusan.get(i).getCaseId().equalsIgnoreCase(caseId)) {
                return i;
            }
        }
        return -1;
    }

    public DynamicArray<Putusan> urutkanBerdasarkanVonis(boolean menaik) {
        return urutkanBerdasarkanVonis(null, menaik);
    }

    public DynamicArray<Putusan> urutkanBerdasarkanVonis(
            DynamicArray<Putusan> sumber, boolean menaik) {

        DynamicArray<Putusan> asal = sumber == null ? daftarPutusan : sumber;
        DynamicArray<Putusan> salinan = new DynamicArray<>();

        for (int i = 0; i < asal.size(); i++) {
            salinan.add(asal.get(i));
        }

        int langkah = 0;

        for (int i = 1; i < salinan.size(); i++) {
            Putusan kunci = salinan.get(i);
            int j = i - 1;

            while (j >= 0) {
                langkah++;

                Putusan lawan = salinan.get(j);
                boolean harusGeser = menaik
                        ? lawan.getVonisBulan() > kunci.getVonisBulan()
                        : lawan.getVonisBulan() < kunci.getVonisBulan();

                if (!harusGeser) {
                    break;
                }

                salinan.set(j + 1, lawan);
                j--;
            }

            salinan.set(j + 1, kunci);
        }

        langkahUrutTerakhir = langkah;
        return salinan;
    }

    public int getLangkahUrutTerakhir() {
        return langkahUrutTerakhir;
    }

    public void catatRiwayat(String aktivitas) {
        catatAktivitas(aktivitas);
    }

    private void catatAktivitas(String aktivitas) {
        String waktu = LocalDateTime.now().format(FORMAT_WAKTU_AUDIT);
        riwayat.add(waktu + " | " + (aktivitas == null ? "" : aktivitas));
    }

    public String ambilRiwayat(int posisi) {
        return riwayat.get(posisi);
    }

    public int jumlahRiwayat() {
        return riwayat.size();
    }

    public int jumlahPutusan() {
        return daftarPutusan.size();
    }

    public void tampilkanRiwayat() {
        riwayat.display();
    }

    public boolean validasiStruktur() {
        return daftarPutusan.validateInvariant()
                && riwayat.validateInvariant();
    }
}
