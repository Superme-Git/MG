#pragma once



class IToolsNotify
{
public:
	virtual void OnAddClip() { }
	virtual void OnToolsControl(UINT nID) { }

};

class CToolsPanel : public CDialog
{
	DECLARE_DYNAMIC(CToolsPanel)

private:
	bool m_bIsInited;
	CToolBar m_Tools;
	IToolsNotify* m_pNotify;

private:
	void ResetSize();	

public:
	void SetToolsNotify(IToolsNotify *pNotify)
	{
		m_pNotify = pNotify;
	}
	virtual ~CToolsPanel();
	CToolsPanel(CWnd* pParent = NULL);   

	enum { IDD = IDD_TOOLS_PANEL };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnToolsControl(UINT nID)
	{
		if (m_pNotify)
		{
			m_pNotify->OnToolsControl(nID);
		}
	}
	afx_msg void OnBnClickedAddClip();
	afx_msg void OnSizing(UINT fwSide, LPRECT pRect);
	afx_msg void OnSize(UINT nType, int cx, int cy);
	virtual BOOL OnInitDialog();
};
