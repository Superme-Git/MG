//-----------------------------------------------------------------------
// pak2image.cpp : Convert pictures in a source pak(package) to the png/bmp/tga format.
//
// See usage() for detailed info.
//
//-----------------------------------------------------------------------
// Revision history:
//-----------------------------------------------------------------------
// 2010-11-26 - V0.8.0 - yanghaibo
// 2011-03-02 - V0.8.1 - yanghaibo - 
//		support extensions like "pic.png" to convert a01.b01 to a01.pic.png.
//-----------------------------------------------------------------------
#include "stdafx.h"
#include "../libpak/PicPak.h"
#include "../libpak/PicPakConverter.h"
#include "../libpak/IImageEncoder.h"
#include <iostream>
#include <shlwapi.h>

#define WIDEN2(x) L ## x
#define WIDEN(x) WIDEN2(x)
#define __TIMESTAMPW__ WIDEN(__DATE__) L" " WIDEN(__TIME__)
#define VERSION_STRING L"Version 0.8.1 - Compiled at " __TIMESTAMPW__

//----------------------------------------------------
// Global variables determined by the arguments.
//----------------------------------------------------
static bool g_verbose_flag = false;
static unsigned int g_filename_policy = 0;
static std::wstring g_image_type;
//----------------------------------------------------

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

	std::wcerr << exepath << L" - Convert pictures in a source pak(package) to the specified image format." << std::endl;
	std::wcerr << VERSION_STRING << std::endl;
	std::wcerr << std::endl;
	std::wcerr << L"Usage:" << std::endl;
	std::wcerr << L"    " << L"1. " << exepath << L" <pak-full-path> <img-full-path> <png>|<tga>|<bmp> --filename-policy:<byname>|<byid> --fileext-policy:<replace>|<append>|<copy>|<trim> [-V]|[--verbose]" << std::endl;
	std::wcerr << L"    " << L"2. " << exepath << L" <pak-full-path> <img-full-path> --image-type:<png>|<tga>|<bmp> --filename-policy:<byname>|<byid> --fileext-policy:<replace>|<append>|<copy>|<trim> [-V]|[--verbose]" << std::endl;
	std::wcerr << L"    " << L"3. " << exepath << L" --pakrootdir:<pak-root-dir> --imgrootdir:<img-root-dir> --image-type:<png>|<tga>|<bmp> --filename-policy:<byname>|<byid> --fileext-policy:<replace>|<append>|<copy>|<trim> [-V]|[--verbose] [-R]|[--recursive] <sub-pak1> <sub-pak2> ..." << std::endl;
	std::wcerr << std::endl;
	std::wcerr << L"Options:" << std::endl;
	std::wcerr << L"    " <<  L"<pak-full-path>: the full path of the source package." << std::endl;
	std::wcerr << L"    " <<  L"<img-full-path>: the full path of the destination img directory." << std::endl;
	std::wcerr << L"    " <<  L"<pak-root-dir>: the root directory of the source package where the sub-packages be in." << std::endl;
	std::wcerr << L"    " <<  L"<img-root-dir>: the root directory of the img package where the sub-packages be converted into." << std::endl;
	std::wcerr << L"    " <<  L"<png>|<tga>|<bmp>: one of the image type to convert to." << std::endl;
	std::wcerr << L"    " <<  L"--filename-policy: optional. <byname>=0, default. byid=1." << std::endl;
	std::wcerr << L"    " <<  L"--fileext-policy: optional. <replace>=0, default. <append>=1 <copy>=2 <trim>=3." <<std::endl;
	std::wcerr << L"    " <<  L"[-V]|[--verbose]: optional. Verbose output if specified. The default is not. " << std::endl;
	std::wcerr << L"    " <<  L"[-R]|[--recursive] : optional. if specified, sub-directories would be converted recursively." << std::endl;
	std::wcerr << L"    " <<  L"<sub-pak1> <sub-pak2> ... : 1 or more, the package list that to be converted." << std::endl;
	std::wcerr << std::endl;
	std::wcerr << L"Examples:" << std::endl;
	std::wcerr << L"    " << L"1. " << exepath << L" D:\\pak\\icon.pak D:\\pak\\icon_img.pak png" << std::endl;
	std::wcerr << L"    " << L"2. " << exepath << L" --pakrootdir:D:\\pak\\ --imgrootdir:D:\\pak_img\\ tga" << std::endl;
	std::wcerr << L"    " << L"3. " << exepath << L" --pakrootdir:D:\\pak\\ --imgrootdir:D:\\pak_img\\ bmp -R" << std::endl;
	std::wcerr << std::endl;
	std::wcerr << L"Notes: "  << std::endl;
	std::wcerr << L"    " << L"1. if [-R]|[--recursive] be specified, the xml files under the <pak-root-dir> will be copied to the <img-root-dir> also." << std::endl;
	std::wcerr << L"    " << L"2. if image type is not the standard name like above, you can specify the file type like --image-type:pic.png or --image-type:pic.bmp also." << std::endl;
	std::wcerr << L"    " << L"3. " << exepath << L" returns the number of the succeeded translated packages. -1 or 0 means error."<< std::endl;
	
}

static bool is_string_begin_with( const std::wstring& strFull, const std::wstring& strPre )
{
	return 0 == wcsncmp( strFull.c_str(), strPre.c_str(), strPre.size() );
}

static bool is_string_end_with( const std::wstring& strFull, const std::wstring& strExt )
{
	return 0 == _wcsicmp( strFull.c_str() + strFull.size() - strExt.size(), strExt.c_str() );
}

static bool convert( std::wstring pakpath, std::wstring imgpath )
{
	MHZX::CPicPakConverter converter;

	MHZX::CPicPak pak, pak_img;

	if( !pak.Open( pakpath.c_str() ) )
	{
		return false;
	}
		
	if( !pak_img.Open( imgpath.c_str(), true ) )
	{
		return false;
	}

	MHZX::IImageEncoder* pEncoder = MHZX::CImageEncoderFactory::CreateImageEncoder(g_image_type.c_str());
	if( pEncoder == NULL )
	{
		std::wcerr << L"ERROR: No image encoder for image type: " << g_image_type << std::endl;
		return false;
	}

	pEncoder->Init( g_filename_policy );
	
	std::wcout<< L"------BEGIN----- " << pakpath << L" --> " << imgpath << L" IMAGE TYPE:" <<  g_image_type << L" ------" << std::endl;
	bool bRet = converter.Convert( pak, pak_img, pEncoder, g_verbose_flag );
	
	pEncoder->Release();

	std::wcout<< L"------ END ----- " << pakpath << L" --> " << imgpath << L" IMAGE TYPE:" <<  g_image_type << L" ------" << std::endl;

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
	// [-R]|[--recursive]
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

static bool check_arg_set_verbose( const std::wstring& strArg )
{
	// [-V]|[--verbose]
	if( strArg.compare( L"-V" ) == 0 ||
		strArg.compare( L"--verbose" ) == 0 )
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

static bool is_arg_set_image_type( const std::wstring& strArg, std::wstring& rstrValue )
{
	if( strArg.compare( L"bmp" ) == 0 ||
		strArg.compare( L"png" ) == 0 ||
		strArg.compare( L"tga" ) == 0 )
	{
		rstrValue = strArg;
		return true;
	}

	if( parse_argument_key_value( strArg,  L"image-type", rstrValue ) )
	{
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

static bool check_arg_set_imgrootdir( const std::wstring& strArg, std::wstring& rimgrootdir )
{
	return parse_argument_key_value( strArg, L"imgrootdir", rimgrootdir );
}

static bool check_arg_set_filename_policy( const std::wstring& strArg, std::wstring& rfilename_policy)
{
	return parse_argument_key_value( strArg, L"filename-policy", rfilename_policy );
}

static bool check_arg_set_fileext_policy( const std::wstring& strArg, std::wstring& rfileext_policy )
{
	return parse_argument_key_value( strArg, L"fileext-policy", rfileext_policy );
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
	int ForEachPak( const std::wstring& imgDir )
	{
		std::list< std::wstring > ::const_iterator itr = m_vPakList.begin();
		std::list< std::wstring > ::const_iterator iEnd = m_vPakList.end();

		const std::wstring& pakrootpath = GetBasePath();
		const std::wstring& imgrootpath = imgDir;

		int successed = 0;
		for( ; itr != iEnd; ++ itr )
		{
			std::wstring pakpath = pakrootpath + *itr;
			std::wstring imgpath = imgrootpath + *itr;
			successed += !!convert( pakpath, imgpath );
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
	int ForEachMetaFile( const std::wstring& imgDir )
	{
		std::list< std::wstring > ::const_iterator itr = m_vMetaFileList.begin();
		std::list< std::wstring > ::const_iterator iEnd = m_vMetaFileList.end();

		const std::wstring& pakrootpath = GetBasePath();
		const std::wstring& imgrootpath = imgDir;

		int successed = 0;

		for( ; itr != iEnd; ++ itr )
		{
			std::wstring pakpath = pakrootpath + *itr;
			std::wstring imgpath = imgrootpath + *itr;

			successed += ::CopyFileW( pakpath.c_str(), imgpath.c_str(), FALSE );
		}

		return successed;
	}

protected:
	std::list< std::wstring > m_vMetaFileList;
};

static int convert_recursively( const std::wstring& pakrootdir, 
							   const std::wstring& imgrootdir )
{
	CFindPaksDeeply finder( pakrootdir );
	finder.DoFind();

	int successed = finder.ForEachPak( imgrootdir );
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
	
	int successed2 = finder.ForEachMetaFile( imgrootdir );
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

static int convert_in_dir(const std::wstring& pakrootdir,
						  const std::wstring& imgrootdir)
{
	CFindPaksInDir finder( pakrootdir );
	finder.DoFind();
	int successed = finder.ForEachPak( imgrootdir );
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

static int convert_orderly(const std::wstring& pakrootdir,
						   const std::wstring& imgrootdir,
						   const std::vector< std::wstring >& paklist )
{
	std::vector< std::wstring > ::const_iterator itr = paklist.begin();
	std::vector< std::wstring > ::const_iterator iEnd = paklist.end();

	int successed = 0;
	for( ; itr != iEnd; ++ itr )
	{
		std::wstring pakpath = pakrootdir + *itr;
		std::wstring imgpath = imgrootdir + *itr;

		successed += !!convert( pakpath, imgpath );
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
	if( argc < 4 )
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
	else
	{
		unsigned int argflags = 0;

		std::wstring pakrootdir;
		std::wstring imgrootdir;
		
		std::vector< std::wstring > paklist;

		unsigned int _filename_policy = MHZX::COPY_SRC_FILE_NAME;
		unsigned int _fileext_policy = MHZX::REPLACE_SRC_EXTENSION;

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
			
			if( check_arg_set_imgrootdir( argv[arg], imgrootdir ) )
			{
				argflags |= 0x04;
				continue;
			}

			if( is_arg_set_image_type( argv[arg], g_image_type ) )
			{
				argflags |= 0x08;
				continue;
			}

			std::wstring filename_policy_str;
			std::wstring fileext_policy_str;

			if( check_arg_set_filename_policy( argv[arg], filename_policy_str ) )
			{
				argflags |= 0x010;

				if( filename_policy_str.compare( L"byname" ) == 0 || 
					filename_policy_str.compare( L"0" ) == 0 )
				{
					_filename_policy = MHZX::COPY_SRC_FILE_NAME;
				}
				else if( filename_policy_str.compare( L"byid" ) == 0 || 
					filename_policy_str.compare( L"1" ) == 0 )
				{
					_filename_policy = MHZX::NEW_FILE_NAME_BY_ID;
				}
				else
				{
					usage( argv[0] );
					std::wcerr << std::endl << L"ERROR: Invalid filename-policy: " << filename_policy_str << std::endl;
					return -1;
				}

				continue;
			}

			if( check_arg_set_fileext_policy(  argv[arg], fileext_policy_str ) )
			{
				argflags |= 0x020;

				if( fileext_policy_str.compare( L"replace" ) == 0 ||
					fileext_policy_str.compare( L"0" ) == 0 )
				{
					_fileext_policy = MHZX::REPLACE_SRC_EXTENSION;
				}
				else if( fileext_policy_str.compare( L"append" ) == 0 ||
					fileext_policy_str.compare( L"1" ) == 0)
				{
					_fileext_policy = MHZX::APPEND_NEW_EXTENSION;
				}
				else if( fileext_policy_str.compare( L"copy" ) == 0 ||
					fileext_policy_str.compare( L"2" ) == 0 )
				{
					_fileext_policy = MHZX::COPY_SRC_EXTENSION;
				}
				else if( fileext_policy_str.compare( L"trim" ) == 0 ||
					fileext_policy_str.compare( L"3" ) == 0 )
				{
					_fileext_policy = MHZX::TRIM_SRC_EXTENSION;
				}
				else
				{
					usage( argv[0] );
					std::wcerr << std::endl << L"ERROR: Invalid fileext-policy: " << fileext_policy_str << std::endl;
					return -1;
				}

				continue;
			}

			if( check_arg_set_verbose(argv[arg] ) )
			{
				argflags |= 0x040;
				g_verbose_flag = true;
				continue;
			}

			paklist.push_back( argv[arg] );
		}

		if( ( argflags & 0x08 ) == 0 ) // no image type, cannot continue.
		{
			usage( argv[0] );
			std::wcerr << std::endl << L"ERROR: You should specify the image type. " << std::endl;
			return -1;
		}

		g_filename_policy = _filename_policy | _fileext_policy;

		if( ( argflags & 0x0F ) == 0x0F )
		{
			path_check_append_slash(pakrootdir);
			path_check_append_slash(imgrootdir);
			return !!convert_recursively( pakrootdir, imgrootdir );
		}
		else if( ( argflags & 0x0F ) == 0x0E ) // not-recursively
		{
			path_check_append_slash(pakrootdir);
			path_check_append_slash(imgrootdir);
			if( paklist.size() > 0 )
				return !!convert_orderly( pakrootdir, imgrootdir, paklist );
			else
				return !!convert_in_dir( pakrootdir, imgrootdir );
		}
		else if( ( argflags & 0x0F ) == 0x08 )
		{
			int argcount = argc;

			// skip optional arguments
			if( argflags & 0x010 ) argcount --;
			if( argflags & 0x020 ) argcount --;
			if( argflags & 0x040 ) argcount --;

			if( argcount == 4 )
			{
				std::wstring pakrootpath = argv[1];
				std::wstring imgrootpath = argv[2];
				return !!convert( pakrootpath, imgrootpath );
			}
		}
	}

	usage( argv[0] );
	return -1;
}

