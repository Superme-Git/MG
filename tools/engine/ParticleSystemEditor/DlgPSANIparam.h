#pragma once
#include "afxwin.h"

#include "..\engine\common\xptypes.h"


// CDlgPSANIparam �Ի���

class CDlgPSANIparam : public CDialog
{
	DECLARE_DYNAMIC(CDlgPSANIparam)

public:
	CDlgPSANIparam(CWnd* pParent = NULL);   // ��׼���캯��
	virtual ~CDlgPSANIparam();

// �Ի�������
	enum { IDD = IDD_DIALOG_PSANI_PARAMDLG };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV ֧��

	DECLARE_MESSAGE_MAP()

public:
	int m_nFramePerSecond;
	BOOL m_nRadioPrintScreenArea;
	CString m_strFilePath;


//		XPIFF_BMP = 0,
//		XPIFF_JPG = 1,
//		XPIFF_TGA = 2,

	Nuclear::XPIMAGE_FILEFORMAT m_nFileFormat;	
	CComboBox m_comboPicFileFormat;

private:
	afx_msg void OnBnClickedOk();
	afx_msg void OnBnClickedCancel();
	afx_msg void OnEnKillfocusEditpsanispeed();
	afx_msg void OnCbnSelchangeComboPicfileformat();
	afx_msg void OnBnClickedRadio1();
public:
	afx_msg void OnBnClickedRadio2();
	virtual BOOL OnInitDialog();
	afx_msg void OnBnClickedButtonSetfilepath();
};
