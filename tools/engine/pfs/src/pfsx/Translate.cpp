#include "../common.h"
#include <Shlwapi.h>
#include "../../pfsx/x2o/X2OTranslator.h"
#include "../../pfsx/zip/ZipTranslator.h"
#include "../../pfsx/bdf/BdfTranslator.h"

namespace PFSX {

	static bool _CopyBaseFile( PFS::CBaseFile& source, PFS::CBaseFile& dest );

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// CTranslateManager
// 
CTranslateManager& CTranslateManager::GetInstance()
{ 
	static CTranslateManager s_instance;
	return s_instance;
}

CTranslateManager::CTranslateManager()
{
	_Initialize();
}

CTranslateManager::~CTranslateManager() 
{
	_UnInitialize();
}

typedef long (*TRANS_GetTransCountT)();
typedef void* (*TRANS_GetTransItemT)(long lIndex);

bool CTranslateManager::_RegisterPlugin( const void * pModule )
{
	HMODULE hModule = (HMODULE)pModule;

	TRANS_GetTransCountT pfn_TRANS_GetDecoderCount = 
		(TRANS_GetTransCountT)GetProcAddress( hModule, "TRANS_GetDecoderCount" );
	if ( pfn_TRANS_GetDecoderCount == NULL )
	{
		return false;
	}

	TRANS_GetTransCountT pfn_TRANS_GetEncoderCount = 
		(TRANS_GetTransCountT)GetProcAddress( hModule, "TRANS_GetEncoderCount" );
	if ( pfn_TRANS_GetEncoderCount == NULL )
	{
		return false;
	}

	TRANS_GetTransItemT pfn_TRANS_GetDecoderItem = 
		(TRANS_GetTransItemT)GetProcAddress( hModule, "TRANS_GetDecoderItem" );
	if ( pfn_TRANS_GetDecoderItem  == NULL )
	{
		return false;
	}

	TRANS_GetTransItemT pfn_TRANS_GetEncoderItem = 
		(TRANS_GetTransItemT)GetProcAddress( hModule, "TRANS_GetEncoderItem" );
	if ( pfn_TRANS_GetEncoderItem == NULL )
	{
		return false;
	}

	using namespace PFSX;
	long lDecoderCount = pfn_TRANS_GetDecoderCount();
	for ( long iIndex = 0; iIndex < lDecoderCount; iIndex ++ )
	{
		CDecoder * pDecoder = (CDecoder*)pfn_TRANS_GetDecoderItem( iIndex );
		if ( pDecoder != NULL )
		{
			_RegisterDecoder ( pDecoder );
		}
	}
	
	long lEncoderCount = pfn_TRANS_GetEncoderCount();
	for ( long iIndex = 0; iIndex < lEncoderCount; iIndex ++ )
	{
		CEncoder * pEncoder = (CEncoder *)pfn_TRANS_GetEncoderItem( iIndex );
		if ( pEncoder != NULL )
		{
			_RegisterEncoder ( pEncoder );
		}
	}

	return true;
}

void CTranslateManager::_OnFindPlugin( LPCWSTR wszFileName )
{
	HMODULE hModule = LoadLibraryW(wszFileName);
	if ( hModule == NULL )
	{
		return;
	}

	if ( !_RegisterPlugin(hModule ) )
	{
		FreeLibrary( hModule );
	}
	else
	{
		m_PluginModules.push_back( hModule );
	}
}

void CTranslateManager::_Initialize()
{
	// 初始化内联的X2O,ZIP,BDF Translaters
	PFSX::X2O::Initialize();
	PFSX::ZIP::Initialize();
	PFSX::BDF::Initialize();

	// 初始化插件动态库中提供的Translaters
	std::wstring wstrPathPattern;
	wstrPathPattern.resize(MAX_PATH);

	// 对于Launcher等没有和GameClient在相同目录下的程序来说，我们需要特殊处理运行时目录
	// 即不能简单地使用ModuleDir。
	// 这里通过对比CurrentDirectory和ModuleDir的关系来确定是否使用CurrentDirectory
	// 原则是，如果CurrentDirectory是ModuleFileName的前缀，我们使用CurrentDirectory
	// 否则使用ModuleDir
	std::wstring wstrPathPatternPWD; // 当前工作目录
	wstrPathPatternPWD.resize(MAX_PATH);

	DWORD dwRet = GetCurrentDirectoryW((DWORD)wstrPathPatternPWD.size(), &*wstrPathPatternPWD.begin());
	wstrPathPatternPWD.resize(dwRet);

	dwRet = GetModuleFileNameW( NULL, &*wstrPathPattern.begin(), wstrPathPattern.size() );
	wstrPathPattern.resize(dwRet);

	std::wstring wstrPluginFolder = wstrPathPattern;
	if( PathIsPrefix(wstrPathPatternPWD.c_str(), wstrPathPattern.c_str() ) )
	{
#ifdef _DEBUG
		wstrPluginFolder = wstrPathPatternPWD + L"\\translators\\debug\\";
#else
		wstrPluginFolder = wstrPathPatternPWD + L"\\translators\\release\\";
#endif
	}
	else
	{
		std::wstring::size_type  pos = wstrPathPattern.find_last_of( L"/\\:");
#ifdef _DEBUG
		wstrPluginFolder.replace(pos, -1, L"\\translators\\debug\\" );
#else
		wstrPluginFolder.replace(pos, -1, L"\\translators\\release\\" );
#endif
	}

	PathCombineW((LPWSTR)wstrPathPattern.c_str(), wstrPluginFolder.c_str(), L"pfsx-*.dll" );

	WIN32_FIND_DATAW FindFileData;
	HANDLE hFind = FindFirstFileW( wstrPathPattern.data(), &FindFileData );
	if (hFind != INVALID_HANDLE_VALUE) 
	{
		do
		{
			std::wstring wstrFilePath = wstrPluginFolder + FindFileData.cFileName;
			_OnFindPlugin ( wstrFilePath.c_str() );
		}
		while( FindNextFileW( hFind, &FindFileData ) != 0 );

		FindClose( hFind );
	}
}

void CTranslateManager::_UnInitialize()
{
	CEncoderMap::iterator itr = m_EncoderMap.begin();
	for ( ; itr != m_EncoderMap.end(); ++ itr )
	{
		(*itr).second->Delete();
	}

	CDecoderMap::iterator itr2 = m_DecoderMap.begin();
	for ( ; itr2 != m_DecoderMap.end(); ++ itr2 )
	{
		(*itr2).second->Delete();
	}

	CPluginModulesContainer::iterator itr3 = m_PluginModules.begin();
	for (; itr3 != m_PluginModules.end(); ++ itr3 )
	{
		FreeLibrary((HMODULE)*itr3);
	}
}

void CTranslateManager::_RegisterEncoder( CEncoder* pEncoder)
{
	m_EncoderMap.insert( std::make_pair( pEncoder->GetID(), pEncoder) );
}

void CTranslateManager::_RegisterDecoder( CDecoder* pDecoder)
{
	m_DecoderMap.insert( std::make_pair( pDecoder->GetID(), pDecoder) );
}

void CTranslateManager::RegisterEncoder( const CEncoder& rEncoder)
{
	assert(m_EncoderMap.find(rEncoder.GetID()) == m_EncoderMap.end() );
	_RegisterEncoder( rEncoder.Clone() );
}

void CTranslateManager::RegisterDecoder( const CDecoder& rDecoder)
{
	assert(m_DecoderMap.find(rDecoder.GetID()) == m_DecoderMap.end() );
	_RegisterDecoder( rDecoder.Clone() );
}

// 注意，从GetEncoder返回的Encoder对象需要调用Delete()销毁
CEncoder* CTranslateManager::GetEncoder( TSID tisd)
{
	CEncoderMap::const_iterator it = m_EncoderMap.find( tisd);
	if( it == m_EncoderMap.end())
		return NULL;
	const CEncoder* src = it->second;
	return src->Clone();
}

// 注意，从GetDecoder返回的Decoder对象需要调用Delete()销毁
CDecoder* CTranslateManager::GetDecoder( TSID tisd)
{
	CDecoderMap::const_iterator it = m_DecoderMap.find( tisd);
	if( it == m_DecoderMap.end())
		return NULL;
	const CDecoder* src = it->second;
	return src->Clone();
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class CFileTranslater
{
	std::wstring m_srcFileName;
	std::wstring m_srcFileName2;
	std::wstring m_dstFileName;
	TSID m_encoderId;
	TSID m_decoderId;

	CEncoder*		m_encoder;
	CDecoder*		m_decoder;
	PFS::CBaseFile*	m_srcfile;
	PFS::CBaseFile*	m_srcfile2;
	PFS::CBaseFile*	m_dstfile;
	PFS::CTempFile*	m_tempfile;
	bool			m_bCloseSrcFile;
	bool			m_bCloseSrcFile2;
	bool			m_bCloseDstFile;
	fsize_t			m_srcOrignalPos; // 暂时没有使用
	fsize_t			m_dstOrignalPos;

public:
	CFileTranslater( const std::wstring& src, TSID encoder, TSID decoder)
		: m_srcFileName( src ), m_srcFileName2(), m_dstFileName(), m_encoderId(encoder), m_decoderId(decoder)
		, m_encoder( NULL), m_decoder( NULL), m_srcfile( NULL), m_srcfile2(NULL), m_dstfile( NULL), m_tempfile( NULL)
		, m_bCloseSrcFile(TRUE), m_bCloseSrcFile2(FALSE), m_bCloseDstFile(TRUE)
		, m_srcOrignalPos(0), m_dstOrignalPos(0)
	{
	}

	CFileTranslater( const std::wstring& src, const std::wstring& dst, TSID encoder, TSID decoder)
		: m_srcFileName( src ), m_dstFileName(dst),  m_encoderId(encoder), m_decoderId(decoder)
		, m_encoder( NULL), m_decoder( NULL), m_srcfile( NULL), m_srcfile2(NULL), m_dstfile( NULL), m_tempfile( NULL)
		, m_bCloseSrcFile(TRUE), m_bCloseSrcFile2(TRUE), m_bCloseDstFile(TRUE)
		, m_srcOrignalPos(0), m_dstOrignalPos(0)
	{
	}

	CFileTranslater( const std::wstring& src,
		const std::wstring& src2,
		const std::wstring& dst, TSID encoder, TSID decoder)
		: m_srcFileName( src ), m_srcFileName2( src2 ), 
		m_dstFileName(dst),  m_encoderId(encoder), m_decoderId(decoder)
		, m_encoder( NULL), m_decoder( NULL), m_srcfile( NULL), m_srcfile2(NULL), m_dstfile( NULL), m_tempfile( NULL)
		, m_bCloseSrcFile(TRUE), m_bCloseSrcFile2(TRUE), m_bCloseDstFile(TRUE)
		, m_srcOrignalPos(0), m_dstOrignalPos(0)
	{
	}

	CFileTranslater( PFS::CBaseFile* psrcFile, PFS::CBaseFile* pdstFile, TSID encoderId, TSID decoderId )
		: m_srcfile(psrcFile), m_srcfile2(NULL)
		, m_dstfile( pdstFile)
		, m_encoderId(encoderId), m_decoderId(decoderId)
		, m_encoder( NULL), m_decoder( NULL)
		, m_tempfile( NULL)
		, m_bCloseSrcFile(psrcFile==NULL)
		, m_bCloseSrcFile2(FALSE)
		, m_bCloseDstFile(pdstFile==NULL)
		, m_srcOrignalPos(0), m_dstOrignalPos(0)
	{
		// 当psrcFile不为NULL或pdstFile不为NULL时，m_bCloseSrcFile&m_bCloseDstFile用来保证,
		// 即使不detach结果文件，也不会被close掉
	}

	~CFileTranslater()
	{
		if( m_encoder)
			m_encoder->Delete();
		if( m_decoder)
			m_decoder->Delete();

		if( m_srcfile && m_bCloseSrcFile )
			PFS::CEnv::CloseFile( m_srcfile);
		
		if( m_srcfile2 && m_bCloseSrcFile2 )
			PFS::CEnv::CloseFile( m_srcfile2);
		
		if( m_dstfile && m_bCloseDstFile )
			PFS::CEnv::CloseFile( m_dstfile);
		
		if( m_tempfile )
			PFS::CEnv::CloseFile( m_tempfile);
	}

private:
	int _Initialize( )
	{
		//
		// 为了方便使用，我们允许有一个encoder或者一个decoder或者两个都有，但不允许没有任何一个
		// 如果两个都有，先使用decoder，后使用encoder。
		//
		if ( m_encoderId == 0 && m_decoderId == 0 )
		{
			return PFS::ECX_DECODER_UNFOUND;
		}

		if( m_srcfile == NULL )
		{
			m_srcfile = PFS::CEnv::OpenFile( m_srcFileName, PFS::FM_EXCL, PFS::FA_RDONLY);
			if( NULL == m_srcfile)
			{
				return PFS::CEnv::GetLastError();
			}
			m_dstOrignalPos = 0;
		}
		else
		{
			m_srcOrignalPos = m_srcfile->GetPos();			
		}

		if( !m_srcFileName2.empty() && m_srcfile2 == NULL )
		{
			m_srcfile2 = PFS::CEnv::OpenFile( m_srcFileName2, PFS::FM_EXCL, PFS::FA_RDONLY);
			if( NULL == m_srcfile2 )
			{
				return PFS::CEnv::GetLastError();
			}
		}

		if ( m_dstfile == NULL )
		{
			if ( m_dstFileName.empty() )
			{
				PFS::CTempFile*	_tempfile = new PFS::CTempFile;
				if( !_tempfile->Open())
				{
					delete _tempfile; 
					return PFS::CEnv::GetLastError();
				}
				m_dstfile = _tempfile;
				_tempfile = NULL;
			}
			else
			{
				PFS::CFile* pFile = new PFS::CFile();
				if( !pFile->Open(m_dstFileName, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_RDWR) )
				{
					delete pFile;
				}
				else
				{
					m_dstfile = pFile;
				}
				//m_dstfile = PFS::CEnv::OpenFile( m_dstFileName, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_RDWR);
				if( m_dstfile == NULL )
					return PFS::CEnv::GetLastError();
			}

			m_dstOrignalPos = 0;
		}
		else
		{
			m_dstOrignalPos = m_dstfile->GetPos();
		}

		if( m_decoderId )
		{
			m_decoder = CTranslateManager::GetInstance().GetDecoder( m_decoderId);
			if( NULL == m_decoder)
			{
				return PFS::ECX_DECODER_UNFOUND;
			}

			m_decoder->SetSource( m_srcfile);
			m_decoder->SetSource2( m_srcfile2);
		}

		if( m_encoderId)
		{
			m_encoder = CTranslateManager::GetInstance().GetEncoder( m_encoderId);
			if( NULL == m_encoder)
			{
				return PFS::ECX_ENCODER_UNFOUND;
			}

			if ( m_decoderId )
			{
				m_tempfile = new PFS::CTempFile;
				if( !m_tempfile->Open())
				{
					delete m_tempfile;
					m_tempfile = NULL;
					return PFS::CEnv::GetLastError();
				}

				m_decoder->SetDestination( m_tempfile);

				m_encoder->SetSource( m_tempfile);
				if( m_srcfile2 != NULL )
				{
					// TODO:
					m_encoder->SetSource2( m_srcfile );
				}
			}
			else
			{
				m_encoder->SetSource( m_srcfile );
				m_encoder->SetSource2( m_srcfile2 );
			}

			m_encoder->SetDestination( m_dstfile);
		}
		else
		{
			m_decoder->SetDestination( m_dstfile);
		}

		return PFS::EC_SUCCESS;
	}

public:
	int DoTranslate()
	{
		int nCode = _Initialize() ;

		if( PFS::EC_SUCCESS != nCode ) 
		{
			return nCode;
		}
		if( m_decoder )
		{
			int nCode = m_decoder->DoDecode();
			if( PFS::EC_SUCCESS != nCode)
			{
				return nCode;
			}

			if( NULL == m_encoder )
			{
				m_dstfile->SetEnd();
				return PFS::EC_SUCCESS;
			}

			if( m_encoder )
			{
				m_tempfile->Seek(0, PFS::FSM_SET); // reset the file pointer
			}
		}

		if( m_encoder )
		{
			int nCode = m_encoder->DoEncode();
			if( PFS::EC_SUCCESS == nCode)
			{
				m_dstfile->SetEnd();
				return PFS::EC_SUCCESS;
			}
			else 
			{
				return nCode;
			}
		}
		
		return PFS::ECX_DECODER_UNFOUND;		
	}

	PFS::CBaseFile* DetachResultFile()
	{
		assert( m_dstfile != NULL );

		// translate 过程结束之后，将目标文件指针重置, 使其对外状态等价为刚刚Open的状态。
		// 否则使用CFileEx的Open之后，都必须强制调用Seek方法重置文件指针，这不太合理。
		// 重置文件位置到Translate前位置
		m_dstfile->Seek( m_dstOrignalPos, PFS::FSM_SET );
		
		PFS::CBaseFile* temp = m_dstfile;
		m_dstfile = NULL;
		return temp;
	}
};

bool CEncodeFile::Open( const std::wstring& filename, PFS::FILE_MODE fm, PFS::FILE_ACCESS fa)
{
	PFS::CMetaInfo	minfo;
	if( !PFS::CEnv::GetFileMetaInfo( filename, minfo))
		return false;

	PFS::FILEPFSXATTRIB	attr = minfo.GetFilePfsxAttributes();
	TSID encoderId = GetEncoderTypeFromFilePfsxAttributes( attr);
	if( 0 == encoderId )
		return CFile::Open( filename, fm, fa);

	if( PFS::FA_RDONLY != fa)
	{
		PFS::CEnv::SetLastError( PFS::EC_FILE_READONLY);
		return false;
	}
	if( PFS::FM_EXCL != fm)
	{
		PFS::CEnv::SetLastError( PFS::EC_FILE_READONLY);
		return false;
	}

	CFileTranslater ft( filename, encoderId, 0 );

	int nErrCode = ft.DoTranslate();
	if( nErrCode == PFS::EC_SUCCESS )
	{
		AttackBaseFile( ft.DetachResultFile() );
		return true;
	}
	else
	{
		PFS::CEnv::SetLastError( nErrCode );
		return false;
	}
}

bool CDecodeFile::Open( const std::wstring& filename, PFS::FILE_MODE fm, PFS::FILE_ACCESS fa)
{
	PFS::CMetaInfo	minfo;
	if( !PFS::CEnv::GetFileMetaInfo( filename, minfo))
		return false;

	PFS::FILEPFSXATTRIB	attr = minfo.GetFilePfsxAttributes();
	TSID decoderId = GetDecoderTypeFromFilePfsxAttributes(attr);

	// 因为ZPD和X2O的Decoder只是Copy原文件.
	// 为了减少不必要的文件Copy，这里直接使用原文件.
	if( 0 == decoderId ||
		decoderId == MakeTypeIDByName('Z','P','D') ||
		decoderId == MakeTypeIDByName('X','2','O')  )
		return CFile::Open( filename, fm, fa);

	if( PFS::FA_RDONLY != fa)
	{
		PFS::CEnv::SetLastError( PFS::EC_FILE_READONLY);
		return false;
	}
	if( PFS::FM_EXCL != fm)
	{
		PFS::CEnv::SetLastError( PFS::EC_FILE_READONLY);
		return false;
	}

	CFileTranslater ft( filename, 0, decoderId );

	int nErrCode = ft.DoTranslate();
	if( nErrCode == PFS::EC_SUCCESS )
	{
		AttackBaseFile( ft.DetachResultFile() );
		return true;
	}
	else
	{
		PFS::CEnv::SetLastError( nErrCode );
		return false;
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

bool CFileEx::Open( const std::wstring& filename, PFS::FILE_MODE fm, PFS::FILE_ACCESS fa)
{
	PFS::CMetaInfo		minfo;
	if( !PFS::CEnv::GetFileMetaInfo( filename, minfo))
		return false;

	PFS::FILEPFSXATTRIB	attr = minfo.GetFilePfsxAttributes();
	if( 0 == attr)
		return CFile::Open( filename, fm, fa);

	if( PFS::FA_RDONLY != fa)
	{
		PFS::CEnv::SetLastError( PFS::EC_FILE_READONLY);
		return false;
	}
	if( PFS::FM_EXCL != fm)
	{
		PFS::CEnv::SetLastError( PFS::EC_FILE_READONLY);
		return false;
	}

	CFileTranslater ft( filename, 
		GetEncoderTypeFromFilePfsxAttributes(attr), 
		GetDecoderTypeFromFilePfsxAttributes(attr));

	int nErrCode = ft.DoTranslate();
	if( nErrCode == PFS::EC_SUCCESS )
	{
		AttackBaseFile( ft.DetachResultFile() );
		return true;
	}
	else
	{
		PFS::CEnv::SetLastError( nErrCode );
		return false;
	}
}

bool EncodeFile( const std::wstring& srcFileName, const std::wstring& dstFileName )
{
	PFS::CMetaInfo	minfo;
	if( !PFS::CEnv::GetFileMetaInfo( srcFileName, minfo))
		return false;

	TSID encoderId = minfo.GetTSID( );
	if( 0 == encoderId )
	{
		// Copy 时已经将Metainfo复制到目标文件中
		return PFS::CEnv::FileCopy( srcFileName, dstFileName, false);
	}
	else if( encoderId == MakeTypeIDByName('B','D','F') )
	{
		// 不是合理的encoder
		PFS::CEnv::SetLastError( PFS::ECX_ENCODER_UNFOUND );
		return false;
	}
	else
	{
		CFileTranslater ft( srcFileName, dstFileName, encoderId, 0 );

		int nErrCode = ft.DoTranslate();

		// 更新FileMetaInfo信息
		// 这里不能更新 attr 属性，否则会导致更新逻辑出现问题。
		// 不更新attr属性不影响游戏运行。
		if( nErrCode == PFS::EC_SUCCESS && !PFS::CEnv::SetFileMetaInfo(dstFileName, minfo) )
		{
			nErrCode = PFS::CEnv::GetLastError();
		}

		if ( nErrCode != PFS::EC_SUCCESS )
		{
			PFS::CEnv::SetLastError( nErrCode );
			return false;
		}
		else
		{
			return true;
		}
	}
}

bool DecodeFile( const std::wstring& srcFileName, const std::wstring& dstFileName )
{	
	PFS::CMetaInfo	minfo;
	if( !PFS::CEnv::GetFileMetaInfo( srcFileName, minfo))
		return false;

	TSID decoderId = minfo.GetTSID();

	// X2O的decoder也是Copy文件,而且它的MD5和CRC都是输入文件所计算出来的,所以这里可以直接做校验.
	// 但ZIP/ZPD格式的文件无法用此方案,因为他们的MD5和CRC使用输入文件的转换前的原始文件计算的.
	if( decoderId == 0 || 
		decoderId == MakeTypeIDByName('X','2','O') )
	{
		// Copy 时已经将Metainfo复制到目标文件中
		return PFS::CEnv::FileCopy( srcFileName, dstFileName, false);
	}
	else if( decoderId == MakeTypeIDByName('B','D','F') )
	{
		// dstFileName : old version file
		// srcFileName : patch for updating dstFileName
		// After PatchFile: dstFileName will be updated to the new one.
		return PatchFile( dstFileName, srcFileName );
	}
	else
	{
		CFileTranslater ft( srcFileName, dstFileName, 0, decoderId );
		int nErrCode = ft.DoTranslate();

		// 更新FileMetaInfo信息
		// 这里不能更新 attr 属性，否则会导致更新逻辑出现问题。
		// 不更新attr属性不影响游戏运行。
		if( nErrCode == PFS::EC_SUCCESS && !PFS::CEnv::SetFileMetaInfo(dstFileName,  minfo ) )
		{
			nErrCode = PFS::CEnv::GetLastError();
		}

		if ( nErrCode != PFS::EC_SUCCESS )
		{
			PFS::CEnv::SetLastError( nErrCode );
			return false;
		}
		else
		{
			return true;
		}
	}
}

bool DiffFile( PFS::CBaseFile* oldFile, PFS::CBaseFile* newFile, PFS::CBaseFile* diffFile )
{
	// TODO: 判断oldFile,newFile,diffFile是否是CTempFile的实例，如果是，没有必要复制一份临时文件。

	PFS::CTempFile tempOld, tempNew, tempDiff;
	if( !tempOld.Open() )
	{
		// TODO:
		return false;
	}

	if( !tempNew.Open() )
	{
		// TODO:
		return false;
	}

	if( !tempDiff.Open() )
	{
		// TODO:
		return false;
	}			

	if( !_CopyBaseFile( *oldFile, tempOld ) )
	{
		return false;
	}

	if( !_CopyBaseFile( *newFile, tempNew ) )
	{
		return false;
	}

	if( diffFile->Seek( 0, PFS::FSM_SET ) != 0 )
	{
		return false;
	}

	std::wstring oldFileName, newFileName, diffFileName;
	oldFileName = tempOld.GetFileName();
	newFileName = tempNew.GetFileName();
	diffFileName = tempDiff.GetFileName();	

	// Close temporary files but do not delete it, for avoid sharing reading/writing problems.
	tempOld.CNativeFile::Close();
	tempNew.CNativeFile::Close();
	tempDiff.CNativeFile::Close();

	int nBdfRet = PFSX::BDF::DiffNativeFile(oldFileName.c_str(), newFileName.c_str(), diffFileName.c_str() );

	// TODO: 记录日志
	if( nBdfRet != 0 )
	{
		return false;
	}

	// 重新Open
	if( !tempDiff.CNativeFile::Open( tempDiff.GetFileName(), PFS::FM_EXCL, PFS::FA_RDONLY ) )
	{
		return false;
	}

	if( !_CopyBaseFile( tempDiff, *diffFile ) )
	{
		return false;
	}

	return true;
}

bool DiffFile( const std::wstring& oldFileName, const std::wstring& newFileName, const std::wstring& diffFileName )
{
	PFS::CMetaInfo	minfo;
	if( !PFS::CEnv::GetFileMetaInfo( diffFileName, minfo))
		return false;

	TSID encoderId = minfo.GetTSID( );
	if( encoderId != MakeTypeIDByName('B','D','F') )
	{
		// 不是合理的encoder
		PFS::CEnv::SetLastError( PFS::ECX_ENCODER_UNFOUND );
		return false;
	}
	else
	{
		CFileTranslater ft( oldFileName, newFileName, diffFileName, encoderId, 0 );

		int nErrCode = ft.DoTranslate();
		if ( nErrCode != PFS::EC_SUCCESS )
		{
			PFS::CEnv::SetLastError( nErrCode );
			return false;
		}
		else
		{
			return true;
		}

		// TODO: DiffFile之后，需要为diffFileName所对应的文件创建MetaInfo吗？
	}
}

// TSID of patchFileName should be BDF
bool PatchFile( const std::wstring& oldFileName, const std::wstring& patchFileName, const std::wstring& newFileName )
{	
	PFS::CMetaInfo	minfo;
	if( !PFS::CEnv::GetFileMetaInfo( patchFileName, minfo))
		return false;

	TSID decoderId = minfo.GetTSID();
	if( decoderId != MakeTypeIDByName('B','D','F') )
	{
		// 不是合理的decoder
		PFS::CEnv::SetLastError( PFS::ECX_DECODER_UNFOUND );
		return false;
	}
	else
	{
		CFileTranslater ft( oldFileName, patchFileName, newFileName, 0, decoderId );
		int nErrCode = ft.DoTranslate();
		if ( nErrCode != PFS::EC_SUCCESS )
		{
			PFS::CEnv::SetLastError( nErrCode );
			return false;
		}

		// 更新FileMetaInfo信息
		// 这里不能调整 attr 属性，否则会导致更新逻辑出现问题。
		// 不调整attr属性不影响游戏运行。
		if( !PFS::CEnv::SetFileMetaInfo(newFileName,  minfo ) )
		{
			return false;
		}

		return true;
	}
}

// TSID of patchFileName should be BDF
bool PatchFile( const std::wstring& oldFileName, const std::wstring& patchFileName )
{	
	PFS::CMetaInfo	minfo;
	if( !PFS::CEnv::GetFileMetaInfo( patchFileName, minfo))
		return false;

	TSID decoderId = minfo.GetTSID();
	if( decoderId != MakeTypeIDByName('B','D','F') )
	{
		// 不是合理的decoder
		PFS::CEnv::SetLastError( PFS::ECX_DECODER_UNFOUND );
		return false;
	}
	else
	{
		PFS::CBaseFile* pDestFile = NULL;
		{
			CFileTranslater ft( oldFileName, patchFileName, std::wstring(), 0, decoderId );
			int nErrCode = ft.DoTranslate();
			if ( nErrCode != PFS::EC_SUCCESS )
			{
				PFS::CEnv::SetLastError( nErrCode );
				return false;
			}

			pDestFile = ft.DetachResultFile();
			if( pDestFile == NULL )
			{
				return false;
			}
			// 必须在这里关闭CFileTranslater对oldFileName文件的独占访问
		}

		PFS::CFile dest;
		if( !dest.Open( oldFileName, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_WRONLY ) )
		{
			delete pDestFile;
			return false;
		}
		
		pDestFile->Seek( 0, PFS::FSM_SET );
		if( !_CopyBaseFile( *pDestFile, dest ) )
		{
			delete pDestFile;
			return false;
		}

		delete pDestFile;

		// 更新FileMetaInfo信息
		// 这里不能更改 attr 属性，否则会导致更新逻辑出现问题。
		// 不更改attr属性不影响游戏运行。
		if( !PFS::CEnv::SetFileMetaInfo(oldFileName,  minfo ) )
		{
			return false;
		}

		return true;
	}
}

// DecodeFile的强化版,执行DecodeFile之后并对结果文件进行完整性检测
bool DecodeFileEn( const std::wstring& srcFileName, const std::wstring& dstFileName )
{
	PFS::CMetaInfo	minfo;
	if( !PFS::CEnv::GetFileMetaInfo( srcFileName, minfo))
		return false;

	PFS::CFile inFile;
	if( !inFile.Open( srcFileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
	{
		return false;
	}

	PFS::CFile outFile;
	if( !outFile.Open( dstFileName, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_RDWR) )
	{
		return false;
	}

	bool bRet = DecodeFileEn( inFile, outFile, minfo );
	if( bRet )
	{
		if( !PFS::CEnv::AddFileMetaInfo(dstFileName, minfo ))
		{
			return false;
		}
	}
	return bRet;
}

static bool _CopyBaseFile( PFS::CBaseFile& source, PFS::CBaseFile& dest )
{
	PFS::CBaseFile*	fsrc = &source;
	PFS::CBaseFile*	fdst = &dest;
	const int	nBufferSize = 4096;
	unsigned char byteBuffer[nBufferSize];
	bool		bResult = true;
	size_t		s = 0;
	while( s = fsrc->Read( byteBuffer, nBufferSize ) )
	{
		if( PFS::FILE_SIZE_ERROR == s )
			return false;
		s = fdst->Write( byteBuffer, s);
		if( PFS::FILE_SIZE_ERROR == s )
			return false;
	}

	return true;
}

// DecodeFile的强化版,执行DecodeFile之后并对结果文件进行完整性检测
bool DecodeFileEn( PFS::CBaseFile& inFile, PFS::CBaseFile& outFile, const PFS::CMetaInfo& minfo )
{
	TSID decoderId =  minfo.GetTSID( );

	// X2O的decoder也是Copy文件,而且它的MD5和CRC都是输入文件所计算出来的,所以这里可以直接做校验.
	// 但ZIP/ZPD格式的文件无法用此方案,因为他们的MD5和CRC使用输入文件的转换前的原始文件计算的.
	if( decoderId == 0 || 
		decoderId == MakeTypeIDByName('X','2','O') )
	{	
		if( !CheckFile(inFile, minfo) )
		{
			return false;
		}

		if( inFile.Seek(0, PFS::FSM_SET) != 0 )
		{
			return false;
		}

		// Copy 文件内容
		return _CopyBaseFile( inFile, outFile );
	}
	else
	{	
		bool bV11 = !minfo.IsV10();
		if( bV11 )
		{
			if( !CheckFile2(inFile, minfo) )
			{
				return false;
			}
			
			if( inFile.Seek(0, PFS::FSM_SET) != 0 )
			{
				return false;
			}
		}

		CFileTranslater ft( &inFile, &outFile, 0, decoderId );
		int nErrCode = ft.DoTranslate();
		if ( nErrCode != PFS::EC_SUCCESS )
		{
			PFS::CEnv::SetLastError( nErrCode );
			return false;
		}

		if( !bV11 )
		{
			// 取出结果文件进行MD5检测
			PFS::CBaseFile* pResultFile = ft.DetachResultFile();
			PFS::CMetaInfo fileMeta;
			bool bMaked = false;
			if( decoderId == MakeTypeIDByName('Z','P','D')  )
			{
				// 对于ZPD文件,我们需要做一次解压处理才能校验MetaInfo
				// 因为直接从pResultFile读取的数据是压缩格式的,而非压缩前的原始数据
				PFSX::CZipReadFile zpdReader(*pResultFile, minfo.GetFileSize(), true );
				bMaked = PFS::CFileMetaInfo::MakeFileMetaInfo(zpdReader, fileMeta);
			}
			else
			{
				bMaked = PFS::CFileMetaInfo::MakeFileMetaInfo(*pResultFile, fileMeta);
			}

			if( !bMaked )
			{
				PFS::CEnv::SetLastError(PFS::ECX_BADFORMAT);
				return false;
			}

			if( minfo.GetFileHash() != fileMeta.GetFileHash() ||
				minfo.GetFileSize() != fileMeta.GetFileSize() )
			{
				PFS::CEnv::SetLastError(PFS::ECX_BADFORMAT);
				return false;
			}
		}		

		return true;
	}
}

// 使用Translate方式copy文件
// 这种方式会同时Copy MetaInfo.
bool TranslateFile( const std::wstring& srcFileName, const std::wstring& dstFileName )
{
	PFS::CMetaInfo	minfo;
	if( !PFS::CEnv::GetFileMetaInfo( srcFileName, minfo))
		return false;

	PFS::FILEPFSXATTRIB attr = minfo.GetFilePfsxAttributes();
	if( 0 == attr )
	{
		// Copy 时已经将Metainfo复制到目标文件中
		return PFS::CEnv::FileCopy( srcFileName, dstFileName, false);
	}
	else
	{
		CFileTranslater ft( srcFileName, dstFileName, 
			GetEncoderTypeFromFilePfsxAttributes(attr), 
			GetDecoderTypeFromFilePfsxAttributes(attr));
		
		int nErrCode = ft.DoTranslate();

		// 更新FileMetaInfo信息
		// 这里不能更新 attr 属性，否则会导致更新逻辑出现问题。
		// 不更新attr属性不影响游戏运行。
		// 此时dstFileName所对应的文件应该已经创建成功，其MetaInfo也应该已经就绪，直接SetFileMetaInfo即可。
		if( nErrCode == PFS::EC_SUCCESS && 
			!PFS::CEnv::SetFileMetaInfo(dstFileName,  minfo ) )
		{
			nErrCode = PFS::CEnv::GetLastError();
		}

		if ( nErrCode != PFS::EC_SUCCESS )
		{
			PFS::CEnv::SetLastError( nErrCode );
			return false;
		}
		else
		{
			return true;
		}
	}
}

bool EncodeFile( PFS::CBaseFile* psrcFile, PFS::CBaseFile* pdstFile, TSID encoderId )
{
	if( encoderId == 0 )
	{
		// 直接copy文件
		const int	nBufferSize = 4096;
		char		szBuffer[nBufferSize];
		size_t s = 0;
		while( s = psrcFile->Read( szBuffer, nBufferSize) )
		{
			if( PFS::FILE_SIZE_ERROR == s )
				return false;
			s = pdstFile->Write( szBuffer, s);
			if( PFS::FILE_SIZE_ERROR == s)
				return false;
		}

		// 置文件尾标志并重置输出文件指针文件头
		//pdstFile->SetEnd();
		//pdstFile->Seek( 0, PFS::FSM_SET );

		return true;
	}

	CFileTranslater ft( psrcFile, pdstFile, encoderId, 0 );

	int nErrCode = ft.DoTranslate();

	if ( nErrCode != PFS::EC_SUCCESS )
	{
		PFS::CEnv::SetLastError( nErrCode );
		return false;
	}
	else
	{
		return true;
	}
}

bool DecodeFile( PFS::CBaseFile* psrcFile, PFS::CBaseFile* pdstFile, TSID decoderId )
{
	if( decoderId == 0 ||
		decoderId == MakeTypeIDByName('X','2','O') )
	{
		// 直接copy文件
		return _CopyBaseFile( *psrcFile, *pdstFile );
		
		// 置文件尾标志并重置输出文件指针至文件头
		//pdstFile->SetEnd();
		//pdstFile->Seek( 0, PFS::FSM_SET );
		// return true;
	}

	CFileTranslater ft( psrcFile, pdstFile, 0, decoderId );

	int nErrCode = ft.DoTranslate();

	if ( nErrCode != PFS::EC_SUCCESS )
	{
		PFS::CEnv::SetLastError( nErrCode );
		return false;
	}
	else
	{
		return true;
	}
}

// 使用Translate方式copy文件
bool TranslateFile( PFS::CBaseFile* psrcFile, PFS::CBaseFile* pdstFile, TSID encoderId, TSID decoderId )
{
	if( encoderId == 0 && decoderId == 0 )
	{
		// 直接copy文件
		const int	nBufferSize = 4096;
		char		szBuffer[nBufferSize];
		size_t s = 0;
		while( s = psrcFile->Read( szBuffer, nBufferSize) )
		{
			if( PFS::FILE_SIZE_ERROR == s )
				return false;
			s = pdstFile->Write( szBuffer, s);
			if( PFS::FILE_SIZE_ERROR == s)
				return false;
		}

		// 置文件尾标志并重置输出文件指针至文件头
		//pdstFile->SetEnd();
		//pdstFile->Seek( 0, PFS::FSM_SET );

		return true;
	}

	CFileTranslater ft( psrcFile, pdstFile, encoderId, decoderId );

	int nErrCode = ft.DoTranslate();

	if ( nErrCode != PFS::EC_SUCCESS )
	{
		PFS::CEnv::SetLastError( nErrCode );
		return false;
	}
	else
	{
		return true;
	}
}

// 根据MetaInfo提供的FileSize和FileHash值校验文件内容是否完整
bool CheckFile( const std::wstring& strFileName )
{
	PFS::CMetaInfo minfo;
	if( !PFS::CEnv::GetFileMetaInfo( strFileName, minfo))
		return false;
	
	PFS::CFile theFile;
	if( !theFile.Open( strFileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
	{
		return false;
	}

	return CheckFile( theFile, minfo );
}

bool CheckFile( PFS::CBaseFile& rFile, const PFS::CMetaInfo& minfo )
{
	PFS::CMetaInfo fileMeta;
	if( !PFS::CFileMetaInfo::MakeFileMetaInfo( rFile, fileMeta))
	{
		return false;
	}

	if( minfo.GetFileSize() != fileMeta.GetFileSize() ||
		minfo.GetFileHash() != fileMeta.GetFileHash() )
	{
		PFS::CEnv::SetLastError(PFS::ECX_BADFORMAT);
		return false;
	}

	return true;
}

// 根据MetaInfo提供的FileSize2和FileHash2值校验文件内容是否完整
bool CheckFile2( const std::wstring& strFileName )
{
	PFS::CMetaInfo minfo;
	if( !PFS::CEnv::GetFileMetaInfo( strFileName, minfo))
		return false;

	PFS::CFile theFile;
	if( !theFile.Open( strFileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
	{
		return false;
	}

	return CheckFile2( theFile, minfo );
}

bool CheckFile2( PFS::CBaseFile& rFile, const PFS::CMetaInfo& minfo )
{
	PFS::CMetaInfo fileMeta;
	if( !PFS::CFileMetaInfo::MakeFileMetaInfo2( rFile, fileMeta))
	{
		return false;
	}

	if( minfo.GetFileSize2() != fileMeta.GetFileSize2() ||
		minfo.GetFileHash2() != fileMeta.GetFileHash2() )
	{
		PFS::CEnv::SetLastError(PFS::ECX_BADFORMAT);
		return false;
	}

	return true;
}

// 如果minfo中的tsid=X2O或0，则调用CheckFile校验文件内容，
// 如果FileSize2和FileHash2都为0，即现在的MetaInfo是1.0,我们不做校验直接pass，
// 否则，我们使用CheckFile2进行校验。
bool CheckFileEx( const std::wstring& strFileName )
{
	PFS::CMetaInfo minfo;
	if( !PFS::CEnv::GetFileMetaInfo( strFileName, minfo))
		return false;

	PFS::CFile theFile;
	if( !theFile.Open( strFileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
	{
		return false;
	}

	return CheckFileEx( theFile, minfo );
}

bool CheckFileEx( PFS::CBaseFile& rFile, const PFS::CMetaInfo& minfo )
{
	TSID tsid = minfo.GetTSID();

	if (tsid == 0 ||
		tsid == MakeTypeIDByName('X','2','O') )
	{
		return CheckFile( rFile, minfo );
	}
	else
	{
		if( minfo.IsV10() )
		{
			// V10不支持对当前文件内容进行版本校验。
			// 但我们这里需要返回true，以表示我们放弃了校验过程，直接通过校验。
			return true;
		}
		else
		{
			return CheckFile2( rFile, minfo );
		}
	}
}

} // namespace PFSX {