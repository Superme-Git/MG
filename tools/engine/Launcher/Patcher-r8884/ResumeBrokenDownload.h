// 实现端点续传下载文件
#pragma once
#include "../../pfs/src/common.h"

class CResumeBrokenDownload
{
	std::wstring		m_file;			// 文件名
	std::wstring		m_url;			// 下载的url
	std::wstring		m_localPath;	// 本地文件夹
	unsigned long long	m_hash;			// 文件hash值用来验证
	PFSX::CNativeFile	m_cfgFile;

public:
	CResumeBrokenDownload( const std::wstring& file, const std::wstring& url, 
		const std::wstring& localPath, unsigned long long hash);
	~CResumeBrokenDownload(void);

	bool Download( HANDLE hBreakEvent);

private:
	// 校验文件
	bool CheckFile();

	// 读配置文件
	bool ReadCfgFile( size_t& rOffset, PFS::fsize_t& rTotal, PFS::FILE_DATA_HASH& rHash );

	// 写配置文件
	bool WriteCfgFile( size_t offset, PFS::fsize_t total=-1, const PFS::FILE_DATA_HASH& hash = 0 );
};
