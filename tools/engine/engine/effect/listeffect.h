#ifndef __Nuclear_LISTEFFECT_H
#define __Nuclear_LISTEFFECT_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "effect.h"

namespace Nuclear
{
	class ListEffect :
		public Effect
	{
	private:
		typedef std::vector<Effect*> EffectList;
		EffectList m_effectList;
		size_t m_nowEffectPos;
		int m_nOnceTime;
		XPCOLOR m_vertexColor;
		XPEFFECT_SCALE_TYPE	m_scaleType;	//! 缩放控制
		bool m_bIsScreenCrood;

	private:
		bool CheckNowEffectIsOk() { return m_nowEffectPos < m_effectList.size(); }
	public:
		ListEffect(EngineBase* pEB);
		virtual ~ListEffect(void);

		//实现Effect的接口

		virtual XPEffectAssureResResult AssureResource(bool async);
		virtual void Release();

		virtual bool Update(DWORD tickTime);	//返回就代表播放完毕了
		virtual int LoadAttribute(XMLIO::CINode& node) { return 0; }	//反正listEffect不会保存为文件形式，更不会作为GEffect中的一种特效
		virtual int SaveAttribute(XMLIO::CONode& node) { return 0; }	//反正listEffect不会保存为文件形式，更不会作为GEffect中的一种特效
		virtual void SetEffectBindType(XPEFFECT_BIND_TYPE t) {}
		virtual XPEFFECT_BIND_TYPE GetEffectBindType() const;

		virtual bool Render(Canvas* canvas) 
		{
			if (CheckNowEffectIsOk())
			{
				return m_effectList[m_nowEffectPos]->Render(canvas);
			}
			return false;
		}
		//! 渲染前端（前端后渲染，也就是说能挡住人）
		virtual void RenderFront(Canvas* canvas)
		{
			if (CheckNowEffectIsOk())
			{
				m_effectList[m_nowEffectPos]->RenderFront(canvas);
			}
		}
		//! 渲染后端（后端先渲染，也就是说能被人挡住）
		virtual void RenderBack(Canvas* canvas)
		{
			if (CheckNowEffectIsOk())
			{
				m_effectList[m_nowEffectPos]->RenderBack(canvas);
			}
		}



		virtual XPCOLOR GetVertexColor() const { return m_vertexColor; }
		virtual float GetTransparent() const { return static_cast<float>(m_vertexColor.a) / 255.0f; }
		virtual const XPVECTOR4& GetColorate() const
		{
			static XPVECTOR4 color(1.0f, 1.0f, 1.0f, 1.0f);
			return color;
		}

		virtual void SetVertexColor( XPCOLOR color)  { m_vertexColor = color; }
		virtual void SetTransparent( float alpha) { m_vertexColor.a = static_cast<unsigned char>(alpha * 255); }

		//不支持曲线着色
		virtual void Colorate( const XPVECTOR4& color) {};
		virtual void SetScreenCrood(bool b);
		virtual bool IsScreenCrood() const { return m_bIsScreenCrood; }
		virtual void SetScaleType(XPEFFECT_SCALE_TYPE t);
		virtual XPEFFECT_SCALE_TYPE GetScaleType() const { return m_scaleType; }

		virtual XPEffectType GetType() const { return XPET_LISTEFFECT; }

		virtual int			 GetPlayOnceTime() const { return m_nOnceTime; }
		virtual bool		 GetBase(XPBase &base)
		{
			if (m_effectList.size() > 0)
			{
				return m_effectList[0]->GetBase(base);
			}
			return false;
		}

		virtual bool Play();
		virtual bool Stop();
		virtual bool Pause();
		virtual bool Resume();

	public:
		void PushAnEffect(Effect* pEffect);
		void NotifyAsyncLoadResult(bool succ) 
		{
			if (m_bIsScreenCrood && succ)//默认就不是屏幕坐标
			{
				EffectList::iterator it = m_effectList.begin(), ie = m_effectList.end();
				for (;it!=ie;++it)
				{
					(*it)->SetScreenCrood(m_bIsScreenCrood);
				}
			}
			Effect::NotifyAsyncLoadResult(succ); 
		}
	};
}

#endif