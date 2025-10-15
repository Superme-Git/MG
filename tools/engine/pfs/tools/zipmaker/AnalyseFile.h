#pragma once
// 分析文件

#include <map>
#include <string>
#include <iomanip>
#include "GlobalInfo.h"

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
			return os	<< L"总共: "	<< std::setw( 8)	<< right.m_count		<< L" 个,"
						<< L"失败: "	<< std::setw( 8)	<< right.m_failcount	<< L" 个,"
						<< L"源大小: "	<< std::setw( 12)	<< right.m_sizeSrc		<< L" B,"
						<< L"现大小: "	<< std::setw( 12)	<< right.m_size			<< L" B,"
						<< L"耗时: "	<< std::setw( 10)	<< right.m_time			<< L" ms,"
						<< L"处理速度: "<< std::setw( 10)	<< (right.m_time ? float(right.m_sizeSrc) / right.m_time : 0) << L" B/ms"		
						<< std::endl;
		}
	};
	typedef std::map< std::wstring, CAnalyseInfo>	CAnalyseInfoMap;

	void AddAnalyseInfo( const std::wstring filename, fsize_t sizeSrc, fsize_t size, DWORD time, bool bfail)
	{
		std::wstring ext = CGlobalInfo::GetFileExt( filename);
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
			os << std::setw(8) << itor->first << L" " << itor->second;
		
		os << L"---------------------------------------" << std::endl;
		CAnalyseInfo allinfo = right.GetAllInfos();
		return os << L"所有文件 " << allinfo;
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