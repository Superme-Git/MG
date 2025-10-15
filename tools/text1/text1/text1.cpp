// text1.cpp : �������̨Ӧ�ó������ڵ㡣
//

#include "stdafx.h"
#include <vector>
#include "text1.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


// Ψһ��Ӧ�ó������

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
		// ��ʼ�� MFC ����ʧ��ʱ��ʾ����
		if (!AfxWinInit(hModule, NULL, ::GetCommandLine(), 0))
		{
			// TODO:  ���Ĵ�������Է���������Ҫ
			_tprintf(_T("����:  MFC ��ʼ��ʧ��\n"));
			nRetCode = 1;
		}
		else
		{
			// TODO:  �ڴ˴�ΪӦ�ó������Ϊ��д���롣
		}
	}
	else
	{
		// TODO:  ���Ĵ�������Է���������Ҫ
		_tprintf(_T("����:  GetModuleHandle ʧ��\n"));
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
