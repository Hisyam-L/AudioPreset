/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connector {
    private static final String URL = "jdbc:mysql://localhost:3306/db_audio_pbo";
    private static final String USER = "root";
    private static final String PASS = "admin123"; 

    // Method statis biar gampang dipanggil tanpa harus bikin object (instansiasi) berkali-kali
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
