#include "stdafx.h"
#include "MovieSection.h"
#include "SceneMovieManager.h"
#include "GameScene.h"
#include "GameApplication.h"
#include "fire/pb/mission/CReqGoto.hpp"
#include "fire/pb/battle/newhand/CReqNewHandBattle.hpp"


MovieSection::MovieSection()
: m_iDurateTime(0)
, m_iTotalTime(0)
, m_bEnd(true)
{

}

MovieSection::~MovieSection()
{
	
}

void MovieSection::Start()
{
	m_bEnd=false;
	m_iDurateTime=0;
}

void MovieSection::End()
{
	m_bEnd = true;
	m_iDurateTime = 0;
}

void MovieSection::Update(int elapse)
{
	if (!m_bEnd)
	{
		m_iDurateTime+=elapse;
		if (m_iDurateTime>=m_iTotalTime)
		{
			End();
		}
	}
}

void MovieSection::OnMouseButtonDown()
{
	return;
}

void MovieSection::SetTotalTime(int time)
{
	m_iTotalTime = time;
}

int MovieSection::GetTotalTime()
{
	return m_iTotalTime;
}

int MovieSection::GetDurationTime()
{
	return m_iDurateTime;
}

bool MovieSection::IsEnd()
{
	return m_bEnd;
}

ReturnSection::ReturnSection(int time, TYPE type, int mapId, int posX, int posY)
	: mType(type)
	, mMapId(mapId)
	, mPosX(posX)
	, mPosY(posY)
{
	SetTotalTime(time);
}

ReturnSection::~ReturnSection()
{
}

void ReturnSection::Start()
{
	MovieSection::Start();

	switch (mType)
	{
	case TYPE_DIRECTLY_RETURN:
		gGetSceneMovieManager()->ReturnToScene();
		break;

	case TYPE_RETURN_TO_SPECIFIED_MAP:
	case TYPE_RETURN_TO_SCHOOL:
	{
		gGetSceneMovieManager()->SetCanDrawLoadingBar(true);
		gGetSceneMovieManager()->ReturnToScene(mMapId, Nuclear::NuclearLocation(mPosX * 16, mPosY * 16));
		gGetNetConnection()->send(fire::pb::mission::CReqGoto(mMapId, mPosX, mPosY));
		break;
	}

	case TYPE_RETURN_TO_FIGHT:
		gGetSceneMovieManager()->RemoveAllMovieEffect();
		gGetScene()->DelAllMovieSceneNpc();

		if (CEGUI::WindowManager::getSingleton().isWindowPresent("root_wnd"))
		{
			CEGUI::Window *rootWnd = CEGUI::WindowManager::getSingleton().getWindow("root_wnd");
			if (CEGUI::System::getSingleton().getGUISheet() != rootWnd)
			{
				CEGUI::System::getSingleton().setGUISheet(rootWnd);
			}
		}

		fire::pb::battle::newhand::CReqNewHandBattle requestBattle;
		gGetNetConnection()->send(requestBattle);
		break;
	}

	//gGetScene()->ClearScene();
}

SetMapSection::SetMapSection(int mapID, int xPos, int yPos) :
m_mapID(mapID),
m_PosX(xPos),
m_PosY(yPos)
{

}

SetMapSection::~SetMapSection()
{

}

void SetMapSection::Start()
{
	MovieSection::Start();

	if (gGetSceneMovieManager())
	{
		gGetSceneMovieManager()->SetCanDrawLoadingBar(false);
		gGetSceneMovieManager()->SetMap(m_mapID, m_PosX * 16, m_PosY * 16);
	}
}

StartSection::StartSection(int TotalTime)
{
	SetTotalTime(TotalTime);
}
StartSection::~StartSection()
{

}
void StartSection::Start()
{
	gGetSceneMovieManager()->OnStartSectionStart();
	MovieSection::Start();
}

void StartSection::End()
{
	MovieSection::End();
	gGetSceneMovieManager()->SetPercent(0.365f);
	gGetSceneMovieManager()->ShowBreakMovieBtn(true);
}

void StartSection::Update(int elapse)
{
	MovieSection::Update(elapse);
	if(!IsEnd())
	{
		int durateTime=GetDurationTime();
		int totalTime=GetTotalTime();
		float fPercent=(float)durateTime/(float)totalTime;
		fPercent=0.5f-0.135f*fPercent;
		gGetSceneMovieManager()->SetPercent(fPercent);
	}
}

EndSection::EndSection(int TotalTime)
{
	SetTotalTime(TotalTime);
}
EndSection::~EndSection()
{

}
void EndSection::Start()
{
	MovieSection::Start();
	gGetSceneMovieManager()->SetIsMovieEnding(true);
    gGetSceneMovieManager()->ShowBreakMovieBtn(false);
    
     gGetSceneMovieManager()->ExitMovieCloseWindow();
}

void EndSection::End()
{
	MovieSection::End();
	gGetSceneMovieManager()->SetPercent(0.0f);
	gGetSceneMovieManager()->SetIsMovieEnding(false);
}

void EndSection::Update(int elapse)
{
	MovieSection::Update(elapse);
	if(!IsEnd())
	{
		int durateTime=GetDurationTime();
		int totalTime=GetTotalTime();
		float fPercent=1.0f-(float)durateTime/(float)totalTime;
		fPercent=0.5f-0.15f*fPercent;
		gGetSceneMovieManager()->SetPercent(fPercent);
	}
}

SetCamSection::SetCamSection(int xPos,int yPos):
m_PosX(xPos),
m_PosY(yPos)
{

}

SetCamSection::~SetCamSection()
{

}

void SetCamSection::Start()
{
	MovieSection::Start();
	if (gGetSceneMovieManager())
	{
		gGetSceneMovieManager()->SetCamara(m_PosX,m_PosY);
	}
}

MoveCamSection::MoveCamSection(int xPos,int yPos,int time):
m_TargetX(xPos),
m_TargetY(yPos)
{
	SetTotalTime(time);
}

MoveCamSection::~MoveCamSection()
{
}

void MoveCamSection::Start()
{
	MovieSection::Start();

	mOldCamUpdateType = Nuclear::GetEngine()->GetWorld()->GetCameraUpdateType();

	gGetScene()->AttachCameraTo(NULL);

	Nuclear::XPCAMERA_MOVE_TO_POSITION_IN_DURATION_PARAMS params;
	params.mStartPosition = gGetScene()->GetViewport().Center();
	params.mTargetPosition = Nuclear::NuclearPoint(m_TargetX * 24, m_TargetY * 16);
	params.mDuration = GetTotalTime();
	
	Nuclear::GetEngine()->GetWorld()->SetCameraUpdateType(Nuclear::XPCAMERA_MOVE_TO_POSITION_IN_DURATION, &params);
}

void MoveCamSection::Update(int elapse)
{
	MovieSection::Update(elapse);
	if (IsEnd())
	{
		Nuclear::NuclearLocation pixLoc(m_TargetX*24,m_TargetY*16);
		Nuclear::GetEngine()->GetWorld()->SetViewport(pixLoc,false);
		Nuclear::GetEngine()->GetWorld()->SetCameraUpdateType(mOldCamUpdateType);
	}
}

CreateNpcSection::CreateNpcSection(int id,const std::wstring& identityName,int pos_X,int pos_Y,int modelID,
	const std::wstring& strNpcName, int title, int dtAction, float scale, int dir, int _colorTemplate, const std::wstring& effectName, int reapeat, int ex, int ey, const std::wstring& chengwei, const std::wstring& cwColor, int mountID, int colorA, int colorB) :
m_id(id),
m_identityName(identityName),
m_posX(pos_X),
m_pos_Y(pos_Y),
m_modelID(modelID),
m_strNpcName(strNpcName),
m_title(title),
m_dir(dir),
m_colorTemplate(_colorTemplate),
m_defaultAction(dtAction),
m_scale(scale),
m_effectName(effectName),
m_eftRepeat(reapeat),
m_effectX(ex),
m_effectY(ey),
m_chengwei(chengwei),
m_cwColor(cwColor),
m_mountID(mountID),
m_colorA(colorA),
m_colorB(colorB)
{

}

CreateNpcSection::~CreateNpcSection()
{

}

void CreateNpcSection::Start()
{
	MovieSection::Start();
	if (gGetSceneMovieManager())
	{
		if (m_defaultAction != -1) 
		{
			gGetSceneMovieManager()->CreateNpc(m_id, m_identityName, m_posX, m_pos_Y, m_modelID, m_strNpcName, m_modelID, m_dir, m_colorTemplate, 
				eActionType(m_defaultAction), m_scale, m_effectName, m_eftRepeat, m_effectX, m_effectY, m_chengwei, m_cwColor);
		}
		else
		{
			gGetSceneMovieManager()->CreateNpc(m_id, m_identityName, m_posX, m_pos_Y, m_modelID, m_strNpcName, m_modelID, m_dir, m_colorTemplate, 
				eActionType(eActionStand), m_scale, m_effectName, m_eftRepeat, m_effectX, m_effectY, m_chengwei, m_cwColor);
		}
		if(m_mountID != -1)
			gGetSceneMovieManager()->SetNpcComponent(m_identityName, eSpriteComponentType::eSprite_Horse, m_mountID);
		if (m_colorA != -1)
			gGetSceneMovieManager()->SetNpcComponent(m_identityName, eSpriteComponentType::eSprite_DyePartA, m_colorA);
		if (m_colorB != -1)
			gGetSceneMovieManager()->SetNpcComponent(m_identityName, eSpriteComponentType::eSprite_DyePartB, m_colorB);
	}
}

ShakeSection::ShakeSection(int id, int range, int time){
	m_id = id;
	m_range = range;
	m_time = time;
	SetTotalTime(50);
}

ShakeSection::~ShakeSection()
{

}

void ShakeSection::Start(){
	MovieSection::Start();
	gGetScene()->BeginShake(m_id, m_range, m_time);
}

MoveNpcSection::MoveNpcSection(const std::wstring& varIdentityName, int target_X, int target_Y, int TotalTime) :
m_identityName(varIdentityName),
m_TargetX(target_X),
m_TargetY(target_Y),
m_time(TotalTime)
{
}

MoveNpcSection::~MoveNpcSection()
{

}

void MoveNpcSection::Start()
{
	MovieSection::Start();
	if (gGetSceneMovieManager())
	{
		gGetSceneMovieManager()->MoveNpc(m_identityName,m_TargetX,m_TargetY,0,m_time);
	}
}

PlayActionSection::PlayActionSection(const std::wstring& identityName,int ActionId, int endAction,int dir):
m_identityName(identityName),
m_ActionID(ActionId),
m_endAction(endAction),
m_dir(dir)
{
}

PlayActionSection::~PlayActionSection()
{

}

void PlayActionSection::Start()
{
	if (gGetSceneMovieManager())
	{
		gGetSceneMovieManager()->NpcPlayAction(m_identityName, m_ActionID, m_endAction, m_dir);
	}
}

void PlayActionSection::End()
{
	MovieSection::End();
}

NpcSayPopSection::NpcSayPopSection(const std::wstring& identityName,const std::wstring& text):
m_identityName(identityName),
m_text(text)
{

}

NpcSayPopSection::~NpcSayPopSection()
{

}

void NpcSayPopSection::Start()
{
	MovieSection::Start();
	if (gGetSceneMovieManager())
	{
		gGetSceneMovieManager()->NpcSayPop(m_identityName,m_text);
	}
}

NpcSpeakWndSection::NpcSpeakWndSection(int headID,const std::wstring& name,const std::wstring& text, int time, bool isPause):
m_HeadID(headID),
m_NpcName(name),
m_text(text),
m_TotalTime(time),
m_DurateTime(0),
m_isPause(isPause)
{
	m_vecText.clear();
}

NpcSpeakWndSection::~NpcSpeakWndSection()
{
	
}

void NpcSpeakWndSection::ShowNextText()
{
	End();
	if (m_isPause)
	{
		gGetSceneMovieManager()->DeleCurSpeakSectionStartNext(false);
		gGetSceneMovieManager()->setSpeakSectionState(false);
		cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("ScriptNpcTalkDialog.DestroyDialog");
	}
	else
	{
		gGetSceneMovieManager()->DeleCurSpeakSectionStartNext(true);
	}
}

void NpcSpeakWndSection::Update(int elapse)
{
	m_DurateTime += elapse;
	int nIsOpen = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("ScriptNpcTalkDialog.isShow");
	if (nIsOpen == 1 && m_DurateTime >= m_TotalTime)
	{
		ShowNextText();
	}
}

void NpcSpeakWndSection::Start()
{
	MovieSection::Start();
	char HeadID[128];
	sprintf(HeadID, "%d", m_HeadID);
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("ScriptNpcTalkDialog.getInstanceAndShow");
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("ScriptNpcTalkDialog.setNpcSpeakWordsCpp", HeadID, ws2s(m_NpcName).c_str(), ws2s(m_text).c_str(), "");
}

void NpcSpeakWndSection::OnMouseButtonDown()
{
	if (!IsEnd())
	{
		int nIsOpen = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("ScriptNpcTalkDialog.isShow");
		if (nIsOpen == 1)
		{
			int nIsEnd = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("ScriptNpcTalkDialog.isCurrentSpeakFinish");
			if (nIsEnd == 0)
			{
				cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunction("ScriptNpcTalkDialog.makeAllWordsShow");
			}
			else 
			{
				ShowNextText();
			}
		}
		else
		{
			ShowNextText();
		}
	}
}


NpcComponentSection::NpcComponentSection(const std::wstring& varIdentityName, int type, int baseid) :
m_identityName(varIdentityName),
m_Type(type),
m_BaseID(baseid)
{

}

NpcComponentSection::~NpcComponentSection()
{

}

void NpcComponentSection::Start()
{
	MovieSection::Start();
	if (gGetSceneMovieManager())
	{
		gGetSceneMovieManager()->SetNpcComponent(m_identityName,(eSpriteComponentType)m_Type, m_BaseID);
	}
}

DeleteNpcSection::DeleteNpcSection(const std::wstring& identityName):
m_identityName(identityName)
{

}

DeleteNpcSection::~DeleteNpcSection()
{

}

void DeleteNpcSection::Start()
{
	MovieSection::Start();
	if (gGetSceneMovieManager())
	{
		gGetSceneMovieManager()->DeleteNpc(m_identityName);
	}
}

WaitSection::WaitSection(int time):
m_WaitTime(time)
{
	SetTotalTime(time);
}

WaitSection::~WaitSection()
{

}

void WaitSection::Start()
{
	MovieSection::Start();
}

StartSpeakSection::StartSpeakSection(int time)
{
	SetTotalTime(time);
}

StartSpeakSection::~StartSpeakSection()
{

}

void StartSpeakSection::Start()
{
	MovieSection::Start();
	gGetSceneMovieManager()->setSpeakSectionState(true);
}

SetEffectSection::SetEffectSection(const std::wstring& name,int x,int y,int effectID,int cycle, int type)://cycle为零则无限次播放
m_name(name),
m_x(x),
m_y(y),
m_effectID(effectID),
m_cycle(cycle),
m_type(type)
{

}

SetEffectSection::~SetEffectSection()
{

}

void SetEffectSection::Start()
{
	MovieSection::Start();
	if (gGetSceneMovieManager())
	{
		gGetSceneMovieManager()->SetSceneEffct(m_name,m_x,m_y,m_effectID,0,m_cycle, m_type);
	}
}

SetNpcDirSection::SetNpcDirSection(const std::wstring& name,int dir):
m_identityName(name)
,m_dir(dir)
{
	
}

SetNpcDirSection::~SetNpcDirSection()
{

}

void SetNpcDirSection::Start()
{
	gGetSceneMovieManager()->SetNpcDir(m_identityName,m_dir);
}

PlaySDEffectSection::PlaySDEffectSection(const std::wstring& efName) :
m_SoundEffectName(efName)
{
}

PlaySDEffectSection::~PlaySDEffectSection()
{
}

void PlaySDEffectSection::Start()
{
	gGetSceneMovieManager()->PlaySoundEffect(m_SoundEffectName);
}

PlaySoundSection::PlaySoundSection(int id,const std::wstring& soundName, int repeat,int _fadeTime):
m_Id(id)
,m_SoundName(soundName)
,m_Repeat(repeat)
,m_FadeTime(_fadeTime)
{

}

PlaySoundSection::~PlaySoundSection()
{

}

void PlaySoundSection::Start()
{
	gGetSceneMovieManager()->PlaySound(m_SoundName,m_Id,m_Repeat,m_FadeTime);
}

StopSoundSection::StopSoundSection(int id,int _fadeTime):
m_Id(id),
m_FadeTime(_fadeTime)
{

}

StopSoundSection::~StopSoundSection()
{

}

void StopSoundSection::Start()
{
	gGetSceneMovieManager()->StopSound(m_Id,m_FadeTime);
}

ClearSceneSection::ClearSceneSection(const std::wstring& type) :
m_TypeName(type)
{

}

ClearSceneSection::~ClearSceneSection()
{

}

void ClearSceneSection::Start()
{
	if (m_TypeName == L"all")
	{
		gGetScene()->ClearScene();
	}
}


ForbiddenAutoPlaySection::ForbiddenAutoPlaySection()
{

}

ForbiddenAutoPlaySection::~ForbiddenAutoPlaySection()
{

}

void ForbiddenAutoPlaySection::Start()
{
	// 不允许自动播放玩家语音
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithBooleanData("CChatManager.SetAllowAutoPlay", false);
}
