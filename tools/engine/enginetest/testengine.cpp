#include <vector>
#include <set>
#include <map>
#include "..\engine\common\xml.h"
#include "testapp.h"
#include <assert.h>

#include "..\engine\common\log.h"


bool LoadConfig(std::wstring cfg_file_name, Nuclear::EngineParameter &ep, TestApp &app)
{
	bool bVerticalSync = false;
	bool bSyncRead = false;
	XMLIO::CFileReader fr;
	int nError = fr.OpenNativeFile(cfg_file_name);
	if( XMLIO::EC_SUCCESS != nError)
		return false;

	XMLIO::CINode	root;
	if( ! fr.GetRootNode(root))
		return false;

	XMLIO::CNodeList nl;
	root.GetChildren(nl);

	std::wstring strVal;
	int iVal;

	for( XMLIO::CNodeList::iterator it = nl.begin(); it != nl.end(); ++ it)
	{
		XMLIO::CINode& node = *it;
		if( L"vertical_sync" == node.GetName() )
		{
			if ( (node.GetAttribute(L"enable", strVal) ) && strVal == L"true" )
				bVerticalSync = true;
		//	continue;
		}
		else if( L"fileio" == node.GetName() )
		{
			if ( (node.GetAttribute(L"sync_read", strVal) ) && strVal == L"true" )
				bSyncRead = true;
		//	continue;
		}
		else if( L"window" == node.GetName() )
		{
			if ( node.GetAttribute(L"width", strVal)  )
			{
				iVal = _wtoi(strVal.c_str());
				if( iVal >= 300 )
					ep.dmode.width = iVal;
			}
			if ( node.GetAttribute(L"height", strVal)  )
			{
				iVal = _wtoi(strVal.c_str());
				if( iVal >= 200 )
					ep.dmode.height = iVal;
			}
			ep.dmode.screenMode = static_cast<Nuclear::XPScreenMode>(node.GetAttributeInteger(L"mode", Nuclear::XPSM_WINDOW));
		//	continue;
		}
		else if( L"init_map" == node.GetName() )
		{
			if ( node.GetAttribute(L"mapname", strVal)  )
			{
				app.m_strInitMapName = strVal;
			}
			if ( node.GetAttribute(L"mazename", strVal)  )
			{
				app.m_strInitMazeName = strVal;
			}
		//	continue;
		}
		
	}
	fr.CloseFile();

	if( bVerticalSync )
		ep.dwRenderFlags |= Nuclear::XPCRF_VERTICALSYNC;
	else
		ep.dwRenderFlags &= ~Nuclear::XPCRF_VERTICALSYNC;

	ep.bAsyncRead = !bSyncRead;
	return true;
}


INT WINAPI WinMain( HINSTANCE hInst, HINSTANCE, LPSTR, INT )
{
			
	_CrtSetDbgFlag( _CRTDBG_ALLOC_MEM_DF | _CRTDBG_LEAK_CHECK_DF);
	TestApp tempApp;
	Nuclear::EngineParameter ep;
	ep.szClassName = L"TestEngineClass";
	ep.szWindowTitle = L"TestEngine";
	ep.bAsyncRead = true;
	ep.bApplictionInBuild = true;
	ep.pApp = &tempApp;
	/*ep.dmode.width = 1024;
	ep.dmode.height = 768;*/
	ep.dmode.width = 800;
	ep.dmode.height = 600;
//	ep.dmode.bitperpix = 16;
	//ep.bAsyncRead = false;

	LoadConfig(L"testcfg.xml", ep, tempApp);

	Nuclear::IEngine* engine = Nuclear::GetEngine();
	
	ep.nAppInitStepCount = 2;
	engine ->Run(ep);
	//engine ->Exit();
	return 0;
}