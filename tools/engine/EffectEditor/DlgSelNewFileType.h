#pragma once
#include "EffectEditorDoc.h"



class CDlgSelNewFileType : public CDialog
{
	DECLARE_DYNAMIC(CDlgSelNewFileType)

public:
	virtual ~CDlgSelNewFileType();
	CDlgSelNewFileType(CWnd* pParent = NULL);   

	enum { IDD = IDD_NEW_FILE_TYPE_DIALOG };
	eEffectFileType type;

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
public:
	virtual void OnOK();
	virtual BOOL OnInitDialog();
};
