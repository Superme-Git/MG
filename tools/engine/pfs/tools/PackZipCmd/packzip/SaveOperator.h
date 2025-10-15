#pragma once

#include "Operator.h"

// 输出安装包或资源包
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
	typedef std::map<HashPair, std::wstring> CFileNameHashMap; // 用来检测文件名的hash值是否重复
	CFileNameHashMap		m_filenamehashs;

	std::wstring				m_file;
	bool						m_bResource;
	ZIPFILE::CFileWriter		m_writter;
	PFSX::CNativeFile			m_fileio;
	CAnalyseFile				m_analyse;

	PFS::CDataFile				m_tempfile;
	PFS::CDataBlock				m_buffer;

	PFSX::SetupMetaVector		m_setuppath;	// 记录setup目录
	PFS::CMetaInfo				m_setupmeta;

	std::wstring				m_md5Value;			// 保存安装包的md5值

	PFS::CMetaInfoMap			m_allMetaInfos;		// 保存所有文件metainfo

	std::wstring				m_lastDataPfs;		// 上一版本安装包pfs的mount路径，输出紧急更新包时有效

	PFS::CMetaInfoMap			m_bakAllMetaInfos;	// 保存所有文件metainfo的备份

	bool MakePakMetaFileHash( PFSX::SetupMetaInfo& setupmeta);	// 生成打包目录meta文件的hash信息
	bool SaveDataToZip( const std::wstring& filename, PFS::CDataBlock& data);		// 保存内存块到zip包
	bool ReadDataFromZip( ZIPFILE::CFileReader& fileReader, 
		const std::wstring& filename, PFS::CDataBlock& data);		// 从zip包读内存块

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

	// *.pak文件是一个zip包但是没有.file.meta信息，要和子包区分开，特殊处理
	bool MergePakFile( CZipTreeItem& item, fsize_t& sizeSrc, fsize_t& sizeDes);
	bool IsPakFile( const std::wstring& filename);

	bool SaveOneFileData( CZipTreeItem& item, fsize_t& sizeSrc, fsize_t& sizeDes, PFS::CMetaInfo& meta);

	// 生成md5值
	bool MakeMD5();

	// 得到上一版本安装包的 MetaInfoMap
	bool GetLastDataPfsMetaInfoMap();

	// 将上一版本setupMeta信息和当前setupMeta信息 Merge
	bool MergeLastSetupMetaInfo();

	// 生成 basefile 的 md5
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
			wstring err = L"打开" + m_file + L"文件失败，该文件可能正在被其他程序使用！";
			MSGLOGW(err.c_str());
			return false;
		}
		m_writter.SetFileIO( m_fileio);

		if( m_listener)
			m_listener->OnNotify( GetItemFileCount( m_parent), COperator::PT_TOTALCOUNT);

		// 如果是输出紧急更新包，预先得到上一版本安装包pfs的 MetaInfoMap
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