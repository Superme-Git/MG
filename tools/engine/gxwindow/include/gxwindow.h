#pragma once

#ifdef GXWINDOW_LIB
#define GXWINDOW_EXPORTS_DLL
#else
#ifdef GXWINDOW_EXPORTS
#define GXWINDOW_EXPORTS_DLL __declspec( dllexport )
#else
#define GXWINDOW_EXPORTS_DLL __declspec( dllimport )
#endif
#endif


// 下面这个宏在工程属性中配置
// #define GXWINDOW_ENABLE_BATCH_RENDER


#include "uncopyable.h"

#include "draw.h"
#include "textdrawmanager.h"
#include "uiaudio.h"

#include "timer.h" 
#include "keyinput.h"

#include "imagebase.h"
#include "component.h"
#include "tipshowpos.h"
#include "frame.h"

#include "textcomponent.h" 
#include "imagecomponent.h"
#include "iconcomoment.h"
#include "CustomTextPiece.h"

#include "buttons.h" 
#include "barcomponent.h" 
#include "scrollpanel.h" 

#include "listcomponent.h" 
#include "table.h" 
#include "treectrl.h" 
#include "tabcontrol.h" 

#include "editbase.h" 
#include "editexpand.h" 
#include "textarea.h" 
#include "editarea.h" 

#include "package.h"
#include "autocloseframe.h"

namespace GXWINDOW {

GXWINDOW_EXPORTS_DLL void GXWInitialize( HWND hWnd, int w, int h);
GXWINDOW_EXPORTS_DLL void GXWUninitialize();
GXWINDOW_EXPORTS_DLL void GXWSetSize( int w, int h);
GXWINDOW_EXPORTS_DLL void GXWEnableMousePosChange( bool bEnable);

GXWINDOW_EXPORTS_DLL void GXWSetDefaultRenderer( CDraw* renderer);

GXWINDOW_EXPORTS_DLL const CPanel& GXWGetDesktopPanel();
GXWINDOW_EXPORTS_DLL void GXWSetDesktopLayoutManager( const CLayoutManager& lm);
GXWINDOW_EXPORTS_DLL void GXWDesktopDoLayout();

GXWINDOW_EXPORTS_DLL CAbstractTipInfoFrameManager* GXWSetTipFrameManager( CAbstractTipInfoFrameManager* ptifm = NULL);
GXWINDOW_EXPORTS_DLL CAbstractTipInfoFrameManager* GXWGetTipFrameManager();
GXWINDOW_EXPORTS_DLL CFrame* GXWSetTipFrame( CFrame* pFrame = NULL);
GXWINDOW_EXPORTS_DLL CFrame* GXWGetTipFrame();
GXWINDOW_EXPORTS_DLL CTipShowPosLayout* GXWSetTipShowPosLayout( CTipShowPosLayout* pLayout = NULL);
GXWINDOW_EXPORTS_DLL CTipShowPosLayout* GXWGetTipShowPosLayout();
GXWINDOW_EXPORTS_DLL void GXWSetFrameFront( CFrame& frame);
GXWINDOW_EXPORTS_DLL void GXWSetFrameBack( CFrame& frame);
GXWINDOW_EXPORTS_DLL void GXWSetComponentBorderColor( COLOR clr);
GXWINDOW_EXPORTS_DLL void GXWSetFramePositionRestrict( bool bRestrict); // 设为true时, 所有Frame无法移出Desktop范围, 默认为false

GXWINDOW_EXPORTS_DLL void GXWUpdate( CDraw& draw);
GXWINDOW_EXPORTS_DLL void GXWUpdate();
GXWINDOW_EXPORTS_DLL void GXTimerUpdate();

GXWINDOW_EXPORTS_DLL bool GXWMessage( HWND hWnd, UINT uMsg, WPARAM wParam, LPARAM lParam);

GXWINDOW_EXPORTS_DLL void GXWMessageDispatchHookBeforeAdd( CMessageDispatchNotify& mdn);
GXWINDOW_EXPORTS_DLL void GXWMessageDispatchHookBeforeRemove( CMessageDispatchNotify& mdn);
GXWINDOW_EXPORTS_DLL void GXWMessageDispatchHookAfterAdd( CMessageDispatchNotify& mdn);
GXWINDOW_EXPORTS_DLL void GXWMessageDispatchHookAfterRemove( CMessageDispatchNotify& mdn);

GXWINDOW_EXPORTS_DLL void GXWPostFrameRenderNotifyAdd( CPostFrameRenderNotify& pfrn);
GXWINDOW_EXPORTS_DLL void GXWPostFrameRenderNotifyRemove( CPostFrameRenderNotify& pfrn);

GXWINDOW_EXPORTS_DLL int GXWGetMousePointX();
GXWINDOW_EXPORTS_DLL int GXWGetMousePointY();
GXWINDOW_EXPORTS_DLL int GXWGetFrameCount();
GXWINDOW_EXPORTS_DLL CFrame* GXWGetFrame( int n);
GXWINDOW_EXPORTS_DLL CFrame* GXWGetTopAutoCloseFrame();
GXWINDOW_EXPORTS_DLL void GXWCloseAllFrame();

GXWINDOW_EXPORTS_DLL void HookFrameClosedNotifyAdd( CFrameShowHideNotify& fcn);
GXWINDOW_EXPORTS_DLL void HookFrameClosedNotifyRemove( CFrameShowHideNotify& fcn);

GXWINDOW_EXPORTS_DLL void GXWSetStringToClipBoard( const wchar_t* text);

//启动单词自动换行功能 add by yzp 2011-04-09
GXWINDOW_EXPORTS_DLL void GXWEnableWordBreak( bool bEnable);

#ifdef GXWINDOW_ENABLE_BATCH_RENDER
GXWINDOW_EXPORTS_DLL void GXWNeedRedraw(); // 暴露这个接口使得外部定制控件也能够触发redraw
GXWINDOW_EXPORTS_DLL void GXWSetBatchRender( bool batch);
GXWINDOW_EXPORTS_DLL bool GXWGetBatchRender();
#endif

} //namespace GXWINDOW {

