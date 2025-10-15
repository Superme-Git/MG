#ifndef __BATTLEMAGICCONTROL_H
#define __BATTLEMAGICCONTROL_H


#include "BattleCommon.h"
#include "Singleton.hpp"

class Battler;
typedef std::vector<sBattlerData*>			NewBattlerList;

class BattleMagicControl : public CSingleton<BattleMagicControl>
{
public:
	BattleMagicControl();
	~BattleMagicControl();

	Battler*	FindBattlerByID(int battleID);
	int GetRoundNum();

	void AddNewBattler(bool bShow=true);
	
	void ShowNewBattler();		

	void DeleteRemoveBattler();		//让那些瞬时逃跑的单位消失

	//销毁所有魔法
	void DeleteAllBattleMagic();

	void AddNewBattlerData(sBattlerData* pBattlerData) { m_pNewBattleList.push_back(pBattlerData); }

private:
	NewBattlerList		m_pNewBattleList;
	std::list<int>		m_RemoveBattlerList;
};

inline BattleMagicControl* GetBattleMagicControl()
{
	return BattleMagicControl::GetInstance();
}
#endif
