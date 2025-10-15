#define NOMINMAX
#include "CEGUICocos2DTextureTarget.h"

#include <CEGUIExceptions.h>
#include <CEGUILogger.h>
#include <CEGUIRenderQueue.h>
#include <CEGUIGeometryBuffer.h>

#include "CEGUICocos2DRenderer.h"
#include "CEGUICocos2DTexture.h"

namespace CEGUI
{
const float Cocos2DTextureTarget::DEFAULT_SIZE = 128.0f;

class CCTextureTarget : public cocos2d::CCRenderTexture
{
public:
	cocos2d::CCTexture2D* getTexture()
	{
		return m_pTexture;
	}

	GLuint* getFBO(){ return &m_uFBO; }
	GLint*	getOldFBO(){ return &m_nOldFBO; }
};

Cocos2DTextureTarget::Cocos2DTextureTarget(Cocos2DRenderer& owner) :
    Cocos2DRenderTarget(owner),
    d_surface(0),
	d_texture(0)
{
    declareRenderSize(Size(DEFAULT_SIZE, DEFAULT_SIZE));
}

Cocos2DTextureTarget::~Cocos2DTextureTarget()
{
    cleanupRenderTexture();
}

void Cocos2DTextureTarget::declareRenderSize(const Size& sz)
{
    // exit if current size is enough
    if ((d_area.getWidth() >= sz.d_width) && (d_area.getHeight() >=sz.d_height))
        return;

    setArea(Rect(d_area.getPosition(), sz));
    resizeRenderTexture();
    clear();
}

bool Cocos2DTextureTarget::isImageryCache() const
{
    return true;
}

void Cocos2DTextureTarget::activate()
{	
	d_surface->begin();	
	Cocos2DRenderTarget::activate();
	Size sz = d_texture->getSize();
    glViewport(0, 0, d_area.getWidth(), d_area.getHeight());
}

void Cocos2DTextureTarget::deactivate()
{
    Cocos2DRenderTarget::deactivate();
	d_surface->end();
}

//----------------------------------------------------------------------------//
void Cocos2DTextureTarget::clear()
{
	if (d_surface)
	{
		d_surface->beginWithClear(0,0,0,0);
		d_surface->end();
	}	
}

Texture& Cocos2DTextureTarget::getTexture() const
{
    return *d_texture;
}

void Cocos2DTextureTarget::initialiseRenderTexture()
{
    Size tex_sz(d_owner.getAdjustedSize(d_area.getSize()));

	//d_surface = cocos2d::CCRenderTexture::renderTextureWithWidthAndHeight(static_cast<int>(tex_sz.d_width), static_cast<int>(tex_sz.d_height));
	d_surface = new CCTextureTarget();
	d_surface->initWithWidthAndHeight( static_cast<int>(tex_sz.d_width), static_cast<int>(tex_sz.d_height), cocos2d::kCCTexture2DPixelFormat_RGBA8888);
	d_texture = &(Cocos2DTexture&)d_owner.createTexture(dynamic_cast<CCTextureTarget*>(d_surface)->getTexture());
	d_texture->setOriginalDataSize(d_area.getSize());
}

void Cocos2DTextureTarget::resizeRenderTexture()
{
    cleanupRenderTexture();
    initialiseRenderTexture();
}

void Cocos2DTextureTarget::cleanupRenderTexture()
{
    if (d_surface)
    {
        d_surface->release();
        d_surface = 0;
    }

	if (d_texture)
	{
		d_owner.destroyTexture(d_texture);
		d_texture = 0;
	}	
}

bool Cocos2DTextureTarget::isRenderingInverted() const
{
    return true;
}

bool Cocos2DTextureTarget::saveToFile(const String& filename)
{
	if (d_surface)
	{
		cocos2d::CCImage* pImage = d_surface->newCCImage();
		if (pImage)
		{
			int areaWidth = d_area.getWidth();
			int areaHeight = d_area.getHeight();
			if (pImage->getWidth() > areaWidth || pImage->getHeight() > areaHeight)
			{
				GLubyte* pClampedData = new GLubyte[areaWidth * areaHeight * 4];
				if (pClampedData)
				{
					GLubyte* pSrcData = pImage->getData();
					for (int r = 0; r < areaHeight; ++r)
					{
						int srcR = pImage->getHeight() - areaHeight + r;

						GLubyte* pSrcLine = pSrcData + srcR * pImage->getWidth() * 4;
						GLubyte* pDstLine = pClampedData + r * areaWidth * 4;
						int copyBytes = areaWidth * 4;
						memcpy(pDstLine, pSrcLine, copyBytes);
					}

					cocos2d::CCImage* pClampedImage = new cocos2d::CCImage;
					if (pClampedImage)
					{
						if (pClampedImage->initWithImageData(pClampedData, areaWidth * areaHeight * 4, cocos2d::CCImage::kFmtRawData, areaWidth, areaHeight, 8))
						{
							CC_SAFE_DELETE(pImage);
							pImage = pClampedImage;
						}
					}

					delete[] pClampedData;
				}
			}

			bool bRet = pImage->saveToFile(filename.c_str());

			CC_SAFE_DELETE(pImage);

			return bRet;
		}
	}
	return false;
}

//----------------------------------------------------------------------------//
void Cocos2DTextureTarget::preD3DReset()
{
    if (d_surface)
    {
        d_surface->release();
        d_surface = 0;
    }

	if (d_texture)
	{
		d_owner.destroyTexture(d_texture);
		d_texture = 0;
	}
}

void Cocos2DTextureTarget::postD3DReset()
{
   assert("post reset not implement" && false);
}


} // End of  CEGUI namespace section
