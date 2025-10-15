#pragma once
#include <vector>

enum
{
NAME = 0,
ORIGINALSIZE = 1,
COMPRESSSIZE = 2,
TYPE = 3,
OFFSET = 4,
MODIFYTIME = 5,
CRC32 = 6,
ZIPRADIO = 7,
};

class CFileCentralCache : public std::vector<DWORD_PTR>
{

public:

	void Sort( int type, bool bAsc);
};