param(
    [string]$ResourceGroup = "rg-satecho-demo",
    [string]$BackendApp = "agrosafe-back"
)

$ErrorActionPreference = "Stop"

az account set --subscription "c9a50372-ff5c-4ec9-9b97-2b80a3a05346"

$containers = @(
    "agrosafe-rabbitmq-demo",
    "agrosafe-redis-demo",
    "agrosafe-mqtt-demo"
)

foreach ($container in $containers) {
    az container start --resource-group $ResourceGroup --name $container --output none
}

az containerapp update `
    --name $BackendApp `
    --resource-group $ResourceGroup `
    --min-replicas 0 `
    --max-replicas 1 `
    --output none

Write-Host "Demo services are starting."
Write-Host "Backend: https://agrosafe-back.bluemeadow-4bdb72df.eastus.azurecontainerapps.io"
Write-Host "RabbitMQ: satecho-rabbitmq-demo-cientifica.eastus.azurecontainer.io:5672"
Write-Host "RabbitMQ management: http://satecho-rabbitmq-demo-cientifica.eastus.azurecontainer.io:15672"
Write-Host "Redis: satecho-redis-demo-cientifica.eastus.azurecontainer.io:6379"
Write-Host "MQTT: satecho-mqtt-demo-cientifica.eastus.azurecontainer.io:1883"

