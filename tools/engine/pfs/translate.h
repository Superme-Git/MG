#pragma once

namespace PFSX
{

inline TSID MakeTypeIDByName( char a = ' ', char b = ' ', char c = ' ', char d = ' ')
{
	return ((unsigned int)a) | (((unsigned int)b) << 8) | (((unsigned int)c) << 16) | (((unsigned int)d) << 24);
}

inline PFS::FILEPFSXATTRIB MakeFilePfsxAttributes( TSID decoder, CRC32 crc)
{
	return ((PFS::FILEPFSXATTRIB)decoder | ((PFS::FILEPFSXATTRIB)crc << 32));
}

inline TSID GetEncoderTypeFromFilePfsxAttributes( PFS::FILEPFSXATTRIB attr)
{
	return (TSID)(attr);
}

inline TSID GetDecoderTypeFromFilePfsxAttributes( PFS::FILEPFSXATTRIB attr)
{
	return (TSID)(attr);
}

inline CRC32 GetCRC32FromFilePfsxAttributes( PFS::FILEPFSXATTRIB attr)
{
	return (CRC32)(attr >> 32);
}

class CTranslateBase : public PFS::CUncopyableObject
{
	TSID				m_tsid;
	PFS::CBaseFile*		m_src;
	PFS::CBaseFile*		m_src2;
	PFS::CBaseFile*		m_dst;

protected:
	CTranslateBase( TSID tsid)
		: m_tsid( tsid)
		, m_src( NULL)
		, m_src2( NULL)
		, m_dst( NULL)
	{}
	virtual ~CTranslateBase() {}

public:
	PFS::CBaseFile& GetSource() { assert( m_src); return *m_src; }
	PFS::CBaseFile* GetSource2() { return m_src2; }
	PFS::CBaseFile& GetDestination() { assert( m_dst); return *m_dst; }
public:
	TSID GetID() const { return m_tsid; }

	void SetParam( const void* data, size_t size) { /* dummy implementation */ }
	size_t GetParamSize() const { return 0; }
	const void* GetParam() const { return NULL; }

	void SetSource( PFS::CBaseFile* src) { m_src = src; }
	void SetSource2( PFS::CBaseFile* src2) { m_src2 = src2; }
	void SetDestination( PFS::CBaseFile* dst) { m_dst = dst; }
};

class CEncoder : public CTranslateBase
{
public:
	CEncoder( TSID tsid) : CTranslateBase( tsid) {}
	virtual ~CEncoder() {}
public:
	virtual int DoEncode() = 0;
	virtual CEncoder* Clone() const = 0;
	virtual void Delete() = 0;
};

class CDecoder : public CTranslateBase
{
public:
	CDecoder( TSID tsid) : CTranslateBase( tsid) {}
	virtual ~CDecoder() {}
public:
	virtual int DoDecode() = 0;
	virtual CDecoder* Clone() const = 0;
	virtual void Delete() = 0;
};

class CTranslateManager
{
public:
	// ���� CTranslateManager ����
	// ˵�������η��ʻ���ж�����ڲ���ʼ������ԱȽ���
	static CTranslateManager& GetInstance();

	// ����tsid ��ȡ��Ӧ��Encoder����
	// ע�⣬��GetEncoder���ص�Encoder������Ҫ����Delete()����
	CEncoder* GetEncoder( TSID tisd);

	// ����tsid ��ȡ��Ӧ��Decoder����
	// ע�⣬��GetDecoder���ص�Decoder������Ҫ����Delete()����
	CDecoder* GetDecoder( TSID tisd);

	// ��̬���е�Encoderע��
	void RegisterEncoder( const CEncoder& rEncoder);

	// ��̬���е�Decoderע��
	void RegisterDecoder( const CDecoder& rDecoder);

private:
	CTranslateManager();
	~CTranslateManager();

	// �ڲ�ʵ��
	void _Initialize();
	void _UnInitialize();
	void _OnFindPlugin( const wchar_t* wszFileName );
	bool _RegisterPlugin( const void * pModule );
	void _RegisterEncoder( CEncoder* pEncoder);
	void _RegisterDecoder( CDecoder* pDecoder);

private:
	typedef std::map<TSID,CEncoder*> CEncoderMap;
	typedef std::map<TSID,CDecoder*> CDecoderMap;
	typedef std::list<void*> CPluginModulesContainer;

	CEncoderMap m_EncoderMap;
	CDecoderMap m_DecoderMap;
	CPluginModulesContainer m_PluginModules;
};

class CEncodeFile : public PFS::CFile
{
public:
	CEncodeFile() {}
	virtual ~CEncodeFile() {}
public:
	virtual bool Open( const std::wstring& filename, PFS::FILE_MODE fm, PFS::FILE_ACCESS fa);
};

bool EncodeFile( const std::wstring& srcFileName, const std::wstring& dstFileName );

bool EncodeFile( PFS::CBaseFile* psrcFile, PFS::CBaseFile* pdstFile, TSID encoderId );

class CDecodeFile : public PFS::CFile
{
public:
	CDecodeFile() {}
	virtual ~CDecodeFile() {}
public:
	virtual bool Open( const std::wstring& filename, PFS::FILE_MODE fm, PFS::FILE_ACCESS fa);
};

bool DecodeFile( const std::wstring& srcFileName, const std::wstring& dstFileName );
// DecodeFile��ǿ����,ִ��DecodeFile֮�󲢶Խ���ļ����������Լ��
// �ú���Ϊ�汾��֤�����ر��ṩ
bool DecodeFileEn( const std::wstring& srcFileName, const std::wstring& dstFileName );
bool DecodeFileEn( PFS::CBaseFile& inFile, PFS::CBaseFile& outFile, const PFS::CMetaInfo& minfo );
bool DecodeFile( PFS::CBaseFile* psrcFile, PFS::CBaseFile* pdstFile, TSID decoderId );

// make difference of two the files( oldFileName and newFileName) 
bool DiffFile( const std::wstring& oldFileName, const std::wstring& newFileName, const std::wstring& diffFileName );
bool DiffFile( PFS::CBaseFile* oldFile, PFS::CBaseFile* newFile, PFS::CBaseFile* diffFile );

// Patch oldFileName using the file of patchFileName, the result is also oldFileName.
// TSID of patchFileName should be BDF
bool PatchFile( const std::wstring& oldFileName, const std::wstring& patchFileName );

// Patch the file of oldFileName using the file of patchFileName, the result is the file of newFileName
// TSID of patchFileName should be BDF
// newFileName should not be same as oldFileName
bool PatchFile( const std::wstring& oldFileName, const std::wstring& patchFileName, const std::wstring& newFileName );

//////////////////////////////////////////////////////////////////////////
// ʹ��Translate��ʽOpen�ļ�(ͬʱӦ��Encoder/Decoder)
class CFileEx : public PFS::CFile
{
public:
	CFileEx() {}
	virtual ~CFileEx() {}
public:
	virtual bool Open( const std::wstring& filename, PFS::FILE_MODE fm, PFS::FILE_ACCESS fa);
};

// ʹ��Translate��ʽcopy�ļ�(ͬʱӦ��Encoder/Decoder)
// �ļ�MetaInfo�����е�m_attributes������� encoder/decoder ��Ϊ0 ʱ��Ŀ���ļ�Ϊ�����ļ���Ӳ����
// ����encoderId��decoderId�������ļ�psrcFile����ת���������д��pdstFile��
// ��encoderId �� decoderId ��Ϊ0 ʱ��Ŀ���ļ�Ϊ�����ļ���Ӳ����
bool TranslateFile( const std::wstring& srcFileName, const std::wstring& dstFileName );
bool TranslateFile( PFS::CBaseFile* psrcFile, PFS::CBaseFile* pdstFile, TSID encoderId, TSID decoderId );

//////////////////////////////////////////////////////////////////////////
// У���ļ�
// עrFile�����ĺ�������ִ����Check���̺��ļ�ָ�벻һ�����ļ�ͷ��
// �������Ҫ�����÷�Seek���ļ�ͷ��
//////////////////////////////////////////////////////////////////////////

// ����MetaInfo�ṩ��FileSize��FileHashֵУ���ļ������Ƿ�����
bool CheckFile( const std::wstring& strFileName );

bool CheckFile( PFS::CBaseFile& rFile, const PFS::CMetaInfo& minfo );

// ����MetaInfo�ṩ��FileSize2��FileHash2ֵУ���ļ������Ƿ�����
bool CheckFile2( const std::wstring& strFileName );
bool CheckFile2( PFS::CBaseFile& rFile, const PFS::CMetaInfo& minfo );

// ���minfo�е�tsid=X2O��0�������CheckFileУ���ļ����ݣ�
// ���FileSize2��FileHash2��Ϊ0�������ڵ�MetaInfo��1.0,���ǲ���У��ֱ��pass��
// ��������ʹ��CheckFile2����У�顣
bool CheckFileEx( const std::wstring& strFileName );
bool CheckFileEx( PFS::CBaseFile& rFile, const PFS::CMetaInfo& minfo );

} // namespace PFSX {

