#include "StdAfx.h"
#include "SoundApp.h"
#include "Resource.h"

#include "engine\renderer\ifontmanager.h"
#include "engine\common\worldlogiccoord.h"
#include "xmlio\xmlio.h"
#include "engine\common\xptypes.h"
#include "iostream"

#include "engine\common\util.h"
#include "engine\common\xmarshal.h"
#include "engine\iengine.h"

#include "fstream"
#include "MainFrm.h"
#include "PropView.h"
#include "SoundEditor.h"


using namespace std;

const wchar_t* strNpceType[5]=
{
	L"无",
	L"重要NPC",
	L"传送NPC",
	L"商业NPC",
	L"普通NPC"
};

const wchar_t* strDirType[8]=
{
	L"0 上",
	L"1 右上",
	L"2 右",
	L"3 右下",
	L"4 下",
	L"5 左下",
	L"6 左",
	L"7 左上"
};

SoundApp::SoundApp(void) : m_pEngine(NULL), m_editState(EDIT_STATE_MAZE), m_pHero(NULL), m_pDoc(NULL), m_bDrawGird(false), m_bDrawBlock(false)
{
	m_BlockRectList.clear();

	m_NpcList.resize(0);
	m_NpcBaseList.resize(0);
	m_vecJumpVector.clear();
	m_strNpcXmlName=L"";
	m_strGotoXmlName=L"";
	m_pCurSelNpc=NULL;
	m_pNpcDlg=NULL;
	m_pJPInfDlg=NULL;

	monKind  = -1;
	monIndex = -1;

	m_pEditNpc=NULL;
	m_selectJumpPointID=-1;
	m_EditJumpPointID=-1;
	m_bEngineInited=false;
	m_MazeBuffer=NULL;
	m_JumpBlockBuffer=NULL;
	m_IslandBuffer=NULL;
	m_IsLandNumber=0;
	m_IslandBuffer2=NULL;
	m_IsLandNumber2=0;

	wchar_t workdir[MAX_PATH];
	::GetCurrentDirectory(MAX_PATH, workdir);
	CString stringPath = workdir;
	m_EdgeSize = ::GetPrivateProfileInt(L"RegionParam", L"edgeSize", 10, stringPath + L"\\MazeAndNpcEditor.ini");
}

//-2015 Yao
void SoundApp::CallMonsterDlg()
{
	/*MonsterKind m_pMonsterDlg ;
	m_pMonsterDlg.DoModal();
	m_pMonsterDlg.GetMonsterKind(monKind);
	m_pMonsterDlg.GetMonsterIndex(monIndex);*/

	//-
	CMainFrame* pMainFrame=(CMainFrame*)AfxGetApp()->m_pMainWnd;
	if (pMainFrame!=NULL)
	{
		CPropView* pPropView=(CPropView*)pMainFrame->m_HSplitterWindow.GetPane(0,1);
		if (pPropView)
		{
			m_pMonPanel=pPropView->m_tabPropPanel.m_pMonPanel;
			m_pMonPanel->GetMonsterKind(monKind);
			m_pMonPanel->GetMonsterIndex(monIndex);
		}
	}
}

SoundApp::~SoundApp(void)
{
	RemoveAllBaseNpc();

	if (m_MazeBuffer!=NULL)
	{
		delete []m_MazeBuffer;
	}

	if (NULL!=m_JumpBlockBuffer)
	{
		delete []m_JumpBlockBuffer;
	}

	if (NULL!=m_IslandBuffer)
	{
		delete []m_IslandBuffer;
	}

	if (NULL!=m_IslandBuffer2)
	{
		delete []m_IslandBuffer2;
	}
}

void SoundApp::Reset()
{
	m_editState = EDIT_STATE_MAZE;
	RemoveAllSprite();
	RemoveAllNpc();
	m_pCurSelNpc=NULL;
	m_pEditNpc=NULL;
	m_selectJumpPointID=0;
	m_EditJumpPointID=-1;
}

void SoundApp::SetState(EDIT_STATE state)
{
	if (state != m_editState)
	{
		SpriteMap::iterator iter = m_spriteMap.begin();
		ASSERT(m_pEngine);
		Nuclear::IWorld* pWorld = m_pEngine->GetWorld();
		if (!pWorld)
			return;
		m_editState = state;
		switch(state)
		{
		case EDIT_STATE_MapJump:  //编辑跳转点
			{   
				RemoveAllSprite();
				ResetMapNpc(m_strNpcXmlName);
				m_pCurSelNpc=NULL;
				m_pEditNpc=NULL;
				m_selectJumpPointID=0;
				LoadJumpPoint(m_strGotoXmlName);
			}
			break;
		case EDIT_STATE_MAZE:
			{   
				//RemoveAllSprite();
				//RemoveAllNpc();
			}
			break;
		case EDIT_STATE_NPC:
			{
				RemoveAllSprite();
				ResetMapNpc(m_strNpcXmlName);
				m_pCurSelNpc=NULL;
				m_pEditNpc=NULL;
				m_pNpcDlg->Reset();
			}
			break;
		case EDIT_STATE_TESTING:
			if (m_pHero == NULL)
			{				
				Nuclear::ISprite* pXPSprite = pWorld->NewSprite(L"male");
				Sprite* pSprite = new Sprite(pXPSprite);
				Nuclear::CRECT vp = pWorld->GetViewport();
				pSprite->SetLocation(vp.Center());
				m_pHero = pSprite;

				m_spriteMap[pXPSprite] = pSprite;
				pWorld->AttachCameraTo(pXPSprite);
				pSprite->SetComponent(0/*body*/, L"10jnanbupao");
				pSprite->SetComponent(1/*head*/, L"bangpaidizinantou1");
				pSprite->SetComponent(3/*weapon*/, L"10jdnandao");	

				//pSprite->SetTitle(0, L"test0", 1, 0xFFFFFFFF);
				//pSprite->SetTitle(1, L"test1", 1, 0xFFFFFFFF);
				//pSprite->SetTitle(2, L"test2", 1, 0xFFFFFFFF);
			}

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

	Nuclear::IFontManager *pFontMan = m_pEngine->GetRenderer()->GetFontManager();
	if (pFontMan)
	{
		std::wstring strFont = L"/ui/fonts/DFYuanW7-GB2312.ttf";

		pFontMan->AddFontType(1, strFont.c_str(), 15);
	}

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

	//2015 Yao
	LoadNpcShapeMap();
	LoadAllBaseNpc();
	LoadAllMonster();

	m_bEngineInited=true;

	return true;
}

void SoundApp::RemoveAllSprite()
{
	m_pHero = NULL;
	SpriteMap::iterator iter = m_spriteMap.begin();
	ASSERT(m_pEngine);
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

void SoundApp::RemoveAllNpc()
{
	m_NpcList.resize(0);
	m_pCurSelNpc=NULL;
}


void SoundApp::OnRenderUI(int now)
{
	//这里可以考虑优化，如果全部画区域太慢的话
	if (m_pDoc)
	{
		Nuclear::Renderer* pRenderer = m_pEngine->GetRenderer();
		Nuclear::CRECT vp = m_pEngine->GetWorld()->GetViewport();//vp可能会在画SelectedArea那里改了
		int addX = vp.left % GRID_WIDTH;
		int addY = vp.top % GRID_HEIGHT;
		int col = 1024 / GRID_WIDTH;
		int row = 768 / GRID_HEIGHT;
		if (m_bDrawGird)
		{
			float x1, y1, x2, y2;
			y1 = 0.0f;
			y2 = 768.0f;
			for (int x = 0; x <= col; ++x)
			{
				x2 = x1 = static_cast<float>(x * GRID_WIDTH - addX);
				pRenderer->DrawLine(x1, y1, x2, y2, 0x3F00FF00);
			}
			x1 = 0.0f;
			x2 = 1024.0f;
			for (int y = 0; y <= row; ++y)
			{
				y1 = y2 = static_cast<float>(y * GRID_HEIGHT - addY);
				pRenderer->DrawLine(x1, y1, x2, y2, 0x3F0000FF);
			}
		}
		
		if (m_bDrawBlock )
		{
			//水，草，土不画阻挡，陈晨5.18需求
			if( m_regionType!=7 && m_regionType!=8 && m_regionType!=9)
			{
				m_BlockRectList.clear();
				
				const Nuclear::FPOINT view = vp.GetPos().ToFPOINT();
				for (int x = vp.left/GRID_WIDTH; x <= vp.right/GRID_WIDTH; ++x)
				{
					for (int y = vp.top/GRID_HEIGHT; y <= vp.bottom/GRID_HEIGHT; ++y)
					{
						unsigned int mask = GetMazeMask(CPoint(x* GRID_WIDTH, y* GRID_HEIGHT));

						if (m_regionType==24||m_regionType==25)
						{
							if (mask&s_BlockLayer2Mask)
							{
								m_BlockRectList.push_back(Nuclear::FRECT(Nuclear::FPOINT(x * GRID_WIDTH, y * GRID_HEIGHT), GRID_WIDTH, GRID_HEIGHT)-view);
							}
						}else if ( ( mask & 0x3 ) == 0x3 )
							m_BlockRectList.push_back(Nuclear::FRECT(Nuclear::FPOINT(x * GRID_WIDTH, y * GRID_HEIGHT), GRID_WIDTH, GRID_HEIGHT)-view);
					}
				}
				if (!m_BlockRectList.empty())
				{
					DWORD color=0x3fffFF00;
					if (m_regionType==24)
					{
						color=0x3f88FF00;
					}
					pRenderer->DrawBox(&m_BlockRectList[0], (int)m_BlockRectList.size(), color);
				}
			}
			switch( m_regionType)
			{
			case 3:/*TS_SETBLOCK*/

				break;
			case  4://TS_QIEZUO,//切磋
				{
					m_BlockRectList.clear();
					const Nuclear::FPOINT view = vp.GetPos().ToFPOINT();
					for (int x = vp.left/GRID_WIDTH; x <= vp.right/GRID_WIDTH; ++x)
					{
						for (int y = vp.top/GRID_HEIGHT; y <= vp.bottom/GRID_HEIGHT; ++y)
						{
							//unsigned int mask = GetMazeMask(CPoint(x* GRID_WIDTH, y* GRID_HEIGHT));
							 if (GetIsTypeOfRegion(x,y,RY_QIEZUO)) 
								m_BlockRectList.push_back(Nuclear::FRECT(Nuclear::FPOINT(x * GRID_WIDTH, y * GRID_HEIGHT), GRID_WIDTH, GRID_HEIGHT)-view);
						}
					}
					if (!m_BlockRectList.empty())
						pRenderer->DrawBox(&m_BlockRectList[0], (int)m_BlockRectList.size(), 0x3fff0000);

				}
				break;
			case 7:/*TS_DUST:*/
			case 8:/*TS_GRASS:*/
			case 9:/*TS_WATER:*/
				{
					m_BlockRectList.clear();
					m_BlockRectList1.clear();
					m_BlockRectList2.clear();
					const Nuclear::FPOINT view = vp.GetPos().ToFPOINT();
					for (int x = vp.left/GRID_WIDTH; x <= vp.right/GRID_WIDTH; ++x)
					{
						for (int y = vp.top/GRID_HEIGHT; y <= vp.bottom/GRID_HEIGHT; ++y)
						{
							unsigned char mask = GetMazeMask(CPoint(x* GRID_WIDTH, y* GRID_HEIGHT));
							unsigned char type = mask>>6;
							switch(type)
							{
							case 1:	//土地
								m_BlockRectList.push_back(Nuclear::FRECT(Nuclear::FPOINT(x * GRID_WIDTH, y * GRID_HEIGHT), GRID_WIDTH, GRID_HEIGHT)-view);
								break;
							case 2:	//草地
								m_BlockRectList1.push_back(Nuclear::FRECT(Nuclear::FPOINT(x * GRID_WIDTH, y * GRID_HEIGHT), GRID_WIDTH, GRID_HEIGHT)-view);
								break;
							case 3:	//水
								m_BlockRectList2.push_back(Nuclear::FRECT(Nuclear::FPOINT(x * GRID_WIDTH, y * GRID_HEIGHT), GRID_WIDTH, GRID_HEIGHT)-view);
								break;
							default:
								break;
							}
						}
					}
					if (!m_BlockRectList.empty())
						pRenderer->DrawBox(&m_BlockRectList[0], (int)m_BlockRectList.size(), 0x7ff7ff3f);	//土地是黄色
					if (!m_BlockRectList1.empty())
						pRenderer->DrawBox(&m_BlockRectList1[0], (int)m_BlockRectList1.size(), 0x7f3fff3f);	//草地是绿色
					if (!m_BlockRectList2.empty())
						pRenderer->DrawBox(&m_BlockRectList2[0], (int)m_BlockRectList2.size(), 0x7f3f3fff);	//水是蓝色
				}
				break;
			case  5://
				{
					m_BlockRectList.clear();
					const Nuclear::FPOINT view = vp.GetPos().ToFPOINT();
					for (int x = vp.left/GRID_WIDTH; x <= vp.right/GRID_WIDTH; ++x)
					{
						for (int y = vp.top/GRID_HEIGHT; y <= vp.bottom/GRID_HEIGHT; ++y)
						{
							//RY_QIEZUO
							if (GetIsTypeOfRegion(x,y,RY_BOSA))
								m_BlockRectList.push_back(Nuclear::FRECT(Nuclear::FPOINT(x * GRID_WIDTH, y * GRID_HEIGHT), GRID_WIDTH, GRID_HEIGHT)-view);
						}
					}
					if (!m_BlockRectList.empty())
						pRenderer->DrawBox(&m_BlockRectList[0], (int)m_BlockRectList.size(), 0x3fffff00);

				}
				break;
			case  6://
				{
					m_BlockRectList.clear();
					const Nuclear::FPOINT view = vp.GetPos().ToFPOINT();
					for (int x = vp.left/GRID_WIDTH; x <= vp.right/GRID_WIDTH; ++x)
					{
						for (int y = vp.top/GRID_HEIGHT; y <= vp.bottom/GRID_HEIGHT; ++y)
						{
							//RY_QIEZUO
							if (GetIsTypeOfRegion(x,y,RY_BAITAN))
								m_BlockRectList.push_back(Nuclear::FRECT(Nuclear::FPOINT(x * GRID_WIDTH, y * GRID_HEIGHT), GRID_WIDTH, GRID_HEIGHT)-view);
						}
					}
					if (!m_BlockRectList.empty())
						pRenderer->DrawBox(&m_BlockRectList[0], (int)m_BlockRectList.size(), 0x3fff00ff);

				}
				break;
			case  10://
				{
					m_BlockRectList.clear();
					m_MonBlockList.clear();
					const Nuclear::FPOINT view = vp.GetPos().ToFPOINT();
					for (int x = vp.left/GRID_WIDTH; x <= vp.right/GRID_WIDTH; ++x)
					{
						for (int y = vp.top/GRID_HEIGHT; y <= vp.bottom/GRID_HEIGHT; ++y)
						{
							//RY_QIEZUO
							if (GetIsTypeOfRegion(x,y,RY_KITE))
								m_BlockRectList.push_back(Nuclear::FRECT(Nuclear::FPOINT(x * GRID_WIDTH, y * GRID_HEIGHT), GRID_WIDTH, GRID_HEIGHT)-view);
						}
					}
					if (!m_BlockRectList.empty())
						pRenderer->DrawBox(&m_BlockRectList[0], (int)m_BlockRectList.size(), 0x3f00ffff);

				}
				break;
			case  11://TS_SHUAGUAI
				{
					m_BlockRectList.clear();
					const Nuclear::FPOINT view = vp.GetPos().ToFPOINT();

					int strMonIndex;
					std::wstring dMonColor;
					Nuclear::XPCOLOR color;
					Nuclear::XPVECTOR4 rgb;
				  
					for (int x = vp.left/GRID_WIDTH; x <= vp.right/GRID_WIDTH; ++x)
					{
						for (int y = vp.top/GRID_HEIGHT; y <= vp.bottom/GRID_HEIGHT; ++y)
						{
							//RY_QIEZUO
							if (GetIsTypeOfRegion(x,y,RY_SHUAGUAI))
							{
								m_BlockRectList.push_back(Nuclear::FRECT(Nuclear::FPOINT(x * GRID_WIDTH, y * GRID_HEIGHT), GRID_WIDTH, GRID_HEIGHT)-view);
							}
							if(CheckMonsterPoint(x,y)!=-1)
							{
								int iTmpMonID = CheckMonsterPoint(x,y);
								int iTmpMonType = 0;
								for(int i=0;i<m_monsterBaseList.size();++i)
								{
									int tmpMonId00 = m_monsterBaseList[i]->BaseID;
									if(tmpMonId00==iTmpMonID)
									{
										iTmpMonType = i;
										i=m_monsterBaseList.size()-1;
										break;
									}
								}
								strMonIndex	= m_monsterBaseList[iTmpMonType]->BaseID;
								dMonColor	= m_monsterBaseList[iTmpMonType]->strMonsterColor;
								
								int argb[8];
								
								if(dMonColor.size()<8)
								{
									rgb.x = 0.0f;
									rgb.y = 0.0f;
									rgb.z = 0.0f;
									rgb.w = 0.0f;
								}
								else
								{
									for(int i=0;i<dMonColor.size();++i)
									{
										int iColourVal = 0;
										if(dMonColor[i] >= '0'	&&	dMonColor[i]<='9')
										{
											iColourVal	= dMonColor[0] - '0';
										}
										if(dMonColor[i] >= 'a'	&&	dMonColor[i]<='f')
										{
											iColourVal	= '0'+(dMonColor[i]-'a');
										}
										if(dMonColor[i] >= 'A'	&&	dMonColor[i]<='F')
										{
											iColourVal	= '0'+(dMonColor[i]-'A');
										}
										if((i%2) == 0)
										{
											iColourVal	*= 16;
										}
										argb[i]	= iColourVal;
									}

									float r		= argb[0]+argb[1]; 
									float g		= argb[2]+argb[3]; 
									float b		= argb[4]+argb[5]; 
									float a		= argb[6]+argb[7]; 

									rgb.x		= r;
									rgb.y		= g;
									rgb.z		= b;
									rgb.w		= a;
								}

								color.r = rgb.x;
								color.g = rgb.y;
								color.b = rgb.z;
								color.a = rgb.w;

								MonsterInfo tmpMonInfo;
								tmpMonInfo.ID		= strMonIndex;
								tmpMonInfo.Color	= color;
								tmpMonInfo.Area		= Nuclear::FRECT(Nuclear::FPOINT(x * GRID_WIDTH, y * GRID_HEIGHT), GRID_WIDTH, GRID_HEIGHT)-view;
                                
								m_MonBlockList[iTmpMonID] = tmpMonInfo;
							}
						}
					}
					
					if(!m_MonBlockList.empty()&&!m_BlockRectList.empty())
					{
						std::map<int,MonsterInfo>::iterator it	= m_MonBlockList.begin();
						std::map<int,MonsterInfo>::iterator end = m_MonBlockList.end();
						pRenderer->DrawBox(&m_BlockRectList[0], (int)m_BlockRectList.size(), 0x3fFF0000);
		
						for(int i=0;i<m_BlockRectList.size();++i)
						{
							Nuclear::CRECT NameRect(0,0,0,0);

							NameRect.left	=(int)(m_BlockRectList[i].left);
							NameRect.top	=(int)(m_BlockRectList[i].top);
							NameRect.right	=(int)(m_BlockRectList[i].right);
							NameRect.bottom	=(int)(m_BlockRectList[i].bottom);
						
							LONG tmp_X = NameRect.left/GRID_WIDTH  + view.x;
							LONG tmp_Y = NameRect.top /GRID_HEIGHT + view.y;

							int iTmpId = CheckMonsterPoint(tmp_X,tmp_Y);
							int iMonId  = iTmpId > 0 ? iTmpId : 0;
							wchar_t charMonId[10];
							_itow_s(iMonId,charMonId,10,10);
							std::wstring strMonID = charMonId;

							pRenderer->DrawText(strMonID.c_str(),strMonID.size(),&NameRect,it->second.Color);
						}
					}
				}
				break;
			case 12://TS_MINGSHENG
				{
					m_BlockRectList.clear();
					const Nuclear::FPOINT view = vp.GetPos().ToFPOINT();
					for (int x = vp.left/GRID_WIDTH; x <= vp.right/GRID_WIDTH; ++x)
					{
						for (int y = vp.top/GRID_HEIGHT; y <= vp.bottom/GRID_HEIGHT; ++y)
						{
							//RY_QIEZUO
							if (GetIsTypeOfRegion(x,y,RY_MINGSHENG))
								m_BlockRectList.push_back(Nuclear::FRECT(Nuclear::FPOINT(x * GRID_WIDTH, y * GRID_HEIGHT), GRID_WIDTH, GRID_HEIGHT)-view);
						}
					}
					if (!m_BlockRectList.empty())
						pRenderer->DrawBox(&m_BlockRectList[0], (int)m_BlockRectList.size(), 0x3fFF0000);
				}
				break;

			case 13://飞跃1阶阻挡
			case 14://飞跃2阶阻挡
			case 15://飞跃3阶阻挡
			case 16://飞跃4阶阻挡
				{
					DrawJumpBlockRect();
				}
				break;
			case 17://孤岛信息
				{
					DrawGuDaoInf(1);
				}
				break;
			case 25://孤岛2层信息
				{
					DrawGuDaoInf(2);
				}
				break;
			case 18://11号区域
			case 19://12号区域
			case 20://13号区域
			case 21://14号区域
			case 22://15号区域
			case 23://16号区域
				{
					DrawFreeArea(m_regionType);
				}
				break;
			case 26://高级刷怪区
				{
					m_BlockRectList.clear();
					const Nuclear::FPOINT view = vp.GetPos().ToFPOINT();
					for (int x = vp.left/GRID_WIDTH; x <= vp.right/GRID_WIDTH; ++x)
					{
						for (int y = vp.top/GRID_HEIGHT; y <= vp.bottom/GRID_HEIGHT; ++y)
						{
							//RY_QIEZUO
							if (GetIsTypeOfRegion(x,y,RY_GAOJISHUGUAI))
								m_BlockRectList.push_back(Nuclear::FRECT(Nuclear::FPOINT(x * GRID_WIDTH, y * GRID_HEIGHT), GRID_WIDTH, GRID_HEIGHT)-view);
						}
					}
					if (!m_BlockRectList.empty())
						pRenderer->DrawBox(&m_BlockRectList[0], (int)m_BlockRectList.size(), 0x3f00FF88);
				}
				break;
			}
		}
	}
	if (m_editState==EDIT_STATE_NPC)
	{
		DrawSelectNpcBox();
		DrawNpcName();
	}
	if (m_editState==EDIT_STATE_MapJump)
	{
		DrawJumpPoint();
	}
}

bool SoundApp::ReadMazeBuffer(CString mazefilemame)
{
	fstream binary_file(mazefilemame.GetBuffer(mazefilemame.GetLength()),ios::binary|ios::in);
	unsigned int minsize = 0;
	size_t map_width = 0;
	size_t map_height = 0;
	binary_file.read((char*)&minsize,sizeof(int));
	binary_file.read((char*)&map_width,sizeof(size_t));
	binary_file.read((char*)&map_height,sizeof(size_t));
	NewMazeBuffer();
	if (map_width == m_mapGridWidth && map_height == m_mapGridHeight)
	{
		binary_file.read((char*)m_MazeBuffer,m_mapGridWidth*m_mapGridHeight);

		CheckMazeLayer2IsNew();
		return true;
	}
	return false;

}
bool SoundApp::WriteMazeBuffer(CString mazefilemame)
{
	if (CheckLonelyIsland())
	{
		fstream binary_file(mazefilemame.GetBuffer(mazefilemame.GetLength()),ios::binary|ios::out);
		unsigned int minsize = 12;
		binary_file.write((char*)&minsize,sizeof(int));
		binary_file.write((char*)&m_mapGridWidth,sizeof(size_t));
		binary_file.write((char*)&m_mapGridHeight,sizeof(size_t));
		binary_file.write((char*)m_MazeBuffer,m_mapGridWidth*m_mapGridHeight);
		binary_file.close();

		fstream file(mazefilemame.GetBuffer(mazefilemame.GetLength()),ios::binary|ios::in);
		char* MazeBuffer = new char[m_mapGridWidth*m_mapGridHeight+12];
		file.read((char*)MazeBuffer,m_mapGridWidth*m_mapGridHeight+12);
		if (m_pEngine->GetWorld()->SetMapMaze(MazeBuffer,m_mapGridWidth*m_mapGridHeight+12))
		{
			MessageBox(NULL, L"更新阻挡点", L"提示", NULL);
		}
		file.close();
		delete MazeBuffer;
		return true;
	}
	else
		return false;
}

//以下都是像素坐标
void SoundApp::SetBlockPoint(CPoint point,bool bAllScreen,bool tab,bool brush ,unsigned char mask)
{
	//unsigned int mask = GetMazeMask(point);
	//if ( ( mask & 0x3 ) == 0x3 && tab)
	//	ClearMazeMask(0x03,point);
	//else
	//{
	SetMazeMask(mask,point,bAllScreen,brush);
	//}
}
unsigned char SoundApp::GetMazeMask(CPoint pixel)
{
	pixel.x /= GRID_WIDTH;
	pixel.y /= GRID_HEIGHT;
	return m_MazeBuffer[pixel.x + pixel.y*m_mapGridWidth];
}
//检查孤岛
bool SoundApp::CheckLonelyIsland()
{
	for (size_t i=0;i < m_mapGridWidth;i++)
	{
		for (size_t j=0;j < m_mapGridHeight;j++)
		{
			//如果是非阻挡点，则检查周围8个点不能全是阻挡点
			if ((m_MazeBuffer[i + j*m_mapGridWidth] & 0x03) != 0x03)
			{
				bool bAllblock = true;
				for (int a=-1;a<=1;a++)
				{
					for (int b=-1;b<=1;b++)
					{
						if (a==0 && b==0)
							continue;
						else
						{
							int index = (j+b)*m_mapGridWidth + i + a;
							if (index >=0 && index < m_mapGridWidth*m_mapGridHeight)
							{
								if ((m_MazeBuffer[index] & 0x03) != 0x03)
								{
									bAllblock = false;
									//break;
								}
							}
							else
								continue;
						}
					}
				}
				if (bAllblock)
				{
					m_MazeBuffer[i + j*m_mapGridWidth] |= 0x03;
					MessageBox(NULL, L"存在孤岛", L"提示", NULL);
					return false;
				}
			}
			else
				continue;
		}
	}
	return true;
}

void SoundApp::SetMazeMask(unsigned int mask,CPoint pixel,bool bAllScreen,bool brush)
{
	pixel.x /= GRID_WIDTH;
	pixel.y /= GRID_HEIGHT;
	if (brush)
	{
		if (!bAllScreen)
		{
			for (int i=-m_EdgeSize / 2; i<m_EdgeSize / 2; i++)
			{
				for (int j=-m_EdgeSize / 2; j<m_EdgeSize / 2; j++)
				{
					if ((pixel.x+i+(pixel.y+j)*m_mapGridWidth) >= 0 && (pixel.x+i+(pixel.y+j)*m_mapGridWidth) < m_mapGridWidth*m_mapGridHeight)
					{
						//如果是刷鼠标点击效果点，则先清空原来的效果
						if (mask == 0x01<<6 || mask == 0x02<<6 || mask == 0x03<<6)
						{
							m_MazeBuffer[pixel.x + i + (pixel.y+j)*m_mapGridWidth] &= 0x3F;
						}
						m_MazeBuffer[pixel.x + i + (pixel.y+j)*m_mapGridWidth] |= mask;
					}
				}
			}
		}
		else
		{
			SetAllMask(mask);
		}
	}
	else
	{
		if (!bAllScreen)
		{
			//如果是刷鼠标点击效果点，则先清空原来的效果
			if (mask == 0x01<<6 || mask == 0x02<<6 || mask == 0x03<<6)
			{
				m_MazeBuffer[pixel.x + pixel.y*m_mapGridWidth] &= 0x3F;
			}
			m_MazeBuffer[pixel.x + pixel.y*m_mapGridWidth] |= mask;
		}
		else
		{
			SetAllMask(mask);
		}
	}
}
void SoundApp::ClearMazeMask(unsigned int mask,CPoint pixel,bool bAllScreen)
{
	if (!bAllScreen)
	{
		pixel.x /= GRID_WIDTH;
		pixel.y /= GRID_HEIGHT;
		m_MazeBuffer[pixel.x + pixel.y*m_mapGridWidth] &= ~mask;
	}
	else
	{
		ClearAllMask(mask);
	}
}

void SoundApp::ClearMazeMask(unsigned int mask,CPoint center,int edge,bool bAllScreen)
{
	if (!bAllScreen)
	{
		center.x /= GRID_WIDTH;
		center.y /= GRID_HEIGHT;

		for (int i = -edge / 2; i < edge / 2 ; ++i)
		{
			int dx = center.x + i;
			if (dx < 0 || dx >= m_mapGridWidth)
				continue;

			for (int j = -edge / 2; j < edge / 2; ++j )
			{
				int dy = center.y + j;
				if (dy < 0 || dy >= m_mapGridHeight)
					continue;

				m_MazeBuffer[dx  + dy * m_mapGridWidth] &= ~mask;
			}
		}
	}
	else
	{
		ClearAllMask(mask);
	}
}


void SoundApp::OnDrawGird()
{
	m_bDrawGird = !m_bDrawGird;
}

void SoundApp::OnDrawBlock()
{
	m_bDrawBlock = !m_bDrawBlock;
}
void SoundApp::OnUpdateDrawGird(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck(m_bDrawGird);
}

void SoundApp::OnUpdateDrawBlock(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck(m_bDrawBlock);
}

void SoundApp::LoadNpcShapeMap()
{
	m_NpcShapeMap.clear();

	XMLIO::CFileReader fr;
	XMLIO::CNodeList typenl;

	std::wstring strNpcBaseXmlPath=L"/table/xmltable/npc.cnpcshape.xml";
	if (!GetXmlNodeList(strNpcBaseXmlPath,fr,typenl))
		return ;

	for( XMLIO::CNodeList::iterator typeIt = typenl.begin(); typeIt != typenl.end(); ++typeIt)
	{
		int modelID=typeIt->GetAttributeInteger(L"id");
		int dirType=typeIt->GetAttributeInteger(L"dir");
		std::wstring modelName(L"");
		typeIt->GetAttribute(L"shape3d",modelName);
		if (modelName.empty())
		{
			typeIt->GetAttribute(L"shape",modelName);
		}

		std::wstring bodyName(L"");
		typeIt->GetAttribute(L"body",bodyName);
		
		std::wstring headName(L"");
		typeIt->GetAttribute(L"head",headName);

		std::wstring hairName(L"");
		typeIt->GetAttribute(L"hair",hairName);


		m_NpcShapeMap[modelID]=modelName;

		m_NpcBodyNameMap[modelID]=bodyName;
        m_NpcHeadNameMap[modelID]=headName;
		m_NpcHairNameMap[modelID]=hairName;

		m_NpcDirMap[modelName]=dirType;
	}

	fr.CloseFile();
}

void SoundApp::LoadAllBaseNpc()
{
	XMLIO::CFileReader fr;
	XMLIO::CNodeList typenl;

	std::wstring strNpcBaseXmlPath=	L"/table/xmltable/npc.CNPCConfig.xml";
	if (!GetXmlNodeList(strNpcBaseXmlPath,fr,typenl))
		return ;
	for( XMLIO::CNodeList::iterator typeIt = typenl.begin(); typeIt != typenl.end(); ++typeIt)
	{
		stNpcBaseData* pNpcBase=new stNpcBaseData;
		pNpcBase->BaseID = typeIt->GetAttributeInteger(L"id");
		
		int modelID=typeIt->GetAttributeInteger(L"modelID");
		pNpcBase->strNpcModel=m_NpcShapeMap[modelID];
		pNpcBase->strBodyResName=m_NpcBodyNameMap[modelID];
		pNpcBase->strHeadResName=m_NpcHeadNameMap[modelID];
		pNpcBase->strHairResName=m_NpcHairNameMap[modelID];

		pNpcBase->dirType=m_NpcDirMap[pNpcBase->strNpcModel];
		pNpcBase->eNpcType=typeIt->GetAttributeInteger(L"npctype");
		typeIt->GetAttribute(L"name",pNpcBase->strName);
		typeIt->GetAttribute(L"title",pNpcBase->strTitle);
		//typeIt->GetAttribute(L"chitchat",pNpcBase->strChitchat);
		pNpcBase->chat1= typeIt->GetAttributeInteger(L"chitchatid1");
		pNpcBase->chat2= typeIt->GetAttributeInteger(L"chitchatid2");
		pNpcBase->chat3= typeIt->GetAttributeInteger(L"chitchatid3");

		m_NpcBaseList.push_back(pNpcBase);

	}
	fr.CloseFile();

}

void SoundApp::RemoveAllBaseNpc()
{   
	NpcBaseList::iterator it=m_NpcBaseList.begin();
	for (;it!=m_NpcBaseList.end();++it)
	{
		delete (*it);
	}
}


void SoundApp::ResetMapNpc(std::wstring NpcXmlfilemame)
{
	RemoveAllSprite();
	RemoveAllNpc();
	m_pCurSelNpc=NULL;

	m_strNpcXmlName=NpcXmlfilemame;

	SpriteMap::iterator iter = m_spriteMap.begin();
	ASSERT(m_pEngine);
	Nuclear::IWorld* pWorld = m_pEngine->GetWorld();
	if (!pWorld)
		return;

	XMLIO::CFileReader fr;
	XMLIO::CNodeList typenl;
	if (!GetXmlNodeList(NpcXmlfilemame,fr,typenl))
		return ;

	for( XMLIO::CNodeList::iterator typeIt = typenl.begin(); typeIt != typenl.end(); ++typeIt)
	{
		int id=typeIt->GetAttributeInteger(L"id");
		int xPos=typeIt->GetAttributeInteger(L"posx");
		int yPos=typeIt->GetAttributeInteger(L"posy");

		if (m_pEngine&&m_pEngine->GetWorld())
		{
			Nuclear::CPOINT mapSize;
			m_pEngine->GetWorld()->GetMapSize(mapSize);
			int mapWidth=(int)mapSize.x;
			int mapHeight=(int)mapSize.y;
			if (xPos>mapWidth||yPos>mapHeight)
			{
				CString strMsg;
				strMsg.Format(L"ID为%d的NPC超出地图范围,当前地图大小(%d,%d)",id,mapWidth,mapHeight);
				MessageBox(NULL,strMsg,L"提示",0);
				continue;
			}
		}


		const stNpcBaseData* pNpcBase=GetNpcBaseById(id);
		if (pNpcBase==NULL)
		{
			continue;;
		}
		std::wstring modeName=pNpcBase->strNpcModel;
		Nuclear::ISprite* pXPSprite = pWorld->NewSprite(modeName);
		CNpc* pNewNpc = new CNpc(pXPSprite);
		pNewNpc->m_pNpcBaseData=pNpcBase;

		int dir=typeIt->GetAttributeInteger(L"dir");

		Nuclear::CRECT vp = pWorld->GetViewport();
		Nuclear::Location Loc=Nuclear::WorldLogicCoord::logic2world(xPos,yPos);
		pNewNpc->SetLocation(Loc);
		pNewNpc->SetDirection(dir);

		m_spriteMap[pXPSprite] = pNewNpc;

		std::wstring trdName=modeName.substr(0,3);
		if (trdName!=L"3ds")
		{
			pNewNpc->SetComponent(0/*body*/, L"bodyonly");
		}

		if (!pNpcBase->strBodyResName.empty())
		{
			pNewNpc->SetComponent(1,pNpcBase->strBodyResName);
		}

		if (!pNpcBase->strHeadResName.empty())
		{
			pNewNpc->SetComponent(2,pNpcBase->strHeadResName);
		}

		if (!pNpcBase->strHairResName.empty())
		{
			pNewNpc->SetComponent(3,pNpcBase->strHairResName);
		}

		//pWorld->AttachCameraTo(pXPSprite);

		//pNewNpc->SetComponent(0/*body*/, L"bodyonly");
		//pNewNpc->SetComponent(1/*head*/, L"bangpaidizinantou1");
		//pNewNpc->SetComponent(3/*weapon*/, L"10jdnandao");	

		pNewNpc->SetName(pNpcBase->strName);

		m_NpcList.push_back(pNewNpc);
	}
	fr.CloseFile();

}


bool SoundApp::GetXmlNodeList(std::wstring path, XMLIO::CFileReader& fr, XMLIO::CNodeList& typenl)
{
	if (path.empty())
	{
		return false;
	}
	int nError = fr.OpenFile(path);
	if( XMLIO::EC_SUCCESS != nError)
	{
		fr.CloseFile();
		return false;
	}
	XMLIO::CINode	root;
	if (!fr.GetRootNode(root))
	{
		fr.CloseFile();
		return false;
	}
	root.GetChildren(typenl);
	return true;
}

const stNpcBaseData* SoundApp::GetNpcBaseById(int id) 
{
	NpcBaseList::iterator it=m_NpcBaseList.begin();
	for (;it!=m_NpcBaseList.end();++it)
	{
		if ((*it)->BaseID==id)
		{
			return (*it);
		}
	}
	return NULL;
}

//bool SoundApp::SelectNpc(CPoint pt)
//{
//	if (m_pNpcDlg==NULL)
//	{
//		CMainFrame* pMainFrame=(CMainFrame*)AfxGetApp()->m_pMainWnd;
//		if (pMainFrame!=NULL)
//		{
//			CPropView* pPropView=(CPropView*)pMainFrame->m_wndHSplitter.GetPane(0,1);
//			if (pPropView)
//			{
//				m_pNpcDlg=pPropView->m_tabPropPanel.m_pdlgNpcPropertyDlg;
//			}
//			//m_pNpcDlg=(CNpcPropertyDlg*)pMainFrame->GetDlgItem(IDD_NPCPANEL);
//		}
//	}
//
//	if (NULL == m_pEngine)
//	{
//		return false;
//	}
//	Nuclear::IWorld* pWorld = m_pEngine->GetWorld();
//	if (!pWorld)
//		return false;
//	if (m_pCurSelNpc!=NULL)
//	{
//		Nuclear::Location npcloc=m_pCurSelNpc->GetLocation();
//		unsigned char mask = GetMazeMask(npcloc);
//		if ((mask&0x03) != 0x00)
//		{
//			//MessageBoxA(NULL,"sorry,所选NPC当前位置含阻挡信息!!","错误提示",MB_OK);
//			return false;
//		}
//	}
//
//	std::vector<Nuclear::ISelectableObj*> objs;
//	Nuclear::Location loc(pt.x,pt.y);
//	pWorld->SelectObjs(loc,objs);
//	if (objs.size()>0)
//	{
//		NpcList::iterator it=m_NpcList.begin();
//		for (;it!=m_NpcList.end();++it)
//		{
//			if ((*it)->m_pSprite==objs.front())
//			{
//				if ((*it)!=m_pCurSelNpc)
//				{
//					m_pCurSelNpc=(*it);
//				}else
//				{
//					m_pCurSelNpc=NULL;
//					return false;
//				}
//			}
//		}
//
//		//m_pCurSelNpc=(CNpc*)objs.front();
//
//		if (m_pCurSelNpc)
//		{
//			if (m_pNpcDlg)
//			{
//				m_pNpcDlg->ChangeSelNpc(m_pCurSelNpc);
//			}
//			return true;
//		}
//
//	}
//	else
//	{
//		m_pCurSelNpc=NULL;
//	}
//	return false;
//}

bool SoundApp::SelectNpc(CPoint pt)
{
	if (m_pNpcDlg==NULL)
	{
		CMainFrame* pMainFrame=(CMainFrame*)AfxGetApp()->m_pMainWnd;
		if (pMainFrame!=NULL)
		{
			CPropView* pPropView=(CPropView*)pMainFrame->m_HSplitterWindow.GetPane(0,1);
			if (pPropView)
			{
				m_pNpcDlg=pPropView->m_tabPropPanel.m_pdlgNpcPropertyDlg;
			}
		}
	}

	if (NULL == m_pEngine)
	{
		return false;
	}

	Nuclear::IWorld* pWorld = m_pEngine->GetWorld();
	if (!pWorld)
		return false;

	Nuclear::CRECT vp = m_pEngine->GetWorld()->GetViewport();//vp可能会在画SelectedArea那里改了

	if (m_pCurSelNpc!=NULL)
	{
		Nuclear::Location npcloc=m_pCurSelNpc->GetLocation();
		unsigned char mask = GetMazeMask(npcloc);
		if ((mask&0x03) != 0x00)
		{
			//MessageBoxA(NULL,"sorry,所选NPC当前位置含阻挡信息!!","错误提示",MB_OK);
			return false;
		}
	}

	std::vector<Nuclear::ISelectableObj*> objs;
	Nuclear::Location loc(pt.x,pt.y);
	pWorld->SelectObjs(loc,objs);
	if (objs.size()>0)
	{
		NpcList::iterator it=m_NpcList.begin();
		for (;it!=m_NpcList.end();++it)
		{
			if ((*it)->m_pSprite==objs.front())
			{
				if ((*it)!=m_pCurSelNpc)
				{
					m_pCurSelNpc=(*it);
				}
				else
				{
					m_pCurSelNpc=NULL;
					return false;
				}
			}
		}
	}
	else
	{
		std::vector<CNpc*>::iterator itNpc = m_NpcList.begin();
		while (itNpc != m_NpcList.end())
		{
			CNpc* pNpc = *itNpc;
			Nuclear::Location npcLoc = pNpc->GetLocation();
			Nuclear::FRECT rect;
			rect.top=(float)(npcLoc.y-vp.top)-120.0f;
			rect.left=(float)(npcLoc.x-vp.left)-30.0f;
			rect.right=rect.left+60.0f;
			rect.bottom=rect.top+120.0f;

			Nuclear::FPOINT ptMouse(pt.x,pt.y);
			if (rect.PtInRect(pt))
			{
				m_pCurSelNpc = pNpc;
				break;
			}
			
			++itNpc;
		}
		
		if (itNpc == m_NpcList.end())
		{
			m_pCurSelNpc=NULL;
		}
	}

	//m_pCurSelNpc=(CNpc*)objs.front();

	if (m_pCurSelNpc)
	{
		if (m_pNpcDlg)
		{
			m_pNpcDlg->ChangeSelNpc(m_pCurSelNpc);
		}
		return true;
	}
	return false;
}

void SoundApp::MoveSelectNpc(CPoint pt)
{
	if (m_pCurSelNpc!=NULL)
	{
		Nuclear::Location loc(pt.x,pt.y);
		m_pCurSelNpc->SetLocation(loc);

		Nuclear::Location LogicLoc=Nuclear::WorldLogicCoord::world2logic(loc);
		m_pNpcDlg->ChangeSelNpcPos(LogicLoc.x,LogicLoc.y);
	}
}

void SoundApp::PutNpc(CPoint pt)
{
	if (m_pCurSelNpc!=NULL)
	{
		Nuclear::Location loc(pt.x,pt.y);
		m_pCurSelNpc->SetLocation(loc);

		m_pCurSelNpc=NULL;

	}
}

CNpc* SoundApp::AddNewNpc(int baseid)
{
	ASSERT(m_pEngine);
	Nuclear::IWorld* pWorld = m_pEngine->GetWorld();
	if (!pWorld)
		return NULL;

	const stNpcBaseData* pNpcBase=GetNpcBaseById(baseid);
	if (pNpcBase==NULL)
	{
		return NULL;
	}
	std::wstring modeName=pNpcBase->strNpcModel;
	Nuclear::ISprite* pXPSprite = pWorld->NewSprite(modeName);
	CNpc* pNewNpc = new CNpc(pXPSprite);
	pNewNpc->m_pNpcBaseData=pNpcBase;

	Nuclear::CRECT vp = pWorld->GetViewport();
	pNewNpc->SetLocation(Nuclear::Location(vp.Center()));

	m_spriteMap[pXPSprite] = pNewNpc;
	//pWorld->AttachCameraTo(pXPSprite);
	std::wstring trdName=modeName.substr(0,3);
	if (trdName!=L"3ds")
	{
		pNewNpc->SetComponent(0/*body*/, L"bodyonly");
	}

	if (!pNpcBase->strBodyResName.empty())
	{
		pNewNpc->SetComponent(1,pNpcBase->strBodyResName);
	}
	
	if (!pNpcBase->strHeadResName.empty())
	{
		pNewNpc->SetComponent(2,pNpcBase->strHeadResName);
	}

	if (!pNpcBase->strHairResName.empty())
	{
		pNewNpc->SetComponent(3,pNpcBase->strHairResName);
	}
	//pNewNpc->SetComponent(1/*head*/, L"bangpaidizinantou1");
	//pNewNpc->SetComponent(3/*weapon*/, L"10jdnandao");	

	pNewNpc->SetName(pNpcBase->strName);

	if (pNpcBase->dirType==4)
	{
		pNewNpc->SetDirection(3);
	}else
	{
		pNewNpc->SetDirection(4);
	}

	m_NpcList.push_back(pNewNpc);

	m_pCurSelNpc=pNewNpc;
	m_pNpcDlg->ChangeSelNpc(pNewNpc);
}

void SoundApp::InitNpcBaseComBox(CComboBox* pComBox)
{
	if (pComBox)
	{
		pComBox->ResetContent();
		NpcBaseList::iterator it=m_NpcBaseList.begin();
		int i=0;
		for (;it!=m_NpcBaseList.end();++it)
		{
			/*if (CheckNpcBaseUsed((*it)->BaseID))
			{
				continue;
			}*/
			wchar_t strItem[128]={0};
			wsprintf(strItem,L"%d %s %s",(*it)->BaseID,(*it)->strNpcModel.c_str(),(*it)->strName.c_str());
			pComBox->InsertString(i,strItem);
			pComBox->SetItemDataPtr(i,(*it));
			++i;
		}

	}
}
int  SoundApp::GetNewNpcBaseID() //获得新的npcbaseID
{

	NpcBaseList::iterator it=m_NpcBaseList.begin();
	int i=0;
	for (;it!=m_NpcBaseList.end();++it)
	{
		if ((*it)->BaseID>i)
		{
			i=(*it)->BaseID;
		}
	}
	return ++i;
}

bool SoundApp::AddNewNpcBase(int id,int type,std::wstring strName,std::wstring strModel,std::wstring strTitle,int chat1,int chat2,int chat3 )
{
	stNpcBaseData* pNewBase=new stNpcBaseData;
	pNewBase->BaseID=id;
	pNewBase->strName=strName;
	pNewBase->strNpcModel=strModel;
	pNewBase->eNpcType=type;
	pNewBase->strTitle=strTitle;
	pNewBase->chat1=chat1;
	pNewBase->chat2=chat2;
	pNewBase->chat3=chat3;

	return AddNewNpcBase(pNewBase);

}

bool SoundApp::AddNewNpcBase(stNpcBaseData* pNewNpcBase)
{
	if (pNewNpcBase!=NULL)
	{
		int id=pNewNpcBase->BaseID;
		if (CheckNpcBaseUsed(id))
		{
			return false;
		}else
		{
			m_NpcBaseList.push_back(pNewNpcBase);
			return true;
		}

	}
	return false;
}

bool SoundApp::CheckNpcBaseUsed(int id) //检查该npcbaseid是否已在当前地图中存在
{
	NpcList::iterator it=m_NpcList.begin();
	for (;it!=m_NpcList.end();++it)
	{
		if ((*it)->m_pNpcBaseData->BaseID==id)
		{
			return true;
		}
	}
	return false;
}
bool SoundApp::IsExistBaseID(int id) //查表base表中是否有该BaseId
{
	NpcBaseList::iterator it=m_NpcBaseList.begin();
	for (;it!=m_NpcBaseList.end();++it)
	{
		if ((*it)->BaseID==id)
		{
			return true;
		}
	}
	return false;
}

void SoundApp::InitNpcTypeComBox(CComboBox* pComBox)
{
	if (pComBox==NULL)
	{
		return;
	}
	for (int i=(int)eNpcTypeNone+1;i<(int)eNpcTypeMax;i++)
	{
		pComBox->AddString(strNpceType[i]);
	}
}

void SoundApp::DrawSelectNpcBox()
{
	Nuclear::Renderer* pRenderer = m_pEngine->GetRenderer();
	Nuclear::CRECT vp = m_pEngine->GetWorld()->GetViewport();//vp可能会在画SelectedArea那里改了

	if (m_pEditNpc!=NULL)
	{
		if (!CheckNpcBaseUsed(m_pEditNpc->m_pNpcBaseData->BaseID))
		{
			return;
		}
		Nuclear::Location loc=m_pEditNpc->GetLocation();
		Nuclear::FRECT rect;
		rect.top=(float)(loc.y-vp.top)-120.0f;
		rect.left=(float)(loc.x-vp.left)-30.0f;
		rect.right=rect.left+60.0f;
		rect.bottom=rect.top+120.0f;

		pRenderer->DrawBox(rect,0x33ff0000);
	}
}

void SoundApp::DrawNpcName()
{
	Nuclear::Renderer* pRenderer = m_pEngine->GetRenderer();
	Nuclear::CRECT vp = m_pEngine->GetWorld()->GetViewport();//vp可能会在画SelectedArea那里改了
	NpcList::iterator it=m_NpcList.begin();
	Nuclear::IFontManager *pFontMan = m_pEngine->GetRenderer()->GetFontManager();
	for (;it!=m_NpcList.end();++it)
	{
		std::wstring name=(*it)->GetName();
		Nuclear::Location loc=(*it)->GetLocation();

		if (name.size()>0)
		{
			float width=(float)m_pEngine->GetRenderer()->GetFontManager()->GetStringWidth(name.c_str(),1);
			float heigh=(float)m_pEngine->GetRenderer()->GetFontManager()->GetFontTypeHeight(1);

			Nuclear::FPOINT pt((float)(loc.x-vp.left),float(loc.y-vp.top));

			Nuclear::CRECT NameRect(0,0,0,0);
			NameRect.left=(int)(pt.x-width/2.0f);
			NameRect.top=(int)(pt.y+2.0f);
			NameRect.right=(int)(NameRect.left+width);
			NameRect.bottom=(int)(NameRect.top+width);
			Nuclear::XPCOLOR xpcolor(0xffffff00);
			//m_pEngine->GetRenderer()->DrawText(name.c_str(),name.size(),&NameRect,xpcolor);
			
			if (pFontMan)
			{
				Nuclear::TextBlockHandle handle=pFontMan->NewText(name.c_str(),1,0xfffff000,0xfffff000);
				if (handle!=Nuclear::INVALID_TEXTBLOCK_HANDLE)
				{
					Nuclear::ITextBlock* pBlock=pFontMan->GetTextBlock(handle);
					pBlock->SetShadowColor(0xff200238);
					pFontMan->DrawText(handle,NameRect.left,NameRect.top+15,NULL);
					pFontMan->ReleaseTextBlock(handle);
				}
			}
		}


	}

}

void SoundApp::InitTypeEditBox(int type,CEdit* pEditBox)
{
	if (pEditBox!=NULL)
	{
		if (type>=0&&type<5)
		{
			pEditBox->SetWindowText(strNpceType[type]);
		}else
		{
			pEditBox->SetWindowText(strNpceType[0]);
		}
		
	}

}

bool SoundApp::OnSaveNpcBaseInf() //存储NPC信息
{
	XMLIO::CFileWriter fw;
	XMLIO::CONode node;
	fw.GetRootNode(node);//不需要判断返回值，因为fw是一个全新的对象，不依赖外界参数以及环境

	for(NpcBaseList::iterator it=m_NpcBaseList.begin();it!=m_NpcBaseList.end();++it)
	{

		XMLIO::CONode subnode;
		if ( !fw.CreateElement( node, L"record", subnode ) )
			continue;
		subnode.SetAttribute( L"id",(*it)->BaseID );
		subnode.SetAttribute( L"npctype",(*it)->eNpcType );
		subnode.SetAttribute( L"model",(*it)->strNpcModel );
		subnode.SetAttribute( L"name",(*it)->strName );
		subnode.SetAttribute( L"title",(*it)->strTitle );
		subnode.SetAttribute( L"chitchatid1",(*it)->chat1);
		subnode.SetAttribute( L"chitchatid2",(*it)->chat2 );
		subnode.SetAttribute( L"chitchatid3",(*it)->chat3 );

	}

	int retcode = fw.Save(L"config/AutoConfig/fire.gsp.npc.CNPCConfig.xml");
	return retcode == XMLIO::EC_SUCCESS;
}

bool SoundApp::OnSaveNpcInf()//存储NPCBASE信息
{
	XMLIO::CFileWriter fw;
	XMLIO::CONode node;
	fw.GetRootNode(node);//不需要判断返回值，因为fw是一个全新的对象，不依赖外界参数以及环境

	for(NpcList::iterator it=m_NpcList.begin();it!=m_NpcList.end();++it)
	{
		XMLIO::CONode subnode;
		if ( !fw.CreateElement( node, L"record", subnode ) )
			continue;
		Nuclear::Location WorldLoc=(*it)->GetLocation();
		Nuclear::Location LogicLoc=Nuclear::WorldLogicCoord::world2logic(WorldLoc.x,WorldLoc.y);
		subnode.SetAttribute( L"id",(*it)->m_pNpcBaseData->BaseID );
		subnode.SetAttribute( L"posx", LogicLoc.x);
		subnode.SetAttribute( L"posy",LogicLoc.y );
		subnode.SetAttribute( L"dir",(*it)->GetDirection());
	}

	int retcode = fw.Save(theApp.GetExePath().GetBuffer()+m_strNpcXmlName);
	return retcode == XMLIO::EC_SUCCESS;
}

void SoundApp::InitDirCombox(int dirType,CComboBox* pComBox)
{
	if (pComBox!=NULL)
	{
		pComBox->ResetContent();
		std::vector<int> vecDir;
		vecDir.clear();
		/*if (dirType==1)
		{
			vecDir.push_back(4);
		}else if (dirType==3)
		{
			vecDir.push_back(3);
			vecDir.push_back(4);
			vecDir.push_back(5);
		}else if (dirType==4)
		{
			vecDir.push_back(1);
			vecDir.push_back(3);
			vecDir.push_back(5);
			vecDir.push_back(7);
		}else if (dirType==8)
		{*/
			vecDir.push_back(0);
			vecDir.push_back(1);
			vecDir.push_back(2);
			vecDir.push_back(3);
            vecDir.push_back(4);
			vecDir.push_back(5);
			vecDir.push_back(6);
			vecDir.push_back(7);
		//}

		for ( size_t i=0;i<vecDir.size();i++)
		{
			int dir=vecDir[i];
			pComBox->AddString(strDirType[dir]);
		}
	}
}

void SoundApp::DelCurEditNpc()
{
	RemoveNpc(m_pEditNpc);

}

void SoundApp::RemoveNpc(CNpc* pNpc)
{
	NpcList::iterator it;
	it=std::find(m_NpcList.begin(),m_NpcList.end(),pNpc);
	if (it!=m_NpcList.end())
	{
		Nuclear::IWorld* pWorld = m_pEngine->GetWorld();
		if (!pWorld)
			return;
		pWorld->DeleteSprite(pNpc->m_pSprite);

		SpriteMap::iterator spriteiter = m_spriteMap.begin();
		for (;spriteiter!=m_spriteMap.end();++spriteiter)
		{
			if (spriteiter->first==pNpc->m_pSprite)
			{
				delete spriteiter->second;
				m_spriteMap.erase(spriteiter);
				break;
			}
		}
		m_NpcList.erase(it);
		if (m_pEditNpc==pNpc)
		{
			m_pEditNpc=NULL;
		}
		if (m_pCurSelNpc==pNpc)
		{
			m_pCurSelNpc=NULL;
		}

		//delete pNpc;
	}

}

int SoundApp::AddJumpPoint(int Logic_X,int Logic_Y,int destMapID,int Dest_X,int Dest_Y)
{
	if (m_pEngine)
	{
		Nuclear::IEffect* pEffect=m_pEngine->GetWorld()->SetEffect(L"animation/ui/mt_chuansong/chuansong",
			Nuclear::XPEL_UNDER_SPRITE,Logic_X,Logic_Y,false);
		if (pEffect!=NULL)
		{

			int newId=GenerateNewJumpPointID();
			stJumpPointInf JPInf;
			JPInf.id=newId;
			JPInf.effect=pEffect;
			JPInf.dest_mapID=destMapID;
			JPInf.dest_X=Dest_X;
			JPInf.dest_Y=Dest_Y;
			m_vecJumpVector.push_back(JPInf);
			return newId;
		}
	}
	return -1;

}
int  SoundApp::SelectJumpPointByPoint(CPoint pt) //选择跳转点
{
	for (size_t i=0;i<m_vecJumpVector.size();++i)
	{
		Nuclear::Location Pos=m_vecJumpVector[i].effect->GetLocation();
		Nuclear::Location selectPos(pt);
		if (abs(selectPos.x-Pos.x)<=(s_iJumPointGridNum/2+1)*GRID_WIDTH&&abs(selectPos.y-Pos.y)<=(s_iJumPointGridNum/2+1)*GRID_HEIGHT)
		{
			if (m_vecJumpVector[i].id==m_selectJumpPointID)
			{
				continue;
			}
			return m_vecJumpVector[i].id;
		}

	}
	return -1;

}
void SoundApp::DelectJumpPoint(int id) //删除一个跳转点
{
	for (size_t i=0;i<m_vecJumpVector.size();++i)
	{
		int curID=m_vecJumpVector[i].id;
		if (curID==id)
		{
			m_pEngine->GetWorld()->RemoveEffect(m_vecJumpVector[i].effect);
			m_vecJumpVector.erase(m_vecJumpVector.begin()+i);
			if (m_selectJumpPointID==id)
			{
				SetSelJumpPoint(-1);
			}
			break;
		}
	}

}
void SoundApp::LoadJumpPoint(const std::wstring& strGotoXmlName) //载入该地图的跳转点信息
{
	DeleAllJumPoint();
	m_strGotoXmlName=strGotoXmlName;

	Nuclear::IWorld* pWorld = m_pEngine->GetWorld();
	if (!pWorld)
		return;


	XMLIO::CFileReader fr;
	XMLIO::CNodeList typenl;
	if (!GetXmlNodeList(strGotoXmlName,fr,typenl))
		return ;

	int firstrecord=s_iJumPointGridNum*s_iJumPointGridNum/2;
	int step=s_iJumPointGridNum*s_iJumPointGridNum;
	int curIdx=firstrecord;
	size_t size=typenl.size();
	if (step>size)
	{
		fr.CloseFile();
		return;
	}
	for( ;;)
	{

		if (curIdx>=size)
		{
			break;
		}
		XMLIO::CNodeList::iterator typeIt = typenl.begin()+curIdx;
		int pos_X=typeIt->GetAttributeInteger(L"posx");
		int pos_Y=typeIt->GetAttributeInteger(L"posy");
		int mapID=typeIt->GetAttributeInteger(L"destmap");
		int dest_X=typeIt->GetAttributeInteger(L"destx");
		int dest_Y=typeIt->GetAttributeInteger(L"desty");

		AddJumpPoint(pos_X*24,pos_Y*16,mapID,dest_X,dest_Y);

		curIdx+=step;
	}

	fr.CloseFile();
}

void SoundApp::DeleAllJumPoint()
{
	for(std::vector<stJumpPointInf>::iterator it=m_vecJumpVector.begin();it!=m_vecJumpVector.end();++it)
	{
		m_pEngine->GetWorld()->RemoveEffect((*it).effect);
	}
	m_vecJumpVector.clear();
	SetEditJumPoint(-1);
	SetSelJumpPoint(-1);

}
bool SoundApp::SaveJumpPointInf() //保存信息
{
	XMLIO::CFileWriter fw;
	XMLIO::CONode node;
	fw.GetRootNode(node);//不需要判断返回值，因为fw是一个全新的对象，不依赖外界参数以及环境

	for(std::vector<stJumpPointInf>::iterator it=m_vecJumpVector.begin();it!=m_vecJumpVector.end();++it)
	{
		int mapID=(*it).dest_mapID;
		int Dest_X=(*it).dest_X;
		int Dest_Y=(*it).dest_Y;

		Nuclear::Location loc=(*it).effect->GetLocation();
		int Grid_X=loc.x/24-s_iJumPointGridNum/2;
		int Grid_Y=loc.y/16-s_iJumPointGridNum/2;

		for (int x=Grid_X;x<Grid_X+s_iJumPointGridNum;++x)
		{
			for (int y=Grid_Y;y<Grid_Y+s_iJumPointGridNum;++y)
			{
				XMLIO::CONode subnode;
				if ( !fw.CreateElement( node, L"item", subnode ) )
					continue;

				subnode.SetAttribute(L"posx",x);
				subnode.SetAttribute(L"posy",y);
				subnode.SetAttribute( L"destmap",mapID );
				subnode.SetAttribute( L"destx",Dest_X );
				subnode.SetAttribute( L"desty",Dest_Y );
			}

		}
	}

	int retcode = fw.Save(theApp.GetExePath().GetBuffer()+m_strGotoXmlName);
	return retcode == XMLIO::EC_SUCCESS;
}

void SoundApp::AddNewJumpPoint()
{
	Nuclear::CRECT vp = m_pEngine->GetWorld()->GetViewport();
	Nuclear::Location pt=vp.Center();
	int id=AddJumpPoint(pt.x,pt.y,0,0,0);
	SetSelJumpPoint(id);
	SetEditJumPoint(id);
}

int SoundApp::GenerateNewJumpPointID()
{
	int curMaxID=-1;
	for (size_t i=0;i<m_vecJumpVector.size();++i)
	{
		int curID=m_vecJumpVector[i].id;
		if (curMaxID<curID)
		{
			curMaxID=curID;
		}
	}
	return curMaxID+1;

}
void SoundApp::SetSelJumpPoint(int id)
{
	m_selectJumpPointID=id;
}

void SoundApp::SetEditJumPoint(int id)
{
	if (m_pJPInfDlg==NULL)
	{

		CMainFrame* pMainFrame=(CMainFrame*)AfxGetApp()->m_pMainWnd;
		if (pMainFrame!=NULL)
		{
			CPropView* pPropView=(CPropView*)pMainFrame->m_HSplitterWindow.GetPane(0,1);
			if (pPropView)
			{
				m_pJPInfDlg=pPropView->m_tabPropPanel.m_pJPInfDlg;

			}
			//m_pNpcDlg=(CNpcPropertyDlg*)pMainFrame->GetDlgItem(IDD_NPCPANEL);
		}

	}

	if (m_EditJumpPointID!=id)
	{
		m_EditJumpPointID=id;

		if (m_EditJumpPointID!=-1)
		{
			for (size_t i=0;i<m_vecJumpVector.size();++i)
			{
				int curID=m_vecJumpVector[i].id;
				if (curID==m_EditJumpPointID)
				{
					int mapID=m_vecJumpVector[i].dest_mapID;
					int Dest_X=m_vecJumpVector[i].dest_X;
					int Dest_Y=m_vecJumpVector[i].dest_Y;
					m_pJPInfDlg->ChangeJPInf(mapID,Dest_X,Dest_Y);
					break;
				}
			}
		}else
		{
			m_pJPInfDlg->ChangeJPInf(0,0,0);
		}

	}
}

void SoundApp::ChangeEditJPInf(int mapID,int Dest_X,int Dest_Y)
{
	if (m_EditJumpPointID!=-1)
	{
		for (size_t i=0;i<m_vecJumpVector.size();++i)
		{
			int curID=m_vecJumpVector[i].id;
			if (curID==m_EditJumpPointID)
			{
				m_vecJumpVector[i].dest_mapID=mapID;
				m_vecJumpVector[i].dest_X=Dest_X;
				m_vecJumpVector[i].dest_Y=Dest_Y;
				break;
			}
		}
	}
}

void SoundApp::DelCurEditJumPoint()
{
	if (m_EditJumpPointID!=-1)
	{
		DelectJumpPoint(m_EditJumpPointID);
	}

}

void SoundApp::MoveSelJumpPoint(CPoint pt)
{
	if (m_selectJumpPointID!=-1)
	{
		for (size_t i=0;i<m_vecJumpVector.size();++i)
		{
			int curID=m_vecJumpVector[i].id;
			if (curID==m_selectJumpPointID)
			{
				Nuclear::Location loc(pt.x,pt.y);
				m_vecJumpVector[i].effect->SetLocation(loc);
				break;
			}
		}
	}

}

void SoundApp::DrawJumpPoint() //画选择的跳转点的标志
{

	for (size_t i=0;i<m_vecJumpVector.size();++i)
	{
		int curID=m_vecJumpVector[i].id;
		//if (curID==m_EditJumpPointID)
		{
			Nuclear::Location loc;
			Nuclear::Renderer* pRenderer = m_pEngine->GetRenderer();
			Nuclear::CRECT vp = m_pEngine->GetWorld()->GetViewport();//vp可能会在画SelectedArea那里改了
			loc=m_vecJumpVector[i].effect->GetLocation();
			loc.x=(loc.x/24)*24;
			loc.y=(loc.y/16)*16;
			Nuclear::FRECT rect;
			rect.top=(float)(loc.y-vp.top)-(s_iJumPointGridNum/2)*GRID_HEIGHT;
			rect.left=(float)(loc.x-vp.left)-(s_iJumPointGridNum/2)*GRID_WIDTH;
			rect.right=rect.left+s_iJumPointGridNum*GRID_WIDTH;
			rect.bottom=rect.top+s_iJumPointGridNum*GRID_HEIGHT;

			Nuclear::XPCOLOR color=0xccffff00;
			if (curID==m_EditJumpPointID)
			{
				color=0xcc87ceeb; 

				
				
			}
			pRenderer->DrawBox(rect,color);		}
	}
}

void SoundApp::SetRegion(const int& x, const int& y , RegionType rt)
{
	m_RegionBuffer.AddPoint(x,y,rt);

	//2015 Yao
	AddMonsterPoint(x,y);
}

void SoundApp::SetRegionBig(const int& x, const int& y , RegionType rt)
{
	for (int i = -m_EdgeSize / 2; i < m_EdgeSize / 2 ; ++i)
	{
		int dx = x + i;
		if (dx < 0 || dx >= m_mapGridWidth)
			continue;

		for (int j = -m_EdgeSize / 2; j < m_EdgeSize / 2; ++j )
		{
			int dy = y + j;
			if (dy < 0 || dy >= m_mapGridHeight)
				continue;

			m_RegionBuffer.AddPoint(dx,dy,rt);

			//2015 Yao
			AddMonsterPoint(dx,dy);
		}
	}
}

void SoundApp::DelRegion(const int& x, const int& y , RegionType rt)
{
	m_RegionBuffer.DelPoint(x,y,rt);

	//2015 Yao
	DelMonsterPoint(x,y);
}

void SoundApp::DelRegionBig(const int& x, const int& y , RegionType rt)
{
	for (int i = -m_EdgeSize / 2; i < m_EdgeSize / 2 ; ++i)
	{
		int dx = x + i;
		if (dx < 0 || dx >= m_mapGridWidth)
			continue;

		for (int j = -m_EdgeSize / 2; j < m_EdgeSize / 2; ++j )
		{
			int dy = y + j;
			if (dy < 0 || dy >= m_mapGridHeight)
				continue;
			m_RegionBuffer.DelPoint(dx,dy,rt);

			//2015 Yao
			DelMonsterPoint(dx,dy);
		}
	}
}


bool SoundApp::GetIsTypeOfRegion(const int& x, const int& y , RegionType rt)
{
	return m_RegionBuffer.CheckPointType(x,y,rt);
}

void SoundApp::WriteRegionBufferToFile(std::wstring filename)
{
	if (!m_RegionBuffer.IsEmpty())
 		m_RegionBuffer.SaveToNativePath(filename.c_str());
}

void SoundApp::ReadRegionBufferFromFile(std::wstring filename)
{
	if (!m_RegionBuffer.LoadFromNativePath(filename.c_str()))
	{
		m_RegionBuffer.Init(m_mapGridWidth,m_mapGridHeight);
	}
}

//2015 Yao MonsterSetting
void SoundApp::AddMonsterPoint(const int& w, const int& h)
{
	assert(w <= 65535 || h <= 65535);
	unsigned int key = w << 16 | h;

	if (m_Monbuffer.count(key) > 0)
	{
		m_Monbuffer[key] = monKind;
	}
	else{
		m_Monbuffer[key] = monKind;
	}	
}

void SoundApp::DelMonsterPoint(const int& x, const int& y)
{
	assert(x <= 65535 || y <= 65535);
	unsigned int key = x << 16 | y;
	
	if (m_Monbuffer.count(key) > 0)
	{
		int val = m_Monbuffer[key];
		
		val &= ~val;
		
		m_Monbuffer.erase(key);
	}
}

bool SoundApp::SaveMonsterKind(std::wstring filename)
{
	//-if (m_Monbuffer.size()>0)
	{	
		FILE *f = Nuclear::FOpen(filename.c_str(), L"wb");
		if( f == NULL )
			return false;
		
		Nuclear::XBuffer	osXBuffer;
		Nuclear::XOStream	osXStream;

		try {

			int headfile = 16;
			osXStream.push_byte(&headfile,sizeof(int));
			
			osXStream << m_mapGridWidth;
			osXStream << m_mapGridHeight;
			osXStream << (int)m_Monbuffer.size();

			std::map<unsigned int,int>::const_iterator it		= m_Monbuffer.begin();
			std::map<unsigned int,int>::const_iterator itend	= m_Monbuffer.end();
		
			while(it != itend)
			{
				osXStream << it->first<< it->second;
				++it;
			}
		}
		catch(Nuclear::XMarshal::Exception)
		{
			return false;
		}

		osXBuffer = osXStream;

		bool r = Nuclear::FWrite(f,osXBuffer.constbegin(),osXBuffer.size());
        if(!r)
			return false;

		fclose(f);
		return r;
	}
}

bool SoundApp::ReadMonsterKind(std::wstring filename)
{
	FILE *f = Nuclear::FOpen(filename.c_str(), L"rb");
	if( f == NULL )
		return false;
	
	if( f == NULL || -1 == fseek(f, 0, SEEK_END) ) return false;
	int len = ftell(f);
	if( -1 == len || -1 == fseek(f, 0, 0) ) return false;

	Nuclear::XBuffer	osXBuffer;
	osXBuffer.resize(len);

	if( 0 != Nuclear::FRead(f, osXBuffer.normalbegin(), len) )
	{
		Nuclear::XIStream osXIStream(osXBuffer);
		try {

			int headfile = 16;
			osXIStream.pop_byte(&headfile,sizeof(int));
			m_Monbuffer.clear();

			size_t w, h;
			osXIStream >> w;
			osXIStream >> h;

			if(w == m_mapGridWidth && h == m_mapGridHeight)
			{
				size_t bsize = m_Monbuffer.size();

				osXIStream >> bsize;

				for (size_t i = 0; i < bsize; ++i)
				{
					unsigned int key;
					int val;

					osXIStream >> key;
					osXIStream >> val;

					m_Monbuffer[key] = val;
				}
			}
		}
		catch(Nuclear::XMarshal::Exception)
		{
			return false;
		} 
	}
	else
	{
		return false;
	}

	fclose(f);
	return true;
}

int SoundApp::CheckMonsterPoint(const unsigned int& w, const unsigned int& h)
{
	assert(w <= 65535 || h <= 65535);
	unsigned int key = w << 16 | h;

	if (m_Monbuffer.count(key) == 0)
	{	
		return -1;	
	}
	else
	{	
		int val = m_Monbuffer[key];
		return val;
	}
}

void SoundApp::LoadAllMonster()
{
	XMLIO::CFileReader fr;
	XMLIO::CNodeList typenl;

	std::wstring strMonsterXmlPath=	L"/table/xmltable/map.cmineareainfo.xml";
	if (!GetXmlNodeList(strMonsterXmlPath,fr,typenl))
		return ;

	for( XMLIO::CNodeList::iterator typeIt = typenl.begin(); typeIt != typenl.end(); ++typeIt)
	{
		stMonsterBaseData* pMonsterBase	=new stMonsterBaseData;
		pMonsterBase->BaseID			=(int)typeIt->GetAttributeInteger(L"id");
		pMonsterBase->strMonsterColor	=typeIt->GetAttribute(L"colour");
		pMonsterBase->strMonsterDescribe=(std::wstring)typeIt->GetAttribute(L"discribe");
		m_monsterBaseList.push_back(pMonsterBase);
	}
	fr.CloseFile();
}

void SoundApp::CreateMonCombo(CComboBox* pMonsterCombo)
{
	for ( int i=0;i<m_monsterBaseList.size();i++)
	{	
		int iMonId						= m_monsterBaseList[i]->BaseID;
		wchar_t charMonId[10];
		_itow_s(iMonId,charMonId,10,10);
		std::wstring strMonID			= charMonId;

		std::wstring strMonDes			= m_monsterBaseList[i]->strMonsterDescribe;
		std::wstring strMonResult		= strMonID + L"   " +strMonDes;

		pMonsterCombo->AddString(strMonResult.c_str());
	}
}

int SoundApp::GetMonKind(int id)
{
	return m_monsterBaseList[id]->BaseID;
}

void SoundApp::SetMonKind(int id)
{
	monKind = id;
}

void SoundApp::ShowJumpInf(CPoint pt)
{
	int id=SelectJumpPointByPoint(pt);
	if (id!=-1)
	{
		SetEditJumPoint(id);
	}
}
bool SoundApp::PointCanJump(CPoint pt)
{
	int Grid_X=pt.x/24-s_iJumPointGridNum/2;
	int Grid_Y=pt.y/16-s_iJumPointGridNum/2;

	for (int x=Grid_X;x<Grid_X+s_iJumPointGridNum;++x)
	{

		for (int y=Grid_Y;y<Grid_Y+s_iJumPointGridNum;++y)
		{
			unsigned int mask = GetMazeMask(CPoint(x* GRID_WIDTH, y* GRID_HEIGHT));
			if ( ( mask & 0x3 ) == 0x3 ) //是阻挡点
			{
				return false;

			}
		}

	}
	return true;
}

CPoint SoundApp::FixPoint(const CPoint& pt)
{
	CPoint newPt(pt);
	newPt.x=(pt.x/24)*24;
	newPt.y=(pt.y/16)*16;
	return newPt;
}

int  SoundApp::GetComboxIdxByTypeAndDir(int dirType,int dir)
{
	
	if (dirType<0&&dirType>=8)
	{
		return -1;
	}
	return dir;
	
}

int SoundApp::GetDirByComboxIdxAndDirType(int dirType,int idx)
{
	if (dirType<0&&dirType>=8)
	{
		return -1;
	}
	if (dirType==1)
	{
		return 4;
	}else if (dirType==3&&idx<3)
	{
		return idx+3;
	}else if (dirType==4&&idx<4)
	{
		return idx*2+1;
	}else if (dirType==8)
	{
		return idx;
	}
	return -1;

}

void SoundApp::ReleaseJumpBlockInf()
{
	if (NULL!=m_JumpBlockBuffer)
	{
		delete []m_JumpBlockBuffer;
		m_JumpBlockBuffer=NULL;
	}
}


void SoundApp::NewJumpBlockBuffer()
{
	ReleaseJumpBlockInf();

	m_JumpBlockBuffer = new unsigned char[m_mapGridWidth*m_mapGridHeight];
	memset(m_JumpBlockBuffer,0,m_mapGridWidth*m_mapGridHeight);

}
bool SoundApp::LoadJumpBlockInf(std::wstring jumpBlockFileName)
{
	NewJumpBlockBuffer();

	
	CRegionBuffer<unsigned char> jumpBlockBuffer;

	if (jumpBlockBuffer.LoadFromNativePath(jumpBlockFileName.c_str()))
	{
		
		for (int x=0;x<m_mapGridWidth;++x)
		{
			for (int y=0;y<m_mapGridHeight;++y)
			{
				unsigned char mask=jumpBlockBuffer.GetPointValue(x,y);
				m_JumpBlockBuffer[x+y*m_mapGridWidth]=mask;
			}
		}
		return true;
	}
	
	return false;

}

unsigned char SoundApp::GetJumpBlock(CPoint pixel)
{
	pixel.x /= GRID_WIDTH;
	pixel.y /= GRID_HEIGHT;

	if (pixel.x >= 0 && pixel.x <= m_mapGridWidth
		&& pixel.y>=0 && pixel.y <=m_mapGridHeight)
	{
		return m_JumpBlockBuffer[pixel.x + pixel.y*m_mapGridWidth];
	}
	
	return 0;
}


void SoundApp::ClearJumpBlock(CPoint center,int edge)
{
	center.x /= GRID_WIDTH;
	center.y /= GRID_HEIGHT;

	for (int i = -edge / 2; i < edge / 2 ; ++i)
	{
		int dx = center.x + i;
		if (dx < 0 || dx >= m_mapGridWidth)
			continue;

		for (int j = -edge / 2; j < edge / 2; ++j )
		{
			int dy = center.y + j;
			if (dy < 0 || dy >= m_mapGridHeight)
				continue;

			m_JumpBlockBuffer[dx  + dy * m_mapGridWidth] = 0x00;
		}
	}
}

void SoundApp::SetJumpBlock(CPoint pixel,bool brush,unsigned char type)
{
	pixel.x /= GRID_WIDTH;
	pixel.y /= GRID_HEIGHT;
	
	if (brush)
	{
		for (int i=-m_EdgeSize / 2; i<m_EdgeSize / 2; i++)
		{
			for (int j=-m_EdgeSize / 2; j<m_EdgeSize / 2; j++)
			{
				if ((pixel.x+i+(pixel.y+j)*m_mapGridWidth) >= 0 && (pixel.x+i+(pixel.y+j)*m_mapGridWidth) < m_mapGridWidth*m_mapGridHeight)
				{
					//如果是刷鼠标点击效果点，则先清空原来的效果
					/*if (mask == 0x01<<6 || mask == 0x02<<6 || mask == 0x03<<6)
					{
						m_JumpBlockBuffer[pixel.x + i + (pixel.y+j)*m_mapGridWidth] &= 0x3F;
					}*/
					m_JumpBlockBuffer[pixel.x + i + (pixel.y+j)*m_mapGridWidth] =type;
				}
			}
		}
	}
	else
	{
		//如果是刷鼠标点击效果点，则先清空原来的效果
		/*if (mask == 0x01<<6 || mask == 0x02<<6 || mask == 0x03<<6)
		{
			m_JumpBlockBuffer[pixel.x + pixel.y*m_mapGridWidth] &= 0x3F;
		}*/
		m_JumpBlockBuffer[pixel.x + pixel.y*m_mapGridWidth]= type;
	}

}

void SoundApp::DrawGuDaoInf(int layer)
{
	char* buffer=NULL;

	int islandNum=0;

	if (layer==1)
	{
		if (NULL==m_IslandBuffer||m_IsLandNumber<=0)
		{
			return;
		}
		buffer=m_IslandBuffer;
		islandNum=m_IsLandNumber;
	}else if (layer==2)
	{
		if (NULL==m_IslandBuffer2||m_IsLandNumber2<=0)
		{
			return;
		}
		buffer=m_IslandBuffer2;
		islandNum=m_IsLandNumber2;
	}
	
	Nuclear::CRECT vp = m_pEngine->GetWorld()->GetViewport();//vp可能会在画SelectedArea那里改了
	const Nuclear::FPOINT view = vp.GetPos().ToFPOINT();
	for (int x = vp.left/GRID_WIDTH; x <= vp.right/GRID_WIDTH; ++x)
	{
		for (int y = vp.top/GRID_HEIGHT; y <= vp.bottom/GRID_HEIGHT; ++y)
		{
			//RY_QIEZUO
		    unsigned char type=GetJumpBlock(CPoint(x*GRID_WIDTH,y*GRID_HEIGHT));		
		
			char islandIndex=buffer[x+y*m_mapGridWidth];
			int intIdx=(int)islandIndex;

			Nuclear::FRECT rect(Nuclear::FRECT(Nuclear::FPOINT(x * GRID_WIDTH, y * GRID_HEIGHT), GRID_WIDTH, GRID_HEIGHT)-view);


			Nuclear::XPCOLOR xpcolor(0xFFFF0000);

			if (islandIndex!=-1)
			{
				DWORD step=(0xFFFFFF/islandNum)*intIdx;

				unsigned char r=(step>>16)&0xff;
				unsigned char g=(step>>8)&0xff;
				unsigned char b=step&0xff;

				xpcolor.Reset(b,g,r,0xFF);
			}

			wchar_t buffer[32]={0};
			wsprintf(buffer,L"%d",intIdx);

			std::wstring strText(buffer);

			Nuclear::CRECT NameRect(0,0,0,0);
			NameRect.left=(int)(rect.left);
			NameRect.top=(int)(rect.top);
			NameRect.right=(int)(rect.right);
			NameRect.bottom=(int)(rect.bottom);

			m_pEngine->GetRenderer()->DrawText(strText.c_str(),strText.size(),&NameRect,xpcolor);
		}
	}
}

void SoundApp::DrawJumpBlockRect()
{
	m_BlockRectList.clear();
	Nuclear::CRECT vp = m_pEngine->GetWorld()->GetViewport();//vp可能会在画SelectedArea那里改了
	const Nuclear::FPOINT view = vp.GetPos().ToFPOINT();
	for (int x = vp.left/GRID_WIDTH; x <= vp.right/GRID_WIDTH; ++x)
	{
		for (int y = vp.top/GRID_HEIGHT; y <= vp.bottom/GRID_HEIGHT; ++y)
		{
			//RY_QIEZUO
			unsigned char type=GetJumpBlock(CPoint(x*GRID_WIDTH,y*GRID_HEIGHT));
			
			DWORD color=0;

			switch (type)
			{
			case QG_1:
				{
					color=0x3FFF0000;

				}
				break;
			case QG_2:
				{
					color=0x3F00FF00;
				}
				break;
			case QG_3:
				{
					color=0x3F0000FF;
				}
				break;
			case QG_4:
				{
					color=0x3FFF00FF;
				}
				break;
			}
			if (color!=0)
			{							
				Nuclear::FRECT rect(Nuclear::FRECT(Nuclear::FPOINT(x * GRID_WIDTH, y * GRID_HEIGHT), GRID_WIDTH, GRID_HEIGHT)-view);
				m_pEngine->GetRenderer()->DrawBox(rect,color);
			}
		}
	}

}

void SoundApp::SaveJumpBlockInf(std::wstring JumpBlockFileName)
{	
	CRegionBuffer<unsigned char> jumpblockBuffer;
	jumpblockBuffer.Init(m_mapGridWidth,m_mapGridHeight);
	
	size_t length=0;
	for (size_t x=0;x<m_mapGridWidth;++x)
	{
		for (size_t y=0;y<m_mapGridHeight;++y)
		{
			char mask=m_JumpBlockBuffer[x+y*m_mapGridWidth];
			if (mask>0)
			{
				length++;
				jumpblockBuffer.AddPoint(x,y,mask);
			}
		}
	}

	/*if (!jumpblockBuffer.IsEmpty())*/
		jumpblockBuffer.SaveToNativePath(JumpBlockFileName.c_str());
}

void SoundApp::prinfBlock(char* blockinfo,int x_grid_num,int y_grid_num)
{
	OutputDebugStringA("******intput*********\n");

	for(int x = 0; x < x_grid_num;x++)
	{
		for(int y = 0; y < y_grid_num;y++)
		{
			char mask=blockinfo[x+y*x_grid_num];
			char msg[128]={0};
			sscanf_s(msg,"%d ",mask);
			OutputDebugStringA(msg);
		}
		OutputDebugStringA("\n");
	}

	OutputDebugStringA("******output*********\n");
}


void SoundApp::SaveIslandInf(std::wstring islandInfFilemame,int layer) //保存孤岛信息
{
	int x_gridNum=m_mapGridWidth;
	int y_gridNum=m_mapGridHeight;
	if (x_gridNum>0&&y_gridNum>0)
	{
		char* buffer=NULL;
		if (layer==1)
		{
			if (m_IslandBuffer!=NULL)
			{
				delete []m_IslandBuffer;
				m_IslandBuffer=NULL;
			}
			m_IslandBuffer = new  char[x_gridNum*y_gridNum];
			memset(m_IslandBuffer,0,x_gridNum*y_gridNum);
			buffer=m_IslandBuffer;
		}else if (layer==2)
		{
			if (m_IslandBuffer2!=NULL)
			{
				delete []m_IslandBuffer2;
				m_IslandBuffer2=NULL;
			}
			m_IslandBuffer2 = new  char[x_gridNum*y_gridNum];
			memset(m_IslandBuffer2,0,x_gridNum*y_gridNum);
			buffer=m_IslandBuffer2;
		}else
		{
			return;
		}
		
		for(int x = 0; x < x_gridNum;x++)
		{
			for(int y = 0; y < y_gridNum;y++)
			{
				unsigned char mask= m_MazeBuffer[x + y*x_gridNum];
				char newMask=0;
				char checkMask=0x3;
				if (layer==2)
				{
					checkMask=s_BlockLayer2Mask;
				}
				if (mask&checkMask)
				{
					newMask=-1;
				}

				/*int kk=rand()%2;
				if (kk=0)
				{
					newMask=-1;
				}*/
				buffer[x+y*x_gridNum]=newMask;
			}
		}

		//prinfBlock(islandBuffer,x_gridNum,y_gridNum);

		CRegionBuffer<char> islandBuffer;
		islandBuffer.Init(m_mapGridWidth,m_mapGridHeight);
		
		int isLandNum=processBlockInfo(buffer,x_gridNum,y_gridNum);

		if (layer==1)
		{
			m_IsLandNumber=isLandNum;
		}else
		{
			m_IsLandNumber2=isLandNum;
		}

		for(int x1 = 0; x1 < x_gridNum;x1++)
		{
			for(int y1 = 0; y1 < y_gridNum;y1++)
			{
				char mask= buffer[x1 + y1*x_gridNum];
				if (mask>0)
				{
					islandBuffer.AddPoint(x1,y1,mask);
				}
			}
		}

		/*if (!islandBuffer.IsEmpty())*/
			islandBuffer.SaveToNativePath(islandInfFilemame.c_str());

		CString strMsg;
		strMsg.Format(L"本地图共有%d个%d层孤岛",isLandNum,layer);
		MessageBox(NULL,strMsg,L"提示",0);
	}
}

int SoundApp::processBlockInfo(char* blockinfo,int x_grid_num,int y_grid_num)
{
	int islandindex = 1;
	for(int x = 0; x < x_grid_num;x++)
	{
		for(int y = 0; y < y_grid_num;y++)
		{
			if(blockinfo[x+y*x_grid_num] != 0)
				continue;
			blockinfo[x+y*x_grid_num] = islandindex;
			//processIsland(blockinfo,x,y,x_grid_num,y_grid_num,islandindex);
			std::vector<Nuclear::Location> poses ;
			poses.push_back(Nuclear::Location(x,y));
			std::vector<Nuclear::Location> outPoes;
			while(!poses.empty())
			{
				 processAdjacentPoses(blockinfo, poses,outPoes,x_grid_num,y_grid_num, islandindex);
				 poses.clear();
				 std::vector<Nuclear::Location>::iterator it=outPoes.begin();
				 for (;it!=outPoes.end();++it)
				 {
					 poses.push_back(Nuclear::Location((*it).x,(*it).y));
				 }
				 outPoes.clear();
			}
			islandindex++;
		}
	}
	return islandindex-1;
}
	
	/**
	 * 从一个非阻挡点深度遍历寻找该点所处的岛
	 * @param blockinfo
	 * @param x
	 * @param y
	 * @param islandindex
	 */
void SoundApp::processIsland(char* blockinfo,int x, int y,int x_grid_num,int y_grid_num,  int islandindex)
{
	int maxx = x_grid_num;
	int maxy = y_grid_num;

	std::vector<Nuclear::Location> vecPos;
	getAdjacentPos(vecPos,x,y,maxx,maxy);
	std::vector<Nuclear::Location>::iterator it=vecPos.begin();
	for(;it!=vecPos.end();++it)
	{
		int tmpx = (*it).x;
		int tmpy =(*it).y;
		if(blockinfo[tmpx+tmpy*x_grid_num] != 0)
			continue;
		if(!validPos(blockinfo, x, y, tmpx, tmpy,maxx,maxy))
			continue;
		blockinfo[tmpx+tmpy*x_grid_num] = islandindex;
		processIsland(blockinfo, tmpx, tmpy,maxx,maxy, islandindex);
	}
}
	
	/**
	 * 不合法的单层斜向阻挡点，即如下的阻挡是非法的，不应该出现的情况
	 * 	———— ————
	 * |阻挡	|非     |
	 * | 	|阻挡|
	 *  ———— ————
	 * |非	|    |
	 * |阻挡	|阻挡|
	 *  ———— ————
	*/
bool SoundApp::validPos(char* blockinfo, int x, int y, int tmpx, int tmpy,int x_grid_num,int y_grid_num)
{
	if(x == tmpx || y == tmpy)
		return true;
	if(blockinfo[x+tmpy*x_grid_num] == -1 && blockinfo[tmpx+y*x_grid_num] == -1)
	{
		//System.out.println("坐标("+ x +","+ y+")与坐标(" +tmpx +","+ tmpy +")位置不合法");
		////TODO 抛出异常或提示框，终止保存
		return false;
	}
	return true;
}
	
	/**
	 * @return 获取邻近点，最多八个
	 */
 void SoundApp::getAdjacentPos(std::vector<Nuclear::Location>& vecPos,int x, int y,int maxx,int maxy)
 {
	vecPos.clear();

	 for(int i=x-1; i<=x+1; i++){
		for(int j=y-1; j<=y+1; j++)
		{
			if(i>=0 && i < maxx && j>=0  && j < maxy)
			{
				if(i==x && j==y)
					continue;
				Nuclear::Location pt(i,j);
				vecPos.push_back(pt);
			}
		}
	}
	
}

 void SoundApp::processAdjacentPoses(char* blockinfo, const std::vector<Nuclear::Location>& inputVecPos,
	 std::vector<Nuclear::Location>& outVecPos,int x_grid_num,int y_grid_num, char islandindex)
{
	 int maxx = x_grid_num;
	 int maxy = y_grid_num;
	 
	 std::vector<Nuclear::Location>::const_iterator it=inputVecPos.begin();
	 

	 for(;it!=inputVecPos.end();++it)
	 {
		  std::vector<Nuclear::Location> findAdjVec;

		 getAdjacentPos(findAdjVec,(*it).x,(*it).y,x_grid_num,y_grid_num);

		 std::vector<Nuclear::Location>::iterator adjIt=findAdjVec.begin();
		 for(;adjIt!=findAdjVec.end();++adjIt)
		{
			int tmpx = (*adjIt).x;
			int tmpy = (*adjIt).y;
			if(blockinfo[tmpx+tmpy*x_grid_num] != 0)
				continue;
			if(!validPos(blockinfo, (*it).x, (*it).y, tmpx, tmpy,x_grid_num,y_grid_num))
				continue;
			blockinfo[tmpx+tmpy*x_grid_num] = islandindex;
			outVecPos.push_back(Nuclear::Location(tmpx,tmpy));
		}
	 }
	
}

void SoundApp::DrawFreeArea(int regionType)
{
	Nuclear::Renderer* pRenderer = m_pEngine->GetRenderer();
	Nuclear::CRECT vp = m_pEngine->GetWorld()->GetViewport();//vp可能会在画SelectedArea那里改了
	switch (regionType)
	{
	case 18:
	case 19:
	case 20:
	case 21:
	case 22:
	case 23:
		{
			m_BlockRectList.clear();
			const Nuclear::FPOINT view = vp.GetPos().ToFPOINT();
			for (int x = vp.left/GRID_WIDTH; x <= vp.right/GRID_WIDTH; ++x)
			{
				for (int y = vp.top/GRID_HEIGHT; y <= vp.bottom/GRID_HEIGHT; ++y)
				{
					//RY_QIEZUO
					int regionTypeMask=1<<(regionType-8);
					if (GetIsTypeOfRegion(x,y,(RegionType)regionTypeMask))
						m_BlockRectList.push_back(Nuclear::FRECT(Nuclear::FPOINT(x * GRID_WIDTH, y * GRID_HEIGHT), GRID_WIDTH, GRID_HEIGHT)-view);
				}
			}
			if (!m_BlockRectList.empty())
			{
				DWORD color=GetFreeAreaColor(regionType);
				pRenderer->DrawBox(&m_BlockRectList[0], (int)m_BlockRectList.size(), color);
			}
		}
		break;
	}

}
DWORD SoundApp::GetFreeAreaColor(int regionType)
{
	int idx=regionType-18;
	if (idx>=0&&idx<6)
	{
		return freeAreaColor[idx];
	}

	return 0xFFFFFFFF;
}

void SoundApp::JumpToIsland(int islandIdx) //跳到孤岛的第一个格子坐为屏幕的中心
{
	if (NULL==m_IslandBuffer||m_IsLandNumber<=0)
	{
		return;
	}

	if (m_regionType!=17)
	{
		return;
	}
	CString strMsg("");

	if (islandIdx>0&&islandIdx<=m_IsLandNumber)
	{
		for (int x = 0; x <= m_mapGridWidth; ++x)
		{
			for (int y = 0; y <= m_mapGridHeight; ++y)
			{
				//RY_QIEZUO
				unsigned char type=GetJumpBlock(CPoint(x*GRID_WIDTH,y*GRID_HEIGHT));

				char islandIndex=m_IslandBuffer[x+y*m_mapGridWidth];
				int intIdx=(int)islandIndex;

				if (intIdx==islandIdx&&m_pEngine)
				{
					int newViewPortX=x*GRID_WIDTH-512;
					int newViewPortY=y*GRID_HEIGHT-384;
					
					if (newViewPortX<0)
					{
						newViewPortX=0;
					}
					if (newViewPortY<0)
					{
						newViewPortY=0;
					}
					m_pEngine->GetWorld()->SetViewportLT(newViewPortX, newViewPortY);

					strMsg.Format(L"%d号孤岛第一个格子屏幕坐标(%d,%d)",islandIdx,x*GRID_WIDTH-newViewPortX,y*GRID_HEIGHT-newViewPortY);

					break;
				}
			}
		}

	}
	if (!strMsg.IsEmpty())
	{
		MessageBox(NULL,strMsg,L"提示",0);
	}
}

void SoundApp::CheckMazeLayer2IsNew()//检测2层阻挡是不是没刷过，没有任何二层阻挡则认为是新的，把这层的阻挡全刷上
{
	int x_gridNum=m_mapGridWidth;
	int y_gridNum=m_mapGridHeight;
	if (x_gridNum>0&&y_gridNum>0)
	{
		for(int x = 0; x < x_gridNum;x++)
		{
			for(int y = 0; y < y_gridNum;y++)
			{
				unsigned char mask= m_MazeBuffer[x + y*x_gridNum];
				char newMask=0;
				if (mask&s_BlockLayer2Mask)
				{
					return;
				}
			}
		}

		for(int x = 0; x < x_gridNum;x++)
		{
			for(int y = 0; y < y_gridNum;y++)
			{
				unsigned char mask= m_MazeBuffer[x + y*x_gridNum];
				char newMask=mask|s_BlockLayer2Mask;

				m_MazeBuffer[x + y*x_gridNum]=newMask;
			}
		}
	}

}

void SoundApp::SetAllMask(unsigned int mask)
{
	for (int i = 0; i < m_mapGridWidth*m_mapGridHeight ; ++i)
	{
		m_MazeBuffer[i] |= mask;
	}
}


void SoundApp::ClearAllMask(unsigned int mask)
{
	for (int i = 0; i < m_mapGridWidth*m_mapGridHeight ; ++i)
	{
		m_MazeBuffer[i] &= ~mask;
	}
}

//void SoundApp::DelAllRegion(RegionType rt)
//{
//	for (int i = 0; i < m_mapGridWidth; ++i)
//	{
//		for (int j = 0; j < m_mapGridHeight; ++j)
//		{
//			m_RegionBuffer.DelPoint(i,j,rt);
//		}
//	}
//}
//
//void SoundApp::DelAllRegionBig(RegionType rt)
//{
//	for (int i = 0; i < m_mapGridWidth; ++i)
//	{
//		for (int j = 0; j < m_mapGridHeight; ++j)
//		{
//			m_RegionBuffer.DelPoint(i,j,rt);
//		}
//	}
//}
