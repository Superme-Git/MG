#pragma once
#include "BrowseLastPfsPathThread.h"
#include "SubtractMetaInfo.h"
#include "../../src/zip/zipfile.h"
#include "../../setupmetafile.h"
#include "GlobalFunction.h"
#include <iostream>
#include <fstream>
#include <iomanip>

#define WM_MAKEPROGRESS		(WM_USER+100)

enum
{
	INFO = 0,
	FILECOUNT = 1,
	FILEPROGRESS = 2,
	END = 3,
};


class CMakeDifferenceThread : public CBaseThread
{
private:

	enum { BUFFERSIZE = 65536 };// 64K

	std::wstring m_extension;				// 补丁包后缀

	//std::wstring m_lastpfs;
	std::wstring m_curpfs;
	std::wstring m_savefilepath;
	
	PFS::CMetaInfoMap	m_curmetamap;
	CFileInfoMap		m_lastpfss;
	bool				m_bMakeServer;		// 是否生成服务器更新包

	PFSX::CVersionInfo m_curVersion;

	PFSX::CNativeFile		m_savefile;
	PFS::CPFSWriter			m_zipwritter;
	PFSX::CNativeFile		m_serverSaveFile;
	ZIPFILE::CFileWriter	m_serverZipWritter;

	PFS::CDataBlock			m_buffer;

	PFSX::SetupPathVector	m_validpaths;		// 子目录中有 .files2.meta 文件的目录

	class CMakeDifferenceError {};

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

	void Init( )
	{
		//OutputString( L"分析上一版本安装包 %s", m_lastpfs.c_str());
		//if( !PFS::CEnv::Mount( LAST, m_lastpfs, PFS::FST_RUNZIP, PFS::MT_READONLY))
		//{
		//	OutputString( L"失败，上一版本安装包 %s 不是有效的pfs文件", m_lastpfs.c_str());
		//	throw CMakeDifferenceError();
		//}

		OutputString( L"分析当前版本安装包 %s ", m_curpfs.c_str());
		if( !PFS::CEnv::Mount( CUR, m_curpfs, PFS::FST_RUNZIP, PFS::MT_READONLY))
		{
			OutputString( L"失败，当前版本安装包 %s 不是有效的pfs文件", m_curpfs.c_str());
			throw CMakeDifferenceError();
		}

		OutputString( L"获得当前版本安装包 %s 的meta信息 ", m_curpfs.c_str());
		m_validpaths.clear();
		if( !helper::ReadPFSAllMetaInfos( CUR, m_curmetamap, m_validpaths))
		{
			OutputString( L"失败，获得当前版本安装包 %s 的meta信息出错", m_curpfs.c_str());
			throw CMakeDifferenceError();
		}

		m_curVersion.Close();
		if( !PFSX::GetLocalVersion( CUR, m_curVersion))
		{
			OutputString( L"失败，获得当前版本安装包 %s 的版本信息出错", m_curpfs.c_str());
			throw CMakeDifferenceError();
		}
	}

	void InitOne( const std::wstring& lastversion, bool bMakeServer)
	{
		std::wstring versionstr;
		PFSX::Version2String( m_curVersion.m_Version, versionstr);

		// .cpch要尽量不与系统中其他文件扩展名冲突，这里要与bootstrap中关联的扩展名同步
		std::wstring filename = m_savefilepath + L"\\" + lastversion + L"-" + versionstr + L"." + m_extension;
		if( !m_savefile.Open( filename, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_WRONLY))
		{
			OutputString( L"失败， %s 文件创建失败", filename.c_str());
			throw CMakeDifferenceError();
		}
		m_zipwritter.SetFileIO( m_savefile);

		if( bMakeServer)
		{
			filename = m_savefilepath + L"\\" + lastversion + L"-" + versionstr + L".spch";
			if( !m_serverSaveFile.Open( filename, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_WRONLY))
			{
				OutputString( L"失败， %s 文件创建失败", filename.c_str());
				throw CMakeDifferenceError();
			}
			m_serverZipWritter.SetFileIO( m_serverSaveFile);
		}
	}

	void SubtractMeta( const std::wstring& mountstr, const std::wstring& lastpfs, 
		PFS::CMetaInfoMap& subMeta, PFS::CMetaInfoMap& delMeta)
	{
		std::wstring v1, v2;
		PFSX::Version2String( m_curVersion.m_lastVersion, v1);
		PFSX::Version2String( m_curVersion.m_Version, v2);
		std::wstring chayiliebiao = this->m_savefilepath + L"/" + v1 + L"-" + v2 + this->m_extension + L".txt";
		std::wofstream fout(chayiliebiao.c_str(), std::ios::out|std::ios::trunc);
		PFS::CMetaInfoMap lastMeta;

		OutputString( L"获得旧版本安装包 %s 的meta信息 ", lastpfs.c_str());
		// 这里还用 GetMetaInfos 为了与旧版本安装包兼容
		if( !PFS::CEnv::GetMetaInfos( mountstr, lastMeta))
		{
			OutputString( L"失败，获得旧版本安装包 %s 的meta信息出错！", lastpfs.c_str());
			throw CMakeDifferenceError();
		}

		OutputString( L"计算差异文件 ");
		CSubtractMetaInfo::SubtractMetaInfoMap( m_curmetamap, lastMeta, subMeta);
		for (PFS::CMetaInfoMap::iterator it = subMeta.begin(); it != subMeta.end(); it ++) {
			PFS::fsize_t s1 = 0;
			PFS::fsize_t s2 = 0;
			PFS::CMetaInfoMap::iterator sit = m_curmetamap.find(it->first);
			if (sit != m_curmetamap.end()) {
				s1 = sit->second.GetFileSize();
			}
			sit = lastMeta.find(it->first);
			if (sit != lastMeta.end()) {
				s2 = sit->second.GetFileSize();
			}
			fout << L"AM\t" << std::setw(12) << (long long)(s1-s2) << L"  " << it->first << std::endl;
			//fout << "AM\t" << std::setw(12) << s1-s2 << std::endl;
		}
		if( subMeta.size() == 0)
		{
			OutputString( L"失败，%s 与当前版本的差异文件数量为0", lastpfs.c_str());
			throw CMakeDifferenceError();
		}

		// .version.meta最后处理
		subMeta.erase( PFSX::VERSION_META_NAME);

		// 计算需要删除的文件
		CSubtractMetaInfo::SubtractMetaInfoMapEx( lastMeta, m_curmetamap, delMeta);
		for (PFS::CMetaInfoMap::iterator it = delMeta.begin(); it != delMeta.end(); it ++) {
			fout << L"DD\t" << std::setw(12) << (long long)(0-it->second.GetFileSize()) << L"  " << it->first << std::endl;
			//fout << "DD\t" << std::setw(12) << 0-it->second.GetFileSize() << std::endl;
		}
	}

	// bMakeServer 是否保存到服务器差异包
	// bSaveClient 是否保存到客户端差异包
	void SaveOneDifferenceFile( const std::wstring& filename, bool bSaveServer, bool bSaveClient)
	{
		OutputString( L"处理文件 %s ", filename.c_str());

		std::wstring subfilename = CUR + filename;
		PFS::CFile file;
		if( !file.Open( subfilename, PFS::FM_EXCL, PFS::FA_RDONLY))
		{
			OutputString( L"失败，打开文件 %s 出错", filename.c_str());
			throw CMakeDifferenceError();
		}
		ZIPFILE::CFileCentral central;
		central.m_usMethod = 0;
		central.m_name = filename;
		central.m_name.erase( 0, central.m_name.find_first_not_of( L'/'));

		if( bSaveClient)
		{
			if( !m_zipwritter.WriteNextCentral( central))
			{
				OutputString( L"失败，写文件 %s 出错", filename.c_str());
				throw CMakeDifferenceError();
			}
			m_zipwritter.SetZipLevel( 0);
		}

		if( bSaveServer)
		{
			if( !m_serverZipWritter.WriteNextCentral( central))
			{
				OutputString( L"失败，写文件 %s 出错", filename.c_str());
				throw CMakeDifferenceError();
			}
			m_serverZipWritter.SetZipLevel( 0);
		}

		while( size_t res = file.Read( m_buffer.GetData(), BUFFERSIZE))
		{
			if( m_stop)
			{
				throw CMakeDifferenceError();
			}
			if( PFS::FILE_SIZE_ERROR == res)
			{
				OutputString( L"失败，读文件 %s 出错", filename.c_str());
				throw CMakeDifferenceError();
			}
			m_buffer.Resize( res);

			if( bSaveClient)
			{
				if( !m_zipwritter.WriteFileData( m_buffer, false))
				{
					OutputString( L"失败，写文件 %s 出错", filename.c_str());
					throw CMakeDifferenceError();
				}
			}

			if( bSaveServer)
			{
				if( !m_serverZipWritter.WriteFileData( m_buffer, false))
				{
					OutputString( L"失败，写文件 %s 出错", filename.c_str());
					throw CMakeDifferenceError();
				}
			}
		}
		m_curVersion.m_sizeUpdate += file.GetSize();	// 累加子文件大小得到补丁包文件大小

		if( m_listener)
		{
			m_listener->OnNotify( FILEPROGRESS, 0);
		}
	}

	void SaveMetaFile( const std::wstring& name, const PFS::CMetaInfoMap& subMeta, 
		bool bMakeServer, unsigned short usVersion)
	{
		OutputString( L"正在处理文件 %s ", name.c_str());
		
		m_buffer.Resize( 0);
		if( !PFS::CMetaDataWrite::DoWriteByVersion( m_buffer, subMeta, usVersion))
		{
			OutputString( L"失败，写文件 %s 出错", name.c_str());
			throw CMakeDifferenceError();
		}

		ZIPFILE::CFileCentral central;
		central.m_usMethod = 0;
		central.m_name = name;

		if( bMakeServer)
		{
			if( !m_serverZipWritter.WriteNextCentral( central))
			{
				OutputString( L"失败，写文件 %s 出错", name.c_str());
				throw CMakeDifferenceError();
			}
			if( !m_serverZipWritter.WriteFileData( m_buffer, false))
			{
				OutputString( L"失败，写文件 %s 出错", name.c_str());
				throw CMakeDifferenceError();
			}

		}
		else
		{
			if( !m_zipwritter.WriteNextCentral( central))
			{
				OutputString( L"失败，写文件 %s 出错", name.c_str());
				throw CMakeDifferenceError();
			}
			if( !m_zipwritter.WriteFileData( m_buffer, false))
			{
				OutputString( L"失败，写文件 %s 出错", name.c_str());
				throw CMakeDifferenceError();
			}
		}
	}

	void SaveVersionMeta( bool bMakeServer)
	{
		OutputString( L"正在处理文件 %s ", PFSX::VERSION_META_NAME.c_str());
		
		m_buffer.Resize( 0);
		m_curVersion.m_sizeUpdate /= (1024.0*1024.0);		// 单位为M
		if( !m_curVersion.WriteTo( m_buffer))
		{
			OutputString( L"失败，写文件 %s 出错", PFSX::VERSION_META_NAME.c_str());
			throw CMakeDifferenceError();
		}
		PFS::CDataBlock temp;
		temp.Append( "\xFF\xFE", 2);
		temp.Append( m_buffer);

		ZIPFILE::CFileCentral central;
		central.m_usMethod = 0;
		central.m_name = L".version.meta";

		if( !m_zipwritter.WriteNextCentral( central))
		{
			OutputString( L"失败，写文件 %s 出错", PFSX::VERSION_META_NAME.c_str());
			throw CMakeDifferenceError();
		}
		if( !m_zipwritter.WriteFileData( temp, false))
		{
			OutputString( L"失败，写文件 %s 出错", PFSX::VERSION_META_NAME.c_str());
			throw CMakeDifferenceError();
		}

		if( bMakeServer)
		{
			if( !m_serverZipWritter.WriteNextCentral( central))
			{
				OutputString( L"失败，写文件 %s 出错", PFSX::VERSION_META_NAME.c_str());
				throw CMakeDifferenceError();
			}
			if( !m_serverZipWritter.WriteFileData( temp, false))
			{
				OutputString( L"失败，写文件 %s 出错", PFSX::VERSION_META_NAME.c_str());
				throw CMakeDifferenceError();
			}
		}

	}

	bool IsHasChildFiles( const PFS::CMetaInfoMap& subMeta, const std::wstring& path)
	{
		if( path.size() < 2)
		{
			return true;	// 根目录
		}

		PFS::CMetaInfoMap::const_iterator iBegin = subMeta.lower_bound( path);
		if ( iBegin != subMeta.end() && iBegin->first.compare( 0, path.size(), path ) == 0 )
		{
			return true;
		}

		return false;
	}

	void Make( const std::wstring& mountstr, const CFileInfo& fileinfo, bool bMakeServer)
	{
		m_curVersion.m_lastVersion = fileinfo.m_version;

		std::wstring versionstr;
		PFSX::Version2String( fileinfo.m_version, versionstr);
		InitOne( versionstr, bMakeServer);

		// 得到差异文件
		PFS::CMetaInfoMap subMeta, delMeta;
		SubtractMeta( mountstr, fileinfo.m_filename, subMeta, delMeta);
		if( m_listener)
		{
			int count = subMeta.size();
			if( bMakeServer)
			{
				for( PFSX::SetupPathVector::iterator i=m_validpaths.begin(); i!=m_validpaths.end(); i++)
				{
					// 目录下有差异文件才计算在内
					const std::wstring childpath = *i + L"/";
					if( IsHasChildFiles( subMeta, childpath) || 
						IsHasChildFiles( delMeta, childpath) 
						)
					{
						count++;
					}
				}
			}
			m_listener->OnNotify( FILECOUNT, count);
		}
		// 写差异文件
		for( PFS::CMetaInfoMap::const_iterator i = subMeta.begin(), e=subMeta.end(); i != e; ++i)
		{
			SaveOneDifferenceFile( i->first, bMakeServer, true);
		}
		// 写 .files.meta 到客户端差异包
		SaveMetaFile( FILES_META, subMeta, false, 0x0100);
		// 写 .deletes.meta 到客户端差异包
		if( !delMeta.empty())
		{
			// 有需要删除的文件才写 .deletes.meta
			SaveMetaFile( DELETE_META, delMeta, false, 0x0100);
		}

		if( bMakeServer)
		{
			SaveMetaFile( FILES_META, m_curmetamap, true, 0x0100);
			// 将最新版本安装包中所有 .files2.meta 文件拷贝到服务器差异包中
			for( PFSX::SetupPathVector::iterator i=m_validpaths.begin(); i!=m_validpaths.end(); i++)
			{
				const std::wstring childpath = *i + L"/";
				if( IsHasChildFiles( subMeta, childpath) || 
					IsHasChildFiles( delMeta, childpath) 
					// BUGFIX: 如果某个子包中没有文件需要更新，但有文件需要删除，同样需要更新.files2.meta。
					)
				{
					// childpath 目录下有差异文件才保存 files2.meta 到 childpath 下
					SaveOneDifferenceFile( childpath+FILES2_META, true, false);
				}
			}
		}

		// 写version.meta文件
		SaveVersionMeta( bMakeServer);

		DestroyOne( bMakeServer);
	}

	void DestroyOne( bool bMakeServer)
	{
		m_zipwritter.CloseFile();
		m_savefile.Close();
		
		if( bMakeServer)
		{
			m_serverZipWritter.CloseFile();
			m_serverSaveFile.Close();
		}
	}

	void Destroy()
	{
		// 清除操作
		PFS::CEnv::Unmount( CUR);
	}

	virtual void OnRunning()
	{
		bool res = true;
		try
		{
			Init();
			DWORD maxVersion = 0;
			for( CFileInfoMap::iterator i = m_lastpfss.begin(), e=m_lastpfss.end(); i != e; ++i)
			{
				const CFileInfo& fi = i->second;
				if( fi.m_version > maxVersion)
				{
					maxVersion = fi.m_version;
				}
			}
			for( CFileInfoMap::iterator i = m_lastpfss.begin(), e=m_lastpfss.end(); i != e; ++i)
			{
				const CFileInfo& fi = i->second;
				// 只对最新的旧版本生成服务器更新包
				Make( i->first, fi, m_bMakeServer ? ( fi.m_version == maxVersion) : false);
			}
		}
		catch( CMakeDifferenceError&)
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

	void SetIsMakeServer( bool bMakeServer)
	{
		m_bMakeServer = bMakeServer;
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