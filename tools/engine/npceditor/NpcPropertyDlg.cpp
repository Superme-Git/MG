// NpcPropertyDlg.cpp : implementation file
//

#include "stdafx.h"
#include "NpcPropertyDlg.h"
#include "MainDlg.h"
#include "MainFrm.h"
#include "SoundEditorView.h"
#include "engine/common/worldlogiccoord.h"





// CNpcPropertyDlg dialog

IMPLEMENT_DYNAMIC(CNpcPropertyDlg, CDialog)

CNpcPropertyDlg::CNpcPropertyDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CNpcPropertyDlg::IDD, pParent)
	
{
	

}

CNpcPropertyDlg::~CNpcPropertyDlg()
{
	//m_pCurSelNpc=NULL;
}

void CNpcPropertyDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

BOOL CNpcPropertyDlg::OnInitDialog()
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
			m_pMainDlg->m_soundApp.SelectNpc(CPoint(0,0));

		}
		//m_pNpcDlg=(CNpcPropertyDlg*)pMainFrame->GetDlgItem(IDD_NPCPANEL);
	}

	//m_pCurSelNpc=NULL;

	return true;
}

void CNpcPropertyDlg::ChangeSelNpc(CNpc* pNpc)
{
	if (pNpc!=NULL/*&&pNpc!=m_pCurSelNpc*/)
	{
		//m_pCurSelNpc=pNpc;
		//if (m_pName!=NULL)
		{
			//m_pName->SetWindowText(pNpc->m_pNpcBaseData->strName.c_str());
			CEdit* pBaseID=(CEdit*)GetDlgItem(IDC_EEDI_BASEID);
			
			CEdit* pModeName=(CEdit*)GetDlgItem(IDC_EEDI_MODEL);
			CEdit* pName=(CEdit*)GetDlgItem(IDC_EDIT_NAME);
			CEdit* pXpos=(CEdit*)GetDlgItem(IDC_EDIT_XPOS);
			CEdit* pYpos=(CEdit*)GetDlgItem(IDC_EDIT_YPOS);

			CComboBox* pDir=(CComboBox*)GetDlgItem(IDC_COMBO_DIR);

			wchar_t baseid[16]={0};
			wchar_t xPos[16]={0};
			wchar_t type[16]={0};
			wchar_t yPos[16]={0};
			wchar_t dir[16]={0};

			Nuclear::Location WorldLoc=pNpc->GetLocation();
			Nuclear::Location LogicLoc=Nuclear::WorldLogicCoord::world2logic(WorldLoc.x,WorldLoc.y);
			
			wsprintf(baseid,L"%d",pNpc->m_pNpcBaseData->BaseID);
			wsprintf(type,L"%d",pNpc->m_pNpcBaseData->eNpcType);
			wsprintf(xPos,L"%d",LogicLoc.x);
			wsprintf(yPos,L"%d",LogicLoc.y);
			wsprintf(dir,L"%d",(int)pNpc->GetDirection());
			
			pBaseID->SetWindowText(baseid);
			pModeName->SetWindowText(pNpc->m_pNpcBaseData->strNpcModel.c_str());
			pName->SetWindowText(pNpc->m_pNpcBaseData->strName.c_str());
			pXpos->SetWindowText(xPos);
			pYpos->SetWindowText(yPos);
			//pDir->SetWindowText(dir);

			CEdit* pType=(CEdit*)GetDlgItem(IDC_EDIT_TYPE);
			CEdit* pTitle=(CEdit*)GetDlgItem(IDC_EDIT_TITLE);

			CEdit* pChat1=(CEdit*)GetDlgItem(IDC_EDIT_CHAT1);
			CEdit* pChat2=(CEdit*)GetDlgItem(IDC_EDIT_CHAT2);
			CEdit* pChat3=(CEdit*)GetDlgItem(IDC_EDIT_CHAT3);
			
			if (m_pMainDlg!=NULL)
			{
				m_pMainDlg->m_soundApp.m_pEditNpc=pNpc;

				m_pMainDlg->m_soundApp.InitTypeEditBox(pNpc->m_pNpcBaseData->eNpcType,pType);
				pTitle->SetWindowText(pNpc->m_pNpcBaseData->strTitle.c_str());
				
				wchar_t cstrChat1[16]={0};
				_itot_s(pNpc->m_pNpcBaseData->chat1,cstrChat1,16);
				pChat1->SetWindowText(cstrChat1);
				
				wchar_t cstrChat2[16]={0};
				_itot_s(pNpc->m_pNpcBaseData->chat2,cstrChat2,16);
				pChat2->SetWindowText(cstrChat2);

				wchar_t cstrChat3[16]={0};
				_itot_s(pNpc->m_pNpcBaseData->chat3,cstrChat3,16);
				pChat3->SetWindowText(cstrChat3);

				int dir=(int)pNpc->GetDirection();

				int dirType=pNpc->m_pNpcBaseData->dirType;
				int curSelIdx=m_pMainDlg->m_soundApp.GetComboxIdxByTypeAndDir(dirType,dir);
				m_pMainDlg->m_soundApp.InitDirCombox(dirType,pDir);
				pDir->SetCurSel(curSelIdx);
			}
			
		}
	}
}

void CNpcPropertyDlg::ChangeSelNpcPos(int x, int y)
{
	//if (m_pCurSelNpc!=NULL)
	{
		CEdit* pXpos=(CEdit*)GetDlgItem(IDC_EDIT_XPOS);
		CEdit* pYpos=(CEdit*)GetDlgItem(IDC_EDIT_YPOS);
		wchar_t xPos[16]={0};
		wchar_t yPos[16]={0};

		wsprintf(xPos,L"%d",x);
		wsprintf(yPos,L"%d",y);
		pXpos->SetWindowText(xPos);
		pYpos->SetWindowText(yPos);
	}
}



BEGIN_MESSAGE_MAP(CNpcPropertyDlg, CDialog)
	ON_BN_CLICKED(IDC_BUTTON_ADDNPC, &CNpcPropertyDlg::OnBnClickedButtonAddnpc)
	ON_BN_CLICKED(IDC_BUTTON_NEWNPC, &CNpcPropertyDlg::OnBnClickedButtonNewnpc)
	ON_BN_CLICKED(ID_NPC_OK, &CNpcPropertyDlg::OnBnClickedNpcOk)
	ON_BN_CLICKED(ID_NPC_SAVE, &CNpcPropertyDlg::OnBnClickedNpcSave)
	ON_BN_CLICKED(ID_NPC_CANCEL, &CNpcPropertyDlg::OnBnClickedNpcCancel)
END_MESSAGE_MAP()


// CNpcPropertyDlg message handlers
// NpcPropertyDlg.cpp : implementation file
//

#include "stdafx.h"
#include "NpcPropertyDlg.h"



void CNpcPropertyDlg::OnBnClickedButtonAddnpc()
{
	m_AddNpcDlg.DoModal();
}

void CNpcPropertyDlg::OnBnClickedButtonNewnpc()
{
	
	
}

void CNpcPropertyDlg::OnBnClickedNpcOk()
{
	int BaseId=0;
	int type=0;

	std::wstring strName(L"");
	std::wstring strModel(L"");
	std::wstring strTitle(L"");
	int chat1=0;
	int chat2=0;
	int chat3=0;
	int dir=4;

	CEdit* pBaseID=(CEdit*)GetDlgItem(IDC_EDIT_BASEID);
	CString cstrBaseID;
	pBaseID->GetWindowText(cstrBaseID);
	BaseId=_ttoi(cstrBaseID.GetBuffer());

	CEdit* pType=(CEdit*)GetDlgItem(IDC_EDIT_TYPE);
	CString cstrType;
	pType->GetWindowText(cstrType);
	type=_ttoi(cstrType.GetBuffer());	

	CEdit* pName=(CEdit*)GetDlgItem(IDC_EDIT_NAME);
	CString cstrName;
	pName->GetWindowText(cstrName);
	strName = cstrName.GetBuffer();

	CEdit* pModel=(CEdit*)GetDlgItem(IDC_EEDI_MODEL);
	CString cstrModel;
	pModel->GetWindowText(cstrModel);
	strModel = cstrModel.GetBuffer();

	CEdit* pTitle=(CEdit*)GetDlgItem(IDC_EDIT_TITLE);
	CString cstrTitle;
	pTitle->GetWindowText(cstrTitle);
	strTitle = cstrTitle.GetBuffer();

	CEdit* pChat1=(CEdit*)GetDlgItem(IDC_EDIT_CHAT1);
	CString cstrChat1;
	pChat1->GetWindowText(cstrChat1);
	chat1=_ttoi(cstrChat1.GetBuffer());

	CEdit* pChat2=(CEdit*)GetDlgItem(IDC_EDIT_CHAT2);
	CString cstrChat2;
	pChat2->GetWindowText(cstrChat2);
	chat2=_ttoi(cstrChat2.GetBuffer());

	CEdit* pChat3=(CEdit*)GetDlgItem(IDC_EDIT_CHAT3);
	CString cstrChat3;
	pChat3->GetWindowText(cstrChat3);
	chat3=_ttoi(cstrChat3.GetBuffer());

	CComboBox* pDir=(CComboBox*)GetDlgItem(IDC_COMBO_DIR);
	CString cstrDir;
	dir=pDir->GetCurSel();
	//dir=_ttoi(cstrDir.GetBuffer());

	stNpcBaseData* pNpcBase=(stNpcBaseData*)(m_pMainDlg->m_soundApp.GetNpcBaseById(BaseId));
	if (pNpcBase!=NULL)
	{
		pNpcBase->eNpcType=type;
		pNpcBase->strName=strName;
		pNpcBase->strNpcModel=strModel;
		pNpcBase->strTitle=strTitle;
		pNpcBase->chat1=chat1;
		pNpcBase->chat2=chat2;
		pNpcBase->chat3=chat3;

		m_pMainDlg->m_soundApp.m_pEditNpc->SetName(strName);
		//int realDir=m_pMainDlg->m_soundApp.GetDirByComboxIdxAndDirType(pNpcBase->dirType,dir);
		m_pMainDlg->m_soundApp.m_pEditNpc->SetDirection(dir);
	}
}

void CNpcPropertyDlg::OnBnClickedNpcSave()
{
	if (m_pMainDlg->m_soundApp.OnSaveNpcInf()/*&&m_pMainDlg->m_soundApp.OnSaveNpcBaseInf()*/)
	{
		MessageBoxA(NULL,"保存NPC信息成功!!","提示",MB_OK);
	}
	else
	{
		MessageBoxA(NULL,"保存NPC信息失败!!","提示",MB_OK);
	}	
}

void CNpcPropertyDlg::OnBnClickedNpcCancel()
{
	m_pMainDlg->m_soundApp.DelCurEditNpc();
	Reset();
}

void CNpcPropertyDlg::Reset()
{
	CEdit* pBaseID=(CEdit*)GetDlgItem(IDC_EDIT_BASEID);
	pBaseID->SetWindowText(L"");
	
	CEdit* pType=(CEdit*)GetDlgItem(IDC_EDIT_TYPE);
	pType->SetWindowText(L"");

	CEdit* pName=(CEdit*)GetDlgItem(IDC_EDIT_NAME);
	pName->SetWindowText(L"");

	CEdit* pModel=(CEdit*)GetDlgItem(IDC_EEDI_MODEL);
	pModel->SetWindowText(L"");

	CEdit* pTitle=(CEdit*)GetDlgItem(IDC_EDIT_TITLE);
	pTitle->SetWindowText(L"");

	CEdit* pXpos=(CEdit*)GetDlgItem(IDC_EDIT_XPOS);
	CEdit* pYpos=(CEdit*)GetDlgItem(IDC_EDIT_YPOS);
	pXpos->SetWindowText(L"");
	pYpos->SetWindowText(L"");

	CEdit* pChat1=(CEdit*)GetDlgItem(IDC_EDIT_CHAT1);
	pChat1->SetWindowText(L"");

	CEdit* pChat2=(CEdit*)GetDlgItem(IDC_EDIT_CHAT2);
	pChat2->SetWindowText(L"");
	
	CEdit* pChat3=(CEdit*)GetDlgItem(IDC_EDIT_CHAT3);
	pChat3->SetWindowText(L"");

	CComboBox* pDir=(CComboBox*)GetDlgItem(IDC_COMBO_DIR);
	pDir->ResetContent();

}

