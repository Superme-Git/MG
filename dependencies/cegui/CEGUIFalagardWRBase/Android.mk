LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := ceguifalagardwrbase_static

LOCAL_MODULE_FILENAME := libceguifalagardwrbase

LOCAL_SRC_FILES := \
	../CEGUI/src/WindowRendererSets/Falagard/FalAnimateText.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalAnimationButton.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalButton.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalCompnenttip.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalDefault.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalEditbox.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalEditboxProperties.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalFrameWindow.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalGroupBtnTree.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalIrregularButton.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalIrregularFigure.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalItemCell.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalItemCellGeneral.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalItemEntry.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalItemListbox.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalItemTable.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalLinkText.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalListHeader.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalListHeaderProperties.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalListHeaderSegment.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalListbox.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalMenuItem.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalMenubar.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalModule.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalMultiColumnList.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalMultiLineEditbox.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalMultiLineEditboxProperties.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalPopupMenu.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalProgressBar.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalProgressBarProperties.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalProgressBarTwoValue.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalRichEditbox.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalRichEditboxProperties.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalScrollablePane.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalScrollbar.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalScrollbarProperties.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalSkillBox.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalSlider.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalSliderProperties.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalSpecialTree.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalStatic.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalStaticImage.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalStaticImageProperties.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalStaticProperties.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalStaticText.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalStaticTextProperties.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalSwitch.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalSystemButton.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalTabButton.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalTabControl.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalTabControlProperties.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalTitlebar.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalToggleButton.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalToggleButtonExtStateImagery.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalTooltip.cpp \
	../CEGUI/src/WindowRendererSets/Falagard/FalTree.cpp \


LOCAL_C_INCLUDES := $(LOCAL_PATH)/../CEGUI/include \
	$(LOCAL_PATH)/../CEGUI/include/WindowRendererSets/Falagard \
	$(LOCAL_PATH)/../../freetype-2.4.9/include \
	$(LOCAL_PATH)/../../pcre-8.31/prj2 \
	$(LOCAL_PATH)/../../../cocos2d-2.0-rc2-x-2.0.1/cocos2dx \
	$(LOCAL_PATH)/../../../cocos2d-2.0-rc2-x-2.0.1/cocos2dx/include \
	$(LOCAL_PATH)/../../../cocos2d-2.0-rc2-x-2.0.1/cocos2dx/platform \
	$(LOCAL_PATH)/../../../cocos2d-2.0-rc2-x-2.0.1/cocos2dx/platform/android \
	$(LOCAL_PATH)/../../../cocos2d-2.0-rc2-x-2.0.1/cocos2dx/kazmath/include \
	$(LOCAL_PATH)/../../../common \
	$(LOCAL_PATH)/../../../common/platform \
	$(LOCAL_PATH)/../../../common/ljfm/code/include \


LOCAL_LDLIBS := -llog \

LOCAL_CFLAGS := -DPUBLISHED_VERSION \
	-DUSE_FILE32API \
	-D_OS_IOS \
	-D_OS_ANDROID \
	-DHAVE_CONFIG_H \
	-DCEGUI_STATIC \
	-DANDROID \
	-DCC_SUPPORT_PVRTC \


LOCAL_CPPFLAGS := -fexceptions -fpermissive

include $(BUILD_STATIC_LIBRARY)


