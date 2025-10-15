#pragma once
#include "afxwin.h"
#include <string>



class CChunkInfo : public CDialog
{
	DECLARE_DYNAMIC(CChunkInfo)
private:
	std::wstring m_BlockInfoStr;
	CStatic m_BlockInfoTip;
public:
	int m_Height;
	int m_Width;

public:
	enum { IDD = IDD_DLG_BLOCK_INFO };
	virtual ~CChunkInfo();


	CChunkInfo(const std::wstring &modelname, const std::wstring &actionName);   

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
public:
	virtual BOOL OnInitDialog();
};
