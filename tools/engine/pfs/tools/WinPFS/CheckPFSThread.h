#pragma once
#include "WinPFSDoc.h"
#include "../../src/meta/metafileio.h"

// CCheckPFSThread

class CCheckPFSThread : public CWinThread
{
	DECLARE_DYNCREATE(CCheckPFSThread)

protected:
	CCheckPFSThread();           // protected constructor used by dynamic creation
	virtual ~CCheckPFSThread();

	// running and idle processing
	virtual int Run();

	void CheckOneFile( const ZIPFILE::CFileCentral& fc, int& crcResult, int& md5Result);

public:
	virtual BOOL InitInstance();
	virtual int ExitInstance();

	enum
	{
		EQUAL = 0,
		NOEQUAL,
		NOFOUND,
	};

	void SetDocument( CWinPFSDoc* doc) { m_doc = doc; }
	void SetWnd( CWnd* wnd) { m_wnd = wnd; }

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
	PFS::CMetaInfoMap	m_mim;
};


