#pragma once
#include "../Picture.h"
#include "PictureDecoder.h"

namespace MHZX
{
	class CPNGDecoder : public IPictureDecoder
	{
	public:
		virtual bool Init( const void *pvEnv )
		{
			return true;
		}
		virtual bool Decode( PFS::CBaseFile& rFile, CPicture& rPicture );
	};
}
