//  FileDownloader.h
//  FireClient


#ifndef __FireClient__FileDownloader__
#define __FireClient__FileDownloader__

#include <string>

class FileDownloader
{
public:
	static bool SynDownloadOneFile(const std::wstring& url, const std::wstring& destfile, bool notify = false);
};

#endif /* defined(__FireClient__FileDownloader__) */
