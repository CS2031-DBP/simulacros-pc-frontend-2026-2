#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
case "${1:-}" in
  espacios-estudio|talleres-culturales|mantenimiento-campus|prestamo-juegos|entregas-reciclaje|solicitudes-tutoria) ;;
  *) echo "Indica un servicio de la tabla del README." >&2; exit 2 ;;
esac
docker compose rm --stop --force "$1"
volume="simulacros-pc-frontend-2026-2_$1"
if docker volume inspect "$volume" >/dev/null 2>&1; then
  docker volume rm "$volume"
fi
docker compose up --build -d "$1"
