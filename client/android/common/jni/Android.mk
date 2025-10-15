LOCAL_PATH := $(call my-dir)
 
#add by snowfish -s
include $(CLEAR_VARS)
LOCAL_MODULE := gangaOnlineUnityHelper
LOCAL_SRC_FILES :=../../../3rdplatform/YijieSDK/lib/$(TARGET_ARCH_ABI)/libgangaOnlineUnityHelper.so
include $(PREBUILT_SHARED_LIBRARY)
#add by snowfish -e
 
#add baidu location sdk -s
include $(CLEAR_VARS)
LOCAL_MODULE := locSDK6a
LOCAL_SRC_FILES :=../../../3rdplatform/BaiduLBS_AndroidSDK_Lib/libs/$(TARGET_ARCH_ABI)/liblocSDK6a.so
include $(PREBUILT_SHARED_LIBRARY)
#add baidu location sdk -e

#add libdu location sdk -s
include $(CLEAR_VARS)
LOCAL_MODULE := libdu
LOCAL_SRC_FILES :=../../../3rdplatform/duClient_SDK_Lib/libs/$(TARGET_ARCH_ABI)/libdu.so
include $(PREBUILT_SHARED_LIBRARY)
#add libdu location sdk -e

include $(CLEAR_VARS)

LOCAL_MODULE := game_shared

LOCAL_MODULE_FILENAME := libgame

GAME_ROOT := $(LOCAL_PATH)/../../../..

$(call import-add-path,${GAME_ROOT}/)
$(call import-add-path,${GAME_ROOT}/client/)
$(call import-add-path,${GAME_ROOT}/common/)
$(call import-add-path,${GAME_ROOT}/dependencies/)
$(call import-add-path,${GAME_ROOT}/cocos2d-2.0-rc2-x-2.0.1/)
$(call import-add-path,${GAME_ROOT}/cocos2d-2.0-rc2-x-2.0.1/extensions/)
$(call import-add-path,${GAME_ROOT}/cocos2d-2.0-rc2-x-2.0.1/cocos2dx/platform/third_party/android/prebuilt/)

LOCAL_SRC_FILES := main.cpp 


LOCAL_C_INCLUDES := \
	$(LOCAL_PATH)/../../../../dependencies/google-breakpad/src \
	$(LOCAL_PATH)/../../../../dependencies/google-breakpad/src/common/android/include \


#LOCAL_SHARED_LIBRARIES := updateengine_shared  \


#LOCAL_WHOLE_STATIC_LIBRARIES := \
 	breakpad_client \
	cocos2dx_static \
	cocosdenshion_static \
	cegui_static \
 	xmlio_static \
 	cauthc_static \
	platform_static \
 	ljfm_static \
	engine_static \
	FireClient_static \

LOCAL_WHOLE_STATIC_LIBRARIES := cocos2dx_static
LOCAL_WHOLE_STATIC_LIBRARIES += cocosdenshion_static
LOCAL_WHOLE_STATIC_LIBRARIES += breakpad_client
LOCAL_WHOLE_STATIC_LIBRARIES += cegui_static
LOCAL_WHOLE_STATIC_LIBRARIES += xmlio_static
LOCAL_WHOLE_STATIC_LIBRARIES += platform_static
LOCAL_WHOLE_STATIC_LIBRARIES += ljfm_static
LOCAL_WHOLE_STATIC_LIBRARIES += engine_static
LOCAL_WHOLE_STATIC_LIBRARIES += FireClient_static
LOCAL_WHOLE_STATIC_LIBRARIES += cauthc_static
LOCAL_WHOLE_STATIC_LIBRARIES += updateengine_static
LOCAL_WHOLE_STATIC_LIBRARIES += cocos_network_static

#add by snowfish -s
LOCAL_SHARED_LIBRARIES += gangaOnlineUnityHelper
#add by snowfish -e

#add baidu location SDK -s
LOCAL_SHARED_LIBRARIES += locSDK6a
#add baidu location SDK -e

#add libdu location SDK -s
LOCAL_SHARED_LIBRARIES += du
#add libdu location SDK -e

include $(BUILD_SHARED_LIBRARY)

$(call import-module,CocosDenshion/android)
$(call import-module,google-breakpad/android/google_breakpad)
$(call import-module,cocos2dx)
$(call import-module,cegui)
$(call import-module,cauthc/projects/android)
$(call import-module,platform)
$(call import-module,ljfm)
$(call import-module,updateengine)
$(call import-module,engine)
$(call import-module,FireClient)
$(call import-module,network)

