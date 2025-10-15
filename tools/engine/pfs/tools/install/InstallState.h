#pragma once
#include <string>
#include "../../pfs2.h"
#include "InstallDlg.h"
#include "../packzip/KHLogger.h"
class CInstallState
{
protected:
	CInstallState() : m_parent( 0) {}
	CInstallDlg*	m_parent;

public:
	void SetInstallDlg( CInstallDlg* parent) { m_parent = parent; }

	virtual void Show() = 0;
	void ChangeState( CInstallState& state) { m_parent->ChangeState( state); }
	void Exit() { PostQuitMessage( 0); }
	
	// 是否可以切换全屏
	virtual bool CanToggleFullsceen() const
	{ return true; };

	enum
	{
		ERROR_USERCANCEL = -3000,
		ERROR_CHECK,
		ERROR_MD5FILE,
		ERROR_NOTSUSPENDOS64,
	};

	static const std::wstring& GetAppPathStr()
	{
		static std::wstring str = L"/apppath";
		return str;
	}
	static const std::wstring& GetResourceStr()
	{
		static std::wstring str = L"/resource";
		return str;
	}
};