// ImageEditorView.cpp : CImageEditorView 类的实现
//

#include "stdafx.h"
#include "ImageEditor.h"

#include "ImageEditorDoc.h"
#include "ImageEditorView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

#include <math.h>

#include "..\engine\common\util.h"
#include "..\engine\common\log.h"
#include "..\engine\common\recttopologylist.h"
#include "..\engine\sprite\spritemanager.h"
#include "..\engine\sprite\componentsprite.h"
#include "..\engine\engine\configmanager.h"

using namespace Nuclear;

IMPLEMENT_DYNCREATE(CImageEditorView, CView)

BEGIN_MESSAGE_MAP(CImageEditorView, CView)
	// 标准打印命令
	ON_COMMAND(ID_FILE_PRINT, &CView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_DIRECT, &CView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_PREVIEW, &CView::OnFilePrintPreview)
	ON_WM_SIZE()
	ON_WM_SIZING()
	ON_WM_LBUTTONDOWN()
	ON_WM_MOUSEMOVE()
	ON_WM_LBUTTONUP()
	ON_WM_HSCROLL()
	ON_WM_VSCROLL()
	ON_WM_CLOSE()
	ON_WM_RBUTTONDOWN()
	ON_COMMAND(ID_DELMASK, &CImageEditorView::OnDelmask)
	ON_WM_KEYDOWN()
	ON_WM_TIMER()
END_MESSAGE_MAP()

// CImageEditorView 构造/析构

CImageEditorView::CImageEditorView()
{
	// TODO: 在此处添加构造代码
	m_pRenderer = NULL;
	m_fcoefU = 1.0f;
	m_fcoefV = 1.0f;
	m_bDrawBorder = false;

	m_nViewWidth = 0;
	m_nViewHeight = 0;
	m_nScrollPosX = 0;
	m_nScrollPosY = 0;
	m_vectorPicID.clear();
	m_bIsPlay = false;
	m_bMoveBorder = false;
	m_bSetMaskpt = false;
	m_nMovePointIndex = -1;
	m_bAdjustMaskpt = false;

	m_bModifyMask = false;

	m_nCurSelMaskpt = -1;
	m_nMouseMoveCnt = 0;
	m_imgMasks.clear();

	m_fZoomRate = 1.0f;
	m_nScollCnt = 0;
	for(int i=0; i<4; i++)
	{
		m_pointBorderCent[i] = CPoint(0, 0);
		m_pointBorderVertex[i] = CPoint(0, 0);
	}
	m_initialPoint = CPoint(0, 0);

	m_pConfigMan = new Nuclear::ConfigManager(this);
	m_pConfigMan->Init();

	//精灵相关
	m_pSprMan = new Nuclear::SpriteManager(this);


	if (!m_pSprMan->PreInit())
	{
		AfxMessageBox(L"精灵管理器初始化整体失败");
		return;
	}
	size_t SprCount = m_pSprMan->GetInitCount();
	bool succ = true;
	for (size_t i=0;i<SprCount;++i)
	{
		if( m_pSprMan->Init(i, 1) )
		{	
			XPLOG_ERROR(L"精灵管理器初始化部分失败，第 %d 号精灵有问题！", i);
			succ = false;
		}
	}
	if (!succ)
	{
		AfxMessageBox(L"精灵管理器初始化部分失败，请查看日志并且联系美术～");
	}
	if (!m_pSprMan->PostInit())
	{
		AfxMessageBox(L"精灵管理器初始化失败");
		return;
	}

	m_pAniMan = new Nuclear::AniManager(this);
	if( !m_pAniMan->Init() ) 
	{
		//XPALARM("动画管理器初始化失败");
		XPLOG_INFO(L"Init Faild.\n");
//		return;
	}

	//m_pSprite = NULL;
	m_bPicTransFlag = false;
}

CImageEditorView::~CImageEditorView()
{
	delete m_pConfigMan;
	delete m_pAniMan;
	m_pAniMan = NULL;
	delete m_pSprMan;
	m_pSprMan = NULL;
//	delete m_pSprite;
	//m_pSprite = NULL;

	DestroyRenderer(m_pRenderer);
}

BOOL CImageEditorView::PreCreateWindow(CREATESTRUCT& cs)
{
	// TODO: 在此处通过修改
	//  CREATESTRUCT cs 来修改窗口类或样式

	return CView::PreCreateWindow(cs);
}

// CImageEditorView 绘制
void CImageEditorView::InitBorderCentPos()
{
	CImageEditorDoc* pDoc = (CImageEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (pDoc == NULL)
		return;

	PImg& imgFile = pDoc->GetImgObject();

	for(int i=0; i<4; i++)
	{
		int j = (i+1)>3 ? 0:(i+1);
		m_pointBorderCent[i].x = (imgFile.pos[i].x + imgFile.pos[j].x)/2;
		m_pointBorderCent[i].y = (imgFile.pos[i].y + imgFile.pos[j].y)/2;

		m_pointBorderVertex[i].x = imgFile.pos[i].x; // -m_nScrollPosX;
		m_pointBorderVertex[i].y = imgFile.pos[i].y; // -m_nScrollPosY;
	}
	
	m_imgMasks.clear();
	m_imgMasks = imgFile.m_mask;

// 	int cnt = (int)imgFile.m_mask.size();
// 	Nuclear::FRECT fRct;
// 	for(int i=0; i<cnt; i++)
// 	{
// 		fRct.left = (float)imgFile.m_mask.at(i).left;
// 		fRct.right = (float)imgFile.m_mask.at(i).right;
// 		fRct.top = (float)imgFile.m_mask.at(i).top;
// 		fRct.bottom = (float)imgFile.m_mask.at(i).bottom;
// 		m_imgMasks.push_back(fRct);
// 	}
}

void CImageEditorView::OnDraw(CDC* pDC)
{
	CImageEditorDoc* pDoc = (CImageEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (pDoc == NULL)
		return;
}

void CImageEditorView::Render()
{
	CImageEditorDoc* pDoc = (CImageEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (pDoc == NULL)
		return;

	if( GetCapture() == this &&!m_bModifyMask )//鼠标操作...
			return;

	GetClientRect(&m_rectClient);
	if( m_pRenderer == NULL ) 
	{
		m_rectClientInit = m_rectClient;
		//m_rectClient.right-m_rectClient.left, m_rectClient.bottom-m_rectClient.top
		HRESULT hr = CreateRenderer(&m_pRenderer, m_hWnd, Nuclear::XDisplayMode(GetSystemMetrics(SM_CXSCREEN), GetSystemMetrics(SM_CYSCREEN)) , theApp.m_pFileIOManager);
		if( FAILED(hr) )
			return;

		Nuclear::CRECT sourceRect(0, 0, m_rectClient.right-m_rectClient.left, m_rectClient.bottom-m_rectClient.top);
		m_pRenderer->SetSourceRect(&sourceRect);
	}

	/*if(m_pSprite == NULL)
	{
		m_pSprite = new Nuclear::ComponentSprite(this, L"");
		m_pSprite->SetLocation(Nuclear::Location(300, 300));
		m_pSprite->SetComponent(Nuclear::XPSC_ALL, L"npc/npc1");
		m_pSprite->SetDefaultAction(L"run", false, 1.0f);
		m_pSprite->SetDirection(Nuclear::XPDIR_BOTTOM);
	}*/

	m_bPicTransFlag = false;
	std::vector<Nuclear::FPOINT> polygon;
	//m_imgMasks
	int cnt = (int)m_imgMasks.size();
	for(int i=0; i<cnt; i++)
	{
		polygon.push_back( m_imgMasks.at(i).ToFPOINT() *m_fZoomRate );
	}

	//FPOINT pt0 = m_pSprite->GetLocation().ToFPOINT();
	//FPOINT pt[4]; //左上、左下、右下、右上
	//pt[0] = pt0 +Nuclear::FPOINT(-43.0f, -100.0f);
	//pt[1] = pt0 +Nuclear::FPOINT(-43.0f, 10.0f);
	//pt[2] = pt0 +Nuclear::FPOINT(43.0f, 10.0f);
	//pt[3] = pt0 +Nuclear::FPOINT(43.0f, -100.0f);
	//int i=0;
	//for(i=0; i<4; i++)
	//{
	//	if( !pt[i].isInPolygon(polygon) )
	//		break;
	//}
	//if(i>=4)
	//	m_bPicTransFlag = true;
	
	DWORD bkColor = pDoc->GetBackGroundColor();
	m_pRenderer->Begin(bkColor);

	Nuclear::CPOINT ptTemp;
	//Nuclear::SortBaseRectType SpriteBase;
	Nuclear::SortBaseRectType ImgPicBase;

	//Nuclear::Location location = m_pSprite->GetLocation();
	//location.x = (int)(location.x /m_fZoomRate);
	//location.y = (int)(location.y /m_fZoomRate);
	//Nuclear::TransToDiamondRadix( (location.x-10), location.y, ptTemp);
	//SpriteBase.left = ptTemp.x;
	//SpriteBase.bottom = ptTemp.y;
	//Nuclear::TransToDiamondRadix( (location.x+10), location.y, ptTemp);
	//SpriteBase.right = ptTemp.x;
	//SpriteBase.top = ptTemp.y;

	PImg& imgFile = pDoc->GetImgObject();
	Nuclear::TransToDiamondRadix(imgFile.pos[0].x, imgFile.pos[0].y, ptTemp);
	ImgPicBase.left = ptTemp.x;
	ImgPicBase.bottom = ptTemp.y;
	Nuclear::TransToDiamondRadix(imgFile.pos[2].x, imgFile.pos[2].y, ptTemp);
	ImgPicBase.right = ptTemp.x;
	ImgPicBase.top = ptTemp.y;

	//bool bres = SpriteBase.IsBehind(ImgPicBase);

//	if(bres)
	//{//精灵被图片挡..
	//	if(m_bSetMaskpt || m_bAdjustMaskpt)
			//m_pSprite->Render(NULL);

		DrawPic();
	//}
	//else
	//{
	//	DrawPic();

	//	if(m_bSetMaskpt || m_bAdjustMaskpt)
	//		m_pSprite->Render(NULL);
	//}

	m_pRenderer->End();
}

void CImageEditorView::DrawPic()
{
	CImageEditorDoc* pDoc = (CImageEditorDoc*)GetDocument();

	GetClientRect(&m_rectClient);

	PImg& imgFile = pDoc->GetImgObject();
	std::vector<CRECT> vectorRct = imgFile.m_vectorRct;
	if(vectorRct.size() == 0)
		return;

	if( !pDoc->IsPicInit() )
	{
		for(std::vector<Nuclear::PictureHandle>::iterator it = m_vectorPicID.begin(), ie = m_vectorPicID.end(); it != ie; ++it)
		{
			m_pRenderer->FreePicture(*it);
		}
		m_vectorPicID.clear();

		//根据文件名来加载显示...
		CString fileName = pDoc->GetImageInfoView()->GetImgFileName();
		int nPos = fileName.ReverseFind('.');
		fileName = fileName.Left(nPos);

		int cnt = (int)vectorRct.size();
		for(int i=0; i<cnt; i++)
		{
		//	wchar_t fnbuffer[50];
		//	wsprintf(fnbuffer, L"_%d", i);
			CString str;
			str.Format(_T("_%d"), i);
			CString fileNameNoEx = fileName+str;
			PictureHandle handle = m_pRenderer->LoadPictureFromNativePath(std::wstring(fileNameNoEx));
			m_vectorPicID.push_back(handle);
		}

		pDoc->SetPicInit(true);
	}
	if( !pDoc->IsPicInit() ) return;

	float fZoomRate = pDoc->GetImageInfoView()->GetCurrentZoomRate();
	m_fZoomRate = fZoomRate;
	float fScrollPosX = m_nScrollPosX*fZoomRate;
	float fScrollPosY = m_nScrollPosY*fZoomRate;

	int cnt = (int)m_vectorPicID.size();
	DrawPictureParam param;
	for(int i=0; i<cnt; i++)
	{
		float fWidth = vectorRct.at(i).Width() *fZoomRate;
		float fHeight = vectorRct.at(i).Height() *fZoomRate;

		FRECT srcRct( (float)(m_rectClient.left), (float)(m_rectClient.top),
			(float)(m_rectClient.right), (float)(m_rectClient.bottom) );
		srcRct.left = (float)vectorRct.at(i).left *fZoomRate -fScrollPosX;
		srcRct.top = (float)vectorRct.at(i).top *fZoomRate -fScrollPosY;
		srcRct.right = fWidth > srcRct.Width() ? (srcRct.right-fScrollPosX):(srcRct.left+fWidth-fScrollPosX); 
		srcRct.bottom = fHeight>srcRct.Height() ? (srcRct.bottom-fScrollPosY):(srcRct.top+fHeight-fScrollPosY); 

		float du = (fScrollPosX-vectorRct.at(i).left)/(float)fWidth;
		float dv = (fScrollPosY-vectorRct.at(i).top)/(float)fHeight;
		du = du>0 ? du:0;
		dv = dv>0 ? dv:0;

		m_fcoefU = srcRct.Width()/(float)fWidth +du;
		m_fcoefV = srcRct.Height()/(float)fHeight +dv;
		m_fcoefU = m_fcoefU>1 ? 1: m_fcoefU;
		m_fcoefV = m_fcoefV>1 ? 1: m_fcoefV;
		FRECT srcUV(du, dv, m_fcoefU, m_fcoefV);
		param.handle = m_vectorPicID.at(i);
		param.pRect = &srcRct;
		param.pSrcrect = &srcUV;
		XPCOLOR color = 0xd0ffffff;
		if(m_bPicTransFlag)
		{
			param.colorCount = 1;
			param.pColor = &color;
		}

		m_pRenderer->DrawPicture(param);
	}

	float centerPointX0 = (float)imgFile.CenterPoint.x -fScrollPosX; //重心
	float centerPointY0 = (float)imgFile.CenterPoint.y -fScrollPosY;
	centerPointX0 *= fZoomRate;
	centerPointY0 *= fZoomRate;
	m_pRenderer->DrawLine(centerPointX0-8, centerPointY0-8, centerPointX0+8, centerPointY0+8, 0xffff0000);
	m_pRenderer->DrawLine(centerPointX0+8, centerPointY0-8, centerPointX0-8, centerPointY0+8, 0xffff0000);

	FPOINT p[4];
	p[0].x = (float)imgFile.pos[enum_left].x  -fScrollPosX;
	p[0].y = (float)imgFile.pos[enum_left].y  -fScrollPosY;
	p[1].x = (float)imgFile.pos[enum_bottom].x  -fScrollPosX;
	p[1].y = (float)imgFile.pos[enum_bottom].y  -fScrollPosY;
	p[2].x = (float)imgFile.pos[enum_right].x  -fScrollPosX;
	p[2].y = (float)imgFile.pos[enum_right].y  -fScrollPosY;
	p[3].x = (float)imgFile.pos[enum_up].x  -fScrollPosX;
	p[3].y = (float)imgFile.pos[enum_up].y  -fScrollPosY;

	float r = 3.0f;
	for(int i=0; i<4; i++)
	{
		int j = (i+1)>3 ? 0:(i+1);
		m_pRenderer->DrawLine(p[i].x*fZoomRate, p[i].y*fZoomRate, p[j].x*fZoomRate, p[j].y*fZoomRate, 0xff00ff00);

		m_pRenderer->DrawEllipse(p[i].x*fZoomRate, p[i].y*fZoomRate, r, r, 0xffff0000, Nuclear::XPFM_WIREFRAME);
	}

	cnt = (int)imgFile.m_mask.size();
	for(int i=0; i<cnt; i++)
	{
		int j = (i+1)==cnt ? 0:(i+1);
		Nuclear::FPOINT pt0 = m_imgMasks.at(i).ToFPOINT();
		Nuclear::FPOINT pt1 = m_imgMasks.at(j).ToFPOINT();
		pt0.x = pt0.x *fZoomRate;
		pt0.y = pt0.y *fZoomRate;
		pt1.x = pt1.x *fZoomRate;
		pt1.y = pt1.y *fZoomRate;
		m_pRenderer->DrawLine(pt0.x, pt0.y, pt1.x, pt1.y, 0xff00ffa0);

		m_pRenderer->DrawEllipse(pt0.x, pt0.y, r, r, 0xff0000ff, Nuclear::XPFM_WIREFRAME);
	}
}

// CImageEditorView 诊断

#ifdef _DEBUG
void CImageEditorView::AssertValid() const
{
	CView::AssertValid();
}

void CImageEditorView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}

#endif //_DEBUG

// CImageEditorView 消息处理程序

void CImageEditorView::OnLButtonDown(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	CImageEditorDoc* pDoc = (CImageEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	PImg& imgFile = pDoc->GetImgObject();

	CRect rect;
	GetClientRect(&rect);

	if(m_bSetMaskpt)
	{
		//记录点.. //todo
	//	SetCapture();

		CPOINT pt = Nuclear::CPOINT(point.x, point.y);
		pt.x = (int)(pt.x/m_fZoomRate);
		pt.y = (int)(pt.y/m_fZoomRate);

		imgFile.m_mask.push_back(pt);

		m_imgMasks.push_back(pt);
		pDoc->SetModifiedFlag(true);
	}
	else if(m_bAdjustMaskpt)
	{
		m_bModifyMask = false;

		m_nCurSelMaskpt = -1;
		int cnt = (int)m_imgMasks.size();
		for(int i=0; i<cnt; i++)
		{
			Nuclear::FPOINT fpt = m_imgMasks.at(i).ToFPOINT();
			CPoint pt;
			pt.x = (int)(fpt.x*m_fZoomRate +0.5f);
			pt.y = (int)(fpt.y*m_fZoomRate +0.5f);
			float dis = Computer2PointDis(point, pt);
			if(dis<3)
			{
				m_bModifyMask = true;
				m_nCurSelMaskpt = i;
				break;
			}
		}
		if(m_nCurSelMaskpt != -1)
		{
			Invalidate(true);
			SetCapture();
		}
/*
		//test点在多边形内..
		int tickStart = (int)GetMilliSeconds();
		std::vector<Nuclear::FPOINT> polygon;
		for(int i=0; i<cnt; i++)
		{
			Nuclear::FPOINT fpt = m_imgMasks.at(i).ToFPOINT();
			fpt *= m_fZoomRate;
			polygon.push_back(fpt);
		}
		Nuclear::FPOINT fpt = Nuclear::FPOINT((float)point.x, (float)point.y);

		bool bres = fpt.isInPolygon(polygon);
		int tickEnd = (int)GetMilliSeconds();

		CString str;
		str.Format(L"%d-----------", bres);
		CString strTime;
		strTime.Format(L"%d\n", tickEnd- tickStart);
		str += strTime;
		OutputDebugString(str);
		//
*/
	}
	else
	{
		m_bMoveBorder = false;
		m_nMovePointIndex = -1;
		for(int i=0; i<4; i++)
		{
			CPoint borderVertex = m_pointBorderVertex[i] - CPoint(m_nScrollPosX, m_nScrollPosY);
			borderVertex.x = (int)(borderVertex.x *m_fZoomRate);
			borderVertex.y = (int)(borderVertex.y *m_fZoomRate);

			float dis = Computer2PointDis(point, borderVertex);
			if(dis<3)
			{
				m_nMovePointIndex = i;
				m_bMoveBorder = true;
			}
		}

		if( rect.PtInRect(point) && (!m_bIsPlay) )
		{ 
			m_bDrawBorder = false;
			SetCapture();
			m_initialPoint = point;

			CPoint pt = point;

			for(int i=0; i<4; i++)
			{
				if(i<2)
				{
					m_ptFrom[i] = pt;
					m_ptTo[i] = pt;
				}
				m_fPosx[i] = (float)point.x /m_fZoomRate;
				m_fPosy[i] = (float)point.y /m_fZoomRate;
			}
		}
	}

	CView::OnLButtonDown(nFlags, point);
}

CPoint CImageEditorView::ZoomPoint(CPoint pt, float fZoomRate)
{
	pt.x = (int)(pt.x*fZoomRate);
	pt.y = (int)(pt.y*fZoomRate);
	return pt;
}

void CImageEditorView::OnMouseMove(UINT nFlags, CPoint point)
{
	CImageEditorDoc* pDoc = (CImageEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (pDoc == NULL)
		return;

	PImg& imgFile = pDoc->GetImgObject();

	float fZoomRate = m_fZoomRate;//pDoc->GetImageInfoView()->GetCurrentZoomRate();
//	m_fZoomRate = fZoomRate;
	CPoint scllPos(m_nScrollPosX, m_nScrollPosY);

	bool bModifyMask = false;
	int nSelPointIndex = -1; //选中了哪个点... 

	if(m_bSetMaskpt)
	{
	}
	else if(m_bAdjustMaskpt)
	{
		int cnt = (int)m_imgMasks.size();
		for(int i=0; i<cnt; i++)
		{
			Nuclear::FPOINT fpt = m_imgMasks.at(i).ToFPOINT();
			CPoint pt;
			pt.x = (int)(fpt.x*m_fZoomRate +0.5f);
			pt.y = (int)(fpt.y*m_fZoomRate +0.5f);
			float dis = Computer2PointDis(point, pt);
			if(dis<3)
			{
				bModifyMask = true;
				nSelPointIndex = i;
				break;
			}
		}

		if(bModifyMask)
			::SetCursor(AfxGetApp()->LoadStandardCursor(IDC_SIZEALL));
		else
		{ //不用处理..
		}
	}
	else
	{
		for(int i=0; i<4; i++)
		{
			CPoint borderVertex = m_pointBorderVertex[i] - scllPos;
			borderVertex.x = (int)(borderVertex.x *fZoomRate);
			borderVertex.y = (int)(borderVertex.y *fZoomRate);

			float dis = Computer2PointDis(point, borderVertex);
			if(dis<3)
			{
				::SetCursor(AfxGetApp()->LoadStandardCursor(IDC_SIZEALL));
				break;
			}
		}
	}

	if( (GetCapture()==this) && (nFlags==MK_LBUTTON) )
	{
		CDC *pDC = GetDC();

		if(m_bSetMaskpt)
		{//不用处理..
		}
		else if(m_bAdjustMaskpt)
		{
			if(m_nCurSelMaskpt != -1)
			{
				if(m_bModifyMask)
				{
					float dx = point.x - m_imgMasks.at(m_nCurSelMaskpt).x *m_fZoomRate;
					float dy = point.y - m_imgMasks.at(m_nCurSelMaskpt).y *m_fZoomRate;

					m_imgMasks.at(m_nCurSelMaskpt).x += (int)dx;
					m_imgMasks.at(m_nCurSelMaskpt).y += (int)dy;
					//绘制橡皮筋线..这种情况允许每帧render可以代替橡皮筋线...
				}
				else
				{
					//不用处理..
				}
			}
		}
		else
		{
			if(!m_bMoveBorder)
			{
				float dis = Computer2PointDis(m_initialPoint, point);
				if(dis>3.0f)
				{
					m_bDrawBorder = true;

					CPoint pt0 = m_initialPoint;
					CPoint pt1 = point;
					pt0.x = (int)(pt0.x/m_fZoomRate);
					pt0.y = (int)(pt0.y/m_fZoomRate);

					pt1.x = (int)(pt1.x/m_fZoomRate);
					pt1.y = (int)(pt1.y/m_fZoomRate);

					ComputerImgFilePos(pt0, pt1);

					CPoint pfr0 = ZoomPoint(m_ptFrom[0], m_fZoomRate);
					InvertLine(pDC, m_ptFrom[0], ZoomPoint(m_ptTo[0],m_fZoomRate) );//
					CPoint pt = CPoint((int)m_fPosx[1], (int)m_fPosy[1]);
					InvertLine(pDC, m_ptFrom[0], ZoomPoint(pt, m_fZoomRate));

					InvertLine(pDC, m_ptFrom[0], ZoomPoint(m_ptTo[1], m_fZoomRate));
					pt = CPoint((int)m_fPosx[3], (int)m_fPosy[3]);
					InvertLine(pDC, m_ptFrom[0], ZoomPoint(pt, m_fZoomRate));

					InvertLine(pDC, m_ptFrom[1], ZoomPoint(m_ptTo[0], m_fZoomRate));
					pt = CPoint((int)m_fPosx[1], (int)m_fPosy[1]);
					InvertLine(pDC, point, ZoomPoint(pt, m_fZoomRate));

					InvertLine(pDC, m_ptFrom[1], ZoomPoint(m_ptTo[1], m_fZoomRate));
					pt = CPoint((int)m_fPosx[3], (int)m_fPosy[3]);
					InvertLine(pDC, point, ZoomPoint(pt, m_fZoomRate));

					m_ptTo[0] = CPoint((int)m_fPosx[1], (int)m_fPosy[1]);
					m_ptTo[1] = CPoint((int)m_fPosx[3], (int)m_fPosy[3]);
					m_ptFrom[1] = point;
				}
			}
			else
			{
				int ia = m_nMovePointIndex;
				int ib = (ia+1)>3 ? 0:(ia+1);
				int ic = (ia+2)>3 ? (ia-2):(ia+2);
				int id = (ia-1)<0 ? 3:(ia-1);

				CPoint pt0 = CPoint(imgFile.pos[ic].x, imgFile.pos[ic].y)-scllPos;

				CPoint temp_point = point;
				temp_point.x = (int)(temp_point.x/m_fZoomRate);
				temp_point.y = (int)(temp_point.y/m_fZoomRate);

				ComputerImgFilePos(pt0, temp_point); //真数据计算

				pt0.x = (int)(pt0.x*m_fZoomRate);
				pt0.y = (int)(pt0.y*m_fZoomRate);
				CPoint pt1 = CPoint(imgFile.pos[ib].x, imgFile.pos[ib].y)-scllPos;
				pt1.x = (int)(pt1.x*m_fZoomRate);
				pt1.y = (int)(pt1.y*m_fZoomRate);
				InvertLine(pDC, pt0, pt1);

				pt1 = CPoint(imgFile.pos[id].x, imgFile.pos[id].y)-scllPos;
				pt1.x = (int)(pt1.x*m_fZoomRate);
				pt1.y = (int)(pt1.y*m_fZoomRate);
				InvertLine(pDC, pt0, pt1);

				pt1 = CPoint(imgFile.pos[ia].x, imgFile.pos[ia].y)-scllPos;
				pt1.x = (int)(pt1.x*m_fZoomRate);
				pt1.y = (int)(pt1.y*m_fZoomRate);
				CPoint pt2 = CPoint(imgFile.pos[ib].x, imgFile.pos[ib].y)-scllPos;
				pt2.x = (int)(pt2.x*m_fZoomRate);
				pt2.y = (int)(pt2.y*m_fZoomRate);
				InvertLine(pDC, pt1, pt2);

				pt1 = CPoint(imgFile.pos[ia].x, imgFile.pos[ia].y)-scllPos;
				pt1.x = (int)(pt1.x*m_fZoomRate);
				pt1.y = (int)(pt1.y*m_fZoomRate);
				pt2 = CPoint(imgFile.pos[id].x, imgFile.pos[id].y)-scllPos;
				pt2.x = (int)(pt2.x*m_fZoomRate);
				pt2.y = (int)(pt2.y*m_fZoomRate);
				InvertLine(pDC, pt1, pt2);

				SetImgFilePos(imgFile); //

				pt0 = CPoint(imgFile.pos[ic].x, imgFile.pos[ic].y)-scllPos;
				pt0.x = (int)(pt0.x*m_fZoomRate);
				pt0.y = (int)(pt0.y*m_fZoomRate);
				pt1 = CPoint(imgFile.pos[ib].x, imgFile.pos[ib].y)-scllPos;
				pt1.x = (int)(pt1.x*m_fZoomRate);
				pt1.y = (int)(pt1.y*m_fZoomRate);
				InvertLine(pDC, pt0, pt1);

				pt1 = CPoint(imgFile.pos[id].x, imgFile.pos[id].y)-scllPos;
				pt1.x = (int)(pt1.x*m_fZoomRate);
				pt1.y = (int)(pt1.y*m_fZoomRate);
				InvertLine(pDC, pt0, pt1);

				pt1 = CPoint(imgFile.pos[ia].x, imgFile.pos[ia].y)-scllPos;
				pt1.x = (int)(pt1.x*m_fZoomRate);
				pt1.y = (int)(pt1.y*m_fZoomRate);
				pt2 = CPoint(imgFile.pos[ib].x, imgFile.pos[ib].y)-scllPos;
				pt2.x = (int)(pt2.x*m_fZoomRate);
				pt2.y = (int)(pt2.y*m_fZoomRate);
				InvertLine(pDC, pt1, pt2);

				pt1 = CPoint(imgFile.pos[ia].x, imgFile.pos[ia].y)-scllPos;
				pt1.x = (int)(pt1.x*m_fZoomRate);
				pt1.y = (int)(pt1.y*m_fZoomRate);
				pt2 = CPoint(imgFile.pos[id].x, imgFile.pos[id].y)-scllPos;
				pt2.x = (int)(pt2.x*m_fZoomRate);
				pt2.y = (int)(pt2.y*m_fZoomRate);
				InvertLine(pDC, pt1, pt2);;	
			}
		}
		ReleaseDC(pDC);	
	}

	CView::OnMouseMove(nFlags, point);
}

void CImageEditorView::OnLButtonUp(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	CImageEditorDoc* pDoc = (CImageEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	if( GetCapture()==this )
	{
		PImg& imgFile = pDoc->GetImgObject();

		if(m_bSetMaskpt)
		{
		}
		else if(m_bAdjustMaskpt)
		{
			if(m_nCurSelMaskpt == -1)
				return CView::OnLButtonUp(nFlags, point);
			//移动到point
			if(m_bModifyMask)
			{
				float dx = point.x - m_imgMasks.at(m_nCurSelMaskpt).x *m_fZoomRate;
				float dy = point.y - m_imgMasks.at(m_nCurSelMaskpt).y *m_fZoomRate;

				m_imgMasks.at(m_nCurSelMaskpt).x += (int)dx;
				m_imgMasks.at(m_nCurSelMaskpt).y += (int)dy;
			}
			else
			{ //不用处理..
			}
			//
			imgFile.m_mask.at(m_nCurSelMaskpt).x = m_imgMasks.at(m_nCurSelMaskpt).x;
			imgFile.m_mask.at(m_nCurSelMaskpt).y = m_imgMasks.at(m_nCurSelMaskpt).y;
			m_nCurSelMaskpt = -1;
		}
		else
		{
			if(!m_bMoveBorder)
			{
				if(!m_bDrawBorder)
				{
					imgFile.CenterPoint.x = (int)(point.x/m_fZoomRate) +m_nScrollPosX;
					imgFile.CenterPoint.y = (int)(point.y/m_fZoomRate) +m_nScrollPosY;
				}
				else
				{
					CPoint pt0 = m_initialPoint;
					CPoint pt1 = point;
					pt0.x = (int)(pt0.x/m_fZoomRate);
					pt0.y = (int)(pt0.y/m_fZoomRate);

					pt1.x = (int)(pt1.x/m_fZoomRate);
					pt1.y = (int)(pt1.y/m_fZoomRate);

					ComputerImgFilePos(pt0, pt1);
					SetImgFilePos(imgFile);
				}
			}
			else
			{
				SetImgFilePos(imgFile);
			}
		}

		pDoc->UpdateAllViews(NULL);
		pDoc->SetModifiedFlag(true);
		m_bDrawBorder = false;
		m_bMoveBorder = false;
		m_bModifyMask = false;

		ReleaseCapture();
	}

	CView::OnLButtonUp(nFlags, point);
}

void CImageEditorView::InvertLine(CDC* pDC, CPoint ptFrom, CPoint ptTo)
{
	int nOldMode = pDC->SetROP2(R2_NOT);
	pDC->MoveTo (ptFrom);
	pDC->LineTo (ptTo);
	pDC->SetROP2 (nOldMode);
}

float CImageEditorView::Computer2PointDis(CPoint pt1, CPoint pt2)
{
	float dis = powf((float)(pt1.x - pt2.x), 2.0f) + powf((float)(pt1.y - pt2.y), 2.0f);
	dis = sqrt(dis);
	return dis;
}

void CImageEditorView::ComputerImgFilePos(const CPoint& initialPoint, const CPoint& point)
{
	//俯视32度
	m_fPosx[0] = (float)initialPoint.x;
	m_fPosy[0] = (float)initialPoint.y;
	m_fPosx[2] = (float)point.x;
	m_fPosy[2] = (float)point.y;

	float dx = (float)( (point.x - initialPoint.x) );
	float dy = (float)( (point.y - initialPoint.y)/cos(58/57.3f) );
	float a = (dx+dy)/2.0f;
	float b = (dy-dx)/2.0f;
	m_fPosx[3] = a +initialPoint.x;
	m_fPosy[3] = a*cos(58/57.3f) +initialPoint.y;
	m_fPosx[1] = -b +initialPoint.x;
	m_fPosy[1] = b*cos(58/57.3f) +initialPoint.y;

	for(int i=0; i<4; i++)
	{
		int j = (i+1)>3 ? 0:(i+1);
		m_pointBorderCent[i].x = (int)(m_fPosx[i] + m_fPosx[j]) /2;
		m_pointBorderCent[i].y = (int)(m_fPosy[i] + m_fPosy[j]) /2;
	}
}

void CImageEditorView::SetImgFilePos(PImg& ImgFile)
{
	int IndexMAX_x = 0;
	int IndexMAX_y = 0;
	int IndexMIN_x = 0;
	int IndexMIN_y = 0;

	int i=0;

	float fmin_x = 1000000.0f;
	float fmax_x = -100.0f;
	float fmin_y = 1000000.0f;
	float fmax_y = -100.0f;

	for(i=0; i<4; i++)
	{
		if(m_fPosx[i]>fmax_x)
		{
			IndexMAX_x = i;
			fmax_x = m_fPosx[i];
		}
		if(m_fPosx[i]<fmin_x)
		{
			IndexMIN_x = i;
			fmin_x = m_fPosx[i];
		}

		if(m_fPosy[i]>fmax_y)
		{
			IndexMAX_y = i;
			fmax_y = m_fPosy[i];
		}
		if(m_fPosy[i]<fmin_y)
		{
			IndexMIN_y = i;
			fmin_y = m_fPosy[i];
		}
	}

	ImgFile.pos[enum_left].x = (int)(m_fPosx[IndexMIN_x]) +m_nScrollPosX;
	ImgFile.pos[enum_left].y = (int)(m_fPosy[IndexMIN_x]) +m_nScrollPosY;

	ImgFile.pos[enum_right].x = (int)(m_fPosx[IndexMAX_x]) +m_nScrollPosX;
	ImgFile.pos[enum_right].y = (int)(m_fPosy[IndexMAX_x]) +m_nScrollPosY;

	ImgFile.pos[enum_bottom].x = (int)(m_fPosx[IndexMAX_y]) +m_nScrollPosX;
	ImgFile.pos[enum_bottom].y = (int)(m_fPosy[IndexMAX_y]) +m_nScrollPosY;

	ImgFile.pos[enum_up].x = (int)(m_fPosx[IndexMIN_y]) +m_nScrollPosX;
	ImgFile.pos[enum_up].y = (int)(m_fPosy[IndexMIN_y]) +m_nScrollPosY;

	for(i=0; i<4; i++)
	{
		m_pointBorderVertex[i].x = ImgFile.pos[i].x;
		m_pointBorderVertex[i].y = ImgFile.pos[i].y;
	}
}

void CImageEditorView::OnSize(UINT nType, int cx, int cy)
{
	CView::OnSize(nType, cx, cy);

	// TODO: 在此处添加消息处理程序代码

	GetClientRect(&m_rectClient);

	if (m_pRenderer)
	{
		Nuclear::CRECT sourceRect(0, 0, m_rectClient.right-m_rectClient.left, m_rectClient.bottom-m_rectClient.top);
		m_pRenderer->SetSourceRect(&sourceRect);
	}

	CRect rct(m_rectClient);
	m_nScrollPosX = m_nScrollPosY = 0; 	
	m_nVPageSizeX = m_nVPageSizeY = 0;
	SCROLLINFO si;
	si.cbSize = sizeof(SCROLLINFO);
	si.fMask = SIF_ALL; 
	si.nMin = 0;
	si.nMax = 0;
	si.nPage = 0;
	si.nPos = 0;
	if( m_nViewHeight>rct.Height() )
	{
		si.nMax = m_nViewHeight-1;
		si.nPage = rct.Height();
		m_nVPageSizeY = rct.Height();
		m_nScrollPosY = min (m_nScrollPosY, m_nViewHeight - m_nVPageSizeY - 1);
	}
	SetScrollInfo(SB_VERT, &si, TRUE); 	

	si.nMax = 0;
	si.nPage = 0;
	if( m_nViewWidth>rct.Width() )
	{
		si.nMax = m_nViewWidth-1;
		si.nPage = rct.Width();
		m_nVPageSizeX = rct.Width();
		m_nScrollPosX = min (m_nScrollPosX, m_nViewWidth - m_nVPageSizeX - 1);
	}
	SetScrollInfo(SB_HORZ, &si, TRUE); 
}

void CImageEditorView::OnSizing(UINT fwSide, LPRECT pRect)
{
	CView::OnSizing(fwSide, pRect);

	// TODO: 在此处添加消息处理程序代码

	if (m_pRenderer)
	{
		GetClientRect(&m_rectClient);
		Nuclear::CRECT sourceRect(0, 0, m_rectClient.right-m_rectClient.left, m_rectClient.bottom-m_rectClient.top);
		m_pRenderer->SetSourceRect(&sourceRect);
	}
}

void CImageEditorView::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	int nDelta;
	int nMaxPos = m_nViewWidth - m_nVPageSizeX;

	switch(nSBCode)
	{
	case SB_LINERIGHT:
		if(m_nScrollPosX >= nMaxPos)
			return;
		nDelta = min(max(m_nViewWidth/20, 5), m_nViewWidth-m_nScrollPosX);
		break;
	case SB_LINELEFT:
		if(m_nScrollPosX <= 0)
			return;
		nDelta = -min(max(m_nViewWidth/20, 5), m_nScrollPosX);
		break;
	case SB_PAGELEFT:
		if(m_nScrollPosX >= nMaxPos)
			return;
		nDelta = min(max(m_nViewWidth/10, 5), m_nViewWidth-m_nScrollPosX);
		break;
	case SB_THUMBTRACK:
	case SB_THUMBPOSITION:
		nDelta = (int)nPos - m_nScrollPosX;
		break;
	case SB_PAGERIGHT :
		if(m_nScrollPosX <= 0)
			return;
		nDelta = -min(max(m_nViewWidth/10, 5), m_nScrollPosX);
		break;
	default:
		return;
	}
	m_nScrollPosX += nDelta;
	SetScrollPos(SB_HORZ, m_nScrollPosX, TRUE);
	ScrollWindow(-nDelta, 0);

	CView::OnHScroll(nSBCode, nPos, pScrollBar);
}

void CImageEditorView::OnVScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	int nDelta;
	int nMaxPos = m_nViewHeight - m_nVPageSizeY;

	switch(nSBCode)
	{
	case SB_LINEDOWN:	
		if(m_nScrollPosY >= nMaxPos)
			return;
		nDelta = min(max(nMaxPos/20, 5),nMaxPos-m_nScrollPosY);
		break;
	case SB_LINEUP:
		if(m_nScrollPosY <= 0)
			return;
		nDelta = -min(max(nMaxPos/20, 5), m_nScrollPosY);
		break;
	case SB_PAGEDOWN:
		if(m_nScrollPosY >= nMaxPos)
			return;
		nDelta = min(max(nMaxPos/10, 5), nMaxPos-m_nScrollPosY);
		break;
	case SB_THUMBTRACK:
	case SB_THUMBPOSITION:
		nDelta = (int)nPos - m_nScrollPosY;
		break;
	case SB_PAGEUP:
		if(m_nScrollPosY <= 0)
			return;
		nDelta = -min(max(nMaxPos/10, 5), m_nScrollPosY);
		break;
	default:
		return;
	}
	m_nScrollPosY += nDelta;
	SetScrollPos(SB_VERT, m_nScrollPosY, TRUE);
	ScrollWindow(0, -nDelta);

	CView::OnVScroll(nSBCode, nPos, pScrollBar);
}

void CImageEditorView::SetViewSize(int iViewWidth, int iViewHeight)
{
	m_nViewWidth0 = iViewWidth; 
	m_nViewHeight0 = iViewHeight;

	m_nViewWidth = 0; //iViewWidth //*m_fZoomRate
	m_nViewHeight = 0; //iViewHeight
	m_nScrollPosX = 0;
	m_nScrollPosY = 0;

	SendMessage(WM_SIZE);
	Invalidate(false);
}

void CImageEditorView::ReSetViewSize()
{
	m_nViewWidth = m_nViewWidth0; //*m_fZoomRate
	m_nViewHeight = m_nViewHeight0;
	m_nScrollPosX = 0;
	m_nScrollPosY = 0;

	SendMessage(WM_SIZE);
	Invalidate(false);
}

void CImageEditorView::SetViewSize0()
{
	m_nViewWidth = 0; //iViewWidth //*m_fZoomRate
	m_nViewHeight = 0; //iViewHeight
	m_nScrollPosX = 0;
	m_nScrollPosY = 0;

	SendMessage(WM_SIZE);
	Invalidate(false);
}

void CImageEditorView::OnClose()
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	CView::OnClose();
}

void CImageEditorView::OnRButtonDown(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	if(!m_bAdjustMaskpt)
		return;
	BOOL bSelMask = false;
	m_nRButtonSelpt = -1;
	int cnt = (int)m_imgMasks.size();
	for(int i=0; i<cnt; i++)
	{
		Nuclear::FPOINT fpt = m_imgMasks.at(i).ToFPOINT();
		CPoint pt;
		pt.x = (int)(fpt.x*m_fZoomRate);
		pt.y = (int)(fpt.y*m_fZoomRate);
		float dis = Computer2PointDis(point, pt);
		if(dis<3)
		{
			bSelMask = true;
			m_nRButtonSelpt = i;
			break;
		}
	}
	if(bSelMask)
	{
		CPoint pt;
		::GetCursorPos(&pt);

		CMenu menu;
		menu.LoadMenu(IDR_MENU2);
		CMenu* pMenu=menu.GetSubMenu(0);
		pMenu->TrackPopupMenu(TPM_LEFTALIGN | TPM_RIGHTBUTTON, pt.x, pt.y, this, NULL);
	}

	CView::OnRButtonDown(nFlags, point);
}

void CImageEditorView::OnDelmask()
{
	CImageEditorDoc* pDoc = (CImageEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (pDoc == NULL)
		return;

	PImg& imgFile = pDoc->GetImgObject();

	std::vector<Nuclear::CPOINT>::iterator it = m_imgMasks.begin();
	m_imgMasks.erase(it+m_nRButtonSelpt);

	std::vector<Nuclear::CPOINT>::iterator itbe = imgFile.m_mask.begin();
	imgFile.m_mask.erase(itbe+m_nRButtonSelpt);
}

void CImageEditorView::OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	switch (nChar)
	{
	case VK_DOWN:
	case VK_UP:
	case VK_LEFT:
	case VK_RIGHT:
	//	SetFocus();
		SetActiveWindow();
		break;
	}

	//int dx = 0;
	//int dy = 0;
	//int nZoom = 1;
	//if( GetKeyState(VK_CONTROL) < 0 )
	//	nZoom = 5;
	//switch (nChar)
	//{
	//case VK_DOWN:
	//	dy = 1;
	//	break;
	//case VK_UP:
	//	dy = -1;
	//	break;
	//case VK_LEFT:
	//	dx = -1;
	//	break;
	//case VK_RIGHT:
	//	dx = 1;
	//	break;
	//}
	///Nuclear::Location pos = m_pSprite->GetLocation();
	//pos += Nuclear::CPOINT(dx *nZoom, dy *nZoom);
	//m_pSprite->SetLocation(pos);

	__super::OnKeyDown(nChar, nRepCnt, nFlags);
}

void CImageEditorView::OnInitialUpdate()
{
	__super::OnInitialUpdate();

	// TODO: 在此添加专用代码和/或调用基类
	SetTimer(1, 30, NULL);
}


void CImageEditorView::SetMaskRctFlag(BOOL bSetMaskRct)
{
	m_bSetMaskpt = bSetMaskRct; 
	if(m_bSetMaskpt)
		SetFocus(); //这个时候保证能按方向键控制精灵移动
/*
	//计算旋转角度+缩放比例
	CImageEditorDoc* pDoc = (CImageEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	PImg imgFile = pDoc->GetImgObject();

	float leftx = (float)(imgFile.pos[enum_left].x);
	float lefty = (float)(imgFile.pos[enum_left].y);

	float rightx = (float)(imgFile.pos[enum_right].x);
	float righty = (float)(imgFile.pos[enum_right].y);

	float bottomx = (float)(imgFile.pos[enum_bottom].x);
	float bottomy = (float)(imgFile.pos[enum_bottom].y);

	float topx = (float)(imgFile.pos[enum_up].x);
	float topy = (float)(imgFile.pos[enum_up].y);

	float centx = (leftx+rightx)/2.0f;
	float centy = (topy+bottomy)/2.0f;

	float pos1x = (topx+rightx)/2.0f;
	float pos1y = (topy+righty)/2.0f;
	float pos3x = (bottomx+rightx)/2.0f;
	float pos3y = (bottomy+righty)/2.0f;
	float pos5x = (bottomx+leftx)/2.0f;
	float pos5y = (bottomy+lefty)/2.0f;
	float pos7x = (topx+leftx)/2.0f;
	float pos7y = (topy+lefty)/2.0f;

	XPVECTOR2 v0 = XPVECTOR2(topx-centx, topy-centy);
	XPVECTOR2 v1 = XPVECTOR2(pos1x-centx, pos1y-centy);
	XPVECTOR2 v2 = XPVECTOR2(rightx-centx, righty-centy);
	XPVECTOR2 v3 = XPVECTOR2(pos3x-centx, pos3y-centy);

	XPVECTOR2 v4 = XPVECTOR2(bottomx-centx, bottomy-centy);
	XPVECTOR2 v5 = XPVECTOR2(pos5x-centx, pos5y-centy);
	XPVECTOR2 v6 = XPVECTOR2(leftx-centx, lefty-centy);
	XPVECTOR2 v7 = XPVECTOR2(pos7x-centx, pos7y-centy);

	float dis2 = VectorLength(v2);

	float dis0 = VectorLength(v0)/dis2;
	float dis1 = VectorLength(v1)/dis2;
	float dis3 = VectorLength(v3)/dis2;
	float dis4 = VectorLength(v4)/dis2;
	float dis5 = VectorLength(v5)/dis2;
	float dis6 = VectorLength(v6)/dis2;
	float dis7 = VectorLength(v7)/dis2;

	float angle0 = Computer2VectorAngle(v0, v2);
	float angle1 = Computer2VectorAngle(v1, v2);
	float angle2 = Computer2VectorAngle(v2, v2);
	float angle3 = Computer2VectorAngle(v3, v2);
	float angle4 = Computer2VectorAngle(v4, v2);
	float angle5 = Computer2VectorAngle(v5, v2);
	float angle6 = Computer2VectorAngle(v6, v2);
	float angle7 = Computer2VectorAngle(v7, v2);

	XPVECTOR2 v20 = XPVECTOR2(topx-rightx, topy-righty);
	XPVECTOR2 v24 = XPVECTOR2(bottomx-rightx, bottomy-righty);
	float fangle = Computer2VectorAngle(v20, v24); //验证角度的..
	int test = 0;
	//向量长度
	// 		inline float VectorLength(const XPVECTOR2& v)
	// 		{
	// 			return sqrt(v.x * v.x + v.y * v.y);
	// 		}
	// 
	// 		//两向量的夹角
	// 		inline float Computer2VectorAngle(const XPVECTOR2& v1, const XPVECTOR2& v2)
	// 		{
	// 			float fcosangle = DotProduct(v1, v2)/(VectorLength(v1)*VectorLength(v2));
	// 			return acos(fcosangle);
	// 		}


	//

	*/
}
void CImageEditorView::OnTimer(UINT_PTR nIDEvent)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	if(nIDEvent==1)
	{
		Render();
	}

	__super::OnTimer(nIDEvent);
}
