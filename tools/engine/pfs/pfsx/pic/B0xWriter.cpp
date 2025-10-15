//////////////////////////////////////////////////////////////////////////
//
// B0xWriter.cpp
//
// 从BMP中还原B0x(b00~b04, b06,b07)的实现
//
// author : yanghaibo
// date : 2008-11-20

#include "../common/Translator.h"
#include <vector>

enum PixelFormat
{
	PIX_FMT_R5G6B5,
	PIX_FMT_A4R4G4B4,	
	PIX_FMT_R8G8B8,
	PIX_FMT_A8R8G8B8,
	PIX_FMT_X8R8G8B8,	// 如果输入通道只有3，输出通道要求4，生成此类型，而非A8R8G8B8类型
	PIX_FMT_A8,			// 8bit gray　目前不支持
	PIX_FMT_A1R5G5B5,
	PIX_FMT_X1R5G5B5,	// 如果输入通道只有3，输出通道要求4，生成此类型，而非A1R5G5B5类型

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

typedef struct _pic_dest_struct {
	struct translator_dest_struct pub;	/* public fields */
	JDIMENSION buffer_width;	/* width of one row */
	JSAMPARRAY whole_image;	/* Needed to hold the whole image for cutting the blocks */
	JDIMENSION source_row;	/* Current source row number */
	int		   format;		/* sub format */
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

typedef unsigned char BYTE;
typedef unsigned long DWORD;

int CalcBytesPerPixel( int format )
{
	int bytesPerPixel;
	switch( format )
	{
	case PIX_FMT_R5G6B5:	// 0
	case PIX_FMT_A4R4G4B4:	// 1
		bytesPerPixel = 2;
		break;
	case PIX_FMT_R8G8B8:	// 2
		bytesPerPixel = 3;
		break;
	case PIX_FMT_A8R8G8B8:	// 3
	case PIX_FMT_X8R8G8B8:	// 4
		bytesPerPixel = 4;
		break;
	case PIX_FMT_A1R5G5B5:	// 6
	case PIX_FMT_X1R5G5B5:	// 7
		bytesPerPixel = 2;
		break;
	default:
		assert(false);
		bytesPerPixel = 1;
		break;
	}

	return bytesPerPixel;
}

class IBlockLineWriter
{
public:
	virtual bool WriteLine(PFS::CBaseFile* pFile, const BYTE* pRowSource) = 0;
	virtual void Destroy() = 0;
};

class CBaseLineWriter : public IBlockLineWriter
{
protected:
	BYTE* m_rowBuffer;
	int m_rowLength;
	int m_blockWidth;
	int m_bppInput;
	int m_bppOutput;

public:
	CBaseLineWriter(int format, int bppInput, int iBlockWidth )
	{
		m_bppInput = bppInput;
		m_bppOutput = CalcBytesPerPixel( format );
		m_blockWidth = iBlockWidth;
		m_rowLength = m_bppOutput * m_blockWidth;
		m_rowBuffer = alloc_srow( m_rowLength );
	}

	virtual ~CBaseLineWriter( )
	{
		free_srow( m_rowBuffer );
	}

	virtual void Destroy()
	{
		delete this;
	}
};

class C565LineWriter : public CBaseLineWriter
{
public:
	C565LineWriter(int format, int bppInput, int iBlockWidth )
		: CBaseLineWriter( format, bppInput, iBlockWidth )
	{
		// 因为我们已经不做丢块处理，所以，这里保证输入总是3个components
		assert(  m_bppInput == 3 );
	}
	
	virtual bool WriteLine(PFS::CBaseFile* pFile, const BYTE* pRowSource)
	{
		register const BYTE* inptr = pRowSource;
		register JDIMENSION col;
		register short* ptr = (short*)m_rowBuffer;
		for (col = m_blockWidth; col > 0; col--) {
			/* B8G8R8 to B5G6R5 order */
			// 255.0/31.0 = 8.2258064516129032258064516129032
			// 255.0/63.0 = 4.047619047619047619047619047619
			unsigned short r5 = (unsigned short)(GETJSAMPLE(inptr[2])/8.2258);		// R
			unsigned short g6 = (unsigned short)(GETJSAMPLE(inptr[1])/4.047619);	// G
			unsigned short b5 = (unsigned short)(GETJSAMPLE(inptr[0])/8.2258);		// B
			ptr[0] = (unsigned short) (((r5 & 0x1F) << 11) | ((g6 & 0x3F) << 5) | (b5 & 0x1F));
			inptr += m_bppInput; // 尽管 3 or 4 都可以，但是应该总是3
			ptr ++;;
		}

		return pFile->Write( m_rowBuffer, m_rowLength ) == m_rowLength;
	}
};

class C1555LineWriter : public CBaseLineWriter
{
public:
	C1555LineWriter(int format, int bppInput, int iBlockWidth )
		: CBaseLineWriter( format, bppInput, iBlockWidth )
	{
		// 因为我们已经不做丢块处理，所以，这里保证输入总是4个components
		assert(  m_bppInput == 4 );
	}

	virtual bool WriteLine(PFS::CBaseFile* pFile, const BYTE* pRowSource)
	{
		register const BYTE* inptr = pRowSource;
		register JDIMENSION col;
		register short* ptr = (short*)m_rowBuffer;
		for (col = m_blockWidth; col > 0; col--) {
			/* A8B8G8R8 to A1B5G5R5 order */
			// 255.0/31.0 = 8.2258064516129032258064516129032
			unsigned short r5 = (unsigned short)(GETJSAMPLE(inptr[2])/8.2258);	// R
			unsigned short g5 = (unsigned short)(GETJSAMPLE(inptr[1])/8.2258);	// G
			unsigned short b5 = (unsigned short)(GETJSAMPLE(inptr[0])/8.2258);	// B

			if( m_bppInput == 3 )
			{
				ptr[0] = (unsigned short) ( 0x8FFF | ((r5 & 0x1F) << 10) | ((g5 & 0x1F) << 5) | (b5 & 0x1F));
			}
			else if( m_bppInput == 4 )
			{
				// 总是应该走这里
				unsigned short a1 = (unsigned short)(!!GETJSAMPLE(inptr[3])); // A
				ptr[0] = (unsigned short) ( ((a1 & 0x1) << 15) | ((r5 & 0x1F) << 10) | ((g5 & 0x1F) << 5) | (b5 & 0x1F));
			}

			inptr += m_bppInput; // 3 or 4
			ptr ++;;
		}

		return pFile->Write( m_rowBuffer, m_rowLength ) == m_rowLength;
	}
};

class C4444LineWriter : public CBaseLineWriter
{
public:
	C4444LineWriter(int format, int bppInput, int iBlockWidth )
		: CBaseLineWriter( format, bppInput, iBlockWidth )
	{
		// 因为我们已经不做丢块处理，所以，这里保证输入总是4个components
		assert(  m_bppInput == 4 );
	}

	virtual bool WriteLine(PFS::CBaseFile* pFile, const BYTE* pRowSource)
	{
		register const BYTE* inptr = pRowSource;
		register JDIMENSION col;
		register BYTE* outptr = m_rowBuffer;
		for (col = m_blockWidth; col > 0; col--)
		{
			/*A8B8G8R8 to A4R4|G4B4 order */
			outptr[0] = (JSAMPLE)(GETJSAMPLE(inptr[1])/17 << 4 | GETJSAMPLE(inptr[0])/17 ); 
			if ( m_bppInput == 3 )
			{
				outptr[1] = (JSAMPLE)(0xF | GETJSAMPLE(inptr[2])/17 );
				inptr += 3;
			}
			else // if ( m_bppInput == 4 )
			{
				// 总是应该走这里
				outptr[1] = (JSAMPLE)(GETJSAMPLE(inptr[3])/17 << 4 | GETJSAMPLE(inptr[2])/17 );
				inptr += 4;
			}
			outptr += 2; // m_bppOutput == 2
		}

		return pFile->Write( m_rowBuffer, m_rowLength ) == m_rowLength;
	}
};

class C888LineWriter : public CBaseLineWriter
{
public:
	C888LineWriter(int format, int bppInput, int iBlockWidth )
		: CBaseLineWriter( format, bppInput, iBlockWidth )
	{
		// 因为我们已经不做丢块处理，所以，这里保证输入总是3个components
		assert(  m_bppInput == 3 );
	}

	virtual bool WriteLine(PFS::CBaseFile* pFile, const BYTE* pRowSource)
	{
		if( m_bppInput == m_bppOutput ) // m_bppOutput == 3
		{
			// 总是应该走这里
			return pFile->Write( pRowSource, m_rowLength ) == m_rowLength;
		}
		else
		{
			register const BYTE* inptr = pRowSource;
			register JDIMENSION col;
			register BYTE* outptr = m_rowBuffer;
			for (col = m_blockWidth; col > 0; col--)
			{
				/* BGR to BGR order */
				outptr[0] = (JSAMPLE) GETJSAMPLE(inptr[0]); // B
				outptr[1] = (JSAMPLE) GETJSAMPLE(inptr[1]); // G
				outptr[2] = (JSAMPLE) GETJSAMPLE(inptr[2]); // R
				inptr += m_bppInput, outptr += 3;
			}

			return pFile->Write( m_rowBuffer, m_rowLength ) == m_rowLength;
		}
	}
};

class C8888LineWriter : public CBaseLineWriter
{
public:
	C8888LineWriter(int format, int bppInput, int iBlockWidth )
		: CBaseLineWriter( format, bppInput, iBlockWidth )
	{
		// 因为我们已经不做丢块处理，所以，这里保证输入总是4个components
		assert(  m_bppInput == 4 );
	}

	virtual bool WriteLine(PFS::CBaseFile* pFile, const BYTE* pRowSource)
	{
		if ( m_bppInput == 4 )
		{
			// bpp-input == bpp-output
			// 总是应该走这里
			return m_rowLength == pFile->Write( pRowSource, m_rowLength );
		}
		else
		{
			register const BYTE* inptr = pRowSource;
			register JDIMENSION col;
			register BYTE* outptr = m_rowBuffer;
			for (col = m_blockWidth; col > 0; col--)
			{
				/* BGR to BGRA order */
				outptr[0] = (JSAMPLE) GETJSAMPLE(inptr[0]); // B
				outptr[1] = (JSAMPLE) GETJSAMPLE(inptr[1]); // G
				outptr[2] = (JSAMPLE) GETJSAMPLE(inptr[2]); // R
				outptr[3] = 0xFF; // A
				inptr += 3;
				outptr += 4;
			}

			return m_rowLength == pFile->Write( m_rowBuffer, m_rowLength );
		}
	}
};

IBlockLineWriter * CreateLineWriter( int format, int bppInput, int width  )
{
	switch( format )
	{
	case PIX_FMT_R5G6B5:
		return new C565LineWriter(format, bppInput,width);
	case PIX_FMT_A1R5G5B5:
	case PIX_FMT_X1R5G5B5:
		return new C1555LineWriter(format, bppInput, width);
	case PIX_FMT_A4R4G4B4:
		return new C4444LineWriter(format, bppInput,width);
	case PIX_FMT_R8G8B8:
		return new C888LineWriter(format, bppInput,width);
	case PIX_FMT_A8R8G8B8:
	case PIX_FMT_X8R8G8B8:
		return new C8888LineWriter(format, bppInput,width);
	default:
		return NULL;
	}
}

class CBlock
{
public:
	CBlock( const BYTE* pImage, 
		int format, 
		int bytesPerPixel, // describe the input source
		int imageWidth,
		int left, 
		int top, 
		int width, 
		int height )
		: m_pImage((const BYTE* const)pImage)
		, m_format ( format )
		, m_bytesPerPixel( bytesPerPixel )
		, m_imageWidth(imageWidth)
	{
		SetRect( left, top, width, height );
	}
	
	//bool CalcRetainFlag()
	//{
	//	if ( m_bytesPerPixel != 4 )
	//	{
	//		return m_bRetainFlag = true;
	//	}
	//	else 
	//	{
	//		// need calculate the flag only if it's a 32bit picture.

	//		m_bRetainFlag = false;			
	//		const DWORD* blockPtr = (const DWORD*)m_pImage;
	//		blockPtr = &blockPtr[m_top*m_imageWidth + m_left];
	//		for(int i = 0; i < m_height; i++ )
	//		{
	//			const DWORD* rowPtr = blockPtr + m_imageWidth*i;
	//			for(int j = 0; j < m_width; j ++ )
	//			{
	//				if( rowPtr[j] != 0 ) 
	//				{
	//					m_bRetainFlag = true;
	//					break;
	//				}
	//			}
	//		}

	//		return m_bRetainFlag;
	//	}
	//}

	//bool GetRetainFlag()
	//{
	//	return m_bRetainFlag;
	//}

	void SetRect( int left, int top, int width, int height )
	{
		m_left = left;
		m_top = top;
		m_width = width;
		m_height = height;
		m_bRetainFlag = true;
	}

	bool WriteBlockHeader( PFS::CBaseFile* pFile )
	{
		int bytesPerPixel = CalcBytesPerPixel( m_format );
		unsigned fileSize = m_width*m_height*bytesPerPixel;
		struct BlockDesc blockDesc = { m_width, m_height, m_left, m_top, fileSize };
		if(pFile->Write(&blockDesc,sizeof( blockDesc)) != sizeof( blockDesc))
		{
			//assert(false);
			return false;
		}
		return true;
	}

	bool WriteBlockContent( PFS::CBaseFile* pFile )
	{
		IBlockLineWriter* pLineWriter = CreateLineWriter( m_format, m_bytesPerPixel, m_width  );
		if ( pLineWriter == NULL )
		{
			return false;
		}

		int lineSize = m_imageWidth * m_bytesPerPixel;
		int col_offset = m_bytesPerPixel * m_left;
		const BYTE* pBlock = &m_pImage[ m_top * lineSize ] + col_offset;
		for ( int i = 0; i < m_height; i ++ )
		{
			const BYTE* pRow = &pBlock[ i * lineSize ];
			if( !pLineWriter->WriteLine( pFile, pRow) )
			{
				//assert(false);
				pLineWriter->Destroy();
				return false;
			}
		}

		pLineWriter->Destroy();

		return true;
	}

public:
	const BYTE* const m_pImage; // 4 bytesPerPixel always
	const int m_format;
	const int m_bytesPerPixel;// 3 or 4
	const int m_imageWidth;

private:
	int m_left;
	int m_top;
	int m_width;
	int m_height;
	bool m_bRetainFlag;	
};

class CB0xPicture
{
private:

	typedef std::vector<int> INT_VEC;

	static int GetMaxPow2( int size)
	{
		if(size >= 256)return 256;
		if(size >= 128)return 128;
		if(size >= 64)return 64;
		if(size >= 32)return 32;
		return size;
	}

	static const int MAX_WASTE = 15;

	static void CalcCutSize( int n, INT_VEC& cuts)
	{
		while ( n > 32)
		{
			int c = GetMaxPow2( n + MAX_WASTE);
			if ( c - n >= 0)
			{
				cuts.push_back( n);
				return;
			}
			else
			{
				cuts.push_back( c);
				n -= c;
			}
		}

		cuts.push_back( n);
	}

	static void CalcCutSize( int w, int h, INT_VEC& cutw, INT_VEC& cuth)
	{
		CalcCutSize( w, cutw);
		CalcCutSize( h, cuth);
	}

public:
	const BYTE* const m_pImage; 
	const int m_format;
	const int m_imageWidth;
	const int m_imageHeight;
	const int m_bytesPerPixel;// 3 or 4

private:
	std::vector<CBlock*> m_blocks;

public:

	CB0xPicture( 
		const BYTE* pImage,
		int format,
		int bytesPerPixel, // describe the input source
		int imageWidth, 
		int imageHeight )
		: m_pImage( pImage )
		, m_format ( format )
		, m_bytesPerPixel( bytesPerPixel )
		, m_imageWidth( imageWidth )
		, m_imageHeight( imageHeight)
	{
	}

	~CB0xPicture()
	{
		std::vector<CBlock*>::const_iterator itr = m_blocks.begin();
		for ( ; itr != m_blocks.end(); ++ itr )
		{
			delete *itr;
		}
	}

	int CutPicture()
	{
		INT_VEC cutW, cutH;

		CalcCutSize( m_imageWidth, m_imageHeight, cutW, cutH );
		
		CBlock* pLastBlock = NULL;
		int sizeH = (int)cutH.size();
		int sizeW = (int)cutW.size();
		for ( int j = 0, left = 0; j < sizeW; left += cutW[j++] )
		{
			for ( int i = 0, top = 0; i < sizeH; top += cutH[i++] )
			{
				pLastBlock = new CBlock( m_pImage, m_format, m_bytesPerPixel,
					m_imageWidth, left, top, cutW[j], cutH[i] );
				m_blocks.push_back( pLastBlock );
			}
		}

		// return the block count
		return (int)m_blocks.size();
	}

	bool WriteFile( PFS::CBaseFile* pFile )
	{
		// 写文件头
		PicHeader header = { m_format, m_imageWidth, m_imageHeight, (int)m_blocks.size() };
		if( pFile->Write(&header, sizeof(header)) != sizeof(header) )
		{
			return false;
		}

		// 写多个块头
		std::vector<CBlock*>::const_iterator itr = m_blocks.begin();
		for ( ; itr != m_blocks.end(); ++ itr )
		{
			if( !(*itr)->WriteBlockHeader( pFile ) )
			{
				return false;
			}
		}

		// 写多个块内容
		itr = m_blocks.begin();
		for ( ; itr != m_blocks.end(); ++ itr )
		{
			if( !(*itr)->WriteBlockContent( pFile ) )
			{
				return false;
			}
		}

		return true;
	}
};

/*
* Write some pixel data.
* In this module rows_supplied will always be 1.
*/

//////////////////////////////////////////////////////////////////////////
//
// input is 3 components and output is 3 components
//
// RGB to BGR order
//
static void 
put_24pixel_rows (translator_dest_ptr dinfo,
				  JDIMENSION rows_supplied)
/* used for unquantized full-color output */
{
	translator_common_ptr cinfo = dinfo->cinfo; 
	pic_dest_ptr dest = (pic_dest_ptr) dinfo;
	register const BYTE* inptr;
	register JSAMPLE * outptr;
	register JDIMENSION col;

	inptr = dest->pub.buffer;
	outptr = dest->whole_image[dest->source_row];
	for (col = cinfo->image_width; col > 0; col--) {
		/* RGB to BGR order */
		outptr[0] = (JSAMPLE) GETJSAMPLE(inptr[2]); // B
		outptr[1] = (JSAMPLE) GETJSAMPLE(inptr[1]); // G
		outptr[2] = (JSAMPLE) GETJSAMPLE(inptr[0]); // R
		inptr += 3, outptr += 3;
	}

	dest->source_row ++;
}

//////////////////////////////////////////////////////////////////////////
//
// input is 4 components and output is 3 components
//
// RGBA to BGR order
//
static void 
put_24pixel_rows_a (translator_dest_ptr dinfo,
				  JDIMENSION rows_supplied)
/* used for unquantized full-color output */
{
	translator_common_ptr cinfo = dinfo->cinfo; 
	pic_dest_ptr dest = (pic_dest_ptr) dinfo;
	register const BYTE* inptr;
	register JSAMPLE * outptr;
	register JDIMENSION col;

	inptr = dest->pub.buffer;
	outptr = dest->whole_image[dest->source_row];
	for (col = cinfo->image_width; col > 0; col--) {
		/* RGBA to BGR order */
		outptr[0] = (JSAMPLE) GETJSAMPLE(inptr[2]); // B
		outptr[1] = (JSAMPLE) GETJSAMPLE(inptr[1]); // G
		outptr[2] = (JSAMPLE) GETJSAMPLE(inptr[0]); // R
		// drop the alpha channel
		inptr += 4, outptr += 3;
	}

	dest->source_row ++;
}

//////////////////////////////////////////////////////////////////////////
//
// input is 4 components and output is 4 components
//
// RGBA to BGRA order
//
/* for 32bit rows */
static void 
put_32pixel_rows (translator_dest_ptr dinfo,
				  JDIMENSION rows_supplied)
{

	translator_common_ptr cinfo = dinfo->cinfo; 
	pic_dest_ptr dest = (pic_dest_ptr) dinfo;
	register JSAMPROW inptr;
	register JSAMPLE * outptr;
	register JDIMENSION col;

	inptr = dest->pub.buffer;
	outptr = dest->whole_image[dest->source_row];
	for (col = cinfo->image_width; col > 0; col--) {
		/* RGBA to BGRA order */
		outptr[0] = (JSAMPLE) GETJSAMPLE(inptr[2]); // B
		outptr[1] = (JSAMPLE) GETJSAMPLE(inptr[1]); // G
		outptr[2] = (JSAMPLE) GETJSAMPLE(inptr[0]); // R
		outptr[3] = (JSAMPLE) GETJSAMPLE(inptr[3]); // A
		inptr += 4;
		outptr += 4;
	}

	dest->source_row ++;
}

//////////////////////////////////////////////////////////////////////////
//
// input is 3 components and output is 4 components
//
// RGB to BGRA order
// 
static void 
put_32pixel_rows_a(translator_dest_ptr dinfo,
				  JDIMENSION rows_supplied)
{

	translator_common_ptr cinfo = dinfo->cinfo; 
	pic_dest_ptr dest = (pic_dest_ptr) dinfo;
	register JSAMPROW inptr;
	register JSAMPLE * outptr;
	register JDIMENSION col;

	inptr = dest->pub.buffer;
	outptr = dest->whole_image[dest->source_row];
	for (col = cinfo->image_width; col > 0; col--) {
		/* RGB to BGRA order */
		outptr[0] = (JSAMPLE) GETJSAMPLE(inptr[2]); // B
		outptr[1] = (JSAMPLE) GETJSAMPLE(inptr[1]); // G
		outptr[2] = (JSAMPLE) GETJSAMPLE(inptr[0]); // R
		outptr[3] = 0xFF;							// A
		inptr += 3;
		outptr += 4;
	}

	dest->source_row ++;
}

/*
* Startup: write the file header.
*/

static void 
start_output_pic (translator_dest_ptr dinfo)
{
	pic_dest_ptr dest = (pic_dest_ptr)dinfo;
	dest->source_row = 0;
}

/*
* Finish up at the end of the file.
*/

static void 
finish_output_pic (translator_dest_ptr dinfo)
{
	dinfo->buffer = NULL;
	pic_dest_ptr dest = (pic_dest_ptr)dinfo;

	//
	// 构造Picture对象用于分割和写文件
	//
	int bytesPerPixel  = dest->buffer_width/dinfo->cinfo->image_width;
	CB0xPicture B0xPicture( (const BYTE* const)dest->whole_image[0], 
		dest->format, bytesPerPixel, dinfo->cinfo->image_width, dinfo->cinfo->image_height );
	
	// 分割整个图为多个block
	B0xPicture.CutPicture( );
	
	bool bRet = B0xPicture.WriteFile( dinfo->output_file );
	
	// 不再使用图像缓存
	if ( dest->whole_image != NULL )
	{
		free_sarray( dest->whole_image );
		dest->whole_image = NULL;
	}

	if( !bRet )
	{
		ERREXIT(dinfo->cinfo, JERR_FILE_WRITE ); 
	}
}

/*
* The module selection routine for b0x(b00~b04) format output.
*/

translator_dest_ptr
my_init_write_b0x (translator_common_ptr cinfo)
{
	pic_dest_ptr dest;

	/* Create module interface object, fill in method pointers */
	dest = (pic_dest_ptr) malloc(sizeof(pic_dest_struct));
	dest->pub.cinfo = cinfo;
	dest->pub.start_output = start_output_pic;
	dest->pub.finish_output = finish_output_pic;
	dest->pub.put_pixel_rows = NULL;

	/* Create I/O buffer.  Note we make this near on a PC. */

	int format = PIX_FMT_R5G6B5;
	if ( cinfo->input_components == 3 )
	{
		if( cinfo->out_color_components == 3)
		{
			dest->pub.put_pixel_rows = put_24pixel_rows;

			if( cinfo->data_precision == 5 )
			{
				format = PIX_FMT_R5G6B5;
			}
			else // cinfo->data_precision == 8;
			{
				format = PIX_FMT_R8G8B8;
			}
		}
		else // out_color_components == 4;
		{
			dest->pub.put_pixel_rows = put_32pixel_rows_a;

			if( cinfo->data_precision == 4 )
			{
				format = PIX_FMT_A4R4G4B4;
			}
			else if( cinfo->data_precision == 5 )
			{
				format = PIX_FMT_X1R5G5B5;
			}
			else // data_precision == 8 
			{
				format = PIX_FMT_X8R8G8B8;
			}
		}	
	}
	else if ( cinfo->input_components == 4 )
	{
		// 因为我们已经不再根据Alpha通道进行丢块处理，所以，
		// 这里根据不同的输出通道要求执行不同的缓存处理
		if( cinfo->out_color_components == 3)
		{
			dest->pub.put_pixel_rows = put_24pixel_rows_a;

			if( cinfo->data_precision == 5 )
			{
				format = PIX_FMT_R5G6B5;
			}
			else // cinfo->data_precision == 8;
			{
				format = PIX_FMT_R8G8B8;
			}
		}
		else // out_color_components == 4;
		{
			dest->pub.put_pixel_rows = put_32pixel_rows;

			if( cinfo->data_precision == 4 )
			{
				format = PIX_FMT_A4R4G4B4;
			}
			else if( cinfo->data_precision == 5 )
			{
				format = PIX_FMT_A1R5G5B5;
			}
			else // data_precision == 8 
			{
				format = PIX_FMT_A8R8G8B8;
			}
		}
	}

	dest->buffer_width = cinfo->out_color_components * cinfo->image_width;
	dest->pub.buffer = NULL;

	//
	// 说明：
	// whole_image 总是尝试以B8G8R8或者B8G8R8A8方式容纳图像数据
	// 在此基础上，我们对图像进行分块处理，然后将分好的块写到目标文件中。
	// 
	// dest->pub.put_pixel_rows 负责将图像数据放置到whole_image(内存中)供分块.
	//
	dest->whole_image = alloc_sarray(dest->buffer_width, cinfo->image_height);
	if ( dest->whole_image == NULL )
	{
		ERREXIT1(cinfo, JERR_OUT_OF_MEMORY, 0 ); 
	}

	dest->source_row = 0;
	dest->format = format;

	return (translator_dest_ptr) dest;
}
