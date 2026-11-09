package kelas.model;

public class Produk {
    private final String kode;
    private final String nama;
    private final String kategori;
    private final double harga;
    private final String gambarUri;

    public Produk(String kode, String nama, String kategori, double harga, String gambarUri) {
        if (kode == null || kode.isBlank()) throw new IllegalArgumentException("Kode produk wajib diisi.");
        if (nama == null || nama.isBlank()) throw new IllegalArgumentException("Nama produk tidak boleh kosong.");
        if (harga <= 0) throw new IllegalArgumentException("Harga harus lebih besar dari nol.");
        
        this.kode = kode;
        this.nama = nama.trim();
        this.kategori = kategori;
        this.harga = harga;
        this.gambarUri = gambarUri;
    }

    public String getKode() { return kode; }
    public String getNama() { return nama; }
    public String getKategori() { return kategori; }
    public double getHarga() { return harga; }
    public String getGambarUri() { return gambarUri; } // Getter baru
}