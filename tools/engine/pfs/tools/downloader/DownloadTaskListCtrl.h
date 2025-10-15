#pragma once

// CDownloadTaskListCtrl

class CDownloadTaskListCtrl : public CListCtrl
{
	DECLARE_DYNAMIC(CDownloadTaskListCtrl)

public:
	CDownloadTaskListCtrl();
	virtual ~CDownloadTaskListCtrl();

protected:
	
	BOOL _InnerInit();

protected:
	DECLARE_MESSAGE_MAP()
public:
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);

	// �༭����
	void SelectAll();
	void Delete();
	void Paste();	

	// ��������
	void Start();
	void Pause();
	void Cancel();

	// ��ȡȫ���б�����
	void GetFileNameList( wstringlist& rFileNameList );

protected:
	virtual void PreSubclassWindow();
public:
	virtual BOOL PreTranslateMessage(MSG* pMsg);
};


