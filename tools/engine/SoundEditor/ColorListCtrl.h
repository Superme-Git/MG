#pragma once
#include <map>

// CColorListCtrl

class CColorListCtrl : public CListCtrl
{
	DECLARE_DYNAMIC(CColorListCtrl)
private:
	std::map<DWORD_PTR, COLORREF> m_colors;
public:
	CColorListCtrl();
	virtual ~CColorListCtrl();

protected:
	DECLARE_MESSAGE_MAP()
public:
	void SetItemColor(DWORD_PTR nItemData, COLORREF color) { m_colors[nItemData] = color; }
	COLORREF GetItemColor(DWORD_PTR nItemData) { return m_colors[nItemData]; }
	afx_msg void OnNMCustomdraw(NMHDR *pNMHDR, LRESULT *pResult);
};


