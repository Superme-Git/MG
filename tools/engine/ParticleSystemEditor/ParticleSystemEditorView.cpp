// ParticleSystemEditorView.cpp : CParticleSystemEditorView 类的实现
//

#include "stdafx.h"
#include <d3dx9.h>
#include "ParticleSystemEditor.h"
#include "ParticleSystemEditorDoc.h"
#include "ParticleSystemEditorView.h"
#include "PictureTGA.h"

#include "MainFrm.h"

#include "ParameterAdjustDlg.h"
#include "PictureTGA.h"
#include "OgreDDSCodec.h"

#include <assert.h>
#include <mmsystem.h>

#include "..\engine\common\pngwriter.h"
#include "..\engine\common\util.h"
#include "..\engine\common\pfsutil.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

static CParticleSystemEditorView* msInstance = NULL;

IMPLEMENT_DYNCREATE(CParticleSystemEditorView, CView)

BEGIN_MESSAGE_MAP(CParticleSystemEditorView, CView)
	ON_WM_TIMER()
	ON_WM_SIZE()
	ON_WM_LBUTTONDOWN()
	ON_WM_MOUSEMOVE()
	ON_WM_RBUTTONDOWN()
	ON_COMMAND(ID_MENU_PROPERTIES, &CParticleSystemEditorView::OnMenuProperties)
	ON_WM_SIZING()
	ON_WM_KEYDOWN()
	ON_WM_RBUTTONUP()
	ON_WM_MBUTTONDOWN()
	ON_WM_MBUTTONUP()
	ON_WM_LBUTTONDBLCLK()
	ON_WM_LBUTTONUP()

	ON_COMMAND(ID_BUTTON_OPENPSL, &CParticleSystemEditorView::OnOpenPsl)
	ON_COMMAND(ID_BUTTON_SAVEPSL, &CParticleSystemEditorView::OnSavePsl)
	ON_COMMAND(ID_BUTTON_SAVEASPSL, &CParticleSystemEditorView::OnSaveasPsl)

	ON_COMMAND(ID_BTN_TGA2DDS, &CParticleSystemEditorView::OnBtnTga2dds)
END_MESSAGE_MAP()

// CParticleSystemEditorView 构造/析构
void TGA2DDS(CString path);
size_t utf16toutf8(const wchar_t* u16, size_t size, char* u8, size_t outBufferSize);

void CParticleSystemEditorView::OnOpenPsl()
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return;

	pDoc->GetParticleParameterView()->OnBnClickedButtonOpenparticlesys();
}

void CParticleSystemEditorView::OnSavePsl()
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return;

	pDoc->GetParticleParameterView()->OnBnClickedButtonSavemodifypsl();
}

void CParticleSystemEditorView::OnSaveasPsl()
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return;

	pDoc->GetParticleParameterView()->OnBnClickedButtonSaveparticlesys();
}

CParticleSystemEditorView::CParticleSystemEditorView():
m_voronoi(300, 800, 600)
{
	msInstance = this;

	m_bDeviceLost = FALSE;
	m_bTrackFx = false;

	m_nMouseMoveCnt = 0;
	m_vectorD3DTexture.clear();

	m_pRenderer = NULL;
	m_pParSysManager = NULL;
	m_hBKpicHandle = Nuclear::INVALID_PICTURE_HANDLE;
	m_bShowBKpic = true;
	m_bMoveMap = false;
	m_bBlindArea = false;
	m_bSetEmitterSpinCentFlag = FALSE;
	m_bSetParticleHoleFlag = FALSE;
	m_bSetMaxBox = FALSE;

	m_bBlockLine = FALSE;
	m_bReflectLine = FALSE;

	m_FrctMapPos = Nuclear::FRECT();
	m_initialPoint = CPoint(0, 0);
	m_ViewPort = Nuclear::CRECT(0, 0, GetSystemMetrics(SM_CXSCREEN), GetSystemMetrics(SM_CYSCREEN));
	m_rctMaxBox = CRect(0, 0, 0, 0);
	//m_psTextureInfo

	m_pD3dTextureShapes = NULL;

	m_bStop = false;
	m_bPologyOk = false;
	m_bSetShapeTexture = false;
	m_hShapePicHandle = Nuclear::INVALID_PICTURE_HANDLE;

	m_ptLastPos = CPoint(0, 0);
	m_nMouseDownIndex = -1;
	m_bPologyModify = false;
	m_nVertexType = 0;

	m_nVertexRow = 20; //0-----40 //转场特效的前景拆分（四边形和三角形）
	m_nVertexCol = 20;
	m_nShapeTextureWidth = 800;
	m_nShapeTextureHeight = 600;
	m_nLineAngle = 60;

	m_fLastTime = (float)timeGetTime();

//	CMainFrame* pMainWnd = reinterpret_cast<CMainFrame*>(theApp.GetMainWnd());
//	pMainWnd->ShowToolBars();
}

CParticleSystemEditorView::~CParticleSystemEditorView()
{
	//	SAFE_RELEASE(m_psTextureInfo.m_pD3DTexture);
	bool bres = m_pRenderer->FreePicture(m_hShapePicHandle);

	if(m_pD3dTextureShapes != Nuclear::INVALID_PICTURE_HANDLE)
		m_pRenderer->FreePicture(m_pD3dTextureShapes);
	//以上2个应该是同一个,需要合并修改

	ReleaseVectorD3Dtexture(m_vectorD3DTexture);
 	 bres = m_pRenderer->FreePicture(m_hBKpicHandle);
	 if(bres)
		OutputDebugString(L"CParticleSystemEditorView--FreePicture(m_hBKpicHandle) succ\n");
	DestroyRenderer(m_pRenderer); //psManager在这里面释放了..
	//HRESULT hr = DestroyRenderer(m_pRenderer); //psManager在这里面释放了..
	//if( SUCCEEDED(hr) )
	//	OutputDebugString(L"CParticleSystemEditorView--DestroyRenderer(m_pRenderer) succ\n");

//	SAFE_RELEASE(m_pD3dTextureShapes);


//	CMainFrame* pMainWnd = dynamic_cast<CMainFrame*>(theApp.GetMainWnd());
//	pMainWnd->HideToolBars();

	msInstance = NULL;
}

CParticleSystemEditorView* CParticleSystemEditorView::getInstance()
{
	return msInstance;
}

void CParticleSystemEditorView::ReleaseVectorD3Dtexture(std::vector<Nuclear::PictureHandle> &vectorD3DTexture)
{
	int cnt = (int)vectorD3DTexture.size();
	for(int i=0; i<cnt; i++)
		m_pRenderer->FreePicture(vectorD3DTexture[i]);
	vectorD3DTexture.clear();
}

BOOL CParticleSystemEditorView::PreCreateWindow(CREATESTRUCT& cs)
{
	// TODO: 在此处通过修改
	//  CREATESTRUCT cs 来修改窗口类或样式

	return CView::PreCreateWindow(cs);
}

// CParticleSystemEditorView 绘制

void CParticleSystemEditorView::OnDraw(CDC* /*pDC*/)
{
	Render();
	// TODO: 在此处为本机数据添加绘制代码
}

void CParticleSystemEditorView::Render()
{
	if( !(theApp.m_bUpdateParticleSys) )
	{
		return;
	}

	HRESULT hr(S_FALSE);
	if(m_pRenderer == NULL)
		return;

	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return;

	Nuclear::ParticleSystemHandle handle = pDoc->GetParticleParameterView()->GetCurrentPShandle();
	if(handle == Nuclear::INVALID_PS_HANDLE)
	{
//		handle = 1;
		return;
	}

	Nuclear::CParticleSystem* pParticleSys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticleSys == NULL)
	{
		MessageBox(L"逻辑错误, CParticleSystemEditorView::Render()\n");
		return;
	}
	Nuclear::XHardRef<Nuclear::PSL> ppsl = pParticleSys->GetPSL();

	float fCurTime = (float)timeGetTime();
	float timeDelta = (fCurTime - m_fLastTime) *0.001f;
	m_fLastTime = fCurTime;

//	if(m_bStop) //这个参数没用了..直接调用PS::SetStopFlag()在update的时候处理 //2009-9-18
//		timeDelta = 0.0f;

	if(timeDelta > 0.5f)//
		timeDelta = 0.0f;

	bool bres = m_pParSysManager->UpdateParticleSystem(handle, timeDelta);

	DWORD bkColor = pDoc->GetParticleParameterView()->GetBKColor();
	m_pRenderer->Begin(bkColor); //0xf0a0b09a
	if(m_bShowBKpic && m_hBKpicHandle!=Nuclear::INVALID_PICTURE_HANDLE)
		m_pRenderer->DrawPicture(m_hBKpicHandle, m_FrctMapPos, 0xffffffff);

	if(ppsl->m_nInterfacePsl == 1) //界面特效---把界面的中心标记出来
	{
		float dx = (m_FrctMapPos.left + m_FrctMapPos.right)/2.0f;
		float dy = (m_FrctMapPos.top + m_FrctMapPos.bottom)/2.0f;
		m_pRenderer->DrawCircle(dx, dy, 6.0f, 0xff00ff00, Nuclear::XPFM_SOLIDFILL);
		m_pRenderer->DrawCircle(dx, dy, 3.0f, 0xff000000, Nuclear::XPFM_WIREFRAME);
	}

	CRect rct;
	GetClientRect(&rct);
	Nuclear::CRECT viewRct;
	viewRct.left = rct.left;
	viewRct.top = rct.top;
	viewRct.right = rct.right;
	viewRct.bottom = rct.bottom;

	m_pParSysManager->OnViewportUpdate(viewRct); //m_ViewPort
	if(ppsl->m_nAbsrtactPsl == 1)
	{
		if(m_bPologyOk)
			m_pParSysManager->RenderParticleSystem(handle, true);
	}
	else
		m_pParSysManager->RenderParticleSystem(handle, true);

	const Nuclear::PSParameter& psParameter = pParticleSys->GetPSParameter();
	Nuclear::FPOINT pt = ppsl->m_pointEmitterSpinCent *ppsl->m_fPSLzoom + psParameter.m_psModifyParam.m_pointCent;

	m_pRenderer->DrawCircle(pt.x, pt.y, 3.0f, 0xffff0000, Nuclear::XPFM_WIREFRAME);

	float holex = ppsl->m_pointParticleHole.x *ppsl->m_fPSLzoom;
	float holey = ppsl->m_pointParticleHole.y *ppsl->m_fPSLzoom;
	float fangle = psParameter.m_psModifyParam.m_fEmitterSpinAngle +Nuclear::TORADIANS(ppsl->m_fEmitterSpin);
		
	pt = Nuclear::FPOINT(holex*cos(fangle) - holey*sin(fangle), holex*sin(fangle) + holey*cos(fangle))
																+ psParameter.m_psModifyParam.m_pointCent;

	Nuclear::FRECT line = Nuclear::FRECT(pt.x-3.0f, pt.y-3.0f, pt.x+4.0f, pt.y+4.0f);
	m_pRenderer->DrawLine(line, 0xffff0000);
	line = Nuclear::FRECT(pt.x-3.0f, pt.y+3.0f, pt.x+4.0f, pt.y-4.0f);
	m_pRenderer->DrawLine(line, 0xffff0000);

	Nuclear::FPOINT cent0 = psParameter.m_psModifyParam.m_pointCent;
	if(ppsl->m_nMoveType != Nuclear::PSLMOVETYPE_FULLSCREEN && ppsl->m_nAbsrtactPsl != 1)
	{
		Nuclear::FRECT rect;
		rect.left = (float)m_rctMaxBox.left;
		rect.top = (float)m_rctMaxBox.top;
		rect.right = (float)m_rctMaxBox.right;
		rect.bottom = (float)m_rctMaxBox.bottom;
		m_pRenderer->DrawBox(rect, 0xff0080ff, Nuclear::XPFM_WIREFRAME);

		rect = ppsl->m_rctMaxBox.ToFRECT();
		rect += cent0;
		m_pRenderer->DrawBox(rect, 0xff0000ff, Nuclear::XPFM_WIREFRAME);

		int nSize = (int)(psParameter.m_psModifyParam.m_vectorRctBlindArea.size());
		for(int i=0; i<nSize; i++)
		{
			Nuclear::CRECT rct = psParameter.m_psModifyParam.m_vectorRctBlindArea[i];
			Nuclear::FRECT frect = rct.ToFRECT();
			frect += cent0;
			m_pRenderer->DrawBox(frect, 0xff00ffff, Nuclear::XPFM_WIREFRAME);
		}

		Nuclear::FRECT frect = Nuclear::FRECT((float)m_InitBoxPoint.x, (float)m_InitBoxPoint.y, (float)m_oldBoxPoint.x, (float)m_oldBoxPoint.y);

		if(m_bBlockLine || m_bReflectLine)
			m_pRenderer->DrawLine(frect, 0xffafaf00);

		nSize = (int)(psParameter.m_psModifyParam.m_vectorBlockLine.size());
		for(int i=0; i<nSize; i++)
		{
			Nuclear::FPOINT pt0 = psParameter.m_psModifyParam.m_vectorBlockLine[i].pt0.ToFPOINT();
			Nuclear::FPOINT pt1 = psParameter.m_psModifyParam.m_vectorBlockLine[i].pt1.ToFPOINT();
			frect = Nuclear::FRECT(pt0.x, pt0.y, pt1.x, pt1.y);
			frect += cent0;
			m_pRenderer->DrawLine(frect, 0xffff0000);
		}

		nSize = (int)(psParameter.m_psModifyParam.m_vectorReflectLine.size());
		for(int i=0; i<nSize; i++)
		{
			Nuclear::FPOINT pt0 = psParameter.m_psModifyParam.m_vectorReflectLine[i].pt0.ToFPOINT();
			Nuclear::FPOINT pt1 = psParameter.m_psModifyParam.m_vectorReflectLine[i].pt1.ToFPOINT();
			frect = Nuclear::FRECT(pt0.x, pt0.y, pt1.x, pt1.y);
			frect += cent0;
			m_pRenderer->DrawLine(frect, 0xffffff00);
		}

	}

	if(ppsl->m_nAbsrtactPsl==1)
	{
		if(m_pD3dTextureShapes != NULL) // !m_bPologyOk && 
		{
			//绘制前景图
			Nuclear::FRECT frct;
			Nuclear::PictureInfo picinfo;
			bool bres = m_pRenderer->GetPictureInfo(m_hShapePicHandle, picinfo);
			float left = 0.0f;
			float top = 0.0f;
			float right = left + picinfo.m_nPicWidth;
			float bottom = top + picinfo.m_nPicHeight;
			frct = Nuclear::FRECT(left, top, right, bottom);

			if(!m_bPologyOk)
				m_pRenderer->DrawPicture(m_hShapePicHandle, frct, 0xffffffff);

			//
			//绘制抽象粒子特效的分块..
			if(!m_bPologyOk)
			{
				switch(m_nVertexType)
				{
				case 0: //自定义
					{
						voronoi::VectorTessellation tessellations = m_voronoi.getTessellations();
						int cnt = (int)tessellations.size();
						for(int i=0; i<cnt; i++)
						{
							voronoi::Tessellation tess = tessellations[i];
							voronoi::Point center = tess.center;	// 镶嵌中心
							int vertexNum = (int)tess.polygon.size();
							for(int j=0; j<vertexNum; j++)
							{
								int k = (j+1)%vertexNum;
								voronoi::Point pt1 = tess.polygon[j];
								voronoi::Point pt2 = tess.polygon[k];
								//绘制线...
								float x1 = (float)pt1.x;
								float y1 = (float)pt1.y;
								float x2 = (float)pt2.x;
								float y2 = (float)pt2.y;

								line = Nuclear::FRECT(x1, y1, x2, y2);

								m_pRenderer->DrawLine(line, 0xffffff00);
							}
							m_pRenderer->DrawCircle((float)center.x, (float)center.y, 3.0f, 0xffff00ff, Nuclear::XPFM_SOLIDFILL);
						}
					}
					break;
				case 1: //矩形
					{
						ComputeShapeVertex();
						int cnt = (int)m_shapeList.size();
						for(int i=0; i<cnt; i++)
						{
							Nuclear::SHAPE shape;
							shape = m_shapeList[i];
							Nuclear::FPOINT center;

							center.x = shape.center.u *picinfo.m_nPicWidth;
							center.y = shape.center.v *picinfo.m_nPicHeight;

							Nuclear::FPOINT lt;
							Nuclear::FPOINT rb;

							lt.x = shape.vertexs[0].u *picinfo.m_nPicWidth;
							lt.y = shape.vertexs[0].v *picinfo.m_nPicHeight;
							rb.x = shape.vertexs[2].u *picinfo.m_nPicWidth;
							rb.y = shape.vertexs[2].v *picinfo.m_nPicHeight;

							Nuclear::FRECT frct(lt, rb);						
							m_pRenderer->DrawBox(frct, 0xffffff00, Nuclear::XPFM_WIREFRAME);
							m_pRenderer->DrawCircle((float)center.x, (float)center.y, 3.0f, 0xffff00ff, Nuclear::XPFM_SOLIDFILL);
						}
					}
					break;
				case 2: //三角形
					{
						ComputeShapeVertex();
						int cnt = (int)m_shapeList.size();
						for(int i=0; i<cnt; i++)
						{
							Nuclear::SHAPE shape;
							shape = m_shapeList[i];
							Nuclear::FPOINT center;

							center.x = shape.center.u *picinfo.m_nPicWidth;
							center.y = shape.center.v *picinfo.m_nPicHeight;

							Nuclear::FPOINT pt0;
							Nuclear::FPOINT pt1;
							Nuclear::FPOINT pt2;

							pt0.x = shape.vertexs[0].u *picinfo.m_nPicWidth;
							pt0.y = shape.vertexs[0].v *picinfo.m_nPicHeight;
							pt1.x = shape.vertexs[1].u *picinfo.m_nPicWidth;
							pt1.y = shape.vertexs[1].v *picinfo.m_nPicHeight;
							pt2.x = shape.vertexs[2].u *picinfo.m_nPicWidth;
							pt2.y = shape.vertexs[2].v *picinfo.m_nPicHeight;
					
							m_pRenderer->DrawLine(pt0.x, pt0.y, pt1.x, pt1.y, 0xffffff00);
							m_pRenderer->DrawLine(pt0.x, pt0.y, pt2.x, pt2.y, 0xffffff00);
							m_pRenderer->DrawLine(pt2.x, pt2.y, pt1.x, pt1.y, 0xffffff00);
							m_pRenderer->DrawCircle((float)center.x, (float)center.y, 3.0f, 0xffff00ff, Nuclear::XPFM_SOLIDFILL);
						}
					}
				    break;

				case 3: //2块。。。
					{
						ComputeShapeVertex();
						int cnt = (int)m_shapeList.size();
						for(int i=0; i<cnt; i++)
						{
							Nuclear::SHAPE shape;
							shape = m_shapeList[i];
							Nuclear::FPOINT center;

							center.x = shape.center.u *picinfo.m_nPicWidth;
							center.y = shape.center.v *picinfo.m_nPicHeight;

							std::vector<Nuclear::CPOINT> pts;
							for(int j=0; j<4; j++)
							{
								Nuclear::CPOINT pt;
								pt.x = (int)(shape.vertexs[j].u *picinfo.m_nPicWidth);
								pt.y = (int)(shape.vertexs[j].v *picinfo.m_nPicHeight);
								pts.push_back(pt);
							}

							m_pRenderer->DrawPolygon(pts, 0xffffff00, Nuclear::XPFM_WIREFRAME);

							m_pRenderer->DrawCircle((float)center.x, (float)center.y, 3.0f, 0xffff00ff, Nuclear::XPFM_SOLIDFILL);
						}
					}
					break;

				default:
				    break;
				}
			}
		}
	}


	int nSpinAngle = (int)( Nuclear::TOANGLE(psParameter.m_psModifyParam.m_fEmitterSpinAngle) +ppsl->m_fEmitterSpin +0.5f);
	float fcosSpinAngle = Nuclear::XPCOS(nSpinAngle);
	float fsinSpinAngle = Nuclear::XPSIN(nSpinAngle);
	float fPslZoom = ppsl->m_fPSLzoom * psParameter.m_psModifyParam.m_fEmissionScale;

	switch(ppsl->m_nPosType)
	{
	case Nuclear::PSLTYPE_POINT:
		{
			Nuclear::FPOINT pt0 = psParameter.m_psModifyParam.m_pointCent;
			Nuclear::FPOINT pt = ppsl->m_pointEmitterSpinCent *fPslZoom + pt0;
			pt0 -= pt;
			float x0 = pt0.x;
			float y0 = pt0.y;

			pt0.x = x0*fcosSpinAngle - y0*fsinSpinAngle;
			pt0.y = x0*fsinSpinAngle + y0*fcosSpinAngle;

			pt0 += pt;

		//	m_pRenderer->DrawEllipse(pt0.x, pt0.y, 3, 3, 0xffffff00, Nuclear::XPFM_SOLIDFILL);

		}
		break;
	case Nuclear::PSLTYPE_LINE:
		{
			float x = ppsl->m_point1.x - ppsl->m_point0.x;
			float y = ppsl->m_point1.y - ppsl->m_point0.y;
			x /= 2.0f;
			y /= 2.0f;

			x *= fPslZoom;
			y *= fPslZoom;

			if(psParameter.m_psModifyParam.m_fLineLength > 0)
			{ //文字背景特效
				if( fabs(x)>10.0f && fabs(y)<3.0f ) //视为水平方向的线
				{
					x = psParameter.m_psModifyParam.m_fLineLength/2.0f;
					y = 0;
				}
			}

			Nuclear::FPOINT pt0 = Nuclear::FPOINT(-x, -y) +psParameter.m_psModifyParam.m_pointCent;
			Nuclear::FPOINT pt1 = Nuclear::FPOINT(x, y) +psParameter.m_psModifyParam.m_pointCent;

			Nuclear::FPOINT pt = ppsl->m_pointEmitterSpinCent *fPslZoom + psParameter.m_psModifyParam.m_pointCent;
			pt0 -= pt;
			pt1 -= pt;
			float x0 = pt0.x;
			float y0 = pt0.y;
			float x1 = pt1.x;
			float y1 = pt1.y;

			pt0.x = x0*fcosSpinAngle - y0*fsinSpinAngle;
			pt0.y = x0*fsinSpinAngle + y0*fcosSpinAngle;
			pt1.x = x1*fcosSpinAngle - y1*fsinSpinAngle;
			pt1.y = x1*fsinSpinAngle + y1*fcosSpinAngle;

			pt0 += pt;
			pt1 += pt;

			Nuclear::FRECT frct = Nuclear::FRECT(pt0, pt1);
			m_pRenderer->DrawLine(frct, 0xffffff00);
		}
		break;
	case Nuclear::PSLTYPE_CIRCLE:
		{
			float rx = (float)ppsl->m_nSysPosRadiusX *ppsl->m_fPSLzoom;
			float ry = (float)ppsl->m_nSysPosRadiusY *ppsl->m_fPSLzoom;
			float x0 = psParameter.m_psModifyParam.m_pointCent.x;
			float y0 = psParameter.m_psModifyParam.m_pointCent.y;

			Nuclear::FPOINT pt0 = Nuclear::FPOINT(x0, y0);
			Nuclear::FPOINT pt = ppsl->m_pointEmitterSpinCent *fPslZoom + psParameter.m_psModifyParam.m_pointCent;
			pt0 -= pt;
			x0 = pt0.x;
			y0 = pt0.y;

			pt0.x = x0*fcosSpinAngle - y0*fsinSpinAngle;
			pt0.y = x0*fsinSpinAngle + y0*fcosSpinAngle;

			pt0 += pt;
			x0 = pt0.x;
			y0 = pt0.y;

			m_pRenderer->DrawEllipse(x0, y0, rx, ry, 0xffffff00, Nuclear::XPFM_WIREFRAME, Nuclear::TOANGLE(fangle));
		}
	    break;
	case Nuclear::PSLTYPE_RECT:
		{
			float fx = 1.0f; //放大缩小倍数
			float fy = 1.0f;

			if(psParameter.m_psModifyParam.m_fRctWidth>0 && psParameter.m_psModifyParam.m_fRctHeight>0) //文字背景特效
			{
				fx = psParameter.m_psModifyParam.m_fRctWidth/(float)(ppsl->m_rct.Width());
				fy = psParameter.m_psModifyParam.m_fRctHeight/(float)(ppsl->m_rct.Height());
			}

			float left = (float)ppsl->m_rct.left  *ppsl->m_fPSLzoom *fx;
			float top = (float)ppsl->m_rct.top *ppsl->m_fPSLzoom *fy;
			float right = (float)ppsl->m_rct.right *ppsl->m_fPSLzoom *fx;
			float bottom = (float)ppsl->m_rct.bottom *ppsl->m_fPSLzoom *fy;

			Nuclear::FPOINT pt = ppsl->m_pointEmitterSpinCent *fPslZoom + psParameter.m_psModifyParam.m_pointCent;

			Nuclear::FPOINT pt0 = Nuclear::FPOINT(left, top) +psParameter.m_psModifyParam.m_pointCent;
			pt0 -= pt;
			float x0 = pt0.x;
			float y0 = pt0.y;
			pt0.x = x0*fcosSpinAngle - y0*fsinSpinAngle;
			pt0.y = x0*fsinSpinAngle + y0*fcosSpinAngle;
			pt0 += pt;

			Nuclear::FPOINT pt1 = Nuclear::FPOINT(left, bottom) +psParameter.m_psModifyParam.m_pointCent;
			pt1 -= pt;
			x0 = pt1.x;
			y0 = pt1.y;
			pt1.x = x0*fcosSpinAngle - y0*fsinSpinAngle;
			pt1.y = x0*fsinSpinAngle + y0*fcosSpinAngle;
			pt1 += pt;

			Nuclear::FPOINT pt2 = Nuclear::FPOINT(right, bottom) +psParameter.m_psModifyParam.m_pointCent;
			pt2 -= pt;
			x0 = pt2.x;
			y0 = pt2.y;
			pt2.x = x0*fcosSpinAngle - y0*fsinSpinAngle;
			pt2.y = x0*fsinSpinAngle + y0*fcosSpinAngle;
			pt2 += pt;

			Nuclear::FPOINT pt3 = Nuclear::FPOINT(right, top) +psParameter.m_psModifyParam.m_pointCent;
			pt3 -= pt;
			x0 = pt3.x;
			y0 = pt3.y;
			pt3.x = x0*fcosSpinAngle - y0*fsinSpinAngle;
			pt3.y = x0*fsinSpinAngle + y0*fcosSpinAngle;
			pt3 += pt;

			m_pRenderer->DrawLine(pt0.x, pt0.y, pt1.x, pt1.y, 0xffffff00);
			m_pRenderer->DrawLine(pt1.x, pt1.y, pt2.x, pt2.y, 0xffffff00);
			m_pRenderer->DrawLine(pt2.x, pt2.y, pt3.x, pt3.y, 0xffffff00);
			m_pRenderer->DrawLine(pt0.x, pt0.y, pt3.x, pt3.y, 0xffffff00);
		}
		break;
	case Nuclear::PSLTYPE_CUSTOMPATH:
		break;
	default:
	    break;
	}

	m_pRenderer->End();
}

void CParticleSystemEditorView::OnInitialUpdate()
{
	CView::OnInitialUpdate();

	if( !InitD3D() )
	{
		MessageBox(L"InitD3D() FAILED!");
		return;
	}

	//测试代码...初始化抽象粒子顶点数据..
#if 0
	m_shapeList.clear();
	int nWidth = 1024;
	int nHeight = 768;
	int cnt = 20;
	float dx = (float)nWidth/cnt;
	float dy = (float)nHeight/cnt;
	for(int i=0; i<cnt; i++)
	{
		for(int j=0; j<cnt; j++)
		{
			Nuclear::SHAPE shape;
			shape.center.x = dx*j +dx/2;
			shape.center.y = dy*i +dy/2;
			shape.center.u = (dx*j +dx/2)/nWidth;
			shape.center.v = (dy*i +dy/2)/nHeight;

			Nuclear::VERTEX_LIST vertexs;
			Nuclear::Vertex pt;
			pt.x = -dx/2;
			pt.y = -dy/2;
			pt.u = (shape.center.x +pt.x)/nWidth;
			pt.v = (shape.center.y +pt.y)/nHeight;
			pt.u = pt.u<0.0f ? 0.0f: (pt.u>1.0f ? 1.0f:pt.u);
			pt.v = pt.v<0.0f ? 0.0f: (pt.v>1.0f ? 1.0f:pt.v);
			vertexs.push_back(pt);

			pt.x = -dx/2;
			pt.y = dy/2;
			pt.u = (shape.center.x +pt.x)/nWidth;
			pt.v = (shape.center.y +pt.y)/nHeight;
			pt.u = pt.u<0.0f ? 0.0f: (pt.u>1.0f ? 1.0f:pt.u);
			pt.v = pt.v<0.0f ? 0.0f: (pt.v>1.0f ? 1.0f:pt.v);
			vertexs.push_back(pt);

			pt.x = dx/2;
			pt.y = dy/2;
			pt.u = (shape.center.x +pt.x)/nWidth;
			pt.v = (shape.center.y +pt.y)/nHeight;
			pt.u = pt.u<0.0f ? 0.0f: (pt.u>1.0f ? 1.0f:pt.u);
			pt.v = pt.v<0.0f ? 0.0f: (pt.v>1.0f ? 1.0f:pt.v);
			vertexs.push_back(pt);

			pt.x = dx/2;
			pt.y = -dy/2;
			pt.u = (shape.center.x +pt.x)/nWidth;
			pt.v = (shape.center.y +pt.y)/nHeight;
			pt.u = pt.u<0.0f ? 0.0f: (pt.u>1.0f ? 1.0f:pt.u);
			pt.v = pt.v<0.0f ? 0.0f: (pt.v>1.0f ? 1.0f:pt.v);
			vertexs.push_back(pt);
			shape.vertexs = vertexs;

			shape.center.x -= nWidth/2.0f;
			shape.center.y -= nHeight/2.0f;

			m_shapeList.push_back(shape);
		}
	}
	//
	//初始化参数...

	LPDIRECT3DTEXTURE9 texShapes = NULL;
	CString strTexPath = theApp.GetResPath() + L"res\\effect\\particle\\texture\\shapesTexture.bmp";

	D3DXIMAGE_INFO pSrcInfo;
	HRESULT hr = D3DXCreateTextureFromFileEx(m_pD3D9device, strTexPath, 
		D3DX_DEFAULT_NONPOW2, D3DX_DEFAULT_NONPOW2, 1, 0,
		D3DFMT_A8R8G8B8,
		D3DPOOL_MANAGED,
		D3DX_FILTER_NONE,
		D3DX_FILTER_NONE,
		0, &pSrcInfo, NULL, &texShapes);

	if( FAILED(hr) ) 
	{
		CString str = strTexPath + L" CParticleSystemEditorView::OnInitialUpdate()--FAILED(hr)";
		MessageBox(str);
		XPASSERT(0);
	}

//	hr = D3DXSaveTextureToFile(L"D:\\shapeListsTexture.bmp", D3DXIFF_BMP, texShapes, NULL);

	m_pD3dTextureShapes = new Nuclear::Direct9Texture(); //在particleManager里面删除..
	m_pD3dTextureShapes->m_pTexture = texShapes;

#endif

	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

 	Nuclear::PSParameter psParameter;
//	psParameter.m_psModifyParam.m_pointCent.x = pSrcInfo.Width/2.0f;
//	psParameter.m_psModifyParam.m_pointCent.y = pSrcInfo.Height/2.0f;

	std::wstring defaultPsl = L"default.";
	defaultPsl += FNExt_ParticleWS;
	Nuclear::ParticleSystemHandle psHandle = m_pParSysManager->CreateParticleSystem(defaultPsl, psParameter);
	if(psHandle != Nuclear::INVALID_PS_HANDLE)
	{
		pDoc->GetParticleParameterView()->SetParticleSys(m_pParSysManager->GetParticleSysByHandle(psHandle), psHandle);
	}

//	m_pParSysManager->SetParticleDraw(m_pParticleDraw);
//	m_pParSysManager->SetShapeList(psHandle, m_shapeList, m_pD3dTextureShapes);

	//背景图片信息在这里设置..
	CString strBKpicFilePath = L"/effect/particle/texture/bkground.bmp";
	wchar_t workdir[MAX_PATH];
	::GetCurrentDirectory(MAX_PATH, workdir);
	CString filePath = workdir;
	filePath += L"\\ParticleSystemEditor.ini";
	CString strSection = L"ALL";
	CString strSectionKey = L"BKpicture";
	TCHAR value[256];
	std::wstring outFile;
	GetPrivateProfileString(strSection, strSectionKey, strBKpicFilePath, value, 256, filePath);
	PFS::CEnv::NativeFileNameToPFSFileName(value, outFile);
	m_hBKpicHandle = m_pRenderer->LoadPicture(outFile);

	Nuclear::PictureInfo picinfo;
	bool bres = false;
	m_FrctMapPos = Nuclear::FRECT(0.0f, 0.0f, 300.0f, 300.0f);
	if(m_hBKpicHandle != Nuclear::INVALID_PICTURE_HANDLE)
	{
		bres = m_pRenderer->GetPictureInfo(m_hBKpicHandle, picinfo);
		if(bres)
		{
			float left = 0.0f;
			float top = 0.0f;
			float right = left + picinfo.m_nPicWidth;
			float bottom = top + picinfo.m_nPicHeight;
			m_FrctMapPos = Nuclear::FRECT(left, top, right, bottom);
		}
	}

	SetTimer(1, 500, NULL);//统计fps
	SetTimer(2, 30, NULL); //render()	
}

void CParticleSystemEditorView::ComputeShapeVertex()
{ 
	//赋值m_shapeList
	Nuclear::PictureInfo picinfo;
	bool bres = m_pRenderer->GetPictureInfo(m_hShapePicHandle, picinfo);
	if(!bres)
	{
		MessageBox(L"还没设置前景地图\n");
		return;
	}

	float fWidth = (float)picinfo.m_nPicWidth;
	float fHeight = (float)picinfo.m_nPicHeight;

	m_nShapeTextureWidth = picinfo.m_nPicWidth;
	m_nShapeTextureHeight = picinfo.m_nPicHeight;

	switch(m_nVertexType)
	{
	case 0:
		{
			if(1) //m_shapeList.empty()
			{
				voronoi::VectorTessellation tessellations = m_voronoi.getTessellations();
				int cnt = (int)tessellations.size();
				for(int i=0; i<cnt; i++)
				{
					Nuclear::SHAPE shape;
					Nuclear::VERTEX_LIST vertexs;

					voronoi::Tessellation tess = tessellations[i];
					voronoi::Point center = tess.center;	// 镶嵌中心

					shape.center.u = center.x/fWidth;
					shape.center.v = center.y/fHeight;

					int vertexNum = (int)tess.polygon.size();
					for(int j=0; j<vertexNum; j++)
					{
						voronoi::Point pt = tess.polygon[j];
						Nuclear::Vertex ver;

						ver.u = pt.x/fWidth;
						ver.v = pt.y/fHeight;

						vertexs.push_back(ver);
					}

					shape.vertexs = vertexs;
					m_shapeList.push_back(shape);
				}
			}
			else
			{//打开文件的时候...
				int cnt = (int)m_shapeList.size();
				POINT pt;
				m_voronoi.reset();
				for(int i=0; i<cnt; i++)
				{
					Nuclear::SHAPE shape = m_shapeList[i];

					pt.x = (int)( shape.center.u *m_nShapeTextureWidth);
					pt.y = (int)( shape.center.v *m_nShapeTextureHeight);

					m_voronoi.addPoint(pt.x, pt.y);
				}

				m_voronoi.calcTessellations();
			}
		}
		break;

	case 1:
		{
			m_shapeList.clear();
			float dx = fWidth/m_nVertexCol;
			float dy = fHeight/m_nVertexRow;
			for(int i=0; i<m_nVertexRow; i++)
			{
				for(int j=0; j<m_nVertexCol; j++)
				{
					Nuclear::SHAPE shape;
					Nuclear::FPOINT center;
					center.x = dx*j +dx/2.0f;
					center.y = dy*i +dy/2.0f;
					shape.center.u = (dx*j +dx/2.0f)/fWidth;
					shape.center.v = (dy*i +dy/2.0f)/fHeight;

					Nuclear::VERTEX_LIST vertexs;
					Nuclear::Vertex pt;
					Nuclear::FPOINT point;
					point.x = -dx/2;
					point.y = -dy/2;
					pt.u = (center.x +point.x)/fWidth;
					pt.v = (center.y +point.y)/fHeight;
					pt.u = pt.u<0.0f ? 0.0f: (pt.u>1.0f ? 1.0f:pt.u);
					pt.v = pt.v<0.0f ? 0.0f: (pt.v>1.0f ? 1.0f:pt.v);
					vertexs.push_back(pt);

					point.x = -dx/2;
					point.y = dy/2;
					pt.u = (center.x +point.x)/fWidth;
					pt.v = (center.y +point.y)/fHeight;
					pt.u = pt.u<0.0f ? 0.0f: (pt.u>1.0f ? 1.0f:pt.u);
					pt.v = pt.v<0.0f ? 0.0f: (pt.v>1.0f ? 1.0f:pt.v);
					vertexs.push_back(pt);

					point.x = dx/2;
					point.y = dy/2;
					pt.u = (center.x +point.x)/fWidth;
					pt.v = (center.y +point.y)/fHeight;
					pt.u = pt.u<0.0f ? 0.0f: (pt.u>1.0f ? 1.0f:pt.u);
					pt.v = pt.v<0.0f ? 0.0f: (pt.v>1.0f ? 1.0f:pt.v);
					vertexs.push_back(pt);

					point.x = dx/2;
					point.y = -dy/2;
					pt.u = (center.x +point.x)/fWidth;
					pt.v = (center.y +point.y)/fHeight;
					pt.u = pt.u<0.0f ? 0.0f: (pt.u>1.0f ? 1.0f:pt.u);
					pt.v = pt.v<0.0f ? 0.0f: (pt.v>1.0f ? 1.0f:pt.v);
					vertexs.push_back(pt);
					shape.vertexs = vertexs;

					center.x -= fWidth/2.0f;
					center.y -= fHeight/2.0f;

					m_shapeList.push_back(shape);
				}
			}
		}
	    break;
	case 2: //三角形
		{
			m_shapeList.clear();
			float dx = fWidth/m_nVertexCol;
			float dy = fHeight/m_nVertexRow;
			for(int i=0; i<m_nVertexRow; i++)
			{
				for(int j=0; j<m_nVertexCol; j++)
				{
					Nuclear::Vertex pt0;
					Nuclear::FPOINT point;
					point.x = dx*j +dx/2;
					point.y = dy*i +dy/2;
					pt0.u = (dx*j +dx/2)/fWidth;
					pt0.v = (dy*i +dy/2)/fHeight;

				//	Nuclear::VERTEX_LIST vertexs;
					Nuclear::Vertex pt1;
					Nuclear::FPOINT point1;
					point1.x = -dx/2;
					point1.y = -dy/2;
					pt1.u = (point.x +point1.x)/fWidth;
					pt1.v = (point.y +point1.y)/fHeight;
					pt1.u = pt1.u<0.0f ? 0.0f: (pt1.u>1.0f ? 1.0f:pt1.u);
					pt1.v = pt1.v<0.0f ? 0.0f: (pt1.v>1.0f ? 1.0f:pt1.v);
					
					Nuclear::Vertex pt2;
					Nuclear::FPOINT point2;
					point2.x = -dx/2;
					point2.y = dy/2;
					pt2.u = (point.x +point2.x)/fWidth;
					pt2.v = (point.y +point2.y)/fHeight;
					pt2.u = pt2.u<0.0f ? 0.0f: (pt2.u>1.0f ? 1.0f:pt2.u);
					pt2.v = pt2.v<0.0f ? 0.0f: (pt2.v>1.0f ? 1.0f:pt2.v);
					
					Nuclear::Vertex pt3;
					Nuclear::FPOINT point3;
					point3.x = dx/2;
					point3.y = dy/2;
					pt3.u = (point.x +point3.x)/fWidth;
					pt3.v = (point.y +point3.y)/fHeight;
					pt3.u = pt3.u<0.0f ? 0.0f: (pt3.u>1.0f ? 1.0f:pt3.u);
					pt3.v = pt3.v<0.0f ? 0.0f: (pt3.v>1.0f ? 1.0f:pt3.v);
					
					Nuclear::Vertex pt4;
					Nuclear::FPOINT point4;
					point4.x = dx/2;
					point4.y = -dy/2;
					pt4.u = (point.x +point4.x)/fWidth;
					pt4.v = (point.y +point4.y)/fHeight;
					pt4.u = pt4.u<0.0f ? 0.0f: (pt4.u>1.0f ? 1.0f:pt4.u);
					pt4.v = pt4.v<0.0f ? 0.0f: (pt4.v>1.0f ? 1.0f:pt4.v);
					
					Nuclear::SHAPE shape;
					shape.center = pt0;
					shape.vertexs.push_back(pt1);
					shape.vertexs.push_back(pt2);
					shape.vertexs.push_back(pt4);
					m_shapeList.push_back(shape);

					shape.reset();
					shape.center = pt0;
					shape.vertexs.push_back(pt2);
					shape.vertexs.push_back(pt3);
					shape.vertexs.push_back(pt4);
					m_shapeList.push_back(shape);
				}
			}
		}
	    break;

	case 3:
		{
			m_shapeList.clear();
			
			float centx = 0.5f;
			float centy = 0.5f;

			Nuclear::Vertex ptCent;
			ptCent.u = 0.5f;
			ptCent.v = 0.5f;

			Nuclear::Vertex pt0;
			Nuclear::Vertex pt1;
			Nuclear::Vertex pt2;
			Nuclear::Vertex pt3;
			Nuclear::Vertex ptA;
			Nuclear::Vertex ptB;

			pt0.u = 0.0f;
			pt0.v = 0.0f;
			pt1.u = 1.0f;
			pt1.v = 0.0f;
			pt2.u = 1.0f;
			pt2.v = 1.0f;
			pt3.u = 0.0f;
			pt3.v = 1.0f;

			if(m_nLineAngle == 90 || m_nLineAngle == 270)
			{
				ptA.u = 0.5f;
				ptA.v = 0.0f;
				ptB.u = 0.5f;
				ptB.v = 1.0f;
			}
			else
			{
				float fk = tan(m_nLineAngle/57.3f);

				ptA.u = 0.0f;
				ptA.v = fk*(0.0f-0.5f) +0.5f;
				if(ptA.v>1.0f || ptA.v<0.0f)
				{
					ptA.u = (0.0f-0.5f)/fk +0.5f;
					ptA.v = 0.0f;
				}

				ptB.u = 1.0f;
				ptB.v = fk*(1.0f-0.5f) +0.5f;
				if(ptB.v>1.0f || ptB.v<0.0f)
				{
					ptB.u = (1.0f-0.5f)/fk +0.5f;
					ptB.v = 1.0f;
				}
			}

			if( ptA.u == pt0.u )
			{
				Nuclear::SHAPE shape;
				shape.center = ptCent;
				shape.vertexs.push_back(pt0);
				shape.vertexs.push_back(pt1);
				shape.vertexs.push_back(ptB);
				shape.vertexs.push_back(ptA);
				m_shapeList.push_back(shape);

				shape.reset();
				shape.center = ptCent;
				shape.vertexs.push_back(ptA);
				shape.vertexs.push_back(ptB);
				shape.vertexs.push_back(pt2);
				shape.vertexs.push_back(pt3);
				m_shapeList.push_back(shape);
			}
			else
			{
				Nuclear::SHAPE shape;
				shape.center = ptCent;
				shape.vertexs.push_back(pt0);
				shape.vertexs.push_back(ptA);
				shape.vertexs.push_back(ptB);
				shape.vertexs.push_back(pt3);
				m_shapeList.push_back(shape);

				shape.reset();
				shape.center = ptCent;
				shape.vertexs.push_back(ptA);
				shape.vertexs.push_back(pt1);
				shape.vertexs.push_back(pt2);
				shape.vertexs.push_back(ptB);
				m_shapeList.push_back(shape);				
			}
		}
		break;

	default:
	    break;
	}

	if(m_bPologyOk)
	{
		CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
		ASSERT_VALID(pDoc);
		if (!pDoc)
			return;

		Nuclear::CSpecialParticleSystem* pPS = NULL;
		if( pPS = dynamic_cast<Nuclear::CSpecialParticleSystem*>(pDoc->GetParticleParameterView()->GetParticleSys()) )
			pPS->SetShapePsVertexs(m_shapeList);
	}
}

void CParticleSystemEditorView::SaveShapeList(const CString& filePath)
{
	if(m_bPologyOk)
	{
		m_vertexsObject.SetShapeList(m_shapeList);

		m_vertexsObject.SetShapeType((Nuclear::PSL_ABSRTACTSHAPETYPE)m_nVertexType);
		m_vertexsObject.SetShapeRow(m_nVertexRow);
		m_vertexsObject.SetShapeCol(m_nVertexCol);
		m_vertexsObject.SetLineAngle(m_nLineAngle);

		bool bres = m_vertexsObject.SaveToNativePath(filePath);
		if(!bres)
			OutputDebugString(L"SaveShapeList() --- Failed\n");

		m_bPologyModify = false;
		m_vtxsFilePath = filePath;
	}
	else
	{
		MessageBox(L"请先确定拆分完毕再保存\n");
	}
}

void CParticleSystemEditorView::LoadShapeList(const CString& filePath)
{
	bool bres = m_vertexsObject.LoadFromNativePath(filePath);
	if(!bres)
		OutputDebugString(L"LoadFromNativePath() --- Failed\n");

	m_nVertexType = (int)( m_vertexsObject.GetShapeType() );
	m_nVertexRow = m_vertexsObject.GetShapeRow();
	m_nVertexCol = m_vertexsObject.GetShapeCol();
	m_nLineAngle = m_vertexsObject.GetLineAngle();

	if(m_nVertexType == 0)
	{
		m_voronoi.reset();
		int nWidth = m_nShapeTextureWidth;
		int nHeight = m_nShapeTextureHeight;
		POINT pt;
		int cnt = (int)( m_vertexsObject.GetShapeList().size() );
		for(int i=0; i<cnt; i++)
		{
			Nuclear::SHAPE shape = m_vertexsObject.GetShapeList()[i];
		//	pt.x = (int)( shape.center.x ) + nWidth/2;
		//	pt.y = (int)( shape.center.y ) + nHeight/2;

			pt.x = (int)( shape.center.u *nWidth);
			pt.y = (int)( shape.center.v *nHeight);

			m_voronoi.addPoint(pt.x, pt.y);
		}

		m_voronoi.calcTessellations();
	}
	else
	{
	}

	m_bPologyModify = false;
	m_vtxsFilePath = filePath;

	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	Nuclear::CSpecialParticleSystem* pPS = NULL;
	if( pPS = dynamic_cast<Nuclear::CSpecialParticleSystem*>(pDoc->GetParticleParameterView()->GetParticleSys()) )
		pPS->SetShapePsVertexs(m_vertexsObject.GetShapeList());
}

void CParticleSystemEditorView::SetShapeListData(const std::wstring& strVtxsObjectPath, const Nuclear::SHAPE_LIST& shapeList)
{//打开抽象FNExt_ParticleWS时初始化预览数据...
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	Nuclear::CParticleSystem* pParticleSys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticleSys == NULL)
		return;

	pParticleSys->GetPSL()->m_strVtxsObjectPath = strVtxsObjectPath;

	do 
	{
		MessageBox(L"请设置前景地图\n");
		pDoc->GetParticleParameterView()->OnBnClickedButtonSetshapetexture();
	} while(m_hShapePicHandle == Nuclear::INVALID_PICTURE_HANDLE);

	Nuclear::PictureInfo picinfo;
	bool bres = m_pRenderer->GetPictureInfo(m_hShapePicHandle, picinfo);
	if(!bres)
	{
		MessageBox(L"报错！\n");
		return;
	}
	m_nShapeTextureWidth = picinfo.m_nPicWidth;
	m_nShapeTextureHeight = picinfo.m_nPicHeight;

	if(m_nVertexType == 0)
	{ 
		//
		POINT pt0;
		pt0.x = m_nShapeTextureWidth/2;
		pt0.y = m_nShapeTextureHeight/2;

		m_shapeList = shapeList;
		int cnt = (int)m_shapeList.size();
		POINT pt;
		m_voronoi.reset();
		for(int i=0; i<cnt; i++)
		{
			Nuclear::SHAPE shape = m_shapeList[i];
		//	pt.x = (int)( shape.center.x ) + pt0.x;
		//	pt.y = (int)( shape.center.y ) + pt0.y;

			pt.x = (int)( shape.center.u *m_nShapeTextureWidth);
			pt.y = (int)( shape.center.v *m_nShapeTextureHeight);

			m_voronoi.addPoint(pt.x, pt.y);
		}

		m_voronoi.calcTessellations();
	}
	else //2
	{ //todo
		ComputeShapeVertex();
	}

	m_bPologyModify = false;
}

void CParticleSystemEditorView::CreateShapeTexture(const CString& filePath)
{
	std::wstring outFile;
	PFS::CEnv::NativeFileNameToPFSFileName(filePath.GetString(), outFile);
	m_hShapePicHandle = m_pRenderer->LoadPicture(outFile);

	if(m_pD3dTextureShapes != Nuclear::INVALID_PICTURE_HANDLE)
		m_pRenderer->FreePicture(m_pD3dTextureShapes);

	m_pD3dTextureShapes = m_hShapePicHandle;
	Nuclear::PictureInfo picinfo;
	m_pRenderer->GetPictureInfo(m_hShapePicHandle, picinfo);

	m_voronoi.init(300, picinfo.m_nPicWidth, picinfo.m_nPicHeight);
	ComputeShapeVertex();

	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	Nuclear::CParticleSystem* pParticleSys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticleSys == NULL)
		return;

	RECT rct;
	GetClientRect(&rct);
	
	Nuclear::PSParameter psParameter = pParticleSys->GetPSParameter();
	psParameter.m_psModifyParam.m_pointCent.x = rct.left + picinfo.m_nPicWidth/2.0f;
	psParameter.m_psModifyParam.m_pointCent.y = rct.top + picinfo.m_nPicHeight/2.0f;

	Nuclear::ParticleSystemHandle handle = pDoc->GetParticleParameterView()->GetCurrentPShandle();
	bool bres = m_pParSysManager->ModifyParticleSystem(handle, psParameter);
	bres = m_pParSysManager->SetSpecialPsTexture(handle, m_hShapePicHandle, 1.0f, 1.0f);
	
	m_nShapeTextureWidth = picinfo.m_nPicWidth;
	m_nShapeTextureHeight = picinfo.m_nPicHeight;

	m_bSetShapeTexture = true;
}

void CParticleSystemEditorView::CreateBKTextureHandle(const std::wstring& strBKpicFilePath)
{
	std::wstring BKpicPFSpath;
	bool bres = PFS::CEnv::NativeFileNameToPFSFileName(strBKpicFilePath, BKpicPFSpath);
	if(!bres)
	{
		MessageBox(L"CreateBKTextureHandle--Failed");
		return;
	}
	if(m_pRenderer != NULL)
	{
		m_pRenderer->FreePicture(m_hBKpicHandle);
		m_hBKpicHandle = m_pRenderer->LoadPicture(BKpicPFSpath);

		Nuclear::PictureInfo picinfo;
		bool bres = m_pRenderer->GetPictureInfo(m_hBKpicHandle, picinfo);
		float left = 0.0f;
		float top = 0.0f;
		float right = left + picinfo.m_nPicWidth;
		float bottom = top + picinfo.m_nPicHeight;
		m_FrctMapPos = Nuclear::FRECT(left, top, right, bottom);
	}
}

HRESULT CParticleSystemEditorView::CreateVectorD3DTexture(std::vector<std::wstring> vectorTextureName)
{
	ReleaseVectorD3Dtexture(m_vectorD3DTexture);
	HRESULT hr = -1;

	int cnt = (int)vectorTextureName.size();
	if(cnt<1 || cnt>32) //多纹理最多不能超过32张纹理..
	{
		MessageBox(L"一个粒子允许的纹理数cnt: 0<cnt<33");
		return -1;
	}

	std::wstring strTexName = vectorTextureName[0];
	std::wstring strTexPath = theApp.GetResPath() + L"\\effect\\particle\\texture\\";
	strTexPath += strTexName;
	CPictureTGA tga;
	bool bres = tga.Open(strTexPath.c_str());
	if(!bres)
	{
		MessageBox(L"打开tga图片失败..\n");
		return -1;
	}
	int nWidth = 0;
	int nHeight = 0;
	tga.GetWidthHeight(nWidth, nHeight);

	Nuclear::PictureHandle handle;
	for(int i=0; i<cnt; i++)
	{
		strTexName = vectorTextureName[i];
		strTexPath = theApp.GetResPath() + L"\\effect\\particle\\texture\\";
		strTexPath += strTexName;

		handle = m_pRenderer->LoadPictureFromNativePath(strTexPath);
		if(handle != Nuclear::INVALID_PICTURE_HANDLE)
		{
			Nuclear::PictureInfo picinfo;
			if( !m_pRenderer->GetPictureInfo(handle, picinfo) )
				return false;

			int nWidth_cur = picinfo.m_nPicWidth;
			int nHeight_cur = picinfo.m_nPicHeight;

			if(nWidth_cur != nWidth || nHeight_cur != nHeight)
			{
				MessageBox(L"同一个粒子使用了多个纹理..大小必须一致..");
				return -1; //
			}
		}

		m_vectorD3DTexture.push_back(handle);
	}

	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return -1;

	Nuclear::XHardRef<Nuclear::PSTEXTUREINFOSTRUCT>* pRefPSTextureInfo = NULL;
	//pDoc->GetParticleParameterView()->GetParticleSys()->GetPSTextureInfo();
	
	Nuclear::CGeneralParticleSystem* pPS = NULL;
	if( pPS = dynamic_cast<Nuclear::CGeneralParticleSystem*>(pDoc->GetParticleParameterView()->GetParticleSys()) )
		pRefPSTextureInfo = &( pPS->GetPSTextureInfo() );

	if( pRefPSTextureInfo->IsValid() )
	{
		m_pRenderer->FreePicture((*pRefPSTextureInfo)->PsTextureInfo.handle);
	}
	else
	{
		Nuclear::PSTEXTUREINFOSTRUCT* PsTextureInfo = new Nuclear::PSTEXTUREINFOSTRUCT(m_pRenderer);
		pRefPSTextureInfo = new Nuclear::XHardRef<Nuclear::PSTEXTUREINFOSTRUCT>(PsTextureInfo);
	}

	bool bre = GenPSTextureInfo(m_vectorD3DTexture, &((*pRefPSTextureInfo)->PsTextureInfo) );

	return hr;
}

bool CParticleSystemEditorView::GenPSTextureInfo(const std::vector<Nuclear::PictureHandle>& vectorD3DTexture, Nuclear::PSTEXTUREINFO* ppslTexture)
{
	if(m_pRenderer==NULL)
		return false;

	int cnt = (int)vectorD3DTexture.size();
	if(cnt<1 || cnt>32)
		return false;
	Nuclear::PictureInfo picinfo;
	if( !m_pRenderer->GetPictureInfo(vectorD3DTexture[0], picinfo) )
		return false;

	int nWidth = picinfo.m_nPicWidth;
	int nHeight = picinfo.m_nPicHeight;


	int nFlipIndex = 0;
	for(int i=0; i<6; i++)
	{
		int maxCntPow2Min = 1<<i;
		if(maxCntPow2Min>=cnt)
		{
			nFlipIndex = i;
			break;
		}
	}
	//列大于等于行...
	//纹理数据按行优先填充
	int nCol = 0;
	int nRow = 0;
	if(nFlipIndex%2) //奇数
	{
		nCol = 1<<((nFlipIndex/2)+1);
		nRow = 1<<(nFlipIndex/2);
	}
	else
	{
		nCol = 1<<(nFlipIndex/2);
		nRow = 1<<(nFlipIndex/2);
	}

	Nuclear::PictureHandle newhandle = m_pRenderer->NewPicture(nCol*nWidth, nRow*nHeight,NULL, Nuclear::XPTEXFMT_A8R8G8B8);
	if( newhandle == Nuclear::INVALID_PICTURE_HANDLE )
		return false;
	ppslTexture->handle = newhandle;

	ppslTexture->m_nTexWidth = nWidth;
	ppslTexture->m_nTexHeight = nHeight;
	ppslTexture->m_nTexNum = cnt;
	ppslTexture->m_nPslTexCol = nCol;
	ppslTexture->m_nPslTexRow = nRow;

	Nuclear::CRECT srcrect(0,0,nWidth,nHeight);
	int buffersize = nWidth*nHeight*4;
	void *buffer = malloc(buffersize);
	if( buffer == NULL ) return false;
	for(int i=0; i<cnt; i++)
	{
		m_pRenderer->GetPictureData(vectorD3DTexture[i], buffer, buffersize, &srcrect);
		int row = i/ppslTexture->m_nPslTexCol;
		int col = i%ppslTexture->m_nPslTexCol;
		Nuclear::CRECT dstrect(col*nWidth,row*nHeight,(col+1)*nWidth,(row+1)*nHeight);
		m_pRenderer->SetPictureData(newhandle, buffer, buffersize, &dstrect);

	}
	free(buffer);
	return true;
}

bool CParticleSystemEditorView::InitD3D()
{
	if( m_pRenderer == NULL ) 
	{
		CRect rectClient;
		GetClientRect(&rectClient);
		HWND hwnd = ::GetDesktopWindow();
		HDC hdc = ::GetDC(hwnd);
		HRESULT hr = CreateRenderer(&m_pRenderer, m_hWnd,
			Nuclear::XDisplayMode(GetSystemMetrics(SM_CXSCREEN), GetSystemMetrics(SM_CYSCREEN),GetDeviceCaps(hdc, BITSPIXEL)), 
			theApp.m_pFileIOManager, Nuclear::XPCRF_VERTICALSYNC);
		::ReleaseDC(hwnd, hdc);
		if( FAILED(hr) )
			return false;

		Nuclear::CRECT sourceRect(0, 0, rectClient.right-rectClient.left, rectClient.bottom-rectClient.top);
		m_pRenderer->SetSourceRect(&sourceRect);
	}

	m_pParSysManager = dynamic_cast<Nuclear::ParticleManager*>(m_pRenderer->GetParticleManager());

	m_pParSysManager->SetLogPath(theApp.GetResPath().GetBuffer()); //L"E:\\game2d\\whf\\bin\\"

	return true;
}

// CParticleSystemEditorView 消息处理程序

void CParticleSystemEditorView::OnTimer(UINT_PTR nIDEvent)
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return;

	switch(nIDEvent)
	{
	case 1:
		{
			Nuclear::TimerOB::OnSourceTimer(); //计算fps
			pDoc->GetParticleSysInfoView()->Invalidate(true);
		}
		break;
	case 2:
		Render();
		break;
	case 5: // 录制序列帧
		PrintScreenToFile();
		break;
	default:
	    break;
	}
	CView::OnTimer(nIDEvent);
}

void CParticleSystemEditorView::OnSize(UINT nType, int cx, int cy)
{
	CView::OnSize(nType, cx, cy);
	// TODO: 在此处添加消息处理程序代码
	if (m_pRenderer)
	{
		CRect rectClient;
		GetClientRect(&rectClient);
		Nuclear::CRECT sourceRect(0, 0, rectClient.right-rectClient.left, rectClient.bottom-rectClient.top);
		m_pRenderer->SetSourceRect(&sourceRect);
	}
}

void CParticleSystemEditorView::OnSizing(UINT fwSide, LPRECT pRect)
{
	CView::OnSizing(fwSide, pRect);

	// TODO: 在此处添加消息处理程序代码
	if (m_pRenderer)
	{
		CRect rectClient;
		GetClientRect(&rectClient);
		Nuclear::CRECT sourceRect(0, 0, rectClient.right-rectClient.left, rectClient.bottom-rectClient.top);
		m_pRenderer->SetSourceRect(&sourceRect);
	}
}

void CParticleSystemEditorView::OnLButtonDown(UINT nFlags, CPoint point)
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	Nuclear::CParticleSystem* pParticleSys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticleSys == NULL)
		return;

	Nuclear::FPOINT pt;
	pt.x = (float)point.x;
	pt.y = (float)point.y;

	const Nuclear::PSParameter& psParameter = pParticleSys->GetPSParameter();
	Nuclear::XHardRef<Nuclear::PSL> ppsl = pParticleSys->GetPSL();
	pt -= psParameter.m_psModifyParam.m_pointCent;

	if(ppsl->m_nAbsrtactPsl==0)
	{
		if(m_bMoveMap)//拖动地图 m_FrctMapPos
			m_initialPoint = point;
		else if(m_bSetEmitterSpinCentFlag)//设置发射器旋转中心
			ppsl->m_pointEmitterSpinCent = pt;
		else if(m_bSetParticleHoleFlag)//设置粒子影响点
		{ 
			//做相反的操作..
			float holex = pt.x/ppsl->m_fPSLzoom;
			float holey = pt.y/ppsl->m_fPSLzoom;
			float fangle = -psParameter.m_psModifyParam.m_fEmitterSpinAngle -Nuclear::TORADIANS(ppsl->m_fEmitterSpin);

			pt.x = holex*cos(fangle) - holey*sin(fangle);
			pt.y = holex*sin(fangle) + holey*cos(fangle);

			ppsl->m_pointParticleHole = pt;
		}
		else
			SetParticleSysPos(point);
	}
	else
	{//抽象粒子特效..这里面是编辑拆分前景地图.. //2009--6--2
		if(m_bPologyOk)
		{
			SetParticleSysPos(point);
		}
		else
		{
			if(m_nVertexType == 0)
			{
				if(m_bSetShapeTexture)
				{
					if(nFlags & MK_SHIFT)
					{
						m_nMouseDownIndex = m_voronoi.hitTest(point.x, point.y);
						if(m_nMouseDownIndex != -1)
						{
							m_ptLastPos = point;
							SetCapture();
						}
					}
					else
					{
						if( m_voronoi.addPoint(point.x, point.y) )
						{
							m_voronoi.calcTessellations();
							m_bPologyModify = true;
						}
					}
				}
				else
				{
					MessageBox(L"请先设置前景图\n");
				}
			}
		}
	}

	CView::OnLButtonDown(nFlags, point);
}

void CParticleSystemEditorView::OnMouseMove(UINT nFlags, CPoint point)
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return CView::OnMouseMove(nFlags, point);

	Nuclear::CParticleSystem* pParticleSys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticleSys == NULL)
		return;

	Nuclear::XHardRef<Nuclear::PSL> ppsl = pParticleSys->GetPSL();
	if(ppsl->m_nAbsrtactPsl==1)
	{
		if(!m_bPologyOk)
		{
			if(m_nVertexType == 0)
			{
				if(m_nMouseDownIndex != -1)
				{
					if( m_voronoi.movePoint( m_nMouseDownIndex, point.x-m_ptLastPos.x, point.y-m_ptLastPos.y, false))
					{
						m_voronoi.calcTessellations();
						m_bPologyModify = true;
					}

					m_ptLastPos = point;
				}
			}
		}
	}
	else
	{
		if(nFlags==MK_LBUTTON)
		{
			if(m_bMoveMap)
			{//拖动地图 m_FrctMapPos
				int dx = point.x - m_initialPoint.x;
				int dy = point.y - m_initialPoint.y;
				m_initialPoint = point;
				m_FrctMapPos += Nuclear::FPOINT((float)dx, (float)dy);
				m_ViewPort -= Nuclear::CPOINT(dx, dy);
				//		m_pParSysManager->OnViewportUpdate(m_ViewPort);
			}
			else if(m_bSetEmitterSpinCentFlag)
			{
			}
			else if(m_bSetParticleHoleFlag)
			{
			}
			else
			{//修改粒子发射位置... 			
				SetParticleSysPos(point);
			}
		}
		else if(nFlags==MK_RBUTTON)
		{
			CDC *pDC = GetDC();
			if(m_bSetMaxBox || m_bBlindArea) //粒子盲区也共用设置包围盒的参数了..
			{
				SIZE size;
				size.cx = 1;
				size.cy = 1;

				if(m_InitBoxPoint != point)
				{
					CRect newRect(min(m_InitBoxPoint.x, point.x),
						min(m_InitBoxPoint.y, point.y),
						max(m_InitBoxPoint.x, point.x),
						max(m_InitBoxPoint.y, point.y));
					//		CRect oldRect(min(m_InitBoxPoint.x, m_oldBoxPoint.x),
					//			min(m_InitBoxPoint.y, m_oldBoxPoint.y),
					//			max(m_InitBoxPoint.x, m_oldBoxPoint.x),
					//			max(m_InitBoxPoint.y, m_oldBoxPoint.y));

					//		pDC->DrawDragRect(&newRect, size, &oldRect, size, NULL, NULL);

					m_rctMaxBox = newRect; //保存下来在render的时候d3d绘制...
				}
				m_oldBoxPoint=point;
			}
			else if(m_bBlockLine || m_bReflectLine)
			{
				m_oldBoxPoint=point;
			}
			else
			{
			}
		}
		else
		{
		}
	}

	CView::OnMouseMove(nFlags, point);
}

void CParticleSystemEditorView::OnLButtonUp(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	if(m_nMouseDownIndex != -1)
	{
		m_nMouseDownIndex = -1;
		ReleaseCapture();
	}

	CView::OnLButtonUp(nFlags, point);
}

void CParticleSystemEditorView::SetParticleSysPos(CPoint point)
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;
	RECT rct;
	GetClientRect(&rct);
	POINT pt;
	pt.x = point.x;
	pt.y = point.y;

	Nuclear::CParticleSystem* pParticleSys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticleSys == NULL)
		return;

	Nuclear::PSParameter psParameter = pParticleSys->GetPSParameter();
	//Nuclear::PSParameter& 不能... 这里需要copy一份数据
	if(m_bTrackFx)
	{
		psParameter.m_psModifyParam.m_pointObject = Nuclear::FPOINT((float)pt.x, (float)pt.y);
	}
	else
	{
		psParameter.m_psModifyParam.m_pointCent.x = (float)pt.x;
		psParameter.m_psModifyParam.m_pointCent.y = (float)pt.y;
	}

	Nuclear::ParticleSystemHandle handle = pDoc->GetParticleParameterView()->GetCurrentPShandle();
	if(handle == Nuclear::INVALID_PS_HANDLE)
		handle = 1; //逻辑错误了...应该不会这样的

	m_pParSysManager->ModifyParticleSystem(handle, psParameter);
}

void CParticleSystemEditorView::OnRButtonUp(UINT nFlags, CPoint point)
{
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return;

	Nuclear::CParticleSystem* pParticleSys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticleSys == NULL)
		return;

	Nuclear::PSParameter& psParameter = pParticleSys->GetPSParameter();
	int x = (int)psParameter.m_psModifyParam.m_pointCent.x;
	int y = (int)psParameter.m_psModifyParam.m_pointCent.y;
	Nuclear::CRECT rctMaxBox; //转为相对于特效中心点的相对坐标..水平右x+，垂直下y+
	rctMaxBox.left = m_rctMaxBox.left - x;
	rctMaxBox.top = m_rctMaxBox.top - y;
	rctMaxBox.right = m_rctMaxBox.right - x;
	rctMaxBox.bottom = m_rctMaxBox.bottom - y;
	m_rctMaxBox = CRect(0,0,0,0);

	if(m_bSetMaxBox)
	{
		//m_rctMaxBox //修改包围盒的位置...
		pParticleSys->GetPSL()->m_rctMaxBox = rctMaxBox;
	}
	else if(m_bBlindArea)
	{
		psParameter.m_psModifyParam.m_vectorRctBlindArea.push_back(rctMaxBox);
	}
	else if(m_bBlockLine)
	{
		Nuclear::CPOINT pt0 = Nuclear::CPOINT(m_InitBoxPoint.x, m_InitBoxPoint.y) - Nuclear::CPOINT(x, y);
		Nuclear::CPOINT pt1 = Nuclear::CPOINT(m_oldBoxPoint.x, m_oldBoxPoint.y) - Nuclear::CPOINT(x, y);

		Nuclear::LINE line = Nuclear::LINE(pt0, pt1);
		psParameter.m_psModifyParam.m_vectorBlockLine.push_back(line);
	}
	else if(m_bReflectLine)
	{
		Nuclear::CPOINT pt0 = Nuclear::CPOINT(m_InitBoxPoint.x, m_InitBoxPoint.y) - Nuclear::CPOINT(x, y);
		Nuclear::CPOINT pt1 = Nuclear::CPOINT(m_oldBoxPoint.x, m_oldBoxPoint.y) - Nuclear::CPOINT(x, y);

		Nuclear::LINE line = Nuclear::LINE(pt0, pt1);
		psParameter.m_psModifyParam.m_vectorReflectLine.push_back(line);
	}
	else
	{
	}

	m_oldBoxPoint = m_InitBoxPoint; //在界面上的显示bug问题...使之成为一个点

	CView::OnRButtonUp(nFlags, point);
}

void CParticleSystemEditorView::SetObjTrackFlag(BOOL bTrackFx)
{
	m_bTrackFx = bTrackFx;
}

void CParticleSystemEditorView::SetVertexType(int nVertexType)
{
	if(nVertexType != m_nVertexType)
	{
		m_shapeList.clear();
		m_nVertexType = nVertexType;
	}
}

void CParticleSystemEditorView::SetVertexRow(int nVertexRow)
{
	m_nVertexRow = nVertexRow;
}

void CParticleSystemEditorView::SetVertexCol(int nVertexCol)
{
	m_nVertexCol = nVertexCol;
}

void CParticleSystemEditorView::SetLineAngle(int nLineAngle)
{
	m_nLineAngle = nLineAngle;
	
	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if(!pDoc)
		return;

	Nuclear::CParticleSystem* pParticleSys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticleSys == NULL)
		return;

	Nuclear::XHardRef<Nuclear::PSL> ppsl = pParticleSys->GetPSL();

	if(ppsl->m_nAbsrtactPsl==1)
	{
		if(m_nVertexType == 3)
		{
			ppsl->m_fEmissionDirection = (float)m_nLineAngle;
			ppsl->m_nEmitterType = FALSE;
		}
	}
}

void CParticleSystemEditorView::OnRButtonDown(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	Nuclear::CParticleSystem* pParticleSys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticleSys == NULL)
		return;

	Nuclear::XHardRef<Nuclear::PSL> ppsl = pParticleSys->GetPSL();

	if(ppsl->m_nAbsrtactPsl==1) //删除块...
	{
		if(m_nVertexType == 0)
		{
			if( !m_bPologyOk )
			{
				m_voronoi.reset();

				m_bPologyModify = true;
			}
		}
	}
	else
	{
		if(!m_bSetMaxBox && !m_bBlindArea && !m_bBlockLine && !m_bReflectLine)
		{
			CPoint pt;
			::GetCursorPos(&pt);

			CMenu menu;
			menu.LoadMenu(IDR_MENU3);
			//	menu.EnableMenuItem(ID_MENU_AddEffectGroup, MF_BYCOMMAND | MF_DISABLED | MF_GRAYED);
			menu.EnableMenuItem(ID_MENU_AddEffectGroup, MF_BYCOMMAND | MF_ENABLED);

			CMenu* pMenu = menu.GetSubMenu(0);
			pMenu->TrackPopupMenu(TPM_LEFTALIGN | TPM_RIGHTBUTTON,pt.x,pt.y,this,NULL);
		}
		else
		{//设置包围盒或者设置粒子盲区或者阻挡线或者反射线...
			m_InitBoxPoint = point;
			m_oldBoxPoint = point;
		}
	}

	CView::OnRButtonDown(nFlags, point);
}

void CParticleSystemEditorView::OnMenuProperties()
{
	CParameterAdjustDlg dlg;

	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;
	std::vector<PARAMETERPROPERTY> paramPropertyVector;
	pDoc->GetParticleParameterView()->GetParameterPropertyVector(paramPropertyVector);
	dlg.SetParameterPropertyVector(paramPropertyVector);

	int iRes = dlg.DoModal();
	if(iRes == IDOK)
	{
	}
}

void CParticleSystemEditorView::OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	switch(nChar)
	{
	case 'P':
		{
			CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
			ASSERT_VALID(pDoc);
			if (!pDoc)
				return CView::OnKeyDown(nChar, nRepCnt, nFlags);
			pDoc->GetParticleParameterView()->OnBnClickedButtonReplay();

			m_bStop = false;
			pDoc->GetParticleParameterView()->OnSetStopFlag(m_bStop);
		}
		break;

	case 'O':
		{
			m_bStop = !m_bStop;

			CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
			ASSERT_VALID(pDoc);
			if (!pDoc)
				return CView::OnKeyDown(nChar, nRepCnt, nFlags);
			pDoc->GetParticleParameterView()->OnSetStopFlag(m_bStop);
		}
		break;

	case 'N':
		{
			CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
			ASSERT_VALID(pDoc);
			if (!pDoc)
				return CView::OnKeyDown(nChar, nRepCnt, nFlags);
			
			Nuclear::CGeneralParticleSystem* pPS = dynamic_cast<Nuclear::CGeneralParticleSystem*>(pDoc->GetParticleParameterView()->GetParticleSys());

			static int nIndex = 0;
			nIndex++;
			std::vector<int> keyPointIndex = pDoc->GetParticleParameterView()->GetVectorKeyPointIndex();
			int cnt = (int)keyPointIndex.size();
			if(cnt<1)
				break;
			if( nIndex > cnt-1 )
				nIndex = 0;

			bool bres = pPS->SetKeyPointIndex(keyPointIndex[nIndex]);
			if(!bres)
			{
				MessageBox(L"出错啦...老版本的粒子？保存粒子之后重新打开一次试试。");
			}
		}
		break;

	default:
		break;
	}
	CView::OnKeyDown(nChar, nRepCnt, nFlags);
}

void CParticleSystemEditorView::OnMButtonDown(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	CView::OnMButtonDown(nFlags, point);
}

void CParticleSystemEditorView::OnMButtonUp(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	Nuclear::CParticleSystem* pParticleSys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticleSys == NULL)
		return;

	Nuclear::XHardRef<Nuclear::PSL> ppsl = pParticleSys->GetPSL();

	if(ppsl->m_nAbsrtactPsl==1 && !m_bPologyOk) //删除块...
	{
		if(m_nVertexType == 0)
		{
			bool bRefresh = false;
			int index = m_voronoi.hitTest(point.x, point.y);
			if( index != -1 )
				bRefresh = m_voronoi.erasePoint(index);

			if(bRefresh)
			{
				m_voronoi.calcTessellations();
				m_bPologyModify = true;
			}
		}
	}
	else
	{
		Nuclear::PSParameter& psParameter = pParticleSys->GetPSParameter();
		int nsize = 0;
		int index = -1;//查找当前要删除的对象
		Nuclear::CPOINT pt;
		pt.x = point.x;
		pt.y = point.y;
		if(m_bBlindArea)
		{
			nsize = psParameter.m_psModifyParam.m_vectorRctBlindArea.size();

			for(int i=0; i<nsize; i++)
			{
				Nuclear::CRECT rct = psParameter.m_psModifyParam.m_vectorRctBlindArea[i];
				rct += psParameter.m_psModifyParam.m_pointCent;				
				bool bres = rct.PtInRect(pt);
				if(bres)
				{//找到一个就结束..如果允许矩形交叠，再修改为找离中心点最近的一个矩形..
					index = i;
					break;
				}
			}
			if(index != -1)
			{
				std::vector<Nuclear::CRECT>::iterator it0 = psParameter.m_psModifyParam.m_vectorRctBlindArea.begin() +index;
				psParameter.m_psModifyParam.m_vectorRctBlindArea.erase(it0);
			}
		}

		if(m_bBlockLine)
		{
			nsize = psParameter.m_psModifyParam.m_vectorBlockLine.size();
			float minDis = 10000000.0f;
			index = -1;

			pt.x -= (int)(psParameter.m_psModifyParam.m_pointCent.x);
			pt.y -= (int)(psParameter.m_psModifyParam.m_pointCent.y);

			for(int i=0; i<nsize; i++)
			{
				//删除距离线段最近的..
				Nuclear::LINE line = psParameter.m_psModifyParam.m_vectorBlockLine[i];
				float fdis = Nuclear::PointToLineMinDis(pt, line);
				if(fdis < minDis)
				{
					minDis = fdis;
					index = i;
				}
			}

			if(index != -1)
			{
				std::vector<Nuclear::LINE>::iterator it0 = psParameter.m_psModifyParam.m_vectorBlockLine.begin() +index;
				psParameter.m_psModifyParam.m_vectorBlockLine.erase(it0);
			}
		}

		if(m_bReflectLine)
		{
			nsize = psParameter.m_psModifyParam.m_vectorReflectLine.size();
			float minDis = 10000000.0f;
			index = -1;

			pt.x -= (int)(psParameter.m_psModifyParam.m_pointCent.x);
			pt.y -= (int)(psParameter.m_psModifyParam.m_pointCent.y);

			for(int i=0; i<nsize; i++)
			{
				Nuclear::LINE line = psParameter.m_psModifyParam.m_vectorReflectLine[i];
				float fdis = Nuclear::PointToLineMinDis(pt, line);
				if(fdis < minDis)
				{
					minDis = fdis;
					index = i;
				}
			}
			if(index != -1)
			{
				std::vector<Nuclear::LINE>::iterator it0 = psParameter.m_psModifyParam.m_vectorReflectLine.begin() +index;
				psParameter.m_psModifyParam.m_vectorReflectLine.erase(it0);
			}
		}
	}

	CView::OnMButtonUp(nFlags, point);
}

void CParticleSystemEditorView::OnLButtonDblClk(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	CParticleSystemEditorDoc* pDoc = (CParticleSystemEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	Nuclear::CParticleSystem* pParticleSys = pDoc->GetParticleParameterView()->GetParticleSys();
	if(pParticleSys == NULL)
		return;

	Nuclear::XHardRef<Nuclear::PSL> ppsl = pParticleSys->GetPSL();

	if(ppsl->m_nAbsrtactPsl==1 && !m_bPologyOk) //随机生成50块...
	{
		if(m_nVertexType == 0)
		{
			bool bRefresh = false;
			bRefresh = m_voronoi.randomPoints(50, point.x, point.y);
			if(bRefresh)
			{
				m_voronoi.calcTessellations();
			}
		}
	}

	CView::OnLButtonDblClk(nFlags, point);
}

bool CParticleSystemEditorView::CutBigPicToSmallPic(int nRow, int nCol, const CString& strPath)
{
	if(m_pRenderer == NULL)
	{
		MessageBox(L"请先初始化pRenderer---\n");
		return false;
	}
	
	Nuclear::PictureHandle handle = m_pRenderer->LoadPictureFromNativePath(std::wstring(strPath));
	if(handle == Nuclear::INVALID_PICTURE_HANDLE)
	{
		MessageBox(L"打开大图片失败...\n");
		return false;
	}

	Nuclear::PictureInfo picinfo;
	if( !m_pRenderer->GetPictureInfo(handle, picinfo) )
		return false;

	int nWidth = picinfo.m_nPicWidth;
	int nHeight = picinfo.m_nPicHeight;

	int dw = nWidth/nCol;
	int dh = nHeight/nRow;
	int buffersize = dw*dh*4;
	void *buffer = malloc(buffersize);
	if( buffer == NULL ) return false;

	CString strName = strPath;
	int nPos = strName.ReverseFind('.');
	strName = strName.Left(nPos);

	for(int i=0; i<nRow; i++)
	{
		for(int j=0; j<nCol; j++)
		{
			Nuclear::CRECT srcrect(j*dw, i*dh, (j+1)*dw, (i+1)*dh);
			m_pRenderer->GetPictureData(handle, buffer, buffersize, &srcrect);

			Nuclear::XBuffer outbuf;
			if( Nuclear::Write32BitPNGWithPitch(outbuf, (unsigned char*)buffer, true, dw, dh, 4*dw) )
			{
				CString str;
				str.Format(L"_%d_%d.png", i, j);
				str = strName + str;

				FILE *tfile = Nuclear::FOpen(str, L"wb");
				if( tfile )
				{
					Nuclear::FWrite(tfile, outbuf.constbegin(), outbuf.size());
					fclose(tfile);
				}
			}
		}
	}

	free(buffer);
	m_pRenderer->FreePicture(handle);
	CString str1;
	str1.Format(L" %d 个小图片已经生成,并且保存在了原文件所在目录里面\n", nRow*nCol);
	MessageBox(str1);

	return true;
}

void CParticleSystemEditorView::PrintScreenToFile()
{
	CString strTemp = L"_";
	if(m_nFrameIndex < 10)
		strTemp = L"_00";
	else if(m_nFrameIndex < 1000)
		strTemp = L"_0";

	CString str;
	str.Format(L"%d", m_nFrameIndex);
	str = strTemp+str;

	CString strFilePathName = m_strFilePath + L"\\" + m_strPslName + str;
	
	switch(m_nFileFormat)
	{
	case 0:
		strFilePathName += L".bmp";
		break;
	case 1:
		strFilePathName += L".jpg";
		break;
	case 2:
	//	strFilePathName += L".tga";
	//	break;
	case 3:
	//	strFilePathName += L".png";
	//	break;
	case 4:
		strFilePathName += L".dds";
	    break;
	default:
	    break;
	}

	void *buf = NULL;
	int size = 0;
	if(m_pRenderer->PrintScreen(&buf, &size, m_nFileFormat, &m_RctScreen))
	{
		FILE *f= Nuclear::FOpen(strFilePathName, L"wb");
		if(f)
		{
			Nuclear::FWrite(f, buf, size);
			fclose(f);
		}
		Nuclear::XFree(buf);
	}

	m_nFrameIndex++;	
}

void CParticleSystemEditorView::SetPrintScreenParam(CString filePath, CString pslName, Nuclear::XPIMAGE_FILEFORMAT fileformat, Nuclear::CRECT srcRct, int nFramePerSecond)
{
	m_strFilePath = filePath;

	m_strPslName = pslName;
	std::wstring wsTmp = FNExt_ParticleWS;
	int pos = pslName.Find((L"." + wsTmp).c_str());
	if(pos != -1) //把扩展名弃掉
	{
		CString resonableName = pslName.Left(pos);
		m_strPslName = resonableName;
	}

	pos = m_strPslName.ReverseFind('\\'); 
	if(pos != -1)//把路径弃掉，只留下文件名
	{
		m_strPslName = m_strPslName.Right(m_strPslName.GetLength() - pos -1);
	}
	
	m_nFileFormat = fileformat;
	
	if( (m_nFileFormat == 2) || (m_nFileFormat == 3) ) //临时添加的..因为2 3 暂时不支持
		m_nFileFormat = Nuclear::XPIFF_DDS;	
	
	m_RctScreen = srcRct; //先向右下减一个像素(否则会把包围盒的边也截下来), 然后再减成4的倍数
	m_RctScreen.left += 1;
	m_RctScreen.top += 1;

	/*转成2的幂也没用
	int nWidth = m_RctScreen.Width();
	int nHeight = m_RctScreen.Height();
	int cx = 1;
	while(cx <= nWidth)
	{
		cx = cx<<1;
	}
	nWidth = cx>>1;

	int cy = 1;
	while(cy <= nHeight)
	{
		cy = cy<<1;
	}
	nHeight = cy>>1;

	m_RctScreen.right = m_RctScreen.left + nWidth;
	m_RctScreen.bottom = m_RctScreen.top + nHeight;
	*/

//	m_RctScreen.right -= (m_RctScreen.right - m_RctScreen.left)%4;
//	m_RctScreen.bottom -= (m_RctScreen.bottom - m_RctScreen.top)%4;

	m_nFramePerSecond = nFramePerSecond;
}

void CParticleSystemEditorView::StrtAni()
{//开始保存序列帧
	SetTimer(5, 1000/m_nFramePerSecond, NULL);
	m_nFrameIndex = 0;
}

void CParticleSystemEditorView::EndAni()
{//结束保存序列帧
	KillTimer(5);
	m_nFrameIndex = 0;
}

CString RootPath = _T("../res\\effect\\particle\\");

void CParticleSystemEditorView::OnBtnTga2dds()
{
	TGA2DDS(RootPath + _T("psl\\"));	
}

void TGA2DDS(CString path)
{
	CFileFind finder;
	CString strPath;
	CString strFile;

	BOOL bExist = finder.FindFile(path + _T("\\*.*"));

	while (bExist)
	{
		bExist = finder.FindNextFile();
		strPath = finder.GetFilePath();

		if (finder.IsDirectory() && !finder.IsDots())
		{
			TGA2DDS(strPath);
		}
		else
		{
			strFile = finder.GetFileName();
			std::wstring wsTmp = FNExt_ParticleWS;

			if (strFile.Find((_T(".") + wsTmp).c_str()) != -1)
			{
				CStdioFile cf;
				if (cf.Open(strPath, CFile::modeReadWrite | CFile::typeText))
				{
					ULONGLONG nFileLength = cf.GetLength();
					CString str;
					if(cf.Read((void*)str.GetBufferSetLength(nFileLength), sizeof(CString::XCHAR)*nFileLength))
					{
						int aaa = str.Find(_T("Texture0 = "));
						int bbb = str.Find(_T(".tga"));
						if (bbb != -1)
						{
							int count = bbb + 4 - aaa - 11;
							CString PathAndName = str.Mid(aaa + 11, count);
							CString subPath = _T("");

							if (PathAndName.ReverseFind(_T('\\')) != -1)
							{
								int ccc = PathAndName.ReverseFind(_T('\\'));
								subPath = PathAndName.Mid(0, ccc);
							}

							CString t = _T("texconv -f DXT5 -o ") + RootPath + _T("texture\\") + subPath + _T(" ") + RootPath + _T("texture\\") + PathAndName;

							char* buffer = new char[t.GetLength()*3 + 1];
							memset(buffer, 0, t.GetLength()*3 + 1);

							utf16toutf8(t.GetBuffer(), t.GetLength(), buffer, t.GetLength()*3 + 1);

							system(buffer);
							delete [] buffer;

							cf.Seek(bbb * sizeof(CString::XCHAR), CFile::begin);
							cf.Write(_T(".dds"), 4 * sizeof(CString::XCHAR));
						}
						
					}

					cf.Close();
				}

			}


		}



	}
}

size_t utf16toutf8(const wchar_t* u16, size_t size, char* u8, size_t outBufferSize)
{
	size_t converted = 0;

	while (size > 0)
	{
		if (*u16 < 0x80)
		{
			//1 byte
			if (converted == outBufferSize)
			{
				break;
			}
			*(u8++) = static_cast<char>(*(u16++));
			--size;
			++converted;
		}
		else if (*u16 < 0x800)
		{
			//2 bytes
			if (converted + 2 > outBufferSize)
			{
				break;
			}
			*u8 = (*u16 >> 6) | 0xc0;
			*(u8+1) = (*u16 & 0x3f) | 0x80;
			u8 += 2;
			++u16;
			--size;
			converted += 2;
		}
		else
		{
			//3 bytes
			if (converted + 3 > outBufferSize)
			{
				break;
			}
			*u8 = (*u16 >> 12) | 0xe0;
			*(u8+1) = ((*u16 >> 6) & 0x3f) | 0x80;
			*(u8+2) = (*u16 & 0x3f) | 0x80;
			u8 += 3;
			++u16;
			--size;
			converted += 3;
		}
	}
	return converted;
}
