

#include "stdafx.h"
#include "SpriteEditor.h"
#include "AnimationFilenameDlg.h"




IMPLEMENT_DYNAMIC(CAnimationFilenameDlg, CDialog)

BEGIN_MESSAGE_MAP(CAnimationFilenameDlg, CDialog)
END_MESSAGE_MAP()

void CAnimationFilenameDlg::GetAnimationFilename(std::wstring &name)
{
	name = m_AniFilename.GetString();
}

void CAnimationFilenameDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX,IDC_ANIMFILE_NAME, m_AniFilename);
}

CAnimationFilenameDlg::~CAnimationFilenameDlg()
{
}

CAnimationFilenameDlg::CAnimationFilenameDlg(CWnd* pParent )
: CDialog(CAnimationFilenameDlg::IDD, pParent)
{

}
