

#include "stdafx.h"
#include "MapEditor.h"
#include "BackgroundInfoDlg.h"




IMPLEMENT_DYNAMIC(CBackgroundInfoDlg, CDialog)

void CBackgroundInfoDlg::OnBnClickedReset()
{
	ResetActiveRect();
}
bool CBackgroundInfoDlg::CheckData(bool checkActiveRect)
{
	UpdateData();
	CString errStr = L"";
	if (info.relSpeed.x < 0)
	{
		errStr += L"����ٶ�X����С��0\n";
	}
	if (info.relSpeed.x > 1)
	{
		errStr += L"����ٶ�X���ܴ���1\n";
	}
	if (info.relSpeed.y < 0)
	{
		errStr += L"����ٶ�Y����С��0\n";
	}
	if (info.relSpeed.y > 1)
	{
		errStr += L"����ٶ�Y���ܴ���1\n";
	}
	if (info.girdWidth == 0)
	{
		errStr += L"  ���ӵĿ�Ȳ���Ϊ0\n";
	}
	if (info.girdWidth > 256 && (info.girdWidth % 256 != 0))
	{
		errStr += L"  ������ӵĿ�ȴ���256���ͱ�����256�ı���\n";
	}
	if (info.girdHeight == 0)
	{
		errStr += L"  ���ӵĸ߶Ȳ���Ϊ0\n";
	}
	if (info.girdHeight > 256 && (info.girdHeight % 256 != 0))
	{
		errStr += L"  ������ӵĸ߶ȴ���256���ͱ�����256�ı���\n";
	}
	if (info.width == 0)
	{
		errStr += L"  �����Ŀ�Ȳ���Ϊ0\n";
	}
	if (info.height == 0)
	{
		errStr += L"  �����ĸ߶Ȳ���Ϊ0\n";
	}

	if (checkActiveRect)
	{
		if (info.activeRect.Width() <= 0)
		{
			errStr += L"  ����������߽���ڵ����ұ߽�\n";
		}
		if (info.activeRect.Height() <= 0)
		{
			errStr += L"  ���������ϱ߽���ڵ����±߽�\n";
		}
	}
	if (errStr.GetLength() > 0)
	{
		errStr = L"�������´���" + errStr;
		MessageBox(errStr);
		return false;
	}
	return true;
}
void CBackgroundInfoDlg::ResetActiveRect()
{
	if (CheckData(false))
	{
		int viewportWidth = 1024;
		int viewportHeight = 768;
		float f;
		int dx = (info.girdWidth * info.width + viewportWidth) / 2;
		int dy = (info.girdHeight * info.height + viewportHeight) / 2;
		f = 1-info.relSpeed.x;
		dx = f<0.0001?dx:static_cast<int>(dx/f);
		f = 1-info.relSpeed.y;
		dy = f<0.0001?dy:static_cast<int>(dy/f);
		info.activeRect.left = info.pos.x - dx;
		info.activeRect.right = info.pos.x + dx;
		info.activeRect.top = info.pos.y - dy;
		info.activeRect.bottom = info.pos.y + dy;
		UpdateData(false);
	}
}

void CBackgroundInfoDlg::OnBnClickedOk()
{

	if (CheckData(true))
	{
		OnOK();
	}
}

BEGIN_MESSAGE_MAP(CBackgroundInfoDlg, CDialog)
	ON_BN_CLICKED(IDOK, &CBackgroundInfoDlg::OnBnClickedOk)
	ON_BN_CLICKED(IDC_RESET, &CBackgroundInfoDlg::OnBnClickedReset)
END_MESSAGE_MAP()

void CBackgroundInfoDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	CString strName;
	if (!pDX->m_bSaveAndValidate)
	{
		strName = info.name.c_str();
	}
	DDX_Text(pDX, IDC_NAME, strName);
	if (pDX->m_bSaveAndValidate)
	{
		info.name = strName;
	}
	DDX_Text(pDX, IDC_POS_X, info.pos.x);
	DDX_Text(pDX, IDC_POS_Y, info.pos.y);
	DDX_Text(pDX, IDC_X_REL_SPEED, info.relSpeed.x);
	DDX_Text(pDX, IDC_Y_REL_SPEED, info.relSpeed.y);
	DDX_Text(pDX, IDC_GIRD_WIDTH, info.girdWidth);
	DDX_Text(pDX, IDC_GIRD_HEIGHT, info.girdHeight);
	DDX_Text(pDX, IDC_WIDTH, info.width);
	DDX_Text(pDX, IDC_HEIGHT, info.height);
	BOOL value;
	value = info.wrapHorz;
	DDX_Check(pDX, IDC_WRAP_HORZ, value);
	info.wrapHorz = value?true:false;
	value = info.wrapVert;
	DDX_Check(pDX, IDC_WRAP_VERT, value);
	info.wrapVert = value?true:false;
	DDX_Text(pDX, IDC_ACTIVE_TOP, info.activeRect.top);
	DDX_Text(pDX, IDC_ACTIVE_BOTTOM, info.activeRect.bottom);
	DDX_Text(pDX, IDC_ACTIVE_LEFT, info.activeRect.left);
	DDX_Text(pDX, IDC_ACTIVE_RIGHT, info.activeRect.right);
}

CBackgroundInfoDlg::~CBackgroundInfoDlg()
{
}

CBackgroundInfoDlg::CBackgroundInfoDlg(CWnd* pParent /*=NULL*/)
: CDialog(CBackgroundInfoDlg::IDD, pParent)
{

}

