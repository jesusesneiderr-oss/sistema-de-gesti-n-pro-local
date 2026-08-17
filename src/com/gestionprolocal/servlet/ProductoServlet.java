package com.gestionprolocal.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.gestionprolocal.dao.ProductoDAO;
import com.gestionprolocal.modelo.Producto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet que expone las operaciones CRUD de Producto como un servicio web.
 * Escucha en la ruta /productos y responde en formato JSON.
 *
 * Métodos soportados:
 *  GET    /productos        -> lista todos los productos
 *  POST   /productos        -> crea un nuevo producto
 *  PUT    /productos        -> actualiza un producto existente
 *  DELETE /productos?id=X   -> elimina el producto con ese id
 */
@WebServlet("/productos")
public class ProductoServlet extends HttpServlet {

    private final ProductoDAO productoDAO = new ProductoDAO();

    private void configurarCabeceras(HttpServletResponse response) {
        response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    private String productoAJson(Producto p) {
        return "{"
                + "\"id\":" + p.getId() + ","
                + "\"nombre\":\"" + p.getNombre() + "\","
                + "\"precio\":" + p.getPrecio() + ","
                + "\"stock\":" + p.getStock()
                + "}";
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        configurarCabeceras(response);
        PrintWriter salida = response.getWriter();

        List<Producto> productos = productoDAO.consultarProductos();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < productos.size(); i++) {
            json.append(productoAJson(productos.get(i)));
            if (i < productos.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");

        salida.print(json);
        salida.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        configurarCabeceras(response);
        PrintWriter salida = response.getWriter();

        String nombre = request.getParameter("nombre");
        double precio = Double.parseDouble(request.getParameter("precio"));
        int stock = Integer.parseInt(request.getParameter("stock"));

        Producto nuevoProducto = new Producto(nombre, precio, stock);
        boolean exito = productoDAO.registrarProducto(nuevoProducto);

        if (exito) {
            response.setStatus(HttpServletResponse.SC_CREATED);
            salida.print("{\"mensaje\":\"Producto registrado correctamente.\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            salida.print("{\"mensaje\":\"Error al registrar el producto.\"}");
        }

        salida.flush();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        configurarCabeceras(response);
        PrintWriter salida = response.getWriter();

        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        double precio = Double.parseDouble(request.getParameter("precio"));
        int stock = Integer.parseInt(request.getParameter("stock"));

        Producto producto = new Producto(id, nombre, precio, stock);
        boolean exito = productoDAO.actualizarProducto(producto);

        if (exito) {
            salida.print("{\"mensaje\":\"Producto actualizado correctamente.\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            salida.print("{\"mensaje\":\"No se encontró el producto a actualizar.\"}");
        }

        salida.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        configurarCabeceras(response);
        PrintWriter salida = response.getWriter();

        int id = Integer.parseInt(request.getParameter("id"));
        boolean exito = productoDAO.eliminarProducto(id);

        if (exito) {
            salida.print("{\"mensaje\":\"Producto eliminado correctamente.\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            salida.print("{\"mensaje\":\"No se encontró el producto a eliminar.\"}");
        }

        salida.flush();
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
        configurarCabeceras(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}