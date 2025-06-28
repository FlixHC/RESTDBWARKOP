package com.mycompany.mavenproject3;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class Mavenproject3 extends JFrame implements Runnable {
    private List<Product> products = new ArrayList<>();
    private List<Customer> customer = new ArrayList<>();
    private List<Transaction> transaction = new ArrayList<>();
    private List<Reservasi> reservasi = new ArrayList<>();
    private List<Ruangan> ruangan = new ArrayList<>();

    private ProductForm productform;
    private CustomerForm customerform;
    private TransactionForm transactionform;
    private ReservasiForm reservasiform;
    private RuanganForm ruanganform;
    private LoginForm loginform;

    private String bannerText;
    private int x;
    private int width;
    private BannerPanel bannerPanel;

    private JButton addProductButton;
    private JButton addcustomerButton;
    private JButton addTransactionButton;
    private JButton addReservasiButton;
    private JButton addRuanganButton;
    private JButton logoutButton;

    public Mavenproject3(LoginForm loginform) {
        this.loginform = loginform;

        // Muat data dinamis jika sudah ada API
        loadInitialData();

        setTitle("WK. STI Chill");
        setSize(600, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        bannerPanel = new BannerPanel();
        add(bannerPanel, BorderLayout.CENTER);
        updateBannerText();

        JPanel bottomPanel = new JPanel();
        addProductButton = new JButton("Kelola Produk");
        addcustomerButton = new JButton("Form Customer");
        addTransactionButton = new JButton("Transaksi");
        addReservasiButton = new JButton("Reservasi");
        addRuanganButton = new JButton("Ruangan");
        logoutButton = new JButton("Logout");

        bottomPanel.add(addProductButton);
        bottomPanel.add(addcustomerButton);
        bottomPanel.add(addTransactionButton);
        bottomPanel.add(addReservasiButton);
        bottomPanel.add(addRuanganButton);
        bottomPanel.add(logoutButton);

        add(bottomPanel, BorderLayout.SOUTH);

        productform = new ProductForm(products, this);
        customerform = new CustomerForm(products, customer, this);
        transactionform = new TransactionForm(products, this, customer, transaction);
        reservasiform = new ReservasiForm(reservasi, this, ruangan);
        ruanganform = new RuanganForm(ruangan, this);

        transactionform.setProductForm(productform);
        transactionform.setCustomerForm(customerform);
        customerform.setTransactionForm(transactionform);

        // 👇 Tombol navigasi
        addProductButton.addActionListener(e -> productform.setVisible(true));
        addcustomerButton.addActionListener(e -> customerform.setVisible(true));
        addTransactionButton.addActionListener(e -> transactionform.setVisible(true));
        addReservasiButton.addActionListener(e -> reservasiform.setVisible(true));
        addRuanganButton.addActionListener(e -> ruanganform.setVisible(true));

        logoutButton.addActionListener(e -> {
            dispose();
            new LoginForm();
        });

        setVisible(true);

        // Banner berjalan
        new Thread(this).start();
    }

    // 🧠 Fungsi untuk memuat data awal (dari API kalau sudah ada)
    private void loadInitialData() {
        // Bisa ganti bagian ini ke API call jika tersedia
        try {
            // Contoh data dummy (bisa diganti jadi ProductAPI.fetchAll(), dll.)
            products.add(new Product(1, "P001", "Americano", "Coffee", 18000, 10));
            products.add(new Product(2, "P002", "Pandan Latte", "Coffee", 15000, 8));
            products.add(new Product(3, "P003", "Cheese", "Dairy", 15000, 8));

            customer.add(new Customer(1, "AAAAA", 231235134));

            ruangan = RuanganAPI.fetchAll(); // <- ini real-time via API
            reservasi = ReservasiAPI.fetchAll(); // <- juga real-time via API

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data awal dari server: " + e.getMessage());
        }
    }

    public void updateBannerText() {
        StringBuilder sb = new StringBuilder("Menu yang tersedia: ");
        for (Product p : products) {
            if (p.getStock() > 0) {
                sb.append(p.getName()).append(" | ");
            }
        }
        bannerText = sb.toString();
        bannerPanel.repaint();
    }

    class BannerPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString(bannerText, x, getHeight() / 2);
        }
    }

    @Override
    public void run() {
        width = getWidth();
        while (true) {
            x += 5;
            if (x > width) {
                x = -getFontMetrics(new Font("Arial", Font.BOLD, 18)).stringWidth(bannerText);
            }
            bannerPanel.repaint();
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginForm::new);
    }
}