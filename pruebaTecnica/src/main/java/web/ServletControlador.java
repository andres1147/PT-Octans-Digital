package web;

import datos.UsuarioDaoJDBC;
import dominio.Usuario;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.swing.text.Document;

/**
 * Esta clase controla los jsp a partir de las reglas de negocio
 *
 * @author: Andres Garces
 * @version: 19/08/2021
 */
@WebServlet("/ServletControlador")
public class ServletControlador extends HttpServlet {

    /**
     * Controla la interaccion de las acciones enviadas por GET de los botones
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "visualizar":
                    this.visualizarUsuario(request, response);
                    break;
                case "crear":
                    this.desbloquearInput(request, response);
                    break;
                case "resetear":
                    this.accionDefault(request, response);
                    break;
                case "editar":
                    this.editarUsuario(request, response);
                    break;
                case "eliminar":
                    this.eliminarUsuario(request, response);
                    break;
                default:
                    this.accionDefault(request, response);
            }
        } else {
            this.accionDefault(request, response);
        }
    }

     /**
     * Ejecuta el comportamiento de todos los componentes al principio de la ejecucion
     */
    private void accionDefault(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Usuario> usuarios = new UsuarioDaoJDBC().listar();
        System.out.println("usuarios: " + usuarios);
        HttpSession sesion = request.getSession();
        sesion.setAttribute("usuarios", usuarios);
        //request.getRequestDispatcher("usuarios.jsp").forward(request, response);
        response.sendRedirect("usuarios.jsp");
    }

    private void editarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String funcionGuardar = "modificar";
        //Recuperamos el idUsuario
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
        Usuario usuario = new UsuarioDaoJDBC().encontrar(new Usuario(idUsuario));
        request.setAttribute("usuario", usuario);

        request.setAttribute("funcionGuardar", funcionGuardar);

        this.validarFormulario(request, response, usuario);

        String habilitar = "si";
        request.setAttribute("habilitar", habilitar);

        request.getRequestDispatcher("usuarios.jsp").forward(request, response);
    }

    private void validarFormulario(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {

        String rolSelect = "";
        String checkbox;

        //Bloque validacion checkbox
        if (usuario.getActivo() == 'S') {
            checkbox = "si";
        } else {
            checkbox = "no";
        }
        request.setAttribute("checkbox", checkbox);
        //Fin bloque validacion checkbox

        //Bloque validacion select
        switch (usuario.getIdRol()) {
            case 1:
                rolSelect = "administrador";
            case 2:
                rolSelect = "auditor";
            case 3:
                rolSelect = "auxiliar";
        }
        request.setAttribute("rolSelect", rolSelect);
        //Fin bloque validacion select
    }

    private void visualizarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
        Usuario usuario = new UsuarioDaoJDBC().encontrar(new Usuario(idUsuario));

        this.validarFormulario(request, response, usuario);

        request.setAttribute("usuario", usuario);

        String habilitar = "no";
        String ver = "si";
        request.setAttribute("habilitar", habilitar);
        request.setAttribute("ver", ver);

        request.getRequestDispatcher("usuarios.jsp").forward(request, response);
    }

    private void desbloquearInput(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String habilitar = "si";
        String funcionGuardar = "guardar";
        request.setAttribute("habilitar", habilitar);
        request.setAttribute("funcionGuardar", funcionGuardar);
        request.getRequestDispatcher("usuarios.jsp").forward(request, response);
    }

     /**
     * Controla la interaccion de las acciones enviadas por POST de los botones
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion != null) {
            switch (accion) {
                case "insertar":
                    this.insertarUsuario(request, response);
                    break;
                case "consultar":
                    this.consultarUsuario(request, response);
                    break;
                case "modificar":
                    this.modificarUsuario(request, response);
                    break;
                default:
                    this.accionDefault(request, response);
            }
        } else {
            this.accionDefault(request, response);
        }
    }

    private void insertarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Recuperamos los valores del formulario agregarUsuario
        String nombre = request.getParameter("nombre");
        String rolString = request.getParameter("rol");
        String activoString = request.getParameter("activo");

        int idRol = Integer.parseInt(rolString);
        char activo = activoString.charAt(0);
        //Falta hacer la validacion de que no se repita el nombre alla en la base de datos

        //Creamos el objeto de usuario(modelo)
        Usuario usuario = new Usuario(idRol, nombre, activo);

        //Insertamos el nuevo objeto en la base de datos
        boolean repetido = new UsuarioDaoJDBC().consultarNombre(nombre);

        if (!repetido) {
            int registrosModificados = new UsuarioDaoJDBC().insertar(usuario);
        }
        //Redirigimos hacia la accion por default
        this.accionDefault(request, response);
    }

    private void consultarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Recuperamos los valores del formulario agregarUsuario
        String nombre = request.getParameter("nombre");

        //Insertamos el nuevo objeto en la base de datos
        List<Usuario> usuarios = new UsuarioDaoJDBC().listarConsulta(nombre);
        HttpSession sesion = request.getSession();
        sesion.setAttribute("usuarios", usuarios);
        response.sendRedirect("usuarios.jsp");
    }

    private void modificarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Recuperamos los valores del formulario editarUsuario
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
        String nombre = request.getParameter("nombre");
        String rolString = request.getParameter("rol");
        String activoString = request.getParameter("activo");

        int idRol = Integer.parseInt(rolString);
        char activo = activoString.charAt(0);
        //Falta hacer la validacion de que no se repita el nombre alla en la base de datos

        //Creamos el objeto de usuario(modelo)
        Usuario usuario = new Usuario(idUsuario, idRol, nombre, activo);

        //Mofificamos el nuevo objeto en la base de datos
        int registrosModificados = new UsuarioDaoJDBC().actualizar(usuario);
        System.out.println("registrosModificados" + registrosModificados);

        //Redirigimos hacia la accion por default
        this.accionDefault(request, response);
    }

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Recuperamos los valores del formulario editarUsuario
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));

        //Creamos el objeto de usuario(modelo)
        Usuario usuario = new Usuario(idUsuario);

        //Eliminamos el nuevo objeto en la base de datos
        int registrosModificados = new UsuarioDaoJDBC().eliminar(usuario);
        System.out.println("registrosModificados" + registrosModificados);

        //Redirigimos hacia la accion por default
        this.accionDefault(request, response);
    }

}
