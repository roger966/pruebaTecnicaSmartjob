/**
 * 
 */
package co.com.crudSec.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import co.com.crudSec.dto.UsuarioDTO;
import co.com.crudSec.dto.enums.NivelContrasenaEnum;
import co.com.crudSec.entity.NivelContrasena;
import co.com.crudSec.entity.Usuario;
import co.com.crudSec.repository.NivelContrasenaRepository;
import co.com.crudSec.repository.UsuarioRepository;
import co.com.crudSec.service.imp.UsuarioService;

/**
 * <b>Descripción:</b> Clase UsuarioServiceTest que contiene todo el proceso de controller para garantizar el registro de un usuario
 * @author roger
 *
 */
public class UsuarioServiceTest {
	
	@InjectMocks
	private UsuarioService usuarioService;
	@Mock
	private UsuarioRepository usuarioRepository;
	@Mock
	private NivelContrasenaRepository nivelContrasenaRepository;
	@BeforeEach
	public void setUp() {
    	MockitoAnnotations.openMocks(this);
    }
	
	
	/**
	 * Test testFallidoPorFaltaDeDatos()  de forma fallida ya que no cumple con los datos necesarios para registrar usuario
	 * 
	 */
	@Test
	public void testFallidoPorFaltaDeDatos() {
		UsuarioDTO usuario = new UsuarioDTO();
		usuario.setName("");
		ResponseEntity<String> exception = usuarioService.registrarUsuario(usuario);
        usuarioService.registrarUsuario(usuario);
		assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("{\"mensaje\": \"No es posible registrar por falta de datos\"}", exception.getBody());
	}
	
	/**
	 * Test testGuardar() de forma éxitosa que valida el debido proceso de registrar usuario
	 * 
	 */
	@Test
	public void testGuardar() {
		UsuarioDTO usuario = new UsuarioDTO();
		usuario.setName("Roger");
		usuario.setEmail("roger@gmail.com");
		usuario.setPassword("1Qp#");
		Usuario uEntidad = new Usuario();
		uEntidad.setActive(true);
		uEntidad.setName(usuario.getName());
		uEntidad.setPassword(usuario.getPassword());
		uEntidad.setEmail(usuario.getEmail());
		when(usuarioRepository.save(any(Usuario.class))).thenReturn(uEntidad);
		NivelContrasena nivelContrasena = new NivelContrasena();
		nivelContrasena.setId(1L);
		nivelContrasena.setCantidadMinima(4L);
		nivelContrasena.setCantidadMaxima(16L);
		nivelContrasena.setNivelContrasena(NivelContrasenaEnum.ALTO.name());
		when(nivelContrasenaRepository.save(any(NivelContrasena.class))).thenReturn(nivelContrasena);		
		ResponseEntity<String> response = usuarioService.registrarUsuario(usuario);
		assertEquals("{\"mensaje\": \"Usuario creado éxitosamente\"}", response.getBody());
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}
	
	/**
	 * Test testFallidoPorMail() de forma fallida ya que el mail correspondiente de registrar usuario no cumple los requerimientos
	 * 
	 */
	@Test
	public void testFallidoPorMail() {
		UsuarioDTO usuario = new UsuarioDTO();
		usuario.setName("Juan");
		usuario.setEmail("juanjuan.com");
		usuario.setPassword("456asX*");
		ResponseEntity<String> exception = usuarioService.registrarUsuario(usuario);
        usuarioService.registrarUsuario(usuario);
		assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("{\"mensaje\": \"El correo no cumple con el formato requerido\"}", exception.getBody());
	}
	
	/**
	 * Test testFallidoModificarPorId() de forma fallida ya que no hay id de usuario correspondiente para modificar al usuario
	 * 
	 */
	@Test
	public void testFallidoModificarPorId() {
		UsuarioDTO usuario = new UsuarioDTO();
		usuario.setName("Roger");
		usuario.setEmail("roger@gmail.com");
		usuario.setPassword("1Qp#");
		Usuario uEntidad = new Usuario();
		uEntidad.setActive(true);
		uEntidad.setName(usuario.getName());
		uEntidad.setPassword(usuario.getPassword());
		uEntidad.setEmail(usuario.getEmail());
		when(usuarioRepository.save(any(Usuario.class))).thenReturn(uEntidad);
		ResponseEntity<String> response = usuarioService.modificarUsuario(usuario);
		assertEquals("{\"mensaje\": \"No se encontró usuario con ese id\"}", response.getBody());
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	
	/**
	 * Test testExitosoModificarUsuario() de forma éxitosa para modificar al usuario
	 * 
	 */
	@Test
	public void testFallidoEliminarUsuarioPorId() {
		ResponseEntity<String> response = usuarioService.eliminarUsuario(UUID.fromString("40bff7be-98a3-4ce6-b136-662c0c46afa1"));
		assertEquals("{\"mensaje\": \"No se encontró usuario con ese id\"}", response.getBody());
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	/**
	 * Test testExitosoModificarUsuario() de forma éxitosa para modificar al usuario
	 * 
	 */
	@Test
	public void testFallidoEliminarAusenciaUsuario() {
		UsuarioDTO usuario = new UsuarioDTO();
		ResponseEntity<String> response = usuarioService.eliminarUsuario(usuario.getId());
		assertEquals("{\"mensaje\": \"No se suministró el id\"}", response.getBody());
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	/**
	 * Test testConsultarUsuario() de forma éxitosa para consultar a los usuarios
	 * 
	 */
	@Test
	public void testConsultarUsuario() {
		Usuario usuario = new Usuario();
		usuario.setName("Roger");
		usuario.setEmail("roger@gmail.com");
		usuario.setPassword("1Qp#");
		usuario.setCreated(new Date());
		Usuario uEntidad = new Usuario();
		uEntidad.setActive(true);
		uEntidad.setName(usuario.getName());
		uEntidad.setPassword(usuario.getPassword());
		uEntidad.setEmail(usuario.getEmail());
		uEntidad.setCreated(usuario.getCreated());
		when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));
		List<Usuario>listaUsuario= usuarioService.consultarUsuarios();
		assertNotNull(listaUsuario);
	}
}
