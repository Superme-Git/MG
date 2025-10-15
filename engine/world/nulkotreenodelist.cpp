#include "nulkotreenodelist.h"

namespace Nuclear
{
	LkoTreeNodeList::LkoTreeNodeList(void)
	{
		SetZoomParamFlag(false); //为了让以前的绘制植物方式依然正确加的。

		m_bTouchFlag = false;
		m_fLkoLife = -1.0f;

		m_bLKOEnableZoom = false;
		m_fLKOzoom = 2.0f;
		m_fLKOTime = 3.0f;
		m_Static = false;
	}

	LkoTreeNodeList::~LkoTreeNodeList(void)
	{
		DTList::iterator iter = m_Lists.begin();
		for (; iter!=m_Lists.end(); ++iter)
		{
			delete *iter;
		}
	}

	void LkoTreeNodeList::AddANode(LkoTreeNode *node)
	{
		m_Lists.push_back(node);
	}

	void LkoTreeNodeList::RandomBrightTime()
	{
		DTList::iterator iter = m_Lists.begin();
		DTList::iterator iterEnd = m_Lists.end();
		for (;iter!=iterEnd;++iter)
		{
			if ((*iter)->LinkedObjectMoveParam.m_dwFlag & FIRE_LINKEDOBJ_LIGHT)
			{
				(*iter)->nBeginBrightTime = FIRE_NIGHT_START_TIME + (rand() % (FIRE_LIGNT_RANGE * 2)) - FIRE_LIGNT_RANGE;
				(*iter)->nEndBrightTime = FIRE_DAY_START_TIME + (rand() % (FIRE_LIGNT_RANGE * 2)) - FIRE_LIGNT_RANGE;
				if ((*iter)->nBeginBrightTime >= FIRE_A_DAY_TIME)
				{
					(*iter)->nBeginBrightTime -= FIRE_A_DAY_TIME;
				}
				if ((*iter)->nEndBrightTime >= FIRE_A_DAY_TIME)
				{
					(*iter)->nEndBrightTime -= FIRE_A_DAY_TIME;
				}
				if ((*iter)->nBeginBrightTime < 0)
				{
					(*iter)->nBeginBrightTime += FIRE_A_DAY_TIME;
				}
				if ((*iter)->nEndBrightTime < 0)
				{
					(*iter)->nEndBrightTime += FIRE_A_DAY_TIME;
				}
			}
		}
	}

	void LkoTreeNodeList::SimpleUpdate(int timeDelta, int viewPortLeft, int viewPortTop, float fRatio)
	{
		DTList::iterator iter = m_Lists.begin();
		DTList::iterator iterEnd = m_Lists.end();
		NuclearMatrix3 m;
		NuclearVector2 v;

		for(; iter!=iterEnd; ++iter)
		{	
			//生成平移矩阵
			if ((*iter)->pParent)//有父节点，自己不是根节点
			{
				(*iter)->mTransAll = (*iter)->pParent->mTransAll;
				(*iter)->mTransAll._31 += (*iter)->fromMainRotateCent.x;
				(*iter)->mTransAll._32 += (*iter)->fromMainRotateCent.y;
			}
			else
			{	//如果自己就是根节点，那么就平移后缩放
				v.x = ((*iter)->fromMainRotateCent.x - viewPortLeft) * fRatio;
				v.y = ((*iter)->fromMainRotateCent.y - viewPortTop) * fRatio;
				XPMatrixTranslation(&(*iter)->mTransAll, &v);
				(*iter)->mTransAll._11 = fRatio;
				(*iter)->mTransAll._22 = fRatio;
			}

			//生成最终画图的矩阵
			(*iter)->mTransAll.Translation((*iter)->mTransResult, -static_cast<float>((*iter)->rotateCent.x), -static_cast<float>((*iter)->rotateCent.y));
		}
	}

	void LkoTreeNodeList::Update(int timeDelta, int viewPortLeft, int viewPortTop, float fRatio)
	{
		DTList::iterator iter = m_Lists.begin();
		DTList::iterator iterEnd = m_Lists.end();
		NuclearMatrix3 m;
		NuclearVector2 v;

		float fZoom = 1.0f;
		if(m_bLKOEnableZoom)
		{
			if(!m_bTouchFlag)
			{
				if(m_fLkoLife > 0.0f)
				{
					fZoom = (m_fLKOzoom - 1.0f)*m_fLkoLife/m_fLKOTime  +1.0f;
					m_fLkoLife -= timeDelta *0.001f;
				}
				else
					fZoom = 1.0f;
			}
			else
				fZoom = m_fLKOzoom;
		}

		for(; iter!=iterEnd; ++iter)
		{
			//自己的变换矩阵（不包括平移）
			if ((*iter)->LinkedObjectMoveParam.m_nDistortionType == XPPLANTROTATE)	//旋转
			{
				if (m_Static)
				{
					(*iter)->fAngle = 0.0f;
					(*iter)->LinkedObjectMoveParam.m_fSpeed = 0.0f;
				}
				else
				{
					(*iter)->fAngle += timeDelta * (*iter)->LinkedObjectMoveParam.m_fSpeed *fZoom *0.001f;
					if ((*iter)->fAngle > (*iter)->LinkedObjectMoveParam.m_fAngle2 *fZoom)
					{
						(*iter)->fAngle = (*iter)->LinkedObjectMoveParam.m_fAngle2 *fZoom; //为了做45度左右的晃动素材快速归位加的！
						(*iter)->LinkedObjectMoveParam.m_fSpeed = -fabs((*iter)->LinkedObjectMoveParam.m_fSpeed);
					}
					if ((*iter)->fAngle < (*iter)->LinkedObjectMoveParam.m_fAngle1 *fZoom)
					{
						(*iter)->fAngle = (*iter)->LinkedObjectMoveParam.m_fAngle1 *fZoom; //为了做45度左右的晃动素材快速归位加的！
						(*iter)->LinkedObjectMoveParam.m_fSpeed = fabs((*iter)->LinkedObjectMoveParam.m_fSpeed);
					}
				}
				XPMatrixRotationOrigin(&(*iter)->mTransAll, (*iter)->fAngle *0.01745f);
			} 
			else if ((*iter)->LinkedObjectMoveParam.m_nDistortionType == XPPLANTSKEW)	//错切
			{
				(*iter)->fSkewx += static_cast<float>(timeDelta) * (*iter)->LinkedObjectMoveParam.m_nSkewSpeedX *fZoom *0.001f;
				if ((*iter)->fSkewx > (*iter)->LinkedObjectMoveParam.m_nSkewPixelX *fZoom)
				{
					(*iter)->fSkewx = static_cast<float>( (*iter)->LinkedObjectMoveParam.m_nSkewPixelX *fZoom );
					(*iter)->LinkedObjectMoveParam.m_nSkewSpeedX = -abs((*iter)->LinkedObjectMoveParam.m_nSkewSpeedX);
				}
				if ((*iter)->fSkewx < -(*iter)->LinkedObjectMoveParam.m_nSkewPixelX *fZoom)
				{
					(*iter)->fSkewx = static_cast<float>( -(*iter)->LinkedObjectMoveParam.m_nSkewPixelX *fZoom );
					(*iter)->LinkedObjectMoveParam.m_nSkewSpeedX = abs((*iter)->LinkedObjectMoveParam.m_nSkewSpeedX);
				}
				if( (*iter)->LinkedObjectMoveParam.m_nSkewType == XPSKEWHORIZONTAL )
				{//水平切
					XPMatrixShearX(&(*iter)->mTransAll, static_cast<float>((*iter)->fSkewx *fZoom) / (*iter)->height);
				}
				else
				{//垂直切
					XPMatrixShearY(&(*iter)->mTransAll, static_cast<float>((*iter)->fSkewx *fZoom) / (*iter)->width);
				}
			}
			else //XPPLANTTRANSLATION
			{
				(*iter)->fTranslationx += (float)( timeDelta * (*iter)->LinkedObjectMoveParam.m_fTranslationSpeedx *fZoom *0.001f ); // / 1000
				if((*iter)->fTranslationx > (*iter)->LinkedObjectMoveParam.m_fSizeRight *fZoom)
				{
					(*iter)->fTranslationx = (*iter)->LinkedObjectMoveParam.m_fSizeRight *fZoom;
					(*iter)->LinkedObjectMoveParam.m_fTranslationSpeedx = -fabs((*iter)->LinkedObjectMoveParam.m_fTranslationSpeedx);
				}
				if((*iter)->fTranslationx < (*iter)->LinkedObjectMoveParam.m_fSizeLeft *fZoom)
				{
					(*iter)->fTranslationx = (*iter)->LinkedObjectMoveParam.m_fSizeLeft *fZoom;
					(*iter)->LinkedObjectMoveParam.m_fTranslationSpeedx = fabs((*iter)->LinkedObjectMoveParam.m_fTranslationSpeedx);
				}

				(*iter)->fTranslationy += (float)( timeDelta * (*iter)->LinkedObjectMoveParam.m_fTranslationSpeedy *fZoom *0.001f ); // / 1000
				if((*iter)->fTranslationy > (*iter)->LinkedObjectMoveParam.m_fSizeDown *fZoom)
				{
					(*iter)->fTranslationy = (*iter)->LinkedObjectMoveParam.m_fSizeDown *fZoom;
					(*iter)->LinkedObjectMoveParam.m_fTranslationSpeedy = -fabs((*iter)->LinkedObjectMoveParam.m_fTranslationSpeedy);
				}
				if((*iter)->fTranslationy < (*iter)->LinkedObjectMoveParam.m_fSizeTop *fZoom)
				{
					(*iter)->fTranslationy = (*iter)->LinkedObjectMoveParam.m_fSizeTop *fZoom;
					(*iter)->LinkedObjectMoveParam.m_fTranslationSpeedy = fabs((*iter)->LinkedObjectMoveParam.m_fTranslationSpeedy);
				}
				XPMatrixTranslation(&(*iter)->mTransAll, (*iter)->fTranslationx, (*iter)->fTranslationy);
			}

			//生成平移矩阵
			if ((*iter)->pParent)//有父节点，自己不是根节点
			{
				switch ((*iter)->LinkedObjectMoveParam.m_nMoveFollowType)	//链接类型
				{
				case XPMOVEFOLLOWFLEXIBLE:	//软连接
					XPVec3TransformCoord(&v, &(*iter)->fromMainRotateCent, &(*iter)->pParent->mTransAll);
					XPMatrixTranslation(&(*iter)->mTransfer, &v);
					(*iter)->mTransfer._11 = fRatio;
					(*iter)->mTransfer._22 = fRatio;
					break;
				case XPMOVEFOLLOWRIGID:	//硬链接
					(*iter)->pParent->mTransAll.Translation((*iter)->mTransfer, (*iter)->fromMainRotateCent);
					break;
				case XPMOVEFOLLOWNO:	//不链接在父节点上
					XPVec3TransformCoord(&v, &(*iter)->fromMainRotateCent, &(*iter)->pParent->mTransfer);
					XPMatrixTranslation(&(*iter)->mTransfer, &v);
					(*iter)->mTransfer._11 = fRatio;
					(*iter)->mTransfer._22 = fRatio;
					break;
				default:	//出错了？
					break;
				}
			} else {	//如果自己就是根节点，那么就平移后缩放
				v.x = ((*iter)->fromMainRotateCent.x - viewPortLeft) * fRatio;
				v.y = ((*iter)->fromMainRotateCent.y - viewPortTop) * fRatio;
				XPMatrixTranslation(&(*iter)->mTransfer, &v);
				(*iter)->mTransfer._11 = fRatio;
				(*iter)->mTransfer._22 = fRatio;
			}
			//生成变换矩阵
			(*iter)->mTransAll *= (*iter)->mTransfer;

			(*iter)->mTransAll.Translation((*iter)->mTransResult, -static_cast<float>((*iter)->rotateCent.x), -static_cast<float>((*iter)->rotateCent.y));
		}
		
	}

}
