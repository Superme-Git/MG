// DNSQuery.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <conio.h>
#include <windows.h>
#include <windns.h>
#include <vector>

#pragma comment( lib, "Dnsapi.lib")

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

LPWSTR FormatErrorMessage( DWORD dwErrorCode, ... )
{
	va_list vl;
	va_start( vl, dwErrorCode );
	LPWSTR lpBuffer = NULL;
	::FormatMessageW( FORMAT_MESSAGE_FROM_SYSTEM | FORMAT_MESSAGE_ALLOCATE_BUFFER, NULL,
		dwErrorCode, 0, (LPWSTR)&lpBuffer, 0, &vl );	
	va_end( vl );

	return lpBuffer;
}

void FreeMessageBuffer( LPWSTR lpMessage )
{
	LocalFree( lpMessage );
}

int LogW( const wchar_t * _Format, ...  )
{
	va_list valist;
	va_start(valist, _Format);	
	return vwprintf( _Format, valist );
}


int DNSQuery4( LPCTSTR szHostName, std::vector<DWORD>& rIpV4Records )
{

	PDNS_RECORD pQueryResultsSet = NULL;
	PCTSTR lpstrName = NULL;

	WORD wType = DNS_TYPE_A;
	DWORD Options =  
		//DNS_QUERY_USE_TCP_ONLY  |
	//DNS_QUERY_NO_HOSTS_FILE | 
	//DNS_QUERY_WIRE_ONLY | 
	//DNS_QUERY_NO_RECURSION |
	//DNS_QUERY_RETURN_MESSAGE |
	//DNS_QUERY_BYPASS_CACHE|
	//DNS_QUERY_DONT_RESET_TTL_VALUES|
	0;

	DNS_STATUS status = DnsQuery_W(
		szHostName,
		wType,
		Options,
		NULL,
		&pQueryResultsSet,
		NULL);
	if ( status != 0 )
	{
		LPWSTR lpszErrorMessage = FormatErrorMessage(status);
		if( lpszErrorMessage != NULL )
		{
			LogW( L"Failed to query the ip addresses for %s (%d):\n%s", szHostName, status, lpszErrorMessage);
			FreeMessageBuffer( lpszErrorMessage );
		}
		else
		{
			LogW( L"Failed to query the ip addresses for %s (%d).\n", szHostName, status);
		}

		return status;
	}

	for( ; pQueryResultsSet != NULL ; 
		pQueryResultsSet = pQueryResultsSet->pNext )
	{
		LogW( L"%-40s %-3d\t", pQueryResultsSet->pName, pQueryResultsSet->wType );

		switch( pQueryResultsSet->wType )
		{
		case DNS_TYPE_A:
			{
				if ( pQueryResultsSet->Flags.S.Section  == DnsSectionAddtional )
					break;
				ipv4helper* ipv4 = (ipv4helper*)&pQueryResultsSet->Data.A.IpAddress;
				LogW( L"%d.%d.%d.%d", ipv4->b1,ipv4->b2,ipv4->b3,ipv4->b4 );
				rIpV4Records.push_back( pQueryResultsSet->Data.A.IpAddress );
			}
			break;
		case DNS_TYPE_CNAME:
			LogW( L"%s - alias", pQueryResultsSet->Data.PTR.pNameHost );
			break;
		case DNS_TYPE_NS:
			LogW( L"%s - authoritative name server", pQueryResultsSet->Data.PTR.pNameHost );
			break;
			break;
		case DNS_TYPE_MD:
		case DNS_TYPE_MF:
		case DNS_TYPE_MG:
		case DNS_TYPE_MR:
			LogW( L"%s", pQueryResultsSet->Data.PTR.pNameHost );
			break;
		case DNS_TYPE_SOA:
			LogW( L"Server:%s Administrator:%s\n%-40s\tTTL:%d Expire:%d Refresh:%d\n%-40s\tRetry:%d SerialNo:%d", 
				pQueryResultsSet->Data.SOA.pNamePrimaryServer, pQueryResultsSet->Data.SOA.pNameAdministrator, 
				L"\t",
				pQueryResultsSet->Data.SOA.dwDefaultTtl,
				pQueryResultsSet->Data.SOA.dwExpire, pQueryResultsSet->Data.SOA.dwRefresh, 
				L"\t",
				pQueryResultsSet->Data.SOA.dwRetry, pQueryResultsSet->Data.SOA.dwSerialNo );
			break;			
		case DNS_TYPE_MX:
			LogW( L"%s, preference:%d", pQueryResultsSet->Data.MX.pNameExchange, pQueryResultsSet->Data.MX.wPreference );
			break;
		case DNS_TYPE_AAAA:
			{
				WORD* IP6Word = pQueryResultsSet->Data.AAAA.Ip6Address.IP6Word;
				LogW( L"IPv6 Address: %x:%x:%x:%x:%x:%x:%x:%x", 
					swapbyteorder(IP6Word[0]),
					swapbyteorder(IP6Word[1]),
					swapbyteorder(IP6Word[2]), 
					swapbyteorder(IP6Word[3]),
					swapbyteorder(IP6Word[4]),
					swapbyteorder(IP6Word[5]),
					swapbyteorder(IP6Word[6]),
					swapbyteorder(IP6Word[7]));
			}
			break;
		default:
			LogW( L"unknown type ");
			break;
		}

		switch ( pQueryResultsSet->Flags.S.Section )
		{
		case DnsSectionQuestion:
			LogW( L" - Question   ");
			break;
		case DnsSectionAnswer:
			LogW( L" - Answer     ");
			break;
		case DnsSectionAuthority:
			LogW( L" - Authority  ");
			break;
		case DnsSectionAddtional:
			LogW( L" - Additional ");
			break;
		}

		LogW( L"\n");
	}

	DnsRecordListFree( pQueryResultsSet, DnsFreeRecordList );
	
	return 0;
}

int DoQuery(int argc, _TCHAR* argv[], WORD wType)
{

	PDNS_RECORD pQueryResultsSet = NULL;
	PCTSTR lpstrName = NULL;

	for( int i = 1; i < argc; i ++ )
	{
		LogW( L"%s: -----------------------------------------------------\n", argv[i] );

		DWORD Options =  
			//DNS_QUERY_USE_TCP_ONLY  |
			//DNS_QUERY_NO_HOSTS_FILE | 
			//DNS_QUERY_WIRE_ONLY | 
			//DNS_QUERY_NO_RECURSION |
			//DNS_QUERY_RETURN_MESSAGE |
			DNS_QUERY_BYPASS_CACHE|
			//DNS_QUERY_DONT_RESET_TTL_VALUES|
			0;

		DNS_STATUS status = DnsQuery_W(
		  argv[i],
		  wType,
		  Options,
		  NULL,
		  &pQueryResultsSet,
		  NULL);
		if ( status != 0 )
		{
			LPWSTR lpszErrorMessage = FormatErrorMessage(status);
			if( lpszErrorMessage != NULL )
			{
				LogW( L"Failed to query the ip addresses for %s (%d):\n%s", argv[i], status, lpszErrorMessage);
				FreeMessageBuffer( lpszErrorMessage );
			}
			else
			{
				LogW( L"Failed to query the ip addresses for %s (%d).\n", argv[i], status);
			}

			continue;
		}

		for( ; pQueryResultsSet != NULL ; 
			pQueryResultsSet = pQueryResultsSet->pNext )
		{
			LogW( L"%-40s %-3d\t", pQueryResultsSet->pName, pQueryResultsSet->wType );

			switch( pQueryResultsSet->wType )
			{
			case DNS_TYPE_A:
				{
					ipv4helper* ipv4 = (ipv4helper*)&pQueryResultsSet->Data.A.IpAddress;
					LogW( L"%d.%d.%d.%d", ipv4->b1,ipv4->b2,ipv4->b3,ipv4->b4 );
				}
				break;
			case DNS_TYPE_CNAME:
				LogW( L"%s - alias", pQueryResultsSet->Data.PTR.pNameHost );
				break;
			case DNS_TYPE_NS:
				LogW( L"%s - authoritative name server", pQueryResultsSet->Data.PTR.pNameHost );
				break;
				break;
			case DNS_TYPE_MD:
			case DNS_TYPE_MF:
			case DNS_TYPE_MG:
			case DNS_TYPE_MR:
				LogW( L"%s", pQueryResultsSet->Data.PTR.pNameHost );
				break;
			case DNS_TYPE_SOA:
				LogW( L"Server:%s Administrator:%s\n%-40s\tTTL:%d Expire:%d Refresh:%d\n%-40s\tRetry:%d SerialNo:%d", 
					pQueryResultsSet->Data.SOA.pNamePrimaryServer, pQueryResultsSet->Data.SOA.pNameAdministrator, 
					L"\t",
					pQueryResultsSet->Data.SOA.dwDefaultTtl,
					pQueryResultsSet->Data.SOA.dwExpire, pQueryResultsSet->Data.SOA.dwRefresh, 
					L"\t",
					pQueryResultsSet->Data.SOA.dwRetry, pQueryResultsSet->Data.SOA.dwSerialNo );
				break;			
			case DNS_TYPE_MX:
				LogW( L"%s, preference:%d", pQueryResultsSet->Data.MX.pNameExchange, pQueryResultsSet->Data.MX.wPreference );
				break;
			case DNS_TYPE_AAAA:
				{
					WORD* IP6Word = pQueryResultsSet->Data.AAAA.Ip6Address.IP6Word;
					LogW( L"IPv6 Address: %x:%x:%x:%x:%x:%x:%x:%x", 
						swapbyteorder(IP6Word[0]),
						swapbyteorder(IP6Word[1]),
						swapbyteorder(IP6Word[2]), 
						swapbyteorder(IP6Word[3]),
						swapbyteorder(IP6Word[4]),
						swapbyteorder(IP6Word[5]),
						swapbyteorder(IP6Word[6]),
						swapbyteorder(IP6Word[7]));
				}
				break;
			default:
				LogW( L"unknown type ");
				break;
			}
						
			switch ( pQueryResultsSet->Flags.S.Section )
			{
			case DnsSectionQuestion:
				LogW( L" - Question   ");
				break;
			case DnsSectionAnswer:
				LogW( L" - Answer     ");
				break;
			case DnsSectionAuthority:
				LogW( L" - Authority  ");
				break;
			case DnsSectionAddtional:
				LogW( L" - Additional ");
				break;
			}

			LogW( L"\n");
		}

		DnsRecordListFree( pQueryResultsSet, DnsFreeRecordList );

	}

	return 0;
}

int _tmain(int argc, _TCHAR* argv[])
{
	if( argc <= 1 )
	{
		LogW( L"Usage: pass a domain name as the argument\n" );
		LogW( L"Press any key to quit...." );
		_getch();
		return 0;
	}

	DoQuery( argc, argv,  DNS_TYPE_A ); // input host name
	//DoQuery( argc, argv,  DNS_TYPE_PTR ); // input ip
	
	//DoQuery( argc, argv,  DNS_TYPE_CNAME );
	//DoQuery( argc, argv,  DNS_TYPE_NS );
	//DoQuery( argc, argv,  DNS_TYPE_ALL );

	return 0;
}

