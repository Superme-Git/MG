#pragma once
#include "InstallState.h"
#include "FinishState.h"
#include "LayoutManager.h"
#include "GlobalError.h"
#include "Config.h"

typedef BOOL (WINAPI *LPFN_ISWOW64PROCESS) (HANDLE, PBOOL);
BOOL IsWow64()
{
	LPFN_ISWOW64PROCESS fnIsWow64Process = (LPFN_ISWOW64PROCESS)GetProcAddress(
		GetModuleHandle( L"kernel32"), "IsWow64Process");
	BOOL bIsWow64 = FALSE;

	if( NULL != fnIsWow64Process)
	{
		if( !fnIsWow64Process( GetCurrentProcess(), &bIsWow64))
		{
			// handle error
		}
	}
	return bIsWow64;
}

class CInitState : public CInstallState
{
public:
	enum { IDT_INIT = 1001, };
	virtual void Show()
	{
		assert( m_parent);
		if( CConfig::GetInstance().m_checkOS64 && IsWow64())
		{
			m_parent->ShowWindow( SW_MINIMIZE);
			CGlobalError::GetInstance().SetError( CInstallState::ERROR_NOTSUSPENDOS64, 
				CConfig::GetInstance().m_product.c_str());
			ChangeState( CFinishState::GetInstance());
			return;
		}
		if( CLayoutManager::GetInstance().ReadLogoJpg())
		{
			m_parent->SetTimer( IDT_INIT, 1000, NULL);
		}
		else
		{
			m_parent->ShowWindow( SW_MINIMIZE);
			CGlobalError::GetInstance().SetError( PFS::CEnv::GetLastError(), L"logo.jpg");
			ChangeState( CFinishState::GetInstance());
		}
	}
	
	static CInitState& GetInstance()
	{
		static CInitState state;
		return state;
	}
};