#include "../common/nulog.h"
#include "../common/nuxml.h"
#include "../particlesystem/nuiparticlemanager.h"
#include "../renderer/nurenderer.h"
#include "nuparticleeffect.h"
#include <platform/platform_types.h>

#include "../../dependencies/LJXML//Include/LJXML.hpp"


namespace Nuclear
{
	ParticleEffect::ParticleEffect(EngineBase* pEB) : Effect(pEB), m_handle(INVALID_PS_HANDLE), 
		m_nPlayOnceTime(0), m_coloration(1.0f, 1.0f, 1.0f, 1.0f)
	{
	}

	ParticleEffect::~ParticleEffect(void)
	{
		if( m_handle != INVALID_PS_HANDLE )
		{
			m_pEB->GetRenderer()->GetParticleManager()->DeleteParticleSystem(m_handle);
		}
	}

	void ParticleEffect::SetScaleType(Nuclear_EffectScaleType t)
	{
		if (m_parmeter.m_psModifyParam.m_scaleType != t)
		{
			m_parmeter.m_psModifyParam.m_scaleType = t;
			m_pEB->GetRenderer()->GetParticleManager()->ModifyParticleSystem(m_handle, m_parmeter);
		}
	}

	void ParticleEffect::OnReady(bool success)
	{
		if (success)
		{
			m_state = XPES_STOP;
			this->Play();
		} else {
            
			m_pEB->GetRenderer()->GetParticleManager()->DeleteParticleSystem(m_handle);
			m_handle = INVALID_PS_HANDLE;
			m_state = XPES_EMPTY;
		}
		NotifyAsyncLoadResult(success);
	}

	Nuclear_EffectAssureResResult ParticleEffect::AssureResource(bool async)
	{
		if (m_state != XPES_EMPTY)
			return XPEARR_RESREADY;
		if( m_handle != INVALID_PS_HANDLE )
		{
			async = async && m_pEB->IsAsyncRead();
			switch (m_pEB->GetRenderer()->GetParticleManager()->EnsureParticleSystemResouce(m_handle, async, this))
			{
			case XPEARR_LOADING:
				return XPEARR_LOADING;
				break;
			case XPEARR_RESREADY:
				m_state = XPES_STOP;
				this->Play();
				return XPEARR_RESREADY;
				break;
			case XPEARR_LOAD_ERROR:
				m_pEB->GetRenderer()->GetParticleManager()->DeleteParticleSystem(m_handle);
				m_state = XPES_EMPTY;
				return XPEARR_LOAD_ERROR;
				break;
			default:
				XPASSERT(false && L"ParticleEffect::AssureResource");
				break;
			}
		}
		return XPEARR_LOAD_ERROR;
	}

	void ParticleEffect::Release()
	{
		if (m_state != XPES_STOP)
			return;
		m_pEB->GetRenderer()->GetParticleManager()->ReleaseParticleSystemRes(m_handle);
		m_state = XPES_EMPTY;
	}

	int ParticleEffect::LoadAttribute(LJXML::LJXML_Node<LJXML::Char>* pNode)
	{
		m_vertexColor.data = _wtoi(pNode->first_attribute(L"v_c")->value());
		//默认颜色
		if (m_vertexColor.data == 0xFF000001)
			m_vertexColor.data = 0xFFFFFFFF;
		m_pos.x = _wtoi(pNode->first_attribute(L"rel_x")->value()); 
		m_pos.y = _wtoi(pNode->first_attribute(L"rel_y")->value());

		m_playMode = (NuclearPlayMode)_wtoi(pNode->first_attribute(L"p_m")->value());
		switch (m_playMode)
		{
		case XPPM_LOOP:
			SetLoopNum(-1);
			break;
		case XPPM_LOOPNUM:
			SetLoopNum(_wtoi(pNode->first_attribute(L"l_n")->value()));
			break;
		case XPPM_TIME:
			SetPlayTime(_wtoi(pNode->first_attribute(L"p_t")->value()));
			break;
		default:
			XPASSERT(false && L"ParticleEffect::LoadAttribute!");
			break;
		}
		//在SetLoopNum或者SetPlayTime里面会保存的
		return 0;
	}

	bool ParticleEffect::Update(DWORD tickTime)
	{
		if (!m_bVisiblity) return true;
		if (m_state != XPES_PLAYING) return true;
		if( m_handle == INVALID_PS_HANDLE )	return false;
		IParticleManager* pMgr = m_pEB->GetRenderer()->GetParticleManager();
		
		if (m_nCurTime < 0)
		{
			m_nCurTime = 0;
			tickTime = 0;
		}
		m_nCurTime += tickTime;
		//链接粒子就忽略m_translate吧
		//位置
		m_parmeter.m_psModifyParam.m_pointCent.x = m_pos.x + m_translate.x;
		m_parmeter.m_psModifyParam.m_pointCent.y = m_pos.y + m_translate.y;
		//旋转
		m_parmeter.m_psModifyParam.m_fEmitterSpinAngle = m_rotationRadian;
		//缩放
		m_parmeter.m_psModifyParam.m_fEmissionScale = m_scale.x;
		m_parmeter.m_psModifyParam.m_fParticlePosCoef = m_pEB->GetWorldScale();
		//颜色
		NuclearColor color(m_vertexColor); 
		color *= m_coloration;
		m_parmeter.m_psModifyParam.m_dwColor = color.data;
		pMgr->ModifyParticleSystem(m_handle, m_parmeter.m_psModifyParam);
		pMgr->UpdateParticleSystem(m_handle, static_cast<float>(tickTime) / 1000);

		if (m_nCurTime >= m_nPlayOnceTime)
		{
			++m_curLoopNum;
			NotifyEnd();
			m_nCurTime -= m_nPlayOnceTime;
			if ((m_playMode != XPPM_LOOP) || (m_nLoopNum != -1))
				pMgr->ResetParticleSystem(m_handle);
		}
		switch (m_playMode)
		{
		case XPPM_TIME:
			if (m_curLoopNum >= 1)
			{
				Stop();
				return false;
			}
			break;
		case XPPM_LOOPNUM:
			if (m_curLoopNum >= m_nLoopNum)
			{
				Stop();
				return false;
			}
			
			break;
		case XPPM_LOOP:
			break;
		}
		return true;
	}

	void ParticleEffect::SetEffectBindType(Nuclear_EffectBindType t)
	{
		XPASSERT(false && L"粒子不能设这个，还没有实现！");
	}

	Nuclear_EffectBindType ParticleEffect::GetEffectBindType() const
	{
		return m_pEB->GetRenderer()->GetParticleManager()->GetEffectBindType(m_handle);
	}
	
	//! 渲染
	bool ParticleEffect::Render(Canvas* canvas, bool realRender)
	{
		if (!m_bVisiblity) return true;
		if (m_state == XPES_STOP)
			return false;
		if (m_state == XPES_EMPTY)
			return false;
		if (m_nCurTime < 0)
			return true;
		if (m_handle == INVALID_PS_HANDLE)
			return false;

		m_pEB->GetRenderer()->GetParticleManager()->RenderParticleSystem(m_handle, true, true);
		return true;
	}

	//粒子收集以供批绘制
	bool ParticleEffect::CollectRender()
	{
		if (m_state == XPES_STOP) 
			return false;
		if (m_state == XPES_EMPTY)
			return false;
		if (m_nCurTime < 0)
			return true;
		if (m_handle == INVALID_PS_HANDLE)
			return false;

		m_pEB->GetRenderer()->GetParticleManager()->RenderParticleSystem(m_handle, true, false);
		return true;
	}

	//设置循环模式（如果是XPPCM_NO_CYCLE就return false）
	bool ParticleEffect::SetCycleMode(XPParticleEffectCycleMode mode)
	{
		if (mode == XPPCM_NO_CYCLE)
			return false;

		if (!Effect::SetLoopNum(-1))
			return false;
		if (mode == XPPCM_PULSE_EMISSION)
		{
			m_nLoopNum = -2;
			//粒子会死的循环，播放多少遍，其实是粒子Reset多少次，每一遍都是等所有粒子都死光之后再发射
			m_parmeter.cycle = false;
		} else {
			m_parmeter.cycle = true;
		}
		m_parmeter.time = PSDEFAULTTIME;
		m_pEB->GetRenderer()->GetParticleManager()->ModifyParticleSystem(m_handle, m_parmeter);
		m_nPlayOnceTime = static_cast<int>((GetSysLife() + GetParticleMaxLife()) * 1000);
		return true;
	}

	XPParticleEffectCycleMode ParticleEffect::GetCycleMode() const
	{
		if (m_nLoopNum == -2)
			return XPPCM_PULSE_EMISSION;
		if (m_nLoopNum == -1)
			return XPPCM_ALWAY_EMISSION;
		return XPPCM_NO_CYCLE;
	}

	//设置粒子系统的生命长度（秒），-1代表默认，只在XPPCM_PULSE_EMISSION的时候起作用
	bool ParticleEffect::SetSysLife(float time)
	{
		if (m_nLoopNum != -2)
			return false;
		m_parmeter.time = time;
		m_pEB->GetRenderer()->GetParticleManager()->ModifyParticleSystem(m_handle, m_parmeter);
		return true;
	}

	float ParticleEffect::GetSysLife()
	{
		return m_pEB->GetRenderer()->GetParticleManager()->GetSysLife(m_handle);
	}

	void ParticleEffect::SetLocation(const NuclearLocation& location)
	{
		if (m_parmeter.m_bModifyLineEmitter)
		{
			NuclearPoint pt = location - m_pos;
			m_parmeter.m_psModifyParam.m_fpoint0 += pt;
			m_parmeter.m_psModifyParam.m_fpoint1 += pt;
		}
		Effect::SetLocation(location);
	}

	//如果是特效链粒子，那么可以设链的两个端点，type==0代表是起点，type==1代表终点
	//如果不是特效链粒子，那么返回false
	bool ParticleEffect::SetEmitterLinkPoint(const NuclearFPoint &pt, int type)
	{
		if (!m_parmeter.m_bModifyLineEmitter)
			return false;
		switch (type)
		{
		case 0:
			m_parmeter.m_psModifyParam.m_fpoint0 = pt;
			break;
		case 1:
			m_parmeter.m_psModifyParam.m_fpoint1 = pt;
			break;
		default:
			return false;
		}
		m_pos = m_parmeter.m_psModifyParam.m_fpoint0 + m_parmeter.m_psModifyParam.m_fpoint1;
		m_pos /= 2;
		return true;
	}

	bool ParticleEffect::GetEmitterLinkPoint(NuclearFPoint &pt, int type)
	{
		if (!m_parmeter.m_bModifyLineEmitter)
			return false;
		switch (type)
		{
		case 0:
			pt = m_parmeter.m_psModifyParam.m_fpoint0;
			break;
		case 1:
			pt = m_parmeter.m_psModifyParam.m_fpoint1;
			break;
		default:
			return false;
		}
		return true;
	}

	//! 小于0表示无限循环
	bool ParticleEffect::SetLoopNum( int loopNum )
	{
		if (!Effect::SetLoopNum(loopNum))
			return false;
		//粒子会死的循环，播放多少遍，其实是粒子Reset多少次，每一遍都是等所有粒子都死光之后再发射
		m_parmeter.time = PSDEFAULTTIME;
		m_parmeter.cycle = loopNum<0?true:false;
		m_pEB->GetRenderer()->GetParticleManager()->ModifyParticleSystem(m_handle, m_parmeter);
		m_nPlayOnceTime = static_cast<int>((GetSysLife() + GetParticleMaxLife()) * 1000);
		return true;
	}

	bool ParticleEffect::SetPlayTime( DWORD time) //毫秒
	{
		if ( !Effect::SetPlayTime( time))
			return false;
		float fPartMaxLife = GetParticleMaxLife();
		m_parmeter.time = static_cast<float>(time) / 1000 - fPartMaxLife;
		if (m_parmeter.time < 0.0f)
		{
			m_parmeter.time = 0.1f; //至少让粒子系统存在0.1秒
		}
		m_parmeter.cycle = false;
		m_pEB->GetRenderer()->GetParticleManager()->ModifyParticleSystem(m_handle, m_parmeter);
		m_nPlayOnceTime = static_cast<int>((m_parmeter.time + fPartMaxLife) * 1000);
		return true;
	}

	bool ParticleEffect::GetBase(NuclearBase &base)
	{
		base.left.x = -10;
		base.left.y = 0;
		base.right.x = 10;
		base.right.y = 0;
		return true;
	}

	void ParticleEffect::SetScreenCrood(bool b)
	{
		if (m_parmeter.m_bScreenCoord != b)
		{
			m_parmeter.m_bScreenCoord = b;
			m_pEB->GetRenderer()->GetParticleManager()->ModifyParticleSystem(m_handle, m_parmeter);
		}
	}

	bool ParticleEffect::Play()
	{
		if (Effect::Play())
		{
			m_pEB->GetRenderer()->GetParticleManager()->ResetParticleSystem(m_handle);
			return true;
		}
		return false;
	}

	// 获得指定粒子特效当前在渲染的粒子数
	int ParticleEffect::GetNumActiveParticles() const
	{
		return m_pEB->GetRenderer()->GetParticleManager()->GetNumActiveParticles(m_handle);
	}

	// 获得包围盒等信息..
	PSINFO ParticleEffect::GetParticleSystemInfo() const
	{
		return m_pEB->GetRenderer()->GetParticleManager()->GetParticleSystemInfo(m_handle);
	}

	// 粒子的可能存活最大时间(秒)
	float ParticleEffect::GetParticleMaxLife() const 
	{
		return m_pEB->GetRenderer()->GetParticleManager()->GetParticleMaxLife(m_handle);
	}

	//以下针对特殊粒子特效的设置..
	// 设置纹理, 以及纹理的最大u、v坐标(烂显卡上创建没有纹理过滤的非2幂纹理)
	bool ParticleEffect::SetSpecialPsTexture(PictureHandle ShapesTexture, float fu, float fv)
	{
		return m_pEB->GetRenderer()->GetParticleManager()->SetSpecialPsTexture(m_handle, ShapesTexture, fu, fv);
	}
	// 获得特殊特效使用的纹理handle (方便外面删除等操作)
	PictureHandle ParticleEffect::GetSpecialPSTextureHandle() const
	{
		return m_pEB->GetRenderer()->GetParticleManager()->GetSpecialPSTextureHandle(m_handle);
	}
	// 设置特殊特效缩放比例(小图贴大屏幕或者反之..)
	bool ParticleEffect::SetSpecialPsScale(float fcx, float fcy)
	{
		return m_pEB->GetRenderer()->GetParticleManager()->SetSpecialPsScale(m_handle, fcx, fcy);
	}

	// 转场特效静默时间ftime,单位:秒 在这段时间内保持原始图片的状态。
	bool ParticleEffect::SetSilentTime(float ftime)
	{
		return m_pEB->GetRenderer()->GetParticleManager()->SetSilentTime(m_handle, ftime);
	}

	void ParticleEffect::Clone(Effect* p)
	{
		Effect::Clone(p);
		ParticleEffect* pDst = static_cast<ParticleEffect*>(p);
		pDst->SetVertexColor(m_vertexColor);
		pDst->SetLocation(m_pos);
		pDst->SetPlayMode(m_playMode);
		switch (m_playMode)
		{
		case XPPM_LOOP:
			pDst->SetLoopNum(-1);
			break;
		case XPPM_LOOPNUM:
			pDst->SetLoopNum(Effect::GetLoopNum());
			break;
		case XPPM_TIME:
			SetPlayTime(Effect::GetPlayTime());
			break;
		default:
			XPASSERT(false && L"ParticleEffect::LoadAttribute!");
			break;
		}
	}
}
