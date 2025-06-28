package com.mycompany.mavenproject3;

import java.awt.BorderLayout;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXDatePicker;
import java.util.Date;
import java.time.LocalTime;
import java.time.ZoneId;

public class ReservasiForm extends JFrame {
    private JTable drinkTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JXDatePicker datePicker;
    private JComboBox<String> ruangField;
    private JButton saveButton;
    private JButton removeButton;
    private JButton editButton;
    private JSpinner timeSpinner;

    private int idCounter = 1;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    private List<Reservasi> reservasi;
    private List<Ruangan> ruang;
    private Mavenproject3 mainWindow;

    public ReservasiForm(List<Reservasi> reservasi, Mavenproject3 mainWindow, List<Ruangan> ruang) {
        this.reservasi = reservasi;
        this.ruang = ruang;
        this.mainWindow = mainWindow;

        setTitle("AAA");
        setSize(600, 450);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Customer Name", "Room", "Reserved at"};
        tableModel = new DefaultTableModel(columnNames, 0);
        drinkTable = new JTable(tableModel);

        JPanel formPanel = new JPanel();

        formPanel.add(new JLabel("Nama Customer:"));
        nameField = new JTextField(10);
        formPanel.add(nameField);

        formPanel.add(new JLabel("Ruangan:"));
        ruangField = new JComboBox<>();
        for (Ruangan r : ruang) {
            ruangField.addItem(r.getKode_ruang());
        }
        formPanel.add(ruangField);

        formPanel.add(new JLabel("Waktu:"));
        SpinnerDateModel timeModel = new SpinnerDateModel();
        timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setValue(new Date());
        formPanel.add(timeSpinner);

        formPanel.add(new JLabel("Tanggal:"));
        datePicker = new JXDatePicker();
        datePicker.setDate(new Date());
        datePicker.setFormats("dd-MM-yyyy");
        formPanel.add(datePicker);

        saveButton = new JButton("Simpan");
        removeButton = new JButton("Hapus");
        editButton = new JButton("Edit");
        formPanel.add(saveButton);
        formPanel.add(removeButton);
        formPanel.add(editButton);

        drinkTable.getSelectionModel().addListSelectionListener(event -> {
            int selectedRow = drinkTable.getSelectedRow();
            if (selectedRow != -1) {
                nameField.setText(drinkTable.getValueAt(selectedRow, 1).toString());

                // Set date
                try {
                    Date date = sdf.parse(drinkTable.getValueAt(selectedRow, 2).toString());
                    datePicker.setDate(date);
                } catch (Exception e) {
                    datePicker.setDate(new Date());
                }

                // Set time
                try {
                    Date time = timeFormat.parse(drinkTable.getValueAt(selectedRow, 3).toString());
                    timeSpinner.setValue(time);
                } catch (Exception e) {
                    timeSpinner.setValue(new Date());
                }
            }
        });

        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                String room = ruangField.getSelectedItem().toString(); // belum disimpan ke model
                Date date = datePicker.getDate();
                LocalTime time = ((Date) timeSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

                Reservasi reserve = new Reservasi(0, name, date, time, null); // sesuaikan jika model mendukung ruangan

                ReservasiAPI.add(reserve);
                loadProductData();
                clearFields();
                mainWindow.updateBannerText();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan reservasi.");
            }
        });

        removeButton.addActionListener(e -> {
            int selectedRow = drinkTable.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    Reservasi r = reservasi.get(selectedRow);
                    ReservasiAPI.delete(r.getIdReservasi());

                    loadProductData();
                    clearFields();
                    mainWindow.updateBannerText();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus reservasi.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih reservasi yang ingin dihapus!");
            }
        });

        editButton.addActionListener(e -> {
            int selectedRow = drinkTable.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    Reservasi r = reservasi.get(selectedRow);
                    String newName = nameField.getText();
                    Date newDate = datePicker.getDate();
                    LocalTime newTime = ((Date) timeSpinner.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

                    r.setCustomerName(newName);
                    r.setTanggalReservasi(newDate);
                    r.setMasaWaktuReservasi(newTime);

                    ReservasiAPI.update(r);
                    loadProductData();
                    clearFields();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Gagal mengedit reservasi.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih reservasi yang ingin diubah!");
            }
        });
        add(new JScrollPane(drinkTable), BorderLayout.CENTER);
        add(formPanel, BorderLayout.SOUTH);

        loadProductData();
    }

    private void loadProductData() {
        try {
            reservasi.clear();
            reservasi.addAll(ReservasiAPI.fetchAll());
            tableModel.setRowCount(0);

            for (Reservasi r : reservasi) {
                String formatted = sdf.format(r.getTanggalReservasi()) + " at " + r.getMasaWaktuReservasi();
                tableModel.addRow(new Object[]{
                    r.getIdReservasi(),
                    r.getCustomerName(),
                    "-", // belum ada relasi ruangan secara langsung
                    formatted
                });
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data reservasi.");
        }
    }

    private void clearFields() {
        nameField.setText("");
        datePicker.setDate(new Date());
        timeSpinner.setValue(new Date());
    }
}