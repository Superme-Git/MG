
#include <stdio.h>
#include <arpa/inet.h>
#include "db.h"

using namespace GNET;

class Query : public IQueryKey
{
public:
	 virtual bool update( const void *key, size_t key_len )
	 {
	 	printf("%d\n", htonl(*(int *)key));
		return true;
	 }
};

class QueryCursor : public IQueryKey
{
public:
	 QueryCursor()
	 {
	 	next = -1;
	 }
	 int next;
	 virtual bool update( const void *key, size_t key_len )
	 {
	 	next = htonl(*(int *)key);
		return false;
	 }
};

int main()
{
	{
	DBStandalone* db = new DBStandalone("next.db");
	db->init();

	for( int i = 0; i < 10000; i+=2)
	{
		int n = htonl(i);
		db->put(&n, 4, &n, 4);
	}
	db->checkpoint();
	delete db;
	}

	{
	DBStandalone* db = new DBStandalone("next.db");
	db->init();

	Query q; db->walk(&q);
	
	size_t key_len;
	for( int i = 0; i < 10000; i++)
	{
		key_len = 4;
		int n = htonl(i);
		//printf("%p\n", db->next_key(&n, &key_len));
		void * next = db->greater_key(&n, &key_len);
		if (next)
			;//printf("next %d=%d\n", i, htonl(*(int *)next));
		else
			printf("next %d=null\n", i);
	}
	delete db;
	}

	{
	DBStandalone* db = new DBStandalone("next.db");
	db->init();

	size_t key_len;
	for( int i = 0; i < 10000; i++)
	{
		int n = htonl(i);
		QueryCursor q;
		db->walk(&n, 4, &q);
		if (q.next == -1) // 没有找到记录
			printf("next %d is null\n", i);
	}
	delete db;
	}
}

