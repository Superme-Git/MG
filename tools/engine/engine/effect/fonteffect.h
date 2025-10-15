#ifndef __Nuclear_FONTEFFECT_H
#define __Nuclear_FONTEFFECT_H

#if _MSC_VER > 1000
#pragma once
#endif


#include "effect.h"
#include "..\engine\renderable.h"
#include "..\renderer\fontmanager.h"

namespace Nuclear
{
	class FontEffect :	public Effect //public IFontEffect, 
	{
	public:
	//	FontEffect(void);
		virtual ~FontEffect(void);

		FontEffect(EngineBase* pEB, TextBlockHandle textHandle, const XPFontEffectParam& structFontParam) : Effect(pEB)
		{
			m_TextBlockHandle = textHandle;
			IFontManager* pIFontManager = pEB->GetRenderer()->GetFontManager();
			m_pITextBlock = pIFontManager->GetTextBlock(textHandle);

			m_fAge = 0.0f;

			m_structFontParam = structFontParam;
			if(structFontParam.pFrctSrc != NULL)
			{
				m_structFontParam.pFrctSrc = new FRECT();
				m_structFontParam.pFrctSrc->left = structFontParam.pFrctSrc->left;
				m_structFontParam.pFrctSrc->top = structFontParam.pFrctSrc->top;
				m_structFontParam.pFrctSrc->right = structFontParam.pFrctSrc->right;
				m_structFontParam.pFrctSrc->bottom = structFontParam.pFrctSrc->bottom;
			}
			else
				m_structFontParam.pFrctSrc = NULL;

			m_state = XPES_STOP;
			m_nPlayTime = static_cast<int>(structFontParam.fCycle * 1000);
			m_nPlayOnceTime = static_cast<int>(structFontParam.fCycle *1000);
			
			if(structFontParam.nPlayTime > 0)
				SetPlayTime(structFontParam.nPlayTime);
			else
				SetLoopNum(structFontParam.nLoopNum);//! С��0��ʾ����ѭ��
		}

		virtual bool Render(Canvas* canvas);
		//effect create������ʱ��ǰʱ���Ǹ��ģ�update��ʱ��������ֵ�ǰʱ��Ϊ��������0��update�����Ҹĵ�ǰʱ��Ϊ0
		virtual bool Update(DWORD tickTime);	//����false�ʹ����������

		virtual void Release();

		virtual XPEffectAssureResResult AssureResource(bool async) { return XPEARR_RESREADY; }
		virtual void SetScaleType(XPEFFECT_SCALE_TYPE t) { return; }
		virtual XPEFFECT_SCALE_TYPE GetScaleType() const { return XPEST_NOSCALE; }

		virtual int LoadAttribute(XMLIO::CINode& node) { return 0; }
		virtual int SaveAttribute(XMLIO::CONode& node) { return 0; }

		virtual void SetScreenCrood(bool b) { return; }
		virtual bool IsScreenCrood() const { return true; }

		virtual XPCOLOR GetVertexColor() const { return XPCOLOR_WHITE; }
		virtual float GetTransparent() const { return 0.0f; }

		virtual const XPVECTOR4& GetColorate() const 
		{ 
			static XPVECTOR4 color(1.0f, 1.0f, 1.0f, 1.0f); 
			return color; 
		}

		virtual void SetVertexColor( XPCOLOR color) { return; }
		virtual void SetTransparent( float alpha) { return; }
		virtual void Colorate( const XPVECTOR4& color) { return; }

		// ��Ч�ҵ�
		virtual void SetEffectBindType(XPEFFECT_BIND_TYPE t) { return; }
		virtual XPEFFECT_BIND_TYPE GetEffectBindType() const { return XPEBT_NULL; }

		virtual XPEffectType GetType() const { return XPET_ANI; }

		virtual int			 GetPlayOnceTime() const { return 0; } //����
		virtual bool		 GetBase(XPBase &base) { return true; }

	public:
		void ModifyFontParam(const XPFontEffectParam& structFontParam) { m_structFontParam = structFontParam; }
	
	private:
		TextBlockHandle m_TextBlockHandle;
		ITextBlock* m_pITextBlock;
		
		XPFontEffectParam m_structFontParam;
		float m_fAge;		 //��Ч��һ�������ڲ����˶����.. ��λ: ��

		bool m_bZoomFlag;

		int	m_nPlayOnceTime;	//����һ���ʱ�䣨���룩

	private:
		FontEffect(const FontEffect&);
		FontEffect& operator=(const FontEffect&);
	};
}

#endif