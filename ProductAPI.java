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
import com.google.gson.reflect.TypeToken;
import com.google.gson.*;

public class ProductAPI {
    private static final String BASE_URL = "http://localhost:4567/products"; // ganti sesuai backend kamu
    private static final Gson gson = new Gson();

    public static List<Product> fetchAll() throws IOException {
        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            return gson.fromJson(reader, new TypeToken<List<Product>>() {}.getType());
        }
    }

    public static void add(Product product) throws IOException {
        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(gson.toJson(product).getBytes());
        }

        conn.getInputStream().close();
    }

    public static void update(Product product) throws IOException {
        URL url = new URL(BASE_URL + "/" + product.getId());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(gson.toJson(product).getBytes());
        }

        conn.getInputStream().close();
    }

    public static void delete(int id) throws IOException {
        URL url = new URL(BASE_URL + "/" + id);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.getInputStream().close();
    }
}
