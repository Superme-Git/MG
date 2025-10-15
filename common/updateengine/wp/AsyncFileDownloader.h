#include<iostream>

class AsyncFileDownloader
{
	public:
	static void InitUrlAndDestdir(const std::wstring& url, const std::wstring& destdir);
	static void DownloadOneFileAsyn(const std::wstring& filename);
	static void StartDownload();
	static void Clear();
};
