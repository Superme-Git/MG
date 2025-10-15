//////////////////////////////////////////////////////////////////////////
//
// PicTranslator.cpp
//
#include "PicTranslator.h"

#include "../common/translator.h"

extern translator_source_ptr my_init_read_pic (translator_common_ptr cinfo);
extern translator_dest_ptr my_init_write_pic (translator_common_ptr cinfo);
extern translator_source_ptr my_init_read_b0x (translator_common_ptr cinfo);
extern translator_dest_ptr my_init_write_b0x (translator_common_ptr cinfo);

enum PixelFormat
{
	PIX_FMT_FIRST = 0,
	PIX_FMT_R5G6B5 = 0,	// 0
	PIX_FMT_A4R4G4B4,	// 1
	PIX_FMT_R8G8B8,		// 2
	PIX_FMT_A8R8G8B8,	// 3
	PIX_FMT_X8R8G8B8,	// 4
	PIX_FMT_A8,			// 5 8bit gray
	PIX_FMT_A1R5G5B5,	// 6
	PIX_FMT_X1R5G5B5,	// 7
	PIX_FMT_COUNT = 8,	
	
	//以下目前不支持
	PIX_FMT_DXT1 = 8,
	PIX_FMT_DXT2,
	PIX_FMT_DXT3,
	PIX_FMT_DXT4,
	PIX_FMT_DXT5,
};

namespace PFSX {

	namespace PIC {

		class CPicEncoder : public CEncoder
		{
		public:
			explicit CPicEncoder( PixelFormat subtype ) 
				: CEncoder( MakeTypeIDByName('P', '0', '0'+subtype) )
			{
			}

			explicit CPicEncoder( TSID tsid ) 
				: CEncoder( tsid )
			{
			}
		
			virtual int DoEncode();

			virtual CEncoder* Clone() const
			{
				return new CPicEncoder( GetID() );
			}

			virtual void Delete() { delete this; }
		};
	
		class CPicDecoder : public CDecoder 
		{
		public:
			explicit CPicDecoder( PixelFormat subtype ) 
				: CDecoder( MakeTypeIDByName('P', '0', '0'+subtype) )
			{
			}

			explicit CPicDecoder( TSID tsid ) 
				: CDecoder( tsid )
			{
			}

			virtual int DoDecode();

			virtual CDecoder* Clone() const
			{
				return new CPicDecoder(GetID());
			}

			virtual void Delete() { delete this; }
		};

		class CB0XDecoder : public CDecoder 
		{
		public:
			explicit CB0XDecoder( PixelFormat subtype ) 
				: CDecoder( MakeTypeIDByName('B', '0', '0'+subtype) )
			{
			}

			explicit CB0XDecoder( TSID tsid ) 
				: CDecoder( tsid )
			{
			}

			virtual int DoDecode();

			virtual CDecoder* Clone() const
			{
				return new CB0XDecoder(GetID());
			}

			virtual void Delete() { delete this; }
		};

		class CB0XEncoder : public CEncoder 
		{
		public:
			explicit CB0XEncoder( PixelFormat subtype ) 
				: CEncoder( MakeTypeIDByName('B', '0', '0'+subtype) )
			{
			}

			explicit CB0XEncoder( TSID tsid ) 
				: CEncoder( tsid )
			{
			}

			virtual int DoEncode();

			virtual CEncoder* Clone() const
			{
				return new CB0XEncoder(GetID());
			}

			virtual void Delete() { delete this; }
		};


		class _CPicTranslatorAutoRegister
		{
		public:
			_CPicTranslatorAutoRegister()
			{
				for( long lIndex = PIX_FMT_FIRST; lIndex < PIX_FMT_COUNT; lIndex ++ )
				{
					CTranslateManager::GetInstance().RegisterEncoder( CPicEncoder( (PixelFormat)lIndex) );
					CTranslateManager::GetInstance().RegisterDecoder( CPicDecoder( (PixelFormat)lIndex) );
					CTranslateManager::GetInstance().RegisterEncoder( CB0XEncoder( (PixelFormat)lIndex) );
					CTranslateManager::GetInstance().RegisterDecoder( CB0XDecoder( (PixelFormat)lIndex) );
				}
			}
		}; //for _autoRegister;

		int Initialize()
		{
			static _CPicTranslatorAutoRegister _autoRegister;
			return PFS::EC_SUCCESS;
		}

		long GetEncoderCount()
		{
			return PIX_FMT_COUNT*2;
		}

		void* GetEncoderItem(long lIndex)
		{
			if ( lIndex >= PIX_FMT_FIRST  && lIndex < PIX_FMT_COUNT  )
			{
				return CPicEncoder( (PixelFormat)(lIndex) ).Clone();
			}
			else if ( lIndex-=PIX_FMT_COUNT, 
				(lIndex >= PIX_FMT_FIRST && lIndex < PIX_FMT_COUNT) )
			{
				return CB0XEncoder( (PixelFormat)(lIndex) ).Clone();
			}
			else
			{
				return NULL;
			}
		}

		long GetDecoderCount()
		{
			return PIX_FMT_COUNT*2;
		}

		void* GetDecoderItem(long lIndex)
		{
			if ( lIndex >= PIX_FMT_FIRST  && lIndex < PIX_FMT_COUNT  )
			{
				return CPicDecoder( (PixelFormat)(lIndex) ).Clone();
			}
			else if ( lIndex-=PIX_FMT_COUNT, 
				(lIndex >= PIX_FMT_FIRST && lIndex < PIX_FMT_COUNT) )
			{
				return CB0XDecoder( (PixelFormat)(lIndex) ).Clone();
			}
			else
			{
				return NULL;
			}
		}

#define JMESSAGE(code,string)	string ,
		static const char * const cdjpeg_message_table[] = {
#include "cderror.h"
			NULL
		};


#ifndef EXIT_WARNING  
#define EXIT_WARNING  2
#endif

		using PFS::CBaseFile;

		//////////////////////////////////////////////////////////////////////////
		//
		// Translate Pic to bmp
		//
		int Pic2BmpProc( CBaseFile* pInputStream, CBaseFile* pOutputStream,
			translator_source_ptr (*init_read_pic) (translator_common_ptr) )
		{
			translator_common_struct cinfo_object;
			translator_common_ptr cinfo = &cinfo_object;
			create_common_translator( cinfo );
			my_translator_error( cinfo->err );
			/* Add some application-specific error messages (from cderror.h) */
			cinfo->err->addon_message_table = cdjpeg_message_table;
			cinfo->err->first_addon_message = JMSG_FIRSTADDONCODE;
			cinfo->err->last_addon_message = JMSG_LASTADDONCODE;

			my_translator_progress( cinfo->progress );

			translator_source_ptr src_mgr = init_read_pic(cinfo);
			src_mgr->input_file = pInputStream;
			(*src_mgr->start_input)(src_mgr);
			(*src_mgr->preload_image)(src_mgr);

			translator_dest_ptr dst_mgr = my_init_write_bmp(cinfo);
			dst_mgr->output_file = pOutputStream;
			(*dst_mgr->start_output)(dst_mgr);

			JDIMENSION row_data;
			while ( (row_data = (*src_mgr->get_pixel_rows)( src_mgr ) ) != 0 )
			{
				// link the buffer
				dst_mgr->buffer = src_mgr->buffer;
				(*dst_mgr->put_pixel_rows)(dst_mgr, row_data);
			}

			(*src_mgr->finish_input)( src_mgr );
			(*dst_mgr->finish_output)( dst_mgr );

			destroy_translator_source (src_mgr);
			destroy_translator_dest (dst_mgr);

			destroy_common_translator( cinfo );

			return 0;
		}

		int Bmp2PicProc( CBaseFile* pInputStream, CBaseFile* pOutputStream, int subtype,
			translator_dest_ptr (*init_write_pic)(translator_common_ptr ) )
		{
			translator_common_struct cinfo_object;
			translator_common_ptr cinfo = &cinfo_object;
			create_common_translator( cinfo );
			my_translator_error( cinfo->err );
			/* Add some application-specific error messages (from cderror.h) */
			cinfo->err->addon_message_table = cdjpeg_message_table;
			cinfo->err->first_addon_message = JMSG_FIRSTADDONCODE;
			cinfo->err->last_addon_message = JMSG_LASTADDONCODE;

			my_translator_progress( cinfo->progress );

			translator_source_ptr src_mgr = my_init_read_bmp(cinfo);
			src_mgr->input_file = pInputStream;
			(*src_mgr->start_input)(src_mgr);
			(*src_mgr->preload_image)(src_mgr);

			// 根据输入文件调整文件格式影射方式
			if ( cinfo->input_components == 3 || 
				cinfo->input_components == 4 )
			{
				switch ( subtype )
				{
				case PIX_FMT_R5G6B5:
					cinfo->out_color_components = 3;
					cinfo->data_precision = 5;
					break;
				case PIX_FMT_A4R4G4B4:
					cinfo->out_color_components = 4;
					cinfo->data_precision = 4;
					break;
				case PIX_FMT_A8R8G8B8: 
				case PIX_FMT_X8R8G8B8:
					cinfo->out_color_components = 4;
					cinfo->data_precision = 8;
					break;
				case PIX_FMT_R8G8B8:
					cinfo->out_color_components = 3;
					cinfo->data_precision = 8;
					break;
				case PIX_FMT_A1R5G5B5: 
				case PIX_FMT_X1R5G5B5:
					cinfo->out_color_components = 4;
					cinfo->data_precision = 5;
					break;
				default:
					cinfo->out_color_components = cinfo->input_components;
					cinfo->data_precision = 8;
					break;
				}
			}
			else
			{
				throw (int)1; // bad input
			}

			translator_dest_ptr dst_mgr = init_write_pic(cinfo);
			dst_mgr->output_file = pOutputStream;
			(*dst_mgr->start_output)(dst_mgr);

			JDIMENSION row_data;
			while ( (row_data = (*src_mgr->get_pixel_rows)( src_mgr ) ) != 0 )
			{
				// link the buffer
				dst_mgr->buffer = src_mgr->buffer;
				(*dst_mgr->put_pixel_rows)(dst_mgr, row_data);
			}

			(*src_mgr->finish_input)( src_mgr );
			(*dst_mgr->finish_output)( dst_mgr );

			destroy_translator_source (src_mgr);
			destroy_translator_dest (dst_mgr);

			destroy_common_translator( cinfo );

			return 0;
		}

		// Bmp2Pic
		//
		int CPicEncoder::DoEncode()
		{
			int iRet = PFS::ECX_BADFORMAT;

			try
			{
				TSID tsid = GetID();
				int	subtype = ((tsid >> 16)&0xFF) - '0';
				iRet = Bmp2PicProc( &GetSource(), &GetDestination(), subtype, my_init_write_pic );
			}
			catch (int)
			{
				return PFS::ECX_BADFORMAT;
			}

			if ( iRet == EXIT_SUCCESS )
			{
				return PFS::EC_SUCCESS;
			}
			else
			{
				return PFS::ECX_BADFORMAT;
			}
		}

		// Pic to Bmp
		int CPicDecoder::DoDecode()
		{
			int iRet = PFS::ECX_BADFORMAT;

			try
			{
				iRet = Pic2BmpProc( &GetSource(), &GetDestination(), my_init_read_pic );
			}
			catch (int)
			{
				return PFS::ECX_BADFORMAT;
			}

			if ( iRet == EXIT_SUCCESS )
			{
				return PFS::EC_SUCCESS;
			}
			else
			{
				return PFS::ECX_BADFORMAT;
			}
		}

		// Bmp to B0x
		int CB0XEncoder::DoEncode()
		{
			int iRet = PFS::ECX_BADFORMAT;
			try
			{
				TSID tsid = GetID();
				int	subtype = ((tsid >> 16) & 0xFF) - '0';
				iRet = Bmp2PicProc( &GetSource(), &GetDestination(), subtype, my_init_write_b0x );
			}
			catch (int)
			{
				return PFS::ECX_BADFORMAT;
			}

			if ( iRet == EXIT_SUCCESS )
			{
				return PFS::EC_SUCCESS;
			}
			else
			{
				return PFS::ECX_BADFORMAT;
			}
		}

		// B0X to Bmp
		int CB0XDecoder::DoDecode()
		{
			int iRet = PFS::ECX_BADFORMAT;

			try
			{
				iRet = Pic2BmpProc( &GetSource(), &GetDestination(), my_init_read_b0x );
			}
			catch (int)
			{
				return PFS::ECX_BADFORMAT;
			}

			if ( iRet == EXIT_SUCCESS )
			{
				return PFS::EC_SUCCESS;
			}
			else
			{
				return PFS::ECX_BADFORMAT;
			}
		}

	}
}

