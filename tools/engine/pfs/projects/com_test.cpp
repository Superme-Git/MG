#include "../pfs2.h"
//#include "../../JpegUtil/JpegUtil.h"
#include <iostream>

//#include "../../iolib/common/marshal.h"

//#pragma comment(lib, "jpegutil.lib")

//using namespace GNET;
//
//void TestByteOrder()
//{
//	Marshal::OctetsStream os;
//	int i = 0xfff;
//	CompactUINT cu( i ) ;
//	cu.marshal( os );
//	cu.unmarshal( os );
//	std::cout<< i << std::endl;
//}


void TestJpegUtil()
{
	int nWidth = 1024;
	int nHight = 1024;

	BYTE * pcbData;

	pcbData = new BYTE[nHight*nHight*4];

	BYTE* ptr = pcbData;
	int p = 0;
//	int j = 0;

	//for ( int t = 0; t < 4; t ++ )
	//{
	//	for ( int i = 0; i < 256; i ++ )
	//	{
	//		for( int k = 0; k < 256; k ++ )
	//		{
	//			*ptr ++ = i;
	//			*ptr ++ = j;
	//			*ptr ++ = k;
	//			*ptr ++ = 255;
	//		}

	//		for( int k = 255; k >= 0; k -- )
	//		{
	//			*ptr ++ = 255-i;
	//			*ptr ++ = j;
	//			*ptr ++ = k;
	//			*ptr ++ = 255;
	//		}

	//		if ( i % 8 == 0 )
	//		{
	//			j ++;
	//		}
	//	}

	//	for ( int i = 0; i < 256; i ++ )
	//	{
	//		for( int k = 255; k >= 0; k -- )
	//		{
	//			*ptr ++ = 255-i;
	//			*ptr ++ = j;
	//			*ptr ++ = k;
	//			*ptr ++ = 255;
	//		}

	//		for( int k = 0; k < 256; k ++ )
	//		{
	//			*ptr ++ = 255-i;
	//			*ptr ++ = j;
	//			*ptr ++ = k;
	//			*ptr ++ = 255;
	//		}

	//		if ( i % 8 == 0 )
	//		{
	//			j ++;
	//		}
	//	}

	//}
	/*
	int i = 0;
	int j = 0;
	for ( int t = 0; t < 1024; t ++)
	{
		for( int r = 0, k = 0; k < 1024; k ++ )
		{
			*ptr ++ = i;
			*ptr ++ = j;

			if ( k < 255 )
			{
				r ++;
			}
			else if ( k == 255 )
			{
				
			}
			else if ( k < 511 )
			{
				r = j;
			}
			else if ( k == 511 )
			{
				r = j;
			}
			else if ( k < 767 )
			{
				r = 255-j;
			}
			else if ( k == 767 )
			{
				r = 0;
			}
			else
			{
				r = i;
			}

			*ptr ++ = r;
			*ptr ++ = 255;
		}
		if ( t < 255 )
		{
			i ++;
			j ++;
		}
		else if( t == 255 )
		{
			j = 255;
		}
		else if ( t < 511 )
		{
			i --;
		}
		else if( t == 511 )
		{
			i = 0;
		}
		else if ( t < 767 )
		{
			j --;
		}
		else if( t == 767 )
		{
		
		}
		else
		{	
			i ++;
		}
		
	}*/




	//for ( int x = 0; x < 256; x ++)
	//{
	//	for ( int r = 0; r < 256; r ++ )
	//	{
	//		*ptr ++ = r;
	//		*ptr ++ = 255-r;
	//		*ptr ++ = 0;
	//		*ptr ++ = 255;
	//	}

	//	for ( int r = 0; r < 256; r ++ )
	//	{
	//		*ptr ++ = 255;
	//		*ptr ++ = 0;
	//		*ptr ++ = r;
	//		*ptr ++ = 255;
	//	}

	//	for ( int r = 0; r < 256; r ++ )
	//	{
	//		*ptr ++ = r;
	//		*ptr ++ = r;
	//		*ptr ++ = 255;
	//		*ptr ++ = 255;
	//	}

	//	for ( int r = 0; r < 256; r ++ )
	//	{
	//		*ptr ++ = 0;
	//		*ptr ++ = 255;
	//		*ptr ++ = 255-r;
	//		*ptr ++ = 255;
	//	}
	//}

	//for ( int x = 0; x < 256; x ++)
	//{
	//	for ( int r = 0; r < 256; r ++ )
	//	{
	//		*ptr ++ = 0; //blue --
	//		*ptr ++ = x;//255;	// green 255
	//		*ptr ++ = r-255;
	//		*ptr ++ = 255;
	//	}

	//	for ( int r = 0; r < 256; r ++ )
	//	{
	//		*ptr ++ = r;   // blue 0
	//		*ptr ++ = 255-r;
	//		*ptr ++ = 0;
	//		*ptr ++ = 255;
	//	}

	//	for ( int r = 0; r < 256; r ++ )
	//	{
	//		*ptr ++ = 255;
	//		*ptr ++ = x;//0;
	//		*ptr ++ = r;
	//		*ptr ++ = 255;
	//	}

	//	for ( int r = 0; r < 256; r ++ )
	//	{
	//		*ptr ++ = 255-r;
	//		*ptr ++ = r;
	//		*ptr ++ = 255;
	//		*ptr ++ = 255;
	//	}
	//}

	//for ( int x = 0; x < 256; x ++)
	//{
	//	for ( int r = 0; r < 256; r ++ )
	//	{
	//		*ptr ++ = 255-r; 
	//		*ptr ++ = r; // green ++
	//		*ptr ++ = x;//255;
	//		*ptr ++ = 255;
	//	}

	//	for ( int r = 0; r < 256; r ++ )
	//	{
	//		*ptr ++ = 0;
	//		*ptr ++ = 255;
	//		*ptr ++ = r-255;
	//		*ptr ++ = 255;
	//	}

	//	for ( int r = 0; r < 256; r ++ )
	//	{
	//		*ptr ++ = r;
	//		*ptr ++ = 255-r;
	//		*ptr ++ = x;//0;
	//		*ptr ++ = 255;
	//	}

	//	for ( int r = 0; r < 256; r ++ )
	//	{
	//		*ptr ++ = 255;
	//		*ptr ++ = 0;
	//		*ptr ++ = r;
	//		*ptr ++ = 255;
	//	}
	//}
	//for ( int x = 0; x < 256; x ++)
	//{
	//	for ( int r = 0; r < 256; r ++ )
	//	{
	//		*ptr ++ = x;//255;  // blue ++
	//		*ptr ++ = 0;  // green 0
	//		*ptr ++ = r;	
	//		*ptr ++ = 255;
	//	}

	//	for ( int r = 0; r < 256; r ++ )
	//	{
	//		*ptr ++ = 255-r; // blue --
	//		*ptr ++ = r;	// green ++
	//		*ptr ++ = 255;
	//		*ptr ++ = 255;
	//	}

	//	for ( int r = 0; r < 256; r ++ )
	//	{
	//		*ptr ++ = x;//0;	// blue--
	//		*ptr ++ = 255; // green 255
	//		*ptr ++ = 255-r;
	//		*ptr ++ = 255;
	//	}

	//	for ( int r = 0; r < 256; r ++ )
	//	{
	//		*ptr ++ = r;
	//		*ptr ++ = 255-r;
	//		*ptr ++ = 0;
	//		*ptr ++ = 255;
	//	}
	//}

	//RGB to YUV Conversion

	//Y  =      (0.257 * R) + (0.504 * G) + (0.098 * B) + 16

	//Cr = V =  (0.439 * R) - (0.368 * G) - (0.071 * B) + 128

	//Cb = U = -(0.148 * R) - (0.291 * G) + (0.439 * B) + 128


	//YUV to RGB Conversion

	//B = 1.164(Y - 16)                   + 2.018(U - 128)

	//G = 1.164(Y - 16) - 0.813(V - 128) - 0.391(U - 128)

	//R = 1.164(Y - 16) + 1.596(V - 128)

	for ( short Y = 0; Y < 256; Y ++)
	{
		for ( int j = 0; j < 4; j ++ )
		{
			for ( short U = 0; U < 256; U ++ )
			{
				BYTE V = 64*j;
				BYTE B = (BYTE)(1.164*(Y - 16) + 2.018*(U - 128));
				BYTE G = (BYTE)(1.164*(Y - 16) - 0.813*(V - 128) - 0.391*(U - 128));
				BYTE R = (BYTE)(1.164*(Y - 16) + 1.596*(V - 128));

				*ptr ++ = B;
				*ptr ++ = G;
				*ptr ++ = R;
				*ptr ++ = 255;
			}
		}
	}


	for ( short Y = 0; Y < 256; Y ++)
	{
		for ( int j = 0; j < 4; j ++ )
		{
			for ( short V = 0; V < 256; V ++ )
			{
				BYTE U = 64*j;
				BYTE B = (BYTE)(1.164*(Y - 16) + 2.018*(U - 128));
				BYTE G = (BYTE)(1.164*(Y - 16) - 0.813*(V - 128) - 0.391*(U - 128));
				BYTE R = (BYTE)(1.164*(Y - 16) + 1.596*(V - 128));

				*ptr ++ = B;
				*ptr ++ = G;
				*ptr ++ = R;
				*ptr ++ = 255;
			}
		}
	}

	for ( short U = 0; U < 256; U ++ )	
	{
		for ( int j = 0; j < 4; j ++ )
		{
			for ( short Y = 0; Y < 256; Y ++)
			{
				BYTE V = 0;
				BYTE B = (BYTE)(1.164*(Y - 16) + 2.018*(U - 128));
				BYTE G = (BYTE)(1.164*(Y - 16) - 0.813*(V - 128) - 0.391*(U - 128));
				BYTE R = (BYTE)(1.164*(Y - 16) + 1.596*(V - 128));

				*ptr ++ = B;
				*ptr ++ = G;
				*ptr ++ = R;
				*ptr ++ = 255;
			}
		}
	}

	for ( short V = 0; V < 256; V ++ )
	{
		for ( int j = 0; j < 4; j ++ )
		{
			for ( short Y = 0; Y < 256; Y ++)
			{
				BYTE U = 0;
				BYTE B = (BYTE)(1.164*(Y - 16) + 2.018*(U - 128));
				BYTE G = (BYTE)(1.164*(Y - 16) - 0.813*(V - 128) - 0.391*(U - 128));
				BYTE R = (BYTE)(1.164*(Y - 16) + 1.596*(V - 128));

				*ptr ++ = B;
				*ptr ++ = G;
				*ptr ++ = R;
				*ptr ++ = 255;
			}
		}
	}
//
//for ( short Y = 0; Y < 256; Y ++)
//{
//	for ( int j = 0; j < 4; j ++ )
//	{
//		for ( short U = 0; U < 256; U ++ )
//		{
//			BYTE V = 64*j;
//			BYTE B = 1.164*(Y - 16) + 2.018*(U - 128);
//			BYTE G = 1.164*(Y - 16) - 0.813*(V - 128) - 0.391*(U - 128);
//			BYTE R = 1.164*(Y - 16) + 1.596*(V - 128);
//
//			*ptr ++ = B;
//			*ptr ++ = G;
//			*ptr ++ = R;
//			*ptr ++ = 255;
//		}
//	}
//}
//
//
//for ( short Y = 0; Y < 256; Y ++)
//{
//	for ( int j = 0; j < 4; j ++ )
//	{
//		for ( short V = 0; V < 256; V ++ )
//		{
//			BYTE U = 64*j;
//			BYTE B = 1.164*(Y - 16) + 2.018*(U - 128);
//			BYTE G = 1.164*(Y - 16) - 0.813*(V - 128) - 0.391*(U - 128);
//			BYTE R = 1.164*(Y - 16) + 1.596*(V - 128);
//
//			*ptr ++ = B;
//			*ptr ++ = G;
//			*ptr ++ = R;
//			*ptr ++ = 255;
//		}
//	}
//}

//RGB to YUV Conversion

//Y  =      (0.257 * R) + (0.504 * G) + (0.098 * B) + 16

//Cr = V =  (0.439 * R) - (0.368 * G) - (0.071 * B) + 128

//Cb = U = -(0.148 * R) - (0.291 * G) + (0.439 * B) + 128


//YUV to RGB Conversion

//B = 1.164(Y - 16)                   + 2.018(U - 128)

//G = 1.164(Y - 16) - 0.813(V - 128) - 0.391(U - 128)

//R = 1.164(Y - 16) + 1.596(V - 128)
//
//for ( short B = 0; B < 256; B ++ )	
//{
//	for ( int G = 0; G < 4; G ++ )
//	{
//		for ( short R = 0; R < 256; R ++)
//		{
//			BYTE V = (0.439 * R) - (0.368 * G) - (0.071 * B) + 128;
//			BYTE Y = (0.257 * R) + (0.504 * G) + (0.098 * B) + 16;
//			BYTE U = -(0.148 * R) - (0.291 * G) + (0.439 * B) + 128;
//			
//			*ptr ++ = V;
//			*ptr ++ = Y;
//			*ptr ++ = U;
//			*ptr ++ = 255;
//		}
//	}
//}
//for ( short B = 0; B < 256; B ++ )	
//{
//	for ( int G = 0; G < 4; G ++ )
//	{
//		for ( short R = 0; R < 256; R ++)
//		{
//			BYTE V = (0.439 * R) - (0.368 * G) - (0.071 * B) + 128;
//			BYTE Y = (0.257 * R) + (0.504 * G) + (0.098 * B) + 16;
//			BYTE U = -(0.148 * R) - (0.291 * G) + (0.439 * B) + 128;
//
//			*ptr ++ = Y;
//			*ptr ++ = V;
//			*ptr ++ = U;
//			*ptr ++ = 255;
//		}
//	}
//}
//for ( short B = 0; B < 256; B ++ )	
//{
//	for ( int G = 0; G < 4; G ++ )
//	{
//		for ( short R = 0; R < 256; R ++)
//		{
//			BYTE V = (0.439 * R) - (0.368 * G) - (0.071 * B) + 128;
//			BYTE Y = (0.257 * R) + (0.504 * G) + (0.098 * B) + 16;
//			BYTE U = -(0.148 * R) - (0.291 * G) + (0.439 * B) + 128;
//
//			*ptr ++ = U;
//			*ptr ++ = V;
//			*ptr ++ = Y;
//			*ptr ++ = 255;
//		}
//	}
//}
//
//for ( short B = 0; B < 256; B ++ )	
//{
//	for ( int G = 0; G < 4; G ++ )
//	{
//		for ( short R = 0; R < 256; R ++)
//		{
//			BYTE V = (0.439 * R) - (0.368 * G) - (0.071 * B) + 128;
//			BYTE Y = (0.257 * R) + (0.504 * G) + (0.098 * B) + 16;
//			BYTE U = -(0.148 * R) - (0.291 * G) + (0.439 * B) + 128;
//
//			*ptr ++ = Y;
//			*ptr ++ = U;
//			*ptr ++ = V;
//			*ptr ++ = 255;
//		}
//	}
//}
	std::wcout<< L"TEST start ... " << std::endl;

	LPCWSTR fileName = L"outputJpegUtil.jpg";
	int iRet = 1; //JUtilRawMemoryToJpegFile(pcbData, nWidth, nHight, fileName  );
	if ( iRet == 0 )
	{
		std::wcout<< fileName << L" generated!" << std::endl;
	}
	else
		std::wcout<< fileName << L" failed !" << std::endl;

	delete[] pcbData;

	std::wcout<< L"TEST done!" << std::endl;
}