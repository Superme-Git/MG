#ifndef __Nuclear_COUNTABLE_H__
#define __Nuclear_COUNTABLE_H__

#include "timer.h"
#include "port.h"

namespace Nuclear
{
	class NuclearFreqCountable : public NuclearTimer
	{	
	public:
		NuclearFreqCountable() { FReset(); attachTimer(); }
		virtual ~NuclearFreqCountable() { detachTimer(); }

		virtual void OnTimer() { ++m_uTick; m_uLatestCPS = m_uCount - m_uPrevTickCount; m_uPrevTickCount = m_uCount; }

	protected:
		void FReset() { m_uTick = 0; m_uCount = 0; m_uPrevTickCount = 0; m_uLatestCPS = 0; }
		void AddCount() { ++m_uCount; }

		DWORD GetCount() const { return m_uCount; }
		DWORD GetTick() const { return m_uTick; }

		float GetCPS() const { return m_uCount/((float)m_uTick + 0.00000001f); }
		unsigned int GetLatestCPS() const { return m_uLatestCPS; }

	private:
		unsigned int m_uTick;
		unsigned int m_uCount;

		unsigned int m_uPrevTickCount;
		unsigned int m_uLatestCPS;
	};
};

#endif