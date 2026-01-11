# Script to analyze all mod elements
$elementsDir = "C:\Users\bayne\Documents\TheGreatExpansion\elements"
$files = Get-ChildItem -Path $elementsDir -Filter "*.mod.json"

$inventory = @{}

foreach ($file in $files) {
    try {
        $content = Get-Content -Path $file.FullName -Raw | ConvertFrom-Json
        $type = $content._type
        $name = $content.definition.name

        if (-not $inventory.ContainsKey($type)) {
            $inventory[$type] = @()
        }

        $inventory[$type] += @{
            FileName = $file.Name
            Name = $name
            Definition = $content.definition
        }
    } catch {
        Write-Host "Error processing $($file.Name): $_"
    }
}

# Output summary
foreach ($type in $inventory.Keys | Sort-Object) {
    Write-Host "`n=== $type ($($ inventory[$type].Count) items) ===" -ForegroundColor Cyan
    foreach ($item in $inventory[$type] | Sort-Object { $_.Name }) {
        Write-Host "  - $($item.Name)" -ForegroundColor Yellow
    }
}

# Save detailed JSON
$inventory | ConvertTo-Json -Depth 10 | Out-File "C:\Users\bayne\Documents\TheGreatExpansion\element_inventory.json"
Write-Host "`nDetailed inventory saved to element_inventory.json"
