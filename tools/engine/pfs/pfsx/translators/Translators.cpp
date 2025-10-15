//////////////////////////////////////////////////////////////////////////
//
// Translators.cpp
//
// ʵ�ַ�������PFSX LIBȱʡ�ṩ��Translators�Ľӿ�
//
// ������Щ PFSX Lib �Ѿ�����:
// 	pfsx-jpeg.lib
// 	pfsx-pic.lib
// 	pfsx-targa.lib
// 	pfsx-zip.lib **** // ���Ƴ�
//	pfsx-x2o.lib **** // ���Ƴ�
//
// module : pfsx-translators.dll
// author : yanghaibo
// date: 2008-11-12
// date: 2009-01-21 - ʵ��x2o
//
// ��ZIPTranslater/X2OTranslater��������pfslib�У���ˣ������ｫ���Ƴ�
// �����Ҫ���������2��Translater�İ汾�Ķ�̬�⣬��������ĺ꼴�ɣ�
//	#define  INCLUDE_INLINE_TRANSLATORS
//  [5/11/2009 yanghaibo]
#include "Translators.h"

#include "../jpeg/JpgTranslator.h"
#include "../pic/PicTranslator.h"
#include "../targa/TargaTranslator.h"

#ifdef INCLUDE_INLINE_TRANSLATORS
#include "../zip/ZipTranslator.h"
#include "../x2o/X2OTranslator.h"
#endif

// ȫ�־�̬������Ӧ��ֻ����ʼ��һ��
static long rgIndexEncoder[5];
static long rgIndexDecoder[5];

long TRANS_GetEncoderCount()
{
	static long lCount = -1;
	if (lCount == -1)
	{
		// ͳ��Encoder��������¼ÿ����̬���ṩ��Encoder�ķ��������ۼ�ֵ
		lCount = 0;
		lCount += PFSX::JPEG::GetEncoderCount();
		rgIndexEncoder[0] = lCount;
		lCount += PFSX::PIC::GetEncoderCount();
		rgIndexEncoder[1] = lCount;
		lCount += PFSX::TARGA::GetEncoderCount();
		rgIndexEncoder[2] = lCount;
#ifdef INCLUDE_INLINE_TRANSLATORS
		lCount += PFSX::ZIP::GetEncoderCount();
		rgIndexEncoder[3] = lCount;
		lCount += PFSX::X2O::GetEncoderCount();
		rgIndexEncoder[4] = lCount;
#endif
	}
	return lCount;
}

void* TRANS_GetEncoderItem(long lIndex)
{
	// �����δ��ʼ������������ִ��һ��
	if( rgIndexEncoder[1] == 0 ) TRANS_GetEncoderCount();

	if ( lIndex >= 0 && lIndex < rgIndexEncoder[0] )
	{
		return PFSX::JPEG::GetEncoderItem(lIndex);
	}
	else if ( lIndex < rgIndexEncoder[1] )
	{
		return PFSX::PIC::GetEncoderItem(lIndex-rgIndexEncoder[0]);
	}
	else if ( lIndex < rgIndexEncoder[2] )
	{
		return PFSX::TARGA::GetEncoderItem(lIndex-rgIndexEncoder[1]);
	}
#ifdef INCLUDE_INLINE_TRANSLATORS
	else if ( lIndex < rgIndexEncoder[3] )
	{
		return PFSX::ZIP::GetEncoderItem(lIndex-rgIndexEncoder[2]);
	}
	else if ( lIndex < rgIndexEncoder[4] )
	{
		return PFSX::X2O::GetEncoderItem(lIndex-rgIndexEncoder[3]);
	}
#endif
	else
	{
		return 0;
	}
}

long TRANS_GetDecoderCount()
{
	static long lCount = -1;
	if (lCount == -1)
	{
		// ͳ��Decoder��������¼ÿ����̬���ṩ��Decoder�ķ��������ۼ�ֵ
		lCount = 0;
		lCount += PFSX::JPEG::GetDecoderCount();
		rgIndexDecoder[0] = lCount;
		lCount += PFSX::PIC::GetDecoderCount();
		rgIndexDecoder[1] = lCount;
		lCount += PFSX::TARGA::GetDecoderCount();
		rgIndexDecoder[2] = lCount;
#ifdef INCLUDE_INLINE_TRANSLATORS
		lCount += PFSX::ZIP::GetDecoderCount();
		rgIndexDecoder[3] = lCount;
		lCount += PFSX::X2O::GetDecoderCount();
		rgIndexDecoder[4] = lCount;
#endif
	}
	return lCount;
}

void* TRANS_GetDecoderItem(long lIndex)
{
	// �����δ��ʼ������������ִ��һ��
	if( rgIndexDecoder[1] == 0 ) TRANS_GetDecoderCount();

	if ( lIndex >= 0 && lIndex < rgIndexDecoder[0] )
	{
		return PFSX::JPEG::GetDecoderItem(lIndex);
	}
	else if ( lIndex < rgIndexDecoder[1] )
	{
		return PFSX::PIC::GetDecoderItem(lIndex-rgIndexDecoder[0]);
	}
	else if ( lIndex < rgIndexDecoder[2] )
	{
		return PFSX::TARGA::GetDecoderItem(lIndex-rgIndexDecoder[1]);
	}
#ifdef INCLUDE_INLINE_TRANSLATORS
	else if ( lIndex < rgIndexDecoder[3] )
	{
		return PFSX::ZIP::GetDecoderItem(lIndex-rgIndexDecoder[2]);
	}
	else if ( lIndex < rgIndexDecoder[4] )
	{
		return PFSX::X2O::GetDecoderItem(lIndex-rgIndexDecoder[3]);
	}
#endif
	else
	{
		return 0;
	}
}
