///=============================================================================
/// Product   : PFS
/// Project   : pfslib
/// Module    : pfsbase.cpp
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	CPFSBase ���ʵ��
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-1-6 Created.
///=============================================================================
#include "../common.h"
#include "pfsbase.h"

#include "../native/native.h"	// for CNativeFS
#include "../zip/zip.h"			// for CZipFS
#include "../brief/brief.h"		// for CBriefFS
#include "../runzip/runzip.h"	// for CRunZipFS
#include "../utility/md5hash.h"

namespace PFS
{
	// ע�⣺������뱣֤mapBases��helper��3���������Ⱥ�˳��
	typedef std::map<FS_TYPE,CPFSBase*> CPFSBaseMap;
	static CPFSBaseMap g_mapBases;

	namespace helper
	{
		static CNativeFS		g_dummy( NULL);
		static CZipFS			g_zip( NULL);
		static CBriefFS			g_brief( NULL);
		static CRunZipFS		g_runzip( NULL);
	}

	CPFSBase::CPFSBase( FS_TYPE type) 
		: m_BasePFS(NULL)
		, m_bMetaChanged( false) 
		, m_usFilesMetaVersion(0x0100)
	{
		g_mapBases.insert( std::make_pair( type, this));
	}

	CPFSBase* CPFSBase::Create( FS_TYPE type)
	{
		CPFSBaseMap::const_iterator it = g_mapBases.find( type);
		if( it == g_mapBases.end())
			return NULL;
		const CPFSBase* ps = it->second;
		return ps->Clone();
	}

	std::wstring CPFSBase::_CalcNativeDevicePathForChildPFS(const std::wstring& subDevice)
	{
		if ( subDevice.compare( 0, m_mountpoint.size(), m_mountpoint ) == 0 )
		{
			return GetNativeFilePath( subDevice.substr( m_mountpoint.size(), -1 ) );
		}
		else
		{
			return std::wstring();
		}
	}

	// ����ݹ�����PFS�Ĵ����豸ȫ·��
	void CPFSBase::CalcNativeDevicePath()
	{
		if( m_BasePFS == NULL )
		{
			m_nativeDevicePath = m_deviceName;
			std::replace( m_nativeDevicePath.begin(), m_nativeDevicePath.end(), L'\\', L'/' );
		}
		else
		{
			m_nativeDevicePath = m_BasePFS->_CalcNativeDevicePathForChildPFS( m_deviceName );
		}
	}
	
	// Ϊָ�����ļ��������ڱ�PFS�е�NativeFilePath
	std::wstring CPFSBase::GetNativeFilePath( const std::wstring& rFileName ) const 
	{
		if( !rFileName.empty() )
		{
			if ( rFileName[0] == L'/' || rFileName[0] == L'\\' )
			{
				return GetNativeDevicePath() + rFileName;
			}
			else
			{
				return GetNativeDevicePath() + L'/' + rFileName;
			}
		}

		return GetNativeDevicePath();
	}

	bool CPFSBase::Commit( )
	{
		// Ϊ�˱���Meta�ļ��ظ�д�����⣬�����m_bMetaChanged�����жϡ�
		if( m_bMetaChanged )
		{
			return RefreshMetaFile();
		}

		return true;
	}

	bool CPFSBase::Destroy( bool commit )
	{
		if( commit )
		{
			if( ! CPFSBase::Commit() )
			{
				return false;
			}

			// Commit�ɹ�ʱ��MetaChanged��Ǳ�ȻӦ��Ϊfalse
			assert( m_bMetaChanged == false );
		}

		return true;
	}

	bool CPFSBase::RefreshMetaFile()
	{
		if( IsMountReadOnly( m_MountType ) )
		{
			// ���ﷵ��false�Ļ�readonly��ϵͳ��unmountʧ��
			m_bMetaChanged = false;
			return true;
		}

		// ������ִ��ʱ�������m_bMetaChanged��ǣ�������ǿ��ˢ��
		bool res = false;
		CBaseFile* file = NULL;
		int nErrorCode = OpenFile( GetMetaFileName(), FM_CREAT|FM_TRUNC, FA_WRONLY, file);
		if( file )
		{
			m_MetaInfos.erase( GetMetaFileName());

			// GetFilesMetaVersion() ��ȡ���õİ汾��Ϣ��
			if( CMetaFileWrite::DoWriteByVersion( *file, m_MetaInfos, GetFilesMetaVersion())) 
			{
				res = true;
				m_bMetaChanged = false;// д�ɹ������metaChanged = false.
			}
			else
			{
				CEnv::GetOutLogInstance()->Print( L"Save files.meta error DeviceName = %s (%d)\n", m_deviceName.c_str(), CEnv::GetLastError());
			}
			CEnv::CloseFile( file);
		}
		else
		{
			CEnv::SetLastError( nErrorCode );
			CEnv::GetOutLogInstance()->Print( L"Open files.meta for write error DeviceName = %s (%d)\n", m_deviceName.c_str(), nErrorCode );
		}

		return res;
	}

	// �õ�Ŀ¼ dir �� files.meta �ļ���hashֵ
	bool CPFSBase::GetMetaFileHash( FILE_DATA_HASH& hash )
	{
		bool res = false;
		CBaseFile* file = NULL;
		int nErrorCode = OpenFile( GetMetaFileName(), FM_EXCL, FA_RDONLY, file);
		if( file)
		{
			helper::CMD5FileHashMethod md5Method;
			if( md5Method.MakeFileHash( *file, hash))
			{
				res = true;
			}
			else
			{
				CEnv::GetOutLogInstance()->Print( L"MakeFileHash files.meta error DeviceName = %s (%d)\n", m_deviceName.c_str(), CEnv::GetLastError());
			}
			CEnv::CloseFile( file);
		}
		else
		{
			CEnv::SetLastError( nErrorCode );
			CEnv::GetOutLogInstance()->Print( L"Open files.meta for read error DeviceName = %s (%d)\n", m_deviceName.c_str(), nErrorCode );
		}

		return res;
	}

	void CPFSBase::DumpBaseInfos( COutLog& outlog) const
	{
		outlog.Print( L"m_mountpoint = '%s' m_deviceName = '%s' m_nativeDevicePath = '%s' m_MountType = %d(%08x) m_usFilesMetaVersion = %d(%04x) m_bMetaChanged = %s", m_mountpoint.c_str(), m_deviceName.c_str(), m_nativeDevicePath.c_str(), m_MountType, m_MountType, m_usFilesMetaVersion, m_usFilesMetaVersion, m_bMetaChanged ? L"true" : L"false");
	}

	void CPFSBase::DumpInfos( COutLog& outlog) const
	{
		outlog.Print( L"\tCPFSBase = { ");
		DumpBaseInfos( outlog);
		outlog.Print( L" }\n");
		
		if( NULL != m_BasePFS)
		{
			outlog.Print( L"\tm_BasePFS = { ");
			m_BasePFS->DumpBaseInfos( outlog);
			outlog.Print( L"}\n");
		}
		if( !m_rmfs.empty())
		{
			outlog.Print( L"\tm_rmfs = { ");
			for( CRefMountFSSet::const_iterator it = m_rmfs.begin(), ite = m_rmfs.end(); it != ite; ++ it)
			{
				const std::wstring& device = *it;
				outlog.Print( L"'%s' ", device.c_str());
			}
			outlog.Print( L"}\n");
		}

		if( !m_MetaInfos.empty())
		{
			outlog.Print( L"\tm_MetaInfos = {\n");
			for( CMetaInfoMap::const_iterator it = m_MetaInfos.begin(), ite = m_MetaInfos.end(); it != ite; ++ it)
			{
				const std::wstring& fname = it->first;
				const CMetaInfo& info = it->second;
				outlog.Print( L"\t\t{ filename = '%s' ", fname.c_str());
				info.DumpInfo( outlog);
				outlog.Print( L" }\n");
			}
			outlog.Print( L"\t}\n");
		}
	}

} // namespace PFS {
