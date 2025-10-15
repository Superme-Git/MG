//////////////////////////////////////////////////////////////////////////
//
// MemoryReader.cpp
//

#include "MemoryReader.h"

typedef struct _mem_source_struct * mem_source_ptr;

typedef struct _mem_source_struct {
	struct my_cjpeg_source_struct pub; /* public fields */
	j_compress_ptr cinfo;		/* back link saves passing separate parm */
	JDIMENSION	source_row;	/* Current source row number */
	JDIMENSION	row_width;		/* Physical width of scanlines in file */
} mem_source_struct;

static JDIMENSION get_32bit_row (j_compress_ptr cinfo, my_cjpeg_source_ptr sinfo)
{
	mem_source_ptr source = (mem_source_ptr) sinfo;
	register JSAMPROW inptr, outptr;
	register JDIMENSION col;

	/* Fetch next row from virtual array */
	source->source_row--;
	inptr  =  (JSAMPROW)(source->pub.whole_image + source->row_width * source->source_row);

	/* Transfer data.  Note source values are in BGR order
	* (even though Microsoft's own documents say the opposite).
	*/	
	outptr = source->pub.buffer;

	for (col = cinfo->image_width; col > 0; col--) {
		outptr[2] = *inptr++;	
		outptr[1] = *inptr++;	
		outptr[0] = *inptr++;	
		inptr++;				/* alpha channel */
		outptr += 3;
	}

	return 1;
}

/*
* Read the file header; return image size and component count.
*/

static void
start_input_mem (j_compress_ptr cinfo, my_cjpeg_source_ptr sinfo)
{
}

/*
* Finish up at the end of the file.
*/

METHODDEF(void)
finish_input_mem (j_compress_ptr cinfo, my_cjpeg_source_ptr sinfo)
{
	free( sinfo->buffer );
	sinfo->buffer = NULL;
	sinfo->whole_image = NULL;
}

/*
* The module selection routine for BMP format input.
*/

GLOBAL(my_cjpeg_source_ptr)
my_init_read_mem (j_compress_ptr cinfo, const BYTE* data, int nWidth, int nHeight)
{
	mem_source_ptr source = (mem_source_ptr)malloc(sizeof(mem_source_struct));
	source->cinfo = cinfo;	
	cinfo->image_width = (JDIMENSION) nWidth;
	cinfo->image_height = (JDIMENSION) nHeight;

	source->pub.start_input = start_input_mem;	
	source->pub.get_pixel_rows = get_32bit_row;
	source->pub.finish_input = finish_input_mem;

	source->source_row = cinfo->image_height;
	source->row_width = nWidth * 4;

	/* Allocate one-row buffer for returned data */

	source->pub.buffer = (JSAMPROW)malloc( source->row_width );
	source->pub.whole_image = (BYTE*)data;	
	
	cinfo->in_color_space = JCS_RGB;
	cinfo->input_components = 3;
	cinfo->data_precision = 8;

	return (my_cjpeg_source_ptr) source;
}

