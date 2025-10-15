#include "stdafx.h"
#include "UISprite.h"
#include "GameTable/item/CEquipEffect.h"
#include "GameUIManager.h"
#include "GameApplication.h"
#include "GameTable/item/CEquipEffect.h"
#include "engine/nuengine.h"
#include "sprite/nuspritemanager.h"
#include "CallLuaUtil.h"

stUISpriteHandle::stUISpriteHandle()
: handle(Nuclear::INVALID_ENGINE_SPRITE_HANDLE)
, baction(false)
{

}

#define RANDOMACTION_INTERVAL 10000
UISprite::UISprite(int modelid,bool haveshadow)
: m_SpriteHandle(Nuclear::INVALID_ENGINE_SPRITE_HANDLE)
, m_iModelID(modelid)
, m_iRandomActionInterval(RANDOMACTION_INTERVAL)
, m_bRandomAction(true)
, m_UILocation(0,0)
, m_UISpriteShadow(NULL)
, m_iWeaponColor(0)
, m_pWeaponColorParticle(NULL)
, m_pViceWeaponColorParticle(NULL)
, m_bShowBack(true)
, m_bShowFace1(true)
, m_bShowFace2(true)
, m_bShowHead(true)
, m_iActionID(0)
{
	int ShapeConfig_id = GetTableNumberFromLua("npc.cnpcshape", modelid, "id");
	if (ShapeConfig_id != -1)
	{
		std::wstring ShapeConfig_shape = GetTableWStringFromLua("npc.cnpcshape", modelid, "shape");

		m_SpriteHandle = Nuclear::GetEngine()->CreateEngineSprite(ShapeConfig_shape, true);
		
		SetUISpriteComponentByShapeID(modelid);

		//角色model设置向下
		if (IsCharacterModel())
		{
			Nuclear::GetEngine()->SetEngineSpriteDirection(m_SpriteHandle,Nuclear::XPDIR_BOTTOM);
		}
		else
		{
			Nuclear::GetEngine()->SetEngineSpriteDirection(m_SpriteHandle,Nuclear::XPDIR_BOTTOM);
		}

		SetDefaultAction(eActionStand);

		const Nuclear::Action *act = Nuclear::Engine::GetInstance().GetSpriteManager()->GetAction(ShapeConfig_shape, GetActionName(eActionStand));
		if (act)
		{
			m_CurActDuration = act->GetTime();
		}
	}
	if (gGetGameUIManager())
	{
		gGetGameUIManager()->AddUISprite(this);
	}
}

UISprite::~UISprite()
{
	ClearSpriteComponent();

	m_ComponentInfo.clear();

	if (m_UISpriteShadow)
	{
		RemoveEngineSpriteDurativeEffect(m_UISpriteShadow);
	}
	if (m_SpriteHandle)
	{
		Nuclear::GetEngine()->ReleaseEngineSprite(m_SpriteHandle);
		m_SpriteHandle = Nuclear::INVALID_ENGINE_SPRITE_HANDLE;
	}
	if (gGetGameUIManager())
	{
		gGetGameUIManager()->RemoveUISprite(this);
	}
}

Nuclear::EngineSpriteHandle UISprite::GetSprite()
{
	return m_SpriteHandle;
}

//装备颜色	1白色 2绿色 3蓝色 4紫色 5橙色 6暗金色
void UISprite::UpdateWeaponColorParticle(int weaponid)	//武器粒子效果
{
}

void UISprite::SetDyePartIndex(int part, int index)
{
	Nuclear::ISprite* pSprite = GetSpritePtr();
	if (pSprite != NULL)
	{
		if (index == 0)
			pSprite->SetDyePartIndex(part, index);
		else
		{
			int rIndex = cocos2d::CCScriptEngineManager::sharedManager()->getScriptEngine()->executeGlobalFunctionWithIntegerData("GetDyeIndex", index);
			pSprite->SetDyePartIndex(part, rIndex);
		}
	}
}

void UISprite::RemoveEngineSpriteDurativeEffect(Nuclear::IEffect*pEffect)
{
	if (m_SpriteHandle)
	{
		Nuclear::GetEngine()->RemoveEngineSpriteDurativeEffect(m_SpriteHandle, pEffect);
	}
}

Nuclear::IEffect* UISprite::SetEngineSpriteDurativeEffect(const std::wstring &effect_name, bool underSprite, unsigned int flag)
{
	if (m_SpriteHandle)
	{
		if (underSprite)
		{
			flag |= Nuclear::XPSPREFFFLAG_DRAW_BEFORE_SPRITE;
		}
		return Nuclear::GetEngine()->SetEngineSpriteDurativeEffect(m_SpriteHandle, effect_name, Nuclear::NuclearLocation(0, 0), flag);
	}
	else
		return NULL;
}

Nuclear::IEffect* UISprite::PlayUISpriteEffect(const std::wstring &effectname, int times, unsigned int flag)
{
	if (m_SpriteHandle)
	{
		return Nuclear::GetEngine()->PlayEngineSpriteEffect(m_SpriteHandle, effectname, 0, 0, times, flag, 0);
	}
	return NULL;
}

//设置精灵的换装不为，baseid为0代表是清除
void UISprite::SetSpriteComponent(eSpriteComponentType type, int baseid)
{
	ComponentInfoMap::iterator it = m_ComponentInfo.find(type);

	//首先判断是否是清除该部件
	if (baseid == 0)
	{
		DeleteComponent(type);
	}
	else
	{
		if (IsCharacterModel())
		{
			Nuclear::NuclearVector3 offset(0, 0, 0);
			int modelid = m_iModelID % 10;
			if (modelid == 0)
			{
				modelid = 10;
			}
			switch (type)
			{
			case eSprite_Weapon://武器
			{
				const GameTable::item::CEquipEffect& EquipConfig = GameTable::item::GetCEquipEffectTableInstance().getRecorder(baseid);
				if (EquipConfig.id != -1) {
					std::wstring resource = StringCover::intTowstring(EquipConfig.weaponid);
					Nuclear::GetEngine()->SetEngineSpriteComponent(m_SpriteHandle, 1, resource);

					int iLayerIndex = GetSpritePtr()->GetLayerIndexByName(L"effect");
					if (EquipConfig.weaponeffectid == 0)
					{
						Nuclear::GetEngine()->SetEngineSpriteComponent(m_SpriteHandle, iLayerIndex, L"");
					}
					else
					{
						std::wstring effectresource = StringCover::intTowstring(EquipConfig.weaponeffectid);
						Nuclear::GetEngine()->SetEngineSpriteComponent(m_SpriteHandle, iLayerIndex, effectresource);
					}
				}
			}
			break;
			case eSprite_Facedress1://面饰1
			case eSprite_Facedress2://面饰2
			case eSprite_Headdress://头饰
			case eSprite_Backdress://背饰
			{
				const GameTable::item::CEquipEffect& EquipConfig = GameTable::item::GetCEquipEffectTableInstance().getRecorder(baseid);
				if (!EquipConfig.modelpathleft.empty() && !EquipConfig.socketleft.empty())
				{
					if (EquipConfig.modelpathleft[0] != L"" && EquipConfig.socketleft[0] != L"")
					{
						std::map<eSpriteComponentType, stUISpriteHandle>::iterator iter = m_ComponentHandleMap.find(type);

						if (iter != m_ComponentHandleMap.end())
						{
							Nuclear::GetEngine()->ReleaseEngineSprite(iter->second.handle);
							iter->second.handle = Nuclear::INVALID_ENGINE_SPRITE_HANDLE;
							iter->second.baction = false;
						}
						//新建武器部件精灵
						m_ComponentHandleMap[type].handle = Nuclear::GetEngine()->CreateEngineSprite(EquipConfig.modelpathleft[0], true);

						if (EquipConfig.modelpathleft[0].find(L"3ds") != std::wstring::npos)
						{
							m_ComponentHandleMap[type].baction = true;
							Nuclear::GetEngine()->SetEngineSpriteDefaultAction(m_ComponentHandleMap[type].handle, GetComponentActionName(eActionStand));
						}
					}
				}
			}
			break;
			default:
				break;
			}
			if (50 <= type && type <= 59)	//染色
			{
				SetDyePartIndex(type - 50, baseid);
			}
		}
		if (it != m_ComponentInfo.end())
		{
			it->second = baseid;
		}
		else
		{
			m_ComponentInfo[type] = baseid;
		}

		//有可能武器变了，要重新设置SpriteDefaultAction
		if (type == eSprite_Weapon)
		{
			Nuclear::GetEngine()->SetEngineSpriteDefaultAction(m_SpriteHandle, GetActionName(eActionStand));
		}
	}
	refreshActionID();
}

//更新精灵换装components这个map仅仅是改变量
void UISprite::UpdateSpriteComponent(const ComponentInfoMap& components)
{
	ComponentInfoMap::const_iterator it = components.find(eSprite_WeaponColor);
	if (it != components.end())
	{
		m_iWeaponColor = it->second;
		//如果components信息里没有关于武器，而单单只是刷新了武器颜色，则这里要UpdateWeaponColorParticle
		if (components.find(eSprite_Weapon) == components.end())
		{
			if (m_ComponentHandleMap.find(eSprite_Weapon) != m_ComponentHandleMap.end()
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

	for (ComponentInfoMap::const_iterator iter = components.begin(); iter != components.end(); iter++)
	{
		if (iter->first != eSprite_WeaponColor && iter->first != eSprite_Fashion)
		{
			SetSpriteComponent(eSpriteComponentType(iter->first), iter->second);
		}

		//如果是武器颜色
		if (iter->first == eSprite_Weapon)
		{
			UpdateWeaponColorParticle(iter->second);
		}
	}

	SetDefaultAction(eActionStand);
}

//刷新精灵装备 是用components全部替换之前的components
void UISprite::RefreshSpriteComponent(const ComponentInfoMap& components)
{
	ComponentInfoMap oldcomponents = m_ComponentInfo;
	for (ComponentInfoMap::const_iterator it = oldcomponents.begin(); it != oldcomponents.end(); it++)
	{
		//如果原来m_ComponentInfo有这个装备，components现在没有，则删除
		ComponentInfoMap::const_iterator itor = components.find(it->first);
		if (itor == components.end())
		{
			if (it->first != eSprite_WeaponColor && it->first != eSprite_Fashion)
			{
				SetSpriteComponent(eSpriteComponentType(it->first), 0);
			}
		}
		//如果原来m_ComponentInfo有这个装备，现在的components也有但不同，则替换
		else if (itor->second != it->second)
		{
			if (itor->first != eSprite_WeaponColor && it->first != eSprite_Fashion)
			{
				SetSpriteComponent(eSpriteComponentType(itor->first), itor->second);
			}
		}
	}

	//如果原来有时装现在没有了，需要用components[eSprite_Fashion] = 0;
	ComponentInfoMap newcomponents = components;
	if (m_ComponentInfo.find(eSprite_Fashion) != m_ComponentInfo.end()
		&& newcomponents.find(eSprite_Fashion) == newcomponents.end())
	{
		newcomponents[eSprite_Fashion] = 0;
	}

	//然后用components去刷新
	UpdateSpriteComponent(newcomponents);
}

//删除某个部件
void UISprite::DeleteComponent(eSpriteComponentType type)
{
	if (eSprite_Weapon == type) {
		DeleteViceWeaponSprite();
	}
	else if (50 <= type && type <= 59)
		SetDyePartIndex(type - 50, 0);
}

void UISprite::RenderUISprite()
{
	Nuclear::GetEngine()->RendererEngineSprite(m_SpriteHandle);
}

//设置动作
void UISprite::PlayAction(eActionType actionType, float freq)
{
	Nuclear::GetEngine()->SetEngineSpriteAction(m_SpriteHandle, GetActionName(actionType));

	std::wstring actionname = GetComponentActionName(actionType);

	for (std::map<eSpriteComponentType, stUISpriteHandle>::iterator it = m_ComponentHandleMap.begin(); it != m_ComponentHandleMap.end(); it++)
	{
		if (it->first == eSprite_Backdress)
		{
			actionname = L"stand1";
		}
		if (it->second.baction)
		{
			Nuclear::GetEngine()->SetEngineSpriteAction(it->second.handle, actionname);
		}
	}
	if (Nuclear::INVALID_ENGINE_SPRITE_HANDLE != m_ViceWeaponHandle.handle
		&& m_ViceWeaponHandle.baction)
	{
		Nuclear::GetEngine()->SetEngineSpriteAction(m_ViceWeaponHandle.handle, actionname);
	}

	int ShapeConfig_id = GetTableNumberFromLua("npc.cnpcshape", m_iModelID, "id");
	if (ShapeConfig_id != -1)
	{
		std::wstring ShapeConfig_shape = GetTableWStringFromLua("npc.cnpcshape", m_iModelID, "shape");
		const Nuclear::Action *act = Nuclear::Engine::GetInstance().GetSpriteManager()->GetAction(ShapeConfig_shape, GetActionName(actionType));
		if (act)
		{
			m_CurActDuration = act->GetTime();
		}
	}
}

int UISprite::GetCurActDuration()
{
	return m_CurActDuration;
}

//设置默认动作
void UISprite::SetDefaultAction(eActionType actionType)
{
	std::wstring actionname = GetComponentActionName(actionType);
	Nuclear::GetEngine()->SetEngineSpriteDefaultAction(m_SpriteHandle, actionname);
}

int UISprite::GetModelID()
{
	return m_iModelID;
}

void UISprite::DeleteViceWeaponSprite()
{
	if (m_SpriteHandle != Nuclear::INVALID_ENGINE_SPRITE_HANDLE)
	{
		Nuclear::GetEngine()->SetEngineSpriteComponent(m_SpriteHandle, 1, L"");
		int iLayerIndex = GetSpritePtr()->GetLayerIndexByName(L"effect");
		GetSpritePtr()->SetComponent(iLayerIndex, L"");
	}
}

bool UISprite::IsCharacterModel()
{
	return m_iModelID < 1000 || m_iModelID>1000000;
}

void UISprite::SetModel(const int& modelid, bool haveshadow)
{
	//先删所有部件
	RemoveSpriteComponent();

	m_iModelID = modelid;

	int ShapeConfig_id = GetTableNumberFromLua("npc.cnpcshape", modelid, "id");
	if (ShapeConfig_id == -1)
	{
		return;
	}

	std::wstring ShapeConfig_shape = GetTableWStringFromLua("npc.cnpcshape", modelid, "shape");

	if (m_SpriteHandle != Nuclear::INVALID_ENGINE_SPRITE_HANDLE)
	{
		Nuclear::GetEngine()->SetEngineSpriteModel(m_SpriteHandle, ShapeConfig_shape, true);//异步加载
		//重新做动作
		SetDefaultAction(eActionStand);
	}
	else
	{
		m_SpriteHandle = Nuclear::GetEngine()->CreateEngineSprite(ShapeConfig_shape, true);
	}
	Nuclear::GetEngine()->SetEngineSpriteDirection(m_SpriteHandle, Nuclear::XPDIR_BOTTOM);


	std::wstring wsLayerName;
	std::wstring wsResource;
	int iLayerIndex = 0;

	wsLayerName = L"body";
	wsResource = L"bodyonly";

	iLayerIndex = GetSpritePtr()->GetLayerIndexByName(wsLayerName);
	GetSpritePtr()->SetComponent(iLayerIndex, wsResource, 0xffffffff, wsLayerName);

	wsLayerName = L"zhuangshi";
	wsResource = L"zhuangshi";
	iLayerIndex = GetSpritePtr()->GetLayerIndexByName(wsLayerName);
	GetSpritePtr()->SetComponent(iLayerIndex, wsResource, 0xffffffff, wsLayerName);

	refreshActionID();
	SetDefaultAction(eActionStand);

	const Nuclear::Action *act = Nuclear::Engine::GetInstance().GetSpriteManager()->GetAction(ShapeConfig_shape, GetActionName(eActionStand));
	if (act)
	{
		m_CurActDuration = act->GetTime();
	}
}

Nuclear::ISprite* UISprite::GetSpritePtr()
{
	Nuclear::Sprite *pSprite = Nuclear::GetEngine()->FindAndAddEngineSprite(GetSprite());
	return (Nuclear::ISprite*)pSprite;
}

std::wstring UISprite::GetComponentActionName(eActionType type)
{
	return gGetActionName(type,m_iActionID);
}

//如果是主角，则要加上主角的武器前缀
std::wstring UISprite::GetActionName(eActionType type)
{
	return gGetActionName(type,m_iActionID);
}
void UISprite::refreshActionID()
{
	m_iActionID = 0;
	int iWeaponID = 0;
	std::wstring ShapeConfig_shape = GetTableWStringFromLua("npc.cnpcshape", m_iModelID, "shape");
	ComponentInfoMap::const_iterator it = m_ComponentInfo.find(eSprite_Weapon);
	if (it != m_ComponentInfo.end())
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
//删除所有配件（主要是摆摊时候用）
void UISprite::ClearSpriteComponent()
{
	for (std::map<eSpriteComponentType, stUISpriteHandle>::iterator it = m_ComponentHandleMap.begin(); it != m_ComponentHandleMap.end(); it++)
	{
		//如果是武器，还要删除粒子特效
		if (it->first == eSprite_Weapon && m_pWeaponColorParticle)
		{
			m_pWeaponColorParticle = NULL;
		}
		if (it->second.handle != Nuclear::INVALID_ENGINE_SPRITE_HANDLE)
		{
			Nuclear::GetEngine()->ReleaseEngineSprite(it->second.handle);
		}
	}
	//双手武器时另外一个武器
	DeleteViceWeaponSprite();

	m_ComponentHandleMap.clear();
	refreshActionID();
}

//删除所有配件及m_ComponentInfo信息，创建角色界面用
void UISprite::RemoveSpriteComponent()
{
	ClearSpriteComponent();
	m_ComponentInfo.clear();
	refreshActionID();
}

//还原所有配件
void UISprite::ResetSpriteComponent()
{
	UpdateSpriteComponent(m_ComponentInfo);
	refreshActionID();
}

void UISprite::SetUIAlpha(unsigned char alpha)
{
	Nuclear::GetEngine()->SetEngineSpriteAlpha(m_SpriteHandle, alpha);
}

void UISprite::SetUIScale(const float& scale)
{
	Nuclear::GetEngine()->SetEngineSpriteScale(m_SpriteHandle, scale);
}

void UISprite::SetUIRotationSpeed(float speed)
{
}

void UISprite::SetUIDirection(const Nuclear::NuclearLocation& target)
{
	Nuclear::GetEngine()->SetEngineSpriteDirection(m_SpriteHandle, target);
}

//设置UI精灵的朝向
void UISprite::SetUIDirection(const Nuclear::NuclearDirection& dir)
{
	Nuclear::GetEngine()->SetEngineSpriteDirection(m_SpriteHandle, dir);
}

//设置UI精灵的位置
void UISprite::SetUILocation(const Nuclear::NuclearLocation& loc)
{
	Nuclear::GetEngine()->SetEngineSpriteLoc(m_SpriteHandle, loc);
	m_UILocation = loc;
}

void UISprite::SetUISpriteComponentByShapeID(const int& shapeid)
{
	std::wstring wsLayerName;
	std::wstring wsResource;
	int iLayerIndex = 0;

	wsLayerName = L"body";
	wsResource = L"bodyonly";

	iLayerIndex = GetSpritePtr()->GetLayerIndexByName(wsLayerName);
	GetSpritePtr()->SetComponent(iLayerIndex, wsResource, 0xffffffff, wsLayerName);

	wsLayerName = L"zhuangshi";
	wsResource = L"zhuangshi";
	iLayerIndex = GetSpritePtr()->GetLayerIndexByName(wsLayerName);
	GetSpritePtr()->SetComponent(iLayerIndex, wsResource, 0xffffffff, wsLayerName);

	refreshActionID();
	Nuclear::GetEngine()->SetEngineSpriteDirection(m_SpriteHandle, Nuclear::XPDIR_BOTTOM);
	SetDefaultAction(eActionStand);
}

const Nuclear::NuclearLocation& UISprite::GetUILocation()
{
	return m_UILocation;
}

void UISprite::SetRandomAction(bool b)
{
	m_bRandomAction = b;
}

void UISprite::Run(int now, int delta)
{
	m_iRandomActionInterval -= delta;
}
