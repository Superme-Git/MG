#pragma once
#include "InstallState.h"
#include "CheckDlg.h"


class CCheckState : public CInstallState
{
	CCheckDlg	m_dialog;
	
	CCheckState() { }
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

	static CCheckState& GetInstance()
	{
		static CCheckState state;
		return state;
	}
};