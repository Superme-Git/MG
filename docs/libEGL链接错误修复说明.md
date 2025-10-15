# libEGL.lib 链接错误修复说明

## 📋 问题描述

**错误信息**:
```
4>LINK : fatal error LNK1104: 无法打开文件"libEGL.lib"
```

**发生时机**: 编译 `cocos2d-win32.vc2010.sln` 时

---

## 🔍 问题根本原因

通过深入分析,发现这是一个**平台配置错误**:

### 1. ANGLE 项目简介

- **ANGLE** (Almost Native Graphics Layer Engine) 是 Google 开发的项目
- 用于将 **OpenGL ES API** 调用转换为 **DirectX** 调用
- 主要用于移动平台(WinRT/WP8)在 Windows 上运行

### 2. Win32 vs 移动平台的差异

| 平台 | 使用的图形 API | 需要的库 |
|------|----------------|----------|
| **Win32 桌面** | 原生 OpenGL (通过 GLEW) | `opengl32.lib`, `glew32.lib` |
| **WinRT/WP8** | OpenGL ES (通过 ANGLE) | `libEGL.lib`, `libGLESv2.lib` |

### 3. 项目配置错误

- `cocos2d-win32.vcxproj` 中错误地包含了 ANGLE 库依赖
- 但 `cocos2d-win32.vc2010.sln` 解决方案中**没有包含 ANGLE 项目**
- Win32 平台根本**不需要**也**不应该使用** ANGLE

### 4. 代码证据

**Win32 平台头文件** (`cocos2dx/platform/win32/CCGL.h`):
```cpp
#ifndef __CCGL_H__
#define __CCGL_H__

#include "GL/glew.h"  // 使用原生 OpenGL

#define CC_GL_DEPTH24_STENCIL8    GL_DEPTH24_STENCIL8

#endif // __CCGL_H__
```

**移动平台代码** (WinRT/WP8):
```cpp
// 使用 EGL
EGLDisplay display = eglGetDisplay(EGL_DEFAULT_DISPLAY);
eglInitialize(display, NULL, NULL);
// ... 更多 EGL 代码
```

从代码搜索结果可以确认:
- 所有 `EGL` 和 `GLES` 引用都在移动平台代码中
- Win32 平台完全不使用 EGL/GLES

---

## ✅ 解决方案

从 `cocos2d-win32.vcxproj` 中移除不需要的 ANGLE 库依赖:

### 修改内容

**文件**: `cocos2d-2.0-rc2-x-2.0.1/cocos2dx/proj.win32/cocos2d-win32.vcxproj`

#### 修改 1: Debug 配置 (第 72 行)

```xml
<!-- 修改前 -->
<AdditionalDependencies>opengl32.lib;glew32.lib;libxml2.lib;libzlib.lib;libjpeg.lib;winmm.lib;libpng.lib;libtiff.lib;libiconv.lib;pthreadVCE2.lib;libwebp.lib;libEGL.lib;libGLESv2.lib;libBox2D.lib;%(AdditionalDependencies)</AdditionalDependencies>

<!-- 修改后 -->
<AdditionalDependencies>opengl32.lib;glew32.lib;libxml2.lib;libzlib.lib;libjpeg.lib;winmm.lib;libpng.lib;libtiff.lib;libiconv.lib;pthreadVCE2.lib;libwebp.lib;libBox2D.lib;%(AdditionalDependencies)</AdditionalDependencies>
```

#### 修改 2: Release 配置 (第 110 行)

```xml
<!-- 修改前 -->
<AdditionalDependencies>opengl32.lib;glew32.lib;libxml2.lib;libzlib.lib;libjpeg.lib;winmm.lib;libtiff.lib;libpng.lib;libiconv.lib;pthreadVCE2.lib;libEGL.lib;libGLESv2.lib;libBox2D.lib;libwebp.lib;%(AdditionalDependencies)</AdditionalDependencies>

<!-- 修改后 -->
<AdditionalDependencies>opengl32.lib;glew32.lib;libxml2.lib;libzlib.lib;libjpeg.lib;winmm.lib;libtiff.lib;libpng.lib;libiconv.lib;pthreadVCE2.lib;libBox2D.lib;libwebp.lib;%(AdditionalDependencies)</AdditionalDependencies>
```

**变更说明**: 移除了 `libEGL.lib` 和 `libGLESv2.lib`

---

## 📊 技术背景

### OpenGL vs OpenGL ES

| 特性 | OpenGL | OpenGL ES |
|------|--------|-----------|
| **目标平台** | 桌面系统 | 嵌入式/移动设备 |
| **功能集** | 完整功能 | 精简子集 |
| **Windows 支持** | 原生支持 | 需要 ANGLE 转换层 |
| **MT3 Win32 使用** | ✅ 是 | ❌ 否 |

### GLEW (OpenGL Extension Wrangler)

- 用于加载和管理 OpenGL 扩展
- Win32 版本的 Cocos2d-x 通过 GLEW 使用原生 OpenGL
- 已经在 `cocos2dx/platform/third_party/win32/libraries/glew32.lib` 中提供

---

## 🧪 验证方法

编译 `cocos2d-win32.vc2010.sln`:

```batch
REM 使用提供的批处理脚本
build_cocos2d.bat
```

或者直接编译:

```batch
call "C:\Program Files (x86)\Microsoft Visual Studio 14.0\VC\vcvarsall.bat" x86
devenv.com "cocos2d-2.0-rc2-x-2.0.1\cocos2d-win32.vc2010.sln" /Rebuild "Release|Win32"
```

**预期结果**: 
- ✅ 编译成功,没有 LNK1104 错误
- ✅ 生成 `libcocos2d.dll` 和 `libcocos2d.lib`
- ✅ 位于 `cocos2d-2.0-rc2-x-2.0.1/Release.win32/` 目录

---

## 📝 总结

### 问题本质

- 这是一个**跨平台配置污染**问题
- 移动平台(WinRT/WP8)的配置错误地混入了 Win32 桌面项目
- 导致 Win32 项目尝试链接本不需要的移动平台库

### 解决思路

1. **识别平台差异**: Win32 使用 OpenGL,移动平台使用 OpenGL ES
2. **清理配置**: 移除 Win32 项目中的 ANGLE 依赖
3. **保持简洁**: Win32 只需要 `opengl32.lib` 和 `glew32.lib`

### 相关修复

这是 MT3 项目 VS2015 兼容性修复系列的第 4 个问题:

1. ✅ C1083 - SimpleAudioEngine.h 头文件路径
2. ✅ LNK2005/LNK1169 - CRT 库冲突
3. ✅ C2011 - timespec 重定义
4. ✅ **LNK1104 - libEGL.lib 缺失 (本文档)**

---

**修复日期**: 2025-10-11  
**修复人员**: Claude Code Assistant  
**影响文件**: `cocos2d-2.0-rc2-x-2.0.1/cocos2dx/proj.win32/cocos2d-win32.vcxproj`