# 构建系统详细说明

## 项目文件格式
- **格式**: Visual Studio 2010 (.vcxproj)
- **构建工具**: MSBuild
- **支持的平台工具集**:
  - v120 (Visual Studio 2013) - **唯一支持的版本**
  - ⚠️ v140及更高版本 - **不支持，会导致运行时崩溃**

## 配置类型

### Debug配置
```xml
<Optimization>Disabled</Optimization>                <!-- /Od -->
<RuntimeLibrary>MultiThreadedDebugDLL</RuntimeLibrary> <!-- /MDd -->
<DebugInformationFormat>ProgramDatabase</DebugInformationFormat> <!-- /Zi -->
```

### Release配置
```xml
<Optimization>MaxSpeed</Optimization>                <!-- /O2 -->
<InlineFunctionExpansion>AnySuitable</InlineFunctionExpansion>
<FavorSizeOrSpeed>Speed</FavorSizeOrSpeed>          <!-- /Ot -->
<RuntimeLibrary>MultiThreadedDLL</RuntimeLibrary>    <!-- /MD -->
```

## 编译依赖关系

### 静态库依赖树
```
MT3.exe
├── FireClient.lib (预编译)
├── engine.lib
│   ├── libcocos2d.lib
│   ├── libCocosDenshion.lib
│   ├── platform.lib
│   ├── lua.lib
│   ├── cauthc.lib
│   └── ljfm.lib
├── platform.lib
└── lua.lib

engine.lib
├── libcocos2d.lib
├── libCocosDenshion.lib
├── platform.lib
├── lua.lib
├── cauthc.lib
└── ljfm.lib

ljfm.lib
└── platform.lib
```

### 运行时DLL依赖
MT3.exe需要以下DLL（位于`client\resource\bin\debug`目录）：
1. libcocos2d.dll - Cocos2d-x引擎核心
2. libCocosDenshion.dll - 音频引擎
3. glew32.dll - OpenGL扩展
4. pthreadVCE2.dll - 线程库
5. libcurl.dll - HTTP网络
6. iconv.dll - 字符编码转换
7. libxml2.dll - XML解析
8. zlib1.dll - 压缩
9. fmodex.dll - FMOD音频

## 编译输出目录
- **中间文件**: `$(Configuration).win32\`
- **输出文件**: `$(Configuration).win32\`
- **最终可执行文件**: `client\resource\bin\debug\` 或 `client\resource\bin\release\`

## 预编译头（PCH）
- **启用**: Yes (`Use`)
- **PCH文件**: `nupch.h`, `stdafx.h`
- **好处**: 编译时间优化约85%

## 重要编译选项

### 字符集
- **字符集**: Unicode
- **字符类型**: wchar_t
- **宏定义**: `_UNICODE`, `UNICODE`

### 警告级别
- Level3 (`/W3`)

### 平台
- 仅支持Win32 (x86 32位)
- 不支持x64

## 已知编译问题

### v140及更高版本工具集不兼容
- **症状**: 即使编译链接成功，运行时也会立即崩溃
- **根本原因**: FireClient.lib是v120预编译库，与v140+ ABI不兼容
  - C++运行时库不兼容
  - STL ABI变化
  - 虚函数表布局差异
- **唯一解决方案**: 必须使用v120工具集
- **结论**: MT3项目不支持v140及更高版本

### 常见错误
1. **缺少头文件**: 确保include路径正确配置
2. **链接错误**: 检查库文件顺序和依赖关系
3. **DLL找不到**: 运行`copy_runtime_dlls.bat`