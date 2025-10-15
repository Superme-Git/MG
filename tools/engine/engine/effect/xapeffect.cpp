#include "stdafx.h"
#include "..\common\log.h"
#include "..\common\xml.h"
#include "..\common\fileiomanager.h"
#include "..\renderer\renderer.h"
#include "..\engine\animanager.h"
#include "..\engine\configmanager.h"
#include "xapeffect.h"

namespace Nuclear
{
	XapEffect::XapEffect(EngineBase *pEB) : Effect(pEB), m_coloration(1.0f, 1.0f, 1.0f, 1.0f), m_nPlayOnceTime(0), 
		m_nFrameTime(0), m_scaleType(XPEST_NOSCALE), m_pHoldAni(NULL), m_bScreenCrood(false), m_bHoldAni(false)
	{

	}

	XapEffect::~XapEffect(void)
	{
		//cancel notify
		if (m_state == XPES_EMPTY)
		{
			m_pEngine->GetAniManager()->RemoveLoadingNotify(L"/effect/" + m_name + SFI_ExtName, this);
		}

		if (m_pHoldAni)
			delete m_pHoldAni;
	}

	void XapEffect::OnLoadAniReady(const std::wstring &filename)
	{
		XHardRef<XAni> hardani = m_ani;
		if( !hardani.IsValid() || hardani->GetState() == XAni::XPANISTATE_NULL)
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
			m_pHoldAni = new XHardRef<XAni>(hardani);
		}
		Play();
		NotifyAsyncLoadResult(true);
	}

	void XapEffect::OnLoadAniBaseReady(const std::wstring &filename)
	{
		ResetPlayOnceTimeAndRelBox();
	}

	XPEffectAssureResResult XapEffect::AssureResource(bool async)
	{
		if (m_state != XPES_EMPTY)
			return XPEARR_RESREADY;
		std::wstring uri = L"/effect/" + m_name + SFI_ExtName;
		m_ani = m_pEngine->GetAniManager()->GetAni(uri, async && m_pEngine->IsAsyncRead());
		XHardRef<XAni> hardani = m_ani;
		if( !hardani.IsValid() || hardani->GetState() == XAni::XPANISTATE_NULL)
		{
			m_state = XPES_EMPTY;
			XPLOG_ERROR(L"加载资源失败：%s\n", uri.c_str());
			return XPEARR_LOAD_ERROR;
		}
		if (async && m_pEngine->IsAsyncRead())
		{
			if (m_pEngine->GetAniManager()->AsyncLoadAni(uri, this) == XPEARR_LOADING)
				return XPEARR_LOADING;
		}
		ResetPlayOnceTimeAndRelBox();
		m_pEngine->GetAniManager()->SyncLoadAni(hardani);
		m_state = XPES_STOP;
		Play();
		return XPEARR_RESREADY;
	}

	void XapEffect::ResetPlayOnceTimeAndRelBox()
	{
		XHardRef<XAni> hardani = m_ani;
		if (!hardani.IsValid())
			return;
		if (m_nFrameTime > 0)
		{
			m_nPlayOnceTime = hardani->GetFrameCount() * m_nFrameTime;
		} else {
			m_nPlayOnceTime = hardani->GetPlayTime();
			m_nFrameTime = m_nPlayOnceTime / hardani->GetFrameCount();
		}
		hardani->GetBorder(m_relBBox);
	}

	void XapEffect::Release()
	{
		if (m_state != XPES_STOP) 
			return;
		m_ani = XWeakRef<XAni>();
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
		if (fps > 0)
		{
			if (m_state != XPES_EMPTY)
			{
				CheckHardAni();
				XHardRef<XAni> hardani = m_ani;
				m_nPlayOnceTime = static_cast<DWORD>(hardani->GetFrameCount() * 1000 / fps);
			}
			m_nFrameTime = static_cast<DWORD>(1000.0f / fps);
		}
		
	}

	int XapEffect::LoadAttribute(XMLIO::CINode& node)
	{
		float fps = (float)node.GetFloatAttribute( L"fps");
		SetFps( fps);
		m_pos.x = node.GetIntAttribute( L"rel_x");
		m_pos.y = node.GetIntAttribute( L"rel_y");
		m_vertexColor.data = node.GetUIntAttribute( L"v_c", 0xFFFFFFFF);

		// 播放控制
		m_playMode = (XPPLAY_MODE)node.GetIntAttribute( L"p_m");
		if ( IsLoopNum())
			m_nLoopNum = node.GetIntAttribute( L"l_n" );
		else if ( IsPlayTime())
			m_nPlayTime = node.GetIntAttribute( L"p_t" );

		return 0;
	}

	int XapEffect::SaveAttribute(XMLIO::CONode& node)
	{
		node.SetAttribute( L"fps", GetFps());
		node.SetAttribute( L"rel_x", m_pos.x);
		node.SetAttribute( L"rel_y", m_pos.y);
		node.SetAttribute( L"v_c", (unsigned int)m_vertexColor.data);

		// 播放控制
		node.SetAttribute( L"p_m", (int)m_playMode);
		if ( IsLoopNum())
			node.SetAttribute( L"l_n", m_nLoopNum);
		else if( IsPlayTime())
			node.SetAttribute( L"p_t", m_nPlayTime);	

		return 0;
	}

	bool XapEffect::Update(DWORD tickTime)
	{
		if (m_state != XPES_PLAYING) return true;
		if( !m_ani.IsValid()) return false;
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
		} else {
			if (m_nCurTime >= m_nPlayOnceTime)
			{
				NotifyEnd();
				++m_curLoopNum;
				if ((m_playMode == XPPM_LOOPNUM) && (m_curLoopNum >= m_nLoopNum))
				{
					Stop();
					return false;
				}
				m_nCurTime %= m_nPlayOnceTime;
			}
		}
		//ConfigManager *pCfgMan = m_pEngine->GetConfigManager();
		//if (pCfgMan && pCfgMan->GetSystemLevel() < m_systemLevel)
		//	return true;
		//就算播放级别不够也要正常update 
		CheckHardAni();
		XHardRef<XAni> hardani = m_ani;
		if( hardani.IsValid() )
		{
			if( !hardani->TestDir(m_dir) )
			{
				m_dir = XPDIR_TOP;
				//Stop();
				//return false;
				//这里到底要Stop然后return false好还是直接用TOP好？
			}
			m_nCurFrame = (m_nCurTime / m_nFrameTime) % hardani->GetFrameCount();
		} else {
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
		XHardRef<XAni> hardani = m_ani;
		if( !hardani.IsValid() || hardani->GetState() == XAni::XPANISTATE_NO_XAP )
		{
			//这里是同步的，但是一般来说不会调用到
			m_ani = m_pEngine->GetAniManager()->GetAni(L"/effect/" + m_name + SFI_ExtName, false);
		}
	}

	bool XapEffect::GetBase(XPBase &base)
	{
		if (m_state==XPES_EMPTY)
			return false;
		CheckHardAni();
		XHardRef<XAni> hardani = m_ani;
		if( hardani.IsValid() )
		{
			return hardani->GetBase(base);
		}
		return false;
	}

	void XapEffect::SetEffectBindType(XPEFFECT_BIND_TYPE t)
	{
		if (m_state==XPES_EMPTY)
			return;
		CheckHardAni();
		XHardRef<XAni> hardani = m_ani;
		if( hardani.IsValid() )
		{
			hardani->SetEffectBindType(t);
		}
	}
	XPEFFECT_BIND_TYPE XapEffect::GetEffectBindType() const
	{
		if (m_state==XPES_EMPTY)
			return XPEBT_NULL;
		const_cast<XapEffect*>(this)->CheckHardAni();
		XHardRef<XAni> hardani = m_ani;
		if( hardani.IsValid() )
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
		if (m_state==XPES_EMPTY)
			return;
		if (m_pHoldAni)
			delete m_pHoldAni;
		m_pHoldAni = new XHardRef<XAni>(m_ani);
	}

	//! 渲染
	bool XapEffect::Render(Canvas* canvas)
	{
		if (m_state == XPES_STOP) 
			return false;
		if (m_nCurTime < 0)
			return true;
		CheckHardAni();
		XHardRef<XAni> hardani = m_ani;
		if (hardani.IsValid())
		{
			hardani.SetTick(m_pEngine->GetTick());
			ConfigManager *pCfgMan = m_pEngine->GetConfigManager();
			if (pCfgMan && pCfgMan->GetSystemLevel() < hardani->GetSysLevel())
				return true;
			switch (hardani->GetState())
			{
			case XAni::XPANISTATE_NOPICTURE:
			case XAni::XPANISTATE_BASE_PIC:
				m_pEngine->GetAniManager()->SyncLoadAni(hardani);
				break;
			case XAni::XPANISTATE_NULL:
				return true;
				break;
			}
			XAni::XPic pic;
			DrawPictureParam dpParam;
			dpParam.colorCount = 1;
			XPMATRIX3 resultM, tmpM;
			XPMatrixIdentity(&resultM);
			if ((hardani->GetDirMode() == XPANIDM_8FROM5 && m_dir > XPDIR_BOTTOM ) ||
				(hardani->GetDirMode() == XPANIDM_B2FROM1 && m_dir == XPDIR_BOTTOMLEFT ) ||
				(hardani->GetDirMode() == XPANIDM_4FROM2 && m_dir == XPDIR_TOPRIGHT) ||
				(hardani->GetDirMode() == XPANIDM_4FROM2 && m_dir == XPDIR_BOTTOMLEFT)
				)
			{
				XPMatrixMirrorY(&resultM);
			}
			else if( hardani->GetDirMode() == XPANIDM_8FROM1 && m_dir != XPDIR_RIGHT )
			{
				GetRotationScalingMartrixByDirection(&resultM, NULL, m_dir);														
			}
			if (m_scaleType == XPEST_ALLSCALE)
			{
				XPMatrixScaling(&tmpM, m_scale.x * m_pEngine->GetWorldScale(), m_scale.y * m_pEngine->GetWorldScale());
			}
			else
			{
				XPMatrixScaling(&tmpM, m_scale.x, m_scale.y);
			}
			resultM *= tmpM;
			XPMatrixRotationOrigin(&tmpM, m_rotationRadian);
			resultM *= tmpM;
			CRECT viewport = m_pEngine->GetViewport();
			if (m_bScreenCrood)
			{
				viewport.left = 0;
				viewport.top = 0;
			}
			if (m_scaleType == XPEST_NOSCALE)
			{
				XPVECTOR2 pos(m_pos.x + m_translate.x - viewport.left, 
					m_pos.y + m_translate.y - viewport.top);
				XPMatrixTranslation(&tmpM, &pos);
			}
			else
			{
				XPVECTOR2 pos((m_pos.x + m_translate.x - viewport.left) * m_pEngine->GetWorldScale(), 
					(m_pos.y + m_translate.y - viewport.top) * m_pEngine->GetWorldScale());
				XPMatrixTranslation(&tmpM, &pos);
			}
			resultM *= tmpM;
			dpParam.pMatrix = &resultM;
			XPCOLOR color;
			dpParam.pColor = &color;
			dpParam.blend = hardani->GetBlend();
			color = m_vertexColor;
			color *= m_coloration;
			for(int k=0; k< hardani->GetRegionCount();++k)
			{
				if( hardani->GetPic(m_dir, m_nCurFrame, k, pic) && pic.pBigPic && pic.pBigPic->handle != INVALID_PICTURE_HANDLE )
				{
					FRECT rect = pic.offset.ToFRECT();
					dpParam.handle = pic.pBigPic->handle;
					dpParam.pRect = &rect;
					dpParam.pSrcrect = &pic.srcrect;
					XPRENDER_EFFECT oldEffect = m_pEngine->GetRenderer()->GetRenderEffect();
					if (dpParam.blend == XPTEXBM_ADDITIVE)
					{
						m_pEngine->GetRenderer()->SelectRenderEffect(XPRE_NULL);
					}
					m_pEngine->GetRenderer()->DrawPicture(dpParam);
					if (dpParam.blend == XPTEXBM_ADDITIVE)
					{
						m_pEngine->GetRenderer()->SelectRenderEffect(oldEffect);
					}
				}
			}
			return true;
		}
		return false;
	}
}