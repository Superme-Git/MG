LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := ljfm_static

LOCAL_MODULE_FILENAME := libljfm

LOCAL_SRC_FILES := \
	code/source/common.cpp \
	code/source/ljfm.cpp \
	code/source/ljfmbase.cpp \
	code/source/ljfmopen.cpp \
	code/source/ljfileinfo.cpp \
	code/source/ljfsfile.cpp \
	code/source/ljfszipfile.cpp \
	code/source/ljfmasync.cpp \
	code/source/timelog.cpp \
	code/source/ljfmfs.cpp \
	code/source/ljfmfsmanager.cpp \
	code/source/ljfmimage.cpp \
	code/source/ljfmfex.cpp \
	code/source/util_android.cpp \


LOCAL_C_INCLUDES := $(LOCAL_PATH)/code \
                    $(LOCAL_PATH)/code/include \
                    $(LOCAL_PATH)/../platform \
$(LOCAL_PATH)/../../cocos2d-2.0-rc2-x-2.0.1/cocos2dx/include \


LOCAL_LDLIBS := -llog \
				-lz

LOCAL_WHOLE_STATIC_LIBRARIES := cocos2dx_static         
   
LOCAL_CFLAGS := \
	-DUSE_FILE32API \
	-D_OS_IOS \
	-DXPP_IOS \
	-D_OS_ANDROID \
	-DANDROID \


LOCAL_CPPFLAGS := -fexceptions -fpermissive

include $(BUILD_STATIC_LIBRARY)

$(call import-module,cocos2dx)


