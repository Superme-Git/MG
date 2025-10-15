/********************************************************************
	filename: 	ITickObject.h
	purpose:	
*********************************************************************/

#pragma once

class ITickObject 
{
public:
	virtual ~ITickObject(){}

	//如果return false 那就继续下一次Tick，如果return ture，那就停止下一次tick
	virtual bool OnTick(int now, int delta) = 0;
};
