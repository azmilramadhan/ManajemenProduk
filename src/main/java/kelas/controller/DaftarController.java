package kelas.controller;

import java.io.IOException;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import kelas.Navigator;
import kelas.data.DataProduk;
import kelas.model.Produk;

public class DaftarController {

    @FXML
    private TableView<Produk> tabel;
    @FXML
    private TableColumn<Produk, String> kodeCol;
    @FXML
    private TableColumn<Produk, String> namaCol;
    @FXML
    private TableColumn<Produk, String> kategoriCol;
    @FXML
    private TableColumn<Produk, Double> hargaCol;
    @FXML
    private TextField cariField;
    @FXML
    private ComboBox<String> filterKategoriBox;
    @FXML
    private Label pesanLabel;

    private Navigator navigator;
    private DataProduk data;
    private final ObservableList<Produk> dataTampilan = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        kodeCol.setCellValueFactory(new PropertyValueFactory<>("kode"));
        namaCol.setCellValueFactory(new PropertyValueFactory<>("nama"));
        kategoriCol.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        hargaCol.setCellValueFactory(new PropertyValueFactory<>("harga"));

        filterKategoriBox.getItems().addAll("Semua", "Makanan", "Minuman");
        filterKategoriBox.setValue("Semua");

        tabel.setItems(dataTampilan);
        tabel.setPlaceholder(new Label("Belum ada data produk yang sesuai."));
    }

    public void setKonteks(Navigator navigator, DataProduk data) {
        this.navigator = navigator;
        this.data = data;
        cari();
    }

    @FXML
    private void cari() {
        if (data == null) {
            return;
        }
        dataTampilan.clear();
        String kata = cariField.getText().trim().toLowerCase(Locale.ROOT);
        String kategoriDipilih = filterKategoriBox.getValue();

        for (Produk p : data.getDaftar()) {
            String kode = p.getKode().toLowerCase(Locale.ROOT);
            String nama = p.getNama().toLowerCase(Locale.ROOT);
            boolean cocokKata = false;
            boolean cocokKategori = false;

            if (kode.contains(kata) || nama.contains(kata)) {
                cocokKata = true;
            }
            if ("Semua".equals(kategoriDipilih) || p.getKategori().equals(kategoriDipilih)) {
                cocokKategori = true;
            }
            if (cocokKata && cocokKategori) {
                dataTampilan.add(p);
            }
        }
        tabel.sort();
        pesanLabel.setText("Jumlah data yang tampil: " + dataTampilan.size());
    }

    @FXML
    private void resetPencarian() {
        cariField.clear();
        filterKategoriBox.setValue("Semua");
        cari();
    }

    @FXML
    private void tambah() {
        try {
            navigator.bukaForm();
        } catch (IOException e) {
            pesanLabel.setText("Form gagal dibuka.");
            e.printStackTrace();
        }
    }

    @FXML
    private void detail() {
        Produk p = tabel.getSelectionModel().getSelectedItem();
        if (p == null) {
            pesanLabel.setText("Pilih satu baris dahulu.");
            return;
        }
        try {
            navigator.bukaDetail(p);
        } catch (IOException e) {
            pesanLabel.setText("Detail gagal dibuka.");
            e.printStackTrace();
        }
    }

    @FXML
    private void hapus() {
        Produk p = tabel.getSelectionModel().getSelectedItem();
        if (p == null) {
            pesanLabel.setText("Pilih satu baris dahulu.");
            return;
        }
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION,
                "Hapus " + p.getKode() + " - " + p.getNama() + "?",
                ButtonType.YES, ButtonType.NO);
        dialog.initOwner(tabel.getScene().getWindow());
        ButtonType jawaban = dialog.showAndWait().orElse(ButtonType.NO);
        if (jawaban == ButtonType.YES) {
            data.hapus(p);
            cari();
        }
    }
}