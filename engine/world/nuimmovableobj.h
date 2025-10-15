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
	// ������Ʒ��������...�Ⱦ�̬����
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

		// �Ƿ����
		virtual void SetHighlight(bool b) { EntitativeObj::SetHighlight(b); }
		virtual bool IsHighlight() const { return EntitativeObj::IsHighlight(); }

		// ����ܷ�ѡ��ani��ʱ���ģʽ
		virtual void SetMouseTestMode(Nuclear_Mouse_TestMode m) { EntitativeObj::SetMouseTestMode(m); }
		virtual Nuclear_Mouse_TestMode GetMouseTestMode() const { return EntitativeObj::GetMouseTestMode(); }
		
		// �����Ƿ�ɼ�
		virtual void SetVisible(bool v) { EntitativeObj::SetVisible(v); }
		virtual bool IsVisiable() const { return EntitativeObj::IsVisiable(); }

		// �����Ƿ�ɼ�ʵ�壨���SetVisible(false)�˵Ļ������������ô�趼���ɼ���
		virtual void SetEntityVisible(bool v) { EntitativeObj::SetEntityVisible(v); }
		virtual bool IsEntityVisible() const { return EntitativeObj::IsEntityVisible(); }

		// �����Ƿ�ɼ����֣����SetVisible(false)�˵Ļ������������ô�趼���ɼ���
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
