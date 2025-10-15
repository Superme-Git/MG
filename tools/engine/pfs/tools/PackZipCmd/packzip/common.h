#pragma once
#include <time.h>
#include "../../../pfs2.h"
#include "../../../translate.h"
#include <strsafe.h>

#define MSGLOG(msg, ...) \
printf(msg, ##__VA_ARGS__);\
Logger::GetInstance().WriteLog(msg, ##__VA_ARGS__)
#define MSGLOGW(msg, ...) \
wprintf(msg, ##__VA_ARGS__);\
Logger::GetInstance().WriteLog(msg, ##__VA_ARGS__)
#define ERRLOG Logger::GetInstance().WriteLog

using namespace std;
const int BUFFSIZE = 8*1024;

static const unsigned int ZipID = PFSX::MakeTypeIDByName( 'Z', 'I', 'P');
static const unsigned int X2OID = PFSX::MakeTypeIDByName( 'X', '2', 'O');
static const unsigned int ZpdID = PFSX::MakeTypeIDByName( 'Z', 'P', 'D');
static const unsigned int BzfID = PFSX::MakeTypeIDByName( 'B', 'Z', 'F');
static const unsigned int NoDecode = 0;
static const unsigned int DecodeCount = 4;

enum
{
	NO = 0,
	RADIO1,
	RADIO2,
	RADIO3,
	RADIO4,
	RADIO5,
	RADIO6,
	RADIO7,
	RADIO8,
	RADIO9,
};

static std::wstring GetFileExt( const std::wstring& filename)
{
	std::wstring sExt = filename.substr( filename.find_last_of(L'.')+1);
	std::transform( sExt.begin(), sExt.end(), sExt.begin(), ::towlower);
	return sExt;
}

static void Data2AnsiString( PFS::CDataBlock& in, std::string& out)
{
	static const std::string TABLE = "0123456789abcdef";

	out.clear();
	const char* data = (const char*)in.GetData();
	for( size_t i = 0; i < in.GetSize(); i ++)
	{
		const char& c = data[i];
		out += TABLE[ (c >> 4) & 0x0f];
		out += TABLE[ c & 0x0f];
	}
}

class Logger
{
	HANDLE m_hMutex;

public:
	Logger() 
	{
		m_hMutex = CreateMutexW(NULL, false, NULL);
	}
	~Logger() { CloseHandle(m_hMutex); }

	void WriteLog( const wchar_t* strDetail, va_list args )
	{
		WaitForSingleObject(m_hMutex, INFINITE);
		const wchar_t* logFileName = L"LogFile.txt";
		FILE* logfile = _wfopen(logFileName, L"a");
		if(!logfile)
			goto Finally;
		wchar_t szBuffer[BUFFSIZE] = {0};
		HRESULT hr = StringCchVPrintfW(szBuffer, _countof(szBuffer), strDetail, args);
		va_end(args);
		if(SUCCEEDED(hr))
		{
			wchar_t buffer[BUFFSIZE] = {0};
			time_t t = time(0);
			tm* pt = localtime(&t);
			hr = StringCchPrintfW(buffer, BUFFSIZE, L"%04d-%02d-%02d %02d:%02d:%02d %s\n", 
				pt->tm_year+1900,
				pt->tm_mon+1,
				pt->tm_mday,
				pt->tm_hour,
				pt->tm_min,
				pt->tm_sec,
				szBuffer);
			if(SUCCEEDED(hr))
				fputws(buffer, logfile);
		}
Finally:
		fclose(logfile);
		ReleaseMutex(m_hMutex);
	}

	void WriteLog( const wchar_t* strDetail, ... )
	{
		va_list args;
		va_start(args, strDetail);
		WriteLog(strDetail, args);
	}

	void WriteLog( const char* strDetail, ... )
	{
		wchar_t buffer[BUFFSIZE] = {0};
		mbstowcs(buffer, strDetail, BUFFSIZE);
		va_list args;
		va_start(args, strDetail);
		WriteLog(buffer, args);
	}

	static Logger& GetInstance()
	{
		static Logger _log;
		return _log;
	}
};

typedef unsigned long long fsize_t;

class CAnalyseFile
{
public:
	struct CAnalyseInfo
	{
		DWORD			m_count;		// 个数
		DWORD			m_failcount;	// 失败个数
		fsize_t			m_sizeSrc;		// 源大小
		fsize_t			m_size;			// 大小
		DWORD			m_time;			// 时间（毫秒）
		CAnalyseInfo() : m_sizeSrc( 0), m_size( 0), m_time( 0), m_count( 0), m_failcount( 0) {}
		CAnalyseInfo& operator+=( const CAnalyseInfo& right)
		{
			m_count += right.m_count;
			m_failcount += right.m_failcount;
			m_sizeSrc += right.m_sizeSrc;
			m_size += right.m_size;
			m_time += right.m_time;
			return *this;
		}
		friend std::wostream& operator<<( std::wostream& os, const CAnalyseInfo& right)
		{
			return os	<< L"总共: "	<< right.m_count		<< L" 个,"<<std::endl
						<< L"失败: "	<< right.m_failcount	<< L" 个,"<<std::endl
						<< L"源大小: "	<< right.m_sizeSrc		<< L" B,"<<std::endl
						<< L"现大小: "	<< right.m_size			<< L" B,"<<std::endl
						<< L"耗时: "	<< right.m_time			<< L" ms,"<<std::endl;
		}
	};
	typedef std::map< std::wstring, CAnalyseInfo>	CAnalyseInfoMap;

	void AddAnalyseInfo( const std::wstring filename, fsize_t sizeSrc, fsize_t size, DWORD time, bool bfail)
	{
		std::wstring ext = GetFileExt( filename);
		CAnalyseInfoMap::iterator itor = m_infos.find( ext);
		if( itor == m_infos.end())
		{
			CAnalyseInfoMap::_Pairib pr = m_infos.insert( std::make_pair( ext, CAnalyseInfo()));
			itor = pr.first;
		}
		itor->second.m_count++;
		if( bfail)
			itor->second.m_failcount++;
		itor->second.m_sizeSrc += sizeSrc;
		itor->second.m_size += size;
		itor->second.m_time += time;
	}

	CAnalyseInfoMap& GetAnalyseInfo() { return m_infos; }

	friend std::wostream& operator<<( std::wostream& os, const CAnalyseFile& right)
	{
		for( CAnalyseInfoMap::const_iterator itor = right.m_infos.begin(); itor != right.m_infos.end(); ++itor)
			os << itor->first << L" " << itor->second<<std::endl;
		
		os << L"---------------------------------------" << std::endl;
		CAnalyseInfo allinfo = right.GetAllInfos();
		os << L"所有文件 " << allinfo << std::endl;
		os << L"---------------------------------------" << std::endl;
		return os;
	}
	
	CAnalyseInfo GetAllInfos() const
	{
		CAnalyseInfo allinfo;
		for( CAnalyseInfoMap::const_iterator itor = m_infos.begin(); itor != m_infos.end(); ++itor)
			allinfo += itor->second;
		return allinfo;
	}

private:
	CAnalyseInfoMap		m_infos;
		
};

static bool IsFileExisting( const LPCWSTR filename )
{
	DWORD	dwAttribute = GetFileAttributesW( filename );
	if( INVALID_FILE_ATTRIBUTES == dwAttribute)
		return false;
	return FILE_ATTRIBUTE_DIRECTORY != ( dwAttribute & FILE_ATTRIBUTE_DIRECTORY);
}

static bool IsDirectoryExisting( const LPCWSTR dir)
{
	DWORD	dwAttribute = GetFileAttributesW( dir );
	if( INVALID_FILE_ATTRIBUTES == dwAttribute)
		return false;
	return FILE_ATTRIBUTE_DIRECTORY == ( dwAttribute & FILE_ATTRIBUTE_DIRECTORY);
}

static time_t ParseDate( const std::wstring& src )
{
	std::wistringstream ss( src);
	std::wstring token;
	int i = 0;
	tm ltime;
	ltime.tm_hour = 0;
	ltime.tm_min = 0;
	ltime.tm_sec = 0;
	while (true)
	{
		std::getline( ss, token, L'-');
		if( ss.fail())
			break;

		if( i == 0)
			ltime.tm_year = _wtoi( token.c_str())-1900;
		else if( i == 1)
			ltime.tm_mon = _wtoi( token.c_str()) - 1;
		else if( i == 2)
			ltime.tm_mday = _wtoi( token.c_str());
		i++;
	}
	if( i != 3)
		return 0;
	return mktime(&ltime);
}