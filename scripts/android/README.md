# MT3 Android 构建脚本

## 📁 脚本列表

| 脚本名称 | 用途 | 推荐使用场景 |
|---------|------|-------------|
| `check_build_env.ps1` | 环境检查 | 首次构建前，验证所有依赖 |
| `rebuild_third_party_libs.bat` | 重建第三方库（批处理） | CMD 环境 |
| `rebuild_third_party_libs.ps1` | 重建第三方库（PowerShell 包装） | PowerShell 环境 |

## 🚀 快速开始

### 方法 1：PowerShell（推荐）

```powershell
# 1. 检查环境
cd E:\MT3\scripts\android
.\check_build_env.ps1

# 2. 如果检查通过，运行构建
.\rebuild_third_party_libs.ps1
```

### 方法 2：CMD

```cmd
cd E:\MT3
scripts\android\rebuild_third_party_libs.bat
```

### 方法 3：从项目根目录调用

```powershell
# PowerShell
cd E:\MT3
& scripts\android\rebuild_third_party_libs.ps1

# CMD
cd E:\MT3
scripts\android\rebuild_third_party_libs.bat
```

## ⚙️ 环境要求

### 必需工具

- **Android NDK r10e**
  - 默认路径：`D:\android-ndk-r10e`
  - 或设置环境变量：`$env:NDK_HOME`

### 目录结构

脚本期望以下目录存在：

```
E:\MT3\
├── third_party\
│   ├── zlib\jni\Android.mk
│   ├── libpng\jni\Android.mk
│   ├── libjpeg\jni\Android.mk
│   ├── libtiff\jni\Android.mk
│   ├── openssl\jni\Android.mk
│   └── curl\jni\Android.mk
├── dependencies\
└── cocos2d-2.0-rc2-x-2.0.1\external\
```

## 🔧 构建输出

构建成功后，静态库将被安装到：

| 库名称 | 输出路径 |
|-------|---------|
| zlib | `dependencies\zlib\prebuilt\android\armeabi-v7a\libz.a` |
| libpng | `dependencies\png\prebuilt\android\armeabi-v7a\libpng.a` |
| libjpeg | `dependencies\jpeg\prebuilt\android\armeabi-v7a\libjpeg.a` |
| libtiff | `cocos2d-2.0-rc2-x-2.0.1\external\tiff\prebuilt\android\armeabi-v7a\libtiff.a` |
| libcrypto | `cocos2d-2.0-rc2-x-2.0.1\external\curl\prebuilt\android\armeabi-v7a\libcrypto.a` |
| libssl | `cocos2d-2.0-rc2-x-2.0.1\external\curl\prebuilt\android\armeabi-v7a\libssl.a` |
| libcurl | `cocos2d-2.0-rc2-x-2.0.1\external\curl\prebuilt\android\armeabi-v7a\libcurl.a` |

## 📝 日志文件

构建日志保存在：`E:\MT3\build_logs\third_party_rebuild_*.log`

## 🐛 故障排查

### 问题 1：PowerShell 中执行 .bat 报错

**症状：**
```
't' 不是内部或外部命令，也不是可运行的程序
```

**解决方案：**
- 使用 `rebuild_third_party_libs.ps1` 而不是直接调用 `.bat` 文件
- 或者先进入 CMD 环境：`cmd`，然后运行 `.bat`

### 问题 2：NDK_HOME 未设置

**症状：**
```
[ERROR] NDK_HOME 未设置且默认路径 D:\android-ndk-r10e 不存在
```

**解决方案：**
```powershell
# 临时设置（当前会话）
$env:NDK_HOME = "你的NDK路径"

# 或使用参数
.\rebuild_third_party_libs.ps1 -NdkHome "你的NDK路径"
```

### 问题 3：某个库构建失败

**症状：**
日志中显示 `[ERROR] 构建 XXX 失败`

**解决方案：**
1. 检查日志文件查看详细错误
2. 确认该库的 `jni\Android.mk` 文件存在
3. 验证 NDK 版本为 r10e
4. 检查该库的源代码是否完整

## 🔄 重新构建

如需重新构建某个库，可以：

1. 清理该库的 `obj` 目录：
   ```powershell
   Remove-Item -Recurse -Force E:\MT3\third_party\库名\obj
   ```

2. 重新运行构建脚本

## 📚 相关文档

- [Android 编译完整指南](../../docs/android/)
- [环境配置指南](../../docs/android/03_环境配置指南.md)
- [问题诊断决策树](../../docs/android/05_问题诊断决策树.md)
