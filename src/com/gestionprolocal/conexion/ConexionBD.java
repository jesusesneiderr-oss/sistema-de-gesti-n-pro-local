package com.gestionprolocal.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/gestionprolocal";
    private static final String USER = "root";
    private static final String PASSWORD = "jesusrojas18";

    public static Connection conectar() {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("======================================");
            System.out.println(" Conexión exitosa a GestiónPro Local ");
            System.out.println("======================================");

            return conexion;

        } catch (ClassNotFoundException e) {
            System.out.println("No se encontró el driver de MySQL.");
            e.printStackTrace();

        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos.");
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        Connection conexion = conectar();

        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada correctamente.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}