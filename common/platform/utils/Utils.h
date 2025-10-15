/***********************************************************************
filename: 	Utils.h
created:	2012-03-13
author:		eagle

purpose:	常用辅助函数
*************************************************************************/
#ifndef __UTILS_H__
#define __UTILS_H__

#include <string>
#include <fstream>
#include <iostream>

#include "StringUtil.h"
#include "../../ljfm/code/include/ljfm.h"
#if (defined WIN7_32)
#include "../../ljfm/code/include/ljfmbase.h"
#endif

namespace MHSD_UTILS
{
    const std::wstring& GetRootDir(); //lua
    const std::wstring& GetDocDir(); //lua
	const std::wstring& GetRunDir();

	bool ExistFile(const std::wstring& strFileName);
	bool ExistDir(const std::wstring& strDir);
	std::wstring CreateDir(const std::wstring& strDir);

	bool RemoveFile(const std::wstring& strFileName);
	bool RemoveDir(const std::wstring& strDir);

    void flurryEvent(std::wstring s, bool once = false);
}

inline std::streamsize loadFromStream(std::istream& input, char*& fileBuf)
{
	input.seekg(0, std::ios::end);
	std::streamsize ss = static_cast<std::streamsize>(input.tellg());

	if (ss <= 3) return 0;

	input.seekg(0, std::ios::beg);

	ss += 2;
	fileBuf = new char[ss];
	input.read(fileBuf, ss-2);
	fileBuf[ss - 2] = 0;
	fileBuf[ss - 1] = 0;

	return ss;
}

inline bool LoadFromFile(const char* filename, char*& fileBuf, std::streamsize& ss)
{
	if (filename == NULL)
		return false;

	std::fstream file;
	file.open(filename, std::ios_base::in | std::ios_base::binary);
	if (!file.is_open())
	{
		return false;
	}

	ss = loadFromStream(file, fileBuf);

	file.close();
	return ss > 3;
}

//从文件中获取到文件内容到fileBuf中（注意fileBuf是函数内部分配的空间，需要外部进行释放）
inline int GetBuffFromFile(std::wstring fileName, char*& fileBuf, std::streamsize& ss)
{
	int nError = 0;
	LJFM::LJFMF AFile;
	if (AFile.Open(fileName, LJFM::FM_EXCL, LJFM::FA_RDONLY))
	{
		unsigned int prefix = 0;
		std::size_t readsize = sizeof(unsigned int);
		readsize = AFile.Read(&prefix, readsize);
		if (readsize == LJFM::FILE_SIZE_ERROR)
		{
			return LJFM::LJFMOpen::GetLastError();
		}
		if (sizeof(unsigned int) != readsize)
		{
			return -1;
		}
		AFile.Seek(0, LJFM::FSM_SET);

		ss = AFile.GetSize() + 2;
		fileBuf = new char[ss + 1];
		if (AFile.ReadAll(fileBuf, ss))
		{
			fileBuf[ss - 2] = 0;
			fileBuf[ss - 1] = 0;
			fileBuf[ss] = 0;
			return 0;
		}

		delete[] fileBuf;
		return 1;
	}
	else
	{
		nError = LJFM::LJFMOpen::GetLastError();
	}
	return nError;
}


//#ifndef ANDROID
#include "IOS_Utils.h"
//#endif


#endif//__UTILS_H__
