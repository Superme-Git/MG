# Android 工具链环境快速验证与一键编译指南

本指南用于“再次验证系统安装的工具链与环境”，并提供“在编译时立即准确调用”的一键命令。适用于本项目的 Ant + dx + aapt + ApkBuilder + zipalign + jarsigner + NDK 原始打包链路。

- 相关脚本（可点击）：
  - [Locojoy 构建脚本](client/android/LocojoyProject/build/build.xml:1)
  - [Locojoy 多 dex 构建脚本](client/android/LocojoyProject/build/build_multidex_common.xml:1)
  - [Locojoy dex 构建脚本](client/android/LocojoyProject/build/build_dex.xml:1)
  - [Joysdk 构建脚本](client/android/JoysdkProject/build/build.xml:1)
  - [Yijie 构建脚本](client/android/YijieProject/build/build.xml:1)
  - [common 构建脚本](client/android/common/build/build.xml:1)

---

## 1) 工具链与路径目标

- JDK 1.8.0_144：
  - 路径：C:\Program Files\Java\jdk1.8.0_144
  - 已在所有构建脚本统一为该路径：例如 [build.xml](client/android/LocojoyProject/build/build.xml:19)、[build_multidex_common.xml](client/android/LocojoyProject/build/build_multidex_common.xml:16)、[build_dex.xml](client/android/LocojoyProject/build/build_dex.xml:16)、[build_monthpayment.xml](client/android/LocojoyProject/build/build_monthpayment.xml:19)、[Joysdk](client/android/JoysdkProject/build/build.xml:18)、[Yijie](client/android/YijieProject/build/build.xml:18)、[common](client/android/common/build/build.xml:18)
  - 编译器配置已统一为 modern + source/target=1.7（示例： [javac](client/android/LocojoyProject/build/build.xml:212)）

- Android SDK（r24.x 旧组件）：
  - Build-Tools 22.0.1（aapt.exe、dx.bat、zipalign.exe）
  - Platform android-22（android.jar）
  - Platform-tools（adb.exe）
  - SDK 根目录（示例）：C:\Program Files (x86)\Android\android-sdk

- Android NDK：
  - 版本：r10e
  - 路径：D:\android-ndk-r10e
  - 构建入口：ndk-build.cmd（示例： [mt3_build.bat](client/android/LocojoyProject/mt3_build.bat:1)）

- Apache Ant：
  - 版本：1.9.x（示例：C:\apache-ant-1.9.7）

- Python（如项目需要）：
  - Python 2.7 路径：C:\Python27（可选加入 PATH）
  - 已记录于手册（参考： [技术手册](docs/Android_Ant_NDK_APK_打包与安装技术要点与排错手册.md:9)）

---

## 2) 绝对路径快速验证（无需依赖 PATH，可直接在 VSCode 终端执行）

为避免 PowerShell 对“(x86)”路径解析问题，建议使用短路径 C:\Progra~2。以下命令用于“再次验证工具链存在且可执行”。

- aapt 版本：
  - "C:\Progra~2\Android\android-sdk\build-tools\22.0.1\aapt.exe" version
- dx 版本与帮助：
  - call "C:\Progra~2\Android\android-sdk\build-tools\22.0.1\dx.bat" --version
  - call "C:\Progra~2\Android\android-sdk\build-tools\22.0.1\dx.bat" --help
- zipalign 帮助：
  - "C:\Progra~2\Android\android-sdk\build-tools\22.0.1\zipalign.exe" -h
- android.jar 存在性：
  - dir /b "C:\Progra~2\Android\android-sdk\platforms\android-22\android.jar"
- adb 版本：
  - "C:\Progra~2\Android\android-sdk\platform-tools\adb.exe" version
- ndk-build 帮助：
  - dir /b "D:\android-ndk-r10e\ndk-build.cmd"
  - call "D:\android-ndk-r10e\ndk-build.cmd" --help

执行预期：
- aapt 显示版本（Android Asset Packaging Tool）
- dx 显示 version 1.10 与帮助
- zipalign 显示帮助文本
- android.jar 显示文件名
- adb 显示版本（1.0.32）
- ndk-build 显示 GNU make usage（表示 r10e 工具链正常）

---

## 3) 一键编译（临时会话）—确保“立即准确调用”

如果 VSCode 终端未加载系统 PATH，可在单次会话中临时设置 ANDROID_HOME/NDK_HOME 并追加 PATH，然后调用 Ant 构建。以下命令使用绝对路径与短路径，确保立即准确调用。

- Locojoy 多 dex（推荐）：
  - cmd /c "setlocal & set ANDROID_HOME=C:\Progra~2\Android\android-sdk & set NDK_HOME=D:\android-ndk-r10e & set PATH=%PATH%;%ANDROID_HOME%\build-tools\22.0.1;%ANDROID_HOME%\platform-tools & cd /d e:\MT3\client\android\LocojoyProject\build & C:\apache-ant-1.9.7\bin\ant.bat -Dbuild.compiler=modern -Dant.build.javac.source=1.7 -Dant.build.javac.target=1.7 -buildfile build_multidex_common.xml -v release & endlocal"
  - 构建脚本： [build_multidex_common.xml](client/android/LocojoyProject/build/build_multidex_common.xml:1)

- Locojoy 默认脚本：
  - cmd /c "setlocal & set ANDROID_HOME=C:\Progra~2\Android\android-sdk & set PATH=%PATH%;%ANDROID_HOME%\build-tools\22.0.1;%ANDROID_HOME%\platform-tools & cd /d e:\MT3\client\android\LocojoyProject\build & C:\apache-ant-1.9.7\bin\ant.bat -Dbuild.compiler=modern -Dant.build.javac.source=1.7 -Dant.build.javac.target=1.7 -buildfile build.xml -v release & endlocal"
  - 构建脚本： [build.xml](client/android/LocojoyProject/build/build.xml:1)

- Joysdk：
  - cmd /c "setlocal & set ANDROID_HOME=C:\Progra~2\Android\android-sdk & set PATH=%PATH%;%ANDROID_HOME%\build-tools\22.0.1;%ANDROID_HOME%\platform-tools & cd /d e:\MT3\client\android\JoysdkProject\build & C:\apache-ant-1.9.7\bin\ant.bat -Dbuild.compiler=modern -Dant.build.javac.source=1.7 -Dant.build.javac.target=1.7 -buildfile build_multidex_common.xml -v release & endlocal"
  - 构建脚本： [build_multidex_common.xml](client/android/JoysdkProject/build/build_multidex_common.xml:1)

- Yijie：
  - cmd /c "setlocal & set ANDROID_HOME=C:\Progra~2\Android\android-sdk & set PATH=%PATH%;%ANDROID_HOME%\build-tools\22.0.1;%ANDROID_HOME%\platform-tools & cd /d e:\MT3\client\android\YijieProject\build & C:\apache-ant-1.9.7\bin\ant.bat -Dbuild.compiler=modern -Dant.build.javac.source=1.7 -Dant.build.javac.target=1.7 -buildfile build_multidex_common.xml -v release & endlocal"
  - 构建脚本： [build_multidex_common.xml](client/android/YijieProject/build/build_multidex_common.xml:1)

- common：
  - cmd /c "setlocal & set ANDROID_HOME=C:\Progra~2\Android\android-sdk & set PATH=%PATH%;%ANDROID_HOME%\build-tools\22.0.1;%ANDROID_HOME%\platform-tools & cd /d e:\MT3\client\android\common\build & C:\apache-ant-1.9.7\bin\ant.bat -Dbuild.compiler=modern -Dant.build.javac.source=1.7 -Dant.build.javac.target=1.7 -buildfile build.xml -v release & endlocal"
  - 构建脚本： [build.xml](client/android/common/build/build.xml:1)

说明：
- -Dbuild.compiler=modern：让 Ant 使用当前 JDK 的标准编译器；配合 source/target=1.7 与脚本中的 bootclasspath 指向 [android.jar](client/android/LocojoyProject/build/build.xml:21)。
- 若需覆盖 SDK 路径（脚本仍有硬编码），可额外传入：
  - -Dsdk-folder="C:\Program Files (x86)\Android\android-sdk"
  - -Dplatform-folder="C:\Program Files (x86)\Android\android-sdk\platforms\android-22"
  - -Dplatform-tools-folder="C:\Program Files (x86)\Android\android-sdk\build-tools\22.0.1"
  - -Djdk-folder="C:\Program Files\Java\jdk1.8.0_144"

---

## 4) 安装与验证（zipalign + adb）

签名产物完成后，推荐对齐并安装验证：

- 对齐（zipalign）：
  - "C:\Progra~2\Android\android-sdk\build-tools\22.0.1\zipalign.exe" -f -v 4 e:\MT3\client\android\LocojoyProject\build\bin\mt3_locojoy.apk e:\MT3\client\android\LocojoyProject\build\bin\mt3_locojoy_aligned.apk
- 卸载旧包（如需）：
  - "C:\Progra~2\Android\android-sdk\platform-tools\adb.exe" uninstall com.locojoy.mini.mt3.locojoy
- 安装：
  - "C:\Progra~2\Android\android-sdk\platform-tools\adb.exe" install -r e:\MT3\client\android\LocojoyProject\build\bin\mt3_locojoy_aligned.apk

---

## 5) 常见问题与立即修复

- VSCode 终端未加载 PATH：
  - 现象：where aapt/adb 未命中；解决：使用本指南“一键编译（临时会话）”命令或重启 VSCode/终端使 PATH 生效。
- 仍使用旧的 JDK7 编译器标识：
  - 现象：Ant 报“找不到 javac1.7 编译器”；解决：已统一改为 modern（见 [javac](client/android/LocojoyProject/build/build.xml:212) 等），也可用 -Dbuild.compiler 覆盖。
- dx 多 dex 缺类：
  - 现象：运行时 ClassNotFound；解决：补充 [main-dex-rule.txt](client/android/LocojoyProject/build/main-dex-rule.txt:1) 关键入口类；优先使用多 dex 构建脚本。
- aapt 路径差异：
  - 现象：找不到 aapt；解决：使用短路径 C:\Progra~2 或传入 -Dplatform-tools-folder 覆盖脚本路径。

---

## 6) 验证清单（再次确认）

依次执行：
- "C:\Progra~2\Android\android-sdk\build-tools\22.0.1\aapt.exe" version
- call "C:\Progra~2\Android\android-sdk\build-tools\22.0.1\dx.bat" --version
- "C:\Progra~2\Android\android-sdk\build-tools\22.0.1\zipalign.exe" -h
- "C:\Progra~2\Android\android-sdk\platform-tools\adb.exe" version
- call "D:\android-ndk-r10e\ndk-build.cmd" --help

输出均正确即为“工具链与环境验证完成”，可直接编译。