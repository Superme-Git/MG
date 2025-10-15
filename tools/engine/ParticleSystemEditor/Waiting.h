#pragma once


class CWaiting : public CWnd
{
// Construction
public:
	CWaiting();

public:
	BOOL Create( CWnd* pParentWnd);
	void SetRange(int nMax);
	void SetPos(int nPos);

private:
	int m_nMax;
	int m_nPos;

public:
	virtual ~CWaiting();

	// Generated message map functions
protected:
	//{{AFX_MSG(CWaiting)
	afx_msg void OnPaint();
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};
