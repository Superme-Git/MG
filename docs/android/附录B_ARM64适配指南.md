# 构建 arm64-v8a 静态库 libjpeg.a（libjpeg-turbo）并集成到工程

目标
- 产出 Android arm64-v8a 架构的静态库 libjpeg.a，并放置于工程要求路径，以解决 64-bit-only 设备（如 Pixel 7）在加载 `libgame.so` 链接到 JPEG 静态库时的缺失问题。
- 工程引用路径要求见 [Android.mk](dependencies/jpeg/prebuilt/android/Android.mk:6)：`dependencies/jpeg/prebuilt/android/arm64-v8a/libjpeg.a`。
- 说明：当前项目默认仅支持 armeabi-v7a。本附录用于未来规划 arm64 适配，启用前需在 Application.mk 中显式添加 arm64 配置并完成所有第三方库适配。

注意事项
- arm64-v8a 需要 `ANDROID_PLATFORM` >= 21（需将 [APP_PLATFORM](client/android/LocojoyProject/jni/Application.mk:4) 提升到 android-21）。
- 生成的 libjpeg.a 必须为 `-fPIC`（Position Independent Code），用于链接到共享对象。
- 产出的 lib 与工程中的 `libgame.so` 在 API level/ABI 上保持一致（arm64-v8a / android-21）。

推荐方案 A（Windows + NDK r21e + CMake）
说明：使用 NDK r21e 的 CMake toolchain 构建第三方库，产出的静态库可供工程（当前使用 r10e/clang）链接使用。由于是静态库，只要 ABI/API 一致，混用 NDK 版本通常可行。
1) 准备依赖：
   - 安装 CMake（>= 3.22）和 Ninja（推荐）或使用 `"NMake Makefiles"`（需 Visual Studio 工具链）。
   - 下载 libjpeg-turbo 源码（例如 `libjpeg-turbo-2.x`），解压到某路径（例如 `E:\thirdparty\libjpeg-turbo`）。
2) 配置环境变量（PowerShell 示例）：
   ```
   $env:ANDROID_NDK="D:\android-ndk-r21e"
   ```
3) 生成与构建（以 Ninja 为例）：
   - 进入源码根目录：
     ```
     cd E:\thirdparty\libjpeg-turbo
     ```
   - 生成构建目录并配置 CMake：
     ```
     cmake -S . -B build-arm64 `
       -G "Ninja" `
       -DCMAKE_TOOLCHAIN_FILE="$env:ANDROID_NDK\build\cmake\android.toolchain.cmake" `
       -DANDROID_ABI=arm64-v8a `
       -DANDROID_PLATFORM=android-21 `
       -DCMAKE_BUILD_TYPE=Release `
       -DENABLE_SHARED=OFF `
       -DENABLE_STATIC=ON `
       -DWITH_SIMD=ON
     ```
   - 执行构建：
     ```
     cmake --build build-arm64 --target jpeg
     ```
   - 构建完成后，产物 libjpeg.a 常见位置：
     - `build-arm64/libjpeg.a` 或
     - `build-arm64/libjpeg-turbo/libjpeg.a`
     具体位置取决于版本与 CMake 配置输出结构。可用如下命令查找：
     ```
     Get-ChildItem -Recurse build-arm64 | Where-Object {$_.Name -eq "libjpeg.a"} | Select-Object FullName
     ```
4) 拷贝到工程要求路径：
   - 将生成的 `libjpeg.a` 复制到：
     ```
     e:\MT3\dependencies\jpeg\prebuilt\android\arm64-v8a\libjpeg.a
     ```
   - 若目录不存在，先创建 `arm64-v8a` 子目录。

方案 B（Windows + NDK r10e + Standalone toolchain + CMake）
说明：在 r10e 环境下直接生成 arm64 静态库。r10e 需要使用 clang 与 standalone toolchain，并且构建过程通常需要 MSYS2/Cygwin/Bash 环境。若你没有 Bash 环境，建议优先使用方案 A。
1) 生成 standalone toolchain（Bash 环境下执行）：
   ```
   D:/android-ndk-r10e/build/tools/make-standalone-toolchain.sh \
     --platform=android-21 \
     --arch=arm64 \
     --install-dir=C:/temp/ndk-arm64-toolchain
   ```
2) 设置工具链变量（PowerShell 示例）：
   ```
   $TOOLCHAIN="C:\temp\ndk-arm64-toolchain"
   $CC="$TOOLCHAIN\bin\aarch64-linux-android-clang"
   $AR="$TOOLCHAIN\bin\llvm-ar"
   $RANLIB="$TOOLCHAIN\bin\llvm-ranlib"
   ```
3) 使用 CMake 指定编译器与 flags（确保 -fPIC）：
   ```
   cd E:\thirdparty\libjpeg-turbo
   cmake -S . -B build-r10e-arm64 `
     -G "Ninja" `
     -DCMAKE_C_COMPILER="$CC" `
     -DCMAKE_AR="$AR" `
     -DCMAKE_RANLIB="$RANLIB" `
     -DCMAKE_BUILD_TYPE=Release `
     -DENABLE_SHARED=OFF `
     -DENABLE_STATIC=ON `
     -DWITH_SIMD=ON `
     -DCMAKE_C_FLAGS="-fPIC"
   cmake --build build-r10e-arm64 --target jpeg
   ```
4) 拷贝产物到工程路径：
   ```
   e:\MT3\dependencies\jpeg\prebuilt\android\arm64-v8a\libjpeg.a
   ```

验证步骤
- 在工程中已引用路径 [Android.mk](dependencies/jpeg/prebuilt/android/Android.mk:6)：
  ```
  LOCAL_SRC_FILES := $(TARGET_ARCH_ABI)/libjpeg.a
  ```
  因此仅需确保 `arm64-v8a/libjpeg.a` 存在。
- 启用前需确认工程 NDK 配置（如采用 arm64）：
  - [APP_ABI](client/android/LocojoyProject/jni/Application.mk:3) 添加 `arm64-v8a`
  - [APP_PLATFORM](client/android/LocojoyProject/jni/Application.mk:4) 提升为 `android-21`
  - [NDK_TOOLCHAIN_VERSION](client/android/LocojoyProject/jni/Application.mk:19) 使用 `clang`（r10e 下 arm64 需 clang）
- 注意：默认打包脚本仅处理 armeabi-v7a。如需 arm64，请在打包脚本中补充相应 so 复制与打包规则（视工程实际而定）。

构建与打包（PowerShell 或 CMD）
- PowerShell（推荐，逐步执行，避免多终端干扰）：
  ```
  cd e:\MT3\client\android\LocojoyProject
  $env:NDK_HOME="D:\android-ndk-r10e"
  ./mt3_build.bat
  ant -buildfile build.xml release
  ```
- CMD（单行链路）：
  ```
  cmd /C "cd client\android\LocojoyProject && set NDK_HOME=D:\android-ndk-r10e && mt3_build.bat && ant -buildfile build.xml release"
  ```

常见问题与排错
- 错误：`LOCAL_SRC_FILES points to a missing file`（NDK 日志）
  - 说明：`dependencies/jpeg/prebuilt/android/arm64-v8a/libjpeg.a` 缺失或路径错误。
  - 处理：确保 `arm64-v8a/libjpeg.a` 放置正确；如你将文件放到其它路径，可临时修改 [Android.mk](dependencies/jpeg/prebuilt/android/Android.mk:6) 的 `LOCAL_SRC_FILES` 指向绝对路径进行验证。
- 错误：编译失败因缺少 Bash 或 Ninja
  - 方案 A 推荐安装 Ninja 并使用 CMake；如不便安装，CMake 也可用 `"NMake Makefiles"`（需 VS 工具链）或 `"Unix Makefiles"`（需 msys/mingw）。
- 注意：ARM64 设备验证
  - 成功打包后，APK 内应含 `lib/arm64-v8a/` 与 `lib/armeabi-v7a/` 两套架构；在 Pixel 7 上将优先加载 `lib/arm64-v8a`。
  - 如仍出现闪退，请抓取 logcat 关键日志（linker/UnsatisfiedLinkError/libc），并检查所有依赖库（du/locSDK6a/game）是否同步提供了 arm64 版本。

结论
- 通过上述任一方案生成 `arm64-v8a/libjpeg.a` 并放置到工程路径后，双架构构建可继续执行。完成后在 64-bit-only 设备首启不再因 `libjpeg.a` 缺失导致崩溃。