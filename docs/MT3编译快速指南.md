# MT3 编译快速指南 ⚡

> 这是一个精简版指南,详细版本请查看 [MT3项目编译问题分析与解决方案.md](MT3项目编译问题分析与解决方案.md)

## 🎯 问题和解决方案

### 问题
```
错误 LNK1169: 找到一个或多个多重定义的符号
错误 LNK2005: __crt_debugger_hook 已经在 MSVCRT.lib 中定义
```

### 根本原因
依赖库使用旧版编译器(VS2010)编译,与 VS2015 的新运行时库(UCRT)不兼容。

### 解决方案
✅ **使用 VS2015 重新编译所有依赖库**

---

## 🚀 快速开始(3 步)

### 方法 1: Visual Studio GUI(推荐新手)

```
1. 打开解决方案
   E:\梦幻西游MG原版源码\client\FireClient\FireClient.sln

2. 批量编译
   菜单 → 生成 → 批生成 → 勾选 Release|Win32 → 生成

3. 等待完成
   约 30-60 分钟
```

### 方法 2: 自动化脚本(推荐高级用户)

**步骤 1**: 创建 `build_all.ps1`(脚本内容见下方)

**步骤 2**: 运行脚本
```powershell
cd "E:\梦幻西游MG原版源码"
.\build_all.ps1 -Configuration Release
```

**步骤 3**: 验证输出
```powershell
# 检查可执行文件
Test-Path "client\FireClient\Release.win32\MT3.exe"
```

---

## 📜 自动化脚本

### 完整脚本: build_all.ps1

```powershell
# MT3 一键编译脚本
param(
    [ValidateSet("Debug", "Release", "All")]
    [string]$Configuration = "Release"
)

$ErrorActionPreference = "Stop"
$root = "E:\梦幻西游MG原版源码"
$sln = "$root\client\FireClient\FireClient.sln"
$devenv = "C:\Program Files (x86)\Microsoft Visual Studio 14.0\Common7\IDE\devenv.exe"

# 验证环境
if (-not (Test-Path $devenv)) {
    Write-Host "错误: 未找到 Visual Studio 2015" -ForegroundColor Red
    exit 1
}

# 1. 备份(可选)
Write-Host "[1/4] 备份..." -ForegroundColor Cyan
$backup = "$root\backup_$(Get-Date -Format 'yyyyMMdd_HHmmss')"
New-Item -ItemType Directory -Path $backup -Force | Out-Null
Copy-Item -Recurse "$root\common" "$backup\common" -Force
Copy-Item -Recurse "$root\engine" "$backup\engine" -Force
Write-Host "  完成: $backup" -ForegroundColor Green

# 2. 清理
Write-Host "[2/4] 清理..." -ForegroundColor Cyan
& $devenv $sln /Clean "$Configuration|Win32" | Out-Null
Write-Host "  完成" -ForegroundColor Green

# 3. 编译
Write-Host "[3/4] 编译..." -ForegroundColor Cyan
$log = "$root\build_$(Get-Date -Format 'yyyyMMdd_HHmmss').log"
$start = Get-Date
& $devenv $sln /Rebuild "$Configuration|Win32" /Out $log
$duration = ((Get-Date) - $start).TotalMinutes

if ($LASTEXITCODE -eq 0) {
    Write-Host "  完成 (用时: $([math]::Round($duration, 1)) 分钟)" -ForegroundColor Green
} else {
    Write-Host "  失败! 查看日志: $log" -ForegroundColor Red
    exit 1
}

# 4. 验证
Write-Host "[4/4] 验证..." -ForegroundColor Cyan
$buildDir = if ($Configuration -eq "Debug") { "Debug.win32" } else { "Release.win32" }
$exe = "$root\client\FireClient\$buildDir\MT3.exe"

if (Test-Path $exe) {
    $size = (Get-Item $exe).Length / 1MB
    Write-Host "  ✓ MT3.exe ($([math]::Round($size, 2)) MB)" -ForegroundColor Green
    Write-Host ""
    Write-Host "可执行文件: $exe" -ForegroundColor Yellow
} else {
    Write-Host "  ✗ MT3.exe 未生成" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "=== 编译成功! ===" -ForegroundColor Green
```

### 使用示例

```powershell
# 仅编译 Release 版本(推荐)
.\build_all.ps1 -Configuration Release

# 仅编译 Debug 版本
.\build_all.ps1 -Configuration Debug

# 编译两个版本
.\build_all.ps1 -Configuration All
```

---

## ⏱️ 时间预估

| 任务 | 时间 |
|------|------|
| 备份 | 1-2 分钟 |
| 清理 | 1-2 分钟 |
| 编译所有库 | 30-60 分钟 |
| 验证 | 1 分钟 |
| **总计** | **约 35-65 分钟** |

## 📋 编译后检查

```powershell
# 检查关键库文件
$libs = @(
    "cocos2d-2.0-rc2-x-2.0.1\Release.win32\libcocos2d.lib",
    "engine\Release.win32\engine.lib",
    "common\platform\Release.win32\platform.lib",
    "client\FireClient\Release.win32\MT3.exe"
)

foreach ($lib in $libs) {
    $path = "E:\梦幻西游MG原版源码\$lib"
    if (Test-Path $path) {
        $size = (Get-Item $path).Length / 1MB
        Write-Host "✓ $lib ($([math]::Round($size, 2)) MB)" -ForegroundColor Green
    } else {
        Write-Host "✗ 缺失: $lib" -ForegroundColor Red
    }
}
```

---

## ⚠️ 常见问题

### 问题 1: 找不到 devenv.exe

**解决**:
```powershell
# 查找 devenv.exe 位置
Get-ChildItem "C:\Program Files (x86)" -Recurse -Filter "devenv.exe" -ErrorAction SilentlyContinue

# 或使用 MSBuild(如果找不到 devenv.exe)
$msbuild = "C:\Program Files (x86)\MSBuild\14.0\Bin\MSBuild.exe"
& $msbuild $sln /t:Rebuild /p:Configuration=Release /p:Platform=Win32
```

### 问题 2: 运行时缺少 DLL

**症状**: `MSVCP140.dll` 或 `VCRUNTIME140.dll` 缺失

**解决**: 安装 VC++ 2015 Redistributable
```
下载: https://www.microsoft.com/zh-cn/download/details.aspx?id=48145
安装: vc_redist.x86.exe (32位版本)
```

### 问题 3: 编译错误 MSB8036

**症状**: 找不到 Windows SDK

**解决**: 
1. 打开 Visual Studio Installer
2. 修改 VS2015 安装
3. 勾选 "Windows SDK 8.1" 或 "Windows SDK 10"

---

## 🎯 依赖关系图

```
MT3.exe
├── FireClient.lib
├── engine.lib
│   ├── libcocos2d.lib ⭐ 核心引擎
│   │   ├── Box2D.lib
│   │   ├── chipmunk.lib
│   │   ├── lua.lib
│   │   └── ANGLE 组件
│   ├── platform.lib
│   ├── cauthc.lib
│   ├── ljfm.lib
│   └── cegui.lib
│       ├── pcre.lib
│       └── SILLY.lib
└── 音频库
    ├── libspeex.lib
    ├── libogg.lib
    └── libvorbis.lib
```

---

## 📞 需要更多帮助?

- 📖 详细文档: [MT3项目编译问题分析与解决方案.md](MT3项目编译问题分析与解决方案.md)
- 🔍 查看编译日志: `E:\梦幻西游MG原版源码\build_logs`
- 🐛 查看错误信息: Visual Studio 输出窗口

---

## ✅ 成功标志

编译成功后,您应该看到:
```
✓ 所有项目编译成功(0 个错误)
✓ MT3.exe 生成成功
✓ 程序可以正常启动
✓ 无崩溃或错误提示
```

---

**最后更新**: 2025-10-11  
**版本**: 1.0