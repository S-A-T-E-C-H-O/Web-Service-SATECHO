param(
    [string]$ResourceGroup = "rg-satecho-demo",
    [string]$BackendApp = "agrosafe-back"
)

$ErrorActionPreference = "Stop"

az account set --subscription "c9a50372-ff5c-4ec9-9b97-2b80a3a05346"

az containerapp update `
    --name $BackendApp `
    --resource-group $ResourceGroup `
    --min-replicas 0 `
    --max-replicas 1 `
    --output none

$containers = @(
    "agrosafe-rabbitmq-demo",
    "agrosafe-redis-demo",
    "agrosafe-mqtt-demo"
)

foreach ($container in $containers) {
    az container stop --resource-group $ResourceGroup --name $container --output none
}

Write-Host "Demo services were stopped. Backend is configured with min replicas = 0."

