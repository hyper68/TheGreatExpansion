<#
.SYNOPSIS
  Create Desktop and Start Menu shortcuts for the dashboard.

.DESCRIPTION
  This script creates a Windows shortcut (.lnk) that launches `run_dashboard.bat`.
  It will place shortcuts on the current user's Desktop and Start Menu->Programs folder
  and optionally set a hotkey (default: Ctrl+Alt+D).

.EXAMPLE
  .\create_windows_shortcut.ps1

#>

param(
    [string]$ShortcutName = 'MCreator Dashboard',
    [string]$BatRelative = 'run_dashboard.bat',
    [string]$Hotkey = 'CTRL+ALT+D'
)

try {
    $scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
    $targetPath = Join-Path $scriptDir $BatRelative

    if (-not (Test-Path $targetPath)) {
        Write-Error "Target not found: $targetPath"
        exit 1
    }

    $WshShell = New-Object -ComObject WScript.Shell

    $desktop = [Environment]::GetFolderPath([Environment+SpecialFolder]::Desktop)
    $programs = [Environment]::GetFolderPath([Environment+SpecialFolder]::Programs)

    $links = @()
    $links += Join-Path $desktop "$ShortcutName.lnk"
    # $programs may be a single string; if it's an array, iterate
    if ($programs -is [System.Array]) {
        foreach ($p in $programs) {
            $links += Join-Path $p "$ShortcutName.lnk"
        }
    } elseif ($programs) {
        $links += Join-Path $programs "$ShortcutName.lnk"
    }

    foreach ($link in $links) {
        $dir = Split-Path $link -Parent
        if (-not (Test-Path $dir)) {
            New-Item -ItemType Directory -Path $dir -Force | Out-Null
        }

        $shortcut = $WshShell.CreateShortcut($link)
        $shortcut.TargetPath = $targetPath
        $shortcut.WorkingDirectory = Split-Path $targetPath -Parent
        $shortcut.IconLocation = "$targetPath,0"
        if ($Hotkey) { $shortcut.Hotkey = $Hotkey }
        $shortcut.Save()

        Write-Output "Created shortcut: $link"
    }

    Write-Output "Shortcuts created. Press the hotkey (if set) or use the shortcut to launch the dashboard."
} catch {
    Write-Error $_.Exception.Message
    exit 1
}
