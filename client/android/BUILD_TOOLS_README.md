# MT3 Android 构建工具使用指南

## 📋 目录

1. [系统要求](#系统要求)
2. [工具路径验证](#工具路径验证)
3. [构建脚本说明](#构建脚本说明)
4. [快速开始](#快速开始)
5. [常见问题](#常见问题)
6. [故障排除](#故障排除)

---

## 🔧 系统要求

### 必需工具

| 工具 | 版本要求 | 当前状态 | 路径 |
|------|---------|---------|------|
| **Java JDK** | 1.8 (推荐) | ✅ 已安装 | `C:\Program Files\Java\jdk1.8.0_144` |
| **Apache Ant** | 1.9+ | ✅ 已安装 | `C:\apache-ant-1.9.7` |
| **Android SDK** | API 17+ | ✅ 已安装 | `C:\Program Files (x86)\Android\android-sdk` |
| **Android NDK** | r10e (必需) | ✅ 已安装 | `D:\android-ndk-r10e` |

### 环境变量

| 变量 | 状态 | 建议操作 |
|------|------|---------|
| `JAVA_HOME` | ✅ 已设置 | 无需操作 |
| `ANT_HOME` | ✅ 已设置 | 无需操作 |
| `ANDROID_HOME` | ✅ 已设置 | 无需操作 |
| `NDK_HOME` | ⚠️ 未设置 | 建议设置（非必需） |

---

## 🔍 工具路径验证

### 方法 1: 使用自动检查脚本（推荐）

```cmd
cd E:\MT3\client\android
check_and_fix_paths.bat
```

**脚本功能**：
- ✅ 自动检测所有必需工具
- ✅ 验证工具版本
- ✅ 自动生成 `local.properties`
- ✅ 提供修复建议

### 方法 2: 手动验证

```cmd
# 验证 Java
java -version

# 验证 Ant
ant -version

# 验证 Android SDK
dir "C:\Program Files (x86)\Android\android-sdk\platform-tools\adb.exe"

# 验证 Android NDK
dir D:\android-ndk-r10e\ndk-build.cmd
```

---

## 📦 构建脚本说明

### 1. `build_unified.bat` - 完整构建系统

**功能**：
- 完整的环境检测
- 支持多渠道编译
- 编译报告生成
- APK 验证

**使用方法**：

```cmd
# 单渠道编译
build_unified.bat --channel LocojoyProject

# 编译所有渠道
build_unified.bat --all

# 清理后重新编译
build_unified.bat --channel LocojoyProject --clean

# 跳过测试
build_unified.bat --channel LocojoyProject --skip-tests
```

**支持的渠道**：
- `LocojoyProject` (默认)
- `YijieProject`
- `JoysdkProject`
- `LocojoyProject_test2016`

---

### 2. `quick_build.bat` - 快速构建工具（推荐）

**功能**：
- ✅ 自动检测并设置 NDK 路径
- ✅ 简化的输出信息
- ✅ 更好的错误处理
- ✅ 自动显示 APK 信息

**使用方法**：

```cmd
# 默认编译 LocojoyProject
quick_build.bat

# 编译指定渠道
quick_build.bat --channel YijieProject

# 清理后编译
quick_build.bat --channel LocojoyProject --clean
```

**优势**：
- 无需手动设置 `NDK_HOME`
- 自动从 `D:\android-ndk-r10e` 或 `C:\android-ndk-r10e` 加载
- 更清晰的进度提示

---

### 3. `setup_android_env.bat` - 环境配置向导

**功能**：
- 自动探测所有工具位置
- 生成 `local.properties`
- 配置系统环境变量（需管理员权限）

**使用方法**：

```cmd
# 普通模式（临时环境变量）
setup_android_env.bat

# 管理员模式（永久配置）
右键 setup_android_env.bat > 以管理员身份运行
```

---

### 4. `verify_apk.bat` - APK 验证工具

**功能**：
- 检查 APK 大小
- 验证 Native 库
- 签名验证
- 权限列表

**使用方法**：

```cmd
verify_apk.bat LocojoyProject\build\bin\mt3_locojoy.apk
```

---

### 5. `check_and_fix_paths.bat` - 路径检查修复工具（新）

**功能**：
- ✅ 全面检查所有工具路径
- ✅ 自动生成 `local.properties`
- ✅ 提供详细的修复建议
- ✅ 验证工具版本

**使用方法**：

```cmd
check_and_fix_paths.bat
```

---

## 🚀 快速开始

### 方案 A: 使用快速构建工具（推荐新手）

```cmd
# 1. 打开 Windows CMD（非 Git Bash）
cd /d E:\MT3\client\android

# 2. 直接构建
quick_build.bat
```

### 方案 B: 使用完整构建系统

```cmd
# 1. 打开 Windows CMD
cd /d E:\MT3\client\android

# 2. 首次使用：检查环境
check_and_fix_paths.bat

# 3. 构建项目
build_unified.bat --channel LocojoyProject
```

### 方案 C: 设置环境变量后构建

```cmd
# 1. 设置 NDK_HOME（一次性配置）
setx NDK_HOME "D:\android-ndk-r10e" /M

# 2. 重启 CMD，然后构建
cd /d E:\MT3\client\android
build_unified.bat --channel LocojoyProject
```

---

## ❓ 常见问题

### Q1: 为什么 Git Bash 中运行脚本失败？

**A**: 批处理脚本在 Git Bash 中会遇到编码和路径转换问题。

**解决方案**：
- ✅ 使用 Windows CMD
- ✅ 使用 PowerShell（部分支持）
- ❌ 避免使用 Git Bash

---

### Q2: NDK_HOME 未设置怎么办？

**A**: 有三种解决方案：

**方案 1**：使用 `quick_build.bat`（自动检测）
```cmd
quick_build.bat
```

**方案 2**：临时设置
```cmd
set NDK_HOME=D:\android-ndk-r10e
build_unified.bat --channel LocojoyProject
```

**方案 3**：永久设置（需管理员）
```cmd
setx NDK_HOME "D:\android-ndk-r10e" /M
```

---

### Q3: 如何验证环境配置是否正确？

**A**: 运行路径检查工具

```cmd
check_and_fix_paths.bat
```

查看输出，确保所有项都显示 `[OK]`。

---

### Q4: 编译失败如何查看详细日志？

**A**: 日志位置

```cmd
# 查看最新日志
type LocojoyProject\build_logs\android_build_*.log | more

# 或使用记事本打开
notepad LocojoyProject\build_logs\android_build_YYYYMMDD_HHMMSS.log
```

---

### Q5: APK 编译成功但很小（< 50MB）？

**A**: 可能原因：

1. **Native 库未编译**
   - 检查 NDK 配置
   - 查看编译日志中的 ndk-build 部分

2. **资源未打包**
   - 检查 `assets/` 目录
   - 验证 `ant.properties` 配置

3. **增量编译问题**
   - 使用 `--clean` 参数重新编译

---

## 🔧 故障排除

### 问题 1: "Java not found"

**症状**：
```
[ERROR] Java 未安装或未配置到PATH
```

**解决步骤**：
1. 检查 Java 是否安装
   ```cmd
   java -version
   ```

2. 如未安装，下载并安装 JDK 1.8
   - [Eclipse Temurin (OpenJDK)](https://adoptium.net/temurin/releases/?version=8)

3. 设置 JAVA_HOME
   ```cmd
   setx JAVA_HOME "C:\Program Files\Java\jdk1.8.0_xxx" /M
   ```

---

### 问题 2: "Apache Ant not found"

**症状**：
```
[ERROR] Apache Ant 未安装或未配置到PATH
```

**解决步骤**：
1. 下载 Apache Ant
   - [Apache Ant Binary Distribution](https://ant.apache.org/bindownload.cgi)

2. 解压到 `C:\apache-ant-1.10.x`

3. 设置环境变量
   ```cmd
   setx ANT_HOME "C:\apache-ant-1.10.x" /M
   setx PATH "%PATH%;%ANT_HOME%\bin" /M
   ```

---

### 问题 3: "Android SDK not configured"

**症状**：
```
[ERROR] Android SDK 未配置（ANDROID_HOME）
```

**解决步骤**：
1. 安装 Android Studio（包含 SDK）
   - [Android Studio Download](https://developer.android.com/studio)

2. 或单独安装 SDK Command-line Tools
   - [SDK Tools Only](https://developer.android.com/studio#command-tools)

3. 设置 ANDROID_HOME
   ```cmd
   setx ANDROID_HOME "C:\Users\<username>\AppData\Local\Android\Sdk" /M
   ```

---

### 问题 4: "Android NDK r10e not found"

**症状**：
```
[ERROR] Android NDK r10e 未安装
```

**解决步骤**：
1. 下载 NDK r10e
   - [Windows x86_64](https://dl.google.com/android/repository/android-ndk-r10e-windows-x86_64.zip)

2. 解压到 `D:\android-ndk-r10e`

3. 设置 NDK_HOME
   ```cmd
   setx NDK_HOME "D:\android-ndk-r10e" /M
   ```

**重要提示**：
- ⚠️ 必须使用 r10e 版本，更高版本不兼容
- ✅ 推荐解压到 D 盘（避免路径过长）

---

### 问题 5: "Encoding issues / 中文乱码"

**症状**：
```
'�期：2025-10-22' is not recognized...
```

**原因**：
- Git Bash 对批处理脚本的编码支持不佳

**解决方案**：
1. **使用 Windows CMD（推荐）**
   ```cmd
   cmd /c build_unified.bat
   ```

2. **使用 PowerShell**
   ```powershell
   powershell -Command "& {.\build_unified.bat}"
   ```

3. **使用快速构建脚本**
   ```cmd
   quick_build.bat
   ```

---

## 📊 构建产物说明

### 构建成功后的文件位置

```
client/android/LocojoyProject/
├── build/
│   └── bin/
│       └── mt3_locojoy.apk          # 最终 APK (约 391MB)
├── build_logs/
│   └── android_build_*.log          # 构建日志
├── libs/
│   └── armeabi-v7a/
│       ├── libdu.so                 # Native 库
│       └── liblocSDK6a.so
└── obj/                             # 编译中间文件
```

### APK 验证

```cmd
# 使用验证工具
verify_apk.bat LocojoyProject\build\bin\mt3_locojoy.apk

# 或手动检查
dir LocojoyProject\build\bin\*.apk
```

### 安装测试

```cmd
# 通过 ADB 安装
adb install -r LocojoyProject\build\bin\mt3_locojoy.apk

# 或拷贝到设备手动安装
copy LocojoyProject\build\bin\mt3_locojoy.apk E:\
```

---

## 📚 相关文档

- [完整编译指南](../../docs/14-Android编译完整指南.md)
- [工具路径验证报告](tool_paths_verification.txt)
- [APK 验证报告](apk_verification_report.txt)

---

## 🆘 获取帮助

### 查看脚本帮助

```cmd
# build_unified.bat 帮助
build_unified.bat --help

# quick_build.bat 使用说明
quick_build.bat --help
```

### 常用诊断命令

```cmd
# 1. 检查所有工具路径
check_and_fix_paths.bat

# 2. 查看环境变量
set | findstr "JAVA\|ANT\|ANDROID\|NDK"

# 3. 验证工具版本
java -version && ant -version

# 4. 查看编译日志
type LocojoyProject\build_logs\android_build_*.log | more
```

---

**最后更新**: 2025-10-23
**维护者**: Build Engineering Team
**版本**: v1.0
