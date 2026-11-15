# ADR-001: Pemilihan Struktur Data untuk Pengelolaan Data Putusan Narkotika

- **Status:** Accepted
- **Tanggal:** 2026-11-15
- **Pemilik:** Tim Pengembang Sistem Data Putusan Narkotika

## Konteks

Aplikasi ini digunakan untuk mengelola data putusan perkara narkotika yang diimpor dari file CSV. Setiap data putusan direpresentasikan oleh class `model.Putusan` dan memiliki informasi seperti Case ID, nomor perkara, pengadilan, tanggal putusan, jenis narkotika, berat barang bukti, tuntutan, vonis, denda, serta informasi sumber halaman.

Aplikasi memerlukan struktur data untuk mendukung beberapa operasi berikut:

- Menambahkan data putusan.
- Mengambil data berdasarkan posisi.
- Mengubah data putusan.
- Menghapus data putusan.
- Mencari data berdasarkan Case ID atau nomor perkara.
- Menyaring data berdasarkan beberapa kriteria.
- Mengurutkan data berdasarkan lama vonis.
- Menyimpan riwayat aktivitas aplikasi.
- Menjaga agar data utama tidak berubah ketika ditampilkan dalam keadaan terfilter atau terurut.

Project ini dibuat untuk menerapkan materi Struktur Data. Oleh karena itu, struktur data utama tidak menggunakan `ArrayList` atau koleksi bawaan Java. Struktur data perlu diimplementasikan sendiri agar proses penyimpanan, penambahan kapasitas, penghapusan, dan pemeriksaan kondisi internal dapat dipahami serta diuji secara langsung.

## Keputusan

Aplikasi menggunakan dua struktur data buatan sendiri:

1. `DynamicArray<T>` sebagai struktur data utama untuk menyimpan objek `Putusan`.
2. `AuditList<T>` sebagai linked list untuk menyimpan riwayat aktivitas dan pesan audit.

Kedua struktur tersebut berada di package `struktur` dan digunakan oleh class controller.

### 1. DynamicArray sebagai struktur data utama

Class `DynamicArray<T>` mengimplementasikan interface `StrukturData<T>` dan menyediakan operasi:

- `add(T value)` untuk menambahkan elemen.
- `get(int position)` untuk mengambil elemen berdasarkan indeks.
- `set(int position, T value)` untuk mengganti elemen.
- `remove(int position)` untuk menghapus elemen.
- `size()` untuk mengambil jumlah elemen.
- `isEmpty()` untuk memeriksa apakah struktur kosong.
- `contains(T value)` untuk memeriksa keberadaan elemen.
- `clear()` untuk menghapus seluruh elemen.

Kapasitas awal `DynamicArray` adalah 10 elemen. Jika jumlah elemen telah mencapai kapasitas, kapasitas array digandakan melalui method `tambahKapasitas()`.

Pada aplikasi, struktur ini digunakan dalam `PutusanController` melalui field:

```java
private DynamicArray<Putusan> daftarPutusan;
```

Struktur yang sama juga digunakan untuk menyimpan hasil filter, salinan data yang akan diurutkan, serta distribusi jenis narkotika pada proses agregasi.

### 2. AuditList sebagai struktur data riwayat

Class `AuditList<T>` menggunakan node dan pointer `next` untuk membentuk linked list. Struktur ini dipilih untuk menyimpan aktivitas audit aplikasi, seperti:

- Penambahan putusan.
- Perubahan putusan.
- Penghapusan putusan.
- Pencarian data.
- Penggunaan filter.
- Pengurutan data.
- Impor dan ekspor CSV.
- Perhitungan agregasi.

`AuditList` memiliki pointer `head` dan `tail`, sehingga penambahan aktivitas baru dilakukan pada bagian akhir list tanpa harus menelusuri seluruh node.

Pada aplikasi, struktur ini digunakan dalam `PutusanController` melalui field:

```java
private AuditList<String> riwayat;
```

`AuditList` juga digunakan oleh `HasilImpor` untuk menyimpan pesan baris CSV yang ditolak dan peringatan selama proses impor.

## Alasan Keputusan

### Alasan memilih DynamicArray

`DynamicArray` dipilih karena karakteristik data utama aplikasi sesuai dengan array dinamis:

- Data putusan umumnya ditampilkan dan diakses berdasarkan indeks.
- Data perlu ditambahkan secara bertahap ketika proses impor CSV berlangsung.
- Jumlah data dapat berubah karena terdapat fitur tambah dan hapus putusan.
- Akses berdasarkan posisi sering digunakan oleh tabel GUI dan controller.
- Struktur data dapat digunakan dengan generic type, sehingga tidak hanya bergantung pada satu jenis objek.

Pemilihan ini juga mendukung tujuan akademik project untuk mengimplementasikan struktur data array dinamis secara mandiri.

### Alasan memilih AuditList

`AuditList` dipilih untuk riwayat karena aktivitas baru selalu ditambahkan ke bagian akhir catatan. Dengan adanya pointer `tail`, operasi penambahan dapat dilakukan secara efisien dan tidak memerlukan pemindahan elemen seperti pada array.

Linked list juga memudahkan pemisahan antara penyimpanan data utama dan penyimpanan riwayat aktivitas. Data putusan dapat diakses dan diurutkan melalui `DynamicArray`, sedangkan audit tetap tersusun berdasarkan urutan waktu pencatatan.

### Alasan tidak menggunakan koleksi bawaan sebagai struktur utama

Koleksi seperti `ArrayList` dan `LinkedList` tidak digunakan sebagai struktur penyimpanan utama karena project ini ditujukan untuk menerapkan konsep struktur data secara manual. Dengan implementasi sendiri, project dapat menunjukkan:

- Cara kerja pertumbuhan kapasitas array.
- Cara kerja penambahan dan penghapusan elemen.
- Cara kerja node pada linked list.
- Validasi indeks dan kondisi kosong.
- Pemeriksaan invariant struktur data.

## Dampak Positif

- Struktur data sesuai dengan tujuan pembelajaran mata kuliah Struktur Data.
- `DynamicArray` dapat menyimpan objek `Putusan` secara generic.
- Akses elemen berdasarkan indeks pada `DynamicArray` memiliki kompleksitas O(1).
- Penambahan elemen pada akhir `DynamicArray` memiliki kompleksitas O(1) secara amortisasi.
- Penambahan audit pada akhir `AuditList` memiliki kompleksitas O(1) karena menggunakan pointer `tail`.
- Struktur utama tetap dapat dipakai oleh fitur impor, pencarian, filter, pengurutan, dan statistik.
- Method `validateInvariant()` membantu mendeteksi kerusakan kondisi internal struktur data.
- Data asli tidak berubah ketika proses filter atau pengurutan dilakukan karena controller membuat salinan `DynamicArray`.

## Dampak Negatif

- Penghapusan elemen dari `DynamicArray` memerlukan pergeseran elemen setelahnya sehingga memiliki kompleksitas O(n).
- Pengambilan audit berdasarkan posisi pada `AuditList` memerlukan penelusuran node dari `head`, sehingga memiliki kompleksitas O(n).
- Pencarian Case ID dan nomor perkara masih menggunakan linear search dengan kompleksitas O(n).
- Implementasi sendiri membutuhkan penanganan kapasitas, indeks tidak valid, referensi `null`, dan invariant.
- `DynamicArray` tidak memiliki seluruh fitur yang tersedia pada koleksi bawaan Java.
- Data audit yang banyak akan membutuhkan waktu lebih lama jika sering diambil berdasarkan indeks yang jauh dari awal list.

## Kompleksitas Operasi

| Operasi | Struktur | Kompleksitas |
|---------|----------|--------------|
| Menambah putusan | `DynamicArray.add` | O(1) amortisasi |
| Mengambil putusan berdasarkan indeks | `DynamicArray.get` | O(1) |
| Mengubah putusan | `DynamicArray.set` | O(1) |
| Menghapus putusan | `DynamicArray.remove` | O(n) |
| Mencari Case ID/nomor perkara | Linear search pada `DynamicArray` | O(n) |
| Menyaring data | Penelusuran satu kali | O(n) |
| Menyalin data untuk pengurutan | `DynamicArray` | O(n) |
| Mengurutkan berdasarkan vonis | Insertion Sort | O(n²) pada kasus terburuk |
| Menambah riwayat audit | `AuditList.add` | O(1) |
| Mengambil riwayat berdasarkan posisi | `AuditList.get` | O(n) |
| Memeriksa invariant | Penelusuran struktur | O(n) |

Keterangan: `n` adalah jumlah data yang sedang diproses.

## Konsekuensi pada Implementasi Kode

Keputusan ini tercermin pada beberapa bagian source code berikut:

### `PutusanController`

`PutusanController` menyimpan daftar putusan dalam `DynamicArray<Putusan>` dan riwayat dalam `AuditList<String>`:

```java
private DynamicArray<Putusan> daftarPutusan;
private AuditList<String> riwayat;
```

Operasi tambah, ubah, hapus, pencarian, dan pengambilan data dilakukan melalui struktur tersebut.

### `FilterController`

Hasil filter tidak menggantikan data utama. Method `saring()` membuat `DynamicArray<Putusan>` baru, lalu menambahkan putusan yang memenuhi kriteria.

```java
DynamicArray<Putusan> hasil = new DynamicArray<>();
```

Dengan pendekatan ini, filter tidak merusak atau mengubah urutan data pada struktur utama.

### Pengurutan pada `PutusanController`

Method `urutkanBerdasarkanVonis()` terlebih dahulu membuat salinan data ke dalam `DynamicArray`, kemudian menjalankan Insertion Sort pada salinan tersebut. Oleh karena itu, tombol pengurutan pada GUI hanya mengubah tampilan tabel dan tidak mengubah urutan asli data.

### `HasilImpor`

`HasilImpor` menggunakan `AuditList<String>` untuk menyimpan baris yang ditolak dan peringatan impor. Hal ini membuat pesan impor tetap tersusun berdasarkan urutan kemunculannya saat file CSV dibaca.

### Validasi invariant

`PutusanController.validasiStruktur()` memeriksa invariant dari kedua struktur:



Status hasil pemeriksaan tersebut ditampilkan pada antarmuka utama aplikasi.

## Alternatif yang Dipertimbangkan

### `ArrayList<Putusan>`

`ArrayList` memiliki implementasi yang matang dan menyediakan banyak method siap pakai. Namun, pilihan ini tidak digunakan karena struktur data utama harus dibuat secara mandiri untuk memenuhi tujuan pembelajaran project.

### `LinkedList<Putusan>` sebagai struktur data utama

Linked list dapat melakukan penambahan dan penghapusan elemen dengan baik pada posisi tertentu. Namun, aplikasi sering mengambil data berdasarkan indeks untuk mengisi tabel, melakukan pencarian, dan menghitung statistik. Karena itu, `DynamicArray` lebih sesuai sebagai struktur utama.

### Array Java biasa

Array biasa memiliki ukuran tetap dan akan menyulitkan proses impor dengan jumlah record yang berubah-ubah serta fitur tambah data. `DynamicArray` dipilih untuk memberikan kapasitas yang dapat berkembang secara otomatis.

### HashMap berdasarkan Case ID

`HashMap` dapat mempercepat pencarian berdasarkan Case ID. Namun, struktur tersebut tidak digunakan karena kebutuhan project adalah menerapkan linear search dan struktur data dasar buatan sendiri. Selain itu, aplikasi juga membutuhkan urutan data dan akses berdasarkan posisi.

## Batasan

- `DynamicArray` belum melakukan penyusutan kapasitas ketika jumlah elemen berkurang.
- Pencarian belum menggunakan indeks tambahan berdasarkan Case ID atau nomor perkara.
- Audit hanya disimpan di memori selama aplikasi berjalan sampai diekspor ke CSV.
- Struktur data belum dirancang untuk penggunaan multi-thread atau akses bersamaan.
- Validasi invariant digunakan untuk memeriksa kondisi internal, tetapi belum menggantikan pengujian unit yang lengkap.

## Status dan Evaluasi Ulang

Keputusan ini berstatus **Accepted** dan sesuai dengan kebutuhan project saat ini.

Keputusan dapat dievaluasi ulang apabila:

- Jumlah data meningkat secara signifikan.
- Aplikasi membutuhkan pencarian yang lebih cepat.
- Riwayat audit harus disimpan secara permanen dalam database.
- Aplikasi berubah menjadi sistem multi-user atau membutuhkan akses bersamaan.
- Kebutuhan performa tidak lagi sesuai dengan kompleksitas linear dan Insertion Sort.
