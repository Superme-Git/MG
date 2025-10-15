# Android Ant + NDK APK 打包与安装技术要点与排错手册

本手册总结了基于 Ant + NDK 的旧版 Android 构建链条在 Windows 环境下的完整打包、签名、对齐、安装与常见错误排查流程，并给出可执行的命令清单与最佳实践。文中对项目内关键文件均以可点击形式引用，例如 [build.xml](client/android/LocojoyProject/build/build.xml:1)、[AndroidManifest.update.xml](client/android/LocojoyProject/AndroidManifest.update.xml:1)。

---

## 1. 工具链与环境

- JDK: 建议 JDK 8（JNI/旧 aapt/dx 兼容）。路径例如 C:\Program Files\Java\jdk1.8.0_144
- Android SDK: Build-Tools 22.0.1、Platform android-22（与项目脚本匹配）
- NDK: 对应项目 NDK 版本，配合 [Android.mk](client/FireClient/Android.mk:1)、[Application.mk](client/android/LocojoyProject/jni/Application.mk:1)
- Apache Ant: 1.9.x，例如 C:\apache-ant-1.9.7
- Python: 如需使用 Python 脚本，请将 C:\Python27 加入 PATH（可选：C:\Python27\Scripts）
- 环境变量（可不全局设置，脚本中写死亦可）:
  - JAVA_HOME、ANDROID_HOME/ANDROID_SDK_ROOT、ANDROID_NDK_HOME、ANT_HOME
  - 将 sdk\platform-tools（adb）与 build-tools\22.0.1（aapt、zipalign）加入 PATH 便于直接调用

工具存在性快速检查（PowerShell）:
- aapt、dx、adb 路径：C:\Progra~2\Android\android-sdk\...
- 版本：javac -version、java -version、ant -version、aapt version

---

## 2. 项目结构与关键脚本

- 核心构建脚本: [build.xml](client/android/LocojoyProject/build/build.xml:1)
  - init → copy_libs → gen-R → compile → dex → package → build-unsigned-apk → add-subdex-toapk → sign-apk → release
- APK 输出目录：build\bin\mt3_locojoy.apk（由 [release](client/android/LocojoyProject/build/build.xml:378) 复制而来）
- keystore（签名）：[mt3.keystore](client/android/LocojoyProject/build/mt3.keystore:1)
- 资源及第三方 SDK：3rdplatform/ShareSDK、Meiqia 等在 gen-R/package 阶段叠加
- 多 dex 及主 dex 规则： [main-dex-rule.txt](client/android/LocojoyProject/build/main-dex-rule.txt:1)

---

## 3. 打包流水线技术要点

1) 生成资源与 R.java（aapt）
- 命令由 [gen-R](client/android/LocojoyProject/build/build.xml:178) 触发，合并多个 res 目录（工程 + 第三方）
- 需注意图片 iCCP profile 警告：非致命，可用图像工具移除 iCCP 以净化日志

2) Java 编译（javac）
- [compile](client/android/LocojoyProject/build/build.xml:209) 使用 JDK8，compiler=javac1.8，bootclasspath 指向 android.jar
- 若升级 targetSdkVersion，保持 target=1.7/1.8 取决于 SDK 兼容需求

3) dx 转 dex（多 dex）
- [dex](client/android/LocojoyProject/build/build.xml:232) 使用 dx.bat，--multi-dex，主 dex 列表受 [main-dex-rule.txt](client/android/LocojoyProject/build/main-dex-rule.txt:1) 控制
- 启动崩溃或找不到类 → 调整 main-dex-list 以确保关键入口类进主 dex

4) aapt 打包资源（package）
- [package](client/android/LocojoyProject/build/build.xml:250) 产出中间包（未含 classes.dex），路径为 .\mt3_locojoy
- aapt 22 与旧资源更兼容，Gradle 时代则为 aapt2

5) 组包（ApkBuilderMain）
- [build-unsigned-apk](client/android/LocojoyProject/build/build.xml:277) 调用 sdklib ApkBuilderMain，将 classes.dex 写入临时 unsigned 包
- 多 dex 时额外通过 aapt add 追加 classes2.dex 等（见 add-subdex-toapk）

6) 签名与对齐
- 签名（jarsigner）由 [sign-apk](client/android/LocojoyProject/build/build.xml:358) 完成，产出 .\mt3_locojoy.apk
- 对齐（zipalign）：建议构建后执行 zipalign（如下命令示例）；jarsigner→zipalign 顺序需注意（或使用 apksigner）

---

## 4. Manifest 管理与权限模型

- 目标 SDK：Android 6.0+ 启用运行时权限模型；降级安装会触发 INSTALL_FAILED_PERMISSION_MODEL_DOWNGRADE（-26）
- 本项目原 Manifest 为 [AndroidManifest.xml](client/android/LocojoyProject/AndroidManifest.xml:7)（targetSdkVersion=17）
- 升级 Manifest 的两种方式：
  1) 直接改源 Manifest（需关闭编辑器避免锁）：targetSdkVersion 改为 27/28/30/32
  2) 构建时覆盖：传入 -Dmanifest=路径 或在 [init](client/android/LocojoyProject/build/build.xml:125) 中追加 replaceregexp，将 targetSdkVersion 动态替换
- 我们已使用覆盖清单 [AndroidManifest.update.xml](client/android/LocojoyProject/AndroidManifest.update.xml:1)（targetSdkVersion=27）完成构建

运行时权限策略：
- 提升 targetSdkVersion 后，请在应用入口（例如 [GameApp.java](client/android/common/src/com/locojoy/mini/mt3/GameApp.java:1)）增加运行时权限申请逻辑（存储/定位/录音/电话等）

---

## 5. 签名管理与覆盖安装

- keystore：默认使用 [mt3.keystore](client/android/LocojoyProject/build/mt3.keystore:1)
- 覆盖安装前提：相同包名 + 相同签名 + 允许覆盖（adb install -r）
- 不同签名或权限模型降级：
  - 不同签名 → INSTALL_FAILED_UPDATE_INCOMPATIBLE 或 -505/-504 类错误，需先 adb uninstall 包名
  - 权限模型降级（-26）→ 提升 targetSdkVersion 与设备上已装版本持平或更高；或先卸载旧包
- 版本号：
  - 降级安装需 -d；否则升级安装建议 versionCode 递增（见 [AndroidManifest.update.xml](client/android/LocojoyProject/AndroidManifest.update.xml:4)）

签名/包信息验证命令：
- adb shell dumpsys package com.locojoy.mini.mt3.locojoy | findstr versionCode versionName
- keytool -list -v -keystore mt3.keystore
- apksigner verify --print-certs mt3_locojoy.apk（若使用 apksigner）

---

## 6. ABI 与 SDK 兼容性

- 模拟器 ABI：adb shell getprop ro.product.cpu.abi（x86/x86_64/arm64-v8a）
- 若出现 INSTALL_FAILED_NO_MATCHING_ABIS：
  - 确认 ndk-build 产出 lib 的 ABI 与设备 ABI 匹配（armeabi-v7a/x86/x86_64/arm64-v8a）
  - NDK 的 APP_ABI 配置（[Application.mk](client/android/LocojoyProject/jni/Application.mk:1)）需覆盖目标 ABI
- SDK 等级：
  - minSdkVersion/targetSdkVersion 与设备 SDK（adb shell getprop ro.build.version.sdk）匹配
  - 若过低或过高，需评估兼容性与第三方库依赖

---

## 7. 常见安装失败码与处理

- -7 应用已存在/签名或同名冲突（设备弹窗“应用已存在”）：
  - adb uninstall com.locojoy.mini.mt3.locojoy
  - 若系统分用户：adb shell pm uninstall --user 0 包名
  - 再安装：adb install -r mt3_locojoy.apk 或 zipalign 后安装
- -26 INSTALL_FAILED_PERMISSION_MODEL_DOWNGRADE（权限模型降级）：
  - 提升 targetSdkVersion（如 27），或卸载旧包
- -505/-504 签名冲突或重复包：
  - 确保使用相同 keystore 签名，冲突时卸载旧包
- -19/-103 ABI 不匹配/降级错误：
  - ABI：补齐或调整 .so ABI
  - 降级：加 -d 并注意 versionCode 关系（生产建议升级 versionCode）
- 其他：
  - -12 空间不足：清理设备空间
  - 验证安装后：adb shell pm list packages | findstr 包名

---

## 8. 推荐命令清单（Windows）

构建（使用覆盖 Manifest）：
- cd /d e:\MT3\client\android\LocojoyProject\build
- C:\apache-ant-1.9.7\bin\ant.bat -Dmanifest=e:\MT3\client\android\LocojoyProject\AndroidManifest.update.xml -buildfile build.xml -v release

对齐与安装：
- cd /d e:\MT3\client\android\LocojoyProject\build\bin
- C:\Progra~2\Android\android-sdk\build-tools\22.0.1\zipalign.exe -f -v 4 mt3_locojoy.apk mt3_locojoy_aligned.apk
- C:\Progra~2\Android\android-sdk\platform-tools\adb.exe uninstall com.locojoy.mini.mt3.locojoy
- C:\Progra~2\Android\android-sdk\platform-tools\adb.exe install -r mt3_locojoy_aligned.apk

快速查询与清理：
- C:\Progra~2\Android\android-sdk\platform-tools\adb.exe devices -l
- C:\Progra~2\Android\android-sdk\platform-tools\adb.exe shell getprop ro.product.cpu.abi
- C:\Progra~2\Android\android-sdk\platform-tools\adb.exe shell getprop ro.build.version.sdk
- C:\Progra~2\Android\android-sdk\platform-tools\adb.exe shell pm list packages | findstr com.locojoy.mini.mt3.locojoy

---

## 9. CI 与日志

- 将 aapt、dx、ApkBuilder、jarsigner 各阶段日志落盘（重定向到 apk_build.log），失败即时抓取尾部
- 构建前清理 build/bin、gen，以避免“Unable to delete directory”类错误
- 在 CI 上引入 zipalign 与 apksigner 验证步骤（Gradle 时代使用 assembleRelease + apksigner）

---

## 10. 迁移建议（Gradle）

- 旧构建链（Ant+dx+aapt+jarsigner+zipalign）可用，但建议规划迁移：
  - Gradle + aapt2 + d8/r8，signingConfigs 统一签名
  - flavors/variants 管理多渠道
  - tasks：./gradlew assembleRelease
- 迁移前先将 Manifest/签名/ABI/权限策略规范化，减少迁移成本

---

## 11. 本项目实操要点小结

- 已通过覆盖 Manifest 的方式提升 targetSdkVersion 到 27，构建成功：
  - 覆盖清单： [AndroidManifest.update.xml](client/android/LocojoyProject/AndroidManifest.update.xml:1)
  - 构建脚本： [build.xml](client/android/LocojoyProject/build/build.xml:1)
  - 产物： [mt3_locojoy.apk](client/android/LocojoyProject/build/bin/mt3_locojoy.apk:1)
- 遇安装失败 -7（应用已存在）：执行 adb 卸载后再安装，或在“应用管理”中卸载已有版本
- 若遇 -26：维持 targetSdkVersion ≥ 旧版本，或先卸载旧包
- 对齐安装可提升兼容性（zipalign），签名冲突需统一 keystore

---

## 12. 常用排错速查

- “ERROR: No AndroidManifest.xml file found.”：检查 aapt -M 的 Manifest 路径是否指向覆盖清单或源清单（[package](client/android/LocojoyProject/build/build.xml:252)）
- “Unable to delete directory build\bin”：确认无进程占用，先关闭资源占用，再清理
- “INSTALL_FAILED_NO_MATCHING_ABIS”：确认 .so ABI 与模拟器/设备 ABI 一致（[Application.mk](client/android/LocojoyProject/jni/Application.mk:1)）
- “jar 已签名但无时间戳”：生产签名建议附带 TSA（-tsa 或 -tsacert），便于长期校验

---

如需将上述策略固化回源清单，请关闭编辑器中对 [AndroidManifest.xml](client/android/LocojoyProject/AndroidManifest.xml:7) 的占用后更新 targetSdkVersion，同时在 [build.xml](client/android/LocojoyProject/build/build.xml:125) 的 init 任务添加 replaceregexp 守护，防止回退。
## 附录：JDK 1.8.0_144 兼容性分析与配置建议

本项目当前脚本在 [build.xml](client/android/LocojoyProject/build/build.xml:213) 中配置了：
- compiler="javac1.7"
- target="1.7"
- bootclasspath 指向 android-22 的 [android.jar](client/android/LocojoyProject/build/build.xml:21)

在仅安装并使用 JDK 1.8.0_144（路径：C:\Program Files\Java\jdk1.8.0_144）的场景下，兼容性与建议如下：

1) 可兼容点
- Ant 1.9.x 在 JDK 8 下稳定可用；jarsigner（来自 JDK8）用于 APK 签名完全兼容。
- dx/aapt/zipalign（来自 Android SDK build-tools 22.0.1）与 JDK 版本无直接耦合。
- 只要编译的 Java 源/目标版本为 1.7（或与工程依赖一致），JDK8 的 javac 可以正确编译并生成字节码。

2) 潜在不匹配与修正
- 脚本中的 compiler="javac1.7" 在部分 Ant/JDK 组合下可能被视为“指定旧编译器”，会导致 Ant 查找“javac1.7”实现而非现代编译器。
- 建议将 [build.xml](client/android/LocojoyProject/build/build.xml:213) 的 compiler 属性改为：
  - compiler="modern"（让 Ant 选择当前 JDK 的标准编译器）
  - 或 compiler="javac1.8"（显式指定 JDK8 编译器）
- 维持 target="1.7"，并建议同时设置 source="1.7" 以避免源/目标版本不一致（在 [build.xml](client/android/LocojoyProject/build/build.xml:213) 的 <javac> 上增加 source="1.7"）。
- 如不想改文件，可在命令行通过 Ant 内置属性选择编译器：
  - -Dbuild.compiler=modern 或 -Dbuild.compiler=javac1.8
  - 同时可通过 -Dant.build.javac.source=1.7 -Dant.build.javac.target=1.7 统一源/目标版本（当脚本未显式设置时）。

3) 推荐配置（保持 JDK 8 路径）
- 使用 JDK 8（C:\Program Files\Java\jdk1.8.0_144）
- 将 PATH 中的 JDK8 bin 保持在前（java、javac、jarsigner 可直接调用）
- 保持 Android SDK 路径与版本（aapt/dx/zipalign/adb）为 build-tools 22.0.1、platform android-22
- Ant 命令示例（不改脚本、通过覆盖参数运行）：
  - C:\apache-ant-1.9.7\bin\ant.bat -Dbuild.compiler=modern -Dant.build.javac.source=1.7 -Dant.build.javac.target=1.7 -buildfile e:\MT3\client\android\LocojoyProject\build\build_multidex_common.xml release
  - 或对默认脚本：[build.xml](client/android/LocojoyProject/build/build.xml:1) 同样的覆盖参数

4) 验证清单
- javac 版本：javac -version → 1.8.0_144
- Ant 编译日志：确认使用的是 Modern compiler（无“找不到 javac1.7 编译器”的提示）
- APK 构建成功且安装验证通过（adb install -r），主 dex 规则生效（参考 [main-dex-rule.txt](client/android/LocojoyProject/build/main-dex-rule.txt:1)）

5) 何时需要 JDK 7
- 若严格依赖 Ant 的“javac1.7”编译器名且构建系统不接受 modern/javac1.8（较少见），则安装 JDK 7 并将 JAVA_HOME 指向 JDK 7，可避免编译器标识与 JDK 版本不一致。
- 实务上，JDK 8 + modern 编译器 + source/target=1.7 更通用且便于维护。

结论：在保持 Android SDK/NDK 版本与工程配置不变的情况下，JDK 1.8.0_144 完全可以用于项目的打包流程。建议将编译器标识改为“modern”或“javac1.8”，同时明确 source/target=1.7，以确保与现有依赖的兼容性。