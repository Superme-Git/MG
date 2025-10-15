#include <stdio.h>
#include <windows.h>
#include <conio.h>

#include "datadef.h"
#include "xmlloader.h"
#include "dataprepare.h"
#include "codeout.h"
#include "outdatadef.h"
#include "outdatainstance.h"

#ifdef _DEBUG
#pragma comment( lib, "../../lib/zlibstaticd.lib")
#else
#pragma comment( lib, "../../lib/zlibstatic.lib")
#endif

int main( int argc, char* argv[])
{
	try
	{
		DATA::CItemList			itemlist;
		DATA::CGroupList		grouplist;

		wchar_t		wszPath[MAX_PATH];
		wchar_t*	pName;

		if( 0 == GetFullPathNameW( L"datapool.xml", MAX_PATH, wszPath, &pName))
		{
			printf( "GetFullPathNameW datapool.xml failed! code = %d\n", GetLastError());
			throw -1;
		}
		int nError = LOADER::Load( wszPath, itemlist, grouplist);
		if( nError)
		{
			printf( "Load datapool.xml failed! code = %d\n", nError);
			throw -1;
		}

		DATA::CItemList			itemData, itemNotify;
		bool b = DATA::SplitAndSortItemList( itemlist, itemData, itemNotify);
		if( !b)
			throw -1;
		b = DATA::SortGroupList( grouplist);
		if( !b)
			throw -1;

		const wchar_t* const FILE_NAME_DATA_DEFS = L"datadefs.h";
		const wchar_t* const FILE_NAME_NOTIFY_DEFS = L"notifydefs.h";
		const wchar_t* const FILE_NAME_NOTIFY_OBJECTS = L"notifyobjects.h";
		const wchar_t* const FILE_NAME_OBJECT_INSTANCE = L"objectinstance.hpp";

		{
			CODEOUT::CFileCodeOut		codeout( FILE_NAME_DATA_DEFS);
			CODEOUT::OutDataDefineHPP( itemData, codeout);
		}

		{
			CODEOUT::CFileCodeOut		codeout( FILE_NAME_NOTIFY_DEFS);
			CODEOUT::OutNotifyDefineHPP( itemNotify, codeout);
		}
		{
			CODEOUT::CFileCodeOut		codeout( FILE_NAME_NOTIFY_OBJECTS);
			CODEOUT::OutNotifyGetInstanceHPP( itemNotify, codeout, FILE_NAME_DATA_DEFS, FILE_NAME_NOTIFY_DEFS);
		}
		{
			CODEOUT::CFileCodeOut		codeout( FILE_NAME_OBJECT_INSTANCE);
			CODEOUT::OutDataInstanceHPP( itemNotify, grouplist, codeout);
		}
		printf( "success!\n");
	}
	catch( int&) 
	{
	}
	_getch();
	return 0;
}