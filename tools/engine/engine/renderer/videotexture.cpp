#include "stdafx.h"
#include <atlbase.h>
#include <algorithm>
#include "..\common\log.h"
//#include "..\common\pfsutil.h"
#include "VideoTexture.h"


namespace Nuclear
{
	//-----------------------------------------------------------------------------
	// Define GUID for Texture Renderer
	// {AAAE1B00-3946-4448-8166-7B513514BB75}
	//-----------------------------------------------------------------------------
	struct __declspec(uuid("{AAAE1B00-3946-4448-8166-7B513514BB75}")) CLSID_TextureRenderer;

	class CTextureRenderer : public CBaseVideoRenderer
	{
	public:
		CTextureRenderer(LPUNKNOWN pUnk,HRESULT *phr);
		~CTextureRenderer();

	public:
		HRESULT CheckMediaType(const CMediaType *pmt );
		HRESULT SetMediaType(const CMediaType *pmt ); 
		HRESULT DoRenderSample(IMediaSample *pMediaSample);

		int m_nVidWidth;   // Video width
		int m_nVidHeight;  // Video Height
		int m_nVidPitch;   // Video Pitch
		VideoTexture* m_pVideoTexture; //m_pVideoTexture
	};

	VideoTexture::VideoTexture(LPDIRECT3DDEVICE9 pd3dDevice) //, FileIOManager* pFileIOMan
	{
		m_pd3dDevice = pd3dDevice;
//		m_pFileIOMan = pFileIOMan;

		m_VideoD3DTexture = NULL;
		m_VideoD3DTextureDes = NULL;

		m_nVideoWidth = 800;   // Video width
		m_nVideoHeight = 600;  // Video Height

		m_bLoop = false;
	}

	VideoTexture::~VideoTexture()
	{
		CleanupDShow();

	//	DelTempFile();
	}

	void VideoTexture::CleanupDShow()
	{
		if(!(!m_pMC)) m_pMC->Stop();
		if(!(!m_pMC)) m_pMC.Release();
		if(!(!m_pME)) m_pME.Release();
		if(!(!m_pMP)) m_pMP.Release();
		if(!(!m_pGB)) m_pGB.Release();

		if(m_VideoD3DTexture)
		{
			m_VideoD3DTexture->Release();
			m_VideoD3DTexture = NULL;
		}
		if(m_VideoD3DTextureDes)
		{
			m_VideoD3DTextureDes->Release();
			m_VideoD3DTextureDes = NULL;
		}
	}

	void VideoTexture::getMovieDimensions(int& nwidth, int& nheight)
	{
		nwidth = m_nVideoWidth;
		nheight = m_nVideoHeight;
	}

	void VideoTexture::setMovieDimensions(int nwidth, int nheight)
	{
		m_nVideoWidth = nwidth;
		m_nVideoHeight = nheight;
	}

	void VideoTexture::SetLoop(bool bFlag)
	{
		m_bLoop = bFlag;
	}

	void VideoTexture::HoldLastFrame()
	{
		Seek(1.0f);
	}

	float VideoTexture::GetTotalLength() //秒
	{
		REFERENCE_TIME TotalLength;
		m_pMS->GetDuration(&TotalLength);
		float adf = TotalLength/10000000.0f;
		int a=0;
		return adf;
	}

	void VideoTexture::SetPosition(float ftime)
	{
		float fcoef = ftime/GetTotalLength();
		Seek(fcoef);
	}

	bool VideoTexture::DelTempFile()
	{
		std::wstring tempFile = L"../bin/loginvideompeg1.yt";
		if( ::DeleteFileW( tempFile.c_str()) )
			return true;

		return false;
	}

	bool VideoTexture::init(std::wstring filename, bool bTextureCaps_pow2)
	{
		m_bTextureCaps_pow2 = bTextureCaps_pow2;

		//filename ---pfs路径, 把包文件暂时写到一个临时文件里
/*		std::wstring tempFile = L"../bin/loginvideompeg1.yt";

		DWORD attr = GetFileAttributes( tempFile.c_str());
		if(attr != INVALID_FILE_ATTRIBUTES)
		{
			if( (FILE_ATTRIBUTE_HIDDEN|FILE_ATTRIBUTE_READONLY) & attr)
			{
				attr &= ~(FILE_ATTRIBUTE_HIDDEN|FILE_ATTRIBUTE_READONLY);
				SetFileAttributes(tempFile.c_str(), attr);
			}
		}

		::PFS::CFile::CopyFile(filename, tempFile);
		filename = tempFile;
*/		

/*
		XBuffer data;
		bool bres = m_pFileIOMan->GetFileImage(filename, data);
		if(bres)
		{
			DWORD attr = GetFileAttributes( tempFile.c_str());
			if(attr != INVALID_FILE_ATTRIBUTES)
			{
				if( (FILE_ATTRIBUTE_HIDDEN|FILE_ATTRIBUTE_READONLY) & attr)
				{
					attr &= ~(FILE_ATTRIBUTE_HIDDEN|FILE_ATTRIBUTE_READONLY);
					SetFileAttributes(tempFile.c_str(), attr);
				}
			}

			FILE *f = FOpen(tempFile.c_str(), L"wb");
			if( f==NULL )
			{
				fclose(f);
				return false;
			}
			bres = FWrite(f, data.begin(), data.size());
			if(!bres)
			{
				fclose(f);
				return false;
			}
			fclose(f);

			filename = tempFile;
		}
		//
*/
/*		DWORD	dwAttribute = GetFileAttributes( filename.c_str());
		if( INVALID_FILE_ATTRIBUTES == dwAttribute)
			return false;

		if( !(FILE_ATTRIBUTE_HIDDEN & dwAttribute) )
		{
			dwAttribute |= FILE_ATTRIBUTE_HIDDEN;
			SetFileAttributes( filename.c_str(), dwAttribute);
		}
*/
		if( FAILED(InitGraph(filename)) ) 
		{
			XPLOG_ERROR(L"InitGraph failed.\n");
			return false;
		}

		return true;
	}

	HRESULT VideoTexture::InitGraph(std::wstring filename)
	{
		HRESULT hr = S_OK;
		CComPtr<IBaseFilter>    pFTR;
		CComPtr<IBaseFilter>    pFSrc;
		CComPtr<IPin>           pFSrcPinOut;
		CTextureRenderer        *pCTR;

		if( FAILED(m_pGB.CoCreateInstance(CLSID_FilterGraph, NULL, CLSCTX_INPROC)) )
		{
			XPLOG_ERROR(L"CoCreate failed.");
			return E_FAIL;
		}

		pCTR = new CTextureRenderer(NULL, &hr);
		if( FAILED(hr) )
		{
			XPLOG_ERROR(L"Could not create texture renderer object!  hr=0x%x", hr);
			return E_FAIL;
		}
		pCTR->m_pVideoTexture = this;

		pFTR = pCTR;
		if( FAILED(hr = m_pGB->AddFilter(pFTR, L"TEXTURERENDERER")) )
		{
			XPLOG_ERROR(L"Could not add renderer filter to graph!  hr=0x%x", hr);
			return hr;
		}

		std::replace( filename.begin(), filename.end(), L'/', L'\\');
		std::wstring::size_type pos = filename.find_last_not_of( L"\\");
		if( pos != filename.size()-1)
		{
			filename.erase( pos+1);
		}
		filename.resize( filename.size() + 1, 0 );

		if( FAILED(hr = m_pGB->RenderFile(filename.c_str(), NULL)) )
		{
			XPLOG_ERROR(L"Could not render Source file.  hr=0x%x", hr);
			return hr;
		}
/*
		if( FAILED(hr = m_pGB->AddSourceFilter(filename.c_str(), L"SOURCE", &pFSrc)) )
		{
			XPTRACE(L"Could not create source filter to graph!  hr=0x%x", hr);
			return hr;
		}

		if( FAILED(hr = pFSrc->FindPin(L"Output", &pFSrcPinOut)) )
		{
			XPTRACE(L"Could not find output pin!  hr=0x%x", hr);
			return hr;
		}

		if( FAILED(hr = m_pGB->Render(pFSrcPinOut)) )
		{
			XPTRACE(L"Could not render Source Pin.  hr=0x%x", hr);
			return hr;
		}
*/
		m_pGB.QueryInterface(&m_pMC);
		m_pGB.QueryInterface(&m_pMP);
		m_pGB.QueryInterface(&m_pME);
		m_pGB.QueryInterface(&m_pMS);

		if( FAILED(hr = m_pMC->Run()) )
		{
			XPLOG_ERROR(L"Could not run the DirectShow graph!  hr=0x%x", hr);
			return hr;
		}
		return S_OK;
	}


	void VideoTexture::Loop()
	{
		long lEventCode;
		LONG_PTR lParam1;
		LONG_PTR lParam2;
		HRESULT hr;

		// Check for completion events
		if(m_pME != NULL)
		{
			hr = m_pME->GetEvent(&lEventCode, &lParam1, &lParam2, 0);
			if(SUCCEEDED(hr))
			{
				if( (EC_COMPLETE == lEventCode) )
				{
					if(m_bLoop)
					{
						hr = m_pMP->put_CurrentPosition(0);
					}
					else
					{
						if(m_pMC != NULL)
						{
							m_pMC->Pause();
						}
					}
				}

				// Free any memory associated with this event
				hr = m_pME->FreeEventParams(lEventCode, lParam1, lParam2);
			}
		}
	}

	void VideoTexture::setTexture()
	{
		if(m_pd3dDevice != NULL)
		{
			m_pd3dDevice->SetTexture(0, m_VideoD3DTextureDes); //
		//	D3DXSaveTextureToFile(L"D:\\111.bmp", D3DXIFF_BMP, m_VideoD3DTextureDes, NULL);
		}
	}

	void VideoTexture::CleanTextureContents()
	{	
		BYTE *pTxtBuffer;  

		D3DLOCKED_RECT d3dlr;
		if (FAILED(m_VideoD3DTexture->LockRect(0, &d3dlr, 0, 0)))
			return;

		pTxtBuffer = static_cast<byte *>(d3dlr.pBits);

		for(int i=0; i<m_nVideoHeight; i++)
		{
			memset(&pTxtBuffer[m_nVideoWidth*i*4], 0, 4 * m_nVideoWidth);
		}

		if(FAILED(m_VideoD3DTexture->UnlockRect(0)))
			return;
	}

	float VideoTexture::GetPosition()
	{
		if (!m_pMC)
			return 0.0f;

		REFERENCE_TIME TotalLength, CurrentPos, Useless;
		m_pMS->GetDuration(&TotalLength);
		m_pMS->GetPositions(&CurrentPos, &Useless);
		return float(CurrentPos) / float(TotalLength);
	}

	void VideoTexture::Seek(float AbsolutePosition)
	{
		if(m_pMS == NULL)
		{
			return;
		}

		HRESULT hr;
		REFERENCE_TIME TotalLength;
		m_pMS->GetDuration(&TotalLength);
		TotalLength = REFERENCE_TIME(TotalLength * double(AbsolutePosition));
		hr = m_pMS->SetPositions(&TotalLength, AM_SEEKING_AbsolutePositioning, NULL, AM_SEEKING_NoPositioning);
	}

	void VideoTexture::SetSpeed(float NewRate)
	{
		if(m_pMS != NULL)
		{
			m_pMS->SetRate(NewRate);
		}
	}

	void VideoTexture::pauseMovie()
	{
		if(m_pMC != NULL)
		{
			m_pMC->Pause();
		}
	}

	void VideoTexture::playMovie()
	{
		if(m_pMC != NULL)
		{
			m_pMC->Run();
		}
	}

	void VideoTexture::rePlayMovie()
	{
		if(m_pMS == NULL) return;

		LONGLONG p1 = 0;
		LONGLONG p2 = 0;

		m_pMS->SetPositions(
			&p1, AM_SEEKING_AbsolutePositioning, &p2, AM_SEEKING_NoPositioning);
	}


	void VideoTexture::stopMovie()
	{
		if (m_pMC != NULL)
			m_pMC->Stop();
	}

	bool VideoTexture::isPlayingMovie()
	{
		OAFilterState pfs;
		HRESULT hr;

		if(m_pME!=NULL)
		{
			long ev, p1, p2;

			while(E_ABORT != m_pME->GetEvent(&ev, &p1, &p2, 0))
			{
				// check for completion
				if(ev==EC_COMPLETE)
				{
					pauseMovie();
					return false;
				}

				// release event params
				hr = m_pME->FreeEventParams(ev, p1, p2);
				if(FAILED(hr))
				{
					pauseMovie();
					return false;
				}
			}
		}

		// get the running state!
		if(m_pMC != NULL)
		{
			hr = m_pMC->GetState(0, &pfs);
			if(FAILED(hr))
			{
				pauseMovie();
				return false;
			}

			return pfs==State_Running;
		}

		// it hasn't even been initialized!
		return false;
	}

	HRESULT VideoTexture::ReleaseVideoTexture()	//释放资源..
	{
		stopMovie();
		m_fPosTime = GetPosition();
		if(m_VideoD3DTexture != NULL)
		{
			m_VideoD3DTexture->Release();
			m_VideoD3DTexture = NULL;
		}
		if(m_VideoD3DTextureDes != NULL)
		{
			m_VideoD3DTextureDes->Release();
			m_VideoD3DTextureDes = NULL;
		}
		return S_OK;
	}

	HRESULT VideoTexture::Restore()
	{
		HRESULT hr;

		// 如果显卡要求纹理必须是2幂....
		int twoSquared;
		int width = m_nVideoWidth;
		int height = m_nVideoHeight;
		for(twoSquared=2; m_nTexWidth==0 || m_nTexHeight==0; twoSquared*=2)
		{
			if (m_nTexWidth==0 && twoSquared>=width)
				m_nTexWidth = twoSquared;
			if (m_nTexHeight==0 && twoSquared>=height)
				m_nTexHeight = twoSquared;
		}
		if(!m_bTextureCaps_pow2)
		{
			m_nTexWidth=width;
			m_nTexHeight=height;
		}

		if( FAILED( hr = D3DXCreateTexture(m_pd3dDevice, m_nTexWidth, m_nTexHeight, 1, 0,
											D3DFMT_X8R8G8B8, D3DPOOL_SYSTEMMEM, &m_VideoD3DTexture) ) )
														
		{
			XPLOG_ERROR(L"Could not create the D3DX texture!  hr=0x%x", hr);
			return hr;
		}

		D3DSURFACE_DESC ddsd;
		if( FAILED( hr = m_VideoD3DTexture->GetLevelDesc( 0, &ddsd ) ) ) 
		{
			XPLOG_ERROR(L"Could not get level Description of D3DX texture! hr = 0x%x", hr);
			return hr;
		}

		if(ddsd.Format != D3DFMT_X8R8G8B8)
		{
			XPLOG_ERROR(L"Texture is format we can't handle! Format = 0x%x", ddsd.Format);
			return VFW_E_TYPE_NOT_ACCEPTED;
		}

		if( FAILED( hr = D3DXCreateTexture(m_pd3dDevice, m_nTexWidth, m_nTexHeight, 1, 0,
			D3DFMT_X8R8G8B8, D3DPOOL_DEFAULT, &m_VideoD3DTextureDes ) ) )
		{
			XPLOG_ERROR(L"Could not create the D3DX texture!  hr=0x%x", hr);
			return hr;
		}

		if( !(isPlayingMovie()) )
		{
			playMovie();
		}
		Seek(m_fPosTime);

		CleanTextureContents();
		return S_OK;
	}

	CTextureRenderer::CTextureRenderer( LPUNKNOWN pUnk, HRESULT *phr )
		: CBaseVideoRenderer(__uuidof(CLSID_TextureRenderer),
		NAME("Texture Renderer"), pUnk, phr)
	{
		*phr = S_OK;
	}


	//-----------------------------------------------------------------------------
	// CTextureRenderer destructor
	//-----------------------------------------------------------------------------
	CTextureRenderer::~CTextureRenderer()
	{
		// Do nothing
	}

	HRESULT CTextureRenderer::CheckMediaType(const CMediaType *pmt)
	{
		HRESULT hr = E_FAIL;
		VIDEOINFO *pvi;

		if( *pmt->FormatType() != FORMAT_VideoInfo )
		{
			return E_INVALIDARG;
		}

		// Only accept RGB24
		pvi = (VIDEOINFO *)pmt->Format();
		if(IsEqualGUID( *pmt->Type(), MEDIATYPE_Video)  &&
			IsEqualGUID( *pmt->Subtype(), MEDIASUBTYPE_RGB32))
		{
			hr = S_OK;
		}

		return hr;
	}

	HRESULT CTextureRenderer::SetMediaType(const CMediaType *pmt)
	{
		VIDEOINFO *pviBmp;   // Bitmap info header
		pviBmp = (VIDEOINFO *)pmt->Format();
		m_nVidWidth  = pviBmp->bmiHeader.biWidth;
		m_nVidHeight = abs(pviBmp->bmiHeader.biHeight);

		m_pVideoTexture->setMovieDimensions(m_nVidWidth, m_nVidHeight);

		m_nVidPitch = m_nVidWidth * 4; //32位
	
	//	m_nVidPitch  = (m_nVidWidth * 3 + 3) & ~(3); //24位rgb

		m_pVideoTexture->ReleaseVideoTexture();
		return m_pVideoTexture->Restore();
	}

	HRESULT CTextureRenderer::DoRenderSample( IMediaSample* pSample )
	{		
		BYTE  *pBmpBuffer, *pTxtBuffer;   // Bitmap buffer, texture buffer
		//LONG  lTxtPitch;                // Pitch of bitmap, texture

		// Get the video bitmap buffer
		pSample->GetPointer( &pBmpBuffer );

		D3DLOCKED_RECT d3dlr;
		if (FAILED(m_pVideoTexture->m_VideoD3DTexture->LockRect(0, &d3dlr, 0, 0)))
			return E_FAIL;

		//m_pVideoTexture->bsetTexture = false;

		// Get the texture buffer & pitch
		pTxtBuffer = static_cast<byte *>(d3dlr.pBits);
		//lTxtPitch = d3dlr.Pitch;

		for(int i=0; i<m_nVidHeight; i++)
		{
			memcpy(&pTxtBuffer[m_nVidWidth*i*4], &pBmpBuffer[m_nVidWidth*i*4], 4 * m_nVidWidth);
		}

		if(FAILED(m_pVideoTexture->m_VideoD3DTexture->UnlockRect(0)))
			return E_FAIL;

		m_pVideoTexture->m_pd3dDevice->UpdateTexture(m_pVideoTexture->m_VideoD3DTexture,
														m_pVideoTexture->m_VideoD3DTextureDes);
		/*

		BYTE  *pBmpBuffer, *pTxtBuffer; // Bitmap buffer, texture buffer
		LONG  lTxtPitch;                // Pitch of bitmap, texture

		BYTE  * pbS = NULL;
		DWORD * pdwS = NULL;
		DWORD * pdwD = NULL;
		UINT row, col, dwordWidth;

		pSample->GetPointer( &pBmpBuffer );

		D3DLOCKED_RECT d3dlr;
		if( FAILED(m_pVideoTexture->m_VideoD3DTexture->LockRect(0, &d3dlr, 0, 0)))
			return E_FAIL;

		// Get the texture buffer & pitch
		pTxtBuffer = static_cast<byte *>(d3dlr.pBits);
		lTxtPitch = d3dlr.Pitch;

	//	int nVidPitch  = (m_nVidWidth * 3 + 3) & ~(3);

		// Copy the bits

	//	if (g_TextureFormat == D3DFMT_X8R8G8B8)
		{
			// Instead of copying data bytewise, we use DWORD alignment here.
			// We also unroll loop by copying 4 pixels at once.
			//
			// original BYTE array is [b0][g0][r0][b1][g1][r1][b2][g2][r2][b3][g3][r3]
			//
			// aligned DWORD array is     [b1 r0 g0 b0][g2 b2 r1 g1][r3 g3 b3 r2]
			//
			// We want to transform it to [ff r0 g0 b0][ff r1 g1 b1][ff r2 g2 b2][ff r3 b3 g3]
			// below, bitwise operations do exactly this.

			dwordWidth = m_nVidWidth / 4; // aligned width of the row, in DWORDS
			// (pixel by 3 bytes over sizeof(DWORD))

			for( row = 0; row< (UINT)m_nVidHeight; row++)
			{
				pdwS = ( DWORD*)pBmpBuffer;
				pdwD = ( DWORD*)pTxtBuffer;

				for( col = 0; col < dwordWidth; col ++ )
				{
					pdwD[0] =  pdwS[0] | 0xFF000000;
					pdwD[1] = ((pdwS[1]<<8)  | 0xFF000000) | (pdwS[0]>>24);
					pdwD[2] = ((pdwS[2]<<16) | 0xFF000000) | (pdwS[1]>>16);
					pdwD[3] = 0xFF000000 | (pdwS[2]>>8);
					pdwD +=4;
					pdwS +=3;
				}

				// we might have remaining (misaligned) bytes here
				pbS = (BYTE*) pdwS;
				for( col = 0; col < (UINT)m_nVidWidth % 4; col++)
				{
					*pdwD = 0xFF000000     |
						(pbS[2] << 16) |
						(pbS[1] <<  8) |
						(pbS[0]);
					pdwD++;
					pbS += 3;
				}

				pBmpBuffer += m_nVidPitch; //nVidPitch
				pTxtBuffer += lTxtPitch;
			}// for rows
		}

		if(FAILED(m_pVideoTexture->m_VideoD3DTexture->UnlockRect(0)))
			return E_FAIL;

		m_pVideoTexture->m_pd3dDevice->UpdateTexture(m_pVideoTexture->m_VideoD3DTexture,
														m_pVideoTexture->m_VideoD3DTextureDes);
*/
		return S_OK;
	}

};