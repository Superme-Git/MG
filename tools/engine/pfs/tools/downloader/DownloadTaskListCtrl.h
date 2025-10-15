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

	// 编辑命令
	void SelectAll();
	void Delete();
	void Paste();	

	// 下载命令
	void Start();
	void Pause();
	void Cancel();

	// 获取全部列表内容
	void GetFileNameList( wstringlist& rFileNameList );

protected:
	virtual void PreSubclassWindow();
public:
	virtual BOOL PreTranslateMessage(MSG* pMsg);
};


