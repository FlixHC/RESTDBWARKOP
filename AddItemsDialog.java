package com.mycompany.mavenproject3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AddItemsDialog extends JDialog {
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> productBox;
    private JTextField qtyField;
    private List<TransactionItem> selectedItems = new ArrayList<>();
    private List<Product> products;

    public AddItemsDialog(JFrame parent, List<Product> products) {
        super(parent, "Tambah Barang", true);
        this.products = products;

        setSize(600, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Table for item list
        model = new DefaultTableModel(new String[]{"Produk", "Harga", "Qty", "Subtotal"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Input controls
        JPanel controlPanel = new JPanel();
        productBox = new JComboBox<>();
        for (Product p : products) productBox.addItem(p.getName());

        qtyField = new JTextField(5);
        JButton addButton = new JButton("Tambah");
        JButton editButton = new JButton("Edit");
        JButton removeButton = new JButton("Hapus");

        controlPanel.add(new JLabel("Produk:"));
        controlPanel.add(productBox);
        controlPanel.add(new JLabel("Qty:"));
        controlPanel.add(qtyField);
        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(removeButton);

        add(controlPanel, BorderLayout.NORTH);
        
        // Add Button Logic
        addButton.addActionListener(e -> {
            String productName = (String) productBox.getSelectedItem();
            int qty;
            try {
                qty = Integer.parseInt(qtyField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Qty harus angka.");
                return;
            }

            Product selected = products.stream()
                    .filter(p -> p.getName().equals(productName))
                    .findFirst().orElse(null);

            if (selected == null || qty > selected.getStock()) {
                JOptionPane.showMessageDialog(this, "Stok tidak cukup atau produk tidak ditemukan.");
                return;
            }

            TransactionItem item = new TransactionItem(productName, qty, selected.getPrice());
            selectedItems.add(item);
            double price = selected.getPrice();
            double total = item.getTotalPrice();
            String formattedPrice = "Rp." + String.valueOf(price);
            String formattedTotal = "Rp." + String.valueOf(total);
            model.addRow(new Object[]{productName, formattedPrice, qty, formattedTotal});

            qtyField.setText("");
        });

        // Edit Button Logic
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0 && selectedRow < selectedItems.size()) {
                String productName = (String) model.getValueAt(selectedRow, 0);
                String qtyStr = JOptionPane.showInputDialog(this, "Edit Qty:", model.getValueAt(selectedRow, 2));

                try {
                    int newQty = Integer.parseInt(qtyStr);

                    Product selected = products.stream()
                            .filter(p -> p.getName().equals(productName))
                            .findFirst().orElse(null);

                    if (selected == null || newQty > selected.getStock()) {
                        JOptionPane.showMessageDialog(this, "Stok tidak cukup atau produk tidak ditemukan.");
                        return;
                    }
                    
                    TransactionItem updated = new TransactionItem(productName, newQty, selected.getPrice());
                    double updateTotal = updated.getTotalPrice();
                    String updatedTotalPrice = "Rp." + String.valueOf(updateTotal);
                    selectedItems.set(selectedRow, updated);

                    model.setValueAt(newQty, selectedRow, 2);
                    model.setValueAt( updatedTotalPrice, selectedRow, 3);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Qty harus angka.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih item untuk diedit.");
            }
        });

        // Remove Button Logic
        removeButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0 && selectedRow < selectedItems.size()) {
                selectedItems.remove(selectedRow);
                model.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Pilih item untuk dihapus.");
            }
        });

        // Finish Button
        JButton okButton = new JButton("Selesai");
        okButton.addActionListener(e -> dispose());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(okButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public List<TransactionItem> getSelectedItems() {
        return selectedItems;
    }
}
