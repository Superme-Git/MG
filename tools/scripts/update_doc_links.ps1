# Update docs filename references and relocated paths using external JSON mapping
# Usage examples:
#   powershell -NoProfile -ExecutionPolicy Bypass -File tools\scripts\update_doc_links.ps1
#   powershell -NoProfile -ExecutionPolicy Bypass -File tools\scripts\update_doc_links.ps1 -DryRun
# Notes:
# - This script loads mappings from tools\scripts\link_map.json (UTF-8) to avoid encoding issues
# - It normalizes legacy 'doc/' link prefixes to 'docs/'
# - It applies filename renames (e.g., 编译指南.md -> 06-编译完整指南.md)
# - It applies path relocations (e.g., CEGUI技术集成指南.md -> toolchains/CEGUI技术集成指南.md)

[CmdletBinding()]
param(
  [string]$Root,
  [switch]$DryRun
)

$ErrorActionPreference = 'Stop'

# Ensure UTF-8 IO
[Console]::InputEncoding  = [System.Text.Encoding]::UTF8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

# Resolve project root (default: two levels up from this script)
if (-not $Root -or [string]::IsNullOrWhiteSpace($Root)) {
  $Root = Split-Path -Parent (Split-Path -Parent $PSCommandPath)
}
$Root = [string]$Root

# Directories to process
$targets = @(
  [System.IO.Path]::Combine($Root, 'docs'),
  [System.IO.Path]::Combine($Root, '.claude')
)

# Load mapping JSON
$scriptDir = Split-Path -Parent $PSCommandPath
$mapPath   = Join-Path $scriptDir 'link_map.json'
if (-not (Test-Path -LiteralPath $mapPath)) {
  Write-Error "Mapping file not found: $mapPath"
}

$jsonText  = Get-Content -LiteralPath $mapPath -Raw -Encoding UTF8
$mapObj    = ConvertFrom-Json -InputObject $jsonText

# Mappings
$renameMap = $mapObj.renameMap    # array of { old, new }
$pathMap   = $mapObj.pathMap      # array of { filename, newPath }

function Escape([string]$s) {
  return [System.Text.RegularExpressions.Regex]::Escape($s)
}

function Update-Content([string]$text) {
  $updated = $text

  # Normalize legacy 'doc/' prefix inside Markdown links: (doc/... or (../doc/...
  # Convert to '(docs/...' while preserving optional '../'
  $updated = [regex]::Replace($updated, '\((\.\./)?doc/', '($1docs/')

  # Filename renames (e.g., 编译指南.md -> 06-编译完整指南.md)
  foreach ($item in $renameMap) {
    $old = [string]$item.old
    $new = [string]$item.new

    # raw filename mention
    $updated = [regex]::Replace($updated, (Escape($old)), $new)

    # (./filename)
    $updated = [regex]::Replace($updated, (Escape('./' + $old)), './' + $new)

    # (docs/filename)
    $updated = [regex]::Replace($updated, (Escape('docs/' + $old)), 'docs/' + $new)

    # (../docs/filename)
    $updated = [regex]::Replace($updated, (Escape('../docs/' + $old)), '../docs/' + $new)

    # Legacy (doc/filename) with :line suffix and non-suffix forms
    $updated = [regex]::Replace($updated, (Escape('doc/' + $old)), 'docs/' + $new)

    # With :line suffix
    $updated = [regex]::Replace($updated, (Escape('./' + $old + ':')), './' + $new + ':')
    $updated = [regex]::Replace($updated, (Escape('docs/' + $old + ':')), 'docs/' + $new + ':')
    $updated = [regex]::Replace($updated, (Escape('../docs/' + $old + ':')), '../docs/' + $new + ':')
    $updated = [regex]::Replace($updated, (Escape('doc/' + $old + ':')), 'docs/' + $new + ':')
  }

  # Path relocations (e.g., CEGUI技术集成指南.md -> toolchains/CEGUI技术集成指南.md)
  foreach ($item in $pathMap) {
    $fn      = [string]$item.filename
    $newPath = [string]$item.newPath

    # (./filename) -> (./new/subpath)
    $updated = [regex]::Replace($updated, (Escape('./' + $fn)), './' + $newPath)

    # (docs/filename) -> (docs/new/subpath)
    $updated = [regex]::Replace($updated, (Escape('docs/' + $fn)), 'docs/' + $newPath)

    # (../docs/filename) -> (../docs/new/subpath)
    $updated = [regex]::Replace($updated, (Escape('../docs/' + $fn)), '../docs/' + $newPath)

    # (doc/filename) legacy -> (docs/new/subpath)
    $updated = [regex]::Replace($updated, (Escape('doc/' + $fn)), 'docs/' + $newPath)

    # With :line suffix
    $updated = [regex]::Replace($updated, (Escape('./' + $fn + ':')), './' + $newPath + ':')
    $updated = [regex]::Replace($updated, (Escape('docs/' + $fn + ':')), 'docs/' + $newPath + ':')
    $updated = [regex]::Replace($updated, (Escape('../docs/' + $fn + ':')), '../docs/' + $newPath + ':')
    $updated = [regex]::Replace($updated, (Escape('doc/' + $fn + ':')), 'docs/' + $newPath + ':')
  }

  return $updated
}

# Process files
$changed = New-Object System.Collections.Generic.List[string]

foreach ($dir in $targets) {
  if (-not (Test-Path -LiteralPath $dir)) { continue }
  Get-ChildItem -Path $dir -Recurse -File -Filter *.md | ForEach-Object {
    $path    = $_.FullName
    $content = Get-Content -LiteralPath $path -Raw -Encoding UTF8
    $updated = Update-Content -text $content

    if ($updated -ne $content) {
      if ($DryRun.IsPresent) {
        Write-Host "[DRYRUN] Would update: $path"
      } else {
        Set-Content -LiteralPath $path -Encoding UTF8 $updated
        $changed.Add($path)
        Write-Host "Updated: $path"
      }
    }
  }
}

# Scan summary for remaining old names and legacy 'doc/' links
Write-Host ""
Write-Host "Scan summary (remaining old filename mentions and legacy prefixes):"
foreach ($dir in $targets) {
  if (-not (Test-Path -LiteralPath $dir)) { continue }
  Get-ChildItem -Path $dir -Recurse -File -Filter *.md | ForEach-Object {
    $path    = $_.FullName
    $content = Get-Content -LiteralPath $path -Raw -Encoding UTF8

    foreach ($item in $renameMap) {
      $old   = [string]$item.old
      $count = ([regex]::Matches($content, (Escape($old)))).Count
      if ($count -gt 0) {
        Write-Host ("{0}: {1} occurrences in {2}" -f $old, $count, $path)
      }
    }

    $legacyCount = ([regex]::Matches($content, '\((\.\./)?doc/')).Count
    if ($legacyCount -gt 0) {
      Write-Host ("legacy 'doc/' links: {0} in {1}" -f $legacyCount, $path)
    }
  }
}

Write-Host ""
if ($DryRun.IsPresent) {
  Write-Host "DryRun complete. No files were modified."
} else {
  Write-Host "Files changed:"
  $changed | ForEach-Object { Write-Host $_ }
  Write-Host ""
  Write-Host "Done."
}