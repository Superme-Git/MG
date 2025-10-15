#pragma once
#include "afxcmn.h"
#include <list>
#include "../engine/common/xptypes.h"
#include "ColorListCtrl.h"

// CMazeInfo dialog

class CMazeInfo : public CDialog
{
	DECLARE_DYNAMIC(CMazeInfo)
public:	// Dialog Data
	enum { IDD = IDD_MAZE_SETTING_DLG };

private:
	CListCtrl m_listMapId;
	CColorListCtrl m_listMazeColor;
	CSliderCtrl m_SilderAlpha;
	size_t m_nOldColorCount;
	bool m_colorChanged;
	bool m_mapmapChanged;
	std::list<std::wstring> m_deleteMapIDlist;
	int m_nAlpha;
	DWORD_PTR m_nItemDataSeed;

public:
	bool ingoreColors;//如果为true在InitDialog自己读Colors
	CString mazeUrlFormat;
	unsigned int spriteMask;
	Nuclear::XPMazeColors colors;

public:
	CMazeInfo(CWnd* pParent = NULL);   // standard constructor
	virtual ~CMazeInfo();

private:
	void LoadMapIdMap();
	void SaveMapIdMap();
	void LoadColorMap();
	void SaveColorMap();

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	virtual void OnOK();
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnBnClickedButtonAddMapid();
	afx_msg void OnBnClickedButtonDelMapid();
	afx_msg void OnBnClickedButtonAddMaskColor();
	afx_msg void OnBnClickedButtonDelMaskColor();
	virtual BOOL OnInitDialog();
	afx_msg void OnNMDblclkListMazeColor(NMHDR *pNMHDR, LRESULT *pResult);
};
