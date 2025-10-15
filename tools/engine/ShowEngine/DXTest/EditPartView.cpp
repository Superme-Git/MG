// EditPartView.cpp : implementation file
//

#include "stdafx.h"
#include "DXTest.h"
#include "EditPartView.h"
#include "BrowsePath.h"
#include <algorithm>

typedef struct
{
	LPCTSTR name;
	LPCTSTR		filename;
	int     layer;
	int			type;
} LayerInfo;

// 把逻辑上相邻的层放在一起便于查找
LayerInfo g_layer[] = { 

{ _T("头发B"),				_T("hairb"),			MH_SHOW_HAIR_B,			SHOW_TYPE_UNDEFINE	},
{ _T("头发A"),				_T("haira"),			MH_SHOW_HAIR_A,			SHOW_TYPE_HAIR	},

{ _T("衣服T恤"),			_T("shirt"),			MH_SHOW_SHIRT,			SHOW_TYPE_COAT	},
{ _T("衣服T恤 袖子(左)"),	_T("shirt sleevel"),	MH_SHOW_SHIRT_SLEEVE_L,	SHOW_TYPE_UNDEFINE	},
{ _T("衣服T恤 袖子(右)"),	_T("shirt sleever"),	MH_SHOW_SHIRT_SLEEVE_R,	SHOW_TYPE_UNDEFINE	},

{ _T("外衣"),				_T("coat"),				MH_SHOW_COAT,			SHOW_TYPE_COAT	},
{ _T("外衣 袖子(左)"),		_T("coat sleevel"),		MH_SHOW_COAT_SLEEVE_L,	SHOW_TYPE_UNDEFINE	},
{ _T("外衣 袖子(右)"),		_T("coat sleever"),		MH_SHOW_COAT_SLEEVE_R,	SHOW_TYPE_UNDEFINE	},

{ _T("裤子(鞋里面)"),		_T(""),					MH_SHOW_TROUSERS_INNER,	SHOW_TYPE_TROUSERS	},
{ _T("裤子(鞋外面)"),		_T("trousers"),			MH_SHOW_TROUSERS_OUTER,	SHOW_TYPE_TROUSERS	},

{ _T("袜子"),				_T("socks"),			MH_SHOW_SOCKS,			SHOW_TYPE_TROUSERS	},
{ _T("鞋"),					_T("shoes"),			MH_SHOW_SHOES,			SHOW_TYPE_SHOES },

{ _T("皮带"),				_T("waistband"),		MH_SHOW_WAISTBAND,		SHOW_TYPE_ACCESSORIES_A },

{ _T("帽子B"),				_T("hatb"),				MH_SHOW_HAT_B,			SHOW_TYPE_UNDEFINE },
{ _T("帽子A（可移动）"),	_T("hata"),				MH_SHOW_HAT_A,			SHOW_TYPE_HAT },

{ _T("脸型和耳朵"),			_T("face"),				MH_SHOW_HEAD_EAR,		SHOW_TYPE_HEAD_EAR },
{ _T("胡子"),				_T("beard"),			MH_SHOW_BEARD,			SHOW_TYPE_FACEWEAR },
{ _T("五官和耳环 (耳环)"),	_T("earring"),			MH_SHOW_EARRING,		SHOW_TYPE_FACEWEAR  },
{ _T("五官和耳环 (眉毛)"),	_T("brows"),			MH_SHOW_BROWS,			SHOW_TYPE_FACEWEAR },
{ _T("五官和耳环 (嘴)"),	_T("mouth"),			MH_SHOW_MOUTH,			SHOW_TYPE_FACEWEAR  },
{ _T("五官和耳环 (鼻子)"),	_T("nose"),				MH_SHOW_NOSE,			SHOW_TYPE_FACEWEAR  },
{ _T("五官和耳环 (眼睛)"),	_T("eyes"),				MH_SHOW_EYE,			SHOW_TYPE_FACEWEAR  },

{ _T("背景"),				_T("background"),		MH_SHOW_BACK_GROUND,	SHOW_TYPE_BACK_GROUND },
{ _T("翅膀"),				_T("swing"),			MH_SHOW_WING,			SHOW_TYPE_WING },
{ _T("配饰（可移动）"),		_T("accessorites"),		MH_SHOW_ACCESSORIES_A,	SHOW_TYPE_ACCESSORIES_A },
{ _T("围巾（可移动）"),		_T("necklace"),			MH_SHOW_SHAWL,			SHOW_TYPE_SHAWL },
{ _T("周边装饰（可移动）"), _T("handbag"),			MH_SHOW_ACCESSORIES_B,	SHOW_TYPE_BAG },
{ _T("宠物（可移动）"),		_T("pet"),				MH_SHOW_PET,			SHOW_TYPE_NORMAL_PET },
{ _T("字幕（可移动）"),		_T("subtiting"),		MH_SHOW_SUBTITLING,		SHOW_TYPE_SUBTITLING },
{ _T("相框"),				_T("photoframe"),		MH_SHOW_PHOTO_FRAME,	SHOW_TYPE_PHOTO_FRAME },

{ _T("披风B"),				_T("mantleb"),			MH_SHOW_MANTLE_B,		SHOW_TYPE_UNDEFINE},
{ _T("披风A"),				_T("mantlea"),			MH_SHOW_MANTLE_A,		SHOW_TYPE_MANTLE },

{ _T("交通工具"),			_T("vehicle"),			MH_SHOW_VEHICLE,		SHOW_TYPE_VEHICLE },
{ _T("同伴"),				_T("companion"),		MH_SHOW_COMPANION,		SHOW_TYPE_ASSOCIATE },
{ _T("文身（可移动）"),		_T("tattoo"),			MH_SHOW_TATTOO,			SHOW_TYPE_ACCESSORIES_A },
{ _T("杂志 "),				_T("magzine"),			MH_SHOW_MAGZINE,		SHOW_TYPE_AMBITUS },

{ _T("手臂(右)"),			_T("armr"),				MH_SHOW_ARM_R,			SHOW_TYPE_UNDEFINE },
{ _T("腿"),					_T("leg"),				MH_SHOW_LEG,			SHOW_TYPE_UNDEFINE },
{ _T("胸口"),				_T("body"),				MH_SHOW_BODY,			SHOW_TYPE_UNDEFINE  },
{ _T("手（左手）"),			_T("arml"),				MH_SHOW_ARM_L,			SHOW_TYPE_UNDEFINE },

};

LPCTSTR g_type[] = {
_T("未定义"),
_T("外衣"),
_T("裤子"),
_T("套装"),
_T("围巾"),
_T("帽子"),
_T("鞋"),
_T("披风"),

_T("头发"),
_T("表情"),
_T("头像"),

_T("背景"),
_T("杂志"),
_T("相框"),
_T("字幕"),
_T("周边装饰"),
_T("配饰"),
_T("同伴"),
_T("交通工具"),
//------------------------------------------------------------------------------
// 配饰
_T("头饰"),
_T("脸饰"),
_T("项链"),
_T("耳环"),
_T("眼镜"),
_T("包包"),
_T("翅膀"),
_T("胸针"),
_T("腰饰"),
_T("其他"),
//------------------------------------------------------------------------------
// 宠物
_T("普通宠物"),
_T("特殊宠物"),
//------------------------------------------------------------------------------
// 套装
_T("精品"),
_T("人气"),
//------------------------------------------------------------------------------
// VIP
_T("VIP"),
};

LPCTSTR g_flag[]={
_T("是"),
_T("否"),
};

// 定义 m_list 的列
enum{
COL_FILE = 0,
COL_ID,
COL_LAYER,
COL_TYPE,
COL_NAME,
COL_PRICE,
COL_DESCRIPT,
COL_HOT,
COL_NEW,
COL_COMMEND,
COL_CONFLICT,
COL_ISSALING,
COL_LINK,
};

IMPLEMENT_DYNCREATE(CEditPartView, CFormView)

CEditPartView::CEditPartView()
: CFormView(CEditPartView::IDD)
, m_first( true)
, m_sex( 0)
, m_pDXTestView( NULL)
, m_changed( false)
, m_sorttype( SORT_BY_SUIT)
, m_bMouseEvent( TRUE)
{

}

CEditPartView::~CEditPartView()
{
}

void CEditPartView::DoDataExchange(CDataExchange* pDX)
{
	CFormView::DoDataExchange(pDX);
	DDX_Control( pDX, IDC_TAB1, m_tab);
	DDX_Control( pDX, IDC_LIST1, m_list);
}

BEGIN_MESSAGE_MAP(CEditPartView, CFormView)
	ON_WM_CREATE()
	ON_WM_SIZE()
	ON_NOTIFY(TCN_SELCHANGE, IDC_TAB1, &CEditPartView::OnTcnSelchangeTab1)
	ON_NOTIFY(LVN_ITEMCHANGED, IDC_LIST1, &CEditPartView::OnLvnItemchangedList1)

	ON_MESSAGE(WM_SET_ITEMS, PopulateComboList)
	ON_MESSAGE(WM_VALIDATE, OnEndLabelEditVariableCriteria)

	ON_COMMAND(ID_BUTTON_MAN, OnOptionManModel)
	ON_COMMAND(ID_BUTTON_WOMAN, OnOptionWomanModel)
	ON_UPDATE_COMMAND_UI(ID_BUTTON_MAN, OnUpdateOptionManModel)
	ON_UPDATE_COMMAND_UI(ID_BUTTON_WOMAN, OnUpdateOptionWomanModel)
	ON_COMMAND(ID_FILE_SAVE, OnSaveConfig)
	ON_UPDATE_COMMAND_UI(ID_FILE_SAVE, OnUpdateSaveConfig)
	ON_COMMAND(ID_BUTTON_SAVEPOS, OnSavePos)
	ON_UPDATE_COMMAND_UI(ID_BUTTON_SAVEPOS, OnUpdateSavePos)
	ON_COMMAND(ID_BUTTON_LAYER, OnSortByLayer)
	ON_UPDATE_COMMAND_UI(ID_BUTTON_LAYER, OnUpdateSortByLayer)
	ON_COMMAND(ID_BUTTON_SUIT, OnSortBySuit)
	ON_UPDATE_COMMAND_UI(ID_BUTTON_SUIT, OnUpdateSortBySuit)
	ON_COMMAND(ID_BUTTON_ADDPATH, OnAddPath)
	ON_UPDATE_COMMAND_UI(ID_BUTTON_ADDPATH, OnUpdateAddPath)
	ON_COMMAND(ID_BUTTON_TYPE, OnSortByType)
	ON_UPDATE_COMMAND_UI(ID_BUTTON_TYPE, OnUpdateSortByType)
	ON_COMMAND(ID_BUTTON_DELETEPART, OnDeletePart)
	ON_COMMAND(ID_BUTTON_ADDPART, OnAddPart)
	ON_UPDATE_COMMAND_UI(ID_BUTTON_ADDPART, OnUpdateAddPart)
	ON_COMMAND(ID_BUTTON_NAKED, OnOptionNaked)
	ON_NOTIFY(NM_RCLICK, IDC_TAB1, &CEditPartView::OnNMRclickTab1)

	ON_COMMAND( ID_SUIT_SELECTALL, OnSuitSelectAll)
	ON_COMMAND( ID_SUIT_UNSELECTALL, OnSuitUnSelectAll)
	ON_COMMAND( ID_SUIT_DELETE, OnSuitDelete)
	ON_COMMAND( ID_SHOWSELECTED, OnShowSelected)
	ON_COMMAND( ID_SHOWANTISELECTED, OnShowAntiSelected)
	ON_NOTIFY(NM_RCLICK, IDC_LIST1, &CEditPartView::OnNMRclickList1)

	ON_COMMAND( ID_FILE_VALIDATE, OnFileValidate)
END_MESSAGE_MAP()


// CEditPartView diagnostics

#ifdef _DEBUG
void CEditPartView::AssertValid() const
{
	CFormView::AssertValid();
}

#ifndef _WIN32_WCE
void CEditPartView::Dump(CDumpContext& dc) const
{
	CFormView::Dump(dc);
}
#endif
#endif //_DEBUG


// CEditPartView message handlers

int CEditPartView::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CFormView::OnCreate(lpCreateStruct) == -1)
		return -1;

	// TODO:  Add your specialized creation code here
	

	return 0;
}

void CEditPartView::OnInitialUpdate()
{
	CFormView::OnInitialUpdate();

	// TODO: Add your specialized code here and/or call the base class
	if( m_first)
	{
		m_first = false;

		if( m_loader.Load() < 0)
		{
			return;
		}

		m_list.SetImageList( &m_loader.GetImageList(), LVSIL_SMALL);
		m_list.SetExtendedStyle( LVS_EX_CHECKBOXES|LVS_EX_FULLROWSELECT );

		m_list.InsertColumn( COL_FILE,		L"文件名",	LVCFMT_LEFT,	180);
		m_list.InsertColumn( COL_ID,		L"id",		LVCFMT_LEFT,	80);
		m_list.InsertColumn( COL_LAYER,		L"层",		LVCFMT_LEFT,	120);
		m_list.InsertColumn( COL_TYPE,		L"类型",	LVCFMT_LEFT,	80);
		m_list.InsertColumn( COL_NAME,		L"名字",	LVCFMT_LEFT,	120);
		m_list.InsertColumn( COL_PRICE,		L"价格",	LVCFMT_LEFT,	50);
		m_list.InsertColumn( COL_DESCRIPT,	L"描述",	LVCFMT_LEFT,	100);
		m_list.InsertColumn( COL_HOT,		L"热卖",	LVCFMT_LEFT,	40);
		m_list.InsertColumn( COL_NEW,		L"新品",	LVCFMT_LEFT,	40);
		m_list.InsertColumn( COL_COMMEND,	L"推荐",	LVCFMT_LEFT,	40);
		m_list.InsertColumn( COL_CONFLICT,	L"互斥层",	LVCFMT_LEFT,	130);
		m_list.InsertColumn( COL_ISSALING,	L"上架",	LVCFMT_LEFT,	40);
		std::wostringstream wos;
		for( int i=0; i<LINK_MAX_COUNT; i++)
		{
			wos.str( L"");
			wos << L"Link" << (i+1);
			m_list.InsertColumn( COL_LINK+i, wos.str().c_str(), LVCFMT_LEFT, 50);
		}

		m_list.SetReadOnlyColumns( COL_FILE);
		m_list.SetReadOnlyColumns( COL_ID);
		m_list.SetComboColumns( COL_LAYER);
		m_list.SetComboColumns( COL_TYPE);
		m_list.SetComboColumns( COL_HOT);
		m_list.SetComboColumns( COL_NEW);
		m_list.SetComboColumns( COL_COMMEND);
		m_list.SetComboColumns( COL_ISSALING);
		m_list.SetListBoxColumns( COL_CONFLICT);
	}
	if( m_pDXTestView)
	{
		InsertItemsToTab();
		InsertItemsToList( );
		ShowDefaultParts();
	}
}

void CEditPartView::OnSize(UINT nType, int cx, int cy)
{
	CFormView::OnSize(nType, cx, cy);
	// TODO: Add your message handler code here
	UpdateControlPos();
}

void CEditPartView::OnTcnSelchangeTab1(NMHDR *pNMHDR, LRESULT *pResult)
{
	// TODO: Add your control notification handler code here
	if( SORT_BY_SUIT == m_sorttype)
	{
		ShowDefaultParts();
	}
	InsertItemsToList( );


	*pResult = 0;
}

void CEditPartView::InsertItemsToTab()
{
	m_tab.DeleteAllItems();
	if( SORT_BY_LAYER == m_sorttype)
	{
		int count = sizeof( g_layer) / sizeof( g_layer[0]);
		for( int i=0; i<count; i++)
		{
			m_tab.InsertItem( i, g_layer[i].name);
		}
	}
	else if( SORT_BY_SUIT == m_sorttype)
	{
		std::set<std::wstring>	suits;
		m_loader.GetAllSuitName( suits, m_sex);
		int index = 0;
		for( std::set<std::wstring>::iterator i=suits.begin(), e=suits.end(); i != e; ++i, ++index)
		{
			m_tab.InsertItem( index, i->c_str());
		}
	}
	else if( SORT_BY_TYPE == m_sorttype)
	{
		int count = sizeof( g_type) / sizeof( g_type[0]);
		for( int i=0; i<count; i++)
		{
			m_tab.InsertItem( i, g_type[i]);
		}
	}
}

static LPCTSTR GetStrFromLayerID( int layerID)
{
	int count = sizeof( g_layer) / sizeof( g_layer[0]);
	for( int i=0; i<count; i++)
	{
		if( g_layer[i].layer == layerID)
		{
			return g_layer[i].name;
		}
	}
	static const wchar_t c = 0;
	return &c;
}

static LPCTSTR GetStrFromTypeID( int typeID)
{
	int count = sizeof( g_type) / sizeof( g_type[0]);
	if( typeID >= -1 && typeID < count-1)
	{
		// g_type数组索引比实际 lType 值大1
		return g_type[typeID+1];
	}
	static const wchar_t c = 0;
	return &c;
}

static int GetLayerIDFromStr( LPCTSTR pszLayer)
{
	int count = sizeof( g_layer) / sizeof( g_layer[0]);
	for( int i=0; i<count; i++)
	{
		if( wcscmp( g_layer[i].name, pszLayer) == 0)
		{
			return g_layer[i].layer;
		}
	}
	return -1;
}

static int GetTypeIDFromStr( LPCTSTR pszType)
{
	int count = sizeof( g_type) / sizeof( g_type[0]);
	for( int i=0; i<count; i++)
	{
		if( wcscmp( g_type[i], pszType) == 0)
		{
			return i-1;
		}
	}
	return -1;
}

// 设置List的flag文本
void CEditPartView::SetListFlagText( int index, int col, int flag, int flagBit)
{
	int flagType = 1 << flagBit;
	if( flag & flagType)
	{
		m_list.SetItemText( index, col, g_flag[0]);
	}
	else
	{
		m_list.SetItemText( index, col, g_flag[1]);
	}
}

void CEditPartView::InsertItemsToList( )
{
	m_list.HideAllInPlaceCtrl();
	m_list.DeleteAllItems();

	int tabindex = m_tab.GetCurSel();
	TCITEM tcItem;
	wchar_t buffer[256] = {0};
	tcItem.pszText = buffer;
	tcItem.cchTextMax = 256;
	tcItem.mask = TCIF_TEXT;
	m_tab.GetItem( tabindex, &tcItem);
	if( 0 == buffer[0])
	{
		return;
	}
	
	TShowPartPtrContainer parts;
	if( SORT_BY_LAYER == m_sorttype)
	{
		int layer = g_layer[tabindex].layer;
		m_loader.GetPartsByLayer( parts, m_sex, layer);
	}
	else if( SORT_BY_SUIT == m_sorttype)
	{
		m_loader.GetPartsBySuitName( parts, m_sex, tcItem.pszText);
	}
	else if( SORT_BY_TYPE == m_sorttype)
	{
		int type = GetTypeIDFromStr( tcItem.pszText);
		m_loader.GetPartsByType( parts, m_sex, type);
	}

	m_bMouseEvent = FALSE;

	SvgImageMap showparts;
	m_pDXTestView->GetAllShowParts( showparts);
	for( TShowPartPtrContainer::iterator i=parts.begin(), e=parts.end(); i != e; ++i)
	{
		ShowPartItem& part = *(*i);
		
		int index = InsertOneItemToList( part);

		SvgImageMap::const_iterator iFind = showparts.find( part.ulLayer);
		if( iFind != showparts.end() && iFind->second->get_image_id() == part.ulID)
		{
			// 如果当前正在显示就 check 上
			m_list.SetCheck( index, TRUE);
		}
	}

	m_bMouseEvent = TRUE;

	// 选中上次的行
	//for( CTabSaveItemVector::const_iterator i=m_lastselect.begin(), e=m_lastselect.end(); i!=e; ++i)
	//{
	//	if( i->name == tcItem.pszText && i->sex == m_sex && i->sorttype == m_sorttype)
	//	{
	//		const std::set<int>& select = i->selects;
	//		for( std::set<int>::const_iterator iS=select.begin(), eS=select.end(); iS != eS; ++iS)
	//		{
	//			m_list.SetItemState( *iS, LVIS_SELECTED, LVIS_SELECTED);
	//		}
	//	}
	//}
}

// 插入一个部件信息到list
int CEditPartView::InsertOneItemToList( ShowPartItem& part)
{
	int index = m_list.GetItemCount();

	int image = part.image;
	if( image < 0)
	{
		image = m_loader.GetImageIndex( part.strTga.c_str());
		if( image < 0)
		{
			image = 0;
		}
		part.image = image;
	}
	index = m_list.InsertItem( index, part.strSvg.c_str(), image);

	std::wostringstream oss;
	oss << part.ulID;
	m_list.SetItemText( index, COL_ID, oss.str().c_str());
	m_list.SetItemText( index, COL_LAYER, GetStrFromLayerID( part.ulLayer));
	m_list.SetItemText( index, COL_TYPE, GetStrFromTypeID( part.lType));
	m_list.SetItemText( index, COL_NAME, part.name.c_str());
	oss.str( L"");
	oss << part.ulPrice;
	m_list.SetItemText( index, COL_PRICE, oss.str().c_str());
	m_list.SetItemText( index, COL_DESCRIPT, part.descript.c_str());
	SetListFlagText( index, COL_HOT, part.ulFlag, 0);
	SetListFlagText( index, COL_NEW, part.ulFlag, 1);
	SetListFlagText( index, COL_COMMEND, part.ulFlag, 2);
	SetListFlagText( index, COL_ISSALING, part.bIsSaling, 0);
	// conflict
	oss.str( L"");
	for( int i=0; i<MH_SHOW_LAYER_MAX; i++)
	{
		if( part.conflict & (1i64 << i))
		{
			if( !oss.str().empty())
			{
				oss << L";";
			}
			oss << GetStrFromLayerID( i);
		}
	}
	m_list.SetItemText( index, COL_CONFLICT, oss.str().c_str());
	// link
	for( int i=0; i<LINK_MAX_COUNT; i++)
	{
		oss.str( L"");
		oss << part.ulLinkID[i];
		m_list.SetItemText( index, COL_LINK+i, oss.str().c_str());
	}

	m_list.SetItemData( index, part.ulID);

	return index;
}

// 显示裸模
void CEditPartView::ShowDefaultParts()
{
	TShowPartPtrContainer tspc;
	m_loader.GetDefaultParts( tspc, m_sex);
	if( m_pDXTestView)
	{
		m_pDXTestView->ClearAllParts();
		m_pDXTestView->ShowPart( tspc);
	}
}

void CEditPartView::OnLvnItemchangedList1(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMLISTVIEW pNMLV = reinterpret_cast<LPNMLISTVIEW>(pNMHDR);
	// TODO: Add your control notification handler code here

	int item = pNMLV->iItem;
	ShowPartItem* part = m_loader.GetItemFromID( (unsigned long)m_list.GetItemData( item));
	if( part)
	{
		TShowPartPtrContainer tspc;
		if((pNMLV->uOldState & INDEXTOSTATEIMAGEMASK(1))		/* old state : unchecked */ 
			&& (pNMLV->uNewState & INDEXTOSTATEIMAGEMASK(2)))	/* new state : checked */ 
		{ 
			//TRACE("Item %d is checked\n", pNMLV->iItem);
			//tspc.push_back( part);
			ShowLinkedParts( *part, tspc, TRUE);
			if( m_bMouseEvent && m_pDXTestView)
			{
				m_pDXTestView->ShowPart( tspc);
				if( m_list.GetItemState( item, LVIS_SELECTED) & LVIS_SELECTED)
				{
					m_pDXTestView->SelectPart( tspc);
				}
			}

			//UpdateLastSelect( true, item);

		} 
		else if((pNMLV->uOldState & INDEXTOSTATEIMAGEMASK(2))	/* old state : checked */ 
			&& (pNMLV->uNewState & INDEXTOSTATEIMAGEMASK(1))) 	/* new state : unchecked */ 
		{ 
			//TRACE("Item %d is unchecked\n", pNMLV->iItem);
			//tspc.push_back( part);
			ShowLinkedParts( *part, tspc, FALSE);
			if( m_bMouseEvent && m_pDXTestView)
			{
				m_pDXTestView->HidePart( tspc);
			}

			// 清空上一次选中索引
			//UpdateLastSelect( false, item);
		} 
		else 
		{
			if( pNMLV->uNewState & LVIS_SELECTED)
			{
				tspc.push_back( part);
				if( m_bMouseEvent && m_pDXTestView)
				{
					m_pDXTestView->SelectPart( tspc);
				}

				// 保存到上一次选择索引
				//UpdateLastSelect( true, item);
			}
			//else if( SORT_BY_LAYER == m_sorttype )
			//{
			//	// 如果是按层分类，同时只能check一行
			//	m_list.SetCheck( item, FALSE);
			//}
		}
	}
	*pResult = 0;
}

// 男女装模型切换
void CEditPartView::OnUpdateOptionManModel(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck( m_sex == 0 );
}

// 男女装模型切换
void CEditPartView::OnUpdateOptionWomanModel(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck( m_sex == 1 );
}

// 男女装模型切换
void CEditPartView::OnOptionManModel()
{
	if( m_sex != 0)
	{
		m_sex = 0;
		ShowDefaultParts();
		if( SORT_BY_SUIT == m_sorttype)
		{
			InsertItemsToTab();
			UpdateControlPos();
		}
		InsertItemsToList( );
	}
}

// 男女装模型切换
void CEditPartView::OnOptionWomanModel()
{
	if( m_sex != 1)
	{
		m_sex = 1;
		ShowDefaultParts();
		if( SORT_BY_SUIT == m_sorttype)
		{
			InsertItemsToTab();
			UpdateControlPos();
		}
		InsertItemsToList( );
	}
}

LRESULT CEditPartView::PopulateComboList(WPARAM wParam, LPARAM lParam)
{
	CStringList* pComboList = reinterpret_cast<CStringList*>(lParam);
	if (pComboList)
	{
		if( COL_LAYER == wParam || COL_CONFLICT == wParam)
		{
			// 填充 layer
			int count = sizeof( g_layer) / sizeof( g_layer[0]);
			for( int i=0; i<count; i++)
			{
				pComboList->AddTail( g_layer[i].name);
			}
		}
		else if ( COL_TYPE == wParam )
		{
			// 填充 type
			int count = sizeof( g_type) / sizeof( g_type[0]);
			for( int i=0; i<count; i++)
			{
				pComboList->AddTail( g_type[i]);
			}
		}
		else if( COL_HOT == wParam || COL_NEW == wParam || COL_COMMEND == wParam 
			|| COL_ISSALING == wParam)
		{
			pComboList->AddTail( g_flag[0]);
			pComboList->AddTail( g_flag[1]);
		}
	}
	return 0;
}

void SetFlagFromText( unsigned long& flag, int col, const wchar_t* text, int flagBit)
{
	bool res = (wcscmp( text, g_flag[0]) == 0);
	int flagType = 1 << flagBit;
	if( res)
	{
		flag |= flagType;
	}
	else
	{
		flag &= ~flagType;
	}
}

__int64 SetConflictFromText( const wchar_t* text)
{
	__int64 conflict = 0;
	std::wstringstream ss( text);
	while( true)
	{
		std::wstring token;
		std::getline(ss, token, L';');
		if (ss.fail())
			break;
		int layer = GetLayerIDFromStr( token.c_str());
		if( layer >= 0)
		{
			conflict |= (1i64 << layer);
		}
	}
	return conflict;
}

LRESULT CEditPartView::OnEndLabelEditVariableCriteria(WPARAM wParam, LPARAM lParam)
{
	LV_DISPINFO* pDispInfo = (LV_DISPINFO*)lParam;
	if( NULL == pDispInfo->item.pszText)
	{
		return 0;
	}
	ShowPartItem* part = m_loader.GetItemFromID( (unsigned long)m_list.GetItemData( pDispInfo->item.iItem));
	if( NULL == part)
	{
		return 0;
	}
	int col = pDispInfo->item.iSubItem;
	if( COL_LAYER == col)
	{
		int oldLayer = part->ulLayer;
		int newLayer = GetLayerIDFromStr( pDispInfo->item.pszText);
		if( oldLayer == newLayer)
		{
			return 0;
		}
		part->ulLayer = newLayer;
		if( m_pDXTestView)
		{
			TShowPartPtrContainer tspc;
			tspc.push_back( part);
			m_pDXTestView->ChangeLayer( tspc, oldLayer);
		}
	}
	else if( COL_TYPE == col)
	{
		part->lType = GetTypeIDFromStr( pDispInfo->item.pszText);
	}
	else if( COL_NAME == col)
	{
		part->name = pDispInfo->item.pszText;
	}
	else if( COL_DESCRIPT == col)
	{
		part->descript = pDispInfo->item.pszText;
	}
	else if( COL_PRICE == col)
	{
		part->ulPrice = _wtoi( pDispInfo->item.pszText);
	}
	else if( COL_HOT == col || COL_NEW == col || COL_COMMEND == col)
	{
		SetFlagFromText( part->ulFlag, col, pDispInfo->item.pszText, col - COL_HOT);
	}
	else if( COL_ISSALING == col)
	{
		SetFlagFromText( part->bIsSaling, col, pDispInfo->item.pszText, 0);
	}
	else if( col >= COL_LINK && col < COL_LINK+LINK_MAX_COUNT )
	{
		part->ulLinkID[col-COL_LINK] = _wtoi( pDispInfo->item.pszText);
	}
	else if( COL_CONFLICT == col)
	{
		 part->conflict = SetConflictFromText( pDispInfo->item.pszText);
		 if( m_list.GetCheck( pDispInfo->item.iItem))
		 {
			if( m_pDXTestView)
			{
				// 设置成功后清除互斥的部件
				m_pDXTestView->ClearConflictParts( part->conflict, true );
			}
		 }
	}
	else
	{
		return 0;
	}
	m_changed = true;
	return 0;
}

void CEditPartView::OnSaveConfig()
{
	CFileDialog OpenDlg( FALSE, NULL, NULL, CREATE_ALWAYS, 
		_T("Xml Files(*.xml)|*.xml|")
		_T("All Files(*.*)|*.*||"),
		this, 0 );
	if( IDCANCEL == OpenDlg.DoModal())
	{
		return;
	}
	if( !m_loader.Save( OpenDlg.GetPathName()))
	{
		AfxMessageBox( L"保存失败！");
		return;
	}
	m_changed = false;
}

void CEditPartView::OnSavePos()
{
	if( m_pDXTestView)
	{
		SvgImageMap parts;
		m_pDXTestView->GetAllShowParts( parts);
		for( SvgImageMap::const_iterator i=parts.begin(), e=parts.end(); i != e; ++i)
		{
			svg::svg_image_ptr image = i->second;
			ShowPartItem* part = m_loader.GetItemFromID( image->get_image_id());
			if( part)
			{
				image->get_trans_affine( part->transform);
				image->bounding_rect( part->boundingbox[0], part->boundingbox[1],
					part->boundingbox[2], part->boundingbox[3]);
				m_changed = true;
			}
		}
	}
}

void CEditPartView::OnUpdateSaveConfig(CCmdUI *pCmdUI)
{
	pCmdUI->Enable( m_changed);
}

void CEditPartView::OnUpdateSavePos(CCmdUI *pCmdUI)
{
	//svg::svg_image_ptr select = m_pDXTestView->GetSelectPart();
	//pCmdUI->Enable( select != NULL);
}

// 根据部件id切换当前tab
void CEditPartView::ChangeTabByPartID( int partID)
{
	ShowPartItem* part = m_loader.GetItemFromID( partID);
	if( NULL == part)
	{
		return;
	}
	LPCTSTR pszTab = NULL;
	if( SORT_BY_LAYER == m_sorttype)
	{
		pszTab = GetStrFromLayerID( part->ulLayer);
	}
	else if( SORT_BY_SUIT == m_sorttype )
	{
		pszTab = part->strSvg.c_str();
	}
	else if( SORT_BY_TYPE == m_sorttype)
	{
		pszTab = GetStrFromTypeID( part->lType);
	}

	if( pszTab == NULL)
	{
		return;
	}
	int count = m_tab.GetItemCount();
	TCITEM tcItem;
	wchar_t buffer[256] = {0};
	tcItem.pszText = buffer;
	tcItem.cchTextMax = 256;
	tcItem.mask = TCIF_TEXT;
	for( int i=0; i<count; i++)
	{
		m_tab.GetItem( i, &tcItem);
		bool bMatch = false;
		if( SORT_BY_SUIT == m_sorttype )
		{
			bMatch = ( wcsncmp( pszTab, tcItem.pszText, wcslen( tcItem.pszText)) == 0);
		}
		else
		{
			// 不是按套分类的情况要全匹配
			bMatch = ( wcscmp( pszTab, tcItem.pszText) == 0);
		}
		if( bMatch )
		{
			if( i != m_tab.GetCurSel())
			{
				// 与当前选中相等就不切换
				m_tab.SetCurSel( i);
				InsertItemsToList( );
			}
			SelectItemByPartID( partID);
			break;
		}
	}
}

// 根据partID选中行
void CEditPartView::SelectItemByPartID( unsigned long selPartID)
{
	m_bMouseEvent = FALSE;

	int count = m_list.GetItemCount();
	for( int i=0; i<count; i++)
	{
		if( m_list.GetItemData( i) == selPartID)
		{
			m_list.SetCheck( i, TRUE);
			m_list.SetItemState( i, LVIS_SELECTED, LVIS_SELECTED);
		}
		else
		{
			m_list.SetItemState( i, 0, LVIS_SELECTED);
		}
	}

	m_bMouseEvent = TRUE;
}

// 更新上一次选择索引信息
void CEditPartView::UpdateLastSelect( bool bSave, int index)
{
	// 保存到上一次选择索引
	int tabindex = m_tab.GetCurSel();
	TCITEM tcItem;
	wchar_t buffer[256] = {0};
	tcItem.pszText = buffer;
	tcItem.cchTextMax = 256;
	tcItem.mask = TCIF_TEXT;
	m_tab.GetItem( tabindex, &tcItem);
	if( 0 == buffer[0])
	{
		return;
	}

	bool find = false;
	for( CTabSaveItemVector::iterator i=m_lastselect.begin(), e=m_lastselect.end(); i != e; ++i)
	{
		if( i->name == buffer && i->sex == m_sex && i->sorttype == m_sorttype)
		{
			find = true;
			std::set<int>& select = i->selects;
			if( bSave)
			{
				select.insert( index);
			}
			else
			{
				select.erase( index);
			}
		}
	}
	if( !find && bSave)
	{
		m_lastselect.push_back( CTabSaveItem());
		CTabSaveItem& item = m_lastselect.back();
		item.name = buffer;
		item.sex = m_sex;
		item.sorttype = m_sorttype;
		item.selects.insert( index);
	}
}

void CEditPartView::OnSortByLayer()
{
	if( SORT_BY_LAYER != m_sorttype )
	{
		m_sorttype = SORT_BY_LAYER;
		InsertItemsToTab();
		InsertItemsToList( );
		UpdateControlPos();
	}
}

void CEditPartView::OnUpdateSortByLayer(CCmdUI* pCmdUI)
{
	pCmdUI->SetCheck( SORT_BY_LAYER == m_sorttype );
}

void CEditPartView::OnSortBySuit()
{
	if( SORT_BY_SUIT != m_sorttype )
	{
		m_sorttype = SORT_BY_SUIT;
		InsertItemsToTab();
		InsertItemsToList( );
		UpdateControlPos();
	}
}

void CEditPartView::OnUpdateSortBySuit(CCmdUI* pCmdUI)
{
	pCmdUI->SetCheck( SORT_BY_SUIT == m_sorttype );
}

void CEditPartView::OnSortByType()
{
	if( SORT_BY_TYPE != m_sorttype )
	{
		m_sorttype = SORT_BY_TYPE;
		InsertItemsToTab();
		InsertItemsToList( );
		UpdateControlPos();
	}
}

void CEditPartView::OnUpdateSortByType(CCmdUI* pCmdUI)
{
	pCmdUI->SetCheck( SORT_BY_TYPE == m_sorttype );
}

// 更新控件位置
void CEditPartView::UpdateControlPos()
{
	if( ::IsWindow( m_tab.GetSafeHwnd()) && ::IsWindow( m_list.GetSafeHwnd()))
	{
		CRect rc;
		GetClientRect( &rc);

		m_tab.MoveWindow( 0, 0, rc.Width(), rc.Height());
		CRect rcTab;
		m_tab.GetItemRect( 0, &rcTab);
		int bottom = rcTab.Height() * m_tab.GetRowCount();
		m_tab.MoveWindow( 0, 0, rc.Width(), bottom);
		m_list.MoveWindow( 0, bottom+2, rc.Width(), rc.Height()-bottom);
	}
}

static bool IsDirectoryExisting( LPCTSTR dir)
{
	DWORD	dwAttribute = GetFileAttributesW( dir);
	if( INVALID_FILE_ATTRIBUTES == dwAttribute)
		return false;
	return FILE_ATTRIBUTE_DIRECTORY == ( dwAttribute & FILE_ATTRIBUTE_DIRECTORY);
}

// 得到最大套件名称
void CEditPartView::GetMaxSuitName( std::wstring& suitname, int sex)
{
	std::wstring fullpath;
	CShowPartLoader::GetSvgFullPath( fullpath);

	int index = 1;
	CString str;
	while( true)
	{
		str.Format( L"%d/%02d/", sex, index++);
		suitname = str.GetString();
		str.Insert( 0, fullpath.c_str());
		if( !IsDirectoryExisting( str))
		{
			break;
		}
	}
}

// 删除目录，将目录里文件及子目录也删除
static bool DeletePath( std::wstring path)
{
	std::replace( path.begin(), path.end(), L'/', L'\\');
	std::wstring::size_type pos = path.find_last_not_of( L'\\');
	if( pos != std::wstring::npos)
	{
		path.erase( pos+1);
	}
	path.resize( path.size() + 1, 0 );
	SHFILEOPSTRUCT FileOp = {0};
	FileOp.pFrom = path.c_str();
	FileOp.wFunc = FO_DELETE;
	FileOp.fFlags = FOF_NOCONFIRMATION | FOF_NOERRORUI | FOF_SILENT;
	int iRet = ::SHFileOperation( &FileOp);
	if ( iRet != ERROR_SUCCESS )
	{
		return false;
	}
	return true;
}

// 添加目录到套件
bool CEditPartView::AddPathToSuit( const std::wstring& srcpath, const std::wstring& suitname, std::wstring& error)
{
	std::wstring srcpathname( srcpath);
	srcpathname += L"\\";
	std::wstring temp( srcpathname);
	temp += L"*.svg";

	// 创建svg目标目录
	std::wstring despath;
	CShowPartLoader::GetSvgFullPath( despath);
	despath += suitname;
	if( !IsDirectoryExisting( despath.c_str()))
	{
		if( !::CreateDirectory( despath.c_str(), NULL))
		{
			std::wostringstream wos;
			wos << despath.c_str() << L"目录创建失败！";
			wos << L"错误号(" << ::GetLastError() << L")";
			error = wos.str();
			return false;
		}
	}
	// 创建tga目标目录
	std::wstring tgadespath;
	CShowPartLoader::GetTgaFullPath( tgadespath);
	tgadespath += suitname;
	if( !IsDirectoryExisting( tgadespath.c_str()))
	{
		if( !::CreateDirectory( tgadespath.c_str(), NULL))
		{
			std::wostringstream wos;
			wos << tgadespath.c_str() << L"目录创建失败！";
			wos << L"错误号(" << ::GetLastError() << L")";
			error = wos.str();
			return false;
		}
	}

	WIN32_FIND_DATAW wfd;
	HANDLE	hFind = FindFirstFile( temp.c_str(), &wfd);
	if( INVALID_HANDLE_VALUE == hFind)
	{
		error = srcpath;
		error += L" 目录下没有svg文件！";
		return false;
	}

	bool res = true;
	do
	{
		if( L'.' == wfd.cFileName[0])
			continue;
		if( (FILE_ATTRIBUTE_DIRECTORY & wfd.dwFileAttributes) == 0)
		{
			// 是svg文件
			if( !AddOnePart( srcpathname, wfd.cFileName, suitname, error))
			{
				res = false;
				break;
			}
		}
	}while( FindNextFile( hFind, &wfd));
	FindClose( hFind);

	if( !res)
	{
		// 失败后将添加的目录删除
		DeletePath( despath);
	}
	return res;
}

void GetLayerAndTypeByFileName( const std::wstring& svgfile, int& layer, int& type)
{
	int count = sizeof( g_layer) / sizeof( g_layer[0]);
	for( int i=0; i<count; i++)
	{
		if( 0 == g_layer[i].filename[0])
		{
			continue;
		}
		std::wstring temp = g_layer[i].filename;
		temp += L".svg";
		if( svgfile.find( temp) != std::wstring::npos)
		{
			layer = g_layer[i].layer;
			type = g_layer[i].type;
			return;
		}
	}
	layer = MH_SHOW_BACK_GROUND;
	type = SHOW_TYPE_UNDEFINE;
}

// 添加一个部件
bool CEditPartView::AddOnePart( const std::wstring& srcpath, const std::wstring& svg, 
				const std::wstring& suitname, std::wstring& error)
{
	std::wstring svgfile( svg);
	std::transform( svgfile.begin(), svgfile.end(), svgfile.begin(), ::towlower);

	std::wstring srcfile( srcpath);
	srcfile += svgfile;
	std::wstring desfile;
	CShowPartLoader::GetSvgFullPath( desfile);
	desfile += suitname;
	desfile += svgfile;
	if( ::CopyFile( srcfile.c_str(), desfile.c_str(), TRUE))
	{
		int layer, type;
		GetLayerAndTypeByFileName( svgfile, layer, type);
		m_loader.AddPart( suitname + svgfile, m_sex, layer, type);
	}
	else
	{
		std::wostringstream wos;
		wos << L"文件拷贝失败：\nfrom " << srcfile.c_str() 
			<< L"\n to " << desfile.c_str()
			<< L"\n错误号(" << ::GetLastError() << L")";
		error = wos.str();
		return false;
	}
	// 拷贝tga文件
	std::wstring temp( svgfile);
	temp.replace( temp.size()-3, 3, L"tga");
	srcfile = srcpath;
	srcfile += temp;
	CShowPartLoader::GetTgaFullPath( desfile);
	desfile += suitname;
	desfile += temp;
	if( !::CopyFile( srcfile.c_str(), desfile.c_str(), TRUE))
	{
		// tga文件拷贝失败要处理么？
	}
	return true;
}

void CEditPartView::OnAddPath()
{
	if( SORT_BY_SUIT != m_sorttype)
	{
		AfxMessageBox( L"只能在按套分类的情况下才能添加");
		return;
	}

	CBrowsePath browser( m_hWnd);
	browser.SetIniPath( m_selectpath);
	if( !browser.DoModal())
	{
		return;
	}
	m_selectpath = browser.GetSelectPath();

	std::wstring suitname;
	GetMaxSuitName( suitname, m_sex);
	std::wstring error;
	if( !AddPathToSuit( m_selectpath, suitname, error))
	{
		CString str;
		str.Format( L"添加失败\n%s", error.c_str());
		AfxMessageBox( str);
		return;
	}
	int index = m_tab.InsertItem( m_tab.GetItemCount(), suitname.c_str());
	m_tab.SetCurSel( index);
	InsertItemsToList( );
	UpdateControlPos();

	m_changed = true;
}

void CEditPartView::OnUpdateAddPath(CCmdUI* pCmdUI)
{
	pCmdUI->Enable( SORT_BY_SUIT == m_sorttype);
}

void CEditPartView::OnDeletePart()
{
	POSITION pos = m_list.GetFirstSelectedItemPosition();
	if (pos == NULL)
	{
		AfxMessageBox( L"请先选择要删除的行！");
		return;
	}
	if( IDNO == AfxMessageBox( L"确认删除这些部件？", MB_YESNO|MB_DEFBUTTON2))
	{
		return;
	}
	for (int i=m_list.GetItemCount(); i>=0; i--)
	{
		if( m_list.GetItemState(i, LVIS_SELECTED) == LVIS_SELECTED)
		{
			ShowPartItem* part = m_loader.GetItemFromID( (unsigned long)m_list.GetItemData( i));
			if( part )
			{
				unsigned long oldID = part->ulID;
				unsigned long oldLayer = part->ulLayer;
				if( m_loader.DeletePart( oldID))
				{
					if( m_pDXTestView)
					{
						m_pDXTestView->RemovePart( oldID, oldLayer);
					}
					m_list.DeleteItem(i);
					m_changed = true;
				}
			}
		}
	}
}

void CEditPartView::OnAddPart()
{
	if( SORT_BY_SUIT != m_sorttype)
	{
		AfxMessageBox( L"只能在按套分类的情况下才能添加");
		return;
	}
	int tabindex = m_tab.GetCurSel();
	TCITEM tcItem;
	wchar_t buffer[256] = {0};
	tcItem.pszText = buffer;
	tcItem.cchTextMax = 256;
	tcItem.mask = TCIF_TEXT;
	m_tab.GetItem( tabindex, &tcItem);
	if( 0 == buffer[0])
	{
		AfxMessageBox( L"得不到当前套所在的目录");
		return;
	}

	CFileDialog OpenDlg( TRUE, NULL, NULL, OFN_HIDEREADONLY, 
		_T("Svg Files(*.svg)|*.svg|"),
		this, 0 );
	if( IDCANCEL == OpenDlg.DoModal())
	{
		return;
	}
	std::wstring srcpath = OpenDlg.GetPathName().GetString();
	std::wstring::size_type pos = srcpath.find_last_of( L"\\");
	if( pos == std::wstring::npos)
	{
		AfxMessageBox( L"选择的文件名有问题");
		return;
	}
	std::wstring svgfile = srcpath.substr( pos+1);
	srcpath.erase( pos+1);
	
	std::wstring error;
	if( !AddOnePart( srcpath, svgfile, buffer, error))
	{
		CString str;
		str.Format( L"添加失败\n%s", error.c_str());
		AfxMessageBox( str);
		return;
	}
	ShowPartItem* part = m_loader.GetItemFromID( m_loader.GetMaxID());
	if( part)
	{
		int index = InsertOneItemToList( *part);
		m_list.SetItemState( index, LVIS_SELECTED, LVIS_SELECTED);
		m_changed = true;
	}
}

void CEditPartView::OnUpdateAddPart(CCmdUI* pCmdUI)
{
	pCmdUI->Enable( SORT_BY_SUIT == m_sorttype);
}

void CEditPartView::OnOptionNaked()
{
	SelectAllParts( FALSE);
	ShowDefaultParts();
}

void CEditPartView::OnNMRclickTab1(NMHDR *pNMHDR, LRESULT *pResult)
{
	// TODO: Add your control notification handler code here
	
	CPoint point;
	::GetCursorPos( &point);

	TCHITTESTINFO info;
	info.pt = point;
	m_tab.ScreenToClient( &info.pt);
	info.flags = TCHT_ONITEM;
	int hitIndex = m_tab.HitTest( &info);
	if( hitIndex >= 0)
	{
		if( hitIndex != m_tab.GetCurSel())
		{
			m_tab.SetCurSel( hitIndex);
			InsertItemsToList();
		}
		CMenu popupMenu;
		if( !popupMenu.LoadMenu( IDR_SUIT_EDIT ) )
		{
			return;
		}

		CMenu *pSubMenu = popupMenu.GetSubMenu(0);
		if ( pSubMenu != NULL )
		{
			if( SORT_BY_SUIT != m_sorttype)
			{
				pSubMenu->EnableMenuItem( ID_SUIT_SELECTALL, MF_BYCOMMAND|MF_GRAYED );
				pSubMenu->EnableMenuItem( ID_SUIT_DELETE, MF_BYCOMMAND|MF_GRAYED );
			}
			pSubMenu->TrackPopupMenu( 0, point.x, point.y, this );
		}
	}
	*pResult = 0;
}

void CEditPartView::OnSuitSelectAll()
{
	SelectAllParts( TRUE );

	ShowAllParts( TRUE);
}

void CEditPartView::OnSuitUnSelectAll()
{
	SelectAllParts( FALSE );

	if( SORT_BY_SUIT == m_sorttype)
	{
		ShowDefaultParts();
	}
	else
	{
		ShowAllParts( FALSE);
	}
}

void CEditPartView::OnSuitDelete()
{
	if( SORT_BY_SUIT != m_sorttype)
	{
		AfxMessageBox( L"只能在按套分类的情况下才能添加");
		return;
	}
	int tabindex = m_tab.GetCurSel();
	TCITEM tcItem;
	wchar_t buffer[256] = {0};
	tcItem.pszText = buffer;
	tcItem.cchTextMax = 256;
	tcItem.mask = TCIF_TEXT;
	m_tab.GetItem( tabindex, &tcItem);
	if( 0 == buffer[0])
	{
		AfxMessageBox( L"得不到当前套所在的目录");
		return;
	}
	CString str;
	str.Format( L"确认删除 \"%s\" 套及下面所有部件？", buffer);
	if( IDNO == AfxMessageBox( str, MB_YESNO|MB_DEFBUTTON2))
	{
		return;
	}
	// 暂时实现为不删除文件，避免误操作
	//std::wstring desfile;
	//CShowPartLoader::GetSvgFullPath( desfile);
	//desfile += buffer;
	//if( !DeletePath( desfile))
	//{
	//	str.Format( L"删除 %s 目录失败，错误号(%d)", desfile.c_str(), ::GetLastError());
	//	AfxMessageBox( str);
	//	return;
	//}
	//CShowPartLoader::GetTgaFullPath( desfile);
	//desfile += buffer;
	//if( !DeletePath( desfile ))
	//{
	//	str.Format( L"删除 %s 目录失败，错误号(%d)", desfile.c_str(), ::GetLastError());
	//	AfxMessageBox( str);
	//	return;
	//}

	int count = m_list.GetItemCount();
	for( int i=0; i<count; i++)
	{
		m_loader.DeletePart( (unsigned long)m_list.GetItemData( i));
	}
	m_tab.DeleteItem( tabindex);

	tabindex--;
	if( tabindex < 0)
	{
		tabindex = 0;
	}
	m_tab.SetCurSel( tabindex );
	InsertItemsToList();

	ShowDefaultParts();

	m_changed = true;
}

// 选择或不选所有
void CEditPartView::SelectAllParts( BOOL bSel)
{
	m_bMouseEvent = FALSE;

	int count = m_list.GetItemCount();
	for( int i=0; i<count; i++)
	{
		m_list.SetCheck( i, bSel);
	}

	m_bMouseEvent = TRUE;
}

// 显示或不显示所有
void CEditPartView::ShowAllParts( BOOL bShow)
{
	TShowPartPtrContainer tspc;
	int count = m_list.GetItemCount();
	for( int i=0; i<count; i++)
	{
		ShowPartItem* part = m_loader.GetItemFromID( (unsigned long)m_list.GetItemData( i));
		if( part)
		{
			tspc.push_back( part);
		}
	}
	if( m_pDXTestView)
	{
		if( bShow)
		{
			m_pDXTestView->ShowPart( tspc);
		}
		else
		{
			m_pDXTestView->HidePart( tspc);
		}
	}
}

void CEditPartView::OnNMRclickList1(NMHDR *pNMHDR, LRESULT *pResult)
{
	// TODO: Add your control notification handler code here

	CMenu popupMenu;
	if( !popupMenu.LoadMenu( IDR_PART_EDIT ) )
	{
		return;
	}

	CMenu *pSubMenu = popupMenu.GetSubMenu( 0);
	if ( pSubMenu != NULL )
	{
		CPoint point;
		::GetCursorPos( &point);

		//CPoint pt( point);
		//m_list.ScreenToClient( &pt);
		//UINT flag = 0;
		//int nItem = m_list.HitTest( pt, &flag);
		//if( nItem < 0)
		//{
		//	pSubMenu->EnableMenuItem( ID_BUTTON_DELETEPART, MF_BYCOMMAND|MF_GRAYED );
		//}
		if( SORT_BY_SUIT != m_sorttype)
		{
			pSubMenu->EnableMenuItem( ID_BUTTON_ADDPART, MF_BYCOMMAND|MF_GRAYED );
			pSubMenu->EnableMenuItem( ID_BUTTON_DELETEPART, MF_BYCOMMAND|MF_GRAYED );
		}

		pSubMenu->TrackPopupMenu( 0, point.x, point.y, this );
	}

	*pResult = 0;
}

// 显示选中
void CEditPartView::OnShowSelected()
{
	m_bMouseEvent = FALSE;

	TShowPartPtrContainer tspcShow;
	TShowPartPtrContainer tspcHide;
	int count = m_list.GetItemCount();
	for( int i=0; i<count; i++)
	{
		ShowPartItem* part = m_loader.GetItemFromID( (unsigned long)m_list.GetItemData( i));
		if( NULL == part)
		{
			continue;
		}
		if( m_list.GetItemState( i, LVIS_SELECTED) & LVIS_SELECTED)
		{
			if( !m_list.GetCheck( i))
			{
				m_list.SetCheck( i, TRUE);
				tspcShow.push_back( part);
			}
		}
		else
		{
			if( m_list.GetCheck( i))
			{
				m_list.SetCheck( i, FALSE);
				tspcHide.push_back( part);
			}
		}
	}

	m_bMouseEvent = TRUE;

	if( m_pDXTestView)
	{
		m_pDXTestView->HidePart( tspcHide);
		m_pDXTestView->ShowPart( tspcShow);
	}
}

// 反选
void CEditPartView::OnShowAntiSelected()
{
	m_bMouseEvent = FALSE;

	TShowPartPtrContainer tspcShow;
	TShowPartPtrContainer tspcHide;
	int count = m_list.GetItemCount();
	for( int i=0; i<count; i++)
	{
		ShowPartItem* part = m_loader.GetItemFromID( (unsigned long)m_list.GetItemData( i));
		if( NULL == part)
		{
			continue;
		}
		if( m_list.GetItemState( i, LVIS_SELECTED) & LVIS_SELECTED)
		{
			if( m_list.GetCheck( i))
			{
				m_list.SetCheck( i, FALSE);
				tspcHide.push_back( part);
			}
		}
		else
		{
			if( !m_list.GetCheck( i))
			{
				m_list.SetCheck( i, TRUE);
				tspcShow.push_back( part);
			}
		}
	}

	m_bMouseEvent = TRUE;

	if( m_pDXTestView)
	{
		m_pDXTestView->HidePart( tspcHide);
		m_pDXTestView->ShowPart( tspcShow);
	}
}

// 显示 linked 的部件
void CEditPartView::ShowLinkedParts( ShowPartItem& part, TShowPartPtrContainer& tspc, BOOL bCheck)
{
	tspc.push_back( &part);

	int count = m_list.GetItemCount();
	for( int i=0; i<count; i++)
	{
		if( m_list.GetItemData( i) == part.ulID)
		{
			m_list.SetCheck( i, bCheck);
		}
	}

	for( int i=0; i<LINK_MAX_COUNT; i++)
	{
		if( part.ulLinkID[i] == 0)
		{
			// linkID为0不处理
			continue;
		}
		ShowPartItem* temp = m_loader.GetItemFromID( part.ulLinkID[i]);
		if( temp)
		{
			ShowLinkedParts( *temp, tspc, bCheck);
		}
	}
}

void CEditPartView::OnFileValidate()
{
	if( ::IsWindow( m_dlgValidate.GetSafeHwnd()))
	{
		if( m_dlgValidate.IsWindowVisible())
		{
			m_dlgValidate.ShowWindow( SW_HIDE);
		}
		else
		{
			m_dlgValidate.ShowWindow( SW_SHOW);
		}
	}
	else
	{
		m_dlgValidate.SetShowPartLoaderPtr( &m_loader);
		m_dlgValidate.Create( IDD_DIALOG_VALIDATE, this);
		m_dlgValidate.ShowWindow( SW_SHOW);
	}
}