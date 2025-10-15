#include "nulog.h"
#include "nuljfmutil.h"

namespace Nuclear
{
	static const int READ_BLOCK_SIZE = 8192;

	bool FRead(LJFM::LJFMBF *cf, void *buf, size_t size)
	{
		size_t remainsize = size;
		char *p = (char*)buf;
		while( remainsize )
		{
			size_t readsize = remainsize > READ_BLOCK_SIZE ? READ_BLOCK_SIZE : remainsize;
			size_t readsizeret = cf->Read(p, readsize);
			if( readsizeret == LJFM::FILE_SIZE_ERROR ) return false;
			p += readsizeret;
			remainsize -= readsizeret;
		}
		return true;
	}

	bool FWrite(LJFM::LJFMBF *cf, const void *buf, size_t size)
	{
		size_t remainsize = size;
		char *p = (char*)buf;
		while( remainsize )
		{
			size_t readsize = remainsize > READ_BLOCK_SIZE ? READ_BLOCK_SIZE : remainsize;
			size_t readsizeret = cf->Write(p, readsize);
			if( readsizeret == LJFM::FILE_SIZE_ERROR ) return false;
			p += readsizeret;
			remainsize -= readsizeret;
		}
		return true;
	}

	void LJFMLog::VPrint( const wchar_t* info, va_list vl)
	{
        wprintf(info, vl);
#if (defined WIN32) && _DEBUG
		wchar_t ss[1024];
		_vsnwprintf_s(ss, 1024, 1024, info, vl);
		OutputDebugStringW(ss);
#endif
	}

	NuclearOctets& NuclearFileOctets::insert(void *pos, const void *x, size_t len)
	{// 不可以 在begin()和end()之间
		XPLOG_ERROR(L"NuclearFileOctets::insert() %d\n", (int)len); 
		return *this;
	}
	NuclearOctets& NuclearFileOctets::insert(void *pos, const void *x, const void *y)
	{
		XPLOG_ERROR(L"NuclearFileOctets::insert()\n"); 
		return *this;
	}
	NuclearOctets& NuclearFileOctets::resize(size_t size)
	{
		XPLOG_ERROR(L"NuclearFileOctets::resize() %d\n", (int)size); 
		return *this;
	}

	NuclearOctets& NuclearFileOctets::reserve(size_t size)
	{ 
		XPLOG_ERROR(L"NuclearFileOctets::reserve() %d\n", (int)size); 
		return *this; 
	}

	NuclearOctets& NuclearFileOctets::replace(const void *data, size_t size)
	{
		XPLOG_ERROR(L"NuclearFileOctets::replace() %d\n", (int)size); 
		return *this; 
	}

	void *NuclearFileOctets::begin()
	{
		XPLOG_ERROR(L"NuclearFileOctets::begin()\n"); 
		return NULL;
	}

	void *NuclearFileOctets::end()
	{
		XPLOG_ERROR(L"NuclearFileOctets::end()\n"); 
		return NULL;
	}

	NuclearOctets& NuclearFileOctets::clear()
	{
		XPLOG_ERROR(L"NuclearFileOctets::clear()\n"); 
		return *this;
	}

	NuclearOctets& NuclearFileOctets::erase(void *x, void *y)
	{
		XPLOG_ERROR(L"NuclearFileOctets::erase()\n"); 
		return *this;
	}

}