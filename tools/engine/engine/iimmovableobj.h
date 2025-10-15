#ifndef __Nuclear_IIMMOVABLEOBJ_H
#define __Nuclear_IIMMOVABLEOBJ_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "common\xptypes.h"
#include "iselectableobj.h"

namespace Nuclear
{
	class IImmovableObj : virtual public ISelectableObj
	{
	public:
		IImmovableObj(){}
		virtual ~IImmovableObj() { }

		// 设置位置。location 世界像素坐标。
		virtual void SetLocation(const Location& location) = 0;
		// 得到位置.
		virtual Location GetLocation() const = 0;

		// 设置和得到方向
		virtual void SetDirection( XPDIRECTION direction ) = 0;
		virtual XPDIRECTION GetDirection() const = 0;

		// 设置是否可见
		virtual void SetVisible(bool v) = 0; // default true
		virtual bool IsVisiable() const = 0;

		// 设置是否可见实体（如果SetVisible(false)了的话，这个不管怎么设都不可见）
		virtual void SetEntityVisible(bool v) = 0; // default true
		virtual bool IsEntityVisible() const = 0;

		// 设置是否可见名字（如果SetVisible(false)了的话，这个不管怎么设都不可见）
		virtual void SetTitleVisible(bool v) = 0; // default true
		virtual bool IsTitleVisible() const = 0;
		
		// 新版头顶文字，用UI来画
		virtual void SetTitle(EntitativeTitleHandle handle, XPTITLE_ALIGN align = XPTA_DEFAULT) = 0;
		virtual EntitativeTitleHandle GetTitle() = 0;
		virtual EntitativeTitleHandle UnsetTitle() = 0;
		virtual void SetTitleSize(int width, int height) = 0;
		virtual int GetTitleWidth() const = 0;
		virtual int GetTitleHeight() const = 0;

		// 是否高亮
		virtual void SetHighlight(bool b) = 0;
		virtual bool IsHighlight() const = 0;

		// 是否TestAlpha
		__declspec(deprecated) void SetTestAlpha(bool b) { SetMouseTestMode(b?XPTM_ALPHA:XPTM_POLYGON); }
		__declspec(deprecated) bool IsTestAlpha() const	{ return GetMouseTestMode() == XPTM_ALPHA; }

		// 检查能否选择ani的时候的模式
		virtual void SetMouseTestMode(XPMOUSE_TEST_MODE m) = 0;
		virtual XPMOUSE_TEST_MODE GetMouseTestMode() const = 0;

		virtual IEffect* SetEffect(const std::wstring &name) = 0;
		virtual void RemoveEffect(IEffect* pEffect) = 0;
	};


}

#endif