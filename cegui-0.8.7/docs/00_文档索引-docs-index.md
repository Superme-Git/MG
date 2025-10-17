# CEGUI 0.8.7 文档索引 / Docs Index

> 依据《docs/00_全局文档编写规范.md》编排，集中归档 CEGUI 0.8.7 子项目的维护、架构、构建与依赖信息；所有章节均链接回 MT3 主仓文档，便于交叉查阅。

## 目录结构 Roadmap

- [✅ 01_技术维护记录-maintenance-notes.md](./01_技术维护记录-maintenance-notes.md)  
  记录版本里程碑、缺陷修复、质量基线与风险提示；同步引用主库《docs/12-特殊技术专题.md》《docs/android/ARM64_libjpeg_build_guide.md》中的检查表。
- [✅ 02_架构分析报告-architecture-review.md](./02_架构分析报告-architecture-review.md)  
  覆盖渲染管线、资源系统、输入事件、OpenHarmony 适配以及与 cocos2d-x 的交互，参考《docs/toolchains/CEGUI技术集成指南.md》。
- [✅ 03_构建与编译指南-build-and-compile.md](./03_构建与编译指南-build-and-compile.md)  
  对 Windows / Linux / Android / OpenHarmony 编译流程进行详细拆解，补充 `build/android-build.py`、`mt3_build.bat`、`docs/09-第三方库编译指南.md` 中的关键参数与排错路径。
- [✅ 04_依赖与版本管理-dependency-tracking.md](./04_依赖与版本管理-dependency-tracking.md)  
  统计第三方库版本矩阵、许可证要求、ARM64 libjpeg 补丁，并提供升级策略。
- `assets/`（可选）：存放架构图、流程图等资源；如需新增，请遵循 UTF-8 命名和索引补录原则。

## 使用指引 Usage

1. **变更同步**：调整构建脚本或引入新依赖时，务必先更新对应专题，再回写索引。
2. **编码规范**：所有 Markdown 文件采用 UTF-8，无 BOM；章节标题使用中英双语描述。
3. **引用策略**：跨文档引用使用相对路径，优先指向 MT3 主仓已发布的权威文档。
4. **提交说明**：建议使用 `docs(cegui): ...` 格式，并在 MR/PR 中附带构建日志或验证结果。

如需扩展章节，请保持“编号_中文-英文.md”的命名方式，避免与主仓现有文档重复。
