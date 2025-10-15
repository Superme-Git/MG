LOCAL_PATH := $(call my-dir) 

include $(CLEAR_VARS)

LOCAL_MODULE := cegui_static

LOCAL_MODULE_FILENAME := libcegui

LOCAL_SRC_FILES := 


LOCAL_C_INCLUDES := 


LOCAL_LDLIBS := 


LOCAL_WHOLE_STATIC_LIBRARIES := pcre_static
LOCAL_WHOLE_STATIC_LIBRARIES += silly_static
LOCAL_WHOLE_STATIC_LIBRARIES += freetype_static
LOCAL_WHOLE_STATIC_LIBRARIES += ceguibase_static
LOCAL_WHOLE_STATIC_LIBRARIES += ceguicocos2drender_static
LOCAL_WHOLE_STATIC_LIBRARIES += ceguifalagardwrbase_static
LOCAL_WHOLE_STATIC_LIBRARIES += ceguiimagecodec_static
LOCAL_WHOLE_STATIC_LIBRARIES += ceguixmlparser_static
LOCAL_WHOLE_STATIC_LIBRARIES += ceguilua_static


# define the macro to compile through support/zip_support/ioapi.c                
LOCAL_CFLAGS := -DUSE_FILE32API

include $(BUILD_STATIC_LIBRARY)

$(call import-module, pcre-8.31/Project/PCRE)
$(call import-module, SILLY-0.1.0)
$(call import-module, freetype-2.4.9/builds/android)
$(call import-module, cegui/CEGUIBase)
$(call import-module, cegui/CEGUICocos2DRender)
$(call import-module, cegui/CEGUIFalagardWRBase)
$(call import-module, cegui/CEGUIImageCodec)
$(call import-module, cegui/CEGUIXmlParser)
$(call import-module, cegui/CEGUILuaScriptModule)
