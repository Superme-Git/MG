# CEGUI 0.8.7 构建与编译指南 / Build & Compile Guide

- **最后更新 / Last Updated**：2025-10-17
- **适用版本 / Target Version**：CEGUI 0.8.7
- **参考流程 / Master References**：  
  《docs/09-第三方库编译指南.md》《docs/12-特殊技术专题.md》《docs/android/ARM64_libjpeg_build_guide.md》

---

## 1. 环境准备 Environment Setup

| 平台 Platform | 必备工具 Required Toolchain | 备注 Notes |
|---------------|-----------------------------|-----------|
| Windows | Visual Studio 2019 Build Tools（含 MSVC v140）、CMake ≥ 3.22、Python 3.8、Git | 与《docs/09-第三方库编译指南.md》保持一致；构建输出放在 `dependencies/cegui/project/win32/Release.win32`。 |
| Linux | GCC ≥ 9 或 Clang ≥ 10、CMake ≥ 3.20、Ninja、pkg-config | 安装 `freetype`、`libpng`、`glm` 等依赖。 |
| Android | Android NDK r23+、SDK Platform 31、CMake ≥ 3.22、Ninja | 使用 `build/android-build.py` 或独立 CMake 构建。 |
| OpenHarmony | OpenHarmony NDK (3.2+)、CMake ≥ 3.22、Python 3.8 | 参考 `docs/cegui-cocos-ohos-sample`。 |

公共要求：
- `git submodule update --init --recursive`
- Windows 端建议在 “x64 Native Tools Command Prompt” 中执行命令，确保找到 MSVC 工具链。

---

## 2. Windows 构建流程 Windows Build (MSVC)

### 2.1 VS 工程（兼容 MT3 旧流程）

1. 打开项目：  
   `dependencies\cegui\project\win32\cegui.win32.vcxproj`
2. 根据《docs/09-第三方库编译指南.md》调整：
   - **Platform Toolset**：`v140`
   - **C/C++ → 代码生成 → 运行库**：`多线程 DLL (/MD)`
   - **C/C++ → 预处理器**：`CEGUI_STATIC;WIN32;NDEBUG;_LIB`
3. 使用 MSBuild 构建：
   ```batch
   msbuild cegui.win32.vcxproj ^
     /t:Rebuild ^
     /p:Configuration=Release ^
     /p:Platform=Win32 ^
     /p:PlatformToolset=v140
   ```
4. 验证输出：
   ```batch
   dir Release.win32\cegui.lib
   dir Release.win32\CEGUIBase.dll
   ```

### 2.2 CMake 现代化流程

```powershell
cd cegui-0.8.7
cmake -S . -B build-win32 `
      -G "Visual Studio 16 2019" `
      -A Win32 `
      -DCEGUI_BUILD_SHARED_LIBS=ON `
      -DCEGUI_BUILD_RENDERER_OPENGL3=ON `
      -DCEGUI_BUILD_IMAGECODEC_STB=ON
cmake --build build-win32 --config Release
```

> 若需静态链接，请设置 `CEGUI_BUILD_SHARED_LIBS=OFF` 并在主工程链接选项中加入生成的 `.lib`。

---

## 3. Linux 构建流程 Linux Build

```bash
cd cegui-0.8.7
cmake -S . -B build-linux \
      -DCMAKE_BUILD_TYPE=Release \
      -DCEGUI_BUILD_SHARED_LIBS=ON \
      -DCEGUI_BUILD_RENDERER_OPENGL=ON \
      -DCEGUI_BUILD_IMAGECODEC_STB=ON
ninja -C build-linux
```

常见依赖安装（Debian/Ubuntu）：
```bash
sudo apt-get install build-essential cmake ninja-build libfreetype6-dev libpng-dev libglm-dev libgl1-mesa-dev
```

若需裁剪样例、文档：
```
-DCEGUI_BUILD_SAMPLES=OFF -DCEGUI_BUILD_DOCS=OFF
```

---

## 4. Android NDK 构建流程 Android Build

### 4.1 与 MT3 工程一体化构建

执行 `client/android/LocojoyProject/mt3_build.bat` 会同时编译 `libgame.so` 与依赖的 CEGUI 模块。构建前确认：

- `client/android/LocojoyProject/jni/Application.mk` 包含 `APP_ABI := armeabi-v7a arm64-v8a`
- `dependencies/jpeg/prebuilt/android/arm64-v8a/libjpeg.a` 已按《docs/android/ARM64_libjpeg_build_guide.md》生成
- `build/android-build.py` 使用 `--with-cegui` 参数：
  ```bash
  python build/android-build.py \
    --app client/android/LocojoyProject \
    --ndk D:/android-ndk-r23c \
    --sdk C:/Android/sdk \
    --abis armeabi-v7a,arm64-v8a \
    --compile-script \
    --with-cegui
  ```

### 4.2 独立验证 Standalone Build

```bash
mkdir -p cegui-0.8.7/build-android && cd cegui-0.8.7/build-android
cmake .. \
  -DCMAKE_TOOLCHAIN_FILE=%NDK%\build\cmake\android.toolchain.cmake \
  -DANDROID_ABI=arm64-v8a \
  -DANDROID_PLATFORM=android-21 \
  -DCEGUI_BUILD_SHARED_LIBS=ON \
  -DCEGUI_BUILD_RENDERER_OPENGLES2=ON \
  -DCEGUI_BUILD_IMAGECODEC_STB=ON
cmake --build . --config Release
```

产物位于 `build-android/lib/arm64-v8a` 与 `.../lib/armeabi-v7a`。构建结束后请执行：
```bash
aapt list mt3_locojoy.apk | findstr /i "^lib/"
```
确认两个 ABI 的 so 均存在。

---

## 5. OpenHarmony 构建流程 OpenHarmony Build

```bash
cd docs/cegui-cocos-ohos-sample
mkdir -p out/ohos && cd out/ohos
cmake ../ \
  -G Ninja \
  -DOHOS_PLATFORM=phone \
  -DCEGUI_ROOT=../../../cegui-0.8.7 \
  -DCEGUI_BUILD_RENDERER_OPENGLES2=ON
ninja
```

运行前需将 `datafiles` 打包到 HAP `resources/rawfile/cegui` 下，并复制生成的 `.so` 到 `libs/arm64-v8a`。事件桥接流程可参考示例项目中的 `CEGUIHelper.cpp`。

---

## 6. 常见问题与排查 Troubleshooting

| 症状 Symptom | 可能原因 Cause | 解决方案 Fix |
|--------------|---------------|--------------|
| `LNK2038 runtime library mismatch` | VS 项目仍为 `/MT` 或 `/MTd` | 运行 `tools/fix_all_vcxproj_runtime.ps1`，确保 `/MD`；详见《docs/09-第三方库编译指南.md》。 |
| `undefined reference to CEGUI::` (Android) | 未启用 `--with-cegui` 或 so 未拷贝至 `lib/` | 确认 `build/android-build.py` 参数与 `build.xml` 中的 `add-subdex-toapk` 步骤。 |
| `LOCAL_SRC_FILES points to a missing file` | 缺少 `arm64-v8a/libjpeg.a` | 按《docs/android/ARM64_libjpeg_build_guide.md》生成并复制到 `dependencies/jpeg/prebuilt/android/arm64-v8a/`。 |
| OpenHarmony 运行时缺字体 | HAP 未包含 Freetype/字体资源 | 在构建脚本中添加资源复制步骤，并更新 `licenses/`。 |
| Linux 构建缺 `freetype` | 系统未安装 dev 包 | 执行 `sudo apt-get install libfreetype6-dev`。 |

---

## 7. 集成与发布 Integration & Release

1. 将构建产物复制到主工程预期目录：
   - Windows：`dependencies/cegui/project/win32/Release.win32`
   - Android：`client/android/LocojoyProject/libs/<abi>`
   - OHOS：HAP `libs/arm64-v8a` + 资源目录
2. 更新《docs/07_发布说明.md》中的版本信息，并在《01_技术维护记录-maintenance-notes.md》登记。
3. 在 MR/PR 中附上构建日志、测试结果以及影响范围说明。

---

> 若改动构建脚本或依赖版本，请同步更新本指南与索引条目，并在提交说明中引用相关 MT3 文档，保证历史可追溯。
