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
 * Servlet encargado de exponer las operaciones CRUD de clientes
 * mediante un servicio web.
 *
 * Ruta:
 * /clientes
 *
 * Operaciones:
 * GET    -> consultar clientes
 * POST   -> registrar cliente
 * PUT    -> actualizar cliente
 * DELETE -> eliminar cliente
 */
@WebServlet("/clientes")
public class ClienteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ClienteDAO clienteDAO = new ClienteDAO();

    /**
     * Configura las cabeceras de las respuestas HTTP.
     */
    private void configurarCabeceras(HttpServletResponse response) {

        response.setContentType("application/json; charset=UTF-8");

        response.setHeader("Access-Control-Allow-Origin", "*");

        response.setHeader(
                "Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS"
        );

        response.setHeader(
                "Access-Control-Allow-Headers",
                "Content-Type"
        );
    }

    /**
     * Convierte un objeto Cliente a formato JSON.
     */
    private String clienteAJson(Cliente cliente) {

        return "{"
                + "\"id\":" + cliente.getId() + ","
                + "\"nombre\":\"" + cliente.getNombre() + "\","
                + "\"telefono\":\"" + cliente.getTelefono() + "\","
                + "\"correo\":\"" + cliente.getCorreo() + "\""
                + "}";
    }

    /**
     * GET /clientes
     *
     * Consulta todos los clientes registrados.
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
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

        response.setStatus(HttpServletResponse.SC_OK);

        salida.print(json.toString());

        salida.flush();
    }

    /**
     * POST /clientes
     *
     * Registra un nuevo cliente.
     *
     * Parámetros:
     * nombre
     * telefono
     * correo
     */
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        configurarCabeceras(response);

        PrintWriter salida = response.getWriter();

        String nombre = request.getParameter("nombre");
        String telefono = request.getParameter("telefono");
        String correo = request.getParameter("correo");

        // Validar campos obligatorios
        if (nombre == null || nombre.trim().isEmpty()
                || telefono == null || telefono.trim().isEmpty()
                || correo == null || correo.trim().isEmpty()) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            salida.print(
                    "{\"mensaje\":\"Nombre, telefono y correo son obligatorios.\"}"
            );

            salida.flush();

            return;
        }

        Cliente nuevoCliente =
                new Cliente(
                        nombre.trim(),
                        telefono.trim(),
                        correo.trim()
                );

        boolean exito =
                clienteDAO.registrarCliente(nuevoCliente);

        if (exito) {

            response.setStatus(HttpServletResponse.SC_CREATED);

            salida.print(
                    "{\"mensaje\":\"Cliente registrado correctamente.\"}"
            );

        } else {

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            salida.print(
                    "{\"mensaje\":\"Error al registrar el cliente.\"}"
            );
        }

        salida.flush();
    }

    /**
     * PUT /clientes
     *
     * Actualiza un cliente existente.
     *
     * Parámetros:
     * id
     * nombre
     * telefono
     * correo
     */
    @Override
    protected void doPut(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        configurarCabeceras(response);

        PrintWriter salida = response.getWriter();

        /*
         * Primero obtenemos el ID como String.
         * Esto evita que Integer.parseInt() reciba null.
         */
        String idParametro = request.getParameter("id");

        String nombre = request.getParameter("nombre");
        String telefono = request.getParameter("telefono");
        String correo = request.getParameter("correo");

        // ==========================================
        // VALIDAR ID
        // ==========================================

        if (idParametro == null || idParametro.trim().isEmpty()) {

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST
            );

            salida.print(
                    "{\"mensaje\":\"El parametro id es obligatorio.\"}"
            );

            salida.flush();

            return;
        }

        int id;

        try {

            id = Integer.parseInt(idParametro.trim());

        } catch (NumberFormatException e) {

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST
            );

            salida.print(
                    "{\"mensaje\":\"El parametro id debe ser un numero entero.\"}"
            );

            salida.flush();

            return;
        }

        // ==========================================
        // VALIDAR DATOS DEL CLIENTE
        // ==========================================

        if (nombre == null || nombre.trim().isEmpty()) {

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST
            );

            salida.print(
                    "{\"mensaje\":\"El nombre es obligatorio.\"}"
            );

            salida.flush();

            return;
        }

        if (telefono == null || telefono.trim().isEmpty()) {

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST
            );

            salida.print(
                    "{\"mensaje\":\"El telefono es obligatorio.\"}"
            );

            salida.flush();

            return;
        }

        if (correo == null || correo.trim().isEmpty()) {

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST
            );

            salida.print(
                    "{\"mensaje\":\"El correo es obligatorio.\"}"
            );

            salida.flush();

            return;
        }

        // ==========================================
        // CREAR OBJETO CLIENTE
        // ==========================================

        Cliente cliente =
                new Cliente(
                        id,
                        nombre.trim(),
                        telefono.trim(),
                        correo.trim()
                );

        // ==========================================
        // ACTUALIZAR CLIENTE
        // ==========================================

        boolean exito =
                clienteDAO.actualizarCliente(cliente);

        if (exito) {

            response.setStatus(
                    HttpServletResponse.SC_OK
            );

            salida.print(
                    "{\"mensaje\":\"Cliente actualizado correctamente.\"}"
            );

        } else {

            response.setStatus(
                    HttpServletResponse.SC_NOT_FOUND
            );

            salida.print(
                    "{\"mensaje\":\"No se encontro el cliente a actualizar.\"}"
            );
        }

        salida.flush();
    }

    /**
     * DELETE /clientes?id=X
     *
     * Elimina un cliente mediante su ID.
     */
    @Override
    protected void doDelete(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        configurarCabeceras(response);

        PrintWriter salida = response.getWriter();

        String idParametro = request.getParameter("id");

        // Validar ID
        if (idParametro == null || idParametro.trim().isEmpty()) {

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST
            );

            salida.print(
                    "{\"mensaje\":\"El parametro id es obligatorio.\"}"
            );

            salida.flush();

            return;
        }

        int id;

        try {

            id = Integer.parseInt(idParametro.trim());

        } catch (NumberFormatException e) {

            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST
            );

            salida.print(
                    "{\"mensaje\":\"El parametro id debe ser un numero entero.\"}"
            );

            salida.flush();

            return;
        }

        boolean exito =
                clienteDAO.eliminarCliente(id);

        if (exito) {

            response.setStatus(
                    HttpServletResponse.SC_OK
            );

            salida.print(
                    "{\"mensaje\":\"Cliente eliminado correctamente.\"}"
            );

        } else {

            response.setStatus(
                    HttpServletResponse.SC_NOT_FOUND
            );

            salida.print(
                    "{\"mensaje\":\"No se encontro el cliente a eliminar.\"}"
            );
        }

        salida.flush();
    }

    /**
     * OPTIONS
     *
     * Permite las solicitudes preliminares CORS
     * utilizadas por navegadores antes de PUT y DELETE.
     */
    @Override
    protected void doOptions(
            HttpServletRequest request,
            HttpServletResponse response) {

        configurarCabeceras(response);

        response.setStatus(
                HttpServletResponse.SC_OK
        );
    }
}