# MT3 项目开发规则 (Project Rules)

**版本**: 3.0  
**更新日期**: 2025-10-13  
**状态**: ✅ 生产可用  

---

## 🎯 项目定位

- **项目**: 梦幻西游MG (2D MMORPG客户端)
- **引擎**: Cocos2d-x 2.0-rc2-x-2.0.1 + Nuclear自研引擎
- **语言**: C++03/C++11 + Lua 5.1
- **规模**: ~96,000行代码 (C++ 66k + Lua 30k)

---

## 🚨 强制性技术约束 (不可违背)

### 1. 编译工具链

```xml
<!-- ✅ 正确配置 -->
<PlatformToolset>v120</PlatformToolset>
<CharacterSet>Unicode</CharacterSet>
<RuntimeLibrary>MultiThreadedDLL</RuntimeLibrary>  <!-- Release: /MD -->
<RuntimeLibrary>MultiThreadedDebugDLL</RuntimeLibrary>  <!-- Debug: /MDd -->
```

**原因**:
- FireClient.lib 为 v120 预编译库
- v140/v141/v142 与 v120 存在 ABI 不兼容
- 使用其他工具集会导致运行时崩溃

#### 权威引用
- 技术栈指引: [docs/MT3_技术栈指引.md](../docs/MT3_技术栈指引.md)
- 编译完整指南: [docs/06-编译完整指南.md](../docs/06-编译完整指南.md)
- 文档索引: [docs/13-文档索引.md](../docs/13-文档索引.md)

### 2. 依赖库版本 (不可更改)

| 库 | 版本 | 原因 |
|---|------|------|
| Cocos2d-x | 2.0-rc2-x-2.0.1 | Nuclear引擎深度耦合 |
| Lua | 5.1 | tolua++绑定依赖 |
| FMOD | Ex | CocosDenshion音频依赖 |

### 3. 编译顺序 (严格遵守)

```
阶段1: 基础库 (可并行)
├─ lua.lib
├─ platform.lib
├─ cauthc.lib
└─ ljfm.lib

阶段2: Cocos2d-x
├─ libcocos2d.dll
└─ libCocosDenshion.dll

阶段3: Nuclear引擎
└─ engine.lib

阶段4: 应用程序
├─ FireClient.lib (预编译)
└─ MT3.exe
```

---

## 📝 代码规范

### 命名约定

```cpp
// 接口类: I前缀 + PascalCase
class IEngine { };

// 普通类: PascalCase
class Sprite { };

// 成员变量: m_前缀 + camelCase
class Example {
private:
    int m_count;
    Sprite* m_pSprite;  // 指针加p前缀
};

// 函数: PascalCase
void SetLocation(const NuclearLocation& loc);

// 常量/枚举: 全大写 + 下划线
const int MAX_SPRITE_COUNT = 1000;
```

### 预编译头 (必须使用)

```cpp
// 每个.cpp文件第一行
#include "nupch.h"
```

**效果**: 编译时间减少 85%

---

## 🎮 Lua 脚本规范

```lua
-- ✅ 推荐: 缓存全局函数
local GetEngine = Nuclear.GetEngine
local engine = GetEngine()

-- ✅ 推荐: 复用对象
local point = Nuclear.NuclearPoint(0, 0)
function UpdatePos(x, y)
    point.x = x
    point.y = y
    engine:SetEngineSpriteLoc(handle, point)
end

-- ❌ 避免: 频繁创建对象
function UpdatePos(x, y)
    local point = Nuclear.NuclearPoint(x, y)  -- 每次创建,低效
    engine:SetEngineSpriteLoc(handle, point)
end
```

---

## 🚫 禁止事项

### 绝对禁止 ❌

1. **使用 v140/v141/v142 工具集**
   - 后果: 编译成功但运行崩溃

2. **修改 FireClient.lib 相关接口**
   - 后果: 链接错误或运行时崩溃

3. **升级 Cocos2d-x 版本**
   - 后果: 大量接口不兼容

4. **不使用预编译头**
   - 后果: 编译时间增加 10 倍以上
5. **使用 /FORCE 强制链接（如 /FORCE:MULTIPLE）**
    - 后果: 掩盖符号/CRT 冲突，导致不可预期运行时错误
6. **跨 CRT 边界分配/释放内存**
    - 后果: 堆损坏或崩溃；不同 CRT 版本的堆不可互通

---

## ✅ 推荐实践

### 1. 开发流程

```bash
# 1. 备份当前分支
git branch backup_$(date +%Y%m%d)

# 2. 增量编译 (仅编译修改的模块)
msbuild engine\\engine.win32.vcxproj /t:Build /p:PlatformToolset=v120

# 3. 测试验证
cd client\\resource\\bin\\debug
MT3.exe

# 4. 提交前完整编译
build_mt3_v120_complete.bat
```

### 2. 性能优化技巧

```cpp
// ✅ 使用对象池
ObjectPool<Effect> g_effectPool(100);
Effect* effect = g_effectPool.Allocate();
// ... 使用
g_effectPool.Free(effect);

// ✅ 脏标记避免重复计算
if (m_bTransformDirty) {
    RecalculateMatrix();
    m_bTransformDirty = false;
}
```

---

## 🔍 常见问题快速索引

| 问题 | 解决方案 | 文档位置 |
|-----|---------|---------|
| v140 编译崩溃 | 必须使用 v120 | [技术体系总结 §7.1](../docs/03-技术体系总结.md) |
| 链接错误 LNK2001 | 检查库链接顺序 | [编译指南 §常见问题](../docs/06-编译完整指南.md) |
| 缺少 DLL | 运行 copy_runtime_dlls.bat | [快速启动指南](../docs/01-快速启动指南.md) |
| Lua 脚本错误 | 检查绑定和语法 | [技术体系总结 §7.2](../docs/03-技术体系总结.md) |

---

## 📚 必读文档 (按顺序)

### 新人必读
1. [MT3项目完整技术体系总结](../docs/03-技术体系总结.md) - 15,000字核心文档
2. [编译指南](../docs/06-编译完整指南.md) - 完整编译流程
3. [项目概述](../docs/02-项目概述.md) - 架构设计
4. [快速启动指南](../docs/01-快速启动指南.md) - 5分钟上手

### 进阶阅读
5. [C++架构深度分析](../docs/04-架构深度分析.md)
6. [关键技术文档](../docs/11-关键技术文档.md)

---

## 🎓 学习路线

### 初级 (1-2周)
- 理解五层架构
- 学会编译和运行
- 阅读 Lua 脚本
- 修改简单逻辑

### 中级 (1-2月)
- 深入 C++ 代码
- 理解 Nuclear 引擎
- 掌握 Cocos2d-x API
- 性能调优技巧

### 高级 (3-6月)
- 架构设计模式
- 内存管理策略
- 渲染管线优化
- 跨模块重构

---

## 🤖 MCP 工具智能使用 (整合全局规则)

### 自动触发规则

1. **文档查询** → 自动使用 Context7
   - 触发: "Cocos2d-x 2.0 API", "Lua 5.1 特性"
   - 行为: 静默获取最新文档

2. **架构分析** → 自动进入 Sequential Thinking
   - 触发: "如何设计", "架构方案", "技术对比"
   - 行为: 多维度深度分析

3. **代码操作** → 自动使用 Serena
   - 触发: "分析代码", "查找符号", "重构"
   - 行为: 优先使用符号工具,避免全文件读取

---

## 🎯 核心原则 (KISS + YAGNI + SOLID + DRY)

### KISS (简单至上)
- 避免不必要的复杂性
- 代码清晰直观

### YAGNI (精益求精)
- 仅实现当前需要的功能
- 不过度设计

### SOLID
- **S** (单一职责): 每个类只负责一件事
- **O** (开放封闭): 扩展开放,修改封闭
- **L** (里氏替换): 子类可替换父类
- **I** (接口隔离): 接口专一
- **D** (依赖倒置): 依赖抽象而非实现

### DRY (杜绝重复)
- 消除代码重复
- 提升复用性

---

## ⚙️ 环境未满足时的处理流程（安装 v120、复验、阻断生产编译）

- 步骤 1：运行环境检测脚本
  - `cmd /c "_v120_check\detect_v120_and_build.bat"`
  - 结果判定：
    - 退出码 0：v120 工具集已成功激活并通过测试（允许进入编译流程）
    - 退出码 2：仅激活新版工具集，缺少 v120（必须安装 v120 后复验）
    - 退出码 1：未找到 VS 工具集或编译失败（必须先安装 VS/修复环境）
- 步骤 2：安装 v120 工具集
  - 参见 [docs/06-编译完整指南.md](../docs/06-编译完整指南.md:1) 的“附录A：v120 工具集安装指南”
- 步骤 3：复验与通行证
  - 完成安装后再次执行检测脚本，直到返回码为 0 方可进行生产编译
  - 未通过时：阻断生产编译，任何产物不得入库/发布
- 例外（仅限本地非生产验证）：
  - 可在不链接预编译 FireClient.lib 的独立模块上进行局部开发验证
  - 禁止将此类产物合入主分支或用于发布

审计与记录：
- 在 [docs/00-编译步骤工作计划.md](../docs/00-编译步骤工作计划.md:1) “产出与验收记录”中登记检测结果与安装操作

**文档维护**: 每季度审查
**问题反馈**: 直接修改文档并记录
**下次审查**: 2026-01-13

## 🧩 WinRT/ANGLE 示例项目处理原则（v120 主线）

- 主线编译仅针对 Win32；任何 WinRT/Store/ANGLE 示例项目一律不纳入主线解决方案的加载与编译。
- 原因：这些示例项目要求额外的 Windows Store/WinRT SDK 与工具链，破坏 v120 Win32 的一致性与可复现性。
- 执行：
  - 解决方案层：从 FireClient.sln 中移除或禁用 WinRT/ANGLE 示例项目的加载；仅保留 Win32 目标的项目。
  - 项目层：确保所有保留项目的 PlatformToolset 为 v120，字符集为 Unicode，运行时库为 /MD（Release）或 /MDd（Debug）。
- 交叉引用：详见 [docs/06-编译完整指南.md](../docs/06-编译完整指南.md:1) “环境准备/编译前检查”与“方法 B：手动编译”。

## 📄 日志与归档规范（UpgradeLog）

- 采集范围：Visual Studio 升级日志与项目加载失败日志（UpgradeLog.htm/UpgradeLog.html）。
- 编码统一：统一转存为 UTF-8（原始 UTF-16 作为输入源，不纳入主线）。
- 归档位置：docs/archive/upgrade_logs/（按日期与解决方案命名）。
- 流程：
  1) 完成 G0 环境检测并安装 v120 后，重新打开解决方案验证。若仍生成升级/加载错误日志，立即转存 UTF-8。
  2) 将日志文件归档并在索引中登记：更新 [docs/13-文档索引.md](../docs/13-文档索引.md:1) “归档说明”与“规则与维护”条目。
  3) 在 [docs/00-编译步骤工作计划.md](../docs/00-编译步骤工作计划.md:1) “产出与验收记录”中登记本次日志与处理结论。
- 禁止事项：
  - 禁止将未通过 G0 的日志保留在生产目录（例如 [client/FireClient/UpgradeLog.htm](../client/FireClient/UpgradeLog.htm:1)）。完成归档后应从解决方案/目录中移除该临时日志。
- 审计：
  - 每次归档更新需执行链接校验，确保索引有效并与主线规则一致。

## 📑 文本与文档编码规范
- 所有 Markdown/文本文件统一编码：UTF-8 无 BOM
- Windows 批处理/PowerShell 脚本：使用 ANSI 或 UTF-8 无 BOM，避免带 BOM 导致解析异常
- 日志与报告归档统一存放：docs/archive/ 下（UpgradeLog 详见下节），索引更新于 [docs/13-文档索引.md](../docs/13-文档索引.md)

## 🔧 Retarget 统一配置（Windows SDK 8.1）

- 所有 Win32 项目必须 Retarget 到 Windows SDK 8.1；不得使用 10.0 SDK 进行主线发布编译。
- 检查要点：
  - Project → Property → General → Windows SDK Version = 8.1
  - PlatformToolset = v120
  - CharacterSet = Unicode
  - RuntimeLibrary = MultiThreadedDLL（Release）/ MultiThreadedDebugDLL（Debug）
- 例外：仅限本地非生产验证，可在 10.0 SDK 下进行独立模块试编；产物不得入库或发布。
---

**相关文件**:
- 详细规则: [.claude/RULES.md](.claude/RULES.md)
- 技术栈指引: [docs/MT3_技术栈指引.md](../docs/MT3_技术栈指引.md)
- 编译完整指南: [docs/06-编译完整指南.md](../docs/06-编译完整指南.md)
- 文档索引: [docs/13-文档索引.md](../docs/13-文档索引.md)