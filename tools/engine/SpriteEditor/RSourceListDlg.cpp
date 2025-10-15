

#include "stdafx.h"
#include "SpriteEditor.h"
#include "RSourceListDlg.h"

#include "SpriteEditorDoc.h"
#include "..\engine\common\util.h"
#include "..\engine\common\pfsutil.h"



IMPLEMENT_DYNAMIC(CRSourceListDlg, CDialog)

void CRSourceListDlg::OnCbnSelchangeCmbRsrcmodelname()
{
	int cursel = m_cmbModelName.GetCurSel();
	if( cursel < 0 ) return;
	CString str;
	m_cmbModelName.GetLBText(cursel, str);
	ChangeModel(std::wstring(str));
	m_CurIndex.m_IA = m_CurIndex.m_IL = m_CurIndex.m_IR = 0;
	ShowStdAnimationList();
}
void CRSourceListDlg::ShowList()
{
	assert(m_pDoc);
	if( m_xRoles.empty() || m_xActions.empty() ) return;
}

static void collectxap(const std::wstring &dir, std::vector<std::wstring> &xapfiles, int layer)
{
	WIN32_FIND_DATA fd;
	std::wstring searchpath = dir + L"\\*";
	HANDLE hFind = ::FindFirstFile(searchpath.c_str(), &fd);

	if ( hFind != INVALID_HANDLE_VALUE )
	{
		do{
			std::wstring fn = dir + L"\\" + fd.cFileName;
			if ( fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY )
			{
				if( fd.cFileName[0] != L'.' )
				{
					collectxap(fn, xapfiles, layer+1);
				}
			}
			else if( layer > 0 )
			{
				if( fn.substr(fn.length()-4) == SFI_ExtName)
					xapfiles.push_back(fn);
			}
		}while (::FindNextFile(hFind, &fd));

		::FindClose(hFind);
	}
}

void CRSourceListDlg::InitResource(const std::wstring &dir)
{
	if( dir == m_FileDir ) return;
	m_FileDir = dir;
	m_XapRSrc.clear();
	m_xRoles.clear();
	m_xActions.clear();

	SetDlgItemText(IDC_EDT_XAPPATH, dir.c_str());

	m_listXap.DeleteAllItems();
	while( m_listXap.DeleteColumn(0));

	std::vector<std::wstring> v;
	collectxap(dir, v, 0);

	for(int i=0; i<(int)v.size();++i)
	{
		std::wstring p1, f1;
		v[i]  = v[i].substr(dir.size()+1, v[i].length()-4-dir.size()-1);
		int pos = (int)v[i].length()-1;
		while(pos>=0 && v[i][pos] != L'/' && v[i][pos] != L'\\') --pos;
		if( pos <0 || pos >= (int)v[i].length()-1) continue;
		f1 = v[i].substr(pos+1);
		pos-=1;
		int pos1 = pos;
		while(pos>=0 && v[i][pos] != L'/' && v[i][pos] != L'\\') --pos;
		if( pos >= pos1 ) continue;
		p1 = v[i].substr(pos+1,pos1-pos);


		sIndex2 ind2;
		for(ind2.m_IR = 0;ind2.m_IR<(int)m_xRoles.size();++ind2.m_IR) if( m_xRoles[ind2.m_IR] == p1 ) break;
		if( ind2.m_IR == (int)m_xRoles.size() ) m_xRoles.push_back(p1);

		for(ind2.m_IA = 0;ind2.m_IA<(int)m_xActions.size();++ind2.m_IA) if( m_xActions[ind2.m_IA] == f1 ) break;
		if( ind2.m_IA == (int)m_xActions.size() ) m_xActions.push_back(f1);

		m_XapRSrc.insert(ind2);

	}

	if( m_xActions.empty()) return;

	m_listXap.InsertColumn(0,L"",0,100);
	for(int i=0; i <(int)m_xActions.size();++i)
		m_listXap.InsertColumn(i+1,m_xActions[i].c_str(),0,100);

	for(int i=0; i< (int)m_xRoles.size();++i)
	{
		m_listXap.InsertItem(i, m_xRoles[i].c_str());
		for(int j=0; j<(int)m_xActions.size();++j)
		{
			if( m_XapRSrc.find(sIndex2(j,i)) != m_XapRSrc.end())
				m_listXap.SetItemText(i,j+1,L"Ok");
		}

	}

	ShowList();
}

void CRSourceListDlg::OnCbnSelchangeCmbStdrole()
{
	if( !m_CurIndex.Valid()) return;
	m_CurIndex.m_IR = m_cmbStdRole.GetCurSel();
	ShowStdAnimationList();
}

void CRSourceListDlg::OnCbnSelchangeCmbStdact1()
{
	if( !m_CurIndex.Valid()) return;
	m_CurIndex.m_IA = m_cmbStdAct1.GetCurSel();
	ShowStdAnimationList();
}

void CRSourceListDlg::OnCbnSelchangeCmbStdlayer()
{
	if( !m_CurIndex.Valid()) return;
	m_CurIndex.m_IL = m_cmbStdLayer.GetCurSel();
	ShowStdAnimationList();
}

void CRSourceListDlg::OnTcnSelchangeTabStdlist(NMHDR *pNMHDR, LRESULT *pResult)
{
	m_ListType2 = m_tabStdList.GetCurSel();
	ShowStdAnimationList();
	*pResult = 0;
}

static std::wstring s_lastdir;


static int CALLBACK BrowseCallbackProc2(          HWND hwnd,
										UINT uMsg,
										LPARAM lParam,
										LPARAM lpData
										)
{
	switch( uMsg )
	{
	case BFFM_INITIALIZED:
		if( !s_lastdir.empty())
			SendMessage(hwnd, BFFM_SETSELECTION, TRUE, (LPARAM)(s_lastdir.c_str()));
		break;
	default:
		break;
	}
	return 0;
}

void CRSourceListDlg::OnBnClickedBtnSelxappath()
{
	BROWSEINFO   bInfo;  
	ZeroMemory(&bInfo,   sizeof(bInfo));  
	bInfo.hwndOwner   =   NULL;  
	TCHAR   tchPath[MAX_PATH];  
	bInfo.lpszTitle   =   _T("选择统计目录:   ");  
	bInfo.ulFlags   =   BIF_RETURNONLYFSDIRS | BIF_DONTGOBELOWDOMAIN;
	bInfo.lpfn = &BrowseCallbackProc2;


	LPITEMIDLIST   lpDlist;  

	lpDlist   =   SHBrowseForFolder(&bInfo)   ;   
	if(lpDlist == NULL)  
		return;


	SHGetPathFromIDList(lpDlist,   tchPath);

	std::wstring newpath = tchPath;
	s_lastdir = newpath;

	InitResource(newpath);


}

void CRSourceListDlg::ShowStdAnimationList()
{
	assert(m_pDoc);
	InitStdResource();
	if( !m_CurIndex.Valid() ) return;
	m_cmbStdAct1.ShowWindow((m_ListType1 == 0 && m_ListType2 ==1)? SW_SHOW : SW_HIDE);
	m_cmbStdRole.ShowWindow((m_ListType1 == 0 && m_ListType2 ==2)? SW_SHOW : SW_HIDE);
	m_cmbStdLayer.ShowWindow((m_ListType1 == 0 && m_ListType2 ==3)? SW_SHOW : SW_HIDE);

	m_listStdAction.DeleteAllItems();
	while(m_listStdAction.DeleteColumn(0));

	if( m_CurModelName == L"" ) return;
	sModelPack &mp = m_ModelRSrcs[m_CurModelName];
	switch( m_ListType2 )
	{
	case 0: 
		{
			m_listStdAction.InsertColumn(0,L"",0,200);
			m_listStdAction.InsertColumn(1,L"",0,100);
			for(int i=0; i<(int)mp.m_ActionArr.size();++i)
				m_listStdAction.InsertColumn(i+2, mp.m_ActionArr[i].c_str(), 0, 120);
			int nec = (int)mp.m_LayerArr.size();
			for(int i=0; i<(int)mp.m_RoleArr.size();++i)
			{

				for(int j=0; j<nec; ++j)
				{
					m_listStdAction.InsertItem(i*nec+j, j==0?mp.m_RoleArr[i].c_str():L"");
					m_listStdAction.SetItemText(i*nec+j,1,mp.m_LayerArr[j].c_str());
				}
			}
			for(int i=0; i<(int)mp.m_ActionArr.size();++i)for(int j=0;j<(int)mp.m_RoleArr.size();++j)for(int k=0;k<nec;++k)
			{
				if( std::find(mp.m_StdRSrc.begin(), mp.m_StdRSrc.end(), sIndex3(k, i, j)) == mp.m_StdRSrc.end() )
				{
				}
				else
				{
					m_listStdAction.SetItemText(j*nec+k,i+2,L"Ok");
				}
			}
		}
		break;
	case 1: 
		{
			m_listStdAction.InsertColumn(0,L"",0,200);
			for(int i=0; i<(int)mp.m_LayerArr.size();++i)
				m_listStdAction.InsertColumn(i+1, mp.m_LayerArr[i].c_str(), 0, 100);
			for(int i=0; i<(int)mp.m_RoleArr.size();++i)
				m_listStdAction.InsertItem(i, mp.m_RoleArr[i].c_str());

			for(int i=0; i<(int)mp.m_LayerArr.size();++i)for(int j=0; j<(int)mp.m_RoleArr.size(); ++j)
			{
				if( std::find(mp.m_StdRSrc.begin(), mp.m_StdRSrc.end(), sIndex3(i, m_CurIndex.m_IA, j)) == mp.m_StdRSrc.end() )
				{
				}
				else
				{
					m_listStdAction.SetItemText(j,i+1,L"Ok");
				}
			}
		}
		break;
	case 2:  
		{
			m_listStdAction.InsertColumn(0,L"",0,200);
			for(int i=0; i<(int)mp.m_LayerArr.size();++i)
				m_listStdAction.InsertColumn(i+1, mp.m_LayerArr[i].c_str(), 0, 100);
			for(int i=0; i<(int)mp.m_ActionArr.size();++i)
				m_listStdAction.InsertItem(i, mp.m_ActionArr[i].c_str());

			for(int i=0; i<(int)mp.m_LayerArr.size();++i)for(int j=0; j<(int)mp.m_ActionArr.size(); ++j)
			{
				if( std::find(mp.m_StdRSrc.begin(), mp.m_StdRSrc.end(), sIndex3(i, j, m_CurIndex.m_IR)) == mp.m_StdRSrc.end() )
				{
				}
				else
				{
					m_listStdAction.SetItemText(j,i+1,L"Ok");
				}
			}
		}
		break;
	case 3: 
		{
			m_listStdAction.InsertColumn(0,L"",0,200);
			for(int i=0; i<(int)mp.m_ActionArr.size();++i)
				m_listStdAction.InsertColumn(i+1, mp.m_ActionArr[i].c_str(), 0, 120);
			for(int i=0; i<(int)mp.m_RoleArr.size();++i)
				m_listStdAction.InsertItem(i, mp.m_RoleArr[i].c_str());

			for(int i=0; i<(int)mp.m_ActionArr.size();++i)for(int j=0; j<(int)mp.m_RoleArr.size(); ++j)
			{
				if( std::find(mp.m_StdRSrc.begin(), mp.m_StdRSrc.end(), sIndex3(m_CurIndex.m_IL, i, j)) == mp.m_StdRSrc.end() )
				{
				}
				else
				{
					m_listStdAction.SetItemText(j,i+1,L"Ok");
				}
			}
		}
		break;
	default:
		break;
	}


}

void CRSourceListDlg::Refresh()
{

	m_ListType1 = m_tabList.GetCurSel();

	m_listStdAction.ShowWindow(m_ListType1 == 0 ? SW_SHOW : SW_HIDE);
	m_tabStdList.ShowWindow(m_ListType1 == 0 ? SW_SHOW : SW_HIDE);

	m_cmbModelName.ShowWindow(m_ListType1 == 0 ? SW_SHOW : SW_HIDE);
	m_cmbStdAct1.ShowWindow((m_ListType1 == 0 && m_ListType2 ==1)? SW_SHOW : SW_HIDE);
	m_cmbStdRole.ShowWindow((m_ListType1 == 0 && m_ListType2 ==2)? SW_SHOW : SW_HIDE);
	m_cmbStdLayer.ShowWindow((m_ListType1 == 0 && m_ListType2 ==3)? SW_SHOW : SW_HIDE);

	m_listXap.ShowWindow(m_ListType1 == 1 ? SW_SHOW : SW_HIDE);
	GetDlgItem(IDC_EDT_XAPPATH)->ShowWindow(m_ListType1 == 1 ? SW_SHOW : SW_HIDE);
	GetDlgItem(IDC_BTN_SELXAPPATH)->ShowWindow(m_ListType1 == 1 ? SW_SHOW : SW_HIDE);

	switch( m_ListType1 )
	{
	case 0: 
		ShowStdAnimationList();
		break;
	case 1: 
		ShowList();
		break;
	case 2: 
		break;
	default:
		break;
	}
}

void CRSourceListDlg::ChangeModel(const std::wstring &modelname)
{
	if( modelname == L"" || modelname == m_CurModelName ) return;
	sModelPack &mp = m_ModelRSrcs[modelname];
	m_cmbStdLayer.ResetContent();
	for(int i=0; i<(int)mp.m_LayerArr.size(); ++i)
		m_cmbStdLayer.AddString(mp.m_LayerArr[i].c_str());
	m_cmbStdLayer.SetCurSel(0);

	m_cmbStdAct1.ResetContent();
	for(int i=0; i<(int)mp.m_ActionArr.size(); ++i)
		m_cmbStdAct1.AddString(mp.m_ActionArr[i].c_str());
	m_cmbStdAct1.SetCurSel(0);

	m_cmbStdRole.ResetContent();
	for(int i=0; i<(int)mp.m_RoleArr.size(); ++i)
		m_cmbStdRole.AddString(mp.m_RoleArr[i].c_str());
	m_cmbStdRole.SetCurSel(0);
	m_CurModelName = modelname;
}

void CRSourceListDlg::InitStdResource()
{

	if( m_fStdInit ) return;
	m_fStdInit = true;
	m_CurIndex.m_IA = m_CurIndex.m_IL = m_CurIndex.m_IR = 0;

	Nuclear::SpriteManager *pSprMan = m_pDoc->GetSpriteManager();
	std::vector<std::wstring> modelnames;
	pSprMan->DumpModelNames(modelnames);
	int nm = (int)modelnames.size();
	if( nm == 0 ) return;

	for(int ni = 0; ni < nm; ++ni)
	{

		sModelPack &mp = m_ModelRSrcs[modelnames[ni]];

		int nec = pSprMan->GetLayerCount(modelnames[ni]);
		if( nec <=0 ) continue;
		mp.m_LayerArr.resize(nec);
		for(int i=0; i<nec; ++i)
			mp.m_LayerArr[i] = pSprMan->GetLayerName(modelnames[ni], i);


		std::vector<std::pair<std::wstring,int> > v;
		pSprMan->DumpBaseAction(modelnames[ni],v);
		if( v.empty() ) continue;
		mp.m_ActionArr.resize(v.size());
		for(int i=0; i<(int)v.size(); ++i)
			mp.m_ActionArr[i] = v[i].first;


		std::vector<std::wstring> v2;
		for(int i=0; i<nec; ++i)
		{
			pSprMan->DumpComponent(modelnames[ni],i, v2);
			mp.m_RoleArr.insert(mp.m_RoleArr.end(), v2.begin(), v2.end());
		}
		std::sort(mp.m_RoleArr.begin(), mp.m_RoleArr.end());
		mp.m_RoleArr.erase(std::unique(mp.m_RoleArr.begin(), mp.m_RoleArr.end()), mp.m_RoleArr.end());
		if( mp.m_RoleArr.empty() ) continue;


		std::wstring basepath = L"/model/" + modelnames[ni];
		std::wstring nativebase;
		PFS::CEnv::PFSFileNameToNativeFileName(basepath, nativebase);

		for(int i=0; i<nec; ++i)for(int j=0; j<(int)mp.m_RoleArr.size();++j)for(int k=0; k<(int)mp.m_ActionArr.size();++k)
		{
			std::wstring xapname = nativebase + L"\\" + mp.m_LayerArr[i] + L"\\" + mp.m_RoleArr[j] + L"\\" + mp.m_ActionArr[k] + SFI_ExtName;
			FILE *f = Nuclear::FOpen(xapname.c_str(), L"rb");
			if( f ) 
			{
				mp.m_StdRSrc.push_back(sIndex3(i,k,j));
				fclose(f);
			}
		}
		m_cmbModelName.AddString(modelnames[ni].c_str());
	}
	if( m_cmbModelName.GetCount() < 1 ) return;
	m_cmbModelName.SetCurSel(0);
	CString str;
	m_cmbModelName.GetLBText(0,str);

	ChangeModel(std::wstring(str));	

}

void CRSourceListDlg::OnTcnSelchangeTabRsrclist(NMHDR *pNMHDR, LRESULT *pResult)
{
	Refresh();	
	*pResult = 0;
}

BOOL CRSourceListDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_tabList.InsertItem(0, L"标准动作");
	m_tabList.InsertItem(1, L"动画统计");
	m_tabList.InsertItem(2, L"音效还没有");

	m_tabStdList.InsertItem(0, L"总表");
	m_tabStdList.InsertItem(1, L"按动作分类");
	m_tabStdList.InsertItem(2, L"按角色分类");
	m_tabStdList.InsertItem(3, L"按层分类");

	m_listStdAction.SetExtendedStyle(LVS_EX_FULLROWSELECT);
	m_listXap.SetExtendedStyle(LVS_EX_FULLROWSELECT);

	m_fStdInit = false;

	Refresh();

	return TRUE;  

}

BEGIN_MESSAGE_MAP(CRSourceListDlg, CDialog)
	ON_NOTIFY(TCN_SELCHANGE, IDC_TAB_RSRCLIST, &CRSourceListDlg::OnTcnSelchangeTabRsrclist)
	ON_BN_CLICKED(IDC_BTN_SELXAPPATH, &CRSourceListDlg::OnBnClickedBtnSelxappath)
	ON_NOTIFY(TCN_SELCHANGE, IDC_TAB_STDLIST, &CRSourceListDlg::OnTcnSelchangeTabStdlist)
	ON_CBN_SELCHANGE(IDC_CMB_STDLAYER, &CRSourceListDlg::OnCbnSelchangeCmbStdlayer)
	ON_CBN_SELCHANGE(IDC_CMB_STDACT1, &CRSourceListDlg::OnCbnSelchangeCmbStdact1)
	ON_CBN_SELCHANGE(IDC_CMB_STDROLE, &CRSourceListDlg::OnCbnSelchangeCmbStdrole)
	ON_CBN_SELCHANGE(IDC_CMB_RSRCMODELNAME, &CRSourceListDlg::OnCbnSelchangeCmbRsrcmodelname)
END_MESSAGE_MAP()




void CRSourceListDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_TAB_RSRCLIST, m_tabList);
	DDX_Control(pDX, IDC_LST_STDACTION, m_listStdAction);
	DDX_Control(pDX, IDC_LST_XAP, m_listXap);
	DDX_Control(pDX, IDC_TAB_STDLIST, m_tabStdList);
	DDX_Control(pDX, IDC_CMB_STDLAYER, m_cmbStdLayer);
	DDX_Control(pDX, IDC_CMB_STDACT1, m_cmbStdAct1);
	DDX_Control(pDX, IDC_CMB_STDROLE, m_cmbStdRole);
	DDX_Control(pDX, IDC_CMB_RSRCMODELNAME, m_cmbModelName);
}


CRSourceListDlg::~CRSourceListDlg()
{
}

CRSourceListDlg::CRSourceListDlg(CWnd* pParent /*=NULL*/)
: CDialog(CRSourceListDlg::IDD, pParent)
{
	m_ListType1 = 0;
	m_ListType2 = 0;
}

