#pragma once

#include "resource.h"


class CAnimationFilenameDlg : public CDialog
{
	DECLARE_DYNAMIC(CAnimationFilenameDlg)

public:
	enum { IDD = IDD_DIALOG1 };
	void GetAnimationFilename(std::wstring &name);

	CString m_AniFilename;
	virtual ~CAnimationFilenameDlg();

	CAnimationFilenameDlg(CWnd* pParent = NULL);   

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
};
