// LinkedObjectEditorView.cpp : CLinkedObjectEditorView 类的实现
//

#include "stdafx.h"
#include "LinkedObjectEditor.h"

#include <Mmsystem.h>
#include "LinkedObjectEditorDoc.h"
#include "LinkedObjectEditorView.h"

#include "DlgLinkedObjectMoveParam.h"

#include "ZoomMoveParamDlg.h"

#include "..\engine\common\timer.h"
#include "..\engine\common\util.h"
#include "..\engine\common\pfsutil.h"
#include "..\engine\map\pimg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// CLinkedObjectEditorView

IMPLEMENT_DYNCREATE(CLinkedObjectEditorView, CView)

BEGIN_MESSAGE_MAP(CLinkedObjectEditorView, CView)
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
	ON_COMMAND(ID_MENU_DELCURSELOBJECT, &CLinkedObjectEditorView::OnMenuDelcurselobject)
	ON_COMMAND(ID_MENU_SETPLANTMOVEPARAM, &CLinkedObjectEditorView::OnMenuSetplantmoveparam)
	ON_WM_TIMER()
	ON_WM_KEYDOWN()
	ON_COMMAND(ID_MENU_LightObject, &CLinkedObjectEditorView::OnMenuLightobject)
	ON_COMMAND(ID_MENU_SETZOOMPARAM, &CLinkedObjectEditorView::OnMenuSetzoomparam)
	ON_COMMAND(ID_EDIT_UNDO, &CLinkedObjectEditorView::OnEditUndo)
	ON_UPDATE_COMMAND_UI(ID_EDIT_UNDO, &CLinkedObjectEditorView::OnUpdateEditUndo)
	ON_COMMAND(ID_EDIT_REDO, &CLinkedObjectEditorView::OnEditRedo)
	ON_UPDATE_COMMAND_UI(ID_EDIT_REDO, &CLinkedObjectEditorView::OnUpdateEditRedo)
END_MESSAGE_MAP()

// CLinkedObjectEditorView 构造/析构

CLinkedObjectEditorView::CLinkedObjectEditorView()
{
	// TODO: 在此处添加构造代码
	m_pRender = NULL;
	m_initialPoint = CPoint(0, 0);
	m_dwBKcolor = 0xffa0a0a0;
	m_fLastTime = 0.0f;
	m_bSetLinkedObjectPosCenter = false;
	m_bStop = false;

	m_dwPicIDMax = -1;
	m_curMouseFileID = -1;
	m_dwMainNodeID = -1;
	m_curSelFileID = -1;
	m_dwMainNodeID_last = -1;

	m_bDrawBorder = false;
	m_bMoveBorder = false;
	m_nMovePointIndex = -1;

	for(int i=0; i<4; i++)
	{
		m_ptBasePos[i] = Nuclear::CPOINT(0, 0);
		m_ptPos[i] = CPoint(0, 0);
	}

	m_ptCentPos = Nuclear::CPOINT(0, 0);

	m_bLoadPic = false;

	m_bLKOEnableZoom = false; //是否允许被干扰
	m_fLKOzoom = 2.0f;//干扰最激烈程度
	m_fLKOTime = 5.0f;//恢复所需时间

	m_nLightAlpha = 255;

	m_VectorUndoOperations.clear();
	m_VectorUndoOperations.clear();

	m_bDragFlag = true;
	m_bPreviewLKO = false;

	m_fmtMap[Nuclear::XPTEXFMT_A8R8G8B8] = std::wstring(L"A8R8G8B8");
	m_fmtMap[Nuclear::XPTEXFMT_A4R4G4B4] = std::wstring(L"A4R4G4B4");
	m_fmtMap[Nuclear::XPTEXFMT_DXT1] = std::wstring(L"DXT1");
	m_fmtMap[Nuclear::XPTEXFMT_DXT2] = std::wstring(L"DXT2");
	m_fmtMap[Nuclear::XPTEXFMT_DXT3] = std::wstring(L"DXT3");
	m_fmtMap[Nuclear::XPTEXFMT_DXT4] = std::wstring(L"DXT4");
	m_fmtMap[Nuclear::XPTEXFMT_DXT5] = std::wstring(L"DXT5");
}

CLinkedObjectEditorView::~CLinkedObjectEditorView()
{
	//free纹理..
	for(std::map<std::wstring, PicHandleInfoVetor>::const_iterator it = m_mapPicHandle.begin(), ie = m_mapPicHandle.end(); it != ie; ++it)
	{
		PicHandleInfoVetor handleInfoVector = it->second;
		int cnt = (int)handleInfoVector.size();
		for(int i=0; i<cnt; i++)
		{
			m_pRender->FreePicture(handleInfoVector.at(i).m_handlePic);
		}
		handleInfoVector.clear();
	}
	m_mapPicHandle.clear();
	m_mapLinkedObjectInfo.clear();

	DestroyRenderer(m_pRender);

	m_DynamicLinkedObject.RemoveAllNode();
}

BOOL CLinkedObjectEditorView::PreCreateWindow(CREATESTRUCT& cs)
{
	// TODO: 在此处通过修改
	//  CREATESTRUCT cs 来修改窗口类或样式

	return CView::PreCreateWindow(cs);
}

// CLinkedObjectEditorView 绘制

void CLinkedObjectEditorView::OnDraw(CDC* pDC)
{
	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;
//	Render(); //目的是绘重心底座..	

	//
	// TODO: 在此处为本机数据添加绘制代码
}

void CLinkedObjectEditorView::SetLightAlpha(int nLightAlpha)
{
	m_nLightAlpha = nLightAlpha;
}

int CLinkedObjectEditorView::GetDTListIndex(DWORD dwFileID) //返回对应ID的物件在dtlist中的存放位置
{
	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return -1;
	if(dwFileID == -1)
		return -1;

	if(dwFileID == m_dwMainNodeID)
		return 0; //要查询的是主节点..主节点存放在第一个位置

	int index = -1;
	int mainNode = -1; //绘制顺序..

	CString str;
	str.Format(L"%d", m_dwMainNodeID);

	LVFINDINFO info;
	info.flags = LVFI_PARTIAL|LVFI_STRING;
	info.psz = str;

	int ib =  pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.FindItem(&info);
	if(ib != -1)
	{
		mainNode = ib;
	}
	else
	{
		int a = 0;
		OutputDebugString(L"没有主节点。。。GetDTListIndex()---1\n");
	}

	str.Format(L"%d", dwFileID);
	info.flags = LVFI_PARTIAL|LVFI_STRING;
	info.psz = str;
	ib =  pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.FindItem(&info);
	if(ib != -1)
	{
		index = ib;
	}
	else
	{
		int a = 0;
		OutputDebugString(L"没有找到要对应key的节点。。。GetDTListIndex()---2\n");
	}

	if(index < mainNode)
		index++;

// 	if(index == mainNode)
// 	{ //事实上这种情况在上面就返回了..
// 		index = 0;
// 	}
// 	else if(index > mainNode)
// 	{
// 		index = index; //位置关系保持不变
// 	}
// 	else
// 	{
// 		index = index+1; //主节点提前了..该节点位置应该往后挪一个
// 	}
	return index;
}
void CLinkedObjectEditorView::OnInitialUpdate()
{
	SetTimer(1, 1000, NULL);
	SetTimer(2, 30, NULL);
}

void CLinkedObjectEditorView::RenderPreviewLKO()
{
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
	}

	float fCurTime = (float)timeGetTime();
	float timeDelta = (fCurTime - m_fLastTime)*0.001f;	
	m_fLastTime = fCurTime;
	if( timeDelta>0.2f )
		timeDelta = 0.0f;

	m_DynamicLinkedObjectPreview.Update((int)(timeDelta *1000), 0, 0, 1.0f); //timeDelta 毫秒

	m_pRender->Begin(m_dwBKcolor);

	Nuclear::DrawPictureParam param;

	Nuclear::DTList &dtlist = m_DynamicLinkedObjectPreview.GetLinkedObjectList();

	Nuclear::PLinkedObject::VectorLinkedObjectInfo::iterator iter = m_plantObjectPreview.m_vectorLinkedObjectInfo.begin() + m_plantObjectPreview.GetMainNodeID();
	std::wstring imgFilename;
//	float mainCenterX, mainCenterY;
//	mainCenterX = (m_plantObjectPreview.m_ptCentPos.x - iter->m_nWidth / 2);
//	mainCenterY = (m_plantObjectPreview.m_ptCentPos.y - iter->m_nHeight / 2);
	iter = m_plantObjectPreview.m_vectorLinkedObjectInfo.begin();
	for(int i=0; iter!=m_plantObjectPreview.m_vectorLinkedObjectInfo.end(); iter++,i++)
	{
		imgFilename = iter->m_strPicName;

		PicHandleInfoVetor picHandles = m_mapPreviewPicHandle[imgFilename];
		int cntHandle = (int)(picHandles.size());
		if(cntHandle==0)
		{
			OutputDebugString(L"RenderPreviewLKO()---picHandles.size()==0\n");
			continue;
		}

		int index = i;
		int mainNode = m_plantObjectPreview.GetMainNodeID();

		if(i == mainNode)
		{
			index = 0;
		}
		else if(i > mainNode)
		{
			index = i; //位置关系保持不变
		}
		else
		{
			index = i+1; //主节点提前了..该节点位置应该往后挪一个
		}

		param.pMatrix = &(dtlist[index]->mTransResult);

		for(int j=0; j<cntHandle; j++)
		{
			Nuclear::FRECT rct_temp = picHandles[j].m_rct.ToFRECT();
			param.handle = picHandles[j].m_handlePic;
			param.pRect = &rct_temp;

			m_pRender->DrawPicture(param);
		}

	//	if (iter->m_bMainNode)
	}

	//包围盒、主节点以及运动参数等信息需要预览吗？...
	int todo;
	//

	m_pRender->End();
}

void CLinkedObjectEditorView::Render()
{
	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	if( GetCapture()==this )
		return; //鼠标操作...

	if( m_pRender == NULL ) 
	{
		CRect rectClient;
		GetClientRect(&rectClient);

		//rectClient.right-rectClient.left, rectClient.bottom-rectClient.top
		HRESULT hr = CreateRenderer(&m_pRender, m_hWnd, Nuclear::XDisplayMode(GetSystemMetrics(SM_CXSCREEN), GetSystemMetrics(SM_CYSCREEN)), 
							theApp.m_pFileIOManager, Nuclear::XPCRF_VERTICALSYNC);
		if( FAILED(hr) )
			return;

		Nuclear::CRECT sourceRect(0, 0, rectClient.right-rectClient.left, rectClient.bottom-rectClient.top);
		m_pRender->SetSourceRect(&sourceRect);
	}

	if(!m_bLoadPic)
	{
		InitData();
		m_bLoadPic = true;
	}

	float fCurTime = (float)timeGetTime();
	float timeDelta = (fCurTime - m_fLastTime)*0.001f;	
	m_fLastTime = fCurTime;
	if( timeDelta>0.2f )
		timeDelta = 0.0f;

	HashMapLinkedObjectInfo::iterator it_3 = m_mapLinkedObjectInfo.find(m_dwMainNodeID);
	if(m_bSetLinkedObjectPosCenter || m_bStop)
		timeDelta = 0.0f;

	m_DynamicLinkedObject.Update((int)(timeDelta *1000), 0, 0, 1.0f); //timeDelta 毫秒

	m_pRender->Begin(m_dwBKcolor);
	
	int cnt = pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.GetItemCount();
	Nuclear::XPMATRIX3 m1, m2;
	Nuclear::DrawPictureParam param;

	Nuclear::PLinkedObject plantObject;
	plantObject.m_vectorLinkedObjectInfo.clear();

	Nuclear::XPCOLOR lightColor(unsigned char(m_nLightAlpha), 255, 255, 255);
	Nuclear::XPCOLOR dwcolor(255, 255, 255, 255);

	std::vector<Nuclear::XPVECTOR2> vectorRotatePoint;
	Nuclear::XPVECTOR2 currentPointt;

	Nuclear::DTList &dtlist = m_DynamicLinkedObject.GetLinkedObjectList();
	for(int i=0; i<cnt; i++)
	{
		DWORD fileID = _wtoi( pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.GetItemText(i, 0) );
		HashMapLinkedObjectInfo::const_iterator it = m_mapLinkedObjectInfo.find(fileID);
		PicHandleInfoVetor handleInfoVector;
		handleInfoVector = m_mapPicHandle[it->second.m_strPicName];
		int cntHandle = (int)handleInfoVector.size();
		if(cntHandle==0)
		{
			OutputDebugString(L"Render()---handleInfoVector.size()==0\n");
			continue;
		}

		int index = i;
		int mainNode = i;

		CString str;
		str.Format(L"%d", m_dwMainNodeID);

		LVFINDINFO info;
		info.flags = LVFI_PARTIAL|LVFI_STRING;
		info.psz = str;

		int ib =  pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.FindItem(&info);
		if(ib != -1)
		{
			mainNode = ib;
		}
		else
		{
			int a = 0;
			OutputDebugString(L"出错啦。。。Render()--\n");
		}

		if(i == mainNode)
		{
			index = 0;
		}
		else if(i > mainNode)
		{
			index = i; //位置关系保持不变
		}
		else
		{
			index = i+1; //主节点提前了..该节点位置应该往后挪一个
		}

		param.pMatrix = &(dtlist[index]->mTransResult);
		//index有问题...fileID才是正确地key--用不存在的key去访问map,map就会加一条对应的key的默认构造对象
		bool bLight = m_mapLinkedObjectInfo[fileID].m_LinkedObjectMoveParam.m_dwFlag & Nuclear::XP_LINKEDOBJ_LIGHT;
		param.pColor = &dwcolor;
		if(bLight)
		{
			param.pColor = &lightColor;
		}

		for(int j=0; j<cntHandle; j++)
		{
			Nuclear::FRECT rct_temp = handleInfoVector.at(j).m_rct.ToFRECT();
			param.handle = handleInfoVector.at(j).m_handlePic;
			param.pRect = &rct_temp;

			m_pRender->DrawPicture(param);
		}

		Nuclear::XPVECTOR2 rotatePt;
		Nuclear::XPVECTOR2 pt;
		pt.x = it->second.m_rotateCent.x + it->second.m_nWidth /2.0f;
		pt.y = it->second.m_rotateCent.y + it->second.m_nHeight /2.0f;
		Nuclear::XPVec3TransformCoord(&rotatePt, &pt, param.pMatrix);

		vectorRotatePoint.push_back(rotatePt);
	//	m_pRender->DrawCircle(rotatePt.x, rotatePt.y, 2, 0xffff0000, Nuclear::XPFM_WIREFRAME);
		if(it->first == m_curSelFileID)
		{
			currentPointt = rotatePt;
		//	m_pRender->DrawCircle(rotatePt.x, rotatePt.y, 2, 0xff00ff00, Nuclear::XPFM_WIREFRAME);
		}
	}
	
	//标示出旋转中心点 ---画完图片再画点，否则可能被图片把点盖住。
	cnt = (int)(vectorRotatePoint.size());
	Nuclear::XPVECTOR2 pt;
	for(int i=0; i<cnt; i++)
	{
		pt = vectorRotatePoint[i];
		m_pRender->DrawCircle(pt.x, pt.y, 2, 0xffff0000, Nuclear::XPFM_WIREFRAME);
		m_pRender->DrawCircle(currentPointt.x, currentPointt.y, 2, 0xff00ff00, Nuclear::XPFM_WIREFRAME);
	}
	//

	//标示当前选中物件
	HashMapLinkedObjectInfo::const_iterator it = m_mapLinkedObjectInfo.find(m_curSelFileID);
	if(it != m_mapLinkedObjectInfo.end())
	{
		int index = GetDTListIndex(m_curSelFileID);

		Nuclear::XPVECTOR2 vect[4];
		Nuclear::XPVECTOR2 vec;
		Nuclear::XPMATRIX3 *pM = &dtlist[index]->mTransResult;
		vec.x = 0;
		vec.y = 0;
		Nuclear::XPVec3TransformCoord(vect, &vec, pM);
		vec.x = (float)( it->second.m_nWidth );
		vec.y = 0.0f;
		Nuclear::XPVec3TransformCoord(vect+1, &vec, pM);
		vec.y = (float)( it->second.m_nHeight );
		Nuclear::XPVec3TransformCoord(vect+2, &vec, pM);
		vec.x = 0.0f;
		Nuclear::XPVec3TransformCoord(vect+3, &vec, pM);
		for (int i=0; i<3; ++i)
		{
			m_pRender->DrawLine(vect[i].x, vect[i].y, vect[i+1].x, vect[i+1].y, 0xff00ff00);
		}
		m_pRender->DrawLine(vect[0].x, vect[0].y, vect[3].x, vect[3].y, 0xff00ff00);
	}

	if(m_curSelFileID != m_curMouseFileID)
	{
		HashMapLinkedObjectInfo::const_iterator it = m_mapLinkedObjectInfo.find(m_curMouseFileID);
		if(it != m_mapLinkedObjectInfo.end())
		{
			int index = GetDTListIndex(m_curMouseFileID);

			Nuclear::XPVECTOR2 vect[4];
			Nuclear::XPVECTOR2 vec;
			Nuclear::XPMATRIX3 *pM = &dtlist[index]->mTransResult;
			vec.x = 0.0f;
			vec.y = 0.0f;
			Nuclear::XPVec3TransformCoord(vect, &vec, pM);
			vec.x = (float)( it->second.m_nWidth );
			vec.y = 0.0f;
			Nuclear::XPVec3TransformCoord(vect+1, &vec, pM);
			vec.y = (float)( it->second.m_nHeight );
			Nuclear::XPVec3TransformCoord(vect+2, &vec, pM);
			vec.x = 0.0f;
			Nuclear::XPVec3TransformCoord(vect+3, &vec, pM);
			for (int i=0; i<3; ++i)
			{
				m_pRender->DrawLine(vect[i].x, vect[i].y, vect[i+1].x, vect[i+1].y, 0xffff0000);
			}
			m_pRender->DrawLine(vect[0].x, vect[0].y, vect[3].x, vect[3].y, 0xffff0000);
		}
	}

	//绘制重心底座..
	int centerPointX0 = (int)( m_ptCentPos.x + m_FrctMainNodPos.left );
	int centerPointY0 = (int)( m_ptCentPos.y + m_FrctMainNodPos.top );
	m_pRender->DrawLine(centerPointX0-8.0f, centerPointY0-8.0f, centerPointX0+8.0f, centerPointY0+8.0f, 0xffff0000);
	m_pRender->DrawLine(centerPointX0+8.0f, centerPointY0-8.0f, centerPointX0-8.0f, centerPointY0+8.0f, 0xffff0000);

	Nuclear::FPOINT p[4];
	p[0].x = (float)m_ptBasePos[Nuclear::enum_left].x  + m_FrctMainNodPos.left;
	p[0].y = (float)m_ptBasePos[Nuclear::enum_left].y  + m_FrctMainNodPos.top;
	p[1].x = (float)m_ptBasePos[Nuclear::enum_bottom].x  + m_FrctMainNodPos.left;
	p[1].y = (float)m_ptBasePos[Nuclear::enum_bottom].y  + m_FrctMainNodPos.top;
	p[2].x = (float)m_ptBasePos[Nuclear::enum_right].x  + m_FrctMainNodPos.left;
	p[2].y = (float)m_ptBasePos[Nuclear::enum_right].y  + m_FrctMainNodPos.top;
	p[3].x = (float)m_ptBasePos[Nuclear::enum_up].x  + m_FrctMainNodPos.left;
	p[3].y = (float)m_ptBasePos[Nuclear::enum_up].y  + m_FrctMainNodPos.top;

	for(int i=0; i<4; i++)
	{
		int j = (i+1)>3 ? 0:(i+1);
		m_pRender->DrawLine(p[i].x, p[i].y, p[j].x, p[j].y, 0xffff00ff);

		m_pRender->DrawEllipse(p[i].x, p[i].y, 3, 3, 0xffc0c0c0, Nuclear::XPFM_WIREFRAME);
	}

	//绘制最大包围盒
	plantObject.m_ptCentPos = m_ptCentPos;
	for(int i=0; i<4; i++)
		plantObject.m_ptBasePos[i] = m_ptBasePos[i];
	
	Nuclear::CRECT rct = ComputeLinkedObjectMaxBox();
	Nuclear::FRECT frct;// = pDoc->GetLinkedObjectObject().m_rctMaxBox.ToFRECT();
	frct = rct.ToFRECT();
	frct += Nuclear::FPOINT((float)centerPointX0, (float)centerPointY0);
	m_pRender->DrawBox(frct, 0xff0000ff, Nuclear::XPFM_WIREFRAME);
	//

	RECT trect;
	TCHAR szbuffer[256];
	int f1 = (int)(m_pRender->GetLatestFPS()*10);
	wsprintf(szbuffer, _T("FPS: %3d.%1d"), f1/10, f1%10);	
	trect.left = 0;
	trect.top =0;
	trect.right =  500;
	trect.bottom = 300;
	m_pRender->DrawText(szbuffer, 11, &trect, 0xffff00ff);

	m_pRender->End();
}

void CLinkedObjectEditorView::Update(float timeDelta) //秒
{
}

// CLinkedObjectEditorView 打印

BOOL CLinkedObjectEditorView::OnPreparePrinting(CPrintInfo* pInfo)
{
	// 默认准备
	return DoPreparePrinting(pInfo);
}

void CLinkedObjectEditorView::OnBeginPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	// TODO: 添加额外的打印前进行的初始化过程
}

void CLinkedObjectEditorView::OnEndPrinting(CDC* /*pDC*/, CPrintInfo* /*pInfo*/)
{
	// TODO: 添加打印后进行的清除过程
}

// CLinkedObjectEditorView 诊断

#ifdef _DEBUG
void CLinkedObjectEditorView::AssertValid() const
{
	CView::AssertValid();
}

void CLinkedObjectEditorView::Dump(CDumpContext& dc) const
{
	CView::Dump(dc);
}

#endif //_DEBUG


// CLinkedObjectEditorView 消息处理程序

int CLinkedObjectEditorView::AddPicMap(CString strFilePath)
{
	m_bLoadPic = true;

	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	if(pDoc == NULL)
		return -1;

	m_dwPicIDMax++;

	//读img文件初始化一个img对象..下面使用其相关信息来load纹理和宽高等...
	Nuclear::PImg imgObject;
	FILE *f = NULL;
	_tfopen_s(&f, strFilePath, _T("rb"));
	if( f == NULL )
		return -2;

	BOOL ret = imgObject.Load(f) ? TRUE:FALSE;
	fclose(f);
	if(!ret)
		return -3;

	int nPicWidth = imgObject.m_nFileWidth;
	int nPicHeight = imgObject.m_nFileHeight;

//	std::wstring fileName;
//	int npos = strFilePath.ReverseFind('\\');
//	fileName = strFilePath.Right(strFilePath.GetLength() -npos-1);

	std::wstring strKey;//elments下包含子目录名的文件名
	int len = strFilePath.GetLength();
	int npos = strFilePath.Find(L"\\map\\elements\\");
	strKey = strFilePath.Right(len-npos);
	std::wstring TempKey = strKey;
	bool bres = PFS::CEnv::NativeFileNameToPFSFileName(TempKey, strKey);

	std::transform(strKey.begin(), strKey.end(), strKey.begin(), ::towlower);
	//
	std::map<std::wstring, PicHandleInfoVetor>::const_iterator it = m_mapPicHandle.find(strKey);
	if(it == m_mapPicHandle.end())
	{
		//读img文件初始化handleInfoVector--

		//根据文件名来加载显示...
		PicHandleInfoVetor handleInfoVector;
		handleInfoVector.clear();
		int nPos = strFilePath.ReverseFind('.');
		CString filePathNoEX = strFilePath.Left(nPos);

		int cnt = (int)imgObject.m_vectorRct.size();
		for(int i=0; i<cnt; i++)
		{
			CString str;
			str.Format(_T("_%d"), i);
			CString fileNameNoEx = filePathNoEX + str;
			Nuclear::PictureHandle handle = m_pRender->LoadPictureFromNativePath(std::wstring(fileNameNoEx));

			IMGPICHANDLEINFO picInfo;
			picInfo.m_handlePic = handle;
			picInfo.m_rct = imgObject.m_vectorRct.at(i);
			handleInfoVector.push_back(picInfo);
		}

		//
		m_mapPicHandle.insert( std::make_pair(strKey, handleInfoVector) );
	}

//	std::map<std::wstring, Nuclear::PLANTPICINFO>::const_iterator iter = m_mapLinkedObjectInfo.find(fileName);
//	if(iter == m_mapLinkedObjectInfo.end()) //一棵树上不允许有两片相同的树叶..
	{
		Nuclear::PLANTPICINFO LinkedObjectPicInfo;
		LinkedObjectPicInfo.m_strPicName = strKey;
		LinkedObjectPicInfo.m_nWidth = nPicWidth;
		LinkedObjectPicInfo.m_nHeight = nPicHeight;

		Nuclear::LkoTreeNode* pNode = new Nuclear::LkoTreeNode();
		Nuclear::LkoTreeNode* pRootNode = NULL;
		Nuclear::CPOINT rootRotateCent = Nuclear::CPOINT();
		Nuclear::PLANTMOVEPARAM LinkedObjectMoveParam;

		int cnt = (int)m_mapLinkedObjectInfo.size();
		if(cnt == 0) //加入的第一个节点设置为主节点..
		{
			LinkedObjectPicInfo.m_bMainNode = true;
			LinkedObjectPicInfo.m_ptOffset = Nuclear::CPOINT();
			m_FrctMainNodPos = Nuclear::FRECT(0.0f, 0.0f, (float)nPicWidth, (float)nPicHeight);
			m_dwMainNodeID = m_dwPicIDMax;

			pNode->pParent = NULL;
			pNode->pRoot = pNode;
			pRootNode = pNode;
			pNode->LinkedObjectMoveParam.m_nMoveFollowType = Nuclear::XPMOVEFOLLOWNO;
			pNode->height = nPicHeight;
			pNode->width = nPicWidth;
			pNode->rotateCent = Nuclear::CPOINT();
			rootRotateCent = pNode->rotateCent;
			pNode->height = nPicHeight;
			pNode->width = nPicWidth;
			pNode->fromMainRotateCent = Nuclear::FPOINT(m_FrctMainNodPos.Width()/2, m_FrctMainNodPos.Height()/2);			
			pNode->rotateCent = pNode->rotateCent +Nuclear::CPOINT(nPicWidth/2, nPicHeight/2);
			pNode->LinkedObjectMoveParam = LinkedObjectMoveParam;
			pNode->fAngle = pNode->fSkewx = 0.0f;
			pNode->fTranslationx = pNode->fTranslationy = 0.0f;

			//从img文件中读出重心底座...
			m_ptCentPos = imgObject.CenterPoint; //plant的重心,(相对于主节点的左上角..)
			for(int a=0; a<4; a++)
			{
				m_ptBasePos[a] = imgObject.pos[a];//plant的底座,----位置关系同上	
				m_ptPos[a] = CPoint(m_ptBasePos[a].x, m_ptBasePos[a].y) +CPoint((int)m_FrctMainNodPos.left, (int)m_FrctMainNodPos.top);
			}
		}
		else
		{
			LinkedObjectPicInfo.m_bMainNode = false;
			LinkedObjectPicInfo.m_ptOffset = Nuclear::CPOINT(rand()%200, rand()%300);
			LinkedObjectPicInfo.m_rotateCent = Nuclear::CPOINT();

			int cx = (int)( m_FrctMainNodPos.Width()/2.0f );
			int cy = (int)( m_FrctMainNodPos.Height()/2.0f );
			LinkedObjectPicInfo.m_rotateCent_2 = LinkedObjectPicInfo.m_ptOffset - Nuclear::CPOINT(cx, cy) +Nuclear::CPOINT(nPicWidth/2, nPicHeight/2);

			Nuclear::DTList& dtlist = m_DynamicLinkedObject.GetLinkedObjectList();
			pNode->pParent = dtlist[0];
			pNode->pRoot = pNode->pParent;
			pNode->LinkedObjectMoveParam.m_nMoveFollowType = Nuclear::XPMOVEFOLLOWNO;
			pNode->height = nPicHeight;
			pNode->width = nPicWidth;
			pNode->rotateCent = Nuclear::CPOINT();
			pNode->height = nPicHeight;
			pNode->width = nPicWidth;

			HashMapLinkedObjectInfo::const_iterator itRoot = m_mapLinkedObjectInfo.find(m_dwMainNodeID);
			if(itRoot == m_mapLinkedObjectInfo.end())
			{
				MessageBox(L"逻辑错误...没有主节点？？AddPicMap()---\n");
			}

			Nuclear::CPOINT pt0 = LinkedObjectPicInfo.m_rotateCent + Nuclear::CPOINT(nPicWidth/2, nPicHeight/2) + LinkedObjectPicInfo.m_ptOffset;
			Nuclear::CPOINT pt1 = itRoot->second.m_rotateCent + Nuclear::CPOINT(cx, cy);
			pNode->fromMainRotateCent = (pt0 - pt1).ToFPOINT();
				//LinkedObjectPicInfo.m_ptOffset.ToFPOINT() + Nuclear::FPOINT(m_FrctMainNodPos.left/2, m_FrctMainNodPos.top/2);			
			pNode->rotateCent = pNode->rotateCent +Nuclear::CPOINT(nPicWidth/2, nPicHeight/2);
			pNode->LinkedObjectMoveParam = LinkedObjectMoveParam;
			pNode->fAngle = pNode->fSkewx = 0.0f;

			pNode->fTranslationx = pNode->fTranslationy = 0.0f;
		}
		m_mapLinkedObjectInfo.insert( std::make_pair(m_dwPicIDMax, LinkedObjectPicInfo) );

		IMGPICMOVEPARAM DefaultMoveParam;
		DefaultMoveParam.m_ptOffset = LinkedObjectPicInfo.m_ptOffset;
		m_mapMoveParam.insert( std::make_pair(m_dwPicIDMax, DefaultMoveParam) );

		pDoc->GetLinkedObjectStatusView()->m_strFileNameList.push_back(strKey.c_str());
		pDoc->GetLinkedObjectStatusView()->Invalidate(true);

		//先要得到当前的最大ID...2-27下午修改
		cnt = pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.GetItemCount();
		CString str;
		str.Format(L"%d", m_dwPicIDMax);
		pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.InsertItem(cnt, str);
		pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.SetItemText(cnt, 1, strKey.c_str());
		str.Format(_T("%d"), LinkedObjectPicInfo.m_bMainNode);
		pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.SetItemText(cnt, 2, str);

		str.Format(_T("%d"), LinkedObjectPicInfo.m_LinkedObjectMoveParam.m_nMoveFollowType);
		pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.SetItemText(cnt, 3, str);

		str.Format(_T("%d"), LinkedObjectPicInfo.m_LinkedObjectMoveParam.m_dwFlag);
		pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.SetItemText(cnt, 4, str);

		Nuclear::XPTEXTURE_FORMAT nfmt = (Nuclear::XPTEXTURE_FORMAT)(imgObject.m_texfmt);
		str = m_fmtMap[nfmt].c_str();
		pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.SetItemText(cnt, 5, str);

		m_DynamicLinkedObject.AddANode(pNode);

	//	int index = cnt;
	//	int nMainNode = m_dwMainNodeID;
	//	m_DynamicLinkedObject.InsertLinkedObjectNode(pNode, index, nMainNode);
	}

	pDoc->SetModifiedFlag(true);

	return 1;
}

void CLinkedObjectEditorView::OnSize(UINT nType, int cx, int cy)
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

void CLinkedObjectEditorView::OnSizing(UINT fwSide, LPRECT pRect)
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

void CLinkedObjectEditorView::OnRButtonDown(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	CPoint pt;
	::GetCursorPos(&pt);
	if(m_curSelFileID != -1)
	{
		CMenu menu;
		menu.LoadMenu(IDR_MENU1);
		menu.EnableMenuItem(ID_MENU_DELCURSELOBJECT, MF_ENABLED | MF_BYCOMMAND);
		menu.EnableMenuItem(ID_MENU_SETPLANTMOVEPARAM, MF_ENABLED | MF_BYCOMMAND);

		menu.EnableMenuItem(ID_MENU_LightObject, MF_ENABLED | MF_BYCOMMAND);
 
 		bool bMainNode = m_mapLinkedObjectInfo[m_curSelFileID].m_bMainNode;
 		if(bMainNode)
 			menu.EnableMenuItem(ID_MENU_LightObject, MF_DISABLED | MF_GRAYED | MF_BYCOMMAND);
 
		bool bLight = m_mapLinkedObjectInfo[m_curSelFileID].m_LinkedObjectMoveParam.m_dwFlag & Nuclear::XP_LINKEDOBJ_LIGHT;
 		if(bLight)
 			menu.CheckMenuItem(ID_MENU_LightObject, MF_CHECKED | MF_BYCOMMAND);

		CMenu* pMenu = menu.GetSubMenu(0);
		pMenu->TrackPopupMenu(TPM_LEFTALIGN | TPM_RIGHTBUTTON, pt.x, pt.y, this, NULL);
	}
	else
	{
		//摇晃参数测试.. IDR_MENU_ZOOMPARAM
		CMenu menu;
		menu.LoadMenu(IDR_MENU_ZOOMPARAM);
		menu.EnableMenuItem(ID_MENU_SETZOOMPARAM, MF_ENABLED | MF_BYCOMMAND);

		CMenu* pMenu = menu.GetSubMenu(0);
		pMenu->TrackPopupMenu(TPM_LEFTALIGN | TPM_RIGHTBUTTON, pt.x, pt.y, this, NULL);
	}

	CView::OnRButtonDown(nFlags, point);
}

void CLinkedObjectEditorView::OnLButtonDblClk(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	if(m_bSetLinkedObjectPosCenter)
	{
		//设置重心、底座...
	}
	else
	{	
		//m_rotateCent	
		if(m_curSelFileID != -1)
		{
			HashMapLinkedObjectInfo::iterator it = m_mapLinkedObjectInfo.find(m_curSelFileID);
			if(it != m_mapLinkedObjectInfo.end())
			{
				Nuclear::PLANTPICINFO plantPicInfo = it->second;
				int left = (int)m_FrctMainNodPos.left + m_mapMoveParam[it->first].m_ptOffset.x;//plantPicInfo.m_ptOffset.x;
				int top = (int)m_FrctMainNodPos.top + m_mapMoveParam[it->first].m_ptOffset.y;//plantPicInfo.m_ptOffset.y;

				int centx = left + plantPicInfo.m_nWidth/2;
				int centy = top + plantPicInfo.m_nHeight/2;

				Nuclear::CPOINT oldRotateCent = plantPicInfo.m_rotateCent;

				plantPicInfo.m_rotateCent.x = point.x - centx;
				plantPicInfo.m_rotateCent.y = point.y - centy;

				HashMapLinkedObjectInfo::iterator it_2 = m_mapLinkedObjectInfo.find(m_dwMainNodeID);
				if(it_2 != m_mapLinkedObjectInfo.end())
				{
					Nuclear::PLANTPICINFO plantPicInfo_2 = it_2->second;
					int centx_2 = (int)( m_FrctMainNodPos.left + m_FrctMainNodPos.right )/2;
					int centy_2 = (int)( m_FrctMainNodPos.top + m_FrctMainNodPos.bottom )/2;
					plantPicInfo.m_rotateCent_2.x = point.x - centx_2;
					plantPicInfo.m_rotateCent_2.y = point.y - centy_2;
				}

				it->second = plantPicInfo;

				//
				Nuclear::CPOINT pos = m_ptCentPos + Nuclear::FPOINT(m_FrctMainNodPos.left, m_FrctMainNodPos.top);
				int index = GetDTListIndex(m_curSelFileID);
				Nuclear::DTList& dtlist = m_DynamicLinkedObject.GetLinkedObjectList();
				Nuclear::CPOINT rootRotateCent = Nuclear::CPOINT();
				if(index == 0)
				{
					dtlist[index]->rotateCent = plantPicInfo.m_rotateCent;
					rootRotateCent = plantPicInfo.m_rotateCent;

					Nuclear::CPOINT DeltaPt = rootRotateCent - oldRotateCent;

					dtlist[index]->rotateCent += Nuclear::CPOINT(plantPicInfo.m_nWidth/2, plantPicInfo.m_nHeight/2);
					dtlist[index]->fromMainRotateCent = (pos - m_ptCentPos + dtlist[0]->rotateCent).ToFPOINT();

					//同时需要修改其它节点..
					int cnt = (int)dtlist.size();
					for(int i=1; i<cnt; i++)
					{
						dtlist[i]->fromMainRotateCent -= DeltaPt.ToFPOINT();
					}
				}
				else
				{
					rootRotateCent = dtlist[0]->rotateCent - Nuclear::CPOINT(dtlist[0]->width/2, dtlist[0]->height/2);
					dtlist[index]->fromMainRotateCent = (plantPicInfo.m_rotateCent_2 - rootRotateCent).ToFPOINT();
				
					dtlist[index]->rotateCent = plantPicInfo.m_rotateCent;
					dtlist[index]->rotateCent += Nuclear::CPOINT(plantPicInfo.m_nWidth/2, plantPicInfo.m_nHeight/2);
				}
				//

				Invalidate(false);

				CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
				if (pDoc == NULL)
					return;
				pDoc->SetModifiedFlag(true);
			}
			else
			{
				MessageBox(_T("怎么会?--CLinkedObjectEditorView::OnLButtonDblClk"));
			}
		}
	}

	CView::OnLButtonDblClk(nFlags, point);
}

float CLinkedObjectEditorView::Computer2PointDis(CPoint pt1, CPoint pt2)
{
	float dis = powf((float)(pt1.x - pt2.x), 2.0f) + powf((float)(pt1.y - pt2.y), 2.0f);
	dis = sqrt(dis);
	return dis;
}

void CLinkedObjectEditorView::OnMenuDelcurselobject()
{
	//主节点不应许删除...
	// TODO: 在此添加命令处理程序代码
	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	if (pDoc == NULL)
		return;
	pDoc->SetModifiedFlag(true);

	if(m_curSelFileID == m_dwMainNodeID)
	{
		MessageBox(L"主节点不允许删除!\n要删除该节点请先设置别的节点为主节点...\n");
		return;
	}

	int index = GetDTListIndex(m_curSelFileID);
	if(index != -1)
	{
		m_DynamicLinkedObject.DelLinkedObjectNode(index);
	}
	else
	{
		OutputDebugString(L"error-DelNode- GetDTListIndex(m_curSelFileID) == -1 ---\n");
	}

	CString str;
	str.Format(L"%d", m_curSelFileID);

	LVFINDINFO info;
	int nIndex;
	info.flags = LVFI_PARTIAL|LVFI_STRING;
	info.psz = str;

	CString strFileName;
	nIndex =  pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.FindItem(&info);
	if(nIndex != -1)
	{
		strFileName = pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.GetItemText(nIndex, 1);
		pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.DeleteItem(nIndex);
	}
	else
	{
		MessageBox(L"逻辑错误\n");
	}

	//
	std::list<CString>::iterator iter = pDoc->GetLinkedObjectStatusView()->m_strFileNameList.begin();
	std::list<CString>::iterator itend = pDoc->GetLinkedObjectStatusView()->m_strFileNameList.end();
	for(; iter != itend; iter++)
	{
		if(*iter == strFileName)
		{
			pDoc->GetLinkedObjectStatusView()->m_strFileNameList.erase(iter);
			//remove同名的所有项都删除了...
			break; //删一个就行了
		}
	}
	
	pDoc->GetLinkedObjectStatusView()->Invalidate(true);
	//

	 DelNode(m_curSelFileID);
}

void CLinkedObjectEditorView::OnMenuSetplantmoveparam()
{
	CDlgLinkedObjectMoveParam dlg;
	Nuclear::PLANTMOVEPARAM param;
	HashMapLinkedObjectInfo::const_iterator it = m_mapLinkedObjectInfo.find(m_curSelFileID);
	if(it != m_mapLinkedObjectInfo.end())
	{
		param.m_fAngle1 = it->second.m_LinkedObjectMoveParam.m_fAngle1;
		param.m_fAngle2 = it->second.m_LinkedObjectMoveParam.m_fAngle2;
		param.m_fSpeed = it->second.m_LinkedObjectMoveParam.m_fSpeed;
		param.m_nDistortionType = it->second.m_LinkedObjectMoveParam.m_nDistortionType;
		param.m_nSkewType = it->second.m_LinkedObjectMoveParam.m_nSkewType;
		param.m_nMoveFollowType = it->second.m_LinkedObjectMoveParam.m_nMoveFollowType;
		param.m_nSkewPixelX = it->second.m_LinkedObjectMoveParam.m_nSkewPixelX;
		param.m_nSkewSpeedX = it->second.m_LinkedObjectMoveParam.m_nSkewSpeedX;

		param.m_fSizeLeft = it->second.m_LinkedObjectMoveParam.m_fSizeLeft;
		param.m_fSizeRight = it->second.m_LinkedObjectMoveParam.m_fSizeRight;
		param.m_fSizeTop = it->second.m_LinkedObjectMoveParam.m_fSizeTop;
		param.m_fSizeDown = it->second.m_LinkedObjectMoveParam.m_fSizeDown;
		param.m_fTranslationSpeedx = it->second.m_LinkedObjectMoveParam.m_fTranslationSpeedx;

		dlg.SetInitMoveParam(param);
	}

	if(IDOK == dlg.DoModal())
	{
		dlg.GetMoveParam(param);
		HashMapLinkedObjectInfo::iterator it = m_mapLinkedObjectInfo.find(m_curSelFileID);
		if(it != m_mapLinkedObjectInfo.end())
		{
			Nuclear::PLANTPICINFO plantPicInfo = it->second;
			plantPicInfo.m_LinkedObjectMoveParam.m_fAngle1 = param.m_fAngle1;
			plantPicInfo.m_LinkedObjectMoveParam.m_fAngle2 = param.m_fAngle2;
			plantPicInfo.m_LinkedObjectMoveParam.m_fSpeed = param.m_fSpeed;

			plantPicInfo.m_LinkedObjectMoveParam.m_nDistortionType = param.m_nDistortionType;
			plantPicInfo.m_LinkedObjectMoveParam.m_nSkewType = param.m_nSkewType;
			plantPicInfo.m_LinkedObjectMoveParam.m_nMoveFollowType = param.m_nMoveFollowType;
			plantPicInfo.m_LinkedObjectMoveParam.m_nSkewPixelX = param.m_nSkewPixelX;
			plantPicInfo.m_LinkedObjectMoveParam.m_nSkewSpeedX = param.m_nSkewSpeedX;

			plantPicInfo.m_LinkedObjectMoveParam.m_fSizeLeft = param.m_fSizeLeft;
			plantPicInfo.m_LinkedObjectMoveParam.m_fSizeRight = param.m_fSizeRight;
			plantPicInfo.m_LinkedObjectMoveParam.m_fSizeTop = param.m_fSizeTop;
			plantPicInfo.m_LinkedObjectMoveParam.m_fSizeDown = param.m_fSizeDown;
			plantPicInfo.m_LinkedObjectMoveParam.m_fTranslationSpeedx = param.m_fTranslationSpeedx;
			plantPicInfo.m_LinkedObjectMoveParam.m_fTranslationSpeedy = param.m_fTranslationSpeedy;

			Nuclear::CPOINT pt = Nuclear::CPOINT(0, 0);
			if(plantPicInfo.m_LinkedObjectMoveParam.m_nDistortionType == Nuclear::XPPLANTSKEW)
			{
				pt = plantPicInfo.m_rotateCent;
				if(plantPicInfo.m_LinkedObjectMoveParam.m_nSkewType == Nuclear::XPSKEWHORIZONTAL)
				{ //水平切
					plantPicInfo.m_rotateCent = Nuclear::CPOINT(0, plantPicInfo.m_nHeight/2);
				}
				else
				{//XPSKEWVERTICAL   //垂直切
					plantPicInfo.m_rotateCent = Nuclear::CPOINT(-plantPicInfo.m_nWidth/2, 0);
				}
				pt = pt - plantPicInfo.m_rotateCent;
				plantPicInfo.m_rotateCent_2 = plantPicInfo.m_rotateCent_2 -pt;
			}

			it->second = plantPicInfo;

			int index = GetDTListIndex(m_curSelFileID);
			//从父节点旋转中心到自己的旋转中心的向量
			//如果自己是根节点，那么这个就是在地图上的坐标（世界坐标）
			if(index != -1)
			{
				Nuclear::DTList &dtlist = m_DynamicLinkedObject.GetLinkedObjectList();
				dtlist[index]->LinkedObjectMoveParam = plantPicInfo.m_LinkedObjectMoveParam;
				dtlist[index]->rotateCent -= pt;
				dtlist[index]->fromMainRotateCent -= pt.ToFPOINT();
				if(index == 0)
				{
					//同时需要修改其它节点..
					int cnt = (int)dtlist.size();
					for(int i=1; i<cnt; i++)
					{
						dtlist[i]->fromMainRotateCent += pt.ToFPOINT();
					}
				}
			}
			else
			{
				OutputDebugString(L"error-OnMenuSetplantmoveparam- GetDTListIndex(m_curSelFileID) == -1 ---\n");
			}

			CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
			if (pDoc == NULL)
				return;

			CString str;
			LVFINDINFO info;
			info.flags = LVFI_PARTIAL|LVFI_STRING;
			str.Format(L"%d", m_curSelFileID);
			info.psz = str;
			int nIndex =  pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.FindItem(&info);

			// 使得nIndex位置处项目高亮显示并获得焦点
			if(nIndex != -1)
			{
//				pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.SetFocus();
				UINT flag = LVIS_SELECTED|LVIS_FOCUSED;
				pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.SetItemState(nIndex, flag, flag);
			}

			str.Format(L"%d", m_mapLinkedObjectInfo[m_curSelFileID].m_LinkedObjectMoveParam.m_nMoveFollowType);
			pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.SetItemText(nIndex, 3, str);
		}
	}

	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	if (pDoc == NULL)
		return;

	pDoc->SetModifiedFlag(true);
}

void CLinkedObjectEditorView::OnMenuSetzoomparam()
{
	CZoomMoveParamDlg dlg;

	dlg.SetLKOZoomFlag(m_bLKOEnableZoom);
	dlg.SetLKOZoom(m_fLKOzoom);
	dlg.SetLKOTime(m_fLKOTime);

	if(IDOK == dlg.DoModal())
	{
		float fZoom = dlg.GetZoomParam();
		float fTime = dlg.GetTimeParam();
		BOOL bZoomFlag = dlg.GetEnableZoomFlag();

		m_DynamicLinkedObject.SetLKOEnableZoomFlag(bZoomFlag==1);
		m_DynamicLinkedObject.SetLKOZoom(fZoom);
		m_DynamicLinkedObject.SetLKOTime(fTime);

		CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
		if (pDoc == NULL)
			return;

		Nuclear::PLinkedObject& plant = pDoc->GetLinkedObjectObject();
		plant.m_bLKOEnableZoom = (bZoomFlag==TRUE);
		plant.m_fLKOTime = fTime;
		plant.m_fLKOzoom = fZoom;

		m_bLKOEnableZoom = (bZoomFlag==1);
		m_fLKOzoom = fZoom;
		m_fLKOTime = fTime;

		pDoc->SetModifiedFlag(true);
	}
}

void CLinkedObjectEditorView::OnTimer(UINT_PTR nIDEvent)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	if(nIDEvent==1)
	{
		Nuclear::TimerOB::OnSourceTimer(); //计算fps
	}
	if(nIDEvent == 2)
	{
		if(m_bPreviewLKO)
			RenderPreviewLKO();
		else
			Render();
	}

	CView::OnTimer(nIDEvent);
}

void CLinkedObjectEditorView::OnUpdate(CView* /*pSender*/, LPARAM /*lHint*/, CObject* /*pHint*/)
{
}

BOOL CLinkedObjectEditorView::DelNode(DWORD curSelFileID)
{
	//主节点不应许删除...
	HashMapLinkedObjectInfo::iterator iter = m_mapLinkedObjectInfo.find(curSelFileID);
	if(iter != m_mapLinkedObjectInfo.end())
	{
		m_mapLinkedObjectInfo.erase(curSelFileID);
		m_mapMoveParam.erase(curSelFileID);

		if(curSelFileID == m_dwMainNodeID)
		{
			OutputDebugString(L"逻辑错误..删除主节点？？CLinkedObjectEditorView::DelNode()\n");

			m_dwMainNodeID = -1;
			m_FrctMainNodPos = Nuclear::FRECT();

			m_ptCentPos = Nuclear::CPOINT();
			for(int i=0; i<4; i++)
				m_ptBasePos[i] = Nuclear::CPOINT();

			for(HashMapLinkedObjectInfo::iterator it = m_mapLinkedObjectInfo.begin(), ie = m_mapLinkedObjectInfo.end(); it != ie; ++it)
			{
				it->second.m_rotateCent_2 = it->second.m_rotateCent_2 -it->second.m_ptOffset;
				it->second.m_ptOffset = Nuclear::CPOINT();
				m_mapMoveParam[it->first].m_ptOffset = Nuclear::CPOINT();
			}
		}
		m_curSelFileID = -1;

		CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
		if (pDoc == NULL)
			return false;

		pDoc->SetModifiedFlag(true);
	}
	return true;
}

BOOL CLinkedObjectEditorView::SetMainNode(DWORD dwFileID)
{
	m_dwMainNodeID = dwFileID;
	int centx_2 = 0;
	int centy_2 = 0;
	HashMapLinkedObjectInfo::iterator it_2 = m_mapLinkedObjectInfo.find(m_dwMainNodeID);
	if(it_2 != m_mapLinkedObjectInfo.end())
	{
		Nuclear::FRECT frct;
		frct.left = 0; //m_FrctMainNodPos.left + it_2->second.m_ptOffset.x; //
		frct.top = 0; //m_FrctMainNodPos.top + it_2->second.m_ptOffset.y; //
		frct.right = frct.left + it_2->second.m_nWidth;
		frct.bottom = frct.top + it_2->second.m_nHeight;

		centx_2 = (int)(frct.left + frct.right)/2;
		centy_2 = (int)(frct.top + frct.bottom)/2;

		m_FrctMainNodPos = frct;

		m_ptCentPos = Nuclear::CPOINT();
		for(int i=0; i<4; i++)
			m_ptBasePos[i] = Nuclear::CPOINT();

		it_2->second.m_ptOffset = Nuclear::CPOINT();
		m_mapMoveParam[it_2->first].m_ptOffset = Nuclear::CPOINT();

		//
		std::wstring fileName = m_mapLinkedObjectInfo[m_dwMainNodeID].m_strPicName;
		std::wstring TempKey = fileName;
		std::transform( TempKey.begin(), TempKey.end(), TempKey.begin(), ::towlower);
		bool bres = PFS::CEnv::PFSFileNameToNativeFileName(TempKey, fileName);

		Nuclear::PImg imgObject;
		FILE *f = NULL;
		_tfopen_s(&f, fileName.c_str(), _T("rb"));
		if( f == NULL )
			return FALSE;

		BOOL ret = imgObject.Load(f) ? TRUE:FALSE;
		fclose(f);
		if(!ret)
			return FALSE;

		//从img文件中读出重心底座...
		m_ptCentPos = imgObject.CenterPoint; //plant的重心,(相对于主节点的左上角..)
		for(int a=0; a<4; a++)
		{
			m_ptBasePos[a] = imgObject.pos[a];//plant的底座,----位置关系同上
			m_ptPos[a] = CPoint(m_ptBasePos[a].x, m_ptBasePos[a].y) +CPoint((int)m_FrctMainNodPos.left, (int)m_FrctMainNodPos.top);
		}
		//
	}
	else
	{
		OutputDebugString(_T("CLinkedObjectEditorView::SetMainNode()--err\n"));
		return false;
	}

	for(HashMapLinkedObjectInfo::iterator it = m_mapLinkedObjectInfo.begin(), ie = m_mapLinkedObjectInfo.end(); it != ie; ++it)
	{
		it->second.m_bMainNode = FALSE;

		POINT pt;
		int left = (int)m_FrctMainNodPos.left + it->second.m_ptOffset.x;
		int top = (int)m_FrctMainNodPos.top + it->second.m_ptOffset.y;

		int centx = left + it->second.m_nWidth/2;
		int centy = top + it->second.m_nHeight/2;

		pt.x = it->second.m_rotateCent.x +centx;
		pt.y = it->second.m_rotateCent.y +centy;
		it->second.m_rotateCent_2.x = pt.x -centx_2;
		it->second.m_rotateCent_2.y = pt.y -centy_2;

		if(it->first == m_dwMainNodeID)
		{
			it->second.m_bMainNode = TRUE;
			it->second.m_LinkedObjectMoveParam.m_nMoveFollowType = Nuclear::XPMOVEFOLLOWNO;
			it->second.m_rotateCent = Nuclear::CPOINT();
			it->second.m_rotateCent_2 = Nuclear::CPOINT();
		}
	}

	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	if (pDoc == NULL)
		return false;

	pDoc->SetModifiedFlag(true);

	//重建dtlist以下
	bool bres = ResetDTList();
	//以上

	return true;
}

void CLinkedObjectEditorView::SetLinkedObjectPosCenterFlag(bool bSetLinkedObjectPosCenter)
{
	m_bSetLinkedObjectPosCenter = bSetLinkedObjectPosCenter;
	SetLinkedObjectStopState( m_bSetLinkedObjectPosCenter || m_bStop );

// 	if(m_bSetLinkedObjectPosCenter)
// 	{//设置重心底座状态....物件静止并恢复到无形变状态..当取消改状态时恢复..
// 		m_dwMainNodeID_last = m_dwMainNodeID;
// 		m_dwMainNodeID = -1;
// 
// 		for(HashMapLinkedObjectInfo::iterator it = m_mapLinkedObjectInfo.begin(), ie = m_mapLinkedObjectInfo.end(); it != ie; ++it)
// 		{
// 			m_mapMoveParam[it->first].m_fAngle = 0.0f;
// 			m_mapMoveParam[it->first].m_fSkewx = 0.0f;
// 			it->second.m_LinkedObjectMoveParam.m_fSpeed = fabs(it->second.m_LinkedObjectMoveParam.m_fSpeed);
// 			it->second.m_LinkedObjectMoveParam.m_nSkewSpeedX = abs(it->second.m_LinkedObjectMoveParam.m_nSkewSpeedX);
// 		}
// 	}
// 	else
// 	{
// 		m_dwMainNodeID = m_dwMainNodeID_last;
// 	}
}

void CLinkedObjectEditorView::SetLinkedObjectPreviewFlag(bool bPreviewFlag)
{
	m_bPreviewLKO = bPreviewFlag;
}

void CLinkedObjectEditorView::SetLinkedObjectDragFlag(bool bDragFlag)
{
	m_bDragFlag = bDragFlag;
}

void CLinkedObjectEditorView::SetLinkedObjectStopFlag(bool bStop)
{
	m_bStop = bStop;
	SetLinkedObjectStopState( m_bSetLinkedObjectPosCenter || m_bStop );
}

void CLinkedObjectEditorView::SetLinkedObjectStopState(bool bFlag)
{
	if(bFlag)
	{
//		m_curSelFileID = -1;
//		m_curMouseFileID = -1;
		Nuclear::DTList& dtlist = m_DynamicLinkedObject.GetLinkedObjectList();

		Nuclear::DTList::iterator iter = dtlist.begin();
		for (; iter!=dtlist.end(); ++iter)
		{
			(*iter)->fAngle = 0.0f;
			(*iter)->fSkewx = 0.0f;
		}
	}


	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	if (pDoc != NULL)
	{
		Nuclear::PLinkedObject& plant = pDoc->GetLinkedObjectObject();
		plant.m_bStatic = bFlag;
	}
}

void CLinkedObjectEditorView::OnLButtonDown(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	if(m_bPreviewLKO)
	{
		m_initialPoint = point;
		return CView::OnLButtonDown(nFlags, point);
	}

	if(m_bSetLinkedObjectPosCenter)
	{
		//设置重心、底座...
		CRect rect;
		GetClientRect(&rect);

		m_bMoveBorder = false;
		m_nMovePointIndex = -1;
		for(int i=0; i<4; i++)
		{
			CPoint borderVertex = m_ptPos[i];
			float dis = Computer2PointDis(point, borderVertex);
			if(dis<3)
			{
				m_nMovePointIndex = i;
				m_bMoveBorder = true;
			}
		}

		if( rect.PtInRect(point) )
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
			}
		}
	}
	else
	{
		Nuclear::FPOINT fpt;
		fpt.x = (float)point.x;
		fpt.y = (float)point.y;

		float min_dis = 100000.0f;
		bool bFind = false;
		m_curSelFileID = -1;
		for(HashMapLinkedObjectInfo::iterator it = m_mapLinkedObjectInfo.begin(), ie = m_mapLinkedObjectInfo.end(); it != ie; ++it)
		{
			Nuclear::FRECT frct;
			frct.left = m_FrctMainNodPos.left + m_mapMoveParam[it->first].m_ptOffset.x;//it->second.m_ptOffset.x;
			frct.top = m_FrctMainNodPos.top + m_mapMoveParam[it->first].m_ptOffset.y;//it->second.m_ptOffset.y;
			frct.right = frct.left + it->second.m_nWidth;
			frct.bottom = frct.top + it->second.m_nHeight;

		//	if( frct.PtInRect(fpt) ) //判断点不一定在框内
			{
				CPoint pt;
				pt.x = (int)(frct.left + frct.right)/2;
				pt.y = (int)(frct.top + frct.bottom)/2;

				pt.x = it->second.m_rotateCent.x  +pt.x;
				pt.y = it->second.m_rotateCent.y  +pt.y;

				float dis = Computer2PointDis(point, pt); //以错切中心点为判断选中的依据..
				if(dis<min_dis)
				{
					min_dis = dis;
					bFind = true;
					m_curSelFileID = it->first;
				}
			}
		}
		if(bFind)
		{
			m_curMouseFileID = m_curSelFileID;
			m_initialPoint = point;

			//列表框选中项...
			CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
			if (pDoc == NULL)
				return;

			LVFINDINFO info;
			info.flags = LVFI_PARTIAL|LVFI_STRING;
			CString str;
			str.Format(L"%d", m_curSelFileID);
			info.psz = str;
			int nIndex =  pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.FindItem(&info);

			// 使得nIndex位置处项目高亮显示并获得焦点
			if(nIndex != -1)
			{
//				pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.SetFocus(); //这样会使得键盘消息无效..
				UINT flag = LVIS_SELECTED|LVIS_FOCUSED;
				pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.SetItemState(nIndex, flag, flag);
			}
		}
	}

	CView::OnLButtonDown(nFlags, point);
}

void CLinkedObjectEditorView::OnMouseMove(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	if(nFlags != MK_LBUTTON)
	{
		if(m_bSetLinkedObjectPosCenter)
		{
			//设置重心、底座...
			for(int i=0; i<4; i++)
			{
				CPoint borderVertex = m_ptPos[i];
				float dis = Computer2PointDis(point, borderVertex);
				if(dis<3)
				{
					::SetCursor(AfxGetApp()->LoadStandardCursor(IDC_SIZEALL));
					break;
				}
			}
		}
		else
		{
			Nuclear::FPOINT fpt;
			fpt.x = (float)point.x;
			fpt.y = (float)point.y;

			float min_dis = 100000.0f;
			bool bFind = false;
			m_curMouseFileID = -1;
			for(HashMapLinkedObjectInfo::iterator it = m_mapLinkedObjectInfo.begin(), ie = m_mapLinkedObjectInfo.end(); it != ie; ++it)
			{
				Nuclear::FRECT frct;
				frct.left = m_FrctMainNodPos.left + m_mapMoveParam[it->first].m_ptOffset.x;//it->second.m_ptOffset.x;
				frct.top = m_FrctMainNodPos.top + m_mapMoveParam[it->first].m_ptOffset.y;//it->second.m_ptOffset.y;
				frct.right = frct.left + it->second.m_nWidth;
				frct.bottom = frct.top + it->second.m_nHeight;

				if( frct.PtInRect(fpt) )
				{
					CPoint pt;
					pt.x = (int)(frct.left + frct.right)/2;
					pt.y = (int)(frct.top + frct.bottom)/2;
					float dis = Computer2PointDis(point, pt);
					if(dis<min_dis)
					{
						min_dis = dis;
						bFind = true;
						m_curMouseFileID = it->first;
					}
				}
			}
			if(bFind)
				Invalidate(false);
		}
	}
	else
	{ //nFlags==MK_LBUTTON
		if(m_bSetLinkedObjectPosCenter)
		{
			CDC *pDC = GetDC();
			//设置重心、底座...
			if(!m_bMoveBorder)
			{//不应许设置..只应许修改...
				/*
				float dis = Computer2PointDis(m_initialPoint, point);
				if(dis>3.0f)
				{
					m_bDrawBorder = true;
					CPoint pt0 = m_initialPoint;
					CPoint pt1 = point;

					ComputerPltFilePos(pt0, pt1);

					InvertLine(pDC, m_ptFrom[0], m_ptTo[0]);//
					InvertLine(pDC, m_ptFrom[0], m_ptPos[1]);

					InvertLine(pDC, m_ptFrom[0], m_ptTo[1]);
					InvertLine(pDC, m_ptFrom[0], m_ptPos[3]);

					InvertLine(pDC, m_ptFrom[1], m_ptTo[0]);
					InvertLine(pDC, point, m_ptPos[1]);

					InvertLine(pDC, m_ptFrom[1], m_ptTo[1]);
					InvertLine(pDC, point, m_ptPos[3]);

					m_ptTo[0] = m_ptPos[1];
					m_ptTo[1] = m_ptPos[3];
					m_ptFrom[1] = point;
				}
				*/
			}
			else
			{
				int ia = m_nMovePointIndex;
				int ib = (ia+1)>3 ? 0:(ia+1);
				int ic = (ia+2)>3 ? (ia-2):(ia+2);
				int id = (ia-1)<0 ? 3:(ia-1);

				CPoint pt0 = m_ptPos[ic];
				CPoint pre_Pos[4];
				for(int i=0; i<4; i++)
					pre_Pos[i] = m_ptPos[i];

				ComputerPltFilePos(pt0, point); //原始数据计算--计算之后m_ptPos[]的顺序打乱了...
				//恢复顺序..
				CPoint temp[4];
				for(int i=0; i<4; i++)
				{
					temp[i] = m_ptPos[i];
				}
				m_ptPos[ic] = temp[0];
				m_ptPos[ia] = temp[2];
				m_ptPos[ib] = temp[1];
				m_ptPos[id] = temp[3];
				//
			//	SetPltFilePos(); //鼠标释放之后再设置..

				CPoint pt1 = pre_Pos[ib];
				InvertLine(pDC, pt0, pt1);

				pt1 = pre_Pos[id];
				InvertLine(pDC, pt0, pt1);

				pt1 = pre_Pos[ia];
				CPoint pt2 = pre_Pos[ib];
				InvertLine(pDC, pt1, pt2);

				pt1 = pre_Pos[ia];
				pt2 = pre_Pos[id];
				InvertLine(pDC, pt1, pt2);

				pt0 = m_ptPos[ic];
				pt1 = m_ptPos[ib];
				InvertLine(pDC, pt0, pt1);

				pt1 = m_ptPos[id];
				InvertLine(pDC, pt0, pt1);

				pt1 = m_ptPos[ia];
				pt2 = m_ptPos[ib];
				InvertLine(pDC, pt1, pt2);

				pt1 = m_ptPos[ia];
				pt2 = m_ptPos[id];
				InvertLine(pDC, pt1, pt2);;	
			}
			ReleaseDC(pDC);	
		}
		else
		{
			int nFileId = -1;
			if(m_bDragFlag)
				nFileId = m_curSelFileID;
			else //允许拖动主节点
				nFileId = m_dwMainNodeID;

			int dx = point.x - m_initialPoint.x;
			int dy = point.y - m_initialPoint.y;
			m_initialPoint = point;
			if(m_bPreviewLKO)
			{
				MovePreviewLKO(dx, dy);
			}
			else
			{
				if(nFileId != -1)
				{
					MoveSelectedElements(nFileId, dx, dy);

					MOVENODEPARAM param;
					param.dwFileID = nFileId;
					param.dx = dx;
					param.dy = dy;
					m_VectorUndoOperations.push_back(param);
				}
			}
		}
	}
	CView::OnMouseMove(nFlags, point);
}

void CLinkedObjectEditorView::OnLButtonUp(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	//设置重心、底座...
//	if( GetCapture()==this )
//	if(m_curSelFileID != -1) //这样不能设置重心底座了
	{
		if(m_bSetLinkedObjectPosCenter)
		{
			if(!m_bMoveBorder)
			{
				if(!m_bDrawBorder)
				{
					CPoint pt0((int)m_FrctMainNodPos.left, (int)m_FrctMainNodPos.top);
					CPoint pt = point - pt0;
					m_ptCentPos = Nuclear::CPOINT(pt.x, pt.y);
				}
				else
				{
					CPoint pt0 = m_initialPoint;
					CPoint pt1 = point;
					ComputerPltFilePos(pt0, pt1);
					SetPltFilePos();
				}
			}
			else
			{
				SetPltFilePos();
			}

			m_bDrawBorder = false;
			m_bMoveBorder = false;
			ReleaseCapture();
			Invalidate(false);
		}
		else
		{//都在mousemove中处理好了..

		}
	}

	CView::OnLButtonUp(nFlags, point);
}

void CLinkedObjectEditorView::InvertLine(CDC* pDC, CPoint ptFrom, CPoint ptTo)
{
	int nOldMode = pDC->SetROP2(R2_NOT);
	pDC->MoveTo (ptFrom);
	pDC->LineTo (ptTo);
	pDC->SetROP2 (nOldMode);
}

void CLinkedObjectEditorView::ComputerPltFilePos(const CPoint& initialPoint, const CPoint& point)
{
	//俯视32度
	m_ptPos[0] = initialPoint;
	m_ptPos[2] = point;

	float dx = (float)( (point.x - initialPoint.x) );
	float dy = (float)( (point.y - initialPoint.y)/Nuclear::XPCOS(58) ); //cos(58/57.3f)
	float a = (dx+dy)/2.0f;
	float b = (dy-dx)/2.0f;
	m_ptPos[3].x = (int)( a +initialPoint.x );
	m_ptPos[3].y = (int)( a*Nuclear::XPCOS(58) +initialPoint.y ); //cos(58/57.3f)
	m_ptPos[1].x = (int)( -b +initialPoint.x );
	m_ptPos[1].y = (int)( b*Nuclear::XPCOS(58) +initialPoint.y ); //cos(58/57.3f)
}

void CLinkedObjectEditorView::SetPltFilePos()
{
	int IndexMAX_x = 0;
	int IndexMAX_y = 0;
	int IndexMIN_x = 0;
	int IndexMIN_y = 0;

	int i=0;

	int min_x = 1000000;
	int max_x = -100;
	int min_y = 1000000;
	int max_y = -100;

	for(i=0; i<4; i++)
	{
		if(m_ptPos[i].x > max_x)
		{
			IndexMAX_x = i;
			max_x = m_ptPos[i].x;
		}
		if(m_ptPos[i].x < min_x)
		{
			IndexMIN_x = i;
			min_x = m_ptPos[i].x;
		}

		if(m_ptPos[i].y > max_y)
		{
			IndexMAX_y = i;
			max_y = m_ptPos[i].y;
		}
		if(m_ptPos[i].y < min_y)
		{
			IndexMIN_y = i;
			min_y = m_ptPos[i].y;
		}
	}

	CPoint pt0((int)m_FrctMainNodPos.left, (int)m_FrctMainNodPos.top);
	CPoint pt = m_ptPos[IndexMIN_x] - pt0;
	m_ptBasePos[Nuclear::enum_left] = Nuclear::CPOINT(pt.x, pt.y);
	pt = m_ptPos[IndexMAX_x] - pt0;
	m_ptBasePos[Nuclear::enum_right] = Nuclear::CPOINT(pt.x, pt.y);
	pt = m_ptPos[IndexMAX_y] - pt0;
	m_ptBasePos[Nuclear::enum_bottom] = Nuclear::CPOINT(pt.x, pt.y);
	pt = m_ptPos[IndexMIN_y] - pt0;
	m_ptBasePos[Nuclear::enum_up] = Nuclear::CPOINT(pt.x, pt.y);


	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	if (pDoc == NULL)
		return;

	pDoc->SetModifiedFlag(true);
}

void CLinkedObjectEditorView::SetLinkedObjectData(const Nuclear::PLinkedObject& plantObj)
{
	m_bDragFlag = false; //新打开的lko文件默认不允许拖动

	m_ptCentPos = plantObj.m_ptCentPos;
	for(int i=0; i<4; i++)
	{
		m_ptBasePos[i] = plantObj.m_ptBasePos[i];
		m_ptPos[i] = m_ptBasePos[i];
	}

	m_bLKOEnableZoom = plantObj.m_bLKOEnableZoom; //是否允许被干扰
	m_fLKOzoom = plantObj.m_fLKOzoom;//干扰最激烈程度
	m_fLKOTime = plantObj.m_fLKOTime;//恢复所需时间

	int cnt = (int)plantObj.m_vectorLinkedObjectInfo.size();
	for(int i=0; i<cnt; i++)
	{
		Nuclear::PLANTPICINFO LinkedObjectPicInfo = plantObj.m_vectorLinkedObjectInfo.at(i);
		LinkedObjectPicInfo.m_LinkedObjectMoveParam.m_fSpeed = fabs( LinkedObjectPicInfo.m_LinkedObjectMoveParam.m_fSpeed );
		LinkedObjectPicInfo.m_LinkedObjectMoveParam.m_nSkewSpeedX = abs( LinkedObjectPicInfo.m_LinkedObjectMoveParam.m_nSkewSpeedX );

		m_mapLinkedObjectInfo.insert( std::make_pair(i, LinkedObjectPicInfo) );
	}

	m_dwPicIDMax = (DWORD)m_mapLinkedObjectInfo.size()-1;

	m_dwMainNodeID = plantObj.GetMainNodeID();
	m_FrctMainNodPos.left = 0;
	m_FrctMainNodPos.top = 0;
	m_FrctMainNodPos.right = (float)m_mapLinkedObjectInfo[m_dwMainNodeID].m_nWidth;
	m_FrctMainNodPos.bottom = (float)m_mapLinkedObjectInfo[m_dwMainNodeID].m_nHeight;

	m_bLoadPic = false;
}

void CLinkedObjectEditorView::InitData()
{
	if(m_bPreviewLKO)
	{
		MessageBox(L"目前处于lko文件预览状态\n");
	}

	m_mapPicHandle.clear();
	m_mapMoveParam.clear();

	TCHAR workdir[MAX_PATH];
	GetModuleFileName(NULL, workdir, MAX_PATH);
	std::wstring strWorkDir = workdir;
	int npos = (int)strWorkDir.rfind('\\');
	strWorkDir = std::wstring(strWorkDir, 0, npos);
	std::wstring sysFilePath = _T("\\res\\map"); // \\elements\\

	std::wstring fileName = L"";
	int PicID = 0;

	int cntLinkedObjectElements = (int)m_mapLinkedObjectInfo.size();
	for(PicID=0; PicID<cntLinkedObjectElements; PicID++)
	{
		Nuclear::PLANTPICINFO plantPicInfo;
		plantPicInfo = m_mapLinkedObjectInfo[PicID];
		bool bMainNode = plantPicInfo.m_bMainNode;
		if(bMainNode)
			m_dwMainNodeID = PicID;

		fileName = plantPicInfo.m_strPicName;
		std::wstring strKey = fileName;

		std::wstring TempKey = fileName;
		std::transform( TempKey.begin(), TempKey.end(), TempKey.begin(), ::towlower);
		bool bres = PFS::CEnv::PFSFileNameToNativeFileName(TempKey, fileName);

//		std::wstring tempStr = strWorkDir + sysFilePath + fileName;
//		CString strFilePath = tempStr.c_str();
		CString strFilePath = fileName.c_str();
		//读img文件初始化一个img对象..下面使用其相关信息来load纹理和宽高等...
		Nuclear::PImg imgObject;
		FILE *f = NULL;
		_tfopen_s(&f, strFilePath, _T("rb"));
		if( f == NULL )
		{
			OutputDebugString(L"InitData()---_tfopen_s()--error\n");
			continue;
		}

		BOOL ret = imgObject.Load(f) ? TRUE:FALSE;
		fclose(f);
		if(!ret)
		{
			OutputDebugString(L"InitData()---_tfopen_s()--error\n");
			continue;
		}

		int nPicWidth = imgObject.m_nFileWidth;
		int nPicHeight = imgObject.m_nFileHeight;

		std::map<std::wstring, PicHandleInfoVetor>::const_iterator iter = m_mapPicHandle.find(fileName);
		if(iter == m_mapPicHandle.end())
		{
			//读img文件初始化handleInfoVector--

			//根据文件名来加载显示...
			PicHandleInfoVetor handleInfoVector;
			handleInfoVector.clear();
			int nPos = strFilePath.ReverseFind('.');
			CString filePathNoEX = strFilePath.Left(nPos);

			int cnt = (int)imgObject.m_vectorRct.size();
			for(int i=0; i<cnt; i++)
			{
				CString str;
				str.Format(_T("_%d"), i);
				CString fileNameNoEx = filePathNoEX + str;
				Nuclear::PictureHandle handle = m_pRender->LoadPictureFromNativePath(std::wstring(fileNameNoEx));

				IMGPICHANDLEINFO picInfo;
				picInfo.m_handlePic = handle;
				picInfo.m_rct = imgObject.m_vectorRct.at(i);
				handleInfoVector.push_back(picInfo);
			}

			//
			m_mapPicHandle.insert( std::make_pair(strKey, handleInfoVector) );
		}

		IMGPICMOVEPARAM moveParam;
		moveParam.m_ptOffset = plantPicInfo.m_ptOffset;
		m_mapMoveParam.insert( std::make_pair(PicID, moveParam) );

		CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
		ASSERT_VALID(pDoc);
		if (!pDoc)
			return;
		pDoc->GetLinkedObjectStatusView()->m_strFileNameList.push_back(plantPicInfo.m_strPicName.c_str());
		pDoc->GetLinkedObjectStatusView()->Invalidate(true);

		CString str;
		str.Format(L"%d", PicID);
		pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.InsertItem(PicID, str);
		pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.SetItemText(PicID, 1, plantPicInfo.m_strPicName.c_str());
		str.Format(_T("%d"), plantPicInfo.m_bMainNode);
		pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.SetItemText(PicID, 2, str);
		str.Format(_T("%d"), plantPicInfo.m_LinkedObjectMoveParam.m_nMoveFollowType);
		pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.SetItemText(PicID, 3, str);

		str.Format(_T("%d"), plantPicInfo.m_LinkedObjectMoveParam.m_dwFlag);
		pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.SetItemText(PicID, 4, str);

		Nuclear::XPTEXTURE_FORMAT nfmt = (Nuclear::XPTEXTURE_FORMAT)(imgObject.m_texfmt);
		str = m_fmtMap[nfmt].c_str();
		pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.SetItemText(PicID, 5, str);

	}
}

Nuclear::CRECT CLinkedObjectEditorView::ComputeLinkedObjectMaxBox(const Nuclear::PLinkedObject& plantObject)
{
	Nuclear::CRECT rctMaxBox; //相对于植物重心的坐标...
	int min_x = 1000;
	int min_y = 1000;
	int max_x = -1000;
	int max_y = -1000;

	Nuclear::PLinkedObject::VectorLinkedObjectInfo vectorLinkedObjectInfo = plantObject.m_vectorLinkedObjectInfo;
	for(Nuclear::PLinkedObject::VectorLinkedObjectInfo::const_iterator it = vectorLinkedObjectInfo.begin(), ie = vectorLinkedObjectInfo.end(); it != ie; ++it)
	{
		Nuclear::PLANTPICINFO plantInfo = *it;

		if( plantInfo.m_ptOffset.x < min_x )
			min_x = plantInfo.m_ptOffset.x;
		if( plantInfo.m_ptOffset.y < min_y )
			min_y = plantInfo.m_ptOffset.y;

		if( plantInfo.m_ptOffset.x + plantInfo.m_nWidth > max_x )
			max_x = plantInfo.m_ptOffset.x + plantInfo.m_nWidth;
		if( plantInfo.m_ptOffset.y + plantInfo.m_nHeight > max_y )
			max_y = plantInfo.m_ptOffset.y + plantInfo.m_nHeight;
	}

	rctMaxBox = Nuclear::CRECT(min_x, min_y, max_x, max_y);
	rctMaxBox -= plantObject.m_ptCentPos;

	return rctMaxBox;
}

Nuclear::CRECT CLinkedObjectEditorView::ComputeLinkedObjectMaxBox()
{
	Nuclear::CRECT rctMaxBox; //相对于植物重心的坐标...
	int min_x = 1000;
	int min_y = 1000;
	int max_x = -1000;
	int max_y = -1000;

	for(HashMapLinkedObjectInfo::const_iterator it = m_mapLinkedObjectInfo.begin(), ie = m_mapLinkedObjectInfo.end(); it != ie; ++it)
	{
		Nuclear::PLANTPICINFO plantInfo = it->second;

		if( plantInfo.m_ptOffset.x < min_x )
			min_x = plantInfo.m_ptOffset.x;
		if( plantInfo.m_ptOffset.y < min_y )
			min_y = plantInfo.m_ptOffset.y;

		if( plantInfo.m_ptOffset.x + plantInfo.m_nWidth > max_x )
			max_x = plantInfo.m_ptOffset.x + plantInfo.m_nWidth;
		if( plantInfo.m_ptOffset.y + plantInfo.m_nHeight > max_y )
			max_y = plantInfo.m_ptOffset.y + plantInfo.m_nHeight;
	}

	rctMaxBox = Nuclear::CRECT(min_x, min_y, max_x, max_y);
	rctMaxBox -= m_ptCentPos;

	return rctMaxBox;
}

void CLinkedObjectEditorView::RefreshLinkedObject()
{
	//释放旧的纹理资源
	//free纹理..
	for(std::map<std::wstring, PicHandleInfoVetor>::const_iterator it = m_mapPicHandle.begin(), ie = m_mapPicHandle.end(); it != ie; ++it)
	{
		PicHandleInfoVetor handleInfoVector = it->second;
		int cnt = (int)handleInfoVector.size();
		for(int i=0; i<cnt; i++)
		{
			m_pRender->FreePicture(handleInfoVector.at(i).m_handlePic);
		}
		handleInfoVector.clear();
	}
	m_mapPicHandle.clear();
	//

	for(HashMapLinkedObjectInfo::iterator it = m_mapLinkedObjectInfo.begin(), ie = m_mapLinkedObjectInfo.end(); it != ie; ++it)
	{
		Nuclear::PLANTPICINFO& plantInfo = it->second;
		std::wstring strPicName = plantInfo.m_strPicName;
		std::wstring strKey = strPicName;
		std::wstring TempKey = strPicName;
		std::transform( TempKey.begin(), TempKey.end(), TempKey.begin(), ::towlower);
		bool bres = PFS::CEnv::PFSFileNameToNativeFileName(TempKey, strPicName);

		CString strFilePath = strPicName.c_str();
		//读img文件初始化一个img对象..下面使用其相关信息来load纹理和宽高等...
		Nuclear::PImg imgObject;
		FILE *f = NULL;
		_tfopen_s(&f, strFilePath, _T("rb"));
		if( f == NULL )
		{
			OutputDebugString(L"RefreshLinkedObject()---_tfopen_s()--error\n");
			continue;
		}

		BOOL ret = imgObject.Load(f) ? TRUE:FALSE;
		fclose(f);
		if(!ret)
		{
			OutputDebugString(L"RefreshLinkedObject()---_tfopen_s()--error\n");
			continue;
		}

		int nPicWidth = imgObject.m_nFileWidth;
		int nPicHeight = imgObject.m_nFileHeight;

		plantInfo.m_nWidth = nPicWidth;
		plantInfo.m_nHeight = nPicHeight;

		std::map<std::wstring, PicHandleInfoVetor>::const_iterator iter = m_mapPicHandle.find(strKey);
		if(iter == m_mapPicHandle.end())
		{
			//读img文件初始化handleInfoVector--

			//根据文件名来加载显示...
			PicHandleInfoVetor handleInfoVector;
			handleInfoVector.clear();
			int nPos = strFilePath.ReverseFind('.');
			CString filePathNoEX = strFilePath.Left(nPos);

			int cnt = (int)imgObject.m_vectorRct.size();
			for(int i=0; i<cnt; i++)
			{
				CString str;
				str.Format(_T("_%d"), i);
				CString fileNameNoEx = filePathNoEX + str;
				Nuclear::PictureHandle handle = m_pRender->LoadPictureFromNativePath(std::wstring(fileNameNoEx));

				IMGPICHANDLEINFO picInfo;
				picInfo.m_handlePic = handle;
				picInfo.m_rct = imgObject.m_vectorRct.at(i);
				handleInfoVector.push_back(picInfo);
			}
			//
			m_mapPicHandle.insert( std::make_pair(strKey, handleInfoVector) );
		}
	}
}

bool CLinkedObjectEditorView::CreateDTList(const Nuclear::PLinkedObject& plantObject)
{
	//打开文件时候生成list结构 //m_DynamicLinkedObject 重设主节点时重建该结构
	m_VectorUndoOperations.clear(); //清空操作记录
	m_VectorRedoOperations.clear(); //清空操作记录

	m_DynamicLinkedObject.RemoveAllNode();

	m_DynamicLinkedObject.SetLKOEnableZoomFlag(plantObject.m_bLKOEnableZoom);
	m_DynamicLinkedObject.SetLKOZoom(plantObject.m_fLKOzoom);
	m_DynamicLinkedObject.SetLKOTime(plantObject.m_fLKOTime);

	Nuclear::CPOINT pos = plantObject.m_ptCentPos + Nuclear::FPOINT(m_FrctMainNodPos.left, m_FrctMainNodPos.top);

	Nuclear::LkoTreeNode* tmpNode, *nodeRoot;
	DWORD rootId = plantObject.GetMainNodeID();
	Nuclear::PLinkedObject::VectorLinkedObjectInfo::const_iterator nodeIter = plantObject.m_vectorLinkedObjectInfo.begin() + rootId;
	if (nodeIter == plantObject.m_vectorLinkedObjectInfo.end())
		return false;
	tmpNode = new Nuclear::LkoTreeNode();
	tmpNode->pParent = NULL;
	tmpNode->pRoot = tmpNode;
	nodeRoot = tmpNode;
	tmpNode->LinkedObjectMoveParam.m_nMoveFollowType = nodeIter->m_LinkedObjectMoveParam.m_nMoveFollowType;
	tmpNode->height = nodeIter->m_nHeight;
	tmpNode->width = nodeIter->m_nWidth;
	tmpNode->rotateCent = nodeIter->m_rotateCent;
	Nuclear::CPOINT rootRotateCent = nodeIter->m_rotateCent;
	tmpNode->rotateCent.x += nodeIter->m_nWidth / 2;
	tmpNode->rotateCent.y += nodeIter->m_nHeight / 2;
	tmpNode->fromMainRotateCent = (pos - plantObject.m_ptCentPos + tmpNode->rotateCent).ToFPOINT();
	tmpNode->LinkedObjectMoveParam = nodeIter->m_LinkedObjectMoveParam;
	tmpNode->fAngle = tmpNode->fSkewx = 0.0f;
	tmpNode->fTranslationx = tmpNode->fTranslationy = 0.0f;
	m_DynamicLinkedObject.AddANode(tmpNode);

	int i = 0;
	for(nodeIter = plantObject.m_vectorLinkedObjectInfo.begin(); nodeIter != plantObject.m_vectorLinkedObjectInfo.end();++nodeIter,++i)
	{
		if (i == rootId)
			continue;
		tmpNode = new Nuclear::LkoTreeNode();
		tmpNode->pRoot = nodeRoot;
		tmpNode->pParent = nodeRoot;
		tmpNode->LinkedObjectMoveParam.m_nMoveFollowType = nodeIter->m_LinkedObjectMoveParam.m_nMoveFollowType;
		tmpNode->height = nodeIter->m_nHeight;
		tmpNode->width = nodeIter->m_nWidth;
		tmpNode->fromMainRotateCent = (nodeIter->m_rotateCent_2 - rootRotateCent).ToFPOINT();
		tmpNode->rotateCent = nodeIter->m_rotateCent;
		tmpNode->rotateCent.x += nodeIter->m_nWidth / 2;
		tmpNode->rotateCent.y += nodeIter->m_nHeight / 2;
		tmpNode->LinkedObjectMoveParam = nodeIter->m_LinkedObjectMoveParam;
		tmpNode->fAngle = tmpNode->fSkewx = 0.0f;
		tmpNode->fTranslationx = tmpNode->fTranslationy = 0.0f;
		m_DynamicLinkedObject.AddANode(tmpNode);
	}
	return true;
}

bool CLinkedObjectEditorView::ResetDTList()
{
	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	if (pDoc == NULL)
		return false;

	m_VectorRedoOperations.clear();
	m_VectorUndoOperations.clear();

	m_DynamicLinkedObject.RemoveAllNode();

	Nuclear::CPOINT pos = m_ptCentPos + Nuclear::FPOINT(m_FrctMainNodPos.left, m_FrctMainNodPos.top);

	Nuclear::LkoTreeNode* tmpNode, *nodeRoot;

	HashMapLinkedObjectInfo::const_iterator itRoot = m_mapLinkedObjectInfo.find(m_dwMainNodeID);
	if(itRoot == m_mapLinkedObjectInfo.end())
	{
		OutputDebugString(L"逻辑错误....CLinkedObjectEditorView::ResetDTList()\n");
		return false;
	}

	tmpNode = new Nuclear::LkoTreeNode();
	tmpNode->pParent = NULL;
	tmpNode->pRoot = tmpNode;
	nodeRoot = tmpNode;
	tmpNode->LinkedObjectMoveParam.m_nMoveFollowType = itRoot->second.m_LinkedObjectMoveParam.m_nMoveFollowType;
	tmpNode->height = itRoot->second.m_nHeight;
	tmpNode->width = itRoot->second.m_nWidth;
	tmpNode->rotateCent = itRoot->second.m_rotateCent;
	Nuclear::CPOINT rootRotateCent = itRoot->second.m_rotateCent;
	tmpNode->rotateCent.x += itRoot->second.m_nWidth / 2;
	tmpNode->rotateCent.y += itRoot->second.m_nHeight / 2;
	tmpNode->fromMainRotateCent = (pos - m_ptCentPos + tmpNode->rotateCent).ToFPOINT();
	tmpNode->LinkedObjectMoveParam = itRoot->second.m_LinkedObjectMoveParam;
	tmpNode->fAngle = tmpNode->fSkewx = 0.0f;

	tmpNode->fTranslationx = tmpNode->fTranslationy = 0.0f;

	m_DynamicLinkedObject.AddANode(tmpNode);

	int cnt = pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.GetItemCount();
	for(int i=0; i<cnt; i++)
	{
		DWORD fileID = _wtoi( pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.GetItemText(i, 0) );
		if(fileID == m_dwMainNodeID)
			continue; //主节点已经在上面处理了..

		HashMapLinkedObjectInfo::const_iterator it = m_mapLinkedObjectInfo.find(fileID);
		if(it == m_mapLinkedObjectInfo.end())
		{
			OutputDebugString(L"逻辑错误....CLinkedObjectEditorView::ResetDTList()\n");
			return false;
		}

		tmpNode = new Nuclear::LkoTreeNode();
		tmpNode->pRoot = nodeRoot;
		tmpNode->pParent = nodeRoot;
		tmpNode->LinkedObjectMoveParam.m_nMoveFollowType = it->second.m_LinkedObjectMoveParam.m_nMoveFollowType;
		tmpNode->height = it->second.m_nHeight;
		tmpNode->width = it->second.m_nWidth;
		tmpNode->fromMainRotateCent = (it->second.m_rotateCent_2 - rootRotateCent).ToFPOINT();
		tmpNode->rotateCent = it->second.m_rotateCent;
		tmpNode->rotateCent.x += it->second.m_nWidth / 2;
		tmpNode->rotateCent.y += it->second.m_nHeight / 2;
		tmpNode->LinkedObjectMoveParam = it->second.m_LinkedObjectMoveParam;
		tmpNode->fAngle = tmpNode->fSkewx = 0.0f;
		tmpNode->fTranslationx = tmpNode->fTranslationy = 0.0f;
		m_DynamicLinkedObject.AddANode(tmpNode);
	}

	return true;
}


void CLinkedObjectEditorView::OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	int dx = 0;
	int dy = 0;
	switch (nChar)
	{
	case VK_DOWN:
		dy = 1;
		break;
	case VK_UP:
		dy = -1;
		break;
	case VK_LEFT:
		dx = -1;
		break;
	case VK_RIGHT:
		dx = 1;
		break;

	case 'P':
		m_DynamicLinkedObject.SetZoomParamFlag(true);
		break;
	case 'O':
		m_DynamicLinkedObject.SetZoomParamFlag(false);
		break;
	}

	if(m_bPreviewLKO)
	{
		MovePreviewLKO(5*dx, 5*dy);
	}
	else
	{
		if(m_curSelFileID != -1) //	&& m_bDragFlag 方向键任意时候可用
		{
			MoveSelectedElements(m_curSelFileID, dx, dy);

			MOVENODEPARAM param;
			param.dwFileID = m_curSelFileID;
			param.dx = dx;
			param.dy = dy;
			m_VectorUndoOperations.push_back(param);
		}
	}

	CView::OnKeyDown(nChar, nRepCnt, nFlags);
}

bool CLinkedObjectEditorView::MovePreviewLKO(int dx, int dy)
{ //移动主节点
	Nuclear::DTList &dtlist = m_DynamicLinkedObjectPreview.GetLinkedObjectList();
	if(dtlist.empty())
		return false;

	dtlist[0]->fromMainRotateCent += Nuclear::CPOINT(dx, dy).ToFPOINT();

	return true;
}

bool CLinkedObjectEditorView::MoveSelectedElements(DWORD dwFileID, int dx, int dy)
{
	::SetCursor(AfxGetApp()->LoadStandardCursor(IDC_SIZEALL));

	HashMapLinkedObjectInfo::iterator it = m_mapLinkedObjectInfo.find(dwFileID);
	if(it != m_mapLinkedObjectInfo.end())
	{
		int index = GetDTListIndex(dwFileID);
		//从父节点旋转中心到自己的旋转中心的向量
		//如果自己是根节点，那么这个就是在地图上的坐标（世界坐标）
		if(index != -1)
		{
			Nuclear::DTList &dtlist = m_DynamicLinkedObject.GetLinkedObjectList();
			dtlist[index]->fromMainRotateCent += Nuclear::CPOINT(dx, dy).ToFPOINT();
		}
		else
		{
			OutputDebugString(L"error-MoveSelectedElements- GetDTListIndex(dwFileID) == -1 -\n");
		}

		Nuclear::PLANTPICINFO plantPicInfo = it->second;
		if(dwFileID != m_dwMainNodeID)
		{
			plantPicInfo.m_ptOffset.x += dx;
			plantPicInfo.m_ptOffset.y += dy;
			plantPicInfo.m_rotateCent_2.x += dx;
			plantPicInfo.m_rotateCent_2.y += dy;
		}
		else
		{
			//拖动的是主节点..
			m_FrctMainNodPos += Nuclear::FPOINT(float(dx), float(dy));

			for(int i=0; i<4; i++)
			{
				m_ptPos[i].x += dx;
				m_ptPos[i].y += dy;
			}
		}

		it->second = plantPicInfo;
		m_mapMoveParam[it->first].m_ptOffset = plantPicInfo.m_ptOffset;
		Invalidate(false);
	}
	else
	{
		MessageBox(_T("要恢复的操作对象已经被删除?..CLinkedObjectEditorView::MoveSelectedElements()"));
		m_VectorRedoOperations.clear();
		m_VectorUndoOperations.clear();
	//	MessageBox(_T("怎么会没找到?..CLinkedObjectEditorView::MoveSelectedElements()"));
	}

	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	pDoc->SetModifiedFlag(true);

	return true;
}

void CLinkedObjectEditorView::OnMenuLightobject()
{
	if(m_curSelFileID == -1)
	{
		MessageBox(L"OnMenuMovefollow()---没有选中的元素?\n");
		return;
	}
	DWORD dwflag = m_mapLinkedObjectInfo[m_curSelFileID].m_LinkedObjectMoveParam.m_dwFlag;
	if(dwflag & Nuclear::XP_LINKEDOBJ_LIGHT)
		m_mapLinkedObjectInfo[m_curSelFileID].m_LinkedObjectMoveParam.m_dwFlag &= ~Nuclear::XP_LINKEDOBJ_LIGHT;
	else
		m_mapLinkedObjectInfo[m_curSelFileID].m_LinkedObjectMoveParam.m_dwFlag |= Nuclear::XP_LINKEDOBJ_LIGHT;
	
	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	if (pDoc == NULL)
		return;

	CString str;
	LVFINDINFO info;
	info.flags = LVFI_PARTIAL|LVFI_STRING;
	str.Format(L"%d", m_curSelFileID);
	info.psz = str;
	int nIndex =  pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.FindItem(&info);

	// 使得nIndex位置处项目高亮显示并获得焦点
	if(nIndex != -1)
	{
		pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.SetFocus();
		UINT flag = LVIS_SELECTED|LVIS_FOCUSED;
		pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.SetItemState(nIndex, flag, flag);
	}

	str.Format(L"%d", m_mapLinkedObjectInfo[m_curSelFileID].m_LinkedObjectMoveParam.m_dwFlag);
	pDoc->GetLinkedObjectInfoView()->m_listCtrlLinkedObjectLevel.SetItemText(nIndex, 4, str);

	pDoc->SetModifiedFlag(true);
}

void CLinkedObjectEditorView::OnEditUndo()
{
	MOVENODEPARAM param = m_VectorUndoOperations.back();
	m_VectorUndoOperations.pop_back();
	MoveSelectedElements(param.dwFileID, -(param.dx), -(param.dy));

	m_VectorRedoOperations.push_back(param);
}

void CLinkedObjectEditorView::OnUpdateEditUndo(CCmdUI *pCmdUI)
{
	int  cnt = (int)(m_VectorUndoOperations.size());
	if(cnt>0)
		pCmdUI->Enable(TRUE);
	else
		pCmdUI->Enable(FALSE);
}

void CLinkedObjectEditorView::OnEditRedo()
{
	MOVENODEPARAM param = m_VectorRedoOperations.back();
	m_VectorRedoOperations.pop_back();
	MoveSelectedElements(param.dwFileID, param.dx, param.dy);

	m_VectorUndoOperations.push_back(param);
}

void CLinkedObjectEditorView::OnUpdateEditRedo(CCmdUI *pCmdUI)
{
	int  cnt = (int)(m_VectorRedoOperations.size());
	if(cnt>0)
		pCmdUI->Enable(TRUE);
	else
		pCmdUI->Enable(FALSE);
}

bool CLinkedObjectEditorView::OpenPreviewFile(CString strLkoFileName)
{
	CLinkedObjectEditorDoc* pDoc = (CLinkedObjectEditorDoc*)GetDocument();
	if (pDoc == NULL)
		return false;

	FILE *f = NULL;
	_tfopen_s(&f, strLkoFileName, _T("rb"));
	if( f == NULL ) return FALSE;

	bool ret = m_plantObjectPreview.Load(f) ? true : false;
	fclose(f);
	if(!ret)
		return ret;

//	m_mapPreviewPicHandle.clear(); //一直缓存。。。。提高预览速度。。
	
	TCHAR workdir[MAX_PATH];
	GetModuleFileName(NULL, workdir, MAX_PATH);
	std::wstring strWorkDir = workdir;
	int npos = (int)strWorkDir.rfind('\\');
	strWorkDir = std::wstring(strWorkDir, 0, npos);
	std::wstring sysFilePath = _T("\\res\\map"); // \\elements\\

	std::wstring fileName = L"";
	
	pDoc->GetLinkedObjectStatusView()->m_strFileNameList.clear();

	Nuclear::PLinkedObject::VectorLinkedObjectInfo::iterator iter = m_plantObjectPreview.m_vectorLinkedObjectInfo.begin();
	for(; iter!=m_plantObjectPreview.m_vectorLinkedObjectInfo.end(); iter++)
	{
		fileName = iter->m_strPicName;
		std::wstring strKey = fileName;

		CString NodeInfo = strKey.c_str();
		if(iter->m_bMainNode)
		{
			m_FrctMainNodPos = Nuclear::FRECT(0.0f, 0.0f, (float)(iter->m_nWidth), (float)(iter->m_nHeight));
			NodeInfo += L"-------主节点";
		}
		else
		{
			NodeInfo += L"-------非主节点";
		}
		if(iter->m_LinkedObjectMoveParam.m_dwFlag & Nuclear::XP_LINKEDOBJ_LIGHT)
		{
			NodeInfo += L"-------灯光";
		}
		else
		{
			NodeInfo += L"-------非灯光";
		}

		pDoc->GetLinkedObjectStatusView()->m_strFileNameList.push_back(NodeInfo);

		std::wstring TempKey = fileName;
		std::transform( TempKey.begin(), TempKey.end(), TempKey.begin(), ::towlower);
		bool bres = PFS::CEnv::PFSFileNameToNativeFileName(TempKey, fileName);

		CString strFilePath = fileName.c_str();
		//读img文件初始化一个img对象..下面使用其相关信息来load纹理和宽高等...
		Nuclear::PImg imgObject;
		FILE *f = NULL;
		_tfopen_s(&f, strFilePath, _T("rb"));
		if( f == NULL )
		{
			OutputDebugString(L"InitData()---_tfopen_s()--error\n");
			continue;
		}

		BOOL ret = imgObject.Load(f) ? TRUE:FALSE;
		fclose(f);
		if(!ret)
		{
			OutputDebugString(L"InitData()---_tfopen_s()--error\n");
			continue;
		}

		int nPicWidth = imgObject.m_nFileWidth;
		int nPicHeight = imgObject.m_nFileHeight;

		std::map<std::wstring, PicHandleInfoVetor>::const_iterator iter = m_mapPreviewPicHandle.find(fileName);
		if(iter == m_mapPreviewPicHandle.end())
		{
			//读img文件初始化handleInfoVector--

			//根据文件名来加载显示...
			PicHandleInfoVetor handleInfoVector;
			handleInfoVector.clear();
			int nPos = strFilePath.ReverseFind('.');
			CString filePathNoEX = strFilePath.Left(nPos);

			int cnt = (int)imgObject.m_vectorRct.size();
			for(int i=0; i<cnt; i++)
			{
				CString str;
				str.Format(_T("_%d"), i);
				CString fileNameNoEx = filePathNoEX + str;
				Nuclear::PictureHandle handle = m_pRender->LoadPictureFromNativePath(std::wstring(fileNameNoEx));

				IMGPICHANDLEINFO picInfo;
				picInfo.m_handlePic = handle;
				picInfo.m_rct = imgObject.m_vectorRct.at(i);
				handleInfoVector.push_back(picInfo);
			}

			//
			m_mapPreviewPicHandle.insert( std::make_pair(strKey, handleInfoVector) );
		}
	}
	pDoc->GetLinkedObjectStatusView()->Invalidate(true);

	CreatePreviewDTList(m_plantObjectPreview);
	
	return true;
}

bool CLinkedObjectEditorView::CreatePreviewDTList(const Nuclear::PLinkedObject& previewObj)
{
	m_DynamicLinkedObjectPreview.RemoveAllNode();

	m_DynamicLinkedObjectPreview.SetLKOEnableZoomFlag(previewObj.m_bLKOEnableZoom);
	m_DynamicLinkedObjectPreview.SetLKOZoom(previewObj.m_fLKOzoom);
	m_DynamicLinkedObjectPreview.SetLKOTime(previewObj.m_fLKOTime);

	Nuclear::CPOINT pos = previewObj.m_ptCentPos + Nuclear::FPOINT(m_FrctMainNodPos.left, m_FrctMainNodPos.top);

	Nuclear::LkoTreeNode* tmpNode, *nodeRoot;
	DWORD rootId = previewObj.GetMainNodeID();
	Nuclear::PLinkedObject::VectorLinkedObjectInfo::const_iterator nodeIter = previewObj.m_vectorLinkedObjectInfo.begin() + rootId;
	if (nodeIter == previewObj.m_vectorLinkedObjectInfo.end())
		return false;
	tmpNode = new Nuclear::LkoTreeNode();
	tmpNode->pParent = NULL;
	tmpNode->pRoot = tmpNode;
	nodeRoot = tmpNode;
	tmpNode->LinkedObjectMoveParam.m_nMoveFollowType = nodeIter->m_LinkedObjectMoveParam.m_nMoveFollowType;
	tmpNode->height = nodeIter->m_nHeight;
	tmpNode->width = nodeIter->m_nWidth;
	tmpNode->rotateCent = nodeIter->m_rotateCent;
	Nuclear::CPOINT rootRotateCent = nodeIter->m_rotateCent;
	tmpNode->rotateCent.x += nodeIter->m_nWidth / 2;
	tmpNode->rotateCent.y += nodeIter->m_nHeight / 2;
	tmpNode->fromMainRotateCent = (pos - previewObj.m_ptCentPos + tmpNode->rotateCent).ToFPOINT();

	tmpNode->LinkedObjectMoveParam = nodeIter->m_LinkedObjectMoveParam;
	tmpNode->fAngle = tmpNode->fSkewx = 0.0f;
	tmpNode->fTranslationx = tmpNode->fTranslationy = 0.0f;
	m_DynamicLinkedObjectPreview.AddANode(tmpNode);

	int i = 0;
	for(nodeIter = previewObj.m_vectorLinkedObjectInfo.begin(); nodeIter != previewObj.m_vectorLinkedObjectInfo.end();++nodeIter,++i)
	{
		if (i == rootId)
			continue;
		tmpNode = new Nuclear::LkoTreeNode();
		tmpNode->pRoot = nodeRoot;
		tmpNode->pParent = nodeRoot;
		tmpNode->LinkedObjectMoveParam.m_nMoveFollowType = nodeIter->m_LinkedObjectMoveParam.m_nMoveFollowType;
		tmpNode->height = nodeIter->m_nHeight;
		tmpNode->width = nodeIter->m_nWidth;
		tmpNode->fromMainRotateCent = (nodeIter->m_rotateCent_2 - rootRotateCent).ToFPOINT();
		tmpNode->rotateCent = nodeIter->m_rotateCent;
		tmpNode->rotateCent.x += nodeIter->m_nWidth / 2;
		tmpNode->rotateCent.y += nodeIter->m_nHeight / 2;
		tmpNode->LinkedObjectMoveParam = nodeIter->m_LinkedObjectMoveParam;
		tmpNode->fAngle = tmpNode->fSkewx = 0.0f;
		tmpNode->fTranslationx = tmpNode->fTranslationy = 0.0f;
		m_DynamicLinkedObjectPreview.AddANode(tmpNode);
	}
	return true;
}