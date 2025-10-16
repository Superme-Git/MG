APP_STL := gnustl_static
APP_CPPFLAGS := -frtti -std=c++11
APP_ABI := armeabi-v7a
APP_PLATFORM := android-11
# 说明：
# - 由于 x86_64 缺少 [libjpeg.a](dependencies/jpeg/prebuilt/android/x86_64/libjpeg.a:1)，暂时仅编译 ARMv7，避免 NDK 构建失败。
# - 已启用模拟器兼容开关 allow_emulator=1（见 [ljsdk.cfg](client/android/LocojoyProject/assets/ljsdk.cfg)），ARM 翻译层可运行时应避免闪退。
# - 若后续需要 x86_64 支持，请补全第三方库的 x86_64 版本（jpeg、breakpad 等）后再启用。
CFLAGS := -g

ifeq ($(NDK_DEBUG),1)
  APP_CPPFLAGS += -DCOCOS2D_DEBUG=1 -DXPP_IOS -DNDEBUG -DPUBLISHED_VERSION -DLOGCAT
  APP_OPTIM := debug
else
  APP_CPPFLAGS += -DCOCOS2D_DEBUG=0 -DXPP_IOS -DNDEBUG -DPUBLISHED_VERSION
  APP_OPTIM := release
endif

NDK_TOOLCHAIN_VERSION := 4.8