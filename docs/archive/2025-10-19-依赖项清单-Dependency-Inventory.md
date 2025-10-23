# MT3 依赖项清单 / Dependency Inventory

- 最后更新时间：2025-10-19
- 维护联系人：平台技术组（tech@mt3.example）
- 适用范围：MT3 仓库内所有第三方库、工具链、渠道 SDK 及其与自研模块的映射关系；与《自研组件全景》配套使用。

---

## 1. 文档目标

1. 列出全仓库第三方依赖及版本、许可证、来源与构建产出，支撑合规、升级与构建复现。
2. 建立“自研模块 ⇄ 第三方依赖”的映射，明确最小兼容版本与测试状态。
3. 形成平台/架构的版本矩阵，指导渠道升级与跨平台适配。

---

## 2. 使用说明与统一约定

- 版本号规范：遵循 semver（MAJOR.MINOR.PATCH），若上游不遵循则在“备注”标注差异。
- 平台范围：Windows、iOS、Android、Server（JVM/Native）。
- 路径规范：均为仓库相对路径，示例：engine、common/ljfm、client/android/common。
- 引用方式：对于二进制依赖，记录引入形式（静态/动态/源码编译）；对于脚本类依赖，记录运行时最低版本。
- 变更流程：新增/升级/替换依赖需在本清单登记，并附“影响评估与回滚方案”。

---

## 3. 统一目录

- 自研组件总览：见《自研组件全景》文档。
- 第三方库编译与使用：见 docs/09-第三方库编译指南.md。
- 平台专项构建诊断：见 docs/windows/MT3_Release_Build_Diagnostics.md 与 docs/android/BUILD_GUIDE_CORRECT.md。

---

## 4. 第三方依赖清单（按字母序）

| 名称 | 版本 | 许可证 | 语言/平台 | 用途 | 来源/获取方式 | 集成路径 | 构建产出/包 | 备注 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| CEGUI | 0.7.1 | MIT | C++/Win32 | UI 框架 | 源码（tools/CEGUI-0.7.1） | tools/CEGUI-0.7.1 | CEGUIBase.lib/CEGUIBase.dll | 需要预编译依赖FreeType |
| Cocos2d | 2.0.4 | MIT | C++/跨平台 | 渲染内核 | cocos2d-2.0-rc2-x-2.0.1 | cocos2d-2.0-rc2-x-2.0.1 | libcocos2d.lib/libcocos2d.a | 核心渲染引擎，包含LuaJIT 2.0.3 |
| FreeType | 2.4.12 | FTL/GPL | C/C++ | 字体渲染 | 官方源码 | dependencies/freetype | freetype.lib/libfreetype.a | 字体渲染库，CEGUI依赖 |
| Lua | 5.1.5/5.2.4 | MIT | C | 脚本运行时 | 官方源码 | common/lua | lua.dll/lua.lib/lua.so | tolua++ 1.0.93用于C++绑定 |
| NVTT/压缩库 | 2.1.1 | BSD | C++/Win | 纹理压缩 | 官方源码 | tools/TextureCompressTool | nvtt.lib/nvcompress.exe | 纹理压缩工具，支持DDS格式 |
| OpenSSL | 1.0.2u | OpenSSL | C/C++ | 加密/SSL | 官方源码 | common/updateengine | libeay32.lib/ssleay32.lib | 用于更新引擎的网络通信 |
| libcurl | 7.40.0 | MIT | C/C++ | HTTP/下载 | 官方源码 | common/updateengine | libcurl.lib/libcurl.so | 用于资源下载和更新 |
| zlib | 1.2.8 | zlib | C/C++ | 压缩 | 官方源码 | common/updateengine | zlib.lib/libz.so | 用于资源包压缩 |
| tolua++ | 1.0.93 | MIT | C++ | Lua绑定 | 官方源码 | common/tolua++-1.0.93 | tolua++.lib/tolua++.so | C++与Lua桥接工具 |
| ShareSDK | 2.7.8 | 商业 | Android/iOS | 社交分享 | 官方SDK | client/3rdplatform/ShareSDK | ShareSDK.jar/ShareSDK.aar | 支持微信/QQ/微博等平台 |
| Meiqia | 3.5.2 | 商业 | Android/iOS | 客服系统 | 官方SDK | client/3rdplatform/MeiqiaSdk | MeiqiaSDK.jar/MeiqiaSDK.aar | 在线客服SDK |
| BaiduLBS | 4.3.1 | 商业 | Android | 定位服务 | 官方SDK | client/android/libs | BaiduLBS_android.jar | 百度定位SDK |
| Alipay | 15.7.9 | 商业 | Android/iOS | 支付宝支付 | 官方SDK | client/android/libs | alipaySdk.jar | 支付宝支付SDK |
| WeChatPay | 5.4.1 | 商业 | Android/iOS | 微信支付 | 官方SDK | client/android/libs | libammsdk.jar | 微信支付SDK |

注：请根据实际依赖补齐并按字母序维护；每次升级需更新此表。

---

## 5. 自研模块 ⇄ 依赖映射

| 自研模块 | 路径 | 关键第三方依赖 | 最低兼容版本 | 集成方式 | 负责人 | 最近验证 |
| --- | --- | --- | --- | --- | --- | --- |
| 引擎 engine | engine | CEGUI 0.7.1、Cocos2d 2.0.4、FreeType 2.4.12 | 见第4节 | 源码/静态库 | 平台组 | 2025-10-22 |
| 公共库 ljfm | common/ljfm | zlib 1.2.8 | 1.2.8 | 静态库 | 平台组 | 2025-10-22 |
| 更新引擎 updateengine | common/updateengine | libcurl 7.40.0/OpenSSL 1.0.2u | 见第4节 | 静态/动态 | 平台组 | 2025-10-22 |
| 客户端 Windows | client/FireClient/FireClient | CEGUI、引擎库 | 见第4节 | 链接库 | 客户端组 | 2025-10-22 |
| 客户端 Android | client/android/common | NDK r10e、渠道 SDK | 见第4节 | aar/so | 客户端组 | 2025-10-22 |
| 客户端 iOS | client/FireClient/FireClient | Cocos2d、渠道 SDK | 见第4节 | Framework | 客户端组 | 2025-10-22 |
| 服务器 jio/rpc | server/core/jio、server/core/rpc | JDK 1.7+、JMX | 8+/依赖版本 | jar | 服务端组 | 2025-10-22 |

维护规则：当第4节条目更新时，需同步评估本映射的“最低兼容版本”与“最近验证”状态。

---

## 6. 版本矩阵与兼容性

| 平台 | 模块 | 依赖 | 最低版本 | 已验证最高版本 | 兼容状态 | 备注 |
| --- | --- | --- | --- | --- | --- | --- |
| Windows | engine | CEGUI | 0.7.1 | 0.7.1 | 通过 | UI框架兼容性良好 |
| Windows | engine | FreeType | 2.4.12 | 2.4.12 | 通过 | 字体渲染正常 |
| Android | updateengine | NDK | r10e | r10e | 通过 | 资源更新功能正常 |
| Android | updateengine | OpenSSL | 1.0.2u | 1.0.2u | 通过 | HTTPS下载功能正常 |
| Android | updateengine | libcurl | 7.40.0 | 7.40.0 | 通过 | HTTP下载功能正常 |
| iOS | client | iOS SDK | 12 | 14 | 通过 | iOS 14兼容性测试通过 |
| Android | client | Android SDK | 4.0+ | 11 | 通过 | Android 11兼容性测试通过 |

说明：变更任一行需触发回归任务；请在提交描述中附带矩阵行号。

---

## 7. 许可证与合规

- 为每个依赖记录：许可证类型、是否需归属声明、是否与 GPL 类许可证产生传染性风险、是否包含专利条款。
- 发布包需附带第三方许可证聚合文本，来源可由脚本生成。
- 商业 SDK（如渠道/统计）需保留合同与版本截图，存档于安全位置。

---

## 8. 自研补丁与定制更改

| 依赖 | 补丁路径 | 变更摘要 | 上游 Issue/PR | 合并策略 | 回归计划 |
| --- | --- | --- | --- | --- | --- |
| CEGUI | patches/cegui/001-fix-utf8.patch | UTF-8字符显示修复 | 无 | 长期维护 | 单元/集成 |
| Cocos2d | patches/cocos2d/001-fix-luajit.patch | LuaJIT内存泄漏修复 | 无 | 长期维护 | 内存泄漏测试 |
| tolua++ | patches/tolua/001-fix-template.patch | 模板生成修复 | 无 | 长期维护 | 绑定功能测试 |

注意：补丁需最小化、可逆；必须在升级前评估冲突并产出替代方案。

---

## 9. 构建与来源

- 源码依赖：记录 commit/tag，保留下载方式与镜像地址。
- 二进制依赖：记录下载地址、校验哈希与解压路径。
- 构建脚本：统一在 scripts/ 或 tools/ 下维护；请在第4节“来源/获取方式”列指向脚本或说明文档。

---

## 10. 审计与健康检查

- 周期性脚本：依赖许可证扫描、二进制漂白检测、重复库版本扫描。
- 触发节点：引擎升级、渠道 SDK 变更、编译器/NDK 升级。
- 审计结果：在本文件底部追加“审计记录”子节，链接至 build_logs/。

---

## 11. 维护流程

1. 提出升级 → 填写影响评估与回滚方案 → 评审通过 → 升级实现 → 回归 → 更新本清单与矩阵。
2. 渠道 SDK：需联动客户端脚本/桥接层，完成灰度发布后再更新为“已验证”。

---

## 附录 A：条目模板示例

- 第四节表（复制粘贴一行后替换字段）
  - 名称：XXX
  - 版本：X.Y.Z
  - 许可证：MIT/BSD/…
  - 语言/平台：C++/Android/…
  - 用途：……
  - 来源/获取方式：……
  - 集成路径：……
  - 构建产出/包：……
  - 备注：……

- 第五节映射表：更新对应模块行并记录“最近验证”日期。

---

## 附录 B：快速校验 Checklist

- [ ] 新引入依赖已在第四节登记
- [ ] 版本矩阵已更新并通过回归
- [ ] 许可证文本已聚合进发布包
- [ ] 自研补丁已评审并存档
- [ ] 下载来源与哈希校验已记录

---

## 附录 C：常见错误与排错指引

- Windows 链接错误：参考 docs/06-编译完整指南.md。
- Android NDK 配置：参考 docs/android/03_环境配置指南.md 与 04_构建流程详解.md。
- 第三方库编译：参考 docs/09-第三方库编译指南.md。
- Cocos2d-x架构问题：参考 docs/10-Cocos2d-x架构专题.md。

---

## 审计记录（追加）

- 2025-10-19：初始化清单，建立模板与示例行。
- 2025-10-22：补充完整版本信息和依赖关系，修正构建路径。