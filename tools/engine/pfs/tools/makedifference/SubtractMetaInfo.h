#pragma once

class CSubtractMetaInfo
{
	CSubtractMetaInfo() {}
	~CSubtractMetaInfo() {}
	
	//
	// summary:
	//		�Ƚ�����MetaInfo�Ƿ�һ��
	//		����ֻ�Ƚ��ļ���С��Hashֵ
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
	//		��Server�˵�MetaInfo��Ϣ���û������汾��MetaInfo���в��컯�ж�
	//		����ȷ����Ҫ���µ��б�����
	//
	// parameters:
	//		[in] rMinuend - ��ȫ����Ϣ��MetaInfo��Ϣ��Server�˻�Server��Local�Ĳ����������
	//		[in] rSubtrahend - �û�������MetaInfo��Ϣ��Local��Cache�е���Ϣ����������
	//		[out] rDifference - ������������Ҫ���µ�MetaInfo��Ϣ
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

			// ���������ͬ���ж�MetaInfo�Ƿ�һ��������һ���ļӵ������б���
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
	//		����һ�汾��MetaInfo��Ϣ�͵�ǰ�汾��MetaInfo���бȽ�
	//		����ȷ����ǰ�汾��Ҫɾ�����б�
	//
	// parameters:
	//		[in] rLast - ��һ�汾��MetaInfo��Ϣ
	//		[in] rCur  - ��ǰ�汾��MetaInfo��Ϣ
	//		[out] rDelete - ������������Ҫɾ����MetaInfo��Ϣ
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