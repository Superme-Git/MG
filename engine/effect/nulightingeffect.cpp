#include "nulightingeffect.h"
#include "../common/nulog.h"
#include "../common/nuxml.h"
#include "../common/nufileiomanager.h"
#include "../renderer/nurenderer.h"
#include "../engine/nuanimanager.h"
#include "../engine/nuconfigmanager.h"
#include <platform/platform_types.h>

#include "../../dependencies/LJXML//Include/LJXML.hpp"

const int MAXSEGCOUNT = 5;
namespace Nuclear
{
	lightingEffect::lightingEffect(EngineBase *pEB) : Effect(pEB), m_coloration(1.0f, 1.0f, 1.0f, 1.0f), m_nPlayOnceTime(0),
		m_nFrameTime(0), m_scaleType(XPEST_NOSCALE), m_pHoldAni(NULL), m_bScreenCrood(false), m_bHoldAni(false), m_Fps(0.0f)
		, m_appearTime(0), m_disappearTime(0), m_totalTime(0), m_nCurLightingTime(0), m_CurrentColor(0xffffffff)
	{
		m_pPos = new NuclearTCT1Vertex[MAXSEGCOUNT * 4];
		for (int i = 0; i < MAXSEGCOUNT * 4; i++)
		{
			m_pPos[i].z = 1.0f;
			m_pPos[i].rhw = 1.0f;
			m_pPos[i].c = 0x00ffffff;
		}
		m_TargetX = 0;
		m_TargetY = 0;
		drawCount = 0;
	}

	lightingEffect::~lightingEffect(void)
	{
		if (m_pPos != NULL)
		{
			delete[] m_pPos;
			m_pPos = NULL;
		}
		//cancel notify
		//if (m_state == XPES_EMPTY)
		{
			m_pEB->GetAniManager()->RemoveLoadingNotify(L"/effect/" + m_name + SFI_ExtName, this);
		}

		if (m_pHoldAni)
			delete m_pHoldAni;
	}

	void lightingEffect::OnLoadAniReady(const std::wstring &filename)
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

	void lightingEffect::OnLoadAniBaseReady(const std::wstring &filename)
	{
		ResetPlayOnceTimeAndRelBox();
	}

	Nuclear_EffectAssureResResult lightingEffect::AssureResource(bool async)
	{
		if (m_state != XPES_EMPTY)
			return XPEARR_RESREADY;
		std::wstring uri = L"/effect/" + m_name + SFI_ExtName;
		m_ani = m_pEB->GetAniManager()->GetAni(uri, async && m_pEB->IsAsyncRead(), m_bLookupSegmPak);
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

	void lightingEffect::ResetPlayOnceTimeAndRelBox()
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

	void lightingEffect::Release()
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

	void lightingEffect::SetFps(float fps)
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

	int lightingEffect::LoadAttribute(LJXML::LJXML_Node<LJXML::Char>* pNode)
	{
		SetFps((float)_wtof(pNode->first_attribute(L"fps")->value()));
		m_pos.x = (float)_wtoi(pNode->first_attribute(L"rel_x")->value());
		m_pos.y = (float)_wtoi(pNode->first_attribute(L"rel_y")->value());
		m_vertexColor.data = pNode->first_attribute(L"v_c") ? _wtou(pNode->first_attribute(L"v_c")->value()) : 0xFFFFFFFF;

		// 播放控制
		m_playMode = (NuclearPlayMode)_wtoi(pNode->first_attribute(L"p_m")->value());
		if (IsLoopNum())
			m_nLoopNum = _wtoi(pNode->first_attribute(L"l_n")->value());
		else if (IsPlayTime())
			m_nPlayTime = _wtoi(pNode->first_attribute(L"p_t")->value());

		return 0;
	}

	void lightingEffect::SetLightTime(DWORD appearTime, DWORD disappearTime, DWORD totalTime)
	{
		m_appearTime = appearTime;
		m_disappearTime = disappearTime;
		m_totalTime = totalTime;
	}
	bool lightingEffect::Update(DWORD tickTime)
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
				m_nCurTime -= m_nPlayOnceTime;
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
			if (hardani->GetFrameCount() > 0)
			{
				m_nCurFrame = (m_nCurTime / m_nFrameTime) % hardani->GetFrameCount();
			}
		}
		else {
			Stop();
			return false;
		}

		if (m_nCurLightingTime < 0)
		{
			m_nCurLightingTime = 0;
			tickTime = 0;
		}
		m_nCurLightingTime += tickTime;
		if (m_nCurLightingTime > (int)m_totalTime)
		{
			NotifyEnd();
			Stop();
			return false;
		}
		if (m_nCurLightingTime < (int)m_appearTime)
		{
			int _al = 0xff * m_nCurLightingTime / m_appearTime;
			_al <<= 24;
			m_CurrentColor = _al | 0x00ffffff;
		}
		else if (m_nCurLightingTime > m_totalTime - m_disappearTime)
		{
			int _al = 0xff * (m_totalTime - m_nCurLightingTime) / m_disappearTime;
			_al <<= 24;
			m_CurrentColor = _al | 0x00ffffff;
		}
		else
			m_CurrentColor = 0xffffffff;
		return true;
	}

	//播放状态控制
	bool lightingEffect::Play()
	{
		if (Effect::Play())
		{
			m_nCurFrame = 0;
			return true;
		}
		return false;
	}

	void lightingEffect::CheckHardAni()
	{
		NuclearHardRef<XAni> hardani = m_ani;
		if (!hardani.IsValid() || hardani->GetState() == XAni::XPANISTATE_NO_XAP)
		{
			//这里是同步的，但是一般来说不会调用到
			m_ani = m_pEB->GetAniManager()->GetAni(L"/effect/" + m_name + SFI_ExtName, false, m_bLookupSegmPak);
		}
	}

	bool lightingEffect::GetBase(NuclearBase &base)
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

	void lightingEffect::SetEffectBindType(Nuclear_EffectBindType t)
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
	Nuclear_EffectBindType lightingEffect::GetEffectBindType() const
	{
		if (m_state == XPES_EMPTY)
			return XPEBT_NULL;
		const_cast<lightingEffect*>(this)->CheckHardAni();
		NuclearHardRef<XAni> hardani = m_ani;
		if (hardani.IsValid())
		{
			return hardani->GetEffectBindType();
		}
		return XPEBT_NULL;
	}

	void lightingEffect::HoldRes()
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

	void lightingEffect::SetTargetPos(int posX, int posY)
	{
		m_TargetX = posX;
		m_TargetY = posY;
	}
	void lightingEffect::buildLine(int xfrom, int yfrom, int xto, int yto, NuclearFRectt& picRECT, NuclearFRectt& uvRect)
	{
		float _detx = float(xto - xfrom);
		float _dety = float(yto - yfrom);
		float _len = sqrt(_detx*_detx + _dety*_dety);

		float seg = picRECT.Height();
		int _count = int(_len / seg) + 1;
		_count = (_count > MAXSEGCOUNT) ? MAXSEGCOUNT : _count;
		float lenscale = (float)picRECT.Width() / _len;
		float gx = lenscale * _dety * 0.5f;
		float gy = lenscale * _detx * 0.5f;

		float rate = seg / _len;
		float segX = _detx * rate;
		float segY = _dety * rate;
		for (int i = 0; i < _count; i++)
		{
			float addlenX = segX*i;
			float addlenY = segY*i;

			m_pPos[i * 4].x = xfrom + addlenX - gx;
			m_pPos[i * 4].y = yfrom + addlenY + gy;
			m_pPos[i * 4 + 3].x = xfrom + addlenX + gx;
			m_pPos[i * 4 + 3].y = yfrom + addlenY - gy;
			m_pPos[i * 4].u = uvRect.left;
			m_pPos[i * 4].v = uvRect.top;
			m_pPos[i * 4 + 3].u = uvRect.right;
			m_pPos[i * 4 + 3].v = uvRect.top;
			if (i == _count - 1)
			{
				m_pPos[i * 4 + 1].x = xto - gx;
				m_pPos[i * 4 + 1].y = yto + gy;
				m_pPos[i * 4 + 2].x = xto + gx;
				m_pPos[i * 4 + 2].y = yto - gy;

				float uvRt = (_len - seg*i) / seg;

				m_pPos[i * 4 + 1].u = uvRect.left;
				m_pPos[i * 4 + 1].v = uvRect.top + uvRt*(uvRect.bottom - uvRect.top);
				m_pPos[i * 4 + 2].u = uvRect.right;
				m_pPos[i * 4 + 2].v = uvRect.top + uvRt*(uvRect.bottom - uvRect.top);
			}
			else
			{
				m_pPos[i * 4 + 1].x = xfrom + addlenX + segX - gx;
				m_pPos[i * 4 + 1].y = yfrom + addlenY + segY + gy;
				m_pPos[i * 4 + 2].x = xfrom + addlenX + segX + gx;
				m_pPos[i * 4 + 2].y = yfrom + addlenY + segY - gy;
				m_pPos[i * 4 + 1].u = uvRect.left;
				m_pPos[i * 4 + 1].v = uvRect.bottom;
				m_pPos[i * 4 + 2].u = uvRect.right;
				m_pPos[i * 4 + 2].v = uvRect.bottom;
			}
			m_pPos[i * 4].c = m_CurrentColor;
			m_pPos[i * 4 + 1].c = m_CurrentColor;
			m_pPos[i * 4 + 2].c = m_CurrentColor;
			m_pPos[i * 4 + 3].c = m_CurrentColor;
		}

		drawCount = _count * 4;
	}
	//! 渲染
	bool lightingEffect::Render(Canvas* canvas, bool realRender)
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
			DrawPictureParam dpParam;
			dpParam.colorCount = 1;
			NuclearMatrix3 resultM, tmpM;
			XPMatrixIdentity(&resultM);
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
			NuclearVector2 pos;
			if (m_scaleType == XPEST_NOSCALE)
			{
				pos = NuclearVector2(m_pos.x + m_translate.x - viewport.left,
					m_pos.y + m_translate.y - viewport.top);
				XPMatrixTranslation(&tmpM, &pos);
			}
			else
			{
				pos = NuclearVector2((m_pos.x + m_translate.x - viewport.left) * m_pEB->GetWorldScale(),
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
					buildLine((int)pos.x, (int)pos.y, m_TargetX - viewport.left, m_TargetY - viewport.top, rect, pic.srcrect);
					m_pEB->GetRenderer()->DrawParticles(pic.pBigPic->handle, m_pPos, drawCount);
					if (dpParam.blend == XPTEXBM_ADDITIVE)
					{
						m_pEB->GetRenderer()->SelectRenderEffect(oldEffect);
					}
				}
			}
			return true;
		}
		return false;
	}

	void lightingEffect::Clone(Effect* p)
	{
		Effect::Clone(p);
		lightingEffect* pDst = static_cast<lightingEffect*>(p);
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