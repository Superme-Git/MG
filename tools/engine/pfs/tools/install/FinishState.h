#pragma once
#include "FinishDlg.h"
#include "InstallState.h"

class CFinishState : public CInstallState
{
	CFinishDlg		m_dialog;
	CFinishState() {}
public:
	virtual void Show()
	{
		m_dialog.Create( m_parent);
		m_dialog.CenterWindow();
		m_dialog.ShowWindow( SW_SHOW);
	}
	const std::wstring& GetInstallPath() { return m_parent->GetInstallPath(); }
	static CFinishState& GetInstance()
	{
		static CFinishState state;
		return state;
	}
};