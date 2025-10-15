#include "FileDownloader.h"
#include "UpdateInterface.h"

bool FileDownloader::SynDownloadOneFile(const std::wstring& urlstring, const std::wstring& destfile ,bool notify )
{
	Platform::String^ src = ref new Platform::String(urlstring.c_str());
	Platform::String^ dst = ref new Platform::String(destfile.c_str());
	return PhoneDirect3DXamlAppComponent::UpdateInterface::callSynDownloadOneFile(src, dst);
}
