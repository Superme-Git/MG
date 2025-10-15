#pragma once
#include "BrowseLastPfsPathThread.h"
#include "../packzip/basethread.h"
#include "../../src/zip/zipfile.h"
#include "GlobalFunction.h"

// �ϲ�������߳�
class CMergePatchThread : public CBaseThread
{
	enum { BUFFERSIZE = 65536 };// 64K

	std::wstring m_extension;				// ��������׺

	CFileInfoMap		m_lastpfss;
	std::wstring		m_curpfs;
	std::wstring		m_savefilepath;
	PFS::CDataBlock		m_buffer;
	PFSX::CVersionInfo	m_curVersion;
	PFS::CMetaInfoMap	m_curmetamap;

	typedef std::map<DWORD, std::wstring, std::greater<DWORD> >	CVersionMountMap;
	CVersionMountMap	m_versionMap;

	PFSX::CNativeFile	m_savefile;
	PFS::CPFSWriter		m_zipwritter;

	PFS::CMetaInfoMap	m_updateMap;
	PFS::CMetaInfoMap	m_deleteMap;

	void OutputString( const wchar_t* lpszFormat, ...)
	{
		wchar_t szBuffer[1024] = {0};
		va_list args;
		va_start(args, lpszFormat);
		int res = _vsnwprintf_s( szBuffer, _countof(szBuffer), lpszFormat, args);
		va_end(args);
		if( res > 0)
		{
			CErrorStringArray::GetInstance().Push( szBuffer);
			if( m_listener)
			{
				m_listener->OnNotify( INFO, 0);
			}
		}
		else
		{
			assert( false);
		}
	}

	class CMergePatchError{};
	void Init( )
	{
		OutputString( L"������ǰ�汾��װ�� %s ", m_curpfs.c_str());
		if( !PFS::CEnv::Mount( CUR, m_curpfs, PFS::FST_RUNZIP, PFS::MT_READONLY))
		{
			OutputString( L"ʧ�ܣ���ǰ�汾��װ�� %s ������Ч��pfs�ļ�", m_curpfs.c_str());
			throw CMergePatchError();
		}

		OutputString( L"��õ�ǰ�汾��װ�� %s ��meta��Ϣ ", m_curpfs.c_str());
		if( !PFS::CEnv::GetMetaInfos( CUR, m_curmetamap))
		{
			OutputString( L"ʧ�ܣ���õ�ǰ�汾��װ�� %s ��meta��Ϣ����", m_curpfs.c_str());
			throw CMergePatchError();
		}

		m_curVersion.Close();
		if( !PFSX::GetLocalVersion( CUR, m_curVersion))
		{
			OutputString( L"ʧ�ܣ���õ�ǰ�汾��װ�� %s �İ汾��Ϣ����", m_curpfs.c_str());
			throw CMergePatchError();
		}

		DWORD maxVersion = 0, minVersion = 0;
		for( CFileInfoMap::iterator i=m_lastpfss.begin(), e=m_lastpfss.end(); i!=e; ++i)
		{
			const CFileInfo& fi = i->second;
			if( maxVersion == 0)
			{
				maxVersion = fi.m_version;
			}
			else if( maxVersion < fi.m_version)
			{
				maxVersion = fi.m_version;
			}
			if( minVersion == 0)
			{
				minVersion = fi.m_lastVersion;
			}
			else if( minVersion > fi.m_lastVersion)
			{
				minVersion = fi.m_lastVersion;
			}
			// ���հ汾�Ŵ��µ�������
			m_versionMap.insert( std::make_pair( fi.m_version, i->first));
		}
		if( maxVersion != m_curVersion.m_Version)
		{
			OutputString( L"ʧ�ܣ���װ���İ汾�������������°汾�Ų�ƥ�䣬���滻��Ӧ�ļ������ԣ�");
			throw CMergePatchError();
		}
		m_curVersion.m_lastVersion = minVersion;

		std::wstring versionStr, lastVersionStr;
		PFSX::Version2String( m_curVersion.m_Version, versionStr);
		PFSX::Version2String( m_curVersion.m_lastVersion, lastVersionStr);

		// .cpchҪ��������ϵͳ�������ļ���չ����ͻ������Ҫ��bootstrap�й�������չ��ͬ��
		std::wstring filename = m_savefilepath + L"\\" + lastVersionStr + L"-" + versionStr + L"." + m_extension;
		if( !m_savefile.Open( filename, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_WRONLY))
		{
			OutputString( L"ʧ�ܣ� %s �ļ�����ʧ��", filename.c_str());
			throw CMergePatchError();
		}
		m_zipwritter.SetFileIO( m_savefile);
	}

	void SaveOneDifferenceFile( const std::wstring& filename, const std::wstring& mount)
	{
		OutputString( L"�����ļ� %s ", filename.c_str());

		std::wstring subfilename = mount + filename;
		PFS::CFile file;
		if( !file.Open( subfilename, PFS::FM_EXCL, PFS::FA_RDONLY))
		{
			OutputString( L"ʧ�ܣ����ļ� %s ����", filename.c_str());
			throw CMergePatchError();
		}
		ZIPFILE::CFileCentral central;
		central.m_usMethod = 0;
		central.m_name = filename;
		central.m_name.erase( 0, central.m_name.find_first_not_of( L'/'));
		if( !m_zipwritter.WriteNextCentral( central))
		{
			OutputString( L"ʧ�ܣ�д�ļ� %s ����", filename.c_str());
			throw CMergePatchError();
		}
		m_zipwritter.SetZipLevel( 0);

		while( size_t res = file.Read( m_buffer.GetData(), BUFFERSIZE))
		{
			if( m_stop)
			{
				throw CMergePatchError();
			}
			if( PFS::FILE_SIZE_ERROR == res)
			{
				OutputString( L"ʧ�ܣ����ļ� %s ����", filename.c_str());
				throw CMergePatchError();
			}
			m_buffer.Resize( res);
			if( !m_zipwritter.WriteFileData( m_buffer, false))
			{
				OutputString( L"ʧ�ܣ�д�ļ� %s ����", filename.c_str());
				throw CMergePatchError();
			}
		}
		m_curVersion.m_sizeUpdate += file.GetSize();	// �ۼ����ļ���С�õ��������ļ���С
	}

	void Save()
	{
		m_updateMap.clear();
		m_deleteMap.clear();
		for( CVersionMountMap::iterator i = m_versionMap.begin(), e=m_versionMap.end(); i!=e; ++i)
		{
			const std::wstring& mount = i->second;
			PFS::CMetaInfoMap mim;
			if( !PFS::CEnv::GetMetaInfos( mount, mim))
			{
				std::wstring versionStr;
				PFSX::Version2String( i->first, versionStr);
				OutputString( L"ʧ�ܣ��õ� �汾 %s �� metainfo ��Ϣ����", versionStr.c_str());
				throw CMergePatchError();
			}
			if( m_listener)
			{
				m_listener->OnNotify( FILECOUNT, mim.size());
			}
			for( PFS::CMetaInfoMap::iterator im = mim.begin(), em=mim.end(); im!=em; ++im)
			{
				if( m_listener)
				{
					m_listener->OnNotify( FILEPROGRESS, 0);
				}
				const std::wstring& file = im->first;
				const PFS::CMetaInfo& meta = im->second;
				if( m_updateMap.find( file) != m_updateMap.end())
				{
					// ����ӵ������
					continue;
				}
				if( m_curmetamap.find( file) == m_curmetamap.end())
				{
					// �����°汾��װ����δ����
					continue;
				}
				SaveOneDifferenceFile( file, mount);
				m_updateMap[file] = meta;
			}

			// ����ɾ���б�
			PFS::CFile file;
			if( !file.Open( mount+L"/"+DELETE_META, PFS::FM_EXCL, PFS::FA_RDONLY))
			{
				// deletes.meta �п��ܲ�����
				continue;
			}
			mim.clear();
			PFS::CMetaFileRead::DoRead( file, mim);
			for( PFS::CMetaInfoMap::iterator im = mim.begin(), em=mim.end(); im!=em; ++im)
			{
				const std::wstring& file = im->first;
				const PFS::CMetaInfo& meta = im->second;
				if( m_deleteMap.find( file) != m_deleteMap.end())
				{
					// ����ӵ������
					continue;
				}
				if( m_curmetamap.find( file) != m_curmetamap.end())
				{
					// �����°汾��װ���г���
					continue;
				}
				m_deleteMap[file] = meta;
			}
		}
	}

	void SaveMetaFile( const std::wstring& name, const PFS::CMetaInfoMap& subMeta, unsigned short usVersion)
	{
		OutputString( L"���ڴ����ļ� %s ", name.c_str());

		m_buffer.Resize( 0);
		if( !PFS::CMetaDataWrite::DoWriteByVersion( m_buffer, subMeta, usVersion))
		{
			OutputString( L"ʧ�ܣ�д�ļ� %s ����", name.c_str());
			throw CMergePatchError();
		}

		ZIPFILE::CFileCentral central;
		central.m_usMethod = 0;
		central.m_name = name;
		
		if( !m_zipwritter.WriteNextCentral( central))
		{
			OutputString( L"ʧ�ܣ�д�ļ� %s ����", name.c_str());
			throw CMergePatchError();
		}
		if( !m_zipwritter.WriteFileData( m_buffer, false))
		{
			OutputString( L"ʧ�ܣ�д�ļ� %s ����", name.c_str());
			throw CMergePatchError();
		}
	}

	void SaveVersionMeta( )
	{
		OutputString( L"���ڴ����ļ� %s ", PFSX::VERSION_META_NAME.c_str());

		m_buffer.Resize( 0);
		m_curVersion.m_sizeUpdate /= (1024.0*1024.0);		// ��λΪM
		if( !m_curVersion.WriteTo( m_buffer))
		{
			OutputString( L"ʧ�ܣ�д�ļ� %s ����", PFSX::VERSION_META_NAME.c_str());
			throw CMergePatchError();
		}
		PFS::CDataBlock temp;
		temp.Append( "\xFF\xFE", 2);
		temp.Append( m_buffer);

		ZIPFILE::CFileCentral central;
		central.m_usMethod = 0;
		central.m_name = PFSX::VERSION_META_NAME;
		central.m_name.erase( 0, central.m_name.find_first_not_of( L'/'));

		if( !m_zipwritter.WriteNextCentral( central))
		{
			OutputString( L"ʧ�ܣ�д�ļ� %s ����", PFSX::VERSION_META_NAME.c_str());
			throw CMergePatchError();
		}
		if( !m_zipwritter.WriteFileData( temp, false))
		{
			OutputString( L"ʧ�ܣ�д�ļ� %s ����", PFSX::VERSION_META_NAME.c_str());
			throw CMergePatchError();
		}
	}

	void Destroy()
	{
		m_zipwritter.CloseFile();
		m_savefile.Close();
		// �������
		PFS::CEnv::Unmount( CUR);
	}

	virtual void OnRunning()
	{
		bool res = true;
		try
		{
			Init();
			Save();
			// д .files.meta �� .deletes.meta
			SaveMetaFile( FILES_META, m_updateMap, 0x0100);
			if( !m_deleteMap.empty())
			{
				// ����Ҫɾ�����ļ���д .deletes.meta
				SaveMetaFile( DELETE_META, m_deleteMap, 0x0100);
			}
			SaveVersionMeta();
		}
		catch( CMergePatchError&)
		{
			res = false;
		}
		Destroy();
		if( m_listener)
		{
			m_listener->OnNotify( END, res);
		}
	}

public:
	void SetFileName( const CFileInfoMap& lastpfss, const std::wstring& curpfs, 
		const std::wstring& savefilepath)
	{
		m_lastpfss = lastpfss;
		m_curpfs = curpfs;
		m_savefilepath = savefilepath;
	
		m_buffer.Resize( BUFFERSIZE);
	}

	void SetIsZip( bool bZip)
	{
		m_zipwritter.SetIsZip( bZip);
	}

	void SetExtension( const std::wstring& extension)
	{
		m_extension = extension;
	}
};