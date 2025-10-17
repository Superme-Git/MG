# MT3 项目文档入口 INDEX

> 统一入口页面。权威索引请使用：[./13-文档索引.md](./13-文档索引.md:1)。本页提供快速跳转与公共章节入口，便于 IDE 搜索与跨平台协作。

---

## 快速入口

- 总索引（权威）：[./13-文档索引.md](./13-文档索引.md:1)
- Android 快速验证与一键编译：[./android/Android_工具链环境快速验证与一键编译指南.md](./android/Android_工具链环境快速验证与一键编译指南.md:1)
- Android 打包与安装技术要点：[./android/Android_Ant_NDK_APK_打包与安装技术要点与排错手册.md](./android/Android_Ant_NDK_APK_打包与安装技术要点与排错手册.md:1)
- Windows/Release 构建诊断：[./windows/MT3_Release_Build_Diagnostics.md](./windows/MT3_Release_Build_Diagnostics.md:1)
- CEGUI 集成指南：[./toolchains/CEGUI技术集成指南.md](./toolchains/CEGUI技术集成指南.md:1)

---

## Android 公共章节

- 环境统一章节：[./common/Android_Env.md](./common/Android_Env.md:1)
- ABI/平台版本一致性排查：[./common/Android_ABI_Troubleshooting.md](./common/Android_ABI_Troubleshooting.md:1)

说明：Android 环境验证与编译步骤请按上述公共章节执行；如需批量链接检查与修复，参考脚本：[tools/scripts/update_doc_links.ps1](tools/scripts/update_doc_links.ps1:1)。

## 运行流程类文档

- 编译指南（完整流程）：[./workflows/编译指南.md](./workflows/编译指南.md:1)
- 编译快速指南（快速路径）：[./workflows/MT3编译快速指南.md](./workflows/MT3编译快速指南.md:1)
- 编译步骤工作计划（流程计划）：[./workflows/00-编译步骤工作计划.md](./workflows/00-编译步骤工作计划.md:1)
---

## 归档与阶段性报告

- v140 相关文档归档：[./archive/v140相关文档](./archive/v140相关文档:1)
- 临时报告归档：[./archive/临时报告](./archive/临时报告:1)

---

## 维护规则

- 新增或更新文档的交叉引用，统一指向：[./13-文档索引.md](./13-文档索引.md:1)
- 避免长篇绝对路径命令，优先使用参数化或统一验证脚本
- Android 文档涉及环境/ABI/安装验证内容，统一引用“公共章节”
- 如需重组分类目录与重命名，记录映射于：[./文档整合与重命名方案.md](./文档整合与重命名方案.md:1)