# MT3 依赖组件全量清单 / Full Dependency Inventory

- **最后更新时间**：2025-10-19
- **适用范围**：MT3 客户端（Windows/iOS/Android）、服务器端、工具链及公共第三方库
- **维护人**：平台技术组（联络：tech@mt3.example）

---

## 1. 盘点说明

- **覆盖范围**：`E:\MT3` 仓库内 `dependencies/`、`client/`、`server/`、`tools/`、`client/3rdplatform/` 等目录的全部第三方源代码、二进制产物与渠道 SDK。
- **版本识别**：优先以随附版本号、官方包名或文件时间戳判定；若仓库中无显式版本信息，则标记为“见仓库源”并在备注里说明。
- **32/64 位支持解读**：标记仓库内是否已有现成二进制。若需自行编译或缺失对应架构，会在备注中给出动作建议。
- **下载链接**：优先列出官方站点或长期可访问的仓库。渠道/私有包无公开地址时标记为“内部/需授权”。
- **校验建议**：同步下载时建议记录 SHA256 校验值并存档于 `docs/common/`，本文档仅提供来源指引。

---

## 2. 核心引擎与跨平台基础库

| 组件 | 版本/来源 | 仓库路径 | 32 位支持 | 64 位支持 | 下载/备注 |
| --- | --- | --- | --- | --- | --- |
| Cocos2d-x | 2.0-rc2-x-2.0.1 官方包 | `cocos2d-2.0-rc2-x-2.0.1` | Win32 工程齐备 | 需自行生成 | <https://github.com/cocos2d/cocos2d-x/releases/tag/cocos2d-2.0-rc2> |
| CEGUI（旧版） | 0.7.x 自改 | `dependencies/cegui` | Win32 静态库 & 项目 | 无 | 需按官方源码重编出 x64 |
| CEGUI（升级稿） | 0.8.7 官方源码 | `cegui-0.8.7` | 源码自编 | 源码自编 | <https://github.com/cegui/cegui> |
| FreeImage | 3.x 预编译 | `dependencies/FreeImage` | Win32 DLL/LIB，iOS/OSX/Android 静态库 | 无 Win64 | <https://sourceforge.net/projects/freeimage/files/> |
| FreeType | 2.x | `dependencies/FreeType`, `dependencies/freetype-2.4.9` | Win32 & 移动静态库 | 无 Win64 | <https://download.savannah.gnu.org/releases/freetype/> |
| libjpeg / libjpeg-turbo | 官方源 | `dependencies/jpeg` | Win32、armv5/v7/x86 等 | ARM64 待补齐 | <https://libjpeg-turbo.org/> |
| libpng | 1.6.x | `dependencies/png` | Win32 静态库 | 需编译 | <http://www.libpng.org/pub/png/libpng.html> |
| zlib | 1.2.x | `dependencies/zlib` | Win32 静态库 | 需编译 | <https://zlib.net/> |
| libogg | 1.3.2 | `dependencies/libogg-1.3.2` | Win32 静态库 | 无 | <https://downloads.xiph.org/releases/ogg/> |
| libvorbis | 1.3.5 | `dependencies/libvorbis-1.3.5` | Win32 静态库 | 无 | <https://downloads.xiph.org/releases/vorbis/> |
| Speex | 1.2rc2 | `dependencies/speex-1.2rc2` | Win32/Android 源 | 无 Win64 | <https://downloads.xiph.org/releases/speex/> |
| PCRE | 8.31 | `dependencies/pcre-8.31` | Win32 静态库 | 需编译 | <https://www.pcre.org/> |
| RapidXML | 1.13 | `dependencies/RapidXml` | Header-only | 同左 | <http://rapidxml.sourceforge.net/> |
| SILLY | 0.1.0 | `dependencies/SILLY-0.1.0` | Win32 库 | 需编译 | <https://sourceforge.net/projects/libsilly/> |
| LuaSocket | 2.0.2 | `dependencies/luasocket-2.0.2` | Win32/Android 源 | 需编译 | <http://w3.impa.br/~diego/software/luasocket/> |
| Lua Debug | 内部源码 | `dependencies/luadebug` | 源码（x86） | 同左 | 私有 |
| Google Breakpad | 主干版 | `dependencies/google-breakpad` | Win32 源码 | 需适配 | <https://chromium.googlesource.com/breakpad/breakpad> |
| PLCRashReporter | 1.2 arm64 beta | `dependencies/plcrashreporter-1.2-arm64-beta1` | iOS 静态库 | arm64 需更新 | <https://github.com/microsoft/plcrashreporter> |
| KSCrash | Framework | `dependencies/KS` | FAT (32/64) | ✔ | <https://github.com/kstenerud/KSCrash> |
| Flurry iOS | 5.2.0 | `dependencies/Flurry/libFlurry_5.2.0.a` | FAT | ✔ | <https://developer.yahoo.com/flurry/> |
| Visual Leak Detector | 2.5 | `dependencies/vld` | Win32 DLL/LIB | 无 | <https://kinddragon.github.io/vld/> |
| wxWidgets | 2.8.11 | `dependencies/wxWidgets-2.8.11` | Win32 项目 | 无 Win64 | <https://www.wxwidgets.org/downloads/> |
| ASIHTTPRequest | 1.8.x | `dependencies/asihttp` | iOS/Mac 源码 | 同左 | <https://github.com/pokeb/asi-http-request> |
| LJFilePack / LJXML | 自研 | `dependencies/LJFilePack`, `dependencies/LJXML` | Win32 源码 | 需扩展 | 内部 |
| OpenGLES_v2 Stub | 自研 | `dependencies/opengles_v2` | Win32 静态库 | 无 | 内部 |

---

## 3. Windows 客户端专项依赖

| 组件 | 路径 | 用途 | 32 位支持 | 64 位支持 | 备注 |
| --- | --- | --- | --- | --- | --- |
| dbghelp.dll / vc120 工具集 | `client/MT3Win32App/Debug.win32` | 崩溃转储 | 系统 DLL | 系统提供 | 标准 VS2013 运行库 |
| Visual Leak Detector | `client/MT3Win32App/Debug.win32/vld_*` | 泄漏检测 | ✔ | ✖ | 已集成配置 |
| CEGUI lib/engine.lib | `client/FireClient/Debug.win32`, `Release.win32` | UI/引擎 | ✔ | ✖ | 需同步源代码 |
| translator_common.lib | `client/FireClient/Release.win32` | 多语言支持 | ✔ | ✖ | 内部库 |
| MT3.exe 依赖 | `client/MT3Win32App` | 最终可执行 | ✔ | ✖ | 仅 x86 交付 |

> Windows 客户端若计划迁移至 x64，需要统一重编 CEGUI、FreeImage、FreeType、Ogg/Vorbis、Speex、libpng、zlib 等底层库。

---

## 4. iOS 客户端渠道与 SDK

| SDK/库 | 版本/来源 | 仓库路径 | 架构 | 下载/备注 |
| --- | --- | --- | --- | --- |
| Chartboost SDK | 官方 iOS | `client/FireClient/FireClient/Chartboost_sdk/Chartboost.framework` | FAT (armv7/armv7s/arm64) | <https://answers.chartboost.com/en-us/child_article/ios> |
| ShareSDK | 3.3.0 | `client/FireClient/FireClient/ShareSDK/ShareSDK.framework` | FAT | <https://www.mob.com/wiki/detailed/ShareSDK> |
| ShareSDK 平台包 | 2.7.0/1.1.0 | `client/3rdplatform/ShareSDK` | Java/资源 | 需与 MOB 官网同步 |
| MeiQia SDK | 最新包 | `client/FireClient/FireClient/Meiqia-SDK-files/MeiQiaSDK.framework` | FAT | <https://docs.meiqia.com> |
| TalkingData App CPA | 未标注（静态库） | `client/FireClient/FireClient/TalkingData/libTalkingDataAppCpa.a` | FAT | <https://www.talkingdata.com/app/document_center/> |
| Flurry Analytics | 5.2.0 | `dependencies/Flurry/libFlurry_5.2.0.a` | FAT | 建议升级新版 |
| PLCrashReporter | 1.2 beta | `dependencies/plcrashreporter-1.2-arm64-beta1` | armv7/arm64 | 建议替换为稳定版 |
| KSCrash | 官方 | `dependencies/KS/KSCrash.framework` | FAT | 推荐作为首选崩溃框架 |
| OpenSSL libcrypto | 未标注 | `client/FireClient/FireClient/SDK_Locojoy/libcrypto.a` | FAT | 注意苹果 3rd party 审核要求 |
| ASIHTTPRequest | 1.8.x | `dependencies/asihttp` | 源码 | 需结合 ARC/非 ARC 设置 |

---

## 5. Android 客户端依赖

### 5.1 JAR 依赖

| 库 | 版本 | 常见路径 | 说明 | 下载/备注 |
| --- | --- | --- | --- | --- |
| android-async-http | 1.4.x | `client/android/*/libs/android-async-http.jar` | HTTP 请求框架 | <https://github.com/android-async-http/android-async-http> |
| android-support-multidex | 1.x | `client/android/*/libs/android-support-multidex.jar` | 多 Dex 支持 | <https://developer.android.com/studio/build/multidex> |
| android-support-v4 | v4 | `client/android/JoysdkProject/libs/android-support-v4.jar` | 旧版支持库 | 建议迁移 AndroidX |
| okhttp | 2.7.0 | `client/android/*/libs/okhttp-2.7.0.jar` | HTTP 客户端 | <https://square.github.io/okhttp/2.x/> |
| okio | 1.6.0 | `client/android/*/libs/okio-1.6.0.jar` | IO 抽象 | <https://square.github.io/okio/> |
| volley | Google 版 | `client/android/*/libs/volley.jar` | 网络框架 | <https://github.com/google/volley> |
| httpcore/httpmime | 4.3.x | `client/android/*/libs/httpcore-4.3.2.jar` 等 | Apache HTTP 组件 | <https://hc.apache.org/httpcomponents-client-4.3.x/> |
| commons-io/common-net | 2.4/3.4 | 同上 | Apache 常用组件 | <https://commons.apache.org/> |
| BaiduLBS_Android | 官方包 | `client/android/*/libs/BaiduLBS_Android.jar` | 地图定位 | <https://lbsyun.baidu.com/> |
| FlurryAnalytics | 4.1.0 | `client/android/common/libs/FlurryAnalytics-4.1.0.jar` | 统计 | 建议升级 |
| TalkingData AdTracking | 未标注 | `client/android/*/libs/TalkingData_AdTracking_SDK_Android.jar` | 广告归因 | <https://www.talkingdata.com/app/document_center/> |
| ShareSDK 各子包 | 2.7.0 | `client/3rdplatform/ShareSDK/libs` | 渠道分享 | <https://www.mob.com/wiki/detailed/ShareSDK> |
| Meiqia SDK | `meiqiasdk.jar` | `client/3rdplatform/MeiqiaSdk/bin` | 在线客服 | <https://docs.meiqia.com> |
| ahelper/gangaonline 等 | 渠道包 | `client/android/common/libs` | 登陆/支付 SDK | 内部渠道 |
| du.jar | 渠道包 | `client/android/*/libs/du.jar` | 登陆 SDK | 内部渠道 |

### 5.2 SO 依赖

| SO 库 | 来源 | 路径 | 架构覆盖 | 备注 |
| --- | --- | --- | --- | --- |
| libdu.so | 渠道 SDK | `client/android/LocojoyProject/libs`, `client/3rdplatform/duClient_SDK_Lib/libs` | armeabi/armeabi-v7a/arm64/mips/mips64/x86/x86_64 | 渠道私有 |
| liblocSDK6a.so | 百度 LBS | `client/android/*/libs`, `client/3rdplatform/BaiduLBS_AndroidSDK_Lib/libs` | armeabi/armeabi-v7a/arm64/x86/x86_64 | 需关注最新版 |
| libgangaOnlineUnityHelper.so | 易接渠道 | `client/3rdplatform/YijieSDK/lib` | armeabi/armeabi-v7a/x86 | 建议评估 arm64 |
| libgame.so / libengine.a / libFireClient.a | NDK 构建 | `client/android/LocojoyProject/obj/local/armeabi-v7a` | armeabi-v7a | 核心引擎需补 arm64 |

> Google Play 已强制 64 位，自研 so 需补齐 `arm64-v8a`。同时需检查所有第三方 so 是否具备 64 位版本。

---

## 6. 服务器端（Java）依赖

| 组件 | 版本 | 位置示例 | 备注 / 下载 |
| --- | --- | --- | --- |
| Apache Ant | 1.9.x | `server/server/game_server/buildscript/lib/ant.jar` | <https://ant.apache.org/bindownload.cgi> |
| Commons CLI | 1.2 | `server/server/game_server/buildscript/lib` | <https://commons.apache.org/proper/commons-cli/> |
| Commons IO | 1.4 | 同上 | <https://commons.apache.org/proper/commons-io/> |
| SVNKit | 1.8.x | 同上 | <https://svnkit.com/download.php> |
| Commons BeanUtils | 1.8.0 | `server/server/game_server/gs/gs_lib` | 老版本，注意 CVE |
| Commons Collections | 3.1 | 同上 | 建议升级或引入安全补丁 |
| Commons Lang | 2.6 | 同上 | 推荐升级至 3.x |
| Commons Logging | 1.1.1/1.2 | `gs_lib`, `xbrowse/lib` | — |
| EzMorph | 1.0.6 | `gs_lib` | <http://ezmorph.sourceforge.net/> |
| JSON-lib | 2.3-jdk15 | `gs_lib` | <http://json-lib.sourceforge.net/> |
| Apache POI | 3.7-beta1 | `gs_lib` | 版本老旧，注意漏洞 |
| FreeMarker | 2.3.x | `gs_lib` | <https://freemarker.apache.org/> |
| H2 Database | 1.3.176 | `gs_lib` | 建议评估升级至 2.x |
| c3p0 | 0.9.5.2 | `gs/lib` | <https://www.mchange.com/projects/c3p0/> |
| Log4j | 1.2.15/1.2.16 | 多处 | 存在已知 CVE，需替换或打补丁 |
| XStream | 1.4.3 | 多处 | 建议升级并启用安全模式 |
| XDB/XMerge/MonkeyKing/MyLogic | 自研 JAR | `server/tools/**` | 内部构建 |
| ProGuard | 4.x | `server/tools/xbrowse/lib` | <https://sourceforge.net/projects/proguard/> |
| javadocking | 1.5.x | `xbrowse/lib/javadocking.jar` | <http://www.javadocking.com/> |
| JMX Client | 自研 | `xbrowse` | 内部 |

---

## 7. 工具链与辅助项目

| 工具 | 位置 | 功能 | 备注 |
| --- | --- | --- | --- |
| CEGUI-0.7.1 编辑器 | `tools/CEGUI-0.7.1` | UI 编辑/预览 | Win32 工程，依赖老版 CEGUI |
| CELayoutEditor / AssetsConvert | `tools/CELayoutEditor`, `tools/AssetsConvert` | UI/资源处理 | 自研 |
| ExcelParse / ExcelParse2 | `tools/ExcelParse*` | 策划 Excel 导表 | 使用 C#/.NET |
| MissionEditor / NpcList | `tools/MissionEditor`, `tools/NpcList` | 任务/角色配置 | 自研 |
| rpcgen | `tools/rpcgen` | 协议代码生成 | 依赖 `rpcgen.jar` |
| transform_mkdb | `tools/transform_mkdb` | DB 转换 | 依赖 `xbean.jar` 等 |
| TextureCompressTool | `tools/TextureCompressTool` | 纹理压缩 | Windows 平台 |
| validate | `tools/validate` | 资源检查 | 脚本/二进制混合 |

---

## 8. 风险与行动建议

- **Windows x64 迁移缺口**：所有第三方预编译库均为 x86，若规划 x64 发行需要统一编译链路并更新 `vcxproj`。
- **Android 64 位合规**：`libgame.so` 等核心库仅提供 `armeabi-v7a`，需升级 NDK 构建并补齐 `arm64-v8a`，同步验证依赖库。
- **iOS 崩溃框架**：PLCrashReporter 使用 beta 包，建议升级至官方稳定版或完全切换至 KSCrash。
- **服务端安全风险**：Log4j 1.2.x、Commons Collections 3.1 等存在公开漏洞，需尽快替换或加防护（例如使用 `reload4j`、移除 `Invokers`）。
- **渠道 SDK 更新**：ShareSDK、Baidu LBS、TalkingData、MeiQia 等需定期从官方获取最新包以满足隐私合规与系统兼容。
- **文档与校验**：建议在每次依赖升级后更新本文档，并于 `docs/common/` 新增哈希清单，保证可追溯性。

---

## 9. 维护流程与检查清单

- [ ] 依赖升级时同步更新版本号、下载链接及 32/64 位支持状态。
- [ ] 新增渠道或第三方 SDK 时在表格中补录路径与授权信息。
- [ ] 对内生库（如 `libgame.so`、`monkeyking.jar`）的编译流程需在 `docs/workflows/` 留存操作手册。
- [ ] 对存在安全风险的旧版库（Log4j、Commons 等）制定替换计划并跟踪执行状态。
- [ ] 每季度运行一次仓库扫描（脚本可放置于 `tools/scripts`）核对依赖清单与实际文件差异。
- [ ] 将关键压缩包/原始下载物保存在制品库（如 Nexus/Artifactory），避免依赖外部镜像失效。

> 需要补充或纠正条目时，请直接更新本文档并在提交记录中说明依赖源及验证方式。

