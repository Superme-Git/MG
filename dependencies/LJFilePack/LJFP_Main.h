#include "LJFP_Main_Helper.h"
#include <iostream>
int main(int argc, char* argv[])
{
 	//LJFP_ZipFile ZF(9999, crc32, mz_compress2, mz_uncompress, SMS4Ex, DeSMS4Ex, "locojoy123456789");
	//ZF.ZipFile(L"TestRoot.txt", L"TestRoot.ljzip");
	//ZF.UnZipFile(L"TestRoot.ljzip", L"TestRoot2.txt");

	//LJFP_ZipFile ZF(crc32, mz_compress2, mz_uncompress, SMS4Ex, DeSMS4Ex, "locojoy123456789");
	//ZF.ZipFile(L"ZD.doc", L"ZD.ljzip");
	//ZF.UnZipFile(L"ZD.ljzip", L"ZD2.doc");

	//LJFP_NodeEx* pNodeEx = NULL;
	//pNodeEx->LoadFromXMLFile(L"Option.xml", pNodeEx);
	//pNodeEx->SaveToFile(L"Option.ljnd", pNodeEx);
	//delete pNodeEx;
	//LJFP_NodeEx* pNodeEx2 = NULL;
	//pNodeEx2->LoadFromFile(L"Option.ljnd", (LJFP_Node*&)pNodeEx2);
	//pNodeEx2->SaveToXMLFile(L"Option2.xml", (LJFP_NodeEx*)pNodeEx2);
	//delete pNodeEx2;
	//GetFileUtil().RemoveDir(L"aaa");
	//setlocale(LC_ALL, "chs");
	//GetFileUtil().CreateDirEx(L"/aaa/aa/a");

	setlocale(LC_ALL, "chs");

	if (argc > 1)
	{
		std::string sParam;
		std::string sParamValue;
		std::wstring wsParamV;
		for (int i = 1; i < argc; i++)
		{
			sParam = argv[i];
			if (sParam == "getversionnum")
			{
				std::wstring wsInput = L"1";
				std::wstring wsVersion;
				unsigned int uiVersion = 0;
				LJFP_Version V;
				while (wsInput != L"0")
				{
					std::wcout << L"������汾�Ÿ�ʽ:255.4095.4095, ����0�˳�" << std::endl;
					std::wcin >> wsVersion;
					uiVersion = V.VersionCaption2Version(wsVersion);
					if (uiVersion > 0)
					{
						std::wcout << uiVersion << std::endl;
					}
					if (wsVersion == L"0")
					{
						break;
					}
				}
				return 0;
			}
			else if (sParam == "getversioncaption")
			{
				std::wstring wsInput = L"1";
				std::wstring wsVersion;
				unsigned int uiVersion = 1;
				LJFP_Version V;
				while (uiVersion != 0)
				{
					std::wcout << L"������汾������:4294967295, ����0�˳�" << std::endl;
					std::wcin >> wsVersion;
					bool bIsAllNumber = true;
					for (int i = 0; i < wsVersion.length(); i++)
					{
						std::wstring wsCurNum = wsVersion.substr(i, 1);
						if (GetStringUtil().WS2I(wsCurNum) < 0 || GetStringUtil().WS2I(wsCurNum) > 9)
						{
							bIsAllNumber = false;
							break;
						}
					}
					if (bIsAllNumber == false)
					{
						continue;
					}
					uiVersion = GetStringUtil().WS2UI(wsVersion);
					if (uiVersion == 0)
					{
						break;
					}
					wsVersion = V.Version2VersionCaption(uiVersion);
					std::wcout << wsVersion << std::endl;
					if (wsVersion == L"0.0.0" || wsVersion == L"")
					{
						break;
					}
				}
				return 0;
			}
			else if (sParam == "getstrcrc32")
			{
				std::wstring wsInput = L"1";
				std::string sInput = "1";
				unsigned int uiCRC32 = 0;
				while (wsInput != L"0")
				{
					std::wcout << L"�������ַ���, ����0�˳�" << std::endl;
					std::wcin >> wsInput;
					sInput = GetStringUtil().WS2S(wsInput);
					uiCRC32 = crc32(0, (unsigned char*)sInput.c_str(), sInput.size() * sizeof(char));
					if (uiCRC32 > 0)
					{
						std::wcout << uiCRC32 << std::endl;
					}
					if (wsInput == L"0")
					{
						break;
					}
				}
				return 0;
			}
			else if (sParam.find("verljvi2xml:") == 0)
			{
				wprintf(L"���ڵ���XML��ʽ�汾�ļ�,���Ժ�...\n");
				sParamValue = sParam.substr(strlen("verljvi2xml:"));
				std::wstring strVersionInfoName = GetStringUtil().S2WS(sParamValue);
				std::wstring strVersionInfoNameXML = strVersionInfoName + L".xml";
				Ver_LJVI2XML(strVersionInfoName, strVersionInfoNameXML);
				return 0;
			}
			else if (sParam.find("verxml2ljvi:") == 0)
			{
				wprintf(L"���ڵ���XML��ʽ�汾�ļ�,���Ժ�...\n");
				sParamValue = sParam.substr(strlen("verxml2ljvi:"));
				std::wstring strVersionInfoNameXML = GetStringUtil().S2WS(sParamValue);
				std::wstring  strVersionInfoName = strVersionInfoNameXML + L".ljvi";
				Ver_XML2LJVI(strVersionInfoNameXML, strVersionInfoName);
				return 0;
			}
			else if (sParam.find("ljpi2xml:") == 0)
			{
				wprintf(L"���ڵ���XML��ʽ�ļ��б�,���Ժ�...\n");
				sParamValue = sParam.substr(strlen("ljpi2xml:"));
				std::wstring strPackInfoName = GetStringUtil().S2WS(sParamValue);
				std::wstring strPackInfoNameXML = strPackInfoName + L".xml";
				LJPIToXML(strPackInfoName, strPackInfoNameXML);
				return 0;
			}
			else if (sParam.find("ljzip2xml:") == 0)
			{
				wprintf(L"���ڵ���XML��ʽ�ļ��б�,���Ժ�...\n");
				sParamValue = sParam.substr(strlen("ljzip2xml:"));
				std::wstring strPackInfoNameZip = GetStringUtil().S2WS(sParamValue);
				std::wstring strPackInfoNameUnZip = strPackInfoNameZip + L".ljpi";
				std::wstring strPackInfoNameXML = strPackInfoNameUnZip + L".xml";
				LJZIPToLJPI(strPackInfoNameZip, strPackInfoNameUnZip);
				LJPIToXML(strPackInfoNameUnZip, strPackInfoNameXML);
				return 0;
			}
			else if (sParam.find("decode:") == 0)
			{
				wprintf(L"���ڽ����ļ�,���Ժ�...\n");
				sParamValue = sParam.substr(strlen("decode:"));
				std::wstring strSrcFileName = GetStringUtil().S2WS(sParamValue);
				std::wstring strDstFileName = strSrcFileName + L".decode";
				ExportPackFile(strSrcFileName, strDstFileName, true, false);
				return 0;
			}
			else if (sParam.find("unzip:") == 0)
			{
				wprintf(L"���ڽ�ѹ�ļ�,���Ժ�...\n");
				sParamValue = sParam.substr(strlen("unzip:"));
				std::wstring strSrcFileName = GetStringUtil().S2WS(sParamValue);
				std::wstring strDstFileName = strSrcFileName + L".unzip";
				ExportPackFile(strSrcFileName, strDstFileName, false, true);
				return 0;
			}
			else if (sParam.find("decodeunzip:") == 0)
			{
				wprintf(L"���ڽ��ܲ���ѹ�ļ�,���Ժ�...\n");
				sParamValue = sParam.substr(strlen("decodeunzip:"));
				std::wstring strSrcFileName = GetStringUtil().S2WS(sParamValue);
				std::wstring strDstFileName = strSrcFileName + L".decodeunzip";
				ExportPackFile(strSrcFileName, strDstFileName, true, true);
				return 0;
			}
			else if (sParam.find("unpack:") == 0)
			{
				wprintf(L"���ڽ����ļ��б�,���Ժ�...\n");
				sParamValue = sParam.substr(strlen("unpack:"));
				std::wstring strPackInfoName = GetStringUtil().S2WS(sParamValue);
				if (sParam.substr(sParam.size() - 5, 5).compare(".ljpi") == 0)
				{
				}
				else if (sParam.substr(sParam.size() - 6, 6).compare(".ljzip") == 0)
				{
					std::wstring strPackInfoNameZip = strPackInfoName;
					std::wstring strPackInfoNameUnZip = strPackInfoName + L".unzip";
					LJZIPToLJPI(strPackInfoNameZip, strPackInfoNameUnZip);
					strPackInfoName = strPackInfoNameUnZip;
				}
				else
				{
					wprintf(L"�ļ����Ͳ���ȷ,��ѡ��.ljpi��.ljzip���͵��ļ�\n");
					return 0;
				}
				wprintf(L"�����ͷ��ļ�,���Ժ�...\n");
				UnZipPack(strPackInfoName);
				system("pause");
				return 0;
			}
			else if (sParam.find("makeupdatepack:") == 0)
			{
				sParamValue = sParam.substr(strlen("makeupdatepack:"));
				std::wstring wsParamValue = GetStringUtil().S2WS(sParamValue);
				std::wstring wsPackFileNameBase;
				std::wstring wsPackFileNameNew;
				std::wstring wsPackDirName;
				std::vector<std::wstring> ResultStr;
				GetStringUtil().SplitStrW(wsParamValue, L"|", ResultStr);
				if (ResultStr.size() == 3)
				{
					wsPackFileNameBase = ResultStr[0];
					wsPackFileNameNew = ResultStr[1];
					wsPackDirName = ResultStr[2];
				}
				else
				{
					wprintf(L"��������\n");
					return -1;
				}
				int iResult = MakeUpdatePack(wsPackFileNameBase, wsPackFileNameNew, wsPackDirName);
				if (iResult == 0)
				{
					wprintf(L"������°����ɳɹ�\n");
				}
				else
				{
					wprintf(L"������°�����ʧ��\n");
				}
				system("pause");
				return iResult;
			}
			else if (sParam.find("makeupdatepackall:") == 0)
			{
				sParamValue = sParam.substr(strlen("makeupdatepackall:"));
				std::wstring wsParamValue = GetStringUtil().S2WS(sParamValue);
				std::wstring wsPackPathNameListFileName;
				wsPackPathNameListFileName = wsParamValue;
				int iResult = MakeUpdatePackAll(wsPackPathNameListFileName);
				if (iResult == 0)
				{
					wprintf(L"������°����ɳɹ�\n");
				}
				else
				{
					wprintf(L"������°�����ʧ��\n");
				}
				system("pause");
				return iResult;
			}
			else if (sParam.find("version:") == 0)
			{
				sParamValue = sParam.substr(strlen("version:"));
				OptionIndexVersion = GetStringUtil().S2WS(sParamValue);
			}
			else if (sParam.find("update:") == 0)
			{
				sParamValue = sParam.substr(strlen("update:"));
				OptionIndexUpdate = GetStringUtil().S2WS(sParamValue);
			}
			else if (sParam.find("channel:") == 0)
			{
				sParamValue = sParam.substr(strlen("channel:"));
				OptionIndexChannel = GetStringUtil().S2WS(sParamValue);
			}
			else if (sParam.find("extend:") == 0)
			{
				sParamValue = sParam.substr(strlen("extend:"));
				OptionIndexExtend = GetStringUtil().S2WS(sParamValue);
			}
			else if (sParam.find("io:") == 0)
			{
				sParamValue = sParam.substr(strlen("io:"));
				OptionIndexIO = GetStringUtil().S2WS(sParamValue);
			}
			else if (sParam.find("filter:") == 0)
			{
				sParamValue = sParam.substr(strlen("filter:"));
				OptionIndexFilter = GetStringUtil().S2WS(sParamValue);
			}
			else if (sParam.find("pack:") == 0)
			{
				sParamValue = sParam.substr(strlen("pack:"));
				OptionIndexPack = GetStringUtil().S2WS(sParamValue);
			}
			else if (sParam.find("compress:") == 0)
			{
				sParamValue = sParam.substr(strlen("compress:"));
				OptionIndexCompress = GetStringUtil().S2WS(sParamValue);
			}
			else if (sParam.find("code:") == 0)
			{
				sParamValue = sParam.substr(strlen("code:"));
				OptionIndexCode = GetStringUtil().S2WS(sParamValue);
			}
			else if (sParam == "nopause")
			{
				bPause = false;
			}
			else if (sParam == "?")
			{
				ShowParamHint(bPause);
				return 0;
			}
		}
	}
	if (CheckOption() != 0)
	{
		return 0;
	}
	int iResult = 0;
	std::wstring wsFindPath = GetOption().GetFindPath(OptionIndexIO);
	wprintf((L"����ɨ��Ŀ¼-" + wsFindPath + L"\n").c_str());
	GetFind()->m_FindFunc = FindOneFile;
	GetFind()->FindFiles(wsFindPath, L"", L"");//F:/Work/MT_G/dependencies/LJFilePack/
	GetFileList()->SetPackMaxSize(GetOption().GetPackMaxSize(OptionIndexPack));//52428800 = 1024 * 1024 * 50
	wprintf((L"������������\n"));
	std::wstring wsOutputPath = GetOption().GetOutputPath(OptionIndexIO);
	//wsOutputPath = wsOutputPath + GetOption().GetVersionCaptionBase() + L"/" + GetOption().GetVersionCaption() + L"/";
	iResult = GetFileList()->ExportRes(wsOutputPath, GetOption().GetOutputType(OptionIndexIO), bCompress, bCode);
	if (iResult != 0)
	{
		if (bPause)
		{
			system("pause");
		}
		return 0;
	}
	ExportVersion(wsOutputPath);
	ReleaseFind();
	ReleaseFileList();
	GetOption().ReleaseOption();
	if (bPause)
	{
		system("pause");
	}
	return 0;
}