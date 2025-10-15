//////////////////////////////////////////////////////////////////////////
//
// BmpWriter.cpp
//

#include "Translator.h"

/*
* Since BMP stores scanlines bottom-to-top, we have to invert the image
* from JPEG's top-to-bottom order.  To do this, we save the outgoing data
* in a virtual array during put_pixel_row calls, then actually emit the
* BMP file during finish_output.  The virtual array contains one JSAMPLE per
* pixel if the output is grayscale or colormapped, three if it is full color.
*/

/* Private version of data destination object */

typedef struct {
	struct translator_dest_struct pub;	/* public fields */
	JSAMPARRAY	whole_image;	/* needed to reverse row order */
	JDIMENSION	data_width;	/* JSAMPLEs per row */
	JDIMENSION	row_width;		/* physical width of one row in the BMP file */
	int			pad_bytes;		/* number of padding bytes needed per row */
	JDIMENSION	cur_output_row;	/* next row# to write to virtual array */
} bmp_dest_struct;

typedef bmp_dest_struct * bmp_dest_ptr;


/* Forward declarations */
LOCAL(void) write_colormap
JPP((bmp_dest_ptr dest,
	int map_colors, int map_entry_size));

/*
* Write some pixel data.
* In this module rows_supplied will always be 1.
*/

METHODDEF(void)
put_pixel_rows (translator_dest_ptr dinfo,
				JDIMENSION rows_supplied)
				/* This version is for writing 24-bit pixels */
{
	translator_common_ptr cinfo = dinfo->cinfo;
	bmp_dest_ptr dest = (bmp_dest_ptr) dinfo;
	JSAMPROW row_ptr;
	register JSAMPROW inptr, outptr;
	register JDIMENSION col;
	int pad;

	/* Access next row in virtual array */
	row_ptr = dest->whole_image[dest->cur_output_row];
	dest->cur_output_row++;

	/* Transfer data.  Note destination values must be in BGR order
	* (even though Microsoft's own documents say the opposite).
	*/
	inptr = dest->pub.buffer;
	outptr = row_ptr;
	for (col = cinfo->output_width; col > 0; col--) {
		outptr[2] = *inptr++;	/* can omit GETJSAMPLE() safely */
		outptr[1] = *inptr++;
		outptr[0] = *inptr++;
		outptr += 3;
	}

	/* Zero out the pad bytes. */
	pad = dest->pad_bytes;
	while (--pad >= 0)
		*outptr++ = 0;
}


/*
* Write some pixel data.
* In this module rows_supplied will always be 1.
*/

METHODDEF(void)
put_32bit_pixel_rows (translator_dest_ptr dinfo,
				JDIMENSION rows_supplied)
				/* This version is for writing 24-bit pixels */
{
	translator_common_ptr cinfo = dinfo->cinfo;
	bmp_dest_ptr dest = (bmp_dest_ptr) dinfo;
	JSAMPROW row_ptr;
	register JSAMPROW inptr, outptr;
	register JDIMENSION col;
	int pad;

	/* Access next row in virtual array */
	row_ptr = dest->whole_image[dest->cur_output_row];

	dest->cur_output_row++;

	/* Transfer data.  Note destination values must be in BGR order
	* (even though Microsoft's own documents say the opposite).
	*/
	inptr = dest->pub.buffer;
	outptr = row_ptr;

	for (col = cinfo->output_width; col > 0; col--) {
		outptr[2] = *inptr++;	/* can omit GETJSAMPLE() safely */
		outptr[1] = *inptr++;
		outptr[0] = *inptr++;
		outptr[3] = *inptr++;   /* alpha component */
		outptr += 4;
	}

	/* Zero out the pad bytes. */
	pad = dest->pad_bytes;
	while (--pad >= 0)
		*outptr++ = 0;
}


METHODDEF(void)
put_gray_rows (translator_dest_ptr dinfo,
			   JDIMENSION rows_supplied)
			   /* This version is for grayscale OR quantized color output */
{
	translator_common_ptr cinfo = dinfo->cinfo;
	bmp_dest_ptr dest = (bmp_dest_ptr) dinfo;
	JSAMPROW row_ptr;
	register JSAMPROW inptr, outptr;
	register JDIMENSION col;
	int pad;

	/* Access next row in virtual array */
	row_ptr = dest->whole_image[dest->cur_output_row];

	dest->cur_output_row++;

	/* Transfer data. */
	inptr = dest->pub.buffer;
	outptr = row_ptr;
	for (col = cinfo->output_width; col > 0; col--) {
		*outptr++ = *inptr++;	/* can omit GETJSAMPLE() safely */
	}

	/* Zero out the pad bytes. */
	pad = dest->pad_bytes;
	while (--pad >= 0)
		*outptr++ = 0;
}


/*
* Startup: normally writes the file header.
* In this module we may as well postpone everything until finish_output.
*/

METHODDEF(void)
start_output_bmp (translator_dest_ptr dinfo)
{
	/* no work here */
}

static int stream_putc(int ch, CBaseFile* pStream)
{
	size_t cbWritten = pStream->Write(&ch,1);
	return (int)cbWritten;
}

/*
* Finish up at the end of the file.
*
* Here is where we really output the BMP file.
*
* First, routines to write the Windows and OS/2 variants of the file header.
*/

LOCAL(void)
write_bmp_header (bmp_dest_ptr dest)
/* Write a Windows-style BMP file header, including colormap if needed */
{
	translator_common_ptr cinfo = dest->pub.cinfo;
	char bmpfileheader[14];
	char bmpinfoheader[40];
#define PUT_2B(array,offset,value)  \
	(array[offset] = (char) ((value) & 0xFF), \
	array[offset+1] = (char) (((value) >> 8) & 0xFF))
#define PUT_4B(array,offset,value)  \
	(array[offset] = (char) ((value) & 0xFF), \
	array[offset+1] = (char) (((value) >> 8) & 0xFF), \
	array[offset+2] = (char) (((value) >> 16) & 0xFF), \
	array[offset+3] = (char) (((value) >> 24) & 0xFF))
	INT32 headersize, bfSize;
	int bits_per_pixel, cmap_entries;

	/* Compute colormap size and total file size */
	{
		/* Unquantized, full color RGB */
		if( cinfo->input_components == 4 )
		{
			/* 32 bitmap support - yanghaibo */
			bits_per_pixel = 32;
		}
		else
		{
			bits_per_pixel = 24;
		}
		cmap_entries = 0;
	}

	/* File size */
	headersize = 14 + 40 + cmap_entries * 4; /* Header and colormap */
	bfSize = headersize + (INT32) dest->row_width * (INT32) cinfo->output_height;

	/* Set unused fields of header to 0 */
	MEMZERO(bmpfileheader, SIZEOF(bmpfileheader));
	MEMZERO(bmpinfoheader, SIZEOF(bmpinfoheader));

	/* Fill the file header */
	bmpfileheader[0] = 0x42;	/* first 2 bytes are ASCII 'B', 'M' */
	bmpfileheader[1] = 0x4D;
	PUT_4B(bmpfileheader, 2, bfSize); /* bfSize */
	/* we leave bfReserved1 & bfReserved2 = 0 */
	PUT_4B(bmpfileheader, 10, headersize); /* bfOffBits */

	/* Fill the info header (Microsoft calls this a BITMAPINFOHEADER) */
	PUT_2B(bmpinfoheader, 0, 40);	/* biSize */
	PUT_4B(bmpinfoheader, 4, cinfo->output_width); /* biWidth */
	PUT_4B(bmpinfoheader, 8, cinfo->output_height); /* biHeight */
	PUT_2B(bmpinfoheader, 12, 1);	/* biPlanes - must be 1 */
	PUT_2B(bmpinfoheader, 14, bits_per_pixel); /* biBitCount */
	/* we leave biCompression = 0, for none */
	/* we leave biSizeImage = 0; this is correct for uncompressed data */
	if (cinfo->density_unit == 2) { /* if have density in dots/cm, then */
		PUT_4B(bmpinfoheader, 24, (INT32) (cinfo->X_density*100)); /* XPels/M */
		PUT_4B(bmpinfoheader, 28, (INT32) (cinfo->Y_density*100)); /* XPels/M */
	}
	PUT_2B(bmpinfoheader, 32, cmap_entries); /* biClrUsed */
	/* we leave biClrImportant = 0 */

	if (JFWRITE(dest->pub.output_file, bmpfileheader, 14) != (size_t) 14)
		ERREXIT(cinfo, JERR_FILE_WRITE);

	if (JFWRITE(dest->pub.output_file, bmpinfoheader, 40) != (size_t) 40)
		ERREXIT(cinfo, JERR_FILE_WRITE);

	if (cmap_entries > 0)
		write_colormap(dest, cmap_entries, 4);
}

/*
* Write the colormap.
* Windows uses BGR0 map entries; OS/2 uses BGR entries.
*/

LOCAL(void)
write_colormap (bmp_dest_ptr dest,
				int map_colors, int map_entry_size)
{
	translator_common_ptr cinfo = dest->pub.cinfo;
	JSAMPARRAY colormap = cinfo->colormap;
	int num_colors = cinfo->actual_number_of_colors;
	CBaseFile * outfile = dest->pub.output_file;
	int i;

	if (colormap != NULL) {
		if (cinfo->out_color_components == 3) {
			/* Normal case with RGB colormap */
			for (i = 0; i < num_colors; i++) {
				stream_putc(GETJSAMPLE(colormap[2][i]), outfile);
				stream_putc(GETJSAMPLE(colormap[1][i]), outfile);
				stream_putc(GETJSAMPLE(colormap[0][i]), outfile);
				if (map_entry_size == 4)
					stream_putc(0, outfile);
			}
		} else {
			/* Grayscale colormap (only happens with grayscale quantization) */
			for (i = 0; i < num_colors; i++) {
				stream_putc(GETJSAMPLE(colormap[0][i]), outfile);
				stream_putc(GETJSAMPLE(colormap[0][i]), outfile);
				stream_putc(GETJSAMPLE(colormap[0][i]), outfile);
				if (map_entry_size == 4)
					stream_putc(0, outfile);
			}
		}
	} else {
		/* If no colormap, must be grayscale data.  Generate a linear "map". */
		for (i = 0; i < 256; i++) {
			stream_putc(i, outfile);
			stream_putc(i, outfile);
			stream_putc(i, outfile);
			if (map_entry_size == 4)
				stream_putc(0, outfile);
		}
	}
	/* Pad colormap with zeros to ensure specified number of colormap entries */ 
	if (i > map_colors)
		ERREXIT1(cinfo, JERR_TOO_MANY_COLORS, i);
	for (; i < map_colors; i++) {
		stream_putc(0, outfile);
		stream_putc(0, outfile);
		stream_putc(0, outfile);
		if (map_entry_size == 4)
			stream_putc(0, outfile);
	}
}

METHODDEF(void)
finish_output_bmp (translator_dest_ptr dinfo)
{
	translator_common_ptr cinfo = dinfo->cinfo;
	bmp_dest_ptr dest = (bmp_dest_ptr) dinfo;
	register CBaseFile * outfile = dest->pub.output_file;
	JSAMPROW row_ptr;
	//register JSAMPROW data_ptr;
	JDIMENSION row;
	//register JDIMENSION col;
	translator_progress_ptr progress = (translator_progress_ptr) cinfo->progress;

	/* Write the header and colormap */
	write_bmp_header(dest);

	/* Write the file body from our virtual array */
	for (row = cinfo->output_height; row > 0; row--) {
		if (progress != NULL) {
			progress->pass_counter = (long) (cinfo->output_height - row);
			progress->pass_limit = (long) cinfo->output_height;
			(*progress->progress_monitor) ( cinfo);
		}

		row_ptr = dest->whole_image[row-1];
		
		// optimized by yanghaibo
		// Old:
		//data_ptr = image_ptr[0];
		//for (col = dest->row_width; col > 0; col--) {
		//	stream_putc(GETJSAMPLE(*data_ptr), outfile);
		//	data_ptr++;
		//}

		// New:
		JFWRITE( outfile, row_ptr, dest->row_width);
	}
	if (progress != NULL)
		progress->completed_extra_passes++;

	// commented by yanghaibo

	/* Make sure we wrote the output file OK */
	// fflush(outfile);

	//
	//if (ferror(outfile))
	//	ERREXIT(cinfo, JERR_FILE_WRITE);

	// free buffers
	if ( dest->whole_image != NULL )
	{
		free_sarray( dest->whole_image );
		dest->whole_image = NULL;
	}

	dinfo->buffer = NULL;
}

/*
* The module selection routine for BMP format output.
*/

void bmp_calc_output_dimensions( translator_common_ptr cinfo )
{
	// TODO:
	cinfo->output_height = cinfo->image_height;
	cinfo->output_width = cinfo->image_width;
}

GLOBAL(translator_dest_ptr)
my_init_write_bmp (translator_common_ptr cinfo)
{
	bmp_dest_ptr dest;
	JDIMENSION row_width;

	/* Create module interface object, fill in method pointers */
	dest = (bmp_dest_ptr) malloc( sizeof(bmp_dest_struct) );

	dest->pub.cinfo = cinfo;
	dest->pub.start_output = start_output_bmp;
	dest->pub.finish_output = finish_output_bmp;
	cinfo->quantize_colors = FALSE;

	if (cinfo->out_color_space == JCS_GRAYSCALE) {
		dest->pub.put_pixel_rows = put_gray_rows;
	} else if (cinfo->out_color_space == JCS_RGB) {
		if (cinfo->quantize_colors)
			dest->pub.put_pixel_rows = put_gray_rows;
		else
		{
			if ( cinfo->input_components == 4 )
			{
				/* 32 bitmap support - yanghaibo */
				dest->pub.put_pixel_rows = put_32bit_pixel_rows;
			}
			else
			{
				dest->pub.put_pixel_rows = put_pixel_rows;
			}
		}
	} else {
		ERREXIT(cinfo, JERR_BMP_COLORSPACE);
	}

	/* Calculate output image dimensions so we can allocate space */
	// jpeg_calc_output_dimensions(cinfo);
	bmp_calc_output_dimensions(cinfo);

	/* Determine width of rows in the BMP file (padded to 4-byte boundary). */
	row_width = cinfo->output_width * cinfo->input_components;
	
	dest->data_width = row_width;
	while ((row_width & 3) != 0) row_width++;
	dest->row_width = row_width;
	dest->pad_bytes = (int) (row_width - dest->data_width);

	/* Allocate space for inversion array, prepare for write pass */
	dest->whole_image = alloc_sarray( row_width, cinfo->output_height);

	dest->cur_output_row = 0;

	if (cinfo->progress != NULL)
	{
		translator_progress_ptr progress = (translator_progress_ptr) cinfo->progress;
		progress->total_extra_passes++; /* count file input as separate pass */
	}

	/* output buffer. */
	dest->pub.buffer = NULL;// it's a temporary pointer

	// Need to be initialized after return.
	dest->pub.output_file = NULL;

	return (translator_dest_ptr) dest;
}
