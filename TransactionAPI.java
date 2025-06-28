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

public class TransactionAPI {
    private static final String BASE_URL = "http://localhost:4567/transactions"; // ganti sesuai servermu
    private static final Gson gson = new Gson();

    public static List<Transaction> fetchAll() throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL).openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            return gson.fromJson(reader, new TypeToken<List<Transaction>>(){}.getType());
        }
    }

    public static void add(Transaction transaction) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(BASE_URL).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        String json = gson.toJson(transaction);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes());
        }

        conn.getInputStream().close(); // just to trigger request
    }
}