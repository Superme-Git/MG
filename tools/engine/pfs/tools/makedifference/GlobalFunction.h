#pragma once

//const std::wstring LAST = L"/last";
const std::wstring CUR = L"/cur";
const std::wstring FILES_META = L".files.meta";
const std::wstring FILES2_META = L".files2.meta";
const std::wstring DELETE_META = L".deletes.meta";
const std::wstring SETUP_META = L".setup.meta";


class helper
{
public:

	static bool ReadMetaInfos( const std::wstring& filename, PFS::CMetaInfoMap& mim)
	{
		PFS::CFile file;
		if( !file.Open( filename, PFS::FM_EXCL, PFS::FA_RDONLY))
		{
			return false;
		}
		return PFS::CMetaFileRead::DoRead( file, mim);
	}

	// ����װ�����е� meta ��Ϣ������Ŀ¼��������Ŀ¼�� .file2.meta �ļ���
	static bool ReadPFSAllMetaInfos( const std::wstring& mountstr, PFS::CMetaInfoMap& mim, 
		PFSX::SetupPathVector& validpaths)
	{
		PFSX::SetupPathVector paths;
		if( !PFSX::CSetupMetaFile::DoRead( mountstr+L"/"+SETUP_META, paths))
		{
			return false;
		}

		PFS::CMetaInfoMap mim_1_0;	// �� 1.0 �汾 .files.meta ��Ҫ��Ϊ����֤
		if( !ReadMetaInfos( mountstr+L"/"+FILES_META, mim_1_0))
		{
			return false;
		}

		paths.push_back( std::wstring());	// �����Ŀ¼
		for( PFSX::SetupPathVector::iterator itor=paths.begin(); itor!=paths.end(); itor++)
		{
			const std::wstring& path = *itor;
			PFS::CMetaInfoMap mimChild;
			if( ReadMetaInfos( mountstr+path+L"/"+FILES2_META, mimChild))
			{
				for( PFS::CMetaInfoMap::iterator i=mimChild.begin(), e=mimChild.end(); i!=e; i++)
				{
					mim.insert( std::make_pair( path+i->first, i->second));
				}
				validpaths.push_back( path);
			}
			else
			{
				// Ϊ�˼�����ǰ�汾�İ�װ�����������ļ���meta��Ϣ���ڸ�Ŀ¼�� .files2.meta �ļ��У�
				// ��������������Ⱥ��ԣ�����������һ��У��
			}
		}

		// ���Ҫ��֤һ�� 1.0 �汾��meta��Ϣ��1.1�汾����Ϣ�Ƿ����
		return ( mim_1_0.size() == mim.size());
	}
};