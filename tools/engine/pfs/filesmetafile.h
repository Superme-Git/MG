///=============================================================================
/// Product   : PFS
/// Project   : Miscellaneous Files
/// Module    : filesmetafile.h
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	filesmetafile - .files.meta �ļ�����ӿ�
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-10-27 Created.
///=============================================================================

#pragma once
#include "pfs2.h"

namespace PFSX
{
	const std::wstring FILES_META_NAME = L"/.files.meta";
	const std::wstring FILES2_META_NAME = L"/.files2.meta";
	const std::wstring SERVER_META_NAME = L"/.server.meta";
}// namespace PFSX

#include "src/meta/metafileio.h"

namespace PFSX
{
	using PFS::CMetaDataRead;
	using PFS::CMetaDataWrite;
	using PFS::CMetaFileRead;
	using PFS::CMetaFileWrite;

	// ��ָ��Ŀ¼�µ�MetaInfo�ļ�
	inline bool ReadMetaInfoFile( std::wstring path, PFS::CMetaInfoMap& mimFile )
	{
		bool bOK = false;
		PFS::CFile theFile;
		if( theFile.Open( path,	PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			bOK = PFS::CMetaFileRead::DoRead( theFile, mimFile );
		}
		return bOK;
	}

	// ��MetaInfo�ļ�д��ָ��Ŀ¼��
	inline bool WriteMetaInfoFile( std::wstring path, const PFS::CMetaInfoMap& mimFile )
	{
		bool bOK = false;
		PFS::CFile theFile;
		if( theFile.Open( path, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_WRONLY ) )
		{
			bOK = PFS::CMetaFileWrite::DoWriteByVersion( theFile, mimFile, 0x0101 );
		}

		return bOK;
	}

}// namespace PFSX