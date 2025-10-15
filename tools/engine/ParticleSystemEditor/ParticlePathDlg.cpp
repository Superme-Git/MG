// ParticlePathDlg.cpp : 实现文件
//

#include "stdafx.h"
#include "ParticleSystemEditor.h"

#include "ParticlePathDlg.h"
#include "TextInputDlg.h"
#include <math.h>	  
#include "..\engine\common\xpmaths.h"
#include "..\engine\particlesystem\CommonFuncs.h"

// CParticlePathDlg 对话框

IMPLEMENT_DYNAMIC(CParticlePathDlg, CDialog)

CParticlePathDlg::CParticlePathDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CParticlePathDlg::IDD, pParent)
	, m_bAddPoint(TRUE)
	, m_bMovePoint(FALSE)
	, m_bCurveAdjust(FALSE)
	, m_bVectorCharPath(FALSE)
	, m_bMoveMap(FALSE)
	, m_fPathZoom(1.0f)
	, m_bShowGrid(TRUE)
	, m_fPathSpin(0)
	, m_bCurveSample(FALSE)
{
	m_nBezierPtIndex = -1; //0--pre_controlPoint  1--next_controlPoint
	m_curSelPointIndex = -1;
	m_nCurSelCharIndex = -1;
	m_nMouseMoveCnt = 0;

	m_hAddCursor = NULL;
	m_hMoveCursor = NULL;
	m_hRotateCursor = NULL;
	m_bVectorCharPath = false;

	m_bModifyPath = false;
	m_strPathFileName = L"";
	m_bPslCoor = false;
	m_bBKchange = false;
	m_nPathMirrorType = 0;
	m_bhasKeyPoint = FALSE;
}

CParticlePathDlg::~CParticlePathDlg()
{
	DestroyCursor(m_hAddCursor);
	DestroyCursor(m_hMoveCursor);
	DestroyCursor(m_hRotateCursor);
}

void CParticlePathDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Check(pDX, IDC_CHECK_ADDPOINT, m_bAddPoint);
	DDX_Check(pDX, IDC_CHECK_MOVEPOINT, m_bMovePoint);
	DDX_Check(pDX, IDC_CHECK_CurveAdjust, m_bCurveAdjust);
	DDX_Check(pDX, IDC_CHECK_LoadVectorChar, m_bVectorCharPath);
	DDX_Check(pDX, IDC_CHECK_MOVEMAP, m_bMoveMap);
	DDX_Control(pDX, IDC_SLIDER_PATHZOOM, m_sliderPathZoom);

	DDX_Text(pDX, IDC_EDIT_PathZoom, m_fPathZoom);
	DDV_MinMaxFloat(pDX, m_fPathZoom, 0.5f, 2.0f);
	DDX_Check(pDX, IDC_CHECK_ShowGrid, m_bShowGrid);
	DDX_Control(pDX, IDC_SLIDER_PATHSPIN, m_sliderPathSpin);
	DDX_Text(pDX, IDC_EDIT_PATHSPIN, m_fPathSpin);
	DDV_MinMaxFloat(pDX, m_fPathSpin, -360.0f, 360.0f);
	DDX_Control(pDX, IDC_COMBO_PATHMIRRORTYPE, m_comboPathMirrorType);
	DDX_Check(pDX, IDC_CHECK_CurveSample, m_bCurveSample);
}


BEGIN_MESSAGE_MAP(CParticlePathDlg, CDialog)
	ON_BN_CLICKED(IDOK, &CParticlePathDlg::OnBnClickedOk)
	ON_BN_CLICKED(IDCANCEL, &CParticlePathDlg::OnBnClickedCancel)
	ON_WM_PAINT()
	ON_WM_CLOSE()
	ON_WM_LBUTTONDOWN()
	ON_WM_LBUTTONUP()
	ON_WM_MOUSEMOVE()
	ON_WM_RBUTTONDOWN()
	ON_WM_RBUTTONUP()
	ON_COMMAND(ID_NENU_DELPOINT, &CParticlePathDlg::OnNenuDelpoint)
	ON_COMMAND(ID_NENU_CURVE, &CParticlePathDlg::OnNenuCurve)
	ON_COMMAND(ID_NENU_LINK, &CParticlePathDlg::OnNenuLink)
	ON_BN_CLICKED(IDC_CHECK_ADDPOINT, &CParticlePathDlg::OnBnClickedCheckAddpoint)
	ON_BN_CLICKED(IDC_CHECK_MOVEPOINT, &CParticlePathDlg::OnBnClickedCheckMovepoint)
	ON_BN_CLICKED(IDC_BUTTON_RESET, &CParticlePathDlg::OnBnClickedButtonReset)
	ON_BN_CLICKED(IDC_BUTTON_SAVEPATH, &CParticlePathDlg::OnBnClickedButtonSavepath)
	ON_BN_CLICKED(IDC_BUTTON_LOADPATH, &CParticlePathDlg::OnBnClickedButtonLoadpath)
	ON_BN_CLICKED(IDC_CHECK_CurveAdjust, &CParticlePathDlg::OnBnClickedCheckCurveadjust)
	ON_BN_CLICKED(IDC_BUTTON_SetVectorChar, &CParticlePathDlg::OnBnClickedButtonSetvectorchar)
	ON_BN_CLICKED(IDC_CHECK_LoadVectorChar, &CParticlePathDlg::OnBnClickedCheckLoadvectorchar)
	ON_BN_CLICKED(IDC_BUTTON_SetBkMap, &CParticlePathDlg::OnBnClickedButtonSetbkmap)
	ON_BN_CLICKED(IDC_CHECK_MOVEMAP, &CParticlePathDlg::OnBnClickedCheckMovemap)

	ON_CONTROL_RANGE(EN_KILLFOCUS, IDC_EDIT_PathZoom, IDC_EDIT_PATHSPIN, &CParticlePathDlg::OnEnKillfocusEdit)

	ON_WM_HSCROLL()
	ON_BN_CLICKED(IDC_CHECK_ShowGrid, &CParticlePathDlg::OnBnClickedCheckShowgrid)
	ON_CBN_SELCHANGE(IDC_COMBO_PATHMIRRORTYPE, &CParticlePathDlg::OnCbnSelchangeComboPathmirrortype)
	ON_BN_CLICKED(IDC_CHECK_CurveSample, &CParticlePathDlg::OnBnClickedCheckCurveSample)
	ON_COMMAND(ID_MENU_KEYPOINT, &CParticlePathDlg::OnMenuKeypoint)
END_MESSAGE_MAP()


BOOL CParticlePathDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// TODO:  在此添加额外的初始化
	GetDlgItem(IDC_PICTURE_DRAWPATHRCT)->GetWindowRect(&m_rctDrawPath);
	ScreenToClient(&m_rctDrawPath);

	m_sliderPathZoom.SetRange(50, 200); // *0.01f
	m_sliderPathZoom.SetPos(100);

	m_sliderPathSpin.SetRange(-360, 360, true);
	m_sliderPathSpin.SetPos(0);

//	GetClientRect(&m_rctDrawPath);
//	m_rctDrawPath.DeflateRect(10, 10, 10, 50);

//	m_vectorPathPoint.clear();
	m_curSelPointIndex = -1;
	m_nCurSelCharIndex = -1;

	m_hAddCursor = AfxGetApp()->LoadCursor(IDC_CURSOR_INS);
	m_hMoveCursor = AfxGetApp()->LoadCursor(IDC_CURSOR_MOVE);
	m_hRotateCursor = AfxGetApp()->LoadCursor(IDC_CURSOR_ROTATE);

	((CButton*)GetDlgItem(IDC_BUTTON_SetVectorChar))->EnableWindow(false);

	std::wstring sysFilePath = theApp.GetResPath() + L"effect\\particle\\texture\\bkground.bmp";

	//读配置文件..
	wchar_t workdir[MAX_PATH];
	::GetCurrentDirectory(MAX_PATH, workdir);
	CString filePath = workdir;
	filePath += L"\\ParticleSystemEditor.ini";
	CString strSection = L"ALL";
	CString strSectionKey = L"BKpicture";
	TCHAR value[256];
	std::wstring outFile;
	GetPrivateProfileString(strSection, strSectionKey, sysFilePath.c_str(), value, 256, filePath);

	HBITMAP hBitmap;
	CString bkPicFilePath = value;
	int pos = bkPicFilePath.ReverseFind('.');
	int length = bkPicFilePath.GetLength();
	CString exName = bkPicFilePath.Right(length-pos-1);
	CString strBMP = L"bmp";
	CString strJPG = L"jpg";

	if(exName.CompareNoCase(strBMP) == 0)
		hBitmap = (HBITMAP)::LoadImage(::AfxGetInstanceHandle(), bkPicFilePath,
										IMAGE_BITMAP, 0, 0, LR_LOADFROMFILE|LR_CREATEDIBSECTION);
	else
		hBitmap = LoadJPG(bkPicFilePath);

	if( hBitmap != NULL )
	{
		m_bitmap.Detach();
		m_bitmap.Attach(hBitmap);
	}
	else
	{
		CString str = L"导入背景图失败!";
		str = str+bkPicFilePath;
		MessageBox(str, L"提示", MB_OK);
	}

	m_rctMapPos = m_rctDrawPath;

	CString str = m_strSysPath + L"path\\";		 
	SetWindowText(str + m_strPathFileName);

	m_comboPathMirrorType.ResetContent();
	m_comboPathMirrorType.InsertString(0, L"不镜像");
	m_comboPathMirrorType.InsertString(1, L"左右镜像");
	m_comboPathMirrorType.InsertString(2, L"上下镜像");	
	m_comboPathMirrorType.InsertString(3, L"中心对称");	
	m_comboPathMirrorType.SetCurSel(m_nPathMirrorType);

	return TRUE;  // return TRUE unless you set the focus to a control
	// 异常: OCX 属性页应返回 FALSE
}


// CParticlePathDlg 消息处理程序

void CParticlePathDlg::SetSysPath(CString sysPath)
{
	m_strSysPath = sysPath;
}

/*
void CParticlePathDlg::SetPathPoint(std::vector<Nuclear::PATHPOINT>& vectorPathPoint)
{
	m_curSelPointIndex = -1;
	m_nCurSelCharIndex = -1;
	m_nBezierPtIndex = -1;
	m_vectorPathPoint.clear();
	m_vectorPathPoint = vectorPathPoint;

	int cnt = m_vectorPathPoint.size();
//	int width = m_rctDrawPath.Width();
//	int height = m_rctDrawPath.Height();
//	float coefx = (float)width/m_nBackBufferWidth;
//	float coefy = (float)height/m_nBackBufferHeight;
	for(int i=0; i<cnt; i++)
	{
		m_vectorPathPoint[i].pt.x = vectorPathPoint[i].pt.x;
		m_vectorPathPoint[i].pt.y = vectorPathPoint[i].pt.y;
		m_vectorPathPoint[i].pre_ControlPt.x = vectorPathPoint[i].pre_ControlPt.x;
		m_vectorPathPoint[i].pre_ControlPt.y = vectorPathPoint[i].pre_ControlPt.y;
		m_vectorPathPoint[i].next_ControlPt.x = vectorPathPoint[i].next_ControlPt.x;
		m_vectorPathPoint[i].next_ControlPt.y = vectorPathPoint[i].next_ControlPt.y;
	}
}
*/

void CParticlePathDlg::OnBnClickedOk()
{
	// TODO: 在此添加控件通知处理程序代码
	if(m_bModifyPath)
	{
		//请先保存路径...m_strPathFileName
		CString strInfo = L"请先保存当前编辑好的路径";
		MessageBox(strInfo, L"提示", MB_OK);
		OnBnClickedButtonSavepath();
		return;
	}
	OnOK();
}

void CParticlePathDlg::OnBnClickedCancel()
{
	// TODO: 在此添加控件通知处理程序代码
	OnCancel();
}

void CParticlePathDlg::Draw(CDC *pDC)
{
	int nWidth = m_rctDrawPath.Width();
	int nHeight = m_rctDrawPath.Height();
	int centx = m_rctDrawPath.left + nWidth/2 ;
	int centy = m_rctDrawPath.top + nHeight/2 ;

	CBrush brush;
	BOOL bres = brush.CreateSolidBrush(RGB(0, 0, 0));
	pDC->FillRect(&m_rctDrawPath, &brush);
	brush.DeleteObject();

	//绘制背景图片
	DrawBKPicture(pDC);
	//

	HPEN redPen, greenPen, whitePen, grayPen, bluePen, OldPen;
	redPen = CreatePen(PS_SOLID, 1, RGB(255, 0, 0));
	greenPen = CreatePen(PS_SOLID, 1, RGB(0, 255, 0));//创建画笔
	whitePen = CreatePen(PS_SOLID, 1, RGB(255, 255, 255));
	grayPen = CreatePen(PS_SOLID, 1, RGB(120, 100, 100));
	bluePen = CreatePen(PS_SOLID, 3, RGB(0, 0, 255));
	OldPen = (HPEN)SelectObject(pDC->m_hDC, redPen);//选中画笔，选中新画笔时返回旧画笔

	int arrowSize = 8;
	pDC->MoveTo(m_rctDrawPath.left, centy);
	pDC->LineTo(m_rctDrawPath.right, centy);
	pDC->LineTo(m_rctDrawPath.right-arrowSize, centy-arrowSize);
	pDC->MoveTo(m_rctDrawPath.right, centy);
	pDC->LineTo(m_rctDrawPath.right-arrowSize, centy+arrowSize);

	pDC->MoveTo(centx, m_rctDrawPath.bottom);
	pDC->LineTo(centx, m_rctDrawPath.top);
	pDC->LineTo(centx-arrowSize, m_rctDrawPath.top+arrowSize);
	pDC->MoveTo(centx, m_rctDrawPath.top);
	pDC->LineTo(centx+arrowSize, m_rctDrawPath.top+arrowSize);

	pDC->SelectObject(grayPen);
	int i, j;
	int dx = 20;
	int dy = 20;

	if(m_bShowGrid)
	{
		int cntx = nWidth/dx;
		int cnty = nHeight/dy;
		for(i=0; i<cntx; i++)
		{
			pDC->MoveTo(m_rctDrawPath.left +i*dx, m_rctDrawPath.top);
			pDC->LineTo(m_rctDrawPath.left +i*dx, m_rctDrawPath.bottom);
		}
		for(i=0; i<cnty; i++)
		{
			pDC->MoveTo(m_rctDrawPath.left, m_rctDrawPath.top +i*dy);
			pDC->LineTo(m_rctDrawPath.right, m_rctDrawPath.top +i*dy);
		}
	}

	//////////////////////////////以下///////////////画路径曲线
	if(m_bPslCoor)
	{//就是为了那个预览路径加的...
		m_sliderPathZoom.SetPos((int)(m_fPathZoom*100));
		m_sliderPathSpin.SetPos((int)(m_fPathSpin));
		m_comboPathMirrorType.SetCurSel(m_nPathMirrorType);

		m_bPslCoor = false;

		int x0 = (m_rctDrawPath.left + m_rctDrawPath.right)/2;
		int y0 = (m_rctDrawPath.top + m_rctDrawPath.bottom)/2;
		int cnt = (int)m_vectorPathPoint.size();

		for(int i=0; i<cnt; i++)
		{
			m_vectorPathPoint[i].pt.x += x0;
			m_vectorPathPoint[i].pt.y = -m_vectorPathPoint[i].pt.y + y0;
			m_vectorPathPoint[i].pre_ControlPt.x += x0;
			m_vectorPathPoint[i].pre_ControlPt.y = -m_vectorPathPoint[i].pre_ControlPt.y + y0;
			m_vectorPathPoint[i].next_ControlPt.x += x0;
			m_vectorPathPoint[i].next_ControlPt.y = -m_vectorPathPoint[i].next_ControlPt.y + y0;
		}
	}
	if(m_bVectorCharPath)
	{
		//画矢量字符路径...2008--10-21--上午
		int ncharNum = m_vectorStrContour.size();
		for(i=0; i<ncharNum; i++)
		{
			SelectObject(pDC->m_hDC, whitePen);
			int x0 = m_vectorCharStartPt[i].x -m_vectorCharCenterPt[i].x;
			int y0 = m_vectorCharStartPt[i].y -m_vectorCharCenterPt[i].y;

			POINT centPt;
			centPt.x = m_vectorCharStartPt[i].x ;
			centPt.y = m_vectorCharStartPt[i].y ;

			CharContor Chcontor = m_vectorStrContour[i];
			int num = Chcontor.size();
			for(j=0; j<num; j++)
			{
				Nuclear::Contour contor = Chcontor[j];
				int ptNum = contor.vectorPoint.size();
				for(int a=0; a<ptNum; a++)
				{
					CPoint pt;
					pt.x = (int)( x0+contor.vectorPoint[a].x ); //
					pt.y = (int)( y0+contor.vectorPoint[a].y ); //

					RotatePoint(pt, centPt, m_fVectorFontAngle[i]);

					if(a==0)
						pDC->MoveTo(pt);
					pDC->LineTo(pt);
				}
			}

			if(i==m_nCurSelCharIndex)
				SelectObject(pDC->m_hDC, redPen);
			else
				SelectObject(pDC->m_hDC, greenPen);

			DrawCharContourRct(pDC, i);
		}
	}
	else
	{
		//生成曲线
		SelectObject(pDC->m_hDC, greenPen);
		int cnt = m_vectorPathPoint.size();
		m_vectorCurvePoint.clear();
		for(i=0; (i<cnt-1) && (cnt>1); i++)
		{
			CreatePathCurve(i);
		}
		//绘制曲线
		int nCurveNum = m_vectorCurvePoint.size();
		for(i=0; i<nCurveNum; i++)// 比m_vectorPathPoint少1
		{
			CurvePoint curvePt = m_vectorCurvePoint[i];
			pDC->MoveTo(curvePt[0]);
			int num = m_vectorCurvePoint[i].size();
			for(j=0; j<num; j++)
			{
				pDC->LineTo(curvePt[j]);
			}
		}

		if(cnt>1)
		{
			//标记曲线的首-尾
			int r = 4;
			CPoint pt = m_vectorCurvePoint[0][0];
			pDC->Rectangle(pt.x-r, pt.y-r, pt.x+r, pt.y+r);
			r = 6;
			pt = m_vectorCurvePoint[i-1][j-1];
			pDC->Rectangle(pt.x-r, pt.y-r, pt.x+r, pt.y+r);
		}

		for(int i=0; i<cnt; i++)
		{//画路径点和bezier杆
			DrawPathPoint(pDC, m_vectorPathPoint[i]);
		}

		if(m_curSelPointIndex != -1)
		{
			SelectObject(pDC->m_hDC, redPen); //当前选中点..
			int r = 4;
			CPoint pt = m_vectorPathPoint[m_curSelPointIndex].pt;
			TransCoor(pt, m_fPathZoom, m_fPathSpin, m_nPathMirrorType);
			CRect rct(pt.x-r, pt.y-r, pt.x+r, pt.y+r);				
			pDC->Ellipse(&rct);
		}

		//标记KeyPoint
		for(int i=0; i<cnt; i++)
		{
			BOOL bKeypt = m_vectorPathPoint[i].bKeyPoint;
			if(bKeypt)
			{
				SelectObject(pDC->m_hDC, bluePen);
				int r = 3;
				CPoint pt = m_vectorPathPoint[i].pt;
				TransCoor(pt, m_fPathZoom, m_fPathSpin, m_nPathMirrorType);
				CRect rct(pt.x-r, pt.y-r, pt.x+r, pt.y+r);				
				pDC->Ellipse(&rct);
			}
		}
	}

	SelectObject(pDC->m_hDC, OldPen);//恢复新的画笔，返回了Pen

	DeleteObject(redPen);
	DeleteObject(greenPen);
	DeleteObject(whitePen);
	DeleteObject(grayPen);
	DeleteObject(bluePen);
}

void CParticlePathDlg::OnPaint()
{
	CPaintDC dc(this); // device context for painting
	// TODO: 在此处添加消息处理程序代码
	// 1.绘制基本线框和坐标等信息.. --- m_rctDrawPath
	// 2.绘制路径数据结构里的信息...

	CBitmap	ShowBitmap;
	ShowBitmap.CreateCompatibleBitmap(&dc, m_rctDrawPath.Width(), m_rctDrawPath.Height());
	if(!ShowBitmap.m_hObject)
		return;

	CDC memDC;
	memDC.CreateCompatibleDC(&dc);
	if(!memDC.m_hDC)
		return;
	CBitmap *pOldBitmap	= memDC.SelectObject(&ShowBitmap);

	Draw(&memDC); //先绘制在这里..这样就不会闪烁了

	StretchBlt(dc.GetSafeHdc(),			// handle to destination device context
		m_rctDrawPath.left,		// x-coordinate of upper-left corner of dest. rectangle
		m_rctDrawPath.top,		// y-coordinate of upper-left corner of dest. rectangle
		m_rctDrawPath.Width(),   // width of destination rectangle
		m_rctDrawPath.Height(),  // height of destination rectangle
		memDC.GetSafeHdc(), // handle to source device context
		m_rctDrawPath.left,		// x-coordinate of upper-left corner of source rectangle
		m_rctDrawPath.top,		// y-coordinate of upper-left corner of source rectangle
		m_rctDrawPath.Width(),   // width of source rectangle
		m_rctDrawPath.Height(),  // height of source rectangle
		SRCCOPY);			// raster operation code

	memDC.SelectObject(pOldBitmap);
	memDC.DeleteDC( );
	ShowBitmap.DeleteObject();
	// 不为绘图消息调用 CDialog::OnPaint()
}

void CParticlePathDlg::DrawBKPicture(CDC* dc)
{
	CDC CDCompatible;
	CDCompatible.CreateCompatibleDC(dc);//创建兼容DC
	CDCompatible.SelectObject(&m_bitmap);

	if( NULL==HBITMAP(m_bitmap) )
		return;

	BITMAP bmp;
	m_bitmap.GetBitmap(&bmp); //bmp.bmWidth, bmp.bmHeight

	int nWidth = m_rctDrawPath.Width();
	int nHeight = m_rctDrawPath.Height();

	int left = m_rctMapPos.left;
	int top = m_rctMapPos.top;
	int cx = m_rctMapPos.Width();
	int cy = m_rctMapPos.Height();

	dc->StretchBlt(m_rctDrawPath.left, m_rctDrawPath.top, nWidth, nHeight, &CDCompatible, left, top, cx, cy, SRCCOPY);
	CDCompatible.DeleteDC();
}

void CParticlePathDlg::DrawCharContourRct(CDC* dc, int nCharIndex)
{
	CPoint pt1 = m_vectorCharContourRct[nCharIndex].pt[0];
	CPoint pt2 = m_vectorCharContourRct[nCharIndex].pt[1];
	CPoint pt3 = m_vectorCharContourRct[nCharIndex].pt[2];
	CPoint pt4 = m_vectorCharContourRct[nCharIndex].pt[3];
	CPoint centPt = m_vectorCharStartPt[nCharIndex];
	int r = 3;
	CRect rct(centPt.x-r, centPt.y-r, centPt.x+r, centPt.y+r);
	dc->Ellipse(&rct);

	float fAngle = m_fVectorFontAngle[nCharIndex];
	RotatePoint(pt1, centPt, fAngle);
	RotatePoint(pt2, centPt, fAngle);
	RotatePoint(pt3, centPt, fAngle);
	RotatePoint(pt4, centPt, fAngle);

	dc->MoveTo(pt1);
	dc->LineTo(pt2);
	dc->LineTo(pt3);
	dc->LineTo(pt4);
	dc->LineTo(pt1);

	rct = CRect(pt1.x-r, pt1.y-r, pt1.x+r, pt1.y+r);
	dc->Ellipse(&rct);
	rct = CRect(pt2.x-r, pt2.y-r, pt2.x+r, pt2.y+r);
	dc->Ellipse(&rct);
	rct = CRect(pt3.x-r, pt3.y-r, pt3.x+r, pt3.y+r);
	dc->Ellipse(&rct);
	rct = CRect(pt4.x-r, pt4.y-r, pt4.x+r, pt4.y+r);
	dc->Ellipse(&rct);
}

void CParticlePathDlg::DrawCharContourRct(CDC* dc, POINT centPt, int rx, int ry)
{
	int r = 3;
	CRect rct(centPt.x-r, centPt.y-r, centPt.x+r, centPt.y+r);
	dc->Ellipse(&rct);

	CPoint pt1(centPt.x-rx, centPt.y-ry);
	CPoint pt2(centPt.x-rx, centPt.y+ry);
	CPoint pt3(centPt.x+rx, centPt.y+ry);
	CPoint pt4(centPt.x+rx, centPt.y-ry);

	//四个点围绕矩形中心点centPt旋转m_nFontAngle度
	RotatePoint(pt1, centPt, m_fFontAngle);
	RotatePoint(pt2, centPt, m_fFontAngle);
	RotatePoint(pt3, centPt, m_fFontAngle);
	RotatePoint(pt4, centPt, m_fFontAngle);

	dc->MoveTo(pt1);
	dc->LineTo(pt2);
	dc->LineTo(pt3);
	dc->LineTo(pt4);
	dc->LineTo(pt1);

	rct = CRect(pt1.x-r, pt1.y-r, pt1.x+r, pt1.y+r);
	dc->Ellipse(&rct);
	rct = CRect(pt2.x-r, pt2.y-r, pt2.x+r, pt2.y+r);
	dc->Ellipse(&rct);
	rct = CRect(pt3.x-r, pt3.y-r, pt3.x+r, pt3.y+r);
	dc->Ellipse(&rct);
	rct = CRect(pt4.x-r, pt4.y-r, pt4.x+r, pt4.y+r);
	dc->Ellipse(&rct);
}

void CParticlePathDlg::DrawPathPoint(CDC* dc, Nuclear::PATHPOINT PathPoint)
{
	int r = 3;

	CPoint pt = PathPoint.pt;

	TransCoor(pt, m_fPathZoom, m_fPathSpin, m_nPathMirrorType);
	CRect rct(pt.x-r, pt.y-r, pt.x+r, pt.y+r);
	dc->Ellipse(&rct);

	//绘制初始贝赛尔杆
	if(PathPoint.m_bCurve)
	{
		HPEN purplePen, oldPen;
		purplePen = CreatePen(PS_SOLID, 1, RGB(255, 0, 255));
		oldPen = (HPEN)SelectObject(dc->m_hDC, purplePen);//选中画笔，选中新画笔时返回旧画笔

		CPoint pre_ControlPt = PathPoint.pre_ControlPt;
		CPoint next_ControlPt = PathPoint.next_ControlPt;
		TransCoor(pre_ControlPt, m_fPathZoom, m_fPathSpin, m_nPathMirrorType);
		TransCoor(next_ControlPt, m_fPathZoom, m_fPathSpin, m_nPathMirrorType);

		dc->MoveTo(pt);
		dc->LineTo(pre_ControlPt);
		dc->MoveTo(pt);
		dc->LineTo(next_ControlPt);
		dc->MoveTo(pt);

		DrawControlPoint(dc, pre_ControlPt);
		DrawControlPoint(dc, next_ControlPt);

		SelectObject(dc->m_hDC, oldPen);

		DeleteObject(purplePen);
	}
}

void CParticlePathDlg::DrawControlPoint(CDC* dc, POINT pt)
{//画方块
	int r = 3;
	dc->Rectangle(pt.x-r, pt.y-r, pt.x+r, pt.y+r);
}

void CParticlePathDlg::InvertLine(CDC* pDC, CPoint ptFrom, CPoint ptTo)
{
	int nOldMode = pDC->SetROP2(R2_NOT);
	pDC->MoveTo(ptFrom);
	pDC->LineTo(ptTo);
	pDC->SetROP2(nOldMode);
}

void CParticlePathDlg::OnClose()
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	if(m_bModifyPath)
	{
		CString strInfo = L"请先保存当前编辑好的路径";
		int res = MessageBox(strInfo, L"提示", MB_ICONQUESTION|MB_YESNOCANCEL);
		if(res == IDYES) //IDYES
		{
			OnBnClickedButtonSavepath();
		}
		else if(res == IDCANCEL)
		{
			return;
		}
		else
		{// res == IDNO 继续后面的处理...
		}
	}

	CDialog::OnClose();
}

void CParticlePathDlg::OnLButtonDown(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	if( m_rctDrawPath.PtInRect(point) )
	{
		TransCoor(point, 1.0f/m_fPathZoom, -m_fPathSpin, m_nPathMirrorType);//实际数据没改，这里调整鼠标去匹配
		m_curSelPointIndex = -1;
		m_nBezierPtIndex = -1;
		m_nCurSelCharIndex = -1;

		if(m_bAddPoint)
		{
			float dis = 10; //大于5就行...
			int cnt = m_vectorPathPoint.size();
			if(cnt > 0)
				dis = ComputerDistance(point, m_vectorPathPoint[cnt-1].pt);
			if(dis > 5)//修改双击同一个点报错的bug
			{
				Nuclear::PATHPOINT PathPoint;
				PathPoint.pt.x = point.x;
				PathPoint.pt.y = point.y;

				PathPoint.pre_ControlPt.x = point.x; // - 30
				PathPoint.pre_ControlPt.y = point.y;
				PathPoint.next_ControlPt.x = point.x; // + 30
				PathPoint.next_ControlPt.y = point.y;

				m_vectorPathPoint.push_back(PathPoint);

				m_curSelPointIndex = cnt;//m_vectorPathPoint.size()-1
				m_bModifyPath = true;
			}
		}
		else if(m_bMovePoint)
		{
			BOOL bSelPoint = FALSE;
			int cnt = m_vectorPathPoint.size();
			for(int i=0; i<cnt; i++)
			{
				float dis = ComputerDistance(point, m_vectorPathPoint[i].pt);
				if(dis<5)
				{
					m_curSelPointIndex = i;
					bSelPoint = TRUE;
					InvalidateRect(&m_rctDrawPath, false); //特殊颜色标示选中点...

					int nextPointIndex = (i+1)>(cnt-1) ? (cnt-1):(i+1);
					int prePointIndex = (i-1)<0 ? 0:(i-1);

					if(i==0)//首节点
					{
						m_ptFrom.x = m_vectorPathPoint[nextPointIndex].pt.x;
						m_ptFrom.y = m_vectorPathPoint[nextPointIndex].pt.y;
						m_ptTo.x = m_vectorPathPoint[i].pt.x;
						m_ptTo.y = m_vectorPathPoint[i].pt.y;
					}
					else if(i==cnt-1)//末节点
					{
						m_ptFrom.x = m_vectorPathPoint[prePointIndex].pt.x;
						m_ptFrom.y = m_vectorPathPoint[prePointIndex].pt.y;
						m_ptTo.x = m_vectorPathPoint[i].pt.x;
						m_ptTo.y = m_vectorPathPoint[i].pt.y;
					}
					else
					{
						m_ptFrom0.x = m_vectorPathPoint[prePointIndex].pt.x;
						m_ptFrom0.y = m_vectorPathPoint[prePointIndex].pt.y;
						m_ptFrom.x = m_vectorPathPoint[nextPointIndex].pt.x;
						m_ptFrom.y = m_vectorPathPoint[nextPointIndex].pt.y;
						m_ptTo.x = m_vectorPathPoint[i].pt.x;
						m_ptTo.y = m_vectorPathPoint[i].pt.y;
					}

					break;
				}
			}
			m_bModifyPath = true;
		}
		else if(m_bCurveAdjust)
		{//选择曲线点调整..
			BOOL bSelBezierPoint = FALSE;
			int cnt = m_vectorPathPoint.size();
			for(int i=0; i<cnt; i++)
			{
				float dis = ComputerDistance(point, m_vectorPathPoint[i].pt);
				if(dis<5)
					m_curSelPointIndex = i;

				if(m_vectorPathPoint[i].m_bCurve)
				{
					float pre_dis = ComputerDistance(point, m_vectorPathPoint[i].pre_ControlPt);
					float next_dis = ComputerDistance(point, m_vectorPathPoint[i].next_ControlPt);

					if(pre_dis<5)
					{
						m_nBezierPtIndex = 0;
						m_ptTo.x = m_vectorPathPoint[i].pre_ControlPt.x;
						m_ptTo.y = m_vectorPathPoint[i].pre_ControlPt.y;
						m_ptTo0.x = m_vectorPathPoint[i].next_ControlPt.x;
						m_ptTo0.y = m_vectorPathPoint[i].next_ControlPt.y;
						bSelBezierPoint = true;
					}
					if(next_dis<5)
					{
						m_nBezierPtIndex = 1;
						m_ptTo.x = m_vectorPathPoint[i].pre_ControlPt.x;
						m_ptTo.y = m_vectorPathPoint[i].pre_ControlPt.y;
						m_ptTo0.x = m_vectorPathPoint[i].next_ControlPt.x;
						m_ptTo0.y = m_vectorPathPoint[i].next_ControlPt.y;
						bSelBezierPoint = true;
					}
					if(bSelBezierPoint)
					{
						m_curSelPointIndex = i;
						m_ptFrom.x = m_vectorPathPoint[i].pt.x;
						m_ptFrom.y = m_vectorPathPoint[i].pt.y;
						break;
					}
				}
			}
			m_bModifyPath = true;
		}
		else if(m_bVectorCharPath)
		{ //字符路径...
			bool bSelChar = false;
			int cnt = m_vectorCharStartPt.size();
			int i=0;
			m_bMoveChar = false;
			m_bRotateChar = false;
			for(i=0; i<cnt; i++)
			{
				float dis = ComputerDistance(point, m_vectorCharStartPt[i]);
				if(dis<45)
				{
					m_bMoveChar = true;
					bSelChar = true;
					break;
				}
				m_nCurRotatePtIndex = -1;
				float fMinDis4Corner = ComputerMinDis4Corner(point, i);
				if(fMinDis4Corner<10)
				{
					m_bRotateChar = true;
					bSelChar = true;
					break;
				}
			}
			if(bSelChar)
			{
				m_pointMouseDown = point;
				m_nCurSelCharIndex = i;

				m_ptFrom.x = m_vectorCharStartPt[i].x;
				m_ptFrom.y = m_vectorCharStartPt[i].y;
				m_ptTo.x = m_vectorCharStartPt[i].x;
				m_ptTo.y = m_vectorCharStartPt[i].y;
			}
		}
		else if(m_bMoveMap)
		{
			m_initialPoint = point;
		}
		else
		{
			int cnt = m_vectorPathPoint.size();
			for(int i=0; i<cnt; i++)
			{
				float dis = ComputerDistance(point, m_vectorPathPoint[i].pt);
				if(dis<5)
					m_curSelPointIndex = i;
			}
		}
	}
	else
	{
		OnEnKillfocusEdit(0);
	}

	CDialog::OnLButtonDown(nFlags, point);
}

void CParticlePathDlg::OnLButtonUp(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	if(m_bRotateChar)
	{
		m_bRotateChar = false;
	}

	InvalidateRect(&m_rctDrawPath, false); 
	CDialog::OnLButtonUp(nFlags, point);
}

void CParticlePathDlg::OnMouseMove(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	//设置鼠标...
	if( m_rctDrawPath.PtInRect(point) ) 
	{
		TransCoor(point, 1.0f/m_fPathZoom, -m_fPathSpin, m_nPathMirrorType);//实际数据没改，这里调整鼠标去匹配

		if(m_bAddPoint)
			SetCursor(m_hAddCursor);

		bool bMoveCursor = false;
		if(m_bMovePoint)
		{
			int cnt = m_vectorPathPoint.size();
			for(int i=0; i<cnt; i++)
			{
				float dis = ComputerDistance(point, m_vectorPathPoint[i].pt);
				if(dis<5)
				{
					bMoveCursor = true;
					break;
				}
			}
		}

		bool bRotateCursor = false;
		if(m_bVectorCharPath)
		{ //字符路径...
			int cnt = m_vectorCharStartPt.size();
			for(int i=0; i<cnt; i++)
			{
				float dis = ComputerDistance(point, m_vectorCharStartPt[i]);
				if(dis<45)
				{
					bMoveCursor = true;
					break;
				}
				float fMinDis4Corner = ComputerMinDis4Corner(point, i);
				if(fMinDis4Corner<10)
				{
					bRotateCursor = true;
					break;
				}
			}
		}
		if(bMoveCursor)
			SetCursor(m_hMoveCursor);
		//::SetCursor(AfxGetApp()->LoadStandardCursor(IDC_SIZEALL));
		if(bRotateCursor)
			SetCursor(m_hRotateCursor);
	}
	//

	if( nFlags==MK_LBUTTON )
	{
		CDC *pDC = GetDC();

		if(m_bMovePoint)
		{
			if(m_curSelPointIndex == -1)
				return;
			m_bModifyPath = true;

			int dx = point.x - m_vectorPathPoint[m_curSelPointIndex].pt.x;
			int dy = point.y - m_vectorPathPoint[m_curSelPointIndex].pt.y;
			m_vectorPathPoint[m_curSelPointIndex].pt.x += dx;
			m_vectorPathPoint[m_curSelPointIndex].pt.y += dy;

			m_vectorPathPoint[m_curSelPointIndex].pre_ControlPt.x += dx;
			m_vectorPathPoint[m_curSelPointIndex].pre_ControlPt.y += dy;
			m_vectorPathPoint[m_curSelPointIndex].next_ControlPt.x += dx;
			m_vectorPathPoint[m_curSelPointIndex].next_ControlPt.y += dy;

			//绘制橡皮筋线..10-14上午.... 
			//移动首节点、末节点、中间节点...分三种情况处理
// 			int cnt = m_vectorPathPoint.size();
// 			if( (m_curSelPointIndex==0)||(m_curSelPointIndex==cnt-1) )
// 			{
// 				InvertLine(pDC, m_ptFrom, m_ptTo);
// 				InvertLine(pDC, m_ptFrom, point);
// 			}
// 			else
// 			{
// 				InvertLine(pDC, m_ptFrom, m_ptTo);
// 				InvertLine(pDC, m_ptFrom, point);
// 
// 				InvertLine(pDC, m_ptFrom0, m_ptTo);
// 				InvertLine(pDC, m_ptFrom0, point);
// 			}
// 			m_ptTo = point;

		}
		if( m_bCurveAdjust ) //!(m_bMovePoint || m_bAddPoint)
		{//曲线调整
			if(m_curSelPointIndex == -1)
				return;
			m_bModifyPath = true;

			BOOL bCurve = m_vectorPathPoint[m_curSelPointIndex].m_bCurve;
			if(!bCurve)
				return;
			BOOL bLink = m_vectorPathPoint[m_curSelPointIndex].m_bLink;
			if(bLink)
			{
				CPoint pt = m_vectorPathPoint[m_curSelPointIndex].pt;
				if(m_nBezierPtIndex==0)
				{
					m_vectorPathPoint[m_curSelPointIndex].pre_ControlPt.x = point.x;
					m_vectorPathPoint[m_curSelPointIndex].pre_ControlPt.y = point.y;
					m_vectorPathPoint[m_curSelPointIndex].next_ControlPt.x = 2*pt.x - point.x;
					m_vectorPathPoint[m_curSelPointIndex].next_ControlPt.y = 2*pt.y - point.y;
				}
				else
				{// ==1
					m_vectorPathPoint[m_curSelPointIndex].next_ControlPt.x = point.x;
					m_vectorPathPoint[m_curSelPointIndex].next_ControlPt.y = point.y;
					m_vectorPathPoint[m_curSelPointIndex].pre_ControlPt.x = 2*pt.x - point.x;
					m_vectorPathPoint[m_curSelPointIndex].pre_ControlPt.y = 2*pt.y - point.y;
				}

// 				InvertLine(pDC, m_ptFrom, m_ptTo);
// 				m_ptTo = m_vectorPathPoint[m_curSelPointIndex].pre_ControlPt;
// 				InvertLine(pDC, m_ptFrom, m_ptTo);
// 
// 				InvertLine(pDC, m_ptFrom, m_ptTo0);
// 				m_ptTo0 = m_vectorPathPoint[m_curSelPointIndex].next_ControlPt;
// 				InvertLine(pDC, m_ptFrom, m_ptTo0);
			}
			else
			{
				if(m_nBezierPtIndex==0)
				{
					m_vectorPathPoint[m_curSelPointIndex].pre_ControlPt.x = point.x;
					m_vectorPathPoint[m_curSelPointIndex].pre_ControlPt.y = point.y;

// 					InvertLine(pDC, m_ptFrom, m_ptTo);
// 					m_ptTo = m_vectorPathPoint[m_curSelPointIndex].pre_ControlPt;
// 					InvertLine(pDC, m_ptFrom, m_ptTo);
				}
				else
				{// ==1
					m_vectorPathPoint[m_curSelPointIndex].next_ControlPt.x = point.x;
					m_vectorPathPoint[m_curSelPointIndex].next_ControlPt.y = point.y;

// 					InvertLine(pDC, m_ptFrom, m_ptTo0);
// 					m_ptTo0 = m_vectorPathPoint[m_curSelPointIndex].next_ControlPt;
// 					InvertLine(pDC, m_ptFrom, m_ptTo0);
				}
			}
		}
		if(m_bVectorCharPath)
		{
			if(m_nCurSelCharIndex == -1)
				return;
			m_bModifyPath = true;

			CPoint old_Point = m_pointMouseDown;
		//	old_Point = m_pointMouseDown;
			m_pointMouseDown = point;
			int dx = 0;
			int dy = 0;
			if(m_bMoveChar)
			{
				dx = point.x - old_Point.x;
				dy = point.y - old_Point.y;

				m_vectorCharStartPt[m_nCurSelCharIndex].x += dx;
				m_vectorCharStartPt[m_nCurSelCharIndex].y += dy;

				for(int i=0; i<4; i++)
					m_vectorCharContourRct[m_nCurSelCharIndex].pt[i] += CPoint(dx, dy);
			}
			else if(m_bRotateChar)
			{
				CPoint centPt;
				centPt = m_vectorCharStartPt[m_nCurSelCharIndex];
			//	int nAngle = Computer2LineAngle(centPt, pt, point);//角度
				float fAngle = 0.0f;
				bool bres = Computer2LineAngle(centPt, old_Point, point, fAngle); //pt
				m_fVectorFontAngle[m_nCurSelCharIndex] += fAngle;	
			}

		//	InvertLine(pDC, m_ptFrom, m_ptTo);
		//	InvertLine(pDC, m_ptFrom, point);
		//	m_ptTo = point;
		}
		if(m_bMoveMap)
		{
			//移动地图
			int dx = point.x - m_initialPoint.x;
			int dy = point.y - m_initialPoint.y;
			m_initialPoint = point;
			m_rctMapPos -= CPoint(dx, dy);
		}

		//绘制曲线的橡皮筋线很麻烦..这里简略处理了..以后有空可以继续补充完善
		//如果很闪就去掉..恢复上面的InvertLine..
		if(m_nMouseMoveCnt>10)
		{
			InvalidateRect(&m_rctDrawPath, false); 
			m_nMouseMoveCnt = 0;
		}
		m_nMouseMoveCnt++;
		//以上 2008-10-15

		ReleaseDC(pDC);
	}

	CDialog::OnMouseMove(nFlags, point);
}

void CParticlePathDlg::OnRButtonDown(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	if( m_rctDrawPath.PtInRect(point) )
	{
		//距离目标点小于5个像素叫选中.. 弹出右键菜单:删除、自由曲线等..
		BOOL bSelPoint = FALSE;
		int cnt = m_vectorPathPoint.size();
		for(int i=0; i<cnt; i++)
		{
			float dis = ComputerDistance(point, m_vectorPathPoint[i].pt);
			if(dis<5)
			{
				m_curSelPointIndex = i;
				bSelPoint = TRUE;
				InvalidateRect(&m_rctDrawPath, false); //特殊颜色标示选中点...
				break;
			}
		}
		if(bSelPoint)
		{
			CPoint pt;
			::GetCursorPos(&pt);

			CMenu menu;
			menu.LoadMenu(IDR_MENU1);

			BOOL bCurve = m_vectorPathPoint[m_curSelPointIndex].m_bCurve;
			BOOL bLink = m_vectorPathPoint[m_curSelPointIndex].m_bLink;

			if(bCurve)
			{
				menu.CheckMenuItem(ID_NENU_CURVE, MF_CHECKED | MF_BYCOMMAND);
				menu.EnableMenuItem(ID_NENU_LINK, MF_ENABLED | MF_BYCOMMAND);
			}
			else
			{
				menu.CheckMenuItem(ID_NENU_CURVE, MF_UNCHECKED | MF_BYCOMMAND);
				menu.CheckMenuItem(ID_NENU_LINK, MF_UNCHECKED | MF_BYCOMMAND);
				menu.EnableMenuItem(ID_NENU_LINK, MF_BYCOMMAND | MF_DISABLED | MF_GRAYED);
			}
			if(bLink)
				menu.CheckMenuItem(ID_NENU_LINK, MF_CHECKED | MF_BYCOMMAND);
			else
				menu.CheckMenuItem(ID_NENU_LINK, MF_UNCHECKED | MF_BYCOMMAND);

			CMenu* pMenu = menu.GetSubMenu(0);
			pMenu->TrackPopupMenu(TPM_LEFTALIGN | TPM_RIGHTBUTTON, pt.x, pt.y, this, NULL);
		}
	}

	CDialog::OnRButtonDown(nFlags, point);
}

float CParticlePathDlg::ComputerDistance(CPoint pt1, CPoint pt2)
{
	float dis = powf((float)(pt1.x - pt2.x), 2.0f) + powf((float)(pt1.y - pt2.y), 2.0f);
	dis = sqrt(dis);
	return dis;
}

float CParticlePathDlg::ComputerMinDis4Corner(CPoint pt0, int centPtIndex)
{
	CPoint pt1 = m_vectorCharContourRct[centPtIndex].pt[0];
	CPoint pt2 = m_vectorCharContourRct[centPtIndex].pt[1];
	CPoint pt3 = m_vectorCharContourRct[centPtIndex].pt[2];
	CPoint pt4 = m_vectorCharContourRct[centPtIndex].pt[3];

	CPoint centPt = m_vectorCharStartPt[centPtIndex];
	float fAngle = m_fVectorFontAngle[centPtIndex];
	RotatePoint(pt1, centPt, fAngle);
	RotatePoint(pt2, centPt, fAngle);
	RotatePoint(pt3, centPt, fAngle);
	RotatePoint(pt4, centPt, fAngle);

	float dis[4];
	dis[0] = powf((float)(pt0.x - pt1.x), 2.0f) + powf((float)(pt0.y - pt1.y), 2.0f);
	dis[1] = powf((float)(pt0.x - pt2.x), 2.0f) + powf((float)(pt0.y - pt2.y), 2.0f);
	dis[2] = powf((float)(pt0.x - pt3.x), 2.0f) + powf((float)(pt0.y - pt3.y), 2.0f);
	dis[3] = powf((float)(pt0.x - pt4.x), 2.0f) + powf((float)(pt0.y - pt4.y), 2.0f);

	float min_dis = dis[0];
	m_nCurRotatePtIndex = 0;
	for(int i=1; i<4; i++)
	{
		if(dis[i]<min_dis)
		{
			min_dis = dis[i];
			m_nCurRotatePtIndex = i;
		}
	}
	min_dis = sqrt(min_dis);
	return min_dis;
}

CPoint CParticlePathDlg::Computer3BezierCurvePoint(float u, CPoint p0, CPoint p1, CPoint p2, CPoint p3)
{
	float coef0 = powf(1.0f-u, 3.0f);
	float coef1 = 3.0f * u * powf(1.0f-u, 2);
	float coef2 = 3 * powf(u, 2.0f) * (1-u);
	float coef3 = powf(u, 3.0f);

	CPoint pt;
	pt.x = (long)(coef0*p0.x + coef1*p1.x + coef2*p2.x + coef3*p3.x);
	pt.y = (long)(coef0*p0.y + coef1*p1.y + coef2*p2.y + coef3*p3.y);
	return pt;
}

void CParticlePathDlg::RotatePoint(CPoint& pt, CPoint centPt, float fFontAngle)
{
	CPoint pt111 = pt;
	CPoint pt0;
	float fAngle = -Nuclear::TORADIANS(fFontAngle);
	pt -= centPt;
	pt0.x = (int)( pt.x*cos(fAngle) - pt.y*sin(fAngle) +0.5f);
	pt0.y = (int)( pt.x*sin(fAngle) + pt.y*cos(fAngle) +0.5f);
	pt0 += centPt;
	pt = pt0;
}

int CParticlePathDlg::Computer2LineAngle(const CPoint& ptAnchor, const CPoint& ptOld, const CPoint& ptNew)
{//有问题..
	float dx, dy;  
	float fAgO, fAgN; 
	dx = (float)( ptOld.x - ptAnchor.x );
	dy = (float)( ptOld.y - ptAnchor.y );
	if( fabs(dx) < Nuclear::XP_EPSILON )
	{  
		if(dy>0)  
			fAgO = 90;  
		else  
			fAgO = 270;  
	}  
	else  
	{
		fAgO = Nuclear::TOANGLE(atan(dy/dx));//(90,-90)之间  
		if( (dy>0)&&(dx>0) )
			fAgO = fAgO;
		if( (dy>0)&&(dx<0) )
			fAgO = 180+fAgO;
		if( (dy<0)&&(dx<0) )
			fAgO = 180+fAgO;
		if( (dy<0)&&(dx>0) )
			fAgO = 360+fAgO;
	}  
	dx = (float)( ptNew.x-ptAnchor.x );  
	dy = (float)( ptNew.y-ptAnchor.y );  
	if( fabs(dx)<Nuclear::XP_EPSILON )  //
	{  
		if(dy>0)  
			fAgN = 90;  
		else  
			fAgN = 270;  
	}  
	else  
	{  
		fAgN = Nuclear::TOANGLE(atan(dy/dx));//(90,-90)之间  
		if( (dy>0)&&(dx>0) )
			fAgN = fAgN;
		if( (dy>0)&&(dx<0) )
			fAgN = 180+fAgN;
		if( (dy<0)&&(dx<0) )
			fAgN = 180+fAgN;
		if( (dy<0)&&(dx>0) )
			fAgN = 360+fAgN;
	}
	return (int)(fAgO-fAgN);
}

bool CParticlePathDlg::Computer2LineAngle(const CPoint &ptAnchor,const CPoint &ptOld,const CPoint &ptNew, float &fValue)
{
	float xCentre = 0;
	float yCentre = 0;
	xCentre	= (float)ptAnchor.x;
	yCentre	= (float)ptAnchor.y;

	float xPos	= 0;
	float yPos	= 0;
	xPos = (float)ptOld.x;
	yPos = (float)ptOld.y;
	CPoint point;
	point = ptNew;

	if((fabs(xPos-xCentre)<0.000000001)&&(fabs(yPos-yCentre)<0.000000001))
		return false;

	if((fabs(point.x-xCentre)<0.000000001)&&(fabs(point.y-yCentre)<0.000000001))
		return false;

	if((fabs(point.x-xPos)<0.000000001)&&(fabs(point.y-yPos)<0.000000001))
		return false;

	float ao =	sqrt((xPos-xCentre)*(xPos-xCentre)+(yPos-yCentre)*(yPos-yCentre));
	float bo =	sqrt((point.x-xCentre)*(point.x-xCentre)+(point.y-yCentre)*(point.y-yCentre));
	float ab =	sqrt((xPos-point.x)*(xPos-point.x)+(yPos-point.y)*(yPos-point.y));

	if((bo<0.000000001)&&(ao<0.000000001))
		return false;

	if(ab<0.000000001)
		return false;

	float cosx	= (ao*ao+bo*bo-ab*ab)/(2*ao*bo);
	cosx = cosx>1.0f?1.0f:cosx<-1.0f?-1.0f:cosx;
	float fAngle =	acos(cosx);

	float ax =	xPos-xCentre;
	float ay =	yPos-yCentre;
	float bx =	point.x-xCentre;
	float by =	point.y-yCentre;

	if(ax*by-ay*bx<0)
		fAngle	=	-fAngle;

	fValue=	-Nuclear::TOANGLE(fAngle);
	return true;
}

void CParticlePathDlg::TransCoor(CPoint& pt, float fZoom, float fSpin, int nPathMirrorType)
{
	int x0 = (m_rctDrawPath.left + m_rctDrawPath.right)/2;
	int y0 = (m_rctDrawPath.top + m_rctDrawPath.bottom)/2;

	pt.x -= x0;
	pt.y = -pt.y + y0;

	pt.x = (int)( pt.x *fZoom );
	pt.y = (int)( pt.y *fZoom );

	fSpin = Nuclear::TORADIANS(fSpin);
	float posx = (float)pt.x;
	float posy = (float)pt.y;
	pt.x = (int)( posx*cos(fSpin) - posy*sin(fSpin) );
	pt.y = (int)( posx*sin(fSpin) + posy*cos(fSpin) );

	switch(nPathMirrorType)
	{
	case Nuclear::PATHMIRRORTYPE_NONE:
		break;
	case Nuclear::PATHMIRRORTYPE_LR:
		pt.x *= -1;
		break;
	case Nuclear::PATHMIRRORTYPE_TB:
		pt.y *= -1;
		break;
	case Nuclear::PATHMIRRORTYPE_LRTB:
		pt.x *= -1;
		pt.y *= -1;
		break;
	default:
		break;
	}

	pt.x += x0;
	pt.y = -pt.y + y0;
}

void CParticlePathDlg::CreateVectorCurvePoint(int index, CPoint pt0, CPoint pt1, CPoint pt2, CPoint pt3)
{
	 //先1.0f的时候来采样计算产生fullpoint然后再缩放到正常的情况
	//否则缩小了之后采样点会减少..程序出错

	float dis = ComputerDistance(pt0, pt3);
	float step = 8.0f/dis;
	int nStep = (int)(dis/8.0f);

	CurvePoint curvePt;
	for(int i=0; i<nStep; i++)
	{
		float u = i*step;
		CPoint point;
		point = Computer3BezierCurvePoint(u, pt0, pt1, pt2, pt3);

		TransCoor(point, m_fPathZoom, m_fPathSpin, m_nPathMirrorType);

		curvePt.push_back(point);
	}
	if(nStep*step < 1.0f)
	{
		TransCoor(pt3, m_fPathZoom, m_fPathSpin, m_nPathMirrorType);
		curvePt.push_back(pt3);
	}

	m_vectorCurvePoint.push_back(curvePt);
}

void CParticlePathDlg::CreatePathCurve(int curSelPointIndex)
{
	CPoint pt0 = m_vectorPathPoint[curSelPointIndex].pt;
	CPoint pt1 = m_vectorPathPoint[curSelPointIndex].next_ControlPt;
	CPoint pt2 = m_vectorPathPoint[curSelPointIndex +1].pre_ControlPt;
	CPoint pt3 = m_vectorPathPoint[curSelPointIndex +1].pt;
	CreateVectorCurvePoint(curSelPointIndex, pt0, pt1, pt2, pt3);
}

void CParticlePathDlg::OnRButtonUp(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	InvalidateRect(&m_rctDrawPath, false); //特殊颜色标示选中点...
	CDialog::OnRButtonUp(nFlags, point);
}

void CParticlePathDlg::OnNenuDelpoint()
{
	std::vector<Nuclear::PATHPOINT>::iterator it0 = m_vectorPathPoint.begin() +m_curSelPointIndex;
	m_vectorPathPoint.erase(it0);
	
	m_curSelPointIndex = -1; //当前点已经被删除...

	InvalidateRect(&m_rctDrawPath, false);
}

void CParticlePathDlg::OnNenuCurve()
{
	// TODO: 在此添加命令处理程序代码
	BOOL bCurve = m_vectorPathPoint[m_curSelPointIndex].m_bCurve;
	bCurve = !bCurve;
	m_vectorPathPoint[m_curSelPointIndex].m_bCurve = bCurve;
//	m_vectorPathPoint[m_curSelPointIndex].m_bLink = bCurve; //---2010-9-8--笔误？？

	//给曲线控制点赋初始值
	int r=0;
	if(bCurve)
		r = 20;

	CPoint pt;
	pt.x = m_vectorPathPoint[m_curSelPointIndex].pt.x;
	pt.y = m_vectorPathPoint[m_curSelPointIndex].pt.y;

	m_vectorPathPoint[m_curSelPointIndex].pre_ControlPt.x = pt.x -r;
	m_vectorPathPoint[m_curSelPointIndex].pre_ControlPt.y = pt.y;
	m_vectorPathPoint[m_curSelPointIndex].next_ControlPt.x = pt.x +r;
	m_vectorPathPoint[m_curSelPointIndex].next_ControlPt.y = pt.y;

	InvalidateRect(&m_rctDrawPath, false);
}

void CParticlePathDlg::OnNenuLink()
{
	BOOL bLink = m_vectorPathPoint[m_curSelPointIndex].m_bLink;
	bLink = !bLink;
	m_vectorPathPoint[m_curSelPointIndex].m_bLink = bLink;
}

void CParticlePathDlg::OnBnClickedCheckAddpoint()
{
	UpdateData(TRUE);
	if(m_bAddPoint)
	{
		m_bMovePoint = false;
		m_bCurveAdjust = false;
		m_bVectorCharPath = false;
		m_bMoveMap = false;
	}
	UpdateData(false);
}

void CParticlePathDlg::OnBnClickedCheckMovepoint()
{
	UpdateData(TRUE);
	if(m_bMovePoint)
	{
		m_bAddPoint = false;
		m_bCurveAdjust = false;
		m_bVectorCharPath = false;
		m_bMoveMap = false;
	}
	UpdateData(false);
}

void CParticlePathDlg::OnBnClickedCheckCurveadjust()
{
	UpdateData(TRUE);
	if(m_bCurveAdjust)
	{
		m_bAddPoint = false;
		m_bMovePoint = false;
		m_bVectorCharPath = false;
		m_bMoveMap = false;
	}
	UpdateData(false);
}


void CParticlePathDlg::OnBnClickedCheckLoadvectorchar()
{
	UpdateData(TRUE);
	if(m_bVectorCharPath)
	{
		m_bAddPoint = false;
		m_bMovePoint = false;
		m_bCurveAdjust = false;
		m_bMoveMap = false;
		m_bCurveSample = false;
	}
	((CButton*)GetDlgItem(IDC_BUTTON_SetVectorChar))->EnableWindow(m_bVectorCharPath);
	UpdateData(false);
}

void CParticlePathDlg::OnBnClickedCheckMovemap()
{
	UpdateData(TRUE);
	if(m_bMoveMap)
	{
		m_bAddPoint = false;
		m_bMovePoint = false;
		m_bVectorCharPath = false;
		m_bCurveAdjust = false;
	}
	UpdateData(false);
}

void CParticlePathDlg::OnBnClickedCheckShowgrid()
{
	UpdateData(TRUE);
	InvalidateRect(&m_rctDrawPath, false);
}

void CParticlePathDlg::OnBnClickedButtonReset()
{
	m_vectorPathPoint.clear();
	m_curSelPointIndex = -1;
	m_nBezierPtIndex = -1;

	InvalidateRect(&m_rctDrawPath, false);
}

void CParticlePathDlg::OnBnClickedButtonSavepath()
{
	CString strDefExt = L"path";
	std::wstring strFileName = m_strSysPath + L"path\\*.path";
	CString strFilter = L"(*.path)|*.path|";

	CFileDialog dlg(false, strDefExt, strFileName.c_str(), OFN_OVERWRITEPROMPT, strFilter, NULL);

	dlg.DoModal();
	CString filePath = dlg.GetFileName();
	if( filePath.IsEmpty() )
		return;

	filePath = dlg.GetPathName();
	int pos = filePath.Find(L"\\path\\");
	m_strPathFileName = filePath.Right(filePath.GetLength() - pos-6);

	//判断路径是否合法。。指定了别的目录下的文件是不可以的
	CString resonablePath = m_strSysPath + L"path\\" + m_strPathFileName;
	if(filePath.CompareNoCase(resonablePath) != 0)
	{
		MessageBox(L"指定的文件路径不合法\n");
		return;
	}
	//

	m_PslPathObject.SetVersion(3); //0--1字节序调整 1--2 关键帧路径(路径由美术编辑好)
								   //2--3关键帧路径(路径可以由程序来曲线插值)

	m_PslPathObject.SetVectorCharPathFlag(m_bVectorCharPath);
	m_PslPathObject.SetCurveSampleFlag(m_bCurveSample);

	int x0 = (m_rctDrawPath.left + m_rctDrawPath.right)/2;
	int y0 = (m_rctDrawPath.top + m_rctDrawPath.bottom)/2;

	std::vector<POINT> vectorFullPathPoint;
	std::vector<Nuclear::PATHPOINT> vectorPathPoint = m_vectorPathPoint;
//	int cnt = m_vectorPathPoint.size();
//	for(int i=0; i<cnt; i++)
//	{
//		vectorPathPoint.push_back(m_vectorPathPoint[i]);
//	}

	Nuclear::CRECT rctCent;
	rctCent.left = m_rctDrawPath.left;
	rctCent.top = m_rctDrawPath.top;
	rctCent.right = m_rctDrawPath.right;
	rctCent.bottom = m_rctDrawPath.bottom;

	if(m_bVectorCharPath)
	{
		CreateFullVectorCharStrPath();//vectorFullPathPoint
		vectorFullPathPoint = m_vectorFullPathPoint;
		int cnt = vectorFullPathPoint.size();
		for(int i=0; i<cnt; i++)
		{
			vectorFullPathPoint[i].x -= x0;
			vectorFullPathPoint[i].y = -vectorFullPathPoint[i].y + y0;
		}
	}
	else
	{
		int cnt = vectorPathPoint.size();
		for(int i=0; i<cnt; i++)
		{
			vectorPathPoint[i].pt.x -= x0;
			vectorPathPoint[i].pt.y = -vectorPathPoint[i].pt.y + y0;
			vectorPathPoint[i].pre_ControlPt.x -= x0;
			vectorPathPoint[i].pre_ControlPt.y = -vectorPathPoint[i].pre_ControlPt.y + y0;
			vectorPathPoint[i].next_ControlPt.x -= x0;
			vectorPathPoint[i].next_ControlPt.y = -vectorPathPoint[i].next_ControlPt.y + y0;
		}

		//即使在调节了缩放参数的情况下修改了路径..上面路径点依然是1:1的，可以直接存盘..
	}

	m_PslPathObject.SetFullPathPointVector(vectorFullPathPoint); //字符
	m_PslPathObject.SetVectorPathPoint(vectorPathPoint); //用户自定义
	//

	std::wstring strPath = filePath;
	bool bres = m_PslPathObject.SaveToNativePath(strPath.c_str());
	if(!bres)
	{
		OutputDebugString(L"SavePath() --- Failed\n");
	}

	if(m_bCurveSample || m_bhasKeyPoint)
		WriteKeyPointIndexFile(strPath);

	m_bModifyPath = false;	
	SetWindowText(filePath);
}

void CParticlePathDlg::WriteKeyPointIndexFile(std::wstring strPath) 
{//strPath是路径完整文件名

	CString KeyPointFilePath = m_strSysPath + L"path\\" + m_strPathFileName;

	//减掉path扩展名换成_keyPoint.txt
	KeyPointFilePath = KeyPointFilePath.Left(KeyPointFilePath.GetLength() - 4) + L"_keyPoint.txt";

	FILE *file = NULL;
//	setlocale(LC_CTYPE, "chinese_china");
	_tfopen_s(&file, KeyPointFilePath, L"wt,ccs=UNICODE"); //创建unicode文件，默认是ansi
	if( file==NULL )
	{
		MessageBox(L"打开keyPoint.txt文件失败\n");
		return;
	}

	std::wstring strTemp0 = strPath + L" 中的关键点索引序号\n";
	CString strTemp = strTemp0.c_str();

	int pos = strTemp.Find(L"\\res\\");
	strTemp = strTemp.Right(strTemp.GetLength() - pos); //减掉绝对路径

	_ftprintf(file, strTemp); 

	m_vectorKeyPointIndex.clear();
	if(m_bCurveSample) //就用美术编辑的点，不需要程序插值了...
	{//保存关键帧点的序号--文本文件

		int  cnt = m_vectorPathPoint.size();
		for(int i=0; i<cnt; i++)
		{
			BOOL bKeypt = m_vectorPathPoint[i].bKeyPoint;
			if(bKeypt)
			{
				m_vectorKeyPointIndex.push_back(i);
				_ftprintf(file, L"%d\n", i);
			}
		}
	}
	else
	{ //m_bhasKeyPoint == true
		//根据曲线插值结果写关键点索引文件
		int cnt = (int)m_vectorPathPoint.size();
		if(cnt<2)
		{
			return;
		}

		int nPointNum = 0;
		for(int i=0; (i<cnt-1)&&(cnt>1); i++)
		{
			BOOL bKeypt = m_vectorPathPoint[i].bKeyPoint;
			if(bKeypt)
			{
				m_vectorKeyPointIndex.push_back(nPointNum);
				_ftprintf(file, L"%d\n", nPointNum );
			}
			POINT pt0 = m_vectorPathPoint[i].pt;
			POINT pt3 = m_vectorPathPoint[i+1].pt;
			float dis = ComputerDistance(pt0, pt3);

			float step = 3.0f/dis; //点的疏密控制...以后要改变精度就直接修改这里就行了
			int nStep = (int)(dis/3.0f);

			nPointNum += nStep;
			if(nStep*step < 1.0f)
				nPointNum += 1;
		}

		BOOL bKeypt = m_vectorPathPoint[cnt-1].bKeyPoint;
		if(bKeypt)
		{
			m_vectorKeyPointIndex.push_back(nPointNum-1);
			_ftprintf(file, L"%d\n", nPointNum-1 );
		}
	}
	fclose(file);
}

void CParticlePathDlg::Loadpath(std::wstring pathFileName)
{
	bool bres = m_PslPathObject.LoadFromNativePath(pathFileName.c_str());
	if(!bres)
	{
		OutputDebugString(L"OpenPath() --- Failed\n");
		return;
	}

	if(m_PslPathObject.GetVersion() < 2)
		m_bModifyPath = true;

	m_bVectorCharPath = m_PslPathObject.GetVectorCharPointFlag();
	m_bCurveSample = m_PslPathObject.GetCurveSampleFlag();

	m_vectorFullPathPoint = m_PslPathObject.GetFullPathPointVector();
	std::vector<Nuclear::PATHPOINT> vectorPathPoint;
	vectorPathPoint = m_PslPathObject.GetVectorPathPoint();
	m_vectorPathPoint.clear();

	m_vectorPathPoint = vectorPathPoint;

	TransformPathPoint(m_vectorPathPoint, m_fPathZoom, m_fPathSpin, Nuclear::PSL_PATHMIRRORTYPE(m_nPathMirrorType));

	m_bPslCoor = true;
	//为了处理打开时预览当前使用路径...先存相对，第一次绘制的时候转为绝对坐标，用m_bPslCoor标记
	//在view中要预览的时候还不知道m_rctDrawPath的大小，所以在这里不能转..
	//这个做法比较2，以后有空修改..

	if(m_bVectorCharPath)
	{
		m_bAddPoint = false;
		m_bMovePoint = false;
		m_bCurveAdjust = false;
	}

	if(m_bCurveSample)
	{
		m_bCurveAdjust = false;
	}
}

void CParticlePathDlg::TransformPathPoint(std::vector<Nuclear::PATHPOINT>& vectorPathPoint, float fPathZoom, float fPathSpin, Nuclear::PSL_PATHMIRRORTYPE nPathMirrorType)
{
	//缩放vectorPathPoint --
	Nuclear::PATHPOINT pathpoint;
	int cnt = (int)vectorPathPoint.size();
	fPathSpin = Nuclear::TORADIANS(fPathSpin);
	float posx = 0.0f;
	float posy = 0.0f;
	for(int i=0; i<cnt; i++)
	{
		pathpoint = vectorPathPoint[i];

		pathpoint.pt.x = (int)(pathpoint.pt.x *fPathZoom);
		pathpoint.pt.y = (int)(pathpoint.pt.y *fPathZoom);
		pathpoint.pre_ControlPt.x = (int)(pathpoint.pre_ControlPt.x *fPathZoom);
		pathpoint.pre_ControlPt.y = (int)(pathpoint.pre_ControlPt.y *fPathZoom);
		pathpoint.next_ControlPt.x = (int)(pathpoint.next_ControlPt.x *fPathZoom);
		pathpoint.next_ControlPt.y = (int)(pathpoint.next_ControlPt.y *fPathZoom);

		posx = (float)pathpoint.pt.x;
		posy = (float)pathpoint.pt.y;
		pathpoint.pt.x = (int)(posx*cos(fPathSpin) - posy*sin(fPathSpin));
		pathpoint.pt.y = (int)(posx*sin(fPathSpin) + posy*cos(fPathSpin));

		posx = (float)pathpoint.pre_ControlPt.x;
		posy = (float)pathpoint.pre_ControlPt.y;
		pathpoint.pre_ControlPt.x = (int)(posx*cos(fPathSpin) - posy*sin(fPathSpin));
		pathpoint.pre_ControlPt.y = (int)(posx*sin(fPathSpin) + posy*cos(fPathSpin));

		posx = (float)pathpoint.next_ControlPt.x;
		posy = (float)pathpoint.next_ControlPt.y;
		pathpoint.next_ControlPt.x = (int)(posx*cos(fPathSpin) - posy*sin(fPathSpin));
		pathpoint.next_ControlPt.y = (int)(posx*sin(fPathSpin) + posy*cos(fPathSpin));

		switch(nPathMirrorType)
		{
		case Nuclear::PATHMIRRORTYPE_NONE:
			break;
		case Nuclear::PATHMIRRORTYPE_LR:
			pathpoint.pt.x *= -1;
			pathpoint.pre_ControlPt.x *= -1;
			pathpoint.next_ControlPt.x *= -1;
			break;
		case Nuclear::PATHMIRRORTYPE_TB:
			pathpoint.pt.y *= -1;
			pathpoint.pre_ControlPt.y *= -1;
			pathpoint.next_ControlPt.y *= -1;
			break;
		case Nuclear::PATHMIRRORTYPE_LRTB:
			pathpoint.pt.x *= -1;
			pathpoint.pt.y *= -1;
			pathpoint.pre_ControlPt.x *= -1;
			pathpoint.pre_ControlPt.y *= -1;
			pathpoint.next_ControlPt.x *= -1;
			pathpoint.next_ControlPt.y *= -1;
			break;
		default:
			break;
		}

		vectorPathPoint[i] = pathpoint;
	}
}

void CParticlePathDlg::OnBnClickedButtonLoadpath()
{
	CString strDefExt = L"path";
	std::wstring strFileName = m_strSysPath + L"path\\*.path";
	CString strFilter = L"(*.path)|*.path|";

	CFileDialog dlg(true, strDefExt, strFileName.c_str(), OFN_OVERWRITEPROMPT, strFilter, NULL);

	dlg.DoModal();
	CString filePath = dlg.GetFileName();
	if( filePath.IsEmpty() )
		return;

	filePath = dlg.GetPathName();
	int pos = filePath.Find(L"\\path\\");
	m_strPathFileName = filePath.Right(filePath.GetLength() - pos-6);

	//判断路径是否合法。。指定了别的目录下的文件是不可以的
	CString resonablePath = m_strSysPath + L"path\\" + m_strPathFileName;
	if(filePath.CompareNoCase(resonablePath) != 0)
	{
		MessageBox(L"指定的文件路径不合法\n");
		return;
	}
	//

	m_vectorPathPoint.clear();
	m_curSelPointIndex = -1;
	m_nBezierPtIndex = -1;

	m_fPathZoom = 1.0f;
	m_fPathSpin = 0.0f;
	Loadpath( filePath.GetString() );

	//	m_bModifyPath = false; //2010-9-9 bug？旧版本升级提示会被重置了...

	((CButton*)GetDlgItem(IDC_BUTTON_SetVectorChar))->EnableWindow(m_bVectorCharPath);
	UpdateData(false);

	SetWindowText(filePath);

	Invalidate(true);
}

void CParticlePathDlg::OnBnClickedButtonSetvectorchar()
{
	CTextInputDlg dlg;
	if (dlg.DoModal() == IDOK)
	{
		m_strInputText = dlg.GetInputText();
		m_nFontHeight = dlg.GetFontHeight();
		m_nFontSpace = dlg.GetFontSpace();
		m_strFontName = dlg.GetFontName();
		m_fFontAngle = (float)( dlg.GetFontAngle() );

		m_vectorPathPoint.clear();
		m_curSelPointIndex = -1;
		m_nBezierPtIndex = -1;

		CreateVectorCharPoint();// ........... 
		//
//		int cnt = CreateFullVectorCharStrPath(); //m_vectorFullPathPoint
		m_bModifyPath = true;	
		Invalidate(true);
	}
}

void CParticlePathDlg::CreateVectorCharPoint()
{
	CDC* pdc = GetDC();

	if (m_strInputText.IsEmpty())
		return;

	CFont font;
	VERIFY( font.CreateFont(
		m_nFontHeight,             // nHeight
		0,						   // nWidth
		0,           // nEscapement //0.1-degree units   //m_nFontAngle*10
		0,                         // nOrientation
		FW_NORMAL,                 // nWeight
		FALSE,                     // bItalic
		FALSE,                     // bUnderline
		0,                         // cStrikeOut
		ANSI_CHARSET,              // nCharSet
		OUT_DEFAULT_PRECIS,        // nOutPrecision
		CLIP_DEFAULT_PRECIS,       // nClipPrecision
		DEFAULT_QUALITY,           // nQuality
		DEFAULT_PITCH | FF_SWISS,  // nPitchAndFamily
		m_strFontName) ); 
	CFont* pOldFont = pdc->SelectObject(&font);

	int nChar = 0;  

	m_vectorStrContour.clear();

	int x0 = 50;//(m_rctDrawPath.left + m_rctDrawPath.right)/2;
	int y0 = (m_rctDrawPath.top + m_rctDrawPath.bottom)/2;

	m_vectorCharStartPt.clear(); //存储每个字符在屏幕的起始坐标
	m_vectorCharCenterPt.clear();
	m_vectorCharContourRct.clear();
	m_fVectorFontAngle.clear();
	m_nCurSelCharIndex = -1;

	for(int i=0; i<m_strInputText.GetLength(); i++)
	{
// 		if(m_strInputText.GetAt(i) & 0x80) //deal singlebyte char or multibyte char
// 		{
// 		//	int th = m_strInputText.GetAt(i);
// 		//	int tl = m_strInputText.GetAt(i + 1);
// 		//	nChar = ((th & 0x00ff)<<8) + (tl & 0x00ff);
// 		//	i++;
// 		}
// 		else
// 		{
// 			nChar = m_strInputText.GetAt(i);
// 		}

		nChar = m_strInputText.GetAt(i);

		GLYPHMETRICS glyphmetrics;
		Nuclear::CTTFOutline* outline = CreateOutline(pdc, nChar, glyphmetrics);

		std::vector<Nuclear::Contour> charcontor = outline->GetVectorContour();
		m_vectorStrContour.push_back(charcontor);

		//找charcontor的中心
		DWORD dwPointNum = 0;
		long dwSumX = 0;
		long dwSumY = 0;

		//m_vectorCharCenterPt
		int cnt = outline->GetContourCount();
		delete outline;

		for(int j=0; j<cnt; j++)
		{
			Nuclear::Contour ChContour = charcontor[j];
			int ptNum = ChContour.vectorPoint.size();
			for(int k=0; k<ptNum; k++)
			{
				dwSumX += ChContour.vectorPoint[k].x;
				dwSumY += ChContour.vectorPoint[k].y;
				dwPointNum++;
			}
		}
		int centx = (int)( dwSumX/(float)dwPointNum );
		int centy = (int)( dwSumY/(float)dwPointNum );
		POINT centPt;
		centPt.x = centx +5;
		centPt.y = centy;
		m_vectorCharCenterPt.push_back(centPt);

		POINT pt;
		pt.x = x0 +centx;
		pt.y = y0 +centy;
		m_vectorCharStartPt.push_back(pt);

		int rx = m_vectorCharCenterPt[i].x +30;
		int ry = -m_vectorCharCenterPt[i].y +20;
		CPoint pt1(pt.x-rx, pt.y-ry);
		CPoint pt2(pt.x-rx, pt.y+ry);
		CPoint pt3(pt.x+rx, pt.y+ry);
		CPoint pt4(pt.x+rx, pt.y-ry);

		RotatePoint(pt1, pt, m_fFontAngle);
		RotatePoint(pt2, pt, m_fFontAngle);
		RotatePoint(pt3, pt, m_fFontAngle);
		RotatePoint(pt4, pt, m_fFontAngle);

		CharContourRct4Point Rct4Point;
		Rct4Point.pt[0] = pt1;
		Rct4Point.pt[1] = pt2;
		Rct4Point.pt[2] = pt3;
		Rct4Point.pt[3] = pt4;
		m_vectorCharContourRct.push_back(Rct4Point); //字符外接矩形的四个点

 		x0 += glyphmetrics.gmCellIncX;  //control the next char position
 		x0 += m_nFontSpace;
 		y0 += glyphmetrics.gmCellIncY;

		m_fVectorFontAngle.push_back(m_fFontAngle);
	}

	pdc->SelectObject(pOldFont);
}

int CParticlePathDlg::CreateFullVectorCharStrPath()
{
	int i, j;
	int cnt = 0;

	m_vectorFullPathPoint.clear();
	int ncharNum = m_vectorStrContour.size();
	for(i=0; i<ncharNum; i++)
	{
		int x0 = m_vectorCharStartPt[i].x -m_vectorCharCenterPt[i].x;
		int y0 = m_vectorCharStartPt[i].y -m_vectorCharCenterPt[i].y;

		POINT centPt;
		centPt.x = m_vectorCharStartPt[i].x ;
		centPt.y = m_vectorCharStartPt[i].y ;

		CharContor Chcontor = m_vectorStrContour[i];
		int num = Chcontor.size();
		for(j=0; j<num; j++)
		{
			Nuclear::Contour contor = Chcontor[j];
			int ptNum = contor.vectorPoint.size();
			CPoint pt;
			POINT old_pt;

			pt.x = x0+contor.vectorPoint[0].x;
			pt.y = y0+contor.vectorPoint[0].y;
			RotatePoint(pt, centPt, m_fVectorFontAngle[i]);
		//	pt.x = (int)( pt.x * coefx );
		//	pt.y = (int)( pt.y * coefy );

			old_pt.x = pt.x;
			old_pt.y = pt.y;
			float min_dis = 20; //控制矢量字符路径上点的采样密度
			for(int a=0; a<ptNum; a++)
			{
				pt.x = x0+contor.vectorPoint[a].x;
				pt.y = y0+contor.vectorPoint[a].y;
				RotatePoint(pt, centPt, m_fVectorFontAngle[i]);
			//	pt.x = (int)( pt.x * coefx );
			//	pt.y = (int)( pt.y * coefy );


				float dis = ComputerDistance(pt, old_pt);
				if(dis<min_dis)
				{
					m_vectorFullPathPoint.push_back(pt);
				}
				else
				{
					int nstep = (int)(dis/min_dis);
					float fstep = min_dis/dis;
					CPoint pt0;
					for(int k=0; k<=nstep; k++)
					{
						pt0.x = (int)( pt.x *k*fstep + old_pt.x *(1-k*fstep) );
						pt0.y = (int)( pt.y *k*fstep + old_pt.y *(1-k*fstep) );
						m_vectorFullPathPoint.push_back(pt0);
					}
					if(nstep*fstep<0.999f)
					{
						m_vectorFullPathPoint.push_back(pt);
					}
				}
				old_pt.x = pt.x;
				old_pt.y = pt.y;
			}
		}
	}

	return cnt;
}


Nuclear::CTTFOutline* CParticlePathDlg::CreateOutline(CDC *pDC, int nchar, GLYPHMETRICS& gmm)
{
	MAT2 mat;
	mat.eM11.value = 1;		mat.eM11.fract = 0;
	mat.eM12.value = 0;		mat.eM12.fract = 0;
	mat.eM21.value = 0;		mat.eM21.fract = 0;
	mat.eM22.value = 1;		mat.eM22.fract = 0;

														//GGO_BITMAP //
	DWORD dwSize = GetGlyphOutline( pDC->m_hDC, nchar,  GGO_NATIVE, &gmm, 0, 0, &mat );
	if( dwSize == GDI_ERROR )
		return NULL;

	BYTE* pBuffer = new BYTE[dwSize];
											//GGO_BITMAP //
	if( GetGlyphOutline( pDC->m_hDC, nchar, GGO_NATIVE, &gmm, dwSize, pBuffer, &mat ) == GDI_ERROR ) 
	{
		delete pBuffer;
		return NULL;
	}

	TTPOLYGONHEADER* pHeader = (TTPOLYGONHEADER*)pBuffer;
	Nuclear::CTTFOutline* pOutline = new Nuclear::CTTFOutline;
	if( pOutline->Create( pHeader, dwSize ) )
	{
		delete pBuffer;
		return pOutline;
	}
	delete pBuffer;
	delete pOutline;
	return NULL;
}

void CParticlePathDlg::OnBnClickedButtonSetbkmap()
{
	CString strDefExt = L"jpg|bmp|allFile||";
	std::wstring strFileName = m_strSysPath + L"texture\\*.jpg";
	CString strFilter = L"(*.jpg)|*.jpg|(*.bmp)|*.bmp|(*.*)|*.*||";

	DWORD dwFlags = OFN_HIDEREADONLY|OFN_PATHMUSTEXIST;
	CFileDialog dlg(true, strDefExt, strFileName.c_str(), dwFlags, strFilter, NULL);

	dlg.DoModal();
	CString filePath = dlg.GetFileName();
	if( filePath.IsEmpty() )
		return;
	filePath = dlg.GetPathName();

	HBITMAP hBitmap;

	int pos = filePath.ReverseFind('.');
	int length = filePath.GetLength();
	CString exName = filePath.Right(length-pos-1);
	CString strBMP = L"bmp";
	CString strJPG = L"jpg";

	if(exName.CompareNoCase(strBMP) == 0)
		hBitmap = (HBITMAP)::LoadImage(::AfxGetInstanceHandle(), filePath,
										IMAGE_BITMAP, 0, 0, LR_LOADFROMFILE|LR_CREATEDIBSECTION);
	else
		hBitmap = LoadJPG(filePath);
	
	if( hBitmap != NULL )
	{
		m_bitmap.Detach();
		m_bitmap.Attach(hBitmap);
	}
	else
	{
		MessageBox(L"导入背景图失败!", L"提示", MB_OK);
	}

	m_rctMapPos = m_rctDrawPath;

	wchar_t workdir[MAX_PATH];
	::GetCurrentDirectory(MAX_PATH, workdir);
	CString iniFile = workdir;
	iniFile += L"\\ParticleSystemEditor.ini";

	CString strSection = L"ALL";
	CString strSectionKey = L"BKpicture";
	CString strValue = filePath;
	WritePrivateProfileString(strSection, strSectionKey, strValue, iniFile);

	Invalidate(true);

	//更新view的背景
	m_bBKchange = true;
}

void CParticlePathDlg::OnEnKillfocusEdit(UINT nID)
{
	// TODO: 在此添加控件通知处理程序代码
	UpdateData(true);
	m_fPathZoom = m_fPathZoom>2.0f ? 2.0f:m_fPathZoom;
	m_fPathZoom = m_fPathZoom<0.5f ? 0.5f:m_fPathZoom;

	m_fPathSpin = m_fPathSpin>360.0f ? 360.0f:m_fPathSpin;
	m_fPathSpin = m_fPathSpin<-360.0f ? -360.0f:m_fPathSpin;
	UpdateData(false);

	m_sliderPathZoom.SetPos((int)(m_fPathZoom*100));
	m_sliderPathSpin.SetPos((int)(m_fPathSpin));

	InvalidateRect(m_rctDrawPath, false);
}

void CParticlePathDlg::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	if( pScrollBar==NULL )
	{ //点击的是滚动条..
	}
	else
	{
		m_fPathZoom = m_sliderPathZoom.GetPos() *0.01f;
		m_fPathSpin = (float)(m_sliderPathSpin.GetPos());
		UpdateData(false);

		InvalidateRect(m_rctDrawPath, false);
	}

	CDialog::OnHScroll(nSBCode, nPos, pScrollBar);
}

HBITMAP CParticlePathDlg::LoadJPG(LPCTSTR filepath)
{
	IPicture* p = NULL;
	IStream* s = NULL; 
	HGLOBAL hG;
	void* pp = NULL;
	FILE* fp = NULL;
 	//Read file in memory
	_tfopen_s(&fp, filepath, L"rb");
	if( fp==NULL )
		return NULL;

	fseek(fp, 0, SEEK_END);
	int fs = ftell(fp);
	fseek(fp, 0, SEEK_SET);
	hG = GlobalAlloc(GPTR, fs);
	if(hG == NULL)
	{
		fclose(fp);
		return NULL;
	}
	pp = (void*)hG;
	fread(pp, 1, fs, fp);
	fclose(fp);

	CreateStreamOnHGlobal(hG, false, &s);
	if(s == NULL)
	{ 
		GlobalFree(hG);
		return NULL; 
	}

	OleLoadPicture(s, 0, false, IID_IPicture, (void**)&p);

	if(p == NULL)  
	{
		s->Release();
		GlobalFree(hG);
		return NULL;
	}

	s->Release();
	GlobalFree(hG);

	HBITMAP hB = NULL;  
	p->get_Handle( (unsigned int*)&hB );  

	//Copy the image. Necessary, because upon p's release,
	//the handle is destroyed.
	HBITMAP hBB = (HBITMAP)CopyImage(hB, IMAGE_BITMAP, 0, 0, LR_COPYRETURNORG);

	p->Release();
	return hBB;
}

void CParticlePathDlg::OnCbnSelchangeComboPathmirrortype()
{
	m_nPathMirrorType = m_comboPathMirrorType.GetCurSel();
	InvalidateRect(m_rctDrawPath, false);
}

void CParticlePathDlg::OnBnClickedCheckCurveSample()
{
	UpdateData(TRUE);
	if(m_bCurveSample)
	{
		m_bVectorCharPath = FALSE;
	}
	UpdateData(false);
}

void CParticlePathDlg::OnMenuKeypoint()
{
	BOOL bKeyPoint = m_vectorPathPoint[m_curSelPointIndex].bKeyPoint;
	bKeyPoint = !bKeyPoint;
	m_vectorPathPoint[m_curSelPointIndex].bKeyPoint = bKeyPoint;

	m_bhasKeyPoint = TRUE;
 
 	InvalidateRect(&m_rctDrawPath, false);
}
