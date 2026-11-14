package controller;

import model.Putusan;
import struktur.DynamicArray;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class CSVWriter {

    private static final String HEADER =
            "caseId,nomorPerkara,pengadilan,tanggalPutusan,terdakwaKode,"
                    + "jenisNarkotika,beratBarangBuktiGram,pasalDakwaan,pasalTerbukti,"
                    + "tuntutanBulan,vonisBulan,dendaRupiah,barangBukti,"
                    + "keadaanMemberatkan,keadaanMeringankan,sourcePages";

    private CSVWriter() {
    }


    public static int exportCSV(File file, PutusanController controller) throws IOException {
        DynamicArray<Putusan> data = new DynamicArray<>();
        for (int i = 0; i < controller.jumlahPutusan(); i++) {
            data.add(controller.ambilPutusan(i));
        }
        return exportCSV(file, data);
    }

    public static int exportCSV(File file, DynamicArray<Putusan> data) throws IOException {
        int n = data.size();

        try (BufferedWriter w = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            w.write(HEADER);
            w.newLine();

            for (int i = 0; i < n; i++) {
                Putusan p = data.get(i);

                String[] kolom = {
                        p.getCaseId(),
                        p.getNomorPerkara(),
                        p.getPengadilan(),
                        p.getTanggalPutusan() == null ? "" : p.getTanggalPutusan().toString(),
                        p.getTerdakwaKode(),
                        p.getJenisNarkotika(),
                        String.valueOf(p.getBeratBarangBuktiGram()),
                        p.getPasalDakwaan(),
                        p.getPasalTerbukti(),
                        String.valueOf(p.getTuntutanBulan()),
                        String.valueOf(p.getVonisBulan()),
                        String.valueOf(p.getDendaRupiah()),
                        p.getBarangBukti(),
                        p.getKeadaanMemberatkan(),
                        p.getKeadaanMeringankan(),
                        p.getSourcePages()
                };

                StringBuilder baris = new StringBuilder();
                for (int k = 0; k < kolom.length; k++) {
                    if (k > 0) baris.append(',');
                    baris.append(escape(kolom[k]));
                }
                w.write(baris.toString());
                w.newLine();
            }
        }
        return n;
    }

    public static int exportAuditCSV(File file, PutusanController controller)
            throws IOException {
        int n = controller.jumlahRiwayat();
        try (BufferedWriter w = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            w.write("waktu,aktivitas");
            w.newLine();

            for (int i = 0; i < n; i++) {
                String entri = controller.ambilRiwayat(i);
                int pemisah = entri.indexOf(" | ");
                String waktu = pemisah >= 0 ? entri.substring(0, pemisah) : "";
                String aktivitas = pemisah >= 0
                        ? entri.substring(pemisah + 3) : entri;
                w.write(escape(waktu) + "," + escape(aktivitas));
                w.newLine();
            }
        }
        return n;
    }

    private static String escape(String teks) {
        if (teks == null) return "";
        boolean perluKutip = teks.contains(",") || teks.contains("\"")
                || teks.contains("\n") || teks.contains("\r");
        String aman = teks.replace("\"", "\"\"");
        return perluKutip ? "\"" + aman + "\"" : aman;
    }
}
