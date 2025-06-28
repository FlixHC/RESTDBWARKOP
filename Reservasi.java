package com.mycompany.mavenproject3;

import java.util.Date;
import java.time.LocalTime;

public class Reservasi {
    private int idReservasi;
    private String customerName;
    private Date tanggalReservasi;
    private LocalTime masaWaktuReservasi;
    private String detailReservasi;

    public Reservasi(int idReservasi, String customerName, Date tanggalReservasi, LocalTime masaWaktuReservasi, String detailReservasi) {
        this.idReservasi = idReservasi;
        this.customerName = customerName;
        this.tanggalReservasi = tanggalReservasi;
        this.masaWaktuReservasi = masaWaktuReservasi;
        this.detailReservasi = detailReservasi;
    }

    public int getIdReservasi() {
        return idReservasi;
    }

    public void setIdReservasi(int idReservasi) {
        this.idReservasi = idReservasi;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getTanggalReservasi() {
        return tanggalReservasi;
    }

    public void setTanggalReservasi(Date tanggalReservasi) {
        this.tanggalReservasi = tanggalReservasi;
    }

    public LocalTime getMasaWaktuReservasi() {
        return masaWaktuReservasi;
    }

    public void setMasaWaktuReservasi(LocalTime masaWaktuReservasi) {
        this.masaWaktuReservasi = masaWaktuReservasi;
    }

    public String getDetailReservasi() {
        return detailReservasi;
    }

    public void setDetailReservasi(String detailReservasi) {
        this.detailReservasi = detailReservasi;
    }
}
