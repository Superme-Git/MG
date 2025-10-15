#pragma once
namespace PFSX { struct PatchInfo; }

class PatchDownloader
{
public:
	PatchDownloader(PFSX::PatchInfo& patchInfo);
	~PatchDownloader();

	bool Download(HANDLE hBreakEvent, const std::wstring& url, const std::wstring& cacheDir);

private:
	bool HttpDownload(HANDLE hBreakEvent, const std::wstring& dir, const std::wstring& url);
	bool P2spDownload(HANDLE hBreakEvent, const std::wstring& dir);

private:
	bool m_httpFailed;
	bool m_p2spFailed;
	PFSX::PatchInfo& m_patchInfo;
};