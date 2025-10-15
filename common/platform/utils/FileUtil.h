//  FileUtil.h
#ifndef share_FileUtil_h
#define share_FileUtil_h

#include <vector>
#include <string>
#include <fstream>
#include "stringutil.h"

class CFileUtil
{
public:
    static std::string GetRootDir();
    static std::string MakePath(const char* aPathPrefix, const char* aPathSubfix);    
    
    static std::string GetDocDir();
    
    static std::string GetCacheDir();
    
    static std::string GetTempDir();

	static std::string GetLogDir();
    
    static bool IsCurLanguageSimpleCh();

	static double GetTotalMemory();//获取当前设备总内存(单位：MB）

	static double GetAvailableMemory();//获取当前设备可用内存(单位：MB）

	static double GetUsedMemory(); //获取当前任务所占用的内存（单位：MB）

	static int CopyFileByFS(std::wstring Src, std::wstring Dst)
	{
		std::ifstream FSSrc(ws2s(Src).c_str(), std::ios_base::binary);
		if (!FSSrc.is_open())
		{
			return -1;
		}
		FSSrc.seekg(0, std::ios_base::beg);
		std::ofstream FSDst(ws2s(Dst).c_str(), std::ios_base::binary);
		if (!FSDst.is_open())
		{
			FSSrc.close();
			return -2;
		}
		FSDst.seekp(0, std::ios_base::beg);
		FSDst << FSSrc.rdbuf();
		FSDst.close();
		FSSrc.close();
		return 0;
	}
	static int GetDataFromFileByFS(std::wstring wsSrcFile, unsigned char*& pDstData, unsigned int& uiDstDataLen)
	{
		std::ifstream FSSrc(ws2s(wsSrcFile).c_str(), std::ios_base::binary);
		if (!FSSrc.is_open())
		{
			return -1;
		}
		if (pDstData != NULL)
		{
			delete[] pDstData;
		}
		FSSrc.seekg(0, std::ios_base::end);
		uiDstDataLen = FSSrc.tellg();
		pDstData = new unsigned char[uiDstDataLen];
		FSSrc.seekg(0, std::ios_base::beg);
		FSSrc.read((char*)&pDstData[0], uiDstDataLen);
		FSSrc.close();
		return 0;
	}
	static int PutDataToFileByFS(unsigned char* pSrcData, unsigned int uiSrcDataLen, std::wstring wsDstFile)
	{
		std::ofstream FSDst(ws2s(wsDstFile).c_str(), std::ios_base::binary);
		if (!FSDst.is_open())
		{
			return -1;
		}
		FSDst.seekp(0, std::ios_base::beg);
		FSDst.write((char*)&pSrcData[0], uiSrcDataLen);
		FSDst.close();
		return 0;
	}

	static int GetFileArrOfPath(std::wstring wsBasePath, std::wstring wsFileType, bool bLoop, std::vector<std::wstring>& FileArr);
	
	static int DelFileArrOfPath(std::wstring wsBasePath, std::wstring wsFileType, bool bLoop);
};

#endif
