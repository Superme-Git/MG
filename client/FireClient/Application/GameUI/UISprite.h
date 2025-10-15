#pragma once
#include "ComponentsObject.h"

struct stUISpriteHandle
{
	Nuclear::EngineSpriteHandle handle;
	bool	baction;
	stUISpriteHandle();
};

////可变装的精灵
class UISprite
{
public:
	UISprite(int model, bool haveshadow = false);
	virtual ~UISprite();

public:
	Nuclear::EngineSpriteHandle GetSprite();

	Nuclear::ISprite* GetSpritePtr();

	void DeleteComponent(eSpriteComponentType type);		//删除某个物件

	void DeleteViceWeaponSprite();
	bool IsCharacterModel();

public:
	void RenderUISprite();
	void SetDefaultAction(eActionType type);
	int	 GetModelID();
	void SetModel(const int& modelid, bool haveshadow = false);

	void SetUISpriteComponentByShapeID(const int& shapeid);
	const Nuclear::NuclearLocation& GetUILocation();

	void SetUIAlpha(unsigned char alpha);
	void SetUIScale(const float& scale);
	void SetUIDirection(const Nuclear::NuclearLocation& target);
	void SetUIDirection(const Nuclear::NuclearDirection& dir);
	void SetUIRotationSpeed(float speed);	//精灵转向速度
	void SetUILocation(const Nuclear::NuclearLocation& loc);

	void UpdateSpriteComponent(const ComponentInfoMap& components);//根据服务器发下来的换装map值刷新人物Components

	void RemoveSpriteComponent();					//删除所有配件及m_ComponentInfo信息，创建角色界面用
	void ClearSpriteComponent();					//删除所有配件（主要是摆摊时候用）
	void ResetSpriteComponent();					//还原所有配件（主要是摆摊时候用）

	void SetSpriteComponent(eSpriteComponentType type, int baseid);

	void RefreshSpriteComponent(const ComponentInfoMap& components);//刷新component，用于队伍界面更换时
	int GetCurActDuration();
public:
	std::wstring GetComponentActionName(eActionType type);
	std::wstring GetActionName(eActionType type);
	void PlayAction(eActionType actionType, float freq = 1.0f);

public:
	void SetRandomAction(bool b);
	void Run(int now, int delta);

public:
	//特效相关
	Nuclear::IEffect* SetEngineSpriteDurativeEffect(const std::wstring &effect_name, bool underSprite = false, unsigned int flag = Nuclear::XPSPREFFFLAG_ASYNC | Nuclear::XPSPREFFFLAG_NO_SOUND);
	Nuclear::IEffect* PlayUISpriteEffect(const std::wstring &effectname, int times = 1, unsigned int flag = Nuclear::XPSPREFFFLAG_ASYNC | Nuclear::XPSPREFFFLAG_NO_SOUND);
	void SetDyePartIndex(int part, int index);
	void RemoveEngineSpriteDurativeEffect(Nuclear::IEffect*pEffect);

private:
	void UpdateWeaponColorParticle(int weaponid);	//武器粒子效果
	void refreshActionID();
private:
	Nuclear::IEffect*	m_pViceWeaponColorParticle;
	Nuclear::IEffect*	m_pWeaponColorParticle;
	int	m_iWeaponColor;
	int m_iActionID;
private:
	Nuclear::NuclearLocation m_UILocation;
	int m_iModelID;
	Nuclear::EngineSpriteHandle m_SpriteHandle;

	int m_CurActDuration;					//当前动画的播放时间
	stUISpriteHandle m_ViceWeaponHandle;	//对于双手武器，该武器对应的是另外一个武器部件精灵
	std::map<eSpriteComponentType, stUISpriteHandle>	m_ComponentHandleMap;

	ComponentInfoMap m_ComponentInfo;

private:
	Nuclear::IEffect* m_UISpriteShadow;

private:
	bool m_bRandomAction;
	int	 m_iRandomActionInterval;

private:
	bool m_bShowHead;
	bool m_bShowFace2;
	bool m_bShowFace1;
	bool m_bShowBack;
};
