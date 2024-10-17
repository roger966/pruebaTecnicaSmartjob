/**
 * 
 */
package co.com.crudSec.service.imp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import co.com.crudSec.dto.RegistraUsuarioDTO;
import co.com.crudSec.dto.TelefonoDTO;
import co.com.crudSec.dto.UsuarioDTO;
import co.com.crudSec.dto.enums.NivelContrasenaEnum;
import co.com.crudSec.entity.NivelContrasena;
import co.com.crudSec.entity.Telefono;
import co.com.crudSec.entity.Usuario;
import co.com.crudSec.repository.NivelContrasenaRepository;
import co.com.crudSec.repository.UsuarioRepository;
import co.com.crudSec.seguridad.PasswordUtil;
import co.com.crudSec.service.i.IUsuarioService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <b>Descripción:</b> Clase UsuarioService que contiene toda la lógica necesaria para realizar su debido proceso de registro
 * @author roger
 *
 */
@Service
public class UsuarioService implements IUsuarioService{

	@Autowired
    private UsuarioRepository usuarioRepository;
	@Autowired
	private NivelContrasenaRepository nivelContrasenaRepository;
	
    private static final String DEFAULT_PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%#$&/()=_*?&])([A-Za-z\\d$@$!%*?&]|[^ ]){4,16}$";											
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
   
    
    /**
     * <b>Descripción:</b> Método encargado de registrar un usuario con sus validaciones
     * @author roger
     *
     */
    public ResponseEntity<String> registrarUsuario(RegistraUsuarioDTO usuario) {
    	if(usuario.getName().isEmpty() || usuario.getEmail().isEmpty() || usuario.getPassword().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("{\"mensaje\": \"No es posible registrar por falta de datos\"}");
		}
		if(!validarCorreo(usuario.getEmail())){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("{\"mensaje\": \"El correo no cumple con el formato requerido\"}");
		}
		if(!validarPasword(usuario.getPassword())){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("{\"mensaje\": \"La contraseña no cumple con el formato requerido\"}");
		}
		Optional<Usuario> usuarioCorreo = usuarioRepository.findByEmail(usuario.getEmail());
		
		if(!usuarioCorreo.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("{\"mensaje\": \"El correo ya se encuentra registrado\"}");
		}
		
		Usuario usuarioEntidad=convertirDtoEntidadRegistro(usuario);
		usuarioEntidad.setCreated(new Date());
		usuarioEntidad.setModified(new Date());
		usuarioEntidad.setPassword(PasswordUtil.encodePassword(usuario.getPassword()));
		usuarioRepository.save(usuarioEntidad);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body("{\"mensaje\": \"Usuario creado éxitosamente\"}");
    }
    

	/**
     * <b>Descripción:</b> Método encargado de modificar un usuario con sus validaciones
     * @author roger
     *
     */
	public ResponseEntity<String> modificarUsuario(UsuarioDTO usuario) {
		Optional<Usuario> usuarioId = usuarioRepository.findById(usuario.getId());
		Optional<Usuario> usuarioCorreo = usuarioRepository.findByEmail(usuario.getEmail());
		if(usuarioId.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("{\"mensaje\": \"No se encontró usuario con ese id\"}");
		}
		if(usuario.getName().isEmpty() || usuario.getEmail().isEmpty() || usuario.getPassword().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("{\"mensaje\": \"No es posible registrar por falta de datos\"}");
		}
		if(!validarCorreo(usuario.getEmail())){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("{\"mensaje\": \"El correo no cumple con el formato requerido\"}");
		}
		if(!validarPasword(usuario.getPassword())){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("{\"mensaje\": \"La contraseña no cumple con el formato requerido\"}");
		}
		if(usuarioCorreo.isPresent() && usuarioCorreo.get().getId()!= usuario.getId()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("{\"mensaje\": \"El correo ya se encuentra registrado\"}");
		}
		Usuario usuarioEntidad=convertirDtoEntidad(usuario);
		usuarioEntidad.setId(usuario.getId());
		usuarioEntidad.setCreated(usuarioId.get().getCreated());
		usuarioEntidad.setModified(new Date());
		usuarioEntidad.setPassword(PasswordUtil.encodePassword(usuario.getPassword()));
		usuarioRepository.save(usuarioEntidad);
		return ResponseEntity.status(HttpStatus.OK)
				.body("{\"mensaje\": \"El usuario ha sido modificado éxitosamente\"}");
	}
	
	/**
	 * <b>Descripción:</b> Método que contiene lógica necesaria para realizar el debido proceso de consultar a los usuarios 
	 * @author roger
	 */
	public List<Usuario> consultarUsuarios(){
		List<Usuario> usuario = usuarioRepository.findAll();
		return usuario;
	}
	
	/**
	 * <b>Descripción:</b> Método que contiene lógica necesaria para realizar el debido proceso de eliminar usuario
	 * @param id dato de busqueda para la acción de eliminar
	 */
	public ResponseEntity<String> eliminarUsuario(UUID id){
		if(id == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("{\"mensaje\": \"No se suministró el id\"}");
		}
		Optional<Usuario> usuarioId = usuarioRepository.findById(id);
		if(usuarioId.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("{\"mensaje\": \"No se encontró usuario con ese id\"}");
		}
		usuarioRepository.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK)
				.body("{\"mensaje\": \"El usuario ha sido eliminado éxitosamente\"}");
	}


	/**
     * <b>Descripción:</b> Método encargado de convertir dto a entidad
     * @author roger
     *
     */
	private Usuario convertirDtoEntidad(UsuarioDTO usuario) {
		Usuario usuarioEntidad = new Usuario();
		List<Telefono> telefonos = new ArrayList<>();
		usuarioEntidad.setName(usuario.getName().trim());
		usuarioEntidad.setEmail(usuario.getEmail().trim());
		usuarioEntidad.setPassword(usuario.getPassword());
		usuarioEntidad.setLastLogin(new Date());
		usuarioEntidad.setToken(UUID.randomUUID().toString()); 
		usuarioEntidad.setActive(true);
	    if (usuario.getPhones() != null) {
	        for (TelefonoDTO telefonoDTO : usuario.getPhones()) {
	            Telefono telefono = new Telefono();
	            telefono.setNumber(telefonoDTO.getNumber().trim());
	            telefono.setCitycode(telefonoDTO.getCitycode().trim());
	            telefono.setContrycode(telefonoDTO.getContrycode().trim());
	            telefonos.add(telefono);
	        }
	    }
		usuarioEntidad.setPhones(telefonos);
		return usuarioEntidad;
	}

	
	/**
	 * <b>Descripción:</b> Método encargado de realizar la validación del correo
	 * @author roger
	 *
	 */
	private boolean validarCorreo(String correo) {
		if (correo == null || correo.isEmpty()) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(correo);
        return matcher.matches();
	}
	
	/**
	 * <b>Descripción:</b> Método encargado de realizar la validación de la contraseña la cual de contar con una combinación de mínimo 4
	 * carácteres y entre ellos debe existir una letra miniscula, mayuscula,número y un carácter especial.
	 * @author roger
	 *
	 */
	private boolean validarPasword(String password) {
		if (password == null || password.isEmpty()) {
            return false;
        }
		String password_REGEX = null ;
		Pattern PASSWORD_PATTERN = null;
		PASSWORD_PATTERN = Pattern.compile(identificarNivelContrasena(password_REGEX));
		Matcher matcher = PASSWORD_PATTERN.matcher(password);
        return matcher.matches();
	}

	
	/**
	 * <b>Descripción:</b> Método encargado de identificar el nivel de la contraseña la cual debe contar con una combinación de mínimo 4
	 * carácteres, este método permite la posibilidad de modificar el nivel de seguridad de la contraseña, puede ser BAJO, MEDIO o ALTO
	 * @author roger
	 *
	 */
	private String identificarNivelContrasena(String password_REGEX) {
		List<NivelContrasena> nivelContrasena = nivelContrasenaRepository.findAll();
		if(!nivelContrasena.isEmpty()) {
			if(nivelContrasena.get(0).getNivelContrasena().equals(NivelContrasenaEnum.BAJO.name())) {
				password_REGEX = NivelContrasenaEnum.BAJO.getCodigoMensaje() + "{" +
			            nivelContrasena.get(0).getCantidadMinima() + "," +
			            nivelContrasena.get(0).getCantidadMaxima() + "}$";
			}
			if(nivelContrasena.get(0).getNivelContrasena().equals(NivelContrasenaEnum.MEDIO.name())) {
				password_REGEX = NivelContrasenaEnum.MEDIO.getCodigoMensaje() + "{" +
			            nivelContrasena.get(0).getCantidadMinima() + "," +
			            nivelContrasena.get(0).getCantidadMaxima() + "}$";
			}
			if (nivelContrasena.get(0).getNivelContrasena().equals(NivelContrasenaEnum.ALTO.name())) {
				password_REGEX = NivelContrasenaEnum.ALTO.getCodigoMensaje() + "{" +
		            nivelContrasena.get(0).getCantidadMinima() + "," +
		            nivelContrasena.get(0).getCantidadMaxima() + "}$";
			}
		}else {
			password_REGEX = DEFAULT_PASSWORD_REGEX;					 
		}
		return password_REGEX;
	} 
	
	/**
     * <b>Descripción:</b> Método encargado de convertir dto a entidad para realizar el registro
     * @author roger
     *
     */
	private Usuario convertirDtoEntidadRegistro(RegistraUsuarioDTO usuario) {
		Usuario usuarioEntidad = new Usuario();
		List<Telefono> telefonos = new ArrayList<>();
		usuarioEntidad.setName(usuario.getName().trim());
		usuarioEntidad.setEmail(usuario.getEmail().trim());
		usuarioEntidad.setPassword(usuario.getPassword());
		usuarioEntidad.setLastLogin(new Date());
		usuarioEntidad.setToken(UUID.randomUUID().toString()); 
		usuarioEntidad.setActive(true);
	    if (usuario.getPhones() != null) {
	        for (TelefonoDTO telefonoDTO : usuario.getPhones()) {
	            Telefono telefono = new Telefono();
	            telefono.setNumber(telefonoDTO.getNumber().trim());
	            telefono.setCitycode(telefonoDTO.getCitycode().trim());
	            telefono.setContrycode(telefonoDTO.getContrycode().trim());
	            telefonos.add(telefono);
	        }
	    }
		usuarioEntidad.setPhones(telefonos);
		return usuarioEntidad;
	}
}
