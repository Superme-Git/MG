#pragma once 

namespace GXWINDOW { 

namespace INLINECLASS { class CTableDataModuleDataChangeNotifySet; class CTableHeaderVector; 
						class CTextTableDataGrid; class CTableMouseOnChangeNotifySet; 
						class CTableSelectChangeNotifySet; class CTableDataModuleRowParamSet; 
						class CTableHeaderImages; class CTableRowsImages; 
						class CTableDataModuleRowLongParamSet; } 

class GXWINDOW_EXPORTS_DLL CTableDataModuleDataChangeNotify : public CComponentNotify
{
	friend class CTableDataModule;
public:
	CTableDataModuleDataChangeNotify();
	virtual ~CTableDataModuleDataChangeNotify();
protected:
	virtual void OnTableDataModuleDataChange( CTableDataModule& ldm) = 0;
};

class GXWINDOW_EXPORTS_DLL CTableDataModule : public CUncopyableObject
{
	friend class CTableHeader;
	friend class CTable;

	CTable*		m_pParent;
	bool		m_bShow;

	INLINECLASS::CTableDataModuleDataChangeNotifySet*		m_dcn;
	//bool						m_bFixHeader;	// 为true时固定显示表头, 即在scrollpanel中表头永远居上
public:
	enum GRIDSTYLE
	{
		GRID_LINE = 0,
		GRID_IMAGE,
	};
protected:
	int							m_nCellTextOffsetX;
	COLOR						m_clrGrid;
	CImagesFill					m_imgHeaderGrid;
	CImagesFill					m_imgCellGrid;
	GRIDSTYLE					m_gridStyle;
public:
	CTableDataModule();
	virtual ~CTableDataModule();
public:
	enum CCTYPE
	{
		CCT_NORMAL		= 0,
		CCT_MOUSEON,
		CCT_SELECT,
	};
protected:
	virtual void DrawHeaderComponent( int nIndex, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, int nCellWidth, int nCellHeight) = 0;
	virtual void DrawClientComponent( int col, int row, CCTYPE cct, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, int nCellWidth, int nCellHeight) = 0;

	virtual void OnComponentShow();
	virtual void OnComponentHide();

	// 留几个控件相关的接口, 做一些嵌入控件的派生类时可能需要
	void DrawCellComponent( GXWINDOW::CComponent& c, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
	{
		c.Draw( draw, l, t, cx, cy, cw, ch);
	}
	void ComponentShow( GXWINDOW::CComponent& c) { c.OnComponentShow(); }
	void ComponentHide( GXWINDOW::CComponent& c) { c.OnComponentHide(); }
	void ComponentMouseEnter( GXWINDOW::CComponent& c) { c.OnMouseEnter(); }
	void ComponentMouseLeave( GXWINDOW::CComponent& c) { c.OnMouseLeave(); }

	virtual void OnMouseEnter() {}
	virtual void OnMouseLeave() {}
	virtual bool OnMouseMove( int x, int y, int key, int col, int row) { return false; }
	virtual bool OnLeftButtonUp( int x, int y, int key, int col, int row) { return false; }
	virtual bool OnLeftButtonDown( int x, int y, int key, int col, int row) { return false; }
public:
	virtual int GetColumnCount() = 0;
	virtual int GetRowCount() = 0;

	virtual void SetGridColor( COLOR clr);
	virtual COLOR GetGridColor() const;

	//virtual void SetFixTableHeader( bool fix = true);
	//virtual bool IsFixTableHeader() const;

	virtual void SetCellTextOffset( int x) { m_nCellTextOffsetX = x; }
	virtual int GetCellTextOffsetX() const { return m_nCellTextOffsetX; }

	virtual CImagesFill& GetCellGridImagesFill();
	virtual CImagesFill& GetHeaderGridImagesFill();
	virtual void SetGridStyle( GRIDSTYLE style);

	void FireDataChanged();

	CTable* GetParentTable() { return m_pParent; }

	void TableDataModuleDataChangeNotifyAdd( CTableDataModuleDataChangeNotify& dcn);
	void TableDataModuleDataChangeNotifyRemove( CTableDataModuleDataChangeNotify& dcn);

private:
	void SetParentTable( CTable* table) { m_pParent = table; }
};

class GXWINDOW_EXPORTS_DLL CTextTableDataModule : public CTableDataModule, public CSimpleTextable
{
	INLINECLASS::CTextTableDataGrid*		m_grid;
	INLINECLASS::CTableDataModuleRowParamSet*		m_params;
	INLINECLASS::CTableDataModuleRowLongParamSet*	m_longparams;
	CTextDrawManager*						m_tdm;

	int							m_nFontHeader;
	COLOR						m_clrHeader;
	COLOR						m_clrHeaderGradual;
	COLOR						m_clrRowSelected;
	COLOR						m_clrRowMouseIn;
	COLOR						m_clrHeaderEffect;
	FONTEFEECTFLAG				m_fefHeader;

	TEXTALIGNFLAG				m_taFlag;
	bool						m_bMultilines;
public:
	CTextTableDataModule();
	virtual ~CTextTableDataModule();
protected:
	virtual void DrawHeaderComponent( int nIndex, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, int nCellWidth, int nCellHeight);
	virtual void DrawClientComponent( int col, int row, CCTYPE cct, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, int nCellWidth, int nCellHeight);
	virtual void DrawMultilineClientComponent( int col, int row, CCTYPE cct, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, int nCellWidth, int nCellHeight);
public:
	virtual int GetColumnCount();
	virtual int GetRowCount();

	virtual void SetTextColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void SetTextSelectedColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void SetTextMouseOnColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void SetTextFont( int font);

	virtual void SetCellTextColor( int col, int row, COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void SetCellTextSelectedColor( int col, int row, COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void SetCellTextMouseOnColor( int col, int row, COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	
	virtual void SetRowTextColor( int row, COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void SetRowTextSelectedColor( int row, COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void SetRowTextMouseOnColor( int row, COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);

	virtual int GetTableHeaderFont() const;
	virtual void SetTableHeaderFont( int font);
	virtual COLOR GetTableHeaderColor() const;
	virtual COLOR GetTableHeaderGradualColor() const;
	virtual FONTEFEECTFLAG GetTableHeaderEffect() const;
	virtual COLOR GetTableHeaderEffectColor() const;
	virtual void SetTableHeaderColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
public:
	virtual void InsertColumn( const wchar_t* szTitle);
	virtual void InsertColumn( int nAt, const wchar_t* szTitle);
	virtual const wchar_t* GetColumnText( int n) const;
	virtual void RemoveColumn( int n);
	virtual void ClearColumns();
	virtual void SetColumnText( int n, const wchar_t* szTitle);

	virtual void SetCellText( int col, int row, const wchar_t* szText);
	virtual const wchar_t* GetCellText( int col, int row);
	virtual int InsertRow( int n, unsigned int uiParam = 0);
	virtual int InsertRow();
	virtual void SetRowCount( int n);
	virtual void RemoveRow( int row);
	virtual void ClearRows();

	virtual void SetRowParam( int row, unsigned int uiParam);
	virtual unsigned int GetRowParam( int row) const;

	virtual void SetRowLongParam( int row, unsigned __int64 ulParam);
	virtual unsigned __int64 GetRowLongParam( int row) const;

	virtual void FreeAllTextDraw();
	CTextDrawManager& GetTextDrawManager();

	virtual void SetBackSelectedColor( COLOR clr);
	virtual void SetBackMouseOnColor( COLOR clr);

	virtual void SetTextAlign( TEXTALIGNFLAG flag);
	virtual TEXTALIGNFLAG GetTextAlign() const;

	virtual void SetMultilines( bool bMultiline);
	virtual bool IsMultilines() const;

	virtual int GetCellGUID( int col, int row) const;
};

class GXWINDOW_EXPORTS_DLL CImageTextTableDataModule : public CTextTableDataModule
{
	INLINECLASS::CTableRowsImages*		m_rowImages;
	bool m_bShow;
	int  m_nCellImageOffsetX;
public:
	CImageTextTableDataModule();
	virtual ~CImageTextTableDataModule();
protected:
	virtual void DrawHeaderComponent( int nIndex, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, int nCellWidth, int nCellHeight);
	virtual void DrawClientComponent( int col, int row, CCTYPE cct, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, int nCellWidth, int nCellHeight);
	virtual void DrawMultilineClientComponent( int col, int row, CCTYPE cct, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, int nCellWidth, int nCellHeight);

	virtual void OnComponentShow();
	virtual void OnComponentHide();
public:
	virtual CImagesFill* SetCellImage( int col, int row);
	virtual CImagesFill* GetCellImage( int col, int row);
	virtual void RemoveCellImage( int col, int row);

	virtual void SetCellImageOffset( int x) { m_nCellImageOffsetX = x; }
	virtual int GetCellImageOffsetX() const { return m_nCellImageOffsetX; }

	virtual void RemoveRow( int row);
	virtual void ClearRows();
};

struct GXWINDOW_EXPORTS_DLL CTableHeaderColumn
{
	int		nWidth;
	bool	bSizable;
	int		nMin;
	int		nMax;
};

class GXWINDOW_EXPORTS_DLL CTableHeader : public CBKComponent
{
	friend class CTable;

	INLINECLASS::CTableHeaderVector*	m_thv;
	CTableDataModule*					m_ptdm;
	INLINECLASS::CTableHeaderImages*	m_pheaderimgs;
	int									m_nCurSizeCol;
	int									m_nLastSizeColX;
	bool								m_bImageTitleAlignCenter;

public:
	CTableHeader();
	virtual ~CTableHeader();
private:
	void SetSize( int w, int h);

	void SetColumnCount( int n);
	void SetTableDataModule( CTableDataModule& tdm);

	bool OnLeftButtonUp( int x, int y, int key);
	bool OnLeftButtonDown( int x, int y, int key);
	bool OnMouseMove( int x, int y, int key);

	void DoSizeHeaderColumn( int x);
	int DoGetColumnIndexFromCX( int cx);

	void AdjustColumnImagesPos( int index);
	void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);

protected:
	virtual void OnComponentShow();
	virtual void OnComponentHide();

public:
	int GetColumnCount() const;

	CTableHeaderColumn& GetHeaderColumn( int n);
	const CTableHeaderColumn& GetHeaderColumn( int n) const;

	int ColumnIndexFromPoint( int x, int y);
	
	void FireTableHeaderColumnSizeChanged();

	void SetHeaderHeight( int n);

	void SetHeaderCellImage( int index, const ImageParams& ip, int width, int height);
	void RemoveHeaderCellImage( int index);
	void SetHeaderCellImageAlignCenter( bool bCenter = true);
	bool IsHeaderCellImageAlignCenter() const;
};

class GXWINDOW_EXPORTS_DLL CTableMouseOnChangeNotify : public CComponentNotify
{
	friend class CTable;
public:
	CTableMouseOnChangeNotify();
	virtual ~CTableMouseOnChangeNotify();
protected:
	virtual void OnTableMouseOnChangeNotify( CTable& t, int n, int o) = 0;
};

class GXWINDOW_EXPORTS_DLL CTableSelectChangeNotify : public CComponentNotify
{
	friend class CTable;
public:
	CTableSelectChangeNotify();
	virtual ~CTableSelectChangeNotify();
protected:
	virtual void OnTableSelectChangeNotify( CTable& t, int n, int o) = 0;
};

class GXWINDOW_EXPORTS_DLL CTable : public CBKComponent, public CTableDataModuleDataChangeNotify, public CComponentSizeChangedNotify
{
	friend class CTableDataModule;

	CTableHeader		m_th;
	CTableDataModule*	m_ptdm;

	CComponent*			m_pLastMouseIn;

	int					m_nRowHeight;
	int					m_nSelectedRow;
	int					m_nMouseOnRow;

	INLINECLASS::CTableMouseOnChangeNotifySet*	m_tmcns;
	INLINECLASS::CTableSelectChangeNotifySet*	m_tscns;

	CImagesFill			m_ifMouseOn;
	CImagesFill			m_ifSelected;
public:
	CTable();
	virtual ~CTable();
public:
	CTableHeader& GetTableHeader();
	virtual void SetTableHeaderHeight( int n);

	void SetTableDataModule( CTableDataModule& tdm);

	virtual void SetRowHeight( int n);
	virtual int  GetRowHeight();

	int ColumnIndexFromPoint( int x, int y);
	int RowIndexFromPoint( int x, int y);

	int GetSelectRow() const;
	void SetSelectRow( int n);

	void TableMouseOnChangeNotifyAdd( CTableMouseOnChangeNotify& mcn);
	void TableMouseOnChangeNotifyRemove( CTableMouseOnChangeNotify& mcn);
	void TableSelectChangeNotifyAdd( CTableSelectChangeNotify& scn);
	void TableSelectChangeNotifyRemove( CTableSelectChangeNotify& scn);
protected:
	virtual void OnTableDataModuleDataChange( CTableDataModule& ldm);
	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
	virtual void OnMouseLeave();

	virtual void OnComponentShow();
	virtual void OnComponentHide();
	virtual void ResetTableDataModule( CTableDataModule* tdm);
private:
	void OnComponentSizeChanged( CComponent& c, int nw, int nh, int ow, int oh);

	void CheckTableComponentSize();
	void SetSize( int w, int h);
public:
   	virtual bool OnLeftButtonUp( int x, int y, int key);
	virtual	bool OnLeftButtonDown( int x, int y, int key);
	virtual bool OnMouseMove( int x, int y, int key);

	virtual CImagesFill& GetRowMouseOnImage();
	virtual CImagesFill& GetRowSelectedImage();
};

} // namespace GXWINDOW { 
