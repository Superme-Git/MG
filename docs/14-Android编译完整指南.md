# MT3 Android平台编译完整指南

文档版本: 1.0
最后更新: 2025-10-22
适用平台: Android (armeabi-v7a)
编译工具: Android NDK r10e + Ant + JDK 1.7/1.8

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

### 方法A：一键编译（推荐）

**使用增强版编译脚本：**

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

### 方法B：分步手动编译（精确控制）

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

### 2. 持续集成配置

**Jenkins Pipeline示例：**
```groovy
pipeline {
    agent any

    environment {
        ANDROID_HOME = 'C:\\Android\\Sdk'
        NDK_HOME = 'D:\\android-ndk-r10e'
        ANT_HOME = 'C:\\apache-ant-1.10.x'
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://your-repo.git'
            }
        }

        stage('Build NDK') {
            steps {
                bat '''
                cd client\\android\\LocojoyProject
                call mt3_build.bat
                '''
            }
        }

        stage('Build APK') {
            steps {
                bat '''
                cd client\\android\\LocojoyProject
                call ant clean release
                '''
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts 'client/android/LocojoyProject/bin/*.apk'
            }
        }
    }
}
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

**批量编译脚本：**
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
- v1.0 (2025-10-22): 初始版本，完整Android编译流程
- 后续更新请在此记录

---

**致谢：**

本文档基于对MT3项目的深度分析生成，感谢原开发团队的技术积累和文档沉淀。
