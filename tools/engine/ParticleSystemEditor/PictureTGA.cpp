#include "StdAfx.h"
#include "PictureTGA.h"
#include "engine.h"
#include "renderer/dx9renderer.h"
#include "ParticleSystemEditorView.h"

CPictureTGA::CPictureTGA(void)
: mWidth(0)
, mHeight(0)
, mpPixels(NULL)
{
}

CPictureTGA::~CPictureTGA(void)
{
	Release();
}

void CPictureTGA::Release()
{
	if(mpPixels)
	{
		delete [] mpPixels;
		mpPixels = NULL;
	}
}

bool CPictureTGA::GetWidthHeight(int &nWidth, int &nHeight)
{
	if( mpPixels == NULL )
		return FALSE;
	nWidth = mWidth ;
	nHeight = mHeight ;
	return TRUE;
}

bool CPictureTGA::Open(LPCTSTR strFilePath)
{
	bool bRet = false;

	Release();

	Nuclear::DX9Renderer* pRenderer = dynamic_cast<Nuclear::DX9Renderer*>(CParticleSystemEditorView::getInstance()->GetRenderer());
	if(pRenderer)
	{
		IDirect3DDevice9* pDevice = pRenderer->GetDevice();
		if(pDevice)
		{
			D3DXIMAGE_INFO imgInfo;
			if(SUCCEEDED(D3DXGetImageInfoFromFile(strFilePath, &imgInfo)))
			{
				mWidth = imgInfo.Width;
				mHeight = imgInfo.Height;

				if(mWidth > 0 && mHeight > 0)
				{
					IDirect3DSurface9* pImgSurface = NULL;
					pDevice->CreateOffscreenPlainSurface(mWidth, mHeight, D3DFMT_A8R8G8B8, D3DPOOL_DEFAULT, &pImgSurface, NULL);
					if(pImgSurface)
					{
						if(SUCCEEDED(D3DXLoadSurfaceFromFile(pImgSurface, NULL, NULL, strFilePath, NULL, D3DX_FILTER_NONE, 0, NULL)))
						{
							D3DLOCKED_RECT lockRc;
							if(SUCCEEDED(pImgSurface->LockRect(&lockRc, NULL, D3DLOCK_READONLY)))
							{
								int lineBytes = mWidth * 4;

								mpPixels = new BYTE[lineBytes * mHeight];
								if(mpPixels)
								{
									for(int r = 0; r < mHeight; ++r)
									{
										const BYTE* pSrcLine = (const BYTE*)lockRc.pBits + r * lockRc.Pitch;
										BYTE* pDstLine = mpPixels + r * lineBytes;
										memcpy(pDstLine, pSrcLine, lineBytes);
									}

									bRet = true;
								}

								pImgSurface->UnlockRect();
							}
						}

						pImgSurface->Release();
						pImgSurface = NULL;
					}
				}
			}
		}
	}
	
	return bRet;
}

bool CPictureTGA::ReadRGBAData(BYTE* pDest)
{
	if(pDest && mpPixels)
	{
		memcpy(pDest, mpPixels, mWidth * mHeight * 4);
		return true;
	}
	return false;
}