#include "stdafx.h"
#include "..\common\pfsutil.h"
#include "dx9renderer.h"


namespace Nuclear
{
	const float Renderer::Z = 0.5f;

	void Renderer::ResetFPSCounter() 
	{ 
		FReset(); 
	}
	float Renderer::GetFPS() const 
	{ 
		return GetCPS();
		
	}
	float Renderer::GetLatestFPS() const 
	{ 
		return (float)GetLatestCPS(); 
	}

	PictureHandle Renderer::LoadPictureFromNativePath(const std::wstring &nativepath)
	{
		Nuclear::FileBuffer fb(nativepath.c_str());
		int size = fb.Size();
		if( size == 0 ) return INVALID_PICTURE_HANDLE;
		return LoadPictureFromMem(fb.Begin(), size);
	}

	bool Renderer::End()
	{
		AddCount();
		return true;
	}

	// 创建函数
	bool CreateRenderer(Renderer **ppr, HWND hwnd, const XDisplayMode &dmode, CFileIOManager *pFileIOMan,DWORD flags, XPRENDERER_VERSION rv)
	{
		if( ppr == NULL ) 
			return false;
		*ppr = NULL;
		switch( rv )
		{		
		case XPRV_DEFAULT:
		case XPRV_DX9:			
			*ppr = new DX9Renderer(pFileIOMan);
			break;
		default:
			break;
		}
		if( *ppr == NULL ) 
			return false;

		if( !(*ppr)->Create(hwnd, dmode, flags) )
		{
			(*ppr)->Destroy();
			delete *ppr;
			*ppr = NULL;
			return false;
		}
		return true;
	}

	// 销毁函数
	void DestroyRenderer(Renderer *r)
	{ 
		if( r != NULL ) 
		{
			r->Destroy();
			delete r;
		}
	}

};
