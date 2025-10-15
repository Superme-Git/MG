#include "Picture.h"
#include "Decoder/BlockDecoder.h"
#include <iostream>

namespace MHZX
{

	CPicture::CPicture(void)
	{
		m_pBitArray = NULL;
	}

	CPicture::~CPicture(void)
	{
		FreeBitmap();
	}

	bool CPicture::AllocBitmap( )
	{
		if( m_pBitArray != NULL )
		{
			FreeBitmap();
		}
		
		if( m_PictureDesc.width > 0 && m_PictureDesc.height > 0 )
			m_pBitArray = new unsigned char[ m_PictureDesc.width * m_PictureDesc.height*4 ];

		return true;
	}

	void CPicture::FreeBitmap()
	{
		if( m_pBitArray != NULL )
		{
			delete [] m_pBitArray;
			m_pBitArray = NULL;
		}
	}

	void CPicture::DecodeAndFillBlock( const unsigned char* pBitBlock, unsigned int size,
		int width, int height, int x_offset, int y_offset )
	{
		IBlockDecoder* pAdapter = 
			CBlockDecoderFactory::CreateBlockDecoder( m_PictureDesc.format );
		if (pAdapter==NULL)
		{
			std::wcerr << L"No find decoder for image of format: " << m_PictureDesc.format << std::endl;
			return;
		}

		pAdapter->Decode( m_pBitArray, m_PictureDesc.width,
			pBitBlock, size, width, height, x_offset, y_offset );
	}

	void CPicture::DecodeAndFillBlock( const unsigned char* pBitBlock, unsigned int size,
		int width, int height, int x_offset, int y_offset, int format )
	{
		IBlockDecoder* pAdapter = 
			CBlockDecoderFactory::CreateBlockDecoder( format );
		if (pAdapter==NULL)
		{
			std::wcerr << L"No find decoder for image of format: " << format << std::endl;
			return;
		}

		pAdapter->Decode( m_pBitArray, m_PictureDesc.width, 
			pBitBlock, size, width, height, x_offset, y_offset );
	}

	void CPicture::FillImageData( const unsigned char* pSrcImageData, unsigned int size )
	{
		memcpy( m_pBitArray, pSrcImageData, size );
	}

	void CPicture::DumpAsXML( std::wostream& os ) const
	{
		os << L"<picture width=\"" << m_PictureDesc.width << L"\" height=\"" << m_PictureDesc.height
			<< L"\" x_offset=\"" << m_PictureDesc.x_offset << L"\" y_offset=\"" << m_PictureDesc.y_offset 
			<< L"\" format=\"" << m_PictureDesc.format << L"\" storage=\"" << m_PictureDesc.storage
			<< L"\" file=\"" << m_PictureDesc.filename;

		if( m_vBlocks.size() > 0 )
		{
			os 	<< L"\">" << std::endl;
			size_t blockcount = m_vBlocks.size();
			for( size_t j = 0; j < blockcount ; j ++ )
			{
				const BlockDesc & rBlockDesc = m_vBlocks[j];
				os << L"  <block width=\"" << rBlockDesc.width
					<< L"\" height=\"" << rBlockDesc.height
					<< L"\" x_offset=\"" << rBlockDesc.x_offset 
					<< L"\" y_offset=\"" << rBlockDesc.y_offset 
					<< L"\" size=\"" << rBlockDesc.size
					<< L"\"/>" << std::endl;
			}

			os << L"</block>" << std::endl;
		}
		else
		{
			os << L"\"/>" << std::endl;
		}
	}
}