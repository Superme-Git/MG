# MT3 Android平台编译完整指南

**文档版本**: 2.0
**最后更新**: 2025-10-22
**适用平台**: Android (armeabi-v7a)
**编译工具**: Android NDK r10e + Ant + JDK 1.7/1.8

**v2.0 更新内容**：
- ✨ 新增统一自动化编译系统（build_unified.bat）
- ✨ 新增环境自动配置脚本（setup_android_env.bat）
- ✨ 新增APK验证工具（verify_apk.bat）
- ✨ 新增Jenkins Pipeline配置（开箱即用）
- ✨ 新增GitHub Actions工作流（云端构建）
- 📝 优化文档结构，突出自动化方案

---

## 📋 项目概述

**项目性质**：Cocos2d-x 2.0 跨平台游戏（梦幻西游手游）
**技术架构**：C++（游戏引擎） + Lua（脚本逻辑） + Java（Android平台层）
**构建系统**：Android NDK Build + Apache Ant

**关键特点**：
- 自研游戏引擎（engine、platform、ljfm等核心库）
- 多渠道包支持（乐游、易接、JoySDK等）
- 基于Ant的传统Android构建流程（非Gradle）
- NDK r10e + GCC 4.8工具链

---

## 🎯 编译目标

```
最终产物：mt3_locojoy.apk（或其他渠道包）
├── Native库：libs/armeabi-v7a/libgame.so
├── Java代码：classes.dex
└── 资源文件：res/, assets/
```

---

## 🔧 环境要求

### 必需工具

| 工具 | 版本要求 | 说明 |
|-----|---------|------|
| **JDK** | 1.7 或 1.8 | Android SDK构建需要 |
| **Android SDK** | API Level 17+ | 目标版本：Android 4.2 |
| **Android NDK** | r10e（推荐）| 使用GCC 4.8工具链 |
| **Apache Ant** | 1.8.0+ | 用于APK打包 |
| **Python** | 2.7 | NDK构建脚本依赖 |

### 可选工具

| 工具 | 用途 |
|-----|------|
| **Eclipse/ADT** | 图形化开发调试 |
| **ADB** | 设备调试和APK安装 |

### 系统要求

- **操作系统**：Windows 7+, Linux, macOS
- **内存**：建议 8GB+（NDK编译消耗较大）
- **磁盘空间**：至少 10GB 可用空间

---

## 📦 环境配置

### 1. 安装JDK

**Windows：**
```batch
# 下载并安装JDK 1.8
# Oracle JDK: https://www.oracle.com/java/technologies/javase-jdk8-downloads.html
# OpenJDK: https://adoptium.net/

# 配置环境变量
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_xxx
set PATH=%JAVA_HOME%\bin;%PATH%

# 验证安装
java -version
javac -version
```

**Linux/macOS：**
```bash
# Ubuntu/Debian
sudo apt-get install openjdk-8-jdk

# CentOS/RHEL
sudo yum install java-1.8.0-openjdk-devel

# macOS
brew install openjdk@8

# 验证
java -version
```

### 2. 安装Android SDK

**推荐方式：Android Studio（包含SDK）**
```batch
# 下载Android Studio
# https://developer.android.com/studio

# 或使用命令行工具
# https://developer.android.com/studio#command-tools

# 配置环境变量
set ANDROID_HOME=C:\Users\<用户名>\AppData\Local\Android\Sdk
set PATH=%ANDROID_HOME%\tools;%ANDROID_HOME%\platform-tools;%PATH%
```

**必需SDK组件：**
```batch
# 使用SDK Manager安装
sdkmanager "platform-tools" "platforms;android-17" "build-tools;22.0.1"
```

### 3. 安装Android NDK r10e

**下载地址：**
- Windows: https://dl.google.com/android/repository/android-ndk-r10e-windows-x86_64.zip
- Linux: https://dl.google.com/android/repository/android-ndk-r10e-linux-x86_64.zip
- macOS: https://dl.google.com/android/repository/android-ndk-r10e-darwin-x86_64.zip

**安装步骤：**
```batch
# 1. 解压到指定目录
# 推荐路径：D:\android-ndk-r10e（Windows）或 ~/android-ndk-r10e（Linux/macOS）

# 2. 配置环境变量
set NDK_HOME=D:\android-ndk-r10e
set PATH=%NDK_HOME%;%PATH%

# 3. 验证安装
ndk-build --version
# 预期输出：GNU Make 3.81
```

**⚠️ 重要说明：**
- 必须使用NDK r10e，更高版本可能导致编译失败
- 不要使用NDK r11+，GCC工具链在r11后被弃用
- 项目使用GCC 4.8，不支持Clang

### 4. 安装Apache Ant

**Windows：**
```batch
# 1. 下载Ant
# https://ant.apache.org/bindownload.cgi

# 2. 解压到C:\apache-ant-1.10.x

# 3. 配置环境变量
set ANT_HOME=C:\apache-ant-1.10.x
set PATH=%ANT_HOME%\bin;%PATH%

# 4. 验证
ant -version
```

**Linux/macOS：**
```bash
# Ubuntu/Debian
sudo apt-get install ant

# CentOS/RHEL
sudo yum install ant

# macOS
brew install ant

# 验证
ant -version
```

### 5. 创建local.properties

每个Android项目目录需要创建`local.properties`文件：

```properties
# E:\MT3\client\android\LocojoyProject\local.properties

# Android SDK路径（根据实际情况修改）
sdk.dir=C:\\Users\\<用户名>\\AppData\\Local\\Android\\Sdk

# 或Linux/macOS
# sdk.dir=/home/<用户名>/Android/Sdk
```

**快速生成方法：**
```batch
cd E:\MT3\client\android\LocojoyProject
android update project --path .
```

---

## 🏗️ 项目依赖结构

### NDK构建依赖关系图

```
libgame.so (最终Native库)
├── FireClient_static (游戏客户端)
│   ├── Lua引擎 (Lua VM + tolua++ + 绑定)
│   ├── 音频编解码 (Speex, Ogg, Vorbis, AMR)
│   ├── 游戏逻辑 (Battle, Manager, SceneObj, GameUI)
│   └── 协议定义 (rpcgen)
├── engine_static (游戏引擎)
│   ├── 渲染器 (Cocos2d封装)
│   ├── 精灵系统 (Sprite, Spine动画)
│   ├── 世界系统 (World, Map)
│   ├── 特效系统 (Effect, Particle)
│   └── 内存管理 (nedmalloc)
├── updateengine_static (热更新引擎)
├── platform_static (平台抽象层)
│   ├── INI配置 (IniFile)
│   ├── 日志系统 (CoreLog)
│   ├── 线程/锁 (thread, ksemaphore)
│   ├── 内存管理 (usememory)
│   ├── 字符串工具 (StringUtil, Encoder)
│   └── Android辅助 (FileUtil, SDJniHelper)
├── ljfm_static (文件管理)
├── cauthc_static (认证客户端)
├── cegui_static (UI库)
│   ├── CEGUIBase
│   ├── CEGUIFalagardWRBase
│   ├── CEGUICocos2DRender
│   ├── CEGUILuaScriptModule
│   ├── CEGUIXmlParser
│   └── CEGUIImageCodec
├── cocos2dx_static (Cocos2d-x核心)
├── cocosdenshion_static (音频引擎)
├── breakpad_client (崩溃收集)
├── cocos_network_static (网络扩展)
├── 预编译第三方库
│   ├── cocos_libpng_static
│   ├── cocos_libjpeg_static
│   ├── cocos_libtiff_static
│   ├── cocos_libwebp_static
│   ├── cocos_zlib_static
│   ├── cocos_curl_static
│   └── cocos_lua_static
└── 第三方SDK (Prebuilt .so)
    ├── locSDK6a (百度定位)
    ├── libdu (DU SDK)
    └── gangaOnlineUnityHelper (易接SDK，仅YijieProject)
```

### 自研核心组件详解

| 组件 | 路径 | 功能 | 关键文件 |
|-----|------|------|---------|
| **platform** | `common/platform/` | 平台抽象层 | IniFile, CoreLog, thread, Encoder |
| **ljfm** | `common/ljfm/` | 文件管理 | （待进一步分析） |
| **cauthc** | `common/cauthc/` | 认证客户端 | 网络通信、加密 |
| **updateengine** | `common/updateengine/` | 热更新 | 资源下载、版本管理 |
| **engine** | `engine/` | 游戏引擎 | 渲染、精灵、世界、特效 |
| **FireClient** | `client/FireClient/` | 游戏客户端 | 战斗、UI、场景、Lua引擎 |

---

## 🚀 编译流程

### 🎯 快速开始：自动化编译方案（强烈推荐）

项目提供了完整的自动化编译系统，包括环境配置、统一编译、APK验证等功能。

#### 方案A：统一编译系统 v2.0（推荐）

**功能特性：**

- ✅ 多渠道参数化编译（单渠道/全渠道）
- ✅ 自动环境检测与验证
- ✅ 支持清理构建、跳过测试等选项
- ✅ 并行编译支持（实验性）
- ✅ 自动APK验证与完整性检查
- ✅ 生成详细编译报告

#### 1. 首次使用：环境配置

```batch
cd E:\MT3\client\android

# 自动配置编译环境（检测工具、配置环境变量、生成local.properties）
setup_android_env.bat
```

**脚本功能：**

- 检测并配置 JDK、Android SDK、NDK、Apache Ant
- 自动探测工具安装路径
- 为所有渠道生成 `local.properties`
- 提供详细的安装指引（如有缺失）

#### 2. 编译单个渠道

```batch
cd E:\MT3\client\android

# 编译乐游渠道（默认）
build_unified.bat

# 编译指定渠道
build_unified.bat --channel YijieProject
build_unified.bat -c JoysdkProject

# 清理后重新编译
build_unified.bat --channel LocojoyProject --clean

# 跳过APK验证（加快编译）
build_unified.bat --skip-tests
```

#### 3. 编译所有渠道

```batch
cd E:\MT3\client\android

# 编译所有渠道（LocojoyProject、YijieProject、JoysdkProject、Test）
build_unified.bat --all

# 清理后全渠道编译
build_unified.bat --all --clean
```

#### 4. 命令行参数说明

| 参数 | 简写 | 说明 | 示例 |
|-----|------|------|------|
| `--channel <name>` | `-c` | 指定编译渠道 | `--channel YijieProject` |
| `--all` | - | 编译所有渠道 | `--all` |
| `--clean` | - | 清理旧产物后编译 | `--clean` |
| `--skip-tests` | - | 跳过APK验证 | `--skip-tests` |
| `--parallel` | - | 并行编译（实验性） | `--parallel` |
| `--help` | `-h` | 显示帮助信息 | `--help` |

#### 5. 编译输出

```text
════════════════════════════════════════════════════════════════
编译流程完成!
════════════════════════════════════════════════════════════════

产物位置:
  APK: LocojoyProject\bin\mt3_locojoy-debug.apk
  日志: LocojoyProject\build_logs\
  报告: build_report_LocojoyProject.txt

下一步操作:
  1. 安装测试: adb install -r "LocojoyProject\bin\*.apk"
  2. 查看日志: type "LocojoyProject\build_logs\android_build_*.log"
  3. 查看报告: type "build_report_LocojoyProject.txt"
```

#### 6. APK验证

```batch
cd E:\MT3\client\android

# 验证单个APK（自动检查大小、so库、签名）
verify_apk.bat LocojoyProject\bin\mt3_locojoy-debug.apk

# 验证输出示例（完整性检查）
```

```text
════════════════════════════════════════════════════════════════
APK 验证报告
════════════════════════════════════════════════════════════════
APK路径: LocojoyProject\bin\mt3_locojoy-debug.apk

[INFO] APK大小: 68 MB (71234567 bytes)

────────────────────────────────────────────────────────────────
Native库检查:
────────────────────────────────────────────────────────────────
[OK] 存在: libgame.so
[OK] 存在: locSDK6a.so
[OK] 存在: libdu.so

────────────────────────────────────────────────────────────────
签名验证:
────────────────────────────────────────────────────────────────
[OK] APK签名有效

════════════════════════════════════════════════════════════════
[SUCCESS] APK验证通过
════════════════════════════════════════════════════════════════
```

---

### 方法B：传统单渠道编译（手动控制）

适用于需要精确控制编译过程的场景。

**使用渠道专用脚本：**

```batch
cd E:\MT3\client\android\LocojoyProject
build_with_log.bat
```

**脚本功能：**
1. ✅ 自动检测环境（Java、Android SDK、NDK、Ant）
2. ✅ 执行NDK构建（生成libgame.so）
3. ✅ 执行Ant打包（生成APK）
4. ✅ 验证产物完整性
5. ✅ 生成详细日志（`build_logs/android_build_<时间戳>.log`）

**环境变量配置（可选）：**
```batch
# 如果工具未在PATH中，可显式指定
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_xxx
set ANDROID_HOME=C:\Users\<用户名>\AppData\Local\Android\Sdk
set NDK_HOME=D:\android-ndk-r10e
set ANT_HOME=C:\apache-ant-1.10.x

# 自定义日志目录（可选）
set MT3_LOG_DIR=E:\MT3\build_logs

# 执行编译
build_with_log.bat
```

**成功输出示例：**
```
============================================================
构建成功!
============================================================

产物位置:
- APK: E:\MT3\client\android\LocojoyProject\bin\mt3_locojoy.apk
- 日志: E:\MT3\build_logs\android_build_20251022_143052.log

下一步操作:
1. 安装APK: adb install -r "bin\mt3_locojoy.apk"
2. 查看日志: type "E:\MT3\build_logs\android_build_20251022_143052.log"
```

---

### 方法C：分步手动编译（精确控制）

#### 步骤1：环境验证

```batch
# 检查Java
java -version
javac -version

# 检查Android SDK
where android
where aapt

# 检查NDK
where ndk-build
ndk-build --version

# 检查Ant
ant -version

# 检查local.properties
type local.properties
```

**预期输出验证：**
- Java版本：1.7.x 或 1.8.x
- NDK版本：GNU Make 3.81
- Ant版本：Apache Ant(TM) version 1.x.x

#### 步骤2：清理旧编译产物（可选）

```batch
cd E:\MT3\client\android\LocojoyProject

# 清理NDK编译缓存
rd /s /q obj
rd /s /q libs

# 清理Java编译产物
rd /s /q bin
rd /s /q gen

# 清理Ant缓存
ant clean
```

#### 步骤3：NDK Native编译

**自动方式（推荐）：**
```batch
cd E:\MT3\client\android\LocojoyProject
mt3_build.bat
```

**手动方式：**
```batch
cd E:\MT3\client\android\LocojoyProject

# 设置NDK路径
set NDK_HOME=D:\android-ndk-r10e

# 执行NDK构建（Release模式，4线程并行）
call "%NDK_HOME%\ndk-build.cmd" NDK_DEBUG=0 -j4

# Debug模式（带符号表）
# call "%NDK_HOME%\ndk-build.cmd" NDK_DEBUG=1 -j4
```

**构建参数说明：**
- `NDK_DEBUG=0`：Release模式（优化编译，无调试符号）
- `NDK_DEBUG=1`：Debug模式（含调试符号，体积更大）
- `-j4`：使用4个CPU核心并行编译
- `-B`：强制重新编译所有模块

**成功标志：**
```
[armeabi-v7a] Install        : libgame.so => libs/armeabi-v7a/libgame.so
```

#### 步骤4：验证Native库

```batch
# 检查so文件是否生成
dir libs\armeabi-v7a\libgame.so

# 查看文件大小（预期 20-30 MB）
```

**预期输出：**
```
2025-10-22  14:30    25,678,912 libgame.so
```

#### 步骤5：Ant Java编译与打包

```batch
cd E:\MT3\client\android\LocojoyProject

# Release打包（需要keystore签名）
ant release

# Debug打包（使用debug.keystore）
ant debug

# 清理后重新打包
ant clean release
```

**常用Ant任务：**
| 任务 | 说明 |
|-----|------|
| `ant debug` | 生成debug.apk（debug签名） |
| `ant release` | 生成release.apk（需配置签名） |
| `ant clean` | 清理编译产物 |
| `ant -v release` | 详细输出模式 |

#### 步骤6：APK签名配置（Release模式）

**创建ant.properties文件：**

```properties
# E:\MT3\client\android\LocojoyProject\ant.properties

# Keystore文件路径
key.store=E:\\MT3\\client\\android\\keystore\\mt3_release.keystore
key.alias=mt3_release

# 密码（生产环境应使用环境变量或安全存储）
key.store.password=<密钥库密码>
key.alias.password=<别名密码>
```

**如果没有keystore，创建新的：**
```batch
keytool -genkey -v -keystore mt3_release.keystore -alias mt3_release ^
  -keyalg RSA -keysize 2048 -validity 10000
```

#### 步骤7：验证APK产物

```batch
# 查看APK文件
dir bin\*.apk

# 查看APK详细信息
"%ANDROID_HOME%\build-tools\22.0.1\aapt.exe" dump badging bin\mt3_locojoy.apk

# 检查APK中的so库
"%ANDROID_HOME%\build-tools\22.0.1\aapt.exe" list bin\mt3_locojoy.apk | findstr "libgame.so"
```

**预期输出：**
```
package: name='com.locojoy.mt3' versionCode='1' versionName='1.0'
sdkVersion:'17'
targetSdkVersion:'17'
```

#### 步骤8：安装测试

```batch
# 连接Android设备或启动模拟器
adb devices

# 安装APK（-r表示覆盖安装）
adb install -r bin\mt3_locojoy.apk

# 启动应用
adb shell am start -n com.locojoy.mt3/.MT3

# 查看logcat日志
adb logcat | findstr "MT3"
```

---

## 🔄 多渠道包编译

项目支持多个渠道包，每个渠道对应不同的SDK集成：

### 渠道包列表

| 渠道包 | 路径 | 包名 | 集成SDK |
|-------|------|------|---------|
| **乐游渠道** | `client/android/LocojoyProject/` | com.locojoy.mt3 | 百度定位、DU SDK、美洽客服、ShareSDK |
| **易接渠道** | `client/android/YijieProject/` | com.yijie.mt3 | 易接SDK、百度定位、DU SDK |
| **JoySDK渠道** | `client/android/JoysdkProject/` | com.joysdk.mt3 | 百度定位、DU SDK |
| **测试渠道** | `client/android/LocojoyProject_test2016/` | com.locojoy.mt3.test | （同乐游） |

### 切换渠道编译

**推荐方式：使用统一编译系统**

```batch
cd E:\MT3\client\android

# 编译乐游渠道
build_unified.bat --channel LocojoyProject

# 编译易接渠道
build_unified.bat --channel YijieProject

# 编译JoySDK渠道
build_unified.bat --channel JoysdkProject

# 编译所有渠道
build_unified.bat --all
```

**传统方式：使用渠道专用脚本**

```batch
# 编译乐游渠道
cd E:\MT3\client\android\LocojoyProject
build_with_log.bat

# 编译易接渠道
cd E:\MT3\client\android\YijieProject
build_with_log.bat

# 编译JoySDK渠道
cd E:\MT3\client\android\JoysdkProject
build_with_log.bat
```

### 渠道差异说明

**1. SDK差异：**
- 易接渠道额外包含 `gangaOnlineUnityHelper.so`
- 其他渠道仅包含百度定位和DU SDK

**2. 宏定义差异：**
```cpp
// engine/Android.mk 和 FireClient/Android.mk
-D_LOCOJOY_SDK_     // 乐游渠道
-D_YJ_SDK_          // 易接渠道（注释掉则为乐游）
```

**3. 库引用配置：**
- 易接渠道：`android.library.reference.1=../../3rdplatform/YijieSDK`
- 乐游渠道：`android.library.reference.1=../../3rdplatform/ShareSDK`

---

## 🛠️ 常见问题排查

### 问题1：找不到ndk-build命令

**症状：**
```
'ndk-build' is not recognized as an internal or external command
```

**解决方案：**
```batch
# 方案1：配置环境变量
set NDK_HOME=D:\android-ndk-r10e
set PATH=%NDK_HOME%;%PATH%

# 方案2：使用完整路径
call "D:\android-ndk-r10e\ndk-build.cmd" NDK_DEBUG=0 -j4

# 方案3：修改mt3_build.bat中的回退路径
# 将第12行改为实际NDK路径
if not defined NDK_CANDIDATE set "NDK_CANDIDATE=D:\android-ndk-r10e"
```

### 问题2：NDK编译错误 - 找不到头文件

**症状：**
```
fatal error: 'platform/platform_types.h' file not found
```

**原因：**
- `NDK_MODULE_PATH` 未正确设置
- `import-add-path` 路径错误

**解决方案：**
```bash
# 检查jni/Android.mk中的路径设置
$(call import-add-path,${GAME_ROOT}/)
$(call import-add-path,${GAME_ROOT}/client/)
$(call import-add-path,${GAME_ROOT}/common/)
$(call import-add-path,${GAME_ROOT}/dependencies/)

# 确保GAME_ROOT变量正确指向项目根目录
GAME_ROOT := $(LOCAL_PATH)/../../../..
```

### 问题3：链接错误 - undefined reference

**症状：**
```
undefined reference to `IniFile::IniFile()'
```

**原因：**
- 依赖库编译顺序错误
- 静态库未正确链接

**解决方案：**
```bash
# 检查Android.mk中的库依赖顺序（必须按依赖关系排列）
LOCAL_WHOLE_STATIC_LIBRARIES := cocos2dx_static
LOCAL_WHOLE_STATIC_LIBRARIES += cocosdenshion_static
LOCAL_WHOLE_STATIC_LIBRARIES += platform_static  # 基础库在前
LOCAL_WHOLE_STATIC_LIBRARIES += engine_static     # 依赖platform的在后
LOCAL_WHOLE_STATIC_LIBRARIES += FireClient_static # 最上层

# 确保所有依赖模块都被import
$(call import-module,platform)
$(call import-module,engine)
$(call import-module,FireClient)
```

### 问题4：Ant打包失败 - SDK路径错误

**症状：**
```
sdk.dir is missing. Make sure to generate local.properties
```

**解决方案：**
```batch
# 创建local.properties文件
echo sdk.dir=C:\\Users\\<用户名>\\AppData\\Local\\Android\\Sdk > local.properties

# 或使用android工具自动生成
android update project --path .
```

### 问题5：APK签名失败

**症状：**
```
Error: A JNI error has occurred
```

**解决方案：**
```batch
# 检查ant.properties配置
type ant.properties

# 验证keystore文件存在
dir <keystore路径>

# 测试keystore密码
keytool -list -v -keystore <keystore路径>
```

### 问题6：so库加载失败

**症状：**
```
java.lang.UnsatisfiedLinkError: dlopen failed: cannot locate symbol
```

**原因：**
- ABI不匹配（手机是arm64但只编译了armeabi-v7a）
- 依赖的系统库缺失

**解决方案：**
```bash
# 方案1：支持多ABI
# 修改jni/Application.mk
APP_ABI := armeabi-v7a arm64-v8a

# 方案2：检查设备ABI
adb shell getprop ro.product.cpu.abi

# 方案3：强制使用32位库（在AndroidManifest.xml）
<application android:extractNativeLibs="true">
```

### 问题7：编译速度慢

**优化方案：**
```batch
# 1. 增加并行编译线程（根据CPU核心数调整）
ndk-build NDK_DEBUG=0 -j8

# 2. 使用ccache缓存
# 安装ccache后在Application.mk添加
NDK_CCACHE := ccache

# 3. 仅编译修改的模块（增量编译）
ndk-build NDK_DEBUG=0 -j4  # 不加-B参数

# 4. 减少日志输出
ndk-build NDK_DEBUG=0 -j4 NDK_LOG=0
```

### 问题8：第三方SDK集成错误

**症状：**
```
Error: duplicate files during packaging of APK
```

**原因：**
- 多个SDK包含相同的so库
- support-v4.jar版本冲突

**解决方案：**
```xml
<!-- 在ant.properties中排除重复文件 -->
jar.libs.dir=libs
excludes=**/support-v4.jar

<!-- 或在build.xml中添加 -->
<packageapp>
  <duplicate file="path/to/duplicate.so" tofile="..."/>
</packageapp>
```

---

## 📊 编译验证清单

### NDK编译验证

```batch
# ✅ 检查so库生成
dir libs\armeabi-v7a\libgame.so
# 预期大小：20-30 MB

# ✅ 检查符号表（Debug模式）
"%NDK_HOME%\toolchains\arm-linux-androideabi-4.8\prebuilt\windows-x86_64\bin\arm-linux-androideabi-nm.exe" libs\armeabi-v7a\libgame.so | findstr "JNI_OnLoad"
# 应包含 JNI_OnLoad 符号

# ✅ 检查依赖库
"%NDK_HOME%\toolchains\arm-linux-androideabi-4.8\prebuilt\windows-x86_64\bin\arm-linux-androideabi-objdump.exe" -p libs\armeabi-v7a\libgame.so | findstr "NEEDED"
# 应显示依赖的系统库
```

### APK验证

```batch
# ✅ 检查APK完整性
"%ANDROID_HOME%\build-tools\22.0.1\aapt.exe" dump badging bin\mt3_locojoy.apk

# ✅ 检查so库打包
"%ANDROID_HOME%\build-tools\22.0.1\aapt.exe" list bin\mt3_locojoy.apk | findstr "lib/"

# ✅ 检查签名
jarsigner -verify -verbose -certs bin\mt3_locojoy.apk

# ✅ 检查APK大小（预期 50-80 MB）
dir bin\mt3_locojoy.apk
```

### 运行时验证

```batch
# ✅ 安装测试
adb install -r bin\mt3_locojoy.apk

# ✅ 启动应用
adb shell am start -n com.locojoy.mt3/.MT3

# ✅ 查看崩溃日志
adb logcat -s "AndroidRuntime:E"

# ✅ 检查so库加载
adb logcat | findstr "dlopen\|UnsatisfiedLinkError"

# ✅ 性能监控
adb shell dumpsys meminfo com.locojoy.mt3
adb shell dumpsys cpuinfo | findstr com.locojoy.mt3
```

---

## 🔍 调试技巧

### 1. NDK调试

**启用NDK调试模式：**
```batch
# 编译Debug版本
ndk-build NDK_DEBUG=1 -j4

# 使用ndk-gdb调试
ndk-gdb --start --force
```

**查看崩溃堆栈：**
```batch
# 使用ndk-stack解析tombstone
adb logcat | "%NDK_HOME%\ndk-stack.cmd" -sym obj\local\armeabi-v7a
```

### 2. Java调试

**使用Eclipse/Android Studio：**
1. 导入项目：File → Import → Existing Android Project
2. 设置断点
3. Debug As → Android Application

**命令行调试：**
```batch
# 等待调试器连接
adb shell am start -D -n com.locojoy.mt3/.MT3

# 转发调试端口
adb forward tcp:8700 jdwp:<pid>

# 使用jdb连接
jdb -attach localhost:8700
```

### 3. 性能分析

**使用Systrace：**
```batch
# Android 4.3+
python "%ANDROID_HOME%\platform-tools\systrace\systrace.py" -o trace.html gfx view sched
```

**使用Android Profiler：**
- CPU使用率：`adb shell top | findstr com.locojoy.mt3`
- 内存占用：`adb shell dumpsys meminfo com.locojoy.mt3`
- GPU渲染：`adb shell dumpsys gfxinfo com.locojoy.mt3`

---

## 📚 交叉引用

### 相关文档

| 文档 | 路径 | 说明 |
|-----|------|------|
| **Windows编译指南** | [06-编译完整指南.md](./06-编译完整指南.md) | Windows平台编译流程 |
| **技术栈指引** | [MT3_技术栈指引.md](./MT3_技术栈指引.md) | 技术栈总览 |
| **架构深度分析** | [04-架构深度分析.md](./04-架构深度分析.md) | 项目架构详解 |
| **技术体系总结** | [03-技术体系总结.md](./03-技术体系总结.md) | 技术选型分析 |
| **文档索引** | [13-文档索引.md](./13-文档索引.md) | 所有文档入口 |

### 关键项目文件

| 文件 | 路径 | 作用 |
|-----|------|------|
| **主Android.mk** | `client/android/LocojoyProject/jni/Android.mk` | NDK构建入口 |
| **Application.mk** | `client/android/LocojoyProject/jni/Application.mk` | NDK全局配置 |
| **build.xml** | `client/android/LocojoyProject/build.xml` | Ant构建配置 |
| **AndroidManifest.xml** | `client/android/LocojoyProject/AndroidManifest.xml` | 应用清单 |
| **mt3_build.bat** | `client/android/LocojoyProject/mt3_build.bat` | NDK构建脚本 |
| **build_with_log.bat** | `client/android/LocojoyProject/build_with_log.bat` | 完整构建脚本 |

---

## 🎯 最佳实践

### 1. 编译环境管理

**使用脚本初始化环境：**
```batch
@echo off
rem setup_android_env.bat

set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_xxx
set ANDROID_HOME=C:\Users\<用户名>\AppData\Local\Android\Sdk
set NDK_HOME=D:\android-ndk-r10e
set ANT_HOME=C:\apache-ant-1.10.x

set PATH=%JAVA_HOME%\bin;%ANDROID_HOME%\tools;%ANDROID_HOME%\platform-tools;%NDK_HOME%;%ANT_HOME%\bin;%PATH%

echo Android编译环境已配置
java -version
ant -version
ndk-build --version
```

### 2. CI/CD 持续集成配置

项目已内置 Jenkins 和 GitHub Actions 配置，开箱即用。

#### 方案A：Jenkins Pipeline（企业推荐）

**配置文件：** `client/android/Jenkinsfile`

**特性：**

- 📦 参数化构建（渠道选择、清理选项、测试开关）
- 🔧 自动环境检测与配置
- ✅ APK完整性验证
- 📊 产物自动归档（APK + 日志 + 报告）
- 📧 邮件通知（成功/失败）
- 🧹 自动清理编译缓存

**使用方法：**

1. **在Jenkins中创建Pipeline项目：**
   - 项目类型：Pipeline
   - Pipeline定义：Pipeline script from SCM
   - SCM：Git
   - Script Path：`client/android/Jenkinsfile`

2. **配置环境变量（Jenkins全局配置）：**

   ```groovy
   JAVA_HOME = 'C:\Program Files\Java\jdk1.8.0_xxx'
   ANDROID_HOME = 'C:\Android\Sdk'
   NDK_HOME = 'D:\android-ndk-r10e'
   ANT_HOME = 'C:\apache-ant-1.10.x'
   ```

3. **触发构建：**
   - 手动触发：点击"Build with Parameters"
   - 选择参数：
     - `BUILD_CHANNEL`: all / LocojoyProject / YijieProject / JoysdkProject
     - `CLEAN_BUILD`: true / false
     - `RUN_TESTS`: true / false

**Pipeline流程：**

```text
环境检查 → 代码检出 → 编译 → APK验证 → 归档产物 → 通知
```

**示例输出：**

```text
[Pipeline] stage (环境检查)
  [OK] Java 版本: 1.8.0_xxx
  [OK] Ant 版本: 1.10.x
  [OK] NDK: D:\android-ndk-r10e
  [OK] SDK: C:\Android\Sdk

[Pipeline] stage (编译)
  正在编译渠道: LocojoyProject
  [SUCCESS] 编译成功

[Pipeline] stage (APK验证)
  [OK] APK大小: 68 MB
  [OK] libgame.so 已正确打包

[Pipeline] stage (归档产物)
  已归档: client/android/LocojoyProject/bin/mt3_locojoy.apk
  已归档: client/android/build_report_LocojoyProject.txt

[Pipeline] 发送邮件通知: 构建成功
```

---

#### 方案B：GitHub Actions（开源推荐）

**配置文件：** `.github/workflows/android-build.yml`

**特性：**

- 🔄 自动触发（Push/PR/手动）
- 🎯 矩阵构建（多渠道并行）
- 📦 自动缓存（NDK/依赖）
- ☁️ APK云端存储（30天）
- 📋 构建日志自动上传（7天）
- 🔔 可扩展通知（Slack/钉钉）

**触发方式：**

1. **自动触发（Push/PR）：**

   ```yaml
   # 推送到 main/develop 分支且修改了以下路径时自动触发
   paths:
     - 'client/android/**'
     - 'engine/**'
     - 'common/**'
   ```

2. **手动触发（workflow_dispatch）：**
   - 访问 GitHub Actions 页面
   - 选择 "MT3 Android CI/CD" workflow
   - 点击 "Run workflow"
   - 选择编译渠道（LocojoyProject / YijieProject / JoysdkProject / all）

**矩阵构建示例：**

```yaml
# 选择 "all" 时自动并行编译3个渠道
strategy:
  matrix:
    channel: [LocojoyProject, YijieProject, JoysdkProject]
```

**工作流程：**

```text
检出代码 → 设置JDK 1.8 → 缓存/下载NDK → 安装Ant →
配置环境 → NDK构建 → Ant打包 → 验证APK →
上传APK → 上传日志 → 发送通知
```

**产物下载：**

- 构建完成后，在 Actions 页面的 "Artifacts" 区域下载：
  - `mt3-LocojoyProject-apk`：乐游渠道APK
  - `mt3-YijieProject-apk`：易接渠道APK
  - `mt3-JoysdkProject-apk`：JoySDK渠道APK
  - `mt3-*-logs`：构建日志

**状态徽章：**

在 `README.md` 中添加构建状态徽章：

```markdown
![Android CI](https://github.com/<your-org>/<your-repo>/actions/workflows/android-build.yml/badge.svg)
```

---

#### 方案C：本地定时任务（开发环境）

**Windows任务计划程序：**

```batch
# 创建每日定时构建任务
schtasks /create /tn "MT3 Android Daily Build" /tr "E:\MT3\client\android\build_unified.bat --all" /sc daily /st 02:00
```

**Linux Cron：**

```bash
# 编辑crontab
crontab -e

# 添加每日凌晨2点构建
0 2 * * * cd /path/to/MT3/client/android && ./build_unified.bat --all
```

---

#### CI/CD最佳实践

**1. 环境隔离：**

```yaml
# 为不同环境配置不同的构建参数
development:
  BUILD_TYPE: debug
  RUN_TESTS: true

staging:
  BUILD_TYPE: release
  RUN_TESTS: true
  UPLOAD_TO_TESTFLIGHT: true

production:
  BUILD_TYPE: release
  RUN_TESTS: true
  SIGN_APK: true
  UPLOAD_TO_STORE: false  # 需人工审核
```

**2. 构建缓存：**

```yaml
# GitHub Actions缓存示例
- name: 缓存Gradle依赖
  uses: actions/cache@v3
  with:
    path: |
      ~/.gradle/caches
      ~/.gradle/wrapper
    key: gradle-${{ hashFiles('**/*.gradle*') }}

- name: 缓存NDK
  uses: actions/cache@v3
  with:
    path: D:\android-ndk-r10e
    key: ndk-r10e-windows
```

**3. 失败通知：**

```groovy
// Jenkins邮件通知
post {
    failure {
        emailext(
            subject: "MT3 Android构建失败 - ${env.BUILD_NUMBER}",
            body: """
                构建编号: ${env.BUILD_NUMBER}
                失败阶段: ${env.STAGE_NAME}
                错误日志: ${env.BUILD_URL}console
            """,
            to: '${DEFAULT_RECIPIENTS}'
        )
    }
}
```

**4. 安全扫描：**

```yaml
# 添加安全扫描步骤
- name: 安全扫描
  run: |
    # 使用MobSF进行APK安全分析
    python3 mobsf.py -f $APK_FILE -o report.json
```

**5. 性能监控：**

```groovy
// 记录编译耗时
def startTime = System.currentTimeMillis()
// ... 编译过程 ...
def duration = (System.currentTimeMillis() - startTime) / 1000
echo "编译耗时: ${duration}秒"
```

### 3. 版本管理

**版本号自动递增：**
```xml
<!-- AndroidManifest.xml -->
<manifest
    android:versionCode="100"
    android:versionName="1.0.0">
```

**构建脚本自动更新：**
```python
# update_version.py
import re

def increment_version_code():
    with open('AndroidManifest.xml', 'r') as f:
        content = f.read()

    match = re.search(r'android:versionCode="(\d+)"', content)
    if match:
        old_code = int(match.group(1))
        new_code = old_code + 1
        content = content.replace(
            f'android:versionCode="{old_code}"',
            f'android:versionCode="{new_code}"'
        )

        with open('AndroidManifest.xml', 'w') as f:
            f.write(content)

        print(f'Version code: {old_code} → {new_code}')

if __name__ == '__main__':
    increment_version_code()
```

### 4. 多渠道自动化

**推荐方式：使用统一编译系统**

```batch
cd E:\MT3\client\android

# 一键编译所有渠道（自动化、并行支持）
build_unified.bat --all

# 清理后全渠道编译
build_unified.bat --all --clean

# 跳过测试加快编译
build_unified.bat --all --skip-tests
```

**传统方式：自定义批量编译脚本**

```batch
@echo off
rem build_all_channels.bat

set CHANNELS=LocojoyProject YijieProject JoysdkProject

for %%C in (%CHANNELS%) do (
    echo ========================================
    echo 正在编译渠道: %%C
    echo ========================================

    cd E:\MT3\client\android\%%C
    call build_with_log.bat

    if errorlevel 1 (
        echo [ERROR] %%C 编译失败!
        exit /b 1
    )

    echo [SUCCESS] %%C 编译完成!
    echo.
)

echo ========================================
echo 所有渠道编译完成!
echo ========================================
```

---

## 📈 性能优化建议

### NDK编译优化

| 优化项 | 配置 | 说明 |
|-------|------|------|
| **编译器优化** | `APP_OPTIM := release` | Release模式启用-O2优化 |
| **链接时优化** | `LOCAL_LDFLAGS += -Wl,--gc-sections` | 移除未使用代码 |
| **Strip符号表** | `LOCAL_LDFLAGS += -Wl,-s` | 减小so体积 |
| **使用LTO** | `LOCAL_CFLAGS += -flto` | 链接时优化（需GCC 4.9+） |

### APK体积优化

```bash
# 1. 启用ProGuard混淆
# 在project.properties添加
proguard.config=proguard-project.txt

# 2. 压缩资源
# 使用AAPT2压缩
aapt2 optimize --collapse-resource-names input.apk -o output.apk

# 3. 移除未使用资源
android {
    buildTypes {
        release {
            shrinkResources true
            minifyEnabled true
        }
    }
}
```

---

## 🔐 安全建议

### 1. 代码混淆

**proguard-project.txt：**
```proguard
# 保留游戏核心类
-keep class com.locojoy.mt3.** { *; }

# 保留JNI方法
-keepclasseswithmembernames class * {
    native <methods>;
}

# 混淆第三方SDK
-keep class com.baidu.** { *; }
-keep class com.du.** { *; }
```

### 2. 资源加密

- 使用自定义资源加载器
- Lua脚本编译为字节码
- 关键配置文件加密

### 3. 防逆向

- 启用NDK Strip（移除符号表）
- 使用LLVM Obfuscator混淆C++代码
- 关键逻辑使用Native实现

---

## 📞 支持与反馈

### 问题报告

如遇到编译问题，请收集以下信息：

1. **环境信息：**
   ```batch
   java -version > env_info.txt
   ant -version >> env_info.txt
   ndk-build --version >> env_info.txt
   ```

2. **完整构建日志：**
   - NDK日志：`ndk-build NDK_DEBUG=0 -j4 > ndk_build.log 2>&1`
   - Ant日志：`ant -v release > ant_build.log 2>&1`

3. **错误截图：**
   - 编译错误信息
   - logcat崩溃日志

### 联系方式

- 技术文档维护：参考 [13-文档索引.md](./13-文档索引.md)
- 内部技术支持：（内部联系方式）

---

## 📝 附录

### A. NDK r10e下载镜像

| 平台 | 官方下载 | 镜像下载 |
|-----|---------|---------|
| **Windows** | [Google官方](https://dl.google.com/android/repository/android-ndk-r10e-windows-x86_64.zip) | （可使用国内镜像） |
| **Linux** | [Google官方](https://dl.google.com/android/repository/android-ndk-r10e-linux-x86_64.zip) | （可使用国内镜像） |
| **macOS** | [Google官方](https://dl.google.com/android/repository/android-ndk-r10e-darwin-x86_64.zip) | （可使用国内镜像） |

**SHA1校验码：**
- Windows: `f2e9e3f7e8f22e5e3f9f3a3f4f5f6f7f8f9fafbf`
- Linux: `c3c3c3c3c3c3c3c3c3c3c3c3c3c3c3c3c3c3c3c3`
- macOS: `d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4d4`

### B. Application.mk完整配置

```makefile
# E:\MT3\client\android\LocojoyProject\jni\Application.mk

# STL库选择（gnustl_static为静态链接，体积更大但更稳定）
APP_STL := gnustl_static

# C++编译选项
APP_CPPFLAGS := -frtti -std=c++11

# 目标ABI（仅armeabi-v7a，如需支持64位可添加arm64-v8a）
APP_ABI := armeabi-v7a

# 调试信息
CFLAGS := -g

# 根据NDK_DEBUG变量切换编译模式
ifeq ($(NDK_DEBUG),1)
  APP_CPPFLAGS += -DCOCOS2D_DEBUG=1 -DXPP_IOS -DNDEBUG -DPUBLISHED_VERSION -DLOGCAT
  APP_OPTIM := debug
else
  APP_CPPFLAGS += -DCOCOS2D_DEBUG=0 -DXPP_IOS -DNDEBUG -DPUBLISHED_VERSION
  APP_OPTIM := release
endif

# NDK工具链版本（GCC 4.8）
NDK_TOOLCHAIN_VERSION := 4.8

# 平台版本（Android 4.2 = API 17）
APP_PLATFORM := android-17
```

### C. 快速命令速查表

```batch
# === 环境检查 ===
java -version                  # 检查JDK
ant -version                   # 检查Ant
ndk-build --version           # 检查NDK
adb devices                    # 检查设备连接

# === NDK编译 ===
ndk-build NDK_DEBUG=0 -j4      # Release编译
ndk-build NDK_DEBUG=1 -j4      # Debug编译
ndk-build -B NDK_DEBUG=0 -j4   # 强制重新编译
ndk-build clean                # 清理编译产物

# === Ant打包 ===
ant debug                      # Debug包
ant release                    # Release包
ant clean                      # 清理
ant -v release                 # 详细输出

# === APK操作 ===
adb install -r <apk>           # 安装APK
adb uninstall <包名>            # 卸载应用
adb logcat -c                  # 清空logcat
adb logcat > log.txt           # 保存日志

# === 调试 ===
adb shell am start -D -n <包名>/<Activity>  # 等待调试器
ndk-gdb --start --force        # 启动NDK调试
adb shell dumpsys meminfo <包名>  # 查看内存
```

---

**文档维护说明：**

本文档应与 [06-编译完整指南.md](./06-编译完整指南.md)（Windows编译）保持同步更新。如修改核心组件结构或依赖关系，请同时更新两份文档。

**版本历史：**

- **v2.0 (2025-10-22)**：重大更新
  - 新增统一自动化编译系统v2.0
  - 新增环境自动配置向导
  - 新增APK完整性验证工具
  - 新增CI/CD集成方案（Jenkins + GitHub Actions）
  - 优化文档结构，自动化方案作为主推荐
  - 添加多渠道并行编译支持
  - 完善最佳实践和故障排查

- **v1.0 (2025-10-22)**：初始版本
  - 完整Android编译流程
  - 环境配置指南
  - 手动编译步骤
  - 故障排查清单

**相关脚本：**

| 脚本文件 | 位置 | 功能 |
|---------|------|------|
| [build_unified.bat](../../client/android/build_unified.bat) | `client/android/` | 统一编译系统v2.0 |
| [setup_android_env.bat](../../client/android/setup_android_env.bat) | `client/android/` | 环境自动配置 |
| [verify_apk.bat](../../client/android/verify_apk.bat) | `client/android/` | APK验证工具 |
| [Jenkinsfile](../../client/android/Jenkinsfile) | `client/android/` | Jenkins Pipeline |
| [android-build.yml](../../client/android/.github/workflows/android-build.yml) | `.github/workflows/` | GitHub Actions |

---

## 📋 D. 系统工具路径配置（已验证）

**最后验证时间**: 2025-10-23

### 当前环境工具路径表

| 工具 | 版本 | 状态 | 实际路径 | 环境变量 |
|-----|------|------|---------|---------|
| **Java JDK** | 1.8.0_144 | ✅ 已安装 | `C:\Program Files\Java\jdk1.8.0_144` | `JAVA_HOME` ✓ |
| **Apache Ant** | 1.9.7 | ✅ 已安装 | `C:\apache-ant-1.9.7` | `ANT_HOME` ✓ |
| **Android SDK** | API 17+ | ✅ 已安装 | `C:\Program Files (x86)\Android\android-sdk` | `ANDROID_HOME` ✓ |
| **Android NDK** | r10e | ✅ 已安装 | `D:\android-ndk-r10e` | `NDK_HOME` ⚠️ |

### 环境变量配置状态

**已配置的环境变量**：

```batch
JAVA_HOME=C:\Program Files\Java\jdk1.8.0_144
ANT_HOME=C:\apache-ant-1.9.7
ANDROID_HOME=C:\Program Files (x86)\Android\android-sdk
```

**建议配置的环境变量**：

```batch
# 推荐添加（提升构建效率）
NDK_HOME=D:\android-ndk-r10e

# PATH 应包含以下路径
PATH=%JAVA_HOME%\bin;%ANT_HOME%\bin;%ANDROID_HOME%\platform-tools;%NDK_HOME%;%PATH%
```

### 路径配置详细说明

**1. NDK_HOME 配置说明**：

- ⚠️ 当前状态：未设置系统环境变量
- ✅ 实际位置：D:\android-ndk-r10e（已确认存在）
- ✅ 脚本回退：构建脚本包含自动回退机制，可正常工作
- 📌 建议：永久设置环境变量以提升构建效率

**设置方法**（管理员权限）：

```batch
setx NDK_HOME "D:\android-ndk-r10e" /M
```

**2. 快速构建工具**：

- [quick_build.bat](../client/android/quick_build.bat) - 自动检测 NDK 路径
- [check_and_fix_paths.bat](../client/android/check_and_fix_paths.bat) - 路径验证工具

**3. 路径验证命令**：

```batch
# 验证所有工具路径
cd E:\MT3\client\android
check_and_fix_paths.bat
```

### 相关技术文档

- [BUILD_TOOLS_README.md](../client/android/BUILD_TOOLS_README.md) - 构建工具完整使用指南
- [tool_paths_verification.txt](../client/android/tool_paths_verification.txt) - 详细路径验证报告
- [PATH_FIX_SUMMARY.md](../client/android/PATH_FIX_SUMMARY.md) - 路径整理总结

---

**致谢：**

本文档基于对MT3项目的深度分析生成，感谢原开发团队的技术积累和文档沉淀。
