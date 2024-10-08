/**
 * 
 */
package co.com.crudSec.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import co.com.crudSec.dto.enums.NivelContrasenaEnum;
import co.com.crudSec.entity.NivelContrasena;
import co.com.crudSec.repository.NivelContrasenaRepository;
import co.com.crudSec.service.i.INivelContrasenaService;

/**
 * <b>Descripción:</b> Clase NivelContrasenaService que contiene toda la lógica necesaria para realizar su debido proceso de nivel de contraseña
 * @author roger
 *
 */
@Service
public class NivelContrasenaService implements INivelContrasenaService{
	@Autowired
    private NivelContrasenaRepository nivelContrasenaRepository;
	
	public ResponseEntity<String> guardarConfiguracionContrasena(String dato) {
		Long minimo,maximo;
		if(dato == null) {
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("{\"mensaje\": \"No es posible configurar la contraseña, no se recibieron datos\"}");
		}
		nivelContrasenaRepository.deleteAll();
		String[] datos=dato.split(",");
		minimo=Long.parseLong(datos[0]);
		maximo=Long.parseLong(datos[1]);
		if(minimo>=maximo) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("{\"mensaje\": \"La cantidad mínima no puede ser mayor o igual que la cantidad máxima\"}");
		}
		if(minimo<4||maximo>16) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("{\"mensaje\": \"El rango de las cantidades mínima y máxima no son congruentes\"}");
		}
		if(!(datos[2].equals(String.valueOf(NivelContrasenaEnum.BAJO))||
				datos[2].equals(String.valueOf(NivelContrasenaEnum.MEDIO))||
				datos[2].equals(String.valueOf(NivelContrasenaEnum.ALTO)))) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("{\"mensaje\": \"Nivel de contraseña errado\"}");
		}
		NivelContrasena nivelContrasena = new NivelContrasena();
		nivelContrasena.setCantidadMinima(Long.parseLong(datos[0]));
		nivelContrasena.setCantidadMaxima(Long.parseLong(datos[1]));
		nivelContrasena.setNivelContrasena(String.valueOf(datos[2]));
		nivelContrasenaRepository.save(nivelContrasena);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body("{\"mensaje\": \"Configuracion de contraseña registrada éxitosamente\"}");
	}

	@Override
	public List<NivelContrasena> consultarConfiguracion() {
		List<NivelContrasena> nivelContrasena;
		nivelContrasena=nivelContrasenaRepository.findAll();
		return nivelContrasena;
	}

}
