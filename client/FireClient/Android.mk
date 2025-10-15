LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := FireClient_static

LOCAL_MODULE_FILENAME := libFireClient

CLIENT_MANAGER := \
	Application/Manager/ArtTextManager.cpp \
	Application/Manager/BattleReplayManager.cpp \
	Application/Manager/ConfigManager.cpp \
	Application/Manager/DownloadManager.cpp \
	Application/Manager/GameStateManager.cpp \
	Application/Manager/IconManager.cpp \
	Application/Manager/LoginManager.cpp \
	Application/Manager/MainRoleDataManager.cpp \
	Application/Manager/ProtocolLuaFunManager.cpp \
	Application/Manager/RoleItemManager.cpp \
	Application/Manager/SceneMovieManager.cpp \
	Application/Manager/TaskOnOffEffectManager.cpp \
	Application/Manager/VoiceManager.cpp \
	Application/Manager/EmotionManager.cpp \
	Application/Manager/GameUIManager.cpp \
	Application/Manager/MessageManager.cpp \
	Application/Manager/NewRoleGuideManager.cpp \
	Application/Manager/MusicSoundVolumeMixer.cpp \
	Application/Manager/SpaceManager.cpp \

CLIENT_BATTLE := \
	Application/Battle/BattleIllusion.cpp \
	Application/Battle/BattleMagicControl.cpp \
	Application/Battle/BattleManager_AI.cpp \
	Application/Battle/BattleManager_Protocol.cpp \
	Application/Battle/BattleManager_Render.cpp \
	Application/Battle/BattleManager_Replay.cpp \
	Application/Battle/BattleManager_Watch.cpp \
	Application/Battle/BattleManagerDemo.cpp \
	Application/Battle/BattleManagerOperate.cpp \
	Application/Battle/Battler.cpp \
	Application/Battle/BattleScriptPlayer.cpp \
	Application/Battle/Skill.cpp \
	Application/Battle/SkillBuilder.cpp \
	
CLIENT_FRAMEWORK := \
	Application/Framework/ConnectGetServerInfo.cpp \
	Application/Framework/DeviceInfo.cpp \
	Application/Framework/GameApplication.cpp \
	Application/Framework/GameOperateState.cpp \
	Application/Framework/GameScene.cpp \
	Application/Framework/ITickTimer.cpp \
	Application/Framework/LuaMessageTask.cpp \
	Application/Framework/LuaTickerRegister.cpp \
	Application/Framework/LuaXPRenderEffect.cpp \
	Application/Framework/XPRenderEffect.cpp \
	Application/Framework/MapWalker.cpp \
	Application/Framework/MessageTask.cpp \
	Application/Framework/MovieSection.cpp \
	Application/Framework/NetConnection.cpp \
	Application/Framework/ResolutionAdapter.cpp \
	Application/Framework/RoleItem.cpp \
	Application/Framework/SceneEffect.cpp \
	Application/Framework/FriendVisitor.cpp \
	Application/Framework/DeviceData.cpp \
	Application/Framework/3rdplatform/ChannelManager.cpp \
	Application/Framework/3rdplatform/GameSdk.cpp \
	Application/platform/win/WavRecorder.cpp \
	Application/Framework/GameCenter_nonIos.cpp \
	Application/Framework/GameCenter_crossPlatform.cpp \
	Application/Framework/LocationDetector_crossPlatform.cpp \
	Application/Framework/LocationDetector_android.cpp \
	
CLIENT_SCENEOBJ := \
	Application/SceneObj/Character.cpp \
	Application/SceneObj/ComponentsObject.cpp \
	Application/SceneObj/DroptItem.cpp \
	Application/SceneObj/FollowNpc.cpp \
	Application/SceneObj/FossickNpc.cpp \
	Application/SceneObj/MainCharacter.cpp \
	Application/SceneObj/MovieSceneNpc.cpp \
	Application/SceneObj/Npc.cpp \
	Application/SceneObj/Pet.cpp \
	Application/SceneObj/SceneNpc.cpp \
	Application/SceneObj/SceneObject.cpp \

CLIENT_GAMEDATA := \
	Application/GameTable/TableDataManager.cpp \

CLIENT_GAMEUI := \
	Application/GameUI/Dialog.cpp \
	Application/GameUI/CEGUIIMEDelegate.cpp \
	Application/GameUI/UISpineSprite.cpp \
	Application/GameUI/UISprite.cpp \
	Application/androidcommon/AndroidLoginDialog.cpp \

CLIENT_OGGENC := \
	Application/oggenc/audio.cpp \
	Application/oggenc/encode.cpp \
	Application/oggenc/lyrics.cpp \
	Application/oggenc/oggenc.cpp \
	Application/oggenc/platform.cpp \
	Application/oggenc/resample.cpp \
	Application/oggenc/skeleton.cpp \

CLIENT_AMRENC := \
	Application/Amr/amr_dec.cpp \
	Application/Amr/amr_enc.cpp \
	Application/Amr/sp_dec.cpp \
	Application/Amr/sp_enc.cpp \

CLIENT_PROTOCOL := \
	Application/ProtoDef/rpcgen.cpp

CLIENT_UTILS := \
	Application/Utils/IniManager.cpp \
	Application/Utils/Utils2.cpp \
	Application/Utils/ChineseCode.cpp \
	Application/Utils/base64.cpp \
	Application/Utils/Voice.cpp \
	Application/Utils/CallLuaUtil.cpp \

CLIENT_WEB := \
	../../cocos2d-2.0-rc2-x-2.0.1/extensions/network/HttpClient.cpp \
	../../cocos2d-2.0-rc2-x-2.0.1/extensions/network/WebSocket.cpp \

CLIENT_LUA_KC_GLUE := \
	Application/Framework/LuaEngine.cpp \
	Application/Framework/LuaFireClient.cpp \

CLIENT_LUA_VM := \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/lapi.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/lauxlib.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/lbaselib.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/lcode.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/ldblib.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/ldebug.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/ldo.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/ldump.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/lfunc.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/lgc.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/linit.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/liolib.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/llex.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/lmathlib.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/lmem.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/loadlib.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/lobject.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/lopcodes.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/loslib.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/lparser.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/lstate.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/lstring.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/lstrlib.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/ltable.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/ltablib.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/ltm.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/lua.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/lundump.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/lvm.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/lzio.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/lua/print.c \

CLIENT_TOLUA++ := \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/tolua/tolua_event.c  \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/tolua/tolua_is.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/tolua/tolua_map.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/tolua/tolua_push.cpp \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/tolua/tolua_to.c  \

CLIENT_LUA_COCOS2D_GLUE := \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/cocos2dx_support/Cocos2dxLuaLoader.cpp \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/cocos2dx_support/tolua_fix.c \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/cocos2dx_support/LuaCocos2d.cpp \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/cocos2dx_support/CCLuaJavaBridge.cpp \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/cocos2dx_support/jni/Java_org_cocos2dx_lib_Cocos2dxLuaJavaBridge.cpp \

CLIENT_LUA_ENGINE := \
	$(CLIENT_TOLUA++) \
	$(CLIENT_LUA_KC_GLUE) \
	$(CLIENT_LUA_COCOS2D_GLUE) \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/cocos2dx_support/CCLuaEngine.cpp \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/cocos2dx_support/LuaFunctor.cpp \
	../../cocos2d-2.0-rc2-x-2.0.1/lua/cocos2dx_support/LJFMLuaLoader.cpp \

CLIENT_SPEEX_LIB := \
	../../dependencies/speex-1.2rc2/libspeex/bits.c \
	../../dependencies/speex-1.2rc2/libspeex/cb_search.c \
	../../dependencies/speex-1.2rc2/libspeex/exc_5_64_table.c \
	../../dependencies/speex-1.2rc2/libspeex/exc_5_256_table.c \
	../../dependencies/speex-1.2rc2/libspeex/exc_8_128_table.c \
	../../dependencies/speex-1.2rc2/libspeex/exc_10_16_table.c \
	../../dependencies/speex-1.2rc2/libspeex/exc_10_32_table.c \
	../../dependencies/speex-1.2rc2/libspeex/exc_20_32_table.c \
	../../dependencies/speex-1.2rc2/libspeex/filters.c \
	../../dependencies/speex-1.2rc2/libspeex/gain_table.c \
	../../dependencies/speex-1.2rc2/libspeex/gain_table_lbr.c \
	../../dependencies/speex-1.2rc2/libspeex/hexc_10_32_table.c \
	../../dependencies/speex-1.2rc2/libspeex/hexc_table.c \
	../../dependencies/speex-1.2rc2/libspeex/high_lsp_tables.c \
	../../dependencies/speex-1.2rc2/libspeex/kiss_fft.c \
	../../dependencies/speex-1.2rc2/libspeex/kiss_fftr.c \
	../../dependencies/speex-1.2rc2/libspeex/lpc.c \
	../../dependencies/speex-1.2rc2/libspeex/lsp.c \
	../../dependencies/speex-1.2rc2/libspeex/lsp_tables_nb.c \
	../../dependencies/speex-1.2rc2/libspeex/ltp.c \
	../../dependencies/speex-1.2rc2/libspeex/modes.c \
	../../dependencies/speex-1.2rc2/libspeex/modes_wb.c \
	../../dependencies/speex-1.2rc2/libspeex/nb_celp.c \
	../../dependencies/speex-1.2rc2/libspeex/quant_lsp.c \
	../../dependencies/speex-1.2rc2/libspeex/sb_celp.c \
	../../dependencies/speex-1.2rc2/libspeex/smallft.c \
	../../dependencies/speex-1.2rc2/libspeex/speex.c \
	../../dependencies/speex-1.2rc2/libspeex/speex_callbacks.c \
	../../dependencies/speex-1.2rc2/libspeex/speex_header.c \
	../../dependencies/speex-1.2rc2/libspeex/stereo.c \
	../../dependencies/speex-1.2rc2/libspeex/vbr.c \
	../../dependencies/speex-1.2rc2/libspeex/vorbis_psy.c \
	../../dependencies/speex-1.2rc2/libspeex/vq.c \
	../../dependencies/speex-1.2rc2/libspeex/window.c \

CLIENT_OGG_LIB := \
	../../dependencies/libogg-1.3.2/src/bitwise.c \
	../../dependencies/libogg-1.3.2/src/framing.c \

CLIENT_VORBIS_LIB := \
	../../dependencies/libvorbis-1.3.5/lib/analysis.c \
	../../dependencies/libvorbis-1.3.5/lib/bitrate.c \
	../../dependencies/libvorbis-1.3.5/lib/block.c \
	../../dependencies/libvorbis-1.3.5/lib/codebook.c \
	../../dependencies/libvorbis-1.3.5/lib/envelope.c \
	../../dependencies/libvorbis-1.3.5/lib/floor0.c \
	../../dependencies/libvorbis-1.3.5/lib/floor1.c \
	../../dependencies/libvorbis-1.3.5/lib/info.c \
	../../dependencies/libvorbis-1.3.5/lib/lookup.c \
	../../dependencies/libvorbis-1.3.5/lib/lpc.c \
	../../dependencies/libvorbis-1.3.5/lib/lsp.c \
	../../dependencies/libvorbis-1.3.5/lib/mapping0.c \
	../../dependencies/libvorbis-1.3.5/lib/mdct.c \
	../../dependencies/libvorbis-1.3.5/lib/psy.c \
	../../dependencies/libvorbis-1.3.5/lib/registry.c \
	../../dependencies/libvorbis-1.3.5/lib/res0.c \
	../../dependencies/libvorbis-1.3.5/lib/sharedbook.c \
	../../dependencies/libvorbis-1.3.5/lib/smallft.c \
	../../dependencies/libvorbis-1.3.5/lib/synthesis.c \
	../../dependencies/libvorbis-1.3.5/lib/vorbisenc.c \
	../../dependencies/libvorbis-1.3.5/lib/window.c \

LOCAL_SRC_FILES := \
	${CLIENT_MANAGER} \
	${CLIENT_BATTLE} \
	${CLIENT_FRAMEWORK} \
	${CLIENT_SCENEOBJ} \
	${CLIENT_GAMEDATA} \
	${CLIENT_GAMEUI} \
	${CLIENT_OGGENC} \
	${CLIENT_AMRENC} \
	${CLIENT_PROTOCOL} \
	${CLIENT_UTILS} \
	${CLIENT_LUA_ENGINE} \
	${CLIENT_SPEEX_LIB} \
	${CLIENT_OGG_LIB} \
	${CLIENT_VORBIS_LIB} \
	#${CLIENT_WEB} \

LOCAL_C_INCLUDES := $(LOCAL_PATH) \
	$(LOCAL_PATH)/Application  \
	$(LOCAL_PATH)/Application/Common \
	$(LOCAL_PATH)/Application/Manager \
	$(LOCAL_PATH)/Application/Battle \
	$(LOCAL_PATH)/Application/Framework \
	$(LOCAL_PATH)/Application/Framework/3rdplatform \
	$(LOCAL_PATH)/Application/SceneObj \
	$(LOCAL_PATH)/Application/GameTable \
	$(LOCAL_PATH)/Application/GameUI \
	$(LOCAL_PATH)/Application/oggenc \
	$(LOCAL_PATH)/Application/oggenc/include \
	$(LOCAL_PATH)/Application/ProtoDef \
	$(LOCAL_PATH)/Application/ProtoDef/rpcgen  \
	$(LOCAL_PATH)/Application/Utils \
	$(LOCAL_PATH)/../../dependencies/pcre-8.31/prj2 \
	$(LOCAL_PATH)/../../dependencies/freetype-2.4.9/include \
	$(LOCAL_PATH)/../../dependencies/SILLY-0.1.0/include \
	$(LOCAL_PATH)/../../dependencies/CEGUI/CEGUI/include \
	$(LOCAL_PATH)/../../dependencies/CEGUI/CEGUI/include/elements \
	$(LOCAL_PATH)/../../dependencies/speex-1.2rc2/include \
	$(LOCAL_PATH)/../../dependencies/speex-1.2rc2/include/speex \
	$(LOCAL_PATH)/../../dependencies/libogg-1.3.2/include \
	$(LOCAL_PATH)/../../dependencies/libvorbis-1.3.5/include \
	$(LOCAL_PATH)/../../dependencies/libvorbis-1.3.5/lib \
	$(LOCAL_PATH)/../../cocos2d-2.0-rc2-x-2.0.1/cocos2dx \
	$(LOCAL_PATH)/../../cocos2d-2.0-rc2-x-2.0.1/cocos2dx/include \
	$(LOCAL_PATH)/../../cocos2d-2.0-rc2-x-2.0.1/cocos2dx/platform \
	$(LOCAL_PATH)/../../cocos2d-2.0-rc2-x-2.0.1/cocos2dx/platform/android \
	$(LOCAL_PATH)/../../cocos2d-2.0-rc2-x-2.0.1/cocos2dx/platform/android/jni \
	$(LOCAL_PATH)/../../cocos2d-2.0-rc2-x-2.0.1/cocos2dx/kazmath/include \
	$(LOCAL_PATH)/../../cocos2d-2.0-rc2-x-2.0.1/CocosDenshion/include \
	$(LOCAL_PATH)/../../cocos2d-2.0-rc2-x-2.0.1/extensions \
	$(LOCAL_PATH)/../../cocos2d-2.0-rc2-x-2.0.1/external/libwebsockets/android/include \
	$(LOCAL_PATH)/../../cocos2d-2.0-rc2-x-2.0.1/external/curl/include/android \
	$(LOCAL_PATH)/../../cocos2d-2.0-rc2-x-2.0.1/lua \
	$(LOCAL_PATH)/../../cocos2d-2.0-rc2-x-2.0.1/lua/cocos2dx_support \
	$(LOCAL_PATH)/../../cocos2d-2.0-rc2-x-2.0.1/lua/lua \
	$(LOCAL_PATH)/../../cocos2d-2.0-rc2-x-2.0.1/lua/tolua \
	$(LOCAL_PATH)/../../cocos2d-2.0-rc2-x-2.0.1/extensions/libSpine/spine-cocos2dx/include \
	$(LOCAL_PATH)/../../cocos2d-2.0-rc2-x-2.0.1/extensions/libSpine/spine-c/include \
	$(LOCAL_PATH)/../../engine \
	$(LOCAL_PATH)/../../engine/common \
	$(LOCAL_PATH)/../../common/platform \
	$(LOCAL_PATH)/../../common/platform/android  \
	$(LOCAL_PATH)/../../common/platform/utils  \
	$(LOCAL_PATH)/../../common/cauthc \
	$(LOCAL_PATH)/../../common/cauthc/net \
	$(LOCAL_PATH)/../../common/cauthc/include \
	$(LOCAL_PATH)/../../common/cauthc/authc \
	$(LOCAL_PATH)/../../common/cauthc/authc/os/android \
	$(LOCAL_PATH)/../../common/ljfm/code/include \
	
LOCAL_LDLIBS := -llog \

LOCAL_WHOLE_STATIC_LIBRARIES := luajit_static

# define the macro to compile through support/zip_support/ioapi.c
LOCAL_CFLAGS := \
	-DUSE_FILE32API \
	-D_OS_IOS \
	-D_OS_ANDROID \
	-DXPP_IOS \
	-DANDROID \
	-DPUBLISHED_VERSION \
	-DCC_SUPPORT_PVRTC \
	-DFIXED_POINT \
	-D_MEIQIA_SDK_ \
	-D_LOCOJOY_SDK_ \
#	-D_YJ_SDK_ \

LOCAL_CPPFLAGS := -fexceptions -fpermissive

include $(BUILD_STATIC_LIBRARY)

$(call import-module, lua/luajit)

