///=============================================================================
/// Product   : PFS
/// Project   : pfslib
/// Module    : FileEx.cpp
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	CFile 相关实现函数
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-3-19 Created.
///=============================================================================
#include "../common.h"
#include "../../fileex.h"
#include "../../nativefile.h"

namespace PFS
{
	bool CFile::Open( const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa)
	{
		assert( NULL == m_file);

		if ( filename.empty() )
		{
			CEnv::SetLastError( EC_INVALID_FILENAME );
			assert( !filename.empty() );
			return false;
		}

		if ( filename[0] == L'/' )
		{
			m_file = CEnv::OpenFile( filename, fm, fa);
		}
		else
		{
			// 尝试以Native方式打开文件
			PFSX::CNativeFile* pFile = new PFSX::CNativeFile;
			if ( pFile == NULL )
			{
				CEnv::SetLastError( ERROR_OUTOFMEMORY );
				return false;
			}

			if( pFile->Open( filename, fm, fa ) )
				m_file = pFile;
			else 
				delete pFile;
		}

		return NULL != m_file;
	}

	void CFile::Close() 
	{ 
		if( m_file != NULL ) 
		{
			CEnv::CloseFile( m_file); 
			m_file = NULL; 
		}
	}
	
	//////////////////////////////////////////////////////////////////////////
	//
	// 静态函数实现
	//
	//////////////////////////////////////////////////////////////////////////

	bool CFile::IsFileExisting( const std::wstring& filename )
	{
		if ( filename.empty() )
		{
			CEnv::SetLastError( EC_INVALID_FILENAME );
			assert( !filename.empty() );
			return false;
		}

		if ( filename[0] == L'/' )
		{
			return CEnv::IsFileExisting( filename );
		}
		else
		{
			// 说明: CFileIO中用如下方式实现IsFileExisting
			// 看上去似乎可以，但似乎有问题。期待更严谨的测试!
			DWORD	dwAttribute = GetFileAttributesW( filename.c_str());
			if( dwAttribute == INVALID_FILE_ATTRIBUTES )
				return false;
			return !( dwAttribute & FILE_ATTRIBUTE_DIRECTORY);
		}
	}

	bool CFile::RemoveFile( const std::wstring& filename )
	{
		if ( filename.empty() )
		{
			CEnv::SetLastError( EC_INVALID_FILENAME );
			assert( !filename.empty() );
			return false;
		}

		if ( filename[0] == L'/' )
		{
			return CEnv::RemoveFile( filename );
		}
		else
		{
			BOOL bOK = ::DeleteFileW( filename.c_str() );
			if ( !bOK)
			{
				CEnv::SetLastError( ::GetLastError() );
			}

			return !!bOK;
		}
	}

#pragma push_macro("CopyFile")
#if defined CopyFile
#undef CopyFile 
#endif // CopyFile

	bool CFile::CopyFile( const std::wstring& src, const std::wstring& dst, bool failifexsting )
	{
		if ( src.empty() || dst.empty() )
		{
			CEnv::SetLastError( EC_INVALID_FILENAME );
			assert( !src.empty() && !dst.empty() );
			return false;
		}

		if ( src[0] == L'/' && dst[0] == L'/' )
		{
			// 只要2个都是pfs路径，使用PFS方式执行FileCopy()，它同时会实现必要的MetaInfo复制。
			return CEnv::FileCopy( src, dst, failifexsting );
		}
		else if( src[0] == L'/' ||  dst[0] == L'/' )
		{
			if( failifexsting && CFile::IsFileExisting( dst ) )
			{
				SetLastError( EC_COPY_DST_EXISTING);
				return false;
			}

			// 如果只有1个是pfs路径，使用CFile实现兼容处理
			CFile srcFile;
			if( !srcFile.Open( src,  FM_EXCL, FA_RDONLY) )
			{
				return false;
			}

			CFile dstFile;
			if( !dstFile.Open( dst, FM_CREAT | FM_TRUNC, FA_WRONLY) )
			{
				return false;
			}

			{// BLOCK for szBuffer
				const int		nBufferSize = 4096;
				unsigned char	szBuffer[nBufferSize];
				while( size_t s = srcFile.Read( szBuffer, nBufferSize) )
				{
					if( FILE_SIZE_ERROR == s )
						return false;
					s = dstFile.Write( szBuffer, s );
					if( FILE_SIZE_ERROR == s )
						return false;
				}
			}

			return true;
		}
		else
		{
			// 2个路径都是Native，使用API优化操作
			BOOL  bOK = ::CopyFileW( src.c_str(), dst.c_str(), failifexsting ? TRUE : FALSE );
			if ( !bOK)
			{
				CEnv::SetLastError( ::GetLastError() );
			}

			return !!bOK;
		}
	}

	bool CFile::CopyFileW( const std::wstring& src, const std::wstring& dst, bool failifexsting )
	{
		return CopyFile( src, dst, failifexsting );
	}

#pragma pop_macro("CopyFile")

}
