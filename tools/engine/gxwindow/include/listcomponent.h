#pragma once

namespace GXWINDOW { 

namespace INLINECLASS { 

class CListDataStringList; 
class CListDataColorStringList;
class CListDataImageList;
class CListDataModuleDataChangeSet; 
class CListSelectIndexSet; 
class CListBoxSelectChangeNotifySet; 
class CComboBoxSelectChangeNotifySet; 
class CListBoxMouseOnChangeNotifySet;

}; // namespace INLINECLASS { 

class CListBox;

class GXWINDOW_EXPORTS_DLL CListDataModuleDataChangeNotify : public CComponentNotify
{
	friend class CListDataModule;
public:
	CListDataModuleDataChangeNotify();
	virtual ~CListDataModuleDataChangeNotify();
protected:
	virtual void OnListDataModuleDataChange( CListDataModule& ldm) = 0;
};

class GXWINDOW_EXPORTS_DLL CListDataModule : public CUncopyableObject
{
	friend class CListBox;
	friend class CComboBox;

	INLINECLASS::CListDataModuleDataChangeSet*	m_nset;
	CListBox*			m_pParent;
	bool				m_bShow;
	float				m_fZoomScale;
	CImagesFill			m_bgRowImg;
public:
	CListDataModule();
	virtual ~CListDataModule();
public:
	enum CCTYPE
	{
		CCT_NORMAL		= 0,
		CCT_MOUSEON,
		CCT_SELECT,
	};
protected:
	virtual void DrawClientComponent( int nIndex, CCTYPE cct, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, int clientW, int clientH, bool bDrawFullBG = false) = 0;
	virtual void DrawRowBackGround( int nIndex, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bDrawFullBG);

	virtual void OnComponentShow();
	virtual void OnComponentHide();

public:
	virtual bool OnLeftButtonDown( int n, int x, int y, int key) { return false; };
	virtual bool OnLeftButtonUp( int n, int x, int y, int key) { return false; };
	virtual bool OnLeftButtonDoubleClick( int n, int x, int y, int key) { return false; };

	virtual bool OnRightButtonDown( int n, int x, int y, int key) { return false; };
	virtual bool OnRightButtonUp( int n, int x, int y, int key) { return false; };
	virtual bool OnRightButtonDoubleClick( int n, int x, int y, int key) { return false; };

	virtual bool OnMouseMove( int n, int x, int y, int key) { return false; };
	virtual bool OnMouseWheel( int n, int x, int y, int key) { return false; };
public:
	virtual int GetClientComponentCount() const = 0;

	// 在调用完listbox的insert方法后, 需要主动调用该方法通知变更, 否则可能会出现列表最后一行高度错误的问题
	virtual void FireDataChanged();

	void ListDataModuleDataChangeNotifyAdd( CListDataModuleDataChangeNotify& dcn);
	void ListDataModuleDataChangeNotifyRemove( CListDataModuleDataChangeNotify& dcn);
	virtual CImagesFill& GetRowBackGround();

	CListBox* GetParent() { return m_pParent; }
};

class GXWINDOW_EXPORTS_DLL CImageListDataModlue : public CListDataModule
{
	COLOR							m_SelectColor;
	COLOR							m_MouseOnColor;
	
	INLINECLASS::CListDataImageList*				m_pList;

public:
	CImageListDataModlue();
	virtual ~CImageListDataModlue();

protected:
	virtual void DrawClientComponent( int nIndex, CCTYPE cct, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, int clientW, int clientH, bool bDrawFullBG = false);
	
	virtual void OnComponentShow();
	virtual void OnComponentHide();
public:
	virtual int GetClientComponentCount() const;

	virtual void InsertItem( ImageParams& ip,  unsigned int uiParam = 0 );
	virtual void RemoveItem( int nIndex );
	virtual void RemoveAll();
	virtual int  GetParam( int nIndex ) const;

	virtual void SetImageSelectedColor( COLOR clr);
	virtual void SetImageMouseOnColor( COLOR clr);

};

class GXWINDOW_EXPORTS_DLL CTextListDataModule : public CListDataModule, public CSimpleTextable
{
	CTextDrawManager*			m_tdm;

	INLINECLASS::CListDataStringList*	m_plist;
	COLOR						m_clrRowBGMouseOn;
	COLOR						m_clrRowBGSelected;

	TEXTALIGNFLAG				m_taFlag;
public:
	CTextListDataModule();
	virtual ~CTextListDataModule();
protected:
	virtual void DrawClientComponent( int nIndex, CCTYPE cct, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, int clientW, int clientH, bool bDrawFullBG = false);
public:
	virtual int GetClientComponentCount() const;

	virtual void InsertText( const wchar_t* szText, unsigned int uiParam = 0, unsigned __int64 ulParam = 0);
	virtual const wchar_t* GetText( int nIndex) const;
	virtual void SetText( int nIndex, const wchar_t* szText);
	virtual void RemoveText( int nIndex);
	virtual void RemoveAll();
	virtual int GetTextCount() const;

	virtual void SetParam( int nIndex, unsigned int uiParam);
	virtual unsigned int GetParam( int nIndex) const;
	virtual void SetLongParam( int nIndex, unsigned __int64 ulParam);
	virtual unsigned __int64 GetLongParam( int nIndex) const;

	virtual void SetTextColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void SetTextSelectedColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void SetTextMouseOnColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void SetTextFont( int font);

	virtual COLOR GetTextColor() const;
	virtual COLOR GetTextSelectedColor() const;
	virtual COLOR GetTextMouseOnColor() const;
	virtual int GetTextFont() const;

	virtual void SetTextColor( int nIndex, COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void SetTextSelectedColor( int nIndex, COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void SetTextMouseOnColor( int nIndex, COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);

	virtual void FreeAllTextDraw();

	virtual void SetBackGroundMouseOnColor( COLOR clr);
	virtual void SetBackGroundSelectedColor( COLOR clr);

	virtual void SetTextAlign( TEXTALIGNFLAG flag);
	virtual TEXTALIGNFLAG GetTextAlign() const;
};

class GXWINDOW_EXPORTS_DLL CListBoxSelectChangeNotify : public CComponentNotify
{
	friend class CListBox;
public:
	CListBoxSelectChangeNotify();
	virtual ~CListBoxSelectChangeNotify();
protected:
	virtual void OnListBoxSelectChange( CListBox& lb, int n) = 0;
};

class GXWINDOW_EXPORTS_DLL CListBoxMouseOnChangeNotify : public CComponentNotify
{
	friend class CListBox;
public:
	CListBoxMouseOnChangeNotify();
	virtual ~CListBoxMouseOnChangeNotify();
protected:
	virtual void OnListBoxMouseOnChange( CListBox& lb, int n, int o) = 0;
};

class GXWINDOW_EXPORTS_DLL CListBox : public CBKComponent, public CListDataModuleDataChangeNotify
{
	friend class CComboBox;
	friend class CListDataModule;

	INLINECLASS::CListBoxSelectChangeNotifySet*		m_scns;
	INLINECLASS::CListBoxMouseOnChangeNotifySet*	m_mons;

	bool								m_bMouseRightDownSendNotify; // 默认为true,鼠标右键按下时发出OnListBoxSelectChange通告
	int									m_nClientHeight;
	int									m_nCurrentSelected;
	int									m_nMouseOnIndex;
	CListDataModule*					m_pDataModule;
	INLINECLASS::CListSelectIndexSet*	m_sis;

	CImagesFill							m_SelectedHighlightIamge;	// 选中项的高亮背景图
	CImagesFill							m_MouseOnHighlightImage;	// 鼠标悬浮项的高亮背景图
public:
	CListBox();
	virtual ~CListBox();
public:
	virtual void SetListDataModule( CListDataModule& ldm);
	virtual CListDataModule& GetListDataModule();

	virtual void SetClientComponentHeight( int n);
	virtual int GetClientComponentHeight() const;
protected:
	virtual void OnMouseLeave();
	virtual void OnListDataModuleDataChange( CListDataModule& ldm);
	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);

	virtual void OnComponentShow();
	virtual void OnComponentHide();
	virtual void ResetListDataModule( CListDataModule* ldm);
public:
	virtual int GetSelectIndex() const;
	virtual void SetSelectIndex( int nIndex);

	virtual int GetHoverIndex() const;

	virtual void SetSelect( int nIndex, bool b = true);
	virtual bool IsSelected( int nIndex) const;

	virtual bool IsMultiSelect() const;
	virtual void SetMultiSelect( bool b = true);

	virtual int GetIndexByPoint( int x, int y);

	virtual bool OnLeftButtonDown( int x, int y, int key);
	virtual bool OnLeftButtonUp( int x, int y, int key);
//	virtual bool OnLeftButtonDoubleClick( int x, int y, int key);

	virtual bool OnRightButtonDown( int x, int y, int key);
	virtual bool OnRightButtonUp( int x, int y, int key);
//	virtual bool OnRightButtonDoubleClick( int x, int y, int key);

	virtual bool OnMouseMove( int x, int y, int key);
	virtual bool OnMouseWheel( int x, int y, int key);

	virtual void SetRightButtonDownSendNotify( bool b);
	virtual bool IsRightButtonDownSendNotify() const;

	virtual CImagesFill& GetSelectedHightLightImage();
	virtual CImagesFill& GetMouseOnHighLightImage();
	
	void ListBoxSelectChangeNotifyAdd( CListBoxSelectChangeNotify& scn);
	void ListBoxSelectChangeNotifyRemove( CListBoxSelectChangeNotify& scn);

	void ListBoxMouseOnChangeNotifyAdd( CListBoxMouseOnChangeNotify& mcn);
	void ListBoxMouseOnChangeNotifyRemove( CListBoxMouseOnChangeNotify& mcn);
};

class GXWINDOW_EXPORTS_DLL CCombinationListBoxAllocator : public CUncopyableObject
{
public:
	CCombinationListBoxAllocator();
	virtual ~CCombinationListBoxAllocator();
public:
	virtual CScrollPanel* NewScrollPanel();
	virtual CListBox* NewListBox();
	virtual void DeleteComponent( CComponent* pc);
public:
	static CCombinationListBoxAllocator& GetInstance();
};

class GXWINDOW_EXPORTS_DLL CCombinationListBox : public CUncopyableObject
{
	CCombinationListBoxAllocator*	m_alloc;
	CScrollPanel*		m_sp;
	CListBox*			m_lb;
public:
	CCombinationListBox( CCombinationListBoxAllocator& alloc = CCombinationListBoxAllocator::GetInstance());
	virtual ~CCombinationListBox();
public:
	CScrollPanel& GetScrollPanel();
	CListBox& GetListBox();
public:
	void SetSize( int w, int h, int lw);
	void SetPos( int x, int y);
	void SetListDataModule( CListDataModule& ldm);
};

class GXWINDOW_EXPORTS_DLL CComboBoxSelectChangeNotify : public CComponentNotify
{
	friend class CComboBox;
public:
	CComboBoxSelectChangeNotify();
	virtual ~CComboBoxSelectChangeNotify();
protected:
	virtual void OnComboBoxSelectChange( CComboBox& lb, int n) = 0;
};

class GXWINDOW_EXPORTS_DLL CComboBoxComponentAllocator : public CUncopyableObject
{
public:
	CComboBoxComponentAllocator();
	virtual ~CComboBoxComponentAllocator();
public:
	virtual CScrollPanel* NewListScrollPanel();
	virtual void DeleteListScrollPanel( CScrollPanel* sp);
	virtual CImageButton* NewBoxImageButton();
	virtual void DeleteImageButton( CImageButton* ib);
	virtual CListBox* NewListBox();
	virtual void DeleteListBox( CListBox* lb);
};

class GXWINDOW_EXPORTS_DLL CComboBox : public CBKComponent, public CListBoxSelectChangeNotify, public CFrameShowHideNotify
{
	INLINECLASS::CComboBoxSelectChangeNotifySet*	m_scns;
	CComboBoxComponentAllocator*					m_Alloc;

	CListBox*			m_list;
	CFrame				m_frame;
	CScrollPanel*		m_panel;
	CImageButton*		m_btn;
	int					m_nListHeight;
	int					m_nListWidth;
	int					m_nBorderWidth;
	bool				m_bDownDraw;
public:
	CComboBox();
	CComboBox( CComboBoxComponentAllocator& alloc);
	virtual ~CComboBox();
private:
	void Initialize( CComboBoxComponentAllocator& alloc);
protected:
	virtual void OnListBoxSelectChange( CListBox& lb, int n);
	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);

	virtual void OnComponentShow();
	virtual void OnComponentHide();

	virtual void OnMouseEnter();
	virtual void OnMouseLeave();

	virtual void OnFrameShowHide( CFrame& c, bool show);
public:
	virtual void SetListDataModule( CListDataModule& ldm);
	virtual CListDataModule& GetListDataModule();

	virtual int GetSelectIndex() const;
	virtual void SetSelectIndex( int nIndex);

	virtual void SetButtonWidth( int w);
	virtual int GetButtonWidth();

	virtual void SetListBoxSize( int w, int h);
	virtual int GetListBoxHeight() const;
	virtual int GetListBoxWidth() const;
	
	virtual void SetClientComponentHeight( int n);
	virtual int GetClientComponentHeight() const;

	virtual void SetSize( int w, int h);
	virtual void SetBorderWidth( int n);

	virtual void SetDownDraw( bool bDown = true);
	virtual bool IsDownDraw() const;

	virtual void ShowListBox();
	virtual void HideListBox();

	virtual void SetEnable( bool bEnable = true);

	virtual bool OnLeftButtonUp( int x, int y, int key);
	virtual bool OnLeftButtonDown( int x, int y, int key);
	virtual bool OnMouseMove( int x, int y, int key);

//	virtual bool OnLeftButtonDoubleClick( int x, int y, int key);
	virtual bool OnRightButtonDown( int x, int y, int key);
	virtual bool OnRightButtonUp( int x, int y, int key);
//	virtual bool OnRightButtonDoubleClick( int x, int y, int key);
	virtual bool OnMouseWheel( int x, int y, int key);

	void SelectChangeNotifyAdd( CComboBoxSelectChangeNotify& scn);
	void SelectChangeNotifyRemove( CComboBoxSelectChangeNotify& scn);

	CImageButton* GetButton() const;
	CScrollPanel* GetScrollPanel() const;

	CImagesFill& GetDrawListBackGround();
	void SetDrawListBackGroundColor( GXWINDOW::COLOR clr);

	CImagesFill& GetSelectedHightLightImage();
	CImagesFill& GetMouseOnHighLightImage();
public:
	virtual void SetBackGroundStyle( BackGroundStyle bgs);
};

} // namespace GXWINDOW { 


