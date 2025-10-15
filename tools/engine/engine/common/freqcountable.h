#ifndef __Nuclear_COUNTABLE_H
#define __Nuclear_COUNTABLE_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "timer.h"

namespace Nuclear
{

	class FreqCountable : virtual public TimerOB
	{	
	public:
		FreqCountable() { FReset(); AttachTimer(); }
		virtual ~FreqCountable() { DetachTimer(); }

		// TimerOB 接口
		virtual void OnTimer() { ++m_uTick; m_uLatestCPS = m_uCount - m_uPrevTickCount; m_uPrevTickCount = m_uCount; }

	protected:
		void FReset() { m_uTick = 0; m_uCount = 0; m_uPrevTickCount = 0; m_uLatestCPS = 0; }
		void AddCount() { ++m_uCount; }

		DWORD GetCount() const { return m_uCount; }
		DWORD GetTick() const { return m_uTick; }

		float GetCPS() const { return m_uCount/((float)m_uTick + 0.00000001f); }
		unsigned int GetLatestCPS() const { return m_uLatestCPS; }
		// 成员
	private:
		unsigned int m_uTick;
		unsigned int m_uCount;

		unsigned int m_uPrevTickCount;
		unsigned int m_uLatestCPS;
	};
};

#endif