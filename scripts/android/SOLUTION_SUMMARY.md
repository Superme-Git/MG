# MT3 Android 构建问题解决方案总结

## 📌 问题诊断

### 原始错误

```powershell
PS E:\MT3> cmd.exe /c "E:\MT3\scripts\android\rebuild_third_party_libs.bat"
't' 不是内部或外部命令，也不是可运行的程序
系统找不到指定的路径。
```

### 根本原因

1. **PowerShell 兼容性问题**
   - 批处理脚本中的 `date /t` 和 `time /t` 在 PowerShell 调用 CMD 时解析失败
   - 已修复：使用 `%RANDOM%` 代替

2. **目录结构不匹配（主要问题）**
   - ❌ 脚本期望：`E:\MT3\third_party\` （不存在）
   - ✅ 实际存在：`E:\MT3\dependencies\` （已有预编译库）

## ✅ 解决方案

### 方案 A：使用正确的构建脚本（推荐）

**不要使用** `rebuild_third_party_libs.bat`，而是使用实际的 Android 构建流程：

```powershell
# 1. 进入 Android 项目目录
cd E:\MT3\client\android\LocojoyProject

# 2. 构建 Native 代码
.\mt3_build.bat

# 3. 打包 APK
.\mt3_apk.bat
```

### 方案 B：环境检查

运行环境测试脚本：

```powershell
# CMD
E:\MT3\scripts\android\test_env.bat

# 或 PowerShell（需要 Bypass 执行策略）
powershell -ExecutionPolicy Bypass -File E:\MT3\scripts\android\check_build_env.ps1
```

## 📁 创建的新文件

### 1. 环境检查脚本

| 文件 | 用途 |
|------|------|
| [test_env.bat](test_env.bat) | 快速环境检查（CMD） |
| [check_build_env.ps1](check_build_env.ps1) | 详细环境检查（PowerShell） |

### 2. 修复的脚本

| 文件 | 修改内容 |
|------|---------|
| [rebuild_third_party_libs.bat](rebuild_third_party_libs.bat) | 修复了时间戳生成逻辑 |
| [rebuild_third_party_libs.ps1](rebuild_third_party_libs.ps1) | 新增 PowerShell 包装脚本 |

### 3. 文档

| 文件 | 用途 |
|------|------|
| [README.md](README.md) | 脚本使用说明 |
| [../../docs/android/BUILD_GUIDE_CORRECT.md](../../docs/android/BUILD_GUIDE_CORRECT.md) | 正确的构建指南 |
| [SOLUTION_SUMMARY.md](SOLUTION_SUMMARY.md) | 本文档 |

## 🎯 推荐工作流

### 首次构建

```powershell
# 1. 检查环境
E:\MT3\scripts\android\test_env.bat

# 2. 如果测试通过，进入项目目录
cd E:\MT3\client\android\LocojoyProject

# 3. 清理之前的构建（可选）
rd /s /q obj
rd /s /q libs

# 4. 构建 Native 代码
mt3_build.bat

# 5. 打包 APK
mt3_apk.bat
```

### 日常开发

```powershell
# 修改代码后重新构建
cd E:\MT3\client\android\LocojoyProject
mt3_build.bat
```

## 🔧 环境要求

### 必需工具

| 工具 | 版本 | 检查命令 | 安装链接 |
|------|------|----------|---------|
| Android NDK | r10e | `dir D:\android-ndk-r10e` | [NDK 历史版本](https://developer.android.com/ndk/downloads/older_releases) |
| Apache Ant | 1.9+ | `ant -version` | [下载 Ant](https://ant.apache.org/bindownload.cgi) |
| Java JDK | 7 或 8 | `java -version` | [下载 JDK 8](https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html) |

### 环境变量设置

```powershell
# PowerShell（临时）
$env:NDK_HOME = "D:\android-ndk-r10e"
$env:JAVA_HOME = "C:\Program Files\Java\jdk1.8.0_xxx"
$env:PATH += ";C:\apache-ant\bin"

# CMD（临时）
set "NDK_HOME=D:\android-ndk-r10e"
set "JAVA_HOME=C:\Program Files\Java\jdk1.8.0_xxx"
set "PATH=%PATH%;C:\apache-ant\bin"

# 永久设置（PowerShell，需要管理员）
[System.Environment]::SetEnvironmentVariable("NDK_HOME", "D:\android-ndk-r10e", "User")
[System.Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk1.8.0_xxx", "User")
```

## 🐛 常见问题速查

### Q1: PowerShell 中执行 .bat 报错

**A:** 使用以下方式之一：

```powershell
# 方式 1：通过 CMD 执行
cmd /c "cd /d E:\MT3\client\android\LocojoyProject && mt3_build.bat"

# 方式 2：先进入 CMD
cmd
cd E:\MT3\client\android\LocojoyProject
mt3_build.bat
```

### Q2: NDK not found

**A:** 设置环境变量：

```powershell
$env:NDK_HOME = "D:\android-ndk-r10e"
```

或修改脚本中的默认路径。

### Q3: 第三方库缺失

**A:** 检查预编译库：

```powershell
ls E:\MT3\dependencies\zlib\prebuilt\android\armeabi-v7a\
ls E:\MT3\dependencies\jpeg\prebuilt\android\armeabi-v7a\
ls E:\MT3\dependencies\png\prebuilt\android\armeabi-v7a\
```

如果缺失，需要手动编译或从备份恢复。

### Q4: Ant 构建失败

**A:** 确认 Ant 和 Java 已安装：

```powershell
ant -version
java -version
echo $env:JAVA_HOME
```

## 📊 构建验证

### 检查 Native 库

```powershell
ls E:\MT3\client\android\LocojoyProject\libs\armeabi-v7a\*.so
```

**预期输出：**
- `libcocos2d.so`
- `libgame.so`
- `liblocojoyproject.so`

### 检查 APK

```powershell
ls E:\MT3\client\android\LocojoyProject\build\bin\*.apk
```

**预期输出：**
- `LocojoyProject-release.apk`

## 📚 相关文档

- [正确的构建指南](../../docs/android/BUILD_GUIDE_CORRECT.md)
- [环境配置指南](../../docs/android/03_环境配置指南.md)
- [问题诊断决策树](../../docs/android/05_问题诊断决策树.md)
- [脚本使用说明](README.md)

## 🎉 总结

- ✅ 已修复 PowerShell 兼容性问题
- ✅ 已识别正确的构建流程
- ✅ 已创建环境检查脚本
- ✅ 已编写完整文档
- ⚠️ `rebuild_third_party_libs.bat` 基于错误目录结构，不应使用
- ✅ 实际构建请使用 `client\android\LocojoyProject\mt3_build.bat`

## 🚀 下一步

1. 运行环境检查：`E:\MT3\scripts\android\test_env.bat`
2. 如果检查通过，执行构建：
   ```
   cd E:\MT3\client\android\LocojoyProject
   mt3_build.bat
   mt3_apk.bat
   ```
3. 测试生成的 APK

---

**生成时间：** 2025-10-18
**问题状态：** 已解决 ✅
