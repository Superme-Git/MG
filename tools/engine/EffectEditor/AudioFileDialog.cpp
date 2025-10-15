

#include "stdafx.h"
#include "..\engine\common\pfsutil.h"
#include "EffectEditor.h"
#include "AudioFileDialog.h"




IMPLEMENT_DYNAMIC(CAudioFileDialog, CFileDialog)

void CAudioFileDialog::OnFileNameChange()
{
	if (m_pDSM)
	{
		CString url = GetPathName();
		if (url.GetLength() > 0)
		{
			std::wstring name;
			if (PFS::CEnv::NativeFileNameToPFSFileName(url.GetBuffer(), name))
			{
				if (m_handle != Nuclear::INVALID_SOUND_HANDLE)
				{
					m_pDSM->StopSound(m_handle);
				}
				m_handle = m_pDSM->XPPlaySound(name, 0, 0, 255, 128, false);
			}
		}
	}
	CFileDialog::OnFileNameChange();
}
BEGIN_MESSAGE_MAP(CAudioFileDialog, CFileDialog)
END_MESSAGE_MAP()







CAudioFileDialog::~CAudioFileDialog()
{
	if (m_handle != Nuclear::INVALID_SOUND_HANDLE)
	{
		m_pDSM->StopSound(m_handle);
	}
}


CAudioFileDialog::CAudioFileDialog(Nuclear::DirectSoundManager *pDSM, BOOL bOpenFileDialog, LPCTSTR lpszDefExt, LPCTSTR lpszFileName,
								   DWORD dwFlags, LPCTSTR lpszFilter, CWnd* pParentWnd) :
CFileDialog(bOpenFileDialog, lpszDefExt, lpszFileName, dwFlags, lpszFilter, pParentWnd), m_pDSM(pDSM)
, m_handle(Nuclear::INVALID_SOUND_HANDLE)
{

}

