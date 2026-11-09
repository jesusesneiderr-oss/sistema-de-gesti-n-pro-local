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
 * Servlet encargado de exponer las operaciones CRUD de productos
 * mediante un servicio web.
 *
 * Ruta:
 * /productos
 *
 * Operaciones:
 * GET    -> consultar productos
 * POST   -> registrar producto
 * PUT    -> actualizar producto
 * DELETE -> eliminar producto
 */
@WebServlet("/productos")
public class ProductoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ProductoDAO productoDAO = new ProductoDAO();

    private void configurarCabeceras(HttpServletResponse response) {

        response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    private String productoAJson(Producto producto) {

        return "{"
                + "\"id\":" + producto.getId() + ","
                + "\"nombre\":\"" + producto.getNombre() + "\","
                + "\"precio\":" + producto.getPrecio() + ","
                + "\"stock\":" + producto.getStock()
                + "}";
    }

    /**
     * Lee el cuerpo de la petición (formato x-www-form-urlencoded)
     * y lo convierte en un mapa clave-valor. Necesario porque el
     * servidor solo parsea automáticamente el body como formulario
     * en peticiones POST, no en PUT.
     */
    private java.util.Map<String, String> leerParametrosDelBody(
            HttpServletRequest request) throws IOException {

        java.util.Map<String, String> parametros = new java.util.HashMap<>();

        StringBuilder cuerpo = new StringBuilder();

        try (java.io.BufferedReader lector = request.getReader()) {

            String linea;

            while ((linea = lector.readLine()) != null) {
                cuerpo.append(linea);
            }
        }

        for (String par : cuerpo.toString().split("&")) {

            String[] partes = par.split("=", 2);

            if (partes.length == 2) {

                String clave = java.net.URLDecoder.decode(partes[0], "UTF-8");
                String valor = java.net.URLDecoder.decode(partes[1], "UTF-8");

                parametros.put(clave, valor);
            }
        }

        return parametros;
    }

    /**
     * GET /productos
     *
     * Consulta todos los productos registrados.
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
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

        response.setStatus(HttpServletResponse.SC_OK);

        salida.print(json.toString());

        salida.flush();
    }

    /**
     * POST /productos
     *
     * Registra un nuevo producto.
     *
     * Parámetros:
     * nombre
     * precio
     * stock
     */
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        configurarCabeceras(response);

        PrintWriter salida = response.getWriter();

        String nombre = request.getParameter("nombre");
        String precioParametro = request.getParameter("precio");
        String stockParametro = request.getParameter("stock");

        if (nombre == null || nombre.trim().isEmpty()
                || precioParametro == null || precioParametro.trim().isEmpty()
                || stockParametro == null || stockParametro.trim().isEmpty()) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            salida.print(
                    "{\"mensaje\":\"Nombre, precio y stock son obligatorios.\"}"
            );

            salida.flush();

            return;
        }

        double precio;
        int stock;

        try {
            precio = Double.parseDouble(precioParametro.trim());
            stock = Integer.parseInt(stockParametro.trim());
        } catch (NumberFormatException e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            salida.print(
                    "{\"mensaje\":\"Precio o stock con formato invalido.\"}"
            );

            salida.flush();

            return;
        }

        Producto nuevoProducto = new Producto(nombre.trim(), precio, stock);

        boolean exito = productoDAO.registrarProducto(nuevoProducto);

        if (exito) {

            response.setStatus(HttpServletResponse.SC_CREATED);

            salida.print(
                    "{\"mensaje\":\"Producto registrado correctamente.\"}"
            );

        } else {

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            salida.print(
                    "{\"mensaje\":\"Error al registrar el producto.\"}"
            );
        }

        salida.flush();
    }

    /**
     * PUT /productos
     *
     * Actualiza un producto existente.
     *
     * Parámetros (leídos del body, no con getParameter):
     * id
     * nombre
     * precio
     * stock
     */
    @Override
    protected void doPut(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        configurarCabeceras(response);

        PrintWriter salida = response.getWriter();

        /*
         * IMPORTANTE: el servidor solo parsea automáticamente el
         * body como formulario (getParameter) en peticiones POST.
         * En PUT, request.getParameter() siempre devuelve null
         * aunque el body venga bien formado, por lo que leemos y
         * parseamos el body manualmente.
         */
        java.util.Map<String, String> datosBody = leerParametrosDelBody(request);

        String idParametro = datosBody.get("id");
        String nombre = datosBody.get("nombre");
        String precioParametro = datosBody.get("precio");
        String stockParametro = datosBody.get("stock");

        if (idParametro == null || idParametro.trim().isEmpty()) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            salida.print(
                    "{\"mensaje\":\"El parametro id es obligatorio.\"}"
            );

            salida.flush();

            return;
        }

        if (nombre == null || nombre.trim().isEmpty()
                || precioParametro == null || precioParametro.trim().isEmpty()
                || stockParametro == null || stockParametro.trim().isEmpty()) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            salida.print(
                    "{\"mensaje\":\"Nombre, precio y stock son obligatorios.\"}"
            );

            salida.flush();

            return;
        }

        int id;
        double precio;
        int stock;

        try {
            id = Integer.parseInt(idParametro.trim());
            precio = Double.parseDouble(precioParametro.trim());
            stock = Integer.parseInt(stockParametro.trim());
        } catch (NumberFormatException e) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            salida.print(
                    "{\"mensaje\":\"id, precio o stock con formato invalido.\"}"
            );

            salida.flush();

            return;
        }

        Producto producto = new Producto(id, nombre.trim(), precio, stock);

        boolean exito = productoDAO.actualizarProducto(producto);

        if (exito) {

            response.setStatus(HttpServletResponse.SC_OK);

            salida.print(
                    "{\"mensaje\":\"Producto actualizado correctamente.\"}"
            );

        } else {

            response.setStatus(HttpServletResponse.SC_NOT_FOUND);

            salida.print(
                    "{\"mensaje\":\"No se encontro el producto a actualizar.\"}"
            );
        }

        salida.flush();
    }

    /**
     * DELETE /productos?id=X
     *
     * Elimina un producto mediante su ID.
     */
    @Override
    protected void doDelete(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        configurarCabeceras(response);

        PrintWriter salida = response.getWriter();

        String idParametro = request.getParameter("id");

        if (idParametro == null || idParametro.trim().isEmpty()) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

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

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            salida.print(
                    "{\"mensaje\":\"El parametro id debe ser un numero entero.\"}"
            );

            salida.flush();

            return;
        }

        boolean exito = productoDAO.eliminarProducto(id);

        if (exito) {

            response.setStatus(HttpServletResponse.SC_OK);

            salida.print(
                    "{\"mensaje\":\"Producto eliminado correctamente.\"}"
            );

        } else {

            response.setStatus(HttpServletResponse.SC_NOT_FOUND);

            salida.print(
                    "{\"mensaje\":\"No se encontro el producto a eliminar.\"}"
            );
        }

        salida.flush();
    }

    /**
     * OPTIONS
     *
     * Permite las solicitudes preliminares CORS utilizadas por
     * navegadores antes de PUT y DELETE.
     */
    @Override
    protected void doOptions(
            HttpServletRequest request,
            HttpServletResponse response) {

        configurarCabeceras(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}