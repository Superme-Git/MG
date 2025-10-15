#include "stdafx.h"
#include "Npc.h"
#include "GameScene.h"
#include "MessageManager.h"
#include "../world/nuworld.h"

Npc::Npc()
	: m_pTaskStateEffect(NULL)
	, m_pTitleEffect(NULL)
	, m_pInBattleEffect(NULL)
	, m_totaldir(4)
	, m_iLastChatTime(0)
	, m_ptInitLocation(-1, -1)
	, m_TitleTextureHandle(Nuclear::INVALID_TEXTBLOCK_HANDLE)
	, m_pTitleTextBlock(NULL)
	, m_bFadeOut(false)
	, m_bDisappear(false)
	, m_TitleColor(0xFF00F6FF)
	, m_TitleShadowColor(0xff000000)
	, m_MouseOverTitleColor(0xffe04389)
	, m_MouseOverTitleShadowColor(0xff22010a)
	, m_iLeavingScene(-1)
	, m_pVisitEffect(NULL)
{
	m_eSceneType = eSceneObjNpc;
	SetNameColour(0xff00f0ff);
	SetNameShadowColor(0xff000000);
	SetMouseOverNameColor(0xfffdbff4);
	SetMouseOverShadowColor(0xff8a0805);
}
Npc::~Npc()
{
	if (m_pTaskStateEffect)
	{
		RemoveDurativeEffect(m_pTaskStateEffect);
		m_pTaskStateEffect = NULL;
	}
	if (m_pInBattleEffect)
	{
		RemoveDurativeEffect(m_pInBattleEffect);
		m_pInBattleEffect = NULL;
	}
	if (m_pTitleEffect)
	{
		RemoveDurativeEffect(m_pTitleEffect);
		m_pTitleEffect = NULL;
	}
	if (m_pVisitEffect)
	{
		RemoveDurativeEffect(m_pVisitEffect);
		m_pVisitEffect = NULL;
	}

	if (m_TitleTextureHandle != Nuclear::INVALID_TEXTBLOCK_HANDLE)
	{
		Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
		if (pFontMan)
		{
			pFontMan->ReleaseTextBlock(m_TitleTextureHandle);
		}
	}
	m_pTitleTextBlock = NULL;
}

bool Npc::CanFBAttack()
{
	return false;
}
bool Npc::CanFBRepair()
{
	return false;
}
bool Npc::OnSetCursor()
{
	if (SceneObject::OnSetCursor())
		return true;
	return false;
}
bool Npc::CanBeVisited()
{
	if (m_iLeavingScene >= 0)
	{
		return false;
	}

	int nParam1 = m_NpcBaseData.npctype;
	int nParam2 = 0;
	int nParam3 = 0;
	int nParam4 = 0;
	int nResult = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("CanBeVisited", nParam1, nParam2, nParam3, nParam4);

	if (nResult == 1)
	{
		return true;
	}
	return false;
}
bool Npc::CanBeAttack()
{
	if (m_iLeavingScene >= 0)
	{
		return false;
	}

	if (m_NpcBaseData.npctype == eNpcTypeTalkMonster)
	{
		return true;
	}
	return false;
}
bool Npc::CanPickedUp()
{
	if (m_NpcBaseData.npctype == eNpcUndeadSceneItem && m_actionType == eActionStand)
	{
		return true;
	}
	else if (m_NpcBaseData.npctype == eNpcTypeFossick && CanFossick())
	{
		return true;
	}
	else if (m_NpcBaseData.npctype == eNpcItemBox && !m_bFadeOut)
	{
		return true;
	}
	else
		return false;
}
int Npc::CanBeTriggered()
{
	if ((m_NpcBaseData.npctype == eNpcClickToTrigger)
		)
	{
		return m_NpcBaseData.npctype;
	}

	return -1;
}
bool Npc::IsInHighLevel()
{
	return m_data.bHighlevel;
}
Nuclear::NuclearLocation Npc::GetInitLocation() {
	return m_ptInitLocation;
}
bool Npc::SetAttribute(stNpcData* pData)
{
	m_data = *pData;

	stNPCConfig config;
	m_NpcBaseData = config;
	m_NpcBaseData.id = GetTableNumberFromLua("npc.cnpcconfig", pData->NpcBaseID, "id");
	m_NpcBaseData.bodytype = GetTableFloatFromLua("npc.cnpcconfig", pData->NpcBaseID, "bodytype");
	m_NpcBaseData.npctype = GetTableNumberFromLua("npc.cnpcconfig", pData->NpcBaseID, "npctype");
	m_NpcBaseData.modelID = GetTableNumberFromLua("npc.cnpcconfig", pData->NpcBaseID, "modelID");
	m_NpcBaseData.name = GetTableWStringFromLua("npc.cnpcconfig", pData->NpcBaseID, "name");
	m_NpcBaseData.foottitle = GetTableWStringFromLua("npc.cnpcconfig", pData->NpcBaseID, "foottitle");
	m_NpcBaseData.headtitle = GetTableWStringFromLua("npc.cnpcconfig", pData->NpcBaseID, "headtitle");
	m_NpcBaseData.title = GetTableWStringFromLua("npc.cnpcconfig", pData->NpcBaseID, "title");
	m_NpcBaseData.area1colour = GetTableNumberFromLua("npc.cnpcconfig", pData->NpcBaseID, "area1colour");
	m_NpcBaseData.area2colour = GetTableNumberFromLua("npc.cnpcconfig", pData->NpcBaseID, "area2colour");
	m_NpcBaseData.mapid = GetTableNumberFromLua("npc.cnpcconfig", pData->NpcBaseID, "mapid");
	m_NpcBaseData.xPos = GetTableNumberFromLua("npc.cnpcconfig", pData->NpcBaseID, "xPos");
	m_NpcBaseData.yPos = GetTableNumberFromLua("npc.cnpcconfig", pData->NpcBaseID, "yPos");
	m_NpcBaseData.hide = GetTableNumberFromLua("npc.cnpcconfig", pData->NpcBaseID, "hide");
	m_NpcBaseData.ndir = GetTableNumberFromLua("npc.cnpcconfig", pData->NpcBaseID, "ndir");
	m_NpcBaseData.nstate = GetTableNumberFromLua("npc.cnpcconfig", pData->NpcBaseID, "nstate");

	SetID(m_data.NpcID);

	UpdatNameTexture();

	InitNameAndTitleColor();

	if (!m_NpcBaseData.foottitle.empty())
	{
		if (!m_data.strName.empty())
		{

			if (m_TitleTextureHandle == Nuclear::INVALID_TEXTBLOCK_HANDLE)
			{
				DWORD color = 0xFFFFFFFF;
				Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();
				if (pFontMan)
				{
					m_TitleTextureHandle = pFontMan->NewText(m_NpcBaseData.foottitle.c_str(), 0, color, color);
					m_pTitleTextBlock = pFontMan->GetTextBlock(m_TitleTextureHandle);
				}
			}
		}
	}

	if (NULL == GetSprite() && m_NpcBaseData.id != -1)	//设置角色造型
	{
		int shapeID = pData->ShapeID != 0 ? pData->ShapeID : m_NpcBaseData.modelID;

		if (NULL == GetSprite())
		{
			NewSpriteModel(shapeID, m_data.bHighlevel);
		}
		else
		{
			ChangeSpriteModel(shapeID);
			SetInHighLevel(m_data.bHighlevel);
		}

		GetSprite()->SetDyePartIndex(0, m_NpcBaseData.area1colour);
		GetSprite()->SetDyePartIndex(1, m_NpcBaseData.area2colour);

		int ShapeConfig_showWeaponId = GetTableNumberFromLua("npc.cnpcshape", shapeID, "showWeaponId");
		int ShapeConfig_showHorseShape = GetTableNumberFromLua("npc.cnpcshape", shapeID, "showHorseShape");
		if (ShapeConfig_showWeaponId != 0)
		{
			SetSpriteComponent(eSprite_Weapon, ShapeConfig_showWeaponId);
		}

		if (ShapeConfig_showHorseShape != 0)
		{
			SetSpriteComponent(eSprite_Horse, ShapeConfig_showHorseShape);
		}


		m_totaldir = GetTableNumberFromLua("npc.cnpcshape", shapeID, "dir");
		if (m_totaldir == 1)
		{
			SetDirection(Nuclear::XPDIR_BOTTOM);
		}
		else if (GetSprite()->GetTurnDir() == 4)
		{
			if (m_data.dir != 0)
			{
				SetDirection(m_data.dir);
				if (m_data.dir == Nuclear::XPDIR_BOTTOM)
					SetDirection(Nuclear::XPDIR_BOTTOMRIGHT);
			}
			else
			{
				Nuclear::NuclearDirection dir = gGetScene()->GetClientConfigNpcDirByBaseID(m_data.NpcBaseID);
				if (dir == Nuclear::XPDIR_BOTTOMRIGHT ||
					dir == Nuclear::XPDIR_BOTTOMLEFT ||
					dir == Nuclear::XPDIR_TOPRIGHT ||
					dir == Nuclear::XPDIR_TOPLEFT)
				{
					SetDirection(dir);
				}
				else
				{
					if (rand() % 2 == 0)
					{
						SetDirection(Nuclear::XPDIR_BOTTOMRIGHT);
					}
					else
					{
						SetDirection(Nuclear::XPDIR_BOTTOMLEFT);
					}
				}
			}
		}
		else
		{
			if (m_data.dir == Nuclear::XPDIR_TOP)
				m_data.dir = Nuclear::XPDIR_BOTTOM;
			SetDirection(m_data.dir);
		}

		if (m_NpcBaseData.npctype == eNpcClickToTrigger)
		{
			SetHighlightEnable(false);
		}

		if (m_NpcBaseData.bodytype < 0.2f)
			m_NpcBaseData.bodytype = 0.2f;
		else if (m_NpcBaseData.bodytype > 5.0f)
			m_NpcBaseData.bodytype = 5.0f;
		SetScale(m_NpcBaseData.bodytype);

		m_ptInitLocation = m_data.ptPos;
		SetLogicLocation(m_data.ptPos);

		if (!(m_data.destPos.x == 0 && m_data.destPos.y == 0))
		{
			if (m_data.moveSpeed > 0)
			{
				GetSprite()->SetMoveSpeed(m_data.moveSpeed / 1000.0f);
			}

			MoveTo(m_data.destPos);
		}
		if (!m_NpcBaseData.headtitle.empty())
		{
			if (m_pTitleEffect) RemoveDurativeEffect(m_pTitleEffect);
			int yoffset = -(GetSprite()->GetBubbleItemOffset() == 0 ? DEFAULT_HPBAR_HEIGHT : GetSprite()->GetBubbleItemOffset());
			m_pTitleEffect = SetDurativeEffect(L"geffect/sprite/npc/" + m_NpcBaseData.headtitle, 0, 0, yoffset*m_NpcBaseData.bodytype, false, false, true);
		}
		if (GetSprite())
		{
			GetSprite()->SetUseWorldScale(1);
		}

		return true;
	}
	else
	{
		std::wostringstream strstream;

		strstream << "Error Info:Can't Find Npc BaseID in\" bool Npc::SetAttribute(stNpcData* pData)\" ID = " << pData->NpcBaseID << std::endl;

		XPLOG_ERROR(strstream.str().c_str());
	}


	return false;
}

void Npc::UpdateNpcState(int scenestate)
{
	int estate = eNpcStateInBattle;
	while (estate < eNpcStateMax)
	{
		//说明是添加了某个状态
		if ((m_data.scenestate & estate) == 0x00
			&& (scenestate & estate) != 0x00)
		{
			AddNpcState(eNpcState(estate));
		}
		//说明是删除了某个状态
		else if ((m_data.scenestate & estate) != 0x00
			&& (scenestate & estate) == 0x00)
		{
			RemoveNpcState(eNpcState(estate));
		}
		estate = estate << 1;
	}
	m_data.scenestate = scenestate;
}

void	Npc::AddNpcState(eNpcState e)
{
	switch (e)
	{
	case eNpcStateInBattle:	//战斗状态
	{
		if (m_pInBattleEffect == NULL)
		{
			m_pInBattleEffect = SetDurativeEffect(MHSD_UTILS::GetEffectPath(10164), 0, 0, 0, false, false, false, true);
		}
		break;
	}
	default:
		break;
	}
}
void	Npc::RemoveNpcState(eNpcState e)
{
	switch (e)
	{
	case eNpcStateInBattle:	//战斗状态
	{
		if (m_pInBattleEffect)
		{
			RemoveDurativeEffect(m_pInBattleEffect);
			m_pInBattleEffect = NULL;
		}
		break;
	}
	default:
		break;
	}
}

void Npc::SetArea1Color(int colorID)
{
	if (GetSprite())
	{
		DWORD color = GetNpcColorByColorID(GetSprite()->GetModelName(), colorID);
		SetComponent(g_npcAreaColorID, g_npcAreaColorComponentName, color);
	}
}
void Npc::SetArea2Color(int color)
{
	SetComponent(2, L"Area2", color ? color : 0xFFFFFFFF);
}
void Npc::SetQuestState(eNpcMissionState queststate, bool bHideImmediate)
{
	m_data.dQuestState = queststate;
	if (m_pTaskStateEffect)
	{
		RemoveDurativeEffect(m_pTaskStateEffect);
		m_pTaskStateEffect = NULL;
	}

	std::wstring taskeffect;

	switch (m_data.dQuestState)
	{
	case eNpcMissionNoQuest:
	{
		if (m_NpcBaseData.hide == 1)
		{
			SetVisible(false);
		}
		else if (m_NpcBaseData.hide > 1 && bHideImmediate == true)
		{
			SetVisible(false);
		}
	}
	break;
	case eNpcMissionDisplay:
	{
		if (m_NpcBaseData.hide >= 1)
			SetVisible(true);

	}
	break;
	case eNpcMissionInCompleteQuest:
	{
		if (m_NpcBaseData.hide >= 1)
			SetVisible(true);

		taskeffect = MHSD_UTILS::GetEffectPath(10149);
	}
	break;
	case eNpcMissionBattle:
	{
		if (m_NpcBaseData.hide >= 1)
			SetVisible(true);

		taskeffect = MHSD_UTILS::GetEffectPath(10147);
	}
	break;
	case eNpcMissionCompleteQuest:
	{
		if (m_NpcBaseData.hide >= 1)
			SetVisible(true);

		taskeffect = MHSD_UTILS::GetEffectPath(10148);
	}
	break;
	case eNpcMissionNewQuest:
	{
		if (m_NpcBaseData.hide >= 1)
			SetVisible(true);

		taskeffect = MHSD_UTILS::GetEffectPath(10151);
	}
	break;
	case eNpcMissionMainQuest:
	{
		if (m_NpcBaseData.hide >= 1)
			SetVisible(true);

		taskeffect = MHSD_UTILS::GetEffectPath(10150);
	}
	break;
	default:
		break;
	}


	if (taskeffect.empty() == false)
	{
		if (!m_NpcBaseData.headtitle.empty() && taskeffect.find(m_NpcBaseData.headtitle) != std::wstring::npos)
			return;

		int yoffset = -(GetSprite()->GetBubbleItemOffset() == 0 ? DEFAULT_HPBAR_HEIGHT : GetSprite()->GetBubbleItemOffset());

		if (m_pTaskStateEffect)
		{
			RemoveDurativeEffect(m_pTaskStateEffect);
			m_pTaskStateEffect = NULL;
		}
		if (m_pTitleEffect)
		{
			m_pTaskStateEffect = SetDurativeEffect(taskeffect, 0, 0, yoffset*m_NpcBaseData.bodytype - 30, false, false);
		}
		else
		{
			m_pTaskStateEffect = SetDurativeEffect(taskeffect, 0, 0, yoffset*m_NpcBaseData.bodytype, false, false);
		}
	}
}
void Npc::SetShouXiNewShapeAndName(int modelid, std::wstring& strName, const std::map<char, int>& components)
{
	if (modelid == -1)
	{
		modelid = m_NpcBaseData.modelID;
		strName = m_NpcBaseData.name;
	}
	if (GetSprite())
	{
		ClearShouXiSpriteComponent();
		ChangeSpriteModel(modelid);
	}
	else
	{
		NewSpriteModel(modelid);
	}

	m_data.ShapeID = modelid;
	UpdateSpriteComponent(components);

	SetDefaultAction(eActionStand);
	if (m_data.strName != strName)
	{
		m_data.strName = strName;
		UpdatNameTexture(true);
	}

	SetQuestState(m_data.dQuestState);
}
Nuclear::NuclearLocation Npc::GetRandomVisitPosition()
{
	int nParam1 = 0;
	int nParam2 = 0;
	int nParam3 = 0;
	int nParam4 = 0;
	int randomradius = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithParamsData("getVisitNpcDis", nParam1, nParam2, nParam3, nParam4);

	Nuclear::NuclearLocation curloc = GetLogicLocation();
	return gGetScene()->GetNearByPosPreferY(curloc, randomradius, IsInHighLevel());	//npc访问格子定义为4-7个格子的距离
}
void Npc::SetFadeOut()
{
	m_bFadeOut = true;
}

void Npc::InitNameAndTitleColor()
{
	int baseID = m_data.NpcBaseID;
}
void Npc::BeginLeaveScene()
{
	if (m_iLeavingScene < 0)
	{
		if (m_NpcBaseData.hide >= 3)
		{
			m_iLeavingScene = 1000;
		}
	}
}
void Npc::Run(int now, int delta)
{
	SceneObject::Run(now, delta);

	if (m_iLeavingScene > 0)
	{
		m_iLeavingScene -= delta;
		if (m_iLeavingScene <= 0)
		{
			m_iLeavingScene = 0;
			SetFadeOut();
		}
	}
	if (m_bFadeOut)
	{
		unsigned char alpha = GetAlpha();
		if (alpha > 0)
		{
			if (alpha > delta / 6)
			{
				SetAlpha(alpha - delta / 6);
			}
			else
			{
				SetAlpha(0);
				m_bDisappear = true;
			}
		}
	}

	if (m_NpcBaseData.npctype != eNpcUndeadScene && m_NpcBaseData.npctype != eNpcUndeadSceneItem)
	{
		StandRandomAction(now, delta);
	}
}
void Npc::Draw(int now)
{
	if (IsVisiable() == false)
		return;

	Nuclear::NuclearLocation loc = GetLocation();
	Nuclear::NuclearRect vp = gGetScene()->GetViewport();
	if (!vp.PtInRect(loc))
		return;

	Nuclear::World* world = static_cast<Nuclear::World*>(Nuclear::GetEngine()->GetWorld());
	float fWorldScale = world->GetScale();

	if (m_NpcBaseData.foottitle.empty() == false)
	{
		DWORD color = m_TitleColor;
		DWORD shadowColor = m_TitleShadowColor;
		if (this == static_cast<Npc*>(gGetScene()->GetMouseOverObjects()))
		{
			color = m_MouseOverTitleColor;
			shadowColor = m_MouseOverTitleShadowColor;
		}

		Nuclear::IFontManager *pFontMan = Nuclear::GetEngine()->GetRenderer()->GetFontManager();

		if (pFontMan&&m_pTitleTextBlock&&m_TitleTextureHandle != Nuclear::INVALID_TEXTBLOCK_HANDLE)
		{
			color = 0xFFCCD50F;
			shadowColor = 0xff002b4d;
			m_pTitleTextBlock->SetFontColor(color, color);
			m_pTitleTextBlock->SetShadowColor(shadowColor);
			int height = m_pTitleTextBlock->GetTextHeight();
			int width = m_pTitleTextBlock->GetTextWidth();

			int left = loc.x * fWorldScale - gGetScene()->GetViewport().left * fWorldScale - width / 2;
			int top = loc.y * fWorldScale - gGetScene()->GetViewport().top * fWorldScale + DRAW_NAME_OFFSET + 15 * m_titleIndex++;

			pFontMan->DrawText(m_TitleTextureHandle, (float)left, (float)top, NULL);

		}
	}

	SceneObject::Draw(now);

}
void Npc::RandomWalk()
{
	if (m_NpcBaseData.npctype == eNpcTypeCreeps)
	{
		int xRandom = (rand() % 7 - 3)*g_logicGrid;
		int yRandom = (rand() % 7 - 3)*g_logicGrid;
		Nuclear::NuclearLocation randomtarget = m_ptInitLocation + Nuclear::NuclearLocation(xRandom, yRandom);
		if (!gGetScene()->IsMoveBlock(randomtarget, GetMoveMask()))
		{
			GetSprite()->SetMoveSpeed(80.f / 1000);
			MoveTo(randomtarget);
		}
	}
}
void Npc::UpdateAction(Nuclear::XPUSA_TYPE type)
{
	if (type == Nuclear::XPUSAT_MOVEINFLEXION)
	{
		if (rand() % 4 == 0)
		{
			RandomWalk();
		}
	}
	else if (type == Nuclear::XPUSAT_PLAYACTIONFINISH)
	{
		if (m_actionType == eActionStandRandom)
		{
			if (rand() % 2 == 0)
			{
				RandomWalk();
			}
		}
		else if (m_actionType == eActionMagic1)
		{
			if (m_iLeavingScene >= 0)
			{
				SetFadeOut();
			}
		}
	}
	SceneObject::UpdateAction(type);
}
void Npc::AddVisitEffect()
{
	if (!m_pVisitEffect)
	{
		m_pVisitEffect = SetDurativeEffect(MHSD_UTILS::GetEffectPath(10300), 0, 0, 0, false, false, false, false, true);
	}
}
void Npc::RemoveVisitEffect()
{
	if (m_pVisitEffect)
	{
		RemoveDurativeEffect(m_pVisitEffect);
		m_pVisitEffect = NULL;
	}
}
