#pragma once
#include <string>

class CShortcut
{
	CShortcut() {}
public:
	static BOOL Create( LPCTSTR szSrc, LPCTSTR szLink)
	{
		BOOL res = FALSE;
		HRESULT hr = CoInitialize( NULL);
		if( SUCCEEDED(hr))
		{
			IShellLink *pisl = NULL;
			hr = CoCreateInstance( CLSID_ShellLink, NULL, CLSCTX_INPROC_SERVER, IID_IShellLink, (void**)&pisl);
			if( SUCCEEDED(hr))
			{
				pisl->SetPath( szSrc);
				IPersistFile* pIPF = NULL;
				hr = pisl->QueryInterface( IID_IPersistFile, (void**)&pIPF);
				if( SUCCEEDED(hr))
				{
					res = SUCCEEDED( pIPF->Save( szLink, FALSE));
					pIPF->Release();
				}
				pisl->Release();
			}
			CoUninitialize();
		}
		return res;
	}
};

const std::wstring DESKTOP = L"%desktop%";
const std::wstring PRODUCE = L"%product%";
class CSpecialFolder
{
	std::wstring	m_programgroup;
public:
	
	static std::wstring GetSpecialFolder( int nFolder)
	{
		LPITEMIDLIST pidl = NULL;
		LPMALLOC pShellMalloc = NULL;
		std::wstring res;
		wchar_t buf[MAX_PATH] = {0};
		if( SUCCEEDED( SHGetMalloc( &pShellMalloc)))
		{
			if( SUCCEEDED( SHGetSpecialFolderLocation( NULL, nFolder, &pidl)))
			{
				if( SHGetPathFromIDList( pidl, buf))
					res = buf;
				pShellMalloc->Free( pidl);
			}
			pShellMalloc->Release();
		}
		return res;
	}

	void SetProgramGroup( const std::wstring& programgroup) { m_programgroup = programgroup; }
	std::wstring GetFolder( const std::wstring& pos)
	{
		if( pos == DESKTOP)
			return GetSpecialFolder( CSIDL_DESKTOPDIRECTORY);
		else if( pos == PRODUCE)
		{
			std::wstring path = GetSpecialFolder( CSIDL_PROGRAMS);
			path += L"\\" + m_programgroup;
			return path;
		}
		return L"";
	}
};