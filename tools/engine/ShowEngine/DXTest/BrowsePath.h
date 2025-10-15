#pragma once

#include <string>

class CBrowsePath
{
	std::wstring	m_inipath;		// ��ʼѡ��Ŀ¼
	std::wstring	m_selectpath;	// ����ѡ��Ŀ¼
	HWND			m_hWnd;
public:
	explicit CBrowsePath( HWND hWnd) : m_hWnd( hWnd) {}

	void SetIniPath( const std::wstring& path) { m_inipath = path; }
	const std::wstring& GetSelectPath() { return m_selectpath; }

	//	��ʼ���ļ����趨�õĻص�����
	static int CALLBACK _SHBrowseForFolderCallbackProc(HWND hwnd, UINT uMsg, LPARAM lParam, LPARAM lpData)
	{
		if( uMsg == BFFM_INITIALIZED)
			::SendMessage( hwnd, BFFM_SETSELECTION, TRUE, lpData);
		else if( uMsg == BFFM_SELCHANGED)
		{
			wchar_t szPath[MAX_PATH] = {0};
			SHGetPathFromIDList( reinterpret_cast<LPITEMIDLIST>(lParam), szPath);
			::SendMessage( hwnd, BFFM_SETSTATUSTEXT, TRUE, (LPARAM)szPath);
		}
		return	0;
	}

	bool DoModal()
	{
		bool res = false;
		wchar_t szPath[MAX_PATH]={0};
		BROWSEINFO m_bi;
		m_bi.ulFlags = BIF_RETURNONLYFSDIRS  | BIF_STATUSTEXT;
		m_bi.hwndOwner = m_hWnd;
		m_bi.pidlRoot = NULL;
		m_bi.lpszTitle = L"Please select path:";
		m_bi.lpfn = _SHBrowseForFolderCallbackProc;
		m_bi.lParam = (LPARAM)m_inipath.c_str();
		m_bi.pszDisplayName = szPath;
		LPITEMIDLIST pidl = ::SHBrowseForFolder( &m_bi );
		if ( pidl )
		{
			if (::SHGetPathFromIDList ( pidl, szPath ) )
			{
				m_selectpath = szPath;
				res = true;
			}
			IMalloc * pMalloc = NULL;
			if ( SUCCEEDED ( ::SHGetMalloc( &pMalloc ) ) )  // ȡ��IMalloc�������ӿ�
			{
				pMalloc->Free( pidl );    // �ͷ��ڴ�
				pMalloc->Release();       // �ͷŽӿ�
			}
		}
		return res;
	}
};