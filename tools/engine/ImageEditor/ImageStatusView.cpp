#include "StdAfx.h"
#include "ImageStatusView.h"
#include "ImageEditorDoc.h"

using namespace Nuclear;

IMPLEMENT_DYNCREATE(CImageStatusView, CView)

CImageStatusView::CImageStatusView(void)
{
	m_fmtMap[Nuclear::XPTEXFMT_A8R8G8B8] = std::wstring(L"A8R8G8B8");
	m_fmtMap[Nuclear::XPTEXFMT_A4R4G4B4] = std::wstring(L"A4R4G4B4");
	m_fmtMap[Nuclear::XPTEXFMT_DXT1] = std::wstring(L"DXT1");
	m_fmtMap[Nuclear::XPTEXFMT_DXT2] = std::wstring(L"DXT2");
	m_fmtMap[Nuclear::XPTEXFMT_DXT3] = std::wstring(L"DXT3");
	m_fmtMap[Nuclear::XPTEXFMT_DXT4] = std::wstring(L"DXT4");
	m_fmtMap[Nuclear::XPTEXFMT_DXT5] = std::wstring(L"DXT5");
}

CImageStatusView::~CImageStatusView(void)
{
}

BEGIN_MESSAGE_MAP(CImageStatusView, CView)
END_MESSAGE_MAP()

// CRegionStatusView drawing

void CImageStatusView::OnDraw(CDC* pDC)
{
	CImageEditorDoc* pDoc = (CImageEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	// TODO: add draw code here
	PImg& imgFile = pDoc->GetImgObject();

	RECT rect;
	GetClientRect(&rect);

	int iFileWidth = imgFile.m_nFileWidth;
	int iFileHeight = imgFile.m_nFileHeight;
	int centerPointX = imgFile.CenterPoint.x;
	int centerPointY = imgFile.CenterPoint.y;
	CPoint pt0 = CPoint(imgFile.pos[enum_left].x, imgFile.pos[enum_left].y);
	CPoint pt1 = CPoint(imgFile.pos[enum_bottom].x, imgFile.pos[enum_bottom].y);
	CPoint pt2 = CPoint(imgFile.pos[enum_right].x, imgFile.pos[enum_right].y);
	CPoint pt3 = CPoint(imgFile.pos[enum_up].x, imgFile.pos[enum_up].y);

	CString str;
	str.Format(_T("图片宽*高:  %d * %d"), iFileWidth, iFileHeight);
	pDC->SetBkMode(TRANSPARENT);
	pDC->TextOut(0, 0, str);

	Nuclear::XPTEXTURE_FORMAT aaa = (Nuclear::XPTEXTURE_FORMAT)(imgFile.m_texfmt);
	pDC->TextOut(180, 0, m_fmtMap[aaa].c_str());

	std::wstring texfmts[7] = {
		L"A8R8G8B8",L"A4R4G4B4",L"DXT1",L"DXT2",L"DXT3",L"DXT4",L"DXT5"
	};

	CImageInfoView* pView = pDoc->GetImageInfoView();
	for(int i=0; i<7; ++i)
	{
		if(m_fmtMap[aaa] == texfmts[i])
		{
			pView->m_comboImgFileFormat.SetCurSel(i);
			break;
		}
	}


	str.Format(_T("重心:  (%d, %d)"), centerPointX, centerPointY);
	pDC->TextOut(0, 20, str);

	str.Format(_T("边框顶点(左、下、右、上):  (%d, %d)、(%d, %d)、(%d, %d)、(%d, %d)"), 
					pt0.x, pt0.y, pt1.x, pt1.y, pt2.x, pt2.y, pt3.x, pt3.y);
	pDC->TextOut(0, 40, str);

}


// CRegionStatusView diagnostics

#ifdef _DEBUG
void CImageStatusView::AssertValid() const
{
	CView::AssertValid();
}

#ifndef _WIN32_WCE
void CImageStatusView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}
#endif
#endif //_DEBUG
