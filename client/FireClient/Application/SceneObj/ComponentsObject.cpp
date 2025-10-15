#include "stdafx.h"
#include "ComponentsObject.h"
#include "GameTable/item/CEquipEffect.h"
#include "MainCharacter.h"
#include "CallLuaUtil.h"
#include "engine/nuengine.h"
#include "sprite/nuspritemanager.h"

ComponentsObject::ComponentsObject()
: m_ViceWeaponSprite(NULL)
, m_pWeaponColorParticle(NULL)
, m_pViceWeaponColorParticle(NULL)
, m_iWeaponColor(0)
, m_pEquipEffect(NULL)
, quality(0)
, m_iActionID(0)
, m_IsMainCharacter(false)
, m_pRideEffect(NULL)
{

}
ComponentsObject::~ComponentsObject()
{ 
	ClearSpriteComponent();

	m_ComponentInfo.clear();
}

void ComponentsObject::deleteViceWeaponSprite()
{
	if (m_ViceWeaponSprite)
	{
		//同时要删除粒子特效
		if (m_pViceWeaponColorParticle)
		{
			m_ViceWeaponSprite->RemoveDurativeEffect(m_pViceWeaponColorParticle);
			m_pViceWeaponColorParticle = NULL;
		}
		if (m_eSceneType == eSceneObjBattler || m_eSceneType == eSceneObjBattleIllusion)
		{
			Nuclear::GetEngine()->GetWorld()->DetachSprite(Nuclear::XPSL_BATTLE, GetSprite(), m_ViceWeaponSprite);
			Nuclear::GetEngine()->GetWorld()->DeleteSprite(m_ViceWeaponSprite);
		}
		else
		{
			Nuclear::GetEngine()->GetWorld()->DetachSprite(IsInHighLevel() ? Nuclear::XPSL_MIDDLE2 : Nuclear::XPSL_MIDDLE1, GetSprite(), m_ViceWeaponSprite);
			Nuclear::GetEngine()->GetWorld()->DeleteSprite(m_ViceWeaponSprite);
		}

		m_ViceWeaponSprite = NULL;
	}
}
void ComponentsObject::deleteComponent(eSpriteComponentType type)
{
    if (eSprite_Weapon == type)
    {
        deleteViceWeaponSprite();

		std::wstring wsLayerName;
		int iLayerIndex = 0;
		wsLayerName = L"weapon";
		if (GetSprite())
		{
			iLayerIndex = GetSprite()->GetLayerIndexByName(wsLayerName);
			GetSprite()->SetComponent(iLayerIndex, L"");

			wsLayerName = L"effect";
			iLayerIndex = GetSprite()->GetLayerIndexByName(wsLayerName);
			GetSprite()->SetComponent(iLayerIndex, L"");
		}
	}
	else if (50 <= type && type <= 59)
	{
		if(IsCharacterModel() && (IsCanUseComponentsAfterChangeModel() || m_iModelID == GetShapeID()))
			SetDyePartIndex(type - 50, 0);
	}
}

int ComponentsObject::GetSpriteComponent(eSpriteComponentType type)
{
	ComponentInfoMap::iterator it=m_ComponentInfo.find(type);
	if (it != m_ComponentInfo.end())
		return it->second;
	return 0;
}
void ComponentsObject::SetSpriteComponent(eSpriteComponentType type,int baseid,bool forcechange)
{
	ComponentInfoMap::iterator it=m_ComponentInfo.find(type);
	if (it != m_ComponentInfo.end() && it->second == baseid
	 || it == m_ComponentInfo.end() && baseid == 0) 
	{
		if (!forcechange)
			return;
	}
	if (baseid == 0)
	{
		if (IsCharacterModel())
		{
			deleteComponent(type);
		}
		//更新m_ComponentInfo信息
		if (it != m_ComponentInfo.end())
		{
			// reset horse
			if (type == eSprite_Horse)
			{
				GetSprite()->SetRideName(L"");
			}
            m_ComponentInfo.erase(it);
			if (type == eSprite_Horse)
			{
				if (m_IsMainCharacter)
				{
					GetMainCharacter()->UpdateSpeed();
				}
				PlayAction(m_actionType);
			}
		}
	}
	else
	{
		if (IsCharacterModel())
		{
			//偏移量
			Nuclear::NuclearVector3 offset(0,0,0);
			int modelid = m_iModelID%10;
			if (modelid == 0)
			{
				modelid = 10;
			}
			switch(type)
			{
			case eSprite_Weapon://武器
				{
                    const GameTable::item::CEquipEffect& effect = GameTable::item::GetCEquipEffectTableInstance().getRecorder(baseid);
                    if (effect.id != -1)
					{
						std::wstring wsResource = StringCover::intTowstring(effect.weaponid);
						std::wstring wsLayerName;
						int iLayerIndex = 0;

						wsLayerName = L"weapon";
						iLayerIndex = GetSprite()->GetLayerIndexByName(wsLayerName);
						GetSprite()->SetComponent(iLayerIndex, wsResource);
						if (effect.weaponeffectid != 0)
						{
							wsResource = StringCover::intTowstring(effect.weaponeffectid);
							wsLayerName = L"effect";
							iLayerIndex = GetSprite()->GetLayerIndexByName(wsLayerName);
							GetSprite()->SetComponent(iLayerIndex, wsResource);
						}
						else
						{
							wsLayerName = L"effect";
							iLayerIndex = GetSprite()->GetLayerIndexByName(wsLayerName);
							GetSprite()->SetComponent(iLayerIndex, L"");
						}
					}
					refreshActionID();
				}
				break;
			case eSprite_Facedress1://面饰1
			case eSprite_Facedress2://面饰2
			case eSprite_Headdress://头饰
			case eSprite_Backdress://背饰
				{
					//配饰的偏移
					const GameTable::item::CEquipEffect& EquipConfig = GameTable::item::GetCEquipEffectTableInstance().getRecorder(baseid);
					if (!EquipConfig.modelpathleft.empty() && !EquipConfig.socketleft.empty())
					{
						if (EquipConfig.modelpathleft[0] != L"" && EquipConfig.socketleft[0] != L"")
						{
							std::map<eSpriteComponentType,Nuclear::ISprite*>::iterator it=m_ComponentMap.find(type);
                            
							if (it != m_ComponentMap.end())
							{
								it->second->SetModel(EquipConfig.modelpathleft[0]);
								Nuclear::GetEngine()->GetWorld()->DetachSprite(Nuclear::XPSL_BATTLE,GetSprite(),it->second);
								if (eSprite_Backdress == type)
								{
									Nuclear::GetEngine()->GetWorld()->AttachSprite(GetSprite(),it->second,L"HS_back",EquipConfig.socketleft[0],offset,0);
								}
								else
								{
									Nuclear::GetEngine()->GetWorld()->AttachSprite(GetSprite(),it->second,L"HS_face",EquipConfig.socketleft[0],offset,0);
								}
							}
							else
							{	
								if (m_eSceneType == eSceneObjBattler || m_eSceneType == eSceneObjBattleIllusion)
								{
									m_ComponentMap[type] = Nuclear::GetEngine()->GetWorld()->NewSprite(Nuclear::XPSL_BATTLE,EquipConfig.modelpathleft[0]);
								}
								else
								{
									m_ComponentMap[type] = Nuclear::GetEngine()->GetWorld()->NewSprite(IsInHighLevel()?Nuclear::XPSL_MIDDLE2:Nuclear::XPSL_MIDDLE1,EquipConfig.modelpathleft[0]);
								}
								if (eSprite_Backdress == type)
								{
									Nuclear::GetEngine()->GetWorld()->AttachSprite(GetSprite(),m_ComponentMap[type],L"HS_back",EquipConfig.socketleft[0],offset,0);
								}
								else
								{
									Nuclear::GetEngine()->GetWorld()->AttachSprite(GetSprite(),m_ComponentMap[type],L"HS_face",EquipConfig.socketleft[0],offset,0);
								}
							}
						}
					}
				}
				break;
			case eSprite_Horse:
				{
					//set horse component. by liugeng
					std::wstring resource = StringCover::intTowstring(baseid);
					GetSprite()->SetRideName(resource);
					
					// set horse effect. (effectId get from table npc.cride by calling lua function)
					int rideEffectId = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("GetRideEffectId", baseid);
					if (-1 == rideEffectId)
					{
						RemoveRideEffect();
					}
					else
					{
						// effect bind pos get from XModel, not from Sprite
						Nuclear::NuclearPoint* pEffPos = NULL;
						CallLuaUtil luaUtil;
						luaUtil.addArg(baseid);
						if (luaUtil.callLua("GetRideEffectPos"))
						{
							pEffPos = (Nuclear::NuclearPoint*)luaUtil.getRetUsertype("Nuclear::NuclearPoint");
						}
						int dx = pEffPos ? pEffPos->x : 0;
						int dy = pEffPos? pEffPos->y: 0;
						SetRideEffect(rideEffectId, dx, dy);
					}
				}
				break;
			default:
				break;
			}

			if (50 <= type && type <= 59)	//染色
			{
				if(IsCharacterModel() && (IsCanUseComponentsAfterChangeModel() || m_iModelID == GetShapeID()))
					SetDyePartIndex(type - 50, baseid);
			}
		}

		//更新m_ComponentInfo信息
		if (it != m_ComponentInfo.end())
		{
			it->second = baseid;
		}
		else
		{
			m_ComponentInfo[type] = baseid;
		}
		if (type == eSprite_Horse)
		{
			if (m_IsMainCharacter)
			{
				GetMainCharacter()->UpdateSpeed();
			}
		}
	}
}

std::wstring ComponentsObject::GetActionName(eActionType type)
{
	if (m_ComponentInfo.find(eSprite_Horse) != m_ComponentInfo.end())
		return gGetActionName(type,m_iActionID,true);
	return gGetActionName(type,m_iActionID);
}
void ComponentsObject::refreshActionID()
{
	m_iActionID = 0;
	int iWeaponID = 0;	
	std::wstring ShapeConfig_shape = GetTableWStringFromLua("npc.cnpcshape", m_iModelID, "shape");
	ComponentInfoMap::const_iterator it = m_ComponentInfo.find(eSprite_Weapon);
	if(it != m_ComponentInfo.end())
	{
		const GameTable::item::CEquipEffect& effect = GameTable::item::GetCEquipEffectTableInstance().getRecorder(it->second);
		if (effect.id != -1)
			iWeaponID = int(effect.weaponid / 100);
	}

	CallLuaUtil util;
	util.addArg(ws2s(ShapeConfig_shape).c_str());
	util.addArg(iWeaponID);
	if (util.callLua("GetActionID"))
	{
		int stageID = util.getRetInt();
		if (stageID != -1)
		{
			m_iActionID = stageID;
		}
	}
}

void ComponentsObject::ResetSpriteComponent()
{
	for (ComponentInfoMap::const_iterator it=m_ComponentInfo.begin();it!=m_ComponentInfo.end();it++)
	{
		if (it->first != eSprite_WeaponColor && it->first != eSprite_Fashion)
		{
			SetSpriteComponent(eSpriteComponentType(it->first),it->second,true);
		}
		if (it->first == eSprite_Weapon)
		{
			UpdateWeaponColorParticle(it->second);
		}
	}
	refreshActionID();
	SetDefaultAction(GetActionType());
}
void ComponentsObject::ClearShouXiSpriteComponent()
{
	ClearSpriteComponent();
	m_ComponentInfo.clear();
}
void ComponentsObject::ClearSpriteComponent()
{
	for (std::map<eSpriteComponentType,Nuclear::ISprite*>::iterator it=m_ComponentMap.begin();it!=m_ComponentMap.end();it++)
	{
		if (it->second)
		{
			if (it->first == eSprite_Weapon && m_pWeaponColorParticle)
			{
				it->second->RemoveDurativeEffect(m_pWeaponColorParticle);
				m_pWeaponColorParticle = NULL;
			}

			if (m_eSceneType == eSceneObjBattler || m_eSceneType == eSceneObjBattleIllusion)
			{
				Nuclear::GetEngine()->GetWorld()->DetachSprite(Nuclear::XPSL_BATTLE,GetSprite(),it->second);
				Nuclear::GetEngine()->GetWorld()->DeleteSprite(it->second);
			}
			else
			{
				Nuclear::GetEngine()->GetWorld()->DetachSprite(IsInHighLevel()?Nuclear::XPSL_MIDDLE2:Nuclear::XPSL_MIDDLE1,GetSprite(),it->second);
				Nuclear::GetEngine()->GetWorld()->DeleteSprite(it->second);
			}
		}
	}
	deleteViceWeaponSprite();
	m_ComponentMap.clear();
	refreshActionID();
}
void ComponentsObject::ChangeSpriteModel(const int& modelid)
{
	if (m_iModelID != modelid)
	{
		{
			m_iModelID = modelid;
			if (GetSprite())
			{
				ClearSpriteComponent();	//换model的时候首先就要清空部件
				SceneObject::ChangeSpriteModel(modelid);

				if (IsCharacterModel() && (IsCanUseComponentsAfterChangeModel() || m_iModelID == GetShapeID()))
				{
					ResetSpriteComponent();
				}
			}
			else
			{
				NewSpriteModel(modelid);
			}
		}
	}
	refreshActionID();
}
bool ComponentsObject::PlayAction(eActionType actionType,float freq)
{
	return SceneObject::PlayAction(actionType,freq);
}
void ComponentsObject::AddEquipEffect(int effectId)
{
	if (!m_pEquipEffect)
	{
		m_pEquipEffect = SetDurativeEffect(MHSD_UTILS::GetEffectPath(effectId), 0, 0, 0, false, false, false, false, false);
	}
}
void ComponentsObject::RemoveEquipEffect()
{
	if (m_pEquipEffect)
	{
		RemoveDurativeEffect(m_pEquipEffect);
		m_pEquipEffect = NULL;
	}
}

void ComponentsObject::SetRideEffect(int effectId, int dx, int dy)
{
	if (m_pRideEffect)
	{
		RemoveRideEffect();
	}

	const std::wstring& effectPath = MHSD_UTILS::GetEffectPath(effectId);
	m_pRideEffect = SetDurativeEffect(effectPath, Nuclear::XPSPREFFFLAG_BIND_EFF_POS_WITH_DIR, dx, dy, false);
}

void ComponentsObject::RemoveRideEffect()
{
	if (m_pRideEffect)
	{
		RemoveDurativeEffect(m_pRideEffect);
		m_pRideEffect = NULL;
	}
}

void ComponentsObject::CheckEquipEffect(int color)
{
	if (color != 0)
	{
		if (color != quality)
		{
			RemoveEquipEffect();
			quality = color;
			int effectId = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("EquipEffect_GetID",quality);
			if (effectId != 0)
			{
				AddEquipEffect(effectId);
			}
		}
	}
	else
	{
		RemoveEquipEffect();
		quality = 0;
	}
}
void ComponentsObject::UpdateSpriteComponent(const ComponentInfoMap& components)
{
	bool bOldFoundHorse = (m_ComponentInfo.find(eSprite_Horse) != m_ComponentInfo.end());

	ComponentInfoMap::const_iterator it=components.find(eSprite_WeaponColor);
	if (it != components.end())
	{
		m_iWeaponColor = it->second;
		if (components.find(eSprite_Weapon) == components.end())
		{
			if (m_ComponentMap.find(eSprite_Weapon) != m_ComponentMap.end()
			 && m_ComponentInfo.find(eSprite_Weapon) != m_ComponentInfo.end())
			{
				UpdateWeaponColorParticle(m_ComponentInfo[eSprite_Weapon]);
			}
		}
		m_ComponentInfo[eSprite_WeaponColor] = m_iWeaponColor;
	}
	else
	{
		ComponentInfoMap::iterator itor = m_ComponentInfo.find(eSprite_WeaponColor);
		if (itor != m_ComponentInfo.end())
		{
			m_ComponentInfo.erase(itor);
		}
	}
	
	for (ComponentInfoMap::const_iterator it=components.begin();it!=components.end();it++)
	{
		if (it->first != eSprite_WeaponColor && it->first != eSprite_Fashion)
		{
			SetSpriteComponent(eSpriteComponentType(it->first),it->second, true);
		}
		if (it->first == eSprite_Weapon)
		{
			UpdateWeaponColorParticle(it->second);
		}
	}

	it=components.find(eSprite_Fashion);
	if (it != components.end())
	{
		if (it->second != 0)
		{
			m_ComponentInfo[eSprite_Fashion] = it->second;
		}
		else
		{
			ComponentInfoMap::iterator itor=m_ComponentInfo.find(eSprite_Fashion);
			if (itor != m_ComponentInfo.end())
			{
				m_ComponentInfo.erase(itor);
			}
		}
	}
	refreshActionID();

	bool bNewFoundHorse = (m_ComponentInfo.find(eSprite_Horse) != m_ComponentInfo.end());
	if (bOldFoundHorse != bNewFoundHorse)
	{
		UpdateAction(Nuclear::XPUSAT_PLAYACTIONFINISH);
	}
}
void ComponentsObject::UpdateWeaponColorParticle(int weaponid)	
{
	if (IsCharacterModel())
	{
		if (m_iWeaponColor >= 4)
		{
			std::map<eSpriteComponentType, Nuclear::ISprite*>::const_iterator itor = m_ComponentMap.find(eSprite_Weapon);
			if (itor != m_ComponentMap.end())
			{
				if (m_pWeaponColorParticle)
				{
					itor->second->RemoveDurativeEffect(m_pWeaponColorParticle);
					m_pWeaponColorParticle = NULL;
				}
				if (m_pViceWeaponColorParticle && m_ViceWeaponSprite)
				{
					m_ViceWeaponSprite->RemoveDurativeEffect(m_pViceWeaponColorParticle);
					m_pViceWeaponColorParticle = NULL;
				}
			}
		}
		else
		{
			std::map<eSpriteComponentType,Nuclear::ISprite*>::const_iterator itor=m_ComponentMap.find(eSprite_Weapon);
			if (itor != m_ComponentMap.end() && m_pWeaponColorParticle)
			{
				itor->second->RemoveDurativeEffect(m_pWeaponColorParticle);
				m_pWeaponColorParticle = NULL;
			}
			if (m_ViceWeaponSprite && m_pViceWeaponColorParticle)
			{
				m_ViceWeaponSprite->RemoveDurativeEffect(m_pViceWeaponColorParticle);
				m_pViceWeaponColorParticle = NULL;
			}
		}
	}
}
