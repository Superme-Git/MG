LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := silly_static

LOCAL_MODULE_FILENAME := libsilly

LOCAL_SRC_FILES := \
	src/SILLYDataSource.cpp \
	src/SILLYFileDataSource.cpp \
	src/SILLYImage.cpp \
	src/SILLYImageContext.cpp \
	src/SILLYImageLoader.cpp \
	src/SILLYImageLoaderManager.cpp \
	src/SILLYMemoryDataSource.cpp \
	src/loaders/SILLYJPGImageContext.cpp \
	src/loaders/SILLYJPGImageLoader.cpp \
	src/loaders/SILLYPNGImageContext.cpp \
	src/loaders/SILLYPNGImageLoader.cpp \
	src/loaders/SILLYTGAImageContext.cpp \
	src/loaders/SILLYTGAImageLoader.cpp \

LOCAL_C_INCLUDES := $(LOCAL_PATH)/ \
	$(LOCAL_PATH)/../jpeg/include/android \
	$(LOCAL_PATH)/../png/include/android \
	$(LOCAL_PATH)/include \
	$(LOCAL_PATH)/include/loaders \
	$(LOCAL_PATH)/dependencies\include \

LOCAL_LDLIBS := \

LOCAL_LDLIBS := -llog \

LOCAL_CPPFLAGS :=  -fexceptions \

include $(BUILD_STATIC_LIBRARY)