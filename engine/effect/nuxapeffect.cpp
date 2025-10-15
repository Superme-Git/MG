#include "nuxapeffect.h"
#include "../common/nulog.h"
#include "../common/nuxml.h"
#include "../common/nufileiomanager.h"
#include "../renderer/nurenderer.h"
#include "../engine/nuanimanager.h"
#include "../engine/nuconfigmanager.h"
#include <platform/platform_types.h>

#include "../../dependencies/LJXML//Include/LJXML.hpp"

namespace Nuclear
{
	XapEffect::XapEffect(EngineBase *pEB) : Effect(pEB), m_coloration(1.0f, 1.0f, 1.0f, 1.0f), m_nPlayOnceTime(0),
		m_nFrameTime(0), m_scaleType(XPEST_NOSCALE), m_pHoldAni(NULL), m_bScreenCrood(false), m_bHoldAni(false), m_Fps(0.0f)
	{

	}

	XapEffect::~XapEffect(void)
	{
		//cancel notify
		//if (m_state == XPES_EMPTY)
		{
			m_pEB->GetAniManager()->RemoveLoadingNotify(L"/effect/" + m_name + SFI_ExtName, this);
		}

		if (m_pHoldAni)
			delete m_pHoldAni;
	}

	void XapEffect::OnLoadAniReady(const std::wstring &filename)
	{
		NuclearHardRef<XAni> hardani = m_ani;
		if (!hardani.IsValid() || hardani->GetState() == XAni::XPANISTATE_NULL)
		{
			m_state = XPES_EMPTY;
			XPLOG_ERROR(L"加载资源失败：%s\n", filename.c_str());
			NotifyAsyncLoadResult(false);
			return;
		}
		ResetPlayOnceTimeAndRelBox();
		m_state = XPES_STOP;
		if (m_bHoldAni)
		{
			if (m_pHoldAni)
				delete m_pHoldAni;
			m_pHoldAni = new NuclearHardRef<XAni>(hardani);
		}
		Play();
		NotifyAsyncLoadResult(true);
	}

	void XapEffect::OnLoadAniBaseReady(const std::wstring &filename)
	{
		ResetPlayOnceTimeAndRelBox();
	}

	Nuclear_EffectAssureResResult XapEffect::AssureResource(bool async)
	{
		if (m_state != XPES_EMPTY)
			return XPEARR_RESREADY;
		std::wstring uri = L"/effect/" + m_name + SFI_ExtName;
		m_ani = m_pEB->GetAniManager()->GetAni(uri, async && m_pEB->IsAsyncRead(), m_bLookupSegmPak, false, 0, this);
		NuclearHardRef<XAni> hardani = m_ani;
		if (!hardani.IsValid() || hardani->GetState() == XAni::XPANISTATE_NULL)
		{
			m_state = XPES_EMPTY;
			XPLOG_ERROR(L"加载资源失败：%s\n", uri.c_str());
			return XPEARR_LOAD_ERROR;
		}
		if (async && m_pEB->IsAsyncRead())
		{
			if (m_pEB->GetAniManager()->AsyncLoadAni(uri, this, m_bLookupSegmPak) == XPEARR_LOADING)
				return XPEARR_LOADING;
		}
		ResetPlayOnceTimeAndRelBox();
		//m_pEB->GetAniManager()->SyncLoadAni(hardani, m_bLookupSegmPak);
		m_state = XPES_STOP;
		Play();
		return XPEARR_RESREADY;
	}

	void XapEffect::ResetPlayOnceTimeAndRelBox()
	{
		NuclearHardRef<XAni> hardani = m_ani;
		if (!hardani.IsValid())
			return;
		if (m_nFrameTime > 0)
		{
			m_nPlayOnceTime = hardani->GetFrameCount() * m_nFrameTime;
		}
		else {
			m_nPlayOnceTime = hardani->GetPlayTime();
			int frameCount = hardani->GetFrameCount();
			m_nFrameTime = frameCount > 0 ? m_nPlayOnceTime / frameCount : 0;
		}
		hardani->GetBorder(m_relBBox);
	}

	void XapEffect::Release()
	{
		if (m_state != XPES_STOP)
			return;
		m_ani = NuclearWeakRef<XAni>();
		if (m_bHoldAni)
		{
			if (m_pHoldAni)
				delete m_pHoldAni;
			m_pHoldAni = NULL;
			m_bHoldAni = false;
		}
		m_state = XPES_EMPTY;
	}

	void XapEffect::SetFps(float fps)
	{
		m_Fps = fps;
		if (fps > 0)
		{
			if (m_state != XPES_EMPTY)
			{
				CheckHardAni();
				NuclearHardRef<XAni> hardani = m_ani;
				m_nPlayOnceTime = static_cast<DWORD>(hardani->GetFrameCount() * 1000 / fps);
			}
			m_nFrameTime = static_cast<DWORD>(1000.0f / fps);
		}
	}

	int XapEffect::LoadAttribute(LJXML::LJXML_Node<LJXML::Char>* pNode)
	{
		SetFps(_wtof(pNode->first_attribute(L"fps")->value()));
		m_pos.x = _wtoi(pNode->first_attribute(L"rel_x")->value());
		m_pos.y = _wtoi(pNode->first_attribute(L"rel_y")->value());
		m_vertexColor.data = pNode->first_attribute(L"v_c") ? _wtou(pNode->first_attribute(L"v_c")->value()) : 0xFFFFFFFF;

		// 播放控制
		m_playMode = (NuclearPlayMode)_wtoi(pNode->first_attribute(L"p_m")->value());
		if (IsLoopNum())
			m_nLoopNum = _wtoi(pNode->first_attribute(L"l_n")->value());
		else if (IsPlayTime())
			m_nPlayTime = _wtoi(pNode->first_attribute(L"p_t")->value());

		return 0;
	}

	bool XapEffect::Update(DWORD tickTime)
	{
		if (!m_bVisiblity) return true;
		if (m_state != XPES_PLAYING) return true;
		if (!m_ani.IsValid()) return false;
		if (m_nCurTime < 0)
		{
			m_nCurTime = 0;
			tickTime = 0;
		}
		m_nCurTime += tickTime;
		if (m_playMode == XPPM_TIME)
		{
			if (m_nCurTime >= m_nPlayTime)
			{
				NotifyEnd();
				Stop();
				return false;
			}
		}
		else {
			if (m_nCurTime >= m_nPlayOnceTime)
			{
				NotifyEnd();
				++m_curLoopNum;
				if ((m_playMode == XPPM_LOOPNUM) && (m_curLoopNum >= m_nLoopNum))
				{
					Stop();
					return false;
				}

				if (m_nPlayOnceTime > 0)
				{
					m_nCurTime %= m_nPlayOnceTime;
				}
			}
		}
		//ConfigManager *pCfgMan = m_pEB->GetConfigManager();
		//if (pCfgMan && pCfgMan->GetSystemLevel() < m_systemLevel)
		//	return true;
		//就算播放级别不够也要正常update
		CheckHardAni();
		NuclearHardRef<XAni> hardani = m_ani;
		if (hardani.IsValid() && (m_nFrameTime != 0))
		{
			if (!hardani->TestDir(m_dir))
			{
				m_dir = XPDIR_TOP;
				//Stop();
				//return false;
				//这里到底要Stop然后return false好还是直接用TOP好？
			}
			if (m_nFrameTime > 0 && hardani->GetFrameCount() > 0)
			{
				m_nCurFrame = (m_nCurTime / m_nFrameTime) % hardani->GetFrameCount();
			}
		}
		else {
			Stop();
			return false;
		}
		return true;
	}

	//播放状态控制
	bool XapEffect::Play()
	{
		if (Effect::Play())
		{
			m_nCurFrame = 0;
			return true;
		}
		return false;
	}

	void XapEffect::CheckHardAni()
	{
		NuclearHardRef<XAni> hardani = m_ani;
		if (!hardani.IsValid() || hardani->GetState() == XAni::XPANISTATE_NO_XAP)
		{
			//这里是同步的，但是一般来说不会调用到
			m_ani = m_pEB->GetAniManager()->GetAni(L"/effect/" + m_name + SFI_ExtName, false, m_bLookupSegmPak, false, 0, this);
		}
	}

	bool XapEffect::GetBase(NuclearBase &base)
	{
		if (m_state == XPES_EMPTY)
			return false;
		CheckHardAni();
		NuclearHardRef<XAni> hardani = m_ani;
		if (hardani.IsValid())
		{
			return hardani->GetBase(base);
		}
		return false;
	}

	void XapEffect::SetEffectBindType(Nuclear_EffectBindType t)
	{
		if (m_state == XPES_EMPTY)
			return;
		CheckHardAni();
		NuclearHardRef<XAni> hardani = m_ani;
		if (hardani.IsValid())
		{
			hardani->SetEffectBindType(t);
		}
	}
	Nuclear_EffectBindType XapEffect::GetEffectBindType() const
	{
		if (m_state == XPES_EMPTY)
			return XPEBT_NULL;
		const_cast<XapEffect*>(this)->CheckHardAni();
		NuclearHardRef<XAni> hardani = m_ani;
		if (hardani.IsValid())
		{
			return hardani->GetEffectBindType();
		}
		return XPEBT_NULL;
	}

	void XapEffect::HoldRes()
	{
		if (m_bHoldAni)
			return;
		m_bHoldAni = true;
		if (m_state == XPES_EMPTY)
			return;
		if (m_pHoldAni)
			delete m_pHoldAni;
		m_pHoldAni = new NuclearHardRef<XAni>(m_ani);
	}

	//! 渲染
	bool XapEffect::Render(Canvas* canvas, bool realRender)
	{
		if (!m_bVisiblity) return true;
		if (m_state == XPES_STOP)
			return false;
		if (m_nCurTime < 0)
			return true;
		CheckHardAni();
		NuclearHardRef<XAni> hardani = m_ani;
		if (hardani.IsValid())
		{
			hardani.SetTick(m_pEB->GetTick());
			ConfigManager *pCfgMan = m_pEB->GetConfigManager();
			if (pCfgMan && pCfgMan->GetSystemLevel() < hardani->GetSysLevel())
				return true;
			switch (hardani->GetState())
			{
			case XAni::XPANISTATE_NOPICTURE:
			case XAni::XPANISTATE_BASE_PIC:
				//m_pEB->GetAniManager()->SyncLoadAni(hardani, m_bLookupSegmPak);
				break;
			case XAni::XPANISTATE_NULL:
				return true;
				break;
			}

			XAni::XPic pic;
			if (realRender)
			{
				DrawPictureParam dpParam;
				dpParam.colorCount = 1;
				NuclearMatrix3 resultM, tmpM;
				XPMatrixIdentity(&resultM);
				if ((hardani->GetDirMode() == XPANIDM_8FROM5 && m_dir > XPDIR_BOTTOM) ||
					(hardani->GetDirMode() == XPANIDM_B2FROM1 && m_dir == XPDIR_BOTTOMLEFT) ||
					(hardani->GetDirMode() == XPANIDM_4FROM2 && m_dir == XPDIR_TOPRIGHT) ||
					(hardani->GetDirMode() == XPANIDM_4FROM2 && m_dir == XPDIR_BOTTOMLEFT)
					)
				{
					XPMatrixMirrorY(&resultM);
				}
				else if (hardani->GetDirMode() == XPANIDM_8FROM1 && m_dir != XPDIR_RIGHT)
				{
					GetRotationScalingMartrixByDirection(&resultM, NULL, m_dir);
				}
				if (m_scaleType == XPEST_ALLSCALE)
				{
					XPMatrixScaling(&tmpM, m_scale.x * m_pEB->GetWorldScale(), m_scale.y * m_pEB->GetWorldScale());
				}
				else
				{
					XPMatrixScaling(&tmpM, m_scale.x, m_scale.y);
				}
				resultM *= tmpM;
				XPMatrixRotationOrigin(&tmpM, m_rotationRadian);
				resultM *= tmpM;
				NuclearRect viewport = m_pEB->GetViewport();
				if (m_bScreenCrood)
				{
					viewport.left = 0;
					viewport.top = 0;
				}
				if (m_scaleType == XPEST_NOSCALE)
				{
					NuclearVector2 pos(m_pos.x + m_translate.x - viewport.left,
						m_pos.y + m_translate.y - viewport.top);
					XPMatrixTranslation(&tmpM, &pos);
				}
				else
				{
					NuclearVector2 pos((m_pos.x + m_translate.x - viewport.left) * m_pEB->GetWorldScale(),
						(m_pos.y + m_translate.y - viewport.top) * m_pEB->GetWorldScale());
					XPMatrixTranslation(&tmpM, &pos);
				}
				resultM *= tmpM;
				dpParam.pMatrix = &resultM;
				NuclearColor color;
				dpParam.pColor = &color;
				dpParam.blend = hardani->GetBlend();
				color = m_vertexColor;
				color *= m_coloration;
				for (int k = 0; k < hardani->GetRegionCount(); ++k)
				{
					if (hardani->GetPic(m_dir, m_nCurFrame, k, pic) && pic.pBigPic && pic.pBigPic->handle != INVALID_PICTURE_HANDLE)
					{
						NuclearFRectt rect = pic.offset.ToFRECT();
						dpParam.handle = pic.pBigPic->handle;
						dpParam.pRect = &rect;
						dpParam.pSrcrect = &pic.srcrect;
						XPRENDER_EFFECT oldEffect = m_pEB->GetRenderer()->GetRenderEffect();
						if (dpParam.blend == XPTEXBM_ADDITIVE)
						{
							m_pEB->GetRenderer()->SelectRenderEffect(XPRE_NULL);
						}
						m_pEB->GetRenderer()->DrawPicture(dpParam);
						if (dpParam.blend == XPTEXBM_ADDITIVE)
						{
							m_pEB->GetRenderer()->SelectRenderEffect(oldEffect);
						}
					}
				}
			}
			else//非真实渲染
			{
				for (int k = 0; k < hardani->GetRegionCount(); ++k)
				{
					hardani->GetPic(m_dir, m_nCurFrame, k, pic);
				}
			}
			return true;
		}
		return false;
	}

	void XapEffect::Clone(Effect* p)
	{
		Effect::Clone(p);
		XapEffect* pDst = static_cast<XapEffect*>(p);
		pDst->SetFps(GetFps());
		pDst->SetLocation(m_pos);
		pDst->SetVertexColor(m_vertexColor);
		pDst->SetPlayMode(m_playMode);
		if (IsLoopNum())
			pDst->SetLoopNum(m_nLoopNum);
		else if (IsPlayTime())
			pDst->SetPlayTime(m_nPlayTime);
	}
}