#ifndef __Nuclear_TIMER_H__
#define __Nuclear_TIMER_H__

#if _MSC_VER > 1000
#pragma once
#endif

namespace Nuclear
{
	class NuclearTimer
	{
	public:
		virtual ~NuclearTimer() {}
		virtual void OnTimer() = 0;

		static void OnSourceTimer();

	protected:

		void attachTimer();
		void detachTimer();
	};
}

#endif