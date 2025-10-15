// SelectCategoryDlg.cpp : implementation file
//

#include "stdafx.h"
#include "ShopEditor.h"
#include "SelectCategoryDlg.h"
#include "RightView.h"

// CSelectCategoryDlg dialog

IMPLEMENT_DYNAMIC(CSelectCategoryDlg, CDialog)

CSelectCategoryDlg::CSelectCategoryDlg( void* scp, CWnd* pParent )
	: CDialog(CSelectCategoryDlg::IDD, pParent)
	, m_manager( NULL )
	, m_selBcp( NULL )
	, m_selScp( NULL )
	, curScp( scp )
{
}

CSelectCategoryDlg::~CSelectCategoryDlg()
{
}

void CSelectCategoryDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_TREE1, m_tcCategory);
}


BEGIN_MESSAGE_MAP(CSelectCategoryDlg, CDialog)
	ON_NOTIFY(TVN_SELCHANGED, IDC_TREE1, &CSelectCategoryDlg::OnTvnSelchangedTree1)
	ON_NOTIFY(NM_DBLCLK, IDC_TREE1, &CSelectCategoryDlg::OnNMDblclkTree1)
END_MESSAGE_MAP()


// CSelectCategoryDlg message handlers

BOOL CSelectCategoryDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_tcCategory.ModifyStyle( 0, TVS_HASBUTTONS|TVS_HASLINES|TVS_LINESATROOT|TVS_SHOWSELALWAYS);

	m_imageList.Create( 16, 16, ILC_COLOR32 | ILC_MASK, 0, 4);
	SHFILEINFO sfi;
	SHGetFileInfo(L"", FILE_ATTRIBUTE_DIRECTORY, &sfi, sizeof(sfi), SHGFI_ICON|SHGFI_SMALLICON);
	if (sfi.hIcon)
	{
		// �ļ���ͼ��
		m_imageList.Add( sfi.hIcon);
	}
	SHGetFileInfo(L"", FILE_ATTRIBUTE_DIRECTORY, &sfi, sizeof(sfi),	SHGFI_ICON|SHGFI_SMALLICON|SHGFI_OPENICON);
	if (sfi.hIcon)
	{
		// �ļ��д�ͼ��
		m_imageList.Add( sfi.hIcon);
	}
	m_tcCategory.SetImageList( &m_imageList, TVSIL_NORMAL);

	// TODO:  Add extra initialization here
	if( m_manager)
	{
		const CBigCategoryList& bcl = m_manager->m_items;
		for( CBigCategoryList::const_iterator i=bcl.begin(),end=bcl.end(); i != end; ++i)
		{
			AddBigCategoryToTree( *i, TVI_LAST);
		}
	}

	return TRUE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

HTREEITEM CSelectCategoryDlg::AddBigCategoryToTree( const CBigCategory& bc, HTREEITEM hInsertAfter)
{
	if( hInsertAfter == NULL )
		hInsertAfter = TVI_FIRST;
	HTREEITEM hSelItem = m_tcCategory.GetSelectedItem();
	HTREEITEM hParent = m_tcCategory.GetParentItem(hSelItem);
	HTREEITEM hRes;
	if(hParent)
	{
		hRes = m_tcCategory.InsertItem( bc.m_name.c_str(), DIR_CLOSE_ICON, DIR_OPEN_ICON, hParent, hInsertAfter);
	}
	else
	{
		// ����ǰ��Ϊ���࣬�������Ӹô����µ�����С��
		hRes = m_tcCategory.InsertItem( bc.m_name.c_str(), DIR_CLOSE_ICON, DIR_OPEN_ICON, TVI_ROOT, hInsertAfter);
		HTREEITEM hSmall;
		for(CSmallCategoryList::const_iterator itor=bc.m_items.begin(),end=bc.m_items.end(); itor!=end; ++itor)
		{
			hSmall = m_tcCategory.InsertItem( (*itor).m_name.c_str(), DIR_CLOSE_ICON, DIR_OPEN_ICON, hRes, TVI_LAST);
			m_tcCategory.SetItemData( hSmall, (DWORD)&(*itor));
		}
	}
	m_tcCategory.SetItemData( hRes, (DWORD)&bc);
	if( &bc == m_selBcp)
	{
		// ѡ�е�ǰ��
		m_tcCategory.SelectItem( hRes);
	}
	return hRes;
}

void CSelectCategoryDlg::OnOK()
{
	// TODO: Add your specialized code here and/or call the base class
	//�ж�Ŀ�����Ƿ�Ϊ�ջ��뵱ǰ��������ͬ
	if( m_selScp == NULL)
	{
		AfxMessageBox( L"��ѡ��Ҫ�ƶ�����С�࣬�����ƶ��������У�");
		return;
	}
	if(m_selScp == (CSmallCategory*)curScp)
	{
		AfxMessageBox( L"�޷����Ƶ���������С���У�");
		return;
	}
	CDialog::OnOK();
}

void CSelectCategoryDlg::OnTvnSelchangedTree1(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTREEVIEW pNMTreeView = reinterpret_cast<LPNMTREEVIEW>(pNMHDR);
	// TODO: Add your control notification handler code here
	HTREEITEM hSel = pNMTreeView->itemNew.hItem;
	HTREEITEM hParent = m_tcCategory.GetParentItem(hSel);
	m_selBcp = hParent ? (CBigCategory*)m_tcCategory.GetItemData( hParent) : NULL;
	if(hParent)
	{
		//ѡ��С�����趨С��ָ��
		m_selScp = (CSmallCategory*)m_tcCategory.GetItemData( hSel);
	}
	else
	{
		//ѡ�������չ���ô���
		m_tcCategory.Expand(hSel,TVE_EXPAND);
		m_selScp = NULL;
	}
	*pResult = 0;
}

void CSelectCategoryDlg::OnNMDblclkTree1(NMHDR *pNMHDR, LRESULT *pResult)
{
	// TODO: Add your control notification handler code here
	CPoint pt;
	::GetCursorPos( &pt);
	m_tcCategory.ScreenToClient( &pt);
	HTREEITEM hRes = m_tcCategory.HitTest( pt, NULL);
	if( hRes)
	{
		OnOK();
	}
	*pResult = 0;
}
