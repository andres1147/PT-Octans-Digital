package datos;

import dominio.Usuario;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase es la que interactua directamente con la base de datos
 *
 * @author: Andres Garces
 * @version: 19/08/2021
 */
public class UsuarioDaoJDBC {

    private static final String SQL_SELECT = "SELECT id_usuario, id_rol, nombre, activo"
            + " FROM usuario";

    private static final String SQL_SELECT_BY_ID = "SELECT id_usuario, id_rol, nombre, activo"
            + " FROM usuario WHERE id_usuario = ?";

    private static final String SQL_SELECT_BY_NAME_LIKE = "SELECT id_usuario, id_rol, nombre, activo"
            + " FROM usuario WHERE nombre LIKE '%of%'";

    private static final String SQL_SELECT_BY_NAME = "SELECT id_usuario, id_rol, nombre, activo"
            + " FROM usuario WHERE nombre = ?";

    private static final String SQL_INSERT = "INSERT INTO usuario(id_rol, nombre, activo) "
            + " VALUES(?, ?, ?)";

    private static final String SQL_UPDATE = "UPDATE usuario"
            + " SET id_rol=?, nombre=?, activo=? WHERE id_usuario=?";

    private static final String SQL_DELETE = "DELETE FROM usuario WHERE id_usuario=?";

    /**
     * Devuelve todos los registros de la tabla usuarios
     *
     * @return la lista de todos los usuarios
     */
    public List<Usuario> listar() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;
        List<Usuario> usuarios = new ArrayList<>();
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int idUsuario = rs.getInt("id_usuario");
                int idRol = rs.getInt("id_rol");
                String nombre = rs.getString("nombre");
                //CUIDADO ACA, VER COMO SE HACE EL TRATAMIENTO PARA EL CHAR
                char activo = (rs.getString("activo")).charAt(0);

                usuario = new Usuario(idUsuario, idRol, nombre, activo);
                usuarios.add(usuario);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return usuarios;
    }

    /**
     * Realiza el filtro de todos los registros de la tabla usuario por
     * parametro establecido
     *
     * @param nombreBuscar El parámetro nombreBuscar define la cadena para
     * ejecutar la consulta LIKE y encontrar coincidencias
     * @return la lista filtrada de los usuarios
     */
    public List<Usuario> listarConsulta(String nombreBuscar) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;
        List<Usuario> usuarios = new ArrayList<>();
        try {
            conn = Conexion.getConnection();

            String nombreConsultar = "'%" + nombreBuscar + "%'";
            stmt = conn.prepareStatement("SELECT id_usuario, id_rol, nombre, activo"
                    + " FROM usuario WHERE nombre LIKE " + nombreConsultar);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int idUsuario = rs.getInt("id_usuario");
                int idRol = rs.getInt("id_rol");
                String nombre = rs.getString("nombre");
                char activo = (rs.getString("activo")).charAt(0);

                usuario = new Usuario(idUsuario, idRol, nombre, activo);
                usuarios.add(usuario);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return usuarios;
    }

    /**
     * Devuelve un usuario en especifico
     *
     * @param usuario Objeto para extraer el id y encontrar el registro en
     * especifico
     * @return usuario en especifico
     */
    public Usuario encontrar(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            stmt.setInt(1, usuario.getIdUsuario());
            rs = stmt.executeQuery();

            while (rs.next()) {
                int idRol = rs.getInt("id_rol");
                String nombre = rs.getString("nombre");
                char activo = (rs.getString("activo")).charAt(0);
                usuario.setIdRol(idRol);
                usuario.setNombre(nombre);
                usuario.setActivo(activo);

            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(rs);
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return usuario;
    }

    /**
     * Inserta un usuario a la tabla usuario
     *
     * @param usuario Objeto para extraer todos sus datos y entregarlos como
     * parametros de la consulta SQL
     * @return filas afectadas
     */
    public int insertar(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setInt(1, usuario.getIdRol());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, String.valueOf(usuario.getActivo()));

            rows = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return rows;
    }

    /**
     * Modifica un usuario de la tabla usuario
     *
     * @param usuario Objeto para trabajar con todos sus datos, en excepcion del
     * id
     * @return filas afectadas
     */
    public int actualizar(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setInt(1, usuario.getIdRol());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, String.valueOf(usuario.getActivo()));
            stmt.setInt(4, usuario.getIdUsuario());

            rows = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return rows;
    }

    /**
     * Consulta coincidencias de un nombre en la tabla usuario
     *
     * @param nombreBuscar Cadena para filtrar la busqueda
     * @return true si ese nombre existe
     */
    public boolean consultarNombre(String nombreBuscar) {
        boolean existe = true;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BY_NAME);
            stmt.setString(1, nombreBuscar);
            rs = stmt.executeQuery();

            if (rs.next()) {
                existe = true;
            } else {
                existe = false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return existe;
    }

    /**
     * Elimina un registro de la tabla usuario
     *
     * @param usuario Objeto para extraer el id y eliminar el registro en
     * especifico
     * @return true si ese nombre existe
     */
    public int eliminar(Usuario usuario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int rows = 0;
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setInt(1, usuario.getIdUsuario());

            rows = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            Conexion.close(stmt);
            Conexion.close(conn);
        }
        return rows;
    }

}
