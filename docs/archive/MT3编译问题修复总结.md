# MT3 项目 VS2015 编译问题修复总结

## 📊 修复概览

**修复日期**: 2025-10-11  
**目标**: 使 MT3 项目能在 Visual Studio 2015 下成功编译  
**状态**: 🔄 进行中 (Cocos2d-x 库正在编译)

---

## ✅ 已修复的问题

### 问题 1: C1083 - 无法打开头文件

**错误信息**:
```
错误 C1083: 无法打开包括文件: "CocosDenshion/Include/SimpleAudioEngine.h": No such file or directory
```

**修复位置**: `client/MT3Win32App/SimpleAudioEngineAdapter.cpp:4`

**解决方案**:
```cpp
// 修改前
#include "CocosDenshion/Include/SimpleAudioEngine.h"

// 修改后
#include "SimpleAudioEngine.h"
```

**原因**: 不正确的相对路径引用

**文档**: 基本修复,已记录在主文档中

---

### 问题 2: LNK2005/LNK1169 - C 运行时库冲突

**错误信息**:
```
错误 LNK2005: __crt_debugger_hook 已经在 MSVCRT.lib 中定义
错误 LNK1169: 找到一个或多个多重定义的符号
```

**修复位置**: `client/MT3Win32App/mt3.win32.vcxproj`

**解决方案**:
1. 将 RuntimeLibrary 改为动态 CRT (`MultiThreadedDLL`)
2. 添加 `legacy_stdio_definitions.lib` 到链接器输入

**根本原因**: 
- VS2015 引入了新的 Universal C Runtime (UCRT)
- 旧库使用旧的 MSVCRT,新程序使用 UCRT
- 两者混用导致符号重复定义

**文档**: `doc/VS2015兼容性补丁说明.md`

---

### 问题 3: C2011 - timespec 结构体重定义

**错误信息**:
```
错误 C2011: "timespec":"struct"类型重定义
```

**修复位置**: `cocos2d-2.0-rc2-x-2.0.1/cocos2dx/platform/third_party/win32/pthread/pthread.h:305-317`

**解决方案**:
```c
// 添加 VS2015 版本检查
#if !defined(_TIMESPEC_DEFINED) && !defined(_INC_TIME) && !defined(TIMESPEC_DEFINED) && (_MSC_VER < 1900)
// VS2015 之前的版本需要定义 timespec
struct timespec {
    long tv_sec;
    long tv_nsec;
};
#endif
```

**根本原因**:
- VS2015 在 `<time.h>` 中已经定义了 `timespec`
- pthread.h 重复定义导致编译错误
- `_MSC_VER >= 1900` 表示 VS2015 或更新版本

**文档**: `doc/VS2015兼容性补丁说明.md`

---

### 问题 4: LNK1104 - 无法打开 libEGL.lib ⭐

**错误信息**:
```
4>LINK : fatal error LNK1104: 无法打开文件"libEGL.lib"
```

**修复位置**: `cocos2d-2.0-rc2-x-2.0.1/cocos2dx/proj.win32/cocos2d-win32.vcxproj`

**解决方案**:
从 Debug 和 Release 配置中移除 `libEGL.lib` 和 `libGLESv2.lib`

```xml
<!-- Debug 配置 (第 72 行) -->
<!-- 移除: libEGL.lib;libGLESv2.lib -->
<AdditionalDependencies>opengl32.lib;glew32.lib;...;libBox2D.lib;%(AdditionalDependencies)</AdditionalDependencies>

<!-- Release 配置 (第 110 行) -->
<!-- 移除: libEGL.lib;libGLESv2.lib -->
<AdditionalDependencies>opengl32.lib;glew32.lib;...;libBox2D.lib;%(AdditionalDependencies)</AdditionalDependencies>
```

**根本原因**:
- **ANGLE** (EGL/GLES) 是为移动平台(WinRT/WP8)设计的
- **Win32 桌面平台**使用原生 OpenGL (通过 GLEW)
- 项目配置错误地包含了移动平台的库依赖
- 这些库根本不存在也不需要

**技术背景**:
| 平台 | 图形 API | 需要的库 |
|------|----------|----------|
| Win32 | 原生 OpenGL | opengl32.lib, glew32.lib |
| WinRT/WP8 | OpenGL ES (ANGLE) | libEGL.lib, libGLESv2.lib |

**文档**: `doc/libEGL链接错误修复说明.md` (详细技术分析)

---

## 🔧 技术要点总结

### 1. Visual Studio 2015 的重大变更

#### Universal C Runtime (UCRT)
- VS2015 引入了新的 C 运行时系统
- 所有库和程序必须使用相同版本的 CRT
- 不兼容 VS2010/VS2012/VS2013 编译的库

#### 解决方案
- **动态链接 CRT**: 使用 `/MD` (Release) 或 `/MDd` (Debug)
- **添加兼容库**: `legacy_stdio_definitions.lib`
- **重新编译**: 所有依赖库必须用 VS2015 重新编译

### 2. 平台特定代码的识别

#### Win32 vs 移动平台
```cpp
// Win32 平台 (CCGL.h)
#include "GL/glew.h"  // 原生 OpenGL

// WinRT/WP8 平台
#include <EGL/egl.h>  // OpenGL ES
#include <GLES2/gl2.h>
```

#### 条件编译宏
```cpp
#if CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
    // Win32 特定代码
#elif CC_TARGET_PLATFORM == CC_PLATFORM_WINRT
    // WinRT 特定代码
#endif
```

### 3. 编译器版本检测

```cpp
// 检测 Visual Studio 版本
#if _MSC_VER >= 1900  // VS2015
    // VS2015+ 特定代码
#elif _MSC_VER >= 1800  // VS2013
    // VS2013 特定代码
#endif
```

**版本对照表**:
| Visual Studio | _MSC_VER |
|---------------|----------|
| VS2010 | 1600 |
| VS2012 | 1700 |
| VS2013 | 1800 |
| **VS2015** | **1900** |
| VS2017 | 1910-1916 |

---

## 📁 修改的文件清单

### 1. 主程序文件

✅ **client/MT3Win32App/SimpleAudioEngineAdapter.cpp**
- 修改: 第 4 行,头文件路径

✅ **client/MT3Win32App/mt3.win32.vcxproj**
- 修改: RuntimeLibrary 配置
- 修改: 添加 legacy_stdio_definitions.lib

### 2. Cocos2d-x 引擎文件

✅ **cocos2d-2.0-rc2-x-2.0.1/cocos2dx/platform/third_party/win32/pthread/pthread.h**
- 修改: 第 305-317 行,添加 VS2015 版本检查

✅ **cocos2d-2.0-rc2-x-2.0.1/cocos2dx/proj.win32/cocos2d-win32.vcxproj**
- 修改: 第 72 行 (Debug 配置)
- 修改: 第 110 行 (Release 配置)
- 移除: libEGL.lib 和 libGLESv2.lib 依赖

### 3. 新增文档

📄 **doc/MT3项目编译问题分析与解决方案.md** (731 行)
- 完整的技术分析和解决方案

📄 **doc/MT3编译快速指南.md** (243 行)
- 快速编译步骤指南

📄 **doc/VS2015兼容性补丁说明.md** (440 行)
- CRT 兼容性详细说明

📄 **doc/libEGL链接错误修复说明.md** (175 行)
- OpenGL vs OpenGL ES 技术分析

📄 **doc/MT3编译问题修复总结.md** (本文档)
- 所有修复的汇总

### 4. 新增脚本

🔧 **build_cocos2d.bat**
- 自动编译 Cocos2d-x 引擎库

---

## 📋 编译步骤

### 阶段 1: 编译 Cocos2d-x 引擎 (当前进行中)

```batch
build_cocos2d.bat
```

**预期输出**:
```
cocos2d-2.0-rc2-x-2.0.1/Release.win32/
├── libcocos2d.dll
├── libcocos2d.lib
├── libBox2D.lib
├── libchipmunk.lib
├── libCocosDenshion.lib
└── liblua.lib
```

### 阶段 2: 编译 MT3 依赖库 (待进行)

```batch
# 待创建脚本
build_dependencies.bat
```

需要编译:
- common/platform → platform.lib
- common/lua → lua.lib
- common/ljfm → ljfm.lib
- common/cauthc → cauthc.lib
- engine → engine.lib

### 阶段 3: 编译 MT3 主程序 (待进行)

```batch
# 待创建脚本
build_mt3.bat
```

**最终输出**: `client/MT3Win32App/Release.win32/MT3.exe`

---

## 🎯 下一步计划

### 立即的下一步
1. ⏳ **等待 Cocos2d-x 编译完成**
2. ✅ **验证 libcocos2d.dll 生成**
3. 📝 **创建依赖库编译脚本**

### 后续步骤
4. 🔨 编译 platform.lib
5. 🔨 编译 lua.lib  
6. 🔨 编译 ljfm.lib
7. 🔨 编译 cauthc.lib
8. 🔨 编译 engine.lib
9. 🔨 编译 MT3.exe
10. 🧪 测试运行

---

## 💡 关键经验教训

### 1. 版本兼容性至关重要
- 所有库必须使用相同的编译器版本
- CRT 库冲突是最常见的链接错误

### 2. 平台特定代码需要识别
- Win32 桌面 ≠ WinRT/WP8 移动平台
- 不要混用平台特定的依赖库

### 3. 第三方库需要仔细检查
- pthread.h 可能与新版本 SDK 冲突
- 需要添加版本检查保护

### 4. 文档化是必须的
- 详细记录每一个修复
- 包含技术原因和背景知识
- 便于未来维护和排查

---

## 📞 问题排查指南

### 遇到 LNK2005 错误
- 检查是否所有项目使用相同的 RuntimeLibrary
- 确保添加了 legacy_stdio_definitions.lib

### 遇到 C2011 重定义错误
- 检查是否有多个库定义了相同的结构体
- 添加 `#ifndef` 保护或版本检查

### 遇到 LNK1104 找不到库文件
- 检查是否真的需要这个库
- 确认库是否已经编译生成
- 验证库搜索路径配置

### 遇到 C1083 找不到头文件
- 检查 #include 路径是否正确
- 验证 AdditionalIncludeDirectories 配置
- 确认文件确实存在

---

**最后更新**: 2025-10-11 23:43 CST  
**编译状态**: Cocos2d-x 正在编译中...  
**下一个里程碑**: Cocos2d-x 编译完成 → 依赖库编译