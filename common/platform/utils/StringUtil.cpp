//  StringUtil.cpp
//  share
#include <iostream>
#include "StringUtil.h"
#include "stdarg.h"
#include "stdint.h"

size_t utf8toutf16(const char* u8, size_t size, wchar_t* u16, size_t outBufferSize);
size_t utf16toutf8(const wchar_t* u16, size_t size, char* u8, size_t outBufferSize);

size_t utf8toutf16(const char* u8, size_t size, wchar_t* u16, size_t outBufferSize)
{
	size_t converted = 0;
    
	while (size > 0)
	{
		if ((*u8 & 0x80) == 0)
		{
			*(u16++) = *(u8++);
			--size;
			++converted;
		}
		else if ((*u8 & 0xe0) == 0xc0)
		{
			if (size < 2)
			{
				break;
			}
			*(u16++) = (*u8 & 0x1f) | ((*(u8+1) & 0x3f) << 5);
			u8 += 2;
			size -= 2;
			++converted;
		}
		else if ((*u8 & 0xf0) == 0xe0)
		{
			if (size < 3)
			{
				break;
			}
			*u16 = ((*u8 & 0x0f) << 12) | ((*(u8+1) & 0x3f) << 6) | (*(u8+2) & 0x3f);
			u8 += 3;
			++u16;
			size -= 3;
			++converted;
		}
		else
		{
			break;
		}
		if (converted == outBufferSize)
		{
			break;
		}
	}
	return converted;
}

///////////////////////////////////////////////////////////////////////////////////////////////////
size_t utf16toutf8(const wchar_t* u16, size_t size, char* u8, size_t outBufferSize)
{
	size_t converted = 0;
    
	while (size > 0)
	{
		if (*u16 < 0x80)
		{
			//1 byte
			if (converted == outBufferSize)
			{
				break;
			}
			*(u8++) = static_cast<char>(*(u16++));
			--size;
			++converted;
		}
		else if (*u16 < 0x800)
		{
			//2 bytes
			if (converted + 2 > outBufferSize)
			{
				break;
			}
			*u8 = (*u16 >> 6) | 0xc0;
			*(u8+1) = (*u16 & 0x3f) | 0x80;
			u8 += 2;
			++u16;
			--size;
			converted += 2;
		}
		else
		{
			//3 bytes
			if (converted + 3 > outBufferSize)
			{
				break;
			}
			*u8 = (*u16 >> 12) | 0xe0;
			*(u8+1) = ((*u16 >> 6) & 0x3f) | 0x80;
			*(u8+2) = (*u16 & 0x3f) | 0x80;
			u8 += 3;
			++u16;
			--size;
			converted += 3;
		}
	}
	return converted;
}

std::string ws2s(std::wstring& ws)
{    
    char* buffer = new char[ws.length()*3 + 1];
    memset(buffer, 0, ws.length()*3 + 1);
    
    utf16toutf8(ws.c_str(), ws.length(), buffer, ws.length()*3 + 1);
    
    std::string ret(buffer);
    delete [] buffer;
    return ret;
}

std::string ws2s(const std::wstring & ws)
{
    std::wstring str(ws);
    return ws2s(str);
}

std::wstring s2ws(std::string& s)
{
    wchar_t* buffer = new wchar_t[s.length() + 1];
    memset(buffer, 0, (s.length() + 1) * sizeof(wchar_t));
        
    utf8toutf16(s.c_str(), s.length(), buffer, s.length() + 1);
    
    std::wstring ret(buffer);
    delete [] buffer;
    return ret;
}

std::wstring  s2ws(const std::string& s)
{
    std::string str(s);
    return s2ws(str);
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
typedef unsigned char   utf8;
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
typedef  unsigned short  utf32;
#else
typedef unsigned int    utf32;
#endif
typedef	unsigned int    uint;

// return number of code units in a null terminated string
size_t utf_length(const utf8* utf8_str)
{
    size_t cnt = 0;
    while (*utf8_str++)
        cnt++;
    
    return cnt;
}

// return number of code units in a null terminated string
size_t utf_length(const utf32* utf32_str)
{
    size_t cnt = 0;
    while (*utf32_str++)
        cnt++;
    
    return cnt;
}

// return the number of utf8 code units required to encode the given utf32 code point
size_t encoded_size(utf32 code_point)
{
    if (code_point < 0x80)
        return 1;
    else if (code_point < 0x0800)
        return 2;
    else if (code_point < 0x10000)
        return 3;
    else
        return 4;
}

// return number of code units required to re-encode given utf32 data as utf8.   len is number of code units in 'buf'.
size_t encoded_size(const utf32* buf, size_t len)
{
    size_t count = 0;
    
    while (len--)
    {
        count += encoded_size(*buf++);
    }
    
    return count;
}


// return number of code units required to re-encode given null-terminated utf32 data as utf8.  return does not include terminating null.
size_t encoded_size(const utf32* buf)
{
    return encoded_size(buf, utf_length(buf));
}


// return number of utf32 code units required to re-encode given utf8 data as utf32.  len is number of code units in 'buf'.
size_t encoded_size(const utf8* buf, size_t len)
{
    utf8 tcp;
    size_t count = 0;
    
    while (len--)
    {
        tcp = *buf++;
        ++count;
        size_t size = 0;
        
        if (tcp < 0x80)
        {
        }
        else if (tcp < 0xE0)
        {
            size = 1;
            ++buf;
        }
        else if (tcp < 0xF0)
        {
            size = 2;
            buf += 2;
        }
        else
        {
            size = 3;
            buf += 3;
        }
        
        if (len >= size)
            len -= size;
        else
            break;
    }
    
    return count;
}

// return number of utf32 code units required to re-encode given utf8 data as utf32.  return does not include terminating null.
size_t encoded_size(const utf8* buf)
{
    return encoded_size(buf, utf_length(buf));
}


// encoding functions
// for all:
//	src_len is in code units, or 0 for null terminated string.
//	dest_len is in code units.
//	returns number of code units put into dest buffer.
size_t encode(const utf32* src, utf8* dest, size_t dest_len, size_t src_len = 0)
{
    // count length for null terminated source...
    if (src_len == 0)
    {
        src_len = utf_length(src);
    }
    
    size_t destCapacity = dest_len;
    
    // while there is data in the source buffer,
    for (uint idx = 0; idx < src_len; ++idx)
    {
        utf32	cp = src[idx];
        
        // check there is enough destination buffer to receive this encoded unit (exit loop & return if not)
        if (destCapacity < encoded_size(cp))
        {
            break;
        }
        
        if (cp < 0x80)
        {
            *dest++ = (utf8)cp;
            --destCapacity;
        }
        else if (cp < 0x0800)
        {
            *dest++ = (utf8)((cp >> 6) | 0xC0);
            *dest++ = (utf8)((cp & 0x3F) | 0x80);
            destCapacity -= 2;
        }
        else if (cp < 0x10000)
        {
            *dest++ = (utf8)((cp >> 12) | 0xE0);
            *dest++ = (utf8)(((cp >> 6) & 0x3F) | 0x80);
            *dest++ = (utf8)((cp & 0x3F) | 0x80);
            destCapacity -= 3;
        }
        else
        {
            *dest++ = (utf8)((cp >> 18) | 0xF0);
            *dest++ = (utf8)(((cp >> 12) & 0x3F) | 0x80);
            *dest++ = (utf8)(((cp >> 6) & 0x3F) | 0x80);
            *dest++ = (utf8)((cp & 0x3F) | 0x80);
            destCapacity -= 4;
        }
        
    }
    
    return dest_len - destCapacity;
}

size_t encode(const utf8* src, utf32* dest, size_t dest_len, size_t src_len = 0)
{
    // count length for null terminated source...
    if (src_len == 0)
    {
        src_len = utf_length(src);
    }
    
    size_t destCapacity = dest_len;
    
    // while there is data in the source buffer, and space in the dest buffer
    for (uint idx = 0; ((idx < src_len) && (destCapacity > 0));)
    {
        utf32	cp;
        utf8	cu = src[idx++];
        
        if (cu < 0x80)
        {
            cp = (utf32)(cu);
        }
        else if (cu < 0xE0)
        {
            cp = ((cu & 0x1F) << 6);
            cp |= (src[idx++] & 0x3F);
        }
        else if (cu < 0xF0)
        {
            cp = ((cu & 0x0F) << 12);
            cp |= ((src[idx++] & 0x3F) << 6);
            cp |= (src[idx++] & 0x3F);
        }
        else
        {
            cp = ((cu & 0x07) << 18);
            cp |= ((src[idx++] & 0x3F) << 12);
            cp |= ((src[idx++] & 0x3F) << 6);
            cp |= (src[idx++] & 0x3F);
        }
        
        *dest++ = cp;
        --destCapacity;
    }
    
    return dest_len - destCapacity;
}


std::string SHARE_Wstring2String(const std::wstring& src)
{
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
	return ws2s(src);
#else
    const int MAX_STRING_PATH = 4096;
    utf8 dst[MAX_STRING_PATH];
    size_t length = encode((utf32*)src.c_str(), dst, MAX_STRING_PATH);
    return std::string((char*)dst,length);
#endif
}

std::wstring SHARE_String2Wstring(const std::string& src)
{
    const int MAX_STRING_PATH = 4096;
    utf32 dst[MAX_STRING_PATH];
    size_t length = encode((utf8*)src.c_str(), dst, MAX_STRING_PATH);
    return std::wstring((wchar_t*)dst, length);
}
//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
#ifdef ANDROID

wchar_t * wcpncpy_x(wchar_t *dest, const wchar_t *src , size_t n)
{
    wint_t c;
    wchar_t *const s = dest;
    
    if (n >= 4)
    {
        size_t n4 = n >> 2;
        
        for (;;)
        {
            c = *src++;
            *dest++ = c;
            if (c == L'\0')
                break;
            c = *src++;
            *dest++ = c;
            if (c == L'\0')
                break;
            c = *src++;
            *dest++ = c;
            if (c == L'\0')
                break;
            c = *src++;
            *dest++ = c;
            if (c == L'\0')
                break;
            if (--n4 == 0)
                goto last_chars;
        }
        n -= dest - s;
        goto zero_fill;
    }
    
last_chars:
    n &= 3;
    if (n == 0)
        return dest;
    
    for (;;)
    {
        c = *src++;
        --n;
        *dest++ = c;
        if (c == L'\0')
            break;
        if (n == 0)
            return dest;
    }
    
zero_fill:
    while (n-- > 0)
        dest[n] = L'\0';
    
    return dest - 1;
}

wchar_t * wcpcpy_x (wchar_t *dest, const wchar_t *src)
{
    wchar_t *wcp = (wchar_t *) dest - 1;
    wint_t c;
    const int off = (int)(src - dest + 1);
    do
    {
        c = wcp[off];
        *++wcp = c;
    }
    while (c != L'\0');
    return wcp;
}


int wcscasecmp_x (const wchar_t *s1, const wchar_t *s2)// LOCALE_PARAM)
//LOCALE_PARAM_DECL
{
    wint_t c1, c2;
    
    if (s1 == s2)
        return 0;
    
    do
    {
        c1 = tolower(*s1++);
        c2 = tolower(*s2++);
        if (c1 == L'\0')
            break;
    }
    while (c1 == c2);
    return c1 - c2;
}

wchar_t * wcschrnul_x (const wchar_t *wcs, const wchar_t wc)
{
    while (*wcs != L'\0')
        if (*wcs == wc)
            break;
        else
            ++wcs;
    return (wchar_t *) wcs;
}

int wcscmp_x(const wchar_t *s1, const wchar_t *s2)
{
    wint_t c1, c2;
    
    do
    {
        c1 = (wint_t) *s1++;
        c2 = (wint_t) *s2++;
        if (c1 == L'\0')
            return c1 - c2;
    }
    while (c1 == c2);
    
    return c1 - c2;
}



wchar_t *  wcschr_x (const wchar_t *wcs, const wchar_t wc)
{
    do
        if (*wcs == wc)
            return (wchar_t *) wcs;
    while (*wcs++ != L'\0');
    
    return NULL;
}

size_t  wcslen_x(const wchar_t *s)
{
    size_t len = 0;
    while (s[len] != L'\0')
    {
        if (s[++len] == L'\0')
            return len;
        if (s[++len] == L'\0')
            return len;
        if (s[++len] == L'\0')
            return len;
        ++len;
    }
    return len;
}

wchar_t * wcscat_x (wchar_t *dest, const wchar_t *src)
{
    register wchar_t *s1 = dest;
    register const wchar_t *s2 = src;
    wchar_t c;
    
    /* Find the end of the string.  */
    do
    {
        c = *s1++;
    }
    while (c != L'\0');
    
    /* Make S1 point before the next character, so we can increment
     it while memory is read (wins on pipelined cpus).	*/
    s1 -= 2;
    
    do
    {
        c = *s2++;
        *++s1 = c;
    }
    while (c != L'\0');
    
    return dest;
}

wchar_t * wcsdup_x (const wchar_t *s)
{
    size_t len = (wcslen_x (s) + 1) * sizeof (wchar_t);
    void * newStr = malloc (len);
    
    if (newStr == NULL)
        return NULL;
    return (wchar_t *) memcpy (newStr, (void *) s, len);
}

int wcsncasecmp_x (const wchar_t *s1, const wchar_t *s2, size_t n )//LOCALE_PARAM)
//LOCALE_PARAM_DECL
{
    wint_t c1, c2;
    
    if (s1 == s2 || n == 0)
        return 0;
    
    do
    {
        c1 = (wint_t) tolower (*s1++);
        c2 = (wint_t) tolower (*s2++);
        if (c1 == L'\0' || c1 != c2)
            return c1 - c2;
    } while (--n > 0);
    
    return c1 - c2;
}

wchar_t * wcsncat_x (wchar_t *dest, const wchar_t *src, size_t n)
{
    wchar_t c;
    wchar_t * const s = dest;
    
    /* Find the end of DEST.  */
    do
        c = *dest++;
    while (c != L'\0');
    
    /* Make DEST point before next character, so we can increment
     it while memory is read (wins on pipelined cpus).	*/
    dest -= 2;
    
    if (n >= 4)
    {
        size_t n4 = n >> 2;
        do
        {
            c = *src++;
            *++dest = c;
            if (c == L'\0')
                return s;
            c = *src++;
            *++dest = c;
            if (c == L'\0')
                return s;
            c = *src++;
            *++dest = c;
            if (c == L'\0')
                return s;
            c = *src++;
            *++dest = c;
            if (c == L'\0')
                return s;
        } while (--n4 > 0);
        n &= 3;
    }
    
    while (n > 0)
    {
        c = *src++;
        *++dest = c;
        if (c == L'\0')
            return s;
        n--;
    }
    
    if (c != L'\0')
        *++dest = L'\0';
    
    return s;
}


wchar_t * wcsncpy_x (wchar_t *dest, const wchar_t *src, size_t n)
{
    wint_t c;
    wchar_t *const s = dest;
    
    --dest;
    
    if (n >= 4)
    {
        size_t n4 = n >> 2;
        
        for (;;)
        {
            c = *src++;
            *++dest = c;
            if (c == L'\0')
                break;
            c = *src++;
            *++dest = c;
            if (c == L'\0')
                break;
            c = *src++;
            *++dest = c;
            if (c == L'\0')
                break;
            c = *src++;
            *++dest = c;
            if (c == L'\0')
                break;
            if (--n4 == 0)
                goto last_chars;
        }
        n = n - (dest - s) - 1;
        if (n == 0)
            return s;
        goto zero_fill;
    }
    
last_chars:
    n &= 3;
    if (n == 0)
        return s;
    
    do
    {
        c = *src++;
        *++dest = c;
        if (--n == 0)
            return s;
    }
    while (c != L'\0');
    
zero_fill:
    do
        *++dest = L'\0';
    while (--n > 0);
    
    return s;
}

size_t wcsnlen_x (const wchar_t *s, size_t maxlen)
{
    size_t len = 0;
    
    while (maxlen > 0 && s[len] != L'\0')
    {
        ++len;
        if (--maxlen == 0 || s[len] == L'\0')
            return len;
        ++len;
        if (--maxlen == 0 || s[len] == L'\0')
            return len;
        ++len;
        if (--maxlen == 0 || s[len] == L'\0')
            return len;
        ++len;
        --maxlen;
    }
    
    return len;
}

wchar_t * wcsstr_x (const wchar_t *haystack, const wchar_t *needle)
{
    wchar_t b, c;
    
    if ((b = *needle) != L'\0')
    {
        haystack--;				/* possible ANSI violation */
        do
            if ((c = *++haystack) == L'\0')
                goto ret0;
        while (c != b);
        
        if (!(c = *++needle))
            goto foundneedle;
        ++needle;
        goto jin;
        
        for (;;)
        {
            wchar_t a;
            const wchar_t *rhaystack, *rneedle;
            
            do
            {
                if (!(a = *++haystack))
                    goto ret0;
                if (a == b)
                    break;
                if ((a = *++haystack) == L'\0')
                    goto ret0;
            shloop:	      ;
            }
            while (a != b);
            
        jin:	  if (!(a = *++haystack))
            goto ret0;
            
            if (a != c)
                goto shloop;
            
            if (*(rhaystack = haystack-- + 1) == (a = *(rneedle = needle)))
                do
                {
                    if (a == L'\0')
                        goto foundneedle;
                    if (*++rhaystack != (a = *++needle))
                        break;
                    if (a == L'\0')
                        goto foundneedle;
                }
            while (*++rhaystack == (a = *++needle));
            
            needle = rneedle;		  /* took the register-poor approach */
            
            if (a == L'\0')
                break;
        }
    }
foundneedle:
    return (wchar_t*) haystack;
ret0:
    return NULL;
}

size_t wcscspn_x(const wchar_t *wcs, const wchar_t *reject)
{
    register size_t count = 0;
    
    while (*wcs != L'\0')
        if (wcschr_x (reject, *wcs++) == NULL)
            ++count;
        else
            return count;
    
    return count;
}
void       tolower_x    (wchar_t * buffer)
{
    for(size_t i = 0 ; i < wcslen_x(buffer) ; i ++ )
    {
        tolower(buffer[i]);
    }
}

void       toupper_x    (wchar_t * buffer)				
{
    for(size_t i = 0 ; i < wcslen_x(buffer) ; i ++ )
    {
        toupper(buffer[i]);
    }
}

#endif

/////////////////////////////////////////////////////////////
enum fmt_arg_type
{
    fat_none,
    fat_float,
    fat_interger,
    fat_pointer,
    fat_char,
    fat_llinteger,
    fat_ullinteger
};

int xfind_fmt_tag(std::string& _fmt , const wchar_t *_buf , fmt_arg_type& _type)
{
    const wchar_t* _base = _buf;
    _type = fat_none;
    //prefix
    if(*_buf == '-' || *_buf == '+' || *_buf == ' ' || *_buf == '0' || *_buf == '#'  )
    {
        _fmt += (char)(*_buf);
        _buf ++;
    }
    
    //width
    if(*_buf <='9' && *_buf >= '0' )
    {
        _fmt += (char)(*_buf);
        _buf ++;
    }
    
    //.
    if(*_buf =='.')
    {
        _fmt += (char)(*_buf);
        _buf ++;
    }
    
    //precision
    if(*_buf <='9' && *_buf >= '0' )
    {
        _fmt += (char)(*_buf);
        _buf ++;
    }
    
    //[{h | l | I64 | L}]
    if(*_buf == 'I' || *_buf == 'l' || *_buf == 'h' || *_buf == 'L' )
    {
        _fmt += (char)(*_buf);
        _buf ++;
        if((*_buf == 'l') && (*(_buf+1) == 'd') )
        {
            _fmt += (char)(*_buf);
            _buf ++;
            
            _fmt += (char)(*_buf);
            _buf ++;
            
            _type = fat_llinteger;
            return _buf - _base;
        }
        //uint64_t
        else if((*_buf == 'l') && (*(_buf+1) == 'u') )
        {
            _fmt += (char)(*_buf);
            _buf ++;
            
            _fmt += (char)(*_buf);
            _buf ++;
            
            _type = fat_ullinteger;
            return _buf - _base;
        }
    }
    else if(*_buf == 'c' || *_buf == 'C')
    {
        _type = fat_char;
    }
    else if(*_buf == 'd' || *_buf == 'i' || *_buf == 'o' || *_buf == 'u' || *_buf == 'x' || *_buf == 'X' || *_buf == 'e' || *_buf == 'E')
    {
        _type = fat_interger;
    }
    else if( *_buf == 'f' || *_buf == 'g' || *_buf == 'G' || *_buf == 'n' || *_buf == 'p'  )
    {
        _type = fat_float;
    }
    else if( *_buf == 'n' || *_buf == 'p'  )
    {
        _type = fat_pointer;
    }
    
    if(_type != fat_none)
    {
        _fmt += (char)(*_buf);
        _buf ++;
        return _buf - _base;
    }
    
    return 0;
    
}


#define GUARD_COUNT  {idx ++ ;  if(idx >= ( (int)count - 1) ) { _out[idx] = 0 ; return idx; } }
int vsnwprintf_x(wchar_t* _out , size_t count , const wchar_t * wcsfmtStr , va_list ap)
{
    if (!wcsfmtStr) {
        return 0;
    }
    int   idx              = 0;
    wchar_t* _ret          = _out;
    const wchar_t *_fmtStr = wcsfmtStr;
    while(*_fmtStr!=0)
    {
        if(*_fmtStr!='%')
        {
            *_ret = *_fmtStr;
            _ret ++;
            GUARD_COUNT(idx);
            _fmtStr++;
        }
        else
        {
            switch(*(++_fmtStr))
            {
                case 's':
                {
                    const char* wcsValue = va_arg(ap , const char*);
                    while( *wcsValue)
                    {
                        *_ret = *wcsValue;
                        wcsValue ++;
                        _ret ++;
                        GUARD_COUNT(idx);
                    }
                    _fmtStr++;
                }
                    break;
                case 'S':
                {
                    const wchar_t* wcsValue = va_arg(ap , const wchar_t*);
                    while( *wcsValue)
                    {
                        *_ret = *wcsValue;
                        wcsValue ++;
                        _ret ++;
                        GUARD_COUNT(idx);
                    }
                    _fmtStr++;
                }
                    break;
                case 'l':
                {
                    if(*(_fmtStr+1) == 's')
                    {
                        ++_fmtStr;
                        const wchar_t* wcsValue = va_arg(ap , const wchar_t*);
                        while( *wcsValue)
                        {
                            *_ret = *wcsValue;
                            wcsValue ++;
                            _ret ++;
                            GUARD_COUNT(idx);
                        }
                        _fmtStr++;
                        break;
                    }
                }
                default:
                {
                    std::string fmtStr = "%";
                    fmt_arg_type _type;
                    int nSkip = xfind_fmt_tag(fmtStr , _fmtStr , _type);
                    if(nSkip > 0)
                    {
                        char ansiBuf[1024] = {0};
                        switch(_type)
                        {
                            case fat_char:
                            {
                                char n = (char)va_arg(ap , int);
                                sprintf(ansiBuf , fmtStr.c_str() , n );
                            }
                                break;
                            case fat_interger:
                            {
                                int n = va_arg(ap , int);
                                sprintf(ansiBuf , fmtStr.c_str() , n );
                            }
                                break;
                            case fat_float:
                            {
                                float n = va_arg(ap , double);
                                sprintf(ansiBuf , fmtStr.c_str() , n );
                            }
                                break;
                            case fat_llinteger:
                            {
                                int64_t n = va_arg(ap , int64_t);
                                sprintf(ansiBuf , fmtStr.c_str() , n );
                            }
                                break;
                            case fat_ullinteger:
                            {
                                uint64_t n = va_arg(ap , uint64_t);
                                sprintf(ansiBuf , fmtStr.c_str() , n );
                            }
                                break;
                            case fat_pointer:
                            {
                                void* n = va_arg(ap , void*);
                                sprintf(ansiBuf , fmtStr.c_str() , n );
                            }
                                break;
                        }
                        int i = 0;
                        while(ansiBuf[i ])
                        {
                            *_ret = ansiBuf[i];
                            _ret ++; i ++;
                            GUARD_COUNT(idx);
                        }
                        
                        _fmtStr += nSkip;
                    }
                    else
                    {
                        *_ret = *_fmtStr;
                        _ret ++;
                        _fmtStr++;
                        GUARD_COUNT(idx);
                    }
                }
            }
        }
        
    }
    return idx;
}


int SprintfW( wchar_t* out, int len ,const wchar_t *format ,...)
{
    va_list ap;
    va_start(ap,format);
    
    int numforwrite = 0;
    memset(out, 0, len*sizeof(wchar_t));
    
    numforwrite = vsnwprintf_x( out,len,format,ap);
    
    va_end(ap);
    return numforwrite;
}

int FormatString( std::wstring& instring,const wchar_t *format ,...)
{
    va_list ap;
    va_start(ap,format);
    wchar_t buffer[1024] = {0};
    
    int numforwrite = 0;
    
    numforwrite = vsnwprintf_x( buffer,1024,format,ap);
    
    if ( numforwrite > 0)
    {
        std::wstring str(buffer);
        instring = str;
    }
    
    else
        instring = L"";
    
    va_end(ap);
    return numforwrite;
}








