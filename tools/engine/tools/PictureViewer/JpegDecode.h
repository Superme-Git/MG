
#pragma once

#define JPEG_CJPEG_DJPEG	/* define proper options in jconfig.h */
#define JPEG_INTERNAL_OPTIONS	/* cjpeg.c,djpeg.c need to see xxx_SUPPORTED */
#include "jinclude.h"
#include "jpeglib.h"
#include "jerror.h"		/* get library error codes too */
#include "cderror.h"		/* get application-specific error codes */
#include <afx.h> /* for mfc CFile */

#define USE_WINDOWS_MESSAGEBOX

/*
* To support 12-bit JPEG data, we'd have to scale output down to 8 bits.
* This is not yet implemented.
*/

#if BITS_IN_JSAMPLE != 8
Sorry, this code only copes with 8-bit JSAMPLEs. /* deliberate syntax err */
#endif

typedef unsigned char U_CHAR;
#define UCH(x)	((int) (x))

class CFile;
typedef CFile CBaseFile;

#undef JFWRITE
#undef JFREAD

#define JFREAD(file,buf,sizeofbuf)  \
	file->Read((void *) (buf), (size_t)(sizeofbuf))

#define JFWRITE(stream, buffer, size) \
	( stream->Write(buffer, size), size )

#define	ReadOK(file,buffer,len)	(JFREAD(file,buffer,len) == ((size_t) (len)))

/*
* Object interface for translator's output file encoding modules
*/

typedef struct my_djpeg_dest_struct * my_djpeg_dest_ptr;

struct my_djpeg_dest_struct {
	/* start_output is called after jpeg_start_decompress finishes.
	* The color map will be ready at this time, if one is needed.
	*/
	void (*start_output)(j_decompress_ptr cinfo,
		my_djpeg_dest_ptr dinfo);
	/* Emit the specified number of pixel rows from the buffer. */
	void (*put_pixel_rows)(j_decompress_ptr cinfo,
		my_djpeg_dest_ptr dinfo,
		JDIMENSION rows_supplied);
	/* Finish up at the end of the image. */
	void (*finish_output)(j_decompress_ptr cinfo,
		my_djpeg_dest_ptr dinfo);

	/* Target file spec; filled in by djpeg.c after object is created. */
	union
	{
		CBaseFile * output_file; // NULL - no need destination file - yanghaibo
		HBITMAP hBitmap;		// DIB - Created by CreateDIBSection()
	};

	/* Output pixel-row buffer.  Created by module init or start_output.
	* Width is cinfo->output_width * cinfo->output_components;
	* height is buffer_height.
	*/
	JSAMPARRAY buffer;
	JDIMENSION buffer_height;
};


typedef struct my_cjpeg_source_struct * my_cjpeg_source_ptr ;

struct my_cjpeg_source_struct  {
	void (*start_input)(j_compress_ptr cinfo,
		my_cjpeg_source_ptr sinfo);

	/* preload_image maybe as a separated procedure for alpha channel info - yanghaibo */
	JDIMENSION (*preload_image)(j_compress_ptr cinfo, 
		my_cjpeg_source_ptr sinfo);
	JDIMENSION (*get_pixel_rows)(j_compress_ptr cinfo,
		my_cjpeg_source_ptr sinfo);
	void (*finish_input)(j_compress_ptr cinfo,
		my_cjpeg_source_ptr sinfo);

	CBaseFile *input_file;

	JSAMPARRAY buffer;
	JDIMENSION buffer_height;

	/* alpha channel info - yanghaibo */
	JOCTET* alpha_channel;				/*alpha channel data*/
	unsigned int alpha_channel_size;	/*alpha channel data size*/
};


struct translator_progress_mgr {
	struct jpeg_progress_mgr pub;	/* fields known to JPEG library */
	int completed_extra_passes;	/* extra passes completed */
	int total_extra_passes;	/* total extra */
	/* last printed percentage stored here to avoid multiple printouts */
	int percent_done;
};

typedef struct translator_progress_mgr * translator_progress_ptr;

extern my_cjpeg_source_ptr my_init_read_bmp (j_compress_ptr cinfo);
extern my_djpeg_dest_ptr my_init_write_bmp (j_decompress_ptr cinfo);
