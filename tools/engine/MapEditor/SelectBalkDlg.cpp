// SelectBalkDlg.cpp : implementation file
//
#include "stdafx.h"
#include "MapEditor.h"
#include "MapEditorDoc.h"
#include "MapEditorView.h"

#include "..\Common\log.h"

#include "SelectBalkDlg.h"
// CSelectBalkDlg dialog

IMPLEMENT_DYNAMIC(CSelectBalkDlg, CDialog)

CSelectBalkDlg::CSelectBalkDlg(CWnd* pParent /*=NULL*/)
: CDialog(CSelectBalkDlg::IDD, pParent), m_color(0), m_pMapEditorView(NULL), m_pRenderer(NULL)
{

}

CSelectBalkDlg::~CSelectBalkDlg()
{
	DestroyRenderer(m_pRenderer);
}

void CSelectBalkDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_TREE_BALK, m_tree);
	DDX_Control(pDX, IDC_COMBO1, m_comboBrushSize);
}

BOOL CSelectBalkDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// TODO:  Add extra initialization here
	//m_nPathLength = theApp.GetPath().GetLength();
	SetWindowText(_T("地图障碍"));

	RenewTrees();
	RenewCombo();

	checkRenderer();

	return TRUE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

BOOL CSelectBalkDlg::Create(UINT nIDTemplate, CWnd* pParentWnd, CMapEditorView* pMapEditorView/* = NULL*/)
{
	// TODO: Add your specialized code here and/or call the base class
	m_pMapEditorView = pMapEditorView;
	return CDialog::Create(nIDTemplate, pParentWnd);
}

bool CSelectBalkDlg::RenewTrees(void)
{
	HTREEITEM item;
	item = m_tree.InsertItem(_T("无障碍"),0,0,NULL);
	m_tree.SetItemData(item, 0);
	item = m_tree.InsertItem(_T("低障碍"),0,0,NULL);
	m_tree.SetItemData(item, 1);
	item = m_tree.InsertItem(_T("高障碍"),0,0,NULL);
	m_tree.SetItemData(item, 2);
	item = m_tree.InsertItem(_T("受伤区"),0,0,NULL);
	m_tree.SetItemData(item, 3);
	item = m_tree.InsertItem(_T("死亡区"),0,0,NULL);
	m_tree.SetItemData(item, 4);
	return true;
}

bool CSelectBalkDlg::RenewCombo(void)
{
	m_comboBrushSize.InsertString(0, _T("1"));
	m_comboBrushSize.InsertString(1, _T("2"));
	m_comboBrushSize.InsertString(2, _T("3"));
	m_comboBrushSize.InsertString(3, _T("4"));
	m_comboBrushSize.InsertString(4, _T("5"));
	m_comboBrushSize.InsertString(5, _T("10"));
	m_comboBrushSize.InsertString(6, _T("15"));
	m_comboBrushSize.InsertString(7, _T("20"));
	m_comboBrushSize.InsertString(8, _T("30"));
	m_comboBrushSize.SetCurSel(0);
	
	return true;
}

BEGIN_MESSAGE_MAP(CSelectBalkDlg, CDialog)
	ON_NOTIFY(TVN_SELCHANGED, IDC_TREE_BALK, &CSelectBalkDlg::OnTvnSelchangedTreeBalk)
	ON_WM_PAINT()
	ON_CBN_EDITCHANGE(IDC_COMBO1, &CSelectBalkDlg::OnCbnEditchangeCombo1)
	ON_CBN_SELCHANGE(IDC_COMBO1, &CSelectBalkDlg::OnCbnSelchangeCombo1)
//	ON_WM_SETFOCUS()
	ON_WM_SHOWWINDOW()
END_MESSAGE_MAP()


// CSelectBalkDlg message handlers

void CSelectBalkDlg::OnTvnSelchangedTreeBalk(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTREEVIEW pNMTreeView = reinterpret_cast<LPNMTREEVIEW>(pNMHDR);
	// TODO: Add your control notification handler code here
	m_brushType = static_cast<unsigned int>(m_tree.GetItemData(pNMTreeView->itemNew.hItem));
	m_pMapEditorView->SetBrushType(m_brushType);
	m_color = CMapEditorView::BALK_COLORS[m_brushType];
	this->Invalidate();
	*pResult = 0;
}

void CSelectBalkDlg::OnPaint()
{
	CPaintDC dc(this); // device context for painting
	// TODO: Add your message handler code here
	// Do not call CDialog::OnPaint() for painting messages
	checkRenderer();

	HRESULT hr;
	V(m_pRenderer->Begin());
	if (m_color != 0)
	{
		m_pRenderer->Clear(0xFFFFFFFF);
		m_pRenderer->DrawBox(0, 0, PICTURE_WIDTH, PICTURE_HEIGHT, m_color);
	}
	V(m_pRenderer->End());
}

void CSelectBalkDlg::OnCbnEditchangeCombo1()
{
	// TODO: Add your control notification handler code here
	CString temp;
	m_comboBrushSize.GetWindowText(temp);
	if (!SetEditMode(temp))
	{
		AfxMessageBox(_T("请输入大于0的数字"));
	}
}

void CSelectBalkDlg::OnCbnSelchangeCombo1()
{
	// TODO: Add your control notification handler code here
	CString temp;
	m_comboBrushSize.GetLBText(m_comboBrushSize.GetCurSel(), temp);
	if (!SetEditMode(temp))
	{
		AfxMessageBox(_T("请选择大于0的数字"));
	}
	
}

bool CSelectBalkDlg::SetEditMode(CString &val)
{
	int size = 0;
	size = _wtoi(val);
	if (size == 0)
		return false;
	m_pMapEditorView->SetEditMode(size);
	return true;
}

void CSelectBalkDlg::OnOK()
{
	// TODO: Add your specialized code here and/or call the base class

	//CDialog::OnOK();
}

void CSelectBalkDlg::OnShowWindow(BOOL bShow, UINT nStatus)
{
	CDialog::OnShowWindow(bShow, nStatus);

	// TODO: Add your message handler code here
	CString temp;
	m_comboBrushSize.GetLBText(m_comboBrushSize.GetCurSel(), temp);
	SetEditMode(temp);
	m_pMapEditorView->SetBrushType(m_brushType);
}
