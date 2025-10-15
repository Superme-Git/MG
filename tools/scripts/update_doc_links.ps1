# Update docs filename references after sequence renaming
# Usage:
#   powershell -NoProfile -ExecutionPolicy Bypass -File tools/scripts/update_doc_links.ps1
# This script updates all Markdown links and raw mentions in docs/ and .claude/ to the new sequenced filenames.

$ErrorActionPreference = 'Stop'

# Ensure UTF-8 IO (avoid mojibake for CJK filenames)
[Console]::InputEncoding  = [System.Text.Encoding]::UTF8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

# Root directory (MT3 project)
$root = Split-Path -Parent (Split-Path -Parent $PSCommandPath)

# Target directories to process
$targets = @(
    Join-Path $root 'docs',
    Join-Path $root '.claude'
)

# Old -> New filename mapping
$map = @{
    '快速启动指南.md'                 = '01-快速启动指南.md'
    '项目概述.md'                     = '02-项目概述.md'
    'MT3项目完整技术体系总结.md'      = '03-技术体系总结.md'
    'C++架构深度分析.md'              = '04-架构深度分析.md'
    '编译环境准备指南.md'             = '05-编译环境准备.md'
    '第三方依赖库重新编译完全指南.md' = '09-第三方库编译指南.md'
    '关键技术文档_正确方法与解决方案.md' = '11-关键技术文档.md'
}

function Update-Content {
    param(
        [string]$text
    )
    $updated = $text
    foreach ($k in $map.Keys) {
        $new = $map[$k]
        # raw filename mention
        $updated = $updated -replace [Regex]::Escape($k), $new
        # (./filename)
        $updated = $updated -replace [Regex]::Escape("./$k"), "./$new"
        # (../docs/filename)
        $updated = $updated -replace [Regex]::Escape("../docs/$k"), "../docs/$new"
        # (docs/filename)
        $updated = $updated -replace [Regex]::Escape("docs/$k"), "docs/$new"
        # (doc/filename) - legacy path support
        $updated = $updated -replace [Regex]::Escape("doc/$k"), "doc/$new"
        # [text](./filename:line)
        $updated = $updated -replace [Regex]::Escape("./$k:"), "./$new:"
        # [text](docs/filename:line)
        $updated = $updated -replace [Regex]::Escape("docs/$k:"), "docs/$new:"
        # [text](../docs/filename:line)
        $updated = $updated -replace [Regex]::Escape("../docs/$k:"), "../docs/$new:"
        # [text](doc/filename:line) - legacy path support
        $updated = $updated -replace [Regex]::Escape("doc/$k:"), "doc/$new:"
    }
    return $updated
}

# Process files
$changed = @()
foreach ($dir in $targets) {
    if (-not (Test-Path -LiteralPath $dir)) { continue }
    Get-ChildItem -Path $dir -Recurse -File -Filter *.md | ForEach-Object {
        $path = $_.FullName
        $content = Get-Content -LiteralPath $path -Raw
        $updated = Update-Content -text $content
        if ($updated -ne $content) {
            Set-Content -LiteralPath $path -Encoding UTF8 $updated
            $changed += $path
            Write-Host "Updated: $path"
        }
    }
}

# Scan summary (remaining old names)
Write-Host ""
Write-Host "Scan summary (remaining old filename mentions):"
foreach ($dir in $targets) {
    Get-ChildItem -Path $dir -Recurse -File -Filter *.md | ForEach-Object {
        $path = $_.FullName
        $content = Get-Content -LiteralPath $path -Raw
        foreach ($k in $map.Keys) {
            $count = [Regex]::Matches($content, [Regex]::Escape($k)).Count
            if ($count -gt 0) {
                Write-Host ("{0}: {1} occurrences in {2}" -f $k, $count, $path)
            }
        }
    }
}

Write-Host ""
Write-Host "Files changed:"
$changed | ForEach-Object { Write-Host $_ }
Write-Host ""
Write-Host "Done."
