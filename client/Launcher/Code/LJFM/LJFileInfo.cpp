#include "LJFileInfo.h"
#include "../LJFP/LJFP_SMS4.h"
#include "../LJFP/LJFP_MiniZ.h"
#include "../Utils/FileUtil.h"

unsigned int LJCRC32Func(unsigned int crc, const unsigned char* ptr, size_t buf_len)
{
	return crc32(crc, ptr, buf_len);
}
int LJZipFunc(unsigned char *pDest, unsigned int *pDest_len, const unsigned char *pSource, unsigned int source_len, int level)
{
	return mz_compress2(pDest, pDest_len, pSource, source_len, level);
}
int LJUnZipFunc(unsigned char *pDest, unsigned int *pDest_len, const unsigned char *pSource, unsigned int source_len)
{
	return mz_uncompress(pDest, pDest_len, pSource, source_len);
}
void LJSMS4Func(unsigned char* inBuff, unsigned char* ouBuff, unsigned int uiSize, std::string strPassword)
{
	SMS4Ex(inBuff, ouBuff, uiSize, strPassword);
}
void LJDeSMS4Func(unsigned char* inBuff, unsigned char* ouBuff, unsigned int uiSize, std::string strPassword)
{
	DeSMS4Ex(inBuff, ouBuff, uiSize, strPassword);
}
int LJZDataToFile(unsigned char* pSrcData, unsigned int uiSrcDataLen, std::wstring wsDstFileName)
{
	unsigned char* pDstData = NULL;
	unsigned int uiDstDataLen = 0;
	int iResult = LJZDataToData(pSrcData, uiSrcDataLen, pDstData, &uiDstDataLen);
	if (iResult != 0)
	{
		return -1;
	}
	return PutDataToFileByFS(pDstData, uiDstDataLen, wsDstFileName);
}
int LJZDataToData(unsigned char* pSrcData, unsigned int uiSrcDataLen, unsigned char*& pDstData, unsigned int* uiDstDataLen)
{
	return LJZipFunc(pDstData, uiDstDataLen, pSrcData, uiSrcDataLen, 9);
}
int LJZFileToData(std::wstring wsSrcFileName, unsigned char*& pDstData, unsigned int* uiDstDataLen)
{
	unsigned char* pSrcData = NULL;
	unsigned int uiSrcDataLen = 0;
	GetDataFromFileByFS(wsSrcFileName, pSrcData, uiSrcDataLen);
	*uiDstDataLen = uiSrcDataLen + 256;
	pDstData = new unsigned char[*uiDstDataLen];
	return LJZDataToData(pSrcData, uiSrcDataLen, pDstData, uiDstDataLen);
}
int LJZFileToFile(std::wstring wsSrcFileName, std::wstring wsDstFileName)
{
	unsigned char* pDstData = NULL;
	unsigned int uiDstDataLen = 0;
	int iResult = LJZFileToData(wsSrcFileName, pDstData, &uiDstDataLen);
	if (iResult != 0)
	{
		return -1;
	}
	return PutDataToFileByFS(pDstData, uiDstDataLen, wsDstFileName);
}

int LJUnZDataToFile(unsigned char* pSrcData, unsigned int uiSrcDataLen, std::wstring wsDstFileName)
{
	unsigned char* pDstData = NULL;
	unsigned int uiDstDataLen = 0;
	int iResult = LJUnZDataToData(pSrcData, uiSrcDataLen, pDstData, &uiDstDataLen);
	if (iResult != 0)
	{
		return -1;
	}
	return PutDataToFileByFS(pDstData, uiDstDataLen, wsDstFileName);
}
int LJUnZDataToData(unsigned char* pSrcData, unsigned int uiSrcDataLen, unsigned char*& pDstData, unsigned int* uiDstDataLen)
{
	return LJUnZipFunc(pDstData, uiDstDataLen, pSrcData, uiSrcDataLen);
}
int LJUnZFileToData(std::wstring wsSrcFileName, unsigned char*& pDstData, unsigned int* uiDstDataLen)
{
	unsigned char* pSrcData = NULL;
	unsigned int uiSrcDataLen = 0;
	GetDataFromFileByFS(wsSrcFileName, pSrcData, uiSrcDataLen);
	*uiDstDataLen = uiSrcDataLen * 100;
	pDstData = new unsigned char[*uiDstDataLen];
	return LJUnZDataToData(pSrcData, uiSrcDataLen, pDstData, uiDstDataLen);
}
int LJUnZFileToFile(std::wstring wsSrcFileName, std::wstring wsDstFileName)
{
	unsigned char* pDstData = NULL;
	unsigned int uiDstDataLen = 0;
	int iResult = LJUnZFileToData(wsSrcFileName, pDstData, &uiDstDataLen);
	if (iResult != 0)
	{
		return -1;
	}
	return PutDataToFileByFS(pDstData, uiDstDataLen, wsDstFileName);
}



int LJAddDataToZipFile(std::wstring wsZipFileName, std::wstring wsDstFileName, unsigned char* ucSrcData, unsigned int uiSrcDataSize, std::wstring wsComment, unsigned int uiCompressLevel)
{
	std::string sZipFileName = StringUtil::WS2S(wsZipFileName);
	std::string sDstFileName = StringUtil::WS2S(wsDstFileName);
	std::string sComment = StringUtil::WS2S(wsComment);
	bool bResult = mz_zip_add_mem_to_archive_file_in_place(sZipFileName.c_str(), sDstFileName.c_str(), ucSrcData, uiSrcDataSize, sComment.c_str(), sComment.size(), uiCompressLevel);
	if (!bResult)
	{
		return -1;
	}
	return 0;
}
int LJAddFileToZipFile(std::wstring wsZipFileName, std::wstring wsDstFileName, std::wstring wsSrcFileName, std::wstring wsComment, unsigned int uiCompressLevel)
{
	unsigned char* ucSrcData = NULL;

	std::ifstream FSSrc(StringUtil::WS2S(wsSrcFileName).c_str(), std::ios_base::binary);
	if (!FSSrc.is_open())
	{
		return -1;
	}
	FSSrc.seekg(0, std::ios_base::end);
	unsigned int uiSrcDataSize = FSSrc.tellg();
	FSSrc.seekg(0, std::ios_base::beg);
	ucSrcData = new unsigned char[uiSrcDataSize];
	FSSrc.read((char*)ucSrcData, uiSrcDataSize);
	FSSrc.close();
	unsigned int uiResult = LJAddDataToZipFile(wsZipFileName, wsDstFileName, ucSrcData, uiSrcDataSize, wsComment, uiCompressLevel);
	delete[] ucSrcData;
	return uiResult; 
}

int LJUnCompress(LJFP_FileInfo* FI, unsigned char* pSrc, unsigned char*& pDst)
{
	pDst = (unsigned char*)malloc(FI->m_SizeOriginal);
	memset(pDst, 0, FI->m_SizeOriginal);
	mz_ulong uiDst = FI->m_SizeOriginal;
	int iResult = LJUnZipFunc(pDst, &uiDst, pSrc, FI->m_Size);
	if (iResult != 0)
	{
		return -1;
	}
	return 0;
}
int LJDeCode(LJFP_FileInfo* FI, unsigned char* pSrc, unsigned char*& pDst)
{
	pDst = (unsigned char*)malloc(FI->m_Size);
	memset(pDst, 0, FI->m_Size);
	mz_ulong uiDst = FI->m_Size;
	LJDeSMS4Func(pSrc, pDst, FI->m_Size, "locojoy123456789");
	return 0;
}
LJFP_PackInfo* LoadPackInfo(std::wstring FullPathFileName, unsigned int FileArea, std::wstring RootPathName)
{
	LJFP_PackInfo* pResult = new LJFP_PackInfo();
	pResult->m_CRC32Func = LJCRC32Func;
	int iResult = pResult->LoadFromFile(FullPathFileName);
	if (iResult != 0)
	{
		delete pResult;
		return NULL;
	}
	pResult->SetFileArea(FileArea);
	pResult->SetRootPathName(RootPathName);
	pResult->MakePackFileName();
	return pResult;
}
bool MergerPackInfo(std::wstring FullPathFileNameBase, unsigned int FileAreaBase, std::wstring RootPathNameBase,
	std::wstring FullPathFileNameNew, unsigned int FileAreaNew, std::wstring RootPathNameNew, LJFP_PackInfo*& pPIResult)
{
	if (pPIResult)
	{
		delete pPIResult;
		pPIResult = NULL;
	}
	LJFP_PackInfo* pPINew = LoadPackInfo(FullPathFileNameNew, FileAreaNew, RootPathNameNew);
	if (!pPINew)
	{
		pPIResult = LoadPackInfo(FullPathFileNameBase, FileAreaBase, RootPathNameBase);
		if (!pPIResult)
		{
			return false;
		}
		return true;
	}
	LJFP_PackInfo* pPIBase = LoadPackInfo(FullPathFileNameBase, FileAreaBase, RootPathNameBase);
	if (!pPIBase)
	{
		delete pPINew;
		return false;
	}
	bool bResult = MergerPackInfo(pPIBase, pPINew, pPIResult);
	delete pPIBase;
	delete pPINew;
	return bResult;
}
bool MergerPackInfo(LJFP_PackInfo* pPIBase, LJFP_PackInfo* pPINew, LJFP_PackInfo*& pPIResult)
{
	if (pPIResult)
	{
		delete pPIResult;
		pPIResult = NULL;
	}
	pPIResult = new LJFP_PackInfo();
	pPIResult->m_CRC32Func = pPIBase->m_CRC32Func;
	LJFP_FileInfo* pFI = NULL;
	LJFP_FileInfo* pFIBase = NULL;
	LJFP_FileInfo* pFINew = NULL;
	for (unsigned int i = 0; i < pPINew->GetFileCount(); i++)
	{
		pFINew = pPINew->GetFileInfo(i);
		unsigned int FNCRC32 = pFINew->m_PathFileNameCRC32;
		pFIBase = pPIBase->FindFileInfo(FNCRC32);
		if (pFIBase != NULL)
		{
			if (pFIBase->m_CRC32 != pFINew->m_CRC32 || pFIBase->m_Size != pFINew->m_Size)
			{
				pFI = pFINew->Clone();
			}
			else
			{
				pFI = pFIBase->Clone();
			}
		}
		else
		{
			pFI = pFINew->Clone();
		}
		pPIResult->AddFileInfo(pFI);
	}
	return true;
}
int ParsePackInfo(LJFP_PackInfo* pPIBase, LJFP_PackInfo* pPINew, LJFP_PackInfo*& pPIAdd, LJFP_PackInfo*& pPIMod, LJFP_PackInfo*& pPIDel)
{
	if (pPIAdd)
	{
		delete pPIAdd;
		pPIAdd = NULL;
	}
	if (pPIMod)
	{
		delete pPIMod;
		pPIMod = NULL;
	}
	if (pPIDel)
	{
		delete pPIDel;
		pPIDel = NULL;
	}
	pPIAdd = new LJFP_PackInfo();
	pPIMod = new LJFP_PackInfo();
	pPIDel = new LJFP_PackInfo();

	pPIAdd->m_CRC32Func = pPIBase->m_CRC32Func;
	pPIMod->m_CRC32Func = pPIBase->m_CRC32Func;
	pPIDel->m_CRC32Func = pPIBase->m_CRC32Func;

	bool SameCRC32 = true;
	LJFP_FileInfo* pFI = NULL;
	LJFP_FileInfo* pFIBase = NULL;
	LJFP_FileInfo* pFINew = NULL;
	for (unsigned int i = 0; i < pPINew->GetFileCount(); i++)
	{
		pFINew = pPINew->GetFileInfo(i);
		unsigned int FNCRC32 = pFINew->m_PathFileNameCRC32;
		pFIBase = pPIBase->FindFileInfo(FNCRC32);
		if (pFIBase != NULL)
		{
			if (pFIBase->m_CRC32 != pFINew->m_CRC32 || pFIBase->m_Size != pFINew->m_Size)
			{
				pFI = pFINew->Clone();
				pPIMod->AddFileInfo(pFI);
			}
			pFIBase->m_IsUse = 1;
		}
		else
		{
			pFI = pFINew->Clone();
			pPIAdd->AddFileInfo(pFI);
		}
	}
	for (unsigned int i = 0; i < pPIBase->GetFileCount(); i++)
	{
		pFIBase = pPIBase->GetFileInfo(i);
		if (pFIBase->m_IsUse == 0)
		{
			pFI = pFIBase->Clone();
			pPIDel->AddFileInfo(pFI);
		}
	}
	return 0;
}