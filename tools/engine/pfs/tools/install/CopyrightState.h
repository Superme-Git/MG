#pragma once
#include "InstallState.h"
#include "CopyrightDlg.h"
#include <sstream>
#include "LayoutManager.h"
#include "FinishState.h"
#include "GlobalError.h"

class CCopyrightState : public CInstallState
{
	CCopyrightDlg	m_dialog;
	CCopyrightState() { }

public:
	virtual void Show()
	{
		assert( m_parent);

		if( !::IsWindow(m_dialog.GetSafeHwnd()))
		{
			PFS::CDataBlock out;
			if( !CLayoutManager::GetInstance().ReadCopyrightTxt( out))
			{
				CGlobalError::GetInstance().SetError( PFS::CEnv::GetLastError(), L"copyright.txt");
				ChangeState( CFinishState::GetInstance());
				return ;
			}
			wchar_t end = 0;
			out.Append( &end, sizeof(wchar_t));

			//if( !CLayoutManager::GetInstance().ReadBackgroundBmp())
			//{
			//	CGlobalError::GetInstance().SetError( PFS::CEnv::GetLastError(), L"background.bmp");
			//	ChangeState( CFinishState::GetInstance());
			//	return ;
			//}

			// int i = 0; // bmp 图的索引从0开始
			// ReadForegroundBmp(i)

			int i = 1; // tga 图的索引从1开始
			while( CLayoutManager::GetInstance().ReadForegroundTga(i))
			{
				i++;
			}

			//m_parent->ShowWindow( SW_SHOWMAXIMIZED);
			
			m_parent->ShowWindow( SW_SHOW);

			m_dialog.Create( m_parent);
			m_dialog.SetCopyrightText( out.GetData());
			m_dialog.CenterWindow();
		}
		m_dialog.ShowWindow( SW_SHOW);
	}
	
	// 可以切换全屏
	virtual bool CanToggleFullsceen() const
	{ return true; };

	static CCopyrightState& GetInstance()
	{
		static CCopyrightState state;
		return state;
	}
};