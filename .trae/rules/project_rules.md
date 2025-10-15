# MT3项目规则文件

## 1. 项目概述

### 1.1 项目介绍
MT3（梦幻西游MG）是一款基于Cocos2d-x 2.0.1引擎开发的跨平台游戏项目，采用C++与Lua混合编程模式，支持Windows、Android、iOS等多个平台。

### 1.2 技术架构
- **核心引擎**：Cocos2d-x 2.0.1（固定管线渲染）
- **编程语言**：C++（C++11）、Lua
- **渲染系统**：OpenGL ES 1.x/2.0（基于固定管线）
- **音频系统**：CocosDenshion（包含SimpleAudioEngine）
- **物理引擎**：Box2D、chipmunk
- **脚本引擎**：Lua + tolua++（用于C++绑定）
- **UI系统**：CEGUI 0.x（静态链接）
- **崩溃报告**：google-breakpad
- **网络协议**：protobuf
- **正则表达式**：pcre
- **图像加载**：SILLY、libpng、libjpeg、libtiff
- **字体渲染**：FreeType
- **音频编解码**：libspeex、libogg、libvorbis
- **网络库**：libcurl
- **OpenGL ES支持**：ANGLE（用于Windows平台）

### 1.3 项目目标
维护和优化现有游戏功能，确保项目稳定运行，同时为未来可能的引擎升级（如Cocos2d-x 3.17.2）做准备。

## 2. 环境配置

### 2.1 开发环境要求
- **操作系统**：Windows 7/8/10/11
- **开发工具**：Visual Studio 2015（Community/Professional/Enterprise）
  - 必须安装完整的Visual C++工具集
  - 推荐安装Windows SDK 10.0.19041.0或更高版本
- **编译器**：Visual C++ 14.0（v140平台工具集）
- **SDK**：Windows SDK 8.1或10（项目配置为10.0.19041.0）
- **构建工具**：MSBuild 14.0
- **运行时**：Visual C++ Redistributable for Visual Studio 2015（x86版本）

### 2.2 环境变量配置
确保以下环境变量已正确配置：
- `VS140COMNTOOLS`：指向Visual Studio 2015的Common7\Tools目录
  - 典型路径：`C:\Program Files (x86)\Microsoft Visual Studio 14.0\Common7\Tools\`
- `PATH`：包含以下路径
  - `C:\Program Files (x86)\MSBuild\14.0\Bin\`
  - `C:\Program Files (x86)\Microsoft Visual Studio 14.0\Common7\IDE\`
  - `C:\Program Files (x86)\Windows Kits\10\bin\x86\`

### 2.3 第三方库依赖层次结构

项目依赖多个第三方库，按编译顺序分为6层：

**第1层：基础库**
- `liblua.lib` - Lua解释器
- `libBox2D.lib` - 2D物理引擎
- `libchipmunk.lib` - 轻量级物理引擎
- `pcre.lib` - PERL兼容正则表达式库
- `SILLY.lib` - 图像加载库
- `libspeex.lib` - 语音编解码
- `libogg.lib` - 音频容器格式
- `libvorbis.lib` - 音频编解码
- `libpng.lib`、`libjpeg.lib`、`libtiff.lib` - 图像格式支持
- `freetype.lib` - 字体渲染
- `zlib.lib` - 压缩库

**第2层：ANGLE组件（OpenGL ES转DirectX）**
- `preprocessor.lib` - GLSL预处理器
- `translator_common.lib` - 着色器转换通用库
- `translator_hlsl.lib` - GLSL到HLSL转换器
- `esUtil.lib` - OpenGL ES工具库

**第3层：引擎核心**
- `libcocos2d.lib` - Cocos2d-x核心引擎
- `libSpine.lib` - 骨骼动画支持
- `libCocosDenshion.lib` - 音频引擎

**第4层：游戏框架**
- `cegui.lib` - UI渲染库
- `platform.lib` - 平台抽象层
- `cauthc.lib` - 认证库
- `ljfm.lib` - 文件管理模块

**第5层：游戏引擎**
- `engine.lib` - 游戏核心引擎

**第6层：应用程序**
- `FireClient.lib` - 客户端静态库
- `MT3.exe` - 主应用程序

详细依赖关系图请参考 [`doc/MT3项目编译问题分析与解决方案.md`](doc/MT3项目编译问题分析与解决方案.md:42)

## 3. 目录结构

### 3.1 主要目录说明
```
├── .git/              # Git版本控制
├── .gitignore         # Git忽略规则
├── client/            # 客户端代码
│   ├── MT3Win32App/   # 主应用程序项目
│   ├── FireClient/    # 客户端静态库
│   ├── Launcher/      # 启动器
│   └── resource/      # 游戏资源
├── common/            # 公共库代码
│   ├── cauthc/        # 认证库
│   ├── ljfm/          # 功能模块
│   ├── lua/           # Lua相关代码
│   └── platform/      # 平台抽象层
├── cocos2d-2.0-rc2-x-2.0.1/  # Cocos2d-x引擎源码
├── dependencies/      # 第三方依赖库
├── doc/               # 项目文档
├── engine/            # 游戏引擎代码
├── gbeans/            # 游戏配置文件
├── scheme_doc/        # 策划文档
├── server/            # 服务器代码
└── tools/             # 开发工具
```

### 3.2 重要文件说明
- [`client/FireClient/FireClient.sln`](client/FireClient/FireClient.sln:1)：主解决方案文件（VS2015格式），包含所有项目
- [`client/MT3Win32App/mt3.win32.vcxproj`](client/MT3Win32App/mt3.win32.vcxproj:1)：主应用程序项目文件
- [`cocos2d-2.0-rc2-x-2.0.1/cocos2d-win32.vc2010.sln`](cocos2d-2.0-rc2-x-2.0.1/cocos2d-win32.vc2010.sln:1)：Cocos2d-x引擎解决方案
- `build_mt3.ps1`：自动化编译脚本（PowerShell）
- [`doc/MT3项目编译问题分析与解决方案.md`](doc/MT3项目编译问题分析与解决方案.md:1)：编译问题详细分析与解决方案
- [`doc/MT3项目系统性技术评估与规划报告.md`](doc/MT3项目系统性技术评估与规划报告.md:1)：项目技术评估与升级规划
- [`client/MT3Win32App/stdafx.h`](client/MT3Win32App/stdafx.h:1)：预编译头文件
- [`client/MT3Win32App/main.cpp`](client/MT3Win32App/main.cpp:1)：程序入口点

## 4. 代码规范

### 4.1 命名规范
- **类名**：使用大驼峰命名法，如`PlayerCharacter`
- **方法和函数**：使用小驼峰命名法，如`getPlayerInfo()`
- **变量**：使用小驼峰命名法，如`playerName`
- **常量**：使用全大写加下划线，如`MAX_PLAYER_COUNT`
- **文件命名**：使用大驼峰或下划线分隔，如`PlayerCharacter.cpp`或`player_character.cpp`

### 4.2 编码风格
- **缩进**：使用4个空格进行缩进
- **花括号**：放在行尾
- **每行长度**：不超过100个字符
- **注释**：使用`//`进行单行注释，`/* */`进行多行注释
- **空行**：在逻辑块之间使用空行分隔
- **命名空间**：遵循Cocos2d-x的命名约定

### 4.3 C++代码规范
- 使用标准C++11特性（项目配置已启用C++11）
- 字符集：Unicode（所有项目统一配置）
- 内存管理：
  - 遵循Cocos2d-x 2.0.1的引用计数机制（retain/release/autorelease）
  - Cocos2d-x对象使用`create()`工厂方法创建，自动加入autorelease池
  - 需要长期持有的对象调用`retain()`，不再使用时调用`release()`
  - 避免循环引用，必要时使用弱引用
- 异常处理：使用`_CRT_SECURE_NO_WARNINGS`和`_SCL_SECURE_NO_WARNINGS`宏禁用部分安全警告
- 编译警告：已禁用4267、4251、4244警告（数据类型转换相关）

### 4.4 Lua代码规范
- 遵循Lua语言的最佳实践
- 函数和变量命名清晰明了
- 使用适当的注释说明代码逻辑
- 避免全局变量污染
- 合理组织模块结构

## 5. 版本控制

### 5.1 Git使用规范
- 使用Git进行版本控制
- 遵循`.gitignore`文件中的忽略规则
- 定期提交代码，保持提交频率
- 提交前确保代码能够编译通过
- 避免提交二进制文件和生成文件

### 5.2 分支管理
- **master**：主分支，保持稳定可发布状态
- **develop**：开发分支，日常开发工作在此分支进行
- **feature/xxx**：功能分支，开发新功能时创建
- **fix/xxx**：修复分支，修复bug时创建
- **release/xxx**：发布分支，准备发布新版本时创建

### 5.3 提交消息格式
提交消息应简洁明了，包含以下信息：
- 类型：如feat（新功能）、fix（修复bug）、docs（文档）、style（格式）、refactor（重构）、test（测试）、chore（构建过程或辅助工具变动）
- 范围：影响的模块或文件
- 描述：简明扼要的变更描述

例如：`feat(login): 添加第三方登录功能`

## 6. 编译流程

### 6.1 编译环境准备
- 确保Visual Studio 2015已正确安装
- 确认所有依赖库已正确配置
- 检查项目配置是否统一（使用v140工具集）

### 6.2 编译方法

#### 6.2.1 使用Visual Studio GUI
1. 打开解决方案：`client/FireClient/FireClient.sln`
2. 选择配置（Debug/Release）和平台（Win32）
3. 点击"生成"菜单，选择"批生成"，勾选需要编译的项目
4. 点击"生成"按钮开始编译

#### 6.2.2 使用自动化脚本
使用项目根目录下的`build_mt3.ps1`脚本进行自动化编译：
```powershell
cd "e:\梦幻西游MG原版源码"
.uild_mt3.ps1 -Configuration Release
```

### 6.3 编译顺序依赖
项目的编译顺序应遵循以下层次结构：
1. 基础库（liblua、libBox2D、pcre等）
2. ANGLE组件
3. 引擎核心（libcocos2d、libSpine）
4. 游戏框架（cegui、platform、cauthc、ljfm）
5. 游戏引擎（engine）
6. 应用程序（FireClient、MT3）

### 6.4 编译配置

所有项目必须统一使用以下设置以避免链接错误：

**通用配置**
- **PlatformToolset**：v140（Visual Studio 2015）
- **WindowsTargetPlatformVersion**：10.0.19041.0（或已安装的最新版本）
- **CharacterSet**：Unicode

**Release配置**
```xml
<RuntimeLibrary>MultiThreadedDLL</RuntimeLibrary>
<Optimization>MaxSpeed</Optimization>
<InlineFunctionExpansion>AnySuitable</InlineFunctionExpansion>
<AdditionalDependencies>legacy_stdio_definitions.lib;%(AdditionalDependencies)</AdditionalDependencies>
<IgnoreSpecificDefaultLibraries>libcmt.lib;libcmtd.lib;msvcrt.lib;msvcrtd.lib</IgnoreSpecificDefaultLibraries>
<AdditionalOptions>/DYNAMICBASE:NO /GS- %(AdditionalOptions)</AdditionalOptions>
```

**Debug配置**
```xml
<RuntimeLibrary>MultiThreadedDebugDLL</RuntimeLibrary>
<Optimization>Disabled</Optimization>
<BasicRuntimeChecks>EnableFastChecks</BasicRuntimeChecks>
<AdditionalDependencies>legacy_stdio_definitions.lib;%(AdditionalDependencies)</AdditionalDependencies>
<IgnoreSpecificDefaultLibraries>libcmt.lib;libcmtd.lib;msvcrt.lib;msvcrtd.lib</IgnoreSpecificDefaultLibraries>
<AdditionalOptions>/DYNAMICBASE:NO /GS- %(AdditionalOptions)</AdditionalOptions>
```

**关键链接器选项说明**
- `/DYNAMICBASE:NO` - 禁用地址空间布局随机化（ASLR），解决兼容性问题
- `/GS-` - 禁用缓冲区安全检查，解决`@__security_check_cookie@4`符号未找到问题
- `legacy_stdio_definitions.lib` - 提供VS2015新C运行时库所需的兼容性符号
- `IgnoreSpecificDefaultLibraries` - 避免多个运行时库版本冲突

参考：[`client/MT3Win32App/mt3.win32.vcxproj`](client/MT3Win32App/mt3.win32.vcxproj:81) 和 [`client/MT3Win32App/mt3.win32.vcxproj`](client/MT3Win32App/mt3.win32.vcxproj:121)

## 7. 开发流程

### 7.1 开发周期
- 遵循敏捷开发流程
- 迭代周期为2-4周
- 每个迭代包含计划、开发、测试、回顾等阶段

### 7.2 任务分配
- 使用项目管理工具（如Jira、Trello）分配任务
- 明确任务负责人和截止日期
- 定期更新任务状态

### 7.3 代码审查
- 所有代码提交前必须经过代码审查
- 使用Pull Request或Merge Request进行代码审查
- 审查内容包括代码质量、功能实现、性能优化等

### 7.4 文档编写
- 开发新功能时应同步编写相关文档
- 文档应放在`doc/`目录下
- 文档格式统一使用Markdown

## 8. 测试与质量保证

### 8.1 测试策略
- **单元测试**：为关键功能模块编写单元测试
- **集成测试**：测试模块间的交互
- **性能测试**：测试游戏性能，包括FPS、内存使用、加载速度等
- **兼容性测试**：测试在不同平台上的兼容性

### 8.2 测试工具
- 使用Visual Studio内置的测试工具进行单元测试
- 使用第三方性能分析工具（如Intel VTune）进行性能测试
- 使用自动化测试框架进行回归测试

### 8.3 缺陷管理
- 使用缺陷管理系统（如Jira、Bugzilla）跟踪缺陷
- 每个缺陷应包含详细的复现步骤、预期结果和实际结果
- 定期召开缺陷回顾会议

## 9. 发布流程

### 9.1 版本管理
- 遵循语义化版本号规范（Major.Minor.Patch）
- 每个版本应有明确的版本号和发布说明
- 重要版本应进行归档备份

### 9.2 打包流程
- 使用自动化脚本进行打包
- 确保所有依赖库和资源文件都已正确包含
- 进行签名和加密（如需要）

### 9.3 发布检查清单
- [ ] 所有功能测试通过
- [ ] 性能测试通过
- [ ] 安全性测试通过
- [ ] 文档更新完毕
- [ ] 版本号已更新
- [ ] 发布说明已编写
- [ ] 备份已完成

## 10. 常见问题与解决方案

### 10.1 编译问题

#### 10.1.1 多重定义符号错误（LNK1169/LNK2005）
**症状**：
```
错误 LNK2005: __crt_debugger_hook 已经在 MSVCRT.lib(utility_desktop.obj) 中定义
错误 LNK1169: 找到一个或多个多重定义的符号
```

**根本原因**：
- 不同库使用了不同版本的C运行时库（CRT）
- VS2015引入了新的Universal C Runtime (UCRT)，与旧版MSVCRT.lib不兼容
- 部分依赖库使用旧版VS编译，导致符号冲突

**解决方法**：
1. 确保所有项目使用统一的运行时库设置：
   - Release：`MultiThreadedDLL` (/MD)
   - Debug：`MultiThreadedDebugDLL` (/MDd)
2. 在链接器设置中添加：
   ```xml
   <IgnoreSpecificDefaultLibraries>libcmt.lib;libcmtd.lib;msvcrt.lib;msvcrtd.lib</IgnoreSpecificDefaultLibraries>
   ```
3. 使用VS2015重新编译所有第三方依赖库

详细说明参考：[`doc/MT3项目编译问题分析与解决方案.md`](doc/MT3项目编译问题分析与解决方案.md:42)

#### 10.1.2 无法解析的外部符号（LNK2001）
**症状**：
```
freetype.lib(sfnt.obj) : error LNK2001: 无法解析的外部符号 @__security_check_cookie@4
zlib.lib(trees.obj) : error LNK2001: 无法解析的外部符号 @__security_check_cookie@4
legacy_stdio_definitions.lib : 多个LNK2001无法解析的外部符号
```

**根本原因**：
- Visual Studio缓冲区安全检查功能（Buffer Security Check）在不同版本间实现不同
- 旧版本编译的库包含`@__security_check_cookie@4`符号引用，但新版VS2015无法提供

**解决方法**：
在项目的链接器选项中添加：
```xml
<AdditionalOptions>/DYNAMICBASE:NO /GS- %(AdditionalOptions)</AdditionalOptions>
```
- `/DYNAMICBASE:NO` - 禁用动态基址（ASLR）
- `/GS-` - 禁用安全检查功能

**已实施**：该修复已应用于 [`mt3.win32.vcxproj`](client/MT3Win32App/mt3.win32.vcxproj:81)

#### 10.1.3 缺少DLL文件
**症状**：运行程序时提示缺少`MSVCP140.dll`或`VCRUNTIME140.dll`

**解决方法**：
1. 下载并安装 Visual C++ Redistributable for Visual Studio 2015
2. 下载地址：https://www.microsoft.com/zh-cn/download/details.aspx?id=48145
3. 选择 `vc_redist.x86.exe`（32位版本）

#### 10.1.4 MSBuild未找到
**症状**：运行批处理脚本时提示找不到MSBuild.exe

**解决方法**：
1. 确认已安装Visual Studio 2015完整版
2. 或单独安装Build Tools for Visual Studio 2015
3. 验证MSBuild路径：`C:\Program Files (x86)\MSBuild\14.0\Bin\MSBuild.exe`
4. 将MSBuild路径添加到系统PATH环境变量

### 10.2 运行时问题

#### 10.2.1 程序崩溃
**症状**：程序启动后立即崩溃或在特定操作后崩溃
**解决方法**：使用调试器分析崩溃原因，检查内存使用情况，查看日志文件

#### 10.2.2 渲染异常
**症状**：游戏画面显示异常，如黑屏、花屏、纹理错误等
**解决方法**：检查OpenGL驱动程序，验证资源文件是否正确加载，检查渲染代码

### 10.3 资源管理问题

#### 10.3.1 资源加载失败
**症状**：游戏无法加载特定资源，显示错误信息
**解决方法**：检查资源文件路径是否正确，验证资源文件格式是否支持，检查资源文件完整性

## 11. 附录

### 11.1 项目配置模板
提供常用项目配置的XML模板，确保所有项目使用统一的设置。

### 11.2 常用命令和脚本
收集项目开发中常用的命令和脚本，提高开发效率。

### 11.3 第三方库文档链接

**核心引擎**
- Cocos2d-x 2.0.1: http://www.cocos2d-x.org/
- Cocos2d-x API文档: http://www.cocos2d-x.org/reference/native-cpp/V2.0.1/index.html

**脚本与绑定**
- Lua 5.1: https://www.lua.org/manual/5.1/
- tolua++: http://www.codenix.com/~tolua/

**UI与渲染**
- CEGUI: http://cegui.org.uk/
- FreeType: https://www.freetype.org/

**物理引擎**
- Box2D: https://box2d.org/
- Chipmunk: http://chipmunk-physics.net/

**图像处理**
- libpng: http://www.libpng.org/
- libjpeg: http://libjpeg.sourceforge.net/
- SILLY: http://www.cegui.org.uk/wiki/SILLY

**音频**
- Ogg Vorbis: https://xiph.org/vorbis/
- libspeex: https://www.speex.org/

**网络与协议**
- libcurl: https://curl.se/libcurl/
- Protocol Buffers: https://developers.google.com/protocol-buffers

**工具库**
- PCRE (Perl Compatible Regular Expressions): https://www.pcre.org/
- zlib: https://www.zlib.net/

### 11.4 术语表

**引擎相关**
- **CCNode** - Cocos2d-x 2.0.1中的基础节点类（3.x版本改为Node）
- **CCSprite** - 精灵类，用于显示2D图像（3.x版本改为Sprite）
- **CCDirector** - 导演类，管理场景切换和游戏主循环（3.x版本改为Director）
- **CCLayer** - 层类，场景的容器（3.x版本改为Layer）
- **CCScene** - 场景类（3.x版本改为Scene）

**编译相关**
- **CRT** - C Runtime Library，C运行时库
- **UCRT** - Universal C Runtime，通用C运行时库（VS2015引入）
- **MSVCRT** - Microsoft Visual C++ Runtime，旧版C运行时库
- **v140** - Visual Studio 2015的平台工具集版本号
- **PlatformToolset** - 平台工具集，定义使用的编译器版本
- **ASLR** - Address Space Layout Randomization，地址空间布局随机化
- **Buffer Security Check** - 缓冲区安全检查，防止缓冲区溢出的编译器功能

**库与依赖**
- **ANGLE** - Almost Native Graphics Layer Engine，用于在Windows上运行OpenGL ES
- **CEGUI** - Crazy Eddie's GUI，游戏UI渲染库
- **SILLY** - Simple Image Loading LibrarY，图像加载库
- **tolua++** - Tool to integrate C/C++ code with Lua，C++到Lua的绑定工具
- **libSpine** - Spine骨骼动画运行时库
- **pcre** - Perl Compatible Regular Expressions，正则表达式库

**项目特定**
- **FireClient** - 游戏客户端静态库
- **ljfm** - 文件管理模块
- **cauthc** - 客户端认证库
- **engine** - 游戏核心引擎库

---

**文档版本**：1.1
**最后更新**：2025-10-12
**适用项目**：MT3游戏项目（基于Cocos2d-x 2.0.1）