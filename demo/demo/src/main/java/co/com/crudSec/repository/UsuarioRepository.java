/**
 * 
 */
package co.com.crudSec.repository;

/**
 * <b>Descripcion:</b> interface UsuarioRepository que contiene la interacción con bd para sus respectivas validaciones
 * @author roger
 *
 */
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.crudSec.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID>{
	Optional<Usuario> findByCorreo(String correo);

}
