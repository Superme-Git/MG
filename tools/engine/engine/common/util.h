#ifndef __Nuclear_UTIL_H
#define __Nuclear_UTIL_H

#if _MSC_VER > 1000
#pragma once
#endif

namespace Nuclear
{
	double static const COS_58 = 0.52991926423320495404678115181609;

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
	
	inline __int64 rdtsc() 
	{ 
		//__asm rdtsc; 
		LARGE_INTEGER li;
		QueryPerformanceCounter(&li);
		return (__int64)li.QuadPart;
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

	inline bool CopyString(LPTSTR dst, LPCTSTR src, int size)
	{
#if _MSC_VER > 1200
#ifdef _UNICODE
		wcsncpy_s(dst, size, src, _TRUNCATE );
#else
		strcpy_s(dst, size, src );
#endif
#else
		_tcsncpy(dst, src, size);
#endif
		return true;
	}

	inline bool CatString(LPTSTR dst, LPCTSTR src, int size)
	{
#if _MSC_VER > 1200
#ifdef _UNICODE
		wcscat_s(dst, size, src);
#else
		strcat_s(dst, size, src);
#endif
#else
		_tcscat(dst, src);
#endif
		return true;
	}

	inline FILE* FOpen(LPCTSTR fn, LPCTSTR mode)
	{
#if _MSC_VER > 1200
		FILE *f = NULL;
#ifdef _UNICODE
		_wfopen_s(&f, fn, mode);
#else
		fopen_s(&f, fn, mode);
#endif
		return f;
#else
		return _tfopen(fn, mode);
#endif
		return NULL;
	}

	unsigned int GetMilliSeconds();

	std::wstring StripPath(std::wstring path, wchar_t d = L'/');
	std::wstring StripPostfix(std::wstring path);
	bool FindPath(const std::wstring &path, std::wstring &path1, std::wstring &fn1);

	bool DeleteWinDir(std::wstring dir);
	bool EnsureDirectoryPathExist(const std::wstring& dirPath);

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
		// 私有成员
		void *m_pBuffer;
		int m_nSize;

		// 构造析构
	public:
		FileBuffer(LPCTSTR filename);
		~FileBuffer();

		// 操作
	public:
		int Size() const { return m_nSize; }

		void* Begin() { return m_pBuffer; }
		const void* Begin() const { return m_pBuffer; }

		void* End() { return m_nSize + (char*)m_pBuffer; }
		const void* End() const { return m_nSize + (const char*)m_pBuffer; }
		// 占坑
	private:
		FileBuffer(const FileBuffer&);
		FileBuffer& operator=(const FileBuffer&);
	};
}


#define Sprite_ExtName		L".sri"
#define Effect_ExtName		L".eff.inf"
#define Bind_ExtName		L".bind.inf"
#define Map_ExtName			L".map"
#define MMap_ExtName		L".mmap"
#define ImageSet_ExtName	L".set"
#define SFI_ExtName			L".ani"//系列帧图片资源文件
#define SPINE_ExtName		L".atlas"

#endif