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

		// ����λ�á�location �����������ꡣ
		virtual void SetLocation(const Location& location) = 0;
		// �õ�λ��.
		virtual Location GetLocation() const = 0;

		// ���ú͵õ�����
		virtual void SetDirection( XPDIRECTION direction ) = 0;
		virtual XPDIRECTION GetDirection() const = 0;

		// �����Ƿ�ɼ�
		virtual void SetVisible(bool v) = 0; // default true
		virtual bool IsVisiable() const = 0;

		// �����Ƿ�ɼ�ʵ�壨���SetVisible(false)�˵Ļ������������ô�趼���ɼ���
		virtual void SetEntityVisible(bool v) = 0; // default true
		virtual bool IsEntityVisible() const = 0;

		// �����Ƿ�ɼ����֣����SetVisible(false)�˵Ļ������������ô�趼���ɼ���
		virtual void SetTitleVisible(bool v) = 0; // default true
		virtual bool IsTitleVisible() const = 0;
		
		// �°�ͷ�����֣���UI����
		virtual void SetTitle(EntitativeTitleHandle handle, XPTITLE_ALIGN align = XPTA_DEFAULT) = 0;
		virtual EntitativeTitleHandle GetTitle() = 0;
		virtual EntitativeTitleHandle UnsetTitle() = 0;
		virtual void SetTitleSize(int width, int height) = 0;
		virtual int GetTitleWidth() const = 0;
		virtual int GetTitleHeight() const = 0;

		// �Ƿ����
		virtual void SetHighlight(bool b) = 0;
		virtual bool IsHighlight() const = 0;

		// �Ƿ�TestAlpha
		__declspec(deprecated) void SetTestAlpha(bool b) { SetMouseTestMode(b?XPTM_ALPHA:XPTM_POLYGON); }
		__declspec(deprecated) bool IsTestAlpha() const	{ return GetMouseTestMode() == XPTM_ALPHA; }

		// ����ܷ�ѡ��ani��ʱ���ģʽ
		virtual void SetMouseTestMode(XPMOUSE_TEST_MODE m) = 0;
		virtual XPMOUSE_TEST_MODE GetMouseTestMode() const = 0;

		virtual IEffect* SetEffect(const std::wstring &name) = 0;
		virtual void RemoveEffect(IEffect* pEffect) = 0;
	};


}

#endif