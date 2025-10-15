// DialogSetPlaySpeed.cpp : 实现文件
//

#include "stdafx.h"
#include "ImageEditor.h"
#include "DialogSetPlaySpeed.h"


// CDialogSetPlaySpeed 对话框

IMPLEMENT_DYNAMIC(CDialogSetPlaySpeed, CDialog)

CDialogSetPlaySpeed::CDialogSetPlaySpeed(CWnd* pParent /*=NULL*/)
	: CDialog(CDialogSetPlaySpeed::IDD, pParent)
	, m_nFPS(5)
{

}

CDialogSetPlaySpeed::~CDialogSetPlaySpeed()
{
}

void CDialogSetPlaySpeed::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDIT1, m_nFPS);
	DDV_MinMaxInt(pDX, m_nFPS, 0, 100);
}

BEGIN_MESSAGE_MAP(CDialogSetPlaySpeed, CDialog)
END_MESSAGE_MAP()


void CDialogSetPlaySpeed::SetFPS(int fps)
{
	m_nFPS = fps;
}

int CDialogSetPlaySpeed::GetFPS()
{
	return m_nFPS;
}

// CDialogSetPlaySpeed 消息处理程序
