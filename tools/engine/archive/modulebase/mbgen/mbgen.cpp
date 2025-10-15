#include <stdio.h>
#include <string>
#include <windows.h>
#include <conio.h>
#include <algorithm>

#include "datadef.h"
#include "xmlloader.h"
#include "dataprepare.h"
#include "codeout.h"
#include "outdatas.h"
#include "outnotifies.h"
#include "outstatedefs.h"
#include "outmodules.h"

#ifdef _DEBUG
#pragma comment( lib, "../../lib/zlibstaticd.lib")
#else
#pragma comment( lib, "../../lib/zlibstatic.lib")
#endif
int main( int arc, char* argv[])
{
	try
	{
		wchar_t		wszPath[MAX_PATH];
		wchar_t*	pName;

		if( 0 == GetFullPathNameW( L"modules.xml", MAX_PATH, wszPath, &pName))
		{
			printf( "GetFullPathNameW datapool.xml failed! code = %d\n", GetLastError());
			throw -1;
		}

		DATA::CItemList		itemlist;
		int nError = LOADER::Load( wszPath, itemlist);
		if( nError)
			throw nError;
		DATA::CItemList		itemState, itemModules, itemData, itemNotify;

		if( !DATA::SplitAndSortItemList( itemlist, itemState, itemData, itemNotify, itemModules))
			throw -1;
		
		CreateDirectoryW( L"common", NULL);
		{
			CODEOUT::CFileCodeOut		co( L"common\\states.h");
			CODEOUT::OutStateDefineHPP( itemState, co);
		}
		{
			CODEOUT::CFileCodeOut		co( L"common\\datadefs.h");
			CODEOUT::OutAllDataDefineHPP( itemData, co);
		}
		{
			CODEOUT::CFileCodeOut		co( L"common\\notifies.h");
			CODEOUT::OutNotifyDefineHPP( itemNotify, co);
		}
		
		CODEOUT::OutModuleDefines( itemModules);
		
		{
			std::wstring filename = L"commondef.h";
			DWORD dwAttributes = GetFileAttributesW( filename.c_str());
			if( -1 == dwAttributes)
			{
				CODEOUT::CFileCodeOut		co( filename.c_str());
				co.Print( L"#pragma once\n\n");

				co.Print( L"#include <string>\n");
				co.Print( L"#include <map>\n");
				co.Print( L"#include <set>\n");
				co.Print( L"#include <list>\n");
				co.Print( L"#include <vector>\n");

				co.Print( L"\n\n");

				co.Print( L"#include \"common/states.h\"\n");
				co.Print( L"#include \"common/datadefs.h\"\n");
				co.Print( L"#include \"common/notifies.h\"\n");
				co.Print( L"#include \"common/moduleids.h\"\n");
				co.Print( L"\n\n");
			}
		}
	}
	catch ( int e) 
	{
		printf( "error = %d\n", e);
	}

	printf( "success\n");
	_getch();
	return 0;
}