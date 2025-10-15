#pragma once

#include "Operator.h"

// �����װ������Դ��
class CSaveOperator : public CNeedProjectOperator
{
	struct HashPair
	{
		HashPair() {}
		HashPair( unsigned int h1, unsigned int h2) : hash1( h1), hash2( h2) {}

		unsigned int hash1;
		unsigned int hash2;

		bool operator<( const HashPair& dst) const
		{
			if( hash1 == dst.hash1)
				return hash2 < dst.hash2;
			else
				return hash1 < dst.hash1;
		}
	};
	typedef std::map<HashPair, std::wstring> CFileNameHashMap; // ��������ļ�����hashֵ�Ƿ��ظ�
	CFileNameHashMap		m_filenamehashs;

	std::wstring				m_file;
	bool						m_bResource;
	ZIPFILE::CFileWriter		m_writter;
	PFSX::CNativeFile			m_fileio;
	CAnalyseFile				m_analyse;

	PFS::CDataFile				m_tempfile;
	PFS::CDataBlock				m_buffer;

	PFSX::SetupMetaVector		m_setuppath;	// ��¼setupĿ¼
	PFS::CMetaInfo				m_setupmeta;

	std::wstring				m_md5Value;			// ���氲װ����md5ֵ

	PFS::CMetaInfoMap			m_allMetaInfos;		// ���������ļ�metainfo

	std::wstring				m_lastDataPfs;		// ��һ�汾��װ��pfs��mount·��������������°�ʱ��Ч

	PFS::CMetaInfoMap			m_bakAllMetaInfos;	// ���������ļ�metainfo�ı���

	bool MakePakMetaFileHash( PFSX::SetupMetaInfo& setupmeta);	// ���ɴ��Ŀ¼meta�ļ���hash��Ϣ
	bool SaveDataToZip( const std::wstring& filename, PFS::CDataBlock& data);		// �����ڴ�鵽zip��
	bool ReadDataFromZip( ZIPFILE::CFileReader& fileReader, 
		const std::wstring& filename, PFS::CDataBlock& data);		// ��zip�����ڴ��

	void SaveToZip( PFS::CBaseFile& file);
	void SaveAllFile( CZipTreeItem& item);
	void SaveOneFile( CZipTreeItem& item);
	void SaveFileMetaInfo();
	void MakeSetupMeta( CZipTreeItem& item);
	void SaveSetupMetaInfo();
	bool EncodeFileEx( PFS::CBaseFile& src, PFS::CBaseFile& des, CZipTreeItem& item, PFSX::TSID& encoder);
	int GetItemFileCount( CZipTreeItem& item);
	bool MergeChildPackage( CZipTreeItem& item, fsize_t& size);

	void SaveUpdateLog( std::wstring& updatelog);

	bool CheckFileNameHash( const std::wstring& name);

	// *.pak�ļ���һ��zip������û��.file.meta��Ϣ��Ҫ���Ӱ����ֿ������⴦��
	bool MergePakFile( CZipTreeItem& item, fsize_t& sizeSrc, fsize_t& sizeDes);
	bool IsPakFile( const std::wstring& filename);

	bool SaveOneFileData( CZipTreeItem& item, fsize_t& sizeSrc, fsize_t& sizeDes, PFS::CMetaInfo& meta);

	// ����md5ֵ
	bool MakeMD5();

	// �õ���һ�汾��װ���� MetaInfoMap
	bool GetLastDataPfsMetaInfoMap();

	// ����һ�汾setupMeta��Ϣ�͵�ǰsetupMeta��Ϣ Merge
	bool MergeLastSetupMetaInfo();

	// ���� basefile �� md5
	bool MakeBaseFileHash( PFS::CBaseFile& basefile, PFS::FILE_DATA_HASH& md5);
	bool MakeBaseFileHash( PFS::CBaseFile& basefile, PFS::CDataBlock& md5);

public:
	CSaveOperator( CZipTreeItem& parent, CProject& project, const std::wstring file, 
		bool bResource, const std::wstring& lastDataPfs)
		: CNeedProjectOperator( parent, project), m_file( file), m_bResource( bResource), m_lastDataPfs( lastDataPfs)
	{ 
		m_buffer.Resize( BUFFERSIZE);
	}

	virtual bool Do()
	{
		if( !m_fileio.Open( m_file, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_RDWR))
		{
			wstring err = L"��" + m_file + L"�ļ�ʧ�ܣ����ļ��������ڱ���������ʹ�ã�";
			MSGLOGW(err.c_str());
			return false;
		}
		m_writter.SetFileIO( m_fileio);

		if( m_listener)
			m_listener->OnNotify( GetItemFileCount( m_parent), COperator::PT_TOTALCOUNT);

		// ���������������°���Ԥ�ȵõ���һ�汾��װ��pfs�� MetaInfoMap
		GetLastDataPfsMetaInfoMap();

		try
		{
			SaveAllFile( m_parent);
			SaveSetupMetaInfo();
			SaveFileMetaInfo();
			m_writter.CloseFile();
			if( !m_bResource && m_project.GetIsMakeMD5())
			{
				MakeMD5();
			}
		}
		catch( ZIPFILE::CZipWriteError&)
		{
		}
		return true;
	}

	CAnalyseFile& GetAnalyseFile() { return m_analyse; }

	const std::wstring& GetMD5Value() { return m_md5Value; }
};