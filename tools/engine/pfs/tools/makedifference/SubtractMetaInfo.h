#pragma once

class CSubtractMetaInfo
{
	CSubtractMetaInfo() {}
	~CSubtractMetaInfo() {}
	
	//
	// summary:
	//		比较两个MetaInfo是否不一样
	//		这里只比较文件大小和Hash值
	//
	static bool IsEqualMetaInfo( const PFS::CMetaInfo& rLeft, const PFS::CMetaInfo& rRight)
	{
		return rLeft.GetFileSize() == rRight.GetFileSize() &&
			rLeft.GetFileHash() == rRight.GetFileHash() && 
			rLeft.GetFilePfsxAttributes() == rRight.GetFilePfsxAttributes() &&
			rLeft.GetMethod() == rRight.GetMethod();
	}

public:
	
	// 
	// summary:
	//		对Server端的MetaInfo信息和用户本机版本的MetaInfo进行差异化判断
	//		用于确定需要更新的列表内容
	//
	// parameters:
	//		[in] rMinuend - 含全部信息的MetaInfo信息，Server端或Server与Local的差，用作被减数
	//		[in] rSubtrahend - 用户本机的MetaInfo信息，Local或Cache中的信息，用作减数
	//		[out] rDifference - 计算结果，包含要更新的MetaInfo信息
	//
	static void SubtractMetaInfoMap( 
		const PFS::CMetaInfoMap& rMinuend, 
		const PFS::CMetaInfoMap& rSubtrahend,
		PFS::CMetaInfoMap&	rDifference )
	{
		PFS::CMetaInfoMap::const_iterator itrS = rMinuend.begin();
		PFS::CMetaInfoMap::const_iterator itrL = rSubtrahend.begin();
		PFS::CMetaInfoMap::key_compare comparator;
		for ( ; itrS != rMinuend.end() &&  itrL != rSubtrahend.end();  ++ itrL )
		{
			const std::wstring& rName2 = itrL->first;

			for( ; itrS != rMinuend.end() && comparator( itrS->first, rName2 ); ++ itrS )
			{
				rDifference.insert( *itrS );
			}

			if ( itrS == rMinuend.end() )
			{
				break;
			}

			// 如果名字相同，判断MetaInfo是否一样，将不一样的加到更新列表中
			if (  itrS->first.compare( rName2 ) == 0 ) 
			{
				if ( !IsEqualMetaInfo( itrS->second, itrL->second) )
				{
					rDifference.insert( *itrS );
				}

				++ itrS;
			}
		}

		for( ; itrS != rMinuend.end() ; ++ itrS )
		{
			rDifference.insert( *itrS );
		}

		rDifference.erase( L"/." );
	}

	// 
	// summary:
	//		对上一版本的MetaInfo信息和当前版本的MetaInfo进行比较
	//		用于确定当前版本需要删除的列表
	//
	// parameters:
	//		[in] rLast - 上一版本的MetaInfo信息
	//		[in] rCur  - 当前版本的MetaInfo信息
	//		[out] rDelete - 计算结果，包含要删除的MetaInfo信息
	//
	static void SubtractMetaInfoMapEx( const PFS::CMetaInfoMap& rLast, 
		const PFS::CMetaInfoMap& rCur,
		PFS::CMetaInfoMap&	rDelete )
	{
		PFS::CMetaInfoMap::const_iterator iLast = rLast.begin();
		PFS::CMetaInfoMap::const_iterator iCur = rCur.begin();
		PFS::CMetaInfoMap::const_iterator lastEnd = rLast.end();
		PFS::CMetaInfoMap::const_iterator curEnd = rCur.end();
		PFS::CMetaInfoMap::key_compare comparator;

		for ( ; iLast != lastEnd &&  iCur != curEnd; )
		{
			const std::wstring& rName1 = iLast->first;
			const std::wstring& rName2 = iCur->first;

			if( comparator( rName1, rName2 ))
			{
				rDelete.insert( *iLast );
				++iLast;
			}
			else if( comparator( rName2, rName1))
			{
				++iCur;
			}
			else	// rName1 == rName2
			{
				++iLast;
				++iCur;
			}
		}

		for( ; iLast != lastEnd ; ++ iLast)
		{
			rDelete.insert( *iLast );
		}
	}
};