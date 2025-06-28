/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3;

/**
 *
 * @author HAK_PHENG
 */
public class Ruangan {
    private int id_ruang;
    private String kode_ruang;
    private boolean is_reserved;
    
    public Ruangan(int id_ruang, String kode_ruang, boolean is_reserved) {
        this.id_ruang = id_ruang;
        this.kode_ruang = kode_ruang;
        this.is_reserved = is_reserved;
    }

    // Getter and Setter for id_ruang
    public int getId_ruang() {
        return id_ruang;
    }

    public void setId_ruang(int id_ruang) {
        this.id_ruang = id_ruang;
    }

    // Getter and Setter for kode_ruang
    public String getKode_ruang() {
        return kode_ruang;
    }

    public void setKode_ruang(String kode_ruang) {
        this.kode_ruang = kode_ruang;
    }

    // Getter and Setter for is_reserved
    public boolean isReserved() {
        return is_reserved;
    }

    public void setReserved(boolean is_reserved) {
        this.is_reserved = is_reserved;
    }
}

