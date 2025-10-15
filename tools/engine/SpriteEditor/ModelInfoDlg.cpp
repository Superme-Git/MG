


#include "stdafx.h"
#include "SpriteEditor.h"
#include "ModelInfoDlg.h"

#include <set>
#include <algorithm>



IMPLEMENT_DYNAMIC(CModelInfoDlg, CDialog)

void CModelInfoDlg::OnOK()
{
	UpdateData();
	if( m_bNew )
	{
		if( m_ModelName == L"" || m_ModelDes == L"")
		{
			AfxMessageBox(L"模型名称和描述不可为空");
			return;
		}



		if( std::find(m_ModelNames.begin(), m_ModelNames.end(), std::wstring(m_ModelName)) != m_ModelNames.end())
		{
			AfxMessageBox(L"模型名称已经存在，换个吧");
			return;
		}

		m_LayerCount = m_cmbLayerCount.GetCurSel() + 1;




		std::set<std::wstring> tset;
		for(int i=0; i<m_LayerCount;++i)
		{
			if( m_LayerNameArray[i] == L"" || m_LayerDesArray[i] == L"")
			{
				AfxMessageBox(L"层名称和描述不可为空");
				return;
			}
			std::wstring mname(m_LayerNameArray[i]);
			if( tset.find(mname) != tset.end() )
			{
				AfxMessageBox(L"层名称不可重复");
				return;
			}
			tset.insert(mname);
		}
	} else {
		if( m_ModelDes == L"")
		{
			AfxMessageBox(L"模型描述不可为空");
			return;
		}

		m_LayerCount = m_cmbLayerCount.GetCurSel() + 1;
		int tmpLayerMaxId = m_LayerCount - 1;

		std::set<std::wstring> tset;
		for(int i=0; i<m_LayerCount;++i)
		{
			std::wstring mname(m_LayerNameArray[i]);
			if( tset.find(mname) != tset.end() )
			{
				AfxMessageBox(L"层名称不可重复(包括删除了的层)，可以先删除然后再重新添加");
				return;
			}

			tset.insert(mname);

			if (m_DelArray[i])
			{
				if (++tmpLayerMaxId >= Nuclear::XPSC_MAXCOUNT)
				{
					CString err;
					err.Format(L"删除太多的层了吧？");
					AfxMessageBox(err);
				}
				continue;
			}
			if( m_LayerDesArray[i] == L"")
			{
				AfxMessageBox(L"层描述不可为空");
				return;
			}
		}
	}
	CDialog::OnOK();
}

BOOL CModelInfoDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	for(int i = 0; i < Nuclear::XPSC_MAXCOUNT; ++i)
	{
		CString cs;
		cs.Format(L"%d", (i + 1));
		m_cmbLayerCount.AddString(cs);
	}

	m_cmbLayerCount.SetCurSel(m_LayerCount-1);

#define SETITEM(aa) if (m_LayerCount >= aa) {\
	GetDlgItem(IDC_EDT_MODELLAYERNAME##aa)->EnableWindow(FALSE);\
	GetDlgItem(IDC_CHK_MODELLAYERDEL##aa)->ShowWindow(SW_SHOW);\
	GetDlgItem(IDC_CHECK_EFFECT##aa)->ShowWindow(SW_SHOW);\
	GetDlgItem(IDC_CHECK_RIDE##aa)->ShowWindow(SW_SHOW);}

	if ( !m_bNew )
	{
		GetDlgItem(IDC_EDT_MODELNAME)->EnableWindow(FALSE);

		SETITEM(1);
		SETITEM(2);
		SETITEM(3);
		SETITEM(4);
		SETITEM(5);
		SETITEM(6);
		SETITEM(7);
		SETITEM(8);
		SETITEM(9);
		SETITEM(10);
		SETITEM(11);
		SETITEM(12);
	}

	return TRUE;  

}

void CModelInfoDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);

	DDX_Text(pDX, IDC_EDT_MODELNAME, m_ModelName);
	DDX_Text(pDX, IDC_EDT_MODELDES, m_ModelDes);
	DDX_Control(pDX, IDC_CMB_MODELLAYERCOUNT, m_cmbLayerCount);

	DDX_Text(pDX, IDC_EDT_MODELLAYERNAME1, m_LayerNameArray[0]);
	DDX_Text(pDX, IDC_EDT_MODELLAYERDES1, m_LayerDesArray[0]);
	DDX_Check(pDX, IDC_CHECK_RIDE1, m_bIsRideArray[0]);
	DDX_Check(pDX, IDC_CHECK_EFFECT1, m_bIsEffectArray[0]);
	DDX_Check(pDX, IDC_CHK_MODELLAYERDEL1, m_DelArray[0]);

	DDX_Text(pDX, IDC_EDT_MODELLAYERNAME2, m_LayerNameArray[1]);
	DDX_Text(pDX, IDC_EDT_MODELLAYERDES2, m_LayerDesArray[1]);
	DDX_Check(pDX, IDC_CHECK_RIDE2, m_bIsRideArray[1]);
	DDX_Check(pDX, IDC_CHECK_EFFECT2, m_bIsEffectArray[1]);
	DDX_Check(pDX, IDC_CHK_MODELLAYERDEL2, m_DelArray[1]);

	DDX_Text(pDX, IDC_EDT_MODELLAYERNAME3, m_LayerNameArray[2]);
	DDX_Text(pDX, IDC_EDT_MODELLAYERDES3, m_LayerDesArray[2]);
	DDX_Check(pDX, IDC_CHECK_RIDE3, m_bIsRideArray[2]);
	DDX_Check(pDX, IDC_CHECK_EFFECT3, m_bIsEffectArray[2]);
	DDX_Check(pDX, IDC_CHK_MODELLAYERDEL3, m_DelArray[2]);

	DDX_Text(pDX, IDC_EDT_MODELLAYERNAME4, m_LayerNameArray[3]);
	DDX_Text(pDX, IDC_EDT_MODELLAYERDES4, m_LayerDesArray[3]);
	DDX_Check(pDX, IDC_CHECK_RIDE4, m_bIsRideArray[3]);
	DDX_Check(pDX, IDC_CHECK_EFFECT4, m_bIsEffectArray[3]);
	DDX_Check(pDX, IDC_CHK_MODELLAYERDEL4, m_DelArray[3]);

	DDX_Text(pDX, IDC_EDT_MODELLAYERNAME5, m_LayerNameArray[4]);
	DDX_Text(pDX, IDC_EDT_MODELLAYERDES5, m_LayerDesArray[4]);
	DDX_Check(pDX, IDC_CHECK_RIDE5, m_bIsRideArray[4]);
	DDX_Check(pDX, IDC_CHECK_EFFECT5, m_bIsEffectArray[4]);
	DDX_Check(pDX, IDC_CHK_MODELLAYERDEL5, m_DelArray[4]);

	DDX_Text(pDX, IDC_EDT_MODELLAYERNAME6, m_LayerNameArray[5]);
	DDX_Text(pDX, IDC_EDT_MODELLAYERDES6, m_LayerDesArray[5]);
	DDX_Check(pDX, IDC_CHECK_RIDE6, m_bIsRideArray[5]);
	DDX_Check(pDX, IDC_CHECK_EFFECT6, m_bIsEffectArray[5]);
	DDX_Check(pDX, IDC_CHK_MODELLAYERDEL6, m_DelArray[5]);

	DDX_Text(pDX, IDC_EDT_MODELLAYERNAME7, m_LayerNameArray[6]);
	DDX_Text(pDX, IDC_EDT_MODELLAYERDES7, m_LayerDesArray[6]);
	DDX_Check(pDX, IDC_CHECK_RIDE7, m_bIsRideArray[6]);
	DDX_Check(pDX, IDC_CHECK_EFFECT7, m_bIsEffectArray[6]);
	DDX_Check(pDX, IDC_CHK_MODELLAYERDEL7, m_DelArray[6]);

	DDX_Text(pDX, IDC_EDT_MODELLAYERNAME8, m_LayerNameArray[7]);
	DDX_Text(pDX, IDC_EDT_MODELLAYERDES8, m_LayerDesArray[7]);
	DDX_Check(pDX, IDC_CHECK_RIDE8, m_bIsRideArray[7]);
	DDX_Check(pDX, IDC_CHECK_EFFECT8, m_bIsEffectArray[7]);
	DDX_Check(pDX, IDC_CHK_MODELLAYERDEL8, m_DelArray[7]);

	DDX_Text(pDX, IDC_EDT_MODELLAYERNAME9, m_LayerNameArray[8]);
	DDX_Text(pDX, IDC_EDT_MODELLAYERDES9, m_LayerDesArray[8]);
	DDX_Check(pDX, IDC_CHECK_RIDE9, m_bIsRideArray[8]);
	DDX_Check(pDX, IDC_CHECK_EFFECT9, m_bIsEffectArray[8]);
	DDX_Check(pDX, IDC_CHK_MODELLAYERDEL9, m_DelArray[8]);

	DDX_Text(pDX, IDC_EDT_MODELLAYERNAME10, m_LayerNameArray[9]);
	DDX_Text(pDX, IDC_EDT_MODELLAYERDES10, m_LayerDesArray[9]);
	DDX_Check(pDX, IDC_CHECK_RIDE10, m_bIsRideArray[9]);
	DDX_Check(pDX, IDC_CHECK_EFFECT10, m_bIsEffectArray[9]);
	DDX_Check(pDX, IDC_CHK_MODELLAYERDEL10, m_DelArray[9]);

	DDX_Text(pDX, IDC_EDT_MODELLAYERNAME11, m_LayerNameArray[10]);
	DDX_Text(pDX, IDC_EDT_MODELLAYERDES11, m_LayerDesArray[10]);
	DDX_Check(pDX, IDC_CHECK_RIDE11, m_bIsRideArray[10]);
	DDX_Check(pDX, IDC_CHECK_EFFECT11, m_bIsEffectArray[10]);
	DDX_Check(pDX, IDC_CHK_MODELLAYERDEL11, m_DelArray[10]);

	DDX_Text(pDX, IDC_EDT_MODELLAYERNAME12, m_LayerNameArray[11]);
	DDX_Text(pDX, IDC_EDT_MODELLAYERDES12, m_LayerDesArray[11]);
	DDX_Check(pDX, IDC_CHECK_RIDE12, m_bIsRideArray[11]);
	DDX_Check(pDX, IDC_CHECK_EFFECT12, m_bIsEffectArray[11]);
	DDX_Check(pDX, IDC_CHK_MODELLAYERDEL12, m_DelArray[11]);
}

CModelInfoDlg::~CModelInfoDlg()
{
}

CModelInfoDlg::CModelInfoDlg(CWnd* pParent /*=NULL*/)
: CDialog(CModelInfoDlg::IDD, pParent)
, m_ModelName(_T(""))
, m_ModelDes(_T(""))

{
	m_bNew = false;
	m_LayerCount = 0;
	for(int i=0; i<Nuclear::XPSC_MAXCOUNT;++i)
	{
		m_bIsRideArray[i] = FALSE;
		m_bIsEffectArray[i] = FALSE;
		m_DelArray[i] = FALSE;
	}
}

