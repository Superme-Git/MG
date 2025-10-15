#include "TargaTranslator.h"

#include "../common/translator.h"

extern translator_dest_ptr my_init_write_targa (translator_common_ptr cinfo);
extern translator_source_ptr my_init_read_targa (translator_common_ptr cinfo);

namespace PFSX {

	namespace TARGA {

		class CBmp2TargaEncoder : public CEncoder
		{
		public:
			CBmp2TargaEncoder() 
				: CEncoder( MakeTypeIDByName('T', 'G', 'A') )
			{
			}

			virtual int DoEncode();
			virtual CEncoder* Clone() const
			{
				return new CBmp2TargaEncoder();
			}

			virtual void Delete() { delete this; }
		};

		class CTarga2BmpDecoder : public CDecoder
		{
		public:
			CTarga2BmpDecoder() 
				: CDecoder( MakeTypeIDByName('T', 'G', 'A' ) )
			{
			}

			virtual int DoDecode();
			virtual CDecoder* Clone() const
			{
				return new CTarga2BmpDecoder();
			}

			virtual void Delete() { delete this; }
		};

		class _CTargaTranslatorAutoRegister
		{
		public:
			_CTargaTranslatorAutoRegister()
			{
				CTranslateManager::GetInstance().RegisterEncoder( CBmp2TargaEncoder() );
				CTranslateManager::GetInstance().RegisterDecoder( CTarga2BmpDecoder() );
			}
		}; //for _autoRegister;

		int Initialize()
		{
			static _CTargaTranslatorAutoRegister _autoRegister;
			return PFS::EC_SUCCESS;
		}

		long GetEncoderCount()
		{
			return 1;
		}

		void* GetEncoderItem(long lIndex)
		{
			if ( lIndex == 0 )
			{
				return CBmp2TargaEncoder().Clone();
			}
			else
			{
				return NULL;
			}
		}

		long GetDecoderCount()
		{
			return 1;
		}

		void* GetDecoderItem(long lIndex)
		{
			if ( lIndex == 0 )
			{
				return CTarga2BmpDecoder().Clone();
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
		// Translate bmp to targa
		//
		int Bmp2TargaProc( CBaseFile* pInputStream, CBaseFile* pOutputStream )
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

			translator_dest_ptr dst_mgr = my_init_write_targa (cinfo);
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

		//////////////////////////////////////////////////////////////////////////
		//
		// Translate targa to bmp
		//
		int Targa2BmpProc( CBaseFile* pInputStream, CBaseFile* pOutputStream )
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

			translator_source_ptr src_mgr = my_init_read_targa(cinfo);
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

		int CBmp2TargaEncoder::DoEncode()
		{
			int iRet = PFS::ECX_BADFORMAT;

			try
			{
				iRet = Bmp2TargaProc( &GetSource(), &GetDestination() );
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

		int CTarga2BmpDecoder::DoDecode()
		{
			int iRet = PFS::ECX_BADFORMAT;

			try
			{
				iRet = Targa2BmpProc( &GetSource(), &GetDestination() );
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

	} // TARTA

}// PFSX