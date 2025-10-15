#ifndef UpdateBin_H
#define UpdateBin_H
#include "../stdafx.h"
#include "../LJFP/LJFP_XML.h"
#include "../LJFM/LJFileInfo.h"
#include "../Utils/FileUtil.h"

using namespace std;

class UpdateBinFile
{
public:
	wstring m_wsRootPathName;
	wstring m_wsFileName;

	unsigned int m_Size;
	unsigned int m_CRC32;
	unsigned char* m_Data;

	unsigned int m_SizeOriginal;
	unsigned int m_CRC32Original;
	unsigned char* m_DataOriginal;

	unsigned int m_SizeCompress;
	unsigned int m_CRC32Compress;
	unsigned char* m_DataCompress;

	int m_iIsUse;

	UpdateBinFile()
	{
		Clear();
	}
	~UpdateBinFile()
	{
		ReleaseData();
		Clear();
	}

	UpdateBinFile* Clone()
	{
		UpdateBinFile* pResult = new UpdateBinFile();
		pResult->m_wsRootPathName = m_wsRootPathName;
		pResult->m_wsFileName = m_wsFileName;
		pResult->m_Size = m_Size;
		pResult->m_CRC32 = m_CRC32;
		pResult->m_SizeOriginal = m_SizeOriginal;
		pResult->m_CRC32Original = m_CRC32Original;
		pResult->m_SizeCompress = m_SizeCompress;
		pResult->m_CRC32Compress = m_CRC32Compress;
		return pResult;
	}

	int LoadData()
	{
		ReleaseData();
		std::ifstream FS((m_wsRootPathName + m_wsFileName).c_str(), std::ios_base::binary);
		if (!FS.is_open())
		{
			return -1;
		}
		FS.seekg(0, std::ios_base::end);
		m_SizeOriginal = FS.tellg();
		m_DataOriginal = new unsigned char[m_SizeOriginal];
		FS.seekg(0, std::ios_base::beg);
		FS.read((char*)&m_DataOriginal[0], m_SizeOriginal);
		FS.close();
		m_CRC32Original = LJCRC32Func(0, m_DataOriginal, m_SizeOriginal);
		m_Data = m_DataOriginal;
		m_Size = m_SizeOriginal;
		m_CRC32 = m_CRC32Original;
		return 0;
	}
	int CompressData()
	{
		unsigned int NewSize = m_Size + 256;
		m_DataCompress = new unsigned char[NewSize];
		int iResult = LJZipFunc(m_DataCompress, &NewSize, m_Data, m_Size, 9);
		m_SizeCompress = NewSize;
		if (iResult != 0)
		{
			return iResult;
		}
		m_CRC32Compress = LJCRC32Func(0, m_DataCompress, m_SizeCompress);
		m_Data = m_DataCompress;
		m_Size = m_SizeCompress;
		m_CRC32 = m_CRC32Compress;
		return 0;
	}
	int SaveData(std::wstring wsRootPathName)
	{
		std::ofstream FS((wsRootPathName + m_wsFileName).c_str(), std::ios_base::binary);//, std::ios_base::binary | std::ios_base::out
		if (!FS.is_open())
		{
			return -1;
		}
		FS.seekp(0, std::ios_base::beg);
		FS.write((char*)&m_Data[0], m_Size);
		FS.close();
		return 0;
	}
	int ReleaseData()
	{
		if (m_DataCompress)
		{
			delete[] m_DataCompress;
			m_DataCompress = NULL;
		}
		if (m_DataOriginal)
		{
			delete[] m_DataOriginal;
			m_DataOriginal = NULL;
		}
		m_Data = NULL;
		return 0;
	}
	int Clear()
	{
		m_wsRootPathName = L"";
		m_wsFileName = L"";

		m_Size = 0;
		m_CRC32 = 0;
		m_Data = NULL;

		m_SizeOriginal = 0;
		m_CRC32Original = 0;
		m_DataOriginal = NULL;

		m_SizeCompress = 0;
		m_CRC32Compress = 0;
		m_DataCompress = NULL;

		m_iIsUse = 0;

		return 0;
	}
};

class UpdateBinFileList
{
public:
	vector<UpdateBinFile*> m_FileArr;
	UpdateBinFileList()
	{
		
	}
	~UpdateBinFileList()
	{
		for (unsigned int i = 0; i < m_FileArr.size(); i++)
		{
			delete m_FileArr[i];
		}
		m_FileArr.clear();
	}
	int AddFile(UpdateBinFile* pFile)
	{
		m_FileArr.push_back(pFile);
		return 0;
	}
	unsigned int GetFileCount()
	{
		return m_FileArr.size();
	}
	UpdateBinFile* GetFile(int iIndex)
	{
		return m_FileArr[iIndex];
	}
	UpdateBinFile* FindFile(wstring wsFileName)
	{
		for (int i = 0; i < GetFileCount(); i++)
		{
			UpdateBinFile* pCurFile = GetFile(i);
			if (pCurFile->m_wsFileName == wsFileName)
			{
				return pCurFile;
			}
		}
		return NULL;
	}
	int Clear()
	{
		m_FileArr.clear();
		return 0;
	}
	int LoadFromXMLFile(std::wstring wsFileName)
	{
		return LoadFromFile(wsFileName, 0);
	}
	int LoadFromBinFile(std::wstring wsFileName)
	{
		return LoadFromFile(wsFileName, 1);
	}
	int LoadFromFile(std::wstring wsFileName, int iFileType)
	{
		LJFP_NodeEx* pNode0 = NULL;
		LJFP_NodeEx* pNode1 = NULL;
		LJFP_NodeEx* pNode2 = NULL;
		int iResult = 0;
		if (iFileType == 0)
		{
			iResult = pNode0->LoadFromXMLFile(wsFileName, pNode0);
		}
		else if (iFileType == 1)
		{
			iResult = pNode0->LoadFromFile(wsFileName, (LJFP_Node*&)pNode0);
		}
		else
		{
			return -1;
		}
		if (iResult != 0)
		{
			return iResult;
		}
		pNode1 = pNode0->FindNode(L"FileList");
		wstring wsFileCount = pNode1->FindAttrValue(L"Count");
		unsigned int uiFileCount = GetStringUtil().WS2UI(wsFileCount);
		wstring wsRootPathName = GetFilePath(wsFileName, L"");
		for (int i = 0; i < uiFileCount; i++)
		{
			pNode2 = pNode1->FindNode(GetStringUtil().UI2WS(i));
			wstring wsCurFileName = pNode2->FindAttrValue(L"FileName");
			UpdateBinFile* pCurFile = new UpdateBinFile();
			pCurFile->m_wsRootPathName = wsRootPathName;
			pCurFile->m_wsFileName = wsCurFileName;
			if (iFileType == 1)
			{
				wstring wsAttrValue;
				unsigned int uiAttrValue;
				wsAttrValue = pNode2->FindAttrValue(L"SO");
				uiAttrValue = GetStringUtil().WS2UI(wsAttrValue);
				pCurFile->m_SizeOriginal = uiAttrValue;
				wsAttrValue = pNode2->FindAttrValue(L"C32O");
				uiAttrValue = GetStringUtil().WS2UI(wsAttrValue);
				pCurFile->m_CRC32Original = uiAttrValue;
				wsAttrValue = pNode2->FindAttrValue(L"S");
				uiAttrValue = GetStringUtil().WS2UI(wsAttrValue);
				pCurFile->m_Size = uiAttrValue;
				wsAttrValue = pNode2->FindAttrValue(L"C32");
				uiAttrValue = GetStringUtil().WS2UI(wsAttrValue);
				pCurFile->m_CRC32 = uiAttrValue;
				pCurFile->m_SizeCompress = pCurFile->m_Size;
				pCurFile->m_CRC32Compress = pCurFile->m_CRC32;
			}
			AddFile(pCurFile);
		}
		delete pNode0;
		return 0;
	}
	int SaveToXMLFile(std::wstring wsFileName)
	{
		return SaveToFile(wsFileName, 0);
	}
	int SaveToBinFile(std::wstring wsFileName)
	{
		return SaveToFile(wsFileName, 1);
	}
	int SaveToFile(std::wstring wsFileName, int iFileType)
	{
		wstring wsPathName = GetFilePath(wsFileName, L"");
		
		LJFP_NodeEx* pNode0 = NULL;
		LJFP_NodeEx* pNode1 = NULL;
		LJFP_NodeEx* pNode2 = NULL;

		pNode0 = new LJFP_NodeEx(L"Root");
		unsigned int uiFileCount = GetFileCount();
		pNode1 = pNode0->AddNode(L"FileList", L"");
		pNode1->AddAttr(L"Count", GetStringUtil().UI2WS(uiFileCount));
		for (int i = 0; i < uiFileCount; i++)
		{
			UpdateBinFile* pCurFile = m_FileArr[i];
			pCurFile->LoadData();
			pCurFile->CompressData();
			pCurFile->SaveData(wsPathName);
			pCurFile->ReleaseData();
			pNode2 = pNode1->AddNode(GetStringUtil().UI2WS(i), L"");
			pNode2->AddAttr(L"FileName", pCurFile->m_wsFileName);
			pNode2->AddAttr(L"SO", GetStringUtil().UI2WS(pCurFile->m_SizeOriginal));
			pNode2->AddAttr(L"C32O", GetStringUtil().UI2WS(pCurFile->m_CRC32Original));
			pNode2->AddAttr(L"S", GetStringUtil().UI2WS(pCurFile->m_Size));
			pNode2->AddAttr(L"C32", GetStringUtil().UI2WS(pCurFile->m_CRC32));
			wprintf((L"MakeBinUpdate:" + pCurFile->m_wsFileName + L"\n").c_str());
		}
		int iResult = 0;
		if (iFileType == 0)
		{
			iResult = pNode0->SaveToXMLFile(wsFileName, pNode0);
		}
		else if (iFileType == 1)
		{
			iResult = pNode0->SaveToFile(wsFileName, (LJFP_Node*&)pNode0);
		}
		else
		{
			return -1;
		}
		if (iResult != 0)
		{
			return iResult;
		}
		return 0;
	}
};
static UpdateBinFileList* LoadUpdateBinFileList(std::wstring FullPathFileName)
{
	UpdateBinFileList* pResult = new UpdateBinFileList();
	int iResult = pResult->LoadFromBinFile(FullPathFileName);
	if (iResult != 0)
	{
		delete pResult;
		return NULL;
	}
	return pResult;
}
static int ParseUpdateBinFileList(UpdateBinFileList* pBFLBase, UpdateBinFileList* pBFLNew, UpdateBinFileList*& pBFLAdd, UpdateBinFileList*& pBFLMod, UpdateBinFileList*& pBFLDel)
{
	if (pBFLAdd)
	{
		delete pBFLAdd;
		pBFLAdd = NULL;
	}
	if (pBFLMod)
	{
		delete pBFLMod;
		pBFLMod = NULL;
	}
	if (pBFLDel)
	{
		delete pBFLDel;
		pBFLDel = NULL;
	}
	pBFLAdd = new UpdateBinFileList();
	pBFLMod = new UpdateBinFileList();
	pBFLDel = new UpdateBinFileList();

	UpdateBinFile* pBF = NULL;
	UpdateBinFile* pBFBase = NULL;
	UpdateBinFile* pBFNew = NULL;

	if (!pBFLBase)
	{
		for (unsigned int i = 0; i < pBFLNew->GetFileCount(); i++)
		{
			pBFNew = pBFLNew->GetFile(i);
			pBF = pBFNew->Clone();
			pBFLAdd->AddFile(pBF);	
		}
	}
	else
	{
		for (unsigned int i = 0; i < pBFLNew->GetFileCount(); i++)
		{
			pBFNew = pBFLNew->GetFile(i);
			pBFBase = pBFLBase->FindFile(pBFNew->m_wsFileName);
			if (pBFBase != NULL)
			{
				if (pBFBase->m_CRC32 != pBFNew->m_CRC32 || pBFBase->m_Size != pBFNew->m_Size)
				{
					pBF = pBFNew->Clone();
					pBFLMod->AddFile(pBF);
				}
				pBFBase->m_iIsUse = 1;
			}
			else
			{
				pBF = pBFNew->Clone();
				pBFLAdd->AddFile(pBF);
			}
		}
		for (unsigned int i = 0; i < pBFLBase->GetFileCount(); i++)
		{
			pBFBase = pBFLBase->GetFile(i);
			if (pBFBase->m_iIsUse == 0)
			{
				pBF = pBFBase->Clone();
				pBFLDel->AddFile(pBF);
			}
		}
	}
	return 0;
}

#endif