#pragma once
#include <vector>
#include <string>
#include "Project.h"
#include "ZipTreeItem.h"

// CLeftView view
class CLeftViewListener;
class CLeftView : public CTreeView
{
	DECLARE_DYNCREATE(CLeftView)

public:
	// ��zip�ļ�ʱ
	bool OpenProject( CProject* project);
	void CloseProject();

	bool AddPath( const std::wstring& path);
	bool AddPathAndFile( std::map<std::wstring, bool>& files);
	bool AddNewPath( const std::wstring& name);
	bool DeletePath( );

	void RefreshTreeItem( const CZipTreeItem& item, int type);

	bool SaveFile( const std::wstring& file, bool bResource, bool bSaveFilesMeta, const std::wstring& lastDataPfs);

	// �����ļ��쿴��ָ��
	void AddFileViewer( CLeftViewListener* listener) { m_vtListener.push_back( listener); }

	void OnUpdateLevel( int nIndex);
	void OnSetIsPack();
	void OnSetUrl();
	void OnSetChildPackage();

	bool GetIsResource() { return m_bIsResource; }
	void SetIsResource( bool bIsResource);

	// ˢ���Ƿ����
	void RefreshIsCheck( );
	void RefreshZipLevel( );

	bool GetIsShowUnChecked() { return m_bShowUnChecked; }
	void RefreshShowUnChecked( )
	{
		m_bShowUnChecked = !m_bShowUnChecked;
		RefreshShowUnChecked( m_root, GetTreeCtrl().GetRootItem());
	}

	// �����Ƿ�����md5ֵ
	void OnSetIsMakeMD5();

	// ˢ��project��ʾ��
	void RefreshProjectShowName();

protected:
	CLeftView();           // protected constructor used by dynamic creation
	virtual ~CLeftView();

private:
	// ����ָ��Ŀ¼��Item
	void InsertChildrenOfItem( const CZipTreeItem& item, HTREEITEM hParent);

	// ����һ��
	HTREEITEM InsertItem( const CZipTreeItem& child, HTREEITEM hParent);
	// �õ�TreeItem�ľ���·��
	//void GetNameFromTreeItem( wchar_t* szName, HTREEITEM hItem);
	
	BOOL IsItemChecked( HTREEITEM hItem) const
	{
		return GetTreeCtrl().GetItemState( hItem, TVIS_STATEIMAGEMASK )>>12 == 2;
	}
	//void RefreshItemIcon( HTREEITEM hItem);
	
	static bool IsItemCanPack( const CZipTreeItem& item);
	bool IsItemDefaultLevel( const CZipTreeItem& item, bool bIsResource) const;

	void RefreshItemStateIcon( HTREEITEM hItem);

	void RefreshShowUnChecked( const CZipTreeItem& item, HTREEITEM parent);
	// �������ӽڵ��в���child	���ڷ����ֽڵ�HTREEITEM�����򷵻�null
	HTREEITEM FindZipItemInTree( const CZipTreeItem& child, HTREEITEM parent);

	HTREEITEM RefreshAddItem( const CZipTreeItem& item, HTREEITEM parent);
	void RefreshDeleteItem( HTREEITEM deleteitem);
	void SetHasChild( HTREEITEM parent, bool bHasChild);
	bool IsHasChild( const CZipTreeItem& parent) const ;

	void SetChanged();

protected:
	DECLARE_MESSAGE_MAP()

	bool							m_bIsResource;			// �Ƿ��ǰ�װ��
	bool							m_bShowUnChecked;		// �Ƿ���ʾδѡ�е�Treeitem

	CProject*				m_project;
	CZipTreeItem			m_root;

	typedef std::vector<CLeftViewListener*>	CListenerVector;
	CListenerVector					m_vtListener;

	HBRUSH							m_brush;
	HBRUSH							m_brushChildPackage;

public:
	afx_msg int OnCreate(LPCREATESTRUCT lpCreateStruct);
	afx_msg void OnTvnItemexpanding(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMRclick(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMCustomdraw(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnTvnSelchanged(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnNMClick(NMHDR *pNMHDR, LRESULT *pResult);
	afx_msg void OnDestroy();
protected:
	virtual BOOL OnCommand(WPARAM wParam, LPARAM lParam);	
	afx_msg void OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags);
};


