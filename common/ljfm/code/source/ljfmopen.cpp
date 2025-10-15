#include "../include/common.h"
#include "../include/ljfmopen.h"
#include "../include/ljfileinfo.h"
#include "../platform/utils/Utils.h"
#include "../platform/utils/StringUtil.h"
#include "../platform/log/CoreLog.h"

#ifdef FORCEGUIEDITOR
#include <direct.h>
#endif

namespace LJFM
{
	LJFM_STATIC_ASSERT(sizeof(fsize_t) == 8);
}

namespace LJFM
{
	namespace LJFMHLP
	{
		static class LJFMDOL : public COutLog
		{
		public:
			LJFMDOL() {}
			virtual ~LJFMDOL() {}
		public:
			virtual void VPrint(const wchar_t* info, va_list vl)
			{
#if (defined WIN32) && _DEBUG
				wchar_t wbuff[2048];
				_vsnwprintf(wbuff, 2048, info, vl);
				OutputDebugStringW(wbuff);
				OutputDebugStringW(L"\n");
#endif
			}
		} g_DummyOutLog;

		COutLog* g_OutLog = &g_DummyOutLog;

	}
}

int LJFM::LJFMOpen::m_nLastError = 0;
bool LJFM::LJFMOpen::m_bLoadFromPak = false;
bool LJFM::LJFMOpen::m_bVersionDonotCheck = false;

namespace LJFM
{

	bool g_bInitLJFP = false;
	LJFP_PackInfo* g_pPackInfo = NULL;

	int LJFMOpen::InitFileList()
	{
		int iResult = 0;
		if (GetLoadFromPak() == false)
		{
			return iResult;
		}
#ifdef WIN7_32
	#ifdef NoPack
		std::wstring RootResPathBase = MHSD_UTILS::GetRootDir() + L"..\\..\\res\\";
	#else
		std::wstring RootResPathBase = MHSD_UTILS::GetRootDir() + L"..\\..\\res1\\";
	#endif
		//size_t length = RootResPathBase.size();
		//wchar_t* buffer = new wchar_t[length + 1];
		//::PathCanonicalizeW(buffer, RootResPathBase.c_str());
		//RootResPathBase = buffer;
		//delete[] buffer;
#else
		std::wstring RootResPathBase = MHSD_UTILS::GetRootDir() + L"/res/";
#endif
		std::replace(RootResPathBase.begin(), RootResPathBase.end(), L'\\', L'/');
		std::wstring FLFName = L"fl.ljpi";
//#ifdef WIN7_32
//		g_pPackInfo = LoadPackInfo(RootResPathBase + FLFName, 0, RootResPathBase);
//		if (g_pPackInfo != NULL)
//		{
//			g_bInitLJFP = true;
//			return 0;
//		}
//#endif
		if (GetVersionDonotCheck())
		{
			g_pPackInfo = LoadPackInfo(RootResPathBase + FLFName, 0, RootResPathBase);
			if (g_pPackInfo != NULL)
			{
				g_bInitLJFP = true;
				return 0;
			}
		}

		std::wstring RootResPathUp = MHSD_UTILS::GetRunDir() + L"/";
		std::replace(RootResPathUp.begin(), RootResPathUp.end(), L'\\', L'/');
		bool bResult = MergerPackInfo(RootResPathBase + FLFName, 0, RootResPathBase, RootResPathUp + FLFName, 1, RootResPathUp, g_pPackInfo);
		if (!bResult)
		{
			return -1;
		}
		if (g_pPackInfo != NULL)
		{
			g_bInitLJFP = true;
			return 0;
		}
		return -1;
	}
	int LJFMOpen::UnInitFileList()
	{
		if (g_pPackInfo)
		{
			delete g_pPackInfo;
		}
		return 0;
	}
	std::wstring TidyFileName(std::wstring strFileName)
	{
		std::wstring strResult;
		int iResult = 0;
		iResult = strFileName.find(L"/");
		if (iResult == 0)
		{
			strResult = strFileName.substr(1, strFileName.size());
		}
		iResult = strResult.find(L"mt3/");
		if (iResult == 0)
		{
			strResult = strResult.substr(4, strResult.size());
		}
		else
		{
			iResult = strResult.find(L"root/");
			if (iResult == 0)
			{
				strResult = strResult.substr(5, strResult.size());
			}
			else
			{
#ifdef NoPack
				iResult = strResult.find(L"res/");
#else
				iResult = strResult.find(L"res1/");
#endif
				if (iResult == 0)
				{
					strResult = strResult.substr(4, strResult.size());
				}
			}
		}
		return strResult;
	}
	void* LJFMOpen::GetFileInfo(std::wstring wstrFileName)
	{
		wstrFileName = TidyFileName(wstrFileName);
		if (GetLoadFromPak() == true && g_pPackInfo)
		{
			LJFP_FileInfo* pFI = g_pPackInfo->GetFileInfo(wstrFileName);
			return pFI;
		}
		else
		{
#ifdef WIN7_32
#ifdef FORCEGUIEDITOR
			char cwdBuf[512];
			memset(cwdBuf, 0, sizeof(cwdBuf));
			getcwd(cwdBuf, sizeof(cwdBuf));
			std::wstring path = s2ws(cwdBuf) + L'/';

			std::wstring RootResPath(path);
			std::wstring CacheResPath(path);
			if (wstrFileName.find(L"../res") == std::wstring::npos)
			{
				RootResPath += L"../res/";
				CacheResPath += L"../res/";
			}
			
#else
	#ifdef NoPack
			std::wstring RootResPath = MHSD_UTILS::GetRootDir() + L"..\\..\\res\\";
			std::wstring CacheResPath = MHSD_UTILS::GetRootDir() + L"..\\..\\res\\";
	#else
			std::wstring RootResPath = MHSD_UTILS::GetRootDir() + L"..\\..\\res1\\";
			std::wstring CacheResPath = MHSD_UTILS::GetRootDir() + L"..\\..\\res1\\";
	#endif
#endif //end of FORCEGUIEDITOR
			//size_t length = RootResPath.size();
			//wchar_t* buffer = new wchar_t[length + 1];
			//::PathCanonicalizeW(buffer, RootResPath.c_str());
			//RootResPath = buffer;
			//CacheResPath = RootResPath;
			//delete[] buffer;
#else
			std::wstring RootResPath = MHSD_UTILS::GetRootDir() + L"/res/";
			std::wstring CacheResPath = MHSD_UTILS::GetRunDir() + L"/";
#endif //end of WIN7_32
			if (MHSD_UTILS::ExistFile(CacheResPath + wstrFileName))
			{
				LJFP_FileInfo* FI = new LJFP_FileInfo();
				FI->m_RootPathName = CacheResPath;
				FI->m_PathFileName = wstrFileName;
				return FI;
			}
			else if (MHSD_UTILS::ExistFile(RootResPath + wstrFileName))
			{
				LJFP_FileInfo* FI = new LJFP_FileInfo();
				FI->m_RootPathName = RootResPath;
				FI->m_PathFileName = wstrFileName;
				return FI;
			}
			return NULL;
		}
	}
	std::wstring LJFMOpen::GetFullPathFileName(const std::wstring& filename)
	{
		// filenameNew将变为filename的全小写
		std::wstring filenameNew;
		if (!CheckDirFileStringFormatEx(filename, filenameNew))
		{
			_LJFM_ERROR((L"不合法的文件名格式!" + filename).c_str());
			return NULL;
		}
		LJFP_FileInfo* FI = NULL;
		FI = (LJFP_FileInfo*)GetFileInfo(filenameNew);
		if (FI)
		{
			filenameNew = FI->GetFullPathFileName();
			if (GetLoadFromPak() == false)
			{
				delete FI;
			}
		}
		else
		{
#ifdef WIN32
			LJFMOpen::GetOutLogInstance()->Print(L"ERROR: LJFMOpen::GetFullPathFileName(name:%s))\n", filename.c_str());
			if (core::Logger::GetInstance())
			{
				SDLOG_ERR(L"ERROR: LJFMOpen::GetFullPathFileName(name:%s))\n", ws2s(filename).c_str());
			}
#endif
			return L"";
		}
		return filenameNew;
	}
	LJFMBF* LJFMOpen::OpenFile(const std::wstring& filename, FILE_MODE fm, FILE_ACCESS fa)
	{
		// filenameNew将变为filename的全小写
		std::wstring filenameNew;
		if (!CheckDirFileStringFormatEx(filename, filenameNew))
		{
			_LJFM_ERROR((L"不合法的文件名格式!" + filename).c_str());
			return NULL;
		}
		LJFP_FileInfo* FI = NULL;
		FI = (LJFP_FileInfo*)GetFileInfo(filenameNew);
		if (FI)
		{
			//filenameNew = FI->GetFullPathFileName();
		}
		else
		{
#ifdef WIN32
			LJFMOpen::GetOutLogInstance()->Print(L"ERROR: LJFMOpen::OpenFile(name:%s))\n", filename.c_str());
			if (core::Logger::GetInstance())
			{
				SDLOG_ERR(L"ERROR: LJFMOpen::OpenFile(name:%s))\n", ws2s(filename).c_str());
			}
#endif
			return NULL;
		}
		bool bResult = false;
		if (FI->m_CompressType == 0 && FI->m_CodeType == 0)
		{
#ifdef ANDROID
			LJFMX::CLJFSZipFile* pLJFSZipFile = new LJFMX::CLJFSZipFile();
			bResult = pLJFSZipFile->Open(FI);
			if (GetLoadFromPak() == false)
			{
				delete FI;
			}
			if (bResult)
			{
				return pLJFSZipFile;
			}
#else
			LJFMX::CLJFSFile* pLJFSFile = new LJFMX::CLJFSFile();
			bResult = pLJFSFile->Open(FI);
			if (GetLoadFromPak() == false)
			{
				delete FI;
			}
			if (bResult)
			{
				return pLJFSFile;
			}
#endif
		}
		else
		{
			LJFMX::CLJFSZipFile* pLJFSZipFile = new LJFMX::CLJFSZipFile();
			bResult = pLJFSZipFile->Open(FI);
			if (GetLoadFromPak() == false)
			{
				delete FI;
			}
			if (bResult)
			{
				return pLJFSZipFile;
			}
		}
		return NULL;
	}
	bool LJFMOpen::CloseFile(LJFMBF* file)
	{
		if (file == NULL)
		{
			return false;
		}
		file->Delete();
		return true;
	}
	bool LJFMOpen::IsFileExisting(const std::wstring& filename)
	{
		std::wstring filenameNew;
		if (!CheckDirFileStringFormatEx(filename, filenameNew))
		{
			_LJFM_ERROR((L"不合法的文件名格式!" + filename).c_str());
			return false;
		}
		LJFP_FileInfo* FI = NULL;
		FI = (LJFP_FileInfo*)GetFileInfo(filenameNew);
		if (FI)
		{
			if (GetLoadFromPak() == false)
			{
				delete FI;
			}
			return true;
		}
		return false;
	}
	int LJFMOpen::GetLastError()
	{
		return m_nLastError;
	}
	void LJFMOpen::SetLastError(int nErrorCode)
	{
		m_nLastError = nErrorCode;
	}
	void LJFMOpen::SetOutLogInstance(COutLog* log)
	{
		LJFMHLP::g_OutLog = log;
		if (NULL == LJFMHLP::g_OutLog)
		{
			LJFMHLP::g_OutLog = &LJFMHLP::g_DummyOutLog;
		}
	}
	COutLog* LJFMOpen::GetOutLogInstance()
	{
		return LJFMHLP::g_OutLog;
	}
}