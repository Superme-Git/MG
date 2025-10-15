#pragma once
#include "Npc.h"

class FollowNpc : public Npc
{
public:
	FollowNpc();
	virtual ~FollowNpc();

public:
	bool Init(int nTableId, int index, const int& basedata);
	virtual	bool SetAttribute(stNpcData* pData);

	virtual void Run(int now,int delta);
	virtual void UpdateAction(Nuclear::XPUSA_TYPE type);
	virtual bool PlayAction(eActionType actionType, float freq);
	virtual void StopMove();

	int GetFollowNumber(){ return m_nNumber; }
	void SetFollowTarget(FollowNpc* pNpc){ m_pFollowerTarget = pNpc; }
	void RunFollow();

	void refreshDistance();

private:
	int m_nNumber;
	FollowNpc*			m_pFollowerTarget;

	int m_nMinFollow;
	int m_nMaxFollow;

};
