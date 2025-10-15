#include "../common/nulog.h"
#include "../common/nurecttopologylist.h"
#include "../renderer/nurenderer.h"
#include "../engine/nuconfigmanager.h"
#include "../world/nuworld.h"
#include "../nuiapp.h"
#include "nuspritemanager.h"
#include "nucomponentsprite.h"

static const float UNDER_WATER_ALPHA = 80.0f;
static const int TAN_THETA = 2;	//当sprite是斜的时候，水面是斜的，宽高比是2（水面和城市网格平行）
static const DWORD SHADOW_COLOR_ON_LAND = 0x66000000;
static const DWORD SHADOW_COLOR_IN_WATER = 0x40000000;
static const int MIN_SHAKE_SCOPE = 5;//单位：像素
static const int MAX_SHAKE_SCOPE = 15;//单位：像素
static const unsigned int SHAKE_INTERVAL = 150;//单位：毫秒

namespace Nuclear
{
	float IntervalRandom(float fMin, float fMax)
	{
		float dRatio = ((float)rand()) / ((float)(RAND_MAX));
		return (fMin + (fMax - fMin)*dRatio);
	}

	ComponentSprite::ComponentSprite(Sprite *pLogicSprite) : m_pLogicSprite(pLogicSprite),
		m_pDefaultAction(NULL), m_pTmpAction(NULL), m_pActionMap(NULL),
		m_flagRideLayer(0), m_flagEffectLayer(0), m_nLayerCount(0),
		m_nSoundPriority(0), m_bLookupSegmpak(false),
		m_nStartShakeTime(0), m_ShakeCount(-1), m_iPartCount(0), m_bDyeEnable(0), m_bActive(true)
	{
		m_nOldFrame = -1;
		m_fMinEffectAlpha = 0.1f;
		m_fSlopeEffectAlpha = (0.5f - m_fMinEffectAlpha) / FIRE_EFFECT_HALF_CYCLE;
		m_fEffectAlpha = m_fMinEffectAlpha;
		m_pLogicSprite->SetPropFlag(Sprite::XPSPPF_RENDER_SHADOW, true);
		m_OrgLocation = m_NextLocation = m_pLogicSprite->GetLocation();
	}

	ComponentSprite::~ComponentSprite()
	{
		ReleaseAllActions();
	}

	void ComponentSprite::ReleaseAllActions()
	{
		ActionNameSet::iterator it = m_holdActions.begin(), ie = m_holdActions.end();
		for (; it != ie;)
		{
			ReleaseAction(*(it++));
		}
	}

	void ComponentSprite::SetComponentAniReleaseFlag(Nuclear::NuclearPaniReleaseFlag flag)
	{
		for (int i = 0; i < (int)m_curComponent.size(); ++i)
		{
			if (NULL != m_curComponent[i])
				m_curComponent[i]->SetAniReleaseFlag(flag);
		}
	}

	void ComponentSprite::ResetEffectPos()
	{
		if (m_pLogicSprite->m_modelName == L"")
		{
			m_pLogicSprite->m_effectPos.x = m_pLogicSprite->m_effectPos.y = 0;
			return;
		}
		m_pLogicSprite->m_effectPos.y -= (m_pLogicSprite->TestPropFlag(Sprite::XPSPPF_IS_READING) ? m_pLogicSprite->m_nHorseHeight + 25 : 0) * m_pLogicSprite->m_fScale;
	}

	bool ComponentSprite::SetModel(const std::wstring &modelname, bool async)
	{
		ReleaseAllActions();
		m_curComponent.clear();
		m_colors.clear();
		m_pDefaultAction = NULL;
		m_pTmpAction = NULL;
		m_flagRideLayer = 0;
		m_flagEffectLayer = 0;
		m_nLayerCount = 0;
		m_bDyeEnable = 0;
		m_pActionMap = NULL;

		if (modelname != L"")
		{
			SpriteManager* pSprMan = m_pLogicSprite->m_pEB->GetSpriteManager();
			m_nLayerCount = pSprMan->GetLayerCount(modelname);
			m_curComponent.resize(m_nLayerCount, NULL);
			m_vShaderType.resize(m_nLayerCount);
			m_bDyeEnable = pSprMan->GetDyeEnable(modelname);
			m_iPartCount = 2;
			m_vPartParams.resize(m_nLayerCount);
			for (int i = 0; i < m_nLayerCount; i++)
			{
				m_vPartParams[i].resize(m_iPartCount);
				int iParamCount = 7;
				for (int i2 = 0; i2 < m_iPartCount; i2++)
				{
					m_vPartParams[i][i2].resize(iParamCount);
					m_vPartParams[i][i2][0] = 0;
					m_vPartParams[i][i2][1] = 0;
					m_vPartParams[i][i2][2] = 2;
					m_vPartParams[i][i2][3] = 3;
					m_vPartParams[i][i2][4] = 4;
					m_vPartParams[i][i2][5] = 0;
					m_vPartParams[i][i2][6] = 0;
				}
			}

			m_colors.resize(m_nLayerCount, 0xffffffff);
			m_pLogicSprite->m_nTitlePos = pSprMan->GetTitlePos(modelname);
			m_pLogicSprite->m_surfaceType = pSprMan->GetWaterSurfaceType(modelname);
			m_pLogicSprite->m_fScale = pSprMan->GetModelScale(modelname) * m_pLogicSprite->m_fSetScale;
			unsigned char flag = 1;
			for (int i = 0; i < m_nLayerCount; ++i)
			{
				if (pSprMan->GetLayerRideEnable(modelname, i))
				{
					m_flagRideLayer |= flag;
				}
				if (pSprMan->GetLayerEffectEnable(modelname, i))
				{
					m_flagEffectLayer |= flag;
					m_pLogicSprite->SetPropFlag(Sprite::XPSPPF_HAS_EFFECT_LAYER, true);
				}
				flag <<= 1;
			}
			Nuclear::ModelSoundParam &param = pSprMan->GetSoundParam(modelname);
			m_pActionMap = param.soundmap[L""];
		}
		ResetEffectPos();
		return true;
	}

	bool ComponentSprite::GetComponent(int scid, std::wstring& resource)
	{
		if (scid < 0 || scid >= m_nLayerCount) return false;
		if (!m_curComponent[scid]) return false;
		resource = m_curComponent[scid]->GetConstName();
		return true;
	}

	void* ComponentSprite::GetComponent(int scid)
	{
		if (scid < 0 || scid >= m_nLayerCount) return NULL;
		if (!m_curComponent[scid]) return NULL;
		return m_curComponent[scid];
	}

	bool ComponentSprite::GetComponentColor(int scid, NuclearColor &color)
	{
		if (scid < 0 || scid >= m_nLayerCount) return false;
		if (!m_curComponent[scid]) return false;
		color = m_colors[scid];
		return true;
	}

	bool ComponentSprite::TestViewport(const NuclearRect &viewport) const
	{
		const Action *pCurAction = m_pTmpAction == NULL ? m_pDefaultAction : m_pTmpAction;
		if (NULL == pCurAction)
			return false;
		Nuclear::NuclearLocation CurLoc = m_pLogicSprite->GetLocation();
		CurLoc *= (m_pLogicSprite->GetFlyScaleCur() / 10000.0f);
		if (IsRectCross(pCurAction->GetBoundingBox(m_pLogicSprite->GetDirection()) + CurLoc, viewport))
			return true;
		NuclearPoint location = m_pLogicSprite->GetTitleLocation();
		NuclearRect titleRect;
		titleRect.left = location.x - m_pLogicSprite->m_nTitleWidth / 2;
		titleRect.right = titleRect.left + m_pLogicSprite->m_nTitleWidth;
		titleRect.bottom = titleRect.top = location.y;
		if (m_pLogicSprite->m_titleAlign == XPTA_CENTER_BOTTOM)
		{
			titleRect.top -= m_pLogicSprite->m_nTitleHeight;
		}
		else {
			titleRect.bottom += m_pLogicSprite->m_nTitleHeight;
		}
		return IsRectCross(titleRect, viewport);
	}

	bool ComponentSprite::SetComponent(int sc, const std::wstring& resource, NuclearColor color/*XRGB*/)
	{
		if (sc < 0 || sc >= m_nLayerCount)
		{
			return false;
		}

		SpriteManager *pSprMan = m_pLogicSprite->m_pEB->GetSpriteManager();
		Component *equip = pSprMan->GetComponent(m_pLogicSprite->m_modelName, sc, resource);
		if ((equip == NULL) && resource.size() > 0)
		{
			return false;
		}
		ActionNameSet::iterator it = m_holdActions.begin(), ie = m_holdActions.end();
		ActionNameSet actions;
		const Action* act = NULL;
		for (; it != ie; ++it)
		{
			if (m_curComponent[sc])
			{
				m_curComponent[sc]->ReleaseAction(*it);
			}
			if (GetActionNames(*it, actions, act) && act->TestEquip(sc) && equip)
			{
				equip->HoldAction(m_pLogicSprite->m_modelName, *it, actions, m_bDyeEnable);
			}
		}
		if (equip)
		{
			if (m_pLogicSprite->m_strDefaultActionName.size() > 0)
			{
				if (GetActionNames(m_pLogicSprite->m_strDefaultActionName, actions, act) && act->TestEquip(sc))
				{
					if (equip->PrefetchAction(m_pLogicSprite->m_modelName, m_pLogicSprite->m_strDefaultActionName, actions, m_bDyeEnable) != XPPR_OK)
					{
						m_pLogicSprite->SetPropFlag(Sprite::XPSPPF_HAS_DEF_ASYNC_LOADING, true);
					}
				}
			}
			if (m_pLogicSprite->m_strTmpActionName.size() > 0)
			{
				if (GetActionNames(m_pLogicSprite->m_strTmpActionName, actions, act) && act->TestEquip(sc))
				{
					if (equip->PrefetchAction(m_pLogicSprite->m_modelName, m_pLogicSprite->m_strTmpActionName, actions, m_bDyeEnable) != XPPR_OK)
					{
						m_pLogicSprite->SetPropFlag(Sprite::XPSPPF_HAS_CUR_ASYNC_LOADING, true);
					}
				}
			}
		}
		m_curComponent[sc] = equip;
		m_colors[sc] = color;
		m_colors[sc].a = 255;
		ModelSoundParam &param = pSprMan->GetSoundParam(m_pLogicSprite->m_modelName);
		if (param.keyComponentID == sc)
		{
			m_pActionMap = param.soundmap[resource];
		}
		return true;
	}

	bool ComponentSprite::GetActionNames(const std::wstring &action_name, ActionNameSet &actions, const Action* &pAct)
	{
		actions.clear();
		SpriteManager *pSprMan = m_pLogicSprite->m_pEB->GetSpriteManager();
		const std::wstring &actionref_name = pSprMan->GetActionRef(m_pLogicSprite->m_modelName, action_name);
		if (actionref_name == L"") return false;
		pAct = pSprMan->GetAction(m_pLogicSprite->m_modelName, actionref_name);
		if (pAct == NULL) return false;
		if (pAct->IsInd())
		{
			int framecount = pAct->GetFrameCount();
			for (int frame = 0; frame < framecount; ++frame)
			{
				actions.insert(pAct->GetLinkName(frame));
			}
		}
		else {
			actions.insert(actionref_name);
		}
		return true;
	}

	// 预取该精灵的动作资源。如果该资源在GC时间范围（120秒）内没有被使用或者被重新（或别的精灵）预取，那么会自动释放
	NuclearPrefetchResult ComponentSprite::PrefetchAction(const std::wstring &action_name)
	{
		const Action *act = NULL;
		ActionNameSet actions;
		if (!GetActionNames(action_name, actions, act))
			return XPPR_ASYNC_LOADING;
		XPASSERT(act);//act为NULL就不可能GetActionNames返回true
		NuclearPrefetchResult result = XPPR_OK, tmpResult;
		for (int i = 0; i < m_nLayerCount; ++i)
		{
			if (NULL == m_curComponent[i]) continue;
			if (!act->TestEquip(i)) continue;
			tmpResult = m_curComponent[i]->PrefetchAction(m_pLogicSprite->m_modelName, action_name, actions, m_bDyeEnable);
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
		for (int i = 0; i < m_nLayerCount; ++i)
		{
			if (NULL == m_curComponent[i]) continue;
			if (!act->TestEquip(i)) continue;
			m_curComponent[i]->HoldAction(m_pLogicSprite->m_modelName, action_name, actions, m_bDyeEnable);
		}
		m_holdActions.insert(action_name);
	}

	// 只有Hold住的Action才需要Release
	void ComponentSprite::ReleaseAction(const std::wstring &action_name)
	{
		if (m_holdActions.find(action_name) == m_holdActions.end())
			return;
		SpriteManager *pSprMan = m_pLogicSprite->m_pEB->GetSpriteManager();
		std::wstring actionref_name = pSprMan->GetActionRef(m_pLogicSprite->m_modelName, action_name);
		if (actionref_name == L"")
			return;
		const Action *act = pSprMan->GetAction(m_pLogicSprite->m_modelName, actionref_name);
		if (act == NULL)
			return;
		for (int i = 0; i < m_nLayerCount; ++i)
		{
			if (NULL == m_curComponent[i]) continue;
			if (!act->TestEquip(i)) continue;
			m_curComponent[i]->ReleaseAction(action_name);
		}
		m_holdActions.erase(action_name);
	}

	void ComponentSprite::SetHoldLastFrame(bool bHoldLastFrame)
	{
		m_pLogicSprite->SetPropFlag(Sprite::XPSPPF_HOLD_LAST_FRAME, bHoldLastFrame);
	}

	bool ComponentSprite::SetDefaultAction(const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float fScaleForTotalTime, bool bHoldLastFrame)
	{
		std::wstring actionref_name = m_pLogicSprite->m_pEB->GetSpriteManager()->GetActionRef(m_pLogicSprite->m_modelName, action_name);
		if (actionref_name == L"")
			return false;
		const Action *act = m_pLogicSprite->m_pEB->GetSpriteManager()->GetAction(m_pLogicSprite->m_modelName, actionref_name);
		if (act == NULL)
			return false;
		m_pLogicSprite->m_strDefaultActionName = action_name;
		m_pLogicSprite->SetPropFlag(Sprite::XPSPPF_HOLD_LAST_FRAME, bHoldLastFrame);
		m_pLogicSprite->SetPropFlag(Sprite::XPSPPF_DEF_ACTION_ASYNC, type != XPSALT_SYNC);
		m_pLogicSprite->SetPropFlag(Sprite::XPSPPF_START_HOLD_LAST_FRAME, false);
		m_strDefaultActionRefName = actionref_name;
		m_pLogicSprite->m_iAniStartTime = m_pLogicSprite->m_pEB->GetTick();
		m_pLogicSprite->m_iAniDurationTime = (int)(act->GetTime() * fScaleForTotalTime);
		m_pLogicSprite->m_iDefaultAniDurationTime = m_pLogicSprite->m_iAniDurationTime;
		m_pLogicSprite->m_iCurFrame = 0;
		m_nOldFrame = -1;
		m_pDefaultAction = act;
		m_pLogicSprite->m_defaultActionTypeFlag = m_pLogicSprite->m_pEB->GetSpriteManager()->actionType.GetActionType(action_name);
		m_pLogicSprite->SetPropFlag(Sprite::XPSPPF_HAS_DEF_ASYNC_LOADING, PrefetchAction(action_name) != XPPR_OK);
		if (bHoldLastFrame)
		{
			World* world = static_cast<World*>(GetEngine()->GetWorld());
			eNuclearWorldMode worldMode = world->GetWorldMode();
			bool bIsInBattle = worldMode == XPWM_SIMPLE_BATTLE || worldMode == XPWM_WORLD_BATTLE;
			if (!bIsInBattle)
				HoldAction(action_name);
		}
		return true;
	}

	bool ComponentSprite::PlayAction(const std::wstring& action_name, XPSPRITE_ACTION_LOAD_TYPE type, float fScaleForTotalTime)
	{
		m_strCurrentActionRefName = action_name;
		if (action_name == L"")
		{
			NuclearPrefetchResult result = PrefetchAction(m_pLogicSprite->m_strDefaultActionName);
			if (result == XPPR_OK || (result == XPPR_BASE_ASYNC_OK && type == XPSALT_BASE_ASYNC) || (type == XPSALT_SYNC))
			{
				if (m_strTmpActionRefName != L"")
				{
					m_pTmpAction = NULL;
					m_strTmpActionRefName.clear();
					m_pLogicSprite->m_strTmpActionName.clear();
					m_pLogicSprite->m_iAniStartTime = m_pLogicSprite->m_pEB->GetTick();
					m_pLogicSprite->m_iCurFrame = 0;
					m_nOldFrame = -1;
					m_pLogicSprite->m_iAniDurationTime = m_pLogicSprite->m_iDefaultAniDurationTime;
				}
				return true;
			}
			return false;
		}

		m_pLogicSprite->SetPropFlag(Sprite::XPSPPF_CUR_ACTION_ASYNC, type != XPSALT_SYNC);
		if (type != XPSALT_SYNC)//异步
		{
			NuclearPrefetchResult result = PrefetchAction(action_name);
			m_pLogicSprite->SetPropFlag(Sprite::XPSPPF_HAS_CUR_ASYNC_LOADING, result != XPPR_OK);
			if (result != XPPR_OK && (result == XPPR_ASYNC_LOADING || type == XPSALT_ASYNC))//资源没有加载好
			{
				if (m_strTmpActionRefName != L"")//如果一个动作不是Default，那么继续播上一个动作
				{
					m_pLogicSprite->m_iAniStartTime = m_pLogicSprite->m_pEB->GetTick();
					m_pLogicSprite->m_iCurFrame = 0;
					m_nOldFrame = -1;
				}
				return false;
			}
		}
		SpriteManager *pSprMan = m_pLogicSprite->m_pEB->GetSpriteManager();
		std::wstring actionref_name = pSprMan->GetActionRef(m_pLogicSprite->m_modelName, action_name);
		if (actionref_name == L"") return false;
		const Action *act = pSprMan->GetAction(m_pLogicSprite->m_modelName, actionref_name);
		if (act == NULL) return false;

		m_pLogicSprite->SetPropFlag(Sprite::XPSPPF_START_HOLD_LAST_FRAME, false);
		m_pLogicSprite->m_strTmpActionName = action_name;
		m_strTmpActionRefName = actionref_name;
		m_pLogicSprite->m_iAniStartTime = m_pLogicSprite->m_pEB->GetTick();
		m_pLogicSprite->m_iAniDurationTime = (int)(act->GetTime() * fScaleForTotalTime);
		m_pLogicSprite->m_iCurFrame = 0;
		m_nOldFrame = -1;
		m_pTmpAction = act;
		return true;
	}


	bool ComponentSprite::GetActionTimeByName(const std::wstring& action_name, float &time)
	{
		SpriteManager *pSprMan = m_pLogicSprite->m_pEB->GetSpriteManager();
		std::wstring actionref_name = pSprMan->GetActionRef(m_pLogicSprite->m_modelName, action_name);
		if (actionref_name == L"") return false;
		const Action *act = pSprMan->GetAction(m_pLogicSprite->m_modelName, actionref_name);
		if (act == NULL) return false;
		time = act->GetTime() / 1000.0f;
		return true;
	}

	bool ComponentSprite::TestPoint(const NuclearLocation &testloc, int r, float testval)
	{
		if (m_pLogicSprite->m_mouseTestMode == XPTM_PIC_RECT)
			return true;

		XPASSERT(r <= 2);
		const Action *pCurAction = m_pTmpAction == NULL ? m_pDefaultAction : m_pTmpAction;
		if (NULL == pCurAction) return false;

		if (!pCurAction->TestDir(m_pLogicSprite->GetDirection())) return false;

		std::vector<int> ecorder(m_nLayerCount);

		pCurAction->GetFrameOrder(m_pLogicSprite->m_iCurFrame, m_pLogicSprite->GetDirection(), &ecorder[0], m_nLayerCount);
		std::wstring curActionName = pCurAction->GetLinkName(m_pLogicSprite->m_iCurFrame);
		if (curActionName == L"")
			curActionName = m_pTmpAction == NULL ? m_strDefaultActionRefName : m_strTmpActionRefName;
		int linkframe = pCurAction->GetLineFrame(m_pLogicSprite->m_iCurFrame);
		NuclearPoint pt = testloc - m_pLogicSprite->GetLocation();
		pt *= m_pLogicSprite->m_pEB->GetWorldScale() / m_pLogicSprite->m_fScale;
		for (int i = 0; i < (int)m_curComponent.size(); ++i)
		{
			int ec = ecorder[i];
			if (ec < 0 || ec >= m_nLayerCount) continue;
			if (NULL == m_curComponent[ec]) continue;
			if (!pCurAction->TestEquip(ec)) continue;
			if ((1 << ec) & m_flagEffectLayer) continue;//特效层不判断了

			NuclearHardRef<XAni> ani = m_curComponent[ec]->GetAni(m_pLogicSprite->m_modelName, curActionName, false);
			if (ani.IsValid() && ani->GetState() > XAni::XPANISTATE_NOPICTURE)
			{
				if (ani->TestPoint(m_pLogicSprite->GetDirection(), linkframe, pt, m_pLogicSprite->m_mouseTestMode, m_pLogicSprite->m_pEB->GetRenderer(), r, testval))
					return true;
			}
		}
		return false;
	}

	void ComponentSprite::UpdateAction(DWORD ticktime)
	{
		if (!m_bActive || !m_pDefaultAction)
			return;

		int d = m_pLogicSprite->m_pEB->GetTick() - m_pLogicSprite->m_iAniStartTime;
		World* world = static_cast<World*>(GetEngine()->GetWorld());
		eNuclearWorldMode worldMode = world->GetWorldMode();
		bool bIsInBattle = worldMode == XPWM_SIMPLE_BATTLE || worldMode == XPWM_WORLD_BATTLE;

		int checkTime = m_pLogicSprite->m_iAniDurationTime;
		if (bIsInBattle)
			checkTime = m_pLogicSprite->m_iAniDurationTime - ticktime;
		if (d > checkTime)
		{
			m_pLogicSprite->m_iCurFrame = 0;
			m_pLogicSprite->m_iAniStartTime = m_pLogicSprite->m_pEB->GetTick();
			m_nOldFrame = -1;
			if (m_pTmpAction != NULL)
			{
				m_pTmpAction = NULL;
				m_strTmpActionRefName.clear();
				m_strCurrentActionRefName.clear();
				m_pLogicSprite->m_strTmpActionName.clear();
				m_pLogicSprite->m_iAniDurationTime = m_pLogicSprite->m_iDefaultAniDurationTime;
				m_pLogicSprite->PlayDefaultAction();
				m_pLogicSprite->m_pEB->GetApp()->OnUpdateSpriteAction(m_pLogicSprite, XPUSAT_PLAYACTIONFINISH);
			}
			else if (m_pLogicSprite->TestPropFlag(Sprite::XPSPPF_HOLD_LAST_FRAME))
				m_pLogicSprite->SetPropFlag(Sprite::XPSPPF_START_HOLD_LAST_FRAME, true);
			else if (m_pLogicSprite->m_pEB->GetApp())
				m_pLogicSprite->m_pEB->GetApp()->OnUpdateSpriteAction(m_pLogicSprite, XPUSAT_DEFAULTACTIONFINISH);
		}
		const Action *pCurAction = m_pTmpAction == NULL ? m_pDefaultAction : m_pTmpAction;
		d = m_pLogicSprite->m_pEB->GetTick() - m_pLogicSprite->m_iAniStartTime;//这里必须再算一次，因为上面的回调可能重新PlayAction了
		float r = m_pLogicSprite->m_iAniDurationTime > 0 ? d / (float)m_pLogicSprite->m_iAniDurationTime : 0;
		int i = 0;
		int n = pCurAction->GetFrameCount();
		for (i = n - 1; i >= 0; --i)
		{
			if (r > pCurAction->GetFrameStartTime(i))
			{
				break;
			}
		}

		if (m_pLogicSprite->TestPropFlag(Sprite::XPSPPF_START_HOLD_LAST_FRAME))
			m_pLogicSprite->m_iCurFrame = n - 1;
		else
			m_pLogicSprite->m_iCurFrame = i > 0 ? i : 0;
		if (m_nOldFrame != m_pLogicSprite->m_iCurFrame)
		{
			FrameNotifyMap::iterator m_fnmIter = m_pLogicSprite->m_mapFrameNotifys.find(
				m_pTmpAction == NULL ? m_pLogicSprite->m_strDefaultActionName :
				m_pLogicSprite->m_strTmpActionName);
			if (m_fnmIter != m_pLogicSprite->m_mapFrameNotifys.end())
			{
				ActNotifyMap &anm = m_fnmIter->second;
				ActNotifyMap::iterator it = anm.find(m_pLogicSprite->m_iCurFrame);
				if (it != anm.end())
				{
					SpriteNotifySet::iterator snsIt = it->second.begin(), snsIe = it->second.end();
					for (; snsIt != snsIe; ++snsIt)
					{
						(*snsIt)->OnPlayFrame(m_pLogicSprite, m_pLogicSprite->m_strTmpActionName, m_pLogicSprite->m_iCurFrame);
					}
				}
			}
			m_nOldFrame = m_pLogicSprite->m_iCurFrame;
		}
	}

	int ComponentSprite::GetTotalFrame() const
	{
		if (!m_pDefaultAction)
			return 0;

		return ((m_pTmpAction == NULL) ? m_pDefaultAction : m_pTmpAction)->GetFrameCount();
	}

	void ComponentSprite::ResetHorseHeight(const std::wstring& ridename)
	{
		m_pLogicSprite->m_nHorseHeight = m_pLogicSprite->m_pEB->GetSpriteManager()->GetHorseHeight(m_pLogicSprite->m_modelName, ridename);
	}

	void ComponentSprite::updateShake(LayerParam& param, bool bShake)
	{
		if (bShake)
		{
			if (m_nStartShakeTime == 0)
			{
				m_nStartShakeTime = Nuclear::GetMilliSeconds();
				m_OrgLocation = param.location;
			}

			unsigned int curTime = Nuclear::GetMilliSeconds();
			unsigned int deltaTime = curTime - m_nStartShakeTime;
			int shakeCount = int(deltaTime / SHAKE_INTERVAL);
			if (shakeCount != m_ShakeCount)
			{
				int fx = IntervalRandom(-1, 1) > 0.0f ? 1 : -1;
				int fy = IntervalRandom(-1, 1) > 0.0f ? 1 : -1;
				float dx = fx*IntervalRandom(MIN_SHAKE_SCOPE, MAX_SHAKE_SCOPE);
				float dy = fy*IntervalRandom(MIN_SHAKE_SCOPE, MAX_SHAKE_SCOPE);
				m_NextLocation.x = m_OrgLocation.x + dx;
				m_NextLocation.y = m_OrgLocation.y + dy;
				m_ShakeCount = shakeCount;
				m_nStartShakeTime = curTime;
				m_OrgLocation = param.location;
			}
			else
			{
				float fScale = (float)deltaTime / SHAKE_INTERVAL;
				if (fScale >= 0.95f)
				{
					param.location.x = m_NextLocation.x;
					param.location.y = m_NextLocation.y;
				}
				else
				{
					param.location.x = m_OrgLocation.x + fScale*(m_NextLocation.x - m_OrgLocation.x);
					param.location.y = m_OrgLocation.y + fScale*(m_NextLocation.y - m_OrgLocation.y);
				}
			}
		}
		else
		{
			m_nStartShakeTime = 0;
			m_ShakeCount = -1;
		}
	}
	NuclearHardRef<XAni> ComponentSprite::LoadLayer(const LayerParam& layerParam, int iLayerIndex, bool bSync)
	{
		NuclearHardRef<XAni> ani = m_curComponent[iLayerIndex]->GetAni(m_pLogicSprite->m_modelName, layerParam.curActionName, !layerParam.bSync);
		if (ani.IsValid())
		{
			ani.SetTick(m_pLogicSprite->m_pEB->GetTick());
			switch (ani->GetState())
			{
			case XAni::XPANISTATE_NULL:
			case XAni::XPANISTATE_NO_XAP:
			case XAni::XPANISTATE_NOPICTURE:
				if (!layerParam.bSync)
					return ani;
				//m_pLogicSprite->m_pEB->GetAniManager()->SyncLoadAni(ani, m_bLookupSegmpak);
				break;
			case XAni::XPANISTATE_BASE_PIC:
				//if (layerParam.bSync)
				//	m_pLogicSprite->m_pEB->GetAniManager()->SyncLoadAni(ani, m_bLookupSegmpak);
				break;
			}
			XAni::XPic pic;
			for (int k = 0; k < ani->GetRegionCount(); ++k)
			{
				ani->GetPic(layerParam.dir, layerParam.linkframe, k, pic);
			}
		}
		return ani;
	}
	void ComponentSprite::RenderALayer(const LayerParam& layerParam, NuclearFRectt &maxRect, bool realRender)
	{
		NuclearHardRef<XAni> ani = m_curComponent[layerParam.ec]->GetAni(m_pLogicSprite->m_modelName, layerParam.curActionName, !layerParam.bSync, m_bDyeEnable);
		if (ani.IsValid())
		{
			ani.SetTick(m_pLogicSprite->m_pEB->GetTick());
			switch (ani->GetState())
			{
			case XAni::XPANISTATE_NULL:
			case XAni::XPANISTATE_NO_XAP:
			case XAni::XPANISTATE_NOPICTURE:
				if (!layerParam.bSync)
				{
					//如果是停留在最后一帧		
					if (m_pLogicSprite->TestPropFlag(Sprite::XPSPPF_HOLD_LAST_FRAME))
					{
						World* world = static_cast<World*>(GetEngine()->GetWorld());
						eNuclearWorldMode worldMode = world->GetWorldMode();
						bool bIsInBattle = worldMode == XPWM_SIMPLE_BATTLE || worldMode == XPWM_WORLD_BATTLE;
						m_pLogicSprite->SetPropFlag(Sprite::XPSPPF_START_HOLD_LAST_FRAME, true);
						if (!bIsInBattle) HoldAction(layerParam.curActionName);
					}
					return;
				}
				//m_pLogicSprite->m_pEB->GetAniManager()->SyncLoadAni(ani, m_bLookupSegmpak);
				break;
			case XAni::XPANISTATE_BASE_PIC:
				//if (layerParam.bSync)
				//	m_pLogicSprite->m_pEB->GetAniManager()->SyncLoadAni(ani, m_bLookupSegmpak);
				break;
			}

			DrawPictureParam param;
			param.bSpecAlpha = true;
			XAni::XPic pic;
			NuclearColor color;
			param.pMatrix = NULL;
			Renderer* pRenderer = m_pLogicSprite->m_pEB->GetRenderer();
			NuclearTextureBlendMode defaultBlend = m_pLogicSprite->IsHighlight() ? XPTEXBM_MODULATE2X : XPTEXBM_DEFAULT;

			//如果在绘制坐骑层，通知sprite调整带挂点的特效. by liugeng
			if (ani->GetRegionCount() > 0 &&
				m_pLogicSprite->m_pEB->GetSpriteManager()->GetLayerRideEnable(m_pLogicSprite->GetModelName(), layerParam.ec) &&
				!m_pLogicSprite->m_bRideAniIsReady)
			{
				m_pLogicSprite->m_bRideAniIsReady = true;
			}
			float rideScale = 1.0;
			if (m_pLogicSprite->m_pEB->GetSpriteManager()->GetLayerRideEnable(m_pLogicSprite->GetModelName(), layerParam.ec))
			{
				rideScale = m_pLogicSprite->m_fHorseScale;
			}

			for (int k = 0; k < ani->GetRegionCount(); ++k)
			{
				if (realRender)
				{
					if (ani->GetPic(layerParam.dir, layerParam.linkframe, k, pic) && pic.pBigPic && pic.pBigPic->handle != INVALID_PICTURE_HANDLE)
					{
						color = ((k == ani->GetRegionCount() - 1) ? m_colors[layerParam.ec] : 0xffffffff);
						NuclearFRectt rect(pic.offset.ToFRECT());
						if (layerParam.bIsEffectlayer)
						{
							rect *= FIRE_EFFECT_SCALE * m_pLogicSprite->m_fScale;
							param.blend = XPTEXBM_ADDITIVE_ALPHA;
							color.a = static_cast<unsigned char>(layerParam.nAlpha *m_fEffectAlpha);
						}
						else
						{
							rect *= m_pLogicSprite->m_fScale;
							param.blend = defaultBlend;
							color.a = layerParam.nAlpha;
						}
						rect *= rideScale;
						rect *= layerParam.iFlyScaleCur / 10000.0f;

						//World* world = static_cast<World*>(GetEngine()->GetWorld());
						//rect *= world->GetScale();

						rect += layerParam.location;

						rect.top -= layerParam.iFlyOffsetYCur / 100;
						rect.bottom -= layerParam.iFlyOffsetYCur / 100;

						if ((m_pLogicSprite->m_nHorseHeight != 0) && m_pLogicSprite->TestPropFlag(Sprite::XPSPPF_IS_READING))
						{
							if (!((1 << layerParam.ec) & m_flagRideLayer))//如果不是马
							{
								rect.top -= m_pLogicSprite->m_nHorseHeight*m_pLogicSprite->m_fScale;
								rect.bottom -= m_pLogicSprite->m_nHorseHeight*m_pLogicSprite->m_fScale;
							}
						}

						if ((ani->GetDirMode() == XPANIDM_8FROM5  && layerParam.dir > XPDIR_BOTTOM) ||
							(ani->GetDirMode() == XPANIDM_B2FROM1 && layerParam.dir == XPDIR_BOTTOMLEFT) ||
							(ani->GetDirMode() == XPANIDM_4FROM2  && layerParam.dir == XPDIR_BOTTOMLEFT) ||
							(ani->GetDirMode() == XPANIDM_4FROM2  && layerParam.dir == XPDIR_TOPRIGHT)
							)
						{
							rect.left = 2 * (layerParam.location.x) - rect.left;
							rect.right = 2 * (layerParam.location.x) - rect.right;
						}

						maxRect.left = std::min(maxRect.left, rect.left);
						maxRect.right = std::max(maxRect.right, rect.right);
						maxRect.top = std::min(maxRect.top, rect.top);
						maxRect.bottom = std::max(maxRect.bottom, rect.bottom);

						param.iShaderType = m_vShaderType[layerParam.ec];
						if (param.iShaderType > 0)
						{
							if (m_bDyeEnable == 0)
								param.iShaderType = 0;
							else if (m_bDyeEnable == 1)
								param.iShaderType = 1;
							else if (m_bDyeEnable == 2)
								param.iShaderType = 3;
						}


						if (pic.pBigPicPart && pic.pBigPicPart->loaded && pic.pBigPicPart->handle)
						{
							param.handlePart = pic.pBigPicPart->handle;
						}
						else
						{
							param.handlePart = 0;
						}

						param.handle = pic.pBigPic->handle;
						param.pRect = &rect;
						param.pSrcrect = &pic.srcrect;
						param.pColor = &color;
						param.colorCount = 1;

						if (param.iShaderType != 0)
						{
							param.pPartParam0[0] = GetPartParam(layerParam.ec, 0)[0] / 180;
							param.pPartParam0[1] = GetPartParam(layerParam.ec, 0)[1] / 100;
							param.pPartParam0[2] = GetPartParam(layerParam.ec, 1)[0] / 180;
							param.pPartParam0[3] = GetPartParam(layerParam.ec, 1)[1] / 100;
							if (GetPartParam(layerParam.ec, 0)[6] < 0.5f)
							{
								param.pPartParam1[0] = GetPartParam(layerParam.ec, 0)[2];
							}
							else
							{
								param.pPartParam1[0] = GetPartParam(layerParam.ec, 0)[5] / 255;
							}
							param.pPartParam1[1] = GetPartParam(layerParam.ec, 0)[3];
							param.pPartParam1[2] = GetPartParam(layerParam.ec, 0)[4];
							param.pPartParam1[3] = GetPartParam(layerParam.ec, 0)[6] + 0.5f;
							if (GetPartParam(layerParam.ec, 1)[6] < 0.5f)
							{
								param.pPartParam2[0] = GetPartParam(layerParam.ec, 1)[2];
							}
							else
							{
								param.pPartParam2[0] = GetPartParam(layerParam.ec, 1)[5] / 255;
							}
							param.pPartParam2[1] = GetPartParam(layerParam.ec, 1)[3];
							param.pPartParam2[2] = GetPartParam(layerParam.ec, 1)[4];
							param.pPartParam2[3] = GetPartParam(layerParam.ec, 1)[6] + 0.5f;
						}

						if (!pRenderer->DrawPicture(param))
						{
							if (pic.pBigPic && pic.pBigPic->handle && pic.pBigPic->loaded)
							{
								if (!pRenderer->IsTextureExist(pic.pBigPic->handle))
								{
									ani->assureLoadPic(pic);
									pRenderer->DrawPicture(param);
								}
							}
						}

						if (layerParam.canvas)
						{
							NuclearRect rect(pic.offset*(m_pLogicSprite->m_fScale / layerParam.worldscale));
							rect += layerParam.loc;
							if ((ani->GetDirMode() == XPANIDM_8FROM5  && layerParam.dir > XPDIR_BOTTOM) ||
								(ani->GetDirMode() == XPANIDM_B2FROM1 && layerParam.dir == XPDIR_BOTTOMLEFT) ||
								(ani->GetDirMode() == XPANIDM_4FROM2  && layerParam.dir == XPDIR_BOTTOMLEFT) ||
								(ani->GetDirMode() == XPANIDM_4FROM2  && layerParam.dir == XPDIR_TOPRIGHT)
								)
							{
								long temp = (layerParam.loc.x << 1) - rect.left;
								rect.left = 2 * layerParam.loc.x - rect.right;
								rect.right = temp;
							}
							layerParam.canvas->Draw(m_pLogicSprite, rect);
						}
					}
				}
				else//非真实渲染
				{
					ani->GetPic(layerParam.dir, layerParam.linkframe, k, pic);
				}
			}
		}
	}

	void ComponentSprite::RenderSprite(BOOL renderAttachedEffect, Canvas* canvas, NuclearFRectt *pOutrect, bool isAlpha, bool bShake, bool realRender)
	{
		if (!m_pDefaultAction)
			return;

		if (m_pTmpAction == NULL && m_strCurrentActionRefName != L"" && m_strCurrentActionRefName != m_pLogicSprite->m_strDefaultActionName)
		{
			World* world = static_cast<World*>(GetEngine()->GetWorld());
			eNuclearWorldMode worldMode = world->GetWorldMode();
			bool bIsInBattle = worldMode == XPWM_SIMPLE_BATTLE || worldMode == XPWM_WORLD_BATTLE;
			if (!bIsInBattle)
			{
				return;
			}
		}
		//特效
		if (m_pLogicSprite->TestPropFlag(Sprite::XPSPPF_HAS_EFFECT_LAYER))
		{
			int d = (m_pLogicSprite->m_pEB->GetTick() - m_nBeginTick) % FIRE_EFFECT_CYCLE;
			d = d > FIRE_EFFECT_HALF_CYCLE ? FIRE_EFFECT_CYCLE - d : d;
			m_fEffectAlpha = m_fMinEffectAlpha + m_fSlopeEffectAlpha * d;
		}

		LayerParam layerParam;
		layerParam.canvas = canvas;
		layerParam.bIsEffectlayer = false;

		layerParam.iFlyScaleCur = m_pLogicSprite->GetFlyScaleCur();
		layerParam.iFlyOffsetYCur = m_pLogicSprite->GetFlyOffsetYCur();

		layerParam.location = m_pLogicSprite->GetLocation();
		updateShake(layerParam, bShake);//进行抖动的效果
		layerParam.loc = layerParam.location;

		const Action *pCurAction = NULL;
		Sprite::XPSPPropFlag asyncLoadingFlag;
		if (m_pTmpAction == NULL)
		{
			pCurAction = m_pDefaultAction;
			layerParam.bSync = !m_pLogicSprite->TestPropFlag(Sprite::XPSPPF_DEF_ACTION_ASYNC);
			asyncLoadingFlag = Sprite::XPSPPF_HAS_DEF_ASYNC_LOADING;
		}
		else
		{
			pCurAction = m_pTmpAction;
			layerParam.bSync = !m_pLogicSprite->TestPropFlag(Sprite::XPSPPF_CUR_ACTION_ASYNC);
			asyncLoadingFlag = Sprite::XPSPPF_HAS_CUR_ASYNC_LOADING;
		}

		// end update
		layerParam.dir = m_pLogicSprite->GetDirection();

		if (!pCurAction->TestDir(layerParam.dir)) return;

		layerParam.curActionName = pCurAction->GetLinkName(m_pLogicSprite->m_iCurFrame);
		if (layerParam.curActionName == L"")
			layerParam.curActionName = m_pTmpAction == NULL ? m_strDefaultActionRefName : m_strTmpActionRefName;
		layerParam.linkframe = pCurAction->GetLineFrame(m_pLogicSprite->m_iCurFrame);

		std::vector<int> ecorder(m_nLayerCount);

		pCurAction->GetFrameOrder(m_pLogicSprite->m_iCurFrame, layerParam.dir, &ecorder[0], m_nLayerCount);

		if (realRender)
		{
			DrawPictureParam param;
			NuclearColor color;
			NuclearFRectt rect;
			NuclearPoint vplt(m_pLogicSprite->m_pEB->GetViewport().left, m_pLogicSprite->m_pEB->GetViewport().top);
			if (m_pLogicSprite->TestPropFlag(Sprite::XPSPPF_SCREEN_COORD))
			{
				vplt.x = 0;
				vplt.y = 0;
				layerParam.worldscale = 1.0f;
			}
			else {
				layerParam.worldscale = m_pLogicSprite->m_pEB->GetWorldScale();
			}
			layerParam.location -= vplt;
			layerParam.location *= layerParam.worldscale;

			ConfigManager* pCfgMan = m_pLogicSprite->m_pEB->GetConfigManager();
			Renderer* pRenderer = m_pLogicSprite->m_pEB->GetRenderer();

			if (!layerParam.bSync)//异步，而且还没有在加载
			{
				bool needPrefech = false;
				bool basePic = false;
				bool bCanUseCurrentDirection = true;
				XAni::XPic pic;
				for (int i = 0; i < m_nLayerCount; ++i)
				{
					layerParam.ec = ecorder[i];
					if (layerParam.ec < 0 || layerParam.ec >= m_nLayerCount) continue;
					if (NULL == m_curComponent[layerParam.ec]) continue;
					if (!pCurAction->TestEquip(layerParam.ec)) continue;
					NuclearHardRef<XAni> ani = m_curComponent[layerParam.ec]->GetAni(m_pLogicSprite->m_modelName, layerParam.curActionName, true);
					if (ani.IsValid())
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
								for (int k = 0; k < ani->GetRegionCount(); ++k)
								{
									if (ani->GetPic(layerParam.dir, 0, k, pic) && pic.pBigPic && pic.pBigPic->handle == INVALID_PICTURE_HANDLE)
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
				if (m_pLogicSprite->TestPropFlag(asyncLoadingFlag))//如果正在加载
				{
					if (!needPrefech)//没有XPANISTATE_NOPICTURE了
					{
						if (basePic)//有的层还没有全部load完
						{
							layerParam.linkframe = 0;
							pCurAction->GetFrameOrder(layerParam.linkframe, layerParam.dir, &ecorder[0], m_nLayerCount);
							if (!bCanUseCurrentDirection)
							{
								for (int dir = XPDIR_TOP; dir < XPDIR_COUNT; ++dir)
								{
									if (pCurAction->TestDir(dir))
									{
										layerParam.dir = (NuclearDirection)dir;
										break;
									}
								}
							}
						}
						else {
							m_pLogicSprite->SetPropFlag(asyncLoadingFlag, false);
						}
					}
				}
				else {	//没有在加载
					if (needPrefech)
					{
						m_pLogicSprite->SetPropFlag(asyncLoadingFlag, true);
						PrefetchAction(pCurAction == m_pDefaultAction ? m_pLogicSprite->GetDefaultAction() : m_pLogicSprite->GetCurActionName());
					}
				}
				if (needPrefech || !bCanUseCurrentDirection)		//必须所有的层都准备好了才能绘制
					realRender = false;
			}
			else
			{
				//必须所有的层都准备好了才能绘制
				XAni::XPic pic;
				for (int i = 0; i < m_nLayerCount; ++i)
				{
					layerParam.ec = ecorder[i];
					if (layerParam.ec < 0 || layerParam.ec >= m_nLayerCount) continue;
					if (NULL == m_curComponent[layerParam.ec]) continue;
					if (!pCurAction->TestEquip(layerParam.ec)) continue;
					std::wstring layername = m_pLogicSprite->m_pEB->GetSpriteManager()->GetLayerName(m_pLogicSprite->m_modelName, layerParam.ec);					
					if (layername == L"weapon") continue;

					NuclearHardRef<XAni> ani = m_curComponent[layerParam.ec]->GetAni(m_pLogicSprite->m_modelName, layerParam.curActionName, !layerParam.bSync, m_bDyeEnable);
					if (ani.IsValid())
					{
						for (int k = 0; k < ani->GetRegionCount(); ++k)
						{
							if (!ani->GetPic(layerParam.dir, layerParam.linkframe, k, pic) || !pic.pBigPic || pic.pBigPic->handle == INVALID_PICTURE_HANDLE)
							{
								realRender = false;
								break;
							}
						}
					}
					else
					{
						realRender = false;
					}
					if(!realRender)
						break;
				}
			}
			if (!realRender)
				return;

			bool lastDrawEffect = false;
			NuclearFRectt outrect;
			outrect.left = static_cast<float>(INT_MAX);
			outrect.right = static_cast<float>(INT_MIN);
			outrect.top = outrect.left;
			outrect.bottom = outrect.right;

			layerParam.nAlpha = m_pLogicSprite->m_nAlpha;
			if (isAlpha && pCfgMan)
			{
				layerParam.nAlpha = static_cast<unsigned char>(layerParam.nAlpha * pCfgMan->GetMaskAlpha());
			}
			//画武器光特效
			if (m_pLogicSprite->TestPropFlag(Sprite::XPSPPF_HAS_EFFECT_LAYER))
			{
				switch (layerParam.dir)
				{
				case 0:
				case 5:
				case 6:
				case 7:
					//先画
					for (int i = 0; i < m_nLayerCount; ++i)
					{
						layerParam.ec = ecorder[i];
						if (layerParam.ec < 0 || layerParam.ec >= m_nLayerCount) continue;
						if (NULL == m_curComponent[layerParam.ec]) continue;
						if (!pCurAction->TestEquip(layerParam.ec)) continue;
						if (((1 << layerParam.ec) & m_flagEffectLayer) == 0) continue;//不是否特效层
						layerParam.bIsEffectlayer = true;
						if (m_vShaderType[layerParam.ec] < 0)
						{
							continue;
						}
						RenderALayer(layerParam, outrect, realRender);
					}
					break;
				default:
					lastDrawEffect = true;
					break;
				}
			}

			m_pLogicSprite->m_bRideAniIsReady = false; //重置一下这个变量，下面会重新检查. by liugeng
			int effectI = -1;
			for (int i = 0; i < m_nLayerCount; ++i)
			{
				layerParam.ec = ecorder[i];
				if (layerParam.ec < 0 || layerParam.ec >= m_nLayerCount) continue;
				if (NULL == m_curComponent[layerParam.ec]) continue;
				if (!pCurAction->TestEquip(layerParam.ec)) continue;
				layerParam.bIsEffectlayer = ((1 << layerParam.ec) & m_flagEffectLayer) != 0;//是否特效层
				if (layerParam.bIsEffectlayer)
				{
					effectI = i;
					continue;
				}
				if (m_vShaderType[layerParam.ec] < 0)
				{
					continue;
				}
				RenderALayer(layerParam, outrect, realRender);
			}

			//画武器光特效
			if (effectI >= 0 && lastDrawEffect)
			{
				layerParam.ec = ecorder[effectI];
				layerParam.bIsEffectlayer = true;
				if (m_vShaderType[layerParam.ec] == 0)
				{
					RenderALayer(layerParam, outrect, realRender);
				}
			}
			if (pOutrect)
			{
				*pOutrect = outrect;
			}
		}
		else//非真实渲染
		{
			if (!layerParam.bSync)//异步，而且还没有在加载
			{
				XAni::XPic pic;
				bool bCanUseCurrentDirection = true;
				for (int i = 0; i < m_nLayerCount; ++i)
				{
					NuclearHardRef<XAni> ani = m_curComponent[layerParam.ec]->GetAni(m_pLogicSprite->m_modelName, layerParam.curActionName, true);
					if (ani.IsValid())
					{
						if (bCanUseCurrentDirection && ani->GetState() == XAni::XPANISTATE_BASE_PIC)
						{
							for (int k = 0; k < ani->GetRegionCount(); ++k)
							{
								if (ani->GetPic(layerParam.dir, 0, k, pic) && pic.pBigPic && pic.pBigPic->handle == INVALID_PICTURE_HANDLE)
								{
									bCanUseCurrentDirection = false;
								}
							}
						}
					}
				}
			}
		}
	}

	void ComponentSprite::GetBase(NuclearSortBaseRectType &base)
	{
		//可以考虑把NuclearBase保存在ComponentSprite里面，这样不用每帧都做一次查表
		NuclearPoint loc = m_pLogicSprite->GetLocation();
		const NuclearBase& modelbase = m_pLogicSprite->m_pEB->GetSpriteManager()->GetBase(m_pLogicSprite->m_modelName);
		NuclearPoint tmpCPoint;
		TransToDiamondRadix(loc.x + modelbase.left.x, loc.y + modelbase.left.y, tmpCPoint);
		base.left = tmpCPoint.x;
		base.bottom = tmpCPoint.y;
		TransToDiamondRadix(loc.x + modelbase.right.x, loc.y + modelbase.right.y, tmpCPoint);
		base.right = tmpCPoint.x;
		base.top = tmpCPoint.y;
		base.srcRect.left = loc.x + modelbase.left.x * 2;
		base.srcRect.right = loc.x + modelbase.right.x * 2;
		base.srcRect.bottom = loc.y + 100;
		base.srcRect.top = loc.y - 500;
		base.y = loc.y + (modelbase.left.y + modelbase.right.y) / 2;
	}

	bool ComponentSprite::SetDurativeEffect(NuclearHardRef<C3DEffect> pEffect, const NuclearPoint& relpos, const std::wstring &hostname, bool async)
	{
		XPASSERT(false && L"ComponentSprite::SetDurativeEffect");
		return false;
	}

	void ComponentSprite::DumpAllActionNames(std::vector<std::wstring> &actions) const
	{
		if (m_pLogicSprite->m_modelName.size() == 0)
			return;
		m_pLogicSprite->m_pEB->GetSpriteManager()->DumpAction(m_pLogicSprite->m_modelName, actions);
	}

	void ComponentSprite::SetPartParam(int iLayerIndex, int iPartIndex, std::vector<float> vParam)
	{
		m_vPartParams[iLayerIndex][iPartIndex].resize(vParam.size());
		for (int i = 0; i < vParam.size(); i++)
		{
			m_vPartParams[iLayerIndex][iPartIndex][i] = vParam[i];
		}
	}
	std::vector<float> ComponentSprite::GetPartParam(int iLayerIndex, int iPartIndex)
	{
		return m_vPartParams[iLayerIndex][iPartIndex];
	}
	void ComponentSprite::SetDyePartIndex(int level, int part, int index)
	{
		if (m_bDyeEnable == false)	return;
		if (index > 0)
		{
			if (part > 0 && m_bDyeEnable <= 1)
				return;
			m_vShaderType[level] |= (part + 1);
		}
		else
			m_vShaderType[level] &= ~(part + 1);
		if (m_vShaderType[level] != 0 && index > 0)
		{
			SpriteManager* pSprMan = m_pLogicSprite->m_pEB->GetSpriteManager();
			SpriteManager::XModel* pModel = pSprMan->GetXModel(m_pLogicSprite->m_modelName);
			SpriteManager::XModel::DyeInfo dinfo;
			if (pModel->GetDyeParam(part, index - 1, dinfo) == true)
			{
				std::vector<float> vParam;
				vParam.push_back((float)dinfo.iH);
				vParam.push_back((float)dinfo.iS);
				vParam.push_back((float)dinfo.iR);
				vParam.push_back((float)dinfo.iG);
				vParam.push_back((float)dinfo.iB);
				vParam.push_back((float)dinfo.iBright);
				vParam.push_back((float)dinfo.iType);
				SetPartParam(level, part, vParam);
			}
		}
		else
		{
			std::vector<float> vParam;
			vParam.push_back(0.0f);
			vParam.push_back(0.0f);
			vParam.push_back(2.0f);
			vParam.push_back(3.0f);
			vParam.push_back(4.0f);
			vParam.push_back(0.0f);
			vParam.push_back(0.0f);
			SetPartParam(level, part, vParam);
		}
	}
	int ComponentSprite::GetDyePartCount()
	{
		SpriteManager* pSprMan = m_pLogicSprite->m_pEB->GetSpriteManager();
		SpriteManager::XModel* pModel = pSprMan->GetXModel(m_pLogicSprite->m_modelName);
		if (pModel == NULL)	return 0;

		return pModel->GetDyePartCount();
	}
	int ComponentSprite::GetDyeProjCount(int part)
	{
		SpriteManager* pSprMan = m_pLogicSprite->m_pEB->GetSpriteManager();
		SpriteManager::XModel* pModel = pSprMan->GetXModel(m_pLogicSprite->m_modelName);
		if (pModel == NULL)	return 0;

		return pModel->GetDyeProjCount(part);
	}
};