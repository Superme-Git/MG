///=============================================================================
/// Product   : PFS
/// Project   : pfslib
/// Module    : setupmetafile.cpp
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	CSetupMetaFile - .setup.meta文件的操作处理类
///
///		因为现阶段，我们以标准XML方式实现的setup.meta文件，
///	所以，下面使用的MSXMLDOM接口进行的操作。
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-10-16 Created.
/// 	V1.0 - 2009-11-11 Update to version 1.0:
///			1. adding attribute v(version) to root node.
///			2. adding attribute h(hash) to f node.
///			3. adding attribute l(flags) to f node.
///=============================================================================
#include <atlbase.h>
#include "../../setupmetafile.h"
#include "../../stringhelper.h"
#include "../../../xmlio/msdom.h"

namespace PFSX
{
	bool CSetupMetaFile::Load( PFS::CBaseFile& file, SetupPathVector& paths )
	{
		MSXMLDOM::CReader reader;
		if( PFS::EC_SUCCESS != reader.OpenFile( file))
		{
			PFS::CEnv::GetOutLogInstance()->Print( L"Error: .setup.meta - open failed.\n");
			return false;
		}

		MSXMLDOM::CReadNode root, child;
		reader.GetRootNode(root);
		std::wstring strValue;
		if( root.GetAttribute( L"v", strValue ) )
		{
			if( strValue != L"1.0" )
			{
				// 不支持其它版本;
				PFS::CEnv::GetOutLogInstance()->Print( L"Error: .setup.meta/.master.meta - invalid version value(%s).\n", strValue.c_str() );
				PFS::CEnv::SetLastError( PFS::EC_INVALID_META_FILE );
				return false;
			}
		}

		size_t count = root.GetChildrenCount();

		for( size_t i=0; i<count; i++)
		{
			if( !root.GetChildAt( i, child ) )
			{
				continue;
			}

			if( !child.GetAttribute( L"n", strValue ) )
			{
				PFS::CEnv::GetOutLogInstance()->Print( L"Warning: .setup.meta/.master.meta - missing attribute - n.\n");
				continue;
			}

			paths.push_back( strValue );
		}

		return true;
	}

	bool CSetupMetaFile::Load( PFS::CBaseFile& file, SetupMetaVector& metas )
	{
		MSXMLDOM::CReader reader;
		if( PFS::EC_SUCCESS != reader.OpenFile( file))
		{
			PFS::CEnv::GetOutLogInstance()->Print( L"Error: .setup.meta/.master.meta - open failed.\n");
			return false;
		}

		MSXMLDOM::CReadNode root, child;
		reader.GetRootNode(root);
		std::wstring strValue;
		if( root.GetAttribute( L"v", strValue ) )
		{
			if( strValue != L"1.0" )
			{
				// 不支持其它版本;
				PFS::CEnv::GetOutLogInstance()->Print( L"Error: .setup.meta/.master.meta - invalid version value(%s).\n", strValue.c_str() );
				PFS::CEnv::SetLastError( PFS::EC_INVALID_META_FILE );
				return false;
			}
		}

		if( root.GetAttribute( L"o", strValue ) )
		{
			if( strValue == L"1" || strValue == L"true" )
			{
				metas.SetOriginal( true );
			}
			else
			{
				metas.SetOriginal( false );
			}
		}

		size_t count = root.GetChildrenCount();
		
		for( size_t i=0; i<count; i++)
		{
			if( !root.GetChildAt( i, child ) )
			{
				continue;
			}
			
			metas.push_back( SetupMetaInfo() );
			
			SetupMetaInfo& rObj = metas.back();

			if( !child.GetAttribute( L"n", rObj.GetName() ) )
			{
				metas.pop_back();
				PFS::CEnv::GetOutLogInstance()->Print( L"Warning: .setup.meta/.master.meta - missing attribute - n.\n");
				continue;
			}

			// Optional attribute 
			if( child.GetAttribute( L"h", strValue ) ) 
			{
				rObj.SetHash( CStringHelper::String2Hash( strValue ) );
			}
	
			// Optional attribute 
			if( child.GetAttribute( L"h2", strValue ) ) 
			{
				rObj.SetHash2( CStringHelper::String2Hash( strValue ) );
			}

			// Optional attribute
			if( child.GetAttribute( L"l", strValue ) ) 
			{
				rObj.SetFlags( CStringHelper::String2Int( strValue ) );
			}
		}

		return true;
	}

	static void _WriteContent( MSXMLDOM::CWriter& writer, const SetupMetaVector& metas )
	{
		MSXMLDOM::CWriterNode root, child;
		writer.GetRootNode( root );

		if( !root.SetAttribute( L"v", L"1.0" ) )
		{
			PFS::CEnv::GetOutLogInstance()->Print( L"Warning: .setup.meta/.master.meta - failed to set version value(1.0).\n" );
			// TODO: throw exception!
		}

		if( metas.IsOriginal() )
		{
			// Only process it for true value
			if( !root.SetAttribute( L"o", L"1") )
			{
				PFS::CEnv::GetOutLogInstance()->Print( L"Warning: .setup.meta/.master.meta - failed to set original value(1).\n" );
				// throw exception!
			}
		}

		for( SetupMetaVector::const_iterator itor = metas.begin(); itor!=metas.end(); ++itor)
		{
			root.CreateSubElement( L"f", child);
			const SetupMetaInfo& rObj = *itor;
			child.SetAttribute( L"n", rObj.GetName().c_str() );
			if( rObj.GetHash() != 0 )
				child.SetAttribute( L"h", CStringHelper::Hash2String( rObj.GetHash() ).c_str() );
			if( rObj.GetHash2() != 0 )
				child.SetAttribute( L"h2", CStringHelper::Hash2String( rObj.GetHash2() ).c_str() );
			if( rObj.GetFlags() != 0 )
				child.SetAttribute( L"l", CStringHelper::Int2String( rObj.GetFlags() ).c_str() );
		}
	}

	bool CSetupMetaFile::SaveAsData( PFS::CDataBlock& out, const SetupMetaVector& metas )
	{
		MSXMLDOM::CWriter writer;
		_WriteContent( writer, metas);
		return PFS::EC_SUCCESS == writer.SaveData( out);
	}

	bool CSetupMetaFile::Save( PFS::CBaseFile& file, const SetupMetaVector& metas )
	{
		MSXMLDOM::CWriter writer;
		_WriteContent( writer, metas);
		return PFS::EC_SUCCESS == writer.Save( file);
	}

	void SubtractSetupMeta( const SetupMetaVector& rMinuend, 
		const SetupMetaVector& rSubtrahend, SetupMetaVector& rDifference )
	{
		//
		// 说明，SetupMetaVector尽管为vector，但实际上它应该是一个有序表
		// 那么，我们应该对输入的rMinuend和rSubstrhend进行clone，
		// 然后对他的副本进行排序。
		// 代码如下：
		SetupMetaVector rMinuend2 = rMinuend; 
		SetupMetaVector rSubtrahend2 = rSubtrahend; 
		std::sort( rMinuend2.begin(), rMinuend2.end() );
		std::sort( rSubtrahend2.begin(), rSubtrahend2.end() );
		rDifference.clear();

		PFSX::SetupMetaVector::const_iterator itrS = rMinuend2.begin();
		PFSX::SetupMetaVector::const_iterator itrL = rSubtrahend2.begin();

		for ( ; itrS != rMinuend2.end() &&  itrL != rSubtrahend2.end();  ++ itrL )
		{
			for( ; itrS != rMinuend2.end() && itrS->GetName() < itrL->GetName(); ++ itrS )
			{
				rDifference.push_back( *itrS );
			}

			if ( itrS == rMinuend2.end() )
			{
				break;
			}

			// 如果名字相同，判断是否一样，将不一样的加到更新列表中
			if ( itrS->GetName() == itrL->GetName() ) 
			{
				if ( itrS->IsChanged( *itrL) )
				{
					rDifference.push_back( *itrS );
				}

				++ itrS;
			}
		}

		for( ; itrS != rMinuend2.end() ; ++ itrS )
		{
			rDifference.push_back( *itrS );
		}
	}

	void DiffSetupMeta( const SetupMetaVector& rFullSet, 
		const SetupMetaVector& rSubSet, SetupMetaVector& rDifference )
	{
		rDifference.clear();

		PFSX::SetupMetaVector::const_iterator itrS = rFullSet.begin();
		PFSX::SetupMetaVector::const_iterator itrL = rSubSet.begin();

		for ( ; itrS != rFullSet.end() &&  itrL != rSubSet.end();  ++ itrL )
		{
			for( ; itrS != rFullSet.end() && itrS->GetName() < itrL->GetName(); ++ itrS )
			{
				rDifference.push_back( *itrS );
			}

			if ( itrS == rFullSet.end() )
			{
				break;
			}

			if ( itrS->GetName() == itrL->GetName() ) 
			{
				++ itrS;
			}
		}

		for( ; itrS != rFullSet.end() ; ++ itrS )
		{
			rDifference.push_back( *itrS );
		}
	}

	void PurgeSetupMeta( SetupMetaVector& rSet, const SetupMetaVector& rSetToPurge )
	{
		SetupMetaVector rNewSet;
		DiffSetupMeta(rSet, rSetToPurge, rNewSet);
		rSet.swap(rNewSet);
	}

	// 将 src 的项合并进 des
	void MergeSetupMeta( const SetupMetaVector& src, SetupMetaVector& des)
	{
		for( SetupMetaVector::const_iterator i = src.begin(); i!=src.end(); ++i)
		{
			const SetupMetaInfo& smi = *i;
			SetupMetaVector::iterator iFind = std::find( des.begin(), des.end(), smi);
			if( iFind == des.end())
			{
				des.push_back( smi);
			}
			else
			{
				iFind->SetHash( i->GetHash());
				iFind->SetHash2( i->GetHash2());
				iFind->SetFlags( i->GetFlags());
			}
		}
	}

} // namespace PFSX