/***********************************************************************
filename: 	ScenePet.h
purpose:	宠物对象接口
*************************************************************************/
#pragma once

#include "ComponentsObject.h"
#include "../ProtoDef/rpcgen/fire/pb/Pet.hpp"


class Character;

class ScenePet : public ComponentsObject
{
public:
	ScenePet();
	virtual ~ScenePet();
public:

	virtual	bool SetAttribute(const stMapPetData& data);

	virtual void StopMove();
	virtual void Run(int now,int delta);
	void	RunRandom();
	void	RunFollow(bool forcemove = false);
	void	RandomTalk();
	int		GetBaseID() { return m_mapData.showpetid;}
	virtual void UpdateAction(Nuclear::XPUSA_TYPE type);

	const stMapPetData& GetConstPetData() { return m_mapData;}

	stMapPetData&	GetPetData() { return m_mapData;}
	void ChangeName(std::wstring name){m_mapData.showpetname = name; UpdatNameTexture(true);}
	virtual std::wstring GetName(){return m_mapData.showpetname;}

	virtual bool CanBeAttack();

	void SetArea1Color(int color);
	void SetArea2Color(int color);

private:
	enum ePetMoveState
	{
		ePetMove_Null,
		ePetMove_Follow,
		ePetMove_Random,
		ePetMove_Stop
	};
	unsigned int	m_iMoveStopTime;
	stMapPetData	m_mapData;

	Character*		m_pMaster; //主人

	ePetMoveState	m_ePetMoveState;

	int				m_iTalkInterval;

	Nuclear::IEffect*	m_pShowEffect;	//展示的特效
};
