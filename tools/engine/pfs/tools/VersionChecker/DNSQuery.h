//////////////////////////////////////////////////////////////////////////
//
// DNSQuery.h
//
//////////////////////////////////////////////////////////////////////////
#pragma once
#include <vector>

union ipv4helper
{
	DWORD dwIp;
	struct 
	{
		unsigned char b1, b2, b3, b4;
	};
};

inline WORD swapbyteorder(WORD w)
{
	return (w&0xff00)>>8|(w&0xff)<<8;
}

//
// summary: query the IPv4 addresses for a host name
//
// returns: 0 when success, and non-zero error code if failed. 
//
int DNSQuery4( LPCTSTR szHostName, std::vector<DWORD>& rIpV4Records );

