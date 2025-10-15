#include "nulog.h"
#include "nuxbuffer.h"

extern "C"
{
	void nedfree(void* p) throw();
	void * nedmalloc(size_t size) throw();
};
namespace Nuclear
{
	static const int READ_BLOCK_SIZE = 8192;

	bool FRead(FILE *f, void *buf, size_t size)
	{
		size_t n = size / READ_BLOCK_SIZE;
		if( size % READ_BLOCK_SIZE ) ++n;
		size_t i;
		char *p = (char*)buf;
		for(i = 0; i <n-1; ++i)
		{
			if( 1 != fread(p, READ_BLOCK_SIZE, 1, f) ) return false;
			p += READ_BLOCK_SIZE;
		}
		if( 1 != fread(p, size - READ_BLOCK_SIZE*(n-1), 1, f) ) return false;
		return true;
	}

	bool FWrite(FILE *f, const void *buf, size_t size)
	{
		size_t n = size / READ_BLOCK_SIZE;
		if( size % READ_BLOCK_SIZE ) ++n;
		size_t i;
		char *p = (char*)buf;
		for(i = 0; i <n-1; ++i)
		{
			if( 1 != fwrite(p, READ_BLOCK_SIZE, 1, f) ) return false;
			p += READ_BLOCK_SIZE;
		}
		if( 1 != fwrite(p, size - READ_BLOCK_SIZE*(n-1), 1, f) ) return false;
		return true;
	}
//
//	struct XL_ALLOC { static const wchar_t *fn() { return L"alloc.log"; } };
//
//
//#define XPDOGTAG_VALID 0xbadf00d
//#define XPDOGTAG_FREE 0x900cda7e
//#define XPDOGTAG_TAIL 0xd097a90
//
//	struct cookie {
//		size_t size;
//		int tag;
//#ifdef SELF_MALLOC
//#ifndef USE_NED_MALLOC
//		HANDLE heap;
//#endif
//#endif
//	};
//
//#ifdef SELF_MALLOC
//#ifndef USE_NED_MALLOC
//	static HANDLE s_heaps = 0;
//#endif
//#endif
//
	void* XAlloc(int size)
	{
        return malloc(size);
//#ifdef SELF_MALLOC
//#ifdef USE_NED_MALLOC
//		struct cookie *p = (struct cookie *)nedmalloc(size + sizeof(struct cookie) + sizeof(DWORD));
//#else //#ifdef USE_NED_MALLOC
//		struct cookie *p = NULL;
//		HANDLE handle = 0;
//		if (size > 1048576 - sizeof(struct cookie) - sizeof(DWORD))//1M
//		{
//			p = (struct cookie *)VirtualAlloc(NULL, size + sizeof(struct cookie) + sizeof(DWORD), MEM_RESERVE | MEM_COMMIT, PAGE_READWRITE);
//
//		} else {
//			if (s_heaps == 0)
//			{
//				s_heaps = HeapCreate(0, 10485760, 0);
//				XPASSERT(s_heaps != 0);
//			}
//			handle = s_heaps;
//			p = (struct cookie *)HeapAlloc(s_heaps, 0, size + sizeof(struct cookie) + sizeof(DWORD));
//		}
//#endif //#ifdef USE_NED_MALLOC
//#else //#ifdef SELF_MALLOC
//		struct cookie *p = (struct cookie *)malloc(size + sizeof(struct cookie) + sizeof(DWORD));
//#endif //#ifdef SELF_MALLOC
//		if (!p)
//		{
//			XPLOG_ERROR(L"Alloc faild! %d\n", size);
//		} else {
//			p->size = size;
//			p->tag = XPDOGTAG_VALID;
//#ifdef SELF_MALLOC
//#ifndef USE_NED_MALLOC
//			p->heap = handle;
//#endif
//#endif
//			++p;
//			DWORD *tail = (DWORD*)(((char*)p) + size);
//			*tail = XPDOGTAG_TAIL;
//			memset(p, 0xCC, size);
//
//		}
//		return p;
	}
	void XFree(void *p)
	{
        free(p);
		//xwprint<XL_ALLOC>(L", , free pos, %X\n", p);
//		if(!p)
//			return;
//		struct cookie *pCookie = (struct cookie *)p;
//		--pCookie;
//		XPASSERT(pCookie->tag != XPDOGTAG_FREE);
//		XPASSERT(pCookie->tag == XPDOGTAG_VALID);
//		DWORD *tail = (DWORD*)(((char*)p) + pCookie->size);
//		XPASSERT(*tail == XPDOGTAG_TAIL);
//		pCookie->tag = XPDOGTAG_FREE;
//		memset(p, 0xCC, pCookie->size);
//#ifdef SELF_MALLOC
//#ifdef USE_NED_MALLOC
//		nedfree(pCookie);
//#else //#ifdef USE_NED_MALLOC
//		if (pCookie->heap == 0)
//		{
//			VirtualFree(pCookie, 0, MEM_RELEASE);
//		} else {
//			HeapFree(pCookie->heap, 0, pCookie);
//		}
//#endif //#ifdef USE_NED_MALLOC
//#else //#ifdef SELF_MALLOC
//		free(pCookie);
//#endif //#ifdef SELF_MALLOC
	}

	int64_t GetMilliSeconds()
	{
//        cocos2d::cc_timeval now;
//        cocos2d::CCTime::gettimeofdayCocos2d(&now, NULL);
        timeval now;
        gettimeofday(&now, NULL);
		return (int64_t)now.tv_sec*1000 + now.tv_usec/1000;
	}

//	std::wstring StripPath(std::wstring path, wchar_t d)
//	{
//		size_t len = path.length();
//		std::wstring::size_type i = path.rfind(d);
//		
//		if( i == std::wstring::npos ) return path;
//
//		return path.substr(i+1);
//	}
//
//	std::wstring StripPostfix(std::wstring path)
//	{
//		size_t len = path.length();
//		std::wstring::size_type i = path.rfind(L'.');
//		
//		if( i == std::wstring::npos ) return path;
//
//		return path.substr(0,i);
//	}

	std::wstring CharArrayToUString(const char *buf, int bufsize)
	{
        assert("CharArrayToUString" && false);
//		if( buf != NULL  && bufsize > 0 )
//		{
//			int n = (int)strnlen(buf, bufsize);
//			int n2 = 2*n + 4;
//			NuclearBuffer os;
//			os.resize(n2);
//			if( int n3 = MultiByteToWideChar(
//				CP_ACP,         // code page
//				0,         // character-type options
//				buf, // string to map
//				n,       // number of bytes in string
//				(wchar_t*)os.normalbegin(),  // wide-character buffer
//				n2        // size of buffer
//				))
//			{
//				return std::wstring((wchar_t*)os.constbegin(), n3);
//			}
//		}
		return std::wstring();
	}

//	bool DeleteWinDir(std::wstring dir)
//	{
//		std::wstring str;
//		str += L"程序将删除目录 " + dir + L" ,确认删除？";
//		if( IDNO == MessageBox(NULL, str.c_str(), L"警告", MB_YESNO))
//			return true;
//		WIN32_FIND_DATA fd;
//		std::wstring searchpath = dir + L"/*";
//		HANDLE hFind = ::FindFirstFile(searchpath.c_str(), &fd);
//
//		if ( hFind != INVALID_HANDLE_VALUE )
//		{
//			do{
//				if ( 0==(fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) )//目录
//				{
//					if( fd.cFileName[0] != L'.')
//					{
//						std::wstring fn = dir + L"/" + fd.cFileName;
//						DeleteFile(fn.c_str());
//					}
//				}
//			}while (::FindNextFile(hFind, &fd));
//
//			::FindClose(hFind);
//		}
//		
//		return TRUE == RemoveDirectory(dir.c_str());
//	}
//	bool EnsureDirectoryPathExist(const std::wstring& dirPath)
//	{
//		std::wstring SrcPath = dirPath;
//		if(SrcPath.rfind('\\') < SrcPath.length()-1)
//		{
//			SrcPath += L"\\";
//		}
//
//		DWORD dwAttribute = GetFileAttributes( dirPath.c_str());
//		if(INVALID_FILE_ATTRIBUTES != dwAttribute)
//		{
//			if( FILE_ATTRIBUTE_DIRECTORY == (dwAttribute & FILE_ATTRIBUTE_DIRECTORY) )
//			{ //目录已经存在
//				return true;
//			}
//		}
//
//		std::vector<std::wstring> dirs;
//		while( !SrcPath.empty() )
//		{
//			int npos = SrcPath.find('\\');
//			if(SrcPath.find('\\') != std::wstring::npos)
//			{
//				std::wstring path = SrcPath.substr(0, npos+1);//std::wstring(SrcPath, 0, npos);
//				if( !dirs.empty() )
//				{
//					path = dirs.back() + path;
//				}
//				dirs.push_back(path);
//				SrcPath = SrcPath.substr(npos+1, SrcPath.length());
//			}
//		};
//
//		int cnt = dirs.size();
//		for(int i=0; i<cnt; i++)
//		{
//			dwAttribute = GetFileAttributes( dirs[i].c_str() );
//
//			if(INVALID_FILE_ATTRIBUTES == dwAttribute)
//			{
//				BOOL bres = CreateDirectory(dirs[i].c_str(), NULL);
//				if(!bres)
//				{
//					return false;
//				}
//			}
//		}
//
//		return true;
//	}
//
//	bool FindPath(const std::wstring &path, std::wstring &path1, std::wstring &fn1)
//	{
//		int n = (int)path.length();
//		int rn = n;
//		for(; rn>0; --rn)
//		{
//			if( path[rn-1] != L'/' && path[rn-1] != L'\\' )
//				break;
//		}
//		if( rn < 1 ) return false;
//		int path1pos = -1;
//		for(int i=rn-1; i>=0; --i)
//		{
//			if( path[i] == L'/' || path[i] == L'\\' )
//			{
//				path1pos = i;
//				break;
//			}
//			if( path[i] == L':' )
//				return false;
//		}
//		if( path1pos == -1 ) return false;
//		path1 = path.substr(0, path1pos);
//		fn1 = path.substr(path1pos+1,rn-1-path1pos);
//		return true;
//	}

	std::wstring replace( const std::wstring& inStr, const std::wstring& subStr, const std::wstring& ReplaceStr)
	{
		std::wstring strSrc = inStr;
		std::wstring::size_type pos=0;
		std::wstring::size_type srclen = subStr.size();
		std::wstring::size_type dstlen = ReplaceStr.size();
		while( (pos=strSrc.find(subStr, pos)) != std::wstring::npos)
		{
			strSrc.replace(pos, srclen, ReplaceStr);               
			pos += dstlen;
		}
		return strSrc;
	}

	FileBuffer::FileBuffer(const char* filename) : m_pBuffer(NULL), m_nSize(0)
	{
		FILE *wf = FOpen(filename, "rb");
		if( wf )
		{
			int len;
			if( fseek(wf, 0, SEEK_END) != -1 )
			{
				len = ftell(wf);
				if( len != -1 && fseek(wf,0,0) != -1 )
				{
					m_pBuffer = XAlloc(len);
					if( m_pBuffer )
					{
						if(  FRead(wf, m_pBuffer, len) ) 
							m_nSize = len; // 这里完全成功
						else
							XFree(m_pBuffer);
					}
				}
			}
			fclose(wf);
		}
	}

	FileBuffer::~FileBuffer()
	{
		if( m_pBuffer ) XFree(m_pBuffer);
	}

}
//
//#ifdef OVERRIDE_NEW
////重载new操作符
//void * operator new(size_t size)
//{
//	return Nuclear::XAlloc(size);
//}
//
//void * operator new(size_t size, int nBlockUse, const char * szFileName, int nLine)
//{
//	return Nuclear::XAlloc(size);
//}
//
//void operator delete(void* p)
//{
//	Nuclear::XFree(p);
//}
//
//void * operator new[] (size_t size, int nBlockUse, const char * szFileName, int nLine)
//{
//	return Nuclear::XAlloc(size);
//}
//
//void * operator new[] (size_t size)
//{
//	return Nuclear::XAlloc(size);
//}
//
//void operator delete[] (void* p)
//{
//	Nuclear::XFree(p);
//}


//#endif