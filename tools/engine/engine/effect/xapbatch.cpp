#include "stdafx.h"
#include "..\engine\animanager.h"
#include "..\renderer\renderer.h"
#include "xapbatch.h"

namespace Nuclear
{
	XapBatch::XapBatch(EngineBase* pEB) : m_pEB(pEB), m_nCurFrameTime(0), m_nCurFrame(0), m_name(L""), m_AlphaMask(INVALID_PICTURE_HANDLE)
	{
	}

	XapBatch::~XapBatch(void)
	{
	}

	bool XapBatch::Update(DWORD tickTime)
	{
		if( !m_ani.IsValid()) return false;
		CheckHardAni();
		XHardRef<XAni> hardani = m_ani;
		if( hardani.IsValid() )
		{
			if( !hardani->TestDir(XPDIR_TOP) )
			{
				return false;
			}
			m_nCurFrameTime += tickTime;
			m_nCurFrame += m_nCurFrameTime / m_nFrameTime;
			m_nCurFrame %= hardani->GetFrameCount();
			m_nCurFrameTime %= m_nFrameTime;
		} else {
			return false;
		}
		return true;
	}

	void XapBatch::CheckHardAni()
	{
		XHardRef<XAni> hardani = m_ani;
		if( !hardani.IsValid() || hardani->GetState() == XAni::XPANISTATE_NO_XAP )
		{
			//这里是同步的，但是一般来说不会调用到
			m_ani = m_pEB->GetAniManager()->GetAni(L"/effect/" + m_name + SFI_ExtName, false);
		}
	}

	//! 渲染
	void XapBatch::Render()
	{
		if( !m_ani.IsValid())
			return;
		if (m_Rects.size() == 0)
			return;
		CheckHardAni();
		XHardRef<XAni> hardani = m_ani;
		if (hardani.IsValid())
		{
			if (hardani->GetState() == XAni::XPANISTATE_NOPICTURE)
			{
				m_pEB->GetAniManager()->SyncLoadAni(hardani);
			}
			hardani.SetTick(m_pEB->GetTick());
			XAni::XPic pic;
			DrawBatchPictureParam dpParam;
			dpParam.colorCount = 0;
			dpParam.pColor = NULL;
			CRECT viewport = m_pEB->GetViewport();
			dpParam.blend = hardani->GetBlend();
			XPRENDER_EFFECT oldEffect = m_pEB->GetRenderer()->GetRenderEffect();
			if (m_AlphaMask != Nuclear::INVALID_PICTURE_HANDLE)
			{
				dpParam.bSecSameuv = false;
				dpParam.pSecSrcRect = &(*m_SrcRects.begin());
				dpParam.secHandle = m_AlphaMask;
				m_pEB->GetRenderer()->SelectRenderEffect(XPRE_NULL);
			}
			if (dpParam.blend == XPTEXBM_ADDITIVE)
			{
				m_pEB->GetRenderer()->SelectRenderEffect(XPRE_NULL);
			}
			dpParam.pRect = &(*m_Rects.begin());
			dpParam.rectCount = static_cast<int>(m_Rects.size());
			dpParam.bSameuv = true;
			for(int k=0; k< hardani->GetRegionCount();++k)
			{
				if( hardani->GetPic(XPDIR_TOP, m_nCurFrame, k, pic) && pic.pBigPic && pic.pBigPic->handle != INVALID_PICTURE_HANDLE )
				{
					dpParam.handle = pic.pBigPic->handle;
					dpParam.pSrcrect = &pic.srcrect;
					m_pEB->GetRenderer()->DrawBatchPicture(dpParam);
				}
			}
			m_pEB->GetRenderer()->SelectRenderEffect(oldEffect);
		}
	}

	bool XapBatch::SetAni(const std::wstring &xapname)
	{
		if (m_name == xapname)
			return true;
		std::wstring uri = L"/effect/" + xapname + SFI_ExtName;
		XWeakRef<XAni> ani = m_pEB->GetAniManager()->GetAni(uri, false);
		XHardRef<XAni> hardani = ani;
		if( !hardani.IsValid())
			return false;
		m_nCurFrameTime = 0;
		m_nCurFrame = 0;
		m_ani = ani;
		m_name = xapname;
		m_nFrameTime = hardani->GetPlayTime() / hardani->GetFrameCount();
		return true;
	}

}