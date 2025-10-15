#include "gxwindowdef.h"

namespace GXWINDOW {

namespace INLINECLASS { 

class CKeyInputNotifySet : public std::list<CKeyInputNotify*>
{
public:
	CKeyInputNotifySet() {}
	virtual ~CKeyInputNotifySet() {}
public:
	void Remove( CKeyInputNotify* kin)
	{
		for( iterator it = begin(); it != end(); ++ it)
		{
			CKeyInputNotify* ps = *it;
			if( ps->IsSameNotify( kin))
			{
				erase( it);
				return;
			}
		} //for( iterator it = begin(); it != end(); ++ it)
	}
};

namespace IMMCTRL {

static HIMC g_hImc = NULL;

static void Disable()
{
	ImmAssociateContext( CDesktop::GetDefault().GetMainHwnd(), NULL);
}

static void Enable()
{
	assert( g_hImc);
	ImmAssociateContext( CDesktop::GetDefault().GetMainHwnd(), g_hImc);
}

static void SetEnable( bool b)
{
	if( b)
		Enable();
	else
		Disable();
}

static void Initialize()
{
	HWND hMainWnd = CDesktop::GetDefault().GetMainHwnd();
	if( IsWindow( hMainWnd))
	{
		g_hImc = ImmGetContext( hMainWnd);
		assert( g_hImc);
		Disable();
	}
}

} // namespace IMMCTRL

static class CKeyEnterEscapeHookSet : public std::list<CKeyEnterEscapeHook*>
{
public:
	CKeyEnterEscapeHookSet() {}
	virtual ~CKeyEnterEscapeHookSet() {}
public:
	void InsertHook( CKeyEnterEscapeHook& kin)
	{
		push_front( &kin);
	}
	void Remove( CKeyEnterEscapeHook& kin)
	{
		for( iterator it = begin(), ite = end(); it != ite; ++ it)
		{
			CKeyEnterEscapeHook*&  k = *it;
			if( &kin == k)
			{
				erase( it);
				return;
			}
		}
	}
	CKeyEnterEscapeHook* PopFront()
	{
		if( empty())
			return NULL;
		CKeyEnterEscapeHook* f = front();
		pop_front();
		return f;
	}
} g_KeyEnterEscapeHookSet;
} // namespace INLINECLASS { 

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CKeyInputNotify::CKeyInputNotify()
{
}

CKeyInputNotify::~CKeyInputNotify()
{
}

CKeyEnterEscapeHook::CKeyEnterEscapeHook()
{
}

CKeyEnterEscapeHook::~CKeyEnterEscapeHook()
{
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CKeyInput::CKeyInput()
	: m_kins( new INLINECLASS::CKeyInputNotifySet()), m_lastkin( NULL), m_defaultkin(NULL), m_EnterEscapeHook( NULL)
{
	INLINECLASS::IMMCTRL::Initialize();
}

CKeyInput::~CKeyInput()
{
	delete m_kins;
}

CKeyInput& CKeyInput::GetInstance()
{
	static CKeyInput instance;
	return instance;
}

void CKeyInput::KeyInputNotifyAdd( CKeyInputNotify& kin)
{
	m_kins->push_back( &kin);
}

void CKeyInput::KeyInputNotifyRemove( CKeyInputNotify& kin)
{
	m_kins->Remove( &kin);
	if( m_lastkin == &kin)
		ReleaseFocus();
}

void CKeyInput::SetFocus( CKeyInputNotify& kin)
{
	if( m_lastkin == &kin)
		return;
	if( m_lastkin)
		m_lastkin->OnLostKeyFocus();
	m_lastkin = &kin;
	if( m_lastkin)
		INLINECLASS::IMMCTRL::SetEnable( m_lastkin->OnSetKeyFocus());
}
CKeyInputNotify* CKeyInput::GetFocus()
{
	return m_lastkin;
}
void CKeyInput::SetDefaultFocus(CKeyInputNotify *pkin)
{
	m_defaultkin = pkin;
	if( NULL == m_lastkin && m_defaultkin)
		SetFocus( *m_defaultkin);
}

CKeyInputNotify* CKeyInput::GetDefaultFocus()
{
	return m_defaultkin;
}
void CKeyInput::ReleaseFocus( CKeyInputNotify& kin)
{
	if( m_lastkin == &kin)
		ReleaseFocus();
}

void CKeyInput::ReleaseFocus()
{
	INLINECLASS::IMMCTRL::Disable();
	if( m_lastkin)
		m_lastkin->OnLostKeyFocus();
	m_lastkin = GetDefaultFocus();
	if (m_lastkin)
		INLINECLASS::IMMCTRL::SetEnable( m_lastkin->OnSetKeyFocus());
}

void CKeyInput::SetEnterEscapeHook( CKeyEnterEscapeHook& hook)
{
	if( m_EnterEscapeHook)
		INLINECLASS::g_KeyEnterEscapeHookSet.InsertHook( *m_EnterEscapeHook);
	m_EnterEscapeHook = &hook;
}

void CKeyInput::ReleaseEnterEscapeHook( CKeyEnterEscapeHook& hook)
{
	if( m_EnterEscapeHook == &hook)
		m_EnterEscapeHook = INLINECLASS::g_KeyEnterEscapeHookSet.PopFront();
	else
		INLINECLASS::g_KeyEnterEscapeHookSet.Remove( hook);
}

void CKeyInput::ReleaseEnterEscapeHook()
{
	m_EnterEscapeHook = NULL;
	INLINECLASS::g_KeyEnterEscapeHookSet.clear();
}

bool CKeyInput::OnDispatchCharMessage( UINT uMsg, WPARAM wParam, LPARAM lParam, CDesktop& desktop)
{
	if( WM_CHAR == uMsg)
	{
		if( VK_RETURN == wParam)
		{
			if( m_EnterEscapeHook && m_EnterEscapeHook->OnKeyEnterEscapePress( true))
				return true;
		}
		else if( VK_ESCAPE == wParam)
		{
			if( m_EnterEscapeHook && m_EnterEscapeHook->OnKeyEnterEscapePress( false))
				return true;
		}
	}
	if( m_lastkin && m_lastkin->OnKeyInput( uMsg, wParam, lParam))
		return true;
	if( WM_CHAR == uMsg)
	{
		if( VK_ESCAPE == wParam)
		{
			 //INLINECLASS::CAbstactDesktop& ad = *desktop.m_desktop;
			 {
				 CFrame* f = desktop.GetPopupFrame();
				 if( f)
				 {
					 desktop.SetPopupFrame( NULL);
					 return true;
				 }
			 }
			 {
				 int nCount = desktop.GetChildCount();
				 //for( int i = 0; i < nCount; i ++)
				 //{
				 //	CFrame* f = dynamic_cast<CFrame*>( ad.GetChild( i));
				 //	assert( f);
				 //	if( f->IsAutoClose())
				 //	{
				 //		f->Hide();
				 //		return true;
				 //	}
				 //} // for( int i = 0; i < nCount; i ++)
				 for( int i = nCount - 1; i >= 0; i --) // 逆序, 在顶层的窗口先关闭
				 {
					 CFrame* f = dynamic_cast<CFrame*>( desktop.GetChild( i));
					 assert( f);
					 if( f->IsCanEscClose())
					 {
						 f->Hide();
						 return true;
					 }
				 } // for( int i = nCount - 1; i >= 0; i --)
			 }
		 }
	} // if( WM_KEYDOWN == uMsg && VK_ESCAPE == wParam)
	return false;
}

} // namespace GXWINDOW {

