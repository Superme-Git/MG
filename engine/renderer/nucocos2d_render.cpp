//  cocos2d_render.cpp
//  engine

#include <iostream>
#include <algorithm>
#include "../common/nulog.h"
#include "../common/nupngwriter.h"
#include "../common/nufileiomanager.h"
#include "nucocos2d_render.h"
#include "nufonttexture.h"
#include "nufontmanager.h"
#include "nufxcode.h"
#include "nucocos2d_type.h"
#include <ccTypes.h>
#include <cocos2d.h>

#include <CCGL.h>
#include <shaders/ccGLStateCache.h>
#include "../engine/nucocos2d_wraper.h"
#include "utils/Utils.h"


#if defined(ANDROID) && defined(LOGCAT)
#include <android/log.h>
#define  LOG_TAG    "mt3"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#else
#define LOGI
#define LOGE
#define LOGD
#endif

#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#else
#include "textures/ETCHeader.h"
#endif
#include <support/image_support/OgreDDSCodec.h>
namespace cocos2d
{
    extern bool isCompressed(PixelFormat pf);
}

namespace Nuclear
{
	const float XP_MMENGINE_Z_FAR = 200000.0f;
 
	#ifdef _MSC_VER
	#pragma pack(push,1)
	#endif
		typedef struct
		{
			uint32_t version;
			uint32_t flags;
			uint64_t pixelFormat;
			uint32_t colorSpace;
			uint32_t channelType;
			uint32_t height;
			uint32_t width;
			uint32_t depth;
			uint32_t numberOfSurfaces;
			uint32_t numberOfFaces;
			uint32_t numberOfMipmaps;
			uint32_t metadataLength;
	#ifdef _MSC_VER
		} ccPVRv3TexHeader;
	#pragma pack(pop)
	#else
	} __attribute__((packed)) ccPVRv3TexHeader;
	#endif

    Cocos2dRenderTarget* Cocos2dRenderTarget::CreateRenderTarget(int aIWidth, int aIHeight, NuclearTextureFormat aTFormat)
    {
        Cocos2dRenderTarget* pRet = new Cocos2dRenderTarget();
        
        cocos2d::CCTexture2DPixelFormat fmt;
        switch (aTFormat) {
            case XPTEXFMT_A8R8G8B8:
                fmt = cocos2d::kCCTexture2DPixelFormat_RGBA8888;
                break;
                
            default:
                assert(false);
                break;
        }
        cocos2d::CCRenderTexture* pRT = cocos2d::CCRenderTexture::create(aIWidth, aIHeight, fmt, 0);        
        if (!pRT){ CC_SAFE_DELETE(pRet); return NULL;}
        pRT->retain();
        pRet->m_pRenderTexture = (MyRenderTexture*)pRT;
        
        return pRet;
    }

	void Cocos2dRenderer::initial()
	{
		m_bUseLJFM = false;
		m_bDeviceLost = false;
		m_picHandleSeed = 1;
		m_bTextureCaps_pow2 = false;
		m_dwPixelShaderVersion = 0;
		m_dwVertexShaderVersion = 0;
        
		m_fUseCursor = false;
		m_iCurCursorHandle = INVALID_PICTURE_HANDLE;
		m_fCurCursorOffsetX = 0;
		m_fCurCursorOffsetY = 0;
		m_pFontMan = NULL;
		m_pParticleMan = NULL;
        
		m_particleVB    = 0;
		m_particleUVB   = 0;
		m_particleIB    = 0;
        m_pIndices      = new GLushort[6*MAX_PARTICLE_NUM*sizeof(WORD)];
        assert(m_pIndices);
        memset(m_pIndices, 0, 6*MAX_PARTICLE_NUM*sizeof(WORD));
        
#ifdef XP_PERFORMANCE
		memset(&m_performance, 0, sizeof(m_performance));
#endif 
		m_pEllipsePicVertex = NULL;
        
		m_iBatchVBOffset = 0;
		m_iBatchIBOffset = 0;
		m_iBatchTCVERTEXVBOffset = 0;
		m_iBatchTCVERTEXIBOffset = 0;
        
		m_fViewportScale = 1.0f;
		m_xpMultisampleType = XPMULTISAMPLE_NONE;
        
         MHSD_UTILS::flurryEvent(L"Cocos2dRenderer_Init");
	}
    
	std::wstring Cocos2dRenderer::GetAdapterDescription()
	{
		return m_adapterDes;
	}
    
    NuclearDisplayMode Cocos2dRenderer::GetDisplayMode() const
	{
        NuclearDisplayMode dismd;
        dismd.width = GetEngine()->m_adapter->get_logic_w();
        dismd.height = GetEngine()->m_adapter->get_logic_h();
        dismd.bitperpix = 32;
        
        return dismd;
    }
    
	void Cocos2dRenderer::GetAvailableMultiSampleType(bool window, bool isMode16, std::vector<NuclearMultiSampleType> &types)
	{
        assert("GetAvailableMultiSampleType" && false);
	}

	Cocos2dRenderer::~Cocos2dRenderer()
	{
		PictureMap::iterator it = m_mapPictures.begin();
		for (; it != m_mapPictures.end(); ++it)
		{
			int cnt = (int)(it->second.m_CTextureInfoVector.size());
			for (int i = 0; i < cnt; ++i)
			{
				cocos2d::CCTexture2D* ifTexture = it->second.m_CTextureInfoVector[i].m_pTexture;
				delete ifTexture;
			}
		}
		m_mapPictures.clear();
	}
    
	XPCREATE_RENDERER_RESULT Cocos2dRenderer::Create(const NuclearDisplayMode &dmode, DWORD flags, NuclearMultiSampleType mstype) 
	{
		m_pParticleMan = new ParticleManager(this, m_pFileIOMan);
        
		m_pParticleMan->SetParticlePath(L"/effect/particle/");
        
		m_pFontMan = new CFontManager();
		m_pFontMan->Init(this, m_pFileIOMan);

		InitDefaultFont();
		InitBatchVB();

		MHSD_UTILS::flurryEvent(L"Cocos2dRenderer_Create");
		return XPCRR_OK; 
	}
    
	bool Cocos2dRenderer::InitDefaultFont()
	{
        return true;
	}
    
	void Cocos2dRenderer::Destroy() 
	{
		if( m_pFontMan )
		{
			m_pFontMan->Release();
			delete m_pFontMan;
		}
        
        delete [] m_pIndices;
		XPSAFE_DELETE(m_pParticleMan);
        MHSD_UTILS::flurryEvent(L"Cocos2dRenderer_Destroy");
	}
 
	bool Cocos2dRenderer::DrawLine(const NuclearFRectt &line, NuclearColor color) 
	{
		return true;
	}
    
	bool Cocos2dRenderer::DrawLine(const NuclearFRectt *lines, int n, NuclearColor color) 
	{
		return true;
	}
    
	bool Cocos2dRenderer::DrawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, NuclearColor color)
	{
		return true;
	}
    
	bool Cocos2dRenderer::DrawEllipsePicture(const DrawEllipsePictureParam& param)
	{
		return true;
	}
    
	bool Cocos2dRenderer::DrawEllipsePicture(PictureHandle handle, float x, float y, float rx, float ry, float u0, float v0, NuclearColor color)
	{
		return true;
	}
    
	bool Cocos2dRenderer::DrawTriangles(const std::vector<NuclearTriangle> &triangles, NuclearColor color, NuclearFillMode fillmode)
	{
		return true;
	}
    
	bool Cocos2dRenderer::DrawPolygon(const std::vector<NuclearPoint> &pts, NuclearColor color, NuclearFillMode fillmode)
	{
		return true;
	}
    
	bool Cocos2dRenderer::DrawTriangleList(const NuclearTCVertex* pVertexs, int nVertexNum)
	{
		return true;
	}
    
	bool Cocos2dRenderer::DrawFan(float x, float y, float r, float fTheta, NuclearColor color, NuclearFillMode fillmode, const NuclearMatrix3 *pMatrix)
	{
		return true;
	}
    
	bool Cocos2dRenderer::DrawEllipseFan(float x, float y, float rx, float ry, NuclearColor color, NuclearFillMode fillmode, float fStartAngle, float fEndAngle, const NuclearMatrix3 *pMatrix)
	{
		return true;
	}
    
	bool Cocos2dRenderer::DrawEllipse(float x, float y, float rx, float ry, NuclearColor color, NuclearFillMode fillmode, float fAngle)
	{
		return true;
	}
    
	bool Cocos2dRenderer::DrawBox(const NuclearFRectt &rect, NuclearColor color, NuclearFillMode fillmode, const NuclearMatrix3 *pMatrix)
	{
#ifdef XP_PERFORMANCE
		m_performance.m_iDrawBoxCountPerFrame++;
#endif
		NuclearVector2 vecOut;
		NuclearVector2 thePos[4];
		thePos[0].x = rect.left;
		thePos[0].y = rect.top;
		thePos[1].x = rect.right;
		thePos[1].y = rect.top;
		thePos[2].x = rect.right;
		thePos[2].y = rect.bottom;
		thePos[3].x = rect.left;
		thePos[3].y = rect.bottom;
		if (pMatrix)
		{
			for (int i=0;i<4;++i)
			{
				XPVec3TransformCoord(&vecOut, thePos+i, pMatrix);
				thePos[i] = vecOut;
			}
		}

		unsigned int colors[4];
		colors[0] = colors[1] = colors[2] = colors[3] = color.data;

		GLfloat   vertices[] = {
			thePos[0].x, thePos[0].y,
			thePos[3].x, thePos[3].y,
			thePos[1].x, thePos[1].y,
			thePos[2].x, thePos[2].y,
		};
        
		cocos2d::ccGLBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		cocos2d::CCShaderCache::sharedShaderCache()->pushShader(kCCShader_PositionColor);
        cocos2d::ccGLEnableVertexAttribs( cocos2d::kCCVertexAttribFlag_Position | cocos2d::kCCVertexAttribFlag_Color);
        
        cocos2d::ccGLBindTexture2D( 0 );
        
		glVertexAttribPointer(cocos2d::kCCVertexAttrib_Position, 2, GL_FLOAT, GL_FALSE, 0, vertices);
		glVertexAttribPointer(cocos2d::kCCVertexAttrib_Color, 4, GL_UNSIGNED_BYTE, GL_TRUE, 0, colors);
        
		if( fillmode == XPFM_SOLIDFILL )
		{
			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
#ifdef XP_PERFORMANCE
            m_performance.m_iDrawPrimitiveUPCountPerFrame++;
#endif
		}
		else if( fillmode == XPFM_WIREFRAME )
		{
			glDrawArrays(GL_LINE_STRIP, 0, 4);
#ifdef XP_PERFORMANCE
            m_performance.m_iDrawPrimitiveUPCountPerFrame++;
#endif
		}

        cocos2d::CCShaderCache::sharedShaderCache()->popShader();

		return true;
	}
    
	bool Cocos2dRenderer::DrawBox(const NuclearFRectt *rects, int n, NuclearColor color, NuclearFillMode fillmode, const NuclearMatrix3 *pMatrix)
	{
		for (int i = 0; i < n; ++i)
		{
			DrawBox(rects[i], color, fillmode, pMatrix);
		}
        return true;
	}
    
	PictureHandle Cocos2dRenderer::NewPicture(int width, int height, NuclearPictureInfo *pPicInfo, NuclearTextureFormat texfmt)
	{
#ifdef XP_PERFORMANCE
		int64_t tick = Nuclear::GetMilliSeconds();
#endif
		if (texfmt != XPTEXFMT_DEFAULT)
		{
			if( !IsTexfmtSupport(texfmt) )
				texfmt = XPTEXFMT_DEFAULT;
		}
        
		if( texfmt == XPTEXFMT_DEFAULT )
		{
			texfmt = IsMode16() ? XPTEXFMT_A4R4G4B4 : XPTEXFMT_A8R8G8B8;
		}
        
		CTextureInfo TextureInfo;
		CPicInfo PicInfo;
        
		if(!m_bTextureCaps_pow2)
		{
			TextureInfo.rect.left = 0;
			TextureInfo.rect.top = 0;
			TextureInfo.rect.right = width;
			TextureInfo.rect.bottom = height;
			if( 0 != GenTexture(texfmt, XPPOOL_MANAGED, TextureInfo) ) 
				return INVALID_PICTURE_HANDLE;
            
			PicInfo.m_info.m_nPicWidth = width;
			PicInfo.m_info.m_nPicHeight = height;
			PicInfo.m_info.m_pooltype = XPPOOL_MANAGED;
			PicInfo.m_info.m_texfmt = texfmt;
            
			if (pPicInfo)
			{
				TextureInfo.fileuri = pPicInfo->fileuri;
				PicInfo.m_info.fileuri = pPicInfo->fileuri;
				*pPicInfo = PicInfo.m_info;
			}
            
			PicInfo.m_CTextureInfoVector.push_back(TextureInfo);
			m_mapPictures.insert(std::make_pair(m_picHandleSeed, PicInfo));            
		}
        else
		{
			if( texfmt != XPTEXFMT_A8R8G8B8 && texfmt != XPTEXFMT_A4R4G4B4 ) return INVALID_PICTURE_HANDLE;
			RECT srcRect;
			srcRect.left = srcRect.top = 0;
			srcRect.right = width;
			srcRect.bottom = height;
			std::vector<NuclearRect> dstRects;
			PicInfo.m_info.m_nPicHeight = height;
			PicInfo.m_info.m_nPicWidth = width;
			PicInfo.m_info.m_pooltype = XPPOOL_MANAGED;
			PicInfo.m_info.m_texfmt = texfmt;
			if (pPicInfo)
			{
				*pPicInfo = PicInfo.m_info;
			}

			PartitionRectToPower2(srcRect, dstRects);
			for(int i=0; i<(int)dstRects.size();++i)
			{
				CTextureInfo TextureInfo;
				TextureInfo.rect = dstRects[i];
                
				if( 0 != GenTexture(texfmt,XPPOOL_MANAGED ,TextureInfo) )
					return INVALID_PICTURE_HANDLE;
                
				PicInfo.m_CTextureInfoVector.push_back(TextureInfo);
			}
            
			m_mapPictures.insert(std::make_pair(m_picHandleSeed, PicInfo));
		}
        
#ifdef XP_PERFORMANCE
		int deltaT = int(Nuclear::GetMilliSeconds() - tick);
		m_performance.m_iTotalLoadTimePerFrame += deltaT;
#endif
		return m_picHandleSeed++;
	}

	bool Cocos2dRenderer::DrawText(LPCTSTR text, int nstr, LPRECT rect, NuclearColor color)
	{
		if (m_pFontMan)
		{
			Nuclear::TextBlockHandle  textureHandle = m_pFontMan->NewText(text, 0, color.data, color.data);
			Nuclear::ITextBlock* textBlock = m_pFontMan->GetTextBlock(textureHandle);
			m_pFontMan->DrawText(textureHandle, (float)rect->left, (float)rect->top, NULL);
			m_pFontMan->ReleaseTextBlock(textureHandle);
			return true;
		}
		return false;
	}

	bool Cocos2dRenderer::DrawTmpText(LPCTSTR text, int nstr, LPRECT rect, NuclearColor color)
	{
		return DrawText(text, nstr, rect, color);
	}
    
	bool Cocos2dRenderer::GetPictureData(PictureHandle handle, void *data, int size, const NuclearRect* pRect)
	{
		return true;
	}
    
	bool Cocos2dRenderer::SetPictureData(PictureHandle handle, const void *data, int size, const NuclearRect* pRect)
	{
        if(data==NULL) return false;
        
		PictureMap::iterator it = m_mapPictures.find(handle);
		if( it == m_mapPictures.end() ) return false;
        
		NuclearRect rect(0,0,it->second.m_info.m_nPicWidth,it->second.m_info.m_nPicHeight);
		if( pRect )
		{
			if( pRect->left > pRect->right || pRect->top > pRect->bottom
               || pRect->left < 0  || pRect->top < 0
               || pRect->right > rect.right || pRect->bottom > rect.bottom 
               )
				return false;
			rect = *pRect;
		}
        
		if( it->second.m_info.m_texfmt != XPTEXFMT_A4R4G4B4 && it->second.m_info.m_texfmt != XPTEXFMT_A8R8G8B8 ) 
		{
			if( size < 4 * rect.Width() * rect.Height() )
				return false;
		}
        
		bool tex16 = it->second.m_info.m_texfmt == XPTEXFMT_A4R4G4B4;
		
		if( size < ( tex16 ? 2 : 4) * rect.Width() * rect.Height() )
			return false;
        
		int n = (int)(it->second.m_CTextureInfoVector.size());
        
		for(int i=0; i<n; i++)
		{
			CTextureInfo texInfo = it->second.m_CTextureInfoVector[i];

            if (texInfo.m_pTexture==NULL)
            {
                cocos2d::CCTexture2D* pTexture = new cocos2d::CCTexture2D();
                
				bool bSuc = pTexture->initWithData(NULL, GetCCPixelFormatFromXP(it->second.m_info.m_texfmt), texInfo.rect.Width(), texInfo.rect.Height(), cocos2d::CCSize((float)texInfo.rect.Width(), (float)texInfo.rect.Height()));
                
                if (bSuc) {
                    texInfo.m_pTexture=pTexture;
                    it->second.m_CTextureInfoVector[i].m_pTexture=pTexture;
                }
            }
           
           {
                glPixelStorei(GL_UNPACK_ALIGNMENT,4);
                cocos2d::ccGLBindTexture2D(texInfo.m_pTexture->getName());
                //glBindTexture(GL_TEXTURE_2D, texInfo.m_pTexture->getName());
                
                glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR );
                glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR );
                glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE );
                glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE );
                
                //copy buffer content
                unsigned char* buf = NULL;
                size_t iStride = 4;

                buf = new unsigned char[(size_t)(rect.Width() * rect.Height() * iStride)];
                memset(buf, 0, rect.Width() * rect.Height() * iStride);
                
                size_t iRowSize = iStride * (size_t)rect.Width();
                for (int row = rect.top; row < rect.bottom; row ++)
				{
                    memcpy(buf + (size_t)((row - rect.top) * iRowSize), ((unsigned char*)data) +  (size_t)((row - rect.top) * iRowSize),iRowSize);
                    //memset(buf + (size_t)((row - rect.top) * iRowSize), 0xFF, iRowSize);
                }
                
                switch(it->second.m_info.m_texfmt)
                {
                    case XPTEXFMT_A8R8G8B8:
                        glTexSubImage2D(GL_TEXTURE_2D, 0, (size_t)rect.left, (size_t)rect.top, (size_t)rect.Width(), (size_t)rect.Height(), GL_RGBA, GL_UNSIGNED_BYTE, buf);
                        break;
                    default:
                      break;
                }
                delete[] buf;
                return true;
            }
		}
		return false;
	}
    
	PictureHandle Cocos2dRenderer::LoadPicture(const std::wstring& filename, NuclearPictureInfo* pPicinfo)
	{
		NuclearBuffer os;
		if( m_pFileIOMan->GetFileImage(filename, os))
		{
			PictureHandle handle = 0;
			if (GetPictureHandle(handle, filename))
			{
				return handle;
			}

            NuclearPictureInfo picinfo;
            if(!pPicinfo)
            {
                picinfo.fileuri = filename;
            }
			else
			{
				pPicinfo->fileuri = filename;
			}
			return LoadPictureFromMem(os.constbegin(), (int)os.size(), pPicinfo?pPicinfo:&picinfo);
		}
		return INVALID_PICTURE_HANDLE;
	}
    
    static char gPVRTexIdentifier[5] = "PVR!";
    
    struct AndroidHeader
    {
        unsigned int sig;
        unsigned int width;
        unsigned int height;
        unsigned int format;
    };

	bool Cocos2dRenderer::GetCreateTextureFormatFromFileInMemory(const void *data, int size, XPIMAGE_INFO& info)
	{
		if (size < 2)
			return false;
		const unsigned short* pData = (const unsigned short*)data;
        switch (*pData)
		{
            case 0xD8FF://jpg
                info.ImageFileFormat = XPIFF_JPG;
                info.Format = IsMode16()?XPTEXFMT_A4R4G4B4:XPTEXFMT_A8R8G8B8;
                break;
            case 0x5089://png
			{
				if (size < 0x20)
					return false;
				info.ImageFileFormat = XPIFF_PNG;
				const unsigned char *pByte = (const unsigned char*)data;
				pByte += 0x18;
				info.Format = (IsMode16() || (*pByte <= 4))?XPTEXFMT_A4R4G4B4:XPTEXFMT_A8R8G8B8;
			}
                break;
            case 0x4D42://bmp
                if (size < 0xF)
                    return false;
                info.ImageFileFormat = XPIFF_BMP;
                pData += 0xE;
                info.Format = (IsMode16() || (*pData <= 16))?XPTEXFMT_A4R4G4B4:XPTEXFMT_A8R8G8B8;
                break;
            case 0x4444://DDS
			{
				if (size < 0x62)
					return false;
                static const unsigned int DDPF_FOURCC = 0x00000004;
                static const unsigned int DDPF_RGB = 0x00000040;
                
				info.ImageFileFormat = XPIFF_DDS;
				const DDS_HEADER *pHeader = (const DDS_HEADER *)(pData + 2);
				if (pHeader->ddspf.dwFlags & DDPF_FOURCC)
				{
					info.Format = (NuclearTextureFormat)pHeader->ddspf.dwFourCC;
				} else if (pHeader->ddspf.dwFlags & DDPF_RGB)
				{
					if (pHeader->ddspf.dwRGBBitCount == 16)
					{
						info.Format = (pHeader->ddspf.dwABitMask == 0)?XPTEXFMT_R5G6B5:XPTEXFMT_A4R4G4B4;
					} else {
						info.Format = IsMode16()?XPTEXFMT_A4R4G4B4:XPTEXFMT_A8R8G8B8;
					}
				} else {
					info.Format = IsMode16()?XPTEXFMT_A4R4G4B4:XPTEXFMT_A8R8G8B8;
				}
			}
                break;
			case 0x4952://WEBP
			{
				if (size < 12)
					return false;
				else
				{
					// RIFFXXXXWEBP
					unsigned char* pHead = (unsigned char*)pData;
					if (pHead[0] != 0x52		// R
						|| pHead[1] != 0x49		// I
						|| pHead[2] != 0x46		// F
						|| pHead[3] != 0x46		// F
						|| pHead[8] != 0x57		// W
						|| pHead[9] != 0x45		// E
						|| pHead[10] != 0x42	// B
						|| pHead[11] != 0x50)	// P
					{
						return false;
					}
					info.ImageFileFormat = XPIFF_WEBP;
					info.Format = XPTEXFMT_A8R8G8B8;
				}

			}
				break;
            case 0://tga
                info.ImageFileFormat = XPIFF_TGA;
                info.Format = IsMode16()?XPTEXFMT_A4R4G4B4:XPTEXFMT_A8R8G8B8;
                break;
            default:
                return false;
		}
		return true;
	}
    
	PictureHandle Cocos2dRenderer::LoadPictureFromNativePath(const std::string &nativepath)
    {
#ifdef XP_PERFORMANCE
		int64_t tick = Nuclear::GetMilliSeconds();
#endif
		cocos2d::CCTexture2D* d_texture = cocos2d::CCTextureCache::sharedTextureCache()->addImage(nativepath.c_str());

        CTextureInfo TextureInfo;
		CPicInfo PicInfo;
		
        TextureInfo.m_pTexture = d_texture;
		int iImageWidth = 0;
		int iImageHeight = 0;
        
        iImageWidth = d_texture->getPixelsWide();
        iImageHeight = d_texture->getPixelsHigh();
        TextureInfo.rect.left = 0;
        TextureInfo.rect.top = 0;
        TextureInfo.rect.right = iImageWidth;
        TextureInfo.rect.bottom = iImageHeight;
        PicInfo.m_info.m_nPicWidth = iImageWidth;
        PicInfo.m_info.m_nPicHeight = iImageHeight;

		NuclearTextureFormat texfmt = IsMode16() ? XPTEXFMT_A4R4G4B4 : XPTEXFMT_A8R8G8B8;
		if (0 != GenTexture(texfmt, XPPOOL_MANAGED, TextureInfo))
			return INVALID_PICTURE_HANDLE;
        
        PicInfo.m_CTextureInfoVector.push_back(TextureInfo);
        m_mapPictures.insert(std::make_pair(m_picHandleSeed, PicInfo));
        
#ifdef XP_PERFORMANCE
		int deltaT = int(Nuclear::GetMilliSeconds() - tick);
		m_performance.m_iTotalLoadTimePerFrame += deltaT;
#endif
		return m_picHandleSeed++;
    }    
    
    void Cocos2dRenderer::CachePicture(PictureHandle pic, const std::wstring &filename)
    {
#if CC_ENABLE_CACHE_TEXTURE_DATA
#ifdef TEST_CACHE_MEM        
        CPicInfo pi;
        PictureMap::iterator it = m_mapPictures.find(pic);LoadPictureFromMem
        if (it != m_mapPictures.end()) {
            pi = it->second;
            
            CTextureInfo ti = pi.m_CTextureInfoVector[0];
            cocos2d::CCImage::EImageFormat ccfmt = cocos2d::CCImage::kFmtTga;
            switch (pi.m_info.m_texfmt) {
                case XPIFF_DDS:
                    ccfmt = cocos2d::CCImage::kFmtDDS;
                    break;
                case XPIFF_JPG:
                    ccfmt = cocos2d::CCImage::kFmtJpg;
                    break;
                case XPIFF_PNG:
                    ccfmt = cocos2d::CCImage::kFmtPng;
                    break;
                case XPIFF_TGA:
                    ccfmt = cocos2d::CCImage::kFmtTga;
                    break;
                default:
                    ccfmt = cocos2d::CCImage::kFmtPng;
            }
            cocos2d::VolatileTexture::addImageTexture(ti.m_pTexture, ws2s(filename).c_str(), ccfmt);
        }
#endif   
#endif        
    }

	PictureHandle Cocos2dRenderer::LoadPictureFromMem(const void *data, int size, NuclearPictureInfo *pPicInfo, NuclearTextureFormat texfmt, NuclearPoolType pooltype, bool bCache/* = true*/, PictureHandle handle, int iScale/* = 100*/)
	{
#ifdef XP_PERFORMANCE
		int64_t tick = Nuclear::GetMilliSeconds();
#endif
		//首先判断在内存是否已经存在此图片 by eagle
		//PictureHandle hRet = 0;
		//if (GetPictureHandle(hRet, pPicInfo->fileuri))
		//{
		//	return hRet;
		//}

		if( data == NULL || size <= 0 )
		{
			data = m_errTexBuf.constbegin();
			size = (int)m_errTexBuf.size();
		}

		if( texfmt != XPTEXFMT_DEFAULT )
		{
			if( !IsTexfmtSupport(texfmt) )
				texfmt = XPTEXFMT_DEFAULT;
		}
        
        XPIMAGE_INFO fileinfo;
        fileinfo.ImageFileFormat = XPIFF_FORCE_DWORD;
        fileinfo.Format = XPTEXFMT_DEFAULT;

//etc begin //etc for ios
//===========================================
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#else
        unsigned char* etcheader_ptr = (unsigned char*)data;
        if(size>=3 && etcheader_ptr[0]=='P' && etcheader_ptr[1]=='K' && etcheader_ptr[2]=='M')
        {
            MaliSDK::ETCHeader etcheader = MaliSDK::ETCHeader((unsigned char*)etcheader_ptr);
            
            cocos2d::CCTexture2D* d_texture = NULL;
            if(d_texture != NULL)
            {
                delete d_texture;
            }
            
            d_texture = new cocos2d::CCTexture2D();
            
            bool bIsETC=false;

            d_texture->initWithETCData(((unsigned char *)data), 0, 8, true,int(etcheader.getWidth()),int(etcheader.getHeight()), cocos2d::kCCTexture2DPixelFormat_ETC);
            bIsETC=true;
            
            CTextureInfo TextureInfo;
            CPicInfo PicInfo;
            
            TextureInfo.m_pTexture = d_texture;
            int iImageWidth = 0;
            int iImageHeight = 0;
            
			iImageWidth = d_texture->getPixelsWide();
			iImageHeight = d_texture->getPixelsHigh();
			TextureInfo.rect.left = 0;
			TextureInfo.rect.top = 0;
			TextureInfo.rect.right = iImageWidth;
			TextureInfo.rect.bottom = iImageHeight;
			if (0 != GenTexture(texfmt, XPPOOL_MANAGED, TextureInfo))
				return INVALID_PICTURE_HANDLE;

			PicInfo.m_info.m_nPicWidth = iImageWidth;
			PicInfo.m_info.m_nPicHeight = iImageHeight;
			PicInfo.m_info.m_pooltype = pooltype;
			PicInfo.m_info.m_texfmt = texfmt;

			if( pPicInfo )
			{
				TextureInfo.fileuri = pPicInfo->fileuri;
				PicInfo.m_info.fileuri = pPicInfo->fileuri;
				if(d_texture)
				{
					d_texture->setDataFileUri(pPicInfo->fileuri);
				}
				*pPicInfo = PicInfo.m_info;
			}
            
			PictureHandle hPicture = (handle == 0) ? m_picHandleSeed : handle;
			PicInfo.m_CTextureInfoVector.push_back(TextureInfo);
			m_mapPictures[hPicture] =  PicInfo;
            
#if CC_ENABLE_CACHE_TEXTURE_DATA
#ifdef TEST_CACHE_MEM
            cocos2d::VolatileTexture::addETCImage(d_texture,data,size,0);
#endif
#endif
#ifdef XP_PERFORMANCE
			unsigned int deltaT = Nuclear::GetMilliSeconds() - tick;
			m_performance.m_iTotalLoadTimePerFrame += deltaT;
			m_performance.m_iLoadTextureTimePerFrame += deltaT;
#endif
			return (handle==0)?(m_picHandleSeed++):handle;
        }
#endif
		//etc end
		//===========================================		
        
		//pvr begin //pvr for ios
		//===========================================
        ccPVRv3TexHeader *header = (ccPVRv3TexHeader *)data;
        
		if (CC_SWAP_INT32_BIG_TO_HOST(header->version) == 0x50565203)
		{
			cocos2d::CCTexture2D* d_texture = NULL;
			if (d_texture != NULL)
			{
				delete d_texture;
			}

			d_texture = new cocos2d::CCTexture2D();
			fileinfo.ImageFileFormat = XPIFF_PVR4;

			d_texture->initWithPVRTCData(((unsigned char *)data) + (sizeof(ccPVRv3TexHeader) + header->metadataLength), 0, fileinfo.ImageFileFormat == XPIFF_PVR4 ? 4 : 2, true, CC_SWAP_INT32_LITTLE_TO_HOST(header->width), fileinfo.ImageFileFormat == XPIFF_PVR4 ? cocos2d::kCCTexture2DPixelFormat_PVRTC4 : cocos2d::kCCTexture2DPixelFormat_PVRTC2);

			CTextureInfo TextureInfo;
			CPicInfo PicInfo;

			TextureInfo.m_pTexture = d_texture;
			int iImageWidth = 0;
			int iImageHeight = 0;

			iImageWidth = d_texture->getPixelsWide();
			iImageHeight = d_texture->getPixelsHigh();
			TextureInfo.rect.left = 0;
			TextureInfo.rect.top = 0;
			TextureInfo.rect.right = iImageWidth;
			TextureInfo.rect.bottom = iImageHeight;
			if (0 != GenTexture(texfmt, XPPOOL_MANAGED, TextureInfo))
				return INVALID_PICTURE_HANDLE;

			PicInfo.m_info.m_nPicWidth = iImageWidth;
			PicInfo.m_info.m_nPicHeight = iImageHeight;
			PicInfo.m_info.m_pooltype = pooltype;
			PicInfo.m_info.m_texfmt = texfmt;

			if (pPicInfo)
			{
				TextureInfo.fileuri = pPicInfo->fileuri;
				PicInfo.m_info.fileuri = pPicInfo->fileuri;
				if (d_texture)
				{
					d_texture->setDataFileUri(pPicInfo->fileuri);
				}
				*pPicInfo = PicInfo.m_info;
			}

			PictureHandle hPicture = (handle == 0) ? m_picHandleSeed : handle;
			PicInfo.m_CTextureInfoVector.push_back(TextureInfo);
			m_mapPictures[hPicture] = PicInfo;

#if CC_ENABLE_CACHE_TEXTURE_DATA
#ifdef TEST_CACHE_MEM
			if(bIsETC)
			{
				cocos2d::VolatileTexture::addETCImage(d_texture,data,size,0);
			}
			else
			{
				cocos2d::VolatileTexture::addPVRTCImage(d_texture, data, size);
			}
#endif       
#endif
            
#ifdef XP_PERFORMANCE
			int deltaT = int(Nuclear::GetMilliSeconds() - tick);
			m_performance.m_iTotalLoadTimePerFrame += deltaT;
			m_performance.m_iLoadTextureTimePerFrame += deltaT;
#endif
			return (handle == 0) ? (m_picHandleSeed++) : handle;
        }
		//pvr begin
		//===========================================

		//atc begin //atc for android
		//===========================================
        AndroidHeader* pAH = (AndroidHeader*)data;
        if (pAH->sig == 0x11111111)
		{
            cocos2d::CCTexture2D* d_texture = NULL;
            
            d_texture = new cocos2d::CCTexture2D();
            d_texture->initWithATCData(((unsigned char *)data) + 4*sizeof(unsigned int), 0, 8, true, pAH->width, pAH->height,cocos2d::kCCTexture2DPixelFormat_ATC_Explicit);
            CTextureInfo TextureInfo;
            CPicInfo PicInfo;
            
            TextureInfo.m_pTexture = d_texture;
            int iImageWidth = 0;
            int iImageHeight = 0;
            
			iImageWidth = d_texture->getPixelsWide();
			iImageHeight = d_texture->getPixelsHigh();
			TextureInfo.rect.left = 0;
			TextureInfo.rect.top = 0;
			TextureInfo.rect.right = iImageWidth;
			TextureInfo.rect.bottom = iImageHeight;
			if (0 != GenTexture(texfmt, XPPOOL_MANAGED, TextureInfo))
				return INVALID_PICTURE_HANDLE;

			PicInfo.m_info.m_nPicWidth = iImageWidth;
			PicInfo.m_info.m_nPicHeight = iImageHeight;
			PicInfo.m_info.m_pooltype = pooltype;
			PicInfo.m_info.m_texfmt = texfmt;

			if (pPicInfo)
			{
				TextureInfo.fileuri = pPicInfo->fileuri;
				PicInfo.m_info.fileuri = pPicInfo->fileuri;
				if (d_texture)
				{
					d_texture->setDataFileUri(pPicInfo->fileuri);
				}
				*pPicInfo = PicInfo.m_info;
			}
            
			PictureHandle hPicture = (handle == 0) ? m_picHandleSeed : handle;
			PicInfo.m_CTextureInfoVector.push_back(TextureInfo);
			m_mapPictures[hPicture] =  PicInfo;
            
#if CC_ENABLE_CACHE_TEXTURE_DATA
 #ifdef TEST_CACHE_MEM            
            cocos2d::VolatileTexture::addAtcImage(d_texture,data,size );
#endif
#endif
#ifdef XP_PERFORMANCE
			int deltaT = int(Nuclear::GetMilliSeconds() - tick);
			m_performance.m_iTotalLoadTimePerFrame += deltaT;
			m_performance.m_iLoadTextureTimePerFrame += deltaT;
#endif
			return (handle == 0) ? (m_picHandleSeed++) : handle;
        }
		//act begin 
		//===========================================        

		//dx begin
		//===========================================
        if(!GetCreateTextureFormatFromFileInMemory(data, size, fileinfo))
            return INVALID_PICTURE_HANDLE;

		if( texfmt == XPTEXFMT_DEFAULT )
		{			
			if(!GetCreateTextureFormatFromFileInMemory(data, size, fileinfo))
				return INVALID_PICTURE_HANDLE;
			
			texfmt = fileinfo.Format;
			if( fileinfo.ImageFileFormat == XPIFF_DDS ) // dds
			{
				if( m_bTextureCaps_pow2 || !IsTexfmtSupport((NuclearTextureFormat)fileinfo.Format) )
					texfmt = XPTEXFMT_A4R4G4B4;
			}
		}

		//normal picture 
		//===========================================
#if (defined ANDROID) || (defined WIN32 || defined _WIN32)
        cocos2d::CCImage* image = new cocos2d::CCImage();
#else
        cocos2d::CCImage image;       
#endif
        cocos2d::CCTexture2D* d_texture = NULL;
        cocos2d::CCImage::EImageFormat ccfmt = cocos2d::CCImage::kFmtPng;
        switch (fileinfo.ImageFileFormat)
		{
            case XPIFF_DDS:
                ccfmt = cocos2d::CCImage::kFmtDDS;
                break;
            case XPIFF_JPG:
                ccfmt = cocos2d::CCImage::kFmtJpg;
                break;
            case XPIFF_PNG:
                ccfmt = cocos2d::CCImage::kFmtPng;
                break;
            case XPIFF_TGA:
                ccfmt = cocos2d::CCImage::kFmtTga;
				break;
			case XPIFF_WEBP:
				ccfmt = cocos2d::CCImage::kFmtWebP;
				break;
            default:
                ccfmt = cocos2d::CCImage::kFmtPng;
        }
#if (defined ANDROID) || (defined WIN32 || defined _WIN32)
        if(image->initWithImageData((void*)data, size, ccfmt))
        {
            if(d_texture != NULL)
            {
                delete d_texture;
            }
            
            cocos2d::CCTexture2DPixelFormat pixfmt;
            if(image->hasAlpha())
            {
                pixfmt = cocos2d::kCCTexture2DPixelFormat_RGBA8888;
            }else
            {
                pixfmt = cocos2d::kCCTexture2DPixelFormat_RGB888;
            }
           
            d_texture = new cocos2d::CCTexture2D();
			d_texture->initWithData(image->getData(), pixfmt, image->getWidth(), image->getHeight(), cocos2d::CCSize(image->getWidth(), image->getHeight()));
            
#if CC_ENABLE_CACHE_TEXTURE_DATA
 #ifdef TEST_CACHE_MEM  
            if (bCache) {
                cocos2d::VolatileTexture::addCCImage(d_texture, image);
                image->release();
            }      
#endif
#endif      
        }
#else
        if(image.initWithImageData((void*)data, size, ccfmt))
        {
            if(d_texture != NULL)
            {
                delete d_texture;
            }
            
            cocos2d::CCTexture2DPixelFormat pixfmt;
            if(image.hasAlpha())
            {
                pixfmt = cocos2d::kCCTexture2DPixelFormat_RGBA8888;
            }else
            {
                pixfmt = cocos2d::kCCTexture2DPixelFormat_RGB888;
            }
            
            d_texture = new cocos2d::CCTexture2D();
			d_texture->initWithData(image.getData(), pixfmt, image.getWidth(), image.getHeight(), cocos2d::CCSize(image.getWidth(), image.getHeight()));
        }
#endif
        
		CTextureInfo TextureInfo;
		CPicInfo PicInfo;
		
        TextureInfo.m_pTexture = d_texture;
		int iImageWidth = 0;
		int iImageHeight = 0;
        
#if (defined ANDROID) || (defined WIN32 || defined _WIN32)
		iImageWidth = image->getWidth();
		iImageHeight = image->getHeight();
		#if CC_ENABLE_CACHE_TEXTURE_DATA
			if (!bCache)
				delete image;
		#else
			delete image;
		#endif
#else
        iImageWidth = image.getWidth();
        iImageHeight = image.getHeight();
#endif
		TextureInfo.rect.left = 0;
		TextureInfo.rect.top = 0;
		TextureInfo.rect.right = iImageWidth;
		TextureInfo.rect.bottom = iImageHeight;
		if( 0 != GenTexture(texfmt, XPPOOL_MANAGED, TextureInfo) ) 
			return INVALID_PICTURE_HANDLE;            

		PicInfo.m_info.m_nPicWidth = iImageWidth;
		PicInfo.m_info.m_nPicHeight = iImageHeight;
		PicInfo.m_info.m_pooltype = pooltype;
		PicInfo.m_info.m_texfmt = texfmt;

		if (pPicInfo)
		{
			TextureInfo.fileuri = pPicInfo->fileuri;
			PicInfo.m_info.fileuri = pPicInfo->fileuri;
			if (d_texture)
			{
				d_texture->setDataFileUri(pPicInfo->fileuri);
			}
			*pPicInfo = PicInfo.m_info;
		}
            
		PictureHandle hPicture = (handle == 0) ? m_picHandleSeed : handle;
		PicInfo.m_CTextureInfoVector.push_back(TextureInfo);
		m_mapPictures[hPicture] =  PicInfo;			

#ifdef XP_PERFORMANCE
		int deltaT = int(Nuclear::GetMilliSeconds() - tick);
		m_performance.m_iTotalLoadTimePerFrame += deltaT;
		m_performance.m_iLoadTextureTimePerFrame += deltaT;
#endif
		return (handle == 0) ? m_picHandleSeed++ : handle;
	}

	bool Cocos2dRenderer::LoadCCImageFromMem(cocos2d::CCImage* pImage, NuclearTextureFormat texfmt, const void *data, int size)
	{
		if (!pImage)
		{
			return false;
		}

		//首先判断在内存是否已经存在此图片 by eagle
		//PictureHandle hRet = 0;
		//if (GetPictureHandle(hRet, pPicInfo->fileuri))
		//{
		//	return hRet;
		//}

		if (data == NULL || size <= 0)
		{
			data = m_errTexBuf.constbegin();
			size = (int)m_errTexBuf.size();
		}

		if (texfmt != XPTEXFMT_DEFAULT)
		{
			if (!IsTexfmtSupport(texfmt))
				texfmt = XPTEXFMT_DEFAULT;
		}

		XPIMAGE_INFO fileinfo;
		fileinfo.ImageFileFormat = XPIFF_FORCE_DWORD;
		fileinfo.Format = XPTEXFMT_DEFAULT;

		//etc begin //etc for ios
		//===========================================
#if (defined WIN7_32) || (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
#else
		unsigned char* etcheader_ptr = (unsigned char*)data;
		if (size >= 3 && etcheader_ptr[0] == 'P' && etcheader_ptr[1] == 'K' && etcheader_ptr[2] == 'M')
		{
			return false;
		}
#endif
		//etc end
		//===========================================		

		//pvr begin //pvr for ios
		//===========================================
		ccPVRv3TexHeader *header = (ccPVRv3TexHeader *)data;

		if (CC_SWAP_INT32_BIG_TO_HOST(header->version) == 0x50565203)
		{
			return false;
		}
		//pvr end
		//===========================================

		//atc begin //atc for android
		//===========================================
		AndroidHeader* pAH = (AndroidHeader*)data;
		if (pAH->sig == 0x11111111)
		{
			return false;
		}
		//act end 
		//===========================================        

		//dx begin
		//===========================================
		if (!GetCreateTextureFormatFromFileInMemory(data, size, fileinfo))
			return false;

		if (texfmt == XPTEXFMT_DEFAULT)
		{
			if (!GetCreateTextureFormatFromFileInMemory(data, size, fileinfo))
				return false;

			texfmt = fileinfo.Format;
			if (fileinfo.ImageFileFormat == XPIFF_DDS) // dds
			{
				if (m_bTextureCaps_pow2 || !IsTexfmtSupport((NuclearTextureFormat)fileinfo.Format))
					texfmt = XPTEXFMT_A4R4G4B4;
			}
		}

		//normal picture 
		//===========================================

		cocos2d::CCImage::EImageFormat ccfmt = cocos2d::CCImage::kFmtPng;
		switch (fileinfo.ImageFileFormat)
		{
		case XPIFF_DDS:
			ccfmt = cocos2d::CCImage::kFmtDDS;
			break;
		case XPIFF_JPG:
			ccfmt = cocos2d::CCImage::kFmtJpg;
			break;
		case XPIFF_PNG:
			ccfmt = cocos2d::CCImage::kFmtPng;
			break;
		case XPIFF_TGA:
			ccfmt = cocos2d::CCImage::kFmtTga;
			break;
		case XPIFF_WEBP:
			ccfmt = cocos2d::CCImage::kFmtWebP;
			break;
		default:
			ccfmt = cocos2d::CCImage::kFmtPng;
		}

		return pImage->initWithImageData((void*)data, size, ccfmt);
	}

	PictureHandle Cocos2dRenderer::LoadPictureFromCCImage(cocos2d::CCImage* image, NuclearTextureFormat texfmt, NuclearPictureInfo *pPicInfo/* = NULL*/, NuclearPoolType pooltype/* = XPPOOL_MANAGED*/, bool bCache/* = true*/, PictureHandle handle/* = 0*/)
	{
#ifdef XP_PERFORMANCE
		int64_t tick = Nuclear::GetMilliSeconds();
#endif

		cocos2d::CCTexture2DPixelFormat pixfmt;
		if (image->hasAlpha())
		{
			pixfmt = cocos2d::kCCTexture2DPixelFormat_RGBA8888;
		}
		else
		{
			pixfmt = cocos2d::kCCTexture2DPixelFormat_RGB888;
		}

		cocos2d::CCTexture2D* d_texture = new cocos2d::CCTexture2D();

		unsigned char* pData = image->getData();
		unsigned short width = image->getWidth();
		unsigned short height = image->getHeight();
		d_texture->initWithData(pData, pixfmt, width, height, cocos2d::CCSize((float)width, (float)height));

#if CC_ENABLE_CACHE_TEXTURE_DATA
#ifdef TEST_CACHE_MEM  
		if (bCache) {
			cocos2d::VolatileTexture::addCCImage(d_texture, image);
		}
#endif
#endif

		CTextureInfo TextureInfo;
		CPicInfo PicInfo;

		TextureInfo.m_pTexture = d_texture;
		int iImageWidth = 0;
		int iImageHeight = 0;

		iImageWidth = image->getWidth();
		iImageHeight = image->getHeight();

		TextureInfo.rect.left = 0;
		TextureInfo.rect.top = 0;
		TextureInfo.rect.right = iImageWidth;
		TextureInfo.rect.bottom = iImageHeight;
		if (0 != GenTexture(texfmt, XPPOOL_MANAGED, TextureInfo))
			return INVALID_PICTURE_HANDLE;

		PicInfo.m_info.m_nPicWidth = iImageWidth;
		PicInfo.m_info.m_nPicHeight = iImageHeight;
		PicInfo.m_info.m_pooltype = pooltype;
		PicInfo.m_info.m_texfmt = texfmt;

		if (pPicInfo)
		{
			TextureInfo.fileuri = pPicInfo->fileuri;
			PicInfo.m_info.fileuri = pPicInfo->fileuri;
			if (d_texture)
			{
				d_texture->setDataFileUri(pPicInfo->fileuri);
			}
			*pPicInfo = PicInfo.m_info;
		}

		PictureHandle hPicture = (handle == 0) ? m_picHandleSeed : handle;
		PicInfo.m_CTextureInfoVector.push_back(TextureInfo);
		m_mapPictures[hPicture] = PicInfo;

#ifdef XP_PERFORMANCE
		int deltaT = int(Nuclear::GetMilliSeconds() - tick);
		m_performance.m_iTotalLoadTimePerFrame += deltaT;
		m_performance.m_iLoadTextureTimePerFrame += deltaT;
#endif
		return (handle == 0) ? m_picHandleSeed++ : handle;
	}
	
	bool Cocos2dRenderer::IsTextureExist(PictureHandle handle)
	{
		PictureMap::iterator it = m_mapPictures.find(handle);
		if (it == m_mapPictures.end()) {
			return false;
		}
		return true;
	}
    
	bool Cocos2dRenderer::IsTextureEtc(PictureHandle handle)
    {
        PictureMap::iterator it = m_mapPictures.find(handle);
        if (it == m_mapPictures.end()) {
            return false;
        }
        
        CTextureInfo texInfo = it->second.m_CTextureInfoVector[0];
        if (texInfo.m_pTexture == NULL) {
            return false;
        }
        return texInfo.m_pTexture->isEtcTexture();
    }

	bool Cocos2dRenderer::DrawBatchPicture(const DrawBatchPictureParam &param)
	{
		if(param.handle == INVALID_PICTURE_HANDLE)
			return false;
        
		if(param.pRect == NULL || param.rectCount <= 0 || param.rectCount * 4 > BATCH_VB_COUNT )
			return false;
        
		PictureMap::iterator it = m_mapPictures.find(param.handle);
		PictureMap::iterator secIt = m_mapPictures.find(param.secHandle);
		if( it == m_mapPictures.end() )
			return false;

		bool hasSecTex = (param.secHandle != INVALID_PICTURE_HANDLE);
		if (hasSecTex)
		{
			XPASSERT(secIt->second.m_CTextureInfoVector.size() == 1);
		}

		int a = (int)(it->second.m_CTextureInfoVector.size()); 

		switch (param.blend)
		{
            case XPTEXBM_MODULATE:
                SetTextureColorBlendType(XPTOP_DEFAULT);
                SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
                SetAlphaBlendDstType(XPBLEND_INVSRCALPHA);
                break;
            case XPTEXBM_ADDITIVE:
                SetTextureColorBlendType(XPTOP_DEFAULT);
                SetAlphaBlendSrcType(XPBLEND_ONE);
                SetAlphaBlendDstType(XPBLEND_ONE);
                break;
            case XPTEXBM_COPY:
                SetTextureColorBlendType(XPTOP_DEFAULT);
                SetAlphaBlendSrcType(XPBLEND_ONE);
                SetAlphaBlendDstType(XPBLEND_ZERO);
                break;
            case XPTEXBM_ADDITIVE_ALPHA:
                SetTextureColorBlendType(XPTOP_DEFAULT);
                SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
                SetAlphaBlendDstType(XPBLEND_ONE);
                break;
            case XPTEXBM_MODULATE2X:
                SetTextureColorBlendType(XPTOP_MODULATE2X);
                SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
                SetAlphaBlendDstType(XPBLEND_INVSRCALPHA);
                break;
            case XPTEXBM_ADDITIVE2X:
                SetTextureColorBlendType(XPTOP_MODULATE2X);
                SetAlphaBlendSrcType(XPBLEND_ONE);
                SetAlphaBlendDstType(XPBLEND_ONE);
                break;
            case XPTEXBM_MUTIPLY:
                SetTextureColorBlendType(XPTOP_DEFAULT);
                SetAlphaBlendSrcType(XPBLEND_DESTCOLOR);
                SetAlphaBlendDstType(XPBLEND_ZERO);
                break;
            case XPTEXBM_MUTIPLY_ALPHA:
                SetTextureColorBlendType(XPTOP_DEFAULT);
                SetAlphaBlendSrcType(XPBLEND_DESTCOLOR);
                SetAlphaBlendDstType(XPBLEND_INVSRCALPHA);
                break;
		}
		SetSeparateAlphaBlend(param.bSpecAlpha);
		m_stateMan.EnableSecondTex(hasSecTex);
        
		NuclearVector2 vecOut;
		NuclearVector2 thePos[4];
        
		NuclearFRectt srcrect(.0f, .0f, 1.0f, 1.0f);
		for (int i = 0; i < a; i++)
		{
			volatile int nPicWidth = it->second.m_info.m_nPicWidth;
			volatile int nPicHeight = it->second.m_info.m_nPicHeight;
			CTextureInfo texInfo = it->second.m_CTextureInfoVector[i];

			volatile float texLeft = (float)texInfo.rect.left;
			volatile float texTop = (float)texInfo.rect.top;
			volatile float texW = (float)texInfo.rect.Width();
			volatile float texH = (float)texInfo.rect.Height();

			////////////////////////////////////////
			float lx = texLeft / nPicWidth;
			float ty = texTop / nPicHeight;
			float rx = (texLeft + texW) / nPicWidth;
			float by = (texTop + texH) / nPicHeight;
			float rx2 = 0;//(texLeft + texRealW)/nPicWidth;
			float by2 = 0;//(texTop + texRealH)/nPicHeight;
		}
            
		return true;
	}

	bool Cocos2dRenderer::GetTextData(const char* pChar, const std::string &font, int size, unsigned char*& pData, int& w, int& h)
	{
#if (defined ANDROID) || (defined WIN32 || defined _WIN32)
		cocos2d::CCImage* image = new cocos2d::CCImage();
		image->initWithString(pChar, size, size, cocos2d::CCImage::kAlignCenter, font.c_str(), size);
		w = image->getWidth();
		h = image->getHeight();
		pData = new unsigned char[w*h*sizeof(unsigned int)];
		memcpy(pData, image->getData(), image->getDataLen()*sizeof(unsigned int));
		delete image;
#else
		cocos2d::CCImage image;
		image.initWithString(pChar, size, size, cocos2d::CCImage::kAlignCenter, font.c_str(), size);
		w = image.getWidth();
		h = image.getHeight();
		pData = new unsigned char[w*h*sizeof(unsigned int)];
		memcpy(pData, image.getData(), image.getDataLen()*sizeof(unsigned int));
#endif
		return true;
	}
	void Cocos2dRenderer::ResetAllTextureUName()
    {
        PictureMap::iterator it = m_mapPictures.begin();
        for (; it != m_mapPictures.end(); it++) {
            CPicInfo* pic = &(it->second);
            std::vector<CTextureInfo>* textureVec = &(pic->m_CTextureInfoVector);
            std::vector<CTextureInfo>::iterator tx = textureVec->begin();
            for (; tx != textureVec->end(); tx++) {
                cocos2d::CCTexture2D* texture = tx->m_pTexture;
                if(NULL == texture)continue;
                std::wstring path = texture->getDataFileUri();
                if(path.length() > 0 && path.find_first_of(L"/") == 0)
                {
                    tx->fileuri = path;
                    CC_SAFE_DELETE(texture);
                    tx->m_pTexture = NULL;
                }
                else if (path.length() == 0)
                {
                    
                }
            }
        }
    }
    
    void Cocos2dRenderer::OnReloadAllTexture()
    {
        LOGD("Cocos2dRenderer::OnReloadAllTexture \n");

		PictureMap::iterator it = m_mapPictures.begin();

        it = m_mapPictures.begin();
        for (; it != m_mapPictures.end(); it++) {
            CPicInfo* pic = &(it->second);
            std::vector<CTextureInfo>* textureVec = &(pic->m_CTextureInfoVector);
            std::vector<CTextureInfo>::iterator tx = textureVec->begin();
            for (; tx != textureVec->end(); tx++) {
                std::wstring path = tx->fileuri;
                if(path.length() > 0 && path.find_first_of(L"/") == 0)
                {
                    NuclearBuffer xb;
                    if(GetEngine()->GetFileIO()->GetFileImage(path, xb))
                    {
                        XPIMAGE_INFO fileinfo;
                        fileinfo.ImageFileFormat = XPIFF_FORCE_DWORD;
                        fileinfo.Format = XPTEXFMT_DEFAULT;
						NuclearPictureInfo pInfo;
						pInfo.fileuri = path;
						LoadPictureFromMem(xb.constbegin(), xb.size(),&pInfo, XPTEXFMT_DEFAULT,XPPOOL_MANAGED, false, it->first);
						break;

                    }else
                    {
                        LOGD("reload texture error: %ls \n", path.c_str());
                    }
                    
                }
                else if (path.length() == 0)
                {
                    
                }
            }
        }
    }
    
	bool Cocos2dRenderer::DrawPicture(PictureHandle picHandle, float left, float top, float right, float bottom)
	{
		Nuclear::DrawPictureParam  pictureParam;
		Nuclear::NuclearFRectt picRect(left, top, right, bottom);
		pictureParam.pRect = &picRect;
		Nuclear::NuclearFRectt srcBackRect(0.0, 0.0f, 1.0f, 1.0f);
		pictureParam.pSrcrect = &srcBackRect;
		pictureParam.handle = picHandle;
		return DrawPicture(pictureParam);
	}

	bool Cocos2dRenderer::DrawPicture(const DrawPictureParam& param)
	{
		PictureMap::iterator it = m_mapPictures.find(param.handle);
		if (it == m_mapPictures.end())
		{
			return false;
		}
		PictureMap::iterator itPart = m_mapPictures.end();
		if (param.handlePart > 0)
		{
			itPart = m_mapPictures.find(param.handlePart);
			if (itPart == m_mapPictures.end())
			{
				return false;
			}
		}

		int a = (int)(it->second.m_CTextureInfoVector.size());
		switch (param.blend)
		{
		case XPTEXBM_MODULATE:
			cocos2d::ccGLBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			break;
		case XPTEXBM_ADDITIVE:
			cocos2d::ccGLBlendFunc(GL_ONE, GL_ONE);
			break;
		case XPTEXBM_COPY:
			cocos2d::ccGLBlendFunc(GL_ONE, GL_ZERO);
			break;
		case XPTEXBM_ADDITIVE_ALPHA:
			cocos2d::ccGLBlendFunc(GL_SRC_ALPHA, GL_ONE);
			break;
		case XPTEXBM_MUTIPLY:
			cocos2d::ccGLBlendFunc(GL_DST_COLOR, GL_ZERO);
			break;
		}

		NuclearVector2 vecOut;
		NuclearVector2 thePos[4];
		for (int i = 0; i < a; i++)
		{
			int nPicWidth = it->second.m_info.m_nPicWidth;
			int nPicHeight = it->second.m_info.m_nPicHeight;
			CTextureInfo texInfo = it->second.m_CTextureInfoVector[i];

			CTextureInfo texInfoPart;
			if (itPart != m_mapPictures.end())
			{
				if (i < (int)itPart->second.m_CTextureInfoVector.size())
				{
					texInfoPart = itPart->second.m_CTextureInfoVector[i];
				}
			}
			
			float texLeft = (float)texInfo.rect.left;
			float texTop = (float)texInfo.rect.top;
			float texW = (float)texInfo.rect.Width();
			float texH = (float)texInfo.rect.Height();

			if (!texInfo.m_pTexture)
				continue;
			float texRealW = (float)texInfo.m_pTexture->getPixelsWide();
			float texRealH = (float)texInfo.m_pTexture->getPixelsHigh();

			float lx = texLeft / nPicWidth;
			float ty = texTop / nPicHeight;
			float rx = (texLeft + texW) / nPicWidth;
			float by = (texTop + texH) / nPicHeight;
			float rx2 = (texLeft + texRealW) / nPicWidth;
			float by2 = (texTop + texRealH) / nPicHeight;


			NuclearFRectt srcrect(.0f, .0f, 1.0f, 1.0f);
			if (param.pSrcrect)
			{
				srcrect = *param.pSrcrect;
			}
			float u1 = std::max(srcrect.left, lx);
			float u2 = std::min(srcrect.right, rx);
			float v1 = std::max(srcrect.top, ty);
			float v2 = std::min(srcrect.bottom, by);
			if ((u1 > u2) || (v1 > v2))
				continue;
			float du = u2 - u1;
			float dv = v2 - v1;
			float rctW = param.pRect->Width();
			float rctH = param.pRect->Height();


			thePos[0].x = param.pRect->left + param.pRect->Width()*(u1 - srcrect.left) / srcrect.Width();
			thePos[0].y = param.pRect->top + param.pRect->Height()*(v1 - srcrect.top) / srcrect.Height();

			thePos[1].x = thePos[0].x;
			thePos[1].y = thePos[0].y + rctH*dv / srcrect.Height();//+0.5f

			thePos[2].x = thePos[0].x + rctW*du / srcrect.Width();//+0.5f
			thePos[2].y = thePos[1].y;

			thePos[3].x = thePos[2].x;
			thePos[3].y = thePos[0].y;

			float tu1 = (u1 - lx) / (rx2 - lx);//+ 0.5f/nPicWidth; // 
			float tv1 = (v1 - ty) / (by2 - ty);//+ 0.5f/nPicHeight;
			float tu2 = (u2 - lx) / (rx2 - lx);//+ 0.5f/nPicWidth;
			float tv2 = (v2 - ty) / (by2 - ty);//+ 0.5f/nPicHeight;

			if (param.pMatrix)
			{
				for (int i = 0; i < 4; ++i)
				{
					XPVec3TransformCoord(&vecOut, thePos + i, param.pMatrix);
					thePos[i] = vecOut;
				}
			}

			unsigned int color[4];

			if (param.pColor == NULL)
			{
				color[0] = color[1] = color[2] = color[3] = 0xFFFFFFFF;
			}
			else
			{
				if (param.colorCount < 4)
				{
					color[0] = color[1] = color[2] = color[3] = param.pColor->data;
				}
				else
				{
					for (int i = 0; i < 4; i++)
					{
						color[i] = param.pColor[i].data;
					}
				}
			}

			GLfloat coordinates[] =
			{
				tu1, tv1,
				tu2, tv1,
				tu1, tv2,
				tu2, tv2,
			};

			GLfloat vertices[] =
			{
				thePos[0].x, thePos[0].y,
				thePos[3].x, thePos[3].y,
				thePos[1].x, thePos[1].y,
				thePos[2].x, thePos[2].y,
			};

			if (!param.bUseBW)
			{
				if (param.iShaderType == 0 || (param.iShaderType == 3 && !texInfoPart.m_pTexture))
				{
					if (texInfo.m_pTexture->isEtcTexture())
					{
						cocos2d::CCShaderCache::sharedShaderCache()->pushShader(kCCShader_PositionTextureColorEtc);
						//cocos2d::ccGLEnableVertexAttribs(cocos2d::kCCVertexAttribFlag_Position | cocos2d::kCCVertexAttribFlag_TexCoords | cocos2d::kCCVertexAttribFlag_Color);
						cocos2d::ccGLActiveTexture(GL_TEXTURE0);
						cocos2d::ccGLBindTexture2D(texInfo.m_pTexture->getName());
						cocos2d::ccGLActiveTexture(GL_TEXTURE1);
						cocos2d::ccGLBindTexture2D(texInfo.m_pTexture->getAlphaName());
					}
					else
					{
						//cocos2d::CCShaderCache::sharedShaderCache()->pushShader(kCCShader_PositionTextureColor);
						//cocos2d::ccGLEnableVertexAttribs(cocos2d::kCCVertexAttribFlag_Position | cocos2d::kCCVertexAttribFlag_TexCoords | cocos2d::kCCVertexAttribFlag_Color);
						cocos2d::ccGLActiveTexture(GL_TEXTURE0);
						cocos2d::ccGLBindTexture2D(texInfo.m_pTexture->getName());
					}
				}
				else //if (param.iShaderType == 1/* && texInfoPart.m_pTexture*/)//HSV
				{
					if (texInfo.m_pTexture->isEtcTexture())
					{
						cocos2d::CCShaderCache::sharedShaderCache()->pushShader(kCCShader_PositionTextureColorEtc);
						//cocos2d::ccGLEnableVertexAttribs(cocos2d::kCCVertexAttribFlag_Position | cocos2d::kCCVertexAttribFlag_TexCoords | cocos2d::kCCVertexAttribFlag_Color);
						cocos2d::ccGLActiveTexture(GL_TEXTURE0);
						cocos2d::ccGLBindTexture2D(texInfo.m_pTexture->getName());
						cocos2d::ccGLActiveTexture(GL_TEXTURE1);
						cocos2d::ccGLBindTexture2D(texInfo.m_pTexture->getAlphaName());
					}
					else
					{
						cocos2d::CCShaderCache::sharedShaderCache()->pushShader(kCCShader_PositionTextureColorHSV);
						cocos2d::CCGLProgram* pProgram = NULL;
						pProgram = cocos2d::CCShaderCache::sharedShaderCache()->programForKey(kCCShader_PositionTextureColorHSV);
						float fH1 = param.pPartParam0[0];
						float fH2= param.pPartParam0[2];
						if (fH1 < 0)
							fH1 += 1;
						if (fH2 < 0)
							fH2 += 1;
						pProgram->setUniformPartParam(0, fH1*360, param.pPartParam0[1], fH2*360, param.pPartParam0[3]);
						pProgram->setUniformPartParam(1, param.pPartParam1[0], param.pPartParam1[1], param.pPartParam1[2], param.pPartParam1[3]);
						pProgram->setUniformPartParam(2, param.pPartParam2[0], param.pPartParam2[1], param.pPartParam2[2], param.pPartParam2[3]);
						
						cocos2d::ccGLEnableVertexAttribs(cocos2d::kCCVertexAttribFlag_Position | cocos2d::kCCVertexAttribFlag_TexCoords | cocos2d::kCCVertexAttribFlag_Color);
						cocos2d::ccGLActiveTexture(GL_TEXTURE0);
						cocos2d::ccGLBindTexture2D(texInfo.m_pTexture->getName());
						if( texInfoPart.m_pTexture != NULL )
						{
							cocos2d::ccGLActiveTexture(GL_TEXTURE2);
							cocos2d::ccGLBindTexture2D(texInfoPart.m_pTexture->getName());
						}
						else
						{
							cocos2d::ccGLActiveTexture(GL_TEXTURE2);
							cocos2d::ccGLBindTexture2D(0);

						}
					}
				}
			}
			else
			{
				cocos2d::CCShaderCache::sharedShaderCache()->pushShader(kCCShader_PositionTextureColorGray);
				//cocos2d::ccGLEnableVertexAttribs(cocos2d::kCCVertexAttribFlag_Position | cocos2d::kCCVertexAttribFlag_TexCoords | cocos2d::kCCVertexAttribFlag_Color);

				cocos2d::ccGLBindTexture2D(texInfo.m_pTexture->getName());

				cocos2d::CCGLProgram* pProgram = cocos2d::CCShaderCache::sharedShaderCache()->programForKey(kCCShader_PositionTextureColorGray);
				GLint ctlLoc = glGetUniformLocation(pProgram->getProgram(), kCCUniformFloatY);
				GLint redLoc = glGetUniformLocation(pProgram->getProgram(), kCCUniformFloatRed);

				pProgram->setUniformLocationWith1f(ctlLoc, (tv2 - tv1)*param.fGreyPercent);
				pProgram->setUniformLocationWith1f(redLoc, (tv2 - tv1)*param.fRedPercent);
			}

			glVertexAttribPointer(cocos2d::kCCVertexAttrib_Position, 2, GL_FLOAT, GL_FALSE, 0, vertices);
			glVertexAttribPointer(cocos2d::kCCVertexAttrib_TexCoords, 2, GL_FLOAT, GL_FALSE, 0, coordinates);
			glVertexAttribPointer(cocos2d::kCCVertexAttrib_Color, 4, GL_UNSIGNED_BYTE, GL_TRUE, 0, color);

			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

			if (texInfo.m_pTexture->isEtcTexture())
			{
				cocos2d::ccGLActiveTexture(GL_TEXTURE0);
			}
			if (!param.bUseBW)
			{
				if (param.iShaderType != 0)
				{
					if (texInfoPart.m_pTexture != NULL)
					{
						cocos2d::ccGLActiveTexture(GL_TEXTURE0);
					}
				}
			}
#ifdef XP_PERFORMANCE
			m_performance.m_iDrawPrimitiveUPCountPerFrame++;
			m_performance.m_iDrawPictureCountPerFrame++;
			m_performance.m_iTotalPixelRenderedPerFrame += int((rctW*du / srcrect.Width())*(rctH*dv / srcrect.Height()));
#endif			
			if (!param.bUseBW)
			{
				if (param.iShaderType == 0 || (param.iShaderType == 3 && !texInfoPart.m_pTexture))
				{
					if (texInfo.m_pTexture->isEtcTexture())
					{
						cocos2d::CCShaderCache::sharedShaderCache()->popShader();
					}
				}
				else //if (param.iShaderType == 1/* && texInfoPart.m_pTexture*/)//HSV
				{
					cocos2d::CCShaderCache::sharedShaderCache()->popShader();
				}
			}
			else
			{
				cocos2d::CCShaderCache::sharedShaderCache()->popShader();
			}
		}
		return true;
	}
    
	bool Cocos2dRenderer::SelectRenderEffect(XPRENDER_EFFECT xpre)
	{
        return true;
	}
    
	XPRENDER_EFFECT Cocos2dRenderer::GetRenderEffect()
	{
		return m_stateMan.GetEffect();
	}
    
	bool Cocos2dRenderer::SetShaderParam(XPRENDER_EFFECT xpre, const void* pParam)
	{
		return m_stateMan.SetShaderParam(xpre, pParam);
	}
    
	bool Cocos2dRenderer::FreePicture(PictureHandle handle)
	{
		PictureMap::iterator it = m_mapPictures.find(handle);
		if( it == m_mapPictures.end() )
		{
			XPLOG_ERROR(L" FreePicture(%d) Failed.\n", handle);
			return false;
		}

		int cnt = (int)(it->second.m_CTextureInfoVector.size());
		for (int i = 0; i < cnt; i++)
		{
			if (it->second.m_CTextureInfoVector[i].m_pTexture)
			{
				delete it->second.m_CTextureInfoVector[i].m_pTexture;
				it->second.m_CTextureInfoVector[i].m_pTexture = NULL;
			}
            
#ifdef XP_PERFORMANCE
			m_performance.m_iManagedTextureCount--;
			m_performance.m_iManagedTextureMemory -= it->second.m_CTextureInfoVector[i].mem;
#endif
		}
        
		m_mapPictures.erase(it);
        
		return true;
	}
    
	int Cocos2dRenderer::GenTexture(NuclearTextureFormat texfmt, NuclearPoolType pooltype, CTextureInfo& TextureInfo)
	{
#ifdef XP_PERFORMANCE
		int npix = TextureInfo.rect.right * TextureInfo.rect.bottom;
		switch( texfmt )
		{
            case XPTEXFMT_A4R4G4B4:
                TextureInfo.mem = 2 * npix;
                break;
            case XPTEXFMT_A8R8G8B8:
                TextureInfo.mem = 4 * npix;
                break;
            case XPTEXFMT_DXT2:
            case XPTEXFMT_DXT3:
            case XPTEXFMT_DXT4:
            case XPTEXFMT_DXT5:
                TextureInfo.mem = npix;
                break;
            case XPTEXFMT_DXT1:
                TextureInfo.mem = npix / 2;
                break;
            default:
                break;
		}

		m_performance.m_iManagedTextureCount++;
		m_performance.m_iManagedTextureMemory += TextureInfo.mem;
#endif
       
		return 0;
	}
    
	bool Cocos2dRenderer::OnLostDevice()
	{
		//ReleaseTexture();
		return true;
	}
    
	void Cocos2dRenderer::SetRestoreTask(INuclearRunnable *pTask)
	{
		m_restoreTasks.push_back(pTask);
	}
    
	void Cocos2dRenderer::SetLostTask(INuclearRunnable *pTask)
	{
		m_lostTasks.push_back(pTask);
	}
    
	bool Cocos2dRenderer::OnResetDevice()
	{
        DestroyBatchVB();
        InitBatchVB();
        m_pParticleMan->OnResetDevice();
		return true;
	}
    
	
	bool Cocos2dRenderer::ResetD3D(const NuclearDisplayMode *newmode)
	{
		return true;
	}
    
	void Cocos2dRenderer::SetScissorRect(const NuclearRect* pRect)
	{
		m_stateMan.SetScissorRect(pRect);
	}
    
	PictureHandle Cocos2dRenderer::GetBackBuffer()
	{		
        return 0;
	}
    
	bool Cocos2dRenderer::GetBackBufferData(void** pBuf, int *pSize, int *pBytePerPix, NuclearRect* pRect, NuclearPixelFormat fmt)
	{
		return true;
	}
    
	bool Cocos2dRenderer::PrintScreen(void** pbuf, int*size,NuclearImageFileFormat filetype,const NuclearRect* src)
	{
		return true;
	}
	
	const void* Cocos2dRenderer::GetTexInfo(PictureHandle handle)
	{
		PictureMap::const_iterator it = m_mapPictures.find(handle);
		if (m_mapPictures.end() != it && !it->second.m_CTextureInfoVector.empty())
		{
			return &(it->second.m_CTextureInfoVector[0]);
		}
        
		return NULL;
	}	
	
	const Cocos2dRenderer::CPicInfo* Cocos2dRenderer::GetPicInfo(PictureHandle handle) const
	{
		PictureMap::const_iterator it = m_mapPictures.find(handle);
		if ( m_mapPictures.end() != it)
			return &(it->second);
        
		return NULL;
	}
	
	bool Cocos2dRenderer::GetPictureInfo(PictureHandle handle, NuclearPictureInfo& picinfo) const
	{
		PictureMap::const_iterator it = m_mapPictures.find(handle);
		if ( m_mapPictures.end() != it)
		{
			picinfo = it->second.m_info;
			return true;
		}
		return false;
	}
    
	bool Cocos2dRenderer::GetPictureInfo(PictureHandle handle, int& pitch, int& width, int& height) const
	{
		PictureMap::const_iterator it = m_mapPictures.find(handle);
		if (m_mapPictures.end() != it)
		{
			width = it->second.m_info.m_nPicWidth;
			height = it->second.m_info.m_nPicHeight;
            
			NuclearTextureFormat texfmt = it->second.m_info.m_texfmt;
			switch( texfmt )
			{
                case XPTEXFMT_A4R4G4B4:
                    pitch = (2 * width +3)&~3;
                    break;
                case XPTEXFMT_A8R8G8B8:
                    pitch = 4 * width;
                    break;
                case XPTEXFMT_DXT2:
                case XPTEXFMT_DXT3:
                case XPTEXFMT_DXT4:
                case XPTEXFMT_DXT5:
                    pitch = 4* ( (width+3)&~3 );
                    break;
                case XPTEXFMT_DXT1:
                    pitch = 2* ( (width+3)&~3 );
                    break;
                default:
                    pitch = 0;
                    break;
			}
            
			return true;
		}
		return false;
	}

	bool Cocos2dRenderer::GetPictureHandle(PictureHandle& handle, const std::wstring& fileuri) const
	{
		PictureMap::const_iterator it = m_mapPictures.begin();
		while (m_mapPictures.end() != it)
		{
			if (it->second.m_info.fileuri.compare(fileuri) == 0)
			{
				handle = it->first;
				return true;
			}
			++it;
		}
		return false;
	}

	bool Cocos2dRenderer::GetPicOutLine(const void *srcdata, int srcsize, std::vector<NuclearPoint>& outLinePoint, bool bflag)
	{
		NuclearFRectt frct(0.0f, 0.0f, 1.0f, 1.0f);
		return GetPicOutLine(srcdata, srcsize, frct, outLinePoint, bflag);
 	}
    
	bool Cocos2dRenderer::GetPicOutLine(const void *srcdata, int srcsize, NuclearFRectt frct, std::vector<NuclearPoint>& outLinePoint, bool bflag)
	{
		return true;
	}
    
	bool Cocos2dRenderer::SaveTextureToDXSurface(const void *srcdata, int srcsize, NuclearBuffer &destBuffer, NuclearTextureFormat texfmt, NuclearImageFileFormat filefmt, NuclearRect &rect, int clipmode, bool setNonzeroAlpha2One)
	{
		return true;
	}
    
	bool Cocos2dRenderer::SaveTextureToFile(PictureHandle handle, NuclearTextureFormat texfmt, NuclearImageFileFormat filefmt, NuclearBuffer &outbuffer)
	{
		return false;
	}
    
	bool Cocos2dRenderer::SaveTextureToFile(PictureHandle handle, NuclearImageFileFormat filefmt, const std::wstring &path)
	{
		return true;
	}
    
	void Cocos2dRenderer::DumpAllPicture(const std::wstring &path)
	{
	}
    
	bool Cocos2dRenderer::SplitTexture(PictureHandle handle, NuclearImageFileFormat filefmt, NuclearRect &rect, std::vector<std::pair<NuclearRect,NuclearBuffer> > &dstpics, int &row, int &col, int width, int height)
	{
        return true;
	}
#ifdef XP_PERFORMANCE
	RenderPerformance& Cocos2dRenderer::GetPerformance()
	{
		return m_performance;
	}
#endif
	int Cocos2dRenderer::GetAvailableTextureMem()
	{
		return 0;
	}
    
    
	bool Cocos2dRenderer::IsTextureCapsNoPow2()
	{
		return false;
	}
    
	bool Cocos2dRenderer::InitBatchVB()
	{
        glGenBuffers(1, &m_particleVB);
        glGenBuffers(1, &m_particleUVB);
        glGenBuffers(1, &m_particleIB);

		WORD n = 0;
        GLushort* pIndices = m_pIndices;
		for(int i=0; i<MAX_PARTICLE_NUM; i++)
		{
			*pIndices++ = n;
			*pIndices++ = n+1;
			*pIndices++ = n+2;
			*pIndices++ = n+2;
			*pIndices++ = n+3;
			*pIndices++ = n;
			n += 4;
		}
        
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_particleIB);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(m_pIndices[0]) * MAX_PARTICLE_NUM * 6, m_pIndices, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        return true;
	}
    
	void Cocos2dRenderer::DestroyBatchVB()
	{
        glDeleteBuffers(1, &m_particleVB);
        glDeleteBuffers(1, &m_particleUVB);
        glDeleteBuffers(1, &m_particleIB);
	}
    
	PictureHandle Cocos2dRenderer::CreateRenderTarget(int iWidth, int iHeight, NuclearTextureFormat texfmt)
	{
		if( texfmt != XPTEXFMT_DEFAULT )
		{
			if( !IsTexfmtSupport(texfmt) )
				texfmt = XPTEXFMT_DEFAULT;
		}
		
		if( texfmt == XPTEXFMT_DEFAULT )
		{
			texfmt = IsMode16()?XPTEXFMT_A4R4G4B4:XPTEXFMT_A8R8G8B8;
		}
        
		CTextureInfo TextureInfo;
		CPicInfo PicInfo;
        
        Cocos2dRenderTarget* pRenderTarget = Cocos2dRenderTarget::CreateRenderTarget(iWidth, iHeight, texfmt);
       	if( !pRenderTarget ) 
		{
            assert(false);
            
			return INVALID_PICTURE_HANDLE;
		}
		TextureInfo.m_pTexture = ((Cocos2dRenderTarget::MyRenderTexture*) pRenderTarget->getRT())->GetTexture();
        
        
		TextureInfo.rect.left = 0;
		TextureInfo.rect.top = 0;
		TextureInfo.rect.right = iWidth;
		TextureInfo.rect.bottom = iHeight;
		PicInfo.m_info.m_nPicWidth = iWidth;
		PicInfo.m_info.m_nPicHeight = iHeight;
		PicInfo.m_info.m_texfmt = texfmt;
		PicInfo.m_CTextureInfoVector.push_back(TextureInfo);
		m_mapPictures.insert(std::make_pair(m_picHandleSeed, PicInfo));
		m_setRenderTargets.insert(m_picHandleSeed);
        m_mapRenderTargets.insert(std::make_pair(m_picHandleSeed, pRenderTarget));
		return m_picHandleSeed++;
	}
    
	bool Cocos2dRenderer::FreeRenderTarget(PictureHandle hRenderTarget)
	{
		if( m_setRenderTargets.end() == m_setRenderTargets.find(hRenderTarget) )
			return false;
		m_setRenderTargets.erase(hRenderTarget);
        std::map<PictureHandle, Cocos2dRenderTarget*>::iterator i = m_mapRenderTargets.find(hRenderTarget);
        if ( i != m_mapRenderTargets.end())
        {
            delete i->second;
        }
        
        PictureMap::iterator it = m_mapPictures.find(hRenderTarget);
		if( it != m_mapPictures.end() )
		{
			m_mapPictures.erase(it);
		}
		return true;
	}
    
	bool Cocos2dRenderer::PushRenderTarget(PictureHandle hRenderTarget)
	{
		if( m_setRenderTargets.end() == m_setRenderTargets.find(hRenderTarget) )
		{
			XPLOG_ERROR(L"PushRenderTarget Err, m_setRenderTargets can't find handle\n");
			return false;
		}
        
		PictureMap::iterator it = m_mapPictures.find(hRenderTarget);
		if ( it == m_mapPictures.end() )
		{
			XPLOG_ERROR(L"PushRenderTarget Err, m_mapPictures can't find handle\n");
			return false;
		}
        
        if (it->second.m_CTextureInfoVector.size() <= 0)
        {
            XPLOG_ERROR(L"PushRenderTarget texture is NULL\n");
            return false;
        }
        
        if (it->second.m_CTextureInfoVector[0].m_pTexture == NULL)
        {
            XPLOG_ERROR(L"PushRenderTarget texture is NULL\n");
            return false;
        }
        
        std::map<PictureHandle, Cocos2dRenderTarget*>::iterator iRT = m_mapRenderTargets.find(hRenderTarget);
        if (iRT == m_mapRenderTargets.end())
        {
            return false;
        }

        Cocos2dRenderTarget* pRenderTarget = dynamic_cast<Cocos2dRenderTarget*>(iRT->second);
        if (pRenderTarget) {
            pRenderTarget->getRT()->begin();
        }
        
        RenderTargetData rtd;
        rtd.pRT = pRenderTarget;
        rtd.width = it->second.m_CTextureInfoVector[0].rect.Width();
        rtd.height = it->second.m_CTextureInfoVector[0].rect.Height();
        m_renderTargetStack.push_back(rtd);
		return true;
	}
    
	bool Cocos2dRenderer::PopRenderTarget()
	{
        if (m_renderTargetStack.empty())
		{
            assert(false);
            return false;
        }
        
        m_renderTargetStack[m_renderTargetStack.size()-1].pRT->getRT()->end();
        m_renderTargetStack.pop_back();
		return true;
	}
    
	void Cocos2dRenderer::SetAlphaBlendEnable(bool f)
	{
		m_stateMan.SetAlphaBlendEnable(f);
	}
    
	bool Cocos2dRenderer::GetAlphaBlendEnable() const
	{
		return m_stateMan.GetAlphaBlendEnable();
	}
    
	void Cocos2dRenderer::SetAlphaBlendSrcType(XPALPHABLEND_TYPE srcblend)
	{
		m_stateMan.SetAlphaBlendSrcType(srcblend);
	}
    
	XPALPHABLEND_TYPE Cocos2dRenderer::GetAlphaBlendSrcType() const
	{
		return m_stateMan.GetAlphaBlendSrcType();
	}
    
	void Cocos2dRenderer::SetAlphaBlendDstType(XPALPHABLEND_TYPE dstblend)
	{
		m_stateMan.SetAlphaBlendDstType(dstblend);
	}
    
	XPALPHABLEND_TYPE Cocos2dRenderer::GetAlphaBlendDstType() const
	{
		return m_stateMan.GetAlphaBlendDstType();
	}
    
	void Cocos2dRenderer::SetTextureColorBlendType(XPTEXCOLORBLEND_TYPE colorop)
	{
		m_stateMan.SetTextureColorBlendType(colorop);
	}
    
	XPTEXCOLORBLEND_TYPE Cocos2dRenderer::GetTextureColorBlendType() const
	{
		return m_stateMan.GetTextureColorBlendType();
	}
    
	void Cocos2dRenderer::SetSeparateAlphaBlend(bool f)
	{
		m_stateMan.SetSeparateAlphaBlend(f);
	}
    
	bool Cocos2dRenderer::GetSeparateAlphaBlend() const
	{
		return m_stateMan.GetSeparateAlphaBlend();
	}
    
	void Cocos2dRenderer::SetRenderEffect(XPRENDER_EFFECT re)
	{
		m_stateMan.SetRenderEffect(re);
	}
    
	XPRENDER_EFFECT Cocos2dRenderer::GetRenderEffect() const
	{
		return m_stateMan.GetRenderEffect();
	}
    
	bool Cocos2dRenderer::DrawTriangleFan(PictureHandle handle, const NuclearTCT1Vertex* pVertexs, int nVertexNum)
	{ 
		return true;
	}
    
	bool Cocos2dRenderer::DrawParticles(PictureHandle handle, const NuclearTCT1Vertex* pVertexs, int nVertexNum)
	{
		if( handle == INVALID_PICTURE_HANDLE || pVertexs == NULL || nVertexNum < 1 ) 
			return false;
		PictureMap::iterator it = m_mapPictures.find(handle);
		if( it == m_mapPictures.end() )
			return false;
        
		int a = (int)(it->second.m_CTextureInfoVector.size());
		if( a != 1 ) return false;
		if (it->second.m_CTextureInfoVector[0].m_pTexture == NULL)
			return false;
        
        const unsigned int kQuadSize = sizeof(NuclearTCT1Vertex);        
        cocos2d::ccGLBindTexture2D(it->second.m_CTextureInfoVector[0].m_pTexture->getName());
        
        if (it->second.m_CTextureInfoVector[0].m_pTexture->isEtcTexture()) {
            cocos2d::ccGLActiveTexture(GL_TEXTURE1);
            cocos2d::ccGLBindTexture2D(it->second.m_CTextureInfoVector[0].m_pTexture->getAlphaName());
        }
        glBindBuffer(GL_ARRAY_BUFFER, m_particleVB);
        glBufferData(GL_ARRAY_BUFFER, nVertexNum*sizeof(NuclearTCT1Vertex), pVertexs, GL_DYNAMIC_DRAW);
        ccGLEnableVertexAttribs(cocos2d::kCCVertexAttribFlag_PosColorTex);
        
        // vertices
        glVertexAttribPointer(cocos2d::kCCVertexAttrib_Position, 3, GL_FLOAT, GL_FALSE, kQuadSize, (GLvoid*) offsetof(NuclearTCT1Vertex, x));
        
        // colors
        glVertexAttribPointer(cocos2d::kCCVertexAttrib_Color, 4, GL_UNSIGNED_BYTE, GL_TRUE, kQuadSize, (GLvoid*) offsetof(NuclearTCT1Vertex, c));
        
        // tex coords
        glVertexAttribPointer(cocos2d::kCCVertexAttrib_TexCoords, 2, GL_FLOAT, GL_FALSE, kQuadSize, (GLvoid*) offsetof(NuclearTCT1Vertex, u));
        
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_particleIB);
        
        glDrawElements(GL_TRIANGLES, (GLsizei)nVertexNum/2*3, GL_UNSIGNED_SHORT, (GLvoid*) (0));
        
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        

        if (it->second.m_CTextureInfoVector[0].m_pTexture->isEtcTexture()) {
            cocos2d::ccGLBindTexture2D(0);
            cocos2d::ccGLActiveTexture(GL_TEXTURE0);
        }

		return true;
	}
    
	bool Cocos2dRenderer::DrawParticles(PictureHandle handle, const NuclearUTCT1Vertex* pVertexs, int nVertexNum)
	{
		if( handle == INVALID_PICTURE_HANDLE || pVertexs == NULL || nVertexNum < 1 ) 
			return false;
		PictureMap::iterator it = m_mapPictures.find(handle);
		if( it == m_mapPictures.end() )
			return false;
        
		int a = (int)(it->second.m_CTextureInfoVector.size());
		if( a != 1 ) return false;
		if (it->second.m_CTextureInfoVector[0].m_pTexture == NULL)
			return false;
        
        const unsigned int kQuadSize = sizeof(NuclearUTCT1Vertex);
        
        cocos2d::ccGLBindTexture2D(it->second.m_CTextureInfoVector[0].m_pTexture->getName());
        
        glBindBuffer(GL_ARRAY_BUFFER, m_particleUVB);              
        
		int nBatchOffset = 0;
		const int nBatchSize = 512; 
		int nTempVertexNum = nVertexNum;
        
		while(nTempVertexNum > 0)
		{
			int nsize = nTempVertexNum-nBatchSize >0 ? nBatchSize:nTempVertexNum;
            
            glBufferSubData(GL_ARRAY_BUFFER, nBatchOffset*sizeof(NuclearUTCT1Vertex), nsize*sizeof(NuclearUTCT1Vertex), pVertexs);
            ccGLEnableVertexAttribs(cocos2d::kCCVertexAttribFlag_PosColorTex);
            
            // vertices
            glVertexAttribPointer(cocos2d::kCCVertexAttrib_Position, 3, GL_FLOAT, GL_FALSE, kQuadSize, (GLvoid*) offsetof(NuclearUTCT1Vertex, x));
            
            // colors
            glVertexAttribPointer(cocos2d::kCCVertexAttrib_Color, 4, GL_UNSIGNED_BYTE, GL_TRUE, kQuadSize, (GLvoid*) offsetof(NuclearUTCT1Vertex, c));
            
            // tex coords
            glVertexAttribPointer(cocos2d::kCCVertexAttrib_TexCoords, 2, GL_FLOAT, GL_FALSE, kQuadSize, (GLvoid*) offsetof(NuclearUTCT1Vertex, u));
            
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_particleIB);
  
            glDrawElements(GL_TRIANGLES, (GLsizei)nsize*6, GL_UNSIGNED_SHORT, (GLvoid*) (nBatchOffset*6*sizeof(m_pIndices[0])));

            nBatchOffset += nsize;
			nBatchOffset = nBatchOffset >= nVertexNum ? 0:nBatchOffset;
			nTempVertexNum -= nsize;
		}
        
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
         
		return true;
	}
    
	bool Cocos2dRenderer::CollectParticles(PictureHandle handle, XPTEXCOLORBLEND_TYPE colorBlend, PSL_RENDSTATE alphaBlend, const NuclearTCT1Vertex* pVertexs, int nVertexNum/*??\u03BC?\u220F???*/)
	{
		ULONG64 ULong64Key = handle;
		ULong64Key = ULong64Key<<16 | colorBlend<<8 | alphaBlend;
        
		std::map<ULONG64, std::vector<NuclearTCT1Vertex> >::iterator it = m_mapParticles.find(ULong64Key);
		if( it == m_mapParticles.end() )
		{
			std::vector<NuclearTCT1Vertex> particles;
			particles.resize(nVertexNum);
			memcpy(&particles[0], pVertexs, nVertexNum * sizeof(NuclearTCT1Vertex));
			m_mapParticles.insert(std::make_pair(ULong64Key, particles));
		}
		else
		{   
			int cnt = (int)(it->second.size());
			if( cnt+nVertexNum >= 4*MAX_PARTICLE_NUM )
			{
				DrawCurParticles(it->first, it->second);
				it->second.clear();
			}
			else
			{
				for(int i=0; i<nVertexNum; i++)
				{
					it->second.push_back( *(pVertexs+i) );
				}
			}
		}
        
		return true;
	}    
    
	bool Cocos2dRenderer::DrawCurParticles(ULONG64 param, const std::vector<NuclearTCT1Vertex>& particles)
	{
		PictureHandle handle = (int)(param>>16 & 0xffffffff);
		XPTEXCOLORBLEND_TYPE colorBlend = (XPTEXCOLORBLEND_TYPE)(param>>8 & 0xff);
		PSL_RENDSTATE alphaBlend = (PSL_RENDSTATE)(param & 0xff);
        
		SetTextureColorBlendType(colorBlend);
		XPRENDER_EFFECT oldefct = GetRenderEffect();
		switch(alphaBlend)
		{
            case PSLRENDSTATE_ALPHA:
                SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
                SetAlphaBlendDstType(XPBLEND_INVSRCALPHA);
                break;
            case PSLRENDSTATE_LIGHTING:
                SetRenderEffect(XPRE_NULL);
                SetAlphaBlendSrcType(XPBLEND_SRCALPHA);
                SetAlphaBlendDstType(XPBLEND_ONE);
                break;
            case PSLRENDSTATE_ADD:
                SetRenderEffect(XPRE_NULL);
                SetAlphaBlendSrcType(XPBLEND_ONE);
                SetAlphaBlendDstType(XPBLEND_ONE);
                break;
            default:
                break;
		}
        
		int cnt = (int)(particles.size());
        
		if( handle == INVALID_PICTURE_HANDLE || cnt < 1 ) 
			return false;

		PictureMap::iterator it = m_mapPictures.find(handle);
		if( it == m_mapPictures.end() )
			return false;
        
		int a = (int)(it->second.m_CTextureInfoVector.size());
		if( a != 1 ) return false;
        
        cocos2d::ccGLEnableVertexAttribs( cocos2d::kCCVertexAttribFlag_Position | cocos2d::kCCVertexAttribFlag_TexCoords );
        
        cocos2d::ccGLBindTexture2D( it->second.m_CTextureInfoVector[0].m_pTexture->getName() );
        
        const unsigned int kQuadSize = sizeof(NuclearTCT1Vertex);
        
        // vertex
        unsigned int diff = offsetof( NuclearTCT1Vertex, x);
        glVertexAttribPointer(cocos2d::kCCVertexAttrib_Position, 3, GL_FLOAT, GL_FALSE, kQuadSize, (void*)(&particles[0] + diff));
        // color
        diff = offsetof( NuclearTCT1Vertex, c);
        glVertexAttribPointer(cocos2d::kCCVertexAttrib_Color, 4, GL_UNSIGNED_BYTE, GL_TRUE, kQuadSize, (void*)(&particles[0] + diff));
        
        // texture coords
        diff = offsetof( NuclearTCT1Vertex, u);
        glVertexAttribPointer(cocos2d::kCCVertexAttrib_TexCoords, 2, GL_FLOAT, GL_FALSE, kQuadSize, (void*)(&particles[0] + diff));
        
		return true;
	}
    
	bool Cocos2dRenderer::BatchDrawParticles()
	{
		std::map<ULONG64, std::vector<NuclearTCT1Vertex> >::iterator itb = m_mapParticles.begin();
		std::map<ULONG64, std::vector<NuclearTCT1Vertex> >::iterator ite = m_mapParticles.end();
		for(; itb != ite; ++itb)
		{
			DrawCurParticles(itb->first, itb->second);
		}
		m_mapParticles.clear();
        
		return true;
	}
    
	bool Cocos2dRenderer::DrawIndexVerticeBuffer(PictureHandle handle, const NuclearTCT1Vertex* pVertexs, const WORD* pIndexs, int nVertexNum, int nIndexNum)
	{
		return true;
	}

	NuclearPictureInfo Cocos2dRenderer::GetPictureSizeInfo(PictureHandle handle)
	{
		NuclearPictureInfo info;
		GetPictureInfo(handle, info);
		return info;
	}

	PictureHandle Cocos2dRenderer::checkPicture(const std::wstring &fileuri)
	{
		PictureHandle handle = 0;
		PictureMap::const_iterator it = m_mapPictures.begin();
		while (m_mapPictures.end() != it)
		{
			CPicInfo picInfo = it->second;
			if (picInfo.m_info.fileuri.compare(fileuri) == 0 && !picInfo.m_CTextureInfoVector.empty())
			{
				handle = it->first;
				return handle;
			}
			++it;
		}
		return handle;
	}
 }
