#pragma once

#include <d3d8.h>
#include "include\xpmaths.h"
#include "include\istatemanager.h"
#include "include\iparticlemanager.h"
#include "include\ifileio.h"
#include "include\createparticlemanager.h"

class PNotify : public Nuclear::IParticleNotify
{
public:
	virtual void OnStart() {OutputDebugString(L"Begin\n");}
	virtual void OnStop() {OutputDebugString(L"Stop\n");}
	virtual void OnEnd() {OutputDebugString(L"End\n");}
};

class D3D8Util
{
private:
	LPDIRECT3DDEVICE8 m_pID3DDevice; 
	unsigned int m_ticks;
	Nuclear::IParticleManager *m_pParticleMan;
	Nuclear::IFileIO *m_pFileIOMan;
	Nuclear::ParticleSystemHandle m_pshandle;
	PNotify m_notify;
public:
	D3D8Util(void);
	~D3D8Util(void);

	bool InitDirect3D(HWND hwnd);
	void MainLoop();
};
