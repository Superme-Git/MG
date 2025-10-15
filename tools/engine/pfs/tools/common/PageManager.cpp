#include "PageManager.h"
#include <cassert>
namespace COMMON
{
bool _TestPageManager()
{
	CPageManager page;
	
	for( unsigned int c = 0; c < 100; c ++ ) 
	{
		page.SetItemCount(c);
		assert( page.GetItemCount() == c ) ;
	}

	for( unsigned int c = 0; c < 100; c ++ ) 
	{
		page.SetPageSize(c+1);
		assert( page.GetPageSize() == c+1 ) ;
	}

	for( unsigned int c = 0; c < 100; c ++ ) 
	{
		page.SetPageSize(c/2+1);
		page.SetItemCount(c);
		assert( page.GetItemCount() == c ) ;
	}

	for( unsigned int c = 0; c < 100; c ++ ) 
	{
		page.SetPageSize(c*2+1);
		page.SetItemCount(c);
		assert( page.GetItemCount() == c ) ;
	}

	for( unsigned int c = 0; c < 100; c ++ ) 
	{
		page.SetPageSize(c+1);
		page.SetItemCount(c*2);
		assert( page.GetItemCount() == c*2 ) ;
	}

	for( unsigned int c = 0; c < 100; c ++ ) 
	{
		page.SetPageSize(c+1);
		page.SetItemCount(c/2);
		assert( page.GetItemCount() == c/2 ) ;
	}

	for( unsigned int c = 0; c < 100; c ++ ) 
	{
		page.SetPageSize(c*2+1);
		page.SetItemCount(c/2);
		assert( page.GetItemCount() == c/2 ) ;
	}

	for( unsigned int c = 0; c < 100; c ++ ) 
	{
		page.SetPageSize(c/2+1);
		page.SetItemCount(c*2);
		assert( page.GetItemCount() == c*2 ) ;
	}


	page.SetItemCount(99);
	for( unsigned int c = 0; c < 200; c ++ ) 
	{
		page.SetPageSize(c+1);
		assert( page.GetPageCount() == (99+c)/(c+1) ) ;
	}

	page.SetItemCount(99);
	page.SetPageSize(10);
	for( unsigned int c = 0; c < 20; c ++ ) 
	{
		if( page.SetPageIndex(c) )
		{
			assert ( c < page.GetPageCount() );
			assert( page.GetPageIndex() == c ) ;
		}
		else
		{
			assert ( c >= page.GetPageCount() );
		}
	}

	page.SetItemCount(99);
	page.SetPageSize(10);
	page.GotoFirstPage();
	for( unsigned int c = 0; c < 20; c ++ ) 
	{
		if( page.PageDown() )
		{
			assert( page.GetPageIndex() == c+1 ) ;
		}
		else
		{
			assert ( c+1 >= page.GetPageCount() );
		}
	}

	page.GotoLastPage();
	assert( page.GetPageIndex() == 9 ) ;
	for( unsigned int c = 9; c >= 0; c -- ) 
	{
		if( page.PageUp() )
		{
			assert( page.GetPageIndex() == c-1 ) ;
		}
		else
		{
			assert ( (int)c <= 0 );
		}
	}

	return true;
}

} // namespace COMMON