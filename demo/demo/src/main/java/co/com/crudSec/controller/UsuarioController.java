package co.com.crudSec.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.crudSec.dto.UsuarioDTO;
import co.com.crudSec.entity.Usuario;
import co.com.crudSec.service.i.IUsuarioService;


/**
 * <b>Descripción:</b> Clase UsuarioController que contiene todo el proceso de controller para garantizar el registro de un usuario
 * @author roger
 *
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
	
	@Autowired
	private IUsuarioService iUsuarioService;
	
	/**
	 * <b>Descripción:</b> Método que contiene toda la lógica necesaria para realizar su debido proceso de registro de un usuario
	 * @param usuario corresponde a los datos de usuario necesarios para el registro
	 * @author roger
	 *
	 */
	@PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody UsuarioDTO usuario) {
		return iUsuarioService.registrarUsuario(usuario);
    }
	
	
	/**
	 * <b>Descripción:</b> Método que contiene toda la lógica necesaria para realizar su debido proceso de modificación de un usuario
	 * @param usuario corresponde a los datos de usuario necesarios para la modificación
	 * @author roger
	 *
	 */
	@PostMapping("/modifica")
    public ResponseEntity<?> modificarUsuario(@RequestBody UsuarioDTO usuario){
		return iUsuarioService.modificarUsuario(usuario);
    }
	
	/**
	 * <b>Descripción:</b> Método que contiene lógica necesaria para realizar el llamado a su debido proceso de consultar a los usuarios 
	 * @author roger
	 */
	@GetMapping("/consulta")
	public List<Usuario> consultarUsuarios(){
		return iUsuarioService.consultarUsuarios();
	}
	
	/**
	 * <b>Descripción:</b> Método que contiene lógica necesaria para realizar el llamado a su debido proceso de eliminar usuario
	 * @param id dato de busqueda para la acción de eliminar
	 * @author roger
	 */
	@DeleteMapping("/elimina/{id}")
	public ResponseEntity<?> eliminaUsuario(@PathVariable UUID id){
		return iUsuarioService.eliminarUsuario(id);
	}
}
