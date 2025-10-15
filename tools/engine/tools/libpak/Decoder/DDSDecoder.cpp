#include "DDSDecoder.h"
#include <iostream>

namespace DECODER
{
	CDDSDecoder::CDDSDecoder()
	{
		m_pidds = NULL;
	}

	CDDSDecoder::~CDDSDecoder()
	{
		if ( m_pidds != NULL )
		{
			m_pidds->Destroy();
			m_pidds = NULL;
		}
	}

	bool CDDSDecoder::Init( )
	{
		if( m_pidds == NULL )
		{
			m_pidds = MIX::CreateDDrawSurface();
			if( m_pidds == NULL )
			{
				return false;
			}
		}

		return true;
	}

	bool CDDSDecoder::Decode( const unsigned char* pbBuffer, unsigned int size, MIX::CBitmap& bmp )
	{
		MIX::IDDrawSurface* pidds = m_pidds;

		if( m_pidds == NULL)
		{
			std::wcerr << L" IDDrawSurface has not been initialized yet!  " << std::endl;
			return false;
		}

		int nRet = pidds->LoadFromMem( pbBuffer, size );

		if ( nRet != 0 )
		{
			std::wcerr << L" LoadFromMem Failed! " << std::endl;
			return false;
		}

		if( pidds->Decompress( bmp ) != 0 )
		{
			std::wcerr << L" Decompress Failed! " << std::endl;
			return false;
		}

		if( !pidds->IsValid() )
		{
			std::wcerr << L" File Is Not Valid! " << std::endl;
			return false;
		}

		return true;
	}
}
