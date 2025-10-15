#ifndef PROPERITES_DLG_H
#define PROPERITES_DLG_H

#pragma once



class CMapEditorView;

class CPropertiesDlg : public CDialog
{
	DECLARE_DYNAMIC(CPropertiesDlg)

private:
	CRect m_ColorRect;
	CMapEditorView* m_pMapEditorView;

public:
	Nuclear::CRECT m_Rect;


public:
	enum { IDD = IDD_PROPERITES_DIALOG };
	void TryInvalidata();


	virtual BOOL Create(CMapEditorView* pMapEditorView = NULL);

	virtual ~CPropertiesDlg();

	CPropertiesDlg(CWnd* pParent = NULL);   

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()

public:
	afx_msg void OnPaint();
	afx_msg void OnBnClickedSelectBackgroundColor();
};

#endif