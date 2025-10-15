LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := engine_static

LOCAL_MODULE_FILENAME := libengine

ENGINE_NEDMALLOC := \
	common/nedmalloc/nunedmalloc.cpp

ENGINE_COMMON := \
	${ENGINE_NEDMALLOC} \
	common/nuexecutor.cpp \
	common/nufileiomanager.cpp \
	common/nuljfmutil.cpp \
	common/nupobject.cpp \
	common/nurecttopologylist.cpp \
	common/nustdafx.cpp \
	common/nutimer.cpp \
	common/nuutil.cpp \
	common/nuxbuffer.cpp \
	common/nuxlock.cpp \
	common/nuxmarshal.cpp \
	common/nuxpmaths.cpp 

ENGINE_RENDERER := \
	renderer/nucocos2d_render.cpp \
	renderer/nufontmanager.cpp \
	renderer/nufonttexture.cpp \
	renderer/nufxcode.cpp \
	renderer/nurenderer.cpp \
	renderer/nustatemanager.cpp \
	renderer/nutextblock.cpp \

ENGINE_ENGINE := \
	engine/nuanimanager.cpp \
	engine/nuspinemanager.cpp \
	engine/nucamera.cpp \
	engine/nucamerasmoother.cpp \
	engine/nucocos2d_type.cpp \
	engine/nucocos2d_wraper.cpp \
	engine/nuconfigmanager.cpp \
	engine/nuengine.cpp \
	engine/nuenginetimer.cpp \
	engine/nuenv.cpp \
	engine/nuguobj.cpp \
	engine/nurendertargetcache.cpp \
	engine/nusystemresourcemanager.cpp \
	engine/nuAccelerateUpdater.cpp \

ENGINE_SPRITE := \
	sprite/nucomponentsprite.cpp \
	sprite/numovablespriteimp.cpp \
	sprite/nupspritemaze.cpp \
	sprite/nusprite.cpp \
	sprite/nuspritemanager.cpp\
	sprite/nuspinesprite.cpp\

ENGINE_WORLD := \
	world/nudistortbase.cpp \
	world/nuentitativeobj.cpp \
	world/nuimmovableobj.cpp \
	world/nulkotree.cpp \
	world/nulkotreenodelist.cpp \
	world/numap.cpp \
	world/nuregionmap.cpp \
	world/nuworld.cpp 

ENGINE_MAP:= \
	map/numapobjs.cpp \
	map/nupbackground.cpp \
	map/nupdistortionobject.cpp \
	map/nupground.cpp \
	map/nupimg.cpp \
	map/nuplinkedobject.cpp \
	map/nupmap.cpp \
	map/nupmask.cpp \
	map/nuppathmap.cpp \
	map/nupstepsoundmap.cpp \
	map/nuptimeeffect.cpp \
	map/nuptransobjs.cpp \
	map/nuptriggerobjs.cpp \
	map/nupwaterarea.cpp

ENGINE_EFFECT:= \
	effect/nueffect.cpp \
	effect/nueffectclip.cpp \
	effect/nueffectmanager.cpp \
	effect/nufonteffect.cpp \
	effect/nugeffect.cpp \
	effect/nulisteffect.cpp \
	effect/nuparticleeffect.cpp \
	effect/nusebind.cpp \
	effect/nuxapbatch.cpp \
	effect/nuxapeffect.cpp \
	effect/nuspineeffect.cpp \
	effect/nulightingeffect.cpp \

ENGINE_PARTICLESYSTEM := \
	particlesystem/nucommonfuncs.cpp \
	particlesystem/nugeneralparticlesystem.cpp \
	particlesystem/nuparticlemanager.cpp \
	particlesystem/nuparticlesystem.cpp \
	particlesystem/nupslgroupobject.cpp \
	particlesystem/nupslpathobject.cpp \
	particlesystem/nupsparamobject.cpp \
	particlesystem/nupsvertexsobject.cpp \
	particlesystem/nuspecialparticlesystem.cpp \

ENGINE_SOUNDAREA := \
	soundarea/stepsoundtype.cpp \
	soundarea/xpenvsoundarea.cpp \
	soundarea/xpenvsoundcirclearea.cpp \
	soundarea/xpenvsoundpolygonarea.cpp \
	soundarea/xpsoundarea.cpp \

LOCAL_SRC_FILES := \
	${ENGINE_COMMON} \
	${ENGINE_RENDERER} \
	${ENGINE_ENGINE} \
	${ENGINE_SPRITE} \
	${ENGINE_WORLD} \
	${ENGINE_MAP} \
	${ENGINE_EFFECT} \
	${ENGINE_PARTICLESYSTEM} \
	${ENGINE_SOUNDAREA} \

LOCAL_C_INCLUDES := \
	$(LOCAL_PATH) \
	$(LOCAL_PATH)/common \
	$(LOCAL_PATH)/engine \
	$(LOCAL_PATH)/engine/common \
	$(LOCAL_PATH)/renderer \
	$(LOCAL_PATH)/sprite \
	$(LOCAL_PATH)/world \
	$(LOCAL_PATH)/map \
	$(LOCAL_PATH)/astar \
	$(LOCAL_PATH)/effect \
	$(LOCAL_PATH)/particlesystem \
	$(LOCAL_PATH)/../common/ljfm/code/include \
	$(LOCAL_PATH)/../common/platform \
	$(LOCAL_PATH)/../common/platform/utils \
	$(LOCAL_PATH)/../common/xmlio/xmlio/xmlio \
	$(LOCAL_PATH)/../dependencies/freetype-2.4.9/include \
	$(LOCAL_PATH)/../dependencies/LJXML/Include \

LOCAL_LDLIBS := -llog \

LOCAL_WHOLE_STATIC_LIBRARIES := cocos2dx_static
LOCAL_WHOLE_STATIC_LIBRARIES += cocos_spine_static

# define the macro to compile through support/zip_support/ioapi.c                
LOCAL_CFLAGS := \
	-DUSE_FILE32API \
	-D_OS_IOS \
	-D_OS_ANDROID \
	-DUSE_NED_MALLOC \
	-DSELF_MALLOC \
	-DXPP_IOS \
	-DANDROID \
	-DCC_SUPPORT_PVRTC \
	-D_LOCOJOY_SDK_ \
#	-D_YJ_SDK_ \

LOCAL_CPPFLAGS := -fexceptions  -fpermissive

include $(BUILD_STATIC_LIBRARY)

$(call import-module,cocos2dx)
$(call import-module,libSpine)

