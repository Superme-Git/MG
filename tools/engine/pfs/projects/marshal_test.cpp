#include <Windows.h>
#include "../../pfs/Marshal.h"
#include <iostream>
using namespace std;


class POST : public PFS::MARSHAL::IMarshalable
{
public:
	int x;
	int y;

	virtual PFS::MARSHAL::CMemoryStream& marshal(PFS::MARSHAL::CMemoryStream &os) const
	{
		os<<x;
		os<<y;
		return os;
	}

	virtual PFS::MARSHAL::CMemoryStream& unmarshal(PFS::MARSHAL::CMemoryStream &os)
	{
		os>>x;
		os>>y;
		return os;
	}
};

void TestMarshal()
{

	PFS::MARSHAL::CMemoryStream stm;

	int x = 0xfff;
	int y = 0;
	float f1 = 1.01f;
	float f2 = 0.0;
	stm<<x;
	stm << f1;
	
	stm>>y;
	stm >> f2;

	if ( x == y )
	{
		cout<<"ok!";
	}
	else
	{
		cout<<"oss!";
	}

	POST pos, pos2;
	pos.x = 100;
	pos.y = 200;
	stm<<pos;
	stm>>pos2;
	if ( pos2.x == pos.x )
	{
		cout<<"ok!";
	}
	else
	{
		cout<<"oss!";
	}


}