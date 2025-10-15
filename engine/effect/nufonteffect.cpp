//#include "FontEffect.h"
//#include "..\common\log.h"

namespace Nuclear
{
// 	FontEffect::FontEffect(void) : Effect(NULL)
// 	{
// 		m_TextBlockHandle = INVALID_TEXTBLOCK_HANDLE;
// 		m_pITextBlock = NULL;
// 	}

//	FontEffect::~FontEffect(void)
//	{
//		m_pEB->GetRenderer()->GetFontManager()->ReleaseTextBlock(m_TextBlockHandle);
//		if(m_structFontParam.pFrctSrc != NULL)
//		{
//			delete m_structFontParam.pFrctSrc;
//			m_structFontParam.pFrctSrc = NULL;
//		}
//	}
//
//	bool FontEffect::Render(Canvas* canvas)
//	{
//		Renderer* pRender = m_pEB->GetRenderer();
//
//		m_pITextBlock->Render( pRender, m_pos.ToFPOINT(), m_scale, m_structFontParam.pFrctSrc );
//		return true;
//	}
//
//	bool FontEffect::Update(DWORD tickTime)
//	{
//		if(m_state != XPES_PLAYING)
//			return true;
//		if(NULL == m_pITextBlock)
//			return false;
//
//		float fLife = m_structFontParam.fCycle;
//		if(m_fAge>fLife)
//		{
//			m_bZoomFlag = false;
//		}
//		if(m_fAge<0.0f)
//		{
//			m_bZoomFlag = true;
//		}
//
//		float fdelta = tickTime/1000.0f;
//
//		m_fAge = m_bZoomFlag ? (m_fAge+fdelta):(m_fAge-fdelta);
//
//		float fcoef = m_fAge/fLife;
//		fcoef = fcoef>1.0f ? 1.0f:fcoef;
//		fcoef = fcoef<0.0f ? 0.0f:fcoef;
//
//		m_scale = Lerpf(m_structFontParam.fMinScale, m_structFontParam.fMaxScale, fcoef);
//
//		m_nCurTime += tickTime;
//		if (m_playMode == XPPM_TIME)
//		{
//			if (m_nCurTime >= m_nPlayTime)
//			{
//				NotifyEnd();
//				Stop();
//				return false;
//			}
//		} 
//		else 
//		{
//			if (m_nCurTime >= m_nPlayOnceTime)
//			{
//				NotifyEnd();
//				++m_curLoopNum;
//				if ((m_playMode == XPPM_LOOPNUM) && (m_curLoopNum >= m_nLoopNum))
//				{
//					Stop();
//					return false;
//				}
//				m_nCurTime -= m_nPlayOnceTime;
//			}
//		}
//
//		return true;
//	}
//
//	void FontEffect::Release()
//	{
//	}

}
