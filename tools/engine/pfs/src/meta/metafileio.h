#pragma once

namespace PFS {

class CMetaIOError{};

class CMetaRead
{
protected:
	CMetaRead() {}
	~CMetaRead() {}

	// mm		 - 解析的文件元信息表
	// usVersion - MetaInfo文件的版本号
	// nCount	 - 解析前所知的信息表的条目数量
	virtual bool DoParseFile( CMetaInfoMap& mm, unsigned short usVersion, unsigned int nCount);
	virtual CBaseFile& GetFile() = 0;
};

class CMetaFileRead : public CMetaRead
{
protected:
	PFSX::CZipReadFile	m_file;
	CMetaFileRead( CBaseFile& basefile);
	~CMetaFileRead() {}
protected:
	virtual CBaseFile& GetFile()
	{
		return m_file;
	}
public:
	static bool DoRead( CBaseFile& basefile, CMetaInfoMap& mm);
};

class CMetaDataRead : public CMetaRead
{
protected:
	CImageFile			m_imagefile;
	PFSX::CZipReadFile	m_file;
	CMetaDataRead( const CDataBlock& data );
protected:
	virtual CBaseFile& GetFile()
	{
		return m_file;
	}
public:
	static bool DoRead( const CDataBlock& data, CMetaInfoMap& mm);
};

class CMetaWrite
{
protected:
	CMetaWrite() {}
	~CMetaWrite() {}
	virtual CBaseFile& GetFile() = 0;
	// mm		 - 文件元信息表
	// usVersion - MetaInfo文件的版本号
	void DoDumpFile( const CMetaInfoMap& mm, unsigned short usVersion);
};

class CMetaFileWrite : CMetaWrite
{
	PFSX::CZipWriteFile		m_file;
	CMetaFileWrite( CBaseFile& basefile, const CMetaInfoMap& mm, unsigned short usVersion) :
		m_file( basefile, 9)
	{
		DoDumpFile( mm, usVersion);
	}
	~CMetaFileWrite() {}
protected:
	virtual CBaseFile& GetFile()
	{
		return m_file;
	}
public:
	static bool DoWriteByVersion( CBaseFile& basefile, const CMetaInfoMap& mm, unsigned short usVersion);
	static bool DoWrite( CBaseFile& basefile, const CMetaInfoMap& mm); // V1.0
};

class CMetaDataWrite : public CMetaWrite
{
	CDataFile				m_datafile;
	PFSX::CZipWriteFile		m_file;
	CMetaDataWrite( CDataBlock& data, const CMetaInfoMap& mm, unsigned short usVersion)
		: m_file( m_datafile, 9)
	{
		DoDumpFile( mm,usVersion);
		PFS::CImageData& image = m_datafile.GetImage();
		data.Append( image.GetData(), (size_t)image.GetSize());
	}
	~CMetaDataWrite() {}
protected:
	virtual CBaseFile& GetFile()
	{
		return m_file;
	}
public:
	static bool DoWriteByVersion( CDataBlock& data, const CMetaInfoMap& mm, unsigned short usVersion);
	static bool DoWrite( CDataBlock& data, const CMetaInfoMap& mm);// V1.0
};

class CFileMetaInfo
{
	CFileHashMethod*		m_fileHashMethod;
	static CFileMetaInfo	s_instance;

	CFileMetaInfo();
	~CFileMetaInfo();

	const CFileHashMethod* GetMethod()
	{
		return m_fileHashMethod;
	}
	void SetMethod( const CFileHashMethod& fhm)
	{
		m_fileHashMethod->Delete();
		m_fileHashMethod = fhm.Clone();
	}
	void SetDefault();

	static CFileMetaInfo& Instance()
	{
		return s_instance;
	}
public:
	//////////////////////////////////////////////////////////////////////////
	//
	// public static methods
	//
	//////////////////////////////////////////////////////////////////////////


	/// <summary>
	/// 	change the file-hash method
	/// </summary>
	/// <param name="fhm">
	/// 	the new method
	/// </param>
	static void SetFileHashMethod( const CFileHashMethod& fhm)
	{
		Instance().SetMethod(fhm);
	}

	/// <summary>
	/// 	Retrieve the file-hash method
	/// </summary>
	/// <return type="const PFS::CFileHashMethod *">
	/// 	file hash method object. Not NULL.
	///	Default method is MD5.
	/// </return>
	static const CFileHashMethod* GetFileHashMethod()
	{
		return Instance().GetMethod();
	}

	/// <summary>
	/// 	Reset file-hash method to default (MD5)
	/// </summary>
	static void SetDefautFileHashMethod()
	{
		Instance().SetDefault();
	}

	/// <summary>
	/// 	Calculate file-size and hash-value for a file
	/// </summary>
	/// <param name="file">
	/// 	the source file
	/// </param>
	/// <param name="info">
	/// 	the result meta info
	/// </param>
	/// <return type="bool">
	/// 	the status
	/// </return>
	static bool MakeFileMetaInfo( CBaseFile& file, CMetaInfo& info)
	{
		info.SetFileSize( file.GetSize());
		FILE_DATA_HASH	digest;
		if( !GetFileHashMethod()->MakeFileHash( file, digest))
			return false;
		info.SetFileHash( digest);
		return true;
	}

	/// <summary>
	/// 	Calculate file-size and hash-value for a translated file
	/// </summary>
	/// <param name="file">
	/// 	the source file
	/// </param>
	/// <param name="info">
	/// 	the result meta info
	/// </param>
	/// <return type="bool">
	/// 	the status
	/// </return>
	static bool MakeFileMetaInfo2( CBaseFile& file, CMetaInfo& info)
	{
		info.SetFileSize2( file.GetSize());
		FILE_DATA_HASH	digest;
		if( !GetFileHashMethod()->MakeFileHash( file, digest))
			return false;
		info.SetFileHash2( digest);
		return true;
	}

	/// <summary>
	/// 	Calculate file-size and hash-value for a file
	///		( both file-size and file-size2 )
	///		( both hash-value and hash-value2 )
	/// </summary>
	/// <param name="file">
	/// 	the source file
	/// </param>
	/// <param name="info">
	/// 	the result meta info
	/// </param>
	/// <return type="bool">
	/// 	the status
	/// </return>
	static bool MakeFileMetaInfoBoth( CBaseFile& file, CMetaInfo& info)
	{
		if( MakeFileMetaInfo( file, info ) )
		{
			info.SetFileSize2( info.GetFileSize() );
			info.SetFileHash2( info.GetFileHash() );
			return true;
		}
		return false;
	}

	/// <summary>
	/// 	Get the constant meta file name 
	/// </summary>
	/// <return type="const std::wstring &">
	/// 	The constant file name for ".files.meta"
	/// </return>
	static const std::wstring& GetMetaFileName()
	{
		static std::wstring name = L"/.files.meta";
		return name;
	}
};

} // namespace PFS {