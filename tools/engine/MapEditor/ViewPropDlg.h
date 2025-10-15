#ifndef VIEW_PROPERT_DLG_H
#define VIEW_PROPERT_DLG_H
#pragma once




class CViewPropDlg : public CDialog
{
	DECLARE_DYNAMIC(CViewPropDlg)
private:
	int m_nRadius;
	COLORREF m_cBgColor;
	COLORREF m_cGirdColor;
	COLORREF m_cBaseColor;
	CRect m_BgRect;
	CRect m_GridRect;
	CRect m_BaseRect;

public:
	enum { IDD = IDD_DLG_VIEW_PROP };
	int GetRadius() const;
	void SetRadius(int nMagnetRadius);

	void SetGridColor(unsigned int color);

	void SetBgColor(unsigned int color);

	void SetBaseColor(unsigned int color);

	unsigned int GetBgColor() const;

	unsigned int GetGridColor() const;
	unsigned int GetBaseColor() const;
	afx_msg void OnBnClickedSelectGridColor();

	afx_msg void OnBnClickedSelectBaseColor();
	afx_msg void OnPaint();
	virtual ~CViewPropDlg();

	CViewPropDlg(CWnd* pParent = NULL);   

protected:
	afx_msg void OnBnClickedSaveProp();
	afx_msg void OnBnClickedSelectBgColor();
	DECLARE_MESSAGE_MAP()	
	virtual void DoDataExchange(CDataExchange* pDX);    

};

#endif