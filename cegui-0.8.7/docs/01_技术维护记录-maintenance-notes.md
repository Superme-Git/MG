# CEGUI 0.8.7 技术维护记录 / Maintenance Notes

- **最后更新 / Last Updated**：2025-10-17
- **适用版本 / Target Version**：CEGUI 0.8.7（嵌入 MT3 cocos2d-x 分支）
- **维护责任人 / Maintainers**：引擎平台组（Engine Platform Team）

---

## 1. 版本历程 Version Timeline

| 日期 Date | 动作 Action | 说明 Notes |
|----------|-------------|-----------|
| 2025-09-22 | 初始引入 Initial import | 从上游 `v0-8` 分支同步源码与 `datafiles`，比对《docs/toolchains/CEGUI技术集成指南.md:1》梳理旧版差异。 |
| 2025-10-05 | 平台裁剪 Platform trimming | 依据《docs/12-特殊技术专题.md:1》建议，仅保留 `OpenGL3/OpenGLES2` 渲染器并移除 Direct3D、NullRenderer。 |
| 2025-10-12 | OHOS 适配 OHOS adaptation | 整合 `docs/cegui-cocos-ohos-sample` 示例，补齐 UTF-8 资源；同步更新 `build/android-build.py`。 |
| 2025-10-14 | 64 位补丁 Arm64 patch | 结合《docs/android/ARM64_libjpeg_build_guide.md:1》，校准 `dependencies/jpeg/prebuilt/android/Android.mk` 与 `client/android/LocojoyProject/jni/Application.mk` 的 arm64-v8a 配置。 |
| 2025-10-17 | 文档迭代 Documentation refresh | 建立 `cegui-0.8.7/docs` 专题目录，补齐维护、架构、构建、依赖四份文档并与主仓索引互链。 |

> 若后续出现回滚或临时热修，请在表格中追加记录并标注提交哈希 / MR 链接。

---

## 2. 已知问题与修复记录 Known Issues & Fixes

| 状态 Status | 问题 Issue | 影响 Impact | 修复方案 Resolution | 追踪单 Tracking |
|-------------|-----------|-------------|---------------------|-----------------|
| ✅ Closed | Android arm64 测试 `libgame.so` 链接缺少 `libjpeg.a` | Pixel 系列 64 位设备 | 采用《docs/android/ARM64_libjpeg_build_guide.md》流程补齐 `dependencies/jpeg/prebuilt/android/arm64-v8a/libjpeg.a`，并在 NDK 构建阶段校验。 | MGENG-2314 |
| ⚠️ Open | VS2015 `/LTCG` 发出 CEGUI/CEGUIBase.lib 运行库冲突告警 | Win32 Release 构建 | 按《docs/09-第三方库编译指南.md》统一 `/MD` 运行库；后续需批量执行 `fix_all_vcxproj_runtime.ps1`。 | MGENG-2479 |
| ✅ Closed | OHOS 流水线缺少 `CEGUIEx` 资源同步 | OpenHarmony CI | 在 `build/android-build.py` 增加资源 copy 步骤，构建指南新增验证脚本引用。 | MGENG-2421 |

---

## 3. 质量基线 Quality Baseline

- **单元测试 / Unit Tests**：启用 `-DCEGUI_BUILD_TESTS=ON` 后执行 `ctest`；结果存档至 `build/cegui-test-report/`。
- **场景验证 / Scenario QA**：
  - `samples/browser`：验证按钮、列表、标签页的渲染。
  - `datafiles/languages/zh_CN.UTF-8.lang`：对照《docs/toolchains/CEGUI技术集成指南.md》中的字体列表确认加载无误。
  - Android/OHOS：使用 `tests/js-tests` 的 CEGUI 场景回归脚本。
- **性能指标 / Performance**：
  - 基准设备：Android Pixel 4 (arm64)、Win32 Release。
  - 目标指标：UI 渲染平均耗时 < 1.2 ms/frame；内存增量 < 12 MB。

---

## 4. 维护流程 Maintenance Workflow

1. **需求/缺陷受理 Intake**：统一在 Jira（项目 `MGENG`）记录；关联相应的主仓文档章节。
2. **分支策略 Branching**：使用 `feature/cegui-<topic>` → `cegui/main`；合并前执行 `clang-format`、`ctest`、Android NDK 构建。
3. **兼容性验证 Compatibility**：
   - Win32：遵循《docs/09-第三方库编译指南.md》流程，执行 CEGUI + SILLY + PCRE 构建。
   - Android：运行 `client/android/LocojoyProject/mt3_build.bat` 并检查 `lib/armeabi-v7a` / `lib/arm64-v8a` 产物。
   - OpenHarmony：构建 `docs/cegui-cocos-ohos-sample` 并在实际设备上验证。
4. **发布节奏 Release cadence**：随 MT3 主版本同步；更新《docs/07_发布说明.md》以及本表格。

---

## 5. 待办与风险 Backlog & Risks

- [ ] 根据《docs/12-特殊技术专题.md》列出的 Windows 检查清单，补齐 PCRE/SILLY 最新编译记录。
- [ ] 对 `build/android-build.py` 新增的资源同步脚本进行自动化回归，避免遗漏。
- [ ] 评估迁移至 CEGUI 0.8.8 的可行性，关注自带的 CMake target 重构与 Unicode 修复。

风险提示：
- CEGUI 0.8.7 仍使用 `std::auto_ptr` 等过时接口，如主工程切换 C++17 需追加兼容补丁。
- Android arm64-v8a 需依赖外部 libjpeg 静态库，缺失会导致 `UnsatisfiedLinkError`；每次升级 NDK 后需复核。
- OpenHarmony 缺省不包含 Freetype，需从 `external/` 子模块打包；许可信息需在 `licenses/` 中同步。

---

> 更新流程：修改任何子文档后，请同步调整本页“最后更新”并在索引标注“已刷新”；提交信息建议：`docs(cegui): refresh maintenance notes for <topic>`。
