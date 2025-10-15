#ifndef __FIRENET_SOCKETFUNC_H__
#define __FIRENET_SOCKETFUNC_H__
#pragma once

#if (defined WIN7_32)
#include <winsock.h>
#endif

namespace FireNet
{

	typedef int socklen_t;

	inline int readsocket( int so, char* buffer, int bsize) { return recv( so, buffer, bsize, 0); }
	inline int readsocket( int so, void* buffer, int bsize) { return readsocket( so, (char*)buffer, bsize); }

	inline int writesocket( int so, const char* buffer, int bsize) { return send( so, buffer, bsize, 0); }
	inline int writesocket( int so, const void* buffer, int bsize) { return writesocket( so, (const char*)buffer, bsize); }

	inline int getsockopt( int so, int level, int optName, int* data, socklen_t* ds) { return ::getsockopt( so, level, optName, (char*)data, ds); }
	inline int setsockopt( int so, int level, int optName, const int* data, socklen_t ds) { return ::setsockopt( so, level, optName, (const char*)data, ds); }

} // namespace FireNet {

#endif