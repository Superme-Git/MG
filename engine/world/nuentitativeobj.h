#ifndef __Nuclear_ENTITATIVEOBJ_H__
#define __Nuclear_ENTITATIVEOBJ_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "../nuiselectableobj.h"
#include "../engine/nurenderable.h"

namespace Nuclear
{
	enum XP_VISIBLE_SIGN
	{
		XP_VIS_VIS = 1,
		XP_VIS_ENTITY = 2,
		XP_VIS_TITLE = 4,
		XP_VIS_ALL = 0xFF,
	};

	class EntitativeObj : virtual public ISelectableObj, public Renderable
	{
	protected:
		EntitativeTitleHandle m_titleHandle;
		Nuclear_Mouse_TestMode m_mouseTestMode;
		NuclearTitleAlign m_titleAlign;		//这个以及下面两个只用来计算title的框的位置来检测点击用的，不用来绘制
		int	m_nTitleWidth;
		int m_nTitleHeight;
		unsigned char m_nVisible;
		bool m_bIsHighlight;			//是否高亮

	public:
		EntitativeObj(EngineBase *pEB);
		virtual ~EntitativeObj() {}

	public:
		// 检查能否选择ani的时候的模式
		virtual void SetMouseTestMode(Nuclear_Mouse_TestMode m) { m_mouseTestMode = m; }
		virtual Nuclear_Mouse_TestMode GetMouseTestMode() const { return m_mouseTestMode; }

		// 设置是否可见
		virtual void SetVisible(bool v)
		{
			if (v)
				m_nVisible |= XP_VIS_VIS;
			else
				m_nVisible &= ~XP_VIS_VIS;
		}
		virtual bool IsVisiable() const { return (m_nVisible & XP_VIS_VIS) != 0; }

		// 设置是否可见实体（如果SetVisible(false)了的话，这个不管怎么设都不可见）
		virtual void SetEntityVisible(bool v)
		{
			if (v)
				m_nVisible |= XP_VIS_ENTITY;
			else
				m_nVisible &= ~XP_VIS_ENTITY;
		}
		virtual bool IsEntityVisible() const { return (m_nVisible & XP_VIS_ENTITY) != 0; }

		// 设置是否可见名字（如果SetVisible(false)了的话，这个不管怎么设都不可见）
		virtual void SetTitleVisible(bool v)
		{
			if (v)
				m_nVisible |= XP_VIS_TITLE;
			else
				m_nVisible &= ~XP_VIS_TITLE;
		}
		virtual bool IsTitleVisible() const { return (m_nVisible & XP_VIS_TITLE) != 0; }

		virtual void SetTitle(EntitativeTitleHandle handle, NuclearTitleAlign align = XPTA_DEFAULT);
		virtual EntitativeTitleHandle GetTitle() { return m_titleHandle; }
		virtual EntitativeTitleHandle UnsetTitle();
		virtual void SetTitleSize(int width, int height) { m_nTitleWidth = width; m_nTitleHeight = height; }
		virtual int GetTitleWidth() const { return m_nTitleWidth; }
		virtual int GetTitleHeight() const { return m_nTitleHeight; }

		virtual bool RenderBottom(Canvas* canvas)
		{
			if (m_nVisible & XP_VIS_VIS)
			{
				if (m_nVisible & XP_VIS_ENTITY)	RenderEntityBottom(canvas);
			}
			return true;
		}
		
		virtual bool RenderMid(Canvas* canvas, bool realRender){return Render(canvas, realRender);}

		virtual bool Render(Canvas* canvas, bool realRender)
		{
			if (m_nVisible & XP_VIS_VIS)
			{
				if (m_nVisible & XP_VIS_ENTITY)	RenderEntity(canvas, realRender);
				if (m_nVisible & XP_VIS_TITLE) RenderTitle(canvas, realRender);
			}
			return true;
		}

		// 名字暂定，为了拾取准备的接口, r为检测半径
		virtual bool TestPoint(const NuclearLocation &testloc, int r, float testval) = 0;

		// 是否高亮
		void SetHighlight(bool b) { m_bIsHighlight = b; }
		bool IsHighlight() const { return m_bIsHighlight; }

	protected:
		bool TestTitleAlpha(const NuclearLocation &testloc, int r);
		// 名字暂定，确定title的位置
		virtual NuclearLocation GetTitleLocation() const = 0;

	private:
		// 名字暂定，在title之前渲染除了title之外的图形，且不需判断visible
		virtual void RenderEntityBottom(Canvas* canvas) = 0;
		virtual void RenderEntity(Canvas* canvas, bool realRender) = 0;

	private:
		void RenderTitle(Canvas* canvas, bool realRender);

	};

}

#endif
