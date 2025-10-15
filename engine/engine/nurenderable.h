#ifndef __Nuclear_RENDERABLE_H__
#define __Nuclear_RENDERABLE_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "nuguobj.h"
#include "nuenginebase.h"

namespace Nuclear
{
	class Canvas;
	class Renderable : public GUObject
	{
	public:
		Renderable(EngineBase* pEB) : m_pEB(pEB) {}
		virtual bool RenderBottom(Canvas* canvas) = 0;//这个是画人物下面底图上面的特效的
		virtual bool RenderMid(Canvas* canvas, bool realRender) = 0;//这个是和有alpha的物件一起画的
		virtual void RenderBeforeMid(bool realRender) {}	//这个是画底图用的
		virtual bool Render(Canvas* canvas, bool realRender) = 0;	//这个是画中间层以外的，或者XPSTT_SPRITE_TRANSLUCENT、XPSTT_NONE的中层
		virtual void RenderSpriteOnly(bool realRender) {}	//当半透明方案是XPSTT_SPRITE_TRANSLUCENT的时候，用来绘制半透明精灵
		virtual ~Renderable() { }

	protected:
		EngineBase *m_pEB;
	};

	struct DrawElement
	{
		Renderable *m_pr;
		NuclearRect m_pos;
		DrawElement(Renderable *pr, NuclearRect pos) : m_pr(pr), m_pos(pos) { }
	};

	class Canvas
	{
		friend class World;
		std::vector<DrawElement> m_elements;
	public:
		void Draw(Renderable* pr, NuclearRect pos)
		{
			m_elements.push_back(DrawElement(pr,pos));
		}
		void Clear()
		{
			m_elements.clear();
		}
	};

}

#endif