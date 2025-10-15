//////////////////////////////////////////////////////////////////////////
//
// 页面管理器
//
// 说明: 页面管理器(PageManager)是一个通用的逻辑组件。
// 实现该组件的目的在于，程序逻辑中多处存在以页方式管理的数据模型，
// 比如列表控件。分页管理算法非常常见且模型统一。将其抽象出来统一
// 实现，可以精简维护量。
//
// 2010-01-18 刘东学
//////////////////////////////////////////////////////////////////////////
#pragma once

namespace COMMON
{
	//
	// summary : 通用的页面管理器
	//
	class CPageManager
	{
	public:

		//
		// summary : 构造函数（每页容纳1个项目，项目总数为0）
		//
		CPageManager(void)
			: m_ulPageSize( 1 )
			, m_ulItemCount( 0 )
			, m_ulPageIndex( 0 )
		{
		}

		//
		// summary : 构造函数，设置页面大小（项目总数为0）
		//
		CPageManager(unsigned long ulPageSize)
			: m_ulPageSize( ulPageSize )
			, m_ulItemCount( 0 )
			, m_ulPageIndex( 0 )
		{
		}

		//
		// summary : 析构函数
		//
		~CPageManager(void)
		{	
		}

		//
		// summary : 设置页面大小
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
		// summary : 获取页面大小
		//
		unsigned long GetPageSize( ) const
		{
			return m_ulPageSize;
		}

		//
		// summary : 设置项目总数
		//
		void SetItemCount( unsigned long ulItemCount )
		{
			m_ulItemCount = ulItemCount;
		}

		//
		// summary : 获取项目总数
		//
		unsigned long GetItemCount( ) const
		{
			return m_ulItemCount;
		}

		//
		// summary : 获取页面总数
		//
		unsigned long GetPageCount() const
		{
			return (m_ulItemCount + (m_ulPageSize-1))/m_ulPageSize;
		}

		//
		// summary : 获取当前页面索引
		//
		unsigned long GetPageIndex() const
		{
			return m_ulPageIndex;
		}

		//
		// summary : 获取页面是否可以上翻？
		//
		bool CanPageUp() const
		{
			return ( m_ulPageIndex > 0 );
		}

		//
		// summary : 获取页面是否可以下翻？
		//
		bool CanPageDown() const
		{
			return GetPageCount() > 1 && ( m_ulPageIndex < GetPageCount()-1 );
		}

		//
		// summary : 随机设置页面索引
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
		// summary : 上翻页面
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
		// summary : 下翻页面
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
		// summary : 页面翻至首页
		//
		void GotoFirstPage()
		{
			m_ulPageIndex = 0;
		}

		//
		// summary : 页面翻至尾页
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

