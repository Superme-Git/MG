/************************************************************************
 File:			Encoder.h
 CopyRight(C):	eagle
 Note:			String Encoding Convertor
 
 ------------------------------------------------------------------------
 v1.0.0		4/28/2015		Create
 
 ************************************************************************/

#ifndef __NUCLEUS_ENCODER_H__
#define __NUCLEUS_ENCODER_H__

#define _ENCODING_LOWER        1
#define _ENCODING_UPPER        2
#define _ENCODING_HALF         4
#define _ENCODING_SIMPLIFIED   8

namespace NUCLEUS
{
	extern void _EncoderInit();

	extern void _NormalizeUTF8(char* text, unsigned options);
	extern void _NormalizeGBK(char* text, unsigned options);

	extern void _GBK_TO_UTF8(const char* from, unsigned int from_len, char** to, unsigned int* to_len);
	extern void _UTF8_TO_GBK(const char* from, unsigned int from_len, char** to, unsigned int* to_len);
}

#endif
