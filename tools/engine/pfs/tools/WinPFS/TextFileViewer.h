#pragma once
#include "FileViewer.h"

class CTextFileViewer :	public CFileViewer
{
protected:
	CRichEditCtrl		m_wndEditPanel;	// simple panel

public:
	CTextFileViewer(void);
	~CTextFileViewer(void);

	virtual bool Create( CWnd* parent);
	virtual bool SetData( const void* pData, size_t len );
	virtual void Show( bool bShow){ m_wndEditPanel.ShowWindow( bShow ? SW_SHOW : SW_HIDE); }
	virtual void Move(){}

protected:
	DECLARE_MESSAGE_MAP()
	afx_msg void OnEditCopy();
	afx_msg void OnUpdateEditCopy(CCmdUI *pCmdUI);
};
