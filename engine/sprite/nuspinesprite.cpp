#include "nuspinesprite.h"
#include "StringUtil.h"
#include "engine/nuenginebase.h"
#include "engine/nuengine.h"
#include "../common/nufileiomanager.h"
#include "../sprite/nuspritemanager.h"
#include "engine/nuconfigmanager.h"
#include "../renderer/nucocos2d_render.h"

namespace Nuclear
{
	SpineSprite::SpineSprite(EngineBase *pEB, const std::wstring &modelname)
		: Sprite(pEB, modelname)
		, m_pSkelAnim(0)
		, m_iAniStartTime(0)
		, m_iAniDurationTime(0)
		, m_fScaleForTotalTime(1.f)
		, m_bActive(true)
		, mpUserData(NULL)
		, m_isUISprite(false)
	{
	}

	SpineSprite::~SpineSprite()
	{
		release();
	}

	void SpineSprite::release()
	{
		if (m_pSkelAnim)
		{
			m_pSkelAnim->release();
			m_pSkelAnim = NULL;
		}
		SpineManager* pSpineMan = m_pEB->GetSpineManager();
		pSpineMan->FreeSpineRes(m_modelName, this);
	}

	bool SpineSprite::SetModel(const std::wstring &modelname, bool async)
	{
		if (m_modelName == modelname)
			return true;

		release();

		m_modelName = modelname;
		std::wstring dir = L"/model/" + modelname + L"/";
		SpineManager* pSpineMan = m_pEB->GetSpineManager();

		if (!async)
		{
			m_strDefaultActionName = L"";
			m_strTmpActionName = L"";
			SetPropFlag(XPSPPF_HAS_EFFECT_LAYER, false);
			SetPropFlag(XPSPPF_HAS_DEF_ASYNC_LOADING, false);
			SetPropFlag(XPSPPF_HAS_CUR_ASYNC_LOADING, false);
			pSpineMan->SyncLoadSpineRes(modelname, dir, this);
		}
		else
		{
			pSpineMan->ASyncLoadSpineRes(modelname, dir, this);
		}

		return false;
	}

	bool SpineSprite::SetDefaultAction(const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float fScaleForTotalTime, bool bHoldLastFrame)
	{
		if (m_strDefaultActionName != action_name)
		{
			m_strDefaultActionName = action_name;
			m_fScaleForTotalTime = fScaleForTotalTime;
			return true;
		}
		return false;
	}

	bool SpineSprite::PlayAction(const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float fScaleForTotalTime)
	{
		std::string actName = ws2s(action_name);

		// set mix between two different actions
		float mixDurationTime = 0.f;
		if (action_name != m_strTmpActionName) {
			m_strTmpActionName = action_name;
			mixDurationTime = 0.1f;
			std::string tmpActName = ws2s(m_strTmpActionName);
			if (m_pSkelAnim)
				m_pSkelAnim->setMix(tmpActName.c_str(), actName.c_str(), mixDurationTime);
		}

		// default action is loop action
		bool loop = (action_name == m_strDefaultActionName) ? true : false;

		if (m_pSkelAnim && m_pSkelAnim->setAnimation(0, actName.c_str(), loop))
		{
			m_pSkelAnim->setTimeScale(fScaleForTotalTime);
			m_actionTypeFlag = m_pEB->GetSpriteManager()->actionType.GetActionType(action_name);
			m_iAniStartTime = m_pEB->GetTick();
			float animationDuration = m_pSkelAnim->getAnimationDuration(0, actName.c_str());
			m_iAniDurationTime = (int)((animationDuration * fScaleForTotalTime + mixDurationTime) * 1000);
			return true;
		}
		return false;
	}

	void SpineSprite::SetScale(float scale)
	{
		float modelScale = GetModelScale();
		m_fSetScale = scale;
		m_fScale = modelScale * m_fSetScale;

		//if (m_pSkelAnim)
		//{
		//	float fWorldScale = m_pEB->GetWorldScale();
		//	m_pSkelAnim->setScaleX(m_fScale * fWorldScale);
		//	m_pSkelAnim->setScaleY(-m_fScale * fWorldScale);	// 因为坐标系不同，因此要做一次上下翻转
		//}
	}
	void SpineSprite::SetIsUISprite(bool b)
	{
		m_isUISprite = b;
	}

	bool SpineSprite::GetActionTimeByName(const std::wstring& action_name, float &time)
	{
		if (m_pSkelAnim)
		{
			std::string actName = ws2s(action_name);
			time = m_pSkelAnim->getAnimationDuration(0, actName.c_str());
			return true;
		}
		return false;
	}

	void SpineSprite::Update(DWORD ticktime)
	{
		Sprite::Update(ticktime);

		if (m_pSkelAnim)
		{
			if (m_isUISprite)
			{
				NuclearLocation curPos = m_pMovableImp->getLocation();
				NuclearLocation NewPos = curPos;
				m_pSkelAnim->setPosition((float)NewPos.x, (float)NewPos.y);
				m_pSkelAnim->setScaleX(m_fScale);
				m_pSkelAnim->setScaleY(-m_fScale);	// 因为坐标系不同，因此要做一次上下翻转
			}
			else
			{
				float fWorldScale = m_pEB->GetWorldScale();
				NuclearLocation curPos = m_pMovableImp->getLocation();
				NuclearLocation NewPos = curPos;
				NewPos *= fWorldScale;
				m_pSkelAnim->setPosition((float)NewPos.x, (float)NewPos.y);
				m_pSkelAnim->setScaleX(m_fScale * fWorldScale);
				m_pSkelAnim->setScaleY(-m_fScale * fWorldScale);	// 因为坐标系不同，因此要做一次上下翻转
			}
			m_pSkelAnim->update(ticktime*0.001f);
		}
	}

	void SpineSprite::UpdateAction(DWORD ticktime)
	{
		// if current action is not default action, then play default action when current action is done!
		if (m_strTmpActionName != m_strDefaultActionName) {
			unsigned int d = m_pEB->GetTick() - m_iAniStartTime;
			if (d > m_iAniDurationTime)
			{
				PlayAction(m_strDefaultActionName, XPSALT_SYNC, m_fScaleForTotalTime);
			}
		}
	}

	void SpineSprite::renderEntity(Canvas* canvas, bool isAlpha, bool realRender)
	{
		if (realRender) m_pMovableImp->RenderPath();
		RenderEffectBack(canvas, realRender);

		if (m_pSkelAnim && realRender && (m_nVisible & XP_VIS_VIS) && (m_nVisible & XP_VIS_ENTITY))
		{
			unsigned int waterDepth = 0;
			if (!TestPropFlag(XPSPPF_SCREEN_COORD))
			{
				if (m_pEB->GetWaterDepth(m_pMovableImp->getLocation(), waterDepth))
				{
					if (TestPropFlag(XPSPPF_IS_WATER_ERROR))
					{
						SetPropFlag(XPSPPF_IS_WATER_ERROR, false);//好了
					}
				}
				else {
					if (!TestPropFlag(XPSPPF_IS_WATER_ERROR))
					{
						SetPropFlag(XPSPPF_IS_WATER_ERROR, true);
						if (m_pEB->GetApp())
						{
							m_pEB->GetApp()->OnSpriteLocationError(this);
						}
					}
				}
			}

			if (m_isUISprite)
			{
				ConfigManager* pCfgMan = m_pEB->GetConfigManager();
				float alpha = isAlpha ? pCfgMan->GetMaskAlpha() : 1.0f;
				m_pSkelAnim->draw(0.0f, 0.0f, alpha * m_nAlpha / 255.0f);
			}
			else
			{
				float fWorldScale = m_pEB->GetWorldScale();
				NuclearLocation curPos = m_pMovableImp->getLocation();
				NuclearLocation NewPos = curPos;
				NewPos *= fWorldScale;
				//m_pSkelAnim->setPosition((float)NewPos.x, (float)NewPos.y);
				//m_pSkelAnim->setScaleX(m_fScale * fWorldScale);
				//m_pSkelAnim->setScaleY(-m_fScale * fWorldScale);	// 因为坐标系不同，因此要做一次上下翻转

				NuclearRect vp = m_pEB->GetViewport();
				cocos2d::CCRect box = m_pSkelAnim->boundingBox();
				float left   = box.origin.x;
				float right  = box.origin.x + box.size.width;
				float top    = box.origin.y;
				float bottom = box.origin.y + box.size.height;

				NuclearRect boundBox((int)(left / fWorldScale), (int)(top / fWorldScale), (int)(right / fWorldScale), (int)(bottom / fWorldScale));
				//NuclearRect boundBox(left, top, right, bottom);

				NuclearRect resRect;
				if (vp.IsCross(boundBox, resRect))
				{
					ConfigManager* pCfgMan = m_pEB->GetConfigManager();
					float alpha = isAlpha ? pCfgMan->GetMaskAlpha() : 1.0f;
					m_pSkelAnim->draw(vp.left * fWorldScale, vp.top * fWorldScale, alpha * m_nAlpha / 255.0f);
					if(canvas)
					{
						const NuclearBase& modelbase = m_pEB->GetSpriteManager()->GetBase(m_modelName);
						NuclearLocation curPos = m_pMovableImp->getLocation();
						NuclearRect clickBox(curPos.x + modelbase.left.x * fWorldScale, curPos.y + modelbase.left.y * fWorldScale, curPos.x + modelbase.right.x * fWorldScale, curPos.y + modelbase.right.y * fWorldScale);
						canvas->Draw(this,clickBox);
					}
				}
			}
		}

		RenderEffectFront(canvas, realRender);

		//回调渲染名字版
		if (realRender && mDrawNameCB)
		{
			(mDrawNameCB)(this);
		}
	}

	bool SpineSprite::TestViewport(const NuclearRect &viewport) const
	{
		if (viewport.PtInRect(GetLocation()))
			return true;

		return false;
	}

	/*void SpineSprite::GetBase(NuclearSortBaseRectType &base)
	{
		//可以考虑把NuclearBase保存在ComponentSprite里面，这样不用每帧都做一次查表
		NuclearPoint loc = m_pMovableImp->getLocation();

		base.y = loc.y;
	}*/
	void SpineSprite::Reset()
	{
		SetScale(m_fScale);
		SetDefaultAction(m_strDefaultActionName, XPSALT_SYNC, m_fScaleForTotalTime, false);
		PlayAction(m_strTmpActionName, XPSALT_SYNC, m_fScaleForTotalTime);
	}

	void SpineSprite::OnLoaded(const std::wstring& wdir, SpineRes* spineRes)
	{
		if (spineRes->mPicHandles.empty())
		{
			return;
		}

		Renderer* pRenderer = m_pEB->GetRenderer();

		spine::PathToTextureMap textureMap;
		for (SpineRes::PictureHandleArray::iterator it = spineRes->mPicHandles.begin(); it != spineRes->mPicHandles.end(); ++it)
		{
			PictureHandle picHandle = *it;
			const Cocos2dRenderer::CTextureInfo* pTextureInfo = (const Cocos2dRenderer::CTextureInfo*)pRenderer->GetTexInfo(picHandle);
			if (pTextureInfo)
			{
				textureMap.insert(std::make_pair(ws2s(pTextureInfo->fileuri), pTextureInfo->m_pTexture));
			}
		}

		std::string dir = ws2s(wdir);
		m_pSkelAnim = spine::SkeletonAnimation::createWithTextureMap((const char*)spineRes->jsonBuffer.constbegin(), spineRes->jsonBuffer.size(), (const char*)spineRes->atlasBuffer.constbegin(), spineRes->atlasBuffer.size(), dir.c_str(), textureMap, 1.0f);
		if (m_pSkelAnim)
		{
			m_pSkelAnim->retain();
			Reset();
		}
	}

};
