#ifndef __Nuclear_XP3DENGINE_H
#define __Nuclear_XP3DENGINE_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\common\xptypes.h"

namespace GM
{
	class GGame;
}


namespace Nuclear
{
	class MA_Impl;
	class Renderer;
	class EngineBase;

	class XP3DEngine
	{
	public:
		XP3DEngine(EngineBase *pEB);
		~XP3DEngine(void);

		bool Init(Renderer *pRenderer, HWND hwnd, bool bEnableMipMap);
		void Destroy();
		void SetCameraPos(float x, float y, float z);
		void Update(int delta);
		void SetMax3DEffectComponentCount(int count);
		int GetMax3DEffectComponentCount() const { return m_nMax3DEffectComponentCount; }
		void SetActionAudioVolume(float fVolume);
		void SetEffectAudioVolume(float fVolume);
		void SetEffectPriority(int nPriority);
		void OnWindowStateChange(XPFADE_SIGN state) { m_3dSoundFadeSign = state; }
		void SetFadeRate(float fFadefRate, bool force);
		bool IsInited() const { return m_bIsInited; }
	private:
		static void On3DAssertErr();
		float Get3DVolume(float oldVolume);
	private:
		MA_Impl *m_p3DImpl;
		GM::GGame *m_p3DGame;
		static EngineBase *s_pEB; //÷ªƒ‹static¡À°£°£°£

		bool m_bIsInited;
		int m_nMax3DEffectComponentCount;
		int m_nEffectPriority;
		float m_fActionAudioVolume;
		float m_fEffectAudioVolume;
		float m_f3dsoundFadefRate;
		XPFADE_SIGN m_3dSoundFadeSign;
	};

}

#endif //#ifndef __Nuclear_XP3DENGINE_H
