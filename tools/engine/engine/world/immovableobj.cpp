#include "stdafx.h"
#include "..\common\log.h"
#include "..\renderer\renderer.h"
#include "..\effect\effectmanager.h"
#include "..\effect\particleeffect.h"
#include "immovableobj.h"


namespace Nuclear
{

	ImmovableObj::ImmovableObj(EngineBase *pEB, const std::wstring objname, XPCOLOR color, float freq)
		: EntitativeObj(pEB), m_name(objname), m_color(color),
		m_iCurFrame(0), m_iAniDurationTime(0), m_iAniStartTime(0)
	{
		std::wstring uri = m_name + SFI_ExtName;
		m_ani = m_pEngine->GetAniManager()->GetAni(uri, false);
		XHardRef<XAni> hardani = m_ani;
		if( !hardani.IsValid())
			return;

		m_iAniDurationTime = (int)(hardani->GetPlayTime() * freq);
		m_iAniStartTime = m_pEngine->GetTick();
	}

	ImmovableObj::~ImmovableObj()
	{
		EffectSet::iterator it = m_pEffects.begin(), ie = m_pEffects.end();
		for (;it!=ie;++it)
		{
			delete (*it);
		}
	}

	bool ImmovableObj::TestPoint(const Location &testloc, int r, float testval)
	{
		if (m_mouseTestMode == XPTM_PIC_RECT)
			return true;

		XPASSERT(r <= 2);
		if (TestTitleAlpha(testloc, r))
			return true;
		if( !m_ani.IsValid()) return false;

		XHardRef<XAni> hardani = m_ani;
		if( !hardani.IsValid()) return false;
		if( !hardani->TestDir(GetDirection()) ) return false;

		return hardani->TestPoint(GetDirection(), m_iCurFrame, 
			testloc - GetLocation(), m_mouseTestMode, 
			m_pEngine->GetRenderer(), r, testval);
	}

	void ImmovableObj::Update(DWORD delta)
	{
		IEffectSet::iterator dIe = m_pDeletedIEffect.end();
		EffectSet::iterator it = m_pEffects.begin(), ie = m_pEffects.end();
		for (;it!=ie;)
		{
			if (m_pDeletedIEffect.find(*it) == dIe)
			{
				(*it)->Update(delta);
				++it;
			} else {
				delete *it;
				m_pEffects.erase(it++);
			}
		}
		m_pDeletedIEffect.clear();
	}

	IEffect* ImmovableObj::SetEffect(const std::wstring &name)
	{
		if (name.size() == 0)
			return NULL;
		Effect* pEffect = m_pEngine->GetEffectManager()->CreateEffect(name, true);
		if (!pEffect)
			return NULL;
		pEffect->AssureResource(true);
		pEffect->SetDirection(GetDirection());
		pEffect->SetLocation(GetLocation());
		m_pEffects.insert(pEffect);
		return pEffect;
	}

	void ImmovableObj::RemoveEffect(IEffect* pEffect)
	{
		pEffect->Stop();
		m_pDeletedIEffect.insert(pEffect);
	}

	void ImmovableObj::SetLocation(const Location& location)
	{
		m_loc = location;
		EffectSet::iterator it = m_pEffects.begin(), ie = m_pEffects.end();
		for (;it!=ie;++it)
		{
			(*it)->SetLocation(location);
		}
	}

	void ImmovableObj::SetDirection( XPDIRECTION direction )
	{
		m_dir = direction;
		EffectSet::iterator it = m_pEffects.begin(), ie = m_pEffects.end();
		for (;it!=ie;++it)
		{
			(*it)->SetDirection(direction);
		}
	}

	void ImmovableObj::RenderEntity(Canvas* canvas)
	{
		if( !m_ani.IsValid()) return;

		int oldframe = m_iCurFrame;
		int tick = m_pEngine->GetTick();
		int d = tick - m_iAniStartTime;
		if( d > m_iAniDurationTime )
		{
			m_iCurFrame = 0;
			m_iAniStartTime = m_pEngine->GetTick();
			d = 0;
		}

		float r = d / (float)m_iAniDurationTime;
		XHardRef<XAni> hardani = m_ani;
		if( !hardani.IsValid() || hardani->GetState() == XAni::XPANISTATE_NO_XAP )
		{
			m_ani = m_pEngine->GetAniManager()->GetAni(m_name + SFI_ExtName, false);
			hardani = m_ani;
		}

		if( hardani.IsValid() )
		{
			int n = hardani->GetFrameCount();
			int i = (int)(r * n);
			if( i >= n ) i = n- 1;
			m_iCurFrame = i >0?i:0;
		}
		else
			return;

		if( !hardani->TestDir(m_dir) ) 
			return;

		XAni::XPic pic;
		XPBase base;
		float scale = m_pEngine->GetWorldScale();
		if( hardani->GetBase(base) )
		{
			switch (hardani->GetState())
			{
			case XAni::XPANISTATE_NOPICTURE:
				m_pEngine->GetAniManager()->SyncLoadAni(hardani);
				break;
			case XAni::XPANISTATE_NULL:
			case XAni::XPANISTATE_BASE_PIC:
				return;
				break;
			}
			hardani.SetTick(tick);
			DrawPictureParam dpParam;
			dpParam.blend = m_bIsHighlight?XPTEXBM_MODULATE2X:XPTEXBM_MODULATE;
			dpParam.colorCount = 1;
			for(int k=0; k< hardani->GetRegionCount();++k)
			{
				if( hardani->GetPic(m_dir, m_iCurFrame, k, pic) && pic.pBigPic && pic.pBigPic->handle != INVALID_PICTURE_HANDLE)
				{
					FRECT rect(pic.offset.ToFRECT());
					CRECT viewport = m_pEngine->GetViewport();
					rect += m_loc;
					rect.left -= viewport.left;
					rect.right -= viewport.left;
					rect.top -= viewport.top;
					rect.bottom -= viewport.top;
					rect *= scale;
					if( hardani->GetDirMode() == XPANIDM_8FROM5 && m_dir > XPDIR_BOTTOM )
					{
						rect.left = 2*(m_loc.x - viewport.left) * scale - rect.left;
						rect.right = 2*(m_loc.x - viewport.left) * scale - rect.right;
					}

					dpParam.handle = pic.pBigPic->handle;
					dpParam.pRect = &rect;
					dpParam.pColor = &m_color;
					dpParam.blend = hardani->GetBlend();
					dpParam.pSrcrect = &pic.srcrect;
					m_pEngine->GetRenderer()->DrawPicture(dpParam);

					if( canvas )
					{
						CRECT rect(pic.offset);
						rect += m_loc;
						if( hardani->GetDirMode() == XPANIDM_8FROM5 && m_dir > XPDIR_BOTTOM )
						{
							rect.left = 2*m_loc.x - rect.left;
							rect.right = 2*m_loc.x - rect.right;
						}
						canvas->Draw(this, rect);
					}
				}
			}

			EffectSet::iterator it = m_pEffects.begin(), ie = m_pEffects.end();
			Effect *pEffect;
			ParticleEffect *pParEffect;

			for (;it!=ie;++it)
			{
				pEffect = *it;
				pParEffect = pEffect->TryConvertToParticleEffect();
				if (pParEffect)
				{
					pParEffect->CollectRender();
				} else {
					pEffect->Render(NULL);
				}
			}
		}
	}

}
