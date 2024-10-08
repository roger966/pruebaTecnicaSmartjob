/**
 * 
 */
package co.com.crudSec.service.i;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import co.com.crudSec.dto.UsuarioDTO;
import co.com.crudSec.entity.Usuario;

/**
 * <b>Descripción:</b> Interface IUsuarioService que contiene lógica necesaria para realizar el llamado a su debido proceso de Usuario
 * @author roger
 *
 */
public interface IUsuarioService {
	
	/**
	 * <b>Descripción:</b> Método que contiene lógica necesaria para realizar el llamado a su debido proceso de registrar usuario
	 * @param usuario corresponde a los datos de usuario necesarios para el registro
	 */
	public ResponseEntity<String> registrarUsuario(UsuarioDTO usuario);
	
	/**
	 * <b>Descripción:</b> Método que contiene lógica necesaria para realizar el llamado a su debido proceso de modificar usuario
	 * @param usuario corresponde a los datos de usuario necesarios para la modificación
	 */
	public ResponseEntity<String> modificarUsuario(UsuarioDTO usuario);
	
	/**
	 * <b>Descripción:</b> Método que contiene lógica necesaria para realizar el llamado a su debido proceso de consultar a los usuarios 
	 */
	public List<Usuario> consultarUsuarios();
	
	/**
	 * <b>Descripción:</b> Método que contiene lógica necesaria para realizar el llamado a su debido proceso de eliminar usuario
	 * @param id dato de busqueda para la acción de eliminar
	 */
	public ResponseEntity<String> eliminarUsuario(UUID id);

}
