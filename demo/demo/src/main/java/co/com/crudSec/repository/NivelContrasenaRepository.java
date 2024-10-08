/**
 * 
 */
package co.com.crudSec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.com.crudSec.entity.NivelContrasena;

/**
 * <b>Descripcion:</b> interface NivelContrasenaRepository que contiene la interacción con bd para sus respectivas validaciones
 * @author roger
 *
 */
@Repository
public interface NivelContrasenaRepository extends JpaRepository<NivelContrasena, Long>{

}
