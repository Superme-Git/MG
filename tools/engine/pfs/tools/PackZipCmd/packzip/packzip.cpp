// packzip.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "SaveManager.h"

static void Usage( _TCHAR* arg0 )
{
	std::wstringstream usage;
	usage << L"Command tool for PackZip" << std::endl;
	usage << L"Usage:" << std::endl;
	usage << L"    " << arg0 << " -c <config-file-path>" << std::endl;
	usage << L"Options:" << std::endl;
	usage << L"    <config-file-path>: the full path of the config file, which must be a xml file" << std::endl;
	usage << L"Examples:" << std::endl;
	usage << L"    " << arg0 << L" cfg.xml" << std::endl;
	usage << L"if a config file is specified, other arguments will be ignored" << std::endl;
	usage << L"Command parameters:" << std::endl;
	usage << L"    -i <inputfile> project file path" << std::endl;
	usage << L"    -o <outputfile> output file path" << std::endl;
	usage << L"    -r this argument denotes whether the package is a resource package" << std::endl;
	usage << L"    -s this argument denotes whether the project file needs to be saved" << std::endl;
	usage << L"    -t this argument denotes whether the operators need to be sorted" << std::endl;
	usage << L"    -v <version> specifies new version, such as <1.5.2>, \
			  if new version is equal to old version plus 1,set version to <+1>, \
			  if don't need to specify new version, don't set this argument" << std::endl;
	usage << L"    -l <luv> the same as version" << std::endl;
	usage << L"    -d <date> specifies new release-date, format is <YYYY-MM-DD>,such as <2011-09-16>, \
			  if use current date, set <NOW> or don't set this argument" << std::endl;
	wprintf(usage.str().c_str());
}

int _tmain(int argc, _TCHAR* argv[])
{
	setlocale(0,".936");
	TCHAR* cfgfile = NULL;
	wstring inputPath = L"";
	wstring outputPath = L"";
	bool bResource = false;
	bool bSave = false;
	bool bSort = false;
	wstring version = L"";
	wstring date = L"";
	wstring luv = L"";
	if(argv[1]!=NULL)
	{
		int i = 1;
		while(argv[i]!=NULL)
		{
			if(!_tcscmp(argv[i], L"-c"))
			{
				cfgfile = argv[++i];
				break;
			}
			else if(!_tcscmp(argv[i], L"-i"))
				inputPath = argv[++i];
			else if(!_tcscmp(argv[i], L"-o"))
				outputPath = argv[++i];
			else if(!_tcscmp(argv[i], L"-r"))
				bResource = true;
			else if(!_tcscmp(argv[i], L"-s"))
				bSave = true;
			else if(!_tcscmp(argv[i], L"-t"))
				bSort = true;
			else if(!_tcscmp(argv[i], L"-v"))
				version = argv[++i];
			else if(!_tcscmp(argv[i], L"-d"))
				date = argv[++i];
			else if(!_tcscmp(argv[i], L"-l"))
				luv = argv[++i];
			i++;
		}
	}
	else
	{
		Usage(argv[0]);
		return 1;
	}
	CSaveManager saveManager;
	saveManager.SetParam(inputPath, outputPath, bResource, bSave, bSort, version, date, luv);
	if(cfgfile != NULL)
	{
		if(!saveManager.ParseXmlCfg(cfgfile))
			return 1;
	}
	if(!saveManager.OpenProject())
	{
		std::wstringstream error;
		error << L"Open ProjFile error!" << std::endl;
		wprintf(error.str().c_str());
		return 1;
	}
	saveManager.SaveFile();
	saveManager.SaveProject();
	return 0;
}

