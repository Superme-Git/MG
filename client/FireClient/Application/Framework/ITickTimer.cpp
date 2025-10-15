/********************************************************************
	filename: 	ITickTimer.cpp
	purpose:	
*********************************************************************/
#include "stdafx.h"
#include "ITickTimer.h"

ITickTimer::ITickTimer(const int& t)
	: m_Time(t)
	, m_bCancel(false)
	, m_Count(0)
{ }

ITickTimer::~ITickTimer()
{ }

void ITickTimer::CancelTimer()
{
	m_bCancel = true;
}

bool ITickTimer::OnTick(int now, int delta)
{
	if (m_bCancel) {
		return true;
	}

	m_Count += delta;

	if (m_Count > m_Time) {
		OnTimer();
		m_Count = 0;
	}

	return false;
}





