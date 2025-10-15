#include "nuGeneralParticleSystem.h"
#include "nuparticlemanager.h"

namespace Nuclear
{
	CGeneralParticleSystem::CGeneralParticleSystem(const NuclearHardRef<PSL>& RefPsl, const PSParameter& psParameter, IBaseRenderer *pRenderer,
		const std::vector<POINT>& vectorFullPathPoint, const std::wstring& pslName)
		:CParticleSystem(RefPsl, psParameter, pRenderer, pslName), m_pLoadingNotify(NULL), m_bLoadFaild(false)
	{
		assert(m_ppsl->m_nAbsrtactPsl != 1);
		//路径文件读取放在了particleManager里Create PS的时候
		if(m_ppsl->m_nPosType == PSLTYPE_CUSTOMPATH)		
			SetPathVector(vectorFullPathPoint);//初始化路径点信息..m_vectorFullPathPoint
		m_restTexRes = 0;

		m_nPosIndex = 0;
		m_fparticleSize = 50.0f;

		m_nCurSysMaxParticles = m_ppsl->m_nSysMaxParticles;
		m_nCurEmissionRate = m_ppsl->m_nEmissionRate;

		m_fRealSysLife = m_ppsl->m_fSysLife;

		m_bLoadedRes = false; //从这里构造的都是没有加载纹理资源的。
		
		m_nKeyPointIndex = -1; //-1 表示使用完整路径
		m_nLinePosIndex = -1;
	}

	CGeneralParticleSystem::~CGeneralParticleSystem(void)
	{//各小图片的引用计数减1
		m_vectorD3DTexture.clear();
	}

	void CGeneralParticleSystem::SetParticleSysResouce(const NuclearHardRef<PSTEXTUREINFOSTRUCT>& pRefPSTextureInfo)
	{
		m_pRefPSTextureInfo = pRefPSTextureInfo;
		InitTexturePicSize();

		int nsize = m_ppsl->m_nSysMaxParticles *4;
		if(m_psParameter.m_bVertexRHWflag)
			m_vertices_TCT.resize(nsize);
		else
			m_vertices_UTCT.resize(nsize);
		
		m_bLoadedRes = true;
	}

	void CGeneralParticleSystem::SetLoadedResFlag(bool bLoadedRes)
	{
		m_bLoadedRes = bLoadedRes;
	}

	bool CGeneralParticleSystem::GetLoadedResFlag()
	{
		return m_bLoadedRes;
	}

	void CGeneralParticleSystem::Reset(void) 
	{
		if( fabs(m_psParameter.time - PSDEFAULTTIME) > FIRE_EPSILON )
			m_fRealSysLife = m_psParameter.time;
		else
			m_fRealSysLife = m_ppsl->m_fSysLife;

		m_listPrePathIndex.clear();
		CParticleSystem::Reset();
	}

	void CGeneralParticleSystem::ResetVerticesVector()
	{//外面修改了m_psParameter.m_bVertexRHWflag
		m_vertices_TCT.clear();
		m_vertices_UTCT.clear();

		int nSysMaxParticles = m_nCurSysMaxParticles;//m_ppsl->m_nSysMaxParticles;

		if(m_psParameter.m_bVertexRHWflag)
			m_vertices_TCT.resize(4* nSysMaxParticles);
		else
			m_vertices_UTCT.resize(4* nSysMaxParticles);
	}

	void CGeneralParticleSystem::ResizeParticleArry(int nSysMaxParticles, int nEmissionRate)
	{
		nSysMaxParticles = nSysMaxParticles>2000 ? 2000:nSysMaxParticles;
		nEmissionRate = nEmissionRate>1000 ? 1000:nEmissionRate;

		if(nEmissionRate > 0 && m_nCurEmissionRate != nEmissionRate)
		{
			m_nCurEmissionRate = nEmissionRate;
		}
		if(m_nCurSysMaxParticles != nSysMaxParticles)
		{
			m_nCurSysMaxParticles = nSysMaxParticles;

			m_particleArray.Resize(nSysMaxParticles);

			if(m_psParameter.m_bVertexRHWflag)
				m_vertices_TCT.resize(4* nSysMaxParticles);
			else
				m_vertices_UTCT.resize(4* nSysMaxParticles);
		}
	}

	bool CGeneralParticleSystem::CreateNewParticles(float fTimeDelta)
	{
		int cnt = m_particleArray.GetNumUsedElements();

		//创建新粒子 --先算出要创建的个数..	
		int iNumNewParts = 0;
		assert(m_ppsl->m_nAbsrtactPsl != 1);

		if(!m_ppsl->m_nEmitterType)
		{//非连续..
			if(cnt==0)
			{
				iNumNewParts = m_nCurSysMaxParticles - m_particleArray.GetNumUsedElements();//始终保持最大粒子数..
			}
			else
				return true;
		}
		else
		{
			int nParticlePerSec = m_nCurEmissionRate; //m_ppsl->m_nEmissionRate;
			iNumNewParts = (int)(nParticlePerSec * fTimeDelta);
			m_fNumNewPartsExcess += nParticlePerSec * fTimeDelta-iNumNewParts;

			if(m_fNumNewPartsExcess > 1.0f) 
			{
				iNumNewParts += (int)m_fNumNewPartsExcess;
				m_fNumNewPartsExcess -= (int)m_fNumNewPartsExcess;
			}

			if(iNumNewParts <= m_nNumReLifePartsExcess)//减去循环粒子时上面强制复活数
			{
				m_nNumReLifePartsExcess -= iNumNewParts;
				iNumNewParts = 0;
			}
			else
			{
				m_nNumReLifePartsExcess = 0;
				iNumNewParts -= m_nNumReLifePartsExcess;
			}
		}

		float fPslZoom = m_ppsl->m_fPSLzoom * m_psParameter.m_psModifyParam.m_fEmissionScale;
		int cntPathPoint = (int)m_vectorFullPathPoint.size();

		if(m_nKeyPointIndex!=-1 && cntPathPoint>0) //2010-9-8 关键帧路径相关参数
			cntPathPoint = m_nKeyPointIndex+1;

		//根据纹理个数产生一个随机数来给粒子赋值纹理..
		int nParPtexNum = 0;
		nParPtexNum = m_pRefPSTextureInfo->PsTextureInfo.m_nTexNum;

		POINT objPoint;
		if(m_ppsl->m_nTrackFx || m_ppsl->m_nAlignToHole)
		{
			if(m_ppsl->m_nTrackFx)
			{
				objPoint.x = (int)m_psParameter.m_psModifyParam.m_pointObject.x;
				objPoint.y = (int)m_psParameter.m_psModifyParam.m_pointObject.y;
			}
			else
			{
				float holex = m_ppsl->m_pointParticleHole.x *fPslZoom;
				float holey = m_ppsl->m_pointParticleHole.y *fPslZoom;

				int nAngle = (int)( TOANGLE(m_psParameter.m_psModifyParam.m_fEmitterSpinAngle) +m_ppsl->m_fEmitterSpin +0.5f);
				float fxpcos = XPCOS(nAngle);
				float fxpsin = XPSIN(nAngle);
				objPoint.x = (int)(holex*fxpcos - holey*fxpsin + m_psParameter.m_psModifyParam.m_pointCent.x);
				objPoint.y = (int)(holex*fxpsin + holey*fxpcos + m_psParameter.m_psModifyParam.m_pointCent.y);
			}
		}

		for(int q=0; q<iNumNewParts && cnt<m_nCurSysMaxParticles; q++)
		{
			if( m_fSysAge>m_fRealSysLife )	//m_fRealSysLife 程序修改的时间
			{
				if(cnt==0)
				{ //返回false，可以删除了..
					//		XPTRACE(L"非循环?...已过了粒子系统生命期并且没有存活的粒子了...\n");
					std::vector<IParticleNotify*>::iterator iter = m_vectNotifys.begin();
					for(;iter!=m_vectNotifys.end();++iter)
					{
						(*iter)->OnStop();
					}

					return false;
				}
				else//11-25--由全部删除改为不再产生新粒子..
					return true;
			}

			CParticle* part = m_particleArray.New();//这个找空位的过程是可以优化的...
													//通过记录每个死去的粒子位置来实现...但是没有必要..因为..
			//对于循环特效，找空位是很少调用的..(只在播第一遍的时候会要找空位,第一遍的空位是很好找的...连续:))
			
			if(part == NULL)
			{
				if( !(ParticleManager::m_logName.empty()) )
					XPLOG_PSMHZX(L"m_particleArray.New() == null ...\n");
				return false;
			}
			cnt++; //已使用粒子数加一。

			if(m_ppsl->m_nParticleAniTextureIndex1 == 0)
				part->m_nStrTextureIndex = RandomNumber(0, nParPtexNum-1);
			else
				part->m_nStrTextureIndex = 0;
			part->m_nCurTextureIndex = part->m_nStrTextureIndex;
			if(m_ppsl->m_nSingleParticleAni == 1)
			{
				float minSpeed = m_ppsl->m_nAniPlaySpeed - m_ppsl->m_fAniSpeedRand;
				float maxSpeed = m_ppsl->m_nAniPlaySpeed + m_ppsl->m_fAniSpeedRand;
				minSpeed = minSpeed>0 ? minSpeed:0;
				maxSpeed = maxSpeed<50 ? maxSpeed:50;
				part->m_fAniSpeed = RandomNumber(minSpeed, maxSpeed);
			}

			if(m_ppsl->m_nMotionToPath == 1)
			{
				switch(m_ppsl->m_nParticlePathInitPosType)
				{
				case PARTICLEPATHPOS_START:
					part->m_nStrtPathIndex = 0;
					break;

				case PARTICLEPATHPOS_RANDOM:
					part->m_nStrtPathIndex = RandomNumber(0, cntPathPoint-1);
					break;

				case PARTICLEPATHPOS_PREPOS:
					if( m_listPrePathIndex.size() !=0 )
					{
						part->m_nStrtPathIndex = m_listPrePathIndex.front();
						m_listPrePathIndex.pop_front();
					}
					else
					{
						part->m_nStrtPathIndex = 0;
					}
					break;

				default:
					break;
				}
			}

			//
			part->m_fAge = 0.0f;
			float fValue = RandomNumber(-m_ppsl->m_fSizeRandom, m_ppsl->m_fSizeRandom);

			float fvaluex1 = 0.0f;
			float fvaluey1 = 0.0f;
			float fvaluex2 = 0.0f;
			float fvaluey2 = 0.0f;

			float fcoef_strt = 10.0f; //分母为0, 这个就随便取个有意义的值
			if(m_ppsl->m_fParSizeStrtY > FIRE_EPSILON)
				fcoef_strt = m_ppsl->m_fParSizeStrtX/m_ppsl->m_fParSizeStrtY;
			float fcoef_end = 10.0f; //分母为0, 这个就随便取个有意义的值
			if(m_ppsl->m_fParSizeEndY > FIRE_EPSILON)
				fcoef_end = m_ppsl->m_fParSizeEndX/m_ppsl->m_fParSizeEndY;

			switch(m_ppsl->m_nParticleSizeRandType)
			{
			case PARTICLESIZERAND_XY:
				fvaluex1 = m_ppsl->m_fParSizeStrtX +fValue;
				fvaluey1 = m_ppsl->m_fParSizeStrtY +fValue;
				fvaluex2 = m_ppsl->m_fParSizeEndX +fValue;
				fvaluey2 = m_ppsl->m_fParSizeEndY +fValue;
				break;
			case PARTICLESIZERAND_X:
				fvaluex1 = m_ppsl->m_fParSizeStrtX +fValue;
				fvaluey1 = m_ppsl->m_fParSizeStrtY;
				fvaluex2 = m_ppsl->m_fParSizeEndX +fValue;
				fvaluey2 = m_ppsl->m_fParSizeEndY;
				break;
			case PARTICLESIZERAND_Y:
				fvaluex1 = m_ppsl->m_fParSizeStrtX;
				fvaluey1 = m_ppsl->m_fParSizeStrtY +fValue;
				fvaluex2 = m_ppsl->m_fParSizeEndX;
				fvaluey2 = m_ppsl->m_fParSizeEndY +fValue;
				break;
			case PARTICLESIZERAND_ZOOM:
				fvaluex1 = m_ppsl->m_fParSizeStrtX +fValue*fcoef_strt;
				fvaluey1 = m_ppsl->m_fParSizeStrtY +fValue;
				fvaluex2 = m_ppsl->m_fParSizeEndX +fValue*fcoef_end;
				fvaluey2 = m_ppsl->m_fParSizeEndY +fValue;
				break;
			default:
				break;
			}

			fvaluex1 = fvaluex1<0 ? 0:fvaluex1;
			fvaluex1 = fvaluex1>100 ? 100:fvaluex1;

			fvaluey1 = fvaluey1<0 ? 0:fvaluey1;
			fvaluey1 = fvaluey1>100 ? 100:fvaluey1;

			fvaluex2 = fvaluex2<0 ? 0:fvaluex2;
			fvaluex2 = fvaluex2>100 ? 100:fvaluex2;

			fvaluey2 = fvaluey2<0 ? 0:fvaluey2;
			fvaluey2 = fvaluey2>100 ? 100:fvaluey2;

			part->m_fParSizeStrtX = fvaluex1;
			part->m_fParSizeStrtY = fvaluey1;
			part->m_fParSizeEndX = fvaluex2;
			part->m_fParSizeEndY = fvaluey2;

			part->m_fParSizeVar = m_ppsl->m_fParSizeVar;

			part->m_fLifetime = RandomNumber(m_ppsl->m_fParticleLifeMin, m_ppsl->m_fParticleLifeMax);
			//		XPTRACE(L"%.1f\n", part->m_fLifetime);

			InitParticlePos(part);
			InitParticleSpeed(part, objPoint);
			InitParticleAlphaColor(part);

			//给新产生的粒子颜色、透明度、尺寸、旋转角度等赋值...
			part->Update(0.0f); //2009--5--31
		}

		return true;
	}

	void CGeneralParticleSystem::InitParticleSpeed(CParticle* part, const POINT& objPoint)
	{
		float fSpeed = RandomNumber(m_ppsl->m_fSpeedMin, m_ppsl->m_fSpeedMax);

// 		if(m_nKeyPointIndex != -1)
// 		{
// 			int nTotalPoints = m_ppsl->m_nNumPointFullPath;
// 			float fcoef = (float)m_nKeyPointIndex/(float)nTotalPoints;
// 			fSpeed *= fcoef;
// 		}

		part->m_fPathMotionSpeed = fSpeed;

		float fDirAngle = -m_ppsl->m_fEmissionDirection; //逆时针旋转
	
		fDirAngle = TORADIANS(fDirAngle);
		fDirAngle += m_psParameter.m_psModifyParam.m_fEmitterSpinAngle;

		float fSpreadAngle = m_ppsl->m_fEmissionSpread;
		fSpreadAngle = TORADIANS(fSpreadAngle);
		fDirAngle = RandomNumber(fDirAngle-fSpreadAngle/2.0f, fDirAngle+fSpreadAngle/2.0f);

		float fvalue1 = 0.0f;
		float fvalue2 = 0.0f;

		if(m_ppsl->m_nTrackFx || m_ppsl->m_nAlignToHole)
		{
			POINT parPos;
			parPos.x = (int)(part->m_vPos.x);
			parPos.y = (int)(part->m_vPos.y);

			float fAngle = Computer2VectorAngle(NuclearVector2((float)(objPoint.x-parPos.x), (float)(objPoint.y-parPos.y)), NuclearVector2(0.0f, -1.0f));
			if(objPoint.x-parPos.x <0)
				fAngle = 2*FIRE_PI - fAngle;

			part->m_fSpinStrt = TOANGLE(fAngle);
			part->m_fSpinEnd = TOANGLE(fAngle);
			part->m_fSpinVar = 0.0f;

			fDirAngle = FIRE_PI*1.5f +fAngle;
		}
		else
		{
			if(m_ppsl->m_nAlignToMotion)
			{
				part->m_fSpinStrt = TOANGLE(fDirAngle) +m_ppsl->m_fAlignToMotionAngle;
				part->m_fSpinEnd = TOANGLE(fDirAngle) +m_ppsl->m_fAlignToMotionAngle;
				part->m_fSpinVar = 0.0f;
			}
			else if(m_ppsl->m_nAlignToPath == 1 && m_ppsl->m_nPosType == PSLTYPE_CUSTOMPATH 
				&& m_ppsl->m_nPathOrder && !m_ppsl->m_nMotionToPath) 
			{	//粒子沿着路径运动的就没必要在这里做了..
				//只针对有用户编辑路径、并且是选择了时序保持的情况处理 //m_nPosIndex
				//					
				int nPos = (m_nPosIndex-1) > -1 ? (m_nPosIndex-1) : 0;

				part->m_fSpinStrt = m_vectorPathAngle[nPos] +m_ppsl->m_fAlignToMotionAngle;
				part->m_fSpinEnd = m_vectorPathAngle[nPos] +m_ppsl->m_fAlignToMotionAngle;
				part->m_fSpinVar = 0.0f;
			}
			else if(m_ppsl->m_nParticleRandAngle)
			{
				float fRandAngle = RandomNumber(0.0f, 360.0f);
				fvalue1 = fRandAngle -m_ppsl->m_fSpinRandom;
				fvalue2 = fRandAngle +m_ppsl->m_fSpinRandom;
				fvalue1 = fvalue1<-360 ? -360:fvalue1;
				fvalue2 = fvalue2>360 ? 360:fvalue2;
				part->m_fSpinStrt = fvalue1;
				part->m_fSpinEnd = fvalue2;
				part->m_fSpinVar = 0.5f;
			}
			else
			{
				fvalue1 = m_ppsl->m_fParSpinStrt -m_ppsl->m_fSpinRandom;
				fvalue2 = m_ppsl->m_fParSpinStrt +m_ppsl->m_fSpinRandom;
				fvalue1 = fvalue1<-360 ? -360:fvalue1;
				fvalue2 = fvalue2>360 ? 360:fvalue2;
				part->m_fSpinStrt = RandomNumber(fvalue1, fvalue2);

				fvalue1 = m_ppsl->m_fParSpinEnd -m_ppsl->m_fSpinRandom;
				fvalue2 = m_ppsl->m_fParSpinEnd +m_ppsl->m_fSpinRandom;
				fvalue1 = fvalue1<-360 ? -360:fvalue1;
				fvalue2 = fvalue2>360 ? 360:fvalue2;
				part->m_fSpinEnd = RandomNumber(fvalue1, fvalue2);
				part->m_fSpinVar = m_ppsl->m_fParSpinVar;
			}
		}

		//2010-3-24 --这只在粒子产生的时候改变方向，技能需要的是任何时候粒子都跟随发射器旋转
// 		if(m_ppsl->m_nAlignToEmitter)
// 		{
// 			float fEmitterAngle = TOANGLE(m_psParameter.m_psModifyParam.m_fEmitterSpinAngle);
// 			part->m_fSpinStrt += fEmitterAngle;
// 			part->m_fSpinEnd += fEmitterAngle;
// 		}

		//下面初始化自旋角度
		if(m_ppsl->m_nParticleRandAngleSelf)
		{
			float fRandAngle = RandomNumber(0.0f, 360.0f);
			fvalue1 = fRandAngle -m_ppsl->m_fAngleRandom;
			fvalue2 = fRandAngle +m_ppsl->m_fAngleRandom;
			fvalue1 = fvalue1<-360 ? -360:fvalue1;
			fvalue2 = fvalue2>360 ? 360:fvalue2;
			part->m_fAngleStrt = fvalue1;
			part->m_fAngleEnd = fvalue2;
			part->m_fAngleVar = 0.5f; //线性插值变化
		}
		else
		{
			fvalue1 = m_ppsl->m_fParAngleStrt -m_ppsl->m_fAngleRandom;
			fvalue2 = m_ppsl->m_fParAngleStrt +m_ppsl->m_fAngleRandom;
			fvalue1 = fvalue1<-360 ? -360:fvalue1;
			fvalue2 = fvalue2>360 ? 360:fvalue2;
			part->m_fAngleStrt = RandomNumber(fvalue1, fvalue2);

			fvalue1 = m_ppsl->m_fParAngleEnd -m_ppsl->m_fAngleRandom;
			fvalue2 = m_ppsl->m_fParAngleEnd +m_ppsl->m_fAngleRandom;
			fvalue1 = fvalue1<-360 ? -360:fvalue1;
			fvalue2 = fvalue2>360 ? 360:fvalue2;
			part->m_fAngleEnd = RandomNumber(fvalue1, fvalue2);

			part->m_fAngleVar = m_ppsl->m_fParAngleVar;
		}

		part->m_fAngle0 = fDirAngle;
		int nAngle = (int)( TOANGLE(fDirAngle) );
		part->m_vDir0.Reset(fSpeed*XPCOS(nAngle), fSpeed*XPSIN(nAngle), 0.0f);
		part->m_vDir.Reset(part->m_vDir0.x, part->m_vDir0.y, part->m_vDir0.z);
	}

	void CGeneralParticleSystem::InitParticlePos(CParticle* part)
	{
		float fPslZoom = m_ppsl->m_fPSLzoom;
		if( !m_psParameter.m_bModifyLineEmitter )
		{
			fPslZoom *= m_psParameter.m_psModifyParam.m_fEmissionScale;
		}

		int rx = 0;
		int ry = 0;
		float angle = 0.0f;
		float x = 0.0f;
		float y = 0.0f;
		int index =0;
		float fWidth = 0.0f;
		float fHeight = 0.0f;
		int rctRandSeed = 0;
		NuclearFPoint pt0;
		NuclearFPoint pt1;
		float fk = 0.0f;

		if(m_ppsl->m_nMoveType == PSLMOVETYPE_FULLSCREEN)
		{
			//发射位置修改为屏幕左上到右上的一条线...
			//中点从psl中获取..
			float fViewWidth = (float)m_curViewPort.Width();
			m_ppsl->m_point0 = NuclearFPoint(0, 0);
			m_ppsl->m_point1 = NuclearFPoint(fViewWidth, 0); //1280
			if(m_ppsl->m_nPosType == PSLTYPE_LINE)
			{
				m_psParameter.m_psModifyParam.m_pointCent.x = (m_ppsl->m_point0.x + m_ppsl->m_point1.x)/2.0f;
				m_psParameter.m_psModifyParam.m_pointCent.y = (m_ppsl->m_point0.y + m_ppsl->m_point1.y)/2.0f;
			}
		//	else if(m_ppsl->m_nPosType == PSLTYPE_RECT)
		//	{
		//		m_psParameter.m_psModifyParam.m_pointCent.x = (m_curViewPort.left + m_curViewPort.right)/2.0f;
		//		m_psParameter.m_psModifyParam.m_pointCent.y = (m_curViewPort.top + m_curViewPort.bottom)/2.0f;
		//	}
			else
			{
				m_psParameter.m_psModifyParam.m_pointCent.x = (m_curViewPort.left + m_curViewPort.right)/2.0f;
				m_psParameter.m_psModifyParam.m_pointCent.y = (m_curViewPort.top + m_curViewPort.bottom)/2.0f;
			}

			if(m_psParameter.m_psModifyParam.m_scaleType != XPEST_NOSCALE &&
				!(m_psParameter.m_bModifyLineEmitter) )
				m_psParameter.m_psModifyParam.m_pointCent *= m_psParameter.m_psModifyParam.m_fParticlePosCoef;
		}

		int nSpinAngle = (int)( TOANGLE(m_psParameter.m_psModifyParam.m_fEmitterSpinAngle) +m_ppsl->m_fEmitterSpin +0.5f);
		float fcosSpinAngle = XPCOS(nSpinAngle);
		float fsinSpinAngle = XPSIN(nSpinAngle);

		switch(m_ppsl->m_nPosType)
		{
		case PSLTYPE_POINT: //点发射
			{
				pt0 = m_psParameter.m_psModifyParam.m_pointCent;
				NuclearFPoint pt = m_ppsl->m_pointEmitterSpinCent *fPslZoom + m_psParameter.m_psModifyParam.m_pointCent;
				pt0 -= pt;
				float x0 = pt0.x;
				float y0 = pt0.y;

				pt0.x = x0*fcosSpinAngle - y0*fsinSpinAngle;
				pt0.y = x0*fsinSpinAngle + y0*fcosSpinAngle;

				pt0 += pt;

				part->m_vPos.Reset( pt0.x, pt0.y, 0.0f );
			}
			break;

		case PSLTYPE_LINE:
			{
				x = m_ppsl->m_point1.x - m_ppsl->m_point0.x;
				y = m_ppsl->m_point1.y - m_ppsl->m_point0.y;
				
				if(m_psParameter.m_bModifyLineEmitter)
				{
				//	x = m_psParameter.m_psModifyParam.m_fpoint1.x - m_psParameter.m_psModifyParam.m_fpoint0.x;
				//	y = m_psParameter.m_psModifyParam.m_fpoint1.y - m_psParameter.m_psModifyParam.m_fpoint0.y;

					x = m_psParameter.m_ptLineEmitterEnd.x - m_psParameter.m_ptLineEmitterStrt.x;
					y = m_psParameter.m_ptLineEmitterEnd.y - m_psParameter.m_ptLineEmitterStrt.y;
				}

				x /= 2.0f;
				y /= 2.0f;

				x *= fPslZoom;
				y *= fPslZoom;

				if(m_psParameter.m_psModifyParam.m_fLineLength > 0)
				{ //文字背景特效
					if( fabs(x)>10.0f && fabs(y)<3.0f ) //视为水平方向的线
					{
						x = m_psParameter.m_psModifyParam.m_fLineLength/2.0f;
						y = 0;
					}
				}

				pt0 = NuclearFPoint(-x, -y) +m_psParameter.m_psModifyParam.m_pointCent;
				pt1 = NuclearFPoint(x, y) +m_psParameter.m_psModifyParam.m_pointCent;

				NuclearFPoint pt = m_ppsl->m_pointEmitterSpinCent *fPslZoom + m_psParameter.m_psModifyParam.m_pointCent;
				pt0 -= pt;
				pt1 -= pt;
				float x0 = pt0.x;
				float y0 = pt0.y;
				float x1 = pt1.x;
				float y1 = pt1.y;

				pt0.x = x0*fcosSpinAngle - y0*fsinSpinAngle;
				pt0.y = x0*fsinSpinAngle + y0*fcosSpinAngle;
				pt1.x = x1*fcosSpinAngle - y1*fsinSpinAngle;
				pt1.y = x1*fsinSpinAngle + y1*fcosSpinAngle;

				pt0 += pt;
				pt1 += pt;

				if(m_psParameter.m_bModifyLineEmitter)
				{//m_nLinePosIndex   m_nCurSysMaxParticles
					m_nLinePosIndex++;
					m_nLinePosIndex = m_nLinePosIndex>m_nCurSysMaxParticles ? 0:m_nLinePosIndex;
					float fLinePos = (float)(m_nLinePosIndex)/m_nCurSysMaxParticles;

					float fCoef = fLinePos/(1.0f-fLinePos);

					x = (pt0.x + fCoef*pt1.x)/(1.0f+fCoef);
					y = (pt0.y + fCoef*pt1.y)/(1.0f+fCoef);
				}
				else
				{
					x = pt1.x - pt0.x;
					y = pt1.y - pt0.y;
					if(fabs(x) > 0.01f)
					{
						fk = y/x;
						x = RandomNumber(pt0.x, pt1.x);
						y = fk*(x-pt0.x) +pt0.y;//直线方程y-y0 = k(x-x0);
					}
					else
					{//x==0
						y = RandomNumber(pt0.y, pt1.y);
						x = pt0.x;
					}
				}
				part->m_vPos.Reset(x, y, 0.0f);
			}
			break;

		case PSLTYPE_CIRCLE: //椭圆
			{
				if(m_ppsl->m_nParFillMode)
				{
					rx = RandomNumber(0, m_ppsl->m_nSysPosRadiusX);
					ry = RandomNumber(0, m_ppsl->m_nSysPosRadiusY);
				}
				else
				{
					rx = m_ppsl->m_nSysPosRadiusX;
					ry = m_ppsl->m_nSysPosRadiusY;
				}

				angle = RandomNumber(-FIRE_PI, FIRE_PI);

				int nAngle = (int)( TOANGLE(angle)  +0.5f);
				x = rx *fPslZoom *XPCOS(nAngle) +(float)m_psParameter.m_psModifyParam.m_pointCent.x;
				y = ry *fPslZoom *XPSIN(nAngle) +(float)m_psParameter.m_psModifyParam.m_pointCent.y;

				pt0 = NuclearFPoint(x, y);
				NuclearFPoint pt = m_ppsl->m_pointEmitterSpinCent *fPslZoom + m_psParameter.m_psModifyParam.m_pointCent;
				pt0 -= pt;
				float x0 = pt0.x;
				float y0 = pt0.y;

				pt0.x = x0*fcosSpinAngle - y0*fsinSpinAngle;
				pt0.y = x0*fsinSpinAngle + y0*fcosSpinAngle;

				pt0 += pt;

				part->m_vPos.Reset( pt0.x, pt0.y, 0.0f );
			}
			break;

		case PSLTYPE_RECT: //矩形
			{
				float fx = 1.0f; //放大缩小倍数
				float fy = 1.0f;

				if(m_psParameter.m_psModifyParam.m_fRctWidth>0 && m_psParameter.m_psModifyParam.m_fRctHeight>0)
				{//文字背景特效 //2011-4-27-程序运行时指定矩形区域的界面特效
					fx = m_psParameter.m_psModifyParam.m_fRctWidth/(float)(m_ppsl->m_rct.Width());
					fy = m_psParameter.m_psModifyParam.m_fRctHeight/(float)(m_ppsl->m_rct.Height());
				}

				x = (float)RandomNumber((int)m_ppsl->m_rct.left, (int)m_ppsl->m_rct.right);
				y = (float)RandomNumber((int)m_ppsl->m_rct.top, (int)m_ppsl->m_rct.bottom);

				if(m_ppsl->m_nParFillMode)
				{ //就用上面计算的x、y
				}
				else
				{
					if(m_ppsl->m_nPathOrder)
					{//2011-4-27-粒子只在左上角产生
						x = (float)(m_ppsl->m_rct.left);
						y = (float)(m_ppsl->m_rct.top);
					}
					else
					{
						rctRandSeed = RandomNumber(0, 39); //10个数一条边
						rctRandSeed /= 10;
						switch(rctRandSeed)
						{
						case 0:
							{
								x = (float)(m_ppsl->m_rct.left);
							}
							break;
						case 1:
							{
								y = (float)(m_ppsl->m_rct.bottom);
							}
							break;
						case 2:
							{
								x = (float)(m_ppsl->m_rct.right);
							}
							break;
						case 3:
							{
								y = (float)(m_ppsl->m_rct.top);
							}
							break;
						default:
							break;
						}
					}
				}

				x *= fPslZoom *fx;
				y *= fPslZoom *fy;

				Nuclear::NuclearFPoint pt = m_ppsl->m_pointEmitterSpinCent *fPslZoom + m_psParameter.m_psModifyParam.m_pointCent;
				pt0 = NuclearFPoint(x, y) +m_psParameter.m_psModifyParam.m_pointCent;
				pt0 -= pt;
				float x0 = pt0.x;
				float y0 = pt0.y;

				pt0.x = x0*fcosSpinAngle - y0*fsinSpinAngle;
				pt0.y = x0*fsinSpinAngle + y0*fcosSpinAngle;

				pt0 += pt;
				part->m_vPos.Reset(pt0.x, pt0.y, 0.0f);
			}
			break;

		case PSLTYPE_CUSTOMPATH:
			{
				//使用自定义路径--m_vectorFullPathPoint--读路径文件来初始化part->m_vPos
				int cntCustomPoints = (int)m_vectorFullPathPoint.size();

				if(m_nKeyPointIndex!=-1 && cntCustomPoints>0) //2010-9-8 关键帧路径相关参数
					cntCustomPoints = m_nKeyPointIndex+1;

				if( cntCustomPoints==0 )
				{
					pt0.x = 100.0f;
					pt0.y = 100.0f;
				}
				else
				{
					if(m_ppsl->m_nPathOrder)
					{//时序保持
						if( m_nPosIndex > cntCustomPoints-1 )
							m_nPosIndex = 0;
						index = m_nPosIndex;

						float fcoef_temp = 1.0f;
 						if(m_nKeyPointIndex != -1)
 						{
 							int nTotalPoints = m_ppsl->m_nNumPointFullPath;
 							fcoef_temp = (float)m_nKeyPointIndex/(float)nTotalPoints;
 						}
						int nPathIndex = (int)(m_fSysAge * m_ppsl->m_nEmitterSpeed * fcoef_temp); //fTimeDelta会误差积累..
						m_nPosIndex = nPathIndex %cntCustomPoints;
					}
					else
					{//随机
						if(m_ppsl->m_nParticlePathInitPosType == PARTICLEPATHPOS_START)
						{
							index = 0;
						}
						else if(m_ppsl->m_nParticlePathInitPosType == PARTICLEPATHPOS_PREPOS)
						{
							index = m_nPosIndex;
						}
						else
						{
							index = RandomNumber(0, cntCustomPoints-1);
						}
					}
					//	int randDis = RandomNumber(-5, 5);
					x = m_vectorFullPathPoint[index].x; // +randDis
					y = -m_vectorFullPathPoint[index].y; // +randDis

					x *= fPslZoom;
					y *= fPslZoom;

					pt0 = NuclearFPoint(x, y) +m_psParameter.m_psModifyParam.m_pointCent;
					Nuclear::NuclearFPoint pt = m_ppsl->m_pointEmitterSpinCent *fPslZoom + m_psParameter.m_psModifyParam.m_pointCent;
					pt0 -= pt;
					float x0 = pt0.x;
					float y0 = pt0.y;

					pt0.x = x0*fcosSpinAngle - y0*fsinSpinAngle;
					pt0.y = x0*fsinSpinAngle + y0*fcosSpinAngle;

					pt0 += pt;
				}
				part->m_vPos.Reset(pt0.x, pt0.y, 0.0f);
			}
			break;

		default:
			break;
		}

		part->m_vPos0 = part->m_vPos;
	}

	bool CGeneralParticleSystem::Update(float fTimeDelta)
	{
		if(fTimeDelta<-0.00001f)
		{
			return false;
		}

		if(!m_bLoadedRes)
			return false;

		if(m_bStop)
			fTimeDelta = 0.0f;

		assert(m_ppsl->m_nAbsrtactPsl != 1);

		float fPslZoom = m_ppsl->m_fPSLzoom * m_psParameter.m_psModifyParam.m_fEmissionScale;
		m_fSysAge += fTimeDelta;

		float fdeltatime = fTimeDelta; //临时保存下来加在非循环粒子的生命上 ---09--7-9
		//避免当玩家发出一个攻击之后游戏窗口失去焦点了,一段时间窗口恢复之后特效还在...

		if(fTimeDelta > PSUPDATECRITICALTIME) //超过0.2f秒就让粒子暂停..
		{
			fTimeDelta = 0.0f;
			if(m_psParameter.cycle)
				fdeltatime = 0.0f;
		}

		if( fabs(m_psParameter.time - PSDEFAULTTIME) > FIRE_EPSILON )
			m_fRealSysLife = m_psParameter.time;
		if(m_fSysAge > m_fRealSysLife)
		{
			if(m_psParameter.cycle)
			{
				m_fSysAge = 0.0f;
				std::vector<IParticleNotify*>::iterator iter = m_vectNotifys.begin();
				for(; iter!=m_vectNotifys.end(); ++iter)
				{
					(*iter)->OnEnd();
				}
			}
			//		else
			//		{//11-25--由全部删除改为不再产生新粒子..
			//			m_Particles.DeleteAll();
			//			return false; 
			//		}
		}

		int cntPathPoint = (int)m_vectorFullPathPoint.size();//更新已存在和计算新生成的位置都用到了

		if(m_nKeyPointIndex!=-1 && cntPathPoint>0) //2010-9-8 关键帧路径相关参数
			cntPathPoint = m_nKeyPointIndex+1;

		if(!m_pRefPSTextureInfo.IsValid())
			return false;
		int nParPtexNum = 0;
		nParPtexNum = m_pRefPSTextureInfo->PsTextureInfo.m_nTexNum;

		int cnt = m_particleArray.GetNumUsedElements();
		int reLifeParNum = 0; //强制复活粒子数

		POINT objPt; //"the point" : track or alignHole
		if(m_ppsl->m_nTrackFx || m_ppsl->m_nAlignToHole)
		{
			if(m_ppsl->m_nTrackFx)
			{
				objPt.x = (int)m_psParameter.m_psModifyParam.m_pointObject.x;
				objPt.y = (int)m_psParameter.m_psModifyParam.m_pointObject.y;
			}
			else
			{
				float holex = m_ppsl->m_pointParticleHole.x *fPslZoom;
				float holey = m_ppsl->m_pointParticleHole.y *fPslZoom;

				int nAngle = (int)( TOANGLE(m_psParameter.m_psModifyParam.m_fEmitterSpinAngle) +m_ppsl->m_fEmitterSpin +0.5f);
				float fxpcos = XPCOS(nAngle);
				float fxpsin = XPSIN(nAngle);
				objPt.x = (int)(holex*fxpcos - holey*fxpsin + m_psParameter.m_psModifyParam.m_pointCent.x);
				objPt.y = (int)(holex*fxpsin + holey*fxpcos + m_psParameter.m_psModifyParam.m_pointCent.y);
			}
		}

		//更新存在的粒子
		for(int q=0; q<m_particleArray.GetTotalElements(); q++)
		{
			if( m_particleArray.IsAlive(q) )
			{
				CParticle* part = m_particleArray.GetAt(q);
				assert(part);

				if( !part->Update(fdeltatime) )
				{
					part->m_fFullScreenTime = 0.0f;
					if(m_psParameter.cycle && (m_ppsl->m_nEmitterType == 1) && fabs(m_psParameter.m_psModifyParam.m_fEmitterSpinAngle)<FIRE_EPSILON )
					{
						//循环并且是连续发射的粒子才能复活..
						//提高效率
						//这里复活了一个粒子..要记录下来后面少生成一个..
						reLifeParNum++;
						part->m_fAge = part->m_fAge - part->m_fLifetime; // 复活后的粒子 age不能直接置零

						//初始化位置
						InitParticlePos(part);
						//初始化速度(包括方向)

						if( !(m_ppsl->m_nTrackFx || m_ppsl->m_nAlignToHole) )
						{
							POINT objPoint;
							InitParticleSpeed(part, objPoint);
						}
						
						InitParticleAlphaColor(part);

						//给新产生的粒子颜色、透明度、尺寸、旋转角度等赋值...否则就还是上次留下的值
						part->Update(0.0f); //2009--9--2 -- 复活的粒子同时也需要初始化颜色透明度大小等信息

						//对于多纹理的特效,在这里还应该初始化各复活的粒子的纹理...
						if(m_ppsl->m_nParticleAniTextureIndex1 == 0)
							part->m_nStrTextureIndex = RandomNumber(0, nParPtexNum-1);
						else
							part->m_nStrTextureIndex = 0;
						part->m_nCurTextureIndex = part->m_nStrTextureIndex;
						//
					}
					else
						m_particleArray.Delete(q);

					if(m_ppsl->m_nMotionToPath == 1) //粒子死了把位置记录下来
						m_listPrePathIndex.push_back(part->m_nCurPathIndex);
				}
				else
				{
					//根据纹理个数产生一个随机数来给粒子赋值纹理..
					//根据粒子生命值和更新频率来改变粒子纹理...
					//单粒子动画--以下
					if(m_ppsl->m_nSingleParticleAni == 1)
					{
						int nTextureIndex = part->m_nStrTextureIndex;
						nTextureIndex = (int)( part->m_nStrTextureIndex + part->m_fAge *part->m_fAniSpeed +0.5f);
						part->m_nCurTextureIndex = nTextureIndex%nParPtexNum;
					}
					//以上
					//粒子沿着路径运动..以下
					if(m_ppsl->m_nMotionToPath == 1 && m_ppsl->m_nPosType == PSLTYPE_CUSTOMPATH)
					{
						if(cntPathPoint >0 )
						{
							int nPathIndex = (int)(part->m_nStrtPathIndex + part->m_fAge * part->m_fPathMotionSpeed);
							part->m_nCurPathIndex = nPathIndex %cntPathPoint;
						}
						else
						{
							part->m_nCurPathIndex = 0;
						}
					}
					//以上..

					//计算粒子的位置..(全屏特效有些情况需要重置Age)以下...
					float fZoomTime = fTimeDelta *fPslZoom; //速度和sizex y 、发射器范围乘了这个参数..					
					part->m_vPos += part->m_vDir*fZoomTime; 
					if(m_ppsl->m_nPosType == PSLTYPE_CUSTOMPATH && m_ppsl->m_nMotionToPath == 1)
					{
						float x = 100.0f;
						float y = 100.0f;

						if( cntPathPoint >0 )
						{
							x = m_vectorFullPathPoint[part->m_nCurPathIndex].x;
							y = m_vectorFullPathPoint[part->m_nCurPathIndex].y;
							x = x + m_psParameter.m_psModifyParam.m_pointCent.x;
							y = -y +m_psParameter.m_psModifyParam.m_pointCent.y;

							if(m_ppsl->m_nAlignToPath == 1)
							{
								part->m_fSpinStrt = m_vectorPathAngle[part->m_nCurPathIndex] +m_ppsl->m_fAlignToMotionAngle;
								part->m_fSpinEnd = m_vectorPathAngle[part->m_nCurPathIndex] +m_ppsl->m_fAlignToMotionAngle;
								part->m_fSpinVar = 0.0f;
							}
						}	

						part->m_vPos.Reset( x, y, 0.0f);
					}
					else
					{ // m_ppsl->m_nMotionToPath == 1 && PSLTYPE_CUSTOMPATH 的情况在上面已经算出准确的位置了
						if(m_ppsl->m_nMoveType == PSLMOVETYPE_FOLLOWSPRITE)
						{
							part->m_vPos.x += m_POINTdxdy.x;
							part->m_vPos.y += m_POINTdxdy.y;
							//
							if(m_fParticleAngle>FIRE_EPSILON)
							{
								part->m_fAngle0 += m_fParticleAngle;
								float fEmitterRotate = TOANGLE(part->m_fAngle0);
								int angle = (int)(fEmitterRotate);
								float fXPCos = XPCOS(angle);
								float fXPSin = XPSIN(angle);
								float fcosSpinAngle = fXPCos - fXPSin*TORADIANS(fEmitterRotate - angle);//cos(fEmitterRotate);
								float fsinSpinAngle = fXPSin + fXPCos*TORADIANS(fEmitterRotate - angle);//sin(fEmitterRotate);
								float fSpeed = part->m_vDir0.Length();
								part->m_vDir0.Reset(fSpeed*fcosSpinAngle, fSpeed*fsinSpinAngle, 0.0f);

							//	int nSpinAngle = (int)(TOANGLE(m_fParticleAngle) +0.5f);
							//	float fcosSpinAngle = XPCOS(nSpinAngle); //
							//	float fsinSpinAngle = XPSIN(nSpinAngle);

							//	float fcosSpinAngle = cosf(m_fParticleAngle);
							//	float fsinSpinAngle = sinf(m_fParticleAngle);

								fEmitterRotate = TOANGLE(m_fParticleAngle);
								angle = (int)(fEmitterRotate); //拉格朗日插值近似计算sin cos
								fXPCos = XPCOS(angle);
								fXPSin = XPSIN(angle);
								fcosSpinAngle = fXPCos - fXPSin*TORADIANS(fEmitterRotate - angle);//cos(m_fParticleAngle);
								fsinSpinAngle = fXPSin + fXPCos*TORADIANS(fEmitterRotate - angle);//sin(m_fParticleAngle);

								NuclearFPoint pt0 = NuclearFPoint(part->m_vPos.x, part->m_vPos.y);
								NuclearFPoint pt = m_ppsl->m_pointEmitterSpinCent *fPslZoom + m_psParameter.m_psModifyParam.m_pointCent;
								pt0 -= pt;
								float x0 = pt0.x;
								float y0 = pt0.y;

								pt0.x = x0*fcosSpinAngle - y0*fsinSpinAngle;
								pt0.y = x0*fsinSpinAngle + y0*fcosSpinAngle;
								pt0 += pt;

								part->m_vPos.x = pt0.x;
								part->m_vPos.y = pt0.y;							
							}
						
						}
						else if(m_ppsl->m_nMoveType == PSLMOVETYPE_DEFAULT)
						{
							if(m_psParameter.m_bVertexRHWflag)
							{
								float fcoef = m_psParameter.m_psModifyParam.m_fParticlePosCoef;
								if(m_psParameter.m_psModifyParam.m_scaleType == XPEST_NOSCALE)
								{
									fcoef = 1.0f;
								}

								part->m_vPos.x -= m_ViewPortdxdy.x *fcoef;
								part->m_vPos.y -= m_ViewPortdxdy.y *fcoef;
							}
						}
						else
						{//全屏效果...
							part->m_vPos.x -= m_ViewPortdxdy.x;
							part->m_vPos.y -= m_ViewPortdxdy.y;

							//把看不见的区域的粒子移动到新产生的区域内...	
							//	if(fabs(m_ViewPortdxdy.x)>FIRE_EPSILON)
							//视口没变的情况也应该对粒子的“出去”“进来”做处理
							{
								if(part->m_vPos.x < -m_fparticleSize)
									part->m_vPos.x += m_preViewPort.Width() +2*m_fparticleSize;
								if(part->m_vPos.x > m_curViewPort.Width() +m_fparticleSize)
									part->m_vPos.x -= m_preViewPort.Width() +2*m_fparticleSize;
							}

							//	if(fabs(m_ViewPortdxdy.y)>FIRE_EPSILON)
							{ //改为对称移动效果会更好..
						/*		if(part->m_vPos.y < -m_fparticleSize)
								{
									part->m_vPos.y += m_preViewPort.Height() +2*m_fparticleSize;

									//	part->m_fAge += part->m_fLifetime*0.1f; //9-1
								}
						*/
								if(part->m_vPos.y > m_curViewPort.Height() +m_fparticleSize)
								{
									part->m_vPos.y -= m_preViewPort.Height() +2*m_fparticleSize;

									//	part->m_fFullScreenTime += part->m_fAge; //9-1
									//	part->m_fAge = 0.0f;
									//2009-9-2 任何地方修改了粒子的age都应该调update(age)重新计算新的大小颜色透明度等信息.
								}
							}
						}
					}

					//以下..计算粒子速度

					//2011-4-27- 粒子沿着矩形发射器顺时针运动...
					if(m_ppsl->m_nPosType == PSLTYPE_RECT && m_ppsl->m_nMotionToPath == 1)
					{
						float posx = part->m_vPos.x;
						float posy = part->m_vPos.y;

						//把粒子坐标还原成m_rct中记录的相对坐标... 只能还原没有旋转的发射器..
						posx -= m_psParameter.m_psModifyParam.m_pointCent.x;
						posy -= m_psParameter.m_psModifyParam.m_pointCent.y;

						float fx = 1.0f; //放大缩小倍数
						float fy = 1.0f;
						if(m_psParameter.m_psModifyParam.m_fRctWidth>0 && m_psParameter.m_psModifyParam.m_fRctHeight>0)
						{//2011-4-27-程序运行时指定矩形区域的界面特效
							fx = m_psParameter.m_psModifyParam.m_fRctWidth/(float)(m_ppsl->m_rct.Width());
							fy = m_psParameter.m_psModifyParam.m_fRctHeight/(float)(m_ppsl->m_rct.Height());
						}

						posx = posx/(fPslZoom *fx);
						posy = posy/(fPslZoom *fy);					
						//
						float left = static_cast<float>(m_ppsl->m_rct.left);
						float top = static_cast<float>(m_ppsl->m_rct.top);
						float right = static_cast<float>(m_ppsl->m_rct.right);
						float bottom = static_cast<float>(m_ppsl->m_rct.bottom);

						if(posx>=left-1.0f && posx<=right)
						{
							if(posy<=top+1.0f)
							{//左上角产生，水平右运动
								part->m_fAngle0 = 0.0f;
							}
							if(posy>=bottom)
							{//从右下角开始水平左运动
								part->m_fAngle0 = FIRE_PI;
							}
						}
						else
						{
							if(posx>right)
							{
								if(posy<=bottom)
								{
									part->m_fAngle0 = 0.5f*FIRE_PI;
								}
								else
								{
									part->m_fAngle0 = FIRE_PI;
								}
							}
							if(posx<left)
							{
								part->m_fAngle0 = 1.5f*FIRE_PI;
							}
						}

						if(posy<top)
						{
							m_particleArray.Delete(q);
						//	part->m_fAge = part->m_fLifetime +0.001f; //杀死粒子...
						}

						float fSpeed = part->m_vDir0.Length();
						int nAngle = (int)( TOANGLE(part->m_fAngle0) +0.5f);
						part->m_vDir0.Reset(fSpeed*XPCOS(nAngle), fSpeed*XPSIN(nAngle), 0.0f);

					}
					//


					float fAge = part->m_fAge; //计算速度用的
					float fAngle0 = part->m_fAngle0;////发射的初始角度.. 应该加上发射器旋转角度吧...？


					if(m_ppsl->m_nTrackFx || m_ppsl->m_nAlignToHole)
					{
						//弹道模拟---强制改变方向...
						POINT parPos;
						parPos.x = (int)(part->m_vPos.x);
						parPos.y = (int)(part->m_vPos.y);

						float fAngle = Computer2VectorAngle(NuclearVector2((float)(objPt.x-parPos.x), (float)(objPt.y-parPos.y)), NuclearVector2(0.0f, -1.0f));
						if(objPt.x-parPos.x <0)
							fAngle = 2*FIRE_PI - fAngle;

						part->m_fSpinStrt = (float)(TOANGLE(fAngle));
						part->m_fSpinEnd = part->m_fSpinStrt;//(float)(TOANGLE(fAngle));

						fAngle0 = FIRE_PI*1.5f +fAngle;

						float fSpeed = part->m_vDir0.Length();
						int nAngle = (int)( TOANGLE(fAngle0) +0.5f);
						part->m_vDir0.Reset(fSpeed*XPCOS(nAngle), fSpeed*XPSIN(nAngle), 0.0f);

						// 							if(m_ppsl->m_nTrackFx)
						// 							{//测试代码.. 当击中目标之后特效如何变化有待讨论...返回消息..触发另外的特效？
						// 								float fdis = ComputerDistance(parPos, pt);
						// 								if(fdis<8)
						// 								{
						// 									part->m_vDir.Reset(0, 0, 0);
						// 
						// 									part->m_fParSizeStrtX = 3.0f;
						// 									part->m_fParSizeStrtY = 3.0f;
						// 									part->m_fParSizeEndX = 3.0f;
						// 									part->m_fParSizeEndY = 3.0f;
						// 									part->m_fLifetime = 6.0f;
						// 								}
						// 							}
					}

					//阻挡线---反射线的处理--以下 --2009--11--04
					//不管是阻挡还是反射，处理的都仅仅是粒子的运动方向及速度
					float particlePosx = part->m_vPos.x -0.5f;
					float particlePosy = part->m_vPos.y -0.5f;
					NuclearPoint pt;
					pt.x = (int)(particlePosx -m_psParameter.m_psModifyParam.m_pointCent.x +0.5f);
					pt.y = (int)(particlePosy -m_psParameter.m_psModifyParam.m_pointCent.y +0.5f);

					int nBlockLine = m_psParameter.m_psModifyParam.m_vectorBlockLine.size();
					int nReflectLine = m_psParameter.m_psModifyParam.m_vectorReflectLine.size();
					
					bool bBlockLine = false;
					bool bReflectLine = false;
					
					for(int i=0; i<nBlockLine; i++)
					{
						LINE& line = m_psParameter.m_psModifyParam.m_vectorBlockLine[i];
						bBlockLine = PtInLine(pt, line);
						if(bBlockLine)
							break;
					}
					if(bBlockLine)
					{
					//	/*阻碍粒子通过,粒子会沿着移动..
					//	应该按照物理规律做运动分解..让粒子沿着阻碍线滑动...直线间夹角...
					//	实现有一定难度,现在实现一个不会移动的,以后有需求了再增加这种功能
					//	*/
					//  2009-11-11
						part->m_vDir.Reset(0, 0, 0); //这变成了粘板了...直接让粒子停在该线上
						part->m_nCurTextureIndex = part->m_nStrTextureIndex;
						part->m_fAngle = part->m_fAngleStrt = part->m_fAngleEnd;
						part->m_fSpin = part->m_fSpinStrt = part->m_fSpinEnd;
					}

					int nRefLineIndex = 0;
					for(int i=0; i<nReflectLine; i++)
					{
						LINE& line = m_psParameter.m_psModifyParam.m_vectorReflectLine[i];
						bReflectLine = PtInLine(pt, line);
						if(bReflectLine)
						{
							nRefLineIndex = i;
							break;
						}
					}
					if(bReflectLine)
					{
						//目前的反射只能一次反射，并且只能是直线运动的反射
						//二次反射或者类抛物线运动的粒子反射不符合物理规律...
						//因为为简化计算，总是把粒子的方向当作是从发射点到碰撞点的连线方向... 2009-11-13
						LINE& line = m_psParameter.m_psModifyParam.m_vectorReflectLine[nRefLineIndex];
						NuclearVector2 vectorLine;
						vectorLine.x = (float)(line.pt0.x - line.pt1.x);
						vectorLine.y = (float)(line.pt0.y - line.pt1.y);

						NuclearVector2 vectorParticle;
						vectorParticle.x = (float)pt.x;
						vectorParticle.y = (float)pt.y;

						if(pt.x - pt.y>0)//线性区域..斜对角线左上区。水平右和竖直下为正方向
						{
							vectorParticle.x = -vectorParticle.x;
							vectorParticle.y = -vectorParticle.y;
						}

						float fAngle = Computer2VectorAngle(vectorLine, vectorParticle); //0--3.14f
						//直线夹角
						float fDeltaAngle = 2.0f* (fAngle -1.5708f);//入射光线与反射光线之间的夹角
						//
					//	fAngle0 = FIRE_PI*1.5f +fAngle0; //大概模拟..

						fAngle0 += 3.1415926f; //运动反向... 这其实也是一种特效:) //2009-11-12
						fAngle0 += fDeltaAngle; //严格光线反射规律的反射角
					
						part->m_fAngle0 = fAngle0;
						int nAngle = (int)( TOANGLE(fAngle0) );
						float fSpeed = part->m_fPathMotionSpeed;//part->m_vDir0.Length();
						part->m_vDir0.Reset(fSpeed*XPCOS(nAngle), fSpeed*XPSIN(nAngle), 0.0f);
					}
					//以上

					if( !bBlockLine ) // && !bReflectLine
					{
						//重力等各系统参数的影响... //质量为1, 重力方向--向下或向上..
						//XPV = v0 + a*t;
						float dVy = m_fGravity*fAge;//0.5f *m_fGravity *powf(fAge, 2.0f);

						//径向加速度---切向加速度等信息...
						float dv = m_fRadialAcc*fAge;//0.5f *m_fRadialAcc *powf(fAge, 2.0f);
						int nAngle = (int)( TOANGLE(fAngle0) );

						float dVx = dv*XPCOS(nAngle);//径向
						dVy += dv*XPSIN(nAngle);

						dv = m_fTangAcc*fAge;//0.5f *m_fTangAcc *powf(fAge, 2.0f);//切向		

						dVx += dv*XPCOS(nAngle + 90); //cos(90+a) = -sin(a)
						dVy += dv*XPSIN(nAngle + 90); //sin(90+a) = cos(a)

						part->m_vDir.Reset(part->m_vDir0.x +dVx, part->m_vDir0.y +dVy, part->m_vDir0.z);						
					}				
				}
			}
		}

		m_POINTdxdy = NuclearFPoint(0.0f, 0.0f);
		m_fParticleAngle = 0.0f;
		m_nNumReLifePartsExcess += reLifeParNum;

		return CreateNewParticles(fTimeDelta);//创建新粒子
	}

	bool CGeneralParticleSystem::Render(bool bUpdate, bool bEndScene)
	{
		//正确情况下只应该在cycle=false age>syslife时 或者 没有资源的时候 返回false

		if(!m_bLoadedRes) //没有资源
			return false;

		if( !bUpdate )
		{
			float fCurTime = (float)GetMilliSeconds();
			float timeDelta = (fCurTime - m_fLastTime)*0.001f;	
			m_fLastTime = fCurTime;

			if(timeDelta > 0.1f)//第一次进来
			{//m_fLastTime初值为0，第一次timeDelta很大，导致最开始的onstop公告
				timeDelta = 0.0f;
			}
			bool bres = Update(timeDelta); 
			if(!bres)
				return false;
		}

		if(!m_pRefPSTextureInfo.IsValid())
			return false;

		bool berror = false;
		int TCTvectorSize = (int)( m_vertices_TCT.size() );
		int UTCTvectorSize = (int)( m_vertices_UTCT.size() );
		int nSysMaxParticles = m_nCurSysMaxParticles;
		int NumUsedElements = m_particleArray.GetNumUsedElements();
		if(m_psParameter.m_bVertexRHWflag)
		{
			if(TCTvectorSize < 4*nSysMaxParticles || nSysMaxParticles < NumUsedElements)
				berror = true;
		}
		else
		{
			if(UTCTvectorSize < 4*nSysMaxParticles || nSysMaxParticles < NumUsedElements)
				berror = true;
		}

		if(berror)
		{
			if( !(ParticleManager::m_logName.empty()) )
			{
				XPLOG_PSMHZX(L"+++++++++++++++++++++++++++++++++++++++++++\n");
				std::wstring str = m_psParameter.m_bVertexRHWflag ? L"---Render: m_bVertexRHWflag: true\n":L"---Render-m_bVertexRHWflag: false\n";
				std::wstring pslName = GetPslName();
				str = pslName +str;
				XPLOG_PSMHZX(str.c_str());

				XPLOG_PSMHZX(L"TCTsize:  %d   UTCTsize:  %d\n", TCTvectorSize, UTCTvectorSize);
				XPLOG_PSMHZX(L"nSysMaxParticles:  %d \n", m_nCurSysMaxParticles);
				XPLOG_PSMHZX(L"TotalElements:  %d \n", m_particleArray.GetTotalElements());
				XPLOG_PSMHZX(L"NumUsedElements:  %d \n", m_particleArray.GetNumUsedElements());
				XPLOG_PSMHZX(L"+++++++++++++++++++++++++++++++++++++++++++\n");

		//		MessageBox(NULL, L"粒子参数异常，请联系程序\n", L"错误\n", 0);
			}
			return false;
		}

		if(m_psParameter.m_bVertexRHWflag)
			return Render_TCT(m_pRefPSTextureInfo->PsTextureInfo.handle, bEndScene);
		//	return Render<NuclearTCT1Vertex>(m_pRefPSTextureInfo->PsTextureInfo.handle);

		return Render_UTCT(m_pRefPSTextureInfo->PsTextureInfo.handle);
	//	return Render<NuclearUTCT1Vertex>(m_pRefPSTextureInfo->PsTextureInfo.handle);		
	}

	// vertices里面的数据为顺时针方向
	//			0------------1  
	//			|-,          |  
	//			|  '.        |  
	//			|    `',     |  
	//			|       '.   |  
	//			|         `' |  
	//			3------------2  

	bool CGeneralParticleSystem::Render_TCT(PictureHandle handle, bool bEndScene) //NuclearTCT1Vertex
	{
// 		NuclearVector3 vDir0(5.0f, 6.0f, 0.0f); //3.14159265f精度远远不够...误差积累很严重
// 		float fSpeed000 = vDir0.Length();
// 		for(int i=0; i<100000; i++)//XPCOS(30)  //XPSIN(30)
// 		{
// 			float fSpeed = vDir0.Length();
// 			vDir0.Reset(fSpeed*XPCOS(30), fSpeed*XPSIN(30), 0.0f);
// 		}
// 		float fSpeed111 = vDir0.Length();

		if(!m_pRefPSTextureInfo.IsValid())
			return false;

		float fEmitterAngle = TOANGLE(m_psParameter.m_psModifyParam.m_fEmitterSpinAngle);

		float fPslZoom = m_ppsl->m_fPSLzoom * m_psParameter.m_psModifyParam.m_fEmissionScale;
		int nMaxSize = m_particleArray.GetTotalElements();
		int nVertexIndex = 0;

		float du = 1.0f/(m_pRefPSTextureInfo->PsTextureInfo.m_nPslTexCol);
		float dv = 1.0f/(m_pRefPSTextureInfo->PsTextureInfo.m_nPslTexRow);
		du /= 2.0f;
		dv /= 2.0f;
		int nTexNum = m_pRefPSTextureInfo->PsTextureInfo.m_nTexNum; //纹理数大于1的不允许自旋

		float fEmitterRotateCos = 0.0f;
		float fEmitterRotateSin = 0.0f;
		float fEmitterRotate = 0.0f;

		if(m_psParameter.m_bModifyLineEmitter)
			fEmitterRotate = TOANGLE(m_psParameter.m_psModifyParam.m_fLineEmitterRotate);

		if(m_ppsl->m_nMotionToPath == 1 && m_ppsl->m_nPosType == PSLTYPE_CUSTOMPATH)
			fEmitterRotate = TOANGLE(m_psParameter.m_psModifyParam.m_fEmitterSpinAngle);
		
		if( m_psParameter.m_bModifyLineEmitter || 
			(m_ppsl->m_nMotionToPath == 1 && m_ppsl->m_nPosType == PSLTYPE_CUSTOMPATH) )
		{
			int angle = (int)(fEmitterRotate); //拉格朗日插值近似计算sin cos
			float fXPCos = XPCOS(angle);
			float fXPSin = XPSIN(angle);
			fEmitterRotateCos = fXPCos - fXPSin*TORADIANS(fEmitterRotate - angle);//cos(fangle);
			fEmitterRotateSin = fXPSin + fXPCos*TORADIANS(fEmitterRotate - angle);//sin(fangle);
		}

		//粒子盲区相关
		int nsize = m_psParameter.m_psModifyParam.m_vectorRctBlindArea.size();
		//
		for(int q=0; q<nMaxSize; q++)
		{
			if(m_particleArray.IsAlive(q)) 
			{
				CParticle* part = m_particleArray.GetAt(q);
				assert(part);
				float particlePosx = part->m_vPos.x -0.5f;
				float particlePosy = part->m_vPos.y -0.5f;

				if(m_psParameter.m_bModifyLineEmitter ||
					(m_ppsl->m_nMotionToPath == 1 && m_ppsl->m_nPosType == PSLTYPE_CUSTOMPATH) )
				{
					float fx0 = m_psParameter.m_psModifyParam.m_pointCent.x;
					float fy0 = m_psParameter.m_psModifyParam.m_pointCent.y;

					particlePosx = (particlePosx - fx0) *m_psParameter.m_psModifyParam.m_fEmissionScale;
					particlePosy = (particlePosy - fy0) *m_psParameter.m_psModifyParam.m_fEmissionScale;

					float x0 = particlePosx;
					float y0 = particlePosy;

					particlePosx = x0*fEmitterRotateCos - y0*fEmitterRotateSin;
					particlePosy = x0*fEmitterRotateSin + y0*fEmitterRotateCos;

					particlePosx += fx0;
					particlePosy += fy0;
				}

				//出了视口的只更新不渲染...要经过D3D坐标变换的除外...._UTCT
			//	if(m_psParameter.m_bVertexRHWflag) //true			
				if( (particlePosy > m_curViewPort.Height() +m_fparticleSize) || (particlePosy < -m_fparticleSize) 
					|| (particlePosx > m_curViewPort.Width() +m_fparticleSize) || (particlePosx < -m_fparticleSize) )
					continue;

				//判断进入"粒子盲区"的
				NuclearPoint pt;
				pt.x = (int)(particlePosx -m_psParameter.m_psModifyParam.m_pointCent.x +0.5f);
				pt.y = (int)(particlePosy -m_psParameter.m_psModifyParam.m_pointCent.y +0.5f);
				bool bBlind = false;
				for(int i=0; i<nsize; i++)
				{
					bBlind = m_psParameter.m_psModifyParam.m_vectorRctBlindArea[i].PtInRect(pt);
					if(bBlind)
						break;
				}
				if(bBlind)
					continue;
				//

				//计算该粒子的纹理坐标
				float u[4];
				float v[4];
				float fangleuv = part->m_fAngle; //角度..
				if(m_ppsl->m_nMoveType == PSLMOVETYPE_FOLLOWSPRITE)
				{
					float fSpinAngle = TOANGLE(m_psParameter.m_psModifyParam.m_fEmitterSpinAngle) +m_ppsl->m_fEmitterSpin;			
					fangleuv -= fSpinAngle;
				}

				//使用了多纹理的psl, 粒子不允许自旋(纹理旋转..大图中的某个区域子图旋转了可能就采样到别的子图像素了...)
				if( nTexNum>1 )
					fangleuv = 0.0f;
				//

				//	int angle = (int)(fangleuv); //自旋角度用整数精度会导致特效有跳帧的感觉--09-7-28
				//测试用例 fog004.psl
				//float fangle = TORADIANS(fangleuv);

				int nRow = part->m_nCurTextureIndex/m_pRefPSTextureInfo->PsTextureInfo.m_nPslTexCol;
			//	int nCol = part->m_nCurTextureIndex%m_pRefPSTextureInfo->PsTextureInfo.m_nPslTexCol;
				int nCol = part->m_nCurTextureIndex - nRow*m_pRefPSTextureInfo->PsTextureInfo.m_nPslTexCol;

				float stru = nCol*du *2.0f +du;
				float strv = nRow*dv *2.0f +dv;

				int angle = (int)(fangleuv); //拉格朗日插值近似计算sin cos
				
				float fXPCos = XPCOS(angle);
				float fXPSin = XPSIN(angle);
				float fcos = fXPCos - fXPSin*TORADIANS(fangleuv - angle);//cos(fangle);
				float fsin = fXPSin + fXPCos*TORADIANS(fangleuv - angle);//sin(fangle);

				float dufcos = du*fcos;
				float dvfcos = dv*fcos;
				float dufsin = du*fsin;
				float dvfsin = dv*fsin;

				u[0] = (-dufcos) - (-dvfsin) +stru;
				v[0] = (-dufsin) + (-dvfcos) +strv;
				//	tu = tu<0 ? 0:(tu>1 ? 1:tu); //旋转了超出的区域，这样会把要留着的东西也删了部分..
				//	tv = tv<0 ? 0:(tv>1 ? 1:tv); //自旋时..通过设置clamp纹理寻址模式来避免这样的问题..

				u[1] = (dufcos) - (-dvfsin) +stru;
				v[1] = (dufsin) + (-dvfcos) +strv;

				u[2] = (dufcos) - (dvfsin) +stru;
				v[2] = (dufsin) + (dvfcos) +strv;

				u[3] = (-dufcos) - (dvfsin) +stru;
				v[3] = (-dufsin) + (dvfcos) +strv;

				if(m_ppsl->m_nUVmirrorh)
				{//水平镜像
					float tempu = u[1];
					float tempv = v[1];
					u[1] = u[0];
					v[1] = v[0];
					u[0] = tempu;
					v[0] = tempv;

					tempu = u[2];
					tempv = v[2];
					u[2] = u[3];
					v[2] = v[3];
					u[3] = tempu;
					v[3] = tempv;
				}
				if(m_ppsl->m_nUVmirrorv)
				{//垂直镜像
					float tempu = u[3];
					float tempv = v[3];
					u[3] = u[0];
					v[3] = v[0];
					u[0] = tempu;
					v[0] = tempv;

					tempu = u[1];
					tempv = v[1];
					u[1] = u[2];
					v[1] = v[2];
					u[2] = tempu;
					v[2] = tempv;
				}
				//以上

				float fSpin = part->m_fSpin; //旋转..

				 //最近几个月添加的参数以及新功能都没有在梦猪专用代码里做对应修改，如果以后用到了需要加上。
				if(m_ppsl->m_nAlignToEmitter)
				{
					fSpin += fEmitterAngle; //加上发射器角度
				}

				angle = (int)(fSpin +0.5f); //这个暂时还是用整数

				float fSizeX = part->m_fSizeX *fPslZoom; // 0--5
				float fSizeY = part->m_fSizeY *fPslZoom; // 0--5
				fSizeX *= m_pRefPSTextureInfo->PsTextureInfo.m_nTexWidth/2;
				fSizeY *= m_pRefPSTextureInfo->PsTextureInfo.m_nTexHeight/2;
				float fParticleSizeCoef = 1.0f;
				if(m_psParameter.m_psModifyParam.m_scaleType == XPEST_ALLSCALE)
					fParticleSizeCoef = m_psParameter.m_psModifyParam.m_fParticlePosCoef;
				if(m_psParameter.m_psModifyParam.m_scaleType == XPEST_ONLY_SCALE_POS)
					fParticleSizeCoef = m_psParameter.m_psModifyParam.m_fParticleSizeCoef;

				fSizeX *= fParticleSizeCoef;
				fSizeY *= fParticleSizeCoef;

				float fxOffset = -fSizeX*2*m_ppsl->m_fRotateCentX;
				float fyOffset = -fSizeY*2*m_ppsl->m_fRotateCentY;

				float posx = -fSizeX + fxOffset;
				float posy = -fSizeY + fyOffset;

				NuclearTCT1Vertex vertex;

				fcos = XPCOS(angle);
				fsin = XPSIN(angle);

				vertex.x = posx*fcos - posy*fsin +particlePosx;
				vertex.y = posx*fsin + posy*fcos +particlePosy;
				vertex.z = 0.0f;
				DWORD dwcolor = part->m_color.data;
				vertex.c = dwcolor;
				
				vertex.u = u[0];
				vertex.v = v[0];
				m_vertices_TCT[nVertexIndex++] = vertex;				

				posx = fSizeX + fxOffset;
				posy = -fSizeY + fyOffset;
				vertex.x = posx*fcos - posy*fsin +particlePosx;
				vertex.y = posx*fsin + posy*fcos +particlePosy;
				vertex.z = 0.0f;
				vertex.c = dwcolor;

				vertex.u = u[1];
				vertex.v = v[1];
				m_vertices_TCT[nVertexIndex++] = vertex;

				posx = +fSizeX + fxOffset;
				posy = +fSizeY + fyOffset;
				vertex.x = posx*fcos - posy*fsin +particlePosx;
				vertex.y = posx*fsin + posy*fcos +particlePosy;
				vertex.z = 0.0f;
				vertex.c = dwcolor;

				vertex.u = u[2];
				vertex.v = v[2];
				m_vertices_TCT[nVertexIndex++] = vertex;

				posx = -fSizeX + fxOffset;
				posy = +fSizeY + fyOffset;
				vertex.x = posx*fcos - posy*fsin +particlePosx;
				vertex.y = posx*fsin + posy*fcos +particlePosy;
				vertex.z = 0.0f;
				vertex.c = dwcolor;

				vertex.u = u[3];
				vertex.v = v[3];
				m_vertices_TCT[nVertexIndex++] = vertex;
			}
//			if(nVertexIndex > 399) //更新不是问题..主要是显卡处理..
//			{
//				m_pRenderer->DrawParticles(handle, &m_vertices_TCT[0], nVertexIndex);
//				nVertexIndex = 0;
//			}
		}

		if( nVertexIndex != 0)
		{//(int)m_vertices_TCT.size() //每次render的活着的粒子个数是不一样的，所以应该用nVertexIndex
			if(bEndScene)
			{
				return m_pRenderer->DrawParticles(handle, &m_vertices_TCT[0], nVertexIndex);
			}
			else
			{
				static XPTEXCOLORBLEND_TYPE shaderColorOPMap[12] = 
				{
					XPTOP_DEFAULT, //0
					XPTOP_DEFAULT, //1
					XPTOP_SELECTARG1, //2
					XPTOP_SELECTARG2, //3
					XPTOP_MODULATE, //4
					XPTOP_MODULATE2X, //5
					XPTOP_DEFAULT, //6
					XPTOP_ADD, //7
					XPTOP_ADDSIGNED, //8
					XPTOP_DEFAULT, //9
					XPTOP_SUBTRACT, //10
					XPTOP_ADDSMOOTH, //11
				};

				m_pRenderer->SetTextureColorBlendType(shaderColorOPMap[m_ppsl->m_nBlendMode]);

				return m_pRenderer->CollectParticles(handle, shaderColorOPMap[m_ppsl->m_nBlendMode],
											(PSL_RENDSTATE)(m_ppsl->m_nDestBlendMode), &m_vertices_TCT[0], nVertexIndex);
			}
		}
		else
			return false;

		return true;
	}

	bool CGeneralParticleSystem::Render_UTCT(PictureHandle handle) //NuclearUTCT1Vertex
	{
		if(!m_pRefPSTextureInfo.IsValid())
			return false;

		float fPslZoom = m_ppsl->m_fPSLzoom * m_psParameter.m_psModifyParam.m_fEmissionScale;
		int nMaxSize = m_particleArray.GetTotalElements();
		int nVertexIndex = 0;

		float du = 1.0f/(m_pRefPSTextureInfo->PsTextureInfo.m_nPslTexCol);
		float dv = 1.0f/(m_pRefPSTextureInfo->PsTextureInfo.m_nPslTexRow);

		du /= 2.0f;
		dv /= 2.0f;

		for(int q=0; q<nMaxSize; q++)  //m_iMaxParticles //NUMPARTICLES
		{
			if(m_particleArray.IsAlive(q)) 
			{
				CParticle* part = m_particleArray.GetAt(q);
				assert(part);
				float particlePosx = part->m_vPos.x;
				float particlePosy = part->m_vPos.y;

				//出了视口的只更新不渲染...要经过D3D坐标变换的除外....
// 				if(m_psParameter.m_bVertexRHWflag)
// 				{
// 					if( (particlePosy > m_curViewPort.Height() +m_fparticleSize) || (particlePosy < -m_fparticleSize) 
// 						|| (particlePosx > m_curViewPort.Width() +m_fparticleSize) || (particlePosx < -m_fparticleSize) )
// 						continue;
// 				}

				//计算该粒子的纹理坐标
				float u[4];
				float v[4];
				float fangleuv = part->m_fAngle; //角度..
				//	int angle = (int)(fangleuv); //自旋角度用整数精度会导致特效有跳帧的感觉--09-7-28
			//	float fangle = TORADIANS(fangleuv);

				int nRow = part->m_nCurTextureIndex/m_pRefPSTextureInfo->PsTextureInfo.m_nPslTexCol;
				//	int nCol = part->m_nCurTextureIndex%m_pRefPSTextureInfo->PsTextureInfo.m_nPslTexCol;
				int nCol = part->m_nCurTextureIndex - nRow*m_pRefPSTextureInfo->PsTextureInfo.m_nPslTexCol;

				float stru = nCol*du *2.0f +du;
				float strv = nRow*dv *2.0f +dv;

				int angle = (int)(fangleuv); 

				float fXPCos = XPCOS(angle);
				float fXPSin = XPSIN(angle);
				float fcos = fXPCos - fXPSin*TORADIANS(fangleuv - angle);//cos(fangle);
				float fsin = fXPSin + fXPCos*TORADIANS(fangleuv - angle);//sin(fangle);

				float dufcos = du*fcos;
				float dvfcos = dv*fcos;
				float dufsin = du*fsin;
				float dvfsin = dv*fsin;

				u[0] = (-dufcos) - (-dvfsin) +stru;
				v[0] = (-dufsin) + (-dvfcos) +strv;
				//	tu = tu<0 ? 0:(tu>1 ? 1:tu); //旋转了超出的区域，这样会把要留着的东西也删了部分..
				//	tv = tv<0 ? 0:(tv>1 ? 1:tv); //自旋时..通过设置clamp纹理寻址模式来避免这样的问题..

				u[1] = (dufcos) - (-dvfsin) +stru;
				v[1] = (dufsin) + (-dvfcos) +strv;

				u[2] = (dufcos) - (dvfsin) +stru;
				v[2] = (dufsin) + (dvfcos) +strv;

				u[3] = (-dufcos) - (dvfsin) +stru;
				v[3] = (-dufsin) + (dvfcos) +strv;

				if(m_ppsl->m_nUVmirrorh)
				{//水平镜像
					float tempu = u[1];
					float tempv = v[1];
					u[1] = u[0];
					v[1] = v[0];
					u[0] = tempu;
					v[0] = tempv;

					tempu = u[2];
					tempv = v[2];
					u[2] = u[3];
					v[2] = v[3];
					u[3] = tempu;
					v[3] = tempv;
				}
				if(m_ppsl->m_nUVmirrorv)
				{//垂直镜像
					float tempu = u[3];
					float tempv = v[3];
					u[3] = u[0];
					v[3] = v[0];
					u[0] = tempu;
					v[0] = tempv;

					tempu = u[1];
					tempv = v[1];
					u[1] = u[2];
					v[1] = v[2];
					u[2] = tempu;
					v[2] = tempv;
				}
				//以上

				float fSpin = part->m_fSpin; //旋转..
				angle = (int)(fSpin +0.5f); //这个暂时还是用整数

				float fSizeX = part->m_fSizeX *fPslZoom; // 0--5
				float fSizeY = part->m_fSizeY *fPslZoom; // 0--5
				fSizeX *= m_pRefPSTextureInfo->PsTextureInfo.m_nTexWidth/2;
				fSizeY *= m_pRefPSTextureInfo->PsTextureInfo.m_nTexHeight/2;

				float fxOffset = -fSizeX*2*m_ppsl->m_fRotateCentX;
				float fyOffset = -fSizeY*2*m_ppsl->m_fRotateCentY;

				float posx = -fSizeX + fxOffset;
				float posy = -fSizeY + fyOffset;

				NuclearUTCT1Vertex vertex;

				fcos = XPCOS(angle);
				fsin = XPSIN(angle);

				vertex.x = posx*fcos - posy*fsin +particlePosx;
				vertex.y = posx*fsin + posy*fcos +particlePosy;
				vertex.z = 0.0f;
				DWORD dwcolor = part->m_color.data;
				vertex.c = dwcolor;
			//	if( !m_psParameter.m_bVertexRHWflag ) 这个条件是必然成立的
					vertex.y = -vertex.y;

				vertex.u = u[0];
				vertex.v = v[0];
				m_vertices_UTCT[nVertexIndex++] = vertex;				

				posx = fSizeX + fxOffset;
				posy = -fSizeY + fyOffset;
				vertex.x = posx*fcos - posy*fsin +particlePosx;
				vertex.y = posx*fsin + posy*fcos +particlePosy;
				vertex.z = 0.0f;
				vertex.c = dwcolor;
			//	if( !m_psParameter.m_bVertexRHWflag )
					vertex.y = -vertex.y;

				vertex.u = u[1];
				vertex.v = v[1];
				m_vertices_UTCT[nVertexIndex++] = vertex;

				posx = +fSizeX + fxOffset;
				posy = +fSizeY + fyOffset;
				vertex.x = posx*fcos - posy*fsin +particlePosx;
				vertex.y = posx*fsin + posy*fcos +particlePosy;
				vertex.z = 0.0f;
				vertex.c = dwcolor;
			//	if( !m_psParameter.m_bVertexRHWflag )
					vertex.y = -vertex.y;

				vertex.u = u[2];
				vertex.v = v[2];
				m_vertices_UTCT[nVertexIndex++] = vertex;

				posx = -fSizeX + fxOffset;
				posy = +fSizeY + fyOffset;
				vertex.x = posx*fcos - posy*fsin +particlePosx;
				vertex.y = posx*fsin + posy*fcos +particlePosy;
				vertex.z = 0.0f;
				vertex.c = dwcolor;
			//	if( !m_psParameter.m_bVertexRHWflag )
					vertex.y = -vertex.y;

				vertex.u = u[3];
				vertex.v = v[3];
				m_vertices_UTCT[nVertexIndex++] = vertex;
			}
//			if(nVertexIndex > 399)
//			{
//				m_pRenderer->DrawParticles(handle, &m_vertices_UTCT[0], nVertexIndex);
//				nVertexIndex = 0;
//			}
		}

		if( nVertexIndex != 0 )
		{ //(int)m_vertices_UTCT.size()
			return m_pRenderer->DrawParticles(handle, &m_vertices_UTCT[0], nVertexIndex);
		}

		return true;
	}
/*	
	template<class VERTEX>
	bool CGeneralParticleSystem::Render(PictureHandle handle)
	{

		// vertices里面的数据为顺时针方向
		//			0------------1  
		//			|-,          |  
		//			|  '.        |  
		//			|    `',     |  
		//			|       '.   |  
		//			|         `' |  
		//			3------------2  

		if(!m_pRefPSTextureInfo.IsValid())
			return false;

		float fPslZoom = m_ppsl->m_fPSLzoom * m_psParameter.m_psModifyParam.m_fEmissionScale;
		std::vector<VERTEX> vertices;
		int nMaxSize = m_particleArray.GetTotalElements();
		vertices.resize(4*nMaxSize);
		int nVertexIndex = 0;

		// Render each particle //m_ppsl->m_nSysMaxParticles
		for(int q=0; q<nMaxSize; q++)  //m_iMaxParticles //NUMPARTICLES
		{
			if(m_particleArray.IsAlive(q)) 
			{
				CParticle* part = m_particleArray.GetAt(q);
				assert(part);
				float particlePosx = part->m_vPos.x;
				float particlePosy = part->m_vPos.y;

				//出了视口的只更新不渲染...要经过D3D坐标变换的除外..版本4605中删掉..

				//计算该粒子的纹理坐标
				float u[4];
				float v[4];
				float fangleuv = part->m_fAngle; //角度..
				//	int angle = (int)(fangleuv); //自旋角度用整数精度会导致特效有跳帧的感觉--09-7-28
				float fangle = TORADIANS(fangleuv);

				int nRow = part->m_nCurTextureIndex/m_pRefPSTextureInfo->PsTextureInfo.m_nPslTexCol;
				int nCol = part->m_nCurTextureIndex%m_pRefPSTextureInfo->PsTextureInfo.m_nPslTexCol;

				float du = 1.0f/(float)(m_pRefPSTextureInfo->PsTextureInfo.m_nPslTexCol);
				float dv = 1.0f/(float)(m_pRefPSTextureInfo->PsTextureInfo.m_nPslTexRow);
				float stru = nCol*du;
				float strv = nRow*dv;

				float fcos = cos(fangle);
				float fsin = sin(fangle);

				u[0] = (-du/2.0f)*fcos - (-dv/2.0f)*fsin +(stru+du/2.0f);
				v[0] = (-du/2.0f)*fsin + (-dv/2.0f)*fcos +(strv+dv/2.0f);
				//	tu = tu<0 ? 0:(tu>1 ? 1:tu); //旋转了超出的区域，这样会把要留着的东西也删了部分..
				//	tv = tv<0 ? 0:(tv>1 ? 1:tv); //自旋时..通过设置clamp纹理寻址模式来避免这样的问题..

				u[1] = (du/2.0f)*fcos - (-dv/2.0f)*fsin +(stru+du/2.0f);
				v[1] = (du/2.0f)*fsin + (-dv/2.0f)*fcos +(strv+dv/2.0f);

				u[2] = (du/2.0f)*fcos - (dv/2.0f)*fsin +(stru+du/2.0f);
				v[2] = (du/2.0f)*fsin + (dv/2.0f)*fcos +(strv+dv/2.0f);

				u[3] = (-du/2.0f)*fcos - (dv/2.0f)*fsin +(stru+du/2.0f);
				v[3] = (-du/2.0f)*fsin + (dv/2.0f)*fcos +(strv+dv/2.0f);

				if(m_ppsl->m_nUVmirrorh)
				{//水平镜像
					float tempu = u[1];
					float tempv = v[1];
					u[1] = u[0];
					v[1] = v[0];
					u[0] = tempu;
					v[0] = tempv;

					tempu = u[2];
					tempv = v[2];
					u[2] = u[3];
					v[2] = v[3];
					u[3] = tempu;
					v[3] = tempv;
				}
				if(m_ppsl->m_nUVmirrorv)
				{//垂直镜像
					float tempu = u[3];
					float tempv = v[3];
					u[3] = u[0];
					v[3] = v[0];
					u[0] = tempu;
					v[0] = tempv;

					tempu = u[1];
					tempv = v[1];
					u[1] = u[2];
					v[1] = v[2];
					u[2] = tempu;
					v[2] = tempv;
				}
				//以上

				float fSpin = part->m_fSpin; //旋转..
				int angle = (int)(fSpin +0.5f); //这个暂时还是用整数

				float fSizeX = part->m_fSizeX *fPslZoom; // 0--5
				float fSizeY = part->m_fSizeY *fPslZoom; // 0--5
				fSizeX *= m_pRefPSTextureInfo->PsTextureInfo.m_nTexWidth/2;
				fSizeY *= m_pRefPSTextureInfo->PsTextureInfo.m_nTexHeight/2;

				float posx = -fSizeX - fSizeX*2*m_ppsl->m_fRotateCentX;
				float posy = -fSizeY - fSizeY*2*m_ppsl->m_fRotateCentY;

				VERTEX vertex;

				fcos = XPCOS(angle);
				fsin = XPSIN(angle);

				vertex.x = posx*fcos - posy*fsin +particlePosx;
				vertex.y = posx*fsin + posy*fcos +particlePosy;
				vertex.z = 0.0f;
				DWORD dwcolor = part->m_color.ToDWORD();
				vertex.c = dwcolor;
				if( !m_psParameter.m_bVertexRHWflag )
					vertex.y = -vertex.y;

				vertex.u = u[0];
				vertex.v = v[0];
				//	vertices.push_back(vertex);
				vertices[nVertexIndex++] = vertex;				

				posx = fSizeX - fSizeX*2*m_ppsl->m_fRotateCentX;
				posy = -fSizeY - fSizeY*2*m_ppsl->m_fRotateCentY;
				vertex.x = posx*fcos - posy*fsin +particlePosx;
				vertex.y = posx*fsin + posy*fcos +particlePosy;
				vertex.z = 0.0f;
				vertex.c = dwcolor;
				if( !m_psParameter.m_bVertexRHWflag )
					vertex.y = -vertex.y;

				vertex.u = u[1];
				vertex.v = v[1];
				//	vertices.push_back(vertex);
				vertices[nVertexIndex++] = vertex;

				posx = +fSizeX - fSizeX*2*m_ppsl->m_fRotateCentX;
				posy = +fSizeY - fSizeY*2*m_ppsl->m_fRotateCentY;
				vertex.x = posx*fcos - posy*fsin +particlePosx;
				vertex.y = posx*fsin + posy*fcos +particlePosy;
				vertex.z = 0.0f;
				vertex.c = dwcolor;
				if( !m_psParameter.m_bVertexRHWflag )
					vertex.y = -vertex.y;

				vertex.u = u[2];
				vertex.v = v[2];
				//	vertices.push_back(vertex);
				vertices[nVertexIndex++] = vertex;

				posx = -fSizeX - fSizeX*2*m_ppsl->m_fRotateCentX;
				posy = +fSizeY - fSizeY*2*m_ppsl->m_fRotateCentY;
				vertex.x = posx*fcos - posy*fsin +particlePosx;
				vertex.y = posx*fsin + posy*fcos +particlePosy;
				vertex.z = 0.0f;
				vertex.c = dwcolor;
				if( !m_psParameter.m_bVertexRHWflag )
					vertex.y = -vertex.y;

				vertex.u = u[3];
				vertex.v = v[3];

				//	vertices.push_back(vertex);
				vertices[nVertexIndex++] = vertex;

			}
		}

		if( !vertices.empty() )
		{
			return m_pRenderer->DrawParticles(handle, &vertices[0],(int)vertices.size());
		}

		return true;
	}
*/

	void CGeneralParticleSystem::InitTexturePicSize()
	{
		//对于很大的粒子，如何准确判断粒子是否出屏幕了？..还需要粒子纹理的宽高和放大倍数.
		float zoomx = m_ppsl->m_fParSizeStrtX > m_ppsl->m_fParSizeEndX ? m_ppsl->m_fParSizeStrtX : m_ppsl->m_fParSizeEndX;
		float zoomy = m_ppsl->m_fParSizeStrtY > m_ppsl->m_fParSizeEndY ? m_ppsl->m_fParSizeStrtY : m_ppsl->m_fParSizeEndY;
		float particleSizex = m_pRefPSTextureInfo->PsTextureInfo.m_nTexWidth * zoomx/2.0f;//粒子的半径
		float particleSizey = m_pRefPSTextureInfo->PsTextureInfo.m_nTexHeight * zoomy/2.0f;

		m_fparticleSize = particleSizex > particleSizey ? particleSizex : particleSizey;
	}
	
	void CGeneralParticleSystem::SetPicHandleRefResource(const std::vector<NuclearHardRef<PSPICHANDLESTRUCT> >& vectorD3DTexture)
	{//使各小纹理图片的引用计数加1
		m_vectorD3DTexture.clear();
		m_vectorD3DTexture = vectorD3DTexture;
	}

	bool CGeneralParticleSystem::ReleaseResource()
	{
		m_vectorD3DTexture.clear();

		m_pRefPSTextureInfo = NuclearHardRef<PSTEXTUREINFOSTRUCT>();
		m_vertices_TCT.clear();
		m_vertices_UTCT.clear();
		m_particleArray.DeleteAll();
		m_bLoadedRes = false;
		return true;
	}

	void CGeneralParticleSystem::SetPSLPathPointVector(const std::wstring& strPathFileName)
	{ //编辑器专用
		m_ppsl->m_strFullPathFile = strPathFileName;
		LoadFullPathPoint();
	}

	void CGeneralParticleSystem::ComputePathAngle()
	{
		//计算m_vectorPathAngle;
		m_vectorPathAngle.clear();
		int cnt = (int)m_vectorFullPathPoint.size();
		float fAngle = 0.0f;
		float prefAngle = 0.0f;
		bool bFirstZero = false;

		m_vectorPathAngle.push_back( 0 ); //占个位，后面修改为与at(1)的值相同
		for(int i=1; i<cnt-1; i++)
		{
			NuclearFPoint p0 = m_vectorFullPathPoint[i];
			NuclearFPoint p1;
			p1.x = p0.x;
			p1.y = p0.y -1;
			NuclearFPoint p2 = m_vectorFullPathPoint[i+1];

			NuclearVector2 v1 = NuclearVector2(0.0f, -1.0f);
			NuclearVector2 v2 = NuclearVector2(p2.x-p0.x, p2.y-p0.y);

			float fResult = DotProduct(v1, v2);
			float fpV1Len = VectorLength(v1);
			float fpV2Len = VectorLength(v2);

			if(fpV1Len*fpV2Len < FIRE_EPSILON)
			{
				//		if(i==1)
				//		{
				//			bFirstZero = true;
				//		}
				//		else
				//		{
				fAngle = prefAngle;
				//		}
			}
			else
			{
				fAngle = fResult/(fpV1Len*fpV2Len);
				fAngle = acos(fAngle);
				if( v2.x >0 )
					fAngle = 2*FIRE_PI - fAngle;

				prefAngle = fAngle;
			}

			m_vectorPathAngle.push_back( TOANGLE(fAngle) );
		}
		m_vectorPathAngle.push_back( TOANGLE(fAngle) );
		//	if(bFirstZero)
		//	{
		//		bFirstZero = false;
		//		m_vectorPathAngle[1] = m_vectorPathAngle[2];
		//	}
		m_vectorPathAngle[0] = m_vectorPathAngle[1];
	}

	bool CGeneralParticleSystem::SetKeyPointIndex(int nIndex)
	{
		int cnt = (int)m_vectorFullPathPoint.size();
		if(nIndex > cnt-1)
		{
			return false;
		}
		if(cnt==0)
			m_nKeyPointIndex = -1;
		else
			m_nKeyPointIndex = nIndex;

		if(m_nKeyPointIndex == -1)
		{
			return false;
		}
		//按比例缩放粒子数、速度等参数---在使用的地方判断如果m_nKeyPointIndex != -1则缩放

 		int nTotalPoints = m_ppsl->m_nNumPointFullPath;
 		float fcoef = (float)m_nKeyPointIndex/(float)nTotalPoints;
 
 		int nSysMaxParticles = (int)(m_ppsl->m_nSysMaxParticles *fcoef);
 		int nEmissionRate = (int)(m_ppsl->m_nEmissionRate *fcoef);
 		ResizeParticleArry(nSysMaxParticles, nEmissionRate);

		Reset();

		return true;
	}

	void CGeneralParticleSystem::SetPathVector(const std::vector<POINT>& vectorFullPathPoint)
	{
		m_vectorFullPathPoint.clear();
		int cnt = (int)vectorFullPathPoint.size();
		for(int i=0; i<cnt; i++)
		{
			NuclearFPoint pt(vectorFullPathPoint[i]);
			m_vectorFullPathPoint.push_back(pt);
		}
		//计算m_vectorPathAngle;
		ComputePathAngle();
	}

	//2010--9-13 为方便测试关键帧路径的效果, 把m_ppsl->m_nNumPointFullPath == -1的情况忽略了。
	//如果现在还存在的这样的psl文件...等发现问题了再做修改。
	bool CGeneralParticleSystem::LoadFullPathPoint()
	{
		//ps..PathFileName仅仅是文件名。
		wchar_t workdir[MAX_PATH];
        assert(false);
//		GetModuleFileName(NULL, workdir, MAX_PATH);
		std::wstring strWorkDir = workdir;
		int npos = (int)strWorkDir.rfind('\\');
		strWorkDir = std::wstring(strWorkDir, 0, npos);
		std::wstring strFilePath = L"\\res\\effect\\particle\\path\\";
		strFilePath = strWorkDir + strFilePath + m_ppsl->m_strFullPathFile;

		CPslPathObject PslPathObject;

		bool bres = PslPathObject.LoadFromNativePath(strFilePath.c_str());
		if(!bres)
			return false;

		std::vector<POINT> vectorFullPathPoint;

		BOOL bVectorCharPath = PslPathObject.GetVectorCharPointFlag();
		BOOL bCurveSample = PslPathObject.GetCurveSampleFlag();
		if(bVectorCharPath)
		{
			vectorFullPathPoint = PslPathObject.GetFullPathPointVector();
		}
		else
		{
			std::vector<PATHPOINT> vectorPathPoint = PslPathObject.GetVectorPathPoint();
			//	ZoomPathPoint(vectorPathPoint, psl.m_fPathZoom, psl.m_fPathSpin); //更合理的做法是。。应该先解码之后再缩放... todo

			//PATHPOINT转换为point---自定义路径点解码
			int cnt = (int)vectorPathPoint.size();
			if(bCurveSample)
			{
				for(int i=0; i<cnt; i++)
					vectorFullPathPoint.push_back(vectorPathPoint[i].pt);
			}
			else
			{
				if(cnt==1)
				{
					vectorFullPathPoint.push_back(vectorPathPoint[0].pt);
				}
				for(int i=0; (i<cnt-1)&&(cnt>1); i++)
				{
					POINT pt0 = vectorPathPoint[i].pt;
					POINT pt1 = vectorPathPoint[i].next_ControlPt;
					POINT pt2 = vectorPathPoint[i +1].pre_ControlPt;
					POINT pt3 = vectorPathPoint[i +1].pt;
					float dis = ComputerDistance(pt0, pt3);
					float step = 3.0f/dis; //点的疏密控制...
					int nStep = (int)(dis/3.0f);
// 					if(m_ppsl->m_nNumPointFullPath == -1)
// 					{
// 						step = 8.0f/dis; //点的疏密控制...
// 						nStep = (int)(dis/8.0f);
// 					}

					for(int j=0; j<nStep; j++)
					{
						float u = j*step;
						POINT point;
						point = Computer3BezierCurvePoint(u, pt0, pt1, pt2, pt3);
						vectorFullPathPoint.push_back(point);
					}
					if(nStep*step < 1.0f)
						vectorFullPathPoint.push_back(pt3);
				}

// 				if(m_ppsl->m_nNumPointFullPath == -1)
// 				{
// 				}
// 				else
// 				{
// 					cnt = (int)vectorFullPathPoint.size();
// 					float coef = (float)cnt/(float)(m_ppsl->m_nNumPointFullPath);
// 
// 					m_ppsl->m_nEmitterSpeed = (int)(m_ppsl->m_nEmitterSpeed *coef);
// 					if(m_ppsl->m_nMotionToPath == 1)
// 					{
// 						m_ppsl->m_fSpeedMin *= coef;
// 						m_ppsl->m_fSpeedMax *= coef;
// 					}
// 				}
			}
		}
		//解码后缩放+旋转
		TransformPoint(vectorFullPathPoint, m_ppsl->m_fPathZoom, m_ppsl->m_fPathSpin, m_ppsl->m_nPathMirrorType);

		m_ppsl->m_nNumPointFullPath = (int)vectorFullPathPoint.size();
		//只在编辑器里调用的,调用一次之后再存盘就把-1的情况消灭了
		//计算m_vectorPathAngle;
		SetPathVector(vectorFullPathPoint);

		return true;
	}
};