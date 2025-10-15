#include <windows.h>
#include <Iphlpapi.h>

#include <dxerr9.h>
#include <d3dx9core.h>

#include "infocollection.h"

namespace Defence
{

void FetchAdpaterDescription()
{
	IDirect3D9 *ifD3D = Direct3DCreate9(D3D_SDK_VERSION);
	if( ifD3D == NULL ) return;

	bool ret = false;
	for (UINT Adapter=0;Adapter<ifD3D->GetAdapterCount();Adapter++)
	{
		D3DADAPTER_IDENTIFIER9 identifier;
		HRESULT Res;
		Res = ifD3D->GetAdapterIdentifier(Adapter,0,&identifier);
		if (strstr(identifier.Description,"PerfHUD") != 0)
		{
			//printf("%s\n",identifier.Description);
			Collector::Engine::GetInstance().SetAdpaterDescription( identifier.Description);
			ret = true;
			break;
		}
	}
	if (!ret)
	{
		
		D3DADAPTER_IDENTIFIER9 identifier;
		if( SUCCEEDED(ifD3D->GetAdapterIdentifier(D3DADAPTER_DEFAULT,0,&identifier)))
		{
			//printf("%s\n",identifier.Description);
			Collector::Engine::GetInstance().SetAdpaterDescription(identifier.Description);
		}
	}
	ifD3D->Release();
}

}