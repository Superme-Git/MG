// AudioFileDialog.cpp : implementation file
//

#include "stdafx.h"
#include "SoundEditor.h"
#include "SoundEditorDoc.h"
#include "AudioFileDialog.h"


// CAudioFileDialog

IMPLEMENT_DYNAMIC(CAudioFileDialog, CFileDialog)

CAudioFileDialog::CAudioFileDialog(Nuclear::DirectSoundManager *pDSM, BOOL bOpenFileDialog, LPCTSTR lpszDefExt, LPCTSTR lpszFileName,
		DWORD dwFlags, LPCTSTR lpszFilter, CWnd* pParentWnd) :
CFileDialog(bOpenFileDialog, lpszDefExt, lpszFileName, dwFlags, lpszFilter, pParentWnd), m_pDSM(pDSM)
, m_handle(Nuclear::INVALID_SOUND_HANDLE)
{

}

CAudioFileDialog::~CAudioFileDialog()
{
	if (m_handle != Nuclear::INVALID_SOUND_HANDLE)
	{
		m_pDSM->StopSound(m_handle);
	}
}


BEGIN_MESSAGE_MAP(CAudioFileDialog, CFileDialog)
END_MESSAGE_MAP()



// CAudioFileDialog message handlers



void CAudioFileDialog::OnFileNameChange()
{
	// TODO: Add your specialized code here and/or call the base class
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
