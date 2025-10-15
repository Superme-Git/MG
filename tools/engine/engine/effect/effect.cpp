#include "stdafx.h"
#include "effect.h"

namespace Nuclear
{
	//! 小于0表示无限循环
	bool Effect::SetLoopNum( int loopNum)
	{
		if (loopNum < 0)
		{
			m_playMode = XPPM_LOOP;
			m_nLoopNum = -1;
		} else {			
			m_playMode = XPPM_LOOPNUM;
			m_nLoopNum = loopNum;
		}
		return true;
	}

	bool Effect::SetPlayTime( DWORD time)
	{
		m_nPlayTime = time;
		m_playMode = XPPM_TIME;
		return true;
	}

	//播放状态控制
	bool Effect::Play()
	{
		if ( m_state != XPES_STOP)
		{
			return false;
		} else {
			m_state = XPES_PLAYING;
			m_curLoopNum = 0;
			m_nCurTime = -1;
			return true;
		}
	}

	bool Effect::Stop()
	{
		if ( m_state == XPES_PLAYING || m_state == XPES_PAUSE)
		{
			m_state = XPES_STOP;
			m_curLoopNum = 0;
			m_nCurTime = -1;
			NotifyStop();
			return true;
		} else {
			return false;
		}
	}

	bool Effect::Pause()
	{
		if ( m_state != XPES_PLAYING)
		{
			return false;
		} else {
			m_state = XPES_PAUSE;
			return true;
		}
	}

	bool Effect::Resume()
	{
		if ( m_state != XPES_PAUSE)
		{
			return false;
		} else {
			m_state = XPES_PLAYING;
			return true;
		}
	}

	void Effect::NotifyAsyncLoadResult(bool succ)
	{
		NotifySet::iterator notifyIt = m_SetPNotifies.begin(), notifyIe = m_SetPNotifies.end();
		for (;notifyIt!=notifyIe;)
		{
			(*(notifyIt++))->OnAsyncLoaded(this, succ);
		}
	}

	void Effect::NotifyStop()
	{
		NotifySet::iterator notifyIt = m_SetPNotifies.begin(), notifyIe = m_SetPNotifies.end();
		for (;notifyIt!=notifyIe;)
		{
			(*(notifyIt++))->OnStop(this);
		}
	}

	void Effect::NotifyEnd()
	{
		NotifySet::iterator notifyIt = m_SetPNotifies.begin(), notifyIe = m_SetPNotifies.end();
		for (;notifyIt!=notifyIe;)
		{
			(*(notifyIt++))->OnEnd(this);
		}
	}

	void Effect::NotifyDelete()
	{
		NotifySet::iterator notifyIt = m_SetPNotifies.begin(), notifyIe = m_SetPNotifies.end();
		for (;notifyIt!=notifyIe;)
		{
			(*(notifyIt++))->OnDelete(this);
		}
	}
}