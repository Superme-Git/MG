#pragma once
#include "WinPFSDoc.h"


// CMakeHashTableThread

class CMakeHashTableThread : public CWinThread
{
	DECLARE_DYNCREATE(CMakeHashTableThread)

protected:
	CMakeHashTableThread();           // protected constructor used by dynamic creation
	virtual ~CMakeHashTableThread();

public:
	virtual BOOL InitInstance();
	virtual int ExitInstance();

	// running and idle processing
	virtual int Run();

	void SetDocument( CWinPFSDoc* doc) { m_doc = doc; }
	void SetWnd( CWnd* wnd) { m_wnd = wnd; }
	void SetOutputFileName( bool output) { m_outputFileName = output; }
	void SetMountName( const std::wstring& mountName) { m_mountName = mountName; }
	void SetSaveFileName( const std::wstring& saveFileName) { m_saveFileName = saveFileName; }

	bool IsRunning()
	{ 
		DWORD res = WaitForSingleObject( m_hThread, 0); 
		return ( (WAIT_FAILED != res) && ( WAIT_OBJECT_0 != res));
	}

	void Stop()
	{
		m_stop = true;
		WaitForSingleObject( m_hThread, INFINITE);
	}

protected:
	DECLARE_MESSAGE_MAP()

	bool				m_stop;
	CWinPFSDoc*			m_doc;
	CWnd*				m_wnd;
	bool				m_outputFileName;	// 
	std::wstring		m_mountName;

	std::wstring		m_saveFileName;
};


