#pragma once
#include "InstallState.h"

#include "FolderDlg.h"
class CFolderState : public CInstallState
{
	CFolderDlg		m_dialog;
	CFolderState() { }
public:
	virtual void Show()
	{
		if( !::IsWindow( m_dialog.GetSafeHwnd()))
		{
			m_dialog.Create( m_parent);
			m_dialog.CenterWindow();
		}
		m_dialog.ShowWindow( SW_SHOW);
	}
	void SetInstallPath( const std::wstring& path) { m_parent->SetInstallPath( path); }
	static CFolderState& GetInstance()
	{
		static CFolderState state;
		return state;
	}
};