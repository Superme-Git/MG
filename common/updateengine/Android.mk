LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := updateengine_static

LOCAL_MODULE_FILENAME := libupdateengine

LOCAL_SRC_FILES := \
	UpdateEngine.cpp \
	UpdateManagerEx.cpp \
	android/AsyncFileDownloader.cpp \
	android/FileDownloader.cpp \
	android/GlobalFunction.cpp \
	android/GlobalNotification.cpp \
	android/UpdateEngineJni.cpp \


LOCAL_C_INCLUDES := \
	$(LOCAL_PATH) \
 	$(LOCAL_PATH)/android \
	$(LOCAL_PATH)/../platform \
	$(LOCAL_PATH)/../platform/android \
	$(LOCAL_PATH)/../ljfm/code/include \


LOCAL_WHOLE_STATIC_LIBRARIES := \
	platform_static \
 	ljfm_static \
 	xmlio_static \

# define the macro to compile through support/zip_support/ioapi.c                
LOCAL_CFLAGS := \
	-DUSE_FILE32API \
	-DXPP_IOS \
	-D_OS_IOS \
	-D_OS_ANDROID \
	-DANDROID

LOCAL_LDLIBS := -llog \

LOCAL_CPPFLAGS := -fexceptions

include $(BUILD_STATIC_LIBRARY)


