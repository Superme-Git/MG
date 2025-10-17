#requires -Version 5.1
<#
Purpose:
- Batch rename files (git mv preferred, fallback Move-Item) using a CSV mapping as per docs/文档整合与重命名方案.md
- Update cross-references in all markdown files under docs (oldPath -> newPath)
- Detect potential GBK/invalid names or content by scanning U+FFFD (replacement character '�')

Usage:
1) Create mapping at docs/rename_map.csv with header:
   oldPath,newPath
   docs/旧文件名.md,docs/新文件名.md
   docs/workflows/GBK示例.md,docs/workflows/00-编译步骤工作计划.md

2) Dry run (recommended first):
   powershell -ExecutionPolicy Bypass -File tools\scripts\rename_gbk_files.ps1 -DryRun

3) Execute:
   powershell -ExecutionPolicy Bypass -File tools\scripts\rename_gbk_files.ps1

Notes:
- git mv is attempted to preserve history; Move-Item used if git is not available
- Link replacement covers forms like "(oldPath:line)", "(oldPath)", "(./oldFile)" and "(oldFile)"
- Only processes markdown files under docs
#>

param(
  [switch]$DryRun,
  [string]$DocsPath
)

$ErrorActionPreference = 'Stop'

function Info($m){ Write-Host ("[INFO] {0}" -f $m) -ForegroundColor Cyan }
function Ok($m){ Write-Host ("[OK]   {0}" -f $m) -ForegroundColor Green }
function Warn($m){ Write-Host ("[WARN]  {0}" -f $m) -ForegroundColor Yellow }
function Err($m){ Write-Host ("[ERR]  {0}" -f $m) -ForegroundColor Red }

# Determine repository root and docs path
$scriptDir = Split-Path -Parent $PSCommandPath               # ...\tools\scripts
$toolsDir  = Split-Path -Parent $scriptDir                   # ...\tools
$root      = Split-Path -Parent $toolsDir                    # repo root (e:\MT3)
if ([string]::IsNullOrWhiteSpace($DocsPath)) {
  $docs = Join-Path $root 'docs'
} else {
  $docs = Resolve-Path -LiteralPath $DocsPath -ErrorAction SilentlyContinue
  if ($null -eq $docs) { $docs = $DocsPath }
}

$mapCsv = Join-Path $docs 'rename_map.csv'

if (!(Test-Path -LiteralPath $docs)) {
  Err ("docs directory not found: {0}" -f $docs)
  exit 1
}

if (Test-Path -LiteralPath $mapCsv) {
  Info ("reading map: {0}" -f $mapCsv)
} else {
  Warn ("map file missing: {0} (only scanning phase will run)" -f $mapCsv)
}

# 1) Scan for U+FFFD in file names and markdown contents
Info "scan names for U+FFFD"
$fffd = [char]0xFFFD
Get-ChildItem -LiteralPath $docs -Recurse -File |
  Where-Object { $_.Name.Contains($fffd) } |
  ForEach-Object {
    Warn ("[GBK? NAME] {0}" -f $_.FullName)
  }

Info "scan markdown contents for U+FFFD"
Get-ChildItem -LiteralPath $docs -Recurse -File |
  Where-Object { $_.Extension -ieq '.md' } |
  ForEach-Object {
    $txt = Get-Content -LiteralPath $_.FullName -Raw -ErrorAction SilentlyContinue
    if ($null -ne $txt -and $txt.Contains($fffd)) {
      Warn ("[GBK? CONTENT] {0}" -f $_.FullName)
    }
  }

# 2) Execute renames per mapping
$maps = @()
if (Test-Path -LiteralPath $mapCsv) {
  try {
    $maps = Import-Csv -LiteralPath $mapCsv
  } catch {
    Err ("failed to read map: {0}" -f $_.Exception.Message)
    exit 2
  }
}

foreach ($m in $maps) {
  $old = $m.oldPath
  $new = $m.newPath

  if ([string]::IsNullOrWhiteSpace($old) -or [string]::IsNullOrWhiteSpace($new)) {
    Warn ("invalid map entry: old='{0}' new='{1}'" -f $old, $new)
    continue
  }

  $oldFull = if ([System.IO.Path]::IsPathRooted($old)) { $old } else { Join-Path $root $old }
  $newFull = if ([System.IO.Path]::IsPathRooted($new)) { $new } else { Join-Path $root $new }

  if (!(Test-Path -LiteralPath $oldFull)) {
    Warn ("old path not found, skip: {0}" -f $oldFull)
    continue
  }

  $newDir = Split-Path -Parent $newFull
  if (!(Test-Path -LiteralPath $newDir)) {
    if ($DryRun) {
      Info ("[DRY] create dir: {0}" -f $newDir)
    } else {
      New-Item -ItemType Directory -Force -Path $newDir | Out-Null
      Ok ("create dir: {0}" -f $newDir)
    }
  }

  if ($DryRun) {
    Info ("[DRY] rename: {0} -> {1}" -f $oldFull, $newFull)
  } else {
    # Prefer git mv
    $gitOk = $false
    try {
      & git mv -- "$oldFull" "$newFull" 2>$null
      if ($LASTEXITCODE -eq 0) { $gitOk = $true; Ok ("git mv: {0} -> {1}" -f $old, $new) }
    } catch { }

    if (-not $gitOk) {
      try {
        Move-Item -LiteralPath $oldFull -Destination $newFull -Force
        Ok ("move: {0} -> {1}" -f $old, $new)
      } catch {
        Err ("rename failed: {0} -> {1} -> {2}" -f $old, $new, $_.Exception.Message)
        continue
      }
    }
  }

  # 3) Update links in docs markdown files (oldPath -> newPath)
  Info ("update links: {0} -> {1}" -f $old, $new)
  Get-ChildItem -LiteralPath $docs -Recurse -File |
    Where-Object { $_.Extension -ieq '.md' } |
    ForEach-Object {
      $p = $_.FullName
      $raw = Get-Content -LiteralPath $p -Raw -ErrorAction SilentlyContinue
      if ($null -eq $raw) { return }
      $updated = $raw

      $oldEsc = [regex]::Escape($old)

      # Form 1: (oldPath:line) -> (newPath:1)
      $updated = [regex]::Replace($updated, "\(\s*${oldEsc}\s*:\d+\)", ("({0}:1)" -f $new))

      # Form 2: (oldPath) -> (newPath)
      $updated = [regex]::Replace($updated, "\(\s*${oldEsc}\s*\)", ("({0})" -f $new))

      # Form 3: filename-only references
      $oldFileName = Split-Path -Leaf $old
      $newFileName = Split-Path -Leaf $new
      $oldFileEsc  = [regex]::Escape($oldFileName)
      $updated     = [regex]::Replace($updated, "\(\s*\.\/${oldFileEsc}\s*\)", ("(./{0})" -f $newFileName))
      $updated     = [regex]::Replace($updated, "\(\s*${oldFileEsc}\s*\)", ("({0})" -f $newFileName))

      if ($updated -ne $raw) {
        if ($DryRun) {
          Info ("[DRY] update: {0}" -f $p)
        } else {
          Set-Content -LiteralPath $p -Value $updated -Encoding UTF8
          Ok ("update: {0}" -f $p)
        }
      }
    }
}

Ok "done"