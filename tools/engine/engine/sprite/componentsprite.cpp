#include "stdafx.h"
#include "..\common\log.h"
#include "..\common\recttopologylist.h"
#include "..\renderer\renderer.h"
#include "..\engine\configmanager.h"
#include "..\soundarea\stepsoundtype.h"
#include "..\sound\directsoundmanager.h"
#include "..\iapp.h"
#include "spritemanager.h"
#include "ComponentSprite.h"


static const float UNDER_WATER_ALPHA = 80.0f;
static const int TAN_THETA = 2;	//当sprite是斜的时候，水面是斜的，宽高比是2（水面和城市网格平行）
static const DWORD SHADOW_COLOR_ON_LAND = 0x66000000;
static const DWORD SHADOW_COLOR_IN_WATER = 0x40000000;

namespace Nuclear
{
	ComponentSprite::ComponentSprite(EngineBase *pEB, const std::wstring &modelname)
		: Sprite(pEB), m_pDefaultAction(NULL), m_pTmpAction(NULL), m_pActionMap(NULL), 
		m_flagRideLayer(0), m_FlagEffectLayer(0), m_ModelName(L""), m_nLayerCount(0), 
		m_nSoundPriority(0)
	{
		m_nOldFrame = -1;
		m_fMinEffectAlpha = 0.1f;
		m_fSlopeEffectAlpha = (0.5f - m_fMinEffectAlpha) / XP_EFFECT_HALF_CYCLE;
		m_fEffectAlpha = m_fMinEffectAlpha;
		m_nBeginTick = pEB->GetTick();
		SetPropFlag(XPSPPF_RENDER_SHADOW, true);
		SetModel(modelname);
	}

	ComponentSprite::~ComponentSprite()
	{
		ReleaseAllActions();
	}

	void ComponentSprite::ReleaseAllActions()
	{
		ActionNameSet::iterator it = m_holdActions.begin(), ie = m_holdActions.end();
		for (;it!=ie;)
		{
			ReleaseAction(*(it++));
		}
	}

	void ComponentSprite::ResetStepAction(const std::wstring& ridename)
	{
		if (m_ModelName == L"")
		{
			m_soundActorName = L"";
			return;
		}
		StepSoundType *pSST = m_pEngine->GetStepSoundType();
		if (!pSST)
			return;
		const XPSSModelMap &mm = pSST->GetModelMap();
		XPSSModelMap::const_iterator it = mm.find(m_ModelName);
		if (it == mm.end())
		{
			m_soundActorName = L"";
			return;
		}
		if (ridename == L"")
		{
			m_soundActorName = it->second.defaultType;
			return;
		}
		XPRideType::const_iterator rIt = it->second.ridetype.find(ridename);
		if (rIt == it->second.ridetype.end())
		{
			m_soundActorName = L"";
			return;
		}
		m_soundActorName = rIt->second;
	}

	void ComponentSprite::ResetEffectPos()
	{
		if (m_ModelName == L"")
		{
			m_effectPos.x = m_effectPos.y = 0;
			return;
		}
		m_effectPos = m_pEngine->GetSpriteManager()->GetEffectPos(m_ModelName);
		m_effectPos.y -= (TestPropFlag(XPSPPF_IS_READING)?m_nHorseHeight+25:0);
	}

	void ComponentSprite::SetModel(const std::wstring &modelname)
	{
		if( modelname == m_ModelName ) return;
		ReleaseAllActions();
		m_CurComponents.clear();
		m_Colors.clear();
		m_pDefaultAction = NULL;
		m_pTmpAction = NULL;
		m_strDefaultActionOutsideName = L"";
		m_strTmpActionOutsideName = L"";
		m_flagRideLayer = 0;
		m_FlagEffectLayer = 0;
		m_nLayerCount = 0;
		m_pActionMap = NULL;
		SetPropFlag(XPSPPF_HAS_EFFECT_LAYER, false);
		SetPropFlag(XPSPPF_HAS_DEF_ASYNC_LOADING, false);
		SetPropFlag(XPSPPF_HAS_CUR_ASYNC_LOADING, false);
		if( modelname != L"" )
		{
			SpriteManager* pSprMan = m_pEngine->GetSpriteManager();
			m_nLayerCount = pSprMan->GetLayerCount(modelname);
			m_CurComponents.resize(m_nLayerCount, NULL);
			m_Colors.resize(m_nLayerCount, 0xffffffff);
			m_nTitlePos = pSprMan->GetTitlePos(modelname);
			m_surfaceType = pSprMan->GetWaterSurfaceType(modelname);
			unsigned char flag = 1;
			for (int i=0;i<m_nLayerCount;++i)
			{
				if (pSprMan->GetLayerRideEnable(modelname, i))
				{
					m_flagRideLayer |= flag;
				}
				if (pSprMan->GetLayerEffectEnable(modelname, i))
				{
					m_FlagEffectLayer |= flag;
					SetPropFlag(XPSPPF_HAS_EFFECT_LAYER, true);
				}
				flag <<= 1;
			}
			Nuclear::ModelSoundParam &param = pSprMan->GetSoundParam(modelname);
			m_pActionMap = param.soundmap[L""];
		}
		m_ModelName = modelname;
		ResetStepAction(L"");
		ResetEffectPos();
	}

	bool ComponentSprite::GetComponent(int scid, std::wstring& resource)
	{
		if( scid < 0 || scid >= m_nLayerCount ) return false;
		if (!m_CurComponents[scid]) return false;
		resource = m_CurComponents[scid]->GetConstName();
		return true;
	}

	bool ComponentSprite::GetComponentColor(int scid, XPCOLOR &color)
	{
		if( scid < 0 || scid >= m_nLayerCount ) return false;
		if (!m_CurComponents[scid]) return false;
		color = m_Colors[scid];
		return true;
	}

	bool ComponentSprite::TestViewport(const CRECT &viewport) const
	{
		const Action *pCurAction = m_pTmpAction == NULL ? m_pDefaultAction : m_pTmpAction;
		if( NULL == pCurAction ) 
			return false;
		if (IsRectCross(pCurAction->GetBoundingBox(GetDirection()) + GetLocation(), viewport))
			return true;
		CPOINT location = GetTitleLocation();
		CRECT titleRect;
		titleRect.left = location.x - m_nTitleWidth / 2;
		titleRect.right = titleRect.left + m_nTitleWidth;
		titleRect.bottom = titleRect.top = location.y;
		if (m_titleAlign == XPTA_CENTER_BOTTOM)
		{
			titleRect.top -= m_nTitleHeight;
		} else {
			titleRect.bottom += m_nTitleHeight;
		}
		return IsRectCross(titleRect, viewport);
	}

	bool ComponentSprite::SetComponent(int sc, const std::wstring& resource, XPCOLOR color/*XRGB*/)
	{
		if( sc < 0 || sc >= m_nLayerCount ) return false;
		SpriteManager *pSprMan = m_pEngine->GetSpriteManager();
		Component *equip = pSprMan->GetComponent(m_ModelName, sc, resource);
		if ((equip == NULL) && resource.size() > 0)
			return false;
		ActionNameSet::iterator it = m_holdActions.begin(), ie = m_holdActions.end();
		ActionNameSet actions;
		const Action* act = NULL;
		for (;it!=ie;++it)
		{
			if (m_CurComponents[sc])
				m_CurComponents[sc]->ReleaseAction(*it);
			if (GetActionNames(*it, actions, act) && act->TestEquip(sc) && equip)
			{
				equip->HoldAction(m_ModelName, *it, actions);
			}
			
		}

		if (equip)
		{
			if (m_strDefaultActionOutsideName.size() > 0)
			{
				if (GetActionNames(m_strDefaultActionOutsideName, actions, act) && act->TestEquip(sc))
				{
					if (equip->PrefetchAction(m_ModelName, m_strDefaultActionOutsideName, actions) != XPPR_OK)
					{
						SetPropFlag(XPSPPF_HAS_DEF_ASYNC_LOADING, true);
					}
				}
			}

			if (m_strTmpActionOutsideName.size() > 0)
			{
				if (GetActionNames(m_strTmpActionOutsideName, actions, act) && act->TestEquip(sc))
				{
					if (!equip->PrefetchAction(m_ModelName, m_strTmpActionOutsideName, actions) != XPPR_OK)
					{
						SetPropFlag(XPSPPF_HAS_CUR_ASYNC_LOADING, true);
					}
				}
			}
		}

		m_CurComponents[sc] = equip;
		m_Colors[sc] = color;
		m_Colors[sc].a = 255;
		ModelSoundParam &param = pSprMan->GetSoundParam(m_ModelName);
		if (param.keyComponentID == sc)
		{
			m_pActionMap = param.soundmap[resource];
		}
		return true;
	}

	bool ComponentSprite::GetActionNames(const std::wstring &action_name, ActionNameSet &actions, const Action* &pAct)
	{
		actions.clear();
		SpriteManager *pSprMan = m_pEngine->GetSpriteManager();
		const std::wstring &actionref_name = pSprMan->GetActionRef(m_ModelName, action_name);
		if( actionref_name == L"" ) return false;
		pAct = pSprMan->GetAction(m_ModelName, actionref_name);
		if( pAct == NULL ) return false;
		if (pAct->IsInd())
		{
			int framecount = pAct->GetFrameCount();
			for (int frame=0;frame<framecount;++frame)
			{
				actions.insert(pAct->GetLinkName(frame));
			}
		} else {
			actions.insert(actionref_name);
		}
		return true;
	}

	// 预取该精灵的动作资源。如果该资源在GC时间范围（120秒）内没有被使用或者被重新（或别的精灵）预取，那么会自动释放
	XP_PREFETCH_RESULT ComponentSprite::PrefetchAction(const std::wstring &action_name)
	{
		const Action *act = NULL;
		ActionNameSet actions;
		if (!GetActionNames(action_name, actions, act))
			return XPPR_ASYNC_LOADING;
		XPASSERT(act);//act为NULL就不可能GetActionNames返回true
		XP_PREFETCH_RESULT result = XPPR_OK, tmpResult;
		for(int i=0; i< m_nLayerCount; ++i)
		{
			if( NULL == m_CurComponents[i] ) continue;
			if( !act->TestEquip(i)) continue;
			tmpResult = m_CurComponents[i]->PrefetchAction(m_ModelName, action_name, actions);
			if (tmpResult > result)
			{
				result = tmpResult;
			}
		}
		return result;
	}

	// 拿住该精灵的动作资源，如果不调用ReleaseAction，那么就永远不释放，直到精灵换模型或者被销毁
	void ComponentSprite::HoldAction(const std::wstring &action_name)
	{
		if (m_holdActions.find(action_name) != m_holdActions.end())
			return;
		const Action *act = NULL;
		ActionNameSet actions;
		if (!GetActionNames(action_name, actions, act))
			return;
		XPASSERT(act);//act为NULL就不可能GetActionNames返回true
		for(int i=0; i< m_nLayerCount; ++i)
		{
			if( NULL == m_CurComponents[i] ) continue;
			if( !act->TestEquip(i)) continue;
			m_CurComponents[i]->HoldAction(m_ModelName, action_name, actions);
		}
		m_holdActions.insert(action_name);
	}

	// 只有Hold住的Action才需要Release
	void ComponentSprite::ReleaseAction(const std::wstring &action_name)
	{
		if (m_holdActions.find(action_name) == m_holdActions.end())
			return;
		SpriteManager *pSprMan = m_pEngine->GetSpriteManager();
		std::wstring actionref_name = pSprMan->GetActionRef(m_ModelName, action_name);
		if( actionref_name == L"" ) 
			return;
		const Action *act = pSprMan->GetAction(m_ModelName, actionref_name);
		if( act == NULL ) 
			return;
		for(int i=0; i< m_nLayerCount; ++i)
		{
			if( NULL == m_CurComponents[i] ) continue;
			if( !act->TestEquip(i)) continue;
			m_CurComponents[i]->ReleaseAction(action_name);
		}
		m_holdActions.erase(action_name);
	}

	bool ComponentSprite::SetDefaultAction( const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float freq, bool bHoldLastFrame)
	{
		std::wstring actionref_name = m_pEngine->GetSpriteManager()->GetActionRef(m_ModelName, action_name);
		if( actionref_name == L"" ) 
			return false;
		const Action *act = m_pEngine->GetSpriteManager()->GetAction(m_ModelName, actionref_name);
		if( act == NULL ) 
			return false;
		m_strDefaultActionOutsideName = action_name;
		SetPropFlag(XPSPPF_HOLD_LAST_FRAME, bHoldLastFrame);
		SetPropFlag(XPSPPF_DEF_ACTION_ASYNC, type != XPSALT_SYNC);
		SetPropFlag(XPSPPF_START_HOLD_LAST_FRAME, false);
		m_strDefaultActionName = actionref_name;
		m_iAniStartTime = m_pEngine->GetTick();
		m_iAniDurationTime = (int)(act->GetTime() * freq);
		m_iDefaultAniDurationTime = m_iAniDurationTime;
		m_iCurFrame = 0;
		m_nOldFrame = -1;
		m_pDefaultAction = act;
		m_defaultActionTypeFlag = m_pEngine->GetSpriteManager()->actionType.GetActionType(action_name);
		SetPropFlag(XPSPPF_HAS_DEF_ASYNC_LOADING, PrefetchAction(action_name) != XPPR_OK);
		return true;
	}

	void ComponentSprite::PlayActionSound(const std::wstring &action_name)
	{
		if (m_pActionMap)
		{
			DirectSoundManager *pDSM = m_pEngine->GetSoundManager();
			if (!pDSM)
				return;
			SpriteSoundParamVet &vet = (*m_pActionMap)[action_name];
			if (vet.size() > 0)
			{
				SpriteSoundParam &param = vet[RangeRand((int)0, (int)(vet.size() - 1))];
				pDSM->XPPlaySound(param.soundname, m_nSoundPriority, 
					m_pEngine->GetSpriteManager()->GetActionSoundType(action_name), param.vol, param.freq, false);
			}
		}
	}

	bool ComponentSprite::PlayAction(const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float freq)
	{
		if( action_name == L"" )
		{
			XP_PREFETCH_RESULT result = PrefetchAction(m_strDefaultActionOutsideName);
			if (result == XPPR_OK || (result == XPPR_BASE_ASYNC_OK && type == XPSALT_BASE_ASYNC) || (type == XPSALT_SYNC))
			{
				if( m_strTmpActionName != L"" )
				{
					m_pTmpAction = NULL;
					m_strTmpActionName.clear();
					m_strTmpActionOutsideName.clear();
					m_iAniStartTime = m_pEngine->GetTick();
					m_iCurFrame = 0;
					m_nOldFrame = -1;
					m_iAniDurationTime = m_iDefaultAniDurationTime;
				}
				return true;
			}
			return false;
		}

		SetPropFlag(XPSPPF_CUR_ACTION_ASYNC, type != XPSALT_SYNC);
		/*SetPropFlag(XPSPPF_CUR_ACTION_BASEASYNC, type == XPSALT_BASE_ASYNC);*/
		if (type != XPSALT_SYNC)//异步
		{
			XP_PREFETCH_RESULT result = PrefetchAction(action_name);
			SetPropFlag(XPSPPF_HAS_CUR_ASYNC_LOADING, result != XPPR_OK);
			if (result != XPPR_OK && (result == XPPR_ASYNC_LOADING || type == XPSALT_ASYNC))//资源没有加载好
			{
				if( m_strTmpActionName != L"" )//如果一个动作不是Default，那么继续播上一个动作
				{
					PlayActionSound(m_strTmpActionOutsideName);
					m_iAniStartTime = m_pEngine->GetTick();
					m_iCurFrame = 0;
					m_nOldFrame = -1;
				}
				return false;
			}
		}
		SpriteManager *pSprMan = m_pEngine->GetSpriteManager();
		std::wstring actionref_name = pSprMan->GetActionRef(m_ModelName, action_name);
		if( actionref_name == L"" ) return false;
		const Action *act = pSprMan->GetAction(m_ModelName, actionref_name);
		if( act == NULL ) return false;
		
		PlayActionSound(action_name);
		m_actionTypeFlag = pSprMan->actionType.GetActionType(action_name);
		SetPropFlag(XPSPPF_START_HOLD_LAST_FRAME, false);
		m_strTmpActionOutsideName = action_name;
		m_strTmpActionName = actionref_name;
		m_iAniStartTime = m_pEngine->GetTick();
		m_iAniDurationTime = (int)(act->GetTime() * freq);
		m_iCurFrame = 0;
		m_nOldFrame = -1;
		m_pTmpAction = act;
		return true;
	}

	bool ComponentSprite::TestPoint(const Location &testloc, int r, float testval)
	{
		if (m_mouseTestMode == XPTM_PIC_RECT)
			return true;

		XPASSERT(r <= 2);
		if (TestTitleAlpha(testloc, r))
			return true;
		const Action *pCurAction = m_pTmpAction == NULL ? m_pDefaultAction : m_pTmpAction;
		if( NULL == pCurAction ) return false;

		if( !pCurAction->TestDir(GetDirection()) ) return false;

		std::vector<int> ecorder(m_nLayerCount);

		pCurAction->GetFrameOrder(m_iCurFrame, GetDirection(), &ecorder[0], m_nLayerCount);
		std::wstring curActionName = pCurAction->GetLinkName(m_iCurFrame);
		if( curActionName == L"" )
			curActionName = m_pTmpAction == NULL ? m_strDefaultActionName : m_strTmpActionName;
		int linkframe = pCurAction->GetLineFrame(m_iCurFrame);
		CPOINT pt = testloc - GetLocation();
		pt *= m_pEngine->GetWorldScale() / m_fScale;
		for(int i=0; i< (int)m_CurComponents.size(); ++i)
		{
			int ec = ecorder[i];
			if( ec < 0 || ec >= m_nLayerCount ) continue;
			if( NULL == m_CurComponents[ec] ) continue;
			if( !pCurAction->TestEquip(ec)) continue;
			if ((1 << ec) & m_FlagEffectLayer) continue;//特效层不判断了

			XHardRef<XAni> ani = m_CurComponents[ec]->GetAni(m_ModelName, curActionName, true);
			if( ani.IsValid() && ani->GetState() > XAni::XPANISTATE_NOPICTURE)
			{
				if (ani->TestPoint(GetDirection(), linkframe, pt, m_mouseTestMode, m_pEngine->GetRenderer(), r, testval))
					return true;
			}
		}
		return false;
	}

	void ComponentSprite::UpdateAction()
	{
		if (!TestDefaultAction())
			return;
		if ( !TestPropFlag(XPSPPF_IS_IN_TELEPORT) )
		{
			m_blurList.clear();
		}


		int d = m_pEngine->GetTick() - m_iAniStartTime;
		if( d > m_iAniDurationTime )
		{
			m_iCurFrame = 0;
			m_iAniStartTime = m_pEngine->GetTick();
			m_nOldFrame = -1;
			if( m_pTmpAction != NULL )
			{
				m_pTmpAction = NULL;
				m_strTmpActionName.clear();
				m_strTmpActionOutsideName.clear();
				m_iAniDurationTime = m_iDefaultAniDurationTime;
				m_pEngine->GetApp()->OnUpdateSpriteAction(this, XPUSAT_PLAYACTIONFINISH);
			}
			else if( TestPropFlag(XPSPPF_HOLD_LAST_FRAME) )
				SetPropFlag(XPSPPF_START_HOLD_LAST_FRAME, true);
			else if (m_pEngine->GetApp())
				m_pEngine->GetApp()->OnUpdateSpriteAction(this, XPUSAT_DEFAULTACTIONFINISH);
		}
		const Action *pCurAction = m_pTmpAction == NULL ? m_pDefaultAction : m_pTmpAction;
		d = m_pEngine->GetTick() - m_iAniStartTime;//这里必须再算一次，因为上面的回调可能重新PlayAction了
		float r = d / (float)m_iAniDurationTime;
		int i;
		int n =  pCurAction->GetFrameCount();
		for(i=n-1; i>=0;--i)
		{
			if( r > pCurAction->GetFrameStartTime(i) )
			{
				break;
			}
		}

		if( TestPropFlag(XPSPPF_START_HOLD_LAST_FRAME) )
			m_iCurFrame = n-1;
		else
			m_iCurFrame = i>0?i:0;
		if (m_nOldFrame != m_iCurFrame)
		{
			if (m_iCurFrame == 0 && m_pTmpAction == NULL)//default action的第一帧
			{             
				PlayActionSound(m_strDefaultActionOutsideName);
			}
			FrameNotifyMap::iterator m_fnmIter = m_mapFrameNotifys.find(m_pTmpAction == NULL ? m_strDefaultActionOutsideName : m_strTmpActionOutsideName);
			if (m_fnmIter != m_mapFrameNotifys.end())
			{
				ActNotifyMap &anm = m_fnmIter->second;
				ActNotifyMap::iterator it = anm.find(m_iCurFrame);
				if (it != anm.end())
				{
					SpriteNotifySet::iterator snsIt = it->second.begin(), snsIe = it->second.end();
					for (;snsIt!=snsIe;++snsIt)
					{
						(*snsIt)->OnPlayFrame(this, m_strTmpActionOutsideName, m_iCurFrame);
					}
				}
			}
			m_nOldFrame = m_iCurFrame;
		}
		
	}

	//某个部件的框的top或者bottom和分割线（包括水面分割线和过渡分割线）相交情况：
	//1 在水面之上
	//2 和水面相交
	//3 在水面和过渡之间
	//4 和过渡线相交
	//5 在过渡线之下
	//6 同时和两条线相交
	//完全在水上或者完全在过渡线之下的不用在这里处理
	enum CROSS_TYPE
	{
		ABOVE_WATER = 1,
		CROSS_WATER,
		BETWEEN_WATER_AND_HALF,
		CROSS_HALF,
		UNDER_HALF,
		CROSS_BOTH,
	};

	union LAYER_WATER_TYPE
	{
		unsigned char data;
		struct
		{
			unsigned char topType:4;	//top的相交情况
			unsigned char bottomType:4;	//bottom的相交情况
		};
	};

	inline float GetU(const FRECT& rect, const FRECT &srcrect, float x)
	{
		return srcrect.Width() * (x - rect.left) / rect.Width() + srcrect.left;
	}

	inline float GetV(const FRECT& rect, const FRECT &srcrect, float y)
	{
		return srcrect.Height() * (y - rect.top) / rect.Height() + srcrect.top;
	}

	inline float GetCrossPosX(float leftWaterPosX, float leftWaterPosY, float posy, bool leftIsSmall)
	{
		return (leftIsSmall?1:-1) * TAN_THETA * (posy - leftWaterPosY) + leftWaterPosX;
	}

	struct TctParam
	{
		char type;
		TCT1VERTEX* pTct;
		float pWaterPos[2];	//左边是0右边是1，这个是水面的位置（屏幕y坐标）
		float pHalfWaterPos[2];	//这个是过渡的位置（屏幕y坐标）
		float posY;
		float alphaHeight;
		int nSmall;
		int nBig;
		float fAlpha;
	};

	inline void GetTct(const TctParam &tctParam, int &pointIndex, const FRECT &rect, const FRECT &srcrect, XPCOLOR color)
	{
		float baseAlpha = tctParam.fAlpha*255;
		switch (tctParam.type)
		{
		case ABOVE_WATER:
			XPASSERT(fabs(tctParam.posY - rect.top)<0.001);//如果posY不是top，那么不会走这里
			color.a = static_cast<unsigned char>(baseAlpha);
			tctParam.pTct[pointIndex-1].c = tctParam.pTct[pointIndex].c = color.data;
			break;
		case CROSS_WATER:
			color.a = static_cast<unsigned char>(baseAlpha);
			tctParam.pTct[pointIndex].c = tctParam.pTct[pointIndex+1].c = color.data;
			color.a = static_cast<unsigned char>(Lerpf(baseAlpha, UNDER_WATER_ALPHA, (tctParam.posY - tctParam.pWaterPos[tctParam.nSmall]) / tctParam.alphaHeight) * tctParam.fAlpha);
			tctParam.pTct[pointIndex-1].c = color.data;
			tctParam.pTct[pointIndex].x = GetCrossPosX(rect.left, tctParam.pWaterPos[0], tctParam.posY, 0 == tctParam.nSmall);
			tctParam.pTct[pointIndex].u = GetU(rect, srcrect, tctParam.pTct[pointIndex].x);
			++pointIndex;
			break;
		case BETWEEN_WATER_AND_HALF:
			color.a = static_cast<unsigned char>(Lerpf(baseAlpha, UNDER_WATER_ALPHA, (tctParam.posY - tctParam.pWaterPos[tctParam.nSmall]) / tctParam.alphaHeight) * tctParam.fAlpha);
			tctParam.pTct[pointIndex-1].c = color.data;
			color.a = static_cast<unsigned char>(Lerpf(baseAlpha, UNDER_WATER_ALPHA, (tctParam.posY - tctParam.pWaterPos[tctParam.nBig]) / tctParam.alphaHeight) * tctParam.fAlpha);
			tctParam.pTct[pointIndex].c = color.data;
			break;
		case CROSS_HALF:
			color.a = static_cast<unsigned char>(UNDER_WATER_ALPHA * tctParam.fAlpha);
			tctParam.pTct[pointIndex-1].c = tctParam.pTct[pointIndex].c = color.data;
			tctParam.pTct[pointIndex].x = GetCrossPosX(rect.left, tctParam.pHalfWaterPos[0], tctParam.posY, 0 == tctParam.nSmall);
			tctParam.pTct[pointIndex].u = GetU(rect, srcrect, tctParam.pTct[pointIndex].x);
			color.a = static_cast<unsigned char>(Lerpf(baseAlpha, UNDER_WATER_ALPHA, (tctParam.posY - tctParam.pWaterPos[tctParam.nBig]) / tctParam.alphaHeight) * tctParam.fAlpha);
			++pointIndex;
			tctParam.pTct[pointIndex].c = color.data;
			break;
		case CROSS_BOTH:
			color.a = static_cast<unsigned char>(UNDER_WATER_ALPHA * tctParam.fAlpha);
			tctParam.pTct[pointIndex-1].c = tctParam.pTct[pointIndex].c = color.data;//20%
			tctParam.pTct[pointIndex].x = GetCrossPosX(rect.left, tctParam.pHalfWaterPos[0], tctParam.posY, 0 == tctParam.nSmall);
			tctParam.pTct[pointIndex].u = GetU(rect, srcrect, tctParam.pTct[pointIndex].x);
			++pointIndex;
			color.a = static_cast<unsigned char>(baseAlpha);
			tctParam.pTct[pointIndex].c = tctParam.pTct[pointIndex+1].c = color.data;//100%
			tctParam.pTct[pointIndex].x = GetCrossPosX(rect.left, tctParam.pWaterPos[0], tctParam.posY, 0 == tctParam.nSmall);
			tctParam.pTct[pointIndex].u = GetU(rect, srcrect, tctParam.pTct[pointIndex].x);
			++pointIndex;
			break;
		case UNDER_HALF:
			XPASSERT(fabs(tctParam.posY - rect.bottom)<0.001);//如果posY不是bottom，那么不会走这里
			color.a = static_cast<unsigned char>(UNDER_WATER_ALPHA * tctParam.fAlpha);
			tctParam.pTct[pointIndex-1].c = tctParam.pTct[pointIndex].c = color.data;
			break;
		default:
			XPASSERT(false);
			break;
		}
	}
	inline CROSS_TYPE GetWaterCrossType(float smallWater, float bigWater, float smallHalf, float bigHalf, float pos)
	{
		XPASSERT(smallWater <= bigWater);
		XPASSERT(smallHalf <= bigHalf);
		XPASSERT(smallWater < smallHalf);
		XPASSERT(bigWater < bigHalf);
		if (pos <= smallWater)//小于等于smallWater就肯定小于等于bigWater
			return ABOVE_WATER;
		if (pos > smallWater && pos < bigWater)
		{
			if (pos > smallHalf)//小于bigWater就肯定小于bigHalf
				return CROSS_BOTH;
			return CROSS_WATER;
		}
		if (pos >= bigHalf)	//大于等于bigHalf就肯定大于等于smallHalf
			return UNDER_HALF;

		if (pos > smallHalf)	//来到这里，肯定不是大于等于bigHalf，如果还大于smallHalf，那就是相交了
			return CROSS_HALF;

		return BETWEEN_WATER_AND_HALF;
	}

	int ComponentSprite::GetTotalFrame() const
	{
		if (!TestDefaultAction())
			return 0;
		return ((m_pTmpAction==NULL)?m_pDefaultAction:m_pTmpAction)->GetFrameCount();
	}

	void ComponentSprite::ResetHorseHeight(const std::wstring& ridename)
	{ 
		m_nHorseHeight = m_pEngine->GetSpriteManager()->GetHorseHeight(m_ModelName, ridename);
	}

	void ComponentSprite::RenderALayer(const LayerParam& layerParam)
	{
		XHardRef<XAni> ani = m_CurComponents[layerParam.ec]->GetAni(m_ModelName, layerParam.curActionName, !layerParam.bSync);
		if( ani.IsValid() )
		{
			ani.SetTick(m_pEngine->GetTick());
			switch (ani->GetState())
			{
			case XAni::XPANISTATE_NOPICTURE:
			case XAni::XPANISTATE_NO_XAP:
				if (!layerParam.bSync)
					return;
				m_pEngine->GetAniManager()->SyncLoadAni(ani);
				break;
			case XAni::XPANISTATE_NULL:
				return;
				break;
			case XAni::XPANISTATE_BASE_PIC:
				if (layerParam.bSync)
					m_pEngine->GetAniManager()->SyncLoadAni(ani);
				break;
			}
			DrawPictureParam param;
			param.bSpecAlpha = layerParam.bHasRenderTarget;
			XAni::XPic pic;
			XPCOLOR color;
			param.pMatrix = layerParam.pMatrix;
			Renderer* pRenderer = m_pEngine->GetRenderer();
			unsigned char defAlpha = (layerParam.bHasRenderTarget?255:m_nAlpha);
			for(int k=0; k<ani->GetRegionCount();++k)
			{
				if( ani->GetPic(layerParam.dir, layerParam.linkframe, k, pic) && pic.pBigPic && pic.pBigPic->handle != INVALID_PICTURE_HANDLE )
				{
					color = ((k==ani->GetRegionCount()-1)?m_Colors[layerParam.ec]:0xffffffff);
					FRECT rect(pic.offset.ToFRECT());
					if (layerParam.bIsEffectlayer)
					{
						rect *= XP_EFFECT_SCALE * m_fScale;
						param.blend = XPTEXBM_ADDITIVE_ALPHA;
						color.a = static_cast<unsigned char>(defAlpha*m_fEffectAlpha);
					}
					else
					{
						rect *= m_fScale;
						param.blend = layerParam.bHasRenderTarget?XPTEXBM_DEFAULT:(m_bIsHighlight?XPTEXBM_MODULATE2X:XPTEXBM_DEFAULT);
						color.a = defAlpha;
					}
					rect += layerParam.location;
					if ((m_nHorseHeight != 0) && TestPropFlag(XPSPPF_IS_READING))
					{
						if (!((1 << layerParam.ec) & m_flagRideLayer))//如果不是马
						{
							rect.top -= m_nHorseHeight*m_fScale;
							rect.bottom -= m_nHorseHeight*m_fScale;
						}
					}

					if( (ani->GetDirMode() == XPANIDM_8FROM5  &&  layerParam.dir >  XPDIR_BOTTOM) ||
						(ani->GetDirMode() == XPANIDM_B2FROM1 && layerParam.dir == XPDIR_BOTTOMLEFT) ||
						(ani->GetDirMode() == XPANIDM_4FROM2  &&  layerParam.dir == XPDIR_BOTTOMLEFT) ||
						(ani->GetDirMode() == XPANIDM_4FROM2  &&  layerParam.dir == XPDIR_TOPRIGHT)
					   )
					{
						rect.left = 2*(layerParam.location.x) - rect.left;
						rect.right = 2*(layerParam.location.x) - rect.right;
					}

					m_MaxRectInTex.left = min(m_MaxRectInTex.left, rect.left);
					m_MaxRectInTex.right = max(m_MaxRectInTex.right, rect.right);
					m_MaxRectInTex.top = min(m_MaxRectInTex.top, rect.top);
					m_MaxRectInTex.bottom = max(m_MaxRectInTex.bottom, rect.bottom);

					param.handle = pic.pBigPic->handle;
					param.pRect = &rect;
					param.pSrcrect = &pic.srcrect;
					static TctParam tctParam;
					tctParam.nSmall = 0;
					tctParam.nBig = 1;
					tctParam.alphaHeight = layerParam.waterdepth / 2;
					bool onceDraw = false;
					if (layerParam.waterdepth >= 1)
					{
						tctParam.type = 0;//0 -- 1 \  2 /
						if (m_surfaceType != WST_ALWAYS_PLANE)
						{
							switch (layerParam.dir)
							{
							case XPDIR_TOPLEFT:
							case XPDIR_BOTTOMRIGHT:
								tctParam.type = 1;
								break;
							case XPDIR_TOPRIGHT:
							case XPDIR_BOTTOMLEFT:
								tctParam.type = 2;
								break;
							case XPDIR_TOP:
							case XPDIR_BOTTOM:
							case XPDIR_LEFT:
							case XPDIR_RIGHT:
								//nothing
								break;
							default:
								XPASSERT(false);
							}
							if ((m_surfaceType == WST_ATHWART_DIR) && (!TestPropFlag(XPSPPF_IS_READING)))
							{
								//反过来的时候，并且不是骑乘动作
								tctParam.type = 3 - tctParam.type;//1变成2，2变成1
							}
						}

						switch(tctParam.type)
						{
						case 0:
						case 3:
							tctParam.pWaterPos[0] = tctParam.pWaterPos[1] = layerParam.water;
							tctParam.pHalfWaterPos[0] = tctParam.pHalfWaterPos[1] = layerParam.water + tctParam.alphaHeight;
							break;
						case 1:
							tctParam.pWaterPos[0] = layerParam.water - (layerParam.location.x - rect.left) / TAN_THETA;  //宽高比是1:2
							tctParam.pWaterPos[1] = layerParam.water - (layerParam.location.x - rect.right) / TAN_THETA;  //宽高比是1:2
							tctParam.pHalfWaterPos[0] = tctParam.pWaterPos[0] + tctParam.alphaHeight;
							tctParam.pHalfWaterPos[1] = tctParam.pWaterPos[1] + tctParam.alphaHeight;
							break;
						case 2:
							tctParam.pWaterPos[0] = layerParam.water + (layerParam.location.x - rect.left) / TAN_THETA;  //宽高比是1:2
							tctParam.pWaterPos[1] = layerParam.water + (layerParam.location.x - rect.right) / TAN_THETA;  //宽高比是1:2
							tctParam.pHalfWaterPos[0] = tctParam.pWaterPos[0] + tctParam.alphaHeight;
							tctParam.pHalfWaterPos[1] = tctParam.pWaterPos[1] + tctParam.alphaHeight;
							tctParam.nBig = 0;
							tctParam.nSmall = 1;

							break;
						default:
							XPASSERT(false);
						}
						if ((rect.bottom <= tctParam.pWaterPos[0] && rect.bottom <= tctParam.pWaterPos[1]))//全在水面上面
						{
							onceDraw = true;
						} else if (rect.top >= tctParam.pHalfWaterPos[0] && rect.top >= tctParam.pHalfWaterPos[1])//全在水下
						{
							return;
						}
						if (layerParam.bIsShadow)
							color = SHADOW_COLOR_IN_WATER;
					}
					else
					{	//没有水
						onceDraw = true;
						if (layerParam.bIsShadow)
							color = SHADOW_COLOR_ON_LAND;
					}
					if (onceDraw)
					{
						param.pColor = &color;
						param.colorCount = 1;
						pRenderer->DrawPicture(param);
					}
					else
					{
						RendererStateAutoRecover stateAutoRecover(pRenderer);

						pRenderer->SetSeparateAlphaBlend(true);
						pRenderer->SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
						pRenderer->SetAlphaBlendDstType(XPBLEND_INVSRCALPHA);
						pRenderer->SetTextureColorBlendType(XPTOP_DEFAULT);

						float theX[2];			//theX[0]是left，theX[1]是right
						//所以，以后计算的时候，都按照斜率是负的来处理，theX[nSmall]
						float theU[2];			//按照theX来处理
						static TCT1VERTEX tct1[12];
						static WORD indexBuffer[30];
						tctParam.pTct = tct1;
						LAYER_WATER_TYPE type;
						rect.left -= 0.5f;
						rect.top -= 0.5f;
						rect.right -= 0.5f;
						rect.bottom -= 0.5f;
						tctParam.pWaterPos[tctParam.nSmall] -= 0.5f;
						tctParam.pWaterPos[tctParam.nBig] -= 0.5f;
						tctParam.pHalfWaterPos[tctParam.nSmall] -= 0.5f;
						tctParam.pHalfWaterPos[tctParam.nBig] -= 0.5f;
						theX[0] = rect.left;
						theX[1] = rect.right;
						theU[0] = pic.srcrect.left;
						theU[1] = pic.srcrect.right;
						type.topType = GetWaterCrossType(tctParam.pWaterPos[tctParam.nSmall], tctParam.pWaterPos[tctParam.nBig], tctParam.pHalfWaterPos[tctParam.nSmall], tctParam.pHalfWaterPos[tctParam.nBig], rect.top);
						type.bottomType = GetWaterCrossType(tctParam.pWaterPos[tctParam.nSmall], tctParam.pWaterPos[tctParam.nBig], tctParam.pHalfWaterPos[tctParam.nSmall], tctParam.pHalfWaterPos[tctParam.nBig], rect.bottom);
						int pointIndex = 0;
						int indexCount = 0;

						tct1[pointIndex].x = theX[tctParam.nSmall];
						tct1[pointIndex].y = rect.top;
						tct1[pointIndex].z = Renderer::Z;
						tct1[pointIndex].u = theU[tctParam.nSmall];
						tct1[pointIndex].v = pic.srcrect.top;
						tct1[pointIndex+3] = tct1[pointIndex+2] = tct1[pointIndex+1] = tct1[pointIndex];
						++pointIndex;
						tctParam.posY = rect.top;
						tctParam.fAlpha = color.a / 255.0f;
						tctParam.type = type.topType;
						GetTct(tctParam, pointIndex, rect, pic.srcrect, color);//最后一个点，颜色已经算好了
						tct1[pointIndex].x = theX[tctParam.nBig];
						tct1[pointIndex].u = theU[tctParam.nBig];
						++pointIndex;

						//bottom第一个点
						tct1[pointIndex] = tct1[0];
						tct1[pointIndex].y = rect.bottom;
						tct1[pointIndex].v = pic.srcrect.bottom;
						tct1[pointIndex+3] = tct1[pointIndex+2] = tct1[pointIndex+1] = tct1[pointIndex];
						++pointIndex;
						tctParam.posY = rect.bottom;
						tctParam.type = type.bottomType;
						GetTct(tctParam, pointIndex, rect, pic.srcrect, color);//最后一个点，颜色已经算好了
						//最后一个点，颜色已经算好了
						tct1[pointIndex].x = theX[tctParam.nBig];
						tct1[pointIndex].u = theU[tctParam.nBig];
						++pointIndex;

						//水面和矩形左右的交点
						tct1[pointIndex] = tct1[0];
						tct1[pointIndex].y = tctParam.pWaterPos[tctParam.nSmall];
						tct1[pointIndex].v = GetV(rect, pic.srcrect, tct1[pointIndex].y);
						tct1[pointIndex].c = color.data;
						++pointIndex;
						tct1[pointIndex] = tct1[pointIndex-2];
						tct1[pointIndex].y = tctParam.pWaterPos[tctParam.nBig];
						tct1[pointIndex].v = GetV(rect, pic.srcrect, tct1[pointIndex].y);
						tct1[pointIndex].c = color.data;
						++pointIndex;

						color.a = static_cast<unsigned char>(UNDER_WATER_ALPHA);
						tct1[pointIndex] = tct1[0];
						tct1[pointIndex].y = tctParam.pHalfWaterPos[tctParam.nSmall];
						tct1[pointIndex].v = GetV(rect, pic.srcrect, tct1[pointIndex].y);
						tct1[pointIndex].c = color.data;
						++pointIndex;
						tct1[pointIndex] = tct1[pointIndex-2];
						tct1[pointIndex].y = tctParam.pHalfWaterPos[tctParam.nBig];
						tct1[pointIndex].v = GetV(rect, pic.srcrect, tct1[pointIndex].y);
						tct1[pointIndex].c = color.data;
						++pointIndex;
						char a = type.data;

						//////////////////////////////////////
						//   0x21     0x31    0x41    0x51    0x61    topType  ABOVE_WATER
						//   0x22     0x32    0x42    0x52    0x62    topType  CROSS_WATER
						//            0x33    0x43    0x53            topType  BETWEEN_WATER_AND_HALF
						//                    0x44    0x54            topType  CROSS_HALF
						//                    0x46    0x56    0x66    topType  CROSS_BOTH
						// CROSS_W  B_W_A_H  CROSS_H UNDER_H CROSS_BOTH
						//
						//先不考虑背面剪裁
						//XPTRACE(L"%x  %s\n", type.data, m_pEB->GetSpriteManager()->GetLayerDes(m_modelname, ec).c_str());
						if (layerParam.bIsShadow)	//在水里面画影子的话，不用画水下部分
						{
							switch (type.data)
							{
							case 0x21:
								indexBuffer[0]=0;indexBuffer[1]=1;indexBuffer[2]=4;
								indexBuffer[3]=0;indexBuffer[4]=4;indexBuffer[5]=3;
								indexBuffer[6]=0;indexBuffer[7]=3;indexBuffer[8]=5;
								indexCount = 9;
								pointIndex = 6;
								break;
							case 0x31:
								indexBuffer[0]=0;indexBuffer[1]=1;indexBuffer[2]=5;
								indexBuffer[3]=0;indexBuffer[4]=5;indexBuffer[5]=4;
								indexCount = 6;
								pointIndex = 6;
								break;
							case 0x41:
								indexBuffer[0]=0;indexBuffer[1]=1;indexBuffer[2]=6;
								indexBuffer[3]=0;indexBuffer[4]=6;indexBuffer[5]=5;
								indexCount = 6;
								pointIndex = 7;
								break;
							case 0x51:
								indexBuffer[0]=0;indexBuffer[1]=1;indexBuffer[2]=5;
								indexBuffer[3]=0;indexBuffer[4]=5;indexBuffer[5]=4;
								indexCount = 6;
								pointIndex = 6;
								break;
							case 0x61:
								indexBuffer[0]=0;indexBuffer[1]=1;indexBuffer[2]=5;
								indexBuffer[3]=0;indexBuffer[4]=5;indexBuffer[5]=4;
								indexBuffer[6]=0;indexBuffer[7]=4;indexBuffer[8]=6;
								indexCount = 9;
								pointIndex = 7;
								break;
							case 0x22:
								indexBuffer[0]=1;indexBuffer[1]=2;indexBuffer[2]=5;
								indexBuffer[3]=1;indexBuffer[4]=5;indexBuffer[5]=4;
								indexCount = 6;
								pointIndex = 6;
								break;
							case 0x32:
								indexBuffer[9]=1;indexBuffer[10]=2;indexBuffer[11]=6;
								indexCount = 3;
								pointIndex = 7;
								break;
							case 0x42:
								indexBuffer[0]=1;indexBuffer[1]=2;indexBuffer[2]=7;
								indexCount = 3;
								pointIndex = 8;
								break;
							case 0x52:
								indexBuffer[0]=1;indexBuffer[1]=2;indexBuffer[2]=6;
								indexCount = 3;
								pointIndex = 7;
								break;
							case 0x62:
								indexBuffer[0]=1;indexBuffer[1]=2;indexBuffer[2]=6;
								indexBuffer[3]=1;indexBuffer[4]=6;indexBuffer[5]=5;
								indexCount = 6;
								pointIndex = 7;
								break;
							case 0x46:
								indexBuffer[0]=2;indexBuffer[1]=3;indexBuffer[2]=8;
								indexCount = 3;
								pointIndex = 9;
								break;
							case 0x56:
								indexBuffer[0]=2;indexBuffer[1]=3;indexBuffer[2]=7;
								indexCount = 3;
								pointIndex = 8;
								break;
							case 0x66:
								indexBuffer[0]=2;indexBuffer[1]=3;indexBuffer[2]=7;
								indexBuffer[3]=2;indexBuffer[4]=7;indexBuffer[5]=6;
								indexCount = 6;
								pointIndex = 8;
								break;
							case 0x33:
							case 0x43:
							case 0x53:
							case 0x44:
							case 0x54:
								return;//全在水下，不用画
								break;
							}
						} else {
							switch (type.data)
							{
							case 0x21:
								indexBuffer[0]=0;indexBuffer[1]=1;indexBuffer[2]=4;
								indexBuffer[3]=0;indexBuffer[4]=4;indexBuffer[5]=3;
								indexBuffer[6]=0;indexBuffer[7]=3;indexBuffer[8]=5;
								indexBuffer[9]=5;indexBuffer[10]=3;indexBuffer[11]=2;
								indexCount = 12;
								pointIndex = 6;
								break;
							case 0x31:
								indexBuffer[0]=0;indexBuffer[1]=1;indexBuffer[2]=5;
								indexBuffer[3]=0;indexBuffer[4]=5;indexBuffer[5]=4;
								indexBuffer[6]=4;indexBuffer[7]=5;indexBuffer[8]=3;
								indexBuffer[9]=4;indexBuffer[10]=3;indexBuffer[11]=2;
								indexCount = 12;
								pointIndex = 6;
								break;
							case 0x41:
								indexBuffer[0]=0;indexBuffer[1]=1;indexBuffer[2]=6;
								indexBuffer[3]=0;indexBuffer[4]=6;indexBuffer[5]=5;
								indexBuffer[6]=5;indexBuffer[7]=6;indexBuffer[8]=4;
								indexBuffer[9]=5;indexBuffer[10]=4;indexBuffer[11]=3;
								indexBuffer[12]=5;indexBuffer[13]=3;indexBuffer[14]=7;
								indexBuffer[15]=7;indexBuffer[16]=3;indexBuffer[17]=2;
								indexCount = 18;
								pointIndex = 8;
								break;
							case 0x51:
								indexBuffer[0]=0;indexBuffer[1]=1;indexBuffer[2]=5;
								indexBuffer[3]=0;indexBuffer[4]=5;indexBuffer[5]=4;
								indexBuffer[6]=4;indexBuffer[7]=5;indexBuffer[8]=7;
								indexBuffer[9]=4;indexBuffer[10]=7;indexBuffer[11]=6;
								indexBuffer[12]=6;indexBuffer[13]=7;indexBuffer[14]=3;
								indexBuffer[15]=6;indexBuffer[16]=3;indexBuffer[17]=2;
								indexCount = 18;
								pointIndex = 8;
								break;
							case 0x61:
								indexBuffer[0]=0;indexBuffer[1]=1;indexBuffer[2]=5;
								indexBuffer[3]=0;indexBuffer[4]=5;indexBuffer[5]=4;
								indexBuffer[6]=0;indexBuffer[7]=4;indexBuffer[8]=6;
								indexBuffer[9]=6;indexBuffer[10]=4;indexBuffer[11]=3;
								indexBuffer[12]=6;indexBuffer[13]=3;indexBuffer[14]=8;
								indexBuffer[15]=8;indexBuffer[16]=3;indexBuffer[17]=2;
								indexCount = 18;
								pointIndex = 9;
								break;
							case 0x22:
								indexBuffer[0]=0;indexBuffer[1]=1;indexBuffer[2]=4;
								indexBuffer[3]=0;indexBuffer[4]=4;indexBuffer[5]=3;
								indexBuffer[6]=1;indexBuffer[7]=2;indexBuffer[8]=5;
								indexBuffer[9]=1;indexBuffer[10]=5;indexBuffer[11]=4;
								indexCount = 12;
								pointIndex = 6;
								break;
							case 0x32:
								indexBuffer[0]=0;indexBuffer[1]=1;indexBuffer[2]=6;
								indexBuffer[3]=0;indexBuffer[4]=6;indexBuffer[5]=4;
								indexBuffer[6]=0;indexBuffer[7]=4;indexBuffer[8]=3;
								indexBuffer[9]=1;indexBuffer[10]=2;indexBuffer[11]=6;
								indexCount = 12;
								pointIndex = 7;
								break;
							case 0x42:
								indexBuffer[0]=0;indexBuffer[1]=1;indexBuffer[2]=7;
								indexBuffer[3]=0;indexBuffer[4]=7;indexBuffer[5]=5;
								indexBuffer[6]=0;indexBuffer[7]=5;indexBuffer[8]=4;
								indexBuffer[9]=0;indexBuffer[10]=4;indexBuffer[11]=8;
								indexBuffer[12]=1;indexBuffer[13]=2;indexBuffer[14]=7;
								indexBuffer[15]=8;indexBuffer[16]=4;indexBuffer[17]=3;
								indexCount = 18;
								pointIndex = 9;
								break;
							case 0x52:
								indexBuffer[0]=0;indexBuffer[1]=1;indexBuffer[2]=6;
								indexBuffer[3]=0;indexBuffer[4]=6;indexBuffer[5]=8;
								indexBuffer[6]=0;indexBuffer[7]=8;indexBuffer[8]=7;
								indexBuffer[9]=1;indexBuffer[10]=2;indexBuffer[11]=6;
								indexBuffer[12]=7;indexBuffer[13]=8;indexBuffer[14]=4;
								indexBuffer[15]=7;indexBuffer[16]=4;indexBuffer[17]=3;
								indexCount = 18;
								pointIndex = 9;
								break;
							case 0x62:
								indexBuffer[0]=0;indexBuffer[1]=1;indexBuffer[2]=5;
								indexBuffer[3]=0;indexBuffer[4]=5;indexBuffer[5]=4;
								indexBuffer[6]=0;indexBuffer[7]=4;indexBuffer[8]=9;
								indexBuffer[9]=1;indexBuffer[10]=2;indexBuffer[11]=6;
								indexBuffer[12]=1;indexBuffer[13]=6;indexBuffer[14]=5;
								indexBuffer[15]=9;indexBuffer[16]=4;indexBuffer[17]=3;
								indexCount = 18;
								pointIndex = 10;
								break;
							case 0x33:
								indexBuffer[0]=0;indexBuffer[1]=1;indexBuffer[2]=3;
								indexBuffer[3]=0;indexBuffer[4]=3;indexBuffer[5]=2;
								indexCount = 6;
								pointIndex = 4;
								break;
							case 0x43:
								indexBuffer[0]=0;indexBuffer[1]=1;indexBuffer[2]=4;
								indexBuffer[3]=0;indexBuffer[4]=4;indexBuffer[5]=3;
								indexBuffer[6]=0;indexBuffer[7]=3;indexBuffer[8]=7;
								indexBuffer[9]=7;indexBuffer[10]=3;indexBuffer[11]=2;
								indexCount = 12;
								pointIndex = 8;
								break;
							case 0x53:
								indexBuffer[0]=0;indexBuffer[1]=1;indexBuffer[2]=7;
								indexBuffer[3]=0;indexBuffer[4]=7;indexBuffer[5]=6;
								indexBuffer[6]=6;indexBuffer[7]=7;indexBuffer[8]=3;
								indexBuffer[9]=6;indexBuffer[10]=3;indexBuffer[11]=2;
								indexCount = 12;
								pointIndex = 8;
								break;
							case 0x44:
								indexBuffer[0]=0;indexBuffer[1]=1;indexBuffer[2]=4;
								indexBuffer[3]=0;indexBuffer[4]=4;indexBuffer[5]=3;
								indexBuffer[6]=1;indexBuffer[7]=2;indexBuffer[8]=5;
								indexBuffer[9]=1;indexBuffer[10]=5;indexBuffer[11]=4;
								indexCount = 12;
								pointIndex = 6;
								break;
							case 0x54:
								indexBuffer[0]=0;indexBuffer[1]=1;indexBuffer[2]=8;
								indexBuffer[3]=0;indexBuffer[4]=8;indexBuffer[5]=4;
								indexBuffer[6]=0;indexBuffer[7]=4;indexBuffer[8]=3;
								indexBuffer[9]=1;indexBuffer[10]=2;indexBuffer[11]=8;
								indexCount = 12;
								pointIndex = 9;
								break;
							case 0x46:
								indexBuffer[0]=0;indexBuffer[1]=1;indexBuffer[2]=5;
								indexBuffer[3]=0;indexBuffer[4]=5;indexBuffer[5]=4;
								indexBuffer[6]=1;indexBuffer[7]=2;indexBuffer[8]=8;
								indexBuffer[9]=1;indexBuffer[10]=8;indexBuffer[11]=6;
								indexBuffer[12]=1;indexBuffer[13]=6;indexBuffer[14]=5;
								indexBuffer[15]=2;indexBuffer[16]=3;indexBuffer[17]=8;
								indexCount = 18;
								pointIndex = 9;
								break;
							case 0x56:
								indexBuffer[0]=0;indexBuffer[1]=1;indexBuffer[2]=9;
								indexBuffer[3]=0;indexBuffer[4]=9;indexBuffer[5]=5;
								indexBuffer[6]=0;indexBuffer[7]=5;indexBuffer[8]=4;
								indexBuffer[9]=1;indexBuffer[10]=2;indexBuffer[11]=7;
								indexBuffer[12]=1;indexBuffer[13]=7;indexBuffer[14]=9;
								indexBuffer[15]=2;indexBuffer[16]=3;indexBuffer[17]=7;
								indexCount = 18;
								pointIndex = 10;
								break;
							case 0x66:
								indexBuffer[0]=0;indexBuffer[1]=1;indexBuffer[2]=5;
								indexBuffer[3]=0;indexBuffer[4]=5;indexBuffer[5]=4;
								indexBuffer[6]=1;indexBuffer[7]=2;indexBuffer[8]=6;
								indexBuffer[9]=1;indexBuffer[10]=6;indexBuffer[11]=5;
								indexBuffer[12]=2;indexBuffer[13]=3;indexBuffer[14]=7;
								indexBuffer[15]=2;indexBuffer[16]=7;indexBuffer[17]=6;
								indexCount = 18;
								pointIndex = 8;
								break;
							}
						}
						if (layerParam.pMatrix)
						{
							FPOINT out;
							for (int i=0;i<pointIndex;++i)
							{
								FPOINT in(tct1[i].x, tct1[i].y);
								XPVec3TransformCoord(&out, &in, layerParam.pMatrix);
								tct1[i].x = out.x;
								tct1[i].y = out.y;
							}
						}
						pRenderer->DrawIndexVerticeBuffer(pic.pBigPic->handle, tct1, indexBuffer, pointIndex, indexCount);
					}

					if( layerParam.canvas )
					{
						CRECT rect(pic.offset*(m_fScale/layerParam.worldscale));
						rect += layerParam.loc;
						if( (ani->GetDirMode() == XPANIDM_8FROM5  && layerParam.dir >  XPDIR_BOTTOM) ||
							(ani->GetDirMode() == XPANIDM_B2FROM1 && layerParam.dir == XPDIR_BOTTOMLEFT)// ||
							//(ani->GetDirMode() == XPANIDM_4FROM2  && layerParam.dir == XPDIR_BOTTOMLEFT) ||
							//(ani->GetDirMode() == XPANIDM_4FROM2  && layerParam.dir == XPDIR_TOPRIGHT)
						   )
						{
							rect.left = 2*layerParam.loc.x - rect.left;
							rect.right = 2*layerParam.loc.x - rect.right;
						}
						layerParam.canvas->Draw(this, rect);
					}
				}
			}
		}
	}

	void ComponentSprite::RenderSprite(Canvas* canvas) 
	{
		if (!TestDefaultAction())
			return;
		//特效
		if (TestPropFlag(XPSPPF_HAS_EFFECT_LAYER)) 
		{
			int d = (m_pEngine->GetTick() - m_nBeginTick) % XP_EFFECT_CYCLE;
			d = d > XP_EFFECT_HALF_CYCLE?XP_EFFECT_CYCLE - d : d;
			m_fEffectAlpha = m_fMinEffectAlpha + m_fSlopeEffectAlpha * d;
		}

		LayerParam layerParam;
		layerParam.bIsShadow = false;
		layerParam.pMatrix = NULL;
		layerParam.canvas = canvas;
		layerParam.tmpSprite = TestPropFlag(XPSPPF_USE_RENDER_TARGET)?m_pEngine->GetSpritePictureHandle():INVALID_PICTURE_HANDLE;
		layerParam.bIsEffectlayer = false;

		TeleportBlurFrame tbf;
		layerParam.location = GetLocation();
		layerParam.loc = layerParam.location;
		if ( TestPropFlag(XPSPPF_IS_IN_TELEPORT) )
		{
			//d = m_nTeleportStartTime - m_iAniStartTime;
			tbf.tick = m_pEngine->GetTick();
			tbf.loc = layerParam.loc;
		}

		const Action *pCurAction = NULL;
		XPSPPropFlag asyncLoadingFlag;
		if (m_pTmpAction == NULL)
		{
			pCurAction = m_pDefaultAction;
			layerParam.bSync = !TestPropFlag(XPSPPF_DEF_ACTION_ASYNC);
			asyncLoadingFlag = XPSPPF_HAS_DEF_ASYNC_LOADING;
		} else {
			pCurAction = m_pTmpAction;
			layerParam.bSync = !TestPropFlag(XPSPPF_CUR_ACTION_ASYNC);
			asyncLoadingFlag = XPSPPF_HAS_CUR_ASYNC_LOADING;
		}
		// end update
		layerParam.dir = GetDirection();

		if( !pCurAction->TestDir(layerParam.dir) ) return;

		layerParam.curActionName = pCurAction->GetLinkName(m_iCurFrame);
		if( layerParam.curActionName == L"" )
			layerParam.curActionName = m_pTmpAction == NULL ? m_strDefaultActionName : m_strTmpActionName;
		layerParam.linkframe = pCurAction->GetLineFrame(m_iCurFrame);

		std::vector<int> ecorder(m_nLayerCount);

		pCurAction->GetFrameOrder(m_iCurFrame, layerParam.dir, &ecorder[0], m_nLayerCount);

		DrawPictureParam param;
		XPCOLOR color;
		FRECT rect;
		CPOINT vplt(m_pEngine->GetViewport().left, m_pEngine->GetViewport().top);
		layerParam.waterdepth = 0.0f;
		if (TestPropFlag(XPSPPF_SCREEN_COORD))
		{
			if (!TestPropFlag(XPSPPF_RENDER_SHADOW))
				layerParam.tmpSprite = INVALID_PICTURE_HANDLE;
			vplt.x = 0;
			vplt.y = 0;
			layerParam.worldscale = 1.0f;
		} else {
			layerParam.waterdepth = (float)m_pEngine->GetWaterDepth(layerParam.loc);
			layerParam.worldscale = m_pEngine->GetWorldScale();
		}
		
		ConfigManager* pCfgMan = m_pEngine->GetConfigManager();
		Renderer* pRenderer = m_pEngine->GetRenderer();
		RendererStateAutoRecover stateAutoRecover(pRenderer);

		if (!layerParam.bSync)//异步，而且还没有在加载
		{
			bool needPrefech = false;
			bool basePic = false;
			bool bCanUseCurrentDirection = true;
			XAni::XPic pic;
			for(int i=0; i< m_nLayerCount; ++i)
			{
				layerParam.ec = ecorder[i];
				if( layerParam.ec < 0 || layerParam.ec >= m_nLayerCount ) continue;
				if( NULL == m_CurComponents[layerParam.ec] ) continue;
				if( !pCurAction->TestEquip(layerParam.ec)) continue;
				XHardRef<XAni> ani = m_CurComponents[layerParam.ec]->GetAni(m_ModelName, layerParam.curActionName, true);
				if( ani.IsValid())
				{
					switch (ani->GetState())
					{
					case XAni::XPANISTATE_NOPICTURE:
					case XAni::XPANISTATE_NO_XAP:
						needPrefech = true;
						break;
					case XAni::XPANISTATE_BASE_PIC:
						basePic = true;
						if (bCanUseCurrentDirection)
						{
							for(int k=0; k<ani->GetRegionCount();++k)
							{
								if( ani->GetPic(layerParam.dir, 0, k, pic) && pic.pBigPic && pic.pBigPic->handle == INVALID_PICTURE_HANDLE )
								{
									bCanUseCurrentDirection = false;
								}
							}
						}
						break;
					default:
						break;
					}
					if (needPrefech)
						break;
				}
			}
			if (TestPropFlag(asyncLoadingFlag))//如果正在加载
			{
				if (!needPrefech)//没有XPANISTATE_NOPICTURE了
				{
					if (basePic)//有的层还没有全部load完
					{
						layerParam.linkframe = 0;
						pCurAction->GetFrameOrder(layerParam.linkframe, layerParam.dir, &ecorder[0], m_nLayerCount);
						if (!bCanUseCurrentDirection)
						{
							for (int dir=XPDIR_TOP;dir<XPDIR_COUNT;++dir)
							{
								if (pCurAction->TestDir(dir))
								{
									layerParam.dir = (XPDIRECTION)dir;
									break;
								}
							}
						}
					} else {
						SetPropFlag(asyncLoadingFlag, false);
					}
				}
			} else {	//没有在加载
				if (needPrefech)
				{
					SetPropFlag(asyncLoadingFlag, true);
					PrefetchAction(pCurAction == m_pDefaultAction?GetDefaultAction():GetCurActionName());
				}
			}
		}

		XPRENDER_EFFECT oldEffect;
		if (layerParam.tmpSprite != INVALID_PICTURE_HANDLE)
		{
			layerParam.bHasRenderTarget = true;
			pRenderer->PushRenderTarget(layerParam.tmpSprite);
			pRenderer->Clear(0x00000000);
			m_MaxRectInTex.Assign(TEMP_SPRITE_PICTURE_WIDTH, TEMP_SPRITE_PICTURE_HEIGHT, 0, 0);
			layerParam.location.x = (int)TEMP_SPRITE_PICTURE_HALF_WIDTH;
			layerParam.location.y = (int)TEMP_SPRITE_PICTURE_HALF_HEIGHT;
			oldEffect = pRenderer->GetRenderEffect();
			pRenderer->SetRenderEffect(XPRE_NULL);
			layerParam.water = (float)layerParam.location.y - layerParam.waterdepth;
		} else {
			layerParam.bHasRenderTarget = false;
			layerParam.location -= vplt;
			layerParam.location.x = static_cast<int>(layerParam.location.x * layerParam.worldscale);
			layerParam.location.y = static_cast<int>(layerParam.location.y * layerParam.worldscale);
			layerParam.water = (float)layerParam.location.y - layerParam.waterdepth;
			if (pCfgMan)
			{
				if (pCfgMan->IsRenderSpriteShadow() && TestPropFlag(XPSPPF_RENDER_SHADOW))	//影子
				{
					float shearX, scalingY;
					m_pEngine->GetSpriteShadowParam(shearX, scalingY);
					//画影子
					layerParam.bIsShadow = true;
					XPMATRIX3 m1, m2;
					FPOINT fLocation(layerParam.location);
					if (layerParam.waterdepth >= 1)	//有水
					{
						fLocation.y -= layerParam.waterdepth;
					}
					XPMatrixScaling(&m1, &fLocation, 1.0f, scalingY);
					XPMatrixShearX(&m2, &fLocation, shearX);
					m1 *= m2;
					layerParam.pMatrix = &m1;
					layerParam.canvas = NULL;
					for(int i=0; i< m_nLayerCount; ++i)
					{
						layerParam.ec = ecorder[i];
						if( layerParam.ec < 0 || layerParam.ec >= m_nLayerCount ) continue;
						if( NULL == m_CurComponents[layerParam.ec] ) continue;
						if( !pCurAction->TestEquip(layerParam.ec)) continue;
						if (((1 << layerParam.ec) & m_FlagEffectLayer) != 0)
							continue;
						RenderALayer(layerParam);
						
					}
					layerParam.canvas = canvas;
					layerParam.pMatrix = NULL;
					layerParam.bIsShadow = false;

				}
				if (TestPropFlag(XPSPPF_IS_IN_TELEPORT) && pCfgMan->IsBlurForTeleport())
				{//画残影
					TeleportBlurList::iterator it = m_blurList.begin();
					CPOINT oldLocation = layerParam.location;
					unsigned char oldAlpha = m_nAlpha;
					layerParam.canvas = NULL;
					for (;it != m_blurList.end(); )
					{
						int d = tbf.tick - it->tick;
						if (d > m_nTeleportBlurTime)
						{
							it = m_blurList.erase(it);
						} else {
							layerParam.location = it->loc - vplt;
							layerParam.location.x = static_cast<int>(layerParam.location.x * layerParam.worldscale);
							layerParam.location.y = static_cast<int>(layerParam.location.y * layerParam.worldscale);
							float alphadel = static_cast<float>(m_nTeleportBlurTime - d) / m_nTeleportBlurTime;
							m_nAlpha = static_cast<unsigned char>(oldAlpha * alphadel * alphadel * alphadel * 0.8);
							for(int i=0; i< m_nLayerCount; ++i)
							{
								layerParam.ec = ecorder[i];
								if( layerParam.ec < 0 || layerParam.ec >= m_nLayerCount ) continue;
								if( NULL == m_CurComponents[layerParam.ec] ) continue;
								if( !pCurAction->TestEquip(layerParam.ec)) continue;
								if (((1 << layerParam.ec) & m_FlagEffectLayer) != 0)
									continue;
								RenderALayer(layerParam);
							}
							++it;
						}
					}
					m_nAlpha = oldAlpha;
					layerParam.location = oldLocation;
					//param.blend = XPTEXBM_DEFAULT;
					m_blurList.push_back(tbf);
					layerParam.canvas = canvas;
				}
			}
			//如果没有renderTarget，那么就需要画武器光特效了
			if (TestPropFlag(XPSPPF_HAS_EFFECT_LAYER))
			{
				switch(layerParam.dir)
				{
				case 0:
				case 5:
				case 6:
				case 7:
					//先画
					for(int i=0; i< m_nLayerCount; ++i)
					{
						layerParam.ec = ecorder[i];
						if( layerParam.ec < 0 || layerParam.ec >= m_nLayerCount ) continue;
						if( NULL == m_CurComponents[layerParam.ec] ) continue;
						if( !pCurAction->TestEquip(layerParam.ec)) continue;
						if( ((1 << layerParam.ec) & m_FlagEffectLayer) == 0) continue;//不是否特效层
						layerParam.bIsEffectlayer = true;
						RenderALayer(layerParam);
					}
					break;
				default:
					break;
				}
			}
		}

		int effectI = -1;
		int firstdrawEffect = 0;	//0代表不画，1代表先画，2代表后画
		PictureHandle alphaSprite = pCfgMan->IsRenderAlphaSprite()?m_pEngine->GetAlphaSpritePictureHandle():INVALID_PICTURE_HANDLE;
		for(int i=0; i< m_nLayerCount; ++i)
		{
			layerParam.ec = ecorder[i];
			if( layerParam.ec < 0 || layerParam.ec >= m_nLayerCount ) continue;
			if( NULL == m_CurComponents[layerParam.ec] ) continue;
			if( !pCurAction->TestEquip(layerParam.ec)) continue;
			layerParam.bIsEffectlayer = ((1 << layerParam.ec) & m_FlagEffectLayer) != 0;//是否特效层
			if (layerParam.bIsEffectlayer)
			{
				effectI = i;
				switch(layerParam.dir)
				{
				case 0:
				case 5:
				case 6:
				case 7:
					firstdrawEffect = 1;
					break;
				default:
					firstdrawEffect = 2;
					break;
				}
				continue;
			}
			RenderALayer(layerParam);
			//画最上面的半透明贴图
			if (TestPropFlag(XPSPPF_IS_COVERD_ALPHA) && alphaSprite != INVALID_PICTURE_HANDLE && layerParam.tmpSprite == INVALID_PICTURE_HANDLE)
			{
				pRenderer->PushRenderTarget(alphaSprite);
				layerParam.canvas = NULL;
				RenderALayer(layerParam);
				layerParam.canvas = canvas;
				pRenderer->PopRenderTarget();
			}
		}
		if (layerParam.tmpSprite == INVALID_PICTURE_HANDLE)
		{
			//如果没有renderTarget，那么就需要画武器光特效了
			if (effectI >= 0 && firstdrawEffect == 2)
			{
				layerParam.ec = ecorder[effectI];
				layerParam.bIsEffectlayer = true;
				RenderALayer(layerParam);
			}
		} else {
			color.data = 0xFFFFFFFF;
			//计算m_srcRectInTex
			m_srcRectInTex.left = m_MaxRectInTex.left / TEMP_SPRITE_PICTURE_WIDTH;
			m_srcRectInTex.right = m_MaxRectInTex.right / TEMP_SPRITE_PICTURE_WIDTH;
			m_srcRectInTex.top = m_MaxRectInTex.top / TEMP_SPRITE_PICTURE_HEIGHT;
			m_srcRectInTex.bottom = m_MaxRectInTex.bottom / TEMP_SPRITE_PICTURE_HEIGHT;
			m_MaxRectInTex.left -= TEMP_SPRITE_PICTURE_HALF_WIDTH;
			m_MaxRectInTex.top -= TEMP_SPRITE_PICTURE_HALF_HEIGHT;
			m_MaxRectInTex.right -= TEMP_SPRITE_PICTURE_HALF_WIDTH;
			m_MaxRectInTex.bottom -= TEMP_SPRITE_PICTURE_HALF_HEIGHT;

			param.colorCount = 1;
			param.handle = layerParam.tmpSprite;
			pRenderer->SetRenderEffect(oldEffect);
			pRenderer->PopRenderTarget();
			layerParam.location = layerParam.loc - vplt;
			layerParam.location.x = static_cast<int>(layerParam.location.x * layerParam.worldscale);
			layerParam.location.y = static_cast<int>(layerParam.location.y * layerParam.worldscale);
			FRECT rect = m_MaxRectInTex + layerParam.location;
			if (pCfgMan)
			{
				if (pCfgMan->IsRenderSpriteShadow() && TestPropFlag(XPSPPF_RENDER_SHADOW))
				{
					float shearX, scalingY;
					m_pEngine->GetSpriteShadowParam(shearX, scalingY);
					CPOINT lightPos;
					if (m_pEngine->GetLightPos(lightPos))
					{
						float dx = static_cast<float>(lightPos.x - layerParam.loc.x);
						float dy = static_cast<float>(lightPos.y - layerParam.loc.y);
						if (fabs(dy) < 0.001)
						{
							shearX = 3;
						} else {
							shearX = dx / dy;
							if (fabs(shearX) > 3)
							{
								shearX = shearX / fabs(shearX) * 3;
							}
							//scalingY = sqrtf(dx*dx+dy*dy) / (m_srcRectInTex.Height() * TEMP_SPRITE_PICTURE_HEIGHT) * (dy<0?-1:1);
							scalingY *= (dy<0?-1:1);
						}
					}
					//画影子
					XPMATRIX3 m1, m2;
					FRECT frect = m_MaxRectInTex;
					FRECT srcRect = m_srcRectInTex;
					XPCOLOR c = SHADOW_COLOR_ON_LAND;
					XPMatrixScaling(&m1, 1.0f, scalingY);
					XPMatrixShearX(&m2, shearX);
					m1 *= m2;
					if (layerParam.waterdepth < 1)
					{	//没有水
						XPMatrixTranslation( &m2, (float)(layerParam.location.x), (float)(layerParam.location.y) );
						m1 *= m2;
					} else {
						//有水
						srcRect.bottom = (TEMP_SPRITE_PICTURE_HALF_HEIGHT - layerParam.waterdepth) / TEMP_SPRITE_PICTURE_HEIGHT;
						frect.bottom = 0.0f;
						frect.top += layerParam.waterdepth;
						XPMatrixTranslation( &m2, (float)(layerParam.location.x), (float)(layerParam.location.y-layerParam.waterdepth) );
						m1 *= m2;
						c = SHADOW_COLOR_IN_WATER;
					}
					param.pColor = &c;
					param.pMatrix = &m1;
					param.pRect = &frect;
					param.pSrcrect = &srcRect;
					pRenderer->DrawPicture(param);
				}
				if (TestPropFlag(XPSPPF_IS_IN_TELEPORT) && pCfgMan->IsBlurForTeleport())
				{//画残影
					TeleportBlurList::iterator it = m_blurList.begin();
					param.colorCount = 1;
					//XPCOLOR color(0xFF202020);
					XPCOLOR blurColor(0xFFFFFFFF);
					param.pColor = &blurColor;
					param.pMatrix = NULL;
					param.pSrcrect = &m_srcRectInTex;
					//param.blend = XPTEXBM_ADDITIVE;
					for (;it != m_blurList.end();)
					{
						int d = tbf.tick - it->tick;
						if (d > m_nTeleportBlurTime)
						{
							it = m_blurList.erase(it);
						} else {
							FPOINT tmpPt = it->loc - vplt;
							tmpPt *= layerParam.worldscale;
							FRECT rect = m_MaxRectInTex + tmpPt;
							float alphadel = static_cast<float>(m_nTeleportBlurTime - d) / m_nTeleportBlurTime;
							blurColor.a = static_cast<unsigned char>(m_nAlpha * alphadel * alphadel * alphadel * 0.8);
							param.pRect = &rect;
							pRenderer->DrawPicture(param);
							++it;
						}
					}
					//param.blend = XPTEXBM_DEFAULT;
					m_blurList.push_back(tbf);
				}
				//画最上面的半透明贴图
				if (TestPropFlag(XPSPPF_IS_COVERD_ALPHA) && alphaSprite != INVALID_PICTURE_HANDLE)
				{
					param.colorCount = 1;
					param.pColor = &color;
					color.a = m_nAlpha;
					param.pMatrix = NULL;
					param.pSrcrect = &m_srcRectInTex;
					param.pRect = &rect;
					pRenderer->PushRenderTarget(alphaSprite);
					pRenderer->DrawPicture(param);
					pRenderer->PopRenderTarget();
				}
			}
			if (effectI >= 0)
			{
				layerParam.ec = ecorder[effectI];
				layerParam.bIsEffectlayer = true;//是否特效层
			}

			if (firstdrawEffect == 1)
				RenderALayer(layerParam);

			param.blend = m_bIsHighlight?XPTEXBM_MODULATE2X:XPTEXBM_DEFAULT;
			param.colorCount = 1;
			param.pColor = &color;
			color.a = m_nAlpha;
			param.pMatrix = NULL;
			param.pSrcrect = &m_srcRectInTex;
			param.pRect = &rect;
			pRenderer->DrawPicture(param);
			if (firstdrawEffect == 2)
				RenderALayer(layerParam);
		}
	}

	void ComponentSprite::TeleportWithBlur(const Location &point, int telTime, int blurTime)
	{
		StopMove();
		m_nTeleportStartTime = m_pEngine->GetTick();
		SetPropFlag(XPSPPF_IS_IN_TELEPORT, true);
		m_TeleportObj = point;
		m_nTeleportTime = telTime;
		m_nTeleportBlurTime = blurTime;
		m_TeleportStart = GetLocation();
		m_blurList.clear();
	}

	void ComponentSprite::GetBase(SortBaseRectType &base)
	{
		//可以考虑把XPBase保存在ComponentSprite里面，这样不用每帧都做一次查表
		CPOINT loc = GetLocation();
		const XPBase& modelbase = m_pEngine->GetSpriteManager()->GetBase(m_ModelName);
		CPOINT tmpCPoint;
		TransToDiamondRadix(loc.x + modelbase.left.x, loc.y + modelbase.left.y, tmpCPoint);
		base.left = tmpCPoint.x;
		base.bottom = tmpCPoint.y;
		TransToDiamondRadix(loc.x + modelbase.right.x, loc.y + modelbase.right.y, tmpCPoint);
		base.right = tmpCPoint.x;
		base.top = tmpCPoint.y;
	}

};
