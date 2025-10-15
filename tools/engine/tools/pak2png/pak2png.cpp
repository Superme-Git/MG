//-----------------------------------------------------------------------
//
// pak2png - Convert pictures in a source pak(package) to the png format.
//
// See usage() for detailed info.
//
//-----------------------------------------------------------------------
// Revision history:
//-----------------------------------------------------------------------
// 2010-11-25 - V0.8.0 - yanghaibo
//-----------------------------------------------------------------------

#include "stdafx.h"
#include "../libpak/PicPak.h"
#include "../libpak/PicPakConverter.h"
#include "../libpak/IImageEncoder.h"
#include "../libpak/Encoder/PNGEncoder.h"
#include <iostream>
#include <shlwapi.h>

#define WIDEN2(x) L ## x
#define WIDEN(x) WIDEN2(x)
#define __TIMESTAMPW__ WIDEN(__DATE__) L" " WIDEN(__TIME__)
#define VERSION_STRING L"Version 0.8.0 - Compiled at " __TIMESTAMPW__

static void usage( _TCHAR* arg0 )
{
	std::wstring exepath = arg0;
	std::wstring::size_type dot = exepath.rfind(L'\\');
	if( dot == std::wstring::npos )
	{
		dot = exepath.rfind(L'/');
	}
	if( dot != std::wstring::npos )
	{
		exepath.assign( &arg0[dot + 1] );
	}

	std::wcerr << exepath << L" - Convert pictures in a source pak(package) to the png format." << std::endl;
	std::wcerr << VERSION_STRING << std::endl;
	std::wcerr << std::endl;
	std::wcerr << L"Usage:" << std::endl;
	std::wcerr << L"    " << L"1. " << exepath << L" <pak-full-path> <png-full-path>" << std::endl;
	std::wcerr << L"    " << L"2. " << exepath << L" --pakrootdir:<pak-root-dir> --pngrootdir:<png-root-dir> [-R]/[--recursive] <sub-pak1> <sub-pak2> ..." << std::endl;
	std::wcerr << std::endl;
	std::wcerr << L"Options:" << std::endl;
	std::wcerr << L"    " <<  L"<pak-full-path>: the full path of the source package." << std::endl;
	std::wcerr << L"    " <<  L"<png-full-path>: the full path of the destination png directory." << std::endl;
	std::wcerr << L"    " <<  L"<pak-root-dir>: the root directory of the source package where the sub-packages be in." << std::endl;
	std::wcerr << L"    " <<  L"<png-root-dir>: the root directory of the png package where the sub-packages be converted into." << std::endl;
	std::wcerr << L"    " <<  L"[-R]/[--recursive] : optional. if specified, sub-directories would be converted recursively." << std::endl;
	std::wcerr << L"    " <<  L"<sub-pak1> <sub-pak2> ... : 1 or more, the package list that to be converted." << std::endl;
	std::wcerr << std::endl;
	std::wcerr << L"Examples:" << std::endl;
	std::wcerr << L"    " << L"1. " << exepath << L" D:\\pak\\icon.pak D:\\pak\\icon_png.pak" << std::endl;
	std::wcerr << L"    " << L"2. " << exepath << L" --pakrootdir:D:\\pak\\ --pngrootdir:D:\\pak_png\\" << std::endl;
	std::wcerr << L"    " << L"3. " << exepath << L" --pakrootdir:D:\\pak\\ --pngrootdir:D:\\pak_png\\ -R" << std::endl;
	std::wcerr << std::endl;
	std::wcerr << L"Notes: "  << std::endl;
	std::wcerr << L"    " << L"1. if [-R]/[--recursive] be specified, the xml files under the <pak-root-dir> will be copied to the <png-root-dir> also." << std::endl;
	std::wcerr << L"    " << L"2. " << exepath << L" returns the number of the succeeded translated packages. -1 or 0 means error."<< std::endl;
}

static bool is_string_begin_with( const std::wstring& strFull, const std::wstring& strPre )
{
	return 0 == wcsncmp( strFull.c_str(), strPre.c_str(), strPre.size() );
}

static bool is_string_end_with( const std::wstring& strFull, const std::wstring& strExt )
{
	return 0 == _wcsicmp( strFull.c_str() + strFull.size() - strExt.size(), strExt.c_str() );
}

static bool convert( std::wstring pakpath, std::wstring pngpath )
{
	MHZX::CPicPakConverter converter;

	MHZX::CPicPak pak, pak_png;

	if( !pak.Open( pakpath.c_str() ) )
	{
		return false;
	}
		
	if( !pak_png.Open( pngpath.c_str(), true ) )
	{
		return false;
	}

	MHZX::IImageEncoder* pEncoder = MHZX::CImageEncoderFactory::CreateImageEncoder(MHZX::IMAGE_ENCODER_PNG);
	if( pEncoder == NULL )
	{
		std::wcerr << L"ERROR: NO IMAGE ENCODER: " << MHZX::IMAGE_ENCODER_PNG << std::endl;
		return false;
	}

	std::wcout<< L"------BEGIN----- " << pakpath << L" --> " << pngpath << L"------" << std::endl;
	bool bRet = converter.Convert( pak, pak_png, pEncoder );
	
	pEncoder->Release();

	std::wcout<< L"------ END ----- " << pakpath << L" --> " << pngpath << L"------" << std::endl;


	return bRet;
}

static bool is_arg_like_ask_usage( const std::wstring& strArg )
{
	if( strArg.compare( L"-h" ) == 0 ||
		strArg.compare( L"/h" ) == 0 ||
		strArg.compare( L"-?" ) == 0 ||
		strArg.compare( L"/?" ) == 0 ||
		strArg.compare( L"?" ) == 0 ||
		strArg.compare( L"--help" ) == 0 ||
		strArg.compare( L"-help" ) == 0 ||
		strArg.compare( L"/help" ) == 0 )
	{
		return true;
	}
	else
	{
		return false;
	}
}

static bool is_arg_set_recursive( const std::wstring& strArg )
{
	if( strArg.compare( L"-R" ) == 0 ||
		strArg.compare( L"--recursive" ) == 0 )
	{
		return true;
	}
	else
	{
		return false;
	}
}

static bool parse_argument_key_value(const std::wstring& strArg,
							  const std::wstring& rstrKey, 
							  std::wstring& rstrValue )
{
	if( is_string_begin_with( strArg, L"--"+ rstrKey + L":" ) ||
		is_string_begin_with( strArg, L"--"+ rstrKey + L"=" )  )
	{
		rstrValue = strArg.substr( (L"--"+ rstrKey + L":").size() );
		return true;
	}
	else
	{
		return false;
	}
}

static bool check_arg_set_pakrootdir( const std::wstring& strArg, std::wstring& rpakrootdir )
{
	return parse_argument_key_value( strArg, L"pakrootdir", rpakrootdir );
}

static bool check_arg_set_pngrootdir( const std::wstring& strArg, std::wstring& rpngrootdir )
{
	return parse_argument_key_value( strArg, L"pngrootdir", rpngrootdir );
}

class CFindFiles
{
protected:
	std::wstring		m_strBasePath;

public:
	CFindFiles( const std::wstring& strBasePath)
		: m_strBasePath( strBasePath)
	{
	}
	
	virtual ~CFindFiles()
	{
	}

protected:
	void OnFoundFileItem( const std::wstring& strRelativeDir, WIN32_FIND_DATAW& wfd)
	{
		if( L'.' == wfd.cFileName[0] &&
			!!( FILE_ATTRIBUTE_DIRECTORY & wfd.dwFileAttributes) ) // dot directory name
			return;

		_wcslwr_s( wfd.cFileName, MAX_PATH );

		std::wstring	strCurrentFile = strRelativeDir + L"\\" + wfd.cFileName;
		if( !!( FILE_ATTRIBUTE_DIRECTORY & wfd.dwFileAttributes) )
		{
			// It's a directory
			OnFoundDirectory( strCurrentFile );
		}
		else
		{
			// It's a file
			OnFoundOneFile( strCurrentFile );
		}
	}

	void SeachFiles( const std::wstring& strRelativeDir )
	{
		std::wstring		strFindPath = m_strBasePath + strRelativeDir + L"\\*.*";

		WIN32_FIND_DATAW	wfd;

		HANDLE	hFind = FindFirstFileW( strFindPath.c_str(), &wfd );

		if( INVALID_HANDLE_VALUE == hFind)
			return;
		
		do 
		{
			OnFoundFileItem( strRelativeDir, wfd);

		} while( FindNextFileW( hFind, &wfd ) );

		FindClose( hFind);
	}

	bool IsFileExisting( const std::wstring& strRelativeDir, const std::wstring& strFileName )
	{
		return !!::PathFileExistsW( (m_strBasePath + strRelativeDir + L"\\" + strFileName).c_str() );
	}

	virtual bool OnFoundDirectory( const std::wstring& strRelativeDir )
	{
		// Recursively deep into sub-folder
		SeachFiles( strRelativeDir );
		return true;
	}

	virtual void OnFoundOneFile( const std::wstring& strRelativeFileName ) = 0;

public:
	virtual void DoFind( )
	{ 
		SeachFiles( std::wstring() );
	}

	const std::wstring& GetBasePath() const
	{
		return m_strBasePath;
	}
};

class CFindPaksInDir :  public CFindFiles
{
public:
	CFindPaksInDir ( const std::wstring& rootDir )
		: CFindFiles( rootDir )
	{
	}
	
protected:
	virtual bool OnFoundDirectory( const std::wstring& strRelativeDir )
	{
		if( IsFileExisting( strRelativeDir, L"pak.meta.xml" ) )
		{
			m_vPakList.push_back( strRelativeDir );
			return true;
		}
		else
		{
			// do not recursively into sub-folder
			return true;
		}
	}

	virtual void OnFoundOneFile( const std::wstring& strRelativeFileName )
	{
		if( is_string_end_with( strRelativeFileName, L".pak" ) )
		{
			m_vPakList.push_back( strRelativeFileName );
		}
	}

public:
	int ForEachPak( const std::wstring& pngDir )
	{
		std::list< std::wstring > ::const_iterator itr = m_vPakList.begin();
		std::list< std::wstring > ::const_iterator iEnd = m_vPakList.end();

		const std::wstring& pakrootpath = GetBasePath();
		const std::wstring& pngrootpath = pngDir;

		int successed = 0;
		for( ; itr != iEnd; ++ itr )
		{
			std::wstring pakpath = pakrootpath + *itr;
			std::wstring pngpath = pngrootpath + *itr;
			successed += !!convert( pakpath, pngpath );
		}

		return successed;
	}
	
protected:
	std::list< std::wstring > m_vPakList;
	
};

class CFindPaksDeeply : public CFindPaksInDir
{
public:
	CFindPaksDeeply( const std::wstring& rootDir )
		: CFindPaksInDir( rootDir )
	{
	}

protected:
	virtual bool OnFoundDirectory( const std::wstring& strRelativeDir )
	{
		if( IsFileExisting( strRelativeDir, L"pak.meta.xml" ) )
		{
			m_vPakList.push_back( strRelativeDir );
			return true;
		}
		else
		{
			// Recursively deep into sub-folder
			SeachFiles( strRelativeDir );
			return true;
		}
	}

	virtual void OnFoundOneFile( const std::wstring& strRelativeFileName )
	{
		if( is_string_end_with( strRelativeFileName, L".pak" ) )
		{
			m_vPakList.push_back( strRelativeFileName );
		}
		else if( is_string_end_with( strRelativeFileName, L".xml" ) )
		{
			m_vMetaFileList.push_back( strRelativeFileName );
		}
	}

public:
	int ForEachMetaFile( const std::wstring& pngDir )
	{
		std::list< std::wstring > ::const_iterator itr = m_vMetaFileList.begin();
		std::list< std::wstring > ::const_iterator iEnd = m_vMetaFileList.end();

		const std::wstring& pakrootpath = GetBasePath();
		const std::wstring& pngrootpath = pngDir;

		int successed = 0;

		for( ; itr != iEnd; ++ itr )
		{
			std::wstring pakpath = pakrootpath + *itr;
			std::wstring pngpath = pngrootpath + *itr;

			successed += ::CopyFileW( pakpath.c_str(), pngpath.c_str(), FALSE );
		}

		return successed;
	}

protected:
	std::list< std::wstring > m_vMetaFileList;
};

static int convert_recursively( const std::wstring& pakrootdir, const std::wstring& pngrootdir )
{
	CFindPaksDeeply finder( pakrootdir );
	finder.DoFind();

	int successed = finder.ForEachPak( pngrootdir );
	if( successed > 1 )
	{
		std::wcout<< L"----------- " << successed << L" packages were converted. ------" << std::endl;
	}
	else if( successed == 1 )
	{
		std::wcout<< L"----------- " << successed << L" package was converted. ------" << std::endl;
	}
	else
	{
		std::wcout<< L"----------- no any package is converted. ------" << std::endl;
	}
	
	int successed2 = finder.ForEachMetaFile( pngrootdir );
	if( successed2 > 1 )
	{
		std::wcout<< L"----------- " << successed2 << L" xml files were copied. ------" << std::endl;
	}
	else if( successed2 == 1 )
	{
		std::wcout<< L"----------- " << successed2 << L" xml file was copied. ------" << std::endl;
	}

	return successed + successed2;
}

static int convert_in_dir(const std::wstring& pakrootdir, const std::wstring& pngrootdir )
{
	CFindPaksInDir finder( pakrootdir );
	finder.DoFind();
	int successed = finder.ForEachPak( pngrootdir );
	if( successed > 1 )
	{
		std::wcout<< L"----------- " << successed << L" packages were converted. ------" << std::endl;
	}
	else if( successed == 1 )
	{
		std::wcout<< L"----------- " << successed << L" package was converted. ------" << std::endl;
	}
	else
	{
		std::wcout<< L"----------- no any package was converted. ------" << std::endl;
	}
	return successed;
}

static int convert_orderly(const std::wstring& pakrootdir, const std::wstring& pngrootdir,
	const std::vector< std::wstring >& paklist )
{
	std::vector< std::wstring > ::const_iterator itr = paklist.begin();
	std::vector< std::wstring > ::const_iterator iEnd = paklist.end();

	int successed = 0;
	for( ; itr != iEnd; ++ itr )
	{
		std::wstring pakpath = pakrootdir + *itr;
		std::wstring pngpath = pngrootdir + *itr;

		successed += !!convert( pakpath, pngpath );
	}

	if( successed > 1 )
	{
		std::wcout<< L"----------- " << successed << L" packages were converted. ------" << std::endl;
	}
	else if( successed == 1 )
	{
		std::wcout<< L"----------- " << successed << L" package was converted. ------" << std::endl;
	}
	else
	{
		std::wcout<< L"----------- no any package was converted. ------" << std::endl;
	}

	return successed;
}

static void path_check_append_slash( std::wstring& rpath )
{
	if( rpath[rpath.size()-1] != L'\\' && 
		rpath[rpath.size()-1] != L'/' )
	{
		rpath += L'\\';
	}
}

int _tmain(int argc, _TCHAR* argv[])
{
	if( argc < 3 )
	{
		usage( argv[0] );
		if( argc == 2 && is_arg_like_ask_usage(argv[1]) )
		{
			return 0;
		}
		else
		{
			return -1;
		}
	}

	if( argc >= 3 )
	{
		unsigned int argflags = 0;

		std::wstring pakrootdir;
		std::wstring pngrootdir ;

		std::vector< std::wstring > paklist;
		for( int arg = 1; arg < argc; arg ++ ) 
		{
			if( is_arg_set_recursive( argv[arg] ) )
			{
				argflags |= 0x01;
				continue;
			}
			
			if( check_arg_set_pakrootdir( argv[arg], pakrootdir ) )
			{
				argflags |= 0x02;
				continue;
			}
			
			if( check_arg_set_pngrootdir( argv[arg], pngrootdir ) )
			{
				argflags |= 0x04;
				continue;
			}

			paklist.push_back( argv[arg] );
		}

		if( ( argflags & 0x07 ) == 0x07 )
		{
			path_check_append_slash(pakrootdir);
			path_check_append_slash(pngrootdir);
			return !!convert_recursively( pakrootdir, pngrootdir );
		}
		else if( ( argflags & 0x07 ) == 0x06 )
		{
			path_check_append_slash(pakrootdir);
			path_check_append_slash(pngrootdir);
			if( paklist.size() > 0 )
				return !!convert_orderly( pakrootdir, pngrootdir, paklist );
			else
				return !!convert_in_dir( pakrootdir, pngrootdir );
		}
		else if( ( argflags & 0x07 ) == 0x0 )
		{
			if( argc == 3 )
			{
				std::wstring pakrootpath = argv[1];
				std::wstring pngrootpath = argv[2];
				return !!convert( pakrootpath, pngrootpath );
			}
		}
	}

	usage( argv[0] );
	return -1;
}

