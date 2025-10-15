#ifndef __Nuclear_IMMOVABLEOBJ_H
#define __Nuclear_IMMOVABLEOBJ_H

#if _MSC_VER > 1000
#pragma once
#endif

#include "..\engine\animanager.h"
#include "..\iimmovableobj.h"
#include "entitativeobj.h"

namespace Nuclear
{
	
	class Effect;
	// 掉落物品，矿，柱子...等静态对象
	class ImmovableObj : public IImmovableObj, public EntitativeObj
	{
		typedef std::set<Effect*> EffectSet;
		typedef std::set<IEffect*> IEffectSet;
		Location m_loc;
		XPDIRECTION m_dir;
		std::wstring m_name;
		XPCOLOR m_color;

		int m_iCurFrame;
		int m_iAniDurationTime;
		int m_iAniStartTime;
		XWeakRef<XAni> m_ani;
		EffectSet m_pEffects;
		IEffectSet m_pDeletedIEffect;
		
		
	public:
		ImmovableObj(EngineBase *pEB, const std::wstring objname, XPCOLOR color, float freq);
		virtual ~ImmovableObj();

		// 是否高亮
		virtual void SetHighlight(bool b) { EntitativeObj::SetHighlight(b); }
		virtual bool IsHighlight() const { return EntitativeObj::IsHighlight(); }

		// 检查能否选择ani的时候的模式
		virtual void SetMouseTestMode(XPMOUSE_TEST_MODE m) { EntitativeObj::SetMouseTestMode(m); }
		virtual XPMOUSE_TEST_MODE GetMouseTestMode() const { return EntitativeObj::GetMouseTestMode(); }
		
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
		virtual void SetTitle(EntitativeTitleHandle handle, XPTITLE_ALIGN align = XPTA_DEFAULT) { EntitativeObj::SetTitle(handle, align); }
		virtual EntitativeTitleHandle GetTitle() { return EntitativeObj::GetTitle(); }
		virtual EntitativeTitleHandle UnsetTitle()  { return EntitativeObj::UnsetTitle(); }
		virtual void SetTitleSize(int width, int height) { EntitativeObj::SetTitleSize(width, height); }
		virtual int GetTitleWidth() const { return EntitativeObj::GetTitleWidth(); }
		virtual int GetTitleHeight() const { return EntitativeObj::GetTitleHeight(); }

		virtual IEffect* SetEffect(const std::wstring &name);
		virtual void RemoveEffect(IEffect* pEffect);
		void Update(DWORD delta);

		

	private:		
		virtual void RenderEntity(Canvas* canvas);

		virtual Location GetTitleLocation() const
		{
			XHardRef<XAni> hardani = m_ani;
			if (!hardani.IsValid())
				return m_loc;
			CRECT rect;
			hardani->GetBorder(rect);
			return m_loc + Location(0, rect.top);
		}
	public:

		virtual bool TestPoint(const Location &testloc, int r, float testval);
		virtual void SetLocation(const Location& location);
		virtual void SetDirection( XPDIRECTION direction );
		virtual Location GetLocation() const { return m_loc; }
		virtual XPDIRECTION GetDirection() const { return m_dir; }

	};

};

#endif
