#ifndef __Nuclear_IMMOVABLEOBJ_H__
#define __Nuclear_IMMOVABLEOBJ_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "../engine/nuanimanager.h"
#include "../nuiimmovableobj.h"
#include "nuentitativeobj.h"

namespace Nuclear
{
	// 掉落物品，矿，柱子...等静态对象
	class ImmovableObj : public IImmovableObj, public EntitativeObj
	{
		NuclearLocation m_loc;
		NuclearDirection m_dir;
		std::wstring m_name;
		NuclearColor m_color;

		int m_iCurFrame;
		int m_iAniDurationTime;
		int m_iAniStartTime;
		NuclearWeakRef<XAni> m_ani;
		bool m_bLookupSegmpak;
		
	public:
		ImmovableObj(EngineBase *pEB, const std::wstring objname, NuclearColor color, float freq);
		virtual ~ImmovableObj();

		void Update(DWORD delta);

		bool IsInSegmPak() const { return m_bLookupSegmpak; }
		void SetSegmPak(bool b) { m_bLookupSegmpak = b;}

		// 是否高亮
		virtual void SetHighlight(bool b) { EntitativeObj::SetHighlight(b); }
		virtual bool IsHighlight() const { return EntitativeObj::IsHighlight(); }

		// 检查能否选择ani的时候的模式
		virtual void SetMouseTestMode(Nuclear_Mouse_TestMode m) { EntitativeObj::SetMouseTestMode(m); }
		virtual Nuclear_Mouse_TestMode GetMouseTestMode() const { return EntitativeObj::GetMouseTestMode(); }
		
		// 设置是否可见
		virtual void SetVisible(bool v) { EntitativeObj::SetVisible(v); }
		virtual bool IsVisiable() const { return EntitativeObj::IsVisiable(); }

		// 设置是否可见实体（如果SetVisible(false)了的话，这个不管怎么设都不可见）
		virtual void SetEntityVisible(bool v) { EntitativeObj::SetEntityVisible(v); }
		virtual bool IsEntityVisible() const { return EntitativeObj::IsEntityVisible(); }

		// 设置是否可见名字（如果SetVisible(false)了的话，这个不管怎么设都不可见）
		virtual void SetTitleVisible(bool v) { EntitativeObj::SetTitleVisible(v); }
		virtual bool IsTitleVisible() const { return EntitativeObj::IsTitleVisible(); }

		//title
		virtual void SetTitle(EntitativeTitleHandle handle, NuclearTitleAlign align = XPTA_DEFAULT) { EntitativeObj::SetTitle(handle, align); }
		virtual EntitativeTitleHandle GetTitle() { return EntitativeObj::GetTitle(); }
		virtual EntitativeTitleHandle UnsetTitle()  { return EntitativeObj::UnsetTitle(); }
		virtual void SetTitleSize(int width, int height) { EntitativeObj::SetTitleSize(width, height); }
		virtual int GetTitleWidth() const { return EntitativeObj::GetTitleWidth(); }
		virtual int GetTitleHeight() const { return EntitativeObj::GetTitleHeight(); }

		virtual bool TestPoint(const NuclearLocation &testloc, int r, float testval);
		virtual void SetLocation(const NuclearLocation& location);
		virtual void SetDirection( NuclearDirection direction );
		virtual NuclearLocation GetLocation() const { return m_loc; }
		virtual NuclearDirection GetDirection() const { return m_dir; }

	private:
		virtual void RenderEntityBottom(Canvas* canvas) { }
		virtual void RenderEntity(Canvas* canvas, bool realRender);

		virtual NuclearLocation GetTitleLocation() const
		{
			NuclearHardRef<XAni> hardani = m_ani;
			if (!hardani.IsValid())
				return m_loc;
			NuclearRect rect;
			hardani->GetBorder(rect);
			return m_loc + NuclearLocation(0, rect.top);
		}
	};

}

#endif
