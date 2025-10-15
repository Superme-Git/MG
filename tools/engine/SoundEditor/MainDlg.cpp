// MainDlg.cpp : implementation file
//

#include "stdafx.h"
#include "SoundEditor.h"
#include "MainDlg.h"
#include "SoundEditorView.h"
#include "SoundEditorDoc.h"
#include "MazeInfo.h"
#include "..\engine\common\fileiomanager.h"
#include "..\engine\soundarea\xpenvsoundcirclearea.h"
#include "..\engine\soundarea\xpenvsoundpolygonarea.h"
#include "..\engine\map\pstepsoundmap.h"

// CMainDlg dialog

IMPLEMENT_DYNAMIC(CMainDlg, CDialog)

CMainDlg::CMainDlg(CWnd* pParent /*=NULL*/)
: CDialog(CMainDlg::IDD, pParent), m_pDoc(NULL), m_pEngine(NULL), m_pWorld(NULL), m_bIsLoadedMap(false),
m_toolState(TS_MOVE), m_mouseState(MOUSE_STATE_NORMAL)
{
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
	ON_COMMAND_RANGE(ID_TOOL_MOVE, ID_TOOL_CIRCLE, &CMainDlg::OnTool)
	ON_UPDATE_COMMAND_UI_RANGE(ID_TOOL_MOVE, ID_TOOL_CIRCLE, &CMainDlg::OnUpdateTool)
	ON_WM_LBUTTONUP()
	ON_WM_KEYUP()
END_MESSAGE_MAP()


// CMainDlg message handlers

BOOL CMainDlg::OnInitDialog()
{
	CDialog::OnInitDialog();
	m_pDoc = static_cast<CSoundEditorView*>(GetParent())->GetDocument();
	m_soundApp.SetDoc(m_pDoc);
	CRect rect(0, 0, 1024, 768);
	MoveWindow(&rect);

	Nuclear::EngineParameter ep;
	ep.hWnd = m_hWnd;
	ep.bApplictionInBuild = true;
	ep.bAsyncRead = true;
	ep.dmode.width = 1024;
	ep.dmode.height = 768;
	ep.pApp = &m_soundApp;
	Nuclear::IEngine *pEngine = Nuclear::GetEngine();
	if (!pEngine->Run(ep))
	{
		AfxMessageBox(L"引擎初始化失败！");
		exit(0);
	}
	m_pEngine = pEngine;
	while(!m_soundApp.IsInited())
	{
		pEngine->OnIdle();
	}
	m_pWorld = pEngine->GetWorld();
	pEngine->OnWindowsMessage(m_hWnd, WM_SIZE, SIZE_RESTORED, 0);
	SetTimer(1000, 25, NULL);
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
	if (m_pEngine && m_soundApp.IsInited() )
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
		m_pDoc->m_mapName = name;
		m_pDoc->ResetSetMazeName();
		if (m_pWorld->LoadMap(m_pDoc->m_mapName, m_pDoc->m_mazeName, &param, false))
		{
			m_oldViewportPos.x = 0;
			m_oldViewportPos.y = 0;
			m_pWorld->SetViewportLT(0, 0);
			Nuclear::CPOINT size;
			m_pWorld->GetMapSize(size);
			m_pDoc->SetMapSize(size);
			m_bIsLoadedMap = true;
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
		if (((m_pDoc->m_nUpdateViewSign & UVS_VIEWPORT) || (m_pDoc->m_nUpdateViewSign & UVS_TEST_SET_POS)) && m_pDoc->GetUpdateSource() != US_ENGINE)
		{
			m_soundApp.UpdateViewport(m_pDoc->GetViewport());
		}
		if (m_pDoc->m_nUpdateViewSign & UVS_TEST_SET_DARK)
		{
			m_soundApp.EnableNightEffect(m_pDoc->m_bTestingEnableDark);
		}
		if (m_pDoc->m_nUpdateViewSign & UVS_TEST_SET_GAMETIME_CYCLE)
		{
			m_soundApp.SetNightCycle(m_pDoc->GetGameTimeCycle());
		}
		if (m_pDoc->m_nUpdateViewSign & UVS_TEST_SET_GAMETIME && m_pDoc->GetUpdateSource() != US_ENGINE)
		{
			m_soundApp.SetGameTime(m_pDoc->GetGameTime());
		}
		if (m_pDoc->m_nUpdateViewSign & UVS_TEST_SET_VOLUME)
		{
			m_soundApp.SetBkgVolume(m_pDoc->GetBkgVolume());
			m_soundApp.SetEnvVolume(m_pDoc->GetEnvVolume());
			m_soundApp.EnableBkg(m_pDoc->IsBkgEnable());
			m_soundApp.EnableEnv(m_pDoc->IsEnvEnable());
		}
		if (m_pDoc->m_nUpdateViewSign & UVS_TEST_SET_SPRITE)
		{
			Sprite* pSprite = m_soundApp.GetHero();
			if (pSprite)
			{
				pSprite->SetModel(m_pDoc->GetModelName());
				int count = m_pDoc->GetCurSpriteLayerCount(), i;
				for (i=0;i<count;++i)
				{
					pSprite->SetComponent(i, m_pDoc->GetComponent(i));
				}
				pSprite->SetRunLeft(m_pDoc->GetRunLeftAct());
				pSprite->SetRunRight(m_pDoc->GetRunRightAct());
				pSprite->SetStand(m_pDoc->GetStandAct());
				pSprite->SetDefaultAction(m_pDoc->GetStandAct(), false);
				pSprite->SetRideName(m_pDoc->GetRideName());
				pSprite->EnableStepSound(false);
				pSprite->EnableStepSound(true);
			}
		}
	}
}

void CMainDlg::SetOldViewportPoint()
{
	if (m_pWorld)
	{
		Nuclear::CRECT rect = m_pWorld->GetViewport();
		m_oldViewportPos.x = rect.left;
		m_oldViewportPos.y = rect.top;
	}
}

void CMainDlg::MoveViewPort(int dx, int dy)
{
	if (m_pWorld)
	{
		CPoint pt(dx + m_oldViewportPos.x, dy + m_oldViewportPos.y);
		m_soundApp.UpdateViewport(pt);
		/*m_pDoc->UpdateViewport(pt, UVPS_ENGINE);*/
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
		case EDIT_STATE_ENV_SOUND:
			if (m_pDoc->m_nPolygonEditState == 1 && m_pDoc->m_pSelectedEnvArea)
			{
				XPASSERT(m_pDoc->m_pSelectedEnvArea->GetType() == Nuclear::XPAREA_TYPE_POLYGON);
				int pt = m_pDoc->m_pSelectedEnvArea->CheckClickPoint(m_oldMousePoint);
				if (pt > -1)
				{
					m_pDoc->DeleteSelectedPolygonAreaPoint(pt);
				}
			}
			SetOldViewportPoint();
			break;
		case EDIT_STATE_BKG_MUSIC:
			if (m_pDoc->m_nPolygonEditState == 1 && m_pDoc->m_pSelectedBkgArea)
			{
				XPASSERT(m_pDoc->m_pSelectedBkgArea->GetType() == Nuclear::XPAREA_TYPE_POLYGON);
				int pt = m_pDoc->m_pSelectedBkgArea->CheckClickPoint(m_oldMousePoint);
				if (pt > -1)
				{
					m_pDoc->DeleteSelectedPolygonAreaPoint(pt);
				}
			}
			SetOldViewportPoint();
			break;
		case EDIT_STATE_BKG_VOL:
			if (m_pDoc->m_nPolygonEditState == 1 && m_pDoc->m_pSelectedBkgVolArea)
			{
				XPASSERT(m_pDoc->m_pSelectedBkgVolArea->GetType() == Nuclear::XPAREA_TYPE_POLYGON);
				int pt = m_pDoc->m_pSelectedBkgVolArea->CheckClickPoint(m_oldMousePoint);
				if (pt > -1)
				{
					m_pDoc->DeleteSelectedPolygonAreaPoint(pt);
				}
			}
			SetOldViewportPoint();
			break;
		case EDIT_STATE_STEP:
			SetOldViewportPoint();
			break;
		case EDIT_STATE_TESTING:
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
	if (m_mouseState == MOUSE_STATE_NORMAL)
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
		switch (m_pDoc->GetEditState())
		{
		case EDIT_STATE_STEP:
			{
				Nuclear::CPOINT absPt;
				Client2World(point, m_pWorld->GetViewport(), absPt);
				GetStepSoundMouseRect(absPt, m_soundApp.m_mouseRect);
			}
			if (m_mouseState & LEFT_BUTTON_DOWN)
			{
				m_pDoc->SetStepSound(m_soundApp.m_mouseRect, m_pDoc->m_nStepBrush);
			}
			if (!(m_mouseState & RIGHT_BUTTON_DOWN))
			{
				break;
			}
		case EDIT_STATE_BKG_MUSIC:
		case EDIT_STATE_ENV_SOUND:
		case EDIT_STATE_BKG_VOL:
			if ((m_toolState == TS_MOVE && (m_mouseState & LEFT_BUTTON_DOWN)) || (m_mouseState & RIGHT_BUTTON_DOWN))
			{
				Nuclear::CPOINT pt;
				Nuclear::CRECT rect(m_oldViewportPos, 1024, 768);
				Client2World(point, rect, pt);
				MoveViewPort(m_oldMousePoint.x - pt.x, m_oldMousePoint.y - pt.y);
			} else if (m_mouseState & LEFT_BUTTON_DOWN) {
				Nuclear::CPOINT absPt;
				Client2World(point, m_pWorld->GetViewport(), absPt);
				switch (m_pDoc->GetEditState())
				{
				case EDIT_STATE_ENV_SOUND:
					switch (m_toolState)
					{
					case TS_CIRCLE:
						{
							Nuclear::XPCircle* pCircle = dynamic_cast<Nuclear::XPCircle*>(m_pDoc->m_pSelectedShape);
							XPASSERT(pCircle);
							pCircle->m_nRadius = static_cast<int>(sqrtf(static_cast<float>(absPt.Dis2(m_oldMousePoint))));
						}
						break;
					case TS_POLYGON:
						break;
					case TS_SELECT:
						if (m_pDoc->m_pSelectedEnvArea)
						{
							if (m_pDoc->m_nSelectedPt == -1)
							{
								m_pDoc->m_movingSelectArea.x = absPt.x - m_oldMousePoint.x;
								m_pDoc->m_movingSelectArea.y = absPt.y - m_oldMousePoint.y;
							} else {
								switch (m_pDoc->m_pSelectedEnvArea->GetType())
								{
								case Nuclear::XPAREA_TYPE_CIRCLE:
									{
										if (!m_pDoc->m_pSelectedShape)
										{
											m_pDoc->m_pSelectedShape = new Nuclear::XPCircle();
											m_pDoc->m_pSelectedEnvArea->GetShape(m_pDoc->m_pSelectedShape);
										}
										Nuclear::XPCircle* pCircle	= dynamic_cast<Nuclear::XPCircle*>(m_pDoc->m_pSelectedShape);
										XPASSERT(pCircle);
										pCircle->m_nRadius = static_cast<int>(sqrtf(static_cast<float>(absPt.Dis2(pCircle->m_pos))));
									}
									break;
								case Nuclear::XPAREA_TYPE_POLYGON:
									switch (m_pDoc->m_nPolygonEditState)
									{
									case 1:
										{
											if (!m_pDoc->m_pSelectedShape)
											{
												m_pDoc->m_pSelectedShape = new Nuclear::XPPolygon();
												m_pDoc->m_pSelectedEnvArea->GetShape(m_pDoc->m_pSelectedShape);
											}
											Nuclear::XPPolygon* pPolygon = dynamic_cast<Nuclear::XPPolygon*>(m_pDoc->m_pSelectedShape);
											XPASSERT(pPolygon);
											pPolygon->m_points[m_pDoc->m_nSelectedPt] = absPt;
											break;
										}
									case 2:
										m_pDoc->m_CutingPolygonLine.right = absPt.x;
										m_pDoc->m_CutingPolygonLine.bottom = absPt.y;
										break;
									default:
										//0
										break;
									}
									break;
								}
							}
						}
						break;
					}
					break;
				case EDIT_STATE_BKG_VOL:
					switch (m_toolState)
					{
					case TS_CIRCLE:
						{
							Nuclear::XPCircle* pCircle = dynamic_cast<Nuclear::XPCircle*>(m_pDoc->m_pSelectedShape);
							XPASSERT(pCircle);
							pCircle->m_nRadius = static_cast<int>(sqrtf(static_cast<float>(absPt.Dis2(m_oldMousePoint))));
						}
						break;
					case TS_POLYGON:
						break;
					case TS_SELECT:
						if (m_pDoc->m_pSelectedBkgVolArea)
						{
							if (m_pDoc->m_nSelectedPt == -1)
							{
								m_pDoc->m_movingSelectArea.x = absPt.x - m_oldMousePoint.x;
								m_pDoc->m_movingSelectArea.y = absPt.y - m_oldMousePoint.y;
							} else {
								switch (m_pDoc->m_pSelectedBkgVolArea->GetType())
								{
								case Nuclear::XPAREA_TYPE_CIRCLE:
									{
										if (!m_pDoc->m_pSelectedShape)
										{
											m_pDoc->m_pSelectedShape = new Nuclear::XPCircle();
											m_pDoc->m_pSelectedBkgVolArea->GetShape(m_pDoc->m_pSelectedShape);
										}
										Nuclear::XPCircle* pCircle	= dynamic_cast<Nuclear::XPCircle*>(m_pDoc->m_pSelectedShape);
										XPASSERT(pCircle);
										pCircle->m_nRadius = static_cast<int>(sqrtf(static_cast<float>(absPt.Dis2(pCircle->m_pos))));
									}
									break;
								case Nuclear::XPAREA_TYPE_POLYGON:
									switch (m_pDoc->m_nPolygonEditState)
									{
									case 1:
										{
											if (!m_pDoc->m_pSelectedShape)
											{
												m_pDoc->m_pSelectedShape = new Nuclear::XPPolygon();
												m_pDoc->m_pSelectedBkgVolArea->GetShape(m_pDoc->m_pSelectedShape);
											}
											Nuclear::XPPolygon* pPolygon = dynamic_cast<Nuclear::XPPolygon*>(m_pDoc->m_pSelectedShape);
											XPASSERT(pPolygon);
											pPolygon->m_points[m_pDoc->m_nSelectedPt] = absPt;
											break;
										}
									case 2:
										m_pDoc->m_CutingPolygonLine.right = absPt.x;
										m_pDoc->m_CutingPolygonLine.bottom = absPt.y;
										break;
									default:
										//0
										break;
									}
									break;
								}
							}
						}
						break;
					}
					break;
				case EDIT_STATE_BKG_MUSIC:
					switch (m_toolState)
					{
					case TS_CIRCLE:
						{
							Nuclear::XPCircle* pCircle = dynamic_cast<Nuclear::XPCircle*>(m_pDoc->m_pSelectedShape);
							XPASSERT(pCircle);
							pCircle->m_nRadius = static_cast<int>(sqrtf(static_cast<float>(absPt.Dis2(m_oldMousePoint))));
						}
						break;
					case TS_POLYGON:
						break;
					case TS_SELECT:
						if (m_pDoc->m_pSelectedBkgArea)
						{
							if (m_pDoc->m_nSelectedPt == -1)
							{
								m_pDoc->m_movingSelectArea.x = absPt.x - m_oldMousePoint.x;
								m_pDoc->m_movingSelectArea.y = absPt.y - m_oldMousePoint.y;
							} else {
								switch (m_pDoc->m_pSelectedBkgArea->GetType())
								{
								case Nuclear::XPAREA_TYPE_CIRCLE:
									{
										if (!m_pDoc->m_pSelectedShape)
										{
											m_pDoc->m_pSelectedShape = new Nuclear::XPCircle();
											m_pDoc->m_pSelectedBkgArea->GetShape(m_pDoc->m_pSelectedShape);
										}
										Nuclear::XPCircle* pCircle	= dynamic_cast<Nuclear::XPCircle*>(m_pDoc->m_pSelectedShape);
										XPASSERT(pCircle);
										pCircle->m_nRadius = static_cast<int>(sqrtf(static_cast<float>(absPt.Dis2(pCircle->m_pos))));
									}
									break;
								case Nuclear::XPAREA_TYPE_POLYGON:
									switch (m_pDoc->m_nPolygonEditState)
									{
									case 1:
										{
											if (!m_pDoc->m_pSelectedShape)
											{
												m_pDoc->m_pSelectedShape = new Nuclear::XPPolygon();
												m_pDoc->m_pSelectedBkgArea->GetShape(m_pDoc->m_pSelectedShape);
											}
											Nuclear::XPPolygon* pPolygon = dynamic_cast<Nuclear::XPPolygon*>(m_pDoc->m_pSelectedShape);
											XPASSERT(pPolygon);
											pPolygon->m_points[m_pDoc->m_nSelectedPt] = absPt;
											break;
										}
									case 2:
										m_pDoc->m_CutingPolygonLine.right = absPt.x;
										m_pDoc->m_CutingPolygonLine.bottom = absPt.y;
										break;
									default:
										//0
										break;
									}
									break;
								}
							}
						}
						break;
					}
					break;
				}
			}
			break;
		default:
			break;
		}
	}
	CDialog::OnMouseMove(nFlags, point);
}

void CMainDlg::GetStepSoundMouseRect(const POINT &pt, RECT &rect)
{
	rect.left = pt.x / Nuclear::PStepSoundMap::GRID_WIDTH - (m_pDoc->m_nBrushSize >> 1);
	rect.top = pt.y / Nuclear::PStepSoundMap::GRID_HEIGHT - (m_pDoc->m_nBrushSize >> 1);
	rect.right = rect.left + m_pDoc->m_nBrushSize;
	rect.bottom = rect.top + m_pDoc->m_nBrushSize;
	rect.left *= Nuclear::PStepSoundMap::GRID_WIDTH;
	rect.top *= Nuclear::PStepSoundMap::GRID_HEIGHT;
	rect.right *= Nuclear::PStepSoundMap::GRID_WIDTH;
	rect.bottom *= Nuclear::PStepSoundMap::GRID_HEIGHT;
}

void CMainDlg::OnLButtonDown(UINT nFlags, CPoint point)
{
	if (!m_bIsLoadedMap)
	{
		AfxMessageBox(L"请先打开一张地图");
		CDialog::OnLButtonDown(nFlags, point);
		return;
	}
	SetCapture();
	m_mouseState |= LEFT_BUTTON_DOWN;
	if (m_pEngine && m_pDoc)
	{
		Client2World(point, m_pWorld->GetViewport(), m_oldMousePoint);
		switch (m_pDoc->GetEditState())
		{
		case EDIT_STATE_STEP:
			GetStepSoundMouseRect(m_oldMousePoint, m_soundApp.m_mouseRect);
			m_pDoc->BeginSetStepSound();
			m_pDoc->SetStepSound(m_soundApp.m_mouseRect, m_pDoc->m_nStepBrush);
			break;
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
		case EDIT_STATE_ENV_SOUND:
			switch (m_toolState)
			{
			case TS_MOVE:
				SetOldViewportPoint();
				break;
			case TS_SELECT:
				if (m_pDoc->m_pSelectedEnvArea && m_pDoc->m_pSelectedEnvArea->GetType() == Nuclear::XPAREA_TYPE_POLYGON && m_pDoc->m_nPolygonEditState != 0)
				{
					CToolsEnvSoundArea* pOld = m_pDoc->m_pSelectedEnvArea;
					if (m_pDoc->m_nPolygonEditState == 1)
					{
						switch (m_pDoc->CheckSelectEnvArea(m_oldMousePoint))
						{
						case SS_SELECTED:	//拖动区域，这里不用做事情
						case SS_SELECTED_AREA_POINT://拖动点，这里不用做事情
							break;
						case SS_SELECTED_AREA_LINE:
						case SS_ENPTY:
						case SS_NEWSELECTED://加新点
							m_pDoc->m_pSelectedEnvArea = pOld;
							m_pDoc->AddSelectedEnvPolygonPoint(m_oldMousePoint);
							break;
						}
					} else {	//==2(cut)
						switch (m_pDoc->CheckSelectEnvArea(m_oldMousePoint))
						{
						case SS_SELECTED_AREA_POINT:
							{
								CToolsEnvSoundPolygonArea* pPolygonArea = dynamic_cast<CToolsEnvSoundPolygonArea*>(m_pDoc->m_pSelectedEnvArea);
								Nuclear::CPOINT pt = pPolygonArea->GetPoint(m_pDoc->m_nSelectedPt);
								m_pDoc->m_CutingPolygonLine.right = m_pDoc->m_CutingPolygonLine.left = pt.x;
								m_pDoc->m_CutingPolygonLine.bottom = m_pDoc->m_CutingPolygonLine.top = pt.y;
								break;
							}
						case SS_ENPTY:
						case SS_NEWSELECTED:
							m_pDoc->m_nUpdateViewSign = UVS_ENV_SOUND_AREA_SELECTED;
							m_pDoc->UpdateAllViews(NULL);
							m_pDoc->m_nUpdateViewSign = 0;
							break;
						}
					}
					
				} else {
					switch (m_pDoc->CheckSelectEnvArea(m_oldMousePoint))
					{
					case SS_NEWSELECTED:
					case SS_ENPTY:
						m_pDoc->m_nUpdateViewSign = UVS_ENV_SOUND_AREA_SELECTED;
						m_pDoc->UpdateAllViews(NULL);
						m_pDoc->m_nUpdateViewSign = 0;
						break;
					}
				}
				break;
			case TS_CIRCLE:
				m_pDoc->m_pSelectedEnvArea = NULL;
				m_pDoc->m_nUpdateViewSign = UVS_ENV_SOUND_AREA_SELECTED;
				m_pDoc->UpdateAllViews(NULL);
				m_pDoc->m_nUpdateViewSign = 0;
				if (m_pDoc->m_pSelectedShape)
					delete m_pDoc->m_pSelectedShape;
				m_pDoc->m_pSelectedShape = new Nuclear::XPCircle(m_oldMousePoint);
				break;
			case TS_POLYGON:
				{
					m_pDoc->m_pSelectedEnvArea = NULL;
					m_pDoc->m_nUpdateViewSign = UVS_ENV_SOUND_AREA_SELECTED;
					m_pDoc->UpdateAllViews(NULL);
					m_pDoc->m_nUpdateViewSign = 0;
					if (m_pDoc->m_pSelectedShape && m_pDoc->m_pSelectedShape->GetType() != Nuclear::XPSHAPE_TYPE_POLYGON)
					{
						delete m_pDoc->m_pSelectedShape;
						m_pDoc->m_pSelectedShape = NULL;
					}
					if (!m_pDoc->m_pSelectedShape)
						m_pDoc->m_pSelectedShape = new Nuclear::XPPolygon();
					Nuclear::XPPolygon* pPolygon = dynamic_cast<Nuclear::XPPolygon*>(m_pDoc->m_pSelectedShape);
					XPASSERT(pPolygon);
					if (pPolygon->m_points.size() > 2)
					{
						if (pPolygon->m_points.front().Dis2(m_oldMousePoint) < 10)
						{
							CToolsEnvSoundPolygonArea* pPolygonArea = new CToolsEnvSoundPolygonArea();
							CSoundEditorDoc::SetShape(pPolygonArea, pPolygon);
							m_pDoc->AddAnEnvArea(pPolygonArea);
							m_pDoc->m_pSelectedEnvArea = pPolygonArea;
							delete pPolygon;
							pPolygon = NULL;
							m_pDoc->m_pSelectedShape = NULL;
							m_pDoc->m_nUpdateViewSign = UVS_ENV_SOUND_AREA_SELECTED | UVS_ENV_SOUND_AREA_SHAPE;
							m_pDoc->UpdateAllViews(NULL);
							m_pDoc->m_nUpdateViewSign = 0;
						}
					}
					if (pPolygon)
						pPolygon->m_points.push_back(m_oldMousePoint);
				}
				break;
			}
			break;
		case EDIT_STATE_BKG_VOL:
			switch (m_toolState)
			{
			case TS_MOVE:
				SetOldViewportPoint();
				break;
			case TS_SELECT:
				if (m_pDoc->m_pSelectedBkgVolArea && m_pDoc->m_pSelectedBkgVolArea->GetType() == Nuclear::XPAREA_TYPE_POLYGON && m_pDoc->m_nPolygonEditState != 0)
				{
					CToolsBkgVolumeArea* pOld = m_pDoc->m_pSelectedBkgVolArea;
					if (m_pDoc->m_nPolygonEditState == 1)
					{
						switch (m_pDoc->CheckSelectBkgVolArea(m_oldMousePoint))
						{
						case SS_SELECTED:	//拖动区域，这里不用做事情
						case SS_SELECTED_AREA_POINT://拖动点，这里不用做事情
							break;
						case SS_SELECTED_AREA_LINE:
						case SS_ENPTY:
						case SS_NEWSELECTED://加新点
							m_pDoc->m_pSelectedBkgVolArea = pOld;
							m_pDoc->AddSelectedBkgVolPolygonPoint(m_oldMousePoint);
							break;
						}
					} else {	//==2(cut)
						switch (m_pDoc->CheckSelectBkgVolArea(m_oldMousePoint))
						{
						case SS_SELECTED_AREA_POINT:
							{
								CToolsBkgVolPolygonArea* pPolygonArea = dynamic_cast<CToolsBkgVolPolygonArea*>(m_pDoc->m_pSelectedBkgVolArea);
								Nuclear::CPOINT pt = pPolygonArea->GetPoint(m_pDoc->m_nSelectedPt);
								m_pDoc->m_CutingPolygonLine.right = m_pDoc->m_CutingPolygonLine.left = pt.x;
								m_pDoc->m_CutingPolygonLine.bottom = m_pDoc->m_CutingPolygonLine.top = pt.y;
								break;
							}
						case SS_ENPTY:
						case SS_NEWSELECTED:
							m_pDoc->m_nUpdateViewSign = UVS_BKV_SOUND_AREA_SELECTED;
							m_pDoc->UpdateAllViews(NULL);
							m_pDoc->m_nUpdateViewSign = 0;
							break;
						}
					}

				} else {
					switch (m_pDoc->CheckSelectBkgVolArea(m_oldMousePoint))
					{
					case SS_NEWSELECTED:
					case SS_ENPTY:
						m_pDoc->m_nUpdateViewSign = UVS_BKV_SOUND_AREA_SELECTED;
						m_pDoc->UpdateAllViews(NULL);
						m_pDoc->m_nUpdateViewSign = 0;
						break;
					}
				}
				break;
			case TS_CIRCLE:
				m_pDoc->m_pSelectedBkgVolArea = NULL;
				m_pDoc->m_nUpdateViewSign = UVS_BKV_SOUND_AREA_SELECTED;
				m_pDoc->UpdateAllViews(NULL);
				m_pDoc->m_nUpdateViewSign = 0;
				if (m_pDoc->m_pSelectedShape)
					delete m_pDoc->m_pSelectedShape;
				m_pDoc->m_pSelectedShape = new Nuclear::XPCircle(m_oldMousePoint);
				break;
			case TS_POLYGON:
				{
					m_pDoc->m_pSelectedBkgVolArea = NULL;
					m_pDoc->m_nUpdateViewSign = UVS_BKV_SOUND_AREA_SELECTED;
					m_pDoc->UpdateAllViews(NULL);
					m_pDoc->m_nUpdateViewSign = 0;
					if (m_pDoc->m_pSelectedShape && m_pDoc->m_pSelectedShape->GetType() != Nuclear::XPSHAPE_TYPE_POLYGON)
					{
						delete m_pDoc->m_pSelectedShape;
						m_pDoc->m_pSelectedShape = NULL;
					}
					if (!m_pDoc->m_pSelectedShape)
						m_pDoc->m_pSelectedShape = new Nuclear::XPPolygon();
					Nuclear::XPPolygon* pPolygon = dynamic_cast<Nuclear::XPPolygon*>(m_pDoc->m_pSelectedShape);
					XPASSERT(pPolygon);
					if (pPolygon->m_points.size() > 2)
					{
						if (pPolygon->m_points.front().Dis2(m_oldMousePoint) < 10)
						{
							CToolsBkgVolPolygonArea* pPolygonArea = new CToolsBkgVolPolygonArea();
							CSoundEditorDoc::SetShape(pPolygonArea, pPolygon);
							m_pDoc->AddAnBkgVolArea(pPolygonArea);
							m_pDoc->m_pSelectedBkgVolArea = pPolygonArea;
							delete pPolygon;
							pPolygon = NULL;
							m_pDoc->m_pSelectedShape = NULL;
							m_pDoc->m_nUpdateViewSign = UVS_BKV_SOUND_AREA_SELECTED | UVS_BKV_SOUND_AREA_SHAPE;
							m_pDoc->UpdateAllViews(NULL);
							m_pDoc->m_nUpdateViewSign = 0;
						}
					}
					if (pPolygon)
						pPolygon->m_points.push_back(m_oldMousePoint);
				}
				break;
			}
			break;
		case EDIT_STATE_BKG_MUSIC:
			switch (m_toolState)
			{
			case TS_MOVE:
				SetOldViewportPoint();
				break;
			case TS_SELECT:
				if (m_pDoc->m_pSelectedBkgArea && m_pDoc->m_pSelectedBkgArea->GetType() == Nuclear::XPAREA_TYPE_POLYGON && m_pDoc->m_nPolygonEditState != 0)
				{
					CToolsBkgSoundArea* pOld = m_pDoc->m_pSelectedBkgArea;
					if (m_pDoc->m_nPolygonEditState == 1)
					{
						switch (m_pDoc->CheckSelectBkgArea(m_oldMousePoint))
						{
						case SS_SELECTED:	//拖动区域，这里不用做事情
						case SS_SELECTED_AREA_POINT://拖动点，这里不用做事情
							break;
						case SS_SELECTED_AREA_LINE:
						case SS_ENPTY:
						case SS_NEWSELECTED://加新点
							m_pDoc->m_pSelectedBkgArea = pOld;
							m_pDoc->AddSelectedBkgPolygonPoint(m_oldMousePoint);
							break;
						}
					} else {	//==2(cut)
						switch (m_pDoc->CheckSelectBkgArea(m_oldMousePoint))
						{
						case SS_SELECTED_AREA_POINT:
							{
								CToolsBkgSoundPolygonArea* pPolygonArea = dynamic_cast<CToolsBkgSoundPolygonArea*>(m_pDoc->m_pSelectedBkgArea);
								Nuclear::CPOINT pt = pPolygonArea->GetPoint(m_pDoc->m_nSelectedPt);
								m_pDoc->m_CutingPolygonLine.right = m_pDoc->m_CutingPolygonLine.left = pt.x;
								m_pDoc->m_CutingPolygonLine.bottom = m_pDoc->m_CutingPolygonLine.top = pt.y;
								break;
							}
						case SS_ENPTY:
						case SS_NEWSELECTED:
							m_pDoc->m_nUpdateViewSign = UVS_BKG_SOUND_AREA_SELECTED;
							m_pDoc->UpdateAllViews(NULL);
							m_pDoc->m_nUpdateViewSign = 0;
							break;
						}
					}

				} else {
					switch (m_pDoc->CheckSelectBkgArea(m_oldMousePoint))
					{
					case SS_NEWSELECTED:
					case SS_ENPTY:
						m_pDoc->m_nUpdateViewSign = UVS_BKG_SOUND_AREA_SELECTED;
						m_pDoc->UpdateAllViews(NULL);
						m_pDoc->m_nUpdateViewSign = 0;
						break;
					}
				}
				break;
			case TS_CIRCLE:
				m_pDoc->m_pSelectedBkgArea = NULL;
				m_pDoc->m_nUpdateViewSign = UVS_BKG_SOUND_AREA_SELECTED;
				m_pDoc->UpdateAllViews(NULL);
				m_pDoc->m_nUpdateViewSign = 0;
				if (m_pDoc->m_pSelectedShape)
					delete m_pDoc->m_pSelectedShape;
				m_pDoc->m_pSelectedShape = new Nuclear::XPCircle(m_oldMousePoint);
				break;
			case TS_POLYGON:
				{
					m_pDoc->m_pSelectedBkgArea = NULL;
					m_pDoc->m_nUpdateViewSign = UVS_BKG_SOUND_AREA_SELECTED;
					m_pDoc->UpdateAllViews(NULL);
					m_pDoc->m_nUpdateViewSign = 0;
					if (m_pDoc->m_pSelectedShape && m_pDoc->m_pSelectedShape->GetType() != Nuclear::XPSHAPE_TYPE_POLYGON)
					{
						delete m_pDoc->m_pSelectedShape;
						m_pDoc->m_pSelectedShape = NULL;
					}
					if (!m_pDoc->m_pSelectedShape)
						m_pDoc->m_pSelectedShape = new Nuclear::XPPolygon();
					Nuclear::XPPolygon* pPolygon = dynamic_cast<Nuclear::XPPolygon*>(m_pDoc->m_pSelectedShape);
					XPASSERT(pPolygon);
					if (pPolygon->m_points.size() > 2)
					{
						if (pPolygon->m_points.front().Dis2(m_oldMousePoint) < 10)
						{
							CToolsBkgSoundPolygonArea* pPolygonArea = new CToolsBkgSoundPolygonArea();
							CSoundEditorDoc::SetShape(pPolygonArea, pPolygon);
							m_pDoc->AddAnBkgArea(pPolygonArea);
							m_pDoc->m_pSelectedBkgArea = pPolygonArea;
							delete pPolygon;
							pPolygon = NULL;
							m_pDoc->m_pSelectedShape = NULL;
							m_pDoc->m_nUpdateViewSign = UVS_BKG_SOUND_AREA_SELECTED | UVS_BKG_SOUND_AREA_SHAPE;
							m_pDoc->UpdateAllViews(NULL);
							m_pDoc->m_nUpdateViewSign = 0;
						}
					}
					if (pPolygon)
						pPolygon->m_points.push_back(m_oldMousePoint);
				}
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
	if (!(m_mouseState & LEFT_BUTTON_DOWN))
	{
		if (m_mouseState == MOUSE_STATE_NORMAL)
		{
			ReleaseCapture();
		}
		CDialog::OnLButtonUp(nFlags, point);
		return;
	}
	m_mouseState &= ~LEFT_BUTTON_DOWN;
	if (m_mouseState == MOUSE_STATE_NORMAL)
	{
		ReleaseCapture();
	}
	switch (m_pDoc->GetEditState())
	{
	case EDIT_STATE_STEP:
		m_pDoc->EndSetStepSound();
		break;
	case EDIT_STATE_BKG_MUSIC:
	case EDIT_STATE_ENV_SOUND:
	case EDIT_STATE_BKG_VOL:
		if (m_pEngine && m_pDoc)
		{
			Nuclear::CPOINT absPt;
			Client2World(point, m_pWorld->GetViewport(), absPt);
			switch (m_pDoc->GetEditState())
			{
			case EDIT_STATE_ENV_SOUND:
				switch (m_toolState)
				{
				case TS_MOVE:
					break;
				case TS_SELECT:
					if (m_pDoc->m_pSelectedEnvArea)
					{
						if (m_pDoc->m_nSelectedPt == -1)
						{
							m_pDoc->MoveSelectedArea();
						} else {
							switch (m_pDoc->m_pSelectedEnvArea->GetType())
							{
							case Nuclear::XPAREA_TYPE_CIRCLE:
								{
									Nuclear::XPCircle* pCircle	= dynamic_cast<Nuclear::XPCircle*>(m_pDoc->m_pSelectedShape);
									if (pCircle)
									{
										m_pDoc->SetSelectedCircleAreaRadius(static_cast<int>(sqrtf(static_cast<float>(absPt.Dis2(pCircle->m_pos)))));
										delete pCircle;
										m_pDoc->m_pSelectedShape = NULL;
									}
								}
								break;
							case Nuclear::XPAREA_TYPE_POLYGON:
								switch (m_pDoc->m_nPolygonEditState)
								{
								case 1:
									{
										Nuclear::XPPolygon* pPolygon = dynamic_cast<Nuclear::XPPolygon*>(m_pDoc->m_pSelectedShape);
										if (pPolygon)
										{
											m_pDoc->MoveSelectedPolygonAreaPointTo(absPt);
											delete pPolygon;
											m_pDoc->m_pSelectedShape = NULL;
											m_pDoc->m_nUpdateViewSign = UVS_ENV_SOUND_AREA_SHAPE;
											m_pDoc->UpdateAllViews(NULL);
											m_pDoc->m_nUpdateViewSign = 0;
										}
									}
									break;
								case 2:
									{
										CToolsEnvSoundPolygonArea* pPolygonArea = dynamic_cast<CToolsEnvSoundPolygonArea*>(m_pDoc->m_pSelectedEnvArea);
										int oldPt = m_pDoc->m_nSelectedPt;
										if (m_pDoc->CheckSelectEnvArea(absPt) == SS_SELECTED_AREA_POINT)
										{
											if (m_pDoc->m_nSelectedPt != oldPt)
											{
												m_pDoc->CutSelectedPolygon(oldPt, m_pDoc->m_nSelectedPt);
												m_pDoc->m_nUpdateViewSign = UVS_ENV_SOUND_AREA_SHAPE;
												m_pDoc->UpdateAllViews(NULL);
												m_pDoc->m_nUpdateViewSign = 0;
											}
										}
										m_pDoc->m_nSelectedPt = -1;
										m_pDoc->m_pSelectedEnvArea = pPolygonArea;
									}
									break;
								default:
									break;
								}
								break;
							}
							m_pDoc->m_nSelectedPt = -1;
						}
					}
					
					break;
				case TS_CIRCLE:
					{
						Nuclear::XPCircle* pCircle = dynamic_cast<Nuclear::XPCircle*>(m_pDoc->m_pSelectedShape);
						if (!pCircle)
							break;
						pCircle->m_nRadius = static_cast<int>(sqrtf(static_cast<float>(absPt.Dis2(m_oldMousePoint))));
						m_pDoc->m_nUpdateViewSign = UVS_ENV_SOUND_AREA_SELECTED;
						if (pCircle->m_nRadius >= 10)
						{
							CToolsEnvSoundCircleArea* pCircleArea = new CToolsEnvSoundCircleArea();
							CSoundEditorDoc::SetShape(pCircleArea, pCircle);
							m_pDoc->ResetAreaRegion(pCircleArea);
							m_pDoc->AddAnEnvArea(pCircleArea);
							m_pDoc->m_pSelectedEnvArea = pCircleArea;
							m_pDoc->m_nUpdateViewSign = m_pDoc->m_nUpdateViewSign | UVS_ENV_SOUND_AREA_SHAPE;
						}
						delete pCircle;
						m_pDoc->m_pSelectedShape = NULL;
						m_pDoc->UpdateAllViews(NULL);
						m_pDoc->m_nUpdateViewSign = 0;
					}
					break;
				case TS_POLYGON:
					break;
				}
				break;
			case EDIT_STATE_BKG_VOL:
				switch (m_toolState)
				{
				case TS_MOVE:
					break;
				case TS_SELECT:
					if (m_pDoc->m_pSelectedBkgVolArea)
					{
						if (m_pDoc->m_nSelectedPt == -1)
						{
							m_pDoc->MoveSelectedArea();
						} else {
							switch (m_pDoc->m_pSelectedBkgVolArea->GetType())
							{
							case Nuclear::XPAREA_TYPE_CIRCLE:
								{
									Nuclear::XPCircle* pCircle	= dynamic_cast<Nuclear::XPCircle*>(m_pDoc->m_pSelectedShape);
									if (pCircle)
									{
										m_pDoc->SetSelectedCircleAreaRadius(static_cast<int>(sqrtf(static_cast<float>(absPt.Dis2(pCircle->m_pos)))));
										delete pCircle;
										m_pDoc->m_pSelectedShape = NULL;
									}
								}
								break;
							case Nuclear::XPAREA_TYPE_POLYGON:
								switch (m_pDoc->m_nPolygonEditState)
								{
								case 1:
									{
										Nuclear::XPPolygon* pPolygon = dynamic_cast<Nuclear::XPPolygon*>(m_pDoc->m_pSelectedShape);
										if (pPolygon)
										{
											m_pDoc->MoveSelectedPolygonAreaPointTo(absPt);
											delete pPolygon;
											m_pDoc->m_pSelectedShape = NULL;
											m_pDoc->m_nUpdateViewSign = UVS_BKV_SOUND_AREA_SHAPE;
											m_pDoc->UpdateAllViews(NULL);
											m_pDoc->m_nUpdateViewSign = 0;
										}
									}
									break;
								case 2:
									{
										CToolsBkgVolPolygonArea* pPolygonArea = dynamic_cast<CToolsBkgVolPolygonArea*>(m_pDoc->m_pSelectedBkgVolArea);
										int oldPt = m_pDoc->m_nSelectedPt;
										if (m_pDoc->CheckSelectBkgVolArea(absPt) == SS_SELECTED_AREA_POINT)
										{
											if (m_pDoc->m_nSelectedPt != oldPt)
											{
												m_pDoc->CutSelectedPolygon(oldPt, m_pDoc->m_nSelectedPt);
												m_pDoc->m_nUpdateViewSign = UVS_BKV_SOUND_AREA_SHAPE;
												m_pDoc->UpdateAllViews(NULL);
												m_pDoc->m_nUpdateViewSign = 0;
											}
										}
										m_pDoc->m_nSelectedPt = -1;
										m_pDoc->m_pSelectedBkgVolArea = pPolygonArea;
									}
									break;
								default:
									break;
								}
								break;
							}
							m_pDoc->m_nSelectedPt = -1;
						}
					}

					break;
				case TS_CIRCLE:
					{
						Nuclear::XPCircle* pCircle = dynamic_cast<Nuclear::XPCircle*>(m_pDoc->m_pSelectedShape);
						if (!pCircle)
							break;
						pCircle->m_nRadius = static_cast<int>(sqrtf(static_cast<float>(absPt.Dis2(m_oldMousePoint))));
						m_pDoc->m_nUpdateViewSign = UVS_BKV_SOUND_AREA_SELECTED;
						if (pCircle->m_nRadius >= 10)
						{
							CToolsBkgVolCircleArea* pCircleArea = new CToolsBkgVolCircleArea();
							CSoundEditorDoc::SetShape(pCircleArea, pCircle);
							m_pDoc->ResetAreaRegion(pCircleArea);
							m_pDoc->AddAnBkgVolArea(pCircleArea);
							m_pDoc->m_pSelectedBkgVolArea = pCircleArea;
							m_pDoc->m_nUpdateViewSign = m_pDoc->m_nUpdateViewSign | UVS_BKV_SOUND_AREA_SHAPE;
						}
						delete pCircle;
						m_pDoc->m_pSelectedShape = NULL;
						m_pDoc->UpdateAllViews(NULL);
						m_pDoc->m_nUpdateViewSign = 0;
					}
					break;
				case TS_POLYGON:
					break;
				}
				break;
			case EDIT_STATE_BKG_MUSIC:
				switch (m_toolState)
				{
				case TS_MOVE:
					break;
				case TS_SELECT:
					if (m_pDoc->m_pSelectedBkgArea)
					{
						if (m_pDoc->m_nSelectedPt == -1)
						{
							m_pDoc->MoveSelectedArea();
						} else {
							switch (m_pDoc->m_pSelectedBkgArea->GetType())
							{
							case Nuclear::XPAREA_TYPE_CIRCLE:
								{
									Nuclear::XPCircle* pCircle	= dynamic_cast<Nuclear::XPCircle*>(m_pDoc->m_pSelectedShape);
									if (pCircle)
									{
										m_pDoc->SetSelectedCircleAreaRadius(static_cast<int>(sqrtf(static_cast<float>(absPt.Dis2(pCircle->m_pos)))));
										delete pCircle;
										m_pDoc->m_pSelectedShape = NULL;
									}
								}
								break;
							case Nuclear::XPAREA_TYPE_POLYGON:
								switch (m_pDoc->m_nPolygonEditState)
								{
								case 1:
									{
										Nuclear::XPPolygon* pPolygon = dynamic_cast<Nuclear::XPPolygon*>(m_pDoc->m_pSelectedShape);
										if (pPolygon)
										{
											m_pDoc->MoveSelectedPolygonAreaPointTo(absPt);
											delete pPolygon;
											m_pDoc->m_pSelectedShape = NULL;
											m_pDoc->m_nUpdateViewSign = UVS_BKG_SOUND_AREA_SHAPE;
											m_pDoc->UpdateAllViews(NULL);
											m_pDoc->m_nUpdateViewSign = 0;
										}
									}
									break;
								case 2:
									{
										CToolsBkgSoundPolygonArea* pPolygonArea = dynamic_cast<CToolsBkgSoundPolygonArea*>(m_pDoc->m_pSelectedBkgArea);
										int oldPt = m_pDoc->m_nSelectedPt;
										if (m_pDoc->CheckSelectBkgArea(absPt) == SS_SELECTED_AREA_POINT)
										{
											if (m_pDoc->m_nSelectedPt != oldPt)
											{
												m_pDoc->CutSelectedPolygon(oldPt, m_pDoc->m_nSelectedPt);
												m_pDoc->m_nUpdateViewSign = UVS_BKG_SOUND_AREA_SHAPE;
												m_pDoc->UpdateAllViews(NULL);
												m_pDoc->m_nUpdateViewSign = 0;
											}
										}
										m_pDoc->m_nSelectedPt = -1;
										m_pDoc->m_pSelectedBkgArea = pPolygonArea;
									}
									break;
								default:
									break;
								}
								break;
							}
							m_pDoc->m_nSelectedPt = -1;
						}
					}

					break;
				case TS_CIRCLE:
					{
						Nuclear::XPCircle* pCircle = dynamic_cast<Nuclear::XPCircle*>(m_pDoc->m_pSelectedShape);
						if (!pCircle)
							break;
						pCircle->m_nRadius = static_cast<int>(sqrtf(static_cast<float>(absPt.Dis2(m_oldMousePoint))));
						m_pDoc->m_nUpdateViewSign = UVS_BKG_SOUND_AREA_SELECTED;
						if (pCircle->m_nRadius >= 10)
						{
							CToolsBkgSoundCircleArea* pCircleArea = new CToolsBkgSoundCircleArea();
							CSoundEditorDoc::SetShape(pCircleArea, pCircle);
							m_pDoc->ResetAreaRegion(pCircleArea);
							m_pDoc->AddAnBkgArea(pCircleArea);
							m_pDoc->m_pSelectedBkgArea = pCircleArea;
							m_pDoc->m_nUpdateViewSign = m_pDoc->m_nUpdateViewSign | UVS_BKG_SOUND_AREA_SHAPE;
						}
						delete pCircle;
						m_pDoc->m_pSelectedShape = NULL;
						m_pDoc->UpdateAllViews(NULL);
						m_pDoc->m_nUpdateViewSign = 0;
					}
					break;
				case TS_POLYGON:
					break;
				}
				break;
			}
		}
		break;
	default:
		break;
	}
	m_pDoc->m_movingSelectArea.x = 0;
	m_pDoc->m_movingSelectArea.y = 0;
	CDialog::OnLButtonUp(nFlags, point);
}


void CMainDlg::OnTool(UINT nID)
{
	m_toolState = static_cast<TOOLS_STATE>(nID - ID_TOOL_MOVE);
	if (m_toolState == TS_SELECT && m_pDoc)
		m_pDoc->m_nPolygonEditState = 0;
}

void CMainDlg::OnUpdateTool(CCmdUI* pCmdUI)
{
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
	switch (m_toolState)
	{
	case TS_MOVE:
		break;
	case TS_SELECT:
		switch(nChar)
		{
		case VK_DELETE:
			switch (m_pDoc->GetEditState())
			{
			case EDIT_STATE_ENV_SOUND:
			case EDIT_STATE_BKG_MUSIC:
			case EDIT_STATE_BKG_VOL:
				m_pDoc->DeleteSelectedArea();
				break;
			}
		}
		break;
	case TS_CIRCLE:
		break;
	case TS_POLYGON:
		switch (nChar)
		{
		case VK_ESCAPE:
			switch (m_pDoc->GetEditState())
			{
			case EDIT_STATE_ENV_SOUND:
			case EDIT_STATE_BKG_MUSIC:
			case EDIT_STATE_BKG_VOL:
				if (m_pDoc->m_pSelectedShape)
				{
					delete m_pDoc->m_pSelectedShape;
					m_pDoc->m_pSelectedShape = NULL;
				}
				break;
			}
		}
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

void CMainDlg::OnMazeInfo()
{
	CMazeInfo dlg;
	dlg.ingoreColors = false;
	dlg.colors = theApp.GetMazeColors();
	if (dlg.DoModal() == IDOK)
	{
		std::wstring oldMaze = m_pDoc->m_mazeName;
		m_pDoc->ResetSetMazeName();
		m_pDoc->m_SpriteMazeMask = dlg.spriteMask;
		if (oldMaze != m_pDoc->m_mazeName)
		{
			Nuclear::XBuffer buffer;
			m_pDoc->GetEngineBase()->GetFileIOManager()->GetFileImage(L"/map/" + m_pDoc->m_mazeName, buffer);
			m_pWorld->SetMapMaze(buffer.constbegin(), buffer.size());
		}
		m_pWorld->SetMazeColors(dlg.colors);
		theApp.ResetMazeColors();
		if (m_soundApp.GetHero())
		{
			m_soundApp.GetHero()->SetMoveMask(m_pDoc->m_SpriteMazeMask);
		}
	}
}

void CMainDlg::ShowMaze(bool b)
{
	if (b)
	{
		if (m_pDoc->m_mazeName == L"errormaze")
		{
			OnMazeInfo();
		}
		if (m_pDoc->m_mazeName == L"errormaze")
		{
			return;
		}
		Nuclear::XPMazeColors::const_iterator it = theApp.GetMazeColors().begin(), 
			ie = theApp.GetMazeColors().end();
		unsigned int mask = 0;
		for (;it!=ie;++it)
		{
			mask |= it->mask;
		}
		m_pEngine->GetEnv()->SetMapMazeMask(mask);
		m_pWorld->SetMazeColors(theApp.GetMazeColors());
	} else {
		m_pEngine->GetEnv()->SetMapMazeMask(0);
	}
	//m_pEngine->GetEnv()->ShowMapGrid(b);
	m_pDoc->m_bDrawMaze = b;
	
}