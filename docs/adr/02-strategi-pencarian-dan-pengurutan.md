# ADR-002: Strategi Pencarian dan Pengurutan Data Putusan

- **Status:** Accepted
- **Tanggal:** 2026-11-15
- **Pemilik:** Tim Pengembang Sistem Data Putusan Narkotika

## Konteks

Aplikasi Sistem Pengelolaan Data Putusan Narkotika menyimpan objek `Putusan` dalam `DynamicArray<Putusan>`. Data dapat berasal dari file CSV atau ditambahkan melalui antarmuka Java Swing.

Aplikasi membutuhkan dua kemampuan utama untuk memudahkan pengguna mengelola data:

1. Mencari putusan berdasarkan `Case ID` atau `Nomor Perkara`.
2. Mengurutkan tampilan putusan berdasarkan lama vonis secara menaik maupun menurun.

Pencarian dilakukan pada struktur data utama, sedangkan pengurutan digunakan untuk mengubah urutan tampilan tabel. Data asli harus tetap aman agar operasi tampilan tidak mengubah urutan penyimpanan utama. Selain itu, aplikasi perlu menampilkan informasi langkah pencarian dan jumlah perbandingan pengurutan sebagai bagian dari penerapan serta demonstrasi algoritma Struktur Data.

Jumlah data pada file contoh adalah 50 record. Dengan ukuran data tersebut, strategi sederhana yang mudah dipahami dan sesuai dengan kebutuhan akademik masih dapat digunakan.

## Keputusan

Aplikasi menggunakan strategi berikut:

- **Linear Search** untuk mencari data berdasarkan Case ID atau Nomor Perkara.
- **Insertion Sort** untuk mengurutkan data berdasarkan jumlah bulan vonis.
- Hasil pencarian dikembalikan dalam objek `HasilCari` yang memuat status ditemukan, posisi, jumlah langkah, dan objek putusan.
- Pengurutan dilakukan pada salinan `DynamicArray<Putusan>`, bukan pada struktur data utama.
- Pengurutan bersifat stabil, sehingga data dengan nilai vonis yang sama mempertahankan urutan relatifnya.
- Pengurutan dapat diterapkan pada seluruh data maupun hasil filter.

## Strategi Pencarian

### Linear Search

Pencarian dilakukan dengan menelusuri elemen `DynamicArray` dari indeks pertama sampai elemen yang cocok ditemukan atau seluruh data selesai diperiksa.

Method utama yang digunakan adalah:

```java
public HasilCari cari(String kunci, boolean berdasarkanCaseId){}
```

Parameter `berdasarkanCaseId` menentukan field yang digunakan:

- `true`: mencari berdasarkan `Putusan.getCaseId()`.
- `false`: mencari berdasarkan `Putusan.getNomorPerkara()`.

Perbandingan dilakukan tanpa membedakan huruf besar dan kecil menggunakan `equalsIgnoreCase()`.

Contoh logika pencarian pada `PutusanController`:

```java
for (int i = 0; i < daftarPutusan.size(); i++) {
    langkah++;
    Putusan p = daftarPutusan.get(i);
    String nilai = berdasarkanCaseId
            ? p.getCaseId()
            : p.getNomorPerkara();
    if (nilai != null && nilai.equalsIgnoreCase(target)) {
        return new HasilCari(true, i, langkah, p);
    }
}
```

### Hasil pencarian

Class `HasilCari` menyimpan informasi berikut:

- `ditemukan`: apakah data ditemukan.
- `posisi`: indeks data pada struktur utama, atau `-1` jika tidak ditemukan.
- `langkah`: jumlah elemen yang dibandingkan.
- `putusan`: objek `Putusan` yang ditemukan.

Informasi ini digunakan oleh GUI untuk menampilkan pesan seperti posisi data dan jumlah langkah pencarian.

### Penanganan tampilan terfilter dan terurut

Tabel GUI dapat menampilkan data dalam urutan yang berbeda dari struktur utama. Karena itu, posisi hasil pencarian tidak langsung digunakan sebagai indeks baris tabel.

Setelah putusan ditemukan, `MainFrame` mencari kembali `Case ID` pada model tabel melalui method `cariBarisModel()`. Pendekatan ini memastikan bahwa data tetap dapat dipilih meskipun tampilan sedang diurutkan.

Jika data yang dicari tersembunyi karena filter aktif, aplikasi melepaskan filter tersebut, menampilkan kembali data, lalu memilih baris putusan yang ditemukan.

## Strategi Pengurutan

### Insertion Sort

Pengurutan berdasarkan vonis dilakukan melalui method:

```java
public DynamicArray<Putusan> urutkanBerdasarkanVonis(boolean menaik)
```

Terdapat juga versi yang menerima sumber data tertentu:

```java
public DynamicArray<Putusan> urutkanBerdasarkanVonis(
        DynamicArray<Putusan> sumber, boolean menaik)
```

Jika `sumber` bernilai `null`, pengurutan menggunakan seluruh data pada struktur utama. Jika `sumber` berisi hasil filter, hanya data hasil filter tersebut yang diurutkan.

Algoritma Insertion Sort bekerja dengan mengambil satu elemen sebagai `kunci`, kemudian menggeser elemen sebelumnya selama elemen tersebut berada pada posisi yang salah.

Potongan logika pengurutan pada `PutusanController`:

```java
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
```

### Urutan yang tersedia

GUI menyediakan tiga mode tampilan:

- **Naik**: vonis terkecil ditampilkan lebih dahulu.
- **Turun**: vonis terbesar ditampilkan lebih dahulu.
- **Asli**: data dikembalikan ke urutan penyimpanan semula.

Mode tersebut direpresentasikan oleh `modeUrut` pada `MainFrame`:

- `0`: urutan asli.
- `1`: vonis menaik.
- `2`: vonis menurun.

### Pengurutan pada salinan

Sebelum Insertion Sort dilakukan, data disalin ke `DynamicArray<Putusan>` baru. Struktur `daftarPutusan` pada controller tidak diurutkan secara langsung.

Pendekatan ini dipilih agar:

- Urutan asli hasil impor tetap dipertahankan.
- Filter dan pengurutan hanya memengaruhi tampilan.
- Operasi edit dan hapus tetap dapat menemukan indeks yang benar pada struktur utama.
- Data yang sedang ditampilkan dapat diekspor tanpa merusak data asli.

## Alasan Keputusan

### Alasan memilih Linear Search

Linear Search dipilih karena:

- Struktur utama berbentuk `DynamicArray` dan tidak selalu terurut berdasarkan Case ID atau Nomor Perkara.
- Pencarian dapat dilakukan tanpa mengubah urutan data.
- Implementasinya sederhana dan mudah dipahami.
- Pencarian dapat dilakukan untuk dua field berbeda.
- Jumlah data contoh relatif kecil, yaitu 50 record.
- Algoritma ini sesuai dengan tujuan pembelajaran pencarian dasar.

### Alasan memilih Insertion Sort

Insertion Sort dipilih karena:

- Implementasinya sederhana dan sesuai untuk pembelajaran algoritma pengurutan.
- Dapat mengurutkan salinan `DynamicArray` tanpa membutuhkan struktur tambahan yang kompleks.
- Bersifat stabil untuk elemen dengan nilai vonis yang sama.
- Dapat digunakan untuk urutan menaik dan menurun hanya dengan mengubah kondisi perbandingan.
- Efektif pada data berukuran kecil atau data yang sudah hampir terurut.
- Jumlah perbandingan dapat dihitung dan ditampilkan sebagai informasi pembelajaran.

### Alasan tidak mengurutkan struktur utama

Struktur utama berfungsi sebagai sumber data asli aplikasi. Mengurutkan struktur utama secara langsung dapat menyebabkan:

- Urutan hasil impor hilang.
- Posisi data berubah ketika pengguna sedang memilih baris pada tabel.
- Proses edit atau hapus berisiko menggunakan indeks yang tidak sesuai.
- Pengguna tidak dapat mengembalikan tampilan ke urutan asli tanpa melakukan impor ulang.

Oleh karena itu, pengurutan hanya dilakukan pada salinan.

## Dampak Positif

- Algoritma pencarian dan pengurutan terlihat jelas dalam source code.
- Aplikasi dapat melaporkan jumlah langkah pencarian.
- Aplikasi dapat melaporkan jumlah perbandingan pengurutan melalui `getLangkahUrutTerakhir()`.
- Data asli tetap tidak berubah ketika pengguna mengurutkan tampilan.
- Pengurutan dapat dilakukan pada seluruh data maupun hasil filter.
- Hasil pengurutan stabil untuk data dengan vonis yang sama.
- Implementasi mudah dipelajari, diuji, dan dijelaskan dalam laporan akademik.
- Pencarian tidak memerlukan preprocessing atau indeks tambahan.

## Dampak Negatif

- Linear Search memiliki kompleksitas O(n), sehingga pencarian dapat menjadi lambat jika jumlah data meningkat sangat besar.
- Insertion Sort memiliki kompleksitas O(n²) pada kondisi terburuk.
- Setiap pengurutan membuat salinan data tambahan dengan kebutuhan memori O(n).
- Pencarian tidak memanfaatkan indeks atau struktur pencarian khusus.
- Pengurutan harus dijalankan kembali setiap kali mode tampilan berubah.
- Untuk data dalam jumlah besar, algoritma yang dipilih dapat kalah cepat dibandingkan Binary Search atau algoritma pengurutan O(n log n).

## Kompleksitas Algoritma

| Operasi | Strategi | Kompleksitas Waktu | Kompleksitas Memori Tambahan |
|---------|----------|--------------------|------------------------------|
| Pencarian data | Linear Search | O(n) | O(1) |
| Menyalin sumber sebelum urut | Iterasi `DynamicArray` | O(n) | O(n) |
| Insertion Sort terbaik | Data sudah terurut | O(n) | O(1) setelah salinan |
| Insertion Sort rata-rata | Pergeseran sebagian elemen | O(n²) | O(1) setelah salinan |
| Insertion Sort terburuk | Data terurut terbalik | O(n²) | O(1) setelah salinan |
| Menemukan baris pada model tabel | Linear Search berdasarkan Case ID | O(n) | O(1) |

Keterangan: `n` adalah jumlah data pada sumber yang sedang dicari atau diurutkan.

## Konsekuensi pada Implementasi Kode

### `PutusanController.cari()`

Method ini menjadi pusat pencarian linear. Method mencatat aktivitas ke audit, menghitung langkah, dan mengembalikan `HasilCari` tanpa mengubah struktur utama.

Pencarian yang berhasil maupun tidak berhasil sama-sama dicatat dalam audit, termasuk jumlah langkahnya.

### `PutusanController.urutkanBerdasarkanVonis()`

Method ini membuat salinan sumber, menjalankan Insertion Sort, menyimpan jumlah perbandingan pada `langkahUrutTerakhir`, lalu mengembalikan salinan yang sudah terurut.

```java
private int langkahUrutTerakhir;
```

Nilai tersebut dibaca oleh GUI melalui:

```java
public int getLangkahUrutTerakhir()
```

### `MainFrame.tampilkanSemua()`

Method `tampilkanSemua()` menentukan sumber data yang ditampilkan berdasarkan kondisi berikut:

1. Menggunakan seluruh data jika tidak ada filter dan mode urutan adalah asli.
2. Menggunakan salinan hasil `FilterController.saring()` jika filter aktif.
3. Menggunakan hasil `urutkanBerdasarkanVonis()` jika mode pengurutan aktif.
4. Mengisi tabel dari sumber tampilan tanpa mengubah `daftarPutusan`.

### `MainFrame.cariPutusan()`

GUI memanggil `controller.cari()` untuk memperoleh hasil pencarian. Karena tabel dapat berada dalam kondisi filter atau urut, GUI mencari baris berdasarkan Case ID, bukan hanya menggunakan indeks struktur utama.

### Ekspor data

`CSVWriter` dapat mengekspor seluruh data atau data yang sedang ditampilkan. Saat mode tampilan sedang terfilter atau terurut, data yang diekspor berasal dari `sumberTampil`, sehingga hasil ekspor mengikuti tampilan pengguna tanpa mengubah struktur utama.

## Alternatif yang Dipertimbangkan

### Binary Search

Binary Search lebih cepat dengan kompleksitas O(log n), tetapi membutuhkan data yang sudah terurut berdasarkan kunci pencarian. Data aplikasi tidak selalu tersimpan dalam urutan Case ID atau Nomor Perkara, sehingga Binary Search akan membutuhkan proses pengurutan atau indeks tambahan.

Selain itu, pengguna dapat mencari berdasarkan dua field berbeda. Memelihara urutan untuk kedua field akan menambah kompleksitas implementasi.

### HashMap

`HashMap` dapat memberikan pencarian rata-rata O(1) berdasarkan key seperti Case ID. Namun, pendekatan tersebut tidak dipilih karena:

- Project berfokus pada penerapan Linear Search.
- Diperlukan struktur data buatan sendiri sebagai struktur utama.
- Pencarian juga dilakukan berdasarkan Nomor Perkara.
- Penggunaan map tambahan dapat menyebabkan sinkronisasi ketika data ditambah, diubah, atau dihapus.

### Selection Sort

Selection Sort memiliki kompleksitas O(n²), sama seperti Insertion Sort, tetapi tidak sebaik Insertion Sort dalam mempertahankan stabilitas dan memanfaatkan data yang hampir terurut. Karena pengurutan tampilan harus stabil, Insertion Sort dianggap lebih sesuai.

### Merge Sort atau Quick Sort

Merge Sort dan Quick Sort dapat memiliki performa lebih baik untuk data besar, tetapi implementasinya lebih kompleks dan tidak diperlukan untuk ukuran data contoh yang hanya 50 record. Algoritma tersebut dapat dipertimbangkan jika ukuran dataset berkembang secara signifikan.

### Menggunakan sorting bawaan Java

Pengurutan menggunakan `Collections.sort()` atau `Arrays.sort()` tidak digunakan agar algoritma pengurutan dapat ditunjukkan secara eksplisit di dalam source code dan sesuai dengan tujuan pembelajaran.

## Batasan

- Linear Search belum menggunakan indeks tambahan.
- Performa pencarian akan menurun ketika jumlah data meningkat besar.
- Insertion Sort tidak ideal untuk dataset besar.
- Penghitungan `langkah` merepresentasikan jumlah perbandingan elemen, bukan waktu eksekusi nyata.
- Pengurutan hanya berdasarkan `vonisBulan` dan belum menyediakan kriteria pengurutan lain.
- Saat data diubah atau dihapus, pengguna perlu memastikan baris tabel dipetakan kembali ke posisi struktur utama melalui Case ID.

## Evaluasi dan Pengembangan di Masa Mendatang

Strategi ini dapat dievaluasi ulang apabila kebutuhan aplikasi berubah. Beberapa pengembangan yang mungkin dilakukan adalah:

- Menambahkan indeks berdasarkan Case ID dan Nomor Perkara.
- Menggunakan Binary Search setelah menyediakan salinan data yang terurut berdasarkan field tertentu.
- Menggunakan struktur hash buatan sendiri untuk pencarian key yang lebih cepat.
- Mengganti Insertion Sort dengan Merge Sort atau Quick Sort untuk dataset besar.
- Menambahkan pengurutan berdasarkan tanggal putusan, berat barang bukti, total denda, atau pengadilan.
- Menyimpan hasil pengurutan atau indeks pencarian jika performa menjadi kebutuhan utama.
- Menambahkan pengujian unit untuk kasus data kosong, satu data, data duplikat, nilai vonis sama, dan data dalam urutan terbalik.

## Status

Keputusan ini berstatus **Accepted** dan digunakan pada implementasi proyek saat ini. Linear Search dan Insertion Sort dipilih karena sesuai dengan struktur `DynamicArray`, ukuran data contoh, kebutuhan GUI, serta tujuan pembelajaran algoritma Struktur Data.
