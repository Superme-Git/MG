#ifndef LJFILEINFO_H
#define LJFILEINFO_H

#include "../../../../dependencies/LJFilePack/LJFP_Version.h"
#include "../../../../dependencies/LJFilePack/LJFP_FileInfo.h"
#include "../../../../dependencies/LJFilePack/LJFP_XML.h"

class LJFP_PackInfo;
class LJFP_FileInfo;

unsigned int LJCRC32Func(unsigned int crc, const unsigned char* ptr, size_t buf_len);
int LJZipFunc(unsigned char *pDest, unsigned int *pDest_len, const unsigned char *pSource, unsigned int source_len, int level);
int LJUnZipFunc(unsigned char *pDest, unsigned int *pDest_len, const unsigned char *pSource, unsigned int source_len);
void LJSMS4Func(unsigned char* inBuff, unsigned char* ouBuff, unsigned int uiSize, std::string strPassword);
void LJDeSMS4Func(unsigned char* inBuff, unsigned char* ouBuff, unsigned int uiSize, std::string strPassword);

int LJZDataToFile(unsigned char* pSrcData, unsigned int uiSrcDataLen, std::wstring wsDstFileName);
int LJZDataToData(unsigned char* pSrcData, unsigned int uiSrcDataLen, unsigned char*& pDstData, unsigned int* uiDstDataLen);
int LJZFileToData(std::wstring wsSrcFileName, unsigned char*& pDstData, unsigned int* uiDstDataLen);
int LJZFileToFile(std::wstring wsSrcFileName, std::wstring wsDstFileName);

int LJUnZDataToFile(unsigned char* pSrcData, unsigned int uiSrcDataLen, std::wstring wsDstFileName);
int LJUnZDataToData(unsigned char* pSrcData, unsigned int uiSrcDataLen, unsigned char*& pDstData, unsigned int* uiDstDataLen);
int LJUnZFileToData(std::wstring wsSrcFileName, unsigned char*& pDstData, unsigned int* uiDstDataLen);
int LJUnZFileToFile(std::wstring wsSrcFileName, std::wstring wsDstFileName);

int LJAddDataToZipFile(std::wstring wsZipFileName, std::wstring wsDstFileName, unsigned char* ucSrcData, unsigned int uiSrcDataSize, std::wstring wsComment = L"", unsigned int uiCompressLevel = 6);
int LJAddFileToZipFile(std::wstring wsZipFileName, std::wstring wsDstFileName, std::wstring wsSrcFileName, std::wstring wsComment = L"", unsigned int uiCompressLevel = 6);

int LJUnCompress(LJFP_FileInfo* FI, unsigned char* pSrc, unsigned char*& pDst);
int LJDeCode(LJFP_FileInfo* FI, unsigned char* pSrc, unsigned char*& pDst);

LJFP_PackInfo* LoadPackInfo(std::wstring FullPathFileName, unsigned int FileArea, std::wstring RootPathName);
bool MergerPackInfo(std::wstring FullPathFileNameBase, unsigned int FileAreaBase, std::wstring RootPathNameBase,
	std::wstring FullPathFileNameNew, unsigned int FileAreaNew, std::wstring RootPathNameNew, LJFP_PackInfo*& pPIResult);
bool MergerPackInfo(LJFP_PackInfo* pPIBase, LJFP_PackInfo* pPINew, LJFP_PackInfo*& pPIResult);
int ParsePackInfo(LJFP_PackInfo* pPIBase, LJFP_PackInfo* pPINew, LJFP_PackInfo*& pPIAdd, LJFP_PackInfo*& pPIMod, LJFP_PackInfo*& pPIDel);


#endif //LJFILEINFO_H
