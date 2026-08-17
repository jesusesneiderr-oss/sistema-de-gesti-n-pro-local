package com.gestionprolocal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gestionprolocal.conexion.ConexionBD;
import com.gestionprolocal.modelo.Cliente;

/**
 * Clase DAO (Data Access Object) encargada de gestionar las operaciones
 * CRUD (Crear, Consultar, Actualizar, Eliminar) sobre la tabla "clientes".
 */
public class ClienteDAO {

    // ================= CREAR =================
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

    // ================= CONSULTAR (todos) =================
    public List<Cliente> consultarClientes() {

        List<Cliente> listaClientes = new ArrayList<>();
        String sql = "SELECT id, nombre, telefono, correo FROM clientes";

        try {

            Connection conexion = ConexionBD.conectar();

            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Cliente cliente = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("correo")
                );

                listaClientes.add(cliente);
            }

            rs.close();
            ps.close();
            conexion.close();

        } catch (SQLException e) {

            System.out.println("Error al consultar los clientes.");
            e.printStackTrace();
        }

        return listaClientes;
    }

    // ================= ACTUALIZAR =================
    public boolean actualizarCliente(Cliente cliente) {

        String sql = "UPDATE clientes SET nombre = ?, telefono = ?, correo = ? WHERE id = ?";

        try {

            Connection conexion = ConexionBD.conectar();

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getTelefono());
            ps.setString(3, cliente.getCorreo());
            ps.setInt(4, cliente.getId());

            int filasAfectadas = ps.executeUpdate();

            ps.close();
            conexion.close();

            if (filasAfectadas > 0) {
                System.out.println("Cliente actualizado correctamente.");
                return true;
            } else {
                System.out.println("No se encontró el cliente a actualizar.");
                return false;
            }

        } catch (SQLException e) {

            System.out.println("Error al actualizar el cliente.");
            e.printStackTrace();

            return false;
        }
    }

    // ================= ELIMINAR =================
    public boolean eliminarCliente(int id) {

        String sql = "DELETE FROM clientes WHERE id = ?";

        try {

            Connection conexion = ConexionBD.conectar();

            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);

            int filasAfectadas = ps.executeUpdate();

            ps.close();
            conexion.close();

            if (filasAfectadas > 0) {
                System.out.println("Cliente eliminado correctamente.");
                return true;
            } else {
                System.out.println("No se encontró el cliente a eliminar.");
                return false;
            }

        } catch (SQLException e) {

            System.out.println("Error al eliminar el cliente.");
            e.printStackTrace();

            return false;
        }
    }
}