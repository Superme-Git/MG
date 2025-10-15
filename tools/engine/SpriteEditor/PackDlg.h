#pragma once

#include "..\engine\common\xptypes.h"
#include "..\engine\engine\animanager.h"
#include "afxwin.h"

#include <map>
#include <string>





class CPackDlg : public CDialog
{
	DECLARE_DYNAMIC(CPackDlg)

public:
	
	enum { IDD = IDD_DLG_PACK };

private:
	CComboBox m_ComboTimeMode;
	CComboBox m_ComboDirMode;
	CComboBox m_ComboBlend;
	CComboBox m_ComboTexFormat;
	CComboBox m_ComboFileFormat;

public:
	virtual ~CPackDlg();
	CPackDlg(CWnd* pParent = NULL);   
	void GetAnimationFilename(std::wstring &name);

	CString m_AnimationFilename;
	int m_SystemLevel;		


	BOOL m_bBindType;
	bool m_fNew;
	BOOL m_fAutoDetectFrameCount;	
	BOOL m_fAutoDetectRegionCount;	
	int m_nFrame;		
	int m_nRegion;		
	int m_CenterY;		
	int m_CenterX;		
	int m_PackTime;		
	int m_TimeMode;		
	std::wstring m_DstPath;
	std::wstring m_PartPath;

	Nuclear::XPANI_DIRECTIONMODE m_DirMode;
	std::map<std::wstring, Nuclear::XPANI_DIRECTIONMODE> m_DirModeMap;
	Nuclear::XPTEXTURE_BLENDMODE m_Blend;

	Nuclear::XPTEXTURE_FORMAT m_TexFormat;
	Nuclear::XPIMAGE_FILEFORMAT m_FileFormat;
	std::map<std::wstring, Nuclear::XPTEXTURE_BLENDMODE> m_BlendMap;

	std::map<std::wstring, Nuclear::XPTEXTURE_FORMAT> m_TexFormatMap;
	std::map<std::wstring, Nuclear::XPIMAGE_FILEFORMAT> m_FileFormatMap;

	

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()
	
protected:
	virtual void OnOK();
	
public:
	afx_msg void OnCbnSelchangeComboTimeMode();
	afx_msg void OnBnClickedChkAdframe();
	afx_msg void OnBnClickedButton2();
	afx_msg void OnBnClickedBtnSelpackdstdir();
	afx_msg void OnEnChangeEdit2();
	virtual BOOL OnInitDialog();

private:
	CComboBox m_comboSysLevel;
public:
	afx_msg void OnBnClickedBtnSelpartpath();
};
