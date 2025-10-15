// PatchMetaFileView.h : interface of the CPatchMetaFileView class
//


#pragma once
#include "afxwin.h"
#include "../../patchmetafile.h"

class CPatchMetaFileView : public CFormView
{
protected: // create from serialization only
	CPatchMetaFileView();
	DECLARE_DYNCREATE(CPatchMetaFileView)

public:
	enum{ IDD = IDD_PATCHMETAFILE_FORM };

// Attributes
public:
	CPatchMetaFileDoc* GetDocument() const
   { return reinterpret_cast<CPatchMetaFileDoc*>(m_pDocument); }

// Operations
public:

// Overrides
protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	virtual void OnInitialUpdate(); // called first time after construct

// Implementation
public:
	virtual ~CPatchMetaFileView();

protected:
	void FillPatchInfo();
	void FillUpdateInfo();
	void UpdatePatchInfoInList( int index, const PFSX::PatchInfo& pi );
	bool GetPatchInfoFromPatchFile(
		std::wstring natviefilepath,
		std::wstring filename,
		PFSX::PatchInfo& pi,
		PFSX::CVersionInfo& vi );

	void AddPatchInfoToList( PFSX::CPatchInfoMap::const_iterator& i);
	void AddUpdateInfoToList( const PFSX::UpdateInfo& ui);

// Generated message map functions
protected:
	DECLARE_MESSAGE_MAP()

	CListCtrl		m_lcPatch;
	CListCtrl		m_lcUpdate;

	bool			m_first;

	DWORD			m_versionLatest;		// ×îÐÂ°æ±¾
	
public:
	afx_msg void OnBnClickedAddPatch();
	afx_msg void OnBnClickedDelPatch();
	afx_msg void OnBnClickedAddUpdate();
	afx_msg void OnBnClickedDelUpdate();
	afx_msg void OnNMDblclkListUpdate(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMDblclkListPatch(NMHDR *pNMHDR, LRESULT *pResult);
};
