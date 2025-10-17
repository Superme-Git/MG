# Android ABI 与平台版本一致性排查统一指南

本文抽取并统一“ABI 集一致”“平台版本对齐”的公共排查方法，供所有 Android 文档引用，避免重复说明与遗漏。  
适用范围：NDK r10e + Ant 打包链路，APP_ABI=armeabi-v7a 基线。

关联文档：
- [Android_工具链环境快速验证与一键编译指南.md](../Android_工具链环境快速验证与一键编译指南.md:1)
- [Android_Ant_NDK_APK_打包与安装技术要点与排错手册.md](../Android_Ant_NDK_APK_打包与安装技术要点与排错手册.md:1)
- 环境公共章节：[Android_Env.md](./Android_Env.md:1)

---

## 1) 原则与症状速览

原则：
- 构建 ABI 集 == 打包进 APK 的 ABI 集（严格一致）
- NDK 平台版本 APP_PLATFORM == Manifest minSdkVersion（对齐一致）

常见症状：
- 安装失败：INSTALL_FAILED_NO_MATCHING_ABIS（-19/-103）
- 运行崩溃：UnsatisfiedLinkError / dlopen 失败（缺少主库或不匹配）
- 64 位设备秒退：系统优先启用 64 位进程，但 APK 中缺少匹配的 64 位主库

---

## 2) 构建侧配置（APP_ABI 与 APP_PLATFORM）

- 构建 ABI 集（单一 v7a 基线）：
  - [APP_ABI](../../client/android/LocojoyProject/jni/Application.mk:3) := armeabi-v7a

- 平台版本对齐：
  - [APP_PLATFORM](../../client/android/LocojoyProject/jni/Application.mk:4) := android-11
  - [minSdkVersion](../../client/android/LocojoyProject/AndroidManifest.xml:7) = 11
  - 若不一致将出现 NDK 警告与潜在运行时不匹配

说明：如需支持其他 ABI（x86/arm64 等），必须同时在构建与打包两侧完整覆盖并测试；本项目基线为仅 v7a。

---

## 3) 打包侧修订（移除不一致的库复制）

为避免“打包阶段引入与构建不一致的 ABI 库”，需在打包脚本中移除 x86_64 等非 v7a 的复制逻辑：

- 修订位置：
  - [build.xml](../../client/android/LocojoyProject/build/build.xml:226)
  - [build_monthpayment.xml](../../client/android/LocojoyProject/build/build_monthpayment.xml:202)

目标效果：
- APK 内仅保留 lib/armeabi-v7a/*.so
- 避免 64 位设备选择 64 位进程导致缺少 v7a 主库（libgame.so）而秒退

---

## 4) APK ABI 自检（构建后立即执行）

检查 APK 中的原生库目录：

"C:\Progra~2\Android\android-sdk\build-tools\22.0.1\aapt.exe" list e:\MT3\client\android\LocojoyProject\build\bin\mt3_locojoy.apk | findstr /i "^lib/"

期望输出：
- 仅出现 lib/armeabi-v7a/*.so

若发现 lib/x86、lib/x86_64、lib/arm64-v8a 等条目：
- 说明打包阶段仍复制了不一致 ABI 的库；请按照“打包侧修订”章节清理复制逻辑。

---

## 5) 设备/模拟器 ABI 与 SDK 等级查询

查询设备 ABI：
- "C:\Progra~2\Android\android-sdk\platform-tools\adb.exe" shell getprop ro.product.cpu.abi

查询设备 SDK 等级：
- "C:\Progra~2\Android\android-sdk\platform-tools\adb.exe" shell getprop ro.build.version.sdk

匹配策略：
- 构建产物的 ABI 集必须覆盖设备 ABI
- APP_PLATFORM 与 minSdkVersion 应不高于设备 SDK 等级（过高会影响兼容性），不低于工程约束（过低会引发 NDK 警告）

---

## 6) 安装失败码与处理策略

- INSTALL_FAILED_NO_MATCHING_ABIS（-19/-103）：
  - 确认 APK 内仅包含 lib/armeabi-v7a/*.so（或与设备 ABI 严格匹配）
  - 检查 [APP_ABI](../../client/android/LocojoyProject/jni/Application.mk:3) 与打包复制逻辑是否一致

- 权限模型降级（-26）：
  - 提升 targetSdkVersion（建议 ≥ 设备上已安装版本），参考覆盖清单或源清单：
    - [AndroidManifest.update.xml](../../client/android/LocojoyProject/AndroidManifest.update.xml:1)
    - [AndroidManifest.xml](../../client/android/LocojoyProject/AndroidManifest.xml:7)

- 签名冲突（-505/-504）：
  - 相同包名需相同签名；不一致时先 adb uninstall 包名再安装

---

## 7) 运行时快速核查（崩溃定位）

启动与日志关键字筛查：
- "C:\Progra~2\Android\android-sdk\platform-tools\adb.exe" shell am start -n com.locojoy.mini.mt3.locojoy/com.locojoy.mini.mt3.GameApp
- "C:\Progra~2\Android\android-sdk\platform-tools\adb.exe" logcat -v time -d | findstr /r /c:"UnsatisfiedLinkError" /c:"dlopen" /c:"FATAL EXCEPTION" /c:"SIGSEGV" /c:"VerifyError"

入口加载序列：
- [System.loadLibrary](../../client/android/LocojoyProject/src/com/locojoy/mini/mt3/GameApp.java:1538) 按顺序加载依赖库与主库（libgame.so）

若报缺符号（LuaJIT 兼容）：
- 在共享库源中加入 BSD stdio 兼容实现：
  - [bsd_compat.c](../../client/android/LocojoyProject/jni/bsd_compat.c:1)
  - [Android.mk](../../client/android/LocojoyProject/jni/Android.mk:33) 将 bsd_compat.c 纳入 LOCAL_SRC_FILES
  - 链接项修正：
    - 系统库： [Android.mk](../../client/android/LocojoyProject/jni/Android.mk:61) 增加 -llog -lz -landroid
    - 静态支持库： [Android.mk](../../client/android/LocojoyProject/jni/Android.mk:79) import-module android/support 并链接 LOCAL_STATIC_LIBRARIES += android_support

---

## 8) 一致性验证（统一命令/脚本）

建议在“构建完成后、安装前”执行以下组合验证：
1) APK ABI 自检（见第 4 章）
2) 设备 ABI 与 SDK 查询（见第 5 章）
3) 安装与启动 + 日志核查（见第 7 章）

也可结合公共环境脚本：
- [Android_Env.md](./Android_Env.md:1) 中的统一验证脚本：
  - [android_env_check.bat](../../tools/validate/android_env_check.bat:1)
  - [android_env_check.ps1](../../tools/validate/android_env_check.ps1:1)

---

## 9) CI/本地构建检查清单

- 构建前：
  - 校验 [Application.mk](../../client/android/LocojoyProject/jni/Application.mk:3) 的 APP_ABI 与 APP_PLATFORM
  - 校验 Manifest 的 minSdkVersion 与目标版本覆盖策略（源 or 覆盖清单）

- 打包后：
  - APK ABI 自检（aapt list | findstr "^lib/"）
  - 若含非 v7a 目录，回滚不一致的复制逻辑

- 安装前：
  - 查询设备 ABI/SDK；仅在匹配条件下安装
  - 如需覆盖安装，签名与包名一致；不一致先卸载

- 启动后：
  - 关键日志筛查；如缺符号，按“LuaJIT 兼容修复”处理

---

## 10) 迁移注意事项（Gradle）

如迁移至 Gradle + aapt2 + d8/r8：
- 在 build.gradle 的 ndk/abiFilters 与 packagingOptions 中保持 ABI 集一致
- signingConfigs 统一签名与安装策略
- 仍应保留“设备 ABI 与 SDK”查询与“APK ABI 自检”（aapt2/apksigner 辅助）

---

## 11) 结论

遵循“构建 ABI == 打包 ABI”“APP_PLATFORM == minSdkVersion”的两项强制一致性规则，并在构建后执行 APK 自检与设备匹配核对，可显著降低安装失败与运行时崩溃的风险。  
与 [Android_Env.md](./Android_Env.md:1) 的统一脚本结合使用，可将验证固化为标准流程与 CI 阶段。

变更记录：
- 2025-10-16：首次抽取统一；加入构建/打包一致性要点、APK 自检与设备匹配核查、LuaJIT 兼容修复路径。