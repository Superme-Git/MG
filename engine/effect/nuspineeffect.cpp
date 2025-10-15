#include "nuspineeffect.h"
#include "engine/nuenginebase.h"
#include "../renderer/nucocos2d_render.h"
#include "utils/StringCover.h"

namespace Nuclear
{

	SpineEffect::SpineEffect(EngineBase* pEB, const std::wstring &name)
		: Effect(pEB)
		, m_pSkelAnim(0)
		, m_alpha(1.0f)
		, m_totalTime(0)
		, m_isUIEffect(false)
	{
		m_name = name;
		m_nCurTime = 0;
		m_curLoopNum = 0;
		m_defaultActName = "play";
		m_playMode = XPPM_LOOP;
		int slashPos = name.find_last_of(L"/");
		m_spinename = name.substr(slashPos + 1, name.length() - slashPos - 1);
		if (m_spinename.empty())
		{
			int test = 0;
		}
	}

	SpineEffect::~SpineEffect(void)
	{
		Release();
	}

	void SpineEffect::Release()
	{
		if (m_pSkelAnim)
		{
			m_pSkelAnim->release();
			m_pSkelAnim = NULL;
		}
		SpineManager* pSpineMan = m_pEB->GetSpineManager();
		pSpineMan->FreeSpineRes(m_spinename, this);
		m_state = XPES_EMPTY;
	}
	Nuclear_EffectAssureResResult SpineEffect::AssureResource(bool async)
	{
		if (m_state != XPES_EMPTY)
			return XPEARR_RESREADY;

		m_state = XPES_STOP;
		loadEffect(m_name, async);
		return XPEARR_RESREADY;
	}
	bool SpineEffect::Update(DWORD tickTime)
	{
		if (!m_bVisiblity) return true;
		if (m_state != XPES_PLAYING) return true;

		if (m_nCurTime < 0)
		{
			m_nCurTime = 0;
			tickTime = 0;
		}
		m_nCurTime += tickTime;

		if (m_nCurTime > m_totalTime)
		{
			++m_curLoopNum;
			NotifyEnd();
			m_nCurTime -= m_totalTime;

			switch (m_playMode)
			{
			case XPPM_TIME:
				if (m_curLoopNum >= 1)
				{
					Stop();
					return false;
				}
				break;
			case XPPM_LOOPNUM:
				if (m_curLoopNum >= m_nLoopNum)
				{
					Stop();
					return false;
				}
			case XPPM_LOOP:
				break;
			default:
				Stop();
				return false;
			}
		}

		if (m_pSkelAnim)
		{
			m_pSkelAnim->update(tickTime*0.001f);
		}

		return true;
	}

	bool SpineEffect::loadEffect(const std::wstring &name, bool async)
	{
		if (name.empty())
			return false;

		Release();

		int slashPos = name.find_last_of(L"/");
		std::wstring preName = name.substr(0, slashPos);;
		std::wstring uri = L"/effect/" + preName + L"/";
		SpineManager* pSpineMan = m_pEB->GetSpineManager();

		if (!async)
		{
			pSpineMan->SyncLoadSpineRes(m_spinename, uri, this);
		}
		else
		{
			pSpineMan->ASyncLoadSpineRes(m_spinename, uri, this);
		}

		return true;
	}

	bool SpineEffect::Render(Canvas* canvas, bool realRender)
	{
		if (!m_bVisiblity) return true;
		if (m_state == XPES_STOP || m_state == XPES_EMPTY)
			return false;

		if (m_pSkelAnim && realRender)
		{
			if (m_isUIEffect)
			{
				m_pSkelAnim->draw(0.0f, 0.0f, m_alpha);
			}
			else
			{
				float fWorldScale = m_pEB->GetWorldScale();

				m_pSkelAnim->setPosition((float)m_pos.x * fWorldScale, (float)m_pos.y * fWorldScale);
				m_pSkelAnim->setScaleX(m_scale.x * fWorldScale);
				m_pSkelAnim->setScaleY(-m_scale.y * fWorldScale);	// 因为坐标系不同，因此要做一次上下翻转

				NuclearRect vp = m_pEB->GetViewport();
				cocos2d::CCRect box = m_pSkelAnim->boundingBox();
				float left = box.origin.x;
				float right = box.origin.x + box.size.width;
				float top = box.origin.y;
				float bottom = box.origin.y + box.size.height;

				NuclearRect boundBox((int)(left / fWorldScale), (int)(top / fWorldScale), (int)(right / fWorldScale), (int)(bottom / fWorldScale));
				//NuclearRect boundBox((int)(left), (int)(top), (int)(right), (int)(bottom));
				NuclearRect resRect;

				if (vp.IsCross(boundBox, resRect))
				{
					m_pSkelAnim->draw((float)vp.left * fWorldScale, (float)vp.top * fWorldScale, m_alpha);
				}
			}
			return true;
		}

		return false;
	}

	void SpineEffect::SetLocation(const NuclearLocation& location)
	{
		m_pos = location;
		if (m_pSkelAnim)
		{
			if (m_isUIEffect)
			{
				m_pSkelAnim->setPosition((float)m_pos.x, (float)m_pos.y);
			}
			else
			{
				float fWorldScale = m_pEB->GetWorldScale();
				m_pSkelAnim->setPosition((float)m_pos.x * fWorldScale, (float)m_pos.y * fWorldScale);
				//m_pSkelAnim->setPosition((float)m_pos.x, (float)m_pos.y);
			}
		}
	}

	void SpineEffect::SetDefaultActName(const char* szActName)
	{
		m_defaultActName = szActName;
		if (m_pSkelAnim) {
			bool bLoop = m_playMode == XPPM_LOOP ? true : false;
			m_pSkelAnim->setAnimation(0, szActName, bLoop);
		}
	}

	void SpineEffect::SetIsUIEffect(bool b)
	{
		m_isUIEffect = b; 
		if (m_pSkelAnim)
		{
			if (m_isUIEffect)
			{
				m_pSkelAnim->setScaleX(m_scale.x);
				m_pSkelAnim->setScaleY(-m_scale.y);	// 因为坐标系不同，因此要做一次上下翻转
				m_pSkelAnim->setPosition((float)m_pos.x, (float)m_pos.y);
			}
			else
			{
				float fWorldScale = m_pEB->GetWorldScale();
				m_pSkelAnim->setScaleX(m_scale.x * fWorldScale);
				m_pSkelAnim->setScaleY(-m_scale.y * fWorldScale);	// 因为坐标系不同，因此要做一次上下翻转
				m_pSkelAnim->setPosition((float)m_pos.x * fWorldScale, (float)m_pos.y * fWorldScale);
			}
		}
	}
	void SpineEffect::SetScale(float scaleX, float scaleY)
	{
		m_scale.x = scaleX;
		m_scale.y = scaleY;
		if (m_pSkelAnim)
		{
			float fWorldScale = m_pEB->GetWorldScale();
			m_pSkelAnim->setScaleX(m_scale.x * fWorldScale);
			m_pSkelAnim->setScaleY(-m_scale.y * fWorldScale);	// 因为坐标系不同，因此要做一次上下翻转
		}
	}

	void SpineEffect::SetRotationRadian(float radian)
	{
		m_rotationRadian = radian;
		if (m_pSkelAnim)
		{
			m_pSkelAnim->setRotation(TOANGLE(m_rotationRadian));
		}
	}

	void SpineEffect::SetPlayMode(NuclearPlayMode mode)
	{
		m_playMode = mode;
		if (m_pSkelAnim)
		{
			m_pSkelAnim->setAnimation(0, m_defaultActName.c_str(), m_playMode == XPPM_LOOP ? true : false);
		}
	}

	bool SpineEffect::SetLoopNum(int loopNum)
	{
		if (loopNum < 0)
		{
			m_playMode = XPPM_LOOP;
			m_nLoopNum = -1;
			if (m_pSkelAnim)
			{
				m_pSkelAnim->setAnimation(0, m_defaultActName.c_str(), true);
			}
		}
		else {
			m_playMode = XPPM_LOOPNUM;
			m_nLoopNum = loopNum;
			if (m_pSkelAnim)
			{
				m_pSkelAnim->setAnimation(0, m_defaultActName.c_str(), false);
			}
		}
		return true;
	}

	void SpineEffect::Reset()
	{
		if (m_pSkelAnim)
			m_totalTime = int(1000 * m_pSkelAnim->getAnimationDuration(0, m_defaultActName.c_str()));

		SetLocation(m_pos);
		SetScale(m_scale.x, m_scale.y);
		SetRotationRadian(m_rotationRadian);
		SetPlayMode(m_playMode);
		SetLoopNum(m_nLoopNum);
		SetPlayState(XPES_STOP);
		Play();
	}

	void SpineEffect::OnLoaded(const std::wstring& wdir, SpineRes* spineRes)
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
}
