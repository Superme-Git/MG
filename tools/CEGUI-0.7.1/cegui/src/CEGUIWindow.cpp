/***********************************************************************
filename:   CEGUIWindow.cpp
created:    21/2/2004
author:     Paul D Turner

purpose:    Implements the Window base class
*************************************************************************/
/***************************************************************************
*   Copyright (C) 2004 - 2009 Paul D Turner & The CEGUI Development Team
*
*   Permission is hereby granted, free of charge, to any person obtaining
*   a copy of this software and associated documentation files (the
*   "Software"), to deal in the Software without restriction, including
*   without limitation the rights to use, copy, modify, merge, publish,
*   distribute, sublicense, and/or sell copies of the Software, and to
*   permit persons to whom the Software is furnished to do so, subject to
*   the following conditions:
*
*   The above copyright notice and this permission notice shall be
*   included in all copies or substantial portions of the Software.
*
*   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
*   EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
*   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
*   IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
*   OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
*   ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
*   OTHER DEALINGS IN THE SOFTWARE.
***************************************************************************/
#ifdef HAVE_CONFIG_H
#   include "config.h"
#endif

#include "CEGUIWindow.h"
#include "CEGUIExceptions.h"
#include "CEGUIWindowManager.h"
#include "CEGUISystem.h"
#include "CEGUIFontManager.h"
#include "CEGUIImagesetManager.h"
#include "CEGUIImageset.h"
#include "CEGUIMouseCursor.h"
#include "CEGUICoordConverter.h"
#include "CEGUIWindowRendererManager.h"
#include "elements/CEGUITooltip.h"
#include "falagard/CEGUIFalWidgetLookManager.h"
#include "falagard/CEGUIFalWidgetLookFeel.h"
#include "falagard/CEGUIFalWidgetComponent.h"
#include "CEGUIGeometryBuffer.h"
#include "CEGUIRenderingRoot.h"
#include "CEGUIRenderingContext.h"
#include "CEGUIRenderingWindow.h"
#include "CEGUIcolour.h"
#include "CEGUIPropertyHelper.h"
#include "CEGUIAnimationManager.h"
#include <algorithm>
#include <cmath>
#include <stdio.h>
#include "elements/CEGUIEditbox.h"
#include "gesture/CEGUILongPressGestureRecognizer.h"
#include "gesture/CEGUIPanGestureRecognizer.h"
#include "CEGUIAnimationInstance.h"
#if defined (CEGUI_USE_FRIBIDI)
#include "CEGUIFribidiVisualMapping.h"
#elif defined (CEGUI_USE_MINIBIDI)
#include "CEGUIMinibidiVisualMapping.h"
#else
#include "CEGUIBiDiVisualMapping.h"
#endif
#include "CEGUIPropertySet.h"
#include "CEGUIWindowFactoryManager.h"
#include "CEGUITextureTarget.h"

#include "CEGUICocos2DGeometryBuffer.h"


//#define NOTIFY_GESTURE(X) bool handled = false;\
//for (std::vector<CEGUI::Gesture::CEGUIGestureRecognizer*>::iterator iter = d_gestureRecognizers.begin(); iter != d_gestureRecognizers.end(); iter++) {\
//        if ((*iter)->X(e))\
//            handled = true;\
//}\
//if (handled) { e.handled++; return; }

// Start of CEGUI namespace section
namespace CEGUI
{
	//----------------------------------------------------------------------------//
	const String Window::EventNamespace("Window");
	const String Window::EventWindowUpdated("WindowUpdate");
	const String Window::EventParentSized("ParentSized");
	const String Window::EventSized("Sized");
	const String Window::EventMoved("Moved");
	const String Window::EventTextChanged("TextChanged");
	const String Window::EventFontChanged("FontChanged");
	const String Window::EventAlphaChanged("AlphaChanged");
	const String Window::EventIDChanged("IDChanged");
	const String Window::EventActivated("Activated");
	const String Window::EventDeactivated("Deactivated");
	const String Window::EventShown("Shown");
	const String Window::EventHidden("Hidden");
	const String Window::EventEnabled("Enabled");
	const String Window::EventDisabled("Disabled");
	const String Window::EventClippedByParentChanged("ClippingChanged");
	const String Window::EventDestroyedByParentChanged("DestroyedByParentChanged");
	const String Window::EventInheritsAlphaChanged("InheritAlphaChanged");
	const String Window::EventAlwaysOnTopChanged("AlwaysOnTopChanged");
	const String Window::EventTopMostChanged("TopMostChanged");
	const String Window::EventInputCaptureGained("CaptureGained");
	const String Window::EventInputCaptureLost("CaptureLost");
	const String Window::EventRenderingStarted("StartRender");
	const String Window::EventRenderingEnded("EndRender");
	const String Window::EventChildAdded("AddedChild");
	const String Window::EventChildRemoved("RemovedChild");
	const String Window::EventDestructionStarted("DestructStart");
	const String Window::EventZOrderChanged("ZChanged");
	const String Window::EventDragDropItemEnters("DragDropItemEnters");
	const String Window::EventDragDropItemLeaves("DragDropItemLeaves");
	const String Window::EventDragDropItemDropped("DragDropItemDropped");
	const String Window::EventVerticalAlignmentChanged("VerticalAlignmentChanged");
	const String Window::EventHorizontalAlignmentChanged("HorizontalAlignmentChanged");
	const String Window::EventWindowRendererAttached("WindowRendererAttached");
	const String Window::EventWindowRendererDetached("WindowRendererDetached");
	const String Window::EventRotated("Rotated");
	const String Window::EventNonClientChanged("NonClientChanged");
	const String Window::EventTextParsingChanged("TextParsingChanged");
	const String Window::EventMouseEnters("MouseEnter");
	const String Window::EventMouseLeaves("MouseLeave");
	const String Window::EventMouseMove("MouseMove");
	const String Window::EventMouseWheel("MouseWheel");
	const String Window::EventMouseButtonDown("MouseButtonDown");
	const String Window::EventMouseButtonUp("MouseButtonUp");
	const String Window::EventMouseClick("MouseClick");
	const String Window::EventMouseDoubleClick("MouseDoubleClick");
	const String Window::EventMouseTripleClick("MouseTripleClick");
	const String Window::EventLongPress("LongPress");
	const String Window::EventSlide("Slide");
	const String Window::EventDrag("Drag");
	const String Window::EventKeyDown("KeyDown");
	const String Window::EventKeyUp("KeyUp");
	const String Window::EventCharacterKey("CharacterKey");
	const String Window::EventDieTimeTick("DieTimeTick");
	const String Window::EventDisplaySizeChange("DisplaySizeChange");
	const String Window::EventAlignWindowMoved("AlignWindowMove");
	const String Window::EventGuideEnd("GuideEnd");
	const String Window::EventScreenAreaChange("ScreenAreaChange");
	const String Window::EventCreateWndEffectBegin("CreateWndEffectBegin");
	const String Window::EventCreateWndEffectEnd("CreateWndEffectEnd");
	const String Window::EventCloseWndEffectBegin("CloseWndEffectBegin");
	const String Window::EventCloseWndEffectEnd("CloseWndEffectEnd");
	const String Window::EventFlyToTargetPosition("FlyToTargetPosition");
	const String Window::EventKeyboardTargetWndChanged("KeyboardTargetWndChanged");

	//----------------------------------------------------------------------------//
	const String Window::TooltipNameSuffix("__auto_tooltip__");
	const String Window::AutoWidgetNameSuffix("__auto_");

	//----------------------------------------------------------------------------//
	Window* Window::d_captureWindow = 0;
	BasicRenderedStringParser Window::d_basicStringParser;
	DefaultRenderedStringParser Window::d_defaultStringParser;

	const float s_fMinOffset = 30.0f; //¥∞ø⁄◊Ó–°æ‡±ﬂ∆¡ƒª±ﬂæ‡¿�?

	//----------------------------------------------------------------------------//
	WindowProperties::Alpha             Window::d_alphaProperty;
	WindowProperties::AlwaysOnTop       Window::d_alwaysOnTopProperty;
	WindowProperties::TopMost           Window::d_topMostProperty;
	WindowProperties::ClippedByParent   Window::d_clippedByParentProperty;
	WindowProperties::DestroyedByParent Window::d_destroyedByParentProperty;
	WindowProperties::Disabled          Window::d_disabledProperty;
	WindowProperties::Font              Window::d_fontProperty;
	WindowProperties::ID                Window::d_IDProperty;
	WindowProperties::InheritsAlpha     Window::d_inheritsAlphaProperty;
	WindowProperties::MouseCursorImage  Window::d_mouseCursorProperty;
	WindowProperties::RestoreOldCapture Window::d_restoreOldCaptureProperty;
	WindowProperties::Text              Window::d_textProperty;
	WindowProperties::Visible           Window::d_visibleProperty;
	WindowProperties::ZOrderChangeEnabled   Window::d_zOrderChangeProperty;
	WindowProperties::WantsMultiClickEvents Window::d_wantsMultiClicksProperty;
	WindowProperties::MouseButtonDownAutoRepeat Window::d_autoRepeatProperty;
	WindowProperties::AutoRepeatDelay   Window::d_autoRepeatDelayProperty;
	WindowProperties::AutoRepeatRate    Window::d_autoRepeatRateProperty;
	WindowProperties::DistributeCapturedInputs Window::d_distInputsProperty;
	WindowProperties::CustomTooltipType Window::d_tooltipTypeProperty;
	WindowProperties::Tooltip           Window::d_tooltipProperty;
	WindowProperties::InheritsTooltipText Window::d_inheritsTooltipProperty;
	WindowProperties::RiseOnClick       Window::d_riseOnClickProperty;
	WindowProperties::VerticalAlignment   Window::d_vertAlignProperty;
	WindowProperties::HorizontalAlignment Window::d_horzAlignProperty;
	WindowProperties::UnifiedAreaRect   Window::d_unifiedAreaRectProperty;
	WindowProperties::UnifiedPosition   Window::d_unifiedPositionProperty;
	WindowProperties::UnifiedXPosition  Window::d_unifiedXPositionProperty;
	WindowProperties::UnifiedYPosition  Window::d_unifiedYPositionProperty;
	WindowProperties::UnifiedSize       Window::d_unifiedSizeProperty;
	WindowProperties::UnifiedWidth      Window::d_unifiedWidthProperty;
	WindowProperties::UnifiedHeight     Window::d_unifiedHeightProperty;
	WindowProperties::UnifiedMinSize    Window::d_unifiedMinSizeProperty;
	WindowProperties::UnifiedMaxSize    Window::d_unifiedMaxSizeProperty;
	WindowProperties::MousePassThroughEnabled   Window::d_mousePassThroughEnabledProperty;
	WindowProperties::WindowRenderer    Window::d_windowRendererProperty;
	WindowProperties::LookNFeel         Window::d_lookNFeelProperty;
	WindowProperties::DragDropTarget    Window::d_dragDropTargetProperty;
	WindowProperties::AutoRenderingSurface Window::d_autoRenderingSurfaceProperty;
	WindowProperties::Scale Window::d_scaleProperty;
	WindowProperties::Rotation Window::d_rotationProperty;
	WindowProperties::XRotation Window::d_xRotationProperty;
	WindowProperties::YRotation Window::d_yRotationProperty;
	WindowProperties::ZRotation Window::d_zRotationProperty;
	WindowProperties::NonClient Window::d_nonClientProperty;
	WindowProperties::TextParsingEnabled Window::d_textParsingEnabledProperty;
	WindowProperties::DisplaySizeChangePosEnabled Window::d_DisplaySizeChangePosEnabledProperty;
	WindowProperties::DragMoveEnable Window::d_DrageMoveEnabledProperty;
	WindowProperties::XPos Window::d_XPosProperty;
	WindowProperties::YPos Window::d_YPosProperty;
	WindowProperties::Width Window::d_WidthProperty;
	WindowProperties::Height Window::d_HeightProperty;
	WindowProperties::EnableSound Window::d_SoundEnableProperty;
	WindowProperties::SoundResource Window::d_SoundResProperty;
	WindowProperties::CloseSoundResource Window::d_CloseSoundResProperty;
	WindowProperties::LimitWindowSize Window::d_LimitWindowSizeProperty;
	WindowProperties::CloseIsHide Window::d_CloseIsHideProperty;
	WindowProperties::IsPixelDecide Window::d_IsPixelDecideProperty;
	WindowProperties::CreateEffectType Window::d_CreateEffectdProperty;
	WindowProperties::CloseEffectType Window::d_CloseEffectdProperty;
	WindowProperties::AllowModalStateClick Window::d_AllowModalSateProperty;
	WindowProperties::ModalState Window::d_ModalSateProperty;
	WindowProperties::AllowShowWithModalState Window::d_AllowShowWithModalStateProperty;

	//WindowProperties::DestroyDialog Window::d_DestroyDialogProperty;
	//WindowProperties::ModalStateAfterShow Window::d_CloseIsHideProperty;
#ifdef FORCEGUIEDITOR
	WindowProperties::LuaClass Window::d_LuaClassProperty;
	WindowProperties::LuaForDialog Window::d_LuaForDialogProperty;
	WindowProperties::LuaMemberName Window::d_LuaMemberNameProperty;
	WindowProperties::LuaEventOnClicked Window::d_LuaEventOnClickedProperty;
#endif
//----------------------------------------------------------------------------//
Window::Window(const String& type, const String& name) :
	d_type(type),
	d_name(name),
	d_autoWindow(d_name.rfind(AutoWidgetNameSuffix) != String::npos),

	// basic state
	d_initialising(false),
	d_destructionStarted(false),
	d_enabled(true),
	d_visible(true),
	d_drawIt(true),
	d_active(false),

	// parent related fields
	d_parent(0),
	d_destroyedByParent(true),

	// clipping options
	d_clippedByParent(true),
	d_nonClientContent(false),

	// rendering components and options
	d_windowRenderer(0),
	d_geometry(&System::getSingleton().getRenderer()->createGeometryBuffer()),
	d_surface(0),
	d_needsRedraw(true),
	d_autoRenderingWindow(false),
	d_mouseCursor((const Image*)DefaultMouseCursor),

	// alpha transparency set up
	d_alpha(1.0f),
	d_inheritsAlpha(true),

	// mouse input capture set up
	d_oldCapture(0),
	d_restoreOldCapture(false),
	d_distCapturedInputs(false),

	// text system set up
	d_font(0),
	#ifndef CEGUI_BIDI_SUPPORT
	d_bidiVisualMapping(0),
	#elif defined (CEGUI_USE_FRIBIDI)
	d_bidiVisualMapping(new FribidiVisualMapping),
	#elif defined (CEGUI_USE_MINIBIDI)
	d_bidiVisualMapping(new MinibidiVisualMapping),
	#else
	#error "BIDI Configuration is inconsistant, check your config!"
	#endif
	d_bidiDataValid(false),
	d_renderedStringValid(false),
	d_customStringParser(0),
	d_textParsingEnabled(true),

	// user specific data
	d_ID(0),
	d_ID2(-1),
	d_userData(0),

	// z-order related options
	d_alwaysOnTop(false),
	d_topMost(false),
	d_riseOnClick(true),
	d_zOrderingEnabled(true),
	d_alwsyaOnBottom(false),

	// mouse input options
	d_wantsMultiClicks(false),
	d_mousePassThroughEnabled(false),
	d_autoRepeat(false),
	d_repeatDelay(0.3f),
	d_repeatRate(0.06f),
	d_dieTime(0),
	d_dieing(false),
	d_limitSize(true),
	d_repeatButton(NoButton),
	d_repeating(false),
	d_repeatElapsed(0.0f),

	// drag and drop
	d_dragDropTarget(true),

	// tool tip related
	d_customTip(0),
	d_weOwnTip(false),
	d_inheritsTipText(false),

	// XML writing options
	d_allowWriteXML(true),

	// Window position, size, aligment, etc...
	d_area(cegui_reldim(0), cegui_reldim(0), cegui_reldim(0), cegui_reldim(0)),
	d_pixelSize(0.0f, 0.0f),
	d_minSize(cegui_reldim(0), cegui_reldim(0)),
	d_maxSize(cegui_reldim(1), cegui_reldim(1)),
	d_horzAlign(HA_LEFT),
	d_vertAlign(VA_TOP),
	d_rotation(0.0f, 0.0f, 0.0f),
	d_scale(1.0f, 1.0f, 1.0),

	// cached pixel rect validity flags
	d_outerUnclippedRectValid(false),
	d_innerUnclippedRectValid(false),
	d_outerRectClipperValid(false),
	d_innerRectClipperValid(false),
	d_hitTestRectValid(false),

	d_BeingDrag(false),
	d_CanEdit(false),

	d_TimeAutoClose(false),
	d_LifeTime(4.0f),
	d_ElapseTime(0.0f),
	d_clipWidth(1.0f),

	d_TextBorder(false),

	d_DisplaySizeEnable(false),
	d_InParentWndCenter(false),
	d_Flash(false),
	d_FalshElapseTime(0.0f),
	d_FlashFrequence(1.5f),

	d_EscClose(false),
	d_DragMoveEnable(false),
	d_DisplaySizeChangePosEnable(true),

	d_AlignWindow(0),
	d_WndAlignType(WA_None),
	d_InChatOutWnd(false),
	d_HasGuide(false),
	d_UIShiedIsShow(false),
	d_HandleDragMove(false),

	d_RButtonCloseEnable(true),

	d_OldVisable(true),
	d_OnShiedRootWnd(true),

	d_oldSize(0.0f, 0.0f),
	
	d_AutoSizeWithParent(false),
	
	d_EnableFlash(true),
	d_SoundEnable(true),
	d_SoundResource(""),
	d_CloseSoundResource(""),
	
	d_SlideEnable(false),
	d_DragEnable(false),
	d_LongpressEnable(false),

	d_EffectState(WndEffectState_None),

	d_CreateWndType(CreateWndEffect_None),
	d_CloseWndType(CloseWndEffect_None),

	d_WndEffectElaseTime(0.0f),
	d_ModalStateDrawEffect(true),
	
	d_AllowModalSate(false),
	d_ModalStateAfterShow(false),
	d_AllowShowWithModalState(false),
	d_IsFlying(false), //是否在飞行中
	d_FlyStartScreenPoint(0.0f, 0.0f), //起始点的屏幕坐标
	d_FlyTargetScreenPoint(0.0f, 0.0f), //目标点的屏幕坐标
	d_TotalFlyTime(1.0f), //总的飞行时间
	d_FlyElapseTime(0.0f), //已经飞行的时�?

	d_ClickStateScale(1.0f),

	d_CloseIsHide(false),
	d_IsPixelDecide(false),

#ifdef FORCEGUIEDITOR
	d_LuaClassName(""),
	d_LuaForDialog(true),
	d_LuaMemberName(""),
	d_LuaEventOnClicked(""),
#endif // FORCEGUIEDITOR

	d_IsLoadedDraw(false),

	d_MouseButtonDownEventId(-1),

	d_bUpMsgChangedGesture(false),
	
	d_tooltipText("") //yanji 20150917
{
	d_recognizerManager = new Gesture::CEGUIGestureRecognizerManager;
	// add properties
	addStandardProperties();
#ifdef _EDITOR_
	if (isAutoWindow())
	{
		d_AutoSizeWithParent = true;
	}
#endif
}

Window::Window(const String& type) :
	d_type(type),
	d_ID(0),
	d_ID2(-1),
	d_userData(0),
	d_SoundResource("")
{
	d_recognizerManager = new Gesture::CEGUIGestureRecognizerManager;

}

Window::Window(const Window& wnd)
	:d_ID(0),
	d_ID2(-1),
	d_userData(0),
	d_recognizerManager(NULL),
	d_SoundResource("")
{
	*this = wnd;
}

bool Window::onRenameTemplatePrefix(const String& sPrefix)
{
	if (d_parent)
		d_parent = this->getCloneWindowFromTemplate(d_parent, sPrefix.c_str());
	if (d_AlignWindow)
		d_AlignWindow = this->getCloneWindowFromTemplate(d_AlignWindow, sPrefix.c_str());

	WindowFactoryManager& wfMgr = WindowFactoryManager::getSingleton();
	if (d_windowRenderer)
	{
		const char* sRendererName = d_windowRenderer->getName().c_str();
		WindowRenderer* templateRenderer = d_windowRenderer;
		d_windowRenderer = NULL;
		setWindowRenderer(sRendererName,true);
		d_windowRenderer->clone(templateRenderer);
		//setLookNFeel(d_lookName,true);
	}

	for (size_t i = 0; i < this->getChildCount(); ++i)
	{
		Window* wnd = this->getChildAtIdx(i);
		if (!wnd->onRenameTemplatePrefix(sPrefix))
			return false;
	}
	return true;
}

bool Window::onSetTemplateLookNFeel()
{
	if (d_windowRenderer)
	{
		setLookNFeel(d_lookName, true);
	}
	for (size_t i = 0; i < this->getChildCount(); ++i)
	{
		Window* wnd = this->getChildAtIdx(i);
		if (!wnd->onSetTemplateLookNFeel())
			return false;
	}
	return true;
}

const Window& Window::operator=(const Window& t)
{
	d_properties = t.d_properties;
	d_baseProperties = t.d_baseProperties;

	const char* sTest = t.getName().c_str();
	d_name = t.d_name;
	d_falagardType = t.d_falagardType;
	d_autoWindow = t.d_autoWindow;
	d_initialising = t.d_initialising;
	d_destructionStarted = t.d_destructionStarted;
	d_enabled = t.d_enabled;
	d_visible = t.d_visible;
	d_drawIt = t.d_drawIt;
	d_active = t.d_active;
	//d_drawList addChild的时�?已经初始化了.
	d_parent = t.d_parent;//需要重新关�?
	d_destroyedByParent = t.d_destroyedByParent;
	d_clippedByParent = t.d_clippedByParent;
	d_nonClientContent = t.d_nonClientContent;
	d_lookName = t.d_lookName;
	d_windowRenderer = t.d_windowRenderer;
	d_geometry = &System::getSingleton().getRenderer()->createGeometryBuffer();
	assert(t.d_surface==NULL);
	d_surface = NULL;
	d_needsRedraw = t.d_needsRedraw;
	d_autoRenderingWindow = t.d_autoRenderingWindow;
	d_mouseCursor = t.d_mouseCursor;
	d_alpha = t.d_alpha;
	d_inheritsAlpha = t.d_inheritsAlpha;
	assert(t.d_oldCapture==NULL);
	d_oldCapture = NULL;
	d_restoreOldCapture = t.d_restoreOldCapture;
	d_distCapturedInputs = t.d_distCapturedInputs;
	d_font = t.d_font;
	d_textLogical = t.d_textLogical;
	assert(t.d_bidiVisualMapping == NULL);
	d_bidiVisualMapping = NULL;
	d_bidiDataValid = t.d_bidiDataValid;
	d_renderedString = t.d_renderedString;
	d_renderedStringValid = t.d_renderedStringValid;
	assert(t.d_customStringParser == NULL);
	d_customStringParser = NULL;
	d_textParsingEnabled = t.d_textParsingEnabled;
	d_ID = t.d_ID;
	d_ID2 = t.d_ID2;
	//assert(t.d_userData == NULL);
	d_userData = t.d_userData;
	d_userStrings = t.d_userStrings;
	d_alwaysOnTop = t.d_alwaysOnTop;
	d_topMost = t.d_topMost;
	d_riseOnClick = t.d_riseOnClick;
	d_zOrderingEnabled = t.d_zOrderingEnabled;
	d_alwsyaOnBottom = t.d_alwsyaOnBottom;
	d_wantsMultiClicks = t.d_wantsMultiClicks;
	d_mousePassThroughEnabled = t.d_mousePassThroughEnabled;
	d_autoRepeat = t.d_autoRepeat;
	d_repeatDelay = t.d_repeatDelay;
	d_repeatRate = t.d_repeatRate;
	d_dieTime = t.d_dieTime;
	d_dieing = t.d_dieing;
	d_limitSize = t.d_limitSize;
	d_repeatButton = t.d_repeatButton;
	d_repeating = t.d_repeating;
	d_repeatElapsed = t.d_repeatElapsed;
	d_dragDropTarget = t.d_dragDropTarget;
	d_tooltipText = t.d_tooltipText;
	assert(t.d_customTip == NULL);
	d_customTip = NULL;
	d_weOwnTip = t.d_weOwnTip;
	d_inheritsTipText = t.d_inheritsTipText;
	d_allowWriteXML = t.d_allowWriteXML;
#ifdef FORCEGUIEDITOR
	d_bannedXMLProperties = t.d_bannedXMLProperties;
#endif
	d_area = t.d_area;
	d_pixelSize = t.d_pixelSize;
	d_minSize = t.d_minSize;
	d_maxSize = t.d_maxSize;
	d_horzAlign = t.d_horzAlign;
	d_vertAlign = t.d_vertAlign;
	d_rotation = t.d_rotation;
	d_scale = t.d_scale;
	d_outerUnclippedRect = t.d_outerUnclippedRect;
	d_innerUnclippedRect = t.d_innerUnclippedRect;
	d_outerRectClipper = t.d_outerRectClipper;
	d_innerRectClipper = t.d_innerRectClipper;
	d_hitTestRect = t.d_hitTestRect;
	d_outerUnclippedRectValid = t.d_outerUnclippedRectValid;
	d_innerUnclippedRectValid = t.d_innerUnclippedRectValid;
	d_outerRectClipperValid = t.d_outerRectClipperValid;
	d_innerRectClipperValid = t.d_innerRectClipperValid;
	d_hitTestRectValid = t.d_hitTestRectValid;
	d_BeingDrag = t.d_BeingDrag;
	d_CanEdit = t.d_CanEdit;
	d_TimeAutoClose = t.d_TimeAutoClose;
	d_LifeTime = t.d_LifeTime;
	d_ElapseTime = t.d_ElapseTime;
	d_clipWidth = t.d_clipWidth;
	d_TextBorder = t.d_TextBorder;
	d_TextBorderColour = t.d_TextBorderColour;
	d_DisplaySizeEnable = t.d_DisplaySizeEnable;
	d_InParentWndCenter = t.d_InParentWndCenter;
	d_Flash = t.d_Flash;
	d_FalshElapseTime = t.d_FalshElapseTime;
	d_FlashFrequence = t.d_FlashFrequence;
	d_EscClose = t.d_EscClose;
	d_DragMoveEnable = t.d_DragMoveEnable;
	d_DisplaySizeChangePosEnable = t.d_DisplaySizeChangePosEnable;
	assert(t.d_AlignWindow == NULL);
	d_AlignWindow = NULL;
	d_WndAlignType = t.d_WndAlignType;
	d_InChatOutWnd = t.d_InChatOutWnd;
	d_HasGuide = t.d_HasGuide;
	d_UIShiedIsShow = t.d_UIShiedIsShow;
	d_HandleDragMove = t.d_HandleDragMove;
	d_RButtonCloseEnable = t.d_RButtonCloseEnable;
	d_OldVisable = t.d_OldVisable;
	d_OnShiedRootWnd = t.d_OnShiedRootWnd;
	d_oldSize = t.d_oldSize;
	d_AutoSizeWithParent = t.d_AutoSizeWithParent;
	d_EnableFlash = t.d_EnableFlash;
	d_SoundEnable = t.d_SoundEnable;
	d_SoundResource = t.d_SoundResource;
	d_CloseSoundResource = t.d_CloseSoundResource;
	d_SlideEnable = t.d_SlideEnable;
	d_DragEnable = t.d_DragEnable;
	d_LongpressEnable = t.d_LongpressEnable;
	d_EffectState = t.d_EffectState;
	d_CreateWndType = t.d_CreateWndType;
	d_CloseWndType = t.d_CloseWndType;
	d_WndEffectElaseTime = t.d_WndEffectElaseTime;
	d_ModalStateDrawEffect = t.d_ModalStateDrawEffect;
	d_AllowModalSate = t.d_AllowModalSate;
	d_ModalStateAfterShow = t.d_ModalStateAfterShow;
	d_AllowShowWithModalState = t.d_AllowShowWithModalState;
	d_IsFlying = t.d_IsFlying;
	d_FlyStartScreenPoint = t.d_FlyStartScreenPoint;
	d_FlyTargetScreenPoint = t.d_FlyTargetScreenPoint;
	d_TotalFlyTime = t.d_TotalFlyTime;
	d_FlyElapseTime = t.d_FlyElapseTime;
	d_ClickStateScale = t.d_ClickStateScale;
	d_CloseIsHide = t.d_CloseIsHide;
	#ifdef FORCEGUIEDITOR
	d_LuaClassName = t.d_LuaClassName;
	d_LuaForDialog = t.d_LuaForDialog;
	d_LuaMemberName = t.d_LuaMemberName;
	d_LuaEventOnClicked = t.d_LuaEventOnClicked;
	#endif // FORCEGUIEDITOR
	d_bUpMsgChangedGesture = t.d_bUpMsgChangedGesture;
	d_IsLoadedDraw = t.d_IsLoadedDraw;

	for (size_t i = 0; i < t.d_children.size(); i++)
	{
		Window* childWnd = t.d_children[i]->clone(NULL);
		addChildWindow(childWnd);
	}

	/*
		d_children(d_children),
		d_drawList
		d_bidiVisualMapping(d_bidiVisualMapping),
		*/
	return *this;
	/*
	d_name = t.d_name;
	d_autoWindow = t.d_autoWindow;
	d_initialising = t.d_initialising;
	d_destructionStarted = t.d_destructionStarted;
	d_enabled = t.d_enabled;
	d_visible = t.d_visible;
	d_drawIt = t.d_drawIt;
	d_active = t.d_active;
	d_parent = t.d_parent;
	d_destroyedByParent = t.d_destroyedByParent;
	d_clippedByParent = t.d_clippedByParent;
	d_nonClientContent = t.d_nonClientContent;
	d_windowRenderer = t.d_windowRenderer;
	d_geometry = t.d_geometry;
	d_surface = t.d_surface;
	d_needsRedraw = t.d_needsRedraw;
	d_autoRenderingWindow = t.d_autoRenderingWindow;
	d_mouseCursor = t.d_mouseCursor;
	d_alpha = t.d_alpha;
	d_inheritsAlpha = t.d_inheritsAlpha;
	d_oldCapture = t.d_oldCapture;
	d_restoreOldCapture = t.d_restoreOldCapture;
	d_distCapturedInputs = t.d_distCapturedInputs;
	d_font = t.d_font;
#ifndef CEGUI_BIDI_SUPPORT
	d_bidiVisualMapping = t.d_bidiVisualMapping;
#elif defined (CEGUI_USE_FRIBIDI)
	d_bidiVisualMapping(new FribidiVisualMapping),
#elif defined (CEGUI_USE_MINIBIDI)
	d_bidiVisualMapping(new MinibidiVisualMapping),
#else
#error "BIDI Configuration is inconsistant, check your config!"
#endif
	d_bidiDataValid = t.d_bidiDataValid;
	d_renderedStringValid = t.d_renderedStringValid;
	d_customStringParser = t.d_customStringParser;
	d_textParsingEnabled = t.d_textParsingEnabled;
	d_ID = t.d_ID;
	d_userData = t.d_userData;
	d_alwaysOnTop = t.d_alwaysOnTop;
	d_riseOnClick = t.d_riseOnClick;
	d_zOrderingEnabled = t.d_zOrderingEnabled;
	d_topMost = t.d_topMost;
	d_wantsMultiClicks = t.d_wantsMultiClicks;
	d_mousePassThroughEnabled = t.d_mousePassThroughEnabled;
	d_autoRepeat = t.d_autoRepeat;
	d_repeatDelay = t.d_repeatDelay;
	d_repeatRate = t.d_repeatRate;
	d_repeatButton = t.d_repeatButton;
	d_repeating = t.d_repeating;
	d_repeatElapsed = t.d_repeatElapsed;
	d_dragDropTarget = t.d_dragDropTarget;
	d_customTip = t.d_customTip;
	d_weOwnTip = t.d_weOwnTip;
	d_inheritsTipText = t.d_inheritsTipText;
	d_allowWriteXML = t.d_allowWriteXML;
	d_area = t.d_area;
	d_pixelSize = t.d_pixelSize;
	d_oldSize = t.d_oldSize;
	d_minSize = t.d_minSize;
	d_maxSize = t.d_maxSize;
	d_horzAlign = t.d_horzAlign;
	d_vertAlign = t.d_vertAlign;
	d_rotation = t.d_rotation;
	d_scale = t.d_scale;
	d_outerUnclippedRectValid = t.d_outerUnclippedRectValid;
	d_innerUnclippedRectValid = t.d_innerUnclippedRectValid;
	d_outerRectClipperValid = t.d_outerRectClipperValid;
	d_innerRectClipperValid = t.d_innerRectClipperValid;
	d_hitTestRectValid = t.d_hitTestRectValid;
	d_TimeAutoClose = t.d_TimeAutoClose;
	d_ElapseTime = t.d_ElapseTime;
	d_LifeTime = t.d_LifeTime;
	d_CanEdit = t.d_CanEdit;
	d_DisplaySizeEnable = t.d_DisplaySizeEnable;
	d_InParentWndCenter = t.d_InParentWndCenter;
	d_Flash = t.d_Flash;
	d_FalshElapseTime = t.d_FalshElapseTime;
	d_EscClose = t.d_EscClose;
	d_DragMoveEnable = t.d_DragMoveEnable;
	d_DisplaySizeChangePosEnable = t.d_DisplaySizeChangePosEnable;
	d_BeingDrag = t.d_BeingDrag;
	d_dieTime = t.d_dieTime;
	d_dieing = t.d_dieing;
	d_limitSize = t.d_limitSize;
	d_AlignWindow = t.d_AlignWindow;
	d_WndAlignType = t.d_WndAlignType;
	d_InChatOutWnd = t.d_InChatOutWnd;
	d_HasGuide = t.d_HasGuide;
	d_UIShiedIsShow = t.d_UIShiedIsShow;
	d_HandleDragMove = t.d_HandleDragMove;
	d_OldVisable = t.d_OldVisable;
	d_OnShiedRootWnd = t.d_OnShiedRootWnd;
	d_AutoSizeWithParent = t.d_AutoSizeWithParent;
	d_clipWidth = t.d_clipWidth;
	d_EnableFlash = t.d_EnableFlash;
	d_SoundEnable = t.d_SoundEnable;
	d_SoundResource = t.d_SoundResource;
	d_CloseSoundResource = t.d_CloseSoundResource;
	d_alwsyaOnBottom = t.d_alwsyaOnBottom;
	d_FlashFrequence = t.d_FlashFrequence;
	d_RButtonCloseEnable = t.d_RButtonCloseEnable;
	d_SlideEnable = t.d_SlideEnable;
	d_DragEnable = t.d_DragEnable;
	d_LongpressEnable = t.d_LongpressEnable;
	d_CreateWndType = t.d_CreateWndType;
	d_CloseWndType = t.d_CloseWndType;
	d_WndEffectElaseTime = t.d_WndEffectElaseTime;
	d_ModalStateDrawEffect = t.d_ModalStateDrawEffect;
	d_EffectState = t.d_EffectState;
	d_AllowModalSate = t.d_AllowModalSate;
	d_ModalStateAfterShow = t.d_ModalStateAfterShow;
	d_AllowShowWithModalState = t.d_AllowShowWithModalState;
	d_IsFlying = t.d_IsFlying;
	d_FlyStartScreenPoint = t.d_FlyStartScreenPoint;
	d_FlyTargetScreenPoint = t.d_FlyTargetScreenPoint;
	d_TotalFlyTime = t.d_TotalFlyTime;
	d_FlyElapseTime = t.d_FlyElapseTime;
	d_bUpMsgChangedGesture = t.d_bUpMsgChangedGesture;
	d_CloseIsHide = t.d_CloseIsHide;
	d_tooltipText = t.d_tooltipText;
#ifdef FORCEGUIEDITOR
	d_LuaClassName = t.d_LuaClassName;
	d_LuaForDialog = t.d_LuaForDialog;
	d_LuaMemberName=t.d_LuaMemberName;
	d_LuaEventOnClicked=t.d_LuaEventOnClicked;
#endif // FORCEGUIEDITOR
	d_ClickStateScale = t.d_ClickStateScale;
	d_OnCreateAnimationInstance = t.d_OnCreateAnimationInstance;
	d_OnCreateAnimationDeltaTime = t.d_OnCreateAnimationDeltaTime;
	d_OnCreateEffectInstance = t.d_OnCreateEffectInstance;
	d_OnCreateEffectDeltaTime = t.d_OnCreateEffectDeltaTime;
	d_OnCreateEffectCycle = t.d_OnCreateEffectCycle;
	d_OnCloseAnimationInstance = t.d_OnCloseAnimationInstance;
	d_OnCloseAnimationDeltaTime = t.d_OnCloseAnimationDeltaTime;
	d_OnCloseEffectInstance = t.d_OnCloseEffectInstance;
	d_OnCloseEffectDeltaTime = t.d_OnCloseEffectDeltaTime;

	// yangyinyu add�?
	d_falagardType = t.d_falagardType;
	d_lookName = t.d_lookName;
	d_textLogical = t.d_textLogical;
	d_renderedString = t.d_renderedString;
	d_basicStringParser = t.d_basicStringParser;
	d_defaultStringParser = t.d_defaultStringParser;
	d_ID2 = t.d_ID2;
	d_userStrings = t.d_userStrings;
	d_riseOnClick = t.d_riseOnClick;
	d_alwsyaOnBottom = t.d_alwsyaOnBottom;
	d_dieTime = t.d_dieTime;
	d_dieing = t.d_dieing;
	d_limitSize = t.d_limitSize;
	d_tooltipText = t.d_tooltipText;
	d_hitTestRect = t.d_hitTestRect;
	d_TextBorder = t.d_TextBorder;
	d_TextBorderColour = t.d_TextBorderColour;

	// base class.
	d_properties = t.d_properties;

	// 创建子窗�?
	for (size_t i = 0; i < t.d_children.size(); i++)
	{
		Window* childWnd = t.d_children[i]->clone(NULL);
		addChildWindow(childWnd);
	}
	// d_drawList addChild的时�?已经初始化了.

	return *this;
	*/
}
/*
	根据模板窗口获取克隆后的窗口.
*/
Window* Window::getCloneWindowFromTemplate(Window* templateWnd,const char* cloneWndPrifex)
{
	if (templateWnd == NULL)
	{
		return templateWnd;
	}
	std::string templateWndName = templateWnd->getName().c_str();
	static size_t templateNamePrifexLength = strlen("@template@");
	if (templateWndName.length() < templateNamePrifexLength)
	{
		return templateWnd;
	}
	if (memcmp(templateWndName.c_str(), "@template@", templateNamePrifexLength))
	{
		return templateWnd;
	}
	const char* wndName = &templateWndName[templateNamePrifexLength];
	std::string cloneWndName = std::string(cloneWndPrifex) + wndName;
	try
	{
		return WindowManager::getSingleton().getWindow(cloneWndName);
	}
	catch (UnknownObjectException&)
	{
#if defined(WIN32) && _DEBUG
		char debugText[256];
		sprintf(debugText, "find window %s failed!", cloneWndName);
		assert(debugText);
#endif
		return templateWnd;
	}
}
/*
	克隆一个窗�?
*/
Window* Window::clone(Window* wnd)
{
	Window* retWnd = wnd;
	if (retWnd == NULL)
		retWnd = new Window(d_type);
#ifdef _DEBUG
	std::string sType = d_type.c_str();
	assert(d_type == "Window");
#endif
	*retWnd = *this;

	return retWnd;
}

//----------------------------------------------------------------------------//
Window::~Window(void)
{
	// most cleanup actually happened earlier in Window::destroy.

	RenderingContext ctx;
	getRenderingContext(ctx);
	if (ctx.surface)
	{
		// ycl destroyGeometryBuffer ֮ǰ����ȷ���� GeometryBuffer �Ѿ��� RenderQueue ���Ƴ���
		ctx.surface->removeGeometryBuffer(ctx.queue, d_geometry);
	}

	System::getSingleton().getRenderer()->destroyGeometryBuffer(d_geometry);
	delete d_bidiVisualMapping;
	delete d_recognizerManager;
	//    std::vector<Gesture::CEGUIGestureRecognizer*>::iterator iter = d_gestureRecognizers.begin();
	//    for (; iter != d_gestureRecognizers.end(); iter++) {
	//        delete *iter;
	//        *iter = 0;
	//    }
	//    d_gestureRecognizers.clear();
}

//----------------------------------------------------------------------------//
const String& Window::getType(void) const
{
	return d_falagardType.empty() ? d_type : d_falagardType;
}

//----------------------------------------------------------------------------//
bool Window::isDisabled(bool localOnly) const
{
	/* const bool parent_disabled =
	(!d_parent || localOnly) ? false : d_parent->isDisabled();

	return !d_enabled || parent_disabled;*/
	if (!d_enabled)
	{
		return true;
	}
	if (!d_parent || localOnly)
	{
		return !d_enabled;

	}

	Window* pParent = d_parent;
	bool bParentDisable = false;
	while (pParent != NULL)
	{
		bParentDisable = !pParent->GetEnableLocal();
		if (bParentDisable)
		{
			return true;
		}
		pParent = pParent->getParent();
	}
	return bParentDisable;
}

//----------------------------------------------------------------------------//
bool Window::isVisible(bool localOnly) const
{
	/*const bool parent_visible =
	(!d_parent || localOnly) ? true : d_parent->isVisible();

	return d_visible && parent_visible;*/
	if (!d_visible)
	{
		return false;
	}
	if (!d_parent || localOnly)
	{
		return d_visible;
	}

	//return d_parent->isVisible();
	Window* pParent = d_parent;
	bool bParentVis = false;
	while (pParent != NULL)
	{
		bParentVis = pParent->GetVisbleLocal();
		if (!bParentVis)
		{
			return false;
		}
		pParent = pParent->getParent();
	}
	return bParentVis;
}

//----------------------------------------------------------------------------//
bool Window::isActive(void) const
{
	/* const bool parent_active = !d_parent ? true : d_parent->isActive();

	return d_active && parent_active;*/
	if (!d_active)
	{
		return false;
	}
	if (!d_parent)
	{
		return d_active;
	}

	Window* pParent = d_parent;
	bool bParentActive = false;
	while (pParent != NULL)
	{
		bParentActive = pParent->GetActiveLocal();
		if (!bParentActive)
		{
			return false;
		}
		pParent = pParent->getParent();
	}
	return bParentActive;
}

//----------------------------------------------------------------------------//
bool Window::isChild(const String& name) const
{
	const size_t child_count = getChildCount();

	for (size_t i = 0; i < child_count; ++i)
		if (d_children[i]->getName() == name)
			return true;

	return false;
}

//----------------------------------------------------------------------------//
bool Window::isChild(uint ID) const
{
	const size_t child_count = getChildCount();

	for (size_t i = 0; i < child_count; ++i)
		if (d_children[i]->getID() == ID)
			return true;

	return false;
}

//----------------------------------------------------------------------------//
bool Window::isChildRecursive(uint ID) const
{
	const size_t child_count = getChildCount();

	for (size_t i = 0; i < child_count; ++i)
		if (d_children[i]->getID() == ID || d_children[i]->isChildRecursive(ID))
			return true;

	return false;
}

//----------------------------------------------------------------------------//
bool Window::isChild(const Window* window) const
{
	const size_t child_count = getChildCount();

	for (size_t i = 0; i < child_count; ++i)
		if (d_children[i] == window)
			return true;

	return false;
}

//----------------------------------------------------------------------------//
Window* Window::getChild(const String& name) const
{
	const size_t child_count = getChildCount();

	for (size_t i = 0; i < child_count; ++i)
		if (d_children[i]->getName() == name)
			return d_children[i];

	CEGUI_LOGERR("Window::getChild - The Window object named '"
		+ name + "' is not attached to Window '" +
		d_name + "'.\n");
	throw UnknownObjectException("Window::getChild - The Window object named '"
		+ name + "' is not attached to Window '" +
		d_name + "'.");
}

//----------------------------------------------------------------------------//
Window* Window::getChild(uint ID) const
{
	const size_t child_count = getChildCount();

	for (size_t i = 0; i < child_count; ++i)
		if (d_children[i]->getID() == ID)
			return d_children[i];

	return NULL;

	char strbuf[16];
	sprintf(strbuf, "%X", ID);
	CEGUI_LOGERR("Window::getChild: A Window with ID: '" +
		std::string(strbuf) + "' is not attached to Window '" + d_name + "'.\n");
	throw UnknownObjectException("Window::getChild: A Window with ID: '" +
		std::string(strbuf) + "' is not attached to Window '" + d_name + "'.");
}

//----------------------------------------------------------------------------//
Window* Window::getChildRecursive(const String& name) const
{
	const size_t child_count = getChildCount();

	for (size_t i = 0; i < child_count; ++i)
	{
		const String childName(d_children[i]->getName());
		if (childName == name)
			return d_children[i];

		Window* tmp = d_children[i]->getChildRecursive(name);
		if (tmp)
			return tmp;
	}

	return 0;
}

//----------------------------------------------------------------------------//
Window* Window::getChildRecursive(uint ID) const
{
	const size_t child_count = getChildCount();

	for (size_t i = 0; i < child_count; ++i)
	{
		if (d_children[i]->getID() == ID)
			return d_children[i];

		Window* tmp = d_children[i]->getChildRecursive(ID);
		if (tmp)
			return tmp;
	}

	return 0;
}

//----------------------------------------------------------------------------//
Window* Window::getActiveChild(void)
{
	return const_cast<Window*>(
		static_cast<const Window*>(this)->getActiveChild());
}

Window* Window::geKeyboardTargetChild(void)   //ªÒµ√º¸≈Ã ‰»Î≤∂ªÒ¥∞ø�?
{
	size_t pos = getChildCount();

	while (pos-- > 0)
	{
		Window* pKeyBoardTargetWnd = d_drawList[pos]->geKeyboardTargetChild();
		if (pKeyBoardTargetWnd != NULL)
			return pKeyBoardTargetWnd;
	}
	if (d_CanEdit)
	{
		return this;
	}
	else
	{
		return NULL;
	}


}

//µ√µΩ∞¥escº¸ÕÀ≥ˆµƒ¥∞ø⁄£¨÷˜“™ «guisheetµ˜”√
Window* Window::getEscCloseWindow()
{

	size_t pos = getChildCount();

	while (pos-- > 0)
	{
		if (d_drawList[pos]->d_EscClose && d_drawList[pos]->isVisible())
			return d_drawList[pos];
	}

	// no child was active, therefore we are the topmost active window
	return 0;
}
//----------------------------------------------------------------------------//
const Window* Window::getActiveChild(void) const
{
	// are children can't be active if we are not
	if (!isActive())
		return 0;

	size_t pos = getChildCount();

	while (pos-- > 0)
	{
		// don't need full backward scan for activeness as we already know
		// 'this' is active.  NB: This uses the draw-ordered child list, as that
		// should be quicker in most cases.
		if (d_drawList[pos]->d_active)
			return d_drawList[pos]->getActiveChild();
	}

	// no child was active, therefore we are the topmost active window
	return this;
}

//----------------------------------------------------------------------------//
bool Window::isAncestor(const String& name) const
{
	// if we have no ancestor then 'name' can't be ancestor
	if (!d_parent)
		return false;

	// check our immediate parent
	if (d_parent->getName() == name)
		return true;

	// not out parent, check back up the family line
	Window* pParent = d_parent;
	bool bAncestor = false;
	while (pParent != NULL)
	{
		bAncestor = pParent->getName() == name;
		if (bAncestor)
		{
			return true;
		}
		pParent = pParent->getParent();
	}
	return bAncestor;
}

//----------------------------------------------------------------------------//
bool Window::isAncestor(uint ID) const
{
	// return false if we have no ancestor
	if (!d_parent)
		return false;

	// check our immediate parent
	if (d_parent->getID() == ID)
		return true;

	// not our parent, check back up the family line
	// return d_parent->isAncestor(ID);

	Window* pParent = d_parent;
	bool bAncestor = false;
	while (pParent != NULL)
	{
		bAncestor = pParent->getID() == ID;
		if (bAncestor)
		{
			return true;
		}
		pParent = pParent->getParent();
	}
	return bAncestor;
}

//----------------------------------------------------------------------------//
bool Window::isAncestor(const Window* window) const
{
	// if we have no parent, then return false
	if (!d_parent)
		return false;

	// check our immediate parent
	if (d_parent == window)
		return true;

	// not our parent, check back up the family line
	// return d_parent->isAncestor(window);
	Window* pParent = d_parent;
	bool bAncestor = false;
	while (pParent != NULL)
	{
		bAncestor = pParent == window;
		if (bAncestor)
		{
			return true;
		}
		pParent = pParent->getParent();
	}
	return bAncestor;
}

//----------------------------------------------------------------------------//
Font* Window::getFont(bool useDefault) const
{
	if (!d_font)
		return useDefault ? System::getSingleton().getDefaultFont() : 0;

	return d_font;
}

//----------------------------------------------------------------------------//
float Window::getEffectiveAlpha(void) const
{
	if (!d_parent || !inheritsAlpha())
		return d_alpha;

	return d_alpha * d_parent->getEffectiveAlpha();
}

//----------------------------------------------------------------------------//
Rect Window::getUnclippedOuterRect() const
{
	if (!d_outerUnclippedRectValid)
	{
		d_outerUnclippedRect = getUnclippedOuterRect_impl();
		d_outerUnclippedRectValid = true;
	}

	return d_outerUnclippedRect;
}

//----------------------------------------------------------------------------//
Rect Window::getUnclippedInnerRect() const
{
	if (!d_innerUnclippedRectValid)
	{
		d_innerUnclippedRect = getUnclippedInnerRect_impl();
		d_innerUnclippedRectValid = true;
	}

	return d_innerUnclippedRect;
}

//----------------------------------------------------------------------------//
Rect Window::getUnclippedRect(const bool inner) const
{
	return inner ? getUnclippedInnerRect() : getUnclippedOuterRect();
}

//----------------------------------------------------------------------------//
Rect Window::getOuterRectClipper() const
{
	if (!d_outerRectClipperValid)
	{
		d_outerRectClipper = getOuterRectClipper_impl();
		d_outerRectClipperValid = true;
	}

	return d_outerRectClipper;
}

//----------------------------------------------------------------------------//
Rect Window::getInnerRectClipper() const
{
	if (!d_innerRectClipperValid)
	{
		d_innerRectClipper = getInnerRectClipper_impl();
		d_innerRectClipperValid = true;
	}

	return d_innerRectClipper;
}

//----------------------------------------------------------------------------//
Rect Window::getClipRect(const bool non_client) const
{
	return non_client ? getOuterRectClipper() : getInnerRectClipper();
}

//----------------------------------------------------------------------------//
Rect Window::getHitTestRect() const
{
	if (!d_hitTestRectValid)
	{
		d_hitTestRect = getHitTestRect_impl();
		d_hitTestRectValid = true;
	}

	return d_hitTestRect;
}

//----------------------------------------------------------------------------//
Rect Window::getParentElementClipIntersection(const Rect& unclipped_area) const
{
	return unclipped_area.getIntersection(
		(d_parent && d_clippedByParent) ?
		d_parent->getClipRect(d_nonClientContent) :
		Rect(Vector2(0, 0),
		System::getSingleton().getRenderer()->getDisplaySize()));
}

//----------------------------------------------------------------------------//
Rect Window::getUnclippedOuterRect_impl() const
{
	const Rect local(0, 0, d_pixelSize.d_width * d_clipWidth, d_pixelSize.d_height);
	return CoordConverter::windowToScreen(*this, local);
}

//----------------------------------------------------------------------------//
Rect Window::getOuterRectClipper_impl() const
{
	return (d_surface && d_surface->isRenderingWindow()) ?
		getUnclippedOuterRect() :
		getParentElementClipIntersection(getUnclippedOuterRect());
}

//----------------------------------------------------------------------------//
Rect Window::getUnclippedInnerRect_impl(void) const
{
	return d_windowRenderer ? d_windowRenderer->getUnclippedInnerRect() :
		getUnclippedOuterRect();
}

//----------------------------------------------------------------------------//
Rect Window::getInnerRectClipper_impl() const
{
	return (d_surface && d_surface->isRenderingWindow()) ?
		getUnclippedInnerRect() :
		getParentElementClipIntersection(getUnclippedInnerRect());
}

//----------------------------------------------------------------------------//
Rect Window::getHitTestRect_impl() const
{
	// if clipped by parent wnd, hit test area is the intersection of our
	// outer rect with the parent's hit test area intersected with the
	// parent's clipper.
	if (d_parent && d_clippedByParent)
	{
		return getUnclippedOuterRect().getIntersection(
			d_parent->getHitTestRect().getIntersection(
			d_parent->getClipRect(d_nonClientContent)));
	}
	// not clipped to parent wnd, so get intersection with screen area.
	else
	{
		return getUnclippedOuterRect().getIntersection(
			Rect(Vector2(0, 0),
			System::getSingleton().getRenderer()->getDisplaySize()));
	}
}

//----------------------------------------------------------------------------//
bool Window::isHit(const Vector2& position, const bool allow_disabled) const
{
	// cannot be hit if we are disabled.
	if (!allow_disabled && isDisabled())
		return false;

	const Rect test_area(getHitTestRect());

	if ((test_area.getWidth() == 0.0f) || (test_area.getHeight() == 0.0f))
		return false;

	return test_area.isPointInRect(position);
}

//----------------------------------------------------------------------------//
Window* Window::getChildAtPosition(const Vector2& position) const
{
	const ChildList::const_reverse_iterator end = d_drawList.rend();

	Vector2 p;
	// if the window has RenderingWindow backing
	if (d_surface && d_surface->isRenderingWindow())
		static_cast<RenderingWindow*>(d_surface)->unprojectPoint(position, p);
	else
		p = position;

	ChildList::const_reverse_iterator child;
	for (child = d_drawList.rbegin(); child != end; ++child)
	{
		if ((*child)->isVisible())
		{
			// recursively scan children of this child windows...
			Window* const wnd = (*child)->getChildAtPosition(p);

			// return window pointer if we found a hit down the chain somewhere
			if (wnd)
				return wnd;
			// see if this child is hit and return it's pointer if it is
			else if ((*child)->isHit(p))
				return (*child);
		}
	}

	// nothing hit
	return 0;
}

//----------------------------------------------------------------------------//
Window* Window::getTargetChildAtPosition(const Vector2& position,
	const bool allow_disabled) const
{
	const ChildList::const_reverse_iterator end = d_drawList.rend();

	Vector2 p;
	// if the window has RenderingWindow backing
	//    if (d_surface && d_surface->isRenderingWindow())
	//        //static_cast<RenderingWindow*>(d_surface)->unprojectPoint(position, p);
	//    else
	p = position;

	ChildList::const_reverse_iterator child;
	for (child = d_drawList.rbegin(); child != end; ++child)
	{
		if ((*child)->isVisible() && (*child)->getEffectiveAlpha() > 0.05&&
			(*child)->getScale().d_x >= 1.0f &&
			(*child)->getScale().d_x <= 1.0f &&
			(*child)->getScale().d_y >= 1.0f &&
			(*child)->getScale().d_y >= 1.0f
			)
		{
			// recursively scan children of this child windows...
			Window* const wnd =
				(*child)->getTargetChildAtPosition(p, allow_disabled);

			// return window pointer if we found a 'hit' down the chain somewhere
			if (wnd)
				return wnd;
			// see if this child is hit and return it's pointer if it is
			else if (!(*child)->isMousePassThroughEnabled() &&
				(*child)->isHit(p, allow_disabled))
				return (*child);
		}
	}

	// nothing hit
	return 0;
}

//----------------------------------------------------------------------------//
void Window::setAlwaysOnTop(bool setting)
{
	// only react to an actual change
	if (isAlwaysOnTop() == setting)
		return;

	d_alwaysOnTop = setting;

	// move us in front of sibling windows with the same 'always-on-top'
	// setting as we have.
	if (d_parent)
	{
		Window* const org_parent = d_parent;

		org_parent->removeChild_impl(this);
		org_parent->addChild_impl(this);

		onZChange_impl();
	}

	WindowEventArgs args(this);
	onAlwaysOnTopChanged(args);
}

//----------------------------------------------------------------------------//
void Window::setTopMost(bool setting)
{
	// move ancestor in front of root window with the same 'top-most'
	// setting as we have.
	Window* parent = d_parent;
	Window* child = this;
	while (parent && parent != System::getSingleton().getGUISheet())
	{
		child = parent;
		parent = parent->getParent();
	}

	// only react to an actual change
	if (child->isTopMost() == setting)
		return;

	if (parent)
	{
		child->setTopMost_impl(setting);
		child->EnableAllowModalState(setting);

		parent->removeChild_impl(child);
		parent->addChild_impl(child);

		onZChange_impl();
	}

	WindowEventArgs args(this);
	onTopMostChanged(args);
}

void Window::setAlwaysOnBottom(bool setting) //…Ë÷√ «∑Ò «‘⁄◊Óµ◊≤�?
{
	if (d_alwsyaOnBottom == setting)
		return;

	d_alwsyaOnBottom = setting;

	// move us in front of sibling windows with the same 'always-on-top'
	// setting as we have.
	if (d_parent)
	{
		Window* const org_parent = d_parent;

		org_parent->removeChild_impl(this);
		org_parent->addChild_impl(this);

		onZChange_impl();
	}

}


//----------------------------------------------------------------------------//
void Window::setEnabled(bool setting)
{
	// only react if setting has changed
	if (d_enabled == setting)
		return;

	d_enabled = setting;
	WindowEventArgs args(this);

	if (d_enabled)
	{
		// check to see if the window is actually enabled (which depends
		// upon all ancestor windows being enabled) we do this so that
		// events we fire give an accurate indication of the state of a
		// window.
		if ((d_parent && !d_parent->isDisabled()) || !d_parent)
			onEnabled(args);
	}
	else
	{
		onDisabled(args);
	}

	System::getSingleton().updateWindowContainingMouse();
}

//----------------------------------------------------------------------------//
void Window::setVisible(bool setting)
{
	// only react if setting has changed
	if (d_visible == setting)
		return;

	d_visible = setting;
	WindowEventArgs args(this);
	d_visible ? onShown(args) : onHidden(args);

	if (!isInChatOutWnd())
	{
		if (((d_visible&&!d_mousePassThroughEnabled) || !d_visible)
			&& !isParentMousePassThroughEnabled())
		{
			System::getSingleton().updateWindowContainingMouse();
		}

	}

}

void Window::setDrawIt(bool setting)
{
	if (d_drawIt != setting && setting)
	{
		invalidate();
	}
	d_drawIt = setting;
}

//----------------------------------------------------------------------------//
void Window::activate(void)
{
	// exit if the window is not visible, since a hidden window may not be the
	// active window.
	if (!isVisible())
		return;

	// force complete release of input capture.
	// NB: This is not done via releaseCapture() because that has
	// different behaviour depending on the restoreOldCapture setting.
	if (d_captureWindow && d_captureWindow != this)
	{
		Window* const tmpCapture = d_captureWindow;
		d_captureWindow = 0;

		WindowEventArgs args(0);
		tmpCapture->onCaptureLost(args);
	}

	moveToFront();
}

//----------------------------------------------------------------------------//
void Window::deactivate(void)
{
	ActivationEventArgs args(this);
	args.otherWindow = 0;
	onDeactivated(args);
}

//----------------------------------------------------------------------------//
void Window::setClippedByParent(bool setting)
{
	// only react if setting has changed
	if (d_clippedByParent == setting)
		return;

	d_clippedByParent = setting;
	WindowEventArgs args(this);
	onClippingChanged(args);
}

//----------------------------------------------------------------------------//
void Window::setText(const String& text, argb_t defcolour, bool triggerEvent)
{
	//’‚¿Ô»Áπ˚—’…´≤ª“ª—˘£¨“≤“™÷ÿ–¬…Ë÷√
	if (text != d_textLogical || defcolour != 0)
	{
		d_textLogical = text;
		d_renderedStringValid = false;
		d_bidiDataValid = false;

		if (defcolour != 0)
		{
			setProperty("TextColours", PropertyHelper::colourToString(colour(defcolour)));
		}
		if (triggerEvent)
		{
			WindowEventArgs args(this);
			onTextChanged(args);
		}
		else
		{
			invalidate();
		}
	}
}

//----------------------------------------------------------------------------//
void Window::setFont(Font* font)
{
	d_font = font;
	d_renderedStringValid = false;
	WindowEventArgs args(this);
	onFontChanged(args);
}

//----------------------------------------------------------------------------//
void Window::setFont(const String& name)
{
	setFont(!name.empty() ? &FontManager::getSingleton().get(name) : 0);
}

//----------------------------------------------------------------------------//
void Window::addChildWindow(const String& name)
{
	addChildWindow(WindowManager::getSingleton().getWindow(name));
}
//----------------------------------------------------------------------------//
void Window::addChildWindow(Window* window)
{
	// don't add null window or ourself as a child
	if (!window || window == this)
		return;
	if (this == System::getSingleton().getGUISheet() && window->getType() != "TaharezLook/Tooltip")
	{
		window->SetDisplaySizeEnable(true);
		if (System::getSingleton().isDisplaySizingEnable() && window->GetDisplaySizeChangePosEnable())
		{
			Size fact = System::getSingleton().GetDisplayFactByBase();
			window->FixWndSize(fact.d_width, fact.d_height);
		}
	}
	addChild_impl(window);
	WindowEventArgs args(window);
	onChildAdded(args);
	window->onZChange_impl();

	window->d_geometry->SetPater(d_geometry);
}

//----------------------------------------------------------------------------//
void Window::removeChildWindow(const String& name)
{
	const size_t child_count = getChildCount();

	for (size_t i = 0; i < child_count; ++i)
	{
		if (d_children[i]->getName() == name)
		{
			removeChildWindow(d_children[i]);
			return;
		}

	}

}

//----------------------------------------------------------------------------//
void Window::removeChildWindow(Window* window)
{
	removeChild_impl(window);
	WindowEventArgs args(window);
	onChildRemoved(args);
	window->onZChange_impl();

	window->d_geometry->SetPater(0);
}

//----------------------------------------------------------------------------//
void Window::removeChildWindow(uint ID)
{
	const size_t child_count = getChildCount();

	for (size_t i = 0; i < child_count; ++i)
	{
		if (d_children[i]->getID() == ID)
		{
			removeChildWindow(d_children[i]);
			return;
		}

	}

}

//----------------------------------------------------------------------------//
void Window::moveToFront()
{
	moveToFront_impl(false);
}

//----------------------------------------------------------------------------//
bool Window::moveToFront_impl(bool wasClicked)
{
	bool took_action = false;

	// if the window has no parent then we can have no siblings
	if (!d_parent)
	{
		// perform initial activation if required.
		if (!isActive())
		{
			took_action = true;
			ActivationEventArgs args(this);
			args.otherWindow = 0;
			onActivated(args);
		}

		return took_action;
	}

	// bring parent window to front of it's siblings
	took_action = d_parent->moveToFront_impl(wasClicked);

	// get immediate child of parent that is currently active (if any)
	Window* const activeWnd = getActiveSibling();

	// if a change in active window has occurred
	if (activeWnd != this)
	{
		took_action = true;

		// notify ourselves that we have become active
		ActivationEventArgs args(this);
		args.otherWindow = activeWnd;
		onActivated(args);

		// notify any previously active window that it is no longer active
		if (activeWnd)
		{
			args.window = activeWnd;
			args.otherWindow = this;
			args.handled = 0;
			activeWnd->onDeactivated(args);
		}
	}

	// bring us to the front of our siblings
	if (d_zOrderingEnabled &&
		(!wasClicked || d_riseOnClick) &&
		!isTopOfZOrder())
	{
		took_action = true;

		// remove us from our parent's draw list
		d_parent->removeWindowFromDrawList(*this);
		// re-attach ourselves to our parent's draw list which will move us in
		// front of sibling windows with the same 'always-on-top' setting as we
		// have.
		d_parent->addWindowToDrawList(*this);
		// notify relevant windows about the z-order change.
		onZChange_impl();
	}

	return took_action;
}

//----------------------------------------------------------------------------//
void Window::moveToBack()
{
	// if the window is active, de-activate it.
	if (isActive())
	{
		ActivationEventArgs args(this);
		args.otherWindow = 0;
		onDeactivated(args);
	}

	// we only proceed if we have a parent (otherwise we can have no siblings)
	if (d_parent)
	{
		if (d_zOrderingEnabled)
		{
			// remove us from our parent's draw list
			d_parent->removeWindowFromDrawList(*this);
			// re-attach ourselves to our parent's draw list which will move us
			// in behind sibling windows with the same 'always-on-top' setting
			// as we have.
			d_parent->addWindowToDrawList(*this, true);
			// notify relevant windows about the z-order change.
			onZChange_impl();
		}

		d_parent->moveToBack();
	}
}

//----------------------------------------------------------------------------//
bool Window::captureInput(void)
{
	// we can only capture if we are the active window (LEAVE THIS ALONE!)
	if (!isActive() || !WindowManager::getSingleton().isWindowPresent(this->getName()))
		return false;

	if (d_captureWindow != this)
	{
		Window* const current_capture = d_captureWindow;
		d_captureWindow = this;
		WindowEventArgs args(this);

		// inform window which previously had capture that it doesn't anymore.
		if (current_capture && current_capture != this && !d_restoreOldCapture)
			current_capture->onCaptureLost(args);

		if (d_restoreOldCapture)
			d_oldCapture = current_capture;

		onCaptureGained(args);
	}

	return true;
}

//----------------------------------------------------------------------------//
void Window::releaseInput(void)
{
	// if we are not the window that has capture, do nothing
	if (!isCapturedByThis())
		return;

	// restore old captured window if that mode is set
	if (d_restoreOldCapture)
	{

		d_captureWindow = d_oldCapture;

		// check for case when there was no previously captured window
		if (d_oldCapture)
		{
			d_oldCapture = 0;
			d_captureWindow->moveToFront();
		}

	}
	else if (d_recognizerManager&&d_recognizerManager->CanReleaseCapture())
	{
		d_captureWindow = 0;

		WindowEventArgs args(this);
		onCaptureLost(args);

	}

}

//----------------------------------------------------------------------------//
void Window::setRestoreCapture(bool setting)
{
	d_restoreOldCapture = setting;

	const size_t child_count = getChildCount();

	for (size_t i = 0; i < child_count; ++i)
		d_children[i]->setRestoreCapture(setting);
}

//----------------------------------------------------------------------------//
void Window::setAlpha(float alpha)
{
	if ((d_EffectState == WndEffectState_Ready&&d_alpha<alpha) || d_EffectState == WndEffectState_Go)
	{
		return;
	}
	// clamp this to the valid range [0.0, 1.0]
	d_alpha = ceguimax(ceguimin(alpha, 1.0f), 0.0f);
	WindowEventArgs args(this);
	onAlphaChanged(args);
}

//----------------------------------------------------------------------------//
void Window::setInheritsAlpha(bool setting)
{
	if (d_inheritsAlpha != setting)
	{
		// store old effective alpha so we can test if alpha value changes due
		// to new setting.
		const float oldAlpha = getEffectiveAlpha();

		// notify about the setting change.
		d_inheritsAlpha = setting;

		WindowEventArgs args(this);
		onInheritsAlphaChanged(args);

		// if effective alpha has changed fire notification about that too
		if (oldAlpha != getEffectiveAlpha())
		{
			args.handled = 0;
			onAlphaChanged(args);
		}
	}
}

//----------------------------------------------------------------------------//
void Window::invalidate(void)
{
	invalidate(false);
}

//----------------------------------------------------------------------------//
void Window::invalidate(const bool recursive)
{
	invalidate_impl(recursive);
	System::getSingleton().signalRedraw();
}

//----------------------------------------------------------------------------//
void Window::invalidate_impl(const bool recursive)
{
	d_needsRedraw = true;
	invalidateRenderingSurface();
	if (d_geometry)
	{
		Cocos2DGeometryBuffer* pGeo = (Cocos2DGeometryBuffer*)d_geometry;
		if (pGeo)
		{
			pGeo->SetUpdateMatrixFlag(false);
		}
	}
	if (recursive)
	{
		const size_t child_count = getChildCount();
		for (size_t i = 0; i < child_count; ++i)
			if (d_children[i]) d_children[i]->invalidate_impl(true);
	}
}

bool Window::test_render()
{
	if (!this->getGeometryBuffer()->test_draw() && this->getGeometryBuffer()->getBatchCount() == 0)
		return false;

	const size_t child_count = getChildCount();
	for (size_t i = 0; i < child_count; ++i)
	{
		if (d_drawList[i]->getDrawIt())
		{
			if (!d_drawList[i]->test_render())
			{
				return false;
			}
		}
	}

	return true;
}

//----------------------------------------------------------------------------//
bool Window::render()
{
	// don't do anything if window is not visible
	if (!isVisible())
		return true;

	if (d_IsLoadedDraw&&!test_render())
	{
		return false;
	}

	bool bSuccess = true;

	// get rendering context
	RenderingContext ctx;
	getRenderingContext(ctx);

	// clear geometry from surface if it's ours
	if (ctx.owner == this)
		ctx.surface->clearGeometry();

	// redraw if no surface set, or if surface is invalidated
	if (!d_surface || d_surface->isInvalidated())
	{
		// perform drawing for 'this' Window
		drawSelf(ctx);
		if (this->getGeometryBuffer()->isRenderSuccess() || this->getGeometryBuffer()->getBatchCount() == 0)
		{
			// render any child windows
			const size_t child_count = getChildCount();
			for (size_t i = 0; i < child_count; ++i)
			{
				if (d_drawList[i]->getDrawIt()) {
					//d_drawList[i]->invalidate();
					if (!d_drawList[i]->render())
					{
						bSuccess = false;
					}
				}
			}
		}
		else
		{
			bSuccess = false;
		}
	}

	// do final rendering for surface if it's ours
	if (ctx.owner == this)
		ctx.surface->draw();
	WindowEventArgs args(this);
	args.handled = 0;
	onRenderingEnded(args);

	if (!bSuccess)
	{
		invalidate();
	}

	return bSuccess;
}

//----------------------------------------------------------------------------//
void Window::drawSelf(const RenderingContext& ctx)
{
	bufferGeometry(ctx);
	queueGeometry(ctx);
	d_geometry->updateMatrix();
}

//----------------------------------------------------------------------------//
void Window::bufferGeometry(const RenderingContext& ctx)
{
	bool renderSuccess = true;

	if (d_needsRedraw)
	{
		// dispose of already cached geometry.
		d_geometry->reset();

		// signal rendering started
		WindowEventArgs args(this);
		onRenderingStarted(args);

		// HACK: ensure our rendered string content is up to date
		getRenderedString();

		if (d_ModalStateDrawEffect)
			DrawModalStateEffect(ctx.offset.d_x, ctx.offset.d_y);

		// get derived class or WindowRenderer to re-populate geometry buffer.
		if (d_windowRenderer)
			d_windowRenderer->render();
		else
			populateGeometryBuffer();

		// signal rendering ended
		args.handled = 0;
		//onRenderingEnded(args);

		// mark ourselves as no longer needed a redraw.
		d_needsRedraw = false;
	}
}

//----------------------------------------------------------------------------//
void Window::queueGeometry(const RenderingContext& ctx)
{
	// add geometry so that it gets drawn to the target surface.
	ctx.surface->addGeometryBuffer(ctx.queue, d_geometry);
	d_geometry->SetRenderSurface(ctx.surface);
}

//----------------------------------------------------------------------------//
void Window::setParent(Window* parent)
{
	d_parent = parent;

	// if we do not have a surface, xfer any surfaces from our children to
	// whatever our target surface now is.
	if (!d_surface)
		transferChildSurfaces();
	// else, since we have a surface, child surfaces stay with us.  Though we
	// must now ensure /our/ surface is xferred if it is a RenderingWindow.
	else if (d_surface->isRenderingWindow())
	{
		// target surface is eihter the parent's target, or the default root.
		RenderingSurface& tgt = d_parent ?
			d_parent->getTargetRenderingSurface() :
			System::getSingleton().getRenderer()->getDefaultRenderingRoot();

		tgt.transferRenderingWindow(static_cast<RenderingWindow&>(*d_surface));
	}
}

//----------------------------------------------------------------------------//
float Window::getParentPixelWidth(void) const
{
	return d_parent ?
		d_parent->d_pixelSize.d_width :
		System::getSingleton().getRenderer()->getDisplaySize().d_width;
}

//----------------------------------------------------------------------------//
float Window::getParentPixelHeight(void) const
{
	return d_parent ?
		d_parent->d_pixelSize.d_height :
		System::getSingleton().getRenderer()->getDisplaySize().d_height;
}

//----------------------------------------------------------------------------//
Size Window::getParentPixelSize(void) const
{
	return getSize_impl(d_parent);
}

//----------------------------------------------------------------------------//
void Window::cleanupChildren(void)
{
	while (getChildCount() != 0)
	{
		Window* wnd = d_children[0];

		// always remove child
		removeChildWindow(wnd);

		// destroy child if that is required
		if (wnd->isDestroyedByParent())
			WindowManager::getSingleton().destroyWindow(wnd);
	}
}

bool Window::isExistNonAutoChildren()
{
	ChildList::iterator position = d_children.begin();
	for (; position != d_children.end(); ++position)
	{
		Window* pWnd = *position;
		if (pWnd == NULL)
		{
			continue;
		}

		if (!pWnd->isAutoWindow())
		{
			return true;
		}
	}

	return false;
}

void Window::cleanupNonAutoChildren(void)
{
	//    int auto_window = 0;
	//    while(getChildCount() != auto_window)
	//    {
	//        Window* wnd = d_children[0];
	//        
	//        if (wnd->isAutoWindow()) {
	//            auto_window++;
	//        }
	//        else
	//        {
	//            // always remove child
	//            removeChildWindow(wnd);
	//            
	//            // destroy child if that is required
	//            if (wnd->isDestroyedByParent())
	//                WindowManager::getSingleton().destroyWindow(wnd);
	//        }
	//    }

	int i = 0;
	while (isExistNonAutoChildren())
	{
		++i;
		if (i>10000)
		{
			Logger::getSingleton().logEvent("Window::cleanupNonAutoChildren overlow", Informative);
			break;
		}
		ChildList::iterator position = d_children.begin();
		for (; position != d_children.end(); ++position)
		{
			Window* pWnd = *position;
			if (pWnd == NULL)
			{
				continue;
			}

			if (!pWnd->isAutoWindow())
			{
				removeChildWindow(pWnd);

				if (pWnd->isDestroyedByParent())
					WindowManager::getSingleton().destroyWindow(pWnd);
				break;

			}
		}
	}

}

void Window::cleanupAllEvent(void)
{
	removeAllEvents();
	const size_t child_count = getChildCount();

	for (size_t i = 0; i < child_count; ++i)
	{
		d_children[i]->cleanupAllEvent();
	}
}

void Window::setAllChildrenMousePassThroughEnabled(bool bEnable)
{
	setMousePassThroughEnabled(bEnable);
	const size_t child_count = getChildCount();

	for (size_t i = 0; i < child_count; ++i)
	{
		d_children[i]->setMousePassThroughEnabled(bEnable);
	}
}
//----------------------------------------------------------------------------//
void Window::addChild_impl(Window* wnd)
{
	// if window is already attached, detach it first (will fire normal events)
	if (wnd->getParent())
		wnd->getParent()->removeChildWindow(wnd);

	addWindowToDrawList(*wnd);

	// add window to child list
	d_children.push_back(wnd);

	// set the parent window
	wnd->setParent(this);

	// Force and update for the area Rects for 'wnd' so they're correct for it's
	// new parent.
	WindowEventArgs args(this);
	wnd->onParentSized(args, d_pixelSize);
}

//----------------------------------------------------------------------------//
void Window::removeChild_impl(Window* wnd)
{
	// remove from draw list
	removeWindowFromDrawList(*wnd);

	// if window has no children, we can't remove 'wnd' from the child list
	if (d_children.empty())
		return;

	// find this window in the child list
	const ChildList::iterator position =
		std::find(d_children.begin(), d_children.end(), wnd);

	// if the window was found in the child list
	if (position != d_children.end())
	{
		// remove window from child list
		d_children.erase(position);
		// reset windows parent so it's no longer this window.
		wnd->setParent(0);
	}
}

//----------------------------------------------------------------------------//
void Window::onZChange_impl(void)
{
	if (WindowManager::getSingleton().isLoading())
		return;

	if (!d_parent)
	{
		WindowEventArgs args(this);
		onZChanged(args);
	}
	else
	{
		const size_t child_count = d_parent->getChildCount();

		for (size_t i = 0; i < child_count; ++i)
		{
			WindowEventArgs args(d_parent->d_children[i]);
			d_parent->d_children[i]->onZChanged(args);
		}

	}

	if (!isInChatOutWnd())
	{
		if (!isParentMousePassThroughEnabled())
		{
			System::getSingleton().updateWindowContainingMouse();
		}

		System::getSingleton().UpdateKeyBoardTargetWnd();
	}

}

//----------------------------------------------------------------------------//
Size Window::getSize_impl(const Window* window) const
{
	return window ?
		window->d_pixelSize :
		System::getSingleton().getRenderer()->getDisplaySize();
}

//----------------------------------------------------------------------------//
const Image* Window::getMouseCursor(bool useDefault) const
{
	/* if (d_mouseCursor != (const Image*)DefaultMouseCursor)
	return d_mouseCursor;
	else
	return useDefault ? System::getSingleton().getDefaultMouseCursor() : 0;*/
	//return  System::getSingleton().getDefaultMouseCursor();
	return NULL;
}

//----------------------------------------------------------------------------//
void Window::setMouseCursor(const String& imageset, const String& image_name)
{
	/*  d_mouseCursor =
	&ImagesetManager::getSingleton().get(imageset).getImage(image_name);*/
	d_mouseCursor = NULL;
}

void Window::setUserData(void* user_data)
{
	d_userData = user_data; 
}

//----------------------------------------------------------------------------//
void Window::setID(uint ID)
{
	if (d_ID == ID)
		return;

	d_ID = ID;

	WindowEventArgs args(this);
	onIDChanged(args);
}

void Window::setID2(uint ID)
{
	if (d_ID2 == ID)
		return;

	d_ID2 = ID;
}

//----------------------------------------------------------------------------//
void Window::setDestroyedByParent(bool setting)
{
	if (d_destroyedByParent == setting)
		return;

	d_destroyedByParent = setting;

	WindowEventArgs args(this);
	onParentDestroyChanged(args);
}

//----------------------------------------------------------------------------//
void Window::generateAutoRepeatEvent(MouseButton button)
{
	MouseEventArgs ma(this);
	ma.position =
		getUnprojectedPosition(MouseCursor::getSingleton().getPosition());
	ma.moveDelta = Vector2(0.0f, 0.0f);
	ma.button = button;
	ma.sysKeys = System::getSingleton().getSystemKeys();
	ma.wheelChange = 0;
	onMouseButtonDown(ma);
}

//----------------------------------------------------------------------------//
void Window::addStandardProperties(void)
{
	addBaseProperty(&d_alphaProperty);
	addBaseProperty(&d_alwaysOnTopProperty);
	addBaseProperty(&d_topMostProperty);
	addBaseProperty(&d_clippedByParentProperty);
	// addProperty(&d_destroyedByParentProperty);
	addBaseProperty(&d_disabledProperty);
	addBaseProperty(&d_fontProperty);
	// addProperty(&d_IDProperty);
	//addProperty(&d_inheritsAlphaProperty);
	//addProperty(&d_mouseCursorProperty);
	// addProperty(&d_restoreOldCaptureProperty);
	addBaseProperty(&d_textProperty);
	addBaseProperty(&d_visibleProperty);
	//addProperty(&d_zOrderChangeProperty);
	// addProperty(&d_wantsMultiClicksProperty);
	// addProperty(&d_autoRepeatProperty);
	// addProperty(&d_autoRepeatDelayProperty);
	// addProperty(&d_autoRepeatRateProperty);
	// addProperty(&d_distInputsProperty);
	// addProperty(&d_tooltipTypeProperty);
	addBaseProperty(&d_tooltipProperty);
	// addProperty(&d_inheritsTooltipProperty);
	// addProperty(&d_riseOnClickProperty);
	addBaseProperty(&d_vertAlignProperty);
	addBaseProperty(&d_horzAlignProperty);
	addBaseProperty(&d_unifiedAreaRectProperty);
	addBaseProperty(&d_unifiedPositionProperty);
	addBaseProperty(&d_unifiedXPositionProperty);
	addBaseProperty(&d_unifiedYPositionProperty);
	addBaseProperty(&d_unifiedSizeProperty);
	addBaseProperty(&d_unifiedWidthProperty);
	addBaseProperty(&d_unifiedHeightProperty);
	addBaseProperty(&d_CloseIsHideProperty);
	addBaseProperty(&d_IsPixelDecideProperty);
	// by bayaer, 注掉一下两项，因为 staticImage 自动增加的话会无法改变大�?
#ifdef FORCEGUIEDITOR
	//addProperty(&d_unifiedMinSizeProperty);
	//addProperty(&d_unifiedMaxSizeProperty);
	addProperty(&d_LuaClassProperty);
	addProperty(&d_LuaForDialogProperty);
	addProperty(&d_LuaMemberNameProperty);
	addProperty(&d_LuaEventOnClickedProperty);
	//addProperty(&d_DestroyDialogProperty);
#else
	addBaseProperty(&d_unifiedMinSizeProperty);
	addBaseProperty(&d_unifiedMaxSizeProperty);
#endif

	addBaseProperty(&d_mousePassThroughEnabledProperty);
	// addProperty(&d_windowRendererProperty);
	// addProperty(&d_lookNFeelProperty);
	//addProperty(&d_dragDropTargetProperty);
	addBaseProperty(&d_scaleProperty);
	addBaseProperty(&d_autoRenderingSurfaceProperty);
	addBaseProperty(&d_rotationProperty);
	addBaseProperty(&d_xRotationProperty);
	addBaseProperty(&d_yRotationProperty);
	addBaseProperty(&d_zRotationProperty);
	//  addProperty(&d_nonClientProperty);
	addBaseProperty(&d_textParsingEnabledProperty);
	addBaseProperty(&d_DisplaySizeChangePosEnabledProperty);
	addBaseProperty(&d_DrageMoveEnabledProperty);

	addBaseProperty(&d_SoundEnableProperty);
	addBaseProperty(&d_SoundResProperty);
	addBaseProperty(&d_CloseSoundResProperty);

	addBaseProperty(&d_LimitWindowSizeProperty);

	addBaseProperty(&d_CreateEffectdProperty);
	addBaseProperty(&d_CloseEffectdProperty);
	addBaseProperty(&d_AllowModalSateProperty);
	addBaseProperty(&d_ModalSateProperty);
	addBaseProperty(&d_AllowShowWithModalStateProperty);
	//addProperty(&d_DestroyDialogProperty);

#ifdef _EDITOR_
	addProperty(&d_WidthProperty);
	addProperty(&d_HeightProperty);
	addProperty(&d_XPosProperty);
	addProperty(&d_YPosProperty);

#endif

	// we ban some of these properties from xml for auto windows by default
	if (isAutoWindow())
	{
#ifdef FORCEGUIEDITOR
		banPropertyFromXML(&d_destroyedByParentProperty);
		banPropertyFromXML(&d_vertAlignProperty);
		banPropertyFromXML(&d_horzAlignProperty);
		banPropertyFromXML(&d_unifiedAreaRectProperty);
		banPropertyFromXML(&d_unifiedPositionProperty);
		banPropertyFromXML(&d_unifiedXPositionProperty);
		banPropertyFromXML(&d_unifiedYPositionProperty);
		banPropertyFromXML(&d_unifiedSizeProperty);
		banPropertyFromXML(&d_unifiedWidthProperty);
		banPropertyFromXML(&d_unifiedHeightProperty);

		banPropertyFromXML(&d_unifiedMinSizeProperty);
		banPropertyFromXML(&d_unifiedMaxSizeProperty);

		banPropertyFromXML(&d_windowRendererProperty);
		banPropertyFromXML(&d_lookNFeelProperty);

		banPropertyFromXML(&d_WidthProperty);
		banPropertyFromXML(&d_HeightProperty);
		banPropertyFromXML(&d_XPosProperty);
		banPropertyFromXML(&d_YPosProperty);

		banPropertyFromXML(&d_SoundEnableProperty);
		banPropertyFromXML(&d_SoundResProperty);
		banPropertyFromXML(&d_CloseSoundResProperty);
		banPropertyFromXML(&d_textParsingEnabledProperty);
		banPropertyFromXML(&d_CreateEffectdProperty);
		banPropertyFromXML(&d_CloseEffectdProperty);
		banPropertyFromXML(&d_AllowModalSateProperty);
		banPropertyFromXML(&d_AllowShowWithModalStateProperty);
		banPropertyFromXML(&d_ModalSateProperty);
		//banPropertyFromXML(&d_DestroyDialogProperty);

		banPropertyFromXML(&d_LimitWindowSizeProperty);

#endif
	}
}

//----------------------------------------------------------------------------//
bool Window::isZOrderingEnabled(void) const
{
	return d_zOrderingEnabled;
}

//----------------------------------------------------------------------------//
void Window::setZOrderingEnabled(bool setting)
{
	d_zOrderingEnabled = setting;
}

//----------------------------------------------------------------------------//
bool Window::wantsMultiClickEvents(void) const
{
	return d_wantsMultiClicks;
}

//----------------------------------------------------------------------------//
void Window::setWantsMultiClickEvents(bool setting)
{
	d_wantsMultiClicks = setting;
}

//----------------------------------------------------------------------------//
bool Window::isMouseAutoRepeatEnabled(void) const
{
	return d_autoRepeat;
}

//----------------------------------------------------------------------------//
float Window::getAutoRepeatDelay(void) const
{
	return d_repeatDelay;
}

//----------------------------------------------------------------------------//
float Window::getAutoRepeatRate(void) const
{
	return d_repeatRate;
}

float Window::getDieTime(void) const
{
	return d_dieTime;
}

bool Window::getDieingState(void) const
{
	return d_dieing;
}

//----------------------------------------------------------------------------//
void Window::setMouseAutoRepeatEnabled(bool setting)
{
	if (d_autoRepeat == setting)
		return;

	d_autoRepeat = setting;
	d_repeatButton = NoButton;

	// FIXME: There is a potential issue here if this setting is
	// FIXME: changed _while_ the mouse is auto-repeating, and
	// FIXME: the 'captured' state of input could get messed up.
	// FIXME: The alternative is to always release here, but that
	// FIXME: has a load of side effects too - so for now nothing
	// FIXME: is done.  This whole aspect of the system needs a
	// FIXME: review an reworking - though such a change was
	// FIXME: beyond the scope of the bug-fix that originated this
	// FIXME: comment block.  PDT - 30/10/06
}

//----------------------------------------------------------------------------//
void Window::setAutoRepeatDelay(float delay)
{
	d_repeatDelay = delay;
}

//----------------------------------------------------------------------------//
void Window::setAutoRepeatRate(float rate)
{
	d_repeatRate = rate;
}

void Window::setDieTime(float val)
{
	d_dieTime = val;
	WindowEventArgs args(this);
	fireEvent(EventDieTimeTick, args, EventNamespace);
	invalidate();
}

void Window::setDieingState(bool state)
{
	d_dieing = state;
}

void Window::setLimitSize(bool val)
{
	d_limitSize = val;
}

bool Window::getLimitSize(void) const
{
	return d_limitSize;
}

//----------------------------------------------------------------------------//
void Window::update(float elapsed)
{
	// perform update for 'this' Window
	d_ElapseTime += elapsed;
	if (d_TimeAutoClose)
	{
		if (d_ElapseTime >= d_LifeTime)
		{
			destroy();
			return;
		}
	}

	updateSelf(elapsed);

	if (d_dieing)
	{
		if (d_dieTime > 0.0)
		{
			d_dieTime -= elapsed;
			setDieTime(d_dieTime);
		}
	}
	// update underlying RenderingWinodw if needed
	if (d_surface && d_surface->isRenderingWindow())
		static_cast<RenderingWindow*>(d_surface)->update(elapsed);

	UpdateEventArgs e(this, elapsed);
	if (!d_recognizerManager->update(e)) {
		fireEvent(EventWindowUpdated, e, EventNamespace);
	}

	// update child windows
	for (size_t i = 0; i < getChildCount(); ++i)
		d_children[i]->update(elapsed);
}

//----------------------------------------------------------------------------//
void Window::updateSelf(float elapsed)
{
	// Mouse button autorepeat processing.
	if (d_autoRepeat && d_repeatButton != NoButton)
	{
		d_repeatElapsed += elapsed;

		if (d_repeating)
		{
			if (d_repeatElapsed > d_repeatRate)
			{
				d_repeatElapsed -= d_repeatRate;
				// trigger the repeated event
				generateAutoRepeatEvent(d_repeatButton);
			}
		}
		else
		{
			if (d_repeatElapsed > d_repeatDelay)
			{
				d_repeatElapsed = 0;
				d_repeating = true;
				// trigger the repeated event
				generateAutoRepeatEvent(d_repeatButton);
			}
		}
	}

	if (d_Flash)
	{
		d_FalshElapseTime += elapsed;
		if (d_EnableFlash)
		{
			float alpha = fabs(cosf(d_FalshElapseTime*d_FlashFrequence*3.1415926f));
			setAlpha(alpha);
		}
	}
	UpdateWndEffect(elapsed);

	UpdateFlyPos(elapsed);

	// allow for updates within an assigned WindowRenderer
	if (d_windowRenderer)
		d_windowRenderer->update(elapsed);
}

//----------------------------------------------------------------------------//
bool Window::distributesCapturedInputs(void) const
{
	return d_distCapturedInputs;
}

//----------------------------------------------------------------------------//
void Window::setDistributesCapturedInputs(bool setting)
{
	d_distCapturedInputs = setting;
}

//----------------------------------------------------------------------------//
void Window::notifyDragDropItemEnters(DragContainer* item)
{
	if (!item)
		return;

	DragDropEventArgs args(this);
	args.dragDropItem = item;
	onDragDropItemEnters(args);
}

//----------------------------------------------------------------------------//
void Window::notifyDragDropItemLeaves(DragContainer* item)
{
	if (!item)
		return;

	DragDropEventArgs args(this);
	args.dragDropItem = item;
	onDragDropItemLeaves(args);
}

//----------------------------------------------------------------------------//
void Window::notifyDragDropItemDropped(DragContainer* item)
{
	if (!item)
		return;

	DragDropEventArgs args(this);
	args.dragDropItem = item;
	onDragDropItemDropped(args);
}

//----------------------------------------------------------------------------//
void Window::destroy(void)
{
	// because we know that people do not read the API ref properly,
	// here is some protection to ensure that WindowManager does the
	// destruction and not anyone else.
	WindowManager& wmgr = WindowManager::getSingleton();

	if (wmgr.isWindowPresent(this->getName()))
	{
		wmgr.destroyWindow(this);

		// now return, the rest of what we need to do will happen
		// once WindowManager re-calls this method.
		return;
	}

	releaseInput();

	// let go of the tooltip if we have it
	Tooltip* const tip = getTooltip();
	if (tip && tip->getTargetWindow() == this)
		tip->setTargetWindow(0);



	// ensure custom tooltip is cleaned up
	setTooltip(static_cast<Tooltip*>(0));

	// free any assigned WindowRenderer
	if (d_windowRenderer != 0)
	{
		d_windowRenderer->onDetach();
		WindowRendererManager::getSingleton().
			destroyWindowRenderer(d_windowRenderer);
		d_windowRenderer = 0;
	}

	// signal our imminent destruction
	WindowEventArgs args(this);
	onDestructionStarted(args);

	// double check we are detached from parent
	if (d_parent)
		d_parent->removeChildWindow(this);

	cleanupChildren();

	if (System::getSingleton().GetDefaultKeyBoardTargetWnd() == this)
	{
		System::getSingleton().SetDefaultKeyBoardTagetWnd(0);
	}
	if (System::getSingleton().getKeyboardTargetWindow() == this)
	{
		//System::getSingleton().UpdateKeyBoardTargetWnd();
		System::getSingleton().SetKeyBordTagetWnd(NULL);
	}

	releaseRenderingWindow();
}

//----------------------------------------------------------------------------//
bool Window::isUsingDefaultTooltip(void) const
{
	return d_customTip == 0;
}

//----------------------------------------------------------------------------//
Tooltip* Window::getTooltip(void) const
{
	if (isInChatOutWnd())
	{
		return System::getSingleton().GetChatOutWndTips();
	}
	return isUsingDefaultTooltip() ? System::getSingleton().getDefaultTooltip() :
		d_customTip;
}

//----------------------------------------------------------------------------//
void Window::setTooltip(Tooltip* tooltip)
{
	// destroy current custom tooltip if one exists and we created it
	if (d_customTip && d_weOwnTip)
		WindowManager::getSingleton().destroyWindow(d_customTip);

	// set new custom tooltip
	d_weOwnTip = false;
	d_customTip = tooltip;
}

//----------------------------------------------------------------------------//
void Window::setTooltipType(const String& tooltipType)
{
	// destroy current custom tooltip if one exists and we created it
	if (d_customTip && d_weOwnTip)
		WindowManager::getSingleton().destroyWindow(d_customTip);

	if (tooltipType.empty())
	{
		d_customTip = 0;
		d_weOwnTip = false;
	}
	else
	{
		try
		{
			d_customTip = static_cast<Tooltip*>(
				WindowManager::getSingleton().createWindow(
				tooltipType, getName() + TooltipNameSuffix));
			d_weOwnTip = true;
		}
		catch (UnknownObjectException&)
		{
			d_customTip = 0;
			d_weOwnTip = false;
		}
	}
}

//----------------------------------------------------------------------------//
String Window::getTooltipType(void) const
{
	return isUsingDefaultTooltip() ? String("") : d_customTip->getType();
}

//----------------------------------------------------------------------------//
void Window::setTooltipText(const String& tip)
{
	//yanji 20150917 return;
	d_tooltipText = tip;

	Tooltip* const tooltip = getTooltip();

	if (tooltip && tooltip->getTargetWindow() == this)
		tooltip->setText(tip);
}

//----------------------------------------------------------------------------//
const String& Window::getTooltipText(void) const
{
	/* yanji 20150917
	if (d_inheritsTipText && d_parent && d_tooltipText.empty())
		return d_parent->getTooltipText();
	else
	*/
		return d_tooltipText;
}

//----------------------------------------------------------------------------//
bool Window::inheritsTooltipText(void) const
{
	return d_inheritsTipText;
}

//----------------------------------------------------------------------------//
void Window::setInheritsTooltipText(bool setting)
{
	d_inheritsTipText = setting;
}

//----------------------------------------------------------------------------//
void Window::setArea_impl(const UVector2& pos, const UVector2& size,
	bool topLeftSizing, bool fireEvents)
{
	// we make sure the screen areas are recached when this is called as we need
	// it in most cases
	d_outerUnclippedRectValid = false;
	d_innerUnclippedRectValid = false;
	d_outerRectClipperValid = false;
	d_innerRectClipperValid = false;
	d_hitTestRectValid = false;

	// notes of what we did
	bool moved = false, sized;

	// save original size so we can work out how to behave later on
	const Size oldSize(d_pixelSize);

	d_oldSize = d_pixelSize;


	// calculate pixel sizes for everything, so we have a common format for
	// comparisons.
	Vector2 absMax(d_maxSize.asAbsolute(
		System::getSingleton().getRenderer()->getDisplaySize()));
	Vector2 absMin(d_minSize.asAbsolute(
		System::getSingleton().getRenderer()->getDisplaySize()));

	const Size base_size((d_parent && !d_nonClientContent) ?
		d_parent->getUnclippedInnerRect().getSize() :
		getParentPixelSize());

	d_pixelSize = size.asAbsolute(base_size).asSize();

	//modify by eagle(根据条件来决定是否需要分辨率来限制控件尺�?
	if (d_limitSize)
	{
		// limit new pixel size to: minSize <= newSize <= maxSize
		if (d_pixelSize.d_width < absMin.d_x)
			d_pixelSize.d_width = absMin.d_x;
		else if (d_pixelSize.d_width > absMax.d_x)
			d_pixelSize.d_width = absMax.d_x;
		if (d_pixelSize.d_height < absMin.d_y)
			d_pixelSize.d_height = absMin.d_y;
		else if (d_pixelSize.d_height > absMax.d_y)
			d_pixelSize.d_height = absMax.d_y;
	}
	//modify end

	d_area.setSize(size);

	sized = (d_pixelSize != oldSize);

	// If this is a top/left edge sizing op, only modify position if the size
	// actually changed.  If it is not a sizing op, then position may always
	// change.
	if (!topLeftSizing || sized)
	{
		// only update position if a change has occurred.
		if (pos != d_area.d_min)
		{
			d_area.setPosition(pos);
			moved = true;
		}
	}

	// fire events as required
	if (fireEvents)
	{
		WindowEventArgs args(this);

		if (moved)
		{
			onMoved(args);
			// reset handled so 'sized' event can re-use (since  wo do not care
			// about it)
			args.handled = 0;
		}

		if (sized)
			onSized(args);
	}

	if ((moved || sized) && !isInChatOutWnd()
		&& !isParentMousePassThroughEnabled())
		System::getSingleton().updateWindowContainingMouse();

	// update geometry position and clipping if nothing from above appears to
	// have done so already (NB: may be occasionally wasteful, but fixes bugs!)
	if (!d_outerUnclippedRectValid)
		updateGeometryRenderSettings();

	if (getModalState())
	{
		invalidate();
	}

}

//----------------------------------------------------------------------------//
void Window::setArea(const UDim& xpos, const UDim& ypos,
	const UDim& width, const UDim& height)
{
	setArea(UVector2(xpos, ypos), UVector2(width, height));
}

//----------------------------------------------------------------------------//
void Window::setArea(const UVector2& pos, const UVector2& size)
{
	setArea_impl(pos, size);
}

//----------------------------------------------------------------------------//
void Window::setArea(const URect& area)
{
	setArea(area.d_min, area.getSize());
}

//----------------------------------------------------------------------------//
void Window::setPosition(const UVector2& pos)
{
	setArea_impl(pos, d_area.getSize());
}

//----------------------------------------------------------------------------//
void Window::setXPosition(const UDim& x)
{
	setArea_impl(UVector2(x, d_area.d_min.d_y), d_area.getSize());
}

//----------------------------------------------------------------------------//
void Window::setYPosition(const UDim& y)
{
	setArea_impl(UVector2(d_area.d_min.d_x, y), d_area.getSize());
}

//----------------------------------------------------------------------------//
void Window::setSize(const UVector2& size)
{
	setArea_impl(d_area.getPosition(), size);
}

//----------------------------------------------------------------------------//
void Window::setWidth(const UDim& width)
{
	setArea_impl(d_area.getPosition(), UVector2(width, d_area.getSize().d_y));
}

//----------------------------------------------------------------------------//
void Window::setHeight(const UDim& height)
{
	setArea_impl(d_area.getPosition(), UVector2(d_area.getSize().d_x, height));
}

//----------------------------------------------------------------------------//
void Window::setMaxSize(const UVector2& size)
{
	d_maxSize = size;

	// set window area back on itself to cause new maximum size to be applied if
	// required.
	setArea(d_area);
}

//----------------------------------------------------------------------------//
void Window::setMinSize(const UVector2& size)
{
	d_minSize = size;

	// set window area back on itself to cause new minimum size to be applied if
	// required.
	setArea(d_area);
}

//----------------------------------------------------------------------------//
const URect& Window::getArea() const
{
	return d_area;
}

//----------------------------------------------------------------------------//
const UVector2& Window::getPosition() const
{
	return d_area.d_min;
}

//----------------------------------------------------------------------------//
const UDim& Window::getXPosition() const
{
	return d_area.d_min.d_x;
}

//----------------------------------------------------------------------------//
const UDim& Window::getYPosition() const
{
	return d_area.d_min.d_y;
}

//----------------------------------------------------------------------------//
UVector2 Window::getSize() const
{
	return d_area.getSize();
}

//----------------------------------------------------------------------------//
UDim Window::getWidth() const
{
	return d_area.getSize().d_x;
}

//----------------------------------------------------------------------------//
UDim Window::getHeight() const
{
	return d_area.getSize().d_y;
}

//----------------------------------------------------------------------------//
const UVector2& Window::getMaxSize() const
{
	return d_maxSize;
}

//----------------------------------------------------------------------------//
const UVector2& Window::getMinSize() const
{
	return d_minSize;
}

//----------------------------------------------------------------------------//
void Window::setVerticalAlignment(const VerticalAlignment alignment)
{
	if (d_vertAlign == alignment)
		return;

	d_vertAlign = alignment;

	WindowEventArgs args(this);
	onVerticalAlignmentChanged(args);
}

//----------------------------------------------------------------------------//
void Window::setHorizontalAlignment(const HorizontalAlignment alignment)
{
	if (d_horzAlign == alignment)
		return;

	d_horzAlign = alignment;

	WindowEventArgs args(this);
	onHorizontalAlignmentChanged(args);
}

//----------------------------------------------------------------------------//
const String& Window::getLookNFeel() const
{
	return d_lookName;
}

//----------------------------------------------------------------------------//
void Window::setLookNFeel(const String& look, bool bClone)
{
	if (!d_windowRenderer)
		throw NullObjectException("Window::setLookNFeel: There must be a "
		"window renderer assigned to the window '" + d_name +
		"' to set its look'n'feel");

	if (!bClone)
	{
		WidgetLookManager& wlMgr = WidgetLookManager::getSingleton();
		if (!d_lookName.empty())
		{
			d_windowRenderer->onLookNFeelUnassigned();
			const WidgetLookFeel& wlf = wlMgr.getWidgetLook(d_lookName);
			wlf.cleanUpWidget(*this);
		}

		d_lookName = look;
		Logger::getSingleton().logEvent("Assigning LookNFeel '" + look +
			"' to window '" + d_name + "'.", Informative);

		// Work to initialise the look and feel...
		const WidgetLookFeel& wlf = wlMgr.getWidgetLook(look);
		// Get look and feel to initialise the widget as it needs.
		wlf.initialiseWidget(*this);
		// do the necessary binding to the stuff added by the look and feel
		initialiseComponents(bClone);
		// let the window renderer know about this
		d_windowRenderer->onLookNFeelAssigned();
	}
	else
	{
		initialiseComponents(bClone);
		d_windowRenderer->onLookNFeelAssigned();
	}

	invalidate();
}

//----------------------------------------------------------------------------//
void Window::setModalState(bool state)
{
	// do nothing if state isn't changing
	if (getModalState() == state)
		return;

	// if going modal
	if (state)
	{
		activate();
		System::getSingleton().setModalTarget(this);
	}
	// clear the modal target
	else
		System::getSingleton().setModalTarget(0);
}

//----------------------------------------------------------------------------//
void Window::performChildWindowLayout()
{
	if (d_lookName.empty())
		return;

	// here we just grab the look and feel and get it to layout it's children
	try
	{
		const WidgetLookFeel& wlf =
			WidgetLookManager::getSingleton().getWidgetLook(d_lookName);
		// get look'n'feel to layout any child windows it created.
		wlf.layoutChildWidgets(*this);
	}
	catch (UnknownObjectException&)
	{
		Logger::getSingleton().logEvent("Window::performChildWindowLayout: "
			"assigned widget look was not found.", Errors);
	}

	if (d_windowRenderer)
		d_windowRenderer->performChildWindowLayout();
}

//----------------------------------------------------------------------------//
const String& Window::getUserString(const String& name) const
{
	UserStringMap::const_iterator iter = d_userStrings.find(name);

	if (iter == d_userStrings.end())
	{
#if (defined WIN32) && _DEBUG
		throw UnknownObjectException("Window::getUserString: a user string "
			"named '" + name + "' is not defined for Window '" + d_name + "'.");
#endif
		return "";
	}

	return (*iter).second;
}

//----------------------------------------------------------------------------//
bool Window::isUserStringDefined(const String& name) const
{
	return d_userStrings.find(name) != d_userStrings.end();
}

//----------------------------------------------------------------------------//
void Window::setUserString(const String& name, const String& value)
{
	d_userStrings[name] = value;
}
#ifdef FORCEGUIEDITOR
//----------------------------------------------------------------------------//
void Window::writeXMLToStream(XMLSerializer& xml_stream) const
{
	// just stop now if we are'nt allowed to write XML
	if (!d_allowWriteXML)
		return;

	// output opening Window tag
	xml_stream.openTag("Window")
		.attribute("Type", getType());
	// write name if not auto-generated
	if (getName().compare(0, WindowManager::GeneratedWindowNameBase.length(),
		WindowManager::GeneratedWindowNameBase) != 0)
	{
		xml_stream.attribute("Name", getName());
	}
	// write out properties.
	writePropertiesXML(xml_stream);
	// write out attached child windows.
	writeChildWindowsXML(xml_stream);
	// now ouput closing Window tag
	xml_stream.closeTag();
}

//----------------------------------------------------------------------------//
int Window::writePropertiesXML(XMLSerializer& xml_stream) const
{
	int propertiesWritten = 0;
	PropertySet::Iterator iter = PropertySet::getIterator();

	while (!iter.isAtEnd())
	{
		// first we check to make sure the property is'nt banned from XML
		if (!isPropertyBannedFromXML(iter.getCurrentValue()))
		{
			try
			{
				// only write property if it's not at the default state
				if (!isPropertyAtDefault(iter.getCurrentValue()))
				{
					iter.getCurrentValue()->writeXMLToStream(this, xml_stream);
					++propertiesWritten;
				}
			}
			catch (InvalidRequestException&)
			{
				// This catches errors from the MultiLineColumnList for example
				Logger::getSingleton().logEvent(
					"Window::writePropertiesXML: property receiving failed.  "
					"Continuing...", Errors);
			}
		}

		++iter;
	}

	return propertiesWritten;
}

//----------------------------------------------------------------------------//
int Window::writeChildWindowsXML(XMLSerializer& xml_stream) const
{
	int windowsWritten = 0;

	for (uint i = 0; i < getChildCount(); ++i)
	{
		const Window* const child = d_children[i];

		// conditional to ensure that auto created windows are handled
		// seperately.
		if (!child->isAutoWindow())
		{
			child->writeXMLToStream(xml_stream);
			++windowsWritten;
		}
		// this is one of those auto created windows so we do some special
		// handling
		else if (child->writeAutoChildWindowXML(xml_stream))
		{
			++windowsWritten;
		}
	}

	return windowsWritten;
}

//----------------------------------------------------------------------------//
bool Window::writeAutoChildWindowXML(XMLSerializer& xml_stream) const
{

	// just stop now if we are'nt allowed to write XML
	if (!d_allowWriteXML)
		return false;

	// we temporarily output to this string stream to see if have to do the tag
	// at all.  Make sure this stream does UTF-8
	std::ostringstream ss;
	XMLSerializer xml(ss);
	xml.openTag("AutoWindow");
	// Create the XML Child Tree
	// write out properties.
	writePropertiesXML(xml);
	// write out attached child windows.
	writeChildWindowsXML(xml);
	xml.closeTag();
	if (xml.getTagCount() <= 1)
		return false;

	// output opening AutoWindow tag
	xml_stream.openTag("AutoWindow");
	// extract the name suffix
	String suffix(getName(), getParent()->getName().length(), String::npos);
	// write name suffix attribute
	xml_stream.attribute("NameSuffix", suffix);
	// Inefficient : do the XML serialization again
	// write out properties.
	writePropertiesXML(xml_stream);
	// write out attached child windows.
	writeChildWindowsXML(xml_stream);
	xml_stream.closeTag();
	return true;
}
#endif

//----------------------------------------------------------------------------//
void Window::addWindowToDrawList(Window& wnd, bool at_back)
{
	// add behind other windows in same group
	if (at_back)
	{
		// calculate position where window should be added for drawing
		ChildList::iterator pos = d_drawList.begin();
		if (wnd.isTopMost())
		{
			//find first top-most window
			while ((pos != d_drawList.end()) && (!(*pos)->isTopMost()))
				++pos;
		}
		else if (wnd.isAlwaysOnTop())
		{
			// find first always-on-top window
			while ((pos != d_drawList.end()) && (!(*pos)->isAlwaysOnTop()))
				++pos;
		}
		else
		{
			while ((pos != d_drawList.end()) && ((*pos)->isAlwaysOnBottom()))
				++pos;
		}
		// add window to draw list
		d_drawList.insert(pos, &wnd);
	}
	// add in front of other windows in group
	else
	{
		ChildList::iterator position = d_drawList.begin();

		//if it can show with modal window but not can be clicked, then draw it under the modal window. by liugeng
		if (System::getSingleton().getGUISheet() == this && System::getSingleton().getModalTarget() &&
			!wnd.isAllowModalState() && wnd.isAllowShowWithModalState())
		{
			ChildList::iterator position = std::find(d_drawList.begin(), d_drawList.end(), System::getSingleton().getModalTarget());

			// add window to draw list
			d_drawList.insert(position, &wnd);
		}
		else
		{
			// calculate position where window should be added for drawing
			ChildList::reverse_iterator position = d_drawList.rbegin();

			if (wnd.isModalAfterShow()) //add by liugeng
			{
				// find last messagetip or allow-modal-state window
				while ((position != d_drawList.rend()) &&
					(((*position)->isAlwaysOnTop() && !(*position)->d_AllowShowWithModalState) || (*position)->isTopMost() || ((*position)->getType() == "TaharezLook/MessageTip")))
					++position;
			}
			else if (!wnd.isAlwaysOnTop() && !wnd.isTopMost())
			{
				// find last non-alway-on-top window
				if (wnd.isAlwaysOnBottom())
				{
					while ((position != d_drawList.rend()) && (!(*position)->isAlwaysOnBottom()))
						++position;

				}
				else
				{
					while ((position != d_drawList.rend()) && ((*position)->isAlwaysOnTop() || (*position)->isTopMost()))
						++position;
				}

			}
			else if (wnd.isTopMost())        //top-most
			{
				// find last messagetip window
				while ((position != d_drawList.rend()) && ((*position)->getType() == "TaharezLook/MessageTip"))
					++position;
			}
			else if (wnd.isAlwaysOnTop())   //alway-on-top
			{
				// find last non-top-most window
				while ((position != d_drawList.rend()) && ((*position)->getType() == "TaharezLook/MessageTip" || (*position)->isTopMost()))
					++position;
			}

			// add window to draw list
			d_drawList.insert(position.base(), &wnd);
		}

		// ycl ֻ�� debug ģʽ�´� log
#if defined(_DEBUG)
		if (Logger::getSingleton().getLoggingLevel() >= Standard)
		{
			Logger::getSingleton().logEvent("**********************\n");
			//OutputDebugStringA("**********************\n");
			for (size_t i = 0; i < d_drawList.size(); i++)
			{
				Logger::getSingleton().logEvent("name = " + d_drawList[i]->getName() + "\n");
				//String s = "name = " + d_drawList[i]->getName() + "\n";
				//OutputDebugStringA(s.c_str());
			}
			Logger::getSingleton().logEvent("&&&&&&&&&&&&&&&&&&&&&&\n");
			//OutputDebugStringA("&&&&&&&&&&&&&&&&&&&&&&\n");
		}
#endif
	}
}

//----------------------------------------------------------------------------//
void Window::removeWindowFromDrawList(const Window& wnd)
{
	// if draw list is not empty
	if (!d_drawList.empty())
	{
		// attempt to find the window in the draw list
		const ChildList::iterator position =
			std::find(d_drawList.begin(), d_drawList.end(), &wnd);

		// remove the window if it was found in the draw list
		if (position != d_drawList.end())
			d_drawList.erase(position);
	}
}

//----------------------------------------------------------------------------//
void Window::bringWindowAbove(Window* upperWnd, Window* lowerWnd)
{
	if (!upperWnd || !lowerWnd || upperWnd == lowerWnd)
	{
		return;
	}

	const ChildList::iterator upperIt = std::find(d_drawList.begin(), d_drawList.end(), upperWnd);
	if (upperIt == d_drawList.end())
	{
		return;
	}

	ChildList::iterator lowerIt = std::find(d_drawList.begin(), d_drawList.end(), lowerWnd);
	if (lowerIt == d_drawList.end() || upperIt == lowerIt + 1)
	{
		return;
	}

	d_drawList.erase(upperIt);
	lowerIt = std::find(d_drawList.begin(), d_drawList.end(), lowerWnd);
	d_drawList.insert(++lowerIt, upperWnd);
}

//----------------------------------------------------------------------------//
Window* Window::getActiveSibling()
{
	// initialise with this if we are active, else 0
	Window* activeWnd = isActive() ? this : 0;

	// if active window not already known, and we have a parent window
	if (!activeWnd && d_parent)
	{
		// scan backwards through the draw list, as this will
		// usually result in the fastest result.
		size_t idx = d_parent->getChildCount();
		while (idx-- > 0)
		{
			// if this child is active
			if (d_parent->d_drawList[idx]->isActive())
			{
				// set the return value
				activeWnd = d_parent->d_drawList[idx];
				// exit loop early, as we have found what we needed
				break;
			}
		}
	}

	// return whatever we discovered
	return activeWnd;
}

// 修改前缀名字.
bool Window::renameTemplatePrefix(const String& sPrefix)
{
	/*
		"@template@
	*/
	static size_t templateNameSize = strlen("@template@");

	if (this->d_name.length() < templateNameSize)
		return false;

	if (strncmp(d_name.c_str(), "@template@", templateNameSize))
		return false;

	String wndName = &(d_name.c_str()[templateNameSize]);
	d_name = sPrefix + wndName;

	WindowManager& winMgr = WindowManager::getSingleton();
	winMgr.registerWindow(this, sPrefix, wndName);

	for (size_t i = 0; i < this->getChildCount(); ++i)
	{
		Window* wnd = this->getChildAtIdx(i);
		if (!wnd->renameTemplatePrefix(sPrefix))
			return false;
	}

	return true;
}

//----------------------------------------------------------------------------//
void Window::rename(const String& new_name)
{
	WindowManager& winMgr = WindowManager::getSingleton();
	/*
	* Client code should never call this, but again, since we know people do
	* not read and stick to the API reference, here is some built-in protection
	* which ensures that things are handled via the WindowManager anyway.
	*/
	if (winMgr.isWindowPresent(d_name))
	{
		winMgr.renameWindow(this, new_name);
		// now we return, since the work was already done when WindowManager
		// re-called this function in the proper manner.
		return;
	}

	if (winMgr.isWindowPresent(new_name))
		throw AlreadyExistsException("Window::rename - Failed to rename "
		"Window: " + d_name + " as: " + new_name + ".  A Window named:" +
		new_name + "' already exists within the system.");

	// rename Falagard created child windows
	if (!d_lookName.empty())
	{
		const WidgetLookFeel& wlf =
			WidgetLookManager::getSingleton().getWidgetLook(d_lookName);

		// get WidgetLookFeel to rename the children it created
		wlf.renameChildren(*this, new_name);
	}

	// how to detect other auto created windows.
	const String autoPrefix(d_name + AutoWidgetNameSuffix);
	// length of current name
	const size_t oldNameLength = d_name.length();

	// now rename all remaining auto-created windows attached
	for (size_t i = 0; i < getChildCount(); ++i)
	{
		// is this an auto created window that we created?
		if (!d_children[i]->d_name.compare(0, autoPrefix.length(), autoPrefix))
		{
			winMgr.renameWindow(d_children[i],
				new_name +
				d_children[i]->d_name.substr(oldNameLength));
		}
	}

	// log this under informative level
	Logger::getSingleton().logEvent("Renamed window: " + d_name +
		" as: " + new_name,
		Informative);

	// finally, set our new name
	d_name = new_name;
}

//----------------------------------------------------------------------------//
void Window::onSized(WindowEventArgs& e)
{
	// resize the underlying RenderingWindow if we're using such a thing
	if (d_surface && d_surface->isRenderingWindow())
		static_cast<RenderingWindow*>(d_surface)->setSize(getPixelSize());

	// screen area changes when we're resized.
	// NB: Called non-recursive since the onParentSized notifications will deal
	// more selectively with child Window cases.
	notifyScreenAreaChanged(false);

	// we need to layout loonfeel based content first, in case anything is
	// relying on that content for size or positioning info (i.e. some child
	// is used to establish inner-rect position or size).
	//
	// TODO: The subsequent onParentSized notification for those windows cause
	// additional - unneccessary - work; we should look to optimise that.
	performChildWindowLayout();

	// inform children their parent has been re-sized
	const size_t child_count = getChildCount();
	for (size_t i = 0; i < child_count; ++i)
	{
		WindowEventArgs args(this);
		d_children[i]->onParentSized(args, d_oldSize);
	}

	invalidate();

	fireEvent(EventSized, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onMoved(WindowEventArgs& e)
{
	notifyScreenAreaChanged();

	// handle invalidation of surfaces and trigger needed redraws
	if (d_parent)
	{
		d_parent->invalidateRenderingSurface();
		// need to redraw some geometry if parent uses a caching surface
		if (d_parent->getTargetRenderingSurface().isRenderingWindow())
			System::getSingleton().signalRedraw();
	}

	fireEvent(EventMoved, e, EventNamespace);
	RefreshAlignWindowPos();  //À¢–¬Õ£øø’‚∏ˆ¥∞ø⁄µƒ¥∞ø⁄µƒŒª÷�?
	//fireEvent(EventAlignWindowMoved,e,EventNamespace);

}

//----------------------------------------------------------------------------//
void Window::onTextChanged(WindowEventArgs& e)
{
	invalidate();
	fireEvent(EventTextChanged, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onFontChanged(WindowEventArgs& e)
{
	// This was added to enable the Falagard FontDim to work
	// properly.  A better, more selective, solution would
	// probably be to do something funky with events ;)
	performChildWindowLayout();

	invalidate();
	fireEvent(EventFontChanged, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onAlphaChanged(WindowEventArgs& e)
{
	// scan child list and call this method for all children that inherit alpha
	const size_t child_count = getChildCount();

	for (size_t i = 0; i < child_count; ++i)
	{
		if (d_children[i]->inheritsAlpha())
		{
			WindowEventArgs args(d_children[i]);
			d_children[i]->onAlphaChanged(args);
		}

	}

	invalidate();
	fireEvent(EventAlphaChanged, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onIDChanged(WindowEventArgs& e)
{
	fireEvent(EventIDChanged, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onShown(WindowEventArgs& e)
{
	invalidate(true);
	fireEvent(EventShown, e, EventNamespace);
	System::getSingleton().UpdateKeyBoardTargetWnd();
}

//----------------------------------------------------------------------------//
void Window::onHidden(WindowEventArgs& e)
{
	// first deactivate window if it is the active window.
	if (isActive())
		deactivate();
	if (getModalState())
	{
		setModalState(false);
	}

	invalidate(true);

	{
		Window* pKeyBoardTarget = System::getSingleton().getKeyboardTargetWindow();
		if (pKeyBoardTarget&&!pKeyBoardTarget->isVisible())
		{
			System::getSingleton().UpdateKeyBoardTargetWnd();
		}

	}
	fireEvent(EventHidden, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onEnabled(WindowEventArgs& e)
{
	// signal all non-disabled children that they are now enabled
	// (via inherited state)
	const size_t child_count = getChildCount();
	for (size_t i = 0; i < child_count; ++i)
	{
		if (d_children[i]->d_enabled)
		{
			WindowEventArgs args(d_children[i]);
			d_children[i]->onEnabled(args);
		}
	}

	Window* pKeyBoardTarget = System::getSingleton().getKeyboardTargetWindow();
	if (pKeyBoardTarget&&!pKeyBoardTarget->isDisabled())
	{
		System::getSingleton().UpdateKeyBoardTargetWnd();
	}

	invalidate();
	fireEvent(EventEnabled, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onDisabled(WindowEventArgs& e)
{
	// signal all non-disabled children that they are now disabled
	// (via inherited state)
	const size_t child_count = getChildCount();
	for (size_t i = 0; i < child_count; ++i)
	{
		if (d_children[i]->d_enabled)
		{
			WindowEventArgs args(d_children[i]);
			d_children[i]->onDisabled(args);
		}
	}

	Window* pKeyBoardTarget = System::getSingleton().getKeyboardTargetWindow();
	if (pKeyBoardTarget&&!pKeyBoardTarget->isDisabled())
	{
		System::getSingleton().UpdateKeyBoardTargetWnd();
	}

	invalidate();
	fireEvent(EventDisabled, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onClippingChanged(WindowEventArgs& e)
{
	invalidate();
	notifyClippingChanged();
	fireEvent(EventClippedByParentChanged, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onParentDestroyChanged(WindowEventArgs& e)
{
	fireEvent(EventDestroyedByParentChanged, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onInheritsAlphaChanged(WindowEventArgs& e)
{
	invalidate();
	fireEvent(EventInheritsAlphaChanged, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onAlwaysOnTopChanged(WindowEventArgs& e)
{
	// we no longer want a total redraw here, instead we just get each window
	// to resubmit it's imagery to the Renderer.
	System::getSingleton().signalRedraw();
	fireEvent(EventAlwaysOnTopChanged, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onTopMostChanged(WindowEventArgs& e)
{
	// we no longer want a total redraw here, instead we just get each window
	// to resubmit it's imagery to the Renderer.
	System::getSingleton().signalRedraw();
	fireEvent(EventTopMostChanged, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onCaptureGained(WindowEventArgs& e)
{
	fireEvent(EventInputCaptureGained, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onCaptureLost(WindowEventArgs& e)
{
	// reset auto-repeat state
	d_repeatButton = NoButton;

	// handle restore of previous capture window as required.
	if (d_restoreOldCapture && (d_oldCapture != 0)) {
		d_oldCapture->onCaptureLost(e);
		d_oldCapture = 0;
	}

	d_BeingDrag = false;

	// handle case where mouse is now in a different window
	// (this is a bit of a hack that uses the mouse input injector to handle
	// this for us).
	if (isInChatOutWnd())
	{
		System::getSingleton().injectMouseMove(0, 0);

	}
	else if (!isParentMousePassThroughEnabled())
	{
		System::getSingleton().injectMouseMove(0, 0);
	}




	fireEvent(EventInputCaptureLost, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onRenderingStarted(WindowEventArgs& e)
{
	fireEvent(EventRenderingStarted, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onRenderingEnded(WindowEventArgs& e)
{
	fireEvent(EventRenderingEnded, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onZChanged(WindowEventArgs& e)
{
	// we no longer want a total redraw here, instead we just get each window
	// to resubmit it's imagery to the Renderer.
	System::getSingleton().signalRedraw();
	fireEvent(EventZOrderChanged, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onDestructionStarted(WindowEventArgs& e)
{
	d_destructionStarted = true;
	fireEvent(EventDestructionStarted, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onActivated(ActivationEventArgs& e)
{
	d_active = true;
	invalidate();
	fireEvent(EventActivated, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onDeactivated(ActivationEventArgs& e)
{
	// first de-activate all children
	size_t i = 0;
	while (true)
	{
		if (i < d_children.size())
		{
			Window* childWnd = d_children[i];
			assert(childWnd);
			if (childWnd->isActive())
			{
				// make sure the child gets itself as the .window member
				ActivationEventArgs child_e(childWnd);
				child_e.otherWindow = e.otherWindow;
				d_children[i]->onDeactivated(child_e);
			}

			if (i < d_children.size() && childWnd == d_children[i])
			{
				++i;
			}
		}
		else
			break;
	}
	
	d_active = false;

	invalidate();
	fireEvent(EventDeactivated, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onParentSized(WindowEventArgs& e, const Size& oldParentSize)
{
	// set window area back on itself to cause minimum and maximum size
	// constraints to be applied as required.  (fire no events though)
#ifdef _EDITOR_
	UVector2 newPos;
	UVector2 newSize;
	URect newArea(d_area);
	Size newParentSize(getParentPixelSize());
	if (!d_AutoSizeWithParent&&!d_initialising&&this != System::getSingleton().getGUISheet())
	{

		float offsetX = oldParentSize.d_width*d_area.d_min.d_x.d_scale + d_area.d_min.d_x.d_offset;
		float offsetY = oldParentSize.d_height*d_area.d_min.d_y.d_scale + d_area.d_min.d_y.d_offset;
		/*float width=d_area.getSize().asAbsolute(oldParentSize).d_x;
		float height=d_area.getSize().asAbsolute(oldParentSize).d_y;*/

		float width = d_pixelSize.d_width;
		float height = d_pixelSize.d_height;

		float NewParentWidth = newParentSize.d_width;
		float NewParentHeight = newParentSize.d_height;

		float oldParentWidth = oldParentSize.d_width;
		float oldParentHeight = oldParentSize.d_height;

		if (d_area.d_min.d_x.d_offset == 0)  //…œ≤ø π”√œ‡∂‘◊¯±Íœµ
		{
			newArea.d_min.d_x.d_scale = offsetX / NewParentWidth;
		}
		else
		{
			newArea.d_min.d_x.d_offset = offsetX - d_area.d_min.d_x.d_scale*NewParentWidth;
		}

		if (d_area.d_min.d_y.d_offset == 0)  //…œ≤ø π”√œ‡∂‘◊¯±Íœµ
		{
			newArea.d_min.d_y.d_scale = offsetY / NewParentHeight;
		}
		else
		{
			newArea.d_min.d_y.d_offset = offsetY - d_area.d_min.d_y.d_scale*NewParentHeight;
		}

		///////
		if (d_area.d_max.d_x.d_offset == 0)  //…œ≤ø π”√œ‡∂‘◊¯±Íœµ
		{
			newArea.d_max.d_x.d_scale = (offsetX + width) / NewParentWidth;
		}
		else
		{
			newArea.d_max.d_x.d_offset = offsetX + width - d_area.d_max.d_x.d_scale*NewParentWidth;
		}

		if (d_area.d_max.d_y.d_offset == 0)  //…œ≤ø π”√œ‡∂‘◊¯±Íœµ
		{
			newArea.d_max.d_y.d_scale = (offsetY + height) / NewParentHeight;
		}
		else
		{
			newArea.d_max.d_y.d_offset = offsetY + height - d_area.d_max.d_y.d_scale*NewParentHeight;
		}

		//float sleft = 0.0f;
		//float sright = 0.0f;
		//float stop = 0.0f;
		//float sbottom = 0.0f;
		//float aleft = 0.0f;
		//float aright = 0.0f;
		//float atop = 0.0f;
		//float abottom = 0.0f;
		//// If offset being used, move absolutely
		//if (d_area.d_min.d_x.d_offset == 0)		sleft = left;		else	aleft = left;
		//if (d_area.d_max.d_x.d_offset == 0)		sright = right;		else	aright = right;
		//if (d_area.d_min.d_y.d_offset == 0)		stop = top;			else	atop = top;
		//if (d_area.d_max.d_y.d_offset == 0)		sbottom = bottom;	else	abottom = bottom;

		//setArea_impl(newPos, newSize, false, false);
		setArea(newArea);

	}
#endif

	setArea_impl(d_area.getPosition(), d_area.getSize(), false, false);

	const bool moved =
		((d_area.d_min.d_x.d_scale != 0) || (d_area.d_min.d_y.d_scale != 0) ||
		(d_horzAlign != HA_LEFT) || (d_vertAlign != VA_TOP));
	const bool sized =
		((d_area.d_max.d_x.d_scale != 0) || (d_area.d_max.d_y.d_scale != 0) ||
		isInnerRectSizeChanged());

	// now see if events should be fired.
	if (moved)
	{
		WindowEventArgs args(this);
		onMoved(args);
	}

	if (sized)
	{
		WindowEventArgs args(this);
		onSized(args);
	}

	// if we were not moved or sized, do child layout anyway!
	if (!(moved || sized))
		performChildWindowLayout();

	fireEvent(EventParentSized, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onChildAdded(WindowEventArgs& e)
{
	// we no longer want a total redraw here, instead we just get each window
	// to resubmit it's imagery to the Renderer.
	System::getSingleton().signalRedraw();
	fireEvent(EventChildAdded, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onChildRemoved(WindowEventArgs& e)
{
	// we no longer want a total redraw here, instead we just get each window
	// to resubmit it's imagery to the Renderer.
	System::getSingleton().signalRedraw();
	fireEvent(EventChildRemoved, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onMouseEnters(MouseEventArgs& e)
{
	// set the mouse cursor
	//MouseCursor::getSingleton().setImage(getMouseCursor());

	// perform tooltip control
	/* yanji 20150917 
	Tooltip* const tip = getTooltip();
	if (tip && !isAncestor(tip))
		tip->setTargetWindow(this);
	*/

	fireEvent(EventMouseEnters, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onMouseLeaves(MouseEventArgs& e)
{
	// perform tooltip control
	if (d_recognizerManager->onMouseLeaves(e)) {
		++e.handled;
		return;
	}
	/* yanji 20150917 
	const Window* const mw = System::getSingleton().getWindowContainingMouse();
	Tooltip* const tip = getTooltip();
	if (tip && mw != tip && !(mw && mw->isAncestor(tip)))
		tip->setTargetWindow(0);
	*/

	fireEvent(EventMouseLeaves, e, EventNamespace);

	releaseInput();
}

//----------------------------------------------------------------------------//
void Window::onMouseMove(MouseEventArgs& e)
{
	// perform tooltip control
	Tooltip* const tip = getTooltip();
	if (tip)
		tip->resetTimer();

	//	if (d_BeingDrag)
	//	{
	//		if (GetDragMoveEnable())
	//		{
	//			onDragMove(e.moveDelta);
	//		}
	//	}
	//    NOTIFY_GESTURE(onMouseMove);
	if (Window::getCaptureWindow() && (this == Window::getCaptureWindow() || Window::getCaptureWindow()->isAncestor(this)))
	{
		if (d_recognizerManager->onMouseMove(e)) {
			e.handled++;
			return;
		}
	}

	fireEvent(EventMouseMove, e, EventNamespace);
	// by default we now mark mouse events as handled
	// (derived classes may override, of course!)
	++e.handled;
}

void Window::onDragMove(const Vector2& delta)
{
	if (GetDragMoveEnable() && d_HandleDragMove)
	{
		offsetPixelPosition(delta);
		return;
	}
	if (d_parent)
	{
		d_parent->onDragMove(delta);
	}
}

void Window::OnRightButtonClose()
{
	if (GetDragMoveEnable() && d_HandleDragMove&&d_RButtonCloseEnable)
	{
		MouseEventArgs e(this);
		e.button = RightButton;

		fireEvent(EventMouseButtonDown, e, EventNamespace);
		return;
	}
	if (d_parent)
	{
		d_parent->OnRightButtonClose();
	}
}



//----------------------------------------------------------------------------//
void Window::onMouseWheel(MouseEventArgs& e)
{
	fireEvent(EventMouseWheel, e, EventNamespace);
	// by default we now mark mouse events as handled
	// (derived classes may override, of course!)
	++e.handled;
}

//----------------------------------------------------------------------------//
void Window::onMouseButtonDown(MouseEventArgs& e)
{
	// perform tooltip controls
	printf("you click window=%s\n", getName().c_str());

	Tooltip* const tip = getTooltip();
	//yanji 20150917
		
	if (tip && !isAncestor(tip)) {
		String tipText = this->getTooltipText();
		if (tipText.size() > 0)
			tip->setTargetWindow(this);
	}
		

	if ((e.button == LeftButton) && moveToFront_impl(true))
		++e.handled;

	// if auto repeat is enabled and we are not currently tracking
	// the button that was just pushed (need this button check because
	// it could be us that generated this event via auto-repeat).
	if (d_autoRepeat)
	{
		if (d_repeatButton == NoButton)
			captureInput();

		if ((d_repeatButton != e.button) && isCapturedByThis())
		{
			d_repeatButton = e.button;
			d_repeatElapsed = 0;
			d_repeating = false;
		}
	}
	if (e.button == LeftButton
		//        &&GetDragMoveEnable()&&!d_BeingDrag
		)
	{
		captureInput();
		//		if (isCapturedByThis())
		//		{
		//			d_BeingDrag=true;
		//		}

	}

	//    bool handled = false;
	//    for (std::vector<CEGUI::Gesture::CEGUIGestureRecognizer*>::iterator iter = d_gestureRecognizers.begin(); iter != d_gestureRecognizers.end(); iter++) {
	//        if ((*iter)->onMouseButtonDown(e))
	//            handled = true;
	//    }
	//    if (handled) { e.handled++; return; }
	//    NOTIFY_GESTURE(onMouseButtonDown);
	if (d_recognizerManager->onMouseButtonDown(e)) {
		++e.handled;
		return;
	}
	// by default we now mark mouse events as handled
	// (derived classes may override, of course!)

	if (e.button == RightButton&&GetDragMoveEnable())
	{
		OnRightButtonClose();
	}
	else
	{
		fireEvent(EventMouseButtonDown, e, EventNamespace);
	}
	++e.handled;
	//    for (std::vector<CEGUI::Gesture::CEGUIGestureRecognizer*>::iterator iter = d_gestureRecognizers.begin(); iter != d_gestureRecognizers.end(); iter++) {
	//        (*iter)->onMouseButtonDown(e);
	//    }
}

void Window::CheckGuideEnd(MouseButton button)
{
	if (HasGuide())
	{
		if (button == LeftButton || button == RightButton)
		{
			d_HasGuide = false;
			WindowEventArgs e(this);
			fireEvent(EventGuideEnd, e, EventNamespace);
		}
	}
}

//----------------------------------------------------------------------------//
void Window::onMouseButtonUp(MouseEventArgs& e)
{
	printf("window=%s mousebuttonup \n", getName().c_str());
		
	//yanji 20150917
	Tooltip* const tip = getTooltip();
	if (tip && !isAncestor(tip)) {
		tip->setTargetWindow(0);
	}
		
	// reset auto-repeat state
	if (d_autoRepeat && d_repeatButton != NoButton)
	{
		releaseInput();
		d_repeatButton = NoButton;
	}
	if (e.button == LeftButton&&isCapturedByThis())
	{

		releaseInput();
	}
	//    NOTIFY_GESTURE(onMouseButtonUp);
	if (d_recognizerManager->onMouseButtonUp(e)) {
		++e.handled;
		d_bUpMsgChangedGesture = true;
		return;
	}
	d_bUpMsgChangedGesture = false;
	fireEvent(EventMouseButtonUp, e, EventNamespace);
	// by default we now mark mouse events as handled
	// (derived classes may override, of course!)
	++e.handled;
}

//----------------------------------------------------------------------------//
void Window::onLostForce(MouseEventArgs& e)
{
	//    NOTIFY_GESTURE(onMouseButtonUp);
	if (d_recognizerManager->onMouseButtonUp(e)) {
		++e.handled;
		d_bUpMsgChangedGesture = true;
		return;
	}
	d_bUpMsgChangedGesture = false;
	++e.handled;
}

//----------------------------------------------------------------------------//
void Window::onMouseClicked(MouseEventArgs& e)
{
	if (d_bUpMsgChangedGesture) return;
#ifdef WIN32
	std::string strName(d_name.c_str());
#endif // WIN32
	try
	{
		fireEvent(EventMouseClick, e, EventNamespace);
		CheckGuideEnd(e.button);
		// if event was directly injected, mark as handled to be consistent with
		// other mouse button injectors
		if (!System::getSingleton().isMouseClickEventGenerationEnabled())
			++e.handled;
	}
	catch (...)
	{
		throw UnknownObjectException("Window::onMouseClicked, window name: - " + d_name);
	}
}

//----------------------------------------------------------------------------//
void Window::onMouseDoubleClicked(MouseEventArgs& e)
{
	if (d_bUpMsgChangedGesture) return;
	fireEvent(EventMouseDoubleClick, e, EventNamespace);

	// if event was directly injected, mark as handled to be consistent with
	// other mouse button injectors
	if (!System::getSingleton().isMouseClickEventGenerationEnabled())
		++e.handled;
	++e.handled;
}

//----------------------------------------------------------------------------//
void Window::onMouseTripleClicked(MouseEventArgs& e)
{
	fireEvent(EventMouseTripleClick, e, EventNamespace);

	// if event was directly injected, mark as handled to be consistent with
	// other mouse button injectors
	if (!System::getSingleton().isMouseClickEventGenerationEnabled())
		++e.handled;
	++e.handled;
}

bool Window::onLongPress(Gesture::CEGUIGestureRecognizer* pRecognizer) {
	//    GestureEventArgs ge(pRecognizer);
	//    MouseEventArgs* e = (MouseEventArgs*)pRecognizer->GetEvent();
	////    printf("you long press (%f, %f), state=%u\n", e->position.d_x, e->position.d_y, e->clickCount);
	//    if (pRecognizer->GetState() == Gesture::GestureRecognizerStateEnded && e->window)
	//        e->window->releaseInput();
	//    fireEvent(EventLongPress, ge, EventNamespace);
	//    if (!System::getSingleton().isMouseClickEventGenerationEnabled())
	//        ++e->handled;
	//	++e->handled;
	return true;
}

bool Window::onMouseSlide(Gesture::CEGUIGestureRecognizer* pRecognizer)
{
	MouseEventArgs* e = (MouseEventArgs*)pRecognizer->GetEvent();
	if (e == NULL) return false;

	if (!d_SlideEnable&&d_parent != NULL) {
		d_parent->onMouseSlide(pRecognizer);
		++e->handled;
		return true;
	}
	if (d_SlideEnable) {
		fireEvent(EventSlide, *e, EventNamespace);
		if (!System::getSingleton().isMouseClickEventGenerationEnabled())
			++e->handled;
	}

	++e->handled;
	return true;
}

bool Window::onMouseDrag(Gesture::CEGUIGestureRecognizer* pRecognizer)
{
	//add by liugeng, 2016.4.8
	if (d_DragEnable) {
		GestureEventArgs ge(pRecognizer);
	    fireEvent(EventDrag, ge, EventNamespace);
	}

	return true;
}

//----------------------------------------------------------------------------//
void Window::onKeyDown(KeyEventArgs& e)
{
	fireEvent(EventKeyDown, e, EventNamespace);

	// As of 0.7.0 CEGUI::System no longer does input event propogation, so by
	// default we now do that here.  Generally speaking key handling widgets
	// may need to override this behaviour to halt further propogation.
	if (!e.handled && d_parent &&
		d_parent != System::getSingleton().getModalTarget())
	{
		e.window = d_parent;
		d_parent->onKeyDown(e);
	}
}

//----------------------------------------------------------------------------//
void Window::onKeyUp(KeyEventArgs& e)
{
	fireEvent(EventKeyUp, e, EventNamespace);

	// As of 0.7.0 CEGUI::System no longer does input event propogation, so by
	// default we now do that here.  Generally speaking key handling widgets
	// may need to override this behaviour to halt further propogation.
	if (!e.handled && d_parent &&
		d_parent != System::getSingleton().getModalTarget())
	{
		e.window = d_parent;
		d_parent->onKeyUp(e);
	}
}

//----------------------------------------------------------------------------//
void Window::onCharacter(KeyEventArgs& e)
{
	fireEvent(EventCharacterKey, e, EventNamespace);

	// As of 0.7.0 CEGUI::System no longer does input event propogation, so by
	// default we now do that here.  Generally speaking key handling widgets
	// may need to override this behaviour to halt further propogation.
	if (!e.handled && d_parent &&
		this != System::getSingleton().getModalTarget())
	{
		e.window = d_parent;
		d_parent->onCharacter(e);
	}
}

void Window::onKeyboardTargetWndChanged(WindowEventArgs& e)
{
	fireEvent(EventKeyboardTargetWndChanged, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onDragDropItemEnters(DragDropEventArgs& e)
{
	fireEvent(EventDragDropItemEnters, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onDragDropItemLeaves(DragDropEventArgs& e)
{
	fireEvent(EventDragDropItemLeaves, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onDragDropItemDropped(DragDropEventArgs& e)
{
	fireEvent(EventDragDropItemDropped, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onVerticalAlignmentChanged(WindowEventArgs& e)
{
	notifyScreenAreaChanged();

	fireEvent(EventVerticalAlignmentChanged, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onHorizontalAlignmentChanged(WindowEventArgs& e)
{
	notifyScreenAreaChanged();

	fireEvent(EventHorizontalAlignmentChanged, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::setWindowRenderer(const String& name,bool isClone)
{
	WindowRendererManager& wrm = WindowRendererManager::getSingleton();
	if (d_windowRenderer != 0)
	{
		// Allow reset of renderer
		if (d_windowRenderer->getName() == name)
			return;

		WindowEventArgs e(this);
		onWindowRendererDetached(e);
		wrm.destroyWindowRenderer(d_windowRenderer);
	}

	if (!name.empty())
	{
		Logger::getSingleton().logEvent("Assigning the window renderer '" +
			name + "' to the window '" + d_name + "'", Informative);
		d_windowRenderer = wrm.createWindowRenderer(name,isClone);
		if (!isClone)
		{
			WindowEventArgs e(this);
			onWindowRendererAttached(e);
		}
		else
		{
			d_windowRenderer->d_window = this;
		}
	}
	else
		throw InvalidRequestException("Window::setWindowRenderer: Attempt to "
		"assign a 'null' window renderer to window '" + d_name + "'.");
}

//----------------------------------------------------------------------------//
WindowRenderer* Window::getWindowRenderer(void) const
{
	return d_windowRenderer;
}

//----------------------------------------------------------------------------//
void Window::onWindowRendererAttached(WindowEventArgs& e)
{
	if (!validateWindowRenderer(d_windowRenderer->getClass()))
		throw InvalidRequestException("Window::onWindowRendererAttached: The "
		"window renderer '" + d_windowRenderer->getName() + "' is not "
		"compatible with this widget type (" + getType() + ")");

	if (!testClassName(d_windowRenderer->getClass()))
		throw InvalidRequestException("Window::onWindowRendererAttached: The "
		"window renderer '" + d_windowRenderer->getName() + "' is not "
		"compatible with this widget type (" + getType() + "). It requires "
		"a '" + d_windowRenderer->getClass() + "' based window type.");

	d_windowRenderer->d_window = this;
	d_windowRenderer->onAttach();
	fireEvent(EventWindowRendererAttached, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onWindowRendererDetached(WindowEventArgs& e)
{
	d_windowRenderer->onDetach();
	d_windowRenderer->d_window = 0;
	fireEvent(EventWindowRendererDetached, e, EventNamespace);
}

//----------------------------------------------------------------------------//
bool Window::validateWindowRenderer(const String&) const
{
	return true;
}

//----------------------------------------------------------------------------//
String Window::getWindowRendererName(void) const
{
	if (d_windowRenderer)
		return d_windowRenderer->getName();

	return String();
}
#ifdef FORCEGUIEDITOR
//----------------------------------------------------------------------------//
void Window::banPropertyFromXML(const Property* property)
{
	// check if the insertion failed
	if (!d_bannedXMLProperties.insert(property->getName()).second)
		// just log the incidence
		AlreadyExistsException("Window::banPropertyFromXML: The property '" +
		property->getName() + "' is already banned in window '" +
		d_name + "'");
}

//----------------------------------------------------------------------------//
bool Window::isPropertyBannedFromXML(const Property* property) const
{
	const BannedXMLPropertySet::const_iterator i =
		d_bannedXMLProperties.find(property->getName());
	return (i != d_bannedXMLProperties.end());
}
#endif
//----------------------------------------------------------------------------//
bool Window::isPropertyAtDefault(const Property* property) const
{
	// if we have a looknfeel we examine it for defaults
	if (!d_lookName.empty())
	{
		// if we're an autowindow, we check our parent's looknfeel's Child
		// section which we came from as we might be initialised there
		if (d_autoWindow && getParent() && !getParent()->getLookNFeel().empty())
		{
			const WidgetLookFeel& wlf =
				WidgetLookManager::getSingleton().
				getWidgetLook(getParent()->getLookNFeel());

			// find our name suffix
			const String suffix(getName(),
				getParent()->getName().length(),
				String::npos);

			// find the widget component if possible
			const WidgetComponent* const wc = wlf.findWidgetComponent(suffix);
			if (wc)
			{
				const PropertyInitialiser* const propinit =
					wc->findPropertyInitialiser(property->getName());

				if (propinit)
					return (getProperty(property->getName()) ==
					propinit->getInitialiserValue());
			}
		}

		// if the looknfeel has a new default for this property we compare
		// against that
		const WidgetLookFeel& wlf =
			WidgetLookManager::getSingleton().getWidgetLook(d_lookName);
		const PropertyInitialiser* const propinit =
			wlf.findPropertyInitialiser(property->getName());
		if (propinit)
			return (getProperty(property->getName()) ==
			propinit->getInitialiserValue());
	}

	// we dont have a looknfeel with a new value for this property so we rely
	// on the hardcoded default
	return property->isDefault(this);
}

//----------------------------------------------------------------------------//
void Window::notifyClippingChanged(void)
{
	d_outerRectClipperValid = false;
	d_innerRectClipperValid = false;
	d_hitTestRectValid = false;

	// inform children that their clipped screen areas must be updated
	const size_t num = d_children.size();
	for (size_t i = 0; i<num; ++i)
		if (d_children[i]->isClippedByParent())
			d_children[i]->notifyClippingChanged();
}

//----------------------------------------------------------------------------//
void Window::notifyScreenAreaChanged(bool recursive /* = true */)
{
	d_outerUnclippedRectValid = false;
	d_innerUnclippedRectValid = false;
	d_outerRectClipperValid = false;
	d_innerRectClipperValid = false;
	d_hitTestRectValid = false;

	updateGeometryRenderSettings();

	WindowEventArgs e(this);
	fireEvent(EventScreenAreaChange, e, EventNamespace);
	// inform children that their screen area must be updated
	if (recursive)
	{
		const size_t child_count = getChildCount();
		for (size_t i = 0; i < child_count; ++i)
			d_children[i]->notifyScreenAreaChanged();
	}
}

//----------------------------------------------------------------------------//
void Window::updateGeometryRenderSettings()
{
	RenderingContext ctx;
	getRenderingContext(ctx);

	// move the underlying RenderingWindow if we're using such a thing
	if (ctx.owner == this && ctx.surface->isRenderingWindow())
	{
		static_cast<RenderingWindow*>(ctx.surface)->
			setPosition(getUnclippedOuterRect().getPosition());
		static_cast<RenderingWindow*>(d_surface)->setPivot(
			Vector3(d_pixelSize.d_width / 2.0f,
			d_pixelSize.d_height / 2.0f,
			0.0f));
		d_geometry->setTranslation(Vector3(0.0f, 0.0f, 0.0f));
	}
	// if we're not texture backed, update geometry position.
	else
	{
		// position is the offset of the window on the dest surface.
		const Rect ucrect(getUnclippedOuterRect());
		d_geometry->setTranslation(Vector3(ucrect.d_left - ctx.offset.d_x,
			ucrect.d_top - ctx.offset.d_y, 0.0f));
	}

	bool bClipNeedConvert = (ctx.owner == NULL);
	d_geometry->setClippingRegionNeedConvert(bClipNeedConvert && d_clippedByParent);

	initialiseClippers(ctx);
}

//----------------------------------------------------------------------------//
EventSet::Iterator Window::getEventIterator() const
{
	return EventSet::getIterator();
}

//----------------------------------------------------------------------------//
PropertySet::Iterator Window::getPropertyIterator() const
{
	return PropertySet::getIterator();
}

//----------------------------------------------------------------------------//
bool Window::isDragDropTarget() const
{
	return d_dragDropTarget;
}

//----------------------------------------------------------------------------//
void Window::setDragDropTarget(bool setting)
{
	d_dragDropTarget = setting;
}

//-----------------------------------------------------------------------
void Window::setFalagardType(const String& type, const String& rendererType)
{
	// Retrieve the new widget look
	const String separator("/");
	String::size_type pos = type.find(separator);
	const String newLook(type, 0, pos);

	// Check if old one is the same. If so, ignore since we don't need to do
	// anything (type is already assigned)
	pos = d_falagardType.find(separator);
	String oldLook(d_falagardType, 0, pos);
	if (oldLook == newLook)
		return;

	// Obtain widget kind
	String widget(d_falagardType, pos + 1);

	// Build new type (look/widget)
	d_falagardType = newLook + separator + widget;

	// Set new renderer
	if (rendererType.length() > 0)
		setWindowRenderer(rendererType);

	// Apply the new look to the widget
	setLookNFeel(type,false);
}

//----------------------------------------------------------------------------//
bool Window::isTopOfZOrder() const
{
	// if not attached, then always on top!
	if (!d_parent)
		return true;

	// get position of window at top of z-order in same group as this window
	ChildList::reverse_iterator pos = d_parent->d_drawList.rbegin();
	if (!d_alwaysOnTop && !d_topMost)
	{
		// find last non-top-most window
		while ((pos != d_parent->d_drawList.rend()) && ((*pos)->isAlwaysOnTop() || (*pos)->isTopMost()))
			++pos;
	}

	// return whether the window at the top of the z order is us
	return *pos == this;
}

//----------------------------------------------------------------------------//
void Window::insertText(const String& text, const String::size_type position)
{
	d_textLogical.insert(position, text);
	d_bidiDataValid = false;

	WindowEventArgs args(this);
	onTextChanged(args);
}

//----------------------------------------------------------------------------//
void Window::appendText(const String& text)
{
	d_textLogical.append(text);
	d_bidiDataValid = false;

	WindowEventArgs args(this);
	onTextChanged(args);
}

//----------------------------------------------------------------------------//
GeometryBuffer* Window::getGeometryBuffer()
{
	return d_geometry;
}

//----------------------------------------------------------------------------//
void Window::getRenderingContext(RenderingContext& ctx) const
{
	if (d_windowRenderer)
		d_windowRenderer->getRenderingContext(ctx);
	else
		getRenderingContext_impl(ctx);
}

//----------------------------------------------------------------------------//
void Window::getRenderingContext_impl(RenderingContext& ctx) const
{
	//if (d_surface)
	//{
	//	ctx.surface = d_surface;
	//	ctx.owner = this;
	//	ctx.offset = getUnclippedOuterRect().getPosition();
	//	ctx.queue = RQ_BASE;
	//}
	//else if (d_parent)
	//{
	//	d_parent->getRenderingContext(ctx);
	//}
	//else
	//{
	//	ctx.surface =
	//		&System::getSingleton().getRenderer()->getDefaultRenderingRoot();
	//	ctx.owner = 0;
	//	ctx.offset = Vector2(0, 0);
	//	ctx.queue = RQ_BASE;
	//}

	// ycl �򸸴��ڵݹ���ø�Ϊѭ��ʵ��?
	const Window* pWnd = this;
	while (pWnd)
	{
		if (pWnd->d_surface)
		{
			ctx.surface = pWnd->d_surface;
			ctx.owner = pWnd;
			ctx.offset = pWnd->getUnclippedOuterRect().getPosition();
			ctx.queue = RQ_BASE;
			return;
		}
		pWnd = pWnd->d_parent;
	}

	ctx.surface =
		&System::getSingleton().getRenderer()->getDefaultRenderingRoot();
	ctx.owner = 0;
	ctx.offset = Vector2(0, 0);
	ctx.queue = RQ_BASE;
}

//----------------------------------------------------------------------------//
RenderingSurface* Window::getRenderingSurface() const
{
	return d_surface;
}

bool Window::trySaveRenderedImageToFile(const String& filename)
{
	if (d_surface && d_surface->isRenderingWindow())
	{
		RenderingWindow* pRenderingWnd = static_cast<RenderingWindow*>(d_surface);
		TextureTarget& texTarget = pRenderingWnd->getTextureTarget();
		return texTarget.saveToFile(filename);
	}
	return false;
}

//----------------------------------------------------------------------------//
RenderingSurface& Window::getTargetRenderingSurface() const
{
	//if (d_surface)
	//	return *d_surface;
	//else if (d_parent)
	//	return d_parent->getTargetRenderingSurface();
	//else
	//	return System::getSingleton().getRenderer()->getDefaultRenderingRoot();

	// ycl �򸸴��ڵݹ���ø�Ϊѭ��ʵ��?
	const Window* pWnd = this;
	while (pWnd)
	{
		if (pWnd->d_surface)
		{
			return *pWnd->d_surface;
		}
		pWnd = pWnd->d_parent;
	}
	return System::getSingleton().getRenderer()->getDefaultRenderingRoot();
}

//----------------------------------------------------------------------------//
void Window::setRenderingSurface(RenderingSurface* surface)
{
	if (d_surface == surface)
		return;

	if (d_autoRenderingWindow)
		setUsingAutoRenderingSurface(false);

	d_surface = surface;

	// transfer child surfaces to this new surface
	if (d_surface)
	{
		transferChildSurfaces();
		notifyScreenAreaChanged();
	}
}

//----------------------------------------------------------------------------//
void Window::invalidateRenderingSurface()
{
	// invalidate our surface chain if we have one
	if (d_surface)
		d_surface->invalidate();
	// else look through the hierarchy for a surface chain to invalidate.
	else
		if (d_parent) d_parent->invalidateRenderingSurface();
}

//----------------------------------------------------------------------------//
const Window* Window::getRootWindow() const
{
	return d_parent ? d_parent->getRootWindow() : this;
}

//----------------------------------------------------------------------------//
Window* Window::getRootWindow()
{
	return const_cast<Window*>(
		static_cast<const Window*>(this)->getRootWindow());
}

//----------------------------------------------------------------------------//
bool Window::isUsingAutoRenderingSurface() const
{
	return d_autoRenderingWindow;
}

//----------------------------------------------------------------------------//
void Window::setUsingAutoRenderingSurface(bool setting)
{
	if (setting)
		allocateRenderingWindow();
	else
		releaseRenderingWindow();

	// while the actal area on screen may not have changed, the arrangement of
	// surfaces and geometry did...
	notifyScreenAreaChanged();
}

//----------------------------------------------------------------------------//
void Window::allocateRenderingWindow()
{
	if (!d_autoRenderingWindow)
	{
		d_autoRenderingWindow = true;

		TextureTarget* const t =
			System::getSingleton().getRenderer()->createTextureTarget();

		// TextureTargets may not be available, so check that first.
		if (!t)
		{
			Logger::getSingleton().logEvent("Window::allocateRenderingWindow - "
				"Failed to create a suitable TextureTarget for use by Window '"
				+ d_name + "'", Errors);

			d_surface = 0;
			return;
		}

		d_surface = &getTargetRenderingSurface().createRenderingWindow(*t);
		transferChildSurfaces();

		// set size and position of RenderingWindow
		static_cast<RenderingWindow*>(d_surface)->setSize(getPixelSize());
		static_cast<RenderingWindow*>(d_surface)->
			setPosition(getUnclippedOuterRect().getPosition());

		System::getSingleton().signalRedraw();
	}
}

//----------------------------------------------------------------------------//
void Window::releaseRenderingWindow()
{
	if (d_autoRenderingWindow && d_surface)
	{
		RenderingWindow* const old_surface =
			static_cast<RenderingWindow*>(d_surface);
		d_autoRenderingWindow = false;
		d_surface = 0;
		// detach child surfaces prior to destroying the owning surface
		transferChildSurfaces();
		// destroy surface and texture target it used
		TextureTarget* tt = &old_surface->getTextureTarget();
		old_surface->getOwner().destroyRenderingWindow(*old_surface);
		System::getSingleton().getRenderer()->destroyTextureTarget(tt);

		System::getSingleton().signalRedraw();
	}
}

//----------------------------------------------------------------------------//
void Window::transferChildSurfaces()
{
	RenderingSurface& s = getTargetRenderingSurface();

	const size_t child_count = getChildCount();
	for (size_t i = 0; i < child_count; ++i)
	{
		Window* const c = d_children[i];

		if (c->d_surface && c->d_surface->isRenderingWindow())
			s.transferRenderingWindow(
			*static_cast<RenderingWindow*>(c->d_surface));
		else
			c->transferChildSurfaces();
	}
}

//----------------------------------------------------------------------------//
const Vector3& Window::getRotation() const
{
	return d_rotation;
}


//----------------------------------------------------------------------------//
const Vector3& Window::getScale() const
{
	return d_scale;
}

void Window::setGeomRotation(const Vector3& rotation)
{
	if (d_geometry)
	{
		d_geometry->setRotation(rotation);
		invalidate(true);
	}
}

//----------------------------------------------------------------------------//
void Window::setRotation(const Vector3& rotation)
{
	if (rotation == d_rotation)
		return;

	d_rotation = rotation;

	WindowEventArgs args(this);
	onRotated(args);
}

void Window::setGeomScale(const Vector3& scale)
{
	if (d_geometry)
		d_geometry->setScale(scale);
}

void Window::setGeomPivot(const Vector3& p)
{
	if (d_geometry)
		d_geometry->setPivot(p);
}

//----------------------------------------------------------------------------//
void Window::setScale(const Vector3& scale)
{
	if (d_scale == scale)
		return;
	d_scale = scale;
	// if we have no surface set, enable the auto surface
	if (!d_surface)
	{
		Logger::getSingleton().logEvent("Window::setRotation - "
			"Activating AutoRenderingSurface on Window '" + d_name +
			"' to enable rotation support.");

		setUsingAutoRenderingSurface(true);

		// still no surface?  Renderer or HW must not support what we need :(
		if (!d_surface)
		{
			Logger::getSingleton().logEvent("Window::setRotation - "
				"Failed to obtain a suitable ReneringWindow surface for "
				"Window '" + d_name + "'.  Rotation will not be available.",
				Errors);

			return;
		}
	}

	// ensure surface we have is the right type
	if (!d_surface->isRenderingWindow())
	{
		Logger::getSingleton().logEvent("Window::setRotation - "
			"Window '" + d_name + "' has a manual RenderingSurface that is not "
			"a RenderingWindow.  Rotation will not be available.", Errors);

		return;
	}

	// Checks / setup complete!  Now we can finally set the rotation.
	static_cast<RenderingWindow*>(d_surface)->setScale(d_scale);

}
//----------------------------------------------------------------------------//
void Window::initialiseClippers(const RenderingContext& ctx)
{
	if (ctx.surface->isRenderingWindow())
	{
		Rect geo_clip(Vector2(0, 0),
			static_cast<RenderingWindow*>(ctx.surface)->getSize());

		if (ctx.owner == this)
		{
			RenderingSurface& owner =
				static_cast<RenderingWindow*>(d_surface)->getOwner();

			Rect surface_clip(
				d_parent && d_clippedByParent ?
				owner.isRenderingWindow() ?
				d_nonClientContent ?
				d_parent->getUnclippedOuterRect() :
				d_parent->getUnclippedInnerRect() :
				d_nonClientContent ?
				d_parent->getOuterRectClipper() :
				d_parent->getInnerRectClipper() :
				Rect(Vector2(0, 0),
				System::getSingleton().getRenderer()->getDisplaySize())
				);

			static_cast<RenderingWindow*>(d_surface)->
				setClippingRegion(surface_clip);
		}
		else if (d_parent && d_clippedByParent)
		{
			Rect parent_area(d_nonClientContent ?
				d_parent->getOuterRectClipper() :
				d_parent->getInnerRectClipper()
				);

			parent_area.offset(Vector2(-ctx.offset.d_x, -ctx.offset.d_y));
			geo_clip = parent_area.getIntersection(geo_clip);
		}

		d_geometry->setClippingRegion(geo_clip);
	}
	else
	{
		Rect geo_clip(
			d_clippedByParent && d_parent ?
			d_nonClientContent ?
			d_parent->getOuterRectClipper() :
			d_parent->getInnerRectClipper() :
			Rect(Vector2(0, 0),
			System::getSingleton().getRenderer()->getDisplaySize())
			);

		if (isInChatOutWnd())
		{
			Rect ChatOutWnd_clip(Vector2(0, 0), System::getSingleton().GetChatOutRootWndSize());
			geo_clip = ChatOutWnd_clip;
		}

		geo_clip.offset(Vector2(-ctx.offset.d_x, -ctx.offset.d_y));
		d_geometry->setClippingRegion(geo_clip);
	}
}

//----------------------------------------------------------------------------//
bool Window::isNonClientWindow() const
{
	return d_nonClientContent;
}

//----------------------------------------------------------------------------//
void Window::setNonClientWindow(const bool setting)
{
	if (setting == d_nonClientContent)
		return;

	d_nonClientContent = setting;

	WindowEventArgs args(this);
	onNonClientChanged(args);
}

//----------------------------------------------------------------------------//
void Window::onRotated(WindowEventArgs& e)
{
	// if we have no surface set, enable the auto surface
	if (!d_surface)
	{
		Logger::getSingleton().logEvent("Window::setRotation - "
			"Activating AutoRenderingSurface on Window '" + d_name +
			"' to enable rotation support.");

		setUsingAutoRenderingSurface(true);

		// still no surface?  Renderer or HW must not support what we need :(
		if (!d_surface)
		{
			Logger::getSingleton().logEvent("Window::setRotation - "
				"Failed to obtain a suitable ReneringWindow surface for "
				"Window '" + d_name + "'.  Rotation will not be available.",
				Errors);

			return;
		}
	}

	// ensure surface we have is the right type
	if (!d_surface->isRenderingWindow())
	{
		Logger::getSingleton().logEvent("Window::setRotation - "
			"Window '" + d_name + "' has a manual RenderingSurface that is not "
			"a RenderingWindow.  Rotation will not be available.", Errors);

		return;
	}

	// Checks / setup complete!  Now we can finally set the rotation.
	static_cast<RenderingWindow*>(d_surface)->setRotation(d_rotation);
	static_cast<RenderingWindow*>(d_surface)->setPivot(
		Vector3(d_pixelSize.d_width / 2.0f, d_pixelSize.d_height / 2.0f, 0.0f));

	fireEvent(EventRotated, e, EventNamespace);
}

//----------------------------------------------------------------------------//
void Window::onNonClientChanged(WindowEventArgs& e)
{
	// TODO: Trigger update of size and position information if needed

	fireEvent(EventNonClientChanged, e, EventNamespace);
}

//----------------------------------------------------------------------------//
const RenderedString& Window::getRenderedString() const
{
	if (!d_renderedStringValid)
	{
		d_renderedString = getRenderedStringParser().parse(
			getTextVisual(), getFont(), 0);
		d_renderedStringValid = true;
	}

	return d_renderedString;
}

//----------------------------------------------------------------------------//
RenderedStringParser* Window::getCustomRenderedStringParser() const
{
	return d_customStringParser;
}

//----------------------------------------------------------------------------//
void Window::setCustomRenderedStringParser(RenderedStringParser* parser)
{
	d_customStringParser = parser;
	d_renderedStringValid = false;
}

//----------------------------------------------------------------------------//
RenderedStringParser& Window::getRenderedStringParser() const
{
	// if parsing is disabled, we use a DefaultRenderedStringParser that creates
	// a RenderedString to renderi the input text verbatim (i.e. no parsing).
	if (!d_textParsingEnabled)
		return d_defaultStringParser;

	// Next prefer a custom RenderedStringParser assigned to this Window.
	if (d_customStringParser)
		return *d_customStringParser;

	// Next prefer any globally set RenderedStringParser.
	RenderedStringParser* const global_parser =
		CEGUI::System::getSingleton().getDefaultCustomRenderedStringParser();
	if (global_parser)
		return *global_parser;

	// if parsing is enabled and no custom RenderedStringParser is set anywhere,
	// use the system's BasicRenderedStringParser to do the parsing.
	return d_basicStringParser;
}

//----------------------------------------------------------------------------//
Vector2 Window::getUnprojectedPosition(const Vector2& pos) const
{
	RenderingSurface* rs = &getTargetRenderingSurface();

	// if window is not backed by RenderingWindow, return same pos.
	if (!rs->isRenderingWindow())
		return pos;

	// get first target RenderingWindow
	RenderingWindow* rw = static_cast<RenderingWindow*>(rs);

	// setup for loop
	Vector2 out_pos(pos);

	// while there are rendering windows
	while (rw)
	{
		// unproject the point for the current rw
		const Vector2 in_pos(out_pos);
		rw->unprojectPoint(in_pos, out_pos);

		// get next rendering window, if any
		rw = (rs = &rw->getOwner())->isRenderingWindow() ?
			static_cast<RenderingWindow*>(rs) : 0;
	}

	return out_pos;
}

//----------------------------------------------------------------------------//
const String& Window::getTextVisual() const
{
	// no bidi support
	if (!d_bidiVisualMapping)
		return d_textLogical;

	if (!d_bidiDataValid)
	{
		d_bidiVisualMapping->updateVisual(d_textLogical);
		d_bidiDataValid = true;
	}

	return d_bidiVisualMapping->getTextVisual();
}

//----------------------------------------------------------------------------//
bool Window::isTextParsingEnabled() const
{
	return d_textParsingEnabled;
}

//----------------------------------------------------------------------------//
void Window::setTextParsingEnabled(const bool setting)
{
	d_textParsingEnabled = setting;
	d_renderedStringValid = false;

	WindowEventArgs args(this);
	onTextParsingChanged(args);
}

//----------------------------------------------------------------------------//
void Window::onTextParsingChanged(WindowEventArgs& e)
{
	fireEvent(EventTextParsingChanged, e, EventNamespace);
}

//----------------------------------------------------------------------------//
bool Window::isInnerRectSizeChanged() const
{
	const Size old_sz(d_innerUnclippedRect.getSize());
	d_innerUnclippedRectValid = false;
	return old_sz != getUnclippedInnerRect().getSize();
}

void Window::SetTextBoder(bool bBorder)
{
	if (bBorder != d_TextBorder)
	{
		d_TextBorder = bBorder;
		invalidate();
	}

}

void Window::SetTextBorderColour(const ColourRect& color)
{
	/*if (color!=d_TextBorderColour)
	{
	d_TextBorderColour=color;
	invalidate();
	}*/

}

void Window::onDisplaySizeChangeImpl()  //∑÷±Ê¬ ∏ƒ±�?
{
	if (!d_DisplaySizeChangePosEnable)
	{
		CheckWndPos();
		return;
	}
	const Size& fact = System::getSingleton().GetDisplaySizeFact();
	FixWndSize(fact.d_width, fact.d_height);
}


void Window::onDisplaySizeChange()  //∑÷±Ê¬ ∏ƒ±�?
{

	const size_t child_count = getChildCount();
	for (size_t i = 0; i < child_count; ++i)
	{
		/*WindowEventArgs args(this);
		d_children[i]->fireEvent(EventDisplaySizeChange,args,EventNamespace);*/
		if (d_children[i]->isDisplaySizeEnable())
		{
			d_children[i]->onDisplaySizeChangeImpl();
		}
	}
}

void Window::FixWndSize(float xScale, float yScale)  //∏˘æ›∑÷±Ê¬ –ﬁ’˝¥∞ø⁄µƒ¥Û–�?
{

	const UVector2& oldPos = getPosition();
	const UVector2& oldSize = getSize();

	float curWidth = System::getSingleton().getRenderer()->getDisplaySize().d_width;
	float curHeight = System::getSingleton().getRenderer()->getDisplaySize().d_height;

	UVector2 newPos;
	float x_offset = oldPos.d_x.d_scale*(curWidth / xScale) + oldPos.d_x.d_offset - curWidth / xScale / 2.0f;
	float y_offset = oldPos.d_y.d_scale*(curHeight / yScale) + oldPos.d_y.d_offset - curHeight / yScale / 2.0f;

#ifdef FORCEGUIEDITOR
	newPos.d_x.d_scale = 0.0f;
	newPos.d_x.d_offset = (x_offset + curWidth / 2.0f) / curWidth;

	newPos.d_y.d_scale = 0.0f;
	newPos.d_y.d_offset = (y_offset + curHeight / 2.0f) / curHeight;
#else
	newPos.d_x.d_scale = (x_offset + curWidth / 2.0f) / curWidth;
	newPos.d_x.d_offset = 0.0f;

	newPos.d_y.d_scale = (y_offset + curHeight / 2.0f) / curHeight;
	newPos.d_y.d_offset = 0.0f;
#endif
	setPosition(newPos);

	UVector2 newSize;
	newSize.d_x.d_scale = oldSize.d_x.d_scale / xScale;
	newSize.d_x.d_offset = oldSize.d_x.d_offset;
	newSize.d_y.d_scale = oldSize.d_y.d_scale / yScale;
	newSize.d_y.d_offset = oldSize.d_y.d_offset;

	setSize(newSize);

	//»Áπ˚ƒ¨»œŒª÷√ «‘⁄∏∏¥∞ø⁄µƒ’˝÷–—�?
	if (d_InParentWndCenter&&d_parent != NULL)
	{
		Size parentSize = getParentPixelSize();
		UVector2 newPos;
		newPos.d_x.d_scale = (parentSize.d_width - getPixelSize().d_width) / 2.0f / parentSize.d_width;
		newPos.d_x.d_offset = 0.0f;
		newPos.d_y.d_scale = (parentSize.d_height - getPixelSize().d_height) / 2.0f / parentSize.d_height;
		newPos.d_y.d_offset = 0.0f;
		setPosition(newPos);
	}

	CheckWndPos();
}

void Window::SetInParentCenter(bool b)
{
	if (b != d_InParentWndCenter)
	{
		d_InParentWndCenter = b;
		FixWndSize(1.0f, 1.0f);
	}
}

void Window::CheckWndPos()
{
	const UVector2& oldPos = getPosition();
	Size curSize = getPixelSize();
	float curClientWidth = System::getSingleton().getRenderer()->getDisplaySize().d_width;
	float curClientHeight = System::getSingleton().getRenderer()->getDisplaySize().d_height;
	float curoffset_X = oldPos.d_x.d_scale*curClientWidth + oldPos.d_x.d_offset;
	float curoffset_Y = oldPos.d_y.d_scale*curClientHeight + oldPos.d_y.d_offset;

	bool needResetPos = false; // «∑Ò“™÷ÿ–¬…Ë÷√Œª÷�?
	UVector2 newPos(oldPos);
	if ((curoffset_X + curSize.d_width)>curClientWidth)
	{
		newPos.d_x.d_scale = (curClientWidth - curSize.d_width) / curClientWidth;
		newPos.d_x.d_offset = 0.0f;
		needResetPos = true;
	}
	else if (curoffset_X<0.0f)
	{
		newPos.d_x.d_scale = 0.0f;
		newPos.d_x.d_offset = 0.0f;
		needResetPos = true;
	}

	if ((curoffset_Y + curSize.d_height)>curClientHeight)
	{
		newPos.d_y.d_scale = (curClientHeight - curSize.d_height) / curClientHeight;
		newPos.d_y.d_offset = 0.0f;
		needResetPos = true;
	}
	else if (curoffset_Y<0.0f)
	{
		newPos.d_y.d_scale = 0.0f;
		newPos.d_y.d_offset = 0.0f;
		needResetPos = true;
	}
	if (needResetPos)
	{
		setPosition(newPos);
	}

}

void Window::StartFlash(float frequence)
{
	d_FlashFrequence = frequence;
	d_Flash = true;
	d_FalshElapseTime = 0.0f;
	invalidate();
}
void Window::StopFlash()
{
	d_Flash = false;
	d_FalshElapseTime = 0.0f;
	if (d_EnableFlash)
	{
		setAlpha(1.0f);
	}
	invalidate();

}

void Window::SetTextColor(argb_t color, const String& text)
{
	String newText("[colour=\'");
	newText += PropertyHelper::colourToString(colour(color));
	newText += "\']";
	if (text.empty())
	{
		newText += d_textLogical;
	}
	else
	{
		newText += text;
	}
	setText(newText);
}

void Window::SetAlignWindow(Window* pAlignWnd, WindowAlignType type)
{
	//if (pAlignWnd!=NULL&&type!=WA_None)
	{
		if (d_AlignWindow != pAlignWnd)
		{
			d_AlignWindow = pAlignWnd;
			d_WndAlignType = type;

			RefreshAlignWindowPos();
		}
	}

}

void Window::RefreshAlignWindowPos() //µ±Õ£øø¥∞ø⁄µƒŒª÷√∑¢…˙∏ƒ±‰ �?
{
	if (d_AlignWindow&&d_WndAlignType != WA_None)
	{
		float xOffset = 0.0f;
		float yOffset = 0.0f;
		float curWndWidth = getPixelSize().d_width;
		float curWndHeight = getPixelSize().d_height;

		switch (d_WndAlignType)
		{
		case WA_Right:
		{
			xOffset = curWndWidth;
		}
		break;
		case WA_Bottom:
		{
			yOffset = curWndHeight;
		}
		break;
		case WA_Left:
		{
			xOffset = -curWndWidth;
		}
		break;
		case WA_Top:
		{
			yOffset = -curWndHeight;
		}
		break;
		}

		const UDim& XPos = getXPosition();
		float xScreenOffset = CoordConverter::windowToScreenX(*getParent(), XPos);

		const UDim& YPos = getYPosition();
		float yScreenOffset = CoordConverter::windowToScreenY(*getParent(), YPos);

		UVector2 newPos(cegui_absdim(xScreenOffset + xOffset), cegui_absdim(yScreenOffset + yOffset));
		d_AlignWindow->setPosition(newPos);

	}

}

Point Window::GetScreenPos() const//ªÒµ√¥∞ø⁄µƒ∆¡ƒª◊¯±�?
{
	Point pt;
	Window* pBaseWnd = NULL;
	if (d_parent)
	{
		pBaseWnd = d_parent;
		pt.d_x = CoordConverter::windowToScreenX(*pBaseWnd, getPosition().d_x);
		pt.d_y = CoordConverter::windowToScreenY(*pBaseWnd, getPosition().d_y); //¥∞ø⁄∆¡ƒª◊¯±ÍYpBaseWnd=d_parent;
	}
	else
	{
		Size screenSize = System::getSingleton().getRenderer()->getDisplaySize();
		pt.d_x = screenSize.d_width*getXPosition().d_scale + getXPosition().d_offset;
		pt.d_y = screenSize.d_height*getYPosition().d_scale + getYPosition().d_offset;
	}

	return pt;
}

Point Window::GetScreenPosOfCenter() //ªÒµ√¥∞ø⁄÷––ƒŒª÷√µƒ∆¡ƒª◊¯±Í
{
	Point pt = GetScreenPos();
	pt.d_x = pt.d_x + getPixelSize().d_width / 2.0f;
	pt.d_y = pt.d_y + getPixelSize().d_height / 2.0f;
	return pt;

}

bool Window::isMouseOnThisWnd()
{
	Point curMousePt = MouseCursor::getSingleton().getPosition();

	Window* pParent = d_parent;
	if (pParent == NULL)
	{
		pParent = System::getSingleton().getGUISheet();
	}

	if (pParent)
	{
		Rect curRect = CoordConverter::windowToScreen(*pParent, d_area);
		if (curRect.isPointInRect(curMousePt))
		{
			return true;
		}
		return false;
	}
	return true;
}

bool Window::isInChatOutWnd() const// «∑Ò «‘⁄Õ‚÷√¡ƒÃÏ¥∞÷�?
{
	if (d_InChatOutWnd)
	{
		return true;
	}

	if (d_parent)
	{
		return d_parent->isInChatOutWnd();
	}

	return false;

}

Window* Window::GetKeyBoardTargetWnd() //ªÒµ√º¸≈Ã ‰»Î≤∂ªÒ¥∞ø�?
{


	const ChildList::const_reverse_iterator end = d_drawList.rend();
	ChildList::const_reverse_iterator child;
	for (child = d_drawList.rbegin(); child != end; ++child)
	{
		if ((*child)->isVisible() && !(*child)->isDisabled(false) && !(*child)->isDestroyStarted() && (*child)->getAlpha() > 0.0f)
		{
			// recursively scan children of this child windows...
			Window* wnd =
				(*child)->GetKeyBoardTargetWnd();

			// return window pointer if we found a 'hit' down the chain somewhere
			if (wnd)
				return wnd;
			else if ((*child)->getType() == "TaharezLook/ComboDropList")
				return(*child);
			// see if this child is hit and return it's pointer if it is
			else if (!(*child)->isMousePassThroughEnabled() &&
				(*child)->IsCanEdit())
				return (*child);
		}
	}

	// nothing hit
	return 0;

}

bool Window::isInputFocus()
{
	if (IsCanEdit())
	{
		if (System::getSingleton().getKeyboardTargetWindow() == this)
		{
			return true;
		}
	}

	return false;
}

void Window::OnShiedUI()  //∆¡µÙUI ±µƒ¥¶¿Ì
{
	d_OldVisable = d_visible;
	if (d_OnShiedRootWnd)
	{
		d_visible = false;
		/*WindowEventArgs e(this);
		onHidden(e);*/
		invalidate(true);
	}
}

void Window::OnShowUI()
{
	if (d_OnShiedRootWnd)
	{
		/*WindowEventArgs e(this);
		onHidden(e);*/
		if (d_OldVisable)
		{
			d_visible = true;
			//onShown(e);
		}
		else
		{
			d_visible = false;
			//onHidden(e);
		}
		invalidate(true);
	}

}

void Window::ShieAll() //“˛≤ÿÀ˘”–◊”¥∞ø⁄µ´≤ª∞¸¿®–©∆¡µÙUI ±≤ª◊˜¥¶¿Ìµƒ¥∞ø⁄
{
	const size_t child_count = getChildCount();
	for (size_t i = 0; i < child_count; ++i)
	{
		WindowEventArgs args(this);
		d_children[i]->OnShiedUI();
	}

}
void Window::ShowAll() //œ‘ æÀ˘”–◊”¥∞ø⁄µ´≤ª∞¸¿®–©∆¡µÙUI ±≤ª◊˜¥¶¿Ìµƒ¥∞ø⁄
{
	const size_t child_count = getChildCount();
	for (size_t i = 0; i < child_count; ++i)
	{
		WindowEventArgs args(this);
		d_children[i]->OnShowUI();
	}

}



void Window::offsetPixelPosition(const Vector2& offset)
{
	UVector2 uOffset(cegui_absdim(PixelAligned(offset.d_x)),
		cegui_absdim(PixelAligned(offset.d_y)));

	setPosition(d_area.getPosition() + uOffset);
	CheckLeftTopPos();
}

void Window::CheckLeftTopPos()  //ºÏ≤‚◊Û…œΩ«¿Î∆¡ƒª±ﬂ‘µµƒæ‡¿�?
{
	if (getParent() && getParent() == System::getSingleton().getGUISheet())
	{
		Point ptTopLeft = GetScreenPos();
		Point ptRightBottm;
		ptRightBottm.d_x = ptTopLeft.d_x + getPixelSize().d_width;
		ptRightBottm.d_y = ptTopLeft.d_y + getPixelSize().d_height;
		Size ScreenSize = System::getSingleton().getGUISheet()->getPixelSize();

		bool bChange = false;
		if (ptTopLeft.d_x>(ScreenSize.d_width - s_fMinOffset))
		{
			ptTopLeft.d_x = ScreenSize.d_width - s_fMinOffset;
			bChange = true;
		}
		if (ptTopLeft.d_y>(ScreenSize.d_height - s_fMinOffset))
		{
			ptTopLeft.d_y = ScreenSize.d_height - s_fMinOffset;
			bChange = true;
		}

		if (ptRightBottm.d_x<s_fMinOffset)
		{
			ptTopLeft.d_x = ptTopLeft.d_x + (s_fMinOffset - ptRightBottm.d_x);
			bChange = true;
		}

		if (ptRightBottm.d_y<s_fMinOffset)
		{
			ptTopLeft.d_y = ptTopLeft.d_y + (s_fMinOffset - ptRightBottm.d_y);
			bChange = true;
		}

		if (bChange)
		{
			UVector2 newLeftTop(cegui_absdim(ptTopLeft.d_x), cegui_absdim(ptTopLeft.d_y));
			setPosition(newLeftTop);
		}
	}


}

float Window::GetXOffset() const
{
	Point pt = GetScreenPos();
	Point ptParent(0.0f, 0.0f);
	if (d_parent)
	{
		ptParent = d_parent->GetScreenPos();
	}
	return pt.d_x - ptParent.d_x;

}
float Window::GetYOffset()const
{
	Point pt = GetScreenPos();
	Point ptParent(0.0f, 0.0f);
	if (d_parent)
	{
		ptParent = d_parent->GetScreenPos();
	}
	return pt.d_y - ptParent.d_y;

}

void Window::MoveToHorzCenter()  //∫·œÚæ”÷�?
{
	float width = getPixelSize().d_width;
	float parentWidth = 0.0f;
	if (d_parent)
	{
		parentWidth = d_parent->getPixelSize().d_width;
	}
	else
	{
		parentWidth = System::getSingleton().getRenderer()->getDisplaySize().d_width;
	}

	UDim xPos(cegui_reldim((parentWidth - width) / 2.0f / parentWidth));
	setXPosition(xPos);
}
void Window::MoveToVertCenter()  //◊›œÚæ”÷�?
{
	float height = getPixelSize().d_height;
	float parentHeight = 0.0f;
	if (d_parent)
	{
		parentHeight = d_parent->getPixelSize().d_height;

	}
	else
	{
		parentHeight = System::getSingleton().getRenderer()->getDisplaySize().d_height;
	}
	UDim yPos(cegui_reldim((parentHeight - height) / 2.0f / parentHeight));
	setYPosition(yPos);

}

void Window::SetWidthClip(const float& clip)
{
	d_clipWidth = clip;
	//d_geometry->SetWidthClip(d_clipWidth);
	invalidate();
}

float Window::GetWidthClip()
{
	return d_clipWidth;
}

Editbox* Window::GetNextEditBox(int tabOrderID)
{
	const size_t child_count = getChildCount();

	Editbox* pMinTabOrderBox = NULL;

	for (size_t i = 0; i < child_count; ++i)
	{
		if (d_children[i]->GetWindowType() == Editbox::WidgetTypeName)
		{
			Editbox* pEditbox = (Editbox*)d_children[i];
			if (!pEditbox->isReadOnly() && !pEditbox->isDisabled() && pEditbox->isVisible())
			{
				int wndTabOrderID = pEditbox->GetTabOrderID();
				if (pMinTabOrderBox == NULL)
				{
					pMinTabOrderBox = pEditbox;
				}
				else
				{
					if (wndTabOrderID<pMinTabOrderBox->GetTabOrderID())
					{
						pMinTabOrderBox = pEditbox;
					}
				}
				if (wndTabOrderID>tabOrderID)
				{
					return pEditbox;
				}
			}

		}
	}
	return pMinTabOrderBox;
}

bool Window::isParentMousePassThroughEnabled()
{
	if (d_mousePassThroughEnabled)
	{
		return true;
	}
	if (d_parent)
	{
		Window* pParent = d_parent;
		bool bParentPass = false;
		while (pParent != NULL&&pParent != System::getSingleton().getGUISheet())
		{
			bParentPass = pParent->isMousePassThroughEnabled();
			if (bParentPass)
			{
				return true;
			}
			pParent = pParent->getParent();
		}

		return bParentPass;
	}
	return false;
}

void Window::SetWndCenterInParentXPos(float xPos)
{
	Size pixSize = getPixelSize();

	setXPosition(cegui_absdim(xPos - pixSize.d_width / 2.0f));


}
void Window::SetWndCenterInParentYPos(float yPos)
{
	Size pixSize = getPixelSize();

	setYPosition(cegui_absdim(yPos - pixSize.d_height / 2.0f));
	printf("ypos:%d \n", (int)yPos);


}

void Window::BeginCreateEffect()
{
	printf("***%s******Begin create********** \n", d_name.c_str());
	d_EffectState = WndEffectState_Ready;
	setAlpha(0.0f);
	d_WndEffectElaseTime = 0.0f;
}
void Window::EndCreateEffect()
{

	WindowEventArgs e(this);
	fireEvent(EventCreateWndEffectEnd, e, EventNamespace);
	d_EffectState = WndEffectState_None;
	d_WndEffectElaseTime = 0.0f;
	if (d_CreateWndType == CreateWndEffect_ZoomOut)
	{
		Vector3 vScale(1.0f, 1.0f, 1.0f);
		setScale(vScale);
		setUsingAutoRenderingSurface(false);
	}
	printf("****%s**** *End create********** \n", d_name.c_str());
	CenterInParent();

}
void Window::BeginCloseEffect()
{
	d_EffectState = WndEffectState_Close;
	d_WndEffectElaseTime = 0.0f;

}
void Window::EndCloseEffect()
{
	WindowEventArgs e(this);
	fireEvent(EventCloseWndEffectEnd, e, EventNamespace);
	d_EffectState = WndEffectState_None;
	d_WndEffectElaseTime = 0.0f;
	if (d_CreateWndType == CloseWndEffect_ZoomIn)
	{
		Vector3 vScale(1.0f, 1.0f, 1.0f);
		setScale(vScale);
		setUsingAutoRenderingSurface(false);
	}


}

void Window::UpdateWndEffect(float elapse)
{
	switch (d_EffectState) {
	case WndEffectState_Create:
		UpdateCreateEffect(elapse);
		break;
	case WndEffectState_Close:
		UpdateCloseEffect(elapse);
		break;
	case WndEffectState_Ready:
		d_EffectState = WndEffectState_Go;
		break;
	case WndEffectState_Go:
		d_EffectState = WndEffectState_Create;
		break;

	default:
		break;
	}
}
void Window::UpdateCreateEffect(float elapse)
{
	if (d_alpha<0.99f)
	{
		setAlpha(1.0f);
		//if (isAlwaysOnTop())
		//	moveToFront();
	}
	float totalTime = 0.6f;
	if (d_CreateWndType == CreateWndEffect_ZoomOut)
	{

		totalTime = 0.4f;
	}

	d_WndEffectElaseTime += elapse;
	if (d_WndEffectElaseTime>totalTime) {
		EndCreateEffect();
		return;
	}

	if (d_CreateWndType == CreateWndEffect_ZoomOut)
	{


		float angle = (d_WndEffectElaseTime / totalTime)*0.5f*3.1415926f;
		float time1 = totalTime*0.5f;

		float maxScale = 1.2f;
		float minScale = 0.1f;
		float scale = minScale + (maxScale - minScale)*(sinf(angle));
		if (d_WndEffectElaseTime>time1)
		{
			angle = 0.5f*3.1415626f*(d_WndEffectElaseTime - time1) / (totalTime - time1);
			scale = 1.0f + (maxScale - 1.0f)*(1.0f - sinf(angle));

		}
		Vector3 vScale(scale, scale, 1.0f);
		printf("scale %3.2f \n", scale);

		float width = getPixelSize().d_width*scale;
		float height = getPixelSize().d_height*scale;

		float parentWidth = getParentPixelWidth();
		float parentHeight = getParentPixelHeight();

		float xPos = (parentWidth - width) / 2.0f;
		float yPos = (parentHeight - height) / 2.0f;
		setXPosition(cegui_absdim(xPos));
		setYPosition(cegui_absdim(yPos));

		setScale(vScale);


		return;
	}

	float v0 = 100.0f;
	float time1 = totalTime*0.5f;
	float height = getPixelSize().d_height;
	float parentHeight = getParentPixelHeight();
	if (d_CreateWndType != CreateWndEffect_Drop) {
		height = getPixelSize().d_width;
		parentHeight = getParentPixelWidth();
	}

	float g = ((height + parentHeight) - 2.0f*v0*time1) / (time1*time1);


	if (d_WndEffectElaseTime<time1)
	{

		float yOffset1 = v0*d_WndEffectElaseTime + 0.5f*g*d_WndEffectElaseTime*d_WndEffectElaseTime;
		float newPos = parentHeight + height / 2.0f - yOffset1;
		if (d_CreateWndType == CreateWndEffect_Drop || d_CreateWndType == CreateWndEffect_FlyFromLeft)
		{
			newPos = yOffset1 - height / 2.0f;

		}
		if (d_CreateWndType == CreateWndEffect_Drop) {
			SetWndCenterInParentYPos(newPos);
		}
		else
		{
			SetWndCenterInParentXPos(newPos);

		}

		return;
	}

	float time = d_WndEffectElaseTime - time1;
	float angle = (time / (totalTime - time1))*3.1415926f;
	float yOffset = parentHeight / 2.0f - sinf(angle)*50.0f;
	printf("time %d create sinf %d \n", (int)(time*1000.0f), (int)(sinf(angle)*50.0f));
	if (d_CreateWndType == CreateWndEffect_Drop || d_CreateWndType == CreateWndEffect_FlyFromLeft)
	{

		yOffset = parentHeight / 2.0f + sinf(angle)*50.0f;
	}

	if (d_CreateWndType == CreateWndEffect_Drop)
	{

		SetWndCenterInParentYPos(yOffset);
	}
	else
	{
		SetWndCenterInParentXPos(yOffset);
	}

}
void Window::UpdateCloseEffect(float elapse)
{
	float totalTime = 0.5f*0.6f;
	d_WndEffectElaseTime += elapse;
	if (d_WndEffectElaseTime>totalTime) {
		EndCloseEffect();
		return;
	}

	if (d_CloseWndType == CloseWndEffect_ZoomIn)
	{

		float angle = (d_WndEffectElaseTime / totalTime)*0.5f*3.1415926f;


		float maxScale = 1.0f;
		float minScale = 0.1f;
		float scale = minScale + (maxScale - minScale)*(1.0f - sinf(angle));
		//        if (d_WndEffectElaseTime>time1)
		//        {
		//            angle=0.5f*3.1415626f*(d_WndEffectElaseTime-time1)/(totalTime-time1);
		//            scale=1.0f+(maxScale-1.0f)*(1.0f-sinf(angle));
		//        }
		Vector3 vScale(scale, scale, 1.0f);
		float width = getPixelSize().d_width*scale;
		float height = getPixelSize().d_height*scale;

		float parentWidth = getParentPixelWidth();
		float parentHeight = getParentPixelHeight();

		float xPos = (parentWidth - width) / 2.0f;
		float yPos = (parentHeight - height) / 2.0f;
		setXPosition(cegui_absdim(xPos));
		setYPosition(cegui_absdim(yPos));
		setScale(vScale);


		return;
	}

	float time1 = 0.0f;
	float height = getPixelSize().d_height;
	float parentHeight = getParentPixelHeight();
	if (d_CloseWndType != CloseWndEffect_FlyUp&&d_CloseWndType != CloseWndEffect_FlyDown) {
		height = getPixelSize().d_width;
		parentHeight = getParentPixelWidth();
	}

	//    if (d_WndEffectElaseTime<time1) {
	//        
	//        
	//        float angle=(d_WndEffectElaseTime/time1)*0.5f*3.1415926f;
	//        float yOffset=parentHeight/2.0f+sinf(angle)*50.0f;
	//        
	//        if (d_CloseWndType==CloseWndEffect_FlyRight||d_CloseWndType==CloseWndEffect_FlyDown) {
	//            yOffset=parentHeight/2.0f-sinf(angle)*50.0f;
	//        }
	//        if (d_CloseWndType==CloseWndEffect_FlyUp||d_CloseWndType==CloseWndEffect_FlyDown) {
	//            SetWndCenterInParentYPos(yOffset);
	//        }else
	//        {
	//            SetWndCenterInParentXPos(yOffset);
	//            
	//        }
	//        return;
	//    }


	float time = d_WndEffectElaseTime - time1;
	float time2 = totalTime - time1;

	float v0 = 100.0f;
	float g = ((height + parentHeight) - 2.0f*v0*time2) / (time2*time2);
	float newPos = parentHeight / 2.0f - 0.5f*g*time*time;
	if (d_CloseWndType == CloseWndEffect_FlyRight || d_CloseWndType == CloseWndEffect_FlyDown) {
		newPos = parentHeight / 2.0f + 0.5f*g*time*time;
	}
	if (d_CloseWndType == CloseWndEffect_FlyUp || d_CloseWndType == CloseWndEffect_FlyDown) {
		SetWndCenterInParentYPos(newPos);
	}
	else
	{
		SetWndCenterInParentXPos(newPos);

	}
}

bool Window::isAllChildrenShow()
{
	const size_t child_count = getChildCount();
	for (size_t i = 0; i < child_count; ++i)
	{

		if (!d_children[i]->isVisible(true))
		{
			return false;
		}
	}
	return  true;
}
bool Window::isAllChildrenHide()
{
	const size_t child_count = getChildCount();
	for (size_t i = 0; i < child_count; ++i)
	{

		if (d_children[i]->isVisible(true))
		{
			return false;
		}
	}
	return true;
}

Point Window::GetTopLeftPosOnParent()
{
	Point pt(0.0f, 0.0f);
	Size parentSize = getParentPixelSize();
	pt.d_x = getPosition().d_x.d_scale*parentSize.d_width + getPosition().d_x.d_offset;
	pt.d_y = getPosition().d_y.d_scale*parentSize.d_height + getPosition().d_y.d_offset;

	return pt;

}

void Window::SetAllChildrenVis(bool bVis)
{
	const size_t child_count = getChildCount();
	for (size_t i = 0; i < child_count; ++i)
	{
		d_children[i]->setVisible(bVis);
	}

}

void Window::DrawModalStateEffect(float ctx_x, float ctx_y)
{
	if (getModalState())
	{

		if (ImagesetManager::getSingleton().isDefined("LoginBack2"))
		{
			const Imageset& imageSet = ImagesetManager::getSingleton().get("LoginBack2");
			if (imageSet.isImageDefined("CommonBack"))
			{
				const Image* pImage = &imageSet.getImage("CommonBack");
				CEGUI::Point pt = GetScreenPos();
				pt.d_x = 0.0f - pt.d_x + ctx_x;
				pt.d_y = 0.0f - pt.d_y + ctx_y;
				//       printf("ctx: %3.2f %3.2f \n",ctx_x,ctx_y);
				CEGUI::Size screenSize = System::getSingleton().getRenderer()->getDisplaySize();
				CEGUI::Rect clipRect(0.0f, 0.0f, screenSize.d_width, screenSize.d_height);
				pImage->draw(d_geometry, pt, screenSize, 0);

			}
		}
	}
}

bool Window::isAllowModalState(bool local)const
{
	if (d_AllowModalSate)
	{
		return true;
	}
	if (!d_parent)
	{
		return d_AllowModalSate;
	}

	//return d_parent->isVisible();
	Window* pParent = d_parent;
	bool bParentAllow = false;
	while (pParent != NULL)
	{
		bParentAllow = pParent->isAllowModalState(true);
		if (bParentAllow)
		{
			return true;
		}
		pParent = pParent->getParent();
	}
	return bParentAllow;

}

void Window::SetAllChildAlphaButModal(float alpha) //设置所有的子窗口的alpha值除了模态对话框和topmost�?
{
	const size_t child_count = getChildCount();
	for (size_t i = 0; i < child_count; ++i)
	{
		Window *pChild = d_children[i];
		if (alpha > 0.95 || (!pChild->getModalState() && !pChild->isAllowModalState(true) && !pChild->isTopMost() && !pChild->isAllowShowWithModalState()))
		{

			pChild->setAlpha(alpha);

			//            if (pChild->GetWndEffectState()==WndEffectState_None) {
			//                pChild->setAlpha(alpha);
			//            }

		}
	}

}

void Window::CenterInParent()
{
	Size parentSize = getParentPixelSize();
	Size curSize = getPixelSize();

	float x_offset = (parentSize.d_width - curSize.d_width) / 2.0f;
	float y_offset = (parentSize.d_height - curSize.d_height) / 2.0f;

	setXPosition(cegui_absdim(x_offset));
	setYPosition(cegui_absdim(y_offset));
}

void Window::EnableLongPress(bool bEnable) {
	if (bEnable)
		subscriberEventLongPress(Event::Subscriber(&Window::HandleLongPress, this));
}

bool Window::HandleLongPress(const CEGUI::EventArgs &e) {
	const GestureEventArgs& ge = (const GestureEventArgs&)e;
	return onLongPress(ge.d_Recognizer);
}

void Window::EnableDrag(bool bEnable) {
	if (bEnable)
		subscriberEventDrag(Event::Subscriber(&Window::HandleDrag, this));
}

bool Window::HandleDrag(const CEGUI::EventArgs &e) {
	const GestureEventArgs& ge = (const GestureEventArgs&)e;
	return onMouseDrag(ge.d_Recognizer);
}

void Window::subscriberEventLongPress(Event::Subscriber callback) {
	d_recognizerManager->AddRecoginzer(*this, Gesture::LongPress, callback);
	d_LongpressEnable = true;
	//    d_longPressGestureRecognizer = new Gesture::CEGUILongPressGestureRecognizer;
	//    d_longPressGestureRecognizer->addTarget(this, 0);
}

void Window::subscriberEventDrag(Event::Subscriber callback) {
	//    printf("enable drag%s\n", getName().c_str());
	d_recognizerManager->AddRecoginzer(*this, Gesture::Pan, callback);
	d_DragEnable = true;
}

void Window::MoveToStartFlyPoint() //移动到要开始飞行的坐标
{
	if (d_parent)
	{
		Point parentCenterScreenPos = d_parent->GetScreenPosOfCenter();
		float x_offset = d_FlyStartScreenPoint.d_x - parentCenterScreenPos.d_x;
		float y_offset = d_FlyStartScreenPoint.d_y - parentCenterScreenPos.d_y;

		Size parentSize = d_parent->getPixelSize();
		SetWndCenterInParentXPos(parentSize.d_width / 2.0f + x_offset);
		SetWndCenterInParentYPos(parentSize.d_height / 2.0f + y_offset);


	}


}

void Window::FlyToScreenPoint(Point targetPoint, float time) //窗口中心位置从当前位置飞行到屏幕上某一个位置，time为总飞行时�?
{
	d_IsFlying = true;
	d_FlyStartScreenPoint = GetScreenPosOfCenter();
	d_FlyTargetScreenPoint = targetPoint;
	d_FlyElapseTime = 0.0f;
	d_TotalFlyTime = time;

	MoveToStartFlyPoint();
}

void Window::FlyToWndCenter(Window* pTargetWnd, float time)  //窗口中心位置从当前位置飞行到某一个窗口的中心，time为总飞行时�?
{
	d_IsFlying = true;
	d_FlyStartScreenPoint = GetScreenPosOfCenter();
	d_FlyTargetScreenPoint = pTargetWnd->GetScreenPosOfCenter();
	d_FlyElapseTime = 0.0f;
	d_TotalFlyTime = time;

	MoveToStartFlyPoint();

}

void Window::FlyFromPointToWndCenter(Point startPoint, Window* pTargetWnd, float time)//窗口中心位置从某个屏幕坐标飞行到某一个窗口的中心，time为总飞行时�?
{
	d_IsFlying = true;
	d_FlyStartScreenPoint = startPoint;
	d_FlyTargetScreenPoint = pTargetWnd->GetScreenPosOfCenter();
	d_FlyElapseTime = 0.0f;
	d_TotalFlyTime = time;

	MoveToStartFlyPoint();


}

void Window::UpdateFlyPos(float fElapse) //更新飞行效果
{
	if (d_IsFlying)
	{
		d_FlyElapseTime += fElapse;
		if (d_FlyElapseTime >= d_TotalFlyTime)
		{
			if (d_parent)
			{
				Point parentCenterScreenPos = d_parent->GetScreenPosOfCenter();
				float x_offset = d_FlyTargetScreenPoint.d_x - parentCenterScreenPos.d_x;
				float y_offset = d_FlyTargetScreenPoint.d_y - parentCenterScreenPos.d_y;

				Size parentSize = d_parent->getPixelSize();
				SetWndCenterInParentXPos(parentSize.d_width / 2.0f + x_offset);
				SetWndCenterInParentYPos(parentSize.d_height / 2.0f + y_offset);


			}
			d_IsFlying = false;
			d_FlyElapseTime = 0.0f;
			WindowEventArgs args(this);
			fireEvent(EventFlyToTargetPosition, args);
		}
		else
		{
			Point parentCenterScreenPos = d_parent->GetScreenPosOfCenter();
			float percent = d_FlyElapseTime / d_TotalFlyTime;
			float xPos = d_FlyStartScreenPoint.d_x + (d_FlyTargetScreenPoint.d_x - d_FlyStartScreenPoint.d_x)*percent;
			float yPos = d_FlyStartScreenPoint.d_y + (d_FlyTargetScreenPoint.d_y - d_FlyStartScreenPoint.d_y)*percent;
			float x_offset = xPos - parentCenterScreenPos.d_x;
			float y_offset = yPos - parentCenterScreenPos.d_y;

			Size parentSize = d_parent->getPixelSize();
			SetWndCenterInParentXPos(parentSize.d_width / 2.0f + x_offset);
			SetWndCenterInParentYPos(parentSize.d_height / 2.0f + y_offset);

		}
	}
}

void Window::SetClickAniScale(float scale)
{

	//    float fChange=fabsf(scale-d_ClickStateScale);
	//    if (fChange<0.03f)
	{
		d_ClickStateScale = scale;
		invalidate();
	}


}



} // End of  CEGUI namespace section
