#pragma once

#include "runzipfile.h"
#include "../base/pfsbase.h"

namespace PFS {

	class CRunZipFS : public CPFSBase
	{
		friend class CRunZipBaseFile;

		struct HashPair
		{
			HashPair() {}
			HashPair( unsigned int h1, unsigned int h2) : hash1( h1), hash2( h2) {}

			unsigned int hash1;
			unsigned int hash2;

			bool operator<( const HashPair& dst) const
			{
				if( hash1 == dst.hash1)
					return hash2 < dst.hash2;
				else
					return hash1 < dst.hash1;
			}
		};

		typedef std::map<HashPair,CImageData> CImageCacheMap; // ≤‚ ‘ π”√
		CImageCacheMap	m_cachemap;

		ZIPFILE::CRunFileReader			m_zipfile;
		std::wstring					m_strBasePath;
		CBaseFile*						m_basefile;
		CMutex							m_cs;
		CFileImageHandle				m_ImageHandle;

#ifdef OUTLOG_DUMP_INFOS_WITH_FILE_NAME
		std::wstring		m_lastlostfilename;
		HashPair			m_lastlostfilehash;
#endif

	public:
		CRunZipFS( void*);
		CRunZipFS();
		virtual ~CRunZipFS();
	protected:
		virtual std::wstring GetNativeFilePath( const std::wstring& rFileName ) const 
		{
			if( !rFileName.empty() )
				return GetNativeDevicePath() + L'$' + rFileName;
			else
				return GetNativeDevicePath();
		}

	private:	
		void MakeImageHandle();
		void InitMetaInfoFromMetaFile();

	public:
		virtual void DumpInfos( COutLog& outlog) const;

		virtual CPFSBase* Clone() const { return new CRunZipFS; }
		virtual int Initialize( const std::wstring& device, MOUNT_TYPE mt);
		virtual FS_TYPE GetFSType() const { return FST_RUNZIP; }
		virtual int OpenFile( const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa, CBaseFile*& file);
		virtual int RemoveFile( const std::wstring& filename)
		{
			return EC_FS_READONLY;
		}
		virtual bool IsFileExisting( const std::wstring& filename);

		virtual int CreateDirectory( const std::wstring& path, bool bFailIfExisting = false )
		{
			return EC_FS_READONLY;
		}

		virtual int RemoveDirectory( const std::wstring& path, bool bFailIfNotEmpty = false )
		{
			return EC_FS_READONLY;
		}

		virtual bool IsDirectoryExisting( const std::wstring& path );

		virtual bool IsFSBusy() const
		{
			return false;
		}
		virtual void CheckMetaInfo() {}
	};
	
} // PFS namespace