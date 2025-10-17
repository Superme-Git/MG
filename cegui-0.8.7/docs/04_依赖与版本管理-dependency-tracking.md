# CEGUI 0.8.7 依赖与版本管理 / Dependency Tracking

- **最后更新 / Last Updated**：2025-10-17
- **适用版本 / Target Version**：CEGUI 0.8.7（MT3 集成版）
- **关联文档 / References**：  
  《docs/09-第三方库编译指南.md》《docs/android/ARM64_libjpeg_build_guide.md》《docs/12-特殊技术专题.md》

---

## 1. 依赖概览 Dependency Summary

| 组件 Component | 版本 Version | 来源 Source | 用途 Purpose | 备注 Notes |
|----------------|-------------|-------------|--------------|-----------|
| CEGUI Core | 0.8.7 | `cegui-0.8.7` 子目录 | UI 框架 | 采用源码 + 数据文件；保留与旧版 0.7.x 的资源兼容。 |
| Freetype | ≥ 2.9 | `external/freetype` / 系统包 | 字体渲染 | Windows 参考《docs/09-第三方库编译指南.md》重新编译；OHOS 打包随应用。 |
| SILLY | 0.1.0 | `dependencies/SILLY-0.1.0` | 图片编解码 | MT3 旧版依赖，仍需保留；支持 PNG/BMP。 |
| PCRE | 8.31 | `dependencies/pcre-8.31` | 正则解析 | 供 CEGUI 字符串匹配使用；需与 CEGUI 一同构建。 |
| zlib | 1.2.11 | cocos2d-x external | 数据压缩 | 与主工程共享，无需重复打包。 |
| libjpeg | libjpeg-turbo 2.x | `dependencies/jpeg/prebuilt/android` | Android JPEG 支持 | arm64-v8a 静态库需按《docs/android/ARM64_libjpeg_build_guide.md》补齐。 |
| Lua (可选) | 5.1 / 5.3 | `external/lua` | 脚本绑定 | 按需启用 `CEGUI_BUILD_LUA_MODULE`。 |

---

## 2. 平台版本矩阵 Version Matrix

| 平台 Platform | C++ 标准 | Renderer | 工具链 Toolchain | 关键依赖 Notes |
|---------------|----------|----------|------------------|----------------|
| Win32 | C++14 | OpenGL3 | MSVC v140 | SILLY、PCRE、Freetype 静态库；遵循 `/MD`。 |
| Linux | C++14 | OpenGL2 | GCC/Clang | 使用系统 Freetype/zlib，建议启用 `CEGUI_BUILD_IMAGECODEC_STB`. |
| Android | C++14 | OpenGLES2 | NDK r23+ (Clang) | `APP_ABI := armeabi-v7a arm64-v8a`；确保 libjpeg 静态库齐全。 |
| OpenHarmony | C++14 | OpenGLES2 | OHOS NDK 3.2+ | 自带 Freetype/zlib 打包，资源需复制到 HAP。 |

---

## 3. 依赖路径与校验 Dependency Paths

```text
dependencies/
├── cegui/                     # 旧版 CEGUI 0.7.x，仍用于 Win32 兼容
├── jpeg/
│   └── prebuilt/android/
│       ├── armeabi/libjpeg.a
│       ├── armeabi-v7a/libjpeg.a
│       ├── arm64-v8a/libjpeg.a  # 需手动编译
│       └── Android.mk
├── pcre-8.31/
├── SILLY-0.1.0/
└── ...
```

验证脚本建议：
- Windows：执行 `tools/list_big_files.ps1` 或 `dumpbin /DIRECTIVES` 检查运行库；
- Android：`aapt list apk | findstr /i "^lib/"` 确认 ABI；
- OHOS：`hdc fileinfo -f` 检查 HAP 内库。

---

## 4. ARM64 libjpeg 管控 Arm64 JPEG Management

- 根据《docs/android/ARM64_libjpeg_build_guide.md》生成 `libjpeg.a` 并复制至 `dependencies/jpeg/prebuilt/android/arm64-v8a/`。
- CMake/NDK 参数要求：
  - `ANDROID_PLATFORM=android-21`
  - 编译选项 `-fPIC`
  - NDK r21e+ 或 r10e standalone toolchain 均可
- 构建完成后修改记录应写入《01_技术维护记录-maintenance-notes.md》。

---

## 5. 更新策略 Update Policy

| 场景 Scenario | 操作 Action | 验证 Validation | 文档同步 Documentation |
|---------------|------------|-----------------|------------------------|
| 安全补丁 Hotfix | Cherry-pick 上游修复 | Win32 + Android 构建 + 样例运行 | 更新维护记录、发布说明 |
| 小版本升级 Minor upgrade | 评估 0.8.8/0.8.9 兼容性 | 全平台构建、资源回归测试 | 本文档记录差异；构建指南更新步骤 |
| 新依赖引入 New dependency | 增加第三方库 | LICENSE 审核、构建验证 | 在本档新增条目；索引标注更新时间 |

升级流程建议：
1. 阅读官方 Release Notes；
2. 创建 `feature/cegui-upgrade` 分支；
3. 执行 Win32、Linux、Android、OHOS 全量构建；
4. 更新 `licenses/` 与发布文档，MR 合并前附验证日志。

---

## 6. 许可证 License Compliance

| 组件 Component | License | 是否需署名 Attribution | 备注 Notes |
|----------------|---------|-------------------------|-----------|
| CEGUI | MIT | 否 | 主仓 `LICENSE` 已涵盖。 |
| Freetype | Freetype License | 是 | 应在应用文档中列出版权文本。 |
| SILLY | MIT | 否 | 保留源文件头部版权声明。 |
| PCRE | BSD | 否 | `dependencies/pcre-8.31/LICENCE` 需随包体分发。 |
| libjpeg-turbo | BSD | 否 | Build 指南已附带编译说明。 |
| Lua | MIT | 否 | 如启用脚本模块需在发行备注中说明。 |

---

## 7. 自检清单 Checklist

- [ ] Win32 构建的 `.lib/.dll` 均为 `/MD` 运行库（可使用 `dumpbin /DIRECTIVES` 检查）。
- [ ] Android APK 内含 `lib/armeabi-v7a` 与 `lib/arm64-v8a` 两组 so。
- [ ] `dependencies/jpeg/prebuilt/android` 存在 `arm64-v8a/libjpeg.a`。
- [ ] 许可证目录 `licenses/` 与发行包保持同步。
- [ ] 文档索引与维护记录已更新最新修改。

---

> 若对依赖版本或路径进行调整，请同时更新《03_构建与编译指南》和《01_技术维护记录》，并在索引中标记“已刷新”，以保持文档体系一致。
