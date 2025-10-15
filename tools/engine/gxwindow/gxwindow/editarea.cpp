#include "gxwindowdef.h"
#include "editinline.h" 

namespace GXWINDOW { 

namespace INLINECLASS{

CCombinationEditAreaAllocator		g_DefaultCombinationEditAreaAllocator;

class CEditAreaChangedNotifySet : public GXWINDOW::INLINECLASS::CNotifySet<CEditAreaChangedNotify*>
{
public:
	CEditAreaChangedNotifySet() {}
	virtual ~CEditAreaChangedNotifySet() {}
};

} // namespace INLINECLASS{

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CEditArea::CEditArea( CAbstarctTextDecoder& decoder)
	: CTextArea( decoder),  m_strbuffer( new INLINECLASS::CSimpleString()), m_ceacs( new INLINECLASS::CEditAreaChangedNotifySet()), 
	m_nLimit( 1024), m_bNumber( false), m_LimitQuery( NULL), m_InputHook( NULL), m_bDefaultFocus( false), m_bPastFromClipboard( true)
{
	ShowCursor( GetCursor());
	SetSelectAllow();
}

CEditArea::~CEditArea()
{
	delete m_strbuffer;
	delete m_ceacs;
}

void CEditArea::SetDefaultFocus( bool b)
{
	m_bDefaultFocus = b;
}

bool CEditArea::IsDefaultFocus() const
{
	return m_bDefaultFocus;
}

void CEditArea::EnablePastFromClipboard( bool b)
{
	m_bPastFromClipboard = b;
}

bool CEditArea::IsEnablePastFromClipboard() const
{
	return m_bPastFromClipboard;
}

void CEditArea::OnComponentShow()
{
	CTextArea::OnComponentShow();
	if( m_bDefaultFocus)
		CKeyInput::GetInstance().SetFocus( *this);
}

void CEditArea::EditAreaKeyInputHookAdd( CEditAreaKeyInputHook& eakih)
{
	assert( !m_InputHook && "不要随意设置2个HOOK对象，只能有一个有效");
	m_InputHook = &eakih;
}

void CEditArea::EditAreaKeyInputHookRemove()
{
	m_InputHook = NULL;
}

void CEditArea::EditAreaLimitQueryAdd( CEditAreaLimitQuery& lq)
{
	assert( !m_LimitQuery && "不要随意设置2个查询对象，只能有一个有效");
	m_LimitQuery = &lq;
}

void CEditArea::EditAreaLimitQueryRemove()
{
	m_LimitQuery = NULL;
}

void CEditArea::SetLimit( int n)
{
	m_nLimit = n;
}

int CEditArea::GetLimit() const
{
	return m_nLimit;
}

void CEditArea::SetNumber( bool b)
{
	m_bNumber = b;
}

bool CEditArea::IsNumber() const
{
	return m_bNumber;
}

void CEditArea::AppendText( const wchar_t* szText)
{
	AppendText( szText, 0);
}

void CEditArea::AppendText( const wchar_t* szText, int backgroundEffectId)
{
	if ( !szText) return;

	if ( IsNumber())
	{
		const wchar_t* p = szText;
		while ( *p)
		{
			if ( !isdigit( *p))
				return;
			++ p;
		}
	}

	RemoveSelect();

	std::wstring		str;
	if( m_LimitQuery)
	{
		int ncount = m_LimitQuery->QueryEditAreaLimit( *this, szText, false);
		assert( ncount <= (int)wcslen( szText));
		if( 0 == ncount)
			return;
		str.assign( szText, ncount);
	}
	else
	{
		str.assign( szText);
	}

	if( str.empty())
		return;

	CDecoderTextPieceList		tpl;
	m_decoder->Decode( str.c_str(), tpl);
	m_eec->AppendAtCursor( tpl, m_nLimit, backgroundEffectId);
	RelayoutView();
	EnsureViewCursor();
	m_ceacs->NotifyAll( *this, &CEditAreaChangedNotify::OnEditAreaChanged);
}

void CEditArea::AppendRawText( const wchar_t* szText)
{
	if ( !szText) return;

	if ( IsNumber())
	{
		const wchar_t* p = szText;
		while ( *p)
		{
			if ( !isdigit( *p))
				return;
			++ p;
		}
	}

	RemoveSelect();

	std::wstring		str;
	if( m_LimitQuery)
	{
		int ncount = m_LimitQuery->QueryEditAreaLimit( *this, szText, true);
		assert( ncount <= (int)wcslen( szText));
		str.assign( szText, ncount);
	}
	else
	{
		str.assign( szText);
	}

	CDecoderTextPieceList					tpl;
	tpl.Insert( CDecoderTextPiece( str.c_str()));
	m_eec->AppendAtCursor( tpl, m_nLimit);
	RelayoutView();
	EnsureViewCursor();
	m_ceacs->NotifyAll( *this, &CEditAreaChangedNotify::OnEditAreaChanged);
}
//
//bool CEditArea::Encode( CAbstarctTextEncoder& fte)
//{
//	CDecoderTextPieceList tpl;
//	m_eec->EncodeTextPieceList( tpl);
//	fte.AddTextPieces( tpl);
//	return true;
//}

bool CEditArea::CutToClipboard()
{
	bool b = CopyToClipboard();
	RemoveSelect();
	return b;
}

void CEditArea::RemoveSelect()
{
	if( m_eec->IsHasSelect())
	{
		m_eec->RemoveSelect();
		RelayoutView();
	}
}

bool CEditArea::PastFromClipboard()
{
	if ( !m_bPastFromClipboard)
		return false;

	std::wstring str;

	RemoveSelect();

	if( CClipBoard::GetInstace().GetString( str))
	{
		AppendRawText( str.c_str());
		return true;
	}
	return false;
}

bool CEditArea::OnKeyInput( UINT uMsg, WPARAM wParam, LPARAM lParam)
{
	if ( IsViewOnly())
		return false;

	if( m_InputHook && m_InputHook->OnEditAreaKeyInputHook( uMsg, wParam, lParam, *this))
		return true;

	if( CTextArea::OnKeyInput( uMsg, wParam, lParam))
		return true;

	if ( !IsEnable())
		return true;

	if( WM_CHAR == uMsg)
	{
		switch( wParam)
		{
		case 8: // bs
			if( IsHasSelect())
				RemoveSelect();
			else
				CharBackSpaceFromCursor();
			return true;
		case 13: // return
			if( IsSingleLine())
				return false;
			{
				if ( IsHasSelect())
					RemoveSelect();
				CDecoderTextPieceList					tpl;
				tpl.Insert( CDecoderTextPiece());
				m_eec->AppendAtCursor( tpl, m_nLimit);
				RelayoutView();
				EnsureViewCursor();
				return true;
			}
		case 22: // ctrl + v
			{
				PastFromClipboard();
				return true;
			}
		case 24: // ctrl + x
			if( IsHasSelect())
			{
				CutToClipboard();
				return true;
			}
			return false;
		case 27: // esc
			if( IsHasSelect())
			{
				CancelAllSelect();
				return true;
			}
			return false;
		default:
			break;
		}

		if( m_bNumber)
		{
			if( iswdigit( (wint_t)wParam))
			{
				m_strbuffer->append( 1, (wchar_t)wParam);			
				InsertBufferData();
				return true;
			}
		}
		else if( iswprint( (wint_t)wParam))
		{
			m_strbuffer->append( 1, (wchar_t)wParam);			
            InsertBufferData();
			return true;
		}
		else if( wParam >= 128)
		{
			m_strbuffer->append( 1, (wchar_t)wParam);
			CheckInputTimer();
			return true;
		}
	}
	else if( WM_KEYDOWN == uMsg)
	{
		switch( wParam)
		{
		case 36: // home
			if ( IsHasSelect())
				CancelAllSelect();
			m_eec->SetCursorHome();
			m_eec->GetCursorPosition( m_nCursorX, m_nCursorY, GetLineSpace(), GetDefaultFont(), IsPassword());
			EnsureViewCursor();
			return true;
		case 35: // end
			if ( IsHasSelect())
				CancelAllSelect();
			m_eec->SetCursorEnd();
			m_eec->GetCursorPosition( m_nCursorX, m_nCursorY, GetLineSpace(), GetDefaultFont(), IsPassword());
			EnsureViewCursor();
			return true;
		case 46: // delete
			if( IsHasSelect())
				RemoveSelect();
			else
				CharDeleteFromCursor();
			return true;
		case VK_LEFT: // <-
			if ( IsHasSelect())
				CancelAllSelect();
			m_eec->SetCursorLast();
			m_eec->GetCursorPosition( m_nCursorX, m_nCursorY, GetLineSpace(), GetDefaultFont(), IsPassword());
			EnsureViewCursor();
			return true;
		case VK_RIGHT: // ->
			if ( IsHasSelect())
				CancelAllSelect();
			m_eec->SetCursorNext();
			m_eec->GetCursorPosition( m_nCursorX, m_nCursorY, GetLineSpace(), GetDefaultFont(), IsPassword());
			EnsureViewCursor();
			return true;
		case VK_UP:
			{
				int y = m_nCursorY - ( CTextDrawManager::GetInstance().GetFontHeight( GetDefaultFont()) + GetLineSpace());
				if ( y >= 0)
					SetCursorPos( m_nCursorX, y);
			}
			return true;
		case VK_DOWN:
			{
				int y = m_nCursorY + ( CTextDrawManager::GetInstance().GetFontHeight( GetDefaultFont()) + GetLineSpace());
				if ( y < GetAllLinesHeight())
					SetCursorPos( m_nCursorX, y);
			}
			return true;
		default:
			break;
		}
	}

	return false;
}

void CEditArea::CheckInputTimer()
{
	const int nDefaultTimerDelay = 200;
	if( m_timerInput.IsNull())
		CTimer::NewTimer( *this, nDefaultTimerDelay, m_timerInput);
}

void CEditArea::InsertBufferData()
{
	if( !m_strbuffer->empty())
	{
		//std::wstring	wstr = CClipBoard::StringToWString( *m_strbuffer);
		//AppendRawText( wstr.c_str());
		AppendRawText( m_strbuffer->c_str());
		m_strbuffer->erase();
	}
}

void CEditArea::OnTimer( const TIMERID& nid)
{
	if( nid == m_timerInput)
	{
		InsertBufferData();
		m_timerInput.ResetID();
		return;
	}
	CTextArea::OnTimer( nid);
}

void CEditArea::CharBackSpaceFromCursor()
{
	m_eec->SetCursorBackspace();
	RelayoutView();
	EnsureViewCursor();
	m_ceacs->NotifyAll( *this, &CEditAreaChangedNotify::OnEditAreaChanged);
}

void CEditArea::CharDeleteFromCursor()
{
	m_eec->SetCursorDelete();
	RelayoutView();
	EnsureViewCursor();
	m_ceacs->NotifyAll( *this, &CEditAreaChangedNotify::OnEditAreaChanged);
}

void CEditArea::EditAreaChangedNotifyAdd( CEditAreaChangedNotify& meacn)
{
	m_ceacs->Insert( &meacn);
}

void CEditArea::EditAreaChangedNotifyRemove( CEditAreaChangedNotify& meacn)
{
	m_ceacs->Remove( &meacn);
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCombinationEditAreaAllocator::CCombinationEditAreaAllocator( CAbstarctTextDecoder& decoder)
	: m_Decoder( decoder.Clone())
{
}

CCombinationEditAreaAllocator::~CCombinationEditAreaAllocator()
{
	m_Decoder->Delete();
}

CScrollPanel* CCombinationEditAreaAllocator::NewScrollPanel()
{
	return new CScrollPanel();
}

CEditArea* CCombinationEditAreaAllocator::NewEditArea()
{
	return new CEditArea( GetTextDecoder());
}

void CCombinationEditAreaAllocator::DeleteComponent( CComponent* pc)
{
	delete pc;
}

CAbstarctTextDecoder& CCombinationEditAreaAllocator::GetTextDecoder()
{
	return *m_Decoder;
}

CCombinationEditAreaAllocator& CCombinationEditAreaAllocator::GetInstance()
{
	return INLINECLASS::g_DefaultCombinationEditAreaAllocator;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCombinationEditArea::CCombinationEditArea( CCombinationEditAreaAllocator& alloc)
	: m_alloc( &alloc)
{
	m_sp = m_alloc->NewScrollPanel();
	m_ea = m_alloc->NewEditArea();

	m_sp->SetAutoClientWidth( true);
	m_sp->SetComponent( *m_ea);
	m_ea->SetSelectAllow( true);
	m_ea->SetSingleLine( false);
}

CCombinationEditArea::~CCombinationEditArea()
{
	m_alloc->DeleteComponent( m_sp);
	m_alloc->DeleteComponent( m_ea);
}

CScrollPanel& CCombinationEditArea::GetScrollPanel()
{
	return *m_sp;
}

CEditArea& CCombinationEditArea::GetEditArea()
{
	return *m_ea;
}

void CCombinationEditArea::SetSize( int w, int h)
{
	m_sp->SetSize( w, h);
	m_ea->SetWidth( w - m_sp->GetVerticalScrollBar()->GetWidth());
	m_ea->SetMinWidthHeight( h);
}

void CCombinationEditArea::SetCombinationSize( int w, int h)
{
	SetSize( w, h);
	if ( CComponent* pc = dynamic_cast<CComponent*>(m_ea))
		pc->SetSize( pc->GetWidth(), h);
}

void CCombinationEditArea::SetPos( int x, int y)
{
	m_sp->SetPos( x, y);
}

void CCombinationEditArea::ShowCursor( ImageParams& ip)
{
	m_ea->ShowCursor( ip);
}

void CCombinationEditArea::AppendText( const wchar_t* szText)
{
	m_ea->AppendText( szText);
}

void CCombinationEditArea::SetSelectAllow( bool b)
{
	m_ea->SetSelectAllow( b);
}

void CCombinationEditArea::SetSingleLine( bool b)
{
	m_ea->SetSingleLine( b);
}

void CCombinationEditArea::SetViewOnly( bool bViewOnly)
{
	m_sp->SetViewOnly( bViewOnly);
}

bool CCombinationEditArea::IsViewOnly() const
{
	return m_sp->IsViewOnly();
}

CCombinationEditArea::operator CComponent*()
{
	return m_sp;
}

CCombinationEditArea::operator CComponent&()
{
	return *m_sp;
}

CComponent* CCombinationEditArea::operator&()
{
	return m_sp;
}

void CCombinationEditArea::SetComponentName( const wchar_t* name)
{
	m_sp->SetComponentName( name);
	m_ea->SetComponentName( name);
}

const wchar_t* CCombinationEditArea::GetComponentName() const
{
	return m_sp->GetComponentName();
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCombinationSingleLineEditArea::CCombinationSingleLineEditArea( CCombinationEditAreaAllocator& alloc)
	: CCombinationEditArea( alloc)
{
	CEditArea& ea = GetEditArea();
	ea.SetSingleLine();

	CScrollPanel& sp = GetScrollPanel();

	sp.GetHorizontalScrollBar()->SetSize( 20, 0);
	sp.GetVerticalScrollBar()->SetSize( 0, 20);
}

CCombinationSingleLineEditArea::~CCombinationSingleLineEditArea()
{
}

void CCombinationSingleLineEditArea::SetSize( int w, int h)
{
	CCombinationEditArea::SetSize( w, h);
	GetEditArea().SetMinWidthHeight( w);
}

}	// namespace GXWINDOW { 

