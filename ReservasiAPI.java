/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3;

/**
 *
 * @author khouw
 */
import java.io.*;
import java.net.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class ReservasiAPI {
    private static final String BASE_URL = "http://localhost:4567/reservasi";
    private static final Gson gson = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss") // untuk parsing Date
        .create();

    public static List<Reservasi> fetchAll() throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL).openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            return gson.fromJson(reader, new TypeToken<List<Reservasi>>() {}.getType());
        }
    }

    public static void add(Reservasi reservasi) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(gson.toJson(reservasi).getBytes());
        }

        conn.getInputStream().close();
    }

    public static void update(Reservasi reservasi) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL + "/" + reservasi.getIdReservasi()).openConnection();
        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(gson.toJson(reservasi).getBytes());
        }

        conn.getInputStream().close();
    }

    public static void delete(int id) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL + "/" + id).openConnection();
        conn.setRequestMethod("DELETE");
        conn.getInputStream().close();
    }
}
