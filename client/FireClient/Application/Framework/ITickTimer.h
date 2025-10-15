/********************************************************************
	filename: 	ITickTimer.h
	purpose:	一个用Tick做的Timer，计时不是很准
*********************************************************************/


#pragma once

#include "ITickObject.h"

class ITickTimer : public ITickObject 
{
public:
	explicit ITickTimer(const int& t);
	virtual ~ITickTimer();

	virtual void OnTimer() = 0;

	void CancelTimer();

	//如果return false 那就继续下一次Tick，如果return ture，那就停止下一次tick
	bool OnTick(int now, int delta);


private:

	int m_Count;//计时器
	bool m_bCancel;
	int m_Time;//OnTimer触发的频率,毫秒级

};
