//#include "../common/ljfmutil.h"
#include "nucocos2d_render.h"

namespace Nuclear
{
	const float Renderer::Z = 0.5f;

	PictureHandle Renderer::LoadPictureFromNativePath(const std::string &nativepath)
	{
		Nuclear::FileBuffer fb(nativepath.c_str());
		
        int size = fb.Size();
		if( size == 0 ) 
        {
            return INVALID_PICTURE_HANDLE;
        }        
        
		return LoadPictureFromMem(fb.Begin(), size);
	}

	// 创建函数
	XPCREATE_RENDERER_RESULT CreateRenderer(Renderer **ppr, const NuclearDisplayMode &dmode, NuclearFileIOManager *pFileIOMan,DWORD flags, XPRENDERER_VERSION rv, NuclearMultiSampleType mstype)
	{
		if( ppr == NULL ) 
			return XPCRR_NULL_POINTER;
		*ppr = NULL;
		switch( rv )
		{		
		case XPRV_DEFAULT:
		case XPRV_DX9:		
            assert(false && "dx9 render is not supported on mobile");
			//*ppr = new DX9Renderer(pFileIOMan);
			break;
        case XPRV_COCOS2D:
            *ppr = new Cocos2dRenderer(pFileIOMan);
            break;
		default:
			break;
		}
		if( *ppr == NULL ) 
			return XPCRR_NULL_POINTER;
		XPCREATE_RENDERER_RESULT result = (*ppr)->Create(dmode, flags, mstype);

		if( result != XPCRR_OK )
		{
			(*ppr)->Destroy();
			delete *ppr;
			*ppr = NULL;
		}
		return result;
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
