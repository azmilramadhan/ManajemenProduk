package kelas.controller;

import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import kelas.Navigator;
import kelas.data.DataProduk;
import kelas.model.Produk;

public class FormController {

    @FXML private TextField namaField, hargaField;
    @FXML private ComboBox<String> kategoriBox;
    @FXML private Label pesanLabel;
    @FXML private ImageView gambarView;
    
    private Navigator navigator;
    private DataProduk data;
    private String gambarUri; 

    @FXML
    private void initialize() {
        kategoriBox.getItems().addAll("Makanan", "Minuman");
        kategoriBox.setValue("Makanan");
    }

    public void setKonteks(Navigator navigator, DataProduk data) {
        this.navigator = navigator;
        this.data = data;
    }

    @FXML
    private void pilihGambar() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Pilih Gambar Produk");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Gambar PNG/JPG", "*.png", "*.jpg", "*.jpeg"));
        File file = chooser.showOpenDialog(namaField.getScene().getWindow());
        if (file != null) {
            gambarUri = file.toURI().toString();
            gambarView.setImage(new Image(gambarUri, 120, 120, true, true));
            pesanLabel.setStyle("-fx-text-fill: green;");
            pesanLabel.setText("Gambar berhasil dilampirkan.");
        }
    }

    @FXML
    private void simpan() {
        Produk p;
        try {
            double harga = Double.parseDouble(hargaField.getText().trim());
            String nama = namaField.getText();
            String kategori = kategoriBox.getValue();
            
            p = data.tambah(nama, kategori, harga, gambarUri);
        } catch (NumberFormatException e) {
            pesanLabel.setStyle("-fx-text-fill: red;");
            pesanLabel.setText("Harga harus berupa angka yang valid.");
            return;
        } catch (IllegalArgumentException e) {
            pesanLabel.setStyle("-fx-text-fill: red;");
            pesanLabel.setText(e.getMessage());
            return;
        }
        
        try {
            navigator.bukaDetail(p);
        } catch (IOException e) {
            data.hapus(p);
            pesanLabel.setText("Gagal membuka halaman detail.");
        }
    }

    @FXML
    private void bukaDaftar() {
        try { navigator.bukaDaftar(); } 
        catch (IOException e) { e.printStackTrace(); }
    }
}