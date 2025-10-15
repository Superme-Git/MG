#include "nuparticlesystem.h"

namespace Nuclear
{
	const BLEND_MODE g_BlendingModes[8] = 
	{
		{ L"D3DTOP_MODULATE", 4 },
		{ L"D3DTOP_SELECTARG1", 2 },
		{ L"D3DTOP_SELECTARG2", 3 },
		{ L"D3DTOP_ADD", 7 },
		{ L"D3DTOP_SUBTRACT", 10 },
		{ L"D3DTOP_ADDSMOOTH", 11 },
		{ L"D3DTOP_ADDSIGNED", 8 },
		{ L"D3DTOP_MODULATE2X", 5 }
	};

	float XP_POWF[11][42]; //存一半，另一般数据通过对称得到

	InitStaticVar::InitStaticVar(void)
	{
		float fdis = 1.0f/40;
		float fvar = 2.0f/20;
		for(int i=0; i<11; i++)
		{
			for(int j=0; j<41; j++)
			{
				XP_POWF[i][j] = powf(j*fdis, i*fvar);
			}
		
			XP_POWF[i][41] = XP_POWF[i][40]; //为了减少查表时候的if...else...
		}
	}
	InitStaticVar::~InitStaticVar(void) {}
	static InitStaticVar initStaticVar;

	CParticleSystem::CParticleSystem(const NuclearHardRef<PSL>& RefPsl, const PSParameter& psParameter, IBaseRenderer *pRenderer, const std::wstring& pslName)
		:m_ppsl(RefPsl)
		,m_particleArray(m_ppsl->m_nSysMaxParticles)
	{
		m_psParameter = psParameter;

		m_pRenderer = pRenderer;
		m_strPslName = pslName;

		m_fNumNewPartsExcess = 0.0f;
		m_nNumReLifePartsExcess = 0;

		m_fLastTime = 0.0f;

		m_fSysAge = 0.0f;
		m_POINTdxdy = NuclearFPoint(0.0f, 0.0f);
		m_fParticleAngle = 0.0f;
		m_ViewPortdxdy = NuclearFPoint(0.0f, 0.0f);
        m_preViewPort = NuclearRect(0, 0, 512, 768);
		m_curViewPort = NuclearRect(0, 0, 512, 768);

		m_bStop = false;

		m_pointCent0 = psParameter.m_psModifyParam.m_pointCent;

		SetEmitterProperty();
	}

	CParticleSystem::~CParticleSystem()
	{
	}

	void CParticleSystem::SetViewPort(const NuclearRect& ViewPort)
	{
		m_preViewPort = m_curViewPort;
		m_curViewPort = ViewPort;
		m_ViewPortdxdy.x = (float)(m_curViewPort.left-m_preViewPort.left);
		m_ViewPortdxdy.y = (float)(m_curViewPort.top-m_preViewPort.top);
		if( m_psParameter.m_bScreenCoord ) //界面特效应该忽略视口参数
		{
			m_ViewPortdxdy.x = 0.0f;
			m_ViewPortdxdy.y = 0.0f;
		}
	}

	void CParticleSystem::Reset(void) 
	{
		m_particleArray.DeleteAll();
		SetSysAge(0.0f);
		SetEmitterProperty();

		m_fNumNewPartsExcess = 0.0f; //小数部分累加保存
		m_nNumReLifePartsExcess = 0; //复活粒子数
	}

	void CParticleSystem::SetEmitterProperty()
	{
		//系统级属性..不随时间而变化
		m_fGravity = RandomNumber(m_ppsl->m_fGravity-m_ppsl->m_fGravityRand, m_ppsl->m_fGravity+m_ppsl->m_fGravityRand);
		m_fRadialAcc = RandomNumber(m_ppsl->m_fRadialAcc-m_ppsl->m_fRadialAccRand, m_ppsl->m_fRadialAcc+m_ppsl->m_fRadialAccRand);
		m_fTangAcc = RandomNumber(m_ppsl->m_fTangAcc-m_ppsl->m_fTangAccRand, m_ppsl->m_fTangAcc+m_ppsl->m_fTangAccRand);

		m_fGravity *= 10;
		m_fRadialAcc *= 10;
		m_fTangAcc *= 10;
	}

	void CParticleSystem::InitParticleAlphaColor(CParticle* part)
	{			
		unsigned char aStr = (unsigned char)(m_ppsl->m_nParAlphaStrt);
		unsigned char rStr = (unsigned char)(m_ppsl->m_nParColorStrtR);
		unsigned char gStr = (unsigned char)(m_ppsl->m_nParColorStrtG);
		unsigned char bStr = (unsigned char)(m_ppsl->m_nParColorStrtB);

		unsigned char aEnd = (unsigned char)(m_ppsl->m_nParAlphaEnd);
		unsigned char rEnd = (unsigned char)(m_ppsl->m_nParColorEndR);
		unsigned char gEnd = (unsigned char)(m_ppsl->m_nParColorEndG);
		unsigned char bEnd = (unsigned char)(m_ppsl->m_nParColorEndB);

		part->m_fParAlphaMidTime = m_ppsl->m_fParAlphaMidTime;
		part->m_nAlphaMid = m_ppsl->m_nParAlphaMid;
		part->m_fAlphaVar = m_ppsl->m_fParAlphaVar;
		part->m_fColorVar = m_ppsl->m_fParColorVar;

		if(m_ppsl->m_nParticleColorChange)
		{
			part->m_colorStrt.Reset(bStr, gStr, rStr, aStr);
			part->m_colorEnd.Reset(bEnd, gEnd, rEnd, aEnd);
		}
		else
		{//不渐变...指的是颜色rgb不变化, alpha还是要变化的...
			unsigned char r = 0;
			unsigned char g = 0;
			unsigned char b = 0;
			unsigned char a = 0;

			if(m_ppsl->m_nParticleColorRand)
			{ //色彩随机，与A、B颜色无关了
				r = RandomNumber(0, 255);
				g = RandomNumber(0, 255);
				b = RandomNumber(0, 255);
			}
			else
			{
				float colorIndex = RandomNumber(0.0f, 1.0f);
				r = (unsigned char)(colorIndex *rStr + (1.0f-colorIndex) *rEnd);
				g = (unsigned char)(colorIndex *gStr + (1.0f-colorIndex) *gEnd);
				b = (unsigned char)(colorIndex *bStr + (1.0f-colorIndex) *bEnd);
			}

			//地图叠加颜色需要...这里修改程序传入颜色的处理逻辑
			//0xff000001是以前的默认值..这个值可能在geffect里写到磁盘文件了。
			if( (m_psParameter.m_psModifyParam.m_dwColor != PSDEFAULTCOLOR) && (m_psParameter.m_psModifyParam.m_dwColor != 0xff000001) )
			{
				r = (unsigned char)(((m_psParameter.m_psModifyParam.m_dwColor & 0x00ff0000) >>16)/255.0f *r);
				g = (unsigned char)(((m_psParameter.m_psModifyParam.m_dwColor & 0x0000ff00) >>8) /255.0f *g);
				b = (unsigned char)((m_psParameter.m_psModifyParam.m_dwColor & 0x000000ff)/255.0f *b);
			}

			a = m_ppsl->m_nParAlphaStrt;
			part->m_colorStrt.Reset(b, g, r, a);
			a = m_ppsl->m_nParAlphaEnd;
			part->m_colorEnd.Reset(b, g, r, a);
		}
	}

}