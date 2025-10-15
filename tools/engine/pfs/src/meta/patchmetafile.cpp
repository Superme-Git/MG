#include "../../pfs2.h"
#include "../../patchmetafile.h"
#include <sstream>

namespace PFSX
{

	const wchar_t VERSION[] = L"version";
	const wchar_t PATCHES[] = L"patches";
	const wchar_t PATCH[] = L"patch";
	const wchar_t ID[] = L"id";
	const wchar_t NAME[] = L"name";
	const wchar_t SIZE[] = L"size";
	const wchar_t HASH[] = L"hash";
	const wchar_t MD5[] = L"md5";
	const wchar_t UPDATES[] = L"updates";
	const wchar_t UPDATE[] = L"update";
	const wchar_t FROM[] = L"from";
	const wchar_t USE[] = L"use";
	const wchar_t TO[] = L"to";

	const wchar_t PATCHES2[] = L"patches2";
	const wchar_t UPDATES2[] = L"updates2";

	void CPatchMetaFile::Close()
	{
		m_version = 0;
		m_patches.clear();
		m_updates.clear();
		m_patches2.clear();
		m_updates2.clear();
	}

	bool CPatchMetaFile::LoadFrom( PFS::CBaseFile& file)
	{
		Close();

		XMLIO::CFileReader reader;

		int xec = reader.OpenFile( file);
		if( xec != XMLIO::EC_SUCCESS )
		{
			return false;
		}

		XMLIO::CINode root;
		reader.GetRootNode( root );
		m_version = root.GetAttributeUnsignedInteger( VERSION ) ;
		int count = root.GetChildrenCount();
		for( int i=0; i<count; i++)
		{
			XMLIO::CINode child;
			root.GetChildAt( i, child);
			std::wstring name = child.GetName( );

			if( name == PATCHES)
			{
				if( !LoadPatches( child))
				{
					return false;
				}
			}
			else if( name == UPDATES)
			{
				if( !LoadUpdates( child))
				{
					return false;
				}
			}
			else if(name == PATCHES2)
			{
				if( !LoadPatches( child, true))
				{
					return false;
				}
			}
			else if( name == UPDATES2)
			{
				if( !LoadUpdates( child, true))
				{
					return false;
				}
			}
		}

		return true;
	}

	bool CPatchMetaFile::LoadPatches( const XMLIO::CINode& node, bool bSpecial)
	{
		int count = node.GetChildrenCount();
		for( int i=0; i<count; i++)
		{
			XMLIO::CINode child;
			node.GetChildAt( i, child);

			int id = child.GetAttributeInteger( ID);
			PatchInfo pi;
			child.GetAttribute( NAME, pi.filename);
			pi.size = child.GetAttributeUnsignedInteger( SIZE);
			pi.versionFrom = child.GetAttributeUnsignedInteger( FROM);
			pi.versionTo = child.GetAttributeUnsignedInteger( TO);

			// todo 还没定hash值怎么保存
			pi.hash = (PFS::FILE_DATA_HASH)child.GetAttributeInt64( HASH );

			// MD5 - for p2sp
			child.GetAttribute( MD5, pi.md5 );

			CPatchInfoMap::_Pairib pb;
			if(bSpecial)
				pb = m_patches2.insert( std::make_pair( id, pi));
			else
				pb = m_patches.insert( std::make_pair( id, pi));

			if( !pb.second)
			{
				return false;
			}
		}
		return true;
	}

	// 从sSrc中得到以cDelimiter分界的int数组
	bool GetIntVectorFromStr( std::vector<int>& iv, const std::wstring& sSrc, const wchar_t cDelimiter)
	{
		std::wistringstream wiss( sSrc);
		std::wstring token;
		while (true)
		{
			std::getline( wiss, token, cDelimiter);
			if( wiss.fail())
				break;
			iv.push_back( _wtoi( token.c_str()));
		}
		return true;
	}

	bool CPatchMetaFile::LoadUpdates( const XMLIO::CINode& node, bool bSpecial)
	{
		DWORD version = node.GetAttributeUnsignedInteger( VERSION);

		CUpdateInfoVector uiv;
		int count = node.GetChildrenCount();
		uiv.reserve( count);
		for( int i=0; i<count; i++)
		{
			XMLIO::CINode child;
			node.GetChildAt( i, child);

			UpdateInfo ui;
			ui.versionFrom = child.GetAttributeUnsignedInteger( FROM);
			ui.versionTo = child.GetAttributeUnsignedInteger( TO);
			std::wstring usestr;
			child.GetAttribute( USE, usestr);
			GetIntVectorFromStr( ui.use, usestr);

			uiv.push_back( ui);
		}
		
		// 给 uiv 排序
		std::sort( uiv.begin(), uiv.end());
		
		CUpdateInfoMap::_Pairib pb;
		if(bSpecial)
			pb = m_updates2.insert( std::make_pair( version, uiv));
		else
			pb = m_updates.insert( std::make_pair( version, uiv));

		if( !pb.second)
		{
			return false;
		}

		return true;
	}

	bool CPatchMetaFile::Write( PFS::CBaseFile& file) const
	{
		XMLIO::CFileWriter writter;
		XMLIO::CONode root;
		writter.GetRootNode( root);
		root.SetAttribute( VERSION, (unsigned int)m_version);

		if( !WritePatches( root))
		{
			return false;
		}

		if( !WriteUpdates( root))
		{
			return false;
		}

		if( !WritePatches( root, true))
		{
			return false;
		}

		if( !WriteUpdates( root, true))
		{
			return false;
		}

		return ( XMLIO::EC_SUCCESS == writter.Save( file));
	}

	bool CPatchMetaFile::WritePatches( XMLIO::CONode& node, bool bSpecial) const
	{
		XMLIO::CONode patchesNode;
		if(bSpecial)
		{
			node.CreateSubElement( PATCHES2, patchesNode);
			for( CPatchInfoMap::const_iterator i=m_patches2.begin(), e=m_patches2.end(); i!=e; ++i)
			{
				XMLIO::CONode child;
				patchesNode.CreateSubElement( PATCH, child);
				child.SetAttribute( ID, i->first);
				const PatchInfo& pi = i->second;
				child.SetAttribute( NAME, pi.filename);
				child.SetAttribute( SIZE, (unsigned int)pi.size);
				child.SetAttribute( FROM, (unsigned int)pi.versionFrom);
				child.SetAttribute( TO, (unsigned int)pi.versionTo);

				// todo 还没定hash值怎么保存
				child.SetAttribute( HASH, (__int64)pi.hash);

				// MD5 - for p2sp
				if( !pi.md5.empty() )
					child.SetAttribute( MD5, pi.md5 );
			}
		}
		else
		{
			node.CreateSubElement( PATCHES, patchesNode);
			for( CPatchInfoMap::const_iterator i=m_patches.begin(), e=m_patches.end(); i!=e; ++i)
			{
				XMLIO::CONode child;
				patchesNode.CreateSubElement( PATCH, child);
				child.SetAttribute( ID, i->first);
				const PatchInfo& pi = i->second;
				child.SetAttribute( NAME, pi.filename);
				child.SetAttribute( SIZE, (unsigned int)pi.size);
				child.SetAttribute( FROM, (unsigned int)pi.versionFrom);
				child.SetAttribute( TO, (unsigned int)pi.versionTo);

				// todo 还没定hash值怎么保存
				child.SetAttribute( HASH, (__int64)pi.hash);

				// MD5 - for p2sp
				if( !pi.md5.empty() )
					child.SetAttribute( MD5, pi.md5 );
			}
		}

		return true;
	}

	// 从int数组中得到以cDelimiter分界的字符串sSrc
	bool GetStrFromIntVector( const std::vector<int>& iv, std::wstring& sDes, const wchar_t cDelimiter)
	{
		std::wostringstream woss;
		for( std::vector<int>::const_iterator i=iv.begin(), e=iv.end(); i!=e; ++i)
		{
			if( i != iv.begin())
			{
				woss << cDelimiter;
			}
			woss << *i;
		}
		sDes = woss.str();
		return true;
	}

	bool CPatchMetaFile::WriteUpdates( XMLIO::CONode& node, bool bSpecial) const
	{
		if(bSpecial)
		{
			for( CUpdateInfoMap::const_iterator i=m_updates2.begin(), e=m_updates2.end(); i!=e; ++i)
			{
				XMLIO::CONode child;
				node.CreateSubElement( UPDATES2, child);
				child.SetAttribute( VERSION, (unsigned int)i->first);

				const CUpdateInfoVector& uiv = i->second;
				for( CUpdateInfoVector::const_iterator iV=uiv.begin(), eV=uiv.end(); iV!=eV; ++iV)
				{
					XMLIO::CONode grandson;
					child.CreateSubElement( UPDATE, grandson);

					const UpdateInfo& ui = *iV;
					grandson.SetAttribute( FROM, (unsigned int)ui.versionFrom);
					grandson.SetAttribute( TO, (unsigned int)ui.versionTo);
					std::wstring usestr;
					GetStrFromIntVector( ui.use, usestr);
					grandson.SetAttribute( USE, usestr);
				}
			}
		}
		else
		{
			for( CUpdateInfoMap::const_iterator i=m_updates.begin(), e=m_updates.end(); i!=e; ++i)
			{
				XMLIO::CONode child;
				node.CreateSubElement( UPDATES, child);
				child.SetAttribute( VERSION, (unsigned int)i->first);

				const CUpdateInfoVector& uiv = i->second;
				for( CUpdateInfoVector::const_iterator iV=uiv.begin(), eV=uiv.end(); iV!=eV; ++iV)
				{
					XMLIO::CONode grandson;
					child.CreateSubElement( UPDATE, grandson);

					const UpdateInfo& ui = *iV;
					grandson.SetAttribute( FROM, (unsigned int)ui.versionFrom);
					grandson.SetAttribute( TO, (unsigned int)ui.versionTo);
					std::wstring usestr;
					GetStrFromIntVector( ui.use, usestr);
					grandson.SetAttribute( USE, usestr);
				}
			}
		}
		return true;
	}

	// 根据版本得到通用patch信息
	bool CPatchMetaFile::GetPatchInfoFromVersion( PatchInfo& pi, DWORD versionFrom, DWORD versionTo)
	{
		CPatchInfoMap pim;
		for( PFSX::CUpdateInfoMap::const_iterator iMap=m_updates.begin(), eMap=m_updates.end(); iMap!=eMap; ++iMap)
		{
			const CUpdateInfoVector& uiv = iMap->second;
			for( CUpdateInfoVector::const_iterator i=uiv.begin(), e=uiv.end(); i!=e; ++i)
			{
				if( i->versionTo <= versionFrom)
				{
					// 因为uiv已经是排序过的，一旦出现这个case说明uiv中没有合适的补丁包
					break;
				}

				if( i->versionFrom <= versionFrom)
				{
					if( FillPatches( pim, i->use))
					{
						break;
					}
					pim.clear();
				}
			}
		}
		for( CPatchInfoMap::const_iterator i=pim.begin(), e=pim.end(); i!=e; i++)
		{
			const PatchInfo& info = i->second;
			if( info.versionFrom <= versionFrom && info.versionTo > versionFrom)
			{
				pi = info;
				return true;
			}
		}
		return false;
	}

	// 根据版本得到通用patch列表
	bool CPatchMetaFile::GetPatchInfosFromVersion(
		CPatchInfoVector& piVector, DWORD versionFrom, DWORD versionTo)
	{
		const std::vector<int>* pUsePreferenced = NULL;
		for( PFSX::CUpdateInfoMap::const_iterator iMap = m_updates.begin(),
			eMap = m_updates.end(); iMap!= eMap; ++iMap)
		{
			const CUpdateInfoVector& uiv = iMap->second;
			for( CUpdateInfoVector::const_iterator i=uiv.begin(), e=uiv.end(); i!=e; ++i)
			{
				if( i->versionTo <= versionFrom)
				{
					// 因为uiv已经是排序过的，一旦出现这个case说明uiv中没有合适的补丁包
					break;
				}

				if( i->versionFrom <= versionFrom)
				{
					// 找到了最合适的补丁包列表
					pUsePreferenced = &i->use;
					break;
				}
			}
		}

		// 将补丁包id列表转换成补丁包信息列表
		if( pUsePreferenced != NULL && FillPatches( piVector, *pUsePreferenced, versionFrom) )
		{
			return true;
		}

		return false;
	}

	// 根据版本得到通用patch列表
	bool CPatchMetaFile::GetPatchInfosFromVersionEX(
		CPatchInfoVector& piVector, DWORD versionFrom, DWORD versionTo)
	{
		piVector.clear();
		DWORD versionFrom_new = versionFrom;
		do 
		{
			const std::vector<int>* pUsePreferenced = NULL;
			for( PFSX::CUpdateInfoMap::const_iterator iMap = m_updates.begin(),
				eMap = m_updates.end(); iMap!= eMap; ++iMap)
			{
				const CUpdateInfoVector& uiv = iMap->second;
				for( CUpdateInfoVector::const_iterator i=uiv.begin(), e=uiv.end(); i!=e; ++i)
				{
					if( i->versionTo <= versionFrom)
					{
						// 因为uiv已经是排序过的，一旦出现这个case说明uiv中没有合适的补丁包
						break;
					}

					if( i->versionFrom <= versionFrom)
					{
						// 找到了一项合适的补丁包列表
						pUsePreferenced = &i->use;
						versionFrom_new = i->versionTo;
						break;
					}
				}
			}

			// 将补丁包id列表转换成补丁包信息列表
			if( pUsePreferenced != NULL )
			{
				bool bres = FillPatchesEX( piVector, *pUsePreferenced, versionFrom );
				if(!bres)
				{ //配置文件错误的情况... 还有别的可能吗？-----todo
					return false;
				}
				versionFrom = versionFrom_new;
			}
			else //找不到更新序列的情况跳出循环
				break;

		} while(versionFrom_new < versionTo);

		return (versionFrom_new == versionTo);
	}

	// 根据版本得到专用patch信息
	bool CPatchMetaFile::GetPatchInfoFromVersion2( PatchInfo& pi, DWORD versionFrom, DWORD versionTo)
	{
		CPatchInfoMap pim;
		for( PFSX::CUpdateInfoMap::const_iterator iMap=m_updates2.begin(), eMap=m_updates2.end(); iMap!=eMap; ++iMap)
		{
			const CUpdateInfoVector& uiv = iMap->second;
			for( CUpdateInfoVector::const_iterator i=uiv.begin(), e=uiv.end(); i!=e; ++i)
			{
				if( i->versionTo <= versionFrom)
				{
					// 因为uiv已经是排序过的，一旦出现这个case说明uiv中没有合适的补丁包
					break;
				}

				if( i->versionFrom == versionFrom)
				{
					if( FillPatches2( pim, i->use))
					{
						break;
					}
					pim.clear();
				}
			}
		}
		for( CPatchInfoMap::const_iterator i=pim.begin(), e=pim.end(); i!=e; i++)
		{
			const PatchInfo& info = i->second;
			if( info.versionFrom <= versionFrom && info.versionTo > versionFrom)
			{
				pi = info;
				return true;
			}
		}
		return false;
	}

	// 根据版本得到专用patch列表
	bool CPatchMetaFile::GetPatchInfosFromVersion2(
		CPatchInfoVector& piVector, DWORD versionFrom, DWORD versionTo)
	{
		piVector.clear();
		DWORD versionFrom_new = versionFrom;
		do 
		{
			const std::vector<int>* pUsePreferenced = NULL;
			for( PFSX::CUpdateInfoMap::const_iterator iMap = m_updates2.begin(),
				eMap = m_updates2.end(); iMap!= eMap; ++iMap)
			{
				const CUpdateInfoVector& uiv = iMap->second;
				for( CUpdateInfoVector::const_iterator i=uiv.begin(), e=uiv.end(); i!=e; ++i)
				{
					if( i->versionTo <= versionFrom)
					{
						// 因为uiv已经是排序过的，一旦出现这个case说明uiv中没有合适的补丁包
						break;
					}

					if( i->versionFrom == versionFrom)
					{
						// 找到了一个合适的补丁包列表
						pUsePreferenced = &i->use;
						versionFrom_new = i->versionTo;
						break;
					}
				}
			}

			// 将补丁包id列表转换成补丁包信息列表
			if( pUsePreferenced != NULL )
			{
				bool bres = FillPatches2( piVector, *pUsePreferenced, versionFrom );
				if(!bres)
				{ //配置文件错误的情况... 还有别的可能吗？-----todo
					return false;
				}
				versionFrom = versionFrom_new;
			}
			else
				break;
		} while(versionFrom_new < versionTo);

		return (versionFrom_new == versionTo);
	}

	//根据包的大小确定一个最小的包
	bool CPatchMetaFile::GetMinPatchInfoFromVersion( PatchInfo& pi, DWORD versionFrom, DWORD versionTo)
	{
		PatchInfo patch;
		bool bres1 = GetPatchInfoFromVersion( patch, versionFrom, versionTo);
		PatchInfo patch2;
		bool bres2 = GetPatchInfoFromVersion2( patch2, versionFrom, versionTo);
		if(bres1 && bres2)
		{
			pi = patch.size<patch2.size ? patch:patch2;
			return true;
		}
		else if(bres1)
		{
			pi = patch;
			return true;
		}
		else if(bres2)
		{
			pi = patch2;
			return true;
		}
		else
			return false; //没有找到适合用于当前更新的包
	}

	//根据包的大小确定一个最小的包序列
	bool CPatchMetaFile::GetMinPatchInfosFromVersion( CPatchInfoVector& piVector, DWORD versionFrom, DWORD versionTo)
	{
		CPatchInfoVector pi;
		bool bres1 = GetPatchInfosFromVersionEX(pi, versionFrom, versionTo);
		CPatchInfoVector pi2;
		bool bres2 = GetPatchInfosFromVersion2(pi2, versionFrom, versionTo);
		if(bres1 && bres2)
		{
			fsize_t patchSize1 = 0;
			for( std::vector< PatchInfo >::const_iterator i=pi.begin(), e=pi.end(); i!=e; ++i)
				patchSize1 += i->size;

			fsize_t patchSize2 = 0;
			for( std::vector< PatchInfo >::const_iterator i=pi2.begin(), e=pi2.end(); i!=e; ++i)
				patchSize2 += i->size;

			piVector = patchSize1<patchSize2 ? pi:pi2;
			return true;
		}
		else if(bres1)
		{
			piVector = pi;
			return true;
		}
		else if(bres2)
		{
			piVector = pi2;
			return true;
		}
		else
			return false;
	}

	bool CPatchMetaFile::FillPatches( CPatchInfoMap& pim, const std::vector<int>& use)
	{
		pim.clear();
		for( std::vector<int>::const_iterator i=use.begin(), e=use.end(); i!=e; ++i)
		{
			CPatchInfoMap::iterator iFind = m_patches.find( *i);
			if( iFind == m_patches.end())
			{
				return false;
			}
			pim.insert( *iFind);
		}

		return !pim.empty();
	}

	bool CPatchMetaFile::FillPatches( CPatchInfoVector& rPatchInfoVector, const std::vector<int>& use, DWORD versionFrom)
	{
		rPatchInfoVector.clear();
		for( std::vector<int>::const_iterator i=use.begin(), e=use.end(); i!=e; ++i)
		{
			CPatchInfoMap::iterator iFind = m_patches.find( *i);
			if( iFind == m_patches.end())
			{
				return false;
			}

			if( iFind->second.versionTo <= versionFrom)
			{
				continue;
			}

			rPatchInfoVector.push_back( iFind->second );
		}

		return !rPatchInfoVector.empty();
	}

	bool CPatchMetaFile::FillPatchesEX( CPatchInfoVector& rPatchInfoVector, const std::vector<int>& use, DWORD versionFrom)
	{
		for( std::vector<int>::const_iterator i=use.begin(), e=use.end(); i!=e; ++i)
		{
			CPatchInfoMap::iterator iFind = m_patches.find( *i);
			if( iFind == m_patches.end())
			{
				return false;
			}

			if( iFind->second.versionTo <= versionFrom)
			{
				continue;
			}

			std::vector< PatchInfo >::iterator it;
			it = std::find(rPatchInfoVector.begin(), rPatchInfoVector.end(), iFind->second);
			if(it == rPatchInfoVector.end()) //判断该更新包是否已经存在了
			{
				rPatchInfoVector.push_back( iFind->second );
			}
		}

		return !rPatchInfoVector.empty();
	}

	bool CPatchMetaFile::FillPatches2( CPatchInfoMap& pim, const std::vector<int>& use)
	{
		pim.clear();
		for( std::vector<int>::const_iterator i=use.begin(), e=use.end(); i!=e; ++i)
		{
			CPatchInfoMap::iterator iFind = m_patches2.find( *i);
			if( iFind == m_patches2.end())
			{
				return false;
			}
			pim.insert( *iFind);
		}

		return !pim.empty();
	}

	bool CPatchMetaFile::FillPatches2( CPatchInfoVector& rPatchInfoVector, const std::vector<int>& use, DWORD versionFrom)
	{
		for( std::vector<int>::const_iterator i=use.begin(), e=use.end(); i!=e; ++i)
		{
			CPatchInfoMap::iterator iFind = m_patches2.find( *i);
			if( iFind == m_patches2.end())
			{
				return false;
			}

			if( iFind->second.versionTo <= versionFrom)
			{
				continue;
			}

			std::vector< PatchInfo >::iterator it;
			it = std::find(rPatchInfoVector.begin(), rPatchInfoVector.end(), iFind->second);
			if(it == rPatchInfoVector.end()) //判断该更新包是否已经存在了
			{
				rPatchInfoVector.push_back( iFind->second );
			}
		}

		return !rPatchInfoVector.empty();
	}

	bool PatchHasExist( const std::vector<std::vector<int> >& uses, const std::vector<int>& stack, int id)
	{
		for( std::vector<std::vector<int> >::const_iterator i=uses.begin(), e=uses.end(); i!=e; ++i)
		{
			const std::vector<int>& use = *i;
			size_t len = stack.size();
			if( use.size() > len)
			{
				if( std::equal( use.begin(), use.begin()+len, stack.begin()) && use[len] == id)
				{
					return true;
				}
			}
		}

		return false;
	}

	// 根据 versionFrom 和 versionTo 列出所有能使用的通用补丁包列表
	bool CPatchMetaFile::ListAllPatchsFromVersion( std::vector<std::vector<int> >& uses, DWORD versionFrom, DWORD versionTo)
	{
		std::vector<std::vector<int> >	failed;
		std::vector<int> stack;
		DWORD dwStart = versionFrom;

		while( true)
		{
			if( stack.empty())
			{
				dwStart = versionFrom;
			}
			else
			{
				 CPatchInfoMap::iterator i = m_patches.find( stack.back());
				 dwStart = i->second.versionTo;
			}
			bool res = false;			// 是否找到匹配的项
			bool hasMatch = false;		// 是否还存在匹配的项
			CPatchInfoMap::const_iterator i=m_patches.begin();
			for( CPatchInfoMap::const_iterator e=m_patches.end(); i!=e; ++i )
			{
				const PatchInfo& ui = i->second;
				if( ui.versionTo <= dwStart)
				{
					continue;
				}
				if( ui.versionFrom > dwStart)
				{
					hasMatch = true;
					continue;
				}
				if( PatchHasExist( failed, stack, i->first))
				{
					continue;
				}
				if( PatchHasExist( uses, stack, i->first))
				{
					continue;
				}
				stack.push_back( i->first);
				dwStart = ui.versionTo;
				res = true;
				hasMatch = true;
				break;
			}
			if( res)
			{
				if( dwStart >= versionTo)
				{
					// 已经找到一个
					uses.push_back( stack);
					stack.pop_back();	// 出栈
				}
				else
				{
					if( !hasMatch)
					{
						break;	// 全遍历了一遍
					}
				}
			}
			else
			{
				if( stack.empty())
				{
					// 已经找不到了，退出
					break;
				}
				failed.push_back( stack);
				stack.pop_back();	// 出栈
			}
		}
		return !uses.empty();
	}

	// 根据 versionFrom 和 versionTo 列出所有能使用的专用补丁包列表
	bool CPatchMetaFile::ListAllPatchsFromVersion2( std::vector<std::vector<int> >& uses, DWORD versionFrom, DWORD versionTo)
	{
		std::vector<std::vector<int> >	failed;
		std::vector<int> stack;
		DWORD dwStart = versionFrom;

		while( true)
		{
			if( stack.empty())
			{
				dwStart = versionFrom;
			}
			else
			{
				 CPatchInfoMap::iterator i = m_patches2.find( stack.back());
				 dwStart = i->second.versionTo;
			}
			bool res = false;			// 是否找到匹配的项
			bool hasMatch = false;		// 是否还存在匹配的项
			CPatchInfoMap::const_iterator i=m_patches2.begin();
			for( CPatchInfoMap::const_iterator e=m_patches2.end(); i!=e; ++i )
			{
				const PatchInfo& ui = i->second;
				if( ui.versionTo <= dwStart)
				{
					continue;
				}
				if( ui.versionFrom > dwStart)
				{
					hasMatch = true;
					continue;
				}
				if( PatchHasExist( failed, stack, i->first))
				{
					continue;
				}
				if( PatchHasExist( uses, stack, i->first))
				{
					continue;
				}
				stack.push_back( i->first);
				dwStart = ui.versionTo;
				res = true;
				hasMatch = true;
				break;
			}
			if( res)
			{
				if( dwStart == versionTo)
				{
					// 已经找到一个
					uses.push_back( stack);
					stack.pop_back();	// 出栈
				}
				else
				{
					if( !hasMatch)
					{
						break;	// 全遍历了一遍
					}
				}
			}
			else
			{
				if( stack.empty())
				{
					// 已经找不到了，退出
					break;
				}
				failed.push_back( stack);
				stack.pop_back();	// 出栈
			}
		}
		return !uses.empty();
	}

	bool GetRemotePatchMetaFile( const std::wstring& url, CPatchMetaFile& pmf)
	{
		std::wstring urlPatch = url + PATCH_META_NAME;
		PFSX::CInetFile netfile;
		if( !netfile.Open( urlPatch ))
			return false;

		// 因为网络文件CInetFile对Seek操作支持不是很好
		// 所以先从网络上读到内存中，然后作为CImageFile装载
		PFS::CDataBlock db;
		size_t filelen = (size_t)netfile.GetSize();
		db.Resize( filelen);
		if( filelen != netfile.Read( db.GetData(), filelen))
		{
			return false;
		}

		PFS::CImageFile imf( db);
		if( !pmf.LoadFrom( imf))
		{
			return false;
		}
		return true;
	}

};		// namespace PFSX