LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := cauthc_static

LOCAL_MODULE_FILENAME := libcauthc
LOCAL_CPPFLAGS := -std=gnu++0x -fexceptions 

LOCAL_SRC_FILES := \
	../../authc/share/byteorder.cpp \
	../../authc/share/marshal.cpp \
	../../authc/share/octets.cpp \
	../../authc/share/security.cpp \
	../../authc/share/streamcompress.cpp \
	../../authc/authc.cpp \
	../../authc/ioengine.cpp \
	../../authc/netsession.cpp \
	../../authc/pollio.cpp \
	../../authc/protocol.cpp \
	../../authc/rpcgen.cpp \
	../../authc/timer.cpp \
	../../net/FNet.cpp \

LOCAL_CFLAGS := \
	-DANDROID \
	-D_REENTRANT_ \
	-fpermissive \
	-D_LOCOJOY_SDK_ \
#	-D_YJ_SDK_ \

LOCAL_LDLIBS := -llog \

LOCAL_EXPORT_C_INCLUDES := \
	$(LOCAL_PATH)/../../authc	\
	$(LOCAL_PATH)/../../authc/os/android \
	$(LOCAL_PATH)/../../net \

LOCAL_C_INCLUDES := \
	$(LOCAL_PATH)/../../authc	\
	$(LOCAL_PATH)/../../authc/os/android \
	$(LOCAL_PATH)/../../net \


            
include $(BUILD_STATIC_LIBRARY)

