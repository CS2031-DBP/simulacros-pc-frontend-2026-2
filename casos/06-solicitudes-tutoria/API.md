# API · Actualización de solicitudes de tutoría

URL base: `http://localhost:3006`. Verifica con `GET /api/health`, que devuelve `{"status":"UP"}`.

## Autenticación

| Método y ruta | Cuerpo de ejemplo | Respuesta |
|---|---|---|
| `POST /api/auth/register` | `{"name":"Ana","email":"ana@example.test","password":"Practica123"}` | `201` con `name` y `email` |
| `POST /api/auth/login` | `{"email":"estudiante@example.test","password":"Practica123"}` | `200` con `token` |

También puedes usar `companero@example.test` con la misma contraseña. El registro requiere nombre de hasta 100 caracteres, correo válido de hasta 254 caracteres y contraseña de 8–72 caracteres ASCII. Envía `Content-Type: application/json` en solicitudes con cuerpo y `Authorization: Bearer <token>` en las rutas de negocio.

## Consultas

- `GET /api/tutoring-requests?page=1&size=5` devuelve `{"items":[...],"page":1,"size":5,"totalItems":11,"totalPages":3}` con los registros del caso. La cuenta de prueba tiene 11 registros iniciales. La página empieza en 1. El tamaño admitido es de 1 a 50. Una página fuera del rango devuelve una lista vacía.
- `GET /api/tutoring-requests/1` devuelve un registro con ID numérico y los datos del detalle.

Ejemplo de un elemento en `items`:

```json
{"id":1,"course":"Cálculo 1","topic":"Repaso de integrales","status":"PENDING","preferredSlotId":1,"preferredTime":"09:00"}
```

El detalle conserva esos campos y añade `availableSlots: [{ "id": 1, "startsAt": "09:00" }]`.

## Operación principal

`PATCH /api/tutoring-requests/1` con el cuerpo `{"topic":"Repaso de derivadas","preferredSlotId":2}`. Devuelve `200` con el resultado. Tras guardar, muestra los datos actualizados. Ante un conflicto, conserva los datos confirmados en el detalle.

Ejemplo de respuesta exitosa:

```json
{"id":1,"course":"Cálculo 1","topic":"Repaso de derivadas","status":"PENDING","preferredSlotId":2,"preferredTime":"15:00","availableSlots":[{"id":1,"startsAt":"09:00"},{"id":2,"startsAt":"15:00"}]}
```

## Errores

El cuerpo siempre tiene `code` y `message` en los errores de la API documentados. Los estados son `400` (entrada inválida), `401` (sesión inválida o ausente), `404` (registro inexistente o inaccesible) y `409` (conflicto o correo ya registrado).

Para provocar el conflicto evaluado, parte de los datos iniciales. Intenta editar la solicitud 2 con la cuenta de prueba.

El conflicto evaluado devuelve el código `REQUEST_CONFIRMED`. El token dura dos horas y se invalida al reiniciar el backend.

## Prueba rápida con curl

Inicia sesión y consulta la primera página. Copia el valor de `token` de la primera respuesta en la segunda petición.

```bash
curl -sS http://localhost:3006/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"estudiante@example.test","password":"Practica123"}'

curl -sS 'http://localhost:3006/api/tutoring-requests?page=1&size=5' \
  -H 'Authorization: Bearer REEMPLAZA_CON_EL_TOKEN'
```
