#include "stdafx.h"
#include "GeneralParticleSystem.h"
#include "particlemanager.h"

namespace Nuclear
{
	CGeneralParticleSystem::CGeneralParticleSystem(const XHardRef<PSL>& RefPsl, const PSParameter& psParameter, IBaseRenderer *pRenderer,
		const std::vector<POINT>& vectorFullPathPoint, const std::wstring& pslName)
		:CParticleSystem(RefPsl, psParameter, pRenderer, pslName), m_pLoadingNotify(NULL), m_bLoadFaild(false)
	{
		assert(m_ppsl->m_nAbsrtactPsl != 1);
		//·���ļ���ȡ������particleManager��Create PS��ʱ��
		if(m_ppsl->m_nPosType == PSLTYPE_CUSTOMPATH)		
			SetPathVector(vectorFullPathPoint);//��ʼ��·������Ϣ..m_vectorFullPathPoint
		m_restTexRes = 0;

		m_nPosIndex = 0;
		m_fparticleSize = 50.0f;

		m_nCurSysMaxParticles = m_ppsl->m_nSysMaxParticles;
		m_nCurEmissionRate = m_ppsl->m_nEmissionRate;

		m_fRealSysLife = m_ppsl->m_fSysLife;

		m_bLoadedRes = false; //�����ﹹ��Ķ���û�м���������Դ�ġ�
		
		m_nKeyPointIndex = -1; //-1 ��ʾʹ������·��
		m_nLinePosIndex = -1;
	}

	CGeneralParticleSystem::~CGeneralParticleSystem(void)
	{//��СͼƬ�����ü�����1
		m_vectorD3DTexture.clear();
	}

	void CGeneralParticleSystem::SetParticleSysResouce(const XHardRef<PSTEXTUREINFOSTRUCT>& pRefPSTextureInfo)
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
		if( fabs(m_psParameter.time - PSDEFAULTTIME) > XP_EPSILON )
			m_fRealSysLife = m_psParameter.time;
		else
			m_fRealSysLife = m_ppsl->m_fSysLife;

		m_listPrePathIndex.clear();
		CParticleSystem::Reset();
	}

	void CGeneralParticleSystem::ResetVerticesVector()
	{//�����޸���m_psParameter.m_bVertexRHWflag
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

		//���������� --�����Ҫ�����ĸ���..	
		int iNumNewParts = 0;
		assert(m_ppsl->m_nAbsrtactPsl != 1);

		if(!m_ppsl->m_nEmitterType)
		{//������..
			if(cnt==0)
			{
				iNumNewParts = m_nCurSysMaxParticles - m_particleArray.GetNumUsedElements();//ʼ�ձ������������..
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

			if(iNumNewParts <= m_nNumReLifePartsExcess)//��ȥѭ������ʱ����ǿ�Ƹ�����
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

		if(m_nKeyPointIndex!=-1 && cntPathPoint>0) //2010-9-8 �ؼ�֡·����ز���
			cntPathPoint = m_nKeyPointIndex+1;

		//���������������һ��������������Ӹ�ֵ����..
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
			if( m_fSysAge>m_fRealSysLife )	//m_fRealSysLife �����޸ĵ�ʱ��
			{
				if(cnt==0)
				{ //����false������ɾ����..
					//		XPTRACE(L"��ѭ��?...�ѹ�������ϵͳ�����ڲ���û�д���������...\n");
					std::vector<IParticleNotify*>::iterator iter = m_vectNotifys.begin();
					for(;iter!=m_vectNotifys.end();++iter)
					{
						(*iter)->OnStop();
					}

					return false;
				}
				else//11-25--��ȫ��ɾ����Ϊ���ٲ���������..
					return true;
			}

			CParticle* part = m_particleArray.New();//����ҿ�λ�Ĺ����ǿ����Ż���...
													//ͨ����¼ÿ����ȥ������λ����ʵ��...����û�б�Ҫ..��Ϊ..
			//����ѭ����Ч���ҿ�λ�Ǻ��ٵ��õ�..(ֻ�ڲ���һ���ʱ���Ҫ�ҿ�λ,��һ��Ŀ�λ�Ǻܺ��ҵ�...����:))
			
			if(part == NULL)
			{
				if( !(ParticleManager::m_logName.empty()) )
					XPLOG_PSMHZX(L"m_particleArray.New() == null ...\n");
				return false;
			}
			cnt++; //��ʹ����������һ��

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

			float fcoef_strt = 10.0f; //��ĸΪ0, ��������ȡ���������ֵ
			if(m_ppsl->m_fParSizeStrtY > XP_EPSILON)
				fcoef_strt = m_ppsl->m_fParSizeStrtX/m_ppsl->m_fParSizeStrtY;
			float fcoef_end = 10.0f; //��ĸΪ0, ��������ȡ���������ֵ
			if(m_ppsl->m_fParSizeEndY > XP_EPSILON)
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

			//���²�����������ɫ��͸���ȡ��ߴ硢��ת�Ƕȵȸ�ֵ...
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

		float fDirAngle = -m_ppsl->m_fEmissionDirection; //��ʱ����ת
	
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

			float fAngle = Computer2VectorAngle(XPVECTOR2((float)(objPoint.x-parPos.x), (float)(objPoint.y-parPos.y)), XPVECTOR2(0.0f, -1.0f));
			if(objPoint.x-parPos.x <0)
				fAngle = 2*XP_PI - fAngle;

			part->m_fSpinStrt = TOANGLE(fAngle);
			part->m_fSpinEnd = TOANGLE(fAngle);
			part->m_fSpinVar = 0.0f;

			fDirAngle = XP_PI*1.5f +fAngle;
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
			{	//��������·���˶��ľ�û��Ҫ����������..
				//ֻ������û��༭·����������ѡ����ʱ�򱣳ֵ�������� //m_nPosIndex
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

		//2010-3-24 --��ֻ�����Ӳ�����ʱ��ı䷽�򣬼�����Ҫ�����κ�ʱ�����Ӷ����淢������ת
// 		if(m_ppsl->m_nAlignToEmitter)
// 		{
// 			float fEmitterAngle = TOANGLE(m_psParameter.m_psModifyParam.m_fEmitterSpinAngle);
// 			part->m_fSpinStrt += fEmitterAngle;
// 			part->m_fSpinEnd += fEmitterAngle;
// 		}

		//�����ʼ�������Ƕ�
		if(m_ppsl->m_nParticleRandAngleSelf)
		{
			float fRandAngle = RandomNumber(0.0f, 360.0f);
			fvalue1 = fRandAngle -m_ppsl->m_fAngleRandom;
			fvalue2 = fRandAngle +m_ppsl->m_fAngleRandom;
			fvalue1 = fvalue1<-360 ? -360:fvalue1;
			fvalue2 = fvalue2>360 ? 360:fvalue2;
			part->m_fAngleStrt = fvalue1;
			part->m_fAngleEnd = fvalue2;
			part->m_fAngleVar = 0.5f; //���Բ�ֵ�仯
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
		FPOINT pt0;
		FPOINT pt1;
		float fk = 0.0f;

		if(m_ppsl->m_nMoveType == PSLMOVETYPE_FULLSCREEN)
		{
			//����λ���޸�Ϊ��Ļ���ϵ����ϵ�һ����...
			//�е�������л�ȡ..
			float fViewWidth = (float)m_curViewPort.Width();
			m_ppsl->m_point0 = FPOINT(0, 0);
			m_ppsl->m_point1 = FPOINT(fViewWidth, 0); //1280
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
		case PSLTYPE_POINT: //�㷢��
			{
				pt0 = m_psParameter.m_psModifyParam.m_pointCent;
				FPOINT pt = m_ppsl->m_pointEmitterSpinCent *fPslZoom + m_psParameter.m_psModifyParam.m_pointCent;
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
				{ //���ֱ�����Ч
					if( fabs(x)>10.0f && fabs(y)<3.0f ) //��Ϊˮƽ�������
					{
						x = m_psParameter.m_psModifyParam.m_fLineLength/2.0f;
						y = 0;
					}
				}

				pt0 = FPOINT(-x, -y) +m_psParameter.m_psModifyParam.m_pointCent;
				pt1 = FPOINT(x, y) +m_psParameter.m_psModifyParam.m_pointCent;

				FPOINT pt = m_ppsl->m_pointEmitterSpinCent *fPslZoom + m_psParameter.m_psModifyParam.m_pointCent;
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
						y = fk*(x-pt0.x) +pt0.y;//ֱ�߷���y-y0 = k(x-x0);
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

		case PSLTYPE_CIRCLE: //��Բ
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

				angle = RandomNumber(-XP_PI, XP_PI);

				int nAngle = (int)( TOANGLE(angle)  +0.5f);
				x = rx *fPslZoom *XPCOS(nAngle) +(float)m_psParameter.m_psModifyParam.m_pointCent.x;
				y = ry *fPslZoom *XPSIN(nAngle) +(float)m_psParameter.m_psModifyParam.m_pointCent.y;

				pt0 = FPOINT(x, y);
				FPOINT pt = m_ppsl->m_pointEmitterSpinCent *fPslZoom + m_psParameter.m_psModifyParam.m_pointCent;
				pt0 -= pt;
				float x0 = pt0.x;
				float y0 = pt0.y;

				pt0.x = x0*fcosSpinAngle - y0*fsinSpinAngle;
				pt0.y = x0*fsinSpinAngle + y0*fcosSpinAngle;

				pt0 += pt;

				part->m_vPos.Reset( pt0.x, pt0.y, 0.0f );
			}
			break;

		case PSLTYPE_RECT: //����
			{
				float fx = 1.0f; //�Ŵ���С����
				float fy = 1.0f;

				if(m_psParameter.m_psModifyParam.m_fRctWidth>0 && m_psParameter.m_psModifyParam.m_fRctHeight>0)
				{//���ֱ�����Ч //2011-4-27-��������ʱָ����������Ľ�����Ч
					fx = m_psParameter.m_psModifyParam.m_fRctWidth/(float)(m_ppsl->m_rct.Width());
					fy = m_psParameter.m_psModifyParam.m_fRctHeight/(float)(m_ppsl->m_rct.Height());
				}

				x = (float)RandomNumber(m_ppsl->m_rct.left, m_ppsl->m_rct.right);
				y = (float)RandomNumber(m_ppsl->m_rct.top, m_ppsl->m_rct.bottom);

				if(m_ppsl->m_nParFillMode)
				{ //������������x��y
				}
				else
				{
					if(m_ppsl->m_nPathOrder)
					{//2011-4-27-����ֻ�����Ͻǲ���
						x = (float)(m_ppsl->m_rct.left);
						y = (float)(m_ppsl->m_rct.top);
					}
					else
					{
						rctRandSeed = RandomNumber(0, 39); //10����һ����
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

				Nuclear::FPOINT pt = m_ppsl->m_pointEmitterSpinCent *fPslZoom + m_psParameter.m_psModifyParam.m_pointCent;
				pt0 = FPOINT(x, y) +m_psParameter.m_psModifyParam.m_pointCent;
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
				//ʹ���Զ���·��--m_vectorFullPathPoint--��·���ļ�����ʼ��part->m_vPos
				int cntCustomPoints = (int)m_vectorFullPathPoint.size();

				if(m_nKeyPointIndex!=-1 && cntCustomPoints>0) //2010-9-8 �ؼ�֡·����ز���
					cntCustomPoints = m_nKeyPointIndex+1;

				if( cntCustomPoints==0 )
				{
					pt0.x = 100.0f;
					pt0.y = 100.0f;
				}
				else
				{
					if(m_ppsl->m_nPathOrder)
					{//ʱ�򱣳�
						if( m_nPosIndex > cntCustomPoints-1 )
							m_nPosIndex = 0;
						index = m_nPosIndex;

						float fcoef_temp = 1.0f;
 						if(m_nKeyPointIndex != -1)
 						{
 							int nTotalPoints = m_ppsl->m_nNumPointFullPath;
 							fcoef_temp = (float)m_nKeyPointIndex/(float)nTotalPoints;
 						}
						int nPathIndex = (int)(m_fSysAge * m_ppsl->m_nEmitterSpeed * fcoef_temp); //fTimeDelta��������..
						m_nPosIndex = nPathIndex %cntCustomPoints;
					}
					else
					{//���
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

					pt0 = FPOINT(x, y) +m_psParameter.m_psModifyParam.m_pointCent;
					Nuclear::FPOINT pt = m_ppsl->m_pointEmitterSpinCent *fPslZoom + m_psParameter.m_psModifyParam.m_pointCent;
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

		float fdeltatime = fTimeDelta; //��ʱ�����������ڷ�ѭ�����ӵ������� ---09--7-9
		//���⵱��ҷ���һ������֮����Ϸ����ʧȥ������,һ��ʱ�䴰�ڻָ�֮����Ч����...

		if(fTimeDelta > PSUPDATECRITICALTIME) //����0.2f�����������ͣ..
		{
			fTimeDelta = 0.0f;
			if(m_psParameter.cycle)
				fdeltatime = 0.0f;
		}

		if( fabs(m_psParameter.time - PSDEFAULTTIME) > XP_EPSILON )
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
			//		{//11-25--��ȫ��ɾ����Ϊ���ٲ���������..
			//			m_Particles.DeleteAll();
			//			return false; 
			//		}
		}

		int cntPathPoint = (int)m_vectorFullPathPoint.size();//�����Ѵ��ںͼ��������ɵ�λ�ö��õ���

		if(m_nKeyPointIndex!=-1 && cntPathPoint>0) //2010-9-8 �ؼ�֡·����ز���
			cntPathPoint = m_nKeyPointIndex+1;

		if(!m_pRefPSTextureInfo.IsValid())
			return false;
		int nParPtexNum = 0;
		nParPtexNum = m_pRefPSTextureInfo->PsTextureInfo.m_nTexNum;

		int cnt = m_particleArray.GetNumUsedElements();
		int reLifeParNum = 0; //ǿ�Ƹ���������

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

		//���´��ڵ�����
		for(int q=0; q<m_particleArray.GetTotalElements(); q++)
		{
			if( m_particleArray.IsAlive(q) )
			{
				CParticle* part = m_particleArray.GetAt(q);
				assert(part);

				if( !part->Update(fdeltatime) )
				{
					part->m_fFullScreenTime = 0.0f;
					if(m_psParameter.cycle && (m_ppsl->m_nEmitterType == 1) && fabs(m_psParameter.m_psModifyParam.m_fEmitterSpinAngle)<XP_EPSILON )
					{
						//ѭ��������������������Ӳ��ܸ���..
						//���Ч��
						//���︴����һ������..Ҫ��¼��������������һ��..
						reLifeParNum++;
						part->m_fAge = part->m_fAge - part->m_fLifetime; // ���������� age����ֱ������

						//��ʼ��λ��
						InitParticlePos(part);
						//��ʼ���ٶ�(��������)

						if( !(m_ppsl->m_nTrackFx || m_ppsl->m_nAlignToHole) )
						{
							POINT objPoint;
							InitParticleSpeed(part, objPoint);
						}
						
						InitParticleAlphaColor(part);

						//���²�����������ɫ��͸���ȡ��ߴ硢��ת�Ƕȵȸ�ֵ...����ͻ����ϴ����µ�ֵ
						part->Update(0.0f); //2009--9--2 -- ���������ͬʱҲ��Ҫ��ʼ����ɫ͸���ȴ�С����Ϣ

						//���ڶ��������Ч,�����ﻹӦ�ó�ʼ������������ӵ�����...
						if(m_ppsl->m_nParticleAniTextureIndex1 == 0)
							part->m_nStrTextureIndex = RandomNumber(0, nParPtexNum-1);
						else
							part->m_nStrTextureIndex = 0;
						part->m_nCurTextureIndex = part->m_nStrTextureIndex;
						//
					}
					else
						m_particleArray.Delete(q);

					if(m_ppsl->m_nMotionToPath == 1) //�������˰�λ�ü�¼����
						m_listPrePathIndex.push_back(part->m_nCurPathIndex);
				}
				else
				{
					//���������������һ��������������Ӹ�ֵ����..
					//������������ֵ�͸���Ƶ�����ı���������...
					//�����Ӷ���--����
					if(m_ppsl->m_nSingleParticleAni == 1)
					{
						int nTextureIndex = part->m_nStrTextureIndex;
						nTextureIndex = (int)( part->m_nStrTextureIndex + part->m_fAge *part->m_fAniSpeed +0.5f);
						part->m_nCurTextureIndex = nTextureIndex%nParPtexNum;
					}
					//����
					//��������·���˶�..����
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
					//����..

					//�������ӵ�λ��..(ȫ����Ч��Щ�����Ҫ����Age)����...
					float fZoomTime = fTimeDelta *fPslZoom; //�ٶȺ�sizex y ����������Χ�����������..					
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
					{ // m_ppsl->m_nMotionToPath == 1 && PSLTYPE_CUSTOMPATH ������������Ѿ����׼ȷ��λ����
						if(m_ppsl->m_nMoveType == PSLMOVETYPE_FOLLOWSPRITE)
						{
							part->m_vPos.x += m_POINTdxdy.x;
							part->m_vPos.y += m_POINTdxdy.y;
							//
							if(m_fParticleAngle>XP_EPSILON)
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
								angle = (int)(fEmitterRotate); //�������ղ�ֵ���Ƽ���sin cos
								fXPCos = XPCOS(angle);
								fXPSin = XPSIN(angle);
								fcosSpinAngle = fXPCos - fXPSin*TORADIANS(fEmitterRotate - angle);//cos(m_fParticleAngle);
								fsinSpinAngle = fXPSin + fXPCos*TORADIANS(fEmitterRotate - angle);//sin(m_fParticleAngle);

								FPOINT pt0 = FPOINT(part->m_vPos.x, part->m_vPos.y);
								FPOINT pt = m_ppsl->m_pointEmitterSpinCent *fPslZoom + m_psParameter.m_psModifyParam.m_pointCent;
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
						{//ȫ��Ч��...
							part->m_vPos.x -= m_ViewPortdxdy.x;
							part->m_vPos.y -= m_ViewPortdxdy.y;

							//�ѿ�����������������ƶ����²�����������...	
							//	if(fabs(m_ViewPortdxdy.x)>XP_EPSILON)
							//�ӿ�û������ҲӦ�ö����ӵġ���ȥ����������������
							{
								if(part->m_vPos.x < -m_fparticleSize)
									part->m_vPos.x += m_preViewPort.Width() +2*m_fparticleSize;
								if(part->m_vPos.x > m_curViewPort.Width() +m_fparticleSize)
									part->m_vPos.x -= m_preViewPort.Width() +2*m_fparticleSize;
							}

							//	if(fabs(m_ViewPortdxdy.y)>XP_EPSILON)
							{ //��Ϊ�Գ��ƶ�Ч�������..
								if(part->m_vPos.y < -m_fparticleSize)
								{
									part->m_vPos.y += m_preViewPort.Height() +2*m_fparticleSize;

									//	part->m_fAge += part->m_fLifetime*0.1f; //9-1
								}
								if(part->m_vPos.y > m_curViewPort.Height() +m_fparticleSize)
								{
									part->m_vPos.y -= m_preViewPort.Height() +2*m_fparticleSize;

									//	part->m_fFullScreenTime += part->m_fAge; //9-1
									//	part->m_fAge = 0.0f;
									//2009-9-2 �κεط��޸������ӵ�age��Ӧ�õ�update(age)���¼����µĴ�С��ɫ͸���ȵ���Ϣ.
								}
							}
						}
					}

					//����..���������ٶ�

					//2011-4-27- �������ž��η�����˳ʱ���˶�...
					if(m_ppsl->m_nPosType == PSLTYPE_RECT && m_ppsl->m_nMotionToPath == 1)
					{
						float posx = part->m_vPos.x;
						float posy = part->m_vPos.y;

						//���������껹ԭ��m_rct�м�¼���������... ֻ�ܻ�ԭû����ת�ķ�����..
						posx -= m_psParameter.m_psModifyParam.m_pointCent.x;
						posy -= m_psParameter.m_psModifyParam.m_pointCent.y;

						float fx = 1.0f; //�Ŵ���С����
						float fy = 1.0f;
						if(m_psParameter.m_psModifyParam.m_fRctWidth>0 && m_psParameter.m_psModifyParam.m_fRctHeight>0)
						{//2011-4-27-��������ʱָ����������Ľ�����Ч
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
							{//���Ͻǲ�����ˮƽ���˶�
								part->m_fAngle0 = 0.0f;
							}
							if(posy>=bottom)
							{//�����½ǿ�ʼˮƽ���˶�
								part->m_fAngle0 = XP_PI;
							}
						}
						else
						{
							if(posx>right)
							{
								if(posy<=bottom)
								{
									part->m_fAngle0 = 0.5f*XP_PI;
								}
								else
								{
									part->m_fAngle0 = XP_PI;
								}
							}
							if(posx<left)
							{
								part->m_fAngle0 = 1.5f*XP_PI;
							}
						}

						if(posy<top)
						{
							m_particleArray.Delete(q);
						//	part->m_fAge = part->m_fLifetime +0.001f; //ɱ������...
						}

						float fSpeed = part->m_vDir0.Length();
						int nAngle = (int)( TOANGLE(part->m_fAngle0) +0.5f);
						part->m_vDir0.Reset(fSpeed*XPCOS(nAngle), fSpeed*XPSIN(nAngle), 0.0f);

					}
					//


					float fAge = part->m_fAge; //�����ٶ��õ�
					float fAngle0 = part->m_fAngle0;////����ĳ�ʼ�Ƕ�.. Ӧ�ü��Ϸ�������ת�ǶȰ�...��


					if(m_ppsl->m_nTrackFx || m_ppsl->m_nAlignToHole)
					{
						//����ģ��---ǿ�Ƹı䷽��...
						POINT parPos;
						parPos.x = (int)(part->m_vPos.x);
						parPos.y = (int)(part->m_vPos.y);

						float fAngle = Computer2VectorAngle(XPVECTOR2((float)(objPt.x-parPos.x), (float)(objPt.y-parPos.y)), XPVECTOR2(0.0f, -1.0f));
						if(objPt.x-parPos.x <0)
							fAngle = 2*XP_PI - fAngle;

						part->m_fSpinStrt = (float)(TOANGLE(fAngle));
						part->m_fSpinEnd = part->m_fSpinStrt;//(float)(TOANGLE(fAngle));

						fAngle0 = XP_PI*1.5f +fAngle;

						float fSpeed = part->m_vDir0.Length();
						int nAngle = (int)( TOANGLE(fAngle0) +0.5f);
						part->m_vDir0.Reset(fSpeed*XPCOS(nAngle), fSpeed*XPSIN(nAngle), 0.0f);

						// 							if(m_ppsl->m_nTrackFx)
						// 							{//���Դ���.. ������Ŀ��֮����Ч��α仯�д�����...������Ϣ..�����������Ч��
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

					//�赲��---�����ߵĴ���--���� --2009--11--04
					//�������赲���Ƿ��䣬����Ķ����������ӵ��˶������ٶ�
					float particlePosx = part->m_vPos.x -0.5f;
					float particlePosy = part->m_vPos.y -0.5f;
					CPOINT pt;
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
					//	/*�谭����ͨ��,���ӻ������ƶ�..
					//	Ӧ�ð�������������˶��ֽ�..�����������谭�߻���...ֱ�߼�н�...
					//	ʵ����һ���Ѷ�,����ʵ��һ�������ƶ���,�Ժ������������������ֹ���
					//	*/
					//  2009-11-11
						part->m_vDir.Reset(0, 0, 0); //������ճ����...ֱ��������ͣ�ڸ�����
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
						//Ŀǰ�ķ���ֻ��һ�η��䣬����ֻ����ֱ���˶��ķ���
						//���η���������������˶������ӷ��䲻�����������...
						//��ΪΪ�򻯼��㣬���ǰ����ӵķ������Ǵӷ���㵽��ײ������߷���... 2009-11-13
						LINE& line = m_psParameter.m_psModifyParam.m_vectorReflectLine[nRefLineIndex];
						XPVECTOR2 vectorLine;
						vectorLine.x = (float)(line.pt0.x - line.pt1.x);
						vectorLine.y = (float)(line.pt0.y - line.pt1.y);

						XPVECTOR2 vectorParticle;
						vectorParticle.x = (float)pt.x;
						vectorParticle.y = (float)pt.y;

						if(pt.x - pt.y>0)//��������..б�Խ�����������ˮƽ�Һ���ֱ��Ϊ������
						{
							vectorParticle.x = -vectorParticle.x;
							vectorParticle.y = -vectorParticle.y;
						}

						float fAngle = Computer2VectorAngle(vectorLine, vectorParticle); //0--3.14f
						//ֱ�߼н�
						float fDeltaAngle = 2.0f* (fAngle -1.5708f);//��������뷴�����֮��ļн�
						//
					//	fAngle0 = XP_PI*1.5f +fAngle0; //���ģ��..

						fAngle0 += 3.1415926f; //�˶�����... ����ʵҲ��һ����Ч:) //2009-11-12
						fAngle0 += fDeltaAngle; //�ϸ���߷�����ɵķ����
					
						part->m_fAngle0 = fAngle0;
						int nAngle = (int)( TOANGLE(fAngle0) );
						float fSpeed = part->m_fPathMotionSpeed;//part->m_vDir0.Length();
						part->m_vDir0.Reset(fSpeed*XPCOS(nAngle), fSpeed*XPSIN(nAngle), 0.0f);
					}
					//����

					if( !bBlockLine ) // && !bReflectLine
					{
						//�����ȸ�ϵͳ������Ӱ��... //����Ϊ1, ��������--���»�����..
						//XPV = v0 + a*t;
						float dVy = m_fGravity*fAge;//0.5f *m_fGravity *powf(fAge, 2.0f);

						//������ٶ�---������ٶȵ���Ϣ...
						float dv = m_fRadialAcc*fAge;//0.5f *m_fRadialAcc *powf(fAge, 2.0f);
						int nAngle = (int)( TOANGLE(fAngle0) );

						float dVx = dv*XPCOS(nAngle);//����
						dVy += dv*XPSIN(nAngle);

						dv = m_fTangAcc*fAge;//0.5f *m_fTangAcc *powf(fAge, 2.0f);//����		

						dVx += dv*XPCOS(nAngle + 90); //cos(90+a) = -sin(a)
						dVy += dv*XPSIN(nAngle + 90); //sin(90+a) = cos(a)

						part->m_vDir.Reset(part->m_vDir0.x +dVx, part->m_vDir0.y +dVy, part->m_vDir0.z);						
					}				
				}
			}
		}

		m_POINTdxdy = FPOINT(0.0f, 0.0f);
		m_fParticleAngle = 0.0f;
		m_nNumReLifePartsExcess += reLifeParNum;

		return CreateNewParticles(fTimeDelta);//����������
	}

	bool CGeneralParticleSystem::Render(bool bUpdate, bool bEndScene)
	{
		//��ȷ�����ֻӦ����cycle=false age>syslifeʱ ���� û����Դ��ʱ�� ����false

		if(!m_bLoadedRes) //û����Դ
			return false;

		if( !bUpdate )
		{
			float fCurTime = (float)timeGetTime();
			float timeDelta = (fCurTime - m_fLastTime)*0.001f;	
			m_fLastTime = fCurTime;

			if(timeDelta > 0.1f)//��һ�ν���
			{//m_fLastTime��ֵΪ0����һ��timeDelta�ܴ󣬵����ʼ��onstop����
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

		//		MessageBox(NULL, L"���Ӳ����쳣������ϵ����\n", L"����\n", 0);
			}
			return false;
		}

		if(m_psParameter.m_bVertexRHWflag)
			return Render_TCT(m_pRefPSTextureInfo->PsTextureInfo.handle, bEndScene);
		//	return Render<TCT1VERTEX>(m_pRefPSTextureInfo->PsTextureInfo.handle);

		return Render_UTCT(m_pRefPSTextureInfo->PsTextureInfo.handle);
	//	return Render<UTCT1VERTEX>(m_pRefPSTextureInfo->PsTextureInfo.handle);		
	}

	// vertices���������Ϊ˳ʱ�뷽��
	//			0------------1  
	//			|-,          |  
	//			|  '.        |  
	//			|    `',     |  
	//			|       '.   |  
	//			|         `' |  
	//			3------------2  

	bool CGeneralParticleSystem::Render_TCT(PictureHandle handle, bool bEndScene) //TCT1VERTEX
	{
// 		XPVECTOR3 vDir0(5.0f, 6.0f, 0.0f); //3.14159265f����ԶԶ����...�����ۺ�����
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
			int angle = (int)(fEmitterRotate); //�������ղ�ֵ���Ƽ���sin cos
			float fXPCos = XPCOS(angle);
			float fXPSin = XPSIN(angle);
			fEmitterRotateCos = fXPCos - fXPSin*TORADIANS(fEmitterRotate - angle);//cos(fangle);
			fEmitterRotateSin = fXPSin + fXPCos*TORADIANS(fEmitterRotate - angle);//sin(fangle);
		}

		//����ä�����
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

				//�����ӿڵ�ֻ���²���Ⱦ...Ҫ����D3D����任�ĳ���...._UTCT
			//	if(m_psParameter.m_bVertexRHWflag) //true			
				if( (particlePosy > m_curViewPort.Height() +m_fparticleSize) || (particlePosy < -m_fparticleSize) 
					|| (particlePosx > m_curViewPort.Width() +m_fparticleSize) || (particlePosx < -m_fparticleSize) )
					continue;

				//�жϽ���"����ä��"��
				CPOINT pt;
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

				//��������ӵ���������
				float u[4];
				float v[4];
				float fangleuv = part->m_fAngle; //�Ƕ�..
				if(m_ppsl->m_nMoveType == PSLMOVETYPE_FOLLOWSPRITE)
				{
					float fSpinAngle = TOANGLE(m_psParameter.m_psModifyParam.m_fEmitterSpinAngle) +m_ppsl->m_fEmitterSpin;			
					fangleuv -= fSpinAngle;
				}
				//	int angle = (int)(fangleuv); //�����Ƕ����������Ȼᵼ����Ч����֡�ĸо�--09-7-28
				//�������� fog004.psl
				//float fangle = TORADIANS(fangleuv);

				int nRow = part->m_nCurTextureIndex/m_pRefPSTextureInfo->PsTextureInfo.m_nPslTexCol;
			//	int nCol = part->m_nCurTextureIndex%m_pRefPSTextureInfo->PsTextureInfo.m_nPslTexCol;
				int nCol = part->m_nCurTextureIndex - nRow*m_pRefPSTextureInfo->PsTextureInfo.m_nPslTexCol;

				float stru = nCol*du *2.0f +du;
				float strv = nRow*dv *2.0f +dv;

				int angle = (int)(fangleuv); //�������ղ�ֵ���Ƽ���sin cos
				
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
				//	tu = tu<0 ? 0:(tu>1 ? 1:tu); //��ת�˳����������������Ҫ���ŵĶ���Ҳɾ�˲���..
				//	tv = tv<0 ? 0:(tv>1 ? 1:tv); //����ʱ..ͨ������clamp����Ѱַģʽ����������������..

				u[1] = (dufcos) - (-dvfsin) +stru;
				v[1] = (dufsin) + (-dvfcos) +strv;

				u[2] = (dufcos) - (dvfsin) +stru;
				v[2] = (dufsin) + (dvfcos) +strv;

				u[3] = (-dufcos) - (dvfsin) +stru;
				v[3] = (-dufsin) + (dvfcos) +strv;

				if(m_ppsl->m_nUVmirrorh)
				{//ˮƽ����
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
				{//��ֱ����
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
				//����

				float fSpin = part->m_fSpin; //��ת..

				 //�����������ӵĲ����Լ��¹��ܶ�û��������ר�ô���������Ӧ�޸ģ�����Ժ��õ�����Ҫ���ϡ�
				if(m_ppsl->m_nAlignToEmitter)
				{
					fSpin += fEmitterAngle; //���Ϸ������Ƕ�
				}

				angle = (int)(fSpin +0.5f); //�����ʱ����������

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

				TCT1VERTEX vertex;

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
//			if(nVertexIndex > 399) //���²�������..��Ҫ���Կ�����..
//			{
//				m_pRenderer->DrawParticles(handle, &m_vertices_TCT[0], nVertexIndex);
//				nVertexIndex = 0;
//			}
		}

		if( nVertexIndex != 0)
		{//(int)m_vertices_TCT.size() //ÿ��render�Ļ��ŵ����Ӹ����ǲ�һ���ģ�����Ӧ����nVertexIndex
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

				RendererStateAutoRecover stateAutoRecover(m_pRenderer);

				m_pRenderer->SetTextureColorBlendType(shaderColorOPMap[m_ppsl->m_nBlendMode]);

				return m_pRenderer->CollectParticles(handle, shaderColorOPMap[m_ppsl->m_nBlendMode],
											(PSL_RENDSTATE)(m_ppsl->m_nDestBlendMode), &m_vertices_TCT[0], nVertexIndex);
			}
		}
		else
			return false;

		return true;
	}

	bool CGeneralParticleSystem::Render_UTCT(PictureHandle handle) //UTCT1VERTEX
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

				//�����ӿڵ�ֻ���²���Ⱦ...Ҫ����D3D����任�ĳ���....
// 				if(m_psParameter.m_bVertexRHWflag)
// 				{
// 					if( (particlePosy > m_curViewPort.Height() +m_fparticleSize) || (particlePosy < -m_fparticleSize) 
// 						|| (particlePosx > m_curViewPort.Width() +m_fparticleSize) || (particlePosx < -m_fparticleSize) )
// 						continue;
// 				}

				//��������ӵ���������
				float u[4];
				float v[4];
				float fangleuv = part->m_fAngle; //�Ƕ�..
				//	int angle = (int)(fangleuv); //�����Ƕ����������Ȼᵼ����Ч����֡�ĸо�--09-7-28
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
				//	tu = tu<0 ? 0:(tu>1 ? 1:tu); //��ת�˳����������������Ҫ���ŵĶ���Ҳɾ�˲���..
				//	tv = tv<0 ? 0:(tv>1 ? 1:tv); //����ʱ..ͨ������clamp����Ѱַģʽ����������������..

				u[1] = (dufcos) - (-dvfsin) +stru;
				v[1] = (dufsin) + (-dvfcos) +strv;

				u[2] = (dufcos) - (dvfsin) +stru;
				v[2] = (dufsin) + (dvfcos) +strv;

				u[3] = (-dufcos) - (dvfsin) +stru;
				v[3] = (-dufsin) + (dvfcos) +strv;

				if(m_ppsl->m_nUVmirrorh)
				{//ˮƽ����
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
				{//��ֱ����
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
				//����

				float fSpin = part->m_fSpin; //��ת..
				angle = (int)(fSpin +0.5f); //�����ʱ����������

				float fSizeX = part->m_fSizeX *fPslZoom; // 0--5
				float fSizeY = part->m_fSizeY *fPslZoom; // 0--5
				fSizeX *= m_pRefPSTextureInfo->PsTextureInfo.m_nTexWidth/2;
				fSizeY *= m_pRefPSTextureInfo->PsTextureInfo.m_nTexHeight/2;

				float fxOffset = -fSizeX*2*m_ppsl->m_fRotateCentX;
				float fyOffset = -fSizeY*2*m_ppsl->m_fRotateCentY;

				float posx = -fSizeX + fxOffset;
				float posy = -fSizeY + fyOffset;

				UTCT1VERTEX vertex;

				fcos = XPCOS(angle);
				fsin = XPSIN(angle);

				vertex.x = posx*fcos - posy*fsin +particlePosx;
				vertex.y = posx*fsin + posy*fcos +particlePosy;
				vertex.z = 0.0f;
				DWORD dwcolor = part->m_color.data;
				vertex.c = dwcolor;
			//	if( !m_psParameter.m_bVertexRHWflag ) ��������Ǳ�Ȼ������
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

		// vertices���������Ϊ˳ʱ�뷽��
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

				//�����ӿڵ�ֻ���²���Ⱦ...Ҫ����D3D����任�ĳ���..�汾4605��ɾ��..

				//��������ӵ���������
				float u[4];
				float v[4];
				float fangleuv = part->m_fAngle; //�Ƕ�..
				//	int angle = (int)(fangleuv); //�����Ƕ����������Ȼᵼ����Ч����֡�ĸо�--09-7-28
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
				//	tu = tu<0 ? 0:(tu>1 ? 1:tu); //��ת�˳����������������Ҫ���ŵĶ���Ҳɾ�˲���..
				//	tv = tv<0 ? 0:(tv>1 ? 1:tv); //����ʱ..ͨ������clamp����Ѱַģʽ����������������..

				u[1] = (du/2.0f)*fcos - (-dv/2.0f)*fsin +(stru+du/2.0f);
				v[1] = (du/2.0f)*fsin + (-dv/2.0f)*fcos +(strv+dv/2.0f);

				u[2] = (du/2.0f)*fcos - (dv/2.0f)*fsin +(stru+du/2.0f);
				v[2] = (du/2.0f)*fsin + (dv/2.0f)*fcos +(strv+dv/2.0f);

				u[3] = (-du/2.0f)*fcos - (dv/2.0f)*fsin +(stru+du/2.0f);
				v[3] = (-du/2.0f)*fsin + (dv/2.0f)*fcos +(strv+dv/2.0f);

				if(m_ppsl->m_nUVmirrorh)
				{//ˮƽ����
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
				{//��ֱ����
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
				//����

				float fSpin = part->m_fSpin; //��ת..
				int angle = (int)(fSpin +0.5f); //�����ʱ����������

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
		//���ںܴ�����ӣ����׼ȷ�ж������Ƿ����Ļ�ˣ�..����Ҫ��������Ŀ�ߺͷŴ���.
		float zoomx = m_ppsl->m_fParSizeStrtX > m_ppsl->m_fParSizeEndX ? m_ppsl->m_fParSizeStrtX : m_ppsl->m_fParSizeEndX;
		float zoomy = m_ppsl->m_fParSizeStrtY > m_ppsl->m_fParSizeEndY ? m_ppsl->m_fParSizeStrtY : m_ppsl->m_fParSizeEndY;
		float particleSizex = m_pRefPSTextureInfo->PsTextureInfo.m_nTexWidth * zoomx/2.0f;//���ӵİ뾶
		float particleSizey = m_pRefPSTextureInfo->PsTextureInfo.m_nTexHeight * zoomy/2.0f;

		m_fparticleSize = particleSizex > particleSizey ? particleSizex : particleSizey;
	}
	
	void CGeneralParticleSystem::SetPicHandleRefResource(const std::vector<XHardRef<PSPICHANDLESTRUCT>>& vectorD3DTexture)
	{//ʹ��С����ͼƬ�����ü�����1
		m_vectorD3DTexture.clear();
		m_vectorD3DTexture = vectorD3DTexture;
	}

	bool CGeneralParticleSystem::ReleaseResource()
	{
		m_vectorD3DTexture.clear();

		m_pRefPSTextureInfo = XHardRef<PSTEXTUREINFOSTRUCT>();
		m_vertices_TCT.clear();
		m_vertices_UTCT.clear();
		m_particleArray.DeleteAll();
		m_bLoadedRes = false;
		return true;
	}

	void CGeneralParticleSystem::SetPSLPathPointVector(const std::wstring& strPathFileName)
	{ //�༭��ר��
		m_ppsl->m_strFullPathFile = strPathFileName;
		LoadFullPathPoint();
	}

	void CGeneralParticleSystem::ComputePathAngle()
	{
		//����m_vectorPathAngle;
		m_vectorPathAngle.clear();
		int cnt = (int)m_vectorFullPathPoint.size();
		float fAngle = 0.0f;
		float prefAngle = 0.0f;
		bool bFirstZero = false;

		m_vectorPathAngle.push_back( 0 ); //ռ��λ�������޸�Ϊ��at(1)��ֵ��ͬ
		for(int i=1; i<cnt-1; i++)
		{
			FPOINT p0 = m_vectorFullPathPoint[i];
			FPOINT p1;
			p1.x = p0.x;
			p1.y = p0.y -1;
			FPOINT p2 = m_vectorFullPathPoint[i+1];

			XPVECTOR2 v1 = XPVECTOR2(0.0f, -1.0f);
			XPVECTOR2 v2 = XPVECTOR2(p2.x-p0.x, p2.y-p0.y);

			float fResult = DotProduct(v1, v2);
			float fpV1Len = VectorLength(v1);
			float fpV2Len = VectorLength(v2);

			if(fpV1Len*fpV2Len < XP_EPSILON)
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
					fAngle = 2*XP_PI - fAngle;

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
		//�������������������ٶȵȲ���---��ʹ�õĵط��ж����m_nKeyPointIndex != -1������

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
			FPOINT pt(vectorFullPathPoint[i]);
			m_vectorFullPathPoint.push_back(pt);
		}
		//����m_vectorPathAngle;
		ComputePathAngle();
	}

	//2010--9-13 Ϊ������Թؼ�֡·����Ч��, ��m_ppsl->m_nNumPointFullPath == -1����������ˡ�
	//������ڻ����ڵ������������ļ�...�ȷ��������������޸ġ�
	bool CGeneralParticleSystem::LoadFullPathPoint()
	{
		//ps..PathFileName�������ļ�����
		/*TCHAR workdir[MAX_PATH];
		GetModuleFileName(NULL, workdir, MAX_PATH);
		std::wstring strWorkDir = workdir;
		int npos = (int)strWorkDir.rfind('\\');
		strWorkDir = std::wstring(strWorkDir, 0, npos);
		std::wstring strFilePath = L"\\res\\effect\\particle\\path\\";*/
		
		std::wstring strFilePath;
		wchar_t workdir[MAX_PATH];
		::GetCurrentDirectory(MAX_PATH, workdir);
		std::wstring wdir = workdir;
		int tmppos = (int)wdir.rfind('\\');
		wdir = std::wstring(wdir, 0, tmppos);
		std::wstring tmpFilePath = _T("\\res");
		std::wstring strTemp = wdir + tmpFilePath;
		
		strFilePath = strTemp + L"\\effect\\particle\\path\\" + m_ppsl->m_strFullPathFile;

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
			//	ZoomPathPoint(vectorPathPoint, psl.m_fPathZoom, psl.m_fPathSpin); //������������ǡ���Ӧ���Ƚ���֮��������... todo

			//PATHPOINTת��Ϊpoint---�Զ���·�������
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
					float step = 3.0f/dis; //������ܿ���...
					int nStep = (int)(dis/3.0f);
// 					if(m_ppsl->m_nNumPointFullPath == -1)
// 					{
// 						step = 8.0f/dis; //������ܿ���...
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
		//���������+��ת
		TransformPoint(vectorFullPathPoint, m_ppsl->m_fPathZoom, m_ppsl->m_fPathSpin, m_ppsl->m_nPathMirrorType);

		m_ppsl->m_nNumPointFullPath = (int)vectorFullPathPoint.size();
		//ֻ�ڱ༭������õ�,����һ��֮���ٴ��̾Ͱ�-1�����������
		//����m_vectorPathAngle;
		SetPathVector(vectorFullPathPoint);

		return true;
	}
};