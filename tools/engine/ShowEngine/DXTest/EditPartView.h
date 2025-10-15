#pragma once
#include "ShowPartLoader.h"
#include "DXTestView.h"
#include "ComboListCtrl/ComboListCtrl.h"
#include "ValidateFileDlg.h"

// 根据tab名称和sex和sorttype 保存上一次选中索引
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

	// 保存每一分类上一次选中索引
	CTabSaveItemVector		m_lastselect;

	bool			m_changed;

	std::wstring	m_selectpath;		// 上次选择的目录

	BOOL			m_bMouseEvent;		// 是否是鼠标事件

	enum
	{
		SORT_BY_LAYER = 0,	// 按层分
		SORT_BY_SUIT = 1,	// 按套件分
		SORT_BY_TYPE = 2,	// 按类型分
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

	// 根据部件id切换当前tab
	void ChangeTabByPartID( int partID);

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	// 插入item到tab
	void InsertItemsToTab();

	// 插入item到list
	void InsertItemsToList( );

	// 插入一个部件信息到list
	// 返回刚插入的索引
	int InsertOneItemToList( ShowPartItem& part);

	// 显示裸模
	void ShowDefaultParts();

	// 更新上一次选择索引信息
	void UpdateLastSelect( bool bSave, int index);

	// 更新控件位置
	void UpdateControlPos();

	// 得到最大套件名称
	void GetMaxSuitName( std::wstring& suitname, int sex);

	// 添加目录到套件
	bool AddPathToSuit( const std::wstring& srcpath, const std::wstring& suitname, std::wstring& error);

	// 添加一个部件
	bool AddOnePart( const std::wstring& srcpath, const std::wstring& svg, 
		const std::wstring& suitname, std::wstring& error);

	// 设置List的flag文本
	void SetListFlagText( int index, int col, int flag, int flagType);

	// 根据partID选中行
	void SelectItemByPartID( unsigned long selPartID);

	// 选择或不选所有
	void SelectAllParts( BOOL bSel);

	// 显示或不显示所有
	void ShowAllParts( BOOL bShow);

	// 显示 linked 的部件
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


