#include "PicPakConverter.h"
#include "PicPak.h"
#include "IImageEncoder.h"
#include "../../pfs/pfs2.h"
#include <iostream>
#include <sstream>

namespace MHZX
{
	bool CPicPakConverter::Convert(
		CPicPak& rSourcePak, CPicPak& rDestPak, IImageEncoder* pEncoder, bool bVerbose )
	{
		size_t total = rSourcePak.GetPictureCount();

		PFS::CDeviceFile file;
		PFS::CDevice& rDevice = rDestPak.GetDevice();
	
		std::wstringstream wss;
		size_t failed = 0;
		size_t succeeded = 0;
		size_t skipped = 0;
		
		for( size_t i = 0; i < total; i ++ )
		{
			CPicture* pPic = rSourcePak.GetPicture( i );
			if( pPic != NULL )
			{
				// NOTE:
				// Add it whether succeeds or not for generating pak.meta.xml to keep the index of the pictures.
				// Because the MIX engine takes the index as the ID of the picture and also as frame index of the animations.
				//
				// yanghaibo - 2010-11-26
				CPicture* pPngPicture = new CPicture();
				pPngPicture->m_PictureDesc = pPic->m_PictureDesc;
				rDestPak.AddPicture( pPngPicture ); 

				if ( pPic->m_PictureDesc.filename.empty() )
				{
					std::wcerr << L"Warn: skipped a dummy file - id: " << i << std::endl;
					skipped ++;
					continue;
				}

				wss.str(L"");

				std::wstring filename;

				// Use the encoder to replace the file extension automatically.
				pEncoder->GenerateNewFileName( (unsigned int)i, pPic->m_PictureDesc.filename, filename );
				
				wss << pPic->m_PictureDesc.filename << L" -> " << filename;

				if( !rSourcePak.InitPicture( i ) )
				{
					wss << L" -- InitPicture FAILED!" << std::endl;
					std::wcerr << wss.str();

					failed ++;
					continue;
				}

				if( file.Open( &rDevice, filename, PFS::FM_CREAT | PFS::FM_TRUNC, PFS::FA_WRONLY ) )
				{
					if( !pEncoder->Open() )
					{
						wss << L" -- Encoder Init FAILED!" << std::endl;
						std::wcerr << wss.str();

						failed ++;
						continue;
					}

					unsigned int rNewFormat = 0;
					if( pEncoder->Encode( *pPic, file, rNewFormat ) )
					{
						if( bVerbose )
						{
							wss << L" -- OK!" << std::endl;
							std::wcout << wss.str();
						}

						succeeded ++;

						// Reset some picture descriptions.
						pPngPicture->m_PictureDesc.filename = filename; // reset filename
						pPngPicture->m_PictureDesc.format = LOWORD(rNewFormat); // reset format
						pPngPicture->m_PictureDesc.storage = HIWORD(rNewFormat); // reset storage
					}
					else
					{
						wss << L" -- Encode FAILED!" << std::endl;
						std::wcerr << wss.str();
						failed ++;
					}
					
					pEncoder->Close();

					file.Close();
				}
				else
				{
					failed ++;
					wss << L" -- File OPEN FAILED!" << std::endl;
					std::wcerr << wss.str();
				}
			}
		}

		std::wcout << L"-------------------------------------" << std::endl;
		std::wcout << L"  total     : " << total << std::endl;
		std::wcout << L"  succeeded : " << succeeded << std::endl;
		std::wcout << L"  skipped   : " << skipped << std::endl;
		std::wcout << L"  failed    : " << failed << std::endl;
		std::wcout << L"-------------------------------------" << std::endl;

		return true;
	}

} // namespace MHZX

