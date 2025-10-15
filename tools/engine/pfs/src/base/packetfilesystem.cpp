///=============================================================================
/// Product   : PFS
/// Project   : pfslib
/// Module    : packetfilesystem.cpp
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	CPacketFileSystem 相关实现
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-1-6 Created.
/// 	V1.1 - 2010-7-13 Add BaseFilter Implementations.
///=============================================================================
#include "../common.h"
#include "packetfilesystem.h"
#include "pfsbase.h"
#include "../native/native.h"// for CNativeFS
#include "../zip/zip.h"		// for CZipFS

namespace PFS
{
	//
	// 注意: 
	// 这里没有将s_Instance定义为GetInstance()的函数内部局部静态变量，
	// 是为了让该对象尽量早的构造出来.
	// 参见mountmanager.h中的 static CMountManager& Instance()的实现。
	//
	// 其原则是，CMountManager类的静态单件对象析构时，要保证CPacketFileSystem
	// 的单件对象依然没有析构.即CPacketFileSystem单体的生存期要比CMountManager
	// 的单件生存期更长。
	// 当然，为了更加可靠起见，最好在程序退出main函数时显式调用
	//	CMountManager::UnmountPFS()
	// 而不要依赖于CMountManager的析构函数自动Unmount设备。
	// 
	namespace helper
	{
		class CPacketFileSystemWrapper : public CPacketFileSystem
		{
		public:
			CPacketFileSystemWrapper()
			{
			}
		};
		static CPacketFileSystemWrapper s_Instance;
	} // namespace helper

	CPacketFileSystem& CPacketFileSystem::GetInstance()
	{ 
		return helper::s_Instance; 
	}

	namespace PATHHELPER
	{
		//-------------------------------------------------------------------------- 
		// Return a pointer to the end of the next path componenent in the string. 
		// ie return a pointer to the next backslash or terminating NULL. 
		inline LPCTSTR GetPCEnd(LPCTSTR lpszStart) 
		{ 
			LPCTSTR  lpszEnd = StrChr(lpszStart, TEXT('\\')); 
			if (!lpszEnd) 
			{ 
				lpszEnd = lpszStart + lstrlen(lpszStart); 
			} 

			return lpszEnd; 
		} 

		//-------------------------------------------------------------------------- 
		// Given a pointer to the end of a path component, return a pointer to 
		// its begining. 
		// ie return a pointer to the previous backslash (or start of the string). 
		inline LPCTSTR PCStart(LPCTSTR lpszStart, LPCTSTR lpszEnd) 
		{ 
			LPCTSTR lpszBegin = StrRChr(lpszStart, lpszEnd, TEXT('\\')); 
			if (!lpszBegin) 
			{ 
				lpszBegin = lpszStart; 
			} 
			return lpszBegin; 
		} 

		//-------------------------------------------------------------------------- 
		// Canonicalizes a path. 
		// 
		// Windows API PathCanonicalizeA/W has a bug for translating paths like:
		//
		//    "/cfg/f////c.txt"
		//    "/cfg/f/.//../f/c.txt"
		//    "/cfg/f/.\\\\../f/c.txt"
		//    "/cfg/f/.//../f\\c.txt"
		//    "/cfg/f/.\\/../f\\c.txt"
		//    "/cfg/./././f/.\\/../f\\c.txt"
		//    "/cfg/./././f/.\\\\../f\\c.txt"
		//    "/cfg/./././f/.//../f\\c.txt"
		//
		// so, we use the customized one instead of it.
		//
		BOOL _PathCanonicalize(LPTSTR lpszDst, LPCTSTR lpszSrc) 
		{ 
			LPCTSTR lpchSrc = lpszSrc; 
			LPCTSTR lpchPCEnd;           // Pointer to end of path component. 
			LPTSTR lpchDst = lpszDst; 
			BOOL bLastIsSlash = FALSE;

			int cbPC; 

			while (*lpchSrc) 
			{ 
				// this should just return the count 
				lpchPCEnd = GetPCEnd(lpchSrc); 
				cbPC = (lpchPCEnd - lpchSrc)+1; 

				// Check for slashes. 
				if (cbPC == 1 && *lpchSrc == TEXT('\\')) 
				{ 
					// Just copy them. 
					if( !bLastIsSlash )
					{
						*lpchDst = TEXT('\\'); 					
						lpchDst++; 
					}
					lpchSrc++; 

					bLastIsSlash = TRUE;
				} 
				// Check for dots. 
				else if (cbPC == 2 && *lpchSrc == TEXT('.')) 
				{ 
					// Skip it... 
					// Are we at the end? 
					if (*(lpchSrc+1) == TEXT('\0')) 
					{ 
						lpchDst--; 
						lpchSrc++; 
					} 
					else 
						lpchSrc += 2; 

					bLastIsSlash = TRUE;
				} 
				// Check for dot dot. 
				else if (cbPC == 3 && *lpchSrc == TEXT('.') && *(lpchSrc + 1) == TEXT('.')) 
				{ 
					// make sure we aren't already at the root 
					if (!PathIsRoot(lpszDst)) 
					{ 
						// Go up... Remove the previous path component. 
						lpchDst = (LPTSTR)PCStart(lpszDst, lpchDst - 1); 
					} 
					else 
					{ 
						// When we can't back up, remove the trailing backslash 
						// so we don't copy one again. (C:\..\FOO would otherwise 
						// turn into C:\\FOO). 
						if (*(lpchSrc + 2) == TEXT('\\')) 
						{ 
							lpchSrc++; 
						} 
					} 
					lpchSrc += 2;       // skip ".." 

					bLastIsSlash = FALSE;

				} 
				// Everything else 
				else 
				{ 
					// Just copy it. 
					lstrcpyn(lpchDst, lpchSrc, cbPC); 
					lpchDst += cbPC - 1; 
					lpchSrc += cbPC - 1; 
					bLastIsSlash = FALSE;
				} 

				// Keep everything nice and tidy. 
				*lpchDst = TEXT('\0'); 
			} 

			return TRUE; 
		} 
	}

	//
	// Handle the pfs path like these:
	//
	//	1)	"/native//a/b/c"
	//	2)	"/native/\d/e/f"
	//	3)	"/native/./././g/h/i"
	//
	// In above cases, "/native" is a native file system directory.
	//
	// After stripped, the file name would be "/a/b/c", "/d/e/f" and  "/g/h/i",
	// but not "//a/b/c" or "/\d/e/f" or "/./././g/h/i".
	//
	// Test cases for file: /cfg/f/c.txt.
	// Passed ones in both native and zip file system:
	//		TestOpenFile( file, L"/cfg/f/c.txt" );
	//		TestOpenFile( file, L"/cfg/f/./c.txt" );
	//		TestOpenFile( file, L"/cfg/f/././c.txt" );
	//		TestOpenFile( file, L"/cfg/f/.\\../f/c.txt" );
	// The following ones cannot be found in pfs package but can be found in windows native file system.
	// it's bugs of PathCanonicalizeW(). We don't try to fix this behaviors.
	// After canonicalized, the paths would be "/cfg/f/f/c.txt".
	//		TestOpenFile( file, L"/cfg/f/.//../f/c.txt" ); 
	//		TestOpenFile( file, L"/cfg/f/.\\\\../f/c.txt" );
	//		TestOpenFile( file, L"/cfg/f/.//../f\\c.txt" );
	//		TestOpenFile( file, L"/cfg/f/.\\/../f\\c.txt" );
	//
	static void _CanonicalizePath( std::wstring& filename, bool bTransBackslash  )
	{
		// PathCanonicalizeW 函数只对 '\\' 字符有效，
		if( bTransBackslash )
		{
			std::replace( filename.begin(), filename.end(), L'/', L'\\');
		}

		size_t length = filename.size(); 
		wchar_t* buffer = new wchar_t[length+1];
		if ( buffer != NULL )
		{
			// There's BUGS in PathCanonicalizeW(). We give up to use it!
			//::PathCanonicalizeW( buffer, filename.c_str() );
			
			PATHHELPER::_PathCanonicalize( buffer, filename.c_str() );

			filename = buffer;

			delete[] buffer;
		}

		std::replace( filename.begin(), filename.end(), L'\\', L'/');
	}

	//
	// 删除filename前面的basepath，保留base内部的相对路径
	//
	static void _StripBasePath(std::wstring& filename, size_t baselength )
	{
		baselength ++;
		size_t total = filename.size();
		while( baselength < total && ( ( L'/' == filename[baselength] ) ) )
		{
			baselength ++;
		}

		filename.erase( 0, baselength - 1 );

		// assert( CheckDirFileStringFormat( filename ) );
	}

	static void ReformatPFSPath( const std::wstring& filename, std::wstring& filenameNew )
	{
		filenameNew = filename;
		std::replace( filenameNew.begin(), filenameNew.end(), L'\\', L'/');
	}

	static void ReformatNativePath( std::wstring& filename )
	{
		size_t length = filename.size();
		if ( length > 0 )
		{
			std::replace( filename.begin(), filename.end(), L'/', L'\\');
			wchar_t* buffer = new wchar_t[length+1];
			if ( buffer != NULL )
			{
				::PathCanonicalizeW( buffer, filename.c_str() );
				filename = buffer;
				delete[] buffer;
			} // if
		} // if
	}

	static void MetaInfoFilterWithCat( const std::wstring& sub, const CMetaInfoMap& src, CMetaInfoMap& mim)
	{
		std::wstring	subwithend = sub + L"/";
		for( CMetaInfoMap::const_iterator it = src.begin(); it != src.end(); ++ it)
		{
			const std::wstring& strname = it->first;
			const CMetaInfo& metainfo = it->second;

			if( 0 == wcsncmp( subwithend.c_str(), strname.c_str(), subwithend.length()))
			{
				std::wstring newname = strname.substr( sub.length());
				mim.insert( std::make_pair( newname, metainfo));
			} // if
		} // for
	}

	static void MetaInfoFilterWithAppend( const std::wstring& prefix, const CMetaInfoMap& src, CMetaInfoMap& mim)
	{
		for( CMetaInfoMap::const_iterator it = src.begin(); it != src.end(); ++ it)
		{
			const std::wstring& strname = it->first;
			const CMetaInfo& metainfo = it->second;
			std::wstring		newname = prefix + strname;
			mim.insert( std::make_pair( newname, metainfo));
		} // for
	}

} // namespace PFS

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// CPacketFileSystem - implementations
//

namespace PFS
{
//////////////////////////////////////////////////////////////////////////
// private:

void CPacketFileSystem::GetMetaInfosEx( const std::wstring& dir, const std::wstring& point, CPFSBase& pfs, CMetaInfoMap& mim)
{
	if( dir.length() == point.length())
	{
		if( 0 != wmemcmp( dir.c_str(), point.c_str(), dir.length()))
			return;
		const CMetaInfoMap& src = pfs.GetMetaInfoMap();
		mim.insert( src.begin(), src.end());
		return;
	} // if

	if( dir.length() < point.length())
	{
		std::wstring	dirwithend = dir + L"/";
		if( 0 != wcsncmp( dirwithend.c_str(), point.c_str(), dirwithend.length()))
			return;
		const CMetaInfoMap& src = pfs.GetMetaInfoMap();
		std::wstring sub = point.substr( dir.length());
		MetaInfoFilterWithAppend( sub, src, mim);
		return;
	}//if

	assert( dir.length() > point.length());
	{
		std::wstring	pointwithend = point + L"/";
		if( 0 != wcsncmp( pointwithend.c_str(), dir.c_str(), pointwithend.length()))
			return;
		const CMetaInfoMap& src = pfs.GetMetaInfoMap();
		std::wstring sub = dir.substr( point.length());
		MetaInfoFilterWithCat( sub, src, mim);
	}
}

void CPacketFileSystem::TryNativeFileNameToPFSFileName( const std::wstring& filename, std::wstring& NewFileName)
{
	ReformatPFSPath( filename, NewFileName);

	for( CMountPoints::const_iterator it = m_MountPoints.begin(); it != m_MountPoints.end(); ++ it)
	{
		const std::wstring& mp = it->first;
		const CPFSBase* pfsb = it->second;

		if( FST_NATIVE != pfsb->GetFSType())
			continue;

		const CNativeFS* nfs = dynamic_cast<const CNativeFS*>( pfsb);
		if( NULL == nfs)
			continue;

		std::wstring basepath = nfs->GetBasePath();
		std::wstring newbasepath;
		ReformatPFSPath( basepath, newbasepath );
		if( NewFileName.size() < newbasepath.size())
			continue;
		if( 0 != _wcsnicmp( NewFileName.c_str(), newbasepath.c_str(), newbasepath.size()))
			continue;
		if( NewFileName[newbasepath.size()] != L'/')	
			continue;
		std::wstring pfsfilename = mp + NewFileName.substr( newbasepath.size());
		SetLastError( EC_SUCCESS);
		TryNativeFileNameToPFSFileName( pfsfilename, NewFileName);
		return;
	} // for
}

void CPacketFileSystem::TryPFSFileNameToNativeFileName( const std::wstring& filename, std::wstring& NewFileName)
{
	std::wstring tempname = filename;
	CPFSBase* pfsb = FindPFSBase( tempname);
	if( NULL == pfsb)
		return;

	if( tempname.empty() )
	{
		NewFileName = pfsb->GetNativeDevicePath();
		SetLastError( EC_SUCCESS);
		return;
	}

	CNativeFS* nfs = dynamic_cast<CNativeFS*>( pfsb);
	if( NULL == nfs)
	{
		return;
	}

	NewFileName = nfs->GetBasePath() + tempname;
	SetLastError( EC_SUCCESS);
	TryPFSFileNameToNativeFileName( NewFileName, NewFileName);
}

void CPacketFileSystem::NativeFileNameToPFSFileName( const std::wstring& in, std::wstring& out)
{
	SetLastError( EC_MAP_UNFOUND);
	TryNativeFileNameToPFSFileName( in, out);
	if ( GetLastError() == EC_SUCCESS )
	{
		std::transform( out.begin(), out.end(), out.begin(), ::towlower);
	}
}

void CPacketFileSystem::PFSFileNameToNativeFileName( const std::wstring& in, std::wstring& out)
{
	SetLastError( EC_MAP_UNFOUND);
	TryPFSFileNameToNativeFileName( in, out);
	if ( GetLastError() == EC_SUCCESS )
	{
		ReformatNativePath( out );
	}
}

bool CPacketFileSystem::IsRepeatMountZipFile( const std::wstring& device, MOUNT_TYPE mt)
{
	std::wstring srcfile;
	PFSFileNameToNativeFileName( device, srcfile);
	if ( srcfile.empty() )
		srcfile = device;

	CMountPoints::const_iterator itor = m_MountPoints.begin();
	for( ; itor != m_MountPoints.end(); ++itor)
	{
		std::wstring desfile = itor->second->GetNativeDevicePath();
		std::replace( desfile.begin(), desfile.end(), L'/', L'\\');
		if( _wcsicmp( srcfile.c_str(), desfile.c_str() ) == 0 )
			if( !IsMountReadOnly(itor->second->GetMountType()) || !IsMountReadOnly(mt))
				return true;
	}

	return false;
}

CPFSBase* CPacketFileSystem::FindPFSBaseInOriginal( std::wstring& filename )
{
	for( CMountPoints::const_iterator it = m_MountPoints.begin(); it != m_MountPoints.end(); ++ it)
	{	
		const std::wstring& dir = it->first;
		if( dir.size() > filename.size())
			continue;
		int n = wcsncmp( dir.c_str(), filename.c_str(), dir.size() );
		if( n != 0 )
			continue;
		
		wchar_t ch = filename[dir.size()];

		if( ch != L'/' && ch != L'\\' && ch != L'\0' )
			continue;

		_StripBasePath( filename, dir.size() );
		return it->second;
	} // for

	return NULL;
}

CPacketFileSystem::CMountPoints::const_iterator CPacketFileSystem::FindDeepestBasePoint( std::wstring& path )
{
	std::wstring basedir = path;
	CMountPoints::const_iterator itr = m_MountPoints.end();
	while(  !basedir.empty() )
	{
		itr = m_MountPoints.find( basedir );
		if ( itr != m_MountPoints.end() )
		{
			break;
		}

		struct _isPathSep
		{
			bool operator()( std::wstring::const_reference r )
			{
				return r == L'/' || r == L'\\';
			}
		};

		std::wstring::const_reverse_iterator ip = std::find_if(basedir.rbegin(), basedir.rend(), _isPathSep() );

		if( ip != basedir.rend() )
		{
			basedir.resize( basedir.rend() - ip - 1 );
		}
		else
		{
			break;
		}
	}

	if( itr != m_MountPoints.end() )
		path = path.substr( basedir.size() );
	return itr;
}


CPFSBase* CPacketFileSystem::FindPFSBaseUseFilter( std::wstring& filename, bool& bOldBaseUsed )
{
	// try to match the base filter first!
	for( CBaseFilter::const_iterator it = m_BaseFilter.begin(); it != m_BaseFilter.end(); ++ it)
	{	
		const std::wstring& dir = it->first;
		if( dir.size() > filename.size())
			continue;
		int n = wcsncmp( dir.c_str(), filename.c_str(), dir.size() );
		if(  n != 0 )
			continue;
		wchar_t ch = filename[dir.size()];
		if( ch != L'/'&& ch != L'\\' && ch != L'\0' )
			continue;
		
		// match one item !
		std::wstring filter = it->second;
		CMountPoints::const_iterator itNewBase = FindDeepestBasePoint( filter );
		if( itNewBase == m_MountPoints.end() )
		{
			// No such new base! Configuration  ERROR!
			// try to use old one.
			CMountPoints::const_iterator itOldBase = m_MountPoints.find( dir );
			if( itOldBase == m_MountPoints.end() )
			{
				bOldBaseUsed = false;
				// no find in both old and new base.
				return NULL;
			}
			else
			{
				bOldBaseUsed = true;
				// find in old base.
				_StripBasePath( filename, dir.size() );
				return itOldBase->second;
			}
		}
		else
		{
			bOldBaseUsed = false;
			// use the new base;
			_StripBasePath( filename, dir.size() );
			filename = filter + filename;
			return itNewBase->second;
		}
	} // for

	return NULL;
}

//////////////////////////////////////////////////////////////////////////
// public:

CPFSBase* CPacketFileSystem::FindPFSBase( std::wstring& filename )
{
	if( m_BaseFilterFlag )
	{
		// use filter first.
		std::wstring		newname = filename;
		
		_CanonicalizePath( newname, true );

		bool bOldBaseUsed = false;
		CPFSBase* pbase = FindPFSBaseUseFilter( newname, bOldBaseUsed );
		if( NULL != pbase )
		{
			if( !bOldBaseUsed )
			{
				if( pbase->IsFileExisting( newname ) )
				{
					filename = newname;
					return pbase;
				}
				// else, not exists in the new base.
				// pass through...
			}
			else
			{
				// the base is the original one.
				filename = newname;
				return pbase;
			}
		}
	}
	else
	{
		_CanonicalizePath( filename, false );
	}

	// if not find in filterring base, find it in original base.
	CPFSBase* pbase = FindPFSBaseInOriginal( filename );
	return pbase;
}

int CPacketFileSystem::Mount( const std::wstring& dir, const std::wstring& device, FS_TYPE fst, MOUNT_TYPE mt)
{
	if( m_MountPoints.find( dir) != m_MountPoints.end())
		return EC_DIR_HAS_ALREADY_MOUNT;

	if( (FST_ZIP == fst||FST_RUNZIP == fst) && IsRepeatMountZipFile(device, mt))	// 只能同时以只读方式mount一个zip文件
	{
		return EC_FS_BUSY_NOW;
	}

	CPFSBase*	base = CPFSBase::Create( fst);
	if( NULL == base)
		return EC_FSTYPE_UNEXISTING;

	MOUNT_TYPE mt2 = mt & ~MT_OPTIONAL;
	int code = base->Initialize( device, mt2 );
	if( code == EC_SUCCESS)
	{
		// 设置缺省的FilesMetaVersion
		base->SetFilesMetaVersion( m_usFilesMetaDefaultVersion );
		base->SetMountPoint( dir);
		m_MountPoints.insert( std::make_pair( dir, base));
		return EC_SUCCESS;
	}
	base->Delete();
	return code;
}

int CPacketFileSystem::Commit( const std::wstring& dir )
{
	CMountPoints::iterator it = m_MountPoints.find( dir);
	if( it == m_MountPoints.end())
		return EC_MOUNT_DIR_UNFOUND;
	CPFSBase* base = it->second;
	if( !base->Commit() )
		return CEnv::GetLastError();
	return EC_SUCCESS;
}

int CPacketFileSystem::Unmount( const std::wstring& dir, bool commit)
{
	CMountPoints::iterator it = m_MountPoints.find( dir);
	if( it == m_MountPoints.end())
		return EC_MOUNT_DIR_UNFOUND;
	CPFSBase* base = it->second;
	if( base->IsFSMountReference() || base->IsFSBusy())
		return EC_FS_BUSY_NOW;
	if( !base->Destroy( commit ))
		return EC_UNMOUNT_FAILED;
	m_MountPoints.erase( it);
	base->Delete();
	return EC_SUCCESS;
}

int CPacketFileSystem::Open( const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa, CBaseFile*& file)
{
	std::wstring		newname = filename;
	CPFSBase* pbase = FindPFSBase( newname);
	if( NULL == pbase)
		return EC_FILE_NOT_MOUNT;
	return pbase->OpenFile( newname, fm, fa, file);
}

int CPacketFileSystem::GetMetaInfos( const std::wstring& dir, CMetaInfoMap& mim)
{
	CMountPoints::iterator it = m_MountPoints.find( dir);
	if( it == m_MountPoints.end())
		return EC_MOUNT_DIR_UNFOUND;
	CPFSBase* base = it->second;
	mim = base->GetMetaInfoMap(); // 注意这里的 copy 操作
	return EC_SUCCESS;
}

int CPacketFileSystem::SetMetaInfos( const std::wstring& dir, CMetaInfoMap& mim)
{
	CMountPoints::iterator it = m_MountPoints.find( dir);
	if( it == m_MountPoints.end())
		return EC_MOUNT_DIR_UNFOUND;
	CPFSBase* base = it->second;
	mim.swap( base->GetMetaInfoMap() ); // 注意这里的swap操作
	return EC_SUCCESS;
}

bool CPacketFileSystem::GetMetaInfosEx( const std::wstring& dir, CMetaInfoMap& mim)
{
	for( CMountPoints::const_iterator it = m_MountPoints.begin(); it != m_MountPoints.end(); ++ it)
		GetMetaInfosEx( dir, it->first, *it->second, mim);
	return true;
}

int CPacketFileSystem::CheckMetaInfo( const std::wstring& dir)
{
	CMountPoints::iterator it = m_MountPoints.find( dir);
	if( it == m_MountPoints.end())
		return EC_MOUNT_DIR_UNFOUND;
	CPFSBase* base = it->second;
	base->CheckMetaInfo();
	return EC_SUCCESS;
}

int CPacketFileSystem::GetFileMetaInfo( const std::wstring& filename, CMetaInfo& info)
{
	std::wstring		newname = filename;
	CPFSBase* pbase = FindPFSBase( newname);
	if( NULL == pbase)
		return EC_FILE_NOT_MOUNT;
	return pbase->GetFileMetaInfo( newname, info);
}

int CPacketFileSystem::SetFileMetaInfo( const std::wstring& filename, const CMetaInfo& info)
{
	std::wstring		newname = filename;
	CPFSBase* pbase = FindPFSBase( newname);
	if( NULL == pbase)
		return EC_FILE_NOT_MOUNT;
	return pbase->SetFileMetaInfo( newname, info);
}

int CPacketFileSystem::AddFileMetaInfo( const std::wstring& filename, const CMetaInfo& info)
{
	std::wstring		newname = filename;
	CPFSBase* pbase = FindPFSBase( newname);
	if( NULL == pbase)
		return EC_FILE_NOT_MOUNT;
	return pbase->AddFileMetaInfo( newname, info);
}

int CPacketFileSystem::RemoveFile( const std::wstring& filename)
{
	std::wstring		newname = filename;
	CPFSBase* pbase = FindPFSBase( newname);
	if( NULL == pbase)
		return EC_FILE_NOT_MOUNT;
	return pbase->RemoveFile( newname);
}

bool CPacketFileSystem::IsFileExisting( const std::wstring& filename)
{
	std::wstring		newname = filename;
	CPFSBase* pbase = FindPFSBase( newname);
	if( NULL == pbase)
		return false;
	return pbase->IsFileExisting( newname);
}

// 刷新目录 dir 的 files.meta 文件
bool CPacketFileSystem::RefreshMetaFile( const std::wstring& dir)
{
	CMountPoints::iterator it = m_MountPoints.find( dir);
	if( it == m_MountPoints.end())
	{
		SetLastError( EC_MOUNT_DIR_UNFOUND);
		return false;
	}

	return it->second->RefreshMetaFile();
}

// 得到目录 dir 的 files.meta 文件的hash值
bool CPacketFileSystem::GetMetaFileHash( const std::wstring& dir, FILE_DATA_HASH& res)
{
	CMountPoints::iterator it = m_MountPoints.find( dir);
	if( it == m_MountPoints.end())
	{
		SetLastError( EC_MOUNT_DIR_UNFOUND);
		return false;
	}

	return it->second->GetMetaFileHash( res);
}

// 设置base目录的优先替换目录(过滤器方式)
bool CPacketFileSystem::SetBaseFilter( const std::wstring& baseDirOriginal, const std::wstring& baseDirNew )
{
	CMountPoints::iterator it = m_MountPoints.find( baseDirOriginal );
	if( it == m_MountPoints.end())
	{
		SetLastError( EC_FILTER_INVALID_ORI_DIR );
		return false;
	}

	if( baseDirOriginal == baseDirNew )
	{
		SetLastError( EC_FILTER_INVALID_NEW_DIR );
		return false;
	}

	m_BaseFilter[ baseDirOriginal ] = baseDirNew;

	return true;
}

// 获取base目录的优先替换目录(过滤器方式)
bool CPacketFileSystem::GetBaseFilter( const std::wstring& baseDirOriginal, std::wstring& baseDirNew ) const
{
	CBaseFilter::const_iterator itrFilter = m_BaseFilter.find( baseDirOriginal );
	if( itrFilter == m_BaseFilter.end() )
	{
		baseDirNew.clear();
		SetLastError( EC_FILTER_ITEM_NOT_EXISTS );
		return false;
	}
	else
	{
		baseDirNew = itrFilter->second;
		return true;
	}
}

bool CPacketFileSystem::SetBaseFilesMetaVersion( const std::wstring & baseDir, const std::wstring& versionString )
{
	wchar_t *pNext = NULL;
	unsigned short versionMajor = ((unsigned short)wcstoul(versionString.c_str(), &pNext, 10));
	if( versionMajor != 0x01 )
	{
		SetLastError( EC_OPTION_INVALID_VALUE );
		return false;
	}

	if( pNext[0] != L'.' )
	{
		SetLastError( EC_OPTION_INVALID_VALUE );
		return false;
	}

	unsigned short usVersion = ((unsigned short)wcstoul(++pNext, NULL, 10));
	if( usVersion != 0x00 && usVersion != 0x01 ) 
	{
		SetLastError( EC_OPTION_INVALID_VALUE );
		return false;
	}

	usVersion |= versionMajor << 8;
	
	if( baseDir.empty() || baseDir == L"/" )
	{
		m_usFilesMetaDefaultVersion = usVersion;
		return true;
	}
	else
	{
		CMountPoints::iterator it = m_MountPoints.find( baseDir);
		if( it == m_MountPoints.end())
		{
			SetLastError( EC_OPTION_INVALID_NAME );
			return false;
		}

		return it->second->SetFilesMetaVersion( usVersion );
	}
}

bool CPacketFileSystem::GetBaseFilesMetaVersion( const std::wstring& baseDir, std::wstring& rVersionString ) const
{		
	if( baseDir.empty() || baseDir == L"/" )
	{
		std::wostringstream woss(rVersionString);
		woss << ((m_usFilesMetaDefaultVersion & 0x0FF00) >> 8 ) << L"." << (m_usFilesMetaDefaultVersion & 0x0FF);
		woss.flush();
		return true;
	}
	else
	{
		CMountPoints::const_iterator it = m_MountPoints.find( baseDir );
		if( it == m_MountPoints.end())
		{
			SetLastError( EC_OPTION_INVALID_NAME );
			return false;
		}

		unsigned short usVersion = it->second->GetFilesMetaVersion();
 
		std::wostringstream woss;
		woss << ((usVersion & 0x0FF00) >> 8 ) << L"." << (usVersion & 0xFF);
		woss.flush();
		rVersionString = woss.str();
		return true;
	}
}

void CPacketFileSystem::DumpInfos( COutLog& outlog)
{
	for( CMountPoints::const_iterator it = m_MountPoints.begin(); it != m_MountPoints.end(); ++ it)
	{	
		const std::wstring& dir = it->first;
		CPFSBase* base = it->second;
		outlog.Print( L"DumpInfos moutpoint = %s\n", dir.c_str());
		base->DumpInfos( outlog);
		outlog.Print( L"\n");
	} // for
}

} // namespace PFS