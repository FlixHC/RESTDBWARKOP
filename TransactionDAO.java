/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3;

/**
 *
 * @author khouw
 */
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public static void save(Transaction t) {
        try (Connection conn = DB.connect()) {
            conn.setAutoCommit(false);

            // Insert into transaction table
            String transSql = "INSERT INTO transaction (customer_name, transaction_datetime) VALUES (?, ?)";
            PreparedStatement psTrans = conn.prepareStatement(transSql, Statement.RETURN_GENERATED_KEYS);
            psTrans.setString(1, t.getCustomerName());
            psTrans.setTimestamp(2, Timestamp.valueOf(t.getTransactionDateTime()));
            psTrans.executeUpdate();

            ResultSet rs = psTrans.getGeneratedKeys();
            int transactionId = 0;
            if (rs.next()) {
                transactionId = rs.getInt(1);
            }

            // Insert into transaction_item table
            String itemSql = "INSERT INTO transaction_item (transaction_id, product_name, qty, price) VALUES (?, ?, ?, ?)";
            PreparedStatement psItem = conn.prepareStatement(itemSql);

            for (TransactionItem item : t.getItems()) {
                psItem.setInt(1, transactionId);
                psItem.setString(2, item.getProductName());
                psItem.setInt(3, item.getQty());
                psItem.setDouble(4, item.getPrice());
                psItem.addBatch();
            }

            psItem.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Transaction> getAll() {
        List<Transaction> list = new ArrayList<>();

        try (Connection conn = DB.connect()) {
            String transSql = "SELECT * FROM transaction";
            ResultSet rs = conn.createStatement().executeQuery(transSql);

            while (rs.next()) {
                int id = rs.getInt("transaction_id");
                String customer = rs.getString("customer_name");
                LocalDateTime time = rs.getTimestamp("transaction_datetime").toLocalDateTime();

                Transaction t = new Transaction(id, time, customer);

                // Get items
                String itemSql = "SELECT * FROM transaction_item WHERE transaction_id = ?";
                PreparedStatement ps = conn.prepareStatement(itemSql);
                ps.setInt(1, id);
                ResultSet itemRs = ps.executeQuery();
                while (itemRs.next()) {
                    TransactionItem item = new TransactionItem(
                            itemRs.getString("product_name"),
                            itemRs.getInt("qty"),
                            itemRs.getDouble("price")
                    );
                    t.addItem(item);
                }

                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
