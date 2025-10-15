#pragma once

namespace GXWINDOW {

namespace INLINECLASS {  class CPackageCellClickNotifySet; class CPackageCellVector; }

class GXWINDOW_EXPORTS_DLL CPackageCellClickNotify : public CComponentNotify
{
	friend class CAbstactPackageCell;
public:
	CPackageCellClickNotify();
	virtual ~CPackageCellClickNotify();
protected:
	virtual void OnPackageCellClick( CAbstactPackageCell& cell, int key) = 0;
	virtual void OnPackageCellRClick( CAbstactPackageCell& cell) = 0;
	virtual void OnPackageCellMouseMove( CAbstactPackageCell& cell) = 0;
};

class GXWINDOW_EXPORTS_DLL CAbstactPackageCell : public CComponent
{
	friend class CAbstactPackagePanel;

	bool				m_bSelected;
	bool				m_bMouseIn;

	INLINECLASS::CPackageCellClickNotifySet*	m_NotifySet;
public:
	CAbstactPackageCell();
	virtual ~CAbstactPackageCell();
protected:
	virtual void Draw( GXWINDOW::CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
	virtual void OnMouseEnter();
	virtual void OnMouseLeave();
	virtual bool OnLeftButtonUp( int x, int y, int key);
	virtual bool OnRightButtonUp(int x, int y, int key);
	virtual bool OnMouseMove(int x, int y, int key);
protected:
	virtual void OnDrawSelect( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
	virtual void OnDrawIcon( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch) = 0;
	virtual void OnDrawMouseIn( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
public:
	virtual bool IsSelected() const;
	virtual void SetSelect( bool bSelect);
	bool IsMouseIn() const;

	void CellClickNotifyAdd( CPackageCellClickNotify& pccn);
	void CellClickNotifyRemove( CPackageCellClickNotify& pccn);

private:

#ifdef _GXW_CELL_CHECK
	bool m_bWindowsUpdating; // 表示控件当前正在操作中, 此时有删除操作给出警告
	void InitializeWindowsUpdating() { m_bWindowsUpdating = false; }
	void UPDATE_CHECK_BEGIN() { m_bWindowsUpdating = true; }
	void UPDATE_CHECK_END() { m_bWindowsUpdating = false; }
	void UPDATE_CHECK_ASSERT() 
	{ 
		assert( !m_bWindowsUpdating && "当前格子控件正在操作中, 不能进行删除操作!"); 
	}
#else
	bool m_bWindowsUpdating;
	void InitializeWindowsUpdating() {}
	void UPDATE_CHECK_BEGIN() {}
	void UPDATE_CHECK_END() {}
	void UPDATE_CHECK_ASSERT() {}
#endif
};

class GXWINDOW_EXPORTS_DLL CPackagePanelLayout : public CUncopyableObject
{
	friend class CAbstactPackagePanel;
public:
	CPackagePanelLayout();
	virtual ~CPackagePanelLayout();
public:
	virtual CPackagePanelLayout* Clone() const = 0;
	virtual void Delete() = 0;
protected:
	virtual void Layout( CAbstactPackagePanel& panel) = 0;
};

class GXWINDOW_EXPORTS_DLL CGridPackagePanelLayout : public CPackagePanelLayout
{
	int			m_nCols;
	int			m_nRows;
	int			m_nVerticalSpace;
	int			m_nHorizontalSpace;
	int			m_nXMargin;
	int			m_nYMargin;
public:
	CGridPackagePanelLayout();
	virtual ~CGridPackagePanelLayout();
public:
	virtual CPackagePanelLayout* Clone() const;
	virtual void Delete();
protected:
	virtual void Layout( CAbstactPackagePanel& panel);
public:
	virtual void SetRowCol( int nRows, int nCols);
	virtual int GetColCount() const;
	virtual int GetRowCount() const;

	virtual void SetSpace( int nVerticalSpace, int nHorizontalSpace);
	virtual int GetVerticalSpace() const;
	virtual int GetHorizontalSpace() const;

	virtual void SetMargin( int xmargin, int ymargin);
	virtual int GetXMargin() const;
	virtual int GetYMargin() const;
};

class GXWINDOW_EXPORTS_DLL CAbstactPackagePanel : public CPanel, public CPackageCellClickNotify
{
protected:
	int			m_nCount;
	int			m_nCellWidth;
	int			m_nCellHeight;

	INLINECLASS::CPackageCellVector*			m_cells;
	CPackagePanelLayout*						m_layout;
	INLINECLASS::CPackageCellClickNotifySet*	m_NotifySet;
public:
	CAbstactPackagePanel();
	virtual ~CAbstactPackagePanel();
protected:
	void DoUninitialize();

	virtual CAbstactPackageCell* NewCell() = 0;
	virtual void DeleteCell( CAbstactPackageCell* pc) = 0;
	virtual void ClearCell();
	virtual void Relayout();

	void CreateOneCell();
	void RemoveOneCell( CAbstactPackageCell* pc);
public:
	virtual void SetCount( int count);
	int GetCount() const;

	void Clear();

	const CAbstactPackageCell& GetCell( int nIndex) const;
	CAbstactPackageCell& GetCell( int nIndex);

	void SetCellSize( int w, int h);
	int GetCellWidth() const;
	int GetCellHeight() const;
	int GetAbsCellWidth() const;
	int GetAbsCellHeight() const;
public:
	virtual void SetLayout( const CPackagePanelLayout& ppl);
	virtual const CPackagePanelLayout& GetLayout() const;
	virtual CPackagePanelLayout& GetLayout();

	void CellClickNotifyAdd( CPackageCellClickNotify& pccn);
	void CellClickNotifyRemove( CPackageCellClickNotify& pccn);
private:
	void DoInitialize();
	void CreateCell();
};


} // namespace GXWINDOW {