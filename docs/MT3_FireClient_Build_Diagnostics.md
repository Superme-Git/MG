# MT3/FireClient 构建与诊断技术文档 v2025-10-14

概述
本文面向 Windows 10 上使用 VS2013/MSBuild 的 MT3/FireClient 项目，记录一次从编译失败到成功生成可执行的完整构建与诊断过程，并沉淀可复用的规则与方法。

环境与工具
- 操作系统：Windows 10
- 编译工具链：Visual Studio 2013 (v120), MSBuild 12.0, Windows SDK 8.1
- 工作目录：e:/MT3
- 关键项目：FireClient.sln、[client/MT3Win32App/mt3.win32.vcxproj](client/MT3Win32App/mt3.win32.vcxproj:1)、[cocos2d-2.0-rc2-x-2.0.1/CocosDenshion/proj.win32/CocosDenshion.win32.vcxproj](cocos2d-2.0-rc2-x-2.0.1/CocosDenshion/proj.win32/CocosDenshion.win32.vcxproj:1)

工程与依赖拓扑（简）
- 下游可执行 MT3 依赖静态库：见 [XML.Link.AdditionalDependencies](client/MT3Win32App/mt3.win32.vcxproj:71)、[XML.Link.AdditionalDependencies](client/MT3Win32App/mt3.win32.vcxproj:106)
- 关键库目录：见 [XML.Link.AdditionalLibraryDirectories](client/MT3Win32App/mt3.win32.vcxproj:73)、[XML.Link.AdditionalLibraryDirectories](client/MT3Win32App/mt3.win32.vcxproj:108)
- CocosDenshion 产物目录：[cocos2d-2.0-rc2-x-2.0.1/CocosDenshion/proj.win32/Debug.win32](cocos2d-2.0-rc2-x-2.0.1/CocosDenshion/proj.win32/Debug.win32)

问题现象
- 编译错误 C1083：无法打开源文件 “..\FireClient\Application\oggenc\encode.cpp”。
- 链接错误 LNK1104：无法打开文件 “FireClient.lib”。
- 后续链接阶段缺库：libEGL.lib、libGLESv2.lib、libBox2D.lib、libCocosDenshion.lib。
- 警告：MSB8028（中间目录共享）、LNK4099（第三方库缺 PDB）、LNK4098（默认库冲突）。

问题溯源与根本原因（精确定位）
- 根因一：工程引用的 oggenc 源文件缺失 → FireClient 编译失败 → FireClient.lib 未产出 → 下游 MT3 链接 LNK1104。缺失文件： [client/FireClient/Application/oggenc/encode.cpp](client/FireClient/Application/oggenc/encode.cpp:1)。
- 根因二：下游链接阶段第三方库未先生成或库目录未配置/拼写错误（如 CocosDenshion 路径 2.0-1 误拼）→ LNK1104。库工程与目录： [cocos2d-2.0-rc2-x-2.0.1/CocosDenshion/proj.win32/CocosDenshion.win32.vcxproj](cocos2d-2.0-rc2-x-2.0.1/CocosDenshion/proj.win32/CocosDenshion.win32.vcxproj:1)，库目录项： [client/MT3Win32App/mt3.win32.vcxproj](client/MT3Win32App/mt3.win32.vcxproj:73)、[client/MT3Win32App/mt3.win32.vcxproj](client/MT3Win32App/mt3.win32.vcxproj:108)。
- 次要因素： [C++.ReadFromWav()](client/FireClient/Application/oggenc/oggenc.cpp:42) 使用通用标识可能被宏/预处理器污染，产生未声明标识符错误。

排查思路
- 步骤化定位：
  1) 确认编译错误源：检查缺失文件路径与工程引用，若不存在则补回/恢复。
  2) 构建上游库：先编译所有被依赖库工程（如 CocosDenshion），确保生成 .lib。
  3) 链接目录校验：比对 AdditionalLibraryDirectories 与实际库目录，消除拼写/版本号错误。
  4) 链接库名核对：比对 AdditionalDependencies 与实际文件名（lib 前缀 vs 非前缀）。
  5) 复现与验证：每次修改后重建并核查新的链接缺库，迭代补齐。
- 日志证据：通过 MSBuild 输出与 /LIBPATH 展开值核对是否指向真实目录。

解决方案与实施
- A. 恢复 oggenc 源实现：
  - 确保 [client/FireClient/Application/oggenc/encode.cpp](client/FireClient/Application/oggenc/encode.cpp:1) 存在并可编译。
  - 修复 [C++.ReadFromWav()](client/FireClient/Application/oggenc/oggenc.cpp:42)：
    - 采用唯一变量名 __remaining/__copy，避免宏干扰。
    - 严格计算剩余长度与推进位置，规范边界拷贝。
  - 完善 Ogg 写出流程（如 SaveWavData2Ogg）：写前建目录、释放内存、健壮关闭资源。
- B. 构建第三方库并修正库目录：
  - 构建 CocosDenshion：生成 [cocos2d-2.0-rc2-x-2.0.1/CocosDenshion/proj.win32/Debug.win32/libCocosDenshion.lib](cocos2d-2.0-rc2-x-2.0.1/CocosDenshion/proj.win32/Debug.win32/libCocosDenshion.lib:1)。
  - 修正 MT3 的库目录：在 [XML.Link.AdditionalLibraryDirectories](client/MT3Win32App/mt3.win32.vcxproj:73)、[XML.Link.AdditionalLibraryDirectories](client/MT3Win32App/mt3.win32.vcxproj:108) 中追加/修正：
    - ../../cocos2d-2.0-rc2-x-2.0.1/cocos2dx/platform/third_party/winrt/angleproject/lib/$(Configuration)
    - ../../dependencies/opengles_v2/Lib
    - ../../client/FireClient/$(Configuration).win32
    - ../../cocos2d-2.0-rc2-x-2.0.1/CocosDenshion/proj.win32/$(Configuration).win32
  - 修正路径拼写错误：2.0-1 → 2.0.1。
- C. 重建并验证：
  - 重建 MT3（Debug|Win32）→ 链接成功，生成 [client/MT3Win32App/Debug.win32/MT3.exe](client/MT3Win32App/Debug.win32/MT3.exe)。
  - PostBuild 拷贝： [XML.PostBuildEvent](client/MT3Win32App/mt3.win32.vcxproj:118) 将产物复制到 resource\bin\Debug。

验证方法与结果
- 构建成功判定：MSBuild ExitCode=0，链接阶段无 LNK1104/LNK2019 致命错误。
- 产物存在性：文件存在于 [client/MT3Win32App/Debug.win32](client/MT3Win32App/Debug.win32:1)。
- 产物指纹：MD5=706da5a2808d47d2b6a56735cdd79bd8（certutil -hashfile 结果）。
- 日志时间：MSBuild 链接阶段显示生成于约 17:24:16（本地时区 UTC+8）。
- 警告状态：存在 123 条警告，主要为第三方库调试符号与运行时库冲突，属非致命。

构建与打包命令参考（Windows）
- 构建 CocosDenshion（生成 libCocosDenshion.lib）
  - & "C:\Program Files (x86)\MSBuild\12.0\Bin\MSBuild.exe" "cocos2d-2.0-rc2-x-2.0.1\CocosDenshion\proj.win32\CocosDenshion.win32.vcxproj" /t:Rebuild /p:Configuration=Debug /p:Platform=Win32 /m
- 构建 MT3（生成 MT3.exe 并执行 PostBuild）
  - & "C:\Program Files (x86)\MSBuild\12.0\Bin\MSBuild.exe" "client\MT3Win32App\mt3.win32.vcxproj" /t:Rebuild /p:Configuration=Debug /p:Platform=Win32 /m
- 产物指纹计算
  - certutil -hashfile "client\MT3Win32App\Debug.win32\MT3.exe" MD5

规则化与最佳实践
- 库生成顺序：先构建被依赖库工程（如 CocosDenshion/Box2D/ANGLE），再构建可执行。
- 目录配置：将库产物目录按 $(Configuration)（Debug/Release）区分并纳入 AdditionalLibraryDirectories。
- 路径校验：严格核对版本号与路径拼写（示例：2.0.1 vs 2.0-1），避免隐性失败。
- 中间目录隔离：避免多个项目共享 IntDir/OutDir。建议：
  - 将 [XML.PropertyGroup.IntDir](client/MT3Win32App/mt3.win32.vcxproj:44) 改为 $(ProjectName)\$(Configuration)\
  - 将 [XML.PropertyGroup.OutDir](client/MT3Win32App/mt3.win32.vcxproj:43) 改为 $(SolutionDir)$(ProjectName)\$(Configuration)\
- 运行时库一致性：统一 CRT（Debug:/MDd，Release:/MD），必要时使用 /NODEFAULTLIB 排除冲突。
- 调试符号策略：第三方预编译库缺 PDB 时接受 LNK4099 或引入匹配 PDB。
- 代码层防御：变量命名避免与宏冲突，写文件前确保目录存在，资源释放完备。示例： [C++.ReadFromWav()](client/FireClient/Application/oggenc/oggenc.cpp:42)。

关键文件与配置位置（索引）
- MT3 可执行工程：[client/MT3Win32App/mt3.win32.vcxproj](client/MT3Win32App/mt3.win32.vcxproj:1)
  - 头文件目录：[XML.ClCompile.AdditionalIncludeDirectories](client/MT3Win32App/mt3.win32.vcxproj:58)
  - 预处理宏：[XML.ClCompile.PreprocessorDefinitions](client/MT3Win32App/mt3.win32.vcxproj:60)
  - 静态库列表：[XML.Link.AdditionalDependencies](client/MT3Win32App/mt3.win32.vcxproj:71)、[XML.Link.AdditionalDependencies](client/MT3Win32App/mt3.win32.vcxproj:106)
  - 库目录列表：[XML.Link.AdditionalLibraryDirectories](client/MT3Win32App/mt3.win32.vcxproj:73)、[XML.Link.AdditionalLibraryDirectories](client/MT3Win32App/mt3.win32.vcxproj:108)
  - PostBuild 拷贝：[XML.PostBuildEvent.Command](client/MT3Win32App/mt3.win32.vcxproj:119)
- CocosDenshion 库工程：[cocos2d-2.0-rc2-x-2.0.1/CocosDenshion/proj.win32/CocosDenshion.win32.vcxproj](cocos2d-2.0-rc2-x-2.0.1/CocosDenshion/proj.win32/CocosDenshion.win32.vcxproj:1)
- ANGLE/OpenGLES 库目录：
  - ../../cocos2d-2.0-rc2-x-2.0.1/cocos2dx/platform/third_party/winrt/angleproject/lib/$(Configuration)
- FireClient 输出目录：
  - ../../client/FireClient/$(Configuration).win32
- MT3 产物目录：
  - [client/MT3Win32App/Debug.win32](client/MT3Win32App/Debug.win32:1)

产物指纹与发布校验
- 可执行： [client/MT3Win32App/Debug.win32/MT3.exe](client/MT3Win32App/Debug.win32/MT3.exe)
- MD5：706da5a2808d47d2b6a56735cdd79bd8
- 建议：发布时提交 MD5/SHA256，打包时保留构建日志与库版本清单。

附录：示例修复片段（仅索引）
- 修复 ReadFromWav 的变量命名与边界处理： [C++.ReadFromWav()](client/FireClient/Application/oggenc/oggenc.cpp:42)
- 修正库路径拼写： [XML.Link.AdditionalLibraryDirectories](client/MT3Win32App/mt3.win32.vcxproj:73)、[XML.Link.AdditionalLibraryDirectories](client/MT3Win32App/mt3.win32.vcxproj:108)

常见问题与应对
- Q: 为什么 Clean/Rebuild 会异常？
  - A: 多工程共享 IntDir/OutDir，触发 MSB8028。请为各工程设置独立中间/输出目录。
- Q: LNK4099 是否必须处理？
  - A: 非致命，影响仅为无法进入库内部调试。若需消除，请引入匹配版本的 PDB。
- Q: 运行时库冲突（LNK4098）如何处理？
  - A: 统一编译选项（/MDd 或 /MD），必要时用 /NODEFAULTLIB 排除冲突库。
- Q: ANGLE/OpenGLES 库缺失如何定位？
  - A: 搜索 angleproject/lib/$(Configuration) 与 dependencies/opengles_v2/Lib 并在链接器库目录中添加。

版本与维护
- 文档版本：v2025-10-14
- 维护建议：每次依赖升级需更新：库工程构建步骤、库目录、链接清单、PostBuild 拷贝与产物指纹。

结束语
通过严格的“先库后执行”、目录与拼写校验、以及迭代式链接缺库补齐，本次构建链路已完成闭环修复，产物 [client/MT3Win32App/Debug.win32/MT3.exe](client/MT3Win32App/Debug.win32/MT3.exe) 已成功生成。该文档可作为后续构建与排障的操作规范。