#pragma once
#include "FileViewer.h"
#include <map>
#include "../../pfs2.h"

enum FILEVIEWER_DATAFORMAT
{
	FVDF_RAW = 0,
	FVDF_TEXT = 1,
	FVDF_UNICODETEXT = 2,
	FVDF_XML = 3,
	FVDF_OCTETS_XML = 4,
	FVDF_PFS = 5,
	FVDF_FILES_META = 6, // for files like .files.meta / .files2.meta
	FVDF_IMAGE_BMP = 10,
	FVDF_IMAGE_TGA = 11,
	FVDF_IMAGE_JPG = 12,
	FVDF_IMAGE_DDS = 13,
	FVDF_SYSTEM_DEFAULT = 100,
	FVDF_ZIP = 0x0100,
	FVDF_ZIPPED = 0x0200,
};

// CFileViewerFrame frame


class CFileViewerMap : std::map<DWORD, CFileViewer*>
{
public:
	CFileViewerMap();
	~CFileViewerMap();
	
	CFileViewer* CreateFileViewer( DWORD format, CWnd* parent);
};

class CFileViewerFrame : public CFrameWnd
{
	DECLARE_DYNCREATE(CFileViewerFrame)

protected:
	CFileViewerFrame();           // protected constructor used by dynamic creation
	virtual ~CFileViewerFrame();
	CStatusBar			m_wndStatusBar;
	CToolBar			m_wndToolBar;

	PFS::CDataBlock		m_buffer;
	PFS::CDataBlock		m_bufferUnzipped;

	CFileViewerMap		m_viewers;
	CFileViewer*		m_curViewer;

	DWORD				m_format;
	BOOL				m_bTryUnzip;
	BOOL				m_bHexMode;
protected:
	DECLARE_MESSAGE_MAP()

public:
	BOOL ShowText( LPCTSTR lpszText );

	// same as ShowFormatData(pData,len, FVDF_RAW)
	BOOL ShowData( const void* pData, size_t len ); 
	BOOL ShowFormatData( DWORD dwFormatFlags );

public:
	virtual BOOL PreTranslateMessage(MSG* pMsg);
	virtual BOOL OnCmdMsg(UINT nID, int nCode, void* pExtra, AFX_CMDHANDLERINFO* pHandlerInfo);

public:
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	afx_msg void OnViewHexMode();
	afx_msg void OnUpdateViewHexMode(CCmdUI *pCmdUI);
	afx_msg void OnViewTextMode();
	afx_msg void OnUpdateViewTextMode(CCmdUI *pCmdUI);
	afx_msg void OnViewXmlMode();
	afx_msg void OnUpdateViewXmlMode(CCmdUI *pCmdUI);
	afx_msg void OnViewFilesMeta();
	afx_msg void OnUpdateViewFilesMeta(CCmdUI *pCmdUI);
	afx_msg void OnTryUnzip();
	afx_msg void OnUpdateTryUnzip(CCmdUI *pCmdUI);

protected:
	virtual BOOL OnCreateClient(LPCREATESTRUCT lpcs, CCreateContext* pContext);
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
};


