//////////////////////////////////////////////////////////////////////////
//
// JpegUtil.cpp
//
// author: yanghaibo	
//

#include "FileStream.h"
#include "JpegEncodeDest.h"
#include "JpegCodecError.h"
#include "MemoryReader.h"

#define EXIT_WARNING  2

#define JPEG_UTIL_API __declspec( dllexport )
#include "JpegUtil.h"

// pbData size = nWidth * nHeight * size( DWORD)
// pixel format a r g b

int CompressMainProc( const BYTE* pbData, int nWidth, int nHeight, IStream * pOutputStream );

int JUtilRawMemoryToJpegFile(const BYTE* pbData, int nWidth, int nHeight, LPCWSTR lpszFilePath)
{
	IStream * pOutputStream = NULL;
	ULONG nSize = nWidth * nHeight * 4;

	if ( pbData == NULL || nSize == 0 || IsBadReadPtr( pbData, nSize ) )
	{
		return -1;
	}

	HRESULT hr = CreateFileStreamForWrite( lpszFilePath, &pOutputStream );
	if( FAILED(hr) )
	{
		return -2;
	}
	
	try
	{
		int nRet = CompressMainProc( pbData, nWidth, nHeight, pOutputStream );
		if ( nRet == 0 )
		{
			pOutputStream->Release();
			return 0;
		}
		else
		{
			pOutputStream->Release();
			return -3;
		}
	}
	catch (int)
	{
		pOutputStream->Release();
		return -4;
	}

}

#define JMESSAGE(code,string)	string ,

static const char * const cdjpeg_message_table[] = {
#include "cderror.h"
	NULL
};

int CompressMainProc( const BYTE* pbData, int nWidth, int nHeight, IStream * pOutputStream )
{
	struct jpeg_compress_struct cinfo;
	struct jpeg_error_mgr jerr;

	my_cjpeg_source_ptr src_mgr = NULL;
	
	JDIMENSION num_scanlines = 0;

	/* Initialize the JPEG compression object with default error handling. */
	cinfo.err = my_jpeg_error(&jerr);
	jpeg_create_compress(&cinfo);
	/* Add some application-specific error messages (from cderror.h) */
	jerr.addon_message_table = cdjpeg_message_table;
	jerr.first_addon_message = JMSG_FIRSTADDONCODE;
	jerr.last_addon_message = JMSG_LASTADDONCODE;

	/* Initialize JPEG parameters.
	* Much of this may be overridden later.
	* In particular, we don't yet know the input file's color space,
	* but we need to provide some value for jpeg_set_defaults() to work.
	*/

	cinfo.in_color_space = JCS_RGB; /* arbitrary guess */
	jpeg_set_defaults(&cinfo);

	// Determine the file
	/* Figure out the input file format, and set up to read it. */

	src_mgr = my_init_read_mem(&cinfo, pbData, nWidth, nHeight );
	
	/* Read the input file header to obtain file size & colorspace. */
	(*src_mgr->start_input) (&cinfo, src_mgr);

	/* Now that we know input colorspace, fix colorspace-dependent defaults */
	jpeg_default_colorspace(&cinfo);

	/* Specify pbData destination for compression */
	my_jpeg_dest(&cinfo, pOutputStream );

	/* Start compressor */
	jpeg_start_compress(&cinfo, TRUE);

	/* Process pbData */
	while (cinfo.next_scanline < cinfo.image_height) {
		(*src_mgr->get_pixel_rows) (&cinfo, src_mgr);// always one line
		(void) jpeg_write_scanlines(&cinfo, &src_mgr->buffer, 1);
	}

	/* Finish compression and release memory */
	(*src_mgr->finish_input) (&cinfo, src_mgr);

	jpeg_finish_compress(&cinfo);
	jpeg_destroy_compress(&cinfo);

	/* All done. */
	return (jerr.num_warnings ? EXIT_WARNING : EXIT_SUCCESS);

}
