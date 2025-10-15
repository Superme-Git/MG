#include "BDFTranslator.h"
#include "../../pfs2.h"
#include "../../src/native/rawfileio.h"
#include "../../src/native/tempfile.h"
#include "bsdiff/bsdiff.h"

namespace PFSX {

	namespace BDF {

		class CBDFEncoder : public CEncoder
		{
		public:
			CBDFEncoder() 
				: CEncoder( MakeTypeIDByName('B', 'D', 'F') )
			{
			}

			virtual int DoEncode();
			virtual CEncoder* Clone() const
			{
				return new CBDFEncoder();
			}

			virtual void Delete() { delete this; }
		};
		
		class CBDFDecoder : public CDecoder
		{
		public:
			CBDFDecoder() 
				: CDecoder( MakeTypeIDByName('B', 'D', 'F') )
			{
			}

			virtual int DoDecode();
			virtual CDecoder* Clone() const
			{
				return new CBDFDecoder();
			}

			virtual void Delete() { delete this; }
		};

		// 生成差分文件: newFile - oldFile -> diffFile
		int _PerformDiff( PFS::CBaseFile& oldFile, PFS::CBaseFile& newFile, PFS::CBaseFile& diffFile );

		// 使用差分文件生成新文件: oldFile + diffFile -> newFile
		int _PerformPatch( PFS::CBaseFile& oldFile, PFS::CBaseFile& diffFile, PFS::CBaseFile& newFile );

		int CBDFEncoder::DoEncode()
		{
			PFS::CBaseFile& oldFile = GetSource();
			PFS::CBaseFile* newFile = GetSource2();
			PFS::CBaseFile& diffFile = GetDestination();

			int zRet = _PerformDiff( oldFile, *newFile, diffFile );
			if( zRet != 0 )
			{
				return PFS::ECX_BADFORMAT;
			}
			else
			{
				return PFS::EC_SUCCESS;
			}
		}

		int CBDFDecoder::DoDecode()
		{
			PFS::CBaseFile& oldFile = GetSource();
			PFS::CBaseFile* diffFile = GetSource2();
			PFS::CBaseFile& newFile = GetDestination();

			int zRet = _PerformPatch( oldFile, *diffFile, newFile );
			if( zRet != 0 )
			{
				return PFS::ECX_BADFORMAT;
			}
			else
			{
				return PFS::EC_SUCCESS;
			}
		}
		
		static bool _CopyBaseFile( PFS::CBaseFile& source, PFS::CBaseFile& dest )
		{
			PFS::CBaseFile*	fsrc = &source;
			PFS::CBaseFile*	fdst = &dest;
			const int	nBufferSize = 4096;
			unsigned char byteBuffer[nBufferSize];
			bool		bResult = true;
			size_t		s = 0;
			while( s = fsrc->Read( byteBuffer, nBufferSize ) )
			{
				if( PFS::FILE_SIZE_ERROR == s )
					return false;
				s = fdst->Write( byteBuffer, s);
				if( PFS::FILE_SIZE_ERROR == s )
					return false;
			}

			return true;
		}

		static bool WideToMultiByte( const std::wstring& _src, std::string& des)
		{
			des.resize( _src.size());
			int len = ::WideCharToMultiByte( CP_ACP, 0, 
				_src.c_str(), (int)_src.size(), 
				const_cast<LPSTR>( des.c_str()), (int)des.size(), 0, FALSE);
			bool bFailed = ( 0 == len); 
			if (bFailed)
			{
				if( ::GetLastError() == ERROR_INSUFFICIENT_BUFFER)
				{
					int nBufSize = WideCharToMultiByte( CP_ACP, 0,
						_src.c_str(), (int)_src.size(), NULL, 0, 0, FALSE); 
					if( 0 == nBufSize)
						return false;
					des.resize( nBufSize);
					bFailed = ( 0 == ::WideCharToMultiByte( CP_ACP, 0,
						_src.c_str(), (int)_src.size(), 
						const_cast<LPSTR>( des.c_str()), nBufSize, 0, FALSE)); 
				}			
			}
			else
			{
				des.resize( len);
			}
			return !bFailed;
		}

		static bool MultByteToWide( const std::string& _src, std::wstring& des)
		{
			des.resize( _src.size());
			int len = ::MultiByteToWideChar( CP_ACP, 0,
				_src.c_str(), (int)_src.size(), const_cast<LPWSTR>( des.c_str()), (int)des.size());
			bool bFailed = ( 0 == len); 
			if (bFailed)
			{
				if( ::GetLastError() == ERROR_INSUFFICIENT_BUFFER)
				{
					int nBufSize = MultiByteToWideChar( CP_ACP, 0, 
						_src.c_str(), (int)_src.size(), NULL, 0); 
					if( 0 == nBufSize)
						return false;
					des.resize( nBufSize);
					bFailed = ( 0 == ::MultiByteToWideChar( CP_ACP, 0,
						_src.c_str(), (int)_src.size(), 
						const_cast<LPWSTR>( des.c_str()), nBufSize)); 
				}			
			}
			else
			{
				des.resize( len);
			}
			return !bFailed;
		}

		int DiffNativeFile( const wchar_t* oldFile, const wchar_t* newFile, const wchar_t* diffFile )
		{
			std::string oldfile, newfile, difffile;
			WideToMultiByte(oldFile, oldfile);
			WideToMultiByte(newFile, newfile);
			WideToMultiByte(diffFile, difffile );
			enum
			{
				ARGC = 4,
			};

			// 生成差分文件: oldfile + newfile -> patchfile	
			const char* argv[ARGC] = {
				"bsdiff",
				oldfile.c_str(),
				newfile.c_str(),
				difffile.c_str(),
			};

			return bsdiff( ARGC, argv );
		}

		int PatchNativeFile( const wchar_t* oldFile, const wchar_t* diffFile, const wchar_t* newFile )
		{
			std::string oldfile, difffile, newfile;
			WideToMultiByte(oldFile, oldfile);
			WideToMultiByte(diffFile, difffile);
			WideToMultiByte(newFile, newfile );
			enum
			{
				ARGC = 4,
			};

			// 使用差分文件生成新文件: oldfile + patchfile -> newfile		
			const char* argv[ARGC] = {
				"bspatch",
				oldfile.c_str(),
				newfile.c_str(),
				difffile.c_str(),
			};

			return bspatch( ARGC, argv);
		}

		static int _PerformDiff( PFS::CBaseFile& oldFile, PFS::CBaseFile& newFile, PFS::CBaseFile& diffFile )
		{
			std::string oldfile, newfile, patchfile;
			PFS::CTempFile tempOld, tempNew, tempDiff;
			if( !tempOld.Open() )
			{
				// TODO:
				return -1;
			}

			if( !tempNew.Open() )
			{
				// TODO:
				return -2;
			}

			if( !tempDiff.Open() )
			{
				// TODO:
				return -3;
			}			

			if( !_CopyBaseFile( oldFile, tempOld ) )
			{
				return -4;
			}
			
			if( !_CopyBaseFile( newFile, tempNew ) )
			{
				return -5;
			}

			if( diffFile.Seek( 0, PFS::FSM_SET ) != 0 )
			{
				return -6;
			}

			WideToMultiByte(tempOld.GetFileName(), oldfile);
			WideToMultiByte(tempNew.GetFileName(), newfile);
			WideToMultiByte(tempDiff.GetFileName(), patchfile );

			// Close temporary files but do not delete it, for avoid sharing reading/writing problems.
			tempOld.CNativeFile::Close();
			tempNew.CNativeFile::Close();
			tempDiff.CNativeFile::Close();

			enum
			{
				ARGC = 4,
			};

			// 生成差分文件: oldfile + newfile -> patchfile	
			const char* argv[ARGC] = {
				"bsdiff",
				oldfile.c_str(),
				newfile.c_str(),
				patchfile.c_str(),
			};

			if( bsdiff( ARGC, argv) != 0 )
			{
				return -7;
			}

			// 重新Open
			if( !tempDiff.CNativeFile::Open( tempDiff.GetFileName(), PFS::FM_EXCL, PFS::FA_RDONLY ) )
			{
				return -8;
			}

			if( !_CopyBaseFile( tempDiff, diffFile ) )
			{
				return -9;
			}

			return 0;
		}

		static int _PerformPatch( PFS::CBaseFile& oldFile, PFS::CBaseFile& diffFile, PFS::CBaseFile& newFile )
		{
			std::string oldfile, newfile, patchfile;
			PFS::CTempFile tempOld, tempDiff, tempNew;
			if( !tempOld.Open() ) // 创建临时文件，并获得临时文件的名字。
			{
				// TODO:
				return -1;
			}			
			
			if( !tempDiff.Open() ) // 创建临时文件，并获得临时文件的名字。
			{
				// TODO:
				return -2;
			}
			
			if( !tempNew.Open() ) // 创建临时文件，并获得临时文件的名字。
			{
				// TODO:
				return -3;
			}

			if( !_CopyBaseFile( diffFile, tempDiff ) )
			{
				return -4;
			}
			
			if( !_CopyBaseFile( oldFile, tempOld ) )
			{
				return -5;
			}

			if( newFile.Seek( 0, PFS::FSM_SET ) != 0 )
			{
				return -6;
			}

			WideToMultiByte( tempOld.GetFileName(), oldfile );
			WideToMultiByte( tempDiff.GetFileName(), patchfile );
			WideToMultiByte( tempNew.GetFileName(), newfile );
			
			// Close temporary files but do not delete it, for avoid sharing reading/writing problems.
			tempOld.CNativeFile::Close();
			tempDiff.CNativeFile::Close();
			tempNew.CNativeFile::Close();
			
			enum
			{
				ARGC = 4,
			};

			// 使用差分文件生成新文件: oldfile + patchfile -> newfile		
			const char* argv[ARGC] = {
				"bspatch",
				oldfile.c_str(),
				newfile.c_str(),
				patchfile.c_str(),
			};

			if( bspatch( ARGC, argv) != 0 )
			{
				return -7;
			}

			// 重新Open
			bool bRet = tempNew.CNativeFile::Open( tempNew.GetFileName(), PFS::FM_EXCL, PFS::FA_RDONLY );
			if( !bRet )
			{
				return -8;
			}

			if( !_CopyBaseFile( tempNew, newFile ) )
			{
				return -9;
			}

			return 0;
		}

		class _CBDFTranslatorAutoRegister
		{
		public:
			_CBDFTranslatorAutoRegister()
			{
				CTranslateManager::GetInstance().RegisterEncoder( CBDFEncoder() );
				CTranslateManager::GetInstance().RegisterDecoder( CBDFDecoder() );
			}
		}; //for _autoRegister;

		int Initialize()
		{
			static _CBDFTranslatorAutoRegister _autoRegister;
			return PFS::EC_SUCCESS;
		}

		long GetEncoderCount()
		{
			return 1;
		}

		void* GetEncoderItem(long lIndex)
		{
			if ( lIndex == 0 )
			{
				return CBDFEncoder().Clone();
			}
			else
			{
				return NULL;
			}
		}

		long GetDecoderCount()
		{
			return 1;
		}

		void* GetDecoderItem(long lIndex)
		{
			if ( lIndex == 0 )
			{
				return CBDFDecoder().Clone();
			}
			else
			{
				return NULL;
			}
		}

	}
}