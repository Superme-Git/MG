// text1.cpp : 定义控制台应用程序的入口点。
//

#include "stdafx.h"
#include <vector>
#include "text1.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// 唯一的应用程序对象

CWinApp theApp;

using namespace std;

void myGet(CString& path, std::vector<CString>& vsTypes);
void myChange(CString& path, std::vector<CString>& vsTypes);
void myGetDir(CString& path, std::vector<CString>& vsTypes);
void myPaiBan(CString& path, std::vector<CString>& vsTypes);
void myFindTableId(CString& path, std::vector<CString>& vsTypes);
void myMakeProtol(const char* inputFileName, const char* outputFileName);
void Command_GetCommented(CString& path, std::vector<CString>& vsTypes);
void Command_GetString(CString& path, std::vector<CString>& vsTypes);
void Command_FindText(CString& path, std::vector<CString>& vsTypes);
void Command_CompareDirection(CString& path, std::vector<CString>& vsTypes);
void Command_SplitWord(CString& path, std::vector<CString>& vsTypes);
void Command_SplitFile(CString& path, std::vector<CString>& vsTypes);

int _tmain(int argc, TCHAR* argv[], TCHAR* envp[])
{
	int nRetCode = 0;

	HMODULE hModule = ::GetModuleHandle(NULL);

	if (hModule != NULL)
	{
		// 初始化 MFC 并在失败时显示错误
		if (!AfxWinInit(hModule, NULL, ::GetCommandLine(), 0))
		{
			// TODO:  更改错误代码以符合您的需要
			_tprintf(_T("错误:  MFC 初始化失败\n"));
			nRetCode = 1;
		}
		else
		{
			// TODO:  在此处为应用程序的行为编写代码。
		}
	}
	else
	{
		// TODO:  更改错误代码以符合您的需要
		_tprintf(_T("错误:  GetModuleHandle 失败\n"));
		nRetCode = 1;
	}

	if (argc >= 3)
	{
		CString path = argv[2];
		std::vector<CString> vsTypes;
		for (int i = 3; i < argc; i++)
		{
			vsTypes.push_back(argv[i]);
		}

		if (!wcscmp(argv[1], L"FindWord"))
		{
			myGet(path,vsTypes);
			return nRetCode;
		}
		else if(!wcscmp(argv[1], L"push"))
		{
			myChange(path, vsTypes);
			return nRetCode;
		}
		else if (!wcscmp(argv[1], L"get dir"))
		{
			myGetDir(path, vsTypes);
		}
		else if (!wcscmp(argv[1], L"push dir"))
		{
			
		}
		else if (!wcscmp(argv[1], L"paiban"))
		{
			myPaiBan(path, vsTypes);
		}
		else if (!wcscmp(argv[1], L"FindTableIds"))
		{
			myFindTableId(path, vsTypes);
		}
		else if (!wcscmp(argv[1], L"MakeProtol"))
		{
			//myMakeProtol(argv[1],argv[2]);
		}
		else if (!wcscmp(argv[1], L"GetCommented"))
		{
			Command_GetCommented(path, vsTypes);
		}
		else if (!wcscmp(argv[1], L"GetString"))
		{
			Command_GetString(path, vsTypes);
		}
		else if (!wcscmp(argv[1], L"FindText"))
		{
			Command_FindText(path, vsTypes);
		}
		else if (!wcscmp(argv[1], L"CompareDirection"))
		{
			Command_CompareDirection(path,vsTypes);
		}
		else if (!wcscmp(argv[1], L"SplitWord"))
		{
			Command_SplitWord(path, vsTypes);
		}
		else if (!wcscmp(argv[1], L"SplitFile"))
		{
			Command_SplitFile(path, vsTypes);
		}
	}
	else
	{
	}

	printf("param1 = get or push; param2=scanf path; param3,param4 ...  ... = ext names\n");

	return nRetCode;
}
