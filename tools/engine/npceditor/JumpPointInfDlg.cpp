// JumpPointInfDlg.cpp : implementation file
//

#include "stdafx.h"
#include "JumpPointInfDlg.h"
#include "MainDlg.h"
#include "MainFrm.h"
#include "SoundEditorView.h"


// CJumpPointInfDlg dialog

IMPLEMENT_DYNAMIC(CJumpPointInfDlg, CDialog)

CJumpPointInfDlg::CJumpPointInfDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CJumpPointInfDlg::IDD, pParent)
{

}

CJumpPointInfDlg::~CJumpPointInfDlg()
{
}

void CJumpPointInfDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

BOOL CJumpPointInfDlg::OnInitDialog()
{
	CDialog::OnInitDialog();
	/*m_pBaseID=(CEdit*)GetDlgItem(IDC_EEDI_BASEID);
	m_pModeName=(CEdit*)GetDlgItem(IDC_EEDI_MODEL);
	m_pName=(CEdit*)GetDlgItem(IDC_EDIT_NAME);
	m_pXpos=(CEdit*)GetDlgItem(IDC_EDIT_XPOS);
	m_pYpos=(CEdit*)GetDlgItem(IDC_EDIT_YPOS);*/
	CMainFrame* pMainFrame=(CMainFrame*)AfxGetApp()->m_pMainWnd;
	if (pMainFrame!=NULL)
	{
		CSoundEditorView* pMaiView=(CSoundEditorView*)pMainFrame->m_HSplitterWindow.GetPane(0,0);
		if (pMaiView)
		{
			m_pMainDlg=(CMainDlg*)(&(pMaiView->m_mainDlg));
			m_pMainDlg->m_soundApp.SetSelJumpPoint(-1);

		}
		//m_pNpcDlg=(CNpcPropertyDlg*)pMainFrame->GetDlgItem(IDD_NPCPANEL);
	}

	//m_pCurSelNpc=NULL;

	return true;
}


BEGIN_MESSAGE_MAP(CJumpPointInfDlg, CDialog)
	ON_BN_CLICKED(ID_NewJumpOK, &CJumpPointInfDlg::OnBnClickedJPInfOK)
	ON_BN_CLICKED(IDC_DELJUMP, &CJumpPointInfDlg::OnBnClickedDeljump)
	ON_BN_CLICKED(ID_ADDJUMP, &CJumpPointInfDlg::OnBnClickedAddjump)
	ON_BN_CLICKED(IDD_SAVEJPINF, &CJumpPointInfDlg::OnBnClickedSavejpinf)
END_MESSAGE_MAP()


// CJumpPointInfDlg message handlers

void CJumpPointInfDlg::OnBnClickedJPInfOK()
{
	// TODO: Add your control notification handler code here
	CEdit* pMapID=(CEdit*)GetDlgItem(IDC_DestMapID);
	CEdit* pXpos=(CEdit*)GetDlgItem(IDC_DestX);
	CEdit* pYpos=(CEdit*)GetDlgItem(IDC_DestY);
	CString cstrMapID;
	int mapID;
	pMapID->GetWindowText(cstrMapID);
	mapID=_ttoi(cstrMapID.GetBuffer());

	CString cstrDest_X;
	int Dest_X;
	pXpos->GetWindowText(cstrDest_X);
	Dest_X=_ttoi(cstrDest_X.GetBuffer());

	CString cstrDest_Y;
	int Dest_Y;
	pYpos->GetWindowText(cstrDest_Y);
	Dest_Y=_ttoi(cstrDest_Y.GetBuffer());

	m_pMainDlg->m_soundApp.ChangeEditJPInf(mapID,Dest_X,Dest_Y);
}

void CJumpPointInfDlg::OnBnClickedDeljump()
{
	// TODO: Add your control notification handler code here
	m_pMainDlg->m_soundApp.DelCurEditJumPoint();
}

void CJumpPointInfDlg::OnBnClickedAddjump()
{
	// TODO: Add your control notification handler code here
	m_pMainDlg->m_soundApp.AddNewJumpPoint();
}

void CJumpPointInfDlg::ChangeJPInf(int mapID,int dest_X,int dest_Y)
{
	CEdit* pMapID=(CEdit*)GetDlgItem(IDC_DestMapID);
	CEdit* pXpos=(CEdit*)GetDlgItem(IDC_DestX);
	CEdit* pYpos=(CEdit*)GetDlgItem(IDC_DestY);
	wchar_t strMapID[16]={0};
	wchar_t xPos[16]={0};
	wchar_t yPos[16]={0};
    
	wsprintf(strMapID,L"%d",mapID);
	wsprintf(xPos,L"%d",dest_X);
	wsprintf(yPos,L"%d",dest_Y);
	pMapID->SetWindowText(strMapID);
	pXpos->SetWindowText(xPos);
	pYpos->SetWindowText(yPos);

	

}


void CJumpPointInfDlg::OnBnClickedSavejpinf()
{
	// TODO: Add your control notification handler code here
	if (m_pMainDlg->m_soundApp.SaveJumpPointInf())
	{
		MessageBoxA(NULL,"保存跳转点信息成功!!","提示",MB_OK);
	}else
	{
		MessageBoxA(NULL,"保存跳转点信息失败!!","提示",MB_OK);
	}
}

void CJumpPointInfDlg::JumpIsland(int idx) //跳到某一个孤岛的第一个坐标
{
	if (m_pMainDlg)
	{
		m_pMainDlg->m_soundApp.JumpToIsland(idx);
	}
}
