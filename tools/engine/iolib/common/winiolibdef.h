#ifndef MSC_DEFINE_FOR_IOLIB_NAUGHT___
#define MSC_DEFINE_FOR_IOLIB_NAUGHT___

#define _WIN32_WINNT	0x0400

#define _USE_REAL_LOCKER
#define _REAL_MULTI_THREAD

#pragma warning( disable : 4355)
#pragma warning( disable : 4146)
#pragma warning( disable : 4800)
#pragma warning( disable : 4146)

#include <xutility>
#include <map>

//#ifndef FD_SETSIZE
//#define FD_SETSIZE		64
//#endif 
#include <winsock2.h>

typedef int ptrdiff_t;


#ifndef INT64_DEFINED
#define INT64_DEFINED
typedef __int64 int64_t;
typedef unsigned __int64 uint64_t;
#endif

#define strcasecmp	_stricmp


namespace GNET { 

void OutErrorLogFdset( fd_set& a, fd_set& b, fd_set& c); 

int socket( int af, int type, int protocol);
int accept( int s, struct sockaddr FAR *addr, int FAR *addrlen);
int dup( int fd);

inline int setsockopt( int fd, int level, int optname, int * optval, int optlen)
{
	return ::setsockopt( fd, level, optname, (const char*)optval, optlen);
}

inline int getsockopt( int fd, int level, int optname, int* optval, int * optlen)
{
	return ::getsockopt( fd, SOL_SOCKET, optname, (char*)optval, optlen);
}

inline int read( int fd, void* data, size_t len)
{
	return recv( fd, (char*)data, (int)len, 0);
}

inline int write( int fd, void* data, size_t len)
{
	return send( fd, (char*)data, (int)len, 0);
}

void close( int fd);
int shutdown( int s, int how);
bool IsWinSocketShutdown( int fd);

} //namespace GNET { 

#include "octets.h"

#endif  //#ifndef MSC_DEFINE_FOR_IOLIB_NAUGHT___
