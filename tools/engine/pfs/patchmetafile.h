#pragma once
#include <string>
#include <vector>
#include <map>
#include "../xmlio/xmlio.h"

namespace PFSX
{
	const std::wstring PATCH_META_NAME = L"/.patch.meta";

	struct PatchInfo
	{
		std::wstring		filename;
		fsize_t				size;
		PFS::FILE_DATA_HASH	hash;
		std::wstring		md5; // 128bit, 32 hexadecimal value, empty for old version. 
								 // md5sum, a linux command, compitable.
								 // added for p2sp download mode.

		DWORD				versionFrom;
		DWORD				versionTo;

		bool operator == ( const PatchInfo& other)
		{
			return (versionFrom == other.versionFrom) && (versionTo == other.versionTo);
		}
	};
	
	typedef std::vector< PatchInfo >	CPatchInfoVector;
	typedef std::map<int, PatchInfo>	CPatchInfoMap;

	struct UpdateInfo
	{
		DWORD				versionFrom;
		DWORD				versionTo;
		std::vector<int>	use;

		bool operator < ( const UpdateInfo& right)
		{
			// �Ȱ� versionTo �Ӵ�С����
			// ��� versionTo ��ȣ��� versionFrom �Ӵ�С����
			if( versionTo != right.versionTo )
			{
				return ( versionTo > right.versionTo);
			}
			return versionFrom > right.versionFrom;
		}
	};

	typedef std::vector<UpdateInfo>					CUpdateInfoVector;

	typedef std::map<DWORD, CUpdateInfoVector>		CUpdateInfoMap;

	class CPatchMetaFile
	{
		DWORD			m_version;
		CPatchInfoMap	m_patches; //ͨ�ø��°�
		CUpdateInfoMap	m_updates; //ͨ�ø�����
		CPatchInfoMap	m_patches2;//ר�ø��°�
		CUpdateInfoMap	m_updates2;//ר�ø�����

		bool LoadPatches( const XMLIO::CINode& node, bool bSpecial = false);
		bool LoadUpdates( const XMLIO::CINode& node, bool bSpecial = false);

		bool WritePatches( XMLIO::CONode& node, bool bSpecial = false) const;
		bool WriteUpdates( XMLIO::CONode& node, bool bSpecial = false) const;

		bool FillPatches( CPatchInfoMap& pim, const std::vector<int>& use);
		bool FillPatches( CPatchInfoVector& rPatchInfoVector, const std::vector<int>& use, DWORD versionFrom);
		//Ŀǰ������Ŀ��ʱʹ��EX�汾
		bool FillPatchesEX( CPatchInfoVector& rPatchInfoVector, const std::vector<int>& use, DWORD versionFrom);
		
		bool FillPatches2( CPatchInfoMap& pim, const std::vector<int>& use);
		bool FillPatches2( CPatchInfoVector& rPatchInfoVector, const std::vector<int>& use, DWORD versionFrom);

	public:
		CPatchMetaFile() : m_version( 1) { }

		bool LoadFrom( PFS::CBaseFile& file);

		bool Write( PFS::CBaseFile& file) const;

		// ���ݰ汾�õ�ͨ��patch��Ϣ
		bool GetPatchInfoFromVersion( PatchInfo& pi, DWORD versionFrom, DWORD versionTo);
		// ���ݰ汾�õ�ͨ��patch�б�
		bool GetPatchInfosFromVersion( CPatchInfoVector& piVector, DWORD versionFrom, DWORD versionTo);
		//Ŀǰ������Ŀ��ʱʹ��EX�汾
		bool GetPatchInfosFromVersionEX( CPatchInfoVector& piVector, DWORD versionFrom, DWORD versionTo);

		// ���ݰ汾�õ�ר��patch��Ϣ
		bool GetPatchInfoFromVersion2( PatchInfo& pi, DWORD versionFrom, DWORD versionTo);
		// ���ݰ汾�õ�ר��patch�б�
		bool GetPatchInfosFromVersion2( CPatchInfoVector& piVector, DWORD versionFrom, DWORD versionTo);

		//���ݴ�С����ѡһ
		//���ݰ��Ĵ�Сȷ��һ����С�İ�
		bool GetMinPatchInfoFromVersion( PatchInfo& pi, DWORD versionFrom, DWORD versionTo);
		//���ݰ��Ĵ�Сȷ��һ����С�İ�����
		bool GetMinPatchInfosFromVersion( CPatchInfoVector& piVector, DWORD versionFrom, DWORD versionTo);

		void Close();
		
		CPatchInfoMap& GetPatchInfoMap() { return m_patches; }

		CUpdateInfoMap& GetUpdateInfoMap() { return m_updates; }

		CPatchInfoMap& GetPatchInfoMap2() { return m_patches2; }

		CUpdateInfoMap& GetUpdateInfoMap2() { return m_updates2; }

		// ���༭��ʹ��
		// ���� versionFrom �� versionTo �г�������ʹ�õ�ͨ�ò������б�
		bool ListAllPatchsFromVersion( std::vector<std::vector<int> >& uses, DWORD versionFrom, DWORD versionTo);
		// �༭��ר��
		// ���� versionFrom �� versionTo �г�������ʹ�õ�ר�ò������б�
		bool ListAllPatchsFromVersion2( std::vector<std::vector<int> >& uses, DWORD versionFrom, DWORD versionTo);

	public:
		static bool DoRead( const std::wstring& fileName, CPatchMetaFile& pmf)
		{
			PFS::CFile file;
			if( !file.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
			{
				return false;
			}

			return pmf.LoadFrom( file );
		}

		static bool DoWrite( const std::wstring& fileName, const CPatchMetaFile& pmf)
		{
			PFS::CFile file;
			if( !file.Open( fileName, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_WRONLY ) )
			{
				return false;
			}

			return pmf.Write( file);
		}
	};

	bool GetRemotePatchMetaFile( const std::wstring& url, CPatchMetaFile& pmf);

	// ��sSrc�еõ���cDelimiter�ֽ��int����
	bool GetIntVectorFromStr( std::vector<int>& iv, const std::wstring& sSrc, const wchar_t cDelimiter=L',');

	// ��int�����еõ���cDelimiter�ֽ���ַ���sSrc
	bool GetStrFromIntVector( const std::vector<int>& iv, std::wstring& sDes, const wchar_t cDelimiter=L',');

};	// namespace PFSX