#pragma once
#include <string>
#include "../../pfs/pfs2.h"
#include "../../pfs/versionmetafile.h"
// CWebBrowserCtrl html view

class CWebBrowserCtrl : public CHtmlView
{
	DECLARE_DYNCREATE(CWebBrowserCtrl)

protected:
	CWebBrowserCtrl();           // protected constructor used by dynamic creation
	virtual ~CWebBrowserCtrl();
	
	PFSX::CVersionInfo m_localVersion;
	PFSX::CVersionInfo m_remoteVersion;
	BOOL m_bLocalVersionOK;
	BOOL m_bRemoteVersionOK;

	// 请求版本信息(本地版本和服务器最新版本)
	BOOL RequestVersionInfo();

	// 更新页面上的版本信息
	BOOL UpdateVersionInfo();

public:
	// 检查版本线程在检查完版本信息之后，通知新的版本信息
	void SetServerVersionInfo( const PFSX::CVersionInfo& rRemoteVersion )
	{
		m_remoteVersion = rRemoteVersion;
		m_bRemoteVersionOK = TRUE;
	}

protected:
	virtual HRESULT OnShowContextMenu(DWORD dwID, LPPOINT ppt,
		LPUNKNOWN pcmdtReserved, LPDISPATCH pdispReserved);

	DECLARE_MESSAGE_MAP()

public:
	virtual void OnBeforeNavigate2(LPCTSTR lpszURL, DWORD nFlags,
		LPCTSTR lpszTargetFrameName, CByteArray& baPostedData,
		LPCTSTR lpszHeaders, BOOL* pbCancel);

	virtual void OnNavigateError(LPCTSTR lpszURL, LPCTSTR lpszFrame, DWORD dwError, BOOL *pbCancel);
	virtual void OnDocumentComplete(LPCTSTR lpszURL);	

public:
	virtual BOOL PreTranslateMessage(MSG* pMsg);
};
