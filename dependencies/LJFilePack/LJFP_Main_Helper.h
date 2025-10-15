#include "LJFP_Var.h"
#include "LJFP_Pack.h"
#include "LJFP_Find.h"
#include "LJFP_FileUtil.h"
#include "LJFP_Version.h"
#include "LJFP_ZipFile.h"
#include "LJFP_SMS4.h"
#include "LJFP_CRC32.h"
//#include "LJFP_MiniZ.h"

bool bCompress = true;
bool bCode = true;
bool bPack = true;
bool bPause = true;

std::wstring OptionIndexVersion = L"-1";
std::wstring OptionIndexUpdate = L"-1";
std::wstring OptionIndexChannel = L"-1";
std::wstring OptionIndexExtend = L"-1";
std::wstring OptionIndexIO = L"-1";
std::wstring OptionIndexFilter = L"-1";
std::wstring OptionIndexPack = L"-1";
std::wstring OptionIndexCompress = L"-1";
std::wstring OptionIndexCode = L"-1";

void FindOneFile(std::wstring strRootPathName, std::wstring strParentPathName, std::wstring strFileName)
{
	GetFileList()->AddFile(strRootPathName, strParentPathName, strFileName,
		GetOption().IsPackFile(strParentPathName, strFileName),
		GetOption().IsCompressFile(strParentPathName, strFileName),
		GetOption().IsCodeFile(strParentPathName, strFileName));
}
int ExportVersion(std::wstring ExportRootPathName)
{
	LJFP_Version* pVersion = new LJFP_Version();
	pVersion->SetURLInfoArr(GetOption().GetUpdateURLInfoArr());
	pVersion->SetVersionCaption(GetOption().GetVersionCaption());
	pVersion->SetVersionCaptionBase(GetOption().GetVersionCaptionBase());
	pVersion->SetVersionCaptionMinimum(GetOption().GetVersionCaptionMinimum());
	pVersion->SetVersionDonotCheck(GetOption().GetVersionDonotCheck());
	pVersion->SetChannel(GetOption().GetChannel());
	pVersion->SetChannelCaption(GetOption().GetChannelCaption());
	pVersion->SetAppURL(GetOption().GetAppURL());
	pVersion->SetExtendMap(GetOption().GetExtendMap());
	//std::wstring strVersionNameXML = L"ver.xml";
	//pVersion->SaveToXMLFile(ExportRootPathName + strVersionNameXML);
	std::wstring strVersionName = L"ver.ljvi";
	pVersion->SaveToFile(ExportRootPathName + strVersionName);
	delete pVersion;
	return 0;
}
int ShowParamHint(bool bPause)
{
	system("color 0C");
	wprintf(L"���ʱ����ʹ��������������ָ�������ļ��и������������\n");
	wprintf(L"version:0 ��ָ��ʹ�õİ汾��Ϣ��ѡ������\n");
	wprintf(L"update:0 ��ָ��ʹ�õĸ�����Ϣ��ѡ������\n");
	wprintf(L"channel:0 ��ָ��ʹ�õ�������Ϣ��ѡ������\n");
	wprintf(L"extend:0 ��ָ��ʹ�õ���չ��Ϣ��ѡ������\n");
	wprintf(L"io:0 ��ָ���ļ��ڶ�дʱ��ѡ������\n");
	wprintf(L"filter:0 ��ָ���ļ�������ʱ�Ĺ��˵�ѡ������\n");
	wprintf(L"pack:0 ��ָ���ļ��ڴ��ʱ��ѡ������\n");
	wprintf(L"compress:0 ��ָ���ļ���ѹ��ʱ��ѡ������\n");
	wprintf(L"code:0 ��ָ���ļ��ڼ���ʱ��ѡ������\n");
	wprintf(L"nopause �ǲ���ʾ�˳�ʱ�ĵȴ���ʾ(��ѡ)\n");
	wprintf(L"��:version:0 update:0 channel:0 extend:0 io:0 filter:0 pack:0 compress:0 code:0\n");
	wprintf(L"--------------------\n");
	wprintf(L"���¸����ܱ������ִ��\n");
	wprintf(L"getversionnum ����ͨ���汾��0.0.1�����ĸ�ʽ������������ֵ\n");
	wprintf(L"getversioncaption ����ͨ���汾��123456�������������ͷ���0.0.1��ʽֵ\n");
	wprintf(L"verljvi2xml:ver.ljvi ���Խ���չ��Ϊ.ljvi���ļ�ת����չ��Ϊ.xml���ļ�\n");
	wprintf(L"verxml2ljvi:ver.xml ���Խ���չ��Ϊ.xml���ļ�ת����չ��Ϊ.ljvi���ļ�\n");
	wprintf(L"ljpi2xml:fl.ljpi ���Խ���չ��Ϊ.ljpi���ļ�ת����չ��Ϊ.xml���ļ�\n");
	wprintf(L"ljzip2xml:fl.ljzip ���Խ���չ��Ϊ.ljzip���ļ�ת����չ��Ϊ.xml���ļ�\n");
	wprintf(L"decode:xxx.xxx ���Խ��ļ����ܲ�������չ��Ϊ.decode���ļ�\n");
	wprintf(L"unzip:xxx.xxx ���Խ��ļ���ѹ��������չ��Ϊ.unzip���ļ�\n");
	wprintf(L"decodeunzip:xxx.xxx ���Խ��ļ����ܲ���ѹ��������չ��Ϊ.decodeunzip���ļ�\n");
	wprintf(L"unpack:fl.ljpi(fl.ljzip) ���Խ�������ԴĿ¼���ܲ���ѹ������ԭʼ������ԴĿ¼���ļ�\n");
	wprintf(L"makeupdatepack:res0/|res1/|resNew/ ���ԱȽ����������Դ��֮������𲢽��������ɵ��µ�Ŀ¼��\n");
	wprintf(L"makeupdatepackall:xxx.xxx ���ԱȽ϶�������Դ��֮������𲢽��������ɵ��µ�Ŀ¼��\n");
	if (bPause)
	{
		system("pause");
	}
	return 0;
}
int Ver_LJVI2XML(std::wstring strVersionInfoName, std::wstring strVersionInfoNameXML)
{
	LJFP_Version* pVersion = new LJFP_Version();
	pVersion->LoadFromFile(strVersionInfoName);
	pVersion->SaveToXMLFile(strVersionInfoNameXML);
	delete pVersion;
	return 0;
}
int Ver_XML2LJVI(std::wstring strVersionInfoNameXML, std::wstring strVersionInfoName)
{
	LJFP_Version* pVersion = new LJFP_Version();
	pVersion->LoadFromXMLFile(strVersionInfoNameXML);
	pVersion->SaveToFile(strVersionInfoName);
	delete pVersion;
	return 0;
}
int LJPIToXML(std::wstring strPackInfoName, std::wstring strPackInfoNameXML)
{
	LJFP_PackInfo* pPI = new LJFP_PackInfo();
	pPI->m_CRC32Func = crc32;
	pPI->LoadFromFile(strPackInfoName);
	LJFP_NodeEx* pRootNode;
	pPI->SaveToNode((LJFP_Node*&)pRootNode);
	pRootNode->BuildAttrList();
	pRootNode->SaveToXMLFile(strPackInfoNameXML, pRootNode);
	delete pRootNode;
	delete pPI;
	return 0;
}
int LJZIPToLJPI(std::wstring strPackInfoNameZip, std::wstring strPackInfoNameUnZip)
{
	LJFP_ZipFile ZF(9999, crc32, mz_compress2, mz_uncompress, SMS4Ex, DeSMS4Ex, "locojoy123456789");
	ZF.UnZipFile(strPackInfoNameZip, strPackInfoNameUnZip);
	return 0;
}
int ExportPackFile(std::wstring strSrcFileName, std::wstring strDstFileName,bool bDeCode, bool bUnZip)
{
	std::ifstream FSSrc(GetStringUtil().WS2S(strSrcFileName).c_str(), std::ios_base::binary);
	if (!FSSrc.is_open())
	{
		return -1;
	}
	std::ofstream FSDst(GetStringUtil().WS2S(strDstFileName).c_str(), std::ios_base::binary);
	if (!FSDst.is_open())
	{
		FSSrc.close();
		return -2;
	}
	unsigned int SizeAll = 0;
	FSSrc.seekg(0, std::ios_base::end);
	SizeAll = FSSrc.tellg();
	FSSrc.seekg(0, std::ios_base::beg);
	FSDst.seekp(0, std::ios_base::beg);
	unsigned char* DataSrc = new unsigned char[SizeAll];
	FSSrc.read((char*)&DataSrc[0], SizeAll);
	FSSrc.close();
	unsigned int SizeDst = SizeAll;
	unsigned char* DataDst = DataSrc;	
	if (bDeCode)
	{
		unsigned char* DataDecode = new unsigned char[SizeAll];
		DeSMS4Ex(DataSrc, DataDecode, SizeAll, "locojoy123456789");
		if (DataSrc){ delete[] DataSrc; }
		DataSrc = DataDecode;
		DataDst = DataSrc;
	}
	if (bUnZip)
	{
		SizeDst *= 2;
		unsigned char* DataUnZip = new unsigned char[SizeDst];
		int iResult = -1;
		while (iResult != 0)
		{
			iResult = mz_uncompress(DataUnZip, &SizeDst, DataSrc, SizeAll);
			if (iResult == 0)
			{
				break;
			}
			if (SizeDst / SizeAll > 16)
			{
				if (DataSrc){ delete[] DataSrc; }
				if (DataUnZip){ delete[] DataUnZip; }
				FSDst.close();
				GetFileUtil().RemoveFile(strDstFileName.c_str());
				return -3;
			}
			if (iResult == MZ_BUF_ERROR)
			{
				if (DataUnZip){ delete[] DataUnZip; }
				SizeDst *= 2;
				DataUnZip = new unsigned char[SizeDst];
			}
			else if (iResult != 0)
			{
				if (DataSrc){ delete[] DataSrc; }
				if (DataUnZip){ delete[] DataUnZip; }
				FSDst.close();
				GetFileUtil().RemoveFile(strDstFileName.c_str());
				return -3;
			}
		}
		if (DataSrc){ delete[] DataSrc; }
		DataSrc = DataUnZip;
		DataDst = DataSrc;
	}
	FSDst.write((char*)&DataDst[0], SizeDst);
	if (DataDst){ delete[] DataDst; }
	FSDst.close();
	return 0;
}
int UnZipPack(std::wstring strPackInfoName)
{
	struct FunctouTemp
	{
		int operator()(int V)
		{
			if (V == L'\\')
			{
				return L'/';
			}
			return V;
		}
	};
	std::transform(strPackInfoName.begin(), strPackInfoName.end(), strPackInfoName.begin(), FunctouTemp());
	LJFP_PackInfo* pPI = new LJFP_PackInfo();
	pPI->m_CRC32Func = crc32;
	pPI->LoadFromFile(strPackInfoName);
	std::wstring RootPathName = GetFileUtil().GetFilePath(strPackInfoName, L"");
	std::wstring PackDirName = RootPathName + L"FL";
	std::wstring PackPathName = GetFileUtil().DirToPath(PackDirName);
	GetFileUtil().CreateDirEx(PackDirName);
	for (unsigned int i = 0; i < pPI->GetFileCount(); i++)
	{
		LJFP_FileInfo* pFI = pPI->GetFileInfo(i);
		std::wstring CurPathName = GetFileUtil().GetFilePath(pFI->m_PathFileName, L"");
		std::wstring CurFileName = GetFileUtil().GetFileNameFull(pFI->m_PathFileName);
		if (CurPathName != L"")
		{
			GetFileUtil().CreateDirEx(PackPathName + CurPathName);
		}
		if (pFI->m_CompressType > 0 || pFI->m_CodeType > 0)
		{
			ExportPackFile(RootPathName + CurPathName + CurFileName, PackPathName + CurPathName + CurFileName, pFI->m_CodeType, pFI->m_CompressType);
		}
		else
		{
			GetFileUtil().CopyFileByFS(RootPathName + CurPathName + CurFileName, PackPathName + CurPathName + CurFileName);
		}
		wprintf((L"File:" + GetStringUtil().UI2WS(i + 1) + L"/" + GetStringUtil().UI2WS(pPI->GetFileCount()) + L"-" + PackPathName + CurPathName + CurFileName).c_str());
		wprintf(L"\n");
	}
	delete pPI;
	return 0;
}
int ParsePackInfoAddModDel(LJFP_PackInfo* pPIBase, LJFP_PackInfo* pPINew, LJFP_PackInfo*& pPIAdd, LJFP_PackInfo*& pPIMod, LJFP_PackInfo*& pPIDel)
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
int MakeUpdatePack(std::wstring wsPackPathNameBase, std::wstring wsPackPathNameNew, std::wstring wsPackPathNameResult)
{
	std::wstring wsPackFileNameBase;
	std::wstring wsPackFileNameNew;
	wsPackFileNameBase = wsPackPathNameBase + L"fl.ljpi";
	wsPackFileNameNew = wsPackPathNameNew + L"fl.ljpi";
	LJFP_PackInfo* pPIBase = NULL;
	LJFP_PackInfo* pPINew = NULL;
	LJFP_PackInfo* pPIAdd = NULL;
	LJFP_PackInfo* pPIMod = NULL;
	LJFP_PackInfo* pPIDel = NULL;
	int iResult = 0;
	pPIBase = new LJFP_PackInfo();
	pPIBase->m_CRC32Func = crc32;
	iResult = pPIBase->LoadFromFile(wsPackFileNameBase);
	if (iResult != 0)
	{
		wprintf((L"Base List Load Failed:" + wsPackFileNameBase + L"\n").c_str());
		delete pPIBase;
		return -1;
	}
	pPINew = new LJFP_PackInfo();
	pPINew->m_CRC32Func = crc32;
	iResult = pPINew->LoadFromFile(wsPackFileNameNew);
	if (iResult != 0)
	{
		wprintf((L"New List Load Failed:" + wsPackFileNameNew + L"\n").c_str());
		delete pPIBase;
		delete pPINew;
		return -1;
	}
	ParsePackInfoAddModDel(pPIBase, pPINew, pPIAdd, pPIMod, pPIDel);
	GetFileUtil().CreateDirEx(wsPackPathNameResult);
	std::wstring wsPackDirNameNew = GetFileUtil().GetFilePath(wsPackFileNameNew, L"");
	std::wstring wsCurFileNameOne;	
	int iSuccess = 0;
	int iFailed = 0;
	for (unsigned int i = 0; i < pPIAdd->GetFileCount(); i++)
	{
		wsCurFileNameOne = pPIAdd->GetFileInfo(i)->m_PathFileName;
		iResult = GetFileUtil().CopyFileByFS(wsPackDirNameNew + wsCurFileNameOne, wsPackPathNameResult + wsCurFileNameOne);
		if (iResult != 0)
		{
			iFailed += 1;
			wprintf((L"Copy Failed:" + wsCurFileNameOne + L"\n").c_str());
		}
		else
		{
			iSuccess += 1;
			wprintf((GetStringUtil().UI2WS(i + 1) + L":" + wsCurFileNameOne + L"\n").c_str());
		}
	}
	for (unsigned int i = 0; i < pPIMod->GetFileCount(); i++)
	{
		wsCurFileNameOne = pPIMod->GetFileInfo(i)->m_PathFileName;
		iResult = GetFileUtil().CopyFileByFS(wsPackDirNameNew + wsCurFileNameOne, wsPackPathNameResult + wsCurFileNameOne);
		if (iResult != 0)
		{
			iFailed += 1;
			wprintf((L"Copy Failed:" + wsCurFileNameOne + L"\n").c_str());
		}
		else
		{
			iSuccess += 1;
			wprintf((GetStringUtil().UI2WS(i + pPIAdd->GetFileCount() + 1) + L":" + wsCurFileNameOne + L"\n").c_str());
		}
	}
	for (unsigned int i = 0; i < pPIDel->GetFileCount(); i++)
	{
		wsCurFileNameOne = pPIDel->GetFileInfo(i)->m_PathFileName;
		GetFileUtil().RemoveFile(wsPackPathNameResult + wsCurFileNameOne);
	}
	delete pPIBase;
	delete pPINew;
	delete pPIAdd;
	delete pPIMod;
	delete pPIDel;
	wprintf(L"--------------------------------------------------\n");
	wprintf((L"Total File:" + GetStringUtil().UI2WS(iSuccess + iFailed) + L"\n").c_str());
	wprintf((L"Success File:" + GetStringUtil().UI2WS(iSuccess) + L"\n").c_str());
	wprintf((L"Failed File:" + GetStringUtil().UI2WS(iFailed) + L"\n").c_str());
	wprintf(L"--------------------------------------------------\n");
	if (iFailed != 0)
	{
		return iFailed;
	}
	iResult = GetFileUtil().CopyFileByFS(wsPackDirNameNew + L"ver.ljvi", wsPackPathNameResult + L"ver.ljvi");
	if (iResult != 0)
	{
		wprintf(L"Copy Failed:ver.ljvi\n");
		return -1;
	}
	else
	{
		wprintf(L"Copy Success:ver.ljvi\n");
	}
	iResult = GetFileUtil().CopyFileByFS(wsPackDirNameNew + L"fl.ljzip", wsPackPathNameResult + L"fl.ljzip");
	if (iResult != 0)
	{
		wprintf(L"Copy Failed:fl.ljzip\n");
		return -1;
	}
	else
	{
		wprintf(L"Copy Success:fl.ljzip\n");
	}
	wprintf(L"--------------------------------------------------\n");
	return 0;
}
int MakeUpdatePackAll(std::wstring wsPackPathNameListFileName)
{
	std::ifstream FSSrc(GetStringUtil().WS2S(wsPackPathNameListFileName).c_str(), std::ios_base::binary);
	if (!FSSrc.is_open())
	{
		return -1;
	}
	unsigned int SizeAll = 0;
	FSSrc.seekg(0, std::ios_base::end);
	SizeAll = FSSrc.tellg();
	FSSrc.seekg(0, std::ios_base::beg);
	std::string sDataAll;
	sDataAll.resize(SizeAll);
	FSSrc.read((char*)(sDataAll.c_str()), SizeAll);
	FSSrc.close();
	std::wstring wsDataAll = GetStringUtil().S2WS(sDataAll);
	std::vector<std::wstring> wsPackPathNameList;
	GetStringUtil().SplitStrW(wsDataAll, L"\r\n", wsPackPathNameList);
	int iPackCount = wsPackPathNameList.size();
	if (iPackCount < 3)
	{
		wprintf(L"�б��ļ����ݲ�ȫ,��һ��Ϊ���°����Ŀ¼,����ÿһ�ж���ÿһ����°���·��,����Ҫ��������ܹ��Ƚ�,·�����Ҫ��б��( / )\n");
		return -1;
	}
	std::wstring wsPackFileNameBase;
	std::wstring wsPackFileNameNew;
	std::wstring wsPackPathNameResult;
	wsPackPathNameResult = wsPackPathNameList[0];
	int iResult = 0;
	for (int i = 1; i < iPackCount - 1; i++)
	{
		wsPackFileNameBase = wsPackPathNameList[i];
		wsPackFileNameNew = wsPackPathNameList[i + 1];
		iResult = MakeUpdatePack(wsPackFileNameBase, wsPackFileNameNew, wsPackPathNameResult);
		if (iResult == 0)
		{
			wprintf((L"����" + wsPackFileNameBase + L"-->" + wsPackFileNameNew + L"�ɹ�\n").c_str());
		}
		else
		{
			wprintf((L"����" + wsPackFileNameBase + L"-->" + wsPackFileNameNew + L"ʧ��\n").c_str());
			return -1;
		}
	}
	return 0;
}
int CheckOption()
{
	int iResult = 0;
	iResult = GetOption().LoadOption(L"LJFilePackOption.xml");
	if (iResult != 0)
	{
		wprintf(L"�����ļ���ʧ�����Ѿ���������,���޸����ú�ʹ�ò����ٴ�����������\n");
		ShowParamHint(bPause);
		return -1;
	}
	if (OptionIndexVersion == L"-1" || OptionIndexUpdate == L"-1" || OptionIndexChannel == L"-1" ||
		OptionIndexIO == L"-1" || OptionIndexFilter == L"-1" || OptionIndexPack == L"-1" || OptionIndexCompress == L"-1" || OptionIndexCode == L"-1")
	{
		wprintf(L"�����������ݲ���ȷ\n");
		ShowParamHint(bPause);
		return -1;
	}
	iResult = GetOption().InitOption(OptionIndexVersion, OptionIndexUpdate, OptionIndexChannel, OptionIndexExtend, OptionIndexFilter, OptionIndexPack, OptionIndexCompress, OptionIndexCode);
	if (iResult != 0)
	{
		system("color 0C");
		wprintf(L"�����ļ����ݲ���ȷ\n");
		if (iResult == -99){ wprintf(L"Version�ڵ����ݲ���ȷ\n"); }
		if (iResult == -98){ wprintf(L"Update�ڵ����ݲ���ȷ\n"); }
		if (iResult == -97){ wprintf(L"Channel�ڵ����ݲ���ȷ\n"); }
		if (iResult == -96){ wprintf(L"Extend�ڵ����ݲ���ȷ\n"); }
		if (iResult == -1){ wprintf(L"FilterDirName�ڵ����ݲ���ȷ\n"); }
		if (iResult == -2){ wprintf(L"FilterDirNameFull�ڵ����ݲ���ȷ\n"); }
		if (iResult == -3){ wprintf(L"FilterFileName�ڵ����ݲ���ȷ\n"); }
		if (iResult == -4){ wprintf(L"FilterFileNameFull�ڵ����ݲ���ȷ\n"); }
		if (iResult == -5){ wprintf(L"FilterFileType�ڵ����ݲ���ȷ\n"); }
		if (iResult == -6){ wprintf(L"UnPackFileName�ڵ����ݲ���ȷ\n"); }
		if (iResult == -7){ wprintf(L"UnPackFileNameFull�ڵ����ݲ���ȷ\n"); }
		if (iResult == -8){ wprintf(L"UnPackFileType�ڵ����ݲ���ȷ\n"); }
		if (iResult == -9){ wprintf(L"UnCompressFileName�ڵ����ݲ���ȷ\n"); }
		if (iResult == -10){ wprintf(L"UnCompressFileNameFull�ڵ����ݲ���ȷ\n"); }
		if (iResult == -11){ wprintf(L"UnCompressFileType�ڵ����ݲ���ȷ\n"); }
		if (iResult == -12){ wprintf(L"UnCodeFileName�ڵ����ݲ���ȷ\n"); }
		if (iResult == -13){ wprintf(L"UnCodeFileNameFull�ڵ����ݲ���ȷ\n"); }
		if (iResult == -14){ wprintf(L"UnCodeFileType�ڵ����ݲ���ȷ\n"); }
		if (bPause)
		{
			system("pause");
		}
		return -1;
	}
	return 0;
}