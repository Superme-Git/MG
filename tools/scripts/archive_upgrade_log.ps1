$ErrorActionPreference = 'Stop'
$in = 'client/FireClient/UpgradeLog.htm'
$outDir = 'docs/archive/upgrade_logs'
New-Item -ItemType Directory -Path $outDir -Force | Out-Null
$date = Get-Date -Format 'yyyyMMdd'
$out = Join-Path $outDir ("FireClient_UpgradeLog_{0}_utf8.htm" -f $date)
if (!(Test-Path -LiteralPath $in)) {
    Write-Error ("Source not found: " + $in)
}
$text = Get-Content -LiteralPath $in -Raw -Encoding Unicode
$utf8NoBom = New-Object System.Text.UTF8Encoding($false)
[System.IO.File]::WriteAllText($out, $text, $utf8NoBom)
if (Test-Path -LiteralPath $out) {
    Write-Host ("Archived to " + $out)
    Remove-Item -LiteralPath $in -Force
    Write-Host ("Removed source " + $in)
} else {
    throw ("Archive failed: " + $out)
}