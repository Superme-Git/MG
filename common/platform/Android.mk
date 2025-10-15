LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := platform_static 

LOCAL_MODULE_FILENAME := libplatform

LOCAL_SRC_FILES := \
	ini/IniFile.cpp \
	log/CoreLog.cpp \
	platform/ksemaphore.cpp \
	platform/usememory.cpp \
	platform/platform_types.cpp \
	platform/thread.cpp \
	utils/Encoder.cpp \
	utils/stringbuilder.cpp \
	utils/StringCover.cpp \
	utils/StringUtil.cpp \
	utils/Utils.cpp \
	android/FileUtil.cpp \
	android/SDJniHelper.cpp 

LOCAL_C_INCLUDES := \
	$(LOCAL_PATH)/ini \
	$(LOCAL_PATH)/log \
	$(LOCAL_PATH)/platform \
	$(LOCAL_PATH)/utils \
	$(LOCAL_PATH)/android \
	$(LOCAL_PATH)/../authc/include \

LOCAL_WHOLE_STATIC_LIBRARIES := cocos2dx_static

LOCAL_LDLIBS := \
    -llog \

# define the macro to compile through support/zip_support/ioapi.c                
LOCAL_CFLAGS := \
	-DUSE_FILE32API \
	-DANDROID \
	-D_OS_IOS \

LOCAL_CPPFLAGS := -fexceptions -fpermissive

include $(BUILD_STATIC_LIBRARY)

$(call import-module,cauthc/projects/android)
$(call import-module,cocos2dx)

