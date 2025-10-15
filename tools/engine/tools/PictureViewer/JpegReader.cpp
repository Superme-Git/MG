// JpgTranslator.cpp

#include "stdafx.h"
#include "JpegDecode.h"

typedef CFile CBaseFile;

extern struct jpeg_error_mgr * my_jpeg_error (struct jpeg_error_mgr * err);
extern void my_jpeg_dest (j_compress_ptr cinfo, CBaseFile * outfile);
extern void my_jpeg_src (j_decompress_ptr cinfo, CBaseFile * infile);

//namespace JPEG {

#define JMESSAGE(code,string)	string ,
	static const char * const cdjpeg_message_table[] = {
#include "cderror.h"
		NULL
	};

#ifndef EXIT_WARNING  
#define EXIT_WARNING  2
#endif
	//////////////////////////////////////////////////////////////////////////
	//
	// Decompress jpg to dib
	//
	HBITMAP DecodeJpegFile( CBaseFile* pInputStream )
	{
		HBITMAP hBitmap = NULL;
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

		//jpeg_set_marker_processor(&cinfo, JPEG_APP0+4, alpha_channel_app4_marker);

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
		dest_mgr = my_init_write_bmp(&cinfo);

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
		hBitmap = dest_mgr->hBitmap;
		dest_mgr->hBitmap = NULL;

		(void) jpeg_finish_decompress(&cinfo);

		jpeg_destroy_decompress(&cinfo);

#ifdef PROGRESS_REPORT
		my_end_progress_monitor((j_common_ptr) &cinfo);
#endif
		/* All done. */
		/* allow only 1 warning - by yanghaibo */
		//return jerr.num_warnings > 1 ? EXIT_WARNING : EXIT_SUCCESS;

		return hBitmap;
	}

//} // JPEG
