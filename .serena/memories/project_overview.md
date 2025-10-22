# MT3项目概览

## 项目基本信息
- **项目名称**: 梦幻西游MG (MT3 Game Client)
- **项目类型**: 2D MMORPG游戏客户端
- **开发语言**: C++ / Lua
- **游戏引擎**: Cocos2d-x 2.0-rc2-x-2.0.1
- **目标平台**: Windows (Win32)
- **开发年代**: 2016年左右
- **C++标准**: C++03/C++11混合（主要是C++03）
- **唯一支持的编译器**: Visual Studio 2013 (v120)
- **不支持版本**: Visual Studio 2015 (v140) 及更高版本 - 运行时崩溃

## 核心技术栈
- **核心语言**: C++ (引擎和底层)
- **脚本语言**: Lua 5.1 (游戏逻辑)
- **图形API**: OpenGL ES 2.0 (通过ANGLE在Windows上模拟)
- **音频引擎**: CocosDenshion + FMOD
- **UI框架**: CEGUI (Crazy Eddie's GUI)
- **网络**: libcurl (HTTP/HTTPS通信)
- **Lua-C++绑定**: tolua++ 1.0.93

## 代码规模
- 总代码量: 约96,000+行
- C++代码: 约66,000行
- Lua脚本: 约30,000行

## 主要模块说明
1. **client/MT3Win32App**: Windows主程序入口
2. **client/FireClient**: 游戏逻辑核心(未在当前项目中可见，可能是预编译库)
3. **engine/**: 游戏引擎扩展层（Nuclear引擎）
4. **cocos2d-2.0-rc2-x-2.0.1/**: Cocos2d-x图形引擎
5. **common/platform**: 跨平台抽象层
6. **common/lua**: Lua脚本引擎
7. **common/ljfm**: 文件管理库
8. **common/cauthc**: 认证库