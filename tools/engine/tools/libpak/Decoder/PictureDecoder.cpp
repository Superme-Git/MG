#include "../../../pfs/pfs2.h"
#include "../../../pfs/device.h"
#include "../Picture.h"
#include "PictureDecoder.h"
#include "TargaDecoder.h"
#include "DDSDecoder.h"
#include "PNGDecoder.h"
#include <iostream>

namespace MHZX
{
	class CTGADecoder : public IPictureDecoder
	{
	public:
		virtual bool Init( const void *pvEnv )
		{
			return true;
		}
		virtual bool Decode( PFS::CBaseFile& rFile, CPicture& rPicture );
	};

	bool CTGADecoder::Decode( PFS::CBaseFile& rFile, CPicture& rPicture )
	{
		LPVOID pvData = NULL;
		ULONG ulWidth = 0;
		ULONG ulHeight = 0;
		UINT uiBitCount = 0;
		if( !HELPER::LoadTGAData( rFile, &pvData, &ulWidth, &ulHeight, &uiBitCount ) )
		{
			return false;
		}

		if( uiBitCount != 32 )
		{
			if( uiBitCount != 24 )
			{
				// Not support it!
				HELPER::FreePictureData( pvData );
				return false;
			}

			rPicture.m_PictureDesc.format = PIX_FMT_R8G8B8;
			if( !rPicture.AllocBitmap() )
			{
				std::wcerr << L" AllocBitmap for picture failed! Filename: "<< rPicture.m_PictureDesc.filename << std::endl;
				return false;
			}

			rPicture.DecodeAndFillBlock( (const unsigned char * )pvData, 0, ulWidth, ulHeight, 0, 0 );
		}
		else
		{
			if( !rPicture.AllocBitmap() )
			{
				std::wcerr << L" AllocBitmap for picture failed! Filename: "<< rPicture.m_PictureDesc.filename << std::endl;
				return false;
			}

			rPicture.FillImageData( (const unsigned char * )pvData, ulWidth * ulHeight * ( uiBitCount / 8 ) );
		}

		HELPER::FreePictureData( pvData );

		return true;
	}
}

namespace MHZX
{
	class CDDSDecoder : public IPictureDecoder
	{
	public:
		virtual bool Init( const void *pvEnv )
		{
			return m_bufferData != NULL && m_decoder.Init();
		}
		
		virtual bool Decode( PFS::CBaseFile& rFile, CPicture& rPicture );

	public:
		CDDSDecoder()
		{
			m_bufferSize = 0;
			m_bufferData = 0;
			AllocBuffer( 1024 * 1024 *4 );
		}

		~CDDSDecoder()
		{
			if( m_bufferData != NULL)
			{
				free( m_bufferData );
				m_bufferData = NULL;
			}

			m_bufferSize = 0;
		}

	private:
		unsigned char * AllocBuffer(unsigned int newSize)
		{
			if( m_bufferSize < newSize )
			{
				m_bufferSize = newSize;
				m_bufferData = (unsigned char *)realloc(m_bufferData, newSize);
			}

			return m_bufferData;
		}

		bool DecodeDDS( PFS::CBaseFile* pFile, CPicture* pPicture );

		bool DecodeDDSBlock( CPicture* pPicture, 
			const BlockDesc& desc, 
			const unsigned char* pbBuffer
			);

	private:
		unsigned char* m_bufferData;
		unsigned int   m_bufferSize;

		// MIX::IDDrawSurface* m_pidds;

		DECODER::CDDSDecoder m_decoder;
		
	};

	bool CDDSDecoder::DecodeDDSBlock( CPicture* pPicture, 
		const BlockDesc& desc, const unsigned char* pbBuffer )
	{
		const std::wstring& filename = pPicture->m_PictureDesc.filename;

		MIX::CBitmap bmp;

		if( !m_decoder.Decode(pbBuffer, desc.size, bmp ))
		{
			std::wcerr << L" Decode DDS file Failed! " << filename << std::endl;
			return  false;
		}

		unsigned nWidth = bmp.GetWidth();
		unsigned nHeight = bmp.GetHeight(); 

		if( desc.width != nWidth )
		{
			std::wcerr << L" block width is not same as decoded : " << filename << std::endl;
			std::wcerr << L" block.width : " << desc.width << std::endl;
			std::wcerr << L" bitmap width : " << nWidth << std::endl;

			return true;
		}

		if( desc.height != nHeight )
		{
			std::wcerr << L" block height is not same as decoded : " << filename << std::endl;
			std::wcerr << L" block height : " << desc.height << std::endl;
			std::wcerr << L" bitmap height : " << nHeight << std::endl;

			return true;
		}

		// ���ڴ�DDS�ļ�������������format��pak.meta.xml�м�¼��format���ܲ�һ����
		// ��ʵ�������޸ĵ�libdds������������CBitmap����A8R8G8B8��ʽ�ġ�

		if( bmp.GetFormat() == PIX_FMT_A8R8G8B8 || // ��΢�Ż�һ��
			bmp.GetFormat() >= PIX_FMT_R5G6B5 && bmp.GetFormat() <= PIX_FMT_DXT5n )
		{
			// ��������������⣬��ʽ��ô�죿

			// bmp.GetFormat() == PIX_FMT_A8R8G8B8, but pPicture->m_PictureDesc.format is not PIX_FMT_A8R8G8B8.
			// we do not change pPicture->m_PictureDesc.format here.
			// pPicture->m_PictureDesc.format = bmp.GetFormat();

			pPicture->DecodeAndFillBlock( (const unsigned char *)
				bmp.GetPixels(), bmp.GetSize(), nWidth, nHeight, desc.x_offset, desc.y_offset, bmp.GetFormat() );
	
			return true;
		}
		else
		{
			std::wcerr << L" Unsupported DDS file format: " << filename << std::endl;

			return false;
		}
	
	}

	//
	// ����DDS�ļ�(����ֻ֧��DXT1,DXT3,DXT5)
	//
	// ���������DDS�ļ����ܺ�һ���Ҳ���ܺ���顣
	// ֻ��һ�����DDS�ļ�����.ddsΪ��׺���Ǳ�׼��ʽ��DDS�ļ���
	// �������ļ�����.ddsΪ��׺��һ��Ϊ.picΪ��׺(MIX�������)��
	//
	// yanghaibo
	// 2010-09-25 - ֻ������׼.dds�ļ�.
	// 2011-01-12/13 - ֧�ֽ���MIX����������ĸ�ʽ��.dds/.pic�ļ���
	bool CDDSDecoder::DecodeDDS( PFS::CBaseFile* pFile, CPicture* pPicture )
	{
		const std::wstring& filename = pPicture->m_PictureDesc.filename;

		MIX::CBitmap bmp;

		SIZE_T ullSize = (SIZE_T)pFile->GetSize();
		if( ullSize == 0 )
		{
			std::wcerr << L" Empty Picture! filename: " << filename << std::endl;
			return true;
		}

		if( pPicture->m_PictureDesc.storage != PSF_NATIVE_DDS  ) 
		{
			assert( pPicture->m_PictureDesc.storage == PSF_DDS );

			// ����ͷ������ʵ����
			if ( pPicture->m_PictureDesc.version < 2)
			{
				PicHeader header = {0};
				if ( pFile->Read( &header, sizeof( PicHeader)) != sizeof( PicHeader))
				{
					std::wcerr << L" Read PicHeader Failed! filename: " << filename << std::endl;
					return false;
				}

				BlockDesc desc;
				for ( int i = 0; i < header.blockNum; ++i)
				{
					if ( pFile->Read( &desc, sizeof( BlockDesc)) != sizeof( BlockDesc))
					{
						// std::wcerr << L" Read BlockDesc Failed! filename: " << filename<< L" block index:" << i << std::endl;
						//std::wcerr << L" Read BlockDesc Failed! " << L"block index:" << i << std::endl;
						return false;
					}
				}

				ullSize -= (size_t)pFile->GetPos();
			}
		}

		unsigned char* pbBuffer = AllocBuffer( ( unsigned int )ullSize );
		if( pbBuffer == NULL )
		{
			std::wcerr << L" AllocBuffer Failed! " << filename  << std::endl;
			return false;
		}

		SIZE_T ullSizeTotal = 0;
		do 
		{
			ullSizeTotal += pFile->Read( pbBuffer+ullSizeTotal, (UINT)(ullSize-ullSizeTotal) );
			if( (INT)ullSizeTotal < 0 )
			{
				// Memory/Disk error ?
				std::wcerr << L" ReadFile Failed! " << filename  << std::endl;
				return false;
			}
		}while( ullSizeTotal < ullSize );

		// ˵�������۽�����bitmap��ʲô��ʽ��pPicture->AllocBitmap()���ǰ���ÿ����4���ֽڷ���Ŀռ䡣
		if(  pPicture->GetBitmapData() == NULL && !pPicture->AllocBitmap() )
		{
			std::wcerr << L" AllocBitmap for picture failed! Filename: "<<
				filename << std::endl;
			return false;
		}

		if( pPicture->m_vBlocks.size() == 0 )
		{
			// ����Block������ͼֻ��һ�顣
			BlockDesc desc;
			desc.width = pPicture->m_PictureDesc.width;
			desc.height = pPicture->m_PictureDesc.height;
			desc.x_offset = pPicture->m_PictureDesc.x_offset;
			desc.y_offset = pPicture->m_PictureDesc.y_offset;
			desc.size = (unsigned int)ullSize;

			return DecodeDDSBlock(pPicture, desc, pbBuffer);
		}
		else
		{
			// �������Ǵ������������������ǣ�����ܲ�����ȷ���ļ���ʽ��
			std::wcerr << L" WARNING: PSF_NATIVE_DDS ��ʽ���ļ�����ô���ж�飿"
				<< filename << std::endl;

			// ��������block
			for ( size_t  i = 0; i < pPicture->m_vBlocks.size(); ++i)
			{	
				const BlockDesc& desc = pPicture->m_vBlocks[i];
				if( !DecodeDDSBlock(pPicture, desc, pbBuffer) )
				{
					return false;
				}

				pbBuffer += desc.size;
			}
		}

		return true;
	}

	bool CDDSDecoder::Decode( PFS::CBaseFile& rFile, CPicture& rPicture )
	{
		return DecodeDDS( &rFile, &rPicture );
	}
}

namespace MHZX
{
	class CPICDecoder : public IPictureDecoder
	{
	public:
		virtual bool Init( const void *pvEnv )
		{
			return true;
		}
		
		virtual bool Decode( PFS::CBaseFile& rFile, CPicture& rPicture );

		CPICDecoder()
		{
			m_bufferSize = 0;
			m_bufferData = 0;
			AllocBuffer( 1024 * 1024 *4 );
		}

		~CPICDecoder()
		{
			if( m_bufferData != NULL)
			{
				free( m_bufferData );
				m_bufferData = NULL;
			}
			m_bufferSize = 0;
		}

	private:
		unsigned char * const AllocBuffer(unsigned int newSize)
		{
			if( m_bufferSize < newSize )
			{
				m_bufferSize = newSize;
				m_bufferData = (unsigned char *)realloc(m_bufferData, newSize);
			}

			return m_bufferData;
		}

	private:
		unsigned char* m_bufferData;
		unsigned int   m_bufferSize;
	};

	bool CPICDecoder::Decode( PFS::CBaseFile& rFile, CPicture& rPicture )
	{
		CPicture* pPic = &rPicture;
		// assert( pPic->m_PictureDesc.storage == PSF_BITMAP || 
		// pPic->m_PictureDesc.storage == PSF_DDS );
		if ( pPic->m_PictureDesc.version < 2)
		{
			// ����ͷ����������У��
			PicHeader header = {0};
			if ( rFile.Read( &header, sizeof( PicHeader)) != sizeof( PicHeader))
			{
				std::wcerr << L" Read PicHeader Failed! filename: "<< pPic->m_PictureDesc.filename << std::endl;
				return false;
			}

			BlockDesc desc;
			for ( int i = 0; i < header.blockNum; ++i)
			{
				if ( rFile.Read( &desc, sizeof( BlockDesc)) != sizeof( BlockDesc))
				{
					std::wcerr << L" Read BlockDesc Failed! filename: "<<pPic->m_PictureDesc.filename<< L" block index:" << i << std::endl;
					return false;
				}
			}
		}

		unsigned char* const pBlockData = 
			AllocBuffer( pPic->m_PictureDesc.width * pPic->m_PictureDesc.height *4);
		if( pBlockData == NULL )
		{
			std::wcerr << L" AllocBuffer for picture failed! Filename: "<< pPic->m_PictureDesc.filename << std::endl;
			return false;
		}

		if( !pPic->AllocBitmap() )
		{
			std::wcerr << L" AllocBitmap for picture failed! Filename: "<< pPic->m_PictureDesc.filename << std::endl;
			return false;
		}

		size_t bcount = pPic->m_vBlocks.size();
		for( size_t i = 0; i < bcount; i ++ )
		{
			BlockDesc& rBlockDesc = pPic->m_vBlocks[i];
			if( !rFile.Read( pBlockData, rBlockDesc.size ) )
			{
				std::wcerr << L" Read Block Failed! filename: "
					<<pPic->m_PictureDesc.filename<< L" block index:" << i << std::endl;
				return false;
			}

			pPic->DecodeAndFillBlock( pBlockData, rBlockDesc.size,
				rBlockDesc.width, rBlockDesc.height, 
				rBlockDesc.x_offset, rBlockDesc.y_offset);
		}

		return true;
	}
}

namespace MHZX
{
	
	template < class BaseClass >
	class CStaticSingletonTemplate : public BaseClass
	{
	public:
		static CStaticSingletonTemplate<BaseClass> & GetInstance()
		{
			static CStaticSingletonTemplate<BaseClass> instance;
			return instance;
		}
		virtual void Release()
		{
			// Do not need to delete static instance.
		}
	};

	IPictureDecoder* CPictureDecoderFactory::CreatePictureDecoder(int format)
	{
		int storageflag = (int)HIWORD( format );
		switch ( storageflag )
		{
		case PSF_BITMAP:
		case PSF_DDS:
			{
				// �ֿ�ͼ���ʽ��ͳһ��CPICDecoder��
				int pixelformat = (int)LOWORD( format );
				if( pixelformat >= PIX_FMT_R5G6B5 && pixelformat <= PIX_FMT_DXT5n )
				{
					return &CStaticSingletonTemplate<CPICDecoder>::GetInstance();
				}
				std::wcerr<< L"��֧�ֵ�ͼ���ʽ��storage: " << storageflag << L" format:" << pixelformat << std::endl;			
				return NULL;
			}
		case PSF_NATIVE_PNG:
			{
				return &CStaticSingletonTemplate<CPNGDecoder>::GetInstance();
			}
		case PSF_NATIVE_TGA:
			{
				return &CStaticSingletonTemplate<CTGADecoder>::GetInstance();
			}
		case PSF_NATIVE_DDS:
			{
				return &CStaticSingletonTemplate<CDDSDecoder>::GetInstance();
			}
		default:
			std::wcerr<< L"��֧�ֵ�ͼ���ʽ��storage: " << storageflag << std::endl;			
			return NULL;
		}

		return NULL;
	}
}