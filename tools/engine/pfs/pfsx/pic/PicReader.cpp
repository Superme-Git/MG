//////////////////////////////////////////////////////////////////////////
//
// PicReader.cpp
//
//
//

#include "..\common\Translator.h"

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

/* Private version of data source object */

typedef struct _pic_source_struct * pic_source_ptr;

typedef struct _pic_source_struct
{
	struct translator_source_struct pub; /* public fields */
	
	JDIMENSION current_row;	/* Current logical row number to read */
	int pos_in_row;

	/* Result of read_pixel is delivered here: */
	U_CHAR pic_pixel[4];
	int pixel_size;		/* Bytes per Picture pixel (1 to 4) */
	PicHeader picHeader;
	JSAMPARRAY whole_image;
	void (*read_pixel)(pic_source_ptr sinfo);

} pic_source_struct;


/* For expanding 5-bit pixel values to 8-bit with best rounding */

static const UINT8 c5to8bits[32] = {
	0,   8,  16,  25,  33,  41,  49,  58,
	66,  74,  82,  90,  99, 107, 115, 123,
	132, 140, 148, 156, 165, 173, 181, 189,
	197, 206, 214, 222, 230, 239, 247, 255
};

/*
* read_pixel methods: get a single pixel from Picture file into pic_pixel[]
*/

static void
read_pixel (pic_source_ptr sinfo)
/* Read one Picture pixel from the input file */
{
	if( sinfo->pub.input_file->Read(sinfo->pic_pixel, sinfo->pixel_size) == 0 )
	{
		ERREXIT(sinfo->pub.cinfo, JERR_INPUT_EOF);
	}
}

static void
read_pixel_in_memory (pic_source_ptr sinfo)
/* Read one Picture pixel from the whole image buffer */
{
	pic_source_ptr source = (pic_source_ptr) sinfo;
	memcpy(sinfo->pic_pixel,  source->whole_image[source->current_row] + source->pos_in_row, sinfo->pixel_size);
	source->pos_in_row += sinfo->pixel_size;
}

//
// A, R、G、B的顺序是从高到低
//
// 结果要是 从高到低 A, B, G, R 
// 或者说 从低到高 R, G, B, A
//
static JDIMENSION
get_4444bit_row (translator_source_ptr sinfo)
/* This version is for reading 16-bit pixels */
{
	translator_common_ptr cinfo = sinfo->cinfo; 
	pic_source_ptr source = (pic_source_ptr) sinfo;
	register int t;
	register JSAMPROW ptr;
	register JDIMENSION col;

	if( source->current_row >= cinfo->image_height )
	{
		return 0;
	}

	ptr = source->pub.buffer;
	for (col = cinfo->image_width; col > 0; col--) {
		source->read_pixel(source); /* Load next pixel into pic_pixel */
		t = UCH(source->pic_pixel[0]);
		t += UCH(source->pic_pixel[1]) << 8;
		/* We expand 5 bit data to 8 bit sample width.
		* The format of the 16-bit (LSB first) input word is
		*     AAAARRRRGGGGBBBB
		*/
		ptr[2] = (JSAMPLE) (t & 0xF) * 17; // B
		t >>= 4;
		ptr[1] = (JSAMPLE) (t & 0xF) * 17; // G
		t >>= 4;
		ptr[0] = (JSAMPLE) (t & 0xF) * 17; // R
		t >>= 4;
		ptr[3] = (JSAMPLE) (t & 0xF) * 17; // A

		ptr += 4;
	}

	source->current_row++;
	source->pos_in_row = 0;

	return 1;
}


//
// 从高到低的顺序是 R G B,
// 结果要是 从高到低 B, G, R 
// 或者说 从低到高 R, G, B 
//
static JDIMENSION
get_565bit_row (translator_source_ptr sinfo)
/* This version is for reading 16-bit pixels */
{
	translator_common_ptr cinfo = sinfo->cinfo; 
	pic_source_ptr source = (pic_source_ptr) sinfo;
	register int t;
	register JSAMPROW ptr;
	register JDIMENSION col;

	if( source->current_row >= cinfo->image_height )
	{
		return 0;
	}

	ptr = source->pub.buffer;
	for (col = cinfo->image_width; col > 0; col--) {
		source->read_pixel(source); /* Load next pixel into pic_pixel */
		t = UCH(source->pic_pixel[0]);
		t += UCH(source->pic_pixel[1]) << 8;
		/* We expand 5 bit data to 8 bit sample width.
		* The format of the 16-bit (LSB first) input word is
		*     RRRRRGGGGGGBBBBB
		*/
		ptr[2] = (JSAMPLE) c5to8bits[t & 0x1F]; // B
		t >>= 5;
		// 255.0/63.0 = 4.047619047619047619047619047619
		ptr[1] = (JSAMPLE) ((t & 0x3F)* 4.047619);// G
		t >>= 6;
		ptr[0] = (JSAMPLE) c5to8bits[t & 0x1F]; // R
		ptr += 3;
	}

	source->current_row++;
	source->pos_in_row = 0;

	return 1;
}

//
// 从高到低的顺序是 A R G B,
// 结果要是 从高到低 A, B, G, R 
// 或者说 从低到高  R, G, B, A
//
static JDIMENSION
get_1555bit_row (translator_source_ptr sinfo)
/* This version is for reading 16-bit pixels */
{
	translator_common_ptr cinfo = sinfo->cinfo; 
	pic_source_ptr source = (pic_source_ptr) sinfo;
	register int t;
	register JSAMPROW ptr;
	register JDIMENSION col;

	if( source->current_row >= cinfo->image_height )
	{
		return 0;
	}

	ptr = source->pub.buffer;
	for (col = cinfo->image_width; col > 0; col--) {
		source->read_pixel(source); /* Load next pixel into pic_pixel */
		t = UCH(source->pic_pixel[0]);
		t += UCH(source->pic_pixel[1]) << 8;
		/* We expand 5 bit data to 8 bit sample width.
		* The format of the 16-bit (LSB first) input word is
		*     ARRRRRGGGGGBBBBB
		*/
		ptr[2] = (JSAMPLE) c5to8bits[t & 0x1F]; // B
		t >>= 5;
		ptr[1] = (JSAMPLE) c5to8bits[t & 0x1F]; // G
		t >>= 5;
		ptr[0] = (JSAMPLE) c5to8bits[t & 0x1F]; // R
		t >>= 5;
		ptr[3] = (JSAMPLE) (t?255:0);			// A
		ptr += 4;
	}

	source->current_row++;
	source->pos_in_row = 0;

	return 1;
}

//
// 从高到低的顺序是 R G B,
// 结果要是 从高到低 B, G, R 
// 或者说 从低到高 R, G, B 
//
static JDIMENSION
get_24bit_row (translator_source_ptr sinfo)
/* This version is for reading 24-bit pixels */
{
	translator_common_ptr cinfo = sinfo->cinfo; 
	pic_source_ptr source = (pic_source_ptr) sinfo;
	register JSAMPROW ptr;
	register JDIMENSION col;

	if( source->current_row >= cinfo->image_height )
	{
		return 0;
	}
	ptr = source->pub.buffer;
	for (col = cinfo->image_width; col > 0; col--) {
		source->read_pixel(source); /* Load next pixel into pic_pixel */
		*ptr++ = (JSAMPLE) UCH(source->pic_pixel[2]); // R
		*ptr++ = (JSAMPLE) UCH(source->pic_pixel[1]); // G
		*ptr++ = (JSAMPLE) UCH(source->pic_pixel[0]); // B
	}
	
	source->current_row++;
	source->pos_in_row = 0;

	return 1;
}

//
// 从高到低的顺序是A, R, G, B 
// 结果要是 从高到低 A, B, G, R 
// 或者说 从低到高 R, G, B, A
//
static JDIMENSION
get_32bit_row (translator_source_ptr sinfo)
/* This version is for reading 32-bit pixels */
{
	translator_common_ptr cinfo = sinfo->cinfo; 
	pic_source_ptr source = (pic_source_ptr) sinfo;
	register JSAMPROW ptr;
	register JDIMENSION col;
	
	if( source->current_row >= cinfo->image_height )
	{
		return 0;
	}

	ptr = source->pub.buffer;
	for (col = cinfo->image_width; col > 0; col--) {
		source->read_pixel(source);
		*ptr++ = (JSAMPLE) UCH(source->pic_pixel[2]); // R
		*ptr++ = (JSAMPLE) UCH(source->pic_pixel[1]); // G
		*ptr++ = (JSAMPLE) UCH(source->pic_pixel[0]); // B
		*ptr++ = (JSAMPLE) UCH(source->pic_pixel[3]); // A
	}

	source->current_row++;
	source->pos_in_row = 0;

	return 1;
}

/*
* This method is called before the first call on get_pixel_rows.  
*/

static JDIMENSION
preload_image (translator_source_ptr sinfo)
{
	translator_common_ptr cinfo = sinfo->cinfo; 
	pic_source_ptr source = (pic_source_ptr) sinfo;
	
	source->current_row = 0;

	return 0;
}

//
// 对于多块的b0x文件，预读整个文件到内存中
//
static JDIMENSION
preload_image_for_multiple_blocks (translator_source_ptr sinfo)
{
	translator_common_ptr cinfo = sinfo->cinfo; 
	pic_source_ptr source = (pic_source_ptr) sinfo;
	
	PicHeader& header = source->picHeader;
	
	BlockDesc* blockDescs = new BlockDesc[header.blockNum];
	if ( blockDescs == NULL )
	{
		ERREXIT1(cinfo, JERR_OUT_OF_MEMORY, 2); 
	}

	size_t blockDescSize = sizeof( BlockDesc) * header.blockNum;

	if ( !ReadOK(source->pub.input_file, blockDescs,  blockDescSize) )
	{
		delete[] blockDescs;
		ERREXIT(cinfo, JERR_INPUT_EOF);
	}

	JSAMPARRAY bitmapBuffer = source->whole_image;
	int picLineSize = header.width * source->pixel_size;

	for ( int i = 0; i < header.blockNum; ++i)
	{
		BlockDesc& desc = blockDescs[i];

		int blockLineSize = desc.imageWidth * source->pixel_size;
		JSAMPROW block_row = bitmapBuffer[desc.offsetY];
		int block_col = desc.offsetX * source->pixel_size;

		JSAMPLE* dst =  block_row + block_col;
		for ( int j = 0; j < desc.imageHeight; ++j )
		{
			
			if ( !ReadOK(source->pub.input_file, dst, blockLineSize) )
			{
				delete[] blockDescs;
				ERREXIT(cinfo, JERR_INPUT_EOF);
			}

			// goto next image line
			dst += picLineSize;
		}
	}

	delete[] blockDescs;

	source->current_row = 0;

	return 0;
}

/*
* Read the file header; return image size and component count.
*/
static void 
start_input_pic (translator_source_ptr sinfo)
{
	translator_common_ptr cinfo = sinfo->cinfo; 
	pic_source_ptr source = (pic_source_ptr) sinfo;

	PicHeader& picheader = source->picHeader;
	int format;
	unsigned int width, height;
	
#define GET_2B(offset)	((unsigned int) UCH(picheader[offset]) + \
	(((unsigned int) UCH(picheader[offset+1])) << 8))

	if (! ReadOK(source->pub.input_file, &picheader, sizeof( picheader ) ))
		ERREXIT(cinfo, JERR_INPUT_EOF);

	format = picheader.format;
	width = picheader.width;
	height = picheader.height;
	
	if ( width == 0 || height == 0 || picheader.blockNum < 1 )
	{
		ERREXIT(cinfo, JERR_UNKNOWN_FORMAT); 
	}

	if( picheader.blockNum == 1 )
	{	
		// only one block
		source->pub.preload_image = preload_image;
		BlockDesc blockDesc;
		if (! ReadOK(source->pub.input_file, &blockDesc, sizeof( blockDesc ) ))
			ERREXIT(cinfo, JERR_INPUT_EOF);

		// Check the block description 
		if ( blockDesc.offsetX != 0 ||  
			blockDesc.offsetY != 0 ||
			blockDesc.imageWidth != picheader.width || 
			blockDesc.imageHeight != picheader.height )
		{
			ERREXIT(cinfo, JERR_UNKNOWN_FORMAT); 
		}
	}
	else
	{
		// multiple blocks
		source->pub.preload_image = preload_image_for_multiple_blocks;
	}

	cinfo->out_color_space = JCS_RGB;
	cinfo->out_color_components = 3;
	source->pixel_size = 1;
	int components = 3;
	switch (format)
	{
	case PIX_FMT_R5G6B5:
		sinfo->get_pixel_rows = get_565bit_row;
		source->pixel_size = 2;
		components = 3;
		break;
	case PIX_FMT_A4R4G4B4:
		sinfo->get_pixel_rows = get_4444bit_row;
		source->pixel_size = 2;
		components = 4;
		break;
	case PIX_FMT_R8G8B8:
		sinfo->get_pixel_rows = get_24bit_row;
		source->pixel_size = 3;
		components = 3;
		break;
	case PIX_FMT_A8R8G8B8:
	case PIX_FMT_X8R8G8B8:
		sinfo->get_pixel_rows = get_32bit_row;
		source->pixel_size = 4;
		components = 4;
		break;
	case PIX_FMT_A1R5G5B5:
	case PIX_FMT_X1R5G5B5:
		sinfo->get_pixel_rows = get_1555bit_row;
		source->pixel_size = 2;
		components = 4;
		break;
	default:
		ERREXIT(cinfo, JERR_UNKNOWN_FORMAT); // unsupported yet
		break;
	}
	
	if ( picheader.blockNum != 1)
	{
		/* Need a virtual array to hold the source image */
		source->whole_image = alloc_sarray( picheader.width * source->pixel_size, picheader.height );
		if ( source->whole_image == NULL )
		{
			ERREXIT1(cinfo, JERR_OUT_OF_MEMORY, 0 ); 
		}

		// clear the memory buffer with 0
		// all blocks may be not fully filling the image data
		memset(source->whole_image[0], 0, picheader.width * source->pixel_size * picheader.height);

		source->read_pixel = read_pixel_in_memory;
	}
	
	/* need a one-row input buffer. */
	sinfo->buffer = alloc_srow( width * components );
	if( sinfo->buffer == NULL ) 
	{
		ERREXIT1(cinfo, JERR_OUT_OF_MEMORY, 1); 
	}

	cinfo->input_components = components;
	cinfo->data_precision = 8;
	cinfo->image_width = width;
	cinfo->image_height = height;
}

/*
* Finish up at the end of the file.
*/

static void
finish_input_pic (translator_source_ptr sinfo)
{
	translator_common_ptr cinfo = sinfo->cinfo; 
	pic_source_ptr source = (pic_source_ptr) sinfo;
	
	if( source->whole_image != NULL )
	{
		free_sarray( source->whole_image );
		source->whole_image = NULL;
	}

	if( sinfo->buffer != NULL )
	{
		free_srow( sinfo->buffer );
		sinfo->buffer = NULL;
	}
}

/*
* The module selection routine for Picture format input.
*/

translator_source_ptr
my_init_read_pic (translator_common_ptr cinfo)
{
	pic_source_ptr source;
	/* Create module interface object */
	source = (pic_source_ptr) malloc ( sizeof(pic_source_struct) );
	source->pub.cinfo = cinfo; /* make back link for subroutines */

	/* Fill in method ptrs, except get_pixel_rows which start_input sets */
	source->pub.start_input = start_input_pic;

	/* set preload_image as a separated procedure - yanghaibo */
	source->pub.preload_image = preload_image;
	source->pub.finish_input = finish_input_pic;
	source->pub.input_file = NULL;
	source->pub.buffer = NULL;
	source->current_row = NULL;
	source->pixel_size = 0;
	source->picHeader.format = 0;
	source->picHeader.width = 0;
	source->picHeader.height = 0;
	source->picHeader.blockNum = 0;
	source->whole_image = NULL;
	source->read_pixel = read_pixel;
	source->pos_in_row = 0;
	return (translator_source_ptr) source;
}

