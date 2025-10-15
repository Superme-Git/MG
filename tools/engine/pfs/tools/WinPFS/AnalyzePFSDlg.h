#pragma once
#include "afxwin.h"
#include "AnalyzePFSStatic.h"

// CAnalyzePFSDlg dialog

class CWinPFSDoc;
class CAnalyzePFSDlg : public CDialog
{
	DECLARE_DYNAMIC(CAnalyzePFSDlg)

public:
	CAnalyzePFSDlg(CWnd* pParent = NULL);   // standard constructor
	virtual ~CAnalyzePFSDlg();

// Dialog Data
	enum { IDD = IDD_ANALYZEPFS };

	void SetDocument( CWinPFSDoc* doc);

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()

	COLORREF				m_crValid;		// 存在文件块的颜色
	COLORREF				m_crEmpty;		// 空闲块颜色
	COLORREF				m_crFC;			// FileCentral颜色


public:
	CAnalyzePFSStatic	m_result;
	virtual BOOL OnInitDialog();
	afx_msg void OnPaint();
};
