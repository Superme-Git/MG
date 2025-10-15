#include "libshare.h"
#include "../../../platform/utils/StringUtil.h"

//add temporary log for testin. by liugeng
#if defined(ANDROID) && defined(LOGCAT)
#include <android/log.h>
#define  LOG_TAG    "mt3"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#else
#define LOGI
#define LOGE
#define LOGD
#endif

namespace FireNet {

	#define Min_OctetsStream_Size 0			//第0个字节
	#define Max_OctetsStream_Size 5*1024*1024//5M字节

	uint8_t Marshal::OctetsStream::pop_byte_8() const
	{
		uint8_t c;
		pop_byte(c);
		return c;
	}

	uint16_t Marshal::OctetsStream::pop_byte_16() const
	{
		uint16_t s;
		pop_byte(s);
		return FireNet::BYTE16Order(s);
	}

	uint32_t Marshal::OctetsStream::pop_byte_32() const
	{
		uint32_t l;
		pop_byte(l);
		return FireNet::BYTE32Order(l);
	}

	uint64_t Marshal::OctetsStream::pop_byte_64() const
	{
        typedef unsigned long long   uint64_t;
		uint64_t ll64_l, ll64_r;
        
        uint32_t ll_l;
		pop_byte(ll_l);
        ll64_l = ll_l;
        
        uint32_t ll_r;
        pop_byte(ll_r);
        ll64_r = ll_r;
        ll64_r <<= 32;
        
        ll64_l = ll64_l | ll64_r;
		return FireNet::BYTE64Order(ll64_l);
	}

	Marshal::OctetsStream& Marshal::OctetsStream::compact_uint32(size_t x)//change unsigned int to size_t to forbid convert from size_t to unsigned int
	{
		if(x>UINT_MAX)
			throw Marshal::Exception("size of x is bigger than UINT_MAX in Marshal::compact_uint32\n");
		if (x < 0x80)
			return push_byte((unsigned char)x);
		else if (x < 0x4000)
			return push_byte(FireNet::BYTE16Order(x|0x8000));
		else if (x < 0x20000000)
			return push_byte(FireNet::BYTE32Order(x | 0xc0000000));
		push_byte((unsigned char)0xe0);
		return push_byte(FireNet::BYTE32Order(x));
	}

	Marshal::OctetsStream& Marshal::OctetsStream::compact_sint32(int32_t x)
	{
		if (x >= 0)
		{
			if (x < 0x40)
				return push_byte((unsigned char)x);
			else if (x < 0x2000)
				return push_byte(FireNet::BYTE16Order(x | 0x8000));
			else if (x < 0x10000000)
				return push_byte(FireNet::BYTE32Order(x | 0xc0000000));
			push_byte((unsigned char)0xe0);
			return push_byte(FireNet::BYTE32Order(x));
		}
		if (-x > 0)
		{
			x = -x;
			if (x < 0x40)
				return push_byte((unsigned char)x|0x40);
			else if (x < 0x2000)
				return push_byte(FireNet::BYTE16Order(x | 0xa000));
			else if (x < 0x10000000)
				return push_byte(FireNet::BYTE32Order(x | 0xd0000000));
			push_byte((unsigned char)0xf0);
			return push_byte(FireNet::BYTE32Order(x));
		}
		push_byte((unsigned char)0xf0);
		return push_byte(FireNet::BYTE32Order(x));
	}

	const Marshal::OctetsStream& Marshal::OctetsStream::uncompact_uint32(const uint32_t &x) const
	{
		switch ( *((unsigned char *)data.begin()+pos) & 0xe0)
		{
		case 0xe0:
			pop_byte_8();
			remove_const(x) = pop_byte_32();
			return *this;
		case 0xc0:
			remove_const(x) = pop_byte_32() & ~0xc0000000;
			return *this;
		case 0xa0:
		case 0x80:
			remove_const(x) = pop_byte_16() & ~0x8000;
			return *this;
		}
		remove_const(x) = pop_byte_8();
		return *this;
	}

	const Marshal::OctetsStream& Marshal::OctetsStream::uncompact_sint32(const int32_t &x) const
	{
		if (pos == data.size()) throw Marshal::Exception("uncompact_sint32\n");
		switch ( *((unsigned char *)data.begin()+pos) & 0xf0)
		{
		case 0xf0:
			pop_byte_8();
			remove_const(x) = -pop_byte_32();
			return *this;
		case 0xe0:
			pop_byte_8();
			remove_const(x) = pop_byte_32();
			return *this;
		case 0xd0:
			remove_const(x) = -(pop_byte_32() & ~0xd0000000);
			return *this;
		case 0xc0:
			remove_const(x) = pop_byte_32() & ~0xc0000000;
			return *this;
		case 0xb0:
		case 0xa0:
			remove_const(x) = -(pop_byte_16() & ~0xa000);
			return *this;
		case 0x90:
		case 0x80:
			remove_const(x) = pop_byte_16() & ~0x8000;
			return *this;
		case 0x70:
		case 0x60:
		case 0x50:
		case 0x40:
			remove_const(x) = -(pop_byte_8() & ~0x40);
			return *this;
		}
		remove_const(x) = pop_byte_8();
		return *this;
	}

	Marshal::OctetsStream::OctetsStream() : pos(0) {}
	Marshal::OctetsStream::OctetsStream(const Octets &o) : data(o), pos(0) {}
	Marshal::OctetsStream::OctetsStream(const OctetsStream &os) : data(os.data), pos(0) {}

	Marshal::OctetsStream& Marshal::OctetsStream::operator = (const OctetsStream &os)
	{
		if (&os != this)
		{
			pos  = os.pos;
			data = os.data;
		}
		return *this;
	}

	bool Marshal::OctetsStream::operator == (const OctetsStream &os) const
	{
		return data == os.data;
	}
	bool Marshal::OctetsStream::operator != (const OctetsStream &os) const
	{
		return data != os.data;
	}

	size_t Marshal::OctetsStream::size() const
	{
		return data.size();
	}

	void Marshal::OctetsStream::swap( OctetsStream &os)
	{
		data.swap(os.data);
	}

	Marshal::OctetsStream::operator Octets&()
	{
		return data;
	}
	Marshal::OctetsStream::operator const Octets& () const
	{
		return data;
	}

	size_t Marshal::OctetsStream::position() const
	{
		return pos;
	}

	bool Marshal::OctetsStream::isvalid()
	{
		return pos >= Min_OctetsStream_Size && pos <= Max_OctetsStream_Size;
	}

	void* Marshal::OctetsStream::begin()
	{
		return data.begin();
	}

	void* Marshal::OctetsStream::end()
	{
		return data.end();
	}

	void Marshal::OctetsStream::reserve(size_t size)
	{
		data.reserve(size);
	}

	const void *Marshal::OctetsStream::begin() const
	{
		return data.begin();
	}
	const void *Marshal::OctetsStream::end() const
	{
		return data.end();
	}

	void Marshal::OctetsStream::insert(void *pos, const void *x, size_t len)
	{
		data.insert(pos, x, len);
	}
	void Marshal::OctetsStream::insert(void *pos, const void *x, const void *y)
	{
		data.insert(pos, x, y);
	}
	void Marshal::OctetsStream::erase(void *x, void *y)
	{
		data.erase(x, y);
	}
	void Marshal::OctetsStream::clear()
	{
		data.clear();
		pos = 0;
	}

	Marshal::OctetsStream& Marshal::OctetsStream::push_byte(const char *x, size_t len)
	{
		data.insert(data.end(), x, len);
		return *this;
	}


	Marshal::OctetsStream& Marshal::OctetsStream::operator << (char x)
	{
		return push_byte(x);
	}
	Marshal::OctetsStream& Marshal::OctetsStream::operator << (unsigned char x)
	{
		return push_byte(x);
	}
	Marshal::OctetsStream& Marshal::OctetsStream::operator << (bool x)
	{
		return push_byte(x);
	}
	Marshal::OctetsStream& Marshal::OctetsStream::operator << (short x)
	{
		return push_byte(FireNet::BYTE16Order(x));
	}
	Marshal::OctetsStream& Marshal::OctetsStream::operator << (unsigned short x)
	{
		return push_byte(FireNet::BYTE16Order(x));
	}
	Marshal::OctetsStream& Marshal::OctetsStream::operator << (int32_t x)
	{
		return push_byte(FireNet::BYTE32Order(x));
	}
	Marshal::OctetsStream& Marshal::OctetsStream::operator << (uint32_t x)
	{
		return push_byte(FireNet::BYTE32Order(x));
	}
	Marshal::OctetsStream& Marshal::OctetsStream::operator << (int64_t x)
	{
		return push_byte(FireNet::BYTE64Order(x));
	}
	Marshal::OctetsStream& Marshal::OctetsStream::operator << (uint64_t x)
	{
		return push_byte(FireNet::BYTE64Order(x));
	}
	Marshal::OctetsStream& Marshal::OctetsStream::operator << (float x)
	{
		return push_byte(FireNet::BYTE32Order(*(int*)&x));
	}
	Marshal::OctetsStream& Marshal::OctetsStream::operator << (double x)
	{
		return push_byte(FireNet::BYTE64Order(*(unsigned long long*)&x));
	}
	Marshal::OctetsStream& Marshal::OctetsStream::operator << (const Marshal &x)
	{
		return x.marshal(*this);
	}

	Marshal::OctetsStream& Marshal::OctetsStream::operator << (const Octets &x)
	{
		compact_uint32( x.size());
		data.insert(data.end(), x.begin(), x.end());
		return *this;
	}

	bool Marshal::OctetsStream::eos() const
	{
		return pos == data.size();
	}

	const Marshal::OctetsStream& Marshal::OctetsStream::operator >> (Marshal::Transaction trans) const
	{
		switch (trans)
		{
		case Marshal::Begin:
			tranpos = pos;
			break;
		case Marshal::Rollback:
			pos = tranpos;
			break;
		case Marshal::Commit:
			if (pos >= MAXSPARE)
			{
				remove_const(*this).data.erase((char*)data.begin(), (char*)data.begin()+pos);
				pos = 0;
			}
		}
		return *this;
	}

	const Marshal::OctetsStream& Marshal::OctetsStream::operator >> (const char &x) const
	{
		remove_const(x) = pop_byte_8();
		return *this;
	}
	const Marshal::OctetsStream& Marshal::OctetsStream::operator >> (const unsigned char &x) const
	{
		remove_const(x) = pop_byte_8();
		return *this;
	}
	const Marshal::OctetsStream& Marshal::OctetsStream::operator >> (const bool &x) const
	{
		remove_const(x) = pop_byte_8() != 0;
		return *this;
	}
	const Marshal::OctetsStream& Marshal::OctetsStream::operator >> (const short &x) const
	{
		remove_const(x) = pop_byte_16();
		return *this;
	}
	const Marshal::OctetsStream& Marshal::OctetsStream::operator >> (const unsigned short &x) const
	{
		remove_const(x) = pop_byte_16();
		return *this;
	}
	const Marshal::OctetsStream& Marshal::OctetsStream::operator >> (const int32_t &x) const
	{
		remove_const(x) = pop_byte_32();
		return *this;
	}
	const Marshal::OctetsStream& Marshal::OctetsStream::operator >> (const uint32_t &x) const
	{
		remove_const(x) = pop_byte_32();
		return *this;
	}
	const Marshal::OctetsStream& Marshal::OctetsStream::operator >> (const int64_t &x) const
	{
		remove_const(x) = pop_byte_64();
		return *this;
	}
	const Marshal::OctetsStream& Marshal::OctetsStream::operator >> (const uint64_t &x) const
	{
		remove_const(x) = pop_byte_64();
		return *this;
	}
	const Marshal::OctetsStream& Marshal::OctetsStream::operator >> (const float &x) const
	{
		unsigned long l = pop_byte_32();
		remove_const(x) = *(float*)&l;
		return *this;
	}
	const Marshal::OctetsStream& Marshal::OctetsStream::operator >> (const double &x) const
	{
		unsigned long long ll = pop_byte_64();
		remove_const(x) = *(double*)&ll;
		return *this;
	}
	const Marshal::OctetsStream& Marshal::OctetsStream::operator >> (const Marshal &x) const
	{
		return remove_const(x).unmarshal(*this);
	}
	const Marshal::OctetsStream& Marshal::OctetsStream::operator >> (const Octets &x) const
	{
		unsigned int len;
		uncompact_uint32(len);
		if (len > data.size() - pos) throw Marshal::Exception();
		remove_const(x).replace((char*)data.begin()+pos, len);
		pos += len;
		return *this;
	}

	void Marshal::OctetsStream::pop_byte(char *x, size_t len) const
	{
		if (pos + len > data.size()) throw Marshal::Exception();
		memcpy(x, (char*)data.begin()+pos, len);
		pos += len;
	}

    char Marshal::OctetsStream::unmarshal_char()
    {
		char ret = 0;
		if (isvalid())
		{
			ret = (char)pop_byte_8();
		}
		return ret;
    }
    
    unsigned char Marshal::OctetsStream::unmarshal_uchar()
    {
		unsigned char ret = 0;
		if (isvalid())
		{
			ret = (unsigned char)pop_byte_8();
		}
		return ret;
    }
    
    short Marshal::OctetsStream::unmarshal_short()
    {
		short ret = 0;
		if (isvalid())
		{
			ret = pop_byte_16();
		}
		return ret;
    }
    
    bool Marshal::OctetsStream::unmarshal_bool()
    {
		bool ret = false;
		if (isvalid())
		{
			ret = (pop_byte_8() != 0);
		}
		return ret;
    }
    
    int32_t Marshal::OctetsStream::unmarshal_int32()
    {
		int32_t ret = 0;
		if (isvalid())
		{
			ret = (int32_t)pop_byte_32();
		}
		return ret;
    }
    
    uint32_t Marshal::OctetsStream::unmarshal_uint32()
    {
		uint32_t ret = 0;
		if (isvalid())
		{
			ret = (uint32_t)pop_byte_32();
		}
		return ret;
    }
    
    int64_t Marshal::OctetsStream::unmarshal_int64()
    {
		int64_t ret = 0;
		if (isvalid())
		{
			ret = (int64_t)pop_byte_64();
		}
		return ret;
    }
    
    uint64_t Marshal::OctetsStream::unmarshal_uint64()
    {
		uint64_t ret = 0;
		if (isvalid())
		{
			ret = (uint64_t)pop_byte_64();
		}
		return ret;
    }
    
    float Marshal::OctetsStream::unmarshal_float()
    {
		float ret = 0.0f;
		if (isvalid())
		{
			unsigned long l = pop_byte_32();
			ret = *(float*)&l;
		}
		return ret;
    }
    
    void Marshal::OctetsStream::unmarshal_octets(FireNet::Octets& x)
	{
		if (isvalid())
		{
			unsigned int len;
			uncompact_uint32(len);
			if (len > data.size() - pos) throw Marshal::Exception();
			x.replace((char*)data.begin() + pos, len);
			pos += len;
		}
	}
    
    void Marshal::OctetsStream::unmarshal_wstring(std::wstring& x)
    {
		if (isvalid())
		{
			FireNet::Octets oct;
			unmarshal_octets(oct);
			if(oct.size() % 2 != 0)
				throw Marshal::Exception();
        
			x.clear();
			x.reserve(oct.size()/2);
        
			typedef unsigned short utf16;        
			for (utf16* ch=(utf16*)oct.begin(); ch != oct.end(); ch++)
			{
				x.insert(x.end(), (wchar_t)*ch);
			}
		}
    }
    
    void Marshal::OctetsStream::marshal_char(char x)
    {
        push_byte(x);
    }
    
    void Marshal::OctetsStream::marshal_uchar(unsigned char x)
    {
        push_byte(x);
    }
    
    void Marshal::OctetsStream::marshal_short(short x)
    {
		push_byte(FireNet::BYTE16Order(x));
    }
    
    void Marshal::OctetsStream::marshal_bool(bool x)
    {
        push_byte(x);
    }
    
    void Marshal::OctetsStream::marshal_int32(int32_t x)
    {
		push_byte(FireNet::BYTE32Order(x));
    }
    
    void Marshal::OctetsStream::marshal_uint32(uint32_t x)
    {
		push_byte(FireNet::BYTE32Order(x));
    }
    
    void Marshal::OctetsStream::marshal_int64(int64_t x)
    {
		push_byte(FireNet::BYTE64Order(x));
    }
    
    void Marshal::OctetsStream::marshal_uint64(uint64_t x)
    {
		push_byte(FireNet::BYTE64Order(x));
    }
    
    void Marshal::OctetsStream::marshal_float(float x)
    {
		push_byte(FireNet::BYTE32Order(*(int*)&x));
    }
    
    void Marshal::OctetsStream::marshal_octets(FireNet::Octets& x)
    {
        compact_uint32( x.size());
		data.insert(data.end(), x.begin(), x.end());     
    }
    
    void Marshal::OctetsStream::marshal_wstring(std::wstring& x)
    {
        typedef unsigned short utf16;
        FireNet::Octets oct(x.length()*2);
        
        for(size_t pos=0; pos<x.length(); pos++)
        {
            utf16 ch = (utf16)x[pos];
            oct.insert(oct.end(), &ch, 2);
        }
        marshal_octets(oct);
    }

} // namespace FireNet {


