#ifndef __Nuclear_UTIL_H__
#define __Nuclear_UTIL_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include "nuport.h"
#if (defined WIN7_32)
#include "platform.h"
#endif
#include "CCStdC.h"

namespace Nuclear
{
	double static const COS_58 = 0.52991926423320495404678;

	float static const TEMP_SPRITE_PICTURE_WIDTH = 512.0f;
	float static const TEMP_SPRITE_PICTURE_HEIGHT = 1024.0f;
	float static const TEMP_SPRITE_PICTURE_HALF_WIDTH = TEMP_SPRITE_PICTURE_WIDTH / 2;
	float static const TEMP_SPRITE_PICTURE_HALF_HEIGHT = TEMP_SPRITE_PICTURE_HEIGHT / 2;

	#define XPSAFE_DELETE(p)       { if(p) { delete (p);     (p)=NULL; } }
	#define XPSAFE_DELETE_ARRAY(p) { if(p) { delete[] (p);   (p)=NULL; } }
	#define XPSAFE_RELEASE(p)      { if(p) { (p)->Release(); (p)=NULL; } }
		
	template<class T>
	inline T RangeRand(T min, T max)
	{
		T tmp = max - min + 1;
		return min + (tmp<=1?0:rand()%tmp);
	}
	
    union int64_h
	{
        int64 val;
        struct
		{                
            long low;  
            long high;
        };
    };
    
	inline int64 rdtsc() 
	{
        timeval now;
        gettimeofday(&now, NULL);  // ycl 时间相关代码采用 cocos 3.8 的做法
        
        int64_h v;
        v.low = now.tv_usec;
        v.high = now.tv_sec;
        
        return v.val;
	}

	void* XAlloc(int size);
	void XFree(void *p);

	template<class T>
	T* Clone(T* src)
	{
		T* n = (T*)XAlloc(sizeof(T));
		if( n != NULL )
		{
			memcpy(n, src, sizeof(T));
		}
		return n;
	}

	std::wstring CharArrayToUString(const char *buf, int bufsize);

	bool FRead(FILE *f, void *buf, size_t size);
	bool FWrite(FILE *f, const void *buf, size_t size);

	inline FILE* FOpen(const char* fn, const char* mode)
	{
#if _MSC_VER > 1200
		FILE *f = NULL;
		fopen_s(&f, fn, mode);
		return f;
#else
		return fopen(fn, mode);
#endif
		return NULL;
	}

	int64_t GetMilliSeconds();

	std::wstring StripPath(std::wstring path, wchar_t d = L'/');
	std::wstring StripPostfix(std::wstring path);

	inline bool isnanf(float n)
	{
		union FloatBits
		{
			float value;
			struct
			{
				unsigned int x : 23;
				unsigned int e : 8;
				unsigned int s : 1;
			} bitVal;
		};
		FloatBits test;
		test.value = n;
		return (test.bitVal.e == 255);
	}

	std::wstring replace( const std::wstring& inStr, const std::wstring& subStr, const std::wstring& ReplaceStr);

	class FileBuffer
	{
	public:
		FileBuffer(const char* filename);
		~FileBuffer();

	public:
		int Size() const { return m_nSize; }

		void* Begin() { return m_pBuffer; }
		const void* Begin() const { return m_pBuffer; }

		void* End() { return m_nSize + (char*)m_pBuffer; }
		const void* End() const { return m_nSize + (const char*)m_pBuffer; }

	private:
		FileBuffer(const FileBuffer&);
		FileBuffer& operator=(const FileBuffer&);

	private:
		void *m_pBuffer;
		int m_nSize;
	};
}


#define Sprite_ExtName		L".sri"
#define Effect_ExtName		L".eff.inf"
#define Bind_ExtName		L".bind.inf"
#define Map_ExtName			L".map"
#define MMap_ExtName		L".mmap"
#define ImageSet_ExtName	L".set"
#define SFI_ExtName			L".ani"//系列帧图片资源文件


#endif