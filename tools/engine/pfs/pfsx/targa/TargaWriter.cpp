//////////////////////////////////////////////////////////////////////////
//
// TargaWriter.cpp
//

#include "../common/Translator.h"

/* Private version of data destination object */

typedef struct {
  struct translator_dest_struct pub;	/* public fields */

  char *iobuffer;		/* physical I/O buffer */
  JDIMENSION buffer_width;	/* width of one row */
} tga_dest_struct;

typedef tga_dest_struct * tga_dest_ptr;


static void 
write_header (translator_dest_ptr dinfo, int num_colors)
/* Create and write a Targa header */
{
	translator_common_ptr cinfo = dinfo->cinfo; 
  char targaheader[18];

  /* Set unused fields of header to 0 */
  MEMZERO(targaheader, SIZEOF(targaheader));

  if (num_colors > 0) {
    targaheader[1] = 1;		/* color map type 1 */
    targaheader[5] = (char) (num_colors & 0xFF);
    targaheader[6] = (char) (num_colors >> 8);
    targaheader[7] = 24;	/* 24 bits per cmap entry */
  }

  targaheader[12] = (char) (cinfo->image_width & 0xFF);
  targaheader[13] = (char) (cinfo->image_width >> 8);
  targaheader[14] = (char) (cinfo->image_height & 0xFF);
  targaheader[15] = (char) (cinfo->image_height >> 8);
  targaheader[17] = 0x20;	/* Top-down, non-interlaced */

  //if (cinfo->in_color_space == JCS_GRAYSCALE) {
  //  targaheader[2] = 3;		/* image type = uncompressed gray-scale */
  //  targaheader[16] = 8;	/* bits per pixel */
  //} else
  {			/* must be RGB */
    if (num_colors > 0) {
      targaheader[2] = 1;	/* image type = colormapped RGB */
      targaheader[16] = 8;
    } else {
      targaheader[2] = 2;	/* image type = uncompressed RGB */
	  if ( cinfo->input_components == 4 )
	  {
		  targaheader[16] = 32;
	  }
	  else 
	  {
		  targaheader[16] = 24;
	  }
    }
  }

  if (JFWRITE(dinfo->output_file, targaheader, 18) != (size_t) 18)
    ERREXIT(cinfo, JERR_FILE_WRITE);
}


/*
 * Write some pixel data.
 * In this module rows_supplied will always be 1.
 */

static void 
put_pixel_rows (translator_dest_ptr dinfo,
		JDIMENSION rows_supplied)
/* used for unquantized full-color output */
{
	translator_common_ptr cinfo = dinfo->cinfo; 
  tga_dest_ptr dest = (tga_dest_ptr) dinfo;
  register JSAMPROW inptr;
  register char * outptr;
  register JDIMENSION col;

  inptr = dest->pub.buffer;
  outptr = dest->iobuffer;
  for (col = cinfo->image_width; col > 0; col--) {
    outptr[0] = (char) GETJSAMPLE(inptr[2]); /* RGB to BGR order */
    outptr[1] = (char) GETJSAMPLE(inptr[1]);
    outptr[2] = (char) GETJSAMPLE(inptr[0]);
    inptr += 3, outptr += 3;
  }
  (void) JFWRITE(dest->pub.output_file, dest->iobuffer, dest->buffer_width);
}

/* for 32bit rows */
static void 
put_32pixel_rows (translator_dest_ptr dinfo,
				JDIMENSION rows_supplied)
{
	translator_common_ptr cinfo = dinfo->cinfo; 
	tga_dest_ptr dest = (tga_dest_ptr) dinfo;
	register JSAMPROW inptr;
	register char * outptr;
	register JDIMENSION col;

	inptr = dest->pub.buffer;
	outptr = dest->iobuffer;
	for (col = cinfo->image_width; col > 0; col--) {
		outptr[0] = (char) GETJSAMPLE(inptr[2]); /* RGB to BGR order */
		outptr[1] = (char) GETJSAMPLE(inptr[1]);
		outptr[2] = (char) GETJSAMPLE(inptr[0]);
		outptr[3] = (char) GETJSAMPLE(inptr[3]); /* alpha channel */
		inptr += 4, outptr += 4;
	}

	(void) JFWRITE(dest->pub.output_file, dest->iobuffer, dest->buffer_width);
}


static void 
put_gray_rows (translator_dest_ptr dinfo,
	       JDIMENSION rows_supplied)
/* used for grayscale OR quantized color output */
{
	translator_common_ptr cinfo = dinfo->cinfo; 
  tga_dest_ptr dest = (tga_dest_ptr) dinfo;
  register JSAMPROW inptr;
  register char * outptr;
  register JDIMENSION col;

  inptr = dest->pub.buffer;
  outptr = dest->iobuffer;
  for (col = cinfo->image_width; col > 0; col--) {
    *outptr++ = (char) GETJSAMPLE(*inptr++);
  }
  (void) JFWRITE(dest->pub.output_file, dest->iobuffer, dest->buffer_width);
}


/*
 * Write some demapped pixel data when color quantization is in effect.
 * For Targa, this is only applied to grayscale data.
 */

static void 
put_demapped_gray (translator_dest_ptr dinfo,
		   JDIMENSION rows_supplied)
{
	translator_common_ptr cinfo = dinfo->cinfo; 
	tga_dest_ptr dest = (tga_dest_ptr) dinfo;
	register JSAMPROW inptr;
	register char * outptr;
	register JSAMPROW color_map0 = cinfo->colormap[0];
	register JDIMENSION col;

	inptr = dest->pub.buffer;
	outptr = dest->iobuffer;
	for (col = cinfo->image_width; col > 0; col--) {
		*outptr++ = (char) GETJSAMPLE(color_map0[GETJSAMPLE(*inptr++)]);
	}
	(void) JFWRITE(dest->pub.output_file, dest->iobuffer, dest->buffer_width);
}


/*
 * Startup: write the file header.
 */

static void 
start_output_tga (translator_dest_ptr dinfo)
{

  tga_dest_ptr dest = (tga_dest_ptr) dinfo;
  //int num_colors, i;
//  CBaseFile *outfile;

//  if (cinfo->in_color_space == JCS_GRAYSCALE) {
//    ///* Targa doesn't have a mapped grayscale format, so we will */
//    ///* demap quantized gray output.  Never emit a colormap. */
//    //write_header(cinfo, dinfo, 0);
//    //if (cinfo->quantize_colors)
//    //  dest->pub.put_pixel_rows = put_demapped_gray;
//    //else
//    //  dest->pub.put_pixel_rows = put_gray_rows;
//  }
//  else
//	if (cinfo->in_color_space == JCS_RGB)
//	
//	{
//
// //   if (cinfo->quantize_colors) {
// //     /* We only support 8-bit colormap indexes, so only 256 colors */
// //     num_colors = cinfo->actual_number_of_colors;
// //     if (num_colors > 256)
//	//ERREXIT1(cinfo, JERR_TOO_MANY_COLORS, num_colors);
// //     write_header(cinfo, dinfo, num_colors);
// //     /* Write the colormap.  Note Targa uses BGR byte order */
// //     outfile = dest->pub.output_file;
// //     for (i = 0; i < num_colors; i++) {
//	//outfile->Write(&cinfo->colormap[2][i], 1);
//	//outfile->Write(&cinfo->colormap[1][i], 1);
//	//outfile->Write(&cinfo->colormap[0][i], 1);
// //     }
// //     dest->pub.put_pixel_rows = put_gray_rows;
////	}	else
//	{
//      write_header(cinfo, dinfo, 0);
//      dest->pub.put_pixel_rows = put_pixel_rows;
//    }
//  } else {
//    ERREXIT(cinfo, JERR_TGA_COLORSPACE);
//  }

	write_header(dinfo, 0);
	
	translator_common_ptr cinfo = dinfo->cinfo;
	if (cinfo->input_components == 4 )
	{
		dest->pub.put_pixel_rows = put_32pixel_rows;
	}
	else
	{
		dest->pub.put_pixel_rows = put_pixel_rows;
	}

}

/*
 * Finish up at the end of the file.
 */

static void 
finish_output_tga (translator_dest_ptr dinfo)
{
  /* Make sure we wrote the output file OK */
  //fflush(dinfo->output_file);
  //if (ferror(dinfo->output_file))
  // ERREXIT(cinfo, JERR_FILE_WRITE);

	dinfo->buffer = NULL;

	tga_dest_ptr tga_dest = (tga_dest_ptr)dinfo;
	if( tga_dest->iobuffer != NULL )
	{
		 free( tga_dest->iobuffer);
		 tga_dest->iobuffer = NULL;
	}
}

/*
 * The module selection routine for Targa format output.
 */

translator_dest_ptr
my_init_write_targa (translator_common_ptr cinfo)
{
  tga_dest_ptr dest;

  /* Create module interface object, fill in method pointers */
  dest = (tga_dest_ptr) malloc(sizeof(tga_dest_struct));
  dest->pub.cinfo = cinfo;
  dest->pub.start_output = start_output_tga;
  dest->pub.finish_output = finish_output_tga;

  /* Calculate output image dimensions so we can allocate space */
  //jpeg_calc_output_dimensions(cinfo);

  /* Create I/O buffer.  Note we make this near on a PC. */
  dest->buffer_width = cinfo->image_width * cinfo->input_components;
  dest->iobuffer = (char *) malloc( dest->buffer_width );
  dest->pub.buffer = NULL;

  return (translator_dest_ptr) dest;
}
