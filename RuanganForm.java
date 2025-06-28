/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3;

/**
 *
 * @author HAK_PHENG
 */
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class RuanganForm extends JFrame {
    private JTable drinkTable;
    private DefaultTableModel tableModel;
    private JTextField codeField;
    private JComboBox<String> statusField;
    private JButton saveButton;
    private JButton removeButton;
    private JButton editButton;
    private int idCounter = 0; 

    private List<Product> products;
    private List<Customer> customers;
    private Mavenproject3 mainWindow; //for main
    private TransactionForm transactionform;
    private List<Ruangan> ruangan;
    
    public RuanganForm(List<Ruangan> ruangan, Mavenproject3 mainWindow){
        this.ruangan = ruangan;
        this.mainWindow = mainWindow;
        
        setTitle("AAA");
        setSize(600, 450);
        setLocationRelativeTo(null);
        
        String[] columnNames = {"Id", "Code", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        drinkTable = new JTable(tableModel);

        JPanel formPanel = new JPanel();

        formPanel.add(new JLabel("Code: "));
        codeField = new JTextField(10);
        formPanel.add(codeField);
        
        formPanel.add(new JLabel("Kategori:"));
        statusField = new JComboBox<>(new String[]{"Empty", "Reserved"});
        formPanel.add(statusField);

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
                
                codeField.setText(drinkTable.getValueAt(selectedRow, 1).toString());
                statusField.setSelectedItem(drinkTable.getValueAt(selectedRow, 2).toString());
            }
        });

        // Add product
        saveButton.addActionListener(e -> {
            try {
                String code = codeField.getText();
                boolean status = statusField.getSelectedIndex() == 1;

                Ruangan ruang = new Ruangan(0, code, status);
                RuanganAPI.add(ruang);

                loadData();
                codeField.setText("");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan ruangan.");
            }
        });

        // Remove product
        removeButton.addActionListener(e -> {
            int selectedRow = drinkTable.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    Ruangan r = ruangan.get(selectedRow);
                    RuanganAPI.delete(r.getId_ruang());

                    loadData();
                    codeField.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus ruangan.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih ruangan yang ingin dihapus!");
            }
        });

        // Edit product
        editButton.addActionListener(e -> {
            int selectedRow = drinkTable.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    Ruangan r = ruangan.get(selectedRow);
                    String newCode = codeField.getText();
                    boolean newStatus = statusField.getSelectedIndex() == 1;

                    r.setKode_ruang(newCode);
                    r.setReserved(newStatus);

                    RuanganAPI.update(r);
                    loadData();
                    codeField.setText("");

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Gagal mengedit ruangan.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih ruangan yang ingin diubah!");
            }
        });

        
        loadData();
        add(new JScrollPane(drinkTable), BorderLayout.CENTER);
        add(formPanel, BorderLayout.NORTH);

    }
    
    private void loadData() {
        try {
            ruangan.clear();
            ruangan.addAll(RuanganAPI.fetchAll());
            tableModel.setRowCount(0);

            for (Ruangan r : ruangan) {
                String status = r.isReserved() ? "Reserved" : "Empty";
                tableModel.addRow(new Object[]{
                    r.getId_ruang(), r.getKode_ruang(), status
                });
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data ruangan.");
        }
    }
}

