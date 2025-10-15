# MT3.exe 构建与诊断技术文档（Release 版）

本技术文档基于近期在 Windows/VS2013 (v120) 环境下成功完成 MT3.exe 的 Release 构建过程，归纳构建环境、步骤、关键修复、产物验证与常见告警的处理建议，供持续集成与发布参考。

---

## 1. 构建环境

- 操作系统：Windows 10（x86 架构，Win32 目标）
- 编译工具链：
  - Visual Studio 2013 (PlatformToolset v120)
  - MSBuild 12.0
  - Windows SDK 8.1（头文件/库）  
- 解决方案与工程：
  - 主解决方案：[FireClient.sln](client/FireClient/FireClient.sln:1)
  - 可执行工程：[MT3（Win32）](client/MT3Win32App/mt3.win32.vcxproj:1)
  - 静态库工程：[FireClient（Win32）](client/MT3Win32App/FireClient.win32.vcxproj:1)
  - 第三方/依赖工程：cocos2d、CocosDenshion、libogg、libvorbis、speex、pcre、cegui、SILLY、lua 等

---

## 2. 构建目标与产物

- 构建目标：Release|Win32
- 产物输出：
  - 可执行文件：[MT3.exe](client/FireClient/Release.win32/MT3.exe:1)
  - 调试符号：[MT3.pdb](client/FireClient/Release.win32/MT3.pdb:1)
  - 链接库（示例）：libcocos2d.dll/lib，libCocosDenshion.dll/lib，engine.lib，platform.lib，FireClient.lib 等（均位于 [Release.win32](client/FireClient/Release.win32:1)）

- PostBuild 拷贝（自动执行，由工程脚本配置）：
  - 将 MT3.exe 与依赖 DLL 拷贝至发布目录：resource/bin/Release  
    脚本参考：[C++.PostBuildEvent.Command](client/MT3Win32App/mt3.win32.vcxproj:119)

---

## 3. 标准构建步骤（MSBuild）

为避免 PDB 并发写入（C1041）等问题，使用串行构建参数（禁用项目级并行编译）。

1) 刷新 CocosDenshion（确保导出音效接口）
- 作用：生成/刷新 libCocosDenshion.lib/.dll，导出 SimpleAudioEngine 扩展接口符号
- 触发工程：[libCocosDenshion（Win32 DLL）](cocos2d-2.0-rc2-x-2.0.1/CocosDenshion/proj.win32/CocosDenshion.win32.vcxproj:1)
- 产物路径：[libCocosDenshion.lib/.dll](cocos2d-2.0-rc2-x-2.0.1/CocosDenshion/proj.win32/Release.win32/libCocosDenshion.lib:1)

2) 构建主解决方案（Release|Win32）
- 解决方案：[FireClient.sln](client/FireClient/FireClient.sln:1)
- 推荐命令（串行构建）：
  - “禁用并行”构建：MultiProcessorCompilation=false，/m:1
  - 以 PowerShell 调用为例：
    ```
    & 'C:\Program Files (x86)\MSBuild\12.0\Bin\MSBuild.exe' `
      'e:\MT3\client\FireClient\FireClient.sln' `
      '/t:Build' '/p:Configuration=Release' '/p:Platform=Win32' `
      '/p:MultiProcessorCompilation=false' /m:1
    ```

3) 确认产物
- 主产物：[MT3.exe](client/FireClient/Release.win32/MT3.exe:1)
- 依赖库：均在 [Release.win32 目录](client/FireClient/Release.win32:1)产出

---

## 4. 关键修复与构建要点

### 4.1 缺失源文件导致的编译中断（C1083）
- 现象：编译 FireClient 时提示无法打开源文件 `..\FireClient\Application\oggenc\encode.cpp`
- 工程引用位置：[filename](client/MT3Win32App/FireClient.win32.vcxproj:213)
- 修复：补齐 [filename](client/FireClient/Application/oggenc/encode.cpp:1)（占位/恢复实现），保证工程可完整编译。

### 4.2 并发编译导致 PDB 冲突（fatal error C1041）
- 现象：ANGLE `translator_common` 等目标在并发下写同一 PDB 文件导致 C1041
- 处理：串行构建（禁用并行编译），参数：`/p:MultiProcessorCompilation=false /m:1`

### 4.3 CocosDenshion 导出符号缺失导致链接失败（LNK1120）
- 现象：链接时找不到 `SimpleAudioEngine::setEffectVolume/hasEffect/isEffectPlaying`
- 接口声明位置：[C++.SimpleAudioEngine::setEffectVolume](cocos2d-2.0-rc2-x-2.0-1/CocosDenshion/include/SimpleAudioEngine.h:153)、[C++.SimpleAudioEngine::hasEffect](cocos2d-2.0-rc2-x-2.0-1/CocosDenshion/include/SimpleAudioEngine.h:154)、[C++.SimpleAudioEngine::isEffectPlaying](cocos2d-2.0-rc2-x-2.0-1/CocosDenshion/include/SimpleAudioEngine.h:155)  
- 适配/调用示例位置：[filename](engine/soundarea/xpenvsoundarea.cpp:258)
- 修复：先独立重建 [libCocosDenshion 工程](cocos2d-2.0-rc2-x-2.0.1/CocosDenshion/proj.win32/CocosDenshion.win32.vcxproj:1)，确保产出 [libCocosDenshion.lib/.dll](client/FireClient/Release.win32/libCocosDenshion.lib:1)；主工程链接时在库目录中正确检索到该库。

### 4.4 链接检索路径（库目录）
- 目标工程链接检索路径配置：
  - [C++.Link.AdditionalLibraryDirectories](client/MT3Win32App/mt3.win32.vcxproj:108)
  - 已包含：`$(OutDir)`（即 Release.win32）、CocosDenshion 的 Release 路径等
- 如库检索失败，优先检查上述库目录是否包含产出的 `libCocosDenshion.lib/.dll`。

---

## 5. 常见告警与处理建议

- MSB8028/MSB8027：共享中间目录（如多个项目/同名源文件输出到同一 Release.win32）
  - 建议：为 translator_common/translator_hlsl、FireClient/MT3 配置独立 `IntDir/OutDir`，减少清理/重建的交叉影响。

- MSB3245 “platform.winmd”无法解析
  - 来源：个别依赖工程（pcre、SILLY、lua）在 ResolveAssemblyReferences 阶段尝试解析与本项目无关的 WinMD。  
  - 处理：属于无害告警；如需要彻底消除，移除相关工程中无用的 WinMD 引用。

- LNK4099（libjpeg 等第三方预编译库缺少匹配 PDB）
  - 说明：第三方库使用 VS2012(vc110) 工具链生成，发布库缺调试符号。  
  - 处理：不影响运行；如需消除告警，统一用 v120 重建此类第三方库并提供 PDB。

- LNK4098（默认 CRT 冲突）
  - 说明：默认库 “MSVCRT.lib” 与 “libcmt.lib/libcmtd.lib” 冲突，提示 /NODEFAULTLIB。  
  - 处理：当前已成功链接；若需彻底清洁，统一工程 RuntimeLibrary 为 `/MD`（Release）/`/MDd`（Debug）并清理多余默认库。

---

## 6. 依赖库清单（Release.win32）

位于 [Release.win32](client/FireClient/Release.win32:1) 的主要库示例：

- 图形/引擎：libcocos2d.dll/lib、libSpine.lib
- 音频：libCocosDenshion.dll/lib
- ANGLE/GL：libEGL.*、libGLESv2.*、translator_common.lib、translator_hlsl.lib、preprocessor.lib
- 基础组件：engine.lib、platform.lib、ljfm.lib、cauthc.lib、FireClient.lib
- 媒体/压缩：libpng.lib、libjpeg.lib、libtiff.lib、zlib.lib、freetype.lib
- 编解码：libogg.lib、libvorbis.lib、libspeex.lib
- 脚本：lua.lib、liblua.lib
- UI：cegui.lib、SILLY.lib
- 网络：libcurl_imp.lib
- 系统：Ws2_32.lib、winmm.lib 等

---

## 7. 故障诊断快速流程

1) 编译期报错 C1083（源文件缺失）
- 检查工程包含项：是否引用了不存在的源文件  
  - 例：[filename](client/MT3Win32App/FireClient.win32.vcxproj:213) 指向 `..\FireClient\Application\oggenc\encode.cpp`  
- 修复：补齐源文件或从工程移除该编译项。

2) 并发 PDB 写入报错 C1041（translator_common 等）
- 处理：禁用并行编译：`/p:MultiProcessorCompilation=false /m:1`

3) 链接未解析符号（LNK1120 / LNK2001）
- 优先检查库产出是否存在（如 CocosDenshion）
  - 单独重建：[filename](cocos2d-2.0-rc2-x-2.0.1/CocosDenshion/proj.win32/CocosDenshion.win32.vcxproj:1)
  - 确认库路径在链接检索目录中（见 [C++.Link.AdditionalLibraryDirectories](client/MT3Win32App/mt3.win32.vcxproj:108)）
- 确认导出接口声明匹配：  
  - [C++.SimpleAudioEngine::setEffectVolume](cocos2d-2.0-rc2-x-2.0-1/CocosDenshion/include/SimpleAudioEngine.h:153)  
  - [C++.SimpleAudioEngine::hasEffect](cocos2d-2.0-rc2-x-2.0-1/CocosDenshion/include/SimpleAudioEngine.h:154)  
  - [C++.SimpleAudioEngine::isEffectPlaying](cocos2d-2.0-rc2-x-2.0-1/CocosDenshion/include/SimpleAudioEngine.h:155)

4) CRT 冲突告警（LNK4098）
- 对齐工程 RuntimeLibrary（MD/MDd），清理重复默认库。

---

## 8. 验证与发布

- 构建验证：MSBuild 输出显示 MT3.exe 成功链接并生成（见 Link 阶段日志）
- 本地运行验证：双击 [MT3.exe](client/FireClient/Release.win32/MT3.exe:1)（需同目录依赖 DLL）
- 发布拷贝：由 PostBuild 脚本自动将 MT3.exe 与 DLL 拷贝至 `resource/bin/Release`  
  - 配置参考：[C++.PostBuildEvent.Command](client/MT3Win32App/mt3.win32.vcxproj:119)

---

## 9. 附录：常用命令参考

- 刷新 CocosDenshion（Win32 Release）
  ```
  & 'C:\Program Files (x86)\MSBuild\12.0\Bin\MSBuild.exe' `
    'e:\MT3\cocos2d-2.0-rc2-x-2.0.1\CocosDenshion\proj.win32\CocosDenshion.win32.vcxproj' `
    '/t:Clean;Build' '/p:Configuration=Release' '/p:Platform=Win32' /m:1
  ```

- 构建主解决方案（Win32 Release，串行）
  ```
  & 'C:\Program Files (x86)\MSBuild\12.0\Bin\MSBuild.exe' `
    'e:\MT3\client\FireClient\FireClient.sln' `
    '/t:Build' '/p:Configuration=Release' '/p:Platform=Win32' `
    '/p:MultiProcessorCompilation=false' /m:1
  ```

---

## 10. 备注

- ANGLE 相关库（libEGL/libGLESv2 及 translator/preprocessor）在本项目 Release 构建中参与链接。若仅 Win32 桌面渲染，后续可评估剔除不必要的 WinRT 组件以精简依赖。
- 本文档为 Release 构建流程与诊断基线，Debug 构建可能存在额外差异（如 PDB 输出、运行时宏等），需另行评估。