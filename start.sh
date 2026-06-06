#!/bin/bash
# ============================================================
# EcoTravel System - Startup Script (Linux/macOS)
# Priprema okruzenja, ciscenje resursa i verifikacija statusa
# ============================================================

set -e

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
CYAN='\033[0;36m'
NC='\033[0m'

ok()   { echo -e "  ${GREEN}[OK]${NC} $1"; }
warn() { echo -e "  ${YELLOW}[!!]${NC} $1"; }
fail() { echo -e "  ${RED}[XX]${NC} $1"; }
step() { echo -e "\n${CYAN}[$1]${NC} $2"; }

echo "============================================="
echo "  EcoTravel Microservices - Startup Script  "
echo "============================================="

# ─── KORAK 1: Verifikacija Docker-a ───────────────────────
step "1/5" "Verifikacija Docker okruzenja..."
if ! docker info &>/dev/null; then
    fail "Docker nije pokrenut. Pokrenite Docker i pokusajte ponovo."
    exit 1
fi
ok "Docker je pokrenut."

# ─── KORAK 2: Ciscenje prethodno korisenih resursa ────────
step "2/5" "Ciscenje prethodno korisenih resursa..."
docker compose down --remove-orphans 2>/dev/null || true
ok "Stari kontejneri zaustavljeni."
docker volume prune -f &>/dev/null
ok "Docker volumeni ocisceni."
docker network prune -f &>/dev/null
ok "Docker mreze ociscene."

# ─── KORAK 3: Build i pokretanje servisa ──────────────────
step "3/5" "Pokretanje svih servisa (3 instance accommodation, rezervacije i finansije)..."
docker compose up -d --build --scale accommodation-service=3 --scale rezervacije-service=3 --scale finansije-service=3
ok "Servisi pokrenuti."

# ─── KORAK 4: Cekanje na infrastrukturu ───────────────────
step "4/5" "Cekanje na inicijalizaciju infrastrukture (60s)..."
for i in $(seq 1 12); do
    sleep 5
    printf "."
done
echo ""

# ─── KORAK 5: Verifikacija statusa servisa ─────────────────
step "5/5" "Verifikacija statusa serverske infrastrukture..."

check_service() {
    local name=$1
    local url=$2
    if curl -sf "$url" &>/dev/null; then
        ok "$name: UP"
    else
        warn "$name: jos uvek se pokrece..."
    fi
}

check_service "Config Service  " "http://localhost:8888/actuator/health"
check_service "Eureka Server   " "http://localhost:8761/actuator/health"
check_service "Zipkin          " "http://localhost:9411/actuator/health"
check_service "Loki            " "http://localhost:3100/ready"
check_service "Grafana         " "http://localhost:3000/api/health"
check_service "Gateway         " "http://localhost:8080/actuator/health"
check_service "Maintenance     " "http://localhost:8085/actuator/health"

echo ""
echo "  Instance skaliranih servisa:"
for SERVICE_NAME in accommodation-service rezervacije-service finansije-service; do
    SERVICE_COUNT=$(docker compose ps "$SERVICE_NAME" 2>/dev/null | grep -c "Up" || echo 0)
    if [ "$SERVICE_COUNT" -ge 3 ]; then
        ok "$SERVICE_NAME: $SERVICE_COUNT instance aktivno"
    else
        warn "$SERVICE_NAME: $SERVICE_COUNT instanca pokrenuta (ocekivano 3)"
    fi
done

echo ""
echo "  Zipkin servisi:"
ZIPKIN_SVCS=$(curl -sf http://localhost:9411/api/v2/services 2>/dev/null || echo "[]")
ok "Zipkin services: $ZIPKIN_SVCS"

echo ""
echo "============================================="
echo "  Pristupne adrese:"
echo -e "  Grafana:  ${YELLOW}http://localhost:3000${NC}   (admin/admin)"
echo -e "  Zipkin:   ${YELLOW}http://localhost:9411${NC}"
echo -e "  Eureka:   ${YELLOW}http://localhost:8761${NC}"
echo -e "  RabbitMQ: ${YELLOW}http://localhost:15672${NC}  (guest/guest)"
echo -e "  Gateway:  ${YELLOW}http://localhost:8080/swagger-ui.html${NC}"
echo -e "  Adminer:  ${YELLOW}http://localhost:8087${NC}"
echo "============================================="
