#pragma once
#include "ShowPartLoader.h"
#include "DXTestView.h"
#include "ComboListCtrl/ComboListCtrl.h"
#include "ValidateFileDlg.h"

// ����tab���ƺ�sex��sorttype ������һ��ѡ������
struct CTabSaveItem
{
	std::wstring	name;
	int				sex;
	int				sorttype;
	std::set<int>	selects;
};
typedef std::vector<CTabSaveItem>	CTabSaveItemVector;

// CEditPartView form view
class CEditPartView : public CFormView
{
	DECLARE_DYNCREATE(CEditPartView)

protected:
	CEditPartView();           // protected constructor used by dynamic creation
	virtual ~CEditPartView();

	CValidateFileDlg	m_dlgValidate;

	CTabCtrl		m_tab;
	CComboListCtrl	m_list;
	bool			m_first;
	int				m_sex;
	CShowPartLoader		m_loader;

	// ����ÿһ������һ��ѡ������
	CTabSaveItemVector		m_lastselect;

	bool			m_changed;

	std::wstring	m_selectpath;		// �ϴ�ѡ���Ŀ¼

	BOOL			m_bMouseEvent;		// �Ƿ�������¼�

	enum
	{
		SORT_BY_LAYER = 0,	// �����
		SORT_BY_SUIT = 1,	// ���׼���
		SORT_BY_TYPE = 2,	// �����ͷ�
	};
	int				m_sorttype;
	

public:
	enum { IDD = IDD_DIALOG_EDITPART };
	
	CDXTestView*	m_pDXTestView;

#ifdef _DEBUG
	virtual void AssertValid() const;
#ifndef _WIN32_WCE
	virtual void Dump(CDumpContext& dc) const;
#endif
#endif

	bool IsChanged() { return m_changed; }

	// ���ݲ���id�л���ǰtab
	void ChangeTabByPartID( int partID);

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	// ����item��tab
	void InsertItemsToTab();

	// ����item��list
	void InsertItemsToList( );

	// ����һ��������Ϣ��list
	// ���ظղ��������
	int InsertOneItemToList( ShowPartItem& part);

	// ��ʾ��ģ
	void ShowDefaultParts();

	// ������һ��ѡ��������Ϣ
	void UpdateLastSelect( bool bSave, int index);

	// ���¿ؼ�λ��
	void UpdateControlPos();

	// �õ�����׼�����
	void GetMaxSuitName( std::wstring& suitname, int sex);

	// ���Ŀ¼���׼�
	bool AddPathToSuit( const std::wstring& srcpath, const std::wstring& suitname, std::wstring& error);

	// ���һ������
	bool AddOnePart( const std::wstring& srcpath, const std::wstring& svg, 
		const std::wstring& suitname, std::wstring& error);

	// ����List��flag�ı�
	void SetListFlagText( int index, int col, int flag, int flagType);

	// ����partIDѡ����
	void SelectItemByPartID( unsigned long selPartID);

	// ѡ���ѡ����
	void SelectAllParts( BOOL bSel);

	// ��ʾ����ʾ����
	void ShowAllParts( BOOL bShow);

	// ��ʾ linked �Ĳ���
	void ShowLinkedParts( ShowPartItem& part, TShowPartPtrContainer& tspc, BOOL bCheck);

	DECLARE_MESSAGE_MAP()
public:
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	virtual void OnInitialUpdate();
	afx_msg void OnSize(UINT nType, int cx, int cy);
	afx_msg void OnTcnSelchangeTab1(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnLvnItemchangedList1(NMHDR *pNMHDR, LRESULT *pResult);

	afx_msg void OnUpdateOptionManModel(CCmdUI *pCmdUI);
	afx_msg void OnUpdateOptionWomanModel(CCmdUI *pCmdUI);
	afx_msg void OnOptionManModel();
	afx_msg void OnOptionWomanModel();
	afx_msg void OnSaveConfig();
	afx_msg void OnSavePos();
	afx_msg void OnUpdateSaveConfig(CCmdUI *pCmdUI);
	afx_msg void OnUpdateSavePos(CCmdUI *pCmdUI);
	afx_msg void OnSortByLayer();
	afx_msg void OnUpdateSortByLayer(CCmdUI* pCmdUI);
	afx_msg void OnSortBySuit();
	afx_msg void OnUpdateSortBySuit(CCmdUI* pCmdUI);
	afx_msg void OnAddPath();
	afx_msg void OnUpdateAddPath(CCmdUI* pCmdUI);
	afx_msg void OnSortByType();
	afx_msg void OnUpdateSortByType(CCmdUI* pCmdUI);
	afx_msg void OnDeletePart();
	afx_msg void OnAddPart();
	afx_msg void OnUpdateAddPart(CCmdUI* pCmdUI);
	afx_msg void OnOptionNaked();
	afx_msg void OnSuitSelectAll();
	afx_msg void OnSuitUnSelectAll();
	afx_msg void OnSuitDelete();
	afx_msg void OnShowSelected();
	afx_msg void OnShowAntiSelected();

	afx_msg void OnFileValidate();

	afx_msg LRESULT PopulateComboList(WPARAM wParam, LPARAM lParam);
	afx_msg LRESULT OnEndLabelEditVariableCriteria(WPARAM wParam, LPARAM lParam);
	afx_msg void OnNMRclickTab1(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMRclickList1(NMHDR *pNMHDR, LRESULT *pResult);
};


