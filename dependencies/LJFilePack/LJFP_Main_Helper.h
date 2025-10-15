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
	wprintf(L"打包时必须使用以下启动参数指定配置文件中各功能项的索引\n");
	wprintf(L"version:0 是指定使用的版本信息的选项索引\n");
	wprintf(L"update:0 是指定使用的更新信息的选项索引\n");
	wprintf(L"channel:0 是指定使用的渠道信息的选项索引\n");
	wprintf(L"extend:0 是指定使用的扩展信息的选项索引\n");
	wprintf(L"io:0 是指定文件在读写时的选项索引\n");
	wprintf(L"filter:0 是指定文件在搜索时的过滤的选项索引\n");
	wprintf(L"pack:0 是指定文件在打包时的选项索引\n");
	wprintf(L"compress:0 是指定文件在压缩时的选项索引\n");
	wprintf(L"code:0 是指定文件在加密时的选项索引\n");
	wprintf(L"nopause 是不显示退出时的等待提示(可选)\n");
	wprintf(L"例:version:0 update:0 channel:0 extend:0 io:0 filter:0 pack:0 compress:0 code:0\n");
	wprintf(L"--------------------\n");
	wprintf(L"以下各功能必须独立执行\n");
	wprintf(L"getversionnum 可以通过版本号0.0.1这样的格式返回整数类型值\n");
	wprintf(L"getversioncaption 可以通过版本号123456这样的整数类型返回0.0.1格式值\n");
	wprintf(L"verljvi2xml:ver.ljvi 可以将扩展名为.ljvi的文件转成扩展名为.xml的文件\n");
	wprintf(L"verxml2ljvi:ver.xml 可以将扩展名为.xml的文件转成扩展名为.ljvi的文件\n");
	wprintf(L"ljpi2xml:fl.ljpi 可以将扩展名为.ljpi的文件转成扩展名为.xml的文件\n");
	wprintf(L"ljzip2xml:fl.ljzip 可以将扩展名为.ljzip的文件转成扩展名为.xml的文件\n");
	wprintf(L"decode:xxx.xxx 可以将文件解密并生成扩展名为.decode的文件\n");
	wprintf(L"unzip:xxx.xxx 可以将文件解压并生成扩展名为.unzip的文件\n");
	wprintf(L"decodeunzip:xxx.xxx 可以将文件解密并解压并生成扩展名为.decodeunzip的文件\n");
	wprintf(L"unpack:fl.ljpi(fl.ljzip) 可以将整个资源目录解密并解压并生成原始完整资源目录及文件\n");
	wprintf(L"makeupdatepack:res0/|res1/|resNew/ 可以比较两版更新资源包之间的区别并将区别生成到新的目录中\n");
	wprintf(L"makeupdatepackall:xxx.xxx 可以比较多版更新资源包之间的区别并将区别生成到新的目录中\n");
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
		wprintf(L"列表文件数据不全,第一行为更新包输出目录,后面每一行都是每一版更新包的路径,至少要有两版才能够比较,路径最后要加斜杠( / )\n");
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
			wprintf((L"生成" + wsPackFileNameBase + L"-->" + wsPackFileNameNew + L"成功\n").c_str());
		}
		else
		{
			wprintf((L"生成" + wsPackFileNameBase + L"-->" + wsPackFileNameNew + L"失败\n").c_str());
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
		wprintf(L"配置文件因丢失或损坏已经重新生成,请修改配置后使用参数再次启动本程序\n");
		ShowParamHint(bPause);
		return -1;
	}
	if (OptionIndexVersion == L"-1" || OptionIndexUpdate == L"-1" || OptionIndexChannel == L"-1" ||
		OptionIndexIO == L"-1" || OptionIndexFilter == L"-1" || OptionIndexPack == L"-1" || OptionIndexCompress == L"-1" || OptionIndexCode == L"-1")
	{
		wprintf(L"启动参数内容不正确\n");
		ShowParamHint(bPause);
		return -1;
	}
	iResult = GetOption().InitOption(OptionIndexVersion, OptionIndexUpdate, OptionIndexChannel, OptionIndexExtend, OptionIndexFilter, OptionIndexPack, OptionIndexCompress, OptionIndexCode);
	if (iResult != 0)
	{
		system("color 0C");
		wprintf(L"配置文件内容不正确\n");
		if (iResult == -99){ wprintf(L"Version节点内容不正确\n"); }
		if (iResult == -98){ wprintf(L"Update节点内容不正确\n"); }
		if (iResult == -97){ wprintf(L"Channel节点内容不正确\n"); }
		if (iResult == -96){ wprintf(L"Extend节点内容不正确\n"); }
		if (iResult == -1){ wprintf(L"FilterDirName节点内容不正确\n"); }
		if (iResult == -2){ wprintf(L"FilterDirNameFull节点内容不正确\n"); }
		if (iResult == -3){ wprintf(L"FilterFileName节点内容不正确\n"); }
		if (iResult == -4){ wprintf(L"FilterFileNameFull节点内容不正确\n"); }
		if (iResult == -5){ wprintf(L"FilterFileType节点内容不正确\n"); }
		if (iResult == -6){ wprintf(L"UnPackFileName节点内容不正确\n"); }
		if (iResult == -7){ wprintf(L"UnPackFileNameFull节点内容不正确\n"); }
		if (iResult == -8){ wprintf(L"UnPackFileType节点内容不正确\n"); }
		if (iResult == -9){ wprintf(L"UnCompressFileName节点内容不正确\n"); }
		if (iResult == -10){ wprintf(L"UnCompressFileNameFull节点内容不正确\n"); }
		if (iResult == -11){ wprintf(L"UnCompressFileType节点内容不正确\n"); }
		if (iResult == -12){ wprintf(L"UnCodeFileName节点内容不正确\n"); }
		if (iResult == -13){ wprintf(L"UnCodeFileNameFull节点内容不正确\n"); }
		if (iResult == -14){ wprintf(L"UnCodeFileType节点内容不正确\n"); }
		if (bPause)
		{
			system("pause");
		}
		return -1;
	}
	return 0;
}