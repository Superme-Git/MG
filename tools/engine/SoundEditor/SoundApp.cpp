#include "StdAfx.h"
#include "SoundApp.h"
#include "Resource.h"
#include "..\engine\common\pfsutil.h"
#include "..\engine\map\pstepsoundmap.h"
#include "..\engine\soundarea\stepsoundtype.h"
#include "..\engine\soundarea\xpenvsoundcirclearea.h"
#include "..\engine\soundarea\xpenvsoundpolygonarea.h"
#include "..\engine\world\regionmap.h"



SoundApp::SoundApp(void) : m_pEngine(NULL), m_editState(EDIT_STATE_ENV_SOUND), m_pHero(NULL), m_pDoc(NULL), m_bDrawGird(false), m_bInited(false)
{
}

SoundApp::~SoundApp(void)
{
}

void SoundApp::Reset()
{
	m_editState = EDIT_STATE_ENV_SOUND;
	RemoveAllSprite();
}

void SoundApp::EnableNightEffect(bool b)
{
	m_pEngine->GetEnv()->SetRenderNightEffectByShader(b);
}

void SoundApp::SetNightCycle(int cycle)
{
	m_pEngine->GetWorld()->SetGameTimeCycle(cycle);
}

void SoundApp::SetGameTime(float t)
{
	m_pEngine->GetWorld()->SetGameTime(t);
}

void SoundApp::SetEnvVolume(unsigned char v)
{
	m_pEngine->SetSoundVolume(1, v / 100.0f);
}

void SoundApp::EnableEnv(bool b)
{
	m_pEngine->EnableSound(1, b);
}

void SoundApp::SetBkgVolume(unsigned char v)
{
	m_pEngine->SetSoundVolume(0, v / 100.0f);
}

void SoundApp::EnableBkg(bool b)
{
	m_pEngine->EnableSound(0, b);
}

void SoundApp::SetState(EDIT_STATE state)
{
	if (state != m_editState)
	{
		SpriteMap::iterator iter = m_spriteMap.begin();
		XPASSERT(m_pEngine);
		Nuclear::IWorld* pWorld = m_pEngine->GetWorld();
		if (!pWorld)
			return;
		m_editState = state;
		switch(state)
		{
		case EDIT_STATE_ENV_SOUND:
		case EDIT_STATE_BKG_MUSIC:
		case EDIT_STATE_BKG_VOL:
		case EDIT_STATE_STEP:
			RemoveAllSprite();
			EnableNightEffect(false);
			m_pEngine->StopTypeSound(1);
			m_pEngine->SetBGSoundMode(Nuclear::XPBGSM_CUSTOM);
			break;
		case EDIT_STATE_TESTING:
			if (m_pHero == NULL)
			{				
				Nuclear::ISprite* pXPSprite = pWorld->NewSprite(m_pDoc->GetModelName());
				Sprite* pSprite = new Sprite(pXPSprite);
				Nuclear::CRECT vp = pWorld->GetViewport();
				pSprite->SetLocation(vp.Center());
				m_pHero = pSprite;

				m_spriteMap[pXPSprite] = pSprite;
				pWorld->AttachCameraTo(pXPSprite);
				int layerCount = m_pDoc->GetCurSpriteLayerCount();
				for (int i=0;i<layerCount;++i)
				{
					pSprite->SetComponent(i, m_pDoc->GetComponent(i));
				}
				pSprite->SetRunLeft(m_pDoc->GetRunLeftAct());
				pSprite->SetRunRight(m_pDoc->GetRunRightAct());
				pSprite->SetStand(m_pDoc->GetStandAct());
				pSprite->SetDefaultAction(m_pDoc->GetStandAct(), false);
				pSprite->SetRideName(m_pDoc->GetRideName());
				pSprite->SetMoveMask(m_pDoc->m_SpriteMazeMask);
			}
			m_pHero->EnableStepSound(false);
			m_pHero->EnableStepSound(true);
			m_mouseRect.left = 0;
			m_mouseRect.top = 0;
			m_mouseRect.bottom = 0;
			m_mouseRect.right = 0;
			XMLIO::CFileWriter fw;
			XMLIO::CONode oRoot;
			fw.GetRootNode(oRoot);
			m_pDoc->SaveAsXML(oRoot, Nuclear::XPSOUND_IO_NORMAL);
			XMLIO::CFileReader fr;
			fw.GetAsReader(fr);
			XMLIO::CINode iRoot;
			fr.GetRootNode(iRoot);
			//脚步
			pWorld->SetSoundAreas(iRoot, m_pDoc->CloneSSM());
			fr.CloseFile();
			EnableNightEffect(m_pDoc->m_bTestingEnableDark);
			m_pEngine->SetBGSoundMode(Nuclear::XPBGSM_AUTO);
			break;
		}
	}
}

// 主线程主循环中每帧调用，可处理客户端中与渲染无关的日常工作，如处理协议等
void SoundApp::OnTick(int now, int delta)
{

}

// 引擎每帧渲染之前调用，用于客户端更新渲染相关数据，返回false则不渲染，可用此函数内wait来控制帧率
bool SoundApp::OnBeforeRender(int now/*ms*/)
{
	Nuclear::CRECT rect = m_pEngine->GetWorld()->GetViewport();
	m_pDoc->UpdateViewport(CPoint(rect.left, rect.top), US_ENGINE);
	if (m_editState == EDIT_STATE_TESTING)
	{
		m_pDoc->SetGameTime(m_pEngine->GetWorld()->GetTempGameTime(), US_ENGINE);
	}
	return true;
}

// 当精灵状态发生改变时，回调这个函数更新动作。
void SoundApp::OnUpdateSpriteAction(Nuclear::ISprite * sprite, Nuclear::XPUSA_TYPE type)
{
	SpriteMap::iterator it = m_spriteMap.find(sprite);
	if (it != m_spriteMap.end())
	{
		it->second->UpdateAction(type);
	}
}

bool SoundApp::OnExit()
{
	if (!m_bInited)
		return true;
	SpriteMap::iterator iter = m_spriteMap.begin();
	for (;iter!=m_spriteMap.end();++iter)
	{
		delete iter->second;
	}
	m_spriteMap.clear();
	return true;
}

bool SoundApp::OnInit(int step) 
{
	m_pEngine = Nuclear::GetEngine();
	m_pDoc->SetPEB(dynamic_cast<Nuclear::EngineBase*>(m_pEngine));//直接黑出来了。。。。。
	Nuclear::IEnv* pEnv = m_pEngine->GetEnv();
	pEnv->SetConsoleInfo(Nuclear::XPCSINFO_ALL, false);
	pEnv->SetEnableSurfaceCache(true);
	pEnv->SetFrameStateInfo(Nuclear::XPFRAMESTAT_FRAME_TIME, false);
	pEnv->SetFrameStateInfo(Nuclear::XPFRAMESTAT_CAMERA_DIS, false);
	pEnv->SetFrameStateInfo(Nuclear::XPFRAMESTAT_FILE_IO, false);
	pEnv->SetFrameStateInfo(Nuclear::XPFRAMESTAT_TASK_TIME, false);
	pEnv->SetFrameStateInfo(Nuclear::XPFRAMESTAT_IAPP_MSG, false);
	pEnv->SetRenderNightEffectByShader(true);
	pEnv->SetSmoothMove(false);
	pEnv->SetRenderNightEffectByShader(false);

	Nuclear::Renderer* pRenderer = m_pEngine->GetRenderer();
	HRSRC hRsrc = FindResource(AfxGetResourceHandle(), MAKEINTRESOURCE(IDR_PNG_AREA), L"PNG");
	if (hRsrc == NULL)
		AfxMessageBox(L"查找资源出错！");
	else {
		HGLOBAL hData = LoadResource(AfxGetResourceHandle(), hRsrc);
		if (hData == NULL)
			AfxMessageBox(L"加载资源出错！");
		else {
			LPVOID pData = LockResource(hData);
			int size = SizeofResource(AfxGetResourceHandle(), hRsrc);;
			m_AreaHandle = pRenderer->LoadPictureFromMem(pData,  size);
			UnlockResource(hData);
			FreeResource(hData);
		}
	}

	m_bInited = true;
	return true;
}

void SoundApp::RemoveAllSprite()
{
	m_pHero = NULL;
	SpriteMap::iterator iter = m_spriteMap.begin();
	XPASSERT(m_pEngine);
	Nuclear::IWorld* pWorld = m_pEngine->GetWorld();
	if (!pWorld)
		return;
	pWorld->DeleteAllSprite();
	for (;iter!=m_spriteMap.end();++iter)
	{
		delete iter->second;
	}
	m_spriteMap.clear();
}

//画区域
void SoundApp::DrawArea(const CToolsSoundArea *pArea, const Nuclear::CRECT& vp, 
						   Nuclear::Renderer* pRenderer, bool isSelected)
{
	pArea->DrawArea(vp, pRenderer, isSelected);
	Nuclear::CPOINT pt = pArea->GetPos();
	Nuclear::FRECT rect(pt.x - vp.left - 32.0f, pt.y - vp.top - 64.0f, 0, 0);
	rect.right = rect.left + 64;
	rect.bottom = rect.top + 64;
	pRenderer->DrawPicture(m_AreaHandle, rect, 0xFFFFFFFF);
}

void SoundApp::OnRenderUI(int now)
{
	if (!m_bInited)
		return;
	//这里可以考虑优化，如果全部画区域太慢的话
	if (m_pDoc)
	{
		Nuclear::Renderer* pRenderer = m_pEngine->GetRenderer();
		Nuclear::CRECT vp = m_pEngine->GetWorld()->GetViewport();
		bool drawEnv = m_pDoc->GetEditState() == EDIT_STATE_ENV_SOUND;
		if (m_pDoc->GetEditState() == EDIT_STATE_TESTING)
			drawEnv = (m_pDoc->m_bTestingDrawEnvArea == TRUE);
		if (drawEnv)
		{
			const EnvAreaCont& envAreas = m_pDoc->GetEnvArea();
			EnvAreaCont::const_iterator eIt = envAreas.begin(), eIe = envAreas.end();
			CToolsEnvSoundArea* pArea;
			for (;eIt!=eIe;++eIt)
			{
				pArea = *eIt;
				if (pArea != m_pDoc->m_pSelectedEnvArea)
				{
					DrawArea(pArea, vp, pRenderer, false);
				}
			}
			if (m_pDoc->m_pSelectedShape)
			{
				m_pDoc->m_pSelectedShape->Render(pRenderer, vp, 0xFFFF0000, Nuclear::XPFM_WIREFRAME, CToolsEnvSoundArea::CHECK_CLICK_RADIUS);
			} else if (m_pDoc->m_pSelectedEnvArea) {
				vp.left -= m_pDoc->m_movingSelectArea.x;
				vp.top -= m_pDoc->m_movingSelectArea.y;
				DrawArea(m_pDoc->m_pSelectedEnvArea, vp, pRenderer, true);			
			}
			if (m_pDoc->m_pSelectedEnvArea && m_pDoc->m_pSelectedEnvArea->GetType() == Nuclear::XPAREA_TYPE_POLYGON && m_pDoc->m_nPolygonEditState == 2)
			{
				if (m_pDoc->m_nSelectedPt != -1)
				{
					Nuclear::FRECT line;
					line.left = static_cast<float>(m_pDoc->m_CutingPolygonLine.left - vp.left);
					line.right = static_cast<float>(m_pDoc->m_CutingPolygonLine.right - vp.left);
					line.top = static_cast<float>(m_pDoc->m_CutingPolygonLine.top - vp.top);
					line.bottom = static_cast<float>(m_pDoc->m_CutingPolygonLine.bottom - vp.top);
					pRenderer->DrawLine(line, 0xFFFFFFFF);
				}
			}
		}

		bool drawBkg = m_pDoc->GetEditState() == EDIT_STATE_BKG_MUSIC;
		if (m_pDoc->GetEditState() == EDIT_STATE_TESTING)
			drawBkg = (m_pDoc->m_bTestingDrawBkgArea == TRUE);
		if (drawBkg)
		{
			const BkgAreaCont& bkgAreas = m_pDoc->GetBkgArea();
			BkgAreaCont::const_iterator bIt = bkgAreas.begin(), bIe = bkgAreas.end();
			CToolsBkgSoundArea* pArea;
			for (;bIt!=bIe;++bIt)
			{
				pArea = *bIt;
				if (pArea != m_pDoc->m_pSelectedBkgArea)
				{
					DrawArea(pArea, vp, pRenderer, false);
				}
			}
			if (m_pDoc->m_pSelectedShape)
			{
				m_pDoc->m_pSelectedShape->Render(pRenderer, vp, 0xFFFF0000, Nuclear::XPFM_WIREFRAME, CToolsEnvSoundArea::CHECK_CLICK_RADIUS);
			} else if (m_pDoc->m_pSelectedBkgArea) {
				vp.left -= m_pDoc->m_movingSelectArea.x;
				vp.top -= m_pDoc->m_movingSelectArea.y;
				DrawArea(m_pDoc->m_pSelectedBkgArea, vp, pRenderer, true);			
			}
			if (m_pDoc->m_pSelectedBkgArea && m_pDoc->m_pSelectedBkgArea->GetType() == Nuclear::XPAREA_TYPE_POLYGON && m_pDoc->m_nPolygonEditState == 2)
			{
				if (m_pDoc->m_nSelectedPt != -1)
				{
					Nuclear::FRECT line;
					line.left = static_cast<float>(m_pDoc->m_CutingPolygonLine.left - vp.left);
					line.right = static_cast<float>(m_pDoc->m_CutingPolygonLine.right - vp.left);
					line.top = static_cast<float>(m_pDoc->m_CutingPolygonLine.top - vp.top);
					line.bottom = static_cast<float>(m_pDoc->m_CutingPolygonLine.bottom - vp.top);
					pRenderer->DrawLine(line, 0xFFFFFFFF);
				}
			}
		}

		bool drawBkgVol = m_pDoc->GetEditState() == EDIT_STATE_BKG_VOL;
		if (m_pDoc->GetEditState() == EDIT_STATE_TESTING)
			drawBkgVol = (m_pDoc->m_bTestingDrawBkgVolArea == TRUE);
		if (drawBkgVol)
		{
			const BkgVolAreaCont& bkgVolAreas = m_pDoc->GetBkgVolArea();
			BkgVolAreaCont::const_iterator eIt = bkgVolAreas.begin(), eIe = bkgVolAreas.end();
			CToolsBkgVolumeArea* pArea;
			for (;eIt!=eIe;++eIt)
			{
				pArea = *eIt;
				if (pArea != m_pDoc->m_pSelectedBkgVolArea)
				{
					DrawArea(pArea, vp, pRenderer, false);
				}
			}
			if (m_pDoc->m_pSelectedShape)
			{
				m_pDoc->m_pSelectedShape->Render(pRenderer, vp, 0xFFFF0000, Nuclear::XPFM_WIREFRAME, CToolsEnvSoundArea::CHECK_CLICK_RADIUS);
			} else if (m_pDoc->m_pSelectedBkgVolArea) {
				vp.left -= m_pDoc->m_movingSelectArea.x;
				vp.top -= m_pDoc->m_movingSelectArea.y;
				DrawArea(m_pDoc->m_pSelectedBkgVolArea, vp, pRenderer, true);			
			}
			if (m_pDoc->m_pSelectedBkgVolArea && m_pDoc->m_pSelectedBkgVolArea->GetType() == Nuclear::XPAREA_TYPE_POLYGON && m_pDoc->m_nPolygonEditState == 2)
			{
				if (m_pDoc->m_nSelectedPt != -1)
				{
					Nuclear::FRECT line;
					line.left = static_cast<float>(m_pDoc->m_CutingPolygonLine.left - vp.left);
					line.right = static_cast<float>(m_pDoc->m_CutingPolygonLine.right - vp.left);
					line.top = static_cast<float>(m_pDoc->m_CutingPolygonLine.top - vp.top);
					line.bottom = static_cast<float>(m_pDoc->m_CutingPolygonLine.bottom - vp.top);
					pRenderer->DrawLine(line, 0xFFFFFFFF);
				}
			}
		}

		bool drawStep = m_pDoc->GetEditState() == EDIT_STATE_STEP;
		if (m_pDoc->GetEditState() == EDIT_STATE_TESTING)
			drawStep = (m_pDoc->m_bTestingDrawStep == TRUE);
		if (drawStep && m_pDoc->GetSSM())
		{
			const Nuclear::StepSoundGT* pData = m_pDoc->GetSSM()->GetData();
			Nuclear::CRECT vp = m_pEngine->GetWorld()->GetViewport();	//vp可能会在画SelectedArea那里改了
			CPoint gridsize(Nuclear::PStepSoundMap::GRID_WIDTH, Nuclear::PStepSoundMap::GRID_HEIGHT);
			if (pData)
			{
				Nuclear::XPSSGColorMap &cm = m_pDoc->GetEngineBase()->GetStepSoundType()->GetGroundColor();
				const Nuclear::StepSoundGT* pTmpData = NULL;
				int xAdj = vp.left % gridsize.x;
				int yAdj = vp.top % gridsize.y;
				int width = m_pDoc->GetMapWidth() / gridsize.x;
				int height = m_pDoc->GetMapHeight() / gridsize.y;
				int x = vp.right / gridsize.x;
				if (x >= width)
				{
					x = width - 1;
				}
				int y = vp.bottom / gridsize.y;
				if (y >= height)
				{
					y = height - 1;
				}
				int ci = 0, cj = 0, i = 0;
				Nuclear::FRECT tmpRect;
				Nuclear::XPCOLOR color;
				for (int j=vp.top / gridsize.y; j<=y ; ++j, ++cj) 
				{
					ci = 0;
					i = vp.left / gridsize.x;
					pTmpData = pData + j * width + i;
					for ( ; i<=x ; ++i, ++ci, ++pTmpData)
					{
						
						if (!m_pDoc->IsStepVisable(*pTmpData))
							continue;
						color = cm[*pTmpData];
						color.a = 0x80;
						if (color.data == 0x80FFFFFF)
							continue;
						tmpRect.left = static_cast<float>(ci*gridsize.x - xAdj);
						tmpRect.top =  static_cast<float>(cj*gridsize.y - yAdj);
						tmpRect.right = static_cast<float>((ci+1)*gridsize.x - xAdj);
						tmpRect.bottom = static_cast<float>((cj+1)*gridsize.y - yAdj);
						pRenderer->DrawBox(tmpRect, color);
					}
				}
			}
			DrawGrid(vp.GetPos(), gridsize);
		}
		if (m_bDrawGird)
		{
			Nuclear::CRECT vp = m_pEngine->GetWorld()->GetViewport();	//vp可能会在画SelectedArea那里改了
			CPoint gridsize(Nuclear::RegionMap::REGION_WIDTH, Nuclear::RegionMap::REGION_HEIGHT);
			DrawGrid(vp.GetPos(), gridsize);
		}
		if (m_pDoc->GetEditState() == EDIT_STATE_STEP)
		{
			Nuclear::FRECT tmpRect(static_cast<float>(m_mouseRect.left - vp.left),
				static_cast<float>(m_mouseRect.top - vp.top), 
				static_cast<float>(m_mouseRect.right - vp.left), 
				static_cast<float>(m_mouseRect.bottom - vp.top));
			pRenderer->DrawBox(tmpRect, 0xFFFFFFFF, Nuclear::XPFM_WIREFRAME);
		}
	}	
}

void SoundApp::DrawGrid(const POINT& vplt, const POINT& gridsize)
{
	Nuclear::Renderer* pRenderer = m_pEngine->GetRenderer();
	int addX = vplt.x % gridsize.x;
	int addY = vplt.y % gridsize.y;
	float x1, y1, x2, y2;
	y1 = 0.0f;
	y2 = 768.0f;
	int col = 1024 / gridsize.x;
	int row = 768 / gridsize.y;
	for (int x = 0; x <= col; ++x)
	{
		x2 = x1 = static_cast<float>(x * gridsize.x - addX);
		pRenderer->DrawLine(x1, y1, x2, y2, 0xFF000000);
	}
	x1 = 0.0f;
	x2 = 1024.0f;
	for (int y = 0; y <= row; ++y)
	{
		y1 = y2 = static_cast<float>(y * gridsize.y - addY);
		pRenderer->DrawLine(x1, y1, x2, y2, 0xFF000000);
	}
}

void SoundApp::OnDrawGird()
{
	m_bDrawGird = !m_bDrawGird;
}

void SoundApp::OnUpdateDrawGird(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck(m_bDrawGird);
}

void SoundApp::UpdateViewport(CPoint pt)
{
	Nuclear::IWorld *pWorld = m_pEngine->GetWorld();
	switch(m_editState)
	{
	case EDIT_STATE_ENV_SOUND:
	case EDIT_STATE_BKG_MUSIC:
	case EDIT_STATE_BKG_VOL:
	case EDIT_STATE_STEP:
		pWorld->SetViewportLT(pt.x, pt.y);
		break;
	case EDIT_STATE_TESTING:
		pt.x += 512;
		pt.y += 384;
		m_pHero->GoToRun();
		m_pHero->MoveTo(pt.x, pt.y, 0, NULL);
		break;
	}
}