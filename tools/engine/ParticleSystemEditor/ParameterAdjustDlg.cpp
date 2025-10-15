// ParameterAdjustDlg.cpp : 实现文件
//

#include "stdafx.h"
#include "ParticleSystemEditor.h"
#include "ParameterAdjustDlg.h"


// CParameterAdjustDlg 对话框

IMPLEMENT_DYNAMIC(CParameterAdjustDlg, CDialog)

CParameterAdjustDlg::CParameterAdjustDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CParameterAdjustDlg::IDD, pParent)
{
	m_nCurSelParam = -1;

}

CParameterAdjustDlg::~CParameterAdjustDlg()
{
}

void CParameterAdjustDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_TREE_ParameterList, m_treeParameterList);
}


BEGIN_MESSAGE_MAP(CParameterAdjustDlg, CDialog)
	ON_BN_CLICKED(IDOK, &CParameterAdjustDlg::OnBnClickedOk)
	ON_BN_CLICKED(IDCANCEL, &CParameterAdjustDlg::OnBnClickedCancel)
	ON_NOTIFY(NM_CLICK, IDC_TREE_ParameterList, &CParameterAdjustDlg::OnNMClickTreeParameterlist)
	ON_WM_PAINT()
	ON_NOTIFY(TVN_SELCHANGED, IDC_TREE_ParameterList, &CParameterAdjustDlg::OnTvnSelchangedTreeParameterlist)
	ON_WM_LBUTTONDOWN()
	ON_WM_MOUSEMOVE()
END_MESSAGE_MAP()


// CParameterAdjustDlg 消息处理程序

BOOL CParameterAdjustDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// TODO:  在此添加额外的初始化

	GetDlgItem(IDC_STATIC_ParameterCurve)->GetWindowRect(&m_rctDrawParameterCurve);
	ScreenToClient(&m_rctDrawParameterCurve); 

	HTREEITEM hRoot = m_treeParameterList.InsertItem(L"ParticleSystemParameterList");
	int cnt = (int)m_paramPropertyVector.size();
	for(int i=0; i<cnt; i++)
	{
		m_treeParameterList.InsertItem(m_paramPropertyVector[i].m_strParaName, hRoot);
	}

	BOOL bres = m_treeParameterList.Expand(hRoot, TVE_EXPAND);
	m_treeParameterList.Invalidate();



	return TRUE;  // return TRUE unless you set the focus to a control
	// 异常: OCX 属性页应返回 FALSE
}

void CParameterAdjustDlg::OnBnClickedOk()
{
	// TODO: 在此添加控件通知处理程序代码
	OnOK();
}

void CParameterAdjustDlg::OnBnClickedCancel()
{
	// TODO: 在此添加控件通知处理程序代码
	OnCancel();
}

void CParameterAdjustDlg::OnNMClickTreeParameterlist(NMHDR *pNMHDR, LRESULT *pResult)
{
	// TODO: 在此添加控件通知处理程序代码
	*pResult = 0;
}

void CParameterAdjustDlg::OnPaint()
{
	CPaintDC dc(this); // device context for painting
	// TODO: 在此处添加消息处理程序代码

	int nWidth = m_rctDrawParameterCurve.Width();
	int nHeight = m_rctDrawParameterCurve.Height();
	int centx = m_rctDrawParameterCurve.left + nWidth/2 ;
	int centy = m_rctDrawParameterCurve.top + nHeight/2 ;

	CBrush brush;
	BOOL bres = brush.CreateSolidBrush(RGB(120, 120, 120));
	dc.FillRect(&m_rctDrawParameterCurve, &brush);
	brush.DeleteObject();

	HPEN redPen, greenPen, whitePen, grayPen, blackPen, OldPen;
	redPen = CreatePen(PS_SOLID, 1, RGB(255, 0, 0));
	greenPen = CreatePen(PS_SOLID, 1, RGB(0, 255, 0));//创建画笔
	whitePen = CreatePen(PS_SOLID, 1, RGB(255, 255, 255));
	grayPen = CreatePen(PS_SOLID, 1, RGB(180, 180, 180));
	blackPen = CreatePen(PS_SOLID, 1, RGB(0, 0, 0));

	int dx = 20;
	int dy = 20;
	int cntx = nWidth/dx;
	int cnty = nHeight/dy;
	OldPen = (HPEN)dc.SelectObject(grayPen);//选中新画笔时返回旧画笔
	for(int i=3; i<cntx; i++)
	{
		dc.MoveTo(m_rctDrawParameterCurve.left +i*dx, m_rctDrawParameterCurve.top);
		dc.LineTo(m_rctDrawParameterCurve.left +i*dx, m_rctDrawParameterCurve.bottom-35);
	}
	for(int i=0; i<cnty; i++)
	{
		dc.MoveTo(m_rctDrawParameterCurve.left+55, m_rctDrawParameterCurve.top +i*dy);
		dc.LineTo(m_rctDrawParameterCurve.right, m_rctDrawParameterCurve.top +i*dy);
	}

	dc.SelectObject(redPen);
	dc.MoveTo(m_rctDrawParameterCurve.left+60, m_rctDrawParameterCurve.top);
	dc.LineTo(m_rctDrawParameterCurve.left+60, m_rctDrawParameterCurve.bottom -35);

	if(m_nCurSelParam != -1)
	{
		int oldBKMode = dc.SetBkMode(TRANSPARENT);
		COLORREF cr = dc.SetTextColor(RGB(200, 200, 200));

		for(int i=5; i<cntx; i++, i++)
		{
			CString str;
			str.Format(L"%d", (i-3)/2*10);
			dc.TextOut(m_rctDrawParameterCurve.left +i*dx-10, m_rctDrawParameterCurve.bottom-35, str);
		}

		for(int i=cnty/2; i<cnty-1; i++)
		{
			CString str;
			str.Format(L"%d", (i-cnty/2)*(-10));
			dc.TextOut(m_rctDrawParameterCurve.left+25, m_rctDrawParameterCurve.top +i*dy -8, str);
		}
		for(int i=cnty/2; i>0; i--)
		{
			CString str;
			str.Format(L"%d", (i-cnty/2)*(-10));
			dc.TextOut(m_rctDrawParameterCurve.left+25, m_rctDrawParameterCurve.top +i*dy -8, str);
		}

		float fValueIndex = 0.0f;
		if(m_paramPropertyVector[m_nCurSelParam].m_bValueInt)
		{
			fValueIndex = (float)(m_paramPropertyVector[m_nCurSelParam].m_nDefault)/m_paramPropertyVector[m_nCurSelParam].m_nMax;
		}
		else
		{
			fValueIndex = m_paramPropertyVector[m_nCurSelParam].m_fDefault/m_paramPropertyVector[m_nCurSelParam].m_fMax;
		}
		fValueIndex = cnty/2 -10*fValueIndex;

		dc.SelectObject(blackPen);
		dc.MoveTo(m_rctDrawParameterCurve.left+60, m_rctDrawParameterCurve.top +(int)(fValueIndex*dy));
		dc.LineTo(m_rctDrawParameterCurve.right, m_rctDrawParameterCurve.top +(int)(fValueIndex*dy));

		dc.SetBkMode(oldBKMode);
		dc.SetTextColor(cr);
	}

	dc.SelectObject(OldPen);//恢复画笔
	DeleteObject(redPen);
	DeleteObject(greenPen);
	DeleteObject(whitePen);
	// 不为绘图消息调用 CDialog::OnPaint()
}

void CParameterAdjustDlg::OnTvnSelchangedTreeParameterlist(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMTREEVIEW pNMTreeView = reinterpret_cast<LPNMTREEVIEW>(pNMHDR);
	// TODO: 在此添加控件通知处理程序代码
	HTREEITEM hItem = pNMTreeView->itemNew.hItem;
	CString strItem = m_treeParameterList.GetItemText(hItem);

	int cnt = m_paramPropertyVector.size();
	int i=0;
	for(i=0; i<cnt; i++)
	{
		if(strItem.Compare(m_paramPropertyVector[i].m_strParaName) == 0)
		{
			m_nCurSelParam = i;
			break;
		}
	}
	if(i>=cnt)
		m_nCurSelParam = -1;

	InvalidateRect(&m_rctDrawParameterCurve);

	*pResult = 0;
}

void CParameterAdjustDlg::OnLButtonDown(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	if( m_rctDrawParameterCurve.PtInRect(point) )
	{
	}

	CDialog::OnLButtonDown(nFlags, point);
}

void CParameterAdjustDlg::OnMouseMove(UINT nFlags, CPoint point)
{
	// TODO: 在此添加消息处理程序代码和/或调用默认值

	CDialog::OnMouseMove(nFlags, point);
}
