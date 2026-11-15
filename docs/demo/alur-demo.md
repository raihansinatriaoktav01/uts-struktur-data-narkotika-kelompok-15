# Judul: Panduan Skenario Demo Aplikasi

Dokumen ini berisi panduan urutan pengujian aplikasi Sistem Data Putusan Narkotika saat presentasi UTS. Semua skenario ini dibuat untuk membuktikan kalau struktur data (Dynamic Array buatan sendiri) dan fitur yang diminta di soal benar-benar jalan dengan baik.

## Skenario 1: Memuat 50 Data Utama

Cara tes: Buka aplikasi, klik tombol **Impor CSV**, lalu pilih file `DataPutusan50` dari folder data.

Yang diharapkan: Muncul dialog "Hasil Impor CSV" dengan isi Diterima: 50, Ditolak: 0, dan Total di struktur: 50. Tabel terisi dari P001 sampai P050. Indikator di header harus menunjukkan Total Data 50, Ditampilkan 50, dan Invariant "Valid".

Screenshot bukti: **(img/bukti-impor-50-data.png)**

## Skenario 2: Menguji Fitur Pencarian di Struktur

Cara tes: Di panel "Cari di Struktur", pilih mode "Case ID" atau "Nomor Perkara", ketik kata kunci, lalu tekan Enter atau klik **Cari di Struktur**:

- Case ID atau nomor perkara yang posisinya ada di tengah tabel.
- Nilai asal-asalan yang pasti nggak ada (misal: "XYZ-999").
- Data yang ada di urutan paling atas (P001) atau paling bawah (P050).

Yang diharapkan: Data yang ada langsung ditemukan dan ditampilkan dengan pas. Nilai yang nggak ada menghasilkan pesan tidak ditemukan tanpa error.

Screenshot bukti: **(img/bukti-uji-fitur-pencarian.png)**

## Skenario 3: Penambahan Data Baru dan Cek Invariant

Cara tes: Klik tombol **Tambah**, isi form putusan baru, lalu simpan.

Yang diharapkan: Muncul pesan "Putusan berhasil ditambahkan." Indikator Total Data dan Ditampilkan naik jadi 51, dan Invariant tetap "Valid". Ini membuktikan array dinamis bisa menampung elemen baru dengan benar.

Screenshot bukti: **(img/bukti-tambah-data.png)**

## Skenario 4: Penghapusan dan Pergeseran Indeks

Cara tes: Pilih satu baris data di tabel lalu klik **Hapus**. Lakukan hal yang sama untuk data di posisi tengah dan data paling bawah.

Yang diharapkan: Muncul pesan "Putusan berhasil dihapus." Indikator total data berkurang (dari 51 kembali ke 50), tabel merapat tanpa baris kosong atau error, dan Invariant tetap "Valid". Ini membuktikan fungsi geser elemen di DynamicArray sukses bekerja.

Screenshot bukti: **(img/bukti-penghapusan-dan-pergeseran-indeks.png)**

## Skenario 5: Melihat Detail Putusan

Cara tes: Pilih baris P001 di tabel, lalu klik tombol **Detail**.

Yang diharapkan: Dialog "Detail Putusan" menampilkan data lengkap, yaitu Case ID P001, Nomor Perkara 181/Pid.Sus/2025/PN Gns, Terdakwa Kode YB, Jenis Narkotika Metamfetamina (Sabu) dan MDMA (Ekstasi), Berat Barang Bukti 14.916 gram, Pasal 114 Ayat (2) UU No. 35 Tahun 2009, Tuntutan 102 bulan, dan Vonis 90 bulan.

Screenshot bukti: **(img/bukti-detail-putusan.png)**

## Skenario 6: Menguji Filter Gabungan

Cara tes: Isi kriteria di panel "Filter Gabungan" lalu klik **Terapkan**. Coba satu per satu:

- Jenis narkotika: `ganja`
- Pasal terbukti: `114`
- Berat barang bukti: Min `0.5`, Maks `13`
- Vonis (bulan): Min `48`, Maks `60`

Yang diharapkan: Angka "Ditampilkan" berubah sesuai hasil saring, yaitu 6 data untuk ganja, 19 data untuk Pasal 114, 13 data untuk berat 0.5 sampai 13 gram, dan 17 data untuk vonis 48 sampai 60 bulan. Total Data tetap 50 karena data asli tidak berubah. Teks status di bawah menampilkan "Filter: aktif, tampil N". Klik **Reset** untuk mengembalikan semua data.

Screenshot bukti: **(img/bukti-filter-gabungan.png)**

## Skenario 7: Pengurutan Tampilan Berdasarkan Vonis

Cara tes: Setelah filter vonis 48 sampai 60 bulan aktif (17 data), klik tombol **Naik**, lalu **Turun**, lalu **Asli** di panel "Urutan Tampilan".

Yang diharapkan: Mode Naik mengurutkan vonis dari 48 ke 60 dan status bar menampilkan "Urutan: Vonis naik (71 perbandingan)". Mode Turun mengurutkan dari 60 ke 48 dengan "Urutan: Vonis turun (52 perbandingan)". Mode Asli mengembalikan urutan input semula. Pengurutan hanya mengubah tampilan, bukan data di struktur.

Screenshot bukti: **(img/bukti-pengurutan-naik.png)** dan **(img/bukti-pengurutan-turun.png)**

## Skenario 8: Ringkasan Agregasi

Cara tes: Klik tombol **Agregasi** (tanpa filter aktif).

Yang diharapkan: Dialog "Ringkasan Agregasi" menampilkan cakupan seluruh data (50 putusan) dengan Jumlah Perkara 50, Rata-rata Vonis 58,2 bulan, dan Total Denda Rp 39.000.000.000. Distribusi jenis narkotika: Metamfetamina (Sabu) 41 putusan (82%), MDMA (Ekstasi) 6 putusan (12%), Ganja 6 putusan (12%), dan Tembakau Sintetis 3 putusan (6%). Total persentase bisa lebih dari 100% karena perkara dengan lebih dari satu zat dihitung pada tiap zat.

Screenshot bukti: **(img/bukti-agregasi.png)**

## Skenario 9: Audit Aktivitas

Cara tes: Klik tombol **Audit** setelah beberapa operasi dilakukan (impor, tambah, hapus).

Yang diharapkan: Jendela "Audit Aktivitas" menampilkan log berisi kolom No, Waktu, dan Aktivitas, misalnya "Menambahkan putusan: 181/Pid.Sus/2025/PN Gns". Semua operasi tercatat berurutan sehingga ada jejak perubahan data. Tombol **Ekspor Audit CSV** bisa dipakai untuk menyimpan log.

Screenshot bukti: **(img/bukti-audit-aktivitas.png)**

## Skenario 10: Ekspor Data ke CSV

Cara tes: Klik tombol **Ekspor CSV**, lalu simpan file hasilnya.

Yang diharapkan: File CSV berhasil dibuat dan isinya sesuai dengan data yang ada di struktur saat itu.

Screenshot bukti: **(img/bukti-ekspor-csv.png)**