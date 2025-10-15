#pragma once
#include <map>
#include "afxwin.h"


class IAreaPanel
{
public:
	virtual void SelectASound(DWORD_PTR handle) = 0;
};
// AllSoundListPanel dialog

class AllSoundListPanel : public CDialog
{
	DECLARE_DYNAMIC(AllSoundListPanel)
private:
	typedef std::map<DWORD_PTR, UINT> ListMap;

public:
	// Dialog Data
	enum { IDD = IDD_SOUND_LIST };

private:
	CListBox m_soundList;
	ListMap m_listMap;
	IAreaPanel *m_pPanel;
	bool m_bIsInited;

public:
	AllSoundListPanel(CWnd* pParent = NULL);   // standard constructor
	virtual ~AllSoundListPanel();

private:
	void ResetSize();

public:
	void InsertSound(UINT nID, DWORD_PTR handle, const std::wstring &str);
	UINT RemoveSound(DWORD_PTR handle);
	void RemoveSounds(UINT nBeginID, UINT nEndID);	//×ó±ÕÓÒ¿ª
	void ClearAll();
	void SetPanel(IAreaPanel *pPanel) { m_pPanel = pPanel; }

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	DECLARE_MESSAGE_MAP()
public:
	virtual BOOL OnInitDialog();
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnLbnSelchangeAllSoundList();
};
