# MT3 项目 MCP 工具配置建议

**版本**: 1.0  
**更新日期**: 2025-10-13  
**目的**: 为 MT3 项目推荐最合适的 MCP 工具配置

---

## 📋 目录

1. [MCP 工具概述](#mcp-工具概述)
2. [推荐工具配置](#推荐工具配置)
3. [场景化工具使用](#场景化工具使用)
4. [工具使用最佳实践](#工具使用最佳实践)
5. [不推荐的工具](#不推荐的工具)

---

## 🔧 MCP 工具概述

MCP (Model Context Protocol) 工具可以扩展 AI 助手的能力,为特定任务提供专业支持。

### 当前可用的 MCP 工具

| 工具 | 类型 | 适用性 | 推荐度 |
|------|------|--------|--------|
| **Serena** | 代码分析 | ⭐⭐⭐⭐⭐ | 强烈推荐 |
| **Sequential Thinking** | 深度分析 | ⭐⭐⭐⭐⭐ | 强烈推荐 |
| **Context7** | 文档查询 | ⭐⭐⭐ | 推荐 |
| **Filesystem** | 文件操作 | ⭐⭐⭐⭐ | 推荐 |
| **GitHub** | 代码托管 | ⭐⭐⭐ | 可选 |
| **Brave Search** | 网络搜索 | ⭐⭐ | 不推荐 |
| **Slack/Reddit** | 社交工具 | ⭐ | 不推荐 |

---

## ⭐ 推荐工具配置

### 1. Serena (代码符号分析工具) - 强烈推荐 ⭐⭐⭐⭐⭐

**为什么推荐**:
- MT3 项目有 ~96,000 行 C++ 代码
- 复杂的五层架构需要精确的符号导航
- 可以快速定位类/函数/变量的定义和引用
- 避免阅读整个文件,提高效率

**核心功能**:
- `get_symbols_overview` - 获取文件符号概览
- `find_symbol` - 查找特定符号
- `find_referencing_symbols` - 查找符号引用
- `replace_symbol_body` - 精确替换符号体

**使用场景**:
```
场景1: 理解 Nuclear 引擎架构
→ 使用 get_symbols_overview 查看 engine/ 目录结构
→ 使用 find_symbol 定位 IEngine 接口
→ 使用 find_referencing_symbols 查看谁在使用 IEngine

场景2: 重构 Sprite 类
→ 使用 find_symbol 获取 Sprite 类定义
→ 使用 find_referencing_symbols 查找所有引用
→ 使用 replace_symbol_body 更新实现
```

**配置示例**:
```json
{
  "mcpServers": {
    "serena": {
      "command": "npx",
      "args": ["-y", "@serena/mcp-server"]
    }
  }
}
```

---

### 2. Sequential Thinking (深度思考工具) - 强烈推荐 ⭐⭐⭐⭐⭐

**为什么推荐**:
- 编译问题排查需要多步推理
- 架构设计需要深度分析
- v120 vs v140 等复杂技术决策需要详细论证

**核心功能**:
- 多步骤推理分析
- 可以修正和回溯思考过程
- 生成假设并验证
- 最终给出单一正确答案

**使用场景**:
```
场景1: 排查 LNK2001 链接错误
→ 分析可能的原因 (工具集/运行时库/链接顺序)
→ 逐步验证每个假设
→ 定位根本原因
→ 提供解决方案

场景2: 设计新功能架构
→ 分析现有架构约束
→ 评估多个设计方案
→ 权衡利弊
→ 推荐最佳方案
```

**配置示例**:
```json
{
  "mcpServers": {
    "sequentialthinking": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-sequential-thinking"]
    }
  }
}
```

---

### 3. Context7 (文档查询工具) - 推荐 ⭐⭐⭐

**为什么推荐**:
- 虽然项目使用老版本 (Cocos2d-x 2.0, Lua 5.1)
- 但仍可查询基础 API 和概念文档
- 对于不熟悉 Cocos2d-x 的开发者有帮助

**核心功能**:
- `resolve-library-id` - 解析库 ID
- `get-library-docs` - 获取库文档

**使用场景**:
```
场景1: 查询 Cocos2d-x API
→ 查询 CCSprite 的基础用法
→ 查询 CCLayer 的生命周期
→ 查询 CCAction 动画系统

场景2: 查询 Lua 5.1 特性
→ 查询 coroutine 协程用法
→ 查询 table 操作方法
→ 查询 string 库函数
```

**注意事项**:
- 可能无法找到 Cocos2d-x 2.0 的精确文档
- 建议主要参考项目自带文档
- 可作为补充参考

**配置示例**:
```json
{
  "mcpServers": {
    "context7": {
      "command": "npx",
      "args": ["-y", "@upstash/context7-mcp"]
    }
  }
}
```

---

### 4. Filesystem (文件操作工具) - 推荐 ⭐⭐⭐⭐

**为什么推荐**:
- 项目文件众多,需要高效的文件浏览
- 可以快速查看目录结构
- 支持批量读取文件

**核心功能**:
- `list_directory` - 列出目录
- `read_text_file` - 读取文件
- `read_multiple_files` - 批量读取
- `search_files` - 搜索文件

**使用场景**:
```
场景1: 探索项目结构
→ list_directory("engine/") 查看引擎模块
→ list_directory("client/") 查看客户端结构

场景2: 批量分析配置文件
→ read_multiple_files([
    "client/MT3Win32App/mt3.win32.vcxproj",
    "engine/engine.win32.vcxproj"
  ])
→ 检查工具集版本是否一致
```

**配置示例**:
```json
{
  "mcpServers": {
    "filesystem": {
      "command": "npx",
      "args": [
        "-y",
        "@modelcontextprotocol/server-filesystem",
        "e:/MT3"
      ]
    }
  }
}
```

---

### 5. GitHub (代码托管工具) - 可选 ⭐⭐⭐

**适用场景**:
- 如果项目托管在 GitHub
- 需要自动创建 PR/Issue
- 需要查看提交历史

**核心功能**:
- `create_pull_request` - 创建 PR
- `create_issue` - 创建 Issue
- `get_file_contents` - 获取文件内容
- `list_commits` - 查看提交历史

**配置示例**:
```json
{
  "mcpServers": {
    "github": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-github"],
      "env": {
        "GITHUB_PERSONAL_ACCESS_TOKEN": "ghp_your_token_here"
      }
    }
  }
}
```

---

## 🎯 场景化工具使用

### 场景1: 理解项目架构 (新人入职)

**推荐工具链**:
```
Filesystem (探索目录结构)
  ↓
Serena (分析核心类和接口)
  ↓
Sequential Thinking (总结架构设计)
```

**操作流程**:
1. 使用 Filesystem 列出项目目录结构
2. 使用 Serena 获取关键类的符号概览
3. 使用 Sequential Thinking 分析五层架构的设计意图

---

### 场景2: 排查编译错误

**推荐工具链**:
```
Sequential Thinking (分析错误原因)
  ↓
Filesystem (检查配置文件)
  ↓
Serena (定位代码问题)
```

**操作流程**:
1. 使用 Sequential Thinking 多步推理分析错误原因
2. 使用 Filesystem 检查 .vcxproj 配置
3. 使用 Serena 查找符号定义,确认 ABI 兼容性

---

### 场景3: 重构代码

**推荐工具链**:
```
Serena (分析代码结构)
  ↓
Sequential Thinking (设计重构方案)
  ↓
Serena (执行重构)
```

**操作流程**:
1. 使用 Serena 查找需要重构的符号及其引用
2. 使用 Sequential Thinking 评估重构影响和风险
3. 使用 Serena 精确替换符号体

---

### 场景4: 学习新技术 (Cocos2d-x/Lua)

**推荐工具链**:
```
Context7 (查询基础文档)
  ↓
Filesystem (阅读项目示例)
  ↓
Serena (分析实际用法)
```

**操作流程**:
1. 使用 Context7 查询 Cocos2d-x 基础概念
2. 使用 Filesystem 读取项目中的示例代码
3. 使用 Serena 分析项目中的实际用法

---

## ✅ 工具使用最佳实践

### 原则1: 优先使用符号工具

```
❌ 错误做法:
→ 使用 Filesystem 读取整个文件 (1000 行)
→ 浪费 Token,效率低

✅ 正确做法:
→ 使用 Serena 的 get_symbols_overview 获取概览
→ 使用 find_symbol 精确读取需要的符号 (50 行)
→ 节省 95% 的 Token
```

### 原则2: 分步执行,避免并行

```
❌ 错误做法:
→ 同时调用 3 个 Serena 工具
→ 可能导致依赖信息缺失

✅ 正确做法:
→ 先 get_symbols_overview 了解结构
→ 再 find_symbol 查找具体符号
→ 最后 find_referencing_symbols 分析依赖
```

### 原则3: 静默执行,无需征询

```
✅ 自动触发规则 (已在 RULES.md 中配置):
- 提到 "分析代码" → 自动使用 Serena
- 提到 "如何设计" → 自动使用 Sequential Thinking
- 提到 "Cocos2d-x API" → 自动使用 Context7
```

### 原则4: 工具失败时优雅降级

```
如果 Context7 无法获取文档:
→ 不要终止任务
→ 使用本地文档 (docs/ 目录)
→ 基于训练数据给出答案
→ 提示用户查阅项目文档
```

---

## ❌ 不推荐的工具

### 1. Brave Search - 不推荐 ⭐⭐

**原因**:
- MT3 使用的是老旧技术栈 (Cocos2d-x 2.0, VS2013)
- 网络搜索主要返回最新技术文档,不适用
- 项目问题大多是内部架构问题,搜索引擎帮助不大

**替代方案**:
- 优先阅读项目自带文档 (`docs/` 目录)
- 使用 Serena 分析项目内部实现
- 使用 Sequential Thinking 进行技术推理

---

### 2. Slack / Reddit - 不推荐 ⭐

**原因**:
- MT3 是内部项目,不适合在公开平台讨论
- 技术栈过于老旧,社区活跃度低
- 大多数问题可以通过代码分析解决

**替代方案**:
- 团队内部沟通
- 查阅项目文档
- 使用 Sequential Thinking 进行技术分析

---

## 📋 推荐配置清单

### 最小配置 (必须)

```json
{
  "mcpServers": {
    "serena": {
      "command": "npx",
      "args": ["-y", "@serena/mcp-server"]
    },
    "sequentialthinking": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-sequential-thinking"]
    }
  }
}
```

### 标准配置 (推荐)

```json
{
  "mcpServers": {
    "serena": {
      "command": "npx",
      "args": ["-y", "@serena/mcp-server"]
    },
    "sequentialthinking": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-sequential-thinking"]
    },
    "filesystem": {
      "command": "npx",
      "args": [
        "-y",
        "@modelcontextprotocol/server-filesystem",
        "e:/MT3"
      ]
    },
    "context7": {
      "command": "npx",
      "args": ["-y", "@upstash/context7-mcp"]
    }
  }
}
```

### 完整配置 (可选)

在标准配置基础上添加:

```json
{
  "mcpServers": {
    // ... 标准配置
    "github": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-github"],
      "env": {
        "GITHUB_PERSONAL_ACCESS_TOKEN": "ghp_your_token_here"
      }
    }
  }
}
```

---

## 🚀 快速开始

### 步骤1: 配置 MCP 工具

编辑 `.claude/settings.local.json` 或 Claude Desktop 配置文件:

```json
{
  "mcpServers": {
    "serena": {
      "command": "npx",
      "args": ["-y", "@serena/mcp-server"]
    },
    "sequentialthinking": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-sequential-thinking"]
    },
    "filesystem": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-filesystem", "e:/MT3"]
    }
  }
}
```

### 步骤2: 重启 Claude

配置修改后需要重启 Claude 以加载 MCP 工具。

### 步骤3: 验证配置

```
用户: "列出 engine/ 目录的文件"
AI: (自动使用 filesystem 工具)

用户: "分析 IEngine 接口"
AI: (自动使用 serena 工具)

用户: "如何解决 v140 编译崩溃问题"
AI: (自动使用 sequential thinking 工具)
```

---

## 📊 工具使用统计建议

### 预期使用频率

| 工具 | 日均使用次数 | 典型场景 |
|------|------------|----------|
| **Serena** | 20-30 次 | 代码分析、重构、符号查找 |
| **Sequential Thinking** | 5-10 次 | 问题排查、架构设计、技术决策 |
| **Filesystem** | 10-15 次 | 目录浏览、配置检查、批量读取 |
| **Context7** | 3-5 次 | 文档查询、API 参考 |
| **GitHub** | 1-2 次 | 创建 PR、查看历史 |

### 性能优化建议

1. **优先使用 Serena 符号工具**
   - 避免读取整个文件
   - 可节省 80-90% Token

2. **合理使用 Sequential Thinking**
   - 仅在复杂问题时使用
   - 可设置 totalThoughts 限制步数

3. **避免重复查询**
   - 利用上下文记忆
   - 不要重复读取相同内容

---

## 📝 相关文档

- [项目规则](RULES.md) - 核心开发规则 (包含 MCP 自动触发规则)
- [编译流程优化指南](BUILD_GUIDE.md) - 详细编译流程
- [编译检查清单](BUILD_CHECKLIST.md) - 快速检查表

---

**文档维护**: 每季度审查  
**下次审查**: 2026-01-13