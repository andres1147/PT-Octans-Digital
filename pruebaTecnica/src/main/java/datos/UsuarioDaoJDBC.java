package datos;

import dominio.Usuario;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public List<Usuario> listarConsulta(String nombreBuscar) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = null;
        List<Usuario> usuarios = new ArrayList<>();
        try {
            conn = Conexion.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BY_NAME_LIKE);
            //String nombreConsultar = "'%" + nombreBuscar + "%'";
            //String nombreConsultar = "'%of%'";
            //stmt.setString(1, nombreConsultar);
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
