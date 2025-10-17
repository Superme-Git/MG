# 列出仓库中前100个最大文件，输出到 e:\MT3\bigfiles.txt
$root = 'E:\MT3'
Set-Location $root
$files = Get-ChildItem -Recurse -File -ErrorAction SilentlyContinue | Sort-Object Length -Descending | Select-Object -First 100
$out = @()
foreach ($f in $files) {
    $out += "{0}`t{1}`t{2}" -f $([string]$f.FullName), ([math]::Round($f.Length/1MB,2)), $f.Length
}
$out | Out-File -Encoding UTF8 "E:\MT3\bigfiles.txt"
Write-Host "Wrote E:\MT3\bigfiles.txt with $($out.Count) entries"
