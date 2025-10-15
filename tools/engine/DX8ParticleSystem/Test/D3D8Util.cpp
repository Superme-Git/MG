#include "StdAfx.h"
#include <vector>
#include "D3D8Util.h"
#include "FileIOManager.h"


bool D3D8Util::InitDirect3D(HWND hwnd) 
{ 
	IDirect3D8 *pID3D = Direct3DCreate8(D3D_SDK_VERSION); 

	HRESULT hr; 
	// we need the display mode so we can get 
	// the properties of our back buffer 
	D3DDISPLAYMODE d3ddm; 
	hr = pID3D->GetAdapterDisplayMode(D3DADAPTER_DEFAULT, &d3ddm); 
	if(FAILED(hr)) 
		return false;

	D3DPRESENT_PARAMETERS present; 
	ZeroMemory(&present, sizeof(present)); 
	present.SwapEffect = D3DSWAPEFFECT_COPY; 
	present.Windowed = TRUE; 
	present.BackBufferFormat = d3ddm.Format; 
	present.BackBufferHeight = d3ddm.Height;
	present.BackBufferWidth = d3ddm.Width;

	hr = pID3D->CreateDevice(D3DADAPTER_DEFAULT, 
		D3DDEVTYPE_HAL, 
		hwnd, 
		D3DCREATE_SOFTWARE_VERTEXPROCESSING, 
		&present, 
		&m_pID3DDevice); 

	if(FAILED(hr)) 
		return false;

	pID3D->Release();

	// we do our own coloring, so disable lighting 
	hr = m_pID3DDevice->SetRenderState(D3DRS_LIGHTING, FALSE);
	

	if (SUCCEEDED(hr))
	{
		m_pFileIOMan = new FileIOManager();
		m_pParticleMan = CreateParticleManager(m_pID3DDevice, m_pFileIOMan, NULL);

		m_pParticleMan->SetParticlePath(L"/bin/res/effect/particle/");
		

		Nuclear::PSParameter param;
		param.autorender = true;
		param.layer = 1;
		param.m_psModifyParam.m_pointCent.x = 400;
		param.m_psModifyParam.m_pointCent.y = 300;
		param.m_psModifyParam.m_pointObject.x = 100;
		param.m_psModifyParam.m_pointObject.y = 100;
		param.time = Nuclear::PSDEFAULTTIME;
		param.cycle = true;
		m_pshandle = m_pParticleMan->CreateParticleSystem(L"001Ð¡³æ·É",param);

		m_pParticleMan->InsertParticleNotify(m_pshandle, m_notify);

		return true;
	}

	return false;

} 

void D3D8Util::MainLoop()
{
	unsigned int ticks = GetTickCount();
	if (ticks - m_ticks > 10)
	{
		m_ticks = ticks;
		m_pID3DDevice->Clear(0, 0, D3DCLEAR_TARGET, 0xFF000000, 1.0f, 0);
		m_pID3DDevice->BeginScene();
		m_pParticleMan->RenderParticleSystem(m_pshandle);
		m_pID3DDevice->EndScene();
		m_pID3DDevice->Present(NULL, NULL, NULL, NULL);

	} else {
		Sleep(10);
	}
}

D3D8Util::D3D8Util(void)
{
	m_pshandle = Nuclear::INVALID_PS_HANDLE;
	m_pFileIOMan = NULL;
	m_pID3DDevice = NULL;
	m_pParticleMan = NULL;
}

D3D8Util::~D3D8Util(void)
{

	if (m_pshandle != Nuclear::INVALID_PS_HANDLE)
	{
		m_pParticleMan->DeleteParticleSystem(m_pshandle);
	}


	if (m_pParticleMan)
		ReleaseParticleManager(m_pParticleMan);

	if (m_pID3DDevice)
		m_pID3DDevice->Release();

	if (m_pFileIOMan)
		delete m_pFileIOMan;
}
