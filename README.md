# pruebaTecnicaSmartjob

Después de clonar y desplegar el repositorio

Se tiene una url para registrar un usuario bajo los parámetros establecidos en la documentación recibida por SmartJob.

Ejemplo para registrar un usuario
acceder a la siguiente url: http://localhost:8080/usuarios/registro  
la cual es de tipo post y sus datos de entrada corresponden a un json de cierta estructura:

{
    "name": "Juan Pepe",
    "email": "juanPP@gmail.com",
    "password": "aI1#",
    "phones": [
    {
    "number": "1234567",
    "citycode": "1",
    "contrycode": "57"
    }
    ]
}

después el api retornará algún mensaje correspondiente a creación o posible error contemplado.

Si se quiere realizar alguna modificación al usuario registrado, el api cuenta con una url para ello la cual es:
http://localhost:8080/usuarios/modifica
este también es de tipo post
este recibe todos los cambios requeridos que se necesiten modificar al usuario previamente registrado

{
    "id": "d81c4308-9a1e-4973-bc0d-d1fbb866e43d",
    "name": "Juan Pepe1",
    "email": "juanPP@gmail.com,
    "password": "aI1#",
    "phones": [
        {
        "number": "1234567",
        "citycode": "1",
        "countrycode": "57"
        }
    ]
}


También se cuenta con la acción de consultar todos los usuarios registrados, accediendo a la siguiente ruta:
http://localhost:8080/usuarios/consulta 
este es de tipo get

También permite eliminar un usuario registrado, este se eliminará brindando su id
accediendo a la siguiente ruta:
http://localhost:8080/usuarios/elimina/d81c4308-9a1e-4973-bc0d-d1fbb866e43d
este es de tipo delete


También cuenta con la opción de cambiar la contraseña, si desde un inicio no se ha registrado algún nivel de contraseña, el api tomará uno por defecto, el cual es
de tipo ALTO, con una cantidad de datos entre 4 a 16.
estos niveles se pueden modificar al igual que los valores mínimos y máximos exigidos
los niveles son:
BAJO: comprende solo números
MEDIO: comprende solo combinación de letras mayúsculas y minúsculas
ALTO: comprende combinación de letras, números y caracteres especial

para registrar una configuración de contraseña se accede a la siguiente ruta:
http://localhost:8080/configuraContrasenas/guardaContrasena/
este es de tipo post

ejemplo del dato a enviar: 4,8,MEDIO 
(corresponde a mínimo 4 dígitos, máximo 8 y que sea de nivel MEDIO)

para validar cual es la configuración de la contraseña se tiene la siguiente ruta:
http://localhost:8080/configuraContrasenas/consultaContrasena
este es de tipo get

