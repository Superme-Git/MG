#pragma once
// CDownloadThread
#include <string>
#include "../Patcher/GlobalFunction.h"
#include "../../pfs/src/common.h"
#include "../../pfs/versionmetafile.h"

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
	
	int		m_downloaderr;		// 下载出错次数

protected:
	DECLARE_MESSAGE_MAP()
};


