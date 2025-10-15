#include "nueffect.h"

namespace Nuclear
{
	//只能由 EffectManager来构造
	Effect::Effect(EngineBase* pEB) : Renderable(pEB)
		, m_scale(1.0f, 1.0f)
		, m_rotationRadian(0.0f)
		, m_state(XPES_EMPTY)
		, m_bLookupSegmPak(false)
		, m_bVisiblity(true)
	{
	}

	Effect::~Effect()
	{
		NotifyDelete();
	}

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

	void Effect::Clone(Effect* p)
	{
		p->SetRelBouningBox(m_relBBox);
		p->SetName(m_name);
		p->SetPlayMode(m_playMode);
		p->SetLoopNum(GetLoopNum());
		p->SetLocation(m_pos);
		p->SetTranslation(m_translate);
		p->SetScale(m_scale);
		p->SetRotationRadian(m_rotationRadian);
		p->SetDirection(m_dir);
		p->SetPlayState(XPES_EMPTY);
		p->SetCurLoopNum(0);
		p->SetCurTime(-1);
		p->SetSegmPak(m_bLookupSegmPak);
	}
}