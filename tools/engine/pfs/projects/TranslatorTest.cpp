#include <iostream>
#include <iomanip>
#include "../pfs2.h"

enum PixelFormat
{
	PIX_FMT_R5G6B5,		// 0
	PIX_FMT_A4R4G4B4,	// 1
	PIX_FMT_R8G8B8,		// 2
	PIX_FMT_A8R8G8B8,	// 3
	PIX_FMT_X8R8G8B8,	// 4 目前不支持
	PIX_FMT_A8,			// 5 8bit gray 目前不支持
	PIX_FMT_A1R5G5B5,	// 6
	PIX_FMT_X1R5G5B5,	// 7 目前不支持

	//其它目前不支持	
};

void TestByteOrder();

namespace TRANTEST
{

bool CopyFileEx(PFS::CBaseFile* pSrcFile, std::wstring strDstFileName )
{
	assert( pSrcFile != NULL );

	PFS::CBaseFile* pDstFile = PFS::CEnv::OpenFile( strDstFileName, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_WRONLY );
	if( !pDstFile )
	{
		return false;
	}

	PFS::fsize_t pos = pSrcFile->GetPos();
	if( pos != 0 )
	{
		pSrcFile->Seek( 0, PFS::FSM_SET);
	}
	unsigned char buffer[8192];
	size_t read = 0;
	while( (read= pSrcFile->Read( buffer, 8192 ) ) != 0 )
	{
		pDstFile->Write( buffer, read );
	}
	pSrcFile->Seek( pos, PFS::FSM_SET);
	PFS::CEnv::CloseFile( pDstFile );

	return true;
}

void TestFileJpg2Bmp( std::wstring fileName )
{
	PFSX::TSID jpgDecoderEncoder = PFSX::MakeTypeIDByName('J','F','I','F');
	PFS::FILEPFSXATTRIB attribForJpg = PFSX::MakeFilePfsxAttributes( 0, jpgDecoderEncoder );

	PFS::CMetaInfo metainfo1;
	metainfo1.SetFilePfsxAttributes( attribForJpg );
	{
		using PFSX::CFileEx;
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo1 );

		CFileEx fileEx;
		if( !fileEx.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
		}
		else
		{
			std::wcout<<L"Open file: "<<fileName<<std::endl;
		}
	}

}

void TestFileJpg2Bmp2Jpg()
{
	PFSX::TSID jpgDecoderEncoder = PFSX::MakeTypeIDByName('J','F','I','F');
	PFS::FILEPFSXATTRIB attribForJpg = PFSX::MakeFilePfsxAttributes( jpgDecoderEncoder, jpgDecoderEncoder );

	PFS::CMetaInfo metainfo1;
	metainfo1.SetFilePfsxAttributes( attribForJpg );
	// 01.jpg
	{
		using PFSX::CFileEx;
		std::wstring fileName = L"/run1/01.jpg";
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo1 );

		CFileEx fileEx;
		if( !fileEx.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
		}
		else
		{
			std::wcout<<L"Open file: "<<fileName<<std::endl;
		}
	}

}

void TestFileBmp2Jpg()
{
	PFSX::TSID jpgDecoderEncoder = PFSX::MakeTypeIDByName('J','F','I','F');
	PFS::FILEPFSXATTRIB attribForJpg = PFSX::MakeFilePfsxAttributes( jpgDecoderEncoder, 0 );

	PFS::CMetaInfo metainfo1;
	metainfo1.SetFilePfsxAttributes( attribForJpg );
	// xx.bmp
	{
		using PFSX::CFileEx;
		std::wstring fileName = L"/run1/xx.bmp";
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo1 );

		CFileEx fileEx;
		if( !fileEx.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
		}
		else
		{
			std::wcout<<L"Open file: "<<fileName<<std::endl;
		}
	}

}

void TestFileBmp2Tga()
{
	PFSX::TSID DecoderEncoder = PFSX::MakeTypeIDByName('T','G','A');
	PFS::FILEPFSXATTRIB attribForJpg = PFSX::MakeFilePfsxAttributes( DecoderEncoder, 0 );

	PFS::CMetaInfo metainfo1;
	metainfo1.SetFilePfsxAttributes( attribForJpg );
	// xx.bmp
	{
		using PFSX::CFileEx;
		std::wstring fileName = L"/run1/xx.bmp";
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo1 );

		CFileEx fileEx;
		if( !fileEx.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
		}
		else
		{
			std::wcout<<L"Open file: "<<fileName<<std::endl;
		}
	}

}



void TestFileJpg2Bmp2Tga()
{
	PFSX::TSID jpgDecoderEncoder = PFSX::MakeTypeIDByName('J','F','I','F');
	PFSX::TSID tgaDecoderEncoder = PFSX::MakeTypeIDByName('T','G','A',' ');

	PFS::FILEPFSXATTRIB attribForJpg = PFSX::MakeFilePfsxAttributes( tgaDecoderEncoder, jpgDecoderEncoder );

	PFS::CMetaInfo metainfo1;
	metainfo1.SetFilePfsxAttributes( attribForJpg );
	// 01.jpg
	{
		using PFSX::CFileEx;
		std::wstring fileName = L"/run1/01.jpg";
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo1 );

		CFileEx fileEx;
		if( !fileEx.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
		}
		else
		{
			std::wcout<<L"Open file: "<<fileName<<std::endl;
		}
	}

}

void TestFileTga2Bmp()
{

	PFSX::TSID tgaDecoderEncoder = PFSX::MakeTypeIDByName('T','G','A',' ');
	PFS::FILEPFSXATTRIB attribForTGA = PFSX::MakeFilePfsxAttributes( 0, tgaDecoderEncoder );
	PFS::CMetaInfo metainfo3;
	metainfo3.SetFilePfsxAttributes( attribForTGA );

	// teamui.tga
	{
		using PFSX::CFileEx;
		std::wstring fileName = //L"/run1/teamui.tga";
									L"/run1/pfs1.tmp.tga";
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo3 );

		CFileEx fileEx;
		if( !fileEx.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
		}
		else
		{
			std::wcout<<L"Open file: "<<fileName<<std::endl;
		}
	}
}


void TestFileTga2Bmp2Tga()
{

	PFSX::TSID tgaDecoderEncoder = PFSX::MakeTypeIDByName('T','G','A',' ');
	PFS::FILEPFSXATTRIB attribForTGA = PFSX::MakeFilePfsxAttributes( tgaDecoderEncoder, tgaDecoderEncoder );
	PFS::CMetaInfo metainfo3;
	metainfo3.SetFilePfsxAttributes(  attribForTGA );

	// teamui.tga
	{
		using PFSX::CFileEx;

		std::wstring fileName = L"/run1/teamui.tga";
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo3 );

		CFileEx fileEx;
		if( !fileEx.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
		}
		else
		{
			std::wcout<<L"Open file: "<<fileName<<std::endl;
		}
	}
}

void TestFileTga2Bmp2Jpg()
{
	PFSX::TSID tgaDecoderEncoder = PFSX::MakeTypeIDByName('T','G','A',' ');
	PFSX::TSID jpgDecoderEncoder = PFSX::MakeTypeIDByName('J','F','I','F');
	PFS::FILEPFSXATTRIB attribForTGA = PFSX::MakeFilePfsxAttributes( jpgDecoderEncoder, tgaDecoderEncoder );
	PFS::CMetaInfo metainfo3;
	metainfo3.SetFilePfsxAttributes(  attribForTGA );

	// teamui.tga
	{
		using PFSX::CFileEx;

		std::wstring fileName = L"/run1/teamui.tga";
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo3 );

		CFileEx fileEx;
		if( !fileEx.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
		}
		else
		{
			std::wcout<<L"Open file: "<<fileName<<std::endl;
		}
	}
}

void TestFileOpenBmp()
{
	// Mount file system once for the 2 tests.
	bool b = PFS::CEnv::Mount( L"/run1", L"D:\\temp", PFS::FST_NATIVE, PFS::MT_WRITABLE);
	if( !b)
	{
		std::wcout<<L"Mount file system failed: "<<L"D:\\temp"<<std::endl;
		return;
	}

	PFS::CEnv::CheckMetaInfo( L"/run1" );

	PFS::FILEPFSXATTRIB attribForBMP = PFSX::MakeFilePfsxAttributes( 0, 0 );
	PFS::CMetaInfo metainfo2;
	metainfo2.SetFilePfsxAttributes(  attribForBMP );
	{
		using PFSX::CFileEx;
		std::wstring fileName = L"/run1/01.bmp";
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo2 );
		CFileEx fileEx;
		if( !fileEx.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
		}
		else
		{
			std::wcout<<L"Open file: "<<fileName<<std::endl;
		}
	}
}

void TestZipEncodeFile()
{
	PFSX::TSID zipDecoderEncoder = PFSX::MakeTypeIDByName('Z','I','P',' ');
	PFS::FILEPFSXATTRIB attribForZip = PFSX::MakeFilePfsxAttributes( zipDecoderEncoder, 0 );
	PFS::CMetaInfo metainfo2;
	metainfo2.SetFilePfsxAttributes( attribForZip );
	{
		using PFSX::CFileEx;
		//std::wstring fileName = L"/run1/translator.txt";
		std::wstring fileName = L"/run1/nuclear.ncb";

		PFS::CEnv::SetFileMetaInfo(fileName, metainfo2 );
		CFileEx fileEx;
		if( !fileEx.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
		}
		else
		{
			std::wcout<<L"Open file: "<<fileName<<std::endl;
		}
	}
}

void TestZipDecodeFile()
{
	PFSX::TSID zipDecoderEncoder = PFSX::MakeTypeIDByName('Z','I','P',' ');
	PFS::FILEPFSXATTRIB attribForZip = PFSX::MakeFilePfsxAttributes( 0, zipDecoderEncoder );
	PFS::CMetaInfo metainfo2;
	metainfo2.SetFilePfsxAttributes( attribForZip );
	{
		using PFSX::CFileEx;
		//std::wstring fileName = L"/run1/translator.ziped";
		std::wstring fileName = L"/run1/nuclear.ziped";
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo2 );
		CFileEx fileEx;
		if( !fileEx.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
		}
		else
		{
			std::wcout<<L"Open file: "<<fileName<<std::endl;
		}
	}
}

void TestFilePic2Bmp()
{

	PFSX::TSID DecoderEncoder = PFSX::MakeTypeIDByName('P','I','C',' ');
	PFS::FILEPFSXATTRIB attribForPIC = PFSX::MakeFilePfsxAttributes( 0, DecoderEncoder );
	PFS::CMetaInfo metainfo3;
	metainfo3.SetFilePfsxAttributes( attribForPIC );

	// 1.pic A8R8G8B8
	{
		using PFSX::CFileEx;
		std::wstring fileName = L"/run1/1.pic";
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo3 );

		CFileEx fileEx;
		if( !fileEx.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
		}
		else
		{
			std::wcout<<L"Open file: "<<fileName<<std::endl;
		}
	}


	// 7.pic R5G6B5
	{
		using PFSX::CFileEx;
		std::wstring fileName = L"/run1/7.pic";
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo3 );

		CFileEx fileEx;
		if( !fileEx.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
		}
		else
		{
			std::wcout<<L"Open file: "<<fileName<<std::endl;
		}
	}
}

void TestFileP0X2Bmp( std::wstring fileName, char PixelFormat )
{
	PFSX::TSID DecoderEncoder = PFSX::MakeTypeIDByName('P','0','0' + PixelFormat );
	PFS::FILEPFSXATTRIB attribForPIC = PFSX::MakeFilePfsxAttributes( 0, DecoderEncoder  );
	PFS::CMetaInfo metainfo0;
	metainfo0.SetFilePfsxAttributes( attribForPIC );
	{
		using PFSX::CFileEx;
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo0 );

		CFileEx fileEx;
		if( !fileEx.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
		}
		else
		{
			std::wcout<<L"Open file: "<<fileName<<std::endl;
		}
	}
}

void TestFileBmp2P0X( std::wstring fileName, char PixelFormat )
{
	PFSX::TSID DecoderEncoder = PFSX::MakeTypeIDByName('P','0','0' + PixelFormat );
	PFS::FILEPFSXATTRIB attribForPIC = PFSX::MakeFilePfsxAttributes( DecoderEncoder, 0 );
	PFS::CMetaInfo metainfo0;
	metainfo0.SetFilePfsxAttributes( attribForPIC );
	{
		using PFSX::CFileEx;
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo0 );

		CFileEx fileEx;
		if( !fileEx.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
		}
		else
		{
			std::wstring fileName2 = fileName + L".p0"; 
			fileName2 += '0' + PixelFormat;

			if( !CopyFileEx( &fileEx, fileName2 ) )
			{
				std::wcout<<L"failed to copy file: "<<fileName<< L" to "<< fileName2 <<std::endl;
			}
			else
			{
				std::wcout<<L"Open file "<<fileName<< L" and saved to "<< fileName2 << std::endl;
			}
		}
	}
}

bool TestFileP0X2Jpg( std::wstring fileName, char PixelFormat, std::wstring& resultFileName )
{
	PFSX::TSID DecoderEncoder = PFSX::MakeTypeIDByName('P','0','0' + PixelFormat );
	PFSX::TSID jpgDecoderEncoder = PFSX::MakeTypeIDByName('J','F','I','F');
	PFS::FILEPFSXATTRIB attribForPIC = PFSX::MakeFilePfsxAttributes( jpgDecoderEncoder, DecoderEncoder  );
	PFS::CMetaInfo metainfo0;
	metainfo0.SetFilePfsxAttributes( attribForPIC );
	{
		using PFSX::CFileEx;
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo0 );

		CFileEx fileEx;
		if( !fileEx.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
			return false;
		}
		else
		{
			std::wcout<<L"Open file: "<<fileName<<std::endl;
		}

		resultFileName = fileName + L".jpg";

		if( !CopyFileEx( &fileEx, resultFileName ) )
		{
			std::wcout<<L"failed to copy file: "<<fileName<< L" to "<< resultFileName <<std::endl;
			return false;
		}
		else
		{
			std::wcout<<L"Open file "<<fileName<< L" and saved to "<< resultFileName << std::endl;
		}
	}

	return true;
}

bool TestFileJpg2P0X(std::wstring fileName, char DestPixelFormat, std::wstring& dstFileName )
{
	PFSX::TSID jpgDecoderEncoder = PFSX::MakeTypeIDByName('J','F','I','F');
	PFSX::TSID DestEncoder = PFSX::MakeTypeIDByName('P','0','0' + DestPixelFormat );

	PFS::FILEPFSXATTRIB attribForPIC2 = PFSX::MakeFilePfsxAttributes( DestEncoder, jpgDecoderEncoder);
	{
		PFS::CMetaInfo metainfo2;
		metainfo2.SetFilePfsxAttributes( attribForPIC2 );
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo2);
		PFSX::CFileEx fileEx2;
		if( !fileEx2.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
			return false;
		}
		else
		{
			std::wstring fileName2 = fileName + L".p0"; fileName2 += '0' + DestPixelFormat;

			if( !CopyFileEx( &fileEx2, fileName2 ) )
			{
				std::wcout<<L"failed to copy file: "<<fileName<< L" to "<< fileName2 <<std::endl;
				return false;
			}
			else
			{
				std::wcout<<L"Open file "<<fileName<< L" and saved to "<< fileName2 << std::endl;
			}

			dstFileName = fileName2;
		}
	}

	return true;
}

void TestFileP0X2Jpg2P0X (std::wstring fileName, char SrcPixelFormat )
{
	std::wstring strFileName1;
	if( !TestFileP0X2Jpg( fileName, SrcPixelFormat, strFileName1 ) )
	{
		return;
	}

	std::wstring strFileName2;
	TestFileJpg2P0X( strFileName1, 0, strFileName2 );
	TestFileJpg2P0X( strFileName1, 1, strFileName2 );
	TestFileJpg2P0X( strFileName1, 2, strFileName2 );
	TestFileJpg2P0X( strFileName1, 3, strFileName2 );
	TestFileJpg2P0X( strFileName1, 4, strFileName2 );
	TestFileJpg2P0X( strFileName1, 6, strFileName2 );
	TestFileJpg2P0X( strFileName1, 7, strFileName2 );
}

void TestFileB0X2Bmp(std::wstring fileName, char PixelFormat)
{
	PFSX::TSID DecoderEncoder = PFSX::MakeTypeIDByName('B','0','0' + PixelFormat );
	PFS::FILEPFSXATTRIB attribForPIC = PFSX::MakeFilePfsxAttributes( 0, DecoderEncoder  );
	PFS::CMetaInfo metainfo0;
	metainfo0.SetFilePfsxAttributes( attribForPIC );

	{
		using PFSX::CFileEx;
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo0 );

		CFileEx fileEx;
		if( !fileEx.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
		}
		else
		{
			std::wcout<<L"Open file: "<<fileName<<std::endl;
		}
	}
}

void TestFileB0X2Bmp2Jpg(std::wstring fileName, char PixelFormat)
{
	PFSX::TSID DecoderEncoder = PFSX::MakeTypeIDByName('B','0','0' + PixelFormat );
	PFSX::TSID jpgDecoderEncoder = PFSX::MakeTypeIDByName('J','F','I','F');
	PFS::FILEPFSXATTRIB attribForPIC = PFSX::MakeFilePfsxAttributes( jpgDecoderEncoder, DecoderEncoder  );
	PFS::FILEPFSXATTRIB attribForPIC2 = PFSX::MakeFilePfsxAttributes( DecoderEncoder, jpgDecoderEncoder);
	PFS::CMetaInfo metainfo0;
	metainfo0.SetFilePfsxAttributes( attribForPIC );

	PFS::CMetaInfo metainfo2;
	metainfo2.SetFilePfsxAttributes( attribForPIC2 );
	{
		using PFSX::CFileEx;
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo0 );

		CFileEx fileEx;
		if( !fileEx.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
		}
		else
		{
			std::wcout<<L"Open file: "<<fileName<<std::endl;
		}
	}
}


void TestFileBmp2B0X(std::wstring fileName, char PixelFormat)
{
	PFSX::TSID DecoderEncoder = PFSX::MakeTypeIDByName('B','0','0' + PixelFormat );
	PFSX::TSID jpgDecoderEncoder = 0;//PFSX::MakeTypeIDByName('J','F','I','F');
	PFS::FILEPFSXATTRIB attribForPIC = PFSX::MakeFilePfsxAttributes( jpgDecoderEncoder, DecoderEncoder  );
	PFS::FILEPFSXATTRIB attribForPIC2 = PFSX::MakeFilePfsxAttributes( DecoderEncoder, jpgDecoderEncoder);
	PFS::CMetaInfo metainfo0;
	metainfo0.SetFilePfsxAttributes( attribForPIC );

	PFS::CMetaInfo metainfo2;
	metainfo2.SetFilePfsxAttributes( attribForPIC2 );
	{
		using PFSX::CFileEx;
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo2 );

		CFileEx fileEx;
		if( !fileEx.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
		}
		else
		{
			std::wstring fileName2 = fileName + L".b0"; 
			fileName2 += '0' + PixelFormat;

			if( !CopyFileEx( &fileEx, fileName2 ) )
			{
				std::wcout<<L"failed to copy file: "<<fileName<< L" to "<< fileName2 <<std::endl;
			}
			else
			{
				std::wcout<<L"Open file "<<fileName<< L" and saved to "<< fileName2 << std::endl;
			}
		}
	}
}

void TestFileJpg2Bmp2B0X(std::wstring fileName, char PixelFormat)
{
	PFSX::TSID DecoderEncoder = PFSX::MakeTypeIDByName('B','0','0' + PixelFormat );
	PFSX::TSID jpgDecoderEncoder = PFSX::MakeTypeIDByName('J','F','I','F');
	//PFS::FILEPFSXATTRIB attribForPIC = PFSX::MakeFilePfsxAttributes( jpgDecoderEncoder, DecoderEncoder  );
	PFS::FILEPFSXATTRIB attribForPIC2 = PFSX::MakeFilePfsxAttributes( DecoderEncoder, jpgDecoderEncoder);
	//PFS::CMetaInfo metainfo0;
	//metainfo0.SetFilePfsxAttributes( attribForPIC );

	PFS::CMetaInfo metainfo2;
	metainfo2.SetFilePfsxAttributes( attribForPIC2 );
	{
		using PFSX::CFileEx;
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo2);

		CFileEx fileEx;
		if( !fileEx.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
		}
		else
		{
			std::wcout<<L"Open file: "<<fileName<<std::endl;
		}
	}
}

void TestFileJpg2Bmp2P0X(std::wstring fileName, char PixelFormat)
{
	PFSX::TSID DecoderEncoder = PFSX::MakeTypeIDByName('P','0','0' + PixelFormat );
	PFSX::TSID jpgDecoderEncoder = PFSX::MakeTypeIDByName('J','F','I','F');
	PFS::FILEPFSXATTRIB attribForPIC2 = PFSX::MakeFilePfsxAttributes( DecoderEncoder, jpgDecoderEncoder);

	PFS::CMetaInfo metainfo2;
	metainfo2.SetFilePfsxAttributes( attribForPIC2 );
	{
		using PFSX::CFileEx;
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo2);

		CFileEx fileEx;
		if( !fileEx.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
		}
		else
		{
			std::wcout<<L"Open file: "<<fileName<<std::endl;
		}
	}
}


void TestFileB0X2Bmp2B0X(std::wstring fileName, char SrcPixelFormat, char DestPixelFormat)
{
	PFSX::TSID SrcDecoder = PFSX::MakeTypeIDByName('B','0','0' + SrcPixelFormat );
	PFSX::TSID DestEncoder = PFSX::MakeTypeIDByName('B','0','0' + DestPixelFormat );
	PFS::FILEPFSXATTRIB attribForPIC2 = PFSX::MakeFilePfsxAttributes( DestEncoder, SrcDecoder);
	
	PFS::CMetaInfo metainfo2;
	metainfo2.SetFilePfsxAttributes( attribForPIC2 );
	{
		using PFSX::CFileEx;
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo2);

		CFileEx fileEx;
		if( !fileEx.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
		}
		else
		{
			std::wcout<<L"Open file: "<<fileName<<std::endl;
		}
	}
}


bool TestFileB0X2Jpg(std::wstring fileName, char SrcPixelFormat, std::wstring& resultFileName)
{
	PFSX::TSID SrcDecoder = PFSX::MakeTypeIDByName('B','0','0' + SrcPixelFormat );
	PFSX::TSID jpgDecoderEncoder = PFSX::MakeTypeIDByName('J','F','I','F');
	
	PFS::FILEPFSXATTRIB attribForPIC1 = PFSX::MakeFilePfsxAttributes( jpgDecoderEncoder, SrcDecoder);
	
	PFS::CMetaInfo metainfo1;
	metainfo1.SetFilePfsxAttributes( attribForPIC1 );
	{
		using PFSX::CFileEx;
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo1);

		CFileEx fileEx;
		if( !fileEx.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
			return false;
		}
		else
		{
			std::wcout<<L"Open file: "<<fileName<<std::endl;
		}

		resultFileName = fileName + L".jpg";

		if( !CopyFileEx( &fileEx, resultFileName ) )
		{
			std::wcout<<L"failed to copy file: "<<fileName<< L" to "<< resultFileName <<std::endl;
			return false;
		}
		else
		{
			std::wcout<<L"Open file "<<fileName<< L" and saved to "<< resultFileName << std::endl;
		}
	}

	return true;
}


bool TestFileJpg2B0X(std::wstring fileName, char DestPixelFormat, std::wstring dstFileName )
{
	PFSX::TSID jpgDecoderEncoder = PFSX::MakeTypeIDByName('J','F','I','F');
	PFSX::TSID DestEncoder = PFSX::MakeTypeIDByName('B','0','0' + DestPixelFormat );

	PFS::FILEPFSXATTRIB attribForPIC2 = PFSX::MakeFilePfsxAttributes( DestEncoder, jpgDecoderEncoder);
	{
		PFS::CMetaInfo metainfo2;
		metainfo2.SetFilePfsxAttributes( attribForPIC2 );
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo2);
		PFSX::CFileEx fileEx2;
		if( !fileEx2.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
			return false;
		}
		else
		{
			std::wstring fileName2 = fileName + L".b0"; fileName2 += '0' + DestPixelFormat;

			if( !CopyFileEx( &fileEx2, fileName2 ) )
			{
				std::wcout<<L"failed to copy file: "<<fileName<< L" to "<< fileName2 <<std::endl;
				return false;
			}
			else
			{
				std::wcout<<L"Open file "<<fileName<< L" and saved to "<< fileName2 << std::endl;
			}

			dstFileName = fileName2;
		}
	}

	return true;
}

void TestFileB0X2Jpg2B0X(std::wstring fileName, char SrcPixelFormat )
{
	std::wstring strFileName1;
	if( !TestFileB0X2Jpg( fileName, SrcPixelFormat, strFileName1 ) )
	{
		return;
	}

	std::wstring strFileName2;
	TestFileJpg2B0X( strFileName1, 0, strFileName2 );
	TestFileJpg2B0X( strFileName1, 1, strFileName2 );
	TestFileJpg2B0X( strFileName1, 2, strFileName2 );
	TestFileJpg2B0X( strFileName1, 3, strFileName2 );
	TestFileJpg2B0X( strFileName1, 4, strFileName2 );
	TestFileJpg2B0X( strFileName1, 6, strFileName2 );
	TestFileJpg2B0X( strFileName1, 7, strFileName2 );

}

void TestFileEx()
{
	bool b = PFS::CEnv::Mount( L"/run1", L"D:\\temp\\b0x", PFS::FST_NATIVE, PFS::MT_WRITABLE);
	if( !b)
	{
		return;
	}

	if( !PFS::CEnv::CheckMetaInfo( L"/run1" ) )
	{
		PFS::CEnv::Unmount( L"/run1" );
		return;
	}

	//bool b = PFS::CEnv::Mount( L"/run1", L"D:\\temp\\jpg-pic", PFS::FST_NATIVE, PFS::MT_WRITABLE);
	//if( !b)
	//{
	//	return;
	//}

	//b = PFS::CEnv::Mount( L"/run2", L"D:\\temp\\jpg-pic\\run2.pfs", PFS::FST_ZIP, PFS::MT_WRITABLE);
	//if( !b)
	//{
	//	return;
	//}

	//TestFileBmp2P0X( L"/run1/women-8.bmp", PIX_FMT_R5G6B5 );
	//TestFileBmp2P0X( L"/run1/women-8.bmp", PIX_FMT_A4R4G4B4 );
	//TestFileBmp2P0X( L"/run1/women-8.bmp", PIX_FMT_R8G8B8 );
	//TestFileBmp2P0X( L"/run1/women-8.bmp", PIX_FMT_A8R8G8B8 );
	//TestFileBmp2P0X( L"/run1/women-8.bmp", PIX_FMT_X8R8G8B8 );
	//TestFileBmp2P0X( L"/run1/women-8.bmp", PIX_FMT_A1R5G5B5 );
	//TestFileBmp2P0X( L"/run1/women-8.bmp", PIX_FMT_X1R5G5B5 );


	//TestFileBmp2P0X( L"/run1/logo-32.bmp", PIX_FMT_R5G6B5 );
	//TestFileBmp2P0X( L"/run1/logo-32.bmp", PIX_FMT_A4R4G4B4 );
	//TestFileBmp2P0X( L"/run1/logo-32.bmp", PIX_FMT_R8G8B8 );
	//TestFileBmp2P0X( L"/run1/logo-32.bmp", PIX_FMT_A8R8G8B8 );


	//TestFileBmp2P0X( L"/run1/b03.bmp", PIX_FMT_R5G6B5 );
	//TestFileBmp2P0X( L"/run1/b03.bmp", PIX_FMT_A4R4G4B4 );
	//TestFileBmp2P0X( L"/run1/b03.bmp", PIX_FMT_R8G8B8 );
	//TestFileBmp2P0X( L"/run1/b03.bmp", PIX_FMT_A8R8G8B8 );

	// 0.b03 A8R8G8B8
	//TestFileB0X2Bmp(L"/run1/0.b03", PIX_FMT_A8R8G8B8);

	// 7.b03 A8R8G8B8
	//TestFileB0X2Bmp(L"/run1/7.b00", 0);
	//TestFileB0X2Bmp2Jpg(L"/run1/7.b00", 0);

	//// 8.b03 A8R8G8B8
	//TestFileB0X2Bmp(L"/run1/8.b00", 0);
	//TestFileB0X2Bmp2Jpg(L"/run1/8.b00", 0);

	// TODO:
	//TestFileJpg2Bmp2B0X( L"/run1/8.b00.jpg", 0 );
	//TestFileBmp2B0X( L"/run1/8.b00.bmp", 0 );

	//TestFileB0X2Bmp2B0X( L"/run1/8.b00", 0, 0 );
	//TestFileB0X2Bmp2B0X( L"/run1/8.b00", 0, 1 );
	//TestFileB0X2Bmp2B0X( L"/run1/8.b00", 0, 2 );
	//TestFileB0X2Bmp2B0X( L"/run1/8.b00", 0, 3 );

	//TestFileB0X2Bmp2B0X( L"/run1/1.b03", 3, 0 );
	//TestFileB0X2Bmp2B0X( L"/run1/1.b03", 3, 1 );
	//TestFileB0X2Bmp2B0X( L"/run1/1.b03", 3, 2 );
	//TestFileB0X2Bmp2B0X( L"/run1/1.b03", 3, 3 );
	//

	//TestFileBmp2Jpg();
	//TestFileBmp2Tga();
	//TestFileBmp2P0X();
	//TestFilePic2Bmp();
	
	//TestFileJpg2Bmp();
	//TestFileJpg2Bmp2Jpg();
	//TestFileTga2Bmp();
	//TestFileTga2Bmp2Tga();
	//TestFileJpg2Bmp2Tga();
	//TestFileTga2Bmp2Jpg();


	//TestFileJpg2Bmp2P0X(L"/run1/29.p03.jpg", 3 );
	//TestFileJpg2Bmp2P0X(L"/run1/30.p03.jpg", 3 );
	//TestFileJpg2Bmp2P0X(L"/run1/31.p03.jpg", 3 );

	//TestFileP0X2Bmp(  L"/run1/29.p03", 3 );
	//TestFileBmp2P0X(  L"/run1/29.p03.bmp", 3 );
	//TestFileJpg2Bmp(L"/run1/29.p03.jpg");
	//TestFileBmp2P0X(  L"/run1/29.jpg.bmp", 3 );

	//TestFileP0X2Jpg2P0X( L"/run1/29.p03", 3 );
	//TestFileP0X2Jpg2P0X( L"/run1/30.p03", 3 );
	//TestFileP0X2Jpg2P0X( L"/run1/31.p03", 3 );
	//TestFileB0X2Jpg2B0X( L"/run1/1.b03", 3 );
	//TestFileB0X2Jpg2B0X( L"/run1/2.b03", 3 );
	//TestFileB0X2Jpg2B0X( L"/run1/3.b03", 3 );

	//TestFileB0X2Jpg2B0X(  L"/run1/0.b03", 3 );
	//TestFileB0X2Jpg2B0X(  L"/run1/11.b03", 3 );
	//TestFileB0X2Jpg2B0X(  L"/run1/2.b00", 0 );

	//std::wstring resultName;
	//TestFileJpg2B0X( L"/run1/0.b03.jpg", 3, L"/run2/0.b03");
	//TestFileJpg2B0X( L"/run1/1.b03.jpg", 3, L"/run2/1.b03" );
	//TestFileJpg2B0X( L"/run1/10.b03.jpg", 3, L"/run2/10.b03");
	//TestFileJpg2B0X( L"/run1/11.b03.jpg", 3, L"/run2/11.b03");

	//TestFileB0X2Jpg2B0X(  L"/run1/0.b06", 6 );
	//TestFileB0X2Jpg2B0X(  L"/run1/1.b06", 6 );
	//TestFileP0X2Jpg2P0X(  L"/run1/2.p06", 6 );
	//TestFileP0X2Jpg2P0X(  L"/run1/3.p06", 6 );

	TestFileB0X2Jpg2B0X(  L"/run1/0.b00", 0 );
	TestFileB0X2Jpg2B0X(  L"/run1/1.b02", 2 );

	PFS::CEnv::Unmount( L"/run1" );
	//PFS::CEnv::Unmount( L"/run2" );
}

void TestZipTranslator()
{
	// Initialize only once for this kind of test
	//PFSX::ZIP::Initialize();

	// Mount file system once for the 2 tests.
	bool b = PFS::CEnv::Mount( L"/run1", L"D:\\temp", PFS::FST_NATIVE, PFS::MT_WRITABLE);
	if( !b)
	{
		return;
	}

	PFS::CEnv::CheckMetaInfo( L"/run1" );
		 
	// Test 1:
	TestZipEncodeFile();

	// Test 2:
	TestZipDecodeFile();

	PFS::CEnv::Unmount( L"/run1");
}

void TestX20EncodeFile( const std::wstring& dirName, const std::wstring& fileName )
{
	PFSX::TSID DecoderEncoder = PFSX::MakeTypeIDByName('X','2','O');
	PFS::FILEPFSXATTRIB attrib = PFSX::MakeFilePfsxAttributes( DecoderEncoder, 0 );
	PFS::CMetaInfo metainfo;
	metainfo.SetFilePfsxAttributes( attrib );
	std::wstring fileName2 = dirName + L"/x2o" + fileName + L".x2o";
	{
		PFS::CEnv::SetFileMetaInfo(dirName + fileName, metainfo );
		
		DWORD timeElapse = timeGetTime();
		if( !PFSX::EncodeFile( dirName + fileName, fileName2 ) )
		{
			std::wcout<<L"Failed to encode file: "<<fileName<<std::endl;
		}
		else
		{
			timeElapse = timeGetTime() - timeElapse;
			std::wcout<< fileName<< L" encoded to "<< fileName2 << L" successfully - " << timeElapse << L" ms" << std::endl;
		}
	}
}

void TestX20DecodeFile()
{
	PFSX::TSID DecoderEncoder = PFSX::MakeTypeIDByName('X','2','O');
	PFS::FILEPFSXATTRIB attrib = PFSX::MakeFilePfsxAttributes( 0, DecoderEncoder );
	PFS::CMetaInfo metainfo2;
	metainfo2.SetFilePfsxAttributes( attrib );
	{
		using PFSX::CFileEx;
		std::wstring fileName = L"/run-xml/fightorderinfo.xml.x2o";
		PFS::CEnv::SetFileMetaInfo(fileName, metainfo2 );
		CFileEx fileEx;
		if( !fileEx.Open( fileName, PFS::FM_EXCL, PFS::FA_RDONLY ) )
		{
			std::wcout<<L"failed to open file: "<<fileName<<std::endl;
		}
		else
		{
			CopyFileEx( &fileEx, fileName + L".xml" );
			std::wcout<<L"File "<<fileName<< L"was translated successfully!" << std::endl;
		}
	}
}

void TestX2OTranslator()
{
	bool b = PFS::CEnv::Mount( L"/run-xml", L"D:\\temp\\cfg", PFS::FST_NATIVE, PFS::MT_WRITABLE);
	if( !b)
	{
		return;
	}

	PFS::CEnv::CheckMetaInfo( L"/run-xml" );

	PFS::CMetaInfoMap filelist;
	PFS::CEnv::GetMetaInfos( L"/run-xml", filelist );
	PFS::CMetaInfoMap::const_iterator itr = filelist.begin();

	DWORD timeElapse = timeGetTime();

	for ( ; itr != filelist.end(); ++ itr )
	{
		TestX20EncodeFile( L"/run-xml", itr->first );
	}

	timeElapse = timeGetTime() - timeElapse;

	std::wcout<< L"total times: " << timeElapse << L" ms == " << timeElapse /1000 << L" s " << std::endl;

	PFS::CEnv::Unmount( L"/run-xml");
}

void TestTranslatorsDll()
{
	std::wcout<<L"TestTranslatorsDll() START"<<std::endl;

	PFSX::CTranslateManager::GetInstance();

	std::wcout<<L"TestTranslatorsDll() END"<<std::endl;

	return ;
}

void TestTranslatorsDll2()
{
	std::wcout<<L"TestTranslatorsDll() START"<<std::endl;

	HMODULE hDll = LoadLibrary( TEXT("translators\\debug\\pfsx-translators.dll") );
	if ( hDll == NULL )
	{
		std::wcout<<L"LoadLibrary(translatorsd.dll) failed!"<<std::endl;

		std::wcout<<L"TestTranslatorsDll() END"<<std::endl;

		return ;
	}

	typedef long (*TRANS_GetTransCountT)();
	typedef void* (*TRANS_GetTransItemT)(long iIndex);
	TRANS_GetTransCountT pfn_TRANS_GetDecoderCount = (TRANS_GetTransCountT)GetProcAddress( hDll, "TRANS_GetDecoderCount" );
	if ( pfn_TRANS_GetDecoderCount == NULL )
	{
		return;
	}
	
	TRANS_GetTransCountT pfn_TRANS_GetEncoderCount = (TRANS_GetTransCountT)GetProcAddress( hDll, "TRANS_GetEncoderCount" );
	if ( pfn_TRANS_GetEncoderCount == NULL )
	{
		return;
	}

	TRANS_GetTransItemT pfn_TRANS_GetDecoderItem = (TRANS_GetTransItemT)GetProcAddress( hDll, "TRANS_GetDecoderItem" );
	if ( pfn_TRANS_GetDecoderItem  == NULL )
	{
		return;
	}

	TRANS_GetTransItemT pfn_TRANS_GetEncoderItem = (TRANS_GetTransItemT)GetProcAddress( hDll, "TRANS_GetEncoderItem" );
	if ( pfn_TRANS_GetEncoderItem == NULL )
	{
		return;
	}

	//
	// All functions OK!
	//

	int loopCount = 2;
	while ( loopCount -- )
	{
		using namespace PFSX;

		std::wcout<< L"Decoders: "<< std::endl;
		for ( long iIndex = 0; ; iIndex ++ )
		{
			CDecoder * pDecoder = (CDecoder *)pfn_TRANS_GetDecoderItem( iIndex );
			if ( pDecoder != NULL )
			{
				TSID tsid = pDecoder->GetID();
				std::wcout
					<< (char)(tsid &0xFF) 
					<< (char)((tsid &0xFF00) >> 8) 
					<< (char)((tsid &0xFF0000)>>16) 
					<< (char)((tsid &0xFF000000)>>24) 
					<< std::endl;
				pDecoder->Delete();
			}
			else
			{
				std::wcout<< L"NULL Decoder for index :" << iIndex << L" - quit!" << std::endl;
				break;
			}
		}

		std::wcout<< L"Encoders: "<< std::endl;

		for ( long iIndex = 0; ; iIndex ++ )
		{
			CEncoder * pEncoder = (CEncoder *)pfn_TRANS_GetEncoderItem( iIndex );
			if ( pEncoder != NULL )
			{
				TSID tsid = pEncoder->GetID();
				std::wcout
					<< (char)(tsid &0xFF) 
					<< (char)((tsid &0xFF00) >> 8) 
					<< (char)((tsid &0xFF0000)>>16) 
					<< (char)((tsid &0xFF000000)>>24) 
					<< std::endl;
				pEncoder->Delete();
			}
			else
			{
				std::wcout<< L"NULL Encoder for index :" << iIndex << L" - quit!" << std::endl;
				break;
			}
		}

	}

	loopCount = 2;

	while ( loopCount -- )
	{
		using namespace PFSX;

		std::wcout<< L"Decoders: "<< std::endl;
		long lDecoderCount = pfn_TRANS_GetDecoderCount();
		for ( long iIndex = -1; iIndex <= lDecoderCount; iIndex ++ )
		{
			CDecoder * pDecoder = (CDecoder *)pfn_TRANS_GetDecoderItem( iIndex );
			if ( pDecoder != NULL )
			{
				TSID tsid = pDecoder->GetID();
				std::wcout
					<< (char)(tsid &0xFF) 
					<< (char)((tsid &0xFF00) >> 8) 
					<< (char)((tsid &0xFF0000)>>16) 
					<< (char)((tsid &0xFF000000)>>24) 
					<< std::endl;
				pDecoder->Delete();
			}
			else
			{
				std::wcout<< L"NULL Decoder for index :" << iIndex << std::endl;
			}
		}
		std::wcout<< L"Encoders: "<< std::endl;
		long lEncoderCount = pfn_TRANS_GetEncoderCount();
		for ( long iIndex = -1; iIndex <= lEncoderCount; iIndex ++ )
		{
			CEncoder * pEncoder = (CEncoder *)pfn_TRANS_GetEncoderItem( iIndex );
			if ( pEncoder != NULL )
			{
				TSID tsid = pEncoder->GetID();
				std::wcout
					<< (char)(tsid &0xFF) 
					<< (char)((tsid &0xFF00) >> 8) 
					<< (char)((tsid &0xFF0000)>>16) 
					<< (char)((tsid &0xFF000000)>>24) 
					<< std::endl;
				pEncoder->Delete();
			}
			else
			{
				std::wcout<< L"NULL Encoder for index :" << iIndex << std::endl;
			}
		}

	}

	FreeLibrary( hDll );
	std::wcout<<L"TestTranslatorsDll() END"<<std::endl;
}


void TestBDFDiffFile( const std::wstring& dirName, const std::wstring& fileName )
{
	PFSX::TSID DecoderEncoder = PFSX::MakeTypeIDByName('B','D','F');
	PFS::FILEPFSXATTRIB attrib = PFSX::MakeFilePfsxAttributes( DecoderEncoder, 0 );
	PFS::CMetaInfo metainfo;
	metainfo.SetFilePfsxAttributes( attrib );
	std::wstring fileName1 = dirName + L".1" + fileName;
	std::wstring fileName2 = dirName + L".2" + fileName;
	std::wstring fileName3 = dirName + L".diff" + fileName;
	{
		PFS::CEnv::AddFileMetaInfo( fileName3, metainfo );
		
		DWORD timeElapse = timeGetTime();
		if( !PFSX::DiffFile( fileName1, fileName2, fileName3 ) )
		{
			std::wcout<<L"Failed to diff file: "<<fileName1 << L" and " << fileName2 <<std::endl;
		}
		else
		{
			timeElapse = timeGetTime() - timeElapse;
			std::wcout<< L" diff " << fileName2 << L" from "<< fileName1 << L" to " << fileName3 << L" successfully - " << timeElapse << L" ms" << std::endl;
		}
	}
}


void TestBDFPatchFile( const std::wstring& dirName, const std::wstring& fileName )
{
	PFSX::TSID DecoderEncoder = PFSX::MakeTypeIDByName('B','D','F');
	PFS::FILEPFSXATTRIB attrib = PFSX::MakeFilePfsxAttributes( DecoderEncoder, 0 );
	PFS::CMetaInfo metainfo;
	metainfo.SetFilePfsxAttributes( attrib );
	std::wstring fileName1 = dirName + L".1" + fileName;
	std::wstring fileName2 = dirName + L".diff" + fileName;
	std::wstring fileName3 = dirName + L".new" + fileName;
	{
		PFS::CEnv::SetFileMetaInfo(fileName1, metainfo );
		PFS::CEnv::SetFileMetaInfo(fileName2, metainfo );
		
		DWORD timeElapse = timeGetTime();
		//if( !PFSX::PatchFile( fileName1, fileName2, fileName3 ) )
		if( !PFSX::DecodeFile( fileName2, fileName1 ) )
		{
			std::wcout<<L"Failed to encode file: "<<fileName<<std::endl;
		}
		else
		{
			timeElapse = timeGetTime() - timeElapse;
			std::wcout<< fileName<< L" encoded to "<< fileName2 << L" successfully - " << timeElapse << L" ms" << std::endl;
		}
	}
}

void TestBDFDiff()
{
	bool b = PFS::CEnv::Mount( L"/run.1", L"D:\\temp\\cfg.1", PFS::FST_NATIVE, PFS::MT_WRITABLE);
	if( !b)
	{
		return;
	}

	b = PFS::CEnv::Mount( L"/run.2", L"D:\\temp\\cfg.2", PFS::FST_NATIVE, PFS::MT_WRITABLE);
	if( !b)
	{
		return;
	}

	b = PFS::CEnv::Mount( L"/run.diff", L"D:\\temp\\cfg.pch", PFS::FST_NATIVE, PFS::MT_WRITABLE);
	if( !b)
	{
		return;
	}

	PFS::CEnv::CheckMetaInfo( L"/run.1" );
	PFS::CEnv::CheckMetaInfo( L"/run.2" );

	PFS::CMetaInfoMap filelist;
	PFS::CEnv::GetMetaInfos( L"/run.1", filelist );
	PFS::CMetaInfoMap::const_iterator itr = filelist.begin();

	DWORD timeElapse = timeGetTime();

	for ( ; itr != filelist.end(); ++ itr )
	{
		TestBDFDiffFile( L"/run", itr->first );
	}

	timeElapse = timeGetTime() - timeElapse;

	std::wcout<< L"total times: " << timeElapse << L" ms == " << timeElapse /1000 << L" s " << std::endl;

	PFS::CEnv::Unmount( L"/run.diff");
	PFS::CEnv::Unmount( L"/run.2");
	PFS::CEnv::Unmount( L"/run.1");
}

void TestBDFPatch()
{
	bool b = PFS::CEnv::Mount( L"/run.1", L"D:\\temp\\cfg.1", PFS::FST_NATIVE, PFS::MT_WRITABLE);
	if( !b)
	{
		return;
	}

	b = PFS::CEnv::Mount( L"/run.diff", L"D:\\temp\\cfg.pch", PFS::FST_NATIVE, PFS::MT_WRITABLE);
	if( !b)
	{
		return;
	}
	b = PFS::CEnv::Mount( L"/run.new", L"D:\\temp\\cfg.new", PFS::FST_NATIVE, PFS::MT_WRITABLE);
	if( !b)
	{
		return;
	}

	PFS::CEnv::CheckMetaInfo( L"/run.1" );
	PFS::CEnv::CheckMetaInfo( L"/run.diff" );

	PFS::CMetaInfoMap filelist;
	PFS::CEnv::GetMetaInfos( L"/run.diff", filelist );
	PFS::CMetaInfoMap::const_iterator itr = filelist.begin();

	DWORD timeElapse = timeGetTime();

	for ( ; itr != filelist.end(); ++ itr )
	{
		TestBDFPatchFile( L"/run", itr->first );
	}

	timeElapse = timeGetTime() - timeElapse;

	std::wcout<< L"total times: " << timeElapse << L" ms == " << timeElapse /1000 << L" s " << std::endl;

	PFS::CEnv::Unmount( L"/run.new");
	PFS::CEnv::Unmount( L"/run.diff");
	PFS::CEnv::Unmount( L"/run.1");
}


void Test()
{
	//TestByteOrder();

	//std::wcout<<L"TRANSTEST::Test() START"<<std::endl;

	//TestZipTranslator();
	//TestX2OTranslator();
	
	//TestFileEx();
	//TestTranslatorsDll();
	//TestTranslatorsDll2();
	//TestFileOpenBmp();

	TestBDFDiff();

	TestBDFPatch();
	

	//std::wcout<<L"TRANSTEST::Test() DONE!"<<std::endl;
}

}// TRANTEST