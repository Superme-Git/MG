# MT3 项目统一入口 README

> 本文件为唯一访问入口，导航到权威索引与主线编译文档，统一跨平台协作与链接规范。

---

## 统一入口与索引

- 文档主索引（快捷）：[docs/INDEX.md](docs/INDEX.md)
- 文档权威索引（序号体系）：[docs/13-文档索引.md](docs/13-文档索引.md)

维护说明：所有新增或更新文档的交叉引用应指向“权威索引”与主线编译文档。

---

## 快速开始（15 分钟）

1) 先读快速路径：[docs/workflows/MT3编译快速指南.md](docs/workflows/MT3编译快速指南.md)
2) 完整编译流程：权威版：[docs/06-编译完整指南.md](docs/06-编译完整指南.md)
3) 执行计划与阶段门：[docs/workflows/00-编译步骤工作计划.md](docs/workflows/00-编译步骤工作计划.md)
4) 自检清单：[docs/编译前检查清单.md](docs/编译前检查清单.md)

---

## 编译环境与工具链

- 环境准备（VS2013 v120）：[docs/05-编译环境准备.md](docs/05-编译环境准备.md)
- 技术栈入口与统一规则：[docs/MT3_技术栈指引.md](docs/MT3_技术栈指引.md)

Android：

- 环境快速验证与一键编译：[docs/android/Android_工具链环境快速验证与一键编译指南.md](docs/android/Android_工具链环境快速验证与一键编译指南.md)
- 打包与安装技术要点与排错手册：[docs/android/Android_Ant_NDK_APK_打包与安装技术要点与排错手册.md](docs/android/Android_Ant_NDK_APK_打包与安装技术要点与排错手册.md)

Windows：

- Release 构建诊断：[docs/windows/MT3_Release_Build_Diagnostics.md](docs/windows/MT3_Release_Build_Diagnostics.md)

工具链专题：

- CEGUI 集成指南：[docs/toolchains/CEGUI技术集成指南.md](docs/toolchains/CEGUI技术集成指南.md)
- CRT 库冲突深度分析：[docs/CRT库冲突深度分析.md](docs/CRT库冲突深度分析.md)
- libEGL 链接错误修复说明：[docs/libEGL链接错误修复说明.md](docs/libEGL链接错误修复说明.md)
- Visual Studio 工具集兼容性报告（附录建议）：[docs/Visual Studio工具集兼容性技术报告.md](docs/Visual Studio工具集兼容性技术报告.md)

---

## 运行流程文档

- 编译指南（完整流程）：[docs/workflows/编译指南.md](docs/workflows/编译指南.md)
- 编译快速指南（快速路径）：[docs/workflows/MT3编译快速指南.md](docs/workflows/MT3编译快速指南.md)

---

## 归档与阶段性报告

- v140 相关文档归档目录：[docs/archive/v140相关文档](docs/archive/v140相关文档)
- 临时/阶段性报告归档目录：[docs/archive/临时报告](docs/archive/临时报告)
- 升级日志归档目录：[docs/archive/upgrade_logs](docs/archive/upgrade_logs)

说明：归档内容仅供历史参考，不纳入主线编译与规则指导。

---

## 链接维护与校验

- 链接规范：统一使用相对路径，指向新结构文档；新增或更新文档的交叉引用统一指向 [docs/13-文档索引.md](docs/13-文档索引.md) 与主线编译文档。
- 校验脚本：PowerShell 链接批量修复与校验：[tools/scripts/update_doc_links.ps1](tools/scripts/update_doc_links.ps1)

使用示例：

```powershell
# 在项目根目录运行
powershell -ExecutionPolicy Bypass -File tools\scripts\update_doc_links.ps1 -DocsPath ".\docs" -FixInternalLinks -CheckExternalLinks
```

- Android 环境验证脚本入口（参考 docs/INDEX.md 中示例命令）。

---

## 贡献与维护规则

- 权威规则入口：[.claude/RULES.md](.claude/RULES.md)
- 重命名与合并方案：[docs/文档整合与重命名方案.md](docs/文档整合与重命名方案.md)
- 文档维护：每次“重命名/合并/归档”操作后同步更新 [docs/13-文档索引.md](docs/13-文档索引.md) 与 [docs/INDEX.md](docs/INDEX.md)

---

## 快速参考

- 主入口（本页）：[README.md](README.md)
- 文档权威索引：[docs/13-文档索引.md](docs/13-文档索引.md)
- 完整编译指南：[docs/06-编译完整指南.md](docs/06-编译完整指南.md)
- 执行计划：[docs/workflows/00-编译步骤工作计划.md](docs/workflows/00-编译步骤工作计划.md)
- 自检清单：[docs/编译前检查清单.md](docs/编译前检查清单.md)

---

维护状态：进行中（统一入口与索引已建立；持续修复内部/外部链接与归档同步）# MG
