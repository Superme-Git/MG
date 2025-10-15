#include "stdafx.h"
#include "SceneMovieManager.h"
#include "GameUIManager.h"
#include "GameApplication.h"
#include "GameScene.h"
#include "MainRoleDataManager.h"
#include "GameTable/EffectPath/CEffectPath.h"
#include "ConfigManager.h"
#include "MainCharacter.h"
#include "MainRoleDataManager.h"

#include "../ProtoDef/fire/pb/CEndPlayCG.hpp"


#include "SimpleAudioEngine.h"
#include "MusicSoundVolumeMixer.h"

SceneMovieManager::SceneMovieManager():
 m_bMovieStarted(false),
 m_pOldGuiSheet(NULL),
 m_pMovieAniSheet(NULL),
 m_iCurMovieID(0),
 m_bIsMovieScene(false),
 m_bReturningScene(false),
 m_bCanDrawLoadingBar(false),

 m_pBottomPanel(NULL),
 m_pTopPanel(NULL),
 m_curMovieMapID(0),
 m_pCurSection(NULL),
 m_pCurSpeakSection(NULL),
 m_bIsMovieSceneEnding(false),
 m_bSpeakState(false)
{
	m_vecMovieSections.clear();
	m_SpeakSections.clear();
	m_mapSoundHandle.clear();
	InitMovieMap();
	InitHandler();
	InitEffectPath();
}

SceneMovieManager::~SceneMovieManager()
{
	if (m_pMovieAniSheet)
	{
		CEGUI::WindowManager::getSingleton().destroyWindow(m_pMovieAniSheet);
	}
}

void SceneMovieManager::SetNpcDir(const std::wstring& idenName, int dir)
{
	MovieSceneNpc* pNpc = GetMovieNpcByVarName(idenName);
	if (pNpc)
	{
		if (dir >= 0 && dir < Nuclear::XPDIR_COUNT)
		{
			pNpc->SetDirection((Nuclear::NuclearDirection)dir);
		}
	}
}

Nuclear::SoundHandle SceneMovieManager::GetSoundHandleByID(int id)
{
	SoundHandleMapIt it = m_mapSoundHandle.find(id);
	if (it != m_mapSoundHandle.end())
	{
		return it->second;
	}
	return Nuclear::INVALID_SOUND_HANDLE;
}

void SceneMovieManager::DeleteAllSound()
{
	if (m_mapSoundHandle.size() > 0)
	{
		m_mapSoundHandle.clear();
	}

}

void SceneMovieManager::OnStartSectionStart()
{
	if (gGetGameApplication()->isFirstEnterGame())
	{
		gGetGameApplication()->SetFirstEnterGame(false);
		if (gGetScene())
		{
			gGetScene()->EndFirstEnterBlackEffect();
		}
	}
}

bool SceneMovieManager::HandleBreakMovieBtnClick(const CEGUI::EventArgs& e)
{
	if (m_bIsMovieScene&&!m_bIsMovieSceneEnding)
	{
		RequestBreakMovie();
	}

	return true;
}

void SceneMovieManager::ShowBreakMovieBtn(bool bShow)
{
	m_pBottomBreakText->setVisible(bShow);// new add

	if (m_iCurMovieID == 18888) {
		m_pBottomBreakText->setVisible(false);// new add
	}
}

void SceneMovieManager::ExitMovieCloseWindow()
{
	m_pBottomBreakText->setVisible(false);// new add

	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("ScriptNpcTalkDialog.DestroyDialog");
}

bool SceneMovieManager::isOnMovieEnding()
{
	return m_bIsMovieSceneEnding;
}

void SceneMovieManager::SetIsMovieEnding(bool b)
{
	m_bIsMovieSceneEnding = b;
}

void SceneMovieManager::StopSound(int id, int fadeTime)
{
	Nuclear::SoundHandle handle = GetSoundHandleByID(id);
	if (handle != Nuclear::INVALID_SOUND_HANDLE)
	{
		CocosDenshion::SimpleAudioEngine::sharedEngine()->stopBackgroundMusic(true);
	}
}

void SceneMovieManager::PlaySoundEffect(const std::wstring& strAudioName)
{
	if (gGetGameConfigManager() && gGetGameConfigManager()->isPlayEffect())
	{
		std::string newSoundRes;
		std::wstring newws;
		newws = LJFM::LJFMF::GetFullPathFileName(strAudioName);
		if (!newws.empty())
		{
			newSoundRes = StringCover::to_string(newws);

			CocosDenshion::SimpleAudioEngine::sharedEngine()->playEffect(newSoundRes.c_str());
			int BackMusicValue = gGetGameConfigManager()->GetConfigValue(L"soundvalue");
			if (MusicSoundVolumeMixer::GetInstance())
			{
				MusicSoundVolumeMixer::GetInstance()->setBackgroundMusicVolume((float)(BackMusicValue / 255.0f));
				MusicSoundVolumeMixer::GetInstance()->notifySoundStart(newSoundRes);
			}
		}
	}
}

void SceneMovieManager::HandleStopSound(const std::wstring& strParserText)
{
	int id = GetIntByVarName(L"id", strParserText);
	int fadeTime = GetIntByVarName(L"fadeout", strParserText);
	if (fadeTime == -1)
	{
		fadeTime = 500;
	}

	if (id > 0)
	{
		StopSoundSection* pSection = new StopSoundSection(id, fadeTime);
		AddSection(pSection);
	}
}

void SceneMovieManager::SetSceneEffct(const std::wstring& name, int x, int y, int effectID, int lasttime, int circle, int type)
{
	Nuclear::Nuclear_EffectLayer eType = Nuclear::XPEL_NORMAL_TOP;
	switch (type)
	{
	case 0:
		eType = Nuclear::XPEL_NORMAL_UNDER_SPRITE;
		break;
	case 1:
		eType = Nuclear::XPEL_NORMAL_SPRITE_1;
		break;
	case 2:
		eType = Nuclear::XPEL_NORMAL_ABOVE_SPRITE;
		break;
	case 3:
		eType = Nuclear::XPEL_NORMAL_SPRITE_2;
		break;
	case 4:
		eType = Nuclear::XPEL_NORMAL_TOP;
		break;
	}

	if (name.size() > 0)
	{
		Nuclear::NuclearLocation pixLoc(x * 24, y * 16);
		std::wstring strEffectName = GetEffectPathByName(name);
		if (strEffectName.size() > 0)
		{
			if (circle > 0)
			{
				Nuclear::GetEngine()->GetWorld()->PlayEffect(strEffectName, eType, pixLoc.x, pixLoc.y, circle, true, 2);
			}
			else
			{
				Nuclear::IEffect* pEffct = Nuclear::GetEngine()->GetWorld()->SetEffect(strEffectName, eType, pixLoc.x, pixLoc.y, true);
				m_vecEffects.push_back(pEffct);
			}
		}
	}
}

void SceneMovieManager::HandleDeleteNpc(const std::wstring& strParserText)
{
	std::wstring name = GetStringByVarName(L"varname", strParserText);
	DeleteNpcSection* pSection = new DeleteNpcSection(name);
	AddSection(pSection);
}

void SceneMovieManager::HandleClearScene(const std::wstring& strParserText)
{
	std::wstring objType = GetStringByVarName(L"type", strParserText);
	ClearSceneSection* pSection = new ClearSceneSection(objType);
	AddSection(pSection);
}

void SceneMovieManager::HandleNpcSayPop(const std::wstring& strParserText)
{
	std::wstring varName = GetStringByVarName(L"varname", strParserText);
	std::wstring varMsg = GetStringByVarName(L"text", strParserText);
	NpcSayPopSection* pSection = new NpcSayPopSection(varName, varMsg);
	AddSection(pSection);
}

void SceneMovieManager::HandleNpcSpeak(const std::wstring& strParserText)
{
	std::wstring strName = GetStringByVarName(L"name", strParserText);
	std::wstring varMsg = GetStringByVarName(L"text", strParserText);
	int headID = GetIntByVarName(L"head", strParserText);
	int isPause = GetIntByVarName(L"pause", strParserText);
	int time = GetIntByVarName(L"time", strParserText);
	time = time == -1 ? 5000 : time;
	NpcSpeakWndSection* pSection = new NpcSpeakWndSection(headID, strName, varMsg, time, isPause == 1);
	AddSpeakSection(pSection);
}

void SceneMovieManager::MoveNpc(const std::wstring& identityName, int dest_X, int dest_Y, int ActionID, int time)
{
	MovieSceneNpc* pNpc = GetMovieNpcByVarName(identityName);
	if (pNpc)
	{
		Nuclear::NuclearLocation logicLoc(dest_X * 16, dest_Y * 16);
		pNpc->MoveTo(logicLoc);
		if (time > 0)
		{
			Nuclear::NuclearLocation curPixLoc = pNpc->GetLocation();
			Nuclear::NuclearFPoint curfloatPixLoc(curPixLoc);
			Nuclear::NuclearFPoint targetflostLoc((float)dest_X * 24, (float)dest_Y * 16);
			float dis = sqrtf(targetflostLoc.Dis2(curfloatPixLoc));
			float speed = dis / ((float)time);
			pNpc->GetSprite()->SetMoveSpeed(speed);
		}
	}
}

void SceneMovieManager::SetNpcComponent(const std::wstring& idenName, eSpriteComponentType type, int baseid)
{
	MovieSceneNpc* pNpc = GetMovieNpcByVarName(idenName);
	if (pNpc)
	{
		pNpc->SetSpriteComponent(type,baseid);
	}
}

void SceneMovieManager::HandleCreateNpc(const std::wstring& strParserText)
{
	std::wstring strvarName = GetStringByVarName(L"varname", strParserText);
	std::wstring strName = GetStringByVarName(L"npcname", strParserText);
	int title = GetIntByVarName(L"title", strParserText);
	int modelID = GetIntByVarName(L"model", strParserText);
	int colorTemplate = GetIntByVarName(L"color", strParserText);
	int x = 0;
	int y = 0;
	int dir = GetIntByVarName(L"dir", strParserText);
	int id = GetIntByVarName(L"id", strParserText);
	GetPosByVarName(x, y, L"pos", strParserText);
	int defaultAction = GetIntByVarName(L"dtAction", strParserText);
	float scale = GetFloatByVarName(L"scale", strParserText);
	int ex = GetIntByVarName(L"ex", strParserText);
	int ey = GetIntByVarName(L"ey", strParserText);
	std::wstring chengwei = GetStringByVarName(L"chengwei", strParserText);
	std::wstring cwColor = GetStringByVarName(L"cwcolor", strParserText);
	int mount = GetIntByVarName(L"mountid", strParserText);
	int cA = GetIntByVarName(L"colorA", strParserText);
	int cB = GetIntByVarName(L"colorB", strParserText);

	std::wstring eName = GetStringByVarName(L"effect", strParserText);
	int cycle = GetIntByVarName(L"repeat", strParserText);

	scale = scale == -1 ? 1 : scale;

	CreateNpcSection* pSection = new CreateNpcSection(id, strvarName, x, y, modelID, strName, title, defaultAction, scale, dir, colorTemplate, eName, cycle, ex, ey, chengwei, cwColor, mount, cA, cB);
	AddSection(pSection);
}

void SceneMovieManager::HandleRotateCamara(const std::wstring& strParserText)
{

}

void SceneMovieManager::HandleZoomIn(const std::wstring& strParserText)
{

}

void SceneMovieManager::HandleMoveCamara(const std::wstring& strParserText)
{
	int x = 0;
	int y = 0;
	int time = 0;
	GetPosByVarName(x, y, L"pos", strParserText);
	time = GetIntByVarName(L"time", strParserText);

	MoveCamSection* pSection = new MoveCamSection(x, y, time);
	AddSection(pSection);
}

void SceneMovieManager::HandleSetMap(const std::wstring& strParserText)
{
	int mapid = GetIntByVarName(L"id", strParserText);
	int x = 0;
	int y = 0;
	GetPosByVarName(x, y, L"pos", strParserText);
	SetMapSection* pSection = new SetMapSection(mapid, x, y);
	AddSection(pSection);
}

void SceneMovieManager::AddSpeakSection(MovieSection* pSection)
{
	m_SpeakSections.push_back(pSection);
}

void SceneMovieManager::StartCurFirstSpeakSection()
{
	if (m_pCurSpeakSection != NULL)
	{
		DeleSpeakSection(m_pCurSpeakSection);
		m_pCurSpeakSection = NULL;
	}

	if (m_SpeakSections.size() > 0)
	{
		m_pCurSpeakSection = m_SpeakSections[0];
		if (m_pCurSpeakSection != NULL)
		{
			m_pCurSpeakSection->Start();
		}
	}
}

void SceneMovieManager::EndCurSection()
{
	m_bSectionEnd = true;
	m_iCurSectionLastTime = 0;
	m_iCurSetctionNeedTime = 0;
}

void SceneMovieManager::EndPlayMovie()
{
	EndCurSection();
	m_vecMovieSections.clear();


	gGetScene()->OnExitMovieScene(m_curMovieMapID);

	m_bMovieStarted = false;
	m_bIsMovieSceneEnding = false;
	m_iCurMovieID = 0;

	OnStartSectionStart();
}

void SceneMovieManager::ExitMovieScene()   //?à≥?≥°?∞??a≠
{
	if (!m_bIsMovieScene)
	{
		return;
	}

	fire::pb::CEndPlayCG cmd;
	cmd.id = m_iCurMovieID;

	DeleAllSection();
	DeleteAllSound();
	m_pCurSection = NULL;
	m_pCurSpeakSection = NULL;
	MovieSectionIt it = m_SpeakSections.begin();
	for (; it != m_SpeakSections.end(); ++it)
	{
		delete (*it);
	}
	m_SpeakSections.clear();
	m_curMovieMapID = 0;

	m_bIsMovieSceneEnding = false;
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("ScriptNpcTalkDialog.DestroyDialog");

	if (CEGUI::WindowManager::getSingleton().isWindowPresent("root_wnd"))
	{
		CEGUI::Window *rootWnd = CEGUI::WindowManager::getSingleton().getWindow("root_wnd");
		if (CEGUI::System::getSingleton().getGUISheet() != rootWnd)
		{
			CEGUI::System::getSingleton().setGUISheet(rootWnd);
		}
	}

	if (m_pMovieAniSheet)
	{
		m_pMovieAniSheet->destroy();
		m_pMovieAniSheet = NULL;
		m_pBottomPanel = NULL;
		m_pTopPanel = NULL;
	}

	if (gGetNetConnection())
	{
		gGetNetConnection()->send(cmd);
	}

	m_bIsMovieScene = false;
	m_bReturningScene = false;

	if (gGetScene())
	{
		gGetScene()->UpdateNpcStateAfterEndMovie();
		if (gGetGameApplication()->isFirstTimeEnterGame() == 1)
		{
			gGetScene()->handleFirstJuqingPlayedEnded();
		}
		else if (gGetGameApplication()->isFirstTimeEnterGame() == 3)
		{
			gGetGameApplication()->setFirstTimeEnterGameValue(10);
		}
	}

	OnStartSectionStart();
	m_iCurMovieID = 0;

	Nuclear::GetEngine()->GetWorld()->SetCameraUpdateType(mOldCamUpdateType);

	// 退出剧情时，允许自动播放玩家语音
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithBooleanData("CChatManager.SetAllowAutoPlay", true);
}

bool SceneMovieManager::LoadMovie(int movieID)
{
	m_vecMovieSections.clear();

	std::wstring filename = L"/cfg/movie/";
	filename += StringCover::intTowstring(movieID);
	filename += L".txt";

	char* fileBuf = NULL;
	std::streamsize ss = 0;
	if (GetBuffFromFile(filename, fileBuf, ss) != 0)//配置文件的文件流
	{
		return false;
	}

	std::wstring fileString = s2ws(fileBuf);
	delete[] fileBuf;

	//打开配置文件流
	if (fileString.empty())
	{
		std::wstring errMsg(MHSD_UTILS::GETSTRING(1200) + filename);
		//gGetGameUIManager()->AddMessageTip(errMsg);
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushStringToLuaStack(ws2s(errMsg).c_str());
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("CTipsManager.AddMessageTip_", 1);
		return false;
	}

	//读取一行数据，直到文件结尾    
	std::vector<std::wstring> strLineTexts;
	std::vector<std::wstring> delimiters;
	delimiters.push_back(L"\r\n");
	StringCover::split_string(fileString, delimiters, strLineTexts);
	for (unsigned int i = 0; i < strLineTexts.size(); ++i)
	{
		std::wstring& strLineText = strLineTexts[i];

		EraseSpaceOfString(strLineText);

		if (strLineText.size()>0)
		{
			ParserSection(strLineText);
		}
	}

	return true;
}

void SceneMovieManager::InitHandler()
{
	m_mapMovieHandlers[L"Setmap"] = &SceneMovieManager::HandleSetMap;
	m_mapMovieHandlers[L"Start"] = &SceneMovieManager::HandleStart;
	m_mapMovieHandlers[L"End"] = &SceneMovieManager::HandleEnd;
	m_mapMovieHandlers[L"SetCam"] = &SceneMovieManager::HandleSetCamara;
	m_mapMovieHandlers[L"MoveCam"] = &SceneMovieManager::HandleMoveCamara;
	m_mapMovieHandlers[L"ZoomIn"] = &SceneMovieManager::HandleZoomIn;
	m_mapMovieHandlers[L"ZoomOut"] = &SceneMovieManager::HandleZoomOut;
	m_mapMovieHandlers[L"Rotate"] = &SceneMovieManager::HandleRotateCamara;
	m_mapMovieHandlers[L"Shake"] = &SceneMovieManager::HandleShakeCamara;

	m_mapMovieHandlers[L"CreateNpc"] = &SceneMovieManager::HandleCreateNpc;
	m_mapMovieHandlers[L"MoveNPC"] = &SceneMovieManager::HandleMoveNpc;
	m_mapMovieHandlers[L"PlayAct"] = &SceneMovieManager::HandleNpcPlayAction;
	m_mapMovieHandlers[L"Speak"] = &SceneMovieManager::HandleNpcSpeak;
	m_mapMovieHandlers[L"SKbegin"] = &SceneMovieManager::HandleSpeakStart;
	m_mapMovieHandlers[L"Say"] = &SceneMovieManager::HandleNpcSayPop;
	m_mapMovieHandlers[L"DeleteNPC"] = &SceneMovieManager::HandleDeleteNpc;
	m_mapMovieHandlers[L"ClearScene"] = &SceneMovieManager::HandleClearScene;
	m_mapMovieHandlers[L"SetDir"] = &SceneMovieManager::HandleSetDir;
	m_mapMovieHandlers[L"SetMount"] = &SceneMovieManager::HandleSetNpcMount;
	m_mapMovieHandlers[L"SetColor"] = &SceneMovieManager::HandleSetNpcColor;
	
	m_mapMovieHandlers[L"SetSE"] = &SceneMovieManager::HandleSetSceneEffct;
	m_mapMovieHandlers[L"PlaySound"] = &SceneMovieManager::HandlePlaySound;
	m_mapMovieHandlers[L"StopSound"] = &SceneMovieManager::HandleStopSound;

	m_mapMovieHandlers[L"PlayCG"] = &SceneMovieManager::HandlePlayCG;
	m_mapMovieHandlers[L"Wait"] = &SceneMovieManager::HandleWait;
	m_mapMovieHandlers[L"Return"] = &SceneMovieManager::HandleReturn;

	m_mapMovieHandlers[L"PlaySDEffect"] = &SceneMovieManager::HandlePlaySDEffect;

	// 不允许自动播放玩家语音
	m_mapMovieHandlers[L"ForbiddenAutoPlay"] = &SceneMovieManager::HandleForbiddenAutoPlay;
}

bool SceneMovieManager::isOnSceneMovie()
{
	return m_bIsMovieScene;
}

bool SceneMovieManager::CanDrawLoadingBar() const
{
	return m_bCanDrawLoadingBar;
}

void SceneMovieManager::SetCanDrawLoadingBar(bool bCan)
{
	m_bCanDrawLoadingBar = bCan;
}

int SceneMovieManager::GetCurMoiveMapID()
{
	return m_curMovieMapID;
}

void SceneMovieManager::InitMovieMap()
{
	m_mapMovieFiles.clear();
	m_mapMovieFiles[1]=L"movie1";
}

void SceneMovieManager::EraseSpaceOfString(std::wstring& strText)
{
	size_t tabIdx=strText.find_first_of(L"	");

	while (tabIdx!=std::wstring::npos)
	{
		strText.erase(tabIdx,1);
		tabIdx=strText.find_first_of(L"	");
	}

	size_t idxFirst=strText.find_first_not_of(L" ");
	if (idxFirst!=std::wstring::npos)
	{
		strText=strText.substr(idxFirst,std::wstring::npos);
	}

	size_t idxEnd=strText.find_last_not_of(L" ");
	if (idxEnd!=std::wstring::npos)
	{
		strText=strText.substr(0,idxEnd+1);
	}
}

void SceneMovieManager::EnterMovieScene(int movieID) //???oΩˉ??≥°?∞??a≠
{
	mOldCamUpdateType = Nuclear::GetEngine()->GetWorld()->GetCameraUpdateType();

	if (GetMainCharacter())
	{
		GetMainCharacter()->StopMove();
		GetMainCharacter()->m_MapWalker.ClearGoTo();
	}

	if (m_pMovieAniSheet==NULL)
	{
		CEGUI::WindowManager& winMgr = CEGUI::WindowManager::getSingleton();
		m_pMovieAniSheet=winMgr.loadWindowLayout("SceneAniBack.layout");
        m_pMovieAniSheet->EnableAllowModalState(true);
		m_pTopPanel=winMgr.getWindow("SceneAniBack/TopPanel");
		m_pBottomPanel=winMgr.getWindow("SceneAniBack/BottomPanel");

		m_pBottomBreakText = winMgr.getWindow("SceneAniBack/TopPanel/dianjicichutiaoguojuqing");//new add

		m_pBottomBreakText->subscribeEvent(CEGUI::Window::EventMouseClick, CEGUI::Event::Subscriber(&SceneMovieManager::HandleBreakMovieBtnClick, this));

		const CEGUI::URect& oldRectTop = m_pTopPanel->getArea();
		CEGUI::URect newRectTop(oldRectTop);
		newRectTop.d_max.d_y.d_offset = 0.0f;
		newRectTop.d_max.d_y.d_scale = 0.0f;
		m_pTopPanel->setArea(newRectTop);

		const CEGUI::URect& oldRectBottom = m_pBottomPanel->getArea();
		CEGUI::URect newRectBottom(oldRectBottom);
		newRectBottom.d_min.d_y.d_offset = 0.0f;
		newRectBottom.d_min.d_y.d_scale = 1.0f;
		m_pBottomPanel->setArea(newRectBottom);
	}
	if (m_pMovieAniSheet!=NULL)
	{
		CEGUI::System::getSingleton().setGUISheet(m_pMovieAniSheet);
	}
	else
	{
		ExitMovieScene();
		return;
	}

	CocosDenshion::SimpleAudioEngine::sharedEngine()->stopAllEffects();

	m_iCurMovieID=movieID;
	m_bIsMovieScene=true;
	m_bReturningScene = false;
	m_bIsMovieSceneEnding=false;
	m_curMovieMapID=gGetScene()->GetMapID();	

	gGetScene()->OnEnterMovieScene();
	bool bPlaySuccess = BeginPlayMovie(movieID);
	if (!bPlaySuccess) {
		ExitMovieScene();
	}
}

bool SceneMovieManager::BeginPlayMovie(int movieID)
{
	if (LoadMovie(movieID))
	{
		m_iCurMovieID=movieID;
		StartCurFirstSctionInList();
		return true;
	}
	return false;
}

void SceneMovieManager::ParserSection(const std::wstring& strSectionContent)
{
	MovieHandlerMapIt it=m_mapMovieHandlers.begin();
	for (;it!=m_mapMovieHandlers.end();++it)
	{
		std::wstring strCmd=it->first;
		if (strSectionContent.find(strCmd)!=std::wstring::npos)
		{
			(this->*(*it).second)(strSectionContent);
			break;
		}
	}
}

void SceneMovieManager::StartCurFirstSctionInList()
{
	if (m_vecMovieSections.size()>0)
	{
		m_pCurSection=m_vecMovieSections[0];
		if (m_pCurSection!=NULL)
		{
			m_pCurSection->Start();
		}
	}
}


void SceneMovieManager::Updata(int elapseTime)
{
	if (!m_bIsMovieScene)
	{
		return;
	}

	if (m_bSpeakState && m_pCurSpeakSection != NULL)
	{
		m_pCurSpeakSection->Update(elapseTime);
	}

	if (m_pCurSection!=NULL)
	{
		m_pCurSection->Update(elapseTime);
		if (m_pCurSection->IsEnd())
		{
			DeleSection(m_pCurSection);
			m_pCurSection=NULL;
			if (m_vecMovieSections.size()>0)
			{
				StartCurFirstSctionInList();
			}
			else
			{
				ExitMovieScene();
			}
		}
	}
}

void SceneMovieManager::HandleSetCamara(const std::wstring& strParserText)
{
	int x=0;
	int y=0;
	GetPosByVarName(x,y,L"pos",strParserText);
	SetCamara(x,y);
}

void SceneMovieManager::SetCamara(int newPos_X,int newPos_Y)
{
	Nuclear::NuclearLocation pixLoc(newPos_X*24,newPos_Y*16);
	
	gGetScene()->AttachCameraTo(NULL);

	Nuclear::GetEngine()->GetWorld()->SetViewport(pixLoc,true);
}

void SceneMovieManager::HandleZoomOut(const std::wstring& strParserText)
{

}

void SceneMovieManager::HandleShakeCamara(const std::wstring& strParserText)
{
	int id = GetIntByVarName(L"id", strParserText);
	int range = GetIntByVarName(L"range", strParserText);
	int time = GetIntByVarName(L"time", strParserText);
	ShakeSection* pSection = new ShakeSection(id, range, time);
	AddSection(pSection);
}

void SceneMovieManager::HandleMoveNpc(const std::wstring& strParserText)
{
	std::wstring varName=GetStringByVarName(L"varname",strParserText);
	int x=0;
	int y=0;
	int time=0;
	int actionID=0;
	GetPosByVarName(x,y,L"pos",strParserText);
	time=GetIntByVarName(L"time",strParserText);

	MoveNpcSection* pSection=new MoveNpcSection(varName,x,y,time);
	AddSection(pSection);

}

void SceneMovieManager::HandleSetNpcMount(const std::wstring& strParserText)
{
	std::wstring varName = GetStringByVarName(L"varname", strParserText);
	int baseID = 0;
	baseID = GetIntByVarName(L"mountid", strParserText);

	NpcComponentSection* pSection = new NpcComponentSection(varName, eSpriteComponentType::eSprite_Horse, baseID);
	AddSection(pSection);
}
void SceneMovieManager::HandleSetNpcColor(const std::wstring& strParserText)
{
	std::wstring varName = GetStringByVarName(L"varname", strParserText);
	int type = 0;
	int baseID = 0;
	type = GetIntByVarName(L"type", strParserText);
	baseID = GetIntByVarName(L"id", strParserText);

	NpcComponentSection* pSection = new NpcComponentSection(varName, type, baseID);
	AddSection(pSection);
}

void SceneMovieManager::HandleNpcPlayAction(const std::wstring& strParserText)
{
	std::wstring varName=GetStringByVarName(L"varname",strParserText);
	int ActionID=GetIntByVarName(L"id",strParserText);
	int dir=GetIntByVarName(L"dir",strParserText);
	PlayActionSection* pSection=new PlayActionSection(varName,ActionID,0,dir);
	AddSection(pSection);
}

void SceneMovieManager::HandleSpeakStart(const std::wstring& strParserText)
{
	int time = GetIntByVarName(L"time", strParserText);
	StartSpeakSection* pSection = new StartSpeakSection(time);
	AddSection(pSection);
}

void SceneMovieManager::NpcSayPop(const std::wstring& identityName,const std::wstring strContent)
{
	MovieSceneNpc* pNpc=GetMovieNpcByVarName(identityName);
	if (pNpc)
	{
		pNpc->AddClientTalk(strContent,0xFFFFFFFF);
	}
}

void SceneMovieManager::HandleSetDir(const std::wstring& strParserText)
{
	std::wstring name=GetStringByVarName(L"varname",strParserText);
	int dir=GetIntByVarName(L"dir",strParserText);
	SetNpcDirSection* pSection=new SetNpcDirSection(name,dir);
	AddSection(pSection);
}

void SceneMovieManager::HandleSetSceneEffct(const std::wstring& strParserText)
{
	std::wstring name=GetStringByVarName(L"name",strParserText);
	int x=0;
	int y=0;
	int id=GetIntByVarName(L"id",strParserText);
	int cycle=GetIntByVarName(L"repeat",strParserText);
	int type = GetIntByVarName(L"type", strParserText);
	GetPosByVarName(x, y, L"pos", strParserText);

	SetEffectSection* pSection = new SetEffectSection(name, x, y, id, cycle, type);
	AddSection(pSection);
}

void SceneMovieManager::HandleForbiddenAutoPlay(const std::wstring& strParserText)
{
	ForbiddenAutoPlaySection* pSection = new ForbiddenAutoPlaySection();
	AddSection(pSection);
}

void SceneMovieManager::HandlePlaySound(const std::wstring& strParserText)
{
	int id=GetIntByVarName(L"id",strParserText);
	std::wstring resName=GetStringByVarName(L"res",strParserText);
	int repeat=GetIntByVarName(L"repeat",strParserText);
	
	if (repeat==-1)
	{
		repeat=0;
	}
	
	int fadeTime=GetIntByVarName(L"fadein",strParserText);
	if (fadeTime==-1)
	{
		fadeTime=500;
	}
	if (id>0&&resName.size()>0)
	{
		PlaySoundSection* pSection=new PlaySoundSection(id,resName,repeat,fadeTime);
		AddSection(pSection);
	}
}

void SceneMovieManager::HandlePlaySDEffect(const std::wstring& strParserText)
{
	std::wstring resName = GetStringByVarName(L"res", strParserText);
	int type = GetIntByVarName(L"type", strParserText); //如果有type表示是主角说话
	if (type != -1)
	{
		eSexType sex = (eSexType)GetMainRoleDataNumValue<int>("sex");
		if (sex == eSexType(eSexFemale))
		{
			size_t nidx = resName.find_last_of(L".");
			std::wstring sub0 = resName.substr(0, nidx);
			std::wstring sub1 = resName.substr(nidx, resName.size() - nidx);
			sub0.append(L"_f");
			resName = sub0.append(sub1);
		}
	}
	if (resName.size() > 0)
	{
		PlaySDEffectSection* pSection = new PlaySDEffectSection(resName);
		AddSection(pSection);
	}
}

void SceneMovieManager::PlaySound(const std::wstring& strAudioName,int id,int repeat,int fadeTime)
{
	CocosDenshion::SimpleAudioEngine::sharedEngine()->stopBackgroundMusic(true);

	if (gGetGameConfigManager() && gGetGameConfigManager()->isPlayBackMusic())
	{
		std::string newSoundRes;
		std::wstring newws;
		newws = LJFM::LJFMF::GetFullPathFileName(strAudioName);
		if (!newws.empty())
		{
			newSoundRes = StringCover::to_string(newws);

			CocosDenshion::SimpleAudioEngine::sharedEngine()->playBackgroundMusic(newSoundRes.c_str(), true);
			int BackMusicValue = gGetGameConfigManager()->GetConfigValue(L"soundvalue");
			if (MusicSoundVolumeMixer::GetInstance())
				MusicSoundVolumeMixer::GetInstance()->setBackgroundMusicVolume((float)(BackMusicValue / 255.0f));
		}
	}
} 


void SceneMovieManager::HandlePlayCG(const std::wstring& strParserText)
{

}

void SceneMovieManager::HandleWait(const std::wstring& strParserText)
{
	int time=GetIntByVarName(L"time",strParserText);
	Wait(time);
}

void SceneMovieManager::Wait(int time)
{
	WaitSection* pSection=new WaitSection(time);
	AddSection(pSection);
}

void SceneMovieManager::RequestBreakMovie(bool imm)
{
	if (m_bIsMovieScene&&!m_bIsMovieSceneEnding)
	{
		if (imm)
		{
			DeleAllSection();
			m_pCurSection = NULL;

			ExitMovieScene();
		}
		else
		{
			OnBreakMoive();
		}
		m_bIsMovieSceneEnding = true;
	}
}

bool SceneMovieManager::OnBreakMoive()
{
	if (m_bIsMovieScene)
	{
		if (dynamic_cast<ReturnSection*>(m_pCurSection) || dynamic_cast<EndSection*>(m_pCurSection))
		{
			return true;
		}

		m_pCurSection = NULL;

		while (!m_vecMovieSections.empty())
		{
			MovieSectionIt it = m_vecMovieSections.begin();
			MovieSection* pTmp = *it;
			if (dynamic_cast<ReturnSection*>(pTmp) || dynamic_cast<EndSection*>(pTmp))
			{
				break;
			}

			delete pTmp;
			m_vecMovieSections.erase(it);
		}
		
		StartCurFirstSctionInList();

		OnStartSectionStart();

		return true;
	}

	return false;
}

std::wstring SceneMovieManager::GetEffectPathByName(const std::wstring& strName)
{

	std::vector<int> vecAllID;
	vecAllID.clear();
	GameTable::EffectPath::GetCEffectPathTableInstance().getAllID(vecAllID);
	for (size_t i = 0; i < vecAllID.size(); ++i)
	{
		GameTable::EffectPath::CEffectPath path = GameTable::EffectPath::GetCEffectPathTableInstance().getRecorder(vecAllID[i]);
		if (path.id != -1 && path.Name == strName)
		{
			return path.Patn;
		}
	}

	return L"";
}

void SceneMovieManager::InitEffectPath()
{
}

void SceneMovieManager::RemoveAllMovieEffect()
{
	MEffectIt it = m_vecEffects.begin();
	for (; it != m_vecEffects.end(); ++it)
	{

		Nuclear::GetEngine()->GetWorld()->RemoveEffect(*it);
	}
	m_vecEffects.clear();
}

void SceneMovieManager::setSpeakSectionState(bool b)
{
	m_bSpeakState = b;
	if (m_bSpeakState) StartCurFirstSpeakSection();
}

bool SceneMovieManager::OnWindowsMessage(HWND hWnd, UINT msg, WPARAM wParam, LPARAM lParam)
{
	CEGUI::System& guiSystem = CEGUI::System::getSingleton();
	float x = (float)wParam;
	float y = (float)lParam;

	switch (msg)
	{
#ifndef WIN32
	case Nuclear::WM_LBUTTONDOWN:
#else
	case WM_LBUTTONDOWN:
#endif
	{
		CEGUI::MouseCursor& mouse(CEGUI::MouseCursor::getSingleton());
		CEGUI::Point mousePt(x, y);
		mouse.setPosition(mousePt);

		guiSystem.injectMouseButtonDown(CEGUI::LeftButton);
	}
	break;

#ifndef WIN32
	case Nuclear::WM_LBUTTONUP:
#else
	case WM_LBUTTONUP:
#endif
	{
		CEGUI::MouseCursor& mouse(CEGUI::MouseCursor::getSingleton());
		CEGUI::Point mousePt(x, y);
		mouse.setPosition(mousePt);

		if (guiSystem.injectMouseButtonUp(CEGUI::LeftButton))
		{
			if (!guiSystem.isInModalState())
			{
				if (NULL != m_pCurSection)
				{
					m_pCurSection->OnMouseButtonDown();
				}
			}

			return true;
		}
	}
	break;

#ifndef WIN32
	case Nuclear::WM_MOUSEMOVE:
#else
	case WM_MOUSEMOVE:
#endif
	{
		bool b = guiSystem.injectMousePosition(x, y);
		return b;
	}
	}

	return true;
}

void SceneMovieManager::ReturnToScene()
{
	m_bReturningScene = true;
	RemoveAllMovieEffect();
	gGetScene()->OnExitMovieScene(m_curMovieMapID);
	m_curMovieRegionBuffer.Init(0, 0);
}

void SceneMovieManager::ReturnToScene(int mapId, const Nuclear::NuclearLocation& pos)
{
	m_bReturningScene = true;
	RemoveAllMovieEffect();
	gGetScene()->OnExitMovieScene(m_curMovieMapID, mapId, pos);
	m_curMovieRegionBuffer.Init(0, 0);
}

void SceneMovieManager::HandleReturn(const std::wstring& strParserText)
{
	int time = GetIntByVarName(L"time", strParserText);

	ReturnSection::TYPE type = (ReturnSection::TYPE)GetIntByVarName(L"type", strParserText);

	int mapId = -1, x = 0, y = 0;
	switch (type)
	{
	case ReturnSection::TYPE_DIRECTLY_RETURN:
		break;

	case ReturnSection::TYPE_RETURN_TO_SPECIFIED_MAP:
		mapId = GetIntByVarName(L"map", strParserText);
		GetPosByVarName(x, y, L"pos", strParserText);
		break;

	case ReturnSection::TYPE_RETURN_TO_SCHOOL:
		GetMainCharacter()->GetSchoolMapIdAndPos(&mapId, &x, &y);
		break;
	}

	ReturnSection* pSection = new ReturnSection(time, type, mapId, x, y);
	AddSection(pSection);
}

void SceneMovieManager::DeleAllSection()
{
	MovieSectionIt it = m_vecMovieSections.begin();
	for (; it != m_vecMovieSections.end(); ++it)
	{
		delete (*it);
	}
	m_vecMovieSections.clear();
}

void SceneMovieManager::DeleCurSpeakSectionStartNext(bool startNext)
{
	if (NULL != m_pCurSpeakSection)
	{
		DeleSpeakSection(m_pCurSpeakSection);
		m_pCurSpeakSection = NULL;
	}

	if (startNext)
	{
		if (m_SpeakSections.size() > 0)
		{
			StartCurFirstSpeakSection();
		}
	}
}

void SceneMovieManager::DeleSpeakSection(MovieSection* pSection)
{
	MovieSectionIt it = std::find(m_SpeakSections.begin(), m_SpeakSections.end(), pSection);
	if (it != m_SpeakSections.end())
	{
		delete (*it);
		m_SpeakSections.erase(it);
		if (m_pCurSpeakSection == pSection)
		{
			m_pCurSpeakSection = NULL;
		}
	}
}

void SceneMovieManager::DeleSection(MovieSection* pSection)
{
	MovieSectionIt it = std::find(m_vecMovieSections.begin(), m_vecMovieSections.end(), pSection);
	if (it != m_vecMovieSections.end())
	{
		delete (*it);
		m_vecMovieSections.erase(it);
		if (m_pCurSection == pSection)
		{
			m_pCurSection = NULL;
		}
	}
}

void SceneMovieManager::SetPercent(float fPercent)
{
	const CEGUI::URect& oldRectTop = m_pTopPanel->getArea();
	CEGUI::URect newRectTop(oldRectTop);
	newRectTop.d_max.d_y.d_offset = 0.0f;
	newRectTop.d_max.d_y.d_scale = 0.5f - fPercent;
	m_pTopPanel->setArea(newRectTop);

	const CEGUI::URect& oldRectBottom = m_pBottomPanel->getArea();
	CEGUI::URect newRectBottom(oldRectBottom);
	newRectBottom.d_min.d_y.d_offset = 0.0f;
	newRectBottom.d_min.d_y.d_scale = 0.5f + fPercent * 0.92;
	m_pBottomPanel->setArea(newRectBottom);
}

void SceneMovieManager::HandleEnd(const std::wstring& strParserText)
{
	int fadetime = GetIntByVarName(L"fadetime", strParserText);
	if (fadetime == -1)
	{
		fadetime = s_iMovieStartFadeTime;
	}
	EndSection* pSection = new EndSection(fadetime);
	AddSection(pSection);
}

void SceneMovieManager::HandleStart(const std::wstring& strParserText)
{
	int fadetime = GetIntByVarName(L"fadetime", strParserText);
	if (fadetime == -1)
	{
		fadetime = s_iMovieStartFadeTime;
	}

	StartSection* pSection = new StartSection(fadetime);
	AddSection(pSection);

}

void SceneMovieManager::AddSection(MovieSection* pSection)
{
	m_vecMovieSections.push_back(pSection);
}

MovieSceneNpc* SceneMovieManager::GetMovieNpcByVarName(const std::wstring& varName)
{
	if (varName.size() > 0)
	{
		if (m_mapMovieNpc.find(varName) != m_mapMovieNpc.end())
		{
			int64_t id = m_mapMovieNpc[varName];
			return gGetScene()->FindMovieSceneNpcByID(id);
		}
	}
	return NULL;
}

void SceneMovieManager::NpcPlayAction(const std::wstring& identityName, int actionID, int endType, int dir)
{
	MovieSceneNpc* pNpc = GetMovieNpcByVarName(identityName);
	if (pNpc != NULL)
	{
		if (actionID > 0 && actionID < eActionMax)
		{
			if (dir >= 0 && dir < Nuclear::XPDIR_COUNT)
			{
				pNpc->SetDirection((Nuclear::NuclearDirection)dir);
			}

			eActionType actionType = (eActionType)actionID;
			if (actionType == eActionDying || actionType == eActionDeath || actionType == eActionDeathStill)
			{
				pNpc->SetDefaultAction(actionType, true);
			}
			else
			{
				pNpc->PlayAction(actionType);
			}
		}
	}
}

void SceneMovieManager::DeleteNpc(const std::wstring& identityName)
{
	MovieSceneNpc* pNpc = GetMovieNpcByVarName(identityName);
	if (NULL != pNpc)
	{
		gGetScene()->DelMovieSceneNpc(pNpc->GetID());
	}
	std::map<std::wstring, int64_t >::iterator it = m_mapMovieNpc.find(identityName);
	if (it != m_mapMovieNpc.end())
	{
		m_mapMovieNpc.erase(it);
	}
}

void SceneMovieManager::CreateNpc(int id, const std::wstring& identityName, int pos_X, int pos_Y, int modelID, const std::wstring& strNpcName, int title, int dir, int colorTemplate,
	eActionType actype, float scale, const std::wstring& effectName, int repeat, int ex, int ey, const std::wstring& chengwei, const std::wstring& cwColor)
{
	if (id == 0 && identityName == MHSD_UTILS::GETSTRING(1206).c_str())
	{
		std::wstring mrName = s2ws(GetMainRoleDataStrValue("strName"));
		int mrModelID = GetMainRoleDataNumValue<int>("shape");
		gGetScene()->AddMovieSceneNpc((int64_t)id, mrName, title, mrModelID, pos_X * 16, pos_Y * 16, dir, colorTemplate, actype, scale, effectName, repeat, ex, ey, chengwei, cwColor);
	}
	else
	{
		gGetScene()->AddMovieSceneNpc((int64_t)id, strNpcName, title, modelID, pos_X * 16, pos_Y * 16, dir, colorTemplate, actype, scale, effectName, repeat, ex, ey, chengwei, cwColor);
	}
	m_mapMovieNpc[identityName] = id;
}

void SceneMovieManager::GetPosByVarName(int& x, int& y, const std::wstring& varName, const std::wstring& strParseText)
{
	std::wstring varAllName = varName + L"=";
	size_t idx = strParseText.find(varAllName);
	if (idx != std::wstring::npos)
	{
		size_t firstSpaceIdx = strParseText.find_first_of(L" ", idx);

		size_t varSize = varAllName.size();
		std::wstring strPos = strParseText.substr(idx + varSize, firstSpaceIdx - idx - varSize);
		size_t OffIdx = strPos.find(L",");
		std::wstring strX = strPos.substr(0, OffIdx);
		std::wstring strY = strPos.substr(OffIdx + 1, std::wstring::npos);
		x = CEGUI::PropertyHelper::stringToInt(strX);
		y = CEGUI::PropertyHelper::stringToInt(strY);
	}
}

std::wstring SceneMovieManager::GetStringByVarName(const std::wstring& varName, const std::wstring& strParseText)
{
	std::wstring varAllName = varName + L"=";
	size_t idx = strParseText.find(varAllName);
	if (idx != std::wstring::npos)
	{
		size_t varSize = varAllName.size();
		std::wstring strValue;
		GetValueStr(strParseText, idx + varSize, strValue);
		return strValue;
	}
	return L"";
}

void SceneMovieManager::GetValueStr(const std::wstring& str, int idx, std::wstring &val)
{
	size_t nextEqual = str.find_first_of(L"=", idx);
	std::wstring tempstr;
	if (nextEqual == std::wstring::npos)
	{
		val = str.substr(idx);
	}
	else
	{
		tempstr = str.substr(idx, nextEqual - idx);
		size_t nidx = tempstr.find_last_of(L" ");
		val = str.substr(idx, nidx);
	}
};

float SceneMovieManager::GetFloatByVarName(const std::wstring& varName, const std::wstring& strParseText)
{
	std::wstring varAllName = varName + L"=";
	size_t idx = strParseText.find(varAllName);
	if (idx != std::wstring::npos)
	{
		size_t varSize = varAllName.size();
		std::wstring strID;
		GetValueStr(strParseText, idx + varSize, strID);
		float iValue = CEGUI::PropertyHelper::stringToFloat(strID);
		return iValue;
	}
	return -1;
}

int SceneMovieManager::GetIntByVarName(const std::wstring& varName, const std::wstring& strParseText)
{
	std::wstring varAllName = varName + L"=";
	size_t idx = strParseText.find(varAllName);
	if (idx != std::wstring::npos)
	{
		size_t varSize = varAllName.size();
		std::wstring strID;
		GetValueStr(strParseText, idx + varSize, strID);
		int iValue = CEGUI::PropertyHelper::stringToInt(strID);
		return iValue;
	}
	return -1;
}

void SceneMovieManager::SetMap(int mapID, int xLogic, int yLogic)
{
	gGetScene()->OnSceneMovieChangeMap(mapID, m_curMovieMapID, xLogic, yLogic);
	if (m_curMovieMapID != mapID) {
		Nuclear::NuclearLocation mapsize;
		Nuclear::GetEngine()->GetWorld()->GetMapSize(mapsize);
		int mapconfigid = GetTableNumberFromLua("map.cmapconfig", mapID, "id");
		if (mapconfigid  != -1)
		{
			
			std::wstring regionfilename = L"/map/" + GetTableWStringFromLua("map.cmapconfig", mapconfigid, "resdir") + L"/regiontypeinfo.dat";
			if (!m_curMovieRegionBuffer.Load(LPCTSTR(regionfilename.c_str())))
			{
				m_curMovieRegionBuffer.Init((mapsize.x + 24 - 1) / 24, (mapsize.y + 16 - 1) / 16);
			}
		}
	}

	m_curMovieMapID = mapID;
}

//判断是否是遮罩区域
bool SceneMovieManager::IsMaskArea(Nuclear::NuclearLocation logicloc)
{
	return m_curMovieRegionBuffer.CheckPointType(logicloc.x / g_logicGrid, logicloc.y / g_logicGrid, eRegionType_Mask);
}


