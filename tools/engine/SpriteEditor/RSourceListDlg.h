#pragma once
#include "afxcmn.h"

#include <vector>
#include <string>
#include <set>
#include <map>
#include "afxwin.h"


class CSpriteEditorDoc;

class CRSourceListDlg : public CDialog
{
	DECLARE_DYNAMIC(CRSourceListDlg)

public:
	enum { IDD = IDD_DLG_RSRCLIST };
	void Refresh();



	void ShowList();

	struct sIndex2
	{
		int m_IA;
		int m_IR;
		sIndex2(int a=-1,int r=-1) : m_IA(a), m_IR(r) { }
		bool Valid() const { return m_IA>=0 && m_IR>=0; }
		bool operator==(const sIndex2 &rhs) const {
			return m_IA == rhs.m_IA && m_IR == rhs.m_IR; }
		bool operator<(const sIndex2 &rhs) const {
			return m_IR < rhs.m_IR ||
				m_IR == rhs.m_IR && m_IA < rhs.m_IA; }
	};

	std::set<sIndex2> m_XapRSrc;
	std::vector<std::wstring> m_xRoles;
	std::vector<std::wstring> m_xActions;
	void InitResource(const std::wstring &dir);
	std::wstring m_FileDir;
	void ShowStdAnimationList();

	struct sIndex3
	{
		int m_IL;
		int m_IA;
		int m_IR;
		sIndex3(int l=-1,int a=-1,int r=-1) : m_IL(l), m_IA(a), m_IR(r) { }
		bool Valid() const { return m_IL>=0 && m_IA>=0 && m_IR>=0; }
		bool operator==(const sIndex3 &rhs) const {
			return m_IL == rhs.m_IL && m_IA == rhs.m_IA && m_IR == rhs.m_IR; }
		bool operator<(const sIndex3 &rhs) const {
			return m_IL < rhs.m_IL ||
				m_IL == rhs.m_IL && m_IR < rhs.m_IR ||
				m_IL == rhs.m_IL && m_IR == rhs.m_IR && m_IA < rhs.m_IA; }
	};

	sIndex3 m_CurIndex;
	std::wstring m_CurModelName;
	struct sModelPack
	{
		std::vector<std::wstring> m_LayerArr;
		std::vector<std::wstring> m_ActionArr;
		std::vector<std::wstring> m_RoleArr;	
		std::vector<sIndex3> m_StdRSrc;
	};

	typedef std::map<std::wstring, sModelPack> ModelMap;
	ModelMap m_ModelRSrcs;

	void ChangeModel(const std::wstring &modelname);

	void InitStdResource();
	bool m_fStdInit;
	
	int m_ListType2;


	int m_ListType1;
	CSpriteEditorDoc *m_pDoc;

	virtual ~CRSourceListDlg();

	CRSourceListDlg(CWnd* pParent = NULL);   

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    

	DECLARE_MESSAGE_MAP()

public:
	afx_msg void OnCbnSelchangeCmbRsrcmodelname();
	CComboBox m_cmbModelName;
	afx_msg void OnCbnSelchangeCmbStdrole();
	CComboBox m_cmbStdRole;
	afx_msg void OnCbnSelchangeCmbStdact1();
	CComboBox m_cmbStdAct1;
	afx_msg void OnCbnSelchangeCmbStdlayer();
	CComboBox m_cmbStdLayer;
	afx_msg void OnTcnSelchangeTabStdlist(NMHDR *pNMHDR, LRESULT *pResult);
	CTabCtrl m_tabStdList;
	afx_msg void OnBnClickedBtnSelxappath();
	CListCtrl m_listXap;
	CListCtrl m_listStdAction;
	afx_msg void OnTcnSelchangeTabRsrclist(NMHDR *pNMHDR, LRESULT *pResult);
	virtual BOOL OnInitDialog();
	CTabCtrl m_tabList;
};
