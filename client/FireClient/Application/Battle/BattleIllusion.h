#pragma once

#include "ComponentsObject.h"
#include "BattleCommon.h"

class Battler;

class IActionListener;

class BattleIllusion : public ComponentsObject
{
	int		m_iMoveOffsetX;
	int		m_iMoveOffsetY;
public:
	void	SetMoveOffset(int X, int Y)
	{
		m_iMoveOffsetX = X;
		m_iMoveOffsetY = Y;
	}
public:
	BattleIllusion();
	virtual ~BattleIllusion();
public:
	bool SetAttribute(sBattlerData data,float scale);

	bool IsDisappear(){return m_bDisappear;}

	void SetFadeOut();
	void SetFadeIn(unsigned char desalpha);
	void SetDisappear(){m_bDisappear = true;}

	void SetTargetLocation(Nuclear::NuclearLocation loc);

	void SetDisappearLocation(Nuclear::NuclearLocation loc){m_ptDisappearLocation = loc;}
	Nuclear::NuclearLocation GetTargetLocation(){return m_ptTargetLocation;}

	virtual std::wstring GetName(){return L"";}

	void SetDefaultAction(eActionType actionType,bool holdlastframe=false,float freq=g_defaultActionFrequence,eActionType actualActionType = eActionNull);
	void PlayAction(eActionType actionType,float freq=g_defaultActionFrequence,eActionType actualActionType = eActionNull);

	virtual int	 GetShapeID(){return m_data.iShapeID;}
	virtual void Run(int now,int delta);

	virtual void UpdateAction(Nuclear::XPUSA_TYPE type);
	virtual void Draw(int now);

	virtual bool OnSetCursor();

	virtual bool CanBeAttack();
	virtual bool CanBeGiven();
	virtual bool CanJoinTeam();
	virtual bool CanTradeWith();

	void SetOnHitTarget(bool b){m_bOnHitTarget = b;}
	bool IsOnHitTarget(){return m_bOnHitTarget;}

	void SetBattlerDemoSpeedRate(float rate){m_fDemoSpeedRate = rate;}

	void	SetJumpDelay(int jumpdelay){m_iZHDJumpDelay = jumpdelay;}

	void	SetNormalMagicIndex(BYTE index){m_iNormalMagicIndex = index;}

	BYTE	GetNormalMagicIndex(){return m_iNormalMagicIndex;}

	Battler* GetOwnerBattler();
	void SetOwnerBattler(Battler* OwnerBattler);
	void RunTo(Nuclear::NuclearLocation ptMoveTarget);	
	void RunBack();

	void TurnTo(SceneObject* pSceneObj);

	void RegistListener(IActionListener* aPListener);

	void UnregistListener(IActionListener* aPListener);

private:
	bool SetCharacter(int64_t characterID, const int&shapeID, const std::map<char, int>& components);
	bool SetCreeps(int64_t creepID, int shapeID);
	bool SetPet(int64_t petID, int shapeID);

private:
	sBattlerData		m_data;
	unsigned char		m_FadeInAlpha;
	Nuclear::NuclearLocation  m_ptTargetLocation;
	Nuclear::NuclearLocation  m_ptDisappearLocation;
	bool				m_bDisappear;

	bool				m_bFadeOut;

	bool				m_bFadeIn;

	bool m_bOnHitTarget;

	float m_fDemoSpeedRate;

	int		m_iZHDJumpDelay;
	BYTE	m_iNormalMagicIndex;

	std::vector <IActionListener*> m_Listeners;

	Battler* m_OwnerBattler;

};
