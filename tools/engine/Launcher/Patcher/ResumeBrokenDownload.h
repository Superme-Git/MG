// ʵ�ֶ˵����������ļ�
#pragma once
#include "../../pfs/src/common.h"

class CResumeBrokenDownload
{
	std::wstring		m_file;			// �ļ���
	std::wstring		m_url;			// ���ص�url
	std::wstring		m_localPath;	// �����ļ���
	unsigned long long	m_hash;			// �ļ�hashֵ������֤
	PFSX::CNativeFile	m_cfgFile;

public:
	CResumeBrokenDownload( const std::wstring& file, const std::wstring& url, 
		const std::wstring& localPath, unsigned long long hash);
	~CResumeBrokenDownload(void);

	bool Download( HANDLE hBreakEvent);

private:
	// У���ļ�
	bool CheckFile();

	// �������ļ�
	bool ReadCfgFile( size_t& rOffset, PFS::fsize_t& rTotal, PFS::FILE_DATA_HASH& rHash );

	// д�����ļ�
	bool WriteCfgFile( size_t offset, PFS::fsize_t total=-1, const PFS::FILE_DATA_HASH& hash = 0 );
};
