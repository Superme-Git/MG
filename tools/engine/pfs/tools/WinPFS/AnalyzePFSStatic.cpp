// AnalyzePFSStatic.cpp : implementation file
//

#include "stdafx.h"
#include "WinPFS.h"
#include "AnalyzePFSStatic.h"


// CAnalyzePFSStatic

IMPLEMENT_DYNAMIC(CAnalyzePFSStatic, CStatic)

CAnalyzePFSStatic::CAnalyzePFSStatic()
: m_filelen( 0)
, m_fcOffset( 0)
, m_crEmpty( RGB(255,0,0))
, m_crValid( RGB(0,0,255))
, m_crFC( RGB(0,255,0))
{

}

CAnalyzePFSStatic::~CAnalyzePFSStatic()
{
}


BEGIN_MESSAGE_MAP(CAnalyzePFSStatic, CStatic)
	ON_WM_PAINT()
END_MESSAGE_MAP()



// CAnalyzePFSStatic message handlers



void CAnalyzePFSStatic::OnPaint()
{
	CRect rc;
	GetClientRect( &rc);
	if( 0 == rc.Width() || 0 == rc.Height())
	{
		return;
	}

	CPaintDC dc(this); // device context for painting
	// TODO: Add your message handler code here
	// Do not call CStatic::OnPaint() for painting messages
	float scale = float( m_filelen) / rc.Width();

	size_t offset = 0;
	int left = 0;
	CRect rcItem;
	for( CValidBlockVector::iterator i=m_blocks.begin(), e=m_blocks.end(); i!=e; )
	{
		CValidBlock& eb = *i;

		if( eb.m_begin != offset)
		{
			rcItem.SetRect( left, rc.top, int( eb.m_begin/scale), rc.bottom);
			if( rcItem.left == rcItem.right)
			{
				rcItem.right = rcItem.left + 1;		// 保证空闲块能画出
			}
			
			dc.FillSolidRect( &rcItem, m_crEmpty);
			offset = eb.m_begin;
		}
		else
		{
			rcItem.SetRect( left, rc.top, int( eb.m_end/scale), rc.bottom);
			dc.FillSolidRect( &rcItem, m_crValid);
			offset = eb.m_end;
			++i;
		}
		if( left < rcItem.right)
		{
			left = rcItem.right;
		}
	}

	rcItem.SetRect( int( m_fcOffset/scale), rc.top, rc.right, rc.bottom);
	dc.FillSolidRect( &rcItem, m_crFC);
}
