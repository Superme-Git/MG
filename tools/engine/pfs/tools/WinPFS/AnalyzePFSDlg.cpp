// AnalyzePFSDlg.cpp : implementation file
//

#include "stdafx.h"
#include "WinPFS.h"
#include "AnalyzePFSDlg.h"
#include "FileCentralCache.h"
#include "WinPFSDoc.h"
// CAnalyzePFSDlg dialog

IMPLEMENT_DYNAMIC(CAnalyzePFSDlg, CDialog)

CAnalyzePFSDlg::CAnalyzePFSDlg(CWnd* pParent /*=NULL*/)
: CDialog(CAnalyzePFSDlg::IDD, pParent)
, m_crEmpty( RGB(255,0,0))
, m_crValid( RGB(0,0,255))
, m_crFC( RGB(0,255,0))
{

}

CAnalyzePFSDlg::~CAnalyzePFSDlg()
{
}

void CAnalyzePFSDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_STATIC_RESULT, m_result);
}


BEGIN_MESSAGE_MAP(CAnalyzePFSDlg, CDialog)
	ON_WM_PAINT()
END_MESSAGE_MAP()


// CAnalyzePFSDlg message handlers

bool SortByOffset( DWORD_PTR left, DWORD_PTR right)
{
	ZIPFILE::CFileCentral* fcLeft = (ZIPFILE::CFileCentral*)left;
	ZIPFILE::CFileCentral* fcRight = (ZIPFILE::CFileCentral*)right;
	return fcLeft->m_uiOffset < fcRight->m_uiOffset;
}

void CAnalyzePFSDlg::SetDocument( CWinPFSDoc* doc)
{
	if( NULL == doc)
	{
		return;
	}
	ZIPFILE::CFileReader::CFileCentralMap& fcm = doc->m_filereader.GetFileCentralMap();
	m_result.SetFileLen( doc->m_filelen);

	CFileCentralCache fcc;
	fcc.reserve( fcm.size());
	for( ZIPFILE::CFileReader::CFileCentralMap::iterator i=fcm.begin(), e=fcm.end(); i!=e; ++i)
	{
		ZIPFILE::CFileCentral& fc = i->second;
		fcc.push_back( (DWORD_PTR)&fc);
	}

	// 按offset从小到大排序
	std::sort( fcc.begin(), fcc.end(), SortByOffset);

	size_t offsetFC = 0;
	CValidBlockVector& ebv = m_result.GetValidBlockVector();
	ebv.reserve( fcc.size());
	CValidBlock eb;
	for( CFileCentralCache::iterator i=fcc.begin(), e=fcc.end(); i!=e; ++i)
	{
		ZIPFILE::CFileCentral* fc = (ZIPFILE::CFileCentral*)(*i);
		eb.m_begin = fc->m_uiOffset;
		eb.m_end = fc->GetDataOffset() + fc->GetCompressedSize();
		offsetFC = eb.m_end;
		ebv.push_back( eb);
	}
	m_result.SetFCOffset( offsetFC);
}

BOOL CAnalyzePFSDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// TODO:  Add extra initialization here
	size_t filelen = m_result.GetFileLen();
	if( filelen != 0)
	{
		CString str;
		str.Format( L"文件总大小    ：%d B", filelen);
		GetDlgItem( IDC_STATIC_FILELEN)->SetWindowText( str);
		
		size_t validlen = 0;
		CValidBlockVector& vbv = m_result.GetValidBlockVector();
		for( CValidBlockVector::iterator i=vbv.begin(), e=vbv.end(); i!=e; ++i)
		{
			CValidBlock& vb = *i;
			validlen += vb.m_end - vb.m_begin;
		}
		str.Format( L"有效数据大小：%d B，占用百分比：%.2f%%", validlen, validlen*100.0/filelen);
		GetDlgItem( IDC_STATIC_VALID)->SetWindowText( str);

		size_t fcOffset = m_result.GetFCOffset();
		validlen = fcOffset - validlen;
		str.Format( L"空闲块大小    ：%d B，占用百分比：%.2f%%", validlen, validlen*100.0/filelen);
		GetDlgItem( IDC_STATIC_EMPTY)->SetWindowText( str);

		validlen = filelen - fcOffset;
		str.Format( L"描述信息大小：%d B，占用百分比：%.2f%%", validlen, validlen*100.0/filelen);
		GetDlgItem( IDC_STATIC_FC)->SetWindowText( str);
	}

	m_result.SetValidColor( m_crValid);
	m_result.SetEmptyColor( m_crEmpty);
	m_result.SetFCColor( m_crFC);

	return TRUE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

void CAnalyzePFSDlg::OnPaint()
{
	CPaintDC dc(this); // device context for painting
	// TODO: Add your message handler code here
	// Do not call CDialog::OnPaint() for painting messages

	const int XOFFSET = 24;
	const int YOFFSET = 30;

	CRect rcWnd;
	GetWindowRect( &rcWnd);

	CRect rc;
	GetDlgItem( IDC_STATIC_VALID)->GetWindowRect( &rc);
	rc.OffsetRect( -rcWnd.left, -rcWnd.top);
	dc.FillSolidRect( rc.left-XOFFSET, rc.top-YOFFSET, 16, 16, m_crValid);

	GetDlgItem( IDC_STATIC_EMPTY)->GetWindowRect( &rc);
	rc.OffsetRect( -rcWnd.left, -rcWnd.top);
	dc.FillSolidRect( rc.left-XOFFSET, rc.top-YOFFSET, 16, 16, m_crEmpty);

	GetDlgItem( IDC_STATIC_FC)->GetWindowRect( &rc);
	rc.OffsetRect( -rcWnd.left, -rcWnd.top);
	dc.FillSolidRect( rc.left-XOFFSET, rc.top-YOFFSET, 16, 16, m_crFC);
}
