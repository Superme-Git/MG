#pragma once

#include <string>

namespace PFS
{
	class CBaseFile;
}

namespace MHZX
{
	class CPicture;

	class IPictureDecoder
	{
	public:
		virtual bool Init( const void *pvEnv ) = 0;
		virtual bool Decode( PFS::CBaseFile& rFile, CPicture& rPicture ) = 0;
		virtual void Release() = 0;
	};

	class CPictureDecoderFactory
	{
	public:
		static IPictureDecoder* CreatePictureDecoder(int format);
	};
}