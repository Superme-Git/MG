# MT3 项目编译检查清单

**版本**: 1.0  
**更新日期**: 2025-10-13  
**用途**: 避免常见编译错误,确保编译流程正确

---

## 📋 编译前检查清单

### ✅ 环境配置检查

- [ ] **已安装 Visual Studio 2013**
  - 验证: 打开 "关于 Visual Studio" 查看版本号
  - 要求: Visual Studio 2013 (版本 12.0)

- [ ] **已安装 Windows SDK 8.1**
  - 验证: 检查 `C:\Program Files (x86)\Windows Kits\8.1\` 是否存在
  - 如缺失: 从 VS2013 安装程序补装

- [ ] **已配置环境变量**
  - `VS120COMNTOOLS` 指向 VS2013 工具目录
  - 验证: `echo %VS120COMNTOOLS%`

---

## 🔧 项目配置检查

### ✅ 工具集版本 (最重要!)

**检查所有 `.vcxproj` 文件**:

```xml
<!-- ✅ 正确配置 -->
<PlatformToolset>v120</PlatformToolset>

<!-- ❌ 错误配置 -->
<PlatformToolset>v140</PlatformToolset>  <!-- 会导致崩溃! -->
<PlatformToolset>v141</PlatformToolset>
<PlatformToolset>v142</PlatformToolset>
```

**快速检查命令**:
```bash
# 搜索所有项目文件中的 PlatformToolset
findstr /s /i "PlatformToolset" *.vcxproj

# 预期输出: 全部应该是 v120
```

---

### ✅ 字符集配置

- [ ] **所有项目使用 Unicode 字符集**

```xml
<!-- ✅ 正确配置 -->
<CharacterSet>Unicode</CharacterSet>

<!-- ❌ 错误配置 -->
<CharacterSet>MultiByte</CharacterSet>
```

---

### ✅ 运行时库配置

- [ ] **Debug 配置: /MDd (多线程调试 DLL)**

```xml
<RuntimeLibrary>MultiThreadedDebugDLL</RuntimeLibrary>
```

- [ ] **Release 配置: /MD (多线程 DLL)**

```xml
<RuntimeLibrary>MultiThreadedDLL</RuntimeLibrary>
```

**常见错误**:
```xml
<!-- ❌ 不要使用静态链接 -->
<RuntimeLibrary>MultiThreaded</RuntimeLibrary>      <!-- /MT -->
<RuntimeLibrary>MultiThreadedDebug</RuntimeLibrary>  <!-- /MTd -->
```

---

### ✅ 预编译头配置

- [ ] **所有 C++ 项目启用预编译头**

```xml
<PrecompiledHeader>Use</PrecompiledHeader>
<PrecompiledHeaderFile>nupch.h</PrecompiledHeaderFile>
```

- [ ] **每个 `.cpp` 文件第一行包含预编译头**

```cpp
#include "nupch.h"  // 必须是第一行
```

---

### ✅ 库链接顺序检查

- [ ] **检查 `AdditionalDependencies` 顺序**

```xml
<!-- ✅ 正确顺序 (从低层到高层) -->
<AdditionalDependencies>
  lua.lib;
  platform.lib;
  libcocos2d.lib;
  engine.lib;
  FireClient.lib;
  %(AdditionalDependencies)
</AdditionalDependencies>
```

---

## 🔄 编译顺序检查

### ✅ 阶段1: 基础库 (可并行)

- [ ] `lua.lib` 编译成功
- [ ] `platform.lib` 编译成功
- [ ] `cauthc.lib` 编译成功
- [ ] `ljfm.lib` 编译成功

**验证**:
```bash
# 检查 .lib 文件是否生成
dir /s /b lua.lib
dir /s /b platform.lib
```

---

### ✅ 阶段2: Cocos2d-x

- [ ] `libcocos2d.dll` 编译成功
- [ ] `libCocosDenshion.dll` 编译成功

**验证**:
```bash
dir /s /b libcocos2d.dll
dir /s /b libCocosDenshion.dll
```

---

### ✅ 阶段3: Nuclear 引擎

- [ ] `engine.lib` 编译成功

**验证**:
```bash
dir /s /b engine.lib
```

---

### ✅ 阶段4: 应用程序

- [ ] `FireClient.lib` 存在 (预编译库,无需编译)
- [ ] `MT3.exe` 编译成功

**验证**:
```bash
dir /s /b FireClient.lib
dir /s /b MT3.exe
```

---

## 🏃 编译后运行检查

### ✅ DLL 文件完整性

- [ ] **检查必要的 DLL 是否存在**

```
client/resource/bin/debug/ (或 release/)
├── MT3.exe
├── libcocos2d.dll
├── libCocosDenshion.dll
├── MSVCR120.dll       ← 必须!
├── MSVCP120.dll       ← 必须!
└── fmodex.dll         ← 音频库
```

**快速检查**:
```bash
cd client\resource\bin\debug
dir *.dll
```

**如缺失 DLL,运行**:
```bash
copy_runtime_dlls.bat
```

---

### ✅ 资源文件完整性

- [ ] `client/resource/script/` 存在 (Lua 脚本)
- [ ] `client/resource/res/` 存在 (图片/音效)
- [ ] `client/resource/config/` 存在 (配置文件)

---

### ✅ 首次运行测试

- [ ] **启动程序**
  ```bash
  cd client\resource\bin\debug
  MT3.exe
  ```

- [ ] **检查启动日志**
  - 无 "Failed to load script" 错误
  - 无 "Cannot find DLL" 错误
  - 无立即崩溃

- [ ] **功能验证**
  - 主界面正常显示
  - 音效正常播放
  - 基本交互正常

---

## ❌ 常见错误快速排查

### 错误1: LNK2001 无法解析的外部符号

**检查项**:
- [ ] 工具集是否为 v120
- [ ] 运行时库配置是否一致 (/MD vs /MDd)
- [ ] 库链接顺序是否正确

**快速修复**:
```bash
# 清理并重新编译
msbuild MT3.sln /t:Clean
msbuild MT3.sln /t:Build /p:Configuration=Debug /p:Platform=Win32 /p:PlatformToolset=v120
```

---

### 错误2: MSVCR120.dll 缺失

**检查项**:
- [ ] 是否运行了 `copy_runtime_dlls.bat`
- [ ] `C:\Windows\System32\MSVCR120.dll` 是否存在

**快速修复**:
```bash
# 方法1: 运行脚本
copy_runtime_dlls.bat

# 方法2: 手动复制
copy "C:\Windows\System32\MSVCR120.dll" "client\resource\bin\debug\"
copy "C:\Windows\System32\MSVCP120.dll" "client\resource\bin\debug\"
```

---

### 错误3: 预编译头错误 C1010

**检查项**:
- [ ] 每个 `.cpp` 文件第一行是否包含 `#include "nupch.h"`
- [ ] 预编译头配置是否启用

**快速修复**:
```cpp
// 在每个 .cpp 文件第一行添加
#include "nupch.h"
```

---

### 错误4: v140 编译后运行崩溃

**症状**: 编译成功,运行时立即崩溃

**检查项**:
- [ ] 所有项目的 `PlatformToolset` 是否为 v120

**快速修复**:
```bash
# 全局搜索并替换
# 将所有 <PlatformToolset>v140</PlatformToolset>
# 替换为 <PlatformToolset>v120</PlatformToolset>

# 然后清理重新编译
msbuild MT3.sln /t:Clean
msbuild MT3.sln /t:Build /p:PlatformToolset=v120
```

---

### 错误5: Lua 脚本加载失败

**检查项**:
- [ ] `client/resource/script/` 目录是否存在
- [ ] Lua 脚本语法是否正确
- [ ] 工作目录是否正确

**快速修复**:
```bash
# 验证 Lua 语法
luac -p client/resource/script/main.lua

# 确保从正确目录运行
cd client\resource\bin\debug
MT3.exe
```

---

## 📊 完整编译检查表 (打印版)

```
□ 环境配置
  □ Visual Studio 2013 已安装
  □ Windows SDK 8.1 已安装
  □ 环境变量已配置

□ 项目配置
  □ 工具集: v120 (全部项目)
  □ 字符集: Unicode
  □ 运行时库: /MDd (Debug) 或 /MD (Release)
  □ 预编译头: 已启用

□ 编译顺序
  □ 阶段1: lua/platform/cauthc/ljfm.lib
  □ 阶段2: libcocos2d/libCocosDenshion.dll
  □ 阶段3: engine.lib
  □ 阶段4: MT3.exe

□ 运行检查
  □ 所有 DLL 文件存在
  □ 资源文件完整
  □ 程序正常启动
  □ 基本功能正常

□ 提交前验证
  □ 清理后完整编译成功
  □ Debug 和 Release 都编译成功
  □ 运行测试通过
```

---

## 🔧 自动化检查脚本

```batch
@echo off
REM check_build_config.bat - 自动检查编译配置

echo ========================================
echo MT3 项目编译配置检查工具
echo ========================================
echo.

REM 检查 VS2013
echo [1/5] 检查 Visual Studio 2013...
if not defined VS120COMNTOOLS (
    echo [错误] 未检测到 Visual Studio 2013
    exit /b 1
) else (
    echo [通过] Visual Studio 2013: %VS120COMNTOOLS%
)
echo.

REM 检查工具集版本
echo [2/5] 检查工具集版本...
findstr /s /i "v140\|v141\|v142" *.vcxproj >nul
if %ERRORLEVEL% EQU 0 (
    echo [警告] 发现错误的工具集版本!
    findstr /s /i /n "v140\|v141\|v142" *.vcxproj
    exit /b 1
) else (
    echo [通过] 所有项目使用 v120
)
echo.

REM 检查必要的库文件
echo [3/5] 检查必要的库文件...
set MISSING=0
if not exist "common\platform\Release.win32\platform.lib" (
    echo [错误] 缺少 platform.lib
    set MISSING=1
)
if not exist "engine\Release.win32\engine.lib" (
    echo [错误] 缺少 engine.lib
    set MISSING=1
)
if %MISSING% EQU 1 (
    echo [失败] 请先编译基础库
    exit /b 1
) else (
    echo [通过] 库文件完整
)
echo.

REM 检查运行时 DLL
echo [4/5] 检查运行时 DLL...
if not exist "client\resource\bin\debug\MSVCR120.dll" (
    echo [警告] 缺少 MSVCR120.dll
    echo [提示] 运行 copy_runtime_dlls.bat
) else (
    echo [通过] 运行时 DLL 完整
)
echo.

REM 检查资源目录
echo [5/5] 检查资源目录...
if not exist "client\resource\script\" (
    echo [错误] 缺少 script 目录
    exit /b 1
)
if not exist "client\resource\res\" (
    echo [错误] 缺少 res 目录
    exit /b 1
)
echo [通过] 资源目录完整
echo.

echo ========================================
echo 所有检查通过! 可以开始编译。
echo ========================================
exit /b 0
```

**使用**:
```bash
# 编译前运行检查
check_build_config.bat

# 检查通过后再编译
build_mt3_v120_complete.bat
```

---

## 📝 相关文档

- [项目规则](RULES.md) - 核心开发规则
- [编译流程优化指南](BUILD_GUIDE.md) - 详细编译流程
- [MCP工具配置](MCP_CONFIG.md) - 工具推荐

---

**使用建议**:
1. 打印此检查清单,贴在显示器旁边
2. 每次编译前快速扫一遍
3. 遇到错误时按照排查步骤逐项检查

**文档维护**: 每季度审查  
**下次审查**: 2026-01-13