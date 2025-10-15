#include "stdafx.h"
#include "..\common\log.h"
#include "..\common\xml.h"
#include "..\particlesystem\iparticlemanager.h"
#include "..\renderer\renderer.h"
#include "particleeffect.h"


namespace Nuclear
{
	ParticleEffect::ParticleEffect(EngineBase* pEB) : Effect(pEB), m_handle(INVALID_PS_HANDLE), m_nPlayOnceTime(0)
	{
	}

	ParticleEffect::~ParticleEffect(void)
	{
		if( m_handle != INVALID_PS_HANDLE )
		{
			m_pEngine->GetRenderer()->GetParticleManager()->DeleteParticleSystem(m_handle);
		}
	}

	void ParticleEffect::SetScaleType(XPEFFECT_SCALE_TYPE t)
	{
		if (m_parmeter.m_psModifyParam.m_scaleType != t)
		{
			m_parmeter.m_psModifyParam.m_scaleType = t;
			m_pEngine->GetRenderer()->GetParticleManager()->ModifyParticleSystem(m_handle, m_parmeter);
		}
	}

	void ParticleEffect::OnReady(bool success)
	{
		if (success)
		{
			m_state = XPES_STOP;
			this->Play();
		} else {
			m_pEngine->GetRenderer()->GetParticleManager()->DeleteParticleSystem(m_handle);
			m_handle = INVALID_PS_HANDLE;
			m_state = XPES_EMPTY;
		}
		NotifyAsyncLoadResult(success);
	}

	XPEffectAssureResResult ParticleEffect::AssureResource(bool async)
	{
		if (m_state != XPES_EMPTY)
			return XPEARR_RESREADY;
		if( m_handle != INVALID_PS_HANDLE )
		{
			async = async && m_pEngine->IsAsyncRead();
			switch (m_pEngine->GetRenderer()->GetParticleManager()->EnsureParticleSystemResouce(m_handle, async, this))
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
				m_pEngine->GetRenderer()->GetParticleManager()->DeleteParticleSystem(m_handle);
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
		m_pEngine->GetRenderer()->GetParticleManager()->ReleaseParticleSystemRes(m_handle);
		m_state = XPES_EMPTY;
	}

	int ParticleEffect::LoadAttribute(XMLIO::CINode& node)
	{
		m_parmeter.color = node.GetUIntAttribute( L"v_c");
		m_pos.x = node.GetIntAttribute( L"rel_x");
		m_pos.y = node.GetIntAttribute( L"rel_y");
		switch (node.GetIntAttribute( L"p_m"))
		{
		case XPPM_LOOP:
			SetLoopNum(-1);
			break;
		case XPPM_LOOPNUM:
			SetLoopNum(node.GetIntAttribute( L"l_n" ));
			break;
		case XPPM_TIME:
			SetPlayTime(node.GetIntAttribute( L"p_t"));
			break;
		default:
			XPASSERT(false && L"ParticleEffect::LoadAttribute!");
			break;
		}
		//��SetLoopNum����SetPlayTime����ᱣ���
		return 0;
	}

	int ParticleEffect::SaveAttribute(XMLIO::CONode& node)
	{
		node.SetAttribute( L"v_c", (unsigned int)m_parmeter.color);
		node.SetAttribute( L"rel_x", m_pos.x);
		node.SetAttribute( L"rel_y", m_pos.y);

		// ���ſ���
		node.SetAttribute( L"p_m", (int)m_playMode);
		if ( IsLoopNum())
			node.SetAttribute( L"l_n", m_nLoopNum);
		else if( IsPlayTime())
			node.SetAttribute( L"p_t", m_nPlayTime);		
		return 0;
	}

	bool ParticleEffect::Update(DWORD tickTime)
	{
		if (m_state != XPES_PLAYING) return true;
		if( m_handle == INVALID_PS_HANDLE )	return false;
		IParticleManager* pMgr = m_pEngine->GetRenderer()->GetParticleManager();
		
		if (m_nCurTime < 0)
		{
			m_nCurTime = 0;
			tickTime = 0;
		}
		m_nCurTime += tickTime;
		//�������Ӿͺ���m_translate��
		//λ��
		m_parmeter.m_psModifyParam.m_pointCent.x = m_pos.x + m_translate.x;
		m_parmeter.m_psModifyParam.m_pointCent.y = m_pos.y + m_translate.y;
		//��ת
		m_parmeter.m_psModifyParam.m_fEmitterSpinAngle = m_rotationRadian;
		//����
		m_parmeter.m_psModifyParam.m_fEmissionScale = m_scale.x;
		m_parmeter.m_psModifyParam.m_fParticlePosCoef = m_pEngine->GetWorldScale();
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

	void ParticleEffect::SetEffectBindType(XPEFFECT_BIND_TYPE t)
	{
		XPASSERT(false && L"���Ӳ������������û��ʵ�֣�");
	}

	XPEFFECT_BIND_TYPE ParticleEffect::GetEffectBindType() const
	{
		return m_pEngine->GetRenderer()->GetParticleManager()->GetEffectBindType(m_handle);
	}
	
	//! ��Ⱦ
	bool ParticleEffect::Render(Canvas* canvas)
	{
		if (m_state == XPES_STOP) 
			return false;
		if (m_state == XPES_EMPTY)
			return false;
		if (m_nCurTime < 0)
			return true;
		if (m_handle == INVALID_PS_HANDLE)
			return false;

		m_pEngine->GetRenderer()->GetParticleManager()->RenderParticleSystem(m_handle, true, true);
		return true;
	}

	//�����ռ��Թ�������
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

		m_pEngine->GetRenderer()->GetParticleManager()->RenderParticleSystem(m_handle, true, false);
		return true;
	}

	//����ѭ��ģʽ�������XPPCM_NO_CYCLE��return false��
	bool ParticleEffect::SetCycleMode(XPParticleEffectCycleMode mode)
	{
		if (mode == XPPCM_NO_CYCLE)
			return false;

		if (!Effect::SetLoopNum(-1))
			return false;
		if (mode == XPPCM_PULSE_EMISSION)
		{
			m_nLoopNum = -2;
			//���ӻ�����ѭ�������Ŷ��ٱ飬��ʵ������Reset���ٴΣ�ÿһ�鶼�ǵ��������Ӷ�����֮���ٷ���
			m_parmeter.cycle = false;
		} else {
			m_parmeter.cycle = true;
		}
		m_parmeter.time = PSDEFAULTTIME;
		m_pEngine->GetRenderer()->GetParticleManager()->ModifyParticleSystem(m_handle, m_parmeter);
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

	//��������ϵͳ���������ȣ��룩��-1����Ĭ�ϣ�ֻ��XPPCM_PULSE_EMISSION��ʱ��������
	bool ParticleEffect::SetSysLife(float time)
	{
		if (m_nLoopNum != -2)
			return false;
		m_parmeter.time = time;
		m_pEngine->GetRenderer()->GetParticleManager()->ModifyParticleSystem(m_handle, m_parmeter);
		return true;
	}

	float ParticleEffect::GetSysLife()
	{
		return m_pEngine->GetRenderer()->GetParticleManager()->GetSysLife(m_handle);
	}

	void ParticleEffect::SetLocation(const Location& location)
	{
		if (m_parmeter.m_bModifyLineEmitter)
		{
			CPOINT pt = location - m_pos;
			m_parmeter.m_psModifyParam.m_fpoint0 += pt;
			m_parmeter.m_psModifyParam.m_fpoint1 += pt;
		}
		Effect::SetLocation(location);
	}

	//�������Ч�����ӣ���ô���������������˵㣬type==0��������㣬type==1�����յ�
	//���������Ч�����ӣ���ô����false
	bool ParticleEffect::SetEmitterLinkPoint(const FPOINT &pt, int type)
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

	bool ParticleEffect::GetEmitterLinkPoint(FPOINT &pt, int type)
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

	//! С��0��ʾ����ѭ��
	bool ParticleEffect::SetLoopNum( int loopNum )
	{
		if (!Effect::SetLoopNum(loopNum))
			return false;
		//���ӻ�����ѭ�������Ŷ��ٱ飬��ʵ������Reset���ٴΣ�ÿһ�鶼�ǵ��������Ӷ�����֮���ٷ���
		m_parmeter.time = PSDEFAULTTIME;
		m_parmeter.cycle = loopNum<0?true:false;
		m_pEngine->GetRenderer()->GetParticleManager()->
			ModifyParticleSystem(m_handle, m_parmeter);
		m_nPlayOnceTime = static_cast<int>((GetSysLife() + GetParticleMaxLife()) * 1000);
		return true;
	}

	bool ParticleEffect::SetPlayTime( DWORD time) //����
	{
		if ( !Effect::SetPlayTime( time))
			return false;
		float fPartMaxLife = GetParticleMaxLife();
		m_parmeter.time = static_cast<float>(time) / 1000 - fPartMaxLife;
		if (m_parmeter.time < 0.0f)
		{
			m_parmeter.time = 0.1f; //����������ϵͳ����0.1��
		}
		m_parmeter.cycle = false;
		m_pEngine->GetRenderer()->GetParticleManager()->
			ModifyParticleSystem(m_handle, m_parmeter);
		m_nPlayOnceTime = static_cast<int>((m_parmeter.time + fPartMaxLife) * 1000);
		return true;
	}

	bool ParticleEffect::GetBase(XPBase &base)
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
			m_pEngine->GetRenderer()->GetParticleManager()->ModifyParticleSystem(m_handle, m_parmeter);
		}
	}

	void ParticleEffect::SetVertexColor( XPCOLOR color)
	{
		if (m_parmeter.color != color.data)
		{
			m_parmeter.color = color.data;
			m_pEngine->GetRenderer()->GetParticleManager()->ModifyParticleSystem(m_handle, m_parmeter);
		}
	}

	void ParticleEffect::SetTransparent( float alpha)
	{
		XPCOLOR color(m_parmeter.color);
		color.a = static_cast<unsigned char>(alpha * 255);
		SetVertexColor(color);
	}

	bool ParticleEffect::Play()
	{
		if (Effect::Play())
		{
			m_pEngine->GetRenderer()->GetParticleManager()->ResetParticleSystem(m_handle);
			return true;
		}
		return false;
	}

	// ���ָ��������Ч��ǰ����Ⱦ��������
	int ParticleEffect::GetNumActiveParticles() const
	{
		return m_pEngine->GetRenderer()->GetParticleManager()->GetNumActiveParticles(m_handle);
	}

	// ��ð�Χ�е���Ϣ..
	PSINFO ParticleEffect::GetParticleSystemInfo() const
	{
		return m_pEngine->GetRenderer()->GetParticleManager()->GetParticleSystemInfo(m_handle);
	}

	// ���ӵĿ��ܴ�����ʱ��(��)
	float ParticleEffect::GetParticleMaxLife() const 
	{
		return m_pEngine->GetRenderer()->GetParticleManager()->GetParticleMaxLife(m_handle);
	}

	//�����������������Ч������..
	// ��������, �Լ���������u��v����(���Կ��ϴ���û��������˵ķ�2������)
	bool ParticleEffect::SetSpecialPsTexture(PictureHandle ShapesTexture, float fu, float fv)
	{
		return m_pEngine->GetRenderer()->GetParticleManager()->SetSpecialPsTexture(m_handle, ShapesTexture, fu, fv);
	}
	// ���������Чʹ�õ�����handle (��������ɾ���Ȳ���)
	PictureHandle ParticleEffect::GetSpecialPSTextureHandle() const
	{
		return m_pEngine->GetRenderer()->GetParticleManager()->GetSpecialPSTextureHandle(m_handle);
	}
	// ����������Ч���ű���(Сͼ������Ļ���߷�֮..)
	bool ParticleEffect::SetSpecialPsScale(float fcx, float fcy)
	{
		return m_pEngine->GetRenderer()->GetParticleManager()->SetSpecialPsScale(m_handle, fcx, fcy);
	}

	// ת����Ч��Ĭʱ��ftime,��λ:�� �����ʱ���ڱ���ԭʼͼƬ��״̬��
	bool ParticleEffect::SetSilentTime(float ftime)
	{
		return m_pEngine->GetRenderer()->GetParticleManager()->SetSilentTime(m_handle, ftime);
	}
}
