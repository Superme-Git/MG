#include "stdafx.h"
#include "..\renderer\renderer.h"
#include "..\engine\systemresourcemanager.h"
#include "..\engine\configmanager.h"
#include "picresrc.h"
#include "distortbase.h"

namespace Nuclear
{
	//不存在超过512宽高的img文件	
	TCT1VERTEX DistortBase::s_vertexs[1089]; //33*33
	WORD DistortBase::s_index[6144]; // 32*32 *6

	DistortBase::DistortBase(EngineBase* pEB, const DistortionEffectParam& effectParam)
		: Renderable(pEB), m_effectParam(effectParam), m_rct(0, 0, 0, 0), m_color(0xffffffff), m_fZoom(1.0f)
	{
		m_vectorImgInfo.clear();
	}

	DistortBase::~DistortBase(void)
	{
	}

	bool DistortBase::Render(Canvas* canvas)
	{
		if(NULL == m_pEngine)
			return false;

		int cnt = (int)m_vectorImgInfo.size();
		if(cnt<1)
			return false;

		int cnt2 = m_vectorLightImgInfo.size();
		bool hasLight = false;
		if(cnt2 == cnt)
		{
			hasLight = true;
		}

		CRECT vp = m_pEngine->GetViewport();

		int nFileWidth = m_vectorImgInfo[cnt-1].rect.right;
		int nFileHeight = m_vectorImgInfo[cnt-1].rect.bottom;
		//最后一块的right和bottom就是图像的宽、高。(详情见img文件结构)
		bool bres = IsPower2(nFileWidth) && IsPower2(nFileHeight);
		bool isCapsNoPow2 = true;
		SystemResourceManager* pSystemResoureManager = m_pEngine->GetSystemResourceManager();
		if(pSystemResoureManager != NULL)
			isCapsNoPow2 = pSystemResoureManager->IsTextureCapsNoPow2();
		
		bool bLightFlag = true;
		bool bEnableDis = true;
		bool bEnableNight = true;
		bool bNightFlag = true;
		ConfigManager* pCfgMan = m_pEngine->GetConfigManager();
		if(pCfgMan != NULL)
		{
			bEnableDis = pCfgMan->IsEnableLinkedObjs(); //dis文件与lko共用一个开关
			bEnableNight = pCfgMan->IsRenderNightEffectByShader();
		}

		int nGameTime = m_pEngine->GetGameTime();
		bNightFlag = nGameTime<XP_DAY_START_TIME || nGameTime>XP_NIGHT_START_TIME;

		bLightFlag = hasLight&&bNightFlag&&bEnableNight;

		if( !isCapsNoPow2 && !bres || !bEnableDis ) //
		{ //绘制一张静态图片
			for(int i=0; i<cnt; i++) 
			{
				PictureHandle handle = m_vectorImgInfo[i].pPicResrc->handle;
				if (handle == INVALID_PICTURE_HANDLE)
					continue;
				FRECT rct;
				rct = m_vectorImgInfo[i].rect.ToFRECT();
				rct += FPOINT((float)(m_rct.left), (float)(m_rct.top)); //得到该分块绘制的屏幕位置
				rct.left -= vp.left;
				rct.right -= vp.left;
				rct.top -= vp.top;
				rct.bottom -= vp.top;

				rct *= m_fZoom;
			
				DrawPictureParam param;
				param.handle = handle;
				param.pRect = &rct;
				XPCOLOR color(m_color);
				param.pColor = &color;
				param.colorCount = 1;
				param.blend = Nuclear::XPTEXBM_DEFAULT;

				RendererStateAutoRecover stateAutoRecover(m_pEngine->GetRenderer());

				m_pEngine->GetRenderer()->DrawPicture(param);

				if(bLightFlag)
				{
					PictureHandle lightHandle = m_vectorLightImgInfo[i].pPicResrc->handle;
					if(lightHandle == INVALID_PICTURE_HANDLE)
						continue;
					param.handle = lightHandle;
					
					m_pEngine->GetRenderer()->SetRenderEffect(XPRE_NULL);
					m_pEngine->GetRenderer()->DrawPicture(param);
					
				}
			}
			return false;
		}

		bool result = true;

		//大于512被拆成多块..
		int nTotalRows = nFileHeight/XPGRIDSIZE;// 原始图片宽高
		int nTotalCols = nFileWidth/XPGRIDSIZE;

		nTotalRows = XPGRIDSIZE*nTotalRows < nFileHeight ? (nTotalRows+1) : nTotalRows;
		nTotalCols = XPGRIDSIZE*nTotalCols < nFileWidth ? (nTotalCols+1) : nTotalCols;

		for(int i=0; i<cnt; i++) 
		{
			PictureHandle handle = m_vectorImgInfo[i].pPicResrc->handle;
			if (handle == INVALID_PICTURE_HANDLE)
			{
				result = false;
				continue;
			}

			CRECT rct;
			rct = m_vectorImgInfo[i].rect;
			//每块应该记录一个行列号的开始索引..用来快速找到振幅和准确控制顶点移动
			int nRowIndex = rct.top/XPGRIDSIZE;
			int nColIndex = rct.left/XPGRIDSIZE;
			nRowIndex = XPGRIDSIZE*nRowIndex < rct.top ? (nRowIndex+1) : nRowIndex;
			nColIndex = XPGRIDSIZE*nColIndex < rct.left ? (nColIndex+1) : nColIndex;

			int nRowOffset = 0;
			int nColOffset = 0;
			float fcoefx = (float)(nColIndex)/(float)nTotalCols;
			float fcoefy = (float)(nRowIndex)/(float)nTotalRows;
			fcoefx = fcoefx>1.0f ? 1.0f: fcoefx;
			fcoefy = fcoefy>1.0f ? 1.0f: fcoefy;

			const std::vector<FPOINT>& fAmplitudes = m_effectParam.fAmplitudes;
			int nTemp = (int)fAmplitudes.size();

			while(fAmplitudes[nColOffset].x < fcoefx)
				nColOffset++;

			while(fAmplitudes[nRowOffset].x < fcoefy)
				nRowOffset++;

			nRowOffset = nRowOffset>(nTemp-1) ? (nTemp-1): nRowOffset;
			nColOffset = nColOffset>(nTemp-1) ? (nTemp-1): nColOffset;

			int picW = rct.Width();
			int picH = rct.Height();
			int nRows = picH/XPGRIDSIZE +1; //初始化顶点用的...顶点数比等份数多一
			int nCols = picW/XPGRIDSIZE +1;
			nRows = nRows>1 ? nRows:2; //处理不到一个格的情况...比如: picW<XPGRIDSIZE
			nCols = nCols>1 ? nCols:2; //这种情况只能出现在图片的最后一行或最后一列..其实基本都是全透明区域..

	//		if(picH%XPGRIDSIZE != 0)
	//			nRows += 1;
	//		if(picW%XPGRIDSIZE != 0)
	//			nCols += 1;
			//其实当picH/XPGRIDSIZE 或者 picW/XPGRIDSIZE不能整除的情况，要记录最后一格的宽高...
			//这样才能正确绘制，现在是把不能整除的格当作XPGRIDSIZE大小绘制了, 基本都是透明区域，不画或者近似画都可以.
		
			float deltax = (float)picW/(float)(nCols-1.0f);
			float deltay = (float)picH/(float)(nRows-1.0f);

			float deltau = 1.0f/(nCols-1.0f);
			float deltav = 1.0f/(nRows-1.0f);

			rct += CPOINT(m_rct.left, m_rct.top); //得到该分块绘制的屏幕位置
			rct -= CPOINT(vp.left, vp.top);
			
			for(int row=0; row<nRows; row++)
			{
				for(int col=0; col<nCols; col++)
				{
					fcoefy = (float)(row +nRowIndex)/(float)(nTotalRows);
					fcoefx = (float)(col +nColIndex)/(float)(nTotalCols);
					fcoefx = fcoefx>1.0f ? 1.0f: fcoefx;
					fcoefy = fcoefy>1.0f ? 1.0f: fcoefy;

					while(fAmplitudes[nRowOffset].x < fcoefy)
						nRowOffset++;

					while(fAmplitudes[nColOffset].x < fcoefx)
						nColOffset++;

					nRowOffset = nRowOffset>(nTemp-1) ? (nTemp-1): nRowOffset;
					nColOffset = nColOffset>(nTemp-1) ? (nTemp-1): nColOffset;

					int nPreRowOffset = nRowOffset-1;
					int nPreColOffset = nColOffset-1;
					nPreRowOffset = nPreRowOffset<0 ? 0: nPreRowOffset;
					nPreColOffset = nPreColOffset<0 ? 0: nPreColOffset;

					float fSwingx = 0.0f;
					if(nColOffset != nPreColOffset)
						fSwingx = (fcoefx - fAmplitudes[nPreColOffset].x)/
									(fAmplitudes[nColOffset].x - fAmplitudes[nPreColOffset].x);

					fSwingx *= (fAmplitudes[nColOffset].y - fAmplitudes[nPreColOffset].y);
					fSwingx += fAmplitudes[nPreColOffset].y;

					float fSwingy = 0.0f;
					if(nRowOffset != nPreRowOffset)
						fSwingy = (fcoefy - fAmplitudes[nPreRowOffset].x)/
									(fAmplitudes[nRowOffset].x - fAmplitudes[nPreRowOffset].x);

					fSwingy *= (fAmplitudes[nRowOffset].y - fAmplitudes[nPreRowOffset].y);
					fSwingy += fAmplitudes[nPreRowOffset].y;

					float dx = fSwingx*XPGRIDSIZE*sin( (col +nColIndex) *m_effectParam.fCycNum +m_effectParam.fPos);
					float dy = fSwingy*XPGRIDSIZE*sin( (row +nRowIndex) *m_effectParam.fCycNum +m_effectParam.fPos);

					switch(m_effectParam.nType)
					{ // 0--1水平方向  2---3垂直方向
					case XPDISTORTIONTYPE_HORIZONTAL_1:
						dy = 0.0f;
						break;
					case XPDISTORTIONTYPE_HORIZONTAL_2:
						dy = dx;
						break;
					case XPDISTORTIONTYPE_VERTICAL_1:
						dx = 0.0f;
						break;
					case XPDISTORTIONTYPE_VERTICAL_2:
						dx = dy;
						break;
					case XPDISTORTIONTYPE_DIAGONAL: //固定左上角
						break;
					default:
						break;
					}

					TCT1VERTEX vertex;
					vertex.x = (rct.left + col*deltax +dy)*m_fZoom -0.5f;
					vertex.y = (rct.top + row*deltay +dx)*m_fZoom -0.5f;
					vertex.z = 0.0f;

					vertex.c = m_color;
					
					vertex.u = col*deltau;
					vertex.v = row*deltav;
					s_vertexs[row*nCols +col] = vertex;
				}
			}

			int nIndex = 0; // 6*(nRows-1)*(nCols-1)
			for(int j=0; j<nRows-1; j++)
			{
				for(int i=0; i<nCols-1; i++)
				{
					s_index[nIndex++] = j*nCols +i;
					s_index[nIndex++] = (j+1)*nCols +i;
					s_index[nIndex++] = (j+1)*nCols +i+1;

					s_index[nIndex++] = (j+1)*nCols +i+1;
					s_index[nIndex++] = j*nCols +i+1;
					s_index[nIndex++] = j*nCols +i;
				}
			}

			RendererStateAutoRecover stateAutoRecover(m_pEngine->GetRenderer());

 			m_pEngine->GetRenderer()->SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
 			m_pEngine->GetRenderer()->SetAlphaBlendDstType(XPBLEND_INVSRCALPHA);
 			m_pEngine->GetRenderer()->SetTextureColorBlendType(XPTOP_MODULATE);

			m_pEngine->GetRenderer()->DrawIndexVerticeBuffer(handle, s_vertexs, s_index, nRows * nCols, nIndex);

			if(bLightFlag)
			{
				PictureHandle lightHandle = m_vectorLightImgInfo[i].pPicResrc->handle;
				if(lightHandle == INVALID_PICTURE_HANDLE)
				{
					result = false;
					continue;
				}
				m_pEngine->GetRenderer()->SetRenderEffect(XPRE_NULL);
				m_pEngine->GetRenderer()->DrawIndexVerticeBuffer(lightHandle, s_vertexs, s_index, nRows * nCols, nIndex);
			}
		}

		return result;
	}

	bool DistortBase::Update(float fTime)  //秒
	{
		m_effectParam.fPos += fTime*2*Nuclear::XP_PI/m_effectParam.fSpeed;
		int count = (int)(m_effectParam.fPos/(2.0f*Nuclear::XP_PI));
		m_effectParam.fPos -= count*2.0f*Nuclear::XP_PI;//------控制函数移动

		return true;
	}
}