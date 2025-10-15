#include "gxwindowdef.h"
namespace GXWINDOW
{

CCustomTextPiece::CCustomTextPiece():
m_impl(NULL),
m_textNotify( new INLINECLASS::CSimpleString), 
m_bNotTransparent( true)
{
}

CCustomTextPiece::~CCustomTextPiece()
{
	delete m_textNotify;
	if(NULL != m_impl)
	{
		m_impl->Release();
		m_impl = NULL;
	}
}

void CCustomTextPiece::SetNotifyString( const wchar_t* szText)
{
	m_textNotify->assign( szText);
}

const wchar_t* CCustomTextPiece::GetNotifyString() const
{
	return m_textNotify->c_str();
}

bool CCustomTextPiece::OnLeftButtonDown( int x, int y, int key)
{
	CComponent::OnLeftButtonDown( x, y, key);
	if( m_textNotify->empty())
		return m_bNotTransparent;
	else
		return true;
}

void CCustomTextPiece::SetTransparent( bool b)
{
	m_bNotTransparent = !b;
}

bool CCustomTextPiece::IsTransparent() const
{
	return !m_bNotTransparent;
}

void CCustomTextPiece::SetAbstractImpl(CCustomTextPieceAbstractImpl* impl)
{
	assert(m_impl == NULL && impl != NULL);
	if(NULL != m_impl || impl == NULL)
		return;
	m_impl = impl;
}

void CCustomTextPiece::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	if(NULL != m_impl)
		m_impl->Draw(draw, l, t, cx, cy, cw, ch);
}
}