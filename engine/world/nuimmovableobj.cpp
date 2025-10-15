#include "../common/nulog.h"
#include "../renderer/nurenderer.h"
#include "nuimmovableobj.h"


namespace Nuclear
{

	ImmovableObj::ImmovableObj(EngineBase *pEB, const std::wstring objname, NuclearColor color, float freq)
		: EntitativeObj(pEB), m_name(objname), m_color(color), m_iCurFrame(0), m_iAniDurationTime(0), m_iAniStartTime(0), m_bLookupSegmpak(false)
	{
		std::wstring uri = m_name + SFI_ExtName;
		m_ani = m_pEB->GetAniManager()->GetAni(uri, false, m_bLookupSegmpak);
		NuclearHardRef<XAni> hardani = m_ani;
		if (!hardani.IsValid()) return;

		m_iAniDurationTime = (int)(hardani->GetPlayTime() * freq);
		m_iAniStartTime = m_pEB->GetTick();
	}

	ImmovableObj::~ImmovableObj()
	{
	}

	bool ImmovableObj::TestPoint(const NuclearLocation &testloc, int r, float testval)
	{
		if (m_mouseTestMode == XPTM_PIC_RECT)
			return true;

		XPASSERT(r <= 2);
		if (TestTitleAlpha(testloc, r))
			return true;
		if (!m_ani.IsValid()) return false;

		NuclearHardRef<XAni> hardani = m_ani;
		if (!hardani.IsValid()) return false;
		if (!hardani->TestDir(GetDirection())) return false;

		return hardani->TestPoint(GetDirection(), m_iCurFrame,
			testloc - GetLocation(), m_mouseTestMode,
			m_pEB->GetRenderer(), r, testval);
	}

	void ImmovableObj::Update(DWORD delta)
	{

	}

	void ImmovableObj::SetLocation(const NuclearLocation& location)
	{
		m_loc = location;
	}

	void ImmovableObj::SetDirection(NuclearDirection direction)
	{
		m_dir = direction;
	}

	void ImmovableObj::RenderEntity(Canvas* canvas, bool realRender)
	{
		if (!m_ani.IsValid()) return;

		int oldframe = m_iCurFrame;
		int tick = m_pEB->GetTick();
		int d = tick - m_iAniStartTime;
		if (d > m_iAniDurationTime)
		{
			m_iCurFrame = 0;
			m_iAniStartTime = m_pEB->GetTick();
			d = 0;
		}

		float r = d / (float)m_iAniDurationTime;
		NuclearHardRef<XAni> hardani = m_ani;
		if (!hardani.IsValid() || hardani->GetState() == XAni::XPANISTATE_NO_XAP)
		{
			m_ani = m_pEB->GetAniManager()->GetAni(m_name + SFI_ExtName, false, m_bLookupSegmpak);
			hardani = m_ani;
		}

		if (hardani.IsValid())
		{
			hardani.SetTick(tick);
			int n = hardani->GetFrameCount();
			int i = (int)(r * n);
			if (i >= n) i = n - 1;
			m_iCurFrame = i > 0 ? i : 0;
		}
		else
			return;

		if (!hardani->TestDir(m_dir))
			return;

		XAni::XPic pic;
		NuclearBase base;
		float scale = m_pEB->GetWorldScale();
		if (hardani->GetBase(base))
		{
			switch (hardani->GetState())
			{
			case XAni::XPANISTATE_NOPICTURE:
				//m_pEB->GetAniManager()->SyncLoadAni(hardani, m_bLookupSegmpak);
				break;
			case XAni::XPANISTATE_NULL:
			case XAni::XPANISTATE_BASE_PIC:
				return;
				break;
			}
			if (realRender)
			{
				DrawPictureParam dpParam;
				dpParam.blend = m_bIsHighlight ? XPTEXBM_MODULATE2X : XPTEXBM_MODULATE;
				dpParam.colorCount = 1;
				for (int k = 0; k< hardani->GetRegionCount(); ++k)
				{
					if (hardani->GetPic(m_dir, m_iCurFrame, k, pic) && pic.pBigPic && pic.pBigPic->handle != INVALID_PICTURE_HANDLE)
					{
						NuclearFRectt rect(pic.offset.ToFRECT());
						NuclearRect viewport = m_pEB->GetViewport();
						rect += m_loc;
						rect.left -= viewport.left;
						rect.right -= viewport.left;
						rect.top -= viewport.top;
						rect.bottom -= viewport.top;
						rect *= scale;
						if ((hardani->GetDirMode() == XPANIDM_8FROM5 && m_dir > XPDIR_BOTTOM) ||
							(hardani->GetDirMode() == XPANIDM_B2FROM1 && m_dir == XPDIR_BOTTOMLEFT))
						{
							rect.left = 2 * (m_loc.x - viewport.left) * scale - rect.left;
							rect.right = 2 * (m_loc.x - viewport.left) * scale - rect.right;
						}

						dpParam.handle = pic.pBigPic->handle;
						dpParam.pRect = &rect;
						dpParam.pColor = &m_color;
						dpParam.blend = hardani->GetBlend();
						dpParam.pSrcrect = &pic.srcrect;
						m_pEB->GetRenderer()->DrawPicture(dpParam);

						if (canvas)
						{
							NuclearRect rect(pic.offset);
							rect += m_loc;
							if ((hardani->GetDirMode() == XPANIDM_8FROM5 && m_dir > XPDIR_BOTTOM) ||
								(hardani->GetDirMode() == XPANIDM_B2FROM1 && m_dir == XPDIR_BOTTOMLEFT))
							{
								long temp = (m_loc.x << 1) * scale - rect.left;
								rect.left = (2 * m_loc.x)*scale - rect.right;
								rect.right = temp;
							}
							canvas->Draw(this, rect);
						}
					}
				}
			}
			else//∑«’Ê µ‰÷»æ
			{
				for (int k = 0; k < hardani->GetRegionCount(); ++k)
				{
					hardani->GetPic(m_dir, m_iCurFrame, k, pic);
				}
			}
		}
	}

}
