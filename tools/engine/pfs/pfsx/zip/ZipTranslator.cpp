//////////////////////////////////////////////////////////////////////////
//
// ZipTranslator.cpp
//
// 新增 ZipTranslater 方案
// 1. 新增专门用于安装/更新时直接Copy压缩文件的方案(Zpd == Zipped),并修改该方案的缺省压缩级别(level)
// 2. 新增专门用于DDS文件的压缩方案(暂时同Zip方案)
//
// [5/9/2009 yanghaibo]
//
// Add BZF encoder and decoder which need be processed by binary difference tool especially.
// yanghaibo 2010-05-21 
//
#include "ZipTranslator.h"
#include "../../pfs2.h"
#include "../../contrib/zlib-1.2.3/zlib.h"

namespace PFSX {

	namespace ZIP {

		class CZipEncoder : public CEncoder
		{
		public:
			CZipEncoder() 
				: CEncoder( MakeTypeIDByName('Z', 'I', 'P') )
			{
			}

			virtual int DoEncode();
			virtual CEncoder* Clone() const
			{
				return new CZipEncoder();
			}

			virtual void Delete() { delete this; }
		};

		class CZpdEncoder : public CEncoder
		{
		public:
			CZpdEncoder() 
				: CEncoder( MakeTypeIDByName('Z', 'P', 'D' ) )
			{
			}

			virtual int DoEncode();
			virtual CEncoder* Clone() const
			{
				return new CZpdEncoder();
			}

			virtual void Delete() { delete this; }
		};

		class CDDSEncoder : public CEncoder
		{
		public:
			CDDSEncoder() 
				: CEncoder( MakeTypeIDByName('D', 'D', 'S') )
			{
			}

			virtual int DoEncode();
			virtual CEncoder* Clone() const
			{
				return new CDDSEncoder();
			}

			virtual void Delete() { delete this; }
		};

		class CBZFEncoder : public CEncoder
		{
		public:
			CBZFEncoder() 
				: CEncoder( MakeTypeIDByName('B', 'Z', 'F') )
			{
			}

			virtual int DoEncode();
			virtual CEncoder* Clone() const
			{
				return new CBZFEncoder();
			}

			virtual void Delete() { delete this; }
		};

		class CZipDecoder : public CDecoder 
		{
		public:
			CZipDecoder ()
				: CDecoder( MakeTypeIDByName('Z', 'I', 'P') )
			{
			}

			virtual int DoDecode();

			virtual CDecoder* Clone() const
			{
				return new CZipDecoder();
			}

			virtual void Delete() { delete this; }
		};

		class CZpdDecoder : public CDecoder 
		{
		public:
			CZpdDecoder ()
				: CDecoder( MakeTypeIDByName('Z', 'P', 'D') )
			{
			}

			virtual int DoDecode();

			virtual CDecoder* Clone() const
			{
				return new CZpdDecoder();
			}

			virtual void Delete() { delete this; }
		};

		class CDDSDecoder : public CDecoder 
		{
		public:
			CDDSDecoder ()
				: CDecoder( MakeTypeIDByName('D', 'D', 'S') )
			{
			}

			virtual int DoDecode();

			virtual CDecoder* Clone() const
			{
				return new CDDSDecoder();
			}

			virtual void Delete() { delete this; }
		};

		class CBZFDecoder : public CDecoder 
		{
		public:
			CBZFDecoder ()
				: CDecoder( MakeTypeIDByName('B', 'Z', 'F') )
			{
			}

			virtual int DoDecode();

			virtual CDecoder* Clone() const
			{
				return new CBZFDecoder();
			}

			virtual void Delete() { delete this; }
		};

		class _CZipTranslatorAutoRegister
		{
		public:
			_CZipTranslatorAutoRegister()
			{
				CTranslateManager::GetInstance().RegisterEncoder( CZipEncoder() );
				CTranslateManager::GetInstance().RegisterDecoder( CZipDecoder() );
				CTranslateManager::GetInstance().RegisterEncoder( CZpdEncoder() );
				CTranslateManager::GetInstance().RegisterDecoder( CZpdDecoder() );
				CTranslateManager::GetInstance().RegisterEncoder( CDDSEncoder() );
				CTranslateManager::GetInstance().RegisterDecoder( CDDSDecoder() );
				CTranslateManager::GetInstance().RegisterEncoder( CBZFEncoder() );
				CTranslateManager::GetInstance().RegisterDecoder( CBZFDecoder() );
			}
		}; //for _autoRegister;

		int Initialize()
		{
			static _CZipTranslatorAutoRegister _autoRegister;
			return PFS::EC_SUCCESS;
		}

		long GetEncoderCount()
		{
			return 4;
		}

		void* GetEncoderItem(long lIndex)
		{
			if ( lIndex == 0 )
			{
				return CZipEncoder().Clone();
			}
			else if ( lIndex == 1 )
			{
				return CZpdEncoder().Clone();
			}
			else if ( lIndex == 2 )
			{
				return CDDSEncoder().Clone();
			}
			else if ( lIndex == 3 )
			{
				return CBZFEncoder().Clone();
			}
			else
			{
				return NULL;
			}
		}

		long GetDecoderCount()
		{
			return 4;
		}

		void* GetDecoderItem(long lIndex)
		{
			if ( lIndex == 0 )
			{
				return CZipDecoder().Clone();
			}
			else if ( lIndex == 1 )
			{
				return CZpdDecoder().Clone();
			}
			else if ( lIndex == 2 )
			{
				return CDDSDecoder().Clone();
			}
			else if ( lIndex == 3 )
			{
				return CBZFDecoder().Clone();
			}
			else
			{
				return NULL;
			}
		}

		using PFS::CBaseFile;

		template<int level>
		static int _PerformEncode( CBaseFile& source, CBaseFile& destination )
		{
			fsize_t total = source.GetSize();
			fsize_t remaining = total;
			const size_t BLOCK = 4096;
			const size_t BLOCK_ZIPPED = 8192;
			char buffer[BLOCK];
			char buffer_zipped[BLOCK_ZIPPED];

			z_stream strm;
			strm.zalloc = Z_NULL;    
			strm.zfree = Z_NULL;    
			strm.opaque = Z_NULL;
			strm.avail_in = 0;    
			strm.next_in = Z_NULL;

			int ret = deflateInit2( &strm, level, Z_DEFLATED, -MAX_WBITS, 8, Z_DEFAULT_STRATEGY);
			if( ret != Z_OK )
			{
				return ret;
			}

			size_t current = 0;
			while ( remaining > 0 )
			{
				current = source.Read(buffer, BLOCK );
				assert( current != 0 );

				remaining -= current;

				strm.avail_in = (uInt)current;
				strm.next_in = (Bytef*)buffer;

				//bool bFinish = remaining == 0;

				// normally this loop will do only once
				// Note: we should check the avail_in ==0 but not the avail_out !=0 as the quit condition.
				// because in some cases, the avail_out == 0 and also the avail_in == 0,
				// in this case, inflate will failed with -5.
				while( true )
				{
					strm.avail_out = BLOCK_ZIPPED;
					strm.next_out = (Bytef*)buffer_zipped;

					ret = deflate(&strm, Z_SYNC_FLUSH);
					if( ret == Z_BUF_ERROR)
					{
						assert( strm.avail_in == 0 && strm.avail_out == BLOCK_ZIPPED);
						break;
					}
					if( (ret < Z_OK) || (ret == Z_NEED_DICT))	// error
					{
						break;
					}
					size_t have = BLOCK_ZIPPED - strm.avail_out;
					destination.Write( buffer_zipped, have );

					// Note: in some cases, when ret == Z_STREAM_END, 
					// but avail_in is still not 0, and if we don't break here,
					// it will cause a dead-loop.
					if ( ret == Z_STREAM_END )
					{
						break;
					}
				}
			}

			strm.avail_in = 0;
			strm.next_in = NULL;
			strm.avail_out = BLOCK_ZIPPED;
			strm.next_out = (Bytef*)buffer_zipped;
			ret = deflate( &strm, Z_FINISH);
			if( ret < Z_OK)
				return ret;
			size_t have = BLOCK_ZIPPED - strm.avail_out;
			destination.Write( buffer_zipped, have );
			deflateEnd( &strm);

			return ret;
		}
		
		//
		// Encode a data file to Zipped file
		//
		int CZipEncoder::DoEncode()
		{
			CBaseFile& source = GetSource();
			CBaseFile& destination = GetDestination();

			int zRet =  _PerformEncode<Z_BEST_COMPRESSION>( source, destination );
			if( zRet < Z_OK )
			{
				return PFS::ECX_BADFORMAT;
			}
			else
			{
				return PFS::EC_SUCCESS;
			}
		}

		int CZpdEncoder::DoEncode()
		{
			CBaseFile& source = GetSource();
			CBaseFile& destination = GetDestination();

			int zRet =  _PerformEncode<Z_BEST_SPEED>( source, destination );
			if( zRet < Z_OK )
			{
				return PFS::ECX_BADFORMAT;
			}
			else
			{
				return PFS::EC_SUCCESS;
			}
		}

		int CDDSEncoder::DoEncode()
		{
			CBaseFile& source = GetSource();
			CBaseFile& destination = GetDestination();

			int zRet =  _PerformEncode<Z_BEST_COMPRESSION>( source, destination );
			if( zRet < Z_OK )
			{
				return PFS::ECX_BADFORMAT;
			}
			else
			{
				return PFS::EC_SUCCESS;
			}
		}
		int CBZFEncoder::DoEncode()
		{
			CBaseFile& source = GetSource();
			CBaseFile& destination = GetDestination();

			int zRet =  _PerformEncode<Z_BEST_COMPRESSION>( source, destination );
			if( zRet < Z_OK )
			{
				return PFS::ECX_BADFORMAT;
			}
			else
			{
				return PFS::EC_SUCCESS;
			}
		}

		static int _PerformDecode( CBaseFile& source, CBaseFile& destination )
		{
			fsize_t total = source.GetSize();
			fsize_t remaining = total;
			const size_t BLOCK = 1024;
			const size_t BLOCK_UNZIPPED = 8192;
			char buffer[BLOCK];
			char buffer_unzipped[BLOCK_UNZIPPED];

			z_stream strm;
			strm.zalloc = Z_NULL;
			strm.zfree = Z_NULL; 
			strm.opaque = Z_NULL;
			strm.avail_in = 0;
			strm.next_in = Z_NULL;
					
			int ret = inflateInit2( &strm, -MAX_WBITS);
			if( ret != Z_OK )
			{
				return ret;
			}

			size_t current = 0;
			while ( remaining > 0 )
			{
				current = source.Read(buffer, BLOCK );
				if ( current == 0 )
				{
					assert( current != 0 );
					break;
				}
				remaining -= current;
				bool bFinish = remaining==0;

				strm.avail_in = (uInt)current;
				strm.next_in = (Bytef*)buffer;

				// normally this loop will do only once
				// Note: we should check the avail_in ==0 but not the avail_out !=0 as the quit condition.
				// because in some cases, the avail_out == 0 and also the avail_in == 0,
				// in this case, inflate will failed with -5.
				while( true) 
				{
					strm.avail_out = BLOCK_UNZIPPED;
					strm.next_out = (Bytef*)buffer_unzipped;

					ret = inflate( &strm, Z_SYNC_FLUSH);
					if( ret == Z_BUF_ERROR)
					{
						assert( strm.avail_in == 0 && strm.avail_out == BLOCK_UNZIPPED);
						break;
					}
					if( (ret < Z_OK) || (ret == Z_NEED_DICT))	// error
					{
						break;
					}
					size_t have = BLOCK_UNZIPPED - strm.avail_out;
					destination.Write( buffer_unzipped, have );

					// Note: in some cases, when ret == Z_STREAM_END, 
					// but avail_in is still not 0, and if we don't break here,
					// it will cause a dead-loop.
					if ( ret == Z_STREAM_END )
					{
						break;
					}
				} 
			}

			inflateEnd( &strm);

			return ret;
		}

		static bool _CopyBaseFile( CBaseFile& source, CBaseFile& dest )
		{
			CBaseFile*	fsrc = &source;
			CBaseFile*	fdst = &dest;
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

		int CZipDecoder::DoDecode()
		{
			CBaseFile& source = GetSource();
			CBaseFile& destination = GetDestination();

			int zRet =  _PerformDecode( source, destination );
			if( zRet < Z_OK )
			{
				return PFS::EC_FILE_BAD_ZIPDATA;
			}
			else
			{
				return PFS::EC_SUCCESS;
			}
		}

		//						  
		// copy a zipped file to destination file
		//
		int CZpdDecoder::DoDecode()
		{
			CBaseFile& source = GetSource();
			CBaseFile& destination = GetDestination();
		
			bool bRet = _CopyBaseFile( source, destination );
			if( !bRet )
			{
				return PFS::CEnv::GetLastError();
			}
			else
			{
				return PFS::EC_SUCCESS;
			}
		}

		int CDDSDecoder::DoDecode()
		{
			CBaseFile& source = GetSource();
			CBaseFile& destination = GetDestination();

			int zRet =  _PerformDecode( source, destination );
			if( zRet < Z_OK )
			{
				return PFS::EC_FILE_BAD_ZIPDATA;
			}
			else
			{
				return PFS::EC_SUCCESS;
			}
		}

		int CBZFDecoder::DoDecode()
		{
			CBaseFile& source = GetSource();
			CBaseFile& destination = GetDestination();

			int zRet =  _PerformDecode( source, destination );
			if( zRet < Z_OK )
			{
				return PFS::EC_FILE_BAD_ZIPDATA;
			}
			else
			{
				return PFS::EC_SUCCESS;
			}
		}
	}
}
