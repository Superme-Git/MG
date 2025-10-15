// DistortionEditorView.cpp : CDistortionEditorView 类的实现
//

#include "stdafx.h"
#include "DistortionEditor.h"

#include <Mmsystem.h>
#include "DistortionEditorDoc.h"
#include "DistortionEditorView.h"

#include "..\engine\common\timer.h"
#include "..\engine\common\util.h"
#include "..\engine\common\pfsutil.h"
#include "..\engine\engine\systemresourcemanager.h"
#include "..\engine\map\pimg.h"


#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CDistortionEditorView

IMPLEMENT_DYNCREATE(CDistortionEditorView, CView)

BEGIN_MESSAGE_MAP(CDistortionEditorView, CView)
	// 标准打印命令
	ON_COMMAND(ID_FILE_PRINT, &CView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_DIRECT, &CView::OnFilePrint)
	ON_COMMAND(ID_FILE_PRINT_PREVIEW, &CView::OnFilePrintPreview)
	ON_WM_SIZE()
	ON_WM_SIZING()
	ON_WM_RBUTTONDOWN()
	ON_WM_LBUTTONDOWN()
	ON_WM_LBUTTONUP()
	ON_WM_MOUSEMOVE()
	ON_WM_LBUTTONDBLCLK()
	ON_WM_TIMER()
	ON_WM_KEYDOWN()
	ON_COMMAND(ID_MENU_DelLight, &CDistortionEditorView::OnMenuDellight)
END_MESSAGE_MAP()

// CDistortionEditorView 构造/析构

CDistortionEditorView::CDistortionEditorView()
{
	// TODO: 在此处添加构造代码
	m_pRender = NULL;
	m_SystemResoureManager = NULL;
	m_dwBKcolor = 0xffa0a0a0;
	m_fDistortionPos = 0.0f;

	m_bLoadFile = false;
	m_distortionObjEffect = NULL;

	m_initialPoint = CPoint(0, 0);
 	m_CenterPoint = FPOINT(0, 0);
 	for(int i=0; i<4; i++)
 	{
 		m_pos[i] = FPOINT(0, 0);
 	}
}

CDistortionEditorView::~CDistortionEditorView()
{
	//free纹理..
	if(m_distortionObjEffect != NULL)
	{
		delete m_distortionObjEffect;
		m_distortionObjEffect = NULL;
	}
	DestroyRenderer(m_pRender);

	if(m_SystemResoureManager != NULL)
	{
		delete m_SystemResoureManager;
		m_SystemResoureManager = NULL;
	}
}

BOOL CDistortionEditorView::PreCreateWindow(CREATESTRUCT& cs)
{
	// TODO: 在此处通过修改
	//  CREATESTRUCT cs 来修改窗口类或样式

	return CView::PreCreateWindow(cs);
}

// CDistortionEditorView 绘制

void CDistortionEditorView::OnDraw(CDC* pDC)
{
	CDistortionEditorDoc* pDoc = (CDistortionEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;
//	Render(); //目的是绘重心底座..	

	//
	// TODO: 在此处为本机数据添加绘制代码
}

void CDistortionEditorView::Render()
{
	CDistortionEditorDoc* pDoc = (CDistortionEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	if( GetCapture()==this )
		return; //鼠标操作...

	if( m_pRender == NULL ) 
	{
		CRect rectClient;
		GetClientRect(&rectClient);

		HRESULT hr = CreateRenderer(&m_pRender, m_hWnd, Nuclear::XDisplayMode(GetSystemMetrics(SM_CXSCREEN), GetSystemMetrics(SM_CYSCREEN)), 
							theApp.m_pFileIOManager, Nuclear::XPCRF_VERTICALSYNC);
		if( FAILED(hr) )
			return;

		Nuclear::CRECT sourceRect(0, 0, rectClient.right-rectClient.left, rectClient.bottom-rectClient.top);
		m_pRender->SetSourceRect(&sourceRect);

		if(m_SystemResoureManager != NULL)
		{
			delete m_SystemResoureManager;
			m_SystemResoureManager = NULL;
		}
		m_SystemResoureManager = new SystemResourceManager(this);
	}

	Nuclear::PDistortionObject distortionObj = pDoc->GetDistortionObject();
	if(m_bLoadFile)
	{
		std::wstring strFilePath = distortionObj.m_distortionObj.strImgFileName;

		std::wstring TempKey = strFilePath;
		std::transform( TempKey.begin(), TempKey.end(), TempKey.begin(), ::towlower);
		bool bres = PFS::CEnv::PFSFileNameToNativeFileName(TempKey, strFilePath);

		CString strImgPath = strFilePath.c_str();
		AddPicToDis(strImgPath, false);

		if(distortionObj.m_bLightFlag)
		{
			std::wstring strLightFile = distortionObj.m_distortionObj.strLightFile;
			TempKey = strLightFile;
			std::transform( TempKey.begin(), TempKey.end(), TempKey.begin(), ::towlower);
			bres = PFS::CEnv::PFSFileNameToNativeFileName(TempKey, strLightFile);

			CString strLightPath = strLightFile.c_str();
			AddPicToDis(strLightPath, true);
		}

		m_bLoadFile = false;
	}

	float fCurTime = (float)timeGetTime();
	float timeDelta = (fCurTime - m_fLastTime)*0.001f;	
	m_fLastTime = fCurTime;
	if( timeDelta>0.2f )
		timeDelta = 0.0f;

	if(m_distortionObjEffect == NULL)
		return;

	m_distortionObjEffect->Update(timeDelta);
	CRECT ImgRct;
	ImgRct.left = (int)m_frctImgPos.left;
	ImgRct.right = (int)m_frctImgPos.right;
	ImgRct.top = (int)m_frctImgPos.top;
	ImgRct.bottom = (int)m_frctImgPos.bottom;
	m_distortionObjEffect->setPos(ImgRct);
//	m_distortionObjEffect->setZoom(1.0f);

	m_pRender->Begin(m_dwBKcolor);

	m_distortionObjEffect->Render(NULL);

	float centerPointX0 = m_CenterPoint.x +m_frctImgPos.left; //重心
	float centerPointY0 = m_CenterPoint.y +m_frctImgPos.top;
	m_pRender->DrawLine(centerPointX0-8, centerPointY0-8, centerPointX0+8, centerPointY0+8, 0xffff0000);
	m_pRender->DrawLine(centerPointX0+8, centerPointY0-8, centerPointX0-8, centerPointY0+8, 0xffff0000);

	FPOINT p[4];
	p[0].x = m_pos[enum_left].x  +m_frctImgPos.left;
	p[0].y = m_pos[enum_left].y  +m_frctImgPos.top;
	p[1].x = m_pos[enum_bottom].x  +m_frctImgPos.left;
	p[1].y = m_pos[enum_bottom].y  +m_frctImgPos.top;
	p[2].x = m_pos[enum_right].x  +m_frctImgPos.left;
	p[2].y = m_pos[enum_right].y  +m_frctImgPos.top;
	p[3].x = m_pos[enum_up].x  +m_frctImgPos.left;
	p[3].y = m_pos[enum_up].y  +m_frctImgPos.top;

	float r = 3.0f;
	for(int i=0; i<4; i++)
	{
		int j = (i+1)>3 ? 0:(i+1);
		m_pRender->DrawLine(p[i].x, p[i].y, p[j].x, p[j].y, 0xff00ff00);

	//	m_pRender->DrawEllipse(p[i].x, p[i].y, r, r, 0xffff0000, Nuclear::XPFM_WIREFRAME);
	}


	m_pRender->End();

/*
	float fPos = Update(distortionObj, timeDelta); //timeDelta 秒

	m_pRender->Begin(m_dwBKcolor);

	int cnt = (int)m_picHandleInfoVetor.size();

	for(int i=0; i<cnt; i++)
	{
		Nuclear::FRECT rct;
		rct = m_picHandleInfoVetor.at(i).m_rct.ToFRECT();
		rct += Nuclear::FPOINT(m_frctImgPos.left, m_frctImgPos.top);

		int nRowIndex = (int)(rct.top -m_frctImgPos.top)/Nuclear::XPGRIDSIZE;
		int nColIndex = (int)(rct.left -m_frctImgPos.left)/Nuclear::XPGRIDSIZE;

		Nuclear::DrawPictureParam param;
		param.handle = m_picHandleInfoVetor.at(i).m_handlePic;
		param.pRect = &rct;
		param.pMatrix = NULL;
		Nuclear::XPDistortionParam distortionParam;
		distortionParam.nType = distortionObj.m_distortionObj.nType;
		distortionParam.fAmplitudes = distortionObj.m_distortionObj.fAmplitudes;
		distortionParam.fCycNum = distortionObj.m_distortionObj.fCycNum;
		distortionParam.fPos = m_fDistortionPos;

		distortionParam.nRowIndex = nRowIndex;
		distortionParam.nColIndex = nColIndex;

		param.pDistortionParam = &distortionParam;
		m_pRender->DrawPicture(param);
	}

	m_pRender->End();
*/
}

float CDistortionEditorView::Update(const Nuclear::PDistortionObject& distortionObj, float timeDelta) //秒
{
	m_fDistortionPos += timeDelta*2*Nuclear::XP_PI/distortionObj.m_distortionObj.fSpeed;
	int count = (int)(m_fDistortionPos/(2.0f*Nuclear::XP_PI));
	m_fDistortionPos -= count*2.0f*Nuclear::XP_PI;//------控制函数移动

	return m_fDistortionPos;
}

// CDistortionEditorView 打印

BOOL CDistortionEditorView::OnPreparePrinting(CPrintInfo* pInfo)
{
	// 默认准备
	return DoPreparePrinting(pInfo);
}

void CDistortionEditorView::OnBeginPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	// TODO: 添加额外的打印前进行的初始化过程
}

void CDistortionEditorView::OnEndPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	// TODO: 添加打印后进行的清除过程
}

// CDistortionEditorView 诊断

#ifdef _DEBUG
void CDistortionEditorView::AssertValid() const
{
	CView::AssertValid();
}

void CDistortionEditorView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}

#endif //_DEBUG


// CDistortionEditorView 消息处理程序

void CDistortionEditorView::OnSize(UINT nType, int cx, int cy)
{
	CView::OnSize(nType, cx, cy);

	// TODO: 在此处添加消息处理程序代码

	if (m_pRender)
	{
		CRect rectClient;
		GetClientRect(&rectClient);
		Nuclear::CRECT sourceRect(0, 0, rectClient.right-rectClient.left, rectClient.bottom-rectClient.top);
		m_pRender->SetSourceRect(&sourceRect);
	}
}

void CDistortionEditorView::OnSizing(UINT fwSide, LPRECT pRect)
{
	CView::OnSizing(fwSide, pRect);

	// TODO: 在此处添加消息处理程序代码

	if (m_pRender)
	{
		CRect rectClient;
		GetClientRect(&rectClient);
		Nuclear::CRECT sourceRect(0, 0, rectClient.right-rectClient.left, rectClient.bottom-rectClient.top);
		m_pRender->SetSourceRect(&sourceRect);
	}
}

void CDistortionEditorView::OnRButtonDown(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	CDistortionEditorDoc* pDoc = (CDistortionEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return;
	Nuclear::PDistortionObject& distortionObj = pDoc->GetDistortionObject();

	if( distortionObj.m_distortionObj.strLightFile != L"" )
	{
		CPoint pt;
		::GetCursorPos(&pt);

		CMenu menu;
		menu.LoadMenu(IDR_MENU1);
		menu.EnableMenuItem(ID_MENU_DelLight, MF_ENABLED | MF_BYCOMMAND);

		CMenu* pMenu = menu.GetSubMenu(0);
		pMenu->TrackPopupMenu(TPM_LEFTALIGN | TPM_RIGHTBUTTON, pt.x, pt.y, this, NULL);
	}

	CView::OnRButtonDown(nFlags, point);
}

void CDistortionEditorView::OnLButtonDblClk(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	CView::OnLButtonDblClk(nFlags, point);
}

float CDistortionEditorView::Computer2PointDis(CPoint pt1, CPoint pt2)
{
	float dis = powf((float)(pt1.x - pt2.x), 2.0f) + powf((float)(pt1.y - pt2.y), 2.0f);
	dis = sqrt(dis);
	return dis;
}

void CDistortionEditorView::OnInitialUpdate()
{
	SetTimer(2, 30, NULL);
}

void CDistortionEditorView::OnTimer(UINT_PTR nIDEvent)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	if(nIDEvent==1)
	{
		Nuclear::TimerOB::OnSourceTimer(); //计算fps
	}
	if(nIDEvent==2)
	{
		Render();
	}

	CView::OnTimer(nIDEvent);
}

void CDistortionEditorView::OnUpdate(CView* /*pSender*/, LPARAM /*lHint*/, CObject* /*pHint*/)
{
}

void CDistortionEditorView::OnLButtonDown(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	m_initialPoint = point;

	CView::OnLButtonDown(nFlags, point);
}

void CDistortionEditorView::OnMouseMove(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	if(nFlags == MK_LBUTTON)
	{
		int dx = point.x - m_initialPoint.x;
		int dy = point.y - m_initialPoint.y;
		m_initialPoint = point;
		m_frctImgPos += Nuclear::FPOINT(float(dx), float(dy));
	}

	CView::OnMouseMove(nFlags, point);
}

void CDistortionEditorView::OnLButtonUp(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	CView::OnLButtonUp(nFlags, point);
}

void CDistortionEditorView::InvertLine(CDC* pDC, CPoint ptFrom, CPoint ptTo)
{
	int nOldMode = pDC->SetROP2(R2_NOT);
	pDC->MoveTo (ptFrom);
	pDC->LineTo (ptTo);
	pDC->SetROP2 (nOldMode);
}

void CDistortionEditorView::AddPicToDis(CString strFilePath, bool bLightFlag)
{
	CDistortionEditorDoc* pDoc = (CDistortionEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return;
	Nuclear::PDistortionObject& distortionObj = pDoc->GetDistortionObject();

	std::wstring strKey;//elments下包含子目录名的文件名
	int len = strFilePath.GetLength();
	int npos = strFilePath.Find(L"\\map\\elements\\");
	strKey = strFilePath.Right(len-npos);
	std::wstring TempKey = strKey;
	bool bres = PFS::CEnv::NativeFileNameToPFSFileName(TempKey, strKey);

	std::transform(strKey.begin(), strKey.end(), strKey.begin(), ::towlower);

	FILE *f = NULL;
	_tfopen_s(&f, strFilePath, _T("rb"));
	if( f == NULL )
	{
		if(bLightFlag)
		{
			distortionObj.m_bLightFlag = false;
		}
		return;
	}

	Nuclear::PImg imgObject;
	BOOL ret = imgObject.Load(f) ? TRUE:FALSE;
	fclose(f);
	if(!ret)
	{
		if(bLightFlag)
		{
			distortionObj.m_bLightFlag = false;
		}
		return;
	}

	int nPos = strFilePath.ReverseFind('.');
	CString filePathNoEX = strFilePath.Left(nPos);

	int cnt = (int)imgObject.m_vectorRct.size();
	Nuclear::PicResrc* pPicResrc;

	if(!bLightFlag)
	{
	//	distortionObj.m_distortionObj.strLightFile = L""; //这个不应该要 20011-8-5
		distortionObj.m_distortionObj.strImgFileName = strKey;

		m_CenterPoint = imgObject.CenterPoint.ToFPOINT();
		for(int i=0; i<4; i++)
			m_pos[i] = imgObject.pos[i].ToFPOINT();

		if(m_distortionObjEffect != NULL)
		{
		//	m_distortionObjEffect->ResetMainImgInfo();
			delete m_distortionObjEffect;
			m_distortionObjEffect = NULL;
		}
		DistortionEffectParam effectParam;
		effectParam.nType = distortionObj.m_distortionObj.nType; //类型..
		effectParam.fCycNum = distortionObj.m_distortionObj.fCycNum; //控制正余弦的函数周期
		effectParam.fAmplitudes = distortionObj.m_distortionObj.fAmplitudes;
		effectParam.fSpeed = distortionObj.m_distortionObj.fSpeed; //移动速度...移动 2PI 需要的时间 1.0f---------100.0f 单位：秒

		effectParam.fPos = 0.0f; //根据这个值来确定函数位置.. y = a*sin(cyc*x + fPos)
		//每次update-----fPos += fDetaTime*2*PI/fSpeed; --------fpos -= 2*PI;
		m_distortionObjEffect = new CToolsDistort(this, effectParam);

		//读img文件初始化
		for(int i=0; i<cnt; i++)
		{
			CString str;
			str.Format(_T("_%d"), i);
			CString fileNameNoEx = filePathNoEX + str;
			pPicResrc = new Nuclear::PicResrc();
			pPicResrc->uri = fileNameNoEx;
			pPicResrc->handle = m_pRender->LoadPictureFromNativePath(pPicResrc->uri);
			m_distortionObjEffect->PushAnPic(pPicResrc, imgObject.m_vectorRct[i]);
		}
		m_frctImgPos = Nuclear::FRECT(0.0f, 0.0f, (float)imgObject.m_nFileWidth, (float)imgObject.m_nFileHeight);
	}
	else
	{
		if(m_distortionObjEffect == NULL)
		{
			MessageBox(L"先添加主节点再添加灯光:-D");
		}
		else
		{
			int nWidth = (int)(m_frctImgPos.Width());
			int nheight = (int)(m_frctImgPos.Height());
			if(nWidth!=imgObject.m_nFileWidth || nheight!=imgObject.m_nFileHeight)
			{
				MessageBox(L"灯光节点与主节点不一样大小:-D");
				return;
			}

			distortionObj.m_distortionObj.strLightFile = strKey;

			m_distortionObjEffect->ResetLightImgInfo();
			//读img文件初始化
			for(int i=0; i<cnt; i++)
			{
				CString str;
				str.Format(_T("_%d"), i);
				CString fileNameNoEx = filePathNoEX + str;
				pPicResrc = new Nuclear::PicResrc();
				pPicResrc->uri = fileNameNoEx;
				pPicResrc->handle = m_pRender->LoadPictureFromNativePath(pPicResrc->uri);
				m_distortionObjEffect->PushLightPic(pPicResrc, imgObject.m_vectorRct[i]);
			}
		}
	}

	pDoc->UpdateAllViews(NULL);
}

void CDistortionEditorView::OnMenuDellight()
{
	CDistortionEditorDoc* pDoc = (CDistortionEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return;
	Nuclear::PDistortionObject& distortionObj = pDoc->GetDistortionObject();

	if(m_distortionObjEffect==NULL)
		return;

	distortionObj.m_distortionObj.strLightFile = L"";
	distortionObj.m_bLightFlag = false;
	m_distortionObjEffect->ResetLightImgInfo();

	pDoc->UpdateAllViews(NULL);
}
