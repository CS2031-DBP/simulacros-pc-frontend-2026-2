# API · Préstamo de juegos de mesa

URL base: `http://localhost:3004`. Verifica con `GET /api/health`, que devuelve `{"status":"UP"}`.

## Autenticación

| Método y ruta | Cuerpo de ejemplo | Respuesta |
|---|---|---|
| `POST /api/auth/register` | `{"name":"Ana","email":"ana@example.test","password":"Practica123"}` | `201` con `name` y `email` |
| `POST /api/auth/login` | `{"email":"estudiante@example.test","password":"Practica123"}` | `200` con `token` |

También puedes usar `companero@example.test` con la misma contraseña. El registro requiere nombre de hasta 100 caracteres, correo válido de hasta 254 caracteres y contraseña de 8–72 caracteres ASCII. Envía `Content-Type: application/json` en solicitudes con cuerpo y `Authorization: Bearer <token>` en las rutas de negocio.

## Consultas

- `GET /api/games?page=1&size=5` devuelve `{"items":[...],"page":1,"size":5,"totalItems":12,"totalPages":3}` con los registros del caso. La cuenta de prueba tiene 12 registros iniciales. La página empieza en 1. El tamaño admitido es de 1 a 50. Una página fuera del rango devuelve una lista vacía.
- `GET /api/games/1` devuelve un registro con ID numérico y los datos del detalle.

Ejemplo de un elemento en `items`:

```json
{"id":1,"name":"Ajedrez","category":"Estrategia","recommendedPlayers":2,"availableUnits":1}
```

El detalle devuelve los mismos campos que un elemento de la lista.

## Operación principal

`POST /api/games/1/loans` con el cuerpo `{"days":2}`. Devuelve `201` con el resultado. Tras confirmar, actualiza las unidades disponibles.

Ejemplo de respuesta exitosa:

```json
{"gameId":1,"days":2,"availableUnits":0}
```

## Errores

El cuerpo siempre tiene `code` y `message` en los errores de la API documentados. Los estados son `400` (entrada inválida), `401` (sesión inválida o ausente), `404` (registro inexistente o inaccesible) y `409` (conflicto o correo ya registrado).

Para provocar el conflicto evaluado, parte de los datos iniciales. Solicita dos veces el juego 1, que tiene una sola unidad.

El conflicto evaluado devuelve el código `OUT_OF_STOCK`. El token dura dos horas y se invalida al reiniciar el backend.

## Prueba rápida con curl

Inicia sesión y consulta la primera página. Copia el valor de `token` de la primera respuesta en la segunda petición.

```bash
curl -sS http://localhost:3004/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"estudiante@example.test","password":"Practica123"}'

curl -sS 'http://localhost:3004/api/games?page=1&size=5' \
  -H 'Authorization: Bearer REEMPLAZA_CON_EL_TOKEN'
```
