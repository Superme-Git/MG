#pragma once
#include "..\engine\sound\directsoundmanager.h"

// CAudioFileDialog

class CAudioFileDialog : public CFileDialog
{
	DECLARE_DYNAMIC(CAudioFileDialog)

private:
	Nuclear::DirectSoundManager *m_pDSM;
	Nuclear::SoundHandle m_handle;

public:
	CAudioFileDialog(Nuclear::DirectSoundManager *pDSM, BOOL bOpenFileDialog, // TRUE for FileOpen, FALSE for FileSaveAs
		LPCTSTR lpszDefExt = NULL,
		LPCTSTR lpszFileName = NULL,
		DWORD dwFlags = OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT,
		LPCTSTR lpszFilter = NULL,
		CWnd* pParentWnd = NULL);
	virtual ~CAudioFileDialog();

protected:
	DECLARE_MESSAGE_MAP()
	virtual void OnFileNameChange();
};


