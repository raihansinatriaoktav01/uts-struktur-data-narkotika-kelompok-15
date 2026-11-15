KONTRIBUSI ANGGOTA KELOMPOK 15

Aplikasi Pengelolaan Data Putusan Narkotika (UTS Struktur Data)

Dokumen ini mencatat pembagian tugas dan kontribusi anggota dalam pengembangan project Aplikasi Pengelolaan Data Putusan Narkotika.

# 1. Identitas Anggota

| Nama | NIM | Akun GitHub |
| --- | --- | --- |
| Risky Dwi Kirana Aditya | 202510370110106 | riski-dwi24 |
# Implementasi

## View – MainFrame.java

- Membuat dan mengembangkan tampilan utama aplikasi menggunakan Java Swing.

- Mengatur layout, tabel data, sidebar, tombol, dan komponen input.

- Mengintegrasikan fitur pencarian, filter, pengurutan, tambah, edit, dan hapus data.

- Membuat tampilan statistik, agregasi, audit log, serta fitur impor dan ekspor CSV.

- Mengatur desain GUI menggunakan tema warna gelap.

## Struktur – AuditList.java

- Mengembangkan struktur data untuk menyimpan riwayat aktivitas aplikasi.

- Mengelola pencatatan aktivitas seperti penambahan, perubahan, dan penghapusan data.

## Struktur – DynamicArray.java

- Mengimplementasikan struktur data Dynamic Array untuk penyimpanan data secara dinamis.

- Menyediakan operasi penambahan, pengambilan, perubahan, dan penghapusan elemen.

## Struktur – StrukturData.java

- Membuat interface sebagai dasar implementasi struktur data.

- Menentukan operasi standar yang digunakan oleh struktur data aplikasi.

## Model – Putusan.java

- Membuat class model untuk merepresentasikan data putusan narkotika.

- Mengelola atribut putusan seperti nomor perkara, pengadilan, terdakwa, jenis narkotika, berat barang bukti, pasal, vonis, dan denda.

- Menyediakan constructor, getter, dan setter untuk pengelolaan data.

# 2. Identitas Anggota

| Nama | NIM | Akun GitHub |
| --- | --- | --- |
| Rayhan Sinatria Oktav | 202510370110077 | raihansinatriaoktav01 |

# Implementasi

## Controller – CSVReader.java

- Menangani proses membaca dan mengambil data dari file CSV untuk dimasukkan ke aplikasi.

## Controller – CSVWriter.java

- Menangani proses penulisan dan ekspor data aplikasi ke dalam file CSV.

## Controller – FilterController.java

- Mengatur proses penyaringan data putusan berdasarkan kriteria yang dipilih pengguna.

## Controller – HasilAgregasi.java

- Menampung hasil pengolahan atau perhitungan agregasi data putusan.

## Controller – HasilCari.java

- Menampung hasil pencarian data putusan yang sesuai dengan kata kunci pengguna.

## Controller – HasilImpor.java

- Menampung informasi hasil proses impor data, termasuk ringkasan keberhasilan atau kegagalan impor.

## Controller – KriteriaFilter.java

- Mendefinisikan kriteria atau parameter yang digunakan dalam proses filter data putusan.

## Controller – PutusanController.java

- Mengatur alur pengelolaan data putusan dan menghubungkan kebutuhan aplikasi dengan proses pada model atau struktur data.

## Controller – StatistikController.java

- Mengolah data putusan untuk menghasilkan informasi statistik yang ditampilkan pada aplikasi.

# Pengujian

- Memeriksa proses pembacaan dan penulisan file CSV.

- Menguji proses pencarian dan penyaringan data berdasarkan kriteria.

- Memeriksa hasil impor, agregasi, dan statistik.

- Menguji integrasi controller dengan model, struktur data, dan tampilan aplikasi.

