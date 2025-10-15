/***********************************************************************
filename: 	FossickNpc.h
purpose:	采集类npc
*************************************************************************/
#pragma once

#include "Npc.h"

class FossickNpc : public Npc
{
public:
	FossickNpc();
	virtual ~FossickNpc();

public:
	virtual	bool SetAttribute(stNpcData* pData);

	virtual void Run(int now,int delta);
	
	bool CanFossick();//是否可以采集
	void BeginFossick(const int& needbattle);
	void FinishOneFossick(const int& result);//完成一次采集
	void BeginCooling();//开始冷却
	bool IsInCoolState() {return m_bIsCooling;}//判断是否在冷却状态
	bool InCoolTips();
	void AddEffect();
	void RemoveEffect(int itemid);
	void EndCoolingState(int itemid);

private:
	int64_t m_iEndCoolingTime;//开始冷却时间
	bool m_bIsCooling;//是否在冷却状态
	Nuclear::IEffect*	m_pEffect;

};
