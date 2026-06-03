# ============================================================
# EcoTravel System - Startup Script
# Priprema okruzenja, ciscenje resursa i verifikacija statusa
# ============================================================

param(
    [switch]$SkipCleanup,
    [switch]$NoBuild
)

$ErrorActionPreference = "Continue"

function Write-Step($step, $msg) {
    Write-Host "`n[$step] $msg" -ForegroundColor Cyan
}

function Write-Ok($msg) {
    Write-Host "  [OK] $msg" -ForegroundColor Green
}

function Write-Warn($msg) {
    Write-Host "  [!!] $msg" -ForegroundColor Yellow
}

function Write-Fail($msg) {
    Write-Host "  [XX] $msg" -ForegroundColor Red
}

Write-Host "=============================================" -ForegroundColor White
Write-Host "  EcoTravel Microservices - Startup Script  " -ForegroundColor White
Write-Host "=============================================" -ForegroundColor White

# ─── KORAK 1: Verifikacija Docker-a ───────────────────────
Write-Step "1/5" "Verifikacija Docker okruzenja..."
$dockerVersion = & docker version --format "{{.Server.Version}}" 2>$null
if (-not $dockerVersion) {
    Write-Fail "Docker nije pokrenut ili nije dostupan. Pokrenite Docker Desktop i pokusajte ponovo."
    exit 1
}
Write-Ok "Docker je pokrenut (verzija: $dockerVersion)."

# ─── KORAK 2: Ciscenje prethodno korisenih resursa ────────
if (-not $SkipCleanup) {
    Write-Step "2/5" "Ciscenje prethodno korisenih resursa..."
    docker compose down --remove-orphans | Out-Null
    Write-Ok "Stari kontejneri zaustavljeni i uklonjeni."

    docker volume prune -f | Out-Null
    Write-Ok "Docker volumeni ocisceni."

    docker network prune -f | Out-Null
    Write-Ok "Docker mreze ociscene."
} else {
    Write-Step "2/5" "Preskacanje ciscenja (--SkipCleanup)."
}

# ─── KORAK 3: Build i pokretanje servisa ──────────────────
Write-Step "3/5" "Pokretanje svih servisa (3 instance accommodation-service)..."
if ($NoBuild) {
    docker compose up -d --scale accommodation-service=3
} else {
    docker compose up -d --build --scale accommodation-service=3
}

if ($LASTEXITCODE -ne 0) {
    Write-Fail "Greska pri pokretanju servisa. Pogledajte 'docker compose logs'."
    exit 1
}
Write-Ok "Servisi pokrenuti."

# ─── KORAK 4: Cekanje na infrastrukturu ───────────────────
Write-Step "4/5" "Cekanje na inicijalizaciju infrastrukture (60s)..."
$dots = 0
while ($dots -lt 60) {
    Start-Sleep -Seconds 5
    $dots += 5
    Write-Host "  ." -NoNewline -ForegroundColor Gray
}
Write-Host ""

# ─── KORAK 5: Verifikacija statusa servisa ─────────────────
Write-Step "5/5" "Verifikacija statusa serverske infrastrukture..."

$checks = @(
    @{ Name = "Config Service  "; Url = "http://localhost:8888/actuator/health" },
    @{ Name = "Eureka Server   "; Url = "http://localhost:8761/actuator/health" },
    @{ Name = "Zipkin          "; Url = "http://localhost:9411/actuator/health" },
    @{ Name = "Loki            "; Url = "http://localhost:3100/ready" },
    @{ Name = "Grafana         "; Url = "http://localhost:3000/api/health" },
    @{ Name = "Gateway         "; Url = "http://localhost:8080/actuator/health" },
    @{ Name = "Maintenance     "; Url = "http://localhost:8085/actuator/health" }
)

$allUp = $true
foreach ($svc in $checks) {
    try {
        $resp = Invoke-WebRequest -Uri $svc.Url -TimeoutSec 5 -ErrorAction Stop -UseBasicParsing
        if ($resp.StatusCode -eq 200) {
            Write-Ok "$($svc.Name): UP"
        } else {
            Write-Warn "$($svc.Name): Status $($resp.StatusCode)"
            $allUp = $false
        }
    } catch {
        Write-Warn "$($svc.Name): jos uvek se pokrece..."
        $allUp = $false
    }
}

# Provera skaliranja accommodation-service
Write-Host ""
Write-Host "  Instace accommodation-service:" -ForegroundColor Cyan
$containers = docker compose ps --format json 2>&1 | ConvertFrom-Json -ErrorAction SilentlyContinue
if ($containers) {
    $accContainers = $containers | Where-Object { $_.Service -eq "accommodation-service" }
    $count = ($accContainers | Measure-Object).Count
    if ($count -ge 3) {
        Write-Ok "accommodation-service: $count instance aktivno (round-robin load balancing)"
    } else {
        Write-Warn "accommodation-service: $count instanca (ocekivano 3)"
    }
} else {
    $accCount = (docker compose ps accommodation-service 2>&1 | Select-String "Up").Count
    Write-Ok "accommodation-service instance: $accCount aktivno"
}

# Provera Zipkin servisa
Write-Host ""
Write-Host "  Zipkin servisi (iz tracing podataka):" -ForegroundColor Cyan
try {
    $zipkinSvcs = Invoke-WebRequest -Uri "http://localhost:9411/api/v2/services" -TimeoutSec 5 -UseBasicParsing -ErrorAction Stop
    Write-Ok "Zipkin services: $($zipkinSvcs.Content)"
} catch {
    Write-Warn "Zipkin jos nema traceva (pokrenite zahteve prvo)"
}

# ─── Sumarni izvestaj ──────────────────────────────────────
Write-Host ""
Write-Host "=============================================" -ForegroundColor White
Write-Host "  Pristupne adrese:" -ForegroundColor White
Write-Host "  Grafana:  http://localhost:3000   (admin/admin)" -ForegroundColor Yellow
Write-Host "  Zipkin:   http://localhost:9411" -ForegroundColor Yellow
Write-Host "  Eureka:   http://localhost:8761" -ForegroundColor Yellow
Write-Host "  RabbitMQ: http://localhost:15672  (guest/guest)" -ForegroundColor Yellow
Write-Host "  Gateway:  http://localhost:8080/swagger-ui.html" -ForegroundColor Yellow
Write-Host "  Adminer:  http://localhost:8087" -ForegroundColor Yellow
Write-Host "=============================================" -ForegroundColor White

if ($allUp) {
    Write-Host "`n  Sistem je spreman!" -ForegroundColor Green
} else {
    Write-Host "`n  Neki servisi jos uvek startuju. Sacekajte 30-60s i proverite ponovo." -ForegroundColor Yellow
    Write-Host "  Koristite: docker compose logs -f <service-name>" -ForegroundColor Gray
}
