// MainDlg.cpp : implementation file
//

#include "stdafx.h"
#include "SoundEditor.h"
#include "MainDlg.h"
#include "SoundEditorView.h"
#include "SoundEditorDoc.h"

// CMainDlg dialog

IMPLEMENT_DYNAMIC(CMainDlg, CDialog)

CMainDlg::CMainDlg(CWnd* pParent /*=NULL*/)
: CDialog(CMainDlg::IDD, pParent), m_pDoc(NULL), m_pEngine(NULL), m_pWorld(NULL), m_bIsLoadedMap(false),
m_toolState(TS_MOVE), m_mouseState(NORMAL)
{
	m_bAllScreenBrush = false;

	wchar_t workdir[MAX_PATH];
	::GetCurrentDirectory(MAX_PATH, workdir);
	CString stringPath = workdir;
	m_EdgeSize = ::GetPrivateProfileInt(L"RegionParam", L"edgeSize", 10, stringPath + L"\\MazeAndNpcEditor.ini");
}

CMainDlg::~CMainDlg()
{
	m_pWorld = NULL;
	m_pEngine->Exit();
}

void CMainDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}


BEGIN_MESSAGE_MAP(CMainDlg, CDialog)
	ON_WM_TIMER()
	ON_WM_RBUTTONDOWN()
	ON_WM_RBUTTONUP()
	ON_WM_MOUSEMOVE()
	ON_WM_LBUTTONDOWN()
	ON_COMMAND_RANGE(ID_TOOL_MOVE, ID_GAOJISHUAGUAI, &CMainDlg::OnTool)
	ON_UPDATE_COMMAND_UI_RANGE(ID_TOOL_MOVE, ID_GAOJISHUAGUAI, &CMainDlg::OnUpdateTool)
	ON_WM_LBUTTONUP()
	ON_WM_KEYUP()
	ON_COMMAND(ID_SAVEMAZE, &CMainDlg::OnSavemaze)
	ON_COMMAND(ID_32786, &CMainDlg::On32786)
	ON_COMMAND(ID_ALLSCREEN_BRUSH, &CMainDlg::OnAllScreenBrush)
	ON_UPDATE_COMMAND_UI(ID_ALLSCREEN_BRUSH, &CMainDlg::OnUpdateAllScreenBrush)
END_MESSAGE_MAP()


// CMainDlg message handlers

BOOL CMainDlg::OnInitDialog()
{
	CDialog::OnInitDialog();
	m_pDoc = static_cast<CSoundEditorView*>(GetParent())->GetDocument();
	CRect rect(0, 0, 1024, 768);
	MoveWindow(&rect);

	Nuclear::EngineParameter ep;
	ep.hWnd = m_hWnd;
	ep.bApplictionInBuild = true;
	ep.bAsyncRead = true;
	ep.dmode.width = 1024;
	ep.dmode.height = 768;
	ep.pApp = &m_soundApp;
	m_pEngine = Nuclear::GetEngine();
	m_pEngine->Run(ep);

	while(!m_soundApp.isInited())
	{
		m_pEngine->OnIdle();
	}

	m_pEngine->OnWindowsMessage(m_hWnd, WM_SIZE, SIZE_RESTORED, 0);
	m_pWorld = m_pEngine->GetWorld();
	m_soundApp.SetDoc(m_pDoc);

	SetTimer(1000, 25, NULL);

	//m_dlgNpcDlg.Create(m_dlgNpcDlg.IDD,this);

	/*m_dlgNpcDlg.SetWindowPos(NULL,10,800,0,0,SWP_NOSIZE);
	m_dlgNpcDlg.ShowWindow(SW_SHOW);
	m_dlgNpcDlg.BringWindowToTop();*/

	return TRUE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

void CMainDlg::OnTimer(UINT_PTR nIDEvent)
{
	if (m_pEngine && (nIDEvent == 1000))
	{
		m_pEngine->OnIdle();
	}

	CDialog::OnTimer(nIDEvent);
}

LRESULT CMainDlg::WindowProc(UINT message, WPARAM wParam, LPARAM lParam)
{
	if (m_pEngine)
	{
		m_pEngine->OnWindowsMessage(m_hWnd, message, wParam, lParam);
	}
	return CDialog::WindowProc(message, wParam, lParam);
}

bool CMainDlg::UnloadMap()
{
	if (!m_pEngine)
		return false;
	m_bIsLoadedMap = false;
	m_soundApp.Reset();
	return m_pWorld->UnloadMap();
}

bool CMainDlg::LoadMap(const CString& name)
{
	if (UnloadMap())
	{
		Nuclear::XPLoadmapParam param;
		param.objLoc.x = 0;
		param.objLoc.y = 0;

		std::wstring sysFilePath = _T("");

		std::wstring mapname		= sysFilePath + L"/map/"	+	name.GetString();
		std::wstring mazename		= sysFilePath + L"/map/"	+	name.GetString() + L"/maze.dat";
		std::wstring NpcXmlName		= sysFilePath + L"/map/"	+	name.GetString() + L"/npc.xml";
		std::wstring gotoXmlName	= sysFilePath + L"/map/"	+	name.GetString() + L"/goto.dat";

		DWORD	dwAttribute = ::GetFileAttributesW( NpcXmlName.c_str());
		if( dwAttribute == INVALID_FILE_ATTRIBUTES )
		{
			NpcXmlName = sysFilePath + L"/map/"	+	name.GetString() + L"/npc.dat";
		}

		if (m_pWorld->LoadMap(mapname, mazename, &param))
		{
			m_oldViewportPos.x = 0;
			m_oldViewportPos.y = 0;
			m_pWorld->SetViewportLT(0, 0);
			Nuclear::CPOINT size;
			m_pWorld->GetMapSize(size);
			m_pDoc->SetMapSize(size);
			m_pDoc->m_nUpdateViewSign = UVS_ALL;
			m_pDoc->UpdateAllViews(NULL);
			m_pDoc->m_nUpdateViewSign = 0;
			m_bIsLoadedMap = true;
			m_soundApp.ReadMazeBuffer(m_mazefilename);
			m_soundApp.ResetMapNpc(NpcXmlName);
			m_soundApp.LoadJumpPoint(gotoXmlName);
			m_soundApp.ReadRegionBufferFromFile(m_RegionTypeInfoFileName);
			m_soundApp.LoadJumpBlockInf(m_JumpBlockFileName);
			//2015
			m_soundApp.ReadMonsterKind(m_MonsterFileName);
			return true;
		}
	}
	return false;
}

void CMainDlg::OnUpdate()
{
	if (m_pDoc)
	{
		if (m_pDoc->m_nUpdateViewSign & UVS_TAB)
		{
			m_soundApp.SetState(m_pDoc->GetEditState());
		}
	}
}

void CMainDlg::SetOldViewportPoint()
{
	Nuclear::IWorld *pWorld = m_pEngine->GetWorld();
	if (pWorld)
	{
		Nuclear::CRECT rect = pWorld->GetViewport();
		m_oldViewportPos.x = rect.left;
		m_oldViewportPos.y = rect.top;
	}
}

void CMainDlg::MoveViewPort(int dx, int dy)
{
	if (m_pWorld)
	{
		m_pWorld->SetViewportLT(dx + m_oldViewportPos.x, dy + m_oldViewportPos.y);
		m_pEngine->OnIdle();
	}
}

void CMainDlg::OnRButtonDown(UINT nFlags, CPoint point)
{
	if (!m_bIsLoadedMap)
	{
		AfxMessageBox(L"请先打开一张地图");
		CDialog::OnRButtonDown(nFlags, point);
		return;
	}
	SetCapture();
	m_mouseState |= RIGHT_BUTTON_DOWN;
	if (m_pDoc && m_pEngine)
	{
		Client2World(point, m_pWorld->GetViewport(), m_oldMousePoint);
		switch (m_pDoc->GetEditState())
		{
		case EDIT_STATE_MAZE:
		case EDIT_STATE_NPC:
			SetOldViewportPoint();
			break;
		case EDIT_STATE_TESTING:
			break;
		case EDIT_STATE_MapJump:
			{
				m_soundApp.ShowJumpInf(m_oldMousePoint);
			}
			break;
		}
	}

	CDialog::OnRButtonDown(nFlags, point);
}

void CMainDlg::OnRButtonUp(UINT nFlags, CPoint point)
{
	if (!m_bIsLoadedMap)
	{
		CDialog::OnRButtonDown(nFlags, point);
		return;
	}
	m_mouseState &= ~RIGHT_BUTTON_DOWN;
	if (m_mouseState == NORMAL)
	{
		ReleaseCapture();
	}

	CDialog::OnRButtonUp(nFlags, point);
}

void CMainDlg::OnMouseMove(UINT nFlags, CPoint point)
{
	if (!m_bIsLoadedMap)
	{
		CDialog::OnMouseMove(nFlags, point);
		return;
	}
	if (m_pEngine && m_pDoc)
	{
		if (m_pDoc->GetEditState() != EDIT_STATE_TESTING)
		{
			if ((m_toolState == TS_MOVE && (m_mouseState & LEFT_BUTTON_DOWN)) || 
				(m_mouseState & RIGHT_BUTTON_DOWN) 
				&& ::GetKeyState(VK_LSHIFT) < 0)
			{
				Nuclear::CPOINT pt;
				Nuclear::CRECT rect(m_oldViewportPos, 1024, 768);
				Client2World(point, rect, pt);
				MoveViewPort(m_oldMousePoint.x - pt.x, m_oldMousePoint.y - pt.y);
			} 
			else if (m_mouseState & LEFT_BUTTON_DOWN) 
			{
				Nuclear::CPOINT absPt;
				Client2World(point, m_pWorld->GetViewport(), absPt);
				switch (m_pDoc->GetEditState())
				{
				case EDIT_STATE_MAZE:
					switch (m_toolState)
					{
					case TS_SETBLOCK:
						{
							if (::GetKeyState(VK_LCONTROL) < 0)
							{
								m_soundApp.SetBlockPoint(absPt,m_bAllScreenBrush,false,true);
							}
							else
							{
								m_soundApp.SetBlockPoint(absPt,m_bAllScreenBrush,false,false);
							}
						}
						break;
					case TS_SETBLOCK2:
						{
							if (::GetKeyState(VK_LCONTROL) < 0)
							{
								m_soundApp.SetBlockPoint(absPt,m_bAllScreenBrush,false,true,s_BlockLayer2Mask);
							}
							else
							{
								m_soundApp.SetBlockPoint(absPt,m_bAllScreenBrush,false,false,s_BlockLayer2Mask);
							}
						}
						break;
					case TS_PUTNPC:
						break;
					case TS_SELECT:
						break;
						//设置各种阻挡
					case  TS_QIEZUO:	//设置切磋点
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.SetRegionBig(tmpPt.x,tmpPt.y,RY_QIEZUO);
							else
								m_soundApp.SetRegion(tmpPt.x,tmpPt.y,RY_QIEZUO);
						}
						break;

						//设置鼠标点击效果点
					case TS_DUST:
					case TS_GRASS:
					case TS_WATER:
						{
							unsigned char mask = 0x00;
							//取高两位，高两位为1代表土，为2代表草，为3代表水
							if (m_toolState == TS_DUST)
								mask = 0x01<<6;
							else if (m_toolState == TS_GRASS)
								mask = 0x02<<6;
							else
								mask = 0x03<<6;
							if (::GetKeyState(VK_LCONTROL) < 0)
							{
								m_soundApp.SetBlockPoint(absPt,false,true,mask);
							}
							else
								m_soundApp.SetBlockPoint(absPt,false,false,mask);
						}
						break;
					case TS_BOSA:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.SetRegionBig(tmpPt.x,tmpPt.y,RY_BOSA);
							else
								m_soundApp.SetRegion(tmpPt.x,tmpPt.y,RY_BOSA);
						}
						break;
					case TS_BAITAN:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.SetRegionBig(tmpPt.x,tmpPt.y,RY_BAITAN);
							else
								m_soundApp.SetRegion(tmpPt.x,tmpPt.y,RY_BAITAN);
						}
						break;
					case TS_KITE:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.SetRegionBig(tmpPt.x,tmpPt.y,RY_KITE);
							else
								m_soundApp.SetRegion(tmpPt.x,tmpPt.y,RY_KITE);
						}
						break;
					case TS_SHUAGUAI:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.SetRegionBig(tmpPt.x,tmpPt.y,RY_SHUAGUAI);
							else
								m_soundApp.SetRegion(tmpPt.x,tmpPt.y,RY_SHUAGUAI);
						}
						break;
					case TS_MINGSHENG:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.SetRegionBig(tmpPt.x,tmpPt.y,RY_MINGSHENG);
							else
								m_soundApp.SetRegion(tmpPt.x,tmpPt.y,RY_MINGSHENG);
						}
						break;
					case TS_GAOJISHUGUAI:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.SetRegionBig(tmpPt.x,tmpPt.y,RY_GAOJISHUGUAI);
							else
								m_soundApp.SetRegion(tmpPt.x,tmpPt.y,RY_GAOJISHUGUAI);
						}
						break;
					case TS_QINHGONG1:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.SetJumpBlock(absPt,true,(unsigned char)QG_1);
							else
								m_soundApp.SetJumpBlock(absPt,false,(unsigned char)QG_1);
						}
						break;
					case TS_QINHGONG2:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.SetJumpBlock(absPt,true,(unsigned char)QG_2);
							else
								m_soundApp.SetJumpBlock(absPt,false,(unsigned char)QG_2);
						}
						break;
					case TS_QINHGONG3:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.SetJumpBlock(absPt,true,(unsigned char)QG_3);
							else
								m_soundApp.SetJumpBlock(absPt,false,(unsigned char)QG_3);
						}
						break;
					case TS_QINHGONG4:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.SetJumpBlock(absPt,true,(unsigned char)QG_4);
							else
								m_soundApp.SetJumpBlock(absPt,false,(unsigned char)QG_4);
						}
						break;
					case TS_AREA11:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.SetRegionBig(tmpPt.x,tmpPt.y,RY_AREA11);
							else
								m_soundApp.SetRegion(tmpPt.x,tmpPt.y,RY_AREA11);
						}
						break;
					case TS_AREA12:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.SetRegionBig(tmpPt.x,tmpPt.y,RY_AREA12);
							else
								m_soundApp.SetRegion(tmpPt.x,tmpPt.y,RY_AREA12);
						}
						break;
					case TS_AREA13:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.SetRegionBig(tmpPt.x,tmpPt.y,RY_AREA13);
							else
								m_soundApp.SetRegion(tmpPt.x,tmpPt.y,RY_AREA13);
						}
						break;
					case TS_AREA14:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.SetRegionBig(tmpPt.x,tmpPt.y,RY_AREA14);
							else
								m_soundApp.SetRegion(tmpPt.x,tmpPt.y,RY_AREA14);
						}
						break;
					case TS_AREA15:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.SetRegionBig(tmpPt.x,tmpPt.y,RY_AREA15);
							else
								m_soundApp.SetRegion(tmpPt.x,tmpPt.y,RY_AREA15);
						}
						break;
					case TS_AREA16:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.SetRegionBig(tmpPt.x,tmpPt.y,RY_AREA16);
							else
								m_soundApp.SetRegion(tmpPt.x,tmpPt.y,RY_AREA16);
						}
						break;
					

					}
					break;

				case EDIT_STATE_NPC:
					{


					}
					break;
				}
			}
			else if (m_mouseState & RIGHT_BUTTON_DOWN)
			{
				Nuclear::CPOINT absPt;
				Client2World(point, m_pWorld->GetViewport(), absPt);
				switch (m_pDoc->GetEditState())
				{
				case EDIT_STATE_MAZE:
					switch (m_toolState)
					{
					case TS_SETBLOCK:
						{
							if (::GetKeyState(VK_LCONTROL) < 0)
							{
								m_soundApp.ClearMazeMask(0x3,absPt,m_EdgeSize,m_bAllScreenBrush);
							}
							else
							{
								m_soundApp.ClearMazeMask(0x3,absPt,m_bAllScreenBrush);
							}
						}
						break;
					case TS_SETBLOCK2:
						{
							if (::GetKeyState(VK_LCONTROL) < 0)
							{
								m_soundApp.ClearMazeMask(s_BlockLayer2Mask,absPt,m_EdgeSize,m_bAllScreenBrush);
							}
							else
							{
								m_soundApp.ClearMazeMask(s_BlockLayer2Mask,absPt,m_bAllScreenBrush);
							}
						}
						break;
					case TS_PUTNPC:
						break;
					case TS_SELECT:
						break;
						//设置各种阻挡
					case  TS_QIEZUO:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.DelRegionBig(tmpPt.x,tmpPt.y,RY_QIEZUO);
							else
								m_soundApp.DelRegion(tmpPt.x,tmpPt.y,RY_QIEZUO);
						}
						break;
					case TS_DUST:
					case TS_GRASS:
					case TS_WATER:
						{
							if (::GetKeyState(VK_LCONTROL) < 0)
							{
								m_soundApp.ClearMazeMask(0xC0,absPt,m_EdgeSize,m_bAllScreenBrush);
							}
							else
							{
								m_soundApp.ClearMazeMask(0xC0,absPt,m_bAllScreenBrush);
							}
						}
						break;
					case TS_BOSA:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.DelRegionBig(tmpPt.x,tmpPt.y,RY_BOSA);
							else
								m_soundApp.DelRegion(tmpPt.x,tmpPt.y,RY_BOSA);
						}
						break;
					case TS_BAITAN:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.DelRegionBig(tmpPt.x,tmpPt.y,RY_BAITAN);
							else
								m_soundApp.DelRegion(tmpPt.x,tmpPt.y,RY_BAITAN);
						}
						break;
					case TS_KITE:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.DelRegionBig(tmpPt.x,tmpPt.y,RY_KITE);
							else
								m_soundApp.DelRegion(tmpPt.x,tmpPt.y,RY_KITE);
						}
						break;

					case TS_SHUAGUAI:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
							{
								m_soundApp.DelRegionBig(tmpPt.x,tmpPt.y,RY_SHUAGUAI);
							}
							else
							{
								m_soundApp.DelRegion(tmpPt.x,tmpPt.y,RY_SHUAGUAI);
								m_soundApp.DelMonsterPoint(tmpPt.x,tmpPt.y);
							}
						}
						break;
					case TS_MINGSHENG:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.DelRegionBig(tmpPt.x,tmpPt.y,RY_MINGSHENG);
							else
								m_soundApp.DelRegion(tmpPt.x,tmpPt.y,RY_MINGSHENG);
						}
						break;
					case TS_GAOJISHUGUAI:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.DelRegionBig(tmpPt.x,tmpPt.y,RY_GAOJISHUGUAI);
							else
								m_soundApp.DelRegion(tmpPt.x,tmpPt.y,RY_GAOJISHUGUAI);
						}
						break;
					case TS_AREA11:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.DelRegionBig(tmpPt.x,tmpPt.y,RY_AREA11);
							else
								m_soundApp.DelRegion(tmpPt.x,tmpPt.y,RY_AREA11);
						}
						break;
					case TS_AREA12:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.DelRegionBig(tmpPt.x,tmpPt.y,RY_AREA12);
							else
								m_soundApp.DelRegion(tmpPt.x,tmpPt.y,RY_AREA12);
						}
						break;
					case TS_AREA13:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.DelRegionBig(tmpPt.x,tmpPt.y,RY_AREA13);
							else
								m_soundApp.DelRegion(tmpPt.x,tmpPt.y,RY_AREA13);
						}
						break;
					case TS_AREA14:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.DelRegionBig(tmpPt.x,tmpPt.y,RY_AREA14);
							else
								m_soundApp.DelRegion(tmpPt.x,tmpPt.y,RY_AREA14);
						}
						break;
					
					case TS_AREA15:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.DelRegionBig(tmpPt.x,tmpPt.y,RY_AREA15);
							else
								m_soundApp.DelRegion(tmpPt.x,tmpPt.y,RY_AREA15);
						}
						break;
					case TS_AREA16:
						{
							Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.DelRegionBig(tmpPt.x,tmpPt.y,RY_AREA16);
							else
								m_soundApp.DelRegion(tmpPt.x,tmpPt.y,RY_AREA16);
						}
						break;
					case TS_QINHGONG1:
					case TS_QINHGONG2:
					case TS_QINHGONG3:
					case TS_QINHGONG4:
						{
							/*Nuclear::CPOINT tmpPt = absPt;
							tmpPt.x /= GRID_WIDTH;
							tmpPt.y /= GRID_HEIGHT;*/
							if (::GetKeyState(VK_LCONTROL) < 0)
								m_soundApp.ClearJumpBlock(absPt,m_EdgeSize);
							else
								m_soundApp.ClearJumpBlock(absPt,2);
						}
						break;
					}
					break;
				}

			}
			{
				Nuclear::CPOINT absPt;
				Client2World(point, m_pWorld->GetViewport(), absPt);
				switch (m_pDoc->GetEditState())
				{
				case EDIT_STATE_NPC:
					{
						m_soundApp.MoveSelectNpc(absPt);
					}
					break;
				case EDIT_STATE_MapJump:
					{
						m_soundApp.MoveSelJumpPoint(absPt);
					}
					break;
				}

			}
		}
	}
	CDialog::OnMouseMove(nFlags, point);
}

void CMainDlg::OnLButtonDown(UINT nFlags, CPoint point)
{
	if (!m_bIsLoadedMap)
	{
		AfxMessageBox(L"请先打开一张地图");
		CDialog::OnLButtonDown(nFlags, point);
		return;
	}
	m_pDoc->m_bIsIngoreDragging = false;
	SetCapture();
	m_mouseState |= LEFT_BUTTON_DOWN;
	if (m_pEngine && m_pDoc)
	{
		Client2World(point, m_pWorld->GetViewport(), m_oldMousePoint);
		switch (m_pDoc->GetEditState())
		{
		case EDIT_STATE_TESTING:
			{
				Sprite* pHero = m_soundApp.GetHero();
				if (pHero)
				{
					pHero->GoToRun();
					pHero->MoveTo(m_oldMousePoint, 0, NULL);
				}
			}
			break;
		case EDIT_STATE_MAZE:
			switch (m_toolState)
			{
			case TS_MOVE:
				{
					SetOldViewportPoint();
				}
				break;
			case TS_SELECT:
				{

				}
				break;
			case TS_SETBLOCK:
				{
					if (::GetKeyState(VK_LCONTROL) < 0)
					{
						m_soundApp.SetBlockPoint(m_oldMousePoint,m_bAllScreenBrush,true,true);
					}
					else
					{
						m_soundApp.SetBlockPoint(m_oldMousePoint,m_bAllScreenBrush,true,false);
					}
				}
				m_pDoc->UpdateAllViews(NULL);
				break;
			case TS_SETBLOCK2:
				{
					if (::GetKeyState(VK_LCONTROL) < 0)
					{
						m_soundApp.SetBlockPoint(m_oldMousePoint,m_bAllScreenBrush,true,true,s_BlockLayer2Mask);
					}
					else
					{
						m_soundApp.SetBlockPoint(m_oldMousePoint,m_bAllScreenBrush,true,false,s_BlockLayer2Mask);
					}
				}
				m_pDoc->UpdateAllViews(NULL);
				break;
			case TS_PUTNPC:
				{

				}
				break;
			}
			break;
		case EDIT_STATE_NPC:
			switch (m_toolState)
			{
			case TS_MOVE:
				{
					SetOldViewportPoint();
				}

				break;
			case TS_SELECT:
				{
					if (m_soundApp.SelectNpc(m_oldMousePoint))
					{

					}
				}
				break;
			case TS_SETBLOCK:
				break;
			case TS_PUTNPC:
				break;
			}
			break;
		case EDIT_STATE_MapJump:  //跳转点
			switch (m_toolState)
			{
			case TS_MOVE:
				{
					SetOldViewportPoint();
				}

				break;
			case TS_SELECT:
				{
					int id=m_soundApp.SelectJumpPointByPoint(m_oldMousePoint);
					CPoint fixPoint=m_soundApp.FixPoint(m_oldMousePoint);
					int curSelId=m_soundApp.GetCurSelectJumPoint();
					if (curSelId==-1)
					{
						if (id!=-1)
						{
							m_soundApp.SetEditJumPoint(id);
							m_soundApp.SetSelJumpPoint(id);
						}
					}
					else
					{
						if (!m_soundApp.PointCanJump(m_oldMousePoint))
						{
							MessageBox(L"该区域有阻挡信息，无法设置跳转点");
						}
						else
						{
							if (id!=-1)
							{
								m_soundApp.SetSelJumpPoint(id);
								m_soundApp.SetEditJumPoint(id);
							}
							else
							{
								m_soundApp.SetSelJumpPoint(-1);
							}
						}
					}
				}
				break;
			case TS_SETBLOCK:
				break;
			case TS_PUTNPC:
				break;
			}
			break;
		}
	}
	CDialog::OnLButtonDown(nFlags, point);
}

void CMainDlg::OnLButtonUp(UINT nFlags, CPoint point)
{
	if (!m_bIsLoadedMap)
	{
		CDialog::OnLButtonUp(nFlags, point);
		return;
	}
	m_mouseState &= ~LEFT_BUTTON_DOWN;
	if (m_mouseState == NORMAL)
	{
		ReleaseCapture();
	}
	if (m_pDoc->GetEditState() != EDIT_STATE_TESTING)
	{
		if (m_pEngine && m_pDoc)
		{
			Nuclear::CPOINT absPt;
			Client2World(point, m_pWorld->GetViewport(), absPt);
			switch (m_pDoc->GetEditState())
			{
			case EDIT_STATE_MAZE:
				switch (m_toolState)
				{
				case TS_MOVE:
					break;
				case TS_SELECT:		
					break;
				case TS_SETBLOCK:
					{
						//m_pDoc->UpdateAllViews(NULL);
					}
					break;
				case TS_PUTNPC:
					break;
				}
				break;
			case EDIT_STATE_NPC:
				switch (m_toolState)
				{
				case TS_MOVE:
					break;
				case TS_SELECT:
					break;
				case TS_SETBLOCK:
					break;
				case TS_PUTNPC:
					break;
				}
				break;
			}
		}
	}
	CDialog::OnLButtonUp(nFlags, point);
}


void CMainDlg::OnTool(UINT nID)
{
	if (!m_bIsLoadedMap)
		return;
	m_toolState = static_cast<TOOLS_STATE>(nID - ID_TOOL_MOVE);

	switch (m_toolState)
	{
	case TS_SETBLOCK:
	case TS_QIEZUO:
	case TS_BOSA:
	case TS_BAITAN:
	case TS_DUST:
	case TS_GRASS:
	case TS_WATER:
	case TS_KITE:
	case TS_SHUAGUAI:
	case TS_MINGSHENG:
	case TS_QINHGONG1:
	case TS_QINHGONG2:
	case TS_QINHGONG3:
	case  TS_QINHGONG4:
	case  TS_GUDAO:

	case  TS_AREA11:
	case  TS_AREA12:
	case  TS_AREA13:
	case  TS_AREA14:
	case  TS_AREA15:
	case  TS_AREA16:
	case  TS_SETBLOCK2:
	case  TS_GUDAO2:
	case  TS_GAOJISHUGUAI:

		if(m_toolState==TS_SHUAGUAI)
		{
			m_soundApp.CallMonsterDlg();
		}
		m_soundApp.SetDrawGrid(true);
		m_soundApp.SetDrawBlock(true);
		m_soundApp.SetRegionType(m_toolState);
		break;
	}
}

void CMainDlg::OnUpdateTool(CCmdUI* pCmdUI)
{
	pCmdUI->Enable(true);
	
	if (!m_bIsLoadedMap)
		return;

	if (m_pDoc)
	{
		if (m_pDoc->GetEditState() != EDIT_STATE_TESTING)
		{
			pCmdUI->Enable(TRUE);
			pCmdUI->SetRadio((pCmdUI->m_nID - ID_TOOL_MOVE) == m_toolState);
			return;
		}
	}
	pCmdUI->Enable(FALSE);
	pCmdUI->SetRadio(FALSE);

}

void CMainDlg::OnKeyUp(UINT nChar, UINT nRepCnt, UINT nFlags)
{
	if (!m_bIsLoadedMap)
		return;
	switch (m_pDoc->GetEditState())
	{
	case EDIT_STATE_MAZE:
		switch (m_toolState)
		{
		case TS_MOVE:
			break;
		case TS_SELECT:
			switch(nChar)
			{
			case VK_DELETE:
				{
					m_pDoc->m_nUpdateViewSign = UVS_ENV_SOUND_AREA_SELECTED | UVS_ENV_SOUND_AREA_SHAPE;
					m_pDoc->UpdateAllViews(NULL);
					m_pDoc->m_nUpdateViewSign = 0;
					break;
				}
			}
			break;
		case TS_SETBLOCK:
			switch(nChar)
			{
			case VK_SNAPSHOT:
				{
					break;
				}
			}
			break;
		case TS_PUTNPC:
			switch (nChar)
			{
			case VK_ESCAPE:
				if (m_pDoc->m_pSelectedShape)
				{
					delete m_pDoc->m_pSelectedShape;
					m_pDoc->m_pSelectedShape = NULL;
				}
			}
			break;
		}
		break;
	case EDIT_STATE_NPC:
		break;
	}
	CDialog::OnKeyUp(nChar, nRepCnt, nFlags);
}

void CMainDlg::OnDrawGird()
{
	m_soundApp.OnDrawGird();
}

void CMainDlg::OnUpdateDrawGird(CCmdUI *pCmdUI)
{
	m_soundApp.OnUpdateDrawGird(pCmdUI);
}

void CMainDlg::OnDrawBlock()
{
	m_soundApp.OnDrawBlock();
}

void CMainDlg::OnUpdateDrawBlock(CCmdUI *pCmdUI)
{
	m_soundApp.OnUpdateDrawBlock(pCmdUI);
}

void CMainDlg::OnAllScreenBrush()
{
	m_bAllScreenBrush = !m_bAllScreenBrush;
}

void CMainDlg::OnUpdateAllScreenBrush(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck(m_bAllScreenBrush);
}

void CMainDlg::OnSavemaze()
{
	// 保存阻挡点
	m_soundApp.WriteMazeBuffer(m_mazefilename);
	//保存区域
	m_soundApp.WriteRegionBufferToFile(m_RegionTypeInfoFileName);
    
	//保存跳跃阻挡点信息
	m_soundApp.SaveJumpBlockInf(m_JumpBlockFileName);
	m_soundApp.SaveIslandInf(m_IslandInfFileName,1);
	m_soundApp.SaveIslandInf(m_IslandInf2FileName,2);

	m_soundApp.SaveMonsterKind(m_MonsterFileName);
}


void CMainDlg::On32786()
{
	m_soundApp.OnSaveNpcInf();
}
