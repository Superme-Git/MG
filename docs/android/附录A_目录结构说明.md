# Android 原始目录结构快照（仅记录关键结构，完整树请按附录生成）

说明
- 本文档记录 “client\android” 与 “client\3rdplatform” 两个目录的原始关键结构与核心文件位置，用于打包文档中的路径校验与一致性对照。
- 项目当前仅支持 32 位 ABI（armeabi-v7a），打包脚本与配置文件请参考：
  - NDK 配置：[Application.mk](client/android/LocojoyProject/jni/Application.mk:1)
  - Ant 打包脚本（免费服）：[build.xml](client/android/LocojoyProject/build.xml:1)
  - Ant 打包脚本（点卡服）：build_monthpayment.xml(如存在)
  - NDK 模块链接/依赖：[Android.mk](client/android/LocojoyProject/jni/Android.mk:1)
  - NDK 构建入口批处理：[mt3_build.bat](client/android/LocojoyProject/mt3_build.bat:1)
  - 资源更新字符串：[strings.xml](client/android/LocojoyProject/res/values/strings.xml:1)
  - 渠道/服务端配置：[ljsdk.cfg](client/android/LocojoyProject/assets/cn.shuzilm.config.json:1)（其它渠道配置见 3rdplatform）

快照时间
- 生成时间（参考）：2025-10-16 17:12:27（以当次 list 结果为准）

---

## 一、client\android 原始关键结构

顶层项目
- common
  - 核心构建文件：
    - [ant.properties](client/android/common/ant.properties:1)
    - [build.xml](client/android/common/build.xml:1)
    - [project.properties](client/android/common/project.properties:1)
    - [mt3_build.bat](client/android/common/mt3_build.bat:1)
    - [mt3_apk.bat](client/android/common/mt3_apk.bat:1)
  - NDK：
    - [jni/Android.mk](client/android/common/jni/Android.mk:1)
    - [jni/Application.mk](client/android/common/jni/Application.mk:1)
    - [jni/main.cpp](client/android/common/jni/main.cpp:1)
  - 资源与 Java 入口：
    - [res/values/strings.xml](client/android/common/res/values/strings.xml:1)
    - [src/com/locojoy/mini/mt3/Mt3Application.java](client/android/common/src/com/locojoy/mini/mt3/Mt3Application.java:1)
    - [src/com/locojoy/mini/mt3/GameApp.java](client/android/common/src/com/locojoy/mini/mt3/GameApp.java:1)

- JoysdkProject
  - 构建：
    - [build.xml](client/android/JoysdkProject/build.xml:1)
    - [project.properties](client/android/JoysdkProject/project.properties:1)
    - [mt3_build.bat](client/android/JoysdkProject/mt3_build.bat:1)
    - [mt3_apk.bat](client/android/JoysdkProject/mt3_apk.bat:1)
  - NDK：
    - [jni/Android.mk](client/android/JoysdkProject/jni/Android.mk:1)
    - [jni/Application.mk](client/android/JoysdkProject/jni/Application.mk:1)
    - [jni/main.cpp](client/android/JoysdkProject/jni/main.cpp:1)
  - 入口与资源：
    - [res/values/strings.xml](client/android/JoysdkProject/res/values/strings.xml:1)
    - [src/com/locojoy/mini/mt3/GameApp.java](client/android/JoysdkProject/src/com/locojoy/mini/mt3/GameApp.java:1)
    - [src/com/locojoy/mini/mt3/Mt3Application.java](client/android/JoysdkProject/src/com/locojoy/mini/mt3/Mt3Application.java:1)

- LocojoyProject
  - 清单与构建：
    - [AndroidManifest.xml](client/android/LocojoyProject/AndroidManifest.xml:1)
    - [build.xml](client/android/LocojoyProject/build.xml:1)
    - build_monthpayment.xml(如存在)
    - [mt3_build.bat](client/android/LocojoyProject/mt3_build.bat:1)
    - [mt3_apk.bat](client/android/LocojoyProject/mt3_apk.bat:1)
    - [ant.properties](client/android/LocojoyProject/ant.properties:1)
    - [project.properties](client/android/LocojoyProject/project.properties:1)
    - main-dex-rule.txt(如使用)
    - [ant.properties](client/android/LocojoyProject/ant.properties:1)
  - NDK：
    - [jni/Android.mk](client/android/LocojoyProject/jni/Android.mk:1)
    - [jni/Application.mk](client/android/LocojoyProject/jni/Application.mk:1)
    - [jni/main.cpp](client/android/LocojoyProject/jni/main.cpp:1)
  - 资源与入口：
    - [res/values/strings.xml](client/android/LocojoyProject/res/values/strings.xml:1)
    - [src/com/locojoy/mini/mt3/Mt3Application.java](client/android/LocojoyProject/src/com/locojoy/mini/mt3/Mt3Application.java:1)
    - [src/com/locojoy/mini/mt3/GameApp.java](client/android/LocojoyProject/src/com/locojoy/mini/mt3/GameApp.java:1)
    - 其它渠道入口：
      - [src/com/locojoy/immt3/wxapi/WXEntryActivity.java](client/android/LocojoyProject/src/com/locojoy/immt3/wxapi/WXEntryActivity.java:1)
      - [src/com/locojoy/wjmt3/wxapi/WXEntryActivity.java](client/android/LocojoyProject/src/com/locojoy/wjmt3/wxapi/WXEntryActivity.java:1)

- YijieProject
  - 清单与构建：
    - [AndroidManifest.xml](client/android/YijieProject/AndroidManifest.xml:1)
    - [build.xml](client/android/YijieProject/build/build.xml:1)
    - [mt3_build.bat](client/android/YijieProject/mt3_build.bat:1)
    - [mt3_apk.bat](client/android/YijieProject/mt3_apk.bat:1)
    - [project.properties](client/android/YijieProject/project.properties:1)
    - [build/main-dex-rule.txt](client/android/YijieProject/build/main-dex-rule.txt:1)
    - [build/mt3.keystore](client/android/YijieProject/build/mt3.keystore:1)
  - NDK：
    - [jni/Android.mk](client/android/YijieProject/jni/Android.mk:1)
    - [jni/main.cpp](client/android/YijieProject/jni/main.cpp:1)
  - 入口与资源：
    - [res/values/strings.xml](client/android/YijieProject/res/values/strings.xml:1)
    - [src/com/locojoy/mini/mt3/Mt3Application.java](client/android/YijieProject/src/com/locojoy/mini/mt3/Mt3Application.java:1)
    - [src/com/locojoy/mini/mt3/GameApp.java](client/android/YijieProject/src/com/locojoy/mini/mt3/GameApp.java:1)

---

## 二、client\3rdplatform 原始关键结构

- BaiduLBS_AndroidSDK_Lib
  - 说明文件： [readme.txt](client/3rdplatform/BaiduLBS_AndroidSDK_Lib/readme.txt:1)
  - 预编译共享库（按 ABI 分类目录，工程仅使用 v7a）：
    - libs/armeabi-v7a/liblocSDK6a.so（打包脚本会从此处复制到工程的 libs/armeabi-v7a）

- duClient_SDK_Lib
  - 预编译共享库（按 ABI 分类目录，工程仅使用 v7a）：
    - libs/armeabi-v7a/libdu.so（打包脚本会从此处复制到工程的 libs/armeabi-v7a）

- MeiqiaSdk
  - 工程与资源：
    - [project.properties](client/3rdplatform/MeiqiaSdk/project.properties:1)
    - [lint.xml](client/3rdplatform/MeiqiaSdk/lint.xml:1)
    - res/（布局、图片、颜色、字符串等）
    - src/com/meiqia/meiqiasdk/（控制器、模型、视图组件等 Java 源码）

- ShareSDK
  - 工程与资源：
    - [AndroidManifest.xml](client/3rdplatform/ShareSDK/AndroidManifest.xml:1)
    - [project.properties](client/3rdplatform/ShareSDK/project.properties:1)
    - [assets/ShareSDK.xml](client/3rdplatform/ShareSDK/assets/ShareSDK.xml:1)
    - res/（分享相关主题、图片资源）
    - src/cn/sharesdk/…（OneKeyShare 与主题实现等）

- YijieSDK
  - [（渠道相关内容，按原始恢复状态保留）](client/3rdplatform/YijieSDK:1)

备注
- 上述为关键结构与核心文件路径的对照快照，满足打包文档对路径校验与一致性的需求。
- 若需完整目录树（包含所有文件），建议在 Windows 终端使用以下命令生成快照并归档：
  - PowerShell（生成 UTF-8 文本）：  
    `Get-ChildItem -Recurse -File -Directory .\client\android | % { $_.FullName } | Out-File -FilePath .\docs\android\client_android_tree.txt -Encoding UTF8`  
    `Get-ChildItem -Recurse -File -Directory .\client\3rdplatform | % { $_.FullName } | Out-File -FilePath .\docs\android\client_3rdplatform_tree.txt -Encoding UTF8`
  - 或 CMD（可能有编码差异）：  
    `tree /F client\android > docs\android\client_android_tree.txt`  
    `tree /F client\3rdplatform > docs\android\client_3rdplatform_tree.txt`