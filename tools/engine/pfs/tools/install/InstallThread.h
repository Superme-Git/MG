#pragma once
#include "../../src/zip/zipfile.h"
#include "../packzip/basethread.h"
#include "ProgressThreadListener.h"
#include "../../setupmetafile.h"

class CInstallThread : public CBaseThread
{
	PFSX::SetupMetaVector			m_packs;
	
	std::vector<std::wstring>		m_zipfiles;
	std::wstring					m_setuppath;
	PFS::CDataBlock					m_buffer;

public:
	CInstallThread();
	virtual ~CInstallThread() {}

	void SetPath( const std::vector<std::wstring>& zipfiles, const std::wstring& setuppath)
	{
		m_zipfiles = zipfiles;
		m_setuppath = setuppath;
	}
	
protected:
	class CInstallError {};

	virtual void OnRunning()
	{
		try
		{
			if( Init())
			{
				if( Begin())
				{
					WriteAllNativeFile();
					WriteAllZipFile();
				}
			}
		}
		catch( CInstallError&)
		{
		}
		End();
		if( !m_stop )
		{
			m_listener->OnNotify( 0, TOTALEND);
		}
	}
	bool Init();

	bool Begin();
	void End();
	bool WriteNativeFile( const std::wstring& srcname, const std::wstring& desname, PFS::CMetaInfo& meta);
	bool WriteZipFile( const std::wstring& srcname, const std::wstring& desname, 
		PFS::CMetaInfo& meta, ZIPFILE::CFileWriter& writter	);
	bool ReadSetupMetaFile( const std::wstring& src);
	bool WriteSetupMetaFile();

	float GetFileLen( PFS::CMetaInfoMap& mm);
	bool WriteAllNativeFile( );
	bool WriteAllZipFile( );
	bool WriteNativeMetaFile( PFS::CMetaInfoMap& mm);
	bool WriteZipMetaFile( ZIPFILE::CFileWriter& writter, PFS::CMetaInfoMap& mm);

	bool IsZipFile( const std::wstring& filename);
	
	const std::wstring& GetSrcName()
	{
		static std::wstring src = L"/src";
		return src;
	}
	const std::wstring& GetDesName()
	{
		static std::wstring des = L"/des";
		return des;
	}
};
