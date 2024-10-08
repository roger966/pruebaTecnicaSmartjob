/**
 * 
 */
package co.com.crudSec.service.i;

import java.util.List;

import org.springframework.http.ResponseEntity;

import co.com.crudSec.entity.NivelContrasena;

/**
 * <b>Descripción:</b> Interface INivelContrasenaService que contiene lógica necesaria para realizar el llamado a su debido proceso de configurar contraseña
 * @author roger
 *
 */
public interface INivelContrasenaService {

	public ResponseEntity<String> guardarConfiguracionContrasena(String dato);
	
	public List<NivelContrasena> consultarConfiguracion();
}
