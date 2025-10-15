LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE    := cocos_network_static

LOCAL_MODULE_FILENAME := libnetwork

LOCAL_SRC_FILES := HttpClient.cpp

LOCAL_WHOLE_STATIC_LIBRARIES := cocos_curl_static
LOCAL_WHOLE_STATIC_LIBRARIES += cocos2dx_static

LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/network
LOCAL_C_INCLUDES := $(LOCAL_PATH)/../../external/curl/include/android

LOCAL_LDLIBS := -llog \

LOCAL_CFLAGS := -fexceptions
                    
include $(BUILD_STATIC_LIBRARY)

$(call import-module,external/curl/prebuilt/android)
$(call import-module,cocos2dx)
