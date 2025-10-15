///=============================================================================
/// Product   : PFS
/// Project   : pfslib
/// Module    : setupmetafile.h
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	CSetupMetaFile - .setup.meta/.master.meta�ļ��Ĳ���������
///		.setup.meta/.master.meta �ļ�Ϊpfs�������ߺ͸��³������������ļ���
///	���ǿͻ��˰�װ������Դ�ļ����嵥��
///	�����ڴ��嵥�е���Ŀcfg��ʾcfgĿ¼�ڴ�����Ϊcfg.pfs��Դ����
/// ��װ����͸��³���ɸ��ݴ���Ŀȷ��cfg��Ŀ�Ĵ洢��ʽ��cfg.pfs����cfg�ļ�Ŀ¼��
/// ���������һ��.master.meta�ļ����ݵ�ʾ����
///		<data v="1.0" o="1">
///			<f n="/cfg" h="hash of cfg/.files.meta" l="maybe 0"/>
///			<f n="/effect"/>
///			<f n="/map"/>
///			<f n="/pak"/>
///			<f n="/role"/>
///			<f n="/sound"/>
///			<f n="/ui"/>
///		</data>
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-10-16 Created.
///=============================================================================
#pragma once
#include <vector>
#include <string>
#include "pfs2.h"

namespace PFSX
{
	using PFS::FILE_DATA_HASH;

	const std::wstring MASTER_META_NAME = L"/.master.meta";
	const std::wstring SETUP_META_NAME = L"/.setup.meta";

	struct SetupMetaInfo // V1.0
	{
	private:
		std::wstring		m_Name;		// Name of setup pfs file
		FILE_DATA_HASH		m_Hash;		// Hash of .file.meta file
		FILE_DATA_HASH		m_Hash2;	// Hash of .file2.meta itself
		unsigned int		m_Flags;	// Flags of pfs file

	public:
		SetupMetaInfo()
			: m_Hash(0)
			, m_Hash2(0)
			, m_Flags(0)
		{
		}

		SetupMetaInfo(
			const std::wstring&		_Name,
			const FILE_DATA_HASH&	_Hash,
			const FILE_DATA_HASH&	_Hash2,
			unsigned int			_Flags)
			: m_Name(_Name)
			, m_Hash(_Hash)
			, m_Hash2(_Hash2)
			, m_Flags(_Flags)
		{
		}

		// �����ж�Name�ǲ���ͬһ��,���ǲ���ͬһ������
		bool operator == ( const SetupMetaInfo& rOther ) const
		{
			// ����ֻ�ж�Name�ǲ���ͬһ��
			return ( this == &rOther ) || ( m_Name == rOther.m_Name );
		}

		// �����ж϶����״̬�Ƿ����仯
		bool IsChanged( const SetupMetaInfo& rComparedTo ) const
		{
			if( this == &rComparedTo )
			{
				return false;
			}
			// assert( m_Name == rComparedTo.m_Name );
			return ( m_Name != rComparedTo.m_Name ||
				m_Hash != rComparedTo.m_Hash ||
				( m_Hash2 != 0 && rComparedTo.m_Hash2 != 0 && m_Hash2 != rComparedTo.m_Hash2 ) ||
				m_Flags != rComparedTo.m_Flags );
		}

		bool operator < ( const SetupMetaInfo& rOther ) const
		{
			return ( m_Name < rOther.m_Name );
		}

		SetupMetaInfo& Swap( SetupMetaInfo& rOther )
		{
			if( this != &rOther )
			{
				m_Name.swap( rOther.m_Name);
				std::swap( m_Hash, rOther.m_Hash );
				std::swap( m_Hash2, rOther.m_Hash2 );
				std::swap( m_Flags, rOther.m_Flags );
			}
			return *this;
		}

		const std::wstring& GetName() const { return m_Name; }
		const FILE_DATA_HASH& GetHash() const { return m_Hash; }
		const FILE_DATA_HASH& GetHash2() const { return m_Hash2; }
		unsigned int GetFlags() const { return m_Flags; }

		std::wstring& GetName() { return m_Name; }
		FILE_DATA_HASH& GetHash() { return m_Hash; }
		FILE_DATA_HASH& GetHash2() { return m_Hash2; }
		unsigned int& GetFlags() { return m_Flags; }

		void SetName(const std::wstring& _Name) { m_Name = _Name; }
		void SetHash(const FILE_DATA_HASH& _Hash) { m_Hash = _Hash; }
		void SetHash2(const FILE_DATA_HASH& _Hash2) { m_Hash2 = _Hash2; }
		void SetFlags(unsigned int _Flags) { m_Flags = _Flags; }
	};

	typedef std::vector<std::wstring> SetupPathVector;

	class SetupMetaVector : public std::vector<SetupMetaInfo>
	{
	public:
#pragma pack(push, 1)
		union
		{
			unsigned short m_wVersion; // �汾��Ϣ
			struct 
			{
				unsigned char m_bMinor;
				unsigned char m_bMajor;
			};
		};
#pragma pack(pop)

		bool m_bOriginal;	// ԭʼMeta��־
							// �ϰ汾�ĸ��³���ֱ�Ӵӷ��������ش��ļ���,
							// ��δ�������ֱ��ת��/copy,�˱�־����,
							// ���³���ݴ���Ϊ�Ǹ��ļ��Ǵ��ϰ汾setupmeta���ɹ�����,
							// ��ʱ��Ҫ�������Ӱ����д���,��������hash�Ա�.

	public:
		SetupMetaVector()
		{
			m_wVersion = MAKEWORD(0,1); // ��������Ϊ1.0
			m_bOriginal = false;
		}

		bool IsOriginal() const 
		{
			return m_bOriginal;
		}

		void SetOriginal(bool bValue)
		{
			m_bOriginal = bValue;
		}
	};

	class CSetupMetaFile
	{
	public:
		//////////////////////////////////////////////////////////////////////////
		//
		// default methods, allow overridden.
		//
		// file - pfs file object to load from or save to
		// data - memory block object to output to
		// metas - pfs file meta list to save to or to be saved
		// paths - pfs file name list to save to or to be saved
		//////////////////////////////////////////////////////////////////////////
		virtual bool Load( PFS::CBaseFile& file, SetupMetaVector& metas );
		virtual bool Load( PFS::CBaseFile& file, SetupPathVector& paths );
		virtual bool SaveAsData( PFS::CDataBlock& data, const SetupMetaVector& metas );
		virtual bool Save( PFS::CBaseFile& file, const SetupMetaVector& metas );

	public:
		//////////////////////////////////////////////////////////////////////////
		//
		// static methods - using the default reading/writing methods as declared above
		//
		// fileName - native file name or pfs file name to load from or save to
		// file - pfs file object to load from or save to
		// data - memory block object to output to
		// metas - pfs file name list to save to or to be saved
		//
		//////////////////////////////////////////////////////////////////////////
		static bool DoRead( const std::wstring& fileName, SetupMetaVector& metas )
		{
			PFS::CFile file;
			if( !file.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
			{
				return false;
			}

			CSetupMetaFile reader;
			return reader.Load( file, metas );
		}
		
		static bool DoRead( PFS::CBaseFile& file, SetupMetaVector& metas)
		{
			CSetupMetaFile reader;
			return reader.Load( file, metas );
		}

		static bool DoRead( const std::wstring& fileName, SetupPathVector& paths )
		{
			PFS::CFile file;
			if( !file.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
			{
				return false;
			}

			CSetupMetaFile reader;
			return reader.Load( file, paths );
		}

		static bool DoWrite( const std::wstring& fileName, const SetupMetaVector& metas )
		{
			PFS::CFile file;
			if( !file.Open( fileName, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_WRONLY ) )
			{
				return false;
			}

			CSetupMetaFile writer;
			return writer.Save( file, metas );
		}

		static bool DoWrite( PFS::CDataBlock& data, const SetupMetaVector& metas)
		{
			CSetupMetaFile writer;
			return writer.SaveAsData( data, metas );
		}

		static bool DoWrite( PFS::CBaseFile& file, const SetupMetaVector& metas)
		{
			CSetupMetaFile writer;
			return writer.Save( file, metas );
		}
	};

	///-----------------------------------------------------------------------------
	/// summary : ��2��setupmeta��Ϣ��������б����ڼ�������嵥��ȷ�������б�
	/// params  : rMinuend - ������
	///           rSubtrahend - ���� 
	///           rDifference -  ��
	///-----------------------------------------------------------------------------
	void SubtractSetupMeta( const SetupMetaVector& rMinuend, 
		const SetupMetaVector& rSubtrahend, SetupMetaVector& rDifference );

	///-----------------------------------------------------------------------------
	/// summary : ��2��setupmeta��Ϣ���㼯��Ԫ���ų��б����ڼ�������嵥
	///           Ӧ�ó��������ؼ���rFullSet�ȷ���������rSubSet���ڸ���Ԫ�أ�
	///           ������Щ��Ҫɾ���ı��ؼ����ж����Ԫ�ء�
	///           �˼��㲻������Щ���ڡ��������в����Ԫ�ء�
	/// params  : rFullSet - ȫ�����Ѿ���name����
	///           rSubSet - �Ӽ����Ѿ���name����
	///           rDifference - �
	///-----------------------------------------------------------------------------
	void DiffSetupMeta( const SetupMetaVector& rFullSet, 
		const SetupMetaVector& rSubSet, SetupMetaVector& rDifference );

	///-----------------------------------------------------------------------------
	/// summary : ��һ��setupmeta��ɾ��ָ����Ԫ�ء�
	///           Ӧ�ó��������ؼ���rSet�ȷ��������ϴ��ڸ���Ԫ�أ���ЩԪ��ΪrToPergedSet��
	///           ɾ����ЩԪ�ء�rSet�������¡�
	///           ���DiffSetupMeta()ʹ�ã�rToPergedSetӦ��ΪDiffSetupMeta���ص�rDifference��
	/// params  : rSet - ȫ��
	///           rSetToPurge - ��ɾ���ļ���
	///-----------------------------------------------------------------------------
	void PurgeSetupMeta(SetupMetaVector& rSet, const SetupMetaVector& rSetToPurge);

	// �� src ����ϲ��� des
	void MergeSetupMeta( const SetupMetaVector& src, SetupMetaVector& des);

} // namespace PFSX