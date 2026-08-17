package com.gestionprolocal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gestionprolocal.conexion.ConexionBD;
import com.gestionprolocal.modelo.Producto;

/**
 * Clase DAO (Data Access Object) encargada de gestionar las operaciones
 * CRUD (Crear, Consultar, Actualizar, Eliminar) sobre la tabla "productos".
 */
public class ProductoDAO {

    // ================= CREAR =================
    public boolean registrarProducto(Producto producto) {

        String sql = "INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?)";

        try {

            Connection conexion = ConexionBD.conectar();

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getStock());

            ps.executeUpdate();

            System.out.println("Producto registrado correctamente.");

            ps.close();
            conexion.close();

            return true;

        } catch (SQLException e) {

            System.out.println("Error al registrar el producto.");
            e.printStackTrace();

            return false;
        }
    }

    // ================= CONSULTAR (todos) =================
    public List<Producto> consultarProductos() {

        List<Producto> listaProductos = new ArrayList<>();
        String sql = "SELECT id, nombre, precio, stock FROM productos";

        try {

            Connection conexion = ConexionBD.conectar();

            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Producto producto = new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                );

                listaProductos.add(producto);
            }

            rs.close();
            ps.close();
            conexion.close();

        } catch (SQLException e) {

            System.out.println("Error al consultar los productos.");
            e.printStackTrace();
        }

        return listaProductos;
    }

    // ================= ACTUALIZAR =================
    public boolean actualizarProducto(Producto producto) {

        String sql = "UPDATE productos SET nombre = ?, precio = ?, stock = ? WHERE id = ?";

        try {

            Connection conexion = ConexionBD.conectar();

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getStock());
            ps.setInt(4, producto.getId());

            int filasAfectadas = ps.executeUpdate();

            ps.close();
            conexion.close();

            if (filasAfectadas > 0) {
                System.out.println("Producto actualizado correctamente.");
                return true;
            } else {
                System.out.println("No se encontró el producto a actualizar.");
                return false;
            }

        } catch (SQLException e) {

            System.out.println("Error al actualizar el producto.");
            e.printStackTrace();

            return false;
        }
    }

    // ================= ELIMINAR =================
    public boolean eliminarProducto(int id) {

        String sql = "DELETE FROM productos WHERE id = ?";

        try {

            Connection conexion = ConexionBD.conectar();

            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);

            int filasAfectadas = ps.executeUpdate();

            ps.close();
            conexion.close();

            if (filasAfectadas > 0) {
                System.out.println("Producto eliminado correctamente.");
                return true;
            } else {
                System.out.println("No se encontró el producto a eliminar.");
                return false;
            }

        } catch (SQLException e) {

            System.out.println("Error al eliminar el producto.");
            e.printStackTrace();

            return false;
        }
    }
}