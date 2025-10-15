LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := ceguiimagecodec_static

LOCAL_MODULE_FILENAME := libceguiimagecodec

LOCAL_SRC_FILES := \
../CEGUI/src/ImageCodecModules/SILLYImageCodec/CEGUISILLYImageCodec.cpp \
../CEGUI/src/ImageCodecModules/SILLYImageCodec/CEGUISILLYImageCodecModule.cpp \

LOCAL_C_INCLUDES := $(LOCAL_PATH)/../CEGUI/include \
	$(LOCAL_PATH)/../CEGUI/include/ImageCodecModules/SILLYImageCodec \
	$(LOCAL_PATH)/../../SILLY-0.1.0/include \
	$(LOCAL_PATH)/../../SILLY-0.1.0/include/loaders \
	$(LOCAL_PATH)/../../freetype-2.4.9/include \
	$(LOCAL_PATH)/../../pcre-8.31/prj2 \
	$(LOCAL_PATH)/../../../cocos2d-2.0-rc2-x-2.0.1/cocos2dx \
	$(LOCAL_PATH)/../../../cocos2d-2.0-rc2-x-2.0.1/cocos2dx/include \
	$(LOCAL_PATH)/../../../cocos2d-2.0-rc2-x-2.0.1/cocos2dx/platform \
	$(LOCAL_PATH)/../../../cocos2d-2.0-rc2-x-2.0.1/cocos2dx/platform/android \
	$(LOCAL_PATH)/../../../cocos2d-2.0-rc2-x-2.0.1/cocos2dx/kazmath/include \
	$(LOCAL_PATH)/../../../common \
	$(LOCAL_PATH)/../../../common/platform \


LOCAL_LDLIBS := -llog \

LOCAL_CFLAGS := -DPUBLISHED_VERSION \
	-DUSE_FILE32API \
	-D_OS_IOS \
	-D_OS_ANDROID \
	-DHAVE_CONFIG_H \
	-DCEGUI_STATIC \
	-DANDROID \
	-DCC_SUPPORT_PVRTC \


LOCAL_CPPFLAGS := -fexceptions -fpermissive

include $(BUILD_STATIC_LIBRARY)


