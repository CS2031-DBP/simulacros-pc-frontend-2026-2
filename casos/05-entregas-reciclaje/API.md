# API · Registro de entregas para reciclaje

URL base: `http://localhost:3005`. Verifica con `GET /api/health`, que devuelve `{"status":"UP"}`.

## Autenticación

| Método y ruta | Cuerpo de ejemplo | Respuesta |
|---|---|---|
| `POST /api/auth/register` | `{"name":"Ana","email":"ana@example.test","password":"Practica123"}` | `201` con `name` y `email` |
| `POST /api/auth/login` | `{"email":"estudiante@example.test","password":"Practica123"}` | `200` con `token` |

También puedes usar `companero@example.test` con la misma contraseña. El registro requiere nombre de hasta 100 caracteres, correo válido de hasta 254 caracteres y contraseña de 8–72 caracteres ASCII. Envía `Content-Type: application/json` en solicitudes con cuerpo y `Authorization: Bearer <token>` en las rutas de negocio.

## Consultas

- `GET /api/collection-points?page=1&size=5` devuelve `{"items":[...],"page":1,"size":5,"totalItems":12,"totalPages":3}` con los registros del caso. La cuenta de prueba tiene 12 registros iniciales. La página empieza en 1. El tamaño admitido es de 1 a 50. Una página fuera del rango devuelve una lista vacía.
- `GET /api/collection-points/1` devuelve un registro con ID numérico y los datos del detalle.

Ejemplo de un elemento en `items`:

```json
{"id":1,"name":"Punto de acopio 1","zone":"Sector 2","status":"OPEN","materials":[{"id":1,"name":"Papel","active":true},{"id":2,"name":"Vidrio","active":false}]}
```

El detalle devuelve los mismos campos que un elemento de la lista.

## Operación principal

`POST /api/collection-points/1/deliveries` con el cuerpo `{"materialId":1,"weightKg":2.5}`. Devuelve `201` con el resultado. Muestra el material y peso confirmados. Si la API rechaza el material, conserva el peso ingresado.

Ejemplo de respuesta exitosa:

```json
{"id":1,"materialId":1,"material":"Papel","weightKg":2.5}
```

## Errores

El cuerpo siempre tiene `code` y `message` en los errores de la API documentados. Los estados son `400` (entrada inválida), `401` (sesión inválida o ausente), `404` (registro inexistente o inaccesible) y `409` (conflicto o correo ya registrado).

Para provocar el conflicto evaluado, parte de los datos iniciales. Cambia materialId a 2 para probar el material suspendido.

El conflicto evaluado devuelve el código `MATERIAL_SUSPENDED`. El token dura dos horas y se invalida al reiniciar el backend.
Para simular una selección obsoleta, reenvía la petición del formulario con el material suspendido desde las herramientas de desarrollo o simula esa respuesta en el cliente.

## Prueba rápida con curl

Inicia sesión y consulta la primera página. Copia el valor de `token` de la primera respuesta en la segunda petición.

```bash
curl -sS http://localhost:3005/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"estudiante@example.test","password":"Practica123"}'

curl -sS 'http://localhost:3005/api/collection-points?page=1&size=5' \
  -H 'Authorization: Bearer REEMPLAZA_CON_EL_TOKEN'
```
