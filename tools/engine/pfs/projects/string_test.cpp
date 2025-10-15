#include <windows.h>
#include <shlwapi.h>
#pragma comment( lib, "shlwapi.lib")
#include <assert.h>
#include <string>
#include <iostream>
#include <sstream>
#include <iomanip>

namespace STR_TEST{
		
	std::wostream& OutputStringNoLB(std::wostream& os, LPCWSTR lpszMessage)
	{
		std::wostringstream oss;
		int len3 = wcslen (lpszMessage );
		for ( int i = 0; i < len3; i ++ )
		{
			if ( lpszMessage[i] == L'\r' )
			{
				oss<< L"\\r";
			}
			else if ( lpszMessage[i] == L'\n')
			{
				oss<< L"\\n";
			}
			else
			{
				oss<< lpszMessage[i];
			}
		}

		os << oss.str();
		return os;
	}

	//
	// ��lpszMessage�Ļ��з����й淶��
	//
	// �Ե�����\r��\n��ת����\r\n��(PC��׼)
	//
	// ���lplpszMessage2ΪNULL,��ôֻ��lpszMessage���м��
	// ���������Ҫ�淶����*lplpszMessage2ΪlpszMessage�淶���Ľ��
	// 
	// ����ֵΪ�Ƿ���Ҫ�淶�������Ƿ�������lpszMessage�����˹淶������
	// ��������淶�����ַ�����*lplpszMessage2��ΪNULL��
	// ������淶�����ַ��������lplpszMessage2��ΪNULL��*lplpszMessage2 Ϊ�淶�����
	// ʹ��LocalFree�ͷ�֮.
	//
	BOOL CanonicalizeLineFeeds( LPCWSTR lpszMessage, LPCWSTR *lplpszMessage2 )
	{
		assert( lpszMessage != NULL);
		int len = (int)wcslen( lpszMessage );
		if( len <= 0 )
		{
			return FALSE;
		}

		BOOL bNeedWellform = FALSE;
		int pos = 0;
		int len2 = len;
		for( LPCWSTR lpPtr = lpszMessage; lpPtr[0]; lpPtr += pos, len2 -= pos)
		{
			pos = wcscspn( lpPtr, L"\r\n");
			if( pos >= len2 )
			{
				break;
			}

			bNeedWellform = lpPtr[pos] != L'\r' || lpPtr[pos+1] != L'\n';
			if ( bNeedWellform )
			{
				break;
			}
			else
			{
				pos += 2;
			}
		}

		if( bNeedWellform && lplpszMessage2 != NULL )
		{
			int maxneed = len*2+2-pos;
			LPWSTR lpszMessage2 = (LPWSTR)LocalAlloc( LPTR, maxneed * 2);
			wcsncpy_s( lpszMessage2, maxneed, lpszMessage, pos );

			wchar_t c_curr = 0, c_last = 0;
			int iDes = pos;
			int iSrc = pos;
			for ( ; iSrc <= len; iSrc ++ )
			{
				c_curr = lpszMessage[iSrc];

				if ( c_curr == L'\n' )
				{
					if ( c_last != L'\r' )
						lpszMessage2[iDes++] = L'\r';
				}
				else if( c_last == L'\r' )
				{
					if( c_curr != L'\n' )
						lpszMessage2[iDes++] = L'\n';
				}
				lpszMessage2[iDes++] = c_curr;
				c_last = c_curr;
			}
			
			*lplpszMessage2 = lpszMessage2;
		}

		return bNeedWellform;
	}

	void StrTest( LPCWSTR lpszMessage )
	{
		std::wcout<< std::setw(20);
		OutputStringNoLB(std::wcout, lpszMessage) << L" ------ ";

		LPCWSTR lpszMessage2 = NULL;
		BOOL bNeedWellform = CanonicalizeLineFeeds( lpszMessage, &lpszMessage2 );
		
		std::wcout<< std::setw(30);
		
		if( bNeedWellform )
		{
			OutputStringNoLB(std::wcout, lpszMessage2 );
			LocalFree( (HLOCAL)lpszMessage2 );
		}
		else
		{
			OutputStringNoLB(std::wcout, lpszMessage );
			std::wcout<<  L" [NO] "; 
		}

		std::wcout<< std::endl;
	}

	void StringTests()
	{
		StrTest( L"");
		StrTest( L"abcd");
		
		StrTest( L"\r");
		StrTest( L"\n");

		StrTest( L"\rabcd");
		StrTest( L"\nabcd");

		StrTest( L"a\rbcd");
		StrTest( L"a\nbcd");

		StrTest( L"abcd\r");
		StrTest( L"abcd\n");

		StrTest( L"\ra\rbcd");
		StrTest( L"\na\nbcd");

		StrTest( L"a\rbc\nd");
		StrTest( L"a\nbc\nd");

		StrTest( L"\r\r");
		StrTest( L"\n\r");
		StrTest( L"\r\n");
		StrTest( L"\n\n");

		StrTest( L"\r\rabcd");
		StrTest( L"\n\rabcd");
		StrTest( L"\r\nabcd");
		StrTest( L"\n\nabcd");

		StrTest( L"ab\r\rcd");
		StrTest( L"ab\n\rcd");
		StrTest( L"ab\r\ncd");
		StrTest( L"ab\n\ncd");
		StrTest( L"abcd\r\r");
		StrTest( L"abcd\n\r");
		StrTest( L"abcd\r\n");
		StrTest( L"abcd\n\n");

		StrTest( L"\r\r\r");
		StrTest( L"\n\r\n");
		StrTest( L"\r\n\r");
		StrTest( L"\n\n\n");

		StrTest( L"\r\r\r");
		StrTest( L"\n\n\r");
		StrTest( L"\r\r\n");
		StrTest( L"\n\n\n");


		StrTest( L"\r\n\r\n");

		StrTest( L"\r\n\r\n  \r\n\r\n");
		StrTest( L"\r\n\r\n  \r\n \r\n");

		StrTest( L"\r\n\r\n  \r\n \r \n");
	}



	int ReplacePathVariables( std::wstring& path )
	{
		static LPCWSTR rgPathVariables[] = 
		{
			L"%module-dir%", // ���������ڵ�Ŀ¼
			L"%current-dir%", // ����ǰĿ¼
		};

		WCHAR buffer[MAX_PATH+1] = {0};
		int occurrence = 0;
		size_t pos = std::wstring::npos;
		for( int i = 0; i < _countof( rgPathVariables); i ++ )
		{
			LPCWSTR szModuleDirVar = rgPathVariables[i];
			if( (pos = path.find(szModuleDirVar)) != std::wstring::npos )
			{
				if( wcscmp( szModuleDirVar, L"%module-dir%" ) == 0 )
				{
					::GetModuleFileNameW( NULL, buffer, MAX_PATH);
					::PathRemoveFileSpecW( buffer );
					path.replace( pos, wcslen(szModuleDirVar), buffer );
					occurrence ++;
				}
				else if( wcscmp( szModuleDirVar, L"%current-dir%" ) == 0 )
				{
					::GetCurrentDirectoryW( MAX_PATH, buffer );
					::PathRemoveBackslashW( buffer );
					path.replace( pos, wcslen(szModuleDirVar), buffer );
					occurrence ++;
				}
			}
		}

		// ֻ�е��滻һ������ʱ��ִ��·���淶������
		if( occurrence == 1 )
		{
			WCHAR buffer[MAX_PATH+1] ={0};
			::PathCanonicalizeW( buffer, path.c_str() );
			path = buffer;
		}

		return occurrence;
	}

	void Test()
	{
		int occurrence = 0;
		std::wstring str = L"%module-dir%";
		occurrence = ReplacePathVariables( str );
		assert(occurrence  == 1);
		std::wcout<< str << std::endl;


		str = L"%current-dir%";
		occurrence = ReplacePathVariables( str );
		assert(occurrence  == 1);
		std::wcout<< str << std::endl;


		str = L"%current-dir%\\..";
		occurrence = ReplacePathVariables( str );
		assert(occurrence  == 1);
		std::wcout<< str << std::endl;


		str = L"%module-dir%;%current-dir%";
		occurrence = ReplacePathVariables( str );
		assert(occurrence  == 2);
		std::wcout<< str << std::endl;
		
		str = L"%current-dir%;%module-dir%";
		occurrence = ReplacePathVariables( str );
		assert(occurrence  == 2);
		std::wcout<< str << std::endl;

		str = L"%current-dir%\\..;%module-dir%\\..";
		occurrence = ReplacePathVariables( str );
		assert(occurrence  == 2);
		std::wcout<< str << std::endl;


		str = L"%module-dir%\\..";
		occurrence = ReplacePathVariables( str );
		assert(occurrence  == 1);
		std::wcout<< str << std::endl;
	}

} // namespace STR_TEST