# MT3 技术栈指引（v120 主线权威版）
文档版本: 1.0
最后更新: 2025-10-14
适用范围: Windows 10, VS2013 v120（权威）；VS2019/2022 需安装 v120 工具集

---

## 1. 指引目标与适用边界

- 输出一份统一、准确、可执行的“技术栈指引”，用于指导 MT3 项目构建、链接、运行与发布。
- 明确主线采用 VS2013 v120 工具集；所有 v140（VS2015）内容仅作为历史归档，不进入主线。
- 对文档目录进行系统性统筹：识别并剔除陈旧或错误文档，整合有效内容形成唯一权威入口。

参考主线权威文档：
- 编译完整指南：[filename](docs/06-编译完整指南.md:1)
- 编译步骤工作计划：[filename](docs/00-编译步骤工作计划.md:1)
- Release 构建诊断基线：[filename](docs/MT3_Release_Build_Diagnostics.md:1)
- FireClient 构建诊断基线：[filename](docs/MT3_FireClient_Build_Diagnostics.md:1)
- libEGL/ANGLE 专题说明：[filename](docs/libEGL链接错误修复说明.md:1)

---

## 2. 技术栈总览（Win32 v120 主线）

- 语言与标准：C++（VC++，C++03/早期 C++11 特性），Unicode
- 平台与工具链：
  - Visual Studio 2013 (PlatformToolset v120)
  - MSBuild 12.0
  - Windows SDK 8.1
- 游戏与图形：
  - Cocos2d-x 2.0（Win32 原生 OpenGL via GLEW；非 WinRT）
  - Spine（动画）
- 音频与编解码：
  - CocosDenshion（FMOD 后端）
  - libogg/libvorbis/libspeex（语音/音频编码）
- UI：
  - CEGUI（SILLY 图片编解码、PCRE 文本匹配）
- 基础库：
  - platform.lib、engine.lib、ljfm.lib、cauthc.lib、lua.lib 等

核心工程索引：
- 可执行工程：[filename](client/MT3Win32App/mt3.win32.vcxproj:1)
- 静态库工程（游戏逻辑）：[filename](client/MT3Win32App/FireClient.win32.vcxproj:1)
- CocosDenshion 工程：[filename](cocos2d-2.0-rc2-x-2.0.1/CocosDenshion/proj.win32/CocosDenshion.win32.vcxproj:1)

---

## 3. 运行时库与编译配置基线

- 运行时库统一：
  - Debug：/MDd（MultiThreadedDebugDLL）
  - Release：/MD（MultiThreadedDLL）
- 字符集：Unicode
- 预处理/编译选项一致性：
  - 统一使用 v120 工具集
  - 避免忽略 msvcrt/msvcrtd 默认库（LNK4098 冲突源）
- 并发构建策略：
  - 如出现 PDB 并发写冲突（C1041），在解决方案级构建时禁用并行：
    - /p:MultiProcessorCompilation=false /m:1
  - 参考：[filename](docs/MT3_Release_Build_Diagnostics.md:70)

---

## 4. 依赖与产物目录规范

- 目录规范（推荐）：
  - 中间/输出目录区分工程，避免共享导致 MSB8028：
    - IntDir：$(ProjectName)\$(Configuration)\
    - OutDir：$(SolutionDir)$(ProjectName)\$(Configuration)\
- 链接库检索目录（可执行工程）：
  - 见 [XML.Link.AdditionalLibraryDirectories](client/MT3Win32App/mt3.win32.vcxproj:73)、[XML.Link.AdditionalLibraryDirectories](client/MT3Win32App/mt3.win32.vcxproj:108)
- 关键库示例（Release.win32 输出）：
  - libcocos2d.dll/lib、libCocosDenshion.dll/lib、engine.lib、platform.lib、ljfm.lib、cauthc.lib、FireClient.lib
  - 参考：[filename](docs/MT3_Release_Build_Diagnostics.md:107)

---

## 5. 编译与链接统一流程（权威）

权威流程入口：
- 完整指南：[filename](docs/06-编译完整指南.md:67)
- 工作计划（阶段门）：[filename](docs/00-编译步骤工作计划.md:10)

标准顺序（组件 → 引擎 → 应用）：
1) 基础库（platform.lib、ljfm.lib、lua.lib、cauthc.lib）
2) 引擎层（libcocos2d.dll、libCocosDenshion.dll、engine.lib）
3) 应用层（MT3.exe），PostBuild 拷贝运行时 DLL

MSBuild 模板（v120）：
- set MSBUILD="C:\Program Files (x86)\MSBuild\12.0\Bin\MSBuild.exe"
- %MSBUILD% <项目> /t:Rebuild /p:Configuration=Release /p:Platform=Win32 /p:PlatformToolset=v120 /v:minimal /nologo /m

参考命令与路径详见：
- [filename](docs/06-编译完整指南.md:100)

---

## 6. 常见错误 → 根因 → 解法（映射）

- C1083 无法打开源文件（encode.cpp 缺失）
  - 根因：工程包含项引用 [filename](client/MT3Win32App/FireClient.win32.vcxproj:213)，本地文件缺失或路径大小写不一致
  - 解法：恢复 [filename](client/FireClient/Application/oggenc/encode.cpp:1)（占位可解阻，但需用原始实现替换）
  - 证据：[filename](docs/MT3_FireClient_Build_Diagnostics.md:24)、[filename](docs/MT3_Release_Build_Diagnostics.md:65)
- LNK1104 无法打开 FireClient.lib
  - 根因：上游 FireClient 未成功产出 → 下游 MT3 链接找不到库
  - 解法：先构建 FireClient；确认库目录包含 $(SolutionDir)$(Configuration).win32
  - 证据：[filename](client/MT3Win32App/Release.win32/MT3.tlog/link.command.1.tlog:2)
- C1041 PDB 并发写入冲突（ANGLE/translator_common 等）
  - 解法：禁用并行编译：/p:MultiProcessorCompilation=false /m:1
  - 证据：[filename](docs/MT3_Release_Build_Diagnostics.md:70)
- LNK1120/LNK2019 未解析符号（SimpleAudioEngine 扩展接口）
  - 根因：libCocosDenshion 未更新或库目录缺失
  - 解法：单独重建 [filename](cocos2d-2.0-rc2-x-2.0.1/CocosDenshion/proj.win32/CocosDenshion.win32.vcxproj:1)；确认 MT3 链接器库目录
  - 证据：[filename](docs/MT3_Release_Build_Diagnostics.md:74)
- LNK4099 第三方库无匹配 PDB
  - 解法：非致命；可接受或统一用 v120 重建引入 PDB
  - 证据：[filename](docs/MT3_Release_Build_Diagnostics.md:97)
- LNK4098 CRT 默认库冲突
  - 解法：统一 /MD（Release）/MDd（Debug），清理重复默认库
  - 证据：[filename](docs/MT3_Release_Build_Diagnostics.md:101)

---

## 7. ANGLE/libEGL 专题（Win32 主线的处理）

- Win32 桌面主线不依赖 ANGLE（EGL/GLESv2），使用原生 OpenGL + GLEW
- 如 Win32 项目中混入 libEGL/libGLESv2 依赖，建议移除
- 详解与操作示例见：[filename](docs/libEGL链接错误修复说明.md:1)

---

## 8. 文档体系与权威入口（统一）

- 主线生产文档（建议阅读路径）：
  - 00-编译步骤工作计划：[filename](docs/00-编译步骤工作计划.md:1)
  - 06-编译完整指南（权威）：[filename](docs/06-编译完整指南.md:1)
  - Release 构建诊断基线：[filename](docs/MT3_Release_Build_Diagnostics.md:1)
  - FireClient 构建诊断基线：[filename](docs/MT3_FireClient_Build_Diagnostics.md:1)
  - 文档索引（重建为 13-文档索引）：参考旧版 [filename](docs/文档索引.md:1)，按“序号+中文名称”体系更新

- 专题技术（作为附录或专题）：
  - CRT 库冲突、CEGUI 集成、libEGL/ANGLE 差异等，聚合为“12-特殊技术专题.md”（待建）

---

## 9. 归档与剔除（执行清单）

- 归档路径：
  - docs/archive/v140相关文档/
  - docs/archive/临时报告/

- 必须归档（不进入主线）：
  - v140 编译系列文档（示例）：
    - [filename](docs/archive/v140相关文档/README_v140编译指南.md:1)
    - [filename](docs/archive/v140相关文档/MT3_v140编译最终总结报告.md:1)
    - [filename](docs/archive/v140相关文档/v140编译配置说明.md:1)
    - [filename](docs/archive/v140相关文档/v140迁移完整解决方案.md:1)
    - [filename](docs/archive/v140相关文档/v140编译失败组件详细清单.md:1)

- 建议归档（阶段性/临时报告）：
  - [filename](docs/archive/临时报告/编译成功报告.md:1)
  - [filename](docs/archive/临时报告/编译进度与说明.md:1)
  - [filename](docs/archive/临时报告/文档整理报告.md:1)
  - [filename](docs/archive/临时报告/项目整理完成报告.md:1)
  - [filename](docs/archive/临时报告/Phase_A_v120_编译尝试报告.md:1)
  - [filename](docs/archive/临时报告/MT3编译大小分析报告.md:1)

- 已识别重复/冲突：
  - 文档索引存在新旧并存（“文档索引.md” vs 计划中的 “13-文档索引.md”），需按目标结构重建后仅保留新入口
  - 编译指南存在 v120 与 v140 混入内容，已在 [filename](docs/06-编译完整指南.md:13) 明确主线与归档边界

---

## 10. 验证与发布（运行时准备）

- 产物验证（示例）：
  - dir client\MT3Win32App\Release.win32\MT3.exe
  - dir client\MT3Win32App\Release.win32\*.dll
- PostBuild 拷贝（参考工程脚本）：
  - 见 [C++.PostBuildEvent.Command](client/MT3Win32App/mt3.win32.vcxproj:81)
- 运行时 DLL 清单（示例）：
  - libcocos2d.dll、libCocosDenshion.dll、glew32.dll、pthreadVCE2.dll、libcurl.dll、iconv.dll、libxml2.dll、zlib1.dll、fmodex.dll

---

## 11. 执行入口与快速命令（v120）

- 一键（如提供脚本）：
  - PowerShell：powershell.exe -ExecutionPolicy Bypass -File Build-MT3-v120.ps1
  - 批处理：build_mt3_v120_complete.bat
- 手动（组件→引擎→应用）：
  - 详见 [filename](docs/06-编译完整指南.md:117)

---

## 12. 维护与更新原则

- 准确性第一：所有命令、路径与版本需与 v120 主线一致
- 简洁性：消除冗余段落与重复内容，统一术语（工具集、运行时库、产物路径）
- 链接完整性：跨文档引用采用相对路径，并指向主线新结构文档
- 版本标识：每篇文档保留“文档版本/最后更新/维护状态”

---

## 13. 变更建议（下一步执行）

- 重建文档索引为“13-文档索引.md”，按“序号+中文名称”体系发布统一入口（以本指引与 00/06 为核心）。
- 按本章第 9 节清单执行归档移动；保留归档入口说明，不在主线显式呈现 v140 指令。
- 将 CRT/CEGUI/libEGL 等专题整合为“12-特殊技术专题.md”，并从主线文档仅以专题章节引用。
- 为 FireClient/MT3 工程设置独立 IntDir/OutDir，减少 MSB8028 交叉影响（参考 [filename](docs/MT3_FireClient_Build_Diagnostics.md:75) 建议）。

---

## 附录：证据与索引（关键定位）

- FireClient 工程包含 encode.cpp：
  - [XML.ClCompile Include](client/MT3Win32App/FireClient.win32.vcxproj:213)
  - [XML.ClCompile Include](client/MT3Win32App/FireClient.win32.vcxproj.filters:250)
- FireClient.lib 生成日志确认：
  - [text](client/MT3Win32App/Release.win32/FireClient.win32.log:667)
- MT3 链接命令 /LIBPATH 指向 FireClient 输出：
  - [text](client/MT3Win32App/Release.win32/MT3.tlog/link.command.1.tlog:2)
- CocosDenshion 构建入口：
  - [filename](cocos2d-2.0-rc2-x-2.0.1/CocosDenshion/proj.win32/CocosDenshion.win32.vcxproj:1)