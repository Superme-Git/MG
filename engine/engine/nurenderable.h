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
		virtual bool RenderBottom(Canvas* canvas) = 0;//����ǻ����������ͼ�������Ч��
		virtual bool RenderMid(Canvas* canvas, bool realRender) = 0;//����Ǻ���alpha�����һ�𻭵�
		virtual void RenderBeforeMid(bool realRender) {}	//����ǻ���ͼ�õ�
		virtual bool Render(Canvas* canvas, bool realRender) = 0;	//����ǻ��м������ģ�����XPSTT_SPRITE_TRANSLUCENT��XPSTT_NONE���в�
		virtual void RenderSpriteOnly(bool realRender) {}	//����͸��������XPSTT_SPRITE_TRANSLUCENT��ʱ���������ư�͸������
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