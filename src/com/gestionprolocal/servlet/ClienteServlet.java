package com.gestionprolocal.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.gestionprolocal.dao.ClienteDAO;
import com.gestionprolocal.modelo.Cliente;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet que expone las operaciones CRUD de Cliente como un servicio web.
 * Escucha en la ruta /clientes y responde en formato JSON.
 *
 * Métodos soportados:
 *  GET    /clientes        -> lista todos los clientes
 *  POST   /clientes        -> crea un nuevo cliente
 *  PUT    /clientes        -> actualiza un cliente existente
 *  DELETE /clientes?id=X   -> elimina el cliente con ese id
 */
@WebServlet("/clientes")
public class ClienteServlet extends HttpServlet {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    // Permite que el frontend (aunque esté en otro origen) consuma este servicio
    private void configurarCabeceras(HttpServletResponse response) {
        response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    // Convierte un objeto Cliente a texto JSON
    private String clienteAJson(Cliente c) {
        return "{"
                + "\"id\":" + c.getId() + ","
                + "\"nombre\":\"" + c.getNombre() + "\","
                + "\"telefono\":\"" + c.getTelefono() + "\","
                + "\"correo\":\"" + c.getCorreo() + "\""
                + "}";
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        configurarCabeceras(response);
        PrintWriter salida = response.getWriter();

        List<Cliente> clientes = clienteDAO.consultarClientes();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < clientes.size(); i++) {
            json.append(clienteAJson(clientes.get(i)));
            if (i < clientes.size() - 1) {
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
        String telefono = request.getParameter("telefono");
        String correo = request.getParameter("correo");

        Cliente nuevoCliente = new Cliente(nombre, telefono, correo);
        boolean exito = clienteDAO.registrarCliente(nuevoCliente);

        if (exito) {
            response.setStatus(HttpServletResponse.SC_CREATED);
            salida.print("{\"mensaje\":\"Cliente registrado correctamente.\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            salida.print("{\"mensaje\":\"Error al registrar el cliente.\"}");
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
        String telefono = request.getParameter("telefono");
        String correo = request.getParameter("correo");

        Cliente cliente = new Cliente(id, nombre, telefono, correo);
        boolean exito = clienteDAO.actualizarCliente(cliente);

        if (exito) {
            salida.print("{\"mensaje\":\"Cliente actualizado correctamente.\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            salida.print("{\"mensaje\":\"No se encontró el cliente a actualizar.\"}");
        }

        salida.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        configurarCabeceras(response);
        PrintWriter salida = response.getWriter();

        int id = Integer.parseInt(request.getParameter("id"));
        boolean exito = clienteDAO.eliminarCliente(id);

        if (exito) {
            salida.print("{\"mensaje\":\"Cliente eliminado correctamente.\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            salida.print("{\"mensaje\":\"No se encontró el cliente a eliminar.\"}");
        }

        salida.flush();
    }

    // Necesario para que los navegadores permitan PUT/DELETE desde JS (preflight CORS)
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
        configurarCabeceras(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}