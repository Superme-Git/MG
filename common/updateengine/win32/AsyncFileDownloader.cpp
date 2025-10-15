#include "AsyncFileDownloader.h"
#include "utils/StringUtil.h"
#include "GlobalFunction.h"
#include "UpdateInterface.h"

void AsyncFileDownloader::InitUrlAndDestdir(const std::wstring& url, const std::wstring& destdir)
{
	Platform::String^ urlStr = ref new Platform::String(url.c_str());
	Platform::String^ dstStr = ref new Platform::String(destdir.c_str());
	PhoneDirect3DXamlAppComponent::UpdateInterface::callInitUrlAndDestdir(urlStr, dstStr);
}

void AsyncFileDownloader::DownloadOneFileAsyn(const std::wstring& filename)
{
	Platform::String^ name = ref new Platform::String(filename.c_str());
	PhoneDirect3DXamlAppComponent::UpdateInterface::callDownloadOneFileAsyn(name);
}

void AsyncFileDownloader::Clear()
{
	PhoneDirect3DXamlAppComponent::UpdateInterface::callClear();
}


void AsyncFileDownloader::StartDownload()
{
	PhoneDirect3DXamlAppComponent::UpdateInterface::callStartDownload();
}

