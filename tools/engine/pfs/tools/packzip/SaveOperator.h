#pragma once

#include "Operator.h"

class COutputWriter
{
public:
	COutputWriter() {}
	virtual ~COutputWriter() {}
public:
	virtual bool Initialize() = 0;
	virtual void Uninitialize() = 0;

	virtual bool WriteNextCentral( const ZIPFILE::CFileCentral& fc) = 0;
	virtual void SetZipLevel( int level) = 0;
	virtual bool WriteFileData( const PFS::CDataBlock& in, bool bFinish) = 0;
	virtual size_t WriteDeflateData( const PFS::CDataBlock& in, bool bSeek) = 0;

	virtual bool MakeOutZipFileMD5( PFS::CDataBlock& result) = 0;
	virtual const std::wstring& GetOutputFileName() const = 0;
};


class CZipOutputWriter : public COutputWriter
{
	std::wstring				m_file;
	ZIPFILE::CFileWriter		m_writter;
	PFSX::CNativeFile			m_fileio;
public:
	CZipOutputWriter( const std::wstring& file)
		: m_file( file) 
	{}
	virtual ~CZipOutputWriter() {}
public:
	virtual bool Initialize() 
	{
		if( !m_fileio.Open( m_file, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_RDWR))
			return false;
		m_writter.SetFileIO( m_fileio);
		return true;
	}
	
	virtual void Uninitialize()
	{
		m_writter.CloseFile();
	}

	virtual bool WriteNextCentral( const ZIPFILE::CFileCentral& fc)
	{
		return m_writter.WriteNextCentral( fc);
	}
	virtual void SetZipLevel( int level)
	{
		m_writter.SetZipLevel( level);
	}

	virtual bool WriteFileData( const PFS::CDataBlock& in, bool bFinish)
	{
		return m_writter.WriteFileData( in, bFinish);
	}
	virtual size_t WriteDeflateData( const PFS::CDataBlock& in, bool bSeek)
	{
		return m_writter.WriteDeflateData( in, bSeek);
	}

	virtual const std::wstring& GetOutputFileName() const
	{
		return m_file;
	}
	virtual bool MakeOutZipFileMD5( PFS::CDataBlock& result)
	{
		m_fileio.Seek( 0, PFS::FSM_SET);

		PFS::CDataBlock				buffer;
		// ZERO the buffer.
		buffer.Resize(BUFFERSIZE);

		PFS::helper::CMD5Hash md5Method;
		while( size_t read = m_fileio.Read( buffer.GetData(), BUFFERSIZE))
		{
			if( PFS::FILE_SIZE_ERROR == read )
			{
				return false;
			}
			md5Method.Update( buffer.GetData(), read);
		}
		md5Method.Final( result);
		return true;
	}

};

class CPathOutputWriter : public COutputWriter
{
	std::wstring				m_rootpath;
	PFSX::CNativeFile*			m_nativefile;
	PFSX::CZipWriteFile*		m_zipfile;
	PFS::CBaseFile*				m_currentfile;
public:
	CPathOutputWriter( const std::wstring& rootpath)
		: m_rootpath( rootpath) , m_nativefile( NULL), m_zipfile( NULL), m_currentfile( NULL)
	{}
	virtual ~CPathOutputWriter() 
	{
		delete m_nativefile;
		delete m_zipfile;
	}
public:
	virtual bool Initialize() 
	{
		return true;
	}

	virtual void Uninitialize()
	{
		delete m_zipfile;
		delete m_nativefile;
		m_zipfile = NULL;
		m_nativefile = NULL;
	}

	virtual bool WriteNextCentral( const ZIPFILE::CFileCentral& fc)
	{
		delete m_zipfile;
		delete m_nativefile;

		std::wstring	filename = m_rootpath + L"\\" + fc.GetFileName();

		PFS::CFileIO::CheckCreateParentDirectory( filename);

		m_nativefile = new PFSX::CNativeFile();
		bool result = m_nativefile->Open( filename, PFS::FM_CREAT | PFS::FM_TRUNC, PFS::FA_WRONLY);
		if( !result)
			return false;
		if( fc.m_usMethod)
		{
			m_zipfile = new PFSX::CZipWriteFile( *m_nativefile, 1);
			m_currentfile = m_zipfile;
		}
		else
		{
			m_currentfile = m_nativefile;
		}
		return true;
	}
	virtual void SetZipLevel( int level) {}

	virtual bool WriteFileData( const PFS::CDataBlock& in, bool bFinish)
	{
		bool result = PFS::FILE_SIZE_ERROR != m_currentfile->Write( in.GetData(), in.GetSize());
		if( result && bFinish)
		{
			m_currentfile->SetEnd();
			m_nativefile->Close();

			delete m_zipfile;
			delete m_nativefile;
			m_zipfile = NULL;
			m_nativefile = NULL;
		}
		return result;
	}
	virtual size_t WriteDeflateData( const PFS::CDataBlock& in, bool bSeek)
	{
		return m_nativefile->Write( in.GetData(), in.GetSize());
	}

	virtual const std::wstring& GetOutputFileName() const
	{
		return m_rootpath;
	}
	virtual bool MakeOutZipFileMD5( PFS::CDataBlock& result)
	{
		result.Clear();
		return true;
	}

};

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

	COutputWriter*				m_writer;			
	bool						m_bResource;
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
		: CNeedProjectOperator( parent, project), m_writer(  new CZipOutputWriter( file)), m_bResource( bResource), m_lastDataPfs( lastDataPfs)
	{ 
		m_buffer.Resize( BUFFERSIZE);
	}
	CSaveOperator( CZipTreeItem& parent, CProject& project, const std::wstring rootpath)
		: CNeedProjectOperator( parent, project), m_writer(  new CPathOutputWriter( rootpath)), m_bResource( false)
	{ 
		m_buffer.Resize( BUFFERSIZE);
	}

	~CSaveOperator()
	{
		delete m_writer;
	}

	virtual CZipTreeItem* Do()
	{
		if( !m_writer->Initialize())
			return NULL;

		if( m_listener)
			m_listener->OnNotify( GetItemFileCount( m_parent), COperator::PT_TOTALCOUNT);

		// 如果是输出紧急更新包，预先得到上一版本安装包pfs的 MetaInfoMap
		GetLastDataPfsMetaInfoMap();

		try
		{
			SaveAllFile( m_parent);
			SaveSetupMetaInfo();
			SaveFileMetaInfo();
			m_writer->Uninitialize();
			if( !m_bResource && m_project.GetIsMakeMD5())
			{
				MakeMD5();
			}
		}
		catch( ZIPFILE::CZipWriteError&)
		{
		}
		return NULL;
	}
	CAnalyseFile& GetAnalyseFile() { return m_analyse; }

	const std::wstring& GetMD5Value() { return m_md5Value; }
};
