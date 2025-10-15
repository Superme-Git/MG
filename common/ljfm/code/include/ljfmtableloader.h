#ifndef LJFMTABLELOADER_H
#define LJFMTABLELOADER_H

#include "platform/platform_types.h"
#include "ljfmfsmanager.h"
#include "ljfmfex.h"

namespace LJFM
{
	inline int ShortValueFromTable(LJFM::LJFMF* fp, int& iStatus)
    {
        short Result;
		fp->Read(&Result, sizeof(Result));
        return (int)Result;
    }
	inline int IntValueFromTable(LJFM::LJFMF* fp, int& iStatus)
	{
		int Result;
		fp->Read(&Result, sizeof(Result));
		return Result;
	}
	inline long long LongValueFromTable(LJFM::LJFMF* fp, int& iStatus)
	{
		long long Result; //ExcelParse2.exe write 'long' as 'long long'. by lg
		fp->Read(&Result, sizeof(Result));
		return Result;
	}
	inline double DoubleValueFromTable(LJFM::LJFMF* fp, int& iStatus)
	{
		double Result;
		fp->Read(&Result, sizeof(Result));
		return Result;
	}
	inline bool BoolValueFromTable(LJFM::LJFMF* fp, int& iStatus)
	{
		bool Result;
		fp->Read(&Result, sizeof(Result));
		return Result;
	}
	inline std::wstring StringValueFromTable(LJFM::LJFMF* fp, int& iStatus)
	{
		int stringLength;
		fp->Read(&stringLength, sizeof(int));
		char* buf = (char*)malloc(stringLength + 1);
		fp->Read(buf, stringLength);
		buf[stringLength] = 0;
		std::wstring Result = s2ws(buf);
		free(buf);
		return Result;
	}
	inline unsigned int UIntValueFromTable(LJFM::LJFMF* fp, int& iStatus)
	{
		uint32_t Result;
		fp->Read(&Result, sizeof(Result));
		return Result;
	}
}
#endif