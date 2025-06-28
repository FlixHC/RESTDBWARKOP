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
import java.util.ArrayList;
import java.util.List;

public class RuanganDAO {

    public static void insert(Ruangan r) {
        try (Connection conn = DB.connect()) {
            String sql = "INSERT INTO ruangan (kode_ruang, is_reserved) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, r.getKode_ruang());
            ps.setBoolean(2, r.isReserved());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Ruangan> getAll() {
        List<Ruangan> list = new ArrayList<>();
        try (Connection conn = DB.connect()) {
            String sql = "SELECT * FROM ruangan";
            ResultSet rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                Ruangan r = new Ruangan(
                        rs.getInt("id_ruang"),
                        rs.getString("kode_ruang"),
                        rs.getBoolean("is_reserved")
                );
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void updateStatus(int id, boolean isReserved) {
        try (Connection conn = DB.connect()) {
            String sql = "UPDATE ruangan SET is_reserved = ? WHERE id_ruang = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setBoolean(1, isReserved);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(int id) {
        try (Connection conn = DB.connect()) {
            String sql = "DELETE FROM ruangan WHERE id_ruang = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
