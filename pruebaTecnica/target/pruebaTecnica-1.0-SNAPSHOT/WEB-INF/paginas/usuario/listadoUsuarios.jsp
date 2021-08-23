<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<section id="usuarios" class="py-4 mb-4 bg-light">
    <div class="container">
        <div class="row">

            <div class="col-md-5">

                <div class="card mb-3">
                    <div class="card-header bg-primary text-white">
                        <h4>Parametros de busqueda</h4>
                    </div>

                    <form action="${pageContext.request.contextPath}/ServletControlador?accion=consultar"
                          method="POST">

                        <div class="mx-2 py-2 mb-2">
                            <button class="btn btn-primary" type="submit">Consultar</button>
                            <a href="${pageContext.request.contextPath}/ServletControlador?accion=resetear"
                               class="btn btn-primary" type="reset">Limpiar
                            </a>
                        </div>
                        <div class="card-body">
                            <div class="form-group">
                                <label for="nombre">Nombre</label>
                                <input type="text" class="form-control" name="nombre" required>
                            </div>
                        </div>
                    </form>

                </div> 
            </div> 
            <div class="col-md-7">
                <div class="card mb-6">
                    <div class="card-header bg-primary text-white">
                        <h4>Listado de Usuarios</h4>
                    </div>
                    <div class="mx-2 py-2 mb-2">
                        <a href="${pageContext.request.contextPath}/ServletControlador?accion=crear" 
                           class="btn btn-primary">
                            Crear
                        </a>
                    </div>

                    <table class="table table-striped">
                        <thead class="thead-dark">
                            <tr>
                                <th>#</th>
                                <th>ID Rol</th>
                                <th>Nombre</th>
                                <th>Activo</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- Iteramos cada elemento de la lista de usuarios-->
                            <c:forEach var="usuario" items="${usuarios}" varStatus="status">
                                <tr>
                                    <td>${usuario.idUsuario}</td>
                                    <td>${usuario.idRol}</td>
                                    <td>${usuario.nombre}</td>
                                    <td>${usuario.activo}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/ServletControlador?accion=visualizar&idUsuario=${usuario.idUsuario}"
                                           class="btn btn-secondary">Ver
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>    
        </div>
    </div>
</section>

<section  class="py-4 mb-4 bg-light">
    <div class="container mb-3">
        <div class="row">
            <div class="col-md-9">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h4>Informacion de Usuario</h4>
                    </div>
                    <form action="${pageContext.request.contextPath}/ServletControlador?accion=${funcionGuardar eq 'guardar' ? 'insertar' : 'modificar&idUsuario='}${usuario.idUsuario}"
                          method="POST" class="was-validated">

                        <div class="mx-2 py-2 mb-2">

                            <button class="btn btn-primary" type="submit" ${habilitar eq 'si' ? '' : 'disabled' }>Guardar</button>
                            <a data-role="button" type="button" href="${pageContext.request.contextPath}/ServletControlador?accion=editar&idUsuario=${usuario.idUsuario}"
                               class="btn btn-primary" ${ver eq 'si' ? '' : 'style="pointer-events: none; background-color:#62A1FE"'}>Editar
                            </a>
                            <a data-role="button" type="button" href="${pageContext.request.contextPath}/ServletControlador?accion=eliminar&idUsuario=${usuario.idUsuario}" 
                               class="btn btn-primary" ${ver eq 'si' ? '' : 'style="pointer-events: none; background-color:#62A1FE"'}>Eliminar
                            </a>
                        </div>

                        <div class="card-body">
                            <div class="form-group">
                                <label for="id">Id</label>
                                <input type="number" class="form-control" name="id" required value="${usuario.idUsuario}"
                                       readonly="readonly">
                            </div>
                            <div class="form-group">
                                <label for="nombre">Nombre</label>
                                <input type="text" class="form-control" name="nombre" required value="${usuario.nombre}"
                                       ${habilitar eq 'si' ? '' : 'disabled' }>
                            </div>

                            <div class="form-group">
                                <label for="rol">Rol</label>
                                <select class="form-select" name="rol" aria-label="Default select example" required value="${usuario.idRol}"
                                        ${habilitar eq 'si' ? '' : 'disabled' }>
                                    <option >Open this select menu</option>
                                    <option ${rolSelect eq 'administrador' ? 'selected' : '' } value="1" >Administrador</option>
                                    <option ${rolSelect eq 'auditor' ? 'selected' : '' } value="2" >Auditor</option>
                                    <option ${rolSelect eq 'auxiliar' ? 'selected' : '' } value="3" >Auxiliar</option>
                                </select>

                            </div>
                            <div class="form-group">
                                <label for="activo" >Activo</label>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="activo" id="inlineRadio1" value="S"
                                           ${habilitar eq 'si' ? '' : 'disabled'} ${checkbox eq 'si' ? 'checked="checked"' : '' }>
                                    <label class="form-check-label" for="activo">S</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <input class="form-check-input" type="radio" name="activo" id="inlineRadio2" value="N"
                                           ${habilitar eq 'si' ? '' : 'disabled'} ${checkbox eq 'no' ? 'checked="checked"' : '' }>
                                    <label class="form-check-label" for="activo" >N</label>
                                </div>
                            </div>
                        </div>
                    </form>

                </div>
            </div>
        </div>
    </div>
</section>



