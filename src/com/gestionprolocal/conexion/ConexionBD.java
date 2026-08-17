package com.gestionprolocal.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // Datos de conexión a MySQL
    private static final String URL =
            "jdbc:mysql://localhost:3306/gestionprolocal";

    private static final String USER = "root";

    private static final String PASSWORD = "jesusrojas18";

    /**
     * Establece la conexión entre Java y MySQL.
     */
    public static Connection conectar() {

        try {

            // Cargar el controlador JDBC de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establecer conexión
            Connection conexion = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

            System.out.println("======================================");
            System.out.println(" CONEXIÓN EXITOSA A GESTIÓNPRO LOCAL ");
            System.out.println("======================================");

            return conexion;

        } catch (ClassNotFoundException e) {

            System.out.println("======================================");
            System.out.println(" ERROR: NO SE ENCONTRÓ MYSQL CONNECTOR/J ");
            System.out.println("======================================");

            e.printStackTrace();

        } catch (SQLException e) {

            System.out.println("======================================");
            System.out.println(" ERROR AL CONECTAR CON MYSQL ");
            System.out.println("======================================");

            e.printStackTrace();
        }

        return null;
    }

    /**
     * Prueba la conexión directamente.
     */
    public static void main(String[] args) {

        Connection conexion = conectar();

        if (conexion != null) {

            try {

                conexion.close();

                System.out.println(
                        "Conexión cerrada correctamente."
                );

            } catch (SQLException e) {

                System.out.println(
                        "Error al cerrar la conexión."
                );

                e.printStackTrace();
            }
        }
    }
}