#ifndef __Nuclear_RENDERABLE_H__
#define __Nuclear_RENDERABLE_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "guobj.h"
#include "enginebase.h"

namespace Nuclear
{
class Canvas;
class Renderable : public GUObject
{
protected:
	EngineBase *m_pEngine;
public:
	Renderable(EngineBase* pEB) : m_pEngine(pEB) {}
	virtual bool Render(Canvas* canvas) = 0;
	virtual ~Renderable() { }
};

struct DrawElement
{
	Renderable *m_pr;
	CRECT m_pos;
	DrawElement(Renderable *pr, CRECT pos) : m_pr(pr), m_pos(pos) { }
};

// todo 临时用来处理选择的cache, 将来会扩展为批绘制的中间层
class Canvas
{
	friend class World;
	std::vector<DrawElement> m_elements;
public:
	void Draw(Renderable* pr, CRECT pos)
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