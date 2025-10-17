# CEGUI 0.8.7 架构分析报告 / Architecture Review

- **最后更新 / Last Updated**：2025-10-17
- **适用版本 / Target Version**：CEGUI 0.8.7（与 MT3 cocos2d-x 2.x/3.x 混合环境）
- **参考文档 / References**：  
  《docs/toolchains/CEGUI技术集成指南.md》《docs/12-特殊技术专题.md》《docs/03-技术体系总结.md》

---

## 1. 架构总览 Overview

CEGUI 通过三个核心层与 MT3 主引擎交互：

1. **系统层 System**：`CEGUI::System`、`WindowManager`、`SchemeManager` 管理 UI 组件生命周期；
2. **渲染层 Renderer**：`CEGUI::OpenGL3Renderer`（PC）/`CEGUI::OpenGLES2Renderer`（Android/OHOS）将 UI 批次写入 cocos2d-x 渲染队列；
3. **适配层 Integration**：在游戏主循环中注入更新、渲染、输入事件，参考 `client/android/LocojoyProject/src/com/locojoy/mini/mt3/GameApp.java:1538` 以及主仓 C++ 集成指南。

整体流程与《docs/toolchains/CEGUI技术集成指南.md》中给出的旧版 0.7.x 结构一致，差异主要在于 0.8.7 引入 CMake 目标和改进的资源管理。

---

## 2. 渲染管线 Rendering Pipeline

| 环境 | 渲染器 Renderer | 关键差异 Notes |
|------|-----------------|----------------|
| Windows / Linux | `CEGUI::OpenGL3Renderer` | 使用 VAO/VBO + GLSL 3.3；与 cocos2d-x 共用 OpenGL 上下文，需在渲染前后保存状态。 |
| Android / OpenHarmony | `CEGUI::OpenGLES2Renderer` | 兼容 GLES2 指令集；shader 存放于 `datafiles/shaders/OpenGLES2`. |

渲染步骤：

1. 初始化阶段通过 `CEGUI::System::create(renderer)` 挂接到 cocos2d-x；
2. 每帧调用：
   ```cpp
   CEGUI::System& sys = CEGUI::System::getSingleton();
   sys.injectTimePulse(delta);
   sys.renderAllGUIContexts();
   ```
3. cocos2d-x 场景在 UI 之前渲染；若需要 HUD 与 3D 场景混合，可使用 `RenderCommand` 自定义插入位置。

> 状态同步：参照《docs/toolchains/CEGUI技术集成指南.md》中“渲染状态保护”章节，确保 GL 深度、混合、视口在渲染前后恢复。

---

## 3. 资源系统 Resource System

- **路径配置**：通过 `CEGUI::DefaultResourceProvider` 设置为 `dependencies/cegui/datafiles`，与 MT3 的 `FileUtils` 相结合，实现多平台路径映射。
- **主要资源类型**：
  - Layout (`.layout`/`.blayout`)
  - LookNFeel (`.looknfeel`)
  - Imageset (`.imageset`)
  - Font (`.font`)
  - Scheme (`.scheme`)
- **加载流程**：
  ```cpp
  CEGUI::SchemeManager::getSingleton().createFromFile("MT3.scheme");
  CEGUI::GUIContext& ctx = sys.getDefaultGUIContext();
  auto root = CEGUI::WindowManager::getSingleton().loadLayoutFromFile("MainHUD.layout");
  ctx.setRootWindow(root);
  ```

与 MT3 旧版兼容性：保留对 `.blayout` Binary Layout 的支持（参考《docs/toolchains/CEGUI技术集成指南.md》“Binary Layout 序列化”章节），升级时需确保资源转储脚本同步。

---

## 4. 输入与事件 Input & Event Flow

- cocos2d-x 在 Win32 通过 `CCEventDispatcher`、在 Android 通过 JNI → `GameApp` 注入触摸/键盘事件；
- CEGUI 接口映射：
  - 鼠标移动：`sys.injectMouseMove(dx, dy)`
  - 鼠标按钮：`sys.injectMouseButtonDown/Up(button)`
  - 触摸：可映射为鼠标事件或使用多指支持 `PointerInput`；
  - 键盘：`sys.injectKeyDown(scanCode)` + `sys.injectChar(codePoint)`
- Lua 脚本：`cegui-0.8.7/cegui/src/ScriptModules/Lua` 提供 `CEGUI::System::getSingleton().executeScriptFile` 等绑定，参见主仓 Lua 集成示例。

事件订阅示例（与主仓指南保持一致）：
```cpp
CEGUI::PushButton* login =
    static_cast<CEGUI::PushButton*>(window->getChild("LoginButton"));
login->subscribeEvent(
    CEGUI::PushButton::EventClicked,
    CEGUI::Event::Subscriber(&LoginDialogHandler::OnLoginClicked, this));
```

---

## 5. OpenHarmony 适配 OHOS Integration

基于 `docs/cegui-cocos-ohos-sample`：

1. 通过 CMake `-DOHOS_PLATFORM=phone` 指定目标；
2. 使用 ArkUI → cocos2d-x → CEGUI 的事件传递链；
3. 资源部署：将 `datafiles` 打包到 HAP 内部 `/resources/rawfile/cegui`；
4. 运行时依赖：Freetype、zlib 需随应用打包，许可证包含在 `licenses/`。

> OHOS 端如需使用高 DPI 字体，建议在 `datafiles/fonts` 中预生成 `.font` 文件，避免运行时转换开销。

---

## 6. 与主仓的差异与兼容 Compatibility Notes

- MT3 现网文档仍以 CEGUI 0.7.x 为基线（参见《docs/toolchains/CEGUI技术集成指南.md》），0.8.7 与旧版的主要差异：
  - CMake 脚本重构：使用 `CEGUI_BUILD_*` 选项控制模块；
  - Renderer API 更新：`OpenGL3Renderer` 默认启用现代管线；
  - `std::auto_ptr` 仍保留，C++17 环境需兼容处理。
- 为确保可平滑迁移，建议保持以下一致：
  - 资源命名与 LookNFeel 结构不变；
  - 输入事件封装保持向后兼容；
  - Lua API 维持原有命名，新增接口需在维护记录中记录。

---

## 7. 扩展建议 Extension Guidelines

1. **主题定制**：建立独立的 `MT3Theme.looknfeel` 与纹理集，参照《docs/12-特殊技术专题.md》提供的窗口池、批次优化策略。
2. **性能优化**：结合主仓文档中提到的“Window 池化”与 “批量渲染”方案，减少窗口创建与销毁开销。
3. **测试策略**：在 `tests/js-tests` 增加更细粒度的 UI 断言；OHOS 端可借助截图对比工具。
4. **迁移路线**：若升级到 0.8.8，可优先在独立分支验证 Renderer 与资源加载兼容性，再与主仓同步。

---

> 如对渲染管线或资源结构进行重大调整，请同步更新本档，并在维护记录与发布说明中记录影响范围。
