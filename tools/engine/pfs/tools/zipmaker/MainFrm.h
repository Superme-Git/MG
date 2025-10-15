// MainFrm.h : interface of the CMainFrame class
//
#pragma once
#include "Project.h"
#include "FileZipLevelDlg.h"
#include "EditIgnoreDlg.h"

#include "MyBarBottom.h"
typedef CMyBarBottom CKHMessagePane;

class CMainFrame : public CFrameWnd
{
	
public:
	CMainFrame();
protected: 
	DECLARE_DYNAMIC(CMainFrame)

// Attributes
public:
	bool OpenProjectFile( const std::wstring& filename);

	// 刷新是否忽略
	void RefreshIsCheck( );
	void RefreshZipLevel();

	void SetChanged( ) { m_bChanged = true; }

// Operations
public:

// Overrides
public:
	virtual BOOL PreCreateWindow(CREATESTRUCT& cs);
	virtual BOOL OnCmdMsg(UINT nID, int nCode, void* pExtra, AFX_CMDHANDLERINFO* pHandlerInfo);

// Implementation
public:
	virtual ~CMainFrame();

protected:  // control bar embedded members
	CStatusBar		m_wndStatusBar;
	CToolBar		m_wndToolBar;

	CSplitterWnd	m_wndSplitter;

	std::wstring	m_lastpath;
	CProject		m_project;
	std::wstring	m_projectfilename;
	bool			m_bChanged;
	bool			m_bSortOperators;
	bool			m_bInit;

	CFileZipLevelDlg	m_dlgZipLevel;
	CEditIgnoreDlg		m_dlgIgnore;

	CKHMessagePane m_wndMessagePane;	// 消息窗口
	CImageList		m_ilMessagePane;	// 消息窗口的位图列表

// Generated message map functions
protected:
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	DECLARE_MESSAGE_MAP()
	virtual BOOL OnCreateClient(LPCREATESTRUCT lpcs, CCreateContext* pContext);

	// 输出文件
	// bResource 是否输出资源包
	// bSaveFilesMeta 是否生成.files.meta文件
	// tempUpdateStr 临时更新包mount路径
	void SaveFile( bool bResource, bool bSaveFilesMeta, const std::wstring& lastDataPfs);
	void SetTitleText();
	void Clear();
	bool OnCloseProject();
	void AddRecentProject();
	void AddRecentProjectToMenu();
	void DelRecentProjectFromMenu();

public:
	afx_msg LRESULT OnOperatorEnd( WPARAM wParam, LPARAM lParam);
	afx_msg void OnFileAddPath();
	afx_msg void OnFileSaveSetup();
	afx_msg void OnViewEditfileziplevel();
	afx_msg void OnResourcetype();
	afx_msg void OnUpdateResourcetype(CCmdUI *pCmdUI);
	afx_msg void OnSetuptype();
	afx_msg void OnUpdateSetuptype(CCmdUI *pCmdUI);
	afx_msg void OnEditignore();
	afx_msg void OnFileSaveResource();
	afx_msg void OnFileNew();
	afx_msg void OnFileOpen();
	afx_msg void OnFileSave();
	afx_msg void OnClose();
	afx_msg void OnUpdateFileSave(CCmdUI *pCmdUI);
	afx_msg void OnEditAddpathcontent();
	afx_msg void OnUpdateFileSaveresource(CCmdUI *pCmdUI);
	afx_msg void OnUpdateFileSavesetup(CCmdUI *pCmdUI);
	afx_msg void OnUpdateViewEditfileziplevel(CCmdUI *pCmdUI);
	afx_msg void OnUpdateEditAddpathcontent(CCmdUI *pCmdUI);
	afx_msg void OnUpdateEditignore(CCmdUI *pCmdUI);
	afx_msg void OnUpdateFileAddpath(CCmdUI *pCmdUI);
	afx_msg void OnViewShowunchecked();
	afx_msg void OnUpdateViewShowunchecked(CCmdUI *pCmdUI);
	afx_msg void OnFileClose();
	afx_msg void OnUpdateFileClose(CCmdUI *pCmdUI);
	afx_msg void OnRecentProject( UINT id);
	afx_msg void OnEditAddnewpath();
	afx_msg void OnUpdateEditAddnewpath(CCmdUI *pCmdUI);
	afx_msg void OnEditDelete();
	afx_msg void OnUpdateEditDelete(CCmdUI *pCmdUI);
	afx_msg void OnEditProjectPath();
	afx_msg void OnUpdateProjectPath(CCmdUI *pCmdUI);
	afx_msg void OnFileRefresh();
	afx_msg void OnUpdateFileRefresh(CCmdUI *pCmdUI);
	afx_msg void OnDropFiles(HDROP hDropInfo);
	afx_msg void OnEditVersionmeta();
	afx_msg void OnUpdateEditVersionmeta(CCmdUI *pCmdUI);
	afx_msg void OnSetIsMakeMD5();
	afx_msg void OnUpdateSetIsMakeMD5(CCmdUI *pCmdUI);

	afx_msg void OnFileSaveTempUpdate();
	afx_msg void OnUpdateFileSaveTempUpdate(CCmdUI *pCmdUI);

	afx_msg void OnViewUpdateSetupPfs();
	afx_msg void OnEditSortOperators();
	afx_msg void OnUpdateEditSortOperators(CCmdUI *pCmdUI);
	afx_msg void OnUpdateFileSavezip(CCmdUI *pCmdUI);
	afx_msg void OnFileSavezip();
};


