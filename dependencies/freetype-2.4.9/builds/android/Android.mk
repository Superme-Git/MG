LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := freetype_static

LOCAL_MODULE_FILENAME := libfreetype

LOCAL_SRC_FILES := \
../../src/base/ftsystem.c \
../../src/base/ftbase.c \
../../src/base/ftstroke.c \
../../src/base/ftinit.c \
../../src/base/ftglyph.c \
../../src/base/ftfstype.c \
../../src/base/ftgasp.c \
../../src/base/ftdebug.c \
../../src/base/ftbitmap.c \
../../src/base/ftbbox.c \
../../src/base/ftgxval.c \
../../src/base/ftlcdfil.c \
../../src/base/ftmm.c \
../../src/base/ftotval.c \
../../src/base/ftpatent.c \
../../src/base/ftpfr.c \
../../src/base/ftsynth.c \
../../src/base/fttype1.c \
../../src/base/ftwinfnt.c \
../../src/base/ftxf86.c \
../../src/cff/cff.c \
../../src/bdf/bdf.c \
../../src/lzw/ftlzw.c \
../../src/gzip/ftgzip.c \
../../src/autofit/autofit.c \
../../src/smooth/smooth.c \
../../src/winfonts/winfnt.c \
../../src/type42/type42.c \
../../src/cid/type1cid.c \
../../src/type1/type1.c \
../../src/truetype/truetype.c \
../../src/sfnt/sfnt.c \
../../src/raster/raster.c \
../../src/psnames/psmodule.c \
../../src/pshinter/pshinter.c \
../../src/psaux/psaux.c \
../../src/pfr/pfr.c \
../../src/pcf/pcf.c \


LOCAL_C_INCLUDES := $(LOCAL_PATH)/../../include \
                    $(LOCAL_PATH)/../../include/freetype \
                    $(LOCAL_PATH)/../../include/freetype/config \
                    $(LOCAL_PATH)/../../src \


LOCAL_LDLIBS := -llog \

LOCAL_CFLAGS := -DFT2_BUILD_LIBRARY \

LOCAL_CPPFLAGS := 

include $(BUILD_STATIC_LIBRARY)


