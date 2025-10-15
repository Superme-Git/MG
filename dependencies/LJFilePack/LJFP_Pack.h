#ifndef LJFP_Pack_H
#define LJFP_Pack_H

#include "LJFP_Var.h"
#include "LJFP_FileUtil.h"
#include "LJFP_XML.h"
#include "LJFP_FileInfo.h"
#include "LJFP_ZipFile.h"
#include "LJFP_SMS4.h"
#include "LJFP_MiniZ.h"

typedef void(*Pack_Func)(std::wstring strRootPathName, std::wstring strParentPathName, std::wstring strFileName, unsigned int FileIndex, unsigned int FileCount, unsigned int PackIndex);
Pack_Func g_PackFunc;
void PackOneFile(std::wstring strRootPathName, std::wstring strParentPathName, std::wstring strFileName, unsigned int FileIndex, unsigned int FileCount, unsigned int PackIndex)
{
	wprintf((L"Pack:" + GetStringUtil().UI2WS(PackIndex + 1) + L"-File:" + GetStringUtil().UI2WS(FileIndex + 1) + L"/" + GetStringUtil().UI2WS(FileCount) + L"-" +
		(strRootPathName + strParentPathName + strFileName)).c_str());
	wprintf(L"\n");
}

typedef void(*Log_Func)(std::wstring strText, unsigned int uiColor);
Log_Func g_LogFunc;
void Log(std::wstring strText, unsigned int uiColor)
{
	//0 = 黑色
	//1 = 蓝色
	//2 = 绿色
	//3 = 湖蓝色
	//4 = 红色
	//5 = 紫色
	//6 = 黄色
	//7 = 白色
	//8 = 灰色
	//9 = 淡蓝色
	//A = 淡绿色
	//B = 淡浅绿色
	//C = 淡红色
	//D = 淡紫色
	//E = 淡黄色
	//F = 亮白色
	//两位,前面背景,后面文字://system("color 04");
	std::string BackColor = "0";
	std::string TextColor = "F";
	if (uiColor >= 10)
	{
		if (uiColor > 15)
		{
			uiColor = 15;
		}
		TextColor = (char)(65 + uiColor - 10);
	}
	system(("color " + BackColor + TextColor).c_str());
	wprintf(strText.c_str());
	wprintf(L"\n");
}


class LJFP_FileList;
LJFP_FileList* GetFileList();

class LJFP_File
{
public:
	unsigned int m_Pack;
	unsigned int m_Pos;//Pos in Pack, Realtime

	unsigned int m_Size;
	unsigned int m_CRC32;
	unsigned char* m_Data;

	unsigned int m_CompressType;
	unsigned int m_CodeType;

	unsigned int m_SizeOriginal;
	unsigned int m_CRC32Original;
	unsigned char* m_DataOriginal;

	unsigned int m_SizeCompress;
	unsigned int m_CRC32Compress;
	unsigned char* m_DataCompress;

	unsigned int m_SizeCode;
	unsigned int m_CRC32Code;
	unsigned char* m_DataCode;

	std::wstring m_RootPathName;
	std::wstring m_PathName;
	std::wstring m_Name;

	unsigned int m_PathFileNameCRC32;

	LJFP_File()
	{
		Clear();
	}
	~LJFP_File()
	{
		ReleaseData();
		Clear();
	}
	int LoadData()
	{
		ReleaseData();
		std::ifstream FS((m_RootPathName + m_PathName + m_Name).c_str(), std::ios_base::binary);
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
		m_CRC32Original = crc32(0, m_DataOriginal, m_SizeOriginal);
		m_Data = m_DataOriginal;
		m_Size = m_SizeOriginal;
		m_CRC32 = m_CRC32Original;
		//std::wstring PF = m_PathName + m_Name;
		//m_PathFileNameCRC32 = crc32(0, (unsigned char*)PF.c_str(), PF.size() * 2);
		return 0;
	}
	int CompressData(bool bCompress)
	{
		if (m_CompressType == 0)
		{
			return 0;
		}
		if (bCompress == false)
		{
			return 0;
		}
		unsigned int NewSize = m_Size + 256;
		m_DataCompress = new unsigned char[NewSize];
		int iResult = mz_compress2(m_DataCompress, &NewSize, m_Data, m_Size, 9);
		m_SizeCompress = NewSize;
		if (iResult != 0)
		{
			return iResult;
		}
		m_CRC32Compress = crc32(0, m_DataCompress, m_SizeCompress);
		m_Data = m_DataCompress;
		m_Size = m_SizeCompress;
		m_CRC32 = m_CRC32Compress;
		return 0;
	}
	int CodeData(bool bCode)
	{
		if (m_CodeType == 0)
		{
			return 0;
		}
		if (bCode == false)
		{
			return 0;
		}
		m_SizeCode = m_Size;
		m_DataCode = new unsigned char[m_SizeCode];
		SMS4Ex(m_Data, m_DataCode, m_SizeCode, "locojoy123456789");
		m_CRC32Code = crc32(0, m_DataCode, m_SizeCode);
		m_Data = m_DataCode;
		m_Size = m_SizeCode;
		m_CRC32 = m_CRC32Code;
		return 0;
	}
	int SaveData(std::wstring strRootPathName)
	{
		std::ofstream FS((strRootPathName + GetStringUtil().UI2WS(m_PathFileNameCRC32)).c_str(), std::ios_base::binary);//, std::ios_base::binary | std::ios_base::out
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
		if (m_DataCode)
		{
			delete[] m_DataCode;
			m_DataCode = NULL;
		}
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
		m_Pack = 0;
		m_Pos = 0;

		m_Size = 0;
		m_CRC32 = 0;
		m_Data = NULL;

		m_CompressType = 0;
		m_CodeType = 0;

		m_SizeOriginal = 0;
		m_CRC32Original = 0;
		m_DataOriginal = NULL;

		m_SizeCompress = 0;
		m_CRC32Compress = 0;
		m_DataCompress = NULL;

		m_SizeCode = 0;
		m_CRC32Code = 0;
		m_DataCode = NULL;

		m_RootPathName = L"";
		m_PathName = L"";
		m_Name = L"";

		m_PathFileNameCRC32 = 0;
		return 0;
	}
};
class LJFP_Pack
{
public:
	unsigned int m_PackMaxSize;
	std::vector<LJFP_File*> m_FileArr;
	std::map<unsigned int, std::vector<LJFP_File*>> m_FileMap;
	std::map<unsigned int, std::vector<LJFP_File*>> m_SameFileMap;
	LJFP_Pack()
	{
		m_PackMaxSize = 0;
	}
	~LJFP_Pack()
	{
		for (unsigned int i = 0; i < m_FileArr.size(); i++)
		{
			delete m_FileArr[i];
		}
		m_FileArr.clear();
	}
	int AddFile(LJFP_File* pFile)
	{
		m_FileArr.push_back(pFile);//push_back肯定慢,这里以后要优化
		m_FileMap[pFile->m_PathFileNameCRC32].push_back(pFile);
		if (m_FileMap[pFile->m_PathFileNameCRC32].size() > 1)
		{
			m_SameFileMap[pFile->m_PathFileNameCRC32].clear();
			for (unsigned int i = 0; i < m_FileMap[pFile->m_PathFileNameCRC32].size(); i++)
			{
				m_SameFileMap[pFile->m_PathFileNameCRC32].push_back(m_FileMap[pFile->m_PathFileNameCRC32][i]);
			}
		}
		return 0;
	}
	unsigned int GetFileCount()
	{
		return m_FileArr.size();
	}
	int Clear()
	{
		m_FileArr.clear();
		return 0;
	}
	int CheckSameCRC32()
	{
		if (m_SameFileMap.size() > 0)
		{
			if (g_LogFunc)
			{
				wprintf(L"以下文件的文件名的CRC32重复了,请修改文件名之后重试\n");
				std::map<unsigned int, std::vector<LJFP_File*>>::iterator it = m_SameFileMap.begin();
				while (it != m_SameFileMap.end())
				{
					wprintf((L"CRC32:" + GetStringUtil().UI2WS(it->first)).c_str());
					wprintf(L"\n");
					for (unsigned int i2 = 0; i2 < m_SameFileMap[it->first].size(); i2++)
					{
						g_LogFunc(it->second[i2]->m_PathName + it->second[i2]->m_Name, 12);
					}
					it++;
				}
			}
			return -1;
		}
		return 0;
	}
	int ExportFileInfo(unsigned int PackIndex, std::wstring ExportRootPathName, LJFP_Node* pParentNode, bool bPack, bool bCompress, bool bCode)
	{
		if (GetFileCount() == 0 && PackIndex > 0)
		{
			return 0;
		}
		if (!bPack)
		{
			unsigned int PackSize = 0;
			LJFP_Node* pPackNode;
			pPackNode = pParentNode->AddNode(GetStringUtil().UI2WS(PackIndex), L"");
			for (unsigned int i = 0; i < GetFileCount(); i++)
			{
				m_FileArr[i]->LoadData();
				m_FileArr[i]->CompressData(bCompress);
				m_FileArr[i]->CodeData(bCode);
				PackSize += m_FileArr[i]->m_Size;
				m_FileArr[i]->SaveData(ExportRootPathName);
				m_FileArr[i]->ReleaseData();
				ExportFileInfoOne(pPackNode, m_FileArr[i], bPack, i);
				if (g_PackFunc)
				{
					g_PackFunc(ExportRootPathName, m_FileArr[i]->m_PathName, m_FileArr[i]->m_Name, i, GetFileCount(), PackIndex);
				}
			}
			pPackNode->AddAttr(L"FileCount", GetStringUtil().UI2WS(GetFileCount()));
			pPackNode->AddAttr(L"Size", GetStringUtil().UI2WS(PackSize));
			return 1;
		}
		else
		{
			unsigned int PackCount = 0;//0号包是散文件,打包文件从1开始
			//unsigned int FilePos = 0;
			unsigned int FileSize = 0;
			unsigned int FileCount = 0;
			LJFP_Node* pPackNode = NULL;
			std::ofstream FS;
			for (unsigned int i = 0; i < GetFileCount(); i++)
			{
				if (!FS.is_open())
				{
					PackCount += 1;
					FS.open(ExportRootPathName + GetStringUtil().UI2WS(PackCount) + L".ljfp", std::ios_base::binary);
					if (!FS.is_open())
					{
						return -1;
					}
					FS.seekp(0, std::ios_base::beg);
				}
				if (pPackNode == NULL)
				{
					pPackNode = pParentNode->AddNode(GetStringUtil().UI2WS(PackCount), L"");
				}
				m_FileArr[i]->LoadData();
				m_FileArr[i]->CompressData(bCompress);
				m_FileArr[i]->CodeData(bCode);
				m_FileArr[i]->m_Pos = FS.tellp();
				FS.write((char*)&m_FileArr[i]->m_Data[0], m_FileArr[i]->m_Size);
				m_FileArr[i]->ReleaseData();
				ExportFileInfoOne(pPackNode, m_FileArr[i], bPack, FileCount);
				FileSize += m_FileArr[i]->m_Size;
				FileCount += 1;
				if (FileSize >= m_PackMaxSize)
				{
					unsigned int PackSize = FS.tellp();
					pPackNode->AddAttr(L"FileCount", GetStringUtil().UI2WS(FileCount));
					pPackNode->AddAttr(L"Size", GetStringUtil().UI2WS(PackSize));
					pPackNode = NULL;
					FS.close();
					FileSize = 0;
					FileCount = 0;
				}
				if (g_PackFunc)
				{
					g_PackFunc(ExportRootPathName, m_FileArr[i]->m_PathName, m_FileArr[i]->m_Name, i, GetFileCount(), PackCount);
				}
			}
			if (FS.is_open())
			{
				unsigned int PackSize = FS.tellp();
				pPackNode->AddAttr(L"FileCount", GetStringUtil().UI2WS(FileCount));
				pPackNode->AddAttr(L"Size", GetStringUtil().UI2WS(PackSize));
				pPackNode = NULL;
				FS.close();
				FileSize = 0;
				FileCount = 0;
			}
			return PackCount;
		}
		return 0;
	}
	int ExportFileInfoOne(LJFP_Node* pPackNode, LJFP_File* pFile, bool bPack,unsigned int uiIndex)
	{
		LJFP_Node* pFileNode;
		pFileNode = pPackNode->AddNode(GetStringUtil().UI2WS(uiIndex), L"");
		if (bPack)
		{
			pFileNode->AddAttr(L"P", GetStringUtil().UI2WS(pFile->m_Pos));//Pos
		}
		//pFileNode->AddAttr(L"PFN", pFile->m_PathName + pFile->m_Name);//PathFileName
		pFileNode->AddAttr(L"PFNC32", GetStringUtil().UI2WS(pFile->m_PathFileNameCRC32));//PathFileNameCRC32
		pFileNode->AddAttr(L"S", GetStringUtil().UI2WS(pFile->m_Size));//Size
		pFileNode->AddAttr(L"C32", GetStringUtil().UI2WS(pFile->m_CRC32));//CRC32
		pFileNode->AddAttr(L"CPT", GetStringUtil().UI2WS(pFile->m_CompressType));//CompressType
		pFileNode->AddAttr(L"CDT", GetStringUtil().UI2WS(pFile->m_CodeType));//CodeType
		if (pFile->m_CompressType > 0 || pFile->m_CodeType > 0)
		{
			pFileNode->AddAttr(L"SO", GetStringUtil().UI2WS(pFile->m_SizeOriginal));//SizeOriginal
			pFileNode->AddAttr(L"C32O", GetStringUtil().UI2WS(pFile->m_CRC32Original));//CRC32Original
		}
		return 0;
	}
};
class LJFP_FileList
{
public:
	std::map<std::wstring, char> m_PathMapAll;
	std::map<std::wstring, char> m_PathMapPack;
	unsigned int m_PackMaxSize;

	LJFP_Pack* m_FilePack;
	LJFP_Pack* m_FilePackNo;
	LJFP_Pack* m_FilePackAll;

	LJFP_FileList()
	{
		m_PackMaxSize = 0;	
		m_FilePack = new LJFP_Pack();
		m_FilePackNo = new LJFP_Pack();
		m_FilePackAll = new LJFP_Pack();
	}
	~LJFP_FileList()
	{
		delete m_FilePack;
		delete m_FilePackNo;
		m_FilePackAll->Clear();
		delete m_FilePackAll;
	}
	void SetPackMaxSize(unsigned int PackMaxSize)
	{
		m_PackMaxSize = PackMaxSize;
		m_FilePack->m_PackMaxSize = PackMaxSize;
	}
	int AddFile(std::wstring strRootPathName, std::wstring strParentPathName, std::wstring strFileName, unsigned int Pack, unsigned int CompressType, unsigned int CodeType)
	{
		LJFP_File* pFile = new LJFP_File();
		pFile->m_RootPathName = strRootPathName;
		pFile->m_PathName = strParentPathName;
		pFile->m_Name = strFileName;
		pFile->m_CompressType = CompressType;
		pFile->m_CodeType = CodeType;
		pFile->m_Pack = Pack;
		std::wstring wstrPF = pFile->m_PathName + pFile->m_Name;
		std::string strPF = GetStringUtil().WS2S(wstrPF);
		pFile->m_PathFileNameCRC32 = crc32(0, (unsigned char*)strPF.c_str(), strPF.size() * sizeof(char));
		//wprintf(GetStringUtil().UI2WS(pFile->m_PathFileNameCRC32).c_str());
		//wprintf(L"--------->");
		//wprintf(PF.c_str());
		//wprintf(L"\n");
		//pFile->m_PathFileNameCRC32 = 123;
		if (Pack == 0)
		{
			m_FilePackNo->AddFile(pFile);
			if (strParentPathName != L"")
			{
				m_PathMapPack[strParentPathName] = 0;
			}
		}
		else
		{
			m_FilePack->AddFile(pFile);
		}
		if (strParentPathName != L"")
		{
			m_PathMapAll[strParentPathName] = 0;
		}
		m_FilePackAll->AddFile(pFile);
		return 0;
	}
	int MakeDir(std::wstring ExportRootPathName, bool bPack, std::map<std::wstring, char> PathMap)
	{
		std::map<std::wstring, char>::iterator it = PathMap.begin();
		GetFileUtil().CreateDirEx(ExportRootPathName);
		while (it != PathMap.end())
		{
			if (bPack)
			{
				if (it->second == 0)
				{
					if (!GetFileUtil().ExistDir(ExportRootPathName + it->first))
					{
						GetFileUtil().CreateDirEx(ExportRootPathName + it->first);
					}
				}
			}
			else
			{
				if (!GetFileUtil().ExistDir(ExportRootPathName + it->first))
				{
					GetFileUtil().CreateDirEx(ExportRootPathName + it->first);
				}
			}
			it++;
		}
		return 0;
	}
	int ExportRes(std::wstring ExportRootPathName, bool bPack, bool bCompress, bool bCode)
	{
		g_PackFunc = PackOneFile;
		g_LogFunc = Log;
		if (m_FilePackAll->CheckSameCRC32() != 0)
		{
			return -1;
		}
		GetFileUtil().CreateDirEx(ExportRootPathName);
		//if (bPack)
		//{
		//	MakeDir(ExportRootPathName, bPack, m_PathMapPack);
		//} 
		//else
		//{
		//	MakeDir(ExportRootPathName, bPack, m_PathMapAll);
		//}
		LJFP_NodeEx* pRootNode = new LJFP_NodeEx(L"Root", L"");
		unsigned int PackCount = 0;
		if (bPack)
		{
			PackCount = m_FilePackNo->ExportFileInfo(0, ExportRootPathName, pRootNode, false, bCompress, bCode);
			PackCount += m_FilePack->ExportFileInfo(1, ExportRootPathName, pRootNode, true, bCompress, bCode);
		} 
		else
		{
			PackCount = m_FilePackAll->ExportFileInfo(0, ExportRootPathName, pRootNode, false, bCompress, bCode);
		}
		pRootNode->AddAttr(L"PackCount", GetStringUtil().UI2WS(PackCount));
		//std::wstring strFileListXMLName = L"fl.xml";
		//pRootNode->SaveToXMLFile(ExportRootPathName + strFileListXMLName, pRootNode);
		//std::wstring strFileListNodeName = L"fl.ljnd";
		//pRootNode->SaveToFile(ExportRootPathName + strFileListNodeName, pRootNode);
		LJFP_PackInfo* pPI = new LJFP_PackInfo();
		pPI->m_CRC32Func = crc32;
		pPI->LoadFromNode(pRootNode);
		delete pRootNode;
		std::wstring strPackInfoName = L"fl.ljpi";
		pPI->SaveToFile(ExportRootPathName + strPackInfoName);
		delete pPI;
		std::wstring strPackInfoNameZip = L"fl.ljzip";
		LJFP_ZipFile ZF(9999, crc32, mz_compress2, mz_uncompress, SMS4Ex, DeSMS4Ex, "locojoy123456789");
		ZF.ZipFile(ExportRootPathName + strPackInfoName, ExportRootPathName + strPackInfoNameZip);
		return 0;
	}
};

LJFP_FileList* g_pFileList;
LJFP_FileList* GetFileList()
{
	if (!g_pFileList)
	{
		g_pFileList = new LJFP_FileList();
	}
	return g_pFileList;
}
void ReleaseFileList()
{
	if (g_pFileList)
	{
		delete g_pFileList;
		g_pFileList = NULL;
	}
}
#endif //LJFP_Pack_H