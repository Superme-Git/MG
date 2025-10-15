#pragma once

namespace GXWINDOW { 

namespace INLINECLASS { class CTreeNodeExpandStateMap; class CTreeDataModalDataChangeNotifySet; class CTextTreeDataVector; class CTreeViewItemVector; class CTreeMouseOnChangeNotifySet; class CTreeSelectedChangeNotifySet; struct TreeViewItem; class CIconTextTreeDataVector;} 

class GXWINDOW_EXPORTS_DLL CTreeDataModalDataChangeNotify : public CComponentNotify
{
	friend class CTreeDataModal;
public:
	CTreeDataModalDataChangeNotify();
	virtual ~CTreeDataModalDataChangeNotify();
protected:
	virtual void OnTreeDataModaDataChange( CTreeDataModal& ldm) = 0;
};

class GXWINDOW_EXPORTS_DLL CTreeNodeObject : public CUncopyableObject
{
protected:
	int			m_nid;
	unsigned int		m_uiParam;
	unsigned __int64	m_llParam;
public:
	CTreeNodeObject();
	CTreeNodeObject( int nid);
	CTreeNodeObject( const CTreeNodeObject& src);
	virtual ~CTreeNodeObject();
public:
	int  GetNodeId() const { return m_nid; }
	bool IsSameNode( const CTreeNodeObject* ptno) const;
	bool IsSameNode( const CTreeNodeObject& tno) const;
	void SetParam( unsigned int uiParam);
	void SetLongParam( unsigned __int64 llParam);
	unsigned int GetParam() const;
	unsigned __int64 GetLongParam() const;

	virtual int GetNodeLength() const;
};

class CTree;

class GXWINDOW_EXPORTS_DLL CTreeDataModal : public CUncopyableObject
{
	friend class CTree;

	CTree*		m_pParent;
	bool		m_bShow;

	INLINECLASS::CTreeDataModalDataChangeNotifySet*		m_cns;
public:
	CTreeDataModal();
	virtual ~CTreeDataModal();
public:
	virtual int GetRootCount() = 0;
	virtual CTreeNodeObject* GetRoot( int nIndex) = 0;
	virtual int GetChildCount( CTreeNodeObject* parent) = 0;
	virtual CTreeNodeObject* GetChild( CTreeNodeObject* parent, int nIndex) = 0;
	virtual bool IsLeaf( CTreeNodeObject* node);
public:
	enum CCTYPE
	{
		CCT_NORMAL		= 0,
		CCT_MOUSEON,
		CCT_SELECT,
	};
protected:
	virtual void DrawComponent( CTreeNodeObject* node, CCTYPE cct, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch) = 0;

	virtual void OnComponentShow() { m_bShow = true; };
	virtual void OnComponentHide() { m_bShow = false; };
public:
	void FireDataChanged();

	void TreeDataModalDataChangeNotifyAdd( CTreeDataModalDataChangeNotify& dcn);
	void TreeDataModalDataChangeNotifyRemove( CTreeDataModalDataChangeNotify& dcn);
};

class GXWINDOW_EXPORTS_DLL CTextTreeDataModal : public CTreeDataModal, public CSimpleTextable
{
	INLINECLASS::CTextTreeDataVector*		m_ptdv;
	CTextDrawManager*						m_tdm;
public:
	CTextTreeDataModal();
	virtual ~CTextTreeDataModal();
public:
	virtual int GetRootCount();
	virtual CTreeNodeObject* GetRoot( int nIndex);
	virtual int GetChildCount( CTreeNodeObject* parent);
	virtual CTreeNodeObject* GetChild( CTreeNodeObject* parent, int nIndex);

	virtual void SetTextColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void SetTextSelectedColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void SetTextMouseOnColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void SetTextFont( int font);

	virtual void SetTextColor( CTreeNodeObject* node, COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void SetTextSelectedColor( CTreeNodeObject* node, COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void SetTextMouseOnColor( CTreeNodeObject* node, COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
protected:
	virtual void DrawComponent( CTreeNodeObject* node, CCTYPE cct, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
public:
	CTextDrawManager& GetTextDrawManager();

	virtual CTreeNodeObject* NewRoot( const wchar_t* szText, unsigned int uiParam = 0);
	virtual bool RemoveRoot( CTreeNodeObject* node);
	virtual CTreeNodeObject* NewNode( CTreeNodeObject* parent, const wchar_t* szText, unsigned int uiParam = 0);
	virtual CTreeNodeObject* InsertNode( CTreeNodeObject* parent, CTreeNodeObject* insertbefore, const wchar_t* szText, unsigned int uiParam = 0);
	virtual bool RemoveNode( CTreeNodeObject* node, CTreeNodeObject* parent = NULL); // 注意!!! parent参数不要使用, 现在放在这里, 等梦诛相关调用修改之后会去掉
	virtual void SetNodeText( CTreeNodeObject* node, const wchar_t* szText);
	virtual const wchar_t* GetNodeText( const CTreeNodeObject* node) const;
	virtual void SetNodeParam( CTreeNodeObject* node, unsigned int uiParam);
	virtual unsigned int GetNodeParam(CTreeNodeObject* node) const;
	virtual void FreeAllTextDraw();
	virtual void RemoveAllNodes();
};

class GXWINDOW_EXPORTS_DLL CIconTextTreeDataModule : public CTreeDataModal, public CSimpleTextable
{
	INLINECLASS::CIconTextTreeDataVector*			m_pittdv;
	CTextDrawManager*								m_tdm;
public:
	CIconTextTreeDataModule();
	virtual ~CIconTextTreeDataModule();
public:
	virtual int GetRootCount();
	virtual CTreeNodeObject* GetRoot( int nIndex);
	virtual int GetChildCount( CTreeNodeObject* parent);
	virtual CTreeNodeObject* GetChild( CTreeNodeObject* parent, int nIndex);

	virtual void SetTextColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void SetTextSelectedColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void SetTextMouseOnColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void SetTextFont( int font);
protected:
	virtual void DrawComponent( CTreeNodeObject* node, CCTYPE cct, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
public:
	CTextDrawManager& GetTextDrawManager();

	virtual CTreeNodeObject* NewRoot( const wchar_t* szText, int nIcon, int nIconWidth, int nIconHeight, int nShowWidth, int nShowHeigth);
	virtual bool RemoveRoot( CTreeNodeObject* node);
	virtual CTreeNodeObject* NewNode( CTreeNodeObject* parent, const wchar_t* szText, int nIcon, int nIconWidth, int nIconHeight, int nShowWidth, int nShowHeigth);
	virtual bool RemoveNode( CTreeNodeObject* node);
	virtual void SetNodeText( CTreeNodeObject* node, const wchar_t* szText);
	virtual const wchar_t* GetNodeText( const CTreeNodeObject* node) const;
	virtual void SetNodeIcon( CTreeNodeObject* node, int nIcon, int nIconWidth, int nIconHeight, int nShowWidth, int nShowHeigth);
	virtual int GetNodeIcon( CTreeNodeObject* node) const;
	virtual int GetNodeIconWidth( CTreeNodeObject* node) const;
	virtual int GetNodeIconHeight( CTreeNodeObject* node) const;
	virtual void FreeAllTextDraw();
	virtual void RemoveAllNodes();
};

class GXWINDOW_EXPORTS_DLL CTreeMouseOnChangeNotify : public CComponentNotify
{
	friend class CTree;
public:
	CTreeMouseOnChangeNotify();
	virtual ~CTreeMouseOnChangeNotify();
protected:
	virtual void OnTreeMouseOnChange( CTree& tree, CTreeNodeObject* no) = 0;
};

class GXWINDOW_EXPORTS_DLL CTreeSelectedChangeNotify : public CComponentNotify
{
	friend class CTree;
public:
	CTreeSelectedChangeNotify();
	virtual ~CTreeSelectedChangeNotify();
protected:
	virtual void OnTreeSelectedChange( CTree& tree, CTreeNodeObject* no) = 0;
	virtual void OnTreeRightButtonSelectedChange( CTree& tree, CTreeNodeObject* no);
};

class GXWINDOW_EXPORTS_DLL CTree : public CComponent, public CTreeDataModalDataChangeNotify
{
	friend class CTreeDataModal;

	enum { 
		IMG_NORMALMOUSEON = CImageable::MOUSEON, 
		IMG_EXPAND = CImageable::USER_CUSTOM1, 
		IMG_EXPANDMOUSON = CImageable::SELECTED_MOUSEON,
		IMG_LEAF = CImageable::USER_CUSTOM2,
		IMG_LEAFMOUSEON = CImageable::USER_CUSTOM3,
		IMG_LEAFSELECTED = CImageable::USER_CUSTOM4,
	};

	INLINECLASS::CTreeMouseOnChangeNotifySet*		m_mcns;
	INLINECLASS::CTreeSelectedChangeNotifySet*		m_scns;
	INLINECLASS::CTreeViewItemVector*				m_pviv;
	INLINECLASS::CTreeNodeExpandStateMap*			m_expandstate;		// 用于记录根节点的展开状态, 目前要记录所有分支的状态并进行恢复需要考虑到节点的层次顺序, 有些困难, 所以只支持根这一层

	CTreeDataModal*		m_ptdm;

	CImageable		m_ipStats;

	int					m_nBtnWidth;
	int					m_nBtnHeight;
	int					m_nViewWidth;
	int					m_nViewHeight;
	int					m_nMaxViewWidth;	// 自适应宽度模式下, 该值表示当前最大宽度

	CTreeNodeObject*	m_pnoSelected;
	CTreeNodeObject*	m_pnoMouseOn;
	CTreeNodeObject*	m_pnoDrawing;

	bool				m_bImageBehindText;
	bool				m_bAutoViewWidth;	// 为true时根据ViewWidth实际宽度自动调节控件宽度

	COLOR				m_clrMouseOnBackGround;		// 鼠标悬浮时文字背景色
	COLOR				m_clrSelectedBackGround;	// 选中时的文字背景色
public:
	CTree();
	virtual ~CTree();
private:
	void SetSize( int w, int h);
	void CheckExpandNode( int x, int y);
	void CloseExpandNode( int nIndex);
	void MakeExpandNodeDeepVector( INLINECLASS::TreeViewItem& tvi, INLINECLASS::CTreeViewItemVector& tviv, bool bDeep);
	void DrawViewItem( INLINECLASS::TreeViewItem& tvi, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
	void CheckTreeSize();
	int GetViewItemIndex( CTreeNodeObject* pNode);
protected:
	virtual void OnMouseLeave();
	virtual void OnTreeDataModaDataChange( CTreeDataModal& ldm);
	virtual void DrawButton( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bLeaf, bool bExpand, bool bMouseOn, bool bSelected);
	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);

	virtual void OnComponentShow();
	virtual void OnComponentHide();
	virtual void ResetTreeDataModule( CTreeDataModal* tdm);

	const CTreeNodeObject* GetCurrentDrawingNode() const;
public:
	virtual void SetTreeDataModal( CTreeDataModal& tdm);
	virtual CTreeDataModal& GetTreeDataModal();
	virtual const CTreeDataModal& GetTreeDataModal() const;
	virtual CTreeDataModal* GetPtrTreeDataModal() const { return m_ptdm ; } 

	virtual void SetImageNormal( const ImageParams& ip);
	virtual const ImageParams& GetImageNormal() const;
	virtual void SetImageNormalMouseOn( const ImageParams& ip);
	virtual const ImageParams& GetImageNormalMouseOn() const;
	virtual void SetImageExpand( const ImageParams& ip);
	virtual const ImageParams& GetImageExpand() const;
	virtual void SetImageExpandMouseOn( const ImageParams& ip);
	virtual const ImageParams& GetImageExpandMouseOn() const;
	virtual void SetImageLeaf( const ImageParams& ip);
	virtual const ImageParams& GetImageLeaf() const;
	virtual void SetImageLeafMouseOn( const ImageParams& ip);
	virtual const ImageParams& GetImageLeafMouseOn() const;
	virtual void SetImageLeafSelected( const ImageParams& ip);
	virtual const ImageParams& GetImageLeafSelected() const;
	virtual void SetImageBehindText( bool bBehind);
	virtual bool IsImageBehindText() const;

	virtual void SetMouseOnBackGroundColor( COLOR clr);
	virtual COLOR GetMouseOnBackGroundColor() const;
	virtual void SetSelectedBackGroundColor( COLOR clr);
	virtual COLOR GetSelectedBackGroundColor() const;

	virtual int GetButtonWidth() const;
	virtual void SetButtonWidth( int n);
	virtual int GetButtonHeight() const;
	virtual void SetButtonHeight( int n);
	virtual int GetViewWidth() const;
	virtual void SetViewWidth( int n);
	virtual void SetAutoViewWidth( bool bAuto);
	virtual bool IsAutoViewWidth() const;
	virtual int GetViewHeight() const;
	virtual void SetViewHeight( int n);

	virtual CTreeNodeObject* GetCurrentSelect();
	virtual void SetCurrentSelect( CTreeNodeObject* tno = NULL);

	virtual CTreeNodeObject* GetNodeFromPos( int x, int y);
	virtual CTreeNodeObject* GetNodeWithoutImageFromPos( int x, int y);
	virtual void ExpandNode( CTreeNodeObject* pNode, bool bExpand, bool bDeep);
	virtual bool GetRootExpand( CTreeNodeObject* pNode);

	void TreeMouseOnChangeNotifyAdd( CTreeMouseOnChangeNotify& mcn);
	void TreeMouseOnChangeNotifyRemove( CTreeMouseOnChangeNotify& mcn);

	void TreeSelectedChangeNotifyAdd( CTreeSelectedChangeNotify& scn);
	void TreeSelectedChangeNotifyRemove( CTreeSelectedChangeNotify& scn);
public:
	virtual	bool OnLeftButtonDown( int x, int y, int key);
	virtual	bool OnRightButtonDown( int x, int y, int key);
	virtual bool OnMouseMove( int x, int y, int key);
};

} // namespace GXWINDOW { 
