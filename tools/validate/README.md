# Android 环境验证脚本使用说明

本目录提供统一的 Android 工具链环境验证脚本，用于在本地或 CI 环境中快速检查 JDK/SDK/NDK/Ant 等关键组件是否就绪，并输出可读的诊断结果与非零退出码以便流水线判断。

脚本文件：
- 批处理版：[android_env_check.bat](./android_env_check.bat:1)
- PowerShell 版：[android_env_check.ps1](./android_env_check.ps1:1)

公共参考章节：
- 环境配置与验证统一章节：[docs/common/Android_Env.md](../../docs/common/Android_Env.md:1)
- ABI/平台一致性排查统一章节：[docs/common/Android_ABI_Troubleshooting.md](../../docs/common/Android_ABI_Troubleshooting.md:1)

---

## 1) 功能概述

脚本将逐项检测以下组件的存在性与基本可用性（版本/帮助输出）：
- aapt（version）
- dx（--version）
- zipalign（-h）
- android.jar（platform + API 级别）
- adb（version）
- ndk-build（--help）
- Ant（-version）
- Java/javac/jarsigner（-version/-help）

检测完成后输出总结：
- [SUMMARY] 验证完成：所有检查通过（返回码 0）
- 或列出缺失/异常项数量（返回码为缺失/异常计数）

---

## 2) 参数与默认值

两个脚本均支持传入路径参数；未传参时使用默认值。

默认基线：
- SDK=C:\Progra~2\Android\android-sdk
- NDK=D:\android-ndk-r10e
- ANT=C:\apache-ant-1.9.7
- JDK=C:\Program Files\Java\jdk1.8.0_144
- BuildTools=22.0.1
- Platform=android-22

说明：
- Windows 上建议使用短路径 C:\Progra~2 避免 PowerShell/转义问题。
- 不同安装位置请替换为对应路径或在 CI 配置中注入变量。

---

## 3) 使用示例（本地）

批处理版（cmd）：
- [android_env_check.bat](./android_env_check.bat:1) SDK="C:\Progra~2\Android\android-sdk" NDK="D:\android-ndk-r10e" ANT="C:\apache-ant-1.9.7" JDK="C:\Program Files\Java\jdk1.8.0_144" BUILD_TOOLS=22.0.1 PLATFORM=android-22

PowerShell 版：
- powershell -ExecutionPolicy Bypass -File [android_env_check.ps1](./android_env_check.ps1:1) -SDK "C:\Progra~2\Android\android-sdk" -NDK "D:\android-ndk-r10e" -ANT "C:\apache-ant-1.9.7" -JDK "C:\Program Files\Java\jdk1.8.0_144" -BuildTools "22.0.1" -Platform "android-22"

输出示例（通过）：
- [OK] aapt: "C:\Progra~2\Android\android-sdk\build-tools\22.0.1\aapt.exe"
- ...
- [SUMMARY] 验证完成：所有检查通过（返回码 0）

输出示例（存在缺失/异常）：
- [X] adb 缺失："C:\Progra~2\Android\android-sdk\platform-tools\adb.exe"
- ...
- [SUMMARY] 验证完成：存在 2 项缺失/异常（返回码 2）

---

## 4) CI 集成建议（GitHub Actions/自定义 CI）

建议在构建前加入“工具链验证”步骤，失败则中断流程。

伪代码（Windows runner）：
- checkout
- setup Java/Ant（如需）
- 调用 [android_env_check.ps1](./android_env_check.ps1:1) 指定路径与版本
- 若返回码非 0 → fail pipeline

优势：
- 将路径与版本问题前置诊断，减少后续构建失败成本
- 输出关键路径，便于定位缺失组件与版本不匹配

---

## 5) 常见问题与解决

- aapt/dx 路径包含“(x86)”导致解析异常：
  - 使用短路径 C:\Progra~2（已在默认参数中体现）
- android.jar 不匹配：
  - 将 Platform 切换为项目所需 API（例如 android-22），并确保 SDK 对应 platform 安装完整
- ndk-build 未找到或版本不符：
  - 核对 NDK 路径与版本（r10e 基线），或在脚本参数中覆盖路径
- Ant/JDK 未安装：
  - 安装 JDK 8 与 Ant 1.9.x，或在 CI 中通过工具链准备步骤注入

---

## 6) 与项目文档的关联

以下文档已统一引用本 README 与脚本，以减少重复维护：
- Android 快速验证与一键编译指南：[docs/android/Android_工具链环境快速验证与一键编译指南.md](../../docs/android/Android_工具链环境快速验证与一键编译指南.md:1)
- Android 打包与安装技术要点与排错手册：[docs/android/Android_Ant_NDK_APK_打包与安装技术要点与排错手册.md](../../docs/android/Android_Ant_NDK_APK_打包与安装技术要点与排错手册.md:1)
- 环境统一章节：[docs/common/Android_Env.md](../../docs/common/Android_Env.md:1)
- ABI 统一排查：[docs/common/Android_ABI_Troubleshooting.md](../../docs/common/Android_ABI_Troubleshooting.md:1)
- 入口索引（INDEX）：[docs/INDEX.md](../../docs/INDEX.md:1)

---

## 7) 维护与版本约定

- 更新脚本时请同步调整 README 的参数示例与默认值
- 变更 JDK/SDK/NDK/Ant 基线版本请在公共章节与 README 一并更新
- 保持返回码语义稳定（0 通过，非 0 表示缺失/异常项数量）

变更记录：
- 2025-10-16：首次发布统一验证脚本与使用说明；与公共章节联动。