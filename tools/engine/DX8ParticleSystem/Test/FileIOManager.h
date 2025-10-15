#pragma once
#include "include\ifileio.h"

class FileIOManager :
	public Nuclear::IFileIO
{
public:
	FileIOManager(void);
	virtual ~FileIOManager(void);

	virtual bool ReadPFSFile(const std::wstring &filename, Nuclear::XBuffer &data);
};
