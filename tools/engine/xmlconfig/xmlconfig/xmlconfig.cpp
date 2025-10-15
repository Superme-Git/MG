#include "../include/xmlconfig4beans.h"
#include "../include/xmlconfig.h"
#include "../../pfs/nativefile.h"
#include <sstream>

namespace XMLCONFIG {
	namespace helper {

		CConfigManager* g_manager = NULL;

	} // namespace helper {

	void InitializeTableInstances( CConfigManager& manager);
	void UninitializeTableInstances();

	int Initialize( const std::wstring& xmlpath, const std::wstring& cachepath)
	{
		if( helper::g_manager)
			return -1;
		helper::g_manager = new CConfigManager;
		helper::g_manager->SetXMLFilePath( xmlpath);
		helper::g_manager->SetCachePath( cachepath);
		InitializeTableInstances( *helper::g_manager);
		return 0;
	}
	void Uninitialize()
	{
		UninitializeTableInstances();
		delete helper::g_manager;
		helper::g_manager = NULL;
	}

	void CConfigManager::SetCachePath( const std::wstring& path)
	{
		m_cachepath = path;
	}

	PFS::CBaseFile* CConfigManager::TryGetCacheFile( const std::wstring& classname)
	{
		if( m_cachepath.empty())
			return NULL;

		static int index = 0;

		PFSX::CNativeFile* file = new PFSX::CNativeFile();
		while( true)
		{
			std::wstringstream	wss;
			wss << m_cachepath << L"/cachefile_" << index ++ << L".bin";

			if( file->Open( wss.str(), PFS::FM_CREAT | PFS::FM_TRUNC, PFS::FA_RDWR))
				return file;
		}
		return NULL;
	}

} // namespace XMLCONFIG {
