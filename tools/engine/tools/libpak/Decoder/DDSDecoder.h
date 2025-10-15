#pragma once

#include "../../libdds/include/libdds.h"

namespace MIX
{
	 class CBitmap;
	 class IDDrawSurface;
}

namespace DECODER
{
	class CDDSDecoder 
	{
	public:
		CDDSDecoder();
		~CDDSDecoder();

		bool Init( );

		bool Decode( 
			const unsigned char* pbBuffer, 
			unsigned int size, 
			MIX::CBitmap& bmp );

	private:
		MIX::IDDrawSurface* m_pidds;
	};
}