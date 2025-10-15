#include "winiolibdef.h"
#include <sstream>
#include "mutex.h"

//#define OUTSOCKETLOG

namespace GNET { 

inline void MakeOStringStream( std::ostringstream& oss, const char* szName, fd_set& s)
{
	oss << "\t" << szName << " count = " << s.fd_count << "\r\n\t\t";
	for( u_int i = 0; i < s.fd_count; i ++)
		oss << s.fd_array[i] << " ";
	oss << "\r\n\r\n";
}

void OutErrorLogFdset( fd_set& a, fd_set& b, fd_set& c)
{
#ifdef OUTSOCKETLOG
	std::ostringstream oss;
	DWORD		dwWrited;

	oss << "#####error select error = " <<  WSAGetLastError() << "\r\n";

	MakeOStringStream( oss, "read", a);
	MakeOStringStream( oss, "write", b);
	MakeOStringStream( oss, "error", c);
	

	HANDLE		hFile = GetStdHandle( STD_OUTPUT_HANDLE);

	const std::string& strInfo = oss.str();
	WriteFile( hFile, strInfo.c_str(), strInfo.length(), &dwWrited, NULL);
	FlushFileBuffers( hFile);
#endif //#ifdef OUTSOCKETLOG
}

inline void OutStdOutLog( const char* szName, int s, bool bError = false)
{
#ifdef OUTSOCKETLOG
	DWORD		dwWrited;
	std::ostringstream oss;

	if( bError)
		oss << "#####error " << szName << " socket = " << s << " error = " << WSAGetLastError() << "\r\n";
	else
		oss << "##### " << szName << " socket = " << s << "\r\n";

	HANDLE		hFile = GetStdHandle( STD_OUTPUT_HANDLE);

	const std::string& strInfo = oss.str();
	WriteFile( hFile, strInfo.c_str(), strInfo.length(), &dwWrited, NULL);
	if( bError)
		FlushFileBuffers( hFile);
#endif //#ifdef OUTSOCKETLOG
}


struct WinSockParams
{
	WinSockParams()
		: ref( 1), shutdown( false)
	{
	}
	int ref;
	bool shutdown;
};

typedef std::map<int,WinSockParams> WinSockParamsMap;

WinSockParamsMap	g_map;
Thread::Mutex		g_mtx;


int socket( int af, int type, int protocol)
{
	int s = (int)::socket( af, type, protocol);
	
	Thread::Mutex::Scoped		scoped( g_mtx);
	if( -1 != s)
		g_map[s];
	OutStdOutLog( "socket", s);
	return s;
}


int accept( int s, struct sockaddr FAR *addr, int FAR *addrlen)
{
	int fd = (int)::accept( s, addr, addrlen);

	Thread::Mutex::Scoped		scoped( g_mtx);
	if( -1 != fd)
		g_map[fd];
	OutStdOutLog( "accept", fd);
	return fd;
}

/*
inline int dup( int fd)
{
	WSAPROTOCOL_INFO	Info;
	WSADuplicateSocket( fd, GetCurrentProcessId(), &Info);
	return (int)WSASocket( 0, 0, 0, &Info, 0, FROM_PROTOCOL_INFO);
}*/

int dup( int fd)
{
	Thread::Mutex::Scoped		scoped( g_mtx);

	WinSockParamsMap::iterator it = g_map.find( fd);
	if( it == g_map.end())
	{
		OutStdOutLog( "dup", fd, true);
		return -1;
	}
	OutStdOutLog( "dup", fd);
	WinSockParams& wsp = it->second;
	wsp.ref ++;
	return fd;
}

void close( int fd)
{
	Thread::Mutex::Scoped		scoped( g_mtx);

	WinSockParamsMap::iterator it = g_map.find( fd);
	if( it == g_map.end())
	{
		OutStdOutLog( "close", fd, true);
		closesocket( fd);
		return;
	}
	OutStdOutLog( "close", fd);

	WinSockParams& wsp = it->second;
	wsp.ref --;
	if( 0 == wsp.ref)
	{
		g_map.erase( it);
		closesocket( fd);
	}
}

int shutdown( int s, int how)
{
	Thread::Mutex::Scoped		scoped( g_mtx);

	WinSockParamsMap::iterator it = g_map.find( s);
	if( it == g_map.end())
	{
		OutStdOutLog( "shutdown", s, true);
		return -1;
	}
	OutStdOutLog( "shutdown", s);
	WinSockParams& wsp = it->second;
	wsp.shutdown = true;
	return 0;
}

bool IsWinSocketShutdown( int fd)
{
	Thread::Mutex::Scoped		scoped( g_mtx);

	WinSockParamsMap::iterator it = g_map.find( fd);
	if( it == g_map.end())
	{
		OutStdOutLog( "IsWinSocketShutdown", fd, true);
		return false;
	}
	WinSockParams& wsp = it->second;
	return wsp.shutdown;
}

} //namespace GNET { 
