


#include "stdafx.h"
#include "SpriteEditor.h"
#include "EdtIndActDlg.h"
#include "AnimationInfoDlg.h"




IMPLEMENT_DYNAMIC(CEdtIndActDlg, CDialog)

void CEdtIndActDlg::OnBnClickedButton3()
{
	if (m_CurEdt < 0 || m_CurEdt >= m_nFrame)
	{
		AfxMessageBox(L"请先选中一行！");
		return;
	}
	DoSet();
	wchar_t buf[64];
	wchar_t framebuf[64];

	int i, j, frame;
	m_lstEdtIndAct.GetItemText(m_CurEdt, 2, framebuf, 64);
	frame = _wtoi(framebuf) + 1;


	for (i=m_CurEdt+1;i<m_nFrame;++i, ++frame)
	{
		m_lstEdtIndAct.GetItemText(i, 1, buf, 64);

		std::wstring name = buf;
		int maxn = 1;
		for(j=0; j<(int)m_BaseActs.size();++j)
		{
			if( m_BaseActs[j].first == name )
			{
				maxn = m_BaseActs[j].second;
				break;
			}
		}
		maxn = min(maxn - 1, frame);
		wsprintf(framebuf, L"%d", maxn);
		m_lstEdtIndAct.SetItemText(i, 2, framebuf);
	}
}

void CEdtIndActDlg::OnBnClickedButton2()
{
	if (m_CurEdt < 0 || m_CurEdt >= m_nFrame)
	{
		AfxMessageBox(L"请先选中一行！");
		return;
	}
	DoSet();
	wchar_t buf[64];
	wchar_t framebuf[64];

	int i, j, frame;
	m_lstEdtIndAct.GetItemText(m_CurEdt, 2, framebuf, 64);
	frame = _wtoi(framebuf);


	for (i=m_CurEdt+1;i<m_nFrame;++i)
	{
		m_lstEdtIndAct.GetItemText(i, 1, buf, 64);

		std::wstring name = buf;
		int maxn = 1;
		for(j=0; j<(int)m_BaseActs.size();++j)
		{
			if( m_BaseActs[j].first == name )
			{
				maxn = m_BaseActs[j].second;
				break;
			}
		}
		maxn = min(maxn - 1, frame);
		wsprintf(framebuf, L"%d", maxn);
		m_lstEdtIndAct.SetItemText(i, 2, framebuf);
	}
}

void CEdtIndActDlg::OnBnClickedButton1()
{
	if (m_CurEdt < 0 || m_CurEdt >= m_nFrame)
	{
		AfxMessageBox(L"请先选中一行！");
		return;
	}
	DoSet();
	wchar_t buf[64];
	wchar_t framebuf[64];
	wchar_t maxframebuf[64];

	m_lstEdtIndAct.GetItemText(m_CurEdt, 1, buf, 64);

	int i, frame;
	std::wstring name = buf;
	int maxn = 1;
	for(i=0; i<(int)m_BaseActs.size();++i)
	{
		if( m_BaseActs[i].first == name )
		{
			maxn = m_BaseActs[i].second;
			break;
		}
	}
	wsprintf(maxframebuf, L"%d", maxn - 1);

	for (i=m_CurEdt+1;i<m_nFrame;++i)
	{
		m_lstEdtIndAct.SetItemText(i, 1, buf);
		m_lstEdtIndAct.GetItemText(i, 2, framebuf, 64);
		frame = _wtoi(framebuf);
		if (frame >= maxn)
		{
			m_lstEdtIndAct.SetItemText(i, 2, maxframebuf);
		}
	}
}

void CEdtIndActDlg::OnBnClickedBtnSetind()
{

}

void CEdtIndActDlg::DoSet()
{

	int cursel = m_CurEdt;
	if( cursel <0 || cursel >= m_nFrame) return;

	wchar_t buf[64];

	int sel1 = m_cmbIndAct.GetCurSel();
	int sel2 = m_cmbIndFrame.GetCurSel();
	if( sel1 <0 || sel2< 0 ) return;

	m_cmbIndAct.GetLBText(sel1, buf);
	m_lstEdtIndAct.SetItemText(cursel,1,buf);

	m_cmbIndFrame.GetLBText(sel2, buf);
	m_lstEdtIndAct.SetItemText(cursel,2,buf);
}

void CEdtIndActDlg::OnCbnSelchangeCmbIndactname()
{
	int sel = m_cmbIndAct.GetCurSel();
	if( sel < 0 ) return;
	wchar_t buf[64];
	m_cmbIndAct.GetLBText(sel, buf);
	std::wstring name = buf;
	int maxn = 1;
	for(int i=0; i<(int)m_BaseActs.size();++i)
	{
		if( m_BaseActs[i].first == name )
		{
			maxn = m_BaseActs[i].second;
			break;
		}
	}
	int oldsel = m_cmbIndFrame.GetCurSel();
	m_cmbIndFrame.ResetContent();
	for(int i=0; i<maxn; ++i)
	{
		wsprintf(buf, L"%d",i);
		m_cmbIndFrame.AddString(buf);
	}
	if( oldsel < 0 || oldsel >= maxn )
		oldsel = 0;
	m_cmbIndFrame.SetCurSel(oldsel);
}

void CEdtIndActDlg::OnNMClickLstEdtindact(NMHDR *pNMHDR, LRESULT *pResult)
{
	int cursel = -1;
	POSITION pos = m_lstEdtIndAct.GetFirstSelectedItemPosition();
	if (pos != NULL)
	{
		while (pos)
		{
			cursel = m_lstEdtIndAct.GetNextSelectedItem(pos);
			break;
		}

	}
	if( cursel >= 0 && cursel != m_CurEdt )
	{

		DoSet();

		RECT rect1;
		GetWindowRect(&rect1);

		RECT rect2;
		GetClientRect(&rect2);

		int iCaptionHeight = GetSystemMetrics(SM_CYCAPTION);
		int iFixedFrameSizeY = GetSystemMetrics(SM_CYFIXEDFRAME);
		int iFixedFrameSizeX = GetSystemMetrics(SM_CXFIXEDFRAME);

		rect2.top += iCaptionHeight + iFixedFrameSizeY;
		rect2.left += iFixedFrameSizeX;

		rect1.left += rect2.left;
		rect1.top += rect2.top;

		RECT rect3;
		GetDlgItem(IDC_LST_EDTINDACT)->GetWindowRect(&rect3);
		RECT rect4;
		GetDlgItem(IDC_LST_EDTINDACT)->GetClientRect(&rect4);

		rect3.left += rect4.left;
		rect3.top += rect4.top;

		int offx = rect3.left - rect1.left;
		int offy = rect3.top - rect1.top;
		RECT rect;
		m_lstEdtIndAct.GetItemRect(cursel, &rect, LVIR_BOUNDS);

		rect.left += offx;
		rect.right += offx;
		rect.top += offy;
		rect.bottom += offy;

		int w0 = m_lstEdtIndAct.GetColumnWidth(0);
		int w1 = m_lstEdtIndAct.GetColumnWidth(1);
		int w2 = m_lstEdtIndAct.GetColumnWidth(2);
		rect.left += w0+2;
		rect.right = rect.left + w1-4;

		GetDlgItem(IDC_CMB_INDACTNAME)->MoveWindow(&rect);

		rect.left += w1;
		rect.right = rect.left + w2 -4;
		GetDlgItem(IDC_CMB_INDFRAME)->MoveWindow(&rect);
		m_cmbIndAct.ShowWindow(SW_SHOW);
		m_cmbIndFrame.ShowWindow(SW_SHOW);

		UpdateWindow();

		m_CurEdt = cursel;
		wchar_t buf[64];
		wsprintf(buf, L"%d", cursel);
		SetDlgItemText(IDC_EDT_ACTSEQ, buf);

		m_lstEdtIndAct.GetItemText(cursel,1, buf, 64);
		std::wstring name = buf;
		int maxn = 1;
		for(int i=0; i<(int)m_BaseActs.size();++i)
		{
			if( m_BaseActs[i].first == name )
			{
				m_cmbIndAct.SetCurSel(i);
				maxn = m_BaseActs[i].second;
				break;
			}
		}

		m_cmbIndFrame.ResetContent();
		for(int i=0; i<maxn; ++i)
		{
			wsprintf(buf, L"%d",i);
			m_cmbIndFrame.AddString(buf);
		}
		m_lstEdtIndAct.GetItemText(cursel, 2, buf, 64);
		int frame = _wtoi(buf);
		if( frame <0 || frame >=maxn )
			frame = 0;
		m_cmbIndFrame.SetCurSel(frame);

	}
	*pResult = 0;
}

void CEdtIndActDlg::OnHdnItemdblclickLstEdtindact(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMHEADER phdr = reinterpret_cast<LPNMHEADER>(pNMHDR);
	*pResult = 0;
}

void CEdtIndActDlg::OnNMDblclkLstEdtindact(NMHDR *pNMHDR, LRESULT *pResult)
{
	*pResult = 0;
}

BOOL CEdtIndActDlg::OnInitDialog()
{
	CDialog::OnInitDialog();



	m_lstEdtIndAct.SetExtendedStyle(LVS_EX_FULLROWSELECT);
	LONG style = GetWindowLong(m_lstEdtIndAct.GetSafeHwnd(), GWL_STYLE);
	style |= WS_CLIPCHILDREN | WS_CLIPSIBLINGS;
	SetWindowLong(m_lstEdtIndAct.GetSafeHwnd(), GWL_STYLE, style);

	m_BaseActs.push_back(std::pair<std::wstring,int>(L"",1));
	Nuclear::SpriteManager *pSprMan = m_pInfoDlg->m_pSpriteMgr;
	pSprMan->DumpBaseAction(m_pInfoDlg->m_ModelName, m_BaseActs);
	for(int i=0; i<(int)m_BaseActs.size();++i)
		m_cmbIndAct.AddString(m_BaseActs[i].first.c_str());
	m_cmbIndAct.SetCurSel(0);

	m_lstEdtIndAct.InsertColumn(0, L"帧", 0, 100);
	m_lstEdtIndAct.InsertColumn(1, L"引用动作", 0, 300);
	m_lstEdtIndAct.InsertColumn(2, L"引用帧", 0, 100);

	if( m_nFrame != (int)m_pInfoDlg->m_Links.size() )
		m_pInfoDlg->m_Links.resize(m_nFrame);

	m_cmbIndAct.ShowWindow(SW_HIDE);
	m_cmbIndFrame.ShowWindow(SW_HIDE);

	wchar_t buf[32];
	for(int i=0; i<m_nFrame; ++i)
	{
		wsprintf(buf, L"%d", i);
		m_lstEdtIndAct.InsertItem(i, buf);
		m_lstEdtIndAct.SetItemText(i, 1, m_pInfoDlg->m_Links[i].m_Name.c_str());
		wsprintf(buf, L"%d", m_pInfoDlg->m_Links[i].m_Frame);
		m_lstEdtIndAct.SetItemText(i, 2, buf);
	}



	RECT rect1;
	GetWindowRect(&rect1);

	RECT rect2;
	GetClientRect(&rect2);

	int iCaptionHeight = GetSystemMetrics(SM_CYCAPTION);
	int iFixedFrameSizeY = GetSystemMetrics(SM_CYFIXEDFRAME);
	int iFixedFrameSizeX = GetSystemMetrics(SM_CXFIXEDFRAME);

	rect2.top += iCaptionHeight + iFixedFrameSizeY;
	rect2.left += iFixedFrameSizeX;

	rect1.left += rect2.left;
	rect1.top += rect2.top;

	RECT rect3;
	GetDlgItem(IDC_LST_EDTINDACT)->GetWindowRect(&rect3);
	RECT rect4;
	GetDlgItem(IDC_LST_EDTINDACT)->GetClientRect(&rect4);

	rect3.left += rect4.left;
	rect3.top += rect4.top;

	int offx = rect3.left - rect1.left;
	int offy = rect3.top - rect1.top;

	m_lstEdtIndAct.InsertItem(m_nFrame, L"tmp");
	RECT rect;
	m_lstEdtIndAct.GetItemRect(m_nFrame, &rect, LVIR_BOUNDS);
	m_lstEdtIndAct.DeleteItem(m_nFrame);

	int height = rect.bottom - rect.top;
	rect.left += offx;
	rect.right += offx;
	rect.bottom += offy + height * 2;
	rect.top += offy + height;




	int w0 = m_lstEdtIndAct.GetColumnWidth(0);
	int w1 = m_lstEdtIndAct.GetColumnWidth(1);
	int w2 = m_lstEdtIndAct.GetColumnWidth(2);
	rect.left += w0+2;
	rect.right = rect.left + w1 - 4;

	m_btnCopyActionName.MoveWindow(&rect);
	m_btnCopyActionName.BringWindowToTop();


	rect.left += w1;
	rect.right = rect.left + w2 / 2- 4;

	m_btnCopyFrame.MoveWindow(&rect);
	m_btnCopyFrame.BringWindowToTop();

	rect.left = rect.right + 4;
	rect.right += w2 / 2 + 4;
	m_btnIncFrame.MoveWindow(&rect);
	m_btnIncFrame.BringWindowToTop();

	return TRUE;  

}

void CEdtIndActDlg::OnOK()
{
	DoSet();

	wchar_t buf[64];
	for(int i=0; i<m_nFrame; ++i)
	{
		m_lstEdtIndAct.GetItemText(i,1,buf,64);
		m_pInfoDlg->m_Links[i].m_Name = buf;
		m_lstEdtIndAct.GetItemText(i,2,buf,64);
		m_pInfoDlg->m_Links[i].m_Frame = _wtoi(buf);
	}

	CDialog::OnOK();
}

BEGIN_MESSAGE_MAP(CEdtIndActDlg, CDialog)
	ON_NOTIFY(NM_DBLCLK, IDC_LST_EDTINDACT, &CEdtIndActDlg::OnNMDblclkLstEdtindact)
	ON_NOTIFY(HDN_ITEMDBLCLICK, 0, &CEdtIndActDlg::OnHdnItemdblclickLstEdtindact)
	ON_NOTIFY(NM_CLICK, IDC_LST_EDTINDACT, &CEdtIndActDlg::OnNMClickLstEdtindact)
	ON_CBN_SELCHANGE(IDC_CMB_INDACTNAME, &CEdtIndActDlg::OnCbnSelchangeCmbIndactname)
	ON_BN_CLICKED(IDC_BTN_SETIND, &CEdtIndActDlg::OnBnClickedBtnSetind)
	ON_BN_CLICKED(IDC_BUTTON1, &CEdtIndActDlg::OnBnClickedButton1)
	ON_BN_CLICKED(IDC_BUTTON2, &CEdtIndActDlg::OnBnClickedButton2)
	ON_BN_CLICKED(IDC_BUTTON3, &CEdtIndActDlg::OnBnClickedButton3)
END_MESSAGE_MAP()

void CEdtIndActDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_LST_EDTINDACT, m_lstEdtIndAct);
	DDX_Control(pDX, IDC_CMB_INDACTNAME, m_cmbIndAct);
	DDX_Control(pDX, IDC_CMB_INDFRAME, m_cmbIndFrame);
	DDX_Control(pDX, IDC_BUTTON1, m_btnCopyActionName);
	DDX_Control(pDX, IDC_BUTTON2, m_btnCopyFrame);
	DDX_Control(pDX, IDC_BUTTON3, m_btnIncFrame);
}

CEdtIndActDlg::~CEdtIndActDlg()
{
}

CEdtIndActDlg::CEdtIndActDlg(CWnd* pParent /*=NULL*/)
: CDialog(CEdtIndActDlg::IDD, pParent)
{
	m_CurEdt = -1;
}

