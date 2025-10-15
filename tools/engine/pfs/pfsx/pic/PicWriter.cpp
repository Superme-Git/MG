//////////////////////////////////////////////////////////////////////////
//
// PicWriter.cpp
//
#include "../common/Translator.h"

enum PixelFormat
{
	PIX_FMT_R5G6B5,
	PIX_FMT_A4R4G4B4,	
	PIX_FMT_R8G8B8,
	PIX_FMT_A8R8G8B8,
	PIX_FMT_X8R8G8B8,	
	PIX_FMT_A8,			// 8bit gray　目前不支持

	PIX_FMT_A1R5G5B5,
	PIX_FMT_X1R5G5B5,	

	//以下目前不支持
	PIX_FMT_DXT1,
	PIX_FMT_DXT2,
	PIX_FMT_DXT3,
	PIX_FMT_DXT4,
	PIX_FMT_DXT5,
};

//
//每个像素的格式顺序是：
//R5G6B5: R、G、B的顺序是从高到低
//A4R4G4B4：R、G、B的顺序是从高到低
//R8G8B8：R、G、B的顺序是从高到低
//A8R8G8B8: 从高到低的顺序是A,R,G,B
//X8R8G8B8: 从高到低的顺序是X,R,G,B 
//A1R5G5B5: A、R、G、B的顺序是从高到低
//X1R5G5B5: X、R、G、B的顺序是从高到低
//


/* Private version of data destination object */

typedef struct {
	struct translator_dest_struct pub;	/* public fields */
	int format;		/* see above PixelFormat */
	int bytesPerPixel;  
	char *iobuffer;		/* physical I/O buffer */
	JDIMENSION buffer_width;	/* width of one row */
} pic_dest_struct;

typedef pic_dest_struct * pic_dest_ptr;


#pragma pack(push)
#pragma pack(4)
struct PicHeader
{
	int format;
	short width;
	short height;
	int blockNum;
};

struct BlockDesc
{
	short	imageWidth;
	short	imageHeight;
	short	offsetX;
	short	offsetY;
	unsigned fileSize;
};

#pragma pack(pop)

static void 
write_header (translator_dest_ptr dinfo)
/* Create and write a pic header */
{
	translator_common_ptr cinfo = dinfo->cinfo; 
	pic_dest_ptr dest = (pic_dest_ptr) dinfo;

	PicHeader header = {dest->format,cinfo->image_width,cinfo->image_height, 1 };
	
	if (JFWRITE(dinfo->output_file, &header, sizeof(header)) != sizeof(header) )
		ERREXIT(cinfo, JERR_FILE_WRITE);

	unsigned fileSize = cinfo->image_width * cinfo->image_height * dest->bytesPerPixel;

	BlockDesc blockDesc = { cinfo->image_width,cinfo->image_height, 0, 0,  fileSize };

	if (JFWRITE(dinfo->output_file, &blockDesc, sizeof(blockDesc)) != sizeof(blockDesc) )
		ERREXIT(cinfo, JERR_FILE_WRITE);
}


/*
* Write some pixel data.
* In this module rows_supplied will always be 1.
*/

// input is 4 components
static void 
put_24pixel_rows (translator_dest_ptr dinfo,
				JDIMENSION rows_supplied)
				/* used for unquantized full-color output */
{
	translator_common_ptr cinfo = dinfo->cinfo; 
	pic_dest_ptr dest = (pic_dest_ptr) dinfo;
	register JSAMPROW inptr;
	register char * outptr;
	register JDIMENSION col;

	inptr = dest->pub.buffer;
	outptr = dest->iobuffer;
	for (col = cinfo->image_width; col > 0; col--) {
		outptr[0] = (char) GETJSAMPLE(inptr[2]); /* RGBA to BGR order */
		outptr[1] = (char) GETJSAMPLE(inptr[1]);
		outptr[2] = (char) GETJSAMPLE(inptr[0]);
		inptr += 4, outptr += 3;
	}
	
	JFWRITE(dest->pub.output_file, dest->iobuffer, dest->buffer_width);
}

// input is 3 components
static void 
put_24pixel_rows_3 (translator_dest_ptr dinfo,
				  JDIMENSION rows_supplied)
				  /* used for unquantized full-color output */
{
	translator_common_ptr cinfo = dinfo->cinfo; 
	pic_dest_ptr dest = (pic_dest_ptr) dinfo;
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

	JFWRITE(dest->pub.output_file, dest->iobuffer, dest->buffer_width);
}

// input is 4 components
static void 
put_4444pixel_rows (translator_dest_ptr dinfo,
				  JDIMENSION rows_supplied)
{
	translator_common_ptr cinfo = dinfo->cinfo; 
	pic_dest_ptr dest = (pic_dest_ptr) dinfo;
	register JSAMPROW inptr;
	register char * outptr;
	register JDIMENSION col;

	inptr = dest->pub.buffer;
	outptr = dest->iobuffer;
	for (col = cinfo->image_width; col > 0; col--) {
		outptr[0] = (char) (GETJSAMPLE(inptr[1])/17 << 4 | GETJSAMPLE(inptr[2])/17 ); /*RGBA to A4R4|G4B4 order */
		outptr[1] = (char) (GETJSAMPLE(inptr[3])/17 << 4 | GETJSAMPLE(inptr[0])/17 );
		inptr += 4;
		outptr+= 2;
	}

	JFWRITE(dest->pub.output_file, dest->iobuffer, dest->buffer_width);
}

// input is 3 components
static void 
put_4444pixel_rows_3 (translator_dest_ptr dinfo,
					JDIMENSION rows_supplied)
{
	translator_common_ptr cinfo = dinfo->cinfo; 
	pic_dest_ptr dest = (pic_dest_ptr) dinfo;
	register JSAMPROW inptr;
	register char * outptr;
	register JDIMENSION col;

	inptr = dest->pub.buffer;
	outptr = dest->iobuffer;
	for (col = cinfo->image_width; col > 0; col--) {
		outptr[0] = (char) (GETJSAMPLE(inptr[1])/17 << 4 | GETJSAMPLE(inptr[2])/17 ); /*RGB to A4R4|G4B4 order */
		outptr[1] = (char) (0xF0 | GETJSAMPLE(inptr[0])/17 );
		inptr += 3;
		outptr+= 2;
	}

	JFWRITE(dest->pub.output_file, dest->iobuffer, dest->buffer_width);
}

// input is 4 components
static void 
put_565pixel_rows (translator_dest_ptr dinfo,
					JDIMENSION rows_supplied)
{
	translator_common_ptr cinfo = dinfo->cinfo; 
	pic_dest_ptr dest = (pic_dest_ptr) dinfo;
	register JSAMPROW inptr;
	register char * outptr;
	register JDIMENSION col;

	inptr = dest->pub.buffer;
	outptr = dest->iobuffer;
	unsigned short * ptr = (unsigned short *)outptr;
	for (col = cinfo->image_width; col > 0; col--) {
		/* RGBA to B5G6R5 order */
		// 255.0/31.0 = 8.2258064516129032258064516129032
		// 255.0/63.0 = 4.047619047619047619047619047619
		unsigned short r5 = (unsigned short)(GETJSAMPLE(inptr[0])/8.2258); // R
		unsigned short g6 = (unsigned short)(GETJSAMPLE(inptr[1])/4.047619); // G
		unsigned short b5 = (unsigned short)(GETJSAMPLE(inptr[2])/8.2258); // B
		ptr[0] = (unsigned short) (((r5 & 0x1F) << 11) | ((g6 & 0x3F) << 5) | (b5 & 0x1F));
		inptr += 4, ptr ++;;
	}

	JFWRITE(dest->pub.output_file, dest->iobuffer, dest->buffer_width);
}

// input is 3 components
static void 
put_565pixel_rows_3 (translator_dest_ptr dinfo,
				   JDIMENSION rows_supplied)
{
	translator_common_ptr cinfo = dinfo->cinfo; 
	pic_dest_ptr dest = (pic_dest_ptr) dinfo;
	register JSAMPROW inptr;
	register char * outptr;
	register JDIMENSION col;

	inptr = dest->pub.buffer;
	outptr = dest->iobuffer;
	unsigned short * ptr = (unsigned short *)outptr;
	for (col = cinfo->image_width; col > 0; col--) {
		/* RGB to B5G6R5 order */
		// 255.0/31.0 = 8.2258064516129032258064516129032
		// 255.0/63.0 = 4.047619047619047619047619047619
		unsigned short r5 = (unsigned short)(GETJSAMPLE(inptr[0])/8.2258);
		unsigned short g6 = (unsigned short)(GETJSAMPLE(inptr[1])/4.047619);
		unsigned short b5 = (unsigned short)(GETJSAMPLE(inptr[2])/8.2258);

		ptr[0] = (unsigned short) (((r5 & 0x1F) << 11) | ((g6 & 0x3F) << 5) | (b5 & 0x1F));
		inptr += 3, ptr ++;;
	}

	JFWRITE(dest->pub.output_file, dest->iobuffer, dest->buffer_width);
}

static void 
put_1555pixel_rows (translator_dest_ptr dinfo,
				   JDIMENSION rows_supplied)
{
	translator_common_ptr cinfo = dinfo->cinfo; 
	pic_dest_ptr dest = (pic_dest_ptr) dinfo;
	register JSAMPROW inptr;
	register char * outptr;
	register JDIMENSION col;

	inptr = dest->pub.buffer;
	outptr = dest->iobuffer;
	unsigned short * ptr = (unsigned short *)outptr;
	for (col = cinfo->image_width; col > 0; col--) {
		/* ARGB to A1B5G5R5 order */
		// 255.0/31.0 = 8.2258064516129032258064516129032
		unsigned short r5 = (unsigned short)(GETJSAMPLE(inptr[0])/8.2258); // R
		unsigned short g5 = (unsigned short)(GETJSAMPLE(inptr[1])/8.2258); // G
		unsigned short b5 = (unsigned short)(GETJSAMPLE(inptr[2])/8.2258); // B
		unsigned short a1 = (unsigned short)(!!GETJSAMPLE(inptr[3]));

		ptr[0] = (unsigned short) ( ((a1&0x1) << 15) | ((r5 & 0x1F) << 10) | ((g5 & 0x1F) << 5) | (b5 & 0x1F));
		inptr += 4, ptr ++;;
	}

	JFWRITE(dest->pub.output_file, dest->iobuffer, dest->buffer_width);
}

// input is 3 components
static void 
put_1555pixel_rows_3 (translator_dest_ptr dinfo,
					JDIMENSION rows_supplied)
{
	translator_common_ptr cinfo = dinfo->cinfo; 
	pic_dest_ptr dest = (pic_dest_ptr) dinfo;
	register JSAMPROW inptr;
	register char * outptr;
	register JDIMENSION col;

	inptr = dest->pub.buffer;
	outptr = dest->iobuffer;
	unsigned short * ptr = (unsigned short *)outptr;
	for (col = cinfo->image_width; col > 0; col--) {
		/* ARGB to A1B5G5R5 order */
		// 255.0/31.0 = 8.2258064516129032258064516129032
		unsigned short r5 = (unsigned short)(GETJSAMPLE(inptr[0])/8.2258); // R
		unsigned short g5 = (unsigned short)(GETJSAMPLE(inptr[1])/8.2258); // G
		unsigned short b5 = (unsigned short)(GETJSAMPLE(inptr[2])/8.2258); // B
		//unsigned short a1 = 1; // A
		ptr[0] = (unsigned short) ( (0x8000) | ((r5 & 0x1F) << 10) | ((g5 & 0x1F) << 5) | (b5 & 0x1F));
		inptr += 3, ptr ++;;
	}

	JFWRITE(dest->pub.output_file, dest->iobuffer, dest->buffer_width);
}



/* for 32bit rows */
static void 
put_32pixel_rows (translator_dest_ptr dinfo,
				  JDIMENSION rows_supplied)
{
	translator_common_ptr cinfo = dinfo->cinfo; 
	pic_dest_ptr dest = (pic_dest_ptr) dinfo;
	register JSAMPROW inptr;
	register char * outptr;
	register JDIMENSION col;

	inptr = dest->pub.buffer;
	outptr = dest->iobuffer;
	for (col = cinfo->image_width; col > 0; col--) {
		 /* RGBA to BGRA order */
		outptr[0] = (char) GETJSAMPLE(inptr[2]); // B
		outptr[1] = (char) GETJSAMPLE(inptr[1]); // G
		outptr[2] = (char) GETJSAMPLE(inptr[0]); // R
		outptr[3] = (char) GETJSAMPLE(inptr[3]); // A
		inptr += 4, outptr += 4;
	}

	JFWRITE(dest->pub.output_file, dest->iobuffer, dest->buffer_width);
}

// input is 3 components
static void 
put_32pixel_rows_3(translator_dest_ptr dinfo,
				   JDIMENSION rows_supplied)
{

	translator_common_ptr cinfo = dinfo->cinfo; 
	pic_dest_ptr dest = (pic_dest_ptr) dinfo;
	register JSAMPROW inptr;
	register char * outptr;
	register JDIMENSION col;

	inptr = dest->pub.buffer;
	outptr = dest->iobuffer;
	for (col = cinfo->image_width; col > 0; col--) {
		/* RGB to BGRA order */
		outptr[0] = (JSAMPLE) GETJSAMPLE(inptr[2]); // B
		outptr[1] = (JSAMPLE) GETJSAMPLE(inptr[1]); // G
		outptr[2] = (JSAMPLE) GETJSAMPLE(inptr[0]); // R
		outptr[3] = (char)0xFF;						// A
		inptr += 3;
		outptr += 4;
	}

	JFWRITE(dest->pub.output_file, dest->iobuffer, dest->buffer_width);
}


/*
* Startup: write the file header.
*/

static void 
start_output_pic (translator_dest_ptr dinfo)
{
	write_header(dinfo);
}

/*
* Finish up at the end of the file.
*/

static void 
finish_output_pic (translator_dest_ptr dinfo)
{
	dinfo->buffer = NULL;

	pic_dest_ptr pic_dest = (pic_dest_ptr)dinfo;
	if( pic_dest->iobuffer != NULL )
	{
		free( pic_dest->iobuffer);
		pic_dest->iobuffer = NULL;
	}
}

/*
* The module selection routine for pic format output.
*/

translator_dest_ptr
my_init_write_pic (translator_common_ptr cinfo)
{
	pic_dest_ptr dest;

	/* Create module interface object, fill in method pointers */
	dest = (pic_dest_ptr) malloc(sizeof(pic_dest_struct));
	dest->pub.cinfo = cinfo;
	dest->pub.start_output = start_output_pic;
	dest->pub.finish_output = finish_output_pic;
	int& format = dest->format;
	format = PIX_FMT_R5G6B5;
	int& bytesPerPixel = dest->bytesPerPixel;
	/* Create I/O buffer.  Note we make this near on a PC. */
	bool bNeedAlloc = true;
	if ( cinfo->input_components == 3 )
	{
		if( cinfo->out_color_components == 3)
		{
			if( cinfo->data_precision == 5 )
			{
				format = PIX_FMT_R5G6B5;
				dest->pub.put_pixel_rows = put_565pixel_rows_3;
				bytesPerPixel = 2;
			}
			else // cinfo->data_precision == 8;
			{
				format = PIX_FMT_R8G8B8;
				dest->pub.put_pixel_rows = put_24pixel_rows_3;
				bytesPerPixel = 3;
			}
		}
		else // out_color_components == 4;
		{
			if( cinfo->data_precision == 4 )
			{
				format = PIX_FMT_A4R4G4B4;
				dest->pub.put_pixel_rows = put_4444pixel_rows_3;
				bytesPerPixel = 2;
			}
			else if( cinfo->data_precision == 5 )
			{
				format = PIX_FMT_X1R5G5B5;
				dest->pub.put_pixel_rows = put_1555pixel_rows_3;
				bytesPerPixel = 2;
			}
			else // data_precision == 8 
			{
				format = PIX_FMT_X8R8G8B8;
				dest->pub.put_pixel_rows = put_32pixel_rows_3;
				bytesPerPixel = 4;
			}
		}	
	}
	else if ( cinfo->input_components == 4 )
	{
		if( cinfo->out_color_components == 3)
		{
			if( cinfo->data_precision == 5 )
			{
				format = PIX_FMT_R5G6B5;
				dest->pub.put_pixel_rows = put_565pixel_rows;
				bytesPerPixel = 2;
			}
			else // cinfo->data_precision == 8;
			{
				format = PIX_FMT_R8G8B8;
				dest->pub.put_pixel_rows = put_24pixel_rows;
				bytesPerPixel = 3;
			}
		}
		else // out_color_components == 4;
		{
			if( cinfo->data_precision == 4 )
			{
				format = PIX_FMT_A4R4G4B4;
				dest->pub.put_pixel_rows = put_4444pixel_rows;
				bytesPerPixel = 2;
			}
			else if( cinfo->data_precision == 5 )
			{
				format = PIX_FMT_A1R5G5B5;
				dest->pub.put_pixel_rows = put_1555pixel_rows;
				bytesPerPixel = 2;
			}
			else // data_precision == 8 
			{
				format = PIX_FMT_A8R8G8B8;
				dest->pub.put_pixel_rows = put_32pixel_rows;
				bytesPerPixel = 4;
			}
		}
	}

	dest->buffer_width = cinfo->image_width * bytesPerPixel;

	if( bNeedAlloc )
	{
		dest->iobuffer = (char *) malloc( dest->buffer_width );
	}
	else
	{
		// No need
		dest->iobuffer = NULL;
	}

	dest->pub.buffer = NULL;

	return (translator_dest_ptr) dest;
}
