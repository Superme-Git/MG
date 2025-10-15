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

	// ����汾��Ϣ(���ذ汾�ͷ��������°汾)
	BOOL RequestVersionInfo();

	// ����ҳ���ϵİ汾��Ϣ
	BOOL UpdateVersionInfo();

public:
	// ���汾�߳��ڼ����汾��Ϣ֮��֪ͨ�µİ汾��Ϣ
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
