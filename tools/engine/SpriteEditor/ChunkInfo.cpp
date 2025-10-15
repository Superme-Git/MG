

#include "stdafx.h"
#include "SpriteEditor.h"
#include "ChunkInfo.h"




IMPLEMENT_DYNAMIC(CChunkInfo, CDialog)

BOOL CChunkInfo::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_BlockInfoTip.SetWindowText(m_BlockInfoStr.c_str());

	return TRUE;  

}

BEGIN_MESSAGE_MAP(CChunkInfo, CDialog)
END_MESSAGE_MAP()

void CChunkInfo::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_BLOCK_INFO_TIP, m_BlockInfoTip);
	DDX_Text(pDX, IDC_EDIT_WIDTH, m_Width);
	DDX_Text(pDX, IDC_EDIT_HEIGHT, m_Height);
}

CChunkInfo::~CChunkInfo()
{
}

CChunkInfo::CChunkInfo(const std::wstring &modelname, const std::wstring &actionName)
: CDialog(CChunkInfo::IDD, NULL)
, m_Width(0)
, m_Height(0)
{
	m_BlockInfoStr = L"û���ҵ� " + modelname + L" ģ�͵� " + actionName + L" �������ϰ�ͼ�ļ�������������Ϣ���Ը���Ϊ��λ��";
}

