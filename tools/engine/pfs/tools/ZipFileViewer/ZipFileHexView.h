#pragma once

// CZipFileHexView view

class CZipFileHexView : public CEditView
{
	DECLARE_DYNCREATE(CZipFileHexView)

protected:
	CZipFileHexView();           // protected constructor used by dynamic creation
	virtual ~CZipFileHexView();

public:

protected:
	virtual void OnInitialUpdate();     // first time after construct

	DECLARE_MESSAGE_MAP()
public:
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
};


