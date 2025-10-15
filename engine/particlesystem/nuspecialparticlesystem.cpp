#include "nuspecialparticlesystem.h"


namespace Nuclear
{

	CSpecialParticleSystem::CSpecialParticleSystem(const NuclearHardRef<PSL>& RefPsl, const PSParameter& psParameter, 
													IBaseRenderer *pRenderer, const std::wstring& pslName)
		:CParticleSystem(RefPsl, psParameter, pRenderer, pslName)
	{
		assert(m_ppsl->m_nAbsrtactPsl == 1);
		m_shapeList.clear();
		m_pD3dTextureShapes = INVALID_PICTURE_HANDLE;
		m_fShapePsScalex = 1.0f;
		m_fShapePsScaley = 1.0f;
		m_fmaxu = 1.0f;
		m_fmaxv = 1.0f;
		m_nShapeTextureWidth = 1024;
		m_nShapeTextureHeight = 768;
		m_fSilentTime = 0.0f;
		m_fSilentAge = 0.0f;
		m_bDir = false;
	}

	CSpecialParticleSystem::~CSpecialParticleSystem(void)
	{
	}

	bool CSpecialParticleSystem::CreateNewParticles(float fTimeDelta)
	{
		int cnt = m_particleArray.GetNumUsedElements();

		//创建新粒子 --先算出要创建的个数..	
		int iNumNewParts = 0;

		if(cnt == 0)
		{
			iNumNewParts = m_ppsl->m_nSysMaxParticles - m_particleArray.GetNumUsedElements();//始终保持最大粒子数..
		}
		else
			return true;
	
		float fPslZoom = m_ppsl->m_fPSLzoom * m_psParameter.m_psModifyParam.m_fEmissionScale;

		for(int q=0; q<iNumNewParts && cnt<m_ppsl->m_nSysMaxParticles; q++)
		{
			if( m_fSysAge>m_fRealSysLife )	//m_fRealSysLife 程序修改的时间
			{
				if(cnt==0)
				{ //返回false，可以删除了..
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

			CParticle* part = m_particleArray.New();
			if(part == NULL)
			{
				XPTRACE(L"m_particleArray.New() == null ...\n");
				return false;
			}
			cnt++; //已使用粒子数加一。

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

			//位置来自外面传入的 m_shapeList 根据cnt找到...
			SHAPE_LIST::iterator it = m_shapeList.begin()+cnt-1;
			SHAPE_LIST::const_iterator itend = m_shapeList.end();
			if(it != itend)
			{
				part->m_vPos0.Reset(it->center.u *m_nShapeTextureWidth -m_nShapeTextureWidth/2.0f,
					it->center.v *m_nShapeTextureHeight -m_nShapeTextureHeight/2.0f, 0.0f);

				float centx = m_nShapeTextureWidth/2.0f;
				float centy = m_nShapeTextureHeight/2.0f;
				part->m_vPos = part->m_vPos0 +NuclearVector3(centx, centy, 0.0f);
			}

			float fSpeed = RandomNumber(m_ppsl->m_fSpeedMin, m_ppsl->m_fSpeedMax);			
			part->m_fPathMotionSpeed = fSpeed;

			float fDirAngle = -m_ppsl->m_fEmissionDirection; //逆时针旋转
			fDirAngle = TORADIANS(fDirAngle);
			fDirAngle += m_psParameter.m_psModifyParam.m_fEmitterSpinAngle;

			float fSpreadAngle = m_ppsl->m_fEmissionSpread;
			fSpreadAngle = TORADIANS(fSpreadAngle);
			fDirAngle = RandomNumber(fDirAngle-fSpreadAngle/2.0f, fDirAngle+fSpreadAngle/2.0f);

			float fvalue1 = 0.0f;
			float fvalue2 = 0.0f;

			if(m_ppsl->m_nParticleRandAngle)
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

			//下面初始化自旋角度
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

			if(m_ppsl->m_nDirMotion)
			{
				SHAPE_LIST::iterator it = m_shapeList.begin()+cnt-1;
				SHAPE_LIST::const_iterator itend = m_shapeList.end();
				if(it != itend)
				{
					POINT pt0;
					POINT pt1;					
					pt0.x = m_nShapeTextureWidth/2;
					pt0.y = m_nShapeTextureHeight/2;
					pt1.x = (int)(it->center.u *m_nShapeTextureWidth);
					pt1.y = (int)(it->center.v *m_nShapeTextureHeight);

					float fAngle = Computer2VectorAngle(NuclearVector2((float)(pt1.x-pt0.x), (float)(pt1.y-pt0.y)), NuclearVector2(1.0f, 0.0f));
					if(pt1.y-pt0.y <0)
						fAngle = 2*FIRE_PI - fAngle; //

					fDirAngle = fAngle;
				}
			}
			else
			{
				int nNum = m_particleArray.GetTotalElements();
				if(nNum == 2) //应该在psl里增加一个参数来存储这种特殊的效果类型
				{
					if(m_bDir)
					{
						m_bDir = false;
						fDirAngle = m_ppsl->m_fEmissionDirection +180.0f; //逆时针旋转
						fDirAngle = fDirAngle>360.0f ? fDirAngle-360.0f:fDirAngle;
						fDirAngle = TORADIANS(fDirAngle);
					}
					else
					{
						m_bDir = true;
						fDirAngle = m_ppsl->m_fEmissionDirection; //逆时针旋转
						fDirAngle = TORADIANS(fDirAngle);
					}
				}
			}

			part->m_fAngle0 = fDirAngle;
			int nAngle = (int)( TOANGLE(fDirAngle) );
			part->m_vDir0.Reset(fSpeed*XPCOS(nAngle), fSpeed*XPSIN(nAngle), 0.0f);
			part->m_vDir.Reset(part->m_vDir0.x, part->m_vDir0.y, part->m_vDir0.z);

			InitParticleAlphaColor(part);

			//给新产生的粒子颜色、透明度、尺寸、旋转角度等赋值...北大青鸟bug
			part->Update(0.0f); //2009--5--31
		}

		return true;
	}

	void CSpecialParticleSystem::InitParticlePos(CParticle* part)const
	{ 
		//位置来自外面传入的 m_shapeList 根据cnt找到...
		part->m_vPos = part->m_vPos0;
		float x = m_nShapeTextureWidth/2.0f;
		float y = m_nShapeTextureHeight/2.0f;

		part->m_vPos.Add(x, y, 0.0f);
	}

	bool CSpecialParticleSystem::Update(float fTimeDelta)
	{
		if(m_bStop)
			fTimeDelta = 0.0f;
		
		if(m_pD3dTextureShapes == INVALID_PICTURE_HANDLE || m_shapeList.empty())
			return false;

		m_fSilentAge += fTimeDelta;

		if(m_fSilentAge < m_fSilentTime)
		{
			fTimeDelta = 0.0f;
			return true; //可以不用更新了...绘制的时候直接绘制整图。
		}

		float fPslZoom = m_ppsl->m_fPSLzoom * m_psParameter.m_psModifyParam.m_fEmissionScale;
		m_fSysAge += fTimeDelta;

		float fdeltatime = fTimeDelta; //临时保存下来加在粒子生命上 ---09--7-9

		if(fTimeDelta > PSUPDATECRITICALTIME) //0.2f
		{
			fTimeDelta = 0.0f;
			if(m_psParameter.cycle)
				fdeltatime = 0.0f;
		}

		m_fRealSysLife = m_ppsl->m_fSysLife;
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
		}

		int cnt = m_particleArray.GetNumUsedElements();
		int reLifeParNum = 0; //强制复活粒子数

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
					if(m_psParameter.cycle && (m_ppsl->m_nEmitterType == 1) && fabs(m_psParameter.m_psModifyParam.m_fEmitterSpinAngle)<FIRE_EPSILON ) //循环并且是连续发射的粒子才能复活..
					{ //提高效率，同时避免循环遍与遍之间的闪跳问题
						//这里复活了一个粒子..要记录下来后面少生成一个..
						reLifeParNum++;
						part->m_fAge = 0.0f;

						InitParticlePos(part);
						part->Update(0.0f); //2009--9--2 -- 复活的粒子同时也需要初始化颜色透明度大小等信息
					}
					else
						m_particleArray.Delete(q);
				}
				else
				{
					float fAge = part->m_fAge;
					float fAngle0 = part->m_fAngle0;//发射的初始角度.. 应该加上发射器旋转角度吧...？

					float dVy = m_fGravity*fAge;//0.5f *m_fGravity *powf(fAge, 2.0f);

					//径向加速度---切向加速度等信息...
					float dv = m_fRadialAcc*fAge;//0.5f *m_fRadialAcc *powf(fAge, 2.0f);
					int nAngle = (int)( TOANGLE(fAngle0) );
					float dVx = dv*XPCOS(nAngle);//径向
					dVy += dv*XPSIN(nAngle);

					dv = m_fTangAcc*fAge;//0.5f *m_fTangAcc *powf(fAge, 2.0f);//切向		
					dVx += dv*XPCOS(nAngle + 90);
					dVy += dv*XPSIN(nAngle + 90);
					part->m_vDir.Reset(part->m_vDir0.x +dVx, part->m_vDir0.y +dVy, part->m_vDir0.z);

					//计算粒子的位置....
					float fZoomTime = fTimeDelta *fPslZoom; //速度和sizex y 、发射器范围乘了这个参数..					
					part->m_vPos += part->m_vDir*fZoomTime; 

					if(m_ppsl->m_nMoveType == PSLMOVETYPE_FOLLOWSPRITE)
					{
						part->m_vPos.x += m_POINTdxdy.x;
						part->m_vPos.y += m_POINTdxdy.y;
					}
					else if(m_ppsl->m_nMoveType == PSLMOVETYPE_DEFAULT)
					{
						part->m_vPos.x -= m_ViewPortdxdy.x;
						part->m_vPos.y -= m_ViewPortdxdy.y;
					}
					else
					{//全屏效果...
						part->m_vPos.x -= m_ViewPortdxdy.x;
						part->m_vPos.y -= m_ViewPortdxdy.y;

						if(part->m_vPos.x < 0)
							part->m_vPos.x += m_preViewPort.Width();
						if(part->m_vPos.x > m_curViewPort.Width())
							part->m_vPos.x -= m_preViewPort.Width();

						if(part->m_vPos.y < 0)
						{
							part->m_vPos.y += m_preViewPort.Height();

							part->m_fAge += part->m_fLifetime*0.1f;
						}
						if(part->m_vPos.y > m_curViewPort.Height())
						{
							part->m_vPos.y -= m_preViewPort.Height();

							part->m_fFullScreenTime += part->m_fAge;
							part->m_fAge = 0.0f;
						}
					}
				}
			}
		}

		m_POINTdxdy = NuclearFPoint(0.0f, 0.0f);
		m_nNumReLifePartsExcess += reLifeParNum;

		return CreateNewParticles(fTimeDelta);//创建新粒子
	}

	bool CSpecialParticleSystem::Render(bool bUpdate, bool bEndScene)
	{
		// vertices里面的数据为顺时针方向
		//			0------------1  
		//			|-,          |  
		//			|  '.        |  
		//			|    `',     |  
		//			|       '.   |  
		//			|         `' |  
		//			3------------2  

		if( !bUpdate )
		{
            float fCurTime = (float)GetMilliSeconds();
			float timeDelta = (fCurTime - m_fLastTime)*0.001f;	
			m_fLastTime = fCurTime;
			m_fLastTime = fCurTime;

			bool bres = Update(timeDelta);
			if(!bres)
				return false;
		}

		if(m_fSilentAge < m_fSilentTime)
		{
			int numVertex = 4;
			std::vector<NuclearTCT1Vertex> vertices;
			vertices.resize(numVertex);

			float centx = m_curViewPort.Width()/2.0f -0.5f;  //(float)m_curViewPort.left + 
			float centy = m_curViewPort.Height()/2.0f -0.5f; //(float)m_curViewPort.top + 
			float dx = m_curViewPort.Width()/2.0f;
			float dy = m_curViewPort.Height()/2.0f;

			vertices[0].x = -dx +centx;
			vertices[0].y = -dy +centy;
			vertices[0].z = 0.0f;
			vertices[0].rhw = 1.0f;
			vertices[0].c = 0xffffffff;
			vertices[0].u = 0.0f;
			vertices[0].v = 0.0f;

			vertices[1].x = dx +centx;
			vertices[1].y = -dy +centy;
			vertices[1].z = 0.0f;
			vertices[1].rhw = 1.0f;
			vertices[1].c = 0xffffffff;
			vertices[1].u = m_fmaxu;
			vertices[1].v = 0.0f;

			vertices[2].x = dx +centx;
			vertices[2].y = dy +centy;
			vertices[2].z = 0.0f;
			vertices[2].rhw = 1.0f;
			vertices[2].c = 0xffffffff;
			vertices[2].u = m_fmaxu;
			vertices[2].v = m_fmaxv;

			vertices[3].x = -dx +centx;
			vertices[3].y = dy +centy;
			vertices[3].z = 0.0f;
			vertices[3].rhw = 1.0f;
			vertices[3].c = 0xffffffff;
			vertices[3].u = 0.0f;
			vertices[3].v = m_fmaxv;

			return m_pRenderer->DrawTriangleFan(m_pD3dTextureShapes, &vertices[0], numVertex);
		}

		float fcx = (float)m_curViewPort.Width()/(float)m_nShapeTextureWidth;
		float fcy = (float)m_curViewPort.Height()/(float)m_nShapeTextureHeight;

		for(int q=0; q<m_particleArray.GetTotalElements(); q++) //NUMPARTICLES
		{
			if(m_particleArray.IsAlive(q)) 
			{	
				CParticle* part = m_particleArray.GetAt(q);
				assert(part);

				float particlePosx = part->m_vPos.x *m_fShapePsScalex -0.5f;
				float particlePosy = part->m_vPos.y *m_fShapePsScaley -0.5f;
				if( (particlePosy > m_curViewPort.Height() +64) || (particlePosy < -256) 
					|| (particlePosx > m_curViewPort.Width() +64) || (particlePosx < -64) )
					continue;

				float fSpin = part->m_fSpin; //旋转..
				int angle = (int)(fSpin +0.5f);

				SHAPE shape = m_shapeList[q];
				float fCentu = shape.center.u;
				float fCentv = shape.center.v;

				int numVertex = (int)( shape.vertexs.size() );
				std::vector<NuclearTCT1Vertex> vertices;
				vertices.resize(numVertex);

				float fcos = XPCOS(angle);
				float fsin = XPSIN(angle);
				DWORD dwcolor = part->m_color.data;//ToDWORD();

				for(int i=0; i<numVertex; i++)
				{
					float posx = (shape.vertexs[numVertex-1-i].u -fCentu)*m_nShapeTextureWidth *part->m_fSizeX;
					float posy = (shape.vertexs[numVertex-1-i].v -fCentv)*m_nShapeTextureHeight *part->m_fSizeY;
					posx *= m_fShapePsScalex;
					posy *= m_fShapePsScaley;

					vertices[i].x = posx*fcos - posy*fsin +particlePosx;
					vertices[i].y = posx*fsin + posy*fcos +particlePosy;
					vertices[i].z = 0.0f;
					vertices[i].c = dwcolor;

					vertices[i].u = shape.vertexs[numVertex-1-i].u *m_fmaxu;
					vertices[i].v = shape.vertexs[numVertex-1-i].v *m_fmaxv;
				}

				if( !m_pRenderer->DrawTriangleFan(m_pD3dTextureShapes, &vertices[0], numVertex) )
					return false;
			}
		}

		return true;
	}

	void CSpecialParticleSystem::SetShapePsVertexs(const SHAPE_LIST& shapeList)
	{
		m_shapeList = shapeList;
		m_ppsl->m_nSysMaxParticles = (int)m_shapeList.size();

		m_particleArray.Resize(m_ppsl->m_nSysMaxParticles); 
		//上面todo做了之后在这里需要重新vertices.resize(...)数组

		Reset();
	}

	void CSpecialParticleSystem::SetSilentTime(float ftime)
	{
		m_fSilentTime = ftime;
	}

	bool CSpecialParticleSystem::SetShapePsTexture(PictureHandle pShapesTexture, float fu, float fv)
	{
		//图片左下角的纹理坐标 fu fv (在烂显卡上创建非2幂纹理时...)
		if(m_pD3dTextureShapes==pShapesTexture || pShapesTexture==INVALID_PICTURE_HANDLE)
			return false;

		m_fmaxu = fu;
		m_fmaxv = fv;
		m_pD3dTextureShapes = pShapesTexture;

		NuclearPictureInfo picinfo;
		if( !(m_pRenderer->GetPictureInfo(pShapesTexture, picinfo)) )
			return false;

		m_nShapeTextureWidth = picinfo.m_nPicWidth;
		m_nShapeTextureHeight = picinfo.m_nPicHeight;
		Reset(); //每帧设纹理..
		//Reset里面有对vector调用resize()..
		//但是只有确实修改了纹理代码才会流到这里..
		return true;
	}

	void CSpecialParticleSystem::SetShapePsScale(float fcx, float fcy)
	{
		m_fShapePsScalex = fcx;
		m_fShapePsScaley = fcy;
	}

	void CSpecialParticleSystem::Reset(void) 
	{
		m_fSilentAge = 0.0f;
		CParticleSystem::Reset();
	}

}