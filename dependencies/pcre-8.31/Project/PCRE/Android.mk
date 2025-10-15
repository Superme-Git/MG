LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := pcre_static

LOCAL_MODULE_FILENAME := libpcre

LOCAL_SRC_FILES := \
../../pcre_byte_order.c \
../../pcre_compile.c \
../../pcre_config.c \
../../pcre_dfa_exec.c \
../../pcre_exec.c \
../../pcre_fullinfo.c \
../../pcre_get.c \
../../pcre_globals.c \
../../pcre_jit_compile.c \
../../pcre_maketables.c \
../../pcre_newline.c \
../../pcre_ord2utf8.c \
../../pcre_refcount.c \
../../pcre_string_utils.c \
../../pcre_study.c \
../../pcre_tables.c \
../../pcre_ucd.c \
../../pcre_valid_utf8.c \
../../pcre_version.c \
../../pcre_xclass.c \
../../prj2/pcre_chartables.c \


LOCAL_C_INCLUDES := $(LOCAL_PATH)/../../ \
                    $(LOCAL_PATH)/../../prj2 \


LOCAL_LDLIBS := \

# define the macro to compile through support/zip_support/ioapi.c                
LOCAL_CFLAGS := -DUSE_FILE32API \
	-DHAVE_CONFIG_H \
	-DSUPPORT_UTF \

LOCAL_CPPFLAGS := 

include $(BUILD_STATIC_LIBRARY)


