# Sistem Pengelolaan Data Putusan Narkotika

## Anggota Kelompok

| Nama | NIM |
|------|-----|
| Risky Dwi Kirana Aditya| 202510370110106 |
| Rayhan Sinatria Oktav | 202510370110077 |

## Deskripsi Project

Sistem Pengelolaan Data Putusan Narkotika adalah aplikasi desktop berbasis Java Swing yang digunakan untuk mengelola data putusan perkara narkotika dari pengadilan negeri.

Aplikasi ini menyediakan fitur impor data dari CSV, penambahan, pengubahan, penghapusan, pencarian, filter, pengurutan, agregasi statistik, pemeriksaan invariant struktur data, serta ekspor data dan riwayat aktivitas ke CSV.

Project ini dibuat sebagai penerapan materi Struktur Data. Struktur data utama tidak menggunakan `ArrayList` atau koleksi bawaan Java, melainkan menggunakan `DynamicArray` yang diimplementasikan secara mandiri. Riwayat aktivitas dan pesan hasil impor menggunakan `AuditList` berbasis linked list buatan sendiri.

## Tujuan

- Menerapkan konsep Dynamic Array secara mandiri.
- Menerapkan linked list untuk menyimpan riwayat audit.
- Memahami pencarian linear pada kumpulan data.
- Menerapkan algoritma Insertion Sort untuk pengurutan data.
- Mengelola data putusan narkotika secara terstruktur.
- Menghasilkan statistik dan agregasi dari data putusan.
- Mengimplementasikan struktur data dalam aplikasi GUI Java Swing.
- Menyediakan proses impor dan ekspor data CSV yang tervalidasi.

## Fitur Aplikasi

### Pengelolaan Data

- Menampilkan seluruh data putusan dalam tabel.
- Menambahkan data putusan secara manual.
- Mengubah data putusan yang dipilih.
- Menghapus data putusan yang dipilih.
- Menampilkan detail lengkap suatu putusan.

### Impor dan Ekspor

- Mengimpor data putusan dari file CSV.
- Memvalidasi jumlah kolom, format tanggal, format angka, nilai negatif, dan Case ID duplikat.
- Mengabaikan baris kosong pada file CSV.
- Menampilkan jumlah data yang diterima, ditolak, serta peringatan proses impor.
- Mengekspor seluruh data putusan ke CSV.
- Mengekspor data yang sedang ditampilkan, termasuk hasil filter atau pengurutan, ke CSV.
- Mengekspor riwayat audit ke CSV.

### Pencarian

- Pencarian linear berdasarkan Case ID.
- Pencarian linear berdasarkan Nomor Perkara.
- Menampilkan posisi data yang ditemukan.
- Menampilkan jumlah langkah atau perbandingan yang dilakukan selama pencarian.

### Filter

Filter menggunakan logika **AND**, sehingga data harus memenuhi semua kriteria yang diisi.

Kriteria yang tersedia:

- Jenis narkotika, dengan pencarian sebagian dan tidak membedakan huruf besar-kecil.
- Pasal terbukti, dengan pencarian sebagian dan tidak membedakan huruf besar-kecil.
- Berat barang bukti minimum dan maksimum.
- Vonis minimum dan maksimum.

Hasil filter dibuat sebagai salinan sehingga data pada struktur utama tidak berubah.

### Pengurutan

- Mengurutkan data berdasarkan lama vonis secara menaik.
- Mengurutkan data berdasarkan lama vonis secara menurun.
- Mengembalikan tampilan ke urutan asli.
- Pengurutan menggunakan algoritma **Insertion Sort** yang stabil.
- Struktur utama tetap tidak diubah karena pengurutan dilakukan pada salinan data.
- Jumlah perbandingan pada pengurutan terakhir ditampilkan pada status aplikasi.

### Statistik dan Agregasi

Aplikasi dapat menghitung data yang sedang ditampilkan, baik seluruh data maupun hasil filter, berupa:

- Jumlah perkara.
- Rata-rata vonis dalam bulan.
- Total denda dalam rupiah.
- Distribusi jenis narkotika.

Jenis narkotika yang ditulis dalam satu perkara sebagai beberapa zat, misalnya `Metamfetamina (Sabu), MDMA (Ekstasi)`, dihitung pada masing-masing zat. Penulisan `Shabu` dan `Sabu` dinormalisasi agar dihitung sebagai jenis yang sama.

### Audit Aktivitas

Aktivitas penting pengguna dicatat dalam `AuditList`, antara lain:

- Menambahkan putusan.
- Mengubah putusan.
- Menghapus putusan.
- Mencari data.
- Menggunakan filter.
- Mengurutkan tampilan.
- Melakukan agregasi.
- Mengimpor dan mengekspor data.
- Membuka tampilan audit.

Status invariant struktur data juga ditampilkan pada antarmuka aplikasi.

## Teknologi yang Digunakan

| Komponen | Teknologi |
|----------|-----------|
| Bahasa Pemrograman | Java |
| GUI | Java Swing |
| Struktur Data Utama | Dynamic Array buatan sendiri |
| Struktur Data Audit | Linked List buatan sendiri melalui `AuditList` |
| Algoritma Pencarian | Linear Search |
| Algoritma Pengurutan | Insertion Sort |
| Format Penyimpanan | CSV |
| IDE yang Didukung | IntelliJ IDEA |
| Version Control | Git dan GitHub |

## Struktur Project

```text
UTS-Struktur-Data-Narkotika/
└── src/
    ├── controller/
    │   ├── CSVReader.java
    │   ├── CSVWriter.java
    │   ├── FilterController.java
    │   ├── HasilAgregasi.java
    │   ├── HasilCari.java
    │   ├── HasilImpor.java
    │   ├── KriteriaFilter.java
    │   ├── PutusanController.java
    │   └── StatistikController.java
    ├── data/
    │   └── DataPutusan50.csv
    ├── demo/
    │   └── provenance.csv
    ├── model/
    │   └── Putusan.java
    ├── struktur/
    │   ├── AuditList.java
    │   ├── DynamicArray.java
    │   └── StrukturData.java
    └── view/
        └── MainFrame.java
```

## Penjelasan Komponen Utama

| Komponen | Tanggung Jawab |
|----------|----------------|
| `Main.java` | Titik awal aplikasi dan pembuatan GUI. |
| `Putusan.java` | Model data untuk satu putusan perkara. |
| `DynamicArray.java` | Struktur data array dinamis dengan operasi tambah, ambil, ubah, hapus, dan pencarian isi. |
| `AuditList.java` | Linked list untuk menyimpan aktivitas audit dan pesan proses. |
| `StrukturData.java` | Interface operasi dasar struktur data. |
| `PutusanController.java` | Mengelola data putusan, pencarian, pengurutan, audit, dan validasi invariant. |
| `CSVReader.java` | Membaca serta memvalidasi file CSV sebelum data dimasukkan ke struktur utama. |
| `CSVWriter.java` | Mengekspor data putusan dan riwayat audit ke file CSV. |
| `FilterController.java` | Menjalankan filter atas data putusan. |
| `KriteriaFilter.java` | Menyimpan dan mengevaluasi kriteria filter. |
| `StatistikController.java` | Menghitung statistik dan distribusi jenis narkotika. |
| `HasilAgregasi.java` | Menyimpan hasil agregasi dan menyusun laporan ringkasan. |
| `HasilCari.java` | Menyimpan hasil pencarian, posisi, dan jumlah langkah. |
| `HasilImpor.java` | Menyimpan ringkasan data diterima, ditolak, dan peringatan impor. |
| `MainFrame.java` | Antarmuka pengguna berbasis Java Swing. |

## Format File CSV

File utama yang disediakan adalah `src/data/DataPutusan50.csv`. File ini berisi tepat 50 record data putusan dengan 16 kolom berikut:

```text
caseId,nomorPerkara,pengadilan,tanggalPutusan,terdakwaKode,jenisNarkotika,beratBarangBuktiGram,pasalDakwaan,pasalTerbukti,tuntutanBulan,vonisBulan,dendaRupiah,barangBukti,keadaanMemberatkan,keadaanMeringankan,sourcePages
```

Contoh satu baris data:

```csv
P001,181/Pid.Sus/2025/PN Gns,Pengadilan Negeri Gunung Sugih,2025-08-12,YB,"Metamfetamina (Sabu), MDMA (Ekstasi)",14.916,Pasal 114 Ayat (2) UU No. 35 Tahun 2009,Pasal 114 Ayat (2) UU No. 35 Tahun 2009,102,90,1000000000,"1 bungkus plastik klip sedang sabu; 32 bungkus plastik klip shabu",Perbuatan Terdakwa meresahkan masyarakat,Terdakwa mengakui dan menyesali perbuatannya,"halaman 1, 2, 5, 19, 23, 24"
```

Catatan:

- Format tanggal menggunakan `YYYY-MM-DD`, misalnya `2025-08-12`.
- Nilai berat menggunakan satuan gram.
- Nilai tuntutan dan vonis menggunakan satuan bulan.
- Nilai denda menggunakan angka rupiah tanpa pemisah ribuan.
- Kolom teks yang mengandung koma harus diapit tanda kutip ganda.
- File `src/demo/provenance.csv` menyimpan informasi sumber dan verifikasi setiap field data.

## Cara Menjalankan di IntelliJ IDEA

### Persyaratan

- IntelliJ IDEA Community atau Ultimate.
- JDK 8 atau lebih baru. JDK 17 atau lebih baru direkomendasikan.
- Tidak diperlukan Maven atau library eksternal karena aplikasi menggunakan Java Swing bawaan JDK.

### Langkah-langkah

1. Ekstrak arsip project atau clone repository GitHub.
2. Buka IntelliJ IDEA.
3. Pilih **File** → **Open**, lalu pilih folder `UTS-Struktur-Data-Narkotika`.
4. Jika muncul pilihan **Trust Project**, pilih **Trust Project**.
5. Pastikan folder `src` dikenali sebagai **Sources Root**.
6. Buka `src/Main.java`.
7. Klik ikon hijau di samping method `main`, lalu pilih **Run 'Main.main()'**.
8. Setelah jendela aplikasi terbuka, klik tombol **Impor CSV**.
9. Pilih file:

   ```text
   src/data/DataPutusan50.csv
   ```

10. Setelah impor selesai, data putusan akan tampil pada tabel utama.

Aplikasi dimulai dengan struktur data kosong. Data tidak dimuat secara otomatis sehingga proses impor dilakukan melalui tombol **Impor CSV**.

## Cara Menjalankan dari Terminal

Pastikan `java` dan `javac` sudah tersedia pada PATH sistem. Dari folder root project, jalankan:

```bash
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out Main
```

Pada Windows PowerShell, seluruh file Java dapat dikompilasi menggunakan perintah berikut:

```powershell
New-Item -ItemType Directory -Force out
javac -d out (Get-ChildItem -Recurse -Filter *.java src).FullName
java -cp out Main
```

## Alur Penggunaan Singkat

1. Jalankan aplikasi melalui `Main.java`.
2. Klik **Impor CSV** dan pilih `DataPutusan50.csv`.
3. Gunakan tabel untuk melihat data putusan.
4. Gunakan panel pencarian untuk mencari berdasarkan Case ID atau Nomor Perkara.
5. Isi kriteria pada panel filter untuk menyaring data.
6. Gunakan tombol **Naik**, **Turun**, atau **Asli** untuk mengubah urutan tampilan berdasarkan vonis.
7. Klik tombol agregasi/statistik untuk melihat ringkasan data yang sedang ditampilkan.
8. Buka menu audit untuk melihat seluruh riwayat aktivitas.
9. Gunakan fitur ekspor untuk menyimpan data putusan atau audit dalam format CSV.

## Kompleksitas Algoritma

| Operasi | Algoritma/Struktur | Kompleksitas Umum |
|---------|---------------------|-------------------|
| Menambah data ke `DynamicArray` | Array dinamis | O(1) amortisasi |
| Mengambil data berdasarkan indeks | `DynamicArray.get` | O(1) |
| Menghapus data | Pergeseran elemen array | O(n) |
| Mencari Case ID/Nomor Perkara | Linear Search | O(n) |
| Memfilter data | Satu kali penelusuran | O(n) |
| Mengurutkan berdasarkan vonis | Insertion Sort | O(n²) terburuk |
| Menambah audit | Linked list dengan tail | O(1) |
| Mengambil audit berdasarkan posisi | Penelusuran linked list | O(n) |
| Menghitung distribusi narkotika | Penelusuran dan pencocokan jenis | O(n × k) |

Keterangan: `n` adalah jumlah putusan/data yang diproses, sedangkan `k` adalah jumlah jenis narkotika berbeda yang ditemukan.

## Validasi dan Invariant

Data yang diimpor ditolak apabila memenuhi kondisi berikut:

- Jumlah kolom tidak sama dengan 16.
- Case ID kosong.
- Nomor perkara kosong.
- Nama pengadilan kosong.
- Case ID sudah digunakan.
- Format tanggal tidak valid.
- Format angka tidak valid.
- Berat barang bukti, tuntutan, vonis, atau denda bernilai negatif.

Aplikasi juga memeriksa invariant pada `DynamicArray` dan `AuditList`, termasuk jumlah elemen, kondisi head/tail, dan posisi data kosong. Status invariant ditampilkan pada bagian status aplikasi.

## Catatan Pengembangan

- Struktur data utama tidak diubah ketika data ditampilkan dalam keadaan terfilter atau terurut. Tampilan menggunakan salinan data.
- CSV reader mendukung tanda kutip ganda untuk data yang mengandung koma.
- Jika jumlah record hasil impor tidak sama dengan target 50 record, aplikasi menampilkan peringatan.
- Satu atau lebih jenis narkotika dalam satu putusan dapat dihitung pada masing-masing kategori dalam distribusi.
- Riwayat audit hanya tersimpan selama aplikasi berjalan, kecuali diekspor melalui fitur **Ekspor Audit CSV**.

## Lisensi

Project ini dibuat untuk keperluan akademik dan pembelajaran mata kuliah Struktur Data.
