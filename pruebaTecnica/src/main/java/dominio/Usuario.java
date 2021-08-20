package dominio;

/**
 * Esta clase define el dominio de la solucion de software
 *
 * @author: Andres Garces
 * @version: 19/08/2021
 */
public class Usuario {

    private int idUsuario;
    private int idRol;
    private String nombre;
    private char activo;

    public Usuario() {
    }

    /**
     * Constructor para Usuario, se va a utilizar para indicar que registro se eliminara
     * @param idUsuario El parámetro numeroItems define el identificador unico del usuario
     */
    public Usuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

     /**
     * Constructor para Usuario, se va a utilizar para la edicion de un registro
     * @param idRol
     * @param nombre
     * @param activo
     */
    public Usuario(int idRol, String nombre, char activo) {
        this.idRol = idRol;
        this.nombre = nombre;
        this.activo = activo;
    }

     /**
     * Constructor para Usuario, se va a utilizar para la creacion de un nuevo registro
     * @param idUsuario El parámetro numeroItems define el identificador unico del usuario
     * @param idRol
     * @param nombre
     * @param activo
     * 
     */
    public Usuario(int idUsuario, int idRol, String nombre, char activo) {
        this.idUsuario = idUsuario;
        this.idRol = idRol;
        this.nombre = nombre;
        this.activo = activo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public char getActivo() {
        return activo;
    }

    public void setActivo(char activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Usuario{" + "idUsuario=" + idUsuario + ", idRol=" + idRol + ", nombre=" + nombre + ", activo=" + activo + '}';
    }

}
