#pragma once
#include "SceneObject.h"

enum eSpriteComponentType
{
	eSprite_Weapon		= 1,	//武器
	eSprite_Headdress	= 2,	//头饰
	eSprite_Backdress	= 3,	//背饰
	eSprite_Facedress1	= 4,	//面饰1
	eSprite_Facedress2	= 5,	//面饰2
	eSprite_Horse		= 6,	//坐骑
	eSprite_WeaponColor	= 7,	//武器颜色
	eSprite_Fashion = 8,	//时装id
	eSprite_DyePartA = 50,	//染色第一部分id
	eSprite_DyePartB = 51,	//染色第二部分id
	eSprite_Effect = 60, // 套装特效
};

class ComponentInfoMap : public std::map < char, int >
{
public:
	ComponentInfoMap()
		: std::map < char, int >()
	{}

	ComponentInfoMap(const std::map < char, int >& oth)
		: std::map < char, int >(oth)
	{}
};

////可变装的精灵
class ComponentsObject : public SceneObject
{
public:
	ComponentsObject();
	virtual ~ComponentsObject();

	void UpdateSpriteComponent(const ComponentInfoMap& components);//根据服务器发下来的换装map值刷新人物Components

	virtual void SetSpriteComponent(eSpriteComponentType type,int baseid,bool forcechange = false);
	int GetSpriteComponent(eSpriteComponentType type);
	
	void ResetSpriteComponent();					//还原所有配件（主要是摆摊时候用）
	void ClearSpriteComponent();					//删除所有配件（主要是摆摊时候用）
	
	void ClearShouXiSpriteComponent();				//清除所有配件，并且还原m_ComponentInfo，首席-弟子时用
	
	void AddEquipEffect(int effectId);
	
	void RemoveEquipEffect();

	void CheckEquipEffect(int color);

	const ComponentInfoMap& GetComponentInfo(){ return m_ComponentInfo; }

	virtual bool CanBeAttack(){return false;}
	virtual bool CanBeGiven(){return false;}
	virtual bool CanJoinTeam(){return false;}
	virtual bool CanTradeWith(){return false;}
	virtual bool CanGiveFlower(){return false;}
	virtual bool CanGiveRose() { return false;}
	virtual bool CanBeUseSkill(int skillid){return false;}
	virtual bool CanPickedUp(){return false;}
	virtual bool CanBeVisited(){return false;}
	virtual int CanBeTriggered(){return -1;}

	virtual bool CanFBAttack(){return false;}
	virtual bool CanFBRepair(){return false;}

	virtual bool PlayAction(eActionType actionType,float freq =g_defaultActionFrequence);
	
	virtual std::wstring GetActionName(eActionType type);

	virtual void ChangeSpriteModel(const int& modelid);//换造型

	void UpdateWeaponColorParticle(int weaponid);	//武器粒子效果

	bool m_IsMainCharacter;

	void SetRideEffect(int effectId, int dx, int dy);
	void RemoveRideEffect();
	virtual bool IsCanUseComponentsAfterChangeModel() { return false; }//是否可以在变身后 使用原有装备和染色信息
private:

	void deleteComponent(eSpriteComponentType type);		//删除某个物件

	void deleteViceWeaponSprite();
	void refreshActionID();

private:
	int	m_iWeaponColor;
	Nuclear::IEffect*	m_pWeaponColorParticle;
	Nuclear::IEffect*	m_pViceWeaponColorParticle;

	Nuclear::ISprite*		m_ViceWeaponSprite;		//对于双手武器，该武器对应的是另外一个武器部件精灵
	std::map<eSpriteComponentType, Nuclear::ISprite*>	m_ComponentMap;

	ComponentInfoMap	m_ComponentInfo;

	Nuclear::IEffect*	m_pEquipEffect;

	int quality;
	int m_iActionID;

	Nuclear::IEffect* m_pRideEffect;  // 坐骑特效
};
