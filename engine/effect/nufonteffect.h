#ifndef __Nuclear_FONTEFFECT_H__
#define __Nuclear_FONTEFFECT_H__

#include "effect.h"
#include "../engine/renderable.h"
#include "../renderer/fontmanager.h"

namespace Nuclear
{
	class FontEffect :	public Effect //public IFontEffect, 
	{
	public:
	//	FontEffect(void);
		virtual ~FontEffect(void);

		FontEffect(EngineBase* pEB, TextBlockHandle textHandle, const sNuclearFontEffectParam& structFontParam) : Effect(pEB)
		{
			m_TextBlockHandle = textHandle;
			IFontManager* pIFontManager = pEB->GetRenderer()->GetFontManager();
			m_pITextBlock = pIFontManager->GetTextBlock(textHandle);

			m_fAge = 0.0f;

			m_structFontParam = structFontParam;
			if(structFontParam.pFrctSrc != NULL)
			{
				m_structFontParam.pFrctSrc = new NuclearFRectt();
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
				SetLoopNum(structFontParam.nLoopNum);//! 小于0表示无限循环
		}

		virtual bool Render(Canvas* canvas);
		//effect create出来的时候当前时间是负的，update的时候如果发现当前时间为负，就用0来update，并且改当前时间为0
		virtual bool Update(DWORD tickTime);	//返回false就代表播放完毕了

		virtual void Release();

		virtual Nuclear_EffectAssureResResult AssureResource(bool async) { return XPEARR_RESREADY; }
		virtual void SetScaleType(Nuclear_EffectScaleType t) { return; }
		virtual Nuclear_EffectScaleType GetScaleType() const { return XPEST_NOSCALE; }

		virtual int LoadAttribute(LJXML::LJXML_Node<LJXML::Char>* pNode) { return 0; }

		virtual void SetScreenCrood(bool b) { return; }
		virtual bool IsScreenCrood() const { return true; }

		virtual NuclearColor GetVertexColor() const { return XPCOLOR_WHITE; }
		virtual float GetTransparent() const { return 0.0f; }

		virtual const NuclearVector4& GetColorate() const 
		{ 
			static NuclearVector4 color(1.0f, 1.0f, 1.0f, 1.0f); 
			return color; 
		}

		virtual void SetVertexColor( NuclearColor color) { return; }
		virtual void SetTransparent( float alpha) { return; }
		virtual void Colorate( const NuclearVector4& color) { return; }

		// 特效挂点
		virtual void SetEffectBindType(Nuclear_EffectBindType t) { return; }
		virtual Nuclear_EffectBindType GetEffectBindType() const { return XPEBT_NULL; }

		virtual Nuclear_EffectType GetType() const { return XPET_ANI; }

		virtual int			GetPlayOnceTime() const { return 0; } //毫秒
		virtual bool		GetBase(NuclearBase &base) { return true; }

		virtual void		Clone(Effect* p) {}

	public:
		void ModifyFontParam(const sNuclearFontEffectParam& structFontParam) { m_structFontParam = structFontParam; }
	
	private:
		TextBlockHandle m_TextBlockHandle;
		ITextBlock* m_pITextBlock;
		
		sNuclearFontEffectParam m_structFontParam;
		float m_fAge;		 //特效在一个周期内播放了多久了.. 单位: 秒

		bool m_bZoomFlag;

		int	m_nPlayOnceTime;	//播放一遍的时间（毫秒）

	private:
		FontEffect(const FontEffect&);
		FontEffect& operator=(const FontEffect&);
	};
}

#endif