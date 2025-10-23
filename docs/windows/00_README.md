# Windows PC 打包文档索引

> **MT3 Windows客户端打包完整文档体系**
> **版本**: v2.0 (2025-10-17)
> **架构原则**: KISS + DRY + SOLID

---

## 📚 文档导航

### 🚀 快速开始(5分钟上手)

**→ [01_快速开始.md](01_快速开始.md)**
- 3步完成EXE打包
- 常见问题Top5
- 最适合:首次使用或日常打包
*Keywords: quick start, overview*

---

### ✅ 操作手册(按使用频率)

#### 1. **打包前检查清单**
**→ [02_打包前检查清单.md](02_打包前检查清单.md)**
- 7步系统化检查
- 1分钟验证环境就绪
- 降低打包失败风险
- **何时使用**: 每次打包前必读
*Keywords: preflight, checklist*

#### 2. **环境配置指南**
**→ [03_环境配置指南.md](03_环境配置指南.md)**
- 从零配置打包环境
- Visual Studio/MSBuild/Windows SDK完整安装步骤
- 环境变量配置详解
- **何时使用**: 新机器初始化/环境迁移
*Keywords: environment setup, tooling*

#### 3. **问题诊断决策树**
**→ [05_问题诊断决策树.md](05_问题诊断决策树.md)**
- 3大场景:编译失败/启动崩溃/运行时错误
- 流程图式快速定位
- Top5常见原因覆盖80%问题
- **何时使用**: 出现任何异常时第一时间查阅
*Keywords: troubleshooting, diagnostics*

---

### 📖 技术参考(深度内容)

#### 4. **构建流程技术详解**
**→ [04_构建流程详解.md](04_构建流程详解.md)**
- MSBuild编译原理
- 平台工具集(v120/v140/v142)详解
- 依赖库链接机制
- **何时使用**: 需要定制构建流程/深度排查
*Keywords: build pipeline, internals*

#### 5. **完整排错手册**
**→ [06_完整排错手册.md](06_完整排错手册.md)**
- 500+行完整技术参考
- 覆盖所有已知问题
- 包含历史问题记录
- **何时使用**: 决策树未解决的复杂问题
*Keywords: knowledge base, troubleshooting reference*

---

### 📎 附录

#### 附录A: 目录结构说明
**→ [附录A_目录结构说明.md](附录A_目录结构说明.md)**
- 项目目录树
- 关键文件位置
- **何时使用**: 理解项目组织结构
*Keywords: directory map, structure*

#### 附录B: 跨平台工具集适配指南
**→ [附录B_跨平台工具集适配指南.md](附录B_跨平台工具集适配指南.md)**
- v120/v140/v142工具集切换
- CRT库兼容性处理
- **何时使用**: 升级VS版本或解决兼容性问题
*Keywords: toolset, compatibility*

---

### 🗂️ 归档文档

**→ [archive/](archive/)**
- v1.0版本历史文档
- 已整合为v2.0,仅作参考
*Keywords: legacy docs, history*

---

## 🎯 推荐阅读路线

### 路线1: 新手入门(首次使用)

```
1. [01_快速开始.md](01_快速开始.md) - 了解基本流程
   ↓
2. [03_环境配置指南.md](03_环境配置指南.md) - 配置环境
   ↓
3. [02_打包前检查清单.md](02_打包前检查清单.md) - 检查就绪
   ↓
4. 执行MSBuild编译
   ↓
5. [05_问题诊断决策树.md](05_问题诊断决策树.md) - 如遇问题查阅
```

**预计时间**: 30-60分钟完成首次打包

---

### 路线2: 熟手快速打包(日常使用)

```
1. [02_打包前检查清单.md](02_打包前检查清单.md) - 快速检查
   ↓
2. 执行MSBuild命令
   ↓
3. 如有问题查阅[05_问题诊断决策树.md](05_问题诊断决策树.md)
```

**预计时间**: 5-10分钟

---

### 路线3: 深度学习(理解原理)

```
1. [04_构建流程详解.md](04_构建流程详解.md) - 学习架构
   ↓
2. [06_完整排错手册.md](06_完整排错手册.md) - 技术细节
   ↓
3. 查看源码: *.vcxproj, *.props等
```

**预计时间**: 2-4小时

---

## 📊 文档体系架构

```
docs/windows/
│
├─ 00_README.md .................. 【本文档】总索引
│
├─ 入口层(5分钟上手)
│   └─ 01_快速开始.md ............ 3步打包流程
│
├─ 操作层(按使用频率)
│   ├─ 02_打包前检查清单.md ....... 系统化检查
│   ├─ 03_环境配置指南.md ......... 从零配置环境
│   └─ 05_问题诊断决策树.md ....... 快速故障定位
│
├─ 技术层(深度参考)
│   ├─ 04_构建流程详解.md ......... MSBuild完整流程
│   └─ 06_完整排错手册.md ......... 500行技术百科
│
├─ 附录层(补充资料)
│   ├─ 附录A_目录结构说明.md ...... 项目结构
│   └─ 附录B_跨平台工具集适配指南.md ... 工具集适配
│
└─ archive/ ...................... v1.0历史文档
```

---

## 🔧 核心工具与脚本

### MSBuild命令(推荐)

**位置**: 系统PATH或VS安装目录

**基本用法**:
```cmd
# Debug版本
"C:\Program Files (x86)\Microsoft Visual Studio 12.0\VC\bin\MSBuild.exe" ^
  client\MT3Win32App\mt3.win32.vcxproj ^
  /t:Rebuild ^
  /p:Configuration=Debug ^
  /p:Platform=Win32 ^
  /p:PlatformToolset=v120 ^
  /v:minimal ^
  /nologo ^
  /m

# Release版本
"C:\Program Files (x86)\Microsoft Visual Studio 12.0\VC\bin\MSBuild.exe" ^
  client\MT3Win32App\mt3.win32.vcxproj ^
  /t:Rebuild ^
  /p:Configuration=Release ^
  /p:Platform=Win32 ^
  /p:PlatformToolset=v120 ^
  /v:minimal ^
  /nologo ^
  /m
```

---

### 一键打包脚本

**位置**: `client/MT3Win32App/build_release.bat` (待创建)

**功能**:
- ✅ 自动环境检测
- ✅ 依赖库预编译
- ✅ 主程序编译
- ✅ 资源打包
- ✅ 日志记录

---

## 📋 核心配置文件

| 文件 | 作用 | 修改频率 |
|------|------|---------|
| [mt3.win32.vcxproj](../../client/MT3Win32App/mt3.win32.vcxproj) | 主项目配置 | 偶尔 |
| [platform.win32.vcxproj](../../common/platform/platform.win32.vcxproj) | 平台库配置 | 很少 |
| [*.props](../../client/MT3Win32App/) | 属性表配置 | 很少 |
| Version.h | 版本号定义 | 发版时 |
| Resource.rc | 资源文件 | 发版时 |

---

## ⚠️ 重要约束

### 平台限制

**当前项目仅支持 Win32 (x86)**

- ✅ **支持系统**: Windows 7/8/10/11 (32位和64位系统)
- ❌ **不支持**: ARM64 Windows设备

**验证方式**:
```cmd
wmic os get osarchitecture
```

---

### 工具版本要求

| 工具 | 版本 | 原因 |
|------|------|------|
| Visual Studio | 2013 | 提供v120工具集 |
| MSBuild | 12.0 | 编译引擎 |
| Windows SDK | 8.1 | 平台API |
| Platform Toolset | v120 | 项目指定 |

**版本对应关系**:
- v120 = Visual Studio 2013

---

## 🆘 获取帮助

### 问题未解决?

**步骤1**: 查阅诊断决策树
- [05_问题诊断决策树.md](05_问题诊断决策树.md)

**步骤2**: 查看完整排错手册
- [06_完整排错手册.md](06_完整排错手册.md)

**步骤3**: 收集诊断信息
```cmd
# 查看系统信息
wmic os get caption,osarchitecture,version
MSBuild /version

# 查看编译日志
type build_logs\windows_build_最新时间戳.log

# 检查依赖库
dumpbin /dependents client\MT3Win32App\Release.win32\MT3.exe
```

**步骤4**: 提交Issue
- 附带编译日志
- 描述复现步骤
- 说明系统和VS版本

---

## 📝 文档维护规范

### 命名规则

- **数字前缀**: 表示阅读优先级 (00-09)
- **附录**: 补充性内容,非核心流程
- **archive/**: 历史版本,v1.0前缀

### 链接规范

- **文档间引用**: 使用相对路径
  ```markdown
  [快速开始](01_快速开始.md)
  ```

- **代码文件引用**: 使用绝对路径+行号
  ```markdown
  [mt3.win32.vcxproj:42](../../client/MT3Win32App/mt3.win32.vcxproj#L42)
  ```

### 更新原则

1. **KISS**: 保持简洁,删除冗余
2. **DRY**: 避免重复,抽取公共内容
3. **SOLID-O**: 扩展时不破坏现有文档

---

## 🔄 版本历史

### v2.0 (2025-10-17) - 当前版本

**创建内容**:
- ✅ 建立完整文档体系结构
- ✅ 参照Android文档模式组织内容
- ✅ 添加Windows特定的技术说明
- ✅ 创建系统化检查清单和诊断流程

**文档数量**:
- 核心文档: 8个
- 附录文档: 2个

---

## 🌟 最佳实践建议

### 团队协作

- ✅ 统一使用MSBuild命令行编译
- ✅ 提交代码前确保本地编译通过
- ✅ 环境变更及时更新文档
- ✅ 新问题及时补充到诊断决策树

### 发布前检查清单

参考 [02_打包前检查清单.md](02_打包前检查清单.md):
- [ ] 版本号更新(Version.h)
- [ ] 资源文件更新(Resource.rc)
- [ ] 配置文件正确(Release模式)
- [ ] 依赖库完整
- [ ] 真机测试通过

---

## 📧 反馈与贡献

**文档反馈**: 项目Issues
**紧急问题**: 联系项目负责人
**文档贡献**: 欢迎提交PR改进文档

---

**文档维护者**: MT3项目组
**最后更新**: 2025-10-17
**下次审查**: 2026-01-17 (或重大变更时)

---

**感谢使用MT3 Windows打包文档!** 🚀

如有任何疑问,请优先查阅[01_快速开始.md](01_快速开始.md)和[05_问题诊断决策树.md](05_问题诊断决策树.md)。
