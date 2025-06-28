package com.mycompany.mavenproject3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class ProductForm extends JFrame {
    private JTable drinkTable;
    private DefaultTableModel tableModel;
    private JTextField codeField;
    private JTextField nameField;
    private JComboBox<String> categoryField;
    private JTextField priceField;
    private JTextField stockField;
    private JButton saveButton;
    private JButton removeButton;
    private JButton editButton;
    private JButton refreshBannerButton; // refresh banner
    private int idCounter = 3; 

    private List<Product> products;
    private Mavenproject3 mainWindow; //for main

    // Accept product list and reference to main window
    public ProductForm(List<Product> products, Mavenproject3 mainWindow) {
        this.products = products;
        this.mainWindow = mainWindow; // also for main
        

        setTitle("WK. Cuan | Stok Barang");
        setSize(600, 450);
        setLocationRelativeTo(null);

        String[] columnNames = {"Code", "Nama", "Kategori", "Harga", "Stock"};
        tableModel = new DefaultTableModel(columnNames, 0);
        drinkTable = new JTable(tableModel);

        JPanel formPanel = new JPanel();
        formPanel.add(new JLabel("Kode Barang"));
        codeField = new JTextField(10);
        formPanel.add(codeField);

        formPanel.add(new JLabel("Nama Barang:"));
        nameField = new JTextField(10);
        formPanel.add(nameField);

        formPanel.add(new JLabel("Kategori:"));
        categoryField = new JComboBox<>(new String[]{"Coffee", "Dairy", "Juice", "Soda", "Tea"});
        formPanel.add(categoryField);

        formPanel.add(new JLabel("Harga Jual:"));
        priceField = new JTextField(10);
        formPanel.add(priceField);

        formPanel.add(new JLabel("Stok Tersedia:"));
        stockField = new JTextField(10);
        formPanel.add(stockField);

        saveButton = new JButton("Simpan");
        removeButton = new JButton("Hapus");
        editButton = new JButton("Edit");
        formPanel.add(saveButton);
        formPanel.add(removeButton);
        formPanel.add(editButton);

        // Table selection listener
        drinkTable.getSelectionModel().addListSelectionListener(event -> {
            int selectedRow = drinkTable.getSelectedRow();
            if (selectedRow != -1) {
                codeField.setText(drinkTable.getValueAt(selectedRow, 0).toString());
                nameField.setText(drinkTable.getValueAt(selectedRow, 1).toString());
                categoryField.setSelectedItem(drinkTable.getValueAt(selectedRow, 2).toString());
                priceField.setText(drinkTable.getValueAt(selectedRow, 3).toString());
                stockField.setText(drinkTable.getValueAt(selectedRow, 4).toString());
            }
        });

        // Add product
        saveButton.addActionListener(e -> {
            try {
                String code = codeField.getText();
                String name = nameField.getText();
                String category = categoryField.getSelectedItem().toString();
                double price = Double.parseDouble(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());

                Product product = new Product(0, code, name, category, price, stock); // id = 0 for new
                ProductAPI.add(product);

                clearForm();
                loadProductData(); // refresh table
                mainWindow.updateBannerText();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Harga & Stock harus berupa angka!");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal menyimpan produk!");
            }
        });

        // Remove product
        removeButton.addActionListener(e -> {
            int selectedRow = drinkTable.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    Product product = products.get(selectedRow);
                    ProductAPI.delete(product.getId());

                    clearForm();
                    loadProductData();
                    mainWindow.updateBannerText();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Gagal menghapus produk!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih produk yang ingin dihapus!");
            }
        });

        // Edit product
        editButton.addActionListener(e -> {
            int selectedRow = drinkTable.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    Product product = products.get(selectedRow);
                    String newCode = codeField.getText();
                    String newName = nameField.getText();
                    String newCategory = categoryField.getSelectedItem().toString();
                    double newPrice = Double.parseDouble(priceField.getText());
                    int newStock = Integer.parseInt(stockField.getText());

                    product.setCode(newCode);
                    product.setName(newName);
                    product.setCategory(newCategory);
                    product.setPrice(newPrice);
                    product.setStock(newStock);

                    ProductAPI.update(product);

                    clearForm();
                    loadProductData();
                    mainWindow.updateBannerText();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Harga & Stock harus berupa angka!");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Gagal mengupdate produk!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih produk yang ingin diubah!");
            }
        });
        

        add(new JScrollPane(drinkTable), BorderLayout.CENTER);
        add(formPanel, BorderLayout.SOUTH);

        loadProductData(); // Load products into table at start
    }
    

    private void loadProductData() {
        try {
            products = ProductAPI.fetchAll();
            tableModel.setRowCount(0);
            for (Product p : products) {
                String formattedPrice = "Rp." + p.getPrice();
                tableModel.addRow(new Object[]{
                    p.getCode(), p.getName(), p.getCategory(), formattedPrice, p.getStock()
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal memuat data dari server!");
        }
    }
    
    public void refreshStock(){
        tableModel.setRowCount(0);
        
        for(Product product : products){

            tableModel.addRow(new Object[]{
                product.getCode(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getStock()
            } );              
        }
    }

    private void clearForm() {
        codeField.setText("");
        nameField.setText("");
        categoryField.setSelectedIndex(0);
        priceField.setText("");
        stockField.setText("");
    }
}
