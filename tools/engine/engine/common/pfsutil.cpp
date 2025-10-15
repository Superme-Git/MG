#include "stdafx.h"
#include "log.h"
#include "pfsutil.h"

namespace Nuclear
{
	static const int READ_BLOCK_SIZE = 8192;

	bool FRead(PFS::CBaseFile *cf, void *buf, size_t size)
	{
		size_t remainsize = size;
		char *p = (char*)buf;
		while( remainsize )
		{
			size_t readsize = remainsize > READ_BLOCK_SIZE ? READ_BLOCK_SIZE : remainsize;
			size_t readsizeret = cf->Read(p, readsize);
			if( readsizeret == PFS::FILE_SIZE_ERROR ) return false;
			p += readsizeret;
			remainsize -= readsizeret;
		}
		return true;
	}

	bool FWrite(PFS::CBaseFile *cf, const void *buf, size_t size)
	{
		size_t remainsize = size;
		char *p = (char*)buf;
		while( remainsize )
		{
			size_t readsize = remainsize > READ_BLOCK_SIZE ? READ_BLOCK_SIZE : remainsize;
			size_t readsizeret = cf->Write(p, readsize);
			if( readsizeret == PFS::FILE_SIZE_ERROR ) return false;
			p += readsizeret;
			remainsize -= readsizeret;
		}
		return true;
	}

	void PFSLog::VPrint( const wchar_t* info, va_list vl)
	{
		wchar_t ss[1024];
		_vsnwprintf_s(ss, 1024, 1024, info, vl);
		OutputDebugStringW(ss);
	}

	XOctets& XFileOctets::reserve(size_t size)
	{ 
		XPLOG_ERROR(L"XFileOctets::reserve() %d\n", (int)size); 
		return *this; 
	}

	XOctets& XFileOctets::replace(const void *data, size_t size)
	{
		XPLOG_ERROR(L"XFileOctets::replace() %d\n", (int)size); 
		return *this; 
	}

	void *XFileOctets::begin()
	{
		XPLOG_ERROR(L"XFileOctets::begin()\n"); 
		return NULL;
	}

	void *XFileOctets::end()
	{
		XPLOG_ERROR(L"XFileOctets::end()\n"); 
		return NULL;
	}

	XOctets& XFileOctets::clear()
	{
		XPLOG_ERROR(L"XFileOctets::clear()\n"); 
		return *this;
	}

	XOctets& XFileOctets::erase(void *x, void *y)
	{
		XPLOG_ERROR(L"XFileOctets::erase()\n"); 
		return *this;
	}

	// x 不可以 在begin()和end()之间
	XOctets& XFileOctets::insert(void *pos, const void *x, size_t len)
	{
		XPLOG_ERROR(L"XFileOctets::insert() %d\n", (int)len); 
		return *this;
	}
	XOctets& XFileOctets::insert(void *pos, const void *x, const void *y)
	{
		XPLOG_ERROR(L"XFileOctets::insert()\n"); 
		return *this;
	}
	XOctets& XFileOctets::resize(size_t size)
	{
		XPLOG_ERROR(L"XFileOctets::resize() %d\n", (int)size); 
		return *this;
	}

}