// JpgTranslator.cpp

#include <../../contrib/zlib-1.2.3/zlib.h>

#include "Translator.h"


#include "JpgTranslator.h"
extern struct jpeg_error_mgr * my_jpeg_error (struct jpeg_error_mgr * err);
extern void my_jpeg_dest (j_compress_ptr cinfo, CBaseFile * outfile);
extern void my_jpeg_src (j_decompress_ptr cinfo, CBaseFile * infile);

namespace PFSX {

	namespace JPEG {

		class CBmp2JpgEncoder : public CEncoder
		{
		public:
			CBmp2JpgEncoder() 
				: CEncoder( MakeTypeIDByName('J','F', 'I', 'F') )
			{
			}

			virtual int DoEncode();
			virtual CEncoder* Clone() const
			{
				return new CBmp2JpgEncoder();
			}

			virtual void Delete() { delete this; }
		};

		class CJpg2BmpDecoder : public CDecoder
		{
		public:
			CJpg2BmpDecoder() 
				: CDecoder(MakeTypeIDByName('J','F', 'I', 'F' ))
			{
			}

			virtual int DoDecode();
			virtual CDecoder* Clone() const
			{
				return new CJpg2BmpDecoder();
			}

			virtual void Delete() { delete this; }
		};

		class _CJpgTranslatorAutoRegister
		{
		public:
			_CJpgTranslatorAutoRegister()
			{
				CTranslateManager::GetInstance().RegisterEncoder( CBmp2JpgEncoder() );
				CTranslateManager::GetInstance().RegisterDecoder( CJpg2BmpDecoder() );
			}
		}; //for _autoRegister;

		int Initialize()
		{
			static _CJpgTranslatorAutoRegister _autoRegister;
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
				return CBmp2JpgEncoder().Clone();
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
				return CJpg2BmpDecoder().Clone();
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

		/* Read next byte */
		static unsigned int jpeg_getc (j_decompress_ptr cinfo)
		{
			struct jpeg_source_mgr * datasrc = cinfo->src;

			if (datasrc->bytes_in_buffer == 0) {
				if (! (*datasrc->fill_input_buffer) (cinfo))
					ERREXIT(cinfo, JERR_CANT_SUSPEND);
			}
			datasrc->bytes_in_buffer--;
			return GETJOCTET(*datasrc->next_input_byte++);
		}

		boolean alpha_channel_app4_marker (j_decompress_ptr cinfo)
		{
			boolean traceit = (cinfo->err->trace_level >= 1);
			INT32 length;
			unsigned int ch;
			unsigned int lastch = 0;

			unsigned int total_length = 0, page_size = 0;
			unsigned int page_count = 0, page_index = 0;

			unsigned int source_length = 0, compressed_length = 0;

			JOCTET* pch = NULL;
			JOCTET* tempbuffer = NULL;

			length = jpeg_getc(cinfo) << 8;
			length += jpeg_getc(cinfo);
			length -= 2;			/* discount the length word itself */

			if (traceit)
			{
				printf("APP%d, length %ld:\n", cinfo->unread_marker - JPEG_APP0, (long) length);
			}

			if ( length < 16 )
			{
				goto __NOT_ALPHA_MARKER;
			}

			for( unsigned int i = 0; i < 4; i ++ )
			{
				ch = jpeg_getc(cinfo);
				length --;
				if( ch != "ALZP"[i] )
				{
					goto __NOT_ALPHA_MARKER;
				}
			}

			unsigned int version;
			version = jpeg_getc(cinfo) << 8;
			version += jpeg_getc(cinfo);
			length -= 2;

			// Check version is 1.0 or not 
			if( version > 0x100 )
			{
				// unsupported version
				goto __NOT_ALPHA_MARKER;
			}

			total_length = jpeg_getc(cinfo) << 24;
			total_length += jpeg_getc(cinfo) << 16;
			total_length += jpeg_getc(cinfo) << 8;
			total_length += jpeg_getc(cinfo);

			page_count = jpeg_getc(cinfo);
			page_index = jpeg_getc(cinfo);

			page_size = jpeg_getc(cinfo) << 8;
			page_size += jpeg_getc(cinfo);

			compressed_length = jpeg_getc(cinfo) << 8;
			compressed_length += jpeg_getc(cinfo);

			length -= 10;

			if( compressed_length != length )
			{
				assert( compressed_length == length );
				goto __NOT_ALPHA_MARKER;
			}

			tempbuffer = (JOCTET*)malloc( compressed_length );

			if( cinfo->alpha_channel == NULL )
			{
				cinfo->alpha_channel_size = total_length; 
				cinfo->alpha_channel = (JOCTET*)(cinfo->mem->alloc_large)( 
					(j_common_ptr) cinfo, JPOOL_IMAGE, total_length );
			}

			for( pch = tempbuffer; length > 0; length-- )
			{
				*pch ++ = jpeg_getc(cinfo);
			}

			unsigned long result_length = cinfo->alpha_channel_size-cinfo->alpha_channel_filled;

			JOCTET* alpha_channel_buffer = &cinfo->alpha_channel[cinfo->alpha_channel_filled];

			uncompress( alpha_channel_buffer, &result_length, tempbuffer, compressed_length );

			if( result_length != page_size )
			{
				// BAD data
				goto __NOT_ALPHA_MARKER;
			}

			cinfo->alpha_channel_filled += result_length;

			if( total_length == cinfo->alpha_channel_filled &&
				page_index == page_count-1 )
			{
				cinfo->saw_alpha_channel_marker = TRUE;
			}

			if(tempbuffer != NULL)
			{
				free(tempbuffer);
			}

			return TRUE;

__NOT_ALPHA_MARKER:
			// Not Alpha channel marker

			if (traceit)
			{
				printf( "Not a valid alpha zip marker!\n" );
			}

			cinfo->saw_alpha_channel_marker = FALSE;

			cinfo->src->skip_input_data(cinfo, length);

			if(tempbuffer != NULL)
			{
				free ( tempbuffer);
			}

			return TRUE;

		}

		//////////////////////////////////////////////////////////////////////////
		//
		// Compress bmp to jpg
		//
		static int CompressProc( CBaseFile* pInputStream, CBaseFile* pOutputStream)
		{
			struct jpeg_compress_struct cinfo;
			struct jpeg_error_mgr jerr;

#ifdef PROGRESS_REPORT
			struct my_cdjpeg_progress_mgr progress;
#endif

			my_cjpeg_source_ptr src_mgr = NULL;

			JDIMENSION num_scanlines = 0;

			/* Initialize the JPEG compression object with default error handling. */
			cinfo.err = my_jpeg_error(&jerr);
			jpeg_create_compress(&cinfo);
			/* Add some application-specific error messages (from cderror.h) */
			jerr.addon_message_table = cdjpeg_message_table;
			jerr.first_addon_message = JMSG_FIRSTADDONCODE;
			jerr.last_addon_message = JMSG_LASTADDONCODE;

			/* Now safe to enable signal catcher. */
#ifdef NEED_SIGNAL_CATCHER
			enable_signal_catcher((j_common_ptr) &cinfo);
#endif

			/* Initialize JPEG parameters.
			* Much of this may be overridden later.
			* In particular, we don't yet know the input file's color space,
			* but we need to provide some value for jpeg_set_defaults() to work.
			*/

			cinfo.in_color_space = JCS_RGB; /* arbitrary guess */
			jpeg_set_defaults(&cinfo);

			// set the quality from default 75 to 90
			jpeg_set_quality(&cinfo, 90, TRUE);

#ifdef PROGRESS_REPORT
			my_start_progress_monitor((j_common_ptr) &cinfo, &progress);
#endif

			/* Figure out the input file format, and set up to read it. */

			src_mgr = my_init_read_bmp(&cinfo);
			src_mgr->input_file = pInputStream;

			/* Read the input file header to obtain file size & colorspace. */
			(*src_mgr->start_input) (&cinfo, src_mgr);

			/* Now that we know input colorspace, fix colorspace-dependent defaults */
			jpeg_default_colorspace(&cinfo);

			/* Specify data destination for compression */
			my_jpeg_dest(&cinfo, pOutputStream);

			/* Start compressor */
			jpeg_start_compress(&cinfo, TRUE);

			(*src_mgr->preload_image) (&cinfo, src_mgr);

			/* Write alpha channel marker */
			/* Write a special marker.  See libjpeg.doc concerning safe usage. */
			if ( src_mgr->alpha_channel != NULL && src_mgr->alpha_channel_size != 0 )
			{
				unsigned int channel_size_total = src_mgr->alpha_channel_size;

				unsigned int page_size_max = 65000;
				unsigned int page_count = (channel_size_total+page_size_max-1)/page_size_max;

				unsigned int page_size = channel_size_total;
				if ( page_size > page_size_max )
				{
					page_size = page_size_max;
				}

				unsigned int last_page_size = channel_size_total%page_size_max;

				JOCTET* markerBuffer = (JOCTET*)malloc(page_size + 16);

				JOCTET* markerPtr = markerBuffer;
				// ALZP - ALpha ZiP signature 
				markerPtr[0] = 'A';
				markerPtr[1] = 'L';
				markerPtr[2] = 'Z';
				markerPtr[3] = 'P';
				markerPtr += 4;

				markerPtr[0] = 1;	// major
				markerPtr[1] = 0;	// minor
				markerPtr += 2;		// 2 bytes for version 1.0

				markerPtr[0] = (JOCTET)((channel_size_total>> 24 ) & 0xFF); // big endian 4 bytes total_channel_size
				markerPtr[1] = (JOCTET)((channel_size_total>> 16 ) & 0xFF); // 
				markerPtr[2] = (JOCTET)((channel_size_total>> 8 ) & 0xFF); // 
				markerPtr[3] = (JOCTET)(channel_size_total & 0xFF); //
				markerPtr += 4;
				markerPtr[0] = (JOCTET)(page_count & 0xFF); // 1 bytes page count 

				JOCTET* current_page = NULL;

				for( unsigned int iPage = 0; iPage < page_count; iPage ++ )
				{
					if( iPage == page_count-1) // last page
					{
						page_size = last_page_size;
						current_page = &src_mgr->alpha_channel[ channel_size_total - last_page_size ];
					}
					else
					{
						page_size = page_size_max;
						current_page = &src_mgr->alpha_channel[iPage*page_size_max];
					}

					markerPtr = markerBuffer + 11;
					markerPtr[0] = (JOCTET)(iPage & 0xFF);		// 1 bytes page index
					markerPtr += 1;

					markerPtr[0] = (JOCTET)((page_size >> 8) & 0xFF); // big endian page size
					markerPtr[1] = (JOCTET)(page_size & 0xFF);
					markerPtr += 2;

					markerPtr += 2; // 2 reserved bytes for result_size

					unsigned long result_size = page_size;

					// Zip compress
					compress(markerPtr, &result_size, current_page, page_size);

					markerPtr[-2] = (JOCTET)((result_size >> 8) & 0xFF); // big endian compressed size
					markerPtr[-1] = (JOCTET)(result_size & 0xFF);

					jpeg_write_marker(&cinfo, JPEG_APP0+4, markerBuffer, result_size + 16);
				}

				free( markerBuffer );
			}

			/* Process data */
			while (cinfo.next_scanline < cinfo.image_height) {
				num_scanlines = (*src_mgr->get_pixel_rows) (&cinfo, src_mgr);
				(void) jpeg_write_scanlines(&cinfo, src_mgr->buffer, num_scanlines);
			}

			/* Finish compression and release memory */
			(*src_mgr->finish_input) (&cinfo, src_mgr);
			jpeg_finish_compress(&cinfo);

			jpeg_destroy_compress(&cinfo);

#ifdef PROGRESS_REPORT
			my_end_progress_monitor((j_common_ptr) &cinfo);
#endif

			/* All done. */
			return (jerr.num_warnings ? EXIT_WARNING : EXIT_SUCCESS);
		}

		//////////////////////////////////////////////////////////////////////////
		//
		// Decompress jpg to bmp
		//
		static int DecompressProc( CBaseFile* pInputStream, CBaseFile* pOutputStream )
		{
			struct jpeg_decompress_struct cinfo;
			struct jpeg_error_mgr jerr;

#ifdef PROGRESS_REPORT
			struct my_cdjpeg_progress_mgr progress;
#endif

			my_djpeg_dest_ptr dest_mgr = NULL;

			JDIMENSION num_scanlines;

			/* Initialize the JPEG decompression object with default error handling. */
			cinfo.err = my_jpeg_error(&jerr);
			jpeg_create_decompress(&cinfo);
			/* Add some application-specific error messages (from cderror.h) */
			jerr.addon_message_table = cdjpeg_message_table;
			jerr.first_addon_message = JMSG_FIRSTADDONCODE;
			jerr.last_addon_message = JMSG_LASTADDONCODE;

			/* Insert custom marker processor for COM and APP12.
			* APP12 is used by some digital camera makers for textual info,
			* so we provide the ability to display it as text.
			* If you like, additional APPn marker types can be selected for display,
			* but don't try to override APP0 or APP14 this way (see libjpeg.doc).
			*/

			//jpeg_set_marker_processor(&cinfo, JPEG_COM, print_text_marker);

			jpeg_set_marker_processor(&cinfo, JPEG_APP0+4, alpha_channel_app4_marker);

			//jpeg_set_marker_processor(&cinfo, JPEG_APP0+12, print_text_marker);

			/* Now safe to enable signal catcher. */
#ifdef NEED_SIGNAL_CATCHER
			enable_signal_catcher((j_common_ptr) &cinfo);
#endif

#ifdef PROGRESS_REPORT
			my_start_progress_monitor((j_common_ptr) &cinfo, &progress);
#endif

			/* Specify data source for decompression */
			my_jpeg_src(&cinfo, pInputStream );

			/* Read file header, set default decompression parameters */
			(void) jpeg_read_header(&cinfo, TRUE);

			/* Initialize the output module now to let it override any crucial
			* option settings (for instance, GIF wants to force color quantization).
			*/
			dest_mgr = my_init_write_bmp(&cinfo, FALSE);
			dest_mgr->output_file = pOutputStream;

			/* Start decompressor */
			(void) jpeg_start_decompress(&cinfo);

			/* Write output file header */
			(*dest_mgr->start_output) (&cinfo, dest_mgr);

			/* Process data */
			while (cinfo.output_scanline < cinfo.output_height) {
				num_scanlines = jpeg_read_scanlines(&cinfo, dest_mgr->buffer,
					dest_mgr->buffer_height);
				(*dest_mgr->put_pixel_rows) (&cinfo, dest_mgr, num_scanlines);
			}

#ifdef PROGRESS_REPORT
			/* Hack: count final pass as done in case finish_output does an extra pass.
			* The library won't have updated completed_passes.
			*/
			progress.pub.completed_passes = progress.pub.total_passes;
#endif

			/* Finish decompression and release memory.
			* I must do it in this order because output module has allocated memory
			* of lifespan JPOOL_IMAGE; it needs to finish before releasing memory.
			*/
			(*dest_mgr->finish_output) (&cinfo, dest_mgr);
			(void) jpeg_finish_decompress(&cinfo);

			jpeg_destroy_decompress(&cinfo);

#ifdef PROGRESS_REPORT
			my_end_progress_monitor((j_common_ptr) &cinfo);
#endif
			/* All done. */
			/* allow only 1 warning - by yanghaibo */
			return jerr.num_warnings > 1 ? EXIT_WARNING : EXIT_SUCCESS;
		}

		int CBmp2JpgEncoder::DoEncode()
		{
			int iRet = PFS::ECX_BADFORMAT;

			try
			{
				iRet = CompressProc( &GetSource(), &GetDestination() );
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

		int CJpg2BmpDecoder::DoDecode()
		{
			int iRet = PFS::ECX_BADFORMAT;

			try
			{
				iRet = DecompressProc( &GetSource(), &GetDestination() );
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

	} // JPEG
} // PFSX