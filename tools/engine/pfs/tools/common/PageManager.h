//////////////////////////////////////////////////////////////////////////
//
// ҳ�������
//
// ˵��: ҳ�������(PageManager)��һ��ͨ�õ��߼������
// ʵ�ָ������Ŀ�����ڣ������߼��жദ������ҳ��ʽ���������ģ�ͣ�
// �����б�ؼ�����ҳ�����㷨�ǳ�������ģ��ͳһ������������ͳһ
// ʵ�֣����Ծ���ά������
//
// 2010-01-18 ����ѧ
//////////////////////////////////////////////////////////////////////////
#pragma once

namespace COMMON
{
	//
	// summary : ͨ�õ�ҳ�������
	//
	class CPageManager
	{
	public:

		//
		// summary : ���캯����ÿҳ����1����Ŀ����Ŀ����Ϊ0��
		//
		CPageManager(void)
			: m_ulPageSize( 1 )
			, m_ulItemCount( 0 )
			, m_ulPageIndex( 0 )
		{
		}

		//
		// summary : ���캯��������ҳ���С����Ŀ����Ϊ0��
		//
		CPageManager(unsigned long ulPageSize)
			: m_ulPageSize( ulPageSize )
			, m_ulItemCount( 0 )
			, m_ulPageIndex( 0 )
		{
		}

		//
		// summary : ��������
		//
		~CPageManager(void)
		{	
		}

		//
		// summary : ����ҳ���С
		//
		void SetPageSize( unsigned long ulPageSize )
		{
			// exception occurred
#ifdef assert
			assert( m_ulPageSize > 0 );
#elif defined _ASSERT
			_ASSERT( m_ulPageSize > 0 );
#elif defined ASSERT
			ASSERT( m_ulPageSize > 0 );
#endif
			m_ulPageSize = ulPageSize;
		}

		//
		// summary : ��ȡҳ���С
		//
		unsigned long GetPageSize( ) const
		{
			return m_ulPageSize;
		}

		//
		// summary : ������Ŀ����
		//
		void SetItemCount( unsigned long ulItemCount )
		{
			m_ulItemCount = ulItemCount;
		}

		//
		// summary : ��ȡ��Ŀ����
		//
		unsigned long GetItemCount( ) const
		{
			return m_ulItemCount;
		}

		//
		// summary : ��ȡҳ������
		//
		unsigned long GetPageCount() const
		{
			return (m_ulItemCount + (m_ulPageSize-1))/m_ulPageSize;
		}

		//
		// summary : ��ȡ��ǰҳ������
		//
		unsigned long GetPageIndex() const
		{
			return m_ulPageIndex;
		}

		//
		// summary : ��ȡҳ���Ƿ�����Ϸ���
		//
		bool CanPageUp() const
		{
			return ( m_ulPageIndex > 0 );
		}

		//
		// summary : ��ȡҳ���Ƿ�����·���
		//
		bool CanPageDown() const
		{
			return GetPageCount() > 1 && ( m_ulPageIndex < GetPageCount()-1 );
		}

		//
		// summary : �������ҳ������
		//
		bool SetPageIndex( unsigned long ulPageIndex )
		{
			if( GetPageCount() > 0 )
			{
				if( ulPageIndex >= GetPageCount() ) 
					return false;
			}
			else if ( ulPageIndex != 0 )
			{
				return false;
			}

			m_ulPageIndex = ulPageIndex;
			return true;
		}

		//
		// summary : �Ϸ�ҳ��
		//
		bool PageUp()
		{
			if( CanPageUp() )
			{
				m_ulPageIndex -- ;
				return true;
			}
			return false;
		}

		//
		// summary : �·�ҳ��
		//
		bool PageDown()
		{
			if( CanPageDown() )
			{
				m_ulPageIndex ++;
				return true;
			}
			return false;
		}

		//
		// summary : ҳ�淭����ҳ
		//
		void GotoFirstPage()
		{
			m_ulPageIndex = 0;
		}

		//
		// summary : ҳ�淭��βҳ
		//
		void GotoLastPage()
		{
			if( GetPageCount() > 0 )
				m_ulPageIndex = GetPageCount() - 1;
			else
				m_ulPageIndex = 0;
		}

	protected:
		unsigned long m_ulPageSize; 
		unsigned long m_ulItemCount; 
		unsigned long m_ulPageIndex; 
	};

	// for unit-test
	bool _TestPageManager();

} // namespace COMMON

