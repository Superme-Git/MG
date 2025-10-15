#include "gxwindowdef.h"

namespace GXWINDOW {

void GXWSetDefaultRenderer( CDraw* renderer)
{
	CDesktop::GetDefault().SetDefaultRenderer( renderer);
}

void GXWUpdate()
{
	CDesktop::GetDefault().Update();
}

void GXWUpdate( CDraw& draw)
{
	CDesktop::GetDefault().Update( draw);
}

void GXWSetSize( int w, int h)
{
	CDesktop::GetDefault().SetSize( w, h);
}

void GXWEnableMousePosChange( bool bEnable)
{
	CDesktop::GetDefault().EnableMousePosChange( bEnable);
}

void GXWInitialize( HWND hWnd, int w, int h)
{
	_CrtSetDbgFlag( _CRTDBG_ALLOC_MEM_DF | _CRTDBG_LEAK_CHECK_DF); // 用于在debug模式下检测内存泄漏

	CDesktop::GetDefault().SetMainHwnd( hWnd);
	CDesktop::GetDefault().SetSize( w, h);
}

void GXWUninitialize()
{
	CDesktop::GetDefault().SetMainHwnd( NULL);
	CTextDrawManager::ReleaseInstance();
	CDesktop::CloseDefault();
}

const CPanel& GXWGetDesktopPanel()
{
	return CDesktop::GetDefault().GetDesktopPanel();
}

void GXWSetDesktopLayoutManager( const CLayoutManager& lm)
{
	CDesktop::GetDefault().SetDesktopLayoutManager( lm);
}

void GXWDesktopDoLayout()
{
	CDesktop::GetDefault().DoLayout();
}

bool GXWMessage( HWND hWnd, UINT uMsg, WPARAM wParam, LPARAM lParam)
{
	return CDesktop::GetDefault().Message( hWnd, uMsg, wParam, lParam);
}

void GXWMessageDispatchHookBeforeAdd( CMessageDispatchNotify& mdn)
{
	CDesktop::GetDefault().MessageDispatchHookBeforeAdd( mdn);
}

void GXWMessageDispatchHookBeforeRemove( CMessageDispatchNotify& mdn)
{
	CDesktop::GetDefault().MessageDispatchHookBeforeRemove( mdn);
}

void GXWMessageDispatchHookAfterAdd( CMessageDispatchNotify& mdn)
{
	CDesktop::GetDefault().MessageDispatchHookAfterAdd( mdn);
}

void GXWMessageDispatchHookAfterRemove( CMessageDispatchNotify& mdn)
{
	CDesktop::GetDefault().MessageDispatchHookAfterRemove( mdn);
}

void GXWPostFrameRenderNotifyAdd( CPostFrameRenderNotify& pfrn)
{
	CDesktop::GetDefault().PostFrameRenderAdd( pfrn);
}

void GXWPostFrameRenderNotifyRemove( CPostFrameRenderNotify& pfrn)
{
	CDesktop::GetDefault().PostFrameRenderRemove( pfrn);
}

int GXWGetMousePointX()
{
	return CDesktop::GetDefault().GetMousePointX();
}

int GXWGetMousePointY()
{
	return CDesktop::GetDefault().GetMousePointY();
}

int GXWGetFrameCount()
{
	return CDesktop::GetDefault().GetFrameCount();
}

CFrame* GXWGetFrame( int n)
{
	return CDesktop::GetDefault().GetFrame( n);
}

void GXWCloseAllFrame()
{
	CDesktop::GetDefault().CloseAllFrame();
}

CFrame* GXWGetTopAutoCloseFrame()
{
	{
		if ( CFrame* f = CDesktop::GetDefault().GetPopupFrame())
			return f;
	}

	{
		int nCount = CDesktop::GetDefault().GetChildCount();
		for( int i = nCount - 1; i >= 0; i --) // 逆序, 在顶层的窗口先关闭
		{
			if ( CFrame* f = dynamic_cast<CFrame*>( CDesktop::GetDefault().GetChild( i)))
			{
				if( f->IsCanEscClose())
					return f;
			}
		} // for( int i = nCount - 1; i >= 0; i --)
	}

	return NULL;
}

CAbstractTipInfoFrameManager* GXWSetTipFrameManager( CAbstractTipInfoFrameManager* ptifm)
{
	return CDesktop::GetDefault().SetTipInfoFrameManager( ptifm);
}

CAbstractTipInfoFrameManager* GXWGetTipFrameManager()
{
	return &CDesktop::GetDefault().GetTipInfoFrameManager();
}

CFrame* GXWSetTipFrame( CFrame* pFrame)
{
	return CDesktop::GetDefault().SetTipFrame( pFrame);
}

CFrame* GXWGetTipFrame()
{
	return CDesktop::GetDefault().GetTipFrame();
}

void GXWSetFrameFront( CFrame& frame)
{
	CDesktop::GetDefault().SetComponentForeground( &frame);
}

void GXWSetComponentBorderColor( COLOR clr)
{
	CDesktop::GetDefault().SetBorderColor( clr);
}

void GXWSetFramePositionRestrict( bool bRestrict)
{
	CDesktop::GetDefault().SetFramePositionRestrict( bRestrict);
}

void GXWSetFrameBack( CFrame& frame)
{
	CDesktop::GetDefault().SetComponentBackground( &frame);
}

CTipShowPosLayout* GXWSetTipShowPosLayout( CTipShowPosLayout* pLayout)
{
	return CDesktop::GetDefault().SetTipShowPosLayout( pLayout);
}

CTipShowPosLayout* GXWGetTipShowPosLayout()
{
	return CDesktop::GetDefault().GetTipShowPosLayout();
}

void HookFrameClosedNotifyAdd( CFrameShowHideNotify& fcn)
{
	CDesktop::GetDefault().FrameClosedNotifyAdd( fcn);
}

void HookFrameClosedNotifyRemove( CFrameShowHideNotify& fcn)
{
	CDesktop::GetDefault().FrameClosedNotifyRemove( fcn);
}

void GXWSetStringToClipBoard( const wchar_t* text)
{
	std::wstring str( text);
	CClipBoard::GetInstace().SetString( str);
}

void GXWEnableWordBreak( bool bEnable)
{
	CTextAreaProperties::SetEnableWordBreak(bEnable);
}

#ifdef GXWINDOW_ENABLE_BATCH_RENDER
bool g_bBatchRender = false;
bool g_bNeedRedraw = false;
bool GXWIsNeedRedraw()
{
	return g_bNeedRedraw;
}
void GXWCancelNeedRedraw()
{
	g_bNeedRedraw = false;
}
void GXWNeedRedraw()
{
	g_bNeedRedraw = true;
}
void GXWSetBatchRender( bool batch)
{
	g_bBatchRender = batch;
}
bool GXWGetBatchRender()
{
	return g_bBatchRender;
}
#endif


} //namespace GXWINDOW {
