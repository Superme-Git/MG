#include "../common/nufileiomanager.h"
#include "../engine/nuconfigmanager.h"
#include "../sprite/nuspritemanager.h"
#include "../effect/nusebind.h"
#include "../effect/nueffectmanager.h"
#include "../effect/nuxapeffect.h"
#include "../world/nuworld.h"
#include "../nuiapp.h"
#include "../astar/nuastar.hpp"
#include "nucomponentsprite.h"
#include "nusprite.h"
#include "../renderer/nurenderer.h"
#include "nuspritemanager.h"

namespace Nuclear
{
	enum XPSPREFF_FLAG_EX
	{
		XPSPREFFFLAG_SET_EFFECT = 1 << 30,  //外面有没有effect的指针
		XPSPREFFFLAG_DELETED = 1 << 31,		//已经删除了
	};

	class SpriteEffectNotify : public IEffectNotify, public EffectGC
	{
	public:
		Sprite* pSprite;
		int dx;
		int dy;
		unsigned flag;

		virtual void OnDelete(IEffect* pEffect) //override
		{
			//pEffect->RemoveNotify(this);都要删除了，就不用了
			if (pSprite)
			{
				pSprite->m_sprEffNotifys.erase(this);
				if (flag & XPSPREFFFLAG_SET_EFFECT)
					pSprite->removeEffect(pEffect);
			}
			delete this;
		}
		virtual void OnAsyncLoaded(IEffect* pEffect, bool succeeded)// override
		{
			pEffect->RemoveNotify(this);
			if (pSprite)
			{
				Effect *tpEffect = (Effect*)pEffect;
				pSprite->m_sprEffNotifys.erase(this);
				if (succeeded)
				{
					if (flag & XPSPREFFFLAG_BIND_AUTO)
					{
						flag &= ~XPSPREFFFLAG_BIND_AUTO;
						if (pEffect->GetEffectBindType() == XPEBT_EFFECT_POS)
							flag |= XPSPREFFFLAG_BIND_EFF_POS;
					}
					Sprite::EffectInfo info(flag, dx, dy, tpEffect);
					if (flag & XPSPREFFFLAG_SET_EFFECT)
					{
						pSprite->m_mapDuraEffects[tpEffect] = info;
						pSprite->m_sprEffNotMap.erase(pEffect);
					}
					else
					{
						pSprite->m_lstTmpEffects.push_back(info);
					}
					if ((flag & XPSPREFFFLAG_NO_FOLLOW_SCALE) == 0)
					{
						tpEffect->SetScale(pSprite->m_fScale);
					}
					tpEffect->SetScaleType(XPEST_ALLSCALE);//XPEST_ONLY_SCALE_POS
					if (pSprite->GetScreenCoord())
					{
						tpEffect->SetScreenCrood(true);
					}

					if (flag & XPSPREFFFLAG_BIND_EFF_POS_WITH_DIR)
					{
						pSprite->UpdateEffectPosWithDir(info);
					}
				}
				else
				{
					if (flag & XPSPREFFFLAG_SET_EFFECT)
					{
						//不会有内存泄漏么？
						pSprite->removeEffect(pEffect);
					}
					else
					{
						RemoveEffect(pEffect);
					}
				}
			}
			else
			{
				RemoveEffect(pEffect);
			}
			delete this;
		}
	};

	Sprite::Sprite(EngineBase *pEB, const std::wstring &modelname) : EntitativeObj(pEB), m_nPropFlag(0), m_iCurFrame(0), m_iAniDurationTime(1000), 
		m_surfaceType(WST_ALWAYS_PLANE), m_iDefaultAniDurationTime(0), m_iAniStartTime(0), m_nTitlePos(0), 
		m_actionTypeFlag(0), m_defaultActionTypeFlag(0), m_nHorseHeight(0), m_fHorseScale(1.0), m_nAlpha(255), m_nTeleportStartTime(0), 
		m_soundActorName(L""), m_pRenderableSprite(NULL),
		mDrawNameCB(NULL), m_bShaking(false), m_bRideAniIsReady(false),
		m_iFlyScaleCur(10000),//因飞行而引起的缩放当前值
		m_iFlyScaleTgt(10000),//因飞行而引起的缩放目标值
		m_iFlyScaleStep(10000),//因为飞行而引起的缩放的步伐值
		m_iFlyOffsetYCur(0),//因飞行而引起的Y轴偏移(实际上是模拟Z轴)当前值
		m_iFlyOffsetYTgt(0),//因飞行而引起的Y轴偏移(实际上是模拟Z轴)目标值
		m_iFlyOffsetYStep(10000),//因飞行而引起的Y轴偏移(实际上是模拟Z轴)步伐值
		m_iUseWorldScale(0)//1使用世界缩放,0不使用
		, mpUserData(NULL)
	{
		m_fSetScale = 1.f;
		m_fScale = 1.0f;
		m_pMovableImp = new MovableSpriteImp(this, pEB); // 只有一个实现
		//SetModel(modelname, true);
	}

	Sprite::~Sprite()
	{
		SpriteEffectNotifySet::iterator it = m_sprEffNotifys.begin(), ie = m_sprEffNotifys.end();
		for (;it!=ie;++it)
		{
			(*it)->pSprite = NULL;
		}
		m_sprEffNotifys.clear();
		for (EffectInfoMap::iterator itDura = m_mapDuraEffects.begin(); itDura != m_mapDuraEffects.end(); ++itDura)
		{
			itDura->second.effect->Release();
		}
		m_mapDuraEffects.clear();
		for (std::list<EffectInfo>::iterator itTmp = m_lstTmpEffects.begin(); itTmp != m_lstTmpEffects.end(); ++itTmp)
		{
			(*itTmp).effect->Release();
		}
		m_lstTmpEffects.clear();

// 		std::map<IEffect*, SpriteEffectNotify*>::iterator itSpr = m_sprEffNotMap.begin();
// 		for (; itSpr != m_sprEffNotMap.end(); ++itSpr)
// 		{
// 			IEffect* pEffect = itSpr->first;
// 			if (!pEffect->m_isSpine)
// 			{
// 				m_pEB->GetEffectManager()->RemoveEffect(pEffect);
// 			}
// 		}
		m_sprEffNotMap.clear();

		XPSAFE_DELETE(m_pMovableImp);
		XPSAFE_RELEASE(m_pRenderableSprite);
	}

	// 设置精灵的装备组件
	bool Sprite::SetComponent(int scid, const std::wstring& resource, NuclearColor color, std::wstring wsLayerName)
	{
		if (!m_pRenderableSprite)
		{
			return false;
		}

		bool bResult = m_pRenderableSprite->SetComponent(scid, resource, color);

		if (!bResult)
		{
			return bResult;
		}

		//if (iShaderType == 1)
		//{
		//	std::wstring wsLayerNameNew;
		//	std::wstring wsResource;
		//	int iLayerIndex = 0;
		//	wsLayerNameNew = L"part" + wsLayerName;
		//	wsResource = resource;
		//	iLayerIndex = GetLayerIndexByName(wsLayerNameNew);
		//	bResult = bResult & m_pRenderableSprite->SetComponent(iLayerIndex, wsResource, 0xffffffff, (scid + 100) * -1);
		//}

		return bResult;
	}

	bool Sprite::SetModel(const std::wstring &modelname, bool async)
	{
		if (m_modelName == modelname)
			return true;
		
		m_strDefaultActionName = L"";
		m_strTmpActionName = L"";
		SetPropFlag(XPSPPF_HAS_EFFECT_LAYER, false);
		SetPropFlag(XPSPPF_HAS_DEF_ASYNC_LOADING, false);
		SetPropFlag(XPSPPF_HAS_CUR_ASYNC_LOADING, false);
		
		if (modelname == L"")
		{
			m_modelName = L"";
			XPSAFE_RELEASE(m_pRenderableSprite);
			return true;
		}
		else
		{
			XPSAFE_RELEASE(m_pRenderableSprite);
		}

		std::wstring model(modelname);
		NuclearFileIOManager *pFileIOMan = m_pEB->GetFileIOManager();
		XPASSERT(pFileIOMan);
		bool bLookupSegmpak = false;

		if (m_pRenderableSprite)
		{
			if (m_pRenderableSprite->GetType() == XPSPRITETYPE_3D)
			{
				m_pRenderableSprite->Release();
				m_pRenderableSprite = new ComponentSprite(this);
			}
		}
		else {
			m_pRenderableSprite = new ComponentSprite(this);
		}

		if (m_pRenderableSprite->SetModel(model, async))
		{
			m_modelName = model;
			m_pRenderableSprite->SetLookupSegmpak(bLookupSegmpak);
			return true;
		}
		return false;
	}

	void Sprite::SetHoldLastFrame(bool bHoldLastFrame)
	{
		if (m_pRenderableSprite)
		{
			m_pRenderableSprite->SetHoldLastFrame(bHoldLastFrame);
		}
	}

	bool Sprite::SetDefaultAction( const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float fScaleForTotalTime, bool bHoldLastFrame)
	{
		if (m_pRenderableSprite && m_pRenderableSprite->SetDefaultAction(action_name, type, fScaleForTotalTime, bHoldLastFrame))
		{
			m_strDefaultActionName = action_name;
			return true;
		}
		return false;
	}

	void Sprite::PlayDefaultAction()
	{
		m_actionTypeFlag = m_pEB->GetSpriteManager()->actionType.GetActionType(m_strDefaultActionName);
	}

	bool Sprite::PlayAction(const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float fScaleForTotalTime)
	{
		if (m_pRenderableSprite && m_pRenderableSprite->PlayAction(action_name, type, fScaleForTotalTime))
		{
			m_actionTypeFlag = m_pEB->GetSpriteManager()->actionType.GetActionType(action_name);
			m_strTmpActionName = action_name;
			return true;
		}
		return false;
	}
	void Sprite::RenderEntityBottom(Canvas* canvas)
	{
		World* pWorld = m_pEB->GetXPWorld();
		if (pWorld == NULL)
		{
			return;
		}
		for (EffectInfoMap::iterator it = m_mapDuraEffects.begin(); it != m_mapDuraEffects.end(); ++it)
		{
			if ((it->second.flag & XPSPREFFFLAG_DRAW_ON_BOTTOM) != 0)
			{
				pWorld->PushSpriteBottomEffect(&(*it->second.effect));
			}
		}
		for (std::list<EffectInfo>::iterator it = m_lstTmpEffects.begin(); it != m_lstTmpEffects.end(); ++it)
		{
			if ((it->flag & XPSPREFFFLAG_DRAW_ON_BOTTOM) != 0)
			{
				pWorld->PushSpriteBottomEffect(&(*it->effect));
			}
		}
	}
	void Sprite::renderEntity(Canvas* canvas, bool isAlpha, bool realRender)
	{
		if (realRender) m_pMovableImp->RenderPath();
		RenderEffectBack(canvas, realRender);
		if (m_pRenderableSprite) 
		{
			PictureHandle spriteHandle = INVALID_PICTURE_HANDLE;
			unsigned int waterDepth = 0;
			if (!TestPropFlag(XPSPPF_SCREEN_COORD))
			{
				 if (m_pEB->GetWaterDepth(m_pMovableImp->getLocation(), waterDepth))
				 {
					 if (TestPropFlag(XPSPPF_IS_WATER_ERROR))
					 {
						 SetPropFlag(XPSPPF_IS_WATER_ERROR, false);//好了
					 }
				 } else {
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
			Sprite::TeleportBlurFrame tbf;
			ConfigManager *pCfgMan = m_pEB->GetConfigManager();
			bool bIsTel = TestPropFlag(XPSPPF_IS_IN_TELEPORT) && pCfgMan && pCfgMan->IsBlurForTeleport();
			if (bIsTel)
			{
				tbf.tick = m_pEB->GetTick();
				tbf.loc = m_pMovableImp->getLocation();
				if (m_blurList.size() == 0)
				{
					m_blurList.push_back(tbf);
					bIsTel = false;
				} else {
					spriteHandle = m_pEB->GetSpritePictureHandle();
				}
			} 
			if (waterDepth > 0) 
			{
				spriteHandle = m_pEB->GetSpritePictureHandle();
			}

			if (spriteHandle == INVALID_PICTURE_HANDLE)
			{
				m_pRenderableSprite->RenderSprite(TRUE, canvas, NULL, isAlpha, m_bShaking, realRender);
			}
			else
			{
				if (realRender)
				{
					Renderer *pRenderer = m_pEB->GetRenderer();
					NuclearRect vp = m_pEB->GetViewport();
					int width = vp.Width();
					int height = vp.Height();
					NuclearFRectt frctbox, fsrcBox;
					//只有直接画到BackBuffer上才抗锯齿，不过既然只有涉水和冲锋击退的时候才用RenderTarget，那这会儿就不抗了吧
					pRenderer->PushRenderTarget(spriteHandle);
					m_pRenderableSprite->RenderSprite(FALSE, canvas, &frctbox, false, m_bShaking, realRender);
					pRenderer->PopRenderTarget();

					frctbox.left = std::max<float>(frctbox.left, 0);
					frctbox.left = std::min<float>(frctbox.left, width);
					frctbox.right = std::max<float>(frctbox.right, 0);
					frctbox.right = std::min<float>(frctbox.right, width);
					frctbox.top = std::max<float>(frctbox.top, 0);
					frctbox.top = std::min<float>(frctbox.top, height);
					frctbox.left = std::max<float>(frctbox.left, 0);
					frctbox.left = std::min<float>(frctbox.left, height);
					if (frctbox.left < frctbox.right && frctbox.top < frctbox.bottom)
					{
						NuclearFRectt teleBlur;
						//目前精灵的spriteHandle的大小和Viewport一样，所以就这样吧。
						//但是其实逻辑上算srcBox的时候不应该除以viewport的宽高，应该除以spriteHandle的宽高
						fsrcBox.left = frctbox.left / width;
						fsrcBox.right = frctbox.right / width;
						fsrcBox.top = frctbox.top / height;
						fsrcBox.bottom = frctbox.bottom / height;
						//画残影
						if (bIsTel)
						{//画残影
							DrawBatchPictureParam blurparam;
							TeleportBlurList::iterator it = m_blurList.begin();
							NuclearPoint vplt(vp.left, vp.top);
							float fWorldScale = m_pEB->GetWorldScale();
							blurparam.rectCount = static_cast<int>(m_blurList.size());
							std::vector<NuclearFRectt> rects(blurparam.rectCount);
							std::vector<NuclearColor> blurColor(blurparam.rectCount);
							blurparam.pColor = &(blurColor[0]);
							blurparam.pRect = &(rects[0]);
							blurparam.colorCount = 0;
							blurparam.pSrcrect = &fsrcBox;
							blurparam.handle = spriteHandle;
							for (;it != m_blurList.end(); )
							{
								int d = tbf.tick - it->tick;
								if (d > m_nTeleportBlurTime)
								{
									it = m_blurList.erase(it);
									--blurparam.rectCount;
								} else {
									NuclearPoint adj = it->loc - tbf.loc;
									adj.x = static_cast<int>(adj.x * fWorldScale);
									adj.y = static_cast<int>(adj.y * fWorldScale);
									float alphadel = static_cast<float>(m_nTeleportBlurTime - d) / m_nTeleportBlurTime;
									blurColor[blurparam.colorCount].a = static_cast<unsigned char>(m_nAlpha * alphadel * alphadel * alphadel * 0.8);
									rects[blurparam.colorCount] = frctbox + adj;
									++blurparam.colorCount;
									++it;
								}
							}
							pRenderer->DrawBatchPicture(blurparam);
							m_blurList.push_back(tbf);
						}

						//画backbuffer
						DrawBatchPictureParam param;
						param.handle = spriteHandle;
						param.pRect = &frctbox;
						param.pSrcrect = &fsrcBox;
						param.rectCount = 1;
						pRenderer->DrawBatchPicture(param);
						m_pRenderableSprite->Render3DFrontEffect();
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

	void Sprite::TeleportWithBlur(const NuclearLocation &point, int telTime, int blurTime)
	{
		StopMove();
		m_nTeleportStartTime = m_pEB->GetTick();
		SetPropFlag(XPSPPF_IS_IN_TELEPORT, true);
		m_TeleportObj = point;
		m_nTeleportTime = telTime;
		m_nTeleportBlurTime = blurTime;
		m_TeleportStart = GetLocation();
		m_blurList.clear();
	}

	void Sprite::StopTeleport(const NuclearLocation &point)
	{
		if (TestPropFlag(XPSPPF_IS_IN_TELEPORT) == true)
			SetPropFlag(XPSPPF_IS_IN_TELEPORT, false);
	}
	void Sprite::SetRideName(const std::wstring& name)
	{
		if (name == L"" || name == L"0")
		{
			m_nHorseHeight = 0;
			m_fHorseScale = 1.0;
			SetPropFlag(XPSPPF_IS_READING, false);
			ResetEffectPos();
			int layerCount = m_pEB->GetSpriteManager()->GetLayerCount(m_modelName);
			for (int i = 0; i < layerCount; i++)
			{
				if (m_pEB->GetSpriteManager()->GetLayerRideEnable(m_modelName, i))
				{
					SetComponent(i, L""); //remove old
				}
			}
			return;
		}

		int layerCount = m_pEB->GetSpriteManager()->GetLayerCount(m_modelName);
		for (int i = 0; i < layerCount; i++)
		{
			if (m_pEB->GetSpriteManager()->GetLayerRideEnable(m_modelName, i))
			{
				SetComponent(i, L""); //remove old first
				SetComponent(i, name);
			}
		}

		//ResetHorseHeight(name);
		m_nHorseHeight = m_pEB->GetSpriteManager()->GetHorseHeight(m_modelName, name);
		m_fHorseScale = m_pEB->GetSpriteManager()->GetHorseScale(m_modelName, name);
		SetPropFlag(XPSPPF_IS_READING, true);
		ResetEffectPos();
	}

	int Sprite::GetLayerCount()
	{
		int iResult = m_pEB->GetSpriteManager()->GetLayerCount(m_modelName);
		return iResult;
	}
	int Sprite::GetLayerIndexByName(std::wstring wsName)
	{
		int layerCount = m_pEB->GetSpriteManager()->GetLayerCount(m_modelName);
		for (int i = 0; i < layerCount; i++)
		{
			std::wstring wsCurName;
			wsCurName = m_pEB->GetSpriteManager()->GetLayerName(m_modelName, i);
			if (wsCurName.compare(wsName.c_str()) == 0)
			{
				return i;
			}
		}
		return -1;
	}
	std::wstring Sprite::GetLayerNameByIndex(int iIndex)
	{
		std::wstring wsCurName;
		wsCurName = m_pEB->GetSpriteManager()->GetLayerName(m_modelName, iIndex);
		return wsCurName;
	}
	bool Sprite::IsPartLayer(int iIndex)
	{
		std::wstring wsCurName;
		wsCurName = GetLayerNameByIndex(iIndex);
		if (wsCurName.size() > 4)
		{
			if (wsCurName.substr(0, 4).compare(L"part") == 0)
			{
				return true;
			}
		}
		return false;
	}

	void Sprite::EnableStepSound(bool b)
	{
//		if (TestPropFlag(XPSPPF_HAS_SET_STEP_SOUND) != b)
//		{
//			SetPropFlag(XPSPPF_HAS_SET_STEP_SOUND, b);
//			StepSoundType* pSST = m_pEB->GetStepSoundType();
//			if (!pSST)
//				return;
//			const XPSSModelMap &mmap = pSST->GetModelMap();
//			XPSSModelMap::const_iterator mIt = mmap.find(GetModelName());
//			if (mIt == mmap.end())
//				return;
//			XPStepNotifies::const_iterator nIt = mIt->second.notifies.begin(),
//				nIe = mIt->second.notifies.end();
//			World* pWorld = m_pEB->GetXPWorld();
//			if (b)
//			{
//				for (;nIt!=nIe;++nIt)
//				{
//					RegisterFrameNotify(pWorld, nIt->actname, nIt->frame);
//				}
//			} else {
//				for (;nIt!=nIe;++nIt)
//				{
//					DeregisterFrameNotify(pWorld, nIt->actname, nIt->frame);
//				}
//			}
//		}
	}
	
	bool Sprite::SetBindFile(int id, const std::wstring &bindFile)
	{
//		if (id < 0 || id > 1)
//			return false;
//		if (m_strBindFile[id] == bindFile)
//			return true;
//		if (bindFile == L"")
//		{
//			m_Sebind[id] = NuclearHardRef<Sebind>();
//			m_strBindFile[id] = bindFile;
//			return true;
//		}
//		m_Sebind[id] = m_pEB->GetEffectManager()->GetSebindManager()->GetSebind(bindFile);
//		if (!m_Sebind[id].IsValid())
//		{
//			m_strBindFile[id] = L"";
//			return false;
//		}
//		m_strBindFile[id] = bindFile;
		return true;
	}

	float Sprite::GetModelScale() const
	{
		float modelScale = 1;
		if (m_pEB)
		{
			SpriteManager* pSpriteMgr = m_pEB->GetSpriteManager();
			if (pSpriteMgr)
			{
				modelScale = pSpriteMgr->GetModelScale(m_modelName);
			}
		}
		return modelScale;
	}

	void Sprite::SetScale(float scale)
	{
		float modelScale = GetModelScale();
		m_fSetScale = scale;
		m_fScale = modelScale * m_fSetScale;
		EffectInfoList::iterator lIt = m_lstTmpEffects.begin(), lIe = m_lstTmpEffects.end();
		for (;lIt!=lIe;++lIt)
		{
			if (lIt->flag & XPSPREFFFLAG_NO_FOLLOW_SCALE)
				continue;
//			lIt->effect->SetScale(scale);
		}
		EffectInfoMap::iterator mIt = m_mapDuraEffects.begin(), mIe = m_mapDuraEffects.end();
		for (;mIt!=mIe;++mIt)
		{
			if (mIt->second.flag & XPSPREFFFLAG_NO_FOLLOW_SCALE)
				continue;
//			mIt->second.effect->SetScale(scale);
		}
		if (m_pRenderableSprite)
			m_pRenderableSprite->SetScale(m_fScale);
	}

	IEffect* Sprite::PlayEffect(const std::wstring &effect_name, const NuclearPoint& relpos, int times, const std::wstring &hostname, bool async)
	{
		if (!m_pRenderableSprite)
			return NULL;

		Effect *tpEffect = m_pEB->GetEffectManager()->CreateEffect(effect_name, false);
		if (tpEffect && tpEffect->GetType() != XPET_3D)
		{
			XPASSERT(false && L"Sprite::PlayEffect, effect is not 3D");
			delete tpEffect;
			return NULL;
		}
		return tpEffect;
	}

	IEffect* Sprite::PlayEffect(const std::wstring &effect_name, int dx, int dy, int times, unsigned int flag, unsigned char soundtype, int TimeMode, int iRot)
	{
		Effect *tpEffect = m_pEB->GetEffectManager()->CreateEffect(effect_name, false);
		if( tpEffect )
		{
			tpEffect->SetRotationRadian(FIRE_PI / 180.0f * iRot);
			if (flag & XPSPREFFFLAG_NO_SOUND)
				tpEffect->RemoveAudio();
			else
				tpEffect->SetSoundType(soundtype);
			if (TimeMode == 0)
			{
				tpEffect->SetLoopNum(times);
			}
			else if (TimeMode == 1)
			{
				tpEffect->SetPlayTime(times);
			}
			
			switch (tpEffect->AssureResource((flag & XPSPREFFFLAG_ASYNC) != 0))
			{
			case XPEARR_RESREADY:
				{
					tpEffect->Play();
					EffectInfo info(flag, dx, dy, tpEffect);
					m_lstTmpEffects.push_back(info);
				}
				break;
			case XPEARR_LOADING:
				{
					SpriteEffectNotify* pNotify = new SpriteEffectNotify();
					pNotify->flag = flag;
					pNotify->dx = dx;
					pNotify->dy = dy;
					pNotify->pSprite = this;
					tpEffect->AddNotify(pNotify);
					m_sprEffNotifys.insert(pNotify);
				}
				break;
			case XPEARR_LOAD_ERROR:
				delete tpEffect;
				return NULL;
				break;
			}
			if ((flag & XPSPREFFFLAG_NO_FOLLOW_SCALE) == 0)
			{
				tpEffect->SetScale(m_fScale);
			}
			tpEffect->SetScaleType(XPEST_ALLSCALE);//XPEST_ONLY_SCALE_POS
			if (GetScreenCoord())
			{
				tpEffect->SetScreenCrood(true);
			}
		}

		return tpEffect;
	}

	bool Sprite::ChangeEffectOffset(IEffect* pEffect, const NuclearPoint &relpos)
	{
		EffectInfoMap::iterator it = m_mapDuraEffects.find(pEffect);
		if (it != m_mapDuraEffects.end())
		{
			it->second.dx = relpos.x;
			it->second.dy = relpos.y;
		} else {
			std::map<IEffect*, SpriteEffectNotify*>::iterator mIt = m_sprEffNotMap.find(pEffect);
			if (mIt != m_sprEffNotMap.end())//还没有加载完呢
			{
				mIt->second->dx =  relpos.x;
				mIt->second->dy =  relpos.y;
			}
			else
			{// 原来是3D的啊
				if (!m_pRenderableSprite)
					return false;
				return m_pRenderableSprite->ChangeEffectOffset(pEffect, relpos);
			}
		}
		return true;
	}

	IEffect* Sprite::SetDurativeEffect(const std::wstring &effect_name,  const NuclearPoint& relpos, unsigned int flag)
	{
		Effect* tpEffect = m_pEB->GetEffectManager()->CreateEffect(effect_name, true);
		if( tpEffect )
		{
			if (flag & XPSPREFFFLAG_NO_SOUND)
				tpEffect->RemoveAudio();
			switch (tpEffect->AssureResource((flag & XPSPREFFFLAG_ASYNC) != 0))
			{
			case XPEARR_RESREADY:
				{
					tpEffect->Play();
					if (flag & XPSPREFFFLAG_BIND_AUTO)
					{
						flag &= ~XPSPREFFFLAG_BIND_AUTO;
						if (tpEffect->GetEffectBindType() == XPEBT_EFFECT_POS)
							flag |= XPSPREFFFLAG_BIND_EFF_POS;
					}
					EffectInfo info(flag, relpos.x, relpos.y, tpEffect);
					m_mapDuraEffects[tpEffect] = info;
				}
				break;
			case XPEARR_LOADING:
				{
					SpriteEffectNotify* pNotify = new SpriteEffectNotify();
					pNotify->flag = flag | XPSPREFFFLAG_SET_EFFECT;
					pNotify->dx = relpos.x;
					pNotify->dy = relpos.y;
					pNotify->pSprite = this;
					tpEffect->AddNotify(pNotify);
					m_sprEffNotifys.insert(pNotify);
					m_sprEffNotMap[tpEffect] = pNotify;
				}
				break;
			case XPEARR_LOAD_ERROR:
				delete tpEffect;
				return NULL;
				break;
			}
			if ((flag & XPSPREFFFLAG_NO_FOLLOW_SCALE) == 0)
			{
				tpEffect->SetScale(m_fScale);
			}
			tpEffect->SetScaleType(XPEST_ALLSCALE);//XPEST_ONLY_SCALE_POS
			if (GetScreenCoord())
			{
				tpEffect->SetScreenCrood(true);
			}
			return tpEffect;
		}
		return NULL;
	}

	IEffect* Sprite::SetContinueEffect(const std::wstring &headEffect, const std::wstring &midEffect, const std::wstring &endEffect, int continueTime, const NuclearPoint& relpos, unsigned int flag)
	{
		Effect* tpEffect = m_pEB->GetEffectManager()->CreateListEffect(headEffect, midEffect, endEffect, continueTime, true, (flag & XPSPREFFFLAG_ASYNC) != 0, (flag & XPSPREFFFLAG_NO_SOUND) != 0);
		if( tpEffect )
		{
			if (tpEffect->GetPlayState() == XPES_EMPTY)
			{
				SpriteEffectNotify* pNotify = new SpriteEffectNotify();
				pNotify->flag = flag | XPSPREFFFLAG_SET_EFFECT;
				pNotify->dx = relpos.x;
				pNotify->dy = relpos.y;
				pNotify->pSprite = this;
				tpEffect->AddNotify(pNotify);
				m_sprEffNotifys.insert(pNotify);
				m_sprEffNotMap[tpEffect] = pNotify;
			}
			else
			{
				tpEffect->Play();
				if (flag & XPSPREFFFLAG_BIND_AUTO)
				{
					flag &= ~XPSPREFFFLAG_BIND_AUTO;
					if (tpEffect->GetEffectBindType() == XPEBT_EFFECT_POS)
						flag |= XPSPREFFFLAG_BIND_EFF_POS;
				}
				EffectInfo info(flag, relpos.x, relpos.y, tpEffect);
				m_mapDuraEffects[tpEffect] = info;
			}
			if ((flag & XPSPREFFFLAG_NO_FOLLOW_SCALE) == 0)
			{
				tpEffect->SetScale(m_fScale);
			}
			tpEffect->SetScaleType(XPEST_ALLSCALE);//XPEST_ONLY_SCALE_POS
		}
		if (GetScreenCoord())
		{
			tpEffect->SetScreenCrood(true);
		}
		return tpEffect;

	}

	void Sprite::RemoveDurativeEffect(IEffect* pEffect)
	{
		EffectInfoMap::iterator it = m_mapDuraEffects.find(pEffect);
		if (it != m_mapDuraEffects.end())
		{
			pEffect->ClearNotify();
			pEffect->Stop();
			it->second.flag = XPSPREFFFLAG_DELETED;
		}
		else
		{
			std::map<IEffect*, SpriteEffectNotify*>::iterator mIt = m_sprEffNotMap.find(pEffect);
			if (mIt != m_sprEffNotMap.end())//还没有加载完呢，就删掉了
			{
				m_sprEffNotifys.erase(mIt->second);
				mIt->second->pSprite = NULL;
			}
			else
			{// 原来是3D的啊
				if (m_pRenderableSprite)
					m_pRenderableSprite->RemoveEffect(pEffect);
			}
		}
	}

	void Sprite::UpdateEffect(DWORD tickTime)
	{
		static float HALF_RADICAL_2 = 0.70710678118654752440084436210485f;//同时也是cos45 、sin45
		static float COS[8] = { -1.0f, -HALF_RADICAL_2, 0.0f, HALF_RADICAL_2,  1.0f, HALF_RADICAL_2, 0.0f, -HALF_RADICAL_2};
		static float SIN[8] = { 0.0f, -HALF_RADICAL_2, -1.0f, -HALF_RADICAL_2,  0.0f, HALF_RADICAL_2, 1.0f, HALF_RADICAL_2};
		NuclearDirection dir = GetDirection();
		NuclearFPoint targetDir = m_pMovableImp->getVectorDirection();
		NuclearLocation loc = m_pMovableImp->getLocation();
		NuclearLocation eLoc = loc + m_effectPos;
		NuclearLocation NewLoc;
		for(EffectInfoMap::iterator it = m_mapDuraEffects.begin(); it != m_mapDuraEffects.end();)
		{
			EffectInfo& info = it->second;

			bool bNeedUpdate = false;

			if (info.flag & XPSPREFFFLAG_DELETED)
			{
				m_mapDuraEffects.erase(it++);
				continue;
			}
			if (info.flag & XPSPREFFFLAG_BIND_EFF_POS)
			{
				NewLoc = eLoc;
				if (m_bRideAniIsReady)
				{
					NewLoc.y -= (m_nHorseHeight + 60) * m_fScale;
				}
				NewLoc.y -= m_iFlyOffsetYCur / 100.0f * m_fScale * (m_iFlyScaleCur / 10000.0f);
			}
			else
			{
				NewLoc = loc;
			}
			if (info.flag & XPSPREFFFLAG_BIND_WEAPON_0)
			{
				//受绑定文件0控制
				if ((!m_Sebind[0].IsValid()) || (!m_Sebind[0]->Update(this, info.effect.operator ->(), tickTime)))
				{
					info.effect->SetDirection(dir, targetDir);
				}
			}
			else if (info.flag & XPSPREFFFLAG_BIND_WEAPON_1)
			{
				//受绑定文件1控制
				if ((!m_Sebind[1].IsValid()) || (!m_Sebind[1]->Update(this, info.effect.operator ->(), tickTime)))
				{
					info.effect->SetDirection(dir, targetDir);
				}
			}
			else
			{
				//不受绑定文件控制
				if (info.flag & XPSPREFFFLAG_SELF_REF)
				{
					info.effect->SetLocation(static_cast<int>(NewLoc.x + info.dx*COS[dir] - info.dy*SIN[dir]), static_cast<int>(NewLoc.y + info.dx*SIN[dir] + info.dy*COS[dir]));
				}
				else
				{
					info.effect->SetLocation(NewLoc.x + info.dx, NewLoc.y + info.dy);
				}
				info.effect->SetDirection(dir, targetDir);
				bNeedUpdate = true;
			}
			if (info.flag & XPSPREFFFLAG_LINKING_FRAME)
			{
				XapEffect* pEffect = dynamic_cast<XapEffect*>(info.effect.operator->());
				XPASSERT(pEffect /*&& L"精灵某特效有XPSPREFFFLAG_LINKING_FRAME标记，但是不是XapEffect"*/);
				pEffect->SetPlayingFrame(m_iCurFrame);
			}

			if (info.flag &  XPSPREFFFLAG_BIND_EFF_POS_WITH_DIR)
			{
				UpdateEffectPosWithDir(info);

				bNeedUpdate = true;
			}

			if (!info.flag & XPSPREFFFLAG_NO_FOLLOW_SCALE)
			{
				info.effect->SetScale(m_fScale);
			}

			if (bNeedUpdate)
			{
				info.effect->Update(tickTime);
			}

			++it;
		}
		for(std::list<EffectInfo>::iterator it = m_lstTmpEffects.begin(); it != m_lstTmpEffects.end();)
		{
			EffectInfo& info = *it;
			if (info.flag & XPSPREFFFLAG_SELF_REF)
			{
				info.effect->SetLocation(static_cast<int>(loc.x + info.dx*COS[dir] - info.dy*SIN[dir]), static_cast<int>(loc.y + info.dx*SIN[dir] + info.dy*COS[dir]));
			}
			else
			{
				info.effect->SetLocation(loc.x + info.dx, loc.y + info.dy);
			}
			
			info.effect->SetDirection(dir);
			if (!info.effect->Update(tickTime))
			{
				info.effect->Release();
				it = m_lstTmpEffects.erase(it);
			}
			else
			{
				++it;
			}
		}
	}

	void Sprite::RenderEffectFront(Canvas* canvas, bool realRender)
	{
		World *pWorld = m_pEB->GetXPWorld();
		for(EffectInfoMap::iterator it = m_mapDuraEffects.begin(); it != m_mapDuraEffects.end(); ++it)
		{
			if (((it->second.flag & XPSPREFFFLAG_DRAW_ON_TOP) != 0) && pWorld)
			{
				pWorld->PushSpriteTopEffect(&(*it->second.effect));
			}
			else if ((it->second.flag & XPSPREFFFLAG_DRAW_ON_BOTTOM) != 0)
			{
				continue;
			}
			else if ((it->second.flag & XPSPREFFFLAG_DRAW_BEFORE_SPRITE) == 0)
			{
				it->second.effect->RenderFront(canvas, realRender);
			}
		}
		for(std::list<EffectInfo>::iterator it = m_lstTmpEffects.begin(); it != m_lstTmpEffects.end(); ++it)
		{
			if (((it->flag & XPSPREFFFLAG_DRAW_ON_TOP) != 0) && pWorld)
			{
				pWorld->PushSpriteTopEffect(&(*it->effect));
			}
			else if ((it->flag & XPSPREFFFLAG_DRAW_ON_BOTTOM) != 0)
			{
				continue;
			}
			else if ((it->flag & XPSPREFFFLAG_DRAW_BEFORE_SPRITE) == 0)
			{
				it->effect->RenderFront(canvas, realRender);
			}
		}
		if (realRender && m_pRenderableSprite)
		{
			m_pRenderableSprite->RenderChildSpriteEffectFront();
		}
	}
	void Sprite::RenderEffectBack(Canvas* canvas, bool realRender)
	{
		for(EffectInfoMap::iterator it = m_mapDuraEffects.begin(); it != m_mapDuraEffects.end(); ++it)
		{
			if ((it->second.flag & XPSPREFFFLAG_DRAW_ON_TOP) != 0)
			{
				continue;
			}
			if ((it->second.flag & XPSPREFFFLAG_DRAW_ON_BOTTOM) != 0)
			{
				continue;
			}
			if ((it->second.flag & XPSPREFFFLAG_DRAW_BEFORE_SPRITE) != 0)
			{
				it->second.effect->Render(canvas, realRender);
			}
			else
			{
				it->second.effect->RenderBack(canvas, realRender);
			}
		}
		for(std::list<EffectInfo>::iterator it = m_lstTmpEffects.begin(); it != m_lstTmpEffects.end(); ++it)
		{
			if ((it->flag & XPSPREFFFLAG_DRAW_ON_TOP) != 0)
			{
				continue;
			}
			if ((it->flag & XPSPREFFFLAG_DRAW_ON_BOTTOM) != 0)
			{
				continue;
			}
			if ((it->flag & XPSPREFFFLAG_DRAW_BEFORE_SPRITE) != 0)
			{
				it->effect->Render(canvas, realRender);
			}
			else
			{
				it->effect->RenderBack(canvas, realRender);
			}
		}
		if (realRender && m_pRenderableSprite)
		{
			m_pRenderableSprite->RenderChildSpriteEffectBack();
		}
	}

	void Sprite::UpdateEffectPosWithDir(EffectInfo& info)
	{
		NuclearLocation loc = m_pMovableImp->getLocation();

		NuclearFPoint effPos;

		NuclearDirection spriteDirType = GetDirection();
		float spriteRadian = TransXPDirection2Radian(spriteDirType);

		if (info.dx == 0 && info.dy == 0)
		{
			effPos = loc;
		}
		else
		{
			NuclearFPoint spriteDir = TransXPDirection2Vector(spriteDirType);
			spriteDir.normalize();

			NuclearFPoint effDir(-spriteDir.x, -spriteDir.y);
			effPos = loc + effDir * info.dx * m_fScale;

			float yOffset = info.dy;
			switch (spriteDirType)
			{
			case XPDIR_TOPRIGHT:
			case XPDIR_TOPLEFT:
			case XPDIR_TOP:
				yOffset = info.dy * 3.f / 2;
				break;

			case XPDIR_BOTTOMRIGHT:
			case XPDIR_BOTTOMLEFT:
				yOffset = info.dy * 2.f / 6;
				break;
			}

			effPos.y -= yOffset * m_fScale;
		}

		// 目前不能对特效应用旋转
		//info.effect->SetRotationRadian(spriteRadian);
		info.effect->SetLocation(effPos.x, effPos.y);

		float spriteAlpha = m_nAlpha / 255.f;
		info.effect->SetTransparent(spriteAlpha);
	}

	void Sprite::Update(DWORD ticktime)
	{
		if (!TestPropFlag(Sprite::XPSPPF_IS_IN_TELEPORT))
		{
			m_blurList.clear();
		}
		UpdateEffect(ticktime);

		long long TT = ticktime;

		//if (m_iFlyScaleCur != m_iFlyScaleTgt)
		//{
		//	if (m_iFlyScaleCur < m_iFlyScaleTgt)
		//	{
		//		m_iFlyScaleCur += m_iFlyScaleStep * TT / 1000.0f;
		//		if (m_iFlyScaleCur > m_iFlyScaleTgt)
		//		{
		//			m_iFlyScaleCur = m_iFlyScaleTgt;
		//		}
		//	}
		//	if (m_iFlyScaleCur > m_iFlyScaleTgt)
		//	{
		//		m_iFlyScaleCur -= m_iFlyScaleStep * TT / 1000.0f;
		//		if (m_iFlyScaleCur < m_iFlyScaleTgt)
		//		{
		//			m_iFlyScaleCur = m_iFlyScaleTgt;
		//		}
		//	}
		//}

		if (m_iFlyOffsetYCur != m_iFlyOffsetYTgt)
		{
			float fCurStep = m_iFlyOffsetYStep * TT / 1000.0f;
			int iCurStep = fCurStep;
			if (m_iFlyOffsetYStep > 0 && iCurStep == 0)
			{
				iCurStep = 1;
			}
			if (m_iFlyOffsetYCur < m_iFlyOffsetYTgt)
			{
				m_iFlyOffsetYCur += iCurStep;
				if (m_iFlyOffsetYCur > m_iFlyOffsetYTgt)
				{
					m_iFlyOffsetYCur = m_iFlyOffsetYTgt;
				}
			}
			if (m_iFlyOffsetYCur > m_iFlyOffsetYTgt)
			{
				m_iFlyOffsetYCur -= iCurStep;
				if (m_iFlyOffsetYCur < m_iFlyOffsetYTgt)
				{
					m_iFlyOffsetYCur = m_iFlyOffsetYTgt;
				}
			}
		}
		m_iFlyScaleCur = (m_iFlyOffsetYCur + 10000);// / 10000 * 10000;

	}

	// yeqing 2015-11-07
	void Sprite::UpdateAction(DWORD ticktime)
	{
		if (m_pRenderableSprite)
			m_pRenderableSprite->UpdateAction(ticktime);
	}

	// 移动嘀嗒。参数为上一次嘀嗒依赖的时间差。by world
	void Sprite::tick(int now, int deltaTime)
	{
		if ( TestPropFlag(XPSPPF_IS_IN_TELEPORT) )
		{	//正在瞬间移动中
			int telD = now - m_nTeleportStartTime;
			if (telD < m_nTeleportTime)	//还没有移动完成
			{
				NuclearLocation vec = m_TeleportObj - m_TeleportStart;
				float delta = static_cast<float>(telD) / m_nTeleportTime;
				vec.x = static_cast<int>(vec.x * delta);
				vec.y = static_cast<int>(vec.y * delta);
				vec += m_TeleportStart;
				SetLocation(vec);
			} else if ( telD <= m_nTeleportTime + m_nTeleportBlurTime) { //移动到位了，但是留着残影
				SetLocation(m_TeleportObj);
			} else {	//连残影都没有了
				//m_iAniStartTime += TELEPORT_AND_BLUR_TIME;
				SetLocation(m_TeleportObj);
				SetPropFlag(XPSPPF_IS_IN_TELEPORT, false);
				m_pEB->GetApp()->OnUpdateSpriteAction(this, XPUSAT_TELEPORTEND);
			}
		}

		m_pMovableImp->tick(now, deltaTime);
		Update(deltaTime);
	}
    
    void Sprite::SetComponentAniReleaseFlag(Nuclear::NuclearPaniReleaseFlag flag)
    {
        if(m_pRenderableSprite)
            m_pRenderableSprite->SetComponentAniReleaseFlag(flag);
    }

	void  Sprite::StartShake()
	{
		m_bShaking = true;
	}

	void Sprite::StopShake()
	{
		m_bShaking = false;
	}

	void Sprite::SetCurveMove(bool b)
	{
		if (m_pMovableImp)
		{
			m_pMovableImp->SetCurveMove(b);
		}
	}

	void Sprite::SetFlyScaleTgt(int iFlyScaleTgt)
	{
		m_iFlyScaleTgt = iFlyScaleTgt;
	}
	void Sprite::SetFlyScaleCur(int iFlyScaleCur)
	{
		m_iFlyScaleCur = iFlyScaleCur;
	}
	int Sprite::GetFlyScaleCur()
	{
		if (GetUseWorldScale() == 0)
		{
			return m_iFlyScaleCur;
		}
		else
		{
			return (int)(m_iFlyScaleCur * m_pEB->GetWorldScale());
		}
	}
	void Sprite::SetFlyScaleStep(int iFlyScaleStep)
	{
		m_iFlyScaleStep = iFlyScaleStep;
	}
	int Sprite::GetFlyScaleStep()
	{
		return m_iFlyScaleStep;
	}
	void Sprite::SetFlyOffsetYTgt(int iFlyOffsetYTgt)
	{
		m_iFlyOffsetYTgt = iFlyOffsetYTgt;
	}
	void Sprite::SetFlyOffsetYCur(int iFlyOffsetYCur)
	{
		m_iFlyOffsetYCur = iFlyOffsetYCur;
	}
	int Sprite::GetFlyOffsetYCur()
	{
		return m_iFlyOffsetYCur;
	}
	void Sprite::SetFlyOffsetYStep(int iFlyOffsetYStep)
	{
		m_iFlyOffsetYStep = iFlyOffsetYStep;
		if (m_iFlyOffsetYStep < 0)
		{
			m_iFlyOffsetYStep *= -1;
		}
	}
	int Sprite::GetFlyOffsetYStep()
	{
		return m_iFlyOffsetYStep;
	}
	void Sprite::SetUseWorldScale(int iUseWorldScale)
	{
		m_iUseWorldScale = iUseWorldScale;
	}
	int Sprite::GetUseWorldScale()
	{
		return m_iUseWorldScale;
	}

	void Sprite::removeEffect(IEffect* effect)
	{
		std::map<IEffect*, SpriteEffectNotify*>::iterator it = m_sprEffNotMap.find(effect);
		if (it != m_sprEffNotMap.end())
		{
			if (!effect->m_isSpine)
			{
				m_pEB->GetEffectManager()->RemoveEffect(effect);
			}
			m_sprEffNotMap.erase(it);
		}
	}
};
