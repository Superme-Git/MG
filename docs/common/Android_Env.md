# Android 环境统一规范与快速验证

本文抽取并统一 Android 工具链环境配置与验证内容，供所有 Android 文档引用，避免重复与路径硬编码。  
适用范围：Ant + dx + aapt + ApkBuilder + zipalign + jarsigner + NDK r10e 旧链路。

关联文档：
- [Android_工具链环境快速验证与一键编译指南.md](../android/Android_工具链环境快速验证与一键编译指南.md:1)
- [Android_Ant_NDK_APK_打包与安装技术要点与排错手册.md](../android/Android_Ant_NDK_APK_打包与安装技术要点与排错手册.md:1)

---

## 1) 标准化环境变量与参数

推荐使用临时会话参数，避免修改系统 PATH；脚本/命令应支持如下变量：
- SDK=C:\Progra~2\Android\android-sdk
- NDK=D:\android-ndk-r10e
- ANT=C:\apache-ant-1.9.7
- JDK=C:\Program Files\Java\jdk1.8.0_144
- BUILD_TOOLS=22.0.1
- PLATFORM=android-22

示例（cmd 单行临时会话）：
cmd /c "setlocal & set SDK=C:\Progra~2\Android\android-sdk & set NDK=D:\android-ndk-r10e & set ANT=C:\apache-ant-1.9.7 & set JDK=C:\Program Files\Java\jdk1.8.0_144 & set BUILD_TOOLS=22.0.1 & set PATH=%PATH%;%SDK%\build-tools\%BUILD_TOOLS%;%SDK%\platform-tools & aapt.exe version & endlocal"

注意：Windows 下建议使用短路径 C:\Progra~2 以避免 PowerShell/转义问题。

---

## 2) 统一验证脚本

- 批处理版：[android_env_check.bat](../../tools/validate/android_env_check.bat:1)
- PowerShell 版：[android_env_check.ps1](../../tools/validate/android_env_check.ps1:1)

用法（cmd）：
tools\validate\android_env_check.bat SDK="C:\Progra~2\Android\android-sdk" NDK="D:\android-ndk-r10e" ANT="C:\apache-ant-1.9.7" JDK="C:\Program Files\Java\jdk1.8.0_144" BUILD_TOOLS=22.0.1 PLATFORM=android-22

用法（PowerShell）：
powershell -ExecutionPolicy Bypass -File tools\validate\android_env_check.ps1 -SDK "C:\Progra~2\Android\android-sdk" -NDK "D:\android-ndk-r10e" -ANT "C:\apache-ant-1.9.7" -JDK "C:\Program Files\Java\jdk1.8.0_144" -BuildTools "22.0.1" -Platform "android-22"

脚本检查项：
- aapt/version
- dx/help
- zipalign/help
- android.jar 存在性
- adb/version
- ndk-build/help
- Ant/javac/java 版本
- 输出诊断总结与非零退出码

---

## 3) 手动快速验证清单（无脚本）

"C:\Progra~2\Android\android-sdk\build-tools\22.0.1\aapt.exe" version
call "C:\Progra~2\Android\android-sdk\build-tools\22.0.1\dx.bat" --version
"C:\Progra~2\Android\android-sdk\build-tools\22.0.1\zipalign.exe" -h
"C:\Progra~2\Android\android-sdk\platform-tools\adb.exe" version
dir /b "C:\Progra~2\Android\android-sdk\platforms\android-22\android.jar"
call "D:\android-ndk-r10e\ndk-build.cmd" --help

适配性说明：若 SDK/NDK 安装在其他盘符，请将上述路径替换为自定义位置或改用“统一验证脚本”。

---

## 4) 构建参数基线（Ant + JDK8）

- -Dbuild.compiler=modern
- -Dant.build.javac.source=1.7
- -Dant.build.javac.target=1.7
- 覆盖脚本路径参数（如脚本仍含硬编码）：
  - -Dsdk-folder="C:\Program Files (x86)\Android\android-sdk"
  - -Dplatform-folder="C:\Program Files (x86)\Android\android-sdk\platforms\android-22"
  - -Dplatform-tools-folder="C:\Program Files (x86)\Android\android-sdk\build-tools\22.0.1"
  - -Djdk-folder="C:\Program Files\Java\jdk1.8.0_144"

参考实现与示例见：[build.xml](../../client/android/LocojoyProject/build/build.xml:1)、[build_multidex_common.xml](../../client/android/LocojoyProject/build/build_multidex_common.xml:1)。

---

## 5) 安装与对齐（zipalign + adb）

"C:\Progra~2\Android\android-sdk\build-tools\22.0.1\zipalign.exe" -f -v 4 e:\MT3\client\android\LocojoyProject\build\bin\mt3_locojoy.apk e:\MT3\client\android\LocojoyProject\build\bin\mt3_locojoy_aligned.apk
"C:\Progra~2\Android\android-sdk\platform-tools\adb.exe" uninstall com.locojoy.mini.mt3.locojoy
"C:\Progra~2\Android\android-sdk\platform-tools\adb.exe" install -r e:\MT3\client\android\LocojoyProject\build\bin\mt3_locojoy_aligned.apk

---

## 6) 日志快速核查

"C:\Progra~2\Android\android-sdk\platform-tools\adb.exe" shell am start -n com.locojoy.mini.mt3.locojoy/com.locojoy.mini.mt3.GameApp
"C:\Progra~2\Android\android-sdk\platform-tools\adb.exe" logcat -v time -d | findstr /r /c:"FATAL EXCEPTION" /c:"UnsatisfiedLinkError" /c:"SIGSEGV" /c:"dlopen" /c:"VerifyError"

详细 ABI/平台版本一致性与 APK 自检请参见：[Android_ABI_Troubleshooting.md](./Android_ABI_Troubleshooting.md:1)

---

## 7) 维护与演进

- 本文为公共章节，被其他 Android 文档引用；更新时请同时验证脚本输出与示例命令。
- 若迁移至 Gradle，请在公共章节中保留旧链路但首推 Gradle+aapt2+d8/r8 的新流程，迁移指南见各自文档。

变更记录：
- 2025-10-16：首次抽取统一；加入标准变量、脚本用法与验证清单。