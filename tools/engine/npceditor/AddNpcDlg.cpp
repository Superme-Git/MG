// AddNpcDlg.cpp : implementation file
//

#include "stdafx.h"
#include "AddNpcDlg.h"

#include "SoundApp.h"
#include "MainFrm.h"
#include "MainDlg.h"
#include "SoundEditorView.h"


// CAddNpcDlg dialog

IMPLEMENT_DYNAMIC(CAddNpcDlg, CDialog)

CAddNpcDlg::CAddNpcDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CAddNpcDlg::IDD, pParent)
{

}

CAddNpcDlg::~CAddNpcDlg()
{
}

void CAddNpcDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

BOOL CAddNpcDlg::OnInitDialog()
{
	m_pCombox=(CComboBox*)GetDlgItem(IDD_COMBOX_BASEID);
	if (m_pCombox!=NULL)
	{
		CMainFrame* pMainFrame=(CMainFrame*)AfxGetApp()->m_pMainWnd;
		if (pMainFrame!=NULL)
		{
			CSoundEditorView* pMaiView=(CSoundEditorView*)pMainFrame->m_HSplitterWindow.GetPane(0,0);
			if (pMaiView)
			{
				CMainDlg* pMainDlg=(CMainDlg*)(&(pMaiView->m_mainDlg));
				if (pMainDlg!=NULL)
				{
					m_pSoundApp=&(pMainDlg->m_soundApp);
					InitCombox();
				}
			}
			//m_pNpcDlg=(CNpcPropertyDlg*)pMainFrame->GetDlgItem(IDD_NPCPANEL);
		}
	}
	
	return true;
}

void CAddNpcDlg::InitCombox()
{
	if (m_pCombox!=NULL&&m_pSoundApp!=NULL)
	{
		m_pSoundApp->InitNpcBaseComBox(m_pCombox);
	}
}




BEGIN_MESSAGE_MAP(CAddNpcDlg, CDialog)
	ON_BN_CLICKED(IDOK_ADDNPC, &CAddNpcDlg::OnBnClickedAddnpc)
	ON_BN_CLICKED(IDCANCEL_ADDNPC, &CAddNpcDlg::OnBnClickedCancelAddnpc)
END_MESSAGE_MAP()


// CAddNpcDlg message handlers

void CAddNpcDlg::OnBnClickedAddnpc()
{
	if (m_pCombox!=NULL&&m_pSoundApp!=NULL)
	{
		int index=m_pCombox->GetCurSel();
		stNpcBaseData* pBase=(stNpcBaseData*)m_pCombox->GetItemDataPtr(index);
		if (pBase!=NULL)
		{
			m_pSoundApp->AddNewNpc(pBase->BaseID);
			EndDialog(1);
		}
	}
}

void CAddNpcDlg::OnBnClickedCancelAddnpc()
{
	// TODO: Add your control notification handler code here
	EndDialog(1);
}
