#include "StdAfx.h"
#include <string>
#include <vector>
#include <map>
#include <assert.h>
#include "FileIOManager.h"
#include "..\..\pfs\pfs.h"


FileIOManager::FileIOManager(void)
{
	wchar_t workdir[MAX_PATH];
	int nchar = GetModuleFileName(NULL, workdir, MAX_PATH);
	if( 0 ==  nchar )
		return;
	for(int i=nchar-1; i>=0; --i)
	{
		if( workdir[i] == L'/' || workdir[i] == L'\\' )
		{
			workdir[i] = 0;
			break;
		}
		workdir[i] = 0;
	}

	bool r = PFS::CEnv::Mount( L"/bin", workdir, PFS::FST_NATIVE, PFS::MT_READONLY);
}

FileIOManager::~FileIOManager(void)
{
}


bool FileIOManager::ReadPFSFile(const std::wstring &filename, Nuclear::XBuffer &data)
{
	//OutputDebugString(filename.data());
	//OutputDebugString(L"\n");
	PFS::CFile file;			
	if( !file.Open(filename, PFS::FM_EXCL, PFS::FA_RDONLY ))
	{
		return false;
	}

	PFS::CImageData imgdata = file.GetImage();
	if( imgdata.GetSize() == 0 ) return false;
	data.replace(imgdata.GetData(), (size_t)imgdata.GetSize());
	return true;
}