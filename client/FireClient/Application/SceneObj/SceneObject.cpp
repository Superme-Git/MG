#include "stdafx.h"
#include "SceneObject.h"
#include "GameOperateState.h"
#include "GameScene.h"
#include "GameUIManager.h"
#include "SceneMovieManager.h"
#include "../world/nuworld.h"

#if defined(ANDROID) && defined(LOGCAT)
#include <android/log.h>
#define  LOG_TAG    "mt3"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#else
#define LOGI
#define LOGE
#define LOGD
#endif

const float c_TalkMaxWidth = 300.0f;  //聊天泡泡框最大宽度
const float c_TalBoxTopEdge = 3.0f;   //聊天泡泡框垂直偏移
const float c_TalBoxLeftEdge = 7.0f;  //聊天泡泡框横向偏移

#define TalkBoxMaxCount 3 //一个角色最多能顶几个泡泡

static void DrawNameCB(Nuclear::ISprite* pSprite)
{
	gGetScene()->OnDrawNameCB(pSprite);
}

SceneObject::SceneObject()
	: m_dwID(0)
	, m_NameColour(0xffff0000)//名字颜色值
	, m_NameShadowColor(0xff000000) //名字的阴影颜色
	, m_MouseOverNameColor(0xfffdbff4)
	, m_MouseOverShadowColor(0xff8a0805)
	, m_bVisible(true)
	, m_pSprite(NULL)
	, m_actionType(eActionStand)
	, m_eMoveMask(eMoveMaskNormal)
	, m_LastLocation(0, 0)
	, m_titleIndex(0)
	, m_iRandomActionTime(2500)
	, m_bHighLightEnable(true)
	, m_NameTextureHandle(Nuclear::INVALID_TEXTBLOCK_HANDLE)
	, m_pNameTextBlock(NULL)
	, m_pSpriteShadow(NULL)
	, m_iModelID(0)
	, m_bInHighLevel(false)
	, m_PrevDir(0)
	, m_iAutoMove(0)
{
	m_listTalks.clear();
}
SceneObject::~SceneObject()
{
	//删除影子
	if (m_pSpriteShadow)
	{
		RemoveDurativeEffect(m_pSpriteShadow);
		m_pSpriteShadow = NULL;
	}

	DeleteSpriteModel();

	if (!m_listTalks.empty())
	{
		std::vector<sTalkUnit> tmpList = m_listTalks;
		for (size_t i = 0; i < tmpList.size(); ++i)
		{
			CEGUI::RichEditbox* pBox = tmpList[i].pEditBox;
			if (pBox)
			{
				CEGUI::WindowManager::getSingleton().destroyWindow(pBox);
			}
		}
		m_listTalks.clear();
	}

	if (m_NameTextureHandle != Nuclear::INVALID_TEXTBLOCK_HANDLE)
	{
		Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
		if (pFontMan)
		{
			pFontMan->ReleaseTextBlock(m_NameTextureHandle);
		}
	}


	m_pNameTextBlock = NULL;

}

Nuclear::NuclearLocation SceneObject::GetLogicLocation()
{
	if (m_pSprite == NULL) {
		return Nuclear::NuclearLocation(0, 0);
	}
	int x = Round_Off(m_pSprite->GetLocation().x / 1.5f);
	int y = m_pSprite->GetLocation().y;
	return Nuclear::NuclearLocation(x, y);
}
Nuclear::NuclearLocation SceneObject::GetGridLocation()
{
	return Nuclear::NuclearLocation(GetLogicLocation().x / g_logicGrid, GetLogicLocation().y / g_logicGrid);
}
bool SceneObject::IsInHighLevel()
{
	return m_bInHighLevel;
}
void SceneObject::SetInHighLevel(bool bhighlevel)
{
	m_bInHighLevel = bhighlevel;
	//同时MoveMask也改变了
	if (bhighlevel)
	{
		SetMoveMask(eMoveMaskHigh);
	}
	else
	{
		SetMoveMask(eMoveMaskNormal);
	}
	if (m_pSprite)
	{
		if (bhighlevel)
		{
			Nuclear::GetEngine()->GetWorld()->MoveSpriteLayer(m_pSprite, Nuclear::XPSL_MIDDLE1, Nuclear::XPSL_MIDDLE2);
		}
		else
		{
			Nuclear::GetEngine()->GetWorld()->MoveSpriteLayer(m_pSprite, Nuclear::XPSL_MIDDLE2, Nuclear::XPSL_MIDDLE1);
		}
	}
}
std::wstring SceneObject::GetComponentActionName(eActionType type)
{
	return gGetActionName(type);
}
std::wstring SceneObject::GetActionName(eActionType type)
{
	return gGetActionName(type);
}
void SceneObject::SetPartParam(int iLayerIndex, int iPartIndex, std::vector<float> vParam)
{
	GetSprite()->SetPartParam(iLayerIndex, iPartIndex, vParam);
}
std::vector<float> SceneObject::GetPartParam(int iLayerIndex, int iPartIndex)
{
	std::vector<float> vResult;
	vResult = GetSprite()->GetPartParam(iLayerIndex, iPartIndex);
	return vResult;
}
void SceneObject::SetPartParamIndex(int iLayerIndex, int iPartIndex, int iColorIndex)
{
	int ShapeConfig_id = GetTableNumberFromLua("npc.cnpcshape", m_iModelID, "id");
	if (ShapeConfig_id > 0)
	{
		std::vector<float> vParam;
		GetSprite()->SetPartParam(iLayerIndex, iPartIndex, vParam);
	}
}
int SceneObject::GetPartParamIndex(int iLayerIndex, int iPartIndex)
{
	return m_vPartColorIndex[iLayerIndex][iPartIndex];
}
int SceneObject::GetAutoMove()
{
	return m_iAutoMove;
}
Nuclear::NuclearLocation SceneObject::GetScreenPosition()
{
	const Nuclear::NuclearRect& cp = Nuclear::GetEngine()->GetWorld()->GetViewport();
	return GetLocation() - Nuclear::NuclearLocation(cp.left, cp.top);
}
void SceneObject::SetLocation(const Nuclear::NuclearLocation& aLocation)
{
	Nuclear::NuclearLocation location = aLocation;
	Nuclear::NuclearLocation mapsize(0, 0);
	Nuclear::GetEngine()->GetWorld()->GetMapSize(mapsize);
	if (location.x >= mapsize.x || location.x < 0
		|| location.y >= mapsize.y || location.y < 0)
	{
		int mapid = 0;
	}
	bool bCheckPos = true;

	if (gGetSceneMovieManager() && gGetSceneMovieManager()->isOnSceneMovie())
	{
		bCheckPos = false;
		if (m_eSceneType == eSceneObjMovie) {
			bCheckPos = true;
		}
	}
	if (bCheckPos)
	{
		if (location.x >= mapsize.x)
			location.x = mapsize.x - 1;

		if (location.y >= mapsize.y)
			location.y = mapsize.y - 1;

	}

	if (location.x < 0)
		location.x = 0;

	if (location.y < 0)
		location.y = 0;

	if (m_pSprite)
		m_pSprite->SetLocation(location);
}
Nuclear::NuclearLocation SceneObject::GetLocation()
{
	if (m_pSprite)
		return m_pSprite->GetLocation();
	return Nuclear::NuclearLocation(0, 0);
}
void SceneObject::SetLogicLocation(const Nuclear::NuclearLocation& logiclocation)
{
	int x = Round_Off(logiclocation.x*1.5f);
	Nuclear::NuclearLocation loc(x, logiclocation.y);
	SetLocation(loc);
}
void SceneObject::SetLogicLocation(int x, int y)
{
	Nuclear::NuclearLocation loc(Round_Off(x*1.5f), y);
	SetLocation(loc);
}
void SceneObject::SetDefaultAction(eActionType actiontype, bool holdlastframe, float freq)
{
	m_actionType = actiontype;

	if (m_eSceneType == eSceneObjBattler || m_eSceneType == eSceneObjBattleIllusion || m_eSceneType == eSceneObjMovie || m_eSceneType == eSceneObjBoat)
	{
		//如果战斗中是观战单位，则异步加载
		if (m_eSceneType == eSceneObjBattler && m_actionType == eActionStand)
		{
			m_pSprite->SetDefaultAction(GetActionName(actiontype), Nuclear::XPSALT_SYNC, freq, holdlastframe);
		}
		else
		{
			m_pSprite->SetDefaultAction(GetActionName(actiontype), Nuclear::XPSALT_SYNC, freq, holdlastframe);	//同步加载
		}
	}
	else
	{
		m_pSprite->SetDefaultAction(GetActionName(actiontype), Nuclear::XPSALT_ASYNC, freq, holdlastframe);
	}
}
void SceneObject::SetDefaultActionWithoutLogic(eActionType actiontype, bool holdlastframe, float freq)
{
	if (m_eSceneType == eSceneObjBattler || m_eSceneType == eSceneObjBattleIllusion || m_eSceneType == eSceneObjMovie || m_eSceneType == eSceneObjBoat)
	{
		//如果战斗中是观战单位，则异步加载
		if (m_eSceneType == eSceneObjBattler && m_actionType == eActionStand)
		{
			m_pSprite->SetDefaultAction(GetActionName(actiontype), Nuclear::XPSALT_SYNC, freq, holdlastframe);
		}
		else
		{
			m_pSprite->SetDefaultAction(GetActionName(actiontype), Nuclear::XPSALT_SYNC, freq, holdlastframe);	//同步加载
		}
	}
	else
	{
		m_pSprite->SetDefaultAction(GetActionName(actiontype), Nuclear::XPSALT_ASYNC, freq, holdlastframe);
	}
}
void SceneObject::SetMoveMask(eMoveMask movemask)
{
	m_eMoveMask = movemask;
	if (m_pSprite) m_pSprite->SetMoveMask(m_eMoveMask);
}
void SceneObject::SetVisible(bool v)
{
	m_bVisible = v;
	if (m_pSprite)
		m_pSprite->SetVisible(v);


	size_t nBoxCount = m_listTalks.size();
	for (size_t i = 0; i < nBoxCount; i++)
	{
		CEGUI::RichEditbox* pBox = m_listTalks[i].pEditBox;
		if (pBox != NULL)
		{
			pBox->setVisible(v);
		}

	}
}
void SceneObject::SetHighlight(bool highlight)
{
	if (m_pSprite)
	{
		m_pSprite->SetHighlight(highlight);
	}
}
void SceneObject::SetNameTextureHandle(Nuclear::TextBlockHandle handle)
{
	if (handle != Nuclear::INVALID_TEXTBLOCK_HANDLE)
	{
		m_NameTextureHandle = handle;
		Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
		if (pFontMan)
		{
			m_pNameTextBlock = pFontMan->GetTextBlock(handle);
		}
	}
}
void SceneObject::SetAutoMove(int iAutoMove)
{
	m_iAutoMove = iAutoMove;
}
void SceneObject::SetShadowScale(const float& scale)
{
	if (m_pSpriteShadow)
	{
		m_pSpriteShadow->SetScale(scale);
	}
}
void SceneObject::SetDirection(int tx, int ty)
{
	m_pSprite->SetDirection(tx, ty);
}
void SceneObject::SetDirection(Nuclear::NuclearDirection dir)
{
	m_pSprite->SetDirection(dir);
}
void SceneObject::SetDyePartIndex(int part, int index)
{
	if (m_pSprite != NULL)
	{
		if (index == 0)
			m_pSprite->SetDyePartIndex(part, index);
		else
		{
			int rIndex = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("GetDyeIndex", index);
			m_pSprite->SetDyePartIndex(part, rIndex);
		}
	}
}
bool SceneObject::IsFemale()
{
	int modelid = GetModelID();
	if (/*modelid < 1000*/IsCharacterModel())
	{
		modelid = modelid % 10;
		if (modelid == 0)
			modelid = 10;
	}
	return modelid >= 6 && modelid <= 10 && modelid != 8;	// xuxuan:dosomething
}
bool SceneObject::IsMale()
{
	int modelid = GetModelID();
	if (IsCharacterModel())
	{
		modelid = modelid % 10;
		if (modelid == 0)
			modelid = 10;
	}
	return modelid >= 1 && modelid <= 4;	//xuxuan：tryDelete
}
void SceneObject::SetAlpha(unsigned char alpha)
{
	if (m_pSprite)
	{
		m_pSprite->SetAlpha(alpha);
	}
}
//add by yangbin---start
void SceneObject::SavePrevDirection()
{
	m_PrevDir = m_pSprite->GetDirection();
}
void SceneObject::LoadPrevDirection()
{
	if (m_pSprite)	m_pSprite->SetDirection(Nuclear::NuclearDirection(m_PrevDir));
}
//add by yangbin---end

void SceneObject::DeleteSpriteModel()
{
	if (m_pSprite)
	{
		m_pSprite->SetUserData(NULL);

		if (m_eSceneType == eSceneObjBattler || m_eSceneType == eSceneObjBattleIllusion)
			Nuclear::GetEngine()->GetWorld()->DeleteSprite(m_pSprite);
		else
			Nuclear::GetEngine()->GetWorld()->DeleteSprite(m_pSprite);
		m_pSprite = NULL;
	}
}
bool SceneObject::NewSpriteModel(const std::wstring& modelname, bool bhighlevel)
{
	if (m_eSceneType == eSceneObjBattler || m_eSceneType == eSceneObjBattleIllusion)
		m_pSprite = Nuclear::GetEngine()->GetWorld()->NewSprite(Nuclear::XPSL_BATTLE, modelname);
	else
		m_pSprite = Nuclear::GetEngine()->GetWorld()->NewSprite(bhighlevel ? Nuclear::XPSL_MIDDLE2 : Nuclear::XPSL_MIDDLE1, modelname);

	SetInHighLevel(bhighlevel);
	if (m_pSprite)
	{
		m_pSprite->SetUserData(this);

		SetDefaultAction(eActionStand);
		SetVisible(m_bVisible);
		m_pSprite->SetMoveSpeed(g_moveSpeed / 1000);
		SetMoveMask(eMoveMaskNormal);
		return true;
	}
	else
		return false;
}
void SceneObject::UpdateShadowInfo(int shadowsize)
{
	std::wstring shadowpath = L"";
	switch (shadowsize)
	{
	case 1:
		shadowpath = MHSD_UTILS::GetEffectPath(10154);
		break;
	case 2:
		shadowpath = MHSD_UTILS::GetEffectPath(10153);
		break;
	case 3:
		shadowpath = MHSD_UTILS::GetEffectPath(10152);
		break;
		//0 , no shadow
	case 0:
		shadowpath = L"";
		break;
	default:
		break;
	}
	//不为空表示有影子 战斗内幻影没有影子
	if (shadowpath != L"" && m_eSceneType != eSceneObjBattleIllusion)
	{
		if (m_pSpriteShadow)
		{
			RemoveDurativeEffect(m_pSpriteShadow);
			m_pSpriteShadow = SetDurativeEffect(shadowpath, 0, 0, 0, false, false, false, false, true, true);
		}
		else
		{
			m_pSpriteShadow = SetDurativeEffect(shadowpath, 0, 0, 0, false, false, false, false, true, true);
		}
	}
	//为空表示删除影子
	else
	{
		if (m_pSpriteShadow)
		{
			RemoveDurativeEffect(m_pSpriteShadow);
			m_pSpriteShadow = NULL;
		}
	}
}
bool SceneObject::NewSpriteModel(const int& modelid, bool bhighlevel)
{
	m_iModelID = modelid;
	std::wstring ShapeConfig_shape = GetTableWStringFromLua("npc.cnpcshape", modelid, "shape");
	int ShapeConfig_dir = GetTableNumberFromLua("npc.cnpcshape", modelid, "dir");
	int ShapeConfig_shadow = GetTableNumberFromLua("npc.cnpcshape", modelid, "shadow");

	if (m_eSceneType == eSceneObjBattler || m_eSceneType == eSceneObjBattleIllusion)
	{
		m_pSprite = Nuclear::GetEngine()->GetWorld()->NewSprite(Nuclear::XPSL_BATTLE, ShapeConfig_shape);
	}
	else
	{
		m_pSprite = Nuclear::GetEngine()->GetWorld()->NewSprite(bhighlevel ? Nuclear::XPSL_MIDDLE2 : Nuclear::XPSL_MIDDLE1, ShapeConfig_shape);
	}
	if (m_pSprite)
	{
		m_pSprite->SetUserData(this);

		m_vPartColorIndex.resize(GetSprite()->GetLayerCount());
		for (int i = 0; i < GetSprite()->GetLayerCount(); i++)
		{
			m_vPartColorIndex[i].resize(3);
		}
		//设置朝向
		if (ShapeConfig_dir == 4)
			m_pSprite->SetTurnDir(4);
		else if (ShapeConfig_dir == 2)
			m_pSprite->SetTurnDir(2);

		SetDefaultAction(eActionStand);
		m_pSprite->SetMoveSpeed(g_moveSpeed / 1000);
		SetVisible(m_bVisible);
		SetMoveMask(eMoveMaskNormal);

		std::wstring wsLayerName;
		std::wstring wsResource;
		int iLayerIndex = 0;

		wsLayerName = L"body";
		wsResource = L"bodyonly";
		iLayerIndex = GetSprite()->GetLayerIndexByName(wsLayerName);
		GetSprite()->SetComponent(iLayerIndex, wsResource, 0xffffffff, wsLayerName);

		wsLayerName = L"zhuangshi";
		wsResource = L"zhuangshi";
		iLayerIndex = GetSprite()->GetLayerIndexByName(wsLayerName);
		GetSprite()->SetComponent(iLayerIndex, wsResource, 0xffffffff, wsLayerName);

		UpdateShadowInfo(ShapeConfig_shadow);

		if (m_eSceneType != eSceneObjNpc && m_eSceneType != eSceneObjFollowNpc && m_eSceneType != eSceneObjTaskShowNpc && m_eSceneType != eSceneObjClientNpc)
		{
			Nuclear::ISprite::DrawName_CallBack pfnCB = Nuclear::ISprite::DrawName_CallBack(&DrawNameCB);
			m_pSprite->RegisterDrawNameCB(pfnCB);
		}

		return true;
	}
	else
	{
		return false;
	}
}
bool SceneObject::NewSpriteModel(const int& modelid, const std::wstring& modelname, const std::wstring hair, const std::wstring head, const std::wstring body)
{
	m_iModelID = modelid;
	return false;
}
void SceneObject::ChangeSpriteModel(const int& modelid)
{
	m_iModelID = modelid;
	if (GetSprite())
	{
		std::wstring ShapeConfig_shape = GetTableWStringFromLua("npc.cnpcshape", modelid, "shape");
		int ShapeConfig_dir = GetTableNumberFromLua("npc.cnpcshape", modelid, "dir");
		int ShapeConfig_shadow = GetTableNumberFromLua("npc.cnpcshape", modelid, "shadow");
		if (m_pSprite)
		{
			m_pSprite->SetModel(ShapeConfig_shape);

			//设置朝向
			if (ShapeConfig_dir == 4)
				m_pSprite->SetTurnDir(4);
			if (ShapeConfig_dir == 2)
				m_pSprite->SetTurnDir(2);
			if (ShapeConfig_dir == 8)
				m_pSprite->SetTurnDir(8);

			std::wstring wsLayerName;
			std::wstring wsResource;
			int iLayerIndex = 0;

			wsLayerName = L"body";
			wsResource = L"bodyonly";
			iLayerIndex = GetSprite()->GetLayerIndexByName(wsLayerName);
			GetSprite()->SetComponent(iLayerIndex, wsResource, 0xffffffff, wsLayerName);

			wsLayerName = L"zhuangshi";
			wsResource = L"zhuangshi";
			iLayerIndex = GetSprite()->GetLayerIndexByName(wsLayerName);
			GetSprite()->SetComponent(iLayerIndex, wsResource, 0xffffffff, wsLayerName);

		}
		UpdateShadowInfo(ShapeConfig_shadow);
		SetDefaultAction(GetActionType());
	}
	else
	{
		NewSpriteModel(modelid);
	}
}
bool SceneObject::MoveTo(Nuclear::NuclearLocation ptMoveTarget, bool straight)
{
	if (m_pSprite)	m_pSprite->MoveTo(Nuclear::NuclearWorldLogicCoord::logic2world(ptMoveTarget), 0, NULL, straight);
	//只有当寻路成功时才播放eActionRun
	astar::Path path;
	GetPath(path);
	if (!path.empty())
	{
		if (m_actionType == eActionStand || m_actionType == eActionStandRandom)
		{
			if (m_eSceneType == eSceneObjPet)
			{
				PlayAction(eActionRun, GetScale());
			}
			else
			{
				PlayAction(eActionRun);
			}
		}
	}
	return true;
}
Nuclear::IEffect* SceneObject::SetDurativeEffect(const std::wstring &effect_name, int bindType, int dx, int dy, bool selfRef, bool basync, bool bnofollowscale, bool alwayontop, bool underSprite, bool isShadow)
{
	unsigned int flag = 0;
	if (selfRef)
		flag |= Nuclear::XPSPREFFFLAG_BIND_AUTO;  //XPSPREFFFLAG_SELF_REF; //change by lg
	if (basync)
		flag |= Nuclear::XPSPREFFFLAG_ASYNC;
	if (bnofollowscale)
		flag |= Nuclear::XPSPREFFFLAG_NO_FOLLOW_SCALE;
	if (bindType != 0)
		flag |= bindType;
	if (alwayontop)
		flag |= Nuclear::XPSPREFFFLAG_DRAW_ON_TOP;
	if (underSprite)
	{
		flag |= Nuclear::XPSPREFFFLAG_DRAW_BEFORE_SPRITE;
	}
	if (NULL != m_pSprite)
	{
		return m_pSprite->SetDurativeEffect(effect_name, Nuclear::NuclearPoint(dx, dy), flag);
	}
	else
	{
		return NULL;
	}
}
void SceneObject::TurnTo(SceneObject* pSceneObj)
{
	if (pSceneObj)
	{
		SetDirection(pSceneObj->GetLocation().x, pSceneObj->GetLocation().y);
	}
}
void SceneObject::UpdateAction(Nuclear::XPUSA_TYPE type)
{
	switch (type)
	{
	case Nuclear::XPUSAT_DEFAULTACTIONFINISH:
	case Nuclear::XPUSAT_PLAYACTIONFINISH:
	{
		if (m_pSprite->IsMoving())
		{
			switch (m_actionType)
			{
			case eActionStandRandom:
			case eActionStand:
			case eActionRun:
			{
				astar::Path path;
				GetSprite()->GetPath(path);
				if (path.empty())
				{
					PlayAction(eActionStand);
				}
				else
				{
					if (m_eSceneType == eSceneObjPet)
					{
						PlayAction(eActionRun, GetScale());
					}
					else
					{
						PlayAction(eActionRun);
					}
				}
			}
			break;
			default:
				break;
			}
		}
		else
		{
			if (m_actionType != eActionDying && m_actionType != eActionDeath && m_actionType != eActionDeathStill)
			{
				PlayAction(eActionStand);
			}
		}
	}
	break;
	case Nuclear::XPUSAT_TELEPORTEND:
		break;
	case Nuclear::XPUSAT_MOVEINFLEXION:
	{
		astar::Path path;
		GetSprite()->GetPath(path);
		if (path.empty())
		{
			StopMove();
		}
	}
	break;
	default:
		break;
	}
}
void SceneObject::StandRandomAction(int now, int delta)
{
	if (m_actionType == eActionStand)
	{
		m_iRandomActionTime -= delta;
		if (m_iRandomActionTime <= 0)
		{
			if (rand() % 4 == 0)
				PlayAction(eActionStandRandom);
			m_iRandomActionTime = 2500;
		}
	}
}
bool SceneObject::PlayAction(eActionType actionType, float freq)
{
	if (!m_pSprite) return false;
	if (m_actionType == eActionMagic1 && m_eSceneType == eSceneObjBattleIllusion)
	{
		int i = 0;
	}
	m_actionType = actionType;

	std::wstring actionname = GetActionName(m_actionType);
	//主角造型跑步频率加快
	if (actionType == eActionRun && (IsFemale() || IsMale()))
	{
		freq = 0.9f;
	}

	if (m_eSceneType == eSceneObjBattler || m_eSceneType == eSceneObjBattleIllusion || m_eSceneType == eSceneObjMovie || m_eSceneType == eSceneObjBoat)
	{
		if (m_eSceneType == eSceneObjBattler || m_eSceneType == eSceneObjBattleIllusion)
		{
			return m_pSprite->PlayAction(actionname, Nuclear::XPSALT_SYNC, freq);
		}
		else
		{
			return m_pSprite->PlayAction(actionname, Nuclear::XPSALT_SYNC, freq);
		}
	}
	else
	{
		return m_pSprite->PlayAction(actionname, Nuclear::XPSALT_SYNC, freq);
	}
}
void SceneObject::StopMove()
{
	if (m_pSprite)
	{
		m_pSprite->StopMove();
	}
	PlayAction(eActionStand);
}
bool SceneObject::OnSetCursor()
{
	switch (gGetGameOperateState()->GetGameCursorType())
	{
	case eGameCursorType_Talk:
	{
		if (!CanBeVisited())
		{
			gGetGameOperateState()->ChangeGameCursorType(eGameCursorType_Normal);
			return true;
		}
	}
	break;
	case eGameCursorType_Normal:
	{
		if (CanBeVisited())
		{
			gGetGameOperateState()->ChangeGameCursorType(eGameCursorType_Talk);
			return true;
		}
		if (CanPickedUp())
		{
			gGetGameOperateState()->ChangeGameCursorType(eGameCursorType_Pickup);
			return true;
		}
	}
	break;
	default:
		break;
	}
	return false;
}
void SceneObject::Draw(int now)
{
	if (IsVisiable() == false)
		return;

	Nuclear::World* world = static_cast<Nuclear::World*>(Nuclear::GetEngine()->GetWorld());
	float fWorldScale = world->GetScale();

	Nuclear::NuclearLocation loc = GetLocation();

	Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
	if (pFontMan&&m_NameTextureHandle != Nuclear::INVALID_TEXTBLOCK_HANDLE&&m_pNameTextBlock != NULL)
	{
		DWORD colour = m_NameColour;
		DWORD shadowColor = m_NameShadowColor;

		m_pNameTextBlock->SetFontColor(colour, colour);
		m_pNameTextBlock->SetShadowColor(shadowColor);

		int height = m_pNameTextBlock->GetTextHeight();
		int width = m_pNameTextBlock->GetTextWidth();

		int left = loc.x * fWorldScale - Nuclear::GetEngine()->GetWorld()->GetViewport().left * fWorldScale - width / 2;
		int top = loc.y * fWorldScale - Nuclear::GetEngine()->GetWorld()->GetViewport().top * fWorldScale + DRAW_NAME_OFFSET + 15 * m_titleIndex++;

		if (m_eSceneType == eSceneObjNpc)
		{
			top += 7;
		}
		pFontMan->DrawText(m_NameTextureHandle, (float)left, (float)top, NULL);
	}
	m_titleIndex = 0;
}
void SceneObject::AddClientTalk(const std::wstring& strTalk, DWORD color, bool bCheckShied)
{
	std::wstring strFormatMsg;

	StringCover::FormatString(strFormatMsg, L"<T t=\"%ls\" c=\"%x\"></T>", strTalk.c_str(), color);
	AddTalk(strFormatMsg);
}
void SceneObject::AddTalk(const std::wstring& strTalk, bool bCheckShied)
{
	cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->pushStringToLuaStack(ws2s(strTalk).c_str());
	string tmp = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionBackString("CChatManager.ReplaceColor", 1);
	CEGUI::String MsgParseString(s2ws(tmp).c_str());

	std::wstring strMsgNew = StringCover::to_wstring(MsgParseString.c_str());

	Nuclear::NuclearRect rect = Nuclear::GetEngine()->GetWorld()->GetViewport();
	Nuclear::NuclearLocation loc = GetLocation();
	float yOffset = 0.0f;
	if (GetSprite() && GetSprite()->GetBubbleItemOffset())
	{
		yOffset = loc.y - rect.top - (float)GetSprite()->GetBubbleItemOffset() - 10.0f;
		if (m_eSceneType == eSceneObjBattler)
		{
			yOffset -= 25.0f;
		}
	}
	else
	{
		if (m_eSceneType == eSceneObjBattler)
			yOffset = loc.y - rect.top - 125.0f;
		else
			yOffset = loc.y - rect.top - 100.0f;
	}

	if (m_eSceneType == eSceneObjNpc || m_eSceneType == eSceneObjMovie)
	{
		bCheckShied = false;
	}

	AddTalk(strMsgNew, yOffset, bCheckShied);

	if (m_listTalks.size() > TalkBoxMaxCount)
	{
		std::vector<sTalkUnit>::iterator it = m_listTalks.begin();

		CEGUI::RichEditbox* pTalkBox = (*it).pEditBox;
		if (pTalkBox != NULL)
		{
			CEGUI::WindowManager::getSingleton().destroyWindow(pTalkBox);
		}
	}

	if (m_listTalks.size() > 0)
	{
		int nBoxCount = (int)m_listTalks.size();

		for (int i = nBoxCount - 2; i >= 0; --i)
		{
			CEGUI::RichEditbox* pBox = m_listTalks[i].pEditBox;
			if (pBox != NULL)
			{
				CEGUI::Window* pRootWindow = CEGUI::System::getSingleton().getGUISheet();
				float yPos = CEGUI::CoordConverter::windowToScreenY(*pRootWindow, m_listTalks[i + 1].fDestYPos);
				float BoxHeight = pBox->getPixelSize().d_height;
				yPos -= BoxHeight + 3.0f;
				m_listTalks[i].fDestYPos = yPos;
			}
		}
	}
}
void SceneObject::ShowHideAllTalk(bool visible)
{
	for (std::vector<sTalkUnit>::iterator it = m_listTalks.begin(); it != m_listTalks.end(); it++)
	{
		if ((*it).pEditBox)
		{
			(*it).pEditBox->setVisible(visible);
		}
	}
}
void SceneObject::AddTalk(const std::wstring& strTalk, float V_Offset, bool bCheckShied)
{
	CEGUI::WindowManager& winMgr = CEGUI::WindowManager::getSingleton();
	CEGUI::RichEditbox* pRichEditBox = static_cast<CEGUI::RichEditbox*>(winMgr.createWindow("TaharezLook/POPORichEditbox"));
	if (pRichEditBox != NULL)
	{

		pRichEditBox->SetOnShiedUIState(false);
		pRichEditBox->SetTimeAutoClose(true);
		pRichEditBox->setReadOnly(true);
		pRichEditBox->setShowVertScrollbar(false);
		pRichEditBox->setMousePassThroughEnabled(true);
		pRichEditBox->SetForceHideVerscroll(true);
		pRichEditBox->setVisible(m_bVisible);
		pRichEditBox->SetHoriAutoCenter(true);
		pRichEditBox->SetVertAutoCenter(true);
		pRichEditBox->SetTextYOffset(cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithStringData("GetNumberValueForStrKey", "CHAT_POP_TEXT_Y_OFF"));

		pRichEditBox->subscribeEvent(CEGUI::Window::EventDestructionStarted, CEGUI::Event::Subscriber(&SceneObject::HandleRemoveTalkBox, this));
		CEGUI::UDim width(0.0f, cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithStringData("GetNumberValueForStrKey", "CHAT_POP_MAX_WIDTH"));
		CEGUI::UDim height(0.0f, 100.0f);
		pRichEditBox->setSize(CEGUI::UVector2(width, height));
		pRichEditBox->AppendParseText(strTalk, bCheckShied);
		pRichEditBox->Refresh();
		CEGUI::Size NeedSize = pRichEditBox->GetExtendSize();
		NeedSize.d_width += 2.0f * cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithStringData("GetNumberValueForStrKey", "CHAT_POP_LEFT_OFF");
		NeedSize.d_height += 2.0f * cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithStringData("GetNumberValueForStrKey", "CHAT_POP_TOP_OFF");

		pRichEditBox->setSize(CEGUI::UVector2(CEGUI::UDim(0.0f, NeedSize.d_width), CEGUI::UDim(0.0f, NeedSize.d_height)));

		CEGUI::Window* pRootWindow = CEGUI::System::getSingleton().getGUISheet();
		if (pRootWindow != NULL)
		{
			float arrowWidth = 16.0f;
			float arrowHeight = 16.0f;

			pRootWindow->addChildWindow(pRichEditBox);
			pRichEditBox->setAlwaysOnBottom(true);

			Nuclear::NuclearLocation loc = GetLocation();
			RECT rct;
			rct.left = loc.x - Nuclear::GetEngine()->GetWorld()->GetViewport().left - (int)(NeedSize.d_width / 2.0f);

			rct.top = (long)(V_Offset - NeedSize.d_height);
			float xPos = CEGUI::CoordConverter::screenToWindowX(*pRootWindow, (float)rct.left);
			float yPos = CEGUI::CoordConverter::screenToWindowY(*pRootWindow, (float)rct.top);

			CEGUI::UDim xUDimPos(0.0f, xPos);
			CEGUI::UDim yUDimPos(0.0f, yPos - arrowHeight);
			pRichEditBox->setPosition(CEGUI::UVector2(xUDimPos, yUDimPos));
			// 战斗中对话框右边出框的问题
			if (xPos > Nuclear::GetEngine()->GetLogicWidth() - NeedSize.d_width)
			{
				float reallyPos = Nuclear::GetEngine()->GetLogicWidth() - NeedSize.d_width;
				pRichEditBox->setXPosition(CEGUI::UDim(0.0f, reallyPos));
			}
			// 左边出框
			if (xPos < 0)
			{
				pRichEditBox->setXPosition(CEGUI::UDim(0.0f, 0.0));
			}
			sTalkUnit talk;
			talk.pEditBox = pRichEditBox;
			talk.fDestYPos = yPos - arrowHeight;
			m_listTalks.push_back(talk);


			CEGUI::Window* arrow = winMgr.createWindow("TaharezLook/StaticImage");
			CEGUI::UDim arrowPosX(0.5f, -arrowWidth * 0.5f);
			CEGUI::UDim arrowPosY(0.0f, NeedSize.d_height);
			arrow->setPosition(CEGUI::UVector2(arrowPosX, arrowPosY));
			arrow->setSize(CEGUI::UVector2(cegui_absdim(arrowWidth), cegui_absdim(arrowHeight)));
			arrow->setProperty("Image", "set:skillui image:toumingjiao");
			arrow->setClippedByParent(false);
			arrow->setVisible(true);
			pRichEditBox->addChildWindow(arrow);
		}
	}
}
bool SceneObject::HandleRemoveTalkBox(const CEGUI::EventArgs& e)
{
	const CEGUI::WindowEventArgs& WindowArgs = static_cast<const CEGUI::WindowEventArgs&>(e);
	if (WindowArgs.window != NULL)
	{
		CEGUI::RichEditbox* pBox = static_cast<CEGUI::RichEditbox*>(WindowArgs.window);
		if (pBox != NULL)
		{
			std::vector<sTalkUnit>::iterator it = m_listTalks.begin();
			for (; it != m_listTalks.end(); it++)
			{
				if ((*it).pEditBox == pBox)
				{
					m_listTalks.erase(it);
					break;
				}
			}
		}
	}
	return true;
}
void SceneObject::Run(int now, int delta)
{
	Nuclear::NuclearLocation logLoc = GetLogicLocation();
	if (m_eSceneType != eSceneObjBattler&&m_eSceneType != eSceneObjBattleIllusion)
	{
		if (GetSprite() &&
			gGetScene() &&
			(gGetSceneMovieManager()->isOnSceneMovie() ? gGetSceneMovieManager()->IsMaskArea(logLoc) : gGetScene()->IsMaskArea(logLoc)) &&
			m_iAutoMove == 0
			)
		{
			if (GetSprite()->GetAlpha() > 5)
			{
				GetSprite()->SetAlpha(72);
			}
			if (m_pSpriteShadow)
			{
				m_pSpriteShadow->Stop();
			}
		}
		else
		{
			if (GetSprite() && GetSprite()->GetAlpha() > 5)
			{
				GetSprite()->SetAlpha(255);
				if (m_pSpriteShadow)
				{
					m_pSpriteShadow->Play();
				}
			}
			else
			{
				if (m_pSpriteShadow)
				{
					m_pSpriteShadow->Stop();
				}
			}
		}
	}

	Nuclear::NuclearLocation loc = GetLocation();

	Nuclear::NuclearRect rect = Nuclear::GetEngine()->GetWorld()->GetViewport();
	loc.x = loc.x - rect.left;
	loc.y = loc.y - rect.top;

	if (loc != m_LastLocation)
	{
		int x_offset = loc.x - m_LastLocation.x;
		int y_offset = loc.y - m_LastLocation.y;
		if (m_LastLocation.x != 0)
			OnScreenLocationChange(x_offset, y_offset);
		m_LastLocation = loc;
	}

	if (m_listTalks.size() > 1)
	{
		for (size_t i = 0; i < m_listTalks.size() - 1; i++)
		{
			CEGUI::RichEditbox* pBox = m_listTalks[i].pEditBox;
			if (pBox)
			{
				float curpos = pBox->getYPosition().d_offset;
				if ((curpos - m_listTalks[i].fDestYPos) >= 0.5f)
				{
					curpos -= 2.5f;
					if (curpos < m_listTalks[i].fDestYPos)
					{
						curpos = m_listTalks[i].fDestYPos;
					}
					pBox->setYPosition(CEGUI::UDim(0, curpos));
				}

			}
		}
	}
}
void SceneObject::OnScreenLocationChange(int offset_X, int offset_Y)
{
	size_t nBoxCount = m_listTalks.size();
	for (size_t i = 0; i < nBoxCount; i++)
	{
		CEGUI::RichEditbox* pBox = m_listTalks[i].pEditBox;
		if (pBox != NULL)
		{
			float xPos = pBox->getXPosition().d_offset;
			float yPos = pBox->getYPosition().d_offset;
			xPos += (float)offset_X;
			yPos += (float)offset_Y;
			pBox->setXPosition(CEGUI::UDim(0.0f, xPos));
			pBox->setYPosition(CEGUI::UDim(0.0f, yPos));
			m_listTalks[i].fDestYPos += offset_Y;

		}

	}
}
Nuclear::IEffect* SceneObject::PlayEffect(const std::wstring &effect_name, int dx, int dy, int times, bool selfRef, bool bnofollowscale, bool playsound, int TopMidBottom, int TimeMode, int iRot)//1:Top,0:Mid,-1:Bottom //0:LoopNum,1:Time
{
	unsigned int flag = 0;
	if (selfRef)
		flag |= Nuclear::XPSPREFFFLAG_SELF_REF;
	if (bnofollowscale)
		flag |= Nuclear::XPSPREFFFLAG_NO_FOLLOW_SCALE;
	if (!playsound)
		flag |= Nuclear::XPSPREFFFLAG_NO_SOUND;

	if (TopMidBottom > 0)
	{
		flag |= Nuclear::XPSPREFFFLAG_DRAW_ON_TOP;
	}
	else if (TopMidBottom == 0)
	{
	}
	else if (TopMidBottom == -1)
	{
		flag |= Nuclear::XPSPREFFFLAG_DRAW_BEFORE_SPRITE;
	}
	else if (TopMidBottom == -2)
	{
		flag |= Nuclear::XPSPREFFFLAG_DRAW_ON_BOTTOM;
	}

	return m_pSprite != NULL ? m_pSprite->PlayEffect(effect_name, dx, dy, times, flag, 0, TimeMode, iRot) : NULL;
}
Nuclear::IEffect* SceneObject::PlayEffect(const std::wstring &effect_name, bool basync, bool playsound, bool linkframe, int times, int TopMidBottom, int TimeMode, int iRot)//1:Top,0:Mid,-1:Bottom //0:LoopNum,1:Time
{
	unsigned int flag = 0;
	if (basync)
		flag |= Nuclear::XPSPREFFFLAG_ASYNC;
	if (!playsound)
		flag |= Nuclear::XPSPREFFFLAG_NO_SOUND;
	if (linkframe)
		flag |= Nuclear::XPSPREFFFLAG_LINKING_FRAME;

	if (TopMidBottom > 0)
	{
		flag |= Nuclear::XPSPREFFFLAG_DRAW_ON_TOP;
	}
	else if (TopMidBottom == 0)
	{
	}
	else if (TopMidBottom == -1)
	{
		flag |= Nuclear::XPSPREFFFLAG_DRAW_BEFORE_SPRITE;
	}
	else if (TopMidBottom == -2)
	{
		flag |= Nuclear::XPSPREFFFLAG_DRAW_ON_BOTTOM;
	}

	return m_pSprite != NULL ? m_pSprite->PlayEffect(effect_name, 0, 0, times, flag, 0, TimeMode, iRot) : NULL;
}
Nuclear::IEffect* SceneObject::Play3DEffect(const std::wstring &effect_name, const std::wstring &hostname, bool async, int times, int x, int y)
{
	return m_pSprite != NULL ? m_pSprite->PlayEffect(effect_name, 0, 0, 1, Nuclear::XPSPREFFFLAG_ASYNC, 0) : NULL;
}

void SceneObject::ReleaseNameTexture()
{
	LOGD("UpdatNameTexture release old");
	if (m_NameTextureHandle != Nuclear::INVALID_TEXTBLOCK_HANDLE)
	{
		Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
		if (pFontMan)
		{
			LOGD("UpdatNameTexture real release old");
			pFontMan->ReleaseTextBlock(m_NameTextureHandle);
			m_NameTextureHandle = Nuclear::INVALID_TEXTBLOCK_HANDLE;
			m_pNameTextBlock = NULL;
		}
	}
}
void SceneObject::ReloadNameTexture()
{
	std::wstring strName(GetName());
	LOGD("UpdatNameTexture strName %s", SHARE_Wstring2String(strName).c_str());
	if (!strName.empty())
	{
		Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
		if (pFontMan)
		{
			DWORD color = GetNameColour();
			Nuclear::TextBlockHandle handle = pFontMan->NewText(strName.c_str(), 0, color, color);
			LOGD("UpdatNameTexture new handle %d", handle);
			SetNameTextureHandle(handle);
		}
	}
}
void SceneObject::UpdatNameTexture(bool bReleaseOld)
{
	if (bReleaseOld)
	{
		LOGD("UpdatNameTexture release old");
		if (m_NameTextureHandle != Nuclear::INVALID_TEXTBLOCK_HANDLE)
		{
			Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
			if (pFontMan)
			{
				LOGD("UpdatNameTexture real release old");
				pFontMan->ReleaseTextBlock(m_NameTextureHandle);
				m_NameTextureHandle = Nuclear::INVALID_TEXTBLOCK_HANDLE;
				m_pNameTextBlock = NULL;
			}

		}
	}
	else if (m_NameTextureHandle != Nuclear::INVALID_TEXTBLOCK_HANDLE)
	{
		LOGD("UpdatNameTexture not release old");
		return;
	}

	std::wstring strName(GetName());
	LOGD("UpdatNameTexture strName %s", SHARE_Wstring2String(strName).c_str());
	if (!strName.empty())
	{
		Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
		if (pFontMan)
		{
			DWORD color = GetNameColour();
			Nuclear::TextBlockHandle handle = pFontMan->NewText(strName.c_str(), 0, color, color);
			LOGD("UpdatNameTexture new handle %d", handle);
			SetNameTextureHandle(handle);
		}
	}
}

