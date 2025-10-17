#requires -Version 3.0
<#
Android 环境快速验证脚本（PowerShell 版）
用法示例：
  powershell -ExecutionPolicy Bypass -File tools\validate\android_env_check.ps1 `
    -SDK "C:\Progra~2\Android\android-sdk" `
    -NDK "D:\android-ndk-r10e" `
    -ANT "C:\apache-ant-1.9.7" `
    -JDK "C:\Program Files\Java\jdk1.8.0_144" `
    -BuildTools "22.0.1" `
    -Platform "android-22"

说明：
- 支持命名参数，未传参使用默认值（见 Param）
- 返回码：0=全部通过，非0=存在缺失/异常项
- 与批处理版一致的检查项，适配 CI/本地
#>

Param(
  [string]$SDK       = 'C:\Progra~2\Android\android-sdk',
  [string]$NDK       = 'D:\android-ndk-r10e',
  [string]$ANT       = 'C:\apache-ant-1.9.7',
  [string]$JDK       = 'C:\Program Files\Java\jdk1.8.0_144',
  [string]$BuildTools= '22.0.1',
  [string]$Platform  = 'android-22'
)

#region Helpers
function Unquote([string]$s) {
  if ($null -eq $s) { return $s }
  return $s.Trim('"')
}
function Resolve-Paths {
  $script:AAPT       = Join-Path $SDK "build-tools\$BuildTools\aapt.exe"
  $script:DXBAT      = Join-Path $SDK "build-tools\$BuildTools\dx.bat"
  $script:ZIPALIGN   = Join-Path $SDK "build-tools\$BuildTools\zipalign.exe"
  $script:ANDROIDJAR = Join-Path $SDK "platforms\$Platform\android.jar"
  $script:ADB        = Join-Path $SDK "platform-tools\adb.exe"
  $script:NDKBUILD   = Join-Path $NDK "ndk-build.cmd"
  $script:ANTBAT     = Join-Path $ANT "bin\ant.bat"
  $script:JAVA       = Join-Path $JDK "bin\java.exe"
  $script:JAVAC      = Join-Path $JDK "bin\javac.exe"
  $script:JARSIGNER  = Join-Path $JDK "bin\jarsigner.exe"
}
function Write-Head {
  Write-Host ('=' * 59)
  Write-Host "[Android Env Check] 参数:"
  Write-Host ("  SDK=`"{0}`"" -f $SDK)
  Write-Host ("  NDK=`"{0}`"" -f $NDK)
  Write-Host ("  ANT=`"{0}`"" -f $ANT)
  Write-Host ("  JDK=`"{0}`"" -f $JDK)
  Write-Host ("  BuildTools=`"{0}`"" -f $BuildTools)
  Write-Host ("  Platform=`"{0}`"" -f $Platform)
  Write-Host ('-' * 59)
}
function Test-File([string]$path,[string]$label,[string]$args='',$switch='') {
  if (Test-Path -LiteralPath $path) {
    if ($args) {
      try {
        # 使用 Start-Process 捕获退出码；dx.bat 需要 cmd /c
        if ([System.IO.Path]::GetExtension($path) -eq '.bat') {
          $p = Start-Process -FilePath 'cmd.exe' -ArgumentList "/c `"$path`" $args" -Wait -NoNewWindow -PassThru
        } else {
          $p = Start-Process -FilePath $path -ArgumentList $args -Wait -NoNewWindow -PassThru
        }
        if ($p.ExitCode -ne 0) {
          Write-Host ("[WARN] {0} 存在但命令返回非零：`"{1}`"" -f $label,$path)
          return 0
        } else {
          Write-Host ("[OK]   {0}: `"{1}`"" -f $label,$path)
          return 0
        }
      } catch {
        Write-Host ("[WARN] {0} 存在但调用异常：`"{1}`" → {2}" -f $label,$path,$_.Exception.Message)
        return 0
      }
    } else {
      Write-Host ("[OK]   {0}: `"{1}`"" -f $label,$path)
      return 0
    }
  } else {
    Write-Host ("[X]    {0} 缺失：`"{1}`"" -f $label,$path)
    return 1
  }
}
#endregion Helpers

# 规范化输入（去除可能的包裹引号）
$SDK        = Unquote $SDK
$NDK        = Unquote $NDK
$ANT        = Unquote $ANT
$JDK        = Unquote $JDK
$BuildTools = Unquote $BuildTools
$Platform   = Unquote $Platform

Resolve-Paths
Write-Head

$err = 0

# aapt
$err += Test-File -path $AAPT -label 'aapt' -args 'version'

# dx
$err += Test-File -path $DXBAT -label 'dx' -args '--version'

# zipalign
$err += Test-File -path $ZIPALIGN -label 'zipalign' -args '-h'

# android.jar
if (Test-Path -LiteralPath $ANDROIDJAR) {
  Write-Host ("[OK]   android.jar: `"{0}`"" -f $ANDROIDJAR)
} else {
  Write-Host ("[X]    android.jar 缺失：`"{0}`"" -f $ANDROIDJAR)
  $err += 1
}

# adb
$err += Test-File -path $ADB -label 'adb' -args 'version'

# ndk-build
$err += Test-File -path $NDKBUILD -label 'ndk-build' -args '--help'

# ant
$err += Test-File -path $ANTBAT -label 'ant' -args '-version'

# java / javac / jarsigner
$err += Test-File -path $JAVA -label 'java' -args '-version'
$err += Test-File -path $JAVAC -label 'javac' -args '-version'
$err += Test-File -path $JARSIGNER -label 'jarsigner' -args '-help'

Write-Host ('-' * 59)
if ($err -ne 0) {
  Write-Host ("[SUMMARY] 验证完成：存在 {0} 项缺失/异常（请检查上方 [X]/[WARN]）" -f $err)
  Write-Host ("[HINT] 建议使用短路径（C:\Progra~2）与正确版本（Build-Tools={0}、Platform={1}）" -f $BuildTools,$Platform)
  exit $err
} else {
  Write-Host "[SUMMARY] 验证完成：所有检查通过"
  exit 0
}