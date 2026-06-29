param(
    [string]$mensaje = "Deploy $(Get-Date -Format yyyyMMdd_HHmm)"
)

$origen  = "D:\workspaces\java\workspace_main\central"
$destino = "D:\workspaces\git\visus"
$temp    = [System.IO.Path]::GetTempPath() + "deploy_exclude.txt"

# Archivos/carpetas que NO se copian
@"
.git
target
node_modules
logs
.settings
.classpath
.project
deploy.ps1
"@ | Set-Content $temp -Encoding ASCII

Write-Host "Copiando archivos del proyecto al repositorio Git..." -ForegroundColor Cyan
robocopy $origen $destino /E /XD target /XD node_modules /XD logs /XD .git /XD .settings /NP /NFL /NDL /NJH /NJS

Write-Host "Commit y push a GitHub..." -ForegroundColor Cyan
Set-Location $destino

# Evitar warnings de CRLF
git config core.autocrlf false 2>$null

git add . 2>$null
git commit -m $mensaje 2>$null
git push 2>$null

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "Deploy enviado a GitHub correctamente!" -ForegroundColor Green
Write-Host "Ahora armar el Build Now en Jenkins" -ForegroundColor Green
Write-Host "http://localhost:8080/job/deploy-visus/" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Green

Remove-Item $temp -ErrorAction SilentlyContinue
