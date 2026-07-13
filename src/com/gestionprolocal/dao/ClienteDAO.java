package com.gestionprolocal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.gestionprolocal.conexion.ConexionBD;
import com.gestionprolocal.modelo.Cliente;

public class ClienteDAO {

    public boolean registrarCliente(Cliente cliente) {

        String sql = "INSERT INTO clientes (nombre, telefono, correo) VALUES (?, ?, ?)";

        try {

            Connection conexion = ConexionBD.conectar();

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getTelefono());
            ps.setString(3, cliente.getCorreo());

            ps.executeUpdate();

            System.out.println("Cliente registrado correctamente.");

            ps.close();
            conexion.close();

            return true;

        } catch (SQLException e) {

            System.out.println("Error al registrar el cliente.");
            e.printStackTrace();

            return false;
        }
    }
}