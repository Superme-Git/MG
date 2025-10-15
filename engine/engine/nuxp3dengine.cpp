#include "stdafx.h"
#include "..\common\3dengine.h"
#include "..\common\log.h"
#include "..\common\xptimeuselog.h"
#include "..\renderer\renderer.h"
#include "..\iapp.h"
#include "xp3dengine.h"
#include "enginebase.h"

namespace Nuclear
{

	EngineBase* XP3DEngine::s_pEB = NULL;
	extern XPTimeUseLog g_timeuserlog;

	class MA_Impl : public GM::MA_Interface
	{
		EngineBase *m_pEB;
		DWORD m_lastTick;
	public:
		MA_Impl(EngineBase *pEB) : m_pEB(pEB), m_lastTick(pEB->GetTick()) {}

		void MA_Initialize(GM::IGameFramework* framework) {}
		void MA_Exit() {}
		void MA_MainProc() 
		{
			g_timeuserlog.Push(XPTimeUseLog::TUL_3DBEFOREUPDATENODE);
			m_pEB->UpdateSpriteAction(m_pEB->GetTick() - m_lastTick);
			m_lastTick = m_pEB->GetTick();
			g_timeuserlog.Push(XPTimeUseLog::TUL_UPDATESPRITEACTION);
		}
		bool MA_HandleWinMsg(HWND hWnd, UINT uMsg, WPARAM wParam, LPARAM lParam) { return false; }
		void MA_DrawUI() {}
	};

	XP3DEngine::XP3DEngine(EngineBase *pEB) : m_p3DImpl(NULL), m_p3DGame(NULL), m_bIsInited(false), 
		m_nMax3DEffectComponentCount(15), m_nEffectPriority(2), m_fActionAudioVolume(1.0f), 
		m_fEffectAudioVolume(1.0f), m_3dSoundFadeSign(XPFS_NORMAL), m_f3dsoundFadefRate(1.0f)
	{
		s_pEB = pEB;
	}

	XP3DEngine::~XP3DEngine(void)
	{
	}

	void XP3DEngine::SetCameraPos(float x, float y, float z)
	{
		try {
			m_p3DGame->NuclearSetCameraPos(GM::GVector3(x, y, z));
		} catch (ModMud::MAssertException &)
		{
			if (s_pEB && s_pEB->GetApp())
			{
				s_pEB->GetApp()->On3DAssertErr();
			}
		}
	}

	void XP3DEngine::Update(int delta)
	{
		try {
			m_p3DGame->NuclearUpdate(delta / 1000.0f);
			g_timeuserlog.Push(XPTimeUseLog::TUL_3DAFTERUPDATENODE);
		} catch (ModMud::MAssertException &)
		{
			if (s_pEB && s_pEB->GetApp())
			{
				s_pEB->GetApp()->On3DAssertErr();
			}
		}
	}

	void XP3DEngine::On3DAssertErr()
	{
		if (s_pEB && s_pEB->GetApp())
		{
			s_pEB->GetApp()->On3DAssertErr();
		}
	}

	bool XP3DEngine::Init(Renderer *pRenderer, HWND hwnd, bool bEnableMipMap)
	{
		m_bIsInited = false;
		try {
			::GM::GNuclear::SetRenderSystem(pRenderer->GetMMRSI());

			m_p3DImpl = new MA_Impl(s_pEB);

			GM::GGlobal::Create();
			GM::GGlobal::Initialize(GM::EFS_NATIVE, L"", L"", std::vector<GM::GString>());

			m_p3DGame = GM::GGame::GetGame();
			bool bres = m_p3DGame->Create(m_p3DImpl, hwnd, false);
			if(!bres)
			{
				XPLOG_ERROR(L"3D渲染器创建失败");
				return false;
			}
			GM::GConfig g3Dcfg = m_p3DGame->GetConfig();
			g3Dcfg.enableLowTimeAccuracy = true;
			g3Dcfg.maxEffectComponentCount = m_nMax3DEffectComponentCount;
			g3Dcfg.use3DAudio = false;
			if (g3Dcfg.enableMipMap != bEnableMipMap)
			{
				g3Dcfg.enableMipMap = bEnableMipMap;
			}
			m_p3DGame->SetConfig(g3Dcfg);
			m_p3DGame->SetAssertCB(&XP3DEngine::On3DAssertErr);
			m_bIsInited = true;
		} catch (ModMud::MAssertException &)
		{
			if (s_pEB && s_pEB->GetApp())
			{
				s_pEB->GetApp()->On3DAssertErr();
			}
		}
		return true;
	}

	void XP3DEngine::Destroy()
	{
		try{
			if (m_p3DGame)
			{
				m_p3DGame->Destroy();
				m_p3DGame = NULL;
			}
			GM::GGlobal::Finalize();
			GM::GGlobal::Destroy();
		} catch (ModMud::MAssertException &)
		{
			if (s_pEB && s_pEB->GetApp())
			{
				s_pEB->GetApp()->On3DAssertErr();
			}
		}
		XPSAFE_DELETE(m_p3DImpl);
	}

	//3D特效组件数，默认15个
	void XP3DEngine::SetMax3DEffectComponentCount(int count)
	{
		if (m_nMax3DEffectComponentCount != count)
		{
			m_nMax3DEffectComponentCount = count;
			if (m_p3DGame)
			{
				GM::GConfig g3Dcfg = m_p3DGame->GetConfig();
				g3Dcfg.maxEffectComponentCount = m_nMax3DEffectComponentCount;
				m_p3DGame->SetConfig(g3Dcfg);
			}
		}
	}

	void XP3DEngine::SetActionAudioVolume(float fVolume)
	{
		if (m_fActionAudioVolume != fVolume)
		{
			m_fActionAudioVolume = fVolume;
			if (m_p3DGame)
			{
				try {
					GM::GConfig g3Dcfg = m_p3DGame->GetConfig();
					g3Dcfg.actionAudioVolume = Get3DVolume(m_fActionAudioVolume);;
					m_p3DGame->SetConfig(g3Dcfg);
				} catch (ModMud::MAssertException &)
				{
					if (s_pEB && s_pEB->GetApp())
					{
						s_pEB->GetApp()->On3DAssertErr();
					}
				}
			}
		}
	}

	void XP3DEngine::SetEffectAudioVolume(float fVolume)
	{
		if (m_fEffectAudioVolume != fVolume)
		{
			m_fEffectAudioVolume = fVolume;
			if (m_p3DGame)
			{
				try {
					GM::GConfig g3Dcfg = m_p3DGame->GetConfig();
					g3Dcfg.effectAudioVolume = Get3DVolume(m_fEffectAudioVolume);
					m_p3DGame->SetConfig(g3Dcfg);
				} catch (ModMud::MAssertException &)
				{
					if (s_pEB && s_pEB->GetApp())
					{
						s_pEB->GetApp()->On3DAssertErr();
					}
				}
			}
		}
	}

	void XP3DEngine::SetEffectPriority(int nPriority)
	{
		if (m_nEffectPriority != nPriority)
		{
			m_nEffectPriority = nPriority;
			if (m_p3DGame)
			{
				try {
					GM::GConfig g3Dcfg = m_p3DGame->GetConfig();
					g3Dcfg.effectPriority = nPriority;
					m_p3DGame->SetConfig(g3Dcfg);
				} catch (ModMud::MAssertException &)
				{
					if (s_pEB && s_pEB->GetApp())
					{
						s_pEB->GetApp()->On3DAssertErr();
					}
				}
			}
		}
	}

	float XP3DEngine::Get3DVolume(float oldVolume)
	{
		switch (m_3dSoundFadeSign)
		{
		case XPFS_NORMAL:
			break;
		case XPFS_FADE_OUT:
			oldVolume -= m_f3dsoundFadefRate;
			if (oldVolume < 0.0f) oldVolume = 0.0f;
			break;
		case XPFS_FADE_IN:
			oldVolume -= 1 - m_f3dsoundFadefRate;
			if (oldVolume < 0.0f) oldVolume = 0.0f;
			break;
		case XPFS_MUTE:
			oldVolume = 0.0f;
		}
		//3D那边，是0~-10000，我这边是0~-4080，为了统一，我就把外面传进来的0~1的区间映射到0.5~1的区间上
		if (oldVolume > 0.05)
		{
			oldVolume = oldVolume / 2.0f + 0.5f;
		}
		return oldVolume;
	}

	void XP3DEngine::SetFadeRate(float fFadefRate, bool force)
	{
		if (m_f3dsoundFadefRate == fFadefRate && !force)
			return;
		m_f3dsoundFadefRate = fFadefRate;
		if (m_p3DGame)
		{
			try {
				GM::GConfig g3Dcfg = m_p3DGame->GetConfig();
				g3Dcfg.effectAudioVolume = Get3DVolume(m_fEffectAudioVolume);
				g3Dcfg.actionAudioVolume = Get3DVolume(m_fActionAudioVolume);
				m_p3DGame->SetConfig(g3Dcfg);
			} catch (ModMud::MAssertException &)
			{
				if (s_pEB && s_pEB->GetApp())
				{
					s_pEB->GetApp()->On3DAssertErr();
				}
			}
		}
	}
}
