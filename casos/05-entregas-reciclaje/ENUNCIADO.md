# Simulacro 05 · Registro de entregas para reciclaje

**Pregunta práctica · 14 puntos**

Implementa una aplicación web para **registro de entregas para reciclaje**. Usa React, TypeScript, React Router, Axios y Tailwind. Consume el backend provisto según [API.md](API.md).

## 1. Autenticación y navegación · 3 puntos

- **Registro · 1 punto.** En `/register`, solicita nombre, correo y contraseña. Exige un nombre de hasta 100 caracteres, correo válido y una contraseña de 8 a 72 caracteres ASCII. Después del registro, dirige al login.
- **Inicio de sesión · 1 punto.** En `/login`, solicita correo y contraseña. Incluye un enlace al registro y muestra un mensaje si las credenciales son incorrectas. Guarda el JWT en `localStorage`, envíalo como Bearer en las peticiones protegidas y dirige al listado después del login.
- **Protección y cierre manual · 1 punto.** Restringe el listado y el detalle a usuarios con sesión. Incluye una acción de cierre de sesión que limpie el token y vuelva al login.

## 2. Listado · 4 puntos

- **Consulta y campos · 1.5 puntos.** En `/collection-points`, consulta la API y muestra nombre, zona, materiales aceptados y estado de atención.
- **Paginación · 1.5 puntos.** Muestra la página actual y el total. Agrega Anterior y Siguiente, deshabilitados en sus respectivos límites. Cada cambio de página consulta el servidor.
- **Estados · 1 punto.** Muestra un indicador durante la consulta y un mensaje cuando el listado esté vacío.

## 3. Detalle y operación · 4 puntos

- **Detalle · 1 punto.** Abre `/collection-points/:id` desde el listado y consulta el registro por ID. Muestra los materiales y su disponibilidad. Usa un selector de materiales activos y un campo numérico que admita pesos decimales.
- **Validación · 1 punto.** Selecciona un material activo e ingresa un peso en kilogramos mayor que cero. Muestra los errores junto a los campos antes de enviar.
- **Operación y resultado · 2 puntos.** Envía el formulario mediante la operación indicada en el contrato. Muestra el material y peso confirmados. Si la API rechaza el material, conserva el peso ingresado.

## 4. Errores e interfaz · 3 puntos

- **Sesión inválida · 1 punto.** Si una petición protegida devuelve `401`, limpia la sesión y redirige al login.
- **Conflicto del caso · 1 punto.** Cuando la API devuelva el código de conflicto evaluado en el contrato, muestra «El punto dejó de recibir ese material.» junto al formulario. Prueba el caso descrito en el contrato.
- **Estructura y presentación · 1 punto.** Usa componentes reutilizables con props tipadas y un diseño legible con Tailwind. Agrupa los controles de paginación y etiqueta los campos del formulario.

**Total máximo: 14 puntos.**
