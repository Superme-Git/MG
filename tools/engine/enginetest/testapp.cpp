#include <stdlib.h>
#include <time.h>
#include <vector>
#include <set>
#include <map>
#include <tchar.h>
#include "testapp.h"

#include "..\engine\isprite.h"

#include "..\gxwindow\gxw.h"
#include "..\gxwindow\projects\myframe.h"

#include "..\engine\common\xptypes.h"
#include "..\engine\common\log.h"
#include "..\engine\common\util.h"
#include "..\renderer\GXWRenderer.h"
#include "..\engine\renderer\renderer.h"
#include "..\engine\renderer\ifontmanager.h"

//#include <vld.h>

Nuclear::CRECT g_rect;
std::vector<Nuclear::CRECT> g_rects;

GXWRENDER::CGXWRenderer* g_pGxwRenderer = NULL;
CMyFrame* pMyFrame = NULL;

void g_genrects()
{
	g_rect.left = 50+rand()%50;
	g_rect.top = 50+rand()%50;
	g_rect.right = g_rect.left + rand()%400 + 100;
	g_rect.bottom = g_rect.top + rand()%400 + 100;

	g_rects.clear();
	PartitionRectToPower2(g_rect, g_rects);
}

Nuclear::TextBlockHandle hTB = Nuclear::INVALID_TEXTBLOCK_HANDLE;
Nuclear::PictureHandle capHandle = Nuclear::INVALID_PICTURE_HANDLE;
bool isRendeCap = false;
TestApp::TestApp() 
: pEffect(NULL)//, pToolsTitleMan(NULL)
{
	//m_strInitMapName = L"q_10";
	pEngine = NULL;
	m_strInitMapName = L"q_2";
	m_strInitMazeName = L"maze/118.dat";
}

// 加载地图的回调
bool TestApp::LoadingMapNotify(int pro)
{
	XPTRACE(L"loading map %d%%\n", pro);
	return true;
}
#include <hash_set>

Sprite* TestApp::NewSprite(const std::wstring &model, bool attach)
{
	Nuclear::ISprite* pXPSprite = pWorld->NewSprite(model);
	Nuclear::CPOINT pt = pXPSprite->GetEffectPos();
	Sprite* pSprite = new Sprite(pXPSprite);
	m_Sprites[pXPSprite] = pSprite;
	if (attach)
		pWorld->AttachCameraTo(pXPSprite);
	return pSprite;
}
bool TestApp::OnInit(int step) 
{ 	
	if (step == 1)
		return true;
	srand(time(NULL));
	pEngine = Nuclear::GetEngine();
//	pToolsTitleMan = new Nuclear::ToolsTitleManager(pEngine);
//	pEngine->SetEntitativeTitleManager(pToolsTitleMan);

	
	Nuclear::IEnv* theEnv = pEngine->GetEnv();
	//pEngine->GetEnv()->SetRenderSpriteShadow(false);
	//pEngine->GetEnv()->SetRenderAlphaSprite(false);

	Nuclear::XDisplayMode dm = theEnv->GetDisplayMode();

	Nuclear::IFontManager *pFontMan = pEngine->GetRenderer()->GetFontManager();
	
	pFontMan->AddFontType( 0, L"font/fzl2jw.ttf", 32); //测试Freetype效果的文本123456789
	pFontMan->AddFontType(1, L"font\\FZZYGBK.TTF", 14);
	//hTB = pFontMan->NewText( L"abcdefg这是一行测试Freetype效果的文本123456789", 0, 0xffffffff, 0xffff0000, 0x00000000, false);
	
	//Nuclear::ITextBlock* ptb = pEngine->GetRenderer()->GetFontManager()->GetTextBlock( hTB);
	//if ( ptb)
		//ptb->SetShadowColor( 0x88000000);
	//	ptb->SetStrokeColor( 0xff00ff00);
	
	
	GXWINDOW::GXWInitialize( theEnv->GetWindow(), dm.width, dm.height);
	g_pGxwRenderer = new GXWRENDER::CGXWRenderer( pEngine->GetRenderer());
	//GXWRENDER::CGXWRenderer::InitializeImages( pEngine->GetRenderer());
	//GXWINDOW::GXWSetDefaultRenderer( new GXWRENDER::CGXWRenderer( pEngine->GetRenderer()));

	Nuclear::Renderer *tpRenderer = pEngine->GetRenderer();
	Nuclear::PSParameter param;
	param.autorender = true;
	param.layer = 1;
	param.m_psModifyParam.m_pointCent.x = 400;
	param.m_psModifyParam.m_pointCent.y = 300;
	param.m_psModifyParam.m_pointObject.x = 100;
	param.m_psModifyParam.m_pointObject.y = 100;
	param.time = 5000;
	//Nuclear::ParticleSystemHandle pshandle = tpRenderer->GetParticleManager()->CreateParticleSystem(L"particleSysLibrary_004",param);
	
	//theEnv->SetPrintFPSLog(true);
	//theEnv->SetEnableSurfaceCache(false);

	pEngine->GetEnv()->SetEnableWade(false);
	//pEngine->GetEnv()->SetEnableWade(false);
	pEngine->EnableSound(0, true);
	pEngine->EnableSound(1, true);
	pEngine->GetEnv()->SetControlFPS(true);
	theEnv->SetInactiveSoundFadeOutTime(10000);
	theEnv->SetActiveSoundFadeInTime(2000);

	



	pWorld = pEngine->GetWorld();
	
	m_pHero = NewSprite(L"male", true);
	m_pHero->SetComponent(0/*body*/, L"10jnanbupao");
	m_pHero->SetComponent(1/*head*/, L"xuanrennantou0");
	m_pHero->SetComponent(3/*weapon*/, L"10jdnandao");	
	m_pHero->SetComponent(6/*weaponfx*/, L"10jdnandao", 0xFFFFFFff);	

	pEngine->SetEngineSpriteLoc(esh, Nuclear::CPOINT(100, 200));
	pEngine->SetEngineSpriteDirection(esh, Nuclear::XPDIR_BOTTOM);

	Nuclear::XPLoadmapParam lmParam;
	lmParam.objLoc.x = 1000;
	lmParam.objLoc.y = 500;
	pWorld->LoadMap( m_strInitMapName, m_strInitMazeName, &lmParam);

	pEngine->GetEnv()->ShowCursor(false);
	hArrowCursor = pEngine->GetEnv()->RegisterCursor(L"res\\image\\UI\\Arrow.cur");
	hAppStartingCursor = pEngine->GetEnv()->RegisterCursor(L"res\\image\\UI\\AppStarting.ani");
	pEngine->GetEnv()->ChangeCursor(hArrowCursor);
	pEngine->GetEnv()->SetRenderNightEffectByShader(true);

	speed = 300;

	pMyFrame = new CMyFrame;
	XPTRACE(L"after loading map\n");
//	CAppleFrame* pMyFrame = new CAppleFrame;
	XPTRACE(L"after loading map\n");
	//pEngine->ScheduleTimer(this, 1000);

	Sprite *pSprite = NewSprite(L"muzhuang");
	

//	pMyFrame->Show();


	return true; 
}

void TestApp::OnUpdateSpriteAction(Nuclear::ISprite * sprite, Nuclear::XPUSA_TYPE type) // 当精灵状态发生改变时，回调这个函数更新动作。
{
	SpriteMap::iterator it = m_Sprites.find(sprite);
	if (it != m_Sprites.end())
	{
		if (!it->second->UpdateAction(type))
		{
			delete it->second;
			pWorld->DeleteSprite(it->first);
			m_Sprites.erase(it);
		}
	}
}

void TestApp::OnTick(int now, int delta)
{
	GXWINDOW::GXTimerUpdate();
}

void TestApp::OnRenderInit(int now, int step, int totalstep)
{
	
	if (!pEngine)
	{
		TCHAR szbuffer[256];
		wsprintf(szbuffer, _T("TTTTTTTTTTTTTTTT......"));
		RECT rct;
		rct.left = 0;
		rct.top = 50;
		rct.right = 300;
		rct.bottom = 80;
		Nuclear::GetEngine()->GetRenderer()->DrawText(szbuffer, 30, &rct, 0xff00ff00);
		return;
	}
}

void TestApp::OnRenderUI(int now)
{
	GXWINDOW::GXWUpdate();
	//Nuclear::GetEngine()->GetRenderer()->GetParticleManager()->RenderParticleSystem(1);

	//Nuclear::GetEngine()->GetRenderer()->GetFontManager()->DrawText( hTB, 0, 100, NULL);
	// 同一文本画100行测绘制效率
	for ( int i = 0; i < 1; ++i)
		Nuclear::GetEngine()->GetRenderer()->GetFontManager()->DrawText( hTB, 0, i * 2, NULL);

//	Nuclear::GetEngine()->GetRenderer()->GetFontManager()->DrawText( hTB, 0, 100, NULL);

	int cnt = Nuclear::GetEngine()->GetRenderer()->GetParticleManager()->GetTotalNumActiveParticles();

	TCHAR szbuffer[256];
	wsprintf(szbuffer, _T("TotalNumActiveParticles: %5d"), cnt);
	RECT rct;
	rct.left = 0;
	rct.top = 50;
	rct.right = 300;
	rct.bottom = 80;
	Nuclear::GetEngine()->GetRenderer()->DrawText(szbuffer, 30, &rct, 0xff00ff00);
	//Nuclear::GetEngine()->GetRenderer()->DrawPicture(cursorHandle, Nuclear::FRECT(80.0f, 50.0f, 80 + 32.0f, 50 + 32.0f), 0xFFFFFFFF);

	/*
	Nuclear::GetEngine()->GetRenderer()->DrawBox(g_rect.ToFRECT(), 0xffff0000,Nuclear::XPFM_WIREFRAME);
	for(int i=0; i<(int)g_rects.size(); ++i)
	{
		Nuclear::CRECT tr = g_rects[i];
		tr += g_rect.GetPos();
		tr += Nuclear::CPOINT(5,5);
		Nuclear::GetEngine()->GetRenderer()->DrawBox(tr.ToFRECT(), 0xff00ff00,Nuclear::XPFM_WIREFRAME);
	}
	*/
	//pEngine->RendererEngineSprite(esh);
	if (isRendeCap && capHandle != Nuclear::INVALID_PICTURE_HANDLE)
	{
		Nuclear::FRECT rect(0.0f, 0.0f, 800.0f, 600.0f);
		pEngine->GetRenderer()->DrawPicture(capHandle, rect, 0xFFFFFFFF);
	}
}


bool TestApp::OnExit() 
{ 
//	pToolsTitleMan->ClearAllRes();

	if (!pEngine)
		return true;
	pMyFrame->Hide();
	delete pMyFrame;

	GXWINDOW::GXWUninitialize();
	delete g_pGxwRenderer;

	return true; 
}

Nuclear::IEffect *pEffect = NULL;

// windows 消息
// 返回值应该是XPWinMSGFuncSign枚举的按位或
Nuclear::XPWinMSGFuncResult TestApp::OnWindowsMessage( HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam ) 
{
	if (!pEngine)
		return Nuclear::XPWINMSG_OK; 
	if( GXWINDOW::GXWMessage( hWnd, msg, wParam, lParam))
		return Nuclear::XPWINMSG_OK;

	//Nuclear::Location loc = pSprite->GetLocation();
	//Nuclear::ISprite* sp;
	
	switch( msg )
	{
	case WM_SETCURSOR:
		{
			Nuclear::Location loc;
			pEngine->GetEnv()->GetCursorPosition(loc);
			Nuclear::CRECT cp = pWorld->GetViewport();
			loc.x += cp.left;
			loc.y += cp.top;
			std::vector<Nuclear::ISelectableObj*> v;
			pEngine->GetWorld()->SelectObjs(loc, v);
			if (v.size() > 0)
			{
				pEngine->GetEnv()->ChangeCursor(hAppStartingCursor);
			} else {
				pEngine->GetEnv()->ChangeCursor(hArrowCursor);
			}

		}
		
		break;
			
		case WM_RBUTTONDOWN:
			{
				Nuclear::Location loc;
				int y = HIWORD( lParam );
				int x = LOWORD( lParam );
				Nuclear::CRECT cp = pWorld->GetViewport();
				loc.x = cp.left + x;
				loc.y = cp.top + y;
				
				/*
				std::vector<Nuclear::ISprite*> sprites;
				if( pWorld->SelectSprites(loc, sprites) )
				{
					for(std::vector<Nuclear::ISprite*>::iterator it = sprites.begin(), ie = sprites.end(); it != ie; ++it)
					{
						(*it)->SetDirection(Nuclear::XPDIR_RIGHT);
						break;
					}
				}
				*/
				if (::GetKeyState(VK_LSHIFT) < 0)
				{
					pWorld->SetViewport(loc,true); 
					/*
					if( loc.x < 1000 )
						pWorld->SetViewport(Nuclear::Location(3000,1000),true);
					else
						pWorld->SetViewport(Nuclear::Location(0,1000),true);
					*/
				}
				else
				{
					// == pWorld->SetViewport(loc, false);
					//m_pHero->SetLocation(loc);
					m_pHero->TeleportWithBlur(loc, 100, 2000);
				}
				//pMine->SetLocation(loc+Nuclear::CPOINT(100,100));
			}
			break;

		case WM_LBUTTONDOWN:
			{
				
				Nuclear::Location loc;
				int y = HIWORD( lParam );
				int x = LOWORD( lParam );
				Nuclear::CRECT cp = pWorld->GetViewport();
				loc.x = cp.left + x;
				loc.y = cp.top + y;

			
				m_pHero->GoToRun();
				m_pHero->MoveTo(loc, 0, NULL);
				

				
			}
			break;
		case WM_MBUTTONDOWN:
			{
				Nuclear::Location loc;
				int y = HIWORD( lParam );
				int x = LOWORD( lParam );
				Nuclear::CRECT cp = pWorld->GetViewport();
				loc.x = cp.left + x;
				loc.y = cp.top + y;
				std::vector<Nuclear::ISelectableObj*> vet;
				pWorld->SelectObjs(loc, vet);
				break;
			}
			
	
		case WM_KEYDOWN:
		case WM_SYSKEYDOWN:
			switch( wParam)
			{
			case 'T':
				if (m_pHero->GetState() == 3)
				{					
					m_pHero->SetComponent(4/*horsehead*/, L"");
					m_pHero->SetComponent(5/*horsebody*/, L"");
					m_pHero->SetRideName(L"");
				} else {					
					m_pHero->SetComponent(4/*horsehead*/, L"1");
					m_pHero->SetComponent(5/*horsebody*/, L"1");
					m_pHero->SetRideName(L"1");
				}
				break;
			case VK_F1:
				
				break;
			
			case VK_F2:
				//pEngine->GetEnv()->SetDisplayMode(Nuclear::XDisplayMode(1280,800,32));
				//pEngine->PlayEffectSound(L"/sound/Windows XP 启动.ogg", 255, 128);
				pEngine->SetBGSoundMode(Nuclear::XPBGSM_CUSTOM);
				pEngine->PlayBGSound(L"/sound/music/1theme_a.ogg");
				
				
				break;
			case VK_F3:
				{
					pEngine->PlayEffectSound(L"/sound/ambience/sea_wave.ogg", 255, 128);
					
				}
				break;
			case VK_F4:
				{
					pEngine->PlayEffectSound(L"/sound/ambience/snowfield_night.ogg", 255, 128);
				}
				
				
				break;
			case VK_F5:
				{
					pEngine->PlayEffectSound(L"/sound/ambience/loess_day.ogg", 255, 128);
				}
				
				break;
			case VK_F6:
				{
					pEngine->GetEnv()->SetDisplayMode(Nuclear::XDisplayMode(1280, 1024, 32, Nuclear::XPSM_FADE_FULL_SCREEN));
				
				}
				break;

			case VK_F7:
				{
					pEngine->GetEnv()->SetDisplayMode(Nuclear::XDisplayMode(1280, 1024, 32, Nuclear::XPSM_REAL_FULL_SCREEN));
				
				}
				break;
				
			case VK_F8:
			
				break;
			case VK_F9:
				
				break;

			default:
				break;
			}
			break;
		
		}
	return Nuclear::XPWINMSG_OK; 
}


void TestApp::OnTimer()
{
	if (!m_pHero->IsMoving())
	{
		if ((rand() % 10) < 7)
		{
			if (m_pHero->GetState() != 4) 
				m_pHero->Attack();
		} else {
			if (m_pHero->GetState() == 4)
				m_pHero->StopAttack();
		}
	}
	int bornRate = 10, dieRate = 0;
	if (m_monsters.size() >= 20)
	{
		bornRate = 0;
		dieRate = 10;
	} else if (m_monsters.size() >= 10)
	{
		bornRate = 3;
		dieRate = 7;
	} else if (m_monsters.size() >= 5)
	{
		bornRate = 5;
		dieRate = 5;
	}
	Sprite *pSprite = NULL;
	if (rand() % 10 < bornRate)
	{
		pSprite = NewSprite(L"muzhuang");
		pSprite->SetComponent(0, L"murenzhuang");
		Nuclear::Location loc = m_pHero->GetLocation();
		loc.x += (rand() % 600) - 300;
		loc.y += (rand() % 400) - 200;
		pSprite->SetLocation(loc);
		m_monsters.push_back(pSprite);
	}
	if (rand() % 10 < dieRate)
	{
		pSprite = m_monsters.front();
		m_monsters.pop_front();
		pSprite->Die();
	}
}