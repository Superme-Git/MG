#include "stdafx.h"
#include "particlesystem.h"
#include <Shlwapi.h>

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

	//	XP_POWF[0][0] = 0.0f; //powf(0, 0) == 1 这与实际应用需求匹配吗？09--9--14
	}
	InitStaticVar::~InitStaticVar(void) {}
	static InitStaticVar initStaticVar;

	CParticleSystem::CParticleSystem(const XHardRef<PSL>& RefPsl, const PSParameter& psParameter, IBaseRenderer *pRenderer,
									 const std::wstring& pslName)
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
		m_POINTdxdy = FPOINT(0.0f, 0.0f);
		m_fParticleAngle = 0.0f;
		m_ViewPortdxdy = FPOINT(0.0f, 0.0f);
		m_preViewPort = CRECT(0, 0, GetSystemMetrics(SM_CXSCREEN), GetSystemMetrics(SM_CYSCREEN));
		m_curViewPort = CRECT(0, 0, GetSystemMetrics(SM_CXSCREEN), GetSystemMetrics(SM_CYSCREEN));

		m_bStop = false;

		m_pointCent0 = psParameter.m_psModifyParam.m_pointCent;

		SetEmitterProperty();
	}

	CParticleSystem::~CParticleSystem()
	{
	}

	void CParticleSystem::SetViewPort(const CRECT& ViewPort)
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
	//	m_particleArray.Resize(m_ppsl->m_nSysMaxParticles); //特殊特效在初始化shapelist的时候会修改m_nSysMaxParticles。
		
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
			//	r = RandomNumber(rStr, rEnd);
			//	g = RandomNumber(gStr, gEnd);
			//	b = RandomNumber(bStr, bEnd);
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

			if(m_psParameter.color != PSDEFAULTCOLOR)
			{
				a = (unsigned char)((m_psParameter.color & 0xff000000) >>24);
				r = (unsigned char)((m_psParameter.color & 0x00ff0000) >>16);
				g = (unsigned char)((m_psParameter.color & 0x0000ff00) >>8);
				b = (unsigned char)(m_psParameter.color & 0x000000ff);
			}

			a = m_ppsl->m_nParAlphaStrt;
			part->m_colorStrt.Reset(b, g, r, a);
			a = m_ppsl->m_nParAlphaEnd;
			part->m_colorEnd.Reset(b, g, r, a);
		}
	}

	bool CParticleSystem::SavePSL(LPCWSTR strFilename)
	{
		FILE *file = NULL;
		_tfopen_s(&file, strFilename, L"wb");
		if( file==NULL ) return false;

		_ftprintf(file, L"[ParticleSystem 4.00]\n"); 
							//1.00,重力等 三个加速度是用的min--max...bug--升级到2.00
							//3.00,随机角度产生之后随机旋转- //RandAngle为true时恢复SpinRand的作用
							
		WriteProperty(file, L"nAbsrtactPsl", m_ppsl->m_nAbsrtactPsl);

		WriteProperty(file, L"strVtxsObjectPath", m_ppsl->m_strVtxsObjectPath.c_str());

		WriteProperty(file, L"nDirMotion", m_ppsl->m_nDirMotion);
		WriteProperty(file, L"nPSConfigReq", m_ppsl->m_nPSConfigReq);
		WriteProperty(file, L"bPathOrder", m_ppsl->m_nPathOrder);
		WriteProperty(file, L"bUVmirrorh", m_ppsl->m_nUVmirrorh);
		WriteProperty(file, L"bUVmirrorv", m_ppsl->m_nUVmirrorv);
		WriteProperty(file, L"bParticleColorChange", m_ppsl->m_nParticleColorChange);
		WriteProperty(file, L"bParticleColorRand", m_ppsl->m_nParticleColorRand);
		WriteProperty(file, L"bTrackFx", m_ppsl->m_nTrackFx);

		if(m_ppsl->m_nPosType==PSLTYPE_CUSTOMPATH)
			WriteProperty( file, L"strFullPathFile", m_ppsl->m_strFullPathFile.c_str() );
		// 		int cnt = m_vectorPathPoint.size();
		// 		WriteProperty(file, L"nPathPointSize", cnt);
		// 
		// 		std::wstring str = L"PathPoint";
		// 		std::wstring str1;
		// 		WCHAR exname[4];
		// 		for(int i=0; i<cnt; i++)
		// 		{
		// 			//errno_t _itow_s( int value, wchar_t *buffer, size_t sizeInCharacters, int radix ); // <stdlib.h>
		// 			memset(exname, 0, 4*sizeof(WCHAR));
		// 			_itow_s(i, exname, 4, 10); //*sizeof(WCHAR)
		// 			str1 = str+exname;
		// 
		// 			WriteProperty( file, str1.c_str(), m_vectorPathPoint[i] );
		// 		}

		WriteProperty(file, L"bEmitterType", m_ppsl->m_nEmitterType);
		WriteProperty(file, L"bSingleParticleAni", m_ppsl->m_nSingleParticleAni);
		WriteProperty(file, L"bParticleAniTextureIndex1", m_ppsl->m_nParticleAniTextureIndex1);
		WriteProperty(file, L"nAniPlaySpeed", m_ppsl->m_nAniPlaySpeed);
		WriteProperty(file, L"fAniSpeedRand", m_ppsl->m_fAniSpeedRand);
		WriteProperty(file, L"bParFillMode", m_ppsl->m_nParFillMode);
		WriteProperty(file, L"nPosType", m_ppsl->m_nPosType);
		WriteProperty(file, L"nParticlePathInitPosType", m_ppsl->m_nParticlePathInitPosType);
		WriteProperty(file, L"nParticleSizeRandType", m_ppsl->m_nParticleSizeRandType);
		WriteProperty(file, L"nMoveType", m_ppsl->m_nMoveType);
		WriteProperty(file, L"fPointX0", m_ppsl->m_point0.x);
		WriteProperty(file, L"fPointY0", m_ppsl->m_point0.y);
		WriteProperty(file, L"fPointX1", m_ppsl->m_point1.x);
		WriteProperty(file, L"fPointY1", m_ppsl->m_point1.y);
		WriteProperty(file, L"pointEmitterSpinCentX", m_ppsl->m_pointEmitterSpinCent.x);
		WriteProperty(file, L"pointEmitterSpinCentY", m_ppsl->m_pointEmitterSpinCent.y);
		WriteProperty(file, L"pointParticleHoleX", m_ppsl->m_pointParticleHole.x);
		WriteProperty(file, L"pointParticleHoleY", m_ppsl->m_pointParticleHole.y);
		WriteProperty(file, L"nSysPosRadiusX", m_ppsl->m_nSysPosRadiusX);
		WriteProperty(file, L"nSysPosRadiusY", m_ppsl->m_nSysPosRadiusY);

		WriteProperty(file, L"nRctLeft", m_ppsl->m_rct.left);
		WriteProperty(file, L"nRctTop", m_ppsl->m_rct.top);
		WriteProperty(file, L"nRctRight", m_ppsl->m_rct.right);
		WriteProperty(file, L"nRctBottom", m_ppsl->m_rct.bottom);

		WriteProperty(file, L"nSysMaxParticles", m_ppsl->m_nSysMaxParticles);

		WriteProperty(file, L"fPathZoom", m_ppsl->m_fPathZoom);
		WriteProperty(file, L"fPathSpin", m_ppsl->m_fPathSpin);
		WriteProperty(file, L"nPathMirrorType", m_ppsl->m_nPathMirrorType);
		WriteProperty(file, L"nNumPointFullPath", m_ppsl->m_nNumPointFullPath);
		WriteProperty(file, L"fSysLife", m_ppsl->m_fSysLife);
		WriteProperty(file, L"nEmissionRate", m_ppsl->m_nEmissionRate);
		WriteProperty(file, L"nEmitterSpeed", m_ppsl->m_nEmitterSpeed);
		WriteProperty(file, L"fParticleLifeMin", m_ppsl->m_fParticleLifeMin);
		WriteProperty(file, L"fParticleLifeMax", m_ppsl->m_fParticleLifeMax);
		WriteProperty(file, L"fEmissionDirection", m_ppsl->m_fEmissionDirection);
		WriteProperty(file, L"fEmissionSpread", m_ppsl->m_fEmissionSpread);
		WriteProperty(file, L"fSpeedMin", m_ppsl->m_fSpeedMin);
		WriteProperty(file, L"fSpeedMax", m_ppsl->m_fSpeedMax);
		WriteProperty(file, L"fGravity", m_ppsl->m_fGravity);
		WriteProperty(file, L"fGravityRand", m_ppsl->m_fGravityRand);
		WriteProperty(file, L"fRadialAcc", m_ppsl->m_fRadialAcc);
		WriteProperty(file, L"fRadialAccRand", m_ppsl->m_fRadialAccRand);
		WriteProperty(file, L"fTangAcc", m_ppsl->m_fTangAcc);
		WriteProperty(file, L"fTangAccRand", m_ppsl->m_fTangAccRand);
		WriteProperty(file, L"fParSizeStrtX", m_ppsl->m_fParSizeStrtX);
		WriteProperty(file, L"fParSizeStrtY", m_ppsl->m_fParSizeStrtY);
		WriteProperty(file, L"fParSizeEndX", m_ppsl->m_fParSizeEndX);
		WriteProperty(file, L"fParSizeEndY", m_ppsl->m_fParSizeEndY);
		WriteProperty(file, L"fParSizeVar", m_ppsl->m_fParSizeVar);
		WriteProperty(file, L"fParSpinStrt", m_ppsl->m_fParSpinStrt);
		WriteProperty(file, L"fParSpinEnd", m_ppsl->m_fParSpinEnd);
		WriteProperty(file, L"fParSpinVar", m_ppsl->m_fParSpinVar);

		WriteProperty(file, L"fRotateCentX", m_ppsl->m_fRotateCentX);
		WriteProperty(file, L"fRotateCentY", m_ppsl->m_fRotateCentY);

		WriteProperty(file, L"fParAngleStrt", m_ppsl->m_fParAngleStrt);
		WriteProperty(file, L"fParAngleEnd", m_ppsl->m_fParAngleEnd);
		WriteProperty(file, L"fParAngleVar", m_ppsl->m_fParAngleVar);

		WriteProperty(file, L"nParAlphaStrt", m_ppsl->m_nParAlphaStrt);
		WriteProperty(file, L"nParAlphaEnd", m_ppsl->m_nParAlphaEnd);
		WriteProperty(file, L"fParAlphaVar", m_ppsl->m_fParAlphaVar);
		WriteProperty(file, L"nParAlphaMid", m_ppsl->m_nParAlphaMid);
		WriteProperty(file, L"fParAlphaMidTime", m_ppsl->m_fParAlphaMidTime);

		WriteProperty(file, L"fSizeRandom", m_ppsl->m_fSizeRandom);
		WriteProperty(file, L"fSpinRandom", m_ppsl->m_fSpinRandom);
		WriteProperty(file, L"fAngleRandom", m_ppsl->m_fAngleRandom);
		WriteProperty(file, L"fAlignToMotionAngle", m_ppsl->m_fAlignToMotionAngle);
		WriteProperty(file, L"nAlignToMotion", m_ppsl->m_nAlignToMotion);
		WriteProperty(file, L"nAlignToPath", m_ppsl->m_nAlignToPath);
		WriteProperty(file, L"nMotionToPath", m_ppsl->m_nMotionToPath);
		WriteProperty(file, L"nAlignToEmitter", m_ppsl->m_nAlignToEmitter);
		WriteProperty(file, L"nAlignToHole", m_ppsl->m_nAlignToHole);
		WriteProperty(file, L"nParticleRandAngle", m_ppsl->m_nParticleRandAngle);
		WriteProperty(file, L"nParticleRandAngleSelf", m_ppsl->m_nParticleRandAngleSelf);

		WriteProperty(file, L"nParColorStrtR", m_ppsl->m_nParColorStrtR);
		WriteProperty(file, L"nParColorStrtG", m_ppsl->m_nParColorStrtG);
		WriteProperty(file, L"nParColorStrtB", m_ppsl->m_nParColorStrtB);
		WriteProperty(file, L"nParColorEndR", m_ppsl->m_nParColorEndR);
		WriteProperty(file, L"nParColorEndG", m_ppsl->m_nParColorEndG);
		WriteProperty(file, L"nParColorEndB", m_ppsl->m_nParColorEndB);
		WriteProperty(file, L"fParColorVar", m_ppsl->m_fParColorVar);
		WriteProperty(file, L"fPSLzoom", m_ppsl->m_fPSLzoom);
		WriteProperty(file, L"fEmitterSpin", m_ppsl->m_fEmitterSpin);

		WriteProperty(file, L"nDestBlendMode", m_ppsl->m_nDestBlendMode);
		WriteBlendModeProperty(file, L"BlendMode", m_ppsl->m_nBlendMode);

		int cnt = (int)m_ppsl->m_vectorTexturePath.size();
		WriteProperty(file, L"TextureNum", cnt);

		std::wstring str = L"Texture";
		std::wstring str1;
		WCHAR lpstr[3];
		for(int i=0; i<cnt; i++)
		{
			memset(lpstr, 0, 3*sizeof(WCHAR));
			_itow_s(i, lpstr, 3, 10);
			str1 = str+lpstr;
			std::wstring strTmp = m_ppsl->m_vectorTexturePath[i];
			if (strTmp.find(L"/effect/particle/texture/") != std::wstring::npos)
			{
				size_t nPos = 25;//strTmp.rfind(L"/effect/particle/texture/");
				strTmp = strTmp.substr(nPos, strTmp.length()-nPos);
			}
			WriteProperty( file, str1.c_str(), strTmp.c_str() );
		}

		WriteProperty(file, L"nRowNum", m_ppsl->m_nRowNum);
		WriteProperty(file, L"nColNum", m_ppsl->m_nColNum);
		WriteProperty(file, L"nTextureNum", m_ppsl->m_nTextureNum);
		WriteProperty(file, L"rctMaxBoxL", m_ppsl->m_rctMaxBox.left);
		WriteProperty(file, L"rctMaxBoxT", m_ppsl->m_rctMaxBox.top);
		WriteProperty(file, L"rctMaxBoxR", m_ppsl->m_rctMaxBox.right);
		WriteProperty(file, L"rctMaxBoxB", m_ppsl->m_rctMaxBox.bottom);

		WriteProperty(file, L"nInterfacePsl", m_ppsl->m_nInterfacePsl);
		WriteProperty(file, L"ptInterfacialCenterX", m_ppsl->m_ptInterfacialCenter.x);
		WriteProperty(file, L"ptInterfacialCenterY", m_ppsl->m_ptInterfacialCenter.y);
		WriteProperty(file, L"nSkillEffectSpritePoint", m_ppsl->m_nSkillEffectSpritePoint);

		fclose(file);

		return true;
	}
	bool CParticleSystem::SavePSLEx(LPCWSTR strFilename)
	{
		bool bResult = SavePSLToFile(strFilename, m_ppsl);
		return bResult;
	}
	bool CParticleSystem::SavePSLToFile(std::wstring FN, const XHardRef<PSL>& ppsl)
	{
		std::ofstream FS(ws2s(FN).c_str(), std::ios_base::binary);
		if (FS.is_open() == false)
		{
			return false;
		}
		FS.seekp(0, std::ios_base::beg);
		bool bResult = SavePSLToStream(FS, ppsl);
		FS.close();
		return bResult;
	}
	bool CParticleSystem::SavePSLToStream(std::ofstream& FS, const XHardRef<PSL>& ppsl)
	{
		int iVal = 4;
		WriteData(FS, iVal);
		WriteData(FS, ppsl->m_nAbsrtactPsl);//WriteProperty(file, L"nAbsrtactPsl", ppsl->m_nAbsrtactPsl);
		WriteData(FS, ws2s(ppsl->m_strVtxsObjectPath));//WriteProperty(file, L"strVtxsObjectPath", ppsl->m_strVtxsObjectPath.c_str());
		WriteData(FS, ppsl->m_nDirMotion);//WriteProperty(file, L"nDirMotion", ppsl->m_nDirMotion);
		WriteData(FS, ppsl->m_nPSConfigReq);//WriteProperty(file, L"nPSConfigReq", ppsl->m_nPSConfigReq);
		WriteData(FS, ppsl->m_nPathOrder);//WriteProperty(file, L"bPathOrder", ppsl->m_nPathOrder);
		WriteData(FS, ppsl->m_nUVmirrorh);//WriteProperty(file, L"bUVmirrorh", ppsl->m_nUVmirrorh);
		WriteData(FS, ppsl->m_nUVmirrorv);//WriteProperty(file, L"bUVmirrorv", ppsl->m_nUVmirrorv);
		WriteData(FS, ppsl->m_nParticleColorChange);//WriteProperty(file, L"bParticleColorChange", ppsl->m_nParticleColorChange);
		WriteData(FS, ppsl->m_nParticleColorRand);//WriteProperty(file, L"bParticleColorRand", ppsl->m_nParticleColorRand);
		WriteData(FS, ppsl->m_nTrackFx);//WriteProperty(file, L"bTrackFx", ppsl->m_nTrackFx);
		WriteData(FS, ppsl->m_nEmitterType);//WriteProperty(file, L"bEmitterType", ppsl->m_nEmitterType);
		WriteData(FS, ppsl->m_nSingleParticleAni);//WriteProperty(file, L"bSingleParticleAni", ppsl->m_nSingleParticleAni);
		WriteData(FS, ppsl->m_nParticleAniTextureIndex1);//WriteProperty(file, L"bParticleAniTextureIndex1", ppsl->m_nParticleAniTextureIndex1);
		WriteData(FS, ppsl->m_nAniPlaySpeed);//WriteProperty(file, L"nAniPlaySpeed", ppsl->m_nAniPlaySpeed);
		WriteData(FS, ppsl->m_fAniSpeedRand);//WriteProperty(file, L"fAniSpeedRand", ppsl->m_fAniSpeedRand);
		WriteData(FS, ppsl->m_nParFillMode);//WriteProperty(file, L"bParFillMode", ppsl->m_nParFillMode);
		WriteData(FS, ppsl->m_nPosType);//WriteProperty(file, L"nPosType", ppsl->m_nPosType);
		if(ppsl->m_nPosType == PSLTYPE_CUSTOMPATH)
		{
			WriteData(FS, ws2s(ppsl->m_strFullPathFile));//WriteProperty( file, L"strFullPathFile", ppsl->m_strFullPathFile.c_str());
		}
		WriteData(FS, ppsl->m_nParticlePathInitPosType);//WriteProperty(file, L"nParticlePathInitPosType", ppsl->m_nParticlePathInitPosType);
		WriteData(FS, ppsl->m_nParticleSizeRandType);//WriteProperty(file, L"nParticleSizeRandType", ppsl->m_nParticleSizeRandType);
		WriteData(FS, ppsl->m_nMoveType);//WriteProperty(file, L"nMoveType", ppsl->m_nMoveType);
		WriteData(FS, ppsl->m_point0.x);//WriteProperty(file, L"fPointX0", ppsl->m_point0.x);
		WriteData(FS, ppsl->m_point0.y);//WriteProperty(file, L"fPointY0", ppsl->m_point0.y);
		WriteData(FS, ppsl->m_point1.x);//WriteProperty(file, L"fPointX1", ppsl->m_point1.x);
		WriteData(FS, ppsl->m_point1.y);//WriteProperty(file, L"fPointY1", ppsl->m_point1.y);
		WriteData(FS, ppsl->m_pointEmitterSpinCent.x);//WriteProperty(file, L"pointEmitterSpinCentX", ppsl->m_pointEmitterSpinCent.x);
		WriteData(FS, ppsl->m_pointEmitterSpinCent.y);//WriteProperty(file, L"pointEmitterSpinCentY", ppsl->m_pointEmitterSpinCent.y);
		WriteData(FS, ppsl->m_pointParticleHole.x);//WriteProperty(file, L"pointParticleHoleX", ppsl->m_pointParticleHole.x);
		WriteData(FS, ppsl->m_pointParticleHole.y);//WriteProperty(file, L"pointParticleHoleY", ppsl->m_pointParticleHole.y);
		WriteData(FS, ppsl->m_nSysPosRadiusX);//WriteProperty(file, L"nSysPosRadiusX", ppsl->m_nSysPosRadiusX);
		WriteData(FS, ppsl->m_nSysPosRadiusY);//WriteProperty(file, L"nSysPosRadiusY", ppsl->m_nSysPosRadiusY);
		WriteData(FS, (int)(ppsl->m_rct.left));//WriteProperty(file, L"nRctLeft", ppsl->m_rct.left);
		WriteData(FS, (int)(ppsl->m_rct.top));//WriteProperty(file, L"nRctTop", ppsl->m_rct.top);
		WriteData(FS, (int)(ppsl->m_rct.right));//WriteProperty(file, L"nRctRight", ppsl->m_rct.right);
		WriteData(FS, (int)(ppsl->m_rct.bottom));//WriteProperty(file, L"nRctBottom", ppsl->m_rct.bottom);
		WriteData(FS, ppsl->m_nSysMaxParticles);//WriteProperty(file, L"nSysMaxParticles", ppsl->m_nSysMaxParticles);
		WriteData(FS, ppsl->m_fPathZoom);//WriteProperty(file, L"fPathZoom", ppsl->m_fPathZoom);
		WriteData(FS, ppsl->m_fPathSpin);//WriteProperty(file, L"fPathSpin", ppsl->m_fPathSpin);
		WriteData(FS, ppsl->m_nPathMirrorType);//WriteProperty(file, L"nPathMirrorType", ppsl->m_nPathMirrorType);
		WriteData(FS, ppsl->m_nNumPointFullPath);//WriteProperty(file, L"nNumPointFullPath", ppsl->m_nNumPointFullPath);
		WriteData(FS, ppsl->m_fSysLife);//WriteProperty(file, L"fSysLife", ppsl->m_fSysLife);
		WriteData(FS, ppsl->m_nEmissionRate);//WriteProperty(file, L"nEmissionRate", ppsl->m_nEmissionRate);
		WriteData(FS, ppsl->m_nEmitterSpeed);//WriteProperty(file, L"nEmitterSpeed", ppsl->m_nEmitterSpeed);
		WriteData(FS, ppsl->m_fParticleLifeMin);//WriteProperty(file, L"fParticleLifeMin", ppsl->m_fParticleLifeMin);
		WriteData(FS, ppsl->m_fParticleLifeMax);//WriteProperty(file, L"fParticleLifeMax", ppsl->m_fParticleLifeMax);
		WriteData(FS, ppsl->m_fEmissionDirection);//WriteProperty(file, L"fEmissionDirection", ppsl->m_fEmissionDirection);
		WriteData(FS, ppsl->m_fEmissionSpread);//WriteProperty(file, L"fEmissionSpread", ppsl->m_fEmissionSpread);
		WriteData(FS, ppsl->m_fSpeedMin);//WriteProperty(file, L"fSpeedMin", ppsl->m_fSpeedMin);
		WriteData(FS, ppsl->m_fSpeedMax);//WriteProperty(file, L"fSpeedMax", ppsl->m_fSpeedMax);
		WriteData(FS, ppsl->m_fGravity);//WriteProperty(file, L"fGravity", ppsl->m_fGravity);
		WriteData(FS, ppsl->m_fGravityRand);//WriteProperty(file, L"fGravityRand", ppsl->m_fGravityRand);
		WriteData(FS, ppsl->m_fRadialAcc);//WriteProperty(file, L"fRadialAcc", ppsl->m_fRadialAcc);
		WriteData(FS, ppsl->m_fRadialAccRand);//WriteProperty(file, L"fRadialAccRand", ppsl->m_fRadialAccRand);
		WriteData(FS, ppsl->m_fTangAcc);//WriteProperty(file, L"fTangAcc", ppsl->m_fTangAcc);
		WriteData(FS, ppsl->m_fTangAccRand);//WriteProperty(file, L"fTangAccRand", ppsl->m_fTangAccRand);
		WriteData(FS, ppsl->m_fParSizeStrtX);//WriteProperty(file, L"fParSizeStrtX", ppsl->m_fParSizeStrtX);
		WriteData(FS, ppsl->m_fParSizeStrtY);//WriteProperty(file, L"fParSizeStrtY", ppsl->m_fParSizeStrtY);
		WriteData(FS, ppsl->m_fParSizeEndX);//WriteProperty(file, L"fParSizeEndX", ppsl->m_fParSizeEndX);
		WriteData(FS, ppsl->m_fParSizeEndY);//WriteProperty(file, L"fParSizeEndY", ppsl->m_fParSizeEndY);
		WriteData(FS, ppsl->m_fParSizeVar);//WriteProperty(file, L"fParSizeVar", ppsl->m_fParSizeVar);
		WriteData(FS, ppsl->m_fParSpinStrt);//WriteProperty(file, L"fParSpinStrt", ppsl->m_fParSpinStrt);
		WriteData(FS, ppsl->m_fParSpinEnd);//WriteProperty(file, L"fParSpinEnd", ppsl->m_fParSpinEnd);
		WriteData(FS, ppsl->m_fParSpinVar);//WriteProperty(file, L"fParSpinVar", ppsl->m_fParSpinVar);
		WriteData(FS, ppsl->m_fRotateCentX);//WriteProperty(file, L"fRotateCentX", ppsl->m_fRotateCentX);
		WriteData(FS, ppsl->m_fRotateCentY);//WriteProperty(file, L"fRotateCentY", ppsl->m_fRotateCentY);
		WriteData(FS, ppsl->m_fParAngleStrt);//WriteProperty(file, L"fParAngleStrt", ppsl->m_fParAngleStrt);
		WriteData(FS, ppsl->m_fParAngleEnd);//WriteProperty(file, L"fParAngleEnd", ppsl->m_fParAngleEnd);
		WriteData(FS, ppsl->m_fParAngleVar);//WriteProperty(file, L"fParAngleVar", ppsl->m_fParAngleVar);
		WriteData(FS, ppsl->m_nParAlphaStrt);//WriteProperty(file, L"nParAlphaStrt", ppsl->m_nParAlphaStrt);
		WriteData(FS, ppsl->m_nParAlphaEnd);//WriteProperty(file, L"nParAlphaEnd", ppsl->m_nParAlphaEnd);
		WriteData(FS, ppsl->m_fParAlphaVar);//WriteProperty(file, L"fParAlphaVar", ppsl->m_fParAlphaVar);
		WriteData(FS, ppsl->m_nParAlphaMid);//WriteProperty(file, L"nParAlphaMid", ppsl->m_nParAlphaMid);
		WriteData(FS, ppsl->m_fParAlphaMidTime);//WriteProperty(file, L"fParAlphaMidTime", ppsl->m_fParAlphaMidTime);
		WriteData(FS, ppsl->m_fSizeRandom);//WriteProperty(file, L"fSizeRandom", ppsl->m_fSizeRandom);
		WriteData(FS, ppsl->m_fSpinRandom);//WriteProperty(file, L"fSpinRandom", ppsl->m_fSpinRandom);
		WriteData(FS, ppsl->m_fAngleRandom);//WriteProperty(file, L"fAngleRandom", ppsl->m_fAngleRandom);
		WriteData(FS, ppsl->m_fAlignToMotionAngle);//WriteProperty(file, L"fAlignToMotionAngle", ppsl->m_fAlignToMotionAngle);
		WriteData(FS, ppsl->m_nAlignToMotion);//WriteProperty(file, L"nAlignToMotion", ppsl->m_nAlignToMotion);
		WriteData(FS, ppsl->m_nAlignToPath);//WriteProperty(file, L"nAlignToPath", ppsl->m_nAlignToPath);
		WriteData(FS, ppsl->m_nMotionToPath);//WriteProperty(file, L"nMotionToPath", ppsl->m_nMotionToPath);
		WriteData(FS, ppsl->m_nAlignToEmitter);//WriteProperty(file, L"nAlignToEmitter", ppsl->m_nAlignToEmitter);
		WriteData(FS, ppsl->m_nAlignToHole);//WriteProperty(file, L"nAlignToHole", ppsl->m_nAlignToHole);
		WriteData(FS, ppsl->m_nParticleRandAngle);//WriteProperty(file, L"nParticleRandAngle", ppsl->m_nParticleRandAngle);
		WriteData(FS, ppsl->m_nParticleRandAngleSelf);//WriteProperty(file, L"nParticleRandAngleSelf", ppsl->m_nParticleRandAngleSelf);
		WriteData(FS, ppsl->m_nParColorStrtR);//WriteProperty(file, L"nParColorStrtR", ppsl->m_nParColorStrtR);
		WriteData(FS, ppsl->m_nParColorStrtG);//WriteProperty(file, L"nParColorStrtG", ppsl->m_nParColorStrtG);
		WriteData(FS, ppsl->m_nParColorStrtB);//WriteProperty(file, L"nParColorStrtB", ppsl->m_nParColorStrtB);
		WriteData(FS, ppsl->m_nParColorEndR);//WriteProperty(file, L"nParColorEndR", ppsl->m_nParColorEndR);
		WriteData(FS, ppsl->m_nParColorEndG);//WriteProperty(file, L"nParColorEndG", ppsl->m_nParColorEndG);
		WriteData(FS, ppsl->m_nParColorEndB);//WriteProperty(file, L"nParColorEndB", ppsl->m_nParColorEndB);
		WriteData(FS, ppsl->m_fParColorVar);//WriteProperty(file, L"fParColorVar", ppsl->m_fParColorVar);
		WriteData(FS, ppsl->m_fPSLzoom);//WriteProperty(file, L"fPSLzoom", ppsl->m_fPSLzoom);
		WriteData(FS, ppsl->m_fEmitterSpin);//WriteProperty(file, L"fEmitterSpin", ppsl->m_fEmitterSpin);
		WriteData(FS, ppsl->m_nDestBlendMode);//WriteProperty(file, L"nDestBlendMode", ppsl->m_nDestBlendMode);
		WriteDataBlendMode(FS, ppsl->m_nBlendMode);//WriteBlendModeProperty(file, L"BlendMode", ppsl->m_nBlendMode);

		int cnt = (int)ppsl->m_vectorTexturePath.size();
		WriteData(FS, cnt);//WriteProperty(file, L"TextureNum", cnt);
		for(int i = 0; i < cnt; i++)
		{
			std::wstring strTmp = ppsl->m_vectorTexturePath[i];
			if (strTmp.find(L"/effect/particle/texture/") != std::wstring::npos)
			{
				size_t nPos = 25;//strTmp.rfind(L"/effect/particle/texture/");
				strTmp = strTmp.substr(nPos, strTmp.length() - nPos);
			}
			WriteData(FS, ws2s(strTmp));//WriteProperty(file, str1.c_str(), strTmp.c_str());
		}
		WriteData(FS, ppsl->m_nRowNum);//WriteProperty(file, L"nRowNum", ppsl->m_nRowNum);
		WriteData(FS, ppsl->m_nColNum);//WriteProperty(file, L"nColNum", ppsl->m_nColNum);
		WriteData(FS, ppsl->m_nTextureNum);//WriteProperty(file, L"nTextureNum", ppsl->m_nTextureNum);
		WriteData(FS, ppsl->m_rctMaxBox.left);//WriteProperty(file, L"rctMaxBoxL", ppsl->m_rctMaxBox.left);
		WriteData(FS, ppsl->m_rctMaxBox.top);//WriteProperty(file, L"rctMaxBoxT", ppsl->m_rctMaxBox.top);
		WriteData(FS, ppsl->m_rctMaxBox.right);//WriteProperty(file, L"rctMaxBoxR", ppsl->m_rctMaxBox.right);
		WriteData(FS, ppsl->m_rctMaxBox.bottom);//WriteProperty(file, L"rctMaxBoxB", ppsl->m_rctMaxBox.bottom);
		WriteData(FS, ppsl->m_nInterfacePsl);//WriteProperty(file, L"nInterfacePsl", ppsl->m_nInterfacePsl);
		WriteData(FS, ppsl->m_ptInterfacialCenter.x);//WriteProperty(file, L"ptInterfacialCenterX", ppsl->m_ptInterfacialCenter.x);
		WriteData(FS, ppsl->m_ptInterfacialCenter.y);//WriteProperty(file, L"ptInterfacialCenterY", ppsl->m_ptInterfacialCenter.y);
		WriteData(FS, ppsl->m_nSkillEffectSpritePoint);//WriteProperty(file, L"nSkillEffectSpritePoint", ppsl->m_nSkillEffectSpritePoint);

		return true;
	}
};