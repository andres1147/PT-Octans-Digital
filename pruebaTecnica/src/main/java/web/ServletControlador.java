package web;

import datos.UsuarioDaoJDBC;
import dominio.Usuario;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.swing.text.Document;

@WebServlet("/ServletControlador")
public class ServletControlador extends HttpServlet {

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
        //Recuperamos el idUsuario
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
        Usuario usuario = new UsuarioDaoJDBC().encontrar(new Usuario(idUsuario));
        request.setAttribute("usuario", usuario);

        String habilitar = "si";
        //String operacion = "editar";
        //request.setAttribute("operacion", operacion);
        request.setAttribute("habilitar", habilitar);

        //String jspEditar = "/WEB-INF/paginas/usuario/editarUsuario.jsp";
        //request.getRequestDispatcher(jspEditar).forward(request, response);
        request.getRequestDispatcher("usuarios.jsp").forward(request, response);
    }

    private void visualizarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Recuperamos el idUsuario
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
        Usuario usuario = new UsuarioDaoJDBC().encontrar(new Usuario(idUsuario));
        request.setAttribute("usuario", usuario);

        String habilitar = "no";
        request.setAttribute("habilitar", habilitar);

        //String jspEditar = "/WEB-INF/paginas/usuario/editarUsuario.jsp";
        //request.getRequestDispatcher(jspEditar).forward(request, response);
        request.getRequestDispatcher("usuarios.jsp").forward(request, response);
    }

    private void desbloquearInput(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String habilitar = "si";
        String operacion = "crear";
        request.setAttribute("habilitar", habilitar);
        request.setAttribute("operacion", operacion);
        request.getRequestDispatcher("usuarios.jsp").forward(request, response);
    }

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
        //int registrosModificados = new UsuarioDaoJDBC().listarConsulta(nombre);
        List<Usuario> usuarios = new UsuarioDaoJDBC().listarConsulta(nombre);
        HttpSession sesion = request.getSession();
        sesion.setAttribute("usuarios", usuarios);
        //request.getRequestDispatcher("usuarios.jsp").forward(request, response);
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
