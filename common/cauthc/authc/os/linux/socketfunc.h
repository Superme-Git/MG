#ifndef __FIRENET_LINUX_SOCKETFUNC_H__
#define __FIRENET_LINUX_SOCKETFUNC_H__
#pragma once

namespace FireNet
{

	inline void closesocket( int so)
	{
		::close( so);
	}

	inline int readsocket( int so, void* buffer, size_t bsize)
	{
		return ::read( so, buffer, bsize);
	}

	inline int writesocket( int so, const void* buffer, size_t bsize)
	{
		return ::write( so, buffer, bsize);
	}

} // namespace NET {

#endif