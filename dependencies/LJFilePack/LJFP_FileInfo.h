#ifndef LJFP_FileInfo_H
#define LJFP_FileInfo_H

#include "LJFP_Var.h"
#include "LJFP_StringUtil.h"
#include "LJFP_Node.h"
//#include <tr1/unordered_map>

class LJFP_FileInfo
{
public:
	LJFP_FileInfo()
	{
		m_FileArea = 0;

		m_PackIndex = 0;
		m_Pos = 0;
		m_Size = 0;
		m_CRC32 = 0;

		m_CompressType = 0;
		m_CodeType = 0;

		m_SizeOriginal = 0;
		m_CRC32Original = 0;

		m_IsUse = 0;

		m_PathFileNameCRC32 = 0;

	}

	LJFP_FileInfo* Clone()
	{
		LJFP_FileInfo* pFI = new LJFP_FileInfo();
		pFI->m_FileArea = m_FileArea;
		
		pFI->m_PackIndex = m_PackIndex;
		pFI->m_Pos = m_Pos;
		pFI->m_Size = m_Size;
		pFI->m_CRC32 = m_CRC32;

		pFI->m_CompressType = m_CompressType;
		pFI->m_CodeType = m_CodeType;

		pFI->m_SizeOriginal = m_SizeOriginal;
		pFI->m_CRC32Original = m_CRC32Original;

		pFI->m_RootPathName = m_RootPathName;
		pFI->m_PathFileName = m_PathFileName;
		pFI->m_PackFileName = m_PackFileName;

		pFI->m_PathFileNameCRC32 = m_PathFileNameCRC32;

		return pFI;
	}

	unsigned int m_FileArea;

	unsigned int m_PackIndex;
	unsigned int m_Pos;
	unsigned int m_Size;
	unsigned int m_CRC32;

	unsigned int m_CompressType;
	unsigned int m_CodeType;

	unsigned int m_SizeOriginal;
	unsigned int m_CRC32Original;

	std::wstring m_RootPathName;
	std::wstring m_PathFileName;
	std::wstring m_PackFileName;

	unsigned int m_PathFileNameCRC32;

	unsigned int m_IsUse;//Runtime

	std::wstring GetFullPathFileName()
	{
		if (m_FileArea == 0 && m_PackIndex > 0)
		{
			return m_RootPathName + m_PackFileName;
		}
		else
		{
#ifdef ANDROID
			if (m_FileArea == 0)
			{
				return L"res/" + m_PathFileName;
			}
			else
			{
				return m_RootPathName + m_PathFileName;
			}
#else
			return m_RootPathName + m_PathFileName;
#endif
		}
	}
	int LoadFromStream(std::ifstream& FS)
	{
		FS.read((char*)&m_PackIndex, 4);
		if (m_PackIndex > 0)
		{
			FS.read((char*)&m_Pos, 4);
		}
		FS.read((char*)&m_Size, 4);
		FS.read((char*)&m_CRC32, 4);
		FS.read((char*)&m_CompressType, 4);
		FS.read((char*)&m_CodeType, 4);
		if (m_CompressType > 0 || m_CodeType > 0)
		{
			FS.read((char*)&m_SizeOriginal, 4);
			FS.read((char*)&m_CRC32Original, 4);
		}
		else
		{
			m_SizeOriginal = m_Size;
			m_CRC32Original = m_CRC32;
		}
		//unsigned int ui = 0;
		//FS.read((char*)&ui, 4);
		//std::string PFN;
		//PFN.resize(ui);
		//FS.read((char*)PFN.c_str(), ui);
		//m_PathFileName = s2ws(PFN);

		FS.read((char*)&m_PathFileNameCRC32, 4);

		m_PathFileName = GetStringUtil().UI2WS(m_PathFileNameCRC32);

		return 0;
	}
	int LoadFromFile(std::wstring strFileName)
	{
		std::ifstream FS(ws2s(strFileName).c_str(), std::ios_base::binary);
		if (FS.is_open() == false)
		{
			return -1;
		}
		FS.seekg(0, std::ios_base::beg);
		return LoadFromStream(FS);
	}
	int SaveToStream(std::ofstream& FS)
	{
		FS.write((char*)&m_PackIndex, 4);
		if (m_PackIndex > 0)
		{
			FS.write((char*)&m_Pos, 4);
		}
		FS.write((char*)&m_Size, 4);
		FS.write((char*)&m_CRC32, 4);
		FS.write((char*)&m_CompressType, 4);
		FS.write((char*)&m_CodeType, 4);
		if (m_CompressType > 0 || m_CodeType > 0)
		{
			FS.write((char*)&m_SizeOriginal, 4);
			FS.write((char*)&m_CRC32Original, 4);
		}
		//unsigned int ui = m_PathFileName.size() * sizeof(wchar_t);
		//FS.write((char*)&ui, 4);
		//FS.write((char*)m_PathFileName.c_str(), ui);

		//std::string PFN = ws2s(m_PathFileName);
		//unsigned int ui = PFN.size() * sizeof(char);
		//FS.write((char*)&ui, 4);
		//FS.write((char*)PFN.c_str(), ui);

		FS.write((char*)&m_PathFileNameCRC32, 4);
		return 0;
	}
	int SaveToFile(std::wstring strFileName)
	{
		std::ofstream FS(ws2s(strFileName).c_str(), std::ios_base::binary);
		if (FS.is_open() == false)
		{
			return -1;
		}
		FS.seekp(0, std::ios_base::beg);
		return SaveToStream(FS);
	}
};

//typedef std::tr1::unordered_map<unsigned int, LJFP_FileInfo*> LJFP_FileInfoMap;
typedef std::map<unsigned int, LJFP_FileInfo*> LJFP_FileInfoMap;
typedef std::vector<LJFP_FileInfo*> LJFP_FileInfoArr;

struct LJFP_PackInfoOne
{
	unsigned int m_uiFileCount;
	unsigned int m_uiSize;
	LJFP_PackInfoOne()
	{
		m_uiFileCount = 0;
		m_uiSize = 0;
	}
};
typedef std::map<unsigned int, LJFP_PackInfoOne> LJFP_PackInfoOneMap;

class LJFP_PackInfo
{
public:
	LJFP_FileInfoMap m_FileInfoMap;
	LJFP_FileInfoArr m_FileInfoArr;
	LJFP_PackInfoOneMap m_PackInfoOne;

	CRC32_Func m_CRC32Func;

	LJFP_PackInfo()
	{
		
	}
	~LJFP_PackInfo()
	{
		for (unsigned int i = 0; i < GetFileCount(); i++)
		{
			delete m_FileInfoArr[i];
		}
		m_FileInfoArr.clear();
		m_FileInfoMap.clear();
	}
	int SetFileArea(unsigned int FileArea)
	{
		for (unsigned int i = 0; i < GetFileCount(); i++)
		{
			m_FileInfoArr[i]->m_FileArea = FileArea;
		}
		return 0;
	}
	int SetRootPathName(std::wstring RootPathName)
	{
		for (unsigned int i = 0; i < GetFileCount(); i++)
		{
			m_FileInfoArr[i]->m_RootPathName = RootPathName;
		}
		return 0;
	}
	int MakePackFileName()
	{
		for (unsigned int i = 0; i < GetFileCount(); i++)
		{
			if (m_FileInfoArr[i]->m_PackIndex > 0 && m_FileInfoArr[i]->m_FileArea == 0)
			{
				m_FileInfoArr[i]->m_PackFileName = GetStringUtil().UI2WS(m_FileInfoArr[i]->m_PackIndex) + L".ljfp";
			}
		}
		return 0;
	}
	int Clear()
	{
		for (unsigned int i = 0; i < GetFileCount(); i++)
		{
			delete m_FileInfoArr[i];
		}
		m_FileInfoArr.clear();
		m_FileInfoMap.clear();
		m_PackInfoOne.clear();
		return 0;
	}
	unsigned int GetPackCount()
	{
		return m_PackInfoOne.size();
	}
	unsigned int GetFileCount()
	{
		return m_FileInfoArr.size();
	}
	int AddFileInfo(LJFP_FileInfo* pFileInfo)
	{
		m_FileInfoArr.push_back(pFileInfo);
		if (m_CRC32Func)
		{
			unsigned int FNCRC32 = pFileInfo->m_PathFileNameCRC32;
			m_FileInfoMap[FNCRC32] = pFileInfo;
		}
		return GetFileCount() - 1;
	}
	LJFP_FileInfo* GetFileInfo(unsigned int uiIndex)
	{
		if (uiIndex >= 0 && uiIndex < GetFileCount())
		{
			return m_FileInfoArr[uiIndex];
		}
		return NULL;
	}
	LJFP_FileInfo* GetFileInfo(std::wstring wstrPathFileName)
	{
		if (!m_CRC32Func)
		{
			return NULL;
		}
		std::string strPathFileName = ws2s(wstrPathFileName);
		unsigned int FNCRC32 = m_CRC32Func(0, (unsigned char*)(strPathFileName.c_str()), strPathFileName.size() * sizeof(char));
		return FindFileInfo(FNCRC32);
	}
	LJFP_FileInfo* FindFileInfo(unsigned int uiCRC32)
	{
		LJFP_FileInfoMap::iterator it = m_FileInfoMap.find(uiCRC32);
		if (it != m_FileInfoMap.end())
		{
			return it->second;
		}
		return NULL;
	}
	int LoadFromStream(std::ifstream& FS)
	{
		Clear();
		unsigned int FileCount = 0;
		FS.read((char*)&FileCount, 4);
		m_FileInfoArr.resize(FileCount);
		for (unsigned int i = 0; i < FileCount; i++)
		{
			m_FileInfoArr[i] = new LJFP_FileInfo();
			m_FileInfoArr[i]->LoadFromStream(FS);
			if (m_CRC32Func)
			{
				unsigned int FNCRC32 = m_FileInfoArr[i]->m_PathFileNameCRC32;
                m_FileInfoMap[FNCRC32] = m_FileInfoArr[i];
			}
			m_PackInfoOne[m_FileInfoArr[i]->m_PackIndex].m_uiFileCount++;
			m_PackInfoOne[m_FileInfoArr[i]->m_PackIndex].m_uiSize += m_FileInfoArr[i]->m_Size;
		}
		LJFP_PackInfoOneMap::iterator it = m_PackInfoOne.find(0);
		if (it == m_PackInfoOne.end())
		{
			m_PackInfoOne[0].m_uiFileCount = 0;
			m_PackInfoOne[0].m_uiSize = 0;
		}
		return 0;
	}
	int LoadFromFile(std::wstring strFileName)
	{
		std::ifstream FS(ws2s(strFileName).c_str(), std::ios_base::binary);
		if (FS.is_open() == false)
		{
			return -1;
		}
		FS.seekg(0, std::ios_base::beg);
		return LoadFromStream(FS);
	}
	int SaveToStream(std::ofstream& FS)
	{
		unsigned int FileCount = GetFileCount();
		FS.write((char*)&FileCount, 4);
		for (unsigned int i = 0; i < FileCount; i++)
		{
			m_FileInfoArr[i]->SaveToStream(FS);
		}
		return 0;
	}
	int SaveToFile(std::wstring strFileName)
	{
		std::ofstream FS(ws2s(strFileName).c_str(), std::ios_base::binary);
		if (FS.is_open() == false)
		{
			return -1;
		}
		FS.seekp(0, std::ios_base::beg);
		return SaveToStream(FS);
	}
	int LoadFromNode(LJFP_Node* FN)
	{
		Clear();
		LJFP_Node* pNode1 = NULL;
		LJFP_Node* pNode2 = NULL;
		LJFP_FileInfo* pFI = NULL;
		unsigned int uiPackCount = GetStringUtil().WS2UI(FN->FindAttrValue(L"PackCount"));
		for (unsigned int i = 0; i < uiPackCount; i++)
		{
			pNode1 = FN->FindNode(GetStringUtil().UI2WS(i));
			unsigned int uiFileCount = GetStringUtil().WS2UI(pNode1->FindAttrValue(L"FileCount"));
			unsigned int uiSize = GetStringUtil().WS2UI(pNode1->FindAttrValue(L"Size"));
			m_PackInfoOne[i].m_uiFileCount = uiFileCount;
			m_PackInfoOne[i].m_uiSize = uiSize;
			unsigned int uiFileIndex = GetFileCount();
			m_FileInfoArr.resize(uiFileIndex + uiFileCount);
			for (unsigned int i2 = 0; i2 < uiFileCount; i2++)
			{
				pNode2 = pNode1->FindNode(GetStringUtil().UI2WS(i2));
				pFI = pFI = new LJFP_FileInfo();
				pFI->m_PackIndex = i;
				pFI->m_Size = GetStringUtil().WS2UI(pNode2->FindAttrValue(L"S"));
				pFI->m_CRC32 = GetStringUtil().WS2UI(pNode2->FindAttrValue(L"C32"));
				//pFI->m_PathFileName = pNode2->FindAttrValue(L"PFN");
				pFI->m_PathFileNameCRC32 = GetStringUtil().WS2UI(pNode2->FindAttrValue(L"PFNC32"));
				pFI->m_PathFileName = GetStringUtil().UI2WS(pFI->m_PathFileNameCRC32);
				pFI->m_CompressType = GetStringUtil().WS2UI(pNode2->FindAttrValue(L"CPT"));
				pFI->m_CodeType = GetStringUtil().WS2UI(pNode2->FindAttrValue(L"CDT"));
				if (pFI->m_CompressType > 0 || pFI->m_CodeType > 0)
				{
					pFI->m_SizeOriginal = GetStringUtil().WS2UI(pNode2->FindAttrValue(L"SO"));
					pFI->m_CRC32Original = GetStringUtil().WS2UI(pNode2->FindAttrValue(L"C32O"));
				}
				if (i != 0)//第一个包全是散文件
				{
					pFI->m_Pos = GetStringUtil().WS2UI(pNode2->FindAttrValue(L"P"));
				}
				m_FileInfoArr[uiFileIndex + i2] = pFI;
				if (m_CRC32Func)
				{
					std::string strPathFileName = ws2s(pFI->m_PathFileName);
					unsigned int FNCRC32 = m_CRC32Func(0, (unsigned char*)(strPathFileName.c_str()), strPathFileName.size() * sizeof(char));
					m_FileInfoMap[FNCRC32] = pFI;
				}
			}
		}
		return 0;
	}
	unsigned int GetFileIndexInPack(unsigned int uiFileIndex, unsigned int uiPackIndex)
	{
		unsigned int uiFileIndexBegin = 0;
		for (unsigned int i = 0; i < uiPackIndex; i++)
		{
			uiFileIndexBegin += m_PackInfoOne[i].m_uiFileCount;
		}
		return uiFileIndex - uiFileIndexBegin;
	}
	int SaveToNode(LJFP_Node*& FN)
	{
		std::wstring wsKey = L"Root";
		std::wstring wsValue = L"";
		FN = new LJFP_Node(wsKey, wsValue);
		wsKey = L"PackCount";
		unsigned int uiPackCount = this->GetPackCount();
		wsValue = GetStringUtil().UI2WS(uiPackCount);
		FN->AddAttr(wsKey, wsValue);
		std::map<unsigned int,LJFP_Node*> PackArr;
		for (unsigned int i = 0; i < uiPackCount; i++)
		{
			wsKey = GetStringUtil().UI2WS(i); wsValue = L"";
			PackArr[i] = new LJFP_Node(wsKey, wsValue);
			wsKey = L"FileCount"; wsValue = GetStringUtil().UI2WS(m_PackInfoOne[i].m_uiFileCount);
			PackArr[i]->AddAttr(wsKey, wsValue);
			wsKey = L"Size";wsValue = GetStringUtil().UI2WS(m_PackInfoOne[i].m_uiSize);
			PackArr[i]->AddAttr(wsKey, wsValue);
			FN->AddNode(PackArr[i]);
		}
		for (unsigned int i2 = 0; i2 < GetFileCount(); i2++)
		{
			LJFP_FileInfo* pFI = GetFileInfo(i2);
			LJFP_Node* pNode = new LJFP_Node(GetStringUtil().UI2WS(GetFileIndexInPack(i2, pFI->m_PackIndex)));
			wsKey = L"S"; wsValue = GetStringUtil().UI2WS(pFI->m_Size);
			pNode->AddAttr(wsKey, wsValue);
			wsKey = L"C32"; wsValue = GetStringUtil().UI2WS(pFI->m_CRC32);
			pNode->AddAttr(wsKey, wsValue);
			//wsKey = L"PFN"; wsValue = pFI->m_PathFileName;
			//pNode->AddAttr(wsKey, wsValue);
			wsKey = L"PFNC32"; wsValue = GetStringUtil().UI2WS(pFI->m_PathFileNameCRC32);
			pNode->AddAttr(wsKey, wsValue);
			wsKey = L"CPT"; wsValue = GetStringUtil().UI2WS(pFI->m_CompressType);
			pNode->AddAttr(wsKey, wsValue);
			wsKey = L"CDT"; wsValue = GetStringUtil().UI2WS(pFI->m_CodeType);
			pNode->AddAttr(wsKey, wsValue);
			if (pFI->m_CompressType > 0 || pFI->m_CodeType > 0)
			{
				wsKey = L"SO"; wsValue = GetStringUtil().UI2WS(pFI->m_SizeOriginal);
				pNode->AddAttr(wsKey, wsValue);
				wsKey = L"C32O"; wsValue = GetStringUtil().UI2WS(pFI->m_CRC32Original);
				pNode->AddAttr(wsKey, wsValue);
			}
			if (pFI->m_PackIndex != 0)//第一个包全是散文件
			{
				wsKey = L"P"; wsValue = GetStringUtil().UI2WS(pFI->m_Pos);
				pNode->AddAttr(wsKey, wsValue);
			}
			PackArr[pFI->m_PackIndex]->AddNode(pNode);
		}
		return 0;
	}
};

#endif //LJFP_FileInfo_H
