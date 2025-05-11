package org.diplom.dormitory.service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ApiService {
    public static String authenticate(String mail, String password) {
        try {
            URL url = new URL("http://localhost:8080/api/staff/authenticate?mail=" + mail + "&password=" + password);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                Scanner scanner = new Scanner(conn.getInputStream());
                return scanner.hasNext() ? scanner.nextLine() : null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

