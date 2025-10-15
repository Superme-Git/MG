// TestManagerDlg.cpp : 实现文件
//

#include "stdafx.h"
#include "ParticleSystemEditor.h"
#include "TestManagerDlg.h"

#include "SetEffectGroupNameDlg.h"

#include <MMSystem.h>
#include "..\engine\common\log.h"
#include "..\engine\common\pfsutil.h"
#include "..\engine\particlesystem\CommonFuncs.h"

#include <Shlwapi.h>

// CTestManagerDlg 对话框

IMPLEMENT_DYNAMIC(CTestManagerDlg, CDialog)

CTestManagerDlg::CTestManagerDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CTestManagerDlg::IDD, pParent)
	, m_bMoveBKMap(FALSE)
	, m_bMoveEffect(FALSE)
	, m_bLineEmiterDemo(FALSE)
{
	m_pParSysManager = NULL;
	m_pRenderer = NULL;

	m_fFPS = 0.0f;
	m_nFrameCnt = 0;
	m_fTimeElapsed = 0.0f;
	m_fLastTime = 0.0f;

	m_curSelItem = NULL;
	m_curGroupItem = NULL;
	m_hBKpicHandle = 0;
	m_pPSParameterDlg = NULL;

	m_FrctMapPos = Nuclear::FRECT(0, 0, 100, 100);
	m_initialPoint = CPoint(0, 0); //拖动地图..
	m_ViewPort = Nuclear::CRECT();

	m_pDragImage = NULL;

	m_bDragging = false;	//是否正在拖曳
	m_hItemDrag = NULL;	//开始拖曳的位置
	m_hItemDrop = NULL;	//拖曳到哪里
	m_bBKchange = false;
	m_nComputerConfig = 0;
	m_nLowMiddleHigh = 1;

	m_nCurSelPt = -1;
}

CTestManagerDlg::~CTestManagerDlg()
{
	if(m_pRenderer != NULL)
	{
		bool bres = m_pRenderer->FreePicture(m_hBKpicHandle);
		if(bres)
			OutputDebugString(L"CTestManagerDlg--FreePicture(m_hBKpicHandle) succ\n");
	}

	DestroyRenderer(m_pRenderer); //psManager在这里面释放..
	//HRESULT hr = DestroyRenderer(m_pRenderer); //psManager在这里面释放..
	//if( SUCCEEDED(hr) )
	//	OutputDebugString(L"CTestManagerDlg--DestroyRenderer(m_pRenderer succ\n");

	if(m_pPSParameterDlg != NULL)
	{
		delete m_pPSParameterDlg;
		m_pPSParameterDlg = NULL;
	}
	if(m_pDragImage != NULL)
	{
		delete m_pDragImage;
		m_pDragImage = NULL;
	}
}

void CTestManagerDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_Tree_ParticleSysList, m_treeParticleSys);
	DDX_Check(pDX, IDC_CHECK_MoveBKMap, m_bMoveBKMap);
	DDX_Check(pDX, IDC_CHECK_MOVEEFFECT, m_bMoveEffect);
	DDX_Control(pDX, IDC_COMBO_COMPUTERCONFIG, m_comboComputerConfig);
	DDX_Radio(pDX, IDC_RADIO_LOW, m_nLowMiddleHigh);
	DDX_Check(pDX, IDC_CHECK_LineEmiterDemo, m_bLineEmiterDemo);
}

BEGIN_MESSAGE_MAP(CTestManagerDlg, CDialog)
	ON_BN_CLICKED(IDOK, &CTestManagerDlg::OnBnClickedOk)
	ON_BN_CLICKED(IDCANCEL, &CTestManagerDlg::OnBnClickedCancel)
	ON_WM_PAINT()

	ON_WM_TIMER()
	ON_NOTIFY(NM_CLICK, IDC_Tree_ParticleSysList, &CTestManagerDlg::OnNMClickTreeParticlesyslist)
	ON_NOTIFY(NM_RCLICK, IDC_Tree_ParticleSysList, &CTestManagerDlg::OnNMRclickTreeParticlesyslist)
	ON_COMMAND(ID_MENU_AddParticleSys, &CTestManagerDlg::OnMenuAddparticlesys)
	ON_COMMAND(ID_MENU_DelParticleSys, &CTestManagerDlg::OnMenuDelparticlesys)
	ON_COMMAND(ID_MENU_ModifyParticleSys, &CTestManagerDlg::OnMenuModifyparticlesys)
	ON_WM_LBUTTONDOWN()
	ON_WM_MOUSEMOVE()
	ON_COMMAND(ID_MENU_AddEffectGroup, &CTestManagerDlg::OnMenuAddeffectgroup)
	ON_COMMAND(ID_MENU_MODIFYGROUPNAME, &CTestManagerDlg::OnMenuModifygroupname)
	ON_COMMAND(ID_MENU_DELEFFECTGROUP, &CTestManagerDlg::OnMenuDeleffectgroup)
	ON_COMMAND(ID_MENU_SAVEEFFECTGROUP, &CTestManagerDlg::OnMenuSaveeffectgroup)
	ON_COMMAND(ID_MENU_LAODEFFECTGROUP, &CTestManagerDlg::OnMenuLaodeffectgroup)
	ON_BN_CLICKED(IDC_CHECK_MoveBKMap, &CTestManagerDlg::OnBnClickedCheckMovebkmap)
	ON_BN_CLICKED(IDC_BUTTON_SetBkMap, &CTestManagerDlg::OnBnClickedButtonSetbkmap)
	ON_NOTIFY(TVN_BEGINDRAG, IDC_Tree_ParticleSysList, &CTestManagerDlg::OnTvnBegindragTreeParticlesyslist)
	ON_WM_LBUTTONUP()
	ON_COMMAND(ID_MENU_pslPROPERTY, &CTestManagerDlg::OnMenupslproperty)
	ON_COMMAND(ID_MENU_PSreplay, &CTestManagerDlg::OnMenuPsreplay)
	ON_WM_KEYDOWN()
	ON_NOTIFY(TVN_KEYDOWN, IDC_Tree_ParticleSysList, &CTestManagerDlg::OnTvnKeydownTreeParticlesyslist)
	ON_WM_CLOSE()
	ON_BN_CLICKED(IDC_BUTTON_PSLGfileVERconversion, &CTestManagerDlg::OnBnClickedButtonPslgfileverconversion)
	ON_BN_CLICKED(IDC_CHECK_MOVEEFFECT, &CTestManagerDlg::OnBnClickedCheckMoveeffect)
	ON_CBN_SELCHANGE(IDC_COMBO_COMPUTERCONFIG, &CTestManagerDlg::OnCbnSelchangeComboComputerconfig)
	ON_BN_CLICKED(IDC_RADIO_LOW, &CTestManagerDlg::OnBnClickedRadioLow)
	ON_BN_CLICKED(IDC_RADIO_MIDDLE, &CTestManagerDlg::OnBnClickedRadioMiddle)
	ON_BN_CLICKED(IDC_RADIO_HIGH, &CTestManagerDlg::OnBnClickedRadioHigh)
	ON_NOTIFY(TVN_SELCHANGED, IDC_Tree_ParticleSysList, &CTestManagerDlg::OnTvnSelchangedTreeParticlesyslist)
	ON_BN_CLICKED(IDC_SAVE_CONFIGPARAM, &CTestManagerDlg::OnBnClickedSaveConfigparam)
	ON_BN_CLICKED(IDC_CHECK_LineEmiterDemo, &CTestManagerDlg::OnBnClickedCheckLineemiterdemo)
	ON_WM_LBUTTONDBLCLK()
END_MESSAGE_MAP()


// CTestManagerDlg 消息处理程序

void CTestManagerDlg::OnBnClickedOk()
{
	// TODO: 在此添加控件通知处理程序代码
//	OnOK();
}

void CTestManagerDlg::OnBnClickedCancel()
{
	// TODO: 在此添加控件通知处理程序代码
	OnCancel();
}

BOOL CTestManagerDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	GetDlgItem(IDC_PICTURE_DRAWPARTICLES)->GetWindowRect(&m_rectDrawParticle);
	ScreenToClient(&m_rectDrawParticle);


	m_ViewPort = Nuclear::CRECT(300, 10, 900, 460);

	m_strSysPath = theApp.GetResPath() + L"\\effect\\particle\\";

//	m_ImageList.Create(IDB_BITMAP_ICONS_SINGLE, 16, 1, RGB (255, 255, 255));
	HTREEITEM hItem = m_treeParticleSys.InsertItem(L"group1"); //ParticleManager
//	m_treeParticleSys.SetImageList(&m_ImageList, TVSIL_NORMAL);
	
//	BOOL bres = m_treeParticleSys.SetCheck(hItem, true);
	m_treeParticleSys.Invalidate();

	BOOL bres = InitD3D();
	if(!bres)
		return false;

	m_pParSysManager = dynamic_cast<Nuclear::ParticleManager*>(m_pRenderer->GetParticleManager());
	m_pParSysManager->SetGCParam(10.0, 3.0f);

	CString strBKpicFilePath = L"/effect/particle/texture/bkground.bmp";
	wchar_t workdir[MAX_PATH];
	::GetCurrentDirectory(MAX_PATH, workdir);
	CString iniFile = workdir;
	iniFile += L"\\ParticleSystemEditor.ini";
	CString strSection = L"ALL";
	CString strSectionKey = L"BKpicture";
	TCHAR value[256];
	std::wstring outFile;
	GetPrivateProfileString(strSection, strSectionKey, strBKpicFilePath, value, 256, iniFile);
	PFS::CEnv::NativeFileNameToPFSFileName(value, outFile);
	m_hBKpicHandle = m_pRenderer->LoadPicture(outFile);

	Nuclear::PictureInfo picinfo;
	bres = m_pRenderer->GetPictureInfo(m_hBKpicHandle, picinfo);
	float left = 0;//(float)m_rectDrawParticle.left;
	float top = 0;//(float)m_rectDrawParticle.top;
	float right = left + picinfo.m_nPicWidth;
	float bottom = top + picinfo.m_nPicHeight;
	m_FrctMapPos = Nuclear::FRECT(left, top, right, bottom);
	
	m_pPSParameterDlg = new CPSParameterDlg;
	if(m_pPSParameterDlg != NULL)
	{
		BOOL bres = m_pPSParameterDlg->Create(IDD_DIALOG_PSPARAMETERDLG, this);
		if(!bres) //Create failed.
			AfxMessageBox(L"Error creating Dialog");
		CRect rct(700, 600, 1050, 780);
		m_pPSParameterDlg->MoveWindow(&rct);
		m_pPSParameterDlg->ShowWindow(SW_HIDE);
	}

	SetTimer(1, 10, NULL);

	m_comboComputerConfig.ResetContent();
	m_comboComputerConfig.InsertString(0, L"高配");
	m_comboComputerConfig.InsertString(1, L"中配");
	m_comboComputerConfig.InsertString(2, L"低配");
	m_comboComputerConfig.SetCurSel(0);

	return TRUE; // return TRUE unless you set the focus to a control
	// 异常: OCX 属性页应返回 FALSE
}

void CTestManagerDlg::Render()
{
	if(m_pRenderer == NULL)
		return;

	float fCurTime = (float)timeGetTime();
	float timeDelta = (fCurTime - m_fLastTime)*0.001f;	
	m_nFrameCnt++;
	m_fTimeElapsed += timeDelta;
	if(m_fTimeElapsed >= 1.0f)
	{
		m_fFPS = (float)m_nFrameCnt / m_fTimeElapsed;

		m_fTimeElapsed = 0.0f;
		m_nFrameCnt = 0;
	}
	m_fLastTime = fCurTime;

	HTREEITEM hRootItem = m_treeParticleSys.GetRootItem();
	if(hRootItem == NULL)
		return;

	m_pRenderer->Begin(0xff000000);
	m_pRenderer->DrawPicture(m_hBKpicHandle, m_FrctMapPos, 0xFFFFFFFF);

	int nActiveParticles = 0;

	HTREEITEM hCurrent = hRootItem;
	while (hCurrent != NULL)
	{ 
		if(m_treeParticleSys.ItemHasChildren(hCurrent))
		{
			HTREEITEM hChildItem = m_treeParticleSys.GetChildItem(hCurrent);

			while(hChildItem != NULL)
			{
				BOOL bCheck = m_treeParticleSys.GetCheck(hChildItem);
				if(bCheck)
				{
					CString strItemName = m_treeParticleSys.GetItemText(hChildItem);
					int pos = strItemName.ReverseFind('-');
					int cnt = strItemName.GetLength();
					CString strHandle = strItemName.Right(cnt-pos-2);
					int psHandle = _wtoi(strHandle.GetString());
				//	Nuclear::PSINFO psInfo = m_pParSysManager->GetParticleSystemInfo(psHandle);
					
				//	if(  psInfo.m_nPSLConfigReq <= m_nComputerConfig )
						m_pParSysManager->RenderParticleSystem(psHandle);


					nActiveParticles += m_pParSysManager->GetNumActiveParticles(psHandle);

				}
				hChildItem = m_treeParticleSys.GetNextItem(hChildItem, TVGN_NEXT);
			}
		}
		hCurrent = m_treeParticleSys.GetNextItem(hCurrent, TVGN_NEXT);
	}

	//绘制特效链的路径
	//m_fpoints
	if(m_bLineEmiterDemo)
	{
		int cntPt = (int)( m_fpoints.size() );
		for(int i=0; i<cntPt; i++)
		{
			Nuclear::XPCOLOR cr = 0xffff0000;
			if(m_nCurSelPt == i)
			{
				cr = 0xffffff00;
			}
			m_pRenderer->DrawEllipse(m_fpoints[i].x, m_fpoints[i].y, 3, 3, cr, Nuclear::XPFM_WIREFRAME);

			if( i>0 )
			{
				m_pRenderer->DrawLine(m_fpoints[i-1].x, m_fpoints[i-1].y, m_fpoints[i].x, m_fpoints[i].y, 0xff00ff00);
			}
		}

		int cntPS = (int)(m_vectorPS.size());
		for(int i=0; i<cntPS; i++)
		{
			m_pParSysManager->RenderParticleSystem(m_vectorPS[i]);
		}
	}
	//

//	int cnt = m_pParSysManager->GetParticleSystemCount();

	int nTotalNum = nActiveParticles;//m_pParSysManager->GetTotalNumActiveParticles();

	TCHAR szbuffer[256];
	int f1 = (int)m_fFPS*10;
//	int f1 = (int)(m_pRenderer->GetFPS()*10);
	wsprintf(szbuffer, L"TFPS: %3d.%1d %5d------\n", f1/10, f1%10, nTotalNum);	
	RECT trect;
	trect.left = 0;
	trect.top =0;
	trect.right = 800;
	trect.bottom = 600;
	m_pRenderer->DrawText(szbuffer, 20, &trect, 0xffff0000);
// 
// 	OutputDebugString(szbuffer);

	m_pRenderer->End();
}

bool CTestManagerDlg::InitD3D()
{					//m_rectDrawParticle.right-m_rectDrawParticle.left //m_rectDrawParticle.bottom-m_rectDrawParticle.top
	HRESULT hr = CreateRenderer(&m_pRenderer, m_hWnd, Nuclear::XDisplayMode(GetSystemMetrics(SM_CXSCREEN), GetSystemMetrics(SM_CYSCREEN)), 
									theApp.m_pFileIOManager, Nuclear::XPCRF_VERTICALSYNC);
	if( FAILED(hr) )
		return false;

	Nuclear::CRECT sourceRect(0, 0, m_rectDrawParticle.right-m_rectDrawParticle.left, m_rectDrawParticle.bottom-m_rectDrawParticle.top);
	m_pRenderer->SetSourceRect(&sourceRect);

	Nuclear::CRECT destRect(m_rectDrawParticle.left, m_rectDrawParticle.top, m_rectDrawParticle.right, m_rectDrawParticle.bottom);
	m_pRenderer->SetDestRect(&destRect);
	return true;
}
void CTestManagerDlg::OnPaint()
{
	CPaintDC dc(this); // device context for painting
	// TODO: 在此处添加消息处理程序代码
		
	Render();

	// 不为绘图消息调用 CDialog::OnPaint()
}

void CTestManagerDlg::OnTimer(UINT_PTR nIDEvent)
{
//	if( nIDEvent==1 )
		Render();
	CDialog::OnTimer(nIDEvent);
}

void CTestManagerDlg::OnNMClickTreeParticlesyslist(NMHDR *pNMHDR, LRESULT *pResult)
{
	// TODO: 在此添加控件通知处理程序代码
	POINT Point; 
	::GetCursorPos(&Point); 
	m_treeParticleSys.ScreenToClient(&Point);
	UINT uFlags;
	HTREEITEM hItem = m_treeParticleSys.HitTest( Point, &uFlags );
	HTREEITEM hParentItem = NULL;
	if(hItem != NULL)
		hParentItem = m_treeParticleSys.GetParentItem(hItem);

	if(hParentItem == NULL) //组节点
	{
		m_curSelItem = NULL;
		m_curGroupItem = hItem;

		if(uFlags & TVHT_ONITEMSTATEICON)//是点击CHECK框
		{
			BOOL bCheck = m_treeParticleSys.GetCheck(hItem);
			// 检查CheckBox的状态，返回的是在点击之前的状态
			// 意思就是得出来是TRUE，那么待会就会是FALSE
			// 得出来是FALSE，那么待会就会是TRUE

			if(m_treeParticleSys.ItemHasChildren(hItem))
			{
				HTREEITEM hChildItem = m_treeParticleSys.GetChildItem(hItem);

				while(hChildItem != NULL)
				{
					BOOL bres = m_treeParticleSys.SetCheck(hChildItem, !bCheck);
					hChildItem = m_treeParticleSys.GetNextItem(hChildItem, TVGN_NEXT);
				}
				m_treeParticleSys.Invalidate();
			}
		}
	}
	else //psl节点
	{
		m_curSelItem = hItem;
		m_curGroupItem = NULL;
	//	m_nLowMiddleHigh
		InitCtrlPriorityParam();
	}

	*pResult = 0;
}

void CTestManagerDlg::InitCtrlPriorityParam()
{
	if(m_curSelItem != NULL)
	{
		CString strItemName = m_treeParticleSys.GetItemText(m_curSelItem);
		int pos = strItemName.ReverseFind('-');
		int cnt = strItemName.GetLength();
		CString strHandle = strItemName.Right(cnt-pos-2);
		int psHandle = _wtoi(strHandle.GetString());

		Nuclear::CParticleSystem* pParticleSys = m_pParSysManager->GetParticleSysByHandle(psHandle);
		if(pParticleSys == NULL)
		{
			MessageBox(L"逻辑错误..CTestManagerDlg::OnMenuModifyparticlesys()，请告诉相关程序");
			return;
		}

		m_nLowMiddleHigh = pParticleSys->GetPSL()->m_nPSConfigReq;
		UpdateData(false);
	}
}

void CTestManagerDlg::SetParticleSysPriorityParam()
{
	if(m_curSelItem != NULL)
	{
		CString strItemName = m_treeParticleSys.GetItemText(m_curSelItem);
		int pos = strItemName.ReverseFind('-');
		int cnt = strItemName.GetLength();
		CString strHandle = strItemName.Right(cnt-pos-2);
		int psHandle = _wtoi(strHandle.GetString());

		Nuclear::CParticleSystem* pParticleSys = m_pParSysManager->GetParticleSysByHandle(psHandle);
		if(pParticleSys == NULL)
		{
			MessageBox(L"逻辑错误..CTestManagerDlg::OnMenuModifyparticlesys()，请告诉相关程序");
			return;
		}

		pParticleSys->GetPSL()->m_nPSConfigReq = m_nLowMiddleHigh;
	}
}

void CTestManagerDlg::OnNMRclickTreeParticlesyslist(NMHDR *pNMHDR, LRESULT *pResult)
{
	CPoint pt;
	POINT Point; 
	::GetCursorPos(&Point); 
	pt = Point;
	m_treeParticleSys.ScreenToClient(&Point);
	UINT uFlags;
	HTREEITEM hItem = m_treeParticleSys.HitTest( Point, &uFlags );
	HTREEITEM hParentItem = NULL;

	if(hItem != NULL)
		hParentItem = m_treeParticleSys.GetParentItem(hItem);

	CMenu menu;
	menu.LoadMenu(IDR_MENU2);
	menu.EnableMenuItem(ID_MENU_AddEffectGroup, MF_BYCOMMAND | MF_DISABLED | MF_GRAYED);
	menu.EnableMenuItem(ID_MENU_AddParticleSys, MF_BYCOMMAND | MF_DISABLED | MF_GRAYED);
	menu.EnableMenuItem(ID_MENU_DelParticleSys, MF_BYCOMMAND | MF_DISABLED | MF_GRAYED);
	menu.EnableMenuItem(ID_MENU_ModifyParticleSys, MF_BYCOMMAND | MF_DISABLED | MF_GRAYED);
	menu.EnableMenuItem(ID_MENU_MODIFYGROUPNAME, MF_BYCOMMAND | MF_DISABLED | MF_GRAYED);
	menu.EnableMenuItem(ID_MENU_DELEFFECTGROUP, MF_BYCOMMAND | MF_DISABLED | MF_GRAYED);
	menu.EnableMenuItem(ID_MENU_SAVEEFFECTGROUP, MF_BYCOMMAND | MF_DISABLED | MF_GRAYED);
	menu.EnableMenuItem(ID_MENU_LAODEFFECTGROUP, MF_BYCOMMAND | MF_DISABLED | MF_GRAYED);
	menu.EnableMenuItem(ID_MENU_pslPROPERTY, MF_BYCOMMAND | MF_DISABLED | MF_GRAYED);

	if(hItem == NULL)
	{//非节点区---允许新建组、添加组
		menu.EnableMenuItem(ID_MENU_AddEffectGroup, MF_BYCOMMAND | MF_ENABLED);
		menu.EnableMenuItem(ID_MENU_LAODEFFECTGROUP, MF_BYCOMMAND | MF_ENABLED);
	}
	else
	{
		if( hParentItem==NULL ) //组节点
		{
			menu.EnableMenuItem(ID_MENU_AddParticleSys, MF_BYCOMMAND | MF_ENABLED);
			menu.EnableMenuItem(ID_MENU_MODIFYGROUPNAME, MF_BYCOMMAND | MF_ENABLED);
			menu.EnableMenuItem(ID_MENU_DELEFFECTGROUP, MF_BYCOMMAND | MF_ENABLED);
			menu.EnableMenuItem(ID_MENU_SAVEEFFECTGROUP, MF_BYCOMMAND | MF_ENABLED);
			m_curGroupItem = hItem;
			m_curSelItem = NULL;
		}
		else //psl节点
		{
			menu.EnableMenuItem(ID_MENU_DelParticleSys, MF_BYCOMMAND | MF_ENABLED);	
			menu.EnableMenuItem(ID_MENU_pslPROPERTY, MF_BYCOMMAND | MF_ENABLED);
			m_curGroupItem = NULL;
			m_curSelItem = hItem;
		}
		menu.EnableMenuItem(ID_MENU_PSreplay, MF_BYCOMMAND | MF_ENABLED);
		menu.EnableMenuItem(ID_MENU_ModifyParticleSys, MF_BYCOMMAND | MF_ENABLED);
	}

	CMenu* pMenu=menu.GetSubMenu(0);
	pMenu->TrackPopupMenu(TPM_LEFTALIGN | TPM_RIGHTBUTTON, pt.x, pt.y, this, NULL);

	*pResult = 0;
}

void CTestManagerDlg::OnMenuAddparticlesys()
{
	CString strDefExt = FNExt_ParticleWS;
	strDefExt += L"|all files";
	CString strFilter = L"";
	strFilter = strFilter + L"(*." + FNExt_ParticleWS + L")|*." + FNExt_ParticleWS + L"|(*.*)|*.*||";

	std::wstring strInitFolder = m_strSysPath + L"psl\\*." + FNExt_ParticleWS;

	DWORD dwFlags = OFN_HIDEREADONLY|OFN_PATHMUSTEXIST|OFN_ALLOWMULTISELECT;

	CFileDialog dlg(true, strDefExt, strInitFolder.c_str(), dwFlags, strFilter, NULL);

	if(IDOK == dlg.DoModal())
	{
		CString fileName;
		POSITION pos = dlg.GetStartPosition();
		while(pos != NULL)
		{
			fileName = dlg.GetNextPathName(pos);//得到单个文件名
			fileName.Trim();
			int npos = fileName.Find(L"\\psl\\");
			fileName = fileName.Right(fileName.GetLength() - npos-5);

			Nuclear::PSParameter param;//默认参数初始化...利用当前粒子生成一个ps
			param.cycle = true;
			Nuclear::ParticleSystemHandle psHandle = m_pParSysManager->CreateParticleSystem( (std::wstring)( fileName.GetString() ), param);

			if(psHandle == Nuclear::INVALID_PS_HANDLE)
			{
				CString str = fileName;
				str += L"不能被正确读取--可能是使用的texture已经不存在了?\n";
				MessageBox(str);
				continue;
			}

			TCHAR buff[32];
			memset(buff, 0, 32*sizeof(TCHAR));
			_itow_s(psHandle, buff, 6, 10);
			std::wstring strHandle = buff;
			std::wstring ItemName = fileName + L" - ";
			ItemName = ItemName + strHandle;

			HTREEITEM hItem = m_treeParticleSys.InsertItem(ItemName.c_str(), m_curGroupItem);
		//	m_treeParticleSys.SetCheck(hItem, true);
			m_treeParticleSys.Invalidate();
		}
	}
}

void CTestManagerDlg::OnMenuDelparticlesys()
{
	CString strItemName = m_treeParticleSys.GetItemText(m_curSelItem);
	int pos = strItemName.ReverseFind('-');
	int cnt = strItemName.GetLength();
	CString strHandle = strItemName.Right(cnt-pos-2);
	int psHandle = _wtoi(strHandle.GetString());

	m_pParSysManager->DeleteParticleSystem(psHandle);
	m_treeParticleSys.DeleteItem(m_curSelItem);
	m_treeParticleSys.Invalidate();
	m_curSelItem = NULL;
}

void CTestManagerDlg::OnMenuModifyparticlesys()
{
	HTREEITEM hItem = NULL;
	hItem = m_treeParticleSys.GetSelectedItem();
	HTREEITEM hParent = m_treeParticleSys.GetParentItem(hItem);

	if(m_treeParticleSys.ItemHasChildren(hItem))
	{
		m_curGroupItem = hItem;
		m_curSelItem = NULL;
	}
	else if(hParent != NULL)
	{
		m_curSelItem = hItem;
		m_curGroupItem = NULL;
	}
	else
	{
	}

	if(m_curSelItem != NULL)
	{
		CString strItemName = m_treeParticleSys.GetItemText(m_curSelItem);
		int pos = strItemName.ReverseFind('-');
		int cnt = strItemName.GetLength();
		CString strHandle = strItemName.Right(cnt-pos-2);
		int psHandle = _wtoi(strHandle.GetString());

		Nuclear::CParticleSystem* pParticleSys = m_pParSysManager->GetParticleSysByHandle(psHandle);
		if(pParticleSys == NULL)
		{
			MessageBox(L"逻辑错误..CTestManagerDlg::OnMenuModifyparticlesys()，请告诉相关程序");
			return;
		}
		
		Nuclear::PSParameter& param = pParticleSys->GetPSParameter();

		CPSParameterDlg psParameterDlg;
		psParameterDlg.SetPSParameter(param);

		if(IDOK == psParameterDlg.DoModal())
		{
			CString strItemName = m_treeParticleSys.GetItemText(m_curSelItem);
			int pos = strItemName.ReverseFind('-');
			int cnt = strItemName.GetLength();
			CString strHandle = strItemName.Right(cnt-pos-2);
			int psHandle = _wtoi(strHandle.GetString());

			BOOL bCycleFlag = psParameterDlg.GetPsCycleFlag();
			param.cycle = (bCycleFlag != 0);
	//		m_pParSysManager->ModifyParticleSystem(psHandle, param);
		}
	}
	else if(m_curGroupItem != NULL)
	{
		CPSParameterDlg psParameterDlg;
		if(IDOK == psParameterDlg.DoModal())
		{
			BOOL bCycleFlag = psParameterDlg.GetPsCycleFlag();

			HTREEITEM hChildItem = m_treeParticleSys.GetChildItem(m_curGroupItem);
			while(hChildItem != NULL)
			{
				BOOL bCheck = m_treeParticleSys.GetCheck(hChildItem);
				if(bCheck)
				{
					CString strItemName = m_treeParticleSys.GetItemText(hChildItem);
					int pos = strItemName.ReverseFind('-');
					int cnt = strItemName.GetLength();
					CString strHandle = strItemName.Right(cnt-pos-2);
					int psHandle = _wtoi(strHandle.GetString());

					Nuclear::CParticleSystem* pParticleSys = m_pParSysManager->GetParticleSysByHandle(psHandle);
					if(pParticleSys == NULL)
					{
						MessageBox(L"逻辑错误..CTestManagerDlg::OnMenuModifyparticlesys()，请告诉相关程序");
						return;
					}

					Nuclear::PSParameter& param = pParticleSys->GetPSParameter();
					param.cycle = (bCycleFlag != 0);
			//		m_pParSysManager->ModifyParticleSystem(psHandle, param);
				}
				hChildItem = m_treeParticleSys.GetNextItem(hChildItem, TVGN_NEXT);
			}
		}
	}
	else
	{
		MessageBox(L"CTestManagerDlg::OnMenuModifyparticlesys()--逻辑错误\n");
	}
}

void CTestManagerDlg::OnLButtonDblClk(UINT nFlags, CPoint point)
{
	// TODO: Add your message handler code here and/or call default

	BOOL bres = m_rectDrawParticle.PtInRect(point);
	if(bres && m_bLineEmiterDemo)
	{
		m_fpoints.clear();
		int cnt = (int)(m_vectorPS.size());
		for(int i=0; i<cnt; i++)
		{
			m_pParSysManager->DeleteParticleSystem(m_vectorPS[i]);
		}
		m_vectorPS.clear();
	}

	CDialog::OnLButtonDblClk(nFlags, point);
}

float CTestManagerDlg::ComputerDistance(Nuclear::FPOINT pt1, Nuclear::FPOINT pt2)
{
	float dis = powf((float)(pt1.x - pt2.x), 2.0f) + powf((float)(pt1.y - pt2.y), 2.0f);
	dis = sqrt(dis);
	return dis;
}

void CTestManagerDlg::OnLButtonDown(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	BOOL bres = m_rectDrawParticle.PtInRect(point);
	if(bres)
	{
		if(m_bMoveEffect)
			ModifyParticleSysPosInfo(point);
		else if(m_bMoveBKMap)
		{//拖动地图 m_FrctMapPos
			m_initialPoint = point;
		}
		else if(m_bLineEmiterDemo)
		{//添加目标点
			m_nCurSelPt = -1;
			Nuclear::FPOINT fpt;
			fpt.x = point.x - m_rectDrawParticle.left;
			fpt.y = point.y - m_rectDrawParticle.top;

			int cnt = (int)(m_fpoints.size());
			for(int i=0; i<cnt; i++)
			{
				float fdis = ComputerDistance(fpt, m_fpoints[i]);
				if(fdis<5.0f)
				{
					m_nCurSelPt = i;
					break;
				}
			}

			if(m_nCurSelPt == -1)
			{
				m_fpoints.push_back(fpt);

				cnt = (int)(m_fpoints.size());
				if( cnt>1 )
				{
					std::wstring fileName = L"testLineEmiter.";
					fileName += FNExt_ParticleWS;
					Nuclear::PSParameter param;//默认参数初始化...利用当前粒子生成一个ps
					param.m_ptLineEmitterStrt = m_fpoints[cnt-2];
					param.m_ptLineEmitterEnd = m_fpoints[cnt-1];
					param.time = 5.0f;
					param.cycle = true;
					param.m_bModifyLineEmitter = true;

				//	param.m_psModifyParam.m_pointCent = (m_fpoints[cnt-2] + m_fpoints[cnt-1])/2.0f;
				//	param.m_psModifyParam.m_fpoint0 = m_fpoints[cnt-2];
				//	param.m_psModifyParam.m_fpoint1 = m_fpoints[cnt-1];

					Nuclear::ParticleSystemHandle psHandle = m_pParSysManager->CreateParticleSystem(fileName, param);

					if(psHandle == Nuclear::INVALID_PS_HANDLE)
					{
						fileName += L"不能被正确读取--可能是使用的texture已经不存在了?\n";
						MessageBox(fileName.c_str());
					}

					m_vectorPS.push_back(psHandle);
				}
			}
		}
		else
		{
			//什么都不做
		}
	}

	CDialog::OnLButtonDown(nFlags, point);
}

void CTestManagerDlg::OnMouseMove(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	BOOL bres = m_rectDrawParticle.PtInRect(point);
	if( (nFlags==MK_LBUTTON)&&(!m_bDragging) ) //
	{
		if(bres)
		{
			if(m_bMoveEffect)
				ModifyParticleSysPosInfo(point);
			else if(m_bMoveBKMap)
			{//拖动地图 m_FrctMapPos
				int dx = point.x - m_initialPoint.x;
				int dy = point.y - m_initialPoint.y;
				m_initialPoint = point;
				m_FrctMapPos += Nuclear::FPOINT((float)dx, (float)dy);
		//		m_ViewPort -= Nuclear::CPOINT(dx, dy);
		//		m_pParSysManager->OnViewportUpdate(m_ViewPort);
				//修改粒子发射位置... //todo。
			}
			else if(m_bLineEmiterDemo)
			{
				if(m_nCurSelPt != -1)
				{
					Nuclear::FPOINT fpt;
					fpt.x = point.x - m_rectDrawParticle.left;
					fpt.y = point.y - m_rectDrawParticle.top;

					m_fpoints[m_nCurSelPt] = fpt;

					Nuclear::PSModifyParam param;
					if(m_nCurSelPt==0)
					{
				//		param.m_pointCent = (fpt+m_fpoints[1])/2.0f;
						param.m_fpoint0 = fpt;
						param.m_fpoint1 = m_fpoints[1];

						m_pParSysManager->ModifyParticleSystem(m_vectorPS[0], param);
					}
					else if( m_nCurSelPt == m_fpoints.size()-1 )
					{
				//		param.m_pointCent = (fpt+m_fpoints[m_nCurSelPt-1])/2.0f;
						param.m_fpoint0 = m_fpoints[m_nCurSelPt-1];
						param.m_fpoint1 = fpt;
						m_pParSysManager->ModifyParticleSystem(m_vectorPS[m_nCurSelPt-1], param);
					}
					else
					{
				//		param.m_pointCent = (fpt+m_fpoints[m_nCurSelPt-1])/2.0f;
						param.m_fpoint0 = m_fpoints[m_nCurSelPt-1];
						param.m_fpoint1 = fpt;
						m_pParSysManager->ModifyParticleSystem(m_vectorPS[m_nCurSelPt-1], param);

				//		param.m_pointCent = (fpt+m_fpoints[m_nCurSelPt+1])/2.0f;
						param.m_fpoint0 = fpt;
						param.m_fpoint1 = m_fpoints[m_nCurSelPt+1];
						m_pParSysManager->ModifyParticleSystem(m_vectorPS[m_nCurSelPt], param);
					}


				}
			}
			else
			{
				//什么都不用做
			}
		}
	}
	else
	{
		if(m_bDragging)
		{
			CPoint pt(point);
			ClientToScreen(&pt);
			m_pDragImage->DragMove(pt);

			SetCursor(LoadCursor(NULL, IDC_ARROW));
			UINT uFlags;

			// Turn off hilight for previous drop target
		//	m_treeParticleSys.SetItemState (m_hItemDrop, 0, LVIS_DROPHILITED);
			
			// Get the item that is below cursor
			m_treeParticleSys.ScreenToClient(&pt);
			m_hItemDrop = m_treeParticleSys.HitTest(pt, &uFlags);
			// Highlight it
			if(m_hItemDrop != NULL)
			{
				m_pDragImage->DragShowNolock(false);
		//		m_treeParticleSys.SetItemState(m_hItemDrop, LVIS_DROPHILITED, LVIS_DROPHILITED);
				m_treeParticleSys.SelectItem(m_hItemDrop); //SelectDropTarget
				m_curSelItem = m_hItemDrop;
				m_pDragImage->DragShowNolock(true);
			}
	//		m_treeParticleSys.UpdateWindow();

			//If we are not hovering over a CListCtrl, change the cursor
			// to note that we cannot drop here
			//	SetCursor(LoadCursor(NULL, IDC_NO));

			// Lock window updates
		}
	}

	CDialog::OnMouseMove(nFlags, point);
}

bool CTestManagerDlg::ModifyParticleSysPosInfo(CPoint point)
{
	if(m_curSelItem==NULL && m_curGroupItem==NULL)
		return false;

	if(m_curSelItem != NULL)
	{
		BOOL bCheck = m_treeParticleSys.GetCheck(m_curSelItem);
		if(bCheck)
		{
			CString strItemName = m_treeParticleSys.GetItemText(m_curSelItem);
			int pos = strItemName.ReverseFind('-');
			int cnt = strItemName.GetLength();
			CString strHandle = strItemName.Right(cnt-pos-2);
			int psHandle = _wtoi(strHandle.GetString());

			Nuclear::PSModifyParam param;
			POINT pt;
			pt.x = point.x-m_rectDrawParticle.left;//800* (point.x -m_rectDrawParticle.left)/(m_rectDrawParticle.right-m_rectDrawParticle.left);
			pt.y = point.y-m_rectDrawParticle.top;//600* (point.y -m_rectDrawParticle.top)/(m_rectDrawParticle.bottom-m_rectDrawParticle.top);

			param.m_pointCent.x = (float)pt.x;
			param.m_pointCent.y = (float)pt.y;

			bool bres = m_pParSysManager->ModifyParticleSystem(psHandle, param);
		}
	}
	else if(m_curGroupItem != NULL)
	{
		if(m_treeParticleSys.ItemHasChildren(m_curGroupItem))
		{
			HTREEITEM hChildItem = m_treeParticleSys.GetChildItem(m_curGroupItem);

			while(hChildItem != NULL)
			{
				BOOL bCheck = m_treeParticleSys.GetCheck(hChildItem);
				if(bCheck)
				{
					CString strItemName = m_treeParticleSys.GetItemText(hChildItem);
					int pos = strItemName.ReverseFind('-');
					int cnt = strItemName.GetLength();
					CString strHandle = strItemName.Right(cnt-pos-2);
					int psHandle = _wtoi(strHandle.GetString());

					Nuclear::PSModifyParam param;
					POINT pt;
					pt.x = point.x-m_rectDrawParticle.left;//800* (point.x -m_rectDrawParticle.left)/(m_rectDrawParticle.right-m_rectDrawParticle.left);
					pt.y = point.y-m_rectDrawParticle.top;//600* (point.y -m_rectDrawParticle.top)/(m_rectDrawParticle.bottom-m_rectDrawParticle.top);

					param.m_pointCent.x = (float)pt.x;
					param.m_pointCent.y = (float)pt.y;

					bool bres = m_pParSysManager->ModifyParticleSystem(psHandle, param);
				}
				hChildItem = m_treeParticleSys.GetNextItem(hChildItem, TVGN_NEXT);
			}
			m_treeParticleSys.Invalidate();
		}
	}
	else
	{
		MessageBox(L"怎么会？？");
		return false;
	}
	return true;
}

void CTestManagerDlg::OnMenuAddeffectgroup()
{
	CSetEffectGroupNameDlg dlg;
	if(IDOK == dlg.DoModal())
	{
		CString strEffectGroupName = dlg.GetEffectGroupName();
		HTREEITEM hItem = m_treeParticleSys.InsertItem(strEffectGroupName.GetString()); 
	}

	//	m_treeParticleSys.SetItemText(hItem, L"group1");
	m_treeParticleSys.Invalidate();
}

void CTestManagerDlg::OnMenuModifygroupname()
{
	CSetEffectGroupNameDlg dlg;
	if(IDOK == dlg.DoModal())
	{
		CString strEffectGroupName = dlg.GetEffectGroupName();
		m_treeParticleSys.SetItemText(m_curGroupItem, strEffectGroupName.GetString()); 
	}
}

void CTestManagerDlg::OnMenuDeleffectgroup()
{
	if(m_curGroupItem == NULL)
		return;
	DelChildItems(m_curGroupItem); //删除m_curGroupItem的所有子节点以及m_curGroupItem本身。
	Invalidate();
}

HTREEITEM CTestManagerDlg::DelChildItems( HTREEITEM hItem )
{
	if(hItem == NULL)
		return NULL;
	HTREEITEM hPare = NULL;
	if( !m_treeParticleSys.ItemHasChildren(hItem) )
	{
		hPare = m_treeParticleSys.GetParentItem( hItem );
		CString str = m_treeParticleSys.GetItemText( hItem );
		str = L"Delete: " + str+L"\n";
		OutputDebugString( str );

		int pos = str.ReverseFind('-');
		int cnt = str.GetLength();
		CString strHandle = str.Right(cnt-pos-2);
		int psHandle = _wtoi(strHandle.GetString());

		m_pParSysManager->DeleteParticleSystem(psHandle);
		m_treeParticleSys.DeleteItem( hItem );

		return DelChildItems( hPare );
	}
	else
	{
		return DelChildItems( m_treeParticleSys.GetChildItem(hItem) );
	}
}

void CTestManagerDlg::OnMenuSaveeffectgroup()
{
	//组名做默认文件名
	//文件中不存组名
	if(m_curGroupItem == NULL)
		return;
	std::wstring str = L"";
//	std::vector<std::wstring> EffectGroup;
//	EffectGroup.clear();

	m_PSLgroup.m_vectorPSLs.clear();

	str = m_treeParticleSys.GetItemText(m_curGroupItem);
	str = str +L".pslg"; 

	CString strDefExt = L"pslg";
	std::wstring strFileName = m_strSysPath + L"EffectGroup\\";
	strFileName += str;
	CString strFilter = L"(*.pslg)|*.pslg|";

	CFileDialog dlg(false, strDefExt, strFileName.c_str(), OFN_OVERWRITEPROMPT, strFilter, NULL);

	dlg.DoModal();
	CString filePath = dlg.GetFileName();
	if( filePath.IsEmpty() )
		return;
	filePath = dlg.GetPathName();

	if(m_treeParticleSys.ItemHasChildren(m_curGroupItem))
	{
		HTREEITEM hChildItem = m_treeParticleSys.GetChildItem(m_curGroupItem);

		while(hChildItem != NULL)
		{
			CString strItemName = m_treeParticleSys.GetItemText(hChildItem);
			int pos = strItemName.ReverseFind('-');
			int cnt = strItemName.GetLength();
			str = strItemName.Left(pos-1);

			CString strHandle = strItemName.Right(cnt-pos-2);
			int psHandle = _wtoi(strHandle.GetString());

			Nuclear::PSModifyParam param;

			Nuclear::CParticleSystem* pParticleSys = m_pParSysManager->GetParticleSysByHandle(psHandle);
			if(pParticleSys == NULL)
			{
				MessageBox(L"逻辑错误..CTestManagerDlg::OnMenuSaveeffectgroup()，请告诉相关程序");
				return;
			}

			Nuclear::PSParameter& psParam = pParticleSys->GetPSParameter();

			param = psParam.m_psModifyParam;
			Nuclear::CPOINT pslPos;
			pslPos.x = (int)param.m_pointCent.x - (m_rectDrawParticle.left+m_rectDrawParticle.Width()/2);
			pslPos.y = (int)param.m_pointCent.y - (m_rectDrawParticle.top+m_rectDrawParticle.Height()/2);

			Nuclear::pslgroupobject::PSLGROUP pslg;
			pslg.m_strPslName = str;
			pslg.m_PSLpos = pslPos;
//			EffectGroup.push_back(str);
			m_PSLgroup.m_vectorPSLs.push_back(pslg);

			hChildItem = m_treeParticleSys.GetNextItem(hChildItem, TVGN_NEXT);
		}
	}

	SaveGroup(filePath);

//	SaveGroup(filePath, EffectGroup);
}

bool CTestManagerDlg::SaveGroup(LPCTSTR filePath)
{
	FILE *f = NULL;
	_tfopen_s(&f, filePath, L"wb");
	if( f == NULL ) return FALSE;
	bool ret = m_PSLgroup.Save(f) ? TRUE : FALSE;
	fclose(f);

	return ret;
}

bool CTestManagerDlg::SaveGroup(LPCTSTR filePath, const std::vector<std::wstring>& EffectGroup)
{
	//从pobject派生一个效果对象？
	FILE *file = NULL;
	_tfopen_s(&file, filePath, L"wb");
	if( file==NULL ) return false;

	_ftprintf(file, L"[pslgroup 1.00]\n");//
	int cnt = (int)EffectGroup.size();
	for(int i=0; i<cnt; i++)
	{
		std::wstring str = EffectGroup[i];
		_ftprintf(file, L"%s\n", str.c_str()); // //buff
	}

	fclose(file);
	return true;
}

bool CTestManagerDlg::LoadGroup(LPCTSTR filePath)
{
	FILE *f = NULL;
	_tfopen_s(&f, filePath, L"rb");
	if( f == NULL ) return FALSE;

	m_PSLgroup.m_vectorPSLs.clear();
	bool ret = m_PSLgroup.Load(f) ? TRUE : FALSE;
	fclose(f);

	return ret;
}

bool CTestManagerDlg::LoadGroup(LPCTSTR filePath, std::vector<std::wstring>& EffectGroup)
{
	FILE *file = NULL;
	_tfopen_s(&file, filePath, L"rb");
	if( file==NULL ) return false;

	TCHAR buff[256];
	TCHAR strValue[256];
	fgetws(buff, sizeof(buff), file);
	if( wcsstr(buff, L"[pslgroup 1.00]")==NULL )
	{
		fclose(file);
		return false;
	}

	EffectGroup.clear();
	while( !feof(file) )
	{
		fgetws(buff, sizeof(buff), file);
		if( wcslen(buff) >= 2 )
		{
			Nuclear::PluckFirstField(buff, strValue, sizeof(strValue), L"\n");
			LPCTSTR pszTrimChars = L" ";
			BOOL bRes = StrTrim(strValue, pszTrimChars);
			EffectGroup.push_back(strValue);
		}
	}

	fclose(file);
	return true;
}

void CTestManagerDlg::OnMenuLaodeffectgroup()
{
	DWORD dwFlags = OFN_HIDEREADONLY|OFN_PATHMUSTEXIST;
	CString strDefExt = L"pslg";
	std::wstring strInitFolder = m_strSysPath + L"EffectGroup\\*.pslg";
	CString strFilter = L"(*.pslg)|*.pslg|";

	CFileDialog dlg(true, strDefExt, strInitFolder.c_str(), dwFlags, strFilter, NULL);
	dlg.DoModal();

	CString fileName = dlg.GetFileName();
	if( fileName.IsEmpty() )
		return;
	CString filePath = dlg.GetPathName();

//	std::vector<std::wstring> EffectGroup;
//	LoadGroup(filePath, EffectGroup);
	LoadGroup(filePath);

	int npos = fileName.Find('.');
	fileName = fileName.Left(npos);
	HTREEITEM hItem = m_treeParticleSys.InsertItem(fileName.GetString());
	
//	int cnt = (int)EffectGroup.size();

	int centx = m_rectDrawParticle.left + m_rectDrawParticle.Width()/2;
	int centy = m_rectDrawParticle.top + m_rectDrawParticle.Height()/2;
	
	int cnt = (int)m_PSLgroup.m_vectorPSLs.size();
	for(int i=0; i<cnt; i++)
	{
		Nuclear::PSParameter param;//默认参数初始化...利用当前粒子生成一个ps
		param.cycle = true;
		Nuclear::CPOINT PSLpos = m_PSLgroup.m_vectorPSLs[i].m_PSLpos;
		param.m_psModifyParam.m_pointCent = Nuclear::CPOINT(PSLpos.x+centx, PSLpos.y+centy).ToFPOINT();

		std::wstring pslPathName = m_PSLgroup.m_vectorPSLs[i].m_strPslName;
		std::wstring pslName;
		int npos = pslPathName.rfind('\\');

		pslName = std::wstring(pslPathName, npos+1, pslPathName.size() - npos);

		CString strRootPath = theApp.GetResPath() +L"res\\effect\\particle\\psl\\";

		std::vector<CString> fileFullPathVector; //存放完整路径
		int nres = FindFilesInSpecialPath(strRootPath, pslName.c_str(), fileFullPathVector);
		if(nres != 1)
		{
			CString str = L"\n文件存放在了多个位置...\n";
			if(nres <1)
				str = L"\n文件不存在...\n";
			str = strRootPath+pslName.c_str() +str;
			MessageBox(str);
		}

		CString strTemp = fileFullPathVector[0]; //完整路径...要找出psl下的子目录和文件名
		int pos = strTemp.Find(L"\\psl\\");
		CString strPslPathName = strTemp.Right(strTemp.GetLength() - pos-5);

		Nuclear::ParticleSystemHandle psHandle = m_pParSysManager->CreateParticleSystem(strPslPathName.GetString(), param);

		if(psHandle == Nuclear::INVALID_PS_HANDLE)
		{
			CString str = m_PSLgroup.m_vectorPSLs[i].m_strPslName.c_str();
			str += L"不能被正确读取--可能是使用的texture已经不存在了?\n";
			MessageBox(str);
			continue;
		}

		TCHAR buff[32];
		memset(buff, 0, 32*sizeof(TCHAR));
		_itow_s(psHandle, buff, 6, 10);
		std::wstring strHandle = buff;
		std::wstring ItemName = m_PSLgroup.m_vectorPSLs[i].m_strPslName + L" - ";
		ItemName = ItemName + strHandle;

		m_treeParticleSys.InsertItem(ItemName.c_str(), hItem);
	}
	m_treeParticleSys.Invalidate();
}

int CTestManagerDlg::FindFilesInSpecialPath(const CString& strSysPath, const CString& strFileName, std::vector<CString>& fileFullPathVector)
{//返回找到了几个
	CString strRootPath = strSysPath;
	if (strRootPath.Right(1) != L"\\")
		strRootPath += L"\\";
	CString strPath = strRootPath + L"*.*";

	HANDLE hFind;
	WIN32_FIND_DATA fd;
	int nCount = 0;

	if((hFind = ::FindFirstFile(strPath, &fd)) != INVALID_HANDLE_VALUE)
	{
		if(!(fd.dwFileAttributes & FILE_ATTRIBUTE_HIDDEN))
		{
			if(!(fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY))
			{
				CString FileName = fd.cFileName;
				if(FileName.CompareNoCase(strFileName) == 0)
					fileFullPathVector.push_back(strRootPath +strFileName);
			}
			else
			{//目录
				CString strDir = (LPCTSTR) &fd.cFileName;
				if((strDir != L".") && (strDir != L".."))
					FindFilesInSpecialPath(strRootPath +strDir, strFileName, fileFullPathVector);
			}
		}

		while(::FindNextFile(hFind, &fd)) 
		{
			if(!(fd.dwFileAttributes & FILE_ATTRIBUTE_HIDDEN))
			{
				if(!(fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY))
				{
					CString FileName = fd.cFileName;
					if(FileName.CompareNoCase(strFileName) == 0)
						fileFullPathVector.push_back(strRootPath +strFileName);
				}
				else
				{
					//目录
					CString strDir = (LPCTSTR) &fd.cFileName;
					if((strDir != L".") && (strDir != L".."))
						FindFilesInSpecialPath(strRootPath +strDir, strFileName, fileFullPathVector);
				}
			}
		}
	}
	::FindClose(hFind);

	return (int)fileFullPathVector.size();
}

void CTestManagerDlg::OnBnClickedCheckMovebkmap()
{
	UpdateData(true);
	if(m_bMoveBKMap)
	{
		m_bMoveEffect = false;
		m_bLineEmiterDemo = false;
		UpdateData(false);
	}
}

void CTestManagerDlg::OnBnClickedCheckMoveeffect()
{
	UpdateData(true);
	if(m_bMoveEffect)
	{
		m_bMoveBKMap = false;
		m_bLineEmiterDemo = false;
		UpdateData(false);
	}

	m_pParSysManager->GarbageCollect();
}

void CTestManagerDlg::OnBnClickedCheckLineemiterdemo()
{
	UpdateData(true);
	if(m_bLineEmiterDemo)
	{
		m_bMoveBKMap = false;
		m_bMoveEffect = false;
		UpdateData(false);
	}
	
	// TODO: Add your control notification handler code here
}

void CTestManagerDlg::OnBnClickedButtonSetbkmap()
{
	// TODO: 在此添加控件通知处理程序代码
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

	if(m_pRenderer != NULL)
	{
		//释放以前的picHandle ,创建新的picHandle m_pRender
		m_pRenderer->FreePicture(m_hBKpicHandle);
		m_hBKpicHandle = m_pRenderer->LoadPictureFromNativePath(std::wstring(filePath));

		Nuclear::PictureInfo picinfo;
		bool bres = m_pRenderer->GetPictureInfo(m_hBKpicHandle, picinfo);
		float left = 0.0f;
		float top = 0.0f;
		float right = left + picinfo.m_nPicWidth;
		float bottom = top + picinfo.m_nPicHeight;
		m_FrctMapPos = Nuclear::FRECT(left, top, right, bottom);
	}

	wchar_t workdir[MAX_PATH];
	::GetCurrentDirectory(MAX_PATH, workdir);
	CString iniFile = workdir;
	iniFile += L"\\ParticleSystemEditor.ini";

	CString strSection = L"ALL";
	CString strSectionKey = L"BKpicture";
	CString strValue = filePath;
	WritePrivateProfileString(strSection, strSectionKey, strValue, iniFile);

	m_bBKchange = true;
}

CImageList* CTestManagerDlg::CreateDragImageEx(HTREEITEM hItem) 
{ 
	if(m_treeParticleSys.GetImageList(TVSIL_NORMAL) != NULL) 
		return m_treeParticleSys.CreateDragImage(hItem); 

	CRect rect; 
	m_treeParticleSys.GetItemRect(hItem, rect, TRUE); 
	rect.top = rect.left = 0; 

	// Create bitmap 
	CClientDC dc(this); 
	CDC memDC; 

	if(!memDC.CreateCompatibleDC(&dc)) 
		return NULL; 

	CBitmap bitmap; 
	if(!bitmap.CreateCompatibleBitmap(&dc, rect.Width(), rect.Height())) 
		return NULL; 

	CBitmap* pOldMemDCBitmap = memDC.SelectObject( &bitmap ); 
	CFont* pOldFont = memDC.SelectObject(GetFont()); 

	memDC.FillSolidRect(&rect, RGB(0, 255, 0)); // Here green is used as mask color 
	memDC.SetTextColor(GetSysColor(COLOR_WINDOWTEXT)); 
	memDC.TextOut(rect.left, rect.top, m_treeParticleSys.GetItemText(hItem)); 

	memDC.SelectObject( pOldFont ); 
	memDC.SelectObject( pOldMemDCBitmap ); 

	// Create imagelist 
	CImageList* pImageList = new CImageList; 
	pImageList->Create(rect.Width(), rect.Height(), 
		ILC_COLOR | ILC_MASK, 0, 1); 
	pImageList->Add(&bitmap, RGB(0, 255, 0)); // Here green is used as mask color 

	return pImageList;
}

void CTestManagerDlg::OnTvnBegindragTreeParticlesyslist(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTREEVIEW pNMTreeView = reinterpret_cast<LPNMTREEVIEW>(pNMHDR);
	// TODO: 在此添加控件通知处理程序代码

	//拖曳图片
	m_hItemDrag = pNMTreeView->itemNew.hItem;

	HTREEITEM hParent = m_treeParticleSys.GetParentItem(m_hItemDrag);
	if( (m_treeParticleSys.ItemHasChildren(m_hItemDrag)) || (hParent == NULL) )
	{
		m_hItemDrag = NULL;
		MessageBox(L"根节点不允许拖动");
		*pResult = 0;
		return;
	}

	CString strItem = m_treeParticleSys.GetItemText(m_hItemDrag);
//	m_pDragImage = m_treeParticleSys.CreateDragImage(m_hItemDrag);
	m_pDragImage = CreateDragImageEx(m_hItemDrag);
	XPASSERT (m_pDragImage != NULL);

	if(m_pDragImage != NULL)
	{
		CRect rect;
		m_treeParticleSys.GetItemRect(m_hItemDrag, rect, TRUE);
		POINT pt;
		pt.x = pNMTreeView->ptDrag.x - rect.left -16; 
		pt.y = pNMTreeView->ptDrag.y - rect.top -48;

		ClientToScreen(&pt);

		m_pDragImage->BeginDrag(0, pt);
		m_pDragImage->DragEnter(this, pt);

 		m_bDragging = true;
 		m_hItemDrop = NULL;

		SetCapture ();
	}

	*pResult = 0;
}


void CTestManagerDlg::OnLButtonUp(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
	if(m_bDragging)
	{
		m_bDragging = false;
		ReleaseCapture();
		m_pDragImage->DragLeave(this);
		m_pDragImage->EndDrag();

		if(m_pDragImage != NULL)
		{
			delete m_pDragImage;
			m_pDragImage = NULL;
		}

		//插入到新位置..
		//
		if(m_hItemDrop != NULL)
		{
			HTREEITEM hitem = NULL;
			CString str = m_treeParticleSys.GetItemText(m_hItemDrag);
			HTREEITEM hParent = m_treeParticleSys.GetParentItem(m_hItemDrop);
			if( (m_treeParticleSys.ItemHasChildren(m_hItemDrop)) || (hParent == NULL) )
			{
				hParent = m_hItemDrop;
				hitem = m_treeParticleSys.InsertItem(str, hParent, TVI_FIRST); 
			}
			else
			{
				hitem = m_treeParticleSys.InsertItem(str, hParent, m_hItemDrop);
			}
			BOOL bCheck = m_treeParticleSys.GetCheck(m_hItemDrag);
			m_treeParticleSys.SetCheck(hitem, bCheck);

			m_treeParticleSys.DeleteItem(m_hItemDrag);
		}
		//

		m_treeParticleSys.SelectItem(m_hItemDrop); 
		m_curSelItem = m_hItemDrop;
	}

	CDialog::OnLButtonUp(nFlags, point);
}

void CTestManagerDlg::OnMenupslproperty()
{
	CString strItemName = m_treeParticleSys.GetItemText(m_curSelItem);
	int pos = strItemName.ReverseFind('-');
	int cnt = strItemName.GetLength();
	CString strHandle = strItemName.Right(cnt-pos-2);
	int psHandle = _wtoi(strHandle.GetString());

	Nuclear::PSINFO psInfo = m_pParSysManager->GetParticleSystemInfo(psHandle);
	Nuclear::CRECT rct = psInfo.m_rctMaxBox;
	Nuclear::CPOINT pt = psInfo.m_pointCent;

	CString str = strItemName.Left(pos);;
	str += L"\n最大包围盒(左上、右下):";
	CString str1;
	str1.Format(L"(%d, %d)、(%d, %d)\n", rct.left, rct.top, rct.right, rct.bottom);
	str += str1;
	str += L"发射器中心点:";
	str1.Format(L"(%d, %d)\n", pt.x, pt.y);
	str += str1;
	str += L"效果类型: ";
	str1.Format(L"%d\n", psInfo.m_moveType);
	str += str1;
	str += L"粒子形状类型: ";
	str1.Format(L"%d\n", psInfo.m_emisType);
	str += str1;
	str += L"是否循环: ";
	str1.Format(L"%d", psInfo.m_bCycle);
	str += str1;
	AfxMessageBox(str, MB_ICONASTERISK);
}

void CTestManagerDlg::OnMenuPsreplay()
{
	HTREEITEM hItem = NULL;
	hItem = m_treeParticleSys.GetSelectedItem();
	HTREEITEM hParent = m_treeParticleSys.GetParentItem(hItem);

	if(m_treeParticleSys.ItemHasChildren(hItem))
	{
		m_curGroupItem = hItem;
		m_curSelItem = NULL;
	}
	else if(hParent != NULL)
	{
		m_curSelItem = hItem;
		m_curGroupItem = NULL;
	}
	else
	{
	}

	if(m_curSelItem != NULL)
	{
		CString strItemName = m_treeParticleSys.GetItemText(m_curSelItem);
		int pos = strItemName.ReverseFind('-');
		int cnt = strItemName.GetLength();
		CString strHandle = strItemName.Right(cnt-pos-2);
		int psHandle = _wtoi(strHandle.GetString());
		m_pParSysManager->ResetParticleSystem(psHandle);
	}
	else if(m_curGroupItem != NULL)
	{
		HTREEITEM hChildItem = m_treeParticleSys.GetChildItem(m_curGroupItem);

		while(hChildItem != NULL)
		{
			BOOL bCheck = m_treeParticleSys.GetCheck(hChildItem);
			if(bCheck)
			{
				CString strItemName = m_treeParticleSys.GetItemText(hChildItem);
				int pos = strItemName.ReverseFind('-');
				int cnt = strItemName.GetLength();
				CString strHandle = strItemName.Right(cnt-pos-2);
				int psHandle = _wtoi(strHandle.GetString());

				m_pParSysManager->ResetParticleSystem(psHandle);
			}
			hChildItem = m_treeParticleSys.GetNextItem(hChildItem, TVGN_NEXT);
		}
	}
	else
	{
	//	MessageBox(L"报错..逻辑错误...CTestManagerDlg::OnMenuPsreplay()\n");
		MessageBox(L"没有选中可以播放的特效?");
	}
}

void CTestManagerDlg::OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值
//  	switch(nChar)
//  	{
//  	case 'P':
//  		OnMenuPsreplay();
//  		break;
//  	default:
//  	    break;
//  	}

	CDialog::OnKeyDown(nChar, nRepCnt, nFlags);
}

void CTestManagerDlg::OnTvnKeydownTreeParticlesyslist(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTVKEYDOWN pTVKeyDown = reinterpret_cast<LPNMTVKEYDOWN>(pNMHDR);
	// TODO: 在此添加控件通知处理程序代码

	switch(pTVKeyDown->wVKey)
	{
// 	case VK_DOWN:
// 	case VK_UP:
// 	case VK_LEFT:
// 	case VK_RIGHT:
// 		break;
	case 'P':
	//	OnKeyDown(pTVKeyDown->wVKey, 1, pTVKeyDown->flags);
		OnMenuPsreplay();
		break;

	default:
	    break;
	}

	*pResult = 1; // =0 结果不过.. 2009-04-07
}

void CTestManagerDlg::OnClose()
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	CString strInfo = L"确定要关闭？";
	int res = MessageBox(strInfo, L"提示", MB_ICONQUESTION|MB_YESNO);
	if(res == IDNO)
		return;
	else
	{// res == IDYES 继续后面的处理...
	}
	CDialog::OnClose();
}

void CTestManagerDlg::OnBnClickedButtonPslgfileverconversion()
{
	CString strDefExt = L"pslg";
	CString strFilter = L"(*.pslg)|*.pslg||";

	std::wstring strInitFolder = L"";

	DWORD dwFlags = OFN_HIDEREADONLY|OFN_PATHMUSTEXIST;
	CFileDialog dlg(true, strDefExt, NULL, dwFlags, strFilter, NULL);
	dlg.DoModal();
	CString filePath = dlg.GetFileName();
	if( filePath.IsEmpty() )
		return;
	filePath = dlg.GetPathName();
	int nPos = filePath.ReverseFind('\\');
	CString strPath = filePath.Left(nPos); //指定文件所在目录...

	std::vector<CString> VectorPSLGFileName; //指定目录下的所有pslg文件

	if (strPath.Right (1) != L"\\")
		strPath += L"\\";
	CString filePathNoName = strPath;
	strPath += L"*.pslg";

	HANDLE hFind;
	WIN32_FIND_DATA fd;

	if((hFind = ::FindFirstFile(strPath, &fd)) != INVALID_HANDLE_VALUE)
	{
		VectorPSLGFileName.clear();

		if(!(fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY))
		{
			CString str = filePathNoName + fd.cFileName;
			VectorPSLGFileName.push_back(str);
		}

		while (::FindNextFile (hFind, &fd)) 
		{
			if (!(fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY))
			{
				CString str = filePathNoName + fd.cFileName;
				VectorPSLGFileName.push_back(str);
			}
		}
	}
	::FindClose(hFind);

	ConversionPSLGFile(VectorPSLGFileName);
}

void CTestManagerDlg::ConversionPSLGFile(std::vector<CString> &VectorPSLGFileName)
{
	int nCount = 0; //统计转换了多少个文件...

	int cnt = (int)VectorPSLGFileName.size();
	for(int i=0; i<cnt; i++)
	{
		CString filePath = VectorPSLGFileName[i];
	//	int npos = filePath.ReverseFind('.');
	//	CString fileName = filePath.Left(npos);
	
		std::vector<std::wstring> EffectGroup;
		bool bflag = LoadGroup(filePath, EffectGroup);
		if(!bflag)
		{
			MessageBox(L"一个转换失败---可能是该文件不是能辨识的有效旧版本文件");
			continue;
		}

		m_PSLgroup.m_vectorPSLs.clear();
		int num = (int)EffectGroup.size();
		for(int j=0; j<num; j++)
		{
			Nuclear::pslgroupobject::PSLGROUP pslg;
			pslg.m_strPslName = EffectGroup[j];
			pslg.m_PSLpos = Nuclear::CPOINT();
			m_PSLgroup.m_vectorPSLs.push_back(pslg);
		}

		bflag = SaveGroup(filePath);

		nCount++; 
	}

	CString str;
	str.Format(L"%d", nCount);
	str += L" 个旧版本pslg文件被转为新版本pslg文件并存盘，老版本文件被删除";
	MessageBox(str, L"提示"), MB_ICONINFORMATION;
}

void CTestManagerDlg::OnCbnSelchangeComboComputerconfig()
{
	m_nComputerConfig = m_comboComputerConfig.GetCurSel();
	m_pParSysManager->SetSystemConfig(Nuclear::XPCOMPUTERCONFIGTYPE(2-m_nComputerConfig));
}

void CTestManagerDlg::OnBnClickedRadioLow()
{
	UpdateData(true);
	SetParticleSysPriorityParam();
	//	m_nLowMiddleHigh;
}

void CTestManagerDlg::OnBnClickedRadioMiddle()
{
	UpdateData(true);
	SetParticleSysPriorityParam();
	//	m_nLowMiddleHigh;
}

void CTestManagerDlg::OnBnClickedRadioHigh()
{
	UpdateData(true);
	SetParticleSysPriorityParam();
	//	m_nLowMiddleHigh;
}

void CTestManagerDlg::OnTvnSelchangedTreeParticlesyslist(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTREEVIEW pNMTreeView = reinterpret_cast<LPNMTREEVIEW>(pNMHDR);
	// TODO: Add your control notification handler code here

	HTREEITEM hItem = pNMTreeView->itemNew.hItem;
// 
 	HTREEITEM hParent = m_treeParticleSys.GetParentItem(hItem);
 	if( (m_treeParticleSys.ItemHasChildren(hItem)) || (hParent == NULL) )
 	{
		//L"根节点----"
 		*pResult = 0;
 		return;
 	}

	m_curSelItem = hItem;
	InitCtrlPriorityParam();

	*pResult = 0;
}

void CTestManagerDlg::OnBnClickedSaveConfigparam()
{
	// TODO: Add your control notification handler code here
	//不能修改界面特效。。。

	if(m_curSelItem != NULL)
	{
		CString strItemName = m_treeParticleSys.GetItemText(m_curSelItem);
		int pos = strItemName.ReverseFind('-');
		CString strPslName = strItemName.Left(pos-1);

		int cnt = strItemName.GetLength();
		CString strHandle = strItemName.Right(cnt-pos-2);
		int psHandle = _wtoi(strHandle.GetString());


		Nuclear::CParticleSystem* pParticleSys = m_pParSysManager->GetParticleSysByHandle(psHandle);
		if(pParticleSys == NULL)
		{
			MessageBox(L"逻辑错误..CTestManagerDlg::OnMenuModifyparticlesys()，请告诉相关程序");
			return;
		}

		if(pParticleSys->GetPSL()->m_nInterfacePsl == 1)
		{
			//pParticleSys->GetPSL()->m_ptInterfacialCenter = Nuclear::FPOINT(dx, dy);
			//如果是界面特效，要计算偏移坐标
			MessageBox(L"界面特效，不能在这里做参数的修改保存...");
			return;
		}

		CString filePath = L""; 
		GetModuleFileName(NULL, filePath.GetBufferSetLength(MAX_PATH+1), MAX_PATH);
		filePath.ReleaseBuffer();
		int npos = filePath.ReverseFind('\\');
		filePath = filePath.Left(npos); 

		if(filePath.Right(1) != L"\\")
			filePath += L"\\";
		
		CString strSysPath = filePath + L"res\\effect\\particle\\psl\\";
		CString pslPath = strSysPath + strPslName;
		pParticleSys->SavePSL(pslPath.GetString());
	}
}
