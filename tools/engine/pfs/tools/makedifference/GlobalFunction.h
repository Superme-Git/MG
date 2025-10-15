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

	// 读安装包所有的 meta 信息（读根目录和所有子目录的 .file2.meta 文件）
	static bool ReadPFSAllMetaInfos( const std::wstring& mountstr, PFS::CMetaInfoMap& mim, 
		PFSX::SetupPathVector& validpaths)
	{
		PFSX::SetupPathVector paths;
		if( !PFSX::CSetupMetaFile::DoRead( mountstr+L"/"+SETUP_META, paths))
		{
			return false;
		}

		PFS::CMetaInfoMap mim_1_0;	// 读 1.0 版本 .files.meta 主要是为了验证
		if( !ReadMetaInfos( mountstr+L"/"+FILES_META, mim_1_0))
		{
			return false;
		}

		paths.push_back( std::wstring());	// 插入根目录
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
				// 为了兼容以前版本的安装包（将所有文件的meta信息放在根目录的 .files2.meta 文件中）
				// 所以这里出错了先忽略，函数最后进行一次校验
			}
		}

		// 最后要验证一下 1.0 版本的meta信息和1.1版本的信息是否相等
		return ( mim_1_0.size() == mim.size());
	}
};