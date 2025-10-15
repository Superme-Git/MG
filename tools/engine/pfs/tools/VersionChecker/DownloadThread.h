//////////////////////////////////////////////////////////////////////////
//
// CDownloadThread
//
#pragma once

class CDownloadThread : public CWinThread
{
	DECLARE_DYNCREATE(CDownloadThread)

protected:
	CDownloadThread();           // protected constructor used by dynamic creation
	virtual ~CDownloadThread();

public:
	virtual BOOL InitInstance();
	virtual int ExitInstance();

	// running and idle processing
	virtual int Run();
	
protected:
	DECLARE_MESSAGE_MAP()
};


