#include <stdio.h>

#include <string>
#include <list>
#include <vector>
#include <map>
#include <set>

#include "../datapool.h"
#include "notifyobjects.h"
#include "objectinstance.hpp"

#include <conio.h>
#include <assert.h>

class CMyNotify0 : public DATAPOOL::CDataModifyNotify
{
public:
	CMyNotify0() {}
	virtual ~CMyNotify0() {}
protected:
	virtual void OnDataModify( DATAPOOL::NOTIYF_DATA_ID ndid, const DATAPOOL::CNotifyableData& data)
	{
		switch( ndid)
		{
		case DATAPOOL::NDID_RoleInfo:
			assert( dynamic_cast<const DATAPOOL::const_RoleInfo*>( &data)); 
			assert( dynamic_cast<const DATAPOOL::RoleInfo*>( &data)); 
			break;
		}
		wprintf( L"%d has changed\n", ndid);
	}
};

int main( int argc, char* argv[])
{
	CMyNotify0		mn;

	DATAPOOL::DataPoolInitialize();

	DATAPOOL::CNotifyGroupManager::GetInstance().RegisterNotify( L"nextgroup", mn);

	DATAPOOL::RoleInfo& ri = DATAPOOL::CObjManager::GetInstance().get_RoleInfo();
	ri.set_name( L"newname");


	_getch();
	return 0;
}