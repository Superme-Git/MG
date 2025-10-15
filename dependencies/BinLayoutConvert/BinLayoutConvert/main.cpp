//////////////////////////////////////////////////////////////////////////
// File:		main.cpp
// Author:	杨春雷
// Date:		2016/03/04
//////////////////////////////////////////////////////////////////////////

#include <io.h>
#include <string>
#include "BinLayout/CEGUIXMLToBin.h"
#include "CEGUIString.h"
#include "CEGUIDefaultLogger.h"
#include "BinLayout/v1/CEGUIRegSerializers_v1.h"
#include <Windows.h>

void showHelp()
{
	printf("CEGUI layout 文件从 XML 格式转为二进制\n");
	printf("BinLayoutConvert filename\n");
	printf("                        directory\n");
	printf("filename 要转换的 layout 文件名\n");
	printf("directory 要转换的 layout 文件目录\n");
}

bool isAFilename(const char* sz)
{
	DWORD attr = GetFileAttributes(sz);
	if (INVALID_FILE_ATTRIBUTES == attr)
	{
		return false;
	}

	return (attr & FILE_ATTRIBUTE_DIRECTORY) == 0;
}

bool isADirectory(const char* sz)
{
	DWORD attr = GetFileAttributes(sz);
	if (INVALID_FILE_ATTRIBUTES == attr)
	{
		return false;
	}

	return (attr & FILE_ATTRIBUTE_DIRECTORY) != 0;
}

void searchFilesInDir(const std::string& dir, const std::string& filePattern, std::vector<std::string>& fileArr)
{
	std::string fileSpec = dir + filePattern;

	_finddata_t fd;
	intptr_t hFind = _findfirst(fileSpec.c_str(), &fd);
	while (hFind != -1)
	{
		if (0 != strcmp(fd.name, ".") && 0 != strcmp(fd.name, ".."))
		{
			if (fd.attrib & _A_SUBDIR)
			{
				searchFilesInDir(dir + fd.name + "/", filePattern, fileArr);
			}
			else
			{
				std::string filename = dir + fd.name;
				fileArr.push_back(filename);
			}
		}

		if (-1 == _findnext(hFind, &fd))
		{
			_findclose(hFind);
			hFind = -1;
		}
	}
}

int main(int argc, const char* argv[])
{
	CEGUI::DefaultLogger loger;
	CEGUI::BinLayout::g_RegSerializers_v1();

	if (argc >= 2)
	{
		if (isAFilename(argv[1]))
		{
			const char* srcFilename = argv[1];
			printf("convert %s\n", srcFilename);

			CEGUI::BinLayout::XMLToBin xmlToBin;
			xmlToBin.convert(srcFilename, srcFilename);
		}
		else if (isADirectory(argv[1]))
		{
			std::string rootDir = argv[1];
			char lastChar = rootDir[rootDir.size() - 1];
			if (lastChar != '/' && lastChar != '\\')
			{
				rootDir += "/";
			}

			std::vector<std::string> filenameArr;
			searchFilesInDir(rootDir, "*.layout", filenameArr);

			int fileCount = filenameArr.size();
			for (int i = 0; i < fileCount; ++i)
			{
				const std::string& srcFilename = filenameArr[i];
				printf("[%d/%d] convert %s\n", i, fileCount, srcFilename.c_str());

				CEGUI::BinLayout::XMLToBin xmlToBin;
				xmlToBin.convert(srcFilename, srcFilename);
			}
		}
		else
		{
			showHelp();
		}
	}
	else
	{
		showHelp();
	}

	return 0;
}