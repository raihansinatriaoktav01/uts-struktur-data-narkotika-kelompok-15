# Judul: Analisis Kompleksitas Waktu dan Trace Manual

**Bagian 1: Analisis Kompleksitas Waktu**  
Di bagian ini, saya mau membedah seberapa cepat struktur data yang saya buat (`DynamicArray` dan `AuditList`) saat menjalankan operasi-operasi utamanya pada aplikasi pengelolaan data putusan narkotika.

1. Operasi Tambah Data (`add` di DynamicArray)
   Untuk `DynamicArray`, menambahkan data putusan ke urutan paling belakang itu butuh waktu O(1) atau sangat cepat. Program tinggal memasukkan data ke indeks yang ditunjuk oleh variabel `jumlah`, lalu menaikkan nilai `jumlah` satu angka. Tapi, kalau kapasitas array-nya kebetulan sudah penuh, fungsi `tambahKapasitas()` akan jalan buat menggandakan ukuran array dan memindahkan semua data lama ke array baru. Proses pemindahan ini butuh waktu O(n). Jadi, secara rata-rata operasi tambah tetap dianggap O(1) secara amortisasi.

2. Operasi Tambah Riwayat Audit (`add` di AuditList)
   Untuk `AuditList`, menambahkan aktivitas baru ke bagian paling belakang juga butuh waktu O(1). Program membuat node baru, lalu kalau list masih kosong node tersebut menjadi `head` sekaligus `tail`. Kalau sudah ada isinya, node baru dipasang setelah `tail`, kemudian `tail` dipindahkan ke node baru. Karena sudah ada pointer `tail`, program tidak perlu berkeliling dari node pertama untuk mencari posisi terakhir.

3. Operasi Ambil Data (`get`)
   Akses data pada `DynamicArray` berdasarkan indeks selalu O(1). Karena program langsung mengambil isi `data[position]`, prosesnya tidak perlu memutar dari awal untuk mencari elemen tersebut. Berbeda dengan `AuditList`, fungsi `get(position)` harus mulai dari `head` dan berjalan node demi node sampai posisi yang diminta. Oleh karena itu, pengambilan data dari `AuditList` membutuhkan waktu O(n) pada kondisi terburuk.

4. Operasi Cari Data (`cari`)
   Karena saya pakai algoritma Linear Search di `PutusanController`, fungsi pencarian akan mengecek data dari indeks ke-0 sampai data yang dicari ketemu atau sampai elemen terakhir. Jadi kompleksitas waktunya O(n). Kalau datanya ada di paling ujung atau malah tidak ada sama sekali, program harus memeriksa sebanyak jumlah data yang ada. Pada file contoh yang berisi 50 putusan, pencarian terburuk membutuhkan 50 langkah.

5. Operasi Hapus Data (`remove` di DynamicArray)
   Ini yang agak berat. Kalau saya menghapus data di posisi paling akhir, waktunya cuma O(1), karena program hanya mengurangi `jumlah` dan mengosongkan satu slot terakhir. Tapi kalau saya menghapus data di posisi pertama atau di tengah, waktunya jadi O(n). Kenapa? Karena semua data yang ada di sebelah kanannya harus digeser satu per satu ke kiri buat menutup celah kosong supaya urutannya tidak bolong.

6. Operasi Pengurutan Data (`urutkanBerdasarkanVonis`)
   Untuk mengurutkan data berdasarkan lama vonis, program menggunakan algoritma Insertion Sort. Pada kondisi terbaik, kalau data sudah hampir terurut, waktunya bisa mendekati O(n). Tapi pada kondisi terburuk, misalnya data tersusun terbalik, program harus melakukan banyak pergeseran sehingga kompleksitas waktunya menjadi O(n²). Sebelum diurutkan, data disalin terlebih dahulu agar urutan asli pada `DynamicArray` tidak berubah.

**Bagian 2: Penelusuran (Trace) Manual Operasi Hapus**  
Biar lebih jelas, mari kita simulasikan bagaimana fungsi `remove()` di `DynamicArray` saya bekerja di belakang layar saat menghapus satu data putusan di tengah.

Kondisi Awal:

Katakanlah `DynamicArray<Putusan>` memiliki ukuran 5 dan urutannya seperti ini:

```text
[P001, P002, P003, P004, P005]
```

Nilai internalnya:

```text
jumlah = 5
kapasitas = 10
```

Lalu user memilih data `P002` pada indeks ke-1 dan menekan tombol **Hapus**.

Perintah yang akhirnya berjalan di kode:

```java
controller.hapusPutusan(1);
```

Di dalam `PutusanController`, perintah tersebut memanggil:

```java
Putusan putusan = daftarPutusan.remove(1);
```

### Langkah-langkah di memori:

1. Validasi dulu: apakah indeks 1 itu valid? Iya, karena batas indeks yang valid adalah dari 0 sampai `jumlah - 1`, yaitu 0 sampai 4.

2. Program mengamankan data pada indeks ke-1 ke dalam variabel sementara:

   ```text
   nilaiDihapus = P002
   ```

   Data `P002` diamankan supaya bisa dikembalikan oleh method `remove()` dan digunakan oleh `hapusPutusan()` untuk mencatat audit.

3. Program mulai melakukan pergeseran dengan loop berikut:

   ```java
   for (int i = position; i < jumlah - 1; i++) {
       data[i] = data[i + 1];
   }
   ```

   Karena `position = 1` dan `jumlah = 5`, loop berjalan untuk `i = 1`, `i = 2`, dan `i = 3`.

4. Data `P003` yang berada di indeks 2 dipindahkan ke indeks 1.

   ```text
   Sebelum: [P001, P002, P003, P004, P005]
   Sesudah: [P001, P003, P003, P004, P005]
   ```

5. Data `P004` yang berada di indeks 3 dipindahkan ke indeks 2.

   ```text
   Sebelum: [P001, P003, P003, P004, P005]
   Sesudah: [P001, P003, P004, P004, P005]
   ```

6. Data `P005` yang berada di indeks 4 dipindahkan ke indeks 3.

   ```text
   Sebelum: [P001, P003, P004, P004, P005]
   Sesudah: [P001, P003, P004, P005, P005]
   ```

   Sekarang semua data setelah `P002` sudah bergeser satu posisi ke kiri.

7. Kurangi batas ukuran `jumlah` dari 5 menjadi 4:

   ```java
   jumlah--;
   ```

   Kondisi sementara array menjadi:

   ```text
   jumlah = 4
   [P001, P003, P004, P005, P005]
   ```

   Data pada indeks ke-4 sudah berada di luar batas jumlah elemen yang digunakan, tetapi masih tersisa secara fisik di array.

8. Hapus elemen duplikat di ujung kanan dengan memberi nilai `null`:

   ```java
   data[jumlah] = null;
   ```

   Karena `jumlah` sekarang 4, maka `data[4]` dikosongkan.

   Hasil akhir array:

   ```text
   [P001, P003, P004, P005, null]
   ```

9. Method `remove(1)` mengembalikan data yang tadi diamankan:

   ```text
   return nilaiDihapus;
   return P002;
   ```

10. `PutusanController.hapusPutusan()` menerima data `P002`, lalu mencatat aktivitas ke dalam `AuditList`:

    ```text
    Menghapus putusan: nomor perkara milik P002
    ```

11. Setelah tabel diperbarui, `MainFrame` memanggil proses penampilan ulang data. Data yang terlihat sekarang adalah:

    ```text
    [P001, P003, P004, P005]
    ```

12. Terakhir, program dapat memanggil `validasiStruktur()` untuk memastikan kondisi struktur tetap benar. Hasil pengecekan:

    ```text
    jumlah = 4 -> valid
    semua indeks 0 sampai 3 berisi data -> valid
    indeks setelah jumlah, yaitu indeks 4, bernilai null -> valid
    DynamicArray invariant -> aman
    AuditList invariant -> aman
    ```

Jadi, data `P002` berhasil dihapus, data setelahnya bergeser ke kiri, jumlah elemen berkurang menjadi 4, dan tidak ada celah pada urutan data.
