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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservasiDAO {

    public static List<Reservasi> getAll() {
        List<Reservasi> list = new ArrayList<>();
        try (Connection conn = DB.connect()) {
            String sql = "SELECT * FROM reservasi";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                list.add(new Reservasi(
                        rs.getInt("id_reservasi"),
                        rs.getString("customer_name"),
                        rs.getDate("tanggal_reservasi"),
                        rs.getTime("masa_waktu_reservasi").toLocalTime(),
                        rs.getString("detail_reservasi")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void save(Reservasi r) {
        try (Connection conn = DB.connect()) {
            String sql = "INSERT INTO reservasi (customer_name, tanggal_reservasi, masa_waktu_reservasi, detail_reservasi) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, r.getCustomerName());
            ps.setDate(2, (Date) r.getTanggalReservasi());
            ps.setTime(3, Time.valueOf(r.getMasaWaktuReservasi()));
            ps.setString(4, r.getDetailReservasi());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(int id) {
        try (Connection conn = DB.connect()) {
            String sql = "DELETE FROM reservasi WHERE id_reservasi = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void update(Reservasi r) {
        try (Connection conn = DB.connect()) {
            String sql = "UPDATE reservasi SET customer_name = ?, tanggal_reservasi = ?, masa_waktu_reservasi = ?, detail_reservasi = ? WHERE id_reservasi = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, r.getCustomerName());
            ps.setDate(2, (Date) r.getTanggalReservasi());
            ps.setTime(3, Time.valueOf(r.getMasaWaktuReservasi()));
            ps.setString(4, r.getDetailReservasi());
            ps.setInt(5, r.getIdReservasi());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}